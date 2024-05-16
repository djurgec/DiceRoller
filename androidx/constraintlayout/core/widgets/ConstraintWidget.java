package androidx.constraintlayout.core.widgets;

import androidx.constraintlayout.core.Cache;
import androidx.constraintlayout.core.LinearSystem;
import androidx.constraintlayout.core.SolverVariable;
import androidx.constraintlayout.core.state.WidgetFrame;
import androidx.constraintlayout.core.widgets.analyzer.ChainRun;
import androidx.constraintlayout.core.widgets.analyzer.HorizontalWidgetRun;
import androidx.constraintlayout.core.widgets.analyzer.VerticalWidgetRun;
import androidx.constraintlayout.core.widgets.analyzer.WidgetRun;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class ConstraintWidget {
  public static final int ANCHOR_BASELINE = 4;
  
  public static final int ANCHOR_BOTTOM = 3;
  
  public static final int ANCHOR_LEFT = 0;
  
  public static final int ANCHOR_RIGHT = 1;
  
  public static final int ANCHOR_TOP = 2;
  
  private static final boolean AUTOTAG_CENTER = false;
  
  public static final int BOTH = 2;
  
  public static final int CHAIN_PACKED = 2;
  
  public static final int CHAIN_SPREAD = 0;
  
  public static final int CHAIN_SPREAD_INSIDE = 1;
  
  public static float DEFAULT_BIAS = 0.5F;
  
  static final int DIMENSION_HORIZONTAL = 0;
  
  static final int DIMENSION_VERTICAL = 1;
  
  protected static final int DIRECT = 2;
  
  public static final int GONE = 8;
  
  public static final int HORIZONTAL = 0;
  
  public static final int INVISIBLE = 4;
  
  public static final int MATCH_CONSTRAINT_PERCENT = 2;
  
  public static final int MATCH_CONSTRAINT_RATIO = 3;
  
  public static final int MATCH_CONSTRAINT_RATIO_RESOLVED = 4;
  
  public static final int MATCH_CONSTRAINT_SPREAD = 0;
  
  public static final int MATCH_CONSTRAINT_WRAP = 1;
  
  protected static final int SOLVER = 1;
  
  public static final int UNKNOWN = -1;
  
  private static final boolean USE_WRAP_DIMENSION_FOR_SPREAD = false;
  
  public static final int VERTICAL = 1;
  
  public static final int VISIBLE = 0;
  
  private static final int WRAP = -2;
  
  public static final int WRAP_BEHAVIOR_HORIZONTAL_ONLY = 1;
  
  public static final int WRAP_BEHAVIOR_INCLUDED = 0;
  
  public static final int WRAP_BEHAVIOR_SKIPPED = 3;
  
  public static final int WRAP_BEHAVIOR_VERTICAL_ONLY = 2;
  
  private boolean OPTIMIZE_WRAP = false;
  
  private boolean OPTIMIZE_WRAP_ON_RESOLVED = true;
  
  public WidgetFrame frame = new WidgetFrame(this);
  
  private boolean hasBaseline = false;
  
  public ChainRun horizontalChainRun;
  
  public int horizontalGroup;
  
  public HorizontalWidgetRun horizontalRun = null;
  
  private boolean horizontalSolvingPass = false;
  
  private boolean inPlaceholder;
  
  public boolean[] isTerminalWidget = new boolean[] { true, true };
  
  protected ArrayList<ConstraintAnchor> mAnchors;
  
  public ConstraintAnchor mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
  
  int mBaselineDistance;
  
  public ConstraintAnchor mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
  
  boolean mBottomHasCentered;
  
  public ConstraintAnchor mCenter;
  
  ConstraintAnchor mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
  
  ConstraintAnchor mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
  
  private float mCircleConstraintAngle = 0.0F;
  
  private Object mCompanionWidget;
  
  private int mContainerItemSkip;
  
  private String mDebugName;
  
  public float mDimensionRatio;
  
  protected int mDimensionRatioSide;
  
  int mDistToBottom;
  
  int mDistToLeft;
  
  int mDistToRight;
  
  int mDistToTop;
  
  boolean mGroupsToSolver;
  
  int mHeight;
  
  private int mHeightOverride = -1;
  
  float mHorizontalBiasPercent;
  
  boolean mHorizontalChainFixedPosition;
  
  int mHorizontalChainStyle;
  
  ConstraintWidget mHorizontalNextWidget;
  
  public int mHorizontalResolution = -1;
  
  boolean mHorizontalWrapVisited;
  
  private boolean mInVirtualLayout = false;
  
  public boolean mIsHeightWrapContent;
  
  private boolean[] mIsInBarrier;
  
  public boolean mIsWidthWrapContent;
  
  private int mLastHorizontalMeasureSpec = 0;
  
  private int mLastVerticalMeasureSpec = 0;
  
  public ConstraintAnchor mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
  
  boolean mLeftHasCentered;
  
  public ConstraintAnchor[] mListAnchors;
  
  public DimensionBehaviour[] mListDimensionBehaviors;
  
  protected ConstraintWidget[] mListNextMatchConstraintsWidget;
  
  public int mMatchConstraintDefaultHeight = 0;
  
  public int mMatchConstraintDefaultWidth = 0;
  
  public int mMatchConstraintMaxHeight = 0;
  
  public int mMatchConstraintMaxWidth = 0;
  
  public int mMatchConstraintMinHeight = 0;
  
  public int mMatchConstraintMinWidth = 0;
  
  public float mMatchConstraintPercentHeight = 1.0F;
  
  public float mMatchConstraintPercentWidth = 1.0F;
  
  private int[] mMaxDimension = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE };
  
  private boolean mMeasureRequested = true;
  
  protected int mMinHeight;
  
  protected int mMinWidth;
  
  protected ConstraintWidget[] mNextChainWidget;
  
  protected int mOffsetX;
  
  protected int mOffsetY;
  
  public ConstraintWidget mParent;
  
  int mRelX;
  
  int mRelY;
  
  float mResolvedDimensionRatio = 1.0F;
  
  int mResolvedDimensionRatioSide = -1;
  
  boolean mResolvedHasRatio = false;
  
  public int[] mResolvedMatchConstraintDefault = new int[2];
  
  public ConstraintAnchor mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
  
  boolean mRightHasCentered;
  
  public ConstraintAnchor mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
  
  boolean mTopHasCentered;
  
  private String mType;
  
  float mVerticalBiasPercent;
  
  boolean mVerticalChainFixedPosition;
  
  int mVerticalChainStyle;
  
  ConstraintWidget mVerticalNextWidget;
  
  public int mVerticalResolution = -1;
  
  boolean mVerticalWrapVisited;
  
  private int mVisibility;
  
  public float[] mWeight;
  
  int mWidth;
  
  private int mWidthOverride = -1;
  
  private int mWrapBehaviorInParent = 0;
  
  protected int mX;
  
  protected int mY;
  
  public boolean measured = false;
  
  private boolean resolvedHorizontal = false;
  
  private boolean resolvedVertical = false;
  
  public WidgetRun[] run = new WidgetRun[2];
  
  public String stringId;
  
  public ChainRun verticalChainRun;
  
  public int verticalGroup;
  
  public VerticalWidgetRun verticalRun = null;
  
  private boolean verticalSolvingPass = false;
  
  public ConstraintWidget() {
    ConstraintAnchor constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
    this.mCenter = constraintAnchor;
    this.mListAnchors = new ConstraintAnchor[] { this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, constraintAnchor };
    this.mAnchors = new ArrayList<>();
    this.mIsInBarrier = new boolean[2];
    this.mListDimensionBehaviors = new DimensionBehaviour[] { DimensionBehaviour.FIXED, DimensionBehaviour.FIXED };
    this.mParent = null;
    this.mWidth = 0;
    this.mHeight = 0;
    this.mDimensionRatio = 0.0F;
    this.mDimensionRatioSide = -1;
    this.mX = 0;
    this.mY = 0;
    this.mRelX = 0;
    this.mRelY = 0;
    this.mOffsetX = 0;
    this.mOffsetY = 0;
    this.mBaselineDistance = 0;
    float f = DEFAULT_BIAS;
    this.mHorizontalBiasPercent = f;
    this.mVerticalBiasPercent = f;
    this.mContainerItemSkip = 0;
    this.mVisibility = 0;
    this.mDebugName = null;
    this.mType = null;
    this.mGroupsToSolver = false;
    this.mHorizontalChainStyle = 0;
    this.mVerticalChainStyle = 0;
    this.mWeight = new float[] { -1.0F, -1.0F };
    this.mListNextMatchConstraintsWidget = new ConstraintWidget[] { null, null };
    this.mNextChainWidget = new ConstraintWidget[] { null, null };
    this.mHorizontalNextWidget = null;
    this.mVerticalNextWidget = null;
    this.horizontalGroup = -1;
    this.verticalGroup = -1;
    addAnchors();
  }
  
  public ConstraintWidget(int paramInt1, int paramInt2) {
    this(0, 0, paramInt1, paramInt2);
  }
  
  public ConstraintWidget(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    ConstraintAnchor constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
    this.mCenter = constraintAnchor;
    this.mListAnchors = new ConstraintAnchor[] { this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, constraintAnchor };
    this.mAnchors = new ArrayList<>();
    this.mIsInBarrier = new boolean[2];
    this.mListDimensionBehaviors = new DimensionBehaviour[] { DimensionBehaviour.FIXED, DimensionBehaviour.FIXED };
    this.mParent = null;
    this.mWidth = 0;
    this.mHeight = 0;
    this.mDimensionRatio = 0.0F;
    this.mDimensionRatioSide = -1;
    this.mX = 0;
    this.mY = 0;
    this.mRelX = 0;
    this.mRelY = 0;
    this.mOffsetX = 0;
    this.mOffsetY = 0;
    this.mBaselineDistance = 0;
    float f = DEFAULT_BIAS;
    this.mHorizontalBiasPercent = f;
    this.mVerticalBiasPercent = f;
    this.mContainerItemSkip = 0;
    this.mVisibility = 0;
    this.mDebugName = null;
    this.mType = null;
    this.mGroupsToSolver = false;
    this.mHorizontalChainStyle = 0;
    this.mVerticalChainStyle = 0;
    this.mWeight = new float[] { -1.0F, -1.0F };
    this.mListNextMatchConstraintsWidget = new ConstraintWidget[] { null, null };
    this.mNextChainWidget = new ConstraintWidget[] { null, null };
    this.mHorizontalNextWidget = null;
    this.mVerticalNextWidget = null;
    this.horizontalGroup = -1;
    this.verticalGroup = -1;
    this.mX = paramInt1;
    this.mY = paramInt2;
    this.mWidth = paramInt3;
    this.mHeight = paramInt4;
    addAnchors();
  }
  
  public ConstraintWidget(String paramString) {
    ConstraintAnchor constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
    this.mCenter = constraintAnchor;
    this.mListAnchors = new ConstraintAnchor[] { this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, constraintAnchor };
    this.mAnchors = new ArrayList<>();
    this.mIsInBarrier = new boolean[2];
    this.mListDimensionBehaviors = new DimensionBehaviour[] { DimensionBehaviour.FIXED, DimensionBehaviour.FIXED };
    this.mParent = null;
    this.mWidth = 0;
    this.mHeight = 0;
    this.mDimensionRatio = 0.0F;
    this.mDimensionRatioSide = -1;
    this.mX = 0;
    this.mY = 0;
    this.mRelX = 0;
    this.mRelY = 0;
    this.mOffsetX = 0;
    this.mOffsetY = 0;
    this.mBaselineDistance = 0;
    float f = DEFAULT_BIAS;
    this.mHorizontalBiasPercent = f;
    this.mVerticalBiasPercent = f;
    this.mContainerItemSkip = 0;
    this.mVisibility = 0;
    this.mDebugName = null;
    this.mType = null;
    this.mGroupsToSolver = false;
    this.mHorizontalChainStyle = 0;
    this.mVerticalChainStyle = 0;
    this.mWeight = new float[] { -1.0F, -1.0F };
    this.mListNextMatchConstraintsWidget = new ConstraintWidget[] { null, null };
    this.mNextChainWidget = new ConstraintWidget[] { null, null };
    this.mHorizontalNextWidget = null;
    this.mVerticalNextWidget = null;
    this.horizontalGroup = -1;
    this.verticalGroup = -1;
    addAnchors();
    setDebugName(paramString);
  }
  
  public ConstraintWidget(String paramString, int paramInt1, int paramInt2) {
    this(paramInt1, paramInt2);
    setDebugName(paramString);
  }
  
  public ConstraintWidget(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this(paramInt1, paramInt2, paramInt3, paramInt4);
    setDebugName(paramString);
  }
  
  private void addAnchors() {
    this.mAnchors.add(this.mLeft);
    this.mAnchors.add(this.mTop);
    this.mAnchors.add(this.mRight);
    this.mAnchors.add(this.mBottom);
    this.mAnchors.add(this.mCenterX);
    this.mAnchors.add(this.mCenterY);
    this.mAnchors.add(this.mCenter);
    this.mAnchors.add(this.mBaseline);
  }
  
  private void applyConstraints(LinearSystem paramLinearSystem, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, DimensionBehaviour paramDimensionBehaviour, boolean paramBoolean5, ConstraintAnchor paramConstraintAnchor1, ConstraintAnchor paramConstraintAnchor2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat1, boolean paramBoolean6, boolean paramBoolean7, boolean paramBoolean8, boolean paramBoolean9, boolean paramBoolean10, int paramInt5, int paramInt6, int paramInt7, int paramInt8, float paramFloat2, boolean paramBoolean11) {
    // Byte code:
    //   0: aload_1
    //   1: aload #10
    //   3: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   6: astore #38
    //   8: aload_1
    //   9: aload #11
    //   11: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   14: astore #36
    //   16: aload_1
    //   17: aload #10
    //   19: invokevirtual getTarget : ()Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   22: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   25: astore #39
    //   27: aload_1
    //   28: aload #11
    //   30: invokevirtual getTarget : ()Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   33: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   36: astore #37
    //   38: invokestatic getMetrics : ()Landroidx/constraintlayout/core/Metrics;
    //   41: ifnull -> 61
    //   44: invokestatic getMetrics : ()Landroidx/constraintlayout/core/Metrics;
    //   47: astore #35
    //   49: aload #35
    //   51: aload #35
    //   53: getfield nonresolvedWidgets : J
    //   56: lconst_1
    //   57: ladd
    //   58: putfield nonresolvedWidgets : J
    //   61: aload #10
    //   63: invokevirtual isConnected : ()Z
    //   66: istore #34
    //   68: aload #11
    //   70: invokevirtual isConnected : ()Z
    //   73: istore #32
    //   75: aload_0
    //   76: getfield mCenter : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   79: invokevirtual isConnected : ()Z
    //   82: istore #33
    //   84: iconst_0
    //   85: istore #31
    //   87: iconst_0
    //   88: istore #29
    //   90: iload #34
    //   92: ifeq -> 100
    //   95: iconst_0
    //   96: iconst_1
    //   97: iadd
    //   98: istore #29
    //   100: iload #29
    //   102: istore #28
    //   104: iload #32
    //   106: ifeq -> 115
    //   109: iload #29
    //   111: iconst_1
    //   112: iadd
    //   113: istore #28
    //   115: iload #33
    //   117: ifeq -> 129
    //   120: iload #28
    //   122: iconst_1
    //   123: iadd
    //   124: istore #30
    //   126: goto -> 133
    //   129: iload #28
    //   131: istore #30
    //   133: iload #17
    //   135: ifeq -> 144
    //   138: iconst_3
    //   139: istore #29
    //   141: goto -> 148
    //   144: iload #22
    //   146: istore #29
    //   148: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$1.$SwitchMap$androidx$constraintlayout$core$widgets$ConstraintWidget$DimensionBehaviour : [I
    //   151: aload #8
    //   153: invokevirtual ordinal : ()I
    //   156: iaload
    //   157: tableswitch default -> 188, 1 -> 225, 2 -> 219, 3 -> 213, 4 -> 195
    //   188: iload #31
    //   190: istore #22
    //   192: goto -> 228
    //   195: iload #29
    //   197: iconst_4
    //   198: if_icmpeq -> 207
    //   201: iconst_1
    //   202: istore #22
    //   204: goto -> 210
    //   207: iconst_0
    //   208: istore #22
    //   210: goto -> 228
    //   213: iconst_0
    //   214: istore #22
    //   216: goto -> 228
    //   219: iconst_0
    //   220: istore #22
    //   222: goto -> 228
    //   225: iconst_0
    //   226: istore #22
    //   228: aload_0
    //   229: getfield mWidthOverride : I
    //   232: iconst_m1
    //   233: if_icmpeq -> 257
    //   236: iload_2
    //   237: ifeq -> 257
    //   240: iconst_0
    //   241: istore #22
    //   243: aload_0
    //   244: getfield mWidthOverride : I
    //   247: istore #13
    //   249: aload_0
    //   250: iconst_m1
    //   251: putfield mWidthOverride : I
    //   254: goto -> 257
    //   257: aload_0
    //   258: getfield mHeightOverride : I
    //   261: iconst_m1
    //   262: if_icmpeq -> 286
    //   265: iload_2
    //   266: ifne -> 286
    //   269: iconst_0
    //   270: istore #13
    //   272: aload_0
    //   273: getfield mHeightOverride : I
    //   276: istore #22
    //   278: aload_0
    //   279: iconst_m1
    //   280: putfield mHeightOverride : I
    //   283: goto -> 298
    //   286: iload #22
    //   288: istore #28
    //   290: iload #13
    //   292: istore #22
    //   294: iload #28
    //   296: istore #13
    //   298: aload_0
    //   299: getfield mVisibility : I
    //   302: bipush #8
    //   304: if_icmpne -> 316
    //   307: iconst_0
    //   308: istore #13
    //   310: iconst_0
    //   311: istore #22
    //   313: goto -> 328
    //   316: iload #22
    //   318: istore #28
    //   320: iload #13
    //   322: istore #22
    //   324: iload #28
    //   326: istore #13
    //   328: iload #27
    //   330: ifeq -> 385
    //   333: iload #34
    //   335: ifne -> 359
    //   338: iload #32
    //   340: ifne -> 359
    //   343: iload #33
    //   345: ifne -> 359
    //   348: aload_1
    //   349: aload #38
    //   351: iload #12
    //   353: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;I)V
    //   356: goto -> 385
    //   359: iload #34
    //   361: ifeq -> 385
    //   364: iload #32
    //   366: ifne -> 385
    //   369: aload_1
    //   370: aload #38
    //   372: aload #39
    //   374: aload #10
    //   376: invokevirtual getMargin : ()I
    //   379: bipush #8
    //   381: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   384: pop
    //   385: iload #22
    //   387: ifne -> 472
    //   390: iload #9
    //   392: ifeq -> 448
    //   395: aload_1
    //   396: aload #36
    //   398: aload #38
    //   400: iconst_0
    //   401: iconst_3
    //   402: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   405: pop
    //   406: iload #14
    //   408: ifle -> 426
    //   411: aload_1
    //   412: aload #36
    //   414: aload #38
    //   416: iload #14
    //   418: bipush #8
    //   420: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   423: goto -> 426
    //   426: iload #15
    //   428: ldc 2147483647
    //   430: if_icmpge -> 461
    //   433: aload_1
    //   434: aload #36
    //   436: aload #38
    //   438: iload #15
    //   440: bipush #8
    //   442: invokevirtual addLowerThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   445: goto -> 461
    //   448: aload_1
    //   449: aload #36
    //   451: aload #38
    //   453: iload #13
    //   455: bipush #8
    //   457: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   460: pop
    //   461: iload #25
    //   463: istore #12
    //   465: iload #22
    //   467: istore #28
    //   469: goto -> 948
    //   472: iload #30
    //   474: iconst_2
    //   475: if_icmpeq -> 544
    //   478: iload #17
    //   480: ifne -> 544
    //   483: iload #29
    //   485: iconst_1
    //   486: if_icmpeq -> 494
    //   489: iload #29
    //   491: ifne -> 544
    //   494: iconst_0
    //   495: istore #28
    //   497: iload #24
    //   499: iload #13
    //   501: invokestatic max : (II)I
    //   504: istore #13
    //   506: iload #13
    //   508: istore #12
    //   510: iload #25
    //   512: ifle -> 524
    //   515: iload #25
    //   517: iload #13
    //   519: invokestatic min : (II)I
    //   522: istore #12
    //   524: aload_1
    //   525: aload #36
    //   527: aload #38
    //   529: iload #12
    //   531: bipush #8
    //   533: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   536: pop
    //   537: iload #25
    //   539: istore #12
    //   541: goto -> 948
    //   544: iload #24
    //   546: bipush #-2
    //   548: if_icmpne -> 558
    //   551: iload #13
    //   553: istore #12
    //   555: goto -> 562
    //   558: iload #24
    //   560: istore #12
    //   562: iload #25
    //   564: bipush #-2
    //   566: if_icmpne -> 576
    //   569: iload #13
    //   571: istore #15
    //   573: goto -> 580
    //   576: iload #25
    //   578: istore #15
    //   580: iload #13
    //   582: istore #24
    //   584: iload #13
    //   586: ifle -> 602
    //   589: iload #13
    //   591: istore #24
    //   593: iload #29
    //   595: iconst_1
    //   596: if_icmpeq -> 602
    //   599: iconst_0
    //   600: istore #24
    //   602: iload #24
    //   604: istore #13
    //   606: iload #12
    //   608: ifle -> 632
    //   611: aload_1
    //   612: aload #36
    //   614: aload #38
    //   616: iload #12
    //   618: bipush #8
    //   620: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   623: iload #24
    //   625: iload #12
    //   627: invokestatic max : (II)I
    //   630: istore #13
    //   632: iload #13
    //   634: istore #24
    //   636: iload #15
    //   638: ifle -> 691
    //   641: iconst_1
    //   642: istore #25
    //   644: iload #25
    //   646: istore #24
    //   648: iload_3
    //   649: ifeq -> 665
    //   652: iload #25
    //   654: istore #24
    //   656: iload #29
    //   658: iconst_1
    //   659: if_icmpne -> 665
    //   662: iconst_0
    //   663: istore #24
    //   665: iload #24
    //   667: ifeq -> 682
    //   670: aload_1
    //   671: aload #36
    //   673: aload #38
    //   675: iload #15
    //   677: bipush #8
    //   679: invokevirtual addLowerThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   682: iload #13
    //   684: iload #15
    //   686: invokestatic min : (II)I
    //   689: istore #24
    //   691: iload #29
    //   693: iconst_1
    //   694: if_icmpne -> 788
    //   697: iload_3
    //   698: ifeq -> 717
    //   701: aload_1
    //   702: aload #36
    //   704: aload #38
    //   706: iload #24
    //   708: bipush #8
    //   710: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   713: pop
    //   714: goto -> 773
    //   717: iload #19
    //   719: ifeq -> 749
    //   722: aload_1
    //   723: aload #36
    //   725: aload #38
    //   727: iload #24
    //   729: iconst_5
    //   730: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   733: pop
    //   734: aload_1
    //   735: aload #36
    //   737: aload #38
    //   739: iload #24
    //   741: bipush #8
    //   743: invokevirtual addLowerThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   746: goto -> 773
    //   749: aload_1
    //   750: aload #36
    //   752: aload #38
    //   754: iload #24
    //   756: iconst_5
    //   757: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   760: pop
    //   761: aload_1
    //   762: aload #36
    //   764: aload #38
    //   766: iload #24
    //   768: bipush #8
    //   770: invokevirtual addLowerThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   773: iload #12
    //   775: istore #24
    //   777: iload #22
    //   779: istore #28
    //   781: iload #15
    //   783: istore #12
    //   785: goto -> 948
    //   788: iload #29
    //   790: iconst_2
    //   791: if_icmpne -> 933
    //   794: aload #10
    //   796: invokevirtual getType : ()Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   799: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   802: if_acmpeq -> 854
    //   805: aload #10
    //   807: invokevirtual getType : ()Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   810: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   813: if_acmpne -> 819
    //   816: goto -> 854
    //   819: aload_1
    //   820: aload_0
    //   821: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   824: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   827: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   830: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   833: astore #8
    //   835: aload_1
    //   836: aload_0
    //   837: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   840: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   843: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   846: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   849: astore #35
    //   851: goto -> 886
    //   854: aload_1
    //   855: aload_0
    //   856: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   859: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   862: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   865: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   868: astore #8
    //   870: aload_1
    //   871: aload_0
    //   872: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   875: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   878: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   881: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   884: astore #35
    //   886: aload_1
    //   887: invokevirtual createRow : ()Landroidx/constraintlayout/core/ArrayRow;
    //   890: astore #40
    //   892: aload_1
    //   893: aload #40
    //   895: aload #36
    //   897: aload #38
    //   899: aload #35
    //   901: aload #8
    //   903: fload #26
    //   905: invokevirtual createRowDimensionRatio : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;F)Landroidx/constraintlayout/core/ArrayRow;
    //   908: invokevirtual addConstraint : (Landroidx/constraintlayout/core/ArrayRow;)V
    //   911: iload_3
    //   912: ifeq -> 918
    //   915: iconst_0
    //   916: istore #22
    //   918: iload #12
    //   920: istore #24
    //   922: iload #22
    //   924: istore #28
    //   926: iload #15
    //   928: istore #12
    //   930: goto -> 948
    //   933: iload #12
    //   935: istore #24
    //   937: iconst_1
    //   938: istore #5
    //   940: iload #15
    //   942: istore #12
    //   944: iload #22
    //   946: istore #28
    //   948: iload #27
    //   950: ifeq -> 2488
    //   953: iload #19
    //   955: ifeq -> 961
    //   958: goto -> 2488
    //   961: iconst_5
    //   962: istore #13
    //   964: iload #34
    //   966: ifne -> 982
    //   969: iload #32
    //   971: ifne -> 982
    //   974: iload #33
    //   976: ifne -> 982
    //   979: goto -> 2383
    //   982: iload #34
    //   984: ifeq -> 1029
    //   987: iload #32
    //   989: ifne -> 1029
    //   992: aload #10
    //   994: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   997: getfield mOwner : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   1000: astore #6
    //   1002: iload #13
    //   1004: istore #12
    //   1006: iload_3
    //   1007: ifeq -> 1026
    //   1010: iload #13
    //   1012: istore #12
    //   1014: aload #6
    //   1016: instanceof androidx/constraintlayout/core/widgets/Barrier
    //   1019: ifeq -> 1026
    //   1022: bipush #8
    //   1024: istore #12
    //   1026: goto -> 2386
    //   1029: iload #34
    //   1031: ifne -> 1133
    //   1034: iload #32
    //   1036: ifeq -> 1133
    //   1039: aload_1
    //   1040: aload #36
    //   1042: aload #37
    //   1044: aload #11
    //   1046: invokevirtual getMargin : ()I
    //   1049: ineg
    //   1050: bipush #8
    //   1052: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   1055: pop
    //   1056: iload_3
    //   1057: ifeq -> 1130
    //   1060: aload_0
    //   1061: getfield OPTIMIZE_WRAP : Z
    //   1064: ifeq -> 1117
    //   1067: aload #38
    //   1069: getfield isFinalValue : Z
    //   1072: ifeq -> 1117
    //   1075: aload_0
    //   1076: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   1079: astore #8
    //   1081: aload #8
    //   1083: ifnull -> 1117
    //   1086: aload #8
    //   1088: checkcast androidx/constraintlayout/core/widgets/ConstraintWidgetContainer
    //   1091: astore #6
    //   1093: iload_2
    //   1094: ifeq -> 1107
    //   1097: aload #6
    //   1099: aload #10
    //   1101: invokevirtual addHorizontalWrapMinVariable : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;)V
    //   1104: goto -> 1114
    //   1107: aload #6
    //   1109: aload #10
    //   1111: invokevirtual addVerticalWrapMinVariable : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;)V
    //   1114: goto -> 2383
    //   1117: aload_1
    //   1118: aload #38
    //   1120: aload #6
    //   1122: iconst_0
    //   1123: iconst_5
    //   1124: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   1127: goto -> 2383
    //   1130: goto -> 2383
    //   1133: iload #34
    //   1135: ifeq -> 2383
    //   1138: iload #32
    //   1140: ifeq -> 2383
    //   1143: iconst_0
    //   1144: istore #25
    //   1146: iconst_0
    //   1147: istore #13
    //   1149: iconst_4
    //   1150: istore #15
    //   1152: bipush #6
    //   1154: istore #30
    //   1156: iconst_5
    //   1157: istore #22
    //   1159: aload #10
    //   1161: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1164: getfield mOwner : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   1167: astore #8
    //   1169: aload #11
    //   1171: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1174: getfield mOwner : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   1177: astore #35
    //   1179: aload_0
    //   1180: invokevirtual getParent : ()Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   1183: astore #40
    //   1185: iload #28
    //   1187: ifeq -> 1665
    //   1190: iload #29
    //   1192: ifne -> 1331
    //   1195: iload #12
    //   1197: ifne -> 1275
    //   1200: iload #24
    //   1202: ifne -> 1275
    //   1205: iconst_1
    //   1206: istore #25
    //   1208: aload #39
    //   1210: getfield isFinalValue : Z
    //   1213: ifeq -> 1258
    //   1216: aload #37
    //   1218: getfield isFinalValue : Z
    //   1221: ifeq -> 1258
    //   1224: aload_1
    //   1225: aload #38
    //   1227: aload #39
    //   1229: aload #10
    //   1231: invokevirtual getMargin : ()I
    //   1234: bipush #8
    //   1236: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   1239: pop
    //   1240: aload_1
    //   1241: aload #36
    //   1243: aload #37
    //   1245: aload #11
    //   1247: invokevirtual getMargin : ()I
    //   1250: ineg
    //   1251: bipush #8
    //   1253: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   1256: pop
    //   1257: return
    //   1258: iconst_0
    //   1259: istore #13
    //   1261: bipush #8
    //   1263: istore #22
    //   1265: bipush #8
    //   1267: istore #15
    //   1269: iconst_0
    //   1270: istore #12
    //   1272: goto -> 1295
    //   1275: iconst_1
    //   1276: istore #23
    //   1278: iconst_1
    //   1279: istore #12
    //   1281: iconst_5
    //   1282: istore #22
    //   1284: iconst_5
    //   1285: istore #15
    //   1287: iload #13
    //   1289: istore #25
    //   1291: iload #23
    //   1293: istore #13
    //   1295: aload #8
    //   1297: instanceof androidx/constraintlayout/core/widgets/Barrier
    //   1300: ifne -> 1321
    //   1303: aload #35
    //   1305: instanceof androidx/constraintlayout/core/widgets/Barrier
    //   1308: ifeq -> 1314
    //   1311: goto -> 1321
    //   1314: iload #30
    //   1316: istore #23
    //   1318: goto -> 1766
    //   1321: iconst_4
    //   1322: istore #15
    //   1324: iload #30
    //   1326: istore #23
    //   1328: goto -> 1766
    //   1331: iload #29
    //   1333: iconst_2
    //   1334: if_icmpne -> 1391
    //   1337: iconst_5
    //   1338: istore #22
    //   1340: iconst_5
    //   1341: istore #15
    //   1343: aload #8
    //   1345: instanceof androidx/constraintlayout/core/widgets/Barrier
    //   1348: ifne -> 1375
    //   1351: aload #35
    //   1353: instanceof androidx/constraintlayout/core/widgets/Barrier
    //   1356: ifeq -> 1362
    //   1359: goto -> 1375
    //   1362: iconst_1
    //   1363: istore #13
    //   1365: iconst_1
    //   1366: istore #12
    //   1368: iload #30
    //   1370: istore #23
    //   1372: goto -> 1766
    //   1375: iconst_4
    //   1376: istore #15
    //   1378: iconst_1
    //   1379: istore #13
    //   1381: iconst_1
    //   1382: istore #12
    //   1384: iload #30
    //   1386: istore #23
    //   1388: goto -> 1766
    //   1391: iload #29
    //   1393: iconst_1
    //   1394: if_icmpne -> 1414
    //   1397: bipush #8
    //   1399: istore #22
    //   1401: iconst_1
    //   1402: istore #13
    //   1404: iconst_1
    //   1405: istore #12
    //   1407: iload #30
    //   1409: istore #23
    //   1411: goto -> 1766
    //   1414: iload #29
    //   1416: iconst_3
    //   1417: if_icmpne -> 1652
    //   1420: aload_0
    //   1421: getfield mResolvedDimensionRatioSide : I
    //   1424: iconst_m1
    //   1425: if_icmpne -> 1487
    //   1428: iconst_1
    //   1429: istore #25
    //   1431: bipush #8
    //   1433: istore #22
    //   1435: iconst_5
    //   1436: istore #15
    //   1438: iload #20
    //   1440: ifeq -> 1474
    //   1443: iconst_5
    //   1444: istore #15
    //   1446: iconst_4
    //   1447: istore #23
    //   1449: iload_3
    //   1450: ifeq -> 1465
    //   1453: iconst_5
    //   1454: istore #23
    //   1456: iconst_1
    //   1457: istore #13
    //   1459: iconst_1
    //   1460: istore #12
    //   1462: goto -> 1766
    //   1465: iconst_1
    //   1466: istore #13
    //   1468: iconst_1
    //   1469: istore #12
    //   1471: goto -> 1766
    //   1474: bipush #8
    //   1476: istore #23
    //   1478: iconst_1
    //   1479: istore #13
    //   1481: iconst_1
    //   1482: istore #12
    //   1484: goto -> 1766
    //   1487: iconst_1
    //   1488: istore #25
    //   1490: iload #17
    //   1492: ifeq -> 1544
    //   1495: iload #23
    //   1497: iconst_2
    //   1498: if_icmpeq -> 1516
    //   1501: iload #23
    //   1503: iconst_1
    //   1504: if_icmpne -> 1510
    //   1507: goto -> 1516
    //   1510: iconst_0
    //   1511: istore #12
    //   1513: goto -> 1519
    //   1516: iconst_1
    //   1517: istore #12
    //   1519: iload #12
    //   1521: ifne -> 1531
    //   1524: bipush #8
    //   1526: istore #22
    //   1528: iconst_5
    //   1529: istore #15
    //   1531: iconst_1
    //   1532: istore #13
    //   1534: iconst_1
    //   1535: istore #12
    //   1537: iload #30
    //   1539: istore #23
    //   1541: goto -> 1766
    //   1544: iconst_5
    //   1545: istore #22
    //   1547: iload #12
    //   1549: ifle -> 1568
    //   1552: iconst_5
    //   1553: istore #15
    //   1555: iconst_1
    //   1556: istore #13
    //   1558: iconst_1
    //   1559: istore #12
    //   1561: iload #30
    //   1563: istore #23
    //   1565: goto -> 1766
    //   1568: iload #12
    //   1570: ifne -> 1639
    //   1573: iload #24
    //   1575: ifne -> 1639
    //   1578: iload #20
    //   1580: ifne -> 1600
    //   1583: bipush #8
    //   1585: istore #15
    //   1587: iconst_1
    //   1588: istore #13
    //   1590: iconst_1
    //   1591: istore #12
    //   1593: iload #30
    //   1595: istore #23
    //   1597: goto -> 1766
    //   1600: aload #8
    //   1602: aload #40
    //   1604: if_acmpeq -> 1620
    //   1607: aload #35
    //   1609: aload #40
    //   1611: if_acmpeq -> 1620
    //   1614: iconst_4
    //   1615: istore #22
    //   1617: goto -> 1623
    //   1620: iconst_5
    //   1621: istore #22
    //   1623: iconst_4
    //   1624: istore #15
    //   1626: iconst_1
    //   1627: istore #13
    //   1629: iconst_1
    //   1630: istore #12
    //   1632: iload #30
    //   1634: istore #23
    //   1636: goto -> 1766
    //   1639: iconst_1
    //   1640: istore #13
    //   1642: iconst_1
    //   1643: istore #12
    //   1645: iload #30
    //   1647: istore #23
    //   1649: goto -> 1766
    //   1652: iconst_0
    //   1653: istore #13
    //   1655: iconst_0
    //   1656: istore #12
    //   1658: iload #30
    //   1660: istore #23
    //   1662: goto -> 1766
    //   1665: iconst_1
    //   1666: istore #13
    //   1668: aload #39
    //   1670: getfield isFinalValue : Z
    //   1673: ifeq -> 1759
    //   1676: aload #37
    //   1678: getfield isFinalValue : Z
    //   1681: ifeq -> 1759
    //   1684: aload_1
    //   1685: aload #38
    //   1687: aload #39
    //   1689: aload #10
    //   1691: invokevirtual getMargin : ()I
    //   1694: fload #16
    //   1696: aload #37
    //   1698: aload #36
    //   1700: aload #11
    //   1702: invokevirtual getMargin : ()I
    //   1705: bipush #8
    //   1707: invokevirtual addCentering : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;IFLandroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   1710: iload_3
    //   1711: ifeq -> 1758
    //   1714: iload #5
    //   1716: ifeq -> 1758
    //   1719: iconst_0
    //   1720: istore #12
    //   1722: aload #11
    //   1724: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1727: ifnull -> 1737
    //   1730: aload #11
    //   1732: invokevirtual getMargin : ()I
    //   1735: istore #12
    //   1737: aload #37
    //   1739: aload #7
    //   1741: if_acmpeq -> 1758
    //   1744: aload_1
    //   1745: aload #7
    //   1747: aload #36
    //   1749: iload #12
    //   1751: iconst_5
    //   1752: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   1755: goto -> 1758
    //   1758: return
    //   1759: iconst_1
    //   1760: istore #12
    //   1762: iload #30
    //   1764: istore #23
    //   1766: iconst_1
    //   1767: istore #31
    //   1769: iload #12
    //   1771: ifeq -> 1797
    //   1774: aload #39
    //   1776: aload #37
    //   1778: if_acmpne -> 1797
    //   1781: aload #8
    //   1783: aload #40
    //   1785: if_acmpeq -> 1797
    //   1788: iconst_0
    //   1789: istore #12
    //   1791: iconst_0
    //   1792: istore #30
    //   1794: goto -> 1805
    //   1797: iload #12
    //   1799: istore #30
    //   1801: iload #31
    //   1803: istore #12
    //   1805: iload #13
    //   1807: ifeq -> 1900
    //   1810: iload #28
    //   1812: ifne -> 1855
    //   1815: iload #18
    //   1817: ifne -> 1855
    //   1820: iload #20
    //   1822: ifne -> 1855
    //   1825: aload #39
    //   1827: aload #6
    //   1829: if_acmpne -> 1855
    //   1832: aload #37
    //   1834: aload #7
    //   1836: if_acmpne -> 1855
    //   1839: bipush #8
    //   1841: istore #23
    //   1843: bipush #8
    //   1845: istore #12
    //   1847: iconst_0
    //   1848: istore #13
    //   1850: iconst_0
    //   1851: istore_3
    //   1852: goto -> 1863
    //   1855: iload #12
    //   1857: istore #13
    //   1859: iload #22
    //   1861: istore #12
    //   1863: aload #10
    //   1865: invokevirtual getMargin : ()I
    //   1868: istore #22
    //   1870: aload #11
    //   1872: invokevirtual getMargin : ()I
    //   1875: istore #31
    //   1877: aload_1
    //   1878: aload #38
    //   1880: aload #39
    //   1882: iload #22
    //   1884: fload #16
    //   1886: aload #37
    //   1888: aload #36
    //   1890: iload #31
    //   1892: iload #23
    //   1894: invokevirtual addCentering : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;IFLandroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   1897: goto -> 1908
    //   1900: iload #12
    //   1902: istore #13
    //   1904: iload #22
    //   1906: istore #12
    //   1908: iconst_5
    //   1909: istore #23
    //   1911: aload_0
    //   1912: getfield mVisibility : I
    //   1915: bipush #8
    //   1917: if_icmpne -> 1929
    //   1920: aload #11
    //   1922: invokevirtual hasDependents : ()Z
    //   1925: ifne -> 1929
    //   1928: return
    //   1929: iload #30
    //   1931: ifeq -> 2010
    //   1934: iload_3
    //   1935: ifeq -> 1976
    //   1938: aload #39
    //   1940: aload #37
    //   1942: if_acmpeq -> 1976
    //   1945: iload #28
    //   1947: ifne -> 1976
    //   1950: aload #8
    //   1952: instanceof androidx/constraintlayout/core/widgets/Barrier
    //   1955: ifne -> 1969
    //   1958: aload #35
    //   1960: instanceof androidx/constraintlayout/core/widgets/Barrier
    //   1963: ifeq -> 1976
    //   1966: goto -> 1969
    //   1969: bipush #6
    //   1971: istore #12
    //   1973: goto -> 1976
    //   1976: aload_1
    //   1977: aload #38
    //   1979: aload #39
    //   1981: aload #10
    //   1983: invokevirtual getMargin : ()I
    //   1986: iload #12
    //   1988: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   1991: aload_1
    //   1992: aload #36
    //   1994: aload #37
    //   1996: aload #11
    //   1998: invokevirtual getMargin : ()I
    //   2001: ineg
    //   2002: iload #12
    //   2004: invokevirtual addLowerThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   2007: goto -> 2010
    //   2010: iload_3
    //   2011: ifeq -> 2056
    //   2014: iload #21
    //   2016: ifeq -> 2056
    //   2019: aload #8
    //   2021: instanceof androidx/constraintlayout/core/widgets/Barrier
    //   2024: ifne -> 2056
    //   2027: aload #35
    //   2029: instanceof androidx/constraintlayout/core/widgets/Barrier
    //   2032: ifne -> 2056
    //   2035: aload #35
    //   2037: aload #40
    //   2039: if_acmpeq -> 2056
    //   2042: iconst_1
    //   2043: istore #13
    //   2045: bipush #6
    //   2047: istore #15
    //   2049: bipush #6
    //   2051: istore #12
    //   2053: goto -> 2068
    //   2056: iload #12
    //   2058: istore #22
    //   2060: iload #15
    //   2062: istore #12
    //   2064: iload #22
    //   2066: istore #15
    //   2068: iload #13
    //   2070: ifeq -> 2262
    //   2073: iload #12
    //   2075: istore #13
    //   2077: iload #25
    //   2079: ifeq -> 2173
    //   2082: iload #20
    //   2084: ifeq -> 2096
    //   2087: iload #12
    //   2089: istore #13
    //   2091: iload #4
    //   2093: ifeq -> 2173
    //   2096: iload #12
    //   2098: istore #13
    //   2100: aload #8
    //   2102: aload #40
    //   2104: if_acmpeq -> 2114
    //   2107: aload #35
    //   2109: aload #40
    //   2111: if_acmpne -> 2118
    //   2114: bipush #6
    //   2116: istore #13
    //   2118: aload #8
    //   2120: instanceof androidx/constraintlayout/core/widgets/Guideline
    //   2123: ifne -> 2134
    //   2126: aload #35
    //   2128: instanceof androidx/constraintlayout/core/widgets/Guideline
    //   2131: ifeq -> 2137
    //   2134: iconst_5
    //   2135: istore #13
    //   2137: aload #8
    //   2139: instanceof androidx/constraintlayout/core/widgets/Barrier
    //   2142: ifne -> 2153
    //   2145: aload #35
    //   2147: instanceof androidx/constraintlayout/core/widgets/Barrier
    //   2150: ifeq -> 2156
    //   2153: iconst_5
    //   2154: istore #13
    //   2156: iload #20
    //   2158: ifeq -> 2164
    //   2161: iconst_5
    //   2162: istore #13
    //   2164: iload #13
    //   2166: iload #12
    //   2168: invokestatic max : (II)I
    //   2171: istore #13
    //   2173: iload #13
    //   2175: istore #12
    //   2177: iload_3
    //   2178: ifeq -> 2229
    //   2181: iload #15
    //   2183: iload #13
    //   2185: invokestatic min : (II)I
    //   2188: istore #13
    //   2190: iload #13
    //   2192: istore #12
    //   2194: iload #17
    //   2196: ifeq -> 2229
    //   2199: iload #13
    //   2201: istore #12
    //   2203: iload #20
    //   2205: ifne -> 2229
    //   2208: aload #8
    //   2210: aload #40
    //   2212: if_acmpeq -> 2226
    //   2215: iload #13
    //   2217: istore #12
    //   2219: aload #35
    //   2221: aload #40
    //   2223: if_acmpne -> 2229
    //   2226: iconst_4
    //   2227: istore #12
    //   2229: aload_1
    //   2230: aload #38
    //   2232: aload #39
    //   2234: aload #10
    //   2236: invokevirtual getMargin : ()I
    //   2239: iload #12
    //   2241: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   2244: pop
    //   2245: aload_1
    //   2246: aload #36
    //   2248: aload #37
    //   2250: aload #11
    //   2252: invokevirtual getMargin : ()I
    //   2255: ineg
    //   2256: iload #12
    //   2258: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   2261: pop
    //   2262: iload_3
    //   2263: ifeq -> 2301
    //   2266: iconst_0
    //   2267: istore #12
    //   2269: aload #6
    //   2271: aload #39
    //   2273: if_acmpne -> 2283
    //   2276: aload #10
    //   2278: invokevirtual getMargin : ()I
    //   2281: istore #12
    //   2283: aload #39
    //   2285: aload #6
    //   2287: if_acmpeq -> 2301
    //   2290: aload_1
    //   2291: aload #38
    //   2293: aload #6
    //   2295: iload #12
    //   2297: iconst_5
    //   2298: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   2301: iload_3
    //   2302: ifeq -> 2376
    //   2305: iload #28
    //   2307: ifeq -> 2376
    //   2310: iload #14
    //   2312: ifne -> 2369
    //   2315: iload #24
    //   2317: ifne -> 2369
    //   2320: iload #28
    //   2322: ifeq -> 2352
    //   2325: iload #29
    //   2327: iconst_3
    //   2328: if_icmpne -> 2349
    //   2331: aload_1
    //   2332: aload #36
    //   2334: aload #38
    //   2336: iconst_0
    //   2337: bipush #8
    //   2339: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   2342: iload #23
    //   2344: istore #12
    //   2346: goto -> 2386
    //   2349: goto -> 2352
    //   2352: aload_1
    //   2353: aload #36
    //   2355: aload #38
    //   2357: iconst_0
    //   2358: iconst_5
    //   2359: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   2362: iload #23
    //   2364: istore #12
    //   2366: goto -> 2386
    //   2369: iload #23
    //   2371: istore #12
    //   2373: goto -> 2386
    //   2376: iload #23
    //   2378: istore #12
    //   2380: goto -> 2386
    //   2383: iconst_5
    //   2384: istore #12
    //   2386: iload_3
    //   2387: ifeq -> 2487
    //   2390: iload #5
    //   2392: ifeq -> 2487
    //   2395: iconst_0
    //   2396: istore #13
    //   2398: aload #11
    //   2400: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2403: ifnull -> 2413
    //   2406: aload #11
    //   2408: invokevirtual getMargin : ()I
    //   2411: istore #13
    //   2413: aload #37
    //   2415: aload #7
    //   2417: if_acmpeq -> 2487
    //   2420: aload_0
    //   2421: getfield OPTIMIZE_WRAP : Z
    //   2424: ifeq -> 2472
    //   2427: aload #36
    //   2429: getfield isFinalValue : Z
    //   2432: ifeq -> 2472
    //   2435: aload_0
    //   2436: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   2439: astore #6
    //   2441: aload #6
    //   2443: ifnull -> 2472
    //   2446: aload #6
    //   2448: checkcast androidx/constraintlayout/core/widgets/ConstraintWidgetContainer
    //   2451: astore_1
    //   2452: iload_2
    //   2453: ifeq -> 2465
    //   2456: aload_1
    //   2457: aload #11
    //   2459: invokevirtual addHorizontalWrapMaxVariable : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;)V
    //   2462: goto -> 2471
    //   2465: aload_1
    //   2466: aload #11
    //   2468: invokevirtual addVerticalWrapMaxVariable : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;)V
    //   2471: return
    //   2472: aload_1
    //   2473: aload #7
    //   2475: aload #36
    //   2477: iload #13
    //   2479: iload #12
    //   2481: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   2484: goto -> 2487
    //   2487: return
    //   2488: iload #30
    //   2490: iconst_2
    //   2491: if_icmpge -> 2639
    //   2494: iload_3
    //   2495: ifeq -> 2639
    //   2498: iload #5
    //   2500: ifeq -> 2639
    //   2503: aload_1
    //   2504: aload #38
    //   2506: aload #6
    //   2508: iconst_0
    //   2509: bipush #8
    //   2511: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   2514: iload_2
    //   2515: ifne -> 2537
    //   2518: aload_0
    //   2519: getfield mBaseline : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2522: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2525: ifnonnull -> 2531
    //   2528: goto -> 2537
    //   2531: iconst_0
    //   2532: istore #12
    //   2534: goto -> 2540
    //   2537: iconst_1
    //   2538: istore #12
    //   2540: iload #12
    //   2542: istore #13
    //   2544: iload #13
    //   2546: istore #12
    //   2548: iload_2
    //   2549: ifne -> 2623
    //   2552: iload #13
    //   2554: istore #12
    //   2556: aload_0
    //   2557: getfield mBaseline : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2560: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2563: ifnull -> 2623
    //   2566: aload_0
    //   2567: getfield mBaseline : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2570: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2573: getfield mOwner : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   2576: astore #6
    //   2578: aload #6
    //   2580: getfield mDimensionRatio : F
    //   2583: fconst_0
    //   2584: fcmpl
    //   2585: ifeq -> 2620
    //   2588: aload #6
    //   2590: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   2593: iconst_0
    //   2594: aaload
    //   2595: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   2598: if_acmpne -> 2620
    //   2601: aload #6
    //   2603: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   2606: iconst_1
    //   2607: aaload
    //   2608: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   2611: if_acmpne -> 2620
    //   2614: iconst_1
    //   2615: istore #12
    //   2617: goto -> 2623
    //   2620: iconst_0
    //   2621: istore #12
    //   2623: iload #12
    //   2625: ifeq -> 2639
    //   2628: aload_1
    //   2629: aload #7
    //   2631: aload #36
    //   2633: iconst_0
    //   2634: bipush #8
    //   2636: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   2639: return
  }
  
  private boolean isChainHead(int paramInt) {
    paramInt *= 2;
    if ((this.mListAnchors[paramInt]).mTarget != null) {
      ConstraintAnchor constraintAnchor = (this.mListAnchors[paramInt]).mTarget.mTarget;
      ConstraintAnchor[] arrayOfConstraintAnchor = this.mListAnchors;
      if (constraintAnchor != arrayOfConstraintAnchor[paramInt] && (arrayOfConstraintAnchor[paramInt + 1]).mTarget != null && (this.mListAnchors[paramInt + 1]).mTarget.mTarget == this.mListAnchors[paramInt + 1])
        return true; 
    } 
    return false;
  }
  
  private void serializeAnchor(StringBuilder paramStringBuilder, String paramString, ConstraintAnchor paramConstraintAnchor) {
    if (paramConstraintAnchor.mTarget == null)
      return; 
    paramStringBuilder.append(paramString);
    paramStringBuilder.append(" : [ '");
    paramStringBuilder.append(paramConstraintAnchor.mTarget);
    paramStringBuilder.append("',");
    paramStringBuilder.append(paramConstraintAnchor.mMargin);
    paramStringBuilder.append(",");
    paramStringBuilder.append(paramConstraintAnchor.mGoneMargin);
    paramStringBuilder.append(",");
    paramStringBuilder.append(" ] ,\n");
  }
  
  private void serializeAttribute(StringBuilder paramStringBuilder, String paramString, float paramFloat1, float paramFloat2) {
    if (paramFloat1 == paramFloat2)
      return; 
    paramStringBuilder.append(paramString);
    paramStringBuilder.append(" :   ");
    paramStringBuilder.append(paramFloat2);
    paramStringBuilder.append(",\n");
  }
  
  private void serializeCircle(StringBuilder paramStringBuilder, ConstraintAnchor paramConstraintAnchor, float paramFloat) {
    if (paramConstraintAnchor.mTarget == null)
      return; 
    paramStringBuilder.append("circle : [ '");
    paramStringBuilder.append(paramConstraintAnchor.mTarget);
    paramStringBuilder.append("',");
    paramStringBuilder.append(paramConstraintAnchor.mMargin);
    paramStringBuilder.append(",");
    paramStringBuilder.append(paramFloat);
    paramStringBuilder.append(",");
    paramStringBuilder.append(" ] ,\n");
  }
  
  private void serializeDimensionRatio(StringBuilder paramStringBuilder, String paramString, float paramFloat, int paramInt) {
    if (paramFloat == 0.0F)
      return; 
    paramStringBuilder.append(paramString);
    paramStringBuilder.append(" :  [");
    paramStringBuilder.append(paramFloat);
    paramStringBuilder.append(",");
    paramStringBuilder.append(paramInt);
    paramStringBuilder.append("");
    paramStringBuilder.append("],\n");
  }
  
  private void serializeSize(StringBuilder paramStringBuilder, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, float paramFloat1, float paramFloat2) {
    paramStringBuilder.append(paramString);
    paramStringBuilder.append(" :  {\n");
    serializeAttribute(paramStringBuilder, "size", paramInt1, -2.14748365E9F);
    serializeAttribute(paramStringBuilder, "min", paramInt2, 0.0F);
    serializeAttribute(paramStringBuilder, "max", paramInt3, 2.14748365E9F);
    serializeAttribute(paramStringBuilder, "matchMin", paramInt5, 0.0F);
    serializeAttribute(paramStringBuilder, "matchDef", paramInt6, 0.0F);
    serializeAttribute(paramStringBuilder, "matchPercent", paramInt6, 1.0F);
    paramStringBuilder.append("},\n");
  }
  
  public void addChildrenToSolverByDependency(ConstraintWidgetContainer paramConstraintWidgetContainer, LinearSystem paramLinearSystem, HashSet<ConstraintWidget> paramHashSet, int paramInt, boolean paramBoolean) {
    if (paramBoolean) {
      if (!paramHashSet.contains(this))
        return; 
      Optimizer.checkMatchParent(paramConstraintWidgetContainer, paramLinearSystem, this);
      paramHashSet.remove(this);
      addToSolver(paramLinearSystem, paramConstraintWidgetContainer.optimizeFor(64));
    } 
    if (paramInt == 0) {
      HashSet<ConstraintAnchor> hashSet = this.mLeft.getDependents();
      if (hashSet != null) {
        Iterator<ConstraintAnchor> iterator = hashSet.iterator();
        while (iterator.hasNext())
          ((ConstraintAnchor)iterator.next()).mOwner.addChildrenToSolverByDependency(paramConstraintWidgetContainer, paramLinearSystem, paramHashSet, paramInt, true); 
      } 
      hashSet = this.mRight.getDependents();
      if (hashSet != null) {
        Iterator<ConstraintAnchor> iterator = hashSet.iterator();
        while (iterator.hasNext())
          ((ConstraintAnchor)iterator.next()).mOwner.addChildrenToSolverByDependency(paramConstraintWidgetContainer, paramLinearSystem, paramHashSet, paramInt, true); 
      } 
    } else {
      HashSet<ConstraintAnchor> hashSet = this.mTop.getDependents();
      if (hashSet != null) {
        Iterator<ConstraintAnchor> iterator = hashSet.iterator();
        while (iterator.hasNext())
          ((ConstraintAnchor)iterator.next()).mOwner.addChildrenToSolverByDependency(paramConstraintWidgetContainer, paramLinearSystem, paramHashSet, paramInt, true); 
      } 
      hashSet = this.mBottom.getDependents();
      if (hashSet != null) {
        Iterator<ConstraintAnchor> iterator = hashSet.iterator();
        while (iterator.hasNext())
          ((ConstraintAnchor)iterator.next()).mOwner.addChildrenToSolverByDependency(paramConstraintWidgetContainer, paramLinearSystem, paramHashSet, paramInt, true); 
      } 
      hashSet = this.mBaseline.getDependents();
      if (hashSet != null) {
        Iterator<ConstraintAnchor> iterator = hashSet.iterator();
        while (iterator.hasNext())
          ((ConstraintAnchor)iterator.next()).mOwner.addChildrenToSolverByDependency(paramConstraintWidgetContainer, paramLinearSystem, paramHashSet, paramInt, true); 
      } 
    } 
  }
  
  boolean addFirst() {
    return (this instanceof VirtualLayout || this instanceof Guideline);
  }
  
  public void addToSolver(LinearSystem paramLinearSystem, boolean paramBoolean) {
    // Byte code:
    //   0: aload_1
    //   1: aload_0
    //   2: getfield mLeft : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   5: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   8: astore #29
    //   10: aload_1
    //   11: aload_0
    //   12: getfield mRight : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   15: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   18: astore #26
    //   20: aload_1
    //   21: aload_0
    //   22: getfield mTop : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   25: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   28: astore #28
    //   30: aload_1
    //   31: aload_0
    //   32: getfield mBottom : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   35: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   38: astore #27
    //   40: aload_1
    //   41: aload_0
    //   42: getfield mBaseline : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   45: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   48: astore #30
    //   50: aload_0
    //   51: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   54: astore #24
    //   56: aload #24
    //   58: ifnull -> 176
    //   61: aload #24
    //   63: ifnull -> 85
    //   66: aload #24
    //   68: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   71: iconst_0
    //   72: aaload
    //   73: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   76: if_acmpne -> 85
    //   79: iconst_1
    //   80: istore #12
    //   82: goto -> 88
    //   85: iconst_0
    //   86: istore #12
    //   88: aload_0
    //   89: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   92: astore #24
    //   94: aload #24
    //   96: ifnull -> 118
    //   99: aload #24
    //   101: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   104: iconst_1
    //   105: aaload
    //   106: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   109: if_acmpne -> 118
    //   112: iconst_1
    //   113: istore #11
    //   115: goto -> 121
    //   118: iconst_0
    //   119: istore #11
    //   121: aload_0
    //   122: getfield mWrapBehaviorInParent : I
    //   125: tableswitch default -> 152, 1 -> 170, 2 -> 164, 3 -> 155
    //   152: goto -> 182
    //   155: iconst_0
    //   156: istore #12
    //   158: iconst_0
    //   159: istore #11
    //   161: goto -> 182
    //   164: iconst_0
    //   165: istore #12
    //   167: goto -> 182
    //   170: iconst_0
    //   171: istore #11
    //   173: goto -> 182
    //   176: iconst_0
    //   177: istore #12
    //   179: iconst_0
    //   180: istore #11
    //   182: aload_0
    //   183: getfield mVisibility : I
    //   186: bipush #8
    //   188: if_icmpne -> 219
    //   191: aload_0
    //   192: invokevirtual hasDependencies : ()Z
    //   195: ifne -> 219
    //   198: aload_0
    //   199: getfield mIsInBarrier : [Z
    //   202: astore #24
    //   204: aload #24
    //   206: iconst_0
    //   207: baload
    //   208: ifne -> 219
    //   211: aload #24
    //   213: iconst_1
    //   214: baload
    //   215: ifne -> 219
    //   218: return
    //   219: aload_0
    //   220: getfield resolvedHorizontal : Z
    //   223: istore #13
    //   225: iload #13
    //   227: ifne -> 237
    //   230: aload_0
    //   231: getfield resolvedVertical : Z
    //   234: ifeq -> 485
    //   237: iload #13
    //   239: ifeq -> 335
    //   242: aload_1
    //   243: aload #29
    //   245: aload_0
    //   246: getfield mX : I
    //   249: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;I)V
    //   252: aload_1
    //   253: aload #26
    //   255: aload_0
    //   256: getfield mX : I
    //   259: aload_0
    //   260: getfield mWidth : I
    //   263: iadd
    //   264: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;I)V
    //   267: iload #12
    //   269: ifeq -> 335
    //   272: aload_0
    //   273: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   276: astore #24
    //   278: aload #24
    //   280: ifnull -> 335
    //   283: aload_0
    //   284: getfield OPTIMIZE_WRAP_ON_RESOLVED : Z
    //   287: ifeq -> 318
    //   290: aload #24
    //   292: checkcast androidx/constraintlayout/core/widgets/ConstraintWidgetContainer
    //   295: astore #24
    //   297: aload #24
    //   299: aload_0
    //   300: getfield mLeft : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   303: invokevirtual addHorizontalWrapMinVariable : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;)V
    //   306: aload #24
    //   308: aload_0
    //   309: getfield mRight : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   312: invokevirtual addHorizontalWrapMaxVariable : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;)V
    //   315: goto -> 335
    //   318: aload_1
    //   319: aload_1
    //   320: aload #24
    //   322: getfield mRight : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   325: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   328: aload #26
    //   330: iconst_0
    //   331: iconst_5
    //   332: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   335: aload_0
    //   336: getfield resolvedVertical : Z
    //   339: ifeq -> 460
    //   342: aload_1
    //   343: aload #28
    //   345: aload_0
    //   346: getfield mY : I
    //   349: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;I)V
    //   352: aload_1
    //   353: aload #27
    //   355: aload_0
    //   356: getfield mY : I
    //   359: aload_0
    //   360: getfield mHeight : I
    //   363: iadd
    //   364: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;I)V
    //   367: aload_0
    //   368: getfield mBaseline : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   371: invokevirtual hasDependents : ()Z
    //   374: ifeq -> 392
    //   377: aload_1
    //   378: aload #30
    //   380: aload_0
    //   381: getfield mY : I
    //   384: aload_0
    //   385: getfield mBaselineDistance : I
    //   388: iadd
    //   389: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;I)V
    //   392: iload #11
    //   394: ifeq -> 460
    //   397: aload_0
    //   398: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   401: astore #24
    //   403: aload #24
    //   405: ifnull -> 460
    //   408: aload_0
    //   409: getfield OPTIMIZE_WRAP_ON_RESOLVED : Z
    //   412: ifeq -> 443
    //   415: aload #24
    //   417: checkcast androidx/constraintlayout/core/widgets/ConstraintWidgetContainer
    //   420: astore #24
    //   422: aload #24
    //   424: aload_0
    //   425: getfield mTop : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   428: invokevirtual addVerticalWrapMinVariable : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;)V
    //   431: aload #24
    //   433: aload_0
    //   434: getfield mBottom : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   437: invokevirtual addVerticalWrapMaxVariable : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;)V
    //   440: goto -> 460
    //   443: aload_1
    //   444: aload_1
    //   445: aload #24
    //   447: getfield mBottom : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   450: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   453: aload #27
    //   455: iconst_0
    //   456: iconst_5
    //   457: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   460: aload_0
    //   461: getfield resolvedHorizontal : Z
    //   464: ifeq -> 485
    //   467: aload_0
    //   468: getfield resolvedVertical : Z
    //   471: ifeq -> 485
    //   474: aload_0
    //   475: iconst_0
    //   476: putfield resolvedHorizontal : Z
    //   479: aload_0
    //   480: iconst_0
    //   481: putfield resolvedVertical : Z
    //   484: return
    //   485: getstatic androidx/constraintlayout/core/LinearSystem.sMetrics : Landroidx/constraintlayout/core/Metrics;
    //   488: ifnull -> 511
    //   491: getstatic androidx/constraintlayout/core/LinearSystem.sMetrics : Landroidx/constraintlayout/core/Metrics;
    //   494: astore #24
    //   496: aload #24
    //   498: aload #24
    //   500: getfield widgets : J
    //   503: lconst_1
    //   504: ladd
    //   505: putfield widgets : J
    //   508: goto -> 511
    //   511: iload_2
    //   512: ifeq -> 786
    //   515: aload_0
    //   516: getfield horizontalRun : Landroidx/constraintlayout/core/widgets/analyzer/HorizontalWidgetRun;
    //   519: astore #24
    //   521: aload #24
    //   523: ifnull -> 786
    //   526: aload_0
    //   527: getfield verticalRun : Landroidx/constraintlayout/core/widgets/analyzer/VerticalWidgetRun;
    //   530: ifnull -> 786
    //   533: aload #24
    //   535: getfield start : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   538: getfield resolved : Z
    //   541: ifeq -> 786
    //   544: aload_0
    //   545: getfield horizontalRun : Landroidx/constraintlayout/core/widgets/analyzer/HorizontalWidgetRun;
    //   548: getfield end : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   551: getfield resolved : Z
    //   554: ifeq -> 786
    //   557: aload_0
    //   558: getfield verticalRun : Landroidx/constraintlayout/core/widgets/analyzer/VerticalWidgetRun;
    //   561: getfield start : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   564: getfield resolved : Z
    //   567: ifeq -> 786
    //   570: aload_0
    //   571: getfield verticalRun : Landroidx/constraintlayout/core/widgets/analyzer/VerticalWidgetRun;
    //   574: getfield end : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   577: getfield resolved : Z
    //   580: ifeq -> 786
    //   583: getstatic androidx/constraintlayout/core/LinearSystem.sMetrics : Landroidx/constraintlayout/core/Metrics;
    //   586: ifnull -> 606
    //   589: getstatic androidx/constraintlayout/core/LinearSystem.sMetrics : Landroidx/constraintlayout/core/Metrics;
    //   592: astore #24
    //   594: aload #24
    //   596: aload #24
    //   598: getfield graphSolved : J
    //   601: lconst_1
    //   602: ladd
    //   603: putfield graphSolved : J
    //   606: aload_1
    //   607: aload #29
    //   609: aload_0
    //   610: getfield horizontalRun : Landroidx/constraintlayout/core/widgets/analyzer/HorizontalWidgetRun;
    //   613: getfield start : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   616: getfield value : I
    //   619: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;I)V
    //   622: aload_1
    //   623: aload #26
    //   625: aload_0
    //   626: getfield horizontalRun : Landroidx/constraintlayout/core/widgets/analyzer/HorizontalWidgetRun;
    //   629: getfield end : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   632: getfield value : I
    //   635: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;I)V
    //   638: aload_1
    //   639: aload #28
    //   641: aload_0
    //   642: getfield verticalRun : Landroidx/constraintlayout/core/widgets/analyzer/VerticalWidgetRun;
    //   645: getfield start : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   648: getfield value : I
    //   651: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;I)V
    //   654: aload_1
    //   655: aload #27
    //   657: aload_0
    //   658: getfield verticalRun : Landroidx/constraintlayout/core/widgets/analyzer/VerticalWidgetRun;
    //   661: getfield end : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   664: getfield value : I
    //   667: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;I)V
    //   670: aload_1
    //   671: aload #30
    //   673: aload_0
    //   674: getfield verticalRun : Landroidx/constraintlayout/core/widgets/analyzer/VerticalWidgetRun;
    //   677: getfield baseline : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   680: getfield value : I
    //   683: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;I)V
    //   686: aload_0
    //   687: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   690: ifnull -> 775
    //   693: iload #12
    //   695: ifeq -> 734
    //   698: aload_0
    //   699: getfield isTerminalWidget : [Z
    //   702: iconst_0
    //   703: baload
    //   704: ifeq -> 734
    //   707: aload_0
    //   708: invokevirtual isInHorizontalChain : ()Z
    //   711: ifne -> 734
    //   714: aload_1
    //   715: aload_1
    //   716: aload_0
    //   717: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   720: getfield mRight : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   723: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   726: aload #26
    //   728: iconst_0
    //   729: bipush #8
    //   731: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   734: iload #11
    //   736: ifeq -> 775
    //   739: aload_0
    //   740: getfield isTerminalWidget : [Z
    //   743: iconst_1
    //   744: baload
    //   745: ifeq -> 775
    //   748: aload_0
    //   749: invokevirtual isInVerticalChain : ()Z
    //   752: ifne -> 775
    //   755: aload_1
    //   756: aload_1
    //   757: aload_0
    //   758: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   761: getfield mBottom : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   764: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   767: aload #27
    //   769: iconst_0
    //   770: bipush #8
    //   772: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   775: aload_0
    //   776: iconst_0
    //   777: putfield resolvedHorizontal : Z
    //   780: aload_0
    //   781: iconst_0
    //   782: putfield resolvedVertical : Z
    //   785: return
    //   786: getstatic androidx/constraintlayout/core/LinearSystem.sMetrics : Landroidx/constraintlayout/core/Metrics;
    //   789: ifnull -> 809
    //   792: getstatic androidx/constraintlayout/core/LinearSystem.sMetrics : Landroidx/constraintlayout/core/Metrics;
    //   795: astore #24
    //   797: aload #24
    //   799: aload #24
    //   801: getfield linearSolved : J
    //   804: lconst_1
    //   805: ladd
    //   806: putfield linearSolved : J
    //   809: aload_0
    //   810: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   813: ifnull -> 1014
    //   816: aload_0
    //   817: iconst_0
    //   818: invokespecial isChainHead : (I)Z
    //   821: ifeq -> 842
    //   824: aload_0
    //   825: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   828: checkcast androidx/constraintlayout/core/widgets/ConstraintWidgetContainer
    //   831: aload_0
    //   832: iconst_0
    //   833: invokevirtual addChain : (Landroidx/constraintlayout/core/widgets/ConstraintWidget;I)V
    //   836: iconst_1
    //   837: istore #13
    //   839: goto -> 848
    //   842: aload_0
    //   843: invokevirtual isInHorizontalChain : ()Z
    //   846: istore #13
    //   848: aload_0
    //   849: iconst_1
    //   850: invokespecial isChainHead : (I)Z
    //   853: ifeq -> 874
    //   856: aload_0
    //   857: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   860: checkcast androidx/constraintlayout/core/widgets/ConstraintWidgetContainer
    //   863: aload_0
    //   864: iconst_1
    //   865: invokevirtual addChain : (Landroidx/constraintlayout/core/widgets/ConstraintWidget;I)V
    //   868: iconst_1
    //   869: istore #14
    //   871: goto -> 880
    //   874: aload_0
    //   875: invokevirtual isInVerticalChain : ()Z
    //   878: istore #14
    //   880: iload #13
    //   882: ifne -> 938
    //   885: iload #12
    //   887: ifeq -> 938
    //   890: aload_0
    //   891: getfield mVisibility : I
    //   894: bipush #8
    //   896: if_icmpeq -> 938
    //   899: aload_0
    //   900: getfield mLeft : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   903: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   906: ifnonnull -> 938
    //   909: aload_0
    //   910: getfield mRight : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   913: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   916: ifnonnull -> 938
    //   919: aload_1
    //   920: aload_1
    //   921: aload_0
    //   922: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   925: getfield mRight : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   928: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   931: aload #26
    //   933: iconst_0
    //   934: iconst_1
    //   935: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   938: iload #14
    //   940: ifne -> 1003
    //   943: iload #11
    //   945: ifeq -> 1003
    //   948: aload_0
    //   949: getfield mVisibility : I
    //   952: bipush #8
    //   954: if_icmpeq -> 1003
    //   957: aload_0
    //   958: getfield mTop : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   961: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   964: ifnonnull -> 1003
    //   967: aload_0
    //   968: getfield mBottom : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   971: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   974: ifnonnull -> 1003
    //   977: aload_0
    //   978: getfield mBaseline : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   981: ifnonnull -> 1003
    //   984: aload_1
    //   985: aload_1
    //   986: aload_0
    //   987: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   990: getfield mBottom : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   993: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   996: aload #27
    //   998: iconst_0
    //   999: iconst_1
    //   1000: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   1003: iload #13
    //   1005: istore #15
    //   1007: iload #14
    //   1009: istore #16
    //   1011: goto -> 1020
    //   1014: iconst_0
    //   1015: istore #15
    //   1017: iconst_0
    //   1018: istore #16
    //   1020: aload_0
    //   1021: getfield mWidth : I
    //   1024: istore #4
    //   1026: iload #4
    //   1028: istore #6
    //   1030: iload #4
    //   1032: aload_0
    //   1033: getfield mMinWidth : I
    //   1036: if_icmpge -> 1045
    //   1039: aload_0
    //   1040: getfield mMinWidth : I
    //   1043: istore #6
    //   1045: aload_0
    //   1046: getfield mHeight : I
    //   1049: istore #4
    //   1051: iload #4
    //   1053: istore #7
    //   1055: iload #4
    //   1057: aload_0
    //   1058: getfield mMinHeight : I
    //   1061: if_icmpge -> 1070
    //   1064: aload_0
    //   1065: getfield mMinHeight : I
    //   1068: istore #7
    //   1070: aload_0
    //   1071: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1074: iconst_0
    //   1075: aaload
    //   1076: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1079: if_acmpeq -> 1088
    //   1082: iconst_1
    //   1083: istore #13
    //   1085: goto -> 1091
    //   1088: iconst_0
    //   1089: istore #13
    //   1091: aload_0
    //   1092: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1095: iconst_1
    //   1096: aaload
    //   1097: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1100: if_acmpeq -> 1109
    //   1103: iconst_1
    //   1104: istore #14
    //   1106: goto -> 1112
    //   1109: iconst_0
    //   1110: istore #14
    //   1112: iconst_0
    //   1113: istore #17
    //   1115: aload_0
    //   1116: aload_0
    //   1117: getfield mDimensionRatioSide : I
    //   1120: putfield mResolvedDimensionRatioSide : I
    //   1123: aload_0
    //   1124: getfield mDimensionRatio : F
    //   1127: fstore_3
    //   1128: aload_0
    //   1129: fload_3
    //   1130: putfield mResolvedDimensionRatio : F
    //   1133: aload_0
    //   1134: getfield mMatchConstraintDefaultWidth : I
    //   1137: istore #8
    //   1139: aload_0
    //   1140: getfield mMatchConstraintDefaultHeight : I
    //   1143: istore #9
    //   1145: fload_3
    //   1146: fconst_0
    //   1147: fcmpl
    //   1148: ifle -> 1542
    //   1151: aload_0
    //   1152: getfield mVisibility : I
    //   1155: bipush #8
    //   1157: if_icmpeq -> 1542
    //   1160: iconst_1
    //   1161: istore #17
    //   1163: iload #8
    //   1165: istore #4
    //   1167: aload_0
    //   1168: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1171: iconst_0
    //   1172: aaload
    //   1173: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1176: if_acmpne -> 1191
    //   1179: iload #8
    //   1181: istore #4
    //   1183: iload #8
    //   1185: ifne -> 1191
    //   1188: iconst_3
    //   1189: istore #4
    //   1191: iload #9
    //   1193: istore #5
    //   1195: aload_0
    //   1196: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1199: iconst_1
    //   1200: aaload
    //   1201: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1204: if_acmpne -> 1219
    //   1207: iload #9
    //   1209: istore #5
    //   1211: iload #9
    //   1213: ifne -> 1219
    //   1216: iconst_3
    //   1217: istore #5
    //   1219: aload_0
    //   1220: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1223: iconst_0
    //   1224: aaload
    //   1225: astore #25
    //   1227: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1230: astore #24
    //   1232: aload #25
    //   1234: aload #24
    //   1236: if_acmpne -> 1290
    //   1239: aload_0
    //   1240: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1243: iconst_1
    //   1244: aaload
    //   1245: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1248: if_acmpne -> 1290
    //   1251: iload #4
    //   1253: iconst_3
    //   1254: if_icmpne -> 1290
    //   1257: iload #5
    //   1259: iconst_3
    //   1260: if_icmpne -> 1290
    //   1263: aload_0
    //   1264: iload #12
    //   1266: iload #11
    //   1268: iload #13
    //   1270: iload #14
    //   1272: invokevirtual setupDimensionRatio : (ZZZZ)V
    //   1275: iload #5
    //   1277: istore #9
    //   1279: iload #17
    //   1281: istore #13
    //   1283: iload #4
    //   1285: istore #8
    //   1287: goto -> 1546
    //   1290: aload_0
    //   1291: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1294: iconst_0
    //   1295: aaload
    //   1296: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1299: if_acmpne -> 1393
    //   1302: iload #4
    //   1304: iconst_3
    //   1305: if_icmpne -> 1393
    //   1308: aload_0
    //   1309: iconst_0
    //   1310: putfield mResolvedDimensionRatioSide : I
    //   1313: aload_0
    //   1314: getfield mResolvedDimensionRatio : F
    //   1317: aload_0
    //   1318: getfield mHeight : I
    //   1321: i2f
    //   1322: fmul
    //   1323: f2i
    //   1324: istore #9
    //   1326: aload_0
    //   1327: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1330: iconst_1
    //   1331: aaload
    //   1332: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1335: if_acmpeq -> 1367
    //   1338: iload #9
    //   1340: istore #4
    //   1342: iload #5
    //   1344: istore #6
    //   1346: iconst_4
    //   1347: istore #8
    //   1349: iconst_0
    //   1350: istore #13
    //   1352: iload #4
    //   1354: istore #5
    //   1356: iload #7
    //   1358: istore #4
    //   1360: iload #8
    //   1362: istore #7
    //   1364: goto -> 1562
    //   1367: iload #5
    //   1369: istore #6
    //   1371: iload #4
    //   1373: istore #8
    //   1375: iload #7
    //   1377: istore #4
    //   1379: iconst_1
    //   1380: istore #13
    //   1382: iload #9
    //   1384: istore #5
    //   1386: iload #8
    //   1388: istore #7
    //   1390: goto -> 1562
    //   1393: iload #5
    //   1395: istore #9
    //   1397: iload #17
    //   1399: istore #13
    //   1401: iload #4
    //   1403: istore #8
    //   1405: aload_0
    //   1406: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1409: iconst_1
    //   1410: aaload
    //   1411: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1414: if_acmpne -> 1546
    //   1417: iload #5
    //   1419: istore #9
    //   1421: iload #17
    //   1423: istore #13
    //   1425: iload #4
    //   1427: istore #8
    //   1429: iload #5
    //   1431: iconst_3
    //   1432: if_icmpne -> 1546
    //   1435: aload_0
    //   1436: iconst_1
    //   1437: putfield mResolvedDimensionRatioSide : I
    //   1440: aload_0
    //   1441: getfield mDimensionRatioSide : I
    //   1444: iconst_m1
    //   1445: if_icmpne -> 1458
    //   1448: aload_0
    //   1449: fconst_1
    //   1450: aload_0
    //   1451: getfield mResolvedDimensionRatio : F
    //   1454: fdiv
    //   1455: putfield mResolvedDimensionRatio : F
    //   1458: aload_0
    //   1459: getfield mResolvedDimensionRatio : F
    //   1462: aload_0
    //   1463: getfield mWidth : I
    //   1466: i2f
    //   1467: fmul
    //   1468: f2i
    //   1469: istore #7
    //   1471: aload_0
    //   1472: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1475: iconst_0
    //   1476: aaload
    //   1477: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1480: if_acmpeq -> 1512
    //   1483: iload #7
    //   1485: istore #8
    //   1487: iconst_4
    //   1488: istore #9
    //   1490: iload #4
    //   1492: istore #7
    //   1494: iload #6
    //   1496: istore #5
    //   1498: iconst_0
    //   1499: istore #13
    //   1501: iload #8
    //   1503: istore #4
    //   1505: iload #9
    //   1507: istore #6
    //   1509: goto -> 1562
    //   1512: iload #5
    //   1514: istore #9
    //   1516: iload #4
    //   1518: istore #8
    //   1520: iload #6
    //   1522: istore #5
    //   1524: iconst_1
    //   1525: istore #13
    //   1527: iload #7
    //   1529: istore #4
    //   1531: iload #9
    //   1533: istore #6
    //   1535: iload #8
    //   1537: istore #7
    //   1539: goto -> 1562
    //   1542: iload #17
    //   1544: istore #13
    //   1546: iload #6
    //   1548: istore #5
    //   1550: iload #7
    //   1552: istore #4
    //   1554: iload #8
    //   1556: istore #7
    //   1558: iload #9
    //   1560: istore #6
    //   1562: aload_0
    //   1563: getfield mResolvedMatchConstraintDefault : [I
    //   1566: astore #24
    //   1568: aload #24
    //   1570: iconst_0
    //   1571: iload #7
    //   1573: iastore
    //   1574: aload #24
    //   1576: iconst_1
    //   1577: iload #6
    //   1579: iastore
    //   1580: aload_0
    //   1581: iload #13
    //   1583: putfield mResolvedHasRatio : Z
    //   1586: iload #13
    //   1588: ifeq -> 1614
    //   1591: aload_0
    //   1592: getfield mResolvedDimensionRatioSide : I
    //   1595: istore #8
    //   1597: iload #8
    //   1599: ifeq -> 1608
    //   1602: iload #8
    //   1604: iconst_m1
    //   1605: if_icmpne -> 1614
    //   1608: iconst_1
    //   1609: istore #18
    //   1611: goto -> 1617
    //   1614: iconst_0
    //   1615: istore #18
    //   1617: iload #13
    //   1619: ifeq -> 1646
    //   1622: aload_0
    //   1623: getfield mResolvedDimensionRatioSide : I
    //   1626: istore #8
    //   1628: iload #8
    //   1630: iconst_1
    //   1631: if_icmpeq -> 1640
    //   1634: iload #8
    //   1636: iconst_m1
    //   1637: if_icmpne -> 1646
    //   1640: iconst_1
    //   1641: istore #17
    //   1643: goto -> 1649
    //   1646: iconst_0
    //   1647: istore #17
    //   1649: aload_0
    //   1650: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1653: iconst_0
    //   1654: aaload
    //   1655: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1658: if_acmpne -> 1674
    //   1661: aload_0
    //   1662: instanceof androidx/constraintlayout/core/widgets/ConstraintWidgetContainer
    //   1665: ifeq -> 1674
    //   1668: iconst_1
    //   1669: istore #19
    //   1671: goto -> 1677
    //   1674: iconst_0
    //   1675: istore #19
    //   1677: iload #19
    //   1679: ifeq -> 1688
    //   1682: iconst_0
    //   1683: istore #5
    //   1685: goto -> 1688
    //   1688: aload_0
    //   1689: getfield mCenter : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1692: invokevirtual isConnected : ()Z
    //   1695: ifeq -> 1704
    //   1698: iconst_0
    //   1699: istore #14
    //   1701: goto -> 1707
    //   1704: iconst_1
    //   1705: istore #14
    //   1707: aload_0
    //   1708: getfield mIsInBarrier : [Z
    //   1711: astore #24
    //   1713: aload #24
    //   1715: iconst_0
    //   1716: baload
    //   1717: istore #22
    //   1719: aload #24
    //   1721: iconst_1
    //   1722: baload
    //   1723: istore #21
    //   1725: aload_0
    //   1726: getfield mHorizontalResolution : I
    //   1729: iconst_2
    //   1730: if_icmpeq -> 2074
    //   1733: aload_0
    //   1734: getfield resolvedHorizontal : Z
    //   1737: ifne -> 2074
    //   1740: iload_2
    //   1741: ifeq -> 1878
    //   1744: aload_0
    //   1745: getfield horizontalRun : Landroidx/constraintlayout/core/widgets/analyzer/HorizontalWidgetRun;
    //   1748: astore #24
    //   1750: aload #24
    //   1752: ifnull -> 1878
    //   1755: aload #24
    //   1757: getfield start : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   1760: getfield resolved : Z
    //   1763: ifeq -> 1878
    //   1766: aload_0
    //   1767: getfield horizontalRun : Landroidx/constraintlayout/core/widgets/analyzer/HorizontalWidgetRun;
    //   1770: getfield end : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   1773: getfield resolved : Z
    //   1776: ifne -> 1782
    //   1779: goto -> 1878
    //   1782: iload_2
    //   1783: ifeq -> 1875
    //   1786: aload_1
    //   1787: aload #29
    //   1789: aload_0
    //   1790: getfield horizontalRun : Landroidx/constraintlayout/core/widgets/analyzer/HorizontalWidgetRun;
    //   1793: getfield start : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   1796: getfield value : I
    //   1799: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;I)V
    //   1802: aload_1
    //   1803: aload #26
    //   1805: aload_0
    //   1806: getfield horizontalRun : Landroidx/constraintlayout/core/widgets/analyzer/HorizontalWidgetRun;
    //   1809: getfield end : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   1812: getfield value : I
    //   1815: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;I)V
    //   1818: aload_0
    //   1819: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   1822: ifnull -> 1872
    //   1825: iload #12
    //   1827: ifeq -> 1869
    //   1830: aload_0
    //   1831: getfield isTerminalWidget : [Z
    //   1834: iconst_0
    //   1835: baload
    //   1836: ifeq -> 1869
    //   1839: aload_0
    //   1840: invokevirtual isInHorizontalChain : ()Z
    //   1843: ifne -> 1869
    //   1846: aload_1
    //   1847: aload_1
    //   1848: aload_0
    //   1849: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   1852: getfield mRight : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1855: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   1858: aload #26
    //   1860: iconst_0
    //   1861: bipush #8
    //   1863: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   1866: goto -> 2074
    //   1869: goto -> 2074
    //   1872: goto -> 2074
    //   1875: goto -> 2074
    //   1878: aload_0
    //   1879: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   1882: astore #24
    //   1884: aload #24
    //   1886: ifnull -> 1903
    //   1889: aload_1
    //   1890: aload #24
    //   1892: getfield mRight : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1895: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   1898: astore #24
    //   1900: goto -> 1906
    //   1903: aconst_null
    //   1904: astore #24
    //   1906: aload_0
    //   1907: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   1910: astore #25
    //   1912: aload #25
    //   1914: ifnull -> 1931
    //   1917: aload_1
    //   1918: aload #25
    //   1920: getfield mLeft : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1923: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   1926: astore #25
    //   1928: goto -> 1934
    //   1931: aconst_null
    //   1932: astore #25
    //   1934: aload_0
    //   1935: getfield isTerminalWidget : [Z
    //   1938: iconst_0
    //   1939: baload
    //   1940: istore #23
    //   1942: aload_0
    //   1943: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1946: astore #31
    //   1948: aload #31
    //   1950: iconst_0
    //   1951: aaload
    //   1952: astore #32
    //   1954: aload_0
    //   1955: getfield mLeft : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1958: astore #33
    //   1960: aload_0
    //   1961: getfield mRight : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1964: astore #34
    //   1966: aload_0
    //   1967: getfield mX : I
    //   1970: istore #10
    //   1972: aload_0
    //   1973: getfield mMinWidth : I
    //   1976: istore #8
    //   1978: aload_0
    //   1979: getfield mMaxDimension : [I
    //   1982: iconst_0
    //   1983: iaload
    //   1984: istore #9
    //   1986: aload_0
    //   1987: getfield mHorizontalBiasPercent : F
    //   1990: fstore_3
    //   1991: aload #31
    //   1993: iconst_1
    //   1994: aaload
    //   1995: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1998: if_acmpne -> 2007
    //   2001: iconst_1
    //   2002: istore #20
    //   2004: goto -> 2010
    //   2007: iconst_0
    //   2008: istore #20
    //   2010: aload_0
    //   2011: aload_1
    //   2012: iconst_1
    //   2013: iload #12
    //   2015: iload #11
    //   2017: iload #23
    //   2019: aload #25
    //   2021: aload #24
    //   2023: aload #32
    //   2025: iload #19
    //   2027: aload #33
    //   2029: aload #34
    //   2031: iload #10
    //   2033: iload #5
    //   2035: iload #8
    //   2037: iload #9
    //   2039: fload_3
    //   2040: iload #18
    //   2042: iload #20
    //   2044: iload #15
    //   2046: iload #16
    //   2048: iload #22
    //   2050: iload #7
    //   2052: iload #6
    //   2054: aload_0
    //   2055: getfield mMatchConstraintMinWidth : I
    //   2058: aload_0
    //   2059: getfield mMatchConstraintMaxWidth : I
    //   2062: aload_0
    //   2063: getfield mMatchConstraintPercentWidth : F
    //   2066: iload #14
    //   2068: invokespecial applyConstraints : (Landroidx/constraintlayout/core/LinearSystem;ZZZZLandroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;ZLandroidx/constraintlayout/core/widgets/ConstraintAnchor;Landroidx/constraintlayout/core/widgets/ConstraintAnchor;IIIIFZZZZZIIIIFZ)V
    //   2071: goto -> 2074
    //   2074: iconst_1
    //   2075: istore #5
    //   2077: iload_2
    //   2078: ifeq -> 2235
    //   2081: aload_0
    //   2082: getfield verticalRun : Landroidx/constraintlayout/core/widgets/analyzer/VerticalWidgetRun;
    //   2085: astore #24
    //   2087: aload #24
    //   2089: ifnull -> 2235
    //   2092: aload #24
    //   2094: getfield start : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   2097: getfield resolved : Z
    //   2100: ifeq -> 2235
    //   2103: aload_0
    //   2104: getfield verticalRun : Landroidx/constraintlayout/core/widgets/analyzer/VerticalWidgetRun;
    //   2107: getfield end : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   2110: getfield resolved : Z
    //   2113: ifeq -> 2235
    //   2116: aload_0
    //   2117: getfield verticalRun : Landroidx/constraintlayout/core/widgets/analyzer/VerticalWidgetRun;
    //   2120: getfield start : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   2123: getfield value : I
    //   2126: istore #5
    //   2128: aload_1
    //   2129: aload #28
    //   2131: iload #5
    //   2133: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;I)V
    //   2136: aload_0
    //   2137: getfield verticalRun : Landroidx/constraintlayout/core/widgets/analyzer/VerticalWidgetRun;
    //   2140: getfield end : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   2143: getfield value : I
    //   2146: istore #5
    //   2148: aload_1
    //   2149: aload #27
    //   2151: iload #5
    //   2153: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;I)V
    //   2156: aload_1
    //   2157: aload #30
    //   2159: aload_0
    //   2160: getfield verticalRun : Landroidx/constraintlayout/core/widgets/analyzer/VerticalWidgetRun;
    //   2163: getfield baseline : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   2166: getfield value : I
    //   2169: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;I)V
    //   2172: aload_0
    //   2173: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   2176: astore #24
    //   2178: aload #24
    //   2180: ifnull -> 2229
    //   2183: iload #16
    //   2185: ifne -> 2226
    //   2188: iload #11
    //   2190: ifeq -> 2226
    //   2193: aload_0
    //   2194: getfield isTerminalWidget : [Z
    //   2197: iconst_1
    //   2198: baload
    //   2199: ifeq -> 2223
    //   2202: aload_1
    //   2203: aload_1
    //   2204: aload #24
    //   2206: getfield mBottom : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2209: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   2212: aload #27
    //   2214: iconst_0
    //   2215: bipush #8
    //   2217: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   2220: goto -> 2229
    //   2223: goto -> 2226
    //   2226: goto -> 2229
    //   2229: iconst_0
    //   2230: istore #5
    //   2232: goto -> 2235
    //   2235: aload_0
    //   2236: getfield mVerticalResolution : I
    //   2239: iconst_2
    //   2240: if_icmpne -> 2249
    //   2243: iconst_0
    //   2244: istore #5
    //   2246: goto -> 2249
    //   2249: iload #5
    //   2251: ifeq -> 2632
    //   2254: aload_0
    //   2255: getfield resolvedVertical : Z
    //   2258: ifne -> 2632
    //   2261: aload_0
    //   2262: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   2265: iconst_1
    //   2266: aaload
    //   2267: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   2270: if_acmpne -> 2285
    //   2273: aload_0
    //   2274: instanceof androidx/constraintlayout/core/widgets/ConstraintWidgetContainer
    //   2277: ifeq -> 2285
    //   2280: iconst_1
    //   2281: istore_2
    //   2282: goto -> 2287
    //   2285: iconst_0
    //   2286: istore_2
    //   2287: iload_2
    //   2288: ifeq -> 2297
    //   2291: iconst_0
    //   2292: istore #4
    //   2294: goto -> 2297
    //   2297: aload_0
    //   2298: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   2301: astore #24
    //   2303: aload #24
    //   2305: ifnull -> 2322
    //   2308: aload_1
    //   2309: aload #24
    //   2311: getfield mBottom : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2314: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   2317: astore #24
    //   2319: goto -> 2325
    //   2322: aconst_null
    //   2323: astore #24
    //   2325: aload_0
    //   2326: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   2329: astore #25
    //   2331: aload #25
    //   2333: ifnull -> 2350
    //   2336: aload_1
    //   2337: aload #25
    //   2339: getfield mTop : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2342: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   2345: astore #25
    //   2347: goto -> 2353
    //   2350: aconst_null
    //   2351: astore #25
    //   2353: aload_0
    //   2354: getfield mBaselineDistance : I
    //   2357: ifgt -> 2369
    //   2360: aload_0
    //   2361: getfield mVisibility : I
    //   2364: bipush #8
    //   2366: if_icmpne -> 2493
    //   2369: aload_0
    //   2370: getfield mBaseline : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2373: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2376: ifnull -> 2448
    //   2379: aload_1
    //   2380: aload #30
    //   2382: aload #28
    //   2384: aload_0
    //   2385: invokevirtual getBaselineDistance : ()I
    //   2388: bipush #8
    //   2390: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   2393: pop
    //   2394: aload_1
    //   2395: aload #30
    //   2397: aload_1
    //   2398: aload_0
    //   2399: getfield mBaseline : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2402: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2405: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   2408: aload_0
    //   2409: getfield mBaseline : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2412: invokevirtual getMargin : ()I
    //   2415: bipush #8
    //   2417: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   2420: pop
    //   2421: iload #11
    //   2423: ifeq -> 2442
    //   2426: aload_1
    //   2427: aload #24
    //   2429: aload_1
    //   2430: aload_0
    //   2431: getfield mBottom : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2434: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   2437: iconst_0
    //   2438: iconst_5
    //   2439: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   2442: iconst_0
    //   2443: istore #14
    //   2445: goto -> 2493
    //   2448: aload_0
    //   2449: getfield mVisibility : I
    //   2452: bipush #8
    //   2454: if_icmpne -> 2478
    //   2457: aload_1
    //   2458: aload #30
    //   2460: aload #28
    //   2462: aload_0
    //   2463: getfield mBaseline : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2466: invokevirtual getMargin : ()I
    //   2469: bipush #8
    //   2471: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   2474: pop
    //   2475: goto -> 2493
    //   2478: aload_1
    //   2479: aload #30
    //   2481: aload #28
    //   2483: aload_0
    //   2484: invokevirtual getBaselineDistance : ()I
    //   2487: bipush #8
    //   2489: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   2492: pop
    //   2493: aload_0
    //   2494: getfield isTerminalWidget : [Z
    //   2497: iconst_1
    //   2498: baload
    //   2499: istore #19
    //   2501: aload_0
    //   2502: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   2505: astore #33
    //   2507: aload #33
    //   2509: iconst_1
    //   2510: aaload
    //   2511: astore #30
    //   2513: aload_0
    //   2514: getfield mTop : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2517: astore #31
    //   2519: aload_0
    //   2520: getfield mBottom : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2523: astore #32
    //   2525: aload_0
    //   2526: getfield mY : I
    //   2529: istore #9
    //   2531: aload_0
    //   2532: getfield mMinHeight : I
    //   2535: istore #5
    //   2537: aload_0
    //   2538: getfield mMaxDimension : [I
    //   2541: iconst_1
    //   2542: iaload
    //   2543: istore #8
    //   2545: aload_0
    //   2546: getfield mVerticalBiasPercent : F
    //   2549: fstore_3
    //   2550: aload #33
    //   2552: iconst_0
    //   2553: aaload
    //   2554: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   2557: if_acmpne -> 2566
    //   2560: iconst_1
    //   2561: istore #18
    //   2563: goto -> 2569
    //   2566: iconst_0
    //   2567: istore #18
    //   2569: aload_0
    //   2570: aload_1
    //   2571: iconst_0
    //   2572: iload #11
    //   2574: iload #12
    //   2576: iload #19
    //   2578: aload #25
    //   2580: aload #24
    //   2582: aload #30
    //   2584: iload_2
    //   2585: aload #31
    //   2587: aload #32
    //   2589: iload #9
    //   2591: iload #4
    //   2593: iload #5
    //   2595: iload #8
    //   2597: fload_3
    //   2598: iload #17
    //   2600: iload #18
    //   2602: iload #16
    //   2604: iload #15
    //   2606: iload #21
    //   2608: iload #6
    //   2610: iload #7
    //   2612: aload_0
    //   2613: getfield mMatchConstraintMinHeight : I
    //   2616: aload_0
    //   2617: getfield mMatchConstraintMaxHeight : I
    //   2620: aload_0
    //   2621: getfield mMatchConstraintPercentHeight : F
    //   2624: iload #14
    //   2626: invokespecial applyConstraints : (Landroidx/constraintlayout/core/LinearSystem;ZZZZLandroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;ZLandroidx/constraintlayout/core/widgets/ConstraintAnchor;Landroidx/constraintlayout/core/widgets/ConstraintAnchor;IIIIFZZZZZIIIIFZ)V
    //   2629: goto -> 2632
    //   2632: iload #13
    //   2634: ifeq -> 2684
    //   2637: aload_0
    //   2638: getfield mResolvedDimensionRatioSide : I
    //   2641: iconst_1
    //   2642: if_icmpne -> 2666
    //   2645: aload_1
    //   2646: aload #27
    //   2648: aload #28
    //   2650: aload #26
    //   2652: aload #29
    //   2654: aload_0
    //   2655: getfield mResolvedDimensionRatio : F
    //   2658: bipush #8
    //   2660: invokevirtual addRatio : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;FI)V
    //   2663: goto -> 2684
    //   2666: aload_1
    //   2667: aload #26
    //   2669: aload #29
    //   2671: aload #27
    //   2673: aload #28
    //   2675: aload_0
    //   2676: getfield mResolvedDimensionRatio : F
    //   2679: bipush #8
    //   2681: invokevirtual addRatio : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;FI)V
    //   2684: aload_0
    //   2685: getfield mCenter : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2688: invokevirtual isConnected : ()Z
    //   2691: ifeq -> 2729
    //   2694: aload_1
    //   2695: aload_0
    //   2696: aload_0
    //   2697: getfield mCenter : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2700: invokevirtual getTarget : ()Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2703: invokevirtual getOwner : ()Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   2706: aload_0
    //   2707: getfield mCircleConstraintAngle : F
    //   2710: ldc_w 90.0
    //   2713: fadd
    //   2714: f2d
    //   2715: invokestatic toRadians : (D)D
    //   2718: d2f
    //   2719: aload_0
    //   2720: getfield mCenter : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2723: invokevirtual getMargin : ()I
    //   2726: invokevirtual addCenterPoint : (Landroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/ConstraintWidget;FI)V
    //   2729: aload_0
    //   2730: iconst_0
    //   2731: putfield resolvedHorizontal : Z
    //   2734: aload_0
    //   2735: iconst_0
    //   2736: putfield resolvedVertical : Z
    //   2739: return
  }
  
  public boolean allowedInBarrier() {
    boolean bool;
    if (this.mVisibility != 8) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void connect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2) {
    connect(paramType1, paramConstraintWidget, paramType2, 0);
  }
  
  public void connect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2, int paramInt) {
    // Byte code:
    //   0: aload_1
    //   1: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   4: if_acmpne -> 378
    //   7: aload_3
    //   8: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   11: if_acmpne -> 267
    //   14: aload_0
    //   15: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   18: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   21: astore #7
    //   23: aload_0
    //   24: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   27: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   30: astore_3
    //   31: aload_0
    //   32: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   35: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   38: astore_1
    //   39: aload_0
    //   40: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   43: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   46: astore #8
    //   48: iconst_0
    //   49: istore #5
    //   51: iconst_0
    //   52: istore #6
    //   54: aload #7
    //   56: ifnull -> 71
    //   59: iload #5
    //   61: istore #4
    //   63: aload #7
    //   65: invokevirtual isConnected : ()Z
    //   68: ifne -> 116
    //   71: aload_3
    //   72: ifnull -> 89
    //   75: aload_3
    //   76: invokevirtual isConnected : ()Z
    //   79: ifeq -> 89
    //   82: iload #5
    //   84: istore #4
    //   86: goto -> 116
    //   89: aload_0
    //   90: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   93: aload_2
    //   94: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   97: iconst_0
    //   98: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;I)V
    //   101: aload_0
    //   102: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   105: aload_2
    //   106: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   109: iconst_0
    //   110: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;I)V
    //   113: iconst_1
    //   114: istore #4
    //   116: aload_1
    //   117: ifnull -> 131
    //   120: iload #6
    //   122: istore #5
    //   124: aload_1
    //   125: invokevirtual isConnected : ()Z
    //   128: ifne -> 178
    //   131: aload #8
    //   133: ifnull -> 151
    //   136: aload #8
    //   138: invokevirtual isConnected : ()Z
    //   141: ifeq -> 151
    //   144: iload #6
    //   146: istore #5
    //   148: goto -> 178
    //   151: aload_0
    //   152: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   155: aload_2
    //   156: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   159: iconst_0
    //   160: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;I)V
    //   163: aload_0
    //   164: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   167: aload_2
    //   168: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   171: iconst_0
    //   172: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;I)V
    //   175: iconst_1
    //   176: istore #5
    //   178: iload #4
    //   180: ifeq -> 210
    //   183: iload #5
    //   185: ifeq -> 210
    //   188: aload_0
    //   189: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   192: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   195: aload_2
    //   196: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   199: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   202: iconst_0
    //   203: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;I)Z
    //   206: pop
    //   207: goto -> 264
    //   210: iload #4
    //   212: ifeq -> 237
    //   215: aload_0
    //   216: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER_X : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   219: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   222: aload_2
    //   223: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER_X : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   226: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   229: iconst_0
    //   230: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;I)Z
    //   233: pop
    //   234: goto -> 264
    //   237: iload #5
    //   239: ifeq -> 264
    //   242: aload_0
    //   243: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER_Y : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   246: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   249: aload_2
    //   250: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER_Y : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   253: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   256: iconst_0
    //   257: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;I)Z
    //   260: pop
    //   261: goto -> 264
    //   264: goto -> 900
    //   267: aload_3
    //   268: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   271: if_acmpeq -> 338
    //   274: aload_3
    //   275: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   278: if_acmpne -> 284
    //   281: goto -> 338
    //   284: aload_3
    //   285: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   288: if_acmpeq -> 298
    //   291: aload_3
    //   292: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   295: if_acmpne -> 375
    //   298: aload_0
    //   299: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   302: aload_2
    //   303: aload_3
    //   304: iconst_0
    //   305: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;I)V
    //   308: aload_0
    //   309: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   312: aload_2
    //   313: aload_3
    //   314: iconst_0
    //   315: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;I)V
    //   318: aload_0
    //   319: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   322: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   325: aload_2
    //   326: aload_3
    //   327: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   330: iconst_0
    //   331: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;I)Z
    //   334: pop
    //   335: goto -> 900
    //   338: aload_0
    //   339: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   342: aload_2
    //   343: aload_3
    //   344: iconst_0
    //   345: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;I)V
    //   348: aload_0
    //   349: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   352: aload_2
    //   353: aload_3
    //   354: iconst_0
    //   355: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;I)V
    //   358: aload_0
    //   359: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   362: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   365: aload_2
    //   366: aload_3
    //   367: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   370: iconst_0
    //   371: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;I)Z
    //   374: pop
    //   375: goto -> 900
    //   378: aload_1
    //   379: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER_X : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   382: if_acmpne -> 451
    //   385: aload_3
    //   386: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   389: if_acmpeq -> 399
    //   392: aload_3
    //   393: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   396: if_acmpne -> 451
    //   399: aload_0
    //   400: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   403: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   406: astore_1
    //   407: aload_2
    //   408: aload_3
    //   409: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   412: astore_3
    //   413: aload_0
    //   414: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   417: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   420: astore_2
    //   421: aload_1
    //   422: aload_3
    //   423: iconst_0
    //   424: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;I)Z
    //   427: pop
    //   428: aload_2
    //   429: aload_3
    //   430: iconst_0
    //   431: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;I)Z
    //   434: pop
    //   435: aload_0
    //   436: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER_X : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   439: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   442: aload_3
    //   443: iconst_0
    //   444: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;I)Z
    //   447: pop
    //   448: goto -> 900
    //   451: aload_1
    //   452: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER_Y : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   455: if_acmpne -> 520
    //   458: aload_3
    //   459: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   462: if_acmpeq -> 472
    //   465: aload_3
    //   466: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   469: if_acmpne -> 520
    //   472: aload_2
    //   473: aload_3
    //   474: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   477: astore_1
    //   478: aload_0
    //   479: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   482: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   485: aload_1
    //   486: iconst_0
    //   487: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;I)Z
    //   490: pop
    //   491: aload_0
    //   492: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   495: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   498: aload_1
    //   499: iconst_0
    //   500: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;I)Z
    //   503: pop
    //   504: aload_0
    //   505: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER_Y : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   508: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   511: aload_1
    //   512: iconst_0
    //   513: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;I)Z
    //   516: pop
    //   517: goto -> 900
    //   520: aload_1
    //   521: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER_X : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   524: if_acmpne -> 592
    //   527: aload_3
    //   528: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER_X : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   531: if_acmpne -> 592
    //   534: aload_0
    //   535: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   538: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   541: aload_2
    //   542: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   545: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   548: iconst_0
    //   549: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;I)Z
    //   552: pop
    //   553: aload_0
    //   554: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   557: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   560: aload_2
    //   561: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   564: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   567: iconst_0
    //   568: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;I)Z
    //   571: pop
    //   572: aload_0
    //   573: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER_X : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   576: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   579: aload_2
    //   580: aload_3
    //   581: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   584: iconst_0
    //   585: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;I)Z
    //   588: pop
    //   589: goto -> 900
    //   592: aload_1
    //   593: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER_Y : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   596: if_acmpne -> 664
    //   599: aload_3
    //   600: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER_Y : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   603: if_acmpne -> 664
    //   606: aload_0
    //   607: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   610: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   613: aload_2
    //   614: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   617: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   620: iconst_0
    //   621: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;I)Z
    //   624: pop
    //   625: aload_0
    //   626: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   629: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   632: aload_2
    //   633: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   636: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   639: iconst_0
    //   640: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;I)Z
    //   643: pop
    //   644: aload_0
    //   645: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER_Y : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   648: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   651: aload_2
    //   652: aload_3
    //   653: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   656: iconst_0
    //   657: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;I)Z
    //   660: pop
    //   661: goto -> 900
    //   664: aload_0
    //   665: aload_1
    //   666: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   669: astore #7
    //   671: aload_2
    //   672: aload_3
    //   673: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   676: astore_2
    //   677: aload #7
    //   679: aload_2
    //   680: invokevirtual isValidConnection : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;)Z
    //   683: ifeq -> 900
    //   686: aload_1
    //   687: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BASELINE : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   690: if_acmpne -> 728
    //   693: aload_0
    //   694: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   697: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   700: astore_1
    //   701: aload_0
    //   702: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   705: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   708: astore_3
    //   709: aload_1
    //   710: ifnull -> 717
    //   713: aload_1
    //   714: invokevirtual reset : ()V
    //   717: aload_3
    //   718: ifnull -> 725
    //   721: aload_3
    //   722: invokevirtual reset : ()V
    //   725: goto -> 891
    //   728: aload_1
    //   729: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   732: if_acmpeq -> 820
    //   735: aload_1
    //   736: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   739: if_acmpne -> 745
    //   742: goto -> 820
    //   745: aload_1
    //   746: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   749: if_acmpeq -> 765
    //   752: aload_1
    //   753: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   756: if_acmpne -> 762
    //   759: goto -> 765
    //   762: goto -> 891
    //   765: aload_0
    //   766: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   769: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   772: astore_3
    //   773: aload_3
    //   774: invokevirtual getTarget : ()Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   777: aload_2
    //   778: if_acmpeq -> 785
    //   781: aload_3
    //   782: invokevirtual reset : ()V
    //   785: aload_0
    //   786: aload_1
    //   787: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   790: invokevirtual getOpposite : ()Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   793: astore_1
    //   794: aload_0
    //   795: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER_X : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   798: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   801: astore_3
    //   802: aload_3
    //   803: invokevirtual isConnected : ()Z
    //   806: ifeq -> 891
    //   809: aload_1
    //   810: invokevirtual reset : ()V
    //   813: aload_3
    //   814: invokevirtual reset : ()V
    //   817: goto -> 891
    //   820: aload_0
    //   821: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BASELINE : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   824: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   827: astore_3
    //   828: aload_3
    //   829: ifnull -> 836
    //   832: aload_3
    //   833: invokevirtual reset : ()V
    //   836: aload_0
    //   837: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   840: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   843: astore_3
    //   844: aload_3
    //   845: invokevirtual getTarget : ()Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   848: aload_2
    //   849: if_acmpeq -> 856
    //   852: aload_3
    //   853: invokevirtual reset : ()V
    //   856: aload_0
    //   857: aload_1
    //   858: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   861: invokevirtual getOpposite : ()Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   864: astore_1
    //   865: aload_0
    //   866: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER_Y : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   869: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   872: astore_3
    //   873: aload_3
    //   874: invokevirtual isConnected : ()Z
    //   877: ifeq -> 762
    //   880: aload_1
    //   881: invokevirtual reset : ()V
    //   884: aload_3
    //   885: invokevirtual reset : ()V
    //   888: goto -> 762
    //   891: aload #7
    //   893: aload_2
    //   894: iload #4
    //   896: invokevirtual connect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor;I)Z
    //   899: pop
    //   900: return
  }
  
  public void connect(ConstraintAnchor paramConstraintAnchor1, ConstraintAnchor paramConstraintAnchor2, int paramInt) {
    if (paramConstraintAnchor1.getOwner() == this)
      connect(paramConstraintAnchor1.getType(), paramConstraintAnchor2.getOwner(), paramConstraintAnchor2.getType(), paramInt); 
  }
  
  public void connectCircularConstraint(ConstraintWidget paramConstraintWidget, float paramFloat, int paramInt) {
    immediateConnect(ConstraintAnchor.Type.CENTER, paramConstraintWidget, ConstraintAnchor.Type.CENTER, paramInt, 0);
    this.mCircleConstraintAngle = paramFloat;
  }
  
  public void copy(ConstraintWidget paramConstraintWidget, HashMap<ConstraintWidget, ConstraintWidget> paramHashMap) {
    int[] arrayOfInt1;
    ConstraintWidget constraintWidget1;
    this.mHorizontalResolution = paramConstraintWidget.mHorizontalResolution;
    this.mVerticalResolution = paramConstraintWidget.mVerticalResolution;
    this.mMatchConstraintDefaultWidth = paramConstraintWidget.mMatchConstraintDefaultWidth;
    this.mMatchConstraintDefaultHeight = paramConstraintWidget.mMatchConstraintDefaultHeight;
    int[] arrayOfInt3 = this.mResolvedMatchConstraintDefault;
    int[] arrayOfInt2 = paramConstraintWidget.mResolvedMatchConstraintDefault;
    arrayOfInt3[0] = arrayOfInt2[0];
    arrayOfInt3[1] = arrayOfInt2[1];
    this.mMatchConstraintMinWidth = paramConstraintWidget.mMatchConstraintMinWidth;
    this.mMatchConstraintMaxWidth = paramConstraintWidget.mMatchConstraintMaxWidth;
    this.mMatchConstraintMinHeight = paramConstraintWidget.mMatchConstraintMinHeight;
    this.mMatchConstraintMaxHeight = paramConstraintWidget.mMatchConstraintMaxHeight;
    this.mMatchConstraintPercentHeight = paramConstraintWidget.mMatchConstraintPercentHeight;
    this.mIsWidthWrapContent = paramConstraintWidget.mIsWidthWrapContent;
    this.mIsHeightWrapContent = paramConstraintWidget.mIsHeightWrapContent;
    this.mResolvedDimensionRatioSide = paramConstraintWidget.mResolvedDimensionRatioSide;
    this.mResolvedDimensionRatio = paramConstraintWidget.mResolvedDimensionRatio;
    arrayOfInt2 = paramConstraintWidget.mMaxDimension;
    this.mMaxDimension = Arrays.copyOf(arrayOfInt2, arrayOfInt2.length);
    this.mCircleConstraintAngle = paramConstraintWidget.mCircleConstraintAngle;
    this.hasBaseline = paramConstraintWidget.hasBaseline;
    this.inPlaceholder = paramConstraintWidget.inPlaceholder;
    this.mLeft.reset();
    this.mTop.reset();
    this.mRight.reset();
    this.mBottom.reset();
    this.mBaseline.reset();
    this.mCenterX.reset();
    this.mCenterY.reset();
    this.mCenter.reset();
    this.mListDimensionBehaviors = Arrays.<DimensionBehaviour>copyOf(this.mListDimensionBehaviors, 2);
    ConstraintWidget constraintWidget3 = this.mParent;
    arrayOfInt3 = null;
    if (constraintWidget3 == null) {
      constraintWidget3 = null;
    } else {
      constraintWidget3 = paramHashMap.get(paramConstraintWidget.mParent);
    } 
    this.mParent = constraintWidget3;
    this.mWidth = paramConstraintWidget.mWidth;
    this.mHeight = paramConstraintWidget.mHeight;
    this.mDimensionRatio = paramConstraintWidget.mDimensionRatio;
    this.mDimensionRatioSide = paramConstraintWidget.mDimensionRatioSide;
    this.mX = paramConstraintWidget.mX;
    this.mY = paramConstraintWidget.mY;
    this.mRelX = paramConstraintWidget.mRelX;
    this.mRelY = paramConstraintWidget.mRelY;
    this.mOffsetX = paramConstraintWidget.mOffsetX;
    this.mOffsetY = paramConstraintWidget.mOffsetY;
    this.mBaselineDistance = paramConstraintWidget.mBaselineDistance;
    this.mMinWidth = paramConstraintWidget.mMinWidth;
    this.mMinHeight = paramConstraintWidget.mMinHeight;
    this.mHorizontalBiasPercent = paramConstraintWidget.mHorizontalBiasPercent;
    this.mVerticalBiasPercent = paramConstraintWidget.mVerticalBiasPercent;
    this.mCompanionWidget = paramConstraintWidget.mCompanionWidget;
    this.mContainerItemSkip = paramConstraintWidget.mContainerItemSkip;
    this.mVisibility = paramConstraintWidget.mVisibility;
    this.mDebugName = paramConstraintWidget.mDebugName;
    this.mType = paramConstraintWidget.mType;
    this.mDistToTop = paramConstraintWidget.mDistToTop;
    this.mDistToLeft = paramConstraintWidget.mDistToLeft;
    this.mDistToRight = paramConstraintWidget.mDistToRight;
    this.mDistToBottom = paramConstraintWidget.mDistToBottom;
    this.mLeftHasCentered = paramConstraintWidget.mLeftHasCentered;
    this.mRightHasCentered = paramConstraintWidget.mRightHasCentered;
    this.mTopHasCentered = paramConstraintWidget.mTopHasCentered;
    this.mBottomHasCentered = paramConstraintWidget.mBottomHasCentered;
    this.mHorizontalWrapVisited = paramConstraintWidget.mHorizontalWrapVisited;
    this.mVerticalWrapVisited = paramConstraintWidget.mVerticalWrapVisited;
    this.mHorizontalChainStyle = paramConstraintWidget.mHorizontalChainStyle;
    this.mVerticalChainStyle = paramConstraintWidget.mVerticalChainStyle;
    this.mHorizontalChainFixedPosition = paramConstraintWidget.mHorizontalChainFixedPosition;
    this.mVerticalChainFixedPosition = paramConstraintWidget.mVerticalChainFixedPosition;
    float[] arrayOfFloat1 = this.mWeight;
    float[] arrayOfFloat2 = paramConstraintWidget.mWeight;
    arrayOfFloat1[0] = arrayOfFloat2[0];
    arrayOfFloat1[1] = arrayOfFloat2[1];
    ConstraintWidget[] arrayOfConstraintWidget2 = this.mListNextMatchConstraintsWidget;
    ConstraintWidget[] arrayOfConstraintWidget1 = paramConstraintWidget.mListNextMatchConstraintsWidget;
    arrayOfConstraintWidget2[0] = arrayOfConstraintWidget1[0];
    arrayOfConstraintWidget2[1] = arrayOfConstraintWidget1[1];
    arrayOfConstraintWidget1 = this.mNextChainWidget;
    arrayOfConstraintWidget2 = paramConstraintWidget.mNextChainWidget;
    arrayOfConstraintWidget1[0] = arrayOfConstraintWidget2[0];
    arrayOfConstraintWidget1[1] = arrayOfConstraintWidget2[1];
    ConstraintWidget constraintWidget2 = paramConstraintWidget.mHorizontalNextWidget;
    if (constraintWidget2 == null) {
      constraintWidget2 = null;
    } else {
      constraintWidget2 = paramHashMap.get(constraintWidget2);
    } 
    this.mHorizontalNextWidget = constraintWidget2;
    paramConstraintWidget = paramConstraintWidget.mVerticalNextWidget;
    if (paramConstraintWidget == null) {
      arrayOfInt1 = arrayOfInt3;
    } else {
      constraintWidget1 = paramHashMap.get(arrayOfInt1);
    } 
    this.mVerticalNextWidget = constraintWidget1;
  }
  
  public void createObjectVariables(LinearSystem paramLinearSystem) {
    paramLinearSystem.createObjectVariable(this.mLeft);
    paramLinearSystem.createObjectVariable(this.mTop);
    paramLinearSystem.createObjectVariable(this.mRight);
    paramLinearSystem.createObjectVariable(this.mBottom);
    if (this.mBaselineDistance > 0)
      paramLinearSystem.createObjectVariable(this.mBaseline); 
  }
  
  public void ensureMeasureRequested() {
    this.mMeasureRequested = true;
  }
  
  public void ensureWidgetRuns() {
    if (this.horizontalRun == null)
      this.horizontalRun = new HorizontalWidgetRun(this); 
    if (this.verticalRun == null)
      this.verticalRun = new VerticalWidgetRun(this); 
  }
  
  public ConstraintAnchor getAnchor(ConstraintAnchor.Type paramType) {
    switch (paramType) {
      default:
        throw new AssertionError(paramType.name());
      case null:
        return null;
      case null:
        return this.mCenterY;
      case null:
        return this.mCenterX;
      case null:
        return this.mCenter;
      case null:
        return this.mBaseline;
      case null:
        return this.mBottom;
      case null:
        return this.mRight;
      case null:
        return this.mTop;
      case null:
        break;
    } 
    return this.mLeft;
  }
  
  public ArrayList<ConstraintAnchor> getAnchors() {
    return this.mAnchors;
  }
  
  public int getBaselineDistance() {
    return this.mBaselineDistance;
  }
  
  public float getBiasPercent(int paramInt) {
    return (paramInt == 0) ? this.mHorizontalBiasPercent : ((paramInt == 1) ? this.mVerticalBiasPercent : -1.0F);
  }
  
  public int getBottom() {
    return getY() + this.mHeight;
  }
  
  public Object getCompanionWidget() {
    return this.mCompanionWidget;
  }
  
  public int getContainerItemSkip() {
    return this.mContainerItemSkip;
  }
  
  public String getDebugName() {
    return this.mDebugName;
  }
  
  public DimensionBehaviour getDimensionBehaviour(int paramInt) {
    return (paramInt == 0) ? getHorizontalDimensionBehaviour() : ((paramInt == 1) ? getVerticalDimensionBehaviour() : null);
  }
  
  public float getDimensionRatio() {
    return this.mDimensionRatio;
  }
  
  public int getDimensionRatioSide() {
    return this.mDimensionRatioSide;
  }
  
  public boolean getHasBaseline() {
    return this.hasBaseline;
  }
  
  public int getHeight() {
    return (this.mVisibility == 8) ? 0 : this.mHeight;
  }
  
  public float getHorizontalBiasPercent() {
    return this.mHorizontalBiasPercent;
  }
  
  public ConstraintWidget getHorizontalChainControlWidget() {
    ConstraintWidget constraintWidget;
    ConstraintAnchor constraintAnchor2 = null;
    ConstraintAnchor constraintAnchor1 = null;
    if (isInHorizontalChain()) {
      ConstraintWidget constraintWidget1 = this;
      while (true) {
        constraintAnchor2 = constraintAnchor1;
        if (constraintAnchor1 == null) {
          constraintAnchor2 = constraintAnchor1;
          if (constraintWidget1 != null) {
            constraintAnchor2 = constraintWidget1.getAnchor(ConstraintAnchor.Type.LEFT);
            ConstraintAnchor constraintAnchor = null;
            if (constraintAnchor2 == null) {
              constraintAnchor2 = null;
            } else {
              constraintAnchor2 = constraintAnchor2.getTarget();
            } 
            if (constraintAnchor2 == null) {
              constraintAnchor2 = null;
            } else {
              constraintWidget = constraintAnchor2.getOwner();
            } 
            if (constraintWidget == getParent()) {
              constraintWidget = constraintWidget1;
              break;
            } 
            if (constraintWidget != null)
              constraintAnchor = constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).getTarget(); 
            if (constraintAnchor != null && constraintAnchor.getOwner() != constraintWidget1) {
              ConstraintWidget constraintWidget2 = constraintWidget1;
              continue;
            } 
            constraintWidget1 = constraintWidget;
            continue;
          } 
        } 
        break;
      } 
    } 
    return constraintWidget;
  }
  
  public int getHorizontalChainStyle() {
    return this.mHorizontalChainStyle;
  }
  
  public DimensionBehaviour getHorizontalDimensionBehaviour() {
    return this.mListDimensionBehaviors[0];
  }
  
  public int getHorizontalMargin() {
    int i = 0;
    ConstraintAnchor constraintAnchor = this.mLeft;
    if (constraintAnchor != null)
      i = 0 + constraintAnchor.mMargin; 
    constraintAnchor = this.mRight;
    int j = i;
    if (constraintAnchor != null)
      j = i + constraintAnchor.mMargin; 
    return j;
  }
  
  public int getLastHorizontalMeasureSpec() {
    return this.mLastHorizontalMeasureSpec;
  }
  
  public int getLastVerticalMeasureSpec() {
    return this.mLastVerticalMeasureSpec;
  }
  
  public int getLeft() {
    return getX();
  }
  
  public int getLength(int paramInt) {
    return (paramInt == 0) ? getWidth() : ((paramInt == 1) ? getHeight() : 0);
  }
  
  public int getMaxHeight() {
    return this.mMaxDimension[1];
  }
  
  public int getMaxWidth() {
    return this.mMaxDimension[0];
  }
  
  public int getMinHeight() {
    return this.mMinHeight;
  }
  
  public int getMinWidth() {
    return this.mMinWidth;
  }
  
  public ConstraintWidget getNextChainMember(int paramInt) {
    if (paramInt == 0) {
      if (this.mRight.mTarget != null) {
        ConstraintAnchor constraintAnchor2 = this.mRight.mTarget.mTarget;
        ConstraintAnchor constraintAnchor1 = this.mRight;
        if (constraintAnchor2 == constraintAnchor1)
          return constraintAnchor1.mTarget.mOwner; 
      } 
    } else if (paramInt == 1 && this.mBottom.mTarget != null) {
      ConstraintAnchor constraintAnchor2 = this.mBottom.mTarget.mTarget;
      ConstraintAnchor constraintAnchor1 = this.mBottom;
      if (constraintAnchor2 == constraintAnchor1)
        return constraintAnchor1.mTarget.mOwner; 
    } 
    return null;
  }
  
  public int getOptimizerWrapHeight() {
    int i = this.mHeight;
    int j = i;
    if (this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT) {
      if (this.mMatchConstraintDefaultHeight == 1) {
        i = Math.max(this.mMatchConstraintMinHeight, i);
      } else if (this.mMatchConstraintMinHeight > 0) {
        i = this.mMatchConstraintMinHeight;
        this.mHeight = i;
      } else {
        i = 0;
      } 
      int k = this.mMatchConstraintMaxHeight;
      j = i;
      if (k > 0) {
        j = i;
        if (k < i)
          j = this.mMatchConstraintMaxHeight; 
      } 
    } 
    return j;
  }
  
  public int getOptimizerWrapWidth() {
    int i = this.mWidth;
    int j = i;
    if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT) {
      if (this.mMatchConstraintDefaultWidth == 1) {
        i = Math.max(this.mMatchConstraintMinWidth, i);
      } else if (this.mMatchConstraintMinWidth > 0) {
        i = this.mMatchConstraintMinWidth;
        this.mWidth = i;
      } else {
        i = 0;
      } 
      int k = this.mMatchConstraintMaxWidth;
      j = i;
      if (k > 0) {
        j = i;
        if (k < i)
          j = this.mMatchConstraintMaxWidth; 
      } 
    } 
    return j;
  }
  
  public ConstraintWidget getParent() {
    return this.mParent;
  }
  
  public ConstraintWidget getPreviousChainMember(int paramInt) {
    if (paramInt == 0) {
      if (this.mLeft.mTarget != null) {
        ConstraintAnchor constraintAnchor1 = this.mLeft.mTarget.mTarget;
        ConstraintAnchor constraintAnchor2 = this.mLeft;
        if (constraintAnchor1 == constraintAnchor2)
          return constraintAnchor2.mTarget.mOwner; 
      } 
    } else if (paramInt == 1 && this.mTop.mTarget != null) {
      ConstraintAnchor constraintAnchor1 = this.mTop.mTarget.mTarget;
      ConstraintAnchor constraintAnchor2 = this.mTop;
      if (constraintAnchor1 == constraintAnchor2)
        return constraintAnchor2.mTarget.mOwner; 
    } 
    return null;
  }
  
  int getRelativePositioning(int paramInt) {
    return (paramInt == 0) ? this.mRelX : ((paramInt == 1) ? this.mRelY : 0);
  }
  
  public int getRight() {
    return getX() + this.mWidth;
  }
  
  protected int getRootX() {
    return this.mX + this.mOffsetX;
  }
  
  protected int getRootY() {
    return this.mY + this.mOffsetY;
  }
  
  public WidgetRun getRun(int paramInt) {
    return (WidgetRun)((paramInt == 0) ? this.horizontalRun : ((paramInt == 1) ? this.verticalRun : null));
  }
  
  public int getTop() {
    return getY();
  }
  
  public String getType() {
    return this.mType;
  }
  
  public float getVerticalBiasPercent() {
    return this.mVerticalBiasPercent;
  }
  
  public ConstraintWidget getVerticalChainControlWidget() {
    ConstraintWidget constraintWidget;
    ConstraintAnchor constraintAnchor2 = null;
    ConstraintAnchor constraintAnchor1 = null;
    if (isInVerticalChain()) {
      ConstraintWidget constraintWidget1 = this;
      while (true) {
        constraintAnchor2 = constraintAnchor1;
        if (constraintAnchor1 == null) {
          constraintAnchor2 = constraintAnchor1;
          if (constraintWidget1 != null) {
            constraintAnchor2 = constraintWidget1.getAnchor(ConstraintAnchor.Type.TOP);
            ConstraintAnchor constraintAnchor = null;
            if (constraintAnchor2 == null) {
              constraintAnchor2 = null;
            } else {
              constraintAnchor2 = constraintAnchor2.getTarget();
            } 
            if (constraintAnchor2 == null) {
              constraintAnchor2 = null;
            } else {
              constraintWidget = constraintAnchor2.getOwner();
            } 
            if (constraintWidget == getParent()) {
              constraintWidget = constraintWidget1;
              break;
            } 
            if (constraintWidget != null)
              constraintAnchor = constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).getTarget(); 
            if (constraintAnchor != null && constraintAnchor.getOwner() != constraintWidget1) {
              ConstraintWidget constraintWidget2 = constraintWidget1;
              continue;
            } 
            constraintWidget1 = constraintWidget;
            continue;
          } 
        } 
        break;
      } 
    } 
    return constraintWidget;
  }
  
  public int getVerticalChainStyle() {
    return this.mVerticalChainStyle;
  }
  
  public DimensionBehaviour getVerticalDimensionBehaviour() {
    return this.mListDimensionBehaviors[1];
  }
  
  public int getVerticalMargin() {
    int i = 0;
    if (this.mLeft != null)
      i = 0 + this.mTop.mMargin; 
    int j = i;
    if (this.mRight != null)
      j = i + this.mBottom.mMargin; 
    return j;
  }
  
  public int getVisibility() {
    return this.mVisibility;
  }
  
  public int getWidth() {
    return (this.mVisibility == 8) ? 0 : this.mWidth;
  }
  
  public int getWrapBehaviorInParent() {
    return this.mWrapBehaviorInParent;
  }
  
  public int getX() {
    ConstraintWidget constraintWidget = this.mParent;
    return (constraintWidget != null && constraintWidget instanceof ConstraintWidgetContainer) ? (((ConstraintWidgetContainer)constraintWidget).mPaddingLeft + this.mX) : this.mX;
  }
  
  public int getY() {
    ConstraintWidget constraintWidget = this.mParent;
    return (constraintWidget != null && constraintWidget instanceof ConstraintWidgetContainer) ? (((ConstraintWidgetContainer)constraintWidget).mPaddingTop + this.mY) : this.mY;
  }
  
  public boolean hasBaseline() {
    return this.hasBaseline;
  }
  
  public boolean hasDanglingDimension(int paramInt) {
    byte b1;
    byte b2;
    boolean bool2 = true;
    boolean bool1 = true;
    if (paramInt == 0) {
      if (this.mLeft.mTarget != null) {
        paramInt = 1;
      } else {
        paramInt = 0;
      } 
      if (this.mRight.mTarget != null) {
        b1 = 1;
      } else {
        b1 = 0;
      } 
      if (paramInt + b1 >= 2)
        bool1 = false; 
      return bool1;
    } 
    if (this.mTop.mTarget != null) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    if (this.mBottom.mTarget != null) {
      b1 = 1;
    } else {
      b1 = 0;
    } 
    if (this.mBaseline.mTarget != null) {
      b2 = 1;
    } else {
      b2 = 0;
    } 
    if (paramInt + b1 + b2 < 2) {
      bool1 = bool2;
    } else {
      bool1 = false;
    } 
    return bool1;
  }
  
  public boolean hasDependencies() {
    byte b = 0;
    int i = this.mAnchors.size();
    while (b < i) {
      if (((ConstraintAnchor)this.mAnchors.get(b)).hasDependents())
        return true; 
      b++;
    } 
    return false;
  }
  
  public boolean hasDimensionOverride() {
    return (this.mWidthOverride != -1 || this.mHeightOverride != -1);
  }
  
  public boolean hasResolvedTargets(int paramInt1, int paramInt2) {
    boolean bool1 = true;
    boolean bool2 = true;
    if (paramInt1 == 0) {
      if (this.mLeft.mTarget != null && this.mLeft.mTarget.hasFinalValue() && this.mRight.mTarget != null && this.mRight.mTarget.hasFinalValue()) {
        if (this.mRight.mTarget.getFinalValue() - this.mRight.getMargin() - this.mLeft.mTarget.getFinalValue() + this.mLeft.getMargin() >= paramInt2) {
          bool1 = bool2;
        } else {
          bool1 = false;
        } 
        return bool1;
      } 
    } else if (this.mTop.mTarget != null && this.mTop.mTarget.hasFinalValue() && this.mBottom.mTarget != null && this.mBottom.mTarget.hasFinalValue()) {
      if (this.mBottom.mTarget.getFinalValue() - this.mBottom.getMargin() - this.mTop.mTarget.getFinalValue() + this.mTop.getMargin() < paramInt2)
        bool1 = false; 
      return bool1;
    } 
    return false;
  }
  
  public void immediateConnect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2, int paramInt1, int paramInt2) {
    getAnchor(paramType1).connect(paramConstraintWidget.getAnchor(paramType2), paramInt1, paramInt2, true);
  }
  
  public boolean isHeightWrapContent() {
    return this.mIsHeightWrapContent;
  }
  
  public boolean isHorizontalSolvingPassDone() {
    return this.horizontalSolvingPass;
  }
  
  public boolean isInBarrier(int paramInt) {
    return this.mIsInBarrier[paramInt];
  }
  
  public boolean isInHorizontalChain() {
    return ((this.mLeft.mTarget != null && this.mLeft.mTarget.mTarget == this.mLeft) || (this.mRight.mTarget != null && this.mRight.mTarget.mTarget == this.mRight));
  }
  
  public boolean isInPlaceholder() {
    return this.inPlaceholder;
  }
  
  public boolean isInVerticalChain() {
    return ((this.mTop.mTarget != null && this.mTop.mTarget.mTarget == this.mTop) || (this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == this.mBottom));
  }
  
  public boolean isInVirtualLayout() {
    return this.mInVirtualLayout;
  }
  
  public boolean isMeasureRequested() {
    boolean bool;
    if (this.mMeasureRequested && this.mVisibility != 8) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isResolvedHorizontally() {
    return (this.resolvedHorizontal || (this.mLeft.hasFinalValue() && this.mRight.hasFinalValue()));
  }
  
  public boolean isResolvedVertically() {
    return (this.resolvedVertical || (this.mTop.hasFinalValue() && this.mBottom.hasFinalValue()));
  }
  
  public boolean isRoot() {
    boolean bool;
    if (this.mParent == null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isSpreadHeight() {
    int i = this.mMatchConstraintDefaultHeight;
    boolean bool = true;
    if (i != 0 || this.mDimensionRatio != 0.0F || this.mMatchConstraintMinHeight != 0 || this.mMatchConstraintMaxHeight != 0 || this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT)
      bool = false; 
    return bool;
  }
  
  public boolean isSpreadWidth() {
    int i = this.mMatchConstraintDefaultWidth;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (i == 0) {
      bool1 = bool2;
      if (this.mDimensionRatio == 0.0F) {
        bool1 = bool2;
        if (this.mMatchConstraintMinWidth == 0) {
          bool1 = bool2;
          if (this.mMatchConstraintMaxWidth == 0) {
            bool1 = bool2;
            if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT)
              bool1 = true; 
          } 
        } 
      } 
    } 
    return bool1;
  }
  
  public boolean isVerticalSolvingPassDone() {
    return this.verticalSolvingPass;
  }
  
  public boolean isWidthWrapContent() {
    return this.mIsWidthWrapContent;
  }
  
  public void markHorizontalSolvingPassDone() {
    this.horizontalSolvingPass = true;
  }
  
  public void markVerticalSolvingPassDone() {
    this.verticalSolvingPass = true;
  }
  
  public boolean oppositeDimensionDependsOn(int paramInt) {
    boolean bool1;
    boolean bool2 = true;
    if (paramInt == 0) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    DimensionBehaviour[] arrayOfDimensionBehaviour = this.mListDimensionBehaviors;
    DimensionBehaviour dimensionBehaviour1 = arrayOfDimensionBehaviour[paramInt];
    DimensionBehaviour dimensionBehaviour2 = arrayOfDimensionBehaviour[bool1];
    if (dimensionBehaviour1 != DimensionBehaviour.MATCH_CONSTRAINT || dimensionBehaviour2 != DimensionBehaviour.MATCH_CONSTRAINT)
      bool2 = false; 
    return bool2;
  }
  
  public boolean oppositeDimensionsTied() {
    DimensionBehaviour[] arrayOfDimensionBehaviour = this.mListDimensionBehaviors;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (arrayOfDimensionBehaviour[0] == DimensionBehaviour.MATCH_CONSTRAINT) {
      bool1 = bool2;
      if (this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT)
        bool1 = true; 
    } 
    return bool1;
  }
  
  public void reset() {
    this.mLeft.reset();
    this.mTop.reset();
    this.mRight.reset();
    this.mBottom.reset();
    this.mBaseline.reset();
    this.mCenterX.reset();
    this.mCenterY.reset();
    this.mCenter.reset();
    this.mParent = null;
    this.mCircleConstraintAngle = 0.0F;
    this.mWidth = 0;
    this.mHeight = 0;
    this.mDimensionRatio = 0.0F;
    this.mDimensionRatioSide = -1;
    this.mX = 0;
    this.mY = 0;
    this.mOffsetX = 0;
    this.mOffsetY = 0;
    this.mBaselineDistance = 0;
    this.mMinWidth = 0;
    this.mMinHeight = 0;
    float f = DEFAULT_BIAS;
    this.mHorizontalBiasPercent = f;
    this.mVerticalBiasPercent = f;
    this.mListDimensionBehaviors[0] = DimensionBehaviour.FIXED;
    this.mListDimensionBehaviors[1] = DimensionBehaviour.FIXED;
    this.mCompanionWidget = null;
    this.mContainerItemSkip = 0;
    this.mVisibility = 0;
    this.mType = null;
    this.mHorizontalWrapVisited = false;
    this.mVerticalWrapVisited = false;
    this.mHorizontalChainStyle = 0;
    this.mVerticalChainStyle = 0;
    this.mHorizontalChainFixedPosition = false;
    this.mVerticalChainFixedPosition = false;
    float[] arrayOfFloat = this.mWeight;
    arrayOfFloat[0] = -1.0F;
    arrayOfFloat[1] = -1.0F;
    this.mHorizontalResolution = -1;
    this.mVerticalResolution = -1;
    int[] arrayOfInt2 = this.mMaxDimension;
    arrayOfInt2[0] = Integer.MAX_VALUE;
    arrayOfInt2[1] = Integer.MAX_VALUE;
    this.mMatchConstraintDefaultWidth = 0;
    this.mMatchConstraintDefaultHeight = 0;
    this.mMatchConstraintPercentWidth = 1.0F;
    this.mMatchConstraintPercentHeight = 1.0F;
    this.mMatchConstraintMaxWidth = Integer.MAX_VALUE;
    this.mMatchConstraintMaxHeight = Integer.MAX_VALUE;
    this.mMatchConstraintMinWidth = 0;
    this.mMatchConstraintMinHeight = 0;
    this.mResolvedHasRatio = false;
    this.mResolvedDimensionRatioSide = -1;
    this.mResolvedDimensionRatio = 1.0F;
    this.mGroupsToSolver = false;
    boolean[] arrayOfBoolean = this.isTerminalWidget;
    arrayOfBoolean[0] = true;
    arrayOfBoolean[1] = true;
    this.mInVirtualLayout = false;
    arrayOfBoolean = this.mIsInBarrier;
    arrayOfBoolean[0] = false;
    arrayOfBoolean[1] = false;
    this.mMeasureRequested = true;
    int[] arrayOfInt1 = this.mResolvedMatchConstraintDefault;
    arrayOfInt1[0] = 0;
    arrayOfInt1[1] = 0;
    this.mWidthOverride = -1;
    this.mHeightOverride = -1;
  }
  
  public void resetAllConstraints() {
    resetAnchors();
    setVerticalBiasPercent(DEFAULT_BIAS);
    setHorizontalBiasPercent(DEFAULT_BIAS);
  }
  
  public void resetAnchor(ConstraintAnchor paramConstraintAnchor) {
    if (getParent() != null && getParent() instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)getParent()).handlesInternalConstraints())
      return; 
    ConstraintAnchor constraintAnchor3 = getAnchor(ConstraintAnchor.Type.LEFT);
    ConstraintAnchor constraintAnchor6 = getAnchor(ConstraintAnchor.Type.RIGHT);
    ConstraintAnchor constraintAnchor1 = getAnchor(ConstraintAnchor.Type.TOP);
    ConstraintAnchor constraintAnchor2 = getAnchor(ConstraintAnchor.Type.BOTTOM);
    ConstraintAnchor constraintAnchor7 = getAnchor(ConstraintAnchor.Type.CENTER);
    ConstraintAnchor constraintAnchor4 = getAnchor(ConstraintAnchor.Type.CENTER_X);
    ConstraintAnchor constraintAnchor5 = getAnchor(ConstraintAnchor.Type.CENTER_Y);
    if (paramConstraintAnchor == constraintAnchor7) {
      if (constraintAnchor3.isConnected() && constraintAnchor6.isConnected() && constraintAnchor3.getTarget() == constraintAnchor6.getTarget()) {
        constraintAnchor3.reset();
        constraintAnchor6.reset();
      } 
      if (constraintAnchor1.isConnected() && constraintAnchor2.isConnected() && constraintAnchor1.getTarget() == constraintAnchor2.getTarget()) {
        constraintAnchor1.reset();
        constraintAnchor2.reset();
      } 
      this.mHorizontalBiasPercent = 0.5F;
      this.mVerticalBiasPercent = 0.5F;
    } else if (paramConstraintAnchor == constraintAnchor4) {
      if (constraintAnchor3.isConnected() && constraintAnchor6.isConnected() && constraintAnchor3.getTarget().getOwner() == constraintAnchor6.getTarget().getOwner()) {
        constraintAnchor3.reset();
        constraintAnchor6.reset();
      } 
      this.mHorizontalBiasPercent = 0.5F;
    } else if (paramConstraintAnchor == constraintAnchor5) {
      if (constraintAnchor1.isConnected() && constraintAnchor2.isConnected() && constraintAnchor1.getTarget().getOwner() == constraintAnchor2.getTarget().getOwner()) {
        constraintAnchor1.reset();
        constraintAnchor2.reset();
      } 
      this.mVerticalBiasPercent = 0.5F;
    } else if (paramConstraintAnchor == constraintAnchor3 || paramConstraintAnchor == constraintAnchor6) {
      if (constraintAnchor3.isConnected() && constraintAnchor3.getTarget() == constraintAnchor6.getTarget())
        constraintAnchor7.reset(); 
    } else if ((paramConstraintAnchor == constraintAnchor1 || paramConstraintAnchor == constraintAnchor2) && constraintAnchor1.isConnected() && constraintAnchor1.getTarget() == constraintAnchor2.getTarget()) {
      constraintAnchor7.reset();
    } 
    paramConstraintAnchor.reset();
  }
  
  public void resetAnchors() {
    ConstraintWidget constraintWidget = getParent();
    if (constraintWidget != null && constraintWidget instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)getParent()).handlesInternalConstraints())
      return; 
    byte b = 0;
    int i = this.mAnchors.size();
    while (b < i) {
      ((ConstraintAnchor)this.mAnchors.get(b)).reset();
      b++;
    } 
  }
  
  public void resetFinalResolution() {
    this.resolvedHorizontal = false;
    this.resolvedVertical = false;
    this.horizontalSolvingPass = false;
    this.verticalSolvingPass = false;
    byte b = 0;
    int i = this.mAnchors.size();
    while (b < i) {
      ((ConstraintAnchor)this.mAnchors.get(b)).resetFinalResolution();
      b++;
    } 
  }
  
  public void resetSolverVariables(Cache paramCache) {
    this.mLeft.resetSolverVariable(paramCache);
    this.mTop.resetSolverVariable(paramCache);
    this.mRight.resetSolverVariable(paramCache);
    this.mBottom.resetSolverVariable(paramCache);
    this.mBaseline.resetSolverVariable(paramCache);
    this.mCenter.resetSolverVariable(paramCache);
    this.mCenterX.resetSolverVariable(paramCache);
    this.mCenterY.resetSolverVariable(paramCache);
  }
  
  public void resetSolvingPassFlag() {
    this.horizontalSolvingPass = false;
    this.verticalSolvingPass = false;
  }
  
  public StringBuilder serialize(StringBuilder paramStringBuilder) {
    paramStringBuilder.append("{\n");
    serializeAnchor(paramStringBuilder, "left", this.mLeft);
    serializeAnchor(paramStringBuilder, "top", this.mTop);
    serializeAnchor(paramStringBuilder, "right", this.mRight);
    serializeAnchor(paramStringBuilder, "bottom", this.mBottom);
    serializeAnchor(paramStringBuilder, "baseline", this.mBaseline);
    serializeAnchor(paramStringBuilder, "centerX", this.mCenterX);
    serializeAnchor(paramStringBuilder, "centerY", this.mCenterY);
    serializeCircle(paramStringBuilder, this.mCenter, this.mCircleConstraintAngle);
    serializeSize(paramStringBuilder, "width", this.mWidth, this.mMinWidth, this.mMaxDimension[0], this.mWidthOverride, this.mMatchConstraintMinWidth, this.mMatchConstraintDefaultWidth, this.mMatchConstraintPercentWidth, this.mWeight[0]);
    serializeSize(paramStringBuilder, "height", this.mHeight, this.mMinHeight, this.mMaxDimension[1], this.mHeightOverride, this.mMatchConstraintMinHeight, this.mMatchConstraintDefaultHeight, this.mMatchConstraintPercentHeight, this.mWeight[1]);
    serializeDimensionRatio(paramStringBuilder, "dimensionRatio", this.mDimensionRatio, this.mDimensionRatioSide);
    serializeAttribute(paramStringBuilder, "horizontalBias", this.mHorizontalBiasPercent, DEFAULT_BIAS);
    serializeAttribute(paramStringBuilder, "verticalBias", this.mVerticalBiasPercent, DEFAULT_BIAS);
    paramStringBuilder.append("}\n");
    return paramStringBuilder;
  }
  
  public void setBaselineDistance(int paramInt) {
    boolean bool;
    this.mBaselineDistance = paramInt;
    if (paramInt > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    this.hasBaseline = bool;
  }
  
  public void setCompanionWidget(Object paramObject) {
    this.mCompanionWidget = paramObject;
  }
  
  public void setContainerItemSkip(int paramInt) {
    if (paramInt >= 0) {
      this.mContainerItemSkip = paramInt;
    } else {
      this.mContainerItemSkip = 0;
    } 
  }
  
  public void setDebugName(String paramString) {
    this.mDebugName = paramString;
  }
  
  public void setDebugSolverName(LinearSystem paramLinearSystem, String paramString) {
    this.mDebugName = paramString;
    SolverVariable solverVariable2 = paramLinearSystem.createObjectVariable(this.mLeft);
    SolverVariable solverVariable1 = paramLinearSystem.createObjectVariable(this.mTop);
    SolverVariable solverVariable3 = paramLinearSystem.createObjectVariable(this.mRight);
    SolverVariable solverVariable4 = paramLinearSystem.createObjectVariable(this.mBottom);
    solverVariable2.setName(paramString + ".left");
    solverVariable1.setName(paramString + ".top");
    solverVariable3.setName(paramString + ".right");
    solverVariable4.setName(paramString + ".bottom");
    paramLinearSystem.createObjectVariable(this.mBaseline).setName(paramString + ".baseline");
  }
  
  public void setDimension(int paramInt1, int paramInt2) {
    this.mWidth = paramInt1;
    int i = this.mMinWidth;
    if (paramInt1 < i)
      this.mWidth = i; 
    this.mHeight = paramInt2;
    paramInt1 = this.mMinHeight;
    if (paramInt2 < paramInt1)
      this.mHeight = paramInt1; 
  }
  
  public void setDimensionRatio(float paramFloat, int paramInt) {
    this.mDimensionRatio = paramFloat;
    this.mDimensionRatioSide = paramInt;
  }
  
  public void setDimensionRatio(String paramString) {
    float f1;
    boolean bool;
    if (paramString == null || paramString.length() == 0) {
      this.mDimensionRatio = 0.0F;
      return;
    } 
    int i = -1;
    float f2 = 0.0F;
    float f4 = 0.0F;
    float f3 = 0.0F;
    int k = paramString.length();
    int j = paramString.indexOf(',');
    if (j > 0 && j < k - 1) {
      String str = paramString.substring(0, j);
      if (str.equalsIgnoreCase("W")) {
        bool = false;
      } else {
        bool = i;
        if (str.equalsIgnoreCase("H"))
          bool = true; 
      } 
      j++;
      i = bool;
      bool = j;
    } else {
      bool = false;
    } 
    j = paramString.indexOf(':');
    if (j >= 0 && j < k - 1) {
      String str = paramString.substring(bool, j);
      paramString = paramString.substring(j + 1);
      f1 = f2;
      if (str.length() > 0) {
        f1 = f2;
        if (paramString.length() > 0)
          try {
            f4 = Float.parseFloat(str);
            float f = Float.parseFloat(paramString);
            f1 = f3;
            if (f4 > 0.0F) {
              f1 = f3;
              if (f > 0.0F)
                if (i == 1) {
                  f1 = Math.abs(f / f4);
                } else {
                  f1 = Math.abs(f4 / f);
                }  
            } 
          } catch (NumberFormatException numberFormatException) {
            f1 = f2;
          }  
      } 
    } else {
      String str = numberFormatException.substring(bool);
      f1 = f4;
      if (str.length() > 0)
        try {
          f1 = Float.parseFloat(str);
        } catch (NumberFormatException numberFormatException1) {
          f1 = f4;
        }  
    } 
    if (f1 > 0.0F) {
      this.mDimensionRatio = f1;
      this.mDimensionRatioSide = i;
    } 
  }
  
  public void setFinalBaseline(int paramInt) {
    if (!this.hasBaseline)
      return; 
    int i = paramInt - this.mBaselineDistance;
    int j = this.mHeight;
    this.mY = i;
    this.mTop.setFinalValue(i);
    this.mBottom.setFinalValue(j + i);
    this.mBaseline.setFinalValue(paramInt);
    this.resolvedVertical = true;
  }
  
  public void setFinalFrame(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
    setFrame(paramInt1, paramInt2, paramInt3, paramInt4);
    setBaselineDistance(paramInt5);
    if (paramInt6 == 0) {
      this.resolvedHorizontal = true;
      this.resolvedVertical = false;
    } else if (paramInt6 == 1) {
      this.resolvedHorizontal = false;
      this.resolvedVertical = true;
    } else if (paramInt6 == 2) {
      this.resolvedHorizontal = true;
      this.resolvedVertical = true;
    } else {
      this.resolvedHorizontal = false;
      this.resolvedVertical = false;
    } 
  }
  
  public void setFinalHorizontal(int paramInt1, int paramInt2) {
    if (this.resolvedHorizontal)
      return; 
    this.mLeft.setFinalValue(paramInt1);
    this.mRight.setFinalValue(paramInt2);
    this.mX = paramInt1;
    this.mWidth = paramInt2 - paramInt1;
    this.resolvedHorizontal = true;
  }
  
  public void setFinalLeft(int paramInt) {
    this.mLeft.setFinalValue(paramInt);
    this.mX = paramInt;
  }
  
  public void setFinalTop(int paramInt) {
    this.mTop.setFinalValue(paramInt);
    this.mY = paramInt;
  }
  
  public void setFinalVertical(int paramInt1, int paramInt2) {
    if (this.resolvedVertical)
      return; 
    this.mTop.setFinalValue(paramInt1);
    this.mBottom.setFinalValue(paramInt2);
    this.mY = paramInt1;
    this.mHeight = paramInt2 - paramInt1;
    if (this.hasBaseline)
      this.mBaseline.setFinalValue(this.mBaselineDistance + paramInt1); 
    this.resolvedVertical = true;
  }
  
  public void setFrame(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt3 == 0) {
      setHorizontalDimension(paramInt1, paramInt2);
    } else if (paramInt3 == 1) {
      setVerticalDimension(paramInt1, paramInt2);
    } 
  }
  
  public void setFrame(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = paramInt3 - paramInt1;
    paramInt3 = paramInt4 - paramInt2;
    this.mX = paramInt1;
    this.mY = paramInt2;
    if (this.mVisibility == 8) {
      this.mWidth = 0;
      this.mHeight = 0;
      return;
    } 
    paramInt1 = i;
    if (this.mListDimensionBehaviors[0] == DimensionBehaviour.FIXED) {
      paramInt1 = i;
      if (i < this.mWidth)
        paramInt1 = this.mWidth; 
    } 
    paramInt2 = paramInt3;
    if (this.mListDimensionBehaviors[1] == DimensionBehaviour.FIXED) {
      paramInt2 = paramInt3;
      if (paramInt3 < this.mHeight)
        paramInt2 = this.mHeight; 
    } 
    this.mWidth = paramInt1;
    this.mHeight = paramInt2;
    paramInt3 = this.mMinHeight;
    if (paramInt2 < paramInt3)
      this.mHeight = paramInt3; 
    paramInt3 = this.mMinWidth;
    if (paramInt1 < paramInt3)
      this.mWidth = paramInt3; 
    if (this.mMatchConstraintMaxWidth > 0 && this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT)
      this.mWidth = Math.min(this.mWidth, this.mMatchConstraintMaxWidth); 
    if (this.mMatchConstraintMaxHeight > 0 && this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT)
      this.mHeight = Math.min(this.mHeight, this.mMatchConstraintMaxHeight); 
    paramInt3 = this.mWidth;
    if (paramInt1 != paramInt3)
      this.mWidthOverride = paramInt3; 
    paramInt1 = this.mHeight;
    if (paramInt2 != paramInt1)
      this.mHeightOverride = paramInt1; 
  }
  
  public void setGoneMargin(ConstraintAnchor.Type paramType, int paramInt) {
    switch (paramType) {
      default:
        return;
      case null:
        this.mBaseline.mGoneMargin = paramInt;
      case null:
        this.mBottom.mGoneMargin = paramInt;
      case null:
        this.mRight.mGoneMargin = paramInt;
      case null:
        this.mTop.mGoneMargin = paramInt;
      case null:
        break;
    } 
    this.mLeft.mGoneMargin = paramInt;
  }
  
  public void setHasBaseline(boolean paramBoolean) {
    this.hasBaseline = paramBoolean;
  }
  
  public void setHeight(int paramInt) {
    this.mHeight = paramInt;
    int i = this.mMinHeight;
    if (paramInt < i)
      this.mHeight = i; 
  }
  
  public void setHeightWrapContent(boolean paramBoolean) {
    this.mIsHeightWrapContent = paramBoolean;
  }
  
  public void setHorizontalBiasPercent(float paramFloat) {
    this.mHorizontalBiasPercent = paramFloat;
  }
  
  public void setHorizontalChainStyle(int paramInt) {
    this.mHorizontalChainStyle = paramInt;
  }
  
  public void setHorizontalDimension(int paramInt1, int paramInt2) {
    this.mX = paramInt1;
    paramInt1 = paramInt2 - paramInt1;
    this.mWidth = paramInt1;
    paramInt2 = this.mMinWidth;
    if (paramInt1 < paramInt2)
      this.mWidth = paramInt2; 
  }
  
  public void setHorizontalDimensionBehaviour(DimensionBehaviour paramDimensionBehaviour) {
    this.mListDimensionBehaviors[0] = paramDimensionBehaviour;
  }
  
  public void setHorizontalMatchStyle(int paramInt1, int paramInt2, int paramInt3, float paramFloat) {
    this.mMatchConstraintDefaultWidth = paramInt1;
    this.mMatchConstraintMinWidth = paramInt2;
    if (paramInt3 == Integer.MAX_VALUE) {
      paramInt2 = 0;
    } else {
      paramInt2 = paramInt3;
    } 
    this.mMatchConstraintMaxWidth = paramInt2;
    this.mMatchConstraintPercentWidth = paramFloat;
    if (paramFloat > 0.0F && paramFloat < 1.0F && paramInt1 == 0)
      this.mMatchConstraintDefaultWidth = 2; 
  }
  
  public void setHorizontalWeight(float paramFloat) {
    this.mWeight[0] = paramFloat;
  }
  
  protected void setInBarrier(int paramInt, boolean paramBoolean) {
    this.mIsInBarrier[paramInt] = paramBoolean;
  }
  
  public void setInPlaceholder(boolean paramBoolean) {
    this.inPlaceholder = paramBoolean;
  }
  
  public void setInVirtualLayout(boolean paramBoolean) {
    this.mInVirtualLayout = paramBoolean;
  }
  
  public void setLastMeasureSpec(int paramInt1, int paramInt2) {
    this.mLastHorizontalMeasureSpec = paramInt1;
    this.mLastVerticalMeasureSpec = paramInt2;
    setMeasureRequested(false);
  }
  
  public void setLength(int paramInt1, int paramInt2) {
    if (paramInt2 == 0) {
      setWidth(paramInt1);
    } else if (paramInt2 == 1) {
      setHeight(paramInt1);
    } 
  }
  
  public void setMaxHeight(int paramInt) {
    this.mMaxDimension[1] = paramInt;
  }
  
  public void setMaxWidth(int paramInt) {
    this.mMaxDimension[0] = paramInt;
  }
  
  public void setMeasureRequested(boolean paramBoolean) {
    this.mMeasureRequested = paramBoolean;
  }
  
  public void setMinHeight(int paramInt) {
    if (paramInt < 0) {
      this.mMinHeight = 0;
    } else {
      this.mMinHeight = paramInt;
    } 
  }
  
  public void setMinWidth(int paramInt) {
    if (paramInt < 0) {
      this.mMinWidth = 0;
    } else {
      this.mMinWidth = paramInt;
    } 
  }
  
  public void setOffset(int paramInt1, int paramInt2) {
    this.mOffsetX = paramInt1;
    this.mOffsetY = paramInt2;
  }
  
  public void setOrigin(int paramInt1, int paramInt2) {
    this.mX = paramInt1;
    this.mY = paramInt2;
  }
  
  public void setParent(ConstraintWidget paramConstraintWidget) {
    this.mParent = paramConstraintWidget;
  }
  
  void setRelativePositioning(int paramInt1, int paramInt2) {
    if (paramInt2 == 0) {
      this.mRelX = paramInt1;
    } else if (paramInt2 == 1) {
      this.mRelY = paramInt1;
    } 
  }
  
  public void setType(String paramString) {
    this.mType = paramString;
  }
  
  public void setVerticalBiasPercent(float paramFloat) {
    this.mVerticalBiasPercent = paramFloat;
  }
  
  public void setVerticalChainStyle(int paramInt) {
    this.mVerticalChainStyle = paramInt;
  }
  
  public void setVerticalDimension(int paramInt1, int paramInt2) {
    this.mY = paramInt1;
    paramInt2 -= paramInt1;
    this.mHeight = paramInt2;
    paramInt1 = this.mMinHeight;
    if (paramInt2 < paramInt1)
      this.mHeight = paramInt1; 
  }
  
  public void setVerticalDimensionBehaviour(DimensionBehaviour paramDimensionBehaviour) {
    this.mListDimensionBehaviors[1] = paramDimensionBehaviour;
  }
  
  public void setVerticalMatchStyle(int paramInt1, int paramInt2, int paramInt3, float paramFloat) {
    this.mMatchConstraintDefaultHeight = paramInt1;
    this.mMatchConstraintMinHeight = paramInt2;
    if (paramInt3 == Integer.MAX_VALUE)
      paramInt3 = 0; 
    this.mMatchConstraintMaxHeight = paramInt3;
    this.mMatchConstraintPercentHeight = paramFloat;
    if (paramFloat > 0.0F && paramFloat < 1.0F && paramInt1 == 0)
      this.mMatchConstraintDefaultHeight = 2; 
  }
  
  public void setVerticalWeight(float paramFloat) {
    this.mWeight[1] = paramFloat;
  }
  
  public void setVisibility(int paramInt) {
    this.mVisibility = paramInt;
  }
  
  public void setWidth(int paramInt) {
    this.mWidth = paramInt;
    int i = this.mMinWidth;
    if (paramInt < i)
      this.mWidth = i; 
  }
  
  public void setWidthWrapContent(boolean paramBoolean) {
    this.mIsWidthWrapContent = paramBoolean;
  }
  
  public void setWrapBehaviorInParent(int paramInt) {
    if (paramInt >= 0 && paramInt <= 3)
      this.mWrapBehaviorInParent = paramInt; 
  }
  
  public void setX(int paramInt) {
    this.mX = paramInt;
  }
  
  public void setY(int paramInt) {
    this.mY = paramInt;
  }
  
  public void setupDimensionRatio(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
    if (this.mResolvedDimensionRatioSide == -1)
      if (paramBoolean3 && !paramBoolean4) {
        this.mResolvedDimensionRatioSide = 0;
      } else if (!paramBoolean3 && paramBoolean4) {
        this.mResolvedDimensionRatioSide = 1;
        if (this.mDimensionRatioSide == -1)
          this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio; 
      }  
    if (this.mResolvedDimensionRatioSide == 0 && (!this.mTop.isConnected() || !this.mBottom.isConnected())) {
      this.mResolvedDimensionRatioSide = 1;
    } else if (this.mResolvedDimensionRatioSide == 1 && (!this.mLeft.isConnected() || !this.mRight.isConnected())) {
      this.mResolvedDimensionRatioSide = 0;
    } 
    if (this.mResolvedDimensionRatioSide == -1 && (!this.mTop.isConnected() || !this.mBottom.isConnected() || !this.mLeft.isConnected() || !this.mRight.isConnected()))
      if (this.mTop.isConnected() && this.mBottom.isConnected()) {
        this.mResolvedDimensionRatioSide = 0;
      } else if (this.mLeft.isConnected() && this.mRight.isConnected()) {
        this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
        this.mResolvedDimensionRatioSide = 1;
      }  
    if (this.mResolvedDimensionRatioSide == -1) {
      int i = this.mMatchConstraintMinWidth;
      if (i > 0 && this.mMatchConstraintMinHeight == 0) {
        this.mResolvedDimensionRatioSide = 0;
      } else if (i == 0 && this.mMatchConstraintMinHeight > 0) {
        this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
        this.mResolvedDimensionRatioSide = 1;
      } 
    } 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    String str1 = this.mType;
    String str2 = "";
    if (str1 != null) {
      str1 = "type: " + this.mType + " ";
    } else {
      str1 = "";
    } 
    stringBuilder = stringBuilder.append(str1);
    str1 = str2;
    if (this.mDebugName != null)
      str1 = "id: " + this.mDebugName + " "; 
    return stringBuilder.append(str1).append("(").append(this.mX).append(", ").append(this.mY).append(") - (").append(this.mWidth).append(" x ").append(this.mHeight).append(")").toString();
  }
  
  public void updateFromRuns(boolean paramBoolean1, boolean paramBoolean2) {
    // Byte code:
    //   0: iload_1
    //   1: aload_0
    //   2: getfield horizontalRun : Landroidx/constraintlayout/core/widgets/analyzer/HorizontalWidgetRun;
    //   5: invokevirtual isResolved : ()Z
    //   8: iand
    //   9: istore #9
    //   11: iload_2
    //   12: aload_0
    //   13: getfield verticalRun : Landroidx/constraintlayout/core/widgets/analyzer/VerticalWidgetRun;
    //   16: invokevirtual isResolved : ()Z
    //   19: iand
    //   20: istore #8
    //   22: aload_0
    //   23: getfield horizontalRun : Landroidx/constraintlayout/core/widgets/analyzer/HorizontalWidgetRun;
    //   26: getfield start : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   29: getfield value : I
    //   32: istore_3
    //   33: aload_0
    //   34: getfield verticalRun : Landroidx/constraintlayout/core/widgets/analyzer/VerticalWidgetRun;
    //   37: getfield start : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   40: getfield value : I
    //   43: istore #4
    //   45: aload_0
    //   46: getfield horizontalRun : Landroidx/constraintlayout/core/widgets/analyzer/HorizontalWidgetRun;
    //   49: getfield end : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   52: getfield value : I
    //   55: istore #6
    //   57: aload_0
    //   58: getfield verticalRun : Landroidx/constraintlayout/core/widgets/analyzer/VerticalWidgetRun;
    //   61: getfield end : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   64: getfield value : I
    //   67: istore #7
    //   69: iload #6
    //   71: iload_3
    //   72: isub
    //   73: iflt -> 146
    //   76: iload #7
    //   78: iload #4
    //   80: isub
    //   81: iflt -> 146
    //   84: iload_3
    //   85: ldc_w -2147483648
    //   88: if_icmpeq -> 146
    //   91: iload_3
    //   92: ldc 2147483647
    //   94: if_icmpeq -> 146
    //   97: iload #4
    //   99: ldc_w -2147483648
    //   102: if_icmpeq -> 146
    //   105: iload #4
    //   107: ldc 2147483647
    //   109: if_icmpeq -> 146
    //   112: iload #6
    //   114: ldc_w -2147483648
    //   117: if_icmpeq -> 146
    //   120: iload #6
    //   122: ldc 2147483647
    //   124: if_icmpeq -> 146
    //   127: iload #7
    //   129: ldc_w -2147483648
    //   132: if_icmpeq -> 146
    //   135: iload #7
    //   137: istore #5
    //   139: iload #7
    //   141: ldc 2147483647
    //   143: if_icmpne -> 157
    //   146: iconst_0
    //   147: istore_3
    //   148: iconst_0
    //   149: istore #4
    //   151: iconst_0
    //   152: istore #6
    //   154: iconst_0
    //   155: istore #5
    //   157: iload #6
    //   159: iload_3
    //   160: isub
    //   161: istore #6
    //   163: iload #5
    //   165: iload #4
    //   167: isub
    //   168: istore #5
    //   170: iload #9
    //   172: ifeq -> 180
    //   175: aload_0
    //   176: iload_3
    //   177: putfield mX : I
    //   180: iload #8
    //   182: ifeq -> 191
    //   185: aload_0
    //   186: iload #4
    //   188: putfield mY : I
    //   191: aload_0
    //   192: getfield mVisibility : I
    //   195: bipush #8
    //   197: if_icmpne -> 211
    //   200: aload_0
    //   201: iconst_0
    //   202: putfield mWidth : I
    //   205: aload_0
    //   206: iconst_0
    //   207: putfield mHeight : I
    //   210: return
    //   211: iload #9
    //   213: ifeq -> 271
    //   216: iload #6
    //   218: istore_3
    //   219: aload_0
    //   220: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   223: iconst_0
    //   224: aaload
    //   225: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   228: if_acmpne -> 248
    //   231: iload #6
    //   233: istore_3
    //   234: iload #6
    //   236: aload_0
    //   237: getfield mWidth : I
    //   240: if_icmpge -> 248
    //   243: aload_0
    //   244: getfield mWidth : I
    //   247: istore_3
    //   248: aload_0
    //   249: iload_3
    //   250: putfield mWidth : I
    //   253: aload_0
    //   254: getfield mMinWidth : I
    //   257: istore #4
    //   259: iload_3
    //   260: iload #4
    //   262: if_icmpge -> 271
    //   265: aload_0
    //   266: iload #4
    //   268: putfield mWidth : I
    //   271: iload #8
    //   273: ifeq -> 331
    //   276: iload #5
    //   278: istore_3
    //   279: aload_0
    //   280: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   283: iconst_1
    //   284: aaload
    //   285: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   288: if_acmpne -> 308
    //   291: iload #5
    //   293: istore_3
    //   294: iload #5
    //   296: aload_0
    //   297: getfield mHeight : I
    //   300: if_icmpge -> 308
    //   303: aload_0
    //   304: getfield mHeight : I
    //   307: istore_3
    //   308: aload_0
    //   309: iload_3
    //   310: putfield mHeight : I
    //   313: aload_0
    //   314: getfield mMinHeight : I
    //   317: istore #4
    //   319: iload_3
    //   320: iload #4
    //   322: if_icmpge -> 331
    //   325: aload_0
    //   326: iload #4
    //   328: putfield mHeight : I
    //   331: return
  }
  
  public void updateFromSolver(LinearSystem paramLinearSystem, boolean paramBoolean) {
    // Byte code:
    //   0: aload_1
    //   1: aload_0
    //   2: getfield mLeft : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   5: invokevirtual getObjectVariableValue : (Ljava/lang/Object;)I
    //   8: istore #4
    //   10: aload_1
    //   11: aload_0
    //   12: getfield mTop : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   15: invokevirtual getObjectVariableValue : (Ljava/lang/Object;)I
    //   18: istore #7
    //   20: aload_1
    //   21: aload_0
    //   22: getfield mRight : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   25: invokevirtual getObjectVariableValue : (Ljava/lang/Object;)I
    //   28: istore #6
    //   30: aload_1
    //   31: aload_0
    //   32: getfield mBottom : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   35: invokevirtual getObjectVariableValue : (Ljava/lang/Object;)I
    //   38: istore #8
    //   40: iload #4
    //   42: istore #5
    //   44: iload #6
    //   46: istore_3
    //   47: iload_2
    //   48: ifeq -> 127
    //   51: aload_0
    //   52: getfield horizontalRun : Landroidx/constraintlayout/core/widgets/analyzer/HorizontalWidgetRun;
    //   55: astore_1
    //   56: iload #4
    //   58: istore #5
    //   60: iload #6
    //   62: istore_3
    //   63: aload_1
    //   64: ifnull -> 127
    //   67: iload #4
    //   69: istore #5
    //   71: iload #6
    //   73: istore_3
    //   74: aload_1
    //   75: getfield start : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   78: getfield resolved : Z
    //   81: ifeq -> 127
    //   84: iload #4
    //   86: istore #5
    //   88: iload #6
    //   90: istore_3
    //   91: aload_0
    //   92: getfield horizontalRun : Landroidx/constraintlayout/core/widgets/analyzer/HorizontalWidgetRun;
    //   95: getfield end : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   98: getfield resolved : Z
    //   101: ifeq -> 127
    //   104: aload_0
    //   105: getfield horizontalRun : Landroidx/constraintlayout/core/widgets/analyzer/HorizontalWidgetRun;
    //   108: getfield start : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   111: getfield value : I
    //   114: istore #5
    //   116: aload_0
    //   117: getfield horizontalRun : Landroidx/constraintlayout/core/widgets/analyzer/HorizontalWidgetRun;
    //   120: getfield end : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   123: getfield value : I
    //   126: istore_3
    //   127: iload #7
    //   129: istore #6
    //   131: iload #8
    //   133: istore #4
    //   135: iload_2
    //   136: ifeq -> 219
    //   139: aload_0
    //   140: getfield verticalRun : Landroidx/constraintlayout/core/widgets/analyzer/VerticalWidgetRun;
    //   143: astore_1
    //   144: iload #7
    //   146: istore #6
    //   148: iload #8
    //   150: istore #4
    //   152: aload_1
    //   153: ifnull -> 219
    //   156: iload #7
    //   158: istore #6
    //   160: iload #8
    //   162: istore #4
    //   164: aload_1
    //   165: getfield start : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   168: getfield resolved : Z
    //   171: ifeq -> 219
    //   174: iload #7
    //   176: istore #6
    //   178: iload #8
    //   180: istore #4
    //   182: aload_0
    //   183: getfield verticalRun : Landroidx/constraintlayout/core/widgets/analyzer/VerticalWidgetRun;
    //   186: getfield end : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   189: getfield resolved : Z
    //   192: ifeq -> 219
    //   195: aload_0
    //   196: getfield verticalRun : Landroidx/constraintlayout/core/widgets/analyzer/VerticalWidgetRun;
    //   199: getfield start : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   202: getfield value : I
    //   205: istore #6
    //   207: aload_0
    //   208: getfield verticalRun : Landroidx/constraintlayout/core/widgets/analyzer/VerticalWidgetRun;
    //   211: getfield end : Landroidx/constraintlayout/core/widgets/analyzer/DependencyNode;
    //   214: getfield value : I
    //   217: istore #4
    //   219: iload_3
    //   220: iload #5
    //   222: isub
    //   223: iflt -> 302
    //   226: iload #4
    //   228: iload #6
    //   230: isub
    //   231: iflt -> 302
    //   234: iload #5
    //   236: ldc_w -2147483648
    //   239: if_icmpeq -> 302
    //   242: iload #5
    //   244: ldc 2147483647
    //   246: if_icmpeq -> 302
    //   249: iload #6
    //   251: ldc_w -2147483648
    //   254: if_icmpeq -> 302
    //   257: iload #6
    //   259: ldc 2147483647
    //   261: if_icmpeq -> 302
    //   264: iload_3
    //   265: ldc_w -2147483648
    //   268: if_icmpeq -> 302
    //   271: iload_3
    //   272: ldc 2147483647
    //   274: if_icmpeq -> 302
    //   277: iload #4
    //   279: ldc_w -2147483648
    //   282: if_icmpeq -> 302
    //   285: iload #6
    //   287: istore #7
    //   289: iload_3
    //   290: istore #6
    //   292: iload #4
    //   294: istore_3
    //   295: iload #4
    //   297: ldc 2147483647
    //   299: if_icmpne -> 313
    //   302: iconst_0
    //   303: istore #5
    //   305: iconst_0
    //   306: istore #7
    //   308: iconst_0
    //   309: istore #6
    //   311: iconst_0
    //   312: istore_3
    //   313: aload_0
    //   314: iload #5
    //   316: iload #7
    //   318: iload #6
    //   320: iload_3
    //   321: invokevirtual setFrame : (IIII)V
    //   324: return
  }
  
  public enum DimensionBehaviour {
    FIXED, MATCH_CONSTRAINT, MATCH_PARENT, WRAP_CONTENT;
    
    private static final DimensionBehaviour[] $VALUES;
    
    static {
      DimensionBehaviour dimensionBehaviour4 = new DimensionBehaviour("FIXED", 0);
      FIXED = dimensionBehaviour4;
      DimensionBehaviour dimensionBehaviour1 = new DimensionBehaviour("WRAP_CONTENT", 1);
      WRAP_CONTENT = dimensionBehaviour1;
      DimensionBehaviour dimensionBehaviour2 = new DimensionBehaviour("MATCH_CONSTRAINT", 2);
      MATCH_CONSTRAINT = dimensionBehaviour2;
      DimensionBehaviour dimensionBehaviour3 = new DimensionBehaviour("MATCH_PARENT", 3);
      MATCH_PARENT = dimensionBehaviour3;
      $VALUES = new DimensionBehaviour[] { dimensionBehaviour4, dimensionBehaviour1, dimensionBehaviour2, dimensionBehaviour3 };
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\widgets\ConstraintWidget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */