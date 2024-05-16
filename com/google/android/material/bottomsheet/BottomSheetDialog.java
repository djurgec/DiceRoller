package com.google.android.material.bottomsheet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.shape.MaterialShapeDrawable;

public class BottomSheetDialog extends AppCompatDialog {
  private BottomSheetBehavior<FrameLayout> behavior;
  
  private FrameLayout bottomSheet;
  
  private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
      final BottomSheetDialog this$0;
      
      public void onSlide(View param1View, float param1Float) {}
      
      public void onStateChanged(View param1View, int param1Int) {
        if (param1Int == 5)
          BottomSheetDialog.this.cancel(); 
      }
    };
  
  boolean cancelable = true;
  
  private boolean canceledOnTouchOutside = true;
  
  private boolean canceledOnTouchOutsideSet;
  
  private FrameLayout container;
  
  private CoordinatorLayout coordinator;
  
  boolean dismissWithAnimation;
  
  private BottomSheetBehavior.BottomSheetCallback edgeToEdgeCallback;
  
  private boolean edgeToEdgeEnabled;
  
  public BottomSheetDialog(Context paramContext) {
    this(paramContext, 0);
    this.edgeToEdgeEnabled = getContext().getTheme().obtainStyledAttributes(new int[] { R.attr.enableEdgeToEdge }).getBoolean(0, false);
  }
  
  public BottomSheetDialog(Context paramContext, int paramInt) {
    super(paramContext, getThemeResId(paramContext, paramInt));
    supportRequestWindowFeature(1);
    this.edgeToEdgeEnabled = getContext().getTheme().obtainStyledAttributes(new int[] { R.attr.enableEdgeToEdge }).getBoolean(0, false);
  }
  
  protected BottomSheetDialog(Context paramContext, boolean paramBoolean, DialogInterface.OnCancelListener paramOnCancelListener) {
    super(paramContext, paramBoolean, paramOnCancelListener);
    supportRequestWindowFeature(1);
    this.cancelable = paramBoolean;
    this.edgeToEdgeEnabled = getContext().getTheme().obtainStyledAttributes(new int[] { R.attr.enableEdgeToEdge }).getBoolean(0, false);
  }
  
  private FrameLayout ensureContainerAndBehavior() {
    if (this.container == null) {
      FrameLayout frameLayout = (FrameLayout)View.inflate(getContext(), R.layout.design_bottom_sheet_dialog, null);
      this.container = frameLayout;
      this.coordinator = (CoordinatorLayout)frameLayout.findViewById(R.id.coordinator);
      frameLayout = (FrameLayout)this.container.findViewById(R.id.design_bottom_sheet);
      this.bottomSheet = frameLayout;
      BottomSheetBehavior<FrameLayout> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
      this.behavior = bottomSheetBehavior;
      bottomSheetBehavior.addBottomSheetCallback(this.bottomSheetCallback);
      this.behavior.setHideable(this.cancelable);
    } 
    return this.container;
  }
  
  private static int getThemeResId(Context paramContext, int paramInt) {
    int i = paramInt;
    if (paramInt == 0) {
      TypedValue typedValue = new TypedValue();
      if (paramContext.getTheme().resolveAttribute(R.attr.bottomSheetDialogTheme, typedValue, true)) {
        i = typedValue.resourceId;
      } else {
        i = R.style.Theme_Design_Light_BottomSheetDialog;
      } 
    } 
    return i;
  }
  
  public static void setLightStatusBar(View paramView, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 23) {
      int i = paramView.getSystemUiVisibility();
      if (paramBoolean) {
        i |= 0x2000;
      } else {
        i &= 0xFFFFDFFF;
      } 
      paramView.setSystemUiVisibility(i);
    } 
  }
  
  private View wrapInBottomSheet(int paramInt, View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    ensureContainerAndBehavior();
    CoordinatorLayout coordinatorLayout = (CoordinatorLayout)this.container.findViewById(R.id.coordinator);
    View view = paramView;
    if (paramInt != 0) {
      view = paramView;
      if (paramView == null)
        view = getLayoutInflater().inflate(paramInt, (ViewGroup)coordinatorLayout, false); 
    } 
    if (this.edgeToEdgeEnabled)
      ViewCompat.setOnApplyWindowInsetsListener((View)this.bottomSheet, new OnApplyWindowInsetsListener() {
            final BottomSheetDialog this$0;
            
            public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat) {
              if (BottomSheetDialog.this.edgeToEdgeCallback != null)
                BottomSheetDialog.this.behavior.removeBottomSheetCallback(BottomSheetDialog.this.edgeToEdgeCallback); 
              if (param1WindowInsetsCompat != null) {
                BottomSheetDialog.access$002(BottomSheetDialog.this, new BottomSheetDialog.EdgeToEdgeCallback((View)BottomSheetDialog.this.bottomSheet, param1WindowInsetsCompat));
                BottomSheetDialog.this.behavior.addBottomSheetCallback(BottomSheetDialog.this.edgeToEdgeCallback);
              } 
              return param1WindowInsetsCompat;
            }
          }); 
    this.bottomSheet.removeAllViews();
    if (paramLayoutParams == null) {
      this.bottomSheet.addView(view);
    } else {
      this.bottomSheet.addView(view, paramLayoutParams);
    } 
    coordinatorLayout.findViewById(R.id.touch_outside).setOnClickListener(new View.OnClickListener() {
          final BottomSheetDialog this$0;
          
          public void onClick(View param1View) {
            if (BottomSheetDialog.this.cancelable && BottomSheetDialog.this.isShowing() && BottomSheetDialog.this.shouldWindowCloseOnTouchOutside())
              BottomSheetDialog.this.cancel(); 
          }
        });
    ViewCompat.setAccessibilityDelegate((View)this.bottomSheet, new AccessibilityDelegateCompat() {
          final BottomSheetDialog this$0;
          
          public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
            if (BottomSheetDialog.this.cancelable) {
              param1AccessibilityNodeInfoCompat.addAction(1048576);
              param1AccessibilityNodeInfoCompat.setDismissable(true);
            } else {
              param1AccessibilityNodeInfoCompat.setDismissable(false);
            } 
          }
          
          public boolean performAccessibilityAction(View param1View, int param1Int, Bundle param1Bundle) {
            if (param1Int == 1048576 && BottomSheetDialog.this.cancelable) {
              BottomSheetDialog.this.cancel();
              return true;
            } 
            return super.performAccessibilityAction(param1View, param1Int, param1Bundle);
          }
        });
    this.bottomSheet.setOnTouchListener(new View.OnTouchListener() {
          final BottomSheetDialog this$0;
          
          public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
            return true;
          }
        });
    return (View)this.container;
  }
  
  public void cancel() {
    BottomSheetBehavior<FrameLayout> bottomSheetBehavior = getBehavior();
    if (!this.dismissWithAnimation || bottomSheetBehavior.getState() == 5) {
      super.cancel();
      return;
    } 
    bottomSheetBehavior.setState(5);
  }
  
  public BottomSheetBehavior<FrameLayout> getBehavior() {
    if (this.behavior == null)
      ensureContainerAndBehavior(); 
    return this.behavior;
  }
  
  public boolean getDismissWithAnimation() {
    return this.dismissWithAnimation;
  }
  
  public boolean getEdgeToEdgeEnabled() {
    return this.edgeToEdgeEnabled;
  }
  
  public void onAttachedToWindow() {
    super.onAttachedToWindow();
    Window window = getWindow();
    if (window != null && Build.VERSION.SDK_INT >= 21) {
      boolean bool1;
      boolean bool = this.edgeToEdgeEnabled;
      boolean bool2 = true;
      if (bool && Color.alpha(window.getNavigationBarColor()) < 255) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      FrameLayout frameLayout = this.container;
      if (frameLayout != null) {
        if (!bool1) {
          bool = true;
        } else {
          bool = false;
        } 
        frameLayout.setFitsSystemWindows(bool);
      } 
      CoordinatorLayout coordinatorLayout = this.coordinator;
      if (coordinatorLayout != null) {
        if (!bool1) {
          bool = bool2;
        } else {
          bool = false;
        } 
        coordinatorLayout.setFitsSystemWindows(bool);
      } 
      if (bool1)
        window.getDecorView().setSystemUiVisibility(768); 
    } 
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    Window window = getWindow();
    if (window != null) {
      if (Build.VERSION.SDK_INT >= 21) {
        window.setStatusBarColor(0);
        window.addFlags(-2147483648);
        if (Build.VERSION.SDK_INT < 23)
          window.addFlags(67108864); 
      } 
      window.setLayout(-1, -1);
    } 
  }
  
  protected void onStart() {
    super.onStart();
    BottomSheetBehavior<FrameLayout> bottomSheetBehavior = this.behavior;
    if (bottomSheetBehavior != null && bottomSheetBehavior.getState() == 5)
      this.behavior.setState(4); 
  }
  
  void removeDefaultCallback() {
    this.behavior.removeBottomSheetCallback(this.bottomSheetCallback);
  }
  
  public void setCancelable(boolean paramBoolean) {
    super.setCancelable(paramBoolean);
    if (this.cancelable != paramBoolean) {
      this.cancelable = paramBoolean;
      BottomSheetBehavior<FrameLayout> bottomSheetBehavior = this.behavior;
      if (bottomSheetBehavior != null)
        bottomSheetBehavior.setHideable(paramBoolean); 
    } 
  }
  
  public void setCanceledOnTouchOutside(boolean paramBoolean) {
    super.setCanceledOnTouchOutside(paramBoolean);
    if (paramBoolean && !this.cancelable)
      this.cancelable = true; 
    this.canceledOnTouchOutside = paramBoolean;
    this.canceledOnTouchOutsideSet = true;
  }
  
  public void setContentView(int paramInt) {
    super.setContentView(wrapInBottomSheet(paramInt, (View)null, (ViewGroup.LayoutParams)null));
  }
  
  public void setContentView(View paramView) {
    super.setContentView(wrapInBottomSheet(0, paramView, (ViewGroup.LayoutParams)null));
  }
  
  public void setContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    super.setContentView(wrapInBottomSheet(0, paramView, paramLayoutParams));
  }
  
  public void setDismissWithAnimation(boolean paramBoolean) {
    this.dismissWithAnimation = paramBoolean;
  }
  
  boolean shouldWindowCloseOnTouchOutside() {
    if (!this.canceledOnTouchOutsideSet) {
      TypedArray typedArray = getContext().obtainStyledAttributes(new int[] { 16843611 });
      this.canceledOnTouchOutside = typedArray.getBoolean(0, true);
      typedArray.recycle();
      this.canceledOnTouchOutsideSet = true;
    } 
    return this.canceledOnTouchOutside;
  }
  
  private static class EdgeToEdgeCallback extends BottomSheetBehavior.BottomSheetCallback {
    private final WindowInsetsCompat insetsCompat;
    
    private final boolean lightBottomSheet;
    
    private final boolean lightStatusBar;
    
    private EdgeToEdgeCallback(View param1View, WindowInsetsCompat param1WindowInsetsCompat) {
      ColorStateList colorStateList;
      boolean bool;
      this.insetsCompat = param1WindowInsetsCompat;
      if (Build.VERSION.SDK_INT >= 23 && (param1View.getSystemUiVisibility() & 0x2000) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      this.lightStatusBar = bool;
      MaterialShapeDrawable materialShapeDrawable = BottomSheetBehavior.<View>from(param1View).getMaterialShapeDrawable();
      if (materialShapeDrawable != null) {
        colorStateList = materialShapeDrawable.getFillColor();
      } else {
        colorStateList = ViewCompat.getBackgroundTintList(param1View);
      } 
      if (colorStateList != null) {
        this.lightBottomSheet = MaterialColors.isColorLight(colorStateList.getDefaultColor());
      } else if (param1View.getBackground() instanceof ColorDrawable) {
        this.lightBottomSheet = MaterialColors.isColorLight(((ColorDrawable)param1View.getBackground()).getColor());
      } else {
        this.lightBottomSheet = bool;
      } 
    }
    
    private void setPaddingForPosition(View param1View) {
      if (param1View.getTop() < this.insetsCompat.getSystemWindowInsetTop()) {
        BottomSheetDialog.setLightStatusBar(param1View, this.lightBottomSheet);
        param1View.setPadding(param1View.getPaddingLeft(), this.insetsCompat.getSystemWindowInsetTop() - param1View.getTop(), param1View.getPaddingRight(), param1View.getPaddingBottom());
      } else if (param1View.getTop() != 0) {
        BottomSheetDialog.setLightStatusBar(param1View, this.lightStatusBar);
        param1View.setPadding(param1View.getPaddingLeft(), 0, param1View.getPaddingRight(), param1View.getPaddingBottom());
      } 
    }
    
    public void onSlide(View param1View, float param1Float) {
      setPaddingForPosition(param1View);
    }
    
    public void onStateChanged(View param1View, int param1Int) {
      setPaddingForPosition(param1View);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\bottomsheet\BottomSheetDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */