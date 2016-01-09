package com.yike.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
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
import com.yike.R;
import com.yike.action.ServiceAction;
import com.yike.action.UserAction;
import com.yike.adapter.LiveMediaTypeAdapter;
import com.yike.adapter.PageYuyueAdapter;
import com.yike.adapter.SoonZhiBoAdapter;
import com.yike.bean.*;
import com.yike.service.FirstPageService;
import com.yike.service.UserService;
import com.yike.service.ZhiboPageService;
import com.yike.tool.BitmapTool;
import com.yike.tool.DialogTool;
import com.yike.tool.SendActtionTool;
import com.yike.tool.UrlTool;
import com.yike.utils.Contansts;
import com.yike.utils.Contansts.UserParams;
import com.yike.utils.FavoritesUtils;
import com.yike.utils.LogUtils;
import com.yike.utils.MarkLikeUtils;
import com.yike.utils.ShareUtils;
import com.yike.utils.Utils;
import com.yike.view.MyGridView;
import com.yike.view.NoScroListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author rendy
 *         直播界面
 */
public class LiveFragment extends BaseFragment implements OnPageChangeListener {
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

    private boolean isStop = false; // 是否停止子线程, 不会停止

    private NoScroListView mListView;
    private PageYuyueAdapter listAdapter;
    private MyGridView mGridView;
    private LiveMediaTypeAdapter gridAdapter;
    private MyGridView moreGridView;
    private SoonZhiBoAdapter moreListAdapter;

    private Handler handler = new Handler();
    private ViewGroup group;
    private Timer timer;
    private PullToRefreshScrollView mPullRefreshScrollView;
    private int currentTag;
    // View pager的点击事件
    OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int positon = viewPager.getCurrentItem();
            FocusPictureModel model = FirstPageService.getInstance()
                    .getAdvertisementsDatas().getData().get(positon);
            LiveData adData = ZhiboPageService.getInstance().getZhiBaoBanner();
            if (adData != null && adData.getData() != null) {
                if (positon > 0 && positon < adData.getData().size()) {
                    LiveItemInfo liveItemInfo = adData.getData().get(positon);
                    Utils.playVideo(mActivity, liveItemInfo.getVideoDetailUrl());
                }
            }
        }
    };
    private Dialog padyDialog;
    private ImageView mCurrentZhiboView;
    private ImageView mReadButton;
    private ImageButton mMarkLike;
    private ImageButton mAddFav;
    private ImageButton mShare;

    @Override
    public View setContentView(LayoutInflater inflater) {
        mPullRefreshScrollView = (PullToRefreshScrollView) inflater.inflate(
                R.layout.fragment_live, null);
        return mPullRefreshScrollView;
    }

    @Override
    public void initView(View rootView) {
        mMarkLike = (ImageButton) rootView.findViewById(R.id.mark_like);
        mMarkLike.setOnClickListener(this);

        mAddFav = (ImageButton) rootView.findViewById(R.id.mark_fav);
        mAddFav.setOnClickListener(this);

        mShare = (ImageButton) rootView.findViewById(R.id.share);
        mShare.setOnClickListener(this);
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
        if (!ZhiboPageService.getInstance().isHaveDate()) {
            getUrlData();
            // 填充数据
        } else {
            updateData();
        }

        mActivity.registerReceiver(mFavDataChangedReceiver, new IntentFilter(
                Contansts.FAV_DATA_CHANGED_RECEIVER));
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
        LiveItemInfo info = getCurrentShowVideo();
        if (bean != null && info != null) {
            FavoritesUtils.queryHasAdded(bean.getId(), info.getVideoId(), ServiceAction.Action_Check_Fav,
                    this);
        }
    }

    /**
     * 填充数据
     */
    private void updateData() {
        // 初始化 巡展 Øß
        // initAvdViewPage(mPullRefreshScrollView);

        initCurrentZhibo();
        // 音乐节
        // initYinyueJie();
        // 正在直播
        initZhengzaiZhibo();
        // 即将直播
        initSoonZhibo();

        // check 是否点赞过, 是否收藏过
        checkMarkLikeAndFavStatus();
    }

    private void initCurrentZhibo() {
        if (mCurrentZhiboView == null) {
            mCurrentZhiboView = (ImageView) mPullRefreshScrollView.findViewById(R.id.img_grid);
        }
        if (mReadButton == null) {
            mReadButton = (ImageView) mPullRefreshScrollView.findViewById(R.id.read_play);
        }

        LiveData data = ZhiboPageService.getInstance().getZhengZaiZhibo();
        if (data != null) {
            List<LiveItemInfo> items = data.getData();
            if (items != null && items.size() > 0) {
                LiveItemInfo info = items.get(0);
                if (info != null) {
                    mCurrentZhiboView.setTag(info);
                    BitmapTool.getInstance().initAdapterUitl(getActivity())
                            .display(mCurrentZhiboView, info.getPic());
                }
            }
        }

        mCurrentZhiboView.setOnClickListener(this);
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

            // 第一个正在处于直播状态的视频
            case R.id.img_grid:
                try {
                    LiveItemInfo info = (LiveItemInfo) v.getTag();
                    Utils.playVideo(mActivity, info.getVideoDetailUrl());
                    // Utils.playLive(mActivity, info.getVideoId(), info.getName(), info.getAlbumId());
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
                LiveItemInfo info = getCurrentShowVideo();
                String nameString = info.getName();
                String picUrlString = info.getPic();
                String videoString = info.getVideoDetailUrl();
                ShareUtils.share(mActivity, nameString, picUrlString, videoString);
                break;
            default:
                break;
        }
    }

    private void markLike() {
        LiveItemInfo info = getCurrentShowVideo();
        UserBean bean = UserService.getInatance().getUserBean();
        if (bean != null && info != null) {
            MarkLikeUtils.markLike(bean.getId(), info.getVideoId(), ServiceAction.Action_Mark_Like, this);
            mMarkLike.setBackgroundResource(R.drawable.like_one_click);
        } else {
            if (bean == null) {
                Toast.makeText(mActivity, "请先登陆!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mActivity, "点赞失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addToFav() {
        LiveItemInfo info = getCurrentShowVideo();
        UserBean bean = UserService.getInatance().getUserBean();
        if (bean != null && info != null) {
            FavoritesUtils.addToFav(bean.getId(), info.getVideoId(), ServiceAction.Action_Add_Fav, this);
            Toast.makeText(mActivity, "正在添加到收藏夹", Toast.LENGTH_SHORT).show();
            mAddFav.setBackgroundResource(R.drawable.collect_click);
        } else {
            if (bean == null) {
                Toast.makeText(mActivity, "请先登陆!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mActivity, "添加收藏失败,请重试!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private LiveItemInfo getCurrentShowVideo() {
        LiveData data = ZhiboPageService.getInstance().getZhengZaiZhibo();
        LiveItemInfo info = null;
        if (data != null && data.getData() != null) {
            List<LiveItemInfo> list = data.getData();
            if (list != null && list.size() > 0) {
                info = list.get(0);
            }
        }
        return info;
    }

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

    /**
     * 即将直播
     */
    private void initSoonZhibo() {
        if (moreGridView == null) {
            moreGridView = (MyGridView) mPullRefreshScrollView
                    .findViewById(R.id.gridViewMore);
            moreGridView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {
                    if (id == position + SoonZhiBoAdapter.TYPE_RESERVATION_CLICK) {
                        // 预约观看
                        // TODO 预约观看
                        Toast.makeText(mActivity, "预约观看", Toast.LENGTH_SHORT).show();
                    } else {
                        LiveData data = ZhiboPageService.getInstance().getZhengZaiZhibo();
                        if (data == null) {
                            mGridView.setVisibility(View.GONE);
                            return;
                        }
                        List<LiveItemInfo> datas = data.getData();
                        List<LiveItemInfo> infoList = new ArrayList<LiveItemInfo>();
                        for (LiveItemInfo liveItemInfo : datas) {
                            if (liveItemInfo.getType().equals("2")) {
                                infoList.add(liveItemInfo);
                            }
                        }
                        if (data != null && infoList.size() > 0) {
                            if (position >= 0 && position < infoList.size()) {
                                LiveItemInfo model = infoList.get(position);
                                Utils.playVideo(mActivity, model.getVideoDetailUrl());
                            }
                        }
                    }

                }
            });
        }

        LiveData data = ZhiboPageService.getInstance().getZhengZaiZhibo();
        if (data == null) {
            mGridView.setVisibility(View.GONE);
            return;
        }
        List<LiveItemInfo> datas = data.getData();
        List<LiveItemInfo> infoList = new ArrayList<LiveItemInfo>();
        for (LiveItemInfo liveItemInfo : datas) {
            LogUtils.d("test","soon type: " + liveItemInfo.getType());
            if (liveItemInfo.getType().equals("2")) {
                infoList.add(liveItemInfo);
            }
        }
        if ( infoList.size() > 0) {
            moreGridView.setVisibility(View.VISIBLE);
            moreListAdapter = new SoonZhiBoAdapter(infoList,
                    getActivity());
            moreGridView.setAdapter(moreListAdapter);
        } else {
            moreGridView.setVisibility(View.GONE);
        }

    }

    /**
     * 独家热播初始化
     */
    private void initZhengzaiZhibo() {
        if (mGridView == null) {
            mGridView = (MyGridView) mPullRefreshScrollView
                    .findViewById(R.id.gridView);
            mGridView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {
                    
                    LiveData data = ZhiboPageService.getInstance().getZhengZaiZhibo();
                    if (data == null) {
                        return;
                    }

                    List<LiveItemInfo> datas = data.getData();
                    List<LiveItemInfo> infoList = new ArrayList<LiveItemInfo>();
                    
                    for (LiveItemInfo liveItemInfo : datas) {
                        if (liveItemInfo.getType().equals("4")) {
                            infoList.add(liveItemInfo);
                        }
                    }
                    LiveItemInfo info = infoList.get(position);
                    Utils.playVideo(mActivity, info.getVideoDetailUrl());
                }
            });

        }

        LiveData data = ZhiboPageService.getInstance().getZhengZaiZhibo();
        if (data == null) {
            mGridView.setVisibility(View.GONE);
            return;
        }

        List<LiveItemInfo> datas = data.getData();
        List<LiveItemInfo> infoList = new ArrayList<LiveItemInfo>();
        
        for (LiveItemInfo liveItemInfo : datas) {
            LogUtils.d("test","live type: " + liveItemInfo.getType());
            if (liveItemInfo.getType().equals("4")) {
                infoList.add(liveItemInfo);
            }
        }
        if (data != null && datas != null && infoList.size() > 0) {
            mGridView.setVisibility(View.VISIBLE);
        } else {
            mGridView.setVisibility(View.GONE);
            return;
        }
        gridAdapter = new LiveMediaTypeAdapter(infoList, mActivity);
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
        Data data = ZhiboPageService.getInstance().getYuyueDatas();

        if (data == null) {
            mListView.setVisibility(View.GONE);
            return;
        }

        List<FocusPictureModel> datas = data.getData();
        if (data != null && datas != null && datas.size() > 0) {
            mListView.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.GONE);
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
     * @param rootView 初始化 巡展 广告
     */
    private void initAvdViewPage(View rootView) {
        if (group == null) {
            group = (ViewGroup) rootView.findViewById(R.id.viewGroup);
            viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        }

        LiveData data = ZhiboPageService.getInstance().getZhiBaoBanner();

        if (data == null) {
            group.setVisibility(View.GONE);
            return;
        }

        List<LiveItemInfo> datas = data.getData();
        if (data == null || datas == null || datas.size() == 0) {
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
            LiveItemInfo info = datas.get(i);
            String pic = info.getPic();
            Log.e("ww", "zhibo banner pic=" + pic);
            BitmapTool.getInstance().initAdapterUitl(getActivity())
                    .display(imageView, pic);
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
        UserBean bean = UserService.getInatance().getUserBean();
        RequestParams params;
        if (bean != null) {
            params = UrlTool.getParams(Contansts.TYPE, Contansts.TYPE_ZhiBo,
                    Contansts.USER_ID, bean.getId());
        } else {
            params = UrlTool.getParams(Contansts.TYPE, Contansts.TYPE_ZhiBo);
        }

        SendActtionTool.get(Contansts.ContentParams.LIVE_PAGE_URL,
                ServiceAction.Action_FirstPage, null, this, params);
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
            Toast.makeText(mActivity, "点赞成功 +1", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseAddFavResult(JSONObject object) {
        try {
            JSONObject jsonObject = object.getJSONObject(Contansts.DATA);
            // TODO:暂时未使用
            AddFavResult addFavResult = JSON.parseObject(jsonObject.toString(), AddFavResult.class);
            Toast.makeText(mActivity, "添加收藏成功", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFaile(ServiceAction service, Object action, Object value) {
        if (service == ServiceAction.Action_Add_Fav) {
            Toast.makeText(mActivity, "添加收藏失败,请重试!", Toast.LENGTH_SHORT).show();
            mAddFav.setBackgroundResource(R.drawable.collect);
        } else if (service == ServiceAction.Action_Check_Fav) {
            refreshFavStatus(false);
        } else {
            Utils.toast(getActivity(), value.toString());
        }
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
     * @param object 界面数据 json
     *            解析当前界面的 所有数据
     */
    private void updatePageDate(JSONObject object) {

        try {
            JSONArray array = object.getJSONArray(Contansts.DATA);
            List<LiveData> datas = JSON.parseArray(array.toString(), LiveData.class);
            LiveData data = null;
            for (int i = 0; i < datas.size(); i++) {
                int key = -1;
                data = datas.get(i);
                key = data.getSubscribe();
                switch (key) {
                // 正在
                    case 0:
                        ZhiboPageService.getInstance().setZhengZaiZhibo(data);
                        break;
                    // 预约
                    case 1:
                        ZhiboPageService.getInstance().setYuYueZhibo(data);
                        break;
                    // 将要
                    case 2:
                        ZhiboPageService.getInstance().setJiangYaoZhibo(data);
                        break;
                    default:
                        break;
                }
            }
            updateData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param action
     * @param value 预约信息解析
     */
    private void updateYuyue(Object action, Object value) {
        Data data = ZhiboPageService.getInstance().getYuyueDatas();
        if (data == null) {
            return;
        }
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
                Utils.toast(getActivity(), "取消预约成功");
                break;
            default:
                break;
        }
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }
}
