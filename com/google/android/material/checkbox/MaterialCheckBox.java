package com.google.android.material.checkbox;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.widget.CompoundButtonCompat;
import com.google.android.material.R;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;

public class MaterialCheckBox extends AppCompatCheckBox {
  private static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_CompoundButton_CheckBox;
  
  private static final int[][] ENABLED_CHECKED_STATES;
  
  private ColorStateList materialThemeColorsTintList;
  
  private boolean useMaterialThemeColors;
  
  static {
    int[] arrayOfInt = { -16842910, 16842912 };
    ENABLED_CHECKED_STATES = new int[][] { { 16842910, 16842912 }, { 16842910, -16842912 }, arrayOfInt, { -16842910, -16842912 } };
  }
  
  public MaterialCheckBox(Context paramContext) {
    this(paramContext, null);
  }
  
  public MaterialCheckBox(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.checkboxStyle);
  }
  
  public MaterialCheckBox(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(MaterialThemeOverlay.wrap(paramContext, paramAttributeSet, paramInt, i), paramAttributeSet, paramInt);
    paramContext = getContext();
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.MaterialCheckBox, paramInt, i, new int[0]);
    if (typedArray.hasValue(R.styleable.MaterialCheckBox_buttonTint))
      CompoundButtonCompat.setButtonTintList((CompoundButton)this, MaterialResources.getColorStateList(paramContext, typedArray, R.styleable.MaterialCheckBox_buttonTint)); 
    this.useMaterialThemeColors = typedArray.getBoolean(R.styleable.MaterialCheckBox_useMaterialThemeColors, false);
    typedArray.recycle();
  }
  
  private ColorStateList getMaterialThemeColorsTintList() {
    if (this.materialThemeColorsTintList == null) {
      int[][] arrayOfInt = ENABLED_CHECKED_STATES;
      int[] arrayOfInt1 = new int[arrayOfInt.length];
      int j = MaterialColors.getColor((View)this, R.attr.colorControlActivated);
      int k = MaterialColors.getColor((View)this, R.attr.colorSurface);
      int i = MaterialColors.getColor((View)this, R.attr.colorOnSurface);
      arrayOfInt1[0] = MaterialColors.layer(k, j, 1.0F);
      arrayOfInt1[1] = MaterialColors.layer(k, i, 0.54F);
      arrayOfInt1[2] = MaterialColors.layer(k, i, 0.38F);
      arrayOfInt1[3] = MaterialColors.layer(k, i, 0.38F);
      this.materialThemeColorsTintList = new ColorStateList(arrayOfInt, arrayOfInt1);
    } 
    return this.materialThemeColorsTintList;
  }
  
  public boolean isUseMaterialThemeColors() {
    return this.useMaterialThemeColors;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (this.useMaterialThemeColors && CompoundButtonCompat.getButtonTintList((CompoundButton)this) == null)
      setUseMaterialThemeColors(true); 
  }
  
  public void setUseMaterialThemeColors(boolean paramBoolean) {
    this.useMaterialThemeColors = paramBoolean;
    if (paramBoolean) {
      CompoundButtonCompat.setButtonTintList((CompoundButton)this, getMaterialThemeColorsTintList());
    } else {
      CompoundButtonCompat.setButtonTintList((CompoundButton)this, null);
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\checkbox\MaterialCheckBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */