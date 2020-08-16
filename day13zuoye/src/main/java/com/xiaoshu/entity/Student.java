package com.xiaoshu.entity;

import java.util.Date;

import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Table(name="student1")
public class Student {
	private Integer sid;
	private String sname;
	private Integer age;
	private String scode;
	private String grade;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date entrytime;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createtime;
	private Integer cid;
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getScode() {
		return scode;
	}
	public void setScode(String code) {
		this.scode = code;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public Date getEntrytime() {
		return entrytime;
	}
	public void setEntrytime(Date entrytime) {
		this.entrytime = entrytime;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Integer getCid() {
		return cid;
	}
	public void setCid(Integer cid) {
		this.cid = cid;
	}
	@Override
	public String toString() {
		return "Student [sid=" + sid + ", sname=" + sname + ", age=" + age + ", scode=" + scode + ", grade=" + grade
				+ ", entrytime=" + entrytime + ", createtime=" + createtime + ", cid=" + cid + "]";
	}
	
	
}
