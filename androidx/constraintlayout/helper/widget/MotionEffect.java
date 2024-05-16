package androidx.constraintlayout.helper.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import androidx.constraintlayout.motion.widget.MotionController;
import androidx.constraintlayout.motion.widget.MotionHelper;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.R;
import java.util.HashMap;

public class MotionEffect extends MotionHelper {
  public static final int AUTO = -1;
  
  public static final int EAST = 2;
  
  public static final int NORTH = 0;
  
  public static final int SOUTH = 1;
  
  public static final String TAG = "FadeMove";
  
  private static final int UNSET = -1;
  
  public static final int WEST = 3;
  
  private int fadeMove = -1;
  
  private float motionEffectAlpha = 0.1F;
  
  private int motionEffectEnd = 50;
  
  private int motionEffectStart = 49;
  
  private boolean motionEffectStrictMove = true;
  
  private int motionEffectTranslationX = 0;
  
  private int motionEffectTranslationY = 0;
  
  private int viewTransitionId = -1;
  
  public MotionEffect(Context paramContext) {
    super(paramContext);
  }
  
  public MotionEffect(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramContext, paramAttributeSet);
  }
  
  public MotionEffect(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext, paramAttributeSet);
  }
  
  private void init(Context paramContext, AttributeSet paramAttributeSet) {
    if (paramAttributeSet != null) {
      TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.MotionEffect);
      int j = typedArray.getIndexCount();
      int i;
      for (i = 0; i < j; i++) {
        int k = typedArray.getIndex(i);
        if (k == R.styleable.MotionEffect_motionEffect_start) {
          k = typedArray.getInt(k, this.motionEffectStart);
          this.motionEffectStart = k;
          this.motionEffectStart = Math.max(Math.min(k, 99), 0);
        } else if (k == R.styleable.MotionEffect_motionEffect_end) {
          k = typedArray.getInt(k, this.motionEffectEnd);
          this.motionEffectEnd = k;
          this.motionEffectEnd = Math.max(Math.min(k, 99), 0);
        } else if (k == R.styleable.MotionEffect_motionEffect_translationX) {
          this.motionEffectTranslationX = typedArray.getDimensionPixelOffset(k, this.motionEffectTranslationX);
        } else if (k == R.styleable.MotionEffect_motionEffect_translationY) {
          this.motionEffectTranslationY = typedArray.getDimensionPixelOffset(k, this.motionEffectTranslationY);
        } else if (k == R.styleable.MotionEffect_motionEffect_alpha) {
          this.motionEffectAlpha = typedArray.getFloat(k, this.motionEffectAlpha);
        } else if (k == R.styleable.MotionEffect_motionEffect_move) {
          this.fadeMove = typedArray.getInt(k, this.fadeMove);
        } else if (k == R.styleable.MotionEffect_motionEffect_strict) {
          this.motionEffectStrictMove = typedArray.getBoolean(k, this.motionEffectStrictMove);
        } else if (k == R.styleable.MotionEffect_motionEffect_viewTransition) {
          this.viewTransitionId = typedArray.getResourceId(k, this.viewTransitionId);
        } 
      } 
      i = this.motionEffectStart;
      j = this.motionEffectEnd;
      if (i == j)
        if (i > 0) {
          this.motionEffectStart = i - 1;
        } else {
          this.motionEffectEnd = j + 1;
        }  
      typedArray.recycle();
    } 
  }
  
  public boolean isDecorator() {
    return true;
  }
  
  public void onPreSetup(MotionLayout paramMotionLayout, HashMap<View, MotionController> paramHashMap) {
    // Byte code:
    //   0: aload_0
    //   1: aload_0
    //   2: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   5: checkcast androidx/constraintlayout/widget/ConstraintLayout
    //   8: invokevirtual getViews : (Landroidx/constraintlayout/widget/ConstraintLayout;)[Landroid/view/View;
    //   11: astore #13
    //   13: aload #13
    //   15: ifnonnull -> 36
    //   18: ldc 'FadeMove'
    //   20: invokestatic getLoc : ()Ljava/lang/String;
    //   23: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   26: ldc ' views = null'
    //   28: invokevirtual concat : (Ljava/lang/String;)Ljava/lang/String;
    //   31: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   34: pop
    //   35: return
    //   36: new androidx/constraintlayout/motion/widget/KeyAttributes
    //   39: dup
    //   40: invokespecial <init> : ()V
    //   43: astore #14
    //   45: new androidx/constraintlayout/motion/widget/KeyAttributes
    //   48: dup
    //   49: invokespecial <init> : ()V
    //   52: astore #16
    //   54: aload #14
    //   56: ldc 'alpha'
    //   58: aload_0
    //   59: getfield motionEffectAlpha : F
    //   62: invokestatic valueOf : (F)Ljava/lang/Float;
    //   65: invokevirtual setValue : (Ljava/lang/String;Ljava/lang/Object;)V
    //   68: aload #16
    //   70: ldc 'alpha'
    //   72: aload_0
    //   73: getfield motionEffectAlpha : F
    //   76: invokestatic valueOf : (F)Ljava/lang/Float;
    //   79: invokevirtual setValue : (Ljava/lang/String;Ljava/lang/Object;)V
    //   82: aload #14
    //   84: aload_0
    //   85: getfield motionEffectStart : I
    //   88: invokevirtual setFramePosition : (I)V
    //   91: aload #16
    //   93: aload_0
    //   94: getfield motionEffectEnd : I
    //   97: invokevirtual setFramePosition : (I)V
    //   100: new androidx/constraintlayout/motion/widget/KeyPosition
    //   103: dup
    //   104: invokespecial <init> : ()V
    //   107: astore #15
    //   109: aload #15
    //   111: aload_0
    //   112: getfield motionEffectStart : I
    //   115: invokevirtual setFramePosition : (I)V
    //   118: aload #15
    //   120: iconst_0
    //   121: invokevirtual setType : (I)V
    //   124: aload #15
    //   126: ldc 'percentX'
    //   128: iconst_0
    //   129: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   132: invokevirtual setValue : (Ljava/lang/String;Ljava/lang/Object;)V
    //   135: aload #15
    //   137: ldc 'percentY'
    //   139: iconst_0
    //   140: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   143: invokevirtual setValue : (Ljava/lang/String;Ljava/lang/Object;)V
    //   146: new androidx/constraintlayout/motion/widget/KeyPosition
    //   149: dup
    //   150: invokespecial <init> : ()V
    //   153: astore #17
    //   155: aload #17
    //   157: aload_0
    //   158: getfield motionEffectEnd : I
    //   161: invokevirtual setFramePosition : (I)V
    //   164: aload #17
    //   166: iconst_0
    //   167: invokevirtual setType : (I)V
    //   170: aload #17
    //   172: ldc 'percentX'
    //   174: iconst_1
    //   175: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   178: invokevirtual setValue : (Ljava/lang/String;Ljava/lang/Object;)V
    //   181: aload #17
    //   183: ldc 'percentY'
    //   185: iconst_1
    //   186: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   189: invokevirtual setValue : (Ljava/lang/String;Ljava/lang/Object;)V
    //   192: aconst_null
    //   193: astore #9
    //   195: aconst_null
    //   196: astore #10
    //   198: aload_0
    //   199: getfield motionEffectTranslationX : I
    //   202: ifle -> 268
    //   205: new androidx/constraintlayout/motion/widget/KeyAttributes
    //   208: dup
    //   209: invokespecial <init> : ()V
    //   212: astore #9
    //   214: new androidx/constraintlayout/motion/widget/KeyAttributes
    //   217: dup
    //   218: invokespecial <init> : ()V
    //   221: astore #10
    //   223: aload #9
    //   225: ldc 'translationX'
    //   227: aload_0
    //   228: getfield motionEffectTranslationX : I
    //   231: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   234: invokevirtual setValue : (Ljava/lang/String;Ljava/lang/Object;)V
    //   237: aload #9
    //   239: aload_0
    //   240: getfield motionEffectEnd : I
    //   243: invokevirtual setFramePosition : (I)V
    //   246: aload #10
    //   248: ldc 'translationX'
    //   250: iconst_0
    //   251: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   254: invokevirtual setValue : (Ljava/lang/String;Ljava/lang/Object;)V
    //   257: aload #10
    //   259: aload_0
    //   260: getfield motionEffectEnd : I
    //   263: iconst_1
    //   264: isub
    //   265: invokevirtual setFramePosition : (I)V
    //   268: aconst_null
    //   269: astore #12
    //   271: aconst_null
    //   272: astore #11
    //   274: aload_0
    //   275: getfield motionEffectTranslationY : I
    //   278: ifle -> 344
    //   281: new androidx/constraintlayout/motion/widget/KeyAttributes
    //   284: dup
    //   285: invokespecial <init> : ()V
    //   288: astore #12
    //   290: new androidx/constraintlayout/motion/widget/KeyAttributes
    //   293: dup
    //   294: invokespecial <init> : ()V
    //   297: astore #11
    //   299: aload #12
    //   301: ldc 'translationY'
    //   303: aload_0
    //   304: getfield motionEffectTranslationY : I
    //   307: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   310: invokevirtual setValue : (Ljava/lang/String;Ljava/lang/Object;)V
    //   313: aload #12
    //   315: aload_0
    //   316: getfield motionEffectEnd : I
    //   319: invokevirtual setFramePosition : (I)V
    //   322: aload #11
    //   324: ldc 'translationY'
    //   326: iconst_0
    //   327: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   330: invokevirtual setValue : (Ljava/lang/String;Ljava/lang/Object;)V
    //   333: aload #11
    //   335: aload_0
    //   336: getfield motionEffectEnd : I
    //   339: iconst_1
    //   340: isub
    //   341: invokevirtual setFramePosition : (I)V
    //   344: aload_0
    //   345: getfield fadeMove : I
    //   348: istore #7
    //   350: aload_0
    //   351: getfield fadeMove : I
    //   354: iconst_m1
    //   355: if_icmpne -> 550
    //   358: iconst_4
    //   359: newarray int
    //   361: astore #19
    //   363: iconst_0
    //   364: istore #5
    //   366: iload #5
    //   368: aload #13
    //   370: arraylength
    //   371: if_icmpge -> 493
    //   374: aload_2
    //   375: aload #13
    //   377: iload #5
    //   379: aaload
    //   380: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   383: checkcast androidx/constraintlayout/motion/widget/MotionController
    //   386: astore #18
    //   388: aload #18
    //   390: ifnonnull -> 396
    //   393: goto -> 487
    //   396: aload #18
    //   398: invokevirtual getFinalX : ()F
    //   401: aload #18
    //   403: invokevirtual getStartX : ()F
    //   406: fsub
    //   407: fstore_3
    //   408: aload #18
    //   410: invokevirtual getFinalY : ()F
    //   413: aload #18
    //   415: invokevirtual getStartY : ()F
    //   418: fsub
    //   419: fstore #4
    //   421: fload #4
    //   423: fconst_0
    //   424: fcmpg
    //   425: ifge -> 438
    //   428: aload #19
    //   430: iconst_1
    //   431: aload #19
    //   433: iconst_1
    //   434: iaload
    //   435: iconst_1
    //   436: iadd
    //   437: iastore
    //   438: fload #4
    //   440: fconst_0
    //   441: fcmpl
    //   442: ifle -> 455
    //   445: aload #19
    //   447: iconst_0
    //   448: aload #19
    //   450: iconst_0
    //   451: iaload
    //   452: iconst_1
    //   453: iadd
    //   454: iastore
    //   455: fload_3
    //   456: fconst_0
    //   457: fcmpl
    //   458: ifle -> 471
    //   461: aload #19
    //   463: iconst_3
    //   464: aload #19
    //   466: iconst_3
    //   467: iaload
    //   468: iconst_1
    //   469: iadd
    //   470: iastore
    //   471: fload_3
    //   472: fconst_0
    //   473: fcmpg
    //   474: ifge -> 487
    //   477: aload #19
    //   479: iconst_2
    //   480: aload #19
    //   482: iconst_2
    //   483: iaload
    //   484: iconst_1
    //   485: iadd
    //   486: iastore
    //   487: iinc #5, 1
    //   490: goto -> 366
    //   493: aload #19
    //   495: iconst_0
    //   496: iaload
    //   497: istore #8
    //   499: iconst_0
    //   500: istore #6
    //   502: iconst_1
    //   503: istore #5
    //   505: iload #6
    //   507: istore #7
    //   509: iload #5
    //   511: iconst_4
    //   512: if_icmpge -> 550
    //   515: iload #8
    //   517: istore #7
    //   519: iload #8
    //   521: aload #19
    //   523: iload #5
    //   525: iaload
    //   526: if_icmpge -> 540
    //   529: aload #19
    //   531: iload #5
    //   533: iaload
    //   534: istore #7
    //   536: iload #5
    //   538: istore #6
    //   540: iinc #5, 1
    //   543: iload #7
    //   545: istore #8
    //   547: goto -> 505
    //   550: iconst_0
    //   551: istore #6
    //   553: iload #6
    //   555: aload #13
    //   557: arraylength
    //   558: if_icmpge -> 882
    //   561: aload_2
    //   562: aload #13
    //   564: iload #6
    //   566: aaload
    //   567: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   570: checkcast androidx/constraintlayout/motion/widget/MotionController
    //   573: astore #18
    //   575: aload #18
    //   577: ifnonnull -> 583
    //   580: goto -> 876
    //   583: aload #18
    //   585: invokevirtual getFinalX : ()F
    //   588: aload #18
    //   590: invokevirtual getStartX : ()F
    //   593: fsub
    //   594: fstore_3
    //   595: aload #18
    //   597: invokevirtual getFinalY : ()F
    //   600: aload #18
    //   602: invokevirtual getStartY : ()F
    //   605: fsub
    //   606: fstore #4
    //   608: iconst_1
    //   609: istore #8
    //   611: iload #7
    //   613: ifne -> 650
    //   616: iload #8
    //   618: istore #5
    //   620: fload #4
    //   622: fconst_0
    //   623: fcmpl
    //   624: ifle -> 771
    //   627: aload_0
    //   628: getfield motionEffectStrictMove : Z
    //   631: ifeq -> 644
    //   634: iload #8
    //   636: istore #5
    //   638: fload_3
    //   639: fconst_0
    //   640: fcmpl
    //   641: ifne -> 771
    //   644: iconst_0
    //   645: istore #5
    //   647: goto -> 771
    //   650: iload #7
    //   652: iconst_1
    //   653: if_icmpne -> 690
    //   656: iload #8
    //   658: istore #5
    //   660: fload #4
    //   662: fconst_0
    //   663: fcmpg
    //   664: ifge -> 771
    //   667: aload_0
    //   668: getfield motionEffectStrictMove : Z
    //   671: ifeq -> 684
    //   674: iload #8
    //   676: istore #5
    //   678: fload_3
    //   679: fconst_0
    //   680: fcmpl
    //   681: ifne -> 771
    //   684: iconst_0
    //   685: istore #5
    //   687: goto -> 771
    //   690: iload #7
    //   692: iconst_2
    //   693: if_icmpne -> 730
    //   696: iload #8
    //   698: istore #5
    //   700: fload_3
    //   701: fconst_0
    //   702: fcmpg
    //   703: ifge -> 771
    //   706: aload_0
    //   707: getfield motionEffectStrictMove : Z
    //   710: ifeq -> 724
    //   713: iload #8
    //   715: istore #5
    //   717: fload #4
    //   719: fconst_0
    //   720: fcmpl
    //   721: ifne -> 771
    //   724: iconst_0
    //   725: istore #5
    //   727: goto -> 771
    //   730: iload #8
    //   732: istore #5
    //   734: iload #7
    //   736: iconst_3
    //   737: if_icmpne -> 771
    //   740: iload #8
    //   742: istore #5
    //   744: fload_3
    //   745: fconst_0
    //   746: fcmpl
    //   747: ifle -> 771
    //   750: aload_0
    //   751: getfield motionEffectStrictMove : Z
    //   754: ifeq -> 768
    //   757: iload #8
    //   759: istore #5
    //   761: fload #4
    //   763: fconst_0
    //   764: fcmpl
    //   765: ifne -> 771
    //   768: iconst_0
    //   769: istore #5
    //   771: iload #5
    //   773: ifeq -> 876
    //   776: aload_0
    //   777: getfield viewTransitionId : I
    //   780: istore #5
    //   782: iload #5
    //   784: iconst_m1
    //   785: if_icmpne -> 864
    //   788: aload #18
    //   790: aload #14
    //   792: invokevirtual addKey : (Landroidx/constraintlayout/motion/widget/Key;)V
    //   795: aload #18
    //   797: aload #16
    //   799: invokevirtual addKey : (Landroidx/constraintlayout/motion/widget/Key;)V
    //   802: aload #18
    //   804: aload #15
    //   806: invokevirtual addKey : (Landroidx/constraintlayout/motion/widget/Key;)V
    //   809: aload #18
    //   811: aload #17
    //   813: invokevirtual addKey : (Landroidx/constraintlayout/motion/widget/Key;)V
    //   816: aload_0
    //   817: getfield motionEffectTranslationX : I
    //   820: ifle -> 837
    //   823: aload #18
    //   825: aload #9
    //   827: invokevirtual addKey : (Landroidx/constraintlayout/motion/widget/Key;)V
    //   830: aload #18
    //   832: aload #10
    //   834: invokevirtual addKey : (Landroidx/constraintlayout/motion/widget/Key;)V
    //   837: aload_0
    //   838: getfield motionEffectTranslationY : I
    //   841: ifle -> 861
    //   844: aload #18
    //   846: aload #12
    //   848: invokevirtual addKey : (Landroidx/constraintlayout/motion/widget/Key;)V
    //   851: aload #18
    //   853: aload #11
    //   855: invokevirtual addKey : (Landroidx/constraintlayout/motion/widget/Key;)V
    //   858: goto -> 876
    //   861: goto -> 876
    //   864: aload_1
    //   865: iload #5
    //   867: aload #18
    //   869: invokevirtual applyViewTransition : (ILandroidx/constraintlayout/motion/widget/MotionController;)Z
    //   872: pop
    //   873: goto -> 876
    //   876: iinc #6, 1
    //   879: goto -> 553
    //   882: return
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\helper\widget\MotionEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */