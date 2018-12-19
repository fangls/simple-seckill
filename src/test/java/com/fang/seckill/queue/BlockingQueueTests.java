package com.fang.seckill.queue;

import com.fang.seckill.BaseTests;
import com.fang.seckill.entity.GoodsDO;
import com.fang.seckill.service.GoodsSeckillService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Description：JVM队列，生产-消费
 * BlockingQueueConsumer消费
 * @author fangliangsheng
 * @date 2018/12/19
 */
@Slf4j
public class BlockingQueueTests extends BaseTests {

    /**
     * 不会超卖
     * 因为队列让并发序列化执行了
     * @throws Exception
     */
    @Test
    public void blockingQueue() throws Exception{
        //parallel使用的是全局共享的线程池，使用ForkJoinPool可以使用单独的线程池
        ForkJoinPool forkJoinPool = new ForkJoinPool(3);

        forkJoinPool.submit(()->{
            IntStream.range(0, 1000).parallel().forEach(i->{
                try {
                    GoodsSeckillService.blockingQueue.put(super.GOODS_ID);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });

        GoodsSeckillService.downLatch.await();

        Optional<GoodsDO> goodsDO = goodsRepository.findById(GOODS_ID);

        log.info("[blockingQueue]剩余数量{}", goodsDO.get().getNumber());
        Assert.assertTrue(goodsDO.get().getNumber() == 0);
    }
}
