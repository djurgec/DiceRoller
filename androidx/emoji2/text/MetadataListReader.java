package androidx.emoji2.text;

import android.content.res.AssetManager;
import androidx.emoji2.text.flatbuffer.MetadataList;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class MetadataListReader {
  private static final int EMJI_TAG = 1164798569;
  
  private static final int EMJI_TAG_DEPRECATED = 1701669481;
  
  private static final int META_TABLE_NAME = 1835365473;
  
  private static OffsetInfo findOffsetInfo(OpenTypeReader paramOpenTypeReader) throws IOException {
    paramOpenTypeReader.skip(4);
    int i = paramOpenTypeReader.readUnsignedShort();
    if (i <= 100) {
      long l1;
      paramOpenTypeReader.skip(6);
      long l2 = -1L;
      byte b = 0;
      while (true) {
        l1 = l2;
        if (b < i) {
          int j = paramOpenTypeReader.readTag();
          paramOpenTypeReader.skip(4);
          l1 = paramOpenTypeReader.readUnsignedInt();
          paramOpenTypeReader.skip(4);
          if (1835365473 == j)
            break; 
          b++;
          continue;
        } 
        break;
      } 
      if (l1 != -1L) {
        paramOpenTypeReader.skip((int)(l1 - paramOpenTypeReader.getPosition()));
        paramOpenTypeReader.skip(12);
        long l = paramOpenTypeReader.readUnsignedInt();
        for (b = 0; b < l; b++) {
          i = paramOpenTypeReader.readTag();
          long l3 = paramOpenTypeReader.readUnsignedInt();
          l2 = paramOpenTypeReader.readUnsignedInt();
          if (1164798569 == i || 1701669481 == i)
            return new OffsetInfo(l3 + l1, l2); 
        } 
      } 
      throw new IOException("Cannot read metadata.");
    } 
    throw new IOException("Cannot read metadata.");
  }
  
  static MetadataList read(AssetManager paramAssetManager, String paramString) throws IOException {
    InputStream inputStream = paramAssetManager.open(paramString);
    try {
      return read(inputStream);
    } finally {
      if (inputStream != null)
        try {
          inputStream.close();
        } finally {
          inputStream = null;
        }  
    } 
  }
  
  static MetadataList read(InputStream paramInputStream) throws IOException {
    InputStreamOpenTypeReader inputStreamOpenTypeReader = new InputStreamOpenTypeReader(paramInputStream);
    OffsetInfo offsetInfo = findOffsetInfo(inputStreamOpenTypeReader);
    inputStreamOpenTypeReader.skip((int)(offsetInfo.getStartOffset() - inputStreamOpenTypeReader.getPosition()));
    ByteBuffer byteBuffer = ByteBuffer.allocate((int)offsetInfo.getLength());
    int i = paramInputStream.read(byteBuffer.array());
    if (i == offsetInfo.getLength())
      return MetadataList.getRootAsMetadataList(byteBuffer); 
    throw new IOException("Needed " + offsetInfo.getLength() + " bytes, got " + i);
  }
  
  static MetadataList read(ByteBuffer paramByteBuffer) throws IOException {
    paramByteBuffer = paramByteBuffer.duplicate();
    paramByteBuffer.position((int)findOffsetInfo(new ByteBufferReader(paramByteBuffer)).getStartOffset());
    return MetadataList.getRootAsMetadataList(paramByteBuffer);
  }
  
  static long toUnsignedInt(int paramInt) {
    return paramInt & 0xFFFFFFFFL;
  }
  
  static int toUnsignedShort(short paramShort) {
    return 0xFFFF & paramShort;
  }
  
  private static class ByteBufferReader implements OpenTypeReader {
    private final ByteBuffer mByteBuffer;
    
    ByteBufferReader(ByteBuffer param1ByteBuffer) {
      this.mByteBuffer = param1ByteBuffer;
      param1ByteBuffer.order(ByteOrder.BIG_ENDIAN);
    }
    
    public long getPosition() {
      return this.mByteBuffer.position();
    }
    
    public int readTag() throws IOException {
      return this.mByteBuffer.getInt();
    }
    
    public long readUnsignedInt() throws IOException {
      return MetadataListReader.toUnsignedInt(this.mByteBuffer.getInt());
    }
    
    public int readUnsignedShort() throws IOException {
      return MetadataListReader.toUnsignedShort(this.mByteBuffer.getShort());
    }
    
    public void skip(int param1Int) throws IOException {
      ByteBuffer byteBuffer = this.mByteBuffer;
      byteBuffer.position(byteBuffer.position() + param1Int);
    }
  }
  
  private static class InputStreamOpenTypeReader implements OpenTypeReader {
    private final byte[] mByteArray;
    
    private final ByteBuffer mByteBuffer;
    
    private final InputStream mInputStream;
    
    private long mPosition = 0L;
    
    InputStreamOpenTypeReader(InputStream param1InputStream) {
      this.mInputStream = param1InputStream;
      byte[] arrayOfByte = new byte[4];
      this.mByteArray = arrayOfByte;
      ByteBuffer byteBuffer = ByteBuffer.wrap(arrayOfByte);
      this.mByteBuffer = byteBuffer;
      byteBuffer.order(ByteOrder.BIG_ENDIAN);
    }
    
    private void read(int param1Int) throws IOException {
      if (this.mInputStream.read(this.mByteArray, 0, param1Int) == param1Int) {
        this.mPosition += param1Int;
        return;
      } 
      throw new IOException("read failed");
    }
    
    public long getPosition() {
      return this.mPosition;
    }
    
    public int readTag() throws IOException {
      this.mByteBuffer.position(0);
      read(4);
      return this.mByteBuffer.getInt();
    }
    
    public long readUnsignedInt() throws IOException {
      this.mByteBuffer.position(0);
      read(4);
      return MetadataListReader.toUnsignedInt(this.mByteBuffer.getInt());
    }
    
    public int readUnsignedShort() throws IOException {
      this.mByteBuffer.position(0);
      read(2);
      return MetadataListReader.toUnsignedShort(this.mByteBuffer.getShort());
    }
    
    public void skip(int param1Int) throws IOException {
      while (param1Int > 0) {
        int i = (int)this.mInputStream.skip(param1Int);
        if (i >= 1) {
          param1Int -= i;
          this.mPosition += i;
          continue;
        } 
        throw new IOException("Skip didn't move at least 1 byte forward");
      } 
    }
  }
  
  private static class OffsetInfo {
    private final long mLength;
    
    private final long mStartOffset;
    
    OffsetInfo(long param1Long1, long param1Long2) {
      this.mStartOffset = param1Long1;
      this.mLength = param1Long2;
    }
    
    long getLength() {
      return this.mLength;
    }
    
    long getStartOffset() {
      return this.mStartOffset;
    }
  }
  
  private static interface OpenTypeReader {
    public static final int UINT16_BYTE_COUNT = 2;
    
    public static final int UINT32_BYTE_COUNT = 4;
    
    long getPosition();
    
    int readTag() throws IOException;
    
    long readUnsignedInt() throws IOException;
    
    int readUnsignedShort() throws IOException;
    
    void skip(int param1Int) throws IOException;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\MetadataListReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */