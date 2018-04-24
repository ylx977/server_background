package test;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 【人的实体类】实体类
*
 * @version
 * @author wangdong 2018年04月04日 PM14:44:16
 */ 
@Entity
@Table(name = "t_person") 
public class TPerson{

	@Id
	@Column(name = "uid")
	private Long uid; //用户id

	@Column(name = "name")
	private String name; //姓名

	@Column(name = "age")
	private Integer age; //年龄

	@Column(name = "gender")
	private Integer gender; //性别

	@Column(name = "ctime")
	private Integer ctime; //创建时间

	@Column(name = "utime")
	private Integer utime; //更新时间


	public Long getUid(){
		return uid;
	}

	public void setUid(Long uid){
		this.uid=uid;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name=name;
	}

	public Integer getAge(){
		return age;
	}

	public void setAge(Integer age){
		this.age=age;
	}

	public Integer getGender(){
		return gender;
	}

	public void setGender(Integer gender){
		this.gender=gender;
	}

	public Integer getCtime(){
		return ctime;
	}

	public void setCtime(Integer ctime){
		this.ctime=ctime;
	}

	public Integer getUtime(){
		return utime;
	}

	public void setUtime(Integer utime){
		this.utime=utime;
	}

}

