package bio_chartRoom.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @program: socket
 * @description:
 * @author: h2o
 * @create: 2020-10-16 23:47
 **/
public class ChatHandler implements Runnable {
    private ChatServer server;
    private Socket socket;

    public ChatHandler(ChatServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    public void run() {
        try {
            //储存新上线用户
            server.addClient(socket);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg = null;
            while ((msg = reader.readLine()) != null) {
                String forwardMessage = "客户端[" + socket.getPort() + "]:" + msg + "\n";
                System.out.println(forwardMessage);

                //将消息转发给聊天室里的其他在线用户
                server.forwardMessage(socket, forwardMessage);

                //检查用户是否决定退出
                if(server.readyToQuit(msg)){
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                server.removeClient(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
