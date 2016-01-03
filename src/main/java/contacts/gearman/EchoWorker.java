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
		// function 名称
        public static final String ECHO_FUNCTION_NAME = "echo";

        // job server 的地址
        // Echo host 为安装了Gearman 并开启Gearman 服务的主机地址
        public static final String ECHO_HOST = "localhost";

        // job server监听的端口  默认的端口
        public static final int ECHO_PORT = 4730;

        public static void main(String... args) {

        		// 创建一个Gearman 实例
                Gearman gearman = Gearman.createGearman();

                // 创建一个job server
                // 参数1：job server 的地址
                // 参数2：job server 监听的端口
                // job server 收到 client 的job，并将其分发给注册的worker
                // 
                GearmanServer server = gearman.createGearmanServer(
                                EchoWorker.ECHO_HOST, EchoWorker.ECHO_PORT);

                // 创建一个 Gearman worker
                GearmanWorker worker = gearman.createGearmanWorker();

                // 告诉 worker 如何执行工作
                worker.addFunction(EchoWorker.ECHO_FUNCTION_NAME, new EchoWorker());

                // worker 连接服务器
                worker.addServer(server);
        }

//        // work 方法实现了 GearmanFunction 接口中的work 方法，本实例中只是将取得的数据返回
//        @Override
//        public byte[] work(String function, byte[] data,
//                        GearmanFunctionCallback callback) throws Exception {
//        	
//                return data;
//        }
        
    	@Override
    	public byte[] work(String function, byte[] data,
    			GearmanFunctionCallback callback) throws Exception {

    		// work方法实现了GearmanFunction接口中的work方法,本实例中进行了字符串的反写
    		if (data != null) {
    			String str = new String(data);
    			StringBuffer sb = new StringBuffer(str);
    			return sb.reverse().toString().getBytes();
    		} else {
    			return "未接收到data".getBytes();
    		}

    	}

}