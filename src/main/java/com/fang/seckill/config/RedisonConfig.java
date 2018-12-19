package com.fang.seckill.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description：实例化RedissonClient用于操作redis
 *
 * @author fangliangsheng
 * @date 2018/12/19
 */
@Configuration
public class RedisonConfig {

    @Value("${spring.redis.host}")
    private String url;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer().setAddress("redis://"+url+":"+port);

        if (password != null && !password.isEmpty()){
            singleServerConfig.setPassword(password);
        }

        return Redisson.create(config);
    }

}
