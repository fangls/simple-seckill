package com.fang.seckill.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description：Zookeeper配置
 * 使用Curator操作zookeeper
 * @author fangliangsheng
 * @date 2018/12/19
 */
@Configuration
public class ZookeeperConfig {

    @Value("${zookeeper.url}")
    private String zookeeperUrl;

    @Bean
    public InterProcessMutex create(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperUrl, retryPolicy);
        client.start();
        return new InterProcessMutex(client, "/curator/lock");
    }



}
