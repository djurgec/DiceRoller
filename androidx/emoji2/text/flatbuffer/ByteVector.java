package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;

public final class ByteVector extends BaseVector {
  public ByteVector __assign(int paramInt, ByteBuffer paramByteBuffer) {
    __reset(paramInt, 1, paramByteBuffer);
    return this;
  }
  
  public byte get(int paramInt) {
    return this.bb.get(__element(paramInt));
  }
  
  public int getAsUnsigned(int paramInt) {
    return get(paramInt) & 0xFF;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\flatbuffer\ByteVector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */