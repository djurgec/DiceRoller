package androidx.core.view.accessibility;

import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.view.View;

public final class AccessibilityClickableSpanCompat extends ClickableSpan {
  public static final String SPAN_ID = "ACCESSIBILITY_CLICKABLE_SPAN_ID";
  
  private final int mClickableSpanActionId;
  
  private final AccessibilityNodeInfoCompat mNodeInfoCompat;
  
  private final int mOriginalClickableSpanId;
  
  public AccessibilityClickableSpanCompat(int paramInt1, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat, int paramInt2) {
    this.mOriginalClickableSpanId = paramInt1;
    this.mNodeInfoCompat = paramAccessibilityNodeInfoCompat;
    this.mClickableSpanActionId = paramInt2;
  }
  
  public void onClick(View paramView) {
    Bundle bundle = new Bundle();
    bundle.putInt("ACCESSIBILITY_CLICKABLE_SPAN_ID", this.mOriginalClickableSpanId);
    this.mNodeInfoCompat.performAction(this.mClickableSpanActionId, bundle);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\view\accessibility\AccessibilityClickableSpanCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */