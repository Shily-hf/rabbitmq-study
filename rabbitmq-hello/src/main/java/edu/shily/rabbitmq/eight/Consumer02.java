package edu.shily.rabbitmq.eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import edu.shily.rabbitmq.utils.RabbitMqConnectionUtil;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @author Shily-zhang
 * @Description
 */
public class Consumer02 {

    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqConnectionUtil.getChannel();


        //消费消息
        DeliverCallback deliverCallback = (consumerTags,message)->{
            System.out.println("Consumer02接收到的消息是：" + new String(message.getBody(), StandardCharsets.UTF_8));
        };
        channel.basicConsume(DEAD_QUEUE,true,deliverCallback,consumerTag -> {});


    }
}
