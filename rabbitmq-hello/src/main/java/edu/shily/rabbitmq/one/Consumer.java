package edu.shily.rabbitmq.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Shily-zhang
 * @Description 消费者：接受消息
 */
public class Consumer {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.231.128");
        factory.setUsername("shily");
        factory.setPassword("zhang");
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        // 声明 接收消息
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println(new String(message.getBody()));
        };
        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println("消息消费被中断");
        };

        /**
         * @Description 消费者消费消息
         * @Param
         * 1.queue:消费哪个队列
         * 2.autoAck:消费成功之后是否需要自动应答。true代表自动应答
         * 3.deliverCallback:消费者为成功消费的回调
         * 4.CancelCallback:取消消息时的回调
         */
        channel.basicConsume(QUEUE_NAME,false,deliverCallback,cancelCallback);

    }
}
