package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;

public class Struct {
  protected ByteBuffer bb;
  
  protected int bb_pos;
  
  public void __reset() {
    __reset(0, null);
  }
  
  protected void __reset(int paramInt, ByteBuffer paramByteBuffer) {
    this.bb = paramByteBuffer;
    if (paramByteBuffer != null) {
      this.bb_pos = paramInt;
    } else {
      this.bb_pos = 0;
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\flatbuffer\Struct.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */