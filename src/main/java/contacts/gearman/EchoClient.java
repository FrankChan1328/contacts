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

                // 创建一个 Gearman client
                // 用来向 job server 提交请求
                GearmanClient client = gearman.createGearmanClient();

                // 创建一个 job server 对象，该对象代表 remote job server.
                // job server 从clients 得到jobs 然后分发给注册workers
                GearmanServer server = gearman.createGearmanServer(
                                EchoWorker.ECHO_HOST, EchoWorker.ECHO_PORT);

                // client 连接server
                client.addServer(server);

                // 将 job submit 给server
                // 参数1：function 名称
                // 参数2：将要传给server 和 worker 的数据
                // GearmanJobReturn 用来取得 job 的结果
                GearmanJobReturn jobReturn = client.submitJob(
                                EchoWorker.ECHO_FUNCTION_NAME, ("Hello World").getBytes());

                // 遍历job 事件，直到文件末尾
                while (!jobReturn.isEOF()) {

                		// 获得下个job，(阻塞操作)
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