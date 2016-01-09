package com.yike.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.aliyun.mbaas.oss.callback.SaveCallback;
import com.aliyun.mbaas.oss.model.AccessControlList;
import com.aliyun.mbaas.oss.model.OSSException;
import com.aliyun.mbaas.oss.storage.OSSBucket;
import com.aliyun.mbaas.oss.storage.OSSFile;
import com.lidroid.xutils.http.RequestParams;

/**
 * 常用工具包装
 */
public class GeneralTool {
    public static boolean isEmpty(String val) {
        return null == val || "".equals(val.trim());
    }

    /**
     * 隐藏 软键盘
     * 
     * @param context
     */
    public static void KeyBoardCancle(Activity context) {
        View view = context.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) context
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 展现键盘
     * 
     * @param context
     */
    public static void KeyBoardShow(Activity context) {
        View view = context.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) context
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputmanger.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    /**
     * isOpen若返回true，则表示输入法打开
     * 
     * @param context
     * @return
     */
    public static boolean isKeyBoardShow(Activity context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();

    }

    /**
     * 调用系统的 照片查看器 云相册 选择不到 照片 (小米手机)
     * 
     * @param context
     * @param requestcode
     */
    public static void showFileChooser(Activity context, int requestcode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            context.startActivityForResult(
                    Intent.createChooser(intent, "请选择一个要上传的文件"), requestcode);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "对不起您的手机没有资源管理器软件", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * 第三方分享
     * 
     * @param context
     * @param content
     */
    public static void showShareMessage(Context context, String content) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setType("text/plain");
        context.startActivity(intent);
    }

    /**
     * 得到指定路径文件的的名字
     * 
     * @param filePath
     * @return
     */
    public static String getStringFile(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    /**
     * 网络请求data值是否为空(空数组为空) (php 数据检测)
     * 
     * @param data
     * @return
     */
    public static boolean isRequestDataEmpty(String data) {
        if (isEmpty(data) || "null".equals(data) || "[]".equals(data)) {
            return true;
        }
        return false;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 2015年2月10日
     * 
     * @param values
     *            key value 键值对 传入
     * @return 服务器请求的参数
     */
    public static RequestParams getParams(String... values) {
        RequestParams params = new RequestParams();
        for (int i = 0; i < values.length; i += 2) {
            params.addQueryStringParameter(values[i], values[i + 1]);
        }
        return params;
    }

    /**
     * 2015年2月10日
     * 
     * @param values
     *            key value 键值对 传入
     * @return 服务器请求的参数
     */
    public static String getUrlParams(String... values) {
        StringBuilder params = new StringBuilder("");
        for (int i = 0; i < values.length; i += 2) {
            params.append(values[i] + "=" + values[i + 1] + "&");
        }
        return params.substring(0, params.length() > 0 ? params.length() - 1
                : 0);
    }

    /**
     * 2015年2月12日
     * 
     * @param obj
     *            参数数据
     * @return 返回 key=value& 格式的参数
     */
    public static RequestParams getUrlParams(Object obj) {
        // RequestParams params;
        // String value=JSON.toJSONString(obj);
        // try {
        // JSONObject json=new JSONObject(value);
        // params=new RequestParams();
        // Iterator<String> keys=json.keys();
        // while (keys.hasNext()) {
        // String childKey=keys.next();
        // String childVlaue=json.optString(childKey);
        // LogUtil.Show("GeneralTool.getUrlParams()",childKey+"_:"+childVlaue);
        // params.addQueryStringParameter(childKey,childVlaue);
        // }
        // return params;
        // } catch (JSONException e) {
        // e.printStackTrace();
        // }
        return null;
    }

    /**
     * 2015年2月27日
     * 
     * @param gbString
     * @return 返回转换后的 Unicode 的数值
     */
    public static String encodeUnicode(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }

    /**
     * @param context
     * @param Action
     *            接收方的的 注册的 广播key ，也是传值得 key
     * @param actionKey
     *            处理的指令
     */
    public static void sendOrdersBroadcast(Activity context, String Action,
            int actionKey) {
        Intent intent = new Intent(Action);
        intent.putExtra(Action, actionKey);
        context.getApplication().sendOrderedBroadcast(intent, null);
    }

    /**
     * @param picname
     *            图片名字
     * @param picFile
     *            图片路径
     * @param callBack
     *            借口回调
     */
    public static void uploadFile(String picFile,
            SaveCallback callBack) {
        String picname = picFile.substring(picFile.lastIndexOf("/") + 1);
        OSSBucket sampleBucket;
        // 开始单个Bucket的设置
        sampleBucket = new OSSBucket("pic-yikelive");
        sampleBucket.setBucketHostId("oss-cn-beijing.aliyuncs.com");
        sampleBucket.setBucketACL(AccessControlList.PRIVATE);
        OSSFile ossFile = new OSSFile(sampleBucket, picname);
        ossFile.setUploadFilePath(picFile, "image/jpg");
        ossFile.uploadInBackground(callBack);
    }
}
