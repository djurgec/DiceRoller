package androidx.constraintlayout.core.widgets;

import androidx.constraintlayout.core.LinearSystem;
import java.util.HashMap;

public class Barrier extends HelperWidget {
  public static final int BOTTOM = 3;
  
  public static final int LEFT = 0;
  
  public static final int RIGHT = 1;
  
  public static final int TOP = 2;
  
  private static final boolean USE_RELAX_GONE = false;
  
  private static final boolean USE_RESOLUTION = true;
  
  private boolean mAllowsGoneWidget = true;
  
  private int mBarrierType = 0;
  
  private int mMargin = 0;
  
  boolean resolved = false;
  
  public Barrier() {}
  
  public Barrier(String paramString) {
    setDebugName(paramString);
  }
  
  public void addToSolver(LinearSystem paramLinearSystem, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   4: iconst_0
    //   5: aload_0
    //   6: getfield mLeft : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   9: aastore
    //   10: aload_0
    //   11: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   14: iconst_2
    //   15: aload_0
    //   16: getfield mTop : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   19: aastore
    //   20: aload_0
    //   21: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   24: iconst_1
    //   25: aload_0
    //   26: getfield mRight : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   29: aastore
    //   30: aload_0
    //   31: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   34: iconst_3
    //   35: aload_0
    //   36: getfield mBottom : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   39: aastore
    //   40: iconst_0
    //   41: istore_3
    //   42: iload_3
    //   43: aload_0
    //   44: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   47: arraylength
    //   48: if_icmpge -> 76
    //   51: aload_0
    //   52: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   55: iload_3
    //   56: aaload
    //   57: aload_1
    //   58: aload_0
    //   59: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   62: iload_3
    //   63: aaload
    //   64: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   67: putfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   70: iinc #3, 1
    //   73: goto -> 42
    //   76: aload_0
    //   77: getfield mBarrierType : I
    //   80: istore_3
    //   81: iload_3
    //   82: iflt -> 1041
    //   85: iload_3
    //   86: iconst_4
    //   87: if_icmpge -> 1041
    //   90: aload_0
    //   91: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   94: aload_0
    //   95: getfield mBarrierType : I
    //   98: aaload
    //   99: astore #8
    //   101: aload_0
    //   102: getfield resolved : Z
    //   105: ifne -> 113
    //   108: aload_0
    //   109: invokevirtual allSolved : ()Z
    //   112: pop
    //   113: aload_0
    //   114: getfield resolved : Z
    //   117: ifeq -> 216
    //   120: aload_0
    //   121: iconst_0
    //   122: putfield resolved : Z
    //   125: aload_0
    //   126: getfield mBarrierType : I
    //   129: istore_3
    //   130: iload_3
    //   131: ifeq -> 185
    //   134: iload_3
    //   135: iconst_1
    //   136: if_icmpne -> 142
    //   139: goto -> 185
    //   142: iload_3
    //   143: iconst_2
    //   144: if_icmpeq -> 152
    //   147: iload_3
    //   148: iconst_3
    //   149: if_icmpne -> 215
    //   152: aload_1
    //   153: aload_0
    //   154: getfield mTop : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   157: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   160: aload_0
    //   161: getfield mY : I
    //   164: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;I)V
    //   167: aload_1
    //   168: aload_0
    //   169: getfield mBottom : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   172: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   175: aload_0
    //   176: getfield mY : I
    //   179: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;I)V
    //   182: goto -> 215
    //   185: aload_1
    //   186: aload_0
    //   187: getfield mLeft : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   190: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   193: aload_0
    //   194: getfield mX : I
    //   197: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;I)V
    //   200: aload_1
    //   201: aload_0
    //   202: getfield mRight : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   205: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   208: aload_0
    //   209: getfield mX : I
    //   212: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;I)V
    //   215: return
    //   216: iconst_0
    //   217: istore #7
    //   219: iconst_0
    //   220: istore_3
    //   221: iload #7
    //   223: istore_2
    //   224: iload_3
    //   225: aload_0
    //   226: getfield mWidgetsCount : I
    //   229: if_icmpge -> 375
    //   232: aload_0
    //   233: getfield mWidgets : [Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   236: iload_3
    //   237: aaload
    //   238: astore #9
    //   240: aload_0
    //   241: getfield mAllowsGoneWidget : Z
    //   244: ifne -> 258
    //   247: aload #9
    //   249: invokevirtual allowedInBarrier : ()Z
    //   252: ifne -> 258
    //   255: goto -> 369
    //   258: aload_0
    //   259: getfield mBarrierType : I
    //   262: istore #4
    //   264: iload #4
    //   266: ifeq -> 275
    //   269: iload #4
    //   271: iconst_1
    //   272: if_icmpne -> 313
    //   275: aload #9
    //   277: invokevirtual getHorizontalDimensionBehaviour : ()Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   280: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   283: if_acmpne -> 313
    //   286: aload #9
    //   288: getfield mLeft : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   291: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   294: ifnull -> 313
    //   297: aload #9
    //   299: getfield mRight : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   302: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   305: ifnull -> 313
    //   308: iconst_1
    //   309: istore_2
    //   310: goto -> 375
    //   313: aload_0
    //   314: getfield mBarrierType : I
    //   317: istore #4
    //   319: iload #4
    //   321: iconst_2
    //   322: if_icmpeq -> 331
    //   325: iload #4
    //   327: iconst_3
    //   328: if_icmpne -> 369
    //   331: aload #9
    //   333: invokevirtual getVerticalDimensionBehaviour : ()Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   336: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   339: if_acmpne -> 369
    //   342: aload #9
    //   344: getfield mTop : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   347: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   350: ifnull -> 369
    //   353: aload #9
    //   355: getfield mBottom : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   358: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   361: ifnull -> 369
    //   364: iconst_1
    //   365: istore_2
    //   366: goto -> 375
    //   369: iinc #3, 1
    //   372: goto -> 221
    //   375: aload_0
    //   376: getfield mLeft : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   379: invokevirtual hasCenteredDependents : ()Z
    //   382: ifne -> 403
    //   385: aload_0
    //   386: getfield mRight : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   389: invokevirtual hasCenteredDependents : ()Z
    //   392: ifeq -> 398
    //   395: goto -> 403
    //   398: iconst_0
    //   399: istore_3
    //   400: goto -> 405
    //   403: iconst_1
    //   404: istore_3
    //   405: aload_0
    //   406: getfield mTop : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   409: invokevirtual hasCenteredDependents : ()Z
    //   412: ifne -> 434
    //   415: aload_0
    //   416: getfield mBottom : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   419: invokevirtual hasCenteredDependents : ()Z
    //   422: ifeq -> 428
    //   425: goto -> 434
    //   428: iconst_0
    //   429: istore #4
    //   431: goto -> 437
    //   434: iconst_1
    //   435: istore #4
    //   437: iload_2
    //   438: ifne -> 494
    //   441: aload_0
    //   442: getfield mBarrierType : I
    //   445: istore #5
    //   447: iload #5
    //   449: ifne -> 456
    //   452: iload_3
    //   453: ifne -> 488
    //   456: iload #5
    //   458: iconst_2
    //   459: if_icmpne -> 467
    //   462: iload #4
    //   464: ifne -> 488
    //   467: iload #5
    //   469: iconst_1
    //   470: if_icmpne -> 477
    //   473: iload_3
    //   474: ifne -> 488
    //   477: iload #5
    //   479: iconst_3
    //   480: if_icmpne -> 494
    //   483: iload #4
    //   485: ifeq -> 494
    //   488: iconst_1
    //   489: istore #4
    //   491: goto -> 497
    //   494: iconst_0
    //   495: istore #4
    //   497: iconst_5
    //   498: istore_3
    //   499: iload #4
    //   501: ifne -> 506
    //   504: iconst_4
    //   505: istore_3
    //   506: iconst_0
    //   507: istore #4
    //   509: iload #4
    //   511: aload_0
    //   512: getfield mWidgetsCount : I
    //   515: if_icmpge -> 727
    //   518: aload_0
    //   519: getfield mWidgets : [Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   522: iload #4
    //   524: aaload
    //   525: astore #10
    //   527: aload_0
    //   528: getfield mAllowsGoneWidget : Z
    //   531: ifne -> 545
    //   534: aload #10
    //   536: invokevirtual allowedInBarrier : ()Z
    //   539: ifne -> 545
    //   542: goto -> 721
    //   545: aload_1
    //   546: aload #10
    //   548: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   551: aload_0
    //   552: getfield mBarrierType : I
    //   555: aaload
    //   556: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/core/SolverVariable;
    //   559: astore #9
    //   561: aload #10
    //   563: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   566: aload_0
    //   567: getfield mBarrierType : I
    //   570: aaload
    //   571: aload #9
    //   573: putfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   576: iconst_0
    //   577: istore #6
    //   579: iload #6
    //   581: istore #5
    //   583: aload #10
    //   585: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   588: aload_0
    //   589: getfield mBarrierType : I
    //   592: aaload
    //   593: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   596: ifnull -> 640
    //   599: iload #6
    //   601: istore #5
    //   603: aload #10
    //   605: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   608: aload_0
    //   609: getfield mBarrierType : I
    //   612: aaload
    //   613: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   616: getfield mOwner : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   619: aload_0
    //   620: if_acmpne -> 640
    //   623: iconst_0
    //   624: aload #10
    //   626: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   629: aload_0
    //   630: getfield mBarrierType : I
    //   633: aaload
    //   634: getfield mMargin : I
    //   637: iadd
    //   638: istore #5
    //   640: aload_0
    //   641: getfield mBarrierType : I
    //   644: istore #6
    //   646: iload #6
    //   648: ifeq -> 682
    //   651: iload #6
    //   653: iconst_2
    //   654: if_icmpne -> 660
    //   657: goto -> 682
    //   660: aload_1
    //   661: aload #8
    //   663: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   666: aload #9
    //   668: aload_0
    //   669: getfield mMargin : I
    //   672: iload #5
    //   674: iadd
    //   675: iload_2
    //   676: invokevirtual addGreaterBarrier : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;IZ)V
    //   679: goto -> 701
    //   682: aload_1
    //   683: aload #8
    //   685: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   688: aload #9
    //   690: aload_0
    //   691: getfield mMargin : I
    //   694: iload #5
    //   696: isub
    //   697: iload_2
    //   698: invokevirtual addLowerBarrier : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;IZ)V
    //   701: aload_1
    //   702: aload #8
    //   704: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   707: aload #9
    //   709: aload_0
    //   710: getfield mMargin : I
    //   713: iload #5
    //   715: iadd
    //   716: iload_3
    //   717: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   720: pop
    //   721: iinc #4, 1
    //   724: goto -> 509
    //   727: aload_0
    //   728: getfield mBarrierType : I
    //   731: istore_3
    //   732: iload_3
    //   733: ifne -> 809
    //   736: aload_1
    //   737: aload_0
    //   738: getfield mRight : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   741: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   744: aload_0
    //   745: getfield mLeft : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   748: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   751: iconst_0
    //   752: bipush #8
    //   754: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   757: pop
    //   758: aload_1
    //   759: aload_0
    //   760: getfield mLeft : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   763: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   766: aload_0
    //   767: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   770: getfield mRight : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   773: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   776: iconst_0
    //   777: iconst_4
    //   778: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   781: pop
    //   782: aload_1
    //   783: aload_0
    //   784: getfield mLeft : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   787: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   790: aload_0
    //   791: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   794: getfield mLeft : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   797: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   800: iconst_0
    //   801: iconst_0
    //   802: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   805: pop
    //   806: goto -> 1040
    //   809: iload_3
    //   810: iconst_1
    //   811: if_icmpne -> 887
    //   814: aload_1
    //   815: aload_0
    //   816: getfield mLeft : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   819: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   822: aload_0
    //   823: getfield mRight : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   826: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   829: iconst_0
    //   830: bipush #8
    //   832: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   835: pop
    //   836: aload_1
    //   837: aload_0
    //   838: getfield mLeft : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   841: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   844: aload_0
    //   845: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   848: getfield mLeft : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   851: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   854: iconst_0
    //   855: iconst_4
    //   856: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   859: pop
    //   860: aload_1
    //   861: aload_0
    //   862: getfield mLeft : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   865: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   868: aload_0
    //   869: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   872: getfield mRight : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   875: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   878: iconst_0
    //   879: iconst_0
    //   880: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   883: pop
    //   884: goto -> 1040
    //   887: iload_3
    //   888: iconst_2
    //   889: if_icmpne -> 965
    //   892: aload_1
    //   893: aload_0
    //   894: getfield mBottom : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   897: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   900: aload_0
    //   901: getfield mTop : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   904: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   907: iconst_0
    //   908: bipush #8
    //   910: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   913: pop
    //   914: aload_1
    //   915: aload_0
    //   916: getfield mTop : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   919: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   922: aload_0
    //   923: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   926: getfield mBottom : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   929: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   932: iconst_0
    //   933: iconst_4
    //   934: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   937: pop
    //   938: aload_1
    //   939: aload_0
    //   940: getfield mTop : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   943: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   946: aload_0
    //   947: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   950: getfield mTop : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   953: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   956: iconst_0
    //   957: iconst_0
    //   958: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   961: pop
    //   962: goto -> 1040
    //   965: iload_3
    //   966: iconst_3
    //   967: if_icmpne -> 1040
    //   970: aload_1
    //   971: aload_0
    //   972: getfield mTop : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   975: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   978: aload_0
    //   979: getfield mBottom : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   982: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   985: iconst_0
    //   986: bipush #8
    //   988: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   991: pop
    //   992: aload_1
    //   993: aload_0
    //   994: getfield mTop : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   997: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   1000: aload_0
    //   1001: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   1004: getfield mTop : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1007: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   1010: iconst_0
    //   1011: iconst_4
    //   1012: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   1015: pop
    //   1016: aload_1
    //   1017: aload_0
    //   1018: getfield mTop : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1021: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   1024: aload_0
    //   1025: getfield mParent : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   1028: getfield mBottom : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1031: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   1034: iconst_0
    //   1035: iconst_0
    //   1036: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   1039: pop
    //   1040: return
    //   1041: return
  }
  
  public boolean allSolved() {
    // Byte code:
    //   0: iconst_1
    //   1: istore_3
    //   2: iconst_0
    //   3: istore_2
    //   4: iload_2
    //   5: aload_0
    //   6: getfield mWidgetsCount : I
    //   9: if_icmpge -> 107
    //   12: aload_0
    //   13: getfield mWidgets : [Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   16: iload_2
    //   17: aaload
    //   18: astore #7
    //   20: aload_0
    //   21: getfield mAllowsGoneWidget : Z
    //   24: ifne -> 40
    //   27: aload #7
    //   29: invokevirtual allowedInBarrier : ()Z
    //   32: ifne -> 40
    //   35: iload_3
    //   36: istore_1
    //   37: goto -> 99
    //   40: aload_0
    //   41: getfield mBarrierType : I
    //   44: istore_1
    //   45: iload_1
    //   46: ifeq -> 54
    //   49: iload_1
    //   50: iconst_1
    //   51: if_icmpne -> 67
    //   54: aload #7
    //   56: invokevirtual isResolvedHorizontally : ()Z
    //   59: ifne -> 67
    //   62: iconst_0
    //   63: istore_1
    //   64: goto -> 99
    //   67: aload_0
    //   68: getfield mBarrierType : I
    //   71: istore #4
    //   73: iload #4
    //   75: iconst_2
    //   76: if_icmpeq -> 87
    //   79: iload_3
    //   80: istore_1
    //   81: iload #4
    //   83: iconst_3
    //   84: if_icmpne -> 99
    //   87: iload_3
    //   88: istore_1
    //   89: aload #7
    //   91: invokevirtual isResolvedVertically : ()Z
    //   94: ifne -> 99
    //   97: iconst_0
    //   98: istore_1
    //   99: iinc #2, 1
    //   102: iload_1
    //   103: istore_3
    //   104: goto -> 4
    //   107: iload_3
    //   108: ifeq -> 428
    //   111: aload_0
    //   112: getfield mWidgetsCount : I
    //   115: ifle -> 428
    //   118: iconst_0
    //   119: istore_1
    //   120: iconst_0
    //   121: istore #4
    //   123: iconst_0
    //   124: istore #5
    //   126: iload #5
    //   128: aload_0
    //   129: getfield mWidgetsCount : I
    //   132: if_icmpge -> 382
    //   135: aload_0
    //   136: getfield mWidgets : [Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   139: iload #5
    //   141: aaload
    //   142: astore #7
    //   144: aload_0
    //   145: getfield mAllowsGoneWidget : Z
    //   148: ifne -> 162
    //   151: aload #7
    //   153: invokevirtual allowedInBarrier : ()Z
    //   156: ifne -> 162
    //   159: goto -> 376
    //   162: iload_1
    //   163: istore_3
    //   164: iload #4
    //   166: istore_2
    //   167: iload #4
    //   169: ifne -> 257
    //   172: aload_0
    //   173: getfield mBarrierType : I
    //   176: istore_2
    //   177: iload_2
    //   178: ifne -> 196
    //   181: aload #7
    //   183: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   186: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   189: invokevirtual getFinalValue : ()I
    //   192: istore_1
    //   193: goto -> 253
    //   196: iload_2
    //   197: iconst_1
    //   198: if_icmpne -> 216
    //   201: aload #7
    //   203: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   206: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   209: invokevirtual getFinalValue : ()I
    //   212: istore_1
    //   213: goto -> 253
    //   216: iload_2
    //   217: iconst_2
    //   218: if_icmpne -> 236
    //   221: aload #7
    //   223: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   226: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   229: invokevirtual getFinalValue : ()I
    //   232: istore_1
    //   233: goto -> 253
    //   236: iload_2
    //   237: iconst_3
    //   238: if_icmpne -> 253
    //   241: aload #7
    //   243: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   246: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   249: invokevirtual getFinalValue : ()I
    //   252: istore_1
    //   253: iconst_1
    //   254: istore_2
    //   255: iload_1
    //   256: istore_3
    //   257: aload_0
    //   258: getfield mBarrierType : I
    //   261: istore #6
    //   263: iload #6
    //   265: ifne -> 290
    //   268: iload_3
    //   269: aload #7
    //   271: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   274: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   277: invokevirtual getFinalValue : ()I
    //   280: invokestatic min : (II)I
    //   283: istore_1
    //   284: iload_2
    //   285: istore #4
    //   287: goto -> 376
    //   290: iload #6
    //   292: iconst_1
    //   293: if_icmpne -> 318
    //   296: iload_3
    //   297: aload #7
    //   299: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   302: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   305: invokevirtual getFinalValue : ()I
    //   308: invokestatic max : (II)I
    //   311: istore_1
    //   312: iload_2
    //   313: istore #4
    //   315: goto -> 376
    //   318: iload #6
    //   320: iconst_2
    //   321: if_icmpne -> 346
    //   324: iload_3
    //   325: aload #7
    //   327: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   330: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   333: invokevirtual getFinalValue : ()I
    //   336: invokestatic min : (II)I
    //   339: istore_1
    //   340: iload_2
    //   341: istore #4
    //   343: goto -> 376
    //   346: iload_3
    //   347: istore_1
    //   348: iload_2
    //   349: istore #4
    //   351: iload #6
    //   353: iconst_3
    //   354: if_icmpne -> 376
    //   357: iload_3
    //   358: aload #7
    //   360: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   363: invokevirtual getAnchor : (Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   366: invokevirtual getFinalValue : ()I
    //   369: invokestatic max : (II)I
    //   372: istore_1
    //   373: iload_2
    //   374: istore #4
    //   376: iinc #5, 1
    //   379: goto -> 126
    //   382: iload_1
    //   383: aload_0
    //   384: getfield mMargin : I
    //   387: iadd
    //   388: istore_1
    //   389: aload_0
    //   390: getfield mBarrierType : I
    //   393: istore_2
    //   394: iload_2
    //   395: ifeq -> 415
    //   398: iload_2
    //   399: iconst_1
    //   400: if_icmpne -> 406
    //   403: goto -> 415
    //   406: aload_0
    //   407: iload_1
    //   408: iload_1
    //   409: invokevirtual setFinalVertical : (II)V
    //   412: goto -> 421
    //   415: aload_0
    //   416: iload_1
    //   417: iload_1
    //   418: invokevirtual setFinalHorizontal : (II)V
    //   421: aload_0
    //   422: iconst_1
    //   423: putfield resolved : Z
    //   426: iconst_1
    //   427: ireturn
    //   428: iconst_0
    //   429: ireturn
  }
  
  public boolean allowedInBarrier() {
    return true;
  }
  
  @Deprecated
  public boolean allowsGoneWidget() {
    return this.mAllowsGoneWidget;
  }
  
  public void copy(ConstraintWidget paramConstraintWidget, HashMap<ConstraintWidget, ConstraintWidget> paramHashMap) {
    super.copy(paramConstraintWidget, paramHashMap);
    paramConstraintWidget = paramConstraintWidget;
    this.mBarrierType = ((Barrier)paramConstraintWidget).mBarrierType;
    this.mAllowsGoneWidget = ((Barrier)paramConstraintWidget).mAllowsGoneWidget;
    this.mMargin = ((Barrier)paramConstraintWidget).mMargin;
  }
  
  public boolean getAllowsGoneWidget() {
    return this.mAllowsGoneWidget;
  }
  
  public int getBarrierType() {
    return this.mBarrierType;
  }
  
  public int getMargin() {
    return this.mMargin;
  }
  
  public int getOrientation() {
    switch (this.mBarrierType) {
      default:
        return -1;
      case 2:
      case 3:
        return 1;
      case 0:
      case 1:
        break;
    } 
    return 0;
  }
  
  public boolean isResolvedHorizontally() {
    return this.resolved;
  }
  
  public boolean isResolvedVertically() {
    return this.resolved;
  }
  
  protected void markWidgets() {
    for (byte b = 0; b < this.mWidgetsCount; b++) {
      ConstraintWidget constraintWidget = this.mWidgets[b];
      if (this.mAllowsGoneWidget || constraintWidget.allowedInBarrier()) {
        int i = this.mBarrierType;
        if (i == 0 || i == 1) {
          constraintWidget.setInBarrier(0, true);
        } else if (i == 2 || i == 3) {
          constraintWidget.setInBarrier(1, true);
        } 
      } 
    } 
  }
  
  public void setAllowsGoneWidget(boolean paramBoolean) {
    this.mAllowsGoneWidget = paramBoolean;
  }
  
  public void setBarrierType(int paramInt) {
    this.mBarrierType = paramInt;
  }
  
  public void setMargin(int paramInt) {
    this.mMargin = paramInt;
  }
  
  public String toString() {
    String str = "[Barrier] " + getDebugName() + " {";
    for (byte b = 0; b < this.mWidgetsCount; b++) {
      ConstraintWidget constraintWidget = this.mWidgets[b];
      String str1 = str;
      if (b > 0)
        str1 = str + ", "; 
      str = str1 + constraintWidget.getDebugName();
    } 
    return str + "}";
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\widgets\Barrier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */