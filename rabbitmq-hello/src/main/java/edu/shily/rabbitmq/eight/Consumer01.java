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
public class Consumer01 {

    //声明交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String DEAD_EXCHANGE = "dead_exchange";
    // 声明队列
    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqConnectionUtil.getChannel();

        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        // 将普通队列中设置死信交换机
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        // 设置队列routing-key
        arguments.put("x-dead-letter-routing-key","lisi");
        // 设置队列最大长度，制造队列满了，消息转到死信队列中
        // arguments.put("x-max-length",6);

        channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

        // 将普通交换机和队列绑定
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        // 将死信交换机和队列绑定
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");

        //消费消息
        DeliverCallback deliverCallback = (consumerTags,message)->{
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            if (msg.equals("info5")){
                System.out.println("Consumer01接收到的消息是：" + msg + ":此消息是被c1拒收的");
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }else {
                System.out.println("Consumer01接收到的消息是：" + msg);
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }
        };
        // 开启手动应答
        channel.basicConsume(NORMAL_QUEUE,false,deliverCallback,consumerTag -> {});


    }
}
