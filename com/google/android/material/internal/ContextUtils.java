package com.google.android.material.internal;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

public class ContextUtils {
  public static Activity getActivity(Context paramContext) {
    while (paramContext instanceof ContextWrapper) {
      if (paramContext instanceof Activity)
        return (Activity)paramContext; 
      paramContext = ((ContextWrapper)paramContext).getBaseContext();
    } 
    return null;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\internal\ContextUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */