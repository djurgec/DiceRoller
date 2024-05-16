package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;

public final class DoubleVector extends BaseVector {
  public DoubleVector __assign(int paramInt, ByteBuffer paramByteBuffer) {
    __reset(paramInt, 8, paramByteBuffer);
    return this;
  }
  
  public double get(int paramInt) {
    return this.bb.getDouble(__element(paramInt));
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\flatbuffer\DoubleVector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */