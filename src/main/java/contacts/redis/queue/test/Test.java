package contacts.redis.queue.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Redis��list���ݽṹ������"˫�˶���"�����ԣ�ͬʱredis���г־����ݵ����������redisʵ�ֲַ�ʽ�����Ƿǳ���ȫ�ɿ��ġ�<br />
 * ��������JMS�е� "Queue"��ֻ�������ܺͿɿ���(������)��û��JMS�ϸ� <br />
 * 
 * Redis�еĶ�������ʱ������connection���޷�����������������������ڻ������ӳ��������Ҫע�⡣<br />
 * 
 * ����ͨ��spring-data-redis����ʵ��"ͬ������"����Ʒ��������JMS��<br />
 * 
 * ������ʵ���У���û���ṩ���ڶ�������֮�����Ϣȷ�ϻ��ƣ�
 * ��������Ȥ�����Լ�����ʵ������
 *
 */
// �ڳ��������ڼ䣬�����ͨ��redis-cli(�ͻ��˴���)ִ�С�lpush������ᷢ�ֳ���Ŀ���̨��Ȼ�ܹ�������ӡ������Ϣ��
// ˵�������������ڼ䣬��redis-cli �ͻ��˴��� ִ�����lpush user:queue 123 ������̨�·������ӡ����������Ϣ
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
