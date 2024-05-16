package com.google.android.material.card;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Checkable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import com.google.android.material.R;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;

public class MaterialCardView extends CardView implements Checkable, Shapeable {
  private static final String ACCESSIBILITY_CLASS_NAME = "androidx.cardview.widget.CardView";
  
  private static final int[] CHECKABLE_STATE_SET = new int[] { 16842911 };
  
  private static final int[] CHECKED_STATE_SET = new int[] { 16842912 };
  
  private static final int DEF_STYLE_RES;
  
  private static final int[] DRAGGED_STATE_SET = new int[] { R.attr.state_dragged };
  
  private static final String LOG_TAG = "MaterialCardView";
  
  private final MaterialCardViewHelper cardViewHelper;
  
  private boolean checked = false;
  
  private boolean dragged = false;
  
  private boolean isParentCardViewDoneInitializing = true;
  
  private OnCheckedChangeListener onCheckedChangeListener;
  
  static {
    DEF_STYLE_RES = R.style.Widget_MaterialComponents_CardView;
  }
  
  public MaterialCardView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public MaterialCardView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.materialCardViewStyle);
  }
  
  public MaterialCardView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(MaterialThemeOverlay.wrap(paramContext, paramAttributeSet, paramInt, i), paramAttributeSet, paramInt);
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(getContext(), paramAttributeSet, R.styleable.MaterialCardView, paramInt, i, new int[0]);
    MaterialCardViewHelper materialCardViewHelper = new MaterialCardViewHelper(this, paramAttributeSet, paramInt, i);
    this.cardViewHelper = materialCardViewHelper;
    materialCardViewHelper.setCardBackgroundColor(super.getCardBackgroundColor());
    materialCardViewHelper.setUserContentPadding(super.getContentPaddingLeft(), super.getContentPaddingTop(), super.getContentPaddingRight(), super.getContentPaddingBottom());
    materialCardViewHelper.loadFromAttributes(typedArray);
    typedArray.recycle();
  }
  
  private void forceRippleRedrawIfNeeded() {
    if (Build.VERSION.SDK_INT > 26)
      this.cardViewHelper.forceRippleRedraw(); 
  }
  
  private RectF getBoundsAsRectF() {
    RectF rectF = new RectF();
    rectF.set(this.cardViewHelper.getBackground().getBounds());
    return rectF;
  }
  
  public ColorStateList getCardBackgroundColor() {
    return this.cardViewHelper.getCardBackgroundColor();
  }
  
  public ColorStateList getCardForegroundColor() {
    return this.cardViewHelper.getCardForegroundColor();
  }
  
  float getCardViewRadius() {
    return getRadius();
  }
  
  public Drawable getCheckedIcon() {
    return this.cardViewHelper.getCheckedIcon();
  }
  
  public int getCheckedIconMargin() {
    return this.cardViewHelper.getCheckedIconMargin();
  }
  
  public int getCheckedIconSize() {
    return this.cardViewHelper.getCheckedIconSize();
  }
  
  public ColorStateList getCheckedIconTint() {
    return this.cardViewHelper.getCheckedIconTint();
  }
  
  public int getContentPaddingBottom() {
    return (this.cardViewHelper.getUserContentPadding()).bottom;
  }
  
  public int getContentPaddingLeft() {
    return (this.cardViewHelper.getUserContentPadding()).left;
  }
  
  public int getContentPaddingRight() {
    return (this.cardViewHelper.getUserContentPadding()).right;
  }
  
  public int getContentPaddingTop() {
    return (this.cardViewHelper.getUserContentPadding()).top;
  }
  
  public float getProgress() {
    return this.cardViewHelper.getProgress();
  }
  
  public float getRadius() {
    return this.cardViewHelper.getCornerRadius();
  }
  
  public ColorStateList getRippleColor() {
    return this.cardViewHelper.getRippleColor();
  }
  
  public ShapeAppearanceModel getShapeAppearanceModel() {
    return this.cardViewHelper.getShapeAppearanceModel();
  }
  
  @Deprecated
  public int getStrokeColor() {
    return this.cardViewHelper.getStrokeColor();
  }
  
  public ColorStateList getStrokeColorStateList() {
    return this.cardViewHelper.getStrokeColorStateList();
  }
  
  public int getStrokeWidth() {
    return this.cardViewHelper.getStrokeWidth();
  }
  
  public boolean isCheckable() {
    boolean bool;
    MaterialCardViewHelper materialCardViewHelper = this.cardViewHelper;
    if (materialCardViewHelper != null && materialCardViewHelper.isCheckable()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isChecked() {
    return this.checked;
  }
  
  public boolean isDragged() {
    return this.dragged;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    MaterialShapeUtils.setParentAbsoluteElevation((View)this, this.cardViewHelper.getBackground());
  }
  
  protected int[] onCreateDrawableState(int paramInt) {
    int[] arrayOfInt = super.onCreateDrawableState(paramInt + 3);
    if (isCheckable())
      mergeDrawableStates(arrayOfInt, CHECKABLE_STATE_SET); 
    if (isChecked())
      mergeDrawableStates(arrayOfInt, CHECKED_STATE_SET); 
    if (isDragged())
      mergeDrawableStates(arrayOfInt, DRAGGED_STATE_SET); 
    return arrayOfInt;
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    paramAccessibilityEvent.setClassName("androidx.cardview.widget.CardView");
    paramAccessibilityEvent.setChecked(isChecked());
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName("androidx.cardview.widget.CardView");
    paramAccessibilityNodeInfo.setCheckable(isCheckable());
    paramAccessibilityNodeInfo.setClickable(isClickable());
    paramAccessibilityNodeInfo.setChecked(isChecked());
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    this.cardViewHelper.onMeasure(getMeasuredWidth(), getMeasuredHeight());
  }
  
  void setAncestorContentPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.setContentPadding(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public void setBackground(Drawable paramDrawable) {
    setBackgroundDrawable(paramDrawable);
  }
  
  public void setBackgroundDrawable(Drawable paramDrawable) {
    if (this.isParentCardViewDoneInitializing) {
      if (!this.cardViewHelper.isBackgroundOverwritten()) {
        Log.i("MaterialCardView", "Setting a custom background is not supported.");
        this.cardViewHelper.setBackgroundOverwritten(true);
      } 
      super.setBackgroundDrawable(paramDrawable);
    } 
  }
  
  void setBackgroundInternal(Drawable paramDrawable) {
    super.setBackgroundDrawable(paramDrawable);
  }
  
  public void setCardBackgroundColor(int paramInt) {
    this.cardViewHelper.setCardBackgroundColor(ColorStateList.valueOf(paramInt));
  }
  
  public void setCardBackgroundColor(ColorStateList paramColorStateList) {
    this.cardViewHelper.setCardBackgroundColor(paramColorStateList);
  }
  
  public void setCardElevation(float paramFloat) {
    super.setCardElevation(paramFloat);
    this.cardViewHelper.updateElevation();
  }
  
  public void setCardForegroundColor(ColorStateList paramColorStateList) {
    this.cardViewHelper.setCardForegroundColor(paramColorStateList);
  }
  
  public void setCheckable(boolean paramBoolean) {
    this.cardViewHelper.setCheckable(paramBoolean);
  }
  
  public void setChecked(boolean paramBoolean) {
    if (this.checked != paramBoolean)
      toggle(); 
  }
  
  public void setCheckedIcon(Drawable paramDrawable) {
    this.cardViewHelper.setCheckedIcon(paramDrawable);
  }
  
  public void setCheckedIconMargin(int paramInt) {
    this.cardViewHelper.setCheckedIconMargin(paramInt);
  }
  
  public void setCheckedIconMarginResource(int paramInt) {
    if (paramInt != -1)
      this.cardViewHelper.setCheckedIconMargin(getResources().getDimensionPixelSize(paramInt)); 
  }
  
  public void setCheckedIconResource(int paramInt) {
    this.cardViewHelper.setCheckedIcon(AppCompatResources.getDrawable(getContext(), paramInt));
  }
  
  public void setCheckedIconSize(int paramInt) {
    this.cardViewHelper.setCheckedIconSize(paramInt);
  }
  
  public void setCheckedIconSizeResource(int paramInt) {
    if (paramInt != 0)
      this.cardViewHelper.setCheckedIconSize(getResources().getDimensionPixelSize(paramInt)); 
  }
  
  public void setCheckedIconTint(ColorStateList paramColorStateList) {
    this.cardViewHelper.setCheckedIconTint(paramColorStateList);
  }
  
  public void setClickable(boolean paramBoolean) {
    super.setClickable(paramBoolean);
    MaterialCardViewHelper materialCardViewHelper = this.cardViewHelper;
    if (materialCardViewHelper != null)
      materialCardViewHelper.updateClickable(); 
  }
  
  public void setContentPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.cardViewHelper.setUserContentPadding(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public void setDragged(boolean paramBoolean) {
    if (this.dragged != paramBoolean) {
      this.dragged = paramBoolean;
      refreshDrawableState();
      forceRippleRedrawIfNeeded();
      invalidate();
    } 
  }
  
  public void setMaxCardElevation(float paramFloat) {
    super.setMaxCardElevation(paramFloat);
    this.cardViewHelper.updateInsets();
  }
  
  public void setOnCheckedChangeListener(OnCheckedChangeListener paramOnCheckedChangeListener) {
    this.onCheckedChangeListener = paramOnCheckedChangeListener;
  }
  
  public void setPreventCornerOverlap(boolean paramBoolean) {
    super.setPreventCornerOverlap(paramBoolean);
    this.cardViewHelper.updateInsets();
    this.cardViewHelper.updateContentPadding();
  }
  
  public void setProgress(float paramFloat) {
    this.cardViewHelper.setProgress(paramFloat);
  }
  
  public void setRadius(float paramFloat) {
    super.setRadius(paramFloat);
    this.cardViewHelper.setCornerRadius(paramFloat);
  }
  
  public void setRippleColor(ColorStateList paramColorStateList) {
    this.cardViewHelper.setRippleColor(paramColorStateList);
  }
  
  public void setRippleColorResource(int paramInt) {
    this.cardViewHelper.setRippleColor(AppCompatResources.getColorStateList(getContext(), paramInt));
  }
  
  public void setShapeAppearanceModel(ShapeAppearanceModel paramShapeAppearanceModel) {
    if (Build.VERSION.SDK_INT >= 21)
      setClipToOutline(paramShapeAppearanceModel.isRoundRect(getBoundsAsRectF())); 
    this.cardViewHelper.setShapeAppearanceModel(paramShapeAppearanceModel);
  }
  
  public void setStrokeColor(int paramInt) {
    this.cardViewHelper.setStrokeColor(ColorStateList.valueOf(paramInt));
  }
  
  public void setStrokeColor(ColorStateList paramColorStateList) {
    this.cardViewHelper.setStrokeColor(paramColorStateList);
  }
  
  public void setStrokeWidth(int paramInt) {
    this.cardViewHelper.setStrokeWidth(paramInt);
  }
  
  public void setUseCompatPadding(boolean paramBoolean) {
    super.setUseCompatPadding(paramBoolean);
    this.cardViewHelper.updateInsets();
    this.cardViewHelper.updateContentPadding();
  }
  
  public void toggle() {
    if (isCheckable() && isEnabled()) {
      this.checked ^= 0x1;
      refreshDrawableState();
      forceRippleRedrawIfNeeded();
      OnCheckedChangeListener onCheckedChangeListener = this.onCheckedChangeListener;
      if (onCheckedChangeListener != null)
        onCheckedChangeListener.onCheckedChanged(this, this.checked); 
    } 
  }
  
  public static interface OnCheckedChangeListener {
    void onCheckedChanged(MaterialCardView param1MaterialCardView, boolean param1Boolean);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\card\MaterialCardView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */