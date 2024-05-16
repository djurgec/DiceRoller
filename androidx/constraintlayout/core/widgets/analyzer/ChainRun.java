package androidx.constraintlayout.core.widgets.analyzer;

import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import java.util.ArrayList;
import java.util.Iterator;

public class ChainRun extends WidgetRun {
  private int chainStyle;
  
  ArrayList<WidgetRun> widgets = new ArrayList<>();
  
  public ChainRun(ConstraintWidget paramConstraintWidget, int paramInt) {
    super(paramConstraintWidget);
    this.orientation = paramInt;
    build();
  }
  
  private void build() {
    int i;
    ConstraintWidget constraintWidget2 = this.widget;
    ConstraintWidget constraintWidget1;
    for (constraintWidget1 = constraintWidget2.getPreviousChainMember(this.orientation); constraintWidget1 != null; constraintWidget1 = constraintWidget) {
      ConstraintWidget constraintWidget = constraintWidget1.getPreviousChainMember(this.orientation);
      constraintWidget2 = constraintWidget1;
    } 
    this.widget = constraintWidget2;
    this.widgets.add(constraintWidget2.getRun(this.orientation));
    for (constraintWidget1 = constraintWidget2.getNextChainMember(this.orientation); constraintWidget1 != null; constraintWidget1 = constraintWidget1.getNextChainMember(this.orientation))
      this.widgets.add(constraintWidget1.getRun(this.orientation)); 
    for (WidgetRun widgetRun : this.widgets) {
      if (this.orientation == 0) {
        widgetRun.widget.horizontalChainRun = this;
        continue;
      } 
      if (this.orientation == 1)
        widgetRun.widget.verticalChainRun = this; 
    } 
    if (this.orientation == 0 && ((ConstraintWidgetContainer)this.widget.getParent()).isRtl()) {
      i = 1;
    } else {
      i = 0;
    } 
    if (i && this.widgets.size() > 1) {
      ArrayList<WidgetRun> arrayList = this.widgets;
      this.widget = ((WidgetRun)arrayList.get(arrayList.size() - 1)).widget;
    } 
    if (this.orientation == 0) {
      i = this.widget.getHorizontalChainStyle();
    } else {
      i = this.widget.getVerticalChainStyle();
    } 
    this.chainStyle = i;
  }
  
  private ConstraintWidget getFirstVisibleWidget() {
    for (byte b = 0; b < this.widgets.size(); b++) {
      WidgetRun widgetRun = this.widgets.get(b);
      if (widgetRun.widget.getVisibility() != 8)
        return widgetRun.widget; 
    } 
    return null;
  }
  
  private ConstraintWidget getLastVisibleWidget() {
    for (int i = this.widgets.size() - 1; i >= 0; i--) {
      WidgetRun widgetRun = this.widgets.get(i);
      if (widgetRun.widget.getVisibility() != 8)
        return widgetRun.widget; 
    } 
    return null;
  }
  
  void apply() {
    DependencyNode dependencyNode;
    Iterator<WidgetRun> iterator = this.widgets.iterator();
    while (iterator.hasNext())
      ((WidgetRun)iterator.next()).apply(); 
    int i = this.widgets.size();
    if (i < 1)
      return; 
    ConstraintWidget constraintWidget2 = ((WidgetRun)this.widgets.get(0)).widget;
    ConstraintWidget constraintWidget1 = ((WidgetRun)this.widgets.get(i - 1)).widget;
    if (this.orientation == 0) {
      ConstraintAnchor constraintAnchor2 = constraintWidget2.mLeft;
      ConstraintAnchor constraintAnchor1 = constraintWidget1.mRight;
      DependencyNode dependencyNode1 = getTarget(constraintAnchor2, 0);
      i = constraintAnchor2.getMargin();
      ConstraintWidget constraintWidget = getFirstVisibleWidget();
      if (constraintWidget != null)
        i = constraintWidget.mLeft.getMargin(); 
      if (dependencyNode1 != null)
        addTarget(this.start, dependencyNode1, i); 
      dependencyNode = getTarget(constraintAnchor1, 0);
      i = constraintAnchor1.getMargin();
      constraintWidget1 = getLastVisibleWidget();
      if (constraintWidget1 != null)
        i = constraintWidget1.mRight.getMargin(); 
      if (dependencyNode != null)
        addTarget(this.end, dependencyNode, -i); 
    } else {
      ConstraintAnchor constraintAnchor2 = ((ConstraintWidget)dependencyNode).mTop;
      ConstraintAnchor constraintAnchor1 = constraintWidget1.mBottom;
      DependencyNode dependencyNode2 = getTarget(constraintAnchor2, 1);
      i = constraintAnchor2.getMargin();
      ConstraintWidget constraintWidget4 = getFirstVisibleWidget();
      if (constraintWidget4 != null)
        i = constraintWidget4.mTop.getMargin(); 
      if (dependencyNode2 != null)
        addTarget(this.start, dependencyNode2, i); 
      DependencyNode dependencyNode1 = getTarget(constraintAnchor1, 1);
      i = constraintAnchor1.getMargin();
      ConstraintWidget constraintWidget3 = getLastVisibleWidget();
      if (constraintWidget3 != null)
        i = constraintWidget3.mBottom.getMargin(); 
      if (dependencyNode1 != null)
        addTarget(this.end, dependencyNode1, -i); 
    } 
    this.start.updateDelegate = this;
    this.end.updateDelegate = this;
  }
  
  public void applyToWidget() {
    for (byte b = 0; b < this.widgets.size(); b++)
      ((WidgetRun)this.widgets.get(b)).applyToWidget(); 
  }
  
  void clear() {
    this.runGroup = null;
    Iterator<WidgetRun> iterator = this.widgets.iterator();
    while (iterator.hasNext())
      ((WidgetRun)iterator.next()).clear(); 
  }
  
  public long getWrapDimension() {
    int i = this.widgets.size();
    long l = 0L;
    for (byte b = 0; b < i; b++) {
      WidgetRun widgetRun = this.widgets.get(b);
      l = l + widgetRun.start.margin + widgetRun.getWrapDimension() + widgetRun.end.margin;
    } 
    return l;
  }
  
  void reset() {
    this.start.resolved = false;
    this.end.resolved = false;
  }
  
  boolean supportsWrapComputation() {
    int i = this.widgets.size();
    for (byte b = 0; b < i; b++) {
      if (!((WidgetRun)this.widgets.get(b)).supportsWrapComputation())
        return false; 
    } 
    return true;
  }
  
  public String toString() {
    String str;
    StringBuilder stringBuilder = new StringBuilder("ChainRun ");
    if (this.orientation == 0) {
      str = "horizontal : ";
    } else {
      str = "vertical : ";
    } 
    stringBuilder.append(str);
    for (WidgetRun widgetRun : this.widgets) {
      stringBuilder.append("<");
      stringBuilder.append(widgetRun);
      stringBuilder.append("> ");
    } 
    return stringBuilder.toString();
  }
  
  public void update(Dependency paramDependency) {
    float f;
    int k;
    int n;
    int i2;
    int i3;
    int i4;
    if (!this.start.resolved || !this.end.resolved)
      return; 
    ConstraintWidget constraintWidget = this.widget.getParent();
    boolean bool = false;
    if (constraintWidget instanceof ConstraintWidgetContainer)
      bool = ((ConstraintWidgetContainer)constraintWidget).isRtl(); 
    int i6 = this.end.value - this.start.value;
    int i5 = this.widgets.size();
    int j = -1;
    int i = 0;
    while (true) {
      i2 = j;
      if (i < i5) {
        if (((WidgetRun)this.widgets.get(i)).widget.getVisibility() == 8) {
          i++;
          continue;
        } 
        i2 = i;
      } 
      break;
    } 
    j = -1;
    i = i5 - 1;
    while (true) {
      i3 = j;
      if (i >= 0) {
        if (((WidgetRun)this.widgets.get(i)).widget.getVisibility() == 8) {
          i--;
          continue;
        } 
        i3 = i;
      } 
      break;
    } 
    int i1 = 0;
    while (true) {
      i4 = 0;
      n = 0;
      f = 0.0F;
      float f1 = 0.0F;
      j = 0;
      i = 0;
      k = 0;
      int i7 = 0;
      if (i1 < 2) {
        byte b = 0;
        while (b < i5) {
          WidgetRun widgetRun = this.widgets.get(b);
          if (widgetRun.widget.getVisibility() == 8) {
            k = i;
            f = f1;
          } else {
            int i8 = n + 1;
            j = i7;
            if (b > 0) {
              j = i7;
              if (b >= i2)
                j = i7 + widgetRun.start.margin; 
            } 
            i7 = widgetRun.dimension.value;
            if (widgetRun.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
              k = 1;
            } else {
              k = 0;
            } 
            if (k != 0) {
              if (this.orientation == 0 && !widgetRun.widget.horizontalRun.dimension.resolved)
                return; 
              if (this.orientation == 1 && !widgetRun.widget.verticalRun.dimension.resolved)
                return; 
            } else if (widgetRun.matchConstraintsType == 1 && i1 == 0) {
              k = 1;
              i7 = widgetRun.dimension.wrapValue;
              i++;
            } else if (widgetRun.dimension.resolved) {
              k = 1;
            } 
            if (k == 0) {
              i++;
              float f2 = widgetRun.widget.mWeight[this.orientation];
              f = f1;
              if (f2 >= 0.0F)
                f = f1 + f2; 
              f1 = f;
            } else {
              j += i7;
            } 
            i7 = j;
            k = i;
            f = f1;
            n = i8;
            if (b < i5 - 1) {
              i7 = j;
              k = i;
              f = f1;
              n = i8;
              if (b < i3) {
                i7 = j + -widgetRun.end.margin;
                n = i8;
                f = f1;
                k = i;
              } 
            } 
          } 
          b++;
          i = k;
          f1 = f;
        } 
        k = i7;
        j = i;
        f = f1;
        i4 = n;
        if (i7 >= i6) {
          if (i == 0) {
            k = i7;
            j = i;
            f = f1;
            i4 = n;
            break;
          } 
          i1++;
          continue;
        } 
      } 
      break;
    } 
    int m = this.start.value;
    if (bool)
      m = this.end.value; 
    i = m;
    if (k > i6)
      if (bool) {
        i = m + (int)((k - i6) / 2.0F + 0.5F);
      } else {
        i = m - (int)((k - i6) / 2.0F + 0.5F);
      }  
    if (j > 0) {
      n = (int)((i6 - k) / j + 0.5F);
      m = 0;
      byte b = 0;
      i1 = i;
      while (b < i5) {
        WidgetRun widgetRun = this.widgets.get(b);
        if (widgetRun.widget.getVisibility() != 8 && widgetRun.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && !widgetRun.dimension.resolved) {
          i = n;
          if (f > 0.0F) {
            float f1 = widgetRun.widget.mWeight[this.orientation];
            i = (int)((i6 - k) * f1 / f + 0.5F);
          } 
          int i7 = i;
          if (this.orientation == 0) {
            i8 = widgetRun.widget.mMatchConstraintMaxWidth;
            i9 = widgetRun.widget.mMatchConstraintMinWidth;
          } else {
            i8 = widgetRun.widget.mMatchConstraintMaxHeight;
            i9 = widgetRun.widget.mMatchConstraintMinHeight;
          } 
          int i10 = i7;
          if (widgetRun.matchConstraintsType == 1)
            i10 = Math.min(i7, widgetRun.dimension.wrapValue); 
          int i9 = Math.max(i9, i10);
          i7 = i9;
          if (i8 > 0)
            i7 = Math.min(i8, i9); 
          i9 = i;
          int i8 = m;
          if (i7 != i) {
            i8 = m + 1;
            i9 = i7;
          } 
          widgetRun.dimension.resolve(i9);
          m = i8;
        } 
        b++;
      } 
      if (m > 0) {
        n = j - m;
        i = 0;
        for (j = 0; j < i5; j++) {
          WidgetRun widgetRun = this.widgets.get(j);
          if (widgetRun.widget.getVisibility() != 8) {
            k = i;
            if (j > 0) {
              k = i;
              if (j >= i2)
                k = i + widgetRun.start.margin; 
            } 
            k += widgetRun.dimension.value;
            i = k;
            if (j < i5 - 1) {
              i = k;
              if (j < i3)
                i = k + -widgetRun.end.margin; 
            } 
          } 
        } 
        j = n;
      } else {
        i = k;
      } 
      if (this.chainStyle == 2 && m == 0)
        this.chainStyle = 0; 
      k = i;
      n = j;
      i = i1;
    } else {
      n = j;
    } 
    if (k > i6)
      this.chainStyle = 2; 
    if (i4 > 0 && n == 0 && i2 == i3)
      this.chainStyle = 2; 
    j = this.chainStyle;
    if (j == 1) {
      j = 0;
      if (i4 > 1) {
        j = (i6 - k) / (i4 - 1);
      } else if (i4 == 1) {
        j = (i6 - k) / 2;
      } 
      m = j;
      if (n > 0)
        m = 0; 
      j = 0;
      for (k = i; j < i5; k = i) {
        i = j;
        if (bool)
          i = i5 - j + 1; 
        WidgetRun widgetRun = this.widgets.get(i);
        if (widgetRun.widget.getVisibility() == 8) {
          widgetRun.start.resolve(k);
          widgetRun.end.resolve(k);
          i = k;
        } else {
          i = k;
          if (j > 0)
            if (bool) {
              i = k - m;
            } else {
              i = k + m;
            }  
          k = i;
          if (j > 0) {
            k = i;
            if (j >= i2)
              if (bool) {
                k = i - widgetRun.start.margin;
              } else {
                k = i + widgetRun.start.margin;
              }  
          } 
          if (bool) {
            widgetRun.end.resolve(k);
          } else {
            widgetRun.start.resolve(k);
          } 
          n = widgetRun.dimension.value;
          i = n;
          if (widgetRun.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            i = n;
            if (widgetRun.matchConstraintsType == 1)
              i = widgetRun.dimension.wrapValue; 
          } 
          if (bool) {
            k -= i;
          } else {
            k += i;
          } 
          if (bool) {
            widgetRun.start.resolve(k);
          } else {
            widgetRun.end.resolve(k);
          } 
          widgetRun.resolved = true;
          i = k;
          if (j < i5 - 1) {
            i = k;
            if (j < i3)
              if (bool) {
                i = k - -widgetRun.end.margin;
              } else {
                i = k + -widgetRun.end.margin;
              }  
          } 
        } 
        j++;
      } 
    } else if (j == 0) {
      k = (i6 - k) / (i4 + 1);
      if (n > 0)
        k = 0; 
      j = 0;
      m = k;
      while (j < i5) {
        k = j;
        if (bool)
          k = i5 - j + 1; 
        WidgetRun widgetRun = this.widgets.get(k);
        if (widgetRun.widget.getVisibility() == 8) {
          widgetRun.start.resolve(i);
          widgetRun.end.resolve(i);
        } else {
          if (bool) {
            k = i - m;
          } else {
            k = i + m;
          } 
          i = k;
          if (j > 0) {
            i = k;
            if (j >= i2)
              if (bool) {
                i = k - widgetRun.start.margin;
              } else {
                i = k + widgetRun.start.margin;
              }  
          } 
          if (bool) {
            widgetRun.end.resolve(i);
          } else {
            widgetRun.start.resolve(i);
          } 
          n = widgetRun.dimension.value;
          k = n;
          if (widgetRun.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            k = n;
            if (widgetRun.matchConstraintsType == 1)
              k = Math.min(n, widgetRun.dimension.wrapValue); 
          } 
          if (bool) {
            k = i - k;
          } else {
            k = i + k;
          } 
          if (bool) {
            widgetRun.start.resolve(k);
          } else {
            widgetRun.end.resolve(k);
          } 
          i = k;
          if (j < i5 - 1) {
            i = k;
            if (j < i3)
              if (bool) {
                i = k - -widgetRun.end.margin;
              } else {
                i = k + -widgetRun.end.margin;
              }  
          } 
        } 
        j++;
      } 
    } else if (j == 2) {
      if (this.orientation == 0) {
        f = this.widget.getHorizontalBiasPercent();
      } else {
        f = this.widget.getVerticalBiasPercent();
      } 
      float f1 = f;
      if (bool)
        f1 = 1.0F - f; 
      j = (int)((i6 - k) * f1 + 0.5F);
      if (j < 0 || n > 0)
        j = 0; 
      if (bool) {
        i -= j;
      } else {
        i += j;
      } 
      for (j = 0; j < i5; j++) {
        k = j;
        if (bool)
          k = i5 - j + 1; 
        WidgetRun widgetRun = this.widgets.get(k);
        if (widgetRun.widget.getVisibility() == 8) {
          widgetRun.start.resolve(i);
          widgetRun.end.resolve(i);
        } else {
          k = i;
          if (j > 0) {
            k = i;
            if (j >= i2)
              if (bool) {
                k = i - widgetRun.start.margin;
              } else {
                k = i + widgetRun.start.margin;
              }  
          } 
          if (bool) {
            widgetRun.end.resolve(k);
          } else {
            widgetRun.start.resolve(k);
          } 
          i = widgetRun.dimension.value;
          if (widgetRun.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && widgetRun.matchConstraintsType == 1)
            i = widgetRun.dimension.wrapValue; 
          if (bool) {
            k -= i;
          } else {
            k += i;
          } 
          if (bool) {
            widgetRun.start.resolve(k);
          } else {
            widgetRun.end.resolve(k);
          } 
          i = k;
          if (j < i5 - 1) {
            i = k;
            if (j < i3)
              if (bool) {
                i = k - -widgetRun.end.margin;
              } else {
                i = k + -widgetRun.end.margin;
              }  
          } 
        } 
      } 
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\widgets\analyzer\ChainRun.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */