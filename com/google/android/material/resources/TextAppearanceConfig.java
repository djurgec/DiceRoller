package com.google.android.material.resources;

@Deprecated
public class TextAppearanceConfig {
  private static boolean shouldLoadFontSynchronously;
  
  public static void setShouldLoadFontSynchronously(boolean paramBoolean) {
    shouldLoadFontSynchronously = paramBoolean;
  }
  
  public static boolean shouldLoadFontSynchronously() {
    return shouldLoadFontSynchronously;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\resources\TextAppearanceConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */