package com.bumptech.glide.load.model;

import android.util.Log;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteBufferEncoder implements Encoder<ByteBuffer> {
  private static final String TAG = "ByteBufferEncoder";
  
  public boolean encode(ByteBuffer paramByteBuffer, File paramFile, Options paramOptions) {
    boolean bool1;
    boolean bool2 = false;
    try {
      ByteBufferUtil.toFile(paramByteBuffer, paramFile);
      bool1 = true;
    } catch (IOException iOException) {
      bool1 = bool2;
      if (Log.isLoggable("ByteBufferEncoder", 3)) {
        Log.d("ByteBufferEncoder", "Failed to write data", iOException);
        bool1 = bool2;
      } 
    } 
    return bool1;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\model\ByteBufferEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */