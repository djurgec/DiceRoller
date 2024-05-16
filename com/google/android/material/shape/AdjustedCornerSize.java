package com.google.android.material.shape;

import android.graphics.RectF;
import java.util.Arrays;

public final class AdjustedCornerSize implements CornerSize {
  private final float adjustment;
  
  private final CornerSize other;
  
  public AdjustedCornerSize(float paramFloat, CornerSize paramCornerSize) {
    while (paramCornerSize instanceof AdjustedCornerSize) {
      paramCornerSize = ((AdjustedCornerSize)paramCornerSize).other;
      paramFloat += ((AdjustedCornerSize)paramCornerSize).adjustment;
    } 
    this.other = paramCornerSize;
    this.adjustment = paramFloat;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = true;
    if (this == paramObject)
      return true; 
    if (!(paramObject instanceof AdjustedCornerSize))
      return false; 
    paramObject = paramObject;
    if (!this.other.equals(((AdjustedCornerSize)paramObject).other) || this.adjustment != ((AdjustedCornerSize)paramObject).adjustment)
      bool = false; 
    return bool;
  }
  
  public float getCornerSize(RectF paramRectF) {
    return Math.max(0.0F, this.other.getCornerSize(paramRectF) + this.adjustment);
  }
  
  public int hashCode() {
    return Arrays.hashCode(new Object[] { this.other, Float.valueOf(this.adjustment) });
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\shape\AdjustedCornerSize.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */