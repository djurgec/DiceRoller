package androidx.constraintlayout.core.state.helpers;

import androidx.constraintlayout.core.state.ConstraintReference;
import androidx.constraintlayout.core.state.HelperReference;
import androidx.constraintlayout.core.state.State;

public class AlignVerticallyReference extends HelperReference {
  private float mBias = 0.5F;
  
  public AlignVerticallyReference(State paramState) {
    super(paramState, State.Helper.ALIGN_VERTICALLY);
  }
  
  public void apply() {
    for (ConstraintReference constraintReference : this.mReferences) {
      constraintReference = this.mState.constraints(constraintReference);
      constraintReference.clearVertical();
      if (this.mTopToTop != null) {
        constraintReference.topToTop(this.mTopToTop);
      } else if (this.mTopToBottom != null) {
        constraintReference.topToBottom(this.mTopToBottom);
      } else {
        constraintReference.topToTop(State.PARENT);
      } 
      if (this.mBottomToTop != null) {
        constraintReference.bottomToTop(this.mBottomToTop);
      } else if (this.mBottomToBottom != null) {
        constraintReference.bottomToBottom(this.mBottomToBottom);
      } else {
        constraintReference.bottomToBottom(State.PARENT);
      } 
      float f = this.mBias;
      if (f != 0.5F)
        constraintReference.verticalBias(f); 
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\state\helpers\AlignVerticallyReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */