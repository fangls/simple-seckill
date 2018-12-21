package com.fang.seckill.consumer;

import com.fang.seckill.service.GoodsSeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * Description：redis消费者
 * 在RedisMqConfig中被注册使用
 * @author fangliangsheng
 * @date 2018/12/19
 */
@Slf4j
@Component
public class RedisQueueConsumer {

    public static CountDownLatch downLatch = new CountDownLatch(1000);

    @Autowired
    private GoodsSeckillService seckillService;

    /**
     * 此处的消费是并发的，所以减库存时还需要通过锁保证
     * 使用分布式队列，是为了隔离高并发，让大流量先进入mq中，然后在消费
     * 可以通过redis同步记录剩余情况，避免卖完后的900次查库检查
     * @param message
     */
    public void receiveMessage(String message) {
        seckillService.saveGoods(Long.valueOf(message));
        downLatch.countDown();
    }
}
