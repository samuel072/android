package com.yike;

import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.utils.StringUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.http.RequestParams;
import com.yike.action.CommentAction;
import com.yike.action.ServiceAction;
import com.yike.bean.Ad;
import com.yike.bean.AlbumLastVideo;
import com.yike.bean.LatestVideo;
import com.yike.bean.LiveInfo;
import com.yike.bean.Pinglun;
import com.yike.bean.ResultBean;
import com.yike.bean.ResultList;
import com.yike.bean.ResultPinglun;
import com.yike.bean.UserBean;
import com.yike.bean.VideoPlayInfo;
import com.yike.fragment.PlayFragment;
import com.yike.service.UserService;
import com.yike.tool.BitmapTool;
import com.yike.tool.DialogTool;
import com.yike.tool.SendActtionTool;
import com.yike.tool.UrlTool;
import com.yike.utils.Contansts;
import com.yike.utils.LogUtils;
import com.yike.utils.Utils;
import com.yike.utils.Contansts.UserParams;
import com.yike.view.MediaController;
import com.yike.view.PopThreeShare;
import com.yike.view.VideoView;
import com.yike.view.MediaController.OnHiddenListener;
import com.yike.view.MediaController.OnMediaControllerClickedListener;
import com.yike.view.MediaController.OnShownListener;

/**
 * 播放界面
 * 
 * @author zxm
 * 
 */
@SuppressLint("NewApi")
public class PlayActivity extends BaseActivity implements OnCompletionListener,
		OnInfoListener, OnMediaControllerClickedListener, OnHiddenListener,
		OnShownListener {

	private static FragmentManager fMgr;
	private String albumId, videoId;
	private String userId = "";
	private int userStatus;
	private String stringType = "";
	private long playTime;
	private long nowTime = 0;
	private float mScrollX;
	private String standardPic = "";

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	private String mPath = "rtmp://live.v1.cn/v1live/v1live";
	private String mTitle = "";
	private VideoView mVideoView;
	private View mVolumeBrightnessLayout;
	private ImageView mOperationBg;
	private ImageView mOperationPercent;
	private AudioManager mAudioManager;
	/** 最大声音 */
	private int mMaxVolume;
	/** 当前声音 */
	private int mVolume = -1;
	/** 当前亮度 */
	private float mBrightness = -1f;
	/** 当前缩放模式 */
	// private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
	private GestureDetector mGestureDetector;
	private MediaController mMediaController;
	private View mLoadingView;
	RelativeLayout rl_video_info;
	FrameLayout frameLayout;
	private long totalTime;

	private TextView tv_video_name;
	private PlayFragment playFragment;
	private View rl_ad;
	private ImageView img_ad;
	private TextView tv_praise_count;
	private long mPriseCount = 0;
	private BitmapUtils bitmapTool;
	private boolean isShouCanged;

	/**
	 * 初始化首个Fragment
	 */
	private void initFragment() {
		FragmentTransaction ft = fMgr.beginTransaction();
		playFragment = new PlayFragment();
		ft.add(R.id.fragmentRoot, playFragment, "playFragment");
		ft.addToBackStack("playFragment");
		ft.commit();
	}

	public static boolean isOpened;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onStart()
	 */
	@Override
	protected void onStart() {
		isOpened = true;
		super.onStart();
	}

	public void setFragment(Fragment fragment, boolean withAnimation) {

		FragmentTransaction transaction = fMgr.beginTransaction();
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		if (withAnimation) {
			transaction.setCustomAnimations(R.anim.push_right_in,
					R.anim.push_left_out, R.anim.push_left_in,
					R.anim.push_right_out);
		}
		transaction.add(R.id.fragmentRoot, fragment).addToBackStack(null)
				.commitAllowingStateLoss();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_good:
			praisVideo(videoId);
			break;
		case R.id.image_shoucang:
			shouCang(videoId);
			break;
		case R.id.image_share:
			share(mTitle, mTitle, videoId);
			break;
		case R.id.image_dingyue:
			// TODO
			break;
		case R.id.image_cancel_ad:
			tongleAd(false);
			break;
		case R.id.image_ad:
			LogUtils.tiaoshi("adUrl", adUrl);
			if (!TextUtils.isEmpty(adUrl)) {
				Uri uri = Uri.parse(adUrl);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
			break;

		default:
			break;
		}
	}

	int adCount = 1;
	private int index;

	/**
	 * 广告开关
	 * 
	 * @param b
	 */
	private void tongleAd(boolean b) {
		if (b) {
			rl_ad.setVisibility(0);
		} else {
			rl_ad.setVisibility(8);
			Ad ad = mAds.get(index % adCount);
			adUrl = ad.getUrl();
			bitmapTool.display(img_ad, ad.getBigImage());
			index++;
		}
	}

	public void setPriseCount(long count) {
		if (tv_praise_count != null) {
			if (count >= mPriseCount) {
				mPriseCount = count;
				tv_praise_count.setText(mPriseCount + "次");
			}
		}
	}

	/**
	 * 分享
	 * 
	 * @param videoId2
	 */
	private void share(String tille, String content, String videoId) {
		PopThreeShare share = new PopThreeShare(PlayActivity.this);
		share.setShareInfo(tille, content, videoId);
		share.showPop();
	}

	/**
	 * 对视频资源点赞
	 */
	private void praisVideo(String resouceId) {
		if (!isUesrLogined()) {
			return;
		}
		RequestParams params = UrlTool.getParams(Contansts.RESOURCE_ID,
				resouceId, Contansts.USER_ID, userId, Contansts.STATUS,
				String.valueOf(userStatus));

		SendActtionTool.get(UserParams.URL_ADD_PRAISE_RESOUCE,
				ServiceAction.Action_Comment,
				CommentAction.Action_addPraiseResouce, this, params);
	}

	/**
	 * 判断用户状态，如果未登录，跳转登录界面
	 */
	public boolean isUesrLogined() {
		if (TextUtils.isEmpty(userId)) {
			if (UserService.getInatance().isNeedLogin(this)) {
				startActivity(new Intent(this, LoginActivity.class));
				return false;
			} else {
				UserBean bean = UserService.getInatance().getUserBean();
				userId = bean.getId();
				userStatus = bean.getStatus();
				return true;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.modernsky.istv.BaseActivity#setContentView()
	 */
	@Override
	public void setContentView() {
		if (!LibsChecker.checkVitamioLibs(this))
			return;
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_play);
		findViewById(R.id.image_good).setOnClickListener(this);
		findViewById(R.id.image_shoucang).setOnClickListener(this);
		findViewById(R.id.image_share).setOnClickListener(this);
		findViewById(R.id.image_dingyue).setOnClickListener(this);
		tv_praise_count = (TextView) findViewById(R.id.tv_praise_count);
		rl_ad = findViewById(R.id.rl_ad);
		img_ad = (ImageView) findViewById(R.id.image_ad);
		img_ad.setOnClickListener(this);
		findViewById(R.id.image_cancel_ad).setOnClickListener(this);
		bitmapTool = BitmapTool.getInstance()
				.initAdapterUitl(PlayActivity.this);
		initView();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.modernsky.istv.BaseActivity#findViewById()
	 */
	@Override
	public void findViewById() {

		// 获取FragmentManager实例
		fMgr = getSupportFragmentManager();
		Intent intent = getIntent();
		stringType = intent.getStringExtra(Contansts.TYPE);
		LogUtils.d("stringExtra" + stringType);
		try {
			// getMyMediaController().setLiveMode(Contansts.LIVE.equals(stringType));

			if (Contansts.ALBUM_NAME.equals(stringType)) {
				mTitle = intent.getStringExtra(Contansts.ALBUM_NAME);
				albumId = intent.getStringExtra(Contansts.ALBUM_ID);
				playAlbum(albumId);
			} else {
				mTitle = intent.getStringExtra(Contansts.VIDEO_NAME);
				videoId = intent.getStringExtra(Contansts.VIDEO_ID);
				albumId = intent.getStringExtra(Contansts.ALBUM_ID);
				playVideo(videoId);
			}
			tv_video_name.setText(mTitle);

			getMyMediaController().setVideoShowName(mTitle);

			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				full(true);
			} else {
				full(false);
			}
			initFragment();
			playFragment.getAlbumVideo(albumId);
		} catch (Exception e) {
			e.printStackTrace();
			finish();
		}

	}

	public void playVideoByUrl(String videoId, String url, String videoName) {
		mTitle = videoName;
		playVideoByUrl(videoId, url);
	}

	public void playVideoByUrl(String videoId, String url) {
		LogUtils.tiaoshi("playVideoByUrl", videoId + "---" + url);
		tongleAd(false);
		this.videoId = videoId;
		mPriseCount = 0;
		setPriseCount(0);
		mPath = url;
		tv_video_name.setText(mTitle);
		getVideoView().setVideoURI(Uri.parse(mPath));
		getVideoView().start();
		getMyMediaController().setVideoShowName(mTitle);
		playFragment.getPinglunList(videoId, "0");
	}

	public void playVideo(String videoId) {
		RequestParams params = UrlTool.getParams(Contansts.VIDEO_ID, videoId,
				Contansts.USER_ID, userId, Contansts.FILTER,
				Contansts.FILTER_VIDEO);
		if (!TextUtils.isEmpty(videoId)) {
			SendActtionTool.get(Contansts.URL_VIDEO_DETAIL,
					ServiceAction.Action_Comment,
					CommentAction.Action_GetVideo_By_Id, this, params);
		}
	}

	public void playAlbum(String albumId) {
		if (!TextUtils.isEmpty(albumId)) {
			RequestParams params = new RequestParams();
			params.addQueryStringParameter(Contansts.ALBUM_ID, albumId);
			// params.addQueryStringParameter(Contansts.USER_ID, userId);
			params.addQueryStringParameter(Contansts.FILTER,
					Contansts.FILTER_ALBUM);

			SendActtionTool.get(Contansts.URL_ALBUM_DETAIL,
					ServiceAction.Action_Comment,
					CommentAction.Action_GetLastVideo_By_Id, this, params);
		}
	}

	/**
	 * 
	 */
	private void initView() {
		// ~~~ 绑定控件
		tv_video_name = (TextView) findViewById(R.id.tv_video_name);
		mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
		mOperationBg = (ImageView) findViewById(R.id.operation_bg);
		mOperationPercent = (ImageView) findViewById(R.id.operation_percent);
		mLoadingView = findViewById(R.id.video_loading);
		rl_video_info = (RelativeLayout) findViewById(R.id.rl_video_info);
		frameLayout = (FrameLayout) findViewById(R.id.fragmentRoot);

		// ~~~ 绑定数据
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mMaxVolume = mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		// 设置显示名称
		getMyMediaController().setAnchorView(getVideoView());
		getMyMediaController().setOnMediaControllerClickedListener(this);
		getVideoView().setMediaController(getMyMediaController());
		getVideoView().requestFocus();

		getGestureDetector();
		addAd();

	}

	private void addAd() {
		SendActtionTool.get(Contansts.URL_GET_VIDEO_AD,
				ServiceAction.Action_Comment, CommentAction.Action_getVideoAd,
				this);
	}

	public GestureDetector getGestureDetector() {
		if (mGestureDetector == null) {
			mGestureDetector = new GestureDetector(this,
					new MyGestureListener());
		}
		return mGestureDetector;
	}

	private VideoView getVideoView() {
		if (mVideoView == null) {
			mVideoView = (VideoView) findViewById(R.id.vv_surface_view);
			mVideoView.setOnCompletionListener(this);
			mVideoView.setOnInfoListener(this);
		}
		return mVideoView;

	}

	private MediaController getMyMediaController() {
		if (mMediaController == null) {
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mMediaController = (MediaController) findViewById(R.id.mediacontroller_view);
		}
		return mMediaController;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mVideoView != null) {
			playTime = mVideoView.getCurrentPosition();
			mVideoView.pause();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		LayoutParams layoutParams = getVideoView().getLayoutParams();
		getMyMediaController().setLayoutParams(layoutParams);

		if (mVideoView != null) {
			playTime = mVideoView.getCurrentPosition();
			mVideoView.resume();
		}
	}

	@Override
	protected void onDestroy() {
		isOpened = false;
		super.onDestroy();
		if (mVideoView != null) {
			addPlayRecord(String.valueOf(playTime));
			mVideoView.stopPlayback();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mGestureDetector != null && mGestureDetector.onTouchEvent(event)) {
			return true;
		}
		float moveX;

		// 处理手势结束
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mScrollX = event.getX();
			nowTime = getVideoView().getCurrentPosition();
			totalTime = getVideoView().getDuration();
			break;
		case MotionEvent.ACTION_MOVE:
			moveX = event.getX() - mScrollX;
			float f = moveX / getVideoView().getWidth();
			long moveTime = (long) (nowTime + f * getVideoView().getDuration());
			if (f > 0.1f) {
				LogUtils.d("前进：" + StringUtils.generateTime(moveTime) + "/"
						+ StringUtils.generateTime(totalTime));
			} else if (f < -0.1f) {
				LogUtils.d("后退：" + StringUtils.generateTime(moveTime) + "/"
						+ StringUtils.generateTime(totalTime));
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			moveX = event.getX() - mScrollX;
			float f2 = moveX / getVideoView().getWidth();
			if (Math.abs(f2) > 0.1f) {
				long seekToTime = (long) (nowTime + f2 * totalTime);
				seekTo(seekToTime);
			}
			endGesture();
			break;
		}

		return super.onTouchEvent(event);
	}

	/** 手势结束 */
	private void endGesture() {
		mVolume = -1;
		mBrightness = -1f;

		// 隐藏
		mDismissHandler.removeMessages(0);
		mDismissHandler.sendEmptyMessageDelayed(0, 500);
	}

	/** 定时隐藏 */
	private Handler mDismissHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mVolumeBrightnessLayout.setVisibility(View.GONE);
		}
	};
	private String adUrl;
	private List<Ad> mAds = new ArrayList<Ad>();
	private boolean isLive;

	private class MyGestureListener extends SimpleOnGestureListener {

		/** 双击 */
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			if (mVideoView != null) {
				if (mVideoView.isPlaying()) {
				} else {
					mVideoView.start();
				}
			}
			return true;
		}

		/** 滑动 */
		@SuppressWarnings("deprecation")
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			float mOldX = e1.getX(), mOldY = e1.getY();
			int y = (int) e2.getRawY();
			int x = (int) e2.getRawX();
			Display disp = getWindowManager().getDefaultDisplay();
			int windowWidth = disp.getWidth();
			int windowHeight = disp.getHeight();
			if (Math.abs((mOldX - x)) < windowWidth * 0.1) {// 左右滑动长度不超过屏幕的十分之一
				if (mOldX > windowWidth * 4.0 / 5)// 右边滑动
					onVolumeSlide((mOldY - y) / windowHeight);
				else if (mOldX < windowWidth / 5.0)// 左边滑动
					onBrightnessSlide((mOldY - y) / windowHeight);
			} else {
				return false;
			}

			return super.onScroll(e1, e2, distanceX, distanceY);
		}
	}

	/**
	 * 播放到指定时间
	 * 
	 * @param time
	 */
	private void seekTo(long time) {
		if (isLive) {
			return;
		}
		LogUtils.d("seekTo=" + time + " ---mVideoView.getDuration())="
				+ getVideoView().getDuration());
		long toTime;
		if (time <= 0) {
			toTime = 0;
		} else if (time >= getVideoView().getDuration()) {
			toTime = getVideoView().getDuration();
		} else {
			toTime = time;
		}
		getVideoView().seekTo(toTime);
		getVideoView().start();

	}

	/**
	 * 滑动改变声音大小
	 * 
	 * @param percent
	 */
	private void onVolumeSlide(float percent) {
		if (mVolume == -1) {
			mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			if (mVolume < 0)
				mVolume = 0;

			// 显示
			//mOperationBg.setImageResource(R.drawable.video_volumn_bg);
			//mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
		}

		int index = (int) (percent * mMaxVolume) + mVolume;
		if (index > mMaxVolume)
			index = mMaxVolume;
		else if (index < 0)
			index = 0;

		// 变更声音
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

		// 变更进度条
		ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
		lp.width = findViewById(R.id.operation_full).getLayoutParams().width
				* index / mMaxVolume;
		mOperationPercent.setLayoutParams(lp);
	}

	/**
	 * 滑动改变亮度
	 * 
	 * @param percent
	 */
	private void onBrightnessSlide(float percent) {
		if (mBrightness < 0) {
			mBrightness = getWindow().getAttributes().screenBrightness;
			if (mBrightness <= 0.00f)
				mBrightness = 0.50f;
			if (mBrightness < 0.01f)
				mBrightness = 0.01f;

			// 显示
			//mOperationBg.setImageResource(R.drawable.video_brightness_bg);
			mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
		}
		WindowManager.LayoutParams lpa = getWindow().getAttributes();
		lpa.screenBrightness = mBrightness + percent;
		if (lpa.screenBrightness > 1.0f)
			lpa.screenBrightness = 1.0f;
		else if (lpa.screenBrightness < 0.01f)
			lpa.screenBrightness = 0.01f;
		getWindow().setAttributes(lpa);

		ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
		lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
		mOperationPercent.setLayoutParams(lp);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			full(true);
			if (mVideoView != null) {
				mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);

			}
		} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			full(false);
			if (mVideoView != null) {
				mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
				mVideoView.buildLayer();
			}
		}
	}

	/**
	 * 全屏模式开关
	 * 
	 * @param enable
	 */
	private void full(boolean enable) {
		LogUtils.d("===full=" + enable);
		if (enable) {
			rl_video_info.setVisibility(View.GONE);
			frameLayout.setVisibility(View.GONE);
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			getWindow().setAttributes(lp);
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			rl_video_info.setVisibility(View.VISIBLE);
			frameLayout.setVisibility(View.VISIBLE);
			WindowManager.LayoutParams attr = getWindow().getAttributes();
			attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().setAttributes(attr);
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

		}
	}

	/**
	 * 添加播放记录
	 */
	private void addPlayRecord(String playTime) {

		RequestParams params = UrlTool.getParams(Contansts.USER_ID, userId,
				Contansts.ALBUM_ID, albumId, Contansts.VIDEO_ID, videoId,
				Contansts.VIDEO_NAME, mTitle, Contansts.VIDEO_PIC, standardPic,
				Contansts.SOURCE, Contansts.MOBILE, Contansts.PLAY_TIME,
				playTime);

		SendActtionTool.get(UserParams.URL_ADD_PLAY_RECORD,
				ServiceAction.Action_Comment,
				CommentAction.Action_addPlayRecord, this, params);

	}

	/**
	 * 播放完成回调
	 */
	@Override
	public void onCompletion(MediaPlayer player) {
		addPlayRecord(String.valueOf(getVideoView().getDuration()));
		// finish();
	}

	private void stopPlayer() {
		if (mVideoView != null)
			mVideoView.pause();
	}

	private void startPlayer() {
		if (mVideoView != null)
			mVideoView.start();
	}

	private boolean isPlaying() {
		return mVideoView != null && mVideoView.isPlaying();
	}

	@Override
	public void onSuccess(ServiceAction service, Object action, Object value) {
		super.onSuccess(service, action, value);
		String jsonString = value.toString();
		LogUtils.tiaoshi("action== " + action.toString(), jsonString);
		if (action == CommentAction.Action_GetLastVideo_By_Id) {
			ResultBean<AlbumLastVideo<VideoPlayInfo>> resultBean = JSON
					.parseObject(
							jsonString,
							new TypeReference<ResultBean<AlbumLastVideo<VideoPlayInfo>>>() {
							});
			if (resultBean != null) {
				try {
					AlbumLastVideo<VideoPlayInfo> albumLastVideo = resultBean.data;
					LatestVideo<VideoPlayInfo> latestVideo = albumLastVideo
							.getLatestVideo();
					if (!TextUtils.isEmpty(latestVideo.getName())) {
						mTitle = latestVideo.getName();
					}
					checkLiveAndPlay(latestVideo);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			} else {
				LogUtils.d("---resultBean == null");

			}
		} else if (action == CommentAction.Action_GetVideo_By_Id) {
			try {
				ResultBean<LatestVideo<VideoPlayInfo>> resultBean = JSON
						.parseObject(
								jsonString,
								new TypeReference<ResultBean<LatestVideo<VideoPlayInfo>>>() {
								});
				LatestVideo<VideoPlayInfo> albumLastVideo = resultBean.data;
				if (!TextUtils.isEmpty(albumLastVideo.getName())) {
					mTitle = albumLastVideo.getName();
				}
				checkLiveAndPlay(albumLastVideo);

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (action == CommentAction.Action_addPraiseResouce) {

			ResultPinglun<Pinglun> resultPinglun = JSON.parseObject(jsonString,
					new TypeReference<ResultPinglun<Pinglun>>() {
					});
			if (resultPinglun.getStatus() == 1) {
				Utils.toast(this, R.string.dianzan_success);
				setPriseCount(resultPinglun.getCount());
			} else {
				Utils.toast(this, resultPinglun.getMessage());
			}

		} else if (action == CommentAction.Action_shoucang_video) {
			Utils.toast(this, R.string.shoucang_success);
		} else if (action == CommentAction.Action_delshoucang_video) {
			Utils.toast(this, R.string.del_shoucang_success);
		} else if (action == CommentAction.Action_getVideoAd) {

			ResultList<Ad> resultBean = JSON.parseObject(jsonString,
					new TypeReference<ResultList<Ad>>() {
					});
			List<Ad> ads = resultBean.data;
			if (ads != null && ads.size() > 0) {
				mAds = ads;
				Ad ad = ads.get(0);
				adCount = ads.size();
				adUrl = ad.getUrl();
				bitmapTool.display(img_ad, ad.getBigImage());
			}

		}
	}

	private void checkLiveAndPlay(LatestVideo<VideoPlayInfo> albumLastVideo) {
		if (albumLastVideo.getIsNeedPay() == 1
				&& albumLastVideo.getIsPay() == 0) {
			DialogTool.createPayDialog(this,
					String.valueOf(albumLastVideo.getAlbumId()));
			LogUtils.tiaoshi(
					"albumLastVideo.getIsNeedPay() == 1&& albumLastVideo.getIsPay() == 0",
					"return");
			return;
		}
		String videoUrl;
		VideoPlayInfo videoPlayInfo = albumLastVideo.getVideoPlayInfo();
		videoUrl = videoPlayInfo.getVideoUrl();
		if (albumLastVideo.getVedioType() == 4) {
			LiveInfo liveInfo = albumLastVideo.getLiveInfo();
			LogUtils.d("albumLastVideo.getVedioType() == 4");
			if (liveInfo != null) {
				LogUtils.d("liveInfo != null");
				String livePath = liveInfo.getRtmp();
				if (TextUtils.isEmpty(livePath)) {
					LogUtils.d("TextUtils.isEmpty(livePath)");
					livePath = liveInfo.getHls();
				}
				if (!TextUtils.isEmpty(livePath)) {
					videoUrl = livePath;
				}
			}
			mMediaController.setLiveMode(true);
			isLive = true;
		} else {
			mMediaController.setLiveMode(false);
			isLive = false;
		}
		standardPic = albumLastVideo.getStandardPic();
		playVideoByUrl(videoPlayInfo.getVideoId() + "", videoUrl);
	}

	@Override
	public void onFaile(ServiceAction service, Object action, Object value) {
		LogUtils.tiaoshi("tiaoshi", value.toString());
		if ((action != CommentAction.Action_addPlayRecord)
				&& (action != CommentAction.Action_getVideoAd)) {
			Utils.toast(this, value.toString());
		}
	}

	@Override
	public void onException(ServiceAction service, Object action, Object value) {
		super.onException(service, action, value);
		LogUtils.tiaoshi("onException", action.toString());
	}

	/** 是否需要自动恢复播放，用于自动暂停，恢复播放 */
	private boolean needResume = true;

	@Override
	public boolean onInfo(MediaPlayer arg0, int arg1, int arg2) {
		switch (arg1) {
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			// 开始缓存，暂停播放
			if (isPlaying()) {
				stopPlayer();
				needResume = true;
			}
			mLoadingView.setVisibility(View.VISIBLE);
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			// 缓存完成，继续播放
			if (needResume)
				startPlayer();
			mLoadingView.setVisibility(View.GONE);
			break;
		case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
			// 显示 下载速度
			// mListener.onDownloadRateChanged(arg2);
			break;
		}
		return true;
	}

	/*
	 * (non-Javadoc) 用于锁定屏幕方向
	 * 
	 * @see
	 * com.modernsky.istv.view.MediaController.OnMediaControllerClickedListener
	 * #onBackClicked()
	 */
	@Override
	public void onBackClicked() {
		LogUtils.d("onBackClicked--");
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			setScreenOrientation(false);
		} else
			onBackPressed();
	}

	private void setScreenOrientation(boolean toLandScape) {
		int screenOrientationPortrait = toLandScape ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
				: ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		setRequestedOrientation(screenOrientationPortrait);
		getVideoView().postDelayed(new Runnable() {

			@Override
			public void run() {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
			}
		}, 1000);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.modernsky.istv.view.MediaController.OnMediaControllerClickedListener
	 * #onLockChanged(boolean)
	 */
	@Override
	public void onLockChanged(boolean locked) {
		if (locked) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

	}

	/*
	 * 
	 */
	@Override
	public void onShown() {
	}

	/*
	 * 
	 */
	@Override
	public void onHidden() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.modernsky.istv.listener.CommonListener#onStart(com.modernsky.istv
	 * .action.ServiceAction, java.lang.Object)
	 */
	@Override
	public void onStart(ServiceAction service, Object action) {
		LogUtils.d("onStart");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.modernsky.istv.listener.CommonListener#onFinish(com.modernsky.istv
	 * .action.ServiceAction, java.lang.Object)
	 */
	@Override
	public void onFinish(ServiceAction service, Object action) {
		LogUtils.d("onFinish");

	}

	// 点击返回按钮
	@Override
	public void onBackPressed() {
		if (fMgr.getBackStackEntryCount() <= 1) {
			finish();
		} else {
			super.onBackPressed();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.modernsky.istv.view.MediaController.OnMediaControllerClickedListener
	 * #onFangdaClicked()
	 */
	@Override
	public void onFangdaClicked() {
		setScreenOrientation(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.modernsky.istv.view.MediaController.OnMediaControllerClickedListener
	 * #onZanClicked()
	 */
	@Override
	public void onZanClicked() {
		praisVideo(videoId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.modernsky.istv.view.MediaController.OnMediaControllerClickedListener
	 * #onShouCangClicked()
	 */
	@Override
	public void onShouCangClicked() {
		shouCang(videoId);
	}

	/**
	 * 
	 * 添加收藏视频
	 */
	private void shouCang(String resouceId) {
		if (!isUesrLogined()) {
			return;
		}
		RequestParams params = UrlTool.getParams(Contansts.USER_ID, userId,
				Contansts.VIDEO_ID, resouceId);
		SendActtionTool.get(UserParams.URL_COLLECT_ADD,
				ServiceAction.Action_Comment,
				CommentAction.Action_shoucang_video, this, params);
	}

	/**
	 * 
	 * 删除收藏视频
	 */
	private void delShouCang(String resouceId) {
		if (!isUesrLogined()) {
			return;
		}
		RequestParams params = UrlTool.getParams(Contansts.USER_ID, userId,
				Contansts.VIDEO_ID, resouceId);
		SendActtionTool.get(UserParams.URL_Dell_Collect_VIDEO,
				ServiceAction.Action_Comment,
				CommentAction.Action_delshoucang_video, this, params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.modernsky.istv.view.MediaController.OnMediaControllerClickedListener
	 * #onJietuClicked()
	 */
	@Override
	public void onShareClicked() {
		share(mTitle, mTitle, videoId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.modernsky.istv.view.MediaController.OnMediaControllerClickedListener
	 * #onNextClicked()
	 */
	@Override
	public void onNextClicked() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.modernsky.istv.view.MediaController.OnMediaControllerClickedListener
	 * #onStartOrPauseClicked(boolean)
	 */
	@Override
	public void onStartOrPauseClicked(boolean start) {
		tongleAd(!start);
	}

}
