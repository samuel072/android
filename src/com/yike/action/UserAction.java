package com.yike.action;
/**
 * 
 * @author rendy
 *         用户事件归纳 
 */
public enum UserAction {
	Action_login(null),
	Action_Login_OUT(null),
	Action_Regiser_Phone(null),
	Action_Regiser_Phone_Code(null),
	Action_Regiser_Email(null),
	Action_Update_UserName(null),
	Action_Update_Face_Url(null),
	Action_Update_Email(null),
	Action_Update_Mobiles(null),
	Action_OPEN_CHECK(null),
	Action_OPEN_LOGIN(null),
	Action_FindPsd_Phone(null),
	Action_FindPsd_EMAIL(null),
	Action_Push_Acton(null),
	Action_Push_Acton_Out(null),
	Action_CHECK_VERSION(null),
	Action_CHECK_ONE(null),
	Action_BINDING(null),
	// 观看纪录
	Action_See_Leave(null),
	// 删除记录
	Action_See_Leave_DEL(null),
	//收藏
	Action_Shoucang(null),
	Action_Shoucang_Del(null),
	// 添加预约
	Action_Yuyue_Add(null),
	// 取消预约
	Action_Yuyue_Cancle(null),
	// 预约list
	Action_Yuyue_LIST(null);
	
	public Object value;
	private UserAction(Object value) {
		this.value = value;
	}

}
