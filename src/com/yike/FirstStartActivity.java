package com.yike;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.yike.R;
import com.yike.adapter.ViewPagerAdapter;
import com.yike.utils.PreferencesUtils;

/**
 * 
 * @author rendy
 * 
 *         首页界面
 * 
 */
public class FirstStartActivity extends BaseActivity implements
		OnClickListener, OnPageChangeListener {

	private ViewPager vp;
	private ViewPagerAdapter vpAdapter;
	private List<View> views;
	// 引导图片资源
	private static final int[] pics = { 
			 };

	// 底部小点图片
	private ImageView[] dots;

	// 记录当前选中位置
	private int currentIndex;
	private Button startButton;
	private LinearLayout ll;

	/** Called when the activity is first created. */

	@Override
	public void setContentView() {
		if (true) {
			startActivity(new Intent(this, WelcomeActivity.class));
			finish();
		}
		setContentView(R.layout.activity_first);
	}

	@Override
	public void findViewById() {
		startButton = (Button) findViewById(R.id.button1);
		startButton.setOnClickListener(this);
		initViewPagers();
	}

	/**
	 * 初始化巡展
	 */
	private void initViewPagers() {
		views = new ArrayList<View>();
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		// 初始化引导图片列表
		for (int i = 0; i < pics.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(mParams);
			iv.setScaleType(ScaleType.CENTER_CROP);
			iv.setImageResource(pics[i]);
			views.add(iv);
		}
		vp = (ViewPager) findViewById(R.id.viewpager);
		// 初始化Adapter
		vpAdapter = new ViewPagerAdapter(views);
		vp.setAdapter(vpAdapter);
		// 绑定回调
		vp.setOnPageChangeListener(this);

		// 初始化底部小点
		initDots();
	}

	private void initDots() {
		ll = (LinearLayout) findViewById(R.id.ll);

		dots = new ImageView[pics.length];

		// 循环取得小点图片
		for (int i = 0; i < pics.length; i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(true);// 都设为灰色
			dots[i].setOnClickListener(this);
			dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
		}

		currentIndex = 0;
		dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
	}

	/**
	 * 设置当前的引导页
	 */
	private void setCurView(int position) {
		if (position < 0 || position >= pics.length) {
			return;
		}
		setStartButtonVisible(position);

		vp.setCurrentItem(position);
	}

	private void setStartButtonVisible(int position) {
		if (position == pics.length - 1) {
			ll.setVisibility(View.INVISIBLE);
			startButton.setVisibility(View.VISIBLE);
		} else {
			startButton.setVisibility(View.GONE);
			ll.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 这只当前引导小点的选中
	 */
	private void setCurDot(int positon) {
		if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
			return;
		}
		setStartButtonVisible(positon);
		dots[positon].setEnabled(false);
		dots[currentIndex].setEnabled(true);
		currentIndex = positon;
	}

	// 当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	// 当当前页面被滑动时调用
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	// 当新的页面被选中时调用
	@Override
	public void onPageSelected(int arg0) {
		setCurDot(arg0);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button1) {
			PreferencesUtils.setFirstStartInfo(this, true);
			startActivity(new Intent(this, MainActivity.class));
			finish();
		} else {
			int position = (Integer) v.getTag();
			setCurView(position);
			setCurDot(position);
		}
	}

}