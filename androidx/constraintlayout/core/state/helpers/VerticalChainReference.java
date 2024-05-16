package androidx.constraintlayout.core.state.helpers;

import androidx.constraintlayout.core.state.ConstraintReference;
import androidx.constraintlayout.core.state.State;

public class VerticalChainReference extends ChainReference {
  public VerticalChainReference(State paramState) {
    super(paramState, State.Helper.VERTICAL_CHAIN);
  }
  
  public void apply() {
    ConstraintReference constraintReference2 = null;
    ConstraintReference constraintReference1 = null;
    for (Object object : this.mReferences)
      this.mState.constraints(object).clearVertical(); 
    for (ConstraintReference constraintReference3 : this.mReferences) {
      constraintReference3 = this.mState.constraints(constraintReference3);
      ConstraintReference constraintReference4 = constraintReference2;
      if (constraintReference2 == null) {
        constraintReference4 = constraintReference3;
        if (this.mTopToTop != null) {
          constraintReference4.topToTop(this.mTopToTop);
        } else if (this.mTopToBottom != null) {
          constraintReference4.topToBottom(this.mTopToBottom);
        } else {
          constraintReference4.topToTop(State.PARENT);
        } 
      } 
      if (constraintReference1 != null) {
        constraintReference1.bottomToTop(constraintReference3.getKey());
        constraintReference3.topToBottom(constraintReference1.getKey());
      } 
      constraintReference1 = constraintReference3;
      constraintReference2 = constraintReference4;
    } 
    if (constraintReference1 != null)
      if (this.mBottomToTop != null) {
        constraintReference1.bottomToTop(this.mBottomToTop);
      } else if (this.mBottomToBottom != null) {
        constraintReference1.bottomToBottom(this.mBottomToBottom);
      } else {
        constraintReference1.bottomToBottom(State.PARENT);
      }  
    if (constraintReference2 == null)
      return; 
    if (this.mBias != 0.5F)
      constraintReference2.verticalBias(this.mBias); 
    switch (this.mStyle) {
      default:
        return;
      case null:
        constraintReference2.setVerticalChainStyle(2);
      case null:
        constraintReference2.setVerticalChainStyle(1);
      case null:
        break;
    } 
    constraintReference2.setVerticalChainStyle(0);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\state\helpers\VerticalChainReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */