package com.zhangjun.quyi.currency_test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rabbitmq.client.ConnectionFactory;
import com.zhangjun.quyi.currency_test.service.MessageService;
import com.zhangjun.quyi.utils.JsonUtil;
import com.zhangjun.quyi.utils.ResultModel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pressure")
public class PressureController {

    /**
     * SpringAMQP帮我们封装好操作RabbitMQ的对象模板
     */

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    MessageService messageService;

    @Autowired
    AmqpAdmin amqpAdmin;

    @PostMapping("/start")
    public ResultModel start(@RequestBody Map<String,Object> requestBody) throws Exception {
        List<String> caseList = (ArrayList<String>)requestBody.get("caseList");
        caseList.stream().forEach(testCase->{

        });
        return ResultModel.ok();
    }

    /**
     * 执行测试任务
     * @throws Exception
     */
    @PostMapping("/runCase")
    public ResultModel run(@RequestBody Map<String,Object> requestBody) throws Exception {
//        System.out.println("requestBody = " + JsonUtil.objectMapper.writeValueAsString(requestBody));
//        messageService.run();
        List<String> caseList = (ArrayList<String>)requestBody.get("caseList");
        for (String testCase : caseList) {
            amqpAdmin.deleteQueue(testCase);
            amqpAdmin.deleteExchange(testCase);
            amqpAdmin.declareExchange(new DirectExchange(testCase));
        }
        for (int i = 0; i < 5; i++) {
            if (i%2==0) rabbitTemplate.convertAndSend("test_1","test_1.key","hahah1"+i);
            else rabbitTemplate.convertAndSend("test_2","test_2.key","hahah"+i);
        }
        return ResultModel.ok();
    }

    /**
     * 消费消息
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("/analytic")
    public ResultModel analysis() {
        QueueInformation test_1 = amqpAdmin.getQueueInfo("test_1");
        QueueInformation test_2 = amqpAdmin.getQueueInfo("test_2");
        int message1Count = test_1.getMessageCount();
        int message2Count = test_2.getMessageCount();
        System.out.println("队列1的待消费消息总数：" + message1Count);
        System.out.println("队列2的待消费消息总数：" + message2Count);


        for (int i = 0; i < message1Count; i++) {
            Message message = rabbitTemplate.receive("test_1");
            System.out.println("队列1的消息"+ i + "：" + new String(message.getBody()));
        }
        for (int j = 0; j < message2Count; j++) {
            Message message2 = rabbitTemplate.receive("test_2");
            System.out.println("队列2的消息"+j+"：" + new String(message2.getBody()));
        }

//        messageService.analysis();
        return ResultModel.ok();
    }


    @GetMapping("/amqp")
    public ResultModel testAmqpAdmin(){

        /**
         * 创建交换机：交换机类型：
         *  注意：交换机可以绑定多个queue，也可以绑定另一个交换机
         *  direct（点对点）：只有提供的routerKey和绑定队列的routerKey完全一致、则才能发送数据到queue中
         *  topic（主题）：direct类型要求routingkey完全相等，这里的routingkey可以有通配符：'*','#'.其中'*'表示匹配一个单词， '#'则表示匹配没有或者多个单词
         *  fanout（）：直接将消息路由到所有绑定的队列中，无须对消息的routingkey进行匹配操作
         *  任何发送到 Fanout Exchange 的消息都会被转发到与该 Exchange 绑定 (Binding) 的所有
         *      1. 这种模式不需要 RouteKey
         *      2. 这种模式需要提前将 Exchange 与 Queue 进行绑定，一个 Exchange 可以绑定多个
         *          Queue ，一个 Queue 可以同多个 Exchange 进行绑定。
         *      3. 如果接受到消息的 Exchange 没有与任何 Queue 绑定，则消息会被抛弃。
         *  header（）
         */
        amqpAdmin.deleteQueue("test_1");
        amqpAdmin.deleteQueue("test_2");
        amqpAdmin.deleteExchange("test_1");
        amqpAdmin.deleteExchange("test_2");


        amqpAdmin.declareExchange(new DirectExchange("test_1"));
        amqpAdmin.declareExchange(new DirectExchange("test_2"));


        /**
         * 创建三个队列
         */
        amqpAdmin.declareQueue(new Queue("test_1"));
        amqpAdmin.declareQueue(new Queue("test_2"));

        /**
         * 将交换机和队列绑定
         */
        amqpAdmin.declareBinding(new Binding("test_1",Binding.DestinationType.QUEUE,"test_1",
                "test_1.key",null));

        amqpAdmin.declareBinding(new Binding("test_2",Binding.DestinationType.QUEUE,"test_2",
                "test_2.key",null));

        QueueInformation queueInfo1 = amqpAdmin.getQueueInfo("test_1");
        QueueInformation queueInfo2 = amqpAdmin.getQueueInfo("test_2");
        System.out.println("test_1队列消费数量为：" + queueInfo1.getMessageCount());
        System.out.println("test_2队列消费数量为：" + queueInfo2.getMessageCount());


        return ResultModel.ok();
    }

}
