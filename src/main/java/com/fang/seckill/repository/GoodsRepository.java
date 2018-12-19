package com.fang.seckill.repository;

import com.fang.seckill.entity.GoodsDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;

/**
 * Description：
 *
 * @author fangliangsheng
 * @date 2018/12/18
 */
public interface GoodsRepository extends JpaRepository<GoodsDO, Long> {

    @Transactional
    @Modifying
    @Query(value = "update t_goods set number = number - 1 where id = ?1",nativeQuery = true)
    Integer updateNumber(Long id);

    @Transactional
    @Modifying
    @Query(value = "update t_goods set number = number - 1 where id = ?1 and number > 0",nativeQuery = true)
    Integer updateNumberWhereNumber(Long id);

    @Query(value = "select * from t_goods where id = ?1 for update", nativeQuery = true)
    GoodsDO queryForUpdate(Long id);
}
