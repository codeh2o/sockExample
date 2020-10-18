package basic;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @program: socket
 * @description:
 * @author: h2o
 * @create: 2020-10-09 23:09
 **/
public class Server {
    public static void main(String[] args) {
        final String QUIT = "quit";
        final int DEFAULT_PORT = 8888;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("启动了服务器，正在监听"+DEFAULT_PORT);
            while (true){
                Socket socket = serverSocket.accept();//阻塞直到收到请求
                System.out.println("客户端["+socket.getPort()+"]已经连接");
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                //读取客户端发送的消息
                String msg = null;

                while ((msg=reader.readLine())!=null){
                    System.out.println("客户端["+socket.getPort()+"]:"+msg);
                    writer.write("服务器:"+msg+"\n");
                    writer.flush();
                    if(QUIT.equals(msg)){
                        System.out.println("客户端["+socket.getPort()+"]断开链接了");
                        break;
                    }
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(serverSocket!=null){
                try {
                    serverSocket.close();
                    System.out.println("关闭serverSocket");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
