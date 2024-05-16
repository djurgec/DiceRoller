package com.google.android.material.radiobutton;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.widget.CompoundButtonCompat;
import com.google.android.material.R;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;

public class MaterialRadioButton extends AppCompatRadioButton {
  private static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_CompoundButton_RadioButton;
  
  private static final int[][] ENABLED_CHECKED_STATES;
  
  private ColorStateList materialThemeColorsTintList;
  
  private boolean useMaterialThemeColors;
  
  static {
    int[] arrayOfInt1 = { 16842910, 16842912 };
    int[] arrayOfInt2 = { -16842910, 16842912 };
    int[] arrayOfInt3 = { -16842910, -16842912 };
    ENABLED_CHECKED_STATES = new int[][] { arrayOfInt1, { 16842910, -16842912 }, arrayOfInt2, arrayOfInt3 };
  }
  
  public MaterialRadioButton(Context paramContext) {
    this(paramContext, null);
  }
  
  public MaterialRadioButton(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.radioButtonStyle);
  }
  
  public MaterialRadioButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(MaterialThemeOverlay.wrap(paramContext, paramAttributeSet, paramInt, i), paramAttributeSet, paramInt);
    paramContext = getContext();
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.MaterialRadioButton, paramInt, i, new int[0]);
    if (typedArray.hasValue(R.styleable.MaterialRadioButton_buttonTint))
      CompoundButtonCompat.setButtonTintList((CompoundButton)this, MaterialResources.getColorStateList(paramContext, typedArray, R.styleable.MaterialRadioButton_buttonTint)); 
    this.useMaterialThemeColors = typedArray.getBoolean(R.styleable.MaterialRadioButton_useMaterialThemeColors, false);
    typedArray.recycle();
  }
  
  private ColorStateList getMaterialThemeColorsTintList() {
    if (this.materialThemeColorsTintList == null) {
      int k = MaterialColors.getColor((View)this, R.attr.colorControlActivated);
      int i = MaterialColors.getColor((View)this, R.attr.colorOnSurface);
      int j = MaterialColors.getColor((View)this, R.attr.colorSurface);
      int[][] arrayOfInt1 = ENABLED_CHECKED_STATES;
      int[] arrayOfInt = new int[arrayOfInt1.length];
      arrayOfInt[0] = MaterialColors.layer(j, k, 1.0F);
      arrayOfInt[1] = MaterialColors.layer(j, i, 0.54F);
      arrayOfInt[2] = MaterialColors.layer(j, i, 0.38F);
      arrayOfInt[3] = MaterialColors.layer(j, i, 0.38F);
      this.materialThemeColorsTintList = new ColorStateList(arrayOfInt1, arrayOfInt);
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


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\radiobutton\MaterialRadioButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */