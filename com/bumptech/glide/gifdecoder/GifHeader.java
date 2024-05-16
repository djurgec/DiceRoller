package com.bumptech.glide.gifdecoder;

import java.util.ArrayList;
import java.util.List;

public class GifHeader {
  public static final int NETSCAPE_LOOP_COUNT_DOES_NOT_EXIST = -1;
  
  public static final int NETSCAPE_LOOP_COUNT_FOREVER = 0;
  
  int bgColor;
  
  int bgIndex;
  
  GifFrame currentFrame;
  
  int frameCount = 0;
  
  final List<GifFrame> frames = new ArrayList<>();
  
  int[] gct = null;
  
  boolean gctFlag;
  
  int gctSize;
  
  int height;
  
  int loopCount = -1;
  
  int pixelAspect;
  
  int status = 0;
  
  int width;
  
  public int getHeight() {
    return this.height;
  }
  
  public int getNumFrames() {
    return this.frameCount;
  }
  
  public int getStatus() {
    return this.status;
  }
  
  public int getWidth() {
    return this.width;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\gifdecoder\GifHeader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */