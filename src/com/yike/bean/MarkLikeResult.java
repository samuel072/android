package com.yike.bean;

/**
 * Created by wangwei-ps on 15-8-18.
 */
public class MarkLikeResult {
    private long buildTime;
    private String commentId;
    private String content;
    private String id;
    private int isDel;
    private long lastUpdateTime;
    private int resourceId;
    private int status;
    private String toUserId;
    private int type;
    private String userId;

    public void setBuildTime(long buildTime) {
        this.buildTime = buildTime;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getBuildTime() {
        return buildTime;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public int getIsDel() {
        return isDel;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public int getResourceId() {
        return resourceId;
    }

    public int getStatus() {
        return status;
    }

    public String getToUserId() {
        return toUserId;
    }

    public int getType() {
        return type;
    }

    public String getUserId() {
        return userId;
    }
}
