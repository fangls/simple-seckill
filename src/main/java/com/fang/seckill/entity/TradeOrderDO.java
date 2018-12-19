package com.fang.seckill.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

/**
 * Description：交易订单
 *
 * @author fangliangsheng
 * @date 2018/12/18
 */
@Data
@Entity
@Table(name = "t_trade_order")
@RequiredArgsConstructor
public class TradeOrderDO {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private GoodsDO goodsDO;

    private String userId;
}
