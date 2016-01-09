package com.yike.manager;

import java.util.Map;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.aliyun.mbaas.oss.OSSClient;
import com.aliyun.mbaas.oss.model.AccessControlList;
import com.aliyun.mbaas.oss.model.TokenGenerator;
import com.aliyun.mbaas.oss.util.OSSLog;
import com.aliyun.mbaas.oss.util.OSSToolKit;
import com.yike.R;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.message.tag.TagManager.Result;
import com.yike.exception.YxException;
import com.yike.tool.BitmapTool;
import com.yike.utils.LogUtils;
import com.yike.utils.ThreeAppParams;

/**
 * 
 * @author rendy 软件设备 信息
 */
public class BaseApplication extends Application {
	public static int mNetWorkState; // 网络状态
	public static int mVersionCode; // 版本号
	public static String mVersionName; // 版本名称
	public static String jpushId = "";
	public static String xGTokend = "";
	public static DavikActivityManager activityManager = null;
	private PushAgent mPushAgent;
	// 阿里 图片上传 key

	static {
		OSSClient.setGlobalDefaultTokenGenerator(new TokenGenerator() { // 设置全局默认加签器
					@Override
					public String generateToken(String httpMethod, String md5,
							String type, String date, String ossHeaders,
							String resource) {
						String content = httpMethod + "\n" + md5 + "\n" + type
								+ "\n" + date + "\n" + ossHeaders + resource;
						return OSSToolKit.generateToken(
								ThreeAppParams.ALI_accessKey,
								ThreeAppParams.ALI_screctKey, content);
					}
				});
		OSSClient.setGlobalDefaultACL(AccessControlList.PUBLIC_READ); // 设置全局默认bucket访问权限

	}

	@Override
	public void onCreate() {
		initLocalVersion();
		activityManager = DavikActivityManager.getScreenManager();
		BitmapTool.getInstance().initAdapterUitl(getApplicationContext());

		// 初始化 阿里图片上穿
		OSSLog.enableLog(true);
		OSSClient.setApplicationContext(getApplicationContext()); // 传入应用程序context
		// 异常监听
		YxException crashHandler = YxException.getInstance();
		// 注册crashHandler
		crashHandler.init(getApplicationContext());
		initUmengtMessage();
	}

	/**
	 * 初始化友盟 推送
	 */
	private void initUmengtMessage() {
		mPushAgent = PushAgent.getInstance(this);
		// 正式调试之后
		mPushAgent.setDebugMode(false);
		/**
		 * 该Handler是在IntentService中被调用，故 1.
		 * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK 2.
		 * IntentService里的onHandleIntent方法是并不处于主线程中，因此，如果需调用到主线程，需如下所示;
		 * 或者可以直接启动Service
		 * */
		UmengMessageHandler messageHandler = new UmengMessageHandler() {
			@Override
			public void dealWithCustomMessage(final Context context,
					final UMessage msg) {
				LogUtils.tiaoshi("dealWithCustomMessage()",
						msg.extra == null ? "map集合为空" : msg.extra.toString()
								+ "___自定义消息" + msg.custom);
				Thread ru = new Thread(new Runnable() {
					@Override
					public void run() {
						UTrack.getInstance(getApplicationContext())
								.trackMsgClick(msg);
						Map<String, String> data = msg.extra;
						if (data != null) {
							String type = data.get("type");
							String tag = data.get("tag");
							if (type == null || tag == null) {
								return;
							}
							try {
								if (type.equals("add_tag")) {
									mPushAgent.getTagManager().add(tag);
									LogUtils.tiaoshi(
											"推送的消________dealWithCustomMessage()",
											"添加tag" + tag);
								} else if (type.equals("del_tag")) {
									mPushAgent.getTagManager().delete(tag);
									LogUtils.tiaoshi(
											"推送的消息________dealWithCustomMessage()",
											"删除tag" + tag);
								}
							} catch (Exception e) {
								e.printStackTrace();
								LogUtils.tiaoshi("出现异常"
										+ "dealWithCustomMessage", e.toString());

							}
						} else {
							LogUtils.tiaoshi("出现异常" + "dealWithCustomMessage",
									"null");

						}
					}
				});
				ru.start();
			}

			@Override
			public Notification getNotification(Context context, UMessage msg) {
				LogUtils.tiaoshi("推送的消息___1_getNotification()",
						msg.extra == null ? "map集合为空" : msg.extra.toString()
								+ "___自定义消息" + msg.custom);
				switch (msg.builder_id) {
				case 1:
					NotificationCompat.Builder builder = new NotificationCompat.Builder(
							context);
					RemoteViews myNotificationView = new RemoteViews(
							context.getPackageName(),
							R.layout.notification_view);
					myNotificationView.setTextViewText(R.id.notification_title,
							msg.title);
					myNotificationView.setTextViewText(R.id.notification_text,
							msg.text);
					myNotificationView.setImageViewBitmap(
							R.id.notification_large_icon,
							getLargeIcon(context, msg));
					myNotificationView.setImageViewResource(
							R.id.notification_small_icon,
							getSmallIconId(context, msg));
					builder.setContent(myNotificationView);
					builder.setAutoCancel(true);
					Notification mNotification = builder.build();
					// 由于Android
					// v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
					mNotification.contentView = myNotificationView;
					return mNotification;
				default:
					// 默认为0，若填写的builder_id并不存在，也使用默认。
					return super.getNotification(context, msg);
				}
			}

		};
		mPushAgent.setMessageHandler(messageHandler);

		/**
		 * 该Handler是在BroadcastReceiver中被调用，故
		 * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
		 * */
		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
			@Override
			public void dealWithCustomAction(Context context, UMessage msg) {
				LogUtils.tiaoshi("推送的消息___notificationClickHandler()", ""
						+ msg.extra.toString());

				Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
			}
		};
		mPushAgent.setNotificationClickHandler(notificationClickHandler);
	}

	/**
	 * 
	 * 获得当前app版本号
	 **/
	public void initLocalVersion() {
		PackageInfo pinfo;
		try {
			pinfo = this.getPackageManager().getPackageInfo(
					this.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			mVersionCode = pinfo.versionCode;
			mVersionName = pinfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
}
