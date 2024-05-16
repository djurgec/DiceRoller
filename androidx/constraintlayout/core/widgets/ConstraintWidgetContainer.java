package androidx.constraintlayout.core.widgets;

import androidx.constraintlayout.core.LinearSystem;
import androidx.constraintlayout.core.Metrics;
import androidx.constraintlayout.core.SolverVariable;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import androidx.constraintlayout.core.widgets.analyzer.DependencyGraph;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class ConstraintWidgetContainer extends WidgetContainer {
  private static final boolean DEBUG = false;
  
  static final boolean DEBUG_GRAPH = false;
  
  private static final boolean DEBUG_LAYOUT = false;
  
  private static final int MAX_ITERATIONS = 8;
  
  static int myCounter = 0;
  
  private WeakReference<ConstraintAnchor> horizontalWrapMax = null;
  
  private WeakReference<ConstraintAnchor> horizontalWrapMin = null;
  
  BasicMeasure mBasicMeasureSolver = new BasicMeasure(this);
  
  int mDebugSolverPassCount = 0;
  
  public DependencyGraph mDependencyGraph = new DependencyGraph(this);
  
  public boolean mGroupsWrapOptimized = false;
  
  private boolean mHeightMeasuredTooSmall = false;
  
  ChainHead[] mHorizontalChainsArray = new ChainHead[4];
  
  public int mHorizontalChainsSize = 0;
  
  public boolean mHorizontalWrapOptimized = false;
  
  private boolean mIsRtl = false;
  
  public BasicMeasure.Measure mMeasure = new BasicMeasure.Measure();
  
  protected BasicMeasure.Measurer mMeasurer = null;
  
  public Metrics mMetrics;
  
  private int mOptimizationLevel = 257;
  
  int mPaddingBottom;
  
  int mPaddingLeft;
  
  int mPaddingRight;
  
  int mPaddingTop;
  
  public boolean mSkipSolver = false;
  
  protected LinearSystem mSystem = new LinearSystem();
  
  ChainHead[] mVerticalChainsArray = new ChainHead[4];
  
  public int mVerticalChainsSize = 0;
  
  public boolean mVerticalWrapOptimized = false;
  
  private boolean mWidthMeasuredTooSmall = false;
  
  public int mWrapFixedHeight = 0;
  
  public int mWrapFixedWidth = 0;
  
  private int pass;
  
  private WeakReference<ConstraintAnchor> verticalWrapMax = null;
  
  private WeakReference<ConstraintAnchor> verticalWrapMin = null;
  
  HashSet<ConstraintWidget> widgetsToAdd = new HashSet<>();
  
  public ConstraintWidgetContainer() {}
  
  public ConstraintWidgetContainer(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public ConstraintWidgetContainer(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public ConstraintWidgetContainer(String paramString, int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
    setDebugName(paramString);
  }
  
  private void addHorizontalChain(ConstraintWidget paramConstraintWidget) {
    int i = this.mHorizontalChainsSize;
    ChainHead[] arrayOfChainHead = this.mHorizontalChainsArray;
    if (i + 1 >= arrayOfChainHead.length)
      this.mHorizontalChainsArray = Arrays.<ChainHead>copyOf(arrayOfChainHead, arrayOfChainHead.length * 2); 
    this.mHorizontalChainsArray[this.mHorizontalChainsSize] = new ChainHead(paramConstraintWidget, 0, isRtl());
    this.mHorizontalChainsSize++;
  }
  
  private void addMaxWrap(ConstraintAnchor paramConstraintAnchor, SolverVariable paramSolverVariable) {
    SolverVariable solverVariable = this.mSystem.createObjectVariable(paramConstraintAnchor);
    this.mSystem.addGreaterThan(paramSolverVariable, solverVariable, 0, 5);
  }
  
  private void addMinWrap(ConstraintAnchor paramConstraintAnchor, SolverVariable paramSolverVariable) {
    SolverVariable solverVariable = this.mSystem.createObjectVariable(paramConstraintAnchor);
    this.mSystem.addGreaterThan(solverVariable, paramSolverVariable, 0, 5);
  }
  
  private void addVerticalChain(ConstraintWidget paramConstraintWidget) {
    int i = this.mVerticalChainsSize;
    ChainHead[] arrayOfChainHead = this.mVerticalChainsArray;
    if (i + 1 >= arrayOfChainHead.length)
      this.mVerticalChainsArray = Arrays.<ChainHead>copyOf(arrayOfChainHead, arrayOfChainHead.length * 2); 
    this.mVerticalChainsArray[this.mVerticalChainsSize] = new ChainHead(paramConstraintWidget, 1, isRtl());
    this.mVerticalChainsSize++;
  }
  
  public static boolean measure(int paramInt1, ConstraintWidget paramConstraintWidget, BasicMeasure.Measurer paramMeasurer, BasicMeasure.Measure paramMeasure, int paramInt2) {
    boolean bool1;
    boolean bool2;
    if (paramMeasurer == null)
      return false; 
    if (paramConstraintWidget.getVisibility() == 8 || paramConstraintWidget instanceof Guideline || paramConstraintWidget instanceof Barrier) {
      paramMeasure.measuredWidth = 0;
      paramMeasure.measuredHeight = 0;
      return false;
    } 
    paramMeasure.horizontalBehavior = paramConstraintWidget.getHorizontalDimensionBehaviour();
    paramMeasure.verticalBehavior = paramConstraintWidget.getVerticalDimensionBehaviour();
    paramMeasure.horizontalDimension = paramConstraintWidget.getWidth();
    paramMeasure.verticalDimension = paramConstraintWidget.getHeight();
    paramMeasure.measuredNeedsSolverPass = false;
    paramMeasure.measureStrategy = paramInt2;
    if (paramMeasure.horizontalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
      paramInt2 = 1;
    } else {
      paramInt2 = 0;
    } 
    if (paramMeasure.verticalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    } 
    if (paramInt2 != 0 && paramConstraintWidget.mDimensionRatio > 0.0F) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (paramInt1 != 0 && paramConstraintWidget.mDimensionRatio > 0.0F) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    int i = paramInt2;
    if (paramInt2 != 0) {
      i = paramInt2;
      if (paramConstraintWidget.hasDanglingDimension(0)) {
        i = paramInt2;
        if (paramConstraintWidget.mMatchConstraintDefaultWidth == 0) {
          i = paramInt2;
          if (!bool2) {
            paramInt2 = 0;
            paramMeasure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            i = paramInt2;
            if (paramInt1 != 0) {
              i = paramInt2;
              if (paramConstraintWidget.mMatchConstraintDefaultHeight == 0) {
                paramMeasure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
                i = paramInt2;
              } 
            } 
          } 
        } 
      } 
    } 
    paramInt2 = paramInt1;
    if (paramInt1 != 0) {
      paramInt2 = paramInt1;
      if (paramConstraintWidget.hasDanglingDimension(1)) {
        paramInt2 = paramInt1;
        if (paramConstraintWidget.mMatchConstraintDefaultHeight == 0) {
          paramInt2 = paramInt1;
          if (!bool1) {
            paramInt1 = 0;
            paramMeasure.verticalBehavior = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            paramInt2 = paramInt1;
            if (i != 0) {
              paramInt2 = paramInt1;
              if (paramConstraintWidget.mMatchConstraintDefaultWidth == 0) {
                paramMeasure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
                paramInt2 = paramInt1;
              } 
            } 
          } 
        } 
      } 
    } 
    if (paramConstraintWidget.isResolvedHorizontally()) {
      i = 0;
      paramMeasure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
    } 
    if (paramConstraintWidget.isResolvedVertically()) {
      paramInt2 = 0;
      paramMeasure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
    } 
    if (bool2)
      if (paramConstraintWidget.mResolvedMatchConstraintDefault[0] == 4) {
        paramMeasure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
      } else if (paramInt2 == 0) {
        if (paramMeasure.verticalBehavior == ConstraintWidget.DimensionBehaviour.FIXED) {
          paramInt1 = paramMeasure.verticalDimension;
        } else {
          paramMeasure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
          paramMeasurer.measure(paramConstraintWidget, paramMeasure);
          paramInt1 = paramMeasure.measuredHeight;
        } 
        paramMeasure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
        paramMeasure.horizontalDimension = (int)(paramConstraintWidget.getDimensionRatio() * paramInt1);
      }  
    if (bool1)
      if (paramConstraintWidget.mResolvedMatchConstraintDefault[1] == 4) {
        paramMeasure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
      } else if (i == 0) {
        if (paramMeasure.horizontalBehavior == ConstraintWidget.DimensionBehaviour.FIXED) {
          paramInt1 = paramMeasure.horizontalDimension;
        } else {
          paramMeasure.verticalBehavior = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
          paramMeasurer.measure(paramConstraintWidget, paramMeasure);
          paramInt1 = paramMeasure.measuredWidth;
        } 
        paramMeasure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
        if (paramConstraintWidget.getDimensionRatioSide() == -1) {
          paramMeasure.verticalDimension = (int)(paramInt1 / paramConstraintWidget.getDimensionRatio());
        } else {
          paramMeasure.verticalDimension = (int)(paramConstraintWidget.getDimensionRatio() * paramInt1);
        } 
      }  
    paramMeasurer.measure(paramConstraintWidget, paramMeasure);
    paramConstraintWidget.setWidth(paramMeasure.measuredWidth);
    paramConstraintWidget.setHeight(paramMeasure.measuredHeight);
    paramConstraintWidget.setHasBaseline(paramMeasure.measuredHasBaseline);
    paramConstraintWidget.setBaselineDistance(paramMeasure.measuredBaseline);
    paramMeasure.measureStrategy = BasicMeasure.Measure.SELF_DIMENSIONS;
    return paramMeasure.measuredNeedsSolverPass;
  }
  
  private void resetChains() {
    this.mHorizontalChainsSize = 0;
    this.mVerticalChainsSize = 0;
  }
  
  void addChain(ConstraintWidget paramConstraintWidget, int paramInt) {
    if (paramInt == 0) {
      addHorizontalChain(paramConstraintWidget);
    } else if (paramInt == 1) {
      addVerticalChain(paramConstraintWidget);
    } 
  }
  
  public boolean addChildrenToSolver(LinearSystem paramLinearSystem) {
    boolean bool1 = optimizeFor(64);
    addToSolver(paramLinearSystem, bool1);
    int j = this.mChildren.size();
    boolean bool = false;
    int i;
    for (i = 0; i < j; i++) {
      ConstraintWidget constraintWidget = this.mChildren.get(i);
      constraintWidget.setInBarrier(0, false);
      constraintWidget.setInBarrier(1, false);
      if (constraintWidget instanceof Barrier)
        bool = true; 
    } 
    if (bool)
      for (i = 0; i < j; i++) {
        ConstraintWidget constraintWidget = this.mChildren.get(i);
        if (constraintWidget instanceof Barrier)
          ((Barrier)constraintWidget).markWidgets(); 
      }  
    this.widgetsToAdd.clear();
    for (i = 0; i < j; i++) {
      ConstraintWidget constraintWidget = this.mChildren.get(i);
      if (constraintWidget.addFirst())
        if (constraintWidget instanceof VirtualLayout) {
          this.widgetsToAdd.add(constraintWidget);
        } else {
          constraintWidget.addToSolver(paramLinearSystem, bool1);
        }  
    } 
    while (this.widgetsToAdd.size() > 0) {
      i = this.widgetsToAdd.size();
      for (VirtualLayout virtualLayout : this.widgetsToAdd) {
        if (virtualLayout.contains(this.widgetsToAdd)) {
          virtualLayout.addToSolver(paramLinearSystem, bool1);
          this.widgetsToAdd.remove(virtualLayout);
          break;
        } 
      } 
      if (i == this.widgetsToAdd.size()) {
        Iterator<ConstraintWidget> iterator = this.widgetsToAdd.iterator();
        while (iterator.hasNext())
          ((ConstraintWidget)iterator.next()).addToSolver(paramLinearSystem, bool1); 
        this.widgetsToAdd.clear();
      } 
    } 
    if (LinearSystem.USE_DEPENDENCY_ORDERING) {
      HashSet<ConstraintWidget> hashSet = new HashSet();
      for (i = 0; i < j; i++) {
        ConstraintWidget constraintWidget = this.mChildren.get(i);
        if (!constraintWidget.addFirst())
          hashSet.add(constraintWidget); 
      } 
      if (getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
        i = 0;
      } else {
        i = 1;
      } 
      addChildrenToSolverByDependency(this, paramLinearSystem, hashSet, i, false);
      for (ConstraintWidget constraintWidget : hashSet) {
        Optimizer.checkMatchParent(this, paramLinearSystem, constraintWidget);
        constraintWidget.addToSolver(paramLinearSystem, bool1);
      } 
    } else {
      for (i = 0; i < j; i++) {
        ConstraintWidget constraintWidget = this.mChildren.get(i);
        if (constraintWidget instanceof ConstraintWidgetContainer) {
          ConstraintWidget.DimensionBehaviour dimensionBehaviour1 = constraintWidget.mListDimensionBehaviors[0];
          ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = constraintWidget.mListDimensionBehaviors[1];
          if (dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)
            constraintWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED); 
          if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)
            constraintWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED); 
          constraintWidget.addToSolver(paramLinearSystem, bool1);
          if (dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)
            constraintWidget.setHorizontalDimensionBehaviour(dimensionBehaviour1); 
          if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)
            constraintWidget.setVerticalDimensionBehaviour(dimensionBehaviour2); 
        } else {
          Optimizer.checkMatchParent(this, paramLinearSystem, constraintWidget);
          if (!constraintWidget.addFirst())
            constraintWidget.addToSolver(paramLinearSystem, bool1); 
        } 
      } 
    } 
    if (this.mHorizontalChainsSize > 0)
      Chain.applyChainConstraints(this, paramLinearSystem, null, 0); 
    if (this.mVerticalChainsSize > 0)
      Chain.applyChainConstraints(this, paramLinearSystem, null, 1); 
    return true;
  }
  
  public void addHorizontalWrapMaxVariable(ConstraintAnchor paramConstraintAnchor) {
    WeakReference<ConstraintAnchor> weakReference = this.horizontalWrapMax;
    if (weakReference == null || weakReference.get() == null || paramConstraintAnchor.getFinalValue() > ((ConstraintAnchor)this.horizontalWrapMax.get()).getFinalValue())
      this.horizontalWrapMax = new WeakReference<>(paramConstraintAnchor); 
  }
  
  public void addHorizontalWrapMinVariable(ConstraintAnchor paramConstraintAnchor) {
    WeakReference<ConstraintAnchor> weakReference = this.horizontalWrapMin;
    if (weakReference == null || weakReference.get() == null || paramConstraintAnchor.getFinalValue() > ((ConstraintAnchor)this.horizontalWrapMin.get()).getFinalValue())
      this.horizontalWrapMin = new WeakReference<>(paramConstraintAnchor); 
  }
  
  void addVerticalWrapMaxVariable(ConstraintAnchor paramConstraintAnchor) {
    WeakReference<ConstraintAnchor> weakReference = this.verticalWrapMax;
    if (weakReference == null || weakReference.get() == null || paramConstraintAnchor.getFinalValue() > ((ConstraintAnchor)this.verticalWrapMax.get()).getFinalValue())
      this.verticalWrapMax = new WeakReference<>(paramConstraintAnchor); 
  }
  
  void addVerticalWrapMinVariable(ConstraintAnchor paramConstraintAnchor) {
    WeakReference<ConstraintAnchor> weakReference = this.verticalWrapMin;
    if (weakReference == null || weakReference.get() == null || paramConstraintAnchor.getFinalValue() > ((ConstraintAnchor)this.verticalWrapMin.get()).getFinalValue())
      this.verticalWrapMin = new WeakReference<>(paramConstraintAnchor); 
  }
  
  public void defineTerminalWidgets() {
    this.mDependencyGraph.defineTerminalWidgets(getHorizontalDimensionBehaviour(), getVerticalDimensionBehaviour());
  }
  
  public boolean directMeasure(boolean paramBoolean) {
    return this.mDependencyGraph.directMeasure(paramBoolean);
  }
  
  public boolean directMeasureSetup(boolean paramBoolean) {
    return this.mDependencyGraph.directMeasureSetup(paramBoolean);
  }
  
  public boolean directMeasureWithOrientation(boolean paramBoolean, int paramInt) {
    return this.mDependencyGraph.directMeasureWithOrientation(paramBoolean, paramInt);
  }
  
  public void fillMetrics(Metrics paramMetrics) {
    this.mMetrics = paramMetrics;
    this.mSystem.fillMetrics(paramMetrics);
  }
  
  public ArrayList<Guideline> getHorizontalGuidelines() {
    ArrayList<ConstraintWidget> arrayList = new ArrayList();
    byte b = 0;
    int i = this.mChildren.size();
    while (b < i) {
      ConstraintWidget constraintWidget = this.mChildren.get(b);
      if (constraintWidget instanceof Guideline) {
        constraintWidget = constraintWidget;
        if (constraintWidget.getOrientation() == 0)
          arrayList.add(constraintWidget); 
      } 
      b++;
    } 
    return (ArrayList)arrayList;
  }
  
  public BasicMeasure.Measurer getMeasurer() {
    return this.mMeasurer;
  }
  
  public int getOptimizationLevel() {
    return this.mOptimizationLevel;
  }
  
  public LinearSystem getSystem() {
    return this.mSystem;
  }
  
  public String getType() {
    return "ConstraintLayout";
  }
  
  public ArrayList<Guideline> getVerticalGuidelines() {
    ArrayList<ConstraintWidget> arrayList = new ArrayList();
    byte b = 0;
    int i = this.mChildren.size();
    while (b < i) {
      ConstraintWidget constraintWidget = this.mChildren.get(b);
      if (constraintWidget instanceof Guideline) {
        constraintWidget = constraintWidget;
        if (constraintWidget.getOrientation() == 1)
          arrayList.add(constraintWidget); 
      } 
      b++;
    } 
    return (ArrayList)arrayList;
  }
  
  public boolean handlesInternalConstraints() {
    return false;
  }
  
  public void invalidateGraph() {
    this.mDependencyGraph.invalidateGraph();
  }
  
  public void invalidateMeasures() {
    this.mDependencyGraph.invalidateMeasures();
  }
  
  public boolean isHeightMeasuredTooSmall() {
    return this.mHeightMeasuredTooSmall;
  }
  
  public boolean isRtl() {
    return this.mIsRtl;
  }
  
  public boolean isWidthMeasuredTooSmall() {
    return this.mWidthMeasuredTooSmall;
  }
  
  public void layout() {
    // Byte code:
    //   0: aload_0
    //   1: iconst_0
    //   2: putfield mX : I
    //   5: aload_0
    //   6: iconst_0
    //   7: putfield mY : I
    //   10: aload_0
    //   11: iconst_0
    //   12: putfield mWidthMeasuredTooSmall : Z
    //   15: aload_0
    //   16: iconst_0
    //   17: putfield mHeightMeasuredTooSmall : Z
    //   20: aload_0
    //   21: getfield mChildren : Ljava/util/ArrayList;
    //   24: invokevirtual size : ()I
    //   27: istore #10
    //   29: iconst_0
    //   30: aload_0
    //   31: invokevirtual getWidth : ()I
    //   34: invokestatic max : (II)I
    //   37: istore_2
    //   38: iconst_0
    //   39: aload_0
    //   40: invokevirtual getHeight : ()I
    //   43: invokestatic max : (II)I
    //   46: istore_3
    //   47: aload_0
    //   48: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   51: iconst_1
    //   52: aaload
    //   53: astore #16
    //   55: aload_0
    //   56: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   59: iconst_0
    //   60: aaload
    //   61: astore #15
    //   63: aload_0
    //   64: getfield mMetrics : Landroidx/constraintlayout/core/Metrics;
    //   67: astore #17
    //   69: aload #17
    //   71: ifnull -> 86
    //   74: aload #17
    //   76: aload #17
    //   78: getfield layouts : J
    //   81: lconst_1
    //   82: ladd
    //   83: putfield layouts : J
    //   86: aload_0
    //   87: getfield pass : I
    //   90: ifne -> 268
    //   93: aload_0
    //   94: getfield mOptimizationLevel : I
    //   97: iconst_1
    //   98: invokestatic enabled : (II)Z
    //   101: ifeq -> 268
    //   104: aload_0
    //   105: aload_0
    //   106: invokevirtual getMeasurer : ()Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;
    //   109: invokestatic solvingPass : (Landroidx/constraintlayout/core/widgets/ConstraintWidgetContainer;Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;)V
    //   112: iconst_0
    //   113: istore_1
    //   114: iload_1
    //   115: iload #10
    //   117: if_icmpge -> 268
    //   120: aload_0
    //   121: getfield mChildren : Ljava/util/ArrayList;
    //   124: iload_1
    //   125: invokevirtual get : (I)Ljava/lang/Object;
    //   128: checkcast androidx/constraintlayout/core/widgets/ConstraintWidget
    //   131: astore #17
    //   133: aload #17
    //   135: invokevirtual isMeasureRequested : ()Z
    //   138: ifeq -> 262
    //   141: aload #17
    //   143: instanceof androidx/constraintlayout/core/widgets/Guideline
    //   146: ifne -> 262
    //   149: aload #17
    //   151: instanceof androidx/constraintlayout/core/widgets/Barrier
    //   154: ifne -> 262
    //   157: aload #17
    //   159: instanceof androidx/constraintlayout/core/widgets/VirtualLayout
    //   162: ifne -> 262
    //   165: aload #17
    //   167: invokevirtual isInVirtualLayout : ()Z
    //   170: ifne -> 262
    //   173: aload #17
    //   175: iconst_0
    //   176: invokevirtual getDimensionBehaviour : (I)Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   179: astore #19
    //   181: aload #17
    //   183: iconst_1
    //   184: invokevirtual getDimensionBehaviour : (I)Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   187: astore #18
    //   189: aload #19
    //   191: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   194: if_acmpne -> 229
    //   197: aload #17
    //   199: getfield mMatchConstraintDefaultWidth : I
    //   202: iconst_1
    //   203: if_icmpeq -> 229
    //   206: aload #18
    //   208: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   211: if_acmpne -> 229
    //   214: aload #17
    //   216: getfield mMatchConstraintDefaultHeight : I
    //   219: iconst_1
    //   220: if_icmpeq -> 229
    //   223: iconst_1
    //   224: istore #4
    //   226: goto -> 232
    //   229: iconst_0
    //   230: istore #4
    //   232: iload #4
    //   234: ifne -> 262
    //   237: new androidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measure
    //   240: dup
    //   241: invokespecial <init> : ()V
    //   244: astore #18
    //   246: iconst_0
    //   247: aload #17
    //   249: aload_0
    //   250: getfield mMeasurer : Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;
    //   253: aload #18
    //   255: getstatic androidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measure.SELF_DIMENSIONS : I
    //   258: invokestatic measure : (ILandroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measure;I)Z
    //   261: pop
    //   262: iinc #1, 1
    //   265: goto -> 114
    //   268: iload #10
    //   270: iconst_2
    //   271: if_icmple -> 409
    //   274: aload #15
    //   276: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   279: if_acmpeq -> 290
    //   282: aload #16
    //   284: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   287: if_acmpne -> 409
    //   290: aload_0
    //   291: getfield mOptimizationLevel : I
    //   294: sipush #1024
    //   297: invokestatic enabled : (II)Z
    //   300: ifeq -> 409
    //   303: aload_0
    //   304: aload_0
    //   305: invokevirtual getMeasurer : ()Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;
    //   308: invokestatic simpleSolvingPass : (Landroidx/constraintlayout/core/widgets/ConstraintWidgetContainer;Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;)Z
    //   311: ifeq -> 409
    //   314: iload_2
    //   315: istore_1
    //   316: aload #15
    //   318: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   321: if_acmpne -> 356
    //   324: iload_2
    //   325: aload_0
    //   326: invokevirtual getWidth : ()I
    //   329: if_icmpge -> 351
    //   332: iload_2
    //   333: ifle -> 351
    //   336: aload_0
    //   337: iload_2
    //   338: invokevirtual setWidth : (I)V
    //   341: aload_0
    //   342: iconst_1
    //   343: putfield mWidthMeasuredTooSmall : Z
    //   346: iload_2
    //   347: istore_1
    //   348: goto -> 356
    //   351: aload_0
    //   352: invokevirtual getWidth : ()I
    //   355: istore_1
    //   356: iload_3
    //   357: istore_2
    //   358: aload #16
    //   360: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   363: if_acmpne -> 398
    //   366: iload_3
    //   367: aload_0
    //   368: invokevirtual getHeight : ()I
    //   371: if_icmpge -> 393
    //   374: iload_3
    //   375: ifle -> 393
    //   378: aload_0
    //   379: iload_3
    //   380: invokevirtual setHeight : (I)V
    //   383: aload_0
    //   384: iconst_1
    //   385: putfield mHeightMeasuredTooSmall : Z
    //   388: iload_3
    //   389: istore_2
    //   390: goto -> 398
    //   393: aload_0
    //   394: invokevirtual getHeight : ()I
    //   397: istore_2
    //   398: iload_1
    //   399: istore #5
    //   401: iconst_1
    //   402: istore_1
    //   403: iload_2
    //   404: istore #4
    //   406: goto -> 417
    //   409: iconst_0
    //   410: istore_1
    //   411: iload_3
    //   412: istore #4
    //   414: iload_2
    //   415: istore #5
    //   417: aload_0
    //   418: bipush #64
    //   420: invokevirtual optimizeFor : (I)Z
    //   423: ifne -> 444
    //   426: aload_0
    //   427: sipush #128
    //   430: invokevirtual optimizeFor : (I)Z
    //   433: ifeq -> 439
    //   436: goto -> 444
    //   439: iconst_0
    //   440: istore_2
    //   441: goto -> 446
    //   444: iconst_1
    //   445: istore_2
    //   446: aload_0
    //   447: getfield mSystem : Landroidx/constraintlayout/core/LinearSystem;
    //   450: iconst_0
    //   451: putfield graphOptimizer : Z
    //   454: aload_0
    //   455: getfield mSystem : Landroidx/constraintlayout/core/LinearSystem;
    //   458: iconst_0
    //   459: putfield newgraphOptimizer : Z
    //   462: aload_0
    //   463: getfield mOptimizationLevel : I
    //   466: ifeq -> 481
    //   469: iload_2
    //   470: ifeq -> 481
    //   473: aload_0
    //   474: getfield mSystem : Landroidx/constraintlayout/core/LinearSystem;
    //   477: iconst_1
    //   478: putfield newgraphOptimizer : Z
    //   481: aload_0
    //   482: getfield mChildren : Ljava/util/ArrayList;
    //   485: astore #17
    //   487: aload_0
    //   488: invokevirtual getHorizontalDimensionBehaviour : ()Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   491: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   494: if_acmpeq -> 516
    //   497: aload_0
    //   498: invokevirtual getVerticalDimensionBehaviour : ()Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   501: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   504: if_acmpne -> 510
    //   507: goto -> 516
    //   510: iconst_0
    //   511: istore #6
    //   513: goto -> 519
    //   516: iconst_1
    //   517: istore #6
    //   519: aload_0
    //   520: invokespecial resetChains : ()V
    //   523: iconst_0
    //   524: istore_3
    //   525: iload_3
    //   526: iload #10
    //   528: if_icmpge -> 566
    //   531: aload_0
    //   532: getfield mChildren : Ljava/util/ArrayList;
    //   535: iload_3
    //   536: invokevirtual get : (I)Ljava/lang/Object;
    //   539: checkcast androidx/constraintlayout/core/widgets/ConstraintWidget
    //   542: astore #18
    //   544: aload #18
    //   546: instanceof androidx/constraintlayout/core/widgets/WidgetContainer
    //   549: ifeq -> 560
    //   552: aload #18
    //   554: checkcast androidx/constraintlayout/core/widgets/WidgetContainer
    //   557: invokevirtual layout : ()V
    //   560: iinc #3, 1
    //   563: goto -> 525
    //   566: aload_0
    //   567: bipush #64
    //   569: invokevirtual optimizeFor : (I)Z
    //   572: istore #14
    //   574: iconst_0
    //   575: istore_3
    //   576: iconst_1
    //   577: istore #12
    //   579: iload_2
    //   580: istore #7
    //   582: iload_3
    //   583: istore_2
    //   584: iload #12
    //   586: ifeq -> 1524
    //   589: iload_2
    //   590: iconst_1
    //   591: iadd
    //   592: istore #9
    //   594: iload #12
    //   596: istore #11
    //   598: aload_0
    //   599: getfield mSystem : Landroidx/constraintlayout/core/LinearSystem;
    //   602: invokevirtual reset : ()V
    //   605: iload #12
    //   607: istore #11
    //   609: aload_0
    //   610: invokespecial resetChains : ()V
    //   613: iload #12
    //   615: istore #11
    //   617: aload_0
    //   618: aload_0
    //   619: getfield mSystem : Landroidx/constraintlayout/core/LinearSystem;
    //   622: invokevirtual createObjectVariables : (Landroidx/constraintlayout/core/LinearSystem;)V
    //   625: iconst_0
    //   626: istore_2
    //   627: iload_2
    //   628: iload #10
    //   630: if_icmpge -> 661
    //   633: iload #12
    //   635: istore #11
    //   637: aload_0
    //   638: getfield mChildren : Ljava/util/ArrayList;
    //   641: iload_2
    //   642: invokevirtual get : (I)Ljava/lang/Object;
    //   645: checkcast androidx/constraintlayout/core/widgets/ConstraintWidget
    //   648: aload_0
    //   649: getfield mSystem : Landroidx/constraintlayout/core/LinearSystem;
    //   652: invokevirtual createObjectVariables : (Landroidx/constraintlayout/core/LinearSystem;)V
    //   655: iinc #2, 1
    //   658: goto -> 627
    //   661: iload #12
    //   663: istore #11
    //   665: aload_0
    //   666: aload_0
    //   667: getfield mSystem : Landroidx/constraintlayout/core/LinearSystem;
    //   670: invokevirtual addChildrenToSolver : (Landroidx/constraintlayout/core/LinearSystem;)Z
    //   673: istore #12
    //   675: iload #12
    //   677: istore #11
    //   679: aload_0
    //   680: getfield verticalWrapMin : Ljava/lang/ref/WeakReference;
    //   683: astore #18
    //   685: aload #18
    //   687: ifnull -> 740
    //   690: iload #12
    //   692: istore #11
    //   694: aload #18
    //   696: invokevirtual get : ()Ljava/lang/Object;
    //   699: ifnull -> 740
    //   702: iload #12
    //   704: istore #11
    //   706: aload_0
    //   707: aload_0
    //   708: getfield verticalWrapMin : Ljava/lang/ref/WeakReference;
    //   711: invokevirtual get : ()Ljava/lang/Object;
    //   714: checkcast androidx/constraintlayout/core/widgets/ConstraintAnchor
    //   717: aload_0
    //   718: getfield mSystem : Landroidx/constraintlayout/core/LinearSystem;
    //   721: aload_0
    //   722: getfield mTop : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   725: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   728: invokespecial addMinWrap : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;Landroidx/constraintlayout/core/SolverVariable;)V
    //   731: iload #12
    //   733: istore #11
    //   735: aload_0
    //   736: aconst_null
    //   737: putfield verticalWrapMin : Ljava/lang/ref/WeakReference;
    //   740: iload #12
    //   742: istore #11
    //   744: aload_0
    //   745: getfield verticalWrapMax : Ljava/lang/ref/WeakReference;
    //   748: astore #18
    //   750: aload #18
    //   752: ifnull -> 805
    //   755: iload #12
    //   757: istore #11
    //   759: aload #18
    //   761: invokevirtual get : ()Ljava/lang/Object;
    //   764: ifnull -> 805
    //   767: iload #12
    //   769: istore #11
    //   771: aload_0
    //   772: aload_0
    //   773: getfield verticalWrapMax : Ljava/lang/ref/WeakReference;
    //   776: invokevirtual get : ()Ljava/lang/Object;
    //   779: checkcast androidx/constraintlayout/core/widgets/ConstraintAnchor
    //   782: aload_0
    //   783: getfield mSystem : Landroidx/constraintlayout/core/LinearSystem;
    //   786: aload_0
    //   787: getfield mBottom : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   790: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   793: invokespecial addMaxWrap : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;Landroidx/constraintlayout/core/SolverVariable;)V
    //   796: iload #12
    //   798: istore #11
    //   800: aload_0
    //   801: aconst_null
    //   802: putfield verticalWrapMax : Ljava/lang/ref/WeakReference;
    //   805: iload #12
    //   807: istore #11
    //   809: aload_0
    //   810: getfield horizontalWrapMin : Ljava/lang/ref/WeakReference;
    //   813: astore #18
    //   815: aload #18
    //   817: ifnull -> 870
    //   820: iload #12
    //   822: istore #11
    //   824: aload #18
    //   826: invokevirtual get : ()Ljava/lang/Object;
    //   829: ifnull -> 870
    //   832: iload #12
    //   834: istore #11
    //   836: aload_0
    //   837: aload_0
    //   838: getfield horizontalWrapMin : Ljava/lang/ref/WeakReference;
    //   841: invokevirtual get : ()Ljava/lang/Object;
    //   844: checkcast androidx/constraintlayout/core/widgets/ConstraintAnchor
    //   847: aload_0
    //   848: getfield mSystem : Landroidx/constraintlayout/core/LinearSystem;
    //   851: aload_0
    //   852: getfield mLeft : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   855: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   858: invokespecial addMinWrap : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;Landroidx/constraintlayout/core/SolverVariable;)V
    //   861: iload #12
    //   863: istore #11
    //   865: aload_0
    //   866: aconst_null
    //   867: putfield horizontalWrapMin : Ljava/lang/ref/WeakReference;
    //   870: iload #12
    //   872: istore #11
    //   874: aload_0
    //   875: getfield horizontalWrapMax : Ljava/lang/ref/WeakReference;
    //   878: astore #18
    //   880: aload #18
    //   882: ifnull -> 935
    //   885: iload #12
    //   887: istore #11
    //   889: aload #18
    //   891: invokevirtual get : ()Ljava/lang/Object;
    //   894: ifnull -> 935
    //   897: iload #12
    //   899: istore #11
    //   901: aload_0
    //   902: aload_0
    //   903: getfield horizontalWrapMax : Ljava/lang/ref/WeakReference;
    //   906: invokevirtual get : ()Ljava/lang/Object;
    //   909: checkcast androidx/constraintlayout/core/widgets/ConstraintAnchor
    //   912: aload_0
    //   913: getfield mSystem : Landroidx/constraintlayout/core/LinearSystem;
    //   916: aload_0
    //   917: getfield mRight : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   920: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   923: invokespecial addMaxWrap : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;Landroidx/constraintlayout/core/SolverVariable;)V
    //   926: iload #12
    //   928: istore #11
    //   930: aload_0
    //   931: aconst_null
    //   932: putfield horizontalWrapMax : Ljava/lang/ref/WeakReference;
    //   935: iload #12
    //   937: ifeq -> 951
    //   940: iload #12
    //   942: istore #11
    //   944: aload_0
    //   945: getfield mSystem : Landroidx/constraintlayout/core/LinearSystem;
    //   948: invokevirtual minimize : ()V
    //   951: iload #12
    //   953: istore #11
    //   955: goto -> 992
    //   958: astore #18
    //   960: aload #18
    //   962: invokevirtual printStackTrace : ()V
    //   965: getstatic java/lang/System.out : Ljava/io/PrintStream;
    //   968: new java/lang/StringBuilder
    //   971: dup
    //   972: invokespecial <init> : ()V
    //   975: ldc_w 'EXCEPTION : '
    //   978: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   981: aload #18
    //   983: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   986: invokevirtual toString : ()Ljava/lang/String;
    //   989: invokevirtual println : (Ljava/lang/String;)V
    //   992: iload #11
    //   994: ifeq -> 1013
    //   997: aload_0
    //   998: aload_0
    //   999: getfield mSystem : Landroidx/constraintlayout/core/LinearSystem;
    //   1002: getstatic androidx/constraintlayout/core/widgets/Optimizer.flags : [Z
    //   1005: invokevirtual updateChildrenFromSolver : (Landroidx/constraintlayout/core/LinearSystem;[Z)Z
    //   1008: istore #11
    //   1010: goto -> 1060
    //   1013: aload_0
    //   1014: aload_0
    //   1015: getfield mSystem : Landroidx/constraintlayout/core/LinearSystem;
    //   1018: iload #14
    //   1020: invokevirtual updateFromSolver : (Landroidx/constraintlayout/core/LinearSystem;Z)V
    //   1023: iconst_0
    //   1024: istore_2
    //   1025: iload_2
    //   1026: iload #10
    //   1028: if_icmpge -> 1057
    //   1031: aload_0
    //   1032: getfield mChildren : Ljava/util/ArrayList;
    //   1035: iload_2
    //   1036: invokevirtual get : (I)Ljava/lang/Object;
    //   1039: checkcast androidx/constraintlayout/core/widgets/ConstraintWidget
    //   1042: aload_0
    //   1043: getfield mSystem : Landroidx/constraintlayout/core/LinearSystem;
    //   1046: iload #14
    //   1048: invokevirtual updateFromSolver : (Landroidx/constraintlayout/core/LinearSystem;Z)V
    //   1051: iinc #2, 1
    //   1054: goto -> 1025
    //   1057: iconst_0
    //   1058: istore #11
    //   1060: iload #6
    //   1062: ifeq -> 1269
    //   1065: iload #9
    //   1067: bipush #8
    //   1069: if_icmpge -> 1269
    //   1072: getstatic androidx/constraintlayout/core/widgets/Optimizer.flags : [Z
    //   1075: iconst_2
    //   1076: baload
    //   1077: ifeq -> 1269
    //   1080: iconst_0
    //   1081: istore #8
    //   1083: iconst_0
    //   1084: istore_3
    //   1085: iconst_0
    //   1086: istore_2
    //   1087: iload_2
    //   1088: iload #10
    //   1090: if_icmpge -> 1146
    //   1093: aload_0
    //   1094: getfield mChildren : Ljava/util/ArrayList;
    //   1097: iload_2
    //   1098: invokevirtual get : (I)Ljava/lang/Object;
    //   1101: checkcast androidx/constraintlayout/core/widgets/ConstraintWidget
    //   1104: astore #18
    //   1106: iload #8
    //   1108: aload #18
    //   1110: getfield mX : I
    //   1113: aload #18
    //   1115: invokevirtual getWidth : ()I
    //   1118: iadd
    //   1119: invokestatic max : (II)I
    //   1122: istore #8
    //   1124: iload_3
    //   1125: aload #18
    //   1127: getfield mY : I
    //   1130: aload #18
    //   1132: invokevirtual getHeight : ()I
    //   1135: iadd
    //   1136: invokestatic max : (II)I
    //   1139: istore_3
    //   1140: iinc #2, 1
    //   1143: goto -> 1087
    //   1146: iload #11
    //   1148: istore #12
    //   1150: aload_0
    //   1151: getfield mMinWidth : I
    //   1154: iload #8
    //   1156: invokestatic max : (II)I
    //   1159: istore #8
    //   1161: aload_0
    //   1162: getfield mMinHeight : I
    //   1165: iload_3
    //   1166: invokestatic max : (II)I
    //   1169: istore_3
    //   1170: iload_1
    //   1171: istore_2
    //   1172: iload #12
    //   1174: istore #11
    //   1176: aload #15
    //   1178: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1181: if_acmpne -> 1219
    //   1184: iload_1
    //   1185: istore_2
    //   1186: iload #12
    //   1188: istore #11
    //   1190: aload_0
    //   1191: invokevirtual getWidth : ()I
    //   1194: iload #8
    //   1196: if_icmpge -> 1219
    //   1199: aload_0
    //   1200: iload #8
    //   1202: invokevirtual setWidth : (I)V
    //   1205: aload_0
    //   1206: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1209: iconst_0
    //   1210: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1213: aastore
    //   1214: iconst_1
    //   1215: istore_2
    //   1216: iconst_1
    //   1217: istore #11
    //   1219: iload_2
    //   1220: istore_1
    //   1221: iload #11
    //   1223: istore #12
    //   1225: aload #16
    //   1227: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1230: if_acmpne -> 1273
    //   1233: iload_2
    //   1234: istore_1
    //   1235: iload #11
    //   1237: istore #12
    //   1239: aload_0
    //   1240: invokevirtual getHeight : ()I
    //   1243: iload_3
    //   1244: if_icmpge -> 1273
    //   1247: aload_0
    //   1248: iload_3
    //   1249: invokevirtual setHeight : (I)V
    //   1252: aload_0
    //   1253: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1256: iconst_1
    //   1257: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1260: aastore
    //   1261: iconst_1
    //   1262: istore_1
    //   1263: iconst_1
    //   1264: istore #11
    //   1266: goto -> 1277
    //   1269: iload #11
    //   1271: istore #12
    //   1273: iload #12
    //   1275: istore #11
    //   1277: aload_0
    //   1278: getfield mMinWidth : I
    //   1281: aload_0
    //   1282: invokevirtual getWidth : ()I
    //   1285: invokestatic max : (II)I
    //   1288: istore_3
    //   1289: iload_1
    //   1290: istore_2
    //   1291: iload_3
    //   1292: aload_0
    //   1293: invokevirtual getWidth : ()I
    //   1296: if_icmple -> 1318
    //   1299: aload_0
    //   1300: iload_3
    //   1301: invokevirtual setWidth : (I)V
    //   1304: aload_0
    //   1305: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1308: iconst_0
    //   1309: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1312: aastore
    //   1313: iconst_1
    //   1314: istore_2
    //   1315: iconst_1
    //   1316: istore #11
    //   1318: aload_0
    //   1319: getfield mMinHeight : I
    //   1322: aload_0
    //   1323: invokevirtual getHeight : ()I
    //   1326: invokestatic max : (II)I
    //   1329: istore_1
    //   1330: iload_1
    //   1331: aload_0
    //   1332: invokevirtual getHeight : ()I
    //   1335: if_icmple -> 1357
    //   1338: aload_0
    //   1339: iload_1
    //   1340: invokevirtual setHeight : (I)V
    //   1343: aload_0
    //   1344: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1347: iconst_1
    //   1348: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1351: aastore
    //   1352: iconst_1
    //   1353: istore_2
    //   1354: iconst_1
    //   1355: istore #11
    //   1357: iload #11
    //   1359: istore #12
    //   1361: iload_2
    //   1362: istore_1
    //   1363: iload_2
    //   1364: ifne -> 1505
    //   1367: iload #11
    //   1369: istore #13
    //   1371: iload_2
    //   1372: istore_3
    //   1373: aload_0
    //   1374: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1377: iconst_0
    //   1378: aaload
    //   1379: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1382: if_acmpne -> 1436
    //   1385: iload #11
    //   1387: istore #13
    //   1389: iload_2
    //   1390: istore_3
    //   1391: iload #5
    //   1393: ifle -> 1436
    //   1396: iload #11
    //   1398: istore #13
    //   1400: iload_2
    //   1401: istore_3
    //   1402: aload_0
    //   1403: invokevirtual getWidth : ()I
    //   1406: iload #5
    //   1408: if_icmple -> 1436
    //   1411: aload_0
    //   1412: iconst_1
    //   1413: putfield mWidthMeasuredTooSmall : Z
    //   1416: iconst_1
    //   1417: istore_3
    //   1418: aload_0
    //   1419: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1422: iconst_0
    //   1423: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1426: aastore
    //   1427: aload_0
    //   1428: iload #5
    //   1430: invokevirtual setWidth : (I)V
    //   1433: iconst_1
    //   1434: istore #13
    //   1436: iload #13
    //   1438: istore #12
    //   1440: iload_3
    //   1441: istore_1
    //   1442: aload_0
    //   1443: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1446: iconst_1
    //   1447: aaload
    //   1448: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1451: if_acmpne -> 1505
    //   1454: iload #13
    //   1456: istore #12
    //   1458: iload_3
    //   1459: istore_1
    //   1460: iload #4
    //   1462: ifle -> 1505
    //   1465: iload #13
    //   1467: istore #12
    //   1469: iload_3
    //   1470: istore_1
    //   1471: aload_0
    //   1472: invokevirtual getHeight : ()I
    //   1475: iload #4
    //   1477: if_icmple -> 1505
    //   1480: aload_0
    //   1481: iconst_1
    //   1482: putfield mHeightMeasuredTooSmall : Z
    //   1485: iconst_1
    //   1486: istore_1
    //   1487: aload_0
    //   1488: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1491: iconst_1
    //   1492: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1495: aastore
    //   1496: aload_0
    //   1497: iload #4
    //   1499: invokevirtual setHeight : (I)V
    //   1502: iconst_1
    //   1503: istore #12
    //   1505: iload #9
    //   1507: bipush #8
    //   1509: if_icmple -> 1518
    //   1512: iconst_0
    //   1513: istore #12
    //   1515: goto -> 1518
    //   1518: iload #9
    //   1520: istore_2
    //   1521: goto -> 584
    //   1524: aload_0
    //   1525: aload #17
    //   1527: checkcast java/util/ArrayList
    //   1530: putfield mChildren : Ljava/util/ArrayList;
    //   1533: iload_1
    //   1534: ifeq -> 1553
    //   1537: aload_0
    //   1538: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1541: iconst_0
    //   1542: aload #15
    //   1544: aastore
    //   1545: aload_0
    //   1546: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1549: iconst_1
    //   1550: aload #16
    //   1552: aastore
    //   1553: aload_0
    //   1554: aload_0
    //   1555: getfield mSystem : Landroidx/constraintlayout/core/LinearSystem;
    //   1558: invokevirtual getCache : ()Landroidx/constraintlayout/core/Cache;
    //   1561: invokevirtual resetSolverVariables : (Landroidx/constraintlayout/core/Cache;)V
    //   1564: return
    // Exception table:
    //   from	to	target	type
    //   598	605	958	java/lang/Exception
    //   609	613	958	java/lang/Exception
    //   617	625	958	java/lang/Exception
    //   637	655	958	java/lang/Exception
    //   665	675	958	java/lang/Exception
    //   679	685	958	java/lang/Exception
    //   694	702	958	java/lang/Exception
    //   706	731	958	java/lang/Exception
    //   735	740	958	java/lang/Exception
    //   744	750	958	java/lang/Exception
    //   759	767	958	java/lang/Exception
    //   771	796	958	java/lang/Exception
    //   800	805	958	java/lang/Exception
    //   809	815	958	java/lang/Exception
    //   824	832	958	java/lang/Exception
    //   836	861	958	java/lang/Exception
    //   865	870	958	java/lang/Exception
    //   874	880	958	java/lang/Exception
    //   889	897	958	java/lang/Exception
    //   901	926	958	java/lang/Exception
    //   930	935	958	java/lang/Exception
    //   944	951	958	java/lang/Exception
  }
  
  public long measure(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9) {
    this.mPaddingLeft = paramInt8;
    this.mPaddingTop = paramInt9;
    return this.mBasicMeasureSolver.solverMeasure(this, paramInt1, paramInt8, paramInt9, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7);
  }
  
  public boolean optimizeFor(int paramInt) {
    boolean bool;
    if ((this.mOptimizationLevel & paramInt) == paramInt) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void reset() {
    this.mSystem.reset();
    this.mPaddingLeft = 0;
    this.mPaddingRight = 0;
    this.mPaddingTop = 0;
    this.mPaddingBottom = 0;
    this.mSkipSolver = false;
    super.reset();
  }
  
  public void setMeasurer(BasicMeasure.Measurer paramMeasurer) {
    this.mMeasurer = paramMeasurer;
    this.mDependencyGraph.setMeasurer(paramMeasurer);
  }
  
  public void setOptimizationLevel(int paramInt) {
    this.mOptimizationLevel = paramInt;
    LinearSystem.USE_DEPENDENCY_ORDERING = optimizeFor(512);
  }
  
  public void setPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mPaddingLeft = paramInt1;
    this.mPaddingTop = paramInt2;
    this.mPaddingRight = paramInt3;
    this.mPaddingBottom = paramInt4;
  }
  
  public void setPass(int paramInt) {
    this.pass = paramInt;
  }
  
  public void setRtl(boolean paramBoolean) {
    this.mIsRtl = paramBoolean;
  }
  
  public boolean updateChildrenFromSolver(LinearSystem paramLinearSystem, boolean[] paramArrayOfboolean) {
    paramArrayOfboolean[2] = false;
    boolean bool1 = optimizeFor(64);
    updateFromSolver(paramLinearSystem, bool1);
    int i = this.mChildren.size();
    boolean bool = false;
    for (byte b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = this.mChildren.get(b);
      constraintWidget.updateFromSolver(paramLinearSystem, bool1);
      if (constraintWidget.hasDimensionOverride())
        bool = true; 
    } 
    return bool;
  }
  
  public void updateFromRuns(boolean paramBoolean1, boolean paramBoolean2) {
    super.updateFromRuns(paramBoolean1, paramBoolean2);
    int i = this.mChildren.size();
    for (byte b = 0; b < i; b++)
      ((ConstraintWidget)this.mChildren.get(b)).updateFromRuns(paramBoolean1, paramBoolean2); 
  }
  
  public void updateHierarchy() {
    this.mBasicMeasureSolver.updateHierarchy(this);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\widgets\ConstraintWidgetContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */