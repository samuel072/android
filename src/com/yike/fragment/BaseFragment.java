package com.yike.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.yike.action.ServiceAction;
import com.yike.listener.CommonListener;
import com.yike.tool.DialogTool;
import com.yike.utils.LogUtils;
import com.yike.view.LodingDialog;

/**
 * 
 * @author zxm
 * 
 *         fragment 界面 base
 */
public abstract class BaseFragment extends Fragment implements CommonListener,
		OnClickListener {
	private LodingDialog lodingDialog;
	protected Activity mActivity;

	/**
	 * 展示加加载信心等待 dialog
	 */
	public void showLoginDialog() {
		if (lodingDialog == null) {
			lodingDialog = DialogTool.createLoadingDialog(getActivity());
		}
		if (lodingDialog.isShowing()) {
			return;
		} else {
			lodingDialog.show();
		}
	}

	/**
	 * 隐藏加载信息 dialog
	 */
	public void dismissDialog() {
		if (lodingDialog == null) {
			return;
		}
		if (lodingDialog.isShowing()) {
			lodingDialog.dismiss();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = setContentView(inflater);
		initView(rootView);
		return rootView;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		dismissDialog();
	}

	public abstract View setContentView(LayoutInflater inflater);

	public abstract void initView(View rootView);

	@Override
	public void onSuccess(ServiceAction service, Object action, Object value) {

	}

	@Override
	public void onFaile(ServiceAction service, Object action, Object value) {
		LogUtils.tiaoshi("onFaile:", value != null ? value.toString()
				: "value==null");
	}

	@Override
	public void onException(ServiceAction service, Object action, Object value) {
		LogUtils.tiaoshi("onException:", value != null ? value.toString()
				: "value==null");
	}

	@Override
	public void onStart(ServiceAction service, Object action) {
	}

	@Override
	public void onFinish(ServiceAction service, Object action) {
	}

}
