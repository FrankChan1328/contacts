package contacts.redis;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 该类用来发送消息
 *
 */
@Component
public class RedisMessageSend {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisMessageSend.class);
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	private CountDownLatch countDownLatch;
	
	public void sendMessage(String msg){
		LOGGER.info("Sending message......");
		// 
		// redisTemplate.convertAndSend("chat", "Hello from Redis!");
		redisTemplate.convertAndSend("chat", msg);
		// 
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
}
