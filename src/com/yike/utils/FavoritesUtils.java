package com.yike.utils;

import com.lidroid.xutils.http.RequestParams;
import com.yike.action.ServiceAction;
import com.yike.listener.CommonListener;
import com.yike.tool.SendActtionTool;
import com.yike.tool.UrlTool;

/**
 * Created by wangwei-ps on 15-8-17.
 */
public class FavoritesUtils {

    public static void queryHasAdded(String userId, String videoId, ServiceAction resultAction, CommonListener listener) {
        RequestParams params = UrlTool.getParams(Contansts.USER_ID, userId, Contansts.VIDEO_ID, videoId);
        SendActtionTool.get(Contansts.UserParams.URL_QUERY_FAV_STATUS, resultAction, null, listener, params);
    }

    public static void addToFav(String userId, String videoId, ServiceAction resultAction, CommonListener listener) {
        RequestParams params = UrlTool.getParams(Contansts.USER_ID, userId, Contansts.VIDEO_ID, videoId);
        SendActtionTool.get(Contansts.UserParams.URL_ADD_TO_FAV, resultAction, null, listener, params);
    }
}
