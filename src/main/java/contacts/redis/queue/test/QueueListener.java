package contacts.redis.queue.test;

public class QueueListener<String> implements RedisQueueListener<String> {

	@Override
	public void onMessage(String value) {
		System.out.println(value);

	}

}
