package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import androidx.collection.ArrayMap;
import androidx.core.app.SharedElementCallback;
import androidx.core.os.CancellationSignal;
import androidx.core.util.Preconditions;
import androidx.core.view.OneShotPreDrawListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewGroupCompat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class DefaultSpecialEffectsController extends SpecialEffectsController {
  DefaultSpecialEffectsController(ViewGroup paramViewGroup) {
    super(paramViewGroup);
  }
  
  private void startAnimations(List<AnimationInfo> paramList, List<SpecialEffectsController.Operation> paramList1, boolean paramBoolean, Map<SpecialEffectsController.Operation, Boolean> paramMap) {
    final ViewGroup container = getContainer();
    Context context = viewGroup.getContext();
    ArrayList<AnimationInfo> arrayList = new ArrayList();
    boolean bool = false;
    for (AnimationInfo animationInfo : paramList) {
      final boolean isHideOperation;
      if (animationInfo.isVisibilityUnchanged()) {
        animationInfo.completeSpecialEffect();
        continue;
      } 
      FragmentAnim.AnimationOrAnimator animationOrAnimator = animationInfo.getAnimation(context);
      if (animationOrAnimator == null) {
        animationInfo.completeSpecialEffect();
        continue;
      } 
      final Animator animator = animationOrAnimator.animator;
      if (animator == null) {
        arrayList.add(animationInfo);
        continue;
      } 
      final SpecialEffectsController.Operation operation = animationInfo.getOperation();
      Fragment fragment = operation.getFragment();
      if (Boolean.TRUE.equals(paramMap.get(operation))) {
        if (FragmentManager.isLoggingEnabled(2))
          Log.v("FragmentManager", "Ignoring Animator set on " + fragment + " as this Fragment was involved in a Transition."); 
        animationInfo.completeSpecialEffect();
        continue;
      } 
      if (operation.getFinalState() == SpecialEffectsController.Operation.State.GONE) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (bool1)
        paramList1.remove(operation); 
      final View viewToAnimate = fragment.mView;
      viewGroup.startViewTransition(view);
      animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            final DefaultSpecialEffectsController this$0;
            
            final DefaultSpecialEffectsController.AnimationInfo val$animationInfo;
            
            final ViewGroup val$container;
            
            final boolean val$isHideOperation;
            
            final SpecialEffectsController.Operation val$operation;
            
            final View val$viewToAnimate;
            
            public void onAnimationEnd(Animator param1Animator) {
              container.endViewTransition(viewToAnimate);
              if (isHideOperation)
                operation.getFinalState().applyState(viewToAnimate); 
              animationInfo.completeSpecialEffect();
            }
          });
      animator.setTarget(view);
      animator.start();
      animationInfo.getSignal().setOnCancelListener(new CancellationSignal.OnCancelListener() {
            final DefaultSpecialEffectsController this$0;
            
            final Animator val$animator;
            
            public void onCancel() {
              animator.end();
            }
          });
      bool = true;
    } 
    for (AnimationInfo animationInfo : arrayList) {
      final SpecialEffectsController.Operation operation = animationInfo.getOperation();
      Fragment fragment = operation.getFragment();
      if (paramBoolean) {
        if (FragmentManager.isLoggingEnabled(2))
          Log.v("FragmentManager", "Ignoring Animation set on " + fragment + " as Animations cannot run alongside Transitions."); 
        animationInfo.completeSpecialEffect();
        continue;
      } 
      if (bool) {
        if (FragmentManager.isLoggingEnabled(2))
          Log.v("FragmentManager", "Ignoring Animation set on " + fragment + " as Animations cannot run alongside Animators."); 
        animationInfo.completeSpecialEffect();
        continue;
      } 
      final View viewToAnimate = fragment.mView;
      Animation animation = (Animation)Preconditions.checkNotNull(((FragmentAnim.AnimationOrAnimator)Preconditions.checkNotNull(animationInfo.getAnimation(context))).animation);
      if (operation.getFinalState() != SpecialEffectsController.Operation.State.REMOVED) {
        view.startAnimation(animation);
        animationInfo.completeSpecialEffect();
      } else {
        viewGroup.startViewTransition(view);
        FragmentAnim.EndViewTransitionAnimation endViewTransitionAnimation = new FragmentAnim.EndViewTransitionAnimation(animation, viewGroup, view);
        endViewTransitionAnimation.setAnimationListener(new Animation.AnimationListener() {
              final DefaultSpecialEffectsController this$0;
              
              final DefaultSpecialEffectsController.AnimationInfo val$animationInfo;
              
              final ViewGroup val$container;
              
              final View val$viewToAnimate;
              
              public void onAnimationEnd(Animation param1Animation) {
                container.post(new Runnable() {
                      final DefaultSpecialEffectsController.null this$1;
                      
                      public void run() {
                        container.endViewTransition(viewToAnimate);
                        animationInfo.completeSpecialEffect();
                      }
                    });
              }
              
              public void onAnimationRepeat(Animation param1Animation) {}
              
              public void onAnimationStart(Animation param1Animation) {}
            });
        view.startAnimation((Animation)endViewTransitionAnimation);
      } 
      animationInfo.getSignal().setOnCancelListener(new CancellationSignal.OnCancelListener() {
            final DefaultSpecialEffectsController this$0;
            
            final DefaultSpecialEffectsController.AnimationInfo val$animationInfo;
            
            final ViewGroup val$container;
            
            final View val$viewToAnimate;
            
            public void onCancel() {
              viewToAnimate.clearAnimation();
              container.endViewTransition(viewToAnimate);
              animationInfo.completeSpecialEffect();
            }
          });
    } 
  }
  
  private Map<SpecialEffectsController.Operation, Boolean> startTransitions(List<TransitionInfo> paramList, List<SpecialEffectsController.Operation> paramList1, final boolean isPop, final SpecialEffectsController.Operation firstOut, final SpecialEffectsController.Operation lastIn) {
    SpecialEffectsController.Operation operation2 = firstOut;
    SpecialEffectsController.Operation operation1 = lastIn;
    HashMap<Object, Object> hashMap2 = new HashMap<>();
    Iterator<TransitionInfo> iterator1 = paramList.iterator();
    final FragmentTransitionImpl impl;
    for (fragmentTransitionImpl2 = null; iterator1.hasNext(); fragmentTransitionImpl2 = fragmentTransitionImpl3) {
      FragmentTransitionImpl fragmentTransitionImpl3;
      final TransitionInfo transitionInfo = iterator1.next();
      if (transitionInfo1.isVisibilityUnchanged())
        continue; 
      FragmentTransitionImpl fragmentTransitionImpl4 = transitionInfo1.getHandlingImpl();
      if (fragmentTransitionImpl2 == null) {
        fragmentTransitionImpl3 = fragmentTransitionImpl4;
      } else {
        fragmentTransitionImpl3 = fragmentTransitionImpl2;
        if (fragmentTransitionImpl4 != null)
          if (fragmentTransitionImpl2 == fragmentTransitionImpl4) {
            fragmentTransitionImpl3 = fragmentTransitionImpl2;
          } else {
            throw new IllegalArgumentException("Mixing framework transitions and AndroidX transitions is not allowed. Fragment " + transitionInfo1.getOperation().getFragment() + " returned Transition " + transitionInfo1.getTransition() + " which uses a different Transition  type than other Fragments.");
          }  
      } 
    } 
    boolean bool1 = false;
    if (fragmentTransitionImpl2 == null) {
      for (TransitionInfo transitionInfo : paramList) {
        hashMap2.put(transitionInfo.getOperation(), Boolean.valueOf(false));
        transitionInfo.completeSpecialEffect();
      } 
      return (Map)hashMap2;
    } 
    View view2 = new View(getContainer().getContext());
    Object object3 = null;
    final Rect lastInEpicenterRect = new Rect();
    ArrayList<View> arrayList3 = new ArrayList();
    ArrayList<View> arrayList5 = new ArrayList();
    ArrayMap<String, String> arrayMap = new ArrayMap();
    Iterator<TransitionInfo> iterator3 = transitionInfo.iterator();
    View view1 = null;
    boolean bool = false;
    while (iterator3.hasNext()) {
      final TransitionInfo transitionInfo = iterator3.next();
      if (transitionInfo1.hasSharedElementTransition() && operation2 != null && operation1 != null) {
        SharedElementCallback sharedElementCallback1;
        SharedElementCallback sharedElementCallback2;
        ArrayList<String> arrayList10;
        Object object = fragmentTransitionImpl2.wrapTransitionInSet(fragmentTransitionImpl2.cloneTransition(transitionInfo1.getSharedElementTransition()));
        object3 = lastIn.getFragment().getSharedElementSourceNames();
        ArrayList<String> arrayList11 = firstOut.getFragment().getSharedElementSourceNames();
        ArrayList<String> arrayList9 = firstOut.getFragment().getSharedElementTargetNames();
        int i;
        for (i = 0; i < arrayList9.size(); i++) {
          int j = object3.indexOf(arrayList9.get(i));
          if (j != -1)
            object3.set(j, arrayList11.get(i)); 
        } 
        ArrayList<String> arrayList12 = lastIn.getFragment().getSharedElementTargetNames();
        if (!isPop) {
          sharedElementCallback2 = firstOut.getFragment().getExitTransitionCallback();
          sharedElementCallback1 = lastIn.getFragment().getEnterTransitionCallback();
        } else {
          sharedElementCallback2 = firstOut.getFragment().getEnterTransitionCallback();
          sharedElementCallback1 = lastIn.getFragment().getExitTransitionCallback();
        } 
        i = object3.size();
        for (byte b = 0; b < i; b++)
          arrayMap.put(object3.get(b), arrayList12.get(b)); 
        ArrayMap<String, View> arrayMap2 = new ArrayMap();
        findNamedViews((Map<String, View>)arrayMap2, (firstOut.getFragment()).mView);
        arrayMap2.retainAll((Collection)object3);
        if (sharedElementCallback2 != null) {
          sharedElementCallback2.onMapSharedElements((List)object3, (Map)arrayMap2);
          for (i = object3.size() - 1; i >= 0; i--) {
            String str = object3.get(i);
            final View lastInEpicenterView = (View)arrayMap2.get(str);
            if (view == null) {
              arrayMap.remove(str);
            } else if (!str.equals(ViewCompat.getTransitionName(view))) {
              str = (String)arrayMap.remove(str);
              arrayMap.put(ViewCompat.getTransitionName(view), str);
            } 
          } 
          SharedElementCallback sharedElementCallback = sharedElementCallback2;
          arrayList10 = (ArrayList<String>)object3;
        } else {
          arrayMap.retainAll(arrayMap2.keySet());
          ArrayList<String> arrayList13 = arrayList10;
          arrayList10 = (ArrayList<String>)object3;
        } 
        final ArrayMap<String, View> lastInViews = new ArrayMap();
        findNamedViews((Map<String, View>)arrayMap1, (lastIn.getFragment()).mView);
        arrayMap1.retainAll(arrayList12);
        arrayMap1.retainAll(arrayMap.values());
        if (sharedElementCallback1 != null) {
          sharedElementCallback1.onMapSharedElements(arrayList12, (Map)arrayMap1);
          for (i = arrayList12.size() - 1; i >= 0; i--) {
            String str1;
            String str2 = arrayList12.get(i);
            final View lastInEpicenterView = (View)arrayMap1.get(str2);
            if (view == null) {
              str1 = FragmentTransition.findKeyForValue(arrayMap, str2);
              if (str1 != null)
                arrayMap.remove(str1); 
            } else if (!str2.equals(ViewCompat.getTransitionName((View)str1))) {
              str2 = FragmentTransition.findKeyForValue(arrayMap, str2);
              if (str2 != null)
                arrayMap.put(str2, ViewCompat.getTransitionName((View)str1)); 
            } 
          } 
        } else {
          FragmentTransition.retainValues(arrayMap, arrayMap1);
        } 
        retainMatchingViews(arrayMap2, arrayMap.keySet());
        retainMatchingViews(arrayMap1, arrayMap.values());
        if (arrayMap.isEmpty()) {
          arrayMap1 = null;
          arrayList3.clear();
          arrayList5.clear();
          bool1 = false;
          operation1 = firstOut;
          operation2 = lastIn;
        } else {
          FragmentTransition.callSharedElementStartEnd(lastIn.getFragment(), firstOut.getFragment(), isPop, arrayMap2, true);
          OneShotPreDrawListener.add((View)getContainer(), new Runnable() {
                final DefaultSpecialEffectsController this$0;
                
                final SpecialEffectsController.Operation val$firstOut;
                
                final boolean val$isPop;
                
                final SpecialEffectsController.Operation val$lastIn;
                
                final ArrayMap val$lastInViews;
                
                public void run() {
                  FragmentTransition.callSharedElementStartEnd(lastIn.getFragment(), firstOut.getFragment(), isPop, lastInViews, false);
                }
              });
          arrayList3.addAll(arrayMap2.values());
          if (!operation2.isEmpty()) {
            view1 = (View)arrayMap2.get(operation2.get(0));
            fragmentTransitionImpl2.setEpicenter(object, view1);
          } 
          arrayList5.addAll(arrayMap1.values());
          if (!arrayList12.isEmpty()) {
            final View lastInEpicenterView = (View)arrayMap1.get(arrayList12.get(0));
            if (view != null) {
              bool = true;
              OneShotPreDrawListener.add((View)getContainer(), new Runnable() {
                    final DefaultSpecialEffectsController this$0;
                    
                    final FragmentTransitionImpl val$impl;
                    
                    final Rect val$lastInEpicenterRect;
                    
                    final View val$lastInEpicenterView;
                    
                    public void run() {
                      impl.getBoundsOnScreen(lastInEpicenterView, lastInEpicenterRect);
                    }
                  });
            } 
          } 
          fragmentTransitionImpl2.setSharedElementTargets(object, view2, arrayList3);
          bool1 = false;
          fragmentTransitionImpl2.scheduleRemoveTargets(object, null, null, null, null, object, arrayList5);
          operation1 = firstOut;
          hashMap2.put(operation1, Boolean.valueOf(true));
          operation2 = lastIn;
          hashMap2.put(operation2, Boolean.valueOf(true));
          object3 = object;
        } 
      } else {
        SpecialEffectsController.Operation operation5 = operation1;
        operation1 = operation2;
        operation2 = operation5;
      } 
      final ArrayList<View> transitioningViews = arrayList3;
      SpecialEffectsController.Operation operation = operation2;
      operation2 = operation1;
      operation1 = operation;
      arrayList3 = arrayList8;
    } 
    Object object2 = view1;
    Object object4 = arrayList5;
    Rect rect3 = rect2;
    View view4 = view2;
    isPop = bool1;
    FragmentTransitionImpl fragmentTransitionImpl1 = fragmentTransitionImpl2;
    SpecialEffectsController.Operation operation4 = operation1;
    HashMap<Object, Object> hashMap1 = hashMap2;
    ArrayList<View> arrayList4 = arrayList3;
    ArrayList<View> arrayList7 = new ArrayList();
    ArrayList arrayList6 = null;
    View view5 = null;
    Iterator<TransitionInfo> iterator4 = transitionInfo.iterator();
    Rect rect1 = rect3;
    View view3 = (View)object2;
    ArrayList<View> arrayList2 = (ArrayList<View>)object4;
    SpecialEffectsController.Operation operation3 = operation2;
    ArrayList<View> arrayList1 = arrayList4;
    Iterator<TransitionInfo> iterator2 = iterator4;
    object2 = view5;
    object4 = arrayList6;
    while (iterator2.hasNext()) {
      final TransitionInfo transitionInfo = iterator2.next();
      if (transitionInfo1.isVisibilityUnchanged()) {
        hashMap1.put(transitionInfo1.getOperation(), Boolean.valueOf(isPop));
        transitionInfo1.completeSpecialEffect();
        continue;
      } 
      Object object = fragmentTransitionImpl1.cloneTransition(transitionInfo1.getTransition());
      SpecialEffectsController.Operation operation = transitionInfo1.getOperation();
      if (object3 != null && (operation == operation3 || operation == operation4)) {
        bool1 = true;
      } else {
        bool1 = isPop;
      } 
      if (object == null) {
        if (!bool1) {
          hashMap1.put(operation, Boolean.valueOf(isPop));
          transitionInfo1.completeSpecialEffect();
        } 
      } else {
        final ArrayList<View> transitioningViews = new ArrayList();
        captureTransitioningViews(arrayList8, (operation.getFragment()).mView);
        if (bool1)
          if (operation == operation3) {
            arrayList8.removeAll(arrayList1);
          } else {
            arrayList8.removeAll(arrayList2);
          }  
        if (arrayList8.isEmpty()) {
          fragmentTransitionImpl1.addTarget(object, view4);
        } else {
          fragmentTransitionImpl1.addTargets(object, arrayList8);
          fragmentTransitionImpl1.scheduleRemoveTargets(object, object, arrayList8, null, null, null, null);
          if (operation.getFinalState() == SpecialEffectsController.Operation.State.GONE) {
            paramList1.remove(operation);
            ArrayList<View> arrayList9 = new ArrayList<>(arrayList8);
            arrayList9.remove((operation.getFragment()).mView);
            fragmentTransitionImpl1.scheduleHideFragmentView(object, (operation.getFragment()).mView, arrayList9);
            OneShotPreDrawListener.add((View)getContainer(), new Runnable() {
                  final DefaultSpecialEffectsController this$0;
                  
                  final ArrayList val$transitioningViews;
                  
                  public void run() {
                    FragmentTransition.setViewVisibility(transitioningViews, 4);
                  }
                });
          } 
        } 
        if (operation.getFinalState() == SpecialEffectsController.Operation.State.VISIBLE) {
          arrayList7.addAll(arrayList8);
          if (bool)
            fragmentTransitionImpl1.setEpicenter(object, rect1); 
        } else {
          fragmentTransitionImpl1.setEpicenter(object, view3);
        } 
        hashMap1.put(operation, Boolean.valueOf(true));
        if (transitionInfo1.isOverlapAllowed()) {
          object4 = fragmentTransitionImpl1.mergeTransitionsTogether(object4, object, null);
        } else {
          object2 = fragmentTransitionImpl1.mergeTransitionsTogether(object2, object, null);
        } 
      } 
      isPop = false;
      operation4 = lastIn;
    } 
    Object object1 = fragmentTransitionImpl1.mergeTransitionsInSequence(object4, object2, object3);
    for (TransitionInfo transitionInfo1 : transitionInfo) {
      if (transitionInfo1.isVisibilityUnchanged())
        continue; 
      Object object = transitionInfo1.getTransition();
      SpecialEffectsController.Operation operation = transitionInfo1.getOperation();
      if (object3 != null && (operation == operation3 || operation == lastIn)) {
        bool = true;
      } else {
        bool = false;
      } 
      if (object != null || bool) {
        if (!ViewCompat.isLaidOut((View)getContainer())) {
          if (FragmentManager.isLoggingEnabled(2))
            Log.v("FragmentManager", "SpecialEffectsController: Container " + getContainer() + " has not been laid out. Completing operation " + operation); 
          transitionInfo1.completeSpecialEffect();
          continue;
        } 
        fragmentTransitionImpl1.setListenerForTransitionEnd(transitionInfo1.getOperation().getFragment(), object1, transitionInfo1.getSignal(), new Runnable() {
              final DefaultSpecialEffectsController this$0;
              
              final DefaultSpecialEffectsController.TransitionInfo val$transitionInfo;
              
              public void run() {
                transitionInfo.completeSpecialEffect();
              }
            });
      } 
    } 
    if (!ViewCompat.isLaidOut((View)getContainer()))
      return (Map)hashMap1; 
    FragmentTransition.setViewVisibility(arrayList7, 4);
    ArrayList<String> arrayList = fragmentTransitionImpl1.prepareSetNameOverridesReordered(arrayList2);
    fragmentTransitionImpl1.beginDelayedTransition(getContainer(), object1);
    fragmentTransitionImpl1.setNameOverridesReordered((View)getContainer(), arrayList1, arrayList2, arrayList, (Map<String, String>)arrayMap);
    FragmentTransition.setViewVisibility(arrayList7, 0);
    fragmentTransitionImpl1.swapSharedElementTargets(object3, arrayList1, arrayList2);
    return (Map)hashMap1;
  }
  
  void applyContainerChanges(SpecialEffectsController.Operation paramOperation) {
    View view = (paramOperation.getFragment()).mView;
    paramOperation.getFinalState().applyState(view);
  }
  
  void captureTransitioningViews(ArrayList<View> paramArrayList, View paramView) {
    if (paramView instanceof ViewGroup) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      if (ViewGroupCompat.isTransitionGroup(viewGroup)) {
        if (!paramArrayList.contains(paramView))
          paramArrayList.add(viewGroup); 
      } else {
        int i = viewGroup.getChildCount();
        for (byte b = 0; b < i; b++) {
          paramView = viewGroup.getChildAt(b);
          if (paramView.getVisibility() == 0)
            captureTransitioningViews(paramArrayList, paramView); 
        } 
      } 
    } else if (!paramArrayList.contains(paramView)) {
      paramArrayList.add(paramView);
    } 
  }
  
  void executeOperations(List<SpecialEffectsController.Operation> paramList, boolean paramBoolean) {
    SpecialEffectsController.Operation operation2 = null;
    SpecialEffectsController.Operation operation1 = null;
    for (SpecialEffectsController.Operation operation5 : paramList) {
      SpecialEffectsController.Operation operation3;
      SpecialEffectsController.Operation operation4;
      SpecialEffectsController.Operation.State state = SpecialEffectsController.Operation.State.from((operation5.getFragment()).mView);
      switch (operation5.getFinalState()) {
        default:
          operation3 = operation2;
          operation4 = operation1;
          break;
        case null:
          operation3 = operation2;
          operation4 = operation1;
          if (state != SpecialEffectsController.Operation.State.VISIBLE) {
            operation4 = operation5;
            operation3 = operation2;
          } 
          break;
        case null:
        case null:
        case null:
          operation3 = operation2;
          operation4 = operation1;
          if (state == SpecialEffectsController.Operation.State.VISIBLE) {
            operation3 = operation2;
            operation4 = operation1;
            if (operation2 == null) {
              operation3 = operation5;
              operation4 = operation1;
            } 
          } 
          break;
      } 
      operation2 = operation3;
      operation1 = operation4;
    } 
    ArrayList<AnimationInfo> arrayList1 = new ArrayList();
    ArrayList<TransitionInfo> arrayList2 = new ArrayList();
    final ArrayList<SpecialEffectsController.Operation> awaitingContainerChanges = new ArrayList<>(paramList);
    Iterator<SpecialEffectsController.Operation> iterator = paramList.iterator();
    while (true) {
      boolean bool1 = iterator.hasNext();
      boolean bool = true;
      if (bool1) {
        final SpecialEffectsController.Operation operation = iterator.next();
        CancellationSignal cancellationSignal = new CancellationSignal();
        operation.markStartedSpecialEffect(cancellationSignal);
        arrayList1.add(new AnimationInfo(operation, cancellationSignal, paramBoolean));
        cancellationSignal = new CancellationSignal();
        operation.markStartedSpecialEffect(cancellationSignal);
        if (paramBoolean ? (operation == operation2) : (operation == operation1))
          bool = false; 
        arrayList2.add(new TransitionInfo(operation, cancellationSignal, paramBoolean, bool));
        operation.addCompletionListener(new Runnable() {
              final DefaultSpecialEffectsController this$0;
              
              final List val$awaitingContainerChanges;
              
              final SpecialEffectsController.Operation val$operation;
              
              public void run() {
                if (awaitingContainerChanges.contains(operation)) {
                  awaitingContainerChanges.remove(operation);
                  DefaultSpecialEffectsController.this.applyContainerChanges(operation);
                } 
              }
            });
        continue;
      } 
      Map<SpecialEffectsController.Operation, Boolean> map = startTransitions(arrayList2, arrayList, paramBoolean, operation2, operation1);
      startAnimations(arrayList1, arrayList, map.containsValue(Boolean.valueOf(true)), map);
      Iterator<SpecialEffectsController.Operation> iterator1 = arrayList.iterator();
      while (iterator1.hasNext())
        applyContainerChanges(iterator1.next()); 
      arrayList.clear();
      return;
    } 
  }
  
  void findNamedViews(Map<String, View> paramMap, View paramView) {
    String str = ViewCompat.getTransitionName(paramView);
    if (str != null)
      paramMap.put(str, paramView); 
    if (paramView instanceof ViewGroup) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      int i = viewGroup.getChildCount();
      for (byte b = 0; b < i; b++) {
        View view = viewGroup.getChildAt(b);
        if (view.getVisibility() == 0)
          findNamedViews(paramMap, view); 
      } 
    } 
  }
  
  void retainMatchingViews(ArrayMap<String, View> paramArrayMap, Collection<String> paramCollection) {
    Iterator<Map.Entry> iterator = paramArrayMap.entrySet().iterator();
    while (iterator.hasNext()) {
      if (!paramCollection.contains(ViewCompat.getTransitionName((View)((Map.Entry)iterator.next()).getValue())))
        iterator.remove(); 
    } 
  }
  
  private static class AnimationInfo extends SpecialEffectsInfo {
    private FragmentAnim.AnimationOrAnimator mAnimation;
    
    private boolean mIsPop;
    
    private boolean mLoadedAnim = false;
    
    AnimationInfo(SpecialEffectsController.Operation param1Operation, CancellationSignal param1CancellationSignal, boolean param1Boolean) {
      super(param1Operation, param1CancellationSignal);
      this.mIsPop = param1Boolean;
    }
    
    FragmentAnim.AnimationOrAnimator getAnimation(Context param1Context) {
      boolean bool;
      if (this.mLoadedAnim)
        return this.mAnimation; 
      Fragment fragment = getOperation().getFragment();
      if (getOperation().getFinalState() == SpecialEffectsController.Operation.State.VISIBLE) {
        bool = true;
      } else {
        bool = false;
      } 
      FragmentAnim.AnimationOrAnimator animationOrAnimator = FragmentAnim.loadAnimation(param1Context, fragment, bool, this.mIsPop);
      this.mAnimation = animationOrAnimator;
      this.mLoadedAnim = true;
      return animationOrAnimator;
    }
  }
  
  private static class SpecialEffectsInfo {
    private final SpecialEffectsController.Operation mOperation;
    
    private final CancellationSignal mSignal;
    
    SpecialEffectsInfo(SpecialEffectsController.Operation param1Operation, CancellationSignal param1CancellationSignal) {
      this.mOperation = param1Operation;
      this.mSignal = param1CancellationSignal;
    }
    
    void completeSpecialEffect() {
      this.mOperation.completeSpecialEffect(this.mSignal);
    }
    
    SpecialEffectsController.Operation getOperation() {
      return this.mOperation;
    }
    
    CancellationSignal getSignal() {
      return this.mSignal;
    }
    
    boolean isVisibilityUnchanged() {
      SpecialEffectsController.Operation.State state1 = SpecialEffectsController.Operation.State.from((this.mOperation.getFragment()).mView);
      SpecialEffectsController.Operation.State state2 = this.mOperation.getFinalState();
      return (state1 == state2 || (state1 != SpecialEffectsController.Operation.State.VISIBLE && state2 != SpecialEffectsController.Operation.State.VISIBLE));
    }
  }
  
  private static class TransitionInfo extends SpecialEffectsInfo {
    private final boolean mOverlapAllowed;
    
    private final Object mSharedElementTransition;
    
    private final Object mTransition;
    
    TransitionInfo(SpecialEffectsController.Operation param1Operation, CancellationSignal param1CancellationSignal, boolean param1Boolean1, boolean param1Boolean2) {
      super(param1Operation, param1CancellationSignal);
      if (param1Operation.getFinalState() == SpecialEffectsController.Operation.State.VISIBLE) {
        Object object;
        boolean bool;
        if (param1Boolean1) {
          object = param1Operation.getFragment().getReenterTransition();
        } else {
          object = param1Operation.getFragment().getEnterTransition();
        } 
        this.mTransition = object;
        if (param1Boolean1) {
          bool = param1Operation.getFragment().getAllowReturnTransitionOverlap();
        } else {
          bool = param1Operation.getFragment().getAllowEnterTransitionOverlap();
        } 
        this.mOverlapAllowed = bool;
      } else {
        Object object;
        if (param1Boolean1) {
          object = param1Operation.getFragment().getReturnTransition();
        } else {
          object = param1Operation.getFragment().getExitTransition();
        } 
        this.mTransition = object;
        this.mOverlapAllowed = true;
      } 
      if (param1Boolean2) {
        if (param1Boolean1) {
          this.mSharedElementTransition = param1Operation.getFragment().getSharedElementReturnTransition();
        } else {
          this.mSharedElementTransition = param1Operation.getFragment().getSharedElementEnterTransition();
        } 
      } else {
        this.mSharedElementTransition = null;
      } 
    }
    
    private FragmentTransitionImpl getHandlingImpl(Object param1Object) {
      if (param1Object == null)
        return null; 
      if (FragmentTransition.PLATFORM_IMPL != null && FragmentTransition.PLATFORM_IMPL.canHandle(param1Object))
        return FragmentTransition.PLATFORM_IMPL; 
      if (FragmentTransition.SUPPORT_IMPL != null && FragmentTransition.SUPPORT_IMPL.canHandle(param1Object))
        return FragmentTransition.SUPPORT_IMPL; 
      throw new IllegalArgumentException("Transition " + param1Object + " for fragment " + getOperation().getFragment() + " is not a valid framework Transition or AndroidX Transition");
    }
    
    FragmentTransitionImpl getHandlingImpl() {
      FragmentTransitionImpl fragmentTransitionImpl1 = getHandlingImpl(this.mTransition);
      FragmentTransitionImpl fragmentTransitionImpl2 = getHandlingImpl(this.mSharedElementTransition);
      if (fragmentTransitionImpl1 == null || fragmentTransitionImpl2 == null || fragmentTransitionImpl1 == fragmentTransitionImpl2) {
        if (fragmentTransitionImpl1 == null)
          fragmentTransitionImpl1 = fragmentTransitionImpl2; 
        return fragmentTransitionImpl1;
      } 
      throw new IllegalArgumentException("Mixing framework transitions and AndroidX transitions is not allowed. Fragment " + getOperation().getFragment() + " returned Transition " + this.mTransition + " which uses a different Transition  type than its shared element transition " + this.mSharedElementTransition);
    }
    
    public Object getSharedElementTransition() {
      return this.mSharedElementTransition;
    }
    
    Object getTransition() {
      return this.mTransition;
    }
    
    public boolean hasSharedElementTransition() {
      boolean bool;
      if (this.mSharedElementTransition != null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean isOverlapAllowed() {
      return this.mOverlapAllowed;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\fragment\app\DefaultSpecialEffectsController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */