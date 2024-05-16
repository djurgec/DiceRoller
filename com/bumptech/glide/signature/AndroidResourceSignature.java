package com.bumptech.glide.signature;

import android.content.Context;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.Util;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public final class AndroidResourceSignature implements Key {
  private final Key applicationVersion;
  
  private final int nightMode;
  
  private AndroidResourceSignature(int paramInt, Key paramKey) {
    this.nightMode = paramInt;
    this.applicationVersion = paramKey;
  }
  
  public static Key obtain(Context paramContext) {
    Key key = ApplicationVersionSignature.obtain(paramContext);
    return new AndroidResourceSignature((paramContext.getResources().getConfiguration()).uiMode & 0x30, key);
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof AndroidResourceSignature;
    boolean bool1 = false;
    if (bool) {
      paramObject = paramObject;
      bool = bool1;
      if (this.nightMode == ((AndroidResourceSignature)paramObject).nightMode) {
        bool = bool1;
        if (this.applicationVersion.equals(((AndroidResourceSignature)paramObject).applicationVersion))
          bool = true; 
      } 
      return bool;
    } 
    return false;
  }
  
  public int hashCode() {
    return Util.hashCode(this.applicationVersion, this.nightMode);
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {
    this.applicationVersion.updateDiskCacheKey(paramMessageDigest);
    paramMessageDigest.update(ByteBuffer.allocate(4).putInt(this.nightMode).array());
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\signature\AndroidResourceSignature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */