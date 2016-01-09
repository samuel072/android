package com.yike.fragment;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.yike.R;
import com.yike.TopicActivity;
import com.yike.TopicDetailActivity;
import com.yike.action.ServiceAction;
import com.yike.adapter.TopicAdapter;
import com.yike.bean.TopicBean;
import com.yike.service.TopicService;
import com.yike.tool.SendActtionTool;
import com.yike.utils.Contansts;
import com.yike.utils.Utils;
import com.yike.view.NoScroListView;

/**
 * @author QIFA
 *         专题 界面
 */
public class TopicFragment extends BaseFragment implements OnItemClickListener {
    private NoScroListView mListView;
    private PullToRefreshScrollView mPullRefreshScrollView;
    private TopicAdapter adapter;

    @Override
    public void onClick(View v) {

    }

    @Override
    public View setContentView(LayoutInflater inflater) {
        mPullRefreshScrollView = (PullToRefreshScrollView) inflater.inflate(
                R.layout.fragment_topic, null);
        return mPullRefreshScrollView;
    }

    @Override
    public void initView(View rootView) {

        // 上拉、下拉设定
        mPullRefreshScrollView.setMode(Mode.PULL_FROM_START);
        // 上拉监听函数
        mPullRefreshScrollView
                .setOnRefreshListener(new OnRefreshListener<ScrollView>() {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ScrollView> refreshView) {
                        getUrlData();
                        // 执行刷新函数
                    }
                });
        mListView = (NoScroListView) rootView.findViewById(R.id.listview_topic);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                TopicBean bean = TopicService.getInstance().getDatas()
                        .get(position);
                Intent it = new Intent(getActivity(), TopicDetailActivity.class);
                it.putExtra("url", bean.getUrl());
                it.putExtra("title", bean.getName());
                startActivity(it);
            }
        });
        // 检测缓存
        if (!TopicService.getInstance().isHaveDate()) {
            getUrlData();
            // 填充数据
        } else {
            updateData();
        }

    }

    private void updateData() {

        List<TopicBean> datas = TopicService.getInstance().getDatas();
        if (datas == null) {
            return;
        }
        if (adapter == null) {
            adapter = new TopicAdapter(datas, getActivity().getLayoutInflater());
            mListView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

    }

    private void getUrlData() {
        SendActtionTool.get(Contansts.ContentParams.URL_TOPIC, null, null, this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Intent intent = new Intent(getActivity(), TopicActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSuccess(ServiceAction service, Object action, Object value) {
        JSONObject obj = (JSONObject) value;

        try {
            String arr = obj.getString(Contansts.DATA);
            List<TopicBean> datas = JSON.parseArray(arr, TopicBean.class);
            TopicService.getInstance().setDatas(datas);
            updateData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
