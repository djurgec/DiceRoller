package androidx.constraintlayout.core.widgets.analyzer;

import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import java.util.ArrayList;
import java.util.Iterator;

class RunGroup {
  public static final int BASELINE = 2;
  
  public static final int END = 1;
  
  public static final int START = 0;
  
  public static int index;
  
  int direction;
  
  public boolean dual = false;
  
  WidgetRun firstRun = null;
  
  int groupIndex = 0;
  
  WidgetRun lastRun = null;
  
  public int position = 0;
  
  ArrayList<WidgetRun> runs = new ArrayList<>();
  
  public RunGroup(WidgetRun paramWidgetRun, int paramInt) {
    int i = index;
    this.groupIndex = i;
    index = i + 1;
    this.firstRun = paramWidgetRun;
    this.lastRun = paramWidgetRun;
    this.direction = paramInt;
  }
  
  private boolean defineTerminalWidget(WidgetRun paramWidgetRun, int paramInt) {
    if (!paramWidgetRun.widget.isTerminalWidget[paramInt])
      return false; 
    for (Dependency dependency : paramWidgetRun.start.dependencies) {
      if (dependency instanceof DependencyNode) {
        DependencyNode dependencyNode = (DependencyNode)dependency;
        if (dependencyNode.run != paramWidgetRun && dependencyNode == dependencyNode.run.start) {
          if (paramWidgetRun instanceof ChainRun) {
            Iterator<WidgetRun> iterator = ((ChainRun)paramWidgetRun).widgets.iterator();
            while (iterator.hasNext())
              defineTerminalWidget(iterator.next(), paramInt); 
          } else if (!(paramWidgetRun instanceof HelperReferences)) {
            paramWidgetRun.widget.isTerminalWidget[paramInt] = false;
          } 
          defineTerminalWidget(dependencyNode.run, paramInt);
        } 
      } 
    } 
    for (Dependency dependency : paramWidgetRun.end.dependencies) {
      if (dependency instanceof DependencyNode) {
        dependency = dependency;
        if (((DependencyNode)dependency).run != paramWidgetRun && dependency == ((DependencyNode)dependency).run.start) {
          if (paramWidgetRun instanceof ChainRun) {
            Iterator<WidgetRun> iterator = ((ChainRun)paramWidgetRun).widgets.iterator();
            while (iterator.hasNext())
              defineTerminalWidget(iterator.next(), paramInt); 
          } else if (!(paramWidgetRun instanceof HelperReferences)) {
            paramWidgetRun.widget.isTerminalWidget[paramInt] = false;
          } 
          defineTerminalWidget(((DependencyNode)dependency).run, paramInt);
        } 
      } 
    } 
    return false;
  }
  
  private long traverseEnd(DependencyNode paramDependencyNode, long paramLong) {
    WidgetRun widgetRun = paramDependencyNode.run;
    if (widgetRun instanceof HelperReferences)
      return paramLong; 
    long l1 = paramLong;
    int i = paramDependencyNode.dependencies.size();
    byte b = 0;
    while (b < i) {
      Dependency dependency = paramDependencyNode.dependencies.get(b);
      long l = l1;
      if (dependency instanceof DependencyNode) {
        dependency = dependency;
        if (((DependencyNode)dependency).run == widgetRun) {
          l = l1;
        } else {
          l = Math.min(l1, traverseEnd((DependencyNode)dependency, ((DependencyNode)dependency).margin + paramLong));
        } 
      } 
      b++;
      l1 = l;
    } 
    long l2 = l1;
    if (paramDependencyNode == widgetRun.end) {
      l2 = widgetRun.getWrapDimension();
      l2 = Math.min(Math.min(l1, traverseEnd(widgetRun.start, paramLong - l2)), paramLong - l2 - widgetRun.start.margin);
    } 
    return l2;
  }
  
  private long traverseStart(DependencyNode paramDependencyNode, long paramLong) {
    WidgetRun widgetRun = paramDependencyNode.run;
    if (widgetRun instanceof HelperReferences)
      return paramLong; 
    long l1 = paramLong;
    int i = paramDependencyNode.dependencies.size();
    byte b = 0;
    while (b < i) {
      Dependency dependency = paramDependencyNode.dependencies.get(b);
      long l = l1;
      if (dependency instanceof DependencyNode) {
        dependency = dependency;
        if (((DependencyNode)dependency).run == widgetRun) {
          l = l1;
        } else {
          l = Math.max(l1, traverseStart((DependencyNode)dependency, ((DependencyNode)dependency).margin + paramLong));
        } 
      } 
      b++;
      l1 = l;
    } 
    long l2 = l1;
    if (paramDependencyNode == widgetRun.start) {
      l2 = widgetRun.getWrapDimension();
      l2 = Math.max(Math.max(l1, traverseStart(widgetRun.end, paramLong + l2)), paramLong + l2 - widgetRun.end.margin);
    } 
    return l2;
  }
  
  public void add(WidgetRun paramWidgetRun) {
    this.runs.add(paramWidgetRun);
    this.lastRun = paramWidgetRun;
  }
  
  public long computeWrapSize(ConstraintWidgetContainer paramConstraintWidgetContainer, int paramInt) {
    DependencyNode dependencyNode1;
    long l1;
    DependencyNode dependencyNode2;
    WidgetRun widgetRun = this.firstRun;
    if (widgetRun instanceof ChainRun) {
      if (((ChainRun)widgetRun).orientation != paramInt)
        return 0L; 
    } else if (paramInt == 0) {
      if (!(widgetRun instanceof HorizontalWidgetRun))
        return 0L; 
    } else if (!(widgetRun instanceof VerticalWidgetRun)) {
      return 0L;
    } 
    if (paramInt == 0) {
      dependencyNode2 = paramConstraintWidgetContainer.horizontalRun.start;
    } else {
      dependencyNode2 = paramConstraintWidgetContainer.verticalRun.start;
    } 
    if (paramInt == 0) {
      dependencyNode1 = paramConstraintWidgetContainer.horizontalRun.end;
    } else {
      dependencyNode1 = ((ConstraintWidgetContainer)dependencyNode1).verticalRun.end;
    } 
    boolean bool1 = this.firstRun.start.targets.contains(dependencyNode2);
    boolean bool2 = this.firstRun.end.targets.contains(dependencyNode1);
    long l2 = this.firstRun.getWrapDimension();
    if (bool1 && bool2) {
      l1 = traverseStart(this.firstRun.start, 0L);
      long l4 = traverseEnd(this.firstRun.end, 0L);
      long l3 = l1 - l2;
      l1 = l3;
      if (l3 >= -this.firstRun.end.margin)
        l1 = l3 + this.firstRun.end.margin; 
      l4 = -l4 - l2 - this.firstRun.start.margin;
      l3 = l4;
      if (l4 >= this.firstRun.start.margin)
        l3 = l4 - this.firstRun.start.margin; 
      float f = this.firstRun.widget.getBiasPercent(paramInt);
      if (f > 0.0F) {
        l1 = (long)((float)l3 / f + (float)l1 / (1.0F - f));
      } else {
        l1 = 0L;
      } 
      l3 = (long)((float)l1 * f + 0.5F);
      l1 = (long)((float)l1 * (1.0F - f) + 0.5F);
      l1 = this.firstRun.start.margin + l3 + l2 + l1 - this.firstRun.end.margin;
    } else if (bool1) {
      l1 = Math.max(traverseStart(this.firstRun.start, this.firstRun.start.margin), this.firstRun.start.margin + l2);
    } else if (bool2) {
      l1 = traverseEnd(this.firstRun.end, this.firstRun.end.margin);
      long l = -this.firstRun.end.margin;
      l1 = Math.max(-l1, l + l2);
    } else {
      l1 = this.firstRun.start.margin + this.firstRun.getWrapDimension() - this.firstRun.end.margin;
    } 
    return l1;
  }
  
  public void defineTerminalWidgets(boolean paramBoolean1, boolean paramBoolean2) {
    if (paramBoolean1) {
      WidgetRun widgetRun = this.firstRun;
      if (widgetRun instanceof HorizontalWidgetRun)
        defineTerminalWidget(widgetRun, 0); 
    } 
    if (paramBoolean2) {
      WidgetRun widgetRun = this.firstRun;
      if (widgetRun instanceof VerticalWidgetRun)
        defineTerminalWidget(widgetRun, 1); 
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\widgets\analyzer\RunGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */