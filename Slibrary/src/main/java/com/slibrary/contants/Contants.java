package com.slibrary.contants;


public final class Contants {

    /**
     * 后台数据返回无误的code值
     */
    public static final int REQUEST_OK = 200;

    /**
     * 用户文件夹
     */
    public static final String USER_PATH = "USER/user";

    /**
     * 用户文件夹（私有）
     */
    public static final String USER_PATH_PRIVATE = USER_PATH + "/.private";

    /**
     * 用户文件夹（公共）
     */
    public static final String USER_PATH_PUBLIC = USER_PATH + "/public";

    /**
     * 缓存文件夹目录
     */
    public static final String CACHE_PATH = "cache";

    /**
     * 缓存数据目录
     */
    public static final String GET_CACHE_PATH = CACHE_PATH + "/data";

    /**
     * 图片目录
     */
    public static final String IMG_CACHE = CACHE_PATH + "/image";

    /**
     * ImageLoader缓存目录
     */
    public static final String IMAGE_CACHE_PATH = Contants.CACHE_PATH + "/imageLoader-images";

    /**
     * 默认下载目录
     */
    public static final String DOWNLOAD_PATH = "download";

    /**
     * 用户信息文件
     */
    public static final String USER_INFO = "userInfo";

    /**
     * 用户账号的KEY
     */
    public static final String UN = "userName";

    /**
     * 用户密码的KEY
     */
    public static final String UPWD = "userPWD";

    /**
     * 用户账号与密码文件名
     */
    public static final String USER_FILE = "login";

    /**
     * 服务器配置文件名
     */
    public static final String CONFIGURE = "configure";

    /**
     * Post
     */
    /**
     * 修改资料
     */
    public static final String UPDATE_USER_INFO = "CAPool/UpdClienter.aspx?token=";

    /**
     * 第三方注册接口
     */
    public static final String THIRD_PARTY_REGISTER = "CAPool/ClientLogin.aspx";

    /**
     * Get
     */
    /**
     * 总配置文件接口
     */
    public static final String STATIC_DATA = "xml/version.xml";
    /**
     * 根据token获取用户资料
     */
    public static final String GET_USER_INFO = "AMAPI/SimplePlanCmd.aspx?cmd=GetUserInf&token=%s";


    /**
     * 心跳包
     */
    public static final String KEEP_HEART = "CAPool/KeepHeart.aspx";

}
