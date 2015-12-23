package contacts.redis.queue.test;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 
 * Ŀ�ģ��ṩͨ�÷��� �� Bean��
 * <li> blocking �� ��ʽ . </li>
 * <li> �������(queue) �ķ�ʽ . </li>
 * @param <T>
 */
@Component
public class RedisQueue<T> implements InitializingBean,DisposableBean{
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private RedisQueueListener listener;//�첽�ص�
	
	// TODO: get from properties
	private String key = "user:queue";
	
	
	private int cap = Short.MAX_VALUE;//��������������������������ᵼ����վ�����
	private byte[] rawKey;
	private RedisConnectionFactory factory;
	private RedisConnection connection;//for blocking
	private BoundListOperations<String, T> listOperations;//noblocking
	
	private Lock lock = new ReentrantLock();//���ڵײ�IO��������

	private Thread listenerThread;
	
	private boolean isClosed;

	
	@Override
	public void afterPropertiesSet() throws Exception {
		factory = redisTemplate.getConnectionFactory();
		connection = RedisConnectionUtils.getConnection(factory);
		rawKey = redisTemplate.getKeySerializer().serialize(key);
		listOperations = redisTemplate.boundListOps(key);
		if(listener != null){
			listenerThread = new ListenerThread();
			listenerThread.setDaemon(true);
			listenerThread.start();
		}
	}
	
	
	/**
	 * blocking
	 * remove and get last item from queue:BRPOP
	 * @return
	 */
	public T takeFromTail(int timeout) throws InterruptedException{ 
		lock.lockInterruptibly();
		try{
			List<byte[]> results = connection.bRPop(timeout, rawKey);
			if(CollectionUtils.isEmpty(results)){
				return null;
			}
			return (T)redisTemplate.getValueSerializer().deserialize(results.get(1));
		}finally{
			lock.unlock();
		}
	}
	
	public T takeFromTail() throws InterruptedException{
		return takeFromHead(0);
	}
	
	/**
	 * �Ӷ��е�ͷ������
	 */
	public void pushFromHead(T value){
		listOperations.leftPush(value);
	}
	
	/**
	 * �Ӷ��е�β������
	 * @param value
	 */
	public void pushFromTail(T value){
		listOperations.rightPush(value);
	}
	
	/**
	 * <p>��ʽ����������ʽ</p>
	 * �Ӷ��е�ͷ���Ƴ�
	 * <p>���������û�У��򷵻� null .</p>
	 * @return
	 */
	public T removeFromHead(){
		return listOperations.leftPop();
	}
	
	public T removeFromTail(){
		return listOperations.rightPop();
	}
	
	/**
	 * blocking
	 * remove and get first item from queue:BLPOP
	 * @return
	 */
	public T takeFromHead(int timeout) throws InterruptedException{
		lock.lockInterruptibly();
		try{
			List<byte[]> results = connection.bLPop(timeout, rawKey);
			if(CollectionUtils.isEmpty(results)){
				return null;
			}
			return (T)redisTemplate.getValueSerializer().deserialize(results.get(1));
		}finally{
			lock.unlock();
		}
	}
	
	public T takeFromHead() throws InterruptedException{
		return takeFromHead(0);
	}

	@Override
	public void destroy() throws Exception {
		if(isClosed){
			return;
		}
		shutdown();
		RedisConnectionUtils.releaseConnection(connection, factory);
	}
	
	private void shutdown(){
		try{
			listenerThread.interrupt();
		}catch(Exception e){
			//
		}
	}
	
	class ListenerThread extends Thread {
		
		@Override
		public void run(){
			try{
				while(true){
					T value = takeFromHead();//cast exception? you should check.
					//���ִ��
					if(value != null){
						try{
							listener.onMessage(value);
						}catch(Exception e){
							//
						}
					}
				}
			}catch(InterruptedException e){
				//
			}
		}
	}
	
}
