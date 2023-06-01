package edu.shily.rabbitmq.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import edu.shily.rabbitmq.utils.RabbitMqConnectionUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author Shily-zhang
 * @Description
 */
public class ReceiveLogsDirect01 {

    public static final String EXCHAGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqConnectionUtil.getChannel();

        channel.exchangeDeclare(EXCHAGE_NAME, BuiltinExchangeType.DIRECT);

        channel.queueDeclare("console",false,false,false,null);

        channel.queueBind("console",EXCHAGE_NAME,"info");
        channel.queueBind("console",EXCHAGE_NAME,"warning");

        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("ReceiveLogsDirect01控制台打印接收到的消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
        };
        channel.basicConsume("console",true,deliverCallback,consumerTag -> {});
    }
}
