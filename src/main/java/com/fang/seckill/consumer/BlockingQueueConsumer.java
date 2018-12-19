package com.fang.seckill.consumer;

import com.fang.seckill.service.GoodsSeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Description：消费BlockingQueueTests中的数据
 * 在启动的时候开始消费，这里需要开启新线程，否则会阻塞启动
 * @author fangliangsheng
 * @date 2018/12/19
 */
@Slf4j
@Component
public class BlockingQueueConsumer implements ApplicationRunner {

    @Autowired
    private GoodsSeckillService seckillService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("开始消费blockingQueue");
        new Thread( ()->{
            try {
                while (true){
                    Long goodsId = GoodsSeckillService.blockingQueue.take();
                    if (goodsId != null){
                        seckillService.saveGoods(goodsId);
                    }
                }
            }catch (Exception e){

            }
        }).start();
    }
}
