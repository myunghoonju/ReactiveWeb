package practice.reactiveWeb.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class ByteBufferTest {

   @Test
   public void unpooled_heapbuffer_test() throws Exception {
       ByteBuf buffer = Unpooled.buffer(11);
        findBufferDetail(buffer, false);
   }

   @Test
   public void unpooled_dirbuffer_test() throws Exception {
       ByteBuf buffer = Unpooled.directBuffer(11);
        findBufferDetail(buffer, true);
   }

    @Test
    public void createPooledHeapBufferTest() {
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.heapBuffer(11);
        findBufferDetail(buf, false);
    }

    @Test
    public void createPooledDirectBufferTest() {
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(11);
        findBufferDetail(buf, true);
    }

   private void findBufferDetail(ByteBuf buf, boolean isDir) {
       assertEquals(11, buf.capacity());

       assertEquals(isDir, buf.isDirect());

       assertEquals(0, buf.readableBytes());
       assertEquals(11, buf.writableBytes());
   }
}
