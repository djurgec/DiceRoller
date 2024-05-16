package androidx.constraintlayout.core.widgets.analyzer;

import androidx.constraintlayout.core.Metrics;
import androidx.constraintlayout.core.widgets.Barrier;
import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.core.widgets.Guideline;
import androidx.constraintlayout.core.widgets.HelperWidget;
import java.util.ArrayList;
import java.util.Iterator;

public class Grouping {
  private static final boolean DEBUG = false;
  
  private static final boolean DEBUG_GROUPING = false;
  
  public static WidgetGroup findDependents(ConstraintWidget paramConstraintWidget, int paramInt, ArrayList<WidgetGroup> paramArrayList, WidgetGroup paramWidgetGroup) {
    int i;
    WidgetGroup widgetGroup;
    if (paramInt == 0) {
      i = paramConstraintWidget.horizontalGroup;
    } else {
      i = paramConstraintWidget.verticalGroup;
    } 
    if (i != -1 && (paramWidgetGroup == null || i != paramWidgetGroup.id)) {
      byte b = 0;
      while (true) {
        widgetGroup = paramWidgetGroup;
        if (b < paramArrayList.size()) {
          widgetGroup = paramArrayList.get(b);
          if (widgetGroup.getId() == i) {
            if (paramWidgetGroup != null) {
              paramWidgetGroup.moveTo(paramInt, widgetGroup);
              paramArrayList.remove(paramWidgetGroup);
            } 
            break;
          } 
          b++;
          continue;
        } 
        break;
      } 
    } else {
      widgetGroup = paramWidgetGroup;
      if (i != -1)
        return paramWidgetGroup; 
    } 
    paramWidgetGroup = widgetGroup;
    if (widgetGroup == null) {
      paramWidgetGroup = widgetGroup;
      if (paramConstraintWidget instanceof HelperWidget) {
        int j = ((HelperWidget)paramConstraintWidget).findGroupInDependents(paramInt);
        paramWidgetGroup = widgetGroup;
        if (j != -1) {
          i = 0;
          while (true) {
            paramWidgetGroup = widgetGroup;
            if (i < paramArrayList.size()) {
              paramWidgetGroup = paramArrayList.get(i);
              if (paramWidgetGroup.getId() == j)
                break; 
              i++;
              continue;
            } 
            break;
          } 
        } 
      } 
      widgetGroup = paramWidgetGroup;
      if (paramWidgetGroup == null)
        widgetGroup = new WidgetGroup(paramInt); 
      paramArrayList.add(widgetGroup);
      paramWidgetGroup = widgetGroup;
    } 
    if (paramWidgetGroup.add(paramConstraintWidget)) {
      if (paramConstraintWidget instanceof Guideline) {
        Guideline guideline = (Guideline)paramConstraintWidget;
        ConstraintAnchor constraintAnchor = guideline.getAnchor();
        if (guideline.getOrientation() == 0) {
          i = 1;
        } else {
          i = 0;
        } 
        constraintAnchor.findDependents(i, paramArrayList, paramWidgetGroup);
      } 
      if (paramInt == 0) {
        paramConstraintWidget.horizontalGroup = paramWidgetGroup.getId();
        paramConstraintWidget.mLeft.findDependents(paramInt, paramArrayList, paramWidgetGroup);
        paramConstraintWidget.mRight.findDependents(paramInt, paramArrayList, paramWidgetGroup);
      } else {
        paramConstraintWidget.verticalGroup = paramWidgetGroup.getId();
        paramConstraintWidget.mTop.findDependents(paramInt, paramArrayList, paramWidgetGroup);
        paramConstraintWidget.mBaseline.findDependents(paramInt, paramArrayList, paramWidgetGroup);
        paramConstraintWidget.mBottom.findDependents(paramInt, paramArrayList, paramWidgetGroup);
      } 
      paramConstraintWidget.mCenter.findDependents(paramInt, paramArrayList, paramWidgetGroup);
    } 
    return paramWidgetGroup;
  }
  
  private static WidgetGroup findGroup(ArrayList<WidgetGroup> paramArrayList, int paramInt) {
    int i = paramArrayList.size();
    for (byte b = 0; b < i; b++) {
      WidgetGroup widgetGroup = paramArrayList.get(b);
      if (paramInt == widgetGroup.id)
        return widgetGroup; 
    } 
    return null;
  }
  
  public static boolean simpleSolvingPass(ConstraintWidgetContainer paramConstraintWidgetContainer, BasicMeasure.Measurer paramMeasurer) {
    ArrayList<Guideline> arrayList1;
    ArrayList<ConstraintWidget> arrayList8 = paramConstraintWidgetContainer.getChildren();
    int j = arrayList8.size();
    ArrayList<Guideline> arrayList5 = null;
    ArrayList<Guideline> arrayList3 = null;
    ArrayList<Barrier> arrayList4 = null;
    ArrayList<Barrier> arrayList2 = null;
    ArrayList<Barrier> arrayList7 = null;
    ArrayList<Barrier> arrayList6 = null;
    int i;
    for (i = 0; i < j; i++) {
      ConstraintWidget constraintWidget = arrayList8.get(i);
      if (!validInGroup(paramConstraintWidgetContainer.getHorizontalDimensionBehaviour(), paramConstraintWidgetContainer.getVerticalDimensionBehaviour(), constraintWidget.getHorizontalDimensionBehaviour(), constraintWidget.getVerticalDimensionBehaviour()))
        return false; 
      if (constraintWidget instanceof androidx.constraintlayout.core.widgets.Flow)
        return false; 
    } 
    if (paramConstraintWidgetContainer.mMetrics != null) {
      Metrics metrics = paramConstraintWidgetContainer.mMetrics;
      metrics.grouping++;
    } 
    i = 0;
    while (i < j) {
      ConstraintWidget constraintWidget = arrayList8.get(i);
      if (!validInGroup(paramConstraintWidgetContainer.getHorizontalDimensionBehaviour(), paramConstraintWidgetContainer.getVerticalDimensionBehaviour(), constraintWidget.getHorizontalDimensionBehaviour(), constraintWidget.getVerticalDimensionBehaviour()))
        ConstraintWidgetContainer.measure(0, constraintWidget, paramMeasurer, paramConstraintWidgetContainer.mMeasure, BasicMeasure.Measure.SELF_DIMENSIONS); 
      ArrayList<Guideline> arrayList12 = arrayList5;
      ArrayList<Guideline> arrayList11 = arrayList3;
      if (constraintWidget instanceof Guideline) {
        Guideline guideline = (Guideline)constraintWidget;
        ArrayList<Guideline> arrayList15 = arrayList3;
        if (guideline.getOrientation() == 0) {
          arrayList15 = arrayList3;
          if (arrayList3 == null)
            arrayList15 = new ArrayList(); 
          arrayList15.add(guideline);
        } 
        arrayList12 = arrayList5;
        arrayList11 = arrayList15;
        if (guideline.getOrientation() == 1) {
          arrayList3 = arrayList5;
          if (arrayList5 == null)
            arrayList3 = new ArrayList<>(); 
          arrayList3.add(guideline);
          arrayList11 = arrayList15;
          arrayList12 = arrayList3;
        } 
      } 
      ArrayList<Barrier> arrayList10 = arrayList4;
      ArrayList<Barrier> arrayList9 = arrayList2;
      if (constraintWidget instanceof HelperWidget)
        if (constraintWidget instanceof Barrier) {
          Barrier barrier = (Barrier)constraintWidget;
          arrayList10 = arrayList4;
          if (barrier.getOrientation() == 0) {
            arrayList10 = arrayList4;
            if (arrayList4 == null)
              arrayList10 = new ArrayList(); 
            arrayList10.add(barrier);
          } 
          arrayList9 = arrayList2;
          if (barrier.getOrientation() == 1) {
            arrayList9 = arrayList2;
            if (arrayList2 == null)
              arrayList9 = new ArrayList(); 
            arrayList9.add(barrier);
          } 
        } else {
          HelperWidget helperWidget = (HelperWidget)constraintWidget;
          arrayList10 = arrayList4;
          if (arrayList4 == null)
            arrayList10 = new ArrayList<>(); 
          arrayList10.add(helperWidget);
          arrayList9 = arrayList2;
          if (arrayList2 == null)
            arrayList9 = new ArrayList<>(); 
          arrayList9.add(helperWidget);
        }  
      ArrayList<Barrier> arrayList13 = arrayList7;
      if (constraintWidget.mLeft.mTarget == null) {
        arrayList13 = arrayList7;
        if (constraintWidget.mRight.mTarget == null) {
          arrayList13 = arrayList7;
          if (!(constraintWidget instanceof Guideline)) {
            arrayList13 = arrayList7;
            if (!(constraintWidget instanceof Barrier)) {
              arrayList2 = arrayList7;
              if (arrayList7 == null)
                arrayList2 = new ArrayList<>(); 
              arrayList2.add(constraintWidget);
              arrayList13 = arrayList2;
            } 
          } 
        } 
      } 
      ArrayList<Barrier> arrayList14 = arrayList6;
      if (constraintWidget.mTop.mTarget == null) {
        arrayList14 = arrayList6;
        if (constraintWidget.mBottom.mTarget == null) {
          arrayList14 = arrayList6;
          if (constraintWidget.mBaseline.mTarget == null) {
            arrayList14 = arrayList6;
            if (!(constraintWidget instanceof Guideline)) {
              arrayList14 = arrayList6;
              if (!(constraintWidget instanceof Barrier)) {
                arrayList2 = arrayList6;
                if (arrayList6 == null)
                  arrayList2 = new ArrayList<>(); 
                arrayList2.add(constraintWidget);
                arrayList14 = arrayList2;
              } 
            } 
          } 
        } 
      } 
      i++;
      arrayList5 = arrayList12;
      arrayList3 = arrayList11;
      arrayList4 = arrayList10;
      arrayList2 = arrayList9;
      arrayList7 = arrayList13;
      arrayList6 = arrayList14;
    } 
    ArrayList<WidgetGroup> arrayList = new ArrayList();
    if (arrayList5 != null) {
      Iterator<Guideline> iterator = arrayList5.iterator();
      while (iterator.hasNext())
        findDependents((ConstraintWidget)iterator.next(), 0, arrayList, null); 
    } 
    if (arrayList4 != null)
      for (HelperWidget helperWidget : arrayList4) {
        WidgetGroup widgetGroup = findDependents((ConstraintWidget)helperWidget, 0, arrayList, null);
        helperWidget.addDependents(arrayList, 0, widgetGroup);
        widgetGroup.cleanup(arrayList);
      }  
    ConstraintAnchor constraintAnchor = paramConstraintWidgetContainer.getAnchor(ConstraintAnchor.Type.LEFT);
    if (constraintAnchor.getDependents() != null) {
      Iterator iterator = constraintAnchor.getDependents().iterator();
      while (iterator.hasNext())
        findDependents(((ConstraintAnchor)iterator.next()).mOwner, 0, arrayList, null); 
    } 
    constraintAnchor = paramConstraintWidgetContainer.getAnchor(ConstraintAnchor.Type.RIGHT);
    if (constraintAnchor.getDependents() != null) {
      Iterator iterator = constraintAnchor.getDependents().iterator();
      while (iterator.hasNext())
        findDependents(((ConstraintAnchor)iterator.next()).mOwner, 0, arrayList, null); 
    } 
    constraintAnchor = paramConstraintWidgetContainer.getAnchor(ConstraintAnchor.Type.CENTER);
    if (constraintAnchor.getDependents() != null) {
      Iterator iterator = constraintAnchor.getDependents().iterator();
      while (iterator.hasNext())
        findDependents(((ConstraintAnchor)iterator.next()).mOwner, 0, arrayList, null); 
    } 
    if (arrayList7 != null) {
      Iterator<Barrier> iterator = arrayList7.iterator();
      while (iterator.hasNext())
        findDependents((ConstraintWidget)iterator.next(), 0, arrayList, null); 
    } 
    if (arrayList3 != null) {
      Iterator<Guideline> iterator = arrayList3.iterator();
      while (iterator.hasNext())
        findDependents((ConstraintWidget)iterator.next(), 1, arrayList, null); 
    } 
    if (arrayList2 != null)
      for (HelperWidget helperWidget : arrayList2) {
        WidgetGroup widgetGroup = findDependents((ConstraintWidget)helperWidget, 1, arrayList, null);
        helperWidget.addDependents(arrayList, 1, widgetGroup);
        widgetGroup.cleanup(arrayList);
      }  
    constraintAnchor = paramConstraintWidgetContainer.getAnchor(ConstraintAnchor.Type.TOP);
    if (constraintAnchor.getDependents() != null) {
      Iterator iterator = constraintAnchor.getDependents().iterator();
      while (iterator.hasNext())
        findDependents(((ConstraintAnchor)iterator.next()).mOwner, 1, arrayList, null); 
    } 
    constraintAnchor = paramConstraintWidgetContainer.getAnchor(ConstraintAnchor.Type.BASELINE);
    if (constraintAnchor.getDependents() != null) {
      Iterator iterator = constraintAnchor.getDependents().iterator();
      while (iterator.hasNext())
        findDependents(((ConstraintAnchor)iterator.next()).mOwner, 1, arrayList, null); 
    } 
    constraintAnchor = paramConstraintWidgetContainer.getAnchor(ConstraintAnchor.Type.BOTTOM);
    if (constraintAnchor.getDependents() != null) {
      Iterator iterator = constraintAnchor.getDependents().iterator();
      while (iterator.hasNext())
        findDependents(((ConstraintAnchor)iterator.next()).mOwner, 1, arrayList, null); 
    } 
    constraintAnchor = paramConstraintWidgetContainer.getAnchor(ConstraintAnchor.Type.CENTER);
    if (constraintAnchor.getDependents() != null) {
      Iterator iterator = constraintAnchor.getDependents().iterator();
      while (iterator.hasNext())
        findDependents(((ConstraintAnchor)iterator.next()).mOwner, 1, arrayList, null); 
    } 
    if (arrayList6 != null) {
      Iterator<Barrier> iterator = arrayList6.iterator();
      while (iterator.hasNext())
        findDependents((ConstraintWidget)iterator.next(), 1, arrayList, null); 
    } 
    for (i = 0; i < j; i++) {
      ConstraintWidget constraintWidget = arrayList8.get(i);
      if (constraintWidget.oppositeDimensionsTied()) {
        WidgetGroup widgetGroup1 = findGroup(arrayList, constraintWidget.horizontalGroup);
        WidgetGroup widgetGroup2 = findGroup(arrayList, constraintWidget.verticalGroup);
        if (widgetGroup1 != null && widgetGroup2 != null) {
          widgetGroup1.moveTo(0, widgetGroup2);
          widgetGroup2.setOrientation(2);
          arrayList.remove(widgetGroup1);
        } 
      } 
    } 
    if (arrayList.size() <= 1)
      return false; 
    arrayList4 = null;
    arrayList3 = null;
    if (paramConstraintWidgetContainer.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
      WidgetGroup widgetGroup;
      j = 0;
      constraintAnchor = null;
      Iterator<WidgetGroup> iterator = arrayList.iterator();
      ArrayList<ConstraintWidget> arrayList10 = arrayList8;
      while (iterator.hasNext()) {
        WidgetGroup widgetGroup1 = iterator.next();
        if (widgetGroup1.getOrientation() == 1)
          continue; 
        widgetGroup1.setAuthoritative(false);
        int k = widgetGroup1.measureWrap(paramConstraintWidgetContainer.getSystem(), 0);
        i = j;
        if (k > j) {
          i = k;
          widgetGroup = widgetGroup1;
        } 
        j = i;
      } 
      ArrayList<Barrier> arrayList9 = arrayList4;
      if (widgetGroup != null) {
        paramConstraintWidgetContainer.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
        paramConstraintWidgetContainer.setWidth(j);
        widgetGroup.setAuthoritative(true);
        WidgetGroup widgetGroup1 = widgetGroup;
      } 
    } else {
      arrayList2 = arrayList4;
    } 
    if (paramConstraintWidgetContainer.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
      WidgetGroup widgetGroup;
      i = 0;
      constraintAnchor = null;
      for (WidgetGroup widgetGroup1 : arrayList) {
        if (widgetGroup1.getOrientation() == 0)
          continue; 
        widgetGroup1.setAuthoritative(false);
        int k = widgetGroup1.measureWrap(paramConstraintWidgetContainer.getSystem(), 1);
        j = i;
        if (k > i) {
          widgetGroup = widgetGroup1;
          j = k;
        } 
        i = j;
      } 
      if (widgetGroup != null) {
        paramConstraintWidgetContainer.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
        paramConstraintWidgetContainer.setHeight(i);
        widgetGroup.setAuthoritative(true);
        WidgetGroup widgetGroup1 = widgetGroup;
      } else {
        arrayList1 = arrayList3;
      } 
    } else {
      arrayList1 = arrayList3;
    } 
    return (arrayList2 != null || arrayList1 != null);
  }
  
  public static boolean validInGroup(ConstraintWidget.DimensionBehaviour paramDimensionBehaviour1, ConstraintWidget.DimensionBehaviour paramDimensionBehaviour2, ConstraintWidget.DimensionBehaviour paramDimensionBehaviour3, ConstraintWidget.DimensionBehaviour paramDimensionBehaviour4) {
    boolean bool1;
    boolean bool2;
    if (paramDimensionBehaviour3 == ConstraintWidget.DimensionBehaviour.FIXED || paramDimensionBehaviour3 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || (paramDimensionBehaviour3 == ConstraintWidget.DimensionBehaviour.MATCH_PARENT && paramDimensionBehaviour1 != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (paramDimensionBehaviour4 == ConstraintWidget.DimensionBehaviour.FIXED || paramDimensionBehaviour4 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || (paramDimensionBehaviour4 == ConstraintWidget.DimensionBehaviour.MATCH_PARENT && paramDimensionBehaviour2 != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    return (bool1 || bool2);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\widgets\analyzer\Grouping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */