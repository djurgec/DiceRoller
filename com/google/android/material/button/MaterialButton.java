package com.google.android.material.button;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class MaterialButton extends AppCompatButton implements Checkable, Shapeable {
  private static final int[] CHECKABLE_STATE_SET = new int[] { 16842911 };
  
  private static final int[] CHECKED_STATE_SET = new int[] { 16842912 };
  
  private static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_Button;
  
  public static final int ICON_GRAVITY_END = 3;
  
  public static final int ICON_GRAVITY_START = 1;
  
  public static final int ICON_GRAVITY_TEXT_END = 4;
  
  public static final int ICON_GRAVITY_TEXT_START = 2;
  
  public static final int ICON_GRAVITY_TEXT_TOP = 32;
  
  public static final int ICON_GRAVITY_TOP = 16;
  
  private static final String LOG_TAG = "MaterialButton";
  
  private boolean broadcasting;
  
  private boolean checked;
  
  private Drawable icon;
  
  private int iconGravity;
  
  private int iconLeft;
  
  private int iconPadding;
  
  private int iconSize;
  
  private ColorStateList iconTint;
  
  private PorterDuff.Mode iconTintMode;
  
  private int iconTop;
  
  private final MaterialButtonHelper materialButtonHelper;
  
  private final LinkedHashSet<OnCheckedChangeListener> onCheckedChangeListeners = new LinkedHashSet<>();
  
  private OnPressedChangeListener onPressedChangeListenerInternal;
  
  public MaterialButton(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public MaterialButton(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.materialButtonStyle);
  }
  
  public MaterialButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(MaterialThemeOverlay.wrap(paramContext, paramAttributeSet, paramInt, i), paramAttributeSet, paramInt);
    boolean bool = false;
    this.checked = false;
    this.broadcasting = false;
    Context context = getContext();
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(context, paramAttributeSet, R.styleable.MaterialButton, paramInt, i, new int[0]);
    this.iconPadding = typedArray.getDimensionPixelSize(R.styleable.MaterialButton_iconPadding, 0);
    this.iconTintMode = ViewUtils.parseTintMode(typedArray.getInt(R.styleable.MaterialButton_iconTintMode, -1), PorterDuff.Mode.SRC_IN);
    this.iconTint = MaterialResources.getColorStateList(getContext(), typedArray, R.styleable.MaterialButton_iconTint);
    this.icon = MaterialResources.getDrawable(getContext(), typedArray, R.styleable.MaterialButton_icon);
    this.iconGravity = typedArray.getInteger(R.styleable.MaterialButton_iconGravity, 1);
    this.iconSize = typedArray.getDimensionPixelSize(R.styleable.MaterialButton_iconSize, 0);
    MaterialButtonHelper materialButtonHelper = new MaterialButtonHelper(this, ShapeAppearanceModel.builder(context, paramAttributeSet, paramInt, i).build());
    this.materialButtonHelper = materialButtonHelper;
    materialButtonHelper.loadFromAttributes(typedArray);
    typedArray.recycle();
    setCompoundDrawablePadding(this.iconPadding);
    if (this.icon != null)
      bool = true; 
    updateIcon(bool);
  }
  
  private String getA11yClassName() {
    Class<Button> clazz;
    if (isCheckable()) {
      Class<CompoundButton> clazz1 = CompoundButton.class;
    } else {
      clazz = Button.class;
    } 
    return clazz.getName();
  }
  
  private int getTextHeight() {
    TextPaint textPaint = getPaint();
    String str2 = getText().toString();
    String str1 = str2;
    if (getTransformationMethod() != null)
      str1 = getTransformationMethod().getTransformation(str2, (View)this).toString(); 
    Rect rect = new Rect();
    textPaint.getTextBounds(str1, 0, str1.length(), rect);
    return Math.min(rect.height(), getLayout().getHeight());
  }
  
  private int getTextWidth() {
    TextPaint textPaint = getPaint();
    String str2 = getText().toString();
    String str1 = str2;
    if (getTransformationMethod() != null)
      str1 = getTransformationMethod().getTransformation(str2, (View)this).toString(); 
    return Math.min((int)textPaint.measureText(str1), getLayout().getEllipsizedWidth());
  }
  
  private boolean isIconEnd() {
    int i = this.iconGravity;
    return (i == 3 || i == 4);
  }
  
  private boolean isIconStart() {
    int i = this.iconGravity;
    boolean bool2 = true;
    boolean bool1 = bool2;
    if (i != 1)
      if (i == 2) {
        bool1 = bool2;
      } else {
        bool1 = false;
      }  
    return bool1;
  }
  
  private boolean isIconTop() {
    int i = this.iconGravity;
    return (i == 16 || i == 32);
  }
  
  private boolean isLayoutRTL() {
    int i = ViewCompat.getLayoutDirection((View)this);
    boolean bool = true;
    if (i != 1)
      bool = false; 
    return bool;
  }
  
  private boolean isUsingOriginalBackground() {
    boolean bool;
    MaterialButtonHelper materialButtonHelper = this.materialButtonHelper;
    if (materialButtonHelper != null && !materialButtonHelper.isBackgroundOverwritten()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void resetIconDrawable() {
    if (isIconStart()) {
      TextViewCompat.setCompoundDrawablesRelative((TextView)this, this.icon, null, null, null);
    } else if (isIconEnd()) {
      TextViewCompat.setCompoundDrawablesRelative((TextView)this, null, null, this.icon, null);
    } else if (isIconTop()) {
      TextViewCompat.setCompoundDrawablesRelative((TextView)this, null, this.icon, null, null);
    } 
  }
  
  private void updateIcon(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield icon : Landroid/graphics/drawable/Drawable;
    //   4: astore #6
    //   6: aload #6
    //   8: ifnull -> 131
    //   11: aload #6
    //   13: invokestatic wrap : (Landroid/graphics/drawable/Drawable;)Landroid/graphics/drawable/Drawable;
    //   16: invokevirtual mutate : ()Landroid/graphics/drawable/Drawable;
    //   19: astore #6
    //   21: aload_0
    //   22: aload #6
    //   24: putfield icon : Landroid/graphics/drawable/Drawable;
    //   27: aload #6
    //   29: aload_0
    //   30: getfield iconTint : Landroid/content/res/ColorStateList;
    //   33: invokestatic setTintList : (Landroid/graphics/drawable/Drawable;Landroid/content/res/ColorStateList;)V
    //   36: aload_0
    //   37: getfield iconTintMode : Landroid/graphics/PorterDuff$Mode;
    //   40: astore #6
    //   42: aload #6
    //   44: ifnull -> 56
    //   47: aload_0
    //   48: getfield icon : Landroid/graphics/drawable/Drawable;
    //   51: aload #6
    //   53: invokestatic setTintMode : (Landroid/graphics/drawable/Drawable;Landroid/graphics/PorterDuff$Mode;)V
    //   56: aload_0
    //   57: getfield iconSize : I
    //   60: istore_2
    //   61: iload_2
    //   62: ifeq -> 68
    //   65: goto -> 76
    //   68: aload_0
    //   69: getfield icon : Landroid/graphics/drawable/Drawable;
    //   72: invokevirtual getIntrinsicWidth : ()I
    //   75: istore_2
    //   76: aload_0
    //   77: getfield iconSize : I
    //   80: istore_3
    //   81: iload_3
    //   82: ifeq -> 88
    //   85: goto -> 96
    //   88: aload_0
    //   89: getfield icon : Landroid/graphics/drawable/Drawable;
    //   92: invokevirtual getIntrinsicHeight : ()I
    //   95: istore_3
    //   96: aload_0
    //   97: getfield icon : Landroid/graphics/drawable/Drawable;
    //   100: astore #6
    //   102: aload_0
    //   103: getfield iconLeft : I
    //   106: istore #4
    //   108: aload_0
    //   109: getfield iconTop : I
    //   112: istore #5
    //   114: aload #6
    //   116: iload #4
    //   118: iload #5
    //   120: iload #4
    //   122: iload_2
    //   123: iadd
    //   124: iload #5
    //   126: iload_3
    //   127: iadd
    //   128: invokevirtual setBounds : (IIII)V
    //   131: iload_1
    //   132: ifeq -> 140
    //   135: aload_0
    //   136: invokespecial resetIconDrawable : ()V
    //   139: return
    //   140: aload_0
    //   141: invokestatic getCompoundDrawablesRelative : (Landroid/widget/TextView;)[Landroid/graphics/drawable/Drawable;
    //   144: astore #8
    //   146: iconst_0
    //   147: istore_3
    //   148: aload #8
    //   150: iconst_0
    //   151: aaload
    //   152: astore #7
    //   154: aload #8
    //   156: iconst_1
    //   157: aaload
    //   158: astore #6
    //   160: aload #8
    //   162: iconst_2
    //   163: aaload
    //   164: astore #8
    //   166: aload_0
    //   167: invokespecial isIconStart : ()Z
    //   170: ifeq -> 182
    //   173: aload #7
    //   175: aload_0
    //   176: getfield icon : Landroid/graphics/drawable/Drawable;
    //   179: if_acmpne -> 218
    //   182: aload_0
    //   183: invokespecial isIconEnd : ()Z
    //   186: ifeq -> 198
    //   189: aload #8
    //   191: aload_0
    //   192: getfield icon : Landroid/graphics/drawable/Drawable;
    //   195: if_acmpne -> 218
    //   198: iload_3
    //   199: istore_2
    //   200: aload_0
    //   201: invokespecial isIconTop : ()Z
    //   204: ifeq -> 220
    //   207: iload_3
    //   208: istore_2
    //   209: aload #6
    //   211: aload_0
    //   212: getfield icon : Landroid/graphics/drawable/Drawable;
    //   215: if_acmpeq -> 220
    //   218: iconst_1
    //   219: istore_2
    //   220: iload_2
    //   221: ifeq -> 228
    //   224: aload_0
    //   225: invokespecial resetIconDrawable : ()V
    //   228: return
  }
  
  private void updateIconPosition(int paramInt1, int paramInt2) {
    if (this.icon == null || getLayout() == null)
      return; 
    if (isIconStart() || isIconEnd()) {
      this.iconTop = 0;
      paramInt2 = this.iconGravity;
      boolean bool1 = true;
      if (paramInt2 == 1 || paramInt2 == 3) {
        this.iconLeft = 0;
        updateIcon(false);
        return;
      } 
      int i = this.iconSize;
      paramInt2 = i;
      if (i == 0)
        paramInt2 = this.icon.getIntrinsicWidth(); 
      paramInt2 = (paramInt1 - getTextWidth() - ViewCompat.getPaddingEnd((View)this) - paramInt2 - this.iconPadding - ViewCompat.getPaddingStart((View)this)) / 2;
      boolean bool2 = isLayoutRTL();
      if (this.iconGravity != 4)
        bool1 = false; 
      paramInt1 = paramInt2;
      if (bool2 != bool1)
        paramInt1 = -paramInt2; 
      if (this.iconLeft != paramInt1) {
        this.iconLeft = paramInt1;
        updateIcon(false);
      } 
      return;
    } 
    if (isIconTop()) {
      this.iconLeft = 0;
      if (this.iconGravity == 16) {
        this.iconTop = 0;
        updateIcon(false);
        return;
      } 
      int i = this.iconSize;
      paramInt1 = i;
      if (i == 0)
        paramInt1 = this.icon.getIntrinsicHeight(); 
      paramInt1 = (paramInt2 - getTextHeight() - getPaddingTop() - paramInt1 - this.iconPadding - getPaddingBottom()) / 2;
      if (this.iconTop != paramInt1) {
        this.iconTop = paramInt1;
        updateIcon(false);
      } 
    } 
  }
  
  public void addOnCheckedChangeListener(OnCheckedChangeListener paramOnCheckedChangeListener) {
    this.onCheckedChangeListeners.add(paramOnCheckedChangeListener);
  }
  
  public void clearOnCheckedChangeListeners() {
    this.onCheckedChangeListeners.clear();
  }
  
  public ColorStateList getBackgroundTintList() {
    return getSupportBackgroundTintList();
  }
  
  public PorterDuff.Mode getBackgroundTintMode() {
    return getSupportBackgroundTintMode();
  }
  
  public int getCornerRadius() {
    boolean bool;
    if (isUsingOriginalBackground()) {
      bool = this.materialButtonHelper.getCornerRadius();
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public Drawable getIcon() {
    return this.icon;
  }
  
  public int getIconGravity() {
    return this.iconGravity;
  }
  
  public int getIconPadding() {
    return this.iconPadding;
  }
  
  public int getIconSize() {
    return this.iconSize;
  }
  
  public ColorStateList getIconTint() {
    return this.iconTint;
  }
  
  public PorterDuff.Mode getIconTintMode() {
    return this.iconTintMode;
  }
  
  public int getInsetBottom() {
    return this.materialButtonHelper.getInsetBottom();
  }
  
  public int getInsetTop() {
    return this.materialButtonHelper.getInsetTop();
  }
  
  public ColorStateList getRippleColor() {
    ColorStateList colorStateList;
    if (isUsingOriginalBackground()) {
      colorStateList = this.materialButtonHelper.getRippleColor();
    } else {
      colorStateList = null;
    } 
    return colorStateList;
  }
  
  public ShapeAppearanceModel getShapeAppearanceModel() {
    if (isUsingOriginalBackground())
      return this.materialButtonHelper.getShapeAppearanceModel(); 
    throw new IllegalStateException("Attempted to get ShapeAppearanceModel from a MaterialButton which has an overwritten background.");
  }
  
  public ColorStateList getStrokeColor() {
    ColorStateList colorStateList;
    if (isUsingOriginalBackground()) {
      colorStateList = this.materialButtonHelper.getStrokeColor();
    } else {
      colorStateList = null;
    } 
    return colorStateList;
  }
  
  public int getStrokeWidth() {
    boolean bool;
    if (isUsingOriginalBackground()) {
      bool = this.materialButtonHelper.getStrokeWidth();
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public ColorStateList getSupportBackgroundTintList() {
    return isUsingOriginalBackground() ? this.materialButtonHelper.getSupportBackgroundTintList() : super.getSupportBackgroundTintList();
  }
  
  public PorterDuff.Mode getSupportBackgroundTintMode() {
    return isUsingOriginalBackground() ? this.materialButtonHelper.getSupportBackgroundTintMode() : super.getSupportBackgroundTintMode();
  }
  
  public boolean isCheckable() {
    boolean bool;
    MaterialButtonHelper materialButtonHelper = this.materialButtonHelper;
    if (materialButtonHelper != null && materialButtonHelper.isCheckable()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isChecked() {
    return this.checked;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (isUsingOriginalBackground())
      MaterialShapeUtils.setParentAbsoluteElevation((View)this, this.materialButtonHelper.getMaterialShapeDrawable()); 
  }
  
  protected int[] onCreateDrawableState(int paramInt) {
    int[] arrayOfInt = super.onCreateDrawableState(paramInt + 2);
    if (isCheckable())
      mergeDrawableStates(arrayOfInt, CHECKABLE_STATE_SET); 
    if (isChecked())
      mergeDrawableStates(arrayOfInt, CHECKED_STATE_SET); 
    return arrayOfInt;
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    paramAccessibilityEvent.setClassName(getA11yClassName());
    paramAccessibilityEvent.setChecked(isChecked());
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName(getA11yClassName());
    paramAccessibilityNodeInfo.setCheckable(isCheckable());
    paramAccessibilityNodeInfo.setChecked(isChecked());
    paramAccessibilityNodeInfo.setClickable(isClickable());
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (Build.VERSION.SDK_INT == 21) {
      MaterialButtonHelper materialButtonHelper = this.materialButtonHelper;
      if (materialButtonHelper != null)
        materialButtonHelper.updateMaskBounds(paramInt4 - paramInt2, paramInt3 - paramInt1); 
    } 
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    setChecked(savedState.checked);
  }
  
  public Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.checked = this.checked;
    return (Parcelable)savedState;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    updateIconPosition(paramInt1, paramInt2);
  }
  
  protected void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
    super.onTextChanged(paramCharSequence, paramInt1, paramInt2, paramInt3);
    updateIconPosition(getMeasuredWidth(), getMeasuredHeight());
  }
  
  public boolean performClick() {
    toggle();
    return super.performClick();
  }
  
  public void removeOnCheckedChangeListener(OnCheckedChangeListener paramOnCheckedChangeListener) {
    this.onCheckedChangeListeners.remove(paramOnCheckedChangeListener);
  }
  
  public void setBackground(Drawable paramDrawable) {
    setBackgroundDrawable(paramDrawable);
  }
  
  public void setBackgroundColor(int paramInt) {
    if (isUsingOriginalBackground()) {
      this.materialButtonHelper.setBackgroundColor(paramInt);
    } else {
      super.setBackgroundColor(paramInt);
    } 
  }
  
  public void setBackgroundDrawable(Drawable paramDrawable) {
    if (isUsingOriginalBackground()) {
      if (paramDrawable != getBackground()) {
        Log.w("MaterialButton", "MaterialButton manages its own background to control elevation, shape, color and states. Consider using backgroundTint, shapeAppearance and other attributes where available. A custom background will ignore these attributes and you should consider handling interaction states such as pressed, focused and disabled");
        this.materialButtonHelper.setBackgroundOverwritten();
        super.setBackgroundDrawable(paramDrawable);
      } else {
        getBackground().setState(paramDrawable.getState());
      } 
    } else {
      super.setBackgroundDrawable(paramDrawable);
    } 
  }
  
  public void setBackgroundResource(int paramInt) {
    Drawable drawable = null;
    if (paramInt != 0)
      drawable = AppCompatResources.getDrawable(getContext(), paramInt); 
    setBackgroundDrawable(drawable);
  }
  
  public void setBackgroundTintList(ColorStateList paramColorStateList) {
    setSupportBackgroundTintList(paramColorStateList);
  }
  
  public void setBackgroundTintMode(PorterDuff.Mode paramMode) {
    setSupportBackgroundTintMode(paramMode);
  }
  
  public void setCheckable(boolean paramBoolean) {
    if (isUsingOriginalBackground())
      this.materialButtonHelper.setCheckable(paramBoolean); 
  }
  
  public void setChecked(boolean paramBoolean) {
    if (isCheckable() && isEnabled() && this.checked != paramBoolean) {
      this.checked = paramBoolean;
      refreshDrawableState();
      if (this.broadcasting)
        return; 
      this.broadcasting = true;
      Iterator<OnCheckedChangeListener> iterator = this.onCheckedChangeListeners.iterator();
      while (iterator.hasNext())
        ((OnCheckedChangeListener)iterator.next()).onCheckedChanged(this, this.checked); 
      this.broadcasting = false;
    } 
  }
  
  public void setCornerRadius(int paramInt) {
    if (isUsingOriginalBackground())
      this.materialButtonHelper.setCornerRadius(paramInt); 
  }
  
  public void setCornerRadiusResource(int paramInt) {
    if (isUsingOriginalBackground())
      setCornerRadius(getResources().getDimensionPixelSize(paramInt)); 
  }
  
  public void setElevation(float paramFloat) {
    super.setElevation(paramFloat);
    if (isUsingOriginalBackground())
      this.materialButtonHelper.getMaterialShapeDrawable().setElevation(paramFloat); 
  }
  
  public void setIcon(Drawable paramDrawable) {
    if (this.icon != paramDrawable) {
      this.icon = paramDrawable;
      updateIcon(true);
      updateIconPosition(getMeasuredWidth(), getMeasuredHeight());
    } 
  }
  
  public void setIconGravity(int paramInt) {
    if (this.iconGravity != paramInt) {
      this.iconGravity = paramInt;
      updateIconPosition(getMeasuredWidth(), getMeasuredHeight());
    } 
  }
  
  public void setIconPadding(int paramInt) {
    if (this.iconPadding != paramInt) {
      this.iconPadding = paramInt;
      setCompoundDrawablePadding(paramInt);
    } 
  }
  
  public void setIconResource(int paramInt) {
    Drawable drawable = null;
    if (paramInt != 0)
      drawable = AppCompatResources.getDrawable(getContext(), paramInt); 
    setIcon(drawable);
  }
  
  public void setIconSize(int paramInt) {
    if (paramInt >= 0) {
      if (this.iconSize != paramInt) {
        this.iconSize = paramInt;
        updateIcon(true);
      } 
      return;
    } 
    throw new IllegalArgumentException("iconSize cannot be less than 0");
  }
  
  public void setIconTint(ColorStateList paramColorStateList) {
    if (this.iconTint != paramColorStateList) {
      this.iconTint = paramColorStateList;
      updateIcon(false);
    } 
  }
  
  public void setIconTintMode(PorterDuff.Mode paramMode) {
    if (this.iconTintMode != paramMode) {
      this.iconTintMode = paramMode;
      updateIcon(false);
    } 
  }
  
  public void setIconTintResource(int paramInt) {
    setIconTint(AppCompatResources.getColorStateList(getContext(), paramInt));
  }
  
  public void setInsetBottom(int paramInt) {
    this.materialButtonHelper.setInsetBottom(paramInt);
  }
  
  public void setInsetTop(int paramInt) {
    this.materialButtonHelper.setInsetTop(paramInt);
  }
  
  void setInternalBackground(Drawable paramDrawable) {
    super.setBackgroundDrawable(paramDrawable);
  }
  
  void setOnPressedChangeListenerInternal(OnPressedChangeListener paramOnPressedChangeListener) {
    this.onPressedChangeListenerInternal = paramOnPressedChangeListener;
  }
  
  public void setPressed(boolean paramBoolean) {
    OnPressedChangeListener onPressedChangeListener = this.onPressedChangeListenerInternal;
    if (onPressedChangeListener != null)
      onPressedChangeListener.onPressedChanged(this, paramBoolean); 
    super.setPressed(paramBoolean);
  }
  
  public void setRippleColor(ColorStateList paramColorStateList) {
    if (isUsingOriginalBackground())
      this.materialButtonHelper.setRippleColor(paramColorStateList); 
  }
  
  public void setRippleColorResource(int paramInt) {
    if (isUsingOriginalBackground())
      setRippleColor(AppCompatResources.getColorStateList(getContext(), paramInt)); 
  }
  
  public void setShapeAppearanceModel(ShapeAppearanceModel paramShapeAppearanceModel) {
    if (isUsingOriginalBackground()) {
      this.materialButtonHelper.setShapeAppearanceModel(paramShapeAppearanceModel);
      return;
    } 
    throw new IllegalStateException("Attempted to set ShapeAppearanceModel on a MaterialButton which has an overwritten background.");
  }
  
  void setShouldDrawSurfaceColorStroke(boolean paramBoolean) {
    if (isUsingOriginalBackground())
      this.materialButtonHelper.setShouldDrawSurfaceColorStroke(paramBoolean); 
  }
  
  public void setStrokeColor(ColorStateList paramColorStateList) {
    if (isUsingOriginalBackground())
      this.materialButtonHelper.setStrokeColor(paramColorStateList); 
  }
  
  public void setStrokeColorResource(int paramInt) {
    if (isUsingOriginalBackground())
      setStrokeColor(AppCompatResources.getColorStateList(getContext(), paramInt)); 
  }
  
  public void setStrokeWidth(int paramInt) {
    if (isUsingOriginalBackground())
      this.materialButtonHelper.setStrokeWidth(paramInt); 
  }
  
  public void setStrokeWidthResource(int paramInt) {
    if (isUsingOriginalBackground())
      setStrokeWidth(getResources().getDimensionPixelSize(paramInt)); 
  }
  
  public void setSupportBackgroundTintList(ColorStateList paramColorStateList) {
    if (isUsingOriginalBackground()) {
      this.materialButtonHelper.setSupportBackgroundTintList(paramColorStateList);
    } else {
      super.setSupportBackgroundTintList(paramColorStateList);
    } 
  }
  
  public void setSupportBackgroundTintMode(PorterDuff.Mode paramMode) {
    if (isUsingOriginalBackground()) {
      this.materialButtonHelper.setSupportBackgroundTintMode(paramMode);
    } else {
      super.setSupportBackgroundTintMode(paramMode);
    } 
  }
  
  public void toggle() {
    setChecked(this.checked ^ true);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface IconGravity {}
  
  public static interface OnCheckedChangeListener {
    void onCheckedChanged(MaterialButton param1MaterialButton, boolean param1Boolean);
  }
  
  static interface OnPressedChangeListener {
    void onPressedChanged(MaterialButton param1MaterialButton, boolean param1Boolean);
  }
  
  static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public MaterialButton.SavedState createFromParcel(Parcel param2Parcel) {
          return new MaterialButton.SavedState(param2Parcel, null);
        }
        
        public MaterialButton.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new MaterialButton.SavedState(param2Parcel, param2ClassLoader);
        }
        
        public MaterialButton.SavedState[] newArray(int param2Int) {
          return new MaterialButton.SavedState[param2Int];
        }
      };
    
    boolean checked;
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      if (param1ClassLoader == null)
        getClass().getClassLoader(); 
      readFromParcel(param1Parcel);
    }
    
    public SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    private void readFromParcel(Parcel param1Parcel) {
      int i = param1Parcel.readInt();
      boolean bool = true;
      if (i != 1)
        bool = false; 
      this.checked = bool;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.checked);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public MaterialButton.SavedState createFromParcel(Parcel param1Parcel) {
      return new MaterialButton.SavedState(param1Parcel, null);
    }
    
    public MaterialButton.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new MaterialButton.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public MaterialButton.SavedState[] newArray(int param1Int) {
      return new MaterialButton.SavedState[param1Int];
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\button\MaterialButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */