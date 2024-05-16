package androidx.constraintlayout.core.widgets.analyzer;

import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;

public abstract class WidgetRun implements Dependency {
  DimensionDependency dimension = new DimensionDependency(this);
  
  protected ConstraintWidget.DimensionBehaviour dimensionBehavior;
  
  public DependencyNode end = new DependencyNode(this);
  
  protected RunType mRunType = RunType.NONE;
  
  public int matchConstraintsType;
  
  public int orientation = 0;
  
  boolean resolved = false;
  
  RunGroup runGroup;
  
  public DependencyNode start = new DependencyNode(this);
  
  ConstraintWidget widget;
  
  public WidgetRun(ConstraintWidget paramConstraintWidget) {
    this.widget = paramConstraintWidget;
  }
  
  private void resolveDimension(int paramInt1, int paramInt2) {
    ConstraintWidget constraintWidget;
    switch (this.matchConstraintsType) {
      default:
        return;
      case 3:
        if (this.widget.horizontalRun.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || this.widget.horizontalRun.matchConstraintsType != 3 || this.widget.verticalRun.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || this.widget.verticalRun.matchConstraintsType != 3) {
          VerticalWidgetRun verticalWidgetRun;
          HorizontalWidgetRun horizontalWidgetRun;
          ConstraintWidget constraintWidget1 = this.widget;
          if (paramInt1 == 0) {
            verticalWidgetRun = constraintWidget1.verticalRun;
          } else {
            horizontalWidgetRun = ((ConstraintWidget)verticalWidgetRun).horizontalRun;
          } 
          if (horizontalWidgetRun.dimension.resolved) {
            float f = this.widget.getDimensionRatio();
            if (paramInt1 == 1) {
              paramInt1 = (int)(horizontalWidgetRun.dimension.value / f + 0.5F);
            } else {
              paramInt1 = (int)(horizontalWidgetRun.dimension.value * f + 0.5F);
            } 
            this.dimension.resolve(paramInt1);
          } 
        } 
      case 2:
        constraintWidget = this.widget.getParent();
        if (constraintWidget != null) {
          HorizontalWidgetRun horizontalWidgetRun;
          VerticalWidgetRun verticalWidgetRun;
          if (paramInt1 == 0) {
            horizontalWidgetRun = constraintWidget.horizontalRun;
          } else {
            verticalWidgetRun = ((ConstraintWidget)horizontalWidgetRun).verticalRun;
          } 
          if (verticalWidgetRun.dimension.resolved) {
            float f;
            ConstraintWidget constraintWidget1 = this.widget;
            if (paramInt1 == 0) {
              f = constraintWidget1.mMatchConstraintPercentWidth;
            } else {
              f = constraintWidget1.mMatchConstraintPercentHeight;
            } 
            paramInt2 = (int)(verticalWidgetRun.dimension.value * f + 0.5F);
            this.dimension.resolve(getLimitedDimension(paramInt2, paramInt1));
          } 
        } 
      case 1:
        paramInt1 = getLimitedDimension(this.dimension.wrapValue, paramInt1);
        this.dimension.resolve(Math.min(paramInt1, paramInt2));
      case 0:
        break;
    } 
    this.dimension.resolve(getLimitedDimension(paramInt2, paramInt1));
  }
  
  protected final void addTarget(DependencyNode paramDependencyNode1, DependencyNode paramDependencyNode2, int paramInt) {
    paramDependencyNode1.targets.add(paramDependencyNode2);
    paramDependencyNode1.margin = paramInt;
    paramDependencyNode2.dependencies.add(paramDependencyNode1);
  }
  
  protected final void addTarget(DependencyNode paramDependencyNode1, DependencyNode paramDependencyNode2, int paramInt, DimensionDependency paramDimensionDependency) {
    paramDependencyNode1.targets.add(paramDependencyNode2);
    paramDependencyNode1.targets.add(this.dimension);
    paramDependencyNode1.marginFactor = paramInt;
    paramDependencyNode1.marginDependency = paramDimensionDependency;
    paramDependencyNode2.dependencies.add(paramDependencyNode1);
    paramDimensionDependency.dependencies.add(paramDependencyNode1);
  }
  
  abstract void apply();
  
  abstract void applyToWidget();
  
  abstract void clear();
  
  protected final int getLimitedDimension(int paramInt1, int paramInt2) {
    int i;
    if (paramInt2 == 0) {
      i = this.widget.mMatchConstraintMaxWidth;
      paramInt2 = Math.max(this.widget.mMatchConstraintMinWidth, paramInt1);
      if (i > 0)
        paramInt2 = Math.min(i, paramInt1); 
      i = paramInt1;
      if (paramInt2 != paramInt1)
        i = paramInt2; 
    } else {
      i = this.widget.mMatchConstraintMaxHeight;
      paramInt2 = Math.max(this.widget.mMatchConstraintMinHeight, paramInt1);
      if (i > 0)
        paramInt2 = Math.min(i, paramInt1); 
      i = paramInt1;
      if (paramInt2 != paramInt1)
        i = paramInt2; 
    } 
    return i;
  }
  
  protected final DependencyNode getTarget(ConstraintAnchor paramConstraintAnchor) {
    if (paramConstraintAnchor.mTarget == null)
      return null; 
    ConstraintAnchor.Type type = null;
    ConstraintWidget constraintWidget = paramConstraintAnchor.mTarget.mOwner;
    null = paramConstraintAnchor.mTarget.mType;
    switch (null) {
      default:
        return (DependencyNode)type;
      case null:
        return constraintWidget.verticalRun.end;
      case null:
        return constraintWidget.verticalRun.baseline;
      case null:
        return constraintWidget.verticalRun.start;
      case null:
        return constraintWidget.horizontalRun.end;
      case null:
        break;
    } 
    return constraintWidget.horizontalRun.start;
  }
  
  protected final DependencyNode getTarget(ConstraintAnchor paramConstraintAnchor, int paramInt) {
    HorizontalWidgetRun horizontalWidgetRun;
    VerticalWidgetRun verticalWidgetRun;
    if (paramConstraintAnchor.mTarget == null)
      return null; 
    ConstraintAnchor.Type type = null;
    ConstraintWidget constraintWidget = paramConstraintAnchor.mTarget.mOwner;
    if (paramInt == 0) {
      horizontalWidgetRun = constraintWidget.horizontalRun;
    } else {
      verticalWidgetRun = ((ConstraintWidget)horizontalWidgetRun).verticalRun;
    } 
    null = paramConstraintAnchor.mTarget.mType;
    switch (null) {
      default:
        return (DependencyNode)type;
      case null:
      case null:
        return verticalWidgetRun.end;
      case null:
      case null:
        break;
    } 
    return verticalWidgetRun.start;
  }
  
  public long getWrapDimension() {
    return this.dimension.resolved ? this.dimension.value : 0L;
  }
  
  public boolean isCenterConnection() {
    boolean bool;
    int i = 0;
    int j = this.start.targets.size();
    byte b = 0;
    while (b < j) {
      int k = i;
      if (((DependencyNode)this.start.targets.get(b)).run != this)
        k = i + 1; 
      b++;
      i = k;
    } 
    j = this.end.targets.size();
    b = 0;
    while (b < j) {
      int k = i;
      if (((DependencyNode)this.end.targets.get(b)).run != this)
        k = i + 1; 
      b++;
      i = k;
    } 
    if (i >= 2) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isDimensionResolved() {
    return this.dimension.resolved;
  }
  
  public boolean isResolved() {
    return this.resolved;
  }
  
  abstract void reset();
  
  abstract boolean supportsWrapComputation();
  
  public void update(Dependency paramDependency) {}
  
  protected void updateRunCenter(Dependency paramDependency, ConstraintAnchor paramConstraintAnchor1, ConstraintAnchor paramConstraintAnchor2, int paramInt) {
    float f;
    paramDependency = getTarget(paramConstraintAnchor1);
    DependencyNode dependencyNode = getTarget(paramConstraintAnchor2);
    if (!((DependencyNode)paramDependency).resolved || !dependencyNode.resolved)
      return; 
    int j = ((DependencyNode)paramDependency).value + paramConstraintAnchor1.getMargin();
    int i = dependencyNode.value - paramConstraintAnchor2.getMargin();
    int k = i - j;
    if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT)
      resolveDimension(paramInt, k); 
    if (!this.dimension.resolved)
      return; 
    if (this.dimension.value == k) {
      this.start.resolve(j);
      this.end.resolve(i);
      return;
    } 
    ConstraintWidget constraintWidget = this.widget;
    if (paramInt == 0) {
      f = constraintWidget.getHorizontalBiasPercent();
    } else {
      f = constraintWidget.getVerticalBiasPercent();
    } 
    paramInt = j;
    if (paramDependency == dependencyNode) {
      paramInt = ((DependencyNode)paramDependency).value;
      i = dependencyNode.value;
      f = 0.5F;
    } 
    j = this.dimension.value;
    this.start.resolve((int)(paramInt + 0.5F + (i - paramInt - j) * f));
    this.end.resolve(this.start.value + this.dimension.value);
  }
  
  protected void updateRunEnd(Dependency paramDependency) {}
  
  protected void updateRunStart(Dependency paramDependency) {}
  
  public long wrapSize(int paramInt) {
    if (this.dimension.resolved) {
      long l = this.dimension.value;
      if (isCenterConnection()) {
        l += (this.start.margin - this.end.margin);
      } else if (paramInt == 0) {
        l += this.start.margin;
      } else {
        l -= this.end.margin;
      } 
      return l;
    } 
    return 0L;
  }
  
  enum RunType {
    CENTER, END, NONE, START;
    
    private static final RunType[] $VALUES;
    
    static {
      RunType runType3 = new RunType("NONE", 0);
      NONE = runType3;
      RunType runType2 = new RunType("START", 1);
      START = runType2;
      RunType runType4 = new RunType("END", 2);
      END = runType4;
      RunType runType1 = new RunType("CENTER", 3);
      CENTER = runType1;
      $VALUES = new RunType[] { runType3, runType2, runType4, runType1 };
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\widgets\analyzer\WidgetRun.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */