package androidx.constraintlayout.core.widgets;

import androidx.constraintlayout.core.LinearSystem;
import java.util.ArrayList;

public class Chain {
  private static final boolean DEBUG = false;
  
  public static final boolean USE_CHAIN_OPTIMIZATION = false;
  
  static void applyChainConstraints(ConstraintWidgetContainer paramConstraintWidgetContainer, LinearSystem paramLinearSystem, int paramInt1, int paramInt2, ChainHead paramChainHead) {
    // Byte code:
    //   0: aload #4
    //   2: getfield mFirst : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   5: astore #22
    //   7: aload #4
    //   9: getfield mLast : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   12: astore #25
    //   14: aload #4
    //   16: getfield mFirstVisibleWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   19: astore #18
    //   21: aload #4
    //   23: getfield mLastVisibleWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   26: astore #24
    //   28: aload #4
    //   30: getfield mHead : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   33: astore #20
    //   35: aload #4
    //   37: getfield mTotalWeight : F
    //   40: fstore #5
    //   42: aload #4
    //   44: getfield mFirstMatchConstraintWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   47: astore #23
    //   49: aload #4
    //   51: getfield mLastMatchConstraintWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   54: astore #21
    //   56: aload_0
    //   57: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   60: iload_2
    //   61: aaload
    //   62: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   65: if_acmpne -> 74
    //   68: iconst_1
    //   69: istore #13
    //   71: goto -> 77
    //   74: iconst_0
    //   75: istore #13
    //   77: iload_2
    //   78: ifne -> 164
    //   81: aload #20
    //   83: getfield mHorizontalChainStyle : I
    //   86: ifne -> 95
    //   89: iconst_1
    //   90: istore #8
    //   92: goto -> 98
    //   95: iconst_0
    //   96: istore #8
    //   98: aload #20
    //   100: getfield mHorizontalChainStyle : I
    //   103: istore #9
    //   105: iload #8
    //   107: istore #11
    //   109: iload #9
    //   111: iconst_1
    //   112: if_icmpne -> 121
    //   115: iconst_1
    //   116: istore #8
    //   118: goto -> 124
    //   121: iconst_0
    //   122: istore #8
    //   124: aload #20
    //   126: getfield mHorizontalChainStyle : I
    //   129: iconst_2
    //   130: if_icmpne -> 139
    //   133: iconst_1
    //   134: istore #9
    //   136: goto -> 142
    //   139: iconst_0
    //   140: istore #9
    //   142: iconst_0
    //   143: istore #10
    //   145: aload #22
    //   147: astore #17
    //   149: iload #8
    //   151: istore #12
    //   153: iload #9
    //   155: istore #14
    //   157: iload #10
    //   159: istore #8
    //   161: goto -> 244
    //   164: aload #20
    //   166: getfield mVerticalChainStyle : I
    //   169: ifne -> 178
    //   172: iconst_1
    //   173: istore #8
    //   175: goto -> 181
    //   178: iconst_0
    //   179: istore #8
    //   181: aload #20
    //   183: getfield mVerticalChainStyle : I
    //   186: istore #9
    //   188: iload #8
    //   190: istore #11
    //   192: iload #9
    //   194: iconst_1
    //   195: if_icmpne -> 204
    //   198: iconst_1
    //   199: istore #8
    //   201: goto -> 207
    //   204: iconst_0
    //   205: istore #8
    //   207: aload #20
    //   209: getfield mVerticalChainStyle : I
    //   212: iconst_2
    //   213: if_icmpne -> 222
    //   216: iconst_1
    //   217: istore #9
    //   219: goto -> 225
    //   222: iconst_0
    //   223: istore #9
    //   225: iconst_0
    //   226: istore #10
    //   228: aload #22
    //   230: astore #17
    //   232: iload #8
    //   234: istore #12
    //   236: iload #10
    //   238: istore #8
    //   240: iload #9
    //   242: istore #14
    //   244: iload #8
    //   246: ifne -> 685
    //   249: aload #17
    //   251: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   254: iload_3
    //   255: aaload
    //   256: astore #19
    //   258: iconst_4
    //   259: istore #9
    //   261: iload #14
    //   263: ifeq -> 269
    //   266: iconst_1
    //   267: istore #9
    //   269: aload #19
    //   271: invokevirtual getMargin : ()I
    //   274: istore #15
    //   276: aload #17
    //   278: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   281: iload_2
    //   282: aaload
    //   283: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   286: if_acmpne -> 305
    //   289: aload #17
    //   291: getfield mResolvedMatchConstraintDefault : [I
    //   294: iload_2
    //   295: iaload
    //   296: ifne -> 305
    //   299: iconst_1
    //   300: istore #16
    //   302: goto -> 308
    //   305: iconst_0
    //   306: istore #16
    //   308: aload #19
    //   310: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   313: ifnull -> 339
    //   316: aload #17
    //   318: aload #22
    //   320: if_acmpeq -> 339
    //   323: iload #15
    //   325: aload #19
    //   327: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   330: invokevirtual getMargin : ()I
    //   333: iadd
    //   334: istore #15
    //   336: goto -> 339
    //   339: iload #9
    //   341: istore #10
    //   343: iload #14
    //   345: ifeq -> 374
    //   348: iload #9
    //   350: istore #10
    //   352: aload #17
    //   354: aload #22
    //   356: if_acmpeq -> 374
    //   359: iload #9
    //   361: istore #10
    //   363: aload #17
    //   365: aload #18
    //   367: if_acmpeq -> 374
    //   370: bipush #8
    //   372: istore #10
    //   374: aload #19
    //   376: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   379: ifnull -> 507
    //   382: aload #17
    //   384: aload #18
    //   386: if_acmpne -> 413
    //   389: aload_1
    //   390: aload #19
    //   392: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   395: aload #19
    //   397: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   400: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   403: iload #15
    //   405: bipush #6
    //   407: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   410: goto -> 434
    //   413: aload_1
    //   414: aload #19
    //   416: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   419: aload #19
    //   421: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   424: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   427: iload #15
    //   429: bipush #8
    //   431: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   434: iload #10
    //   436: istore #9
    //   438: iload #16
    //   440: ifeq -> 455
    //   443: iload #10
    //   445: istore #9
    //   447: iload #14
    //   449: ifne -> 455
    //   452: iconst_5
    //   453: istore #9
    //   455: aload #17
    //   457: aload #18
    //   459: if_acmpne -> 482
    //   462: iload #14
    //   464: ifeq -> 482
    //   467: aload #17
    //   469: iload_2
    //   470: invokevirtual isInBarrier : (I)Z
    //   473: ifeq -> 482
    //   476: iconst_5
    //   477: istore #9
    //   479: goto -> 482
    //   482: aload_1
    //   483: aload #19
    //   485: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   488: aload #19
    //   490: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   493: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   496: iload #15
    //   498: iload #9
    //   500: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   503: pop
    //   504: goto -> 507
    //   507: iload #13
    //   509: ifeq -> 595
    //   512: aload #17
    //   514: invokevirtual getVisibility : ()I
    //   517: bipush #8
    //   519: if_icmpeq -> 566
    //   522: aload #17
    //   524: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   527: iload_2
    //   528: aaload
    //   529: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   532: if_acmpne -> 566
    //   535: aload_1
    //   536: aload #17
    //   538: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   541: iload_3
    //   542: iconst_1
    //   543: iadd
    //   544: aaload
    //   545: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   548: aload #17
    //   550: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   553: iload_3
    //   554: aaload
    //   555: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   558: iconst_0
    //   559: iconst_5
    //   560: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   563: goto -> 566
    //   566: aload_1
    //   567: aload #17
    //   569: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   572: iload_3
    //   573: aaload
    //   574: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   577: aload_0
    //   578: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   581: iload_3
    //   582: aaload
    //   583: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   586: iconst_0
    //   587: bipush #8
    //   589: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   592: goto -> 595
    //   595: aload #17
    //   597: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   600: iload_3
    //   601: iconst_1
    //   602: iadd
    //   603: aaload
    //   604: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   607: astore #19
    //   609: aload #19
    //   611: ifnull -> 664
    //   614: aload #19
    //   616: getfield mOwner : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   619: astore #19
    //   621: aload #19
    //   623: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   626: iload_3
    //   627: aaload
    //   628: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   631: ifnull -> 658
    //   634: aload #19
    //   636: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   639: iload_3
    //   640: aaload
    //   641: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   644: getfield mOwner : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   647: aload #17
    //   649: if_acmpeq -> 655
    //   652: goto -> 658
    //   655: goto -> 667
    //   658: aconst_null
    //   659: astore #19
    //   661: goto -> 667
    //   664: aconst_null
    //   665: astore #19
    //   667: aload #19
    //   669: ifnull -> 679
    //   672: aload #19
    //   674: astore #17
    //   676: goto -> 682
    //   679: iconst_1
    //   680: istore #8
    //   682: goto -> 244
    //   685: aload #24
    //   687: ifnull -> 872
    //   690: aload #25
    //   692: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   695: iload_3
    //   696: iconst_1
    //   697: iadd
    //   698: aaload
    //   699: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   702: ifnull -> 872
    //   705: aload #24
    //   707: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   710: iload_3
    //   711: iconst_1
    //   712: iadd
    //   713: aaload
    //   714: astore #19
    //   716: aload #24
    //   718: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   721: iload_2
    //   722: aaload
    //   723: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   726: if_acmpne -> 745
    //   729: aload #24
    //   731: getfield mResolvedMatchConstraintDefault : [I
    //   734: iload_2
    //   735: iaload
    //   736: ifne -> 745
    //   739: iconst_1
    //   740: istore #8
    //   742: goto -> 748
    //   745: iconst_0
    //   746: istore #8
    //   748: iload #8
    //   750: ifeq -> 798
    //   753: iload #14
    //   755: ifne -> 798
    //   758: aload #19
    //   760: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   763: getfield mOwner : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   766: aload_0
    //   767: if_acmpne -> 798
    //   770: aload_1
    //   771: aload #19
    //   773: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   776: aload #19
    //   778: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   781: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   784: aload #19
    //   786: invokevirtual getMargin : ()I
    //   789: ineg
    //   790: iconst_5
    //   791: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   794: pop
    //   795: goto -> 840
    //   798: iload #14
    //   800: ifeq -> 840
    //   803: aload #19
    //   805: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   808: getfield mOwner : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   811: aload_0
    //   812: if_acmpne -> 840
    //   815: aload_1
    //   816: aload #19
    //   818: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   821: aload #19
    //   823: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   826: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   829: aload #19
    //   831: invokevirtual getMargin : ()I
    //   834: ineg
    //   835: iconst_4
    //   836: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   839: pop
    //   840: aload_1
    //   841: aload #19
    //   843: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   846: aload #25
    //   848: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   851: iload_3
    //   852: iconst_1
    //   853: iadd
    //   854: aaload
    //   855: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   858: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   861: aload #19
    //   863: invokevirtual getMargin : ()I
    //   866: ineg
    //   867: bipush #6
    //   869: invokevirtual addLowerThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   872: iload #13
    //   874: ifeq -> 918
    //   877: aload_1
    //   878: aload_0
    //   879: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   882: iload_3
    //   883: iconst_1
    //   884: iadd
    //   885: aaload
    //   886: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   889: aload #25
    //   891: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   894: iload_3
    //   895: iconst_1
    //   896: iadd
    //   897: aaload
    //   898: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   901: aload #25
    //   903: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   906: iload_3
    //   907: iconst_1
    //   908: iadd
    //   909: aaload
    //   910: invokevirtual getMargin : ()I
    //   913: bipush #8
    //   915: invokevirtual addGreaterThan : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   918: aload #4
    //   920: getfield mWeightedMatchConstraintsWidgets : Ljava/util/ArrayList;
    //   923: astore_0
    //   924: aload_0
    //   925: ifnull -> 1232
    //   928: aload_0
    //   929: invokevirtual size : ()I
    //   932: istore #9
    //   934: iload #9
    //   936: iconst_1
    //   937: if_icmple -> 1219
    //   940: aconst_null
    //   941: astore #19
    //   943: fconst_0
    //   944: fstore #7
    //   946: aload #4
    //   948: getfield mHasUndefinedWeights : Z
    //   951: ifeq -> 973
    //   954: aload #4
    //   956: getfield mHasComplexMatchWeights : Z
    //   959: ifne -> 973
    //   962: aload #4
    //   964: getfield mWidgetsMatchCount : I
    //   967: i2f
    //   968: fstore #6
    //   970: goto -> 977
    //   973: fload #5
    //   975: fstore #6
    //   977: iconst_0
    //   978: istore #8
    //   980: iload #8
    //   982: iload #9
    //   984: if_icmpge -> 1206
    //   987: aload_0
    //   988: iload #8
    //   990: invokevirtual get : (I)Ljava/lang/Object;
    //   993: checkcast androidx/constraintlayout/core/widgets/ConstraintWidget
    //   996: astore #21
    //   998: aload #21
    //   1000: getfield mWeight : [F
    //   1003: iload_2
    //   1004: faload
    //   1005: fstore #5
    //   1007: fload #5
    //   1009: fconst_0
    //   1010: fcmpg
    //   1011: ifge -> 1060
    //   1014: aload #4
    //   1016: getfield mHasComplexMatchWeights : Z
    //   1019: ifeq -> 1054
    //   1022: aload_1
    //   1023: aload #21
    //   1025: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1028: iload_3
    //   1029: iconst_1
    //   1030: iadd
    //   1031: aaload
    //   1032: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   1035: aload #21
    //   1037: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1040: iload_3
    //   1041: aaload
    //   1042: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   1045: iconst_0
    //   1046: iconst_4
    //   1047: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   1050: pop
    //   1051: goto -> 1200
    //   1054: fconst_1
    //   1055: fstore #5
    //   1057: goto -> 1060
    //   1060: fload #5
    //   1062: fconst_0
    //   1063: fcmpl
    //   1064: ifne -> 1100
    //   1067: aload_1
    //   1068: aload #21
    //   1070: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1073: iload_3
    //   1074: iconst_1
    //   1075: iadd
    //   1076: aaload
    //   1077: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   1080: aload #21
    //   1082: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1085: iload_3
    //   1086: aaload
    //   1087: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   1090: iconst_0
    //   1091: bipush #8
    //   1093: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   1096: pop
    //   1097: goto -> 1200
    //   1100: aload #19
    //   1102: ifnull -> 1192
    //   1105: aload #19
    //   1107: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1110: iload_3
    //   1111: aaload
    //   1112: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   1115: astore #23
    //   1117: aload #19
    //   1119: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1122: iload_3
    //   1123: iconst_1
    //   1124: iadd
    //   1125: aaload
    //   1126: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   1129: astore #27
    //   1131: aload #21
    //   1133: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1136: iload_3
    //   1137: aaload
    //   1138: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   1141: astore #19
    //   1143: aload #21
    //   1145: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1148: iload_3
    //   1149: iconst_1
    //   1150: iadd
    //   1151: aaload
    //   1152: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   1155: astore #26
    //   1157: aload_1
    //   1158: invokevirtual createRow : ()Landroidx/constraintlayout/core/ArrayRow;
    //   1161: astore #28
    //   1163: aload #28
    //   1165: fload #7
    //   1167: fload #6
    //   1169: fload #5
    //   1171: aload #23
    //   1173: aload #27
    //   1175: aload #19
    //   1177: aload #26
    //   1179: invokevirtual createRowEqualMatchDimensions : (FFFLandroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;)Landroidx/constraintlayout/core/ArrayRow;
    //   1182: pop
    //   1183: aload_1
    //   1184: aload #28
    //   1186: invokevirtual addConstraint : (Landroidx/constraintlayout/core/ArrayRow;)V
    //   1189: goto -> 1192
    //   1192: aload #21
    //   1194: astore #19
    //   1196: fload #5
    //   1198: fstore #7
    //   1200: iinc #8, 1
    //   1203: goto -> 980
    //   1206: aload_0
    //   1207: astore #19
    //   1209: aload #20
    //   1211: astore_0
    //   1212: aload #19
    //   1214: astore #17
    //   1216: goto -> 1246
    //   1219: aload_0
    //   1220: astore #19
    //   1222: aload #20
    //   1224: astore_0
    //   1225: aload #19
    //   1227: astore #17
    //   1229: goto -> 1246
    //   1232: aload #17
    //   1234: astore #19
    //   1236: aload_0
    //   1237: astore #17
    //   1239: aload #20
    //   1241: astore_0
    //   1242: aload #19
    //   1244: astore #17
    //   1246: aload #18
    //   1248: ifnull -> 1438
    //   1251: aload #18
    //   1253: aload #24
    //   1255: if_acmpeq -> 1269
    //   1258: iload #14
    //   1260: ifeq -> 1266
    //   1263: goto -> 1269
    //   1266: goto -> 1438
    //   1269: aload #22
    //   1271: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1274: iload_3
    //   1275: aaload
    //   1276: astore #4
    //   1278: aload #25
    //   1280: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1283: iload_3
    //   1284: iconst_1
    //   1285: iadd
    //   1286: aaload
    //   1287: astore #19
    //   1289: aload #4
    //   1291: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1294: ifnull -> 1310
    //   1297: aload #4
    //   1299: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1302: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   1305: astore #4
    //   1307: goto -> 1313
    //   1310: aconst_null
    //   1311: astore #4
    //   1313: aload #19
    //   1315: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1318: ifnull -> 1334
    //   1321: aload #19
    //   1323: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1326: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   1329: astore #17
    //   1331: goto -> 1337
    //   1334: aconst_null
    //   1335: astore #17
    //   1337: aload #18
    //   1339: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1342: iload_3
    //   1343: aaload
    //   1344: astore #20
    //   1346: aload #24
    //   1348: ifnull -> 1365
    //   1351: aload #24
    //   1353: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1356: iload_3
    //   1357: iconst_1
    //   1358: iadd
    //   1359: aaload
    //   1360: astore #19
    //   1362: goto -> 1365
    //   1365: aload #4
    //   1367: ifnull -> 1435
    //   1370: aload #17
    //   1372: ifnull -> 1435
    //   1375: iload_2
    //   1376: ifne -> 1388
    //   1379: aload_0
    //   1380: getfield mHorizontalBiasPercent : F
    //   1383: fstore #5
    //   1385: goto -> 1394
    //   1388: aload_0
    //   1389: getfield mVerticalBiasPercent : F
    //   1392: fstore #5
    //   1394: aload #20
    //   1396: invokevirtual getMargin : ()I
    //   1399: istore_2
    //   1400: aload #19
    //   1402: invokevirtual getMargin : ()I
    //   1405: istore #8
    //   1407: aload_1
    //   1408: aload #20
    //   1410: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   1413: aload #4
    //   1415: iload_2
    //   1416: fload #5
    //   1418: aload #17
    //   1420: aload #19
    //   1422: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   1425: iload #8
    //   1427: bipush #7
    //   1429: invokevirtual addCentering : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;IFLandroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   1432: goto -> 1435
    //   1435: goto -> 2483
    //   1438: iload #11
    //   1440: ifeq -> 1909
    //   1443: aload #18
    //   1445: ifnull -> 1909
    //   1448: aload #4
    //   1450: getfield mWidgetsMatchCount : I
    //   1453: ifle -> 1475
    //   1456: aload #4
    //   1458: getfield mWidgetsCount : I
    //   1461: aload #4
    //   1463: getfield mWidgetsMatchCount : I
    //   1466: if_icmpne -> 1475
    //   1469: iconst_1
    //   1470: istore #9
    //   1472: goto -> 1478
    //   1475: iconst_0
    //   1476: istore #9
    //   1478: aload #18
    //   1480: astore #17
    //   1482: aload #18
    //   1484: astore_0
    //   1485: aload #17
    //   1487: ifnull -> 1906
    //   1490: aload #17
    //   1492: getfield mNextChainWidget : [Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   1495: iload_2
    //   1496: aaload
    //   1497: astore #4
    //   1499: aload #4
    //   1501: ifnull -> 1526
    //   1504: aload #4
    //   1506: invokevirtual getVisibility : ()I
    //   1509: bipush #8
    //   1511: if_icmpne -> 1526
    //   1514: aload #4
    //   1516: getfield mNextChainWidget : [Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   1519: iload_2
    //   1520: aaload
    //   1521: astore #4
    //   1523: goto -> 1499
    //   1526: aload #4
    //   1528: ifnonnull -> 1544
    //   1531: aload #17
    //   1533: aload #24
    //   1535: if_acmpne -> 1541
    //   1538: goto -> 1544
    //   1541: goto -> 1883
    //   1544: aload #17
    //   1546: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1549: iload_3
    //   1550: aaload
    //   1551: astore #20
    //   1553: aload #20
    //   1555: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   1558: astore #23
    //   1560: aload #20
    //   1562: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1565: ifnull -> 1581
    //   1568: aload #20
    //   1570: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1573: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   1576: astore #19
    //   1578: goto -> 1584
    //   1581: aconst_null
    //   1582: astore #19
    //   1584: aload_0
    //   1585: aload #17
    //   1587: if_acmpeq -> 1606
    //   1590: aload_0
    //   1591: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1594: iload_3
    //   1595: iconst_1
    //   1596: iadd
    //   1597: aaload
    //   1598: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   1601: astore #19
    //   1603: goto -> 1650
    //   1606: aload #17
    //   1608: aload #18
    //   1610: if_acmpne -> 1650
    //   1613: aload #22
    //   1615: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1618: iload_3
    //   1619: aaload
    //   1620: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1623: ifnull -> 1644
    //   1626: aload #22
    //   1628: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1631: iload_3
    //   1632: aaload
    //   1633: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1636: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   1639: astore #19
    //   1641: goto -> 1647
    //   1644: aconst_null
    //   1645: astore #19
    //   1647: goto -> 1650
    //   1650: aload #20
    //   1652: invokevirtual getMargin : ()I
    //   1655: istore #10
    //   1657: aload #17
    //   1659: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1662: iload_3
    //   1663: iconst_1
    //   1664: iadd
    //   1665: aaload
    //   1666: invokevirtual getMargin : ()I
    //   1669: istore #8
    //   1671: aload #4
    //   1673: ifnull -> 1695
    //   1676: aload #4
    //   1678: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1681: iload_3
    //   1682: aaload
    //   1683: astore #20
    //   1685: aload #20
    //   1687: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   1690: astore #21
    //   1692: goto -> 1727
    //   1695: aload #25
    //   1697: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1700: iload_3
    //   1701: iconst_1
    //   1702: iadd
    //   1703: aaload
    //   1704: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1707: astore #20
    //   1709: aload #20
    //   1711: ifnull -> 1724
    //   1714: aload #20
    //   1716: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   1719: astore #21
    //   1721: goto -> 1727
    //   1724: aconst_null
    //   1725: astore #21
    //   1727: aload #17
    //   1729: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1732: iload_3
    //   1733: iconst_1
    //   1734: iadd
    //   1735: aaload
    //   1736: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   1739: astore #26
    //   1741: aload #20
    //   1743: ifnull -> 1759
    //   1746: iload #8
    //   1748: aload #20
    //   1750: invokevirtual getMargin : ()I
    //   1753: iadd
    //   1754: istore #8
    //   1756: goto -> 1759
    //   1759: aload_0
    //   1760: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1763: iload_3
    //   1764: iconst_1
    //   1765: iadd
    //   1766: aaload
    //   1767: invokevirtual getMargin : ()I
    //   1770: istore #13
    //   1772: aload #23
    //   1774: ifnull -> 1883
    //   1777: aload #19
    //   1779: ifnull -> 1883
    //   1782: aload #21
    //   1784: ifnull -> 1883
    //   1787: aload #26
    //   1789: ifnull -> 1883
    //   1792: aload #17
    //   1794: aload #18
    //   1796: if_acmpne -> 1814
    //   1799: aload #18
    //   1801: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1804: iload_3
    //   1805: aaload
    //   1806: invokevirtual getMargin : ()I
    //   1809: istore #10
    //   1811: goto -> 1821
    //   1814: iload #10
    //   1816: iload #13
    //   1818: iadd
    //   1819: istore #10
    //   1821: aload #17
    //   1823: aload #24
    //   1825: if_acmpne -> 1845
    //   1828: aload #24
    //   1830: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   1833: iload_3
    //   1834: iconst_1
    //   1835: iadd
    //   1836: aaload
    //   1837: invokevirtual getMargin : ()I
    //   1840: istore #8
    //   1842: goto -> 1845
    //   1845: iload #9
    //   1847: ifeq -> 1857
    //   1850: bipush #8
    //   1852: istore #13
    //   1854: goto -> 1860
    //   1857: iconst_5
    //   1858: istore #13
    //   1860: aload_1
    //   1861: aload #23
    //   1863: aload #19
    //   1865: iload #10
    //   1867: ldc 0.5
    //   1869: aload #21
    //   1871: aload #26
    //   1873: iload #8
    //   1875: iload #13
    //   1877: invokevirtual addCentering : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;IFLandroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   1880: goto -> 1883
    //   1883: aload #17
    //   1885: invokevirtual getVisibility : ()I
    //   1888: bipush #8
    //   1890: if_icmpeq -> 1899
    //   1893: aload #17
    //   1895: astore_0
    //   1896: goto -> 1899
    //   1899: aload #4
    //   1901: astore #17
    //   1903: goto -> 1485
    //   1906: goto -> 2483
    //   1909: bipush #8
    //   1911: istore #8
    //   1913: iload #12
    //   1915: ifeq -> 2483
    //   1918: aload #18
    //   1920: ifnull -> 2483
    //   1923: aload #4
    //   1925: getfield mWidgetsMatchCount : I
    //   1928: ifle -> 1950
    //   1931: aload #4
    //   1933: getfield mWidgetsCount : I
    //   1936: aload #4
    //   1938: getfield mWidgetsMatchCount : I
    //   1941: if_icmpne -> 1950
    //   1944: iconst_1
    //   1945: istore #9
    //   1947: goto -> 1953
    //   1950: iconst_0
    //   1951: istore #9
    //   1953: aload #18
    //   1955: astore #17
    //   1957: aload #18
    //   1959: astore #4
    //   1961: aload #17
    //   1963: ifnull -> 2321
    //   1966: aload #17
    //   1968: getfield mNextChainWidget : [Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   1971: iload_2
    //   1972: aaload
    //   1973: astore_0
    //   1974: aload_0
    //   1975: ifnull -> 1997
    //   1978: aload_0
    //   1979: invokevirtual getVisibility : ()I
    //   1982: iload #8
    //   1984: if_icmpne -> 1997
    //   1987: aload_0
    //   1988: getfield mNextChainWidget : [Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   1991: iload_2
    //   1992: aaload
    //   1993: astore_0
    //   1994: goto -> 1974
    //   1997: aload #17
    //   1999: aload #18
    //   2001: if_acmpeq -> 2298
    //   2004: aload #17
    //   2006: aload #24
    //   2008: if_acmpeq -> 2298
    //   2011: aload_0
    //   2012: ifnull -> 2298
    //   2015: aload_0
    //   2016: aload #24
    //   2018: if_acmpne -> 2026
    //   2021: aconst_null
    //   2022: astore_0
    //   2023: goto -> 2026
    //   2026: aload #17
    //   2028: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2031: iload_3
    //   2032: aaload
    //   2033: astore #20
    //   2035: aload #20
    //   2037: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   2040: astore #26
    //   2042: aload #20
    //   2044: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2047: ifnull -> 2063
    //   2050: aload #20
    //   2052: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2055: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   2058: astore #19
    //   2060: goto -> 2063
    //   2063: aload #4
    //   2065: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2068: iload_3
    //   2069: iconst_1
    //   2070: iadd
    //   2071: aaload
    //   2072: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   2075: astore #27
    //   2077: aconst_null
    //   2078: astore #19
    //   2080: aload #20
    //   2082: invokevirtual getMargin : ()I
    //   2085: istore #13
    //   2087: aload #17
    //   2089: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2092: iload_3
    //   2093: iconst_1
    //   2094: iadd
    //   2095: aaload
    //   2096: invokevirtual getMargin : ()I
    //   2099: istore #8
    //   2101: aload_0
    //   2102: ifnull -> 2155
    //   2105: aload_0
    //   2106: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2109: iload_3
    //   2110: aaload
    //   2111: astore #21
    //   2113: aload #21
    //   2115: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   2118: astore #23
    //   2120: aload #21
    //   2122: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2125: ifnull -> 2141
    //   2128: aload #21
    //   2130: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2133: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   2136: astore #19
    //   2138: goto -> 2144
    //   2141: aconst_null
    //   2142: astore #19
    //   2144: aload #19
    //   2146: astore #20
    //   2148: aload #23
    //   2150: astore #19
    //   2152: goto -> 2198
    //   2155: aload #24
    //   2157: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2160: iload_3
    //   2161: aaload
    //   2162: astore #20
    //   2164: aload #20
    //   2166: ifnull -> 2176
    //   2169: aload #20
    //   2171: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   2174: astore #19
    //   2176: aload #17
    //   2178: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2181: iload_3
    //   2182: iconst_1
    //   2183: iadd
    //   2184: aaload
    //   2185: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   2188: astore #23
    //   2190: aload #20
    //   2192: astore #21
    //   2194: aload #23
    //   2196: astore #20
    //   2198: aload #21
    //   2200: ifnull -> 2216
    //   2203: iload #8
    //   2205: aload #21
    //   2207: invokevirtual getMargin : ()I
    //   2210: iadd
    //   2211: istore #8
    //   2213: goto -> 2216
    //   2216: aload #4
    //   2218: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2221: iload_3
    //   2222: iconst_1
    //   2223: iadd
    //   2224: aaload
    //   2225: invokevirtual getMargin : ()I
    //   2228: istore #14
    //   2230: iload #9
    //   2232: ifeq -> 2242
    //   2235: bipush #8
    //   2237: istore #10
    //   2239: goto -> 2245
    //   2242: iconst_4
    //   2243: istore #10
    //   2245: aload #26
    //   2247: ifnull -> 2291
    //   2250: aload #27
    //   2252: ifnull -> 2291
    //   2255: aload #19
    //   2257: ifnull -> 2291
    //   2260: aload #20
    //   2262: ifnull -> 2291
    //   2265: aload_1
    //   2266: aload #26
    //   2268: aload #27
    //   2270: iload #13
    //   2272: iload #14
    //   2274: iadd
    //   2275: ldc 0.5
    //   2277: aload #19
    //   2279: aload #20
    //   2281: iload #8
    //   2283: iload #10
    //   2285: invokevirtual addCentering : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;IFLandroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   2288: goto -> 2291
    //   2291: bipush #8
    //   2293: istore #8
    //   2295: goto -> 2298
    //   2298: aload #17
    //   2300: invokevirtual getVisibility : ()I
    //   2303: iload #8
    //   2305: if_icmpeq -> 2315
    //   2308: aload #17
    //   2310: astore #4
    //   2312: goto -> 2315
    //   2315: aload_0
    //   2316: astore #17
    //   2318: goto -> 1961
    //   2321: aload #18
    //   2323: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2326: iload_3
    //   2327: aaload
    //   2328: astore_0
    //   2329: aload #22
    //   2331: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2334: iload_3
    //   2335: aaload
    //   2336: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2339: astore #17
    //   2341: aload #24
    //   2343: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2346: iload_3
    //   2347: iconst_1
    //   2348: iadd
    //   2349: aaload
    //   2350: astore #19
    //   2352: aload #25
    //   2354: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2357: iload_3
    //   2358: iconst_1
    //   2359: iadd
    //   2360: aaload
    //   2361: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2364: astore #4
    //   2366: aload #17
    //   2368: ifnull -> 2446
    //   2371: aload #18
    //   2373: aload #24
    //   2375: if_acmpeq -> 2400
    //   2378: aload_1
    //   2379: aload_0
    //   2380: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   2383: aload #17
    //   2385: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   2388: aload_0
    //   2389: invokevirtual getMargin : ()I
    //   2392: iconst_5
    //   2393: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   2396: pop
    //   2397: goto -> 2446
    //   2400: aload #4
    //   2402: ifnull -> 2443
    //   2405: aload_1
    //   2406: aload_0
    //   2407: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   2410: aload #17
    //   2412: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   2415: aload_0
    //   2416: invokevirtual getMargin : ()I
    //   2419: ldc 0.5
    //   2421: aload #19
    //   2423: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   2426: aload #4
    //   2428: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   2431: aload #19
    //   2433: invokevirtual getMargin : ()I
    //   2436: iconst_5
    //   2437: invokevirtual addCentering : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;IFLandroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   2440: goto -> 2446
    //   2443: goto -> 2446
    //   2446: aload #4
    //   2448: ifnull -> 2483
    //   2451: aload #18
    //   2453: aload #24
    //   2455: if_acmpeq -> 2483
    //   2458: aload_1
    //   2459: aload #19
    //   2461: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   2464: aload #4
    //   2466: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   2469: aload #19
    //   2471: invokevirtual getMargin : ()I
    //   2474: ineg
    //   2475: iconst_5
    //   2476: invokevirtual addEquality : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)Landroidx/constraintlayout/core/ArrayRow;
    //   2479: pop
    //   2480: goto -> 2483
    //   2483: aload #24
    //   2485: astore #4
    //   2487: iload #11
    //   2489: ifne -> 2500
    //   2492: aload #4
    //   2494: astore_0
    //   2495: iload #12
    //   2497: ifeq -> 2735
    //   2500: aload #4
    //   2502: astore_0
    //   2503: aload #18
    //   2505: ifnull -> 2735
    //   2508: aload #4
    //   2510: astore_0
    //   2511: aload #18
    //   2513: aload #4
    //   2515: if_acmpeq -> 2735
    //   2518: aload #18
    //   2520: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2523: iload_3
    //   2524: aaload
    //   2525: astore #19
    //   2527: aload #4
    //   2529: astore_0
    //   2530: aload #4
    //   2532: ifnonnull -> 2538
    //   2535: aload #18
    //   2537: astore_0
    //   2538: aload_0
    //   2539: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2542: iload_3
    //   2543: iconst_1
    //   2544: iadd
    //   2545: aaload
    //   2546: astore #20
    //   2548: aload #19
    //   2550: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2553: ifnull -> 2569
    //   2556: aload #19
    //   2558: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2561: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   2564: astore #17
    //   2566: goto -> 2572
    //   2569: aconst_null
    //   2570: astore #17
    //   2572: aload #20
    //   2574: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2577: ifnull -> 2593
    //   2580: aload #20
    //   2582: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2585: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   2588: astore #4
    //   2590: goto -> 2596
    //   2593: aconst_null
    //   2594: astore #4
    //   2596: aload #25
    //   2598: aload_0
    //   2599: if_acmpeq -> 2640
    //   2602: aload #25
    //   2604: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2607: iload_3
    //   2608: iconst_1
    //   2609: iadd
    //   2610: aaload
    //   2611: astore #4
    //   2613: aload #4
    //   2615: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2618: ifnull -> 2634
    //   2621: aload #4
    //   2623: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2626: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   2629: astore #4
    //   2631: goto -> 2637
    //   2634: aconst_null
    //   2635: astore #4
    //   2637: goto -> 2640
    //   2640: aload #18
    //   2642: aload_0
    //   2643: if_acmpne -> 2673
    //   2646: aload #18
    //   2648: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2651: iload_3
    //   2652: aaload
    //   2653: astore #20
    //   2655: aload #18
    //   2657: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2660: iload_3
    //   2661: iconst_1
    //   2662: iadd
    //   2663: aaload
    //   2664: astore #19
    //   2666: aload #20
    //   2668: astore #18
    //   2670: goto -> 2681
    //   2673: aload #19
    //   2675: astore #18
    //   2677: aload #20
    //   2679: astore #19
    //   2681: aload #17
    //   2683: ifnull -> 2735
    //   2686: aload #4
    //   2688: ifnull -> 2735
    //   2691: aload #18
    //   2693: invokevirtual getMargin : ()I
    //   2696: istore_2
    //   2697: aload_0
    //   2698: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   2701: iload_3
    //   2702: iconst_1
    //   2703: iadd
    //   2704: aaload
    //   2705: invokevirtual getMargin : ()I
    //   2708: istore_3
    //   2709: aload_1
    //   2710: aload #18
    //   2712: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   2715: aload #17
    //   2717: iload_2
    //   2718: ldc 0.5
    //   2720: aload #4
    //   2722: aload #19
    //   2724: getfield mSolverVariable : Landroidx/constraintlayout/core/SolverVariable;
    //   2727: iload_3
    //   2728: iconst_5
    //   2729: invokevirtual addCentering : (Landroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;IFLandroidx/constraintlayout/core/SolverVariable;Landroidx/constraintlayout/core/SolverVariable;II)V
    //   2732: goto -> 2735
    //   2735: return
  }
  
  public static void applyChainConstraints(ConstraintWidgetContainer paramConstraintWidgetContainer, LinearSystem paramLinearSystem, ArrayList<ConstraintWidget> paramArrayList, int paramInt) {
    byte b1;
    int i;
    ChainHead[] arrayOfChainHead;
    if (paramInt == 0) {
      b1 = 0;
      i = paramConstraintWidgetContainer.mHorizontalChainsSize;
      arrayOfChainHead = paramConstraintWidgetContainer.mHorizontalChainsArray;
    } else {
      b1 = 2;
      i = paramConstraintWidgetContainer.mVerticalChainsSize;
      arrayOfChainHead = paramConstraintWidgetContainer.mVerticalChainsArray;
    } 
    for (byte b2 = 0; b2 < i; b2++) {
      ChainHead chainHead = arrayOfChainHead[b2];
      chainHead.define();
      if (paramArrayList == null || (paramArrayList != null && paramArrayList.contains(chainHead.mFirst)))
        applyChainConstraints(paramConstraintWidgetContainer, paramLinearSystem, paramInt, b1, chainHead); 
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\widgets\Chain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */