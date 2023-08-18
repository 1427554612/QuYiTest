package com.zhangjun.quyi.api_auto_test.api_core.components.asserts;


import com.zhangjun.quyi.api_auto_test.api_core.entity.ApiAssertEntity;
import okhttp3.Headers;
import okhttp3.Response;

import java.io.IOException;

/**
 * 断言接口
 */
public interface AssertCase {

    /**
     * 判断相等
     * @param apiAssertEntity
     * @return
     */
    boolean eq(ApiAssertEntity apiAssertEntity, Integer code, Headers responseHeaders,String responseBody) throws IOException;

    /**
     * 判断不相等
     * @param apiAssertEntity
     * @return
     */
    boolean notEq(ApiAssertEntity apiAssertEntity, Integer code, Headers responseHeaders,String responseBody) throws IOException;

    /**
     * 判断包含
     * @param apiAssertEntity
     * @return
     */
    boolean contains(ApiAssertEntity apiAssertEntity, Integer code, Headers responseHeaders,String responseBody);

    /**
     * 判断不包含
     * @param apiAssertEntity
     * @return
     */
    boolean notContains(ApiAssertEntity apiAssertEntity, Integer code, Headers responseHeaders,String responseBody);

    /**
     * 判断是空
     * @param apiAssertEntity
     * @param code
     * @param responseHeaders
     * @param responseBody
     * @return
     */
    boolean isNull(ApiAssertEntity apiAssertEntity, Integer code, Headers responseHeaders,String responseBody);

    /**
     * 判断不是空
     * @param apiAssertEntity
     * @param code
     * @param responseHeaders
     * @param responseBody
     * @return
     */
    boolean notIsNull(ApiAssertEntity apiAssertEntity, Integer code, Headers responseHeaders,String responseBody);

    /**
     * 判断长度
     * @param apiAssertEntity
     * @param code
     * @param responseHeaders
     * @param responseBody
     * @return
     */
    boolean lengthEq(ApiAssertEntity apiAssertEntity, Integer code, Headers responseHeaders,String responseBody);

}
