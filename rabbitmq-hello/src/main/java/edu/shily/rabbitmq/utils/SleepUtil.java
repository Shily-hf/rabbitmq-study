package edu.shily.rabbitmq.utils;

/**
 * @author Shily-zhang
 * @Description 睡眠工具类
 */
public class SleepUtil {

    public static void sleep(int second){
        try {
            Thread.sleep(second * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
