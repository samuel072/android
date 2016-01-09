package com.yike.bean;

import java.util.List;
/**
 * 
 * @author rendy
 *        data 数据解析器
 */
public class Data {


	private List<FocusPictureModel> data;
	private String description;
	private int displayOrder;
	private String focusName;
	private int id;
	private int subscribe;
	private int type;
	private String typeDesc;
	private int total;
 
	public List<FocusPictureModel> getData() {
		return data;
	}

	public void setData(List<FocusPictureModel> data) {
		this.data = data;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getFocusName() {
		return focusName;
	}

	public void setFocusName(String focusName) {
		this.focusName = focusName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(int subscribe) {
		this.subscribe = subscribe;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
