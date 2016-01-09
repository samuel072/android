package com.yike.service;

import java.util.List;

import com.yike.bean.AppointmentBean;

public class AppointmentService {
    private static AppointmentService LOGIC;

   private List<AppointmentBean> data;

    private AppointmentService() {
    }

    public static AppointmentService getInstance() {
        return LOGIC = LOGIC == null ? new AppointmentService() : LOGIC;
    }


    public static AppointmentService getLOGIC() {
        return LOGIC;
    }

    public static void setLOGIC(AppointmentService lOGIC) {
        LOGIC = lOGIC;
    }

    public List<AppointmentBean> getData() {
        return data;
    }

    public void setData(List<AppointmentBean> data) {
        this.data = data;
    }

}
