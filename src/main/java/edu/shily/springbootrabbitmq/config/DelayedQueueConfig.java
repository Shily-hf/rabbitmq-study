package edu.shily.springbootrabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author Shily-zhang
 * @Description
 */
// @Configuration
public class DelayedQueueConfig {

    //交换机
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    //队列
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    // routingKey
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    //声明交换机
    @Bean
    public CustomExchange delayedExchange(){
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type","direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME,"x-delayed-message",true,false,arguments);
    }
    //声明队列
    @Bean
    public Queue delayedQueue(){
        return new Queue(DELAYED_QUEUE_NAME);
    }
    //绑定
    public Binding delayedQueueBindingDelayedExchange(@Qualifier("delayedQueue")Queue delayQueue,@Qualifier("delayedExchange") CustomExchange delayedExchange){
        return BindingBuilder.bind(delayQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }

}
