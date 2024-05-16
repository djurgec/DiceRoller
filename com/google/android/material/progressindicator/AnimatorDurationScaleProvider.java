package com.google.android.material.progressindicator;

import android.content.ContentResolver;
import android.os.Build;
import android.provider.Settings;

public class AnimatorDurationScaleProvider {
  private static float defaultSystemAnimatorDurationScale = 1.0F;
  
  public static void setDefaultSystemAnimatorDurationScale(float paramFloat) {
    defaultSystemAnimatorDurationScale = paramFloat;
  }
  
  public float getSystemAnimatorDurationScale(ContentResolver paramContentResolver) {
    return (Build.VERSION.SDK_INT >= 17) ? Settings.Global.getFloat(paramContentResolver, "animator_duration_scale", 1.0F) : ((Build.VERSION.SDK_INT == 16) ? Settings.System.getFloat(paramContentResolver, "animator_duration_scale", 1.0F) : defaultSystemAnimatorDurationScale);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\progressindicator\AnimatorDurationScaleProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */