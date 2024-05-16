package androidx.constraintlayout.core.widgets.analyzer;

import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;

public class HorizontalWidgetRun extends WidgetRun {
  private static int[] tempDimensions = new int[2];
  
  public HorizontalWidgetRun(ConstraintWidget paramConstraintWidget) {
    super(paramConstraintWidget);
    this.start.type = DependencyNode.Type.LEFT;
    this.end.type = DependencyNode.Type.RIGHT;
    this.orientation = 0;
  }
  
  private void computeInsetRatio(int[] paramArrayOfint, int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat, int paramInt5) {
    paramInt1 = paramInt2 - paramInt1;
    paramInt2 = paramInt4 - paramInt3;
    switch (paramInt5) {
      default:
        return;
      case 1:
        paramInt2 = (int)(paramInt1 * paramFloat + 0.5F);
        paramArrayOfint[0] = paramInt1;
        paramArrayOfint[1] = paramInt2;
      case 0:
        paramArrayOfint[0] = (int)(paramInt2 * paramFloat + 0.5F);
        paramArrayOfint[1] = paramInt2;
      case -1:
        break;
    } 
    paramInt3 = (int)(paramInt2 * paramFloat + 0.5F);
    paramInt4 = (int)(paramInt1 / paramFloat + 0.5F);
    if (paramInt3 <= paramInt1 && paramInt2 <= paramInt2) {
      paramArrayOfint[0] = paramInt3;
      paramArrayOfint[1] = paramInt2;
    } 
    if (paramInt1 <= paramInt1 && paramInt4 <= paramInt2) {
      paramArrayOfint[0] = paramInt1;
      paramArrayOfint[1] = paramInt4;
    } 
  }
  
  void apply() {
    if (this.widget.measured)
      this.dimension.resolve(this.widget.getWidth()); 
    if (!this.dimension.resolved) {
      this.dimensionBehavior = this.widget.getHorizontalDimensionBehaviour();
      if (this.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
        if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
          ConstraintWidget constraintWidget = this.widget.getParent();
          if (constraintWidget != null && (constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED || constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_PARENT)) {
            int i = constraintWidget.getWidth();
            int j = this.widget.mLeft.getMargin();
            int k = this.widget.mRight.getMargin();
            addTarget(this.start, constraintWidget.horizontalRun.start, this.widget.mLeft.getMargin());
            addTarget(this.end, constraintWidget.horizontalRun.end, -this.widget.mRight.getMargin());
            this.dimension.resolve(i - j - k);
            return;
          } 
        } 
        if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.FIXED)
          this.dimension.resolve(this.widget.getWidth()); 
      } 
    } else if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
      ConstraintWidget constraintWidget = this.widget.getParent();
      if (constraintWidget != null && (constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED || constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_PARENT)) {
        addTarget(this.start, constraintWidget.horizontalRun.start, this.widget.mLeft.getMargin());
        addTarget(this.end, constraintWidget.horizontalRun.end, -this.widget.mRight.getMargin());
        return;
      } 
    } 
    if (this.dimension.resolved && this.widget.measured) {
      if ((this.widget.mListAnchors[0]).mTarget != null && (this.widget.mListAnchors[1]).mTarget != null) {
        if (this.widget.isInHorizontalChain()) {
          this.start.margin = this.widget.mListAnchors[0].getMargin();
          this.end.margin = -this.widget.mListAnchors[1].getMargin();
        } else {
          DependencyNode dependencyNode = getTarget(this.widget.mListAnchors[0]);
          if (dependencyNode != null)
            addTarget(this.start, dependencyNode, this.widget.mListAnchors[0].getMargin()); 
          dependencyNode = getTarget(this.widget.mListAnchors[1]);
          if (dependencyNode != null)
            addTarget(this.end, dependencyNode, -this.widget.mListAnchors[1].getMargin()); 
          this.start.delegateToWidgetRun = true;
          this.end.delegateToWidgetRun = true;
        } 
      } else if ((this.widget.mListAnchors[0]).mTarget != null) {
        DependencyNode dependencyNode = getTarget(this.widget.mListAnchors[0]);
        if (dependencyNode != null) {
          addTarget(this.start, dependencyNode, this.widget.mListAnchors[0].getMargin());
          addTarget(this.end, this.start, this.dimension.value);
        } 
      } else if ((this.widget.mListAnchors[1]).mTarget != null) {
        DependencyNode dependencyNode = getTarget(this.widget.mListAnchors[1]);
        if (dependencyNode != null) {
          addTarget(this.end, dependencyNode, -this.widget.mListAnchors[1].getMargin());
          addTarget(this.start, this.end, -this.dimension.value);
        } 
      } else if (!(this.widget instanceof androidx.constraintlayout.core.widgets.Helper) && this.widget.getParent() != null && (this.widget.getAnchor(ConstraintAnchor.Type.CENTER)).mTarget == null) {
        DependencyNode dependencyNode = (this.widget.getParent()).horizontalRun.start;
        addTarget(this.start, dependencyNode, this.widget.getX());
        addTarget(this.end, this.start, this.dimension.value);
      } 
    } else {
      if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
        DimensionDependency dimensionDependency2;
        ConstraintWidget constraintWidget;
        DimensionDependency dimensionDependency1;
        switch (this.widget.mMatchConstraintDefaultWidth) {
          case 3:
            if (this.widget.mMatchConstraintDefaultHeight == 3) {
              this.start.updateDelegate = this;
              this.end.updateDelegate = this;
              this.widget.verticalRun.start.updateDelegate = this;
              this.widget.verticalRun.end.updateDelegate = this;
              this.dimension.updateDelegate = this;
              if (this.widget.isInVerticalChain()) {
                this.dimension.targets.add(this.widget.verticalRun.dimension);
                this.widget.verticalRun.dimension.dependencies.add(this.dimension);
                this.widget.verticalRun.dimension.updateDelegate = this;
                this.dimension.targets.add(this.widget.verticalRun.start);
                this.dimension.targets.add(this.widget.verticalRun.end);
                this.widget.verticalRun.start.dependencies.add(this.dimension);
                this.widget.verticalRun.end.dependencies.add(this.dimension);
                break;
              } 
              if (this.widget.isInHorizontalChain()) {
                this.widget.verticalRun.dimension.targets.add(this.dimension);
                this.dimension.dependencies.add(this.widget.verticalRun.dimension);
                break;
              } 
              this.widget.verticalRun.dimension.targets.add(this.dimension);
              break;
            } 
            dimensionDependency2 = this.widget.verticalRun.dimension;
            this.dimension.targets.add(dimensionDependency2);
            dimensionDependency2.dependencies.add(this.dimension);
            this.widget.verticalRun.start.dependencies.add(this.dimension);
            this.widget.verticalRun.end.dependencies.add(this.dimension);
            this.dimension.delegateToWidgetRun = true;
            this.dimension.dependencies.add(this.start);
            this.dimension.dependencies.add(this.end);
            this.start.targets.add(this.dimension);
            this.end.targets.add(this.dimension);
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
      } 
      if ((this.widget.mListAnchors[0]).mTarget != null && (this.widget.mListAnchors[1]).mTarget != null) {
        if (this.widget.isInHorizontalChain()) {
          this.start.margin = this.widget.mListAnchors[0].getMargin();
          this.end.margin = -this.widget.mListAnchors[1].getMargin();
        } else {
          DependencyNode dependencyNode2 = getTarget(this.widget.mListAnchors[0]);
          DependencyNode dependencyNode1 = getTarget(this.widget.mListAnchors[1]);
          if (dependencyNode2 != null)
            dependencyNode2.addDependency(this); 
          if (dependencyNode1 != null)
            dependencyNode1.addDependency(this); 
          this.mRunType = WidgetRun.RunType.CENTER;
        } 
      } else if ((this.widget.mListAnchors[0]).mTarget != null) {
        DependencyNode dependencyNode = getTarget(this.widget.mListAnchors[0]);
        if (dependencyNode != null) {
          addTarget(this.start, dependencyNode, this.widget.mListAnchors[0].getMargin());
          addTarget(this.end, this.start, 1, this.dimension);
        } 
      } else if ((this.widget.mListAnchors[1]).mTarget != null) {
        DependencyNode dependencyNode = getTarget(this.widget.mListAnchors[1]);
        if (dependencyNode != null) {
          addTarget(this.end, dependencyNode, -this.widget.mListAnchors[1].getMargin());
          addTarget(this.start, this.end, -1, this.dimension);
        } 
      } else if (!(this.widget instanceof androidx.constraintlayout.core.widgets.Helper) && this.widget.getParent() != null) {
        DependencyNode dependencyNode = (this.widget.getParent()).horizontalRun.start;
        addTarget(this.start, dependencyNode, this.widget.getX());
        addTarget(this.end, this.start, 1, this.dimension);
      } 
    } 
  }
  
  public void applyToWidget() {
    if (this.start.resolved)
      this.widget.setX(this.start.value); 
  }
  
  void clear() {
    this.runGroup = null;
    this.start.clear();
    this.end.clear();
    this.dimension.clear();
    this.resolved = false;
  }
  
  void reset() {
    this.resolved = false;
    this.start.clear();
    this.start.resolved = false;
    this.end.clear();
    this.end.resolved = false;
    this.dimension.resolved = false;
  }
  
  boolean supportsWrapComputation() {
    return (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) ? ((this.widget.mMatchConstraintDefaultWidth == 0)) : true;
  }
  
  public String toString() {
    return "HorizontalRun " + this.widget.getDebugName();
  }
  
  public void update(Dependency paramDependency) {
    switch (this.mRunType) {
      case null:
        updateRunCenter(paramDependency, this.widget.mLeft, this.widget.mRight, 0);
        return;
      case null:
        updateRunEnd(paramDependency);
        break;
      case null:
        updateRunStart(paramDependency);
        break;
    } 
    if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
      ConstraintWidget constraintWidget;
      int m;
      switch (this.widget.mMatchConstraintDefaultWidth) {
        case 3:
          if (this.widget.mMatchConstraintDefaultHeight == 0 || this.widget.mMatchConstraintDefaultHeight == 3) {
            int n;
            int i1;
            int i2;
            int i3;
            DependencyNode dependencyNode1 = this.widget.verticalRun.start;
            paramDependency = this.widget.verticalRun.end;
            if (this.widget.mLeft.mTarget != null) {
              n = 1;
            } else {
              n = 0;
            } 
            if (this.widget.mTop.mTarget != null) {
              i1 = 1;
            } else {
              i1 = 0;
            } 
            if (this.widget.mRight.mTarget != null) {
              i2 = 1;
            } else {
              i2 = 0;
            } 
            if (this.widget.mBottom.mTarget != null) {
              i3 = 1;
            } else {
              i3 = 0;
            } 
            int i4 = this.widget.getDimensionRatioSide();
            if (n && i1 && i2 && i3) {
              float f1 = this.widget.getDimensionRatio();
              if (dependencyNode1.resolved && ((DependencyNode)paramDependency).resolved) {
                if (!this.start.readyToSolve || !this.end.readyToSolve)
                  return; 
                int i12 = ((DependencyNode)this.start.targets.get(0)).value;
                int i9 = this.start.margin;
                i3 = ((DependencyNode)this.end.targets.get(0)).value;
                int i10 = this.end.margin;
                i1 = dependencyNode1.value;
                int i11 = dependencyNode1.margin;
                n = ((DependencyNode)paramDependency).value;
                i2 = ((DependencyNode)paramDependency).margin;
                computeInsetRatio(tempDimensions, i12 + i9, i3 - i10, i1 + i11, n - i2, f1, i4);
                this.dimension.resolve(tempDimensions[0]);
                this.widget.verticalRun.dimension.resolve(tempDimensions[1]);
                return;
              } 
              if (this.start.resolved && this.end.resolved) {
                if (!dependencyNode1.readyToSolve || !((DependencyNode)paramDependency).readyToSolve)
                  return; 
                int i12 = this.start.value;
                n = this.start.margin;
                i2 = this.end.value;
                i1 = this.end.margin;
                int i10 = ((DependencyNode)dependencyNode1.targets.get(0)).value;
                int i11 = dependencyNode1.margin;
                int i9 = ((DependencyNode)((DependencyNode)paramDependency).targets.get(0)).value;
                i3 = ((DependencyNode)paramDependency).margin;
                computeInsetRatio(tempDimensions, i12 + n, i2 - i1, i10 + i11, i9 - i3, f1, i4);
                this.dimension.resolve(tempDimensions[0]);
                this.widget.verticalRun.dimension.resolve(tempDimensions[1]);
              } 
              if (!this.start.readyToSolve || !this.end.readyToSolve || !dependencyNode1.readyToSolve || !((DependencyNode)paramDependency).readyToSolve)
                return; 
              i3 = ((DependencyNode)this.start.targets.get(0)).value;
              int i6 = this.start.margin;
              i1 = ((DependencyNode)this.end.targets.get(0)).value;
              int i7 = this.end.margin;
              n = ((DependencyNode)dependencyNode1.targets.get(0)).value;
              int i5 = dependencyNode1.margin;
              int i8 = ((DependencyNode)((DependencyNode)paramDependency).targets.get(0)).value;
              i2 = ((DependencyNode)paramDependency).margin;
              computeInsetRatio(tempDimensions, i3 + i6, i1 - i7, n + i5, i8 - i2, f1, i4);
              this.dimension.resolve(tempDimensions[0]);
              this.widget.verticalRun.dimension.resolve(tempDimensions[1]);
              break;
            } 
            if (n != 0 && i2 != 0) {
              if (!this.start.readyToSolve || !this.end.readyToSolve)
                return; 
              float f1 = this.widget.getDimensionRatio();
              n = ((DependencyNode)this.start.targets.get(0)).value + this.start.margin;
              i1 = ((DependencyNode)this.end.targets.get(0)).value - this.end.margin;
              switch (i4) {
                default:
                  break;
                case 1:
                  n = getLimitedDimension(i1 - n, 0);
                  i2 = (int)(n / f1 + 0.5F);
                  i1 = getLimitedDimension(i2, 1);
                  if (i2 != i1)
                    n = (int)(i1 * f1 + 0.5F); 
                  this.dimension.resolve(n);
                  this.widget.verticalRun.dimension.resolve(i1);
                  break;
                case -1:
                case 0:
                  break;
              } 
              n = getLimitedDimension(i1 - n, 0);
              i2 = (int)(n * f1 + 0.5F);
              i1 = getLimitedDimension(i2, 1);
              if (i2 != i1)
                n = (int)(i1 / f1 + 0.5F); 
              this.dimension.resolve(n);
              this.widget.verticalRun.dimension.resolve(i1);
              break;
            } 
            if (i1 != 0 && i3 != 0) {
              if (!dependencyNode1.readyToSolve || !((DependencyNode)paramDependency).readyToSolve)
                return; 
              float f1 = this.widget.getDimensionRatio();
              n = ((DependencyNode)dependencyNode1.targets.get(0)).value + dependencyNode1.margin;
              i1 = ((DependencyNode)((DependencyNode)paramDependency).targets.get(0)).value - ((DependencyNode)paramDependency).margin;
              switch (i4) {
                default:
                  break;
                case 0:
                  n = getLimitedDimension(i1 - n, 1);
                  i2 = (int)(n * f1 + 0.5F);
                  i1 = getLimitedDimension(i2, 0);
                  if (i2 != i1)
                    n = (int)(i1 / f1 + 0.5F); 
                  this.dimension.resolve(i1);
                  this.widget.verticalRun.dimension.resolve(n);
                  break;
                case -1:
                case 1:
                  break;
              } 
              n = getLimitedDimension(i1 - n, 1);
              i2 = (int)(n / f1 + 0.5F);
              i1 = getLimitedDimension(i2, 0);
              if (i2 != i1)
                n = (int)(i1 * f1 + 0.5F); 
              this.dimension.resolve(i1);
              this.widget.verticalRun.dimension.resolve(n);
            } 
            break;
          } 
          m = 0;
          switch (this.widget.getDimensionRatioSide()) {
            case 1:
              m = (int)(this.widget.verticalRun.dimension.value * this.widget.getDimensionRatio() + 0.5F);
              break;
            case 0:
              m = (int)(this.widget.verticalRun.dimension.value / this.widget.getDimensionRatio() + 0.5F);
              break;
            case -1:
              m = (int)(this.widget.verticalRun.dimension.value * this.widget.getDimensionRatio() + 0.5F);
              break;
          } 
          this.dimension.resolve(m);
          break;
        case 2:
          constraintWidget = this.widget.getParent();
          if (constraintWidget != null && constraintWidget.horizontalRun.dimension.resolved) {
            float f1 = this.widget.mMatchConstraintPercentWidth;
            m = (int)(constraintWidget.horizontalRun.dimension.value * f1 + 0.5F);
            this.dimension.resolve(m);
          } 
          break;
      } 
    } 
    if (!this.start.readyToSolve || !this.end.readyToSolve)
      return; 
    if (this.start.resolved && this.end.resolved && this.dimension.resolved)
      return; 
    if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.widget.mMatchConstraintDefaultWidth == 0 && !this.widget.isInHorizontalChain()) {
      DependencyNode dependencyNode1 = this.start.targets.get(0);
      paramDependency = this.end.targets.get(0);
      int n = dependencyNode1.value + this.start.margin;
      int m = ((DependencyNode)paramDependency).value + this.end.margin;
      this.start.resolve(n);
      this.end.resolve(m);
      this.dimension.resolve(m - n);
      return;
    } 
    if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.matchConstraintsType == 1 && this.start.targets.size() > 0 && this.end.targets.size() > 0) {
      DependencyNode dependencyNode1 = this.start.targets.get(0);
      paramDependency = this.end.targets.get(0);
      int n = dependencyNode1.value;
      int m = this.start.margin;
      m = Math.min(((DependencyNode)paramDependency).value + this.end.margin - n + m, this.dimension.wrapValue);
      int i1 = this.widget.mMatchConstraintMaxWidth;
      n = Math.max(this.widget.mMatchConstraintMinWidth, m);
      m = n;
      if (i1 > 0)
        m = Math.min(i1, n); 
      this.dimension.resolve(m);
    } 
    if (!this.dimension.resolved)
      return; 
    paramDependency = this.start.targets.get(0);
    DependencyNode dependencyNode = this.end.targets.get(0);
    int j = ((DependencyNode)paramDependency).value + this.start.margin;
    int i = dependencyNode.value + this.end.margin;
    float f = this.widget.getHorizontalBiasPercent();
    if (paramDependency == dependencyNode) {
      j = ((DependencyNode)paramDependency).value;
      i = dependencyNode.value;
      f = 0.5F;
    } 
    int k = this.dimension.value;
    this.start.resolve((int)(j + 0.5F + (i - j - k) * f));
    this.end.resolve(this.start.value + this.dimension.value);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\widgets\analyzer\HorizontalWidgetRun.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */