package com.yike.bean;

public class ResultBean<T> {
	public String message;
	public int status;
	public String timestamp;
	public int page;
	public int size;
	public int total;
	public T data;
}
