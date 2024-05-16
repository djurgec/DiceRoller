package com.google.android.material.chip;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import com.google.android.material.R;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.resources.TextAppearanceFontCallback;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class Chip extends AppCompatCheckBox implements ChipDrawable.Delegate, Shapeable {
  private static final String BUTTON_ACCESSIBILITY_CLASS_NAME = "android.widget.Button";
  
  private static final int[] CHECKABLE_STATE_SET;
  
  private static final int CHIP_BODY_VIRTUAL_ID = 0;
  
  private static final int CLOSE_ICON_VIRTUAL_ID = 1;
  
  private static final String COMPOUND_BUTTON_ACCESSIBILITY_CLASS_NAME = "android.widget.CompoundButton";
  
  private static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_Chip_Action;
  
  private static final Rect EMPTY_BOUNDS = new Rect();
  
  private static final String GENERIC_VIEW_ACCESSIBILITY_CLASS_NAME = "android.view.View";
  
  private static final int MIN_TOUCH_TARGET_DP = 48;
  
  private static final String NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android";
  
  private static final int[] SELECTED_STATE = new int[] { 16842913 };
  
  private static final String TAG = "Chip";
  
  private ChipDrawable chipDrawable;
  
  private boolean closeIconFocused;
  
  private boolean closeIconHovered;
  
  private boolean closeIconPressed;
  
  private boolean deferredCheckedValue;
  
  private boolean ensureMinTouchTargetSize;
  
  private final TextAppearanceFontCallback fontCallback = new TextAppearanceFontCallback() {
      final Chip this$0;
      
      public void onFontRetrievalFailed(int param1Int) {}
      
      public void onFontRetrieved(Typeface param1Typeface, boolean param1Boolean) {
        CharSequence charSequence;
        Chip chip = Chip.this;
        if (chip.chipDrawable.shouldDrawText()) {
          charSequence = Chip.this.chipDrawable.getText();
        } else {
          charSequence = Chip.this.getText();
        } 
        chip.setText(charSequence);
        Chip.this.requestLayout();
        Chip.this.invalidate();
      }
    };
  
  private InsetDrawable insetBackgroundDrawable;
  
  private int lastLayoutDirection;
  
  private int minTouchTargetSize;
  
  private CompoundButton.OnCheckedChangeListener onCheckedChangeListenerInternal;
  
  private View.OnClickListener onCloseIconClickListener;
  
  private final Rect rect = new Rect();
  
  private final RectF rectF = new RectF();
  
  private RippleDrawable ripple;
  
  private final ChipTouchHelper touchHelper;
  
  static {
    CHECKABLE_STATE_SET = new int[] { 16842911 };
  }
  
  public Chip(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public Chip(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.chipStyle);
  }
  
  public Chip(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(MaterialThemeOverlay.wrap(paramContext, paramAttributeSet, paramInt, i), paramAttributeSet, paramInt);
    paramContext = getContext();
    validateAttributes(paramAttributeSet);
    ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(paramContext, paramAttributeSet, paramInt, i);
    initMinTouchTarget(paramContext, paramAttributeSet, paramInt);
    setChipDrawable(chipDrawable);
    chipDrawable.setElevation(ViewCompat.getElevation((View)this));
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.Chip, paramInt, i, new int[0]);
    if (Build.VERSION.SDK_INT < 23)
      setTextColor(MaterialResources.getColorStateList(paramContext, typedArray, R.styleable.Chip_android_textColor)); 
    boolean bool = typedArray.hasValue(R.styleable.Chip_shapeAppearance);
    typedArray.recycle();
    this.touchHelper = new ChipTouchHelper(this);
    updateAccessibilityDelegate();
    if (!bool)
      initOutlineProvider(); 
    setChecked(this.deferredCheckedValue);
    setText(chipDrawable.getText());
    setEllipsize(chipDrawable.getEllipsize());
    updateTextPaintDrawState();
    if (!this.chipDrawable.shouldDrawText()) {
      setLines(1);
      setHorizontallyScrolling(true);
    } 
    setGravity(8388627);
    updatePaddingInternal();
    if (shouldEnsureMinTouchTargetSize())
      setMinHeight(this.minTouchTargetSize); 
    this.lastLayoutDirection = ViewCompat.getLayoutDirection((View)this);
  }
  
  private void applyChipDrawable(ChipDrawable paramChipDrawable) {
    paramChipDrawable.setDelegate(this);
  }
  
  private int[] createCloseIconDrawableState() {
    int j = 0;
    if (isEnabled())
      j = 0 + 1; 
    int i = j;
    if (this.closeIconFocused)
      i = j + 1; 
    j = i;
    if (this.closeIconHovered)
      j = i + 1; 
    i = j;
    if (this.closeIconPressed)
      i = j + 1; 
    j = i;
    if (isChecked())
      j = i + 1; 
    int[] arrayOfInt = new int[j];
    i = 0;
    if (isEnabled()) {
      arrayOfInt[0] = 16842910;
      i = 0 + 1;
    } 
    j = i;
    if (this.closeIconFocused) {
      arrayOfInt[i] = 16842908;
      j = i + 1;
    } 
    i = j;
    if (this.closeIconHovered) {
      arrayOfInt[j] = 16843623;
      i = j + 1;
    } 
    j = i;
    if (this.closeIconPressed) {
      arrayOfInt[i] = 16842919;
      j = i + 1;
    } 
    if (isChecked())
      arrayOfInt[j] = 16842913; 
    return arrayOfInt;
  }
  
  private void ensureChipDrawableHasCallback() {
    if (getBackgroundDrawable() == this.insetBackgroundDrawable && this.chipDrawable.getCallback() == null)
      this.chipDrawable.setCallback((Drawable.Callback)this.insetBackgroundDrawable); 
  }
  
  private RectF getCloseIconTouchBounds() {
    this.rectF.setEmpty();
    if (hasCloseIcon() && this.onCloseIconClickListener != null)
      this.chipDrawable.getCloseIconTouchBounds(this.rectF); 
    return this.rectF;
  }
  
  private Rect getCloseIconTouchBoundsInt() {
    RectF rectF = getCloseIconTouchBounds();
    this.rect.set((int)rectF.left, (int)rectF.top, (int)rectF.right, (int)rectF.bottom);
    return this.rect;
  }
  
  private TextAppearance getTextAppearance() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      TextAppearance textAppearance = chipDrawable.getTextAppearance();
    } else {
      chipDrawable = null;
    } 
    return (TextAppearance)chipDrawable;
  }
  
  private boolean handleAccessibilityExit(MotionEvent paramMotionEvent) {
    if (paramMotionEvent.getAction() == 10)
      try {
        Field field = ExploreByTouchHelper.class.getDeclaredField("mHoveredVirtualViewId");
        field.setAccessible(true);
        if (((Integer)field.get(this.touchHelper)).intValue() != Integer.MIN_VALUE) {
          Method method = ExploreByTouchHelper.class.getDeclaredMethod("updateHoveredVirtualView", new Class[] { int.class });
          method.setAccessible(true);
          method.invoke(this.touchHelper, new Object[] { Integer.valueOf(-2147483648) });
          return true;
        } 
      } catch (NoSuchMethodException noSuchMethodException) {
        Log.e("Chip", "Unable to send Accessibility Exit event", noSuchMethodException);
      } catch (IllegalAccessException illegalAccessException) {
        Log.e("Chip", "Unable to send Accessibility Exit event", illegalAccessException);
      } catch (InvocationTargetException invocationTargetException) {
        Log.e("Chip", "Unable to send Accessibility Exit event", invocationTargetException);
      } catch (NoSuchFieldException noSuchFieldException) {
        Log.e("Chip", "Unable to send Accessibility Exit event", noSuchFieldException);
      }  
    return false;
  }
  
  private boolean hasCloseIcon() {
    boolean bool;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null && chipDrawable.getCloseIcon() != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void initMinTouchTarget(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.Chip, paramInt, DEF_STYLE_RES, new int[0]);
    this.ensureMinTouchTargetSize = typedArray.getBoolean(R.styleable.Chip_ensureMinTouchTargetSize, false);
    float f = (float)Math.ceil(ViewUtils.dpToPx(getContext(), 48));
    this.minTouchTargetSize = (int)Math.ceil(typedArray.getDimension(R.styleable.Chip_chipMinTouchTargetSize, f));
    typedArray.recycle();
  }
  
  private void initOutlineProvider() {
    if (Build.VERSION.SDK_INT >= 21)
      setOutlineProvider(new ViewOutlineProvider() {
            final Chip this$0;
            
            public void getOutline(View param1View, Outline param1Outline) {
              if (Chip.this.chipDrawable != null) {
                Chip.this.chipDrawable.getOutline(param1Outline);
              } else {
                param1Outline.setAlpha(0.0F);
              } 
            }
          }); 
  }
  
  private void insetChipBackgroundDrawable(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.insetBackgroundDrawable = new InsetDrawable((Drawable)this.chipDrawable, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  private void removeBackgroundInset() {
    if (this.insetBackgroundDrawable != null) {
      this.insetBackgroundDrawable = null;
      setMinWidth(0);
      setMinHeight((int)getChipMinHeight());
      updateBackgroundDrawable();
    } 
  }
  
  private void setCloseIconHovered(boolean paramBoolean) {
    if (this.closeIconHovered != paramBoolean) {
      this.closeIconHovered = paramBoolean;
      refreshDrawableState();
    } 
  }
  
  private void setCloseIconPressed(boolean paramBoolean) {
    if (this.closeIconPressed != paramBoolean) {
      this.closeIconPressed = paramBoolean;
      refreshDrawableState();
    } 
  }
  
  private void unapplyChipDrawable(ChipDrawable paramChipDrawable) {
    if (paramChipDrawable != null)
      paramChipDrawable.setDelegate((ChipDrawable.Delegate)null); 
  }
  
  private void updateAccessibilityDelegate() {
    if (hasCloseIcon() && isCloseIconVisible() && this.onCloseIconClickListener != null) {
      ViewCompat.setAccessibilityDelegate((View)this, (AccessibilityDelegateCompat)this.touchHelper);
    } else {
      ViewCompat.setAccessibilityDelegate((View)this, null);
    } 
  }
  
  private void updateBackgroundDrawable() {
    if (RippleUtils.USE_FRAMEWORK_RIPPLE) {
      updateFrameworkRippleBackground();
    } else {
      this.chipDrawable.setUseCompatRipple(true);
      ViewCompat.setBackground((View)this, getBackgroundDrawable());
      updatePaddingInternal();
      ensureChipDrawableHasCallback();
    } 
  }
  
  private void updateFrameworkRippleBackground() {
    this.ripple = new RippleDrawable(RippleUtils.sanitizeRippleDrawableColor(this.chipDrawable.getRippleColor()), getBackgroundDrawable(), null);
    this.chipDrawable.setUseCompatRipple(false);
    ViewCompat.setBackground((View)this, (Drawable)this.ripple);
    updatePaddingInternal();
  }
  
  private void updatePaddingInternal() {
    if (!TextUtils.isEmpty(getText())) {
      ChipDrawable chipDrawable = this.chipDrawable;
      if (chipDrawable != null) {
        int k = (int)(chipDrawable.getChipEndPadding() + this.chipDrawable.getTextEndPadding() + this.chipDrawable.calculateCloseIconWidth());
        int m = (int)(this.chipDrawable.getChipStartPadding() + this.chipDrawable.getTextStartPadding() + this.chipDrawable.calculateChipIconWidth());
        int j = k;
        int i = m;
        if (this.insetBackgroundDrawable != null) {
          Rect rect = new Rect();
          this.insetBackgroundDrawable.getPadding(rect);
          i = m + rect.left;
          j = k + rect.right;
        } 
        ViewCompat.setPaddingRelative((View)this, i, getPaddingTop(), j, getPaddingBottom());
        return;
      } 
    } 
  }
  
  private void updateTextPaintDrawState() {
    TextPaint textPaint = getPaint();
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      textPaint.drawableState = chipDrawable.getState(); 
    TextAppearance textAppearance = getTextAppearance();
    if (textAppearance != null)
      textAppearance.updateDrawState(getContext(), textPaint, this.fontCallback); 
  }
  
  private void validateAttributes(AttributeSet paramAttributeSet) {
    if (paramAttributeSet == null)
      return; 
    if (paramAttributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "background") != null)
      Log.w("Chip", "Do not set the background; Chip manages its own background drawable."); 
    if (paramAttributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableLeft") == null) {
      if (paramAttributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableStart") == null) {
        if (paramAttributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableEnd") == null) {
          if (paramAttributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableRight") == null) {
            if (paramAttributeSet.getAttributeBooleanValue("http://schemas.android.com/apk/res/android", "singleLine", true) && paramAttributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "lines", 1) == 1 && paramAttributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "minLines", 1) == 1 && paramAttributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "maxLines", 1) == 1) {
              if (paramAttributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "gravity", 8388627) != 8388627)
                Log.w("Chip", "Chip text must be vertically center and start aligned"); 
              return;
            } 
            throw new UnsupportedOperationException("Chip does not support multi-line text");
          } 
          throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        } 
        throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
      } 
      throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
    } 
    throw new UnsupportedOperationException("Please set left drawable using R.attr#chipIcon.");
  }
  
  protected boolean dispatchHoverEvent(MotionEvent paramMotionEvent) {
    return (handleAccessibilityExit(paramMotionEvent) || this.touchHelper.dispatchHoverEvent(paramMotionEvent) || super.dispatchHoverEvent(paramMotionEvent));
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
    return (this.touchHelper.dispatchKeyEvent(paramKeyEvent) && this.touchHelper.getKeyboardFocusedVirtualViewId() != Integer.MIN_VALUE) ? true : super.dispatchKeyEvent(paramKeyEvent);
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    boolean bool2 = false;
    ChipDrawable chipDrawable = this.chipDrawable;
    boolean bool1 = bool2;
    if (chipDrawable != null) {
      bool1 = bool2;
      if (chipDrawable.isCloseIconStateful())
        bool1 = this.chipDrawable.setCloseIconState(createCloseIconDrawableState()); 
    } 
    if (bool1)
      invalidate(); 
  }
  
  public boolean ensureAccessibleTouchTarget(int paramInt) {
    this.minTouchTargetSize = paramInt;
    boolean bool = shouldEnsureMinTouchTargetSize();
    int j = 0;
    if (!bool) {
      if (this.insetBackgroundDrawable != null) {
        removeBackgroundInset();
      } else {
        updateBackgroundDrawable();
      } 
      return false;
    } 
    int k = Math.max(0, paramInt - this.chipDrawable.getIntrinsicHeight());
    int i = Math.max(0, paramInt - this.chipDrawable.getIntrinsicWidth());
    if (i <= 0 && k <= 0) {
      if (this.insetBackgroundDrawable != null) {
        removeBackgroundInset();
      } else {
        updateBackgroundDrawable();
      } 
      return false;
    } 
    if (i > 0) {
      i /= 2;
    } else {
      i = 0;
    } 
    if (k > 0)
      j = k / 2; 
    if (this.insetBackgroundDrawable != null) {
      Rect rect = new Rect();
      this.insetBackgroundDrawable.getPadding(rect);
      if (rect.top == j && rect.bottom == j && rect.left == i && rect.right == i) {
        updateBackgroundDrawable();
        return true;
      } 
    } 
    if (Build.VERSION.SDK_INT >= 16) {
      if (getMinHeight() != paramInt)
        setMinHeight(paramInt); 
      if (getMinWidth() != paramInt)
        setMinWidth(paramInt); 
    } else {
      setMinHeight(paramInt);
      setMinWidth(paramInt);
    } 
    insetChipBackgroundDrawable(i, j, i, j);
    updateBackgroundDrawable();
    return true;
  }
  
  public Drawable getBackgroundDrawable() {
    InsetDrawable insetDrawable = this.insetBackgroundDrawable;
    return (Drawable)((insetDrawable == null) ? this.chipDrawable : insetDrawable);
  }
  
  public Drawable getCheckedIcon() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      Drawable drawable = chipDrawable.getCheckedIcon();
    } else {
      chipDrawable = null;
    } 
    return (Drawable)chipDrawable;
  }
  
  public ColorStateList getCheckedIconTint() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      ColorStateList colorStateList = chipDrawable.getCheckedIconTint();
    } else {
      chipDrawable = null;
    } 
    return (ColorStateList)chipDrawable;
  }
  
  public ColorStateList getChipBackgroundColor() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      ColorStateList colorStateList = chipDrawable.getChipBackgroundColor();
    } else {
      chipDrawable = null;
    } 
    return (ColorStateList)chipDrawable;
  }
  
  public float getChipCornerRadius() {
    ChipDrawable chipDrawable = this.chipDrawable;
    float f = 0.0F;
    if (chipDrawable != null)
      f = Math.max(0.0F, chipDrawable.getChipCornerRadius()); 
    return f;
  }
  
  public Drawable getChipDrawable() {
    return (Drawable)this.chipDrawable;
  }
  
  public float getChipEndPadding() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getChipEndPadding();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  public Drawable getChipIcon() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      Drawable drawable = chipDrawable.getChipIcon();
    } else {
      chipDrawable = null;
    } 
    return (Drawable)chipDrawable;
  }
  
  public float getChipIconSize() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getChipIconSize();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  public ColorStateList getChipIconTint() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      ColorStateList colorStateList = chipDrawable.getChipIconTint();
    } else {
      chipDrawable = null;
    } 
    return (ColorStateList)chipDrawable;
  }
  
  public float getChipMinHeight() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getChipMinHeight();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  public float getChipStartPadding() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getChipStartPadding();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  public ColorStateList getChipStrokeColor() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      ColorStateList colorStateList = chipDrawable.getChipStrokeColor();
    } else {
      chipDrawable = null;
    } 
    return (ColorStateList)chipDrawable;
  }
  
  public float getChipStrokeWidth() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getChipStrokeWidth();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  @Deprecated
  public CharSequence getChipText() {
    return getText();
  }
  
  public Drawable getCloseIcon() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      Drawable drawable = chipDrawable.getCloseIcon();
    } else {
      chipDrawable = null;
    } 
    return (Drawable)chipDrawable;
  }
  
  public CharSequence getCloseIconContentDescription() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      CharSequence charSequence = chipDrawable.getCloseIconContentDescription();
    } else {
      chipDrawable = null;
    } 
    return (CharSequence)chipDrawable;
  }
  
  public float getCloseIconEndPadding() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getCloseIconEndPadding();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  public float getCloseIconSize() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getCloseIconSize();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  public float getCloseIconStartPadding() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getCloseIconStartPadding();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  public ColorStateList getCloseIconTint() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      ColorStateList colorStateList = chipDrawable.getCloseIconTint();
    } else {
      chipDrawable = null;
    } 
    return (ColorStateList)chipDrawable;
  }
  
  public TextUtils.TruncateAt getEllipsize() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      TextUtils.TruncateAt truncateAt = chipDrawable.getEllipsize();
    } else {
      chipDrawable = null;
    } 
    return (TextUtils.TruncateAt)chipDrawable;
  }
  
  public void getFocusedRect(Rect paramRect) {
    if (this.touchHelper.getKeyboardFocusedVirtualViewId() == 1 || this.touchHelper.getAccessibilityFocusedVirtualViewId() == 1) {
      paramRect.set(getCloseIconTouchBoundsInt());
      return;
    } 
    super.getFocusedRect(paramRect);
  }
  
  public MotionSpec getHideMotionSpec() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      MotionSpec motionSpec = chipDrawable.getHideMotionSpec();
    } else {
      chipDrawable = null;
    } 
    return (MotionSpec)chipDrawable;
  }
  
  public float getIconEndPadding() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getIconEndPadding();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  public float getIconStartPadding() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getIconStartPadding();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  public ColorStateList getRippleColor() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      ColorStateList colorStateList = chipDrawable.getRippleColor();
    } else {
      chipDrawable = null;
    } 
    return (ColorStateList)chipDrawable;
  }
  
  public ShapeAppearanceModel getShapeAppearanceModel() {
    return this.chipDrawable.getShapeAppearanceModel();
  }
  
  public MotionSpec getShowMotionSpec() {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      MotionSpec motionSpec = chipDrawable.getShowMotionSpec();
    } else {
      chipDrawable = null;
    } 
    return (MotionSpec)chipDrawable;
  }
  
  public float getTextEndPadding() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getTextEndPadding();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  public float getTextStartPadding() {
    float f;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      f = chipDrawable.getTextStartPadding();
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  public boolean isCheckable() {
    boolean bool;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null && chipDrawable.isCheckable()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  @Deprecated
  public boolean isCheckedIconEnabled() {
    return isCheckedIconVisible();
  }
  
  public boolean isCheckedIconVisible() {
    boolean bool;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null && chipDrawable.isCheckedIconVisible()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  @Deprecated
  public boolean isChipIconEnabled() {
    return isChipIconVisible();
  }
  
  public boolean isChipIconVisible() {
    boolean bool;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null && chipDrawable.isChipIconVisible()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  @Deprecated
  public boolean isCloseIconEnabled() {
    return isCloseIconVisible();
  }
  
  public boolean isCloseIconVisible() {
    boolean bool;
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null && chipDrawable.isCloseIconVisible()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    MaterialShapeUtils.setParentAbsoluteElevation((View)this, this.chipDrawable);
  }
  
  public void onChipDrawableSizeChange() {
    ensureAccessibleTouchTarget(this.minTouchTargetSize);
    requestLayout();
    if (Build.VERSION.SDK_INT >= 21)
      invalidateOutline(); 
  }
  
  protected int[] onCreateDrawableState(int paramInt) {
    int[] arrayOfInt = super.onCreateDrawableState(paramInt + 2);
    if (isChecked())
      mergeDrawableStates(arrayOfInt, SELECTED_STATE); 
    if (isCheckable())
      mergeDrawableStates(arrayOfInt, CHECKABLE_STATE_SET); 
    return arrayOfInt;
  }
  
  protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect) {
    super.onFocusChanged(paramBoolean, paramInt, paramRect);
    this.touchHelper.onFocusChanged(paramBoolean, paramInt, paramRect);
  }
  
  public boolean onHoverEvent(MotionEvent paramMotionEvent) {
    switch (paramMotionEvent.getActionMasked()) {
      default:
        return super.onHoverEvent(paramMotionEvent);
      case 10:
        setCloseIconHovered(false);
      case 7:
        break;
    } 
    setCloseIconHovered(getCloseIconTouchBounds().contains(paramMotionEvent.getX(), paramMotionEvent.getY()));
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    if (isCheckable() || isClickable()) {
      String str;
      if (isCheckable()) {
        str = "android.widget.CompoundButton";
      } else {
        str = "android.widget.Button";
      } 
      paramAccessibilityNodeInfo.setClassName(str);
    } else {
      paramAccessibilityNodeInfo.setClassName("android.view.View");
    } 
    paramAccessibilityNodeInfo.setCheckable(isCheckable());
    paramAccessibilityNodeInfo.setClickable(isClickable());
    if (getParent() instanceof ChipGroup) {
      byte b;
      ChipGroup chipGroup = (ChipGroup)getParent();
      AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.wrap(paramAccessibilityNodeInfo);
      if (chipGroup.isSingleLine()) {
        b = chipGroup.getIndexOfChip((View)this);
      } else {
        b = -1;
      } 
      accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(chipGroup.getRowIndex((View)this), 1, b, 1, false, isChecked()));
    } 
  }
  
  public PointerIcon onResolvePointerIcon(MotionEvent paramMotionEvent, int paramInt) {
    return (getCloseIconTouchBounds().contains(paramMotionEvent.getX(), paramMotionEvent.getY()) && isEnabled()) ? PointerIcon.getSystemIcon(getContext(), 1002) : null;
  }
  
  public void onRtlPropertiesChanged(int paramInt) {
    super.onRtlPropertiesChanged(paramInt);
    if (this.lastLayoutDirection != paramInt) {
      this.lastLayoutDirection = paramInt;
      updatePaddingInternal();
    } 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    boolean bool2 = false;
    boolean bool3 = false;
    int i = paramMotionEvent.getActionMasked();
    boolean bool = getCloseIconTouchBounds().contains(paramMotionEvent.getX(), paramMotionEvent.getY());
    boolean bool4 = true;
    boolean bool1 = bool3;
    switch (i) {
      default:
        bool1 = bool2;
        break;
      case 2:
        bool1 = bool2;
        if (this.closeIconPressed) {
          if (!bool)
            setCloseIconPressed(false); 
          bool1 = true;
        } 
        break;
      case 1:
        bool1 = bool3;
        if (this.closeIconPressed) {
          performCloseIconClick();
          bool1 = true;
        } 
      case 3:
        setCloseIconPressed(false);
        break;
      case 0:
        bool1 = bool2;
        if (bool) {
          setCloseIconPressed(true);
          bool1 = true;
        } 
        break;
    } 
    bool = bool4;
    if (!bool1)
      if (super.onTouchEvent(paramMotionEvent)) {
        bool = bool4;
      } else {
        bool = false;
      }  
    return bool;
  }
  
  public boolean performCloseIconClick() {
    boolean bool;
    playSoundEffect(0);
    View.OnClickListener onClickListener = this.onCloseIconClickListener;
    if (onClickListener != null) {
      onClickListener.onClick((View)this);
      bool = true;
    } else {
      bool = false;
    } 
    this.touchHelper.sendEventForVirtualView(1, 1);
    return bool;
  }
  
  public void setBackground(Drawable paramDrawable) {
    if (paramDrawable != getBackgroundDrawable() && paramDrawable != this.ripple) {
      Log.w("Chip", "Do not set the background; Chip manages its own background drawable.");
    } else {
      super.setBackground(paramDrawable);
    } 
  }
  
  public void setBackgroundColor(int paramInt) {
    Log.w("Chip", "Do not set the background color; Chip manages its own background drawable.");
  }
  
  public void setBackgroundDrawable(Drawable paramDrawable) {
    if (paramDrawable != getBackgroundDrawable() && paramDrawable != this.ripple) {
      Log.w("Chip", "Do not set the background drawable; Chip manages its own background drawable.");
    } else {
      super.setBackgroundDrawable(paramDrawable);
    } 
  }
  
  public void setBackgroundResource(int paramInt) {
    Log.w("Chip", "Do not set the background resource; Chip manages its own background drawable.");
  }
  
  public void setBackgroundTintList(ColorStateList paramColorStateList) {
    Log.w("Chip", "Do not set the background tint list; Chip manages its own background drawable.");
  }
  
  public void setBackgroundTintMode(PorterDuff.Mode paramMode) {
    Log.w("Chip", "Do not set the background tint mode; Chip manages its own background drawable.");
  }
  
  public void setCheckable(boolean paramBoolean) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCheckable(paramBoolean); 
  }
  
  public void setCheckableResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCheckableResource(paramInt); 
  }
  
  public void setChecked(boolean paramBoolean) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable == null) {
      this.deferredCheckedValue = paramBoolean;
    } else if (chipDrawable.isCheckable()) {
      boolean bool = isChecked();
      super.setChecked(paramBoolean);
      if (bool != paramBoolean) {
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = this.onCheckedChangeListenerInternal;
        if (onCheckedChangeListener != null)
          onCheckedChangeListener.onCheckedChanged((CompoundButton)this, paramBoolean); 
      } 
    } 
  }
  
  public void setCheckedIcon(Drawable paramDrawable) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCheckedIcon(paramDrawable); 
  }
  
  @Deprecated
  public void setCheckedIconEnabled(boolean paramBoolean) {
    setCheckedIconVisible(paramBoolean);
  }
  
  @Deprecated
  public void setCheckedIconEnabledResource(int paramInt) {
    setCheckedIconVisible(paramInt);
  }
  
  public void setCheckedIconResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCheckedIconResource(paramInt); 
  }
  
  public void setCheckedIconTint(ColorStateList paramColorStateList) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCheckedIconTint(paramColorStateList); 
  }
  
  public void setCheckedIconTintResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCheckedIconTintResource(paramInt); 
  }
  
  public void setCheckedIconVisible(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCheckedIconVisible(paramInt); 
  }
  
  public void setCheckedIconVisible(boolean paramBoolean) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCheckedIconVisible(paramBoolean); 
  }
  
  public void setChipBackgroundColor(ColorStateList paramColorStateList) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipBackgroundColor(paramColorStateList); 
  }
  
  public void setChipBackgroundColorResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipBackgroundColorResource(paramInt); 
  }
  
  @Deprecated
  public void setChipCornerRadius(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipCornerRadius(paramFloat); 
  }
  
  @Deprecated
  public void setChipCornerRadiusResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipCornerRadiusResource(paramInt); 
  }
  
  public void setChipDrawable(ChipDrawable paramChipDrawable) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != paramChipDrawable) {
      unapplyChipDrawable(chipDrawable);
      this.chipDrawable = paramChipDrawable;
      paramChipDrawable.setShouldDrawText(false);
      applyChipDrawable(this.chipDrawable);
      ensureAccessibleTouchTarget(this.minTouchTargetSize);
    } 
  }
  
  public void setChipEndPadding(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipEndPadding(paramFloat); 
  }
  
  public void setChipEndPaddingResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipEndPaddingResource(paramInt); 
  }
  
  public void setChipIcon(Drawable paramDrawable) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipIcon(paramDrawable); 
  }
  
  @Deprecated
  public void setChipIconEnabled(boolean paramBoolean) {
    setChipIconVisible(paramBoolean);
  }
  
  @Deprecated
  public void setChipIconEnabledResource(int paramInt) {
    setChipIconVisible(paramInt);
  }
  
  public void setChipIconResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipIconResource(paramInt); 
  }
  
  public void setChipIconSize(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipIconSize(paramFloat); 
  }
  
  public void setChipIconSizeResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipIconSizeResource(paramInt); 
  }
  
  public void setChipIconTint(ColorStateList paramColorStateList) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipIconTint(paramColorStateList); 
  }
  
  public void setChipIconTintResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipIconTintResource(paramInt); 
  }
  
  public void setChipIconVisible(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipIconVisible(paramInt); 
  }
  
  public void setChipIconVisible(boolean paramBoolean) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipIconVisible(paramBoolean); 
  }
  
  public void setChipMinHeight(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipMinHeight(paramFloat); 
  }
  
  public void setChipMinHeightResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipMinHeightResource(paramInt); 
  }
  
  public void setChipStartPadding(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipStartPadding(paramFloat); 
  }
  
  public void setChipStartPaddingResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipStartPaddingResource(paramInt); 
  }
  
  public void setChipStrokeColor(ColorStateList paramColorStateList) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipStrokeColor(paramColorStateList); 
  }
  
  public void setChipStrokeColorResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipStrokeColorResource(paramInt); 
  }
  
  public void setChipStrokeWidth(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipStrokeWidth(paramFloat); 
  }
  
  public void setChipStrokeWidthResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setChipStrokeWidthResource(paramInt); 
  }
  
  @Deprecated
  public void setChipText(CharSequence paramCharSequence) {
    setText(paramCharSequence);
  }
  
  @Deprecated
  public void setChipTextResource(int paramInt) {
    setText(getResources().getString(paramInt));
  }
  
  public void setCloseIcon(Drawable paramDrawable) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIcon(paramDrawable); 
    updateAccessibilityDelegate();
  }
  
  public void setCloseIconContentDescription(CharSequence paramCharSequence) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIconContentDescription(paramCharSequence); 
  }
  
  @Deprecated
  public void setCloseIconEnabled(boolean paramBoolean) {
    setCloseIconVisible(paramBoolean);
  }
  
  @Deprecated
  public void setCloseIconEnabledResource(int paramInt) {
    setCloseIconVisible(paramInt);
  }
  
  public void setCloseIconEndPadding(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIconEndPadding(paramFloat); 
  }
  
  public void setCloseIconEndPaddingResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIconEndPaddingResource(paramInt); 
  }
  
  public void setCloseIconResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIconResource(paramInt); 
    updateAccessibilityDelegate();
  }
  
  public void setCloseIconSize(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIconSize(paramFloat); 
  }
  
  public void setCloseIconSizeResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIconSizeResource(paramInt); 
  }
  
  public void setCloseIconStartPadding(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIconStartPadding(paramFloat); 
  }
  
  public void setCloseIconStartPaddingResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIconStartPaddingResource(paramInt); 
  }
  
  public void setCloseIconTint(ColorStateList paramColorStateList) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIconTint(paramColorStateList); 
  }
  
  public void setCloseIconTintResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIconTintResource(paramInt); 
  }
  
  public void setCloseIconVisible(int paramInt) {
    setCloseIconVisible(getResources().getBoolean(paramInt));
  }
  
  public void setCloseIconVisible(boolean paramBoolean) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setCloseIconVisible(paramBoolean); 
    updateAccessibilityDelegate();
  }
  
  public void setCompoundDrawables(Drawable paramDrawable1, Drawable paramDrawable2, Drawable paramDrawable3, Drawable paramDrawable4) {
    if (paramDrawable1 == null) {
      if (paramDrawable3 == null) {
        super.setCompoundDrawables(paramDrawable1, paramDrawable2, paramDrawable3, paramDrawable4);
        return;
      } 
      throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
    } 
    throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
  }
  
  public void setCompoundDrawablesRelative(Drawable paramDrawable1, Drawable paramDrawable2, Drawable paramDrawable3, Drawable paramDrawable4) {
    if (paramDrawable1 == null) {
      if (paramDrawable3 == null) {
        super.setCompoundDrawablesRelative(paramDrawable1, paramDrawable2, paramDrawable3, paramDrawable4);
        return;
      } 
      throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
    } 
    throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
  }
  
  public void setCompoundDrawablesRelativeWithIntrinsicBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (paramInt1 == 0) {
      if (paramInt3 == 0) {
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(paramInt1, paramInt2, paramInt3, paramInt4);
        return;
      } 
      throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
    } 
    throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
  }
  
  public void setCompoundDrawablesRelativeWithIntrinsicBounds(Drawable paramDrawable1, Drawable paramDrawable2, Drawable paramDrawable3, Drawable paramDrawable4) {
    if (paramDrawable1 == null) {
      if (paramDrawable3 == null) {
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(paramDrawable1, paramDrawable2, paramDrawable3, paramDrawable4);
        return;
      } 
      throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
    } 
    throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
  }
  
  public void setCompoundDrawablesWithIntrinsicBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (paramInt1 == 0) {
      if (paramInt3 == 0) {
        super.setCompoundDrawablesWithIntrinsicBounds(paramInt1, paramInt2, paramInt3, paramInt4);
        return;
      } 
      throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
    } 
    throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
  }
  
  public void setCompoundDrawablesWithIntrinsicBounds(Drawable paramDrawable1, Drawable paramDrawable2, Drawable paramDrawable3, Drawable paramDrawable4) {
    if (paramDrawable1 == null) {
      if (paramDrawable3 == null) {
        super.setCompoundDrawablesWithIntrinsicBounds(paramDrawable1, paramDrawable2, paramDrawable3, paramDrawable4);
        return;
      } 
      throw new UnsupportedOperationException("Please set right drawable using R.attr#closeIcon.");
    } 
    throw new UnsupportedOperationException("Please set left drawable using R.attr#chipIcon.");
  }
  
  public void setElevation(float paramFloat) {
    super.setElevation(paramFloat);
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setElevation(paramFloat); 
  }
  
  public void setEllipsize(TextUtils.TruncateAt paramTruncateAt) {
    if (this.chipDrawable == null)
      return; 
    if (paramTruncateAt != TextUtils.TruncateAt.MARQUEE) {
      super.setEllipsize(paramTruncateAt);
      ChipDrawable chipDrawable = this.chipDrawable;
      if (chipDrawable != null)
        chipDrawable.setEllipsize(paramTruncateAt); 
      return;
    } 
    throw new UnsupportedOperationException("Text within a chip are not allowed to scroll.");
  }
  
  public void setEnsureMinTouchTargetSize(boolean paramBoolean) {
    this.ensureMinTouchTargetSize = paramBoolean;
    ensureAccessibleTouchTarget(this.minTouchTargetSize);
  }
  
  public void setGravity(int paramInt) {
    if (paramInt != 8388627) {
      Log.w("Chip", "Chip text must be vertically center and start aligned");
    } else {
      super.setGravity(paramInt);
    } 
  }
  
  public void setHideMotionSpec(MotionSpec paramMotionSpec) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setHideMotionSpec(paramMotionSpec); 
  }
  
  public void setHideMotionSpecResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setHideMotionSpecResource(paramInt); 
  }
  
  public void setIconEndPadding(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setIconEndPadding(paramFloat); 
  }
  
  public void setIconEndPaddingResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setIconEndPaddingResource(paramInt); 
  }
  
  public void setIconStartPadding(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setIconStartPadding(paramFloat); 
  }
  
  public void setIconStartPaddingResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setIconStartPaddingResource(paramInt); 
  }
  
  public void setLayoutDirection(int paramInt) {
    if (this.chipDrawable == null)
      return; 
    if (Build.VERSION.SDK_INT >= 17)
      super.setLayoutDirection(paramInt); 
  }
  
  public void setLines(int paramInt) {
    if (paramInt <= 1) {
      super.setLines(paramInt);
      return;
    } 
    throw new UnsupportedOperationException("Chip does not support multi-line text");
  }
  
  public void setMaxLines(int paramInt) {
    if (paramInt <= 1) {
      super.setMaxLines(paramInt);
      return;
    } 
    throw new UnsupportedOperationException("Chip does not support multi-line text");
  }
  
  public void setMaxWidth(int paramInt) {
    super.setMaxWidth(paramInt);
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setMaxWidth(paramInt); 
  }
  
  public void setMinLines(int paramInt) {
    if (paramInt <= 1) {
      super.setMinLines(paramInt);
      return;
    } 
    throw new UnsupportedOperationException("Chip does not support multi-line text");
  }
  
  void setOnCheckedChangeListenerInternal(CompoundButton.OnCheckedChangeListener paramOnCheckedChangeListener) {
    this.onCheckedChangeListenerInternal = paramOnCheckedChangeListener;
  }
  
  public void setOnCloseIconClickListener(View.OnClickListener paramOnClickListener) {
    this.onCloseIconClickListener = paramOnClickListener;
    updateAccessibilityDelegate();
  }
  
  public void setRippleColor(ColorStateList paramColorStateList) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setRippleColor(paramColorStateList); 
    if (!this.chipDrawable.getUseCompatRipple())
      updateFrameworkRippleBackground(); 
  }
  
  public void setRippleColorResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null) {
      chipDrawable.setRippleColorResource(paramInt);
      if (!this.chipDrawable.getUseCompatRipple())
        updateFrameworkRippleBackground(); 
    } 
  }
  
  public void setShapeAppearanceModel(ShapeAppearanceModel paramShapeAppearanceModel) {
    this.chipDrawable.setShapeAppearanceModel(paramShapeAppearanceModel);
  }
  
  public void setShowMotionSpec(MotionSpec paramMotionSpec) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setShowMotionSpec(paramMotionSpec); 
  }
  
  public void setShowMotionSpecResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setShowMotionSpecResource(paramInt); 
  }
  
  public void setSingleLine(boolean paramBoolean) {
    if (paramBoolean) {
      super.setSingleLine(paramBoolean);
      return;
    } 
    throw new UnsupportedOperationException("Chip does not support multi-line text");
  }
  
  public void setText(CharSequence paramCharSequence, TextView.BufferType paramBufferType) {
    ChipDrawable chipDrawable2 = this.chipDrawable;
    if (chipDrawable2 == null)
      return; 
    CharSequence charSequence = paramCharSequence;
    if (paramCharSequence == null)
      charSequence = ""; 
    if (chipDrawable2.shouldDrawText()) {
      paramCharSequence = null;
    } else {
      paramCharSequence = charSequence;
    } 
    super.setText(paramCharSequence, paramBufferType);
    ChipDrawable chipDrawable1 = this.chipDrawable;
    if (chipDrawable1 != null)
      chipDrawable1.setText(charSequence); 
  }
  
  public void setTextAppearance(int paramInt) {
    super.setTextAppearance(paramInt);
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setTextAppearanceResource(paramInt); 
    updateTextPaintDrawState();
  }
  
  public void setTextAppearance(Context paramContext, int paramInt) {
    super.setTextAppearance(paramContext, paramInt);
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setTextAppearanceResource(paramInt); 
    updateTextPaintDrawState();
  }
  
  public void setTextAppearance(TextAppearance paramTextAppearance) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setTextAppearance(paramTextAppearance); 
    updateTextPaintDrawState();
  }
  
  public void setTextAppearanceResource(int paramInt) {
    setTextAppearance(getContext(), paramInt);
  }
  
  public void setTextEndPadding(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setTextEndPadding(paramFloat); 
  }
  
  public void setTextEndPaddingResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setTextEndPaddingResource(paramInt); 
  }
  
  public void setTextStartPadding(float paramFloat) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setTextStartPadding(paramFloat); 
  }
  
  public void setTextStartPaddingResource(int paramInt) {
    ChipDrawable chipDrawable = this.chipDrawable;
    if (chipDrawable != null)
      chipDrawable.setTextStartPaddingResource(paramInt); 
  }
  
  public boolean shouldEnsureMinTouchTargetSize() {
    return this.ensureMinTouchTargetSize;
  }
  
  private class ChipTouchHelper extends ExploreByTouchHelper {
    final Chip this$0;
    
    ChipTouchHelper(Chip param1Chip1) {
      super((View)param1Chip1);
    }
    
    protected int getVirtualViewAt(float param1Float1, float param1Float2) {
      boolean bool;
      if (Chip.this.hasCloseIcon() && Chip.this.getCloseIconTouchBounds().contains(param1Float1, param1Float2)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    protected void getVisibleVirtualViews(List<Integer> param1List) {
      param1List.add(Integer.valueOf(0));
      if (Chip.this.hasCloseIcon() && Chip.this.isCloseIconVisible() && Chip.this.onCloseIconClickListener != null)
        param1List.add(Integer.valueOf(1)); 
    }
    
    protected boolean onPerformActionForVirtualView(int param1Int1, int param1Int2, Bundle param1Bundle) {
      if (param1Int2 == 16) {
        if (param1Int1 == 0)
          return Chip.this.performClick(); 
        if (param1Int1 == 1)
          return Chip.this.performCloseIconClick(); 
      } 
      return false;
    }
    
    protected void onPopulateNodeForHost(AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      param1AccessibilityNodeInfoCompat.setCheckable(Chip.this.isCheckable());
      param1AccessibilityNodeInfoCompat.setClickable(Chip.this.isClickable());
      if (Chip.this.isCheckable() || Chip.this.isClickable()) {
        String str;
        if (Chip.this.isCheckable()) {
          str = "android.widget.CompoundButton";
        } else {
          str = "android.widget.Button";
        } 
        param1AccessibilityNodeInfoCompat.setClassName(str);
      } else {
        param1AccessibilityNodeInfoCompat.setClassName("android.view.View");
      } 
      CharSequence charSequence = Chip.this.getText();
      if (Build.VERSION.SDK_INT >= 23) {
        param1AccessibilityNodeInfoCompat.setText(charSequence);
      } else {
        param1AccessibilityNodeInfoCompat.setContentDescription(charSequence);
      } 
    }
    
    protected void onPopulateNodeForVirtualView(int param1Int, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      String str = "";
      if (param1Int == 1) {
        CharSequence charSequence = Chip.this.getCloseIconContentDescription();
        if (charSequence != null) {
          param1AccessibilityNodeInfoCompat.setContentDescription(charSequence);
        } else {
          CharSequence charSequence1;
          charSequence = Chip.this.getText();
          Context context = Chip.this.getContext();
          param1Int = R.string.mtrl_chip_close_icon_content_description;
          if (!TextUtils.isEmpty(charSequence))
            charSequence1 = charSequence; 
          param1AccessibilityNodeInfoCompat.setContentDescription(context.getString(param1Int, new Object[] { charSequence1 }).trim());
        } 
        param1AccessibilityNodeInfoCompat.setBoundsInParent(Chip.this.getCloseIconTouchBoundsInt());
        param1AccessibilityNodeInfoCompat.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK);
        param1AccessibilityNodeInfoCompat.setEnabled(Chip.this.isEnabled());
      } else {
        param1AccessibilityNodeInfoCompat.setContentDescription("");
        param1AccessibilityNodeInfoCompat.setBoundsInParent(Chip.EMPTY_BOUNDS);
      } 
    }
    
    protected void onVirtualViewKeyboardFocusChanged(int param1Int, boolean param1Boolean) {
      if (param1Int == 1) {
        Chip.access$402(Chip.this, param1Boolean);
        Chip.this.refreshDrawableState();
      } 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\chip\Chip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */