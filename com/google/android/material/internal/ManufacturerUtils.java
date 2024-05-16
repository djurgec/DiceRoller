package com.google.android.material.internal;

import android.os.Build;
import java.util.Locale;

public class ManufacturerUtils {
  private static final String LGE = "lge";
  
  private static final String MEIZU = "meizu";
  
  private static final String SAMSUNG = "samsung";
  
  public static boolean isDateInputKeyboardMissingSeparatorCharacters() {
    return (isLGEDevice() || isSamsungDevice());
  }
  
  public static boolean isLGEDevice() {
    return Build.MANUFACTURER.toLowerCase(Locale.ENGLISH).equals("lge");
  }
  
  public static boolean isMeizuDevice() {
    return Build.MANUFACTURER.toLowerCase(Locale.ENGLISH).equals("meizu");
  }
  
  public static boolean isSamsungDevice() {
    return Build.MANUFACTURER.toLowerCase(Locale.ENGLISH).equals("samsung");
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\internal\ManufacturerUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */