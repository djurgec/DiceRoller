package androidx.constraintlayout.core.widgets.analyzer;

import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class DependencyGraph {
  private static final boolean USE_GROUPS = true;
  
  private ConstraintWidgetContainer container;
  
  private ConstraintWidgetContainer mContainer;
  
  ArrayList<RunGroup> mGroups = new ArrayList<>();
  
  private BasicMeasure.Measure mMeasure = new BasicMeasure.Measure();
  
  private BasicMeasure.Measurer mMeasurer = null;
  
  private boolean mNeedBuildGraph = true;
  
  private boolean mNeedRedoMeasures = true;
  
  private ArrayList<WidgetRun> mRuns = new ArrayList<>();
  
  private ArrayList<RunGroup> runGroups = new ArrayList<>();
  
  public DependencyGraph(ConstraintWidgetContainer paramConstraintWidgetContainer) {
    this.container = paramConstraintWidgetContainer;
    this.mContainer = paramConstraintWidgetContainer;
  }
  
  private void applyGroup(DependencyNode paramDependencyNode1, int paramInt1, int paramInt2, DependencyNode paramDependencyNode2, ArrayList<RunGroup> paramArrayList, RunGroup paramRunGroup) {
    RunGroup runGroup;
    WidgetRun widgetRun = paramDependencyNode1.run;
    if (widgetRun.runGroup != null || widgetRun == this.container.horizontalRun || widgetRun == this.container.verticalRun)
      return; 
    if (paramRunGroup == null) {
      runGroup = new RunGroup(widgetRun, paramInt2);
      paramArrayList.add(runGroup);
    } else {
      runGroup = paramRunGroup;
    } 
    widgetRun.runGroup = runGroup;
    runGroup.add(widgetRun);
    for (Dependency dependency : widgetRun.start.dependencies) {
      if (dependency instanceof DependencyNode)
        applyGroup((DependencyNode)dependency, paramInt1, 0, paramDependencyNode2, paramArrayList, runGroup); 
    } 
    for (Dependency dependency : widgetRun.end.dependencies) {
      if (dependency instanceof DependencyNode)
        applyGroup((DependencyNode)dependency, paramInt1, 1, paramDependencyNode2, paramArrayList, runGroup); 
    } 
    if (paramInt1 == 1 && widgetRun instanceof VerticalWidgetRun)
      for (Dependency dependency : ((VerticalWidgetRun)widgetRun).baseline.dependencies) {
        if (dependency instanceof DependencyNode)
          applyGroup((DependencyNode)dependency, paramInt1, 2, paramDependencyNode2, paramArrayList, runGroup); 
      }  
    for (DependencyNode dependencyNode : widgetRun.start.targets) {
      if (dependencyNode == paramDependencyNode2)
        runGroup.dual = true; 
      applyGroup(dependencyNode, paramInt1, 0, paramDependencyNode2, paramArrayList, runGroup);
    } 
    for (DependencyNode dependencyNode : widgetRun.end.targets) {
      if (dependencyNode == paramDependencyNode2)
        runGroup.dual = true; 
      applyGroup(dependencyNode, paramInt1, 1, paramDependencyNode2, paramArrayList, runGroup);
    } 
    if (paramInt1 == 1 && widgetRun instanceof VerticalWidgetRun) {
      Iterator<DependencyNode> iterator = ((VerticalWidgetRun)widgetRun).baseline.targets.iterator();
      while (iterator.hasNext())
        applyGroup(iterator.next(), paramInt1, 2, paramDependencyNode2, paramArrayList, runGroup); 
    } 
  }
  
  private boolean basicMeasureWidgets(ConstraintWidgetContainer paramConstraintWidgetContainer) {
    for (ConstraintWidget constraintWidget : paramConstraintWidgetContainer.mChildren) {
      ConstraintWidget.DimensionBehaviour dimensionBehaviour1 = constraintWidget.mListDimensionBehaviors[0];
      ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = constraintWidget.mListDimensionBehaviors[1];
      if (constraintWidget.getVisibility() == 8) {
        constraintWidget.measured = true;
        continue;
      } 
      if (constraintWidget.mMatchConstraintPercentWidth < 1.0F && dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT)
        constraintWidget.mMatchConstraintDefaultWidth = 2; 
      if (constraintWidget.mMatchConstraintPercentHeight < 1.0F && dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT)
        constraintWidget.mMatchConstraintDefaultHeight = 2; 
      if (constraintWidget.getDimensionRatio() > 0.0F)
        if (dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.FIXED)) {
          constraintWidget.mMatchConstraintDefaultWidth = 3;
        } else if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.FIXED)) {
          constraintWidget.mMatchConstraintDefaultHeight = 3;
        } else if (dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
          if (constraintWidget.mMatchConstraintDefaultWidth == 0)
            constraintWidget.mMatchConstraintDefaultWidth = 3; 
          if (constraintWidget.mMatchConstraintDefaultHeight == 0)
            constraintWidget.mMatchConstraintDefaultHeight = 3; 
        }  
      if (dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mMatchConstraintDefaultWidth == 1 && (constraintWidget.mLeft.mTarget == null || constraintWidget.mRight.mTarget == null))
        dimensionBehaviour1 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT; 
      if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mMatchConstraintDefaultHeight == 1 && (constraintWidget.mTop.mTarget == null || constraintWidget.mBottom.mTarget == null))
        dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT; 
      constraintWidget.horizontalRun.dimensionBehavior = dimensionBehaviour1;
      constraintWidget.horizontalRun.matchConstraintsType = constraintWidget.mMatchConstraintDefaultWidth;
      constraintWidget.verticalRun.dimensionBehavior = dimensionBehaviour2;
      constraintWidget.verticalRun.matchConstraintsType = constraintWidget.mMatchConstraintDefaultHeight;
      if ((dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.MATCH_PARENT || dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.FIXED || dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) && (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_PARENT || dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.FIXED || dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)) {
        int i = constraintWidget.getWidth();
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = dimensionBehaviour1;
        if (dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
          i = paramConstraintWidgetContainer.getWidth() - constraintWidget.mLeft.mMargin - constraintWidget.mRight.mMargin;
          dimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
        } 
        int j = constraintWidget.getHeight();
        if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
          j = paramConstraintWidgetContainer.getHeight();
          int k = constraintWidget.mTop.mMargin;
          int m = constraintWidget.mBottom.mMargin;
          dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.FIXED;
          j = j - k - m;
        } 
        measure(constraintWidget, dimensionBehaviour, i, dimensionBehaviour2, j);
        constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
        constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
        constraintWidget.measured = true;
        continue;
      } 
      if (dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.FIXED)) {
        if (constraintWidget.mMatchConstraintDefaultWidth == 3) {
          if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)
            measure(constraintWidget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0); 
          int i = constraintWidget.getHeight();
          int j = (int)(i * constraintWidget.mDimensionRatio + 0.5F);
          measure(constraintWidget, ConstraintWidget.DimensionBehaviour.FIXED, j, ConstraintWidget.DimensionBehaviour.FIXED, i);
          constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
          constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
          constraintWidget.measured = true;
          continue;
        } 
        if (constraintWidget.mMatchConstraintDefaultWidth == 1) {
          measure(constraintWidget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0, dimensionBehaviour2, 0);
          constraintWidget.horizontalRun.dimension.wrapValue = constraintWidget.getWidth();
          continue;
        } 
        if (constraintWidget.mMatchConstraintDefaultWidth == 2) {
          if (paramConstraintWidgetContainer.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.FIXED || paramConstraintWidgetContainer.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            float f = constraintWidget.mMatchConstraintPercentWidth;
            int i = (int)(paramConstraintWidgetContainer.getWidth() * f + 0.5F);
            int j = constraintWidget.getHeight();
            measure(constraintWidget, ConstraintWidget.DimensionBehaviour.FIXED, i, dimensionBehaviour2, j);
            constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
            constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
            constraintWidget.measured = true;
            continue;
          } 
        } else if ((constraintWidget.mListAnchors[0]).mTarget == null || (constraintWidget.mListAnchors[1]).mTarget == null) {
          measure(constraintWidget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0, dimensionBehaviour2, 0);
          constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
          constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
          constraintWidget.measured = true;
          continue;
        } 
      } 
      if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.FIXED)) {
        if (constraintWidget.mMatchConstraintDefaultHeight == 3) {
          if (dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)
            measure(constraintWidget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0); 
          int i = constraintWidget.getWidth();
          float f = constraintWidget.mDimensionRatio;
          if (constraintWidget.getDimensionRatioSide() == -1)
            f = 1.0F / f; 
          int j = (int)(i * f + 0.5F);
          measure(constraintWidget, ConstraintWidget.DimensionBehaviour.FIXED, i, ConstraintWidget.DimensionBehaviour.FIXED, j);
          constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
          constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
          constraintWidget.measured = true;
          continue;
        } 
        if (constraintWidget.mMatchConstraintDefaultHeight == 1) {
          measure(constraintWidget, dimensionBehaviour1, 0, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0);
          constraintWidget.verticalRun.dimension.wrapValue = constraintWidget.getHeight();
          continue;
        } 
        if (constraintWidget.mMatchConstraintDefaultHeight == 2) {
          if (paramConstraintWidgetContainer.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.FIXED || paramConstraintWidgetContainer.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            float f = constraintWidget.mMatchConstraintPercentHeight;
            int j = constraintWidget.getWidth();
            int i = (int)(paramConstraintWidgetContainer.getHeight() * f + 0.5F);
            measure(constraintWidget, dimensionBehaviour1, j, ConstraintWidget.DimensionBehaviour.FIXED, i);
            constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
            constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
            constraintWidget.measured = true;
            continue;
          } 
        } else if ((constraintWidget.mListAnchors[2]).mTarget == null || (constraintWidget.mListAnchors[3]).mTarget == null) {
          measure(constraintWidget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0, dimensionBehaviour2, 0);
          constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
          constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
          constraintWidget.measured = true;
          continue;
        } 
      } 
      if (dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
        if (constraintWidget.mMatchConstraintDefaultWidth == 1 || constraintWidget.mMatchConstraintDefaultHeight == 1) {
          measure(constraintWidget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0);
          constraintWidget.horizontalRun.dimension.wrapValue = constraintWidget.getWidth();
          constraintWidget.verticalRun.dimension.wrapValue = constraintWidget.getHeight();
          continue;
        } 
        if (constraintWidget.mMatchConstraintDefaultHeight == 2 && constraintWidget.mMatchConstraintDefaultWidth == 2 && paramConstraintWidgetContainer.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.FIXED && paramConstraintWidgetContainer.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.FIXED) {
          float f1 = constraintWidget.mMatchConstraintPercentWidth;
          float f2 = constraintWidget.mMatchConstraintPercentHeight;
          int i = (int)(paramConstraintWidgetContainer.getWidth() * f1 + 0.5F);
          int j = (int)(paramConstraintWidgetContainer.getHeight() * f2 + 0.5F);
          measure(constraintWidget, ConstraintWidget.DimensionBehaviour.FIXED, i, ConstraintWidget.DimensionBehaviour.FIXED, j);
          constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
          constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
          constraintWidget.measured = true;
        } 
      } 
    } 
    return false;
  }
  
  private int computeWrap(ConstraintWidgetContainer paramConstraintWidgetContainer, int paramInt) {
    int i = this.mGroups.size();
    long l = 0L;
    for (byte b = 0; b < i; b++)
      l = Math.max(l, ((RunGroup)this.mGroups.get(b)).computeWrapSize(paramConstraintWidgetContainer, paramInt)); 
    return (int)l;
  }
  
  private void displayGraph() {
    String str = "digraph {\n";
    Iterator<WidgetRun> iterator = this.mRuns.iterator();
    while (iterator.hasNext())
      str = generateDisplayGraph(iterator.next(), str); 
    str = str + "\n}\n";
    System.out.println("content:<<\n" + str + "\n>>");
  }
  
  private void findGroup(WidgetRun paramWidgetRun, int paramInt, ArrayList<RunGroup> paramArrayList) {
    for (Dependency dependency : paramWidgetRun.start.dependencies) {
      if (dependency instanceof DependencyNode) {
        applyGroup((DependencyNode)dependency, paramInt, 0, paramWidgetRun.end, paramArrayList, null);
        continue;
      } 
      if (dependency instanceof WidgetRun)
        applyGroup(((WidgetRun)dependency).start, paramInt, 0, paramWidgetRun.end, paramArrayList, null); 
    } 
    for (Dependency dependency : paramWidgetRun.end.dependencies) {
      if (dependency instanceof DependencyNode) {
        applyGroup((DependencyNode)dependency, paramInt, 1, paramWidgetRun.start, paramArrayList, null);
        continue;
      } 
      if (dependency instanceof WidgetRun)
        applyGroup(((WidgetRun)dependency).end, paramInt, 1, paramWidgetRun.start, paramArrayList, null); 
    } 
    if (paramInt == 1)
      for (Dependency dependency : ((VerticalWidgetRun)paramWidgetRun).baseline.dependencies) {
        if (dependency instanceof DependencyNode)
          applyGroup((DependencyNode)dependency, paramInt, 2, null, paramArrayList, null); 
      }  
  }
  
  private String generateChainDisplayGraph(ChainRun paramChainRun, String paramString) {
    int i = paramChainRun.orientation;
    StringBuilder stringBuilder = new StringBuilder("subgraph ");
    stringBuilder.append("cluster_");
    stringBuilder.append(paramChainRun.widget.getDebugName());
    if (i == 0) {
      stringBuilder.append("_h");
    } else {
      stringBuilder.append("_v");
    } 
    stringBuilder.append(" {\n");
    String str2 = "";
    Iterator<WidgetRun> iterator = paramChainRun.widgets.iterator();
    String str1;
    for (str1 = str2; iterator.hasNext(); str1 = generateDisplayGraph(widgetRun, str1)) {
      WidgetRun widgetRun = iterator.next();
      stringBuilder.append(widgetRun.widget.getDebugName());
      if (i == 0) {
        stringBuilder.append("_HORIZONTAL");
      } else {
        stringBuilder.append("_VERTICAL");
      } 
      stringBuilder.append(";\n");
    } 
    stringBuilder.append("}\n");
    return paramString + str1 + stringBuilder;
  }
  
  private String generateDisplayGraph(WidgetRun paramWidgetRun, String paramString) {
    DependencyNode dependencyNode1 = paramWidgetRun.start;
    DependencyNode dependencyNode2 = paramWidgetRun.end;
    StringBuilder stringBuilder = new StringBuilder(paramString);
    if (!(paramWidgetRun instanceof HelperReferences) && dependencyNode1.dependencies.isEmpty() && (dependencyNode2.dependencies.isEmpty() & dependencyNode1.targets.isEmpty()) != 0 && dependencyNode2.targets.isEmpty())
      return paramString; 
    stringBuilder.append(nodeDefinition(paramWidgetRun));
    boolean bool = isCenteredConnection(dependencyNode1, dependencyNode2);
    String str = generateDisplayNode(dependencyNode2, bool, generateDisplayNode(dependencyNode1, bool, paramString));
    paramString = str;
    if (paramWidgetRun instanceof VerticalWidgetRun)
      paramString = generateDisplayNode(((VerticalWidgetRun)paramWidgetRun).baseline, bool, str); 
    if (paramWidgetRun instanceof HorizontalWidgetRun || (paramWidgetRun instanceof ChainRun && ((ChainRun)paramWidgetRun).orientation == 0)) {
      ConstraintWidget.DimensionBehaviour dimensionBehaviour = paramWidgetRun.widget.getHorizontalDimensionBehaviour();
      if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.FIXED || dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
        if (!dependencyNode1.targets.isEmpty() && dependencyNode2.targets.isEmpty()) {
          stringBuilder.append("\n");
          stringBuilder.append(dependencyNode2.name());
          stringBuilder.append(" -> ");
          stringBuilder.append(dependencyNode1.name());
          stringBuilder.append("\n");
        } else if (dependencyNode1.targets.isEmpty() && !dependencyNode2.targets.isEmpty()) {
          stringBuilder.append("\n");
          stringBuilder.append(dependencyNode1.name());
          stringBuilder.append(" -> ");
          stringBuilder.append(dependencyNode2.name());
          stringBuilder.append("\n");
        } 
      } else if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && paramWidgetRun.widget.getDimensionRatio() > 0.0F) {
        stringBuilder.append("\n");
        stringBuilder.append(paramWidgetRun.widget.getDebugName());
        stringBuilder.append("_HORIZONTAL -> ");
        stringBuilder.append(paramWidgetRun.widget.getDebugName());
        stringBuilder.append("_VERTICAL;\n");
      } 
    } else if (paramWidgetRun instanceof VerticalWidgetRun || (paramWidgetRun instanceof ChainRun && ((ChainRun)paramWidgetRun).orientation == 1)) {
      ConstraintWidget.DimensionBehaviour dimensionBehaviour = paramWidgetRun.widget.getVerticalDimensionBehaviour();
      if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.FIXED || dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
        if (!dependencyNode1.targets.isEmpty() && dependencyNode2.targets.isEmpty()) {
          stringBuilder.append("\n");
          stringBuilder.append(dependencyNode2.name());
          stringBuilder.append(" -> ");
          stringBuilder.append(dependencyNode1.name());
          stringBuilder.append("\n");
        } else if (dependencyNode1.targets.isEmpty() && !dependencyNode2.targets.isEmpty()) {
          stringBuilder.append("\n");
          stringBuilder.append(dependencyNode1.name());
          stringBuilder.append(" -> ");
          stringBuilder.append(dependencyNode2.name());
          stringBuilder.append("\n");
        } 
      } else if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && paramWidgetRun.widget.getDimensionRatio() > 0.0F) {
        stringBuilder.append("\n");
        stringBuilder.append(paramWidgetRun.widget.getDebugName());
        stringBuilder.append("_VERTICAL -> ");
        stringBuilder.append(paramWidgetRun.widget.getDebugName());
        stringBuilder.append("_HORIZONTAL;\n");
      } 
    } 
    return (paramWidgetRun instanceof ChainRun) ? generateChainDisplayGraph((ChainRun)paramWidgetRun, paramString) : stringBuilder.toString();
  }
  
  private String generateDisplayNode(DependencyNode paramDependencyNode, boolean paramBoolean, String paramString) {
    // Byte code:
    //   0: new java/lang/StringBuilder
    //   3: dup
    //   4: aload_3
    //   5: invokespecial <init> : (Ljava/lang/String;)V
    //   8: astore #5
    //   10: aload_1
    //   11: getfield targets : Ljava/util/List;
    //   14: invokeinterface iterator : ()Ljava/util/Iterator;
    //   19: astore #6
    //   21: aload #6
    //   23: invokeinterface hasNext : ()Z
    //   28: ifeq -> 333
    //   31: aload #6
    //   33: invokeinterface next : ()Ljava/lang/Object;
    //   38: checkcast androidx/constraintlayout/core/widgets/analyzer/DependencyNode
    //   41: astore #4
    //   43: new java/lang/StringBuilder
    //   46: dup
    //   47: invokespecial <init> : ()V
    //   50: ldc_w '\\n'
    //   53: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   56: aload_1
    //   57: invokevirtual name : ()Ljava/lang/String;
    //   60: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   63: invokevirtual toString : ()Ljava/lang/String;
    //   66: astore_3
    //   67: new java/lang/StringBuilder
    //   70: dup
    //   71: invokespecial <init> : ()V
    //   74: aload_3
    //   75: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   78: ldc_w ' -> '
    //   81: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   84: aload #4
    //   86: invokevirtual name : ()Ljava/lang/String;
    //   89: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   92: invokevirtual toString : ()Ljava/lang/String;
    //   95: astore #4
    //   97: aload_1
    //   98: getfield margin : I
    //   101: ifgt -> 121
    //   104: iload_2
    //   105: ifne -> 121
    //   108: aload #4
    //   110: astore_3
    //   111: aload_1
    //   112: getfield run : Landroidx/constraintlayout/core/widgets/analyzer/WidgetRun;
    //   115: instanceof androidx/constraintlayout/core/widgets/analyzer/HelperReferences
    //   118: ifeq -> 304
    //   121: new java/lang/StringBuilder
    //   124: dup
    //   125: invokespecial <init> : ()V
    //   128: aload #4
    //   130: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   133: ldc_w '['
    //   136: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   139: invokevirtual toString : ()Ljava/lang/String;
    //   142: astore #4
    //   144: aload #4
    //   146: astore_3
    //   147: aload_1
    //   148: getfield margin : I
    //   151: ifle -> 219
    //   154: new java/lang/StringBuilder
    //   157: dup
    //   158: invokespecial <init> : ()V
    //   161: aload #4
    //   163: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   166: ldc_w 'label="'
    //   169: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   172: aload_1
    //   173: getfield margin : I
    //   176: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   179: ldc_w '"'
    //   182: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   185: invokevirtual toString : ()Ljava/lang/String;
    //   188: astore #4
    //   190: aload #4
    //   192: astore_3
    //   193: iload_2
    //   194: ifeq -> 219
    //   197: new java/lang/StringBuilder
    //   200: dup
    //   201: invokespecial <init> : ()V
    //   204: aload #4
    //   206: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   209: ldc_w ','
    //   212: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   215: invokevirtual toString : ()Ljava/lang/String;
    //   218: astore_3
    //   219: aload_3
    //   220: astore #4
    //   222: iload_2
    //   223: ifeq -> 248
    //   226: new java/lang/StringBuilder
    //   229: dup
    //   230: invokespecial <init> : ()V
    //   233: aload_3
    //   234: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   237: ldc_w ' style=dashed '
    //   240: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   243: invokevirtual toString : ()Ljava/lang/String;
    //   246: astore #4
    //   248: aload #4
    //   250: astore_3
    //   251: aload_1
    //   252: getfield run : Landroidx/constraintlayout/core/widgets/analyzer/WidgetRun;
    //   255: instanceof androidx/constraintlayout/core/widgets/analyzer/HelperReferences
    //   258: ifeq -> 283
    //   261: new java/lang/StringBuilder
    //   264: dup
    //   265: invokespecial <init> : ()V
    //   268: aload #4
    //   270: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   273: ldc_w ' style=bold,color=gray '
    //   276: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   279: invokevirtual toString : ()Ljava/lang/String;
    //   282: astore_3
    //   283: new java/lang/StringBuilder
    //   286: dup
    //   287: invokespecial <init> : ()V
    //   290: aload_3
    //   291: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   294: ldc_w ']'
    //   297: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   300: invokevirtual toString : ()Ljava/lang/String;
    //   303: astore_3
    //   304: aload #5
    //   306: new java/lang/StringBuilder
    //   309: dup
    //   310: invokespecial <init> : ()V
    //   313: aload_3
    //   314: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   317: ldc_w '\\n'
    //   320: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   323: invokevirtual toString : ()Ljava/lang/String;
    //   326: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   329: pop
    //   330: goto -> 21
    //   333: aload #5
    //   335: invokevirtual toString : ()Ljava/lang/String;
    //   338: areturn
  }
  
  private boolean isCenteredConnection(DependencyNode paramDependencyNode1, DependencyNode paramDependencyNode2) {
    boolean bool;
    int i = 0;
    int j = 0;
    Iterator<DependencyNode> iterator2 = paramDependencyNode1.targets.iterator();
    while (iterator2.hasNext()) {
      int k = i;
      if ((DependencyNode)iterator2.next() != paramDependencyNode2)
        k = i + 1; 
      i = k;
    } 
    Iterator<DependencyNode> iterator1 = paramDependencyNode2.targets.iterator();
    while (iterator1.hasNext()) {
      int k = j;
      if ((DependencyNode)iterator1.next() != paramDependencyNode1)
        k = j + 1; 
      j = k;
    } 
    if (i > 0 && j > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void measure(ConstraintWidget paramConstraintWidget, ConstraintWidget.DimensionBehaviour paramDimensionBehaviour1, int paramInt1, ConstraintWidget.DimensionBehaviour paramDimensionBehaviour2, int paramInt2) {
    this.mMeasure.horizontalBehavior = paramDimensionBehaviour1;
    this.mMeasure.verticalBehavior = paramDimensionBehaviour2;
    this.mMeasure.horizontalDimension = paramInt1;
    this.mMeasure.verticalDimension = paramInt2;
    this.mMeasurer.measure(paramConstraintWidget, this.mMeasure);
    paramConstraintWidget.setWidth(this.mMeasure.measuredWidth);
    paramConstraintWidget.setHeight(this.mMeasure.measuredHeight);
    paramConstraintWidget.setHasBaseline(this.mMeasure.measuredHasBaseline);
    paramConstraintWidget.setBaselineDistance(this.mMeasure.measuredBaseline);
  }
  
  private String nodeDefinition(WidgetRun paramWidgetRun) {
    ConstraintWidget.DimensionBehaviour dimensionBehaviour;
    boolean bool = paramWidgetRun instanceof VerticalWidgetRun;
    String str = paramWidgetRun.widget.getDebugName();
    StringBuilder stringBuilder = new StringBuilder(str);
    ConstraintWidget constraintWidget = paramWidgetRun.widget;
    if (!bool) {
      dimensionBehaviour = constraintWidget.getHorizontalDimensionBehaviour();
    } else {
      dimensionBehaviour = dimensionBehaviour.getVerticalDimensionBehaviour();
    } 
    RunGroup runGroup = paramWidgetRun.runGroup;
    if (!bool) {
      stringBuilder.append("_HORIZONTAL");
    } else {
      stringBuilder.append("_VERTICAL");
    } 
    stringBuilder.append(" [shape=none, label=<");
    stringBuilder.append("<TABLE BORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"2\">");
    stringBuilder.append("  <TR>");
    if (!bool) {
      stringBuilder.append("    <TD ");
      if (paramWidgetRun.start.resolved)
        stringBuilder.append(" BGCOLOR=\"green\""); 
      stringBuilder.append(" PORT=\"LEFT\" BORDER=\"1\">L</TD>");
    } else {
      stringBuilder.append("    <TD ");
      if (paramWidgetRun.start.resolved)
        stringBuilder.append(" BGCOLOR=\"green\""); 
      stringBuilder.append(" PORT=\"TOP\" BORDER=\"1\">T</TD>");
    } 
    stringBuilder.append("    <TD BORDER=\"1\" ");
    if (paramWidgetRun.dimension.resolved && !paramWidgetRun.widget.measured) {
      stringBuilder.append(" BGCOLOR=\"green\" ");
    } else if (paramWidgetRun.dimension.resolved) {
      stringBuilder.append(" BGCOLOR=\"lightgray\" ");
    } else if (paramWidgetRun.widget.measured) {
      stringBuilder.append(" BGCOLOR=\"yellow\" ");
    } 
    if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT)
      stringBuilder.append("style=\"dashed\""); 
    stringBuilder.append(">");
    stringBuilder.append(str);
    if (runGroup != null) {
      stringBuilder.append(" [");
      stringBuilder.append(runGroup.groupIndex + 1);
      stringBuilder.append("/");
      stringBuilder.append(RunGroup.index);
      stringBuilder.append("]");
    } 
    stringBuilder.append(" </TD>");
    if (!bool) {
      stringBuilder.append("    <TD ");
      if (paramWidgetRun.end.resolved)
        stringBuilder.append(" BGCOLOR=\"green\""); 
      stringBuilder.append(" PORT=\"RIGHT\" BORDER=\"1\">R</TD>");
    } else {
      stringBuilder.append("    <TD ");
      if (((VerticalWidgetRun)paramWidgetRun).baseline.resolved)
        stringBuilder.append(" BGCOLOR=\"green\""); 
      stringBuilder.append(" PORT=\"BASELINE\" BORDER=\"1\">b</TD>");
      stringBuilder.append("    <TD ");
      if (paramWidgetRun.end.resolved)
        stringBuilder.append(" BGCOLOR=\"green\""); 
      stringBuilder.append(" PORT=\"BOTTOM\" BORDER=\"1\">B</TD>");
    } 
    stringBuilder.append("  </TR></TABLE>");
    stringBuilder.append(">];\n");
    return stringBuilder.toString();
  }
  
  public void buildGraph() {
    buildGraph(this.mRuns);
    this.mGroups.clear();
    RunGroup.index = 0;
    findGroup(this.container.horizontalRun, 0, this.mGroups);
    findGroup(this.container.verticalRun, 1, this.mGroups);
    this.mNeedBuildGraph = false;
  }
  
  public void buildGraph(ArrayList<WidgetRun> paramArrayList) {
    paramArrayList.clear();
    this.mContainer.horizontalRun.clear();
    this.mContainer.verticalRun.clear();
    paramArrayList.add(this.mContainer.horizontalRun);
    paramArrayList.add(this.mContainer.verticalRun);
    HashSet<ChainRun> hashSet = null;
    for (ConstraintWidget constraintWidget : this.mContainer.mChildren) {
      if (constraintWidget instanceof androidx.constraintlayout.core.widgets.Guideline) {
        paramArrayList.add(new GuidelineReference(constraintWidget));
        continue;
      } 
      if (constraintWidget.isInHorizontalChain()) {
        if (constraintWidget.horizontalChainRun == null)
          constraintWidget.horizontalChainRun = new ChainRun(constraintWidget, 0); 
        HashSet<ChainRun> hashSet1 = hashSet;
        if (hashSet == null)
          hashSet1 = new HashSet(); 
        hashSet1.add(constraintWidget.horizontalChainRun);
        hashSet = hashSet1;
      } else {
        paramArrayList.add(constraintWidget.horizontalRun);
      } 
      if (constraintWidget.isInVerticalChain()) {
        if (constraintWidget.verticalChainRun == null)
          constraintWidget.verticalChainRun = new ChainRun(constraintWidget, 1); 
        HashSet<ChainRun> hashSet1 = hashSet;
        if (hashSet == null)
          hashSet1 = new HashSet<>(); 
        hashSet1.add(constraintWidget.verticalChainRun);
        hashSet = hashSet1;
      } else {
        paramArrayList.add(constraintWidget.verticalRun);
      } 
      if (constraintWidget instanceof androidx.constraintlayout.core.widgets.HelperWidget)
        paramArrayList.add(new HelperReferences(constraintWidget)); 
    } 
    if (hashSet != null)
      paramArrayList.addAll((Collection)hashSet); 
    null = paramArrayList.iterator();
    while (null.hasNext())
      ((WidgetRun)null.next()).clear(); 
    for (WidgetRun widgetRun : paramArrayList) {
      if (widgetRun.widget == this.mContainer)
        continue; 
      widgetRun.apply();
    } 
  }
  
  public void defineTerminalWidgets(ConstraintWidget.DimensionBehaviour paramDimensionBehaviour1, ConstraintWidget.DimensionBehaviour paramDimensionBehaviour2) {
    if (this.mNeedBuildGraph) {
      buildGraph();
      boolean bool = false;
      for (ConstraintWidget constraintWidget : this.container.mChildren) {
        constraintWidget.isTerminalWidget[0] = true;
        constraintWidget.isTerminalWidget[1] = true;
        if (constraintWidget instanceof androidx.constraintlayout.core.widgets.Barrier)
          bool = true; 
      } 
      if (!bool)
        for (RunGroup runGroup : this.mGroups) {
          boolean bool1;
          boolean bool2;
          if (paramDimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
          if (paramDimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
            bool2 = true;
          } else {
            bool2 = false;
          } 
          runGroup.defineTerminalWidgets(bool1, bool2);
        }  
    } 
  }
  
  public boolean directMeasure(boolean paramBoolean) {
    int j = paramBoolean & true;
    if (this.mNeedBuildGraph || this.mNeedRedoMeasures) {
      for (ConstraintWidget constraintWidget : this.container.mChildren) {
        constraintWidget.ensureWidgetRuns();
        constraintWidget.measured = false;
        constraintWidget.horizontalRun.reset();
        constraintWidget.verticalRun.reset();
      } 
      this.container.ensureWidgetRuns();
      this.container.measured = false;
      this.container.horizontalRun.reset();
      this.container.verticalRun.reset();
      this.mNeedRedoMeasures = false;
    } 
    if (basicMeasureWidgets(this.mContainer))
      return false; 
    this.container.setX(0);
    this.container.setY(0);
    ConstraintWidget.DimensionBehaviour dimensionBehaviour1 = this.container.getDimensionBehaviour(0);
    ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = this.container.getDimensionBehaviour(1);
    if (this.mNeedBuildGraph)
      buildGraph(); 
    int m = this.container.getX();
    int k = this.container.getY();
    this.container.horizontalRun.start.resolve(m);
    this.container.verticalRun.start.resolve(k);
    measureWidgets();
    if (dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
      int n = j;
      if (j != 0) {
        Iterator<WidgetRun> iterator1 = this.mRuns.iterator();
        while (true) {
          n = j;
          if (iterator1.hasNext()) {
            if (!((WidgetRun)iterator1.next()).supportsWrapComputation()) {
              n = 0;
              break;
            } 
            continue;
          } 
          break;
        } 
      } 
      if (n != 0 && dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
        this.container.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
        ConstraintWidgetContainer constraintWidgetContainer = this.container;
        constraintWidgetContainer.setWidth(computeWrap(constraintWidgetContainer, 0));
        this.container.horizontalRun.dimension.resolve(this.container.getWidth());
      } 
      if (n != 0 && dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
        this.container.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
        ConstraintWidgetContainer constraintWidgetContainer = this.container;
        constraintWidgetContainer.setHeight(computeWrap(constraintWidgetContainer, 1));
        this.container.verticalRun.dimension.resolve(this.container.getHeight());
      } 
    } 
    int i = 0;
    if (this.container.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.FIXED || this.container.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
      i = this.container.getWidth() + m;
      this.container.horizontalRun.end.resolve(i);
      this.container.horizontalRun.dimension.resolve(i - m);
      measureWidgets();
      if (this.container.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.FIXED || this.container.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
        i = this.container.getHeight() + k;
        this.container.verticalRun.end.resolve(i);
        this.container.verticalRun.dimension.resolve(i - k);
      } 
      measureWidgets();
      i = 1;
    } 
    for (WidgetRun widgetRun : this.mRuns) {
      if (widgetRun.widget == this.container && !widgetRun.resolved)
        continue; 
      widgetRun.applyToWidget();
    } 
    boolean bool = true;
    Iterator<WidgetRun> iterator = this.mRuns.iterator();
    while (true) {
      paramBoolean = bool;
      if (iterator.hasNext()) {
        WidgetRun widgetRun = iterator.next();
        if (i == 0 && widgetRun.widget == this.container)
          continue; 
        if (!widgetRun.start.resolved) {
          paramBoolean = false;
          break;
        } 
        if (!widgetRun.end.resolved && !(widgetRun instanceof GuidelineReference)) {
          paramBoolean = false;
          break;
        } 
        if (!widgetRun.dimension.resolved && !(widgetRun instanceof ChainRun) && !(widgetRun instanceof GuidelineReference)) {
          paramBoolean = false;
          break;
        } 
        continue;
      } 
      break;
    } 
    this.container.setHorizontalDimensionBehaviour(dimensionBehaviour1);
    this.container.setVerticalDimensionBehaviour(dimensionBehaviour2);
    return paramBoolean;
  }
  
  public boolean directMeasureSetup(boolean paramBoolean) {
    if (this.mNeedBuildGraph) {
      for (ConstraintWidget constraintWidget : this.container.mChildren) {
        constraintWidget.ensureWidgetRuns();
        constraintWidget.measured = false;
        constraintWidget.horizontalRun.dimension.resolved = false;
        constraintWidget.horizontalRun.resolved = false;
        constraintWidget.horizontalRun.reset();
        constraintWidget.verticalRun.dimension.resolved = false;
        constraintWidget.verticalRun.resolved = false;
        constraintWidget.verticalRun.reset();
      } 
      this.container.ensureWidgetRuns();
      this.container.measured = false;
      this.container.horizontalRun.dimension.resolved = false;
      this.container.horizontalRun.resolved = false;
      this.container.horizontalRun.reset();
      this.container.verticalRun.dimension.resolved = false;
      this.container.verticalRun.resolved = false;
      this.container.verticalRun.reset();
      buildGraph();
    } 
    if (basicMeasureWidgets(this.mContainer))
      return false; 
    this.container.setX(0);
    this.container.setY(0);
    this.container.horizontalRun.start.resolve(0);
    this.container.verticalRun.start.resolve(0);
    return true;
  }
  
  public boolean directMeasureWithOrientation(boolean paramBoolean, int paramInt) {
    int j = paramBoolean & true;
    ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = this.container.getDimensionBehaviour(0);
    ConstraintWidget.DimensionBehaviour dimensionBehaviour1 = this.container.getDimensionBehaviour(1);
    int m = this.container.getX();
    int k = this.container.getY();
    if (j != 0 && (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)) {
      int n;
      Iterator<WidgetRun> iterator1 = this.mRuns.iterator();
      while (true) {
        n = j;
        if (iterator1.hasNext()) {
          WidgetRun widgetRun = iterator1.next();
          if (widgetRun.orientation == paramInt && !widgetRun.supportsWrapComputation()) {
            n = 0;
            break;
          } 
          continue;
        } 
        break;
      } 
      if (paramInt == 0) {
        if (n != 0 && dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
          this.container.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
          ConstraintWidgetContainer constraintWidgetContainer = this.container;
          constraintWidgetContainer.setWidth(computeWrap(constraintWidgetContainer, 0));
          this.container.horizontalRun.dimension.resolve(this.container.getWidth());
        } 
      } else if (n != 0 && dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
        this.container.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
        ConstraintWidgetContainer constraintWidgetContainer = this.container;
        constraintWidgetContainer.setHeight(computeWrap(constraintWidgetContainer, 1));
        this.container.verticalRun.dimension.resolve(this.container.getHeight());
      } 
    } 
    int i = 0;
    if (paramInt == 0) {
      if (this.container.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.FIXED || this.container.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
        i = this.container.getWidth() + m;
        this.container.horizontalRun.end.resolve(i);
        this.container.horizontalRun.dimension.resolve(i - m);
        i = 1;
      } 
    } else if (this.container.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.FIXED || this.container.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
      i = this.container.getHeight() + k;
      this.container.verticalRun.end.resolve(i);
      this.container.verticalRun.dimension.resolve(i - k);
      i = 1;
    } 
    measureWidgets();
    for (WidgetRun widgetRun : this.mRuns) {
      if (widgetRun.orientation != paramInt || (widgetRun.widget == this.container && !widgetRun.resolved))
        continue; 
      widgetRun.applyToWidget();
    } 
    boolean bool = true;
    Iterator<WidgetRun> iterator = this.mRuns.iterator();
    while (true) {
      paramBoolean = bool;
      if (iterator.hasNext()) {
        WidgetRun widgetRun = iterator.next();
        if (widgetRun.orientation != paramInt || (i == 0 && widgetRun.widget == this.container))
          continue; 
        if (!widgetRun.start.resolved) {
          paramBoolean = false;
          break;
        } 
        if (!widgetRun.end.resolved) {
          paramBoolean = false;
          break;
        } 
        if (!(widgetRun instanceof ChainRun) && !widgetRun.dimension.resolved) {
          paramBoolean = false;
          break;
        } 
        continue;
      } 
      break;
    } 
    this.container.setHorizontalDimensionBehaviour(dimensionBehaviour2);
    this.container.setVerticalDimensionBehaviour(dimensionBehaviour1);
    return paramBoolean;
  }
  
  public void invalidateGraph() {
    this.mNeedBuildGraph = true;
  }
  
  public void invalidateMeasures() {
    this.mNeedRedoMeasures = true;
  }
  
  public void measureWidgets() {
    Iterator iterator = this.container.mChildren.iterator();
    while (true) {
      while (true)
        break; 
      if (((ConstraintWidget)SYNTHETIC_LOCAL_VARIABLE_8).measured && ((ConstraintWidget)SYNTHETIC_LOCAL_VARIABLE_8).verticalRun.baselineDimension != null)
        ((ConstraintWidget)SYNTHETIC_LOCAL_VARIABLE_8).verticalRun.baselineDimension.resolve(SYNTHETIC_LOCAL_VARIABLE_8.getBaselineDistance()); 
    } 
  }
  
  public void setMeasurer(BasicMeasure.Measurer paramMeasurer) {
    this.mMeasurer = paramMeasurer;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\widgets\analyzer\DependencyGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */