package edu.shily.rabbitmq.five;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import edu.shily.rabbitmq.utils.RabbitMqConnectionUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author Shily-zhang
 * @Description
 */
public class ReceiveLogs02 {

    // 交换机名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqConnectionUtil.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        //声明一个临时队列
        /**
         * @Description 生成一个临时队列，队列的名称是随机的，当消费者断开与队列的连接的时候，队列会自动删除
         * @Param
         */
        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName,EXCHANGE_NAME,"");
        System.out.println("等待接收消息，把接收到消息打印在屏幕上........");

        //接收消息
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("ReceiveLogs02控制台打印接收到的消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
        };
        //消费者取消消息时回调接口
        channel.basicConsume(queueName,true,deliverCallback,consumerTag -> {});

    }

}
