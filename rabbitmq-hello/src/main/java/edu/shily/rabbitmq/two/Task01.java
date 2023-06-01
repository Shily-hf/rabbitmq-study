package edu.shily.rabbitmq.two;

import com.rabbitmq.client.Channel;
 import edu.shily.rabbitmq.utils.RabbitMqConnectionUtil;

import java.util.Scanner;

/**
 * @author Shily-zhang
 * @Description 生产者 发送大量消息，查看队列是否轮询消费者
 */
public class Task01 {

    // 队列名称
    public static final String QUEUE_NAME = "hello";

    // 发送大量消息
    public static void main(String[] args) throws Exception{

        Channel channel = RabbitMqConnectionUtil.getChannel();

        // 发送消息
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("发送消息完成：" + message);
        }


    }
}
