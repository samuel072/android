package com.yike.bean;

import java.util.List;

/**
 * 
 * @author rendy 首页数据
 */
public class HomeDataBean {
	public List<Data> getDatas() {
		return datas;
	}

	public void setDatas(List<Data> datas) {
		this.datas = datas;
	}

	private List<Data> datas;
}
