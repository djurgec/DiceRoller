package androidx.transition;

import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransitionValues {
  final ArrayList<Transition> mTargetedTransitions = new ArrayList<>();
  
  public final Map<String, Object> values = new HashMap<>();
  
  public View view;
  
  @Deprecated
  public TransitionValues() {}
  
  public TransitionValues(View paramView) {
    this.view = paramView;
  }
  
  public boolean equals(Object paramObject) {
    return (paramObject instanceof TransitionValues && this.view == ((TransitionValues)paramObject).view && this.values.equals(((TransitionValues)paramObject).values));
  }
  
  public int hashCode() {
    return this.view.hashCode() * 31 + this.values.hashCode();
  }
  
  public String toString() {
    String str = "TransitionValues@" + Integer.toHexString(hashCode()) + ":\n";
    str = str + "    view = " + this.view + "\n";
    str = str + "    values:";
    for (String str1 : this.values.keySet())
      str = str + "    " + str1 + ": " + this.values.get(str1) + "\n"; 
    return str;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\TransitionValues.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */