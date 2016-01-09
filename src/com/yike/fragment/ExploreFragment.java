package com.yike.fragment;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.yike.CommonWebViewActivity;
import com.yike.R;
import com.yike.action.ExploreAction;
import com.yike.action.ServiceAction;
import com.yike.adapter.ExploreListenMeAdapter;
import com.yike.adapter.ExploreTypeAdapter;
import com.yike.bean.AdBannerData;
import com.yike.bean.ExploreCategoryInfo;
import com.yike.bean.ExploreInfo;
import com.yike.bean.ExploreInfoBean;
import com.yike.bean.ListenMeInfo;
import com.yike.bean.TalkerBean;
import com.yike.service.ExploreService;
import com.yike.service.FirstPageService;
import com.yike.tool.BitmapTool;
import com.yike.tool.SendActtionTool;
import com.yike.utils.Contansts;
import com.yike.utils.LogUtils;
import com.yike.utils.Utils;
import com.yike.view.MyGridView;
import com.yike.view.RoundAngleImageView;

public class ExploreFragment extends BaseFragment implements
        OnPageChangeListener, OnClickListener {
    private ViewPager viewPager;
    /*
     * 装点点的ImageView数组
     */
    private ImageView[] tips;
    /*
     * 装ImageView数组
     */
    private ImageView[] mImageViews;

    private boolean isScrolled;
    private int currentTag;
    private Handler handler = new Handler();
    private ViewGroup group;
    private Timer timer;
    private PullToRefreshScrollView mPullRefreshScrollView;

    private MyGridView mLiveSeeGrid;
    private ExploreTypeAdapter mLiveSeeAdapter;

    private MyGridView mListenMeGridView;
    private ExploreListenMeAdapter ListenMeAdapter;

    private MyGridView qiangDiaoGridView;
    private ExploreTypeAdapter mQiangDiaoListAdapter;

    private MyGridView mChuangyeGridView;
    private ExploreTypeAdapter chuangyeAdapter;

    private MyGridView laiyikeGridView;
    private ExploreTypeAdapter mLaiyikeListAdapter;

    // 点我来讲
    private RoundAngleImageView mImageView;
    private TextView mImageViewTitle;

    private TextView mListenmeMoreTv;
    private TextView mQiangDiaoMoreTv;
    private TextView mChuangyeMoreTv;
    private TextView mLaiyikeMoreTv;
    private TextView mLiveSeeMoreTv;

    private View mListenmeLayout;
    private View mQiangDiaoLayout;
    private View mChuangyeLayout;
    private View mLaiyikeLayout;
    private View mLiveSeeLayout;

    // View pager的点击事件
    OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            List<AdBannerData> datas = FirstPageService.getInstance().getAdBannerdatas();
            AdBannerData banner = datas.get(viewPager.getCurrentItem());
            Utils.playVideo(mActivity, banner.getUrl());
        }
    };

    @Override
    public View setContentView(LayoutInflater inflater) {
        mPullRefreshScrollView = (PullToRefreshScrollView) inflater.inflate(
                R.layout.fragment_explore, null);
        return mPullRefreshScrollView;
    }

    @Override
    public void initView(View rootView) {
        mListenmeLayout = mPullRefreshScrollView
                .findViewById(R.id.listenme_Layout);
        mQiangDiaoLayout = mPullRefreshScrollView
                .findViewById(R.id.qiangdiao_layout);
        mChuangyeLayout = mPullRefreshScrollView
                .findViewById(R.id.chuangye_layout);
        mLaiyikeLayout = mPullRefreshScrollView
                .findViewById(R.id.laiyike_layout);
        mLiveSeeLayout = mPullRefreshScrollView
                .findViewById(R.id.livesee_Layout);

        mImageView = (RoundAngleImageView) mPullRefreshScrollView
                .findViewById(R.id.img_grid);
        mImageViewTitle = (TextView) mPullRefreshScrollView
                .findViewById(R.id.img_grid_title);

        mListenmeMoreTv = (TextView) mPullRefreshScrollView
                .findViewById(R.id.listenme_more);
        mListenmeMoreTv.setOnClickListener(this);

        mQiangDiaoMoreTv = (TextView) mPullRefreshScrollView
                .findViewById(R.id.qiangdiao_more);
        mQiangDiaoMoreTv.setOnClickListener(this);

        mChuangyeMoreTv = (TextView) mPullRefreshScrollView
                .findViewById(R.id.chuangye_more);
        mChuangyeMoreTv.setOnClickListener(this);

        mLaiyikeMoreTv = (TextView) mPullRefreshScrollView
                .findViewById(R.id.laiyike_more);
        mLaiyikeMoreTv.setOnClickListener(this);

        mLiveSeeMoreTv = (TextView) mPullRefreshScrollView
                .findViewById(R.id.livesee_more);
        mLiveSeeMoreTv.setOnClickListener(this);

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
        // 检测缓存
        if (!ExploreService.getInstance().isHaveDate()) {
            getUrlData();
            // 填充数据
        } else {
            updateData();
        }
    }

    private void updateData() {
        initViewPage(mPullRefreshScrollView);
        initTalk();
        initLiveSee();
        //initListenMe();
        initQiangDiao();
        initChuangye();
        initLaiyike();
    }

    private void initTalk() {
        TalkerBean data = ExploreService.getInstance().getTaklData();
        String name = data.getName();
        int num = data.getPoints();
        mImageViewTitle.setText(Html.fromHtml(this.getString(R.string.dian_ta, name, num)));
        String imgUrl = data.getImage();
        if (!TextUtils.isEmpty(imgUrl)) {
            BitmapTool.getInstance().initAdapterUitl(getActivity())
                    .display(mImageView, data.getImage());
        }
        mImageView.setOnClickListener(this);
    }

    /**
     * 今日现场直击初始化
     */
    private void initLiveSee() {
        if (mLiveSeeGrid == null) {
            mLiveSeeGrid = (MyGridView) mPullRefreshScrollView
                    .findViewById(R.id.livesee_gridView);
            mLiveSeeGrid.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {
                    ExploreCategoryInfo info = (ExploreCategoryInfo) mLiveSeeAdapter.getItem(position);
                    Utils.playVideo(mActivity, info.getVideoDetailUrl());
                }
            });
        }
        ExploreInfoBean data = ExploreService.getInstance().getLiveSeeData();
        if (data == null) {
            mPullRefreshScrollView
                    .findViewById(R.id.qiangdiao_layout).setVisibility(View.GONE);
            return;
        }

        List<ExploreCategoryInfo> datas = data.getInfo();
        if (data != null && datas != null && datas.size() > 0) {
            mLiveSeeLayout.setVisibility(View.VISIBLE);
        } else {
            mLiveSeeLayout.setVisibility(View.GONE);
            return;
        }
        mLiveSeeAdapter = new ExploreTypeAdapter(datas, getActivity());
        mLiveSeeGrid.setAdapter(mLiveSeeAdapter);

    }

    /**
     * 腔调初始化
     */
    private void initQiangDiao() {
        if (qiangDiaoGridView == null) {
            qiangDiaoGridView = (MyGridView) mPullRefreshScrollView
                    .findViewById(R.id.qiangdiao_gridView);
            qiangDiaoGridView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {
                    ExploreCategoryInfo info = (ExploreCategoryInfo) mQiangDiaoListAdapter.getItem(position);
                    Utils.playVideo(mActivity, info.getVideoDetailUrl());
                }
            });
        }
        ExploreInfoBean data = ExploreService.getInstance().getQiangdiaoData();
        if (data == null) {
            mPullRefreshScrollView
                    .findViewById(R.id.qiangdiao_layout).setVisibility(View.GONE);
            return;
        }

        List<ExploreCategoryInfo> datas = data.getInfo();
        if (data != null && datas != null && datas.size() > 0) {
            mQiangDiaoLayout.setVisibility(View.VISIBLE);
        } else {
            mQiangDiaoLayout.setVisibility(View.GONE);
            return;
        }
        mQiangDiaoListAdapter = new ExploreTypeAdapter(datas, getActivity());
        qiangDiaoGridView.setAdapter(mQiangDiaoListAdapter);

    }

    /**
     * 创业初始化
     */
    private void initChuangye() {
        if (mChuangyeGridView == null) {
            mChuangyeGridView = (MyGridView) mPullRefreshScrollView
                    .findViewById(R.id.chuangye_gridView);
            mChuangyeGridView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {
                    ExploreCategoryInfo info = (ExploreCategoryInfo) chuangyeAdapter.getItem(position);
                    Utils.playVideo(mActivity, info.getVideoDetailUrl());
                }
            });
        }
        ExploreInfoBean data = ExploreService.getInstance().getChuangyeData();
        if (data == null) {
            mPullRefreshScrollView
                    .findViewById(R.id.chuangye_layout).setVisibility(View.GONE);
            return;
        }

        List<ExploreCategoryInfo> datas = data.getInfo();
        if (data != null && datas != null && datas.size() > 0) {
            mChuangyeLayout.setVisibility(View.VISIBLE);
        } else {
            mChuangyeLayout.setVisibility(View.GONE);
            return;
        }
        chuangyeAdapter = new ExploreTypeAdapter(datas, getActivity());
        mChuangyeGridView.setAdapter(chuangyeAdapter);

    }

    /**
     * 来一课初始化
     */
    private void initLaiyike() {
        if (laiyikeGridView == null) {
            laiyikeGridView = (MyGridView) mPullRefreshScrollView
                    .findViewById(R.id.laiyike_gridView);
            laiyikeGridView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {
                    ExploreCategoryInfo info = (ExploreCategoryInfo) mLaiyikeListAdapter.getItem(position);
                    Utils.playVideo(mActivity, info.getVideoDetailUrl());
                }
            });
        }
        ExploreInfoBean data = ExploreService.getInstance().getLaiyikeData();
        if (data == null) {
            mPullRefreshScrollView
                    .findViewById(R.id.laiyike_layout).setVisibility(View.GONE);
            return;
        }

        List<ExploreCategoryInfo> datas = data.getInfo();
        if (data != null && datas != null && datas.size() > 0) {
            mLaiyikeLayout.setVisibility(View.VISIBLE);
        } else {
            mLaiyikeLayout.setVisibility(View.GONE);
            return;
        }
        mLaiyikeListAdapter = new ExploreTypeAdapter(datas, getActivity());
        laiyikeGridView.setAdapter(mLaiyikeListAdapter);

    }

    /**
     * 听我说初始化
     */
    private void initListenMe() {
        if (mListenMeGridView == null) {
            mListenMeGridView = (MyGridView) mPullRefreshScrollView
                    .findViewById(R.id.listenme_gridView);
            mListenMeGridView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {
                    ListenMeInfo info = (ListenMeInfo)ListenMeAdapter.getItem(position);
                    Utils.playVideo(mActivity, info.getDetail_url());
                }
            });

        }

        List<ListenMeInfo> data = ExploreService.getInstance().getListenMeData();
        if (data == null) {
            mListenMeGridView.setVisibility(View.GONE);
            return;
        }

        if (data != null && data != null && data.size() > 0) {
            mListenmeLayout.setVisibility(View.VISIBLE);
        } else {
            mListenmeLayout.setVisibility(View.GONE);
            return;
        }
        ListenMeAdapter= new ExploreListenMeAdapter(data, getActivity());
        mListenMeGridView.setAdapter(ListenMeAdapter);
    }

    @Override
    public void onClick(View v) {
        Intent it = new Intent(this.getActivity(), CommonWebViewActivity.class);
        ExploreInfoBean data;
        switch (v.getId()) {

            case R.id.listenme_more:
                ListenMeInfo info = ExploreService.getInstance().getListenMeData().get(0);
                it.putExtra("url", info.getMore_url());
                startActivity(it);
                break;
            case R.id.qiangdiao_more:
                data = ExploreService.getInstance().getQiangdiaoData();
                it.putExtra("url", data.getListUrl());
                startActivity(it);
                break;
            case R.id.chuangye_more:
                data = ExploreService.getInstance().getChuangyeData();
                it.putExtra("url", data.getListUrl());
                startActivity(it);
                break;
            case R.id.laiyike_more:
                data = ExploreService.getInstance().getLaiyikeData();
                it.putExtra("url", data.getListUrl());
                startActivity(it);
                break;
            case R.id.img_grid:
                it.putExtra("url", ExploreService.getInstance().getTaklData().getTalkerListUrl());
                LogUtils.d("qifa", "-----------"
                        + ExploreService.getInstance().getTaklData().getTalkerListUrl());
                startActivity(it);
                break;
            case R.id.livesee_more:
                data = ExploreService.getInstance().getLiveSeeData();
                it.putExtra("url", data.getListUrl());
                startActivity(it);
                break;
            default:
                break;
        }
    }

    private void getUrlData() {
        SendActtionTool.get(Contansts.ContentParams.URL_BASE_YIKE_EXPLORE, ServiceAction.Action_Explore,
                ExploreAction.Action_List, this);
        SendActtionTool.get("http://www.yikelive.com/m/api/yk_app/talk.php", ServiceAction.Action_Explore_ListenMe,
                ExploreAction.Action_List, this);
    }

    /**
     * @param rootView
     *            初始化巡展
     */
    private void initViewPage(View rootView) {
        if (group == null) {
            group = (ViewGroup) rootView.findViewById(R.id.viewGroup);
            viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        }
        List<AdBannerData> datas = FirstPageService.getInstance().getAdBannerdatas();
        if (datas == null || datas.size() == 0) {
            group.setVisibility(View.GONE);
            return;
        } else {
            group.setVisibility(View.VISIBLE);
        }
        // 将点点加入到ViewGroup中
        tips = new ImageView[datas.size()];
        group.removeAllViews();
        // 将图片装载到数组中
        for (int i = 0; i < datas.size(); i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setLayoutParams(new LayoutParams(10, 10));
            tips[i] = imageView;
            if (i == 0) {
                tips[i].setBackgroundResource(R.drawable.dot_lunbo_dian_hl);
            } else {
                tips[i].setBackgroundResource(R.drawable.dot_lunbo_dian);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));

            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            group.addView(imageView, layoutParams);
        }
        if (tips.length == 1) {
            group.setVisibility(View.GONE);
        }
        // 将图片装载到数组中
        mImageViews = new ImageView[datas.size()];
        for (int i = 0; i < mImageViews.length; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setScaleType(ScaleType.FIT_XY);
            imageView.setOnClickListener(listener);
            mImageViews[i] = imageView;
            BitmapTool.getInstance().initAdapterUitl(getActivity())
                    .display(imageView, datas.get(i).getBigImage());
        }

        // 设置Adapter
        viewPager.setAdapter(new MyPagerAdapter());
        // 设置监听，主要是设置点点的背景
        viewPager.setOnPageChangeListener(this);
        // 设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
        viewPager.setCurrentItem(0);
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            if (currentTag == mImageViews.length) {
                                currentTag = 0;
                            } else {
                                currentTag += 1;
                            }
                            viewPager.setCurrentItem(currentTag);
                        }
                    });

                }
            }, 2000, 2000);

        }
    }

    // viewpager适配器
    class MyPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // return super.instantiateItem(container, position);

            container.addView(mImageViews[position], 0);
            return mImageViews[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImageViews[position]);
        }

        @Override
        public int getCount() {
            return mImageViews.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        switch (arg0) {
        // 手势滑动
            case 1:
                isScrolled = false;
                break;
            // 界面切换
            case 2:
                isScrolled = true;
                break;
            // 滑动结束
            case 0:
                // 当前为最后一张，此时从右向左滑，则切换到第一张
                if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1
                        && !isScrolled) {
                    viewPager.setCurrentItem(0, false);
                }
                // 当前为第一张，此时从左向右滑，则切换到最后一张
                else if (viewPager.getCurrentItem() == 0 && !isScrolled) {
                    viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1,
                            false);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    private void updatePageDate(JSONObject object) {
        ExploreInfo info = JSON.parseObject(object.toString(), ExploreInfo.class);
        TalkerBean talkerBean = info.getTalker();
        talkerBean.setTalkerListUrl(info.getTalkerListUrl());
        ExploreService.getInstance().setTaklData(talkerBean);
        ExploreInfoBean data = null;
        List<ExploreInfoBean> datas = info.getInfo();
        for (int i = 0; i < datas.size(); i++) {
            data = datas.get(i);
            String name = data.getName();
            LogUtils.d(LogUtils.tag, "name------------->" + name);
            if (name.equals("腔调TALKS")) {
                ExploreService.getInstance().setQiangdiaoData(data);
            } else if (name.equals("创业路汇演")) {
                ExploreService.getInstance().setChuangyeData(data);
            } else if (name.equals("来一课")) {
                ExploreService.getInstance().setLaiyikeData(data);
            } else if (name.equals("今日现场直击")) {
                ExploreService.getInstance().setLiveSeeData(data);
            }
        }
        updateData();

    }

    @Override
    public void onPageSelected(int arg0) {
        for (int i = 0; i < tips.length; i++) {
            if (i == arg0 % mImageViews.length) {
                tips[i].setBackgroundResource(R.drawable.dot_lunbo_dian_hl);
            } else {
                tips[i].setBackgroundResource(R.drawable.dot_lunbo_dian);
            }
        }
    }

    @Override
    public void onSuccess(ServiceAction service, Object action, Object value) {
        super.onSuccess(service, action, value);
        LogUtils.d(LogUtils.tag, "------------onSuccess-------------");
        switch (service) {
        // 当前页面的数据
            case Action_Explore:
                mPullRefreshScrollView.onRefreshComplete();
                updatePageDate((JSONObject) value);
                LogUtils.d(LogUtils.tag, "------------Action_Explore-------------");
                break;
            case Action_Explore_ListenMe:
                JSONObject result = (JSONObject) value;
                try {
                    JSONArray jsonArray = result.getJSONArray("data");
                    List<ListenMeInfo> infos = JSON.parseArray(jsonArray.toString(), ListenMeInfo.class);
                    ExploreService.getInstance().setListenMeData(infos);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                initListenMe();
            default:
                break;
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
