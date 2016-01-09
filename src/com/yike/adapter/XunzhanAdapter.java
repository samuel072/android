package com.yike.adapter;
//package com.modernsky.istv.adapter;
//
//import java.util.List;
//
//import com.modernsky.istv.bean.FocusPictureModel;
//
//import android.support.v4.view.PagerAdapter;
//import android.view.View;
//import android.view.ViewGroup;
//// viewpager适配器
//class MyPagerdapter extends PagerAdapter {
//    private  List<FocusPictureModel> datas;
//    private  
//	@Override
//	public Object instantiateItem(ViewGroup container, int position) {
//		// return super.instantiateItem(container, position);
//
//		container.addView(mImageViews[position], 0);
//		return mImageViews[position];
//	}
//
//	@Override
//	public void destroyItem(ViewGroup container, int position, Object object) {
//		container.removeView(mImageViews[position]);
//	}
//
//	@Override
//	public int getCount() {
//		return mImageViews.length;
//	}
//
//	@Override
//	public boolean isViewFromObject(View arg0, Object arg1) {
//		return arg0 == arg1;
//	}
//}