package com.zhangjun.quyi.constans;

/**
 * 接口常量
 */
public interface PressureConstant {

    public static final String DOUBLE_STR = "%.2f";
    public static final String WRITER_PATH = "D:/pressure-result.json";
    public static final String REQUEST_TYPE_POST = "POST";
    public static final String REQUEST_TYPE_GET = "GET";
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String REQUEST_PARAMS_TYPE_APPLICATION_JSON = "application/json";
    public static final String HEADER_USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36";

    public static final boolean ASSERT_TRUE = true;
    public static final boolean ASSERT_FALSE = false;

    public static final String ZERO_STR = "0";

    /**
     * B2预发布
     */
    public static final String AAJOGO_PRE_CLIENT_URL = "https://aajogo-api.pre-release.xyz";
    public static final String AAJOGO_PRE_ADMIN_URL = "https://aajogo-admin.pre-release.xyz";

    /**
     * P1预发布
     */
    public static final String PHLWIN_PRE_CLIENT_URL = "https://phlwin-api.pre-release.xyz";
    public static final String PHLWIN_PRE_ADMIN_URL = "https://philucky-api.office.coinmoney.xyz";


    /**
     * P2办公网
     */
    public static final String PHILUCKY_TEST_CLIENT_URL = "https://philucky-api.office.coinmoney.xyz";
    public static final String PHILUCKY_TEST_ADMIN_URL = "https://philucky-admin.office.coinmoney.xyz";

    /**
     * P2预发布
     */
    public static final String PHILUCKY_PRE_CLIENT_URL = "https://philucky-api.pre-release.xyz";
    public static final String PHILUCKY_PRE_ADMIN_URL = "https://philucky-admin.pre-release.xyz";


    /**
     * P3办公网
     */
    public static final String APANALO_TEST_CLIENT_URL = "https://apanalo-api.office.coinmoney.xyz";
    public static final String APANALO_TEST_ADMIN_URL = "https://apanalo-admin.office.coinmoney.xyz";

    /**
     * P3办公网
     */
    public static final String MEXLUCKY_RPE_CLIENT_URL = "https://mexlucky-api.pre-release.xyz";

    /**
     * 基础url
     */
    public static final String BASE_CLIENT_URL = PHILUCKY_PRE_CLIENT_URL;
    public static final String BASE_ADMIN_URL = PHILUCKY_PRE_ADMIN_URL;


}
