package edu.shily.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @author Shily-zhang
 * @Description
 *  TTL队列 配置文件类代码
 */
@Configuration
public class TtlQueueConfig {
    // 普通交换机的名称
    public static final String X_EXCHANGE = "X";
    // 死信交换机的名称
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    // 普通队列的名称
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    public static final String QUEUE_C = "QC";
    // 死信队列的名称
    public static final String DEAD_LETTER_QUEUE_D = "QD";

    //声明交换机
    @Bean("xExchange")
    public DirectExchange xExchange(){
        return new DirectExchange(X_EXCHANGE);
    }

    @Bean("yExchange")
    public DirectExchange yExchange(){
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    //声明队列
    @Bean("queueA")
    public Queue queueA(){
        HashMap<String, Object> arguments = new HashMap<>();
        // 设置死信队列
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        // 设置死刑RoutingKey
        arguments.put("x-dead-letter-routing-key","YD");
        // 设置TTL  单位是ms
        arguments.put("x-message-ttl",10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }
    @Bean("queueB")
    public Queue queueB(){
        HashMap<String, Object> arguments = new HashMap<>();
        // 设置死信队列
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        // 设置死刑RoutingKey
        arguments.put("x-dead-letter-routing-key","YD");
        // 设置TTL  单位是ms
        arguments.put("x-message-ttl",40000);
        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }
    @Bean("queueC")
    public Queue queueC(){
        HashMap<String, Object> arguments = new HashMap<>();
        // 设置死信队列
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        // 设置死刑RoutingKey
        arguments.put("x-dead-letter-routing-key","YD");
        // // 设置TTL  单位是ms 不在声明队列的时候设置时间，将设置时间放在发送消息的时候
        // arguments.put("x-message-ttl",40000);
        return QueueBuilder.durable(QUEUE_C).withArguments(arguments).build();
    }


    //死信队列
    @Bean("queueD")
    public Queue queueD(){
        return QueueBuilder.durable(DEAD_LETTER_QUEUE_D).build();
    }

    @Bean
    // 绑定
    public Binding queueABingdingX(@Qualifier("queueA") Queue queueA, @Qualifier("xExchange") DirectExchange xExchange){

        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }
    @Bean
    public Binding queueBBingdingX(@Qualifier("queueB") Queue queueB, @Qualifier("xExchange") DirectExchange xExchange){

        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }
    @Bean
    public Binding queueCBingdingX(@Qualifier("queueC") Queue queueC, @Qualifier("xExchange") DirectExchange xExchange){

        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }
    @Bean
    public Binding queueDBingdingY(@Qualifier("queueD") Queue queueD, @Qualifier("yExchange") DirectExchange yExchange){

        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }
}
