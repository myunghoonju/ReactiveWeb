package practice.reactiveWeb.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DecoderTest {

    @Test
    void frameChunkDecoder_test() {
        //given
        ByteBuf buffer = Unpooled.buffer();
        for (int input = 0; input < 9; input++) {
            buffer.writeByte(input);
        }
        ByteBuf input = buffer.duplicate();

        //when
        EmbeddedChannel ch = new EmbeddedChannel(new FrameChunkDecoder(3));

        //then
        assertTrue(ch.writeInbound(input.readBytes(2)));
        assertThatThrownBy(() -> {
            ch.writeInbound(input.readBytes(5));
        }).isInstanceOf(TooLongFrameException.class);

        ByteBuf read = (ByteBuf)ch.readInbound();
        read.release();
        buffer.release();
    }

    @Test
    void fixedLengthFrameDecoder_test() {
        //given
        ByteBuf buffer = Unpooled.buffer();
        for (int input = 0; input < 9; input++) {
            buffer.writeByte(input);
        }
        ByteBuf input = buffer.duplicate();

        //when
        EmbeddedChannel ch = new EmbeddedChannel(new FixedLengthFrameDecoder(3));

        //then
        assertFalse(ch.writeInbound(input.readBytes(2)));
        assertTrue(ch.writeInbound(input.readBytes(3)));

        ByteBuf read = (ByteBuf)ch.readInbound();
        read.release();
        buffer.release();
    }
}