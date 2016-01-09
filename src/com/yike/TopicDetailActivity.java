package com.yike;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.yike.action.ServiceAction;
import com.yike.adapter.TopicDetailAdapter;
import com.yike.bean.AddFavResult;
import com.yike.bean.CheckFavStatus;
import com.yike.bean.MarkLikeResult;
import com.yike.bean.TopicDetailBean;
import com.yike.bean.UserBean;
import com.yike.listener.CommonListener;
import com.yike.service.TopicDetailService;
import com.yike.service.UserService;
import com.yike.tool.BitmapTool;
import com.yike.tool.SendActtionTool;
import com.yike.utils.Contansts;
import com.yike.utils.FavoritesUtils;
import com.yike.utils.MarkLikeUtils;
import com.yike.utils.ShareUtils;
import com.yike.utils.Utils;
import com.yike.view.NoScroListView;

public class TopicDetailActivity extends BaseActivity implements CommonListener {

    private ImageView mHeadView;
    private TextView mTitleTv;
    private TextView mDirectoryTv;
    private TextView mFirstTextView;
    private NoScroListView mListView;
    private TopicDetailAdapter adapter;

    private Intent mIntent;
    private String mUrl;
    private String mTitleString;
    private ImageButton mMarkLike;
    private ImageButton mAddFav;
    private ImageButton mShare;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_topic_detail);
        mIntent = getIntent();
        mUrl = mIntent.getStringExtra("url");
        mTitleString = mIntent.getStringExtra("title");
        getUrlData();
        registerReceiver(mFavDataChangedReceiver, new IntentFilter(
                Contansts.FAV_DATA_CHANGED_RECEIVER));
    }

    @Override
    public void findViewById() {
        findViewById(R.id.btn_return).setOnClickListener(this);
        findViewById(R.id.img_grid).setOnClickListener(this);
        mMarkLike = (ImageButton) findViewById(R.id.mark_like);
        mMarkLike.setOnClickListener(this);
        mAddFav = (ImageButton) findViewById(R.id.mark_fav);
        mAddFav.setOnClickListener(this);
        mShare = (ImageButton) findViewById(R.id.share);
        mShare.setOnClickListener(this);

        mTitleTv = (TextView) findViewById(R.id.tv_title);
        mTitleTv.setText(mTitleString);

        mFirstTextView = (TextView) findViewById(R.id.tv_first_name);

        mListView = (NoScroListView) findViewById(R.id.listview);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                TopicDetailBean bean = TopicDetailService.getInstance().getDatas()
                        .get(position);
                Utils.playVideo(TopicDetailActivity.this, bean.getVideoDetailUrl());
            }
        });
    }

    private void updateData() {

        List<TopicDetailBean> datas = TopicDetailService.getInstance().getDatas();
        if (datas == null) {
            return;
        }
        if (adapter == null) {
            adapter = new TopicDetailAdapter(datas, getLayoutInflater(),this);
            mListView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        // 初始化第一个
        mHeadView = (ImageView) findViewById(R.id.img_grid);
        BitmapTool.getInstance().initAdapterUitl(this)
                .display(mHeadView, getCurrentShowVideo().getStandardPic());
        mFirstTextView.setText(getCurrentShowVideo().getName());
        
        mDirectoryTv = (TextView) findViewById(R.id.tv_directory);
        if (datas.size() > 1) {
            mDirectoryTv.setText(getString(R.string.topic_detail_directory,datas.size()));
            mDirectoryTv.setVisibility(View.VISIBLE);
        } else {
            mDirectoryTv.setVisibility(View.GONE);
        }
       

        // check 是否点赞过, 是否收藏过
        checkMarkLikeAndFavStatus();
    }

    /**
     * 获取网络数据
     */
    private void getUrlData() {
        SendActtionTool.get(mUrl, ServiceAction.Action_Toptic_Detail, null, this);
    }

    @Override
    public void onSuccess(ServiceAction service, Object action, Object value) {
        JSONObject obj = (JSONObject) value;
        switch (service) {
        // 当前页面的数据
            case Action_Toptic_Detail:
                try {
                    JSONObject object = obj.getJSONObject(Contansts.DATA);
                    String arr2 = object.getString(Contansts.DATA);
                    List<TopicDetailBean> datas = JSON.parseArray(arr2, TopicDetailBean.class);
                    TopicDetailService.getInstance().setDatas(datas);
                    updateData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Action_Add_Fav:
                parseAddFavResult(obj);
                break;
            case Action_Mark_Like:
                parseMarkResult(obj);
                break;
            case Action_Check_Fav:
                parseFavStatus(obj);
                break;
            default:
                break;
        }

    }

    private TopicDetailBean getCurrentShowVideo() {
        List<TopicDetailBean> data = TopicDetailService.getInstance().getDatas();
        TopicDetailBean info = null;
        if (data != null && data.size() > 0) {
            info = data.get(0);
            mHeadView.setTag(info);
        }
        return info;
    }

    private BroadcastReceiver mFavDataChangedReceiver = new BroadcastReceiver() {
        public void onReceive(android.content.Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(Contansts.FAV_DATA_CHANGED_RECEIVER)) {
                checkMarkLikeAndFavStatus();
            }
        }
    };

    private void refreshFavStatus(boolean added) {
        if (mAddFav != null) {
            mAddFav.setBackgroundResource(added ? R.drawable.collect_click : R.drawable.collect);
        }
    }

    private void checkMarkLikeAndFavStatus() {
        UserBean bean = UserService.getInatance().getUserBean();
        TopicDetailBean info = getCurrentShowVideo();
        if (bean != null && info != null) {
            FavoritesUtils.queryHasAdded(bean.getId(), String.valueOf(info.getVideoId()),
                    ServiceAction.Action_Check_Fav,
                    this);
        }
    }

    private void parseFavStatus(JSONObject obj) {
        try {
            JSONObject jsonObject = obj.getJSONObject(Contansts.DATA);
            // TODO:暂时未使用
            CheckFavStatus status = JSON.parseObject(jsonObject.toString(), CheckFavStatus.class);
            if (status != null) {
                refreshFavStatus(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseMarkResult(JSONObject obj) {
        try {
            JSONObject jsonObject = obj.getJSONObject(Contansts.DATA);
            // TODO:暂时未使用
            MarkLikeResult addFavResult = JSON.parseObject(jsonObject.toString(), MarkLikeResult.class);
            Toast.makeText(this, "点赞成功 +1", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseAddFavResult(JSONObject object) {
        try {
            JSONObject jsonObject = object.getJSONObject(Contansts.DATA);
            // TODO:暂时未使用
            AddFavResult addFavResult = JSON.parseObject(jsonObject.toString(), AddFavResult.class);
            Toast.makeText(this, "添加收藏成功", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFaile(ServiceAction service, Object action, Object value) {

        if (service == ServiceAction.Action_Add_Fav) {
            Toast.makeText(this, "添加收藏失败,请重试!", Toast.LENGTH_SHORT).show();
            mAddFav.setBackgroundResource(R.drawable.collect);
        } else if (service == ServiceAction.Action_Check_Fav) {
            refreshFavStatus(false);
        } else {
            Utils.toast(getApplicationContext(), value.toString());
        }
    }

    @Override
    public void onFinish(ServiceAction service, Object action) {
        dismissDialog();
    }

    private void markLike() {
        TopicDetailBean info = getCurrentShowVideo();
        UserBean bean = UserService.getInatance().getUserBean();
        if (bean != null && info != null) {
            MarkLikeUtils.markLike(bean.getId(), String.valueOf(info.getVideoId()),
                    ServiceAction.Action_Mark_Like, this);
            mMarkLike.setBackgroundResource(R.drawable.like_one_click);
        } else {
            if (bean == null) {
                Toast.makeText(this, "请先登陆!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "点赞失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addToFav() {
        TopicDetailBean info = getCurrentShowVideo();
        UserBean bean = UserService.getInatance().getUserBean();
        if (bean != null && info != null) {
            FavoritesUtils.addToFav(bean.getId(), String.valueOf(info.getVideoId()),
                    ServiceAction.Action_Add_Fav, this);
            Toast.makeText(this, "正在添加到收藏夹", Toast.LENGTH_SHORT).show();
            mAddFav.setBackgroundResource(R.drawable.collect_click);
        } else {
            if (bean == null) {
                Toast.makeText(this, "请先登陆!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "添加收藏失败,请重试!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_return:
                finish();
                break;
            case R.id.img_grid:
                try {
                    TopicDetailBean info = (TopicDetailBean) v.getTag();
                    Utils.playVideo(this, info.getVideoDetailUrl());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.mark_like:
                markLike();
                break;
            case R.id.mark_fav:
                addToFav();
                break;
            case R.id.share:
                TopicDetailBean info = getCurrentShowVideo();
                ShareUtils.share(this, info.getName(), "", info.getVideoDetailUrl());
                break;
        }

    }

}
