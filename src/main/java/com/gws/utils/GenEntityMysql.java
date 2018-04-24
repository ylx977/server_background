package com.gws.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 从数据库表反射出实体类，自动生成实体类
 */
public class GenEntityMysql {

	/**
	 * 指定实体生成所在包的路径
	 */
	private String packageOutPath = "test";
	/**
	 * 作者名字
	 */
	private String authorName = "wangdong";
	/**
	 * 表名,如：order_goods
	 */
	private String tablename = "";
	/**
	 * 处理表名字,如：OrderGoods
	 */
	private String bigTableName ="";
	/**
	 * 列名数组
	 */
	private String[] colnames;
	/**
	 * 列名注释数组
	 */
	private String[] colnameComments;
	/**
	 * 列名类型数组
	 */
	private String[] colTypes;
	/**
	 * 列名大小数组
	 */
	private int[] colSizes;
	/**
	 * 是否需要导入包java.util
	 */
	private boolean f_util = false;
	/**
	 * 是否需要导入包java.sql
	 */
	private boolean f_sql = false;
	/**
	 * 是否需要生成基于注解的JPA实体对象
	 */
	private boolean f_jpa = true;

    //数据库连接
	private static final String URL ="jdbc:mysql://127.0.0.1:3306/testspring";
	private static final String NAME = "root";
	private static final String PASS = "123456";
	private static final String DRIVER ="com.mysql.jdbc.Driver";

	/*
	 * 构造函数
	 */
	public GenEntityMysql() throws Exception{
		List<String> list=getTableName();
		for(int p=0;p<list.size();p++){
			tablename=list.get(p);
			//处理表名字
			bigTableName = initcap(strChange(tablename));
	    	//创建连接
	    	Connection con;
			//查要生成实体类的表
	    	String sql = "select * from " + tablename;
	    	PreparedStatement pStemt = null;
	    	try {
	    		try {
					Class.forName(DRIVER);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
	    		con = DriverManager.getConnection(URL,NAME,PASS);
				pStemt = con.prepareStatement(sql);
				ResultSetMetaData rsmd = pStemt.getMetaData();
				int size = rsmd.getColumnCount();	//统计列
				colnames = new String[size];
				colTypes = new String[size];
				colnameComments = new String[size];
				colSizes = new int[size];
				for (int i = 0; i < size; i++) {
					String colunm = rsmd.getColumnName(i + 1);
					colnames[i] = colunm;
					colTypes[i] = rsmd.getColumnTypeName(i + 1);
					colnameComments[i] = getColumnCommentByTableName(tablename, colunm);
					if(colTypes[i].equalsIgnoreCase("datetime")){
						f_util = true;
					}
					if(colTypes[i].equalsIgnoreCase("image") || colTypes[i].equalsIgnoreCase("text")){
						f_sql = true;
					}
					colSizes[i] = rsmd.getColumnDisplaySize(i + 1);
				}
				String tableComment = getCommentByTableName(tablename);
				String content = parseEntity(colnames,colTypes,colSizes,tablename, tableComment,colnameComments);
				try {
					File directory = new File("");
					String outputPath = directory.getAbsolutePath()+ "/src/main/java/"+this.packageOutPath.replace(".", "/")+"/"+initcap(bigTableName) + ".java";
					FileWriter fw = new FileWriter(outputPath);
					PrintWriter pw = new PrintWriter(fw);
					pw.println(content);
					pw.flush();
					pw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
		System.out.println("生成完毕！");
    }
	
	/**
	  * Java方法 得到当前数据库下所有的表名 
	  * @param con
	  */
	private List<String> getTableName() {
		 List<String> list=new ArrayList<String>();
		 try {  
			 DatabaseMetaData meta = DriverManager.getConnection(URL,NAME,PASS).getMetaData();  
			 ResultSet rs = meta.getTables(null, null, null,new String[]{"TABLE"});
			 while (rs.next()) {
				 list.add(rs.getString(3));  
			 }  
		   }catch(Exception e){  
			   e.printStackTrace();  
		   }
		return list;
	 }
	
	/**
	 * 
	 * 获得系统当前时间
	 * 
	 * @author zengjq 2016年4月9日
	 * @return
	 */
	private  String getDate(){
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyy年MM月dd日 aHH:mm:ss");
		String time=format.format(date);
		return time;
	}
	
	/**
	 * 功能：生成实体类主体代码
	 * @param colnames
	 * @param colTypes
	 * @param colSizes
	 * @return
	 */
	private String parseEntity(String[] colnames, String[] colTypes, int[] colSizes,String tablename, String tableComment, String[] colnameComments) {
		StringBuffer sb = new StringBuffer();

		//导包
		sb.append("package " + this.packageOutPath + ";\r\n");
		sb.append("\r\n");
		
		sb.append("import java.math.BigDecimal;\r\n");
		
		//判断是否导入工具包
		if(f_util){
			sb.append("import java.util.Date;\r\n");
		}
		if(f_sql){
			sb.append("import java.sql.*;\r\n");
		}
		
		//jpa
		if(f_jpa){
			sb.append("import javax.persistence.Column;\r\n");
			sb.append("import javax.persistence.Entity;\r\n");
			sb.append("import javax.persistence.GeneratedValue;\r\n");
			sb.append("import javax.persistence.GenerationType;\r\n");
			sb.append("import javax.persistence.Id;\r\n");
			sb.append("import javax.persistence.Table;\r\n\r\n");
		}
		
		//注释部分
		sb.append("/**\r\n");
		sb.append(" * 【"+ tableComment +"】实体类\r\n");
		sb.append("*\r\n");
		sb.append(" * @version\r\n");
		sb.append(" * @author "+this.authorName+" "+getDate()+"\r\n");
		sb.append(" */ \r\n");
		
		if(f_jpa){
			sb.append("@Entity\r\n");
			sb.append("@Table(name = \"" +tablename + "\") \r\n");
		}
		//实体部分
		sb.append("public class " + initcap(bigTableName) + "{\r\n\r\n");
		processAllAttrs(sb);//属性
		processAllMethod(sb);//get set方法
		sb.append("}\r\n");
		return sb.toString();
	}
	
	/**
	 * 功能：生成所有属性
	 * @param sb
	 */
	private void processAllAttrs(StringBuffer sb) {
		sb.append("\t@Id\r\n");
		for (int i = 0; i < colnames.length; i++) {
			//处理列名
			String bigColname  = strChange(colnames[i] );
			sb.append("\t@Column(name = \"" + colnames[i] + "\")\r\n");
			sb.append("\tprivate " + sqlType2JavaType(colTypes[i]) + " " + bigColname + "; //" + colnameComments[i] +"\r\n\r\n");
		}
		sb.append("\r\n");
	}

	/**
	 * 功能：生成所有方法
	 * @param sb
	 */
	private void processAllMethod(StringBuffer sb) {
		
		for (int i = 0; i < colnames.length; i++) {
			//处理列名
			String bigColname  = strChange(colnames[i] );
			
			if(f_jpa){
				if(i==0){
					sb.append("\tpublic " + sqlType2JavaType(colTypes[i]) + " get" + initcap(bigColname) + "(){\r\n");
				}else{
					sb.append("\tpublic " + sqlType2JavaType(colTypes[i]) + " get" + initcap(bigColname) + "(){\r\n");
				}
			}else{
				sb.append("\tpublic " + sqlType2JavaType(colTypes[i]) + " get" + initcap(bigColname) + "(){\r\n");
			}
			sb.append("\t\treturn " + bigColname+ ";\r\n");
			sb.append("\t}\r\n\r\n");
			sb.append("\tpublic void set" + initcap(bigColname) + "(" + sqlType2JavaType(colTypes[i]) + " " + bigColname + "){\r\n");
			sb.append("\t\tthis." + bigColname + "=" +bigColname + ";\r\n");
			sb.append("\t}\r\n\r\n");
		}
		
	}
	
	/**
	 * 功能：将输入字符串的首字母改成大写
	 * @param str
	 * @return
	 */
	private String initcap(String str) {
		char[] ch = str.toCharArray();
		if(ch[0] >= 'a' && ch[0] <= 'z'){
			ch[0] = (char)(ch[0] - 32);
		}
		return new String(ch);
	}
	
	/**
	 * 首字母转大写
	 * @author nan 2016年4月8日
	 * @param s
	 * @return
	 */
//	 public static String toUpperCaseFirstOne(String s){
//		if(Character.isUpperCase(s.charAt(0)))
//			return s;
//		else
//			return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
//	}
	
	/**
	 * 功能：获得列的数据类型
	 * @param sqlType
	 * @return
	 */
	private String sqlType2JavaType(String sqlType) {
		if(sqlType.equalsIgnoreCase("bit")){
			return "Boolean";
		}else if(sqlType.equalsIgnoreCase("tinyint")){
			return "Integer";
		}else if(sqlType.equalsIgnoreCase("smallint")){
			return "Integer";
		}else if(sqlType.equalsIgnoreCase("int")||sqlType.equalsIgnoreCase("INT UNSIGNED")){
			//INT UNSIGNED无符号整形
			return "Integer";
		}else if(sqlType.equalsIgnoreCase("bigint")){
			return "Long";
		}else if(sqlType.equalsIgnoreCase("float")){
			return "Float";
		}else if( sqlType.equalsIgnoreCase("numeric") 
				|| sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money") 
				|| sqlType.equalsIgnoreCase("smallmoney")){
			return "Double";
		}else if(sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char") 
				|| sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar") 
				|| sqlType.equalsIgnoreCase("text")){
			return "String";
		}else if(sqlType.equalsIgnoreCase("datetime")||sqlType.equalsIgnoreCase("date")||sqlType.equalsIgnoreCase("timestamp")){
			return "Date";
		}else if(sqlType.equalsIgnoreCase("image")){
			return "Blod";
		}else if(sqlType.equalsIgnoreCase("decimal")){
			return "BigDecimal";
		}	
		return null;
	}
	
	//去掉下划线并将下划线后面的首字母变大写
	public  String strChange(String s)
    {
		String str[]=s.split("_");
		String tempString=str[0];
		for (int i = 1; i < str.length; i++) {
			str[i]=initcap(str[i]);
			tempString=tempString+str[i];
		}
		return tempString;
    }
	
	 /** 
     * 获得某表的注释 
     * @param tableName 
     * @return 
     * @throws Exception 
     */  
    public static String getCommentByTableName(String tableName) throws Exception {  
    	try{
		    	//创建连接
		    	Connection con;
				Class.forName(DRIVER);
		    	con = DriverManager.getConnection(URL,NAME,PASS);
		        Statement stmt = con.createStatement();
		        ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + tableName);  
		        String comment="";
		        if (rs != null && rs.next()) {  
		            String createDDL = rs.getString(2);  
		            comment = parse(createDDL);  
		        }  
		        rs.close();  
		        stmt.close();  
		        con.close();  
		        return comment;
    	} catch (Exception e) {
				e.printStackTrace();
				return null;
		}
    }  
    /** 
     * 返回注释信息 
     * @param all 
     * @return 
     */  
      
    public static String parse(String all) {  
        String comment = null;  
        int index = all.indexOf("COMMENT='");  
        if (index < 0) {  
            return "";  
        }  
        comment = all.substring(index + 9);  
        comment = comment.substring(0, comment.length() - 1);  
        return comment;  
    }  
    
    /** 
     * 获得某表中所有字段的注释 
     * @param tableName 
     * @return 
     * @throws Exception 
     */  
    public static String  getColumnCommentByTableName(String tableName, String column) throws Exception {  
	    	//创建连接
	    	Connection con;
			Class.forName(DRIVER);
	    	con = DriverManager.getConnection(URL,NAME,PASS);
	        Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("show full columns from " + tableName);
            String comment = "";
            while (rs.next()) {     
                if(rs.getString("Field").equals(column)){
                	comment = rs.getString("Comment");
                	break;
                }
            }   
	        rs.close(); 
	        stmt.close();
	        con.close();
	        return comment;
    }  
    
	/**
	 * 出口
	 * TODO
	 * @param args
	 * @throws Exception 
	 */
    public static void main(String[] args) throws Exception {
        new GenEntityMysql();
    }
}