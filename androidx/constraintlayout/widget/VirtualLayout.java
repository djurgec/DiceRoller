package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;

public abstract class VirtualLayout extends ConstraintHelper {
  private boolean mApplyElevationOnAttach;
  
  private boolean mApplyVisibilityOnAttach;
  
  public VirtualLayout(Context paramContext) {
    super(paramContext);
  }
  
  public VirtualLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  public VirtualLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  protected void applyLayoutFeaturesInConstraintSet(ConstraintLayout paramConstraintLayout) {
    applyLayoutFeatures(paramConstraintLayout);
  }
  
  protected void init(AttributeSet paramAttributeSet) {
    super.init(paramAttributeSet);
    if (paramAttributeSet != null) {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.ConstraintLayout_Layout);
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        if (j == R.styleable.ConstraintLayout_Layout_android_visibility) {
          this.mApplyVisibilityOnAttach = true;
        } else if (j == R.styleable.ConstraintLayout_Layout_android_elevation) {
          this.mApplyElevationOnAttach = true;
        } 
      } 
      typedArray.recycle();
    } 
  }
  
  public void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (this.mApplyVisibilityOnAttach || this.mApplyElevationOnAttach) {
      ViewParent viewParent = getParent();
      if (viewParent instanceof ConstraintLayout) {
        ConstraintLayout constraintLayout = (ConstraintLayout)viewParent;
        int i = getVisibility();
        float f = 0.0F;
        if (Build.VERSION.SDK_INT >= 21)
          f = getElevation(); 
        for (byte b = 0; b < this.mCount; b++) {
          View view = constraintLayout.getViewById(this.mIds[b]);
          if (view != null) {
            if (this.mApplyVisibilityOnAttach)
              view.setVisibility(i); 
            if (this.mApplyElevationOnAttach && f > 0.0F && Build.VERSION.SDK_INT >= 21)
              view.setTranslationZ(view.getTranslationZ() + f); 
          } 
        } 
      } 
    } 
  }
  
  public void onMeasure(androidx.constraintlayout.core.widgets.VirtualLayout paramVirtualLayout, int paramInt1, int paramInt2) {}
  
  public void setElevation(float paramFloat) {
    super.setElevation(paramFloat);
    applyLayoutFeatures();
  }
  
  public void setVisibility(int paramInt) {
    super.setVisibility(paramInt);
    applyLayoutFeatures();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\widget\VirtualLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */