package com.fang.seckill.lock;

import com.fang.seckill.BaseTests;
import com.fang.seckill.entity.GoodsDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
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
 * Description：使用zookeeper锁的方式
 *
 * @author fangliangsheng
 * @date 2018/12/19
 */
@Slf4j
public class ZookeeperLockSeckillTests extends BaseTests {

    @Autowired
    private InterProcessMutex interProcessMutex;

    @Test
    public void zookeeperLock() throws Exception {
        CountDownLatch latch = new CountDownLatch(1000);

        IntStream.range(0, 1000).parallel().forEach(i->{
            try {
                interProcessMutex.acquire();

                Optional<GoodsDO> goodsDO = goodsRepository.findById(GOODS_ID);

                if(goodsDO.get().getNumber() > 0){
                    goodsRepository.updateNumber(goodsDO.get().getId());
                }

                interProcessMutex.release();
            }catch (Exception e){
                log.error("锁获取超时", e);
            }

            latch.countDown();
        });

        latch.await();
        Optional<GoodsDO> goodsDO = goodsRepository.findById(GOODS_ID);

        log.info("[zookeeperLock]剩余数量{}", goodsDO.get().getNumber());
        Assert.assertTrue(goodsDO.get().getNumber() == 0);
    }

}
