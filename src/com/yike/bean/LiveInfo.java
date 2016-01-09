/**
 * 
 */
package com.yike.bean;

/**
 * @author： fengqingyun2008
 * @Email： fengqingyun2008@gmail.com
 * @version：1.0
 * @创建时间：2015-4-27 下午4:01:17
 * @类说明：
 */
public class LiveInfo extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1163748964746199934L;
	private int videoId;
	private String type;
	private String msg;
	private String rtmp;
	private String hls;

	public int getVideoId() {
		return videoId;
	}

	public void setVideoId(int videoId) {
		this.videoId = videoId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getRtmp() {
		return rtmp;
	}

	public void setRtmp(String rtmp) {
		this.rtmp = rtmp;
	}

	public String getHls() {
		return hls;
	}

	public void setHls(String hls) {
		this.hls = hls;
	}

}
