package com.bumptech.glide.load;

public enum PreferredColorSpace {
  DISPLAY_P3, SRGB;
  
  private static final PreferredColorSpace[] $VALUES;
  
  static {
    PreferredColorSpace preferredColorSpace2 = new PreferredColorSpace("SRGB", 0);
    SRGB = preferredColorSpace2;
    PreferredColorSpace preferredColorSpace1 = new PreferredColorSpace("DISPLAY_P3", 1);
    DISPLAY_P3 = preferredColorSpace1;
    $VALUES = new PreferredColorSpace[] { preferredColorSpace2, preferredColorSpace1 };
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\PreferredColorSpace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */