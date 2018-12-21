package com.fang.seckill.service;

import com.fang.seckill.entity.GoodsDO;
import com.fang.seckill.repository.GoodsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Description：
 *
 * @author fangliangsheng
 * @date 2018/12/19
 */
@Slf4j
@Service
public class GoodsSeckillService {

    public static BlockingQueue<Long> blockingQueue = new LinkedBlockingQueue<>(50);

    @Autowired
    private GoodsRepository goodsRepository;

    @Transactional
    public void saveGoodsForUpdate(Long goodsId){
        GoodsDO goodsDO = goodsRepository.queryForUpdate(goodsId);

        if(goodsDO.getNumber() > 0){
            goodsRepository.updateNumber(goodsDO.getId());
        }
    }

    @Transactional
    public void saveGoods(Long goodsId){
        GoodsDO goodsDO = goodsRepository.findById(goodsId).get();

        if(goodsDO.getNumber() > 0){
            goodsRepository.updateNumberWhereNumber(goodsDO.getId());
        }
    }
}
