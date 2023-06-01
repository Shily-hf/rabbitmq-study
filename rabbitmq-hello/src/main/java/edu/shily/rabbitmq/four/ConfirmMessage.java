package edu.shily.rabbitmq.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import edu.shily.rabbitmq.utils.RabbitMqConnectionUtil;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author Shily-zhang
 * @Description 发布确认模式
 *              1.单个确认
 *              2.批量确认
 *              3.异步批量确认
 */
public class ConfirmMessage {

    //批量发消息的个数
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        //1.单个确认
        // ConfirmMessage.publishMessageIndividually(); //439ms
        //2.批量确认
        // ConfirmMessage.publishMessageBatch(); //34ms
        //3.异步批量确认
        ConfirmMessage.publishMessageAsync(); //24ms

    }

    //单个确认，因为要一条条的确认消息，效率较低
    public static void publishMessageIndividually() throws Exception{
        Channel channel = RabbitMqConnectionUtil.getChannel();

        //队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);

        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());
            // 单个消息就马上进行发布确认
            boolean flag = channel.waitForConfirms();
            if (flag){
                System.out.println("消息发送成功");
            }
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单个发布确认消息，耗时：" + (end - begin) + "ms");
    }

    //2.批量确认，效率较高，但是对与确认失败的消息无从得知，对于某些场景不适用，如订单
    public static void publishMessageBatch() throws Exception{
        Channel channel = RabbitMqConnectionUtil.getChannel();

        //队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);

        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        // 批量确认消息大小
        int batchSiez = 100;
        //批量发送消息，批量发布确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());

            //判断达到一百条消息的时候 批量确认一次
            if (i % batchSiez == 0){
                //发布确认
                channel.waitForConfirms();
            }
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量发布确认消息，耗时：" + (end - begin) + "ms");
    }

    //异步确认，性价比高，能够批量且能找到未确认的消息
    public static void publishMessageAsync() throws Exception{
        Channel channel = RabbitMqConnectionUtil.getChannel();

        //队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);

        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        /**
         * @Description 线程安全有序的一个哈希表，适用于高并发的情况下
         * @Param
         * 1.轻松的将序号与消息进行关联
         * 2.轻松批量删除条目 只要给到序号
         * 3.支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long,String> outstandingConfirms = new ConcurrentSkipListMap<>();

        //准备消息监听器 监听哪些消息成功了，哪些失败了
        // 消息确认成功 回调函数
        ConfirmCallback ackCallback = (deliveryTag,multiple)->{
            if(multiple){
                // 2.删除已确认消息，剩下的都是未确认消息
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            }else {
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("确认消息：" + deliveryTag);
        };
        // 消息确认失败 回调函数
        ConfirmCallback nackCallback = (deliveryTag,multiple)->{
            String message = outstandingConfirms.get(deliveryTag);
            System.out.println("未确认消息：" + message + "未标记的消息tag:" + deliveryTag);
        };
        channel.addConfirmListener(ackCallback,nackCallback);

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "消息" + i;
            channel.basicPublish("",queueName,null,message.getBytes());
            //1.记录下所有要发送的消息   消息总和
            outstandingConfirms.put(channel.getNextPublishSeqNo(),message);
        }


        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个异步发布确认消息，耗时：" + (end - begin) + "ms");
    }
}
