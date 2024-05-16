package androidx.constraintlayout.core.widgets.analyzer;

import androidx.constraintlayout.core.widgets.Barrier;
import androidx.constraintlayout.core.widgets.ConstraintWidget;

class HelperReferences extends WidgetRun {
  public HelperReferences(ConstraintWidget paramConstraintWidget) {
    super(paramConstraintWidget);
  }
  
  private void addDependency(DependencyNode paramDependencyNode) {
    this.start.dependencies.add(paramDependencyNode);
    paramDependencyNode.targets.add(this.start);
  }
  
  void apply() {
    if (this.widget instanceof Barrier) {
      this.start.delegateToWidgetRun = true;
      Barrier barrier = (Barrier)this.widget;
      int i = barrier.getBarrierType();
      boolean bool = barrier.getAllowsGoneWidget();
      switch (i) {
        default:
          return;
        case 3:
          this.start.type = DependencyNode.Type.BOTTOM;
          for (i = 0; i < barrier.mWidgetsCount; i++) {
            ConstraintWidget constraintWidget = barrier.mWidgets[i];
            if (bool || constraintWidget.getVisibility() != 8) {
              DependencyNode dependencyNode = constraintWidget.verticalRun.end;
              dependencyNode.dependencies.add(this.start);
              this.start.targets.add(dependencyNode);
            } 
          } 
          addDependency(this.widget.verticalRun.start);
          addDependency(this.widget.verticalRun.end);
        case 2:
          this.start.type = DependencyNode.Type.TOP;
          for (i = 0; i < barrier.mWidgetsCount; i++) {
            ConstraintWidget constraintWidget = barrier.mWidgets[i];
            if (bool || constraintWidget.getVisibility() != 8) {
              DependencyNode dependencyNode = constraintWidget.verticalRun.start;
              dependencyNode.dependencies.add(this.start);
              this.start.targets.add(dependencyNode);
            } 
          } 
          addDependency(this.widget.verticalRun.start);
          addDependency(this.widget.verticalRun.end);
        case 1:
          this.start.type = DependencyNode.Type.RIGHT;
          for (i = 0; i < barrier.mWidgetsCount; i++) {
            ConstraintWidget constraintWidget = barrier.mWidgets[i];
            if (bool || constraintWidget.getVisibility() != 8) {
              DependencyNode dependencyNode = constraintWidget.horizontalRun.end;
              dependencyNode.dependencies.add(this.start);
              this.start.targets.add(dependencyNode);
            } 
          } 
          addDependency(this.widget.horizontalRun.start);
          addDependency(this.widget.horizontalRun.end);
        case 0:
          break;
      } 
      this.start.type = DependencyNode.Type.LEFT;
      for (i = 0; i < barrier.mWidgetsCount; i++) {
        ConstraintWidget constraintWidget = barrier.mWidgets[i];
        if (bool || constraintWidget.getVisibility() != 8) {
          DependencyNode dependencyNode = constraintWidget.horizontalRun.start;
          dependencyNode.dependencies.add(this.start);
          this.start.targets.add(dependencyNode);
        } 
      } 
      addDependency(this.widget.horizontalRun.start);
      addDependency(this.widget.horizontalRun.end);
    } 
  }
  
  public void applyToWidget() {
    if (this.widget instanceof Barrier) {
      int i = ((Barrier)this.widget).getBarrierType();
      if (i == 0 || i == 1) {
        this.widget.setX(this.start.value);
        return;
      } 
      this.widget.setY(this.start.value);
    } 
  }
  
  void clear() {
    this.runGroup = null;
    this.start.clear();
  }
  
  void reset() {
    this.start.resolved = false;
  }
  
  boolean supportsWrapComputation() {
    return false;
  }
  
  public void update(Dependency paramDependency) {
    // Byte code:
    //   0: aload_0
    //   1: getfield widget : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   4: checkcast androidx/constraintlayout/core/widgets/Barrier
    //   7: astore #8
    //   9: aload #8
    //   11: invokevirtual getBarrierType : ()I
    //   14: istore #7
    //   16: iconst_m1
    //   17: istore #5
    //   19: iconst_0
    //   20: istore_2
    //   21: aload_0
    //   22: getfield start : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   25: getfield targets : Ljava/util/List;
    //   28: invokeinterface iterator : ()Ljava/util/Iterator;
    //   33: astore_1
    //   34: aload_1
    //   35: invokeinterface hasNext : ()Z
    //   40: ifeq -> 98
    //   43: aload_1
    //   44: invokeinterface next : ()Ljava/lang/Object;
    //   49: checkcast androidx/constraintlayout/core/widgets/analyzer/DependencyNode
    //   52: getfield value : I
    //   55: istore #4
    //   57: iload #5
    //   59: iconst_m1
    //   60: if_icmpeq -> 73
    //   63: iload #5
    //   65: istore_3
    //   66: iload #4
    //   68: iload #5
    //   70: if_icmpge -> 76
    //   73: iload #4
    //   75: istore_3
    //   76: iload_2
    //   77: istore #6
    //   79: iload_2
    //   80: iload #4
    //   82: if_icmpge -> 89
    //   85: iload #4
    //   87: istore #6
    //   89: iload_3
    //   90: istore #5
    //   92: iload #6
    //   94: istore_2
    //   95: goto -> 34
    //   98: iload #7
    //   100: ifeq -> 129
    //   103: iload #7
    //   105: iconst_2
    //   106: if_icmpne -> 112
    //   109: goto -> 129
    //   112: aload_0
    //   113: getfield start : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   116: aload #8
    //   118: invokevirtual getMargin : ()I
    //   121: iload_2
    //   122: iadd
    //   123: invokevirtual resolve : (I)V
    //   126: goto -> 144
    //   129: aload_0
    //   130: getfield start : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   133: aload #8
    //   135: invokevirtual getMargin : ()I
    //   138: iload #5
    //   140: iadd
    //   141: invokevirtual resolve : (I)V
    //   144: return
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\widgets\analyzer\HelperReferences.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */