package com.yike.service;

import java.util.List;

import com.yike.bean.TopicBean;
import com.yike.bean.TopicDetailBean;

/**
 * 
 * @author rendy 专题界面
 */
public class TopicDetailService {

	private static TopicDetailService LOGIC;
	private List<TopicDetailBean> datas;

	public static TopicDetailService getInstance() {
		return LOGIC = LOGIC == null ? new TopicDetailService() : LOGIC;
	}

	public List<TopicDetailBean> getDatas() {
		return datas;
	}

	public void setDatas(List<TopicDetailBean> datas) {
		this.datas = datas;
	}

	public boolean isHaveDate() {
		return datas != null;
	}
}
