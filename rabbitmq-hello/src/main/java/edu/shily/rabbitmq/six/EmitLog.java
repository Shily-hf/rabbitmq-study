package edu.shily.rabbitmq.six;

import com.rabbitmq.client.Channel;
import edu.shily.rabbitmq.utils.RabbitMqConnectionUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author Shily-zhang
 * @Description
 */
public class EmitLog {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqConnectionUtil.getChannel();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"error",null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息：" + message);
        }


    }

}
