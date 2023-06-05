package edu.shily.springbootrabbitmq.controller;

import edu.shily.springbootrabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author Shily-zhang
 * @Description
 *  发送延迟消息
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message){
        log.info("当前时间：{}，发送一条消息给两个TTL队列：{}",new Date().toString(),message);

        rabbitTemplate.convertAndSend("X","XA","消息来自10s的队列：" + message);
        rabbitTemplate.convertAndSend("X","XB","消息来自40s的队列：" + message);
    }

    // 对延迟队列进行优化，在发送消息时设置过期时间，存在bug，消息与消息之间存在排队，优先级无法体现
    @GetMapping("/sendMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable String message,@PathVariable String ttlTime){
        log.info("当前时间：{}，发送一条时长{}毫秒TTL信息给队列QC:{}",new Date().toString(),ttlTime,message);

        rabbitTemplate.convertAndSend("X","XC",message,msg ->{
            // 发送消息的时候，延迟时长
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
    }

    @GetMapping("/sendDelayMsg/{message}/{delayTime}")
    public void sendDelayMsg(@PathVariable String message,@PathVariable Integer delayTime){
        log.info("当前时间：{}，发送一条时长{}毫秒给延迟队列delayed.queue:{}",new Date().toString(),delayTime,message);

        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME,DelayedQueueConfig.DELAYED_ROUTING_KEY,message, msg ->{
            // 发送消息的时候，延迟时长
            msg.getMessageProperties().setDelay(delayTime);
            return msg;
        });
    }
}
