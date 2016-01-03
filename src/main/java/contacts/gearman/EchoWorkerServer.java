package contacts.gearman;

import java.io.IOException;

import org.gearman.Gearman;
import org.gearman.GearmanServer;
import org.gearman.GearmanWorker;

/**
 * The echo worker/server starts a new server and polls jobs from it job server
 * 
 * The echo worker illustrates how to setup a basic worker
 */
/**
 * 
 * echo worker 说明了如何创建 basic worker
 */
public class EchoWorkerServer {
	public static void main(String... args) throws IOException {

		// 创建一个Gearman 实例
		Gearman gearman = Gearman.createGearman();

		try {

			// 启动一个新的 job server.
			// 在本机启动
			// 参数为监听端口
			GearmanServer server = gearman.startGearmanServer(EchoWorker.ECHO_PORT);

			// 创建一个 gearman worker.
			// 从server 拉任务，然后执行相应的 GearmanFunction
			GearmanWorker worker = gearman.createGearmanWorker();

			// 告诉worker 如何执行工作(主要实现了 GearmanFunction 接口)
			worker.addFunction(EchoWorker.ECHO_FUNCTION_NAME, new EchoWorker());

			// worker 连接服务器
			worker.addServer(server);

		} catch (IOException ioe) {
			// 出现错误，关闭 gearman service
			gearman.shutdown();

			throw ioe;
		}
	}
}