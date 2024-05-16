package androidx.fragment.app;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.R;
import java.util.ArrayList;

public final class FragmentContainerView extends FrameLayout {
  private View.OnApplyWindowInsetsListener mApplyWindowInsetsListener;
  
  private ArrayList<View> mDisappearingFragmentChildren;
  
  private boolean mDrawDisappearingViewsFirst = true;
  
  private ArrayList<View> mTransitioningFragmentViews;
  
  public FragmentContainerView(Context paramContext) {
    super(paramContext);
  }
  
  public FragmentContainerView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public FragmentContainerView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    if (paramAttributeSet != null) {
      String str3 = paramAttributeSet.getClassAttribute();
      String str4 = "class";
      TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.FragmentContainerView);
      String str1 = str3;
      String str2 = str4;
      if (str3 == null) {
        str1 = typedArray.getString(R.styleable.FragmentContainerView_android_name);
        str2 = "android:name";
      } 
      typedArray.recycle();
      if (str1 != null && !isInEditMode())
        throw new UnsupportedOperationException("FragmentContainerView must be within a FragmentActivity to use " + str2 + "=\"" + str1 + "\""); 
    } 
  }
  
  FragmentContainerView(Context paramContext, AttributeSet paramAttributeSet, FragmentManager paramFragmentManager) {
    super(paramContext, paramAttributeSet);
    String str2 = paramAttributeSet.getClassAttribute();
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.FragmentContainerView);
    String str1 = str2;
    if (str2 == null)
      str1 = typedArray.getString(R.styleable.FragmentContainerView_android_name); 
    str2 = typedArray.getString(R.styleable.FragmentContainerView_android_tag);
    typedArray.recycle();
    int i = getId();
    Fragment fragment = paramFragmentManager.findFragmentById(i);
    if (str1 != null && fragment == null) {
      String str;
      if (i <= 0) {
        if (str2 != null) {
          str = " with tag " + str2;
        } else {
          str = "";
        } 
        throw new IllegalStateException("FragmentContainerView must have an android:id to add Fragment " + str1 + str);
      } 
      Fragment fragment1 = paramFragmentManager.getFragmentFactory().instantiate(str.getClassLoader(), str1);
      fragment1.onInflate((Context)str, paramAttributeSet, (Bundle)null);
      paramFragmentManager.beginTransaction().setReorderingAllowed(true).add((ViewGroup)this, fragment1, str2).commitNowAllowingStateLoss();
    } 
    paramFragmentManager.onContainerAvailable(this);
  }
  
  private void addDisappearingFragmentView(View paramView) {
    ArrayList<View> arrayList = this.mTransitioningFragmentViews;
    if (arrayList != null && arrayList.contains(paramView)) {
      if (this.mDisappearingFragmentChildren == null)
        this.mDisappearingFragmentChildren = new ArrayList<>(); 
      this.mDisappearingFragmentChildren.add(paramView);
    } 
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    if (FragmentManager.getViewFragment(paramView) != null) {
      super.addView(paramView, paramInt, paramLayoutParams);
      return;
    } 
    throw new IllegalStateException("Views added to a FragmentContainerView must be associated with a Fragment. View " + paramView + " is not associated with a Fragment.");
  }
  
  protected boolean addViewInLayout(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams, boolean paramBoolean) {
    if (FragmentManager.getViewFragment(paramView) != null)
      return super.addViewInLayout(paramView, paramInt, paramLayoutParams, paramBoolean); 
    throw new IllegalStateException("Views added to a FragmentContainerView must be associated with a Fragment. View " + paramView + " is not associated with a Fragment.");
  }
  
  public WindowInsets dispatchApplyWindowInsets(WindowInsets paramWindowInsets) {
    WindowInsetsCompat windowInsetsCompat = WindowInsetsCompat.toWindowInsetsCompat(paramWindowInsets);
    View.OnApplyWindowInsetsListener onApplyWindowInsetsListener = this.mApplyWindowInsetsListener;
    if (onApplyWindowInsetsListener != null) {
      windowInsetsCompat = WindowInsetsCompat.toWindowInsetsCompat(onApplyWindowInsetsListener.onApplyWindowInsets((View)this, paramWindowInsets));
    } else {
      windowInsetsCompat = ViewCompat.onApplyWindowInsets((View)this, windowInsetsCompat);
    } 
    if (!windowInsetsCompat.isConsumed()) {
      int i = getChildCount();
      for (byte b = 0; b < i; b++)
        ViewCompat.dispatchApplyWindowInsets(getChildAt(b), windowInsetsCompat); 
    } 
    return paramWindowInsets;
  }
  
  protected void dispatchDraw(Canvas paramCanvas) {
    if (this.mDrawDisappearingViewsFirst && this.mDisappearingFragmentChildren != null)
      for (byte b = 0; b < this.mDisappearingFragmentChildren.size(); b++)
        super.drawChild(paramCanvas, this.mDisappearingFragmentChildren.get(b), getDrawingTime());  
    super.dispatchDraw(paramCanvas);
  }
  
  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong) {
    if (this.mDrawDisappearingViewsFirst) {
      ArrayList<View> arrayList = this.mDisappearingFragmentChildren;
      if (arrayList != null && arrayList.size() > 0 && this.mDisappearingFragmentChildren.contains(paramView))
        return false; 
    } 
    return super.drawChild(paramCanvas, paramView, paramLong);
  }
  
  public void endViewTransition(View paramView) {
    ArrayList<View> arrayList = this.mTransitioningFragmentViews;
    if (arrayList != null) {
      arrayList.remove(paramView);
      arrayList = this.mDisappearingFragmentChildren;
      if (arrayList != null && arrayList.remove(paramView))
        this.mDrawDisappearingViewsFirst = true; 
    } 
    super.endViewTransition(paramView);
  }
  
  public WindowInsets onApplyWindowInsets(WindowInsets paramWindowInsets) {
    return paramWindowInsets;
  }
  
  public void removeAllViewsInLayout() {
    for (int i = getChildCount() - 1; i >= 0; i--)
      addDisappearingFragmentView(getChildAt(i)); 
    super.removeAllViewsInLayout();
  }
  
  protected void removeDetachedView(View paramView, boolean paramBoolean) {
    if (paramBoolean)
      addDisappearingFragmentView(paramView); 
    super.removeDetachedView(paramView, paramBoolean);
  }
  
  public void removeView(View paramView) {
    addDisappearingFragmentView(paramView);
    super.removeView(paramView);
  }
  
  public void removeViewAt(int paramInt) {
    addDisappearingFragmentView(getChildAt(paramInt));
    super.removeViewAt(paramInt);
  }
  
  public void removeViewInLayout(View paramView) {
    addDisappearingFragmentView(paramView);
    super.removeViewInLayout(paramView);
  }
  
  public void removeViews(int paramInt1, int paramInt2) {
    for (int i = paramInt1; i < paramInt1 + paramInt2; i++)
      addDisappearingFragmentView(getChildAt(i)); 
    super.removeViews(paramInt1, paramInt2);
  }
  
  public void removeViewsInLayout(int paramInt1, int paramInt2) {
    for (int i = paramInt1; i < paramInt1 + paramInt2; i++)
      addDisappearingFragmentView(getChildAt(i)); 
    super.removeViewsInLayout(paramInt1, paramInt2);
  }
  
  void setDrawDisappearingViewsLast(boolean paramBoolean) {
    this.mDrawDisappearingViewsFirst = paramBoolean;
  }
  
  public void setLayoutTransition(LayoutTransition paramLayoutTransition) {
    if (Build.VERSION.SDK_INT < 18) {
      super.setLayoutTransition(paramLayoutTransition);
      return;
    } 
    throw new UnsupportedOperationException("FragmentContainerView does not support Layout Transitions or animateLayoutChanges=\"true\".");
  }
  
  public void setOnApplyWindowInsetsListener(View.OnApplyWindowInsetsListener paramOnApplyWindowInsetsListener) {
    this.mApplyWindowInsetsListener = paramOnApplyWindowInsetsListener;
  }
  
  public void startViewTransition(View paramView) {
    if (paramView.getParent() == this) {
      if (this.mTransitioningFragmentViews == null)
        this.mTransitioningFragmentViews = new ArrayList<>(); 
      this.mTransitioningFragmentViews.add(paramView);
    } 
    super.startViewTransition(paramView);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\fragment\app\FragmentContainerView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */