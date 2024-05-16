package androidx.core.accessibilityservice;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.pm.PackageManager;
import android.os.Build;

public final class AccessibilityServiceInfoCompat {
  public static final int CAPABILITY_CAN_FILTER_KEY_EVENTS = 8;
  
  public static final int CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 4;
  
  public static final int CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION = 2;
  
  public static final int CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT = 1;
  
  public static final int FEEDBACK_ALL_MASK = -1;
  
  public static final int FEEDBACK_BRAILLE = 32;
  
  public static final int FLAG_INCLUDE_NOT_IMPORTANT_VIEWS = 2;
  
  public static final int FLAG_REPORT_VIEW_IDS = 16;
  
  public static final int FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 8;
  
  public static final int FLAG_REQUEST_FILTER_KEY_EVENTS = 32;
  
  public static final int FLAG_REQUEST_TOUCH_EXPLORATION_MODE = 4;
  
  public static String capabilityToString(int paramInt) {
    switch (paramInt) {
      default:
        return "UNKNOWN";
      case 8:
        return "CAPABILITY_CAN_FILTER_KEY_EVENTS";
      case 4:
        return "CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY";
      case 2:
        return "CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION";
      case 1:
        break;
    } 
    return "CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT";
  }
  
  public static String feedbackTypeToString(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");
    while (paramInt > 0) {
      int i = 1 << Integer.numberOfTrailingZeros(paramInt);
      paramInt &= i ^ 0xFFFFFFFF;
      if (stringBuilder.length() > 1)
        stringBuilder.append(", "); 
      switch (i) {
        default:
          continue;
        case 16:
          stringBuilder.append("FEEDBACK_GENERIC");
          continue;
        case 8:
          stringBuilder.append("FEEDBACK_VISUAL");
          continue;
        case 4:
          stringBuilder.append("FEEDBACK_AUDIBLE");
          continue;
        case 2:
          stringBuilder.append("FEEDBACK_HAPTIC");
          continue;
        case 1:
          break;
      } 
      stringBuilder.append("FEEDBACK_SPOKEN");
    } 
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
  
  public static String flagToString(int paramInt) {
    switch (paramInt) {
      default:
        return null;
      case 32:
        return "FLAG_REQUEST_FILTER_KEY_EVENTS";
      case 16:
        return "FLAG_REPORT_VIEW_IDS";
      case 8:
        return "FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY";
      case 4:
        return "FLAG_REQUEST_TOUCH_EXPLORATION_MODE";
      case 2:
        return "FLAG_INCLUDE_NOT_IMPORTANT_VIEWS";
      case 1:
        break;
    } 
    return "DEFAULT";
  }
  
  public static int getCapabilities(AccessibilityServiceInfo paramAccessibilityServiceInfo) {
    return (Build.VERSION.SDK_INT >= 18) ? paramAccessibilityServiceInfo.getCapabilities() : (paramAccessibilityServiceInfo.getCanRetrieveWindowContent() ? 1 : 0);
  }
  
  public static String loadDescription(AccessibilityServiceInfo paramAccessibilityServiceInfo, PackageManager paramPackageManager) {
    return (Build.VERSION.SDK_INT >= 16) ? paramAccessibilityServiceInfo.loadDescription(paramPackageManager) : paramAccessibilityServiceInfo.getDescription();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\accessibilityservice\AccessibilityServiceInfoCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */