package com.fang.seckill.lock;

import com.fang.seckill.BaseTests;
import com.fang.seckill.entity.GoodsDO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Description：使用redis锁的方式
 *
 * @author fangliangsheng
 * @date 2018/12/19
 */
@Slf4j
public class RedisLockSeckillTests extends BaseTests {

    @Autowired
    private RedissonClient redissonClient;

    private static final String KEY_PREFIX = "goods:";

    @Test
    public void redisLock() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1000);

        RLock lock = redissonClient.getLock(KEY_PREFIX + super.GOODS_ID);

        IntStream.range(0, 1000).parallel().forEach(i->{
            try {
                if(lock.tryLock(200, TimeUnit.MILLISECONDS)){
                    Optional<GoodsDO> goodsDO = goodsRepository.findById(GOODS_ID);

                    if(goodsDO.get().getNumber() > 0){
                        goodsRepository.updateNumber(goodsDO.get().getId());
                    }

                    lock.unlock();
                }
            }catch (InterruptedException e){
                log.error("锁获取超时");
            }

            latch.countDown();
        });

        latch.await();
        Optional<GoodsDO> goodsDO = goodsRepository.findById(GOODS_ID);

        log.info("[redisLock]剩余数量{}", goodsDO.get().getNumber());
        Assert.assertTrue(goodsDO.get().getNumber() == 0);
    }

}
