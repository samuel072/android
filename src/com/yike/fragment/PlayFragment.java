package com.yike.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lidroid.xutils.http.RequestParams;
import com.yike.R;
import com.yike.LoginActivity;
import com.yike.PlayActivity;
import com.yike.action.CommentAction;
import com.yike.action.ServiceAction;
import com.yike.adapter.PlayListAdapter;
import com.yike.adapter.PlayPinglunListAdapter;
import com.yike.bean.Content;
import com.yike.bean.LatestVideo;
import com.yike.bean.Pinglun;
import com.yike.bean.ResultList;
import com.yike.bean.ResultPinglun;
import com.yike.bean.ResultPinglunList;
import com.yike.bean.UserBean;
import com.yike.bean.UserEntity;
import com.yike.bean.VideoPinglun;
import com.yike.bean.VideoPlayInfo;
import com.yike.listener.CommonListener;
import com.yike.service.UserService;
import com.yike.tool.SendActtionTool;
import com.yike.tool.UrlTool;
import com.yike.utils.Contansts;
import com.yike.utils.LogUtils;
import com.yike.utils.Utils;
import com.yike.utils.Contansts.UserParams;
import com.yike.view.NoScroListView;
import com.yike.view.PinglunDialog;
import com.yike.view.PinglunDialog.OnPinglunCompleteLisenter;

/**
 * 播放界面fragment
 * 
 * @author zxm
 * 
 */
public class PlayFragment extends BaseFragment implements CommonListener,
		OnItemClickListener {
	private NoScroListView mXuanjiListView;
	private PlayListAdapter listAdapter;
	private NoScroListView mPinglunListView;
	private PlayPinglunListAdapter playPinglunAdapter;
	PlayActivity playActivity;
	private List<LatestVideo<VideoPlayInfo>> latestVideos;
	private List<VideoPinglun> pingluns;
	private int commentCount;
	private OnDianZanListener dianZanListener;
	private boolean addClicked;
	private String lastBuildTime;

	public interface OnDianZanListener {
		void onCompletion(String commentId, int count);
	}

	/**
	 * 获取视频列表
	 */
	public void getAlbumVideo(String albumId) {
		RequestParams params = UrlTool.getParams(Contansts.ALBUM_ID, albumId,
				Contansts.FILTER, Contansts.FILTER_ALBUM_VIDEO);

		SendActtionTool.get(Contansts.URL_ALL_VIDEO_LIST,
				ServiceAction.Action_Comment,
				CommentAction.Action_getAlbumVideo, this, params);
		LogUtils.tiaoshi("albumId", albumId);
	}

	private int statue = 2;

	/**
	 * 获取普通评论列表
	 */
	public void getPinglunList(String albumId, String buildTime) {

		RequestParams params = UrlTool.getParams(Contansts.RESOURCE_ID,
				albumId, Contansts.BUILD_TIME, buildTime, Contansts.USER_ID,
				"0", Contansts.STATUS, String.valueOf(statue));

		SendActtionTool.get(UserParams.URL_COMMENT_LIST,
				ServiceAction.Action_Comment,
				CommentAction.Action_getPinglunList, this, params);
	}

	@Override
	public void onSuccess(ServiceAction service, Object action, Object value) {
		String jsonString = value.toString();
		LogUtils.tiaoshi("action:" + action.toString(), jsonString);

		if (action == CommentAction.Action_getAlbumVideo) {
			try {
				ResultList<LatestVideo<VideoPlayInfo>> resultBean = JSON
						.parseObject(
								jsonString,
								new TypeReference<ResultList<LatestVideo<VideoPlayInfo>>>() {
								});
				List<LatestVideo<VideoPlayInfo>> albumLastVideo = resultBean.data;
				if (albumLastVideo != null) {
					latestVideos.clear();
					latestVideos.addAll(albumLastVideo);
					initXuanji();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			getPinglunList(playActivity.getVideoId(), "0");
		} else if (action == CommentAction.Action_addPinglun) {

			ResultPinglun<Pinglun> resultPinglun = JSON.parseObject(jsonString,
					new TypeReference<ResultPinglun<Pinglun>>() {
					});
			Pinglun pinglun2 = resultPinglun.getData();
			if (resultPinglun.getStatus() == 1) {
				Utils.toast(playActivity, R.string.pinglun_success);
				VideoPinglun pinglun = new VideoPinglun();
				pinglun.setId(pinglun2.getId());
				UserEntity entity = new UserEntity();
				entity.setUserName(userName);
				entity.setFaceUrl(faceUrl);
				entity.setId(pinglun2.getUserId());
				pinglun.setUserEntity(entity);
				pinglun.setBuildTime(System.currentTimeMillis());
				pinglun.setContent(mContents);
				pingluns.add(0, pinglun);
				commentCount++;
				initPlunlunList();
			} else {
				Utils.toast(playActivity, resultPinglun.getMessage());
			}

		} else if (action == CommentAction.Action_getPinglunList) {

			ResultPinglunList<VideoPinglun> resultPingluns = JSON.parseObject(
					jsonString,
					new TypeReference<ResultPinglunList<VideoPinglun>>() {
					});
			commentCount = resultPingluns.getCommentCount();
			playActivity.setPriseCount(resultPingluns.getPraiseCount());
			List<VideoPinglun> tempPingluns = resultPingluns.getData();
			if (tempPingluns != null) {
				if (!addClicked) {
					pingluns.clear();
					addClicked = false;
					ll_footView.setEnabled(true);
				}
				pingluns.addAll(tempPingluns);
				initPlunlunList();
				if (tempPingluns.size() < 10) {
					if (statue == 2) {
						statue = 1;
						getPinglunList(playActivity.getVideoId(), "0");
					}
				}
			} else {
				statue = 1;
				getPinglunList(playActivity.getVideoId(), "0");
			}

		} else if (action == CommentAction.Action_addPraiseComment) {

			ResultPinglun<Pinglun> resultPinglun = JSON.parseObject(jsonString,
					new TypeReference<ResultPinglun<Pinglun>>() {
					});
			Pinglun data = resultPinglun.getData();
			if (resultPinglun.getStatus() == 1) {
				Utils.toast(playActivity, R.string.dianzan_success);
				if (dianZanListener != null) {
					dianZanListener.onCompletion(data.getCommentId(),
							resultPinglun.getCount());
				}
			} else {
				Utils.toast(getActivity(), resultPinglun.getMessage());
			}
		}

	}

	@Override
	public void onFaile(ServiceAction service, Object action, Object value) {
		Utils.toast(getActivity(), value.toString());
	}

	@Override
	public void onException(ServiceAction service, Object action, Object value) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		VideoPinglun videoPinglun = pingluns.get(position);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Contansts.CONTENT, videoPinglun);
		PinglunFragmet pinglunFragmet = new PinglunFragmet(bundle);
		playActivity.setFragment(pinglunFragmet, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.footbar_play:
			showPingLunDialog();
			break;
		case R.id.ll_footView:
			addClicked = true;
			getPinglunList(playActivity.getVideoId(), lastBuildTime);
			break;

		default:
			break;
		}

	}

	PinglunDialog pinglunView;
	private TextView headText;
	private String userId = "";
	private int userStatus;
	private TextView pintlunTv;
	private View ll_footView;
	private List<Content> mContents;
	private View xuanjiHeaView;
	private String userName;
	private String faceUrl;

	/**
	 * 评论弹窗
	 */
	private void showPingLunDialog() {

		// 判断用户状态
		if (!isUesrLogined())
			return;

		if (pinglunView == null) {
			pinglunView = new PinglunDialog(getActivity(),
					R.style.MmsDialogTheme, new OnPinglunCompleteLisenter() {

						@Override
						public void onComplete(List<Content> contents) {
							if (contents != null) {
								mContents = contents;
								String string = JSON.toJSONString(contents);
								RequestParams params = UrlTool.getParams(
										Contansts.RESOURCE_ID,
										playActivity.getVideoId(),
										Contansts.USER_ID, userId,
										Contansts.CONTENT, string,
										Contansts.STATUS,
										String.valueOf(userStatus));
								SendActtionTool.get(UserParams.URL_ADD_COMMENT,
										ServiceAction.Action_Comment,
										CommentAction.Action_addPinglun,
										PlayFragment.this, params);
							}

						}
					});
		}
		pinglunView.cleanText();
		pinglunView.show();

	}

	/**
	 * 判断用户状态，如果未登录，跳转登录界面
	 */
	public boolean isUesrLogined() {
		if (TextUtils.isEmpty(userId)) {
			if (UserService.getInatance().isNeedLogin(getActivity())) {
				startActivity(new Intent(getActivity(), LoginActivity.class));
				return false;
			} else {
				UserBean bean = UserService.getInatance().getUserBean();
				userId = bean.getId();
				userStatus = bean.getStatus();
				userName = bean.getUserName();
				faceUrl = bean.getFaceUrl();

				return true;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.modernsky.istv.fragment.BaseFragment#setContentView(android.view.
	 * LayoutInflater)
	 */
	@Override
	public View setContentView(LayoutInflater inflater) {
		playActivity = (PlayActivity) getActivity();
		return inflater.inflate(R.layout.fragment_play, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.modernsky.istv.fragment.BaseFragment#initView(android.view.View)
	 */
	@Override
	public void initView(View rootView) {
		latestVideos = new ArrayList<LatestVideo<VideoPlayInfo>>();
		pingluns = new ArrayList<VideoPinglun>();
		// 初始化控件
		pintlunTv = (TextView) rootView.findViewById(R.id.footbar_play);
		mXuanjiListView = (NoScroListView) rootView
				.findViewById(R.id.listview_xuanji);
		LayoutInflater inflater = LayoutInflater.from(playActivity);
		if (xuanjiHeaView == null) {
			xuanjiHeaView = inflater.inflate(R.layout.item_listview_headview,
					null);
		}
		headText = (TextView) xuanjiHeaView.findViewById(R.id.tv_video_name);
		mXuanjiListView.addHeaderView(xuanjiHeaView);
		initXuanji();
		mXuanjiListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position < mXuanjiListView.getHeaderViewsCount()) {
					return;
				}
				try {
					int index = position
							- mXuanjiListView.getHeaderViewsCount();
					LatestVideo<VideoPlayInfo> latestVideo = latestVideos
							.get(index);
					VideoPlayInfo info = latestVideo.getVideoPlayInfo();
					String strUrl = info.getVideoUrl();
					String videoId = String.valueOf(info.getVideoId());
					if (!playActivity.getVideoId().equals(videoId)) {
						playActivity.playVideoByUrl(videoId, strUrl,
								latestVideo.getName());
						getPinglunList(videoId, "0");
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});
		if (ll_footView == null) {
			ll_footView = inflater.inflate(R.layout.foot_playpinglun, null);
		}
		ll_footView.setOnClickListener(this);
		mPinglunListView = (NoScroListView) rootView
				.findViewById(R.id.listview_pinglun);
		mPinglunListView.setOnItemClickListener(this);
		pintlunTv.setOnClickListener(this);
		// 初始化评论内容
		initPlunlunList();
		mPinglunListView.setOnScrollListener(playPinglunAdapter);
	}

	public void initPlunlunList() {
		playPinglunAdapter = new PlayPinglunListAdapter(pingluns,
				getActivity(), this);
		pintlunTv.setText(commentCount + "条评论");
		if (commentCount > pingluns.size()) {
			if (mPinglunListView.getFooterViewsCount() <= 0) {
				mPinglunListView.addFooterView(ll_footView);
			}
		} else {
			mPinglunListView.removeFooterView(ll_footView);
		}
		if (pingluns != null && pingluns.size() > 0) {
			VideoPinglun laseVideoPinglun = pingluns.get(pingluns.size() - 1);
			if (laseVideoPinglun != null) {
				lastBuildTime = String.valueOf(laseVideoPinglun.getBuildTime());
			}
		}
		mPinglunListView.setAdapter(playPinglunAdapter);
	}

	private void initXuanji() {
		listAdapter = new PlayListAdapter(latestVideos, getActivity());
		headText.setText("选集（共" + latestVideos.size() + "个视频）");
		mXuanjiListView.setAdapter(listAdapter);
	}

	/**
	 * 点赞请求
	 * 
	 * @param position
	 */
	public void dianZan(String commentId, String toUserId,
			OnDianZanListener dianZanListener) {
		isUesrLogined();
		setDianZanListener(dianZanListener);
		RequestParams params = UrlTool.getParams(Contansts.COMMENT_ID,
				commentId, Contansts.USER_ID, userId, Contansts.TO_USER_ID,
				toUserId, Contansts.STATUS, String.valueOf(userStatus));
		LogUtils.tiaoshi("dianzan", Contansts.COMMENT_ID + "," + commentId
				+ "," + Contansts.USER_ID + "," + userId + ","
				+ Contansts.TO_USER_ID + "," + toUserId + ","
				+ Contansts.STATUS + "," + String.valueOf(userStatus));
		SendActtionTool.get(UserParams.URL_ADD_PRAISE_COMMENT,
				ServiceAction.Action_Comment,
				CommentAction.Action_addPraiseComment, this, params);
	}

	public OnDianZanListener getDianZanListener() {
		return dianZanListener;
	}

	public void setDianZanListener(OnDianZanListener dianZanListener) {
		this.dianZanListener = dianZanListener;
	}
}
