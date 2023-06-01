package edu.shily.rabbitmq.five;

import com.rabbitmq.client.Channel;
import edu.shily.rabbitmq.utils.RabbitMqConnectionUtil;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author Shily-zhang
 * @Description 发消息   交换机
 */
public class EmitLog {
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception{

        Channel channel = RabbitMqConnectionUtil.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息：" + message);
        }
    }
}
