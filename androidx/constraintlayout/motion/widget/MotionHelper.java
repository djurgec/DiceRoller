package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.R;
import java.util.HashMap;

public class MotionHelper extends ConstraintHelper implements MotionHelperInterface {
  private float mProgress;
  
  private boolean mUseOnHide = false;
  
  private boolean mUseOnShow = false;
  
  protected View[] views;
  
  public MotionHelper(Context paramContext) {
    super(paramContext);
  }
  
  public MotionHelper(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramAttributeSet);
  }
  
  public MotionHelper(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramAttributeSet);
  }
  
  public float getProgress() {
    return this.mProgress;
  }
  
  protected void init(AttributeSet paramAttributeSet) {
    super.init(paramAttributeSet);
    if (paramAttributeSet != null) {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.MotionHelper);
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        if (j == R.styleable.MotionHelper_onShow) {
          this.mUseOnShow = typedArray.getBoolean(j, this.mUseOnShow);
        } else if (j == R.styleable.MotionHelper_onHide) {
          this.mUseOnHide = typedArray.getBoolean(j, this.mUseOnHide);
        } 
      } 
      typedArray.recycle();
    } 
  }
  
  public boolean isDecorator() {
    return false;
  }
  
  public boolean isUseOnHide() {
    return this.mUseOnHide;
  }
  
  public boolean isUsedOnShow() {
    return this.mUseOnShow;
  }
  
  public void onFinishedMotionScene(MotionLayout paramMotionLayout) {}
  
  public void onPostDraw(Canvas paramCanvas) {}
  
  public void onPreDraw(Canvas paramCanvas) {}
  
  public void onPreSetup(MotionLayout paramMotionLayout, HashMap<View, MotionController> paramHashMap) {}
  
  public void onTransitionChange(MotionLayout paramMotionLayout, int paramInt1, int paramInt2, float paramFloat) {}
  
  public void onTransitionCompleted(MotionLayout paramMotionLayout, int paramInt) {}
  
  public void onTransitionStarted(MotionLayout paramMotionLayout, int paramInt1, int paramInt2) {}
  
  public void onTransitionTrigger(MotionLayout paramMotionLayout, int paramInt, boolean paramBoolean, float paramFloat) {}
  
  public void setProgress(float paramFloat) {
    this.mProgress = paramFloat;
    if (this.mCount > 0) {
      this.views = getViews((ConstraintLayout)getParent());
      for (byte b = 0; b < this.mCount; b++)
        setProgress(this.views[b], paramFloat); 
    } else {
      ViewGroup viewGroup = (ViewGroup)getParent();
      int i = viewGroup.getChildCount();
      for (byte b = 0; b < i; b++) {
        View view = viewGroup.getChildAt(b);
        if (!(view instanceof MotionHelper))
          setProgress(view, paramFloat); 
      } 
    } 
  }
  
  public void setProgress(View paramView, float paramFloat) {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\motion\widget\MotionHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */