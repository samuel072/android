package com.yike.bean;

import java.util.List;

/**
 * @author QIFA 探索 model
 */
public class ExploreInfo {
    private TalkerBean talker;
    private String talkerListUrl;
    private List<ExploreInfoBean> info;
    public TalkerBean getTalker() {
        return talker;
    }
    public void setTalker(TalkerBean talker) {
        this.talker = talker;
    }
    public String getTalkerListUrl() {
        return talkerListUrl;
    }
    public void setTalkerListUrl(String talkerListUrl) {
        this.talkerListUrl = talkerListUrl;
    }
    public List<ExploreInfoBean> getInfo() {
        return info;
    }
    public void setInfo(List<ExploreInfoBean> info) {
        this.info = info;
    }

}
