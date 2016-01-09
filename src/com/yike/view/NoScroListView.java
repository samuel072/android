/**
 * 
 */
package com.yike.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author： fengqingyun2008
 * @Email： fengqingyun2008@gmail.com
 * @version：1.0
 * @创建时间：2015-4-17 下午3:25:32
 * @类说明：
 */
public class NoScroListView extends ListView {

	public NoScroListView(Context context) {
		super(context);
	}

	public NoScroListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScroListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
