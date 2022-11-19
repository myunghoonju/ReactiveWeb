package practice.reactiveWeb.netty.pipeLine;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EchoServerHandlerV1 extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf read = (ByteBuf) msg;
        log.info("channel read:: {}", read.toString());
        ctx.write(msg);
        ctx.fireChannelRead(msg); // create a new event:: invoke next handler(EchoServerHandlerV2)
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
