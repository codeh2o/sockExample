package basic;

import java.io.*;
import java.net.Socket;

/**
 * @program: socket
 * @description:
 * @author: h2o
 * @create: 2020-10-11 08:48
 **/
public class Client {
    public static void main(String[] args) {
        final String QUIT = "quit";
        final String DEFAULT_SERVER_HOST = "127.0.0.1";
        final int DEFAULT_PORT = 8888;

        Socket socket = null;
        BufferedWriter writer = null;

        try {
            socket = new Socket(DEFAULT_SERVER_HOST,DEFAULT_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //等待用户输入信息

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            while (true){
                String input = consoleReader.readLine();

                //发送给服务器
                writer.write(input+"\n");
                writer.flush();

                //读取服务器返回的消息
                String msg = reader.readLine();
                System.out.println(msg);

                //检查用户是否退出
                if(QUIT.equals(input)){
                    break;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(writer!=null){
                try {
                    //自动会关闭socket
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
