package com.yike.listener;

import com.yike.action.ServiceAction;

/**
 * 
 * @author rendy
 * 
 *         更新 ui界面回调
 * 
 */
public interface CommonListener {
	/**
	 * 
	 * @param service
	 *            业务标记
	 * @param action
	 *            信号指令
	 * @param value
	 *            返回值
	 */
	public void onSuccess(ServiceAction service, Object action, Object value);

	/**
	 * 
	 * @param service
	 *            业务标记
	 * @param action
	 *            信号指令
	 * @param value
	 *            错误信息
	 */
	public void onFaile(ServiceAction service, Object action, Object value);

	/**
	 * 
	 * @param service
	 *            业务标记
	 * @param action
	 *            信号指令
	 * @param value
	 *            返回值
	 */
	public void onException(ServiceAction service, Object action, Object value);

	/**
	 * 开始请求
	 * 
	 * @param service
	 *            业务标记
	 * @param action
	 *            信号指令
	 * @param value
	 *            返回值
	 */
	public void onStart(ServiceAction service, Object action);

	/**
	 * 请求结束
	 * 
	 * @param service
	 *            业务标记
	 * @param action
	 *            信号指令
	 * @param value
	 *            返回值
	 */
	public void onFinish(ServiceAction service, Object action);
}
