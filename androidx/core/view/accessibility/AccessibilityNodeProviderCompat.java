package androidx.core.view.accessibility;

import android.os.Build;
import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import java.util.ArrayList;
import java.util.List;

public class AccessibilityNodeProviderCompat {
  public static final int HOST_VIEW_ID = -1;
  
  private final Object mProvider;
  
  public AccessibilityNodeProviderCompat() {
    if (Build.VERSION.SDK_INT >= 26) {
      this.mProvider = new AccessibilityNodeProviderApi26(this);
    } else if (Build.VERSION.SDK_INT >= 19) {
      this.mProvider = new AccessibilityNodeProviderApi19(this);
    } else if (Build.VERSION.SDK_INT >= 16) {
      this.mProvider = new AccessibilityNodeProviderApi16(this);
    } else {
      this.mProvider = null;
    } 
  }
  
  public AccessibilityNodeProviderCompat(Object paramObject) {
    this.mProvider = paramObject;
  }
  
  public void addExtraDataToAccessibilityNodeInfo(int paramInt, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat, String paramString, Bundle paramBundle) {}
  
  public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int paramInt) {
    return null;
  }
  
  public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText(String paramString, int paramInt) {
    return null;
  }
  
  public AccessibilityNodeInfoCompat findFocus(int paramInt) {
    return null;
  }
  
  public Object getProvider() {
    return this.mProvider;
  }
  
  public boolean performAction(int paramInt1, int paramInt2, Bundle paramBundle) {
    return false;
  }
  
  static class AccessibilityNodeProviderApi16 extends AccessibilityNodeProvider {
    final AccessibilityNodeProviderCompat mCompat;
    
    AccessibilityNodeProviderApi16(AccessibilityNodeProviderCompat param1AccessibilityNodeProviderCompat) {
      this.mCompat = param1AccessibilityNodeProviderCompat;
    }
    
    public AccessibilityNodeInfo createAccessibilityNodeInfo(int param1Int) {
      AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = this.mCompat.createAccessibilityNodeInfo(param1Int);
      return (accessibilityNodeInfoCompat == null) ? null : accessibilityNodeInfoCompat.unwrap();
    }
    
    public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String param1String, int param1Int) {
      List<AccessibilityNodeInfoCompat> list = this.mCompat.findAccessibilityNodeInfosByText(param1String, param1Int);
      if (list == null)
        return null; 
      ArrayList<AccessibilityNodeInfo> arrayList = new ArrayList();
      int i = list.size();
      for (param1Int = 0; param1Int < i; param1Int++)
        arrayList.add(((AccessibilityNodeInfoCompat)list.get(param1Int)).unwrap()); 
      return arrayList;
    }
    
    public boolean performAction(int param1Int1, int param1Int2, Bundle param1Bundle) {
      return this.mCompat.performAction(param1Int1, param1Int2, param1Bundle);
    }
  }
  
  static class AccessibilityNodeProviderApi19 extends AccessibilityNodeProviderApi16 {
    AccessibilityNodeProviderApi19(AccessibilityNodeProviderCompat param1AccessibilityNodeProviderCompat) {
      super(param1AccessibilityNodeProviderCompat);
    }
    
    public AccessibilityNodeInfo findFocus(int param1Int) {
      AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = this.mCompat.findFocus(param1Int);
      return (accessibilityNodeInfoCompat == null) ? null : accessibilityNodeInfoCompat.unwrap();
    }
  }
  
  static class AccessibilityNodeProviderApi26 extends AccessibilityNodeProviderApi19 {
    AccessibilityNodeProviderApi26(AccessibilityNodeProviderCompat param1AccessibilityNodeProviderCompat) {
      super(param1AccessibilityNodeProviderCompat);
    }
    
    public void addExtraDataToAccessibilityNodeInfo(int param1Int, AccessibilityNodeInfo param1AccessibilityNodeInfo, String param1String, Bundle param1Bundle) {
      this.mCompat.addExtraDataToAccessibilityNodeInfo(param1Int, AccessibilityNodeInfoCompat.wrap(param1AccessibilityNodeInfo), param1String, param1Bundle);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\view\accessibility\AccessibilityNodeProviderCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */