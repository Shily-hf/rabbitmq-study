package edu.shily.rabbitmq.three;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import edu.shily.rabbitmq.utils.RabbitMqConnectionUtil;
import edu.shily.rabbitmq.utils.SleepUtil;

/**
 * @author Shily-zhang
 * @Description 消费者，主要设置手动应答
 */
public class Work03 {

    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqConnectionUtil.getChannel();
        System.out.println("C2等待接收消息处理时间较长");

        DeliverCallback deliverCallback = (consumerTag,message)->{
            SleepUtil.sleep(30);
            System.out.println("接收到的消息：" + new String(message.getBody(),"UTF-8"));

            /**
             * @Description 手动应答
             * @Param
             * 1.消息的标记
             * 2.是否批量应答，一般不开启
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };

        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
        };


        channel.basicConsume(QUEUE_NAME,false,deliverCallback,cancelCallback);
    }
}
