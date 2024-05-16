package com.bumptech.glide.load;

public enum EncodeStrategy {
  NONE, SOURCE, TRANSFORMED;
  
  private static final EncodeStrategy[] $VALUES;
  
  static {
    EncodeStrategy encodeStrategy3 = new EncodeStrategy("SOURCE", 0);
    SOURCE = encodeStrategy3;
    EncodeStrategy encodeStrategy2 = new EncodeStrategy("TRANSFORMED", 1);
    TRANSFORMED = encodeStrategy2;
    EncodeStrategy encodeStrategy1 = new EncodeStrategy("NONE", 2);
    NONE = encodeStrategy1;
    $VALUES = new EncodeStrategy[] { encodeStrategy3, encodeStrategy2, encodeStrategy1 };
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\EncodeStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */