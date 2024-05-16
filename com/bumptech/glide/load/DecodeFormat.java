package com.bumptech.glide.load;

public enum DecodeFormat {
  PREFER_ARGB_8888, PREFER_RGB_565;
  
  private static final DecodeFormat[] $VALUES;
  
  public static final DecodeFormat DEFAULT;
  
  static {
    DecodeFormat decodeFormat1 = new DecodeFormat("PREFER_ARGB_8888", 0);
    PREFER_ARGB_8888 = decodeFormat1;
    DecodeFormat decodeFormat2 = new DecodeFormat("PREFER_RGB_565", 1);
    PREFER_RGB_565 = decodeFormat2;
    $VALUES = new DecodeFormat[] { decodeFormat1, decodeFormat2 };
    DEFAULT = decodeFormat1;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\DecodeFormat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */