package edu.shily.rabbitmq.seven;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import edu.shily.rabbitmq.utils.RabbitMqConnectionUtil;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Shily-zhang
 * @Description
 */
public class EmitLogTopic {

    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqConnectionUtil.getChannel();
        Map<String,String> bindingMap = new HashMap<>();

        bindingMap.put("quick.orange.rabbit","被Q1Q2接收到");
        bindingMap.put("lazy.orange.elephant","被Q1Q2接收到");
        bindingMap.put("quick.orange.fox","被Q1接收到");
        bindingMap.put("lazy.brown,fox","被Q2接收到");
        bindingMap.put("lazy.pink.rabbit","满足两个条件，但是只被Q2接收到一次");
        bindingMap.put("quick.brown.fox","不匹配任何队列，丢弃消息");
        bindingMap.put("quick.orange.male.rabbit","四个单词，且不匹配，被丢弃");
        bindingMap.put("lazy.orange.male.rabbit","四个单词，但是匹配Q2");

        for (Map.Entry<String, String> bindingEntry : bindingMap.entrySet()) {
            String routingKey = bindingEntry.getKey();
            String message = bindingEntry.getValue();

            channel.basicPublish(EXCHANGE_NAME,routingKey,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息：" + message);
        }
    }
}
