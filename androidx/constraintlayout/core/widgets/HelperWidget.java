package androidx.constraintlayout.core.widgets;

import androidx.constraintlayout.core.widgets.analyzer.Grouping;
import androidx.constraintlayout.core.widgets.analyzer.WidgetGroup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class HelperWidget extends ConstraintWidget implements Helper {
  public ConstraintWidget[] mWidgets = new ConstraintWidget[4];
  
  public int mWidgetsCount = 0;
  
  public void add(ConstraintWidget paramConstraintWidget) {
    if (paramConstraintWidget == this || paramConstraintWidget == null)
      return; 
    int i = this.mWidgetsCount;
    ConstraintWidget[] arrayOfConstraintWidget = this.mWidgets;
    if (i + 1 > arrayOfConstraintWidget.length)
      this.mWidgets = Arrays.<ConstraintWidget>copyOf(arrayOfConstraintWidget, arrayOfConstraintWidget.length * 2); 
    arrayOfConstraintWidget = this.mWidgets;
    i = this.mWidgetsCount;
    arrayOfConstraintWidget[i] = paramConstraintWidget;
    this.mWidgetsCount = i + 1;
  }
  
  public void addDependents(ArrayList<WidgetGroup> paramArrayList, int paramInt, WidgetGroup paramWidgetGroup) {
    byte b;
    for (b = 0; b < this.mWidgetsCount; b++)
      paramWidgetGroup.add(this.mWidgets[b]); 
    for (b = 0; b < this.mWidgetsCount; b++)
      Grouping.findDependents(this.mWidgets[b], paramInt, paramArrayList, paramWidgetGroup); 
  }
  
  public void copy(ConstraintWidget paramConstraintWidget, HashMap<ConstraintWidget, ConstraintWidget> paramHashMap) {
    super.copy(paramConstraintWidget, paramHashMap);
    paramConstraintWidget = paramConstraintWidget;
    this.mWidgetsCount = 0;
    int i = ((HelperWidget)paramConstraintWidget).mWidgetsCount;
    for (byte b = 0; b < i; b++)
      add(paramHashMap.get(((HelperWidget)paramConstraintWidget).mWidgets[b])); 
  }
  
  public int findGroupInDependents(int paramInt) {
    for (byte b = 0; b < this.mWidgetsCount; b++) {
      ConstraintWidget constraintWidget = this.mWidgets[b];
      if (paramInt == 0 && constraintWidget.horizontalGroup != -1)
        return constraintWidget.horizontalGroup; 
      if (paramInt == 1 && constraintWidget.verticalGroup != -1)
        return constraintWidget.verticalGroup; 
    } 
    return -1;
  }
  
  public void removeAllIds() {
    this.mWidgetsCount = 0;
    Arrays.fill((Object[])this.mWidgets, (Object)null);
  }
  
  public void updateConstraints(ConstraintWidgetContainer paramConstraintWidgetContainer) {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\widgets\HelperWidget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */