package contacts.gearman;

import org.gearman.Gearman;
import org.gearman.GearmanFunction;
import org.gearman.GearmanFunctionCallback;
import org.gearman.GearmanServer;
import org.gearman.GearmanWorker;

/**
 * The echo worker polls jobs from a job server and execute the echo function.
 * 
 * The echo worker illustrates how to setup a basic worker
 */
public class EchoWorker implements GearmanFunction {

        /** The echo function name */
		// function ����
        public static final String ECHO_FUNCTION_NAME = "echo";

        // job server �ĵ�ַ
        // Echo host Ϊ��װ��Gearman ������Gearman �����������ַ
        public static final String ECHO_HOST = "localhost";

        // job server�����Ķ˿�  Ĭ�ϵĶ˿�
        public static final int ECHO_PORT = 4730;

        public static void main(String... args) {

        		// ����һ��Gearman ʵ��
                Gearman gearman = Gearman.createGearman();

                // ����һ��job server
                // ����1��job server �ĵ�ַ
                // ����2��job server �����Ķ˿�
                // job server �յ� client ��job��������ַ���ע���worker
                // 
                GearmanServer server = gearman.createGearmanServer(
                                EchoWorker.ECHO_HOST, EchoWorker.ECHO_PORT);

                // ����һ�� Gearman worker
                GearmanWorker worker = gearman.createGearmanWorker();

                // ���� worker ���ִ�й���
                worker.addFunction(EchoWorker.ECHO_FUNCTION_NAME, new EchoWorker());

                // worker ���ӷ�����
                worker.addServer(server);
        }

//        // work ����ʵ���� GearmanFunction �ӿ��е�work ��������ʵ����ֻ�ǽ�ȡ�õ����ݷ���
//        @Override
//        public byte[] work(String function, byte[] data,
//                        GearmanFunctionCallback callback) throws Exception {
//        	
//                return data;
//        }
        
    	@Override
    	public byte[] work(String function, byte[] data,
    			GearmanFunctionCallback callback) throws Exception {

    		// work����ʵ����GearmanFunction�ӿ��е�work����,��ʵ���н������ַ����ķ�д
    		if (data != null) {
    			String str = new String(data);
    			StringBuffer sb = new StringBuffer(str);
    			return sb.reverse().toString().getBytes();
    		} else {
    			return "δ���յ�data".getBytes();
    		}

    	}

}