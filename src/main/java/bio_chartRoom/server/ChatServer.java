package bio_chartRoom.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: socket
 * @description:
 * @author: h2o
 * @create: 2020-10-16 23:47
 **/
public class ChatServer {
    private final String QUIT = "quit";
    private final int DEFAULT_PORT = 8888;
    private ExecutorService executorService;

    private ServerSocket serverSocket;
    private Map<Integer, Writer> connectedClient;


    public ChatServer() {
        executorService = Executors.newFixedThreadPool(10);
        connectedClient = new HashMap<Integer, Writer>();
    }

    public synchronized void addClient(Socket socket) throws IOException {
        if (socket != null) {
            int port = socket.getPort();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            connectedClient.put(port, writer);
            System.out.println("客户端[" + port + "]连接到服务器");
        }
    }

    public synchronized void removeClient(Socket socket) throws IOException {
        if (socket != null) {
            int port = socket.getPort();
            if (connectedClient.containsKey(port)) {
                connectedClient.get(port).close();
            }
            connectedClient.remove(port);
            System.out.println("客户端[" + port + "]断开连接");
        }
    }

    public synchronized void forwardMessage(Socket socket, String fwdMsg) throws IOException {
        for (Integer id :
                connectedClient.keySet()) {
            if (!id.equals(socket.getPort())){
                Writer writer = connectedClient.get(id);
                writer.write(fwdMsg);
                writer.flush();
            }

        }

    }

    public synchronized void close(){
        if(serverSocket!=null){
            try {
                serverSocket.close();
                System.out.println("关闭了serverSocket");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean readyToQuit(String msg){
        return QUIT.equals(msg);
    }

    public void start(){
        //绑定监听端口
        try {

            ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("服务器自动了，正在监听端口:"+DEFAULT_PORT);

            while (true){
                //等待客户端连接
                Socket socket = serverSocket.accept();

                //创建ChatHandler
                executorService.execute(new ChatHandler(this, socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.start();
    }

}
