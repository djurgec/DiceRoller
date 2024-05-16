package androidx.fragment.app;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.collection.ArrayMap;
import androidx.core.app.SharedElementCallback;
import androidx.core.os.CancellationSignal;
import androidx.core.view.OneShotPreDrawListener;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

class FragmentTransition {
  private static final int[] INVERSE_OPS = new int[] { 
      0, 3, 0, 1, 5, 4, 7, 6, 9, 8, 
      10 };
  
  static final FragmentTransitionImpl PLATFORM_IMPL;
  
  static final FragmentTransitionImpl SUPPORT_IMPL = resolveSupportImpl();
  
  private static void addSharedElementsWithMatchingNames(ArrayList<View> paramArrayList, ArrayMap<String, View> paramArrayMap, Collection<String> paramCollection) {
    for (int i = paramArrayMap.size() - 1; i >= 0; i--) {
      View view = (View)paramArrayMap.valueAt(i);
      if (paramCollection.contains(ViewCompat.getTransitionName(view)))
        paramArrayList.add(view); 
    } 
  }
  
  private static void addToFirstInLastOut(BackStackRecord paramBackStackRecord, FragmentTransaction.Op paramOp, SparseArray<FragmentContainerTransition> paramSparseArray, boolean paramBoolean1, boolean paramBoolean2) {
    // Byte code:
    //   0: aload_1
    //   1: getfield mFragment : Landroidx/fragment/app/Fragment;
    //   4: astore #18
    //   6: aload #18
    //   8: ifnonnull -> 12
    //   11: return
    //   12: aload #18
    //   14: getfield mContainerId : I
    //   17: istore #13
    //   19: iload #13
    //   21: ifne -> 25
    //   24: return
    //   25: iload_3
    //   26: ifeq -> 42
    //   29: getstatic androidx/fragment/app/FragmentTransition.INVERSE_OPS : [I
    //   32: aload_1
    //   33: getfield mCmd : I
    //   36: iaload
    //   37: istore #5
    //   39: goto -> 48
    //   42: aload_1
    //   43: getfield mCmd : I
    //   46: istore #5
    //   48: iconst_0
    //   49: istore #15
    //   51: iconst_0
    //   52: istore #8
    //   54: iconst_0
    //   55: istore #6
    //   57: iconst_0
    //   58: istore #7
    //   60: iconst_1
    //   61: istore #12
    //   63: iconst_1
    //   64: istore #9
    //   66: iconst_1
    //   67: istore #11
    //   69: iconst_1
    //   70: istore #10
    //   72: iconst_1
    //   73: istore #14
    //   75: iconst_1
    //   76: istore #16
    //   78: iload #5
    //   80: tableswitch default -> 124, 1 -> 379, 2 -> 124, 3 -> 280, 4 -> 194, 5 -> 135, 6 -> 280, 7 -> 379
    //   124: iload #15
    //   126: istore #14
    //   128: iload #8
    //   130: istore #5
    //   132: goto -> 423
    //   135: iload #4
    //   137: ifeq -> 177
    //   140: aload #18
    //   142: getfield mHiddenChanged : Z
    //   145: ifeq -> 171
    //   148: aload #18
    //   150: getfield mHidden : Z
    //   153: ifne -> 171
    //   156: aload #18
    //   158: getfield mAdded : Z
    //   161: ifeq -> 171
    //   164: iload #16
    //   166: istore #14
    //   168: goto -> 174
    //   171: iconst_0
    //   172: istore #14
    //   174: goto -> 184
    //   177: aload #18
    //   179: getfield mHidden : Z
    //   182: istore #14
    //   184: iconst_1
    //   185: istore #7
    //   187: iload #8
    //   189: istore #5
    //   191: goto -> 423
    //   194: iload #4
    //   196: ifeq -> 240
    //   199: aload #18
    //   201: getfield mHiddenChanged : Z
    //   204: ifeq -> 230
    //   207: aload #18
    //   209: getfield mAdded : Z
    //   212: ifeq -> 230
    //   215: aload #18
    //   217: getfield mHidden : Z
    //   220: ifeq -> 230
    //   223: iload #12
    //   225: istore #5
    //   227: goto -> 233
    //   230: iconst_0
    //   231: istore #5
    //   233: iload #5
    //   235: istore #6
    //   237: goto -> 270
    //   240: aload #18
    //   242: getfield mAdded : Z
    //   245: ifeq -> 263
    //   248: aload #18
    //   250: getfield mHidden : Z
    //   253: ifne -> 263
    //   256: iload #9
    //   258: istore #5
    //   260: goto -> 266
    //   263: iconst_0
    //   264: istore #5
    //   266: iload #5
    //   268: istore #6
    //   270: iconst_1
    //   271: istore #5
    //   273: iload #15
    //   275: istore #14
    //   277: goto -> 423
    //   280: iload #4
    //   282: ifeq -> 339
    //   285: aload #18
    //   287: getfield mAdded : Z
    //   290: ifne -> 329
    //   293: aload #18
    //   295: getfield mView : Landroid/view/View;
    //   298: ifnull -> 329
    //   301: aload #18
    //   303: getfield mView : Landroid/view/View;
    //   306: invokevirtual getVisibility : ()I
    //   309: ifne -> 329
    //   312: aload #18
    //   314: getfield mPostponedAlpha : F
    //   317: fconst_0
    //   318: fcmpl
    //   319: iflt -> 329
    //   322: iload #11
    //   324: istore #5
    //   326: goto -> 332
    //   329: iconst_0
    //   330: istore #5
    //   332: iload #5
    //   334: istore #6
    //   336: goto -> 369
    //   339: aload #18
    //   341: getfield mAdded : Z
    //   344: ifeq -> 362
    //   347: aload #18
    //   349: getfield mHidden : Z
    //   352: ifne -> 362
    //   355: iload #10
    //   357: istore #5
    //   359: goto -> 365
    //   362: iconst_0
    //   363: istore #5
    //   365: iload #5
    //   367: istore #6
    //   369: iconst_1
    //   370: istore #5
    //   372: iload #15
    //   374: istore #14
    //   376: goto -> 423
    //   379: iload #4
    //   381: ifeq -> 394
    //   384: aload #18
    //   386: getfield mIsNewlyAdded : Z
    //   389: istore #14
    //   391: goto -> 416
    //   394: aload #18
    //   396: getfield mAdded : Z
    //   399: ifne -> 413
    //   402: aload #18
    //   404: getfield mHidden : Z
    //   407: ifne -> 413
    //   410: goto -> 416
    //   413: iconst_0
    //   414: istore #14
    //   416: iconst_1
    //   417: istore #7
    //   419: iload #8
    //   421: istore #5
    //   423: aload_2
    //   424: iload #13
    //   426: invokevirtual get : (I)Ljava/lang/Object;
    //   429: checkcast androidx/fragment/app/FragmentTransition$FragmentContainerTransition
    //   432: astore #17
    //   434: aload #17
    //   436: astore_1
    //   437: iload #14
    //   439: ifeq -> 467
    //   442: aload #17
    //   444: aload_2
    //   445: iload #13
    //   447: invokestatic ensureContainer : (Landroidx/fragment/app/FragmentTransition$FragmentContainerTransition;Landroid/util/SparseArray;I)Landroidx/fragment/app/FragmentTransition$FragmentContainerTransition;
    //   450: astore_1
    //   451: aload_1
    //   452: aload #18
    //   454: putfield lastIn : Landroidx/fragment/app/Fragment;
    //   457: aload_1
    //   458: iload_3
    //   459: putfield lastInIsPop : Z
    //   462: aload_1
    //   463: aload_0
    //   464: putfield lastInTransaction : Landroidx/fragment/app/BackStackRecord;
    //   467: iload #4
    //   469: ifne -> 534
    //   472: iload #7
    //   474: ifeq -> 534
    //   477: aload_1
    //   478: ifnull -> 495
    //   481: aload_1
    //   482: getfield firstOut : Landroidx/fragment/app/Fragment;
    //   485: aload #18
    //   487: if_acmpne -> 495
    //   490: aload_1
    //   491: aconst_null
    //   492: putfield firstOut : Landroidx/fragment/app/Fragment;
    //   495: aload_0
    //   496: getfield mReorderingAllowed : Z
    //   499: ifne -> 534
    //   502: aload_0
    //   503: getfield mManager : Landroidx/fragment/app/FragmentManager;
    //   506: astore #19
    //   508: aload #19
    //   510: aload #18
    //   512: invokevirtual createOrGetFragmentStateManager : (Landroidx/fragment/app/Fragment;)Landroidx/fragment/app/FragmentStateManager;
    //   515: astore #17
    //   517: aload #19
    //   519: invokevirtual getFragmentStore : ()Landroidx/fragment/app/FragmentStore;
    //   522: aload #17
    //   524: invokevirtual makeActive : (Landroidx/fragment/app/FragmentStateManager;)V
    //   527: aload #19
    //   529: aload #18
    //   531: invokevirtual moveToState : (Landroidx/fragment/app/Fragment;)V
    //   534: aload_1
    //   535: astore #17
    //   537: iload #6
    //   539: ifeq -> 584
    //   542: aload_1
    //   543: ifnull -> 556
    //   546: aload_1
    //   547: astore #17
    //   549: aload_1
    //   550: getfield firstOut : Landroidx/fragment/app/Fragment;
    //   553: ifnonnull -> 584
    //   556: aload_1
    //   557: aload_2
    //   558: iload #13
    //   560: invokestatic ensureContainer : (Landroidx/fragment/app/FragmentTransition$FragmentContainerTransition;Landroid/util/SparseArray;I)Landroidx/fragment/app/FragmentTransition$FragmentContainerTransition;
    //   563: astore #17
    //   565: aload #17
    //   567: aload #18
    //   569: putfield firstOut : Landroidx/fragment/app/Fragment;
    //   572: aload #17
    //   574: iload_3
    //   575: putfield firstOutIsPop : Z
    //   578: aload #17
    //   580: aload_0
    //   581: putfield firstOutTransaction : Landroidx/fragment/app/BackStackRecord;
    //   584: iload #4
    //   586: ifne -> 615
    //   589: iload #5
    //   591: ifeq -> 615
    //   594: aload #17
    //   596: ifnull -> 615
    //   599: aload #17
    //   601: getfield lastIn : Landroidx/fragment/app/Fragment;
    //   604: aload #18
    //   606: if_acmpne -> 615
    //   609: aload #17
    //   611: aconst_null
    //   612: putfield lastIn : Landroidx/fragment/app/Fragment;
    //   615: return
  }
  
  public static void calculateFragments(BackStackRecord paramBackStackRecord, SparseArray<FragmentContainerTransition> paramSparseArray, boolean paramBoolean) {
    int i = paramBackStackRecord.mOps.size();
    for (byte b = 0; b < i; b++)
      addToFirstInLastOut(paramBackStackRecord, paramBackStackRecord.mOps.get(b), paramSparseArray, false, paramBoolean); 
  }
  
  private static ArrayMap<String, String> calculateNameOverrides(int paramInt1, ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt2, int paramInt3) {
    ArrayMap<String, String> arrayMap = new ArrayMap();
    while (--paramInt3 >= paramInt2) {
      BackStackRecord backStackRecord = paramArrayList.get(paramInt3);
      if (backStackRecord.interactsWith(paramInt1)) {
        boolean bool = ((Boolean)paramArrayList1.get(paramInt3)).booleanValue();
        if (backStackRecord.mSharedElementSourceNames != null) {
          ArrayList<String> arrayList1;
          ArrayList<String> arrayList2;
          int i = backStackRecord.mSharedElementSourceNames.size();
          if (bool) {
            arrayList1 = backStackRecord.mSharedElementSourceNames;
            arrayList2 = backStackRecord.mSharedElementTargetNames;
          } else {
            arrayList2 = backStackRecord.mSharedElementSourceNames;
            arrayList1 = backStackRecord.mSharedElementTargetNames;
          } 
          for (byte b = 0; b < i; b++) {
            String str1 = arrayList2.get(b);
            String str2 = arrayList1.get(b);
            String str3 = (String)arrayMap.remove(str2);
            if (str3 != null) {
              arrayMap.put(str1, str3);
            } else {
              arrayMap.put(str1, str2);
            } 
          } 
        } 
      } 
      paramInt3--;
    } 
    return arrayMap;
  }
  
  public static void calculatePopFragments(BackStackRecord paramBackStackRecord, SparseArray<FragmentContainerTransition> paramSparseArray, boolean paramBoolean) {
    if (!paramBackStackRecord.mManager.getContainer().onHasView())
      return; 
    for (int i = paramBackStackRecord.mOps.size() - 1; i >= 0; i--)
      addToFirstInLastOut(paramBackStackRecord, paramBackStackRecord.mOps.get(i), paramSparseArray, true, paramBoolean); 
  }
  
  static void callSharedElementStartEnd(Fragment paramFragment1, Fragment paramFragment2, boolean paramBoolean1, ArrayMap<String, View> paramArrayMap, boolean paramBoolean2) {
    SharedElementCallback sharedElementCallback;
    if (paramBoolean1) {
      sharedElementCallback = paramFragment2.getEnterTransitionCallback();
    } else {
      sharedElementCallback = sharedElementCallback.getEnterTransitionCallback();
    } 
    if (sharedElementCallback != null) {
      int i;
      ArrayList<Object> arrayList1 = new ArrayList();
      ArrayList<Object> arrayList2 = new ArrayList();
      if (paramArrayMap == null) {
        i = 0;
      } else {
        i = paramArrayMap.size();
      } 
      for (byte b = 0; b < i; b++) {
        arrayList2.add(paramArrayMap.keyAt(b));
        arrayList1.add(paramArrayMap.valueAt(b));
      } 
      if (paramBoolean2) {
        sharedElementCallback.onSharedElementStart(arrayList2, arrayList1, null);
      } else {
        sharedElementCallback.onSharedElementEnd(arrayList2, arrayList1, null);
      } 
    } 
  }
  
  private static boolean canHandleAll(FragmentTransitionImpl paramFragmentTransitionImpl, List<Object> paramList) {
    byte b = 0;
    int i = paramList.size();
    while (b < i) {
      if (!paramFragmentTransitionImpl.canHandle(paramList.get(b)))
        return false; 
      b++;
    } 
    return true;
  }
  
  static ArrayMap<String, View> captureInSharedElements(FragmentTransitionImpl paramFragmentTransitionImpl, ArrayMap<String, String> paramArrayMap, Object paramObject, FragmentContainerTransition paramFragmentContainerTransition) {
    ArrayList<String> arrayList;
    Fragment fragment = paramFragmentContainerTransition.lastIn;
    View view = fragment.getView();
    if (paramArrayMap.isEmpty() || paramObject == null || view == null) {
      paramArrayMap.clear();
      return null;
    } 
    ArrayMap<String, View> arrayMap = new ArrayMap();
    paramFragmentTransitionImpl.findNamedViews((Map<String, View>)arrayMap, view);
    BackStackRecord backStackRecord = paramFragmentContainerTransition.lastInTransaction;
    if (paramFragmentContainerTransition.lastInIsPop) {
      paramObject = fragment.getExitTransitionCallback();
      arrayList = backStackRecord.mSharedElementSourceNames;
    } else {
      paramObject = fragment.getEnterTransitionCallback();
      arrayList = ((BackStackRecord)arrayList).mSharedElementTargetNames;
    } 
    if (arrayList != null) {
      arrayMap.retainAll(arrayList);
      arrayMap.retainAll(paramArrayMap.values());
    } 
    if (paramObject != null) {
      paramObject.onMapSharedElements(arrayList, (Map)arrayMap);
      for (int i = arrayList.size() - 1; i >= 0; i--) {
        String str = arrayList.get(i);
        paramObject = arrayMap.get(str);
        if (paramObject == null) {
          paramObject = findKeyForValue(paramArrayMap, str);
          if (paramObject != null)
            paramArrayMap.remove(paramObject); 
        } else if (!str.equals(ViewCompat.getTransitionName((View)paramObject))) {
          str = findKeyForValue(paramArrayMap, str);
          if (str != null)
            paramArrayMap.put(str, ViewCompat.getTransitionName((View)paramObject)); 
        } 
      } 
    } else {
      retainValues(paramArrayMap, arrayMap);
    } 
    return arrayMap;
  }
  
  private static ArrayMap<String, View> captureOutSharedElements(FragmentTransitionImpl paramFragmentTransitionImpl, ArrayMap<String, String> paramArrayMap, Object paramObject, FragmentContainerTransition paramFragmentContainerTransition) {
    ArrayList<String> arrayList;
    if (paramArrayMap.isEmpty() || paramObject == null) {
      paramArrayMap.clear();
      return null;
    } 
    paramObject = paramFragmentContainerTransition.firstOut;
    ArrayMap<String, View> arrayMap = new ArrayMap();
    paramFragmentTransitionImpl.findNamedViews((Map<String, View>)arrayMap, paramObject.requireView());
    BackStackRecord backStackRecord = paramFragmentContainerTransition.firstOutTransaction;
    if (paramFragmentContainerTransition.firstOutIsPop) {
      paramObject = paramObject.getEnterTransitionCallback();
      arrayList = backStackRecord.mSharedElementTargetNames;
    } else {
      paramObject = paramObject.getExitTransitionCallback();
      arrayList = ((BackStackRecord)arrayList).mSharedElementSourceNames;
    } 
    if (arrayList != null)
      arrayMap.retainAll(arrayList); 
    if (paramObject != null) {
      paramObject.onMapSharedElements(arrayList, (Map)arrayMap);
      for (int i = arrayList.size() - 1; i >= 0; i--) {
        String str = arrayList.get(i);
        paramObject = arrayMap.get(str);
        if (paramObject == null) {
          paramArrayMap.remove(str);
        } else if (!str.equals(ViewCompat.getTransitionName((View)paramObject))) {
          str = (String)paramArrayMap.remove(str);
          paramArrayMap.put(ViewCompat.getTransitionName((View)paramObject), str);
        } 
      } 
    } else {
      paramArrayMap.retainAll(arrayMap.keySet());
    } 
    return arrayMap;
  }
  
  private static FragmentTransitionImpl chooseImpl(Fragment paramFragment1, Fragment paramFragment2) {
    ArrayList<Object> arrayList = new ArrayList();
    if (paramFragment1 != null) {
      Object object2 = paramFragment1.getExitTransition();
      if (object2 != null)
        arrayList.add(object2); 
      object2 = paramFragment1.getReturnTransition();
      if (object2 != null)
        arrayList.add(object2); 
      Object object1 = paramFragment1.getSharedElementReturnTransition();
      if (object1 != null)
        arrayList.add(object1); 
    } 
    if (paramFragment2 != null) {
      Object object = paramFragment2.getEnterTransition();
      if (object != null)
        arrayList.add(object); 
      object = paramFragment2.getReenterTransition();
      if (object != null)
        arrayList.add(object); 
      object = paramFragment2.getSharedElementEnterTransition();
      if (object != null)
        arrayList.add(object); 
    } 
    if (arrayList.isEmpty())
      return null; 
    FragmentTransitionImpl fragmentTransitionImpl2 = PLATFORM_IMPL;
    if (fragmentTransitionImpl2 != null && canHandleAll(fragmentTransitionImpl2, arrayList))
      return fragmentTransitionImpl2; 
    FragmentTransitionImpl fragmentTransitionImpl1 = SUPPORT_IMPL;
    if (fragmentTransitionImpl1 != null && canHandleAll(fragmentTransitionImpl1, arrayList))
      return fragmentTransitionImpl1; 
    if (fragmentTransitionImpl2 == null && fragmentTransitionImpl1 == null)
      return null; 
    throw new IllegalArgumentException("Invalid Transition types");
  }
  
  static ArrayList<View> configureEnteringExitingViews(FragmentTransitionImpl paramFragmentTransitionImpl, Object paramObject, Fragment paramFragment, ArrayList<View> paramArrayList, View paramView) {
    ArrayList<View> arrayList = null;
    if (paramObject != null) {
      ArrayList<View> arrayList1 = new ArrayList();
      View view = paramFragment.getView();
      if (view != null)
        paramFragmentTransitionImpl.captureTransitioningViews(arrayList1, view); 
      if (paramArrayList != null)
        arrayList1.removeAll(paramArrayList); 
      arrayList = arrayList1;
      if (!arrayList1.isEmpty()) {
        arrayList1.add(paramView);
        paramFragmentTransitionImpl.addTargets(paramObject, arrayList1);
        arrayList = arrayList1;
      } 
    } 
    return arrayList;
  }
  
  private static Object configureSharedElementsOrdered(final FragmentTransitionImpl impl, ViewGroup paramViewGroup, final View nonExistentView, final ArrayMap<String, String> nameOverrides, final FragmentContainerTransition fragments, final ArrayList<View> sharedElementsOut, final ArrayList<View> sharedElementsIn, final Object enterTransition, final Object inEpicenter) {
    final Object finalSharedElementTransition;
    final Fragment inFragment = fragments.lastIn;
    final Fragment outFragment = fragments.firstOut;
    if (fragment1 == null || fragment2 == null)
      return null; 
    final boolean inIsPop = fragments.lastInIsPop;
    if (nameOverrides.isEmpty()) {
      object = null;
    } else {
      object = getSharedElementTransition(impl, fragment1, fragment2, bool);
    } 
    ArrayMap<String, View> arrayMap = captureOutSharedElements(impl, nameOverrides, object, fragments);
    if (nameOverrides.isEmpty()) {
      object = null;
    } else {
      sharedElementsOut.addAll(arrayMap.values());
    } 
    if (enterTransition == null && inEpicenter == null && object == null)
      return null; 
    callSharedElementStartEnd(fragment1, fragment2, bool, arrayMap, true);
    if (object != null) {
      Rect rect = new Rect();
      impl.setSharedElementTargets(object, nonExistentView, sharedElementsOut);
      setOutEpicenter(impl, object, inEpicenter, arrayMap, fragments.firstOutIsPop, fragments.firstOutTransaction);
      if (enterTransition != null)
        impl.setEpicenter(enterTransition, rect); 
      inEpicenter = rect;
    } else {
      inEpicenter = null;
    } 
    OneShotPreDrawListener.add((View)paramViewGroup, new Runnable() {
          final Object val$enterTransition;
          
          final Object val$finalSharedElementTransition;
          
          final FragmentTransition.FragmentContainerTransition val$fragments;
          
          final FragmentTransitionImpl val$impl;
          
          final Rect val$inEpicenter;
          
          final Fragment val$inFragment;
          
          final boolean val$inIsPop;
          
          final ArrayMap val$nameOverrides;
          
          final View val$nonExistentView;
          
          final Fragment val$outFragment;
          
          final ArrayList val$sharedElementsIn;
          
          final ArrayList val$sharedElementsOut;
          
          public void run() {
            ArrayMap<String, View> arrayMap = FragmentTransition.captureInSharedElements(impl, nameOverrides, finalSharedElementTransition, fragments);
            if (arrayMap != null) {
              sharedElementsIn.addAll(arrayMap.values());
              sharedElementsIn.add(nonExistentView);
            } 
            FragmentTransition.callSharedElementStartEnd(inFragment, outFragment, inIsPop, arrayMap, false);
            Object object = finalSharedElementTransition;
            if (object != null) {
              impl.swapSharedElementTargets(object, sharedElementsOut, sharedElementsIn);
              View view = FragmentTransition.getInEpicenterView(arrayMap, fragments, enterTransition, inIsPop);
              if (view != null)
                impl.getBoundsOnScreen(view, inEpicenter); 
            } 
          }
        });
    return object;
  }
  
  private static Object configureSharedElementsReordered(final FragmentTransitionImpl impl, ViewGroup paramViewGroup, final View epicenterView, ArrayMap<String, String> paramArrayMap, final FragmentContainerTransition epicenter, ArrayList<View> paramArrayList1, ArrayList<View> paramArrayList2, Object paramObject1, Object paramObject2) {
    Object object1;
    Object object2;
    final Fragment inFragment = epicenter.lastIn;
    final Fragment outFragment = epicenter.firstOut;
    if (fragment1 != null)
      fragment1.requireView().setVisibility(0); 
    if (fragment1 == null || fragment2 == null)
      return null; 
    final boolean inIsPop = epicenter.lastInIsPop;
    if (paramArrayMap.isEmpty()) {
      object2 = null;
    } else {
      object2 = getSharedElementTransition(impl, fragment1, fragment2, bool);
    } 
    ArrayMap<String, View> arrayMap2 = captureOutSharedElements(impl, paramArrayMap, object2, epicenter);
    final ArrayMap<String, View> inSharedElements = captureInSharedElements(impl, paramArrayMap, object2, epicenter);
    if (paramArrayMap.isEmpty()) {
      if (arrayMap2 != null)
        arrayMap2.clear(); 
      if (arrayMap1 != null)
        arrayMap1.clear(); 
      paramArrayMap = null;
    } else {
      addSharedElementsWithMatchingNames(paramArrayList1, arrayMap2, paramArrayMap.keySet());
      addSharedElementsWithMatchingNames(paramArrayList2, arrayMap1, paramArrayMap.values());
      object1 = object2;
    } 
    if (paramObject1 == null && paramObject2 == null && object1 == null)
      return null; 
    callSharedElementStartEnd(fragment1, fragment2, bool, arrayMap2, true);
    if (object1 != null) {
      paramArrayList2.add(epicenterView);
      impl.setSharedElementTargets(object1, epicenterView, paramArrayList1);
      setOutEpicenter(impl, object1, paramObject2, arrayMap2, epicenter.firstOutIsPop, epicenter.firstOutTransaction);
      Rect rect1 = new Rect();
      View view2 = getInEpicenterView(arrayMap1, epicenter, paramObject1, bool);
      if (view2 != null)
        impl.setEpicenter(paramObject1, rect1); 
      Rect rect2 = rect1;
      View view1 = view2;
    } else {
      epicenter = null;
      epicenterView = null;
    } 
    OneShotPreDrawListener.add((View)paramViewGroup, new Runnable() {
          final Rect val$epicenter;
          
          final View val$epicenterView;
          
          final FragmentTransitionImpl val$impl;
          
          final Fragment val$inFragment;
          
          final boolean val$inIsPop;
          
          final ArrayMap val$inSharedElements;
          
          final Fragment val$outFragment;
          
          public void run() {
            FragmentTransition.callSharedElementStartEnd(inFragment, outFragment, inIsPop, inSharedElements, false);
            View view = epicenterView;
            if (view != null)
              impl.getBoundsOnScreen(view, epicenter); 
          }
        });
    return object1;
  }
  
  private static void configureTransitionsOrdered(ViewGroup paramViewGroup, FragmentContainerTransition paramFragmentContainerTransition, View paramView, ArrayMap<String, String> paramArrayMap, final Callback callback) {
    Fragment fragment1 = paramFragmentContainerTransition.lastIn;
    final Fragment outFragment = paramFragmentContainerTransition.firstOut;
    FragmentTransitionImpl fragmentTransitionImpl = chooseImpl(fragment2, fragment1);
    if (fragmentTransitionImpl == null)
      return; 
    boolean bool1 = paramFragmentContainerTransition.lastInIsPop;
    boolean bool2 = paramFragmentContainerTransition.firstOutIsPop;
    Object object4 = getEnterTransition(fragmentTransitionImpl, fragment1, bool1);
    Object object2 = getExitTransition(fragmentTransitionImpl, fragment2, bool2);
    ArrayList<View> arrayList3 = new ArrayList();
    ArrayList<View> arrayList1 = new ArrayList();
    Object object3 = configureSharedElementsOrdered(fragmentTransitionImpl, paramViewGroup, paramView, paramArrayMap, paramFragmentContainerTransition, arrayList3, arrayList1, object4, object2);
    if (object4 == null && object3 == null && object2 == null)
      return; 
    ArrayList<View> arrayList2 = configureEnteringExitingViews(fragmentTransitionImpl, object2, fragment2, arrayList3, paramView);
    if (arrayList2 == null || arrayList2.isEmpty())
      object2 = null; 
    fragmentTransitionImpl.addTarget(object4, paramView);
    Object object1 = mergeTransitions(fragmentTransitionImpl, object4, object2, object3, fragment1, paramFragmentContainerTransition.lastInIsPop);
    if (fragment2 != null && arrayList2 != null && (arrayList2.size() > 0 || arrayList3.size() > 0)) {
      final CancellationSignal signal = new CancellationSignal();
      callback.onStart(fragment2, cancellationSignal);
      fragmentTransitionImpl.setListenerForTransitionEnd(fragment2, object1, cancellationSignal, new Runnable() {
            final FragmentTransition.Callback val$callback;
            
            final Fragment val$outFragment;
            
            final CancellationSignal val$signal;
            
            public void run() {
              callback.onComplete(outFragment, signal);
            }
          });
    } 
    if (object1 != null) {
      ArrayList<View> arrayList = new ArrayList();
      fragmentTransitionImpl.scheduleRemoveTargets(object1, object4, arrayList, object2, arrayList2, object3, arrayList1);
      scheduleTargetChange(fragmentTransitionImpl, paramViewGroup, fragment1, paramView, arrayList1, object4, arrayList, object2, arrayList2);
      fragmentTransitionImpl.setNameOverridesOrdered((View)paramViewGroup, arrayList1, (Map<String, String>)paramArrayMap);
      fragmentTransitionImpl.beginDelayedTransition(paramViewGroup, object1);
      fragmentTransitionImpl.scheduleNameReset(paramViewGroup, arrayList1, (Map<String, String>)paramArrayMap);
    } 
  }
  
  private static void configureTransitionsReordered(ViewGroup paramViewGroup, FragmentContainerTransition paramFragmentContainerTransition, View paramView, ArrayMap<String, String> paramArrayMap, final Callback callback) {
    Fragment fragment2 = paramFragmentContainerTransition.lastIn;
    final Fragment outFragment = paramFragmentContainerTransition.firstOut;
    FragmentTransitionImpl fragmentTransitionImpl = chooseImpl(fragment1, fragment2);
    if (fragmentTransitionImpl == null)
      return; 
    boolean bool2 = paramFragmentContainerTransition.lastInIsPop;
    boolean bool1 = paramFragmentContainerTransition.firstOutIsPop;
    ArrayList<View> arrayList4 = new ArrayList();
    ArrayList<View> arrayList3 = new ArrayList();
    Object object2 = getEnterTransition(fragmentTransitionImpl, fragment2, bool2);
    Object object1 = getExitTransition(fragmentTransitionImpl, fragment1, bool1);
    Object object3 = configureSharedElementsReordered(fragmentTransitionImpl, paramViewGroup, paramView, paramArrayMap, paramFragmentContainerTransition, arrayList3, arrayList4, object2, object1);
    if (object2 == null && object3 == null && object1 == null)
      return; 
    ArrayList<View> arrayList1 = configureEnteringExitingViews(fragmentTransitionImpl, object1, fragment1, arrayList3, paramView);
    ArrayList<View> arrayList2 = configureEnteringExitingViews(fragmentTransitionImpl, object2, fragment2, arrayList4, paramView);
    setViewVisibility(arrayList2, 4);
    Object object4 = mergeTransitions(fragmentTransitionImpl, object2, object1, object3, fragment2, bool2);
    if (fragment1 != null && arrayList1 != null && (arrayList1.size() > 0 || arrayList3.size() > 0)) {
      final CancellationSignal signal = new CancellationSignal();
      callback.onStart(fragment1, cancellationSignal);
      fragmentTransitionImpl.setListenerForTransitionEnd(fragment1, object4, cancellationSignal, new Runnable() {
            final FragmentTransition.Callback val$callback;
            
            final Fragment val$outFragment;
            
            final CancellationSignal val$signal;
            
            public void run() {
              callback.onComplete(outFragment, signal);
            }
          });
    } 
    if (object4 != null) {
      replaceHide(fragmentTransitionImpl, object1, fragment1, arrayList1);
      ArrayList<String> arrayList = fragmentTransitionImpl.prepareSetNameOverridesReordered(arrayList4);
      fragmentTransitionImpl.scheduleRemoveTargets(object4, object2, arrayList2, object1, arrayList1, object3, arrayList4);
      fragmentTransitionImpl.beginDelayedTransition(paramViewGroup, object4);
      fragmentTransitionImpl.setNameOverridesReordered((View)paramViewGroup, arrayList3, arrayList4, arrayList, (Map<String, String>)paramArrayMap);
      setViewVisibility(arrayList2, 0);
      fragmentTransitionImpl.swapSharedElementTargets(object3, arrayList3, arrayList4);
    } 
  }
  
  private static FragmentContainerTransition ensureContainer(FragmentContainerTransition paramFragmentContainerTransition, SparseArray<FragmentContainerTransition> paramSparseArray, int paramInt) {
    FragmentContainerTransition fragmentContainerTransition = paramFragmentContainerTransition;
    if (paramFragmentContainerTransition == null) {
      fragmentContainerTransition = new FragmentContainerTransition();
      paramSparseArray.put(paramInt, fragmentContainerTransition);
    } 
    return fragmentContainerTransition;
  }
  
  static String findKeyForValue(ArrayMap<String, String> paramArrayMap, String paramString) {
    int i = paramArrayMap.size();
    for (byte b = 0; b < i; b++) {
      if (paramString.equals(paramArrayMap.valueAt(b)))
        return (String)paramArrayMap.keyAt(b); 
    } 
    return null;
  }
  
  private static Object getEnterTransition(FragmentTransitionImpl paramFragmentTransitionImpl, Fragment paramFragment, boolean paramBoolean) {
    Object object;
    if (paramFragment == null)
      return null; 
    if (paramBoolean) {
      object = paramFragment.getReenterTransition();
    } else {
      object = object.getEnterTransition();
    } 
    return paramFragmentTransitionImpl.cloneTransition(object);
  }
  
  private static Object getExitTransition(FragmentTransitionImpl paramFragmentTransitionImpl, Fragment paramFragment, boolean paramBoolean) {
    Object object;
    if (paramFragment == null)
      return null; 
    if (paramBoolean) {
      object = paramFragment.getReturnTransition();
    } else {
      object = object.getExitTransition();
    } 
    return paramFragmentTransitionImpl.cloneTransition(object);
  }
  
  static View getInEpicenterView(ArrayMap<String, View> paramArrayMap, FragmentContainerTransition paramFragmentContainerTransition, Object paramObject, boolean paramBoolean) {
    BackStackRecord backStackRecord = paramFragmentContainerTransition.lastInTransaction;
    if (paramObject != null && paramArrayMap != null && backStackRecord.mSharedElementSourceNames != null && !backStackRecord.mSharedElementSourceNames.isEmpty()) {
      String str;
      if (paramBoolean) {
        str = backStackRecord.mSharedElementSourceNames.get(0);
      } else {
        str = ((BackStackRecord)str).mSharedElementTargetNames.get(0);
      } 
      return (View)paramArrayMap.get(str);
    } 
    return null;
  }
  
  private static Object getSharedElementTransition(FragmentTransitionImpl paramFragmentTransitionImpl, Fragment paramFragment1, Fragment paramFragment2, boolean paramBoolean) {
    Object object;
    if (paramFragment1 == null || paramFragment2 == null)
      return null; 
    if (paramBoolean) {
      object = paramFragment2.getSharedElementReturnTransition();
    } else {
      object = object.getSharedElementEnterTransition();
    } 
    return paramFragmentTransitionImpl.wrapTransitionInSet(paramFragmentTransitionImpl.cloneTransition(object));
  }
  
  private static Object mergeTransitions(FragmentTransitionImpl paramFragmentTransitionImpl, Object paramObject1, Object paramObject2, Object paramObject3, Fragment paramFragment, boolean paramBoolean) {
    Object object;
    boolean bool2 = true;
    boolean bool1 = bool2;
    if (paramObject1 != null) {
      bool1 = bool2;
      if (paramObject2 != null) {
        bool1 = bool2;
        if (paramFragment != null) {
          if (paramBoolean) {
            paramBoolean = paramFragment.getAllowReturnTransitionOverlap();
          } else {
            paramBoolean = paramFragment.getAllowEnterTransitionOverlap();
          } 
          bool1 = paramBoolean;
        } 
      } 
    } 
    if (bool1) {
      object = paramFragmentTransitionImpl.mergeTransitionsTogether(paramObject2, paramObject1, paramObject3);
    } else {
      object = object.mergeTransitionsInSequence(paramObject2, paramObject1, paramObject3);
    } 
    return object;
  }
  
  private static void replaceHide(FragmentTransitionImpl paramFragmentTransitionImpl, Object paramObject, Fragment paramFragment, final ArrayList<View> exitingViews) {
    if (paramFragment != null && paramObject != null && paramFragment.mAdded && paramFragment.mHidden && paramFragment.mHiddenChanged) {
      paramFragment.setHideReplaced(true);
      paramFragmentTransitionImpl.scheduleHideFragmentView(paramObject, paramFragment.getView(), exitingViews);
      OneShotPreDrawListener.add((View)paramFragment.mContainer, new Runnable() {
            final ArrayList val$exitingViews;
            
            public void run() {
              FragmentTransition.setViewVisibility(exitingViews, 4);
            }
          });
    } 
  }
  
  private static FragmentTransitionImpl resolveSupportImpl() {
    try {
      return Class.forName("androidx.transition.FragmentTransitionSupport").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
    } catch (Exception exception) {
      return null;
    } 
  }
  
  static void retainValues(ArrayMap<String, String> paramArrayMap, ArrayMap<String, View> paramArrayMap1) {
    for (int i = paramArrayMap.size() - 1; i >= 0; i--) {
      if (!paramArrayMap1.containsKey(paramArrayMap.valueAt(i)))
        paramArrayMap.removeAt(i); 
    } 
  }
  
  private static void scheduleTargetChange(final FragmentTransitionImpl impl, ViewGroup paramViewGroup, final Fragment inFragment, final View nonExistentView, final ArrayList<View> sharedElementsIn, final Object enterTransition, final ArrayList<View> enteringViews, final Object exitTransition, final ArrayList<View> exitingViews) {
    OneShotPreDrawListener.add((View)paramViewGroup, new Runnable() {
          final Object val$enterTransition;
          
          final ArrayList val$enteringViews;
          
          final Object val$exitTransition;
          
          final ArrayList val$exitingViews;
          
          final FragmentTransitionImpl val$impl;
          
          final Fragment val$inFragment;
          
          final View val$nonExistentView;
          
          final ArrayList val$sharedElementsIn;
          
          public void run() {
            Object<View> object = (Object<View>)enterTransition;
            if (object != null) {
              impl.removeTarget(object, nonExistentView);
              object = (Object<View>)FragmentTransition.configureEnteringExitingViews(impl, enterTransition, inFragment, sharedElementsIn, nonExistentView);
              enteringViews.addAll((Collection<? extends View>)object);
            } 
            if (exitingViews != null) {
              if (exitTransition != null) {
                object = (Object<View>)new ArrayList();
                object.add(nonExistentView);
                impl.replaceTargets(exitTransition, exitingViews, (ArrayList<View>)object);
              } 
              exitingViews.clear();
              exitingViews.add(nonExistentView);
            } 
          }
        });
  }
  
  private static void setOutEpicenter(FragmentTransitionImpl paramFragmentTransitionImpl, Object paramObject1, Object paramObject2, ArrayMap<String, View> paramArrayMap, boolean paramBoolean, BackStackRecord paramBackStackRecord) {
    if (paramBackStackRecord.mSharedElementSourceNames != null && !paramBackStackRecord.mSharedElementSourceNames.isEmpty()) {
      String str;
      if (paramBoolean) {
        str = paramBackStackRecord.mSharedElementTargetNames.get(0);
      } else {
        str = ((BackStackRecord)str).mSharedElementSourceNames.get(0);
      } 
      View view = (View)paramArrayMap.get(str);
      paramFragmentTransitionImpl.setEpicenter(paramObject1, view);
      if (paramObject2 != null)
        paramFragmentTransitionImpl.setEpicenter(paramObject2, view); 
    } 
  }
  
  static void setViewVisibility(ArrayList<View> paramArrayList, int paramInt) {
    if (paramArrayList == null)
      return; 
    for (int i = paramArrayList.size() - 1; i >= 0; i--)
      ((View)paramArrayList.get(i)).setVisibility(paramInt); 
  }
  
  static void startTransitions(Context paramContext, FragmentContainer paramFragmentContainer, ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2, boolean paramBoolean, Callback paramCallback) {
    SparseArray<FragmentContainerTransition> sparseArray = new SparseArray();
    int i;
    for (i = paramInt1; i < paramInt2; i++) {
      BackStackRecord backStackRecord = paramArrayList.get(i);
      if (((Boolean)paramArrayList1.get(i)).booleanValue()) {
        calculatePopFragments(backStackRecord, sparseArray, paramBoolean);
      } else {
        calculateFragments(backStackRecord, sparseArray, paramBoolean);
      } 
    } 
    if (sparseArray.size() != 0) {
      View view = new View(paramContext);
      int j = sparseArray.size();
      for (i = 0; i < j; i++) {
        int k = sparseArray.keyAt(i);
        ArrayMap<String, String> arrayMap = calculateNameOverrides(k, paramArrayList, paramArrayList1, paramInt1, paramInt2);
        FragmentContainerTransition fragmentContainerTransition = (FragmentContainerTransition)sparseArray.valueAt(i);
        if (paramFragmentContainer.onHasView()) {
          ViewGroup viewGroup = (ViewGroup)paramFragmentContainer.onFindViewById(k);
          if (viewGroup != null)
            if (paramBoolean) {
              configureTransitionsReordered(viewGroup, fragmentContainerTransition, view, arrayMap, paramCallback);
            } else {
              configureTransitionsOrdered(viewGroup, fragmentContainerTransition, view, arrayMap, paramCallback);
            }  
        } 
      } 
    } 
  }
  
  static boolean supportsTransition() {
    return (PLATFORM_IMPL != null || SUPPORT_IMPL != null);
  }
  
  static {
    FragmentTransitionImpl fragmentTransitionImpl;
  }
  
  static {
    if (Build.VERSION.SDK_INT >= 21) {
      fragmentTransitionImpl = new FragmentTransitionCompat21();
    } else {
      fragmentTransitionImpl = null;
    } 
    PLATFORM_IMPL = fragmentTransitionImpl;
  }
  
  static interface Callback {
    void onComplete(Fragment param1Fragment, CancellationSignal param1CancellationSignal);
    
    void onStart(Fragment param1Fragment, CancellationSignal param1CancellationSignal);
  }
  
  static class FragmentContainerTransition {
    public Fragment firstOut;
    
    public boolean firstOutIsPop;
    
    public BackStackRecord firstOutTransaction;
    
    public Fragment lastIn;
    
    public boolean lastInIsPop;
    
    public BackStackRecord lastInTransaction;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\fragment\app\FragmentTransition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */