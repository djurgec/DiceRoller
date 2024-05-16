package androidx.constraintlayout.core.widgets;

import androidx.constraintlayout.core.LinearSystem;
import java.util.ArrayList;
import java.util.HashMap;

public class Flow extends VirtualLayout {
  public static final int HORIZONTAL_ALIGN_CENTER = 2;
  
  public static final int HORIZONTAL_ALIGN_END = 1;
  
  public static final int HORIZONTAL_ALIGN_START = 0;
  
  public static final int VERTICAL_ALIGN_BASELINE = 3;
  
  public static final int VERTICAL_ALIGN_BOTTOM = 1;
  
  public static final int VERTICAL_ALIGN_CENTER = 2;
  
  public static final int VERTICAL_ALIGN_TOP = 0;
  
  public static final int WRAP_ALIGNED = 2;
  
  public static final int WRAP_CHAIN = 1;
  
  public static final int WRAP_NONE = 0;
  
  private ConstraintWidget[] mAlignedBiggestElementsInCols = null;
  
  private ConstraintWidget[] mAlignedBiggestElementsInRows = null;
  
  private int[] mAlignedDimensions = null;
  
  private ArrayList<WidgetsList> mChainList = new ArrayList<>();
  
  private ConstraintWidget[] mDisplayedWidgets;
  
  private int mDisplayedWidgetsCount = 0;
  
  private float mFirstHorizontalBias = 0.5F;
  
  private int mFirstHorizontalStyle = -1;
  
  private float mFirstVerticalBias = 0.5F;
  
  private int mFirstVerticalStyle = -1;
  
  private int mHorizontalAlign = 2;
  
  private float mHorizontalBias = 0.5F;
  
  private int mHorizontalGap = 0;
  
  private int mHorizontalStyle = -1;
  
  private float mLastHorizontalBias = 0.5F;
  
  private int mLastHorizontalStyle = -1;
  
  private float mLastVerticalBias = 0.5F;
  
  private int mLastVerticalStyle = -1;
  
  private int mMaxElementsWrap = -1;
  
  private int mOrientation = 0;
  
  private int mVerticalAlign = 2;
  
  private float mVerticalBias = 0.5F;
  
  private int mVerticalGap = 0;
  
  private int mVerticalStyle = -1;
  
  private int mWrapMode = 0;
  
  private void createAlignedConstraints(boolean paramBoolean) {
    ConstraintWidget constraintWidget;
    if (this.mAlignedDimensions == null || this.mAlignedBiggestElementsInCols == null || this.mAlignedBiggestElementsInRows == null)
      return; 
    byte b;
    for (b = 0; b < this.mDisplayedWidgetsCount; b++)
      this.mDisplayedWidgets[b].resetAnchors(); 
    int[] arrayOfInt = this.mAlignedDimensions;
    int j = arrayOfInt[0];
    int i = arrayOfInt[1];
    arrayOfInt = null;
    float f = this.mHorizontalBias;
    b = 0;
    while (b < j) {
      ConstraintWidget constraintWidget1;
      int k = b;
      if (paramBoolean) {
        k = j - b - 1;
        f = 1.0F - this.mHorizontalBias;
      } 
      ConstraintWidget constraintWidget2 = this.mAlignedBiggestElementsInCols[k];
      int[] arrayOfInt1 = arrayOfInt;
      if (constraintWidget2 != null)
        if (constraintWidget2.getVisibility() == 8) {
          arrayOfInt1 = arrayOfInt;
        } else {
          if (b == 0) {
            constraintWidget2.connect(constraintWidget2.mLeft, this.mLeft, getPaddingLeft());
            constraintWidget2.setHorizontalChainStyle(this.mHorizontalStyle);
            constraintWidget2.setHorizontalBiasPercent(f);
          } 
          if (b == j - 1)
            constraintWidget2.connect(constraintWidget2.mRight, this.mRight, getPaddingRight()); 
          if (b > 0 && arrayOfInt != null) {
            constraintWidget2.connect(constraintWidget2.mLeft, ((ConstraintWidget)arrayOfInt).mRight, this.mHorizontalGap);
            arrayOfInt.connect(((ConstraintWidget)arrayOfInt).mRight, constraintWidget2.mLeft, 0);
          } 
          constraintWidget1 = constraintWidget2;
        }  
      b++;
      constraintWidget = constraintWidget1;
    } 
    b = 0;
    while (b < i) {
      ConstraintWidget constraintWidget2 = this.mAlignedBiggestElementsInRows[b];
      ConstraintWidget constraintWidget1 = constraintWidget;
      if (constraintWidget2 != null)
        if (constraintWidget2.getVisibility() == 8) {
          constraintWidget1 = constraintWidget;
        } else {
          if (b == 0) {
            constraintWidget2.connect(constraintWidget2.mTop, this.mTop, getPaddingTop());
            constraintWidget2.setVerticalChainStyle(this.mVerticalStyle);
            constraintWidget2.setVerticalBiasPercent(this.mVerticalBias);
          } 
          if (b == i - 1)
            constraintWidget2.connect(constraintWidget2.mBottom, this.mBottom, getPaddingBottom()); 
          if (b > 0 && constraintWidget != null) {
            constraintWidget2.connect(constraintWidget2.mTop, constraintWidget.mBottom, this.mVerticalGap);
            constraintWidget.connect(constraintWidget.mBottom, constraintWidget2.mTop, 0);
          } 
          constraintWidget1 = constraintWidget2;
        }  
      b++;
      constraintWidget = constraintWidget1;
    } 
    for (b = 0; b < j; b++) {
      for (byte b1 = 0; b1 < i; b1++) {
        int k = b1 * j + b;
        if (this.mOrientation == 1)
          k = b * i + b1; 
        ConstraintWidget[] arrayOfConstraintWidget = this.mDisplayedWidgets;
        if (k < arrayOfConstraintWidget.length) {
          ConstraintWidget constraintWidget1 = arrayOfConstraintWidget[k];
          if (constraintWidget1 != null && constraintWidget1.getVisibility() != 8) {
            ConstraintWidget constraintWidget2 = this.mAlignedBiggestElementsInCols[b];
            ConstraintWidget constraintWidget3 = this.mAlignedBiggestElementsInRows[b1];
            if (constraintWidget1 != constraintWidget2) {
              constraintWidget1.connect(constraintWidget1.mLeft, constraintWidget2.mLeft, 0);
              constraintWidget1.connect(constraintWidget1.mRight, constraintWidget2.mRight, 0);
            } 
            if (constraintWidget1 != constraintWidget3) {
              constraintWidget1.connect(constraintWidget1.mTop, constraintWidget3.mTop, 0);
              constraintWidget1.connect(constraintWidget1.mBottom, constraintWidget3.mBottom, 0);
            } 
          } 
        } 
      } 
    } 
  }
  
  private final int getWidgetHeight(ConstraintWidget paramConstraintWidget, int paramInt) {
    if (paramConstraintWidget == null)
      return 0; 
    if (paramConstraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
      if (paramConstraintWidget.mMatchConstraintDefaultHeight == 0)
        return 0; 
      if (paramConstraintWidget.mMatchConstraintDefaultHeight == 2) {
        paramInt = (int)(paramConstraintWidget.mMatchConstraintPercentHeight * paramInt);
        if (paramInt != paramConstraintWidget.getHeight()) {
          paramConstraintWidget.setMeasureRequested(true);
          measure(paramConstraintWidget, paramConstraintWidget.getHorizontalDimensionBehaviour(), paramConstraintWidget.getWidth(), ConstraintWidget.DimensionBehaviour.FIXED, paramInt);
        } 
        return paramInt;
      } 
      if (paramConstraintWidget.mMatchConstraintDefaultHeight == 1)
        return paramConstraintWidget.getHeight(); 
      if (paramConstraintWidget.mMatchConstraintDefaultHeight == 3)
        return (int)(paramConstraintWidget.getWidth() * paramConstraintWidget.mDimensionRatio + 0.5F); 
    } 
    return paramConstraintWidget.getHeight();
  }
  
  private final int getWidgetWidth(ConstraintWidget paramConstraintWidget, int paramInt) {
    if (paramConstraintWidget == null)
      return 0; 
    if (paramConstraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
      if (paramConstraintWidget.mMatchConstraintDefaultWidth == 0)
        return 0; 
      if (paramConstraintWidget.mMatchConstraintDefaultWidth == 2) {
        paramInt = (int)(paramConstraintWidget.mMatchConstraintPercentWidth * paramInt);
        if (paramInt != paramConstraintWidget.getWidth()) {
          paramConstraintWidget.setMeasureRequested(true);
          measure(paramConstraintWidget, ConstraintWidget.DimensionBehaviour.FIXED, paramInt, paramConstraintWidget.getVerticalDimensionBehaviour(), paramConstraintWidget.getHeight());
        } 
        return paramInt;
      } 
      if (paramConstraintWidget.mMatchConstraintDefaultWidth == 1)
        return paramConstraintWidget.getWidth(); 
      if (paramConstraintWidget.mMatchConstraintDefaultWidth == 3)
        return (int)(paramConstraintWidget.getHeight() * paramConstraintWidget.mDimensionRatio + 0.5F); 
    } 
    return paramConstraintWidget.getWidth();
  }
  
  private void measureAligned(ConstraintWidget[] paramArrayOfConstraintWidget, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfint) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #11
    //   3: iconst_0
    //   4: istore #10
    //   6: iconst_0
    //   7: istore #12
    //   9: iload_3
    //   10: ifne -> 122
    //   13: aload_0
    //   14: getfield mMaxElementsWrap : I
    //   17: istore #8
    //   19: iload #10
    //   21: istore #6
    //   23: iload #8
    //   25: istore #7
    //   27: iload #8
    //   29: ifgt -> 240
    //   32: iconst_0
    //   33: istore #6
    //   35: iconst_0
    //   36: istore #7
    //   38: iconst_0
    //   39: istore #9
    //   41: iload #9
    //   43: iload_2
    //   44: if_icmpge -> 115
    //   47: iload #6
    //   49: istore #8
    //   51: iload #9
    //   53: ifle -> 65
    //   56: iload #6
    //   58: aload_0
    //   59: getfield mHorizontalGap : I
    //   62: iadd
    //   63: istore #8
    //   65: aload_1
    //   66: iload #9
    //   68: aaload
    //   69: astore #13
    //   71: aload #13
    //   73: ifnonnull -> 83
    //   76: iload #8
    //   78: istore #6
    //   80: goto -> 109
    //   83: iload #8
    //   85: aload_0
    //   86: aload #13
    //   88: iload #4
    //   90: invokespecial getWidgetWidth : (Landroidx/constraintlayout/core/widgets/ConstraintWidget;I)I
    //   93: iadd
    //   94: istore #6
    //   96: iload #6
    //   98: iload #4
    //   100: if_icmple -> 106
    //   103: goto -> 115
    //   106: iinc #7, 1
    //   109: iinc #9, 1
    //   112: goto -> 41
    //   115: iload #10
    //   117: istore #6
    //   119: goto -> 240
    //   122: aload_0
    //   123: getfield mMaxElementsWrap : I
    //   126: istore #8
    //   128: iload #8
    //   130: istore #6
    //   132: iload #12
    //   134: istore #7
    //   136: iload #8
    //   138: ifgt -> 240
    //   141: iconst_0
    //   142: istore #9
    //   144: iconst_0
    //   145: istore #8
    //   147: iconst_0
    //   148: istore #10
    //   150: iload #8
    //   152: istore #6
    //   154: iload #12
    //   156: istore #7
    //   158: iload #10
    //   160: iload_2
    //   161: if_icmpge -> 240
    //   164: iload #9
    //   166: istore #6
    //   168: iload #10
    //   170: ifle -> 182
    //   173: iload #9
    //   175: aload_0
    //   176: getfield mVerticalGap : I
    //   179: iadd
    //   180: istore #6
    //   182: aload_1
    //   183: iload #10
    //   185: aaload
    //   186: astore #13
    //   188: aload #13
    //   190: ifnonnull -> 200
    //   193: iload #6
    //   195: istore #9
    //   197: goto -> 234
    //   200: iload #6
    //   202: aload_0
    //   203: aload #13
    //   205: iload #4
    //   207: invokespecial getWidgetHeight : (Landroidx/constraintlayout/core/widgets/ConstraintWidget;I)I
    //   210: iadd
    //   211: istore #9
    //   213: iload #9
    //   215: iload #4
    //   217: if_icmple -> 231
    //   220: iload #8
    //   222: istore #6
    //   224: iload #12
    //   226: istore #7
    //   228: goto -> 240
    //   231: iinc #8, 1
    //   234: iinc #10, 1
    //   237: goto -> 150
    //   240: aload_0
    //   241: getfield mAlignedDimensions : [I
    //   244: ifnonnull -> 254
    //   247: aload_0
    //   248: iconst_2
    //   249: newarray int
    //   251: putfield mAlignedDimensions : [I
    //   254: iload #6
    //   256: ifne -> 264
    //   259: iload_3
    //   260: iconst_1
    //   261: if_icmpeq -> 297
    //   264: iload #11
    //   266: istore #8
    //   268: iload #6
    //   270: istore #9
    //   272: iload #7
    //   274: istore #10
    //   276: iload #7
    //   278: ifne -> 308
    //   281: iload #11
    //   283: istore #8
    //   285: iload #6
    //   287: istore #9
    //   289: iload #7
    //   291: istore #10
    //   293: iload_3
    //   294: ifne -> 308
    //   297: iconst_1
    //   298: istore #8
    //   300: iload #7
    //   302: istore #10
    //   304: iload #6
    //   306: istore #9
    //   308: iload #8
    //   310: ifne -> 841
    //   313: iload_3
    //   314: ifne -> 337
    //   317: iload_2
    //   318: i2f
    //   319: iload #10
    //   321: i2f
    //   322: fdiv
    //   323: f2d
    //   324: invokestatic ceil : (D)D
    //   327: d2i
    //   328: istore #9
    //   330: iload #10
    //   332: istore #7
    //   334: goto -> 350
    //   337: iload_2
    //   338: i2f
    //   339: iload #9
    //   341: i2f
    //   342: fdiv
    //   343: f2d
    //   344: invokestatic ceil : (D)D
    //   347: d2i
    //   348: istore #7
    //   350: aload_0
    //   351: getfield mAlignedBiggestElementsInCols : [Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   354: astore #13
    //   356: aload #13
    //   358: ifnull -> 381
    //   361: aload #13
    //   363: arraylength
    //   364: iload #7
    //   366: if_icmpge -> 372
    //   369: goto -> 381
    //   372: aload #13
    //   374: aconst_null
    //   375: invokestatic fill : ([Ljava/lang/Object;Ljava/lang/Object;)V
    //   378: goto -> 390
    //   381: aload_0
    //   382: iload #7
    //   384: anewarray androidx/constraintlayout/core/widgets/ConstraintWidget
    //   387: putfield mAlignedBiggestElementsInCols : [Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   390: aload_0
    //   391: getfield mAlignedBiggestElementsInRows : [Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   394: astore #13
    //   396: aload #13
    //   398: ifnull -> 421
    //   401: aload #13
    //   403: arraylength
    //   404: iload #9
    //   406: if_icmpge -> 412
    //   409: goto -> 421
    //   412: aload #13
    //   414: aconst_null
    //   415: invokestatic fill : ([Ljava/lang/Object;Ljava/lang/Object;)V
    //   418: goto -> 430
    //   421: aload_0
    //   422: iload #9
    //   424: anewarray androidx/constraintlayout/core/widgets/ConstraintWidget
    //   427: putfield mAlignedBiggestElementsInRows : [Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   430: iconst_0
    //   431: istore #6
    //   433: iload #6
    //   435: iload #7
    //   437: if_icmpge -> 603
    //   440: iconst_0
    //   441: istore #10
    //   443: iload #10
    //   445: iload #9
    //   447: if_icmpge -> 597
    //   450: iload #10
    //   452: iload #7
    //   454: imul
    //   455: iload #6
    //   457: iadd
    //   458: istore #11
    //   460: iload_3
    //   461: iconst_1
    //   462: if_icmpne -> 475
    //   465: iload #6
    //   467: iload #9
    //   469: imul
    //   470: iload #10
    //   472: iadd
    //   473: istore #11
    //   475: iload #11
    //   477: aload_1
    //   478: arraylength
    //   479: if_icmplt -> 485
    //   482: goto -> 591
    //   485: aload_1
    //   486: iload #11
    //   488: aaload
    //   489: astore #13
    //   491: aload #13
    //   493: ifnonnull -> 499
    //   496: goto -> 591
    //   499: aload_0
    //   500: aload #13
    //   502: iload #4
    //   504: invokespecial getWidgetWidth : (Landroidx/constraintlayout/core/widgets/ConstraintWidget;I)I
    //   507: istore #11
    //   509: aload_0
    //   510: getfield mAlignedBiggestElementsInCols : [Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   513: astore #14
    //   515: aload #14
    //   517: iload #6
    //   519: aaload
    //   520: ifnull -> 536
    //   523: aload #14
    //   525: iload #6
    //   527: aaload
    //   528: invokevirtual getWidth : ()I
    //   531: iload #11
    //   533: if_icmpge -> 545
    //   536: aload_0
    //   537: getfield mAlignedBiggestElementsInCols : [Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   540: iload #6
    //   542: aload #13
    //   544: aastore
    //   545: aload_0
    //   546: aload #13
    //   548: iload #4
    //   550: invokespecial getWidgetHeight : (Landroidx/constraintlayout/core/widgets/ConstraintWidget;I)I
    //   553: istore #11
    //   555: aload_0
    //   556: getfield mAlignedBiggestElementsInRows : [Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   559: astore #14
    //   561: aload #14
    //   563: iload #10
    //   565: aaload
    //   566: ifnull -> 582
    //   569: aload #14
    //   571: iload #10
    //   573: aaload
    //   574: invokevirtual getHeight : ()I
    //   577: iload #11
    //   579: if_icmpge -> 591
    //   582: aload_0
    //   583: getfield mAlignedBiggestElementsInRows : [Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   586: iload #10
    //   588: aload #13
    //   590: aastore
    //   591: iinc #10, 1
    //   594: goto -> 443
    //   597: iinc #6, 1
    //   600: goto -> 433
    //   603: iconst_0
    //   604: istore #6
    //   606: iconst_0
    //   607: istore #10
    //   609: iload #10
    //   611: iload #7
    //   613: if_icmpge -> 675
    //   616: aload_0
    //   617: getfield mAlignedBiggestElementsInCols : [Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   620: iload #10
    //   622: aaload
    //   623: astore #13
    //   625: iload #6
    //   627: istore #11
    //   629: aload #13
    //   631: ifnull -> 665
    //   634: iload #6
    //   636: istore #11
    //   638: iload #10
    //   640: ifle -> 652
    //   643: iload #6
    //   645: aload_0
    //   646: getfield mHorizontalGap : I
    //   649: iadd
    //   650: istore #11
    //   652: iload #11
    //   654: aload_0
    //   655: aload #13
    //   657: iload #4
    //   659: invokespecial getWidgetWidth : (Landroidx/constraintlayout/core/widgets/ConstraintWidget;I)I
    //   662: iadd
    //   663: istore #11
    //   665: iinc #10, 1
    //   668: iload #11
    //   670: istore #6
    //   672: goto -> 609
    //   675: iconst_0
    //   676: istore #10
    //   678: iconst_0
    //   679: istore #11
    //   681: iload #11
    //   683: iload #9
    //   685: if_icmpge -> 747
    //   688: aload_0
    //   689: getfield mAlignedBiggestElementsInRows : [Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   692: iload #11
    //   694: aaload
    //   695: astore #13
    //   697: iload #10
    //   699: istore #12
    //   701: aload #13
    //   703: ifnull -> 737
    //   706: iload #10
    //   708: istore #12
    //   710: iload #11
    //   712: ifle -> 724
    //   715: iload #10
    //   717: aload_0
    //   718: getfield mVerticalGap : I
    //   721: iadd
    //   722: istore #12
    //   724: iload #12
    //   726: aload_0
    //   727: aload #13
    //   729: iload #4
    //   731: invokespecial getWidgetHeight : (Landroidx/constraintlayout/core/widgets/ConstraintWidget;I)I
    //   734: iadd
    //   735: istore #12
    //   737: iinc #11, 1
    //   740: iload #12
    //   742: istore #10
    //   744: goto -> 681
    //   747: aload #5
    //   749: iconst_0
    //   750: iload #6
    //   752: iastore
    //   753: aload #5
    //   755: iconst_1
    //   756: iload #10
    //   758: iastore
    //   759: iload_3
    //   760: ifne -> 798
    //   763: iload #6
    //   765: iload #4
    //   767: if_icmple -> 792
    //   770: iload #7
    //   772: iconst_1
    //   773: if_icmple -> 786
    //   776: iinc #7, -1
    //   779: iload #8
    //   781: istore #6
    //   783: goto -> 830
    //   786: iconst_1
    //   787: istore #6
    //   789: goto -> 830
    //   792: iconst_1
    //   793: istore #6
    //   795: goto -> 830
    //   798: iload #10
    //   800: iload #4
    //   802: if_icmple -> 827
    //   805: iload #9
    //   807: iconst_1
    //   808: if_icmple -> 821
    //   811: iinc #9, -1
    //   814: iload #8
    //   816: istore #6
    //   818: goto -> 830
    //   821: iconst_1
    //   822: istore #6
    //   824: goto -> 830
    //   827: iconst_1
    //   828: istore #6
    //   830: iload #6
    //   832: istore #8
    //   834: iload #7
    //   836: istore #10
    //   838: goto -> 308
    //   841: aload_0
    //   842: getfield mAlignedDimensions : [I
    //   845: astore_1
    //   846: aload_1
    //   847: iconst_0
    //   848: iload #10
    //   850: iastore
    //   851: aload_1
    //   852: iconst_1
    //   853: iload #9
    //   855: iastore
    //   856: return
  }
  
  private void measureChainWrap(ConstraintWidget[] paramArrayOfConstraintWidget, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfint) {
    if (paramInt1 == 0)
      return; 
    this.mChainList.clear();
    WidgetsList widgetsList = new WidgetsList(paramInt2, this.mLeft, this.mTop, this.mRight, this.mBottom, paramInt3);
    this.mChainList.add(widgetsList);
    int i = 0;
    int j = 0;
    if (paramInt2 == 0) {
      int i3 = 0;
      byte b1 = 0;
      while (b1 < paramInt1) {
        ConstraintWidget constraintWidget = paramArrayOfConstraintWidget[b1];
        int i4 = getWidgetWidth(constraintWidget, paramInt3);
        if (constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT)
          j++; 
        if ((i3 == paramInt3 || this.mHorizontalGap + i3 + i4 > paramInt3) && widgetsList.biggest != null) {
          i = 1;
        } else {
          i = 0;
        } 
        if (!i && b1 > 0) {
          int i5 = this.mMaxElementsWrap;
          if (i5 > 0 && b1 % i5 == 0)
            i = 1; 
        } 
        if (i) {
          widgetsList = new WidgetsList(paramInt2, this.mLeft, this.mTop, this.mRight, this.mBottom, paramInt3);
          widgetsList.setStartIndex(b1);
          this.mChainList.add(widgetsList);
          i = i4;
        } else if (b1 > 0) {
          i = i3 + this.mHorizontalGap + i4;
        } else {
          i = i4;
        } 
        widgetsList.add(constraintWidget);
        b1++;
        i3 = i;
      } 
    } else {
      int i3 = 0;
      byte b1 = 0;
      j = i;
      while (b1 < paramInt1) {
        ConstraintWidget constraintWidget = paramArrayOfConstraintWidget[b1];
        int i4 = getWidgetHeight(constraintWidget, paramInt3);
        if (constraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT)
          j++; 
        if ((i3 == paramInt3 || this.mVerticalGap + i3 + i4 > paramInt3) && widgetsList.biggest != null) {
          i = 1;
        } else {
          i = 0;
        } 
        if (i == 0 && b1 > 0) {
          int i5 = this.mMaxElementsWrap;
          if (i5 > 0 && b1 % i5 == 0)
            i = 1; 
        } 
        if (i != 0) {
          widgetsList = new WidgetsList(paramInt2, this.mLeft, this.mTop, this.mRight, this.mBottom, paramInt3);
          widgetsList.setStartIndex(b1);
          this.mChainList.add(widgetsList);
          i = i4;
        } else if (b1 > 0) {
          i = i3 + this.mVerticalGap + i4;
        } else {
          i = i4;
        } 
        widgetsList.add(constraintWidget);
        b1++;
        i3 = i;
      } 
    } 
    int i2 = this.mChainList.size();
    ConstraintAnchor constraintAnchor3 = this.mLeft;
    ConstraintAnchor constraintAnchor1 = this.mTop;
    ConstraintAnchor constraintAnchor4 = this.mRight;
    ConstraintAnchor constraintAnchor2 = this.mBottom;
    int n = getPaddingLeft();
    int i1 = getPaddingTop();
    int m = getPaddingRight();
    int k = getPaddingBottom();
    if (getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    } 
    if (j > 0 && paramInt1 != 0)
      for (i = 0; i < i2; i++) {
        widgetsList = this.mChainList.get(i);
        if (paramInt2 == 0) {
          widgetsList.measureMatchConstraints(paramInt3 - widgetsList.getWidth());
        } else {
          widgetsList.measureMatchConstraints(paramInt3 - widgetsList.getHeight());
        } 
      }  
    byte b = 0;
    paramInt1 = 0;
    i = 0;
    while (b < i2) {
      WidgetsList widgetsList1 = this.mChainList.get(b);
      if (paramInt2 == 0) {
        ConstraintAnchor constraintAnchor;
        if (b < i2 - 1) {
          constraintAnchor = ((WidgetsList)this.mChainList.get(b + 1)).biggest.mTop;
          j = 0;
        } else {
          constraintAnchor = this.mBottom;
          j = getPaddingBottom();
        } 
        constraintAnchor2 = widgetsList1.biggest.mBottom;
        widgetsList1.setup(paramInt2, constraintAnchor3, constraintAnchor1, constraintAnchor4, constraintAnchor, n, i1, m, j, paramInt3);
        i1 = 0;
        paramInt1 = Math.max(paramInt1, widgetsList1.getWidth());
        k = i + widgetsList1.getHeight();
        i = k;
        if (b > 0)
          i = k + this.mVerticalGap; 
        constraintAnchor1 = constraintAnchor2;
        constraintAnchor2 = constraintAnchor;
        k = j;
      } else {
        ConstraintAnchor constraintAnchor;
        if (b < i2 - 1) {
          constraintAnchor = ((WidgetsList)this.mChainList.get(b + 1)).biggest.mLeft;
          j = 0;
        } else {
          constraintAnchor = this.mRight;
          j = getPaddingRight();
        } 
        constraintAnchor4 = widgetsList1.biggest.mRight;
        widgetsList1.setup(paramInt2, constraintAnchor3, constraintAnchor1, constraintAnchor, constraintAnchor2, n, i1, j, k, paramInt3);
        constraintAnchor3 = constraintAnchor4;
        n = 0;
        paramInt1 += widgetsList1.getWidth();
        i = Math.max(i, widgetsList1.getHeight());
        if (b > 0) {
          paramInt1 += this.mHorizontalGap;
          constraintAnchor4 = constraintAnchor;
          m = j;
        } else {
          m = j;
          constraintAnchor4 = constraintAnchor;
        } 
      } 
      b++;
    } 
    paramArrayOfint[0] = paramInt1;
    paramArrayOfint[1] = i;
  }
  
  private void measureNoWrap(ConstraintWidget[] paramArrayOfConstraintWidget, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfint) {
    WidgetsList widgetsList;
    if (paramInt1 == 0)
      return; 
    if (this.mChainList.size() == 0) {
      widgetsList = new WidgetsList(paramInt2, this.mLeft, this.mTop, this.mRight, this.mBottom, paramInt3);
      this.mChainList.add(widgetsList);
    } else {
      widgetsList = this.mChainList.get(0);
      widgetsList.clear();
      widgetsList.setup(paramInt2, this.mLeft, this.mTop, this.mRight, this.mBottom, getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom(), paramInt3);
    } 
    for (paramInt2 = 0; paramInt2 < paramInt1; paramInt2++)
      widgetsList.add(paramArrayOfConstraintWidget[paramInt2]); 
    paramArrayOfint[0] = widgetsList.getWidth();
    paramArrayOfint[1] = widgetsList.getHeight();
  }
  
  public void addToSolver(LinearSystem paramLinearSystem, boolean paramBoolean) {
    byte b;
    int i;
    super.addToSolver(paramLinearSystem, paramBoolean);
    if (getParent() != null && ((ConstraintWidgetContainer)getParent()).isRtl()) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    } 
    switch (this.mWrapMode) {
      case 2:
        createAlignedConstraints(paramBoolean);
        break;
      case 1:
        i = this.mChainList.size();
        for (b = 0; b < i; b++) {
          boolean bool;
          WidgetsList widgetsList = this.mChainList.get(b);
          if (b == i - 1) {
            bool = true;
          } else {
            bool = false;
          } 
          widgetsList.createConstraints(paramBoolean, b, bool);
        } 
        break;
      case 0:
        if (this.mChainList.size() > 0)
          ((WidgetsList)this.mChainList.get(0)).createConstraints(paramBoolean, 0, true); 
        break;
    } 
    needsCallbackFromSolver(false);
  }
  
  public void copy(ConstraintWidget paramConstraintWidget, HashMap<ConstraintWidget, ConstraintWidget> paramHashMap) {
    super.copy(paramConstraintWidget, paramHashMap);
    paramConstraintWidget = paramConstraintWidget;
    this.mHorizontalStyle = ((Flow)paramConstraintWidget).mHorizontalStyle;
    this.mVerticalStyle = ((Flow)paramConstraintWidget).mVerticalStyle;
    this.mFirstHorizontalStyle = ((Flow)paramConstraintWidget).mFirstHorizontalStyle;
    this.mFirstVerticalStyle = ((Flow)paramConstraintWidget).mFirstVerticalStyle;
    this.mLastHorizontalStyle = ((Flow)paramConstraintWidget).mLastHorizontalStyle;
    this.mLastVerticalStyle = ((Flow)paramConstraintWidget).mLastVerticalStyle;
    this.mHorizontalBias = ((Flow)paramConstraintWidget).mHorizontalBias;
    this.mVerticalBias = ((Flow)paramConstraintWidget).mVerticalBias;
    this.mFirstHorizontalBias = ((Flow)paramConstraintWidget).mFirstHorizontalBias;
    this.mFirstVerticalBias = ((Flow)paramConstraintWidget).mFirstVerticalBias;
    this.mLastHorizontalBias = ((Flow)paramConstraintWidget).mLastHorizontalBias;
    this.mLastVerticalBias = ((Flow)paramConstraintWidget).mLastVerticalBias;
    this.mHorizontalGap = ((Flow)paramConstraintWidget).mHorizontalGap;
    this.mVerticalGap = ((Flow)paramConstraintWidget).mVerticalGap;
    this.mHorizontalAlign = ((Flow)paramConstraintWidget).mHorizontalAlign;
    this.mVerticalAlign = ((Flow)paramConstraintWidget).mVerticalAlign;
    this.mWrapMode = ((Flow)paramConstraintWidget).mWrapMode;
    this.mMaxElementsWrap = ((Flow)paramConstraintWidget).mMaxElementsWrap;
    this.mOrientation = ((Flow)paramConstraintWidget).mOrientation;
  }
  
  public void measure(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (this.mWidgetsCount > 0 && !measureChildren()) {
      setMeasure(0, 0);
      needsCallbackFromSolver(false);
      return;
    } 
    int i3 = getPaddingLeft();
    int i2 = getPaddingRight();
    int i1 = getPaddingTop();
    int n = getPaddingBottom();
    int[] arrayOfInt = new int[2];
    int i = this.mOrientation;
    if (i == 1) {
      j = paramInt4 - i1 - n;
    } else {
      j = paramInt2 - i3 - i2;
    } 
    if (i == 0) {
      if (this.mHorizontalStyle == -1)
        this.mHorizontalStyle = 0; 
      if (this.mVerticalStyle == -1)
        this.mVerticalStyle = 0; 
    } else {
      if (this.mHorizontalStyle == -1)
        this.mHorizontalStyle = 0; 
      if (this.mVerticalStyle == -1)
        this.mVerticalStyle = 0; 
    } 
    ConstraintWidget[] arrayOfConstraintWidget = this.mWidgets;
    int k = 0;
    for (i = 0; k < this.mWidgetsCount; i = i4) {
      int i4 = i;
      if (this.mWidgets[k].getVisibility() == 8)
        i4 = i + 1; 
      k++;
    } 
    int m = this.mWidgetsCount;
    if (i > 0) {
      arrayOfConstraintWidget = new ConstraintWidget[this.mWidgetsCount - i];
      i = 0;
      k = 0;
      while (k < this.mWidgetsCount) {
        ConstraintWidget constraintWidget = this.mWidgets[k];
        int i4 = i;
        if (constraintWidget.getVisibility() != 8) {
          arrayOfConstraintWidget[i] = constraintWidget;
          i4 = i + 1;
        } 
        k++;
        i = i4;
      } 
    } else {
      i = m;
    } 
    this.mDisplayedWidgets = arrayOfConstraintWidget;
    this.mDisplayedWidgetsCount = i;
    switch (this.mWrapMode) {
      case 2:
        measureAligned(arrayOfConstraintWidget, i, this.mOrientation, j, arrayOfInt);
        break;
      case 1:
        measureChainWrap(arrayOfConstraintWidget, i, this.mOrientation, j, arrayOfInt);
        break;
      case 0:
        measureNoWrap(arrayOfConstraintWidget, i, this.mOrientation, j, arrayOfInt);
        break;
    } 
    boolean bool2 = false;
    k = arrayOfInt[0] + i3 + i2;
    i = arrayOfInt[1] + i1 + n;
    boolean bool1 = false;
    int j = 0;
    if (paramInt1 != 1073741824)
      if (paramInt1 == Integer.MIN_VALUE) {
        paramInt2 = Math.min(k, paramInt2);
      } else {
        paramInt2 = bool1;
        if (paramInt1 == 0)
          paramInt2 = k; 
      }  
    if (paramInt3 == 1073741824) {
      paramInt1 = paramInt4;
    } else if (paramInt3 == Integer.MIN_VALUE) {
      paramInt1 = Math.min(i, paramInt4);
    } else {
      paramInt1 = j;
      if (paramInt3 == 0)
        paramInt1 = i; 
    } 
    setMeasure(paramInt2, paramInt1);
    setWidth(paramInt2);
    setHeight(paramInt1);
    if (this.mWidgetsCount > 0)
      bool2 = true; 
    needsCallbackFromSolver(bool2);
  }
  
  public void setFirstHorizontalBias(float paramFloat) {
    this.mFirstHorizontalBias = paramFloat;
  }
  
  public void setFirstHorizontalStyle(int paramInt) {
    this.mFirstHorizontalStyle = paramInt;
  }
  
  public void setFirstVerticalBias(float paramFloat) {
    this.mFirstVerticalBias = paramFloat;
  }
  
  public void setFirstVerticalStyle(int paramInt) {
    this.mFirstVerticalStyle = paramInt;
  }
  
  public void setHorizontalAlign(int paramInt) {
    this.mHorizontalAlign = paramInt;
  }
  
  public void setHorizontalBias(float paramFloat) {
    this.mHorizontalBias = paramFloat;
  }
  
  public void setHorizontalGap(int paramInt) {
    this.mHorizontalGap = paramInt;
  }
  
  public void setHorizontalStyle(int paramInt) {
    this.mHorizontalStyle = paramInt;
  }
  
  public void setLastHorizontalBias(float paramFloat) {
    this.mLastHorizontalBias = paramFloat;
  }
  
  public void setLastHorizontalStyle(int paramInt) {
    this.mLastHorizontalStyle = paramInt;
  }
  
  public void setLastVerticalBias(float paramFloat) {
    this.mLastVerticalBias = paramFloat;
  }
  
  public void setLastVerticalStyle(int paramInt) {
    this.mLastVerticalStyle = paramInt;
  }
  
  public void setMaxElementsWrap(int paramInt) {
    this.mMaxElementsWrap = paramInt;
  }
  
  public void setOrientation(int paramInt) {
    this.mOrientation = paramInt;
  }
  
  public void setVerticalAlign(int paramInt) {
    this.mVerticalAlign = paramInt;
  }
  
  public void setVerticalBias(float paramFloat) {
    this.mVerticalBias = paramFloat;
  }
  
  public void setVerticalGap(int paramInt) {
    this.mVerticalGap = paramInt;
  }
  
  public void setVerticalStyle(int paramInt) {
    this.mVerticalStyle = paramInt;
  }
  
  public void setWrapMode(int paramInt) {
    this.mWrapMode = paramInt;
  }
  
  private class WidgetsList {
    private ConstraintWidget biggest = null;
    
    int biggestDimension = 0;
    
    private ConstraintAnchor mBottom;
    
    private int mCount = 0;
    
    private int mHeight = 0;
    
    private ConstraintAnchor mLeft;
    
    private int mMax = 0;
    
    private int mNbMatchConstraintsWidgets = 0;
    
    private int mOrientation = 0;
    
    private int mPaddingBottom = 0;
    
    private int mPaddingLeft = 0;
    
    private int mPaddingRight = 0;
    
    private int mPaddingTop = 0;
    
    private ConstraintAnchor mRight;
    
    private int mStartIndex = 0;
    
    private ConstraintAnchor mTop;
    
    private int mWidth = 0;
    
    final Flow this$0;
    
    public WidgetsList(int param1Int1, ConstraintAnchor param1ConstraintAnchor1, ConstraintAnchor param1ConstraintAnchor2, ConstraintAnchor param1ConstraintAnchor3, ConstraintAnchor param1ConstraintAnchor4, int param1Int2) {
      this.mOrientation = param1Int1;
      this.mLeft = param1ConstraintAnchor1;
      this.mTop = param1ConstraintAnchor2;
      this.mRight = param1ConstraintAnchor3;
      this.mBottom = param1ConstraintAnchor4;
      this.mPaddingLeft = Flow.this.getPaddingLeft();
      this.mPaddingTop = Flow.this.getPaddingTop();
      this.mPaddingRight = Flow.this.getPaddingRight();
      this.mPaddingBottom = Flow.this.getPaddingBottom();
      this.mMax = param1Int2;
    }
    
    private void recomputeDimensions() {
      this.mWidth = 0;
      this.mHeight = 0;
      this.biggest = null;
      this.biggestDimension = 0;
      int i = this.mCount;
      for (byte b = 0; b < i && this.mStartIndex + b < Flow.this.mDisplayedWidgetsCount; b++) {
        ConstraintWidget constraintWidget = Flow.this.mDisplayedWidgets[this.mStartIndex + b];
        if (this.mOrientation == 0) {
          int k = constraintWidget.getWidth();
          int j = Flow.this.mHorizontalGap;
          if (constraintWidget.getVisibility() == 8)
            j = 0; 
          this.mWidth += k + j;
          j = Flow.this.getWidgetHeight(constraintWidget, this.mMax);
          if (this.biggest == null || this.biggestDimension < j) {
            this.biggest = constraintWidget;
            this.biggestDimension = j;
            this.mHeight = j;
          } 
        } else {
          int k = Flow.this.getWidgetWidth(constraintWidget, this.mMax);
          int m = Flow.this.getWidgetHeight(constraintWidget, this.mMax);
          int j = Flow.this.mVerticalGap;
          if (constraintWidget.getVisibility() == 8)
            j = 0; 
          this.mHeight += m + j;
          if (this.biggest == null || this.biggestDimension < k) {
            this.biggest = constraintWidget;
            this.biggestDimension = k;
            this.mWidth = k;
          } 
        } 
      } 
    }
    
    public void add(ConstraintWidget param1ConstraintWidget) {
      if (this.mOrientation == 0) {
        int i = Flow.this.getWidgetWidth(param1ConstraintWidget, this.mMax);
        if (param1ConstraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
          this.mNbMatchConstraintsWidgets++;
          i = 0;
        } 
        int j = Flow.this.mHorizontalGap;
        if (param1ConstraintWidget.getVisibility() == 8)
          j = 0; 
        this.mWidth += i + j;
        i = Flow.this.getWidgetHeight(param1ConstraintWidget, this.mMax);
        if (this.biggest == null || this.biggestDimension < i) {
          this.biggest = param1ConstraintWidget;
          this.biggestDimension = i;
          this.mHeight = i;
        } 
      } else {
        int k = Flow.this.getWidgetWidth(param1ConstraintWidget, this.mMax);
        int i = Flow.this.getWidgetHeight(param1ConstraintWidget, this.mMax);
        if (param1ConstraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
          this.mNbMatchConstraintsWidgets++;
          i = 0;
        } 
        int j = Flow.this.mVerticalGap;
        if (param1ConstraintWidget.getVisibility() == 8)
          j = 0; 
        this.mHeight += i + j;
        if (this.biggest == null || this.biggestDimension < k) {
          this.biggest = param1ConstraintWidget;
          this.biggestDimension = k;
          this.mWidth = k;
        } 
      } 
      this.mCount++;
    }
    
    public void clear() {
      this.biggestDimension = 0;
      this.biggest = null;
      this.mWidth = 0;
      this.mHeight = 0;
      this.mStartIndex = 0;
      this.mCount = 0;
      this.mNbMatchConstraintsWidgets = 0;
    }
    
    public void createConstraints(boolean param1Boolean1, int param1Int, boolean param1Boolean2) {
      boolean bool;
      int m = this.mCount;
      int i;
      for (i = 0; i < m && this.mStartIndex + i < Flow.this.mDisplayedWidgetsCount; i++) {
        ConstraintWidget constraintWidget = Flow.this.mDisplayedWidgets[this.mStartIndex + i];
        if (constraintWidget != null)
          constraintWidget.resetAnchors(); 
      } 
      if (m == 0 || this.biggest == null)
        return; 
      if (param1Boolean2 && param1Int == 0) {
        bool = true;
      } else {
        bool = false;
      } 
      int j = -1;
      int k = -1;
      i = 0;
      while (i < m) {
        int n = i;
        if (param1Boolean1)
          n = m - 1 - i; 
        if (this.mStartIndex + n >= Flow.this.mDisplayedWidgetsCount)
          break; 
        ConstraintWidget constraintWidget = Flow.this.mDisplayedWidgets[this.mStartIndex + n];
        int i1 = j;
        n = k;
        if (constraintWidget != null) {
          i1 = j;
          n = k;
          if (constraintWidget.getVisibility() == 0) {
            k = j;
            if (j == -1)
              k = i; 
            n = i;
            i1 = k;
          } 
        } 
        i++;
        j = i1;
        k = n;
      } 
      ConstraintWidget constraintWidget1 = null;
      ConstraintWidget constraintWidget2 = null;
      if (this.mOrientation == 0) {
        ConstraintWidget constraintWidget4 = this.biggest;
        constraintWidget4.setVerticalChainStyle(Flow.this.mVerticalStyle);
        int n = this.mPaddingTop;
        i = n;
        if (param1Int > 0)
          i = n + Flow.this.mVerticalGap; 
        constraintWidget4.mTop.connect(this.mTop, i);
        if (param1Boolean2)
          constraintWidget4.mBottom.connect(this.mBottom, this.mPaddingBottom); 
        if (param1Int > 0)
          this.mTop.mOwner.mBottom.connect(constraintWidget4.mTop, 0); 
        ConstraintWidget constraintWidget3 = constraintWidget4;
        constraintWidget1 = constraintWidget3;
        if (Flow.this.mVerticalAlign == 3) {
          constraintWidget1 = constraintWidget3;
          if (!constraintWidget4.hasBaseline()) {
            param1Int = 0;
            while (true) {
              constraintWidget1 = constraintWidget3;
              if (param1Int < m) {
                i = param1Int;
                if (param1Boolean1)
                  i = m - 1 - param1Int; 
                if (this.mStartIndex + i >= Flow.this.mDisplayedWidgetsCount) {
                  constraintWidget1 = constraintWidget3;
                  break;
                } 
                constraintWidget1 = Flow.this.mDisplayedWidgets[this.mStartIndex + i];
                if (constraintWidget1.hasBaseline())
                  break; 
                param1Int++;
                continue;
              } 
              break;
            } 
          } 
        } 
        param1Int = 0;
        constraintWidget3 = constraintWidget2;
        while (true) {
          if (param1Int < m) {
            i = param1Int;
            if (param1Boolean1)
              i = m - 1 - param1Int; 
            if (this.mStartIndex + i >= Flow.this.mDisplayedWidgetsCount)
              break; 
            constraintWidget2 = Flow.this.mDisplayedWidgets[this.mStartIndex + i];
            if (constraintWidget2 == null)
              continue; 
            if (param1Int == 0)
              constraintWidget2.connect(constraintWidget2.mLeft, this.mLeft, this.mPaddingLeft); 
            if (i == 0) {
              n = Flow.this.mHorizontalStyle;
              float f2 = Flow.this.mHorizontalBias;
              float f1 = f2;
              if (param1Boolean1)
                f1 = 1.0F - f2; 
              if (this.mStartIndex == 0 && Flow.this.mFirstHorizontalStyle != -1) {
                i = Flow.this.mFirstHorizontalStyle;
                f2 = Flow.this.mFirstHorizontalBias;
                f1 = f2;
                if (param1Boolean1)
                  f1 = 1.0F - f2; 
                f2 = f1;
              } else {
                i = n;
                f2 = f1;
                if (param1Boolean2) {
                  i = n;
                  f2 = f1;
                  if (Flow.this.mLastHorizontalStyle != -1) {
                    i = Flow.this.mLastHorizontalStyle;
                    f2 = Flow.this.mLastHorizontalBias;
                    f1 = f2;
                    if (param1Boolean1)
                      f1 = 1.0F - f2; 
                    f2 = f1;
                  } 
                } 
              } 
              constraintWidget2.setHorizontalChainStyle(i);
              constraintWidget2.setHorizontalBiasPercent(f2);
            } 
            if (param1Int == m - 1)
              constraintWidget2.connect(constraintWidget2.mRight, this.mRight, this.mPaddingRight); 
            if (constraintWidget3 != null) {
              constraintWidget2.mLeft.connect(constraintWidget3.mRight, Flow.this.mHorizontalGap);
              if (param1Int == j)
                constraintWidget2.mLeft.setGoneMargin(this.mPaddingLeft); 
              constraintWidget3.mRight.connect(constraintWidget2.mLeft, 0);
              if (param1Int == k + 1)
                constraintWidget3.mRight.setGoneMargin(this.mPaddingRight); 
            } 
            if (constraintWidget2 != constraintWidget4)
              if (Flow.this.mVerticalAlign == 3 && constraintWidget1.hasBaseline() && constraintWidget2 != constraintWidget1 && constraintWidget2.hasBaseline()) {
                constraintWidget2.mBaseline.connect(constraintWidget1.mBaseline, 0);
              } else {
                switch (Flow.this.mVerticalAlign) {
                  default:
                    if (bool) {
                      constraintWidget2.mTop.connect(this.mTop, this.mPaddingTop);
                      constraintWidget2.mBottom.connect(this.mBottom, this.mPaddingBottom);
                      break;
                    } 
                    constraintWidget2.mTop.connect(constraintWidget4.mTop, 0);
                    constraintWidget2.mBottom.connect(constraintWidget4.mBottom, 0);
                    constraintWidget3 = constraintWidget2;
                    continue;
                  case 1:
                    constraintWidget2.mBottom.connect(constraintWidget4.mBottom, 0);
                    break;
                  case 0:
                    constraintWidget2.mTop.connect(constraintWidget4.mTop, 0);
                    break;
                } 
              }  
          } else {
            break;
          } 
          constraintWidget3 = constraintWidget2;
          continue;
          param1Int++;
        } 
      } else {
        ConstraintWidget constraintWidget = this.biggest;
        constraintWidget.setHorizontalChainStyle(Flow.this.mHorizontalStyle);
        int n = this.mPaddingLeft;
        i = n;
        if (param1Int > 0)
          i = n + Flow.this.mHorizontalGap; 
        if (param1Boolean1) {
          constraintWidget.mRight.connect(this.mRight, i);
          if (param1Boolean2)
            constraintWidget.mLeft.connect(this.mLeft, this.mPaddingRight); 
          if (param1Int > 0)
            this.mRight.mOwner.mLeft.connect(constraintWidget.mRight, 0); 
        } else {
          constraintWidget.mLeft.connect(this.mLeft, i);
          if (param1Boolean2)
            constraintWidget.mRight.connect(this.mRight, this.mPaddingRight); 
          if (param1Int > 0)
            this.mLeft.mOwner.mRight.connect(constraintWidget.mLeft, 0); 
        } 
        for (i = 0; i < m && this.mStartIndex + i < Flow.this.mDisplayedWidgetsCount; i++) {
          ConstraintWidget constraintWidget3 = Flow.this.mDisplayedWidgets[this.mStartIndex + i];
          if (constraintWidget3 != null) {
            if (i == 0) {
              float f1;
              constraintWidget3.connect(constraintWidget3.mTop, this.mTop, this.mPaddingTop);
              n = Flow.this.mVerticalStyle;
              float f2 = Flow.this.mVerticalBias;
              if (this.mStartIndex == 0 && Flow.this.mFirstVerticalStyle != -1) {
                param1Int = Flow.this.mFirstVerticalStyle;
                f1 = Flow.this.mFirstVerticalBias;
              } else {
                param1Int = n;
                f1 = f2;
                if (param1Boolean2) {
                  param1Int = n;
                  f1 = f2;
                  if (Flow.this.mLastVerticalStyle != -1) {
                    param1Int = Flow.this.mLastVerticalStyle;
                    f1 = Flow.this.mLastVerticalBias;
                  } 
                } 
              } 
              constraintWidget3.setVerticalChainStyle(param1Int);
              constraintWidget3.setVerticalBiasPercent(f1);
            } 
            if (i == m - 1)
              constraintWidget3.connect(constraintWidget3.mBottom, this.mBottom, this.mPaddingBottom); 
            if (constraintWidget1 != null) {
              constraintWidget3.mTop.connect(constraintWidget1.mBottom, Flow.this.mVerticalGap);
              if (i == j)
                constraintWidget3.mTop.setGoneMargin(this.mPaddingTop); 
              constraintWidget1.mBottom.connect(constraintWidget3.mTop, 0);
              if (i == k + 1)
                constraintWidget1.mBottom.setGoneMargin(this.mPaddingBottom); 
            } 
            if (constraintWidget3 != constraintWidget)
              if (param1Boolean1) {
                switch (Flow.this.mHorizontalAlign) {
                  case 2:
                    constraintWidget3.mLeft.connect(constraintWidget.mLeft, 0);
                    constraintWidget3.mRight.connect(constraintWidget.mRight, 0);
                    break;
                  case 1:
                    constraintWidget3.mLeft.connect(constraintWidget.mLeft, 0);
                    break;
                  case 0:
                    constraintWidget3.mRight.connect(constraintWidget.mRight, 0);
                    break;
                } 
              } else {
                switch (Flow.this.mHorizontalAlign) {
                  case 2:
                    if (bool) {
                      constraintWidget3.mLeft.connect(this.mLeft, this.mPaddingLeft);
                      constraintWidget3.mRight.connect(this.mRight, this.mPaddingRight);
                      break;
                    } 
                    constraintWidget3.mLeft.connect(constraintWidget.mLeft, 0);
                    constraintWidget3.mRight.connect(constraintWidget.mRight, 0);
                    break;
                  case 1:
                    constraintWidget3.mRight.connect(constraintWidget.mRight, 0);
                    break;
                  case 0:
                    constraintWidget3.mLeft.connect(constraintWidget.mLeft, 0);
                    break;
                } 
              }  
            constraintWidget1 = constraintWidget3;
          } 
        } 
      } 
    }
    
    public int getHeight() {
      return (this.mOrientation == 1) ? (this.mHeight - Flow.this.mVerticalGap) : this.mHeight;
    }
    
    public int getWidth() {
      return (this.mOrientation == 0) ? (this.mWidth - Flow.this.mHorizontalGap) : this.mWidth;
    }
    
    public void measureMatchConstraints(int param1Int) {
      int j = this.mNbMatchConstraintsWidgets;
      if (j == 0)
        return; 
      int i = this.mCount;
      j = param1Int / j;
      for (param1Int = 0; param1Int < i && this.mStartIndex + param1Int < Flow.this.mDisplayedWidgetsCount; param1Int++) {
        ConstraintWidget constraintWidget = Flow.this.mDisplayedWidgets[this.mStartIndex + param1Int];
        if (this.mOrientation == 0) {
          if (constraintWidget != null && constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mMatchConstraintDefaultWidth == 0)
            Flow.this.measure(constraintWidget, ConstraintWidget.DimensionBehaviour.FIXED, j, constraintWidget.getVerticalDimensionBehaviour(), constraintWidget.getHeight()); 
        } else if (constraintWidget != null && constraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mMatchConstraintDefaultHeight == 0) {
          Flow.this.measure(constraintWidget, constraintWidget.getHorizontalDimensionBehaviour(), constraintWidget.getWidth(), ConstraintWidget.DimensionBehaviour.FIXED, j);
        } 
      } 
      recomputeDimensions();
    }
    
    public void setStartIndex(int param1Int) {
      this.mStartIndex = param1Int;
    }
    
    public void setup(int param1Int1, ConstraintAnchor param1ConstraintAnchor1, ConstraintAnchor param1ConstraintAnchor2, ConstraintAnchor param1ConstraintAnchor3, ConstraintAnchor param1ConstraintAnchor4, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6) {
      this.mOrientation = param1Int1;
      this.mLeft = param1ConstraintAnchor1;
      this.mTop = param1ConstraintAnchor2;
      this.mRight = param1ConstraintAnchor3;
      this.mBottom = param1ConstraintAnchor4;
      this.mPaddingLeft = param1Int2;
      this.mPaddingTop = param1Int3;
      this.mPaddingRight = param1Int4;
      this.mPaddingBottom = param1Int5;
      this.mMax = param1Int6;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\widgets\Flow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */