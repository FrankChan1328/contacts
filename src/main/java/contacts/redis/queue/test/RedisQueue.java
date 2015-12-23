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
 * 目的：提供通用服务 的 Bean：
 * <li> blocking 的 方式 . </li>
 * <li> 常规队列(queue) 的方式 . </li>
 * @param <T>
 */
@Component
public class RedisQueue<T> implements InitializingBean,DisposableBean{
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private RedisQueueListener listener;//异步回调
	
	// TODO: get from properties
	private String key = "user:queue";
	
	
	private int cap = Short.MAX_VALUE;//最大阻塞的容量，超过容量将会导致清空旧数据
	private byte[] rawKey;
	private RedisConnectionFactory factory;
	private RedisConnection connection;//for blocking
	private BoundListOperations<String, T> listOperations;//noblocking
	
	private Lock lock = new ReentrantLock();//基于底层IO阻塞考虑

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
	 * 从队列的头，插入
	 */
	public void pushFromHead(T value){
		listOperations.leftPush(value);
	}
	
	/**
	 * 从队列的尾，插入
	 * @param value
	 */
	public void pushFromTail(T value){
		listOperations.rightPush(value);
	}
	
	/**
	 * <p>方式：非阻塞方式</p>
	 * 从队列的头部移除
	 * <p>如果队列中没有，则返回 null .</p>
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
					//逐个执行
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
