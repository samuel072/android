package com.yike.bean;

public class Result<T, R> {
	public String message;
	public int status;
	public String timestamp;
	public int page;
	public int size;
	public int total;
	public SearchData<T, R> data;
}
