package com.yike.bean;

import java.util.List;

public class ResultList<T> {
	public String message;
	public int status;
	public String timestamp;
	public int page;
	public int size;
	public int total;
	public List<T> data;
}
