package androidx.constraintlayout.core.state.helpers;

import androidx.constraintlayout.core.state.ConstraintReference;
import androidx.constraintlayout.core.state.HelperReference;
import androidx.constraintlayout.core.state.State;

public class AlignHorizontallyReference extends HelperReference {
  private float mBias = 0.5F;
  
  public AlignHorizontallyReference(State paramState) {
    super(paramState, State.Helper.ALIGN_VERTICALLY);
  }
  
  public void apply() {
    for (ConstraintReference constraintReference : this.mReferences) {
      constraintReference = this.mState.constraints(constraintReference);
      constraintReference.clearHorizontal();
      if (this.mStartToStart != null) {
        constraintReference.startToStart(this.mStartToStart);
      } else if (this.mStartToEnd != null) {
        constraintReference.startToEnd(this.mStartToEnd);
      } else {
        constraintReference.startToStart(State.PARENT);
      } 
      if (this.mEndToStart != null) {
        constraintReference.endToStart(this.mEndToStart);
      } else if (this.mEndToEnd != null) {
        constraintReference.endToEnd(this.mEndToEnd);
      } else {
        constraintReference.endToEnd(State.PARENT);
      } 
      float f = this.mBias;
      if (f != 0.5F)
        constraintReference.horizontalBias(f); 
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\state\helpers\AlignHorizontallyReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */