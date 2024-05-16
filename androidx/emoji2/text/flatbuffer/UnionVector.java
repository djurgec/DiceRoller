package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;

public final class UnionVector extends BaseVector {
  public UnionVector __assign(int paramInt1, int paramInt2, ByteBuffer paramByteBuffer) {
    __reset(paramInt1, paramInt2, paramByteBuffer);
    return this;
  }
  
  public Table get(Table paramTable, int paramInt) {
    return Table.__union(paramTable, __element(paramInt), this.bb);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\flatbuffer\UnionVector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */