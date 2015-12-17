package contacts.redis.queue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * redis ���в��� ģ�壨List ������
 *
 */
@Component
public class ListOps {

	// TODO :�ͷŵ�ʱ�򣬻����ӻس��ӣ����� destroy ���ӳأ� ��Ҫע��
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * ѹջ
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long stackPush(String key, Object value) {
		return redisTemplate.opsForList().leftPush(key, value);
	}

	/**
	 * ��ջ
	 * 
	 * @param key
	 * @return
	 */
	public Object stackPop(String key) {
		return redisTemplate.opsForList().leftPop(key);
	}

	/**
	 * ���
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long queueIn(String key, Object value) {
		return redisTemplate.opsForList().rightPush(key, value);
	}

	/**
	 * ����
	 * 
	 * @param key
	 * @return
	 */
	public Object queueOut(String key) {
		return redisTemplate.opsForList().leftPop(key);
	}

	/**
	 * ջ/���еĳ���
	 * 
	 * @param key
	 * @return
	 */
	public Long length(String key) {
		return redisTemplate.opsForList().size(key);
	}

	/**
	 * ��Χ����
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
	 * �Ƴ�
	 * 
	 * @param key
	 * @param i
	 * @param value
	 */
	public void remove(String key, long i, Object value) {
		redisTemplate.opsForList().remove(key, i, value);
	}

	/**
	 * ����
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	public Object index(String key, long index) {
		return redisTemplate.opsForList().index(key, index);
	}

	/**
	 * ��ֵ
	 * 
	 * @param key
	 * @param index
	 * @param value
	 */
	public void set(String key, long index, Object value) {
		redisTemplate.opsForList().set(key, index, value);
	}

	/**
	 * �ü�
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	public void trim(String key, long start, int end) {
		redisTemplate.opsForList().trim(key, start, end);
	}
}
