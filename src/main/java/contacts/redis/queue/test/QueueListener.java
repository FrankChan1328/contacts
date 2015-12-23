package contacts.redis.queue.test;

import org.springframework.stereotype.Component;

@Component
public class QueueListener<String> implements RedisQueueListener<String> {

	@Override
	public void onMessage(String value) {
		System.out.println(value);

	}

}
