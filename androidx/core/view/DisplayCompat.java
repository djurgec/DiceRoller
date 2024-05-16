package androidx.core.view;

import android.app.UiModeManager;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.text.TextUtils;
import android.view.Display;
import androidx.core.util.Preconditions;

public final class DisplayCompat {
  private static final int DISPLAY_SIZE_4K_HEIGHT = 2160;
  
  private static final int DISPLAY_SIZE_4K_WIDTH = 3840;
  
  static Point getCurrentDisplaySizeFromWorkarounds(Context paramContext, Display paramDisplay) {
    Point point;
    if (Build.VERSION.SDK_INT < 28) {
      point = parsePhysicalDisplaySizeFromSystemProperties("sys.display-size", paramDisplay);
    } else {
      point = parsePhysicalDisplaySizeFromSystemProperties("vendor.display-size", paramDisplay);
    } 
    if (point != null)
      return point; 
    boolean bool = isSonyBravia4kTv(paramContext);
    paramContext = null;
    if (bool) {
      Point point1;
      if (isCurrentModeTheLargestMode(paramDisplay))
        point1 = new Point(3840, 2160); 
      return point1;
    } 
    return null;
  }
  
  private static Point getDisplaySize(Context paramContext, Display paramDisplay) {
    Point point = getCurrentDisplaySizeFromWorkarounds(paramContext, paramDisplay);
    if (point != null)
      return point; 
    point = new Point();
    if (Build.VERSION.SDK_INT >= 17) {
      Api17Impl.getRealSize(paramDisplay, point);
    } else {
      paramDisplay.getSize(point);
    } 
    return point;
  }
  
  public static ModeCompat getMode(Context paramContext, Display paramDisplay) {
    return (Build.VERSION.SDK_INT >= 23) ? Api23Impl.getMode(paramContext, paramDisplay) : new ModeCompat(getDisplaySize(paramContext, paramDisplay));
  }
  
  public static ModeCompat[] getSupportedModes(Context paramContext, Display paramDisplay) {
    return (Build.VERSION.SDK_INT >= 23) ? Api23Impl.getSupportedModes(paramContext, paramDisplay) : new ModeCompat[] { getMode(paramContext, paramDisplay) };
  }
  
  private static String getSystemProperty(String paramString) {
    try {
      Class<?> clazz = Class.forName("android.os.SystemProperties");
      return (String)clazz.getMethod("get", new Class[] { String.class }).invoke(clazz, new Object[] { paramString });
    } catch (Exception exception) {
      return null;
    } 
  }
  
  static boolean isCurrentModeTheLargestMode(Display paramDisplay) {
    return (Build.VERSION.SDK_INT >= 23) ? Api23Impl.isCurrentModeTheLargestMode(paramDisplay) : true;
  }
  
  private static boolean isSonyBravia4kTv(Context paramContext) {
    boolean bool;
    if (isTv(paramContext) && "Sony".equals(Build.MANUFACTURER) && Build.MODEL.startsWith("BRAVIA") && paramContext.getPackageManager().hasSystemFeature("com.sony.dtv.hardware.panel.qfhd")) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static boolean isTv(Context paramContext) {
    boolean bool;
    UiModeManager uiModeManager = (UiModeManager)paramContext.getSystemService("uimode");
    if (uiModeManager != null && uiModeManager.getCurrentModeType() == 4) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static Point parseDisplaySize(String paramString) throws NumberFormatException {
    String[] arrayOfString = paramString.trim().split("x", -1);
    if (arrayOfString.length == 2) {
      int i = Integer.parseInt(arrayOfString[0]);
      int j = Integer.parseInt(arrayOfString[1]);
      if (i > 0 && j > 0)
        return new Point(i, j); 
    } 
    throw new NumberFormatException();
  }
  
  private static Point parsePhysicalDisplaySizeFromSystemProperties(String paramString, Display paramDisplay) {
    if (paramDisplay.getDisplayId() != 0)
      return null; 
    paramString = getSystemProperty(paramString);
    if (TextUtils.isEmpty(paramString))
      return null; 
    try {
      return parseDisplaySize(paramString);
    } catch (NumberFormatException numberFormatException) {
      return null;
    } 
  }
  
  static class Api17Impl {
    static void getRealSize(Display param1Display, Point param1Point) {
      param1Display.getRealSize(param1Point);
    }
  }
  
  static class Api23Impl {
    static DisplayCompat.ModeCompat getMode(Context param1Context, Display param1Display) {
      Display.Mode mode = param1Display.getMode();
      Point point = DisplayCompat.getCurrentDisplaySizeFromWorkarounds(param1Context, param1Display);
      return (point == null || physicalSizeEquals(mode, point)) ? new DisplayCompat.ModeCompat(mode, true) : new DisplayCompat.ModeCompat(mode, point);
    }
    
    public static DisplayCompat.ModeCompat[] getSupportedModes(Context param1Context, Display param1Display) {
      Display.Mode[] arrayOfMode = param1Display.getSupportedModes();
      DisplayCompat.ModeCompat[] arrayOfModeCompat = new DisplayCompat.ModeCompat[arrayOfMode.length];
      Display.Mode mode = param1Display.getMode();
      Point point = DisplayCompat.getCurrentDisplaySizeFromWorkarounds(param1Context, param1Display);
      if (point == null || physicalSizeEquals(mode, point)) {
        for (byte b1 = 0; b1 < arrayOfMode.length; b1++) {
          boolean bool = physicalSizeEquals(arrayOfMode[b1], mode);
          arrayOfModeCompat[b1] = new DisplayCompat.ModeCompat(arrayOfMode[b1], bool);
        } 
        return arrayOfModeCompat;
      } 
      for (byte b = 0; b < arrayOfMode.length; b++) {
        DisplayCompat.ModeCompat modeCompat;
        if (physicalSizeEquals(arrayOfMode[b], mode)) {
          modeCompat = new DisplayCompat.ModeCompat(arrayOfMode[b], point);
        } else {
          modeCompat = new DisplayCompat.ModeCompat(arrayOfMode[b], false);
        } 
        arrayOfModeCompat[b] = modeCompat;
      } 
      return arrayOfModeCompat;
    }
    
    static boolean isCurrentModeTheLargestMode(Display param1Display) {
      Display.Mode mode = param1Display.getMode();
      Display.Mode[] arrayOfMode = param1Display.getSupportedModes();
      for (byte b = 0; b < arrayOfMode.length; b++) {
        if (mode.getPhysicalHeight() < arrayOfMode[b].getPhysicalHeight() || mode.getPhysicalWidth() < arrayOfMode[b].getPhysicalWidth())
          return false; 
      } 
      return true;
    }
    
    static boolean physicalSizeEquals(Display.Mode param1Mode, Point param1Point) {
      boolean bool;
      if ((param1Mode.getPhysicalWidth() == param1Point.x && param1Mode.getPhysicalHeight() == param1Point.y) || (param1Mode.getPhysicalWidth() == param1Point.y && param1Mode.getPhysicalHeight() == param1Point.x)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    static boolean physicalSizeEquals(Display.Mode param1Mode1, Display.Mode param1Mode2) {
      boolean bool;
      if (param1Mode1.getPhysicalWidth() == param1Mode2.getPhysicalWidth() && param1Mode1.getPhysicalHeight() == param1Mode2.getPhysicalHeight()) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
  }
  
  public static final class ModeCompat {
    private final boolean mIsNative;
    
    private final Display.Mode mMode;
    
    private final Point mPhysicalSize;
    
    ModeCompat(Point param1Point) {
      Preconditions.checkNotNull(param1Point, "physicalSize == null");
      this.mPhysicalSize = param1Point;
      this.mMode = null;
      this.mIsNative = true;
    }
    
    ModeCompat(Display.Mode param1Mode, Point param1Point) {
      Preconditions.checkNotNull(param1Mode, "mode == null, can't wrap a null reference");
      Preconditions.checkNotNull(param1Point, "physicalSize == null");
      this.mPhysicalSize = param1Point;
      this.mMode = param1Mode;
      this.mIsNative = true;
    }
    
    ModeCompat(Display.Mode param1Mode, boolean param1Boolean) {
      Preconditions.checkNotNull(param1Mode, "mode == null, can't wrap a null reference");
      this.mPhysicalSize = new Point(param1Mode.getPhysicalWidth(), param1Mode.getPhysicalHeight());
      this.mMode = param1Mode;
      this.mIsNative = param1Boolean;
    }
    
    public int getPhysicalHeight() {
      return this.mPhysicalSize.y;
    }
    
    public int getPhysicalWidth() {
      return this.mPhysicalSize.x;
    }
    
    @Deprecated
    public boolean isNative() {
      return this.mIsNative;
    }
    
    public Display.Mode toMode() {
      return this.mMode;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\view\DisplayCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */