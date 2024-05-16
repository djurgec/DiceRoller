package androidx.constraintlayout.core.widgets.analyzer;

import androidx.constraintlayout.core.LinearSystem;
import androidx.constraintlayout.core.widgets.Barrier;
import androidx.constraintlayout.core.widgets.ChainHead;
import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.core.widgets.Guideline;
import java.util.ArrayList;

public class Direct {
  private static final boolean APPLY_MATCH_PARENT = false;
  
  private static final boolean DEBUG = false;
  
  private static final boolean EARLY_TERMINATION = true;
  
  private static int hcount;
  
  private static BasicMeasure.Measure measure = new BasicMeasure.Measure();
  
  private static int vcount;
  
  static {
    hcount = 0;
    vcount = 0;
  }
  
  private static boolean canMeasure(int paramInt, ConstraintWidget paramConstraintWidget) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getHorizontalDimensionBehaviour : ()Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   4: astore #7
    //   6: aload_1
    //   7: invokevirtual getVerticalDimensionBehaviour : ()Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   10: astore #6
    //   12: aload_1
    //   13: invokevirtual getParent : ()Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   16: ifnull -> 31
    //   19: aload_1
    //   20: invokevirtual getParent : ()Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   23: checkcast androidx/constraintlayout/core/widgets/ConstraintWidgetContainer
    //   26: astore #5
    //   28: goto -> 34
    //   31: aconst_null
    //   32: astore #5
    //   34: iconst_0
    //   35: istore #4
    //   37: aload #5
    //   39: ifnull -> 56
    //   42: aload #5
    //   44: invokevirtual getHorizontalDimensionBehaviour : ()Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   47: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   50: if_acmpne -> 56
    //   53: goto -> 56
    //   56: aload #5
    //   58: ifnull -> 75
    //   61: aload #5
    //   63: invokevirtual getVerticalDimensionBehaviour : ()Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   66: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   69: if_acmpne -> 75
    //   72: goto -> 75
    //   75: aload #7
    //   77: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   80: if_acmpeq -> 166
    //   83: aload_1
    //   84: invokevirtual isResolvedHorizontally : ()Z
    //   87: ifne -> 166
    //   90: aload #7
    //   92: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   95: if_acmpeq -> 166
    //   98: aload #7
    //   100: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   103: if_acmpne -> 130
    //   106: aload_1
    //   107: getfield mMatchConstraintDefaultWidth : I
    //   110: ifne -> 130
    //   113: aload_1
    //   114: getfield mDimensionRatio : F
    //   117: fconst_0
    //   118: fcmpl
    //   119: ifne -> 130
    //   122: aload_1
    //   123: iconst_0
    //   124: invokevirtual hasDanglingDimension : (I)Z
    //   127: ifne -> 166
    //   130: aload #7
    //   132: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   135: if_acmpne -> 161
    //   138: aload_1
    //   139: getfield mMatchConstraintDefaultWidth : I
    //   142: iconst_1
    //   143: if_icmpne -> 161
    //   146: aload_1
    //   147: iconst_0
    //   148: aload_1
    //   149: invokevirtual getWidth : ()I
    //   152: invokevirtual hasResolvedTargets : (II)Z
    //   155: ifeq -> 161
    //   158: goto -> 166
    //   161: iconst_0
    //   162: istore_0
    //   163: goto -> 168
    //   166: iconst_1
    //   167: istore_0
    //   168: aload #6
    //   170: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   173: if_acmpeq -> 259
    //   176: aload_1
    //   177: invokevirtual isResolvedVertically : ()Z
    //   180: ifne -> 259
    //   183: aload #6
    //   185: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   188: if_acmpeq -> 259
    //   191: aload #6
    //   193: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   196: if_acmpne -> 223
    //   199: aload_1
    //   200: getfield mMatchConstraintDefaultHeight : I
    //   203: ifne -> 223
    //   206: aload_1
    //   207: getfield mDimensionRatio : F
    //   210: fconst_0
    //   211: fcmpl
    //   212: ifne -> 223
    //   215: aload_1
    //   216: iconst_1
    //   217: invokevirtual hasDanglingDimension : (I)Z
    //   220: ifne -> 259
    //   223: aload #7
    //   225: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   228: if_acmpne -> 254
    //   231: aload_1
    //   232: getfield mMatchConstraintDefaultHeight : I
    //   235: iconst_1
    //   236: if_icmpne -> 254
    //   239: aload_1
    //   240: iconst_1
    //   241: aload_1
    //   242: invokevirtual getHeight : ()I
    //   245: invokevirtual hasResolvedTargets : (II)Z
    //   248: ifeq -> 254
    //   251: goto -> 259
    //   254: iconst_0
    //   255: istore_2
    //   256: goto -> 261
    //   259: iconst_1
    //   260: istore_2
    //   261: aload_1
    //   262: getfield mDimensionRatio : F
    //   265: fconst_0
    //   266: fcmpl
    //   267: ifle -> 280
    //   270: iload_0
    //   271: ifne -> 278
    //   274: iload_2
    //   275: ifeq -> 280
    //   278: iconst_1
    //   279: ireturn
    //   280: iload #4
    //   282: istore_3
    //   283: iload_0
    //   284: ifeq -> 296
    //   287: iload #4
    //   289: istore_3
    //   290: iload_2
    //   291: ifeq -> 296
    //   294: iconst_1
    //   295: istore_3
    //   296: iload_3
    //   297: ireturn
  }
  
  private static void horizontalSolvingPass(int paramInt, ConstraintWidget paramConstraintWidget, BasicMeasure.Measurer paramMeasurer, boolean paramBoolean) {
    if (paramConstraintWidget.isHorizontalSolvingPassDone())
      return; 
    hcount++;
    if (!(paramConstraintWidget instanceof ConstraintWidgetContainer) && paramConstraintWidget.isMeasureRequested() && canMeasure(paramInt + 1, paramConstraintWidget))
      ConstraintWidgetContainer.measure(paramInt + 1, paramConstraintWidget, paramMeasurer, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS); 
    constraintAnchor2 = paramConstraintWidget.getAnchor(ConstraintAnchor.Type.LEFT);
    ConstraintAnchor constraintAnchor1 = paramConstraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT);
    int j = constraintAnchor2.getFinalValue();
    int i = constraintAnchor1.getFinalValue();
    if (constraintAnchor2.getDependents() != null && constraintAnchor2.hasFinalValue())
      for (ConstraintAnchor constraintAnchor2 : constraintAnchor2.getDependents()) {
        ConstraintWidget constraintWidget = constraintAnchor2.mOwner;
        boolean bool = canMeasure(paramInt + 1, constraintWidget);
        if (constraintWidget.isMeasureRequested() && bool)
          ConstraintWidgetContainer.measure(paramInt + 1, constraintWidget, paramMeasurer, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS); 
        if (constraintWidget.getHorizontalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || bool) {
          if (constraintWidget.isMeasureRequested())
            continue; 
          if (constraintAnchor2 == constraintWidget.mLeft && constraintWidget.mRight.mTarget == null) {
            int k = constraintWidget.mLeft.getMargin() + j;
            constraintWidget.setFinalHorizontal(k, constraintWidget.getWidth() + k);
            horizontalSolvingPass(paramInt + 1, constraintWidget, paramMeasurer, paramBoolean);
            continue;
          } 
          if (constraintAnchor2 == constraintWidget.mRight && constraintWidget.mLeft.mTarget == null) {
            int k = j - constraintWidget.mRight.getMargin();
            constraintWidget.setFinalHorizontal(k - constraintWidget.getWidth(), k);
            horizontalSolvingPass(paramInt + 1, constraintWidget, paramMeasurer, paramBoolean);
            continue;
          } 
          if (constraintAnchor2 == constraintWidget.mLeft && constraintWidget.mRight.mTarget != null && constraintWidget.mRight.mTarget.hasFinalValue() && !constraintWidget.isInHorizontalChain())
            solveHorizontalCenterConstraints(paramInt + 1, paramMeasurer, constraintWidget, paramBoolean); 
          continue;
        } 
        if (constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mMatchConstraintMaxWidth >= 0 && constraintWidget.mMatchConstraintMinWidth >= 0 && (constraintWidget.getVisibility() == 8 || (constraintWidget.mMatchConstraintDefaultWidth == 0 && constraintWidget.getDimensionRatio() == 0.0F)) && !constraintWidget.isInHorizontalChain() && !constraintWidget.isInVirtualLayout()) {
          boolean bool1;
          if ((constraintAnchor2 == constraintWidget.mLeft && constraintWidget.mRight.mTarget != null && constraintWidget.mRight.mTarget.hasFinalValue()) || (constraintAnchor2 == constraintWidget.mRight && constraintWidget.mLeft.mTarget != null && constraintWidget.mLeft.mTarget.hasFinalValue())) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
          if (bool1 && !constraintWidget.isInHorizontalChain())
            solveHorizontalMatchConstraint(paramInt + 1, paramConstraintWidget, paramMeasurer, constraintWidget, paramBoolean); 
        } 
      }  
    if (paramConstraintWidget instanceof Guideline)
      return; 
    if (constraintAnchor1.getDependents() != null && constraintAnchor1.hasFinalValue())
      for (ConstraintAnchor constraintAnchor : constraintAnchor1.getDependents()) {
        int k;
        ConstraintWidget constraintWidget = constraintAnchor.mOwner;
        boolean bool = canMeasure(paramInt + 1, constraintWidget);
        if (constraintWidget.isMeasureRequested() && bool)
          ConstraintWidgetContainer.measure(paramInt + 1, constraintWidget, paramMeasurer, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS); 
        if ((constraintAnchor == constraintWidget.mLeft && constraintWidget.mRight.mTarget != null && constraintWidget.mRight.mTarget.hasFinalValue()) || (constraintAnchor == constraintWidget.mRight && constraintWidget.mLeft.mTarget != null && constraintWidget.mLeft.mTarget.hasFinalValue())) {
          k = 1;
        } else {
          k = 0;
        } 
        if (constraintWidget.getHorizontalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || bool) {
          if (constraintWidget.isMeasureRequested())
            continue; 
          if (constraintAnchor == constraintWidget.mLeft && constraintWidget.mRight.mTarget == null) {
            k = constraintWidget.mLeft.getMargin() + i;
            constraintWidget.setFinalHorizontal(k, constraintWidget.getWidth() + k);
            horizontalSolvingPass(paramInt + 1, constraintWidget, paramMeasurer, paramBoolean);
            continue;
          } 
          if (constraintAnchor == constraintWidget.mRight && constraintWidget.mLeft.mTarget == null) {
            k = i - constraintWidget.mRight.getMargin();
            constraintWidget.setFinalHorizontal(k - constraintWidget.getWidth(), k);
            horizontalSolvingPass(paramInt + 1, constraintWidget, paramMeasurer, paramBoolean);
            continue;
          } 
          if (k != 0 && !constraintWidget.isInHorizontalChain())
            solveHorizontalCenterConstraints(paramInt + 1, paramMeasurer, constraintWidget, paramBoolean); 
          continue;
        } 
        if (constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mMatchConstraintMaxWidth >= 0 && constraintWidget.mMatchConstraintMinWidth >= 0 && (constraintWidget.getVisibility() == 8 || (constraintWidget.mMatchConstraintDefaultWidth == 0 && constraintWidget.getDimensionRatio() == 0.0F)) && !constraintWidget.isInHorizontalChain() && !constraintWidget.isInVirtualLayout() && k != 0 && !constraintWidget.isInHorizontalChain())
          solveHorizontalMatchConstraint(paramInt + 1, paramConstraintWidget, paramMeasurer, constraintWidget, paramBoolean); 
      }  
    paramConstraintWidget.markHorizontalSolvingPassDone();
  }
  
  public static String ls(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b = 0; b < paramInt; b++)
      stringBuilder.append("  "); 
    stringBuilder.append("+-(" + paramInt + ") ");
    return stringBuilder.toString();
  }
  
  private static void solveBarrier(int paramInt1, Barrier paramBarrier, BasicMeasure.Measurer paramMeasurer, int paramInt2, boolean paramBoolean) {
    if (paramBarrier.allSolved())
      if (paramInt2 == 0) {
        horizontalSolvingPass(paramInt1 + 1, (ConstraintWidget)paramBarrier, paramMeasurer, paramBoolean);
      } else {
        verticalSolvingPass(paramInt1 + 1, (ConstraintWidget)paramBarrier, paramMeasurer);
      }  
  }
  
  public static boolean solveChain(ConstraintWidgetContainer paramConstraintWidgetContainer, LinearSystem paramLinearSystem, int paramInt1, int paramInt2, ChainHead paramChainHead, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
    // Byte code:
    //   0: iload #7
    //   2: ifeq -> 7
    //   5: iconst_0
    //   6: ireturn
    //   7: iload_2
    //   8: ifne -> 20
    //   11: aload_0
    //   12: invokevirtual isResolvedHorizontally : ()Z
    //   15: ifne -> 29
    //   18: iconst_0
    //   19: ireturn
    //   20: aload_0
    //   21: invokevirtual isResolvedVertically : ()Z
    //   24: ifne -> 29
    //   27: iconst_0
    //   28: ireturn
    //   29: aload_0
    //   30: invokevirtual isRtl : ()Z
    //   33: istore #7
    //   35: aload #4
    //   37: invokevirtual getFirst : ()Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   40: astore #16
    //   42: aload #4
    //   44: invokevirtual getLast : ()Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   47: astore #19
    //   49: aload #4
    //   51: invokevirtual getFirstVisibleWidget : ()Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   54: astore #22
    //   56: aload #4
    //   58: invokevirtual getLastVisibleWidget : ()Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   61: astore #23
    //   63: aload #4
    //   65: invokevirtual getHead : ()Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   68: astore #24
    //   70: aload #16
    //   72: astore #20
    //   74: iconst_0
    //   75: istore #9
    //   77: aload #16
    //   79: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   82: iload_3
    //   83: aaload
    //   84: astore #17
    //   86: aload #19
    //   88: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   91: iload_3
    //   92: iconst_1
    //   93: iadd
    //   94: aaload
    //   95: astore #4
    //   97: aload #17
    //   99: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   102: ifnull -> 1069
    //   105: aload #4
    //   107: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   110: ifnonnull -> 116
    //   113: goto -> 1069
    //   116: aload #17
    //   118: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   121: invokevirtual hasFinalValue : ()Z
    //   124: ifeq -> 1067
    //   127: aload #4
    //   129: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   132: invokevirtual hasFinalValue : ()Z
    //   135: ifne -> 141
    //   138: goto -> 1067
    //   141: aload #22
    //   143: ifnull -> 1065
    //   146: aload #23
    //   148: ifnonnull -> 154
    //   151: goto -> 1065
    //   154: aload #17
    //   156: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   159: invokevirtual getFinalValue : ()I
    //   162: aload #22
    //   164: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   167: iload_3
    //   168: aaload
    //   169: invokevirtual getMargin : ()I
    //   172: iadd
    //   173: istore #13
    //   175: aload #4
    //   177: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   180: invokevirtual getFinalValue : ()I
    //   183: aload #23
    //   185: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   188: iload_3
    //   189: iconst_1
    //   190: iadd
    //   191: aaload
    //   192: invokevirtual getMargin : ()I
    //   195: isub
    //   196: istore #14
    //   198: iload #14
    //   200: iload #13
    //   202: isub
    //   203: istore #15
    //   205: iload #15
    //   207: ifgt -> 212
    //   210: iconst_0
    //   211: ireturn
    //   212: iconst_0
    //   213: istore #12
    //   215: new androidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measure
    //   218: dup
    //   219: invokespecial <init> : ()V
    //   222: astore #18
    //   224: iconst_0
    //   225: istore #11
    //   227: iconst_0
    //   228: istore #10
    //   230: iload #9
    //   232: ifne -> 458
    //   235: iconst_0
    //   236: iconst_1
    //   237: iadd
    //   238: aload #20
    //   240: invokestatic canMeasure : (ILandroidx/constraintlayout/core/widgets/ConstraintWidget;)Z
    //   243: ifne -> 248
    //   246: iconst_0
    //   247: ireturn
    //   248: aload #20
    //   250: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   253: iload_2
    //   254: aaload
    //   255: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   258: if_acmpne -> 263
    //   261: iconst_0
    //   262: ireturn
    //   263: aload #20
    //   265: invokevirtual isMeasureRequested : ()Z
    //   268: ifeq -> 292
    //   271: iconst_0
    //   272: iconst_1
    //   273: iadd
    //   274: aload #20
    //   276: aload_0
    //   277: invokevirtual getMeasurer : ()Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;
    //   280: aload #18
    //   282: getstatic androidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measure.SELF_DIMENSIONS : I
    //   285: invokestatic measure : (ILandroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measure;I)Z
    //   288: pop
    //   289: goto -> 292
    //   292: iload #12
    //   294: aload #20
    //   296: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   299: iload_3
    //   300: aaload
    //   301: invokevirtual getMargin : ()I
    //   304: iadd
    //   305: istore #12
    //   307: iload_2
    //   308: ifne -> 324
    //   311: iload #12
    //   313: aload #20
    //   315: invokevirtual getWidth : ()I
    //   318: iadd
    //   319: istore #12
    //   321: goto -> 334
    //   324: iload #12
    //   326: aload #20
    //   328: invokevirtual getHeight : ()I
    //   331: iadd
    //   332: istore #12
    //   334: iload #12
    //   336: aload #20
    //   338: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   341: iload_3
    //   342: iconst_1
    //   343: iadd
    //   344: aaload
    //   345: invokevirtual getMargin : ()I
    //   348: iadd
    //   349: istore #12
    //   351: iinc #11, 1
    //   354: aload #20
    //   356: invokevirtual getVisibility : ()I
    //   359: bipush #8
    //   361: if_icmpeq -> 370
    //   364: iinc #10, 1
    //   367: goto -> 370
    //   370: aload #20
    //   372: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   375: iload_3
    //   376: iconst_1
    //   377: iadd
    //   378: aaload
    //   379: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   382: astore #4
    //   384: aload #4
    //   386: ifnull -> 437
    //   389: aload #4
    //   391: getfield mOwner : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   394: astore #21
    //   396: aload #21
    //   398: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   401: iload_3
    //   402: aaload
    //   403: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   406: ifnull -> 431
    //   409: aload #21
    //   411: astore #4
    //   413: aload #21
    //   415: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   418: iload_3
    //   419: aaload
    //   420: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   423: getfield mOwner : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   426: aload #20
    //   428: if_acmpeq -> 440
    //   431: aconst_null
    //   432: astore #4
    //   434: goto -> 440
    //   437: aconst_null
    //   438: astore #4
    //   440: aload #4
    //   442: ifnull -> 452
    //   445: aload #4
    //   447: astore #20
    //   449: goto -> 455
    //   452: iconst_1
    //   453: istore #9
    //   455: goto -> 230
    //   458: iload #10
    //   460: ifne -> 465
    //   463: iconst_0
    //   464: ireturn
    //   465: iload #10
    //   467: iload #11
    //   469: if_icmpeq -> 474
    //   472: iconst_0
    //   473: ireturn
    //   474: iload #15
    //   476: iload #12
    //   478: if_icmpge -> 483
    //   481: iconst_0
    //   482: ireturn
    //   483: iload #15
    //   485: iload #12
    //   487: isub
    //   488: istore #9
    //   490: iload #5
    //   492: ifeq -> 507
    //   495: iload #9
    //   497: iload #10
    //   499: iconst_1
    //   500: iadd
    //   501: idiv
    //   502: istore #9
    //   504: goto -> 533
    //   507: iload #6
    //   509: ifeq -> 533
    //   512: iload #10
    //   514: iconst_2
    //   515: if_icmple -> 530
    //   518: iload #9
    //   520: iload #10
    //   522: idiv
    //   523: iconst_1
    //   524: isub
    //   525: istore #9
    //   527: goto -> 533
    //   530: goto -> 533
    //   533: iload #10
    //   535: iconst_1
    //   536: if_icmpne -> 625
    //   539: iload_2
    //   540: ifne -> 553
    //   543: aload #24
    //   545: invokevirtual getHorizontalBiasPercent : ()F
    //   548: fstore #8
    //   550: goto -> 560
    //   553: aload #24
    //   555: invokevirtual getVerticalBiasPercent : ()F
    //   558: fstore #8
    //   560: iload #13
    //   562: i2f
    //   563: ldc_w 0.5
    //   566: fadd
    //   567: iload #9
    //   569: i2f
    //   570: fload #8
    //   572: fmul
    //   573: fadd
    //   574: f2i
    //   575: istore_3
    //   576: iload_2
    //   577: ifne -> 596
    //   580: aload #22
    //   582: iload_3
    //   583: aload #22
    //   585: invokevirtual getWidth : ()I
    //   588: iload_3
    //   589: iadd
    //   590: invokevirtual setFinalHorizontal : (II)V
    //   593: goto -> 609
    //   596: aload #22
    //   598: iload_3
    //   599: aload #22
    //   601: invokevirtual getHeight : ()I
    //   604: iload_3
    //   605: iadd
    //   606: invokevirtual setFinalVertical : (II)V
    //   609: iconst_0
    //   610: iconst_1
    //   611: iadd
    //   612: aload #22
    //   614: aload_0
    //   615: invokevirtual getMeasurer : ()Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;
    //   618: iload #7
    //   620: invokestatic horizontalSolvingPass : (ILandroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;Z)V
    //   623: iconst_1
    //   624: ireturn
    //   625: iload #5
    //   627: ifeq -> 929
    //   630: iconst_0
    //   631: istore #10
    //   633: iload #13
    //   635: iload #9
    //   637: iadd
    //   638: istore #11
    //   640: aload #16
    //   642: astore #17
    //   644: iload #10
    //   646: ifne -> 926
    //   649: aload #17
    //   651: invokevirtual getVisibility : ()I
    //   654: bipush #8
    //   656: if_icmpne -> 713
    //   659: iload_2
    //   660: ifne -> 689
    //   663: aload #17
    //   665: iload #11
    //   667: iload #11
    //   669: invokevirtual setFinalHorizontal : (II)V
    //   672: iconst_0
    //   673: iconst_1
    //   674: iadd
    //   675: aload #17
    //   677: aload_0
    //   678: invokevirtual getMeasurer : ()Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;
    //   681: iload #7
    //   683: invokestatic horizontalSolvingPass : (ILandroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;Z)V
    //   686: goto -> 831
    //   689: aload #17
    //   691: iload #11
    //   693: iload #11
    //   695: invokevirtual setFinalVertical : (II)V
    //   698: iconst_0
    //   699: iconst_1
    //   700: iadd
    //   701: aload #17
    //   703: aload_0
    //   704: invokevirtual getMeasurer : ()Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;
    //   707: invokestatic verticalSolvingPass : (ILandroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;)V
    //   710: goto -> 831
    //   713: iload #11
    //   715: aload #17
    //   717: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   720: iload_3
    //   721: aaload
    //   722: invokevirtual getMargin : ()I
    //   725: iadd
    //   726: istore #11
    //   728: iload_2
    //   729: ifne -> 774
    //   732: aload #17
    //   734: iload #11
    //   736: aload #17
    //   738: invokevirtual getWidth : ()I
    //   741: iload #11
    //   743: iadd
    //   744: invokevirtual setFinalHorizontal : (II)V
    //   747: iconst_0
    //   748: iconst_1
    //   749: iadd
    //   750: aload #17
    //   752: aload_0
    //   753: invokevirtual getMeasurer : ()Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;
    //   756: iload #7
    //   758: invokestatic horizontalSolvingPass : (ILandroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;Z)V
    //   761: iload #11
    //   763: aload #17
    //   765: invokevirtual getWidth : ()I
    //   768: iadd
    //   769: istore #11
    //   771: goto -> 811
    //   774: aload #17
    //   776: iload #11
    //   778: aload #17
    //   780: invokevirtual getHeight : ()I
    //   783: iload #11
    //   785: iadd
    //   786: invokevirtual setFinalVertical : (II)V
    //   789: iconst_0
    //   790: iconst_1
    //   791: iadd
    //   792: aload #17
    //   794: aload_0
    //   795: invokevirtual getMeasurer : ()Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;
    //   798: invokestatic verticalSolvingPass : (ILandroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;)V
    //   801: iload #11
    //   803: aload #17
    //   805: invokevirtual getHeight : ()I
    //   808: iadd
    //   809: istore #11
    //   811: iload #11
    //   813: aload #17
    //   815: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   818: iload_3
    //   819: iconst_1
    //   820: iadd
    //   821: aaload
    //   822: invokevirtual getMargin : ()I
    //   825: iadd
    //   826: iload #9
    //   828: iadd
    //   829: istore #11
    //   831: aload #17
    //   833: aload_1
    //   834: iconst_0
    //   835: invokevirtual addToSolver : (Landroidx/constraintlayout/core/LinearSystem;Z)V
    //   838: aload #17
    //   840: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   843: iload_3
    //   844: iconst_1
    //   845: iadd
    //   846: aaload
    //   847: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   850: astore #4
    //   852: aload #4
    //   854: ifnull -> 905
    //   857: aload #4
    //   859: getfield mOwner : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   862: astore #18
    //   864: aload #18
    //   866: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   869: iload_3
    //   870: aaload
    //   871: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   874: ifnull -> 899
    //   877: aload #18
    //   879: astore #4
    //   881: aload #18
    //   883: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   886: iload_3
    //   887: aaload
    //   888: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   891: getfield mOwner : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   894: aload #17
    //   896: if_acmpeq -> 908
    //   899: aconst_null
    //   900: astore #4
    //   902: goto -> 908
    //   905: aconst_null
    //   906: astore #4
    //   908: aload #4
    //   910: ifnull -> 920
    //   913: aload #4
    //   915: astore #17
    //   917: goto -> 923
    //   920: iconst_1
    //   921: istore #10
    //   923: goto -> 644
    //   926: goto -> 1063
    //   929: iload #6
    //   931: ifeq -> 1063
    //   934: iload #10
    //   936: iconst_2
    //   937: if_icmpne -> 1061
    //   940: iload_2
    //   941: ifne -> 1005
    //   944: aload #22
    //   946: iload #13
    //   948: aload #22
    //   950: invokevirtual getWidth : ()I
    //   953: iload #13
    //   955: iadd
    //   956: invokevirtual setFinalHorizontal : (II)V
    //   959: aload #23
    //   961: iload #14
    //   963: aload #23
    //   965: invokevirtual getWidth : ()I
    //   968: isub
    //   969: iload #14
    //   971: invokevirtual setFinalHorizontal : (II)V
    //   974: iconst_0
    //   975: iconst_1
    //   976: iadd
    //   977: aload #22
    //   979: aload_0
    //   980: invokevirtual getMeasurer : ()Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;
    //   983: iload #7
    //   985: invokestatic horizontalSolvingPass : (ILandroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;Z)V
    //   988: iconst_0
    //   989: iconst_1
    //   990: iadd
    //   991: aload #23
    //   993: aload_0
    //   994: invokevirtual getMeasurer : ()Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;
    //   997: iload #7
    //   999: invokestatic horizontalSolvingPass : (ILandroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;Z)V
    //   1002: goto -> 1059
    //   1005: aload #22
    //   1007: iload #13
    //   1009: aload #22
    //   1011: invokevirtual getHeight : ()I
    //   1014: iload #13
    //   1016: iadd
    //   1017: invokevirtual setFinalVertical : (II)V
    //   1020: aload #23
    //   1022: iload #14
    //   1024: aload #23
    //   1026: invokevirtual getHeight : ()I
    //   1029: isub
    //   1030: iload #14
    //   1032: invokevirtual setFinalVertical : (II)V
    //   1035: iconst_0
    //   1036: iconst_1
    //   1037: iadd
    //   1038: aload #22
    //   1040: aload_0
    //   1041: invokevirtual getMeasurer : ()Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;
    //   1044: invokestatic verticalSolvingPass : (ILandroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;)V
    //   1047: iconst_0
    //   1048: iconst_1
    //   1049: iadd
    //   1050: aload #23
    //   1052: aload_0
    //   1053: invokevirtual getMeasurer : ()Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;
    //   1056: invokestatic verticalSolvingPass : (ILandroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measurer;)V
    //   1059: iconst_1
    //   1060: ireturn
    //   1061: iconst_0
    //   1062: ireturn
    //   1063: iconst_1
    //   1064: ireturn
    //   1065: iconst_0
    //   1066: ireturn
    //   1067: iconst_0
    //   1068: ireturn
    //   1069: iconst_0
    //   1070: ireturn
  }
  
  private static void solveHorizontalCenterConstraints(int paramInt, BasicMeasure.Measurer paramMeasurer, ConstraintWidget paramConstraintWidget, boolean paramBoolean) {
    float f = paramConstraintWidget.getHorizontalBiasPercent();
    int m = paramConstraintWidget.mLeft.mTarget.getFinalValue();
    int k = paramConstraintWidget.mRight.mTarget.getFinalValue();
    int j = paramConstraintWidget.mLeft.getMargin() + m;
    int i = k - paramConstraintWidget.mRight.getMargin();
    if (m == k) {
      f = 0.5F;
      j = m;
      i = k;
    } 
    int i1 = paramConstraintWidget.getWidth();
    k = i - j - i1;
    if (j > i)
      k = j - i - i1; 
    if (k > 0) {
      k = (int)(k * f + 0.5F);
    } else {
      k = (int)(k * f);
    } 
    int n = j + k;
    m = n + i1;
    if (j > i) {
      n = j + k;
      m = n - i1;
    } 
    paramConstraintWidget.setFinalHorizontal(n, m);
    horizontalSolvingPass(paramInt + 1, paramConstraintWidget, paramMeasurer, paramBoolean);
  }
  
  private static void solveHorizontalMatchConstraint(int paramInt, ConstraintWidget paramConstraintWidget1, BasicMeasure.Measurer paramMeasurer, ConstraintWidget paramConstraintWidget2, boolean paramBoolean) {
    float f = paramConstraintWidget2.getHorizontalBiasPercent();
    int j = paramConstraintWidget2.mLeft.mTarget.getFinalValue() + paramConstraintWidget2.mLeft.getMargin();
    int i = paramConstraintWidget2.mRight.mTarget.getFinalValue() - paramConstraintWidget2.mRight.getMargin();
    if (i >= j) {
      int m = paramConstraintWidget2.getWidth();
      int k = m;
      if (paramConstraintWidget2.getVisibility() != 8) {
        if (paramConstraintWidget2.mMatchConstraintDefaultWidth == 2) {
          if (paramConstraintWidget1 instanceof ConstraintWidgetContainer) {
            k = paramConstraintWidget1.getWidth();
          } else {
            k = paramConstraintWidget1.getParent().getWidth();
          } 
          k = (int)(paramConstraintWidget2.getHorizontalBiasPercent() * 0.5F * k);
        } else {
          k = m;
          if (paramConstraintWidget2.mMatchConstraintDefaultWidth == 0)
            k = i - j; 
        } 
        m = Math.max(paramConstraintWidget2.mMatchConstraintMinWidth, k);
        k = m;
        if (paramConstraintWidget2.mMatchConstraintMaxWidth > 0)
          k = Math.min(paramConstraintWidget2.mMatchConstraintMaxWidth, m); 
      } 
      m = j + (int)((i - j - k) * f + 0.5F);
      paramConstraintWidget2.setFinalHorizontal(m, m + k);
      horizontalSolvingPass(paramInt + 1, paramConstraintWidget2, paramMeasurer, paramBoolean);
    } 
  }
  
  private static void solveVerticalCenterConstraints(int paramInt, BasicMeasure.Measurer paramMeasurer, ConstraintWidget paramConstraintWidget) {
    float f = paramConstraintWidget.getVerticalBiasPercent();
    int m = paramConstraintWidget.mTop.mTarget.getFinalValue();
    int k = paramConstraintWidget.mBottom.mTarget.getFinalValue();
    int j = paramConstraintWidget.mTop.getMargin() + m;
    int i = k - paramConstraintWidget.mBottom.getMargin();
    if (m == k) {
      f = 0.5F;
      j = m;
      i = k;
    } 
    int i1 = paramConstraintWidget.getHeight();
    k = i - j - i1;
    if (j > i)
      k = j - i - i1; 
    if (k > 0) {
      k = (int)(k * f + 0.5F);
    } else {
      k = (int)(k * f);
    } 
    m = j + k;
    int n = m + i1;
    if (j > i) {
      m = j - k;
      n = m - i1;
    } 
    paramConstraintWidget.setFinalVertical(m, n);
    verticalSolvingPass(paramInt + 1, paramConstraintWidget, paramMeasurer);
  }
  
  private static void solveVerticalMatchConstraint(int paramInt, ConstraintWidget paramConstraintWidget1, BasicMeasure.Measurer paramMeasurer, ConstraintWidget paramConstraintWidget2) {
    float f = paramConstraintWidget2.getVerticalBiasPercent();
    int i = paramConstraintWidget2.mTop.mTarget.getFinalValue() + paramConstraintWidget2.mTop.getMargin();
    int j = paramConstraintWidget2.mBottom.mTarget.getFinalValue() - paramConstraintWidget2.mBottom.getMargin();
    if (j >= i) {
      int m = paramConstraintWidget2.getHeight();
      int k = m;
      if (paramConstraintWidget2.getVisibility() != 8) {
        if (paramConstraintWidget2.mMatchConstraintDefaultHeight == 2) {
          if (paramConstraintWidget1 instanceof ConstraintWidgetContainer) {
            k = paramConstraintWidget1.getHeight();
          } else {
            k = paramConstraintWidget1.getParent().getHeight();
          } 
          k = (int)(f * 0.5F * k);
        } else {
          k = m;
          if (paramConstraintWidget2.mMatchConstraintDefaultHeight == 0)
            k = j - i; 
        } 
        m = Math.max(paramConstraintWidget2.mMatchConstraintMinHeight, k);
        k = m;
        if (paramConstraintWidget2.mMatchConstraintMaxHeight > 0)
          k = Math.min(paramConstraintWidget2.mMatchConstraintMaxHeight, m); 
      } 
      m = i + (int)((j - i - k) * f + 0.5F);
      paramConstraintWidget2.setFinalVertical(m, m + k);
      verticalSolvingPass(paramInt + 1, paramConstraintWidget2, paramMeasurer);
    } 
  }
  
  public static void solvingPass(ConstraintWidgetContainer paramConstraintWidgetContainer, BasicMeasure.Measurer paramMeasurer) {
    ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = paramConstraintWidgetContainer.getHorizontalDimensionBehaviour();
    ConstraintWidget.DimensionBehaviour dimensionBehaviour1 = paramConstraintWidgetContainer.getVerticalDimensionBehaviour();
    hcount = 0;
    vcount = 0;
    paramConstraintWidgetContainer.resetFinalResolution();
    ArrayList<ConstraintWidget> arrayList = paramConstraintWidgetContainer.getChildren();
    int i = arrayList.size();
    byte b1;
    for (b1 = 0; b1 < i; b1++)
      ((ConstraintWidget)arrayList.get(b1)).resetFinalResolution(); 
    boolean bool1 = paramConstraintWidgetContainer.isRtl();
    if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.FIXED) {
      paramConstraintWidgetContainer.setFinalHorizontal(0, paramConstraintWidgetContainer.getWidth());
    } else {
      paramConstraintWidgetContainer.setFinalLeft(0);
    } 
    b1 = 0;
    boolean bool = false;
    byte b2 = 0;
    while (b2 < i) {
      byte b;
      Guideline guideline;
      ConstraintWidget constraintWidget = arrayList.get(b2);
      if (constraintWidget instanceof Guideline) {
        guideline = (Guideline)constraintWidget;
        b = b1;
        if (guideline.getOrientation() == 1) {
          if (guideline.getRelativeBegin() != -1) {
            guideline.setFinalValue(guideline.getRelativeBegin());
          } else if (guideline.getRelativeEnd() != -1 && paramConstraintWidgetContainer.isResolvedHorizontally()) {
            guideline.setFinalValue(paramConstraintWidgetContainer.getWidth() - guideline.getRelativeEnd());
          } else if (paramConstraintWidgetContainer.isResolvedHorizontally()) {
            guideline.setFinalValue((int)(guideline.getRelativePercent() * paramConstraintWidgetContainer.getWidth() + 0.5F));
          } 
          b = 1;
        } 
      } else {
        b = b1;
        if (guideline instanceof Barrier) {
          b = b1;
          if (((Barrier)guideline).getOrientation() == 0) {
            bool = true;
            b = b1;
          } 
        } 
      } 
      b2++;
      b1 = b;
    } 
    if (b1 != 0)
      for (b1 = 0; b1 < i; b1++) {
        ConstraintWidget constraintWidget = arrayList.get(b1);
        if (constraintWidget instanceof Guideline) {
          Guideline guideline = (Guideline)constraintWidget;
          if (guideline.getOrientation() == 1)
            horizontalSolvingPass(0, (ConstraintWidget)guideline, paramMeasurer, bool1); 
        } 
      }  
    horizontalSolvingPass(0, (ConstraintWidget)paramConstraintWidgetContainer, paramMeasurer, bool1);
    if (bool)
      for (b1 = 0; b1 < i; b1++) {
        ConstraintWidget constraintWidget = arrayList.get(b1);
        if (constraintWidget instanceof Barrier) {
          Barrier barrier = (Barrier)constraintWidget;
          if (barrier.getOrientation() == 0)
            solveBarrier(0, barrier, paramMeasurer, 0, bool1); 
        } 
      }  
    if (dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.FIXED) {
      paramConstraintWidgetContainer.setFinalVertical(0, paramConstraintWidgetContainer.getHeight());
    } else {
      paramConstraintWidgetContainer.setFinalTop(0);
    } 
    b1 = 0;
    bool = false;
    b2 = 0;
    while (b2 < i) {
      byte b;
      Guideline guideline;
      ConstraintWidget constraintWidget = arrayList.get(b2);
      if (constraintWidget instanceof Guideline) {
        guideline = (Guideline)constraintWidget;
        if (guideline.getOrientation() == 0) {
          if (guideline.getRelativeBegin() != -1) {
            guideline.setFinalValue(guideline.getRelativeBegin());
          } else if (guideline.getRelativeEnd() != -1 && paramConstraintWidgetContainer.isResolvedVertically()) {
            guideline.setFinalValue(paramConstraintWidgetContainer.getHeight() - guideline.getRelativeEnd());
          } else if (paramConstraintWidgetContainer.isResolvedVertically()) {
            guideline.setFinalValue((int)(guideline.getRelativePercent() * paramConstraintWidgetContainer.getHeight() + 0.5F));
          } 
          b = 1;
        } else {
          b = b1;
        } 
      } else {
        b = b1;
        if (guideline instanceof Barrier) {
          b = b1;
          if (((Barrier)guideline).getOrientation() == 1) {
            bool = true;
            b = b1;
          } 
        } 
      } 
      b2++;
      b1 = b;
    } 
    if (b1 != 0)
      for (b1 = 0; b1 < i; b1++) {
        ConstraintWidget constraintWidget = arrayList.get(b1);
        if (constraintWidget instanceof Guideline) {
          Guideline guideline = (Guideline)constraintWidget;
          if (guideline.getOrientation() == 0)
            verticalSolvingPass(1, (ConstraintWidget)guideline, paramMeasurer); 
        } 
      }  
    verticalSolvingPass(0, (ConstraintWidget)paramConstraintWidgetContainer, paramMeasurer);
    if (bool)
      for (b1 = 0; b1 < i; b1++) {
        ConstraintWidget constraintWidget = arrayList.get(b1);
        if (constraintWidget instanceof Barrier) {
          Barrier barrier = (Barrier)constraintWidget;
          if (barrier.getOrientation() == 1)
            solveBarrier(0, barrier, paramMeasurer, 1, bool1); 
        } 
      }  
    for (b1 = 0; b1 < i; b1++) {
      ConstraintWidget constraintWidget = arrayList.get(b1);
      if (constraintWidget.isMeasureRequested() && canMeasure(0, constraintWidget)) {
        ConstraintWidgetContainer.measure(0, constraintWidget, paramMeasurer, measure, BasicMeasure.Measure.SELF_DIMENSIONS);
        if (constraintWidget instanceof Guideline) {
          if (((Guideline)constraintWidget).getOrientation() == 0) {
            verticalSolvingPass(0, constraintWidget, paramMeasurer);
          } else {
            horizontalSolvingPass(0, constraintWidget, paramMeasurer, bool1);
          } 
        } else {
          horizontalSolvingPass(0, constraintWidget, paramMeasurer, bool1);
          verticalSolvingPass(0, constraintWidget, paramMeasurer);
        } 
      } 
    } 
  }
  
  private static void verticalSolvingPass(int paramInt, ConstraintWidget paramConstraintWidget, BasicMeasure.Measurer paramMeasurer) {
    if (paramConstraintWidget.isVerticalSolvingPassDone())
      return; 
    vcount++;
    if (!(paramConstraintWidget instanceof ConstraintWidgetContainer) && paramConstraintWidget.isMeasureRequested() && canMeasure(paramInt + 1, paramConstraintWidget))
      ConstraintWidgetContainer.measure(paramInt + 1, paramConstraintWidget, paramMeasurer, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS); 
    constraintAnchor2 = paramConstraintWidget.getAnchor(ConstraintAnchor.Type.TOP);
    ConstraintAnchor constraintAnchor1 = paramConstraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM);
    int j = constraintAnchor2.getFinalValue();
    int i = constraintAnchor1.getFinalValue();
    if (constraintAnchor2.getDependents() != null && constraintAnchor2.hasFinalValue())
      for (ConstraintAnchor constraintAnchor : constraintAnchor2.getDependents()) {
        ConstraintWidget constraintWidget = constraintAnchor.mOwner;
        boolean bool = canMeasure(paramInt + 1, constraintWidget);
        if (constraintWidget.isMeasureRequested() && bool)
          ConstraintWidgetContainer.measure(paramInt + 1, constraintWidget, paramMeasurer, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS); 
        if (constraintWidget.getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || bool) {
          if (constraintWidget.isMeasureRequested())
            continue; 
          if (constraintAnchor == constraintWidget.mTop && constraintWidget.mBottom.mTarget == null) {
            int k = constraintWidget.mTop.getMargin() + j;
            constraintWidget.setFinalVertical(k, constraintWidget.getHeight() + k);
            verticalSolvingPass(paramInt + 1, constraintWidget, paramMeasurer);
            continue;
          } 
          if (constraintAnchor == constraintWidget.mBottom && constraintWidget.mBottom.mTarget == null) {
            int k = j - constraintWidget.mBottom.getMargin();
            constraintWidget.setFinalVertical(k - constraintWidget.getHeight(), k);
            verticalSolvingPass(paramInt + 1, constraintWidget, paramMeasurer);
            continue;
          } 
          if (constraintAnchor == constraintWidget.mTop && constraintWidget.mBottom.mTarget != null && constraintWidget.mBottom.mTarget.hasFinalValue())
            solveVerticalCenterConstraints(paramInt + 1, paramMeasurer, constraintWidget); 
          continue;
        } 
        if (constraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mMatchConstraintMaxHeight >= 0 && constraintWidget.mMatchConstraintMinHeight >= 0 && (constraintWidget.getVisibility() == 8 || (constraintWidget.mMatchConstraintDefaultHeight == 0 && constraintWidget.getDimensionRatio() == 0.0F)) && !constraintWidget.isInVerticalChain() && !constraintWidget.isInVirtualLayout()) {
          boolean bool1;
          if ((constraintAnchor == constraintWidget.mTop && constraintWidget.mBottom.mTarget != null && constraintWidget.mBottom.mTarget.hasFinalValue()) || (constraintAnchor == constraintWidget.mBottom && constraintWidget.mTop.mTarget != null && constraintWidget.mTop.mTarget.hasFinalValue())) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
          if (bool1 && !constraintWidget.isInVerticalChain())
            solveVerticalMatchConstraint(paramInt + 1, paramConstraintWidget, paramMeasurer, constraintWidget); 
        } 
      }  
    if (paramConstraintWidget instanceof Guideline)
      return; 
    if (constraintAnchor1.getDependents() != null && constraintAnchor1.hasFinalValue())
      for (ConstraintAnchor constraintAnchor : constraintAnchor1.getDependents()) {
        int k;
        ConstraintWidget constraintWidget = constraintAnchor.mOwner;
        boolean bool = canMeasure(paramInt + 1, constraintWidget);
        if (constraintWidget.isMeasureRequested() && bool)
          ConstraintWidgetContainer.measure(paramInt + 1, constraintWidget, paramMeasurer, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS); 
        if ((constraintAnchor == constraintWidget.mTop && constraintWidget.mBottom.mTarget != null && constraintWidget.mBottom.mTarget.hasFinalValue()) || (constraintAnchor == constraintWidget.mBottom && constraintWidget.mTop.mTarget != null && constraintWidget.mTop.mTarget.hasFinalValue())) {
          k = 1;
        } else {
          k = 0;
        } 
        if (constraintWidget.getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || bool) {
          if (constraintWidget.isMeasureRequested())
            continue; 
          if (constraintAnchor == constraintWidget.mTop && constraintWidget.mBottom.mTarget == null) {
            k = constraintWidget.mTop.getMargin() + i;
            constraintWidget.setFinalVertical(k, constraintWidget.getHeight() + k);
            verticalSolvingPass(paramInt + 1, constraintWidget, paramMeasurer);
            continue;
          } 
          if (constraintAnchor == constraintWidget.mBottom && constraintWidget.mTop.mTarget == null) {
            k = i - constraintWidget.mBottom.getMargin();
            constraintWidget.setFinalVertical(k - constraintWidget.getHeight(), k);
            verticalSolvingPass(paramInt + 1, constraintWidget, paramMeasurer);
            continue;
          } 
          if (k != 0 && !constraintWidget.isInVerticalChain())
            solveVerticalCenterConstraints(paramInt + 1, paramMeasurer, constraintWidget); 
          continue;
        } 
        if (constraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mMatchConstraintMaxHeight >= 0 && constraintWidget.mMatchConstraintMinHeight >= 0 && (constraintWidget.getVisibility() == 8 || (constraintWidget.mMatchConstraintDefaultHeight == 0 && constraintWidget.getDimensionRatio() == 0.0F)) && !constraintWidget.isInVerticalChain() && !constraintWidget.isInVirtualLayout() && k != 0 && !constraintWidget.isInVerticalChain())
          solveVerticalMatchConstraint(paramInt + 1, paramConstraintWidget, paramMeasurer, constraintWidget); 
      }  
    constraintAnchor1 = paramConstraintWidget.getAnchor(ConstraintAnchor.Type.BASELINE);
    if (constraintAnchor1.getDependents() != null && constraintAnchor1.hasFinalValue()) {
      int k = constraintAnchor1.getFinalValue();
      for (ConstraintAnchor constraintAnchor2 : constraintAnchor1.getDependents()) {
        ConstraintWidget constraintWidget = constraintAnchor2.mOwner;
        boolean bool = canMeasure(paramInt + 1, constraintWidget);
        if (constraintWidget.isMeasureRequested() && bool)
          ConstraintWidgetContainer.measure(paramInt + 1, constraintWidget, paramMeasurer, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS); 
        if ((constraintWidget.getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || bool) && !constraintWidget.isMeasureRequested() && constraintAnchor2 == constraintWidget.mBaseline) {
          constraintWidget.setFinalBaseline(constraintAnchor2.getMargin() + k);
          verticalSolvingPass(paramInt + 1, constraintWidget, paramMeasurer);
        } 
      } 
    } 
    paramConstraintWidget.markVerticalSolvingPassDone();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\widgets\analyzer\Direct.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */