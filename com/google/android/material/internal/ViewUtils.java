package com.google.android.material.internal;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.R;

public class ViewUtils {
  public static void addOnGlobalLayoutListener(View paramView, ViewTreeObserver.OnGlobalLayoutListener paramOnGlobalLayoutListener) {
    if (paramView != null)
      paramView.getViewTreeObserver().addOnGlobalLayoutListener(paramOnGlobalLayoutListener); 
  }
  
  public static void doOnApplyWindowInsets(View paramView, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    doOnApplyWindowInsets(paramView, paramAttributeSet, paramInt1, paramInt2, null);
  }
  
  public static void doOnApplyWindowInsets(View paramView, AttributeSet paramAttributeSet, int paramInt1, int paramInt2, final OnApplyWindowInsetsListener listener) {
    TypedArray typedArray = paramView.getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.Insets, paramInt1, paramInt2);
    final boolean paddingBottomSystemWindowInsets = typedArray.getBoolean(R.styleable.Insets_paddingBottomSystemWindowInsets, false);
    final boolean paddingLeftSystemWindowInsets = typedArray.getBoolean(R.styleable.Insets_paddingLeftSystemWindowInsets, false);
    final boolean paddingRightSystemWindowInsets = typedArray.getBoolean(R.styleable.Insets_paddingRightSystemWindowInsets, false);
    typedArray.recycle();
    doOnApplyWindowInsets(paramView, new OnApplyWindowInsetsListener() {
          final ViewUtils.OnApplyWindowInsetsListener val$listener;
          
          final boolean val$paddingBottomSystemWindowInsets;
          
          final boolean val$paddingLeftSystemWindowInsets;
          
          final boolean val$paddingRightSystemWindowInsets;
          
          public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat, ViewUtils.RelativePadding param1RelativePadding) {
            if (paddingBottomSystemWindowInsets)
              param1RelativePadding.bottom += param1WindowInsetsCompat.getSystemWindowInsetBottom(); 
            boolean bool = ViewUtils.isLayoutRtl(param1View);
            if (paddingLeftSystemWindowInsets)
              if (bool) {
                param1RelativePadding.end += param1WindowInsetsCompat.getSystemWindowInsetLeft();
              } else {
                param1RelativePadding.start += param1WindowInsetsCompat.getSystemWindowInsetLeft();
              }  
            if (paddingRightSystemWindowInsets)
              if (bool) {
                param1RelativePadding.start += param1WindowInsetsCompat.getSystemWindowInsetRight();
              } else {
                param1RelativePadding.end += param1WindowInsetsCompat.getSystemWindowInsetRight();
              }  
            param1RelativePadding.applyToView(param1View);
            ViewUtils.OnApplyWindowInsetsListener onApplyWindowInsetsListener = listener;
            if (onApplyWindowInsetsListener != null)
              param1WindowInsetsCompat = onApplyWindowInsetsListener.onApplyWindowInsets(param1View, param1WindowInsetsCompat, param1RelativePadding); 
            return param1WindowInsetsCompat;
          }
        });
  }
  
  public static void doOnApplyWindowInsets(View paramView, final OnApplyWindowInsetsListener listener) {
    ViewCompat.setOnApplyWindowInsetsListener(paramView, new androidx.core.view.OnApplyWindowInsetsListener() {
          final ViewUtils.RelativePadding val$initialPadding;
          
          final ViewUtils.OnApplyWindowInsetsListener val$listener;
          
          public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat) {
            return listener.onApplyWindowInsets(param1View, param1WindowInsetsCompat, new ViewUtils.RelativePadding(initialPadding));
          }
        });
    requestApplyInsetsWhenAttached(paramView);
  }
  
  public static float dpToPx(Context paramContext, int paramInt) {
    Resources resources = paramContext.getResources();
    return TypedValue.applyDimension(1, paramInt, resources.getDisplayMetrics());
  }
  
  public static ViewGroup getContentView(View paramView) {
    if (paramView == null)
      return null; 
    View view = paramView.getRootView();
    ViewGroup viewGroup = (ViewGroup)view.findViewById(16908290);
    return (viewGroup != null) ? viewGroup : ((view != paramView && view instanceof ViewGroup) ? (ViewGroup)view : null);
  }
  
  public static ViewOverlayImpl getContentViewOverlay(View paramView) {
    return getOverlay((View)getContentView(paramView));
  }
  
  public static ViewOverlayImpl getOverlay(View paramView) {
    return (ViewOverlayImpl)((paramView == null) ? null : ((Build.VERSION.SDK_INT >= 18) ? new ViewOverlayApi18(paramView) : ViewOverlayApi14.createFrom(paramView)));
  }
  
  public static float getParentAbsoluteElevation(View paramView) {
    float f = 0.0F;
    for (ViewParent viewParent = paramView.getParent(); viewParent instanceof View; viewParent = viewParent.getParent())
      f += ViewCompat.getElevation((View)viewParent); 
    return f;
  }
  
  public static boolean isLayoutRtl(View paramView) {
    int i = ViewCompat.getLayoutDirection(paramView);
    boolean bool = true;
    if (i != 1)
      bool = false; 
    return bool;
  }
  
  public static PorterDuff.Mode parseTintMode(int paramInt, PorterDuff.Mode paramMode) {
    switch (paramInt) {
      default:
        return paramMode;
      case 16:
        return PorterDuff.Mode.ADD;
      case 15:
        return PorterDuff.Mode.SCREEN;
      case 14:
        return PorterDuff.Mode.MULTIPLY;
      case 9:
        return PorterDuff.Mode.SRC_ATOP;
      case 5:
        return PorterDuff.Mode.SRC_IN;
      case 3:
        break;
    } 
    return PorterDuff.Mode.SRC_OVER;
  }
  
  public static void removeOnGlobalLayoutListener(View paramView, ViewTreeObserver.OnGlobalLayoutListener paramOnGlobalLayoutListener) {
    if (paramView != null)
      removeOnGlobalLayoutListener(paramView.getViewTreeObserver(), paramOnGlobalLayoutListener); 
  }
  
  public static void removeOnGlobalLayoutListener(ViewTreeObserver paramViewTreeObserver, ViewTreeObserver.OnGlobalLayoutListener paramOnGlobalLayoutListener) {
    if (Build.VERSION.SDK_INT >= 16) {
      paramViewTreeObserver.removeOnGlobalLayoutListener(paramOnGlobalLayoutListener);
    } else {
      paramViewTreeObserver.removeGlobalOnLayoutListener(paramOnGlobalLayoutListener);
    } 
  }
  
  public static void requestApplyInsetsWhenAttached(View paramView) {
    if (ViewCompat.isAttachedToWindow(paramView)) {
      ViewCompat.requestApplyInsets(paramView);
    } else {
      paramView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            public void onViewAttachedToWindow(View param1View) {
              param1View.removeOnAttachStateChangeListener(this);
              ViewCompat.requestApplyInsets(param1View);
            }
            
            public void onViewDetachedFromWindow(View param1View) {}
          });
    } 
  }
  
  public static void requestFocusAndShowKeyboard(final View view) {
    view.requestFocus();
    view.post(new Runnable() {
          final View val$view;
          
          public void run() {
            ((InputMethodManager)view.getContext().getSystemService("input_method")).showSoftInput(view, 1);
          }
        });
  }
  
  public static interface OnApplyWindowInsetsListener {
    WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat, ViewUtils.RelativePadding param1RelativePadding);
  }
  
  public static class RelativePadding {
    public int bottom;
    
    public int end;
    
    public int start;
    
    public int top;
    
    public RelativePadding(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      this.start = param1Int1;
      this.top = param1Int2;
      this.end = param1Int3;
      this.bottom = param1Int4;
    }
    
    public RelativePadding(RelativePadding param1RelativePadding) {
      this.start = param1RelativePadding.start;
      this.top = param1RelativePadding.top;
      this.end = param1RelativePadding.end;
      this.bottom = param1RelativePadding.bottom;
    }
    
    public void applyToView(View param1View) {
      ViewCompat.setPaddingRelative(param1View, this.start, this.top, this.end, this.bottom);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\internal\ViewUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */