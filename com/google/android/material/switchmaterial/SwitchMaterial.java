package com.google.android.material.switchmaterial;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.widget.SwitchCompat;
import com.google.android.material.R;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.elevation.ElevationOverlayProvider;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;

public class SwitchMaterial extends SwitchCompat {
  private static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_CompoundButton_Switch;
  
  private static final int[][] ENABLED_CHECKED_STATES = new int[][] { { 16842910, 16842912 }, { 16842910, -16842912 }, { -16842910, 16842912 }, { -16842910, -16842912 } };
  
  private final ElevationOverlayProvider elevationOverlayProvider;
  
  private ColorStateList materialThemeColorsThumbTintList;
  
  private ColorStateList materialThemeColorsTrackTintList;
  
  private boolean useMaterialThemeColors;
  
  public SwitchMaterial(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public SwitchMaterial(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.switchStyle);
  }
  
  public SwitchMaterial(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(MaterialThemeOverlay.wrap(paramContext, paramAttributeSet, paramInt, i), paramAttributeSet, paramInt);
    paramContext = getContext();
    this.elevationOverlayProvider = new ElevationOverlayProvider(paramContext);
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.SwitchMaterial, paramInt, i, new int[0]);
    this.useMaterialThemeColors = typedArray.getBoolean(R.styleable.SwitchMaterial_useMaterialThemeColors, false);
    typedArray.recycle();
  }
  
  private ColorStateList getMaterialThemeColorsThumbTintList() {
    if (this.materialThemeColorsThumbTintList == null) {
      int i = MaterialColors.getColor((View)this, R.attr.colorSurface);
      int k = MaterialColors.getColor((View)this, R.attr.colorControlActivated);
      float f2 = getResources().getDimension(R.dimen.mtrl_switch_thumb_elevation);
      float f1 = f2;
      if (this.elevationOverlayProvider.isThemeElevationOverlayEnabled())
        f1 = f2 + ViewUtils.getParentAbsoluteElevation((View)this); 
      int j = this.elevationOverlayProvider.compositeOverlayIfNeeded(i, f1);
      int[][] arrayOfInt1 = ENABLED_CHECKED_STATES;
      int[] arrayOfInt = new int[arrayOfInt1.length];
      arrayOfInt[0] = MaterialColors.layer(i, k, 1.0F);
      arrayOfInt[1] = j;
      arrayOfInt[2] = MaterialColors.layer(i, k, 0.38F);
      arrayOfInt[3] = j;
      this.materialThemeColorsThumbTintList = new ColorStateList(arrayOfInt1, arrayOfInt);
    } 
    return this.materialThemeColorsThumbTintList;
  }
  
  private ColorStateList getMaterialThemeColorsTrackTintList() {
    if (this.materialThemeColorsTrackTintList == null) {
      int[][] arrayOfInt1 = ENABLED_CHECKED_STATES;
      int[] arrayOfInt = new int[arrayOfInt1.length];
      int k = MaterialColors.getColor((View)this, R.attr.colorSurface);
      int j = MaterialColors.getColor((View)this, R.attr.colorControlActivated);
      int i = MaterialColors.getColor((View)this, R.attr.colorOnSurface);
      arrayOfInt[0] = MaterialColors.layer(k, j, 0.54F);
      arrayOfInt[1] = MaterialColors.layer(k, i, 0.32F);
      arrayOfInt[2] = MaterialColors.layer(k, j, 0.12F);
      arrayOfInt[3] = MaterialColors.layer(k, i, 0.12F);
      this.materialThemeColorsTrackTintList = new ColorStateList(arrayOfInt1, arrayOfInt);
    } 
    return this.materialThemeColorsTrackTintList;
  }
  
  public boolean isUseMaterialThemeColors() {
    return this.useMaterialThemeColors;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (this.useMaterialThemeColors && getThumbTintList() == null)
      setThumbTintList(getMaterialThemeColorsThumbTintList()); 
    if (this.useMaterialThemeColors && getTrackTintList() == null)
      setTrackTintList(getMaterialThemeColorsTrackTintList()); 
  }
  
  public void setUseMaterialThemeColors(boolean paramBoolean) {
    this.useMaterialThemeColors = paramBoolean;
    if (paramBoolean) {
      setThumbTintList(getMaterialThemeColorsThumbTintList());
      setTrackTintList(getMaterialThemeColorsTrackTintList());
    } else {
      setThumbTintList(null);
      setTrackTintList(null);
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\switchmaterial\SwitchMaterial.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */