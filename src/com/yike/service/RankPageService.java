package com.yike.service;

import java.util.List;

import com.yike.bean.RankBean;

public class RankPageService {

    private static RankPageService LOGIC;

    private List<RankBean> today;
    private List<RankBean> month;

    public static RankPageService getInstance() {
        return LOGIC = LOGIC == null ? new RankPageService() : LOGIC;
    }

    public List<RankBean> getToday() {
        return today;
    }

    public void setToday(List<RankBean> today) {
        this.today = today;
    }

    public List<RankBean> getMonth() {
        return month;
    }

    public void setMonth(List<RankBean> month) {
        this.month = month;
    }

    public boolean isHaveDate() {
        return today != null || month != null;
    }
}
