package com.yike;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;

import com.alibaba.fastjson.JSON;
import com.yike.action.ServiceAction;
import com.yike.adapter.AppointmentAdapter;
import com.yike.bean.AppointmentBean;
import com.yike.listener.CommonListener;
import com.yike.service.AppointmentService;
import com.yike.tool.SendActtionTool;
import com.yike.utils.Contansts;
import com.yike.view.NoScroListView;

public class AppointmentListActivity extends BaseActivity implements CommonListener {

    private NoScroListView mListView;
    private AppointmentAdapter adapter;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_appointment_list);
        getUrlData();
    }

    @Override
    public void findViewById() {
        findViewById(R.id.btn_return).setOnClickListener(this);

        mListView = (NoScroListView) findViewById(R.id.listview);
    }

    private void updateData() {

        List<AppointmentBean> datas = AppointmentService.getInstance().getData();
        if (datas == null) {
            return;
        }
        if (adapter == null) {
            adapter = new AppointmentAdapter(datas, getLayoutInflater(), this);
            mListView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取网络数据
     */
    private void getUrlData() {
        SendActtionTool.get(Contansts.UserParams.URL_YUYUE_LIST, ServiceAction.Action_Appointment_List, null,
                this);
    }

    @Override
    public void onSuccess(ServiceAction service, Object action, Object value) {
        JSONObject obj = (JSONObject) value;
        switch (service) {
        // 当前页面的数据
            case Action_Appointment_List:
                try {
                    JSONArray array = obj.getJSONArray(Contansts.DATA);
                    List<AppointmentBean> datas = JSON.parseArray(array.toString(), AppointmentBean.class);
                    AppointmentService.getInstance().setData(datas);
                    updateData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onFaile(ServiceAction service, Object action, Object value) {
    }

    @Override
    public void onFinish(ServiceAction service, Object action) {
        dismissDialog();
    }

    @Override
    public void onClick(View v) {
        finish();
    }

}
