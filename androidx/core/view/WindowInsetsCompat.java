package androidx.core.view;

import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.View;
import android.view.WindowInsets;
import androidx.core.graphics.Insets;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

public class WindowInsetsCompat {
  public static final WindowInsetsCompat CONSUMED;
  
  private static final String TAG = "WindowInsetsCompat";
  
  private final Impl mImpl;
  
  static {
    if (Build.VERSION.SDK_INT >= 30) {
      CONSUMED = Impl30.CONSUMED;
    } else {
      CONSUMED = Impl.CONSUMED;
    } 
  }
  
  private WindowInsetsCompat(WindowInsets paramWindowInsets) {
    if (Build.VERSION.SDK_INT >= 30) {
      this.mImpl = new Impl30(this, paramWindowInsets);
    } else if (Build.VERSION.SDK_INT >= 29) {
      this.mImpl = new Impl29(this, paramWindowInsets);
    } else if (Build.VERSION.SDK_INT >= 28) {
      this.mImpl = new Impl28(this, paramWindowInsets);
    } else if (Build.VERSION.SDK_INT >= 21) {
      this.mImpl = new Impl21(this, paramWindowInsets);
    } else if (Build.VERSION.SDK_INT >= 20) {
      this.mImpl = new Impl20(this, paramWindowInsets);
    } else {
      this.mImpl = new Impl(this);
    } 
  }
  
  public WindowInsetsCompat(WindowInsetsCompat paramWindowInsetsCompat) {
    if (paramWindowInsetsCompat != null) {
      Impl impl = paramWindowInsetsCompat.mImpl;
      if (Build.VERSION.SDK_INT >= 30 && impl instanceof Impl30) {
        this.mImpl = new Impl30(this, (Impl30)impl);
      } else if (Build.VERSION.SDK_INT >= 29 && impl instanceof Impl29) {
        this.mImpl = new Impl29(this, (Impl29)impl);
      } else if (Build.VERSION.SDK_INT >= 28 && impl instanceof Impl28) {
        this.mImpl = new Impl28(this, (Impl28)impl);
      } else if (Build.VERSION.SDK_INT >= 21 && impl instanceof Impl21) {
        this.mImpl = new Impl21(this, (Impl21)impl);
      } else if (Build.VERSION.SDK_INT >= 20 && impl instanceof Impl20) {
        this.mImpl = new Impl20(this, (Impl20)impl);
      } else {
        this.mImpl = new Impl(this);
      } 
      impl.copyWindowDataInto(this);
    } else {
      this.mImpl = new Impl(this);
    } 
  }
  
  static Insets insetInsets(Insets paramInsets, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = Math.max(0, paramInsets.left - paramInt1);
    int j = Math.max(0, paramInsets.top - paramInt2);
    int m = Math.max(0, paramInsets.right - paramInt3);
    int k = Math.max(0, paramInsets.bottom - paramInt4);
    return (i == paramInt1 && j == paramInt2 && m == paramInt3 && k == paramInt4) ? paramInsets : Insets.of(i, j, m, k);
  }
  
  public static WindowInsetsCompat toWindowInsetsCompat(WindowInsets paramWindowInsets) {
    return toWindowInsetsCompat(paramWindowInsets, null);
  }
  
  public static WindowInsetsCompat toWindowInsetsCompat(WindowInsets paramWindowInsets, View paramView) {
    WindowInsetsCompat windowInsetsCompat = new WindowInsetsCompat((WindowInsets)Preconditions.checkNotNull(paramWindowInsets));
    if (paramView != null && ViewCompat.isAttachedToWindow(paramView)) {
      windowInsetsCompat.setRootWindowInsets(ViewCompat.getRootWindowInsets(paramView));
      windowInsetsCompat.copyRootViewBounds(paramView.getRootView());
    } 
    return windowInsetsCompat;
  }
  
  @Deprecated
  public WindowInsetsCompat consumeDisplayCutout() {
    return this.mImpl.consumeDisplayCutout();
  }
  
  @Deprecated
  public WindowInsetsCompat consumeStableInsets() {
    return this.mImpl.consumeStableInsets();
  }
  
  @Deprecated
  public WindowInsetsCompat consumeSystemWindowInsets() {
    return this.mImpl.consumeSystemWindowInsets();
  }
  
  void copyRootViewBounds(View paramView) {
    this.mImpl.copyRootViewBounds(paramView);
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (!(paramObject instanceof WindowInsetsCompat))
      return false; 
    paramObject = paramObject;
    return ObjectsCompat.equals(this.mImpl, ((WindowInsetsCompat)paramObject).mImpl);
  }
  
  public DisplayCutoutCompat getDisplayCutout() {
    return this.mImpl.getDisplayCutout();
  }
  
  public Insets getInsets(int paramInt) {
    return this.mImpl.getInsets(paramInt);
  }
  
  public Insets getInsetsIgnoringVisibility(int paramInt) {
    return this.mImpl.getInsetsIgnoringVisibility(paramInt);
  }
  
  @Deprecated
  public Insets getMandatorySystemGestureInsets() {
    return this.mImpl.getMandatorySystemGestureInsets();
  }
  
  @Deprecated
  public int getStableInsetBottom() {
    return (this.mImpl.getStableInsets()).bottom;
  }
  
  @Deprecated
  public int getStableInsetLeft() {
    return (this.mImpl.getStableInsets()).left;
  }
  
  @Deprecated
  public int getStableInsetRight() {
    return (this.mImpl.getStableInsets()).right;
  }
  
  @Deprecated
  public int getStableInsetTop() {
    return (this.mImpl.getStableInsets()).top;
  }
  
  @Deprecated
  public Insets getStableInsets() {
    return this.mImpl.getStableInsets();
  }
  
  @Deprecated
  public Insets getSystemGestureInsets() {
    return this.mImpl.getSystemGestureInsets();
  }
  
  @Deprecated
  public int getSystemWindowInsetBottom() {
    return (this.mImpl.getSystemWindowInsets()).bottom;
  }
  
  @Deprecated
  public int getSystemWindowInsetLeft() {
    return (this.mImpl.getSystemWindowInsets()).left;
  }
  
  @Deprecated
  public int getSystemWindowInsetRight() {
    return (this.mImpl.getSystemWindowInsets()).right;
  }
  
  @Deprecated
  public int getSystemWindowInsetTop() {
    return (this.mImpl.getSystemWindowInsets()).top;
  }
  
  @Deprecated
  public Insets getSystemWindowInsets() {
    return this.mImpl.getSystemWindowInsets();
  }
  
  @Deprecated
  public Insets getTappableElementInsets() {
    return this.mImpl.getTappableElementInsets();
  }
  
  public boolean hasInsets() {
    return (!getInsets(Type.all()).equals(Insets.NONE) || !getInsetsIgnoringVisibility(Type.all() ^ Type.ime()).equals(Insets.NONE) || getDisplayCutout() != null);
  }
  
  @Deprecated
  public boolean hasStableInsets() {
    return this.mImpl.getStableInsets().equals(Insets.NONE) ^ true;
  }
  
  @Deprecated
  public boolean hasSystemWindowInsets() {
    return this.mImpl.getSystemWindowInsets().equals(Insets.NONE) ^ true;
  }
  
  public int hashCode() {
    int i;
    Impl impl = this.mImpl;
    if (impl == null) {
      i = 0;
    } else {
      i = impl.hashCode();
    } 
    return i;
  }
  
  public WindowInsetsCompat inset(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    return this.mImpl.inset(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public WindowInsetsCompat inset(Insets paramInsets) {
    return inset(paramInsets.left, paramInsets.top, paramInsets.right, paramInsets.bottom);
  }
  
  public boolean isConsumed() {
    return this.mImpl.isConsumed();
  }
  
  public boolean isRound() {
    return this.mImpl.isRound();
  }
  
  public boolean isVisible(int paramInt) {
    return this.mImpl.isVisible(paramInt);
  }
  
  @Deprecated
  public WindowInsetsCompat replaceSystemWindowInsets(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    return (new Builder(this)).setSystemWindowInsets(Insets.of(paramInt1, paramInt2, paramInt3, paramInt4)).build();
  }
  
  @Deprecated
  public WindowInsetsCompat replaceSystemWindowInsets(Rect paramRect) {
    return (new Builder(this)).setSystemWindowInsets(Insets.of(paramRect)).build();
  }
  
  void setOverriddenInsets(Insets[] paramArrayOfInsets) {
    this.mImpl.setOverriddenInsets(paramArrayOfInsets);
  }
  
  void setRootViewData(Insets paramInsets) {
    this.mImpl.setRootViewData(paramInsets);
  }
  
  void setRootWindowInsets(WindowInsetsCompat paramWindowInsetsCompat) {
    this.mImpl.setRootWindowInsets(paramWindowInsetsCompat);
  }
  
  void setStableInsets(Insets paramInsets) {
    this.mImpl.setStableInsets(paramInsets);
  }
  
  public WindowInsets toWindowInsets() {
    Impl impl = this.mImpl;
    if (impl instanceof Impl20) {
      WindowInsets windowInsets = ((Impl20)impl).mPlatformInsets;
    } else {
      impl = null;
    } 
    return (WindowInsets)impl;
  }
  
  static class Api21ReflectionHolder {
    private static Field sContentInsets;
    
    private static boolean sReflectionSucceeded;
    
    private static Field sStableInsets;
    
    private static Field sViewAttachInfoField;
    
    static {
      try {
        Field field = View.class.getDeclaredField("mAttachInfo");
        sViewAttachInfoField = field;
        field.setAccessible(true);
        Class<?> clazz = Class.forName("android.view.View$AttachInfo");
        field = clazz.getDeclaredField("mStableInsets");
        sStableInsets = field;
        field.setAccessible(true);
        field = clazz.getDeclaredField("mContentInsets");
        sContentInsets = field;
        field.setAccessible(true);
        sReflectionSucceeded = true;
      } catch (ReflectiveOperationException reflectiveOperationException) {
        Log.w("WindowInsetsCompat", "Failed to get visible insets from AttachInfo " + reflectiveOperationException.getMessage(), reflectiveOperationException);
      } 
    }
    
    public static WindowInsetsCompat getRootWindowInsets(View param1View) {
      if (!sReflectionSucceeded || !param1View.isAttachedToWindow())
        return null; 
      View view = param1View.getRootView();
      try {
        Object object = sViewAttachInfoField.get(view);
        if (object != null) {
          Rect rect1 = (Rect)sStableInsets.get(object);
          Rect rect2 = (Rect)sContentInsets.get(object);
          if (rect1 != null && rect2 != null) {
            object = new WindowInsetsCompat.Builder();
            super();
            WindowInsetsCompat windowInsetsCompat = object.setStableInsets(Insets.of(rect1)).setSystemWindowInsets(Insets.of(rect2)).build();
            windowInsetsCompat.setRootWindowInsets(windowInsetsCompat);
            windowInsetsCompat.copyRootViewBounds(param1View.getRootView());
            return windowInsetsCompat;
          } 
        } 
      } catch (IllegalAccessException illegalAccessException) {
        Log.w("WindowInsetsCompat", "Failed to get insets from AttachInfo. " + illegalAccessException.getMessage(), illegalAccessException);
      } 
      return null;
    }
  }
  
  public static final class Builder {
    private final WindowInsetsCompat.BuilderImpl mImpl;
    
    public Builder() {
      if (Build.VERSION.SDK_INT >= 30) {
        this.mImpl = new WindowInsetsCompat.BuilderImpl30();
      } else if (Build.VERSION.SDK_INT >= 29) {
        this.mImpl = new WindowInsetsCompat.BuilderImpl29();
      } else if (Build.VERSION.SDK_INT >= 20) {
        this.mImpl = new WindowInsetsCompat.BuilderImpl20();
      } else {
        this.mImpl = new WindowInsetsCompat.BuilderImpl();
      } 
    }
    
    public Builder(WindowInsetsCompat param1WindowInsetsCompat) {
      if (Build.VERSION.SDK_INT >= 30) {
        this.mImpl = new WindowInsetsCompat.BuilderImpl30(param1WindowInsetsCompat);
      } else if (Build.VERSION.SDK_INT >= 29) {
        this.mImpl = new WindowInsetsCompat.BuilderImpl29(param1WindowInsetsCompat);
      } else if (Build.VERSION.SDK_INT >= 20) {
        this.mImpl = new WindowInsetsCompat.BuilderImpl20(param1WindowInsetsCompat);
      } else {
        this.mImpl = new WindowInsetsCompat.BuilderImpl(param1WindowInsetsCompat);
      } 
    }
    
    public WindowInsetsCompat build() {
      return this.mImpl.build();
    }
    
    public Builder setDisplayCutout(DisplayCutoutCompat param1DisplayCutoutCompat) {
      this.mImpl.setDisplayCutout(param1DisplayCutoutCompat);
      return this;
    }
    
    public Builder setInsets(int param1Int, Insets param1Insets) {
      this.mImpl.setInsets(param1Int, param1Insets);
      return this;
    }
    
    public Builder setInsetsIgnoringVisibility(int param1Int, Insets param1Insets) {
      this.mImpl.setInsetsIgnoringVisibility(param1Int, param1Insets);
      return this;
    }
    
    @Deprecated
    public Builder setMandatorySystemGestureInsets(Insets param1Insets) {
      this.mImpl.setMandatorySystemGestureInsets(param1Insets);
      return this;
    }
    
    @Deprecated
    public Builder setStableInsets(Insets param1Insets) {
      this.mImpl.setStableInsets(param1Insets);
      return this;
    }
    
    @Deprecated
    public Builder setSystemGestureInsets(Insets param1Insets) {
      this.mImpl.setSystemGestureInsets(param1Insets);
      return this;
    }
    
    @Deprecated
    public Builder setSystemWindowInsets(Insets param1Insets) {
      this.mImpl.setSystemWindowInsets(param1Insets);
      return this;
    }
    
    @Deprecated
    public Builder setTappableElementInsets(Insets param1Insets) {
      this.mImpl.setTappableElementInsets(param1Insets);
      return this;
    }
    
    public Builder setVisible(int param1Int, boolean param1Boolean) {
      this.mImpl.setVisible(param1Int, param1Boolean);
      return this;
    }
  }
  
  private static class BuilderImpl {
    private final WindowInsetsCompat mInsets;
    
    Insets[] mInsetsTypeMask;
    
    BuilderImpl() {
      this(new WindowInsetsCompat((WindowInsetsCompat)null));
    }
    
    BuilderImpl(WindowInsetsCompat param1WindowInsetsCompat) {
      this.mInsets = param1WindowInsetsCompat;
    }
    
    protected final void applyInsetTypes() {
      Insets[] arrayOfInsets = this.mInsetsTypeMask;
      if (arrayOfInsets != null) {
        Insets insets3 = arrayOfInsets[WindowInsetsCompat.Type.indexOf(1)];
        Insets insets2 = this.mInsetsTypeMask[WindowInsetsCompat.Type.indexOf(2)];
        Insets insets1 = insets2;
        if (insets2 == null)
          insets1 = this.mInsets.getInsets(2); 
        insets2 = insets3;
        if (insets3 == null)
          insets2 = this.mInsets.getInsets(1); 
        setSystemWindowInsets(Insets.max(insets2, insets1));
        insets1 = this.mInsetsTypeMask[WindowInsetsCompat.Type.indexOf(16)];
        if (insets1 != null)
          setSystemGestureInsets(insets1); 
        insets1 = this.mInsetsTypeMask[WindowInsetsCompat.Type.indexOf(32)];
        if (insets1 != null)
          setMandatorySystemGestureInsets(insets1); 
        insets1 = this.mInsetsTypeMask[WindowInsetsCompat.Type.indexOf(64)];
        if (insets1 != null)
          setTappableElementInsets(insets1); 
      } 
    }
    
    WindowInsetsCompat build() {
      applyInsetTypes();
      return this.mInsets;
    }
    
    void setDisplayCutout(DisplayCutoutCompat param1DisplayCutoutCompat) {}
    
    void setInsets(int param1Int, Insets param1Insets) {
      if (this.mInsetsTypeMask == null)
        this.mInsetsTypeMask = new Insets[9]; 
      for (int i = 1; i <= 256; i <<= 1) {
        if ((param1Int & i) != 0)
          this.mInsetsTypeMask[WindowInsetsCompat.Type.indexOf(i)] = param1Insets; 
      } 
    }
    
    void setInsetsIgnoringVisibility(int param1Int, Insets param1Insets) {
      if (param1Int != 8)
        return; 
      throw new IllegalArgumentException("Ignoring visibility inset not available for IME");
    }
    
    void setMandatorySystemGestureInsets(Insets param1Insets) {}
    
    void setStableInsets(Insets param1Insets) {}
    
    void setSystemGestureInsets(Insets param1Insets) {}
    
    void setSystemWindowInsets(Insets param1Insets) {}
    
    void setTappableElementInsets(Insets param1Insets) {}
    
    void setVisible(int param1Int, boolean param1Boolean) {}
  }
  
  private static class BuilderImpl20 extends BuilderImpl {
    private static Constructor<WindowInsets> sConstructor;
    
    private static boolean sConstructorFetched = false;
    
    private static Field sConsumedField;
    
    private static boolean sConsumedFieldFetched = false;
    
    private WindowInsets mPlatformInsets = createWindowInsetsInstance();
    
    private Insets mStableInsets;
    
    static {
    
    }
    
    BuilderImpl20() {}
    
    BuilderImpl20(WindowInsetsCompat param1WindowInsetsCompat) {
      super(param1WindowInsetsCompat);
    }
    
    private static WindowInsets createWindowInsetsInstance() {
      if (!sConsumedFieldFetched) {
        try {
          sConsumedField = WindowInsets.class.getDeclaredField("CONSUMED");
        } catch (ReflectiveOperationException reflectiveOperationException) {
          Log.i("WindowInsetsCompat", "Could not retrieve WindowInsets.CONSUMED field", reflectiveOperationException);
        } 
        sConsumedFieldFetched = true;
      } 
      Field field = sConsumedField;
      if (field != null)
        try {
          WindowInsets windowInsets = (WindowInsets)field.get((Object)null);
          if (windowInsets != null)
            return new WindowInsets(windowInsets); 
        } catch (ReflectiveOperationException reflectiveOperationException) {
          Log.i("WindowInsetsCompat", "Could not get value from WindowInsets.CONSUMED field", reflectiveOperationException);
        }  
      if (!sConstructorFetched) {
        try {
          sConstructor = WindowInsets.class.getConstructor(new Class[] { Rect.class });
        } catch (ReflectiveOperationException reflectiveOperationException) {
          Log.i("WindowInsetsCompat", "Could not retrieve WindowInsets(Rect) constructor", reflectiveOperationException);
        } 
        sConstructorFetched = true;
      } 
      Constructor<WindowInsets> constructor = sConstructor;
      if (constructor != null)
        try {
          Rect rect = new Rect();
          this();
          return constructor.newInstance(new Object[] { rect });
        } catch (ReflectiveOperationException reflectiveOperationException) {
          Log.i("WindowInsetsCompat", "Could not invoke WindowInsets(Rect) constructor", reflectiveOperationException);
        }  
      return null;
    }
    
    WindowInsetsCompat build() {
      applyInsetTypes();
      WindowInsetsCompat windowInsetsCompat = WindowInsetsCompat.toWindowInsetsCompat(this.mPlatformInsets);
      windowInsetsCompat.setOverriddenInsets(this.mInsetsTypeMask);
      windowInsetsCompat.setStableInsets(this.mStableInsets);
      return windowInsetsCompat;
    }
    
    void setStableInsets(Insets param1Insets) {
      this.mStableInsets = param1Insets;
    }
    
    void setSystemWindowInsets(Insets param1Insets) {
      WindowInsets windowInsets = this.mPlatformInsets;
      if (windowInsets != null)
        this.mPlatformInsets = windowInsets.replaceSystemWindowInsets(param1Insets.left, param1Insets.top, param1Insets.right, param1Insets.bottom); 
    }
  }
  
  private static class BuilderImpl29 extends BuilderImpl {
    final WindowInsets.Builder mPlatBuilder;
    
    BuilderImpl29() {
      this.mPlatBuilder = new WindowInsets.Builder();
    }
    
    BuilderImpl29(WindowInsetsCompat param1WindowInsetsCompat) {
      super(param1WindowInsetsCompat);
      WindowInsets.Builder builder;
      WindowInsets windowInsets = param1WindowInsetsCompat.toWindowInsets();
      if (windowInsets != null) {
        builder = new WindowInsets.Builder(windowInsets);
      } else {
        builder = new WindowInsets.Builder();
      } 
      this.mPlatBuilder = builder;
    }
    
    WindowInsetsCompat build() {
      applyInsetTypes();
      WindowInsetsCompat windowInsetsCompat = WindowInsetsCompat.toWindowInsetsCompat(this.mPlatBuilder.build());
      windowInsetsCompat.setOverriddenInsets(this.mInsetsTypeMask);
      return windowInsetsCompat;
    }
    
    void setDisplayCutout(DisplayCutoutCompat param1DisplayCutoutCompat) {
      WindowInsets.Builder builder = this.mPlatBuilder;
      if (param1DisplayCutoutCompat != null) {
        DisplayCutout displayCutout = param1DisplayCutoutCompat.unwrap();
      } else {
        param1DisplayCutoutCompat = null;
      } 
      builder.setDisplayCutout((DisplayCutout)param1DisplayCutoutCompat);
    }
    
    void setMandatorySystemGestureInsets(Insets param1Insets) {
      this.mPlatBuilder.setMandatorySystemGestureInsets(param1Insets.toPlatformInsets());
    }
    
    void setStableInsets(Insets param1Insets) {
      this.mPlatBuilder.setStableInsets(param1Insets.toPlatformInsets());
    }
    
    void setSystemGestureInsets(Insets param1Insets) {
      this.mPlatBuilder.setSystemGestureInsets(param1Insets.toPlatformInsets());
    }
    
    void setSystemWindowInsets(Insets param1Insets) {
      this.mPlatBuilder.setSystemWindowInsets(param1Insets.toPlatformInsets());
    }
    
    void setTappableElementInsets(Insets param1Insets) {
      this.mPlatBuilder.setTappableElementInsets(param1Insets.toPlatformInsets());
    }
  }
  
  private static class BuilderImpl30 extends BuilderImpl29 {
    BuilderImpl30() {}
    
    BuilderImpl30(WindowInsetsCompat param1WindowInsetsCompat) {
      super(param1WindowInsetsCompat);
    }
    
    void setInsets(int param1Int, Insets param1Insets) {
      this.mPlatBuilder.setInsets(WindowInsetsCompat.TypeImpl30.toPlatformType(param1Int), param1Insets.toPlatformInsets());
    }
    
    void setInsetsIgnoringVisibility(int param1Int, Insets param1Insets) {
      this.mPlatBuilder.setInsetsIgnoringVisibility(WindowInsetsCompat.TypeImpl30.toPlatformType(param1Int), param1Insets.toPlatformInsets());
    }
    
    void setVisible(int param1Int, boolean param1Boolean) {
      this.mPlatBuilder.setVisible(WindowInsetsCompat.TypeImpl30.toPlatformType(param1Int), param1Boolean);
    }
  }
  
  private static class Impl {
    static final WindowInsetsCompat CONSUMED = (new WindowInsetsCompat.Builder()).build().consumeDisplayCutout().consumeStableInsets().consumeSystemWindowInsets();
    
    final WindowInsetsCompat mHost;
    
    Impl(WindowInsetsCompat param1WindowInsetsCompat) {
      this.mHost = param1WindowInsetsCompat;
    }
    
    WindowInsetsCompat consumeDisplayCutout() {
      return this.mHost;
    }
    
    WindowInsetsCompat consumeStableInsets() {
      return this.mHost;
    }
    
    WindowInsetsCompat consumeSystemWindowInsets() {
      return this.mHost;
    }
    
    void copyRootViewBounds(View param1View) {}
    
    void copyWindowDataInto(WindowInsetsCompat param1WindowInsetsCompat) {}
    
    public boolean equals(Object param1Object) {
      boolean bool = true;
      if (this == param1Object)
        return true; 
      if (!(param1Object instanceof Impl))
        return false; 
      param1Object = param1Object;
      if (isRound() != param1Object.isRound() || isConsumed() != param1Object.isConsumed() || !ObjectsCompat.equals(getSystemWindowInsets(), param1Object.getSystemWindowInsets()) || !ObjectsCompat.equals(getStableInsets(), param1Object.getStableInsets()) || !ObjectsCompat.equals(getDisplayCutout(), param1Object.getDisplayCutout()))
        bool = false; 
      return bool;
    }
    
    DisplayCutoutCompat getDisplayCutout() {
      return null;
    }
    
    Insets getInsets(int param1Int) {
      return Insets.NONE;
    }
    
    Insets getInsetsIgnoringVisibility(int param1Int) {
      if ((param1Int & 0x8) == 0)
        return Insets.NONE; 
      throw new IllegalArgumentException("Unable to query the maximum insets for IME");
    }
    
    Insets getMandatorySystemGestureInsets() {
      return getSystemWindowInsets();
    }
    
    Insets getStableInsets() {
      return Insets.NONE;
    }
    
    Insets getSystemGestureInsets() {
      return getSystemWindowInsets();
    }
    
    Insets getSystemWindowInsets() {
      return Insets.NONE;
    }
    
    Insets getTappableElementInsets() {
      return getSystemWindowInsets();
    }
    
    public int hashCode() {
      return ObjectsCompat.hash(new Object[] { Boolean.valueOf(isRound()), Boolean.valueOf(isConsumed()), getSystemWindowInsets(), getStableInsets(), getDisplayCutout() });
    }
    
    WindowInsetsCompat inset(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      return CONSUMED;
    }
    
    boolean isConsumed() {
      return false;
    }
    
    boolean isRound() {
      return false;
    }
    
    boolean isVisible(int param1Int) {
      return true;
    }
    
    public void setOverriddenInsets(Insets[] param1ArrayOfInsets) {}
    
    void setRootViewData(Insets param1Insets) {}
    
    void setRootWindowInsets(WindowInsetsCompat param1WindowInsetsCompat) {}
    
    public void setStableInsets(Insets param1Insets) {}
  }
  
  private static class Impl20 extends Impl {
    private static Class<?> sAttachInfoClass;
    
    private static Field sAttachInfoField;
    
    private static Method sGetViewRootImplMethod;
    
    private static Field sVisibleInsetsField;
    
    private static boolean sVisibleRectReflectionFetched = false;
    
    private Insets[] mOverriddenInsets;
    
    final WindowInsets mPlatformInsets;
    
    Insets mRootViewVisibleInsets;
    
    private WindowInsetsCompat mRootWindowInsets;
    
    private Insets mSystemWindowInsets = null;
    
    Impl20(WindowInsetsCompat param1WindowInsetsCompat, WindowInsets param1WindowInsets) {
      super(param1WindowInsetsCompat);
      this.mPlatformInsets = param1WindowInsets;
    }
    
    Impl20(WindowInsetsCompat param1WindowInsetsCompat, Impl20 param1Impl20) {
      this(param1WindowInsetsCompat, new WindowInsets(param1Impl20.mPlatformInsets));
    }
    
    private Insets getInsets(int param1Int, boolean param1Boolean) {
      Insets insets = Insets.NONE;
      for (int i = 1; i <= 256; i <<= 1) {
        if ((param1Int & i) != 0)
          insets = Insets.max(insets, getInsetsForType(i, param1Boolean)); 
      } 
      return insets;
    }
    
    private Insets getRootStableInsets() {
      WindowInsetsCompat windowInsetsCompat = this.mRootWindowInsets;
      return (windowInsetsCompat != null) ? windowInsetsCompat.getStableInsets() : Insets.NONE;
    }
    
    private Insets getVisibleInsets(View param1View) {
      if (Build.VERSION.SDK_INT < 30) {
        if (!sVisibleRectReflectionFetched)
          loadReflectionField(); 
        Method method = sGetViewRootImplMethod;
        Object object = null;
        if (method == null || sAttachInfoClass == null || sVisibleInsetsField == null)
          return null; 
        try {
          Object object1 = method.invoke(param1View, new Object[0]);
          if (object1 == null) {
            object1 = new NullPointerException();
            super();
            Log.w("WindowInsetsCompat", "Failed to get visible insets. getViewRootImpl() returned null from the provided view. This means that the view is either not attached or the method has been overridden", (Throwable)object1);
            return null;
          } 
          object1 = sAttachInfoField.get(object1);
          Rect rect = (Rect)sVisibleInsetsField.get(object1);
          object1 = object;
          if (rect != null)
            object1 = Insets.of(rect); 
          return (Insets)object1;
        } catch (ReflectiveOperationException reflectiveOperationException) {
          Log.e("WindowInsetsCompat", "Failed to get visible insets. (Reflection error). " + reflectiveOperationException.getMessage(), reflectiveOperationException);
          return null;
        } 
      } 
      throw new UnsupportedOperationException("getVisibleInsets() should not be called on API >= 30. Use WindowInsets.isVisible() instead.");
    }
    
    private static void loadReflectionField() {
      try {
        sGetViewRootImplMethod = View.class.getDeclaredMethod("getViewRootImpl", new Class[0]);
        Class<?> clazz = Class.forName("android.view.View$AttachInfo");
        sAttachInfoClass = clazz;
        sVisibleInsetsField = clazz.getDeclaredField("mVisibleInsets");
        sAttachInfoField = Class.forName("android.view.ViewRootImpl").getDeclaredField("mAttachInfo");
        sVisibleInsetsField.setAccessible(true);
        sAttachInfoField.setAccessible(true);
      } catch (ReflectiveOperationException reflectiveOperationException) {
        Log.e("WindowInsetsCompat", "Failed to get visible insets. (Reflection error). " + reflectiveOperationException.getMessage(), reflectiveOperationException);
      } 
      sVisibleRectReflectionFetched = true;
    }
    
    void copyRootViewBounds(View param1View) {
      Insets insets2 = getVisibleInsets(param1View);
      Insets insets1 = insets2;
      if (insets2 == null)
        insets1 = Insets.NONE; 
      setRootViewData(insets1);
    }
    
    void copyWindowDataInto(WindowInsetsCompat param1WindowInsetsCompat) {
      param1WindowInsetsCompat.setRootWindowInsets(this.mRootWindowInsets);
      param1WindowInsetsCompat.setRootViewData(this.mRootViewVisibleInsets);
    }
    
    public boolean equals(Object param1Object) {
      if (!super.equals(param1Object))
        return false; 
      param1Object = param1Object;
      return Objects.equals(this.mRootViewVisibleInsets, ((Impl20)param1Object).mRootViewVisibleInsets);
    }
    
    public Insets getInsets(int param1Int) {
      return getInsets(param1Int, false);
    }
    
    protected Insets getInsetsForType(int param1Int, boolean param1Boolean) {
      int i;
      DisplayCutoutCompat displayCutoutCompat;
      Insets[] arrayOfInsets1;
      Insets insets1;
      Insets insets2;
      WindowInsetsCompat windowInsetsCompat2;
      WindowInsetsCompat windowInsetsCompat1 = null;
      Insets[] arrayOfInsets2 = null;
      switch (param1Int) {
        default:
          return Insets.NONE;
        case 128:
          windowInsetsCompat1 = this.mRootWindowInsets;
          if (windowInsetsCompat1 != null) {
            displayCutoutCompat = windowInsetsCompat1.getDisplayCutout();
          } else {
            displayCutoutCompat = getDisplayCutout();
          } 
          return (displayCutoutCompat != null) ? Insets.of(displayCutoutCompat.getSafeInsetLeft(), displayCutoutCompat.getSafeInsetTop(), displayCutoutCompat.getSafeInsetRight(), displayCutoutCompat.getSafeInsetBottom()) : Insets.NONE;
        case 64:
          return getTappableElementInsets();
        case 32:
          return getMandatorySystemGestureInsets();
        case 16:
          return getSystemGestureInsets();
        case 8:
          arrayOfInsets1 = this.mOverriddenInsets;
          if (arrayOfInsets1 != null) {
            Insets insets = arrayOfInsets1[WindowInsetsCompat.Type.indexOf(8)];
          } else {
            arrayOfInsets1 = arrayOfInsets2;
          } 
          if (arrayOfInsets1 != null)
            return (Insets)arrayOfInsets1; 
          insets2 = getSystemWindowInsets();
          insets1 = getRootStableInsets();
          if (insets2.bottom > insets1.bottom)
            return Insets.of(0, 0, 0, insets2.bottom); 
          insets2 = this.mRootViewVisibleInsets;
          return (insets2 != null && !insets2.equals(Insets.NONE) && this.mRootViewVisibleInsets.bottom > insets1.bottom) ? Insets.of(0, 0, 0, this.mRootViewVisibleInsets.bottom) : Insets.NONE;
        case 2:
          if (param1Boolean) {
            insets1 = getRootStableInsets();
            insets2 = getStableInsets();
            return Insets.of(Math.max(insets1.left, insets2.left), 0, Math.max(insets1.right, insets2.right), Math.max(insets1.bottom, insets2.bottom));
          } 
          insets2 = getSystemWindowInsets();
          windowInsetsCompat2 = this.mRootWindowInsets;
          if (windowInsetsCompat2 != null)
            insets1 = windowInsetsCompat2.getStableInsets(); 
          i = insets2.bottom;
          param1Int = i;
          if (insets1 != null)
            param1Int = Math.min(i, insets1.bottom); 
          return Insets.of(insets2.left, 0, insets2.right, param1Int);
        case 1:
          break;
      } 
      return param1Boolean ? Insets.of(0, Math.max((getRootStableInsets()).top, (getSystemWindowInsets()).top), 0, 0) : Insets.of(0, (getSystemWindowInsets()).top, 0, 0);
    }
    
    public Insets getInsetsIgnoringVisibility(int param1Int) {
      return getInsets(param1Int, true);
    }
    
    final Insets getSystemWindowInsets() {
      if (this.mSystemWindowInsets == null)
        this.mSystemWindowInsets = Insets.of(this.mPlatformInsets.getSystemWindowInsetLeft(), this.mPlatformInsets.getSystemWindowInsetTop(), this.mPlatformInsets.getSystemWindowInsetRight(), this.mPlatformInsets.getSystemWindowInsetBottom()); 
      return this.mSystemWindowInsets;
    }
    
    WindowInsetsCompat inset(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      WindowInsetsCompat.Builder builder = new WindowInsetsCompat.Builder(WindowInsetsCompat.toWindowInsetsCompat(this.mPlatformInsets));
      builder.setSystemWindowInsets(WindowInsetsCompat.insetInsets(getSystemWindowInsets(), param1Int1, param1Int2, param1Int3, param1Int4));
      builder.setStableInsets(WindowInsetsCompat.insetInsets(getStableInsets(), param1Int1, param1Int2, param1Int3, param1Int4));
      return builder.build();
    }
    
    boolean isRound() {
      return this.mPlatformInsets.isRound();
    }
    
    protected boolean isTypeVisible(int param1Int) {
      switch (param1Int) {
        default:
          return true;
        case 4:
          return false;
        case 1:
        case 2:
        case 8:
        case 128:
          break;
      } 
      return true ^ getInsetsForType(param1Int, false).equals(Insets.NONE);
    }
    
    boolean isVisible(int param1Int) {
      for (int i = 1; i <= 256; i <<= 1) {
        if ((param1Int & i) != 0 && !isTypeVisible(i))
          return false; 
      } 
      return true;
    }
    
    public void setOverriddenInsets(Insets[] param1ArrayOfInsets) {
      this.mOverriddenInsets = param1ArrayOfInsets;
    }
    
    void setRootViewData(Insets param1Insets) {
      this.mRootViewVisibleInsets = param1Insets;
    }
    
    void setRootWindowInsets(WindowInsetsCompat param1WindowInsetsCompat) {
      this.mRootWindowInsets = param1WindowInsetsCompat;
    }
  }
  
  private static class Impl21 extends Impl20 {
    private Insets mStableInsets = null;
    
    Impl21(WindowInsetsCompat param1WindowInsetsCompat, WindowInsets param1WindowInsets) {
      super(param1WindowInsetsCompat, param1WindowInsets);
    }
    
    Impl21(WindowInsetsCompat param1WindowInsetsCompat, Impl21 param1Impl21) {
      super(param1WindowInsetsCompat, param1Impl21);
      this.mStableInsets = param1Impl21.mStableInsets;
    }
    
    WindowInsetsCompat consumeStableInsets() {
      return WindowInsetsCompat.toWindowInsetsCompat(this.mPlatformInsets.consumeStableInsets());
    }
    
    WindowInsetsCompat consumeSystemWindowInsets() {
      return WindowInsetsCompat.toWindowInsetsCompat(this.mPlatformInsets.consumeSystemWindowInsets());
    }
    
    final Insets getStableInsets() {
      if (this.mStableInsets == null)
        this.mStableInsets = Insets.of(this.mPlatformInsets.getStableInsetLeft(), this.mPlatformInsets.getStableInsetTop(), this.mPlatformInsets.getStableInsetRight(), this.mPlatformInsets.getStableInsetBottom()); 
      return this.mStableInsets;
    }
    
    boolean isConsumed() {
      return this.mPlatformInsets.isConsumed();
    }
    
    public void setStableInsets(Insets param1Insets) {
      this.mStableInsets = param1Insets;
    }
  }
  
  private static class Impl28 extends Impl21 {
    Impl28(WindowInsetsCompat param1WindowInsetsCompat, WindowInsets param1WindowInsets) {
      super(param1WindowInsetsCompat, param1WindowInsets);
    }
    
    Impl28(WindowInsetsCompat param1WindowInsetsCompat, Impl28 param1Impl28) {
      super(param1WindowInsetsCompat, param1Impl28);
    }
    
    WindowInsetsCompat consumeDisplayCutout() {
      return WindowInsetsCompat.toWindowInsetsCompat(this.mPlatformInsets.consumeDisplayCutout());
    }
    
    public boolean equals(Object param1Object) {
      boolean bool = true;
      if (this == param1Object)
        return true; 
      if (!(param1Object instanceof Impl28))
        return false; 
      param1Object = param1Object;
      if (!Objects.equals(this.mPlatformInsets, ((Impl28)param1Object).mPlatformInsets) || !Objects.equals(this.mRootViewVisibleInsets, ((Impl28)param1Object).mRootViewVisibleInsets))
        bool = false; 
      return bool;
    }
    
    DisplayCutoutCompat getDisplayCutout() {
      return DisplayCutoutCompat.wrap(this.mPlatformInsets.getDisplayCutout());
    }
    
    public int hashCode() {
      return this.mPlatformInsets.hashCode();
    }
  }
  
  private static class Impl29 extends Impl28 {
    private Insets mMandatorySystemGestureInsets = null;
    
    private Insets mSystemGestureInsets = null;
    
    private Insets mTappableElementInsets = null;
    
    Impl29(WindowInsetsCompat param1WindowInsetsCompat, WindowInsets param1WindowInsets) {
      super(param1WindowInsetsCompat, param1WindowInsets);
    }
    
    Impl29(WindowInsetsCompat param1WindowInsetsCompat, Impl29 param1Impl29) {
      super(param1WindowInsetsCompat, param1Impl29);
    }
    
    Insets getMandatorySystemGestureInsets() {
      if (this.mMandatorySystemGestureInsets == null)
        this.mMandatorySystemGestureInsets = Insets.toCompatInsets(this.mPlatformInsets.getMandatorySystemGestureInsets()); 
      return this.mMandatorySystemGestureInsets;
    }
    
    Insets getSystemGestureInsets() {
      if (this.mSystemGestureInsets == null)
        this.mSystemGestureInsets = Insets.toCompatInsets(this.mPlatformInsets.getSystemGestureInsets()); 
      return this.mSystemGestureInsets;
    }
    
    Insets getTappableElementInsets() {
      if (this.mTappableElementInsets == null)
        this.mTappableElementInsets = Insets.toCompatInsets(this.mPlatformInsets.getTappableElementInsets()); 
      return this.mTappableElementInsets;
    }
    
    WindowInsetsCompat inset(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      return WindowInsetsCompat.toWindowInsetsCompat(this.mPlatformInsets.inset(param1Int1, param1Int2, param1Int3, param1Int4));
    }
    
    public void setStableInsets(Insets param1Insets) {}
  }
  
  private static class Impl30 extends Impl29 {
    static final WindowInsetsCompat CONSUMED = WindowInsetsCompat.toWindowInsetsCompat(WindowInsets.CONSUMED);
    
    Impl30(WindowInsetsCompat param1WindowInsetsCompat, WindowInsets param1WindowInsets) {
      super(param1WindowInsetsCompat, param1WindowInsets);
    }
    
    Impl30(WindowInsetsCompat param1WindowInsetsCompat, Impl30 param1Impl30) {
      super(param1WindowInsetsCompat, param1Impl30);
    }
    
    final void copyRootViewBounds(View param1View) {}
    
    public Insets getInsets(int param1Int) {
      return Insets.toCompatInsets(this.mPlatformInsets.getInsets(WindowInsetsCompat.TypeImpl30.toPlatformType(param1Int)));
    }
    
    public Insets getInsetsIgnoringVisibility(int param1Int) {
      return Insets.toCompatInsets(this.mPlatformInsets.getInsetsIgnoringVisibility(WindowInsetsCompat.TypeImpl30.toPlatformType(param1Int)));
    }
    
    public boolean isVisible(int param1Int) {
      return this.mPlatformInsets.isVisible(WindowInsetsCompat.TypeImpl30.toPlatformType(param1Int));
    }
  }
  
  public static final class Type {
    static final int CAPTION_BAR = 4;
    
    static final int DISPLAY_CUTOUT = 128;
    
    static final int FIRST = 1;
    
    static final int IME = 8;
    
    static final int LAST = 256;
    
    static final int MANDATORY_SYSTEM_GESTURES = 32;
    
    static final int NAVIGATION_BARS = 2;
    
    static final int SIZE = 9;
    
    static final int STATUS_BARS = 1;
    
    static final int SYSTEM_GESTURES = 16;
    
    static final int TAPPABLE_ELEMENT = 64;
    
    static final int WINDOW_DECOR = 256;
    
    static int all() {
      return -1;
    }
    
    public static int captionBar() {
      return 4;
    }
    
    public static int displayCutout() {
      return 128;
    }
    
    public static int ime() {
      return 8;
    }
    
    static int indexOf(int param1Int) {
      switch (param1Int) {
        default:
          throw new IllegalArgumentException("type needs to be >= FIRST and <= LAST, type=" + param1Int);
        case 256:
          return 8;
        case 128:
          return 7;
        case 64:
          return 6;
        case 32:
          return 5;
        case 16:
          return 4;
        case 8:
          return 3;
        case 4:
          return 2;
        case 2:
          return 1;
        case 1:
          break;
      } 
      return 0;
    }
    
    public static int mandatorySystemGestures() {
      return 32;
    }
    
    public static int navigationBars() {
      return 2;
    }
    
    public static int statusBars() {
      return 1;
    }
    
    public static int systemBars() {
      return 7;
    }
    
    public static int systemGestures() {
      return 16;
    }
    
    public static int tappableElement() {
      return 64;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface InsetsType {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface InsetsType {}
  
  private static final class TypeImpl30 {
    static int toPlatformType(int param1Int) {
      int j = 0;
      int i = 1;
      while (i <= 256) {
        int k = j;
        if ((param1Int & i) != 0)
          switch (i) {
            default:
              k = j;
              break;
            case 128:
              k = j | WindowInsets.Type.displayCutout();
              break;
            case 64:
              k = j | WindowInsets.Type.tappableElement();
              break;
            case 32:
              k = j | WindowInsets.Type.mandatorySystemGestures();
              break;
            case 16:
              k = j | WindowInsets.Type.systemGestures();
              break;
            case 8:
              k = j | WindowInsets.Type.ime();
              break;
            case 4:
              k = j | WindowInsets.Type.captionBar();
              break;
            case 2:
              k = j | WindowInsets.Type.navigationBars();
              break;
            case 1:
              k = j | WindowInsets.Type.statusBars();
              break;
          }  
        i <<= 1;
        j = k;
      } 
      return j;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\view\WindowInsetsCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */