package androidx.constraintlayout.core.widgets.analyzer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DependencyNode implements Dependency {
  public boolean delegateToWidgetRun = false;
  
  List<Dependency> dependencies = new ArrayList<>();
  
  int margin;
  
  DimensionDependency marginDependency = null;
  
  int marginFactor = 1;
  
  public boolean readyToSolve = false;
  
  public boolean resolved = false;
  
  WidgetRun run;
  
  List<DependencyNode> targets = new ArrayList<>();
  
  Type type = Type.UNKNOWN;
  
  public Dependency updateDelegate = null;
  
  public int value;
  
  public DependencyNode(WidgetRun paramWidgetRun) {
    this.run = paramWidgetRun;
  }
  
  public void addDependency(Dependency paramDependency) {
    this.dependencies.add(paramDependency);
    if (this.resolved)
      paramDependency.update(paramDependency); 
  }
  
  public void clear() {
    this.targets.clear();
    this.dependencies.clear();
    this.resolved = false;
    this.value = 0;
    this.readyToSolve = false;
    this.delegateToWidgetRun = false;
  }
  
  public String name() {
    String str = this.run.widget.getDebugName();
    if (this.type == Type.LEFT || this.type == Type.RIGHT) {
      str = str + "_HORIZONTAL";
      return str + ":" + this.type.name();
    } 
    str = str + "_VERTICAL";
    return str + ":" + this.type.name();
  }
  
  public void resolve(int paramInt) {
    if (this.resolved)
      return; 
    this.resolved = true;
    this.value = paramInt;
    for (Dependency dependency : this.dependencies)
      dependency.update(dependency); 
  }
  
  public String toString() {
    String str;
    StringBuilder stringBuilder = (new StringBuilder()).append(this.run.widget.getDebugName()).append(":").append(this.type).append("(");
    if (this.resolved) {
      Integer integer = Integer.valueOf(this.value);
    } else {
      str = "unresolved";
    } 
    return stringBuilder.append(str).append(") <t=").append(this.targets.size()).append(":d=").append(this.dependencies.size()).append(">").toString();
  }
  
  public void update(Dependency paramDependency) {
    Iterator<DependencyNode> iterator = this.targets.iterator();
    while (iterator.hasNext()) {
      if (!((DependencyNode)iterator.next()).resolved)
        return; 
    } 
    this.readyToSolve = true;
    Dependency dependency = this.updateDelegate;
    if (dependency != null)
      dependency.update(this); 
    if (this.delegateToWidgetRun) {
      this.run.update(this);
      return;
    } 
    dependency = null;
    byte b = 0;
    for (DependencyNode dependencyNode : this.targets) {
      if (dependencyNode instanceof DimensionDependency)
        continue; 
      dependency = dependencyNode;
      b++;
    } 
    if (dependency != null && b == 1 && ((DependencyNode)dependency).resolved) {
      DimensionDependency dimensionDependency = this.marginDependency;
      if (dimensionDependency != null)
        if (dimensionDependency.resolved) {
          this.margin = this.marginFactor * this.marginDependency.value;
        } else {
          return;
        }  
      resolve(((DependencyNode)dependency).value + this.margin);
    } 
    dependency = this.updateDelegate;
    if (dependency != null)
      dependency.update(this); 
  }
  
  enum Type {
    BASELINE, BOTTOM, HORIZONTAL_DIMENSION, LEFT, RIGHT, TOP, UNKNOWN, VERTICAL_DIMENSION;
    
    private static final Type[] $VALUES;
    
    static {
      Type type3 = new Type("UNKNOWN", 0);
      UNKNOWN = type3;
      Type type4 = new Type("HORIZONTAL_DIMENSION", 1);
      HORIZONTAL_DIMENSION = type4;
      Type type5 = new Type("VERTICAL_DIMENSION", 2);
      VERTICAL_DIMENSION = type5;
      Type type7 = new Type("LEFT", 3);
      LEFT = type7;
      Type type8 = new Type("RIGHT", 4);
      RIGHT = type8;
      Type type1 = new Type("TOP", 5);
      TOP = type1;
      Type type6 = new Type("BOTTOM", 6);
      BOTTOM = type6;
      Type type2 = new Type("BASELINE", 7);
      BASELINE = type2;
      $VALUES = new Type[] { type3, type4, type5, type7, type8, type1, type6, type2 };
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\widgets\analyzer\DependencyNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */