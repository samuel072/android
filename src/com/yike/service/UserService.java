package com.yike.service;

import android.app.Activity;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.yike.LoginActivity;
import com.yike.bean.UserBean;
import com.yike.manager.DavikActivityManager;
import com.yike.tool.ShareXmlTool;
import com.yike.utils.GeneralTool;
import com.yike.utils.LogUtils;
import com.yike.utils.Contansts.AppFile;
import com.yike.utils.Contansts.FileKey;

/**
 * 
 * @author rendy
 * 
 *         当前登录用户信息存储
 * 
 */
public class UserService {
	static UserService lOGIC;

	private UserService() {
	}

	private UserBean userBean;
	private boolean isBingQQ;
	private boolean isBingWX;
	private boolean isBingWB;

	/**
	 * @return
	 * 
	 *         当前登录着消息
	 */
	public UserBean getUserBean() {
		if (userBean != null) {
			return userBean;
		}
		ShareXmlTool tool = new ShareXmlTool(DavikActivityManager
				.getScreenManager().currentActivity(), AppFile.USER_INFO);
		String value = tool.getValue(FileKey.USER_BEAN);
		if (!GeneralTool.isEmpty(value)) {
			userBean = (UserBean) JSON.parseObject(value, UserBean.class);
		}
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
		if (userBean != null) {
			ShareXmlTool tool = new ShareXmlTool(DavikActivityManager
					.getScreenManager().currentActivity(), AppFile.USER_INFO);
			String value = JSON.toJSONString(userBean);
			if (!GeneralTool.isEmpty(value)) {
				tool.putValue(FileKey.USER_BEAN, value);
			}
		} else {
			ShareXmlTool tool = new ShareXmlTool(DavikActivityManager
					.getScreenManager().currentActivity(), AppFile.USER_INFO);
			tool.putValue(FileKey.USER_BEAN, "");
		}

	}

	public static UserService getInatance() {
		
		return lOGIC=lOGIC == null ? new UserService() : lOGIC;
	}

	public boolean isNeedLogin(Activity context) {
		if (getUserBean() == null) {
			// DialogTool.createLoginDialog(context,R.string.login);
			return true;
		}
		return false;
	}

	public boolean ifNeedToLogin(Activity context) {
		if (isNeedLogin(context)) {
			context.startActivity(new Intent(context, LoginActivity.class));
			return true;
		}
		return false;

	}

	public boolean isBingQQ() {
		return isBingQQ;
	}

	public void setBingQQ(boolean isBingQQ) {
		this.isBingQQ = isBingQQ;
	}

	public boolean isBingWX() {
		return isBingWX;
	}

	public void setBingWX(boolean isBingWX) {
		this.isBingWX = isBingWX;
	}

	public boolean isBingWB() {
		return isBingWB;
	}

	public void setBingWB(boolean isBingWB) {
		this.isBingWB = isBingWB;
	}

}
