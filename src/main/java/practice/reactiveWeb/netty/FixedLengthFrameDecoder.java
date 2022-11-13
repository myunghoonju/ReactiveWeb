package practice.reactiveWeb.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class FixedLengthFrameDecoder extends ByteToMessageDecoder {

    private final int frameLength;

    public FixedLengthFrameDecoder(int frameLength) {
        if (frameLength <= 0) {
            throw new IllegalArgumentException("not enough length:: " + frameLength);
        }
        this.frameLength = frameLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf input,
                          List<Object> output) throws Exception {
        while (input.readableBytes() >= frameLength) {
            ByteBuf byteBuf = input.readBytes(frameLength);
            output.add(byteBuf);
        }
    }
}
