package com.fang.seckill.repository;

import com.fang.seckill.entity.TradeOrderDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Description：
 *
 * @author fangliangsheng
 * @date 2018/12/18
 */
public interface TradeOrderRepository extends JpaRepository<TradeOrderDO,Long> {
}
