package com.yike;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yike.action.ServiceAction;
import com.yike.bean.UserBean;
import com.yike.service.UserService;
import com.yike.tool.BitmapTool;
import com.yike.utils.GeneralTool;
import com.yike.utils.Utils;

/**
 * @author rendy 设置界面
 */
public class UserCenterActivity extends BaseActivity {
    
    private ImageView mUserHeadView;
    private TextView mUserNameTv;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_user_center);
    }

    @Override
    public void findViewById() {
        mUserHeadView = (ImageView)findViewById(R.id.head_sculpture);
        UserBean userBean = UserService.getInatance().getUserBean();
        if (!GeneralTool.isEmpty(userBean.getFaceUrl())) {
            BitmapTool.getInstance().getAdapterUitl()
                    .display(mUserHeadView, userBean.getFaceUrl());
        }
        mUserNameTv = (TextView)findViewById(R.id.name);
        if (TextUtils.isEmpty(userBean.getUserName())) {
            mUserNameTv.setText(userBean.getMobile());
        } else {
            mUserNameTv.setText(userBean.getUserName());
        }
        
        findViewById(R.id.btn_return).setOnClickListener(this);
        findViewById(R.id.userinfo).setOnClickListener(this);
        findViewById(R.id.userinfo_yuyue).setOnClickListener(this);
        findViewById(R.id.userinfo_collect).setOnClickListener(this);
        findViewById(R.id.userinfo_setting).setOnClickListener(this);

    }

    @Override
    public void onSuccess(ServiceAction service, Object action, Object value) {
    }

    @Override
    public void onFaile(ServiceAction service, Object action, Object value) {
        Utils.toast(getApplicationContext(), value.toString());
    }

    @Override
    public void onFinish(ServiceAction service, Object action) {
        dismissDialog();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.btn_return:
                finish();
                break;
            case R.id.userinfo:
                startActivity(new Intent(this, SetUserInfoActivity.class));
                break;
            case R.id.userinfo_yuyue:
                startActivity(new Intent(this, AppointmentListActivity.class));
                break;
            case R.id.userinfo_collect:
                Intent intent = new Intent(UserCenterActivity.this, FavoritesActivity.class);
               startActivity(intent);
                break;
            case R.id.userinfo_setting:
                startActivity(new Intent(this, SetActivity.class));
                break;
        }

    }

}
