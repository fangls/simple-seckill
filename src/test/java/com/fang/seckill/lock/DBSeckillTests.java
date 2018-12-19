package com.fang.seckill.lock;

import com.fang.seckill.BaseTests;
import com.fang.seckill.entity.GoodsDO;
import com.fang.seckill.repository.GoodsRepository;
import com.fang.seckill.service.GoodsSeckillService;
import com.sun.xml.internal.rngom.parse.host.Base;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.StaleObjectStateException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

/**
 * 基于数据库的秒杀
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DBSeckillTests extends BaseTests {

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsSeckillService seckillService;

    /**
     * 先检查库存后更新
     * 存在超卖
     */
    @Test
    public void queryCheckAndUpdate() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1000);

        IntStream.range(0, 1000).parallel().forEach(i->{
            Optional<GoodsDO> goodsDO = goodsRepository.findById(GOODS_ID);

            if(goodsDO.get().getNumber() > 0){
                goodsRepository.updateNumber(goodsDO.get().getId());
            }

            latch.countDown();
        });

        latch.await();
        Optional<GoodsDO> goodsDO = goodsRepository.findById(GOODS_ID);

        log.info("[queryCheckAndUpdate]剩余数量{}", goodsDO.get().getNumber());
        Assert.assertTrue(goodsDO.get().getNumber() < 0);
    }

    /**
     * 先检查库存后带where更新
     * 不会超卖
     */
    @Test
    public void queryCheckAndUpdateWhere() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1000);

        IntStream.range(0, 1000).parallel().forEach(i->{
            Optional<GoodsDO> goodsDO = goodsRepository.findById(GOODS_ID);

            if(goodsDO.get().getNumber() > 0){
                goodsRepository.updateNumberWhereNumber(goodsDO.get().getId());
            }

            latch.countDown();
        });

        latch.await();
        Optional<GoodsDO> goodsDO = goodsRepository.findById(GOODS_ID);

        log.info("[queryCheckAndUpdateWhere]剩余数量{}", goodsDO.get().getNumber());
        Assert.assertTrue(goodsDO.get().getNumber() == 0);
    }

    /**
     * 先检查库存后通过version乐观锁控制
     * 不会超卖
     * Optimistic Concurrency Control
     */
    @Test
    public void queryCheckAndVersionOCC() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1000);

        IntStream.range(0, 1000).parallel().forEach(i->{
            GoodsDO goodsDO = goodsRepository.findById(GOODS_ID).get();

            if(goodsDO.getNumber() > 0){
                goodsDO.setNumber(goodsDO.getNumber()-1);
                try {
                    goodsRepository.save(goodsDO);
                }catch (Exception e){
                    //此处的具体异常是StaleObjectStateException，但捕获该异常时会报错提示session关闭
                    //该如何捕获该具体异常有待研究
                    log.error("Version过期{}",i);
                }
            }

            latch.countDown();
        });

        latch.await();
        Optional<GoodsDO> goodsDO = goodsRepository.findById(GOODS_ID);

        log.info("[queryCheckAndVersionOCC]剩余数量{}", goodsDO.get().getNumber());
        Assert.assertTrue(goodsDO.get().getNumber() == 0);
    }

    /**
     * 通过for update悲观锁控制
     * 不会超卖
     * Pessimistic Concurrency Control
     */
    @Test
    public void queryCheckAndForUpdatePCC() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1000);

        IntStream.range(0, 1000).parallel().forEach(i->{
            //使用for update要开启事务，所以写到service方法中调用
            seckillService.saveGoodsForUpdate(GOODS_ID);
            latch.countDown();
        });

        latch.await();
        Optional<GoodsDO> goodsDO = goodsRepository.findById(GOODS_ID);

        log.info("[queryCheckAndForUpdatePCC]剩余数量{}", goodsDO.get().getNumber());
        Assert.assertTrue(goodsDO.get().getNumber() == 0);
    }
}

