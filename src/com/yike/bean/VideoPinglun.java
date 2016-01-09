/**
 * 
 */
package com.yike.bean;

import java.util.List;

/**
 * @author： fengqingyun2008
 * @Email： fengqingyun2008@gmail.com
 * @version：1.0
 * @创建时间：2015-4-15 下午2:36:57
 * @类说明：
 */
public class VideoPinglun extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4173092555991907463L;
	private String id;
	private List<Content> content;// 评论内容
	private UserEntity userEntity;// 评论用户
	private UserEntity toUserEntity;// 被评论用户
	private long praiseCount;// 赞的数量
	private long commentCount;// 评论数量
	private int isPraise;// 是否赞 1是0否
	private int status;// 1艺人2普通人
	private List<Huifu> comments;// 评论下的回复
	private List<Prais> praises;// 赞的集合
	private long buildTime;// 创建时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Content> getContent() {
		return content;
	}

	public void setContent(List<Content> content) {
		this.content = content;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public UserEntity getToUserEntity() {
		return toUserEntity;
	}

	public void setToUserEntity(UserEntity toUserEntity) {
		this.toUserEntity = toUserEntity;
	}

	public long getPraiseCount() {
		return praiseCount;
	}

	public void setPraiseCount(long praiseCount) {
		this.praiseCount = praiseCount;
	}

	public long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(long commentCount) {
		this.commentCount = commentCount;
	}

	public int getIsPraise() {
		return isPraise;
	}

	public void setIsPraise(int isPraise) {
		this.isPraise = isPraise;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getBuildTime() {
		return buildTime;
	}

	public void setBuildTime(long buildTime) {
		this.buildTime = buildTime;
	}

	public List<Prais> getPraises() {
		return praises;
	}

	public void setPraises(List<Prais> praises) {
		this.praises = praises;
	}

	public List<Huifu> getComments() {
		return comments;
	}

	public void setComments(List<Huifu> comments) {
		this.comments = comments;
	}

}
