package com.zhangjun.quyi.currency_test.performance.testcase.impl;

import com.zhangjun.quyi.currency_test.performance.testcase.BaseCase;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * 测试用例
 */


public class GPWalletCase extends BaseCase {

    private RabbitTemplate rabbitTemplate;

    public GPWalletCase(Integer requestNumber,String baseUrl,RabbitTemplate rabbitTemplate) {
        super(requestNumber,baseUrl,GPWalletCase.class);
        this.rabbitTemplate=rabbitTemplate;
    }


    /**
     * 测试多用户转账
     */
    public GPWalletCase testMultiUserTransfer() throws Exception {
        return null;
    }

}
