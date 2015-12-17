package contacts.redis.queue.test;

public interface RedisQueueListener<T> {

	public void onMessage(T value);
}