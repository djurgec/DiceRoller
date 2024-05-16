package com.google.android.material.snackbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import com.google.android.material.R;

public class Snackbar extends BaseTransientBottomBar<Snackbar> {
  private static final int[] SNACKBAR_BUTTON_STYLE_ATTR = new int[] { R.attr.snackbarButtonStyle };
  
  private static final int[] SNACKBAR_CONTENT_STYLE_ATTRS = new int[] { R.attr.snackbarButtonStyle, R.attr.snackbarTextViewStyle };
  
  private final AccessibilityManager accessibilityManager;
  
  private BaseTransientBottomBar.BaseCallback<Snackbar> callback;
  
  private boolean hasAction;
  
  private Snackbar(Context paramContext, ViewGroup paramViewGroup, View paramView, ContentViewCallback paramContentViewCallback) {
    super(paramContext, paramViewGroup, paramView, paramContentViewCallback);
    this.accessibilityManager = (AccessibilityManager)paramViewGroup.getContext().getSystemService("accessibility");
  }
  
  private static ViewGroup findSuitableParent(View paramView) {
    ViewGroup viewGroup = null;
    while (true) {
      ViewParent viewParent2;
      if (paramView instanceof androidx.coordinatorlayout.widget.CoordinatorLayout)
        return (ViewGroup)paramView; 
      if (paramView instanceof android.widget.FrameLayout) {
        if (paramView.getId() == 16908290)
          return (ViewGroup)paramView; 
        viewGroup = (ViewGroup)paramView;
      } 
      View view = paramView;
      if (paramView != null) {
        ViewParent viewParent = paramView.getParent();
        if (viewParent instanceof View) {
          View view1 = (View)viewParent;
        } else {
          viewParent = null;
        } 
        viewParent2 = viewParent;
      } 
      if (viewParent2 == null)
        return viewGroup; 
      ViewParent viewParent1 = viewParent2;
    } 
  }
  
  @Deprecated
  protected static boolean hasSnackbarButtonStyleAttr(Context paramContext) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(SNACKBAR_BUTTON_STYLE_ATTR);
    boolean bool = false;
    int i = typedArray.getResourceId(0, -1);
    typedArray.recycle();
    if (i != -1)
      bool = true; 
    return bool;
  }
  
  private static boolean hasSnackbarContentStyleAttrs(Context paramContext) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(SNACKBAR_CONTENT_STYLE_ATTRS);
    boolean bool2 = false;
    int i = typedArray.getResourceId(0, -1);
    int j = typedArray.getResourceId(1, -1);
    typedArray.recycle();
    boolean bool1 = bool2;
    if (i != -1) {
      bool1 = bool2;
      if (j != -1)
        bool1 = true; 
    } 
    return bool1;
  }
  
  public static Snackbar make(Context paramContext, View paramView, CharSequence paramCharSequence, int paramInt) {
    return makeInternal(paramContext, paramView, paramCharSequence, paramInt);
  }
  
  public static Snackbar make(View paramView, int paramInt1, int paramInt2) {
    return make(paramView, paramView.getResources().getText(paramInt1), paramInt2);
  }
  
  public static Snackbar make(View paramView, CharSequence paramCharSequence, int paramInt) {
    return makeInternal((Context)null, paramView, paramCharSequence, paramInt);
  }
  
  private static Snackbar makeInternal(Context paramContext, View paramView, CharSequence paramCharSequence, int paramInt) {
    ViewGroup viewGroup = findSuitableParent(paramView);
    if (viewGroup != null) {
      int i;
      Context context = paramContext;
      if (paramContext == null)
        context = viewGroup.getContext(); 
      LayoutInflater layoutInflater = LayoutInflater.from(context);
      if (hasSnackbarContentStyleAttrs(context)) {
        i = R.layout.mtrl_layout_snackbar_include;
      } else {
        i = R.layout.design_layout_snackbar_include;
      } 
      SnackbarContentLayout snackbarContentLayout = (SnackbarContentLayout)layoutInflater.inflate(i, viewGroup, false);
      Snackbar snackbar = new Snackbar(context, viewGroup, (View)snackbarContentLayout, snackbarContentLayout);
      snackbar.setText(paramCharSequence);
      snackbar.setDuration(paramInt);
      return snackbar;
    } 
    throw new IllegalArgumentException("No suitable parent found from the given view. Please provide a valid view.");
  }
  
  public void dismiss() {
    super.dismiss();
  }
  
  public int getDuration() {
    int j = super.getDuration();
    int i = -2;
    if (j == -2)
      return -2; 
    if (Build.VERSION.SDK_INT >= 29) {
      if (this.hasAction) {
        i = 4;
      } else {
        i = 0;
      } 
      return this.accessibilityManager.getRecommendedTimeoutMillis(j, i | 0x1 | 0x2);
    } 
    if (!this.hasAction || !this.accessibilityManager.isTouchExplorationEnabled())
      i = j; 
    return i;
  }
  
  public boolean isShown() {
    return super.isShown();
  }
  
  public Snackbar setAction(int paramInt, View.OnClickListener paramOnClickListener) {
    return setAction(getContext().getText(paramInt), paramOnClickListener);
  }
  
  public Snackbar setAction(CharSequence paramCharSequence, final View.OnClickListener listener) {
    Button button = ((SnackbarContentLayout)this.view.getChildAt(0)).getActionView();
    if (TextUtils.isEmpty(paramCharSequence) || listener == null) {
      button.setVisibility(8);
      button.setOnClickListener(null);
      this.hasAction = false;
      return this;
    } 
    this.hasAction = true;
    button.setVisibility(0);
    button.setText(paramCharSequence);
    button.setOnClickListener(new View.OnClickListener() {
          final Snackbar this$0;
          
          final View.OnClickListener val$listener;
          
          public void onClick(View param1View) {
            listener.onClick(param1View);
            Snackbar.this.dispatchDismiss(1);
          }
        });
    return this;
  }
  
  public Snackbar setActionTextColor(int paramInt) {
    ((SnackbarContentLayout)this.view.getChildAt(0)).getActionView().setTextColor(paramInt);
    return this;
  }
  
  public Snackbar setActionTextColor(ColorStateList paramColorStateList) {
    ((SnackbarContentLayout)this.view.getChildAt(0)).getActionView().setTextColor(paramColorStateList);
    return this;
  }
  
  public Snackbar setBackgroundTint(int paramInt) {
    return setBackgroundTintList(ColorStateList.valueOf(paramInt));
  }
  
  public Snackbar setBackgroundTintList(ColorStateList paramColorStateList) {
    this.view.setBackgroundTintList(paramColorStateList);
    return this;
  }
  
  public Snackbar setBackgroundTintMode(PorterDuff.Mode paramMode) {
    this.view.setBackgroundTintMode(paramMode);
    return this;
  }
  
  @Deprecated
  public Snackbar setCallback(Callback paramCallback) {
    BaseTransientBottomBar.BaseCallback<Snackbar> baseCallback = this.callback;
    if (baseCallback != null)
      removeCallback(baseCallback); 
    if (paramCallback != null)
      addCallback(paramCallback); 
    this.callback = paramCallback;
    return this;
  }
  
  public Snackbar setMaxInlineActionWidth(int paramInt) {
    ((SnackbarContentLayout)this.view.getChildAt(0)).setMaxInlineActionWidth(paramInt);
    return this;
  }
  
  public Snackbar setText(int paramInt) {
    return setText(getContext().getText(paramInt));
  }
  
  public Snackbar setText(CharSequence paramCharSequence) {
    ((SnackbarContentLayout)this.view.getChildAt(0)).getMessageView().setText(paramCharSequence);
    return this;
  }
  
  public Snackbar setTextColor(int paramInt) {
    ((SnackbarContentLayout)this.view.getChildAt(0)).getMessageView().setTextColor(paramInt);
    return this;
  }
  
  public Snackbar setTextColor(ColorStateList paramColorStateList) {
    ((SnackbarContentLayout)this.view.getChildAt(0)).getMessageView().setTextColor(paramColorStateList);
    return this;
  }
  
  public void show() {
    super.show();
  }
  
  public static class Callback extends BaseTransientBottomBar.BaseCallback<Snackbar> {
    public static final int DISMISS_EVENT_ACTION = 1;
    
    public static final int DISMISS_EVENT_CONSECUTIVE = 4;
    
    public static final int DISMISS_EVENT_MANUAL = 3;
    
    public static final int DISMISS_EVENT_SWIPE = 0;
    
    public static final int DISMISS_EVENT_TIMEOUT = 2;
    
    public void onDismissed(Snackbar param1Snackbar, int param1Int) {}
    
    public void onShown(Snackbar param1Snackbar) {}
  }
  
  public static final class SnackbarLayout extends BaseTransientBottomBar.SnackbarBaseLayout {
    public SnackbarLayout(Context param1Context) {
      super(param1Context);
    }
    
    public SnackbarLayout(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    protected void onMeasure(int param1Int1, int param1Int2) {
      super.onMeasure(param1Int1, param1Int2);
      param1Int2 = getChildCount();
      int k = getMeasuredWidth();
      int i = getPaddingLeft();
      int j = getPaddingRight();
      for (param1Int1 = 0; param1Int1 < param1Int2; param1Int1++) {
        View view = getChildAt(param1Int1);
        if ((view.getLayoutParams()).width == -1)
          view.measure(View.MeasureSpec.makeMeasureSpec(k - i - j, 1073741824), View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), 1073741824)); 
      } 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\snackbar\Snackbar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */