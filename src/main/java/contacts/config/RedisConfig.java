package contacts.config;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableAutoConfiguration 
// TODO 摘自网上，功能：做缓存
@EnableCaching  
public class RedisConfig extends CachingConfigurerSupport{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfig.class);
	
	@Bean
	public RedisConnectionFactory jedisConnectionFactory() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(5);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
		JedisConnectionFactory ob = new JedisConnectionFactory(poolConfig);
		ob.setUsePool(true);
		ob.setHostName("localhost");
		ob.setPort(6379);
		return ob;
	}
	
	@Bean
	public StringRedisTemplate stringRedisTemplate(){
		return new StringRedisTemplate(jedisConnectionFactory());
	}
	
	@Bean
	// TODO 是否正确
	public RedisTemplate<String,String> redisTemplate(){
		RedisTemplate<String,String> redisTemplate = new RedisTemplate<String,String>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		
		// 
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new GenericToStringSerializer <Object> (Object.class));
		redisTemplate.setValueSerializer(new GenericToStringSerializer <Object> (Object.class));
		
		return redisTemplate;
	}
	
	// TODO 摘抄自网上，功能：做缓存
	@Bean  
	public KeyGenerator wiselyKeyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method,
					Object... params) {
				StringBuilder sb = new StringBuilder();
				sb.append(target.getClass().getName());
				sb.append(method.getName());
				for (Object obj : params) {
					sb.append(obj.toString());
				}
				return sb.toString();
			}
		};
	}
	
	// TODO 摘自网上，功能：做缓存
	@Bean  
	public CacheManager cacheManager(@SuppressWarnings("rawtypes") RedisTemplate redisTemplate){
		return new RedisCacheManager(redisTemplate);  
	}

//	@Bean
//	RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
//			MessageListenerAdapter listenerAdapter) {
//
//		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//		container.setConnectionFactory(connectionFactory);
//		container.addMessageListener(listenerAdapter, new PatternTopic("chat"));
//
//		return container;
//	}
//
//	@Bean
//	MessageListenerAdapter listenerAdapter(Receiver receiver) {
//		return new MessageListenerAdapter(receiver, "receiveMessage");
//	}
//
//	@Bean
//	Receiver receiver(CountDownLatch latch) {
//		return new Receiver(latch);
//	}
//
//	@Bean
//	CountDownLatch latch() {
//		return new CountDownLatch(1);
//	}
//
//	@Bean
//	StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
//		return new StringRedisTemplate(connectionFactory);
//	}
}
