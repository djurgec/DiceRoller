package androidx.appcompat.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ContextThemeWrapper;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.view.StandaloneActionMode;
import androidx.appcompat.view.SupportActionModeWrapper;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.WindowCallbackWrapper;
import androidx.appcompat.view.menu.ListMenuPresenter;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.ActionBarContextView;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.appcompat.widget.DecorContentParent;
import androidx.appcompat.widget.FitWindowsViewGroup;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.VectorEnabledTintResources;
import androidx.appcompat.widget.ViewStubCompat;
import androidx.appcompat.widget.ViewUtils;
import androidx.collection.SimpleArrayMap;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.ObjectsCompat;
import androidx.core.view.KeyEventDispatcher;
import androidx.core.view.LayoutInflaterCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.PopupWindowCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

class AppCompatDelegateImpl extends AppCompatDelegate implements MenuBuilder.Callback, LayoutInflater.Factory2 {
  static {
    int i = Build.VERSION.SDK_INT;
    boolean bool2 = false;
    if (i < 21) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    IS_PRE_LOLLIPOP = bool1;
    sWindowBackgroundStyleable = new int[] { 16842836 };
    sCanReturnDifferentContext = "robolectric".equals(Build.FINGERPRINT) ^ true;
    if (Build.VERSION.SDK_INT >= 17)
      bool2 = true; 
    sCanApplyOverrideConfiguration = bool2;
    if (bool1 && !sInstalledExceptionHandler) {
      Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler()) {
            final Thread.UncaughtExceptionHandler val$defHandler;
            
            private boolean shouldWrapException(Throwable param1Throwable) {
              boolean bool1 = param1Throwable instanceof Resources.NotFoundException;
              boolean bool = false;
              if (bool1) {
                String str = param1Throwable.getMessage();
                if (str != null && (str.contains("drawable") || str.contains("Drawable")))
                  bool = true; 
                return bool;
              } 
              return false;
            }
            
            public void uncaughtException(Thread param1Thread, Throwable param1Throwable) {
              if (shouldWrapException(param1Throwable)) {
                Resources.NotFoundException notFoundException = new Resources.NotFoundException(param1Throwable.getMessage() + ". If the resource you are trying to use is a vector resource, you may be referencing it in an unsupported way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.");
                notFoundException.initCause(param1Throwable.getCause());
                notFoundException.setStackTrace(param1Throwable.getStackTrace());
                defHandler.uncaughtException(param1Thread, (Throwable)notFoundException);
              } else {
                defHandler.uncaughtException(param1Thread, param1Throwable);
              } 
            }
          });
      sInstalledExceptionHandler = true;
    } 
  }
  
  AppCompatDelegateImpl(Activity paramActivity, AppCompatCallback paramAppCompatCallback) {
    this((Context)paramActivity, (Window)null, paramAppCompatCallback, paramActivity);
  }
  
  AppCompatDelegateImpl(Dialog paramDialog, AppCompatCallback paramAppCompatCallback) {
    this(paramDialog.getContext(), paramDialog.getWindow(), paramAppCompatCallback, paramDialog);
  }
  
  AppCompatDelegateImpl(Context paramContext, Activity paramActivity, AppCompatCallback paramAppCompatCallback) {
    this(paramContext, (Window)null, paramAppCompatCallback, paramActivity);
  }
  
  AppCompatDelegateImpl(Context paramContext, Window paramWindow, AppCompatCallback paramAppCompatCallback) {
    this(paramContext, paramWindow, paramAppCompatCallback, paramContext);
  }
  
  private AppCompatDelegateImpl(Context paramContext, Window paramWindow, AppCompatCallback paramAppCompatCallback, Object paramObject) {
    this.mContext = paramContext;
    this.mAppCompatCallback = paramAppCompatCallback;
    this.mHost = paramObject;
    if (this.mLocalNightMode == -100 && paramObject instanceof Dialog) {
      AppCompatActivity appCompatActivity = tryUnwrapContext();
      if (appCompatActivity != null)
        this.mLocalNightMode = appCompatActivity.getDelegate().getLocalNightMode(); 
    } 
    if (this.mLocalNightMode == -100) {
      SimpleArrayMap<String, Integer> simpleArrayMap = sLocalNightModes;
      Integer integer = (Integer)simpleArrayMap.get(paramObject.getClass().getName());
      if (integer != null) {
        this.mLocalNightMode = integer.intValue();
        simpleArrayMap.remove(paramObject.getClass().getName());
      } 
    } 
    if (paramWindow != null)
      attachToWindow(paramWindow); 
    AppCompatDrawableManager.preload();
  }
  
  private boolean applyDayNight(boolean paramBoolean) {
    if (this.mDestroyed)
      return false; 
    int i = calculateNightMode();
    paramBoolean = updateForNightMode(mapNightMode(this.mContext, i), paramBoolean);
    if (i == 0) {
      getAutoTimeNightModeManager(this.mContext).setup();
    } else {
      AutoNightModeManager autoNightModeManager = this.mAutoTimeNightModeManager;
      if (autoNightModeManager != null)
        autoNightModeManager.cleanup(); 
    } 
    if (i == 3) {
      getAutoBatteryNightModeManager(this.mContext).setup();
    } else {
      AutoNightModeManager autoNightModeManager = this.mAutoBatteryNightModeManager;
      if (autoNightModeManager != null)
        autoNightModeManager.cleanup(); 
    } 
    return paramBoolean;
  }
  
  private void applyFixedSizeWindow() {
    ContentFrameLayout contentFrameLayout = (ContentFrameLayout)this.mSubDecor.findViewById(16908290);
    View view = this.mWindow.getDecorView();
    contentFrameLayout.setDecorPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    TypedArray typedArray = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme);
    typedArray.getValue(R.styleable.AppCompatTheme_windowMinWidthMajor, contentFrameLayout.getMinWidthMajor());
    typedArray.getValue(R.styleable.AppCompatTheme_windowMinWidthMinor, contentFrameLayout.getMinWidthMinor());
    if (typedArray.hasValue(R.styleable.AppCompatTheme_windowFixedWidthMajor))
      typedArray.getValue(R.styleable.AppCompatTheme_windowFixedWidthMajor, contentFrameLayout.getFixedWidthMajor()); 
    if (typedArray.hasValue(R.styleable.AppCompatTheme_windowFixedWidthMinor))
      typedArray.getValue(R.styleable.AppCompatTheme_windowFixedWidthMinor, contentFrameLayout.getFixedWidthMinor()); 
    if (typedArray.hasValue(R.styleable.AppCompatTheme_windowFixedHeightMajor))
      typedArray.getValue(R.styleable.AppCompatTheme_windowFixedHeightMajor, contentFrameLayout.getFixedHeightMajor()); 
    if (typedArray.hasValue(R.styleable.AppCompatTheme_windowFixedHeightMinor))
      typedArray.getValue(R.styleable.AppCompatTheme_windowFixedHeightMinor, contentFrameLayout.getFixedHeightMinor()); 
    typedArray.recycle();
    contentFrameLayout.requestLayout();
  }
  
  private void attachToWindow(Window paramWindow) {
    if (this.mWindow == null) {
      Window.Callback callback = paramWindow.getCallback();
      if (!(callback instanceof AppCompatWindowCallback)) {
        AppCompatWindowCallback appCompatWindowCallback = new AppCompatWindowCallback(callback);
        this.mAppCompatWindowCallback = appCompatWindowCallback;
        paramWindow.setCallback((Window.Callback)appCompatWindowCallback);
        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(this.mContext, null, sWindowBackgroundStyleable);
        Drawable drawable = tintTypedArray.getDrawableIfKnown(0);
        if (drawable != null)
          paramWindow.setBackgroundDrawable(drawable); 
        tintTypedArray.recycle();
        this.mWindow = paramWindow;
        return;
      } 
      throw new IllegalStateException("AppCompat has already installed itself into the Window");
    } 
    throw new IllegalStateException("AppCompat has already installed itself into the Window");
  }
  
  private int calculateNightMode() {
    int i = this.mLocalNightMode;
    if (i == -100)
      i = getDefaultNightMode(); 
    return i;
  }
  
  private void cleanupAutoManagers() {
    AutoNightModeManager autoNightModeManager = this.mAutoTimeNightModeManager;
    if (autoNightModeManager != null)
      autoNightModeManager.cleanup(); 
    autoNightModeManager = this.mAutoBatteryNightModeManager;
    if (autoNightModeManager != null)
      autoNightModeManager.cleanup(); 
  }
  
  private Configuration createOverrideConfigurationForDayNight(Context paramContext, int paramInt, Configuration paramConfiguration) {
    switch (paramInt) {
      default:
        paramInt = (paramContext.getApplicationContext().getResources().getConfiguration()).uiMode & 0x30;
        break;
      case 2:
        paramInt = 32;
        break;
      case 1:
        paramInt = 16;
        break;
    } 
    Configuration configuration = new Configuration();
    configuration.fontScale = 0.0F;
    if (paramConfiguration != null)
      configuration.setTo(paramConfiguration); 
    configuration.uiMode = configuration.uiMode & 0xFFFFFFCF | paramInt;
    return configuration;
  }
  
  private ViewGroup createSubDecor() {
    ViewGroup viewGroup;
    TypedArray typedArray = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme);
    if (typedArray.hasValue(R.styleable.AppCompatTheme_windowActionBar)) {
      DecorContentParent decorContentParent;
      if (typedArray.getBoolean(R.styleable.AppCompatTheme_windowNoTitle, false)) {
        requestWindowFeature(1);
      } else if (typedArray.getBoolean(R.styleable.AppCompatTheme_windowActionBar, false)) {
        requestWindowFeature(108);
      } 
      if (typedArray.getBoolean(R.styleable.AppCompatTheme_windowActionBarOverlay, false))
        requestWindowFeature(109); 
      if (typedArray.getBoolean(R.styleable.AppCompatTheme_windowActionModeOverlay, false))
        requestWindowFeature(10); 
      this.mIsFloating = typedArray.getBoolean(R.styleable.AppCompatTheme_android_windowIsFloating, false);
      typedArray.recycle();
      ensureWindow();
      this.mWindow.getDecorView();
      LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
      typedArray = null;
      if (!this.mWindowNoTitle) {
        if (this.mIsFloating) {
          viewGroup = (ViewGroup)layoutInflater.inflate(R.layout.abc_dialog_title_material, null);
          this.mOverlayActionBar = false;
          this.mHasActionBar = false;
        } else if (this.mHasActionBar) {
          Context context;
          TypedValue typedValue = new TypedValue();
          this.mContext.getTheme().resolveAttribute(R.attr.actionBarTheme, typedValue, true);
          if (typedValue.resourceId != 0) {
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(this.mContext, typedValue.resourceId);
          } else {
            context = this.mContext;
          } 
          viewGroup = (ViewGroup)LayoutInflater.from(context).inflate(R.layout.abc_screen_toolbar, null);
          decorContentParent = (DecorContentParent)viewGroup.findViewById(R.id.decor_content_parent);
          this.mDecorContentParent = decorContentParent;
          decorContentParent.setWindowCallback(getWindowCallback());
          if (this.mOverlayActionBar)
            this.mDecorContentParent.initFeature(109); 
          if (this.mFeatureProgress)
            this.mDecorContentParent.initFeature(2); 
          if (this.mFeatureIndeterminateProgress)
            this.mDecorContentParent.initFeature(5); 
        } 
      } else if (this.mOverlayActionMode) {
        viewGroup = (ViewGroup)decorContentParent.inflate(R.layout.abc_screen_simple_overlay_action_mode, null);
      } else {
        viewGroup = (ViewGroup)decorContentParent.inflate(R.layout.abc_screen_simple, null);
      } 
      if (viewGroup != null) {
        if (Build.VERSION.SDK_INT >= 21) {
          ViewCompat.setOnApplyWindowInsetsListener((View)viewGroup, new OnApplyWindowInsetsListener() {
                final AppCompatDelegateImpl this$0;
                
                public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat) {
                  int i = param1WindowInsetsCompat.getSystemWindowInsetTop();
                  int j = AppCompatDelegateImpl.this.updateStatusGuard(param1WindowInsetsCompat, (Rect)null);
                  WindowInsetsCompat windowInsetsCompat = param1WindowInsetsCompat;
                  if (i != j)
                    windowInsetsCompat = param1WindowInsetsCompat.replaceSystemWindowInsets(param1WindowInsetsCompat.getSystemWindowInsetLeft(), j, param1WindowInsetsCompat.getSystemWindowInsetRight(), param1WindowInsetsCompat.getSystemWindowInsetBottom()); 
                  return ViewCompat.onApplyWindowInsets(param1View, windowInsetsCompat);
                }
              });
        } else if (viewGroup instanceof FitWindowsViewGroup) {
          ((FitWindowsViewGroup)viewGroup).setOnFitSystemWindowsListener(new FitWindowsViewGroup.OnFitSystemWindowsListener() {
                final AppCompatDelegateImpl this$0;
                
                public void onFitSystemWindows(Rect param1Rect) {
                  param1Rect.top = AppCompatDelegateImpl.this.updateStatusGuard((WindowInsetsCompat)null, param1Rect);
                }
              });
        } 
        if (this.mDecorContentParent == null)
          this.mTitleView = (TextView)viewGroup.findViewById(R.id.title); 
        ViewUtils.makeOptionalFitsSystemWindows((View)viewGroup);
        ContentFrameLayout contentFrameLayout = (ContentFrameLayout)viewGroup.findViewById(R.id.action_bar_activity_content);
        ViewGroup viewGroup1 = (ViewGroup)this.mWindow.findViewById(16908290);
        if (viewGroup1 != null) {
          while (viewGroup1.getChildCount() > 0) {
            View view = viewGroup1.getChildAt(0);
            viewGroup1.removeViewAt(0);
            contentFrameLayout.addView(view);
          } 
          viewGroup1.setId(-1);
          contentFrameLayout.setId(16908290);
          if (viewGroup1 instanceof FrameLayout)
            ((FrameLayout)viewGroup1).setForeground(null); 
        } 
        this.mWindow.setContentView((View)viewGroup);
        contentFrameLayout.setAttachListener(new ContentFrameLayout.OnAttachListener() {
              final AppCompatDelegateImpl this$0;
              
              public void onAttachedFromWindow() {}
              
              public void onDetachedFromWindow() {
                AppCompatDelegateImpl.this.dismissPopups();
              }
            });
        return viewGroup;
      } 
      throw new IllegalArgumentException("AppCompat does not support the current theme features: { windowActionBar: " + this.mHasActionBar + ", windowActionBarOverlay: " + this.mOverlayActionBar + ", android:windowIsFloating: " + this.mIsFloating + ", windowActionModeOverlay: " + this.mOverlayActionMode + ", windowNoTitle: " + this.mWindowNoTitle + " }");
    } 
    viewGroup.recycle();
    throw new IllegalStateException("You need to use a Theme.AppCompat theme (or descendant) with this activity.");
  }
  
  private void ensureSubDecor() {
    if (!this.mSubDecorInstalled) {
      this.mSubDecor = createSubDecor();
      CharSequence charSequence = getTitle();
      if (!TextUtils.isEmpty(charSequence)) {
        DecorContentParent decorContentParent = this.mDecorContentParent;
        if (decorContentParent != null) {
          decorContentParent.setWindowTitle(charSequence);
        } else if (peekSupportActionBar() != null) {
          peekSupportActionBar().setWindowTitle(charSequence);
        } else {
          TextView textView = this.mTitleView;
          if (textView != null)
            textView.setText(charSequence); 
        } 
      } 
      applyFixedSizeWindow();
      onSubDecorInstalled(this.mSubDecor);
      this.mSubDecorInstalled = true;
      PanelFeatureState panelFeatureState = getPanelState(0, false);
      if (!this.mDestroyed && (panelFeatureState == null || panelFeatureState.menu == null))
        invalidatePanelMenu(108); 
    } 
  }
  
  private void ensureWindow() {
    if (this.mWindow == null) {
      Object object = this.mHost;
      if (object instanceof Activity)
        attachToWindow(((Activity)object).getWindow()); 
    } 
    if (this.mWindow != null)
      return; 
    throw new IllegalStateException("We have not been given a Window");
  }
  
  private static Configuration generateConfigDelta(Configuration paramConfiguration1, Configuration paramConfiguration2) {
    Configuration configuration = new Configuration();
    configuration.fontScale = 0.0F;
    if (paramConfiguration2 == null || paramConfiguration1.diff(paramConfiguration2) == 0)
      return configuration; 
    if (paramConfiguration1.fontScale != paramConfiguration2.fontScale)
      configuration.fontScale = paramConfiguration2.fontScale; 
    if (paramConfiguration1.mcc != paramConfiguration2.mcc)
      configuration.mcc = paramConfiguration2.mcc; 
    if (paramConfiguration1.mnc != paramConfiguration2.mnc)
      configuration.mnc = paramConfiguration2.mnc; 
    if (Build.VERSION.SDK_INT >= 24) {
      Api24Impl.generateConfigDelta_locale(paramConfiguration1, paramConfiguration2, configuration);
    } else if (!ObjectsCompat.equals(paramConfiguration1.locale, paramConfiguration2.locale)) {
      configuration.locale = paramConfiguration2.locale;
    } 
    if (paramConfiguration1.touchscreen != paramConfiguration2.touchscreen)
      configuration.touchscreen = paramConfiguration2.touchscreen; 
    if (paramConfiguration1.keyboard != paramConfiguration2.keyboard)
      configuration.keyboard = paramConfiguration2.keyboard; 
    if (paramConfiguration1.keyboardHidden != paramConfiguration2.keyboardHidden)
      configuration.keyboardHidden = paramConfiguration2.keyboardHidden; 
    if (paramConfiguration1.navigation != paramConfiguration2.navigation)
      configuration.navigation = paramConfiguration2.navigation; 
    if (paramConfiguration1.navigationHidden != paramConfiguration2.navigationHidden)
      configuration.navigationHidden = paramConfiguration2.navigationHidden; 
    if (paramConfiguration1.orientation != paramConfiguration2.orientation)
      configuration.orientation = paramConfiguration2.orientation; 
    if ((paramConfiguration1.screenLayout & 0xF) != (paramConfiguration2.screenLayout & 0xF))
      configuration.screenLayout |= paramConfiguration2.screenLayout & 0xF; 
    if ((paramConfiguration1.screenLayout & 0xC0) != (paramConfiguration2.screenLayout & 0xC0))
      configuration.screenLayout |= paramConfiguration2.screenLayout & 0xC0; 
    if ((paramConfiguration1.screenLayout & 0x30) != (paramConfiguration2.screenLayout & 0x30))
      configuration.screenLayout |= paramConfiguration2.screenLayout & 0x30; 
    if ((paramConfiguration1.screenLayout & 0x300) != (paramConfiguration2.screenLayout & 0x300))
      configuration.screenLayout |= paramConfiguration2.screenLayout & 0x300; 
    if (Build.VERSION.SDK_INT >= 26)
      Api26Impl.generateConfigDelta_colorMode(paramConfiguration1, paramConfiguration2, configuration); 
    if ((paramConfiguration1.uiMode & 0xF) != (paramConfiguration2.uiMode & 0xF))
      configuration.uiMode |= paramConfiguration2.uiMode & 0xF; 
    if ((paramConfiguration1.uiMode & 0x30) != (paramConfiguration2.uiMode & 0x30))
      configuration.uiMode |= paramConfiguration2.uiMode & 0x30; 
    if (paramConfiguration1.screenWidthDp != paramConfiguration2.screenWidthDp)
      configuration.screenWidthDp = paramConfiguration2.screenWidthDp; 
    if (paramConfiguration1.screenHeightDp != paramConfiguration2.screenHeightDp)
      configuration.screenHeightDp = paramConfiguration2.screenHeightDp; 
    if (paramConfiguration1.smallestScreenWidthDp != paramConfiguration2.smallestScreenWidthDp)
      configuration.smallestScreenWidthDp = paramConfiguration2.smallestScreenWidthDp; 
    if (Build.VERSION.SDK_INT >= 17)
      Api17Impl.generateConfigDelta_densityDpi(paramConfiguration1, paramConfiguration2, configuration); 
    return configuration;
  }
  
  private AutoNightModeManager getAutoBatteryNightModeManager(Context paramContext) {
    if (this.mAutoBatteryNightModeManager == null)
      this.mAutoBatteryNightModeManager = new AutoBatteryNightModeManager(paramContext); 
    return this.mAutoBatteryNightModeManager;
  }
  
  private AutoNightModeManager getAutoTimeNightModeManager(Context paramContext) {
    if (this.mAutoTimeNightModeManager == null)
      this.mAutoTimeNightModeManager = new AutoTimeNightModeManager(TwilightManager.getInstance(paramContext)); 
    return this.mAutoTimeNightModeManager;
  }
  
  private void initWindowDecorActionBar() {
    ensureSubDecor();
    if (!this.mHasActionBar || this.mActionBar != null)
      return; 
    Object object = this.mHost;
    if (object instanceof Activity) {
      this.mActionBar = new WindowDecorActionBar((Activity)this.mHost, this.mOverlayActionBar);
    } else if (object instanceof Dialog) {
      this.mActionBar = new WindowDecorActionBar((Dialog)this.mHost);
    } 
    object = this.mActionBar;
    if (object != null)
      object.setDefaultDisplayHomeAsUpEnabled(this.mEnableDefaultActionBarUp); 
  }
  
  private boolean initializePanelContent(PanelFeatureState paramPanelFeatureState) {
    View view = paramPanelFeatureState.createdPanelView;
    boolean bool = true;
    if (view != null) {
      paramPanelFeatureState.shownPanelView = paramPanelFeatureState.createdPanelView;
      return true;
    } 
    if (paramPanelFeatureState.menu == null)
      return false; 
    if (this.mPanelMenuPresenterCallback == null)
      this.mPanelMenuPresenterCallback = new PanelMenuPresenterCallback(); 
    paramPanelFeatureState.shownPanelView = (View)paramPanelFeatureState.getListMenuView(this.mPanelMenuPresenterCallback);
    if (paramPanelFeatureState.shownPanelView == null)
      bool = false; 
    return bool;
  }
  
  private boolean initializePanelDecor(PanelFeatureState paramPanelFeatureState) {
    paramPanelFeatureState.setStyle(getActionBarThemedContext());
    paramPanelFeatureState.decorView = (ViewGroup)new ListMenuDecorView(paramPanelFeatureState.listPresenterContext);
    paramPanelFeatureState.gravity = 81;
    return true;
  }
  
  private boolean initializePanelMenu(PanelFeatureState paramPanelFeatureState) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mContext : Landroid/content/Context;
    //   4: astore #4
    //   6: aload_1
    //   7: getfield featureId : I
    //   10: ifeq -> 25
    //   13: aload #4
    //   15: astore_2
    //   16: aload_1
    //   17: getfield featureId : I
    //   20: bipush #108
    //   22: if_icmpne -> 191
    //   25: aload #4
    //   27: astore_2
    //   28: aload_0
    //   29: getfield mDecorContentParent : Landroidx/appcompat/widget/DecorContentParent;
    //   32: ifnull -> 191
    //   35: new android/util/TypedValue
    //   38: dup
    //   39: invokespecial <init> : ()V
    //   42: astore #5
    //   44: aload #4
    //   46: invokevirtual getTheme : ()Landroid/content/res/Resources$Theme;
    //   49: astore #6
    //   51: aload #6
    //   53: getstatic androidx/appcompat/R$attr.actionBarTheme : I
    //   56: aload #5
    //   58: iconst_1
    //   59: invokevirtual resolveAttribute : (ILandroid/util/TypedValue;Z)Z
    //   62: pop
    //   63: aconst_null
    //   64: astore_2
    //   65: aload #5
    //   67: getfield resourceId : I
    //   70: ifeq -> 112
    //   73: aload #4
    //   75: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   78: invokevirtual newTheme : ()Landroid/content/res/Resources$Theme;
    //   81: astore_2
    //   82: aload_2
    //   83: aload #6
    //   85: invokevirtual setTo : (Landroid/content/res/Resources$Theme;)V
    //   88: aload_2
    //   89: aload #5
    //   91: getfield resourceId : I
    //   94: iconst_1
    //   95: invokevirtual applyStyle : (IZ)V
    //   98: aload_2
    //   99: getstatic androidx/appcompat/R$attr.actionBarWidgetTheme : I
    //   102: aload #5
    //   104: iconst_1
    //   105: invokevirtual resolveAttribute : (ILandroid/util/TypedValue;Z)Z
    //   108: pop
    //   109: goto -> 124
    //   112: aload #6
    //   114: getstatic androidx/appcompat/R$attr.actionBarWidgetTheme : I
    //   117: aload #5
    //   119: iconst_1
    //   120: invokevirtual resolveAttribute : (ILandroid/util/TypedValue;Z)Z
    //   123: pop
    //   124: aload_2
    //   125: astore_3
    //   126: aload #5
    //   128: getfield resourceId : I
    //   131: ifeq -> 165
    //   134: aload_2
    //   135: astore_3
    //   136: aload_2
    //   137: ifnonnull -> 155
    //   140: aload #4
    //   142: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   145: invokevirtual newTheme : ()Landroid/content/res/Resources$Theme;
    //   148: astore_3
    //   149: aload_3
    //   150: aload #6
    //   152: invokevirtual setTo : (Landroid/content/res/Resources$Theme;)V
    //   155: aload_3
    //   156: aload #5
    //   158: getfield resourceId : I
    //   161: iconst_1
    //   162: invokevirtual applyStyle : (IZ)V
    //   165: aload #4
    //   167: astore_2
    //   168: aload_3
    //   169: ifnull -> 191
    //   172: new androidx/appcompat/view/ContextThemeWrapper
    //   175: dup
    //   176: aload #4
    //   178: iconst_0
    //   179: invokespecial <init> : (Landroid/content/Context;I)V
    //   182: astore_2
    //   183: aload_2
    //   184: invokevirtual getTheme : ()Landroid/content/res/Resources$Theme;
    //   187: aload_3
    //   188: invokevirtual setTo : (Landroid/content/res/Resources$Theme;)V
    //   191: new androidx/appcompat/view/menu/MenuBuilder
    //   194: dup
    //   195: aload_2
    //   196: invokespecial <init> : (Landroid/content/Context;)V
    //   199: astore_2
    //   200: aload_2
    //   201: aload_0
    //   202: invokevirtual setCallback : (Landroidx/appcompat/view/menu/MenuBuilder$Callback;)V
    //   205: aload_1
    //   206: aload_2
    //   207: invokevirtual setMenu : (Landroidx/appcompat/view/menu/MenuBuilder;)V
    //   210: iconst_1
    //   211: ireturn
  }
  
  private void invalidatePanelMenu(int paramInt) {
    this.mInvalidatePanelMenuFeatures |= 1 << paramInt;
    if (!this.mInvalidatePanelMenuPosted) {
      ViewCompat.postOnAnimation(this.mWindow.getDecorView(), this.mInvalidatePanelMenuRunnable);
      this.mInvalidatePanelMenuPosted = true;
    } 
  }
  
  private boolean isActivityManifestHandlingUiMode() {
    if (!this.mActivityHandlesUiModeChecked && this.mHost instanceof Activity) {
      PackageManager packageManager = this.mContext.getPackageManager();
      if (packageManager == null)
        return false; 
      int i = 0;
      try {
        boolean bool;
        if (Build.VERSION.SDK_INT >= 29) {
          i = 269221888;
        } else if (Build.VERSION.SDK_INT >= 24) {
          i = 786432;
        } 
        ComponentName componentName = new ComponentName();
        this(this.mContext, this.mHost.getClass());
        ActivityInfo activityInfo = packageManager.getActivityInfo(componentName, i);
        if (activityInfo != null && (activityInfo.configChanges & 0x200) != 0) {
          bool = true;
        } else {
          bool = false;
        } 
        this.mActivityHandlesUiMode = bool;
      } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
        Log.d("AppCompatDelegate", "Exception while getting ActivityInfo", (Throwable)nameNotFoundException);
        this.mActivityHandlesUiMode = false;
      } 
    } 
    this.mActivityHandlesUiModeChecked = true;
    return this.mActivityHandlesUiMode;
  }
  
  private boolean onKeyDownPanel(int paramInt, KeyEvent paramKeyEvent) {
    if (paramKeyEvent.getRepeatCount() == 0) {
      PanelFeatureState panelFeatureState = getPanelState(paramInt, true);
      if (!panelFeatureState.isOpen)
        return preparePanel(panelFeatureState, paramKeyEvent); 
    } 
    return false;
  }
  
  private boolean onKeyUpPanel(int paramInt, KeyEvent paramKeyEvent) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mActionMode : Landroidx/appcompat/view/ActionMode;
    //   4: ifnull -> 9
    //   7: iconst_0
    //   8: ireturn
    //   9: iconst_0
    //   10: istore #5
    //   12: aload_0
    //   13: iload_1
    //   14: iconst_1
    //   15: invokevirtual getPanelState : (IZ)Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;
    //   18: astore #7
    //   20: iload_1
    //   21: ifne -> 119
    //   24: aload_0
    //   25: getfield mDecorContentParent : Landroidx/appcompat/widget/DecorContentParent;
    //   28: astore #6
    //   30: aload #6
    //   32: ifnull -> 119
    //   35: aload #6
    //   37: invokeinterface canShowOverflowMenu : ()Z
    //   42: ifeq -> 119
    //   45: aload_0
    //   46: getfield mContext : Landroid/content/Context;
    //   49: invokestatic get : (Landroid/content/Context;)Landroid/view/ViewConfiguration;
    //   52: invokevirtual hasPermanentMenuKey : ()Z
    //   55: ifne -> 119
    //   58: aload_0
    //   59: getfield mDecorContentParent : Landroidx/appcompat/widget/DecorContentParent;
    //   62: invokeinterface isOverflowMenuShowing : ()Z
    //   67: ifne -> 106
    //   70: iload #5
    //   72: istore_3
    //   73: aload_0
    //   74: getfield mDestroyed : Z
    //   77: ifne -> 208
    //   80: iload #5
    //   82: istore_3
    //   83: aload_0
    //   84: aload #7
    //   86: aload_2
    //   87: invokespecial preparePanel : (Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;Landroid/view/KeyEvent;)Z
    //   90: ifeq -> 208
    //   93: aload_0
    //   94: getfield mDecorContentParent : Landroidx/appcompat/widget/DecorContentParent;
    //   97: invokeinterface showOverflowMenu : ()Z
    //   102: istore_3
    //   103: goto -> 208
    //   106: aload_0
    //   107: getfield mDecorContentParent : Landroidx/appcompat/widget/DecorContentParent;
    //   110: invokeinterface hideOverflowMenu : ()Z
    //   115: istore_3
    //   116: goto -> 208
    //   119: aload #7
    //   121: getfield isOpen : Z
    //   124: ifne -> 195
    //   127: aload #7
    //   129: getfield isHandled : Z
    //   132: ifeq -> 138
    //   135: goto -> 195
    //   138: iload #5
    //   140: istore_3
    //   141: aload #7
    //   143: getfield isPrepared : Z
    //   146: ifeq -> 208
    //   149: iconst_1
    //   150: istore #4
    //   152: aload #7
    //   154: getfield refreshMenuContent : Z
    //   157: ifeq -> 175
    //   160: aload #7
    //   162: iconst_0
    //   163: putfield isPrepared : Z
    //   166: aload_0
    //   167: aload #7
    //   169: aload_2
    //   170: invokespecial preparePanel : (Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;Landroid/view/KeyEvent;)Z
    //   173: istore #4
    //   175: iload #5
    //   177: istore_3
    //   178: iload #4
    //   180: ifeq -> 208
    //   183: aload_0
    //   184: aload #7
    //   186: aload_2
    //   187: invokespecial openPanel : (Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;Landroid/view/KeyEvent;)V
    //   190: iconst_1
    //   191: istore_3
    //   192: goto -> 208
    //   195: aload #7
    //   197: getfield isOpen : Z
    //   200: istore_3
    //   201: aload_0
    //   202: aload #7
    //   204: iconst_1
    //   205: invokevirtual closePanel : (Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;Z)V
    //   208: iload_3
    //   209: ifeq -> 251
    //   212: aload_0
    //   213: getfield mContext : Landroid/content/Context;
    //   216: invokevirtual getApplicationContext : ()Landroid/content/Context;
    //   219: ldc_w 'audio'
    //   222: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   225: checkcast android/media/AudioManager
    //   228: astore_2
    //   229: aload_2
    //   230: ifnull -> 241
    //   233: aload_2
    //   234: iconst_0
    //   235: invokevirtual playSoundEffect : (I)V
    //   238: goto -> 251
    //   241: ldc_w 'AppCompatDelegate'
    //   244: ldc_w 'Couldn't get audio manager'
    //   247: invokestatic w : (Ljava/lang/String;Ljava/lang/String;)I
    //   250: pop
    //   251: iload_3
    //   252: ireturn
  }
  
  private void openPanel(PanelFeatureState paramPanelFeatureState, KeyEvent paramKeyEvent) {
    // Byte code:
    //   0: aload_1
    //   1: getfield isOpen : Z
    //   4: ifne -> 429
    //   7: aload_0
    //   8: getfield mDestroyed : Z
    //   11: ifeq -> 17
    //   14: goto -> 429
    //   17: aload_1
    //   18: getfield featureId : I
    //   21: ifne -> 56
    //   24: aload_0
    //   25: getfield mContext : Landroid/content/Context;
    //   28: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   31: invokevirtual getConfiguration : ()Landroid/content/res/Configuration;
    //   34: getfield screenLayout : I
    //   37: bipush #15
    //   39: iand
    //   40: iconst_4
    //   41: if_icmpne -> 49
    //   44: iconst_1
    //   45: istore_3
    //   46: goto -> 51
    //   49: iconst_0
    //   50: istore_3
    //   51: iload_3
    //   52: ifeq -> 56
    //   55: return
    //   56: aload_0
    //   57: invokevirtual getWindowCallback : ()Landroid/view/Window$Callback;
    //   60: astore #5
    //   62: aload #5
    //   64: ifnull -> 92
    //   67: aload #5
    //   69: aload_1
    //   70: getfield featureId : I
    //   73: aload_1
    //   74: getfield menu : Landroidx/appcompat/view/menu/MenuBuilder;
    //   77: invokeinterface onMenuOpened : (ILandroid/view/Menu;)Z
    //   82: ifne -> 92
    //   85: aload_0
    //   86: aload_1
    //   87: iconst_1
    //   88: invokevirtual closePanel : (Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;Z)V
    //   91: return
    //   92: aload_0
    //   93: getfield mContext : Landroid/content/Context;
    //   96: ldc_w 'window'
    //   99: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   102: checkcast android/view/WindowManager
    //   105: astore #6
    //   107: aload #6
    //   109: ifnonnull -> 113
    //   112: return
    //   113: aload_0
    //   114: aload_1
    //   115: aload_2
    //   116: invokespecial preparePanel : (Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;Landroid/view/KeyEvent;)Z
    //   119: ifne -> 123
    //   122: return
    //   123: bipush #-2
    //   125: istore #4
    //   127: aload_1
    //   128: getfield decorView : Landroid/view/ViewGroup;
    //   131: ifnull -> 188
    //   134: aload_1
    //   135: getfield refreshDecorView : Z
    //   138: ifeq -> 144
    //   141: goto -> 188
    //   144: aload_1
    //   145: getfield createdPanelView : Landroid/view/View;
    //   148: ifnull -> 182
    //   151: aload_1
    //   152: getfield createdPanelView : Landroid/view/View;
    //   155: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   158: astore_2
    //   159: iload #4
    //   161: istore_3
    //   162: aload_2
    //   163: ifnull -> 357
    //   166: iload #4
    //   168: istore_3
    //   169: aload_2
    //   170: getfield width : I
    //   173: iconst_m1
    //   174: if_icmpne -> 357
    //   177: iconst_m1
    //   178: istore_3
    //   179: goto -> 357
    //   182: iload #4
    //   184: istore_3
    //   185: goto -> 357
    //   188: aload_1
    //   189: getfield decorView : Landroid/view/ViewGroup;
    //   192: ifnonnull -> 211
    //   195: aload_0
    //   196: aload_1
    //   197: invokespecial initializePanelDecor : (Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;)Z
    //   200: ifeq -> 210
    //   203: aload_1
    //   204: getfield decorView : Landroid/view/ViewGroup;
    //   207: ifnonnull -> 235
    //   210: return
    //   211: aload_1
    //   212: getfield refreshDecorView : Z
    //   215: ifeq -> 235
    //   218: aload_1
    //   219: getfield decorView : Landroid/view/ViewGroup;
    //   222: invokevirtual getChildCount : ()I
    //   225: ifle -> 235
    //   228: aload_1
    //   229: getfield decorView : Landroid/view/ViewGroup;
    //   232: invokevirtual removeAllViews : ()V
    //   235: aload_0
    //   236: aload_1
    //   237: invokespecial initializePanelContent : (Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;)Z
    //   240: ifeq -> 423
    //   243: aload_1
    //   244: invokevirtual hasPanelItems : ()Z
    //   247: ifne -> 253
    //   250: goto -> 423
    //   253: aload_1
    //   254: getfield shownPanelView : Landroid/view/View;
    //   257: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   260: astore #5
    //   262: aload #5
    //   264: astore_2
    //   265: aload #5
    //   267: ifnonnull -> 282
    //   270: new android/view/ViewGroup$LayoutParams
    //   273: dup
    //   274: bipush #-2
    //   276: bipush #-2
    //   278: invokespecial <init> : (II)V
    //   281: astore_2
    //   282: aload_1
    //   283: getfield background : I
    //   286: istore_3
    //   287: aload_1
    //   288: getfield decorView : Landroid/view/ViewGroup;
    //   291: iload_3
    //   292: invokevirtual setBackgroundResource : (I)V
    //   295: aload_1
    //   296: getfield shownPanelView : Landroid/view/View;
    //   299: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   302: astore #5
    //   304: aload #5
    //   306: instanceof android/view/ViewGroup
    //   309: ifeq -> 324
    //   312: aload #5
    //   314: checkcast android/view/ViewGroup
    //   317: aload_1
    //   318: getfield shownPanelView : Landroid/view/View;
    //   321: invokevirtual removeView : (Landroid/view/View;)V
    //   324: aload_1
    //   325: getfield decorView : Landroid/view/ViewGroup;
    //   328: aload_1
    //   329: getfield shownPanelView : Landroid/view/View;
    //   332: aload_2
    //   333: invokevirtual addView : (Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V
    //   336: aload_1
    //   337: getfield shownPanelView : Landroid/view/View;
    //   340: invokevirtual hasFocus : ()Z
    //   343: ifne -> 182
    //   346: aload_1
    //   347: getfield shownPanelView : Landroid/view/View;
    //   350: invokevirtual requestFocus : ()Z
    //   353: pop
    //   354: goto -> 182
    //   357: aload_1
    //   358: iconst_0
    //   359: putfield isHandled : Z
    //   362: new android/view/WindowManager$LayoutParams
    //   365: dup
    //   366: iload_3
    //   367: bipush #-2
    //   369: aload_1
    //   370: getfield x : I
    //   373: aload_1
    //   374: getfield y : I
    //   377: sipush #1002
    //   380: ldc_w 8519680
    //   383: bipush #-3
    //   385: invokespecial <init> : (IIIIIII)V
    //   388: astore_2
    //   389: aload_2
    //   390: aload_1
    //   391: getfield gravity : I
    //   394: putfield gravity : I
    //   397: aload_2
    //   398: aload_1
    //   399: getfield windowAnimations : I
    //   402: putfield windowAnimations : I
    //   405: aload #6
    //   407: aload_1
    //   408: getfield decorView : Landroid/view/ViewGroup;
    //   411: aload_2
    //   412: invokeinterface addView : (Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V
    //   417: aload_1
    //   418: iconst_1
    //   419: putfield isOpen : Z
    //   422: return
    //   423: aload_1
    //   424: iconst_1
    //   425: putfield refreshDecorView : Z
    //   428: return
    //   429: return
  }
  
  private boolean performPanelShortcut(PanelFeatureState paramPanelFeatureState, int paramInt1, KeyEvent paramKeyEvent, int paramInt2) {
    // Byte code:
    //   0: aload_3
    //   1: invokevirtual isSystem : ()Z
    //   4: ifeq -> 9
    //   7: iconst_0
    //   8: ireturn
    //   9: iconst_0
    //   10: istore #6
    //   12: aload_1
    //   13: getfield isPrepared : Z
    //   16: ifne -> 32
    //   19: iload #6
    //   21: istore #5
    //   23: aload_0
    //   24: aload_1
    //   25: aload_3
    //   26: invokespecial preparePanel : (Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;Landroid/view/KeyEvent;)Z
    //   29: ifeq -> 56
    //   32: iload #6
    //   34: istore #5
    //   36: aload_1
    //   37: getfield menu : Landroidx/appcompat/view/menu/MenuBuilder;
    //   40: ifnull -> 56
    //   43: aload_1
    //   44: getfield menu : Landroidx/appcompat/view/menu/MenuBuilder;
    //   47: iload_2
    //   48: aload_3
    //   49: iload #4
    //   51: invokevirtual performShortcut : (ILandroid/view/KeyEvent;I)Z
    //   54: istore #5
    //   56: iload #5
    //   58: ifeq -> 81
    //   61: iload #4
    //   63: iconst_1
    //   64: iand
    //   65: ifne -> 81
    //   68: aload_0
    //   69: getfield mDecorContentParent : Landroidx/appcompat/widget/DecorContentParent;
    //   72: ifnonnull -> 81
    //   75: aload_0
    //   76: aload_1
    //   77: iconst_1
    //   78: invokevirtual closePanel : (Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;Z)V
    //   81: iload #5
    //   83: ireturn
  }
  
  private boolean preparePanel(PanelFeatureState paramPanelFeatureState, KeyEvent paramKeyEvent) {
    DecorContentParent decorContentParent;
    int i;
    if (this.mDestroyed)
      return false; 
    if (paramPanelFeatureState.isPrepared)
      return true; 
    PanelFeatureState panelFeatureState = this.mPreparedPanel;
    if (panelFeatureState != null && panelFeatureState != paramPanelFeatureState)
      closePanel(panelFeatureState, false); 
    Window.Callback callback = getWindowCallback();
    if (callback != null)
      paramPanelFeatureState.createdPanelView = callback.onCreatePanelView(paramPanelFeatureState.featureId); 
    if (paramPanelFeatureState.featureId == 0 || paramPanelFeatureState.featureId == 108) {
      i = 1;
    } else {
      i = 0;
    } 
    if (i) {
      DecorContentParent decorContentParent1 = this.mDecorContentParent;
      if (decorContentParent1 != null)
        decorContentParent1.setMenuPrepared(); 
    } 
    if (paramPanelFeatureState.createdPanelView == null && (!i || !(peekSupportActionBar() instanceof ToolbarActionBar))) {
      DecorContentParent decorContentParent1;
      boolean bool;
      if (paramPanelFeatureState.menu == null || paramPanelFeatureState.refreshMenuContent) {
        if (paramPanelFeatureState.menu == null && (!initializePanelMenu(paramPanelFeatureState) || paramPanelFeatureState.menu == null))
          return false; 
        if (i && this.mDecorContentParent != null) {
          if (this.mActionMenuPresenterCallback == null)
            this.mActionMenuPresenterCallback = new ActionMenuPresenterCallback(); 
          this.mDecorContentParent.setMenu((Menu)paramPanelFeatureState.menu, this.mActionMenuPresenterCallback);
        } 
        paramPanelFeatureState.menu.stopDispatchingItemsChanged();
        if (!callback.onCreatePanelMenu(paramPanelFeatureState.featureId, (Menu)paramPanelFeatureState.menu)) {
          paramPanelFeatureState.setMenu(null);
          if (i) {
            decorContentParent = this.mDecorContentParent;
            if (decorContentParent != null)
              decorContentParent.setMenu(null, this.mActionMenuPresenterCallback); 
          } 
          return false;
        } 
        ((PanelFeatureState)decorContentParent).refreshMenuContent = false;
      } 
      ((PanelFeatureState)decorContentParent).menu.stopDispatchingItemsChanged();
      if (((PanelFeatureState)decorContentParent).frozenActionViewState != null) {
        ((PanelFeatureState)decorContentParent).menu.restoreActionViewStates(((PanelFeatureState)decorContentParent).frozenActionViewState);
        ((PanelFeatureState)decorContentParent).frozenActionViewState = null;
      } 
      if (!callback.onPreparePanel(0, ((PanelFeatureState)decorContentParent).createdPanelView, (Menu)((PanelFeatureState)decorContentParent).menu)) {
        if (i) {
          decorContentParent1 = this.mDecorContentParent;
          if (decorContentParent1 != null)
            decorContentParent1.setMenu(null, this.mActionMenuPresenterCallback); 
        } 
        ((PanelFeatureState)decorContentParent).menu.startDispatchingItemsChanged();
        return false;
      } 
      if (decorContentParent1 != null) {
        i = decorContentParent1.getDeviceId();
      } else {
        i = -1;
      } 
      if (KeyCharacterMap.load(i).getKeyboardType() != 1) {
        bool = true;
      } else {
        bool = false;
      } 
      ((PanelFeatureState)decorContentParent).qwertyMode = bool;
      ((PanelFeatureState)decorContentParent).menu.setQwertyMode(((PanelFeatureState)decorContentParent).qwertyMode);
      ((PanelFeatureState)decorContentParent).menu.startDispatchingItemsChanged();
    } 
    ((PanelFeatureState)decorContentParent).isPrepared = true;
    ((PanelFeatureState)decorContentParent).isHandled = false;
    this.mPreparedPanel = (PanelFeatureState)decorContentParent;
    return true;
  }
  
  private void reopenMenu(boolean paramBoolean) {
    DecorContentParent decorContentParent = this.mDecorContentParent;
    if (decorContentParent != null && decorContentParent.canShowOverflowMenu() && (!ViewConfiguration.get(this.mContext).hasPermanentMenuKey() || this.mDecorContentParent.isOverflowMenuShowPending())) {
      Window.Callback callback = getWindowCallback();
      if (!this.mDecorContentParent.isOverflowMenuShowing() || !paramBoolean) {
        if (callback != null && !this.mDestroyed) {
          if (this.mInvalidatePanelMenuPosted && (this.mInvalidatePanelMenuFeatures & 0x1) != 0) {
            this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
            this.mInvalidatePanelMenuRunnable.run();
          } 
          PanelFeatureState panelFeatureState1 = getPanelState(0, true);
          if (panelFeatureState1.menu != null && !panelFeatureState1.refreshMenuContent && callback.onPreparePanel(0, panelFeatureState1.createdPanelView, (Menu)panelFeatureState1.menu)) {
            callback.onMenuOpened(108, (Menu)panelFeatureState1.menu);
            this.mDecorContentParent.showOverflowMenu();
          } 
        } 
        return;
      } 
      this.mDecorContentParent.hideOverflowMenu();
      if (!this.mDestroyed)
        callback.onPanelClosed(108, (Menu)(getPanelState(0, true)).menu); 
      return;
    } 
    PanelFeatureState panelFeatureState = getPanelState(0, true);
    panelFeatureState.refreshDecorView = true;
    closePanel(panelFeatureState, false);
    openPanel(panelFeatureState, (KeyEvent)null);
  }
  
  private int sanitizeWindowFeatureId(int paramInt) {
    if (paramInt == 8) {
      Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR id when requesting this feature.");
      return 108;
    } 
    if (paramInt == 9) {
      Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY id when requesting this feature.");
      return 109;
    } 
    return paramInt;
  }
  
  private boolean shouldInheritContext(ViewParent paramViewParent) {
    if (paramViewParent == null)
      return false; 
    View view = this.mWindow.getDecorView();
    while (true) {
      if (paramViewParent == null)
        return true; 
      if (paramViewParent == view || !(paramViewParent instanceof View) || ViewCompat.isAttachedToWindow((View)paramViewParent))
        break; 
      paramViewParent = paramViewParent.getParent();
    } 
    return false;
  }
  
  private void throwFeatureRequestIfSubDecorInstalled() {
    if (!this.mSubDecorInstalled)
      return; 
    throw new AndroidRuntimeException("Window feature must be requested before adding content");
  }
  
  private AppCompatActivity tryUnwrapContext() {
    Context context = this.mContext;
    while (context != null) {
      if (context instanceof AppCompatActivity)
        return (AppCompatActivity)context; 
      if (context instanceof ContextWrapper) {
        context = ((ContextWrapper)context).getBaseContext();
        continue;
      } 
      return null;
    } 
    return null;
  }
  
  private boolean updateForNightMode(int paramInt, boolean paramBoolean) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #6
    //   3: aload_0
    //   4: aload_0
    //   5: getfield mContext : Landroid/content/Context;
    //   8: iload_1
    //   9: aconst_null
    //   10: invokespecial createOverrideConfigurationForDayNight : (Landroid/content/Context;ILandroid/content/res/Configuration;)Landroid/content/res/Configuration;
    //   13: astore #9
    //   15: aload_0
    //   16: invokespecial isActivityManifestHandlingUiMode : ()Z
    //   19: istore #7
    //   21: aload_0
    //   22: getfield mEffectiveConfiguration : Landroid/content/res/Configuration;
    //   25: astore #8
    //   27: aload #8
    //   29: ifnonnull -> 47
    //   32: aload_0
    //   33: getfield mContext : Landroid/content/Context;
    //   36: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   39: invokevirtual getConfiguration : ()Landroid/content/res/Configuration;
    //   42: astore #8
    //   44: goto -> 47
    //   47: aload #8
    //   49: getfield uiMode : I
    //   52: bipush #48
    //   54: iand
    //   55: istore_3
    //   56: aload #9
    //   58: getfield uiMode : I
    //   61: bipush #48
    //   63: iand
    //   64: istore #4
    //   66: iload #6
    //   68: istore #5
    //   70: iload_3
    //   71: iload #4
    //   73: if_icmpeq -> 167
    //   76: iload #6
    //   78: istore #5
    //   80: iload_2
    //   81: ifeq -> 167
    //   84: iload #6
    //   86: istore #5
    //   88: iload #7
    //   90: ifne -> 167
    //   93: iload #6
    //   95: istore #5
    //   97: aload_0
    //   98: getfield mBaseContextAttached : Z
    //   101: ifeq -> 167
    //   104: getstatic androidx/appcompat/app/AppCompatDelegateImpl.sCanReturnDifferentContext : Z
    //   107: ifne -> 121
    //   110: iload #6
    //   112: istore #5
    //   114: aload_0
    //   115: getfield mCreated : Z
    //   118: ifeq -> 167
    //   121: aload_0
    //   122: getfield mHost : Ljava/lang/Object;
    //   125: astore #8
    //   127: iload #6
    //   129: istore #5
    //   131: aload #8
    //   133: instanceof android/app/Activity
    //   136: ifeq -> 167
    //   139: iload #6
    //   141: istore #5
    //   143: aload #8
    //   145: checkcast android/app/Activity
    //   148: invokevirtual isChild : ()Z
    //   151: ifne -> 167
    //   154: aload_0
    //   155: getfield mHost : Ljava/lang/Object;
    //   158: checkcast android/app/Activity
    //   161: invokestatic recreate : (Landroid/app/Activity;)V
    //   164: iconst_1
    //   165: istore #5
    //   167: iload #5
    //   169: istore_2
    //   170: iload #5
    //   172: ifne -> 195
    //   175: iload #5
    //   177: istore_2
    //   178: iload_3
    //   179: iload #4
    //   181: if_icmpeq -> 195
    //   184: aload_0
    //   185: iload #4
    //   187: iload #7
    //   189: aconst_null
    //   190: invokespecial updateResourcesConfigurationForNightMode : (IZLandroid/content/res/Configuration;)V
    //   193: iconst_1
    //   194: istore_2
    //   195: iload_2
    //   196: ifeq -> 222
    //   199: aload_0
    //   200: getfield mHost : Ljava/lang/Object;
    //   203: astore #8
    //   205: aload #8
    //   207: instanceof androidx/appcompat/app/AppCompatActivity
    //   210: ifeq -> 222
    //   213: aload #8
    //   215: checkcast androidx/appcompat/app/AppCompatActivity
    //   218: iload_1
    //   219: invokevirtual onNightModeChanged : (I)V
    //   222: iload_2
    //   223: ireturn
  }
  
  private void updateResourcesConfigurationForNightMode(int paramInt, boolean paramBoolean, Configuration paramConfiguration) {
    Resources resources = this.mContext.getResources();
    Configuration configuration = new Configuration(resources.getConfiguration());
    if (paramConfiguration != null)
      configuration.updateFrom(paramConfiguration); 
    configuration.uiMode = (resources.getConfiguration()).uiMode & 0xFFFFFFCF | paramInt;
    resources.updateConfiguration(configuration, null);
    if (Build.VERSION.SDK_INT < 26)
      ResourcesFlusher.flush(resources); 
    paramInt = this.mThemeResId;
    if (paramInt != 0) {
      this.mContext.setTheme(paramInt);
      if (Build.VERSION.SDK_INT >= 23)
        this.mContext.getTheme().applyStyle(this.mThemeResId, true); 
    } 
    if (paramBoolean) {
      Object object = this.mHost;
      if (object instanceof Activity) {
        object = object;
        if (object instanceof LifecycleOwner) {
          if (((LifecycleOwner)object).getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED))
            object.onConfigurationChanged(configuration); 
        } else if (this.mCreated && !this.mDestroyed) {
          object.onConfigurationChanged(configuration);
        } 
      } 
    } 
  }
  
  private void updateStatusGuardColor(View paramView) {
    int i;
    if ((ViewCompat.getWindowSystemUiVisibility(paramView) & 0x2000) != 0) {
      i = 1;
    } else {
      i = 0;
    } 
    if (i) {
      i = ContextCompat.getColor(this.mContext, R.color.abc_decor_view_status_guard_light);
    } else {
      i = ContextCompat.getColor(this.mContext, R.color.abc_decor_view_status_guard);
    } 
    paramView.setBackgroundColor(i);
  }
  
  public void addContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    ensureSubDecor();
    ((ViewGroup)this.mSubDecor.findViewById(16908290)).addView(paramView, paramLayoutParams);
    this.mAppCompatWindowCallback.getWrapped().onContentChanged();
  }
  
  public boolean applyDayNight() {
    return applyDayNight(true);
  }
  
  public Context attachBaseContext2(Context paramContext) {
    boolean bool = true;
    this.mBaseContextAttached = true;
    int i = mapNightMode(paramContext, calculateNightMode());
    if (sCanApplyOverrideConfiguration && paramContext instanceof ContextThemeWrapper) {
      Configuration configuration = createOverrideConfigurationForDayNight(paramContext, i, (Configuration)null);
      try {
        ContextThemeWrapperCompatApi17Impl.applyOverrideConfiguration((ContextThemeWrapper)paramContext, configuration);
        return paramContext;
      } catch (IllegalStateException illegalStateException) {}
    } 
    if (paramContext instanceof ContextThemeWrapper) {
      Configuration configuration = createOverrideConfigurationForDayNight(paramContext, i, (Configuration)null);
      try {
        ((ContextThemeWrapper)paramContext).applyOverrideConfiguration(configuration);
        return paramContext;
      } catch (IllegalStateException illegalStateException) {}
    } 
    if (!sCanReturnDifferentContext)
      return super.attachBaseContext2(paramContext); 
    Configuration configuration2 = null;
    Configuration configuration1 = configuration2;
    if (Build.VERSION.SDK_INT >= 17) {
      configuration1 = new Configuration();
      configuration1.uiMode = -1;
      configuration1.fontScale = 0.0F;
      Configuration configuration4 = Api17Impl.createConfigurationContext(paramContext, configuration1).getResources().getConfiguration();
      Configuration configuration3 = paramContext.getResources().getConfiguration();
      configuration4.uiMode = configuration3.uiMode;
      configuration1 = configuration2;
      if (!configuration4.equals(configuration3))
        configuration1 = generateConfigDelta(configuration4, configuration3); 
    } 
    configuration2 = createOverrideConfigurationForDayNight(paramContext, i, configuration1);
    ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(paramContext, R.style.Theme_AppCompat_Empty);
    contextThemeWrapper.applyOverrideConfiguration(configuration2);
    try {
      Resources.Theme theme = paramContext.getTheme();
      if (theme == null)
        bool = false; 
    } catch (NullPointerException nullPointerException) {
      bool = false;
    } 
    if (bool)
      ResourcesCompat.ThemeCompat.rebase(contextThemeWrapper.getTheme()); 
    return super.attachBaseContext2((Context)contextThemeWrapper);
  }
  
  void callOnPanelClosed(int paramInt, PanelFeatureState paramPanelFeatureState, Menu paramMenu) {
    MenuBuilder menuBuilder;
    PanelFeatureState panelFeatureState = paramPanelFeatureState;
    Menu menu = paramMenu;
    if (paramMenu == null) {
      PanelFeatureState panelFeatureState1 = paramPanelFeatureState;
      if (paramPanelFeatureState == null) {
        panelFeatureState1 = paramPanelFeatureState;
        if (paramInt >= 0) {
          PanelFeatureState[] arrayOfPanelFeatureState = this.mPanels;
          panelFeatureState1 = paramPanelFeatureState;
          if (paramInt < arrayOfPanelFeatureState.length)
            panelFeatureState1 = arrayOfPanelFeatureState[paramInt]; 
        } 
      } 
      panelFeatureState = panelFeatureState1;
      menu = paramMenu;
      if (panelFeatureState1 != null) {
        menuBuilder = panelFeatureState1.menu;
        panelFeatureState = panelFeatureState1;
      } 
    } 
    if (panelFeatureState != null && !panelFeatureState.isOpen)
      return; 
    if (!this.mDestroyed)
      this.mAppCompatWindowCallback.getWrapped().onPanelClosed(paramInt, (Menu)menuBuilder); 
  }
  
  void checkCloseActionMenu(MenuBuilder paramMenuBuilder) {
    if (this.mClosingActionMenu)
      return; 
    this.mClosingActionMenu = true;
    this.mDecorContentParent.dismissPopups();
    Window.Callback callback = getWindowCallback();
    if (callback != null && !this.mDestroyed)
      callback.onPanelClosed(108, (Menu)paramMenuBuilder); 
    this.mClosingActionMenu = false;
  }
  
  void closePanel(int paramInt) {
    closePanel(getPanelState(paramInt, true), true);
  }
  
  void closePanel(PanelFeatureState paramPanelFeatureState, boolean paramBoolean) {
    if (paramBoolean && paramPanelFeatureState.featureId == 0) {
      DecorContentParent decorContentParent = this.mDecorContentParent;
      if (decorContentParent != null && decorContentParent.isOverflowMenuShowing()) {
        checkCloseActionMenu(paramPanelFeatureState.menu);
        return;
      } 
    } 
    WindowManager windowManager = (WindowManager)this.mContext.getSystemService("window");
    if (windowManager != null && paramPanelFeatureState.isOpen && paramPanelFeatureState.decorView != null) {
      windowManager.removeView((View)paramPanelFeatureState.decorView);
      if (paramBoolean)
        callOnPanelClosed(paramPanelFeatureState.featureId, paramPanelFeatureState, (Menu)null); 
    } 
    paramPanelFeatureState.isPrepared = false;
    paramPanelFeatureState.isHandled = false;
    paramPanelFeatureState.isOpen = false;
    paramPanelFeatureState.shownPanelView = null;
    paramPanelFeatureState.refreshDecorView = true;
    if (this.mPreparedPanel == paramPanelFeatureState)
      this.mPreparedPanel = null; 
  }
  
  public View createView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet) {
    AppCompatViewInflater appCompatViewInflater = this.mAppCompatViewInflater;
    boolean bool2 = false;
    if (appCompatViewInflater == null) {
      String str = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme).getString(R.styleable.AppCompatTheme_viewInflaterClass);
      if (str == null) {
        this.mAppCompatViewInflater = new AppCompatViewInflater();
      } else {
        try {
          this.mAppCompatViewInflater = this.mContext.getClassLoader().loadClass(str).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } finally {
          Exception exception = null;
          Log.i("AppCompatDelegate", "Failed to instantiate custom view inflater " + str + ". Falling back to default.", exception);
        } 
      } 
    } 
    boolean bool1 = false;
    boolean bool3 = IS_PRE_LOLLIPOP;
    if (bool3) {
      if (this.mLayoutIncludeDetector == null)
        this.mLayoutIncludeDetector = new LayoutIncludeDetector(); 
      if (this.mLayoutIncludeDetector.detect(paramAttributeSet)) {
        bool1 = true;
      } else if (paramAttributeSet instanceof XmlPullParser) {
        bool1 = bool2;
        if (((XmlPullParser)paramAttributeSet).getDepth() > 1)
          bool1 = true; 
      } else {
        bool1 = shouldInheritContext((ViewParent)paramView);
      } 
    } 
    return this.mAppCompatViewInflater.createView(paramView, paramString, paramContext, paramAttributeSet, bool1, bool3, true, VectorEnabledTintResources.shouldBeUsed());
  }
  
  void dismissPopups() {
    DecorContentParent decorContentParent = this.mDecorContentParent;
    if (decorContentParent != null)
      decorContentParent.dismissPopups(); 
    if (this.mActionModePopup != null) {
      this.mWindow.getDecorView().removeCallbacks(this.mShowActionModePopup);
      if (this.mActionModePopup.isShowing())
        try {
          this.mActionModePopup.dismiss();
        } catch (IllegalArgumentException illegalArgumentException) {} 
      this.mActionModePopup = null;
    } 
    endOnGoingFadeAnimation();
    PanelFeatureState panelFeatureState = getPanelState(0, false);
    if (panelFeatureState != null && panelFeatureState.menu != null)
      panelFeatureState.menu.close(); 
  }
  
  boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
    Object object = this.mHost;
    boolean bool1 = object instanceof KeyEventDispatcher.Component;
    boolean bool = true;
    if (bool1 || object instanceof AppCompatDialog) {
      object = this.mWindow.getDecorView();
      if (object != null && KeyEventDispatcher.dispatchBeforeHierarchy((View)object, paramKeyEvent))
        return true; 
    } 
    if (paramKeyEvent.getKeyCode() == 82 && this.mAppCompatWindowCallback.getWrapped().dispatchKeyEvent(paramKeyEvent))
      return true; 
    int i = paramKeyEvent.getKeyCode();
    if (paramKeyEvent.getAction() != 0)
      bool = false; 
    if (bool) {
      bool1 = onKeyDown(i, paramKeyEvent);
    } else {
      bool1 = onKeyUp(i, paramKeyEvent);
    } 
    return bool1;
  }
  
  void doInvalidatePanelMenu(int paramInt) {
    PanelFeatureState panelFeatureState = getPanelState(paramInt, true);
    if (panelFeatureState.menu != null) {
      Bundle bundle = new Bundle();
      panelFeatureState.menu.saveActionViewStates(bundle);
      if (bundle.size() > 0)
        panelFeatureState.frozenActionViewState = bundle; 
      panelFeatureState.menu.stopDispatchingItemsChanged();
      panelFeatureState.menu.clear();
    } 
    panelFeatureState.refreshMenuContent = true;
    panelFeatureState.refreshDecorView = true;
    if ((paramInt == 108 || paramInt == 0) && this.mDecorContentParent != null) {
      panelFeatureState = getPanelState(0, false);
      if (panelFeatureState != null) {
        panelFeatureState.isPrepared = false;
        preparePanel(panelFeatureState, (KeyEvent)null);
      } 
    } 
  }
  
  void endOnGoingFadeAnimation() {
    ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = this.mFadeAnim;
    if (viewPropertyAnimatorCompat != null)
      viewPropertyAnimatorCompat.cancel(); 
  }
  
  PanelFeatureState findMenuPanel(Menu paramMenu) {
    byte b1;
    PanelFeatureState[] arrayOfPanelFeatureState = this.mPanels;
    if (arrayOfPanelFeatureState != null) {
      b1 = arrayOfPanelFeatureState.length;
    } else {
      b1 = 0;
    } 
    for (byte b2 = 0; b2 < b1; b2++) {
      PanelFeatureState panelFeatureState = arrayOfPanelFeatureState[b2];
      if (panelFeatureState != null && panelFeatureState.menu == paramMenu)
        return panelFeatureState; 
    } 
    return null;
  }
  
  public <T extends View> T findViewById(int paramInt) {
    ensureSubDecor();
    return (T)this.mWindow.findViewById(paramInt);
  }
  
  final Context getActionBarThemedContext() {
    Context context1 = null;
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null)
      context1 = actionBar.getThemedContext(); 
    Context context2 = context1;
    if (context1 == null)
      context2 = this.mContext; 
    return context2;
  }
  
  final AutoNightModeManager getAutoTimeNightModeManager() {
    return getAutoTimeNightModeManager(this.mContext);
  }
  
  public final ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() {
    return new ActionBarDrawableToggleImpl();
  }
  
  public int getLocalNightMode() {
    return this.mLocalNightMode;
  }
  
  public MenuInflater getMenuInflater() {
    if (this.mMenuInflater == null) {
      Context context;
      initWindowDecorActionBar();
      ActionBar actionBar = this.mActionBar;
      if (actionBar != null) {
        context = actionBar.getThemedContext();
      } else {
        context = this.mContext;
      } 
      this.mMenuInflater = (MenuInflater)new SupportMenuInflater(context);
    } 
    return this.mMenuInflater;
  }
  
  protected PanelFeatureState getPanelState(int paramInt, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mPanels : [Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;
    //   4: astore_3
    //   5: aload_3
    //   6: astore #4
    //   8: aload_3
    //   9: ifnull -> 22
    //   12: aload #4
    //   14: astore_3
    //   15: aload #4
    //   17: arraylength
    //   18: iload_1
    //   19: if_icmpgt -> 56
    //   22: iload_1
    //   23: iconst_1
    //   24: iadd
    //   25: anewarray androidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState
    //   28: astore #5
    //   30: aload #4
    //   32: ifnull -> 47
    //   35: aload #4
    //   37: iconst_0
    //   38: aload #5
    //   40: iconst_0
    //   41: aload #4
    //   43: arraylength
    //   44: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   47: aload #5
    //   49: astore_3
    //   50: aload_0
    //   51: aload #5
    //   53: putfield mPanels : [Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;
    //   56: aload_3
    //   57: iload_1
    //   58: aaload
    //   59: astore #5
    //   61: aload #5
    //   63: astore #4
    //   65: aload #5
    //   67: ifnonnull -> 89
    //   70: new androidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState
    //   73: dup
    //   74: iload_1
    //   75: invokespecial <init> : (I)V
    //   78: astore #5
    //   80: aload #5
    //   82: astore #4
    //   84: aload_3
    //   85: iload_1
    //   86: aload #5
    //   88: aastore
    //   89: aload #4
    //   91: areturn
  }
  
  ViewGroup getSubDecor() {
    return this.mSubDecor;
  }
  
  public ActionBar getSupportActionBar() {
    initWindowDecorActionBar();
    return this.mActionBar;
  }
  
  final CharSequence getTitle() {
    Object object = this.mHost;
    return (object instanceof Activity) ? ((Activity)object).getTitle() : this.mTitle;
  }
  
  final Window.Callback getWindowCallback() {
    return this.mWindow.getCallback();
  }
  
  public boolean hasWindowFeature(int paramInt) {
    null = false;
    switch (sanitizeWindowFeatureId(paramInt)) {
      case 109:
        null = this.mOverlayActionBar;
        break;
      case 108:
        null = this.mHasActionBar;
        break;
      case 10:
        null = this.mOverlayActionMode;
        break;
      case 5:
        null = this.mFeatureIndeterminateProgress;
        break;
      case 2:
        null = this.mFeatureProgress;
        break;
      case 1:
        null = this.mWindowNoTitle;
        break;
    } 
    return (null || this.mWindow.hasFeature(paramInt));
  }
  
  public void installViewFactory() {
    LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
    if (layoutInflater.getFactory() == null) {
      LayoutInflaterCompat.setFactory2(layoutInflater, this);
    } else if (!(layoutInflater.getFactory2() instanceof AppCompatDelegateImpl)) {
      Log.i("AppCompatDelegate", "The Activity's LayoutInflater already has a Factory installed so we can not install AppCompat's");
    } 
  }
  
  public void invalidateOptionsMenu() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null && actionBar.invalidateOptionsMenu())
      return; 
    invalidatePanelMenu(0);
  }
  
  public boolean isHandleNativeActionModesEnabled() {
    return this.mHandleNativeActionModes;
  }
  
  int mapNightMode(Context paramContext, int paramInt) {
    switch (paramInt) {
      default:
        throw new IllegalStateException("Unknown value set for night mode. Please use one of the MODE_NIGHT values from AppCompatDelegate.");
      case 3:
        return getAutoBatteryNightModeManager(paramContext).getApplyableNightMode();
      case 0:
        return (Build.VERSION.SDK_INT >= 23 && ((UiModeManager)paramContext.getApplicationContext().getSystemService("uimode")).getNightMode() == 0) ? -1 : getAutoTimeNightModeManager(paramContext).getApplyableNightMode();
      case -1:
      case 1:
      case 2:
        return paramInt;
      case -100:
        break;
    } 
    return -1;
  }
  
  boolean onBackPressed() {
    ActionMode actionMode = this.mActionMode;
    if (actionMode != null) {
      actionMode.finish();
      return true;
    } 
    ActionBar actionBar = getSupportActionBar();
    return (actionBar != null && actionBar.collapseActionView());
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    if (this.mHasActionBar && this.mSubDecorInstalled) {
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null)
        actionBar.onConfigurationChanged(paramConfiguration); 
    } 
    AppCompatDrawableManager.get().onConfigurationChanged(this.mContext);
    this.mEffectiveConfiguration = new Configuration(this.mContext.getResources().getConfiguration());
    applyDayNight(false);
  }
  
  public void onCreate(Bundle paramBundle) {
    this.mBaseContextAttached = true;
    applyDayNight(false);
    ensureWindow();
    Object object = this.mHost;
    if (object instanceof Activity) {
      Object object1;
      paramBundle = null;
      try {
        object = NavUtils.getParentActivityName((Activity)object);
        object1 = object;
      } catch (IllegalArgumentException illegalArgumentException) {}
      if (object1 != null) {
        object1 = peekSupportActionBar();
        if (object1 == null) {
          this.mEnableDefaultActionBarUp = true;
        } else {
          object1.setDefaultDisplayHomeAsUpEnabled(true);
        } 
      } 
      addActiveDelegate(this);
    } 
    this.mEffectiveConfiguration = new Configuration(this.mContext.getResources().getConfiguration());
    this.mCreated = true;
  }
  
  public final View onCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet) {
    return createView(paramView, paramString, paramContext, paramAttributeSet);
  }
  
  public View onCreateView(String paramString, Context paramContext, AttributeSet paramAttributeSet) {
    return onCreateView((View)null, paramString, paramContext, paramAttributeSet);
  }
  
  public void onDestroy() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mHost : Ljava/lang/Object;
    //   4: instanceof android/app/Activity
    //   7: ifeq -> 14
    //   10: aload_0
    //   11: invokestatic removeActivityDelegate : (Landroidx/appcompat/app/AppCompatDelegate;)V
    //   14: aload_0
    //   15: getfield mInvalidatePanelMenuPosted : Z
    //   18: ifeq -> 36
    //   21: aload_0
    //   22: getfield mWindow : Landroid/view/Window;
    //   25: invokevirtual getDecorView : ()Landroid/view/View;
    //   28: aload_0
    //   29: getfield mInvalidatePanelMenuRunnable : Ljava/lang/Runnable;
    //   32: invokevirtual removeCallbacks : (Ljava/lang/Runnable;)Z
    //   35: pop
    //   36: aload_0
    //   37: iconst_1
    //   38: putfield mDestroyed : Z
    //   41: aload_0
    //   42: getfield mLocalNightMode : I
    //   45: bipush #-100
    //   47: if_icmpeq -> 99
    //   50: aload_0
    //   51: getfield mHost : Ljava/lang/Object;
    //   54: astore_1
    //   55: aload_1
    //   56: instanceof android/app/Activity
    //   59: ifeq -> 99
    //   62: aload_1
    //   63: checkcast android/app/Activity
    //   66: invokevirtual isChangingConfigurations : ()Z
    //   69: ifeq -> 99
    //   72: getstatic androidx/appcompat/app/AppCompatDelegateImpl.sLocalNightModes : Landroidx/collection/SimpleArrayMap;
    //   75: aload_0
    //   76: getfield mHost : Ljava/lang/Object;
    //   79: invokevirtual getClass : ()Ljava/lang/Class;
    //   82: invokevirtual getName : ()Ljava/lang/String;
    //   85: aload_0
    //   86: getfield mLocalNightMode : I
    //   89: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   92: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   95: pop
    //   96: goto -> 116
    //   99: getstatic androidx/appcompat/app/AppCompatDelegateImpl.sLocalNightModes : Landroidx/collection/SimpleArrayMap;
    //   102: aload_0
    //   103: getfield mHost : Ljava/lang/Object;
    //   106: invokevirtual getClass : ()Ljava/lang/Class;
    //   109: invokevirtual getName : ()Ljava/lang/String;
    //   112: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   115: pop
    //   116: aload_0
    //   117: getfield mActionBar : Landroidx/appcompat/app/ActionBar;
    //   120: astore_1
    //   121: aload_1
    //   122: ifnull -> 129
    //   125: aload_1
    //   126: invokevirtual onDestroy : ()V
    //   129: aload_0
    //   130: invokespecial cleanupAutoManagers : ()V
    //   133: return
  }
  
  boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    boolean bool = true;
    switch (paramInt) {
      default:
        return false;
      case 82:
        onKeyDownPanel(0, paramKeyEvent);
        return true;
      case 4:
        break;
    } 
    if ((paramKeyEvent.getFlags() & 0x80) == 0)
      bool = false; 
    this.mLongPressBackDown = bool;
  }
  
  boolean onKeyShortcut(int paramInt, KeyEvent paramKeyEvent) {
    PanelFeatureState panelFeatureState1;
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null && actionBar.onKeyShortcut(paramInt, paramKeyEvent))
      return true; 
    PanelFeatureState panelFeatureState2 = this.mPreparedPanel;
    if (panelFeatureState2 != null && performPanelShortcut(panelFeatureState2, paramKeyEvent.getKeyCode(), paramKeyEvent, 1)) {
      panelFeatureState1 = this.mPreparedPanel;
      if (panelFeatureState1 != null)
        panelFeatureState1.isHandled = true; 
      return true;
    } 
    if (this.mPreparedPanel == null) {
      panelFeatureState2 = getPanelState(0, true);
      preparePanel(panelFeatureState2, (KeyEvent)panelFeatureState1);
      boolean bool = performPanelShortcut(panelFeatureState2, panelFeatureState1.getKeyCode(), (KeyEvent)panelFeatureState1, 1);
      panelFeatureState2.isPrepared = false;
      if (bool)
        return true; 
    } 
    return false;
  }
  
  boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
    switch (paramInt) {
      default:
        return false;
      case 82:
        onKeyUpPanel(0, paramKeyEvent);
        return true;
      case 4:
        break;
    } 
    boolean bool = this.mLongPressBackDown;
    this.mLongPressBackDown = false;
    PanelFeatureState panelFeatureState = getPanelState(0, false);
    if (panelFeatureState != null && panelFeatureState.isOpen) {
      if (!bool)
        closePanel(panelFeatureState, true); 
      return true;
    } 
    if (onBackPressed())
      return true; 
  }
  
  public boolean onMenuItemSelected(MenuBuilder paramMenuBuilder, MenuItem paramMenuItem) {
    Window.Callback callback = getWindowCallback();
    if (callback != null && !this.mDestroyed) {
      PanelFeatureState panelFeatureState = findMenuPanel((Menu)paramMenuBuilder.getRootMenu());
      if (panelFeatureState != null)
        return callback.onMenuItemSelected(panelFeatureState.featureId, paramMenuItem); 
    } 
    return false;
  }
  
  public void onMenuModeChange(MenuBuilder paramMenuBuilder) {
    reopenMenu(true);
  }
  
  void onMenuOpened(int paramInt) {
    if (paramInt == 108) {
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null)
        actionBar.dispatchMenuVisibilityChanged(true); 
    } 
  }
  
  void onPanelClosed(int paramInt) {
    if (paramInt == 108) {
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null)
        actionBar.dispatchMenuVisibilityChanged(false); 
    } else if (paramInt == 0) {
      PanelFeatureState panelFeatureState = getPanelState(paramInt, true);
      if (panelFeatureState.isOpen)
        closePanel(panelFeatureState, false); 
    } 
  }
  
  public void onPostCreate(Bundle paramBundle) {
    ensureSubDecor();
  }
  
  public void onPostResume() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null)
      actionBar.setShowHideAnimationEnabled(true); 
  }
  
  public void onSaveInstanceState(Bundle paramBundle) {}
  
  public void onStart() {
    applyDayNight();
  }
  
  public void onStop() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null)
      actionBar.setShowHideAnimationEnabled(false); 
  }
  
  void onSubDecorInstalled(ViewGroup paramViewGroup) {}
  
  final ActionBar peekSupportActionBar() {
    return this.mActionBar;
  }
  
  public boolean requestWindowFeature(int paramInt) {
    paramInt = sanitizeWindowFeatureId(paramInt);
    if (this.mWindowNoTitle && paramInt == 108)
      return false; 
    if (this.mHasActionBar && paramInt == 1)
      this.mHasActionBar = false; 
    switch (paramInt) {
      default:
        return this.mWindow.requestFeature(paramInt);
      case 109:
        throwFeatureRequestIfSubDecorInstalled();
        this.mOverlayActionBar = true;
        return true;
      case 108:
        throwFeatureRequestIfSubDecorInstalled();
        this.mHasActionBar = true;
        return true;
      case 10:
        throwFeatureRequestIfSubDecorInstalled();
        this.mOverlayActionMode = true;
        return true;
      case 5:
        throwFeatureRequestIfSubDecorInstalled();
        this.mFeatureIndeterminateProgress = true;
        return true;
      case 2:
        throwFeatureRequestIfSubDecorInstalled();
        this.mFeatureProgress = true;
        return true;
      case 1:
        break;
    } 
    throwFeatureRequestIfSubDecorInstalled();
    this.mWindowNoTitle = true;
    return true;
  }
  
  public void setContentView(int paramInt) {
    ensureSubDecor();
    ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(16908290);
    viewGroup.removeAllViews();
    LayoutInflater.from(this.mContext).inflate(paramInt, viewGroup);
    this.mAppCompatWindowCallback.getWrapped().onContentChanged();
  }
  
  public void setContentView(View paramView) {
    ensureSubDecor();
    ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(16908290);
    viewGroup.removeAllViews();
    viewGroup.addView(paramView);
    this.mAppCompatWindowCallback.getWrapped().onContentChanged();
  }
  
  public void setContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    ensureSubDecor();
    ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(16908290);
    viewGroup.removeAllViews();
    viewGroup.addView(paramView, paramLayoutParams);
    this.mAppCompatWindowCallback.getWrapped().onContentChanged();
  }
  
  public void setHandleNativeActionModesEnabled(boolean paramBoolean) {
    this.mHandleNativeActionModes = paramBoolean;
  }
  
  public void setLocalNightMode(int paramInt) {
    if (this.mLocalNightMode != paramInt) {
      this.mLocalNightMode = paramInt;
      if (this.mBaseContextAttached)
        applyDayNight(); 
    } 
  }
  
  public void setSupportActionBar(Toolbar paramToolbar) {
    if (!(this.mHost instanceof Activity))
      return; 
    ActionBar actionBar = getSupportActionBar();
    if (!(actionBar instanceof WindowDecorActionBar)) {
      this.mMenuInflater = null;
      if (actionBar != null)
        actionBar.onDestroy(); 
      this.mActionBar = null;
      if (paramToolbar != null) {
        ToolbarActionBar toolbarActionBar = new ToolbarActionBar(paramToolbar, getTitle(), (Window.Callback)this.mAppCompatWindowCallback);
        this.mActionBar = toolbarActionBar;
        this.mAppCompatWindowCallback.setActionBarCallback(toolbarActionBar.mMenuCallback);
      } else {
        this.mAppCompatWindowCallback.setActionBarCallback(null);
      } 
      invalidateOptionsMenu();
      return;
    } 
    throw new IllegalStateException("This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set windowActionBar to false in your theme to use a Toolbar instead.");
  }
  
  public void setTheme(int paramInt) {
    this.mThemeResId = paramInt;
  }
  
  public final void setTitle(CharSequence paramCharSequence) {
    this.mTitle = paramCharSequence;
    DecorContentParent decorContentParent = this.mDecorContentParent;
    if (decorContentParent != null) {
      decorContentParent.setWindowTitle(paramCharSequence);
    } else if (peekSupportActionBar() != null) {
      peekSupportActionBar().setWindowTitle(paramCharSequence);
    } else {
      TextView textView = this.mTitleView;
      if (textView != null)
        textView.setText(paramCharSequence); 
    } 
  }
  
  final boolean shouldAnimateActionModeView() {
    if (this.mSubDecorInstalled) {
      ViewGroup viewGroup = this.mSubDecor;
      if (viewGroup != null && ViewCompat.isLaidOut((View)viewGroup))
        return true; 
    } 
    return false;
  }
  
  public ActionMode startSupportActionMode(ActionMode.Callback paramCallback) {
    if (paramCallback != null) {
      ActionMode actionMode = this.mActionMode;
      if (actionMode != null)
        actionMode.finish(); 
      paramCallback = new ActionModeCallbackWrapperV9(paramCallback);
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null) {
        ActionMode actionMode1 = actionBar.startActionMode(paramCallback);
        this.mActionMode = actionMode1;
        if (actionMode1 != null) {
          AppCompatCallback appCompatCallback = this.mAppCompatCallback;
          if (appCompatCallback != null)
            appCompatCallback.onSupportActionModeStarted(actionMode1); 
        } 
      } 
      if (this.mActionMode == null)
        this.mActionMode = startSupportActionModeFromWindow(paramCallback); 
      return this.mActionMode;
    } 
    throw new IllegalArgumentException("ActionMode callback can not be null.");
  }
  
  ActionMode startSupportActionModeFromWindow(ActionMode.Callback paramCallback) {
    ActionMode.Callback callback1;
    endOnGoingFadeAnimation();
    ActionMode actionMode2 = this.mActionMode;
    if (actionMode2 != null)
      actionMode2.finish(); 
    ActionMode.Callback callback2 = paramCallback;
    if (!(paramCallback instanceof ActionModeCallbackWrapperV9))
      callback2 = new ActionModeCallbackWrapperV9(paramCallback); 
    ActionMode.Callback callback3 = null;
    AppCompatCallback appCompatCallback = this.mAppCompatCallback;
    paramCallback = callback3;
    if (appCompatCallback != null) {
      paramCallback = callback3;
      if (!this.mDestroyed)
        try {
          ActionMode actionMode = appCompatCallback.onWindowStartingSupportActionMode(callback2);
        } catch (AbstractMethodError abstractMethodError) {
          callback1 = callback3;
        }  
    } 
    if (callback1 != null) {
      this.mActionMode = (ActionMode)callback1;
    } else {
      ActionBarContextView actionBarContextView = this.mActionModeView;
      boolean bool = true;
      if (actionBarContextView == null)
        if (this.mIsFloating) {
          Context context;
          TypedValue typedValue = new TypedValue();
          Resources.Theme theme = this.mContext.getTheme();
          theme.resolveAttribute(R.attr.actionBarTheme, typedValue, true);
          if (typedValue.resourceId != 0) {
            Resources.Theme theme1 = this.mContext.getResources().newTheme();
            theme1.setTo(theme);
            theme1.applyStyle(typedValue.resourceId, true);
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(this.mContext, 0);
            contextThemeWrapper.getTheme().setTo(theme1);
          } else {
            context = this.mContext;
          } 
          this.mActionModeView = new ActionBarContextView(context);
          PopupWindow popupWindow = new PopupWindow(context, null, R.attr.actionModePopupWindowStyle);
          this.mActionModePopup = popupWindow;
          PopupWindowCompat.setWindowLayoutType(popupWindow, 2);
          this.mActionModePopup.setContentView((View)this.mActionModeView);
          this.mActionModePopup.setWidth(-1);
          context.getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true);
          int i = TypedValue.complexToDimensionPixelSize(typedValue.data, context.getResources().getDisplayMetrics());
          this.mActionModeView.setContentHeight(i);
          this.mActionModePopup.setHeight(-2);
          this.mShowActionModePopup = new Runnable() {
              final AppCompatDelegateImpl this$0;
              
              public void run() {
                AppCompatDelegateImpl.this.mActionModePopup.showAtLocation((View)AppCompatDelegateImpl.this.mActionModeView, 55, 0, 0);
                AppCompatDelegateImpl.this.endOnGoingFadeAnimation();
                if (AppCompatDelegateImpl.this.shouldAnimateActionModeView()) {
                  AppCompatDelegateImpl.this.mActionModeView.setAlpha(0.0F);
                  AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
                  appCompatDelegateImpl.mFadeAnim = ViewCompat.animate((View)appCompatDelegateImpl.mActionModeView).alpha(1.0F);
                  AppCompatDelegateImpl.this.mFadeAnim.setListener((ViewPropertyAnimatorListener)new ViewPropertyAnimatorListenerAdapter() {
                        final AppCompatDelegateImpl.null this$1;
                        
                        public void onAnimationEnd(View param2View) {
                          AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0F);
                          AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
                          AppCompatDelegateImpl.this.mFadeAnim = null;
                        }
                        
                        public void onAnimationStart(View param2View) {
                          AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
                        }
                      });
                } else {
                  AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0F);
                  AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
                } 
              }
            };
        } else {
          ViewStubCompat viewStubCompat = (ViewStubCompat)this.mSubDecor.findViewById(R.id.action_mode_bar_stub);
          if (viewStubCompat != null) {
            viewStubCompat.setLayoutInflater(LayoutInflater.from(getActionBarThemedContext()));
            this.mActionModeView = (ActionBarContextView)viewStubCompat.inflate();
          } 
        }  
      if (this.mActionModeView != null) {
        endOnGoingFadeAnimation();
        this.mActionModeView.killMode();
        Context context = this.mActionModeView.getContext();
        actionBarContextView = this.mActionModeView;
        if (this.mActionModePopup != null)
          bool = false; 
        StandaloneActionMode standaloneActionMode = new StandaloneActionMode(context, actionBarContextView, callback2, bool);
        if (callback2.onCreateActionMode((ActionMode)standaloneActionMode, standaloneActionMode.getMenu())) {
          standaloneActionMode.invalidate();
          this.mActionModeView.initForMode((ActionMode)standaloneActionMode);
          this.mActionMode = (ActionMode)standaloneActionMode;
          if (shouldAnimateActionModeView()) {
            this.mActionModeView.setAlpha(0.0F);
            ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = ViewCompat.animate((View)this.mActionModeView).alpha(1.0F);
            this.mFadeAnim = viewPropertyAnimatorCompat;
            viewPropertyAnimatorCompat.setListener((ViewPropertyAnimatorListener)new ViewPropertyAnimatorListenerAdapter() {
                  final AppCompatDelegateImpl this$0;
                  
                  public void onAnimationEnd(View param1View) {
                    AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0F);
                    AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
                    AppCompatDelegateImpl.this.mFadeAnim = null;
                  }
                  
                  public void onAnimationStart(View param1View) {
                    AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
                    if (AppCompatDelegateImpl.this.mActionModeView.getParent() instanceof View)
                      ViewCompat.requestApplyInsets((View)AppCompatDelegateImpl.this.mActionModeView.getParent()); 
                  }
                });
          } else {
            this.mActionModeView.setAlpha(1.0F);
            this.mActionModeView.setVisibility(0);
            if (this.mActionModeView.getParent() instanceof View)
              ViewCompat.requestApplyInsets((View)this.mActionModeView.getParent()); 
          } 
          if (this.mActionModePopup != null)
            this.mWindow.getDecorView().post(this.mShowActionModePopup); 
        } else {
          this.mActionMode = null;
        } 
      } 
    } 
    ActionMode actionMode1 = this.mActionMode;
    if (actionMode1 != null) {
      AppCompatCallback appCompatCallback1 = this.mAppCompatCallback;
      if (appCompatCallback1 != null)
        appCompatCallback1.onSupportActionModeStarted(actionMode1); 
    } 
    return this.mActionMode;
  }
  
  final int updateStatusGuard(WindowInsetsCompat paramWindowInsetsCompat, Rect paramRect) {
    int k;
    int i = 0;
    if (paramWindowInsetsCompat != null) {
      i = paramWindowInsetsCompat.getSystemWindowInsetTop();
    } else if (paramRect != null) {
      i = paramRect.top;
    } 
    int m = 0;
    int j = 0;
    ActionBarContextView actionBarContextView = this.mActionModeView;
    if (actionBarContextView != null && actionBarContextView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
      ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)this.mActionModeView.getLayoutParams();
      k = 0;
      int n = 0;
      if (this.mActionModeView.isShown()) {
        if (this.mTempRect1 == null) {
          this.mTempRect1 = new Rect();
          this.mTempRect2 = new Rect();
        } 
        Rect rect2 = this.mTempRect1;
        Rect rect1 = this.mTempRect2;
        if (paramWindowInsetsCompat == null) {
          rect2.set(paramRect);
        } else {
          rect2.set(paramWindowInsetsCompat.getSystemWindowInsetLeft(), paramWindowInsetsCompat.getSystemWindowInsetTop(), paramWindowInsetsCompat.getSystemWindowInsetRight(), paramWindowInsetsCompat.getSystemWindowInsetBottom());
        } 
        ViewUtils.computeFitSystemWindows((View)this.mSubDecor, rect2, rect1);
        m = rect2.top;
        int i2 = rect2.left;
        int i1 = rect2.right;
        paramWindowInsetsCompat = ViewCompat.getRootWindowInsets((View)this.mSubDecor);
        if (paramWindowInsetsCompat == null) {
          j = 0;
        } else {
          j = paramWindowInsetsCompat.getSystemWindowInsetLeft();
        } 
        if (paramWindowInsetsCompat == null) {
          k = 0;
        } else {
          k = paramWindowInsetsCompat.getSystemWindowInsetRight();
        } 
        if (marginLayoutParams.topMargin != m || marginLayoutParams.leftMargin != i2 || marginLayoutParams.rightMargin != i1) {
          marginLayoutParams.topMargin = m;
          marginLayoutParams.leftMargin = i2;
          marginLayoutParams.rightMargin = i1;
          n = 1;
        } 
        if (m > 0 && this.mStatusGuard == null) {
          View view2 = new View(this.mContext);
          this.mStatusGuard = view2;
          view2.setVisibility(8);
          FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, marginLayoutParams.topMargin, 51);
          layoutParams.leftMargin = j;
          layoutParams.rightMargin = k;
          this.mSubDecor.addView(this.mStatusGuard, -1, (ViewGroup.LayoutParams)layoutParams);
        } else {
          View view2 = this.mStatusGuard;
          if (view2 != null) {
            ViewGroup.MarginLayoutParams marginLayoutParams1 = (ViewGroup.MarginLayoutParams)view2.getLayoutParams();
            if (marginLayoutParams1.height != marginLayoutParams.topMargin || marginLayoutParams1.leftMargin != j || marginLayoutParams1.rightMargin != k) {
              marginLayoutParams1.height = marginLayoutParams.topMargin;
              marginLayoutParams1.leftMargin = j;
              marginLayoutParams1.rightMargin = k;
              this.mStatusGuard.setLayoutParams((ViewGroup.LayoutParams)marginLayoutParams1);
            } 
          } 
        } 
        View view1 = this.mStatusGuard;
        if (view1 != null) {
          j = 1;
        } else {
          j = 0;
        } 
        if (j != 0 && view1.getVisibility() != 0)
          updateStatusGuardColor(this.mStatusGuard); 
        if (!this.mOverlayActionMode && j != 0)
          i = 0; 
      } else if (marginLayoutParams.topMargin != 0) {
        n = 1;
        marginLayoutParams.topMargin = 0;
      } else {
        n = k;
      } 
      k = i;
      m = j;
      if (n != 0) {
        this.mActionModeView.setLayoutParams((ViewGroup.LayoutParams)marginLayoutParams);
        k = i;
        m = j;
      } 
    } else {
      k = i;
    } 
    View view = this.mStatusGuard;
    if (view != null) {
      if (m != 0) {
        i = 0;
      } else {
        i = 8;
      } 
      view.setVisibility(i);
    } 
    return k;
  }
  
  static {
    boolean bool1;
  }
  
  static final String EXCEPTION_HANDLER_MESSAGE_SUFFIX = ". If the resource you are trying to use is a vector resource, you may be referencing it in an unsupported way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.";
  
  private static final boolean IS_PRE_LOLLIPOP;
  
  private static final boolean sCanApplyOverrideConfiguration;
  
  private static final boolean sCanReturnDifferentContext;
  
  private static boolean sInstalledExceptionHandler;
  
  private static final SimpleArrayMap<String, Integer> sLocalNightModes = new SimpleArrayMap();
  
  private static final int[] sWindowBackgroundStyleable;
  
  ActionBar mActionBar;
  
  private ActionMenuPresenterCallback mActionMenuPresenterCallback;
  
  ActionMode mActionMode;
  
  PopupWindow mActionModePopup;
  
  ActionBarContextView mActionModeView;
  
  private boolean mActivityHandlesUiMode;
  
  private boolean mActivityHandlesUiModeChecked;
  
  final AppCompatCallback mAppCompatCallback;
  
  private AppCompatViewInflater mAppCompatViewInflater;
  
  private AppCompatWindowCallback mAppCompatWindowCallback;
  
  private AutoNightModeManager mAutoBatteryNightModeManager;
  
  private AutoNightModeManager mAutoTimeNightModeManager;
  
  private boolean mBaseContextAttached;
  
  private boolean mClosingActionMenu;
  
  final Context mContext;
  
  private boolean mCreated;
  
  private DecorContentParent mDecorContentParent;
  
  boolean mDestroyed;
  
  private Configuration mEffectiveConfiguration;
  
  private boolean mEnableDefaultActionBarUp;
  
  ViewPropertyAnimatorCompat mFadeAnim = null;
  
  private boolean mFeatureIndeterminateProgress;
  
  private boolean mFeatureProgress;
  
  private boolean mHandleNativeActionModes = true;
  
  boolean mHasActionBar;
  
  final Object mHost;
  
  int mInvalidatePanelMenuFeatures;
  
  boolean mInvalidatePanelMenuPosted;
  
  private final Runnable mInvalidatePanelMenuRunnable = new Runnable() {
      final AppCompatDelegateImpl this$0;
      
      public void run() {
        if ((AppCompatDelegateImpl.this.mInvalidatePanelMenuFeatures & 0x1) != 0)
          AppCompatDelegateImpl.this.doInvalidatePanelMenu(0); 
        if ((AppCompatDelegateImpl.this.mInvalidatePanelMenuFeatures & 0x1000) != 0)
          AppCompatDelegateImpl.this.doInvalidatePanelMenu(108); 
        AppCompatDelegateImpl.this.mInvalidatePanelMenuPosted = false;
        AppCompatDelegateImpl.this.mInvalidatePanelMenuFeatures = 0;
      }
    };
  
  boolean mIsFloating;
  
  private LayoutIncludeDetector mLayoutIncludeDetector;
  
  private int mLocalNightMode = -100;
  
  private boolean mLongPressBackDown;
  
  MenuInflater mMenuInflater;
  
  boolean mOverlayActionBar;
  
  boolean mOverlayActionMode;
  
  private PanelMenuPresenterCallback mPanelMenuPresenterCallback;
  
  private PanelFeatureState[] mPanels;
  
  private PanelFeatureState mPreparedPanel;
  
  Runnable mShowActionModePopup;
  
  private View mStatusGuard;
  
  ViewGroup mSubDecor;
  
  private boolean mSubDecorInstalled;
  
  private Rect mTempRect1;
  
  private Rect mTempRect2;
  
  private int mThemeResId;
  
  private CharSequence mTitle;
  
  private TextView mTitleView;
  
  Window mWindow;
  
  boolean mWindowNoTitle;
  
  private class ActionBarDrawableToggleImpl implements ActionBarDrawerToggle.Delegate {
    final AppCompatDelegateImpl this$0;
    
    public Context getActionBarThemedContext() {
      return AppCompatDelegateImpl.this.getActionBarThemedContext();
    }
    
    public Drawable getThemeUpIndicator() {
      TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(getActionBarThemedContext(), null, new int[] { R.attr.homeAsUpIndicator });
      Drawable drawable = tintTypedArray.getDrawable(0);
      tintTypedArray.recycle();
      return drawable;
    }
    
    public boolean isNavigationVisible() {
      boolean bool;
      ActionBar actionBar = AppCompatDelegateImpl.this.getSupportActionBar();
      if (actionBar != null && (actionBar.getDisplayOptions() & 0x4) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void setActionBarDescription(int param1Int) {
      ActionBar actionBar = AppCompatDelegateImpl.this.getSupportActionBar();
      if (actionBar != null)
        actionBar.setHomeActionContentDescription(param1Int); 
    }
    
    public void setActionBarUpIndicator(Drawable param1Drawable, int param1Int) {
      ActionBar actionBar = AppCompatDelegateImpl.this.getSupportActionBar();
      if (actionBar != null) {
        actionBar.setHomeAsUpIndicator(param1Drawable);
        actionBar.setHomeActionContentDescription(param1Int);
      } 
    }
  }
  
  static interface ActionBarMenuCallback {
    View onCreatePanelView(int param1Int);
    
    boolean onPreparePanel(int param1Int);
  }
  
  private final class ActionMenuPresenterCallback implements MenuPresenter.Callback {
    final AppCompatDelegateImpl this$0;
    
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) {
      AppCompatDelegateImpl.this.checkCloseActionMenu(param1MenuBuilder);
    }
    
    public boolean onOpenSubMenu(MenuBuilder param1MenuBuilder) {
      Window.Callback callback = AppCompatDelegateImpl.this.getWindowCallback();
      if (callback != null)
        callback.onMenuOpened(108, (Menu)param1MenuBuilder); 
      return true;
    }
  }
  
  class ActionModeCallbackWrapperV9 implements ActionMode.Callback {
    private ActionMode.Callback mWrapped;
    
    final AppCompatDelegateImpl this$0;
    
    public ActionModeCallbackWrapperV9(ActionMode.Callback param1Callback) {
      this.mWrapped = param1Callback;
    }
    
    public boolean onActionItemClicked(ActionMode param1ActionMode, MenuItem param1MenuItem) {
      return this.mWrapped.onActionItemClicked(param1ActionMode, param1MenuItem);
    }
    
    public boolean onCreateActionMode(ActionMode param1ActionMode, Menu param1Menu) {
      return this.mWrapped.onCreateActionMode(param1ActionMode, param1Menu);
    }
    
    public void onDestroyActionMode(ActionMode param1ActionMode) {
      this.mWrapped.onDestroyActionMode(param1ActionMode);
      if (AppCompatDelegateImpl.this.mActionModePopup != null)
        AppCompatDelegateImpl.this.mWindow.getDecorView().removeCallbacks(AppCompatDelegateImpl.this.mShowActionModePopup); 
      if (AppCompatDelegateImpl.this.mActionModeView != null) {
        AppCompatDelegateImpl.this.endOnGoingFadeAnimation();
        AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
        appCompatDelegateImpl.mFadeAnim = ViewCompat.animate((View)appCompatDelegateImpl.mActionModeView).alpha(0.0F);
        AppCompatDelegateImpl.this.mFadeAnim.setListener((ViewPropertyAnimatorListener)new ViewPropertyAnimatorListenerAdapter() {
              final AppCompatDelegateImpl.ActionModeCallbackWrapperV9 this$1;
              
              public void onAnimationEnd(View param2View) {
                AppCompatDelegateImpl.this.mActionModeView.setVisibility(8);
                if (AppCompatDelegateImpl.this.mActionModePopup != null) {
                  AppCompatDelegateImpl.this.mActionModePopup.dismiss();
                } else if (AppCompatDelegateImpl.this.mActionModeView.getParent() instanceof View) {
                  ViewCompat.requestApplyInsets((View)AppCompatDelegateImpl.this.mActionModeView.getParent());
                } 
                AppCompatDelegateImpl.this.mActionModeView.killMode();
                AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
                AppCompatDelegateImpl.this.mFadeAnim = null;
                ViewCompat.requestApplyInsets((View)AppCompatDelegateImpl.this.mSubDecor);
              }
            });
      } 
      if (AppCompatDelegateImpl.this.mAppCompatCallback != null)
        AppCompatDelegateImpl.this.mAppCompatCallback.onSupportActionModeFinished(AppCompatDelegateImpl.this.mActionMode); 
      AppCompatDelegateImpl.this.mActionMode = null;
      ViewCompat.requestApplyInsets((View)AppCompatDelegateImpl.this.mSubDecor);
    }
    
    public boolean onPrepareActionMode(ActionMode param1ActionMode, Menu param1Menu) {
      ViewCompat.requestApplyInsets((View)AppCompatDelegateImpl.this.mSubDecor);
      return this.mWrapped.onPrepareActionMode(param1ActionMode, param1Menu);
    }
  }
  
  class null extends ViewPropertyAnimatorListenerAdapter {
    final AppCompatDelegateImpl.ActionModeCallbackWrapperV9 this$1;
    
    public void onAnimationEnd(View param1View) {
      AppCompatDelegateImpl.this.mActionModeView.setVisibility(8);
      if (AppCompatDelegateImpl.this.mActionModePopup != null) {
        AppCompatDelegateImpl.this.mActionModePopup.dismiss();
      } else if (AppCompatDelegateImpl.this.mActionModeView.getParent() instanceof View) {
        ViewCompat.requestApplyInsets((View)AppCompatDelegateImpl.this.mActionModeView.getParent());
      } 
      AppCompatDelegateImpl.this.mActionModeView.killMode();
      AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
      AppCompatDelegateImpl.this.mFadeAnim = null;
      ViewCompat.requestApplyInsets((View)AppCompatDelegateImpl.this.mSubDecor);
    }
  }
  
  static class Api17Impl {
    static Context createConfigurationContext(Context param1Context, Configuration param1Configuration) {
      return param1Context.createConfigurationContext(param1Configuration);
    }
    
    static void generateConfigDelta_densityDpi(Configuration param1Configuration1, Configuration param1Configuration2, Configuration param1Configuration3) {
      if (param1Configuration1.densityDpi != param1Configuration2.densityDpi)
        param1Configuration3.densityDpi = param1Configuration2.densityDpi; 
    }
  }
  
  static class Api21Impl {
    static boolean isPowerSaveMode(PowerManager param1PowerManager) {
      return param1PowerManager.isPowerSaveMode();
    }
  }
  
  static class Api24Impl {
    static void generateConfigDelta_locale(Configuration param1Configuration1, Configuration param1Configuration2, Configuration param1Configuration3) {
      LocaleList localeList2 = param1Configuration1.getLocales();
      LocaleList localeList1 = param1Configuration2.getLocales();
      if (!localeList2.equals(localeList1)) {
        param1Configuration3.setLocales(localeList1);
        param1Configuration3.locale = param1Configuration2.locale;
      } 
    }
  }
  
  static class Api26Impl {
    static void generateConfigDelta_colorMode(Configuration param1Configuration1, Configuration param1Configuration2, Configuration param1Configuration3) {
      if ((param1Configuration1.colorMode & 0x3) != (param1Configuration2.colorMode & 0x3))
        param1Configuration3.colorMode |= param1Configuration2.colorMode & 0x3; 
      if ((param1Configuration1.colorMode & 0xC) != (param1Configuration2.colorMode & 0xC))
        param1Configuration3.colorMode |= param1Configuration2.colorMode & 0xC; 
    }
  }
  
  class AppCompatWindowCallback extends WindowCallbackWrapper {
    private AppCompatDelegateImpl.ActionBarMenuCallback mActionBarCallback;
    
    final AppCompatDelegateImpl this$0;
    
    AppCompatWindowCallback(Window.Callback param1Callback) {
      super(param1Callback);
    }
    
    public boolean dispatchKeyEvent(KeyEvent param1KeyEvent) {
      return (AppCompatDelegateImpl.this.dispatchKeyEvent(param1KeyEvent) || super.dispatchKeyEvent(param1KeyEvent));
    }
    
    public boolean dispatchKeyShortcutEvent(KeyEvent param1KeyEvent) {
      return (super.dispatchKeyShortcutEvent(param1KeyEvent) || AppCompatDelegateImpl.this.onKeyShortcut(param1KeyEvent.getKeyCode(), param1KeyEvent));
    }
    
    public void onContentChanged() {}
    
    public boolean onCreatePanelMenu(int param1Int, Menu param1Menu) {
      return (param1Int == 0 && !(param1Menu instanceof MenuBuilder)) ? false : super.onCreatePanelMenu(param1Int, param1Menu);
    }
    
    public View onCreatePanelView(int param1Int) {
      AppCompatDelegateImpl.ActionBarMenuCallback actionBarMenuCallback = this.mActionBarCallback;
      if (actionBarMenuCallback != null) {
        View view = actionBarMenuCallback.onCreatePanelView(param1Int);
        if (view != null)
          return view; 
      } 
      return super.onCreatePanelView(param1Int);
    }
    
    public boolean onMenuOpened(int param1Int, Menu param1Menu) {
      super.onMenuOpened(param1Int, param1Menu);
      AppCompatDelegateImpl.this.onMenuOpened(param1Int);
      return true;
    }
    
    public void onPanelClosed(int param1Int, Menu param1Menu) {
      super.onPanelClosed(param1Int, param1Menu);
      AppCompatDelegateImpl.this.onPanelClosed(param1Int);
    }
    
    public boolean onPreparePanel(int param1Int, View param1View, Menu param1Menu) {
      MenuBuilder menuBuilder;
      if (param1Menu instanceof MenuBuilder) {
        menuBuilder = (MenuBuilder)param1Menu;
      } else {
        menuBuilder = null;
      } 
      if (param1Int == 0 && menuBuilder == null)
        return false; 
      if (menuBuilder != null)
        menuBuilder.setOverrideVisibleItems(true); 
      boolean bool2 = false;
      AppCompatDelegateImpl.ActionBarMenuCallback actionBarMenuCallback = this.mActionBarCallback;
      boolean bool1 = bool2;
      if (actionBarMenuCallback != null) {
        bool1 = bool2;
        if (actionBarMenuCallback.onPreparePanel(param1Int))
          bool1 = true; 
      } 
      bool2 = bool1;
      if (!bool1)
        bool2 = super.onPreparePanel(param1Int, param1View, param1Menu); 
      if (menuBuilder != null)
        menuBuilder.setOverrideVisibleItems(false); 
      return bool2;
    }
    
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> param1List, Menu param1Menu, int param1Int) {
      AppCompatDelegateImpl.PanelFeatureState panelFeatureState = AppCompatDelegateImpl.this.getPanelState(0, true);
      if (panelFeatureState != null && panelFeatureState.menu != null) {
        super.onProvideKeyboardShortcuts(param1List, (Menu)panelFeatureState.menu, param1Int);
      } else {
        super.onProvideKeyboardShortcuts(param1List, param1Menu, param1Int);
      } 
    }
    
    public ActionMode onWindowStartingActionMode(ActionMode.Callback param1Callback) {
      return (Build.VERSION.SDK_INT >= 23) ? null : (AppCompatDelegateImpl.this.isHandleNativeActionModesEnabled() ? startAsSupportActionMode(param1Callback) : super.onWindowStartingActionMode(param1Callback));
    }
    
    public ActionMode onWindowStartingActionMode(ActionMode.Callback param1Callback, int param1Int) {
      if (AppCompatDelegateImpl.this.isHandleNativeActionModesEnabled()) {
        switch (param1Int) {
          default:
            return super.onWindowStartingActionMode(param1Callback, param1Int);
          case 0:
            break;
        } 
        return startAsSupportActionMode(param1Callback);
      } 
    }
    
    void setActionBarCallback(AppCompatDelegateImpl.ActionBarMenuCallback param1ActionBarMenuCallback) {
      this.mActionBarCallback = param1ActionBarMenuCallback;
    }
    
    final ActionMode startAsSupportActionMode(ActionMode.Callback param1Callback) {
      SupportActionModeWrapper.CallbackWrapper callbackWrapper = new SupportActionModeWrapper.CallbackWrapper(AppCompatDelegateImpl.this.mContext, param1Callback);
      ActionMode actionMode = AppCompatDelegateImpl.this.startSupportActionMode((ActionMode.Callback)callbackWrapper);
      return (actionMode != null) ? callbackWrapper.getActionModeWrapper(actionMode) : null;
    }
  }
  
  private class AutoBatteryNightModeManager extends AutoNightModeManager {
    private final PowerManager mPowerManager;
    
    final AppCompatDelegateImpl this$0;
    
    AutoBatteryNightModeManager(Context param1Context) {
      this.mPowerManager = (PowerManager)param1Context.getApplicationContext().getSystemService("power");
    }
    
    IntentFilter createIntentFilterForBroadcastReceiver() {
      if (Build.VERSION.SDK_INT >= 21) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.os.action.POWER_SAVE_MODE_CHANGED");
        return intentFilter;
      } 
      return null;
    }
    
    public int getApplyableNightMode() {
      int i = Build.VERSION.SDK_INT;
      byte b = 1;
      if (i >= 21) {
        if (AppCompatDelegateImpl.Api21Impl.isPowerSaveMode(this.mPowerManager))
          b = 2; 
        return b;
      } 
      return 1;
    }
    
    public void onChange() {
      AppCompatDelegateImpl.this.applyDayNight();
    }
  }
  
  abstract class AutoNightModeManager {
    private BroadcastReceiver mReceiver;
    
    final AppCompatDelegateImpl this$0;
    
    void cleanup() {
      if (this.mReceiver != null) {
        try {
          AppCompatDelegateImpl.this.mContext.unregisterReceiver(this.mReceiver);
        } catch (IllegalArgumentException illegalArgumentException) {}
        this.mReceiver = null;
      } 
    }
    
    abstract IntentFilter createIntentFilterForBroadcastReceiver();
    
    abstract int getApplyableNightMode();
    
    boolean isListening() {
      boolean bool;
      if (this.mReceiver != null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    abstract void onChange();
    
    void setup() {
      cleanup();
      IntentFilter intentFilter = createIntentFilterForBroadcastReceiver();
      if (intentFilter == null || intentFilter.countActions() == 0)
        return; 
      if (this.mReceiver == null)
        this.mReceiver = new BroadcastReceiver() {
            final AppCompatDelegateImpl.AutoNightModeManager this$1;
            
            public void onReceive(Context param2Context, Intent param2Intent) {
              AppCompatDelegateImpl.AutoNightModeManager.this.onChange();
            }
          }; 
      AppCompatDelegateImpl.this.mContext.registerReceiver(this.mReceiver, intentFilter);
    }
  }
  
  class null extends BroadcastReceiver {
    final AppCompatDelegateImpl.AutoNightModeManager this$1;
    
    public void onReceive(Context param1Context, Intent param1Intent) {
      this.this$1.onChange();
    }
  }
  
  private class AutoTimeNightModeManager extends AutoNightModeManager {
    private final TwilightManager mTwilightManager;
    
    final AppCompatDelegateImpl this$0;
    
    AutoTimeNightModeManager(TwilightManager param1TwilightManager) {
      this.mTwilightManager = param1TwilightManager;
    }
    
    IntentFilter createIntentFilterForBroadcastReceiver() {
      IntentFilter intentFilter = new IntentFilter();
      intentFilter.addAction("android.intent.action.TIME_SET");
      intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
      intentFilter.addAction("android.intent.action.TIME_TICK");
      return intentFilter;
    }
    
    public int getApplyableNightMode() {
      boolean bool;
      if (this.mTwilightManager.isNight()) {
        bool = true;
      } else {
        bool = true;
      } 
      return bool;
    }
    
    public void onChange() {
      AppCompatDelegateImpl.this.applyDayNight();
    }
  }
  
  private static class ContextThemeWrapperCompatApi17Impl {
    static void applyOverrideConfiguration(ContextThemeWrapper param1ContextThemeWrapper, Configuration param1Configuration) {
      param1ContextThemeWrapper.applyOverrideConfiguration(param1Configuration);
    }
  }
  
  private class ListMenuDecorView extends ContentFrameLayout {
    final AppCompatDelegateImpl this$0;
    
    public ListMenuDecorView(Context param1Context) {
      super(param1Context);
    }
    
    private boolean isOutOfBounds(int param1Int1, int param1Int2) {
      return (param1Int1 < -5 || param1Int2 < -5 || param1Int1 > getWidth() + 5 || param1Int2 > getHeight() + 5);
    }
    
    public boolean dispatchKeyEvent(KeyEvent param1KeyEvent) {
      return (AppCompatDelegateImpl.this.dispatchKeyEvent(param1KeyEvent) || super.dispatchKeyEvent(param1KeyEvent));
    }
    
    public boolean onInterceptTouchEvent(MotionEvent param1MotionEvent) {
      if (param1MotionEvent.getAction() == 0 && isOutOfBounds((int)param1MotionEvent.getX(), (int)param1MotionEvent.getY())) {
        AppCompatDelegateImpl.this.closePanel(0);
        return true;
      } 
      return super.onInterceptTouchEvent(param1MotionEvent);
    }
    
    public void setBackgroundResource(int param1Int) {
      setBackgroundDrawable(AppCompatResources.getDrawable(getContext(), param1Int));
    }
  }
  
  protected static final class PanelFeatureState {
    int background;
    
    View createdPanelView;
    
    ViewGroup decorView;
    
    int featureId;
    
    Bundle frozenActionViewState;
    
    Bundle frozenMenuState;
    
    int gravity;
    
    boolean isHandled;
    
    boolean isOpen;
    
    boolean isPrepared;
    
    ListMenuPresenter listMenuPresenter;
    
    Context listPresenterContext;
    
    MenuBuilder menu;
    
    public boolean qwertyMode;
    
    boolean refreshDecorView;
    
    boolean refreshMenuContent;
    
    View shownPanelView;
    
    boolean wasLastOpen;
    
    int windowAnimations;
    
    int x;
    
    int y;
    
    PanelFeatureState(int param1Int) {
      this.featureId = param1Int;
      this.refreshDecorView = false;
    }
    
    void applyFrozenState() {
      MenuBuilder menuBuilder = this.menu;
      if (menuBuilder != null) {
        Bundle bundle = this.frozenMenuState;
        if (bundle != null) {
          menuBuilder.restorePresenterStates(bundle);
          this.frozenMenuState = null;
        } 
      } 
    }
    
    public void clearMenuPresenters() {
      MenuBuilder menuBuilder = this.menu;
      if (menuBuilder != null)
        menuBuilder.removeMenuPresenter((MenuPresenter)this.listMenuPresenter); 
      this.listMenuPresenter = null;
    }
    
    MenuView getListMenuView(MenuPresenter.Callback param1Callback) {
      if (this.menu == null)
        return null; 
      if (this.listMenuPresenter == null) {
        ListMenuPresenter listMenuPresenter = new ListMenuPresenter(this.listPresenterContext, R.layout.abc_list_menu_item_layout);
        this.listMenuPresenter = listMenuPresenter;
        listMenuPresenter.setCallback(param1Callback);
        this.menu.addMenuPresenter((MenuPresenter)this.listMenuPresenter);
      } 
      return this.listMenuPresenter.getMenuView(this.decorView);
    }
    
    public boolean hasPanelItems() {
      View view = this.shownPanelView;
      boolean bool = false;
      if (view == null)
        return false; 
      if (this.createdPanelView != null)
        return true; 
      if (this.listMenuPresenter.getAdapter().getCount() > 0)
        bool = true; 
      return bool;
    }
    
    void onRestoreInstanceState(Parcelable param1Parcelable) {
      param1Parcelable = param1Parcelable;
      this.featureId = ((SavedState)param1Parcelable).featureId;
      this.wasLastOpen = ((SavedState)param1Parcelable).isOpen;
      this.frozenMenuState = ((SavedState)param1Parcelable).menuState;
      this.shownPanelView = null;
      this.decorView = null;
    }
    
    Parcelable onSaveInstanceState() {
      SavedState savedState = new SavedState();
      savedState.featureId = this.featureId;
      savedState.isOpen = this.isOpen;
      if (this.menu != null) {
        savedState.menuState = new Bundle();
        this.menu.savePresenterStates(savedState.menuState);
      } 
      return savedState;
    }
    
    void setMenu(MenuBuilder param1MenuBuilder) {
      MenuBuilder menuBuilder = this.menu;
      if (param1MenuBuilder == menuBuilder)
        return; 
      if (menuBuilder != null)
        menuBuilder.removeMenuPresenter((MenuPresenter)this.listMenuPresenter); 
      this.menu = param1MenuBuilder;
      if (param1MenuBuilder != null) {
        ListMenuPresenter listMenuPresenter = this.listMenuPresenter;
        if (listMenuPresenter != null)
          param1MenuBuilder.addMenuPresenter((MenuPresenter)listMenuPresenter); 
      } 
    }
    
    void setStyle(Context param1Context) {
      TypedValue typedValue = new TypedValue();
      Resources.Theme theme = param1Context.getResources().newTheme();
      theme.setTo(param1Context.getTheme());
      theme.resolveAttribute(R.attr.actionBarPopupTheme, typedValue, true);
      if (typedValue.resourceId != 0)
        theme.applyStyle(typedValue.resourceId, true); 
      theme.resolveAttribute(R.attr.panelMenuListTheme, typedValue, true);
      if (typedValue.resourceId != 0) {
        theme.applyStyle(typedValue.resourceId, true);
      } else {
        theme.applyStyle(R.style.Theme_AppCompat_CompactMenu, true);
      } 
      ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(param1Context, 0);
      contextThemeWrapper.getTheme().setTo(theme);
      this.listPresenterContext = (Context)contextThemeWrapper;
      TypedArray typedArray = contextThemeWrapper.obtainStyledAttributes(R.styleable.AppCompatTheme);
      this.background = typedArray.getResourceId(R.styleable.AppCompatTheme_panelBackground, 0);
      this.windowAnimations = typedArray.getResourceId(R.styleable.AppCompatTheme_android_windowAnimationStyle, 0);
      typedArray.recycle();
    }
    
    private static class SavedState implements Parcelable {
      public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
          public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel param3Parcel) {
            return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(param3Parcel, null);
          }
          
          public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel param3Parcel, ClassLoader param3ClassLoader) {
            return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(param3Parcel, param3ClassLoader);
          }
          
          public AppCompatDelegateImpl.PanelFeatureState.SavedState[] newArray(int param3Int) {
            return new AppCompatDelegateImpl.PanelFeatureState.SavedState[param3Int];
          }
        };
      
      int featureId;
      
      boolean isOpen;
      
      Bundle menuState;
      
      static SavedState readFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
        SavedState savedState = new SavedState();
        savedState.featureId = param2Parcel.readInt();
        int i = param2Parcel.readInt();
        boolean bool = true;
        if (i != 1)
          bool = false; 
        savedState.isOpen = bool;
        if (bool)
          savedState.menuState = param2Parcel.readBundle(param2ClassLoader); 
        return savedState;
      }
      
      public int describeContents() {
        return 0;
      }
      
      public void writeToParcel(Parcel param2Parcel, int param2Int) {
        param2Parcel.writeInt(this.featureId);
        param2Parcel.writeInt(this.isOpen);
        if (this.isOpen)
          param2Parcel.writeBundle(this.menuState); 
      }
    }
    
    class null implements Parcelable.ClassLoaderCreator<SavedState> {
      public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel param2Parcel) {
        return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(param2Parcel, null);
      }
      
      public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
        return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(param2Parcel, param2ClassLoader);
      }
      
      public AppCompatDelegateImpl.PanelFeatureState.SavedState[] newArray(int param2Int) {
        return new AppCompatDelegateImpl.PanelFeatureState.SavedState[param2Int];
      }
    }
  }
  
  private static class SavedState implements Parcelable {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel param3Parcel) {
          return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(param3Parcel, null);
        }
        
        public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel param3Parcel, ClassLoader param3ClassLoader) {
          return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(param3Parcel, param3ClassLoader);
        }
        
        public AppCompatDelegateImpl.PanelFeatureState.SavedState[] newArray(int param3Int) {
          return new AppCompatDelegateImpl.PanelFeatureState.SavedState[param3Int];
        }
      };
    
    int featureId;
    
    boolean isOpen;
    
    Bundle menuState;
    
    static SavedState readFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      SavedState savedState = new SavedState();
      savedState.featureId = param1Parcel.readInt();
      int i = param1Parcel.readInt();
      boolean bool = true;
      if (i != 1)
        bool = false; 
      savedState.isOpen = bool;
      if (bool)
        savedState.menuState = param1Parcel.readBundle(param1ClassLoader); 
      return savedState;
    }
    
    public int describeContents() {
      return 0;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      param1Parcel.writeInt(this.featureId);
      param1Parcel.writeInt(this.isOpen);
      if (this.isOpen)
        param1Parcel.writeBundle(this.menuState); 
    }
  }
  
  class null implements Parcelable.ClassLoaderCreator<PanelFeatureState.SavedState> {
    public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel param1Parcel) {
      return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(param1Parcel, null);
    }
    
    public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(param1Parcel, param1ClassLoader);
    }
    
    public AppCompatDelegateImpl.PanelFeatureState.SavedState[] newArray(int param1Int) {
      return new AppCompatDelegateImpl.PanelFeatureState.SavedState[param1Int];
    }
  }
  
  private final class PanelMenuPresenterCallback implements MenuPresenter.Callback {
    final AppCompatDelegateImpl this$0;
    
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) {
      boolean bool;
      MenuBuilder menuBuilder = param1MenuBuilder.getRootMenu();
      if (menuBuilder != param1MenuBuilder) {
        bool = true;
      } else {
        bool = false;
      } 
      AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
      if (bool)
        param1MenuBuilder = menuBuilder; 
      AppCompatDelegateImpl.PanelFeatureState panelFeatureState = appCompatDelegateImpl.findMenuPanel((Menu)param1MenuBuilder);
      if (panelFeatureState != null)
        if (bool) {
          AppCompatDelegateImpl.this.callOnPanelClosed(panelFeatureState.featureId, panelFeatureState, (Menu)menuBuilder);
          AppCompatDelegateImpl.this.closePanel(panelFeatureState, true);
        } else {
          AppCompatDelegateImpl.this.closePanel(panelFeatureState, param1Boolean);
        }  
    }
    
    public boolean onOpenSubMenu(MenuBuilder param1MenuBuilder) {
      if (param1MenuBuilder == param1MenuBuilder.getRootMenu() && AppCompatDelegateImpl.this.mHasActionBar) {
        Window.Callback callback = AppCompatDelegateImpl.this.getWindowCallback();
        if (callback != null && !AppCompatDelegateImpl.this.mDestroyed)
          callback.onMenuOpened(108, (Menu)param1MenuBuilder); 
      } 
      return true;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\app\AppCompatDelegateImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */