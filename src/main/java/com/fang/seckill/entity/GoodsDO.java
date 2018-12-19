package com.fang.seckill.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

/**
 * Description：商品及库存
 *
 * @author fangliangsheng
 * @date 2018/12/18
 */
@Data
@Entity
@Table(name = "t_goods")
@RequiredArgsConstructor
public class GoodsDO {

    @Id
    private Long id;

    private String name;

    private Integer number;

    @Version
    private Integer version;
}
