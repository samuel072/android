package com.yike.fragment;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.yike.R;
import com.yike.action.RankAction;
import com.yike.action.ServiceAction;
import com.yike.adapter.Rank2ListAdapter;
import com.yike.bean.RankBean;
import com.yike.service.RankPageService;
import com.yike.tool.SendActtionTool;
import com.yike.tool.UrlTool;
import com.yike.utils.Contansts;
import com.yike.utils.LogUtils;
import com.yike.utils.Utils;
import com.yike.view.NoScroListView;

/**
 * @author rendy 排行界面
 */
public class RankingFragment extends BaseFragment {

    private NoScroListView mListView;
    private Rank2ListAdapter mListAdapter;
    private PullToRefreshScrollView mPullRefreshScrollView;
    private RadioGroup radioGroup;
    private int mCurrentTab = 0;

    @Override
    public void onClick(View v) {

    }

    @Override
    public View setContentView(LayoutInflater inflater) {
        mPullRefreshScrollView = (PullToRefreshScrollView) inflater.inflate(
                R.layout.fragment_ranking, null);
        return mPullRefreshScrollView;
    }

    @Override
    public void initView(View rootView) {

        mListView = (NoScroListView) rootView.findViewById(R.id.listview);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                RankBean bean = (RankBean) mListAdapter.getItem(position);
                Utils.playVideo(mActivity, bean.getVideoDetailUrl());
// Utils.playVideo(getActivity(),
// String.valueOf(bean.getVideoId()), bean.getName(),
// String.valueOf(bean.getChannelId()));
            }
        });
        radioGroup = (RadioGroup) rootView.findViewById(R.id.rd_group);

        mPullRefreshScrollView.setMode(Mode.PULL_FROM_START);
        // 上拉监听函数
        mPullRefreshScrollView
                .setOnRefreshListener(new OnRefreshListener<ScrollView>() {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ScrollView> refreshView) {
                        LogUtils.d("test", "mCurrentTab: " + mCurrentTab);
                        getUrlData(mCurrentTab);
                        // 执行刷新函数
                    }

                });
        // 检测缓存
        if (!RankPageService.getInstance().isHaveDate()) {
            getUrlData(0);
            // 填充数据
        } else {
            updateTodayData();
        }

        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LogUtils.d("test", "checkedId:" + checkedId);
                updateData(checkedId);

            }
        });
    }

    private void getUrlData() {
        SendActtionTool.get(Contansts.ContentParams.URL_RANK, null, RankAction.Action_today, this,
                UrlTool.getParams("type", "1"));
        SendActtionTool.get(Contansts.ContentParams.URL_RANK, null, RankAction.Action_month, this,
                UrlTool.getParams("type", "2"));
    }

    private void getUrlData(int type) {
        if (mCurrentTab == 0) {
            SendActtionTool.get(Contansts.ContentParams.URL_RANK, null, RankAction.Action_today, this,
                    UrlTool.getParams("type", "1"));
        } else {
            SendActtionTool.get(Contansts.ContentParams.URL_RANK, null, RankAction.Action_month, this,
                    UrlTool.getParams("type", "2"));
        }
    }

    private void updateData(int id) {

        switch (id) {
            case R.id.today:
                mCurrentTab = 0;
                updateTodayData();
                break;
            case R.id.toMonth:
                mCurrentTab = 1;
                updateMonthData();
                break;
            default:
                break;
        }
    }

    private void updateTodayData() {
        List<RankBean> datas = RankPageService.getInstance().getToday();
        if (mListAdapter != null) {
            LogUtils.d("test", "1updateTodayData data:" + datas);
            mListAdapter.setDatas(datas);
            mListAdapter.notifyDataSetChanged();
        } else {
            if (datas != null) {
                mListAdapter = new Rank2ListAdapter(datas, getActivity()
                        .getLayoutInflater());
                mListView.setAdapter(mListAdapter);
                LogUtils.d("test", "2updateTodayData data:" + datas);

            }
        }
        // Utils.setListViewHeightBasedOnChildren(mListView);
    }

    private void updateMonthData() {
        List<RankBean> datas = RankPageService.getInstance().getMonth();
        if (datas != null) {
            if (mListAdapter != null) {
                LogUtils.d("test", "1updateMonthData data:" + datas);
                mListAdapter.setDatas(datas);
                mListAdapter.notifyDataSetChanged();
            } else {
                mListAdapter = new Rank2ListAdapter(datas, getActivity()
                        .getLayoutInflater());
                mListView.setAdapter(mListAdapter);
                LogUtils.d("test", "2updateMonthData data:" + datas);
            }
        } else {
            getUrlData(1);
        }
        // Utils.setListViewHeightBasedOnChildren(mListView);
    }

    @Override
    public void onSuccess(ServiceAction service, Object action, Object value) {
        switch ((RankAction) action) {
            case Action_month:
                RankPageService.getInstance().setMonth(
                        analyData((JSONObject) value));
                updateMonthData();
                break;
            case Action_today:
                RankPageService.getInstance().setToday(
                        analyData((JSONObject) value));
                updateTodayData();
                break;
            default:
                break;
        }
    }

    /**
     * @param obj
     * @return 解析数据
     */
    private List<RankBean> analyData(JSONObject obj) {
        List<RankBean> datas = null;
        try {
            String arr = obj.getString(Contansts.DATA);
            datas = JSON.parseArray(arr, RankBean.class);
            return datas;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return datas;
    }

    @Override
    public void onFaile(ServiceAction service, Object action, Object value) {
        super.onFaile(service, action, value);
    }

    @Override
    public void onFinish(ServiceAction service, Object action) {
        super.onFinish(service, action);
        mPullRefreshScrollView.onRefreshComplete();
    }

}
