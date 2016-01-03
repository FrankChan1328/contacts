package contacts.gearman;

import org.gearman.Gearman;
import org.gearman.GearmanClient;
import org.gearman.GearmanJobEvent;
import org.gearman.GearmanJobReturn;
import org.gearman.GearmanServer;

/**
 * The echo client submits an "echo" job to a job server and prints the final
 * result. It's the "Hello World" of the java-geraman-service
 * 
 * The echo example illustrates how send a single job and get the result
 */
public class EchoClient {

        public static void main(String... args) throws InterruptedException {

                Gearman gearman = Gearman.createGearman();

                // ����һ�� Gearman client
                // ������ job server �ύ����
                GearmanClient client = gearman.createGearmanClient();

                // ����һ�� job server ���󣬸ö������ remote job server.
                // job server ��clients �õ�jobs Ȼ��ַ���ע��workers
                GearmanServer server = gearman.createGearmanServer(
                                EchoWorker.ECHO_HOST, EchoWorker.ECHO_PORT);

                // client ����server
                client.addServer(server);

                // �� job submit ��server
                // ����1��function ����
                // ����2����Ҫ����server �� worker ������
                // GearmanJobReturn ����ȡ�� job �Ľ��
                GearmanJobReturn jobReturn = client.submitJob(
                                EchoWorker.ECHO_FUNCTION_NAME, ("Hello World").getBytes());

                // ����job �¼���ֱ���ļ�ĩβ
                while (!jobReturn.isEOF()) {

                		// ����¸�job��(��������)
                        GearmanJobEvent event = jobReturn.poll();

                        switch (event.getEventType()) {

                        // success
                        case GEARMAN_JOB_SUCCESS: // Job completed successfully
                                // print the result
                                System.out.println(new String(event.getData()));
                                break;

                        // failure
                        case GEARMAN_SUBMIT_FAIL: // The job submit operation failed
                        case GEARMAN_JOB_FAIL: // The job's execution failed
                                System.err.println(event.getEventType() + ": "
                                                + new String(event.getData()));

                        }

                }

                /*
                 * Close the gearman service after it's no longer needed. (closes all
                 * sub-services, such as the client)
                 * 
                 * It's suggested that you reuse Gearman and GearmanClient instances
                 * rather recreating and closing new ones between submissions
                 */
                gearman.shutdown();
        }
}