package com.google.android.material.appbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ToolbarUtils;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;

public class MaterialToolbar extends Toolbar {
  private static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_Toolbar;
  
  private Integer navigationIconTint;
  
  private boolean subtitleCentered;
  
  private boolean titleCentered;
  
  public MaterialToolbar(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public MaterialToolbar(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.toolbarStyle);
  }
  
  public MaterialToolbar(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(MaterialThemeOverlay.wrap(paramContext, paramAttributeSet, paramInt, i), paramAttributeSet, paramInt);
    paramContext = getContext();
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.MaterialToolbar, paramInt, i, new int[0]);
    if (typedArray.hasValue(R.styleable.MaterialToolbar_navigationIconTint))
      setNavigationIconTint(typedArray.getColor(R.styleable.MaterialToolbar_navigationIconTint, -1)); 
    this.titleCentered = typedArray.getBoolean(R.styleable.MaterialToolbar_titleCentered, false);
    this.subtitleCentered = typedArray.getBoolean(R.styleable.MaterialToolbar_subtitleCentered, false);
    typedArray.recycle();
    initBackground(paramContext);
  }
  
  private Pair<Integer, Integer> calculateTitleBoundLimits(TextView paramTextView1, TextView paramTextView2) {
    int i = getMeasuredWidth();
    int m = i / 2;
    int j = getPaddingLeft();
    int k = i - getPaddingRight();
    byte b = 0;
    while (b < getChildCount()) {
      View view = getChildAt(b);
      int i1 = j;
      int n = k;
      if (view.getVisibility() != 8) {
        i1 = j;
        n = k;
        if (view != paramTextView1) {
          i1 = j;
          n = k;
          if (view != paramTextView2) {
            i = j;
            if (view.getRight() < m) {
              i = j;
              if (view.getRight() > j)
                i = view.getRight(); 
            } 
            i1 = i;
            n = k;
            if (view.getLeft() > m) {
              i1 = i;
              n = k;
              if (view.getLeft() < k) {
                n = view.getLeft();
                i1 = i;
              } 
            } 
          } 
        } 
      } 
      b++;
      j = i1;
      k = n;
    } 
    return new Pair(Integer.valueOf(j), Integer.valueOf(k));
  }
  
  private void initBackground(Context paramContext) {
    boolean bool;
    Drawable drawable = getBackground();
    if (drawable != null && !(drawable instanceof ColorDrawable))
      return; 
    MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
    if (drawable != null) {
      bool = ((ColorDrawable)drawable).getColor();
    } else {
      bool = false;
    } 
    materialShapeDrawable.setFillColor(ColorStateList.valueOf(bool));
    materialShapeDrawable.initializeElevationOverlay(paramContext);
    materialShapeDrawable.setElevation(ViewCompat.getElevation((View)this));
    ViewCompat.setBackground((View)this, (Drawable)materialShapeDrawable);
  }
  
  private void layoutTitleCenteredHorizontally(View paramView, Pair<Integer, Integer> paramPair) {
    int j = getMeasuredWidth();
    int i = paramView.getMeasuredWidth();
    int k = j / 2 - i / 2;
    int m = k + i;
    int n = Math.max(Math.max(((Integer)paramPair.first).intValue() - k, 0), Math.max(m - ((Integer)paramPair.second).intValue(), 0));
    j = k;
    i = m;
    if (n > 0) {
      j = k + n;
      i = m - n;
      paramView.measure(View.MeasureSpec.makeMeasureSpec(i - j, 1073741824), paramView.getMeasuredHeightAndState());
    } 
    paramView.layout(j, paramView.getTop(), i, paramView.getBottom());
  }
  
  private void maybeCenterTitleViews() {
    if (!this.titleCentered && !this.subtitleCentered)
      return; 
    TextView textView1 = ToolbarUtils.getTitleTextView(this);
    TextView textView2 = ToolbarUtils.getSubtitleTextView(this);
    if (textView1 == null && textView2 == null)
      return; 
    Pair<Integer, Integer> pair = calculateTitleBoundLimits(textView1, textView2);
    if (this.titleCentered && textView1 != null)
      layoutTitleCenteredHorizontally((View)textView1, pair); 
    if (this.subtitleCentered && textView2 != null)
      layoutTitleCenteredHorizontally((View)textView2, pair); 
  }
  
  private Drawable maybeTintNavigationIcon(Drawable paramDrawable) {
    if (paramDrawable != null && this.navigationIconTint != null) {
      paramDrawable = DrawableCompat.wrap(paramDrawable);
      DrawableCompat.setTint(paramDrawable, this.navigationIconTint.intValue());
      return paramDrawable;
    } 
    return paramDrawable;
  }
  
  public boolean isSubtitleCentered() {
    return this.subtitleCentered;
  }
  
  public boolean isTitleCentered() {
    return this.titleCentered;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    MaterialShapeUtils.setParentAbsoluteElevation((View)this);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    maybeCenterTitleViews();
  }
  
  public void setElevation(float paramFloat) {
    super.setElevation(paramFloat);
    MaterialShapeUtils.setElevation((View)this, paramFloat);
  }
  
  public void setNavigationIcon(Drawable paramDrawable) {
    super.setNavigationIcon(maybeTintNavigationIcon(paramDrawable));
  }
  
  public void setNavigationIconTint(int paramInt) {
    this.navigationIconTint = Integer.valueOf(paramInt);
    Drawable drawable = getNavigationIcon();
    if (drawable != null)
      setNavigationIcon(drawable); 
  }
  
  public void setSubtitleCentered(boolean paramBoolean) {
    if (this.subtitleCentered != paramBoolean) {
      this.subtitleCentered = paramBoolean;
      requestLayout();
    } 
  }
  
  public void setTitleCentered(boolean paramBoolean) {
    if (this.titleCentered != paramBoolean) {
      this.titleCentered = paramBoolean;
      requestLayout();
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\appbar\MaterialToolbar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */