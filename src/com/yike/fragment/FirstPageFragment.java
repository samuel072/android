package com.yike.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.http.RequestParams;
import com.yike.LoginActivity;
import com.yike.MainActivity;
import com.yike.R;
import com.yike.action.ServiceAction;
import com.yike.action.UserAction;
import com.yike.adapter.HomePageInfoAdapter;
import com.yike.adapter.MediaTypeAdapter;
import com.yike.adapter.MoreListAdapter;
import com.yike.adapter.PageYuyueAdapter;
import com.yike.bean.*;
import com.yike.manager.BaseApplication;
import com.yike.service.FirstPageService;
import com.yike.service.UserService;
import com.yike.tool.BitmapTool;
import com.yike.tool.DialogTool;
import com.yike.tool.SendActtionTool;
import com.yike.tool.UrlTool;
import com.yike.utils.Contansts;
import com.yike.utils.Contansts.UserParams;
import com.yike.utils.LogUtils;
import com.yike.utils.Utils;
import com.yike.view.MyGridView;
import com.yike.view.NoScroListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author rendy
 *         首页界面
 */
public class FirstPageFragment extends BaseFragment implements
        OnPageChangeListener {
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

    private boolean isStop = false; // 是否停止子线程, 不会停止

    private NoScroListView mListView;
    private PageYuyueAdapter listAdapter;
    private MyGridView mGridView;
    private MediaTypeAdapter gridAdapter;
    private ListView moreListView;
    private MoreListAdapter moreListAdapter;
    private HomePageInfoAdapter mHomePageAdapter;

    private Handler handler = new Handler();
    private ViewGroup group;
    private Timer timer;
    private PullToRefreshScrollView mPullRefreshScrollView;

    @Override
    public View setContentView(LayoutInflater inflater) {
        mPullRefreshScrollView = (PullToRefreshScrollView) inflater.inflate(
                R.layout.fragment_firstpage, null);
        return mPullRefreshScrollView;
    }

    @Override
    public void initView(View rootView) {
        // TODO Auto-generated method stub
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
        // 检测缓存
        if (!FirstPageService.getInstance().isHaveDate()) {
            getUrlData();
            // 填充数据
        } else {
            updateData();
        }
        SendActtionTool.get(UserParams.URL_Check_VERSION,
                ServiceAction.Action_User, UserAction.Action_CHECK_VERSION,
                this);
    }

    // View pager的点击事件
    OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int positon = viewPager.getCurrentItem();
            List<AdBannerData> datas = FirstPageService.getInstance().getAdBannerdatas();
            if (datas != null && datas.size() > 0 && positon >= 0 && positon < datas.size()) {
                AdBannerData banner = datas.get(positon);
                // TODO:wangwei 数据返回格式中无视频信息 ？？？
                // Utils.playMedia(banner, getActivity());
                Utils.playVideo(mActivity, banner.getUrl());
            }
        }
    };

    /**
     * 填充数据
     */
    private void updateData() {
        // 初始化 巡展 Øß
        initAvdViewPage(mPullRefreshScrollView);
        initHomePage();

    }

    private void initHomePage() {
        if (mGridView == null) {
            mGridView = (MyGridView) mPullRefreshScrollView
                    .findViewById(R.id.gridView);
            mGridView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {
                    List<HomePageItemInfo> homePageData = FirstPageService.getInstance().getHomePageData();
                    if (homePageData != null) {
                        if (position >= 0 && position < homePageData.size()) {
                            HomePageItemInfo info = homePageData.get(position);
                            Utils.playVideo(mActivity, info.getVideoDetailUrl());
                        }
                    }

                }
            });

        }

        List<HomePageItemInfo> data = FirstPageService.getInstance().getHomePageData();
        if (data == null) {
            mGridView.setVisibility(View.GONE);
            return;
        }

        List<HomePageItemInfo> datas = data;
        if (data != null && datas != null && datas.size() > 0) {
            mGridView.setVisibility(View.VISIBLE);
        } else {
            mGridView.setVisibility(View.GONE);
            return;
        }
        mHomePageAdapter = new HomePageInfoAdapter(datas, getActivity());
        mGridView.setAdapter(mHomePageAdapter);
    }

    private Dialog padyDialog;

    /**
     * @param albumId
     */
    private void showPayDialog(String albumId) {
        if (padyDialog != null) {
            padyDialog.dismiss();
            padyDialog = null;
        }
        padyDialog = DialogTool.createPayDialog(getActivity(), albumId);
        padyDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        // 预约添加 取消按钮
            case R.id.button1:

                if (UserService.getInatance().isNeedLogin(getActivity())) {
                    getActivity().startActivity(
                            new Intent(getActivity(), LoginActivity.class));
                } else {
                    FocusPictureModel model = (FocusPictureModel) v.getTag();
                    UserBean bean = UserService.getInatance().getUserBean();
                    // 取消预约
                    if (model.getIsSubscribe() == 1) {
                        UserAction.Action_Yuyue_Cancle.value = model.getAlbumId();
                        SendActtionTool.get(UserParams.URL_YUYUE_DEL,
                                ServiceAction.Action_User,
                                UserAction.Action_Yuyue_Cancle, this, UrlTool
                                        .getParams(UserParams.USER_ID,
                                                bean.getId(), UserParams.ALBUM_ID,
                                                model.getAlbumId()));
                        // 添加预约
                    } else {
                        UserAction.Action_Yuyue_Add.value = model.getAlbumId();
                        SendActtionTool.get(UserParams.URL_YUYUE_ADD,
                                ServiceAction.Action_User,
                                UserAction.Action_Yuyue_Add, this, UrlTool
                                        .getParams(UserParams.USER_ID,
                                                bean.getId(), UserParams.ALBUM_ID,
                                                model.getAlbumId(),
                                                UserParams.AlbumName,
                                                model.getName()));

                    }
                    if (model.getCategory().equals("1")) {
                        showPayDialog(model.getAlbumId());
                    } else {
                        showLoginDialog();
                    }
                }
                break;

            default:
                break;
        }
    }

    /**
     * 底部加载 更多的数据
     */
    private void initMoreDatas() {
        if (moreListView == null) {
            moreListView = (ListView) mPullRefreshScrollView
                    .findViewById(R.id.listview_more);
        }
        Data data = FirstPageService.getInstance().getMoreDatas();
        if (data == null) {
            moreListView.setVisibility(8);
            return;
        }
        if (data != null && data.getData() != null && data.getData().size() > 0) {
            moreListView.setVisibility(0);
            moreListAdapter = new MoreListAdapter(data.getData(),
                    (MainActivity) getActivity());
            moreListView.setAdapter(moreListAdapter);
            // Utils.setListViewHeightBasedOnChildren(moreListView);
        } else {
            moreListView.setVisibility(8);
        }

    }

    /**
     * 独家热播初始化
     */
    private void initDujiaRebo() {
        if (mGridView == null) {
            mGridView = (MyGridView) mPullRefreshScrollView
                    .findViewById(R.id.gridView);
            mGridView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {
                    FocusPictureModel model = FirstPageService.getInstance()
                            .getDujiaData().getData().get(position);
                    Utils.playMedia(model, getActivity());
                }
            });

        }

        Data data = FirstPageService.getInstance().getDujiaData();
        if (data == null) {
            mGridView.setVisibility(8);
            return;
        }

        List<FocusPictureModel> datas = data.getData();
        if (data != null && datas != null && datas.size() > 0) {
            mGridView.setVisibility(0);
        } else {
            mGridView.setVisibility(8);
            return;
        }
        gridAdapter = new MediaTypeAdapter(datas, getActivity());
        mGridView.setAdapter(gridAdapter);
        // Utils.setGridViewHeightBasedOnChildren(mGridView, 2);
    }

    /**
     * 初始化 音乐节
     */
    private void initYinyueJie() {
        if (mListView == null) {
            mListView = (NoScroListView) mPullRefreshScrollView
                    .findViewById(R.id.listview);
        }
        Data data = FirstPageService.getInstance().getYuyueDatas();

        if (data == null) {
            mListView.setVisibility(8);
            return;
        }

        List<FocusPictureModel> datas = data.getData();
        if (data != null && datas != null && datas.size() > 0) {
            mListView.setVisibility(0);
        } else {
            mListView.setVisibility(8);
            return;
        }
        if (listAdapter == null) {
            listAdapter = new PageYuyueAdapter(datas, getActivity());
            listAdapter.setOnClicklistener(this);
            mListView.setAdapter(listAdapter);
        } else {
            listAdapter.setDatas(datas);
        }
        // Utils.setListViewHeightBasedOnChildren(mListView);

    }

    /**
     * @param rootView
     *            初始化 巡展 广告
     */
    private void initAvdViewPage(View rootView) {
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

    /**
     * 获取网络数据
     */
    private void getUrlData() {
        // 接口修改，分两次异步请求
        loadHomeData(true);
        loadHomeData(false);
    }

    /**
     * load ad banner resource
     */
    private void loadHomeData(boolean isBanner) {
        UserBean bean = UserService.getInatance().getUserBean();
        RequestParams params;
        if (bean != null) {
            params = UrlTool.getParams(Contansts.TYPE,
                    Contansts.TYPE_FIRSTPAGE, Contansts.USER_ID, bean.getId());
            LogUtils.tiaoshi("FirsttPageFragment.getUrlData()_获取首页数据",
                    bean.getId());
        } else {
            params = UrlTool
                    .getParams(Contansts.TYPE, Contansts.TYPE_FIRSTPAGE);
        }
        SendActtionTool.get(isBanner ? Contansts.ContentParams.HOME_BANNER_URL
                : Contansts.ContentParams.HOME_PAGE_URL,
                isBanner ? ServiceAction.Action_Banner : ServiceAction.Action_FirstPage, null, this, params);
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
        // TODO Auto-generated method stub
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

    @Override
    public void onPageSelected(int arg0) {
        // TODO Auto-generated method stub
        // 设置选中的tip的背景
        for (int i = 0; i < tips.length; i++) {
            if (i == arg0 % mImageViews.length) {
                tips[i].setBackgroundResource(R.drawable.dot_lunbo_dian_hl);
            } else {
                tips[i].setBackgroundResource(R.drawable.dot_lunbo_dian);
            }
        }
    }

    @Override
    public void onDestroy() {
        isStop = true;
        super.onDestroy();
    }

    @Override
    public void onSuccess(ServiceAction service, Object action, Object value) {
        JSONObject obj = (JSONObject) value;
        switch (service) {
        // 当前页面的数据
            case Action_FirstPage:
                mPullRefreshScrollView.onRefreshComplete();
                updatePageDate(obj);
                break;
            // 用户请求
            case Action_User:
                updateYuyue(action, value);
                break;

            case Action_Banner:// home page banner
                mPullRefreshScrollView.onRefreshComplete();
                parseAbBannerData(obj);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFaile(ServiceAction service, Object action, Object value) {
        Utils.toast(getActivity(), value.toString());
        dismissDialog();

    }

    @Override
    public void onException(ServiceAction service, Object action, Object value) {

    }

    @Override
    public void onFinish(ServiceAction service, Object action) {
        super.onFinish(service, action);
        dismissDialog();
    }

    /**
     * @param object
     *            界面数据 json
     *            解析当前界面的 所有数据
     */
    private void updatePageDate(JSONObject object) {
        try {
            JSONArray array = object.getJSONArray(Contansts.DATA);
            JSONObject firstObject = (JSONObject) array.get(0);
            JSONArray dataArray = firstObject.getJSONArray(Contansts.DATA);
            List<HomePageItemInfo> datas = JSON.parseArray(dataArray.toString(), HomePageItemInfo.class);
            FirstPageService.getInstance().setHomePageData(datas);
            updateData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析广告banner数据
     * 
     * @param object
     */
    private void parseAbBannerData(JSONObject object) {
        try {
            JSONArray array = object.getJSONArray(Contansts.DATA);
            List<AdBannerData> datas = JSON.parseArray(array.toString(), AdBannerData.class);
            FirstPageService.getInstance().setAdBannerdatas(datas);
            updateData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param action
     * @param value
     *            预约信息解析
     */
    private void updateYuyue(Object action, Object value) {
        Data data = FirstPageService.getInstance().getYuyueDatas();
        switch ((UserAction) action) {
        // 添加预约成功
            case Action_Yuyue_Add:
                if (data != null && UserAction.Action_Yuyue_Add.value != null) {
                    List<FocusPictureModel> datas = data.getData();
                    for (int i = 0; i < datas.size(); i++) {
                        if (datas.get(i).getAlbumId()
                                .equals(UserAction.Action_Yuyue_Add.value)) {
                            datas.get(i).setIsSubscribe(1);
                            UserAction.Action_Yuyue_Add.value = null;
                            break;
                        }
                    }
                }
                Utils.toast(getActivity(), "添加预约成功");
                if (listAdapter != null) {
                    listAdapter.notifyDataSetChanged();
                }
                break;
            // 取消预约成功
            case Action_Yuyue_Cancle:
                if (data != null && UserAction.Action_Yuyue_Cancle.value != null) {
                    List<FocusPictureModel> datas = data.getData();
                    for (int i = 0; i < datas.size(); i++) {
                        if (datas.get(i).getAlbumId()
                                .equals(UserAction.Action_Yuyue_Cancle.value)) {
                            datas.get(i).setIsSubscribe(0);
                            UserAction.Action_Yuyue_Cancle.value = null;
                            break;
                        }
                    }
                }
                if (listAdapter != null) {
                    listAdapter.notifyDataSetChanged();
                }
                Utils.toast(getActivity(), "取消预约成功");
                break;
            case Action_CHECK_VERSION:
                JSONObject object = (JSONObject) value;
                try {
                    object = object.getJSONObject("androidVersion");
                    String version = object.getString("version");
                    String url = object.getString("url");
                    LogUtils.tiaoshi("版本更新", version + "_"
                            + BaseApplication.mVersionName);
                    if (!version.equals(BaseApplication.mVersionName)) {
                        LogUtils.tiaoshi("版本更新", "开始更新");
                        DialogTool.createCheckDialog(getActivity(), url);
                    } else {
                        LogUtils.tiaoshi("版本更新", "不用更新");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtils.tiaoshi("版本更新", e.toString());

                }
                LogUtils.tiaoshi("版本更新", "更新数据");
                break;
            default:
                break;
        }
    }
}
