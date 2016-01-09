package com.yike.tool;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yike.action.ServiceAction;
import com.yike.action.UserAction;
import com.yike.listener.CommonListener;
import com.yike.utils.Contansts;
import com.yike.utils.LogUtils;
import com.yike.utils.ThreeAppParams;

/**
 * 
 * @author zhaoweiChuang
 * 
 * @2015年4月1日
 * 
 * @descripte
 * 
 *            发送网络请求
 */
public class SendActtionTool {
	/**
	 * 
	 * 2015年4月1日
	 * 
	 * @param url
	 *            带参数字段的 url xx.com?name=a&psd=chuagn123
	 * @param service
	 *            业务分类
	 * @param action
	 *            业务指令信号
	 * @param listener
	 *            回调业务
	 */
	public static void post(final String url, final ServiceAction service,
			final Object action, final CommonListener listener) {
		RequestParams params = new RequestParams();
		post(url, service, action, listener, params);

	}

	/**
	 * 
	 * @param url
	 *            网络地址
	 * @param service
	 *            业务
	 * @param action
	 *            业务指令信号
	 * @param listener
	 *            回调
	 * @param params
	 *            参数字段
	 */
	public static void post(final String url, final ServiceAction service,
			final Object action, final CommonListener listener,
			RequestParams params) {
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, url, params,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						listener.onStart(service, action);
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						super.onLoading(total, current, isUploading);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						listener.onException(service, action, msg);
						listener.onFinish(service, action);
					}

					@Override
					public void onSuccess(ResponseInfo<String> response) {
						try {
							JSONObject object = new JSONObject(response.result);
							// 请求成功
							if (object.optInt(Contansts.STATUS, -1) == 1) {
								listener.onSuccess(service, action, object);
								// 更新推送信息
								updatePush(service, action);
								// 请求失败
							} else {
								listener.onFaile(service, action,
										object.optString(Contansts.MESSAGE, ""));
							}
						} catch (JSONException e) {
							// 服务器出现异常
							listener.onException(service, action, "");
							e.printStackTrace();
						}
						listener.onFinish(service, action);
					}
				});
	}

	/**
	 * 关闭更新到自己服务器的推送
	 * 
	 * @param service
	 * @param action
	 */
	private static void updatePush(ServiceAction service, Object action) {
		if (service == null || action == null) {
			return;
		}
		switch (service) {
		case Action_User:
			switch ((UserAction) action) {
			// 关闭推送服务
			case Action_Push_Acton:
				ThreeAppParams.isNeedToken = false;
				LogUtils.tiaoshi("SendActionTool.updatePush()", "更新推送功能到服务器成功");
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	/**
	 * post后不对返回结果进行校验
	 * 
	 * @param url
	 *            网络地址
	 * @param service
	 *            业务
	 * @param action
	 *            业务指令信号
	 * @param listener
	 *            回调
	 * @param params
	 *            参数字段
	 */
	public static void postNoCheck(final String url,
			final ServiceAction service, final Object action,
			final CommonListener listener, RequestParams params) {
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, url, params,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						listener.onStart(service, action);
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						super.onLoading(total, current, isUploading);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						listener.onException(service, action, msg);
						listener.onFinish(service, action);
					}

					@Override
					public void onSuccess(ResponseInfo<String> response) {
						try {
							JSONObject object = new JSONObject(response.result);
							listener.onSuccess(service, action, object);
						} catch (JSONException e) {
							// 服务器出现异常
							listener.onException(service, action, "");
							e.printStackTrace();
						}
						listener.onFinish(service, action);
					}
				});
	}

	/**
	 * 
	 * 2015年4月1日
	 * 
	 * @param url
	 *            带参数字段的 url xx.com?name=a&psd=chuagn123
	 * @param service
	 *            业务分类
	 * @param action
	 *            业务指令信号
	 * @param listener
	 *            回调业务
	 */
	public static void get(final String url, final ServiceAction service,
			final Object action, final CommonListener listener) {
		RequestParams params = new RequestParams();
		get(url, service, action, listener, params);
	}

	/**
	 * 
	 * @param url
	 * @param service
	 * @param action
	 * @param listener
	 * @param params
	 *            请求参数
	 */
	public static void get(final String url, final ServiceAction service,
			final Object action, final CommonListener listener,
			final RequestParams params) {
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.GET, url, params,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						LogUtils.tiaoshi("onStart", url);
						listener.onStart(service, action);

					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						super.onLoading(total, current, isUploading);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LogUtils.tiaoshi("onFailure", msg);
						listener.onException(service, action, msg);
						listener.onFinish(service, action);
					}

					@Override
					public void onSuccess(ResponseInfo<String> response) {
						LogUtils.tiaoshi("onSuccess", response.result);
						try {
							JSONObject object = new JSONObject(response.result);
							// 请求成功
							if (object.optInt(Contansts.STATUS, -1) == 1) {
								listener.onSuccess(service, action, object);
								// 请求失败
							} else {
								listener.onFaile(service, action,
										object.optString(Contansts.MESSAGE, ""));
							}
						} catch (JSONException e) {
							// 服务器出现异常
							listener.onException(service, action, "");
							e.printStackTrace();
						}
						listener.onFinish(service, action);
					}
				});
	}

}
