package com.yike.bean;

/**
 * @author zxm
 *
 */
public class GridBean {
	private String name;
	private String src;
	private String time;
	private int id;
	
	
	public GridBean(String name, String src, String time, int id) {
		super();
		this.name = name;
		this.src = src;
		this.time = time;
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	

	
}
