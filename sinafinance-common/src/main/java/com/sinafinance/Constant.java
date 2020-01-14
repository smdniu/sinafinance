package com.sinafinance;

/**
 * Created by zhanglinhai01 on 2018/12/14.
 */
public class Constant {

    /**
     * 数据库记录有效/无效
     */
    public static final Integer VALID = 1;
    public static final Integer NOT_VALID = 0;

    /**
     * 是否修改
     */
    public static final Integer UPDATE_YES = 1;
    public static final Integer UPDATE_NO = -1;

    /**
     * 是否应用
     */
    public static final Integer ENABLE = 1;
    public static final Integer UNABLE = 0;

    /**
     * 是否删除
     */
    public static final Integer DELETE = 1;
    public static final Integer UN_DELETE = 0;

    /**
     * key value用于数据库查询返回
     */
    public static final String KEY = "KEY_";
    public static final String VALUE = "VALUE_";

    /**
     * 逃生模式是否开启
     */
    public static final String ESCAPED_ENABLE = "ENABLE";
    public static final String ESCAPED_UNABLE = "UNABLE";

    public static final String USER_SESSION = "USER_NAME";
    public static final String ESCAPE_URL = "escape";
    public static final String LOGIN_URL = "login";
    public static final String LOGOUT_URL = "logout";

    /**
     * 基金投资资产（基金类资产）港股
     */
    public static final String HK_STOCK = "港股";

    /**
     * 吸引子基金后缀
     */
    public static final String FUND_CODE_OF_SUFFIX = ".OF";
    public static final String FUND_CODE_SH_SUFFIX = ".SH";
    public static final String FUND_CODE_SZ_SUFFIX = ".SZ";
    public static final String FUND_CODE_IB_SUFFIX = ".IB";

    /**
     * 分页
     */
    public static final Integer PAGE_START_NO = 1;
    public static final Integer PAGE_SIZE = 1000;

    /**
     * 吸引子全量基金接口 版本号 version
     * version=1 返回数据不带secName
     * version=2 返回数据带secName
     */
    public static final Integer FUNDSTYLE_VERSION = 2;
}
