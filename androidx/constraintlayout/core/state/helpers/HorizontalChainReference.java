package androidx.constraintlayout.core.state.helpers;

import androidx.constraintlayout.core.state.ConstraintReference;
import androidx.constraintlayout.core.state.State;

public class HorizontalChainReference extends ChainReference {
  public HorizontalChainReference(State paramState) {
    super(paramState, State.Helper.HORIZONTAL_CHAIN);
  }
  
  public void apply() {
    ConstraintReference constraintReference2 = null;
    ConstraintReference constraintReference1 = null;
    for (Object object : this.mReferences)
      this.mState.constraints(object).clearHorizontal(); 
    for (ConstraintReference constraintReference3 : this.mReferences) {
      constraintReference3 = this.mState.constraints(constraintReference3);
      ConstraintReference constraintReference4 = constraintReference2;
      if (constraintReference2 == null) {
        constraintReference4 = constraintReference3;
        if (this.mStartToStart != null) {
          constraintReference4.startToStart(this.mStartToStart).margin(this.mMarginStart);
        } else if (this.mStartToEnd != null) {
          constraintReference4.startToEnd(this.mStartToEnd).margin(this.mMarginStart);
        } else {
          constraintReference4.startToStart(State.PARENT);
        } 
      } 
      if (constraintReference1 != null) {
        constraintReference1.endToStart(constraintReference3.getKey());
        constraintReference3.startToEnd(constraintReference1.getKey());
      } 
      constraintReference1 = constraintReference3;
      constraintReference2 = constraintReference4;
    } 
    if (constraintReference1 != null)
      if (this.mEndToStart != null) {
        constraintReference1.endToStart(this.mEndToStart).margin(this.mMarginEnd);
      } else if (this.mEndToEnd != null) {
        constraintReference1.endToEnd(this.mEndToEnd).margin(this.mMarginEnd);
      } else {
        constraintReference1.endToEnd(State.PARENT);
      }  
    if (constraintReference2 == null)
      return; 
    if (this.mBias != 0.5F)
      constraintReference2.horizontalBias(this.mBias); 
    switch (this.mStyle) {
      default:
        return;
      case null:
        constraintReference2.setHorizontalChainStyle(2);
      case null:
        constraintReference2.setHorizontalChainStyle(1);
      case null:
        break;
    } 
    constraintReference2.setHorizontalChainStyle(0);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\state\helpers\HorizontalChainReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */