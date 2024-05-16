package com.bumptech.glide.signature;

import com.bumptech.glide.load.Key;
import java.security.MessageDigest;

public final class EmptySignature implements Key {
  private static final EmptySignature EMPTY_KEY = new EmptySignature();
  
  public static EmptySignature obtain() {
    return EMPTY_KEY;
  }
  
  public String toString() {
    return "EmptySignature";
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\signature\EmptySignature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */