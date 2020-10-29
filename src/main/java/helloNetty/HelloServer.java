package helloNetty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @program: sockExample
 * @description:
 * @author: yetin
 * @create: 2020-10-29 13:45
 **/
public class HelloServer {

    public static void main(String[] args) {
        //定义1对线程组
        //主线程组,用于接受客户端的连接,但是不做任何处理
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        //从线程组,老板线程组会把任务交给wokerGroup
        EventLoopGroup wokerGroup = new NioEventLoopGroup();




        try {

            //netty服务器的创建
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,wokerGroup)
                    .channel(NioServerSocketChannel.class) //设置nio双向通道
                    .childHandler(new HelloServerInitializer());

            //设置端口号 并设置为同步
            ChannelFuture channelFuture = serverBootstrap.bind(8079).sync();

            //监听关闭 channel.设置同步方式
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            wokerGroup.shutdownGracefully();
        }

    }
}
