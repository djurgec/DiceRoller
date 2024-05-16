package androidx.constraintlayout.core.state.helpers;

import androidx.constraintlayout.core.state.ConstraintReference;
import androidx.constraintlayout.core.state.HelperReference;
import androidx.constraintlayout.core.state.State;
import androidx.constraintlayout.core.widgets.Barrier;
import androidx.constraintlayout.core.widgets.HelperWidget;

public class BarrierReference extends HelperReference {
  private Barrier mBarrierWidget;
  
  private State.Direction mDirection;
  
  private int mMargin;
  
  public BarrierReference(State paramState) {
    super(paramState, State.Helper.BARRIER);
  }
  
  public void apply() {
    getHelperWidget();
    byte b = 0;
    switch (this.mDirection) {
      case null:
        b = 3;
        break;
      case null:
        b = 2;
        break;
      case null:
      case null:
        b = 1;
        break;
    } 
    this.mBarrierWidget.setBarrierType(b);
    this.mBarrierWidget.setMargin(this.mMargin);
  }
  
  public HelperWidget getHelperWidget() {
    if (this.mBarrierWidget == null)
      this.mBarrierWidget = new Barrier(); 
    return (HelperWidget)this.mBarrierWidget;
  }
  
  public ConstraintReference margin(int paramInt) {
    this.mMargin = paramInt;
    return (ConstraintReference)this;
  }
  
  public ConstraintReference margin(Object paramObject) {
    margin(this.mState.convertDimension(paramObject));
    return (ConstraintReference)this;
  }
  
  public void setBarrierDirection(State.Direction paramDirection) {
    this.mDirection = paramDirection;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\state\helpers\BarrierReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */