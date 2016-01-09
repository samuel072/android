package com.yike.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.yike.CommonWebViewActivity;
//import com.modernsky.istv.OrderActivity;
import com.yike.PlayActivity;
import com.yike.bean.FocusPictureModel;
import com.yike.bean.UserBean;
import com.yike.service.UserService;

/**
 * 工具类
 * 
 * @author zxm
 */
public class Utils {
    public static Toast mToast = null;

    /**
     * Toast弹窗
     * 
     * @param context
     * @param string
     */
    public static void toast(Context context, String string) {
        if (context == null && Looper.myLooper() != Looper.getMainLooper()) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), string,
                    Toast.LENGTH_SHORT);
        } else {
            mToast.setText(string);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /**
     * 计算gridview的高度 使用此方法 item的最外层布局必须使用LinearLayout布局
     * 
     * @param listView
     * @param numColumns
     */
// public static void setGridViewHeightBasedOnChildren(GridView listView,
// int numColumns) {
// ListAdapter listAdapter = listView.getAdapter();
// if (listAdapter == null) {
// // pre-condition
// return;
// }
//
// int totalHeight = 0;
// int num = listView.getCount() % numColumns;
// int rows = listView.getCount() / numColumns;
// if (num > 0) {
// rows++;
// }
// for (int i = 0; i < rows; i++) {
// View listItem = listAdapter.getView(i, null, listView);
// listItem.measure(0, 0);
// totalHeight += listItem.getMeasuredHeight();
// }
//
// ViewGroup.LayoutParams params = listView.getLayoutParams();
// params.height = totalHeight
// + (listView.getVerticalSpacing() * (rows - 1));
// listView.setLayoutParams(params);
// }

    public static void toast(Context applicationContext, int id) {
        if (applicationContext == null
                && Looper.myLooper() != Looper.getMainLooper()) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(applicationContext, applicationContext
                    .getResources().getString(id), Toast.LENGTH_SHORT);
        } else {
            mToast.setText(applicationContext.getResources().getString(id));
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /**
     * 播放专辑
     * 
     * @param context
     * @param albumId
     * @param albumName
     */
    public static void playAlbum(Context context, String albumId,
            String albumName) {
        if (PlayActivity.isOpened) {
            return;
        }
        Intent intent = new Intent(context, PlayActivity.class);
        intent.putExtra(Contansts.ALBUM_ID, albumId);
        intent.putExtra(Contansts.ALBUM_NAME, albumName);
        intent.putExtra(Contansts.TYPE, Contansts.ALBUM_NAME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent);
    }

    /**
     * 打开视频详情页面
     * 
     * @param context
     * @param urlDetail 详情URL
     */
    public static void playVideo(Context context, String urlDetail) {
        Intent intent = new Intent(context, CommonWebViewActivity.class);
        intent.putExtra("url", urlDetail);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent);
    }

    /**
     * 播放视频
     * 
     * @param context
     * @param videoId
     * @param videoName
     * @param albumId
     */
    public static void playVideo(Context context, String videoId,
            String videoName, String albumId) {
        if (PlayActivity.isOpened) {
            return;
        }
        Intent intent = new Intent(context, PlayActivity.class);
        intent.putExtra(Contansts.ALBUM_ID, albumId);
        intent.putExtra(Contansts.VIDEO_ID, videoId);
        intent.putExtra(Contansts.VIDEO_NAME, videoName);
        intent.putExtra(Contansts.TYPE, Contansts.VIDEO_NAME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent);
    }

    /**
     * 直播视频 video
     * 
     * @param context
     * @param videoId
     * @param videoName
     * @param albumId
     */
    public static void playLive(Context context, String videoId,
            String videoName, String albumId) {
        if (PlayActivity.isOpened) {
            return;
        }
        Intent intent = new Intent(context, PlayActivity.class);
        intent.putExtra(Contansts.ALBUM_ID, albumId);
        intent.putExtra(Contansts.VIDEO_ID, videoId);
        intent.putExtra(Contansts.VIDEO_NAME, videoName);
        intent.putExtra(Contansts.TYPE, Contansts.LIVE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent);
    }

    public static void playMedia(FocusPictureModel model, Activity context) {
        if (model == null) {
            return;
        }
        try {
            int type = Integer.parseInt(model.getType());
            switch (type) {
            // 专辑
                case 0:
                    Utils.playAlbum(context, model.getAlbumId(), model.getName());
                    break;
                // 视屏
                case 1:
                    Utils.playVideo(context, model.getVideoId(), model.getName(),
                            model.getAlbumId());
                    break;
                // 链接
                case 2:
                    Uri uri = Uri.parse(model.getUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
        }
    }

    /**
     * 开启支付界面
     * 
     * @param albumId
     * @param activity
     */
// public static void startPay(String albumId, Activity activity) {
// UserBean bean = UserService.getInatance().getUserBean();
// String userId = bean.getId();
// if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(albumId)) {
// LogUtils.tiaoshi("Utils.startPay()", "UserId_" + userId + ";"
// + "AlbumId_" + albumId);
// Intent intent = new Intent(activity, OrderActivity.class);
// intent.putExtra(Contansts.ALBUM_ID, albumId);
// intent.putExtra(Contansts.USER_ID, userId);
// activity.startActivity(intent);
// }
// }
}
