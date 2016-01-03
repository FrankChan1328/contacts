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
 * echo worker ˵������δ��� basic worker
 */
public class EchoWorkerServer {
	public static void main(String... args) throws IOException {

		// ����һ��Gearman ʵ��
		Gearman gearman = Gearman.createGearman();

		try {

			// ����һ���µ� job server.
			// �ڱ�������
			// ����Ϊ�����˿�
			GearmanServer server = gearman.startGearmanServer(EchoWorker.ECHO_PORT);

			// ����һ�� gearman worker.
			// ��server ������Ȼ��ִ����Ӧ�� GearmanFunction
			GearmanWorker worker = gearman.createGearmanWorker();

			// ����worker ���ִ�й���(��Ҫʵ���� GearmanFunction �ӿ�)
			worker.addFunction(EchoWorker.ECHO_FUNCTION_NAME, new EchoWorker());

			// worker ���ӷ�����
			worker.addServer(server);

		} catch (IOException ioe) {
			// ���ִ��󣬹ر� gearman service
			gearman.shutdown();

			throw ioe;
		}
	}
}