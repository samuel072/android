package com.yike.utils;

import com.lidroid.xutils.http.RequestParams;
import com.yike.action.ServiceAction;
import com.yike.listener.CommonListener;
import com.yike.tool.SendActtionTool;
import com.yike.tool.UrlTool;

/**
 * Created by wangwei-ps on 15-8-18.
 */
public class MarkLikeUtils {

    public static void markLike(String userId, String videoId, ServiceAction resultAction, CommonListener listener) {
        mark(userId, videoId, 1, resultAction, listener);
    }

    public static void cancelMarkLike(String userId, String videoId, ServiceAction resultAction, CommonListener listener) {
        mark(userId, videoId, 0, resultAction, listener);
    }

    public static void mark(String userId, String videoId, int status, ServiceAction resultAction, CommonListener listener) {
        RequestParams params = UrlTool.getParams(Contansts.USER_ID, userId, Contansts.RESOURCE_ID, videoId, Contansts.STATUS, status + "");
        SendActtionTool.get(Contansts.UserParams.URL_MARK_LIKE, resultAction, null, listener, params);
    }
}
