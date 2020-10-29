package helloNetty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LoggingHandler;

/**
 * @program: sockExample
 * @description:这是一个初始化器 channel注册后 会执行里面的相应的初始化方法
 * @author: yetin
 * @create: 2020-10-29 17:31
 **/
public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        //通过channel获得pipline
        ChannelPipeline pipeline = channel.pipeline();

        pipeline.addLast("LoggingHandler",new LoggingHandler());

        //通过管道添加handle
        //当请求到服务器端,我们需要做解码,相应客户端做编码
        pipeline.addLast("HttpServerCodec",new HttpServerCodec());


        //添加自定义的助手类
        pipeline.addLast("custom handler",new CustomHandler());

    }
}
