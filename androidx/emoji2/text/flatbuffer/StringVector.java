package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;

public final class StringVector extends BaseVector {
  private Utf8 utf8 = Utf8.getDefault();
  
  public StringVector __assign(int paramInt1, int paramInt2, ByteBuffer paramByteBuffer) {
    __reset(paramInt1, paramInt2, paramByteBuffer);
    return this;
  }
  
  public String get(int paramInt) {
    return Table.__string(__element(paramInt), this.bb, this.utf8);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\flatbuffer\StringVector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */