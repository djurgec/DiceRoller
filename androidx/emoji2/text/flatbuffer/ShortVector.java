package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;

public final class ShortVector extends BaseVector {
  public ShortVector __assign(int paramInt, ByteBuffer paramByteBuffer) {
    __reset(paramInt, 2, paramByteBuffer);
    return this;
  }
  
  public short get(int paramInt) {
    return this.bb.getShort(__element(paramInt));
  }
  
  public int getAsUnsigned(int paramInt) {
    return get(paramInt) & 0xFFFF;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\flatbuffer\ShortVector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */