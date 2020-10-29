package ChatExample;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @program: sockExample
 * @description:
 * @author: yetin
 * @create: 2020-10-29 19:45
 **/
public class WSServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //web socket基于http协议,所以需要http编解码器
        pipeline.addLast(new HttpServerCodec());

        //对写大数量流的支持
        pipeline.addLast(new ChunkedWriteHandler());

        //对http message 进行聚合 聚合了FullHttpRequest,FullHttpResponse
        pipeline.addLast(new HttpObjectAggregator(1024*64));


        //================================以上是http协议支持=================================


        //websocket 服务器处理的协议,并指定客户端访问的路由
        /*
         * @Description: 本handler会处理繁重复杂的事 比如握手 心跳 close
         * 对websocket来讲都是以frames进行传输,不同数据类型对应的frame也不同
         * @param ch
         * @return: void
         * @Author: yetin
         * @Date: 2020/10/29 19:54
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        //自定义handler
        pipeline.addLast(new ChatHandler());
    }
}
