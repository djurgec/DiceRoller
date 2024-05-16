package com.google.android.material.shape;

import android.graphics.drawable.Drawable;
import android.view.View;
import com.google.android.material.internal.ViewUtils;

public class MaterialShapeUtils {
  static CornerTreatment createCornerTreatment(int paramInt) {
    switch (paramInt) {
      default:
        return createDefaultCornerTreatment();
      case 1:
        return new CutCornerTreatment();
      case 0:
        break;
    } 
    return new RoundedCornerTreatment();
  }
  
  static CornerTreatment createDefaultCornerTreatment() {
    return new RoundedCornerTreatment();
  }
  
  static EdgeTreatment createDefaultEdgeTreatment() {
    return new EdgeTreatment();
  }
  
  public static void setElevation(View paramView, float paramFloat) {
    Drawable drawable = paramView.getBackground();
    if (drawable instanceof MaterialShapeDrawable)
      ((MaterialShapeDrawable)drawable).setElevation(paramFloat); 
  }
  
  public static void setParentAbsoluteElevation(View paramView) {
    Drawable drawable = paramView.getBackground();
    if (drawable instanceof MaterialShapeDrawable)
      setParentAbsoluteElevation(paramView, (MaterialShapeDrawable)drawable); 
  }
  
  public static void setParentAbsoluteElevation(View paramView, MaterialShapeDrawable paramMaterialShapeDrawable) {
    if (paramMaterialShapeDrawable.isElevationOverlayEnabled())
      paramMaterialShapeDrawable.setParentAbsoluteElevation(ViewUtils.getParentAbsoluteElevation(paramView)); 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\shape\MaterialShapeUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */