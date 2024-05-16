package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;

public final class IntVector extends BaseVector {
  public IntVector __assign(int paramInt, ByteBuffer paramByteBuffer) {
    __reset(paramInt, 4, paramByteBuffer);
    return this;
  }
  
  public int get(int paramInt) {
    return this.bb.getInt(__element(paramInt));
  }
  
  public long getAsUnsigned(int paramInt) {
    return get(paramInt) & 0xFFFFFFFFL;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\flatbuffer\IntVector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */