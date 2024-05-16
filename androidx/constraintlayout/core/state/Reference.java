package androidx.constraintlayout.core.state;

import androidx.constraintlayout.core.state.helpers.Facade;
import androidx.constraintlayout.core.widgets.ConstraintWidget;

public interface Reference {
  void apply();
  
  ConstraintWidget getConstraintWidget();
  
  Facade getFacade();
  
  Object getKey();
  
  void setConstraintWidget(ConstraintWidget paramConstraintWidget);
  
  void setKey(Object paramObject);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\state\Reference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */