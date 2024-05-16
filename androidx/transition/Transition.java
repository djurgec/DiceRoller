package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import androidx.collection.ArrayMap;
import androidx.collection.LongSparseArray;
import androidx.collection.SimpleArrayMap;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import org.xmlpull.v1.XmlPullParser;

public abstract class Transition implements Cloneable {
  static final boolean DBG = false;
  
  private static final int[] DEFAULT_MATCH_ORDER = new int[] { 2, 1, 3, 4 };
  
  private static final String LOG_TAG = "Transition";
  
  private static final int MATCH_FIRST = 1;
  
  public static final int MATCH_ID = 3;
  
  private static final String MATCH_ID_STR = "id";
  
  public static final int MATCH_INSTANCE = 1;
  
  private static final String MATCH_INSTANCE_STR = "instance";
  
  public static final int MATCH_ITEM_ID = 4;
  
  private static final String MATCH_ITEM_ID_STR = "itemId";
  
  private static final int MATCH_LAST = 4;
  
  public static final int MATCH_NAME = 2;
  
  private static final String MATCH_NAME_STR = "name";
  
  private static final PathMotion STRAIGHT_PATH_MOTION = new PathMotion() {
      public Path getPath(float param1Float1, float param1Float2, float param1Float3, float param1Float4) {
        Path path = new Path();
        path.moveTo(param1Float1, param1Float2);
        path.lineTo(param1Float3, param1Float4);
        return path;
      }
    };
  
  private static ThreadLocal<ArrayMap<Animator, AnimationInfo>> sRunningAnimators = new ThreadLocal<>();
  
  private ArrayList<Animator> mAnimators = new ArrayList<>();
  
  boolean mCanRemoveViews = false;
  
  ArrayList<Animator> mCurrentAnimators = new ArrayList<>();
  
  long mDuration = -1L;
  
  private TransitionValuesMaps mEndValues = new TransitionValuesMaps();
  
  private ArrayList<TransitionValues> mEndValuesList;
  
  private boolean mEnded = false;
  
  private EpicenterCallback mEpicenterCallback;
  
  private TimeInterpolator mInterpolator = null;
  
  private ArrayList<TransitionListener> mListeners = null;
  
  private int[] mMatchOrder = DEFAULT_MATCH_ORDER;
  
  private String mName = getClass().getName();
  
  private ArrayMap<String, String> mNameOverrides;
  
  private int mNumInstances = 0;
  
  TransitionSet mParent = null;
  
  private PathMotion mPathMotion = STRAIGHT_PATH_MOTION;
  
  private boolean mPaused = false;
  
  TransitionPropagation mPropagation;
  
  private ViewGroup mSceneRoot = null;
  
  private long mStartDelay = -1L;
  
  private TransitionValuesMaps mStartValues = new TransitionValuesMaps();
  
  private ArrayList<TransitionValues> mStartValuesList;
  
  private ArrayList<View> mTargetChildExcludes = null;
  
  private ArrayList<View> mTargetExcludes = null;
  
  private ArrayList<Integer> mTargetIdChildExcludes = null;
  
  private ArrayList<Integer> mTargetIdExcludes = null;
  
  ArrayList<Integer> mTargetIds = new ArrayList<>();
  
  private ArrayList<String> mTargetNameExcludes = null;
  
  private ArrayList<String> mTargetNames = null;
  
  private ArrayList<Class<?>> mTargetTypeChildExcludes = null;
  
  private ArrayList<Class<?>> mTargetTypeExcludes = null;
  
  private ArrayList<Class<?>> mTargetTypes = null;
  
  ArrayList<View> mTargets = new ArrayList<>();
  
  public Transition() {}
  
  public Transition(Context paramContext, AttributeSet paramAttributeSet) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, Styleable.TRANSITION);
    XmlResourceParser xmlResourceParser = (XmlResourceParser)paramAttributeSet;
    long l = TypedArrayUtils.getNamedInt(typedArray, (XmlPullParser)xmlResourceParser, "duration", 1, -1);
    if (l >= 0L)
      setDuration(l); 
    l = TypedArrayUtils.getNamedInt(typedArray, (XmlPullParser)xmlResourceParser, "startDelay", 2, -1);
    if (l > 0L)
      setStartDelay(l); 
    int i = TypedArrayUtils.getNamedResourceId(typedArray, (XmlPullParser)xmlResourceParser, "interpolator", 0, 0);
    if (i > 0)
      setInterpolator((TimeInterpolator)AnimationUtils.loadInterpolator(paramContext, i)); 
    String str = TypedArrayUtils.getNamedString(typedArray, (XmlPullParser)xmlResourceParser, "matchOrder", 3);
    if (str != null)
      setMatchOrder(parseMatchOrder(str)); 
    typedArray.recycle();
  }
  
  private void addUnmatched(ArrayMap<View, TransitionValues> paramArrayMap1, ArrayMap<View, TransitionValues> paramArrayMap2) {
    byte b;
    for (b = 0; b < paramArrayMap1.size(); b++) {
      TransitionValues transitionValues = (TransitionValues)paramArrayMap1.valueAt(b);
      if (isValidTarget(transitionValues.view)) {
        this.mStartValuesList.add(transitionValues);
        this.mEndValuesList.add(null);
      } 
    } 
    for (b = 0; b < paramArrayMap2.size(); b++) {
      TransitionValues transitionValues = (TransitionValues)paramArrayMap2.valueAt(b);
      if (isValidTarget(transitionValues.view)) {
        this.mEndValuesList.add(transitionValues);
        this.mStartValuesList.add(null);
      } 
    } 
  }
  
  private static void addViewValues(TransitionValuesMaps paramTransitionValuesMaps, View paramView, TransitionValues paramTransitionValues) {
    paramTransitionValuesMaps.mViewValues.put(paramView, paramTransitionValues);
    int i = paramView.getId();
    if (i >= 0)
      if (paramTransitionValuesMaps.mIdValues.indexOfKey(i) >= 0) {
        paramTransitionValuesMaps.mIdValues.put(i, null);
      } else {
        paramTransitionValuesMaps.mIdValues.put(i, paramView);
      }  
    String str = ViewCompat.getTransitionName(paramView);
    if (str != null)
      if (paramTransitionValuesMaps.mNameValues.containsKey(str)) {
        paramTransitionValuesMaps.mNameValues.put(str, null);
      } else {
        paramTransitionValuesMaps.mNameValues.put(str, paramView);
      }  
    if (paramView.getParent() instanceof ListView) {
      ListView listView = (ListView)paramView.getParent();
      if (listView.getAdapter().hasStableIds()) {
        long l = listView.getItemIdAtPosition(listView.getPositionForView(paramView));
        if (paramTransitionValuesMaps.mItemIdValues.indexOfKey(l) >= 0) {
          paramView = (View)paramTransitionValuesMaps.mItemIdValues.get(l);
          if (paramView != null) {
            ViewCompat.setHasTransientState(paramView, false);
            paramTransitionValuesMaps.mItemIdValues.put(l, null);
          } 
        } else {
          ViewCompat.setHasTransientState(paramView, true);
          paramTransitionValuesMaps.mItemIdValues.put(l, paramView);
        } 
      } 
    } 
  }
  
  private static boolean alreadyContains(int[] paramArrayOfint, int paramInt) {
    int i = paramArrayOfint[paramInt];
    for (byte b = 0; b < paramInt; b++) {
      if (paramArrayOfint[b] == i)
        return true; 
    } 
    return false;
  }
  
  private void captureHierarchy(View paramView, boolean paramBoolean) {
    if (paramView == null)
      return; 
    int i = paramView.getId();
    ArrayList<Integer> arrayList2 = this.mTargetIdExcludes;
    if (arrayList2 != null && arrayList2.contains(Integer.valueOf(i)))
      return; 
    ArrayList<View> arrayList1 = this.mTargetExcludes;
    if (arrayList1 != null && arrayList1.contains(paramView))
      return; 
    ArrayList<Class<?>> arrayList = this.mTargetTypeExcludes;
    if (arrayList != null) {
      int j = arrayList.size();
      for (byte b = 0; b < j; b++) {
        if (((Class)this.mTargetTypeExcludes.get(b)).isInstance(paramView))
          return; 
      } 
    } 
    if (paramView.getParent() instanceof ViewGroup) {
      TransitionValues transitionValues = new TransitionValues(paramView);
      if (paramBoolean) {
        captureStartValues(transitionValues);
      } else {
        captureEndValues(transitionValues);
      } 
      transitionValues.mTargetedTransitions.add(this);
      capturePropagationValues(transitionValues);
      if (paramBoolean) {
        addViewValues(this.mStartValues, paramView, transitionValues);
      } else {
        addViewValues(this.mEndValues, paramView, transitionValues);
      } 
    } 
    if (paramView instanceof ViewGroup) {
      ArrayList<Integer> arrayList5 = this.mTargetIdChildExcludes;
      if (arrayList5 != null && arrayList5.contains(Integer.valueOf(i)))
        return; 
      ArrayList<View> arrayList4 = this.mTargetChildExcludes;
      if (arrayList4 != null && arrayList4.contains(paramView))
        return; 
      ArrayList<Class<?>> arrayList3 = this.mTargetTypeChildExcludes;
      if (arrayList3 != null) {
        int j = arrayList3.size();
        for (byte b1 = 0; b1 < j; b1++) {
          if (((Class)this.mTargetTypeChildExcludes.get(b1)).isInstance(paramView))
            return; 
        } 
      } 
      ViewGroup viewGroup = (ViewGroup)paramView;
      for (byte b = 0; b < viewGroup.getChildCount(); b++)
        captureHierarchy(viewGroup.getChildAt(b), paramBoolean); 
    } 
  }
  
  private ArrayList<Integer> excludeId(ArrayList<Integer> paramArrayList, int paramInt, boolean paramBoolean) {
    ArrayList<Integer> arrayList = paramArrayList;
    if (paramInt > 0)
      if (paramBoolean) {
        arrayList = ArrayListManager.add(paramArrayList, Integer.valueOf(paramInt));
      } else {
        arrayList = ArrayListManager.remove(paramArrayList, Integer.valueOf(paramInt));
      }  
    return arrayList;
  }
  
  private static <T> ArrayList<T> excludeObject(ArrayList<T> paramArrayList, T paramT, boolean paramBoolean) {
    ArrayList<T> arrayList = paramArrayList;
    if (paramT != null)
      if (paramBoolean) {
        arrayList = ArrayListManager.add(paramArrayList, paramT);
      } else {
        arrayList = ArrayListManager.remove(paramArrayList, paramT);
      }  
    return arrayList;
  }
  
  private ArrayList<Class<?>> excludeType(ArrayList<Class<?>> paramArrayList, Class<?> paramClass, boolean paramBoolean) {
    ArrayList<Class<?>> arrayList = paramArrayList;
    if (paramClass != null)
      if (paramBoolean) {
        arrayList = ArrayListManager.add(paramArrayList, paramClass);
      } else {
        arrayList = ArrayListManager.remove(paramArrayList, paramClass);
      }  
    return arrayList;
  }
  
  private ArrayList<View> excludeView(ArrayList<View> paramArrayList, View paramView, boolean paramBoolean) {
    ArrayList<View> arrayList = paramArrayList;
    if (paramView != null)
      if (paramBoolean) {
        arrayList = ArrayListManager.add(paramArrayList, paramView);
      } else {
        arrayList = ArrayListManager.remove(paramArrayList, paramView);
      }  
    return arrayList;
  }
  
  private static ArrayMap<Animator, AnimationInfo> getRunningAnimators() {
    ArrayMap<Animator, AnimationInfo> arrayMap2 = sRunningAnimators.get();
    ArrayMap<Animator, AnimationInfo> arrayMap1 = arrayMap2;
    if (arrayMap2 == null) {
      arrayMap1 = new ArrayMap();
      sRunningAnimators.set(arrayMap1);
    } 
    return arrayMap1;
  }
  
  private static boolean isValidMatch(int paramInt) {
    boolean bool = true;
    if (paramInt < 1 || paramInt > 4)
      bool = false; 
    return bool;
  }
  
  private static boolean isValueChanged(TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2, String paramString) {
    int i;
    paramTransitionValues1 = (TransitionValues)paramTransitionValues1.values.get(paramString);
    paramTransitionValues2 = (TransitionValues)paramTransitionValues2.values.get(paramString);
    if (paramTransitionValues1 == null && paramTransitionValues2 == null) {
      i = 0;
    } else {
      if (paramTransitionValues1 == null || paramTransitionValues2 == null)
        return true; 
      i = paramTransitionValues1.equals(paramTransitionValues2) ^ true;
    } 
    return i;
  }
  
  private void matchIds(ArrayMap<View, TransitionValues> paramArrayMap1, ArrayMap<View, TransitionValues> paramArrayMap2, SparseArray<View> paramSparseArray1, SparseArray<View> paramSparseArray2) {
    int i = paramSparseArray1.size();
    for (byte b = 0; b < i; b++) {
      View view = (View)paramSparseArray1.valueAt(b);
      if (view != null && isValidTarget(view)) {
        View view1 = (View)paramSparseArray2.get(paramSparseArray1.keyAt(b));
        if (view1 != null && isValidTarget(view1)) {
          TransitionValues transitionValues2 = (TransitionValues)paramArrayMap1.get(view);
          TransitionValues transitionValues1 = (TransitionValues)paramArrayMap2.get(view1);
          if (transitionValues2 != null && transitionValues1 != null) {
            this.mStartValuesList.add(transitionValues2);
            this.mEndValuesList.add(transitionValues1);
            paramArrayMap1.remove(view);
            paramArrayMap2.remove(view1);
          } 
        } 
      } 
    } 
  }
  
  private void matchInstances(ArrayMap<View, TransitionValues> paramArrayMap1, ArrayMap<View, TransitionValues> paramArrayMap2) {
    for (int i = paramArrayMap1.size() - 1; i >= 0; i--) {
      View view = (View)paramArrayMap1.keyAt(i);
      if (view != null && isValidTarget(view)) {
        TransitionValues transitionValues = (TransitionValues)paramArrayMap2.remove(view);
        if (transitionValues != null && isValidTarget(transitionValues.view)) {
          TransitionValues transitionValues1 = (TransitionValues)paramArrayMap1.removeAt(i);
          this.mStartValuesList.add(transitionValues1);
          this.mEndValuesList.add(transitionValues);
        } 
      } 
    } 
  }
  
  private void matchItemIds(ArrayMap<View, TransitionValues> paramArrayMap1, ArrayMap<View, TransitionValues> paramArrayMap2, LongSparseArray<View> paramLongSparseArray1, LongSparseArray<View> paramLongSparseArray2) {
    int i = paramLongSparseArray1.size();
    for (byte b = 0; b < i; b++) {
      View view = (View)paramLongSparseArray1.valueAt(b);
      if (view != null && isValidTarget(view)) {
        View view1 = (View)paramLongSparseArray2.get(paramLongSparseArray1.keyAt(b));
        if (view1 != null && isValidTarget(view1)) {
          TransitionValues transitionValues2 = (TransitionValues)paramArrayMap1.get(view);
          TransitionValues transitionValues1 = (TransitionValues)paramArrayMap2.get(view1);
          if (transitionValues2 != null && transitionValues1 != null) {
            this.mStartValuesList.add(transitionValues2);
            this.mEndValuesList.add(transitionValues1);
            paramArrayMap1.remove(view);
            paramArrayMap2.remove(view1);
          } 
        } 
      } 
    } 
  }
  
  private void matchNames(ArrayMap<View, TransitionValues> paramArrayMap1, ArrayMap<View, TransitionValues> paramArrayMap2, ArrayMap<String, View> paramArrayMap3, ArrayMap<String, View> paramArrayMap4) {
    int i = paramArrayMap3.size();
    for (byte b = 0; b < i; b++) {
      View view = (View)paramArrayMap3.valueAt(b);
      if (view != null && isValidTarget(view)) {
        View view1 = (View)paramArrayMap4.get(paramArrayMap3.keyAt(b));
        if (view1 != null && isValidTarget(view1)) {
          TransitionValues transitionValues1 = (TransitionValues)paramArrayMap1.get(view);
          TransitionValues transitionValues2 = (TransitionValues)paramArrayMap2.get(view1);
          if (transitionValues1 != null && transitionValues2 != null) {
            this.mStartValuesList.add(transitionValues1);
            this.mEndValuesList.add(transitionValues2);
            paramArrayMap1.remove(view);
            paramArrayMap2.remove(view1);
          } 
        } 
      } 
    } 
  }
  
  private void matchStartAndEnd(TransitionValuesMaps paramTransitionValuesMaps1, TransitionValuesMaps paramTransitionValuesMaps2) {
    ArrayMap<View, TransitionValues> arrayMap2 = new ArrayMap((SimpleArrayMap)paramTransitionValuesMaps1.mViewValues);
    ArrayMap<View, TransitionValues> arrayMap1 = new ArrayMap((SimpleArrayMap)paramTransitionValuesMaps2.mViewValues);
    byte b = 0;
    while (true) {
      int[] arrayOfInt = this.mMatchOrder;
      if (b < arrayOfInt.length) {
        switch (arrayOfInt[b]) {
          case 4:
            matchItemIds(arrayMap2, arrayMap1, paramTransitionValuesMaps1.mItemIdValues, paramTransitionValuesMaps2.mItemIdValues);
            break;
          case 3:
            matchIds(arrayMap2, arrayMap1, paramTransitionValuesMaps1.mIdValues, paramTransitionValuesMaps2.mIdValues);
            break;
          case 2:
            matchNames(arrayMap2, arrayMap1, paramTransitionValuesMaps1.mNameValues, paramTransitionValuesMaps2.mNameValues);
            break;
          case 1:
            matchInstances(arrayMap2, arrayMap1);
            break;
        } 
        b++;
        continue;
      } 
      addUnmatched(arrayMap2, arrayMap1);
      return;
    } 
  }
  
  private static int[] parseMatchOrder(String paramString) {
    StringTokenizer stringTokenizer = new StringTokenizer(paramString, ",");
    int[] arrayOfInt = new int[stringTokenizer.countTokens()];
    for (byte b = 0; stringTokenizer.hasMoreTokens(); b++) {
      String str = stringTokenizer.nextToken().trim();
      if ("id".equalsIgnoreCase(str)) {
        arrayOfInt[b] = 3;
      } else if ("instance".equalsIgnoreCase(str)) {
        arrayOfInt[b] = 1;
      } else if ("name".equalsIgnoreCase(str)) {
        arrayOfInt[b] = 2;
      } else if ("itemId".equalsIgnoreCase(str)) {
        arrayOfInt[b] = 4;
      } else {
        int[] arrayOfInt1;
        if (str.isEmpty()) {
          arrayOfInt1 = new int[arrayOfInt.length - 1];
          System.arraycopy(arrayOfInt, 0, arrayOfInt1, 0, b);
          arrayOfInt = arrayOfInt1;
          b--;
        } else {
          throw new InflateException("Unknown match type in matchOrder: '" + arrayOfInt1 + "'");
        } 
      } 
    } 
    return arrayOfInt;
  }
  
  private void runAnimator(Animator paramAnimator, final ArrayMap<Animator, AnimationInfo> runningAnimators) {
    if (paramAnimator != null) {
      paramAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            final Transition this$0;
            
            final ArrayMap val$runningAnimators;
            
            public void onAnimationEnd(Animator param1Animator) {
              runningAnimators.remove(param1Animator);
              Transition.this.mCurrentAnimators.remove(param1Animator);
            }
            
            public void onAnimationStart(Animator param1Animator) {
              Transition.this.mCurrentAnimators.add(param1Animator);
            }
          });
      animate(paramAnimator);
    } 
  }
  
  public Transition addListener(TransitionListener paramTransitionListener) {
    if (this.mListeners == null)
      this.mListeners = new ArrayList<>(); 
    this.mListeners.add(paramTransitionListener);
    return this;
  }
  
  public Transition addTarget(int paramInt) {
    if (paramInt != 0)
      this.mTargetIds.add(Integer.valueOf(paramInt)); 
    return this;
  }
  
  public Transition addTarget(View paramView) {
    this.mTargets.add(paramView);
    return this;
  }
  
  public Transition addTarget(Class<?> paramClass) {
    if (this.mTargetTypes == null)
      this.mTargetTypes = new ArrayList<>(); 
    this.mTargetTypes.add(paramClass);
    return this;
  }
  
  public Transition addTarget(String paramString) {
    if (this.mTargetNames == null)
      this.mTargetNames = new ArrayList<>(); 
    this.mTargetNames.add(paramString);
    return this;
  }
  
  protected void animate(Animator paramAnimator) {
    if (paramAnimator == null) {
      end();
    } else {
      if (getDuration() >= 0L)
        paramAnimator.setDuration(getDuration()); 
      if (getStartDelay() >= 0L)
        paramAnimator.setStartDelay(getStartDelay() + paramAnimator.getStartDelay()); 
      if (getInterpolator() != null)
        paramAnimator.setInterpolator(getInterpolator()); 
      paramAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            final Transition this$0;
            
            public void onAnimationEnd(Animator param1Animator) {
              Transition.this.end();
              param1Animator.removeListener((Animator.AnimatorListener)this);
            }
          });
      paramAnimator.start();
    } 
  }
  
  protected void cancel() {
    int i;
    for (i = this.mCurrentAnimators.size() - 1; i >= 0; i--)
      ((Animator)this.mCurrentAnimators.get(i)).cancel(); 
    ArrayList<TransitionListener> arrayList = this.mListeners;
    if (arrayList != null && arrayList.size() > 0) {
      arrayList = (ArrayList<TransitionListener>)this.mListeners.clone();
      int j = arrayList.size();
      for (i = 0; i < j; i++)
        ((TransitionListener)arrayList.get(i)).onTransitionCancel(this); 
    } 
  }
  
  public abstract void captureEndValues(TransitionValues paramTransitionValues);
  
  void capturePropagationValues(TransitionValues paramTransitionValues) {
    if (this.mPropagation != null && !paramTransitionValues.values.isEmpty()) {
      boolean bool1;
      String[] arrayOfString = this.mPropagation.getPropagationProperties();
      if (arrayOfString == null)
        return; 
      boolean bool2 = true;
      byte b = 0;
      while (true) {
        bool1 = bool2;
        if (b < arrayOfString.length) {
          if (!paramTransitionValues.values.containsKey(arrayOfString[b])) {
            bool1 = false;
            break;
          } 
          b++;
          continue;
        } 
        break;
      } 
      if (!bool1)
        this.mPropagation.captureValues(paramTransitionValues); 
    } 
  }
  
  public abstract void captureStartValues(TransitionValues paramTransitionValues);
  
  void captureValues(ViewGroup paramViewGroup, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: iload_2
    //   2: invokevirtual clearValues : (Z)V
    //   5: aload_0
    //   6: getfield mTargetIds : Ljava/util/ArrayList;
    //   9: invokevirtual size : ()I
    //   12: ifgt -> 25
    //   15: aload_0
    //   16: getfield mTargets : Ljava/util/ArrayList;
    //   19: invokevirtual size : ()I
    //   22: ifle -> 66
    //   25: aload_0
    //   26: getfield mTargetNames : Ljava/util/ArrayList;
    //   29: astore #5
    //   31: aload #5
    //   33: ifnull -> 44
    //   36: aload #5
    //   38: invokevirtual isEmpty : ()Z
    //   41: ifeq -> 66
    //   44: aload_0
    //   45: getfield mTargetTypes : Ljava/util/ArrayList;
    //   48: astore #5
    //   50: aload #5
    //   52: ifnull -> 75
    //   55: aload #5
    //   57: invokevirtual isEmpty : ()Z
    //   60: ifeq -> 66
    //   63: goto -> 75
    //   66: aload_0
    //   67: aload_1
    //   68: iload_2
    //   69: invokespecial captureHierarchy : (Landroid/view/View;Z)V
    //   72: goto -> 297
    //   75: iconst_0
    //   76: istore_3
    //   77: iload_3
    //   78: aload_0
    //   79: getfield mTargetIds : Ljava/util/ArrayList;
    //   82: invokevirtual size : ()I
    //   85: if_icmpge -> 194
    //   88: aload_1
    //   89: aload_0
    //   90: getfield mTargetIds : Ljava/util/ArrayList;
    //   93: iload_3
    //   94: invokevirtual get : (I)Ljava/lang/Object;
    //   97: checkcast java/lang/Integer
    //   100: invokevirtual intValue : ()I
    //   103: invokevirtual findViewById : (I)Landroid/view/View;
    //   106: astore #5
    //   108: aload #5
    //   110: ifnull -> 188
    //   113: new androidx/transition/TransitionValues
    //   116: dup
    //   117: aload #5
    //   119: invokespecial <init> : (Landroid/view/View;)V
    //   122: astore #6
    //   124: iload_2
    //   125: ifeq -> 137
    //   128: aload_0
    //   129: aload #6
    //   131: invokevirtual captureStartValues : (Landroidx/transition/TransitionValues;)V
    //   134: goto -> 143
    //   137: aload_0
    //   138: aload #6
    //   140: invokevirtual captureEndValues : (Landroidx/transition/TransitionValues;)V
    //   143: aload #6
    //   145: getfield mTargetedTransitions : Ljava/util/ArrayList;
    //   148: aload_0
    //   149: invokevirtual add : (Ljava/lang/Object;)Z
    //   152: pop
    //   153: aload_0
    //   154: aload #6
    //   156: invokevirtual capturePropagationValues : (Landroidx/transition/TransitionValues;)V
    //   159: iload_2
    //   160: ifeq -> 177
    //   163: aload_0
    //   164: getfield mStartValues : Landroidx/transition/TransitionValuesMaps;
    //   167: aload #5
    //   169: aload #6
    //   171: invokestatic addViewValues : (Landroidx/transition/TransitionValuesMaps;Landroid/view/View;Landroidx/transition/TransitionValues;)V
    //   174: goto -> 188
    //   177: aload_0
    //   178: getfield mEndValues : Landroidx/transition/TransitionValuesMaps;
    //   181: aload #5
    //   183: aload #6
    //   185: invokestatic addViewValues : (Landroidx/transition/TransitionValuesMaps;Landroid/view/View;Landroidx/transition/TransitionValues;)V
    //   188: iinc #3, 1
    //   191: goto -> 77
    //   194: iconst_0
    //   195: istore_3
    //   196: iload_3
    //   197: aload_0
    //   198: getfield mTargets : Ljava/util/ArrayList;
    //   201: invokevirtual size : ()I
    //   204: if_icmpge -> 297
    //   207: aload_0
    //   208: getfield mTargets : Ljava/util/ArrayList;
    //   211: iload_3
    //   212: invokevirtual get : (I)Ljava/lang/Object;
    //   215: checkcast android/view/View
    //   218: astore_1
    //   219: new androidx/transition/TransitionValues
    //   222: dup
    //   223: aload_1
    //   224: invokespecial <init> : (Landroid/view/View;)V
    //   227: astore #5
    //   229: iload_2
    //   230: ifeq -> 242
    //   233: aload_0
    //   234: aload #5
    //   236: invokevirtual captureStartValues : (Landroidx/transition/TransitionValues;)V
    //   239: goto -> 248
    //   242: aload_0
    //   243: aload #5
    //   245: invokevirtual captureEndValues : (Landroidx/transition/TransitionValues;)V
    //   248: aload #5
    //   250: getfield mTargetedTransitions : Ljava/util/ArrayList;
    //   253: aload_0
    //   254: invokevirtual add : (Ljava/lang/Object;)Z
    //   257: pop
    //   258: aload_0
    //   259: aload #5
    //   261: invokevirtual capturePropagationValues : (Landroidx/transition/TransitionValues;)V
    //   264: iload_2
    //   265: ifeq -> 281
    //   268: aload_0
    //   269: getfield mStartValues : Landroidx/transition/TransitionValuesMaps;
    //   272: aload_1
    //   273: aload #5
    //   275: invokestatic addViewValues : (Landroidx/transition/TransitionValuesMaps;Landroid/view/View;Landroidx/transition/TransitionValues;)V
    //   278: goto -> 291
    //   281: aload_0
    //   282: getfield mEndValues : Landroidx/transition/TransitionValuesMaps;
    //   285: aload_1
    //   286: aload #5
    //   288: invokestatic addViewValues : (Landroidx/transition/TransitionValuesMaps;Landroid/view/View;Landroidx/transition/TransitionValues;)V
    //   291: iinc #3, 1
    //   294: goto -> 196
    //   297: iload_2
    //   298: ifne -> 427
    //   301: aload_0
    //   302: getfield mNameOverrides : Landroidx/collection/ArrayMap;
    //   305: astore_1
    //   306: aload_1
    //   307: ifnull -> 427
    //   310: aload_1
    //   311: invokevirtual size : ()I
    //   314: istore #4
    //   316: new java/util/ArrayList
    //   319: dup
    //   320: iload #4
    //   322: invokespecial <init> : (I)V
    //   325: astore_1
    //   326: iconst_0
    //   327: istore_3
    //   328: iload_3
    //   329: iload #4
    //   331: if_icmpge -> 370
    //   334: aload_0
    //   335: getfield mNameOverrides : Landroidx/collection/ArrayMap;
    //   338: iload_3
    //   339: invokevirtual keyAt : (I)Ljava/lang/Object;
    //   342: checkcast java/lang/String
    //   345: astore #5
    //   347: aload_1
    //   348: aload_0
    //   349: getfield mStartValues : Landroidx/transition/TransitionValuesMaps;
    //   352: getfield mNameValues : Landroidx/collection/ArrayMap;
    //   355: aload #5
    //   357: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   360: invokevirtual add : (Ljava/lang/Object;)Z
    //   363: pop
    //   364: iinc #3, 1
    //   367: goto -> 328
    //   370: iconst_0
    //   371: istore_3
    //   372: iload_3
    //   373: iload #4
    //   375: if_icmpge -> 427
    //   378: aload_1
    //   379: iload_3
    //   380: invokevirtual get : (I)Ljava/lang/Object;
    //   383: checkcast android/view/View
    //   386: astore #5
    //   388: aload #5
    //   390: ifnull -> 421
    //   393: aload_0
    //   394: getfield mNameOverrides : Landroidx/collection/ArrayMap;
    //   397: iload_3
    //   398: invokevirtual valueAt : (I)Ljava/lang/Object;
    //   401: checkcast java/lang/String
    //   404: astore #6
    //   406: aload_0
    //   407: getfield mStartValues : Landroidx/transition/TransitionValuesMaps;
    //   410: getfield mNameValues : Landroidx/collection/ArrayMap;
    //   413: aload #6
    //   415: aload #5
    //   417: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   420: pop
    //   421: iinc #3, 1
    //   424: goto -> 372
    //   427: return
  }
  
  void clearValues(boolean paramBoolean) {
    if (paramBoolean) {
      this.mStartValues.mViewValues.clear();
      this.mStartValues.mIdValues.clear();
      this.mStartValues.mItemIdValues.clear();
    } else {
      this.mEndValues.mViewValues.clear();
      this.mEndValues.mIdValues.clear();
      this.mEndValues.mItemIdValues.clear();
    } 
  }
  
  public Transition clone() {
    try {
      Transition transition = (Transition)super.clone();
      ArrayList<Animator> arrayList = new ArrayList();
      this();
      transition.mAnimators = arrayList;
      TransitionValuesMaps transitionValuesMaps = new TransitionValuesMaps();
      this();
      transition.mStartValues = transitionValuesMaps;
      transitionValuesMaps = new TransitionValuesMaps();
      this();
      transition.mEndValues = transitionValuesMaps;
      transition.mStartValuesList = null;
      transition.mEndValuesList = null;
      return transition;
    } catch (CloneNotSupportedException cloneNotSupportedException) {
      return null;
    } 
  }
  
  public Animator createAnimator(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    return null;
  }
  
  protected void createAnimators(ViewGroup paramViewGroup, TransitionValuesMaps paramTransitionValuesMaps1, TransitionValuesMaps paramTransitionValuesMaps2, ArrayList<TransitionValues> paramArrayList1, ArrayList<TransitionValues> paramArrayList2) {
    ArrayMap<Animator, AnimationInfo> arrayMap = getRunningAnimators();
    long l = Long.MAX_VALUE;
    SparseIntArray sparseIntArray = new SparseIntArray();
    int i = paramArrayList1.size();
    byte b = 0;
    while (b < i) {
      long l1;
      TransitionValues transitionValues1 = paramArrayList1.get(b);
      TransitionValues transitionValues2 = paramArrayList2.get(b);
      if (transitionValues1 != null && !transitionValues1.mTargetedTransitions.contains(this))
        transitionValues1 = null; 
      if (transitionValues2 != null && !transitionValues2.mTargetedTransitions.contains(this))
        transitionValues2 = null; 
      if (transitionValues1 == null && transitionValues2 == null) {
        l1 = l;
      } else {
        byte b1;
        if (transitionValues1 == null || transitionValues2 == null || isTransitionRequired(transitionValues1, transitionValues2)) {
          b1 = 1;
        } else {
          b1 = 0;
        } 
        if (b1) {
          Animator animator = createAnimator(paramViewGroup, transitionValues1, transitionValues2);
          if (animator != null) {
            TransitionValues transitionValues3;
            View view;
            TransitionValues transitionValues5;
            TransitionValues transitionValues4 = null;
            if (transitionValues2 != null) {
              View view1 = transitionValues2.view;
              String[] arrayOfString = getTransitionProperties();
              if (arrayOfString != null && arrayOfString.length > 0) {
                transitionValues4 = new TransitionValues(view1);
                transitionValues5 = (TransitionValues)paramTransitionValuesMaps2.mViewValues.get(view1);
                if (transitionValues5 != null)
                  for (b1 = 0; b1 < arrayOfString.length; b1++)
                    transitionValues4.values.put(arrayOfString[b1], transitionValues5.values.get(arrayOfString[b1]));  
                int j = arrayMap.size();
                for (b1 = 0; b1 < j; b1++) {
                  AnimationInfo animationInfo = (AnimationInfo)arrayMap.get(arrayMap.keyAt(b1));
                  if (animationInfo.mValues != null && animationInfo.mView == view1 && animationInfo.mName.equals(getName()) && animationInfo.mValues.equals(transitionValues4)) {
                    animator = null;
                    break;
                  } 
                } 
              } 
              TransitionValues transitionValues = transitionValues4;
              view = view1;
              Animator animator1 = animator;
              transitionValues3 = transitionValues;
              b1 = b;
            } else {
              view = transitionValues1.view;
              TransitionValues transitionValues = null;
              transitionValues5 = transitionValues3;
              b1 = b;
              transitionValues3 = transitionValues;
            } 
            l1 = l;
            b = b1;
            if (transitionValues5 != null) {
              TransitionPropagation transitionPropagation = this.mPropagation;
              if (transitionPropagation != null) {
                l1 = transitionPropagation.getStartDelay(paramViewGroup, this, transitionValues1, transitionValues2);
                sparseIntArray.put(this.mAnimators.size(), (int)l1);
                l = Math.min(l1, l);
              } 
              arrayMap.put(transitionValues5, new AnimationInfo(view, getName(), this, ViewUtils.getWindowId((View)paramViewGroup), transitionValues3));
              this.mAnimators.add(transitionValues5);
              l1 = l;
              b = b1;
            } 
          } else {
            l1 = l;
          } 
        } else {
          l1 = l;
        } 
      } 
      b++;
      l = l1;
    } 
    if (sparseIntArray.size() != 0)
      for (b = 0; b < sparseIntArray.size(); b++) {
        int j = sparseIntArray.keyAt(b);
        Animator animator = this.mAnimators.get(j);
        animator.setStartDelay(sparseIntArray.valueAt(b) - l + animator.getStartDelay());
      }  
  }
  
  protected void end() {
    int i = this.mNumInstances - 1;
    this.mNumInstances = i;
    if (i == 0) {
      ArrayList<TransitionListener> arrayList = this.mListeners;
      if (arrayList != null && arrayList.size() > 0) {
        arrayList = (ArrayList<TransitionListener>)this.mListeners.clone();
        int j = arrayList.size();
        for (i = 0; i < j; i++)
          ((TransitionListener)arrayList.get(i)).onTransitionEnd(this); 
      } 
      for (i = 0; i < this.mStartValues.mItemIdValues.size(); i++) {
        View view = (View)this.mStartValues.mItemIdValues.valueAt(i);
        if (view != null)
          ViewCompat.setHasTransientState(view, false); 
      } 
      for (i = 0; i < this.mEndValues.mItemIdValues.size(); i++) {
        View view = (View)this.mEndValues.mItemIdValues.valueAt(i);
        if (view != null)
          ViewCompat.setHasTransientState(view, false); 
      } 
      this.mEnded = true;
    } 
  }
  
  public Transition excludeChildren(int paramInt, boolean paramBoolean) {
    this.mTargetIdChildExcludes = excludeId(this.mTargetIdChildExcludes, paramInt, paramBoolean);
    return this;
  }
  
  public Transition excludeChildren(View paramView, boolean paramBoolean) {
    this.mTargetChildExcludes = excludeView(this.mTargetChildExcludes, paramView, paramBoolean);
    return this;
  }
  
  public Transition excludeChildren(Class<?> paramClass, boolean paramBoolean) {
    this.mTargetTypeChildExcludes = excludeType(this.mTargetTypeChildExcludes, paramClass, paramBoolean);
    return this;
  }
  
  public Transition excludeTarget(int paramInt, boolean paramBoolean) {
    this.mTargetIdExcludes = excludeId(this.mTargetIdExcludes, paramInt, paramBoolean);
    return this;
  }
  
  public Transition excludeTarget(View paramView, boolean paramBoolean) {
    this.mTargetExcludes = excludeView(this.mTargetExcludes, paramView, paramBoolean);
    return this;
  }
  
  public Transition excludeTarget(Class<?> paramClass, boolean paramBoolean) {
    this.mTargetTypeExcludes = excludeType(this.mTargetTypeExcludes, paramClass, paramBoolean);
    return this;
  }
  
  public Transition excludeTarget(String paramString, boolean paramBoolean) {
    this.mTargetNameExcludes = excludeObject(this.mTargetNameExcludes, paramString, paramBoolean);
    return this;
  }
  
  void forceToEnd(ViewGroup paramViewGroup) {
    ArrayMap<Animator, AnimationInfo> arrayMap = getRunningAnimators();
    int i = arrayMap.size();
    if (paramViewGroup == null || i == 0)
      return; 
    WindowIdImpl windowIdImpl = ViewUtils.getWindowId((View)paramViewGroup);
    ArrayMap arrayMap1 = new ArrayMap((SimpleArrayMap)arrayMap);
    arrayMap.clear();
    while (--i >= 0) {
      AnimationInfo animationInfo = (AnimationInfo)arrayMap1.valueAt(i);
      if (animationInfo.mView != null && windowIdImpl != null && windowIdImpl.equals(animationInfo.mWindowId))
        ((Animator)arrayMap1.keyAt(i)).end(); 
      i--;
    } 
  }
  
  public long getDuration() {
    return this.mDuration;
  }
  
  public Rect getEpicenter() {
    EpicenterCallback epicenterCallback = this.mEpicenterCallback;
    return (epicenterCallback == null) ? null : epicenterCallback.onGetEpicenter(this);
  }
  
  public EpicenterCallback getEpicenterCallback() {
    return this.mEpicenterCallback;
  }
  
  public TimeInterpolator getInterpolator() {
    return this.mInterpolator;
  }
  
  TransitionValues getMatchedTransitionValues(View paramView, boolean paramBoolean) {
    TransitionValues transitionValues;
    byte b1;
    ArrayList<TransitionValues> arrayList;
    TransitionSet transitionSet = this.mParent;
    if (transitionSet != null)
      return transitionSet.getMatchedTransitionValues(paramView, paramBoolean); 
    if (paramBoolean) {
      arrayList = this.mStartValuesList;
    } else {
      arrayList = this.mEndValuesList;
    } 
    if (arrayList == null)
      return null; 
    int i = arrayList.size();
    byte b2 = -1;
    byte b = 0;
    while (true) {
      b1 = b2;
      if (b < i) {
        TransitionValues transitionValues1 = arrayList.get(b);
        if (transitionValues1 == null)
          return null; 
        if (transitionValues1.view == paramView) {
          b1 = b;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    paramView = null;
    if (b1 >= 0) {
      ArrayList<TransitionValues> arrayList1;
      if (paramBoolean) {
        arrayList1 = this.mEndValuesList;
      } else {
        arrayList1 = this.mStartValuesList;
      } 
      transitionValues = arrayList1.get(b1);
    } 
    return transitionValues;
  }
  
  public String getName() {
    return this.mName;
  }
  
  public PathMotion getPathMotion() {
    return this.mPathMotion;
  }
  
  public TransitionPropagation getPropagation() {
    return this.mPropagation;
  }
  
  public long getStartDelay() {
    return this.mStartDelay;
  }
  
  public List<Integer> getTargetIds() {
    return this.mTargetIds;
  }
  
  public List<String> getTargetNames() {
    return this.mTargetNames;
  }
  
  public List<Class<?>> getTargetTypes() {
    return this.mTargetTypes;
  }
  
  public List<View> getTargets() {
    return this.mTargets;
  }
  
  public String[] getTransitionProperties() {
    return null;
  }
  
  public TransitionValues getTransitionValues(View paramView, boolean paramBoolean) {
    TransitionValuesMaps transitionValuesMaps;
    TransitionSet transitionSet = this.mParent;
    if (transitionSet != null)
      return transitionSet.getTransitionValues(paramView, paramBoolean); 
    if (paramBoolean) {
      transitionValuesMaps = this.mStartValues;
    } else {
      transitionValuesMaps = this.mEndValues;
    } 
    return (TransitionValues)transitionValuesMaps.mViewValues.get(paramView);
  }
  
  public boolean isTransitionRequired(TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    boolean bool3 = false;
    boolean bool2 = false;
    boolean bool1 = bool3;
    if (paramTransitionValues1 != null) {
      bool1 = bool3;
      if (paramTransitionValues2 != null) {
        String[] arrayOfString = getTransitionProperties();
        if (arrayOfString != null) {
          int i = arrayOfString.length;
          byte b = 0;
          while (true) {
            bool1 = bool2;
            if (b < i) {
              if (isValueChanged(paramTransitionValues1, paramTransitionValues2, arrayOfString[b])) {
                bool1 = true;
                break;
              } 
              b++;
              continue;
            } 
            break;
          } 
        } else {
          Iterator<String> iterator = paramTransitionValues1.values.keySet().iterator();
          while (true) {
            bool1 = bool3;
            if (iterator.hasNext()) {
              if (isValueChanged(paramTransitionValues1, paramTransitionValues2, iterator.next())) {
                bool1 = true;
                break;
              } 
              continue;
            } 
            break;
          } 
        } 
      } 
    } 
    return bool1;
  }
  
  boolean isValidTarget(View paramView) {
    int i = paramView.getId();
    ArrayList<Integer> arrayList3 = this.mTargetIdExcludes;
    if (arrayList3 != null && arrayList3.contains(Integer.valueOf(i)))
      return false; 
    ArrayList<View> arrayList2 = this.mTargetExcludes;
    if (arrayList2 != null && arrayList2.contains(paramView))
      return false; 
    ArrayList<Class<?>> arrayList1 = this.mTargetTypeExcludes;
    if (arrayList1 != null) {
      int j = arrayList1.size();
      for (byte b = 0; b < j; b++) {
        if (((Class)this.mTargetTypeExcludes.get(b)).isInstance(paramView))
          return false; 
      } 
    } 
    if (this.mTargetNameExcludes != null && ViewCompat.getTransitionName(paramView) != null && this.mTargetNameExcludes.contains(ViewCompat.getTransitionName(paramView)))
      return false; 
    if (this.mTargetIds.size() == 0 && this.mTargets.size() == 0) {
      arrayList1 = this.mTargetTypes;
      if (arrayList1 == null || arrayList1.isEmpty()) {
        ArrayList<String> arrayList4 = this.mTargetNames;
        if (arrayList4 == null || arrayList4.isEmpty())
          return true; 
      } 
    } 
    if (this.mTargetIds.contains(Integer.valueOf(i)) || this.mTargets.contains(paramView))
      return true; 
    ArrayList<String> arrayList = this.mTargetNames;
    if (arrayList != null && arrayList.contains(ViewCompat.getTransitionName(paramView)))
      return true; 
    if (this.mTargetTypes != null)
      for (byte b = 0; b < this.mTargetTypes.size(); b++) {
        if (((Class)this.mTargetTypes.get(b)).isInstance(paramView))
          return true; 
      }  
    return false;
  }
  
  public void pause(View paramView) {
    if (!this.mEnded) {
      ArrayMap<Animator, AnimationInfo> arrayMap = getRunningAnimators();
      int i = arrayMap.size();
      WindowIdImpl windowIdImpl = ViewUtils.getWindowId(paramView);
      while (--i >= 0) {
        AnimationInfo animationInfo = (AnimationInfo)arrayMap.valueAt(i);
        if (animationInfo.mView != null && windowIdImpl.equals(animationInfo.mWindowId))
          AnimatorUtils.pause((Animator)arrayMap.keyAt(i)); 
        i--;
      } 
      ArrayList<TransitionListener> arrayList = this.mListeners;
      if (arrayList != null && arrayList.size() > 0) {
        arrayList = (ArrayList<TransitionListener>)this.mListeners.clone();
        int j = arrayList.size();
        for (i = 0; i < j; i++)
          ((TransitionListener)arrayList.get(i)).onTransitionPause(this); 
      } 
      this.mPaused = true;
    } 
  }
  
  void playTransition(ViewGroup paramViewGroup) {
    this.mStartValuesList = new ArrayList<>();
    this.mEndValuesList = new ArrayList<>();
    matchStartAndEnd(this.mStartValues, this.mEndValues);
    ArrayMap<Animator, AnimationInfo> arrayMap = getRunningAnimators();
    int i = arrayMap.size();
    WindowIdImpl windowIdImpl = ViewUtils.getWindowId((View)paramViewGroup);
    while (--i >= 0) {
      Animator animator = (Animator)arrayMap.keyAt(i);
      if (animator != null) {
        AnimationInfo animationInfo = (AnimationInfo)arrayMap.get(animator);
        if (animationInfo != null && animationInfo.mView != null && windowIdImpl.equals(animationInfo.mWindowId)) {
          TransitionValues transitionValues4 = animationInfo.mValues;
          View view = animationInfo.mView;
          boolean bool = true;
          TransitionValues transitionValues3 = getTransitionValues(view, true);
          TransitionValues transitionValues2 = getMatchedTransitionValues(view, true);
          TransitionValues transitionValues1 = transitionValues2;
          if (transitionValues3 == null) {
            transitionValues1 = transitionValues2;
            if (transitionValues2 == null)
              transitionValues1 = (TransitionValues)this.mEndValues.mViewValues.get(view); 
          } 
          if ((transitionValues3 == null && transitionValues1 == null) || !animationInfo.mTransition.isTransitionRequired(transitionValues4, transitionValues1))
            bool = false; 
          if (bool)
            if (animator.isRunning() || animator.isStarted()) {
              animator.cancel();
            } else {
              arrayMap.remove(animator);
            }  
        } 
      } 
      i--;
    } 
    createAnimators(paramViewGroup, this.mStartValues, this.mEndValues, this.mStartValuesList, this.mEndValuesList);
    runAnimators();
  }
  
  public Transition removeListener(TransitionListener paramTransitionListener) {
    ArrayList<TransitionListener> arrayList = this.mListeners;
    if (arrayList == null)
      return this; 
    arrayList.remove(paramTransitionListener);
    if (this.mListeners.size() == 0)
      this.mListeners = null; 
    return this;
  }
  
  public Transition removeTarget(int paramInt) {
    if (paramInt != 0)
      this.mTargetIds.remove(Integer.valueOf(paramInt)); 
    return this;
  }
  
  public Transition removeTarget(View paramView) {
    this.mTargets.remove(paramView);
    return this;
  }
  
  public Transition removeTarget(Class<?> paramClass) {
    ArrayList<Class<?>> arrayList = this.mTargetTypes;
    if (arrayList != null)
      arrayList.remove(paramClass); 
    return this;
  }
  
  public Transition removeTarget(String paramString) {
    ArrayList<String> arrayList = this.mTargetNames;
    if (arrayList != null)
      arrayList.remove(paramString); 
    return this;
  }
  
  public void resume(View paramView) {
    if (this.mPaused) {
      if (!this.mEnded) {
        ArrayMap<Animator, AnimationInfo> arrayMap = getRunningAnimators();
        int i = arrayMap.size();
        WindowIdImpl windowIdImpl = ViewUtils.getWindowId(paramView);
        while (--i >= 0) {
          AnimationInfo animationInfo = (AnimationInfo)arrayMap.valueAt(i);
          if (animationInfo.mView != null && windowIdImpl.equals(animationInfo.mWindowId))
            AnimatorUtils.resume((Animator)arrayMap.keyAt(i)); 
          i--;
        } 
        ArrayList<TransitionListener> arrayList = this.mListeners;
        if (arrayList != null && arrayList.size() > 0) {
          arrayList = (ArrayList<TransitionListener>)this.mListeners.clone();
          int j = arrayList.size();
          for (i = 0; i < j; i++)
            ((TransitionListener)arrayList.get(i)).onTransitionResume(this); 
        } 
      } 
      this.mPaused = false;
    } 
  }
  
  protected void runAnimators() {
    start();
    ArrayMap<Animator, AnimationInfo> arrayMap = getRunningAnimators();
    for (Animator animator : this.mAnimators) {
      if (arrayMap.containsKey(animator)) {
        start();
        runAnimator(animator, arrayMap);
      } 
    } 
    this.mAnimators.clear();
    end();
  }
  
  void setCanRemoveViews(boolean paramBoolean) {
    this.mCanRemoveViews = paramBoolean;
  }
  
  public Transition setDuration(long paramLong) {
    this.mDuration = paramLong;
    return this;
  }
  
  public void setEpicenterCallback(EpicenterCallback paramEpicenterCallback) {
    this.mEpicenterCallback = paramEpicenterCallback;
  }
  
  public Transition setInterpolator(TimeInterpolator paramTimeInterpolator) {
    this.mInterpolator = paramTimeInterpolator;
    return this;
  }
  
  public void setMatchOrder(int... paramVarArgs) {
    if (paramVarArgs == null || paramVarArgs.length == 0) {
      this.mMatchOrder = DEFAULT_MATCH_ORDER;
      return;
    } 
    byte b = 0;
    while (b < paramVarArgs.length) {
      if (isValidMatch(paramVarArgs[b])) {
        if (!alreadyContains(paramVarArgs, b)) {
          b++;
          continue;
        } 
        throw new IllegalArgumentException("matches contains a duplicate value");
      } 
      throw new IllegalArgumentException("matches contains invalid value");
    } 
    this.mMatchOrder = (int[])paramVarArgs.clone();
  }
  
  public void setPathMotion(PathMotion paramPathMotion) {
    if (paramPathMotion == null) {
      this.mPathMotion = STRAIGHT_PATH_MOTION;
    } else {
      this.mPathMotion = paramPathMotion;
    } 
  }
  
  public void setPropagation(TransitionPropagation paramTransitionPropagation) {
    this.mPropagation = paramTransitionPropagation;
  }
  
  Transition setSceneRoot(ViewGroup paramViewGroup) {
    this.mSceneRoot = paramViewGroup;
    return this;
  }
  
  public Transition setStartDelay(long paramLong) {
    this.mStartDelay = paramLong;
    return this;
  }
  
  protected void start() {
    if (this.mNumInstances == 0) {
      ArrayList<TransitionListener> arrayList = this.mListeners;
      if (arrayList != null && arrayList.size() > 0) {
        arrayList = (ArrayList<TransitionListener>)this.mListeners.clone();
        int i = arrayList.size();
        for (byte b = 0; b < i; b++)
          ((TransitionListener)arrayList.get(b)).onTransitionStart(this); 
      } 
      this.mEnded = false;
    } 
    this.mNumInstances++;
  }
  
  public String toString() {
    return toString("");
  }
  
  String toString(String paramString) {
    // Byte code:
    //   0: new java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: aload_1
    //   8: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   11: aload_0
    //   12: invokevirtual getClass : ()Ljava/lang/Class;
    //   15: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   18: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   21: ldc_w '@'
    //   24: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   27: aload_0
    //   28: invokevirtual hashCode : ()I
    //   31: invokestatic toHexString : (I)Ljava/lang/String;
    //   34: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   37: ldc_w ': '
    //   40: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   43: invokevirtual toString : ()Ljava/lang/String;
    //   46: astore_3
    //   47: aload_3
    //   48: astore_1
    //   49: aload_0
    //   50: getfield mDuration : J
    //   53: ldc2_w -1
    //   56: lcmp
    //   57: ifeq -> 94
    //   60: new java/lang/StringBuilder
    //   63: dup
    //   64: invokespecial <init> : ()V
    //   67: aload_3
    //   68: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   71: ldc_w 'dur('
    //   74: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   77: aload_0
    //   78: getfield mDuration : J
    //   81: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   84: ldc_w ') '
    //   87: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   90: invokevirtual toString : ()Ljava/lang/String;
    //   93: astore_1
    //   94: aload_1
    //   95: astore_3
    //   96: aload_0
    //   97: getfield mStartDelay : J
    //   100: ldc2_w -1
    //   103: lcmp
    //   104: ifeq -> 141
    //   107: new java/lang/StringBuilder
    //   110: dup
    //   111: invokespecial <init> : ()V
    //   114: aload_1
    //   115: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: ldc_w 'dly('
    //   121: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   124: aload_0
    //   125: getfield mStartDelay : J
    //   128: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   131: ldc_w ') '
    //   134: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   137: invokevirtual toString : ()Ljava/lang/String;
    //   140: astore_3
    //   141: aload_3
    //   142: astore_1
    //   143: aload_0
    //   144: getfield mInterpolator : Landroid/animation/TimeInterpolator;
    //   147: ifnull -> 184
    //   150: new java/lang/StringBuilder
    //   153: dup
    //   154: invokespecial <init> : ()V
    //   157: aload_3
    //   158: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   161: ldc_w 'interp('
    //   164: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   167: aload_0
    //   168: getfield mInterpolator : Landroid/animation/TimeInterpolator;
    //   171: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   174: ldc_w ') '
    //   177: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   180: invokevirtual toString : ()Ljava/lang/String;
    //   183: astore_1
    //   184: aload_0
    //   185: getfield mTargetIds : Ljava/util/ArrayList;
    //   188: invokevirtual size : ()I
    //   191: ifgt -> 206
    //   194: aload_1
    //   195: astore_3
    //   196: aload_0
    //   197: getfield mTargets : Ljava/util/ArrayList;
    //   200: invokevirtual size : ()I
    //   203: ifle -> 420
    //   206: new java/lang/StringBuilder
    //   209: dup
    //   210: invokespecial <init> : ()V
    //   213: aload_1
    //   214: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   217: ldc_w 'tgts('
    //   220: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   223: invokevirtual toString : ()Ljava/lang/String;
    //   226: astore_3
    //   227: aload_3
    //   228: astore_1
    //   229: aload_0
    //   230: getfield mTargetIds : Ljava/util/ArrayList;
    //   233: invokevirtual size : ()I
    //   236: ifle -> 313
    //   239: iconst_0
    //   240: istore_2
    //   241: aload_3
    //   242: astore_1
    //   243: iload_2
    //   244: aload_0
    //   245: getfield mTargetIds : Ljava/util/ArrayList;
    //   248: invokevirtual size : ()I
    //   251: if_icmpge -> 313
    //   254: aload_3
    //   255: astore_1
    //   256: iload_2
    //   257: ifle -> 281
    //   260: new java/lang/StringBuilder
    //   263: dup
    //   264: invokespecial <init> : ()V
    //   267: aload_3
    //   268: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   271: ldc_w ', '
    //   274: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   277: invokevirtual toString : ()Ljava/lang/String;
    //   280: astore_1
    //   281: new java/lang/StringBuilder
    //   284: dup
    //   285: invokespecial <init> : ()V
    //   288: aload_1
    //   289: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   292: aload_0
    //   293: getfield mTargetIds : Ljava/util/ArrayList;
    //   296: iload_2
    //   297: invokevirtual get : (I)Ljava/lang/Object;
    //   300: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   303: invokevirtual toString : ()Ljava/lang/String;
    //   306: astore_3
    //   307: iinc #2, 1
    //   310: goto -> 241
    //   313: aload_1
    //   314: astore_3
    //   315: aload_0
    //   316: getfield mTargets : Ljava/util/ArrayList;
    //   319: invokevirtual size : ()I
    //   322: ifle -> 399
    //   325: iconst_0
    //   326: istore_2
    //   327: aload_1
    //   328: astore_3
    //   329: iload_2
    //   330: aload_0
    //   331: getfield mTargets : Ljava/util/ArrayList;
    //   334: invokevirtual size : ()I
    //   337: if_icmpge -> 399
    //   340: aload_1
    //   341: astore_3
    //   342: iload_2
    //   343: ifle -> 367
    //   346: new java/lang/StringBuilder
    //   349: dup
    //   350: invokespecial <init> : ()V
    //   353: aload_1
    //   354: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   357: ldc_w ', '
    //   360: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   363: invokevirtual toString : ()Ljava/lang/String;
    //   366: astore_3
    //   367: new java/lang/StringBuilder
    //   370: dup
    //   371: invokespecial <init> : ()V
    //   374: aload_3
    //   375: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   378: aload_0
    //   379: getfield mTargets : Ljava/util/ArrayList;
    //   382: iload_2
    //   383: invokevirtual get : (I)Ljava/lang/Object;
    //   386: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   389: invokevirtual toString : ()Ljava/lang/String;
    //   392: astore_1
    //   393: iinc #2, 1
    //   396: goto -> 327
    //   399: new java/lang/StringBuilder
    //   402: dup
    //   403: invokespecial <init> : ()V
    //   406: aload_3
    //   407: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   410: ldc_w ')'
    //   413: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   416: invokevirtual toString : ()Ljava/lang/String;
    //   419: astore_3
    //   420: aload_3
    //   421: areturn
  }
  
  private static class AnimationInfo {
    String mName;
    
    Transition mTransition;
    
    TransitionValues mValues;
    
    View mView;
    
    WindowIdImpl mWindowId;
    
    AnimationInfo(View param1View, String param1String, Transition param1Transition, WindowIdImpl param1WindowIdImpl, TransitionValues param1TransitionValues) {
      this.mView = param1View;
      this.mName = param1String;
      this.mValues = param1TransitionValues;
      this.mWindowId = param1WindowIdImpl;
      this.mTransition = param1Transition;
    }
  }
  
  private static class ArrayListManager {
    static <T> ArrayList<T> add(ArrayList<T> param1ArrayList, T param1T) {
      ArrayList<T> arrayList = param1ArrayList;
      if (param1ArrayList == null)
        arrayList = new ArrayList<>(); 
      if (!arrayList.contains(param1T))
        arrayList.add(param1T); 
      return arrayList;
    }
    
    static <T> ArrayList<T> remove(ArrayList<T> param1ArrayList, T param1T) {
      ArrayList<T> arrayList = param1ArrayList;
      if (param1ArrayList != null) {
        param1ArrayList.remove(param1T);
        arrayList = param1ArrayList;
        if (param1ArrayList.isEmpty())
          arrayList = null; 
      } 
      return arrayList;
    }
  }
  
  public static abstract class EpicenterCallback {
    public abstract Rect onGetEpicenter(Transition param1Transition);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface MatchOrder {}
  
  public static interface TransitionListener {
    void onTransitionCancel(Transition param1Transition);
    
    void onTransitionEnd(Transition param1Transition);
    
    void onTransitionPause(Transition param1Transition);
    
    void onTransitionResume(Transition param1Transition);
    
    void onTransitionStart(Transition param1Transition);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\Transition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */