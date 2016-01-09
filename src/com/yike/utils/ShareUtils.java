package com.yike.utils;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow.OnDismissListener;

import com.yike.view.PopThreeShare;

public class ShareUtils {

    public static void share(final Activity activity, String tille, String content, String videUrl) {
        PopThreeShare share = new PopThreeShare(activity);
        share.setShareInfo(tille, content, videUrl);
        ColorDrawable cd = new ColorDrawable(0x000000);
        share.setBackgroundDrawable(cd);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.2f;
        activity.getWindow().setAttributes(lp);
        share.update();
        share.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.alpha = 1f;
                activity.getWindow().setAttributes(lp);
            }
        });
        share.showPop();
    }

}
