package com.yike;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.http.RequestParams;
import com.yike.R;
import com.yike.action.ServiceAction;
import com.yike.action.UserAction;
import com.yike.bean.UserBean;
import com.yike.manager.DavikActivityManager;
import com.yike.service.UserService;
import com.yike.tool.SendActtionTool;
import com.yike.tool.UrlTool;
import com.yike.utils.CheckCode;
import com.yike.utils.GeneralTool;
import com.yike.utils.JsonUtils;
import com.yike.utils.LogUtils;
import com.yike.utils.Utils;
import com.yike.utils.Contansts.UserParams;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author rendy
 *         设置注册界面
 */
public class RegisterActivity extends BaseActivity {
    private boolean isAgree = true;
    // 顶部指示器
    private TextView tetTime, tetTitle;
    // 顶部指示器
    private View areaPhone, areaEmail;
    private EditText edtPhone, edtEmail, edtCode, edtPsdPone, edtPsdEmial;
    // 1 手机注册 2 邮箱注册
    private int indexiId = 1;
    private View indView;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_register);
    }

    @Override
    public void findViewById() {
        indView = findViewById(R.id.indexView);
        findViewById(R.id.register_registerBtn).setOnClickListener(this);
        findViewById(R.id.activity_xiyi).setOnClickListener(this);
        tetTime = (TextView) findViewById(R.id.register_codeBtn);
        tetTime.setOnClickListener(this);
        tetTitle = (TextView) findViewById(R.id.register_titleTet);
        areaPhone = findViewById(R.id.register_phoneArea);
        areaEmail = findViewById(R.id.register_emailArea);
        // 找到指示器
        edtCode = (EditText) findViewById(R.id.register_codeEdt);
        edtEmail = (EditText) findViewById(R.id.register_emailEdt);
        edtPhone = (EditText) findViewById(R.id.register_phoneEdt);
        edtPsdEmial = (EditText) findViewById(R.id.register_psdEmailEdt);
        edtPsdPone = (EditText) findViewById(R.id.register_psdEdt);
        indexiId = getIntent().getIntExtra(RegisterActivity.class.getName(), 1);
// String value = getIntent().getStringExtra(
// RegisterOneActivity.class.getName());
        if (indexiId == 2) {
            // 邮箱注册
            areaPhone.setVisibility(8);
            areaEmail.setVisibility(0);
            tetTitle.setText(R.string.regiter_email);
            // edtEmail.setText(value);
        } else {
            // 手机注册
            areaPhone.setVisibility(0);
            areaEmail.setVisibility(8);
            tetTitle.setText(R.string.regiter_phone);
            // edtPhone.setText(value);

        }

    }

    public void backBtn(View view) {
        finish();
    }

    public void agreeBtn(View view) {
        if (isAgree) {
            isAgree = false;
            //indView.setBackgroundResource(R.drawable.icon_08_unchoose);
        } else {
            isAgree = true;
           // indView.setBackgroundResource(R.drawable.icon_08_choose);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        // 执行注册
            case R.id.register_registerBtn:
                sendRegister();
                break;
            case R.id.register_codeBtn:
                if (isWaiteInputJiaoYan) {
                    return;
                }
                String phone = edtPhone.getText().toString();
                if (!CheckCode.isMobileNO(phone)) {
                    Utils.toast(getApplicationContext(), "请输入正确的手机号");
                    return;
                }
                RequestParams request = UrlTool.getParams(UserParams.EMAIL_PHONE, phone);
                SendActtionTool.get(UserParams.URL_GET_REGISTER_CODE, null,
                        UserAction.Action_Regiser_Phone_Code, this, request);
                break;
            case R.id.activity_xiyi:
                Uri uri = Uri.parse("http://182.92.80.2/showpages/agreement.html");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                RegisterActivity.this.startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 执行注册
     */
    private void sendRegister() {
        switch (indexiId) {
        // 执行手机注册
            case 1:
                String phone = edtPhone.getText().toString();
                String phoneCode = edtCode.getText().toString();
                String phonePsd = edtPsdPone.getText().toString();

                if (!CheckCode.isMobileNO(phone)) {
                    Utils.toast(getApplicationContext(), "请输入正确的手机号");
                    return;
                }

                if (GeneralTool.isEmpty(phoneCode) || phoneCode.length() != 6) {
                    Utils.toast(getApplicationContext(), "请输入正确的校验码");
                    return;
                }

                if (GeneralTool.isEmpty(phonePsd) || phonePsd.length() < 6
                        || phonePsd.length() > 20) {
                    Utils.toast(getApplicationContext(), R.string.input_psd);
                    return;
                }
// if (!isAgree) {
// Utils.toast(getApplicationContext(), "请同意注册协议");
//
// }

                RequestParams request = UrlTool.getParams(UserParams.EMAIL_PHONE,
                        phone, UserParams.PASSWORD, phonePsd, UserParams.STATUS,
                        UserParams.MOBILE, UserParams.CODE, phoneCode);
                SendActtionTool.get(UserParams.URL_GET_REGISTER, null,
                        UserAction.Action_Regiser_Phone, this, request);
                break;
            // 执行邮箱注册
            case 2:
                String email = edtEmail.getText().toString();
                String emailpsd = edtPsdEmial.getText().toString();
                if (!CheckCode.isEmail(email)) {
                    Utils.toast(getApplicationContext(), "请输入正确的邮箱");
                    return;
                }
                if (GeneralTool.isEmpty(emailpsd) || emailpsd.length() < 6
                        || emailpsd.length() > 20) {
                    Utils.toast(getApplicationContext(), R.string.input_psd);
                    return;
                }
                RequestParams emailReq = UrlTool.getParams(UserParams.EMAIL_PHONE,
                        email, UserParams.PASSWORD, emailpsd, UserParams.STATUS,
                        UserParams.EMAIL, UserParams.CODE, "0");
                SendActtionTool.get(UserParams.URL_REGISTER, null,
                        UserAction.Action_Regiser_Email, this, emailReq);
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(ServiceAction service, Object action, Object value) {
        LogUtils.d("onSuccess", value.toString());
        switch ((UserAction) action) {
            case Action_Regiser_Phone:
            case Action_Regiser_Email:
                Utils.toast(getApplicationContext(), "注册成功");
                JSONObject obj = (JSONObject) value;
                try {
                    String datas = obj.getJSONObject(UserParams.USER_ENTITY)
                            .toString();
                    UserService.getInatance().setUserBean(
                            JsonUtils.parse(datas, UserBean.class));
                } catch (JSONException e1) {
                    e1.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(this, UserCenterActivity.class));
                setResult(RESULT_OK);
                finish();
                break;
            case Action_Regiser_Phone_Code:
                startJishi();
                break;
            default:
                break;
        }

    }

    @Override
    public void onFaile(ServiceAction service, Object action, Object value) {
        super.onFaile(service, action, value);
        LogUtils.d("[RegisterActivity.onFaile]", (String) value);
        Utils.toast(getApplicationContext(), value.toString());

    }

    @Override
    public void onException(ServiceAction service, Object action, Object value) {
        LogUtils.d("[RegisterActivity.onException]", "网络连接异常");
        Utils.toast(getApplicationContext(), "请求异常");

    }

    /**
     * 更新计时器
     */
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            time--;
            if (time <= 0) {
                stopJishi();
            } else {
                tetTime.setText(time
                        + getResources().getString(R.string._regetTime));
            }
        };
    };
    Timer timer;
    private boolean isWaiteInputJiaoYan;
    private int time = 56;

    /**
     * 开始计时
     */
    private void startJishi() {
        if (timer == null) {
            timer = new Timer();
        }
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };
        tetTime.setText(time + getResources().getString(R.string._regetTime));
        isWaiteInputJiaoYan = true;
        timer.schedule(tt, 1000, 1000);
    }

    /**
     * 结束计时
     */
    private void stopJishi() {
        isWaiteInputJiaoYan = false;
        time = 60;
        if (timer != null) {
            timer.cancel();
            timer = null;
            tetTime.setText(R.string._getRegisterCode);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopJishi();
    }

}
