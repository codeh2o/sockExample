package ChatExample;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

/**
 * @program: sockExample
 * @description:处理消息的handler 在netty中为websocket处理文本的对象 frame是消息的载体
 * @author: yetin
 * @create: 2020-10-29 19:57
 **/
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //用来保存客户端channel
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //获取客户端传输过来的消息
        String content = msg.text();
        System.out.println("接收到的数据"+content);

        for (Channel channel:clients) {

            channel.writeAndFlush(new TextWebSocketFrame("[服务器接收到的消息]"+ LocalDateTime.now()+",消息为"+content));
        }

        //下面的方法和上面的一致
        //clients.writeAndFlush(new TextWebSocketFrame("[服务器接收到的消息]"+ LocalDateTime.now()+",消息为"+content));
    }
    
    /*
     * @Description: 当客户端连接服务端后获取客户端channel并放到ChannelGroup中
     * @param ctx
     * @return: void
     * @Author: yetin
     * @Date: 2020/10/29 20:04
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // handle被移除 ChannelGroup自定会把对应的channel移除
        //clients.remove(ctx.channel());
        System.out.println("客户端断开 channel对应的长id为:"+ctx.channel().id().asLongText());
        System.out.println("客户端断开 channel对应的长id为:"+ctx.channel().id().asShortText());
    }
}
