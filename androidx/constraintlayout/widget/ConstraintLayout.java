package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.core.Metrics;
import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.core.widgets.Guideline;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import java.util.ArrayList;
import java.util.HashMap;

public class ConstraintLayout extends ViewGroup {
  private static final boolean DEBUG = false;
  
  private static final boolean DEBUG_DRAW_CONSTRAINTS = false;
  
  public static final int DESIGN_INFO_ID = 0;
  
  private static final boolean MEASURE = false;
  
  private static final boolean OPTIMIZE_HEIGHT_CHANGE = false;
  
  private static final String TAG = "ConstraintLayout";
  
  private static final boolean USE_CONSTRAINTS_HELPER = true;
  
  public static final String VERSION = "ConstraintLayout-2.1.2";
  
  private static SharedValues sSharedValues = null;
  
  SparseArray<View> mChildrenByIds = new SparseArray();
  
  private ArrayList<ConstraintHelper> mConstraintHelpers = new ArrayList<>(4);
  
  protected ConstraintLayoutStates mConstraintLayoutSpec = null;
  
  private ConstraintSet mConstraintSet = null;
  
  private int mConstraintSetId = -1;
  
  private ConstraintsChangedListener mConstraintsChangedListener;
  
  private HashMap<String, Integer> mDesignIds = new HashMap<>();
  
  protected boolean mDirtyHierarchy = true;
  
  private int mLastMeasureHeight = -1;
  
  int mLastMeasureHeightMode = 0;
  
  int mLastMeasureHeightSize = -1;
  
  private int mLastMeasureWidth = -1;
  
  int mLastMeasureWidthMode = 0;
  
  int mLastMeasureWidthSize = -1;
  
  protected ConstraintWidgetContainer mLayoutWidget = new ConstraintWidgetContainer();
  
  private int mMaxHeight = Integer.MAX_VALUE;
  
  private int mMaxWidth = Integer.MAX_VALUE;
  
  Measurer mMeasurer = new Measurer(this);
  
  private Metrics mMetrics;
  
  private int mMinHeight = 0;
  
  private int mMinWidth = 0;
  
  private int mOnMeasureHeightMeasureSpec = 0;
  
  private int mOnMeasureWidthMeasureSpec = 0;
  
  private int mOptimizationLevel = 257;
  
  private SparseArray<ConstraintWidget> mTempMapIdToWidget = new SparseArray();
  
  public ConstraintLayout(Context paramContext) {
    super(paramContext);
    init((AttributeSet)null, 0, 0);
  }
  
  public ConstraintLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramAttributeSet, 0, 0);
  }
  
  public ConstraintLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramAttributeSet, paramInt, 0);
  }
  
  public ConstraintLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    init(paramAttributeSet, paramInt1, paramInt2);
  }
  
  private int getPaddingWidth() {
    int j = Math.max(0, getPaddingLeft()) + Math.max(0, getPaddingRight());
    int i = 0;
    if (Build.VERSION.SDK_INT >= 17)
      i = Math.max(0, getPaddingStart()) + Math.max(0, getPaddingEnd()); 
    if (i > 0)
      j = i; 
    return j;
  }
  
  public static SharedValues getSharedValues() {
    if (sSharedValues == null)
      sSharedValues = new SharedValues(); 
    return sSharedValues;
  }
  
  private final ConstraintWidget getTargetWidget(int paramInt) {
    ConstraintWidget constraintWidget;
    if (paramInt == 0)
      return (ConstraintWidget)this.mLayoutWidget; 
    View view2 = (View)this.mChildrenByIds.get(paramInt);
    View view1 = view2;
    if (view2 == null) {
      view2 = findViewById(paramInt);
      view1 = view2;
      if (view2 != null) {
        view1 = view2;
        if (view2 != this) {
          view1 = view2;
          if (view2.getParent() == this) {
            onViewAdded(view2);
            view1 = view2;
          } 
        } 
      } 
    } 
    if (view1 == this)
      return (ConstraintWidget)this.mLayoutWidget; 
    if (view1 == null) {
      view1 = null;
    } else {
      constraintWidget = ((LayoutParams)view1.getLayoutParams()).widget;
    } 
    return constraintWidget;
  }
  
  private void init(AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    this.mLayoutWidget.setCompanionWidget(this);
    this.mLayoutWidget.setMeasurer(this.mMeasurer);
    this.mChildrenByIds.put(getId(), this);
    this.mConstraintSet = null;
    if (paramAttributeSet != null) {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.ConstraintLayout_Layout, paramInt1, paramInt2);
      paramInt2 = typedArray.getIndexCount();
      for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++) {
        int i = typedArray.getIndex(paramInt1);
        if (i == R.styleable.ConstraintLayout_Layout_android_minWidth) {
          this.mMinWidth = typedArray.getDimensionPixelOffset(i, this.mMinWidth);
        } else if (i == R.styleable.ConstraintLayout_Layout_android_minHeight) {
          this.mMinHeight = typedArray.getDimensionPixelOffset(i, this.mMinHeight);
        } else if (i == R.styleable.ConstraintLayout_Layout_android_maxWidth) {
          this.mMaxWidth = typedArray.getDimensionPixelOffset(i, this.mMaxWidth);
        } else if (i == R.styleable.ConstraintLayout_Layout_android_maxHeight) {
          this.mMaxHeight = typedArray.getDimensionPixelOffset(i, this.mMaxHeight);
        } else if (i == R.styleable.ConstraintLayout_Layout_layout_optimizationLevel) {
          this.mOptimizationLevel = typedArray.getInt(i, this.mOptimizationLevel);
        } else if (i == R.styleable.ConstraintLayout_Layout_layoutDescription) {
          i = typedArray.getResourceId(i, 0);
          if (i != 0)
            try {
              parseLayoutDescription(i);
            } catch (android.content.res.Resources.NotFoundException notFoundException) {
              this.mConstraintLayoutSpec = null;
            }  
        } else if (i == R.styleable.ConstraintLayout_Layout_constraintSet) {
          i = typedArray.getResourceId(i, 0);
          try {
            ConstraintSet constraintSet = new ConstraintSet();
            this();
            this.mConstraintSet = constraintSet;
            constraintSet.load(getContext(), i);
          } catch (android.content.res.Resources.NotFoundException notFoundException) {
            this.mConstraintSet = null;
          } 
          this.mConstraintSetId = i;
        } 
      } 
      typedArray.recycle();
    } 
    this.mLayoutWidget.setOptimizationLevel(this.mOptimizationLevel);
  }
  
  private void markHierarchyDirty() {
    this.mDirtyHierarchy = true;
    this.mLastMeasureWidth = -1;
    this.mLastMeasureHeight = -1;
    this.mLastMeasureWidthSize = -1;
    this.mLastMeasureHeightSize = -1;
    this.mLastMeasureWidthMode = 0;
    this.mLastMeasureHeightMode = 0;
  }
  
  private void setChildrenConstraints() {
    boolean bool = isInEditMode();
    int i = getChildCount();
    byte b;
    for (b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = getViewWidget(getChildAt(b));
      if (constraintWidget != null)
        constraintWidget.reset(); 
    } 
    if (bool)
      for (b = 0; b < i; b++) {
        View view = getChildAt(b);
        try {
          String str2 = getResources().getResourceName(view.getId());
          setDesignInformation(0, str2, Integer.valueOf(view.getId()));
          int k = str2.indexOf('/');
          String str1 = str2;
          if (k != -1)
            str1 = str2.substring(k + 1); 
          getTargetWidget(view.getId()).setDebugName(str1);
        } catch (android.content.res.Resources.NotFoundException notFoundException) {}
      }  
    if (this.mConstraintSetId != -1)
      for (b = 0; b < i; b++) {
        View view = getChildAt(b);
        if (view.getId() == this.mConstraintSetId && view instanceof Constraints)
          this.mConstraintSet = ((Constraints)view).getConstraintSet(); 
      }  
    ConstraintSet constraintSet = this.mConstraintSet;
    if (constraintSet != null)
      constraintSet.applyToInternal(this, true); 
    this.mLayoutWidget.removeAllChildren();
    int j = this.mConstraintHelpers.size();
    if (j > 0)
      for (b = 0; b < j; b++)
        ((ConstraintHelper)this.mConstraintHelpers.get(b)).updatePreLayout(this);  
    for (b = 0; b < i; b++) {
      View view = getChildAt(b);
      if (view instanceof Placeholder)
        ((Placeholder)view).updatePreLayout(this); 
    } 
    this.mTempMapIdToWidget.clear();
    this.mTempMapIdToWidget.put(0, this.mLayoutWidget);
    this.mTempMapIdToWidget.put(getId(), this.mLayoutWidget);
    for (b = 0; b < i; b++) {
      View view = getChildAt(b);
      ConstraintWidget constraintWidget = getViewWidget(view);
      this.mTempMapIdToWidget.put(view.getId(), constraintWidget);
    } 
    for (b = 0; b < i; b++) {
      View view = getChildAt(b);
      ConstraintWidget constraintWidget = getViewWidget(view);
      if (constraintWidget != null) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        this.mLayoutWidget.add(constraintWidget);
        applyConstraintsFromLayoutParams(bool, view, constraintWidget, layoutParams, this.mTempMapIdToWidget);
      } 
    } 
  }
  
  private void setWidgetBaseline(ConstraintWidget paramConstraintWidget, LayoutParams paramLayoutParams, SparseArray<ConstraintWidget> paramSparseArray, int paramInt, ConstraintAnchor.Type paramType) {
    View view = (View)this.mChildrenByIds.get(paramInt);
    ConstraintWidget constraintWidget = (ConstraintWidget)paramSparseArray.get(paramInt);
    if (constraintWidget != null && view != null && view.getLayoutParams() instanceof LayoutParams) {
      paramLayoutParams.needsBaseline = true;
      if (paramType == ConstraintAnchor.Type.BASELINE) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        layoutParams.needsBaseline = true;
        layoutParams.widget.setHasBaseline(true);
      } 
      paramConstraintWidget.getAnchor(ConstraintAnchor.Type.BASELINE).connect(constraintWidget.getAnchor(paramType), paramLayoutParams.baselineMargin, paramLayoutParams.goneBaselineMargin, true);
      paramConstraintWidget.setHasBaseline(true);
      paramConstraintWidget.getAnchor(ConstraintAnchor.Type.TOP).reset();
      paramConstraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).reset();
    } 
  }
  
  private boolean updateHierarchy() {
    boolean bool1;
    int i = getChildCount();
    boolean bool2 = false;
    byte b = 0;
    while (true) {
      bool1 = bool2;
      if (b < i) {
        if (getChildAt(b).isLayoutRequested()) {
          bool1 = true;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    if (bool1)
      setChildrenConstraints(); 
    return bool1;
  }
  
  protected void applyConstraintsFromLayoutParams(boolean paramBoolean, View paramView, ConstraintWidget paramConstraintWidget, LayoutParams paramLayoutParams, SparseArray<ConstraintWidget> paramSparseArray) {
    // Byte code:
    //   0: aload #4
    //   2: invokevirtual validate : ()V
    //   5: aload #4
    //   7: iconst_0
    //   8: putfield helped : Z
    //   11: aload_3
    //   12: aload_2
    //   13: invokevirtual getVisibility : ()I
    //   16: invokevirtual setVisibility : (I)V
    //   19: aload #4
    //   21: getfield isInPlaceholder : Z
    //   24: ifeq -> 38
    //   27: aload_3
    //   28: iconst_1
    //   29: invokevirtual setInPlaceholder : (Z)V
    //   32: aload_3
    //   33: bipush #8
    //   35: invokevirtual setVisibility : (I)V
    //   38: aload_3
    //   39: aload_2
    //   40: invokevirtual setCompanionWidget : (Ljava/lang/Object;)V
    //   43: aload_2
    //   44: instanceof androidx/constraintlayout/widget/ConstraintHelper
    //   47: ifeq -> 68
    //   50: aload_2
    //   51: checkcast androidx/constraintlayout/widget/ConstraintHelper
    //   54: aload_3
    //   55: aload_0
    //   56: getfield mLayoutWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidgetContainer;
    //   59: invokevirtual isRtl : ()Z
    //   62: invokevirtual resolveRtl : (Landroidx/constraintlayout/core/widgets/ConstraintWidget;Z)V
    //   65: goto -> 68
    //   68: aload #4
    //   70: getfield isGuideline : Z
    //   73: ifeq -> 179
    //   76: aload_3
    //   77: checkcast androidx/constraintlayout/core/widgets/Guideline
    //   80: astore_2
    //   81: aload #4
    //   83: getfield resolvedGuideBegin : I
    //   86: istore #7
    //   88: aload #4
    //   90: getfield resolvedGuideEnd : I
    //   93: istore #8
    //   95: aload #4
    //   97: getfield resolvedGuidePercent : F
    //   100: fstore #6
    //   102: getstatic android/os/Build$VERSION.SDK_INT : I
    //   105: bipush #17
    //   107: if_icmpge -> 131
    //   110: aload #4
    //   112: getfield guideBegin : I
    //   115: istore #7
    //   117: aload #4
    //   119: getfield guideEnd : I
    //   122: istore #8
    //   124: aload #4
    //   126: getfield guidePercent : F
    //   129: fstore #6
    //   131: fload #6
    //   133: ldc_w -1.0
    //   136: fcmpl
    //   137: ifeq -> 149
    //   140: aload_2
    //   141: fload #6
    //   143: invokevirtual setGuidePercent : (F)V
    //   146: goto -> 176
    //   149: iload #7
    //   151: iconst_m1
    //   152: if_icmpeq -> 164
    //   155: aload_2
    //   156: iload #7
    //   158: invokevirtual setGuideBegin : (I)V
    //   161: goto -> 176
    //   164: iload #8
    //   166: iconst_m1
    //   167: if_icmpeq -> 176
    //   170: aload_2
    //   171: iload #8
    //   173: invokevirtual setGuideEnd : (I)V
    //   176: goto -> 1395
    //   179: aload #4
    //   181: getfield resolvedLeftToLeft : I
    //   184: istore #8
    //   186: aload #4
    //   188: getfield resolvedLeftToRight : I
    //   191: istore #9
    //   193: aload #4
    //   195: getfield resolvedRightToLeft : I
    //   198: istore #10
    //   200: aload #4
    //   202: getfield resolvedRightToRight : I
    //   205: istore #7
    //   207: aload #4
    //   209: getfield resolveGoneLeftMargin : I
    //   212: istore #11
    //   214: aload #4
    //   216: getfield resolveGoneRightMargin : I
    //   219: istore #12
    //   221: aload #4
    //   223: getfield resolvedHorizontalBias : F
    //   226: fstore #6
    //   228: getstatic android/os/Build$VERSION.SDK_INT : I
    //   231: bipush #17
    //   233: if_icmpge -> 472
    //   236: aload #4
    //   238: getfield leftToLeft : I
    //   241: istore #8
    //   243: aload #4
    //   245: getfield leftToRight : I
    //   248: istore #7
    //   250: aload #4
    //   252: getfield rightToLeft : I
    //   255: istore #14
    //   257: aload #4
    //   259: getfield rightToRight : I
    //   262: istore #11
    //   264: aload #4
    //   266: getfield goneLeftMargin : I
    //   269: istore #10
    //   271: aload #4
    //   273: getfield goneRightMargin : I
    //   276: istore #9
    //   278: aload #4
    //   280: getfield horizontalBias : F
    //   283: fstore #6
    //   285: iload #8
    //   287: iconst_m1
    //   288: if_icmpne -> 335
    //   291: iload #7
    //   293: iconst_m1
    //   294: if_icmpne -> 335
    //   297: aload #4
    //   299: getfield startToStart : I
    //   302: iconst_m1
    //   303: if_icmpeq -> 316
    //   306: aload #4
    //   308: getfield startToStart : I
    //   311: istore #8
    //   313: goto -> 335
    //   316: aload #4
    //   318: getfield startToEnd : I
    //   321: iconst_m1
    //   322: if_icmpeq -> 335
    //   325: aload #4
    //   327: getfield startToEnd : I
    //   330: istore #7
    //   332: goto -> 335
    //   335: iload #14
    //   337: iconst_m1
    //   338: if_icmpne -> 445
    //   341: iload #11
    //   343: iconst_m1
    //   344: if_icmpne -> 445
    //   347: aload #4
    //   349: getfield endToStart : I
    //   352: iconst_m1
    //   353: if_icmpeq -> 394
    //   356: aload #4
    //   358: getfield endToStart : I
    //   361: istore #14
    //   363: iload #10
    //   365: istore #12
    //   367: iload #9
    //   369: istore #13
    //   371: iload #7
    //   373: istore #9
    //   375: iload #11
    //   377: istore #7
    //   379: iload #14
    //   381: istore #10
    //   383: iload #12
    //   385: istore #11
    //   387: iload #13
    //   389: istore #12
    //   391: goto -> 472
    //   394: aload #4
    //   396: getfield endToEnd : I
    //   399: iconst_m1
    //   400: if_icmpeq -> 445
    //   403: aload #4
    //   405: getfield endToEnd : I
    //   408: istore #15
    //   410: iload #14
    //   412: istore #11
    //   414: iload #10
    //   416: istore #13
    //   418: iload #9
    //   420: istore #12
    //   422: iload #15
    //   424: istore #10
    //   426: iload #7
    //   428: istore #9
    //   430: iload #10
    //   432: istore #7
    //   434: iload #11
    //   436: istore #10
    //   438: iload #13
    //   440: istore #11
    //   442: goto -> 472
    //   445: iload #10
    //   447: istore #13
    //   449: iload #9
    //   451: istore #12
    //   453: iload #7
    //   455: istore #9
    //   457: iload #11
    //   459: istore #7
    //   461: iload #14
    //   463: istore #10
    //   465: iload #13
    //   467: istore #11
    //   469: goto -> 472
    //   472: aload #4
    //   474: getfield circleConstraint : I
    //   477: iconst_m1
    //   478: if_icmpeq -> 517
    //   481: aload #5
    //   483: aload #4
    //   485: getfield circleConstraint : I
    //   488: invokevirtual get : (I)Ljava/lang/Object;
    //   491: checkcast androidx/constraintlayout/core/widgets/ConstraintWidget
    //   494: astore_2
    //   495: aload_2
    //   496: ifnull -> 514
    //   499: aload_3
    //   500: aload_2
    //   501: aload #4
    //   503: getfield circleAngle : F
    //   506: aload #4
    //   508: getfield circleRadius : I
    //   511: invokevirtual connectCircularConstraint : (Landroidx/constraintlayout/core/widgets/ConstraintWidget;FI)V
    //   514: goto -> 1011
    //   517: iload #8
    //   519: iconst_m1
    //   520: if_icmpeq -> 562
    //   523: aload #5
    //   525: iload #8
    //   527: invokevirtual get : (I)Ljava/lang/Object;
    //   530: checkcast androidx/constraintlayout/core/widgets/ConstraintWidget
    //   533: astore_2
    //   534: aload_2
    //   535: ifnull -> 559
    //   538: aload_3
    //   539: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   542: aload_2
    //   543: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   546: aload #4
    //   548: getfield leftMargin : I
    //   551: iload #11
    //   553: invokevirtual immediateConnect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;II)V
    //   556: goto -> 604
    //   559: goto -> 604
    //   562: iload #9
    //   564: iconst_m1
    //   565: if_icmpeq -> 604
    //   568: aload #5
    //   570: iload #9
    //   572: invokevirtual get : (I)Ljava/lang/Object;
    //   575: checkcast androidx/constraintlayout/core/widgets/ConstraintWidget
    //   578: astore_2
    //   579: aload_2
    //   580: ifnull -> 604
    //   583: aload_3
    //   584: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   587: aload_2
    //   588: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   591: aload #4
    //   593: getfield leftMargin : I
    //   596: iload #11
    //   598: invokevirtual immediateConnect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;II)V
    //   601: goto -> 604
    //   604: iload #10
    //   606: iconst_m1
    //   607: if_icmpeq -> 646
    //   610: aload #5
    //   612: iload #10
    //   614: invokevirtual get : (I)Ljava/lang/Object;
    //   617: checkcast androidx/constraintlayout/core/widgets/ConstraintWidget
    //   620: astore_2
    //   621: aload_2
    //   622: ifnull -> 643
    //   625: aload_3
    //   626: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   629: aload_2
    //   630: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   633: aload #4
    //   635: getfield rightMargin : I
    //   638: iload #12
    //   640: invokevirtual immediateConnect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;II)V
    //   643: goto -> 691
    //   646: iload #7
    //   648: iconst_m1
    //   649: if_icmpeq -> 691
    //   652: aload #5
    //   654: iload #7
    //   656: invokevirtual get : (I)Ljava/lang/Object;
    //   659: checkcast androidx/constraintlayout/core/widgets/ConstraintWidget
    //   662: astore_2
    //   663: aload_2
    //   664: ifnull -> 688
    //   667: aload_3
    //   668: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   671: aload_2
    //   672: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   675: aload #4
    //   677: getfield rightMargin : I
    //   680: iload #12
    //   682: invokevirtual immediateConnect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;II)V
    //   685: goto -> 691
    //   688: goto -> 691
    //   691: aload #4
    //   693: getfield topToTop : I
    //   696: iconst_m1
    //   697: if_icmpeq -> 742
    //   700: aload #5
    //   702: aload #4
    //   704: getfield topToTop : I
    //   707: invokevirtual get : (I)Ljava/lang/Object;
    //   710: checkcast androidx/constraintlayout/core/widgets/ConstraintWidget
    //   713: astore_2
    //   714: aload_2
    //   715: ifnull -> 793
    //   718: aload_3
    //   719: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   722: aload_2
    //   723: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   726: aload #4
    //   728: getfield topMargin : I
    //   731: aload #4
    //   733: getfield goneTopMargin : I
    //   736: invokevirtual immediateConnect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;II)V
    //   739: goto -> 793
    //   742: aload #4
    //   744: getfield topToBottom : I
    //   747: iconst_m1
    //   748: if_icmpeq -> 793
    //   751: aload #5
    //   753: aload #4
    //   755: getfield topToBottom : I
    //   758: invokevirtual get : (I)Ljava/lang/Object;
    //   761: checkcast androidx/constraintlayout/core/widgets/ConstraintWidget
    //   764: astore_2
    //   765: aload_2
    //   766: ifnull -> 793
    //   769: aload_3
    //   770: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   773: aload_2
    //   774: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   777: aload #4
    //   779: getfield topMargin : I
    //   782: aload #4
    //   784: getfield goneTopMargin : I
    //   787: invokevirtual immediateConnect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;II)V
    //   790: goto -> 793
    //   793: aload #4
    //   795: getfield bottomToTop : I
    //   798: iconst_m1
    //   799: if_icmpeq -> 844
    //   802: aload #5
    //   804: aload #4
    //   806: getfield bottomToTop : I
    //   809: invokevirtual get : (I)Ljava/lang/Object;
    //   812: checkcast androidx/constraintlayout/core/widgets/ConstraintWidget
    //   815: astore_2
    //   816: aload_2
    //   817: ifnull -> 895
    //   820: aload_3
    //   821: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   824: aload_2
    //   825: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   828: aload #4
    //   830: getfield bottomMargin : I
    //   833: aload #4
    //   835: getfield goneBottomMargin : I
    //   838: invokevirtual immediateConnect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;II)V
    //   841: goto -> 895
    //   844: aload #4
    //   846: getfield bottomToBottom : I
    //   849: iconst_m1
    //   850: if_icmpeq -> 895
    //   853: aload #5
    //   855: aload #4
    //   857: getfield bottomToBottom : I
    //   860: invokevirtual get : (I)Ljava/lang/Object;
    //   863: checkcast androidx/constraintlayout/core/widgets/ConstraintWidget
    //   866: astore_2
    //   867: aload_2
    //   868: ifnull -> 895
    //   871: aload_3
    //   872: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   875: aload_2
    //   876: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   879: aload #4
    //   881: getfield bottomMargin : I
    //   884: aload #4
    //   886: getfield goneBottomMargin : I
    //   889: invokevirtual immediateConnect : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;II)V
    //   892: goto -> 895
    //   895: aload #4
    //   897: getfield baselineToBaseline : I
    //   900: iconst_m1
    //   901: if_icmpeq -> 924
    //   904: aload_0
    //   905: aload_3
    //   906: aload #4
    //   908: aload #5
    //   910: aload #4
    //   912: getfield baselineToBaseline : I
    //   915: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BASELINE : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   918: invokespecial setWidgetBaseline : (Landroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/widget/ConstraintLayout$LayoutParams;Landroid/util/SparseArray;ILandroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)V
    //   921: goto -> 979
    //   924: aload #4
    //   926: getfield baselineToTop : I
    //   929: iconst_m1
    //   930: if_icmpeq -> 953
    //   933: aload_0
    //   934: aload_3
    //   935: aload #4
    //   937: aload #5
    //   939: aload #4
    //   941: getfield baselineToTop : I
    //   944: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   947: invokespecial setWidgetBaseline : (Landroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/widget/ConstraintLayout$LayoutParams;Landroid/util/SparseArray;ILandroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)V
    //   950: goto -> 979
    //   953: aload #4
    //   955: getfield baselineToBottom : I
    //   958: iconst_m1
    //   959: if_icmpeq -> 979
    //   962: aload_0
    //   963: aload_3
    //   964: aload #4
    //   966: aload #5
    //   968: aload #4
    //   970: getfield baselineToBottom : I
    //   973: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   976: invokespecial setWidgetBaseline : (Landroidx/constraintlayout/core/widgets/ConstraintWidget;Landroidx/constraintlayout/widget/ConstraintLayout$LayoutParams;Landroid/util/SparseArray;ILandroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)V
    //   979: fload #6
    //   981: fconst_0
    //   982: fcmpl
    //   983: iflt -> 992
    //   986: aload_3
    //   987: fload #6
    //   989: invokevirtual setHorizontalBiasPercent : (F)V
    //   992: aload #4
    //   994: getfield verticalBias : F
    //   997: fconst_0
    //   998: fcmpl
    //   999: iflt -> 1011
    //   1002: aload_3
    //   1003: aload #4
    //   1005: getfield verticalBias : F
    //   1008: invokevirtual setVerticalBiasPercent : (F)V
    //   1011: iload_1
    //   1012: ifeq -> 1047
    //   1015: aload #4
    //   1017: getfield editorAbsoluteX : I
    //   1020: iconst_m1
    //   1021: if_icmpne -> 1033
    //   1024: aload #4
    //   1026: getfield editorAbsoluteY : I
    //   1029: iconst_m1
    //   1030: if_icmpeq -> 1047
    //   1033: aload_3
    //   1034: aload #4
    //   1036: getfield editorAbsoluteX : I
    //   1039: aload #4
    //   1041: getfield editorAbsoluteY : I
    //   1044: invokevirtual setOrigin : (II)V
    //   1047: aload #4
    //   1049: getfield horizontalDimensionFixed : Z
    //   1052: ifne -> 1137
    //   1055: aload #4
    //   1057: getfield width : I
    //   1060: iconst_m1
    //   1061: if_icmpne -> 1122
    //   1064: aload #4
    //   1066: getfield constrainedWidth : Z
    //   1069: ifeq -> 1082
    //   1072: aload_3
    //   1073: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1076: invokevirtual setHorizontalDimensionBehaviour : (Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1079: goto -> 1089
    //   1082: aload_3
    //   1083: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_PARENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1086: invokevirtual setHorizontalDimensionBehaviour : (Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1089: aload_3
    //   1090: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   1093: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1096: aload #4
    //   1098: getfield leftMargin : I
    //   1101: putfield mMargin : I
    //   1104: aload_3
    //   1105: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   1108: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1111: aload #4
    //   1113: getfield rightMargin : I
    //   1116: putfield mMargin : I
    //   1119: goto -> 1170
    //   1122: aload_3
    //   1123: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1126: invokevirtual setHorizontalDimensionBehaviour : (Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1129: aload_3
    //   1130: iconst_0
    //   1131: invokevirtual setWidth : (I)V
    //   1134: goto -> 1170
    //   1137: aload_3
    //   1138: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1141: invokevirtual setHorizontalDimensionBehaviour : (Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1144: aload_3
    //   1145: aload #4
    //   1147: getfield width : I
    //   1150: invokevirtual setWidth : (I)V
    //   1153: aload #4
    //   1155: getfield width : I
    //   1158: bipush #-2
    //   1160: if_icmpne -> 1170
    //   1163: aload_3
    //   1164: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1167: invokevirtual setHorizontalDimensionBehaviour : (Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1170: aload #4
    //   1172: getfield verticalDimensionFixed : Z
    //   1175: ifne -> 1260
    //   1178: aload #4
    //   1180: getfield height : I
    //   1183: iconst_m1
    //   1184: if_icmpne -> 1245
    //   1187: aload #4
    //   1189: getfield constrainedHeight : Z
    //   1192: ifeq -> 1205
    //   1195: aload_3
    //   1196: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1199: invokevirtual setVerticalDimensionBehaviour : (Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1202: goto -> 1212
    //   1205: aload_3
    //   1206: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_PARENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1209: invokevirtual setVerticalDimensionBehaviour : (Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1212: aload_3
    //   1213: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   1216: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1219: aload #4
    //   1221: getfield topMargin : I
    //   1224: putfield mMargin : I
    //   1227: aload_3
    //   1228: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   1231: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1234: aload #4
    //   1236: getfield bottomMargin : I
    //   1239: putfield mMargin : I
    //   1242: goto -> 1293
    //   1245: aload_3
    //   1246: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1249: invokevirtual setVerticalDimensionBehaviour : (Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1252: aload_3
    //   1253: iconst_0
    //   1254: invokevirtual setHeight : (I)V
    //   1257: goto -> 1293
    //   1260: aload_3
    //   1261: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1264: invokevirtual setVerticalDimensionBehaviour : (Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1267: aload_3
    //   1268: aload #4
    //   1270: getfield height : I
    //   1273: invokevirtual setHeight : (I)V
    //   1276: aload #4
    //   1278: getfield height : I
    //   1281: bipush #-2
    //   1283: if_icmpne -> 1293
    //   1286: aload_3
    //   1287: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   1290: invokevirtual setVerticalDimensionBehaviour : (Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1293: aload_3
    //   1294: aload #4
    //   1296: getfield dimensionRatio : Ljava/lang/String;
    //   1299: invokevirtual setDimensionRatio : (Ljava/lang/String;)V
    //   1302: aload_3
    //   1303: aload #4
    //   1305: getfield horizontalWeight : F
    //   1308: invokevirtual setHorizontalWeight : (F)V
    //   1311: aload_3
    //   1312: aload #4
    //   1314: getfield verticalWeight : F
    //   1317: invokevirtual setVerticalWeight : (F)V
    //   1320: aload_3
    //   1321: aload #4
    //   1323: getfield horizontalChainStyle : I
    //   1326: invokevirtual setHorizontalChainStyle : (I)V
    //   1329: aload_3
    //   1330: aload #4
    //   1332: getfield verticalChainStyle : I
    //   1335: invokevirtual setVerticalChainStyle : (I)V
    //   1338: aload_3
    //   1339: aload #4
    //   1341: getfield wrapBehaviorInParent : I
    //   1344: invokevirtual setWrapBehaviorInParent : (I)V
    //   1347: aload_3
    //   1348: aload #4
    //   1350: getfield matchConstraintDefaultWidth : I
    //   1353: aload #4
    //   1355: getfield matchConstraintMinWidth : I
    //   1358: aload #4
    //   1360: getfield matchConstraintMaxWidth : I
    //   1363: aload #4
    //   1365: getfield matchConstraintPercentWidth : F
    //   1368: invokevirtual setHorizontalMatchStyle : (IIIF)V
    //   1371: aload_3
    //   1372: aload #4
    //   1374: getfield matchConstraintDefaultHeight : I
    //   1377: aload #4
    //   1379: getfield matchConstraintMinHeight : I
    //   1382: aload #4
    //   1384: getfield matchConstraintMaxHeight : I
    //   1387: aload #4
    //   1389: getfield matchConstraintPercentHeight : F
    //   1392: invokevirtual setVerticalMatchStyle : (IIIF)V
    //   1395: return
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  protected void dispatchDraw(Canvas paramCanvas) {
    ArrayList<ConstraintHelper> arrayList = this.mConstraintHelpers;
    if (arrayList != null) {
      int i = arrayList.size();
      if (i > 0)
        for (byte b = 0; b < i; b++)
          ((ConstraintHelper)this.mConstraintHelpers.get(b)).updatePreDraw(this);  
    } 
    super.dispatchDraw(paramCanvas);
    if (isInEditMode()) {
      float f3 = getWidth();
      float f2 = getHeight();
      float f1 = 1080.0F;
      int i = getChildCount();
      for (byte b = 0; b < i; b++) {
        View view = getChildAt(b);
        if (view.getVisibility() != 8) {
          Object object = view.getTag();
          if (object != null && object instanceof String) {
            object = ((String)object).split(",");
            if (object.length == 4) {
              int j = Integer.parseInt((String)object[0]);
              int m = Integer.parseInt((String)object[1]);
              int n = Integer.parseInt((String)object[2]);
              int k = Integer.parseInt((String)object[3]);
              j = (int)(j / f1 * f3);
              m = (int)(m / 1920.0F * f2);
              n = (int)(n / f1 * f3);
              k = (int)(k / 1920.0F * f2);
              object = new Paint();
              object.setColor(-65536);
              paramCanvas.drawLine(j, m, (j + n), m, (Paint)object);
              paramCanvas.drawLine((j + n), m, (j + n), (m + k), (Paint)object);
              paramCanvas.drawLine((j + n), (m + k), j, (m + k), (Paint)object);
              paramCanvas.drawLine(j, (m + k), j, m, (Paint)object);
              object.setColor(-16711936);
              paramCanvas.drawLine(j, m, (j + n), (m + k), (Paint)object);
              paramCanvas.drawLine(j, (m + k), (j + n), m, (Paint)object);
            } 
          } 
        } 
      } 
    } 
  }
  
  public void fillMetrics(Metrics paramMetrics) {
    this.mMetrics = paramMetrics;
    this.mLayoutWidget.fillMetrics(paramMetrics);
  }
  
  public void forceLayout() {
    markHierarchyDirty();
    super.forceLayout();
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams(-2, -2);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (ViewGroup.LayoutParams)new LayoutParams(paramLayoutParams);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  public Object getDesignInformation(int paramInt, Object paramObject) {
    if (paramInt == 0 && paramObject instanceof String) {
      paramObject = paramObject;
      HashMap<String, Integer> hashMap = this.mDesignIds;
      if (hashMap != null && hashMap.containsKey(paramObject))
        return this.mDesignIds.get(paramObject); 
    } 
    return null;
  }
  
  public int getMaxHeight() {
    return this.mMaxHeight;
  }
  
  public int getMaxWidth() {
    return this.mMaxWidth;
  }
  
  public int getMinHeight() {
    return this.mMinHeight;
  }
  
  public int getMinWidth() {
    return this.mMinWidth;
  }
  
  public int getOptimizationLevel() {
    return this.mLayoutWidget.getOptimizationLevel();
  }
  
  public View getViewById(int paramInt) {
    return (View)this.mChildrenByIds.get(paramInt);
  }
  
  public final ConstraintWidget getViewWidget(View paramView) {
    if (paramView == this)
      return (ConstraintWidget)this.mLayoutWidget; 
    if (paramView != null) {
      if (paramView.getLayoutParams() instanceof LayoutParams)
        return ((LayoutParams)paramView.getLayoutParams()).widget; 
      paramView.setLayoutParams(generateLayoutParams(paramView.getLayoutParams()));
      if (paramView.getLayoutParams() instanceof LayoutParams)
        return ((LayoutParams)paramView.getLayoutParams()).widget; 
    } 
    return null;
  }
  
  protected boolean isRtl() {
    int i = Build.VERSION.SDK_INT;
    boolean bool = false;
    if (i >= 17) {
      if (((getContext().getApplicationInfo()).flags & 0x400000) != 0) {
        i = 1;
      } else {
        i = 0;
      } 
      boolean bool1 = bool;
      if (i != 0) {
        bool1 = bool;
        if (1 == getLayoutDirection())
          bool1 = true; 
      } 
      return bool1;
    } 
    return false;
  }
  
  public void loadLayoutDescription(int paramInt) {
    if (paramInt != 0) {
      try {
        ConstraintLayoutStates constraintLayoutStates = new ConstraintLayoutStates();
        this(getContext(), this, paramInt);
        this.mConstraintLayoutSpec = constraintLayoutStates;
      } catch (android.content.res.Resources.NotFoundException notFoundException) {
        this.mConstraintLayoutSpec = null;
      } 
    } else {
      this.mConstraintLayoutSpec = null;
    } 
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramInt2 = getChildCount();
    paramBoolean = isInEditMode();
    for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++) {
      View view = getChildAt(paramInt1);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      ConstraintWidget constraintWidget = layoutParams.widget;
      if ((view.getVisibility() != 8 || layoutParams.isGuideline || layoutParams.isHelper || layoutParams.isVirtualGroup || paramBoolean) && !layoutParams.isInPlaceholder) {
        int j = constraintWidget.getX();
        paramInt3 = constraintWidget.getY();
        int i = constraintWidget.getWidth() + j;
        paramInt4 = constraintWidget.getHeight() + paramInt3;
        view.layout(j, paramInt3, i, paramInt4);
        if (view instanceof Placeholder) {
          view = ((Placeholder)view).getContent();
          if (view != null) {
            view.setVisibility(0);
            view.layout(j, paramInt3, i, paramInt4);
          } 
        } 
      } 
    } 
    paramInt2 = this.mConstraintHelpers.size();
    if (paramInt2 > 0)
      for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++)
        ((ConstraintHelper)this.mConstraintHelpers.get(paramInt1)).updatePostLayout(this);  
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mOnMeasureWidthMeasureSpec : I
    //   4: iload_1
    //   5: if_icmpne -> 19
    //   8: aload_0
    //   9: getfield mOnMeasureHeightMeasureSpec : I
    //   12: iload_2
    //   13: if_icmpne -> 19
    //   16: goto -> 19
    //   19: aload_0
    //   20: getfield mDirtyHierarchy : Z
    //   23: ifne -> 69
    //   26: iconst_0
    //   27: ifne -> 69
    //   30: aload_0
    //   31: invokevirtual getChildCount : ()I
    //   34: istore #4
    //   36: iconst_0
    //   37: istore_3
    //   38: iload_3
    //   39: iload #4
    //   41: if_icmpge -> 69
    //   44: aload_0
    //   45: iload_3
    //   46: invokevirtual getChildAt : (I)Landroid/view/View;
    //   49: invokevirtual isLayoutRequested : ()Z
    //   52: ifeq -> 63
    //   55: aload_0
    //   56: iconst_1
    //   57: putfield mDirtyHierarchy : Z
    //   60: goto -> 69
    //   63: iinc #3, 1
    //   66: goto -> 38
    //   69: aload_0
    //   70: getfield mDirtyHierarchy : Z
    //   73: ifne -> 115
    //   76: iconst_0
    //   77: ifeq -> 115
    //   80: aload_0
    //   81: iload_1
    //   82: iload_2
    //   83: aload_0
    //   84: getfield mLayoutWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidgetContainer;
    //   87: invokevirtual getWidth : ()I
    //   90: aload_0
    //   91: getfield mLayoutWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidgetContainer;
    //   94: invokevirtual getHeight : ()I
    //   97: aload_0
    //   98: getfield mLayoutWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidgetContainer;
    //   101: invokevirtual isWidthMeasuredTooSmall : ()Z
    //   104: aload_0
    //   105: getfield mLayoutWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidgetContainer;
    //   108: invokevirtual isHeightMeasuredTooSmall : ()Z
    //   111: invokevirtual resolveMeasuredDimension : (IIIIZZ)V
    //   114: return
    //   115: aload_0
    //   116: iload_1
    //   117: putfield mOnMeasureWidthMeasureSpec : I
    //   120: aload_0
    //   121: iload_2
    //   122: putfield mOnMeasureHeightMeasureSpec : I
    //   125: aload_0
    //   126: getfield mLayoutWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidgetContainer;
    //   129: aload_0
    //   130: invokevirtual isRtl : ()Z
    //   133: invokevirtual setRtl : (Z)V
    //   136: aload_0
    //   137: getfield mDirtyHierarchy : Z
    //   140: ifeq -> 162
    //   143: aload_0
    //   144: iconst_0
    //   145: putfield mDirtyHierarchy : Z
    //   148: aload_0
    //   149: invokespecial updateHierarchy : ()Z
    //   152: ifeq -> 162
    //   155: aload_0
    //   156: getfield mLayoutWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidgetContainer;
    //   159: invokevirtual updateHierarchy : ()V
    //   162: aload_0
    //   163: aload_0
    //   164: getfield mLayoutWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidgetContainer;
    //   167: aload_0
    //   168: getfield mOptimizationLevel : I
    //   171: iload_1
    //   172: iload_2
    //   173: invokevirtual resolveSystem : (Landroidx/constraintlayout/core/widgets/ConstraintWidgetContainer;III)V
    //   176: aload_0
    //   177: iload_1
    //   178: iload_2
    //   179: aload_0
    //   180: getfield mLayoutWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidgetContainer;
    //   183: invokevirtual getWidth : ()I
    //   186: aload_0
    //   187: getfield mLayoutWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidgetContainer;
    //   190: invokevirtual getHeight : ()I
    //   193: aload_0
    //   194: getfield mLayoutWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidgetContainer;
    //   197: invokevirtual isWidthMeasuredTooSmall : ()Z
    //   200: aload_0
    //   201: getfield mLayoutWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidgetContainer;
    //   204: invokevirtual isHeightMeasuredTooSmall : ()Z
    //   207: invokevirtual resolveMeasuredDimension : (IIIIZZ)V
    //   210: return
  }
  
  public void onViewAdded(View paramView) {
    super.onViewAdded(paramView);
    ConstraintWidget constraintWidget = getViewWidget(paramView);
    if (paramView instanceof Guideline && !(constraintWidget instanceof Guideline)) {
      LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
      layoutParams.widget = (ConstraintWidget)new Guideline();
      layoutParams.isGuideline = true;
      ((Guideline)layoutParams.widget).setOrientation(layoutParams.orientation);
    } 
    if (paramView instanceof ConstraintHelper) {
      ConstraintHelper constraintHelper = (ConstraintHelper)paramView;
      constraintHelper.validateParams();
      ((LayoutParams)paramView.getLayoutParams()).isHelper = true;
      if (!this.mConstraintHelpers.contains(constraintHelper))
        this.mConstraintHelpers.add(constraintHelper); 
    } 
    this.mChildrenByIds.put(paramView.getId(), paramView);
    this.mDirtyHierarchy = true;
  }
  
  public void onViewRemoved(View paramView) {
    super.onViewRemoved(paramView);
    this.mChildrenByIds.remove(paramView.getId());
    ConstraintWidget constraintWidget = getViewWidget(paramView);
    this.mLayoutWidget.remove(constraintWidget);
    this.mConstraintHelpers.remove(paramView);
    this.mDirtyHierarchy = true;
  }
  
  protected void parseLayoutDescription(int paramInt) {
    this.mConstraintLayoutSpec = new ConstraintLayoutStates(getContext(), this, paramInt);
  }
  
  public void requestLayout() {
    markHierarchyDirty();
    super.requestLayout();
  }
  
  protected void resolveMeasuredDimension(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2) {
    int i = this.mMeasurer.paddingHeight;
    paramInt1 = resolveSizeAndState(paramInt3 + this.mMeasurer.paddingWidth, paramInt1, 0);
    paramInt3 = resolveSizeAndState(paramInt4 + i, paramInt2, 0 << 16);
    paramInt2 = Math.min(this.mMaxWidth, paramInt1 & 0xFFFFFF);
    paramInt3 = Math.min(this.mMaxHeight, paramInt3 & 0xFFFFFF);
    paramInt1 = paramInt2;
    if (paramBoolean1)
      paramInt1 = paramInt2 | 0x1000000; 
    paramInt2 = paramInt3;
    if (paramBoolean2)
      paramInt2 = paramInt3 | 0x1000000; 
    setMeasuredDimension(paramInt1, paramInt2);
    this.mLastMeasureWidth = paramInt1;
    this.mLastMeasureHeight = paramInt2;
  }
  
  protected void resolveSystem(ConstraintWidgetContainer paramConstraintWidgetContainer, int paramInt1, int paramInt2, int paramInt3) {
    int j = View.MeasureSpec.getMode(paramInt2);
    int i1 = View.MeasureSpec.getSize(paramInt2);
    int i = View.MeasureSpec.getMode(paramInt3);
    int m = View.MeasureSpec.getSize(paramInt3);
    int k = Math.max(0, getPaddingTop());
    int i3 = Math.max(0, getPaddingBottom());
    int n = k + i3;
    int i2 = getPaddingWidth();
    this.mMeasurer.captureLayoutInfo(paramInt2, paramInt3, k, i3, i2, n);
    if (Build.VERSION.SDK_INT >= 17) {
      paramInt3 = Math.max(0, getPaddingStart());
      paramInt2 = Math.max(0, getPaddingEnd());
      if (paramInt3 > 0 || paramInt2 > 0) {
        if (!isRtl())
          paramInt2 = paramInt3; 
      } else {
        paramInt2 = Math.max(0, getPaddingLeft());
      } 
    } else {
      paramInt2 = Math.max(0, getPaddingLeft());
    } 
    paramInt3 = i1 - i2;
    m -= n;
    setSelfDimensionBehaviour(paramConstraintWidgetContainer, j, paramInt3, i, m);
    paramConstraintWidgetContainer.measure(paramInt1, j, paramInt3, i, m, this.mLastMeasureWidth, this.mLastMeasureHeight, paramInt2, k);
  }
  
  public void setConstraintSet(ConstraintSet paramConstraintSet) {
    this.mConstraintSet = paramConstraintSet;
  }
  
  public void setDesignInformation(int paramInt, Object paramObject1, Object paramObject2) {
    if (paramInt == 0 && paramObject1 instanceof String && paramObject2 instanceof Integer) {
      if (this.mDesignIds == null)
        this.mDesignIds = new HashMap<>(); 
      String str = (String)paramObject1;
      paramInt = str.indexOf("/");
      paramObject1 = str;
      if (paramInt != -1)
        paramObject1 = str.substring(paramInt + 1); 
      paramInt = ((Integer)paramObject2).intValue();
      this.mDesignIds.put(paramObject1, Integer.valueOf(paramInt));
    } 
  }
  
  public void setId(int paramInt) {
    this.mChildrenByIds.remove(getId());
    super.setId(paramInt);
    this.mChildrenByIds.put(getId(), this);
  }
  
  public void setMaxHeight(int paramInt) {
    if (paramInt == this.mMaxHeight)
      return; 
    this.mMaxHeight = paramInt;
    requestLayout();
  }
  
  public void setMaxWidth(int paramInt) {
    if (paramInt == this.mMaxWidth)
      return; 
    this.mMaxWidth = paramInt;
    requestLayout();
  }
  
  public void setMinHeight(int paramInt) {
    if (paramInt == this.mMinHeight)
      return; 
    this.mMinHeight = paramInt;
    requestLayout();
  }
  
  public void setMinWidth(int paramInt) {
    if (paramInt == this.mMinWidth)
      return; 
    this.mMinWidth = paramInt;
    requestLayout();
  }
  
  public void setOnConstraintsChanged(ConstraintsChangedListener paramConstraintsChangedListener) {
    this.mConstraintsChangedListener = paramConstraintsChangedListener;
    ConstraintLayoutStates constraintLayoutStates = this.mConstraintLayoutSpec;
    if (constraintLayoutStates != null)
      constraintLayoutStates.setOnConstraintsChanged(paramConstraintsChangedListener); 
  }
  
  public void setOptimizationLevel(int paramInt) {
    this.mOptimizationLevel = paramInt;
    this.mLayoutWidget.setOptimizationLevel(paramInt);
  }
  
  protected void setSelfDimensionBehaviour(ConstraintWidgetContainer paramConstraintWidgetContainer, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    ConstraintWidget.DimensionBehaviour dimensionBehaviour3;
    int i = this.mMeasurer.paddingHeight;
    int j = this.mMeasurer.paddingWidth;
    ConstraintWidget.DimensionBehaviour dimensionBehaviour1 = ConstraintWidget.DimensionBehaviour.FIXED;
    ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.FIXED;
    boolean bool2 = false;
    boolean bool1 = false;
    int k = getChildCount();
    switch (paramInt1) {
      default:
        paramInt1 = bool2;
        break;
      case 1073741824:
        paramInt1 = Math.min(this.mMaxWidth - j, paramInt2);
        break;
      case 0:
        dimensionBehaviour3 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        dimensionBehaviour1 = dimensionBehaviour3;
        paramInt1 = bool2;
        if (k == 0) {
          paramInt1 = Math.max(0, this.mMinWidth);
          dimensionBehaviour1 = dimensionBehaviour3;
        } 
        break;
      case -2147483648:
        dimensionBehaviour3 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        paramInt1 = paramInt2;
        dimensionBehaviour1 = dimensionBehaviour3;
        if (k == 0) {
          paramInt1 = Math.max(0, this.mMinWidth);
          dimensionBehaviour1 = dimensionBehaviour3;
        } 
        break;
    } 
    switch (paramInt3) {
      default:
        paramInt2 = bool1;
        break;
      case 1073741824:
        paramInt2 = Math.min(this.mMaxHeight - i, paramInt4);
        break;
      case 0:
        dimensionBehaviour3 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        dimensionBehaviour2 = dimensionBehaviour3;
        paramInt2 = bool1;
        if (k == 0) {
          paramInt2 = Math.max(0, this.mMinHeight);
          dimensionBehaviour2 = dimensionBehaviour3;
        } 
        break;
      case -2147483648:
        dimensionBehaviour3 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        paramInt2 = paramInt4;
        dimensionBehaviour2 = dimensionBehaviour3;
        if (k == 0) {
          paramInt2 = Math.max(0, this.mMinHeight);
          dimensionBehaviour2 = dimensionBehaviour3;
        } 
        break;
    } 
    if (paramInt1 != paramConstraintWidgetContainer.getWidth() || paramInt2 != paramConstraintWidgetContainer.getHeight())
      paramConstraintWidgetContainer.invalidateMeasures(); 
    paramConstraintWidgetContainer.setX(0);
    paramConstraintWidgetContainer.setY(0);
    paramConstraintWidgetContainer.setMaxWidth(this.mMaxWidth - j);
    paramConstraintWidgetContainer.setMaxHeight(this.mMaxHeight - i);
    paramConstraintWidgetContainer.setMinWidth(0);
    paramConstraintWidgetContainer.setMinHeight(0);
    paramConstraintWidgetContainer.setHorizontalDimensionBehaviour(dimensionBehaviour1);
    paramConstraintWidgetContainer.setWidth(paramInt1);
    paramConstraintWidgetContainer.setVerticalDimensionBehaviour(dimensionBehaviour2);
    paramConstraintWidgetContainer.setHeight(paramInt2);
    paramConstraintWidgetContainer.setMinWidth(this.mMinWidth - j);
    paramConstraintWidgetContainer.setMinHeight(this.mMinHeight - i);
  }
  
  public void setState(int paramInt1, int paramInt2, int paramInt3) {
    ConstraintLayoutStates constraintLayoutStates = this.mConstraintLayoutSpec;
    if (constraintLayoutStates != null)
      constraintLayoutStates.updateConstraints(paramInt1, paramInt2, paramInt3); 
  }
  
  public boolean shouldDelayChildPressedState() {
    return false;
  }
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    public static final int BASELINE = 5;
    
    public static final int BOTTOM = 4;
    
    public static final int CHAIN_PACKED = 2;
    
    public static final int CHAIN_SPREAD = 0;
    
    public static final int CHAIN_SPREAD_INSIDE = 1;
    
    public static final int CIRCLE = 8;
    
    public static final int END = 7;
    
    public static final int GONE_UNSET = -2147483648;
    
    public static final int HORIZONTAL = 0;
    
    public static final int LEFT = 1;
    
    public static final int MATCH_CONSTRAINT = 0;
    
    public static final int MATCH_CONSTRAINT_PERCENT = 2;
    
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    
    public static final int PARENT_ID = 0;
    
    public static final int RIGHT = 2;
    
    public static final int START = 6;
    
    public static final int TOP = 3;
    
    public static final int UNSET = -1;
    
    public static final int VERTICAL = 1;
    
    public static final int WRAP_BEHAVIOR_HORIZONTAL_ONLY = 1;
    
    public static final int WRAP_BEHAVIOR_INCLUDED = 0;
    
    public static final int WRAP_BEHAVIOR_SKIPPED = 3;
    
    public static final int WRAP_BEHAVIOR_VERTICAL_ONLY = 2;
    
    public int baselineMargin = 0;
    
    public int baselineToBaseline = -1;
    
    public int baselineToBottom = -1;
    
    public int baselineToTop = -1;
    
    public int bottomToBottom = -1;
    
    public int bottomToTop = -1;
    
    public float circleAngle = 0.0F;
    
    public int circleConstraint = -1;
    
    public int circleRadius = 0;
    
    public boolean constrainedHeight = false;
    
    public boolean constrainedWidth = false;
    
    public String constraintTag = null;
    
    public String dimensionRatio = null;
    
    int dimensionRatioSide = 1;
    
    float dimensionRatioValue = 0.0F;
    
    public int editorAbsoluteX = -1;
    
    public int editorAbsoluteY = -1;
    
    public int endToEnd = -1;
    
    public int endToStart = -1;
    
    public int goneBaselineMargin = Integer.MIN_VALUE;
    
    public int goneBottomMargin = Integer.MIN_VALUE;
    
    public int goneEndMargin = Integer.MIN_VALUE;
    
    public int goneLeftMargin = Integer.MIN_VALUE;
    
    public int goneRightMargin = Integer.MIN_VALUE;
    
    public int goneStartMargin = Integer.MIN_VALUE;
    
    public int goneTopMargin = Integer.MIN_VALUE;
    
    public int guideBegin = -1;
    
    public int guideEnd = -1;
    
    public float guidePercent = -1.0F;
    
    boolean heightSet = true;
    
    public boolean helped = false;
    
    public float horizontalBias = 0.5F;
    
    public int horizontalChainStyle = 0;
    
    boolean horizontalDimensionFixed = true;
    
    public float horizontalWeight = -1.0F;
    
    boolean isGuideline = false;
    
    boolean isHelper = false;
    
    boolean isInPlaceholder = false;
    
    boolean isVirtualGroup = false;
    
    public int leftToLeft = -1;
    
    public int leftToRight = -1;
    
    public int matchConstraintDefaultHeight = 0;
    
    public int matchConstraintDefaultWidth = 0;
    
    public int matchConstraintMaxHeight = 0;
    
    public int matchConstraintMaxWidth = 0;
    
    public int matchConstraintMinHeight = 0;
    
    public int matchConstraintMinWidth = 0;
    
    public float matchConstraintPercentHeight = 1.0F;
    
    public float matchConstraintPercentWidth = 1.0F;
    
    boolean needsBaseline = false;
    
    public int orientation = -1;
    
    int resolveGoneLeftMargin = Integer.MIN_VALUE;
    
    int resolveGoneRightMargin = Integer.MIN_VALUE;
    
    int resolvedGuideBegin;
    
    int resolvedGuideEnd;
    
    float resolvedGuidePercent;
    
    float resolvedHorizontalBias = 0.5F;
    
    int resolvedLeftToLeft = -1;
    
    int resolvedLeftToRight = -1;
    
    int resolvedRightToLeft = -1;
    
    int resolvedRightToRight = -1;
    
    public int rightToLeft = -1;
    
    public int rightToRight = -1;
    
    public int startToEnd = -1;
    
    public int startToStart = -1;
    
    public int topToBottom = -1;
    
    public int topToTop = -1;
    
    public float verticalBias = 0.5F;
    
    public int verticalChainStyle = 0;
    
    boolean verticalDimensionFixed = true;
    
    public float verticalWeight = -1.0F;
    
    ConstraintWidget widget = new ConstraintWidget();
    
    boolean widthSet = true;
    
    public int wrapBehaviorInParent = 0;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.ConstraintLayout_Layout);
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        float f;
        int k;
        int j = typedArray.getIndex(b);
        switch (Table.map.get(j)) {
          case 66:
            this.wrapBehaviorInParent = typedArray.getInt(j, this.wrapBehaviorInParent);
            break;
          case 65:
            ConstraintSet.parseDimensionConstraints(this, typedArray, j, 1);
            this.heightSet = true;
            break;
          case 64:
            ConstraintSet.parseDimensionConstraints(this, typedArray, j, 0);
            this.widthSet = true;
            break;
          case 55:
            this.goneBaselineMargin = typedArray.getDimensionPixelSize(j, this.goneBaselineMargin);
            break;
          case 54:
            this.baselineMargin = typedArray.getDimensionPixelSize(j, this.baselineMargin);
            break;
          case 53:
            k = typedArray.getResourceId(j, this.baselineToBottom);
            this.baselineToBottom = k;
            if (k == -1)
              this.baselineToBottom = typedArray.getInt(j, -1); 
            break;
          case 52:
            k = typedArray.getResourceId(j, this.baselineToTop);
            this.baselineToTop = k;
            if (k == -1)
              this.baselineToTop = typedArray.getInt(j, -1); 
            break;
          case 51:
            this.constraintTag = typedArray.getString(j);
            break;
          case 50:
            this.editorAbsoluteY = typedArray.getDimensionPixelOffset(j, this.editorAbsoluteY);
            break;
          case 49:
            this.editorAbsoluteX = typedArray.getDimensionPixelOffset(j, this.editorAbsoluteX);
            break;
          case 48:
            this.verticalChainStyle = typedArray.getInt(j, 0);
            break;
          case 47:
            this.horizontalChainStyle = typedArray.getInt(j, 0);
            break;
          case 46:
            this.verticalWeight = typedArray.getFloat(j, this.verticalWeight);
            break;
          case 45:
            this.horizontalWeight = typedArray.getFloat(j, this.horizontalWeight);
            break;
          case 44:
            ConstraintSet.parseDimensionRatioString(this, typedArray.getString(j));
            break;
          case 38:
            this.matchConstraintPercentHeight = Math.max(0.0F, typedArray.getFloat(j, this.matchConstraintPercentHeight));
            this.matchConstraintDefaultHeight = 2;
            break;
          case 37:
            try {
              this.matchConstraintMaxHeight = typedArray.getDimensionPixelSize(j, this.matchConstraintMaxHeight);
            } catch (Exception exception) {
              if (typedArray.getInt(j, this.matchConstraintMaxHeight) == -2)
                this.matchConstraintMaxHeight = -2; 
            } 
            break;
          case 36:
            try {
              this.matchConstraintMinHeight = typedArray.getDimensionPixelSize(j, this.matchConstraintMinHeight);
            } catch (Exception exception) {
              if (typedArray.getInt(j, this.matchConstraintMinHeight) == -2)
                this.matchConstraintMinHeight = -2; 
            } 
            break;
          case 35:
            this.matchConstraintPercentWidth = Math.max(0.0F, typedArray.getFloat(j, this.matchConstraintPercentWidth));
            this.matchConstraintDefaultWidth = 2;
            break;
          case 34:
            try {
              this.matchConstraintMaxWidth = typedArray.getDimensionPixelSize(j, this.matchConstraintMaxWidth);
            } catch (Exception exception) {
              if (typedArray.getInt(j, this.matchConstraintMaxWidth) == -2)
                this.matchConstraintMaxWidth = -2; 
            } 
            break;
          case 33:
            try {
              this.matchConstraintMinWidth = typedArray.getDimensionPixelSize(j, this.matchConstraintMinWidth);
            } catch (Exception exception) {
              if (typedArray.getInt(j, this.matchConstraintMinWidth) == -2)
                this.matchConstraintMinWidth = -2; 
            } 
            break;
          case 32:
            j = typedArray.getInt(j, 0);
            this.matchConstraintDefaultHeight = j;
            if (j == 1)
              Log.e("ConstraintLayout", "layout_constraintHeight_default=\"wrap\" is deprecated.\nUse layout_height=\"WRAP_CONTENT\" and layout_constrainedHeight=\"true\" instead."); 
            break;
          case 31:
            j = typedArray.getInt(j, 0);
            this.matchConstraintDefaultWidth = j;
            if (j == 1)
              Log.e("ConstraintLayout", "layout_constraintWidth_default=\"wrap\" is deprecated.\nUse layout_width=\"WRAP_CONTENT\" and layout_constrainedWidth=\"true\" instead."); 
            break;
          case 30:
            this.verticalBias = typedArray.getFloat(j, this.verticalBias);
            break;
          case 29:
            this.horizontalBias = typedArray.getFloat(j, this.horizontalBias);
            break;
          case 28:
            this.constrainedHeight = typedArray.getBoolean(j, this.constrainedHeight);
            break;
          case 27:
            this.constrainedWidth = typedArray.getBoolean(j, this.constrainedWidth);
            break;
          case 26:
            this.goneEndMargin = typedArray.getDimensionPixelSize(j, this.goneEndMargin);
            break;
          case 25:
            this.goneStartMargin = typedArray.getDimensionPixelSize(j, this.goneStartMargin);
            break;
          case 24:
            this.goneBottomMargin = typedArray.getDimensionPixelSize(j, this.goneBottomMargin);
            break;
          case 23:
            this.goneRightMargin = typedArray.getDimensionPixelSize(j, this.goneRightMargin);
            break;
          case 22:
            this.goneTopMargin = typedArray.getDimensionPixelSize(j, this.goneTopMargin);
            break;
          case 21:
            this.goneLeftMargin = typedArray.getDimensionPixelSize(j, this.goneLeftMargin);
            break;
          case 20:
            k = typedArray.getResourceId(j, this.endToEnd);
            this.endToEnd = k;
            if (k == -1)
              this.endToEnd = typedArray.getInt(j, -1); 
            break;
          case 19:
            k = typedArray.getResourceId(j, this.endToStart);
            this.endToStart = k;
            if (k == -1)
              this.endToStart = typedArray.getInt(j, -1); 
            break;
          case 18:
            k = typedArray.getResourceId(j, this.startToStart);
            this.startToStart = k;
            if (k == -1)
              this.startToStart = typedArray.getInt(j, -1); 
            break;
          case 17:
            k = typedArray.getResourceId(j, this.startToEnd);
            this.startToEnd = k;
            if (k == -1)
              this.startToEnd = typedArray.getInt(j, -1); 
            break;
          case 16:
            k = typedArray.getResourceId(j, this.baselineToBaseline);
            this.baselineToBaseline = k;
            if (k == -1)
              this.baselineToBaseline = typedArray.getInt(j, -1); 
            break;
          case 15:
            k = typedArray.getResourceId(j, this.bottomToBottom);
            this.bottomToBottom = k;
            if (k == -1)
              this.bottomToBottom = typedArray.getInt(j, -1); 
            break;
          case 14:
            k = typedArray.getResourceId(j, this.bottomToTop);
            this.bottomToTop = k;
            if (k == -1)
              this.bottomToTop = typedArray.getInt(j, -1); 
            break;
          case 13:
            k = typedArray.getResourceId(j, this.topToBottom);
            this.topToBottom = k;
            if (k == -1)
              this.topToBottom = typedArray.getInt(j, -1); 
            break;
          case 12:
            k = typedArray.getResourceId(j, this.topToTop);
            this.topToTop = k;
            if (k == -1)
              this.topToTop = typedArray.getInt(j, -1); 
            break;
          case 11:
            k = typedArray.getResourceId(j, this.rightToRight);
            this.rightToRight = k;
            if (k == -1)
              this.rightToRight = typedArray.getInt(j, -1); 
            break;
          case 10:
            k = typedArray.getResourceId(j, this.rightToLeft);
            this.rightToLeft = k;
            if (k == -1)
              this.rightToLeft = typedArray.getInt(j, -1); 
            break;
          case 9:
            k = typedArray.getResourceId(j, this.leftToRight);
            this.leftToRight = k;
            if (k == -1)
              this.leftToRight = typedArray.getInt(j, -1); 
            break;
          case 8:
            k = typedArray.getResourceId(j, this.leftToLeft);
            this.leftToLeft = k;
            if (k == -1)
              this.leftToLeft = typedArray.getInt(j, -1); 
            break;
          case 7:
            this.guidePercent = typedArray.getFloat(j, this.guidePercent);
            break;
          case 6:
            this.guideEnd = typedArray.getDimensionPixelOffset(j, this.guideEnd);
            break;
          case 5:
            this.guideBegin = typedArray.getDimensionPixelOffset(j, this.guideBegin);
            break;
          case 4:
            f = typedArray.getFloat(j, this.circleAngle) % 360.0F;
            this.circleAngle = f;
            if (f < 0.0F)
              this.circleAngle = (360.0F - f) % 360.0F; 
            break;
          case 3:
            this.circleRadius = typedArray.getDimensionPixelSize(j, this.circleRadius);
            break;
          case 2:
            k = typedArray.getResourceId(j, this.circleConstraint);
            this.circleConstraint = k;
            if (k == -1)
              this.circleConstraint = typedArray.getInt(j, -1); 
            break;
          case 1:
            this.orientation = typedArray.getInt(j, this.orientation);
            break;
        } 
      } 
      typedArray.recycle();
      validate();
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.guideBegin = param1LayoutParams.guideBegin;
      this.guideEnd = param1LayoutParams.guideEnd;
      this.guidePercent = param1LayoutParams.guidePercent;
      this.leftToLeft = param1LayoutParams.leftToLeft;
      this.leftToRight = param1LayoutParams.leftToRight;
      this.rightToLeft = param1LayoutParams.rightToLeft;
      this.rightToRight = param1LayoutParams.rightToRight;
      this.topToTop = param1LayoutParams.topToTop;
      this.topToBottom = param1LayoutParams.topToBottom;
      this.bottomToTop = param1LayoutParams.bottomToTop;
      this.bottomToBottom = param1LayoutParams.bottomToBottom;
      this.baselineToBaseline = param1LayoutParams.baselineToBaseline;
      this.baselineToTop = param1LayoutParams.baselineToTop;
      this.baselineToBottom = param1LayoutParams.baselineToBottom;
      this.circleConstraint = param1LayoutParams.circleConstraint;
      this.circleRadius = param1LayoutParams.circleRadius;
      this.circleAngle = param1LayoutParams.circleAngle;
      this.startToEnd = param1LayoutParams.startToEnd;
      this.startToStart = param1LayoutParams.startToStart;
      this.endToStart = param1LayoutParams.endToStart;
      this.endToEnd = param1LayoutParams.endToEnd;
      this.goneLeftMargin = param1LayoutParams.goneLeftMargin;
      this.goneTopMargin = param1LayoutParams.goneTopMargin;
      this.goneRightMargin = param1LayoutParams.goneRightMargin;
      this.goneBottomMargin = param1LayoutParams.goneBottomMargin;
      this.goneStartMargin = param1LayoutParams.goneStartMargin;
      this.goneEndMargin = param1LayoutParams.goneEndMargin;
      this.goneBaselineMargin = param1LayoutParams.goneBaselineMargin;
      this.baselineMargin = param1LayoutParams.baselineMargin;
      this.horizontalBias = param1LayoutParams.horizontalBias;
      this.verticalBias = param1LayoutParams.verticalBias;
      this.dimensionRatio = param1LayoutParams.dimensionRatio;
      this.dimensionRatioValue = param1LayoutParams.dimensionRatioValue;
      this.dimensionRatioSide = param1LayoutParams.dimensionRatioSide;
      this.horizontalWeight = param1LayoutParams.horizontalWeight;
      this.verticalWeight = param1LayoutParams.verticalWeight;
      this.horizontalChainStyle = param1LayoutParams.horizontalChainStyle;
      this.verticalChainStyle = param1LayoutParams.verticalChainStyle;
      this.constrainedWidth = param1LayoutParams.constrainedWidth;
      this.constrainedHeight = param1LayoutParams.constrainedHeight;
      this.matchConstraintDefaultWidth = param1LayoutParams.matchConstraintDefaultWidth;
      this.matchConstraintDefaultHeight = param1LayoutParams.matchConstraintDefaultHeight;
      this.matchConstraintMinWidth = param1LayoutParams.matchConstraintMinWidth;
      this.matchConstraintMaxWidth = param1LayoutParams.matchConstraintMaxWidth;
      this.matchConstraintMinHeight = param1LayoutParams.matchConstraintMinHeight;
      this.matchConstraintMaxHeight = param1LayoutParams.matchConstraintMaxHeight;
      this.matchConstraintPercentWidth = param1LayoutParams.matchConstraintPercentWidth;
      this.matchConstraintPercentHeight = param1LayoutParams.matchConstraintPercentHeight;
      this.editorAbsoluteX = param1LayoutParams.editorAbsoluteX;
      this.editorAbsoluteY = param1LayoutParams.editorAbsoluteY;
      this.orientation = param1LayoutParams.orientation;
      this.horizontalDimensionFixed = param1LayoutParams.horizontalDimensionFixed;
      this.verticalDimensionFixed = param1LayoutParams.verticalDimensionFixed;
      this.needsBaseline = param1LayoutParams.needsBaseline;
      this.isGuideline = param1LayoutParams.isGuideline;
      this.resolvedLeftToLeft = param1LayoutParams.resolvedLeftToLeft;
      this.resolvedLeftToRight = param1LayoutParams.resolvedLeftToRight;
      this.resolvedRightToLeft = param1LayoutParams.resolvedRightToLeft;
      this.resolvedRightToRight = param1LayoutParams.resolvedRightToRight;
      this.resolveGoneLeftMargin = param1LayoutParams.resolveGoneLeftMargin;
      this.resolveGoneRightMargin = param1LayoutParams.resolveGoneRightMargin;
      this.resolvedHorizontalBias = param1LayoutParams.resolvedHorizontalBias;
      this.constraintTag = param1LayoutParams.constraintTag;
      this.wrapBehaviorInParent = param1LayoutParams.wrapBehaviorInParent;
      this.widget = param1LayoutParams.widget;
      this.widthSet = param1LayoutParams.widthSet;
      this.heightSet = param1LayoutParams.heightSet;
    }
    
    public String getConstraintTag() {
      return this.constraintTag;
    }
    
    public ConstraintWidget getConstraintWidget() {
      return this.widget;
    }
    
    public void reset() {
      ConstraintWidget constraintWidget = this.widget;
      if (constraintWidget != null)
        constraintWidget.reset(); 
    }
    
    public void resolveLayoutDirection(int param1Int) {
      int j = this.leftMargin;
      int k = this.rightMargin;
      int i = 0;
      if (Build.VERSION.SDK_INT >= 17) {
        super.resolveLayoutDirection(param1Int);
        if (1 == getLayoutDirection()) {
          param1Int = 1;
        } else {
          param1Int = 0;
        } 
        i = param1Int;
      } 
      this.resolvedRightToLeft = -1;
      this.resolvedRightToRight = -1;
      this.resolvedLeftToLeft = -1;
      this.resolvedLeftToRight = -1;
      this.resolveGoneLeftMargin = -1;
      this.resolveGoneRightMargin = -1;
      this.resolveGoneLeftMargin = this.goneLeftMargin;
      this.resolveGoneRightMargin = this.goneRightMargin;
      float f1 = this.horizontalBias;
      this.resolvedHorizontalBias = f1;
      int n = this.guideBegin;
      this.resolvedGuideBegin = n;
      int m = this.guideEnd;
      this.resolvedGuideEnd = m;
      float f2 = this.guidePercent;
      this.resolvedGuidePercent = f2;
      if (i != 0) {
        param1Int = 0;
        i = this.startToEnd;
        if (i != -1) {
          this.resolvedRightToLeft = i;
          param1Int = 1;
        } else {
          i = this.startToStart;
          if (i != -1) {
            this.resolvedRightToRight = i;
            param1Int = 1;
          } 
        } 
        i = this.endToStart;
        if (i != -1) {
          this.resolvedLeftToRight = i;
          param1Int = 1;
        } 
        i = this.endToEnd;
        if (i != -1) {
          this.resolvedLeftToLeft = i;
          param1Int = 1;
        } 
        i = this.goneStartMargin;
        if (i != Integer.MIN_VALUE)
          this.resolveGoneRightMargin = i; 
        i = this.goneEndMargin;
        if (i != Integer.MIN_VALUE)
          this.resolveGoneLeftMargin = i; 
        if (param1Int != 0)
          this.resolvedHorizontalBias = 1.0F - f1; 
        if (this.isGuideline && this.orientation == 1)
          if (f2 != -1.0F) {
            this.resolvedGuidePercent = 1.0F - f2;
            this.resolvedGuideBegin = -1;
            this.resolvedGuideEnd = -1;
          } else if (n != -1) {
            this.resolvedGuideEnd = n;
            this.resolvedGuideBegin = -1;
            this.resolvedGuidePercent = -1.0F;
          } else if (m != -1) {
            this.resolvedGuideBegin = m;
            this.resolvedGuideEnd = -1;
            this.resolvedGuidePercent = -1.0F;
          }  
      } else {
        param1Int = this.startToEnd;
        if (param1Int != -1)
          this.resolvedLeftToRight = param1Int; 
        param1Int = this.startToStart;
        if (param1Int != -1)
          this.resolvedLeftToLeft = param1Int; 
        param1Int = this.endToStart;
        if (param1Int != -1)
          this.resolvedRightToLeft = param1Int; 
        param1Int = this.endToEnd;
        if (param1Int != -1)
          this.resolvedRightToRight = param1Int; 
        param1Int = this.goneStartMargin;
        if (param1Int != Integer.MIN_VALUE)
          this.resolveGoneLeftMargin = param1Int; 
        param1Int = this.goneEndMargin;
        if (param1Int != Integer.MIN_VALUE)
          this.resolveGoneRightMargin = param1Int; 
      } 
      if (this.endToStart == -1 && this.endToEnd == -1 && this.startToStart == -1 && this.startToEnd == -1) {
        param1Int = this.rightToLeft;
        if (param1Int != -1) {
          this.resolvedRightToLeft = param1Int;
          if (this.rightMargin <= 0 && k > 0)
            this.rightMargin = k; 
        } else {
          param1Int = this.rightToRight;
          if (param1Int != -1) {
            this.resolvedRightToRight = param1Int;
            if (this.rightMargin <= 0 && k > 0)
              this.rightMargin = k; 
          } 
        } 
        param1Int = this.leftToLeft;
        if (param1Int != -1) {
          this.resolvedLeftToLeft = param1Int;
          if (this.leftMargin <= 0 && j > 0)
            this.leftMargin = j; 
        } else {
          param1Int = this.leftToRight;
          if (param1Int != -1) {
            this.resolvedLeftToRight = param1Int;
            if (this.leftMargin <= 0 && j > 0)
              this.leftMargin = j; 
          } 
        } 
      } 
    }
    
    public void setWidgetDebugName(String param1String) {
      this.widget.setDebugName(param1String);
    }
    
    public void validate() {
      this.isGuideline = false;
      this.horizontalDimensionFixed = true;
      this.verticalDimensionFixed = true;
      if (this.width == -2 && this.constrainedWidth) {
        this.horizontalDimensionFixed = false;
        if (this.matchConstraintDefaultWidth == 0)
          this.matchConstraintDefaultWidth = 1; 
      } 
      if (this.height == -2 && this.constrainedHeight) {
        this.verticalDimensionFixed = false;
        if (this.matchConstraintDefaultHeight == 0)
          this.matchConstraintDefaultHeight = 1; 
      } 
      if (this.width == 0 || this.width == -1) {
        this.horizontalDimensionFixed = false;
        if (this.width == 0 && this.matchConstraintDefaultWidth == 1) {
          this.width = -2;
          this.constrainedWidth = true;
        } 
      } 
      if (this.height == 0 || this.height == -1) {
        this.verticalDimensionFixed = false;
        if (this.height == 0 && this.matchConstraintDefaultHeight == 1) {
          this.height = -2;
          this.constrainedHeight = true;
        } 
      } 
      if (this.guidePercent != -1.0F || this.guideBegin != -1 || this.guideEnd != -1) {
        this.isGuideline = true;
        this.horizontalDimensionFixed = true;
        this.verticalDimensionFixed = true;
        if (!(this.widget instanceof Guideline))
          this.widget = (ConstraintWidget)new Guideline(); 
        ((Guideline)this.widget).setOrientation(this.orientation);
      } 
    }
    
    private static class Table {
      public static final int ANDROID_ORIENTATION = 1;
      
      public static final int LAYOUT_CONSTRAINED_HEIGHT = 28;
      
      public static final int LAYOUT_CONSTRAINED_WIDTH = 27;
      
      public static final int LAYOUT_CONSTRAINT_BASELINE_CREATOR = 43;
      
      public static final int LAYOUT_CONSTRAINT_BASELINE_TO_BASELINE_OF = 16;
      
      public static final int LAYOUT_CONSTRAINT_BASELINE_TO_BOTTOM_OF = 53;
      
      public static final int LAYOUT_CONSTRAINT_BASELINE_TO_TOP_OF = 52;
      
      public static final int LAYOUT_CONSTRAINT_BOTTOM_CREATOR = 42;
      
      public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_BOTTOM_OF = 15;
      
      public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_TOP_OF = 14;
      
      public static final int LAYOUT_CONSTRAINT_CIRCLE = 2;
      
      public static final int LAYOUT_CONSTRAINT_CIRCLE_ANGLE = 4;
      
      public static final int LAYOUT_CONSTRAINT_CIRCLE_RADIUS = 3;
      
      public static final int LAYOUT_CONSTRAINT_DIMENSION_RATIO = 44;
      
      public static final int LAYOUT_CONSTRAINT_END_TO_END_OF = 20;
      
      public static final int LAYOUT_CONSTRAINT_END_TO_START_OF = 19;
      
      public static final int LAYOUT_CONSTRAINT_GUIDE_BEGIN = 5;
      
      public static final int LAYOUT_CONSTRAINT_GUIDE_END = 6;
      
      public static final int LAYOUT_CONSTRAINT_GUIDE_PERCENT = 7;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT = 65;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT_DEFAULT = 32;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT_MAX = 37;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT_MIN = 36;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT_PERCENT = 38;
      
      public static final int LAYOUT_CONSTRAINT_HORIZONTAL_BIAS = 29;
      
      public static final int LAYOUT_CONSTRAINT_HORIZONTAL_CHAINSTYLE = 47;
      
      public static final int LAYOUT_CONSTRAINT_HORIZONTAL_WEIGHT = 45;
      
      public static final int LAYOUT_CONSTRAINT_LEFT_CREATOR = 39;
      
      public static final int LAYOUT_CONSTRAINT_LEFT_TO_LEFT_OF = 8;
      
      public static final int LAYOUT_CONSTRAINT_LEFT_TO_RIGHT_OF = 9;
      
      public static final int LAYOUT_CONSTRAINT_RIGHT_CREATOR = 41;
      
      public static final int LAYOUT_CONSTRAINT_RIGHT_TO_LEFT_OF = 10;
      
      public static final int LAYOUT_CONSTRAINT_RIGHT_TO_RIGHT_OF = 11;
      
      public static final int LAYOUT_CONSTRAINT_START_TO_END_OF = 17;
      
      public static final int LAYOUT_CONSTRAINT_START_TO_START_OF = 18;
      
      public static final int LAYOUT_CONSTRAINT_TAG = 51;
      
      public static final int LAYOUT_CONSTRAINT_TOP_CREATOR = 40;
      
      public static final int LAYOUT_CONSTRAINT_TOP_TO_BOTTOM_OF = 13;
      
      public static final int LAYOUT_CONSTRAINT_TOP_TO_TOP_OF = 12;
      
      public static final int LAYOUT_CONSTRAINT_VERTICAL_BIAS = 30;
      
      public static final int LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE = 48;
      
      public static final int LAYOUT_CONSTRAINT_VERTICAL_WEIGHT = 46;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH = 64;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH_DEFAULT = 31;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH_MAX = 34;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH_MIN = 33;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH_PERCENT = 35;
      
      public static final int LAYOUT_EDITOR_ABSOLUTEX = 49;
      
      public static final int LAYOUT_EDITOR_ABSOLUTEY = 50;
      
      public static final int LAYOUT_GONE_MARGIN_BASELINE = 55;
      
      public static final int LAYOUT_GONE_MARGIN_BOTTOM = 24;
      
      public static final int LAYOUT_GONE_MARGIN_END = 26;
      
      public static final int LAYOUT_GONE_MARGIN_LEFT = 21;
      
      public static final int LAYOUT_GONE_MARGIN_RIGHT = 23;
      
      public static final int LAYOUT_GONE_MARGIN_START = 25;
      
      public static final int LAYOUT_GONE_MARGIN_TOP = 22;
      
      public static final int LAYOUT_MARGIN_BASELINE = 54;
      
      public static final int LAYOUT_WRAP_BEHAVIOR_IN_PARENT = 66;
      
      public static final int UNUSED = 0;
      
      public static final SparseIntArray map;
      
      static {
        SparseIntArray sparseIntArray = new SparseIntArray();
        map = sparseIntArray;
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth, 64);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight, 65);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf, 8);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf, 9);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf, 10);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf, 11);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf, 12);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf, 13);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf, 14);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf, 15);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf, 16);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toTopOf, 52);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBottomOf, 53);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircle, 2);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleRadius, 3);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleAngle, 4);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX, 49);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY, 50);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin, 5);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end, 6);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent, 7);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_android_orientation, 1);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf, 17);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf, 18);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf, 19);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf, 20);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft, 21);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginTop, 22);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginRight, 23);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom, 24);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginStart, 25);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd, 26);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginBaseline, 55);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_marginBaseline, 54);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias, 29);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias, 30);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio, 44);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight, 45);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight, 46);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle, 47);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle, 48);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constrainedWidth, 27);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constrainedHeight, 28);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_default, 31);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_default, 32);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_min, 33);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_max, 34);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_percent, 35);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_min, 36);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_max, 37);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_percent, 38);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator, 39);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator, 40);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator, 41);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator, 42);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator, 43);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintTag, 51);
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_wrapBehaviorInParent, 66);
      }
    }
  }
  
  private static class Table {
    public static final int ANDROID_ORIENTATION = 1;
    
    public static final int LAYOUT_CONSTRAINED_HEIGHT = 28;
    
    public static final int LAYOUT_CONSTRAINED_WIDTH = 27;
    
    public static final int LAYOUT_CONSTRAINT_BASELINE_CREATOR = 43;
    
    public static final int LAYOUT_CONSTRAINT_BASELINE_TO_BASELINE_OF = 16;
    
    public static final int LAYOUT_CONSTRAINT_BASELINE_TO_BOTTOM_OF = 53;
    
    public static final int LAYOUT_CONSTRAINT_BASELINE_TO_TOP_OF = 52;
    
    public static final int LAYOUT_CONSTRAINT_BOTTOM_CREATOR = 42;
    
    public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_BOTTOM_OF = 15;
    
    public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_TOP_OF = 14;
    
    public static final int LAYOUT_CONSTRAINT_CIRCLE = 2;
    
    public static final int LAYOUT_CONSTRAINT_CIRCLE_ANGLE = 4;
    
    public static final int LAYOUT_CONSTRAINT_CIRCLE_RADIUS = 3;
    
    public static final int LAYOUT_CONSTRAINT_DIMENSION_RATIO = 44;
    
    public static final int LAYOUT_CONSTRAINT_END_TO_END_OF = 20;
    
    public static final int LAYOUT_CONSTRAINT_END_TO_START_OF = 19;
    
    public static final int LAYOUT_CONSTRAINT_GUIDE_BEGIN = 5;
    
    public static final int LAYOUT_CONSTRAINT_GUIDE_END = 6;
    
    public static final int LAYOUT_CONSTRAINT_GUIDE_PERCENT = 7;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT = 65;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT_DEFAULT = 32;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT_MAX = 37;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT_MIN = 36;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT_PERCENT = 38;
    
    public static final int LAYOUT_CONSTRAINT_HORIZONTAL_BIAS = 29;
    
    public static final int LAYOUT_CONSTRAINT_HORIZONTAL_CHAINSTYLE = 47;
    
    public static final int LAYOUT_CONSTRAINT_HORIZONTAL_WEIGHT = 45;
    
    public static final int LAYOUT_CONSTRAINT_LEFT_CREATOR = 39;
    
    public static final int LAYOUT_CONSTRAINT_LEFT_TO_LEFT_OF = 8;
    
    public static final int LAYOUT_CONSTRAINT_LEFT_TO_RIGHT_OF = 9;
    
    public static final int LAYOUT_CONSTRAINT_RIGHT_CREATOR = 41;
    
    public static final int LAYOUT_CONSTRAINT_RIGHT_TO_LEFT_OF = 10;
    
    public static final int LAYOUT_CONSTRAINT_RIGHT_TO_RIGHT_OF = 11;
    
    public static final int LAYOUT_CONSTRAINT_START_TO_END_OF = 17;
    
    public static final int LAYOUT_CONSTRAINT_START_TO_START_OF = 18;
    
    public static final int LAYOUT_CONSTRAINT_TAG = 51;
    
    public static final int LAYOUT_CONSTRAINT_TOP_CREATOR = 40;
    
    public static final int LAYOUT_CONSTRAINT_TOP_TO_BOTTOM_OF = 13;
    
    public static final int LAYOUT_CONSTRAINT_TOP_TO_TOP_OF = 12;
    
    public static final int LAYOUT_CONSTRAINT_VERTICAL_BIAS = 30;
    
    public static final int LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE = 48;
    
    public static final int LAYOUT_CONSTRAINT_VERTICAL_WEIGHT = 46;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH = 64;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH_DEFAULT = 31;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH_MAX = 34;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH_MIN = 33;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH_PERCENT = 35;
    
    public static final int LAYOUT_EDITOR_ABSOLUTEX = 49;
    
    public static final int LAYOUT_EDITOR_ABSOLUTEY = 50;
    
    public static final int LAYOUT_GONE_MARGIN_BASELINE = 55;
    
    public static final int LAYOUT_GONE_MARGIN_BOTTOM = 24;
    
    public static final int LAYOUT_GONE_MARGIN_END = 26;
    
    public static final int LAYOUT_GONE_MARGIN_LEFT = 21;
    
    public static final int LAYOUT_GONE_MARGIN_RIGHT = 23;
    
    public static final int LAYOUT_GONE_MARGIN_START = 25;
    
    public static final int LAYOUT_GONE_MARGIN_TOP = 22;
    
    public static final int LAYOUT_MARGIN_BASELINE = 54;
    
    public static final int LAYOUT_WRAP_BEHAVIOR_IN_PARENT = 66;
    
    public static final int UNUSED = 0;
    
    public static final SparseIntArray map;
    
    static {
      SparseIntArray sparseIntArray = new SparseIntArray();
      map = sparseIntArray;
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth, 64);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight, 65);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf, 8);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf, 9);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf, 10);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf, 11);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf, 12);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf, 13);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf, 14);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf, 15);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf, 16);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toTopOf, 52);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBottomOf, 53);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircle, 2);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleRadius, 3);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleAngle, 4);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX, 49);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY, 50);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin, 5);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end, 6);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent, 7);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_android_orientation, 1);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf, 17);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf, 18);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf, 19);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf, 20);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft, 21);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginTop, 22);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginRight, 23);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom, 24);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginStart, 25);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd, 26);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginBaseline, 55);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_marginBaseline, 54);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias, 29);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias, 30);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio, 44);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight, 45);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight, 46);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle, 47);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle, 48);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constrainedWidth, 27);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constrainedHeight, 28);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_default, 31);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_default, 32);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_min, 33);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_max, 34);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_percent, 35);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_min, 36);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_max, 37);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_percent, 38);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator, 39);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator, 40);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator, 41);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator, 42);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator, 43);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintTag, 51);
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_wrapBehaviorInParent, 66);
    }
  }
  
  class Measurer implements BasicMeasure.Measurer {
    ConstraintLayout layout;
    
    int layoutHeightSpec;
    
    int layoutWidthSpec;
    
    int paddingBottom;
    
    int paddingHeight;
    
    int paddingTop;
    
    int paddingWidth;
    
    final ConstraintLayout this$0;
    
    public Measurer(ConstraintLayout param1ConstraintLayout1) {
      this.layout = param1ConstraintLayout1;
    }
    
    private boolean isSimilarSpec(int param1Int1, int param1Int2, int param1Int3) {
      if (param1Int1 == param1Int2)
        return true; 
      int i = View.MeasureSpec.getMode(param1Int1);
      View.MeasureSpec.getSize(param1Int1);
      param1Int1 = View.MeasureSpec.getMode(param1Int2);
      param1Int2 = View.MeasureSpec.getSize(param1Int2);
      return (param1Int1 == 1073741824 && (i == Integer.MIN_VALUE || i == 0) && param1Int3 == param1Int2);
    }
    
    public void captureLayoutInfo(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6) {
      this.paddingTop = param1Int3;
      this.paddingBottom = param1Int4;
      this.paddingWidth = param1Int5;
      this.paddingHeight = param1Int6;
      this.layoutWidthSpec = param1Int1;
      this.layoutHeightSpec = param1Int2;
    }
    
    public final void didMeasures() {
      int i = this.layout.getChildCount();
      byte b;
      for (b = 0; b < i; b++) {
        View view = this.layout.getChildAt(b);
        if (view instanceof Placeholder)
          ((Placeholder)view).updatePostMeasure(this.layout); 
      } 
      i = this.layout.mConstraintHelpers.size();
      if (i > 0)
        for (b = 0; b < i; b++)
          ((ConstraintHelper)this.layout.mConstraintHelpers.get(b)).updatePostMeasure(this.layout);  
    }
    
    public final void measure(ConstraintWidget param1ConstraintWidget, BasicMeasure.Measure param1Measure) {
      // Byte code:
      //   0: aload_1
      //   1: ifnonnull -> 5
      //   4: return
      //   5: aload_1
      //   6: invokevirtual getVisibility : ()I
      //   9: bipush #8
      //   11: if_icmpne -> 37
      //   14: aload_1
      //   15: invokevirtual isInPlaceholder : ()Z
      //   18: ifne -> 37
      //   21: aload_2
      //   22: iconst_0
      //   23: putfield measuredWidth : I
      //   26: aload_2
      //   27: iconst_0
      //   28: putfield measuredHeight : I
      //   31: aload_2
      //   32: iconst_0
      //   33: putfield measuredBaseline : I
      //   36: return
      //   37: aload_1
      //   38: invokevirtual getParent : ()Landroidx/constraintlayout/core/widgets/ConstraintWidget;
      //   41: ifnonnull -> 45
      //   44: return
      //   45: aload_2
      //   46: getfield horizontalBehavior : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
      //   49: astore #19
      //   51: aload_2
      //   52: getfield verticalBehavior : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
      //   55: astore #21
      //   57: aload_2
      //   58: getfield horizontalDimension : I
      //   61: istore #7
      //   63: aload_2
      //   64: getfield verticalDimension : I
      //   67: istore #9
      //   69: iconst_0
      //   70: istore #4
      //   72: iconst_0
      //   73: istore #6
      //   75: aload_0
      //   76: getfield paddingTop : I
      //   79: aload_0
      //   80: getfield paddingBottom : I
      //   83: iadd
      //   84: istore #8
      //   86: aload_0
      //   87: getfield paddingWidth : I
      //   90: istore #5
      //   92: aload_1
      //   93: invokevirtual getCompanionWidget : ()Ljava/lang/Object;
      //   96: checkcast android/view/View
      //   99: astore #18
      //   101: getstatic androidx/constraintlayout/widget/ConstraintLayout$1.$SwitchMap$androidx$constraintlayout$core$widgets$ConstraintWidget$DimensionBehaviour : [I
      //   104: aload #19
      //   106: invokevirtual ordinal : ()I
      //   109: iaload
      //   110: tableswitch default -> 140, 1 -> 332, 2 -> 316, 3 -> 296, 4 -> 143
      //   140: goto -> 341
      //   143: aload_0
      //   144: getfield layoutWidthSpec : I
      //   147: iload #5
      //   149: bipush #-2
      //   151: invokestatic getChildMeasureSpec : (III)I
      //   154: istore #7
      //   156: aload_1
      //   157: getfield mMatchConstraintDefaultWidth : I
      //   160: iconst_1
      //   161: if_icmpne -> 170
      //   164: iconst_1
      //   165: istore #5
      //   167: goto -> 173
      //   170: iconst_0
      //   171: istore #5
      //   173: aload_2
      //   174: getfield measureStrategy : I
      //   177: getstatic androidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measure.TRY_GIVEN_DIMENSIONS : I
      //   180: if_icmpeq -> 197
      //   183: iload #7
      //   185: istore #4
      //   187: aload_2
      //   188: getfield measureStrategy : I
      //   191: getstatic androidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measure.USE_GIVEN_DIMENSIONS : I
      //   194: if_icmpne -> 341
      //   197: aload #18
      //   199: invokevirtual getMeasuredHeight : ()I
      //   202: aload_1
      //   203: invokevirtual getHeight : ()I
      //   206: if_icmpne -> 215
      //   209: iconst_1
      //   210: istore #4
      //   212: goto -> 218
      //   215: iconst_0
      //   216: istore #4
      //   218: aload_2
      //   219: getfield measureStrategy : I
      //   222: getstatic androidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measure.USE_GIVEN_DIMENSIONS : I
      //   225: if_icmpeq -> 267
      //   228: iload #5
      //   230: ifeq -> 267
      //   233: iload #5
      //   235: ifeq -> 243
      //   238: iload #4
      //   240: ifne -> 267
      //   243: aload #18
      //   245: instanceof androidx/constraintlayout/widget/Placeholder
      //   248: ifne -> 267
      //   251: aload_1
      //   252: invokevirtual isResolvedHorizontally : ()Z
      //   255: ifeq -> 261
      //   258: goto -> 267
      //   261: iconst_0
      //   262: istore #4
      //   264: goto -> 270
      //   267: iconst_1
      //   268: istore #4
      //   270: iload #4
      //   272: ifeq -> 289
      //   275: aload_1
      //   276: invokevirtual getWidth : ()I
      //   279: ldc 1073741824
      //   281: invokestatic makeMeasureSpec : (II)I
      //   284: istore #4
      //   286: goto -> 341
      //   289: iload #7
      //   291: istore #4
      //   293: goto -> 341
      //   296: aload_0
      //   297: getfield layoutWidthSpec : I
      //   300: aload_1
      //   301: invokevirtual getHorizontalMargin : ()I
      //   304: iload #5
      //   306: iadd
      //   307: iconst_m1
      //   308: invokestatic getChildMeasureSpec : (III)I
      //   311: istore #4
      //   313: goto -> 341
      //   316: aload_0
      //   317: getfield layoutWidthSpec : I
      //   320: iload #5
      //   322: bipush #-2
      //   324: invokestatic getChildMeasureSpec : (III)I
      //   327: istore #4
      //   329: goto -> 341
      //   332: iload #7
      //   334: ldc 1073741824
      //   336: invokestatic makeMeasureSpec : (II)I
      //   339: istore #4
      //   341: getstatic androidx/constraintlayout/widget/ConstraintLayout$1.$SwitchMap$androidx$constraintlayout$core$widgets$ConstraintWidget$DimensionBehaviour : [I
      //   344: aload #21
      //   346: invokevirtual ordinal : ()I
      //   349: iaload
      //   350: tableswitch default -> 380, 1 -> 576, 2 -> 560, 3 -> 540, 4 -> 387
      //   380: iload #6
      //   382: istore #5
      //   384: goto -> 585
      //   387: aload_0
      //   388: getfield layoutHeightSpec : I
      //   391: iload #8
      //   393: bipush #-2
      //   395: invokestatic getChildMeasureSpec : (III)I
      //   398: istore #7
      //   400: aload_1
      //   401: getfield mMatchConstraintDefaultHeight : I
      //   404: iconst_1
      //   405: if_icmpne -> 414
      //   408: iconst_1
      //   409: istore #6
      //   411: goto -> 417
      //   414: iconst_0
      //   415: istore #6
      //   417: aload_2
      //   418: getfield measureStrategy : I
      //   421: getstatic androidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measure.TRY_GIVEN_DIMENSIONS : I
      //   424: if_icmpeq -> 441
      //   427: iload #7
      //   429: istore #5
      //   431: aload_2
      //   432: getfield measureStrategy : I
      //   435: getstatic androidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measure.USE_GIVEN_DIMENSIONS : I
      //   438: if_icmpne -> 585
      //   441: aload #18
      //   443: invokevirtual getMeasuredWidth : ()I
      //   446: aload_1
      //   447: invokevirtual getWidth : ()I
      //   450: if_icmpne -> 459
      //   453: iconst_1
      //   454: istore #5
      //   456: goto -> 462
      //   459: iconst_0
      //   460: istore #5
      //   462: aload_2
      //   463: getfield measureStrategy : I
      //   466: getstatic androidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measure.USE_GIVEN_DIMENSIONS : I
      //   469: if_icmpeq -> 511
      //   472: iload #6
      //   474: ifeq -> 511
      //   477: iload #6
      //   479: ifeq -> 487
      //   482: iload #5
      //   484: ifne -> 511
      //   487: aload #18
      //   489: instanceof androidx/constraintlayout/widget/Placeholder
      //   492: ifne -> 511
      //   495: aload_1
      //   496: invokevirtual isResolvedVertically : ()Z
      //   499: ifeq -> 505
      //   502: goto -> 511
      //   505: iconst_0
      //   506: istore #5
      //   508: goto -> 514
      //   511: iconst_1
      //   512: istore #5
      //   514: iload #5
      //   516: ifeq -> 533
      //   519: aload_1
      //   520: invokevirtual getHeight : ()I
      //   523: ldc 1073741824
      //   525: invokestatic makeMeasureSpec : (II)I
      //   528: istore #5
      //   530: goto -> 585
      //   533: iload #7
      //   535: istore #5
      //   537: goto -> 585
      //   540: aload_0
      //   541: getfield layoutHeightSpec : I
      //   544: aload_1
      //   545: invokevirtual getVerticalMargin : ()I
      //   548: iload #8
      //   550: iadd
      //   551: iconst_m1
      //   552: invokestatic getChildMeasureSpec : (III)I
      //   555: istore #5
      //   557: goto -> 585
      //   560: aload_0
      //   561: getfield layoutHeightSpec : I
      //   564: iload #8
      //   566: bipush #-2
      //   568: invokestatic getChildMeasureSpec : (III)I
      //   571: istore #5
      //   573: goto -> 585
      //   576: iload #9
      //   578: ldc 1073741824
      //   580: invokestatic makeMeasureSpec : (II)I
      //   583: istore #5
      //   585: aload_1
      //   586: invokevirtual getParent : ()Landroidx/constraintlayout/core/widgets/ConstraintWidget;
      //   589: checkcast androidx/constraintlayout/core/widgets/ConstraintWidgetContainer
      //   592: astore #20
      //   594: aload #20
      //   596: ifnull -> 757
      //   599: aload_0
      //   600: getfield this$0 : Landroidx/constraintlayout/widget/ConstraintLayout;
      //   603: invokestatic access$000 : (Landroidx/constraintlayout/widget/ConstraintLayout;)I
      //   606: sipush #256
      //   609: invokestatic enabled : (II)Z
      //   612: ifeq -> 757
      //   615: aload #18
      //   617: invokevirtual getMeasuredWidth : ()I
      //   620: aload_1
      //   621: invokevirtual getWidth : ()I
      //   624: if_icmpne -> 757
      //   627: aload #18
      //   629: invokevirtual getMeasuredWidth : ()I
      //   632: aload #20
      //   634: invokevirtual getWidth : ()I
      //   637: if_icmpge -> 757
      //   640: aload #18
      //   642: invokevirtual getMeasuredHeight : ()I
      //   645: aload_1
      //   646: invokevirtual getHeight : ()I
      //   649: if_icmpne -> 757
      //   652: aload #18
      //   654: invokevirtual getMeasuredHeight : ()I
      //   657: aload #20
      //   659: invokevirtual getHeight : ()I
      //   662: if_icmpge -> 757
      //   665: aload #18
      //   667: invokevirtual getBaseline : ()I
      //   670: aload_1
      //   671: invokevirtual getBaselineDistance : ()I
      //   674: if_icmpne -> 757
      //   677: aload_1
      //   678: invokevirtual isMeasureRequested : ()Z
      //   681: ifne -> 757
      //   684: aload_0
      //   685: aload_1
      //   686: invokevirtual getLastHorizontalMeasureSpec : ()I
      //   689: iload #4
      //   691: aload_1
      //   692: invokevirtual getWidth : ()I
      //   695: invokespecial isSimilarSpec : (III)Z
      //   698: ifeq -> 724
      //   701: aload_0
      //   702: aload_1
      //   703: invokevirtual getLastVerticalMeasureSpec : ()I
      //   706: iload #5
      //   708: aload_1
      //   709: invokevirtual getHeight : ()I
      //   712: invokespecial isSimilarSpec : (III)Z
      //   715: ifeq -> 724
      //   718: iconst_1
      //   719: istore #6
      //   721: goto -> 727
      //   724: iconst_0
      //   725: istore #6
      //   727: iload #6
      //   729: ifeq -> 757
      //   732: aload_2
      //   733: aload_1
      //   734: invokevirtual getWidth : ()I
      //   737: putfield measuredWidth : I
      //   740: aload_2
      //   741: aload_1
      //   742: invokevirtual getHeight : ()I
      //   745: putfield measuredHeight : I
      //   748: aload_2
      //   749: aload_1
      //   750: invokevirtual getBaselineDistance : ()I
      //   753: putfield measuredBaseline : I
      //   756: return
      //   757: aload #19
      //   759: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
      //   762: if_acmpne -> 771
      //   765: iconst_1
      //   766: istore #6
      //   768: goto -> 774
      //   771: iconst_0
      //   772: istore #6
      //   774: aload #21
      //   776: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
      //   779: if_acmpne -> 788
      //   782: iconst_1
      //   783: istore #7
      //   785: goto -> 791
      //   788: iconst_0
      //   789: istore #7
      //   791: aload #21
      //   793: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_PARENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
      //   796: if_acmpeq -> 816
      //   799: aload #21
      //   801: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
      //   804: if_acmpne -> 810
      //   807: goto -> 816
      //   810: iconst_0
      //   811: istore #9
      //   813: goto -> 819
      //   816: iconst_1
      //   817: istore #9
      //   819: aload #19
      //   821: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_PARENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
      //   824: if_acmpeq -> 844
      //   827: aload #19
      //   829: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
      //   832: if_acmpne -> 838
      //   835: goto -> 844
      //   838: iconst_0
      //   839: istore #10
      //   841: goto -> 847
      //   844: iconst_1
      //   845: istore #10
      //   847: iload #6
      //   849: ifeq -> 867
      //   852: aload_1
      //   853: getfield mDimensionRatio : F
      //   856: fconst_0
      //   857: fcmpl
      //   858: ifle -> 867
      //   861: iconst_1
      //   862: istore #11
      //   864: goto -> 870
      //   867: iconst_0
      //   868: istore #11
      //   870: iload #7
      //   872: ifeq -> 890
      //   875: aload_1
      //   876: getfield mDimensionRatio : F
      //   879: fconst_0
      //   880: fcmpl
      //   881: ifle -> 890
      //   884: iconst_1
      //   885: istore #12
      //   887: goto -> 893
      //   890: iconst_0
      //   891: istore #12
      //   893: aload #18
      //   895: ifnonnull -> 899
      //   898: return
      //   899: aload #18
      //   901: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
      //   904: checkcast androidx/constraintlayout/widget/ConstraintLayout$LayoutParams
      //   907: astore #19
      //   909: aload_2
      //   910: getfield measureStrategy : I
      //   913: getstatic androidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measure.TRY_GIVEN_DIMENSIONS : I
      //   916: if_icmpeq -> 968
      //   919: aload_2
      //   920: getfield measureStrategy : I
      //   923: getstatic androidx/constraintlayout/core/widgets/analyzer/BasicMeasure$Measure.USE_GIVEN_DIMENSIONS : I
      //   926: if_icmpeq -> 968
      //   929: iload #6
      //   931: ifeq -> 968
      //   934: aload_1
      //   935: getfield mMatchConstraintDefaultWidth : I
      //   938: ifne -> 968
      //   941: iload #7
      //   943: ifeq -> 968
      //   946: aload_1
      //   947: getfield mMatchConstraintDefaultHeight : I
      //   950: ifeq -> 956
      //   953: goto -> 968
      //   956: iconst_0
      //   957: istore #4
      //   959: iconst_0
      //   960: istore #7
      //   962: iconst_0
      //   963: istore #5
      //   965: goto -> 1348
      //   968: aload #18
      //   970: instanceof androidx/constraintlayout/widget/VirtualLayout
      //   973: ifeq -> 1006
      //   976: aload_1
      //   977: instanceof androidx/constraintlayout/core/widgets/VirtualLayout
      //   980: ifeq -> 1006
      //   983: aload_1
      //   984: checkcast androidx/constraintlayout/core/widgets/VirtualLayout
      //   987: astore #20
      //   989: aload #18
      //   991: checkcast androidx/constraintlayout/widget/VirtualLayout
      //   994: aload #20
      //   996: iload #4
      //   998: iload #5
      //   1000: invokevirtual onMeasure : (Landroidx/constraintlayout/core/widgets/VirtualLayout;II)V
      //   1003: goto -> 1015
      //   1006: aload #18
      //   1008: iload #4
      //   1010: iload #5
      //   1012: invokevirtual measure : (II)V
      //   1015: aload_1
      //   1016: iload #4
      //   1018: iload #5
      //   1020: invokevirtual setLastMeasureSpec : (II)V
      //   1023: aload #18
      //   1025: invokevirtual getMeasuredWidth : ()I
      //   1028: istore #15
      //   1030: aload #18
      //   1032: invokevirtual getMeasuredHeight : ()I
      //   1035: istore #13
      //   1037: aload #18
      //   1039: invokevirtual getBaseline : ()I
      //   1042: istore #14
      //   1044: aload_1
      //   1045: getfield mMatchConstraintMinWidth : I
      //   1048: ifle -> 1065
      //   1051: aload_1
      //   1052: getfield mMatchConstraintMinWidth : I
      //   1055: iload #15
      //   1057: invokestatic max : (II)I
      //   1060: istore #7
      //   1062: goto -> 1069
      //   1065: iload #15
      //   1067: istore #7
      //   1069: iload #7
      //   1071: istore #6
      //   1073: aload_1
      //   1074: getfield mMatchConstraintMaxWidth : I
      //   1077: ifle -> 1091
      //   1080: aload_1
      //   1081: getfield mMatchConstraintMaxWidth : I
      //   1084: iload #7
      //   1086: invokestatic min : (II)I
      //   1089: istore #6
      //   1091: aload_1
      //   1092: getfield mMatchConstraintMinHeight : I
      //   1095: ifle -> 1112
      //   1098: aload_1
      //   1099: getfield mMatchConstraintMinHeight : I
      //   1102: iload #13
      //   1104: invokestatic max : (II)I
      //   1107: istore #7
      //   1109: goto -> 1116
      //   1112: iload #13
      //   1114: istore #7
      //   1116: iload #7
      //   1118: istore #8
      //   1120: aload_1
      //   1121: getfield mMatchConstraintMaxHeight : I
      //   1124: ifle -> 1138
      //   1127: aload_1
      //   1128: getfield mMatchConstraintMaxHeight : I
      //   1131: iload #7
      //   1133: invokestatic min : (II)I
      //   1136: istore #8
      //   1138: aload_0
      //   1139: getfield this$0 : Landroidx/constraintlayout/widget/ConstraintLayout;
      //   1142: invokestatic access$000 : (Landroidx/constraintlayout/widget/ConstraintLayout;)I
      //   1145: iconst_1
      //   1146: invokestatic enabled : (II)Z
      //   1149: ifne -> 1236
      //   1152: iload #11
      //   1154: ifeq -> 1186
      //   1157: iload #9
      //   1159: ifeq -> 1186
      //   1162: aload_1
      //   1163: getfield mDimensionRatio : F
      //   1166: fstore_3
      //   1167: iload #8
      //   1169: i2f
      //   1170: fload_3
      //   1171: fmul
      //   1172: ldc_w 0.5
      //   1175: fadd
      //   1176: f2i
      //   1177: istore #9
      //   1179: iload #8
      //   1181: istore #7
      //   1183: goto -> 1244
      //   1186: iload #6
      //   1188: istore #9
      //   1190: iload #8
      //   1192: istore #7
      //   1194: iload #12
      //   1196: ifeq -> 1244
      //   1199: iload #6
      //   1201: istore #9
      //   1203: iload #8
      //   1205: istore #7
      //   1207: iload #10
      //   1209: ifeq -> 1244
      //   1212: aload_1
      //   1213: getfield mDimensionRatio : F
      //   1216: fstore_3
      //   1217: iload #6
      //   1219: i2f
      //   1220: fload_3
      //   1221: fdiv
      //   1222: ldc_w 0.5
      //   1225: fadd
      //   1226: f2i
      //   1227: istore #7
      //   1229: iload #6
      //   1231: istore #9
      //   1233: goto -> 1244
      //   1236: iload #8
      //   1238: istore #7
      //   1240: iload #6
      //   1242: istore #9
      //   1244: iload #15
      //   1246: iload #9
      //   1248: if_icmpne -> 1272
      //   1251: iload #13
      //   1253: iload #7
      //   1255: if_icmpeq -> 1261
      //   1258: goto -> 1272
      //   1261: iload #9
      //   1263: istore #4
      //   1265: iload #14
      //   1267: istore #5
      //   1269: goto -> 1348
      //   1272: iload #15
      //   1274: iload #9
      //   1276: if_icmpeq -> 1291
      //   1279: iload #9
      //   1281: ldc 1073741824
      //   1283: invokestatic makeMeasureSpec : (II)I
      //   1286: istore #4
      //   1288: goto -> 1291
      //   1291: iload #13
      //   1293: iload #7
      //   1295: if_icmpeq -> 1310
      //   1298: iload #7
      //   1300: ldc 1073741824
      //   1302: invokestatic makeMeasureSpec : (II)I
      //   1305: istore #5
      //   1307: goto -> 1310
      //   1310: aload #18
      //   1312: iload #4
      //   1314: iload #5
      //   1316: invokevirtual measure : (II)V
      //   1319: aload_1
      //   1320: iload #4
      //   1322: iload #5
      //   1324: invokevirtual setLastMeasureSpec : (II)V
      //   1327: aload #18
      //   1329: invokevirtual getMeasuredWidth : ()I
      //   1332: istore #4
      //   1334: aload #18
      //   1336: invokevirtual getMeasuredHeight : ()I
      //   1339: istore #7
      //   1341: aload #18
      //   1343: invokevirtual getBaseline : ()I
      //   1346: istore #5
      //   1348: iload #5
      //   1350: iconst_m1
      //   1351: if_icmpeq -> 1360
      //   1354: iconst_1
      //   1355: istore #16
      //   1357: goto -> 1363
      //   1360: iconst_0
      //   1361: istore #16
      //   1363: iload #4
      //   1365: aload_2
      //   1366: getfield horizontalDimension : I
      //   1369: if_icmpne -> 1390
      //   1372: iload #7
      //   1374: aload_2
      //   1375: getfield verticalDimension : I
      //   1378: if_icmpeq -> 1384
      //   1381: goto -> 1390
      //   1384: iconst_0
      //   1385: istore #17
      //   1387: goto -> 1393
      //   1390: iconst_1
      //   1391: istore #17
      //   1393: aload_2
      //   1394: iload #17
      //   1396: putfield measuredNeedsSolverPass : Z
      //   1399: aload #19
      //   1401: getfield needsBaseline : Z
      //   1404: ifeq -> 1410
      //   1407: iconst_1
      //   1408: istore #16
      //   1410: iload #16
      //   1412: ifeq -> 1435
      //   1415: iload #5
      //   1417: iconst_m1
      //   1418: if_icmpeq -> 1435
      //   1421: aload_1
      //   1422: invokevirtual getBaselineDistance : ()I
      //   1425: iload #5
      //   1427: if_icmpeq -> 1435
      //   1430: aload_2
      //   1431: iconst_1
      //   1432: putfield measuredNeedsSolverPass : Z
      //   1435: aload_2
      //   1436: iload #4
      //   1438: putfield measuredWidth : I
      //   1441: aload_2
      //   1442: iload #7
      //   1444: putfield measuredHeight : I
      //   1447: aload_2
      //   1448: iload #16
      //   1450: putfield measuredHasBaseline : Z
      //   1453: aload_2
      //   1454: iload #5
      //   1456: putfield measuredBaseline : I
      //   1459: return
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\widget\ConstraintLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */