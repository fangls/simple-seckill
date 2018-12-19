package com.fang.seckill;

import com.fang.seckill.entity.GoodsDO;
import com.fang.seckill.repository.GoodsRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

/**
 * Description：
 *
 * @author fangliangsheng
 * @date 2018/12/19
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseTests {

    protected static final Long GOODS_ID = 1000l;

    @Autowired
    protected GoodsRepository goodsRepository;

    @Before
    @Transactional
    public void initData(){
        goodsRepository.deleteAll();

        GoodsDO goodsDO = new GoodsDO();
        goodsDO.setId(GOODS_ID);
        goodsDO.setName("Macbook pro");
        goodsDO.setNumber(100);

        goodsRepository.save(goodsDO);
    }

}
