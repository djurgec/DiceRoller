package androidx.appcompat.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class ActionBarDrawerToggle implements DrawerLayout.DrawerListener {
  private final Delegate mActivityImpl;
  
  private final int mCloseDrawerContentDescRes;
  
  boolean mDrawerIndicatorEnabled = true;
  
  private final DrawerLayout mDrawerLayout;
  
  private boolean mDrawerSlideAnimationEnabled = true;
  
  private boolean mHasCustomUpIndicator;
  
  private Drawable mHomeAsUpIndicator;
  
  private final int mOpenDrawerContentDescRes;
  
  private DrawerArrowDrawable mSlider;
  
  View.OnClickListener mToolbarNavigationClickListener;
  
  private boolean mWarnedForDisplayHomeAsUp = false;
  
  ActionBarDrawerToggle(Activity paramActivity, Toolbar paramToolbar, DrawerLayout paramDrawerLayout, DrawerArrowDrawable paramDrawerArrowDrawable, int paramInt1, int paramInt2) {
    if (paramToolbar != null) {
      this.mActivityImpl = new ToolbarCompatDelegate(paramToolbar);
      paramToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            final ActionBarDrawerToggle this$0;
            
            public void onClick(View param1View) {
              if (ActionBarDrawerToggle.this.mDrawerIndicatorEnabled) {
                ActionBarDrawerToggle.this.toggle();
              } else if (ActionBarDrawerToggle.this.mToolbarNavigationClickListener != null) {
                ActionBarDrawerToggle.this.mToolbarNavigationClickListener.onClick(param1View);
              } 
            }
          });
    } else if (paramActivity instanceof DelegateProvider) {
      this.mActivityImpl = ((DelegateProvider)paramActivity).getDrawerToggleDelegate();
    } else {
      this.mActivityImpl = new FrameworkActionBarDelegate(paramActivity);
    } 
    this.mDrawerLayout = paramDrawerLayout;
    this.mOpenDrawerContentDescRes = paramInt1;
    this.mCloseDrawerContentDescRes = paramInt2;
    if (paramDrawerArrowDrawable == null) {
      this.mSlider = new DrawerArrowDrawable(this.mActivityImpl.getActionBarThemedContext());
    } else {
      this.mSlider = paramDrawerArrowDrawable;
    } 
    this.mHomeAsUpIndicator = getThemeUpIndicator();
  }
  
  public ActionBarDrawerToggle(Activity paramActivity, DrawerLayout paramDrawerLayout, int paramInt1, int paramInt2) {
    this(paramActivity, null, paramDrawerLayout, null, paramInt1, paramInt2);
  }
  
  public ActionBarDrawerToggle(Activity paramActivity, DrawerLayout paramDrawerLayout, Toolbar paramToolbar, int paramInt1, int paramInt2) {
    this(paramActivity, paramToolbar, paramDrawerLayout, null, paramInt1, paramInt2);
  }
  
  private void setPosition(float paramFloat) {
    if (paramFloat == 1.0F) {
      this.mSlider.setVerticalMirror(true);
    } else if (paramFloat == 0.0F) {
      this.mSlider.setVerticalMirror(false);
    } 
    this.mSlider.setProgress(paramFloat);
  }
  
  public DrawerArrowDrawable getDrawerArrowDrawable() {
    return this.mSlider;
  }
  
  Drawable getThemeUpIndicator() {
    return this.mActivityImpl.getThemeUpIndicator();
  }
  
  public View.OnClickListener getToolbarNavigationClickListener() {
    return this.mToolbarNavigationClickListener;
  }
  
  public boolean isDrawerIndicatorEnabled() {
    return this.mDrawerIndicatorEnabled;
  }
  
  public boolean isDrawerSlideAnimationEnabled() {
    return this.mDrawerSlideAnimationEnabled;
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    if (!this.mHasCustomUpIndicator)
      this.mHomeAsUpIndicator = getThemeUpIndicator(); 
    syncState();
  }
  
  public void onDrawerClosed(View paramView) {
    setPosition(0.0F);
    if (this.mDrawerIndicatorEnabled)
      setActionBarDescription(this.mOpenDrawerContentDescRes); 
  }
  
  public void onDrawerOpened(View paramView) {
    setPosition(1.0F);
    if (this.mDrawerIndicatorEnabled)
      setActionBarDescription(this.mCloseDrawerContentDescRes); 
  }
  
  public void onDrawerSlide(View paramView, float paramFloat) {
    if (this.mDrawerSlideAnimationEnabled) {
      setPosition(Math.min(1.0F, Math.max(0.0F, paramFloat)));
    } else {
      setPosition(0.0F);
    } 
  }
  
  public void onDrawerStateChanged(int paramInt) {}
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
    if (paramMenuItem != null && paramMenuItem.getItemId() == 16908332 && this.mDrawerIndicatorEnabled) {
      toggle();
      return true;
    } 
    return false;
  }
  
  void setActionBarDescription(int paramInt) {
    this.mActivityImpl.setActionBarDescription(paramInt);
  }
  
  void setActionBarUpIndicator(Drawable paramDrawable, int paramInt) {
    if (!this.mWarnedForDisplayHomeAsUp && !this.mActivityImpl.isNavigationVisible()) {
      Log.w("ActionBarDrawerToggle", "DrawerToggle may not show up because NavigationIcon is not visible. You may need to call actionbar.setDisplayHomeAsUpEnabled(true);");
      this.mWarnedForDisplayHomeAsUp = true;
    } 
    this.mActivityImpl.setActionBarUpIndicator(paramDrawable, paramInt);
  }
  
  public void setDrawerArrowDrawable(DrawerArrowDrawable paramDrawerArrowDrawable) {
    this.mSlider = paramDrawerArrowDrawable;
    syncState();
  }
  
  public void setDrawerIndicatorEnabled(boolean paramBoolean) {
    if (paramBoolean != this.mDrawerIndicatorEnabled) {
      if (paramBoolean) {
        int i;
        DrawerArrowDrawable drawerArrowDrawable = this.mSlider;
        if (this.mDrawerLayout.isDrawerOpen(8388611)) {
          i = this.mCloseDrawerContentDescRes;
        } else {
          i = this.mOpenDrawerContentDescRes;
        } 
        setActionBarUpIndicator((Drawable)drawerArrowDrawable, i);
      } else {
        setActionBarUpIndicator(this.mHomeAsUpIndicator, 0);
      } 
      this.mDrawerIndicatorEnabled = paramBoolean;
    } 
  }
  
  public void setDrawerSlideAnimationEnabled(boolean paramBoolean) {
    this.mDrawerSlideAnimationEnabled = paramBoolean;
    if (!paramBoolean)
      setPosition(0.0F); 
  }
  
  public void setHomeAsUpIndicator(int paramInt) {
    Drawable drawable = null;
    if (paramInt != 0)
      drawable = this.mDrawerLayout.getResources().getDrawable(paramInt); 
    setHomeAsUpIndicator(drawable);
  }
  
  public void setHomeAsUpIndicator(Drawable paramDrawable) {
    if (paramDrawable == null) {
      this.mHomeAsUpIndicator = getThemeUpIndicator();
      this.mHasCustomUpIndicator = false;
    } else {
      this.mHomeAsUpIndicator = paramDrawable;
      this.mHasCustomUpIndicator = true;
    } 
    if (!this.mDrawerIndicatorEnabled)
      setActionBarUpIndicator(this.mHomeAsUpIndicator, 0); 
  }
  
  public void setToolbarNavigationClickListener(View.OnClickListener paramOnClickListener) {
    this.mToolbarNavigationClickListener = paramOnClickListener;
  }
  
  public void syncState() {
    if (this.mDrawerLayout.isDrawerOpen(8388611)) {
      setPosition(1.0F);
    } else {
      setPosition(0.0F);
    } 
    if (this.mDrawerIndicatorEnabled) {
      int i;
      DrawerArrowDrawable drawerArrowDrawable = this.mSlider;
      if (this.mDrawerLayout.isDrawerOpen(8388611)) {
        i = this.mCloseDrawerContentDescRes;
      } else {
        i = this.mOpenDrawerContentDescRes;
      } 
      setActionBarUpIndicator((Drawable)drawerArrowDrawable, i);
    } 
  }
  
  void toggle() {
    int i = this.mDrawerLayout.getDrawerLockMode(8388611);
    if (this.mDrawerLayout.isDrawerVisible(8388611) && i != 2) {
      this.mDrawerLayout.closeDrawer(8388611);
    } else if (i != 1) {
      this.mDrawerLayout.openDrawer(8388611);
    } 
  }
  
  public static interface Delegate {
    Context getActionBarThemedContext();
    
    Drawable getThemeUpIndicator();
    
    boolean isNavigationVisible();
    
    void setActionBarDescription(int param1Int);
    
    void setActionBarUpIndicator(Drawable param1Drawable, int param1Int);
  }
  
  public static interface DelegateProvider {
    ActionBarDrawerToggle.Delegate getDrawerToggleDelegate();
  }
  
  private static class FrameworkActionBarDelegate implements Delegate {
    private final Activity mActivity;
    
    private ActionBarDrawerToggleHoneycomb.SetIndicatorInfo mSetIndicatorInfo;
    
    FrameworkActionBarDelegate(Activity param1Activity) {
      this.mActivity = param1Activity;
    }
    
    public Context getActionBarThemedContext() {
      ActionBar actionBar = this.mActivity.getActionBar();
      return (Context)((actionBar != null) ? actionBar.getThemedContext() : this.mActivity);
    }
    
    public Drawable getThemeUpIndicator() {
      if (Build.VERSION.SDK_INT >= 18) {
        TypedArray typedArray = getActionBarThemedContext().obtainStyledAttributes(null, new int[] { 16843531 }, 16843470, 0);
        Drawable drawable = typedArray.getDrawable(0);
        typedArray.recycle();
        return drawable;
      } 
      return ActionBarDrawerToggleHoneycomb.getThemeUpIndicator(this.mActivity);
    }
    
    public boolean isNavigationVisible() {
      boolean bool;
      ActionBar actionBar = this.mActivity.getActionBar();
      if (actionBar != null && (actionBar.getDisplayOptions() & 0x4) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void setActionBarDescription(int param1Int) {
      if (Build.VERSION.SDK_INT >= 18) {
        ActionBar actionBar = this.mActivity.getActionBar();
        if (actionBar != null)
          actionBar.setHomeActionContentDescription(param1Int); 
      } else {
        this.mSetIndicatorInfo = ActionBarDrawerToggleHoneycomb.setActionBarDescription(this.mSetIndicatorInfo, this.mActivity, param1Int);
      } 
    }
    
    public void setActionBarUpIndicator(Drawable param1Drawable, int param1Int) {
      ActionBar actionBar = this.mActivity.getActionBar();
      if (actionBar != null)
        if (Build.VERSION.SDK_INT >= 18) {
          actionBar.setHomeAsUpIndicator(param1Drawable);
          actionBar.setHomeActionContentDescription(param1Int);
        } else {
          actionBar.setDisplayShowHomeEnabled(true);
          this.mSetIndicatorInfo = ActionBarDrawerToggleHoneycomb.setActionBarUpIndicator(this.mActivity, param1Drawable, param1Int);
          actionBar.setDisplayShowHomeEnabled(false);
        }  
    }
  }
  
  static class ToolbarCompatDelegate implements Delegate {
    final CharSequence mDefaultContentDescription;
    
    final Drawable mDefaultUpIndicator;
    
    final Toolbar mToolbar;
    
    ToolbarCompatDelegate(Toolbar param1Toolbar) {
      this.mToolbar = param1Toolbar;
      this.mDefaultUpIndicator = param1Toolbar.getNavigationIcon();
      this.mDefaultContentDescription = param1Toolbar.getNavigationContentDescription();
    }
    
    public Context getActionBarThemedContext() {
      return this.mToolbar.getContext();
    }
    
    public Drawable getThemeUpIndicator() {
      return this.mDefaultUpIndicator;
    }
    
    public boolean isNavigationVisible() {
      return true;
    }
    
    public void setActionBarDescription(int param1Int) {
      if (param1Int == 0) {
        this.mToolbar.setNavigationContentDescription(this.mDefaultContentDescription);
      } else {
        this.mToolbar.setNavigationContentDescription(param1Int);
      } 
    }
    
    public void setActionBarUpIndicator(Drawable param1Drawable, int param1Int) {
      this.mToolbar.setNavigationIcon(param1Drawable);
      setActionBarDescription(param1Int);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\app\ActionBarDrawerToggle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */