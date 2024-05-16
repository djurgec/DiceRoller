package androidx.constraintlayout.core.widgets.analyzer;

import androidx.constraintlayout.core.LinearSystem;
import androidx.constraintlayout.core.Metrics;
import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.core.widgets.Optimizer;
import androidx.constraintlayout.core.widgets.VirtualLayout;
import java.util.ArrayList;

public class BasicMeasure {
  public static final int AT_MOST = -2147483648;
  
  private static final boolean DEBUG = false;
  
  public static final int EXACTLY = 1073741824;
  
  public static final int FIXED = -3;
  
  public static final int MATCH_PARENT = -1;
  
  private static final int MODE_SHIFT = 30;
  
  public static final int UNSPECIFIED = 0;
  
  public static final int WRAP_CONTENT = -2;
  
  private ConstraintWidgetContainer constraintWidgetContainer;
  
  private Measure mMeasure = new Measure();
  
  private final ArrayList<ConstraintWidget> mVariableDimensionsWidgets = new ArrayList<>();
  
  public BasicMeasure(ConstraintWidgetContainer paramConstraintWidgetContainer) {
    this.constraintWidgetContainer = paramConstraintWidgetContainer;
  }
  
  private boolean measure(Measurer paramMeasurer, ConstraintWidget paramConstraintWidget, int paramInt) {
    boolean bool;
    this.mMeasure.horizontalBehavior = paramConstraintWidget.getHorizontalDimensionBehaviour();
    this.mMeasure.verticalBehavior = paramConstraintWidget.getVerticalDimensionBehaviour();
    this.mMeasure.horizontalDimension = paramConstraintWidget.getWidth();
    this.mMeasure.verticalDimension = paramConstraintWidget.getHeight();
    this.mMeasure.measuredNeedsSolverPass = false;
    this.mMeasure.measureStrategy = paramInt;
    if (this.mMeasure.horizontalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
      bool = true;
    } else {
      bool = false;
    } 
    if (this.mMeasure.verticalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    if (bool && paramConstraintWidget.mDimensionRatio > 0.0F) {
      bool = true;
    } else {
      bool = false;
    } 
    if (paramInt != 0 && paramConstraintWidget.mDimensionRatio > 0.0F) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    if (bool && paramConstraintWidget.mResolvedMatchConstraintDefault[0] == 4)
      this.mMeasure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED; 
    if (paramInt != 0 && paramConstraintWidget.mResolvedMatchConstraintDefault[1] == 4)
      this.mMeasure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED; 
    paramMeasurer.measure(paramConstraintWidget, this.mMeasure);
    paramConstraintWidget.setWidth(this.mMeasure.measuredWidth);
    paramConstraintWidget.setHeight(this.mMeasure.measuredHeight);
    paramConstraintWidget.setHasBaseline(this.mMeasure.measuredHasBaseline);
    paramConstraintWidget.setBaselineDistance(this.mMeasure.measuredBaseline);
    this.mMeasure.measureStrategy = Measure.SELF_DIMENSIONS;
    return this.mMeasure.measuredNeedsSolverPass;
  }
  
  private void measureChildren(ConstraintWidgetContainer paramConstraintWidgetContainer) {
    // Byte code:
    //   0: aload_1
    //   1: getfield mChildren : Ljava/util/ArrayList;
    //   4: invokevirtual size : ()I
    //   7: istore #5
    //   9: aload_1
    //   10: bipush #64
    //   12: invokevirtual optimizeFor : (I)Z
    //   15: istore #6
    //   17: aload_1
    //   18: invokevirtual getMeasurer : ()Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;
    //   21: astore #7
    //   23: iconst_0
    //   24: istore #4
    //   26: iload #4
    //   28: iload #5
    //   30: if_icmpge -> 386
    //   33: aload_1
    //   34: getfield mChildren : Ljava/util/ArrayList;
    //   37: iload #4
    //   39: invokevirtual get : (I)Ljava/lang/Object;
    //   42: checkcast androidx/constraintlayout/core/widgets/ConstraintWidget
    //   45: astore #9
    //   47: aload #9
    //   49: instanceof androidx/constraintlayout/core/widgets/Guideline
    //   52: ifeq -> 58
    //   55: goto -> 380
    //   58: aload #9
    //   60: instanceof androidx/constraintlayout/core/widgets/Barrier
    //   63: ifeq -> 69
    //   66: goto -> 380
    //   69: aload #9
    //   71: invokevirtual isInVirtualLayout : ()Z
    //   74: ifeq -> 80
    //   77: goto -> 380
    //   80: iload #6
    //   82: ifeq -> 132
    //   85: aload #9
    //   87: getfield horizontalRun : Landroidx/constraintlayout/core/widgets/analyzer/HorizontalWidgetRun;
    //   90: ifnull -> 132
    //   93: aload #9
    //   95: getfield verticalRun : Landroidx/constraintlayout/core/widgets/analyzer/VerticalWidgetRun;
    //   98: ifnull -> 132
    //   101: aload #9
    //   103: getfield horizontalRun : Landroidx/constraintlayout/core/widgets/analyzer/HorizontalWidgetRun;
    //   106: getfield dimension : Landroidx/constraintlayout/core/widgets/analyzer/DimensionDependency;
    //   109: getfield resolved : Z
    //   112: ifeq -> 132
    //   115: aload #9
    //   117: getfield verticalRun : Landroidx/constraintlayout/core/widgets/analyzer/VerticalWidgetRun;
    //   120: getfield dimension : Landroidx/constraintlayout/core/widgets/analyzer/DimensionDependency;
    //   123: getfield resolved : Z
    //   126: ifeq -> 132
    //   129: goto -> 380
    //   132: iconst_0
    //   133: istore_3
    //   134: aload #9
    //   136: iconst_0
    //   137: invokevirtual getDimensionBehaviour : (I)Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   140: astore #8
    //   142: aload #9
    //   144: iconst_1
    //   145: invokevirtual getDimensionBehaviour : (I)Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   148: astore #10
    //   150: iload_3
    //   151: istore_2
    //   152: aload #8
    //   154: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   157: if_acmpne -> 194
    //   160: iload_3
    //   161: istore_2
    //   162: aload #9
    //   164: getfield mMatchConstraintDefaultWidth : I
    //   167: iconst_1
    //   168: if_icmpeq -> 194
    //   171: iload_3
    //   172: istore_2
    //   173: aload #10
    //   175: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   178: if_acmpne -> 194
    //   181: iload_3
    //   182: istore_2
    //   183: aload #9
    //   185: getfield mMatchConstraintDefaultHeight : I
    //   188: iconst_1
    //   189: if_icmpeq -> 194
    //   192: iconst_1
    //   193: istore_2
    //   194: iload_2
    //   195: istore_3
    //   196: iload_2
    //   197: ifne -> 336
    //   200: iload_2
    //   201: istore_3
    //   202: aload_1
    //   203: iconst_1
    //   204: invokevirtual optimizeFor : (I)Z
    //   207: ifeq -> 336
    //   210: iload_2
    //   211: istore_3
    //   212: aload #9
    //   214: instanceof androidx/constraintlayout/core/widgets/VirtualLayout
    //   217: ifne -> 336
    //   220: iload_2
    //   221: istore_3
    //   222: aload #8
    //   224: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   227: if_acmpne -> 262
    //   230: iload_2
    //   231: istore_3
    //   232: aload #9
    //   234: getfield mMatchConstraintDefaultWidth : I
    //   237: ifne -> 262
    //   240: iload_2
    //   241: istore_3
    //   242: aload #10
    //   244: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   247: if_acmpeq -> 262
    //   250: iload_2
    //   251: istore_3
    //   252: aload #9
    //   254: invokevirtual isInHorizontalChain : ()Z
    //   257: ifne -> 262
    //   260: iconst_1
    //   261: istore_3
    //   262: iload_3
    //   263: istore_2
    //   264: aload #10
    //   266: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   269: if_acmpne -> 304
    //   272: iload_3
    //   273: istore_2
    //   274: aload #9
    //   276: getfield mMatchConstraintDefaultHeight : I
    //   279: ifne -> 304
    //   282: iload_3
    //   283: istore_2
    //   284: aload #8
    //   286: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   289: if_acmpeq -> 304
    //   292: iload_3
    //   293: istore_2
    //   294: aload #9
    //   296: invokevirtual isInHorizontalChain : ()Z
    //   299: ifne -> 304
    //   302: iconst_1
    //   303: istore_2
    //   304: aload #8
    //   306: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   309: if_acmpeq -> 322
    //   312: iload_2
    //   313: istore_3
    //   314: aload #10
    //   316: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   319: if_acmpne -> 336
    //   322: iload_2
    //   323: istore_3
    //   324: aload #9
    //   326: getfield mDimensionRatio : F
    //   329: fconst_0
    //   330: fcmpl
    //   331: ifle -> 336
    //   334: iconst_1
    //   335: istore_3
    //   336: iload_3
    //   337: ifeq -> 343
    //   340: goto -> 380
    //   343: aload_0
    //   344: aload #7
    //   346: aload #9
    //   348: getstatic androidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measure.SELF_DIMENSIONS : I
    //   351: invokespecial measure : (Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;Landroidx/constraintlayout/core/widgets/ConstraintWidget;I)Z
    //   354: pop
    //   355: aload_1
    //   356: getfield mMetrics : Landroidx/constraintlayout/core/Metrics;
    //   359: ifnull -> 380
    //   362: aload_1
    //   363: getfield mMetrics : Landroidx/constraintlayout/core/Metrics;
    //   366: astore #8
    //   368: aload #8
    //   370: aload #8
    //   372: getfield measuredWidgets : J
    //   375: lconst_1
    //   376: ladd
    //   377: putfield measuredWidgets : J
    //   380: iinc #4, 1
    //   383: goto -> 26
    //   386: aload #7
    //   388: invokeinterface didMeasures : ()V
    //   393: return
  }
  
  private void solveLinearSystem(ConstraintWidgetContainer paramConstraintWidgetContainer, String paramString, int paramInt1, int paramInt2, int paramInt3) {
    int i = paramConstraintWidgetContainer.getMinWidth();
    int j = paramConstraintWidgetContainer.getMinHeight();
    paramConstraintWidgetContainer.setMinWidth(0);
    paramConstraintWidgetContainer.setMinHeight(0);
    paramConstraintWidgetContainer.setWidth(paramInt2);
    paramConstraintWidgetContainer.setHeight(paramInt3);
    paramConstraintWidgetContainer.setMinWidth(i);
    paramConstraintWidgetContainer.setMinHeight(j);
    this.constraintWidgetContainer.setPass(paramInt1);
    this.constraintWidgetContainer.layout();
  }
  
  public long solverMeasure(ConstraintWidgetContainer paramConstraintWidgetContainer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9) {
    boolean bool;
    Measurer measurer = paramConstraintWidgetContainer.getMeasurer();
    null = 0L;
    paramInt9 = paramConstraintWidgetContainer.mChildren.size();
    int j = paramConstraintWidgetContainer.getWidth();
    int i = paramConstraintWidgetContainer.getHeight();
    boolean bool1 = Optimizer.enabled(paramInt1, 128);
    if (bool1 || Optimizer.enabled(paramInt1, 64)) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    } 
    if (paramInt1 != 0)
      for (paramInt2 = 0; paramInt2 < paramInt9; paramInt2++) {
        ConstraintWidget constraintWidget = paramConstraintWidgetContainer.mChildren.get(paramInt2);
        if (constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
          paramInt3 = 1;
        } else {
          paramInt3 = 0;
        } 
        if (constraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
          paramInt8 = 1;
        } else {
          paramInt8 = 0;
        } 
        if (paramInt3 != 0 && paramInt8 != 0 && constraintWidget.getDimensionRatio() > 0.0F) {
          paramInt3 = 1;
        } else {
          paramInt3 = 0;
        } 
        if (constraintWidget.isInHorizontalChain() && paramInt3 != 0) {
          paramInt1 = 0;
          break;
        } 
        if (constraintWidget.isInVerticalChain() && paramInt3 != 0) {
          paramInt1 = 0;
          break;
        } 
        if (constraintWidget instanceof VirtualLayout) {
          paramInt1 = 0;
          break;
        } 
        if (constraintWidget.isInHorizontalChain() || constraintWidget.isInVerticalChain()) {
          paramInt1 = 0;
          break;
        } 
      }  
    if (paramInt1 != 0 && LinearSystem.sMetrics != null) {
      Metrics metrics = LinearSystem.sMetrics;
      metrics.measures++;
    } 
    if ((paramInt4 == 1073741824 && paramInt6 == 1073741824) || bool1) {
      paramInt2 = 1;
    } else {
      paramInt2 = 0;
    } 
    int k = paramInt1 & paramInt2;
    paramInt1 = 0;
    if (k != 0) {
      paramInt2 = Math.min(paramConstraintWidgetContainer.getMaxWidth(), paramInt5);
      paramInt3 = Math.min(paramConstraintWidgetContainer.getMaxHeight(), paramInt7);
      if (paramInt4 == 1073741824 && paramConstraintWidgetContainer.getWidth() != paramInt2) {
        paramConstraintWidgetContainer.setWidth(paramInt2);
        paramConstraintWidgetContainer.invalidateGraph();
      } 
      if (paramInt6 == 1073741824 && paramConstraintWidgetContainer.getHeight() != paramInt3) {
        paramConstraintWidgetContainer.setHeight(paramInt3);
        paramConstraintWidgetContainer.invalidateGraph();
      } 
      if (paramInt4 == 1073741824 && paramInt6 == 1073741824) {
        bool = paramConstraintWidgetContainer.directMeasure(bool1);
        paramInt1 = 2;
      } else {
        bool = paramConstraintWidgetContainer.directMeasureSetup(bool1);
        if (paramInt4 == 1073741824) {
          bool &= paramConstraintWidgetContainer.directMeasureWithOrientation(bool1, 0);
          paramInt1 = 0 + 1;
        } 
        if (paramInt6 == 1073741824) {
          bool &= paramConstraintWidgetContainer.directMeasureWithOrientation(bool1, 1);
          paramInt1++;
        } 
      } 
      bool1 = true;
      if (bool) {
        boolean bool2;
        if (paramInt4 != 1073741824)
          bool1 = false; 
        if (paramInt6 == 1073741824) {
          bool2 = true;
        } else {
          bool2 = false;
        } 
        paramConstraintWidgetContainer.updateFromRuns(bool1, bool2);
      } 
    } else {
      bool = false;
      paramInt1 = 0;
    } 
    if (!bool || paramInt1 != 2) {
      int m = paramConstraintWidgetContainer.getOptimizationLevel();
      if (paramInt9 > 0)
        measureChildren(paramConstraintWidgetContainer); 
      updateHierarchy(paramConstraintWidgetContainer);
      paramInt4 = this.mVariableDimensionsWidgets.size();
      if (paramInt9 > 0)
        solveLinearSystem(paramConstraintWidgetContainer, "First pass", 0, j, i); 
      if (paramInt4 > 0) {
        boolean bool2;
        paramInt3 = 0;
        if (paramConstraintWidgetContainer.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
          paramInt6 = 1;
        } else {
          paramInt6 = 0;
        } 
        if (paramConstraintWidgetContainer.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
          paramInt7 = 1;
        } else {
          paramInt7 = 0;
        } 
        paramInt2 = Math.max(paramConstraintWidgetContainer.getWidth(), this.constraintWidgetContainer.getMinWidth());
        paramInt1 = Math.max(paramConstraintWidgetContainer.getHeight(), this.constraintWidgetContainer.getMinHeight());
        paramInt8 = 0;
        paramInt5 = paramInt9;
        while (paramInt8 < paramInt4) {
          ConstraintWidget constraintWidget = this.mVariableDimensionsWidgets.get(paramInt8);
          if (constraintWidget instanceof VirtualLayout) {
            int i1 = constraintWidget.getWidth();
            paramInt9 = constraintWidget.getHeight();
            bool2 = paramInt3 | measure(measurer, constraintWidget, Measure.TRY_GIVEN_DIMENSIONS);
            if (paramConstraintWidgetContainer.mMetrics != null) {
              Metrics metrics = paramConstraintWidgetContainer.mMetrics;
              metrics.measuredMatchWidgets++;
            } 
            int i2 = constraintWidget.getWidth();
            int n = constraintWidget.getHeight();
            if (i2 != i1) {
              constraintWidget.setWidth(i2);
              if (paramInt6 != 0 && constraintWidget.getRight() > paramInt2)
                paramInt2 = Math.max(paramInt2, constraintWidget.getRight() + constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin()); 
              bool2 = true;
            } 
            if (n != paramInt9) {
              constraintWidget.setHeight(n);
              if (paramInt7 != 0 && constraintWidget.getBottom() > paramInt1)
                paramInt1 = Math.max(paramInt1, constraintWidget.getBottom() + constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin()); 
              bool2 = true;
            } 
            bool2 |= ((VirtualLayout)constraintWidget).needSolverPass();
          } 
          paramInt8++;
        } 
        paramInt8 = 0;
        while (paramInt8 < 2) {
          paramInt9 = 0;
          boolean bool3 = bool2;
          int n = paramInt4;
          while (paramInt9 < n) {
            boolean bool4;
            int i1;
            int i2;
            ConstraintWidget constraintWidget = this.mVariableDimensionsWidgets.get(paramInt9);
            if ((constraintWidget instanceof androidx.constraintlayout.core.widgets.Helper && !(constraintWidget instanceof VirtualLayout)) || constraintWidget instanceof androidx.constraintlayout.core.widgets.Guideline || constraintWidget.getVisibility() == 8 || (k != 0 && constraintWidget.horizontalRun.dimension.resolved && constraintWidget.verticalRun.dimension.resolved) || constraintWidget instanceof VirtualLayout) {
              bool4 = bool3;
              i2 = paramInt2;
              i1 = paramInt1;
            } else {
              i2 = constraintWidget.getWidth();
              i1 = constraintWidget.getHeight();
              int i3 = constraintWidget.getBaselineDistance();
              paramInt4 = Measure.TRY_GIVEN_DIMENSIONS;
              if (paramInt8 == 2 - 1)
                paramInt4 = Measure.USE_GIVEN_DIMENSIONS; 
              bool3 |= measure(measurer, constraintWidget, paramInt4);
              if (paramConstraintWidgetContainer.mMetrics != null) {
                Metrics metrics = paramConstraintWidgetContainer.mMetrics;
                metrics.measuredMatchWidgets++;
              } 
              int i4 = constraintWidget.getWidth();
              paramInt4 = constraintWidget.getHeight();
              if (i4 != i2) {
                constraintWidget.setWidth(i4);
                if (paramInt6 != 0 && constraintWidget.getRight() > paramInt2)
                  paramInt2 = Math.max(paramInt2, constraintWidget.getRight() + constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin()); 
                bool3 = true;
              } 
              if (paramInt4 != i1) {
                constraintWidget.setHeight(paramInt4);
                if (paramInt7 != 0 && constraintWidget.getBottom() > paramInt1)
                  paramInt1 = Math.max(paramInt1, constraintWidget.getBottom() + constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin()); 
                bool3 = true;
              } 
              bool4 = bool3;
              i2 = paramInt2;
              i1 = paramInt1;
              if (constraintWidget.hasBaseline()) {
                bool4 = bool3;
                i2 = paramInt2;
                i1 = paramInt1;
                if (i3 != constraintWidget.getBaselineDistance()) {
                  bool4 = true;
                  i1 = paramInt1;
                  i2 = paramInt2;
                } 
              } 
            } 
            paramInt9++;
            bool3 = bool4;
            paramInt2 = i2;
            paramInt1 = i1;
          } 
          if (bool3) {
            solveLinearSystem(paramConstraintWidgetContainer, "intermediate pass", paramInt8 + 1, j, i);
            bool3 = false;
            paramInt8++;
            paramInt4 = n;
            boolean bool4 = bool3;
          } 
        } 
      } else {
        null = 0L;
      } 
      paramConstraintWidgetContainer.setOptimizationLevel(m);
      return null;
    } 
    return 0L;
  }
  
  public void updateHierarchy(ConstraintWidgetContainer paramConstraintWidgetContainer) {
    this.mVariableDimensionsWidgets.clear();
    int i = paramConstraintWidgetContainer.mChildren.size();
    for (byte b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = paramConstraintWidgetContainer.mChildren.get(b);
      if (constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || constraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT)
        this.mVariableDimensionsWidgets.add(constraintWidget); 
    } 
    paramConstraintWidgetContainer.invalidateGraph();
  }
  
  public static class Measure {
    public static int SELF_DIMENSIONS = 0;
    
    public static int TRY_GIVEN_DIMENSIONS = 1;
    
    public static int USE_GIVEN_DIMENSIONS = 2;
    
    public ConstraintWidget.DimensionBehaviour horizontalBehavior;
    
    public int horizontalDimension;
    
    public int measureStrategy;
    
    public int measuredBaseline;
    
    public boolean measuredHasBaseline;
    
    public int measuredHeight;
    
    public boolean measuredNeedsSolverPass;
    
    public int measuredWidth;
    
    public ConstraintWidget.DimensionBehaviour verticalBehavior;
    
    public int verticalDimension;
  }
  
  public static interface Measurer {
    void didMeasures();
    
    void measure(ConstraintWidget param1ConstraintWidget, BasicMeasure.Measure param1Measure);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\widgets\analyzer\BasicMeasure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */