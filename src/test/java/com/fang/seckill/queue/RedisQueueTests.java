package com.fang.seckill.queue;

import com.fang.seckill.BaseTests;
import com.fang.seckill.consumer.RedisQueueConsumer;
import com.fang.seckill.entity.GoodsDO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Description：基于redis的分布式队列
 *
 * @author fangliangsheng
 * @date 2018/12/19
 */
@Slf4j
public class RedisQueueTests extends BaseTests {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void redisQueue() throws Exception{
        IntStream.range(0, 1000).parallel().forEach(i->{
            redisTemplate.convertAndSend("seckill.queue", super.GOODS_ID.toString());
        });

        RedisQueueConsumer.downLatch.await();

        Optional<GoodsDO> goodsDO = goodsRepository.findById(GOODS_ID);

        log.info("[redisQueue]剩余数量 {}", goodsDO.get().getNumber());
        Assert.assertTrue(goodsDO.get().getNumber() == 0);
    }

}
