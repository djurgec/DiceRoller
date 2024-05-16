package com.google.android.material.timepicker;

import android.content.Context;
import android.view.View;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

class ClickActionDelegate extends AccessibilityDelegateCompat {
  private final AccessibilityNodeInfoCompat.AccessibilityActionCompat clickAction;
  
  public ClickActionDelegate(Context paramContext, int paramInt) {
    this.clickAction = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(16, paramContext.getString(paramInt));
  }
  
  public void onInitializeAccessibilityNodeInfo(View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat) {
    super.onInitializeAccessibilityNodeInfo(paramView, paramAccessibilityNodeInfoCompat);
    paramAccessibilityNodeInfoCompat.addAction(this.clickAction);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\timepicker\ClickActionDelegate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */