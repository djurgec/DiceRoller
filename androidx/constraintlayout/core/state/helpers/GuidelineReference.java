package androidx.constraintlayout.core.state.helpers;

import androidx.constraintlayout.core.state.Reference;
import androidx.constraintlayout.core.state.State;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.Guideline;

public class GuidelineReference implements Facade, Reference {
  private Object key;
  
  private int mEnd = -1;
  
  private Guideline mGuidelineWidget;
  
  private int mOrientation;
  
  private float mPercent = 0.0F;
  
  private int mStart = -1;
  
  final State mState;
  
  public GuidelineReference(State paramState) {
    this.mState = paramState;
  }
  
  public void apply() {
    this.mGuidelineWidget.setOrientation(this.mOrientation);
    int i = this.mStart;
    if (i != -1) {
      this.mGuidelineWidget.setGuideBegin(i);
    } else {
      i = this.mEnd;
      if (i != -1) {
        this.mGuidelineWidget.setGuideEnd(i);
      } else {
        this.mGuidelineWidget.setGuidePercent(this.mPercent);
      } 
    } 
  }
  
  public GuidelineReference end(Object paramObject) {
    this.mStart = -1;
    this.mEnd = this.mState.convertDimension(paramObject);
    this.mPercent = 0.0F;
    return this;
  }
  
  public ConstraintWidget getConstraintWidget() {
    if (this.mGuidelineWidget == null)
      this.mGuidelineWidget = new Guideline(); 
    return (ConstraintWidget)this.mGuidelineWidget;
  }
  
  public Facade getFacade() {
    return null;
  }
  
  public Object getKey() {
    return this.key;
  }
  
  public int getOrientation() {
    return this.mOrientation;
  }
  
  public GuidelineReference percent(float paramFloat) {
    this.mStart = -1;
    this.mEnd = -1;
    this.mPercent = paramFloat;
    return this;
  }
  
  public void setConstraintWidget(ConstraintWidget paramConstraintWidget) {
    if (paramConstraintWidget instanceof Guideline) {
      this.mGuidelineWidget = (Guideline)paramConstraintWidget;
    } else {
      this.mGuidelineWidget = null;
    } 
  }
  
  public void setKey(Object paramObject) {
    this.key = paramObject;
  }
  
  public void setOrientation(int paramInt) {
    this.mOrientation = paramInt;
  }
  
  public GuidelineReference start(Object paramObject) {
    this.mStart = this.mState.convertDimension(paramObject);
    this.mEnd = -1;
    this.mPercent = 0.0F;
    return this;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\state\helpers\GuidelineReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */