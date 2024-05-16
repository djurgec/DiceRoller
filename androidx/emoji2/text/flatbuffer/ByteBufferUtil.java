package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;

public class ByteBufferUtil {
  public static int getSizePrefix(ByteBuffer paramByteBuffer) {
    return paramByteBuffer.getInt(paramByteBuffer.position());
  }
  
  public static ByteBuffer removeSizePrefix(ByteBuffer paramByteBuffer) {
    paramByteBuffer = paramByteBuffer.duplicate();
    paramByteBuffer.position(paramByteBuffer.position() + 4);
    return paramByteBuffer;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\flatbuffer\ByteBufferUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */