package com.gws.utils.sql;

import com.gws.entity.backstage.GoldenWithdraw;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/6/25.
 */
@Component
public class SQLUtilsss {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 返回成功插入数据的条数
     * @param entityList
     * @param <T>
     * @return
     */
    public <T> int executeInsertMany(List<T> entityList) throws IllegalAccessException {
        StringBuilder sqlBuilder = new StringBuilder();
        if(entityList==null||entityList.isEmpty()){
            return 0;
        }
        String tableName = null;
//        List<String> columnNames = Collections.emptyList();
        List<String> columnNames = new ArrayList<>();
        StringBuilder valueSQL = new StringBuilder();
        for (int i = 0;i<entityList.size();i++){
            Class<?> clazz = entityList.get(i).getClass();
            //表名获取到
            if(tableName==null){
                Table annotation = clazz.getAnnotation(Table.class);
                tableName = annotation.name();
            }

            Field[] fields = clazz.getDeclaredFields();

            //把表的列名字都放好
            if(columnNames.isEmpty()){
                for(Field field : fields){
                    Column columnName = field.getAnnotation(Column.class);
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    Object cast = type.cast(field.get(entityList.get(i)));
                    if(columnName==null || cast==null){
                        continue;
                    }else{
                        String name = columnName.name();
                        columnNames.add(name);
                    }
                }
            }


            //获取每个字段的值
            valueSQL.append("(");
            for (Field field : fields) {
                field.setAccessible(true);
                Class<?> type = field.getType();
                Object cast = type.cast(field.get(entityList.get(i)));
                if(cast==null){
                    continue;
                }else{
                    if(cast instanceof String){
                        valueSQL.append("'"+cast.toString()+"',");
                    }else{
                        valueSQL.append(cast+",");
                    }
                }
            }
            valueSQL.append("),");
        }


        sqlBuilder.append("insert into ").append(tableName).append(" (");
        for(String columnName : columnNames){
            sqlBuilder.append(columnName).append(",");
        }
        sqlBuilder.append("), values");
        sqlBuilder.append(valueSQL);


        String sqlString = sqlBuilder.toString().replaceAll(",\\),",")").replaceAll("\\)\\(","\\),\\(");
        //把sql打印下，看是否正确
        System.out.println(sqlString);
        Query nativeQuery = entityManager.createNativeQuery(sqlString);
        int success = nativeQuery.executeUpdate();
        return success;
//        return 1;
    }

    public static void main(String[] args) throws Exception {
//        List<GoldenWithdraw> list = new ArrayList<>();
//        for (int i = 0;i<3;i++){
//            GoldenWithdraw goldenWithdraw = new GoldenWithdraw();
//            goldenWithdraw.setUid(20L+i);
//            goldenWithdraw.setStatus(2+i);
//            goldenWithdraw.setPersonName("jack"+i);
//            list.add(goldenWithdraw);
//        }
//        new SQLUtilsss().executeInsertMany(list);
        byte[] decode = Base64.getDecoder().decode("MzNleGNoYW5nZTpeZXhjaGFuZ2VAMjAxOEBidHl0b2tlbkAzM15g");
        System.out.println(new String(decode,"utf-8"));
        byte[] encode = Base64.getEncoder().encode("33exchange:^exchange@2018@btytoken@33^".getBytes());
        System.out.println(new String(encode,"utf-8"));
        byte[] encode2 = Base64.getEncoder().encode("33exchange:^exchange@2018@btytoken@33^`".getBytes());
        System.out.println(new String(encode2,"utf-8"));
    }

}
