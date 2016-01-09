package com.yike;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lidroid.xutils.http.RequestParams;
import com.yike.action.CommentAction;
import com.yike.action.ServiceAction;
import com.yike.adapter.AlbumListAdapter;
import com.yike.adapter.GuessLikeAdapter;
import com.yike.adapter.VideoListAdapter;
import com.yike.bean.*;
import com.yike.service.SearchPageService;
import com.yike.service.UserService;
import com.yike.tool.SendActtionTool;
import com.yike.tool.UrlTool;
import com.yike.utils.Contansts;
import com.yike.utils.LogUtils;
import com.yike.utils.Utils;
import com.yike.view.FlowLayout;
import com.yike.view.MyGridView;
import com.yike.view.NoScroListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rendy 搜索界面
 */
public class SearchActivity extends BaseActivity {
    private int[] hot_bg = {};
    private String[] mVals = new String[]{"5.1 音乐节", "谢天笑4.28",
            "4.26 live house 宋冬野歌唱", "草莓音乐节", "摩登音乐节", "6.16 livehouse"};

    private InputMethodManager mInputMethodManager;
    private EditText mSearchView;
    private ImageView mSearch_cancel;
    private LinearLayout mSearchText;
    private int mIv_width;
    private LayoutParams mRightParams;
    private boolean mSearch_cancel_isVisible;
    private ImageView mClearButton;

    protected NoScroListView mAlbumListView;
    protected NoScroListView mVideoListView;
    private List<SearchResultAlbum> albums;
    private List<SearchResultVideo> videos;
    private AlbumListAdapter albumListAdapter;
    private VideoListAdapter videoListAdapter;

    protected CharSequence mFilterString;
    private FlowLayout mFlowLayout;
    private MyGridView gridView;
    private View xihuanArea;

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.tv_hot_detail:

                break;

            default:
                break;
        }
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_search);
        if (!SearchPageService.getInstance().isHaveDate()) {
            getUrlData();
        } else {
            updateData();
        }
    }

    private void updateData() {
        initXihuan();
        initHotDate();
    }

    private void getUrlData() {
        loadData(ServiceAction.Action_Guess_Like);
        loadData(ServiceAction.Action_Hot_words);
    }

    private void loadData(ServiceAction type) {
        RequestParams params = null;
        String requestUrl = "";
        if (type == ServiceAction.Action_Guess_Like) {
            UserBean bean = UserService.getInatance().getUserBean();
            if (bean != null) {
                params = UrlTool.getParams(Contansts.TYPE, Contansts.ContentParams.CONTENT_TYPE_GUESS_LIKE,
                        Contansts.USER_ID, bean.getId());
            } else {
                params = UrlTool.getParams(Contansts.TYPE, Contansts.ContentParams.CONTENT_TYPE_GUESS_LIKE);
            }
            requestUrl = Contansts.ContentParams.SEARCH_GUESS_LIKE_URL;
        } else if (type == ServiceAction.Action_Hot_words) {
            params = UrlTool.getParams(Contansts.SIZE, Contansts.ContentParams.HOT_WORDS_DEFAULT_SIZE);
            requestUrl = Contansts.ContentParams.HOT_WORDS_URL;
        }
        SendActtionTool.get(requestUrl, type, null, this, params);
    }

    @Override
    public void findViewById() {
        initView();
        initHotDate();
        initSearchView();
        updateView();
        initXihuan();
    }

    private void initXihuan() {
        if (xihuanArea == null) {
            xihuanArea = findViewById(R.id.seach_xihunArea);
            gridView = (MyGridView) findViewById(R.id.search_xihuan);
            gridView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    SearchGuessLikeData data = SearchPageService.getInstance().getGudessLikeData();
                    if (data != null && data.getData() != null) {
                        List<SearchGuessLikeItem> list = data.getData();
                        if (position >= 0 && position < list.size()) {
                            SearchGuessLikeItem item = list.get(position);
                            Utils.playVideo(SearchActivity.this, item.getVideoDetailUrl());
                        }
                    }
                }
            });
        }
        SearchGuessLikeData data = SearchPageService.getInstance().getGudessLikeData();
        if (data == null) {
            xihuanArea.setVisibility(View.GONE);
            return;
        }
        List<SearchGuessLikeItem> datas = data.getData();
        if (data != null && datas != null && datas.size() > 0) {
            xihuanArea.setVisibility(View.VISIBLE);
        } else {
            xihuanArea.setVisibility(View.GONE);
            return;
        }
        GuessLikeAdapter adapter = new GuessLikeAdapter(datas, SearchActivity.this);
        adapter.setIsShow(false);
        gridView.setAdapter(adapter);
    }

    private void initView() {
        mAlbumListView = (NoScroListView) findViewById(R.id.listview_search_albumlist);
        mVideoListView = (NoScroListView) findViewById(R.id.listview_search);
        gridView = (MyGridView) findViewById(R.id.search_xihuan);
        mFlowLayout = (FlowLayout) findViewById(R.id.flowgroup);

        albums = new ArrayList<SearchResultAlbum>();
        videos = new ArrayList<SearchResultVideo>();
        albumListAdapter = new AlbumListAdapter(albums, this);
        videoListAdapter = new VideoListAdapter(videos, this);
        mAlbumListView.setAdapter(albumListAdapter);
        // Utils.setListViewHeightBasedOnChildren(mAlbumListView);
        mAlbumListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                // 播放专辑
                SearchResultAlbum album = albums.get(position);
                Utils.playAlbum(SearchActivity.this, album.getAlbumId(),
                        album.getName());
            }

        });
        mVideoListView.setAdapter(videoListAdapter);
        // Utils.setListViewHeightBasedOnChildren(mVideoListView);
        mVideoListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 播放视频
                SearchResultVideo video = videos.get(position);
                Utils.playVideo(SearchActivity.this,video.getVideoDetailUrl());
            }
        });
    }

    private void hideSoftInputFromWindow() {
        mInputMethodManager.hideSoftInputFromWindow(
                mSearchView.getWindowToken(), 0);
    }

    private void initSearchView() {
        mSearchText = (LinearLayout) findViewById(R.id.ll_search_defult);
        mSearchText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftInputFromWindow();
                return false;
            }

        });
        mSearchView = (EditText) findViewById(R.id.searchview);
        mSearchView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    toSearchMode();
                }
                return false;
            }
        });
        mSearchView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                toSearchMode();
            }
        });
        mSearch_cancel = (ImageView) findViewById(R.id.search_cancel);
        mSearch_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSearch_cancel_isVisible) {
                    mSearch_cancel_isVisible = false;
                    mSearchView.setText("");
                    mSearchView.setCursorVisible(false);
                    getAnimatorList(false).start();
                    showSearchResult(false);
                }
            }
        });
        // mSearchView.addTextChangedListener(new TextWatcher() {
        //
        // @Override
        // public void onTextChanged(CharSequence s, int start, int before,
        // int count) {
        // }
        //
        // @Override
        // public void beforeTextChanged(CharSequence s, int start, int count,
        // int after) {
        // }
        //
        // @Override
        // public void afterTextChanged(Editable s) {
        //
        // String query = mSearchView.getText().toString().trim();
        // if (!TextUtils.isEmpty(query)) {
        // // search(query.toString());
        // mClearButton.setVisibility(View.VISIBLE);
        // } else {
        // mAlbumListView.setVisibility(View.GONE);
        // mVideoListView.setVisibility(View.GONE);
        // mSearchText.setVisibility(View.VISIBLE);
        // mClearButton.setVisibility(View.GONE);
        // }
        // }
        // });
        mSearchView.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_UP) {
                    if (!TextUtils.isEmpty(mFilterString)) {
                        mInputMethodManager.hideSoftInputFromWindow(
                                mSearchView.getWindowToken(), 0);
                    }
                    String query = mSearchView.getText().toString();
                    if (!TextUtils.isEmpty(query)) {
                        search(query);
                    }
                }

                return false;
            }
        });
        mClearButton = (ImageView) findViewById(R.id.clear_text);
        mClearButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mSearchView.setText("");
            }
        });
        mSearchView.requestFocus();
        mSearchView.setFocusable(true);
        mSearchView.setCursorVisible(true);
        mSearch_cancel_isVisible = true;
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void toSearchMode() {
        mSearchView.setCursorVisible(true);
        if (!mSearch_cancel_isVisible) {
            mSearch_cancel_isVisible = true;
            mSearch_cancel.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getAnimatorList(true).start();
                }
            }, 300);
        }
    }

    public void initHotDate() {
        if (mFlowLayout == null) {
            mFlowLayout = (FlowLayout) findViewById(R.id.flowgroup);
        }
        mFlowLayout.removeAllViews();
        LayoutInflater mInflater = LayoutInflater.from(this);
        List<HotKeyWords> hotWords = SearchPageService.getInstance().getHotWords();
        if (hotWords != null) {
            for (int i = 0; i < hotWords.size(); i++) {
                HotKeyWords word = hotWords.get(i);
                final TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                        mFlowLayout, false);
                tv.setText(word.getWord());
                tv.setBackgroundResource(R.drawable.hot_word_bg);
                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mSearchView.setText(tv.getText());
                        search(tv.getText().toString());
                    }
                });
                mFlowLayout.addView(tv);
            }
        }
    }

    private void updateView() {
        mRightParams = (LayoutParams) mSearch_cancel.getLayoutParams();
        if (mIv_width == 0) {
            final int widthSpec = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            final int heightSpec = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            mSearch_cancel.measure(widthSpec, heightSpec);
            mIv_width = mSearch_cancel.getMeasuredWidth();
        }
        if (mSearch_cancel_isVisible) {
            mRightParams.rightMargin = getResources().getDimensionPixelSize(R.dimen.search_cancel_margin_right);
        } else {
            mRightParams.rightMargin = -mIv_width;
        }
        mSearch_cancel.getParent().requestLayout();
    }

    private AnimatorSet getAnimatorList(boolean isEditMode) {
        AnimatorSet set = new AnimatorSet();
        Animator[] animatorArray = new Animator[2];
        if (isEditMode) {
            animatorArray[1] = visibleView(mSearch_cancel, true);
        } else {
            animatorArray[1] = visibleView(mSearch_cancel, false);
        }
        set.play(animatorArray[1]);
        return set;
    }

    private Animator visibleView(final View target, final boolean visible) {
        ValueAnimator animator = (ValueAnimator) target
                .getTag(R.id.search_cancel);
        if (animator != null) {
            animator.cancel();
        }
        if (visible) {
            animator = ValueAnimator.ofInt(-mIv_width, getResources().getDimensionPixelSize(R.dimen.search_cancel_margin_right));
        } else {
            animator = ValueAnimator.ofInt(getResources().getDimensionPixelSize(R.dimen.search_cancel_margin_right), -mIv_width);
        }
        target.setTag(R.id.search_cancel, animator);
        ValueAnimator.setFrameDelay(1000 / 60);
        final RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) target
                .getLayoutParams();
        animator.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                if (!visible) {
                    hideSoftInputFromWindow();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                params.rightMargin = value;
                target.getParent().requestLayout();
            }

        });
        return animator;
    }

    @Override
    public void onSuccess(ServiceAction service, Object action, Object value) {
        JSONObject obj = (JSONObject) value;
        switch (service) {
            case Action_Guess_Like:
                parseGuessLikeData(obj);
                break;
            case Action_Hot_words://热词
                parseHotwords(obj);
                break;
            case Action_Comment://search
                String jsonString = value.toString();
                // LogUtils.d("jsonString== " + jsonString);
                Result<SearchResultAlbum, SearchResultVideo> temResult = JSON.parseObject(jsonString,
                        new TypeReference<Result<SearchResultAlbum, SearchResultVideo>>() {
                        });
                if (temResult == null) {
                    LogUtils.d("---temResult == null");
                } else {
                    LogUtils.d("---temResult != null");
                    SearchData data = temResult.data;
                    if (data == null) {
                        LogUtils.d("---data == null");
                        showSearchResult(false);
                    } else {
                        showSearchResult(true);
                        LogUtils.d("---data != null");
                        List<SearchResultAlbum> albumList = data.getAlbumList();
                        if (albumList == null) {
                            LogUtils.d("---albumList != null");
                        } else {
                            albums.clear();
                            albums.addAll(albumList);
                            albumListAdapter.notifyDataSetChanged();
                            // Utils.setListViewHeightBasedOnChildren(mAlbumListView);
                        }
                        List<SearchResultVideo> videoList = data.getVideoList();
                        if (albumList == null) {
                            LogUtils.d("---videoList != null");
                        } else {
                            videos.clear();
                            videos.addAll(videoList);
                            videoListAdapter.notifyDataSetChanged();
                            // Utils.setListViewHeightBasedOnChildren(mVideoListView);
                        }
                    }
                }
                break;
            default:
                break;
        }


    }

    private void parseHotwords(JSONObject obj) {
        try {
            JSONArray array = obj.getJSONArray(Contansts.DATA);
            List<HotKeyWords> datas = JSON.parseArray(array.toString(), HotKeyWords.class);
            if (datas != null && datas.size() > 0) {
                SearchPageService.getInstance().setHotWords(datas);
                updateData();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseGuessLikeData(JSONObject obj) {
        try {
            JSONArray array = obj.getJSONArray(Contansts.DATA);
            List<SearchGuessLikeData> datas = JSON.parseArray(array.toString(), SearchGuessLikeData.class);
            if (datas != null && datas.size() > 0) {
                SearchGuessLikeData data = datas.get(0);
                SearchPageService.getInstance().setGudessLikeData(data);
                updateData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSearchResult(boolean show) {
        if (show) {
            mSearchText.setVisibility(View.GONE);
            mAlbumListView.setVisibility(View.VISIBLE);
            mVideoListView.setVisibility(View.VISIBLE);
            hideSoftInputFromWindow();
            toSearchMode();
            //移动光标到末尾
            mSearchView.setSelection(mSearchView.getText().length());
            mSearchView.setCursorVisible(false);
        } else {
            mSearchText.setVisibility(View.VISIBLE);
            mAlbumListView.setVisibility(View.GONE);
            mVideoListView.setVisibility(View.GONE);

        }
    }

    @Override
    public void onFaile(ServiceAction service, Object action, Object value) {
        // TODO Auto-generated method stub
        super.onFaile(service, action, value);
    }

    @Override
    public void onException(ServiceAction service, Object action, Object value) {
        // TODO Auto-generated method stub
        super.onException(service, action, value);
    }

    protected void search(String string) {
        LogUtils.d("keywords--", string);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("keywords", string);
        SendActtionTool.get(Contansts.SearchParams.SEARCH_ENGINE_URL,
                ServiceAction.Action_Comment,
                CommentAction.Action_Serach_Keywords, this, params);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.modernsky.istv.listener.CommonListener#onStart(com.modernsky.istv
     * .action.ServiceAction, java.lang.Object)
     */
    @Override
    public void onStart(ServiceAction service, Object action) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.modernsky.istv.listener.CommonListener#onFinish(com.modernsky.istv
     * .action.ServiceAction, java.lang.Object)
     */
    @Override
    public void onFinish(ServiceAction service, Object action) {
        // TODO Auto-generated method stub

    }

}
