package contacts.redis.queue.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Redis中list数据结构，具有"双端队列"的特性，同时redis具有持久数据的能力，因此redis实现分布式队列是非常安全可靠的。<br />
 * 它类似于JMS中的 "Queue"，只不过功能和可靠性(事务性)并没有JMS严格。 <br />
 * 
 * Redis中的队列阻塞时，整个connection都无法继续进行其他操作，因此在基于连接池设计是需要注意。<br />
 * 
 * 我们通过spring-data-redis，来实现"同步队列"，设计风格类似与JMS。<br />
 * 
 * 不过本实例中，并没有提供关于队列消费之后的消息确认机制，
 * 如果你感兴趣可以自己尝试实现它。
 *
 */
// 在程序运行期间，你可以通过redis-cli(客户端窗口)执行“lpush”，你会发现程序的控制台仍然能够正常打印队列信息。
// 说明，程序运行期间，在redis-cli 客户端窗口 执行命令：lpush user:queue 123 ，控制台下方将会打印出来队列信息
public class Test {
	public static void maint(String[] args) throws Exception{
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-redis-beans.xml");
		RedisQueue<String> redisQueue = (RedisQueue)context.getBean("jedisQueue");
		redisQueue.pushFromHead("test:app");
		Thread.sleep(15000);
		redisQueue.pushFromHead("test:app");
		Thread.sleep(15000);
		redisQueue.destroy();
	}
}
