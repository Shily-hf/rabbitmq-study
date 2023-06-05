package edu.shily.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Shily-zhang
 * @Description 配置类，发布确认（高级），若消息未被消费则进行回调，两种方法
 *              1.直接回调给生产者，交给生产者去处理
 *              2.将未被消费的消息传递给另外一个备份交换机去处理
 */
@Configuration
public class ConfirmConfig {

    // 交换机
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    // 队列
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";
    // RoutingKey
    public static final String CONFIRM_ROUTING_KEY = "key1";

    //声明交换机
    @Bean("confirmExchange")
    public DirectExchange confirmExchange(){

        return new DirectExchange(CONFIRM_EXCHANGE_NAME);
    }

    //声明队列
    @Bean("confirmQueue")
    public Queue confirmQueue(){

        return new Queue(CONFIRM_QUEUE_NAME);
    }

    //绑定交换机
    @Bean
    public Binding queueBindingExchange(@Qualifier("confirmExchange") DirectExchange confirmExchange,@Qualifier("confirmQueue") Queue confirmQueue){
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
    }
}
