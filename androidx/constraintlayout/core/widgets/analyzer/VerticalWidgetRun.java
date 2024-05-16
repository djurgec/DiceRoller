package androidx.constraintlayout.core.widgets.analyzer;

import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;

public class VerticalWidgetRun extends WidgetRun {
  public DependencyNode baseline = new DependencyNode(this);
  
  DimensionDependency baselineDimension = null;
  
  public VerticalWidgetRun(ConstraintWidget paramConstraintWidget) {
    super(paramConstraintWidget);
    this.start.type = DependencyNode.Type.TOP;
    this.end.type = DependencyNode.Type.BOTTOM;
    this.baseline.type = DependencyNode.Type.BASELINE;
    this.orientation = 1;
  }
  
  void apply() {
    if (this.widget.measured)
      this.dimension.resolve(this.widget.getHeight()); 
    if (!this.dimension.resolved) {
      this.dimensionBehavior = this.widget.getVerticalDimensionBehaviour();
      if (this.widget.hasBaseline())
        this.baselineDimension = new BaselineDimensionDependency(this); 
      if (this.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
        if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
          ConstraintWidget constraintWidget = this.widget.getParent();
          if (constraintWidget != null && constraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED) {
            int j = constraintWidget.getHeight();
            int i = this.widget.mTop.getMargin();
            int k = this.widget.mBottom.getMargin();
            addTarget(this.start, constraintWidget.verticalRun.start, this.widget.mTop.getMargin());
            addTarget(this.end, constraintWidget.verticalRun.end, -this.widget.mBottom.getMargin());
            this.dimension.resolve(j - i - k);
            return;
          } 
        } 
        if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.FIXED)
          this.dimension.resolve(this.widget.getHeight()); 
      } 
    } else if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
      ConstraintWidget constraintWidget = this.widget.getParent();
      if (constraintWidget != null && constraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED) {
        addTarget(this.start, constraintWidget.verticalRun.start, this.widget.mTop.getMargin());
        addTarget(this.end, constraintWidget.verticalRun.end, -this.widget.mBottom.getMargin());
        return;
      } 
    } 
    if (this.dimension.resolved && this.widget.measured) {
      if ((this.widget.mListAnchors[2]).mTarget != null && (this.widget.mListAnchors[3]).mTarget != null) {
        if (this.widget.isInVerticalChain()) {
          this.start.margin = this.widget.mListAnchors[2].getMargin();
          this.end.margin = -this.widget.mListAnchors[3].getMargin();
        } else {
          DependencyNode dependencyNode = getTarget(this.widget.mListAnchors[2]);
          if (dependencyNode != null)
            addTarget(this.start, dependencyNode, this.widget.mListAnchors[2].getMargin()); 
          dependencyNode = getTarget(this.widget.mListAnchors[3]);
          if (dependencyNode != null)
            addTarget(this.end, dependencyNode, -this.widget.mListAnchors[3].getMargin()); 
          this.start.delegateToWidgetRun = true;
          this.end.delegateToWidgetRun = true;
        } 
        if (this.widget.hasBaseline())
          addTarget(this.baseline, this.start, this.widget.getBaselineDistance()); 
      } else if ((this.widget.mListAnchors[2]).mTarget != null) {
        DependencyNode dependencyNode = getTarget(this.widget.mListAnchors[2]);
        if (dependencyNode != null) {
          addTarget(this.start, dependencyNode, this.widget.mListAnchors[2].getMargin());
          addTarget(this.end, this.start, this.dimension.value);
          if (this.widget.hasBaseline())
            addTarget(this.baseline, this.start, this.widget.getBaselineDistance()); 
        } 
      } else if ((this.widget.mListAnchors[3]).mTarget != null) {
        DependencyNode dependencyNode = getTarget(this.widget.mListAnchors[3]);
        if (dependencyNode != null) {
          addTarget(this.end, dependencyNode, -this.widget.mListAnchors[3].getMargin());
          addTarget(this.start, this.end, -this.dimension.value);
        } 
        if (this.widget.hasBaseline())
          addTarget(this.baseline, this.start, this.widget.getBaselineDistance()); 
      } else if ((this.widget.mListAnchors[4]).mTarget != null) {
        DependencyNode dependencyNode = getTarget(this.widget.mListAnchors[4]);
        if (dependencyNode != null) {
          addTarget(this.baseline, dependencyNode, 0);
          addTarget(this.start, this.baseline, -this.widget.getBaselineDistance());
          addTarget(this.end, this.start, this.dimension.value);
        } 
      } else if (!(this.widget instanceof androidx.constraintlayout.core.widgets.Helper) && this.widget.getParent() != null && (this.widget.getAnchor(ConstraintAnchor.Type.CENTER)).mTarget == null) {
        DependencyNode dependencyNode = (this.widget.getParent()).verticalRun.start;
        addTarget(this.start, dependencyNode, this.widget.getY());
        addTarget(this.end, this.start, this.dimension.value);
        if (this.widget.hasBaseline())
          addTarget(this.baseline, this.start, this.widget.getBaselineDistance()); 
      } 
    } else {
      if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
        DimensionDependency dimensionDependency2;
        ConstraintWidget constraintWidget;
        DimensionDependency dimensionDependency1;
        switch (this.widget.mMatchConstraintDefaultHeight) {
          case 3:
            if (this.widget.isInVerticalChain() || this.widget.mMatchConstraintDefaultWidth == 3)
              break; 
            dimensionDependency2 = this.widget.horizontalRun.dimension;
            this.dimension.targets.add(dimensionDependency2);
            dimensionDependency2.dependencies.add(this.dimension);
            this.dimension.delegateToWidgetRun = true;
            this.dimension.dependencies.add(this.start);
            this.dimension.dependencies.add(this.end);
            break;
          case 2:
            constraintWidget = this.widget.getParent();
            if (constraintWidget == null)
              break; 
            dimensionDependency1 = constraintWidget.verticalRun.dimension;
            this.dimension.targets.add(dimensionDependency1);
            dimensionDependency1.dependencies.add(this.dimension);
            this.dimension.delegateToWidgetRun = true;
            this.dimension.dependencies.add(this.start);
            this.dimension.dependencies.add(this.end);
            break;
        } 
      } else {
        this.dimension.addDependency(this);
      } 
      if ((this.widget.mListAnchors[2]).mTarget != null && (this.widget.mListAnchors[3]).mTarget != null) {
        if (this.widget.isInVerticalChain()) {
          this.start.margin = this.widget.mListAnchors[2].getMargin();
          this.end.margin = -this.widget.mListAnchors[3].getMargin();
        } else {
          DependencyNode dependencyNode1 = getTarget(this.widget.mListAnchors[2]);
          DependencyNode dependencyNode2 = getTarget(this.widget.mListAnchors[3]);
          if (dependencyNode1 != null)
            dependencyNode1.addDependency(this); 
          if (dependencyNode2 != null)
            dependencyNode2.addDependency(this); 
          this.mRunType = WidgetRun.RunType.CENTER;
        } 
        if (this.widget.hasBaseline())
          addTarget(this.baseline, this.start, 1, this.baselineDimension); 
      } else if ((this.widget.mListAnchors[2]).mTarget != null) {
        DependencyNode dependencyNode = getTarget(this.widget.mListAnchors[2]);
        if (dependencyNode != null) {
          addTarget(this.start, dependencyNode, this.widget.mListAnchors[2].getMargin());
          addTarget(this.end, this.start, 1, this.dimension);
          if (this.widget.hasBaseline())
            addTarget(this.baseline, this.start, 1, this.baselineDimension); 
          if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.widget.getDimensionRatio() > 0.0F && this.widget.horizontalRun.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            this.widget.horizontalRun.dimension.dependencies.add(this.dimension);
            this.dimension.targets.add(this.widget.horizontalRun.dimension);
            this.dimension.updateDelegate = this;
          } 
        } 
      } else if ((this.widget.mListAnchors[3]).mTarget != null) {
        DependencyNode dependencyNode = getTarget(this.widget.mListAnchors[3]);
        if (dependencyNode != null) {
          addTarget(this.end, dependencyNode, -this.widget.mListAnchors[3].getMargin());
          addTarget(this.start, this.end, -1, this.dimension);
          if (this.widget.hasBaseline())
            addTarget(this.baseline, this.start, 1, this.baselineDimension); 
        } 
      } else if ((this.widget.mListAnchors[4]).mTarget != null) {
        DependencyNode dependencyNode = getTarget(this.widget.mListAnchors[4]);
        if (dependencyNode != null) {
          addTarget(this.baseline, dependencyNode, 0);
          addTarget(this.start, this.baseline, -1, this.baselineDimension);
          addTarget(this.end, this.start, 1, this.dimension);
        } 
      } else if (!(this.widget instanceof androidx.constraintlayout.core.widgets.Helper) && this.widget.getParent() != null) {
        DependencyNode dependencyNode = (this.widget.getParent()).verticalRun.start;
        addTarget(this.start, dependencyNode, this.widget.getY());
        addTarget(this.end, this.start, 1, this.dimension);
        if (this.widget.hasBaseline())
          addTarget(this.baseline, this.start, 1, this.baselineDimension); 
        if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.widget.getDimensionRatio() > 0.0F && this.widget.horizontalRun.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
          this.widget.horizontalRun.dimension.dependencies.add(this.dimension);
          this.dimension.targets.add(this.widget.horizontalRun.dimension);
          this.dimension.updateDelegate = this;
        } 
      } 
      if (this.dimension.targets.size() == 0)
        this.dimension.readyToSolve = true; 
    } 
  }
  
  public void applyToWidget() {
    if (this.start.resolved)
      this.widget.setY(this.start.value); 
  }
  
  void clear() {
    this.runGroup = null;
    this.start.clear();
    this.end.clear();
    this.baseline.clear();
    this.dimension.clear();
    this.resolved = false;
  }
  
  void reset() {
    this.resolved = false;
    this.start.clear();
    this.start.resolved = false;
    this.end.clear();
    this.end.resolved = false;
    this.baseline.clear();
    this.baseline.resolved = false;
    this.dimension.resolved = false;
  }
  
  boolean supportsWrapComputation() {
    return (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) ? ((this.widget.mMatchConstraintDefaultHeight == 0)) : true;
  }
  
  public String toString() {
    return "VerticalRun " + this.widget.getDebugName();
  }
  
  public void update(Dependency paramDependency) {
    switch (this.mRunType) {
      case null:
        updateRunCenter(paramDependency, this.widget.mTop, this.widget.mBottom, 1);
        return;
      case null:
        updateRunEnd(paramDependency);
        break;
      case null:
        updateRunStart(paramDependency);
        break;
    } 
    if (this.dimension.readyToSolve && !this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
      ConstraintWidget constraintWidget;
      switch (this.widget.mMatchConstraintDefaultHeight) {
        case 3:
          if (this.widget.horizontalRun.dimension.resolved) {
            int i = 0;
            switch (this.widget.getDimensionRatioSide()) {
              case 1:
                i = (int)(this.widget.horizontalRun.dimension.value / this.widget.getDimensionRatio() + 0.5F);
                break;
              case 0:
                i = (int)(this.widget.horizontalRun.dimension.value * this.widget.getDimensionRatio() + 0.5F);
                break;
              case -1:
                i = (int)(this.widget.horizontalRun.dimension.value / this.widget.getDimensionRatio() + 0.5F);
                break;
            } 
            this.dimension.resolve(i);
          } 
          break;
        case 2:
          constraintWidget = this.widget.getParent();
          if (constraintWidget != null && constraintWidget.verticalRun.dimension.resolved) {
            float f = this.widget.mMatchConstraintPercentHeight;
            int i = (int)(constraintWidget.verticalRun.dimension.value * f + 0.5F);
            this.dimension.resolve(i);
          } 
          break;
      } 
    } 
    if (!this.start.readyToSolve || !this.end.readyToSolve)
      return; 
    if (this.start.resolved && this.end.resolved && this.dimension.resolved)
      return; 
    if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.widget.mMatchConstraintDefaultWidth == 0 && !this.widget.isInVerticalChain()) {
      paramDependency = this.start.targets.get(0);
      DependencyNode dependencyNode = this.end.targets.get(0);
      int j = ((DependencyNode)paramDependency).value + this.start.margin;
      int i = dependencyNode.value + this.end.margin;
      this.start.resolve(j);
      this.end.resolve(i);
      this.dimension.resolve(i - j);
      return;
    } 
    if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.matchConstraintsType == 1 && this.start.targets.size() > 0 && this.end.targets.size() > 0) {
      paramDependency = this.start.targets.get(0);
      DependencyNode dependencyNode = this.end.targets.get(0);
      int i = ((DependencyNode)paramDependency).value;
      int j = this.start.margin;
      i = dependencyNode.value + this.end.margin - i + j;
      if (i < this.dimension.wrapValue) {
        this.dimension.resolve(i);
      } else {
        this.dimension.resolve(this.dimension.wrapValue);
      } 
    } 
    if (!this.dimension.resolved)
      return; 
    if (this.start.targets.size() > 0 && this.end.targets.size() > 0) {
      DependencyNode dependencyNode = this.start.targets.get(0);
      paramDependency = this.end.targets.get(0);
      int i = dependencyNode.value + this.start.margin;
      int j = ((DependencyNode)paramDependency).value + this.end.margin;
      float f = this.widget.getVerticalBiasPercent();
      if (dependencyNode == paramDependency) {
        i = dependencyNode.value;
        j = ((DependencyNode)paramDependency).value;
        f = 0.5F;
      } 
      int k = this.dimension.value;
      this.start.resolve((int)(i + 0.5F + (j - i - k) * f));
      this.end.resolve(this.start.value + this.dimension.value);
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\widgets\analyzer\VerticalWidgetRun.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */