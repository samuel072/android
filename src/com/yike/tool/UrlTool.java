package com.yike.tool;

import com.lidroid.xutils.http.RequestParams;
import com.yike.utils.LogUtils;

/**
 * 常用工具包装
 */
public class UrlTool {
	/**
	 * 
	 * 2015年2月10日
	 * 
	 * @param values
	 *            key value 键值对 传入
	 * @return 服务器请求的参数
	 */
	public static RequestParams getParams(String... values) {
		RequestParams params = new RequestParams();
		String string = "";
		for (int i = 0; i < values.length; i += 2) {
			params.addQueryStringParameter(values[i], values[i + 1]);
			string = string + values[i] + "," + values[i + 1] + ",";
		}
		LogUtils.tiaoshi("RequestParams," + values[0], string);
		return params;
	}

}
