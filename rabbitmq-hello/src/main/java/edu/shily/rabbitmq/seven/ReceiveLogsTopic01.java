package edu.shily.rabbitmq.seven;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.tools.json.JSONUtil;
import edu.shily.rabbitmq.utils.RabbitMqConnectionUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author Shily-zhang
 * @Description
 */
public class ReceiveLogsTopic01 {

    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        // 连接工厂，创建信道
        Channel channel = RabbitMqConnectionUtil.getChannel();
        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        // 声明队列
        String queueName = "Q1";
        channel.queueDeclare(queueName,false,false,false,null);
        // 队列绑定交换机
        channel.queueBind(queueName,EXCHANGE_NAME,"*.orange.*");
        System.out.println("等待接收消息......");
        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println(new String(message.getBody(), StandardCharsets.UTF_8));
            System.out.println("接收队列：" + queueName + "绑定键：" + message.getEnvelope().getRoutingKey());
        };
        channel.basicConsume(queueName,true,deliverCallback,consumerTag -> {});
    }


}
