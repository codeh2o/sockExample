package bio_chartRoom.client;

import java.io.*;
import java.net.Socket;

/**
 * @program: socket
 * @description:
 * @author: h2o
 * @create: 2020-10-16 23:47
 **/
public class ChatClient {
    private final String QUIT = "quit";
    private final int DEFAULT_PORT = 8888;
    private final String DEFAULT_SERVER_HOST = "127.0.0.1";

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    //发送消息给服务器
    public void send(String msg) throws IOException {
        if(!socket.isOutputShutdown()){
            writer.write(msg+"\n");
            writer.flush();
        }
    }
    //从服务器端接收消息
    public String receive() throws IOException {
        String msg =null;
        if (!socket.isInputShutdown()){
            msg = reader.readLine();
        }
        return msg;
    }
    // 检查用户是否准备退出
    public boolean readyToQuit(String msg){
        return QUIT.equals(msg);
    }

    public void close(){
        if (writer!=null){
            try {
                System.out.println("关闭socket");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void start(){

        try {
            //创建socket对象
            socket = new Socket(DEFAULT_SERVER_HOST, DEFAULT_PORT);

            //创建IO流
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            //处理用户输入
            new Thread(new ChatInputHandler(this)).start();

            //读取服务器转发的消息
            String msg = null;

            while ((msg=receive())!=null){
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
        chatClient.start();
    }
}
