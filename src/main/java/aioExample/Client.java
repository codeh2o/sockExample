package aioExample;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.AsynchronousServerSocketChannel;

/**
 * @program: sockExample
 * @description:
 * @author: yetin
 * @create: 2020-10-23 09:58
 **/
public class Client {
    final String LOCALHOST = "localhost";
    final int DEFAULT_PORT = 8888;

    AsynchronousServerSocketChannel serverChannel;

    private void close(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
                System.out.println("关闭" + closable);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start(){
    }
}
