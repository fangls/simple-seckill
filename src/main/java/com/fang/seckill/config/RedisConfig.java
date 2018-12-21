package com.fang.seckill.config;

import com.fang.seckill.consumer.RedisQueueConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * Description：redis的生产-消费模式配置
 *
 * @author fangliangsheng
 * @date 2018/12/19
 */
@Configuration
public class RedisConfig {

    /**
     * 注册topic的消费类
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic("seckill.queue"));

        return container;
    }

    /**
     * 注册消费类，直接消费方法
     * @param consumer 消费实现类
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapter(RedisQueueConsumer consumer) {
        return new MessageListenerAdapter(consumer, "receiveMessage");
    }

    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}
