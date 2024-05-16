package androidx.constraintlayout.core.state;

import androidx.constraintlayout.core.state.helpers.Facade;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.HelperWidget;
import java.util.ArrayList;
import java.util.Collections;

public class HelperReference extends ConstraintReference implements Facade {
  private HelperWidget mHelperWidget;
  
  protected ArrayList<Object> mReferences = new ArrayList();
  
  protected final State mState;
  
  final State.Helper mType;
  
  public HelperReference(State paramState, State.Helper paramHelper) {
    super(paramState);
    this.mState = paramState;
    this.mType = paramHelper;
  }
  
  public HelperReference add(Object... paramVarArgs) {
    Collections.addAll(this.mReferences, paramVarArgs);
    return this;
  }
  
  public void apply() {}
  
  public ConstraintWidget getConstraintWidget() {
    return (ConstraintWidget)getHelperWidget();
  }
  
  public HelperWidget getHelperWidget() {
    return this.mHelperWidget;
  }
  
  public State.Helper getType() {
    return this.mType;
  }
  
  public void setHelperWidget(HelperWidget paramHelperWidget) {
    this.mHelperWidget = paramHelperWidget;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\state\HelperReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */