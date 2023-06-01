package edu.shily.rabbitmq.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import edu.shily.rabbitmq.utils.RabbitMqConnectionUtil;

/**
 * @author Shily-zhang
 * @Description 一个工作线程（相当于消费者）
 */
public class Work01 {

    // 队列名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqConnectionUtil.getChannel();

        // 消息接收
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("接收到的消息：" + new String(message.getBody()));
        };

        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
        };
        System.out.println("C2等待接收消息.......");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
