package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.motion.widget.MotionLayout;

public class ReactiveGuide extends View implements SharedValues.SharedValuesListener {
  private boolean mAnimateChange = false;
  
  private boolean mApplyToAllConstraintSets = true;
  
  private int mApplyToConstraintSetId = 0;
  
  private int mAttributeId = -1;
  
  public ReactiveGuide(Context paramContext) {
    super(paramContext);
    super.setVisibility(8);
    init((AttributeSet)null);
  }
  
  public ReactiveGuide(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    super.setVisibility(8);
    init(paramAttributeSet);
  }
  
  public ReactiveGuide(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    super.setVisibility(8);
    init(paramAttributeSet);
  }
  
  public ReactiveGuide(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1);
    super.setVisibility(8);
    init(paramAttributeSet);
  }
  
  private void changeValue(int paramInt1, int paramInt2, MotionLayout paramMotionLayout, int paramInt3) {
    ConstraintSet constraintSet = paramMotionLayout.getConstraintSet(paramInt3);
    constraintSet.setGuidelineEnd(paramInt2, paramInt1);
    paramMotionLayout.updateState(paramInt3, constraintSet);
  }
  
  private void init(AttributeSet paramAttributeSet) {
    if (paramAttributeSet != null) {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.ConstraintLayout_ReactiveGuide);
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        if (j == R.styleable.ConstraintLayout_ReactiveGuide_reactiveGuide_valueId) {
          this.mAttributeId = typedArray.getResourceId(j, this.mAttributeId);
        } else if (j == R.styleable.ConstraintLayout_ReactiveGuide_reactiveGuide_animateChange) {
          this.mAnimateChange = typedArray.getBoolean(j, this.mAnimateChange);
        } else if (j == R.styleable.ConstraintLayout_ReactiveGuide_reactiveGuide_applyToConstraintSet) {
          this.mApplyToConstraintSetId = typedArray.getResourceId(j, this.mApplyToConstraintSetId);
        } else if (j == R.styleable.ConstraintLayout_ReactiveGuide_reactiveGuide_applyToAllConstraintSets) {
          this.mApplyToAllConstraintSets = typedArray.getBoolean(j, this.mApplyToAllConstraintSets);
        } 
      } 
      typedArray.recycle();
    } 
    if (this.mAttributeId != -1)
      ConstraintLayout.getSharedValues().addListener(this.mAttributeId, this); 
  }
  
  public void draw(Canvas paramCanvas) {}
  
  public int getApplyToConstraintSetId() {
    return this.mApplyToConstraintSetId;
  }
  
  public int getAttributeId() {
    return this.mAttributeId;
  }
  
  public boolean isAnimatingChange() {
    return this.mAnimateChange;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    setMeasuredDimension(0, 0);
  }
  
  public void onNewValue(int paramInt1, int paramInt2, int paramInt3) {
    setGuidelineBegin(paramInt2);
    int i = getId();
    if (i <= 0)
      return; 
    if (getParent() instanceof MotionLayout) {
      MotionLayout motionLayout = (MotionLayout)getParent();
      paramInt1 = motionLayout.getCurrentState();
      if (this.mApplyToConstraintSetId != 0)
        paramInt1 = this.mApplyToConstraintSetId; 
      if (this.mAnimateChange) {
        if (this.mApplyToAllConstraintSets) {
          int[] arrayOfInt = motionLayout.getConstraintSetIds();
          for (paramInt3 = 0; paramInt3 < arrayOfInt.length; paramInt3++) {
            int j = arrayOfInt[paramInt3];
            if (j != paramInt1)
              changeValue(paramInt2, i, motionLayout, j); 
          } 
        } 
        ConstraintSet constraintSet = motionLayout.cloneConstraintSet(paramInt1);
        constraintSet.setGuidelineEnd(i, paramInt2);
        motionLayout.updateStateAnimate(paramInt1, constraintSet, 1000);
      } else if (this.mApplyToAllConstraintSets) {
        int[] arrayOfInt = motionLayout.getConstraintSetIds();
        for (paramInt1 = 0; paramInt1 < arrayOfInt.length; paramInt1++)
          changeValue(paramInt2, i, motionLayout, arrayOfInt[paramInt1]); 
      } else {
        changeValue(paramInt2, i, motionLayout, paramInt1);
      } 
    } 
  }
  
  public void setAnimateChange(boolean paramBoolean) {
    this.mAnimateChange = paramBoolean;
  }
  
  public void setApplyToConstraintSetId(int paramInt) {
    this.mApplyToConstraintSetId = paramInt;
  }
  
  public void setAttributeId(int paramInt) {
    SharedValues sharedValues = ConstraintLayout.getSharedValues();
    int i = this.mAttributeId;
    if (i != -1)
      sharedValues.removeListener(i, this); 
    this.mAttributeId = paramInt;
    if (paramInt != -1)
      sharedValues.addListener(paramInt, this); 
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


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\widget\ReactiveGuide.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */