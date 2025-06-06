package androidx.constraintlayout.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class Guideline extends View {
  public Guideline(Context paramContext) {
    super(paramContext);
    super.setVisibility(8);
  }
  
  public Guideline(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    super.setVisibility(8);
  }
  
  public Guideline(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    super.setVisibility(8);
  }
  
  public Guideline(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1);
    super.setVisibility(8);
  }
  
  public void draw(Canvas paramCanvas) {}
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    setMeasuredDimension(0, 0);
  }
  
  public void setGuidelineBegin(int paramInt) {
    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)getLayoutParams();
    layoutParams.guideBegin = paramInt;
    setLayoutParams((ViewGroup.LayoutParams)layoutParams);
  }
  
  public void setGuidelineEnd(int paramInt) {
    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)getLayoutParams();
    layoutParams.guideEnd = paramInt;
    setLayoutParams((ViewGroup.LayoutParams)layoutParams);
  }
  
  public void setGuidelinePercent(float paramFloat) {
    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)getLayoutParams();
    layoutParams.guidePercent = paramFloat;
    setLayoutParams((ViewGroup.LayoutParams)layoutParams);
  }
  
  public void setVisibility(int paramInt) {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\widget\Guideline.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */