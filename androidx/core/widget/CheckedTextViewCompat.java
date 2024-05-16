package androidx.core.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.widget.CheckedTextView;
import java.lang.reflect.Field;

public final class CheckedTextViewCompat {
  private static final String TAG = "CheckedTextViewCompat";
  
  public static Drawable getCheckMarkDrawable(CheckedTextView paramCheckedTextView) {
    return (Build.VERSION.SDK_INT >= 16) ? Api16Impl.getCheckMarkDrawable(paramCheckedTextView) : Api14Impl.getCheckMarkDrawable(paramCheckedTextView);
  }
  
  public static ColorStateList getCheckMarkTintList(CheckedTextView paramCheckedTextView) {
    return (Build.VERSION.SDK_INT >= 21) ? Api21Impl.getCheckMarkTintList(paramCheckedTextView) : ((paramCheckedTextView instanceof TintableCheckedTextView) ? ((TintableCheckedTextView)paramCheckedTextView).getSupportCheckMarkTintList() : null);
  }
  
  public static PorterDuff.Mode getCheckMarkTintMode(CheckedTextView paramCheckedTextView) {
    return (Build.VERSION.SDK_INT >= 21) ? Api21Impl.getCheckMarkTintMode(paramCheckedTextView) : ((paramCheckedTextView instanceof TintableCheckedTextView) ? ((TintableCheckedTextView)paramCheckedTextView).getSupportCheckMarkTintMode() : null);
  }
  
  public static void setCheckMarkTintList(CheckedTextView paramCheckedTextView, ColorStateList paramColorStateList) {
    if (Build.VERSION.SDK_INT >= 21) {
      Api21Impl.setCheckMarkTintList(paramCheckedTextView, paramColorStateList);
    } else if (paramCheckedTextView instanceof TintableCheckedTextView) {
      ((TintableCheckedTextView)paramCheckedTextView).setSupportCheckMarkTintList(paramColorStateList);
    } 
  }
  
  public static void setCheckMarkTintMode(CheckedTextView paramCheckedTextView, PorterDuff.Mode paramMode) {
    if (Build.VERSION.SDK_INT >= 21) {
      Api21Impl.setCheckMarkTintMode(paramCheckedTextView, paramMode);
    } else if (paramCheckedTextView instanceof TintableCheckedTextView) {
      ((TintableCheckedTextView)paramCheckedTextView).setSupportCheckMarkTintMode(paramMode);
    } 
  }
  
  private static class Api14Impl {
    private static Field sCheckMarkDrawableField;
    
    private static boolean sResolved;
    
    static Drawable getCheckMarkDrawable(CheckedTextView param1CheckedTextView) {
      if (!sResolved) {
        try {
          Field field1 = CheckedTextView.class.getDeclaredField("mCheckMarkDrawable");
          sCheckMarkDrawableField = field1;
          field1.setAccessible(true);
        } catch (NoSuchFieldException noSuchFieldException) {
          Log.i("CheckedTextViewCompat", "Failed to retrieve mCheckMarkDrawable field", noSuchFieldException);
        } 
        sResolved = true;
      } 
      Field field = sCheckMarkDrawableField;
      if (field != null)
        try {
          return (Drawable)field.get(param1CheckedTextView);
        } catch (IllegalAccessException illegalAccessException) {
          Log.i("CheckedTextViewCompat", "Failed to get check mark drawable via reflection", illegalAccessException);
          sCheckMarkDrawableField = null;
        }  
      return null;
    }
  }
  
  private static class Api16Impl {
    static Drawable getCheckMarkDrawable(CheckedTextView param1CheckedTextView) {
      return param1CheckedTextView.getCheckMarkDrawable();
    }
  }
  
  private static class Api21Impl {
    static ColorStateList getCheckMarkTintList(CheckedTextView param1CheckedTextView) {
      return param1CheckedTextView.getCheckMarkTintList();
    }
    
    static PorterDuff.Mode getCheckMarkTintMode(CheckedTextView param1CheckedTextView) {
      return param1CheckedTextView.getCheckMarkTintMode();
    }
    
    static void setCheckMarkTintList(CheckedTextView param1CheckedTextView, ColorStateList param1ColorStateList) {
      param1CheckedTextView.setCheckMarkTintList(param1ColorStateList);
    }
    
    static void setCheckMarkTintMode(CheckedTextView param1CheckedTextView, PorterDuff.Mode param1Mode) {
      param1CheckedTextView.setCheckMarkTintMode(param1Mode);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\widget\CheckedTextViewCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */