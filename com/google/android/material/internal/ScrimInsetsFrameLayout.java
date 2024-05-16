package com.google.android.material.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.R;

public class ScrimInsetsFrameLayout extends FrameLayout {
  private boolean drawBottomInsetForeground = true;
  
  private boolean drawTopInsetForeground = true;
  
  Drawable insetForeground;
  
  Rect insets;
  
  private Rect tempRect = new Rect();
  
  public ScrimInsetsFrameLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public ScrimInsetsFrameLayout(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ScrimInsetsFrameLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.ScrimInsetsFrameLayout, paramInt, R.style.Widget_Design_ScrimInsetsFrameLayout, new int[0]);
    this.insetForeground = typedArray.getDrawable(R.styleable.ScrimInsetsFrameLayout_insetForeground);
    typedArray.recycle();
    setWillNotDraw(true);
    ViewCompat.setOnApplyWindowInsetsListener((View)this, new OnApplyWindowInsetsListener() {
          final ScrimInsetsFrameLayout this$0;
          
          public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat) {
            if (ScrimInsetsFrameLayout.this.insets == null)
              ScrimInsetsFrameLayout.this.insets = new Rect(); 
            ScrimInsetsFrameLayout.this.insets.set(param1WindowInsetsCompat.getSystemWindowInsetLeft(), param1WindowInsetsCompat.getSystemWindowInsetTop(), param1WindowInsetsCompat.getSystemWindowInsetRight(), param1WindowInsetsCompat.getSystemWindowInsetBottom());
            ScrimInsetsFrameLayout.this.onInsetsChanged(param1WindowInsetsCompat);
            ScrimInsetsFrameLayout scrimInsetsFrameLayout = ScrimInsetsFrameLayout.this;
            if (!param1WindowInsetsCompat.hasSystemWindowInsets() || ScrimInsetsFrameLayout.this.insetForeground == null) {
              boolean bool1 = true;
              scrimInsetsFrameLayout.setWillNotDraw(bool1);
              ViewCompat.postInvalidateOnAnimation((View)ScrimInsetsFrameLayout.this);
              return param1WindowInsetsCompat.consumeSystemWindowInsets();
            } 
            boolean bool = false;
            scrimInsetsFrameLayout.setWillNotDraw(bool);
            ViewCompat.postInvalidateOnAnimation((View)ScrimInsetsFrameLayout.this);
            return param1WindowInsetsCompat.consumeSystemWindowInsets();
          }
        });
  }
  
  public void draw(Canvas paramCanvas) {
    super.draw(paramCanvas);
    int j = getWidth();
    int i = getHeight();
    if (this.insets != null && this.insetForeground != null) {
      int k = paramCanvas.save();
      paramCanvas.translate(getScrollX(), getScrollY());
      if (this.drawTopInsetForeground) {
        this.tempRect.set(0, 0, j, this.insets.top);
        this.insetForeground.setBounds(this.tempRect);
        this.insetForeground.draw(paramCanvas);
      } 
      if (this.drawBottomInsetForeground) {
        this.tempRect.set(0, i - this.insets.bottom, j, i);
        this.insetForeground.setBounds(this.tempRect);
        this.insetForeground.draw(paramCanvas);
      } 
      this.tempRect.set(0, this.insets.top, this.insets.left, i - this.insets.bottom);
      this.insetForeground.setBounds(this.tempRect);
      this.insetForeground.draw(paramCanvas);
      this.tempRect.set(j - this.insets.right, this.insets.top, j, i - this.insets.bottom);
      this.insetForeground.setBounds(this.tempRect);
      this.insetForeground.draw(paramCanvas);
      paramCanvas.restoreToCount(k);
    } 
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    Drawable drawable = this.insetForeground;
    if (drawable != null)
      drawable.setCallback((Drawable.Callback)this); 
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    Drawable drawable = this.insetForeground;
    if (drawable != null)
      drawable.setCallback(null); 
  }
  
  protected void onInsetsChanged(WindowInsetsCompat paramWindowInsetsCompat) {}
  
  public void setDrawBottomInsetForeground(boolean paramBoolean) {
    this.drawBottomInsetForeground = paramBoolean;
  }
  
  public void setDrawTopInsetForeground(boolean paramBoolean) {
    this.drawTopInsetForeground = paramBoolean;
  }
  
  public void setScrimInsetForeground(Drawable paramDrawable) {
    this.insetForeground = paramDrawable;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\internal\ScrimInsetsFrameLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */