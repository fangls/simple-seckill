package com.fang.seckill.consumer;

import com.fang.seckill.service.GoodsSeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * Description：kafka消费者
 *
 * @author fangliangsheng
 * @date 2018/12/20
 */
@Slf4j
@Component
public class KafkaQueueConsumer {

    public static CountDownLatch downLatch = new CountDownLatch(1000);

    @Autowired
    private GoodsSeckillService seckillService;

    @KafkaListener(topics = "seckill.queue")
    public void receiveMessage(String message) {
        seckillService.saveGoods(Long.valueOf(message));
        downLatch.countDown();
    }
}
