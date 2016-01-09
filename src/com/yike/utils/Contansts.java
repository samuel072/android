package com.yike.utils;

public class Contansts {
    public static final String URL_BASE = "http://api.zhengzai.tv";// 正式服务器地址
    public static final String ALBUM_NAME = "album_name";
    public static final String URL_MP4 = "http://f01.v1.cn/group1/M00/1E/71/ChQBFVUb4AaAbeK-AiTSgVNkyeQ619.mp4";
    public static String URL_AGREEMENT = "http://182.92.80.2/showpages/agreement.html";
    public static String URL_ABOUT = "http://182.92.80.2/showpages/aboutus.html";
    public static String URL_LIVE = "http://r.gslb.lecloud.com/live/hls/20150422300000916/desc.m3u8";
    public static String URL_SEARCH_KEYWORDS = URL_BASE + "/search/bykeywords";
    public static String URL_SEARCH_FILTER = URL_BASE + "/search/bykeywords";
    public static final String URL_HOMEPAGE = URL_BASE + "/info/foucs/homePage";
    public static String URL_FOCUSLIST = URL_BASE + "/info/foucs/focusList";
    public static String URL_ALL_VIDEO_LIST = URL_BASE + "/info/video/queryAllVideoList";
    public static String URL_ALBUM_DETAIL = URL_BASE + "/info/album/albumDetail";
    public static String URL_VIDEO_DETAIL = URL_BASE + "/info/video/videoDetail";
    public static String URL_VIDEO_NEXT_PLAY_INFO = URL_BASE + "/info/video/nextPlayInfo";
    public static String URL_GET_VIDEO_AD = URL_BASE + "/info/ad/getAd?type=2";// 视频暂停广告
    public static String URL_GET_START_AD = URL_BASE + "/info/ad/getAd?type=1";// app启动广告
    public static String URL_GET_TICKET_LIST = URL_BASE + "/info/ticket/ticketList";

    public static final String ALBUM_ID = "albumId";
    public static String VIDEO_ID = "videoId";
    public static String VIDEO_NAME = "videoName";
    public static String VIDEO_PIC = "videoPic";
    public static String SOURCE = "source";
    public static String MOBILE = "mobile";
    public static String PLAY_TIME = "playTime";
    public static String LIVE = "LIVE";
    public static String FILTER = "filter";
    public static String FILTER_ALBUM = "latestVideo,name,videoPlayInfo,videoId,videoUrl,standardPic";
    public static String FILTER_VIDEO = "name,data,videoPlayInfo,videoId,videoUrl,standardPic,vedioType,liveInfo,type,msg,rtmp,hls,isPay,isNeedPay";
    public static String FILTER_ALBUM_VIDEO = "videoId,name,videoPlayInfo,videoUrl,standardPicl,vedioType";

    public static final String PINGLUN_TEXT = "pingun_text";
    public static final String PINGLUN_IMG = "pingun_img";

    public static final String MESSAGE = "message";
    public static final String STATUS = "status";
    public static final String USER_ENTITY = "userEntity";
    public static final String DATA = "data";
    public static final String TYPE = "type";
    public static final String PAGE = "page";
    public static final String SIZE = "size";
    public static final String INFO = "info";
    public static final String USER_ID = "userId";
    public static final String TO_USER_ID = "toUserId";

    public static final String AlbumName = "albumName";
    public static final String BUILD_TIME = "buildTime";

    public static final String RESOURCE_ID = "resourceId";
    public static final String COMMENT_ID = "commentId";
    public static final String CONTENT = "content";
    /** 首页 */
    public static final String TYPE_FIRSTPAGE = "1";
    /** 音乐节 */
    public static final String TYPE_EXPLORE = "2";
    /** 演唱会 */
    public static final String TYPE_YanChangHui = "3";
    /** live House */
    public static final String TYPE_LIVE_HOUSE = "4";
    /** mtv */
    public static final String TYPE_MTV = "5";
    /** 直播 */
    public static final String TYPE_ZhiBo = "100";

    public static final String BODY = "body";// 商品描述
    public static final String TOTAL_FEE = "totalFee";// 总金额
    public static final String CLIENT_IP = "clientIp";// ip
    public static final String TRADE_TYPE = "tradeType";// 客户端类型
    public static final String APP = "APP";
    public static final String VOUCHER_CODE = "voucherCode";// 优惠码
    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String ACTION_PAY_RESULT = "ACTION_PAY_RESULT";// 支付结果
    public static final String OUT_TRADE_NO = "outTradeNo";
    public static final String TRADE_STATE = "tradeState";

    public static final String FAV_DATA_CHANGED_RECEIVER = "fav_data_changed";

    /**
     * @author rendy 用户中心 信息参数
     */
    public class UserParams extends Contansts {

        public static final String Yike_Base = "http://123.56.139.81:8080";// 正式接口
        public static final String URL_GET_REGISTER_CODE = Yike_Base + "/m/user/code.jsn";// 获取验证码接口
        public static final String URL_GET_REGISTER = Yike_Base + "/m/user/register.jsn";// 注册
        public static final String URL_GET_LOGIN = Yike_Base + "/m/user/login.jsn";// 登录

        /** 注册接口 **/
        public static final String Base = "http://115.29.51.23";// 正式接口
        public static final String URL_REGISTER = Base + "/m/user/register";
        /** 用户的头像地址 **/
        public static final String USER_URL = "http://pic-yikelive.oss-cn-beijing.aliyuncs.com/";
        public static final String URL_REGISTER_CODE = Base + "/m/user/code";
        public static final String URL_REGISTER_LOGIN = Base + "/m/user/login";
        public static final String URL_USER_UPDATE = Yike_Base + "/m/user/update.jsn";
        /** 检测版本更新 **/
        public static final String URL_Check_VERSION = Yike_Base
                + "/api/check_version.jsn";
        // 通过手机获取验证码
        public static final String URL_USER_UPDATE_Phone_Psd = Yike_Base
                + "/m/user/update_password_mobile.jsn";

        // 通过邮箱获取验证码
        public static final String URL_USER_UPDATE_EMAIL_Psd = Base
                + "/m/user/update_password_email";
        public static final String URL_REGISTER_LOGOUT = Yike_Base
                + "/m/user/logout.jsn";
        public static final String URL_ADD_PUSH_TOKEN = Base
                + "/m/user/add/token";// 添加token
        public static final String URL_COMMENT_LIST = Base + "/m/comment/list";// 评论列表
        public static final String URL_COMMENT_ONE = Base + "/m/comment/one";// 评论回复列表
        public static final String URL_ADD_COMMENT = Base
                + "/m/comment/add_comment";// 添加评论
        public static final String URL_ADD_PRAISE_COMMENT = Base
                + "/m/comment/add_praise/comment";// 对评论点赞
        public static final String URL_ADD_PRAISE_RESOUCE = Base
                + "/m/comment/add_praise/resource";// 对资源点赞
        public static final String URL_ADD_REPLY = Base
                + "/m/comment/add_reply";// 添加回复
        public static final String URL_ADD_PLAY_RECORD = Base
                + "/m/play_record/add";// 添加播放记录

        public static final String URL_GET_WEIXIN_ORDER = Base
                + "/api/wxpay/prepay_id";// 获取微信支付参数
        public static final String URL_WEIXIN_BACK = Base + "/api/wxpay/back";// 获取微信支付结果回调
        /** 检测 第三方的绑定信息 **/
        public static final String URL_GET_ONE = Base + "/m/user/one";

        public static final String URL_YUYUE_ADD = Yike_Base + "/m/reservation/add.jsn";
        public static final String URL_YUYUE_DEL = Yike_Base + "/m/reservation/del.jsn";
        // 预约列表
        public static final String URL_YUYUE_LIST = Yike_Base
                + "/m/reservation/list.jsn";
        // 观看记录
        public static final String URL_PLAY_RECORD = Base
                + "/m/play_record/list";
        // 删除播放记录
        public static final String URL_PLAY_RECORD_DEL = Base
                + "/m/play_record/del";
        // 收藏的专辑
        public static final String URL_COLLECT_LIST_ALBUM = Base
                + "/m/collection/album_list";
        // 收藏的视屏列表
        public static final String URL_COLLECT_LIST_VIDO = Base
                + "/m/collection/video_list";
        // 添加收藏
        public static final String URL_COLLECT_ADD = Base + "/m/collection/add";
        // 删除收藏的专辑
        public static final String URL_Dell_Collect_Album = Base
                + "/m/collection/del_album";
        // 删除收藏的视屏
        public static final String URL_Dell_Collect_VIDEO = Base
                + "/m/collection/del_video";
        // 第三方登陆
        public static final String URL_OPEN_LOGIN = Base + "/m/user/login/open";
        // 获取优惠券信息
        public static final String URL_YOUHUIQUAN = Base + "/m/voucher/list";
        // 使用优惠券
        public static final String URL_USE_YOUHUIQUAN = Base + "/m/voucher/use";
        public static final String URL_OPEN_Check = Base + "/m/user/check_open";
        public static final String URL_USER_BINDING = Base + "/m/user/binding";

        public static final String EMAIL_PHONE = "emailOrMobile";
        public static final String MOBILE = "mobile";
        public static final String PASSWORD = "password";
        public static final String CODE = "code";
        public static final String EMAIL = "email";
        public static final String USERNAME = "username";
        public static final String FACE_URL = "face_url";
        public static final String KEY = "key";
        public static final String VALUE = "value";

        public static final String openId = "openId";
        public static final String openName = "openName";
        public static final String faceUrl = "faceUrl";
        public static final String sex = "sex";
        public static final String location = "location";
        public static final String source = "source";

        public static final String URL_FAVORITES = Yike_Base + "/m/collection/video_list.jsn";

        public static final String DEFAULT_FAV_PAGE = "1";
        public static final String DEFAULT_FAV_PAGE_SIZE = "10";

        public static final String URL_DELETE_FAVORITES = Yike_Base + "/m/collection/delete.jsn";

        public static final String URL_QUERY_FAV_STATUS = Yike_Base + "/m/collection/user_video.jsn";
        public static final String URL_ADD_TO_FAV = Yike_Base + "/m/collection/add.jsn";

        public static final String URL_MARK_LIKE = Yike_Base + "/m/comment/add_praise/resource.jsn";
    }

    /**
     * 内容数据相关参数
     */
    public class ContentParams extends Contansts {
        protected static final String BASE_URL = "http://182.92.75.125";

        /**
         * 主页(不含banner)
         */
        public static final String HOME_PAGE_URL = BASE_URL + "/info/foucs/homePage";

        /**
         * 主页广告banner
         */
        public static final String HOME_BANNER_URL = BASE_URL + "/info/ad/getAd";

        /**
         * 直播
         */
        public static final String LIVE_PAGE_URL = BASE_URL + "/info/foucs/homePage";

        /**
         * 搜索页，猜你喜欢
         */
        public static final String SEARCH_GUESS_LIKE_URL = BASE_URL + "/info/foucs/homePage";
        public static final String HOT_WORDS_URL = BASE_URL + "/search/hotkeywords";
        public static final String HOT_WORDS_DEFAULT_SIZE = "10";
        // 搜索页 猜你喜欢请求类型
        public static final String CONTENT_TYPE_GUESS_LIKE = "2";
        // 探索页面
        public static final String URL_BASE_YIKE_EXPLORE = BASE_URL + "/info/exploration/index";
        // 排行
        public static final String URL_RANK = BASE_URL + "/info/rank/getList";
        // 专题
        public static final String URL_TOPIC = BASE_URL + "/info/collect/getList";
    }

    public class SearchParams extends Contansts {
        protected static final String BASE_URL = "http://182.92.75.125";
        public static final String SEARCH_ENGINE_URL = BASE_URL + "/search/bykeywords";
    }

    public static class ResultConst {
        // 拍照图片返回
        public static final int RESULT_TAKE_PHOTO = 0X2;
        // 本地文件浏览
        public static final int RESULT_SHARE_LOCAL_PHOTO = 0X4;
        // 剪裁之后回调
        public static final int RESULT_SCALE_PIC = 0x3;

        public static final String USER_PHOTO_ICON = "hi_logo.jpg";

        public static final int REQUEST_CODE_REGISTER_SUCCESS = 100;

        public static final int REQUEST_CODE_FAV_REQUEST_LOGIN = 101;

    }

    public class AppFile {
        public static final String USER_INFO = "zhengzaitv";
    }

    public class FileKey {
        public static final String USER_BEAN = "zhengzaitv";

    }
}
