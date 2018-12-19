package com.fang.seckill.lock;

import com.fang.seckill.BaseTests;
import com.fang.seckill.entity.GoodsDO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * Description：基于jvm相关锁的秒杀
 *
 * @author fangliangsheng
 * @date 2018/12/19
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class JvmLockSeckillTests extends BaseTests {

    private Lock lock = new ReentrantLock();

    /**
     * 可重入锁
     * 不会超卖，当直接使用lock.tryLock()会出现剩余
     * @throws InterruptedException
     */
    @Test
    public void lock() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1000);

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

        log.info("[lock]剩余数量{}", goodsDO.get().getNumber());
        Assert.assertTrue(goodsDO.get().getNumber() == 0);
    }



}
