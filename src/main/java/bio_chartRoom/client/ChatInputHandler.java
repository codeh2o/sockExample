package bio_chartRoom.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @program: socket
 * @description:
 * @author: h2o
 * @create: 2020-10-16 23:48
 **/
public class ChatInputHandler implements Runnable {

    private ChatClient chatClient;
    private final String QUIT = "quit";

    public ChatInputHandler(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public void run() {
        //等待用户输入消息
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));


        try {
            while (true) {
                String input = consoleReader.readLine();

                //向服务器发送消息
                chatClient.send(input);


                //检查用户是否准备退出
                if(QUIT.equals(input)){
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }


    }
}
