package com.zhangjun.quyi.currency_test.service;

import com.rabbitmq.client.Channel;
import com.zhangjun.quyi.currency_test.entity.ApiResultEntity;
import com.zhangjun.quyi.currency_test.performance.testcase.impl.ProxyCase;
import com.zhangjun.quyi.service_base.handler.entity.ExceptionEntity;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MessageService {

    Logger logger = Logger.getLogger(MessageService.class);

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    MessageConverter messageConverter;



    // 结果消息列表
    private ArrayList<ApiResultEntity> messageList = new ArrayList<>();


    /**
     * 执行性能测试工作
     * @throws Exception
     */
    public void run() throws Exception {
        ProxyCase proxyCase = new ProxyCase(2,"https://philucky-api.office.coinmoney.xyz",null);
        try {
            proxyCase.inviteUser();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            proxyCase.close();
        }
    }

    /**
     * 从任务队列消费消息、再添加到结果列表中
     * @param
     */
    @RabbitListener(queues = "test_1")
    public void addMessagesList(ApiResultEntity apiResultEntity, Channel channel){
        messageList.add(apiResultEntity);
    }


    /**
     * 分析结果
     */
    public void analysis() {
        logger.info("待处理消息总量：" + messageList.size());
        if (messageList.size() == 0) throw new ExceptionEntity(20001,"消息队列中没有拿到处理结果");
        for (ApiResultEntity apiResultEntity : messageList) {
            String apiName = apiResultEntity.getName();
            logger.info(apiName);
        }
        this.messageList.clear();
    }
}
