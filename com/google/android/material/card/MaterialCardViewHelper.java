package com.google.android.material.card;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.graphics.drawable.DrawableCompat;
import com.google.android.material.R;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shape.CornerTreatment;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

class MaterialCardViewHelper {
  private static final float CARD_VIEW_SHADOW_MULTIPLIER = 1.5F;
  
  private static final int CHECKED_ICON_LAYER_INDEX = 2;
  
  private static final int[] CHECKED_STATE_SET = new int[] { 16842912 };
  
  private static final double COS_45 = Math.cos(Math.toRadians(45.0D));
  
  private static final int DEFAULT_STROKE_VALUE = -1;
  
  private final MaterialShapeDrawable bgDrawable;
  
  private boolean checkable;
  
  private Drawable checkedIcon;
  
  private int checkedIconMargin;
  
  private int checkedIconSize;
  
  private ColorStateList checkedIconTint;
  
  private LayerDrawable clickableForegroundDrawable;
  
  private MaterialShapeDrawable compatRippleDrawable;
  
  private Drawable fgDrawable;
  
  private final MaterialShapeDrawable foregroundContentDrawable;
  
  private MaterialShapeDrawable foregroundShapeDrawable;
  
  private boolean isBackgroundOverwritten = false;
  
  private final MaterialCardView materialCardView;
  
  private ColorStateList rippleColor;
  
  private Drawable rippleDrawable;
  
  private ShapeAppearanceModel shapeAppearanceModel;
  
  private ColorStateList strokeColor;
  
  private int strokeWidth;
  
  private final Rect userContentPadding = new Rect();
  
  public MaterialCardViewHelper(MaterialCardView paramMaterialCardView, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    this.materialCardView = paramMaterialCardView;
    MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(paramMaterialCardView.getContext(), paramAttributeSet, paramInt1, paramInt2);
    this.bgDrawable = materialShapeDrawable;
    materialShapeDrawable.initializeElevationOverlay(paramMaterialCardView.getContext());
    materialShapeDrawable.setShadowColor(-12303292);
    ShapeAppearanceModel.Builder builder = materialShapeDrawable.getShapeAppearanceModel().toBuilder();
    TypedArray typedArray = paramMaterialCardView.getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.CardView, paramInt1, R.style.CardView);
    if (typedArray.hasValue(R.styleable.CardView_cardCornerRadius))
      builder.setAllCornerSizes(typedArray.getDimension(R.styleable.CardView_cardCornerRadius, 0.0F)); 
    this.foregroundContentDrawable = new MaterialShapeDrawable();
    setShapeAppearanceModel(builder.build());
    typedArray.recycle();
  }
  
  private float calculateActualCornerPadding() {
    return Math.max(Math.max(calculateCornerPaddingForCornerTreatment(this.shapeAppearanceModel.getTopLeftCorner(), this.bgDrawable.getTopLeftCornerResolvedSize()), calculateCornerPaddingForCornerTreatment(this.shapeAppearanceModel.getTopRightCorner(), this.bgDrawable.getTopRightCornerResolvedSize())), Math.max(calculateCornerPaddingForCornerTreatment(this.shapeAppearanceModel.getBottomRightCorner(), this.bgDrawable.getBottomRightCornerResolvedSize()), calculateCornerPaddingForCornerTreatment(this.shapeAppearanceModel.getBottomLeftCorner(), this.bgDrawable.getBottomLeftCornerResolvedSize())));
  }
  
  private float calculateCornerPaddingForCornerTreatment(CornerTreatment paramCornerTreatment, float paramFloat) {
    return (paramCornerTreatment instanceof com.google.android.material.shape.RoundedCornerTreatment) ? (float)((1.0D - COS_45) * paramFloat) : ((paramCornerTreatment instanceof com.google.android.material.shape.CutCornerTreatment) ? (paramFloat / 2.0F) : 0.0F);
  }
  
  private float calculateHorizontalBackgroundPadding() {
    float f1;
    float f2 = this.materialCardView.getMaxCardElevation();
    if (shouldAddCornerPaddingOutsideCardBackground()) {
      f1 = calculateActualCornerPadding();
    } else {
      f1 = 0.0F;
    } 
    return f2 + f1;
  }
  
  private float calculateVerticalBackgroundPadding() {
    float f1;
    float f2 = this.materialCardView.getMaxCardElevation();
    if (shouldAddCornerPaddingOutsideCardBackground()) {
      f1 = calculateActualCornerPadding();
    } else {
      f1 = 0.0F;
    } 
    return f2 * 1.5F + f1;
  }
  
  private boolean canClipToOutline() {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 21 && this.bgDrawable.isRoundRect()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private Drawable createCheckedIconLayer() {
    StateListDrawable stateListDrawable = new StateListDrawable();
    Drawable drawable = this.checkedIcon;
    if (drawable != null)
      stateListDrawable.addState(CHECKED_STATE_SET, drawable); 
    return (Drawable)stateListDrawable;
  }
  
  private Drawable createCompatRippleDrawable() {
    StateListDrawable stateListDrawable = new StateListDrawable();
    MaterialShapeDrawable materialShapeDrawable = createForegroundShapeDrawable();
    this.compatRippleDrawable = materialShapeDrawable;
    materialShapeDrawable.setFillColor(this.rippleColor);
    materialShapeDrawable = this.compatRippleDrawable;
    stateListDrawable.addState(new int[] { 16842919 }, (Drawable)materialShapeDrawable);
    return (Drawable)stateListDrawable;
  }
  
  private Drawable createForegroundRippleDrawable() {
    if (RippleUtils.USE_FRAMEWORK_RIPPLE) {
      this.foregroundShapeDrawable = createForegroundShapeDrawable();
      return (Drawable)new RippleDrawable(this.rippleColor, null, (Drawable)this.foregroundShapeDrawable);
    } 
    return createCompatRippleDrawable();
  }
  
  private MaterialShapeDrawable createForegroundShapeDrawable() {
    return new MaterialShapeDrawable(this.shapeAppearanceModel);
  }
  
  private Drawable getClickableForeground() {
    if (this.rippleDrawable == null)
      this.rippleDrawable = createForegroundRippleDrawable(); 
    if (this.clickableForegroundDrawable == null) {
      Drawable drawable = createCheckedIconLayer();
      LayerDrawable layerDrawable = new LayerDrawable(new Drawable[] { this.rippleDrawable, (Drawable)this.foregroundContentDrawable, drawable });
      this.clickableForegroundDrawable = layerDrawable;
      layerDrawable.setId(2, R.id.mtrl_card_checked_layer_id);
    } 
    return (Drawable)this.clickableForegroundDrawable;
  }
  
  private float getParentCardViewCalculatedCornerPadding() {
    return (this.materialCardView.getPreventCornerOverlap() && (Build.VERSION.SDK_INT < 21 || this.materialCardView.getUseCompatPadding())) ? (float)((1.0D - COS_45) * this.materialCardView.getCardViewRadius()) : 0.0F;
  }
  
  private Drawable insetDrawable(Drawable paramDrawable) {
    int j = 0;
    byte b = 0;
    if (Build.VERSION.SDK_INT < 21) {
      i = 1;
    } else {
      i = 0;
    } 
    if (!i) {
      i = b;
      if (this.materialCardView.getUseCompatPadding()) {
        j = (int)Math.ceil(calculateVerticalBackgroundPadding());
        i = (int)Math.ceil(calculateHorizontalBackgroundPadding());
        return (Drawable)new InsetDrawable(paramDrawable, i, j, i, j) {
            final MaterialCardViewHelper this$0;
            
            public int getMinimumHeight() {
              return -1;
            }
            
            public int getMinimumWidth() {
              return -1;
            }
            
            public boolean getPadding(Rect param1Rect) {
              return false;
            }
          };
      } 
      return (Drawable)new InsetDrawable(paramDrawable, i, j, i, j) {
          final MaterialCardViewHelper this$0;
          
          public int getMinimumHeight() {
            return -1;
          }
          
          public int getMinimumWidth() {
            return -1;
          }
          
          public boolean getPadding(Rect param1Rect) {
            return false;
          }
        };
    } 
    j = (int)Math.ceil(calculateVerticalBackgroundPadding());
    int i = (int)Math.ceil(calculateHorizontalBackgroundPadding());
    return (Drawable)new InsetDrawable(paramDrawable, i, j, i, j) {
        final MaterialCardViewHelper this$0;
        
        public int getMinimumHeight() {
          return -1;
        }
        
        public int getMinimumWidth() {
          return -1;
        }
        
        public boolean getPadding(Rect param1Rect) {
          return false;
        }
      };
  }
  
  private boolean shouldAddCornerPaddingInsideCardBackground() {
    boolean bool;
    if (this.materialCardView.getPreventCornerOverlap() && !canClipToOutline()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean shouldAddCornerPaddingOutsideCardBackground() {
    boolean bool;
    if (this.materialCardView.getPreventCornerOverlap() && canClipToOutline() && this.materialCardView.getUseCompatPadding()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void updateInsetForeground(Drawable paramDrawable) {
    if (Build.VERSION.SDK_INT >= 23 && this.materialCardView.getForeground() instanceof InsetDrawable) {
      ((InsetDrawable)this.materialCardView.getForeground()).setDrawable(paramDrawable);
    } else {
      this.materialCardView.setForeground(insetDrawable(paramDrawable));
    } 
  }
  
  private void updateRippleColor() {
    if (RippleUtils.USE_FRAMEWORK_RIPPLE) {
      Drawable drawable = this.rippleDrawable;
      if (drawable != null) {
        ((RippleDrawable)drawable).setColor(this.rippleColor);
        return;
      } 
    } 
    MaterialShapeDrawable materialShapeDrawable = this.compatRippleDrawable;
    if (materialShapeDrawable != null)
      materialShapeDrawable.setFillColor(this.rippleColor); 
  }
  
  void forceRippleRedraw() {
    Drawable drawable = this.rippleDrawable;
    if (drawable != null) {
      Rect rect = drawable.getBounds();
      int i = rect.bottom;
      this.rippleDrawable.setBounds(rect.left, rect.top, rect.right, i - 1);
      this.rippleDrawable.setBounds(rect.left, rect.top, rect.right, i);
    } 
  }
  
  MaterialShapeDrawable getBackground() {
    return this.bgDrawable;
  }
  
  ColorStateList getCardBackgroundColor() {
    return this.bgDrawable.getFillColor();
  }
  
  ColorStateList getCardForegroundColor() {
    return this.foregroundContentDrawable.getFillColor();
  }
  
  Drawable getCheckedIcon() {
    return this.checkedIcon;
  }
  
  int getCheckedIconMargin() {
    return this.checkedIconMargin;
  }
  
  int getCheckedIconSize() {
    return this.checkedIconSize;
  }
  
  ColorStateList getCheckedIconTint() {
    return this.checkedIconTint;
  }
  
  float getCornerRadius() {
    return this.bgDrawable.getTopLeftCornerResolvedSize();
  }
  
  float getProgress() {
    return this.bgDrawable.getInterpolation();
  }
  
  ColorStateList getRippleColor() {
    return this.rippleColor;
  }
  
  ShapeAppearanceModel getShapeAppearanceModel() {
    return this.shapeAppearanceModel;
  }
  
  int getStrokeColor() {
    int i;
    ColorStateList colorStateList = this.strokeColor;
    if (colorStateList == null) {
      i = -1;
    } else {
      i = colorStateList.getDefaultColor();
    } 
    return i;
  }
  
  ColorStateList getStrokeColorStateList() {
    return this.strokeColor;
  }
  
  int getStrokeWidth() {
    return this.strokeWidth;
  }
  
  Rect getUserContentPadding() {
    return this.userContentPadding;
  }
  
  boolean isBackgroundOverwritten() {
    return this.isBackgroundOverwritten;
  }
  
  boolean isCheckable() {
    return this.checkable;
  }
  
  void loadFromAttributes(TypedArray paramTypedArray) {
    MaterialShapeDrawable materialShapeDrawable;
    ColorStateList colorStateList = MaterialResources.getColorStateList(this.materialCardView.getContext(), paramTypedArray, R.styleable.MaterialCardView_strokeColor);
    this.strokeColor = colorStateList;
    if (colorStateList == null)
      this.strokeColor = ColorStateList.valueOf(-1); 
    this.strokeWidth = paramTypedArray.getDimensionPixelSize(R.styleable.MaterialCardView_strokeWidth, 0);
    boolean bool = paramTypedArray.getBoolean(R.styleable.MaterialCardView_android_checkable, false);
    this.checkable = bool;
    this.materialCardView.setLongClickable(bool);
    this.checkedIconTint = MaterialResources.getColorStateList(this.materialCardView.getContext(), paramTypedArray, R.styleable.MaterialCardView_checkedIconTint);
    setCheckedIcon(MaterialResources.getDrawable(this.materialCardView.getContext(), paramTypedArray, R.styleable.MaterialCardView_checkedIcon));
    setCheckedIconSize(paramTypedArray.getDimensionPixelSize(R.styleable.MaterialCardView_checkedIconSize, 0));
    setCheckedIconMargin(paramTypedArray.getDimensionPixelSize(R.styleable.MaterialCardView_checkedIconMargin, 0));
    colorStateList = MaterialResources.getColorStateList(this.materialCardView.getContext(), paramTypedArray, R.styleable.MaterialCardView_rippleColor);
    this.rippleColor = colorStateList;
    if (colorStateList == null)
      this.rippleColor = ColorStateList.valueOf(MaterialColors.getColor((View)this.materialCardView, R.attr.colorControlHighlight)); 
    setCardForegroundColor(MaterialResources.getColorStateList(this.materialCardView.getContext(), paramTypedArray, R.styleable.MaterialCardView_cardForegroundColor));
    updateRippleColor();
    updateElevation();
    updateStroke();
    this.materialCardView.setBackgroundInternal(insetDrawable((Drawable)this.bgDrawable));
    if (this.materialCardView.isClickable()) {
      Drawable drawable = getClickableForeground();
    } else {
      materialShapeDrawable = this.foregroundContentDrawable;
    } 
    this.fgDrawable = (Drawable)materialShapeDrawable;
    this.materialCardView.setForeground(insetDrawable((Drawable)materialShapeDrawable));
  }
  
  void onMeasure(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: getfield clickableForegroundDrawable : Landroid/graphics/drawable/LayerDrawable;
    //   4: ifnull -> 144
    //   7: aload_0
    //   8: getfield checkedIconMargin : I
    //   11: istore #4
    //   13: aload_0
    //   14: getfield checkedIconSize : I
    //   17: istore #5
    //   19: iload_1
    //   20: iload #4
    //   22: isub
    //   23: iload #5
    //   25: isub
    //   26: istore_3
    //   27: iload_2
    //   28: iload #4
    //   30: isub
    //   31: iload #5
    //   33: isub
    //   34: istore #4
    //   36: getstatic android/os/Build$VERSION.SDK_INT : I
    //   39: bipush #21
    //   41: if_icmpge -> 49
    //   44: iconst_1
    //   45: istore_1
    //   46: goto -> 51
    //   49: iconst_0
    //   50: istore_1
    //   51: iload_1
    //   52: ifne -> 70
    //   55: iload #4
    //   57: istore_2
    //   58: iload_3
    //   59: istore_1
    //   60: aload_0
    //   61: getfield materialCardView : Lcom/google/android/material/card/MaterialCardView;
    //   64: invokevirtual getUseCompatPadding : ()Z
    //   67: ifeq -> 99
    //   70: iload #4
    //   72: aload_0
    //   73: invokespecial calculateVerticalBackgroundPadding : ()F
    //   76: fconst_2
    //   77: fmul
    //   78: f2d
    //   79: invokestatic ceil : (D)D
    //   82: d2i
    //   83: isub
    //   84: istore_2
    //   85: iload_3
    //   86: aload_0
    //   87: invokespecial calculateHorizontalBackgroundPadding : ()F
    //   90: fconst_2
    //   91: fmul
    //   92: f2d
    //   93: invokestatic ceil : (D)D
    //   96: d2i
    //   97: isub
    //   98: istore_1
    //   99: aload_0
    //   100: getfield checkedIconMargin : I
    //   103: istore #4
    //   105: iload_1
    //   106: istore #5
    //   108: iload #4
    //   110: istore_3
    //   111: aload_0
    //   112: getfield materialCardView : Lcom/google/android/material/card/MaterialCardView;
    //   115: invokestatic getLayoutDirection : (Landroid/view/View;)I
    //   118: iconst_1
    //   119: if_icmpne -> 128
    //   122: iload_1
    //   123: istore_3
    //   124: iload #4
    //   126: istore #5
    //   128: aload_0
    //   129: getfield clickableForegroundDrawable : Landroid/graphics/drawable/LayerDrawable;
    //   132: iconst_2
    //   133: iload #5
    //   135: aload_0
    //   136: getfield checkedIconMargin : I
    //   139: iload_3
    //   140: iload_2
    //   141: invokevirtual setLayerInset : (IIIII)V
    //   144: return
  }
  
  void setBackgroundOverwritten(boolean paramBoolean) {
    this.isBackgroundOverwritten = paramBoolean;
  }
  
  void setCardBackgroundColor(ColorStateList paramColorStateList) {
    this.bgDrawable.setFillColor(paramColorStateList);
  }
  
  void setCardForegroundColor(ColorStateList paramColorStateList) {
    MaterialShapeDrawable materialShapeDrawable = this.foregroundContentDrawable;
    if (paramColorStateList == null)
      paramColorStateList = ColorStateList.valueOf(0); 
    materialShapeDrawable.setFillColor(paramColorStateList);
  }
  
  void setCheckable(boolean paramBoolean) {
    this.checkable = paramBoolean;
  }
  
  void setCheckedIcon(Drawable paramDrawable) {
    this.checkedIcon = paramDrawable;
    if (paramDrawable != null) {
      paramDrawable = DrawableCompat.wrap(paramDrawable.mutate());
      this.checkedIcon = paramDrawable;
      DrawableCompat.setTintList(paramDrawable, this.checkedIconTint);
    } 
    if (this.clickableForegroundDrawable != null) {
      paramDrawable = createCheckedIconLayer();
      this.clickableForegroundDrawable.setDrawableByLayerId(R.id.mtrl_card_checked_layer_id, paramDrawable);
    } 
  }
  
  void setCheckedIconMargin(int paramInt) {
    this.checkedIconMargin = paramInt;
  }
  
  void setCheckedIconSize(int paramInt) {
    this.checkedIconSize = paramInt;
  }
  
  void setCheckedIconTint(ColorStateList paramColorStateList) {
    this.checkedIconTint = paramColorStateList;
    Drawable drawable = this.checkedIcon;
    if (drawable != null)
      DrawableCompat.setTintList(drawable, paramColorStateList); 
  }
  
  void setCornerRadius(float paramFloat) {
    setShapeAppearanceModel(this.shapeAppearanceModel.withCornerSize(paramFloat));
    this.fgDrawable.invalidateSelf();
    if (shouldAddCornerPaddingOutsideCardBackground() || shouldAddCornerPaddingInsideCardBackground())
      updateContentPadding(); 
    if (shouldAddCornerPaddingOutsideCardBackground())
      updateInsets(); 
  }
  
  void setProgress(float paramFloat) {
    this.bgDrawable.setInterpolation(paramFloat);
    MaterialShapeDrawable materialShapeDrawable = this.foregroundContentDrawable;
    if (materialShapeDrawable != null)
      materialShapeDrawable.setInterpolation(paramFloat); 
    materialShapeDrawable = this.foregroundShapeDrawable;
    if (materialShapeDrawable != null)
      materialShapeDrawable.setInterpolation(paramFloat); 
  }
  
  void setRippleColor(ColorStateList paramColorStateList) {
    this.rippleColor = paramColorStateList;
    updateRippleColor();
  }
  
  void setShapeAppearanceModel(ShapeAppearanceModel paramShapeAppearanceModel) {
    this.shapeAppearanceModel = paramShapeAppearanceModel;
    this.bgDrawable.setShapeAppearanceModel(paramShapeAppearanceModel);
    MaterialShapeDrawable materialShapeDrawable = this.bgDrawable;
    materialShapeDrawable.setShadowBitmapDrawingEnable(materialShapeDrawable.isRoundRect() ^ true);
    materialShapeDrawable = this.foregroundContentDrawable;
    if (materialShapeDrawable != null)
      materialShapeDrawable.setShapeAppearanceModel(paramShapeAppearanceModel); 
    materialShapeDrawable = this.foregroundShapeDrawable;
    if (materialShapeDrawable != null)
      materialShapeDrawable.setShapeAppearanceModel(paramShapeAppearanceModel); 
    materialShapeDrawable = this.compatRippleDrawable;
    if (materialShapeDrawable != null)
      materialShapeDrawable.setShapeAppearanceModel(paramShapeAppearanceModel); 
  }
  
  void setStrokeColor(ColorStateList paramColorStateList) {
    if (this.strokeColor == paramColorStateList)
      return; 
    this.strokeColor = paramColorStateList;
    updateStroke();
  }
  
  void setStrokeWidth(int paramInt) {
    if (paramInt == this.strokeWidth)
      return; 
    this.strokeWidth = paramInt;
    updateStroke();
  }
  
  void setUserContentPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.userContentPadding.set(paramInt1, paramInt2, paramInt3, paramInt4);
    updateContentPadding();
  }
  
  void updateClickable() {
    MaterialShapeDrawable materialShapeDrawable;
    Drawable drawable = this.fgDrawable;
    if (this.materialCardView.isClickable()) {
      Drawable drawable1 = getClickableForeground();
    } else {
      materialShapeDrawable = this.foregroundContentDrawable;
    } 
    this.fgDrawable = (Drawable)materialShapeDrawable;
    if (drawable != materialShapeDrawable)
      updateInsetForeground((Drawable)materialShapeDrawable); 
  }
  
  void updateContentPadding() {
    float f;
    if (shouldAddCornerPaddingInsideCardBackground() || shouldAddCornerPaddingOutsideCardBackground()) {
      i = 1;
    } else {
      i = 0;
    } 
    if (i) {
      f = calculateActualCornerPadding();
    } else {
      f = 0.0F;
    } 
    int i = (int)(f - getParentCardViewCalculatedCornerPadding());
    this.materialCardView.setAncestorContentPadding(this.userContentPadding.left + i, this.userContentPadding.top + i, this.userContentPadding.right + i, this.userContentPadding.bottom + i);
  }
  
  void updateElevation() {
    this.bgDrawable.setElevation(this.materialCardView.getCardElevation());
  }
  
  void updateInsets() {
    if (!isBackgroundOverwritten())
      this.materialCardView.setBackgroundInternal(insetDrawable((Drawable)this.bgDrawable)); 
    this.materialCardView.setForeground(insetDrawable(this.fgDrawable));
  }
  
  void updateStroke() {
    this.foregroundContentDrawable.setStroke(this.strokeWidth, this.strokeColor);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\card\MaterialCardViewHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */