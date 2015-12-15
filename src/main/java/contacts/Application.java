package contacts;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.StringRedisTemplate;

@ComponentScan
@EnableAutoConfiguration
public class Application {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(Application.class);

	public static void main(String[] args) {
		// SpringApplication.run(Application.class, args);

		ApplicationContext ctx = SpringApplication.run(Application.class, args);

		// redisTest(ctx);

//		testRedis(ctx);
	}

	/**
	 * redis 测试.<br>
	 * 将值塞入 redis 库中
	 * 
	 * @param ctx
	 */
	private static void testRedis(ApplicationContext ctx) {
		// RedisTemplate 的实现
		StringRedisTemplate stringRedisTemplate = ctx
				.getBean(StringRedisTemplate.class);

		// Using set to set value
		stringRedisTemplate.opsForValue().set("R", "Ram");
		stringRedisTemplate.opsForValue().set("S", "Shyam");

		// Fetch values from set
		System.out.println(stringRedisTemplate.opsForValue().get("R"));
		System.out.println(stringRedisTemplate.opsForValue().get("S"));

		// Using Hash Operation
		String mohan = "Mohan";
		stringRedisTemplate.opsForHash().put("M",
				String.valueOf(mohan.hashCode()), mohan);
		System.out.println(stringRedisTemplate.opsForHash().get("M",
				String.valueOf(mohan.hashCode())));
	}

	private static void redisTest(ApplicationContext ctx) {
		// RedisTemplate 的实现
		StringRedisTemplate template = ctx.getBean(StringRedisTemplate.class);
		CountDownLatch latch = ctx.getBean(CountDownLatch.class);

		LOGGER.info("Sending message...");
		template.convertAndSend("chat", "Hello from Redis!");

		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.exit(0);
	}
}