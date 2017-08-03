package com.qg.qgnews.model;

/**
 * Created by 小吉哥哥 on 2017/7/29.
 */

public class RequestAdress {
    public static  String HOST = "http://119.23.106.145:80/news_app/";

    /**
     * 上传新闻url
     */
    public static final String UPLOAD_NEWS = HOST + "admin/addnews";
    /**
     * 删除新闻
     */
    public static final String DELETE_NEWS = HOST + "admin/deletenews";
    /**
     * 注册信息提交
     */
    public static final String REGISTER = HOST + "admin/addaccount";
    /**
     * 注册邮箱认证
     */
    public static final String EMAIL_ENSRUE = HOST + "admin/verifyaccount";
    /**
     * 登陆
     */
    public static final String LOGIN = HOST + "admin/login";
    /**
     * 请求验证码
     */
    public static final String SEND_VERIFY_CODE = HOST + "admin/sendverifycode";
    /**
     * 设置新密码
     */
    public static final String SET_NEW_PASSWORD = HOST + "admin/setnewpassword";
    /**
     * 请求新闻
     */
    public static final String REQUEST_NEWS = HOST + "reader/flashnews";
    /**
     * 搜索新聞
     */
    public static final String SEARCH_NEWS = HOST + "reader/searchnews";
    /**
     * 超级管理员添加管理员
     */
    public static final String ADD_ACCOUNT = HOST + "admin/addaccount";
    /**
     * 删除管理员
     */
    public static final String DELETE_ACCOUNT = HOST + "admin/deleteaccount";
    /**
     * 统计所有附件下载次数
     */
    public static final String GET_ALL_FILES_DOWNLOAD_COUNT = HOST + "admin/downloadtime";
    /**
     * 得到所有管理员
     */
    public static final String GET_ALL_MANAGER = HOST + "admin/showmanager";
    /**
     * 得到所有等待审批管理员
     */
    public static final String GET_ALL_MANAGER_NOT_PASS = HOST + "admin/showmanagerapproval";
    /**
     * 拒绝审批
     */
    public static final String REFUSE_PASS = HOST + "admin/approvalrefuse";
    /**
     * 查看自己的新闻情况
     */
    public static final String SHOW_OWN_NEWS = HOST + "admin/showownnews";
    /**
     * 退出登陆
     */
    public static final String LOGOUT = HOST + "admin/logout";

    /**
     *
     */
    public static final String AGRESS_APPLY = HOST + "admin/managerapproval";

    /**
     *
     */
    public static final String DISAGRESS_APPLY = HOST + "admin/approvalrefuse";

    /**
     *
     */
    public static final String RESTRICTACCOUNT = HOST + "admin/restrictaccount";

    /**
     *
     */
    public static final String SHOWOWNNEWS = HOST + "admin/showownnews";

    /**
     *
     */
    public static final String ADDMANAGER = HOST + "admin/addmanager";

    /**
     *
     */
    public static final String GET_NEWS_DETIAL = HOST + "reader/newsdetail";

    /**
     *
     */
    public static final String GET_NEWS_BEHIDE = HOST + "reader/read";
    /**
     *
     */
    public static final String GET_VIEC_FILE_DOWNLOAD_DETAIL = HOST + "admin/downloaddetail";
    /**
     *
     */
    public static final String POST_NEWS_DOWNLOAD_DETAIL = HOST + "reader/downloadfile";
}
