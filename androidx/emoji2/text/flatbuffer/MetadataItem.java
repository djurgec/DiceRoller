package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class MetadataItem extends Table {
  public static void ValidateVersion() {
    Constants.FLATBUFFERS_1_12_0();
  }
  
  public static void addCodepoints(FlatBufferBuilder paramFlatBufferBuilder, int paramInt) {
    paramFlatBufferBuilder.addOffset(6, paramInt, 0);
  }
  
  public static void addCompatAdded(FlatBufferBuilder paramFlatBufferBuilder, short paramShort) {
    paramFlatBufferBuilder.addShort(3, paramShort, 0);
  }
  
  public static void addEmojiStyle(FlatBufferBuilder paramFlatBufferBuilder, boolean paramBoolean) {
    paramFlatBufferBuilder.addBoolean(1, paramBoolean, false);
  }
  
  public static void addHeight(FlatBufferBuilder paramFlatBufferBuilder, short paramShort) {
    paramFlatBufferBuilder.addShort(5, paramShort, 0);
  }
  
  public static void addId(FlatBufferBuilder paramFlatBufferBuilder, int paramInt) {
    paramFlatBufferBuilder.addInt(0, paramInt, 0);
  }
  
  public static void addSdkAdded(FlatBufferBuilder paramFlatBufferBuilder, short paramShort) {
    paramFlatBufferBuilder.addShort(2, paramShort, 0);
  }
  
  public static void addWidth(FlatBufferBuilder paramFlatBufferBuilder, short paramShort) {
    paramFlatBufferBuilder.addShort(4, paramShort, 0);
  }
  
  public static int createCodepointsVector(FlatBufferBuilder paramFlatBufferBuilder, int[] paramArrayOfint) {
    paramFlatBufferBuilder.startVector(4, paramArrayOfint.length, 4);
    for (int i = paramArrayOfint.length - 1; i >= 0; i--)
      paramFlatBufferBuilder.addInt(paramArrayOfint[i]); 
    return paramFlatBufferBuilder.endVector();
  }
  
  public static int createMetadataItem(FlatBufferBuilder paramFlatBufferBuilder, int paramInt1, boolean paramBoolean, short paramShort1, short paramShort2, short paramShort3, short paramShort4, int paramInt2) {
    paramFlatBufferBuilder.startTable(7);
    addCodepoints(paramFlatBufferBuilder, paramInt2);
    addId(paramFlatBufferBuilder, paramInt1);
    addHeight(paramFlatBufferBuilder, paramShort4);
    addWidth(paramFlatBufferBuilder, paramShort3);
    addCompatAdded(paramFlatBufferBuilder, paramShort2);
    addSdkAdded(paramFlatBufferBuilder, paramShort1);
    addEmojiStyle(paramFlatBufferBuilder, paramBoolean);
    return endMetadataItem(paramFlatBufferBuilder);
  }
  
  public static int endMetadataItem(FlatBufferBuilder paramFlatBufferBuilder) {
    return paramFlatBufferBuilder.endTable();
  }
  
  public static MetadataItem getRootAsMetadataItem(ByteBuffer paramByteBuffer) {
    return getRootAsMetadataItem(paramByteBuffer, new MetadataItem());
  }
  
  public static MetadataItem getRootAsMetadataItem(ByteBuffer paramByteBuffer, MetadataItem paramMetadataItem) {
    paramByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
    return paramMetadataItem.__assign(paramByteBuffer.getInt(paramByteBuffer.position()) + paramByteBuffer.position(), paramByteBuffer);
  }
  
  public static void startCodepointsVector(FlatBufferBuilder paramFlatBufferBuilder, int paramInt) {
    paramFlatBufferBuilder.startVector(4, paramInt, 4);
  }
  
  public static void startMetadataItem(FlatBufferBuilder paramFlatBufferBuilder) {
    paramFlatBufferBuilder.startTable(7);
  }
  
  public MetadataItem __assign(int paramInt, ByteBuffer paramByteBuffer) {
    __init(paramInt, paramByteBuffer);
    return this;
  }
  
  public void __init(int paramInt, ByteBuffer paramByteBuffer) {
    __reset(paramInt, paramByteBuffer);
  }
  
  public int codepoints(int paramInt) {
    int i = __offset(16);
    if (i != 0) {
      paramInt = this.bb.getInt(__vector(i) + paramInt * 4);
    } else {
      paramInt = 0;
    } 
    return paramInt;
  }
  
  public ByteBuffer codepointsAsByteBuffer() {
    return __vector_as_bytebuffer(16, 4);
  }
  
  public ByteBuffer codepointsInByteBuffer(ByteBuffer paramByteBuffer) {
    return __vector_in_bytebuffer(paramByteBuffer, 16, 4);
  }
  
  public int codepointsLength() {
    int i = __offset(16);
    if (i != 0) {
      i = __vector_len(i);
    } else {
      i = 0;
    } 
    return i;
  }
  
  public IntVector codepointsVector() {
    return codepointsVector(new IntVector());
  }
  
  public IntVector codepointsVector(IntVector paramIntVector) {
    int i = __offset(16);
    if (i != 0) {
      paramIntVector = paramIntVector.__assign(__vector(i), this.bb);
    } else {
      paramIntVector = null;
    } 
    return paramIntVector;
  }
  
  public short compatAdded() {
    boolean bool;
    int i = __offset(10);
    if (i != 0) {
      bool = this.bb.getShort(this.bb_pos + i);
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean emojiStyle() {
    int i = __offset(6);
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (i != 0) {
      bool1 = bool2;
      if (this.bb.get(this.bb_pos + i) != 0)
        bool1 = true; 
    } 
    return bool1;
  }
  
  public short height() {
    boolean bool;
    int i = __offset(14);
    if (i != 0) {
      bool = this.bb.getShort(this.bb_pos + i);
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int id() {
    int i = __offset(4);
    if (i != 0) {
      i = this.bb.getInt(this.bb_pos + i);
    } else {
      i = 0;
    } 
    return i;
  }
  
  public short sdkAdded() {
    boolean bool;
    int i = __offset(8);
    if (i != 0) {
      bool = this.bb.getShort(this.bb_pos + i);
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public short width() {
    boolean bool;
    int i = __offset(12);
    if (i != 0) {
      bool = this.bb.getShort(this.bb_pos + i);
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static final class Vector extends BaseVector {
    public Vector __assign(int param1Int1, int param1Int2, ByteBuffer param1ByteBuffer) {
      __reset(param1Int1, param1Int2, param1ByteBuffer);
      return this;
    }
    
    public MetadataItem get(int param1Int) {
      return get(new MetadataItem(), param1Int);
    }
    
    public MetadataItem get(MetadataItem param1MetadataItem, int param1Int) {
      return param1MetadataItem.__assign(MetadataItem.__indirect(__element(param1Int), this.bb), this.bb);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\flatbuffer\MetadataItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */