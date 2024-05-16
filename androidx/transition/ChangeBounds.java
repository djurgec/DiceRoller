package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;
import org.xmlpull.v1.XmlPullParser;

public class ChangeBounds extends Transition {
  private static final Property<View, PointF> BOTTOM_RIGHT_ONLY_PROPERTY;
  
  private static final Property<ViewBounds, PointF> BOTTOM_RIGHT_PROPERTY;
  
  private static final Property<Drawable, PointF> DRAWABLE_ORIGIN_PROPERTY;
  
  private static final Property<View, PointF> POSITION_PROPERTY;
  
  private static final String PROPNAME_BOUNDS = "android:changeBounds:bounds";
  
  private static final String PROPNAME_CLIP = "android:changeBounds:clip";
  
  private static final String PROPNAME_PARENT = "android:changeBounds:parent";
  
  private static final String PROPNAME_WINDOW_X = "android:changeBounds:windowX";
  
  private static final String PROPNAME_WINDOW_Y = "android:changeBounds:windowY";
  
  private static final Property<View, PointF> TOP_LEFT_ONLY_PROPERTY;
  
  private static final Property<ViewBounds, PointF> TOP_LEFT_PROPERTY;
  
  private static RectEvaluator sRectEvaluator;
  
  private static final String[] sTransitionProperties = new String[] { "android:changeBounds:bounds", "android:changeBounds:clip", "android:changeBounds:parent", "android:changeBounds:windowX", "android:changeBounds:windowY" };
  
  private boolean mReparent = false;
  
  private boolean mResizeClip = false;
  
  private int[] mTempLocation = new int[2];
  
  static {
    DRAWABLE_ORIGIN_PROPERTY = new Property<Drawable, PointF>(PointF.class, "boundsOrigin") {
        private Rect mBounds = new Rect();
        
        public PointF get(Drawable param1Drawable) {
          param1Drawable.copyBounds(this.mBounds);
          return new PointF(this.mBounds.left, this.mBounds.top);
        }
        
        public void set(Drawable param1Drawable, PointF param1PointF) {
          param1Drawable.copyBounds(this.mBounds);
          this.mBounds.offsetTo(Math.round(param1PointF.x), Math.round(param1PointF.y));
          param1Drawable.setBounds(this.mBounds);
        }
      };
    TOP_LEFT_PROPERTY = new Property<ViewBounds, PointF>(PointF.class, "topLeft") {
        public PointF get(ChangeBounds.ViewBounds param1ViewBounds) {
          return null;
        }
        
        public void set(ChangeBounds.ViewBounds param1ViewBounds, PointF param1PointF) {
          param1ViewBounds.setTopLeft(param1PointF);
        }
      };
    BOTTOM_RIGHT_PROPERTY = new Property<ViewBounds, PointF>(PointF.class, "bottomRight") {
        public PointF get(ChangeBounds.ViewBounds param1ViewBounds) {
          return null;
        }
        
        public void set(ChangeBounds.ViewBounds param1ViewBounds, PointF param1PointF) {
          param1ViewBounds.setBottomRight(param1PointF);
        }
      };
    BOTTOM_RIGHT_ONLY_PROPERTY = new Property<View, PointF>(PointF.class, "bottomRight") {
        public PointF get(View param1View) {
          return null;
        }
        
        public void set(View param1View, PointF param1PointF) {
          ViewUtils.setLeftTopRightBottom(param1View, param1View.getLeft(), param1View.getTop(), Math.round(param1PointF.x), Math.round(param1PointF.y));
        }
      };
    TOP_LEFT_ONLY_PROPERTY = new Property<View, PointF>(PointF.class, "topLeft") {
        public PointF get(View param1View) {
          return null;
        }
        
        public void set(View param1View, PointF param1PointF) {
          ViewUtils.setLeftTopRightBottom(param1View, Math.round(param1PointF.x), Math.round(param1PointF.y), param1View.getRight(), param1View.getBottom());
        }
      };
    POSITION_PROPERTY = new Property<View, PointF>(PointF.class, "position") {
        public PointF get(View param1View) {
          return null;
        }
        
        public void set(View param1View, PointF param1PointF) {
          int j = Math.round(param1PointF.x);
          int i = Math.round(param1PointF.y);
          ViewUtils.setLeftTopRightBottom(param1View, j, i, param1View.getWidth() + j, param1View.getHeight() + i);
        }
      };
    sRectEvaluator = new RectEvaluator();
  }
  
  public ChangeBounds() {}
  
  public ChangeBounds(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, Styleable.CHANGE_BOUNDS);
    boolean bool = TypedArrayUtils.getNamedBoolean(typedArray, (XmlPullParser)paramAttributeSet, "resizeClip", 0, false);
    typedArray.recycle();
    setResizeClip(bool);
  }
  
  private void captureValues(TransitionValues paramTransitionValues) {
    View view = paramTransitionValues.view;
    if (ViewCompat.isLaidOut(view) || view.getWidth() != 0 || view.getHeight() != 0) {
      paramTransitionValues.values.put("android:changeBounds:bounds", new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
      paramTransitionValues.values.put("android:changeBounds:parent", paramTransitionValues.view.getParent());
      if (this.mReparent) {
        paramTransitionValues.view.getLocationInWindow(this.mTempLocation);
        paramTransitionValues.values.put("android:changeBounds:windowX", Integer.valueOf(this.mTempLocation[0]));
        paramTransitionValues.values.put("android:changeBounds:windowY", Integer.valueOf(this.mTempLocation[1]));
      } 
      if (this.mResizeClip)
        paramTransitionValues.values.put("android:changeBounds:clip", ViewCompat.getClipBounds(view)); 
    } 
  }
  
  private boolean parentMatches(View paramView1, View paramView2) {
    boolean bool = true;
    if (this.mReparent) {
      bool = true;
      boolean bool1 = true;
      TransitionValues transitionValues = getMatchedTransitionValues(paramView1, true);
      if (transitionValues == null) {
        if (paramView1 == paramView2) {
          bool = bool1;
        } else {
          bool = false;
        } 
      } else if (paramView2 != transitionValues.view) {
        bool = false;
      } 
    } 
    return bool;
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues);
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues);
  }
  
  public Animator createAnimator(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    // Byte code:
    //   0: aload_2
    //   1: ifnull -> 1110
    //   4: aload_3
    //   5: ifnonnull -> 11
    //   8: goto -> 1110
    //   11: aload_2
    //   12: getfield values : Ljava/util/Map;
    //   15: astore #20
    //   17: aload_3
    //   18: getfield values : Ljava/util/Map;
    //   21: astore #21
    //   23: aload #20
    //   25: ldc 'android:changeBounds:parent'
    //   27: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   32: checkcast android/view/ViewGroup
    //   35: astore #20
    //   37: aload #21
    //   39: ldc 'android:changeBounds:parent'
    //   41: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   46: checkcast android/view/ViewGroup
    //   49: astore #21
    //   51: aload #20
    //   53: ifnull -> 1108
    //   56: aload #21
    //   58: ifnonnull -> 64
    //   61: goto -> 1108
    //   64: aload_3
    //   65: getfield view : Landroid/view/View;
    //   68: astore #22
    //   70: aload_0
    //   71: aload #20
    //   73: aload #21
    //   75: invokespecial parentMatches : (Landroid/view/View;Landroid/view/View;)Z
    //   78: ifeq -> 859
    //   81: aload_2
    //   82: getfield values : Ljava/util/Map;
    //   85: ldc 'android:changeBounds:bounds'
    //   87: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   92: checkcast android/graphics/Rect
    //   95: astore #20
    //   97: aload_3
    //   98: getfield values : Ljava/util/Map;
    //   101: ldc 'android:changeBounds:bounds'
    //   103: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   108: checkcast android/graphics/Rect
    //   111: astore_1
    //   112: aload #20
    //   114: getfield left : I
    //   117: istore #11
    //   119: aload_1
    //   120: getfield left : I
    //   123: istore #14
    //   125: aload #20
    //   127: getfield top : I
    //   130: istore #17
    //   132: aload_1
    //   133: getfield top : I
    //   136: istore #15
    //   138: aload #20
    //   140: getfield right : I
    //   143: istore #19
    //   145: aload_1
    //   146: getfield right : I
    //   149: istore #12
    //   151: aload #20
    //   153: getfield bottom : I
    //   156: istore #18
    //   158: aload_1
    //   159: getfield bottom : I
    //   162: istore #10
    //   164: iload #19
    //   166: iload #11
    //   168: isub
    //   169: istore #13
    //   171: iload #18
    //   173: iload #17
    //   175: isub
    //   176: istore #8
    //   178: iload #12
    //   180: iload #14
    //   182: isub
    //   183: istore #9
    //   185: iload #10
    //   187: iload #15
    //   189: isub
    //   190: istore #16
    //   192: aload_2
    //   193: getfield values : Ljava/util/Map;
    //   196: ldc 'android:changeBounds:clip'
    //   198: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   203: checkcast android/graphics/Rect
    //   206: astore_2
    //   207: aload_3
    //   208: getfield values : Ljava/util/Map;
    //   211: ldc 'android:changeBounds:clip'
    //   213: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   218: checkcast android/graphics/Rect
    //   221: astore #20
    //   223: iconst_0
    //   224: istore #7
    //   226: iconst_0
    //   227: istore #6
    //   229: iload #13
    //   231: ifeq -> 239
    //   234: iload #8
    //   236: ifne -> 257
    //   239: iload #7
    //   241: istore #5
    //   243: iload #9
    //   245: ifeq -> 300
    //   248: iload #7
    //   250: istore #5
    //   252: iload #16
    //   254: ifeq -> 300
    //   257: iload #11
    //   259: iload #14
    //   261: if_icmpne -> 271
    //   264: iload #17
    //   266: iload #15
    //   268: if_icmpeq -> 276
    //   271: iconst_0
    //   272: iconst_1
    //   273: iadd
    //   274: istore #6
    //   276: iload #19
    //   278: iload #12
    //   280: if_icmpne -> 294
    //   283: iload #6
    //   285: istore #5
    //   287: iload #18
    //   289: iload #10
    //   291: if_icmpeq -> 300
    //   294: iload #6
    //   296: iconst_1
    //   297: iadd
    //   298: istore #5
    //   300: aload_2
    //   301: ifnull -> 313
    //   304: aload_2
    //   305: aload #20
    //   307: invokevirtual equals : (Ljava/lang/Object;)Z
    //   310: ifeq -> 330
    //   313: iload #5
    //   315: istore #6
    //   317: aload_2
    //   318: ifnonnull -> 336
    //   321: iload #5
    //   323: istore #6
    //   325: aload #20
    //   327: ifnull -> 336
    //   330: iload #5
    //   332: iconst_1
    //   333: iadd
    //   334: istore #6
    //   336: iload #6
    //   338: ifle -> 856
    //   341: aload_0
    //   342: getfield mResizeClip : Z
    //   345: ifne -> 607
    //   348: aload #22
    //   350: iload #11
    //   352: iload #17
    //   354: iload #19
    //   356: iload #18
    //   358: invokestatic setLeftTopRightBottom : (Landroid/view/View;IIII)V
    //   361: iload #6
    //   363: iconst_2
    //   364: if_icmpne -> 524
    //   367: iload #13
    //   369: iload #9
    //   371: if_icmpne -> 414
    //   374: iload #8
    //   376: iload #16
    //   378: if_icmpne -> 414
    //   381: aload_0
    //   382: invokevirtual getPathMotion : ()Landroidx/transition/PathMotion;
    //   385: iload #11
    //   387: i2f
    //   388: iload #17
    //   390: i2f
    //   391: iload #14
    //   393: i2f
    //   394: iload #15
    //   396: i2f
    //   397: invokevirtual getPath : (FFFF)Landroid/graphics/Path;
    //   400: astore_1
    //   401: aload #22
    //   403: getstatic androidx/transition/ChangeBounds.POSITION_PROPERTY : Landroid/util/Property;
    //   406: aload_1
    //   407: invokestatic ofPointF : (Ljava/lang/Object;Landroid/util/Property;Landroid/graphics/Path;)Landroid/animation/ObjectAnimator;
    //   410: astore_1
    //   411: goto -> 815
    //   414: new androidx/transition/ChangeBounds$ViewBounds
    //   417: dup
    //   418: aload #22
    //   420: invokespecial <init> : (Landroid/view/View;)V
    //   423: astore_2
    //   424: aload_0
    //   425: invokevirtual getPathMotion : ()Landroidx/transition/PathMotion;
    //   428: iload #11
    //   430: i2f
    //   431: iload #17
    //   433: i2f
    //   434: iload #14
    //   436: i2f
    //   437: iload #15
    //   439: i2f
    //   440: invokevirtual getPath : (FFFF)Landroid/graphics/Path;
    //   443: astore_1
    //   444: aload_2
    //   445: getstatic androidx/transition/ChangeBounds.TOP_LEFT_PROPERTY : Landroid/util/Property;
    //   448: aload_1
    //   449: invokestatic ofPointF : (Ljava/lang/Object;Landroid/util/Property;Landroid/graphics/Path;)Landroid/animation/ObjectAnimator;
    //   452: astore_3
    //   453: aload_0
    //   454: invokevirtual getPathMotion : ()Landroidx/transition/PathMotion;
    //   457: iload #19
    //   459: i2f
    //   460: iload #18
    //   462: i2f
    //   463: iload #12
    //   465: i2f
    //   466: iload #10
    //   468: i2f
    //   469: invokevirtual getPath : (FFFF)Landroid/graphics/Path;
    //   472: astore_1
    //   473: aload_2
    //   474: getstatic androidx/transition/ChangeBounds.BOTTOM_RIGHT_PROPERTY : Landroid/util/Property;
    //   477: aload_1
    //   478: invokestatic ofPointF : (Ljava/lang/Object;Landroid/util/Property;Landroid/graphics/Path;)Landroid/animation/ObjectAnimator;
    //   481: astore #20
    //   483: new android/animation/AnimatorSet
    //   486: dup
    //   487: invokespecial <init> : ()V
    //   490: astore_1
    //   491: aload_1
    //   492: iconst_2
    //   493: anewarray android/animation/Animator
    //   496: dup
    //   497: iconst_0
    //   498: aload_3
    //   499: aastore
    //   500: dup
    //   501: iconst_1
    //   502: aload #20
    //   504: aastore
    //   505: invokevirtual playTogether : ([Landroid/animation/Animator;)V
    //   508: aload_1
    //   509: new androidx/transition/ChangeBounds$7
    //   512: dup
    //   513: aload_0
    //   514: aload_2
    //   515: invokespecial <init> : (Landroidx/transition/ChangeBounds;Landroidx/transition/ChangeBounds$ViewBounds;)V
    //   518: invokevirtual addListener : (Landroid/animation/Animator$AnimatorListener;)V
    //   521: goto -> 815
    //   524: iload #11
    //   526: iload #14
    //   528: if_icmpne -> 574
    //   531: iload #17
    //   533: iload #15
    //   535: if_icmpeq -> 541
    //   538: goto -> 574
    //   541: aload_0
    //   542: invokevirtual getPathMotion : ()Landroidx/transition/PathMotion;
    //   545: iload #19
    //   547: i2f
    //   548: iload #18
    //   550: i2f
    //   551: iload #12
    //   553: i2f
    //   554: iload #10
    //   556: i2f
    //   557: invokevirtual getPath : (FFFF)Landroid/graphics/Path;
    //   560: astore_1
    //   561: aload #22
    //   563: getstatic androidx/transition/ChangeBounds.BOTTOM_RIGHT_ONLY_PROPERTY : Landroid/util/Property;
    //   566: aload_1
    //   567: invokestatic ofPointF : (Ljava/lang/Object;Landroid/util/Property;Landroid/graphics/Path;)Landroid/animation/ObjectAnimator;
    //   570: astore_1
    //   571: goto -> 815
    //   574: aload_0
    //   575: invokevirtual getPathMotion : ()Landroidx/transition/PathMotion;
    //   578: iload #11
    //   580: i2f
    //   581: iload #17
    //   583: i2f
    //   584: iload #14
    //   586: i2f
    //   587: iload #15
    //   589: i2f
    //   590: invokevirtual getPath : (FFFF)Landroid/graphics/Path;
    //   593: astore_1
    //   594: aload #22
    //   596: getstatic androidx/transition/ChangeBounds.TOP_LEFT_ONLY_PROPERTY : Landroid/util/Property;
    //   599: aload_1
    //   600: invokestatic ofPointF : (Ljava/lang/Object;Landroid/util/Property;Landroid/graphics/Path;)Landroid/animation/ObjectAnimator;
    //   603: astore_1
    //   604: goto -> 815
    //   607: iload #13
    //   609: iload #9
    //   611: invokestatic max : (II)I
    //   614: istore #5
    //   616: aload #22
    //   618: iload #11
    //   620: iload #17
    //   622: iload #11
    //   624: iload #5
    //   626: iadd
    //   627: iload #17
    //   629: iload #8
    //   631: iload #16
    //   633: invokestatic max : (II)I
    //   636: iadd
    //   637: invokestatic setLeftTopRightBottom : (Landroid/view/View;IIII)V
    //   640: iload #11
    //   642: iload #14
    //   644: if_icmpne -> 662
    //   647: iload #17
    //   649: iload #15
    //   651: if_icmpeq -> 657
    //   654: goto -> 662
    //   657: aconst_null
    //   658: astore_1
    //   659: goto -> 692
    //   662: aload_0
    //   663: invokevirtual getPathMotion : ()Landroidx/transition/PathMotion;
    //   666: iload #11
    //   668: i2f
    //   669: iload #17
    //   671: i2f
    //   672: iload #14
    //   674: i2f
    //   675: iload #15
    //   677: i2f
    //   678: invokevirtual getPath : (FFFF)Landroid/graphics/Path;
    //   681: astore_1
    //   682: aload #22
    //   684: getstatic androidx/transition/ChangeBounds.POSITION_PROPERTY : Landroid/util/Property;
    //   687: aload_1
    //   688: invokestatic ofPointF : (Ljava/lang/Object;Landroid/util/Property;Landroid/graphics/Path;)Landroid/animation/ObjectAnimator;
    //   691: astore_1
    //   692: aload_2
    //   693: ifnonnull -> 713
    //   696: new android/graphics/Rect
    //   699: dup
    //   700: iconst_0
    //   701: iconst_0
    //   702: iload #13
    //   704: iload #8
    //   706: invokespecial <init> : (IIII)V
    //   709: astore_2
    //   710: goto -> 713
    //   713: aload #20
    //   715: ifnonnull -> 735
    //   718: new android/graphics/Rect
    //   721: dup
    //   722: iconst_0
    //   723: iconst_0
    //   724: iload #9
    //   726: iload #16
    //   728: invokespecial <init> : (IIII)V
    //   731: astore_3
    //   732: goto -> 738
    //   735: aload #20
    //   737: astore_3
    //   738: aconst_null
    //   739: astore #21
    //   741: aload_2
    //   742: aload_3
    //   743: invokevirtual equals : (Ljava/lang/Object;)Z
    //   746: ifne -> 806
    //   749: aload #22
    //   751: aload_2
    //   752: invokestatic setClipBounds : (Landroid/view/View;Landroid/graphics/Rect;)V
    //   755: aload #22
    //   757: ldc_w 'clipBounds'
    //   760: getstatic androidx/transition/ChangeBounds.sRectEvaluator : Landroidx/transition/RectEvaluator;
    //   763: iconst_2
    //   764: anewarray java/lang/Object
    //   767: dup
    //   768: iconst_0
    //   769: aload_2
    //   770: aastore
    //   771: dup
    //   772: iconst_1
    //   773: aload_3
    //   774: aastore
    //   775: invokestatic ofObject : (Ljava/lang/Object;Ljava/lang/String;Landroid/animation/TypeEvaluator;[Ljava/lang/Object;)Landroid/animation/ObjectAnimator;
    //   778: astore_2
    //   779: aload_2
    //   780: new androidx/transition/ChangeBounds$8
    //   783: dup
    //   784: aload_0
    //   785: aload #22
    //   787: aload #20
    //   789: iload #14
    //   791: iload #15
    //   793: iload #12
    //   795: iload #10
    //   797: invokespecial <init> : (Landroidx/transition/ChangeBounds;Landroid/view/View;Landroid/graphics/Rect;IIII)V
    //   800: invokevirtual addListener : (Landroid/animation/Animator$AnimatorListener;)V
    //   803: goto -> 809
    //   806: aload #21
    //   808: astore_2
    //   809: aload_1
    //   810: aload_2
    //   811: invokestatic mergeAnimators : (Landroid/animation/Animator;Landroid/animation/Animator;)Landroid/animation/Animator;
    //   814: astore_1
    //   815: aload #22
    //   817: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   820: instanceof android/view/ViewGroup
    //   823: ifeq -> 854
    //   826: aload #22
    //   828: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   831: checkcast android/view/ViewGroup
    //   834: astore_2
    //   835: aload_2
    //   836: iconst_1
    //   837: invokestatic suppressLayout : (Landroid/view/ViewGroup;Z)V
    //   840: aload_0
    //   841: new androidx/transition/ChangeBounds$9
    //   844: dup
    //   845: aload_0
    //   846: aload_2
    //   847: invokespecial <init> : (Landroidx/transition/ChangeBounds;Landroid/view/ViewGroup;)V
    //   850: invokevirtual addListener : (Landroidx/transition/Transition$TransitionListener;)Landroidx/transition/Transition;
    //   853: pop
    //   854: aload_1
    //   855: areturn
    //   856: goto -> 952
    //   859: aload_2
    //   860: getfield values : Ljava/util/Map;
    //   863: ldc 'android:changeBounds:windowX'
    //   865: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   870: checkcast java/lang/Integer
    //   873: invokevirtual intValue : ()I
    //   876: istore #8
    //   878: aload_2
    //   879: getfield values : Ljava/util/Map;
    //   882: ldc 'android:changeBounds:windowY'
    //   884: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   889: checkcast java/lang/Integer
    //   892: invokevirtual intValue : ()I
    //   895: istore #6
    //   897: aload_3
    //   898: getfield values : Ljava/util/Map;
    //   901: ldc 'android:changeBounds:windowX'
    //   903: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   908: checkcast java/lang/Integer
    //   911: invokevirtual intValue : ()I
    //   914: istore #5
    //   916: aload_3
    //   917: getfield values : Ljava/util/Map;
    //   920: ldc 'android:changeBounds:windowY'
    //   922: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   927: checkcast java/lang/Integer
    //   930: invokevirtual intValue : ()I
    //   933: istore #7
    //   935: iload #8
    //   937: iload #5
    //   939: if_icmpne -> 954
    //   942: iload #6
    //   944: iload #7
    //   946: if_icmpeq -> 952
    //   949: goto -> 954
    //   952: aconst_null
    //   953: areturn
    //   954: aload_1
    //   955: aload_0
    //   956: getfield mTempLocation : [I
    //   959: invokevirtual getLocationInWindow : ([I)V
    //   962: aload #22
    //   964: invokevirtual getWidth : ()I
    //   967: aload #22
    //   969: invokevirtual getHeight : ()I
    //   972: getstatic android/graphics/Bitmap$Config.ARGB_8888 : Landroid/graphics/Bitmap$Config;
    //   975: invokestatic createBitmap : (IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;
    //   978: astore_2
    //   979: aload #22
    //   981: new android/graphics/Canvas
    //   984: dup
    //   985: aload_2
    //   986: invokespecial <init> : (Landroid/graphics/Bitmap;)V
    //   989: invokevirtual draw : (Landroid/graphics/Canvas;)V
    //   992: new android/graphics/drawable/BitmapDrawable
    //   995: dup
    //   996: aload_2
    //   997: invokespecial <init> : (Landroid/graphics/Bitmap;)V
    //   1000: astore_2
    //   1001: aload #22
    //   1003: invokestatic getTransitionAlpha : (Landroid/view/View;)F
    //   1006: fstore #4
    //   1008: aload #22
    //   1010: fconst_0
    //   1011: invokestatic setTransitionAlpha : (Landroid/view/View;F)V
    //   1014: aload_1
    //   1015: invokestatic getOverlay : (Landroid/view/View;)Landroidx/transition/ViewOverlayImpl;
    //   1018: aload_2
    //   1019: invokeinterface add : (Landroid/graphics/drawable/Drawable;)V
    //   1024: aload_0
    //   1025: invokevirtual getPathMotion : ()Landroidx/transition/PathMotion;
    //   1028: astore #20
    //   1030: aload_0
    //   1031: getfield mTempLocation : [I
    //   1034: astore_3
    //   1035: aload #20
    //   1037: iload #8
    //   1039: aload_3
    //   1040: iconst_0
    //   1041: iaload
    //   1042: isub
    //   1043: i2f
    //   1044: iload #6
    //   1046: aload_3
    //   1047: iconst_1
    //   1048: iaload
    //   1049: isub
    //   1050: i2f
    //   1051: iload #5
    //   1053: aload_3
    //   1054: iconst_0
    //   1055: iaload
    //   1056: isub
    //   1057: i2f
    //   1058: iload #7
    //   1060: aload_3
    //   1061: iconst_1
    //   1062: iaload
    //   1063: isub
    //   1064: i2f
    //   1065: invokevirtual getPath : (FFFF)Landroid/graphics/Path;
    //   1068: astore_3
    //   1069: aload_2
    //   1070: iconst_1
    //   1071: anewarray android/animation/PropertyValuesHolder
    //   1074: dup
    //   1075: iconst_0
    //   1076: getstatic androidx/transition/ChangeBounds.DRAWABLE_ORIGIN_PROPERTY : Landroid/util/Property;
    //   1079: aload_3
    //   1080: invokestatic ofPointF : (Landroid/util/Property;Landroid/graphics/Path;)Landroid/animation/PropertyValuesHolder;
    //   1083: aastore
    //   1084: invokestatic ofPropertyValuesHolder : (Ljava/lang/Object;[Landroid/animation/PropertyValuesHolder;)Landroid/animation/ObjectAnimator;
    //   1087: astore_3
    //   1088: aload_3
    //   1089: new androidx/transition/ChangeBounds$10
    //   1092: dup
    //   1093: aload_0
    //   1094: aload_1
    //   1095: aload_2
    //   1096: aload #22
    //   1098: fload #4
    //   1100: invokespecial <init> : (Landroidx/transition/ChangeBounds;Landroid/view/ViewGroup;Landroid/graphics/drawable/BitmapDrawable;Landroid/view/View;F)V
    //   1103: invokevirtual addListener : (Landroid/animation/Animator$AnimatorListener;)V
    //   1106: aload_3
    //   1107: areturn
    //   1108: aconst_null
    //   1109: areturn
    //   1110: aconst_null
    //   1111: areturn
  }
  
  public boolean getResizeClip() {
    return this.mResizeClip;
  }
  
  public String[] getTransitionProperties() {
    return sTransitionProperties;
  }
  
  public void setResizeClip(boolean paramBoolean) {
    this.mResizeClip = paramBoolean;
  }
  
  private static class ViewBounds {
    private int mBottom;
    
    private int mBottomRightCalls;
    
    private int mLeft;
    
    private int mRight;
    
    private int mTop;
    
    private int mTopLeftCalls;
    
    private View mView;
    
    ViewBounds(View param1View) {
      this.mView = param1View;
    }
    
    private void setLeftTopRightBottom() {
      ViewUtils.setLeftTopRightBottom(this.mView, this.mLeft, this.mTop, this.mRight, this.mBottom);
      this.mTopLeftCalls = 0;
      this.mBottomRightCalls = 0;
    }
    
    void setBottomRight(PointF param1PointF) {
      this.mRight = Math.round(param1PointF.x);
      this.mBottom = Math.round(param1PointF.y);
      int i = this.mBottomRightCalls + 1;
      this.mBottomRightCalls = i;
      if (this.mTopLeftCalls == i)
        setLeftTopRightBottom(); 
    }
    
    void setTopLeft(PointF param1PointF) {
      this.mLeft = Math.round(param1PointF.x);
      this.mTop = Math.round(param1PointF.y);
      int i = this.mTopLeftCalls + 1;
      this.mTopLeftCalls = i;
      if (i == this.mBottomRightCalls)
        setLeftTopRightBottom(); 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\ChangeBounds.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */