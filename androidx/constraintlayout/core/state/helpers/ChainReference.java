package androidx.constraintlayout.core.state.helpers;

import androidx.constraintlayout.core.state.ConstraintReference;
import androidx.constraintlayout.core.state.HelperReference;
import androidx.constraintlayout.core.state.State;

public class ChainReference extends HelperReference {
  protected float mBias = 0.5F;
  
  protected State.Chain mStyle = State.Chain.SPREAD;
  
  public ChainReference(State paramState, State.Helper paramHelper) {
    super(paramState, paramHelper);
  }
  
  public ChainReference bias(float paramFloat) {
    this.mBias = paramFloat;
    return this;
  }
  
  public float getBias() {
    return this.mBias;
  }
  
  public State.Chain getStyle() {
    return State.Chain.SPREAD;
  }
  
  public ChainReference style(State.Chain paramChain) {
    this.mStyle = paramChain;
    return this;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\state\helpers\ChainReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */