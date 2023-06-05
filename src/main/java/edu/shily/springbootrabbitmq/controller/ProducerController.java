package edu.shily.springbootrabbitmq.controller;

import edu.shily.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Shily-zhang
 * @Description 发送消息
 */
@Slf4j
@RestController
@RequestMapping("/confirm")
public class ProducerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    //发消息
    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message){

        CorrelationData correlationData1 = new CorrelationData("1");

        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,ConfirmConfig.CONFIRM_ROUTING_KEY,message,correlationData1);

        log.info("发送消息：{}",message);

        CorrelationData correlationData2 = new CorrelationData("2");
        //交换机为找打的情况，交换机宕机等其它原因
        // rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME+"123",ConfirmConfig.CONFIRM_ROUTING_KEY,message,correlationData2);
        //队列为找到的情况，队列宕机等其它原因
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,ConfirmConfig.CONFIRM_ROUTING_KEY+"2",message,correlationData2);

        log.info("发送消息：{}",message);
    }

}
