package com.yike.fragment;
///**
// * 
// */
//package com.modernsky.istv.fragment;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.Button;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.TypeReference;
//import com.lidroid.xutils.http.RequestParams;
//import com.modernsky.istv.OrderActivity;
//import com.modernsky.istv.R;
//import com.modernsky.istv.WebActivity;
//import com.modernsky.istv.action.PayAction;
//import com.modernsky.istv.action.ServiceAction;
//import com.modernsky.istv.adapter.OrderGridAdapter;
//import com.modernsky.istv.bean.ResultList;
//import com.modernsky.istv.bean.Ticket;
//import com.modernsky.istv.bean.YouHuiQuan;
//import com.modernsky.istv.tool.SendActtionTool;
//import com.modernsky.istv.tool.UrlTool;
//import com.modernsky.istv.utils.Contansts;
//import com.modernsky.istv.utils.Contansts.UserParams;
//import com.modernsky.istv.utils.LogUtils;
//import com.modernsky.istv.utils.Utils;
//import com.modernsky.istv.view.MyGridView;
//
///**
// * @author： fengqingyun2008
// * @Email： fengqingyun2008@gmail.com
// * @version：1.0
// * @创建时间：2015-4-26 下午12:18:44
// * @类说明：
// */
//public class OrderFragment extends BaseFragment implements OnItemClickListener {
//	private TextView tv_buyCount, tv_total, tv_youhuiquan;
//	private List<Ticket> mTickets = new ArrayList<Ticket>();
//	private List<YouHuiQuan> youHuiQuans;
//
//	private int money = 0;// 元
//
//	public enum PayCallBack {
//		PayCencal, PaySucess, PayFaile
//	}
//
//	private MyGridView gridView;
//	private TextView tv_order_total;
//	private String albumId = "22";
//	private RadioGroup radioGroup;
//	private OrderGridAdapter orderGridAdapter;
//	private TextView tv_showtime;
//	private String url_show_time;
//	private String userId;
//	private OrderActivity activity;
//	private String youhuiCoce;
//	private int youhuimaIndex = -1;
//	private int dayIndex;
//	private Button payBtn;
//
//	@Override
//	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//	}
//
//	private void getYouHuiQuan(String userId) {
//		RequestParams params = UrlTool.getParams(Contansts.USER_ID, userId);
//
//		SendActtionTool.post(UserParams.URL_YOUHUIQUAN,
//				ServiceAction.Action_Pay, PayAction.Action_getYouHuiQuan, this,
//				params);
//	}
//
//	/*
//	 * @see android.view.View.OnClickListener#onClick(android.view.View)
//	 */
//	@Override
//	public void onClick(View v) {
//
//		switch (v.getId()) {
//		case R.id.btn_pay:
//			v.setEnabled(false);
//			choice2pay();
//			break;
//		case R.id.btn_back:
//			activity.onBackPressed();
//			break;
//		case R.id.rl_youhuima:
//			activity.setFragment(new YouHuiQuanFragment(userId), true);
//			break;
//		case R.id.rl_show_time:
//			Intent intent = new Intent(getActivity(), WebActivity.class);
//			intent.putExtra(Contansts.URL, url_show_time);
//			intent.putExtra(Contansts.TITLE, tv_showtime.getText().toString());
//			startActivity(intent);
//			break;
//
//		default:
//			break;
//		}
//
//	}
//
//	public void setPayBtnEnabled(boolean enabled) {
//		if (payBtn != null) {
//			payBtn.setEnabled(enabled);
//		}
//
//	}
//
//	/**
//	 * 选择支付通道和支付
//	 */
//	private void choice2pay() {
//		int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
//
//		switch (checkedRadioButtonId) {
//		case R.id.rb_zhifubao:
//
//			break;
//		case R.id.rb_zhifubao_wap:
//
//			break;
//		case R.id.rb_weixin:
//			Ticket ticket = mTickets.get(dayIndex);
//			if (ticket == null) {
//				return;
//			}
//			String code = "";
//			if (youHuiQuans != null && youhuimaIndex < youHuiQuans.size()
//					&& youhuimaIndex >= 0) {
//				YouHuiQuan huiQuan = youHuiQuans.get(youhuimaIndex);
//				if (huiQuan != null) {
//					code = huiQuan.getCode();
//				}
//			}
//			// activity.getWXOrder(ticket.getScheduleName(), userId,
//			// ticket.getVideoIds(), String.valueOf(1), code);
//			activity.getWXOrder(ticket.getScheduleName(), userId,
//					ticket.getVideoIds(), String.valueOf(getMoney() * 100),
//					code);
//			break;
//
//		default:
//			break;
//		}
//	}
//
//	private void initTicket() {
//		orderGridAdapter = new OrderGridAdapter(mTickets, activity);
//		gridView.setAdapter(orderGridAdapter);
//	}
//
//	private void getDataTimes(String albumId, String userId) {
//
//		RequestParams params = UrlTool.getParams(Contansts.ALBUM_ID, albumId,
//				Contansts.USER_ID, userId);
//
//		SendActtionTool.postNoCheck(Contansts.URL_GET_TICKET_LIST,
//				ServiceAction.Action_Pay, PayAction.Action_getTicketList, this,
//				params);
//	}
//
//	@Override
//	public void onSuccess(ServiceAction service, Object action, Object value) {
//		super.onSuccess(service, action, value);
//		String string = value.toString();
//		LogUtils.tiaoshi("onSuccess---" + action.toString(), value.toString());
//		switch ((PayAction) action) {
//		case Action_getTicketList:
//			ResultList<Ticket> list = JSON.parseObject(string,
//					new TypeReference<ResultList<Ticket>>() {
//					});
//			List<Ticket> data = list.data;
//			List<Ticket> tempData = new ArrayList<Ticket>();
//
//			if (data != null && data.size() > 0) {
//				for (int i = data.size() - 1; i >= 0; i--) {
//					Ticket ticket = data.get(i);
//					if (ticket.getType() == 1) {
//						tempData.add(ticket);
//						data.remove(ticket);
//					}
//				}
//				mTickets.clear();
//				mTickets.addAll(data);
//				mTickets.addAll(tempData);
//				orderGridAdapter.notifyDataSetChanged();
//				Ticket ticket = mTickets.get(0);
//				money = Integer.parseInt(ticket.getPrice());
//				upDateView(ticket);
//				LogUtils.tiaoshi("Ticket", "notifyDataSetChanged");
//			}
//			break;
//		case Action_getYouHuiQuan:
//			ResultList<YouHuiQuan> resultList = JSON.parseObject(string,
//					new TypeReference<ResultList<YouHuiQuan>>() {
//					});
//			List<YouHuiQuan> tempYouHuiQuans = resultList.data;
//			if (tempYouHuiQuans != null && tempYouHuiQuans.size() > 0) {
//				youHuiQuans.clear();
//				youHuiQuans.addAll(tempYouHuiQuans);
//				setYouhuiquanText(youHuiQuans.size());
//			}
//			break;
//
//		default:
//			break;
//		}
//
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		if (!TextUtils.isEmpty(youhuiCoce)) {
//			setYouHuiMa(youhuiCoce);
//		}
//	}
//
//	public void setYouhuiquanText(int size) {
//		tv_youhuiquan.setText("优惠券   " + size + "张");
//	}
//
//	/*
//	 * @see com.modernsky.istv.BaseActivity#onFaile(com.modernsky.istv.action.
//	 * ServiceAction, java.lang.Object, java.lang.Object)
//	 */
//	@Override
//	public void onFaile(ServiceAction service, Object action, Object value) {
//		super.onFaile(service, action, value);
//	}
//
//	/*
//	 * @see
//	 */
//	@Override
//	public void onException(ServiceAction service, Object action, Object value) {
//		// TODO Auto-generated method stub
//		super.onException(service, action, value);
//	}
//
//	/*
//	 * @see
//	 */
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		dayIndex = position;
//		Ticket ticket = mTickets.get(position);
//		if (ticket.getIsPay() != 0) {
//			Utils.toast(activity, "您已经购买本场次，无需再次购买。");
//			return;
//		}
//
//		setMoney(Integer.parseInt(ticket.getPrice()));
//		checkMoney(position, youhuimaIndex);
//		upDateView(ticket);
//
//	}
//
//	private void upDateView(Ticket ticket) {
//		String ids = ticket.getVideoIds();
//		String[] split = ids.split(",");
//		if (split != null) {
//			tv_buyCount.setText(split.length + "");
//		} else
//			tv_buyCount.setText("1");
//		tv_total.setText(ticket.getPrice() + "元");
//		tv_order_total.setText(String.valueOf(getMoney()));
//		tv_showtime.setText(ticket.getScheduleName());
//		url_show_time = ticket.getSchedule();
//	}
//
//	@Override
//	public View setContentView(LayoutInflater inflater) {
//		return inflater.inflate(R.layout.fragment_order, null);
//	}
//
//	@Override
//	public void initView(View rootView) {
//		activity = (OrderActivity) getActivity();
//		userId = activity.getUserId();
//		albumId = activity.getAlbumId();
//
//		rootView.findViewById(R.id.btn_back).setOnClickListener(this);
//		rootView.findViewById(R.id.rl_youhuima).setOnClickListener(this);
//		payBtn = (Button) rootView.findViewById(R.id.btn_pay);
//		payBtn.setOnClickListener(this);
//		rootView.findViewById(R.id.rl_show_time).setOnClickListener(this);
//		tv_buyCount = (TextView) rootView.findViewById(R.id.textView4);
//		tv_total = (TextView) rootView.findViewById(R.id.textView5);
//		tv_order_total = (TextView) rootView.findViewById(R.id.tv_order_total);
//		tv_youhuiquan = (TextView) rootView
//				.findViewById(R.id.tv_youhuiquan_count);
//		tv_showtime = (TextView) rootView.findViewById(R.id.tv_show_time);
//		gridView = (MyGridView) rootView.findViewById(R.id.gv_order_time);
//		gridView.setOnItemClickListener(this);
//		initTicket();
//		radioGroup = (RadioGroup) rootView.findViewById(R.id.gr_pay);
//
//		getDataTimes(albumId, userId);
//		if (youHuiQuans == null) {
//			youHuiQuans = new ArrayList<YouHuiQuan>();
//			getYouHuiQuan(userId);
//		}
//	}
//
//	private void checkMoney(int day, int index) {
//		if (mTickets == null || youHuiQuans == null) {
//			return;
//		}
//		if (youhuimaIndex < 0) {
//			return;
//		}
//		if (day >= mTickets.size() || youhuimaIndex >= youHuiQuans.size()) {
//			return;
//		}
//		Ticket ticket = mTickets.get(day);
//		setMoney(Integer.parseInt(ticket.getPrice()));
//		YouHuiQuan huiQuan = youHuiQuans.get(index);
//		if (ticket == null || huiQuan == null) {
//			return;
//		}
//		if (ticket.getType() == 1) {
//			if (huiQuan.getType() == 2) {
//				setMoney(0);
//			} else if (huiQuan.getType() == 1) {
//				Utils.toast(activity, "单日抵用券不能抵扣全场通票");
//			} else if (huiQuan.getType() == 3) {
//				setMoney(getMoney() - huiQuan.getMoney());
//				if (getMoney() < 0) {
//					setMoney(0);
//				}
//			}
//		} else {
//			if (huiQuan.getType() == 3) {
//				setMoney(getMoney() - huiQuan.getMoney());
//				if (getMoney() < 0) {
//					setMoney(0);
//				}
//			} else {
//				setMoney(0);
//			}
//		}
//		upDateView(ticket);
//	}
//
//	/**
//	 * @param code
//	 */
//	public void setYouHuiMa(String code, int index) {
//		youhuimaIndex = index;
//		setYouHuiMa(code);
//	}
//
//	public void setYouHuiMa(String code) {
//		LogUtils.tiaoshi("code", code);
//		youhuiCoce = code;
//		tv_youhuiquan.setText("优惠券 :" + code);
//		checkMoney(dayIndex, youhuimaIndex);
//	}
//
//	public int getMoney() {
//		return money;
//	}
//
//	public void setMoney(int money) {
//		this.money = money;
//	}
//}
