package contacts.redis.queue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * redis 队列操作 模板（List 操作）
 *
 */
@Component
public class ListOps {

	// TODO :释放的时候，还连接回池子，还是 destroy 连接池， 需要注意
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * 压栈
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long stackPush(String key, Object value) {
		return redisTemplate.opsForList().leftPush(key, value);
	}

	/**
	 * 出栈
	 * 
	 * @param key
	 * @return
	 */
	public Object stackPop(String key) {
		return redisTemplate.opsForList().leftPop(key);
	}

	/**
	 * 入队
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long queueIn(String key, Object value) {
		return redisTemplate.opsForList().rightPush(key, value);
	}

	/**
	 * 出队
	 * 
	 * @param key
	 * @return
	 */
	public Object queueOut(String key) {
		return redisTemplate.opsForList().leftPop(key);
	}

	/**
	 * 栈/队列的长度
	 * 
	 * @param key
	 * @return
	 */
	public Long length(String key) {
		return redisTemplate.opsForList().size(key);
	}

	/**
	 * 范围检索
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Object> range(String key, int start, int end) {
		return redisTemplate.opsForList().range(key, start, end);
	}

	/**
	 * 移除
	 * 
	 * @param key
	 * @param i
	 * @param value
	 */
	public void remove(String key, long i, Object value) {
		redisTemplate.opsForList().remove(key, i, value);
	}

	/**
	 * 检索
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	public Object index(String key, long index) {
		return redisTemplate.opsForList().index(key, index);
	}

	/**
	 * 置值
	 * 
	 * @param key
	 * @param index
	 * @param value
	 */
	public void set(String key, long index, Object value) {
		redisTemplate.opsForList().set(key, index, value);
	}

	/**
	 * 裁剪
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	public void trim(String key, long start, int end) {
		redisTemplate.opsForList().trim(key, start, end);
	}
}
