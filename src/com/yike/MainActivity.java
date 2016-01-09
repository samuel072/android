package com.yike;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.yike.R;
import com.umeng.analytics.MobclickAgent;
import com.yike.fragment.FirstPageFragment;
import com.yike.fragment.LiveFragment;
import com.yike.fragment.RankingFragment;
import com.yike.fragment.TopicFragment;
import com.yike.fragment.ExploreFragment;
import com.yike.manager.DavikActivityManager;
import com.yike.service.UserService;
import com.yike.utils.LogUtils;

/**
 * 主界面
 * 
 * @author zxm
 */
public class MainActivity extends BaseActivity implements OnClickListener {

    private static FragmentManager fMgr;
    private long exitTime = 0;
    private ImageView indexView;
    private TextView titleView;
    private Fragment fragmentFirst, fragmentZhipo, fragmentYinyuejie,
            fragmentPaihang, fragmentZhuanti;
    private Fragment currentFragment;
    private List<Fragment> fragmetns = new ArrayList<Fragment>();
    private RadioGroup radioGroup;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void findViewById() {
        initView();
        // 获取FragmentManager实例
        fMgr = getSupportFragmentManager();
        initFragment();
        dealBottomButtonsClickEvent();
    }

    private void initView() {
        // TODO Auto-generated method stub
        findViewById(R.id.img_me).setOnClickListener(this);
        findViewById(R.id.img_search).setOnClickListener(this);
        findViewById(R.id.img_live).setOnClickListener(this);
        indexView = (ImageView) findViewById(R.id.ImageButton1);
        titleView = (TextView) findViewById(R.id.tv_title);
    }

    public void setTitle(String title) {
        if (titleView != null) {
            titleView.setText(title);
        }
    }

    public void updateView(int id) {
        if (indexView != null) {
            indexView.setImageResource(R.color.white);
            indexView = null;
        }
        indexView = (ImageView) findViewById(id);
        indexView.setImageResource(R.color.default_color);
    }

    /**
     * 2015年1月16日
     * 
     * @param fragmentName
     * @return 展示当前的Fragment
     */
    private Fragment showPlaneFragment(Fragment tagFragment) {
        if (currentFragment.equals(tagFragment)) {
            return tagFragment;
        }
        if (fragmetns.contains(tagFragment)) {
            getFragmentTransaction().hide(currentFragment).show(tagFragment)
                    .commit();
        } else {
            getFragmentTransaction()
                    .hide(currentFragment)
                    .add(R.id.fragmentRoot, tagFragment,
                            tagFragment.getClass().getName()).commit();
            fragmetns.add(tagFragment);
        }
        currentFragment = tagFragment;
        return tagFragment;
    }

    /**
     * 跳转到音乐节
     */
    public void changeToyinYuejie() {
        radioGroup.check(R.id.rb_chennel);
    }

    /**
     * 初始化首个Fragment
     */
    private void initFragment() {

        fragmentFirst = new FirstPageFragment();
        currentFragment = fragmentFirst;
        fragmetns.add(currentFragment);
        getFragmentTransaction().add(R.id.fragmentRoot, fragmentFirst,
                fragmentFirst.getClass().getName()).commit();

    }

    /**
     * 获取Fragment事务处理
     */
    private FragmentTransaction getFragmentTransaction() {
        FragmentTransaction transaction = fMgr.beginTransaction();
        transaction.setCustomAnimations(R.anim.push_right_in,
                R.anim.push_left_out, R.anim.push_left_in,
                R.anim.push_right_out);
        return transaction;
    }

    /**
     * 处理底部点击事件
     */
    private void dealBottomButtonsClickEvent() {
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {

                // 首页
                    case R.id.rb_firstpage:
                        // updateView(R.id.ImageButton1);
                        setTitle(getResources().getString(R.string._firstpage));
                        fragmentFirst = fragmentFirst == null ? new FirstPageFragment()
                                : fragmentFirst;
                        showPlaneFragment(fragmentFirst);
                        break;
                    // 直播
                    case R.id.rb_live:
                        // updateView(R.id.ImageButton);
                        setTitle(getResources().getString(R.string._zhibo));
                        fragmentZhipo = fragmentZhipo == null ? new LiveFragment()
                                : fragmentZhipo;
                        showPlaneFragment(fragmentZhipo);
                        break;
                    // 探索
                    case R.id.rb_chennel:
                        setTitle(getResources().getString(R.string._yinyuejie));
                        // updateView(R.id.ImageButton3);
                        fragmentYinyuejie = fragmentYinyuejie == null ? new ExploreFragment()
                                : fragmentYinyuejie;
                        showPlaneFragment(fragmentYinyuejie);
                        break;
                    // 排行
                    case R.id.rb_ranking:
                        // updateView(R.id.ImageButton4);
                        setTitle(getResources().getString(R.string._paihangbang));
                        fragmentPaihang = fragmentPaihang == null ? new RankingFragment()
                                : fragmentPaihang;
                        showPlaneFragment(fragmentPaihang);
                        break;
                    // 专题
                    case R.id.rb_topic:
                        // updateView(R.id.ImageButton5);
                        setTitle(getResources().getString(R.string._zhuanti));
                        popAllFragmentsExceptTheBottomOne();
                        fragmentZhuanti = fragmentZhuanti == null ? new TopicFragment()
                                : fragmentZhuanti;
                        showPlaneFragment(fragmentZhuanti);
                        break;

                    default:
                        break;
                }
            }
        });

    }

    /**
     * 从back stack弹出所有的fragment
     */
    public static void popAllFragmentsExceptTheBottomOne() {
        for (int i = 0, count = fMgr.getBackStackEntryCount(); i < count; i++) {
            fMgr.popBackStack();
        }
    }

    // 点击返回按钮
    @Override
    public void onBackPressed() {
        if (fMgr.getBackStackEntryCount() <= 1) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                MobclickAgent.onKillProcess(this);
                DavikActivityManager.getScreenManager().exitApp(Activity.class);
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_me:
                if (UserService.getInatance().isNeedLogin(MainActivity.this)) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    startActivity(new Intent(this, UserCenterActivity.class));
                }
                break;
            case R.id.img_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.img_live:
                // Utils.playVideo(this, "269", "2015影响城市之声", "25");

                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        LogUtils.tiaoshi("onActivityResult---", arg0 + "," + arg1);

        super.onActivityResult(arg0, arg1, arg2);
    }

}
