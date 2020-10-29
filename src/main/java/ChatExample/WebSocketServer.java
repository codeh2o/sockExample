package ChatExample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @program: sockExample
 * @description:
 * @author: yetin
 * @create: 2020-10-29 19:40
 **/
public class WebSocketServer {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup mainGroup = new NioEventLoopGroup();
        NioEventLoopGroup subGroup = new NioEventLoopGroup();


        ChannelFuture future = null;
        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(mainGroup,subGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new WSServerInitializer());

            future = serverBootstrap.bind(9999).sync();
            future.channel().closeFuture().sync();
        }  finally {
            mainGroup.shutdownGracefully();
            subGroup.shutdownGracefully();
        }

    }
}
