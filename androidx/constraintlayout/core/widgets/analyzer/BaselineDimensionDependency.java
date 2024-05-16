package androidx.constraintlayout.core.widgets.analyzer;

class BaselineDimensionDependency extends DimensionDependency {
  public BaselineDimensionDependency(WidgetRun paramWidgetRun) {
    super(paramWidgetRun);
  }
  
  public void update(DependencyNode paramDependencyNode) {
    ((VerticalWidgetRun)this.run).baseline.margin = this.run.widget.getBaselineDistance();
    this.resolved = true;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\widgets\analyzer\BaselineDimensionDependency.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */