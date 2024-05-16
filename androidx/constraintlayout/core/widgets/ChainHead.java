package androidx.constraintlayout.core.widgets;

import java.util.ArrayList;

public class ChainHead {
  private boolean mDefined;
  
  protected ConstraintWidget mFirst;
  
  protected ConstraintWidget mFirstMatchConstraintWidget;
  
  protected ConstraintWidget mFirstVisibleWidget;
  
  protected boolean mHasComplexMatchWeights;
  
  protected boolean mHasDefinedWeights;
  
  protected boolean mHasRatio;
  
  protected boolean mHasUndefinedWeights;
  
  protected ConstraintWidget mHead;
  
  private boolean mIsRtl = false;
  
  protected ConstraintWidget mLast;
  
  protected ConstraintWidget mLastMatchConstraintWidget;
  
  protected ConstraintWidget mLastVisibleWidget;
  
  boolean mOptimizable;
  
  private int mOrientation;
  
  int mTotalMargins;
  
  int mTotalSize;
  
  protected float mTotalWeight = 0.0F;
  
  int mVisibleWidgets;
  
  protected ArrayList<ConstraintWidget> mWeightedMatchConstraintsWidgets;
  
  protected int mWidgetsCount;
  
  protected int mWidgetsMatchCount;
  
  public ChainHead(ConstraintWidget paramConstraintWidget, int paramInt, boolean paramBoolean) {
    this.mFirst = paramConstraintWidget;
    this.mOrientation = paramInt;
    this.mIsRtl = paramBoolean;
  }
  
  private void defineChainProperties() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mOrientation : I
    //   4: iconst_2
    //   5: imul
    //   6: istore_3
    //   7: aload_0
    //   8: getfield mFirst : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   11: astore #8
    //   13: iconst_1
    //   14: istore #5
    //   16: aload_0
    //   17: iconst_1
    //   18: putfield mOptimizable : Z
    //   21: aload_0
    //   22: getfield mFirst : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   25: astore #7
    //   27: aload_0
    //   28: getfield mFirst : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   31: astore #6
    //   33: iconst_0
    //   34: istore_2
    //   35: iload_2
    //   36: ifne -> 645
    //   39: aload_0
    //   40: aload_0
    //   41: getfield mWidgetsCount : I
    //   44: iconst_1
    //   45: iadd
    //   46: putfield mWidgetsCount : I
    //   49: aload #7
    //   51: getfield mNextChainWidget : [Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   54: aload_0
    //   55: getfield mOrientation : I
    //   58: aconst_null
    //   59: aastore
    //   60: aload #7
    //   62: getfield mListNextMatchConstraintsWidget : [Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   65: aload_0
    //   66: getfield mOrientation : I
    //   69: aconst_null
    //   70: aastore
    //   71: aload #7
    //   73: invokevirtual getVisibility : ()I
    //   76: bipush #8
    //   78: if_icmpeq -> 535
    //   81: aload_0
    //   82: aload_0
    //   83: getfield mVisibleWidgets : I
    //   86: iconst_1
    //   87: iadd
    //   88: putfield mVisibleWidgets : I
    //   91: aload #7
    //   93: aload_0
    //   94: getfield mOrientation : I
    //   97: invokevirtual getDimensionBehaviour : (I)Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   100: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   103: if_acmpeq -> 124
    //   106: aload_0
    //   107: aload_0
    //   108: getfield mTotalSize : I
    //   111: aload #7
    //   113: aload_0
    //   114: getfield mOrientation : I
    //   117: invokevirtual getLength : (I)I
    //   120: iadd
    //   121: putfield mTotalSize : I
    //   124: aload_0
    //   125: getfield mTotalSize : I
    //   128: aload #7
    //   130: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   133: iload_3
    //   134: aaload
    //   135: invokevirtual getMargin : ()I
    //   138: iadd
    //   139: istore #4
    //   141: aload_0
    //   142: iload #4
    //   144: putfield mTotalSize : I
    //   147: aload_0
    //   148: iload #4
    //   150: aload #7
    //   152: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   155: iload_3
    //   156: iconst_1
    //   157: iadd
    //   158: aaload
    //   159: invokevirtual getMargin : ()I
    //   162: iadd
    //   163: putfield mTotalSize : I
    //   166: aload_0
    //   167: getfield mTotalMargins : I
    //   170: aload #7
    //   172: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   175: iload_3
    //   176: aaload
    //   177: invokevirtual getMargin : ()I
    //   180: iadd
    //   181: istore #4
    //   183: aload_0
    //   184: iload #4
    //   186: putfield mTotalMargins : I
    //   189: aload_0
    //   190: iload #4
    //   192: aload #7
    //   194: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   197: iload_3
    //   198: iconst_1
    //   199: iadd
    //   200: aaload
    //   201: invokevirtual getMargin : ()I
    //   204: iadd
    //   205: putfield mTotalMargins : I
    //   208: aload_0
    //   209: getfield mFirstVisibleWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   212: ifnonnull -> 221
    //   215: aload_0
    //   216: aload #7
    //   218: putfield mFirstVisibleWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   221: aload_0
    //   222: aload #7
    //   224: putfield mLastVisibleWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   227: aload #7
    //   229: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   232: aload_0
    //   233: getfield mOrientation : I
    //   236: aaload
    //   237: getstatic androidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/core/widgets/ConstraintWidget$DimensionBehaviour;
    //   240: if_acmpne -> 535
    //   243: aload #7
    //   245: getfield mResolvedMatchConstraintDefault : [I
    //   248: aload_0
    //   249: getfield mOrientation : I
    //   252: iaload
    //   253: ifeq -> 284
    //   256: aload #7
    //   258: getfield mResolvedMatchConstraintDefault : [I
    //   261: aload_0
    //   262: getfield mOrientation : I
    //   265: iaload
    //   266: iconst_3
    //   267: if_icmpeq -> 284
    //   270: aload #7
    //   272: getfield mResolvedMatchConstraintDefault : [I
    //   275: aload_0
    //   276: getfield mOrientation : I
    //   279: iaload
    //   280: iconst_2
    //   281: if_icmpne -> 431
    //   284: aload_0
    //   285: aload_0
    //   286: getfield mWidgetsMatchCount : I
    //   289: iconst_1
    //   290: iadd
    //   291: putfield mWidgetsMatchCount : I
    //   294: aload #7
    //   296: getfield mWeight : [F
    //   299: aload_0
    //   300: getfield mOrientation : I
    //   303: faload
    //   304: fstore_1
    //   305: fload_1
    //   306: fconst_0
    //   307: fcmpl
    //   308: ifle -> 330
    //   311: aload_0
    //   312: aload_0
    //   313: getfield mTotalWeight : F
    //   316: aload #7
    //   318: getfield mWeight : [F
    //   321: aload_0
    //   322: getfield mOrientation : I
    //   325: faload
    //   326: fadd
    //   327: putfield mTotalWeight : F
    //   330: aload #7
    //   332: aload_0
    //   333: getfield mOrientation : I
    //   336: invokestatic isMatchConstraintEqualityCandidate : (Landroidx/constraintlayout/core/widgets/ConstraintWidget;I)Z
    //   339: ifeq -> 389
    //   342: fload_1
    //   343: fconst_0
    //   344: fcmpg
    //   345: ifge -> 356
    //   348: aload_0
    //   349: iconst_1
    //   350: putfield mHasUndefinedWeights : Z
    //   353: goto -> 361
    //   356: aload_0
    //   357: iconst_1
    //   358: putfield mHasDefinedWeights : Z
    //   361: aload_0
    //   362: getfield mWeightedMatchConstraintsWidgets : Ljava/util/ArrayList;
    //   365: ifnonnull -> 379
    //   368: aload_0
    //   369: new java/util/ArrayList
    //   372: dup
    //   373: invokespecial <init> : ()V
    //   376: putfield mWeightedMatchConstraintsWidgets : Ljava/util/ArrayList;
    //   379: aload_0
    //   380: getfield mWeightedMatchConstraintsWidgets : Ljava/util/ArrayList;
    //   383: aload #7
    //   385: invokevirtual add : (Ljava/lang/Object;)Z
    //   388: pop
    //   389: aload_0
    //   390: getfield mFirstMatchConstraintWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   393: ifnonnull -> 402
    //   396: aload_0
    //   397: aload #7
    //   399: putfield mFirstMatchConstraintWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   402: aload_0
    //   403: getfield mLastMatchConstraintWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   406: astore #6
    //   408: aload #6
    //   410: ifnull -> 425
    //   413: aload #6
    //   415: getfield mListNextMatchConstraintsWidget : [Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   418: aload_0
    //   419: getfield mOrientation : I
    //   422: aload #7
    //   424: aastore
    //   425: aload_0
    //   426: aload #7
    //   428: putfield mLastMatchConstraintWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   431: aload_0
    //   432: getfield mOrientation : I
    //   435: ifne -> 478
    //   438: aload #7
    //   440: getfield mMatchConstraintDefaultWidth : I
    //   443: ifeq -> 454
    //   446: aload_0
    //   447: iconst_0
    //   448: putfield mOptimizable : Z
    //   451: goto -> 515
    //   454: aload #7
    //   456: getfield mMatchConstraintMinWidth : I
    //   459: ifne -> 470
    //   462: aload #7
    //   464: getfield mMatchConstraintMaxWidth : I
    //   467: ifeq -> 515
    //   470: aload_0
    //   471: iconst_0
    //   472: putfield mOptimizable : Z
    //   475: goto -> 515
    //   478: aload #7
    //   480: getfield mMatchConstraintDefaultHeight : I
    //   483: ifeq -> 494
    //   486: aload_0
    //   487: iconst_0
    //   488: putfield mOptimizable : Z
    //   491: goto -> 515
    //   494: aload #7
    //   496: getfield mMatchConstraintMinHeight : I
    //   499: ifne -> 510
    //   502: aload #7
    //   504: getfield mMatchConstraintMaxHeight : I
    //   507: ifeq -> 515
    //   510: aload_0
    //   511: iconst_0
    //   512: putfield mOptimizable : Z
    //   515: aload #7
    //   517: getfield mDimensionRatio : F
    //   520: fconst_0
    //   521: fcmpl
    //   522: ifeq -> 535
    //   525: aload_0
    //   526: iconst_0
    //   527: putfield mOptimizable : Z
    //   530: aload_0
    //   531: iconst_1
    //   532: putfield mHasRatio : Z
    //   535: aload #8
    //   537: aload #7
    //   539: if_acmpeq -> 554
    //   542: aload #8
    //   544: getfield mNextChainWidget : [Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   547: aload_0
    //   548: getfield mOrientation : I
    //   551: aload #7
    //   553: aastore
    //   554: aload #7
    //   556: astore #8
    //   558: aload #7
    //   560: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   563: iload_3
    //   564: iconst_1
    //   565: iadd
    //   566: aaload
    //   567: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   570: astore #6
    //   572: aload #6
    //   574: ifnull -> 625
    //   577: aload #6
    //   579: getfield mOwner : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   582: astore #9
    //   584: aload #9
    //   586: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   589: iload_3
    //   590: aaload
    //   591: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   594: ifnull -> 619
    //   597: aload #9
    //   599: astore #6
    //   601: aload #9
    //   603: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   606: iload_3
    //   607: aaload
    //   608: getfield mTarget : Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   611: getfield mOwner : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   614: aload #7
    //   616: if_acmpeq -> 628
    //   619: aconst_null
    //   620: astore #6
    //   622: goto -> 628
    //   625: aconst_null
    //   626: astore #6
    //   628: aload #6
    //   630: ifnull -> 640
    //   633: aload #6
    //   635: astore #7
    //   637: goto -> 642
    //   640: iconst_1
    //   641: istore_2
    //   642: goto -> 35
    //   645: aload_0
    //   646: getfield mFirstVisibleWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   649: astore #6
    //   651: aload #6
    //   653: ifnull -> 675
    //   656: aload_0
    //   657: aload_0
    //   658: getfield mTotalSize : I
    //   661: aload #6
    //   663: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   666: iload_3
    //   667: aaload
    //   668: invokevirtual getMargin : ()I
    //   671: isub
    //   672: putfield mTotalSize : I
    //   675: aload_0
    //   676: getfield mLastVisibleWidget : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   679: astore #6
    //   681: aload #6
    //   683: ifnull -> 707
    //   686: aload_0
    //   687: aload_0
    //   688: getfield mTotalSize : I
    //   691: aload #6
    //   693: getfield mListAnchors : [Landroidx/constraintlayout/core/widgets/ConstraintAnchor;
    //   696: iload_3
    //   697: iconst_1
    //   698: iadd
    //   699: aaload
    //   700: invokevirtual getMargin : ()I
    //   703: isub
    //   704: putfield mTotalSize : I
    //   707: aload_0
    //   708: aload #7
    //   710: putfield mLast : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   713: aload_0
    //   714: getfield mOrientation : I
    //   717: ifne -> 736
    //   720: aload_0
    //   721: getfield mIsRtl : Z
    //   724: ifeq -> 736
    //   727: aload_0
    //   728: aload #7
    //   730: putfield mHead : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   733: goto -> 744
    //   736: aload_0
    //   737: aload_0
    //   738: getfield mFirst : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   741: putfield mHead : Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   744: aload_0
    //   745: getfield mHasDefinedWeights : Z
    //   748: ifeq -> 761
    //   751: aload_0
    //   752: getfield mHasUndefinedWeights : Z
    //   755: ifeq -> 761
    //   758: goto -> 764
    //   761: iconst_0
    //   762: istore #5
    //   764: aload_0
    //   765: iload #5
    //   767: putfield mHasComplexMatchWeights : Z
    //   770: return
  }
  
  private static boolean isMatchConstraintEqualityCandidate(ConstraintWidget paramConstraintWidget, int paramInt) {
    boolean bool;
    if (paramConstraintWidget.getVisibility() != 8 && paramConstraintWidget.mListDimensionBehaviors[paramInt] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (paramConstraintWidget.mResolvedMatchConstraintDefault[paramInt] == 0 || paramConstraintWidget.mResolvedMatchConstraintDefault[paramInt] == 3)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void define() {
    if (!this.mDefined)
      defineChainProperties(); 
    this.mDefined = true;
  }
  
  public ConstraintWidget getFirst() {
    return this.mFirst;
  }
  
  public ConstraintWidget getFirstMatchConstraintWidget() {
    return this.mFirstMatchConstraintWidget;
  }
  
  public ConstraintWidget getFirstVisibleWidget() {
    return this.mFirstVisibleWidget;
  }
  
  public ConstraintWidget getHead() {
    return this.mHead;
  }
  
  public ConstraintWidget getLast() {
    return this.mLast;
  }
  
  public ConstraintWidget getLastMatchConstraintWidget() {
    return this.mLastMatchConstraintWidget;
  }
  
  public ConstraintWidget getLastVisibleWidget() {
    return this.mLastVisibleWidget;
  }
  
  public float getTotalWeight() {
    return this.mTotalWeight;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\widgets\ChainHead.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */