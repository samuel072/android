package com.yike.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.http.RequestParams;
import com.yike.R;
import com.yike.LoginActivity;
import com.yike.PlayActivity;
import com.yike.action.CommentAction;
import com.yike.action.ServiceAction;
import com.yike.adapter.PinglunDetailAdapter;
import com.yike.adapter.PinglunGridAdapter;
import com.yike.bean.Content;
import com.yike.bean.Huifu;
import com.yike.bean.Pinglun;
import com.yike.bean.Prais;
import com.yike.bean.ResultBean;
import com.yike.bean.ResultPinglun;
import com.yike.bean.UserBean;
import com.yike.bean.UserEntity;
import com.yike.bean.VideoPinglun;
import com.yike.service.UserService;
import com.yike.tool.BitmapTool;
import com.yike.tool.SendActtionTool;
import com.yike.tool.TimeTool;
import com.yike.tool.UrlTool;
import com.yike.utils.Contansts;
import com.yike.utils.LogUtils;
import com.yike.utils.Utils;
import com.yike.utils.Contansts.UserParams;
import com.yike.view.MyGridView;
import com.yike.view.PinglunDialog;
import com.yike.view.RoundAngleImageView;
import com.yike.view.PinglunDialog.OnPinglunCompleteLisenter;

public class PinglunFragmet extends BaseFragment implements OnItemClickListener {
	private ListView listView;
	private View headerView, praisHeadView;
	private PinglunDetailAdapter adapter;
	private List<Huifu> list;
	private List<Prais> praiseList;
	private ViewHolder vh;
	private String userId, resouseId;
	private int userStatus;
	private PlayActivity playActivity;
	private TextView tv_replay;
	private String resouseUserId;
	private String resouseUserName;
	private TextView praiseTextView;
	private BitmapUtils bitmapTool;
	private String userName;
	private long praiseCount;
	private String faceUrl;

	/**
	 * @param bundle
	 */
	public PinglunFragmet(Bundle bundle) {
		setArguments(bundle);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.text_back_pinglun:
			getActivity().onBackPressed();
			break;
		case R.id.footbar_play:
			showPingLunDialog(resouseUserId, resouseUserName);
			break;

		default:
			break;
		}
	}

	@Override
	public View setContentView(LayoutInflater inflater) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_pinglun, null);
	}

	@Override
	public void initView(View rootView) {
		list = new ArrayList<Huifu>();
		praiseList = new ArrayList<Prais>();

		VideoPinglun pinglun = (VideoPinglun) getArguments().get(
				Contansts.CONTENT);
		resouseId = pinglun.getId();
		UserEntity userEntity = pinglun.getUserEntity();
		if (userEntity != null) {
			resouseUserId = userEntity.getId();
		}
		playActivity = (PlayActivity) getActivity();
		rootView.findViewById(R.id.text_back_pinglun).setOnClickListener(this);
		tv_replay = (TextView) rootView.findViewById(R.id.footbar_play);
		tv_replay.setOnClickListener(this);
		bitmapTool = BitmapTool.getInstance().getAdapterUitl();
		LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
		headerView = layoutInflater.inflate(R.layout.item_listview_pinglun,
				null);
		praisHeadView = layoutInflater.inflate(R.layout.head_zan, null);
		praiseTextView = (TextView) praisHeadView.findViewById(R.id.tv_header);
		listView = (ListView) rootView
				.findViewById(R.id.listview_pinglun_detail);
		initHeaderView(headerView, pinglun);
		listView.addHeaderView(headerView);
		initParisHeadView();
		initPlunlunList();
		getPinglunList(pinglun.getId());
		listView.setOnItemClickListener(this);

	}

	/**
	 * 初始化点赞列表
	 */
	private void initParisHeadView() {
		// TODO Auto-generated method stub
		if (praiseList != null && praiseList.size() > 0) {
			if (listView.getHeaderViewsCount() < 2) {
				listView.addHeaderView(praisHeadView);
			}
			refreshPraisHeadView();
		} else {
			listView.removeHeaderView(praisHeadView);
		}
	}

	/**
	 * 刷新点赞列表
	 */
	public void refreshPraisHeadView() {
		String text = "";
		for (Prais prais : praiseList) {
			text += prais.getUserName() + ",";
		}
		text = text.substring(0, text.length() - 1);
		praiseTextView.setText(text);
	}

	/**
	 * 初始化头部view
	 * 
	 * @param pinglun
	 * @param headerView2
	 */
	private void initHeaderView(View convertView, VideoPinglun pinglun) {

		if (pinglun == null) {
			return;
		}
		if (vh == null) {
			vh = new ViewHolder();
			vh.imageView = (RoundAngleImageView) convertView
					.findViewById(R.id.img_grid);
			vh.name = (TextView) convertView.findViewById(R.id.tv_video_name);
			vh.text = (TextView) convertView.findViewById(R.id.textView2);
			vh.time = (TextView) convertView.findViewById(R.id.time);
			vh.good = (TextView) convertView.findViewById(R.id.pinlun_good);
			vh.pinglun = (TextView) convertView.findViewById(R.id.pinlun_count);
			vh.myGridView = (MyGridView) convertView
					.findViewById(R.id.gridview_paly_pinglun);
			vh.good.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dianzZan(resouseId, resouseUserId);
					vh.good.setEnabled(false);
				}
			});
		}
		// setViewHolder(vh, pinglun);
	}

	private void dianzZan(String commentId, String toUserId) {
		if (!isUesrLogined()) {
			return;
		}
		RequestParams params = UrlTool.getParams(Contansts.COMMENT_ID,
				commentId, Contansts.USER_ID, userId, Contansts.TO_USER_ID,
				toUserId, Contansts.STATUS, String.valueOf(userStatus));

		SendActtionTool.get(UserParams.URL_ADD_PRAISE_COMMENT,
				ServiceAction.Action_Comment,
				CommentAction.Action_addPraiseComment, this, params);
	}

	/**
	 * 填充头部view内容
	 * 
	 * @param vh
	 * @param videoPinglun
	 */
	public void setViewHolder(final ViewHolder vh, VideoPinglun videoPinglun) {
		UserEntity userEntity = videoPinglun.getUserEntity();
		if (userEntity != null) {
			resouseUserId = userEntity.getId();
			resouseUserName = userEntity.getUserName();
			bitmapTool.display(vh.imageView, userEntity.getFaceUrl());
			vh.name.setText(userEntity.getUserName());
		}
		vh.time.setText(TimeTool.getTimeString(videoPinglun.getBuildTime(),
				getActivity()));
		if (videoPinglun.getPraiseCount() > praiseCount) {
			praiseCount = videoPinglun.getPraiseCount();
		}
		vh.good.setText("赞(" + praiseCount + ")");
		vh.pinglun.setText("评论(" + videoPinglun.getCommentCount() + ")");

		List<Content> contents = videoPinglun.getContent();
		List<Content> tempContents = new ArrayList<Content>();
		if (contents == null) {
			vh.myGridView.setVisibility(View.GONE);
		} else {
			for (int i = contents.size() - 1; i >= 0; i--) {
				Content map = contents.get(i);
				if (map.getType().equals("1")) {
					vh.text.setText(map.getContent());
				} else {
					tempContents.add(map);
				}
			}
			if (contents.size() > 0) {
				vh.myGridView.setVisibility(View.VISIBLE);
				PinglunGridAdapter pinglunGridAdapter = new PinglunGridAdapter(
						tempContents, getActivity());
				vh.myGridView.setAdapter(pinglunGridAdapter);
				// Utils.setGridViewHeightBasedOnChildren(vh.myGridView, 3);
			}

		}
	}

	/**
	 * 获取评论回复列表
	 */
	public void getPinglunList(String commentId) {
		LogUtils.tiaoshi("请求时间：", "" + System.currentTimeMillis());
		LogUtils.tiaoshi("getPinglunHuifuList", commentId);
		RequestParams params = UrlTool.getParams(Contansts.COMMENT_ID,
				commentId, Contansts.USER_ID, "0");

		SendActtionTool.get(UserParams.URL_COMMENT_ONE,
				ServiceAction.Action_Comment,
				CommentAction.Action_getPinglunList, this, params);
	}

	class ViewHolder {
		RoundAngleImageView imageView;
		TextView name, text, time, good, pinglun;
		MyGridView myGridView;
	}

	@Override
	public void onSuccess(ServiceAction service, Object action, Object value) {
		String jsonString = value.toString();
		LogUtils.tiaoshi("action:" + action.toString(), jsonString);
		if (action == CommentAction.Action_getPinglunList) {

			ResultBean<VideoPinglun> resultPingluns = JSON.parseObject(
					jsonString, new TypeReference<ResultBean<VideoPinglun>>() {
					});
			VideoPinglun data = resultPingluns.data;
			setViewHolder(vh, data);
			List<Prais> praises = data.getPraises();
			if (praises != null) {
				LogUtils.tiaoshi("praises", praises.size() + "");
				praiseList.clear();
				praiseList.addAll(praises);
				// refreshPraisHeadView();
				initParisHeadView();
			} else
				LogUtils.tiaoshi("praises", "praises == null");

			List<Huifu> comments = data.getComments();
			if (comments != null) {
				list.clear();
				list.addAll(comments);
				tv_replay.setText(list.size() + "条回复");
				adapter.notifyDataSetChanged();
			} else {
				LogUtils.tiaoshi("comments", "comments == null");
			}

		} else if (action == CommentAction.Action_addHuifu) {
			ResultPinglun<Pinglun> resultPinglun = JSON.parseObject(jsonString,
					new TypeReference<ResultPinglun<Pinglun>>() {
					});
			if (resultPinglun != null) {
				if (resultPinglun.getStatus() == 1) {
					Utils.toast(getActivity(), "回复成功");
					Huifu huifu = new Huifu();
					huifu.setContent(mContent);
					huifu.setBuildTime(System.currentTimeMillis());

					UserEntity userEntity = new UserEntity();
					userEntity.setFaceUrl(faceUrl);
					userEntity.setUserName(userName);
					userEntity.setId(userId);
					userEntity.setStatus(String.valueOf(userStatus));

					huifu.setUserEntity(userEntity);
					huifu.setToUserEntity(mToUserEntity);
					list.add(huifu);
				}
			}

		} else if (action == CommentAction.Action_addPraiseComment) {

			ResultPinglun<Pinglun> resultPinglun = JSON.parseObject(jsonString,
					new TypeReference<ResultPinglun<Pinglun>>() {
					});
			if (resultPinglun.getStatus() == 1) {
				if (resultPinglun.getCount() > praiseCount) {
					praiseCount = resultPinglun.getCount();
				}
				vh.good.setText("赞(" + praiseCount + ")");
				Prais prais = new Prais();
				prais.setId(userId);
				prais.setUserId(userId);
				prais.setUserName(userName);
				praiseList.add(0, prais);
				initParisHeadView();
				Utils.toast(playActivity, R.string.dianzan_success);
			} else {
				Utils.toast(getActivity(), resultPinglun.getMessage());
			}
		}

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

	private List<Content> mContent;
	private UserEntity mToUserEntity;

	/**
	 * 评论弹窗
	 */
	private void showPingLunDialog(final String toUserId,
			final String toUserName) {

		if (!isUesrLogined())
			return;
		PinglunDialog pinglunView = new PinglunDialog(getActivity(),
				R.style.MmsDialogTheme, toUserName,
				new OnPinglunCompleteLisenter() {

					@Override
					public void onComplete(List<Content> content) {
						if (content != null) {
							mContent = content;
							String string = JSON.toJSONString(content);
							RequestParams params = UrlTool.getParams(
									Contansts.COMMENT_ID, resouseId,
									Contansts.USER_ID, userId,
									Contansts.TO_USER_ID, toUserId,
									Contansts.CONTENT, string,
									Contansts.STATUS, userStatus + "");
							SendActtionTool.get(UserParams.URL_ADD_REPLY,
									ServiceAction.Action_Comment,
									CommentAction.Action_addHuifu,
									PinglunFragmet.this, params);
						}

					}
				});
		pinglunView.show();

	}

	/**
	 * 初始化评论回复列表
	 */
	private void initPlunlunList() {
		LogUtils.tiaoshi("initPlunlunList", list.size() + "");
		adapter = new PinglunDetailAdapter(list, resouseUserId, getActivity());
		listView.setAdapter(adapter);
		tv_replay.setText(list.size() + "条回复");
	}

	@Override
	public void onFaile(ServiceAction service, Object action, Object value) {
		Utils.toast(getActivity(), value.toString());
	}

	@Override
	public void onException(ServiceAction service, Object action, Object value) {
		// TODO Auto-generated method stub
		super.onException(service, action, value);
	}

	@Override
	public void onStart(ServiceAction service, Object action) {
		// TODO Auto-generated method stub
		super.onStart(service, action);
	}

	@Override
	public void onFinish(ServiceAction service, Object action) {
		// TODO Auto-generated method stub
		super.onFinish(service, action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		int position2 = position - listView.getHeaderViewsCount();
		if (position2 >= 0) {
			Huifu item = list.get(position2);
			if (item != null) {
				mToUserEntity = item.getUserEntity();
				showPingLunDialog(mToUserEntity.getId(),
						mToUserEntity.getUserName());
			} else {
				showPingLunDialog(resouseUserId, resouseUserName);
			}
		}
	}

}
