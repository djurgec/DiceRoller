package com.bumptech.glide.gifdecoder;

class GifFrame {
  static final int DISPOSAL_BACKGROUND = 2;
  
  static final int DISPOSAL_NONE = 1;
  
  static final int DISPOSAL_PREVIOUS = 3;
  
  static final int DISPOSAL_UNSPECIFIED = 0;
  
  int bufferFrameStart;
  
  int delay;
  
  int dispose;
  
  int ih;
  
  boolean interlace;
  
  int iw;
  
  int ix;
  
  int iy;
  
  int[] lct;
  
  int transIndex;
  
  boolean transparency;
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\gifdecoder\GifFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */