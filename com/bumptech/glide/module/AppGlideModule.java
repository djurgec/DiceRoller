package com.bumptech.glide.module;

import android.content.Context;
import com.bumptech.glide.GlideBuilder;

public abstract class AppGlideModule extends LibraryGlideModule implements AppliesOptions {
  public void applyOptions(Context paramContext, GlideBuilder paramGlideBuilder) {}
  
  public boolean isManifestParsingEnabled() {
    return true;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\module\AppGlideModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */