package com.service.hibernate.criteria;

import java.util.HashSet;
import java.util.Set;

public class Dept {
	private Integer deptno;
	private String dname;
	private String loc;
	private Set<Emp> emps = new HashSet<Emp>();

	public void addEmp(Emp emp) {
		emps.add(emp);
		emp.setDept(this);
	}

	public Integer getDeptno() {
		return deptno;
	}

	public void setDeptno(Integer deptno) {
		this.deptno = deptno;
	}

	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}
}
