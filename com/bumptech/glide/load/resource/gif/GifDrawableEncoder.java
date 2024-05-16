package com.bumptech.glide.load.resource.gif;

import android.util.Log;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.File;
import java.io.IOException;

public class GifDrawableEncoder implements ResourceEncoder<GifDrawable> {
  private static final String TAG = "GifEncoder";
  
  public boolean encode(Resource<GifDrawable> paramResource, File paramFile, Options paramOptions) {
    boolean bool1;
    GifDrawable gifDrawable = (GifDrawable)paramResource.get();
    boolean bool2 = false;
    try {
      ByteBufferUtil.toFile(gifDrawable.getBuffer(), paramFile);
      bool1 = true;
    } catch (IOException iOException) {
      bool1 = bool2;
      if (Log.isLoggable("GifEncoder", 5)) {
        Log.w("GifEncoder", "Failed to encode GIF drawable data", iOException);
        bool1 = bool2;
      } 
    } 
    return bool1;
  }
  
  public EncodeStrategy getEncodeStrategy(Options paramOptions) {
    return EncodeStrategy.SOURCE;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\gif\GifDrawableEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */