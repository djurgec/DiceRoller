package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;

public final class FloatVector extends BaseVector {
  public FloatVector __assign(int paramInt, ByteBuffer paramByteBuffer) {
    __reset(paramInt, 4, paramByteBuffer);
    return this;
  }
  
  public float get(int paramInt) {
    return this.bb.getFloat(__element(paramInt));
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\flatbuffer\FloatVector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */