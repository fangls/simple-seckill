package com.fang.seckill.queue;

import com.fang.seckill.BaseTests;
import com.fang.seckill.consumer.KafkaQueueConsumer;
import com.fang.seckill.entity.GoodsDO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Description：基于kafka的分布式队列
 *
 * @author fangliangsheng
 * @date 2018/12/19
 */
@Slf4j
public class KafkaQueueTests extends BaseTests {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Test
    public void kafkaQueue() throws Exception{
        IntStream.range(0, 1000).parallel().forEach(i->{
            kafkaTemplate.send("seckill.queue", super.GOODS_ID.toString());
        });

        KafkaQueueConsumer.downLatch.await(2000, TimeUnit.MILLISECONDS);

        Optional<GoodsDO> goodsDO = goodsRepository.findById(GOODS_ID);

        log.info("[kafkaQueue]剩余数量 {}", goodsDO.get().getNumber());
        Assert.assertTrue(goodsDO.get().getNumber() == 0);
    }

}
