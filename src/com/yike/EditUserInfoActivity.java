package com.yike;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yike.R;
import com.yike.action.ServiceAction;
import com.yike.action.UserAction;
import com.yike.bean.UserBean;
import com.yike.service.UserService;
import com.yike.tool.SendActtionTool;
import com.yike.tool.UrlTool;
import com.yike.utils.CheckCode;
import com.yike.utils.GeneralTool;
import com.yike.utils.Utils;
import com.yike.utils.Contansts.UserParams;

/**
 * @author rendy
 *         编辑个人信息
 */
public class EditUserInfoActivity extends BaseActivity {
    private TextView titleTet;
    private EditText edit;
    private int tipe;
    private UserBean userBean;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_back:
                finish();
                break;
            case R.id.edit_save:
                sendAction();
                break;
            default:
                break;
        }
    }

    private void sendAction() {
        String value = edit.getText().toString();
        switch (tipe) {
        // 更改邮箱
            case R.string._emial:
                if (CheckCode.isEmail(value)) {
                    SendActtionTool.get(UserParams.URL_USER_UPDATE, null,
                            UserAction.Action_Update_Email, this, UrlTool
                                    .getParams(UserParams.KEY, UserParams.EMAIL,
                                            UserParams.VALUE, value,
                                            UserParams.USER_ID, userBean.getId()));
                } else {
                    Utils.toast(getApplicationContext(), "请输入正确的邮箱");
                }
                break;
            // 更改名字
            case R.string._name:
                if (!GeneralTool.isEmpty(value)) {
                    SendActtionTool.get(UserParams.URL_USER_UPDATE, null,
                            UserAction.Action_Update_UserName, this, UrlTool
                                    .getParams(UserParams.USERNAME, value,
                                            UserParams.USER_ID, userBean.getId()));
                } else {
                    Utils.toast(getApplicationContext(), "请输入姓名");
                }
                break;
            // 更该 电话
            case R.string._phone:
                if (CheckCode.isMobileNO(value)) {
                    SendActtionTool.get(UserParams.URL_USER_UPDATE, null,
                            UserAction.Action_Update_Mobiles, this, UrlTool
                                    .getParams(UserParams.KEY, UserParams.MOBILE,
                                            UserParams.VALUE, value,
                                            UserParams.USER_ID, userBean.getId()));
                } else {
                    Utils.toast(getApplicationContext(), "请输入正确的手机号");
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_edituserinfo);
        userBean = UserService.getInatance().getUserBean();
    }

    @Override
    public void findViewById() {
        tipe = getIntent().getIntExtra(EditUserInfoActivity.class.getName(),
                R.string._name);
        edit = (EditText) findViewById(R.id.edit_editEdt);
        titleTet = (TextView) findViewById(R.id.edit_title);
        titleTet.setText(tipe);
        initHint();
        findViewById(R.id.edit_save).setOnClickListener(this);
        findViewById(R.id.edit_back).setOnClickListener(this);
    }

    private void initHint() {

        switch (tipe) {
        // 更改邮箱
            case R.string._emial:
                edit.setText(userBean.getEmail());
                edit.setHint("请输入邮箱");
                break;
            // 更改名字
            case R.string._name:
                edit.setText(userBean.getUserName());
                edit.setHint("请输入姓名");
                break;
            // 更该 电话
            case R.string._phone:
                edit.setText(userBean.getMobile());
                edit.setHint("请输入电话");
                break;

            default:
                break;
        }

    }

    @Override
    public void onSuccess(ServiceAction service, Object action, Object value) {
        super.onSuccess(service, action, value);
        String modify = edit.getText().toString();
        switch ((UserAction) action) {
            case Action_Update_Email:
                userBean.setEmail(modify);
                break;
            case Action_Update_UserName:
                userBean.setUserName(modify);
                break;
            case Action_Update_Mobiles:
                userBean.setMobile(modify);
                break;
            default:
                break;
        }
        UserService.getInatance().setUserBean(userBean);
        finish();
    }

    @Override
    public void onFaile(ServiceAction service, Object action, Object value) {
        Utils.toast(getApplicationContext(), value.toString());
    }

    @Override
    public void onFinish(ServiceAction service, Object action) {
        super.onFinish(service, action);
    }

}
