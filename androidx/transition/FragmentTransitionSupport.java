package androidx.transition;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentTransitionImpl;
import java.util.ArrayList;
import java.util.List;

public class FragmentTransitionSupport extends FragmentTransitionImpl {
  private static boolean hasSimpleTarget(Transition paramTransition) {
    return (!isNullOrEmpty(paramTransition.getTargetIds()) || !isNullOrEmpty(paramTransition.getTargetNames()) || !isNullOrEmpty(paramTransition.getTargetTypes()));
  }
  
  public void addTarget(Object paramObject, View paramView) {
    if (paramObject != null)
      ((Transition)paramObject).addTarget(paramView); 
  }
  
  public void addTargets(Object paramObject, ArrayList<View> paramArrayList) {
    paramObject = paramObject;
    if (paramObject == null)
      return; 
    if (paramObject instanceof TransitionSet) {
      paramObject = paramObject;
      int i = paramObject.getTransitionCount();
      for (byte b = 0; b < i; b++)
        addTargets(paramObject.getTransitionAt(b), paramArrayList); 
    } else if (!hasSimpleTarget((Transition)paramObject) && isNullOrEmpty(paramObject.getTargets())) {
      int i = paramArrayList.size();
      for (byte b = 0; b < i; b++)
        paramObject.addTarget(paramArrayList.get(b)); 
    } 
  }
  
  public void beginDelayedTransition(ViewGroup paramViewGroup, Object paramObject) {
    TransitionManager.beginDelayedTransition(paramViewGroup, (Transition)paramObject);
  }
  
  public boolean canHandle(Object paramObject) {
    return paramObject instanceof Transition;
  }
  
  public Object cloneTransition(Object paramObject) {
    Transition transition = null;
    if (paramObject != null)
      transition = ((Transition)paramObject).clone(); 
    return transition;
  }
  
  public Object mergeTransitionsInSequence(Object paramObject1, Object paramObject2, Object paramObject3) {
    Object object = null;
    paramObject1 = paramObject1;
    paramObject2 = paramObject2;
    paramObject3 = paramObject3;
    if (paramObject1 != null && paramObject2 != null) {
      paramObject1 = (new TransitionSet()).addTransition((Transition)paramObject1).addTransition((Transition)paramObject2).setOrdering(1);
    } else if (paramObject1 == null) {
      paramObject1 = object;
      if (paramObject2 != null)
        paramObject1 = paramObject2; 
    } 
    if (paramObject3 != null) {
      paramObject2 = new TransitionSet();
      if (paramObject1 != null)
        paramObject2.addTransition((Transition)paramObject1); 
      paramObject2.addTransition((Transition)paramObject3);
      return paramObject2;
    } 
    return paramObject1;
  }
  
  public Object mergeTransitionsTogether(Object paramObject1, Object paramObject2, Object paramObject3) {
    TransitionSet transitionSet = new TransitionSet();
    if (paramObject1 != null)
      transitionSet.addTransition((Transition)paramObject1); 
    if (paramObject2 != null)
      transitionSet.addTransition((Transition)paramObject2); 
    if (paramObject3 != null)
      transitionSet.addTransition((Transition)paramObject3); 
    return transitionSet;
  }
  
  public void removeTarget(Object paramObject, View paramView) {
    if (paramObject != null)
      ((Transition)paramObject).removeTarget(paramView); 
  }
  
  public void replaceTargets(Object<View> paramObject, ArrayList<View> paramArrayList1, ArrayList<View> paramArrayList2) {
    Transition transition = (Transition)paramObject;
    if (transition instanceof TransitionSet) {
      paramObject = (Object<View>)transition;
      int i = paramObject.getTransitionCount();
      for (byte b = 0; b < i; b++)
        replaceTargets(paramObject.getTransitionAt(b), paramArrayList1, paramArrayList2); 
    } else if (!hasSimpleTarget(transition)) {
      paramObject = (Object<View>)transition.getTargets();
      if (paramObject.size() == paramArrayList1.size() && paramObject.containsAll(paramArrayList1)) {
        if (paramArrayList2 == null) {
          i = 0;
        } else {
          i = paramArrayList2.size();
        } 
        for (byte b = 0; b < i; b++)
          transition.addTarget(paramArrayList2.get(b)); 
        for (int i = paramArrayList1.size() - 1; i >= 0; i--)
          transition.removeTarget(paramArrayList1.get(i)); 
      } 
    } 
  }
  
  public void scheduleHideFragmentView(Object paramObject, final View fragmentView, final ArrayList<View> exitingViews) {
    ((Transition)paramObject).addListener(new Transition.TransitionListener() {
          final FragmentTransitionSupport this$0;
          
          final ArrayList val$exitingViews;
          
          final View val$fragmentView;
          
          public void onTransitionCancel(Transition param1Transition) {}
          
          public void onTransitionEnd(Transition param1Transition) {
            param1Transition.removeListener(this);
            fragmentView.setVisibility(8);
            int i = exitingViews.size();
            for (byte b = 0; b < i; b++)
              ((View)exitingViews.get(b)).setVisibility(0); 
          }
          
          public void onTransitionPause(Transition param1Transition) {}
          
          public void onTransitionResume(Transition param1Transition) {}
          
          public void onTransitionStart(Transition param1Transition) {}
        });
  }
  
  public void scheduleRemoveTargets(Object paramObject1, final Object enterTransition, final ArrayList<View> enteringViews, final Object exitTransition, final ArrayList<View> exitingViews, final Object sharedElementTransition, final ArrayList<View> sharedElementsIn) {
    ((Transition)paramObject1).addListener(new TransitionListenerAdapter() {
          final FragmentTransitionSupport this$0;
          
          final Object val$enterTransition;
          
          final ArrayList val$enteringViews;
          
          final Object val$exitTransition;
          
          final ArrayList val$exitingViews;
          
          final Object val$sharedElementTransition;
          
          final ArrayList val$sharedElementsIn;
          
          public void onTransitionEnd(Transition param1Transition) {
            param1Transition.removeListener(this);
          }
          
          public void onTransitionStart(Transition param1Transition) {
            Object object = enterTransition;
            if (object != null)
              FragmentTransitionSupport.this.replaceTargets(object, enteringViews, (ArrayList<View>)null); 
            object = exitTransition;
            if (object != null)
              FragmentTransitionSupport.this.replaceTargets(object, exitingViews, (ArrayList<View>)null); 
            object = sharedElementTransition;
            if (object != null)
              FragmentTransitionSupport.this.replaceTargets(object, sharedElementsIn, (ArrayList<View>)null); 
          }
        });
  }
  
  public void setEpicenter(Object paramObject, final Rect epicenter) {
    if (paramObject != null)
      ((Transition)paramObject).setEpicenterCallback(new Transition.EpicenterCallback() {
            final FragmentTransitionSupport this$0;
            
            final Rect val$epicenter;
            
            public Rect onGetEpicenter(Transition param1Transition) {
              Rect rect = epicenter;
              return (rect == null || rect.isEmpty()) ? null : epicenter;
            }
          }); 
  }
  
  public void setEpicenter(final Object epicenter, View paramView) {
    if (paramView != null) {
      Transition transition = (Transition)epicenter;
      epicenter = new Rect();
      getBoundsOnScreen(paramView, (Rect)epicenter);
      transition.setEpicenterCallback(new Transition.EpicenterCallback() {
            final FragmentTransitionSupport this$0;
            
            final Rect val$epicenter;
            
            public Rect onGetEpicenter(Transition param1Transition) {
              return epicenter;
            }
          });
    } 
  }
  
  public void setSharedElementTargets(Object<View> paramObject, View paramView, ArrayList<View> paramArrayList) {
    TransitionSet transitionSet = (TransitionSet)paramObject;
    paramObject = (Object<View>)transitionSet.getTargets();
    paramObject.clear();
    int i = paramArrayList.size();
    for (byte b = 0; b < i; b++)
      bfsAddViewChildren((List)paramObject, paramArrayList.get(b)); 
    paramObject.add(paramView);
    paramArrayList.add(paramView);
    addTargets(transitionSet, paramArrayList);
  }
  
  public void swapSharedElementTargets(Object paramObject, ArrayList<View> paramArrayList1, ArrayList<View> paramArrayList2) {
    paramObject = paramObject;
    if (paramObject != null) {
      paramObject.getTargets().clear();
      paramObject.getTargets().addAll(paramArrayList2);
      replaceTargets(paramObject, paramArrayList1, paramArrayList2);
    } 
  }
  
  public Object wrapTransitionInSet(Object paramObject) {
    if (paramObject == null)
      return null; 
    TransitionSet transitionSet = new TransitionSet();
    transitionSet.addTransition((Transition)paramObject);
    return transitionSet;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\FragmentTransitionSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */