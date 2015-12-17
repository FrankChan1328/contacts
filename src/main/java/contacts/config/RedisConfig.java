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
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableAutoConfiguration 
// TODO 摘自网上，功能：做缓存
@EnableCaching  
public class RedisConfig extends CachingConfigurerSupport{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfig.class);
	
	@Bean
	public RedisConnectionFactory jedisConnectionFactory() {
		// config pool
		// TODO 可以配置哪些
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
	public RedisTemplate<String,Object> redisTemplate(){
		RedisTemplate<String,Object> redisTemplate = new RedisTemplate<String,Object>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		// 
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new GenericToStringSerializer <Object> (Object.class));
		redisTemplate.setValueSerializer(new GenericToStringSerializer <Object> (Object.class));
		
		return redisTemplate;
	}
	
	@Bean
	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
		StringRedisTemplate template = new StringRedisTemplate(factory);  
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		// 
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		// 
		jackson2JsonRedisSerializer.setObjectMapper(om);
		
		template.setValueSerializer(jackson2JsonRedisSerializer);
		template.afterPropertiesSet();
		return template;
	}
	
	// TODO 摘抄自网上，功能：做缓存
	@Bean  
	public KeyGenerator wiselyKeyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
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
}
