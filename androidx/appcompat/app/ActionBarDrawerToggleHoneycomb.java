package androidx.appcompat.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.lang.reflect.Method;

class ActionBarDrawerToggleHoneycomb {
  private static final String TAG = "ActionBarDrawerToggleHC";
  
  private static final int[] THEME_ATTRS = new int[] { 16843531 };
  
  public static Drawable getThemeUpIndicator(Activity paramActivity) {
    TypedArray typedArray = paramActivity.obtainStyledAttributes(THEME_ATTRS);
    Drawable drawable = typedArray.getDrawable(0);
    typedArray.recycle();
    return drawable;
  }
  
  public static SetIndicatorInfo setActionBarDescription(SetIndicatorInfo paramSetIndicatorInfo, Activity paramActivity, int paramInt) {
    SetIndicatorInfo setIndicatorInfo = paramSetIndicatorInfo;
    if (paramSetIndicatorInfo == null)
      setIndicatorInfo = new SetIndicatorInfo(paramActivity); 
    if (setIndicatorInfo.setHomeAsUpIndicator != null)
      try {
        ActionBar actionBar = paramActivity.getActionBar();
        setIndicatorInfo.setHomeActionContentDescription.invoke(actionBar, new Object[] { Integer.valueOf(paramInt) });
        if (Build.VERSION.SDK_INT <= 19)
          actionBar.setSubtitle(actionBar.getSubtitle()); 
      } catch (Exception exception) {
        Log.w("ActionBarDrawerToggleHC", "Couldn't set content description via JB-MR2 API", exception);
      }  
    return setIndicatorInfo;
  }
  
  public static SetIndicatorInfo setActionBarUpIndicator(Activity paramActivity, Drawable paramDrawable, int paramInt) {
    SetIndicatorInfo setIndicatorInfo = new SetIndicatorInfo(paramActivity);
    if (setIndicatorInfo.setHomeAsUpIndicator != null) {
      try {
        ActionBar actionBar = paramActivity.getActionBar();
        setIndicatorInfo.setHomeAsUpIndicator.invoke(actionBar, new Object[] { paramDrawable });
        setIndicatorInfo.setHomeActionContentDescription.invoke(actionBar, new Object[] { Integer.valueOf(paramInt) });
      } catch (Exception exception) {
        Log.w("ActionBarDrawerToggleHC", "Couldn't set home-as-up indicator via JB-MR2 API", exception);
      } 
    } else if (setIndicatorInfo.upIndicatorView != null) {
      setIndicatorInfo.upIndicatorView.setImageDrawable(paramDrawable);
    } else {
      Log.w("ActionBarDrawerToggleHC", "Couldn't set home-as-up indicator");
    } 
    return setIndicatorInfo;
  }
  
  static class SetIndicatorInfo {
    public Method setHomeActionContentDescription;
    
    public Method setHomeAsUpIndicator;
    
    public ImageView upIndicatorView;
    
    SetIndicatorInfo(Activity param1Activity) {
      try {
        this.setHomeAsUpIndicator = ActionBar.class.getDeclaredMethod("setHomeAsUpIndicator", new Class[] { Drawable.class });
        this.setHomeActionContentDescription = ActionBar.class.getDeclaredMethod("setHomeActionContentDescription", new Class[] { int.class });
        return;
      } catch (NoSuchMethodException noSuchMethodException) {
        View view1 = param1Activity.findViewById(16908332);
        if (view1 == null)
          return; 
        ViewGroup viewGroup = (ViewGroup)view1.getParent();
        if (viewGroup.getChildCount() != 2)
          return; 
        view1 = viewGroup.getChildAt(0);
        View view2 = viewGroup.getChildAt(1);
        if (view1.getId() == 16908332)
          view1 = view2; 
        if (view1 instanceof ImageView)
          this.upIndicatorView = (ImageView)view1; 
        return;
      } 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\app\ActionBarDrawerToggleHoneycomb.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */