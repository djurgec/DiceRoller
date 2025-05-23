package androidx.vectordrawable.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.graphics.PathParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatorInflaterCompat {
  private static final boolean DBG_ANIMATOR_INFLATER = false;
  
  private static final int MAX_NUM_POINTS = 100;
  
  private static final String TAG = "AnimatorInflater";
  
  private static final int TOGETHER = 0;
  
  private static final int VALUE_TYPE_COLOR = 3;
  
  private static final int VALUE_TYPE_FLOAT = 0;
  
  private static final int VALUE_TYPE_INT = 1;
  
  private static final int VALUE_TYPE_PATH = 2;
  
  private static final int VALUE_TYPE_UNDEFINED = 4;
  
  private static Animator createAnimatorFromXml(Context paramContext, Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser, float paramFloat) throws XmlPullParserException, IOException {
    return createAnimatorFromXml(paramContext, paramResources, paramTheme, paramXmlPullParser, Xml.asAttributeSet(paramXmlPullParser), null, 0, paramFloat);
  }
  
  private static Animator createAnimatorFromXml(Context paramContext, Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, AnimatorSet paramAnimatorSet, int paramInt, float paramFloat) throws XmlPullParserException, IOException {
    AnimatorSet animatorSet;
    ArrayList<AnimatorSet> arrayList;
    int i = paramXmlPullParser.getDepth();
    ObjectAnimator objectAnimator = null;
    String str = null;
    while (true) {
      int j = paramXmlPullParser.next();
      if ((j != 3 || paramXmlPullParser.getDepth() > i) && j != 1) {
        ArrayList<AnimatorSet> arrayList1;
        if (j != 2)
          continue; 
        String str1 = paramXmlPullParser.getName();
        j = 0;
        if (str1.equals("objectAnimator")) {
          objectAnimator = loadObjectAnimator(paramContext, paramResources, paramTheme, paramAttributeSet, paramFloat, paramXmlPullParser);
        } else if (str1.equals("animator")) {
          ValueAnimator valueAnimator = loadAnimator(paramContext, paramResources, paramTheme, paramAttributeSet, null, paramFloat, paramXmlPullParser);
        } else {
          TypedArray typedArray;
          if (str1.equals("set")) {
            animatorSet = new AnimatorSet();
            typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_ANIMATOR_SET);
            int k = TypedArrayUtils.getNamedInt(typedArray, paramXmlPullParser, "ordering", 0, 0);
            createAnimatorFromXml(paramContext, paramResources, paramTheme, paramXmlPullParser, paramAttributeSet, animatorSet, k, paramFloat);
            typedArray.recycle();
          } else if (typedArray.equals("propertyValuesHolder")) {
            PropertyValuesHolder[] arrayOfPropertyValuesHolder = loadValues(paramContext, paramResources, paramTheme, paramXmlPullParser, Xml.asAttributeSet(paramXmlPullParser));
            if (arrayOfPropertyValuesHolder != null && animatorSet instanceof ValueAnimator)
              ((ValueAnimator)animatorSet).setValues(arrayOfPropertyValuesHolder); 
            j = 1;
          } else {
            throw new RuntimeException("Unknown animator name: " + paramXmlPullParser.getName());
          } 
        } 
        str1 = str;
        if (paramAnimatorSet != null) {
          str1 = str;
          if (j == 0) {
            str1 = str;
            if (str == null)
              arrayList1 = new ArrayList(); 
            arrayList1.add(animatorSet);
          } 
        } 
        arrayList = arrayList1;
        continue;
      } 
      break;
    } 
    if (paramAnimatorSet != null && arrayList != null) {
      Animator[] arrayOfAnimator = new Animator[arrayList.size()];
      byte b = 0;
      Iterator<AnimatorSet> iterator = arrayList.iterator();
      while (iterator.hasNext()) {
        arrayOfAnimator[b] = (Animator)iterator.next();
        b++;
      } 
      if (paramInt == 0) {
        paramAnimatorSet.playTogether(arrayOfAnimator);
      } else {
        paramAnimatorSet.playSequentially(arrayOfAnimator);
      } 
    } 
    return (Animator)animatorSet;
  }
  
  private static Keyframe createNewKeyframe(Keyframe paramKeyframe, float paramFloat) {
    if (paramKeyframe.getType() == float.class) {
      paramKeyframe = Keyframe.ofFloat(paramFloat);
    } else if (paramKeyframe.getType() == int.class) {
      paramKeyframe = Keyframe.ofInt(paramFloat);
    } else {
      paramKeyframe = Keyframe.ofObject(paramFloat);
    } 
    return paramKeyframe;
  }
  
  private static void distributeKeyframes(Keyframe[] paramArrayOfKeyframe, float paramFloat, int paramInt1, int paramInt2) {
    paramFloat /= (paramInt2 - paramInt1 + 2);
    while (paramInt1 <= paramInt2) {
      paramArrayOfKeyframe[paramInt1].setFraction(paramArrayOfKeyframe[paramInt1 - 1].getFraction() + paramFloat);
      paramInt1++;
    } 
  }
  
  private static void dumpKeyframes(Object[] paramArrayOfObject, String paramString) {
    if (paramArrayOfObject == null || paramArrayOfObject.length == 0)
      return; 
    Log.d("AnimatorInflater", paramString);
    int i = paramArrayOfObject.length;
    for (byte b = 0; b < i; b++) {
      Float float_;
      Keyframe keyframe = (Keyframe)paramArrayOfObject[b];
      StringBuilder stringBuilder = (new StringBuilder()).append("Keyframe ").append(b).append(": fraction ");
      float f = keyframe.getFraction();
      String str = "null";
      if (f < 0.0F) {
        paramString = "null";
      } else {
        float_ = Float.valueOf(keyframe.getFraction());
      } 
      stringBuilder = stringBuilder.append(float_).append(", , value : ");
      Object object = str;
      if (keyframe.hasValue())
        object = keyframe.getValue(); 
      Log.d("AnimatorInflater", stringBuilder.append(object).toString());
    } 
  }
  
  private static PropertyValuesHolder getPVH(TypedArray paramTypedArray, int paramInt1, int paramInt2, int paramInt3, String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: iload_2
    //   2: invokevirtual peekValue : (I)Landroid/util/TypedValue;
    //   5: astore #12
    //   7: aload #12
    //   9: ifnull -> 18
    //   12: iconst_1
    //   13: istore #7
    //   15: goto -> 21
    //   18: iconst_0
    //   19: istore #7
    //   21: iload #7
    //   23: ifeq -> 36
    //   26: aload #12
    //   28: getfield type : I
    //   31: istore #10
    //   33: goto -> 39
    //   36: iconst_0
    //   37: istore #10
    //   39: aload_0
    //   40: iload_3
    //   41: invokevirtual peekValue : (I)Landroid/util/TypedValue;
    //   44: astore #12
    //   46: aload #12
    //   48: ifnull -> 57
    //   51: iconst_1
    //   52: istore #8
    //   54: goto -> 60
    //   57: iconst_0
    //   58: istore #8
    //   60: iload #8
    //   62: ifeq -> 75
    //   65: aload #12
    //   67: getfield type : I
    //   70: istore #9
    //   72: goto -> 78
    //   75: iconst_0
    //   76: istore #9
    //   78: iload_1
    //   79: iconst_4
    //   80: if_icmpne -> 119
    //   83: iload #7
    //   85: ifeq -> 96
    //   88: iload #10
    //   90: invokestatic isColorType : (I)Z
    //   93: ifne -> 109
    //   96: iload #8
    //   98: ifeq -> 114
    //   101: iload #9
    //   103: invokestatic isColorType : (I)Z
    //   106: ifeq -> 114
    //   109: iconst_3
    //   110: istore_1
    //   111: goto -> 119
    //   114: iconst_0
    //   115: istore_1
    //   116: goto -> 119
    //   119: iload_1
    //   120: ifne -> 129
    //   123: iconst_1
    //   124: istore #11
    //   126: goto -> 132
    //   129: iconst_0
    //   130: istore #11
    //   132: iload_1
    //   133: iconst_2
    //   134: if_icmpne -> 330
    //   137: aload_0
    //   138: iload_2
    //   139: invokevirtual getString : (I)Ljava/lang/String;
    //   142: astore #12
    //   144: aload_0
    //   145: iload_3
    //   146: invokevirtual getString : (I)Ljava/lang/String;
    //   149: astore_0
    //   150: aload #12
    //   152: invokestatic createNodesFromPathData : (Ljava/lang/String;)[Landroidx/core/graphics/PathParser$PathDataNode;
    //   155: astore #15
    //   157: aload_0
    //   158: invokestatic createNodesFromPathData : (Ljava/lang/String;)[Landroidx/core/graphics/PathParser$PathDataNode;
    //   161: astore #14
    //   163: aload #15
    //   165: ifnonnull -> 179
    //   168: aload #14
    //   170: ifnull -> 176
    //   173: goto -> 179
    //   176: goto -> 322
    //   179: aload #15
    //   181: ifnull -> 292
    //   184: new androidx/vectordrawable/graphics/drawable/AnimatorInflaterCompat$PathDataEvaluator
    //   187: dup
    //   188: invokespecial <init> : ()V
    //   191: astore #13
    //   193: aload #14
    //   195: ifnull -> 272
    //   198: aload #15
    //   200: aload #14
    //   202: invokestatic canMorph : ([Landroidx/core/graphics/PathParser$PathDataNode;[Landroidx/core/graphics/PathParser$PathDataNode;)Z
    //   205: ifeq -> 233
    //   208: aload #4
    //   210: aload #13
    //   212: iconst_2
    //   213: anewarray java/lang/Object
    //   216: dup
    //   217: iconst_0
    //   218: aload #15
    //   220: aastore
    //   221: dup
    //   222: iconst_1
    //   223: aload #14
    //   225: aastore
    //   226: invokestatic ofObject : (Ljava/lang/String;Landroid/animation/TypeEvaluator;[Ljava/lang/Object;)Landroid/animation/PropertyValuesHolder;
    //   229: astore_0
    //   230: goto -> 289
    //   233: new android/view/InflateException
    //   236: dup
    //   237: new java/lang/StringBuilder
    //   240: dup
    //   241: invokespecial <init> : ()V
    //   244: ldc_w ' Can't morph from '
    //   247: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   250: aload #12
    //   252: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   255: ldc_w ' to '
    //   258: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   261: aload_0
    //   262: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   265: invokevirtual toString : ()Ljava/lang/String;
    //   268: invokespecial <init> : (Ljava/lang/String;)V
    //   271: athrow
    //   272: aload #4
    //   274: aload #13
    //   276: iconst_1
    //   277: anewarray java/lang/Object
    //   280: dup
    //   281: iconst_0
    //   282: aload #15
    //   284: aastore
    //   285: invokestatic ofObject : (Ljava/lang/String;Landroid/animation/TypeEvaluator;[Ljava/lang/Object;)Landroid/animation/PropertyValuesHolder;
    //   288: astore_0
    //   289: goto -> 324
    //   292: aload #14
    //   294: ifnull -> 322
    //   297: aload #4
    //   299: new androidx/vectordrawable/graphics/drawable/AnimatorInflaterCompat$PathDataEvaluator
    //   302: dup
    //   303: invokespecial <init> : ()V
    //   306: iconst_1
    //   307: anewarray java/lang/Object
    //   310: dup
    //   311: iconst_0
    //   312: aload #14
    //   314: aastore
    //   315: invokestatic ofObject : (Ljava/lang/String;Landroid/animation/TypeEvaluator;[Ljava/lang/Object;)Landroid/animation/PropertyValuesHolder;
    //   318: astore_0
    //   319: goto -> 324
    //   322: aconst_null
    //   323: astore_0
    //   324: aload_0
    //   325: astore #4
    //   327: goto -> 708
    //   330: aconst_null
    //   331: astore #12
    //   333: iload_1
    //   334: iconst_3
    //   335: if_icmpne -> 343
    //   338: invokestatic getInstance : ()Landroidx/vectordrawable/graphics/drawable/ArgbEvaluator;
    //   341: astore #12
    //   343: iload #11
    //   345: ifeq -> 489
    //   348: iload #7
    //   350: ifeq -> 447
    //   353: iload #10
    //   355: iconst_5
    //   356: if_icmpne -> 370
    //   359: aload_0
    //   360: iload_2
    //   361: fconst_0
    //   362: invokevirtual getDimension : (IF)F
    //   365: fstore #5
    //   367: goto -> 378
    //   370: aload_0
    //   371: iload_2
    //   372: fconst_0
    //   373: invokevirtual getFloat : (IF)F
    //   376: fstore #5
    //   378: iload #8
    //   380: ifeq -> 430
    //   383: iload #9
    //   385: iconst_5
    //   386: if_icmpne -> 400
    //   389: aload_0
    //   390: iload_3
    //   391: fconst_0
    //   392: invokevirtual getDimension : (IF)F
    //   395: fstore #6
    //   397: goto -> 408
    //   400: aload_0
    //   401: iload_3
    //   402: fconst_0
    //   403: invokevirtual getFloat : (IF)F
    //   406: fstore #6
    //   408: aload #4
    //   410: iconst_2
    //   411: newarray float
    //   413: dup
    //   414: iconst_0
    //   415: fload #5
    //   417: fastore
    //   418: dup
    //   419: iconst_1
    //   420: fload #6
    //   422: fastore
    //   423: invokestatic ofFloat : (Ljava/lang/String;[F)Landroid/animation/PropertyValuesHolder;
    //   426: astore_0
    //   427: goto -> 486
    //   430: aload #4
    //   432: iconst_1
    //   433: newarray float
    //   435: dup
    //   436: iconst_0
    //   437: fload #5
    //   439: fastore
    //   440: invokestatic ofFloat : (Ljava/lang/String;[F)Landroid/animation/PropertyValuesHolder;
    //   443: astore_0
    //   444: goto -> 486
    //   447: iload #9
    //   449: iconst_5
    //   450: if_icmpne -> 464
    //   453: aload_0
    //   454: iload_3
    //   455: fconst_0
    //   456: invokevirtual getDimension : (IF)F
    //   459: fstore #5
    //   461: goto -> 472
    //   464: aload_0
    //   465: iload_3
    //   466: fconst_0
    //   467: invokevirtual getFloat : (IF)F
    //   470: fstore #5
    //   472: aload #4
    //   474: iconst_1
    //   475: newarray float
    //   477: dup
    //   478: iconst_0
    //   479: fload #5
    //   481: fastore
    //   482: invokestatic ofFloat : (Ljava/lang/String;[F)Landroid/animation/PropertyValuesHolder;
    //   485: astore_0
    //   486: goto -> 684
    //   489: iload #7
    //   491: ifeq -> 619
    //   494: iload #10
    //   496: iconst_5
    //   497: if_icmpne -> 511
    //   500: aload_0
    //   501: iload_2
    //   502: fconst_0
    //   503: invokevirtual getDimension : (IF)F
    //   506: f2i
    //   507: istore_1
    //   508: goto -> 536
    //   511: iload #10
    //   513: invokestatic isColorType : (I)Z
    //   516: ifeq -> 529
    //   519: aload_0
    //   520: iload_2
    //   521: iconst_0
    //   522: invokevirtual getColor : (II)I
    //   525: istore_1
    //   526: goto -> 536
    //   529: aload_0
    //   530: iload_2
    //   531: iconst_0
    //   532: invokevirtual getInt : (II)I
    //   535: istore_1
    //   536: iload #8
    //   538: ifeq -> 603
    //   541: iload #9
    //   543: iconst_5
    //   544: if_icmpne -> 558
    //   547: aload_0
    //   548: iload_3
    //   549: fconst_0
    //   550: invokevirtual getDimension : (IF)F
    //   553: f2i
    //   554: istore_2
    //   555: goto -> 583
    //   558: iload #9
    //   560: invokestatic isColorType : (I)Z
    //   563: ifeq -> 576
    //   566: aload_0
    //   567: iload_3
    //   568: iconst_0
    //   569: invokevirtual getColor : (II)I
    //   572: istore_2
    //   573: goto -> 583
    //   576: aload_0
    //   577: iload_3
    //   578: iconst_0
    //   579: invokevirtual getInt : (II)I
    //   582: istore_2
    //   583: aload #4
    //   585: iconst_2
    //   586: newarray int
    //   588: dup
    //   589: iconst_0
    //   590: iload_1
    //   591: iastore
    //   592: dup
    //   593: iconst_1
    //   594: iload_2
    //   595: iastore
    //   596: invokestatic ofInt : (Ljava/lang/String;[I)Landroid/animation/PropertyValuesHolder;
    //   599: astore_0
    //   600: goto -> 684
    //   603: aload #4
    //   605: iconst_1
    //   606: newarray int
    //   608: dup
    //   609: iconst_0
    //   610: iload_1
    //   611: iastore
    //   612: invokestatic ofInt : (Ljava/lang/String;[I)Landroid/animation/PropertyValuesHolder;
    //   615: astore_0
    //   616: goto -> 684
    //   619: iload #8
    //   621: ifeq -> 682
    //   624: iload #9
    //   626: iconst_5
    //   627: if_icmpne -> 641
    //   630: aload_0
    //   631: iload_3
    //   632: fconst_0
    //   633: invokevirtual getDimension : (IF)F
    //   636: f2i
    //   637: istore_1
    //   638: goto -> 666
    //   641: iload #9
    //   643: invokestatic isColorType : (I)Z
    //   646: ifeq -> 659
    //   649: aload_0
    //   650: iload_3
    //   651: iconst_0
    //   652: invokevirtual getColor : (II)I
    //   655: istore_1
    //   656: goto -> 666
    //   659: aload_0
    //   660: iload_3
    //   661: iconst_0
    //   662: invokevirtual getInt : (II)I
    //   665: istore_1
    //   666: aload #4
    //   668: iconst_1
    //   669: newarray int
    //   671: dup
    //   672: iconst_0
    //   673: iload_1
    //   674: iastore
    //   675: invokestatic ofInt : (Ljava/lang/String;[I)Landroid/animation/PropertyValuesHolder;
    //   678: astore_0
    //   679: goto -> 684
    //   682: aconst_null
    //   683: astore_0
    //   684: aload_0
    //   685: astore #4
    //   687: aload_0
    //   688: ifnull -> 708
    //   691: aload_0
    //   692: astore #4
    //   694: aload #12
    //   696: ifnull -> 708
    //   699: aload_0
    //   700: aload #12
    //   702: invokevirtual setEvaluator : (Landroid/animation/TypeEvaluator;)V
    //   705: aload_0
    //   706: astore #4
    //   708: aload #4
    //   710: areturn
  }
  
  private static int inferValueTypeFromValues(TypedArray paramTypedArray, int paramInt1, int paramInt2) {
    boolean bool1;
    TypedValue typedValue2 = paramTypedArray.peekValue(paramInt1);
    boolean bool2 = true;
    int i = 0;
    if (typedValue2 != null) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    } 
    if (paramInt1 != 0) {
      bool1 = typedValue2.type;
    } else {
      bool1 = false;
    } 
    TypedValue typedValue1 = paramTypedArray.peekValue(paramInt2);
    if (typedValue1 != null) {
      paramInt2 = bool2;
    } else {
      paramInt2 = 0;
    } 
    if (paramInt2 != 0)
      i = typedValue1.type; 
    if ((paramInt1 != 0 && isColorType(bool1)) || (paramInt2 != 0 && isColorType(i))) {
      paramInt1 = 3;
    } else {
      paramInt1 = 0;
    } 
    return paramInt1;
  }
  
  private static int inferValueTypeOfKeyframe(Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, XmlPullParser paramXmlPullParser) {
    TypedArray typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_KEYFRAME);
    byte b = 0;
    TypedValue typedValue = TypedArrayUtils.peekNamedValue(typedArray, paramXmlPullParser, "value", 0);
    if (typedValue != null)
      b = 1; 
    if (b && isColorType(typedValue.type)) {
      b = 3;
    } else {
      b = 0;
    } 
    typedArray.recycle();
    return b;
  }
  
  private static boolean isColorType(int paramInt) {
    boolean bool;
    if (paramInt >= 28 && paramInt <= 31) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static Animator loadAnimator(Context paramContext, int paramInt) throws Resources.NotFoundException {
    Animator animator;
    if (Build.VERSION.SDK_INT >= 24) {
      animator = AnimatorInflater.loadAnimator(paramContext, paramInt);
    } else {
      animator = loadAnimator((Context)animator, animator.getResources(), animator.getTheme(), paramInt);
    } 
    return animator;
  }
  
  public static Animator loadAnimator(Context paramContext, Resources paramResources, Resources.Theme paramTheme, int paramInt) throws Resources.NotFoundException {
    return loadAnimator(paramContext, paramResources, paramTheme, paramInt, 1.0F);
  }
  
  public static Animator loadAnimator(Context paramContext, Resources paramResources, Resources.Theme paramTheme, int paramInt, float paramFloat) throws Resources.NotFoundException {
    XmlResourceParser xmlResourceParser2 = null;
    XmlResourceParser xmlResourceParser3 = null;
    XmlResourceParser xmlResourceParser1 = null;
    try {
      XmlResourceParser xmlResourceParser = paramResources.getAnimation(paramInt);
      xmlResourceParser1 = xmlResourceParser;
      xmlResourceParser2 = xmlResourceParser;
      xmlResourceParser3 = xmlResourceParser;
      Animator animator = createAnimatorFromXml(paramContext, paramResources, paramTheme, (XmlPullParser)xmlResourceParser, paramFloat);
      if (xmlResourceParser != null)
        xmlResourceParser.close(); 
      return animator;
    } catch (XmlPullParserException xmlPullParserException) {
      xmlResourceParser1 = xmlResourceParser3;
      Resources.NotFoundException notFoundException = new Resources.NotFoundException();
      xmlResourceParser1 = xmlResourceParser3;
      StringBuilder stringBuilder = new StringBuilder();
      xmlResourceParser1 = xmlResourceParser3;
      this();
      xmlResourceParser1 = xmlResourceParser3;
      this(stringBuilder.append("Can't load animation resource ID #0x").append(Integer.toHexString(paramInt)).toString());
      xmlResourceParser1 = xmlResourceParser3;
      notFoundException.initCause((Throwable)xmlPullParserException);
      xmlResourceParser1 = xmlResourceParser3;
      throw notFoundException;
    } catch (IOException iOException) {
      xmlResourceParser1 = xmlResourceParser2;
      Resources.NotFoundException notFoundException = new Resources.NotFoundException();
      xmlResourceParser1 = xmlResourceParser2;
      StringBuilder stringBuilder = new StringBuilder();
      xmlResourceParser1 = xmlResourceParser2;
      this();
      xmlResourceParser1 = xmlResourceParser2;
      this(stringBuilder.append("Can't load animation resource ID #0x").append(Integer.toHexString(paramInt)).toString());
      xmlResourceParser1 = xmlResourceParser2;
      notFoundException.initCause(iOException);
      xmlResourceParser1 = xmlResourceParser2;
      throw notFoundException;
    } finally {}
    if (xmlResourceParser1 != null)
      xmlResourceParser1.close(); 
    throw paramContext;
  }
  
  private static ValueAnimator loadAnimator(Context paramContext, Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, ValueAnimator paramValueAnimator, float paramFloat, XmlPullParser paramXmlPullParser) throws Resources.NotFoundException {
    TypedArray typedArray2 = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_ANIMATOR);
    TypedArray typedArray1 = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_PROPERTY_ANIMATOR);
    ValueAnimator valueAnimator = paramValueAnimator;
    if (paramValueAnimator == null)
      valueAnimator = new ValueAnimator(); 
    parseAnimatorFromTypeArray(valueAnimator, typedArray2, typedArray1, paramFloat, paramXmlPullParser);
    int i = TypedArrayUtils.getNamedResourceId(typedArray2, paramXmlPullParser, "interpolator", 0, 0);
    if (i > 0)
      valueAnimator.setInterpolator((TimeInterpolator)AnimationUtilsCompat.loadInterpolator(paramContext, i)); 
    typedArray2.recycle();
    if (typedArray1 != null)
      typedArray1.recycle(); 
    return valueAnimator;
  }
  
  private static Keyframe loadKeyframe(Context paramContext, Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, int paramInt, XmlPullParser paramXmlPullParser) throws XmlPullParserException, IOException {
    Keyframe keyframe;
    boolean bool;
    TypedArray typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_KEYFRAME);
    paramResources = null;
    float f = TypedArrayUtils.getNamedFloat(typedArray, paramXmlPullParser, "fraction", 3, -1.0F);
    TypedValue typedValue = TypedArrayUtils.peekNamedValue(typedArray, paramXmlPullParser, "value", 0);
    if (typedValue != null) {
      bool = true;
    } else {
      bool = false;
    } 
    int i = paramInt;
    if (paramInt == 4)
      if (bool && isColorType(typedValue.type)) {
        i = 3;
      } else {
        i = 0;
      }  
    if (bool) {
      switch (i) {
        case 1:
        case 3:
          keyframe = Keyframe.ofInt(f, TypedArrayUtils.getNamedInt(typedArray, paramXmlPullParser, "value", 0, 0));
          break;
        case 0:
          keyframe = Keyframe.ofFloat(f, TypedArrayUtils.getNamedFloat(typedArray, paramXmlPullParser, "value", 0, 0.0F));
          break;
      } 
    } else if (i == 0) {
      keyframe = Keyframe.ofFloat(f);
    } else {
      keyframe = Keyframe.ofInt(f);
    } 
    paramInt = TypedArrayUtils.getNamedResourceId(typedArray, paramXmlPullParser, "interpolator", 1, 0);
    if (paramInt > 0)
      keyframe.setInterpolator((TimeInterpolator)AnimationUtilsCompat.loadInterpolator(paramContext, paramInt)); 
    typedArray.recycle();
    return keyframe;
  }
  
  private static ObjectAnimator loadObjectAnimator(Context paramContext, Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, float paramFloat, XmlPullParser paramXmlPullParser) throws Resources.NotFoundException {
    ObjectAnimator objectAnimator = new ObjectAnimator();
    loadAnimator(paramContext, paramResources, paramTheme, paramAttributeSet, (ValueAnimator)objectAnimator, paramFloat, paramXmlPullParser);
    return objectAnimator;
  }
  
  private static PropertyValuesHolder loadPvh(Context paramContext, Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser, String paramString, int paramInt) throws XmlPullParserException, IOException {
    int j;
    boolean bool = false;
    ArrayList<Keyframe> arrayList = null;
    int i = paramInt;
    while (true) {
      paramInt = paramXmlPullParser.next();
      j = paramInt;
      if (paramInt != 3 && j != 1) {
        if (paramXmlPullParser.getName().equals("keyframe")) {
          if (i == 4)
            i = inferValueTypeOfKeyframe(paramResources, paramTheme, Xml.asAttributeSet(paramXmlPullParser), paramXmlPullParser); 
          Keyframe keyframe = loadKeyframe(paramContext, paramResources, paramTheme, Xml.asAttributeSet(paramXmlPullParser), i, paramXmlPullParser);
          ArrayList<Keyframe> arrayList1 = arrayList;
          if (keyframe != null) {
            arrayList1 = arrayList;
            if (arrayList == null)
              arrayList1 = new ArrayList(); 
            arrayList1.add(keyframe);
          } 
          paramXmlPullParser.next();
          arrayList = arrayList1;
        } 
        continue;
      } 
      break;
    } 
    if (arrayList != null) {
      paramInt = arrayList.size();
      int k = paramInt;
      if (paramInt > 0) {
        Keyframe keyframe1 = arrayList.get(0);
        Keyframe keyframe2 = arrayList.get(k - 1);
        float f2 = keyframe2.getFraction();
        float f1 = 0.0F;
        paramInt = k;
        if (f2 < 1.0F)
          if (f2 < 0.0F) {
            keyframe2.setFraction(1.0F);
            paramInt = k;
          } else {
            arrayList.add(arrayList.size(), createNewKeyframe(keyframe2, 1.0F));
            paramInt = k + 1;
          }  
        f2 = keyframe1.getFraction();
        int m = paramInt;
        if (f2 != 0.0F)
          if (f2 < 0.0F) {
            keyframe1.setFraction(0.0F);
            m = paramInt;
          } else {
            arrayList.add(0, createNewKeyframe(keyframe1, 0.0F));
            m = paramInt + 1;
          }  
        Keyframe[] arrayOfKeyframe = new Keyframe[m];
        arrayList.toArray(arrayOfKeyframe);
        k = 0;
        paramInt = j;
        while (k < m) {
          keyframe2 = arrayOfKeyframe[k];
          if (keyframe2.getFraction() < f1)
            if (k == 0) {
              keyframe2.setFraction(f1);
            } else if (k == m - 1) {
              keyframe2.setFraction(1.0F);
              f1 = 0.0F;
            } else {
              int i1 = k + 1;
              int n = k;
              j = paramInt;
              for (paramInt = i1; paramInt < m - 1 && arrayOfKeyframe[paramInt].getFraction() < 0.0F; paramInt++)
                n = paramInt; 
              f1 = 0.0F;
              distributeKeyframes(arrayOfKeyframe, arrayOfKeyframe[n + 1].getFraction() - arrayOfKeyframe[k - 1].getFraction(), k, n);
              paramInt = j;
            }  
          k++;
        } 
        PropertyValuesHolder propertyValuesHolder2 = PropertyValuesHolder.ofKeyframe(paramString, arrayOfKeyframe);
        PropertyValuesHolder propertyValuesHolder1 = propertyValuesHolder2;
        if (i == 3) {
          propertyValuesHolder2.setEvaluator(ArgbEvaluator.getInstance());
          propertyValuesHolder1 = propertyValuesHolder2;
        } 
        return propertyValuesHolder1;
      } 
    } 
    return null;
  }
  
  private static PropertyValuesHolder[] loadValues(Context paramContext, Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet) throws XmlPullParserException, IOException {
    PropertyValuesHolder[] arrayOfPropertyValuesHolder;
    ArrayList<PropertyValuesHolder> arrayList;
    String str = null;
    while (true) {
      int i = paramXmlPullParser.getEventType();
      if (i != 3 && i != 1) {
        if (i != 2) {
          paramXmlPullParser.next();
          continue;
        } 
        if (paramXmlPullParser.getName().equals("propertyValuesHolder")) {
          ArrayList<PropertyValuesHolder> arrayList1;
          TypedArray typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_PROPERTY_VALUES_HOLDER);
          String str1 = TypedArrayUtils.getNamedString(typedArray, paramXmlPullParser, "propertyName", 3);
          i = TypedArrayUtils.getNamedInt(typedArray, paramXmlPullParser, "valueType", 2, 4);
          PropertyValuesHolder propertyValuesHolder = loadPvh(paramContext, paramResources, paramTheme, paramXmlPullParser, str1, i);
          if (propertyValuesHolder == null)
            propertyValuesHolder = getPVH(typedArray, i, 0, 1, str1); 
          str1 = str;
          if (propertyValuesHolder != null) {
            str1 = str;
            if (str == null)
              arrayList1 = new ArrayList(); 
            arrayList1.add(propertyValuesHolder);
          } 
          typedArray.recycle();
          arrayList = arrayList1;
        } 
        paramXmlPullParser.next();
        continue;
      } 
      break;
    } 
    paramContext = null;
    if (arrayList != null) {
      int i = arrayList.size();
      PropertyValuesHolder[] arrayOfPropertyValuesHolder1 = new PropertyValuesHolder[i];
      byte b = 0;
      while (true) {
        arrayOfPropertyValuesHolder = arrayOfPropertyValuesHolder1;
        if (b < i) {
          arrayOfPropertyValuesHolder1[b] = arrayList.get(b);
          b++;
          continue;
        } 
        break;
      } 
    } 
    return arrayOfPropertyValuesHolder;
  }
  
  private static void parseAnimatorFromTypeArray(ValueAnimator paramValueAnimator, TypedArray paramTypedArray1, TypedArray paramTypedArray2, float paramFloat, XmlPullParser paramXmlPullParser) {
    long l1 = TypedArrayUtils.getNamedInt(paramTypedArray1, paramXmlPullParser, "duration", 1, 300);
    long l2 = TypedArrayUtils.getNamedInt(paramTypedArray1, paramXmlPullParser, "startOffset", 2, 0);
    int i = TypedArrayUtils.getNamedInt(paramTypedArray1, paramXmlPullParser, "valueType", 7, 4);
    int j = i;
    if (TypedArrayUtils.hasAttribute(paramXmlPullParser, "valueFrom")) {
      j = i;
      if (TypedArrayUtils.hasAttribute(paramXmlPullParser, "valueTo")) {
        int k = i;
        if (i == 4)
          k = inferValueTypeFromValues(paramTypedArray1, 5, 6); 
        PropertyValuesHolder propertyValuesHolder = getPVH(paramTypedArray1, k, 5, 6, "");
        j = k;
        if (propertyValuesHolder != null) {
          paramValueAnimator.setValues(new PropertyValuesHolder[] { propertyValuesHolder });
          j = k;
        } 
      } 
    } 
    paramValueAnimator.setDuration(l1);
    paramValueAnimator.setStartDelay(l2);
    paramValueAnimator.setRepeatCount(TypedArrayUtils.getNamedInt(paramTypedArray1, paramXmlPullParser, "repeatCount", 3, 0));
    paramValueAnimator.setRepeatMode(TypedArrayUtils.getNamedInt(paramTypedArray1, paramXmlPullParser, "repeatMode", 4, 1));
    if (paramTypedArray2 != null)
      setupObjectAnimator(paramValueAnimator, paramTypedArray2, j, paramFloat, paramXmlPullParser); 
  }
  
  private static void setupObjectAnimator(ValueAnimator paramValueAnimator, TypedArray paramTypedArray, int paramInt, float paramFloat, XmlPullParser paramXmlPullParser) {
    // Byte code:
    //   0: aload_0
    //   1: checkcast android/animation/ObjectAnimator
    //   4: astore #6
    //   6: aload_1
    //   7: aload #4
    //   9: ldc_w 'pathData'
    //   12: iconst_1
    //   13: invokestatic getNamedString : (Landroid/content/res/TypedArray;Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;I)Ljava/lang/String;
    //   16: astore_0
    //   17: aload_0
    //   18: ifnull -> 120
    //   21: aload_1
    //   22: aload #4
    //   24: ldc_w 'propertyXName'
    //   27: iconst_2
    //   28: invokestatic getNamedString : (Landroid/content/res/TypedArray;Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;I)Ljava/lang/String;
    //   31: astore #5
    //   33: aload_1
    //   34: aload #4
    //   36: ldc_w 'propertyYName'
    //   39: iconst_3
    //   40: invokestatic getNamedString : (Landroid/content/res/TypedArray;Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;I)Ljava/lang/String;
    //   43: astore #4
    //   45: iload_2
    //   46: iconst_2
    //   47: if_icmpeq -> 55
    //   50: iload_2
    //   51: iconst_4
    //   52: if_icmpne -> 55
    //   55: aload #5
    //   57: ifnonnull -> 99
    //   60: aload #4
    //   62: ifnull -> 68
    //   65: goto -> 99
    //   68: new android/view/InflateException
    //   71: dup
    //   72: new java/lang/StringBuilder
    //   75: dup
    //   76: invokespecial <init> : ()V
    //   79: aload_1
    //   80: invokevirtual getPositionDescription : ()Ljava/lang/String;
    //   83: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   86: ldc_w ' propertyXName or propertyYName is needed for PathData'
    //   89: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   92: invokevirtual toString : ()Ljava/lang/String;
    //   95: invokespecial <init> : (Ljava/lang/String;)V
    //   98: athrow
    //   99: aload_0
    //   100: invokestatic createPathFromPathData : (Ljava/lang/String;)Landroid/graphics/Path;
    //   103: aload #6
    //   105: ldc_w 0.5
    //   108: fload_3
    //   109: fmul
    //   110: aload #5
    //   112: aload #4
    //   114: invokestatic setupPathMotion : (Landroid/graphics/Path;Landroid/animation/ObjectAnimator;FLjava/lang/String;Ljava/lang/String;)V
    //   117: goto -> 135
    //   120: aload #6
    //   122: aload_1
    //   123: aload #4
    //   125: ldc_w 'propertyName'
    //   128: iconst_0
    //   129: invokestatic getNamedString : (Landroid/content/res/TypedArray;Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;I)Ljava/lang/String;
    //   132: invokevirtual setPropertyName : (Ljava/lang/String;)V
    //   135: return
  }
  
  private static void setupPathMotion(Path paramPath, ObjectAnimator paramObjectAnimator, float paramFloat, String paramString1, String paramString2) {
    PathMeasure pathMeasure = new PathMeasure(paramPath, false);
    float f = 0.0F;
    ArrayList<Float> arrayList = new ArrayList();
    arrayList.add(Float.valueOf(0.0F));
    while (true) {
      f += pathMeasure.getLength();
      arrayList.add(Float.valueOf(f));
      if (!pathMeasure.nextContour()) {
        PropertyValuesHolder propertyValuesHolder1;
        PropertyValuesHolder propertyValuesHolder2;
        PathMeasure pathMeasure1 = new PathMeasure(paramPath, false);
        int j = Math.min(100, (int)(f / paramFloat) + 1);
        float[] arrayOfFloat3 = new float[j];
        float[] arrayOfFloat2 = new float[j];
        float[] arrayOfFloat1 = new float[2];
        int i = 0;
        f /= (j - 1);
        paramFloat = 0.0F;
        byte b = 0;
        while (b < j) {
          pathMeasure1.getPosTan(paramFloat - ((Float)arrayList.get(i)).floatValue(), arrayOfFloat1, null);
          arrayOfFloat3[b] = arrayOfFloat1[0];
          arrayOfFloat2[b] = arrayOfFloat1[1];
          paramFloat += f;
          int k = i;
          if (i + 1 < arrayList.size()) {
            k = i;
            if (paramFloat > ((Float)arrayList.get(i + 1)).floatValue()) {
              k = i + 1;
              pathMeasure1.nextContour();
            } 
          } 
          b++;
          i = k;
        } 
        arrayOfFloat1 = null;
        arrayList = null;
        if (paramString1 != null)
          propertyValuesHolder1 = PropertyValuesHolder.ofFloat(paramString1, arrayOfFloat3); 
        ArrayList<Float> arrayList1 = arrayList;
        if (paramString2 != null)
          propertyValuesHolder2 = PropertyValuesHolder.ofFloat(paramString2, arrayOfFloat2); 
        if (propertyValuesHolder1 == null) {
          paramObjectAnimator.setValues(new PropertyValuesHolder[] { propertyValuesHolder2 });
        } else if (propertyValuesHolder2 == null) {
          paramObjectAnimator.setValues(new PropertyValuesHolder[] { propertyValuesHolder1 });
        } else {
          paramObjectAnimator.setValues(new PropertyValuesHolder[] { propertyValuesHolder1, propertyValuesHolder2 });
        } 
        return;
      } 
    } 
  }
  
  private static class PathDataEvaluator implements TypeEvaluator<PathParser.PathDataNode[]> {
    private PathParser.PathDataNode[] mNodeArray;
    
    PathDataEvaluator() {}
    
    PathDataEvaluator(PathParser.PathDataNode[] param1ArrayOfPathDataNode) {
      this.mNodeArray = param1ArrayOfPathDataNode;
    }
    
    public PathParser.PathDataNode[] evaluate(float param1Float, PathParser.PathDataNode[] param1ArrayOfPathDataNode1, PathParser.PathDataNode[] param1ArrayOfPathDataNode2) {
      if (PathParser.canMorph(param1ArrayOfPathDataNode1, param1ArrayOfPathDataNode2)) {
        if (!PathParser.canMorph(this.mNodeArray, param1ArrayOfPathDataNode1))
          this.mNodeArray = PathParser.deepCopyNodes(param1ArrayOfPathDataNode1); 
        for (byte b = 0; b < param1ArrayOfPathDataNode1.length; b++)
          this.mNodeArray[b].interpolatePathDataNode(param1ArrayOfPathDataNode1[b], param1ArrayOfPathDataNode2[b], param1Float); 
        return this.mNodeArray;
      } 
      throw new IllegalArgumentException("Can't interpolate between two incompatible pathData");
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\vectordrawable\graphics\drawable\AnimatorInflaterCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */