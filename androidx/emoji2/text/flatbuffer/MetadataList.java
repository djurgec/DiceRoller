package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class MetadataList extends Table {
  public static void ValidateVersion() {
    Constants.FLATBUFFERS_1_12_0();
  }
  
  public static void addList(FlatBufferBuilder paramFlatBufferBuilder, int paramInt) {
    paramFlatBufferBuilder.addOffset(1, paramInt, 0);
  }
  
  public static void addSourceSha(FlatBufferBuilder paramFlatBufferBuilder, int paramInt) {
    paramFlatBufferBuilder.addOffset(2, paramInt, 0);
  }
  
  public static void addVersion(FlatBufferBuilder paramFlatBufferBuilder, int paramInt) {
    paramFlatBufferBuilder.addInt(0, paramInt, 0);
  }
  
  public static int createListVector(FlatBufferBuilder paramFlatBufferBuilder, int[] paramArrayOfint) {
    paramFlatBufferBuilder.startVector(4, paramArrayOfint.length, 4);
    for (int i = paramArrayOfint.length - 1; i >= 0; i--)
      paramFlatBufferBuilder.addOffset(paramArrayOfint[i]); 
    return paramFlatBufferBuilder.endVector();
  }
  
  public static int createMetadataList(FlatBufferBuilder paramFlatBufferBuilder, int paramInt1, int paramInt2, int paramInt3) {
    paramFlatBufferBuilder.startTable(3);
    addSourceSha(paramFlatBufferBuilder, paramInt3);
    addList(paramFlatBufferBuilder, paramInt2);
    addVersion(paramFlatBufferBuilder, paramInt1);
    return endMetadataList(paramFlatBufferBuilder);
  }
  
  public static int endMetadataList(FlatBufferBuilder paramFlatBufferBuilder) {
    return paramFlatBufferBuilder.endTable();
  }
  
  public static void finishMetadataListBuffer(FlatBufferBuilder paramFlatBufferBuilder, int paramInt) {
    paramFlatBufferBuilder.finish(paramInt);
  }
  
  public static void finishSizePrefixedMetadataListBuffer(FlatBufferBuilder paramFlatBufferBuilder, int paramInt) {
    paramFlatBufferBuilder.finishSizePrefixed(paramInt);
  }
  
  public static MetadataList getRootAsMetadataList(ByteBuffer paramByteBuffer) {
    return getRootAsMetadataList(paramByteBuffer, new MetadataList());
  }
  
  public static MetadataList getRootAsMetadataList(ByteBuffer paramByteBuffer, MetadataList paramMetadataList) {
    paramByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
    return paramMetadataList.__assign(paramByteBuffer.getInt(paramByteBuffer.position()) + paramByteBuffer.position(), paramByteBuffer);
  }
  
  public static void startListVector(FlatBufferBuilder paramFlatBufferBuilder, int paramInt) {
    paramFlatBufferBuilder.startVector(4, paramInt, 4);
  }
  
  public static void startMetadataList(FlatBufferBuilder paramFlatBufferBuilder) {
    paramFlatBufferBuilder.startTable(3);
  }
  
  public MetadataList __assign(int paramInt, ByteBuffer paramByteBuffer) {
    __init(paramInt, paramByteBuffer);
    return this;
  }
  
  public void __init(int paramInt, ByteBuffer paramByteBuffer) {
    __reset(paramInt, paramByteBuffer);
  }
  
  public MetadataItem list(int paramInt) {
    return list(new MetadataItem(), paramInt);
  }
  
  public MetadataItem list(MetadataItem paramMetadataItem, int paramInt) {
    int i = __offset(6);
    if (i != 0) {
      paramMetadataItem = paramMetadataItem.__assign(__indirect(__vector(i) + paramInt * 4), this.bb);
    } else {
      paramMetadataItem = null;
    } 
    return paramMetadataItem;
  }
  
  public int listLength() {
    int i = __offset(6);
    if (i != 0) {
      i = __vector_len(i);
    } else {
      i = 0;
    } 
    return i;
  }
  
  public MetadataItem.Vector listVector() {
    return listVector(new MetadataItem.Vector());
  }
  
  public MetadataItem.Vector listVector(MetadataItem.Vector paramVector) {
    int i = __offset(6);
    if (i != 0) {
      paramVector = paramVector.__assign(__vector(i), 4, this.bb);
    } else {
      paramVector = null;
    } 
    return paramVector;
  }
  
  public String sourceSha() {
    String str;
    int i = __offset(8);
    if (i != 0) {
      str = __string(this.bb_pos + i);
    } else {
      str = null;
    } 
    return str;
  }
  
  public ByteBuffer sourceShaAsByteBuffer() {
    return __vector_as_bytebuffer(8, 1);
  }
  
  public ByteBuffer sourceShaInByteBuffer(ByteBuffer paramByteBuffer) {
    return __vector_in_bytebuffer(paramByteBuffer, 8, 1);
  }
  
  public int version() {
    int i = __offset(4);
    if (i != 0) {
      i = this.bb.getInt(this.bb_pos + i);
    } else {
      i = 0;
    } 
    return i;
  }
  
  public static final class Vector extends BaseVector {
    public Vector __assign(int param1Int1, int param1Int2, ByteBuffer param1ByteBuffer) {
      __reset(param1Int1, param1Int2, param1ByteBuffer);
      return this;
    }
    
    public MetadataList get(int param1Int) {
      return get(new MetadataList(), param1Int);
    }
    
    public MetadataList get(MetadataList param1MetadataList, int param1Int) {
      return param1MetadataList.__assign(MetadataList.__indirect(__element(param1Int), this.bb), this.bb);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\flatbuffer\MetadataList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */