package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;

public final class BooleanVector extends BaseVector {
  public BooleanVector __assign(int paramInt, ByteBuffer paramByteBuffer) {
    __reset(paramInt, 1, paramByteBuffer);
    return this;
  }
  
  public boolean get(int paramInt) {
    boolean bool;
    if (this.bb.get(__element(paramInt)) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\flatbuffer\BooleanVector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */