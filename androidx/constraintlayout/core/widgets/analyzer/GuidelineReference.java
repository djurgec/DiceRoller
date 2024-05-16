package androidx.constraintlayout.core.widgets.analyzer;

import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.Guideline;

class GuidelineReference extends WidgetRun {
  public GuidelineReference(ConstraintWidget paramConstraintWidget) {
    super(paramConstraintWidget);
    paramConstraintWidget.horizontalRun.clear();
    paramConstraintWidget.verticalRun.clear();
    this.orientation = ((Guideline)paramConstraintWidget).getOrientation();
  }
  
  private void addDependency(DependencyNode paramDependencyNode) {
    this.start.dependencies.add(paramDependencyNode);
    paramDependencyNode.targets.add(this.start);
  }
  
  void apply() {
    Guideline guideline = (Guideline)this.widget;
    int j = guideline.getRelativeBegin();
    int i = guideline.getRelativeEnd();
    guideline.getRelativePercent();
    if (guideline.getOrientation() == 1) {
      if (j != -1) {
        this.start.targets.add(this.widget.mParent.horizontalRun.start);
        this.widget.mParent.horizontalRun.start.dependencies.add(this.start);
        this.start.margin = j;
      } else if (i != -1) {
        this.start.targets.add(this.widget.mParent.horizontalRun.end);
        this.widget.mParent.horizontalRun.end.dependencies.add(this.start);
        this.start.margin = -i;
      } else {
        this.start.delegateToWidgetRun = true;
        this.start.targets.add(this.widget.mParent.horizontalRun.end);
        this.widget.mParent.horizontalRun.end.dependencies.add(this.start);
      } 
      addDependency(this.widget.horizontalRun.start);
      addDependency(this.widget.horizontalRun.end);
    } else {
      if (j != -1) {
        this.start.targets.add(this.widget.mParent.verticalRun.start);
        this.widget.mParent.verticalRun.start.dependencies.add(this.start);
        this.start.margin = j;
      } else if (i != -1) {
        this.start.targets.add(this.widget.mParent.verticalRun.end);
        this.widget.mParent.verticalRun.end.dependencies.add(this.start);
        this.start.margin = -i;
      } else {
        this.start.delegateToWidgetRun = true;
        this.start.targets.add(this.widget.mParent.verticalRun.end);
        this.widget.mParent.verticalRun.end.dependencies.add(this.start);
      } 
      addDependency(this.widget.verticalRun.start);
      addDependency(this.widget.verticalRun.end);
    } 
  }
  
  public void applyToWidget() {
    if (((Guideline)this.widget).getOrientation() == 1) {
      this.widget.setX(this.start.value);
    } else {
      this.widget.setY(this.start.value);
    } 
  }
  
  void clear() {
    this.start.clear();
  }
  
  void reset() {
    this.start.resolved = false;
    this.end.resolved = false;
  }
  
  boolean supportsWrapComputation() {
    return false;
  }
  
  public void update(Dependency paramDependency) {
    if (!this.start.readyToSolve)
      return; 
    if (this.start.resolved)
      return; 
    paramDependency = this.start.targets.get(0);
    Guideline guideline = (Guideline)this.widget;
    int i = (int)(((DependencyNode)paramDependency).value * guideline.getRelativePercent() + 0.5F);
    this.start.resolve(i);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\widgets\analyzer\GuidelineReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */