package com.yike.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.yike.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.yike.utils.ThreeAppParams;

/**
 * 第三方 登陆信息
 */
public class PopThreeShare extends PopupWindow implements OnClickListener {

    private UMSocialService mController = UMServiceFactory
            .getUMSocialService("com.umeng.share");
    private Activity mActivity;
    private String tagUrl;
    private String tagTitle;
    private String tagContent;

    public PopThreeShare(Activity activity) {
        super(activity);
        this.mActivity = activity;
        initView(activity);
    }

    /**
     * @param tille
     *            分享的标题
     * @param content
     *            分享的内容
     * @param url
     *            分享的url
     */
    public void setShareInfo(String tille, String content, String videoUrl) {
        this.tagTitle = tille;
        this.tagContent = content;
        this.tagUrl = videoUrl;
    }

    @SuppressWarnings("deprecation")
    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(
                R.layout.pop_three_share, null);
        rootView.findViewById(R.id.wechat).setOnClickListener(this);
        rootView.findViewById(R.id.wechat_circle).setOnClickListener(this);
        rootView.findViewById(R.id.qq).setOnClickListener(this);
        rootView.findViewById(R.id.qzone).setOnClickListener(this);
        setContentView(rootView);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.wechat:
                addWXPlatform();
                performShare(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.wechat_circle:
                addWXPlatform();
                performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.qq:
                addQQPlatform();
                performShare(SHARE_MEDIA.QQ);
                break;
            case R.id.qzone:
                addQQPlatform();
                performShare(SHARE_MEDIA.QZONE);
                break;
            default:
                break;
        }
        dismiss();
    }

    private void performShare(SHARE_MEDIA platform) {
        mController.postShare(mActivity, platform, new SnsPostListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode,
                    SocializeEntity entity) {
                String showText = platform.toString();
                if (eCode == StatusCode.ST_CODE_SUCCESSED) {
                    showText = "分享成功";
                } else {
                    showText = "分享失败";
                }
                Toast.makeText(mActivity, showText, Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }

    private void addQQPlatform() {
        String appId = ThreeAppParams.QQ_APP_ID;
        String appKey = ThreeAppParams.QQ_APP_KEY;
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mActivity, appId,
                appKey);
        qqSsoHandler.addToSocialSDK();
        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mActivity, appId,
                appKey);
        qZoneSsoHandler.addToSocialSDK();
       // UMImage umimg = new UMImage(mActivity, R.drawable.icon);
        // 设置qq 分享的详细内容
        QQShareContent qqContent = new QQShareContent();
        //qqContent.setShareImage(umimg);
        qqContent.setTitle(tagTitle);
        qqContent.setTargetUrl(tagUrl);
        qqContent.setShareContent(tagContent);
        mController.setShareMedia(qqContent);
        // qq 空间的信息分享
        QZoneShareContent QzoneContent = new QZoneShareContent();
        //QzoneContent.setShareImage(umimg);
        QzoneContent.setTitle(tagTitle);
        QzoneContent.setTargetUrl(tagUrl);
        QzoneContent.setShareContent(tagContent);
        mController.setShareMedia(QzoneContent);

    }

    /**
     * @功能描述 : 添加微信平台分享
     * @return
     */
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = ThreeAppParams.WX_APP_ID;
        String appSecret = ThreeAppParams.WX_APP_KEY;
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(mActivity, appId, appSecret);
        wxHandler.addToSocialSDK();

        // Logo 图片
        //UMImage umimg = new UMImage(mActivity, R.drawable.icon);
        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(mActivity, appId,
                appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        // 设置微信分享的详细内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        //weixinContent.setShareImage(umimg);
        weixinContent.setTitle(tagTitle);
        weixinContent.setTargetUrl(tagUrl);
        weixinContent.setShareContent(tagContent);
        mController.setShareMedia(weixinContent);
        // qq 空间的信息分享
        CircleShareContent circleContent = new CircleShareContent();
        //circleContent.setShareImage(umimg);
        circleContent.setTitle(tagTitle);
        circleContent.setTargetUrl(tagUrl);
        circleContent.setShareContent(tagContent);
        mController.setShareMedia(circleContent);

    }

    /**
     * 展示 位置
     */
    public void showPop() {
        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0,
                0);

    }
}
