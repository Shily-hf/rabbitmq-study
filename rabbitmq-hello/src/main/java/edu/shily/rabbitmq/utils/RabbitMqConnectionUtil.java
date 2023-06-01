package edu.shily.rabbitmq.utils;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author Shily-zhang
 * @Description 连接工厂工具类
 */
public class RabbitMqConnectionUtil {

    public static Channel getChannel() throws Exception{
        //定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置连接属性
        factory.setHost("192.168.231.128");
        factory.setUsername("shily");
        factory.setPassword("zhang");
        // 创建连接
        Connection connection = factory.newConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        return channel;
    }
}
