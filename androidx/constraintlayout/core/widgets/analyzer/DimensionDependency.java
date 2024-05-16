package androidx.constraintlayout.core.widgets.analyzer;

class DimensionDependency extends DependencyNode {
  public int wrapValue;
  
  public DimensionDependency(WidgetRun paramWidgetRun) {
    super(paramWidgetRun);
    if (paramWidgetRun instanceof HorizontalWidgetRun) {
      this.type = DependencyNode.Type.HORIZONTAL_DIMENSION;
    } else {
      this.type = DependencyNode.Type.VERTICAL_DIMENSION;
    } 
  }
  
  public void resolve(int paramInt) {
    if (this.resolved)
      return; 
    this.resolved = true;
    this.value = paramInt;
    for (Dependency dependency : this.dependencies)
      dependency.update(dependency); 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\widgets\analyzer\DimensionDependency.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */