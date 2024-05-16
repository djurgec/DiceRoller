package androidx.transition;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;

public class TransitionSet extends Transition {
  private static final int FLAG_CHANGE_EPICENTER = 8;
  
  private static final int FLAG_CHANGE_INTERPOLATOR = 1;
  
  private static final int FLAG_CHANGE_PATH_MOTION = 4;
  
  private static final int FLAG_CHANGE_PROPAGATION = 2;
  
  public static final int ORDERING_SEQUENTIAL = 1;
  
  public static final int ORDERING_TOGETHER = 0;
  
  private int mChangeFlags = 0;
  
  int mCurrentListeners;
  
  private boolean mPlayTogether = true;
  
  boolean mStarted = false;
  
  private ArrayList<Transition> mTransitions = new ArrayList<>();
  
  public TransitionSet() {}
  
  public TransitionSet(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, Styleable.TRANSITION_SET);
    setOrdering(TypedArrayUtils.getNamedInt(typedArray, (XmlPullParser)paramAttributeSet, "transitionOrdering", 0, 0));
    typedArray.recycle();
  }
  
  private void addTransitionInternal(Transition paramTransition) {
    this.mTransitions.add(paramTransition);
    paramTransition.mParent = this;
  }
  
  private void setupStartEndListeners() {
    TransitionSetListener transitionSetListener = new TransitionSetListener(this);
    Iterator<Transition> iterator = this.mTransitions.iterator();
    while (iterator.hasNext())
      ((Transition)iterator.next()).addListener(transitionSetListener); 
    this.mCurrentListeners = this.mTransitions.size();
  }
  
  public TransitionSet addListener(Transition.TransitionListener paramTransitionListener) {
    return (TransitionSet)super.addListener(paramTransitionListener);
  }
  
  public TransitionSet addTarget(int paramInt) {
    for (byte b = 0; b < this.mTransitions.size(); b++)
      ((Transition)this.mTransitions.get(b)).addTarget(paramInt); 
    return (TransitionSet)super.addTarget(paramInt);
  }
  
  public TransitionSet addTarget(View paramView) {
    for (byte b = 0; b < this.mTransitions.size(); b++)
      ((Transition)this.mTransitions.get(b)).addTarget(paramView); 
    return (TransitionSet)super.addTarget(paramView);
  }
  
  public TransitionSet addTarget(Class<?> paramClass) {
    for (byte b = 0; b < this.mTransitions.size(); b++)
      ((Transition)this.mTransitions.get(b)).addTarget(paramClass); 
    return (TransitionSet)super.addTarget(paramClass);
  }
  
  public TransitionSet addTarget(String paramString) {
    for (byte b = 0; b < this.mTransitions.size(); b++)
      ((Transition)this.mTransitions.get(b)).addTarget(paramString); 
    return (TransitionSet)super.addTarget(paramString);
  }
  
  public TransitionSet addTransition(Transition paramTransition) {
    addTransitionInternal(paramTransition);
    if (this.mDuration >= 0L)
      paramTransition.setDuration(this.mDuration); 
    if ((this.mChangeFlags & 0x1) != 0)
      paramTransition.setInterpolator(getInterpolator()); 
    if ((this.mChangeFlags & 0x2) != 0)
      paramTransition.setPropagation(getPropagation()); 
    if ((this.mChangeFlags & 0x4) != 0)
      paramTransition.setPathMotion(getPathMotion()); 
    if ((this.mChangeFlags & 0x8) != 0)
      paramTransition.setEpicenterCallback(getEpicenterCallback()); 
    return this;
  }
  
  protected void cancel() {
    super.cancel();
    int i = this.mTransitions.size();
    for (byte b = 0; b < i; b++)
      ((Transition)this.mTransitions.get(b)).cancel(); 
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues) {
    if (isValidTarget(paramTransitionValues.view))
      for (Transition transition : this.mTransitions) {
        if (transition.isValidTarget(paramTransitionValues.view)) {
          transition.captureEndValues(paramTransitionValues);
          paramTransitionValues.mTargetedTransitions.add(transition);
        } 
      }  
  }
  
  void capturePropagationValues(TransitionValues paramTransitionValues) {
    super.capturePropagationValues(paramTransitionValues);
    int i = this.mTransitions.size();
    for (byte b = 0; b < i; b++)
      ((Transition)this.mTransitions.get(b)).capturePropagationValues(paramTransitionValues); 
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues) {
    if (isValidTarget(paramTransitionValues.view))
      for (Transition transition : this.mTransitions) {
        if (transition.isValidTarget(paramTransitionValues.view)) {
          transition.captureStartValues(paramTransitionValues);
          paramTransitionValues.mTargetedTransitions.add(transition);
        } 
      }  
  }
  
  public Transition clone() {
    TransitionSet transitionSet = (TransitionSet)super.clone();
    transitionSet.mTransitions = new ArrayList<>();
    int i = this.mTransitions.size();
    for (byte b = 0; b < i; b++)
      transitionSet.addTransitionInternal(((Transition)this.mTransitions.get(b)).clone()); 
    return transitionSet;
  }
  
  protected void createAnimators(ViewGroup paramViewGroup, TransitionValuesMaps paramTransitionValuesMaps1, TransitionValuesMaps paramTransitionValuesMaps2, ArrayList<TransitionValues> paramArrayList1, ArrayList<TransitionValues> paramArrayList2) {
    long l = getStartDelay();
    int i = this.mTransitions.size();
    for (byte b = 0; b < i; b++) {
      Transition transition = this.mTransitions.get(b);
      if (l > 0L && (this.mPlayTogether || b == 0)) {
        long l1 = transition.getStartDelay();
        if (l1 > 0L) {
          transition.setStartDelay(l + l1);
        } else {
          transition.setStartDelay(l);
        } 
      } 
      transition.createAnimators(paramViewGroup, paramTransitionValuesMaps1, paramTransitionValuesMaps2, paramArrayList1, paramArrayList2);
    } 
  }
  
  public Transition excludeTarget(int paramInt, boolean paramBoolean) {
    for (byte b = 0; b < this.mTransitions.size(); b++)
      ((Transition)this.mTransitions.get(b)).excludeTarget(paramInt, paramBoolean); 
    return super.excludeTarget(paramInt, paramBoolean);
  }
  
  public Transition excludeTarget(View paramView, boolean paramBoolean) {
    for (byte b = 0; b < this.mTransitions.size(); b++)
      ((Transition)this.mTransitions.get(b)).excludeTarget(paramView, paramBoolean); 
    return super.excludeTarget(paramView, paramBoolean);
  }
  
  public Transition excludeTarget(Class<?> paramClass, boolean paramBoolean) {
    for (byte b = 0; b < this.mTransitions.size(); b++)
      ((Transition)this.mTransitions.get(b)).excludeTarget(paramClass, paramBoolean); 
    return super.excludeTarget(paramClass, paramBoolean);
  }
  
  public Transition excludeTarget(String paramString, boolean paramBoolean) {
    for (byte b = 0; b < this.mTransitions.size(); b++)
      ((Transition)this.mTransitions.get(b)).excludeTarget(paramString, paramBoolean); 
    return super.excludeTarget(paramString, paramBoolean);
  }
  
  void forceToEnd(ViewGroup paramViewGroup) {
    super.forceToEnd(paramViewGroup);
    int i = this.mTransitions.size();
    for (byte b = 0; b < i; b++)
      ((Transition)this.mTransitions.get(b)).forceToEnd(paramViewGroup); 
  }
  
  public int getOrdering() {
    return this.mPlayTogether ^ true;
  }
  
  public Transition getTransitionAt(int paramInt) {
    return (paramInt < 0 || paramInt >= this.mTransitions.size()) ? null : this.mTransitions.get(paramInt);
  }
  
  public int getTransitionCount() {
    return this.mTransitions.size();
  }
  
  public void pause(View paramView) {
    super.pause(paramView);
    int i = this.mTransitions.size();
    for (byte b = 0; b < i; b++)
      ((Transition)this.mTransitions.get(b)).pause(paramView); 
  }
  
  public TransitionSet removeListener(Transition.TransitionListener paramTransitionListener) {
    return (TransitionSet)super.removeListener(paramTransitionListener);
  }
  
  public TransitionSet removeTarget(int paramInt) {
    for (byte b = 0; b < this.mTransitions.size(); b++)
      ((Transition)this.mTransitions.get(b)).removeTarget(paramInt); 
    return (TransitionSet)super.removeTarget(paramInt);
  }
  
  public TransitionSet removeTarget(View paramView) {
    for (byte b = 0; b < this.mTransitions.size(); b++)
      ((Transition)this.mTransitions.get(b)).removeTarget(paramView); 
    return (TransitionSet)super.removeTarget(paramView);
  }
  
  public TransitionSet removeTarget(Class<?> paramClass) {
    for (byte b = 0; b < this.mTransitions.size(); b++)
      ((Transition)this.mTransitions.get(b)).removeTarget(paramClass); 
    return (TransitionSet)super.removeTarget(paramClass);
  }
  
  public TransitionSet removeTarget(String paramString) {
    for (byte b = 0; b < this.mTransitions.size(); b++)
      ((Transition)this.mTransitions.get(b)).removeTarget(paramString); 
    return (TransitionSet)super.removeTarget(paramString);
  }
  
  public TransitionSet removeTransition(Transition paramTransition) {
    this.mTransitions.remove(paramTransition);
    paramTransition.mParent = null;
    return this;
  }
  
  public void resume(View paramView) {
    super.resume(paramView);
    int i = this.mTransitions.size();
    for (byte b = 0; b < i; b++)
      ((Transition)this.mTransitions.get(b)).resume(paramView); 
  }
  
  protected void runAnimators() {
    if (this.mTransitions.isEmpty()) {
      start();
      end();
      return;
    } 
    setupStartEndListeners();
    if (!this.mPlayTogether) {
      for (byte b = 1; b < this.mTransitions.size(); b++) {
        ((Transition)this.mTransitions.get(b - 1)).addListener(new TransitionListenerAdapter() {
              final TransitionSet this$0;
              
              final Transition val$nextTransition;
              
              public void onTransitionEnd(Transition param1Transition) {
                nextTransition.runAnimators();
                param1Transition.removeListener(this);
              }
            });
      } 
      Transition transition = this.mTransitions.get(0);
      if (transition != null)
        transition.runAnimators(); 
    } else {
      Iterator<Transition> iterator = this.mTransitions.iterator();
      while (iterator.hasNext())
        ((Transition)iterator.next()).runAnimators(); 
    } 
  }
  
  void setCanRemoveViews(boolean paramBoolean) {
    super.setCanRemoveViews(paramBoolean);
    int i = this.mTransitions.size();
    for (byte b = 0; b < i; b++)
      ((Transition)this.mTransitions.get(b)).setCanRemoveViews(paramBoolean); 
  }
  
  public TransitionSet setDuration(long paramLong) {
    super.setDuration(paramLong);
    if (this.mDuration >= 0L) {
      ArrayList<Transition> arrayList = this.mTransitions;
      if (arrayList != null) {
        int i = arrayList.size();
        for (byte b = 0; b < i; b++)
          ((Transition)this.mTransitions.get(b)).setDuration(paramLong); 
      } 
    } 
    return this;
  }
  
  public void setEpicenterCallback(Transition.EpicenterCallback paramEpicenterCallback) {
    super.setEpicenterCallback(paramEpicenterCallback);
    this.mChangeFlags |= 0x8;
    int i = this.mTransitions.size();
    for (byte b = 0; b < i; b++)
      ((Transition)this.mTransitions.get(b)).setEpicenterCallback(paramEpicenterCallback); 
  }
  
  public TransitionSet setInterpolator(TimeInterpolator paramTimeInterpolator) {
    this.mChangeFlags |= 0x1;
    ArrayList<Transition> arrayList = this.mTransitions;
    if (arrayList != null) {
      int i = arrayList.size();
      for (byte b = 0; b < i; b++)
        ((Transition)this.mTransitions.get(b)).setInterpolator(paramTimeInterpolator); 
    } 
    return (TransitionSet)super.setInterpolator(paramTimeInterpolator);
  }
  
  public TransitionSet setOrdering(int paramInt) {
    switch (paramInt) {
      default:
        throw new AndroidRuntimeException("Invalid parameter for TransitionSet ordering: " + paramInt);
      case 1:
        this.mPlayTogether = false;
        return this;
      case 0:
        break;
    } 
    this.mPlayTogether = true;
    return this;
  }
  
  public void setPathMotion(PathMotion paramPathMotion) {
    super.setPathMotion(paramPathMotion);
    this.mChangeFlags |= 0x4;
    if (this.mTransitions != null)
      for (byte b = 0; b < this.mTransitions.size(); b++)
        ((Transition)this.mTransitions.get(b)).setPathMotion(paramPathMotion);  
  }
  
  public void setPropagation(TransitionPropagation paramTransitionPropagation) {
    super.setPropagation(paramTransitionPropagation);
    this.mChangeFlags |= 0x2;
    int i = this.mTransitions.size();
    for (byte b = 0; b < i; b++)
      ((Transition)this.mTransitions.get(b)).setPropagation(paramTransitionPropagation); 
  }
  
  TransitionSet setSceneRoot(ViewGroup paramViewGroup) {
    super.setSceneRoot(paramViewGroup);
    int i = this.mTransitions.size();
    for (byte b = 0; b < i; b++)
      ((Transition)this.mTransitions.get(b)).setSceneRoot(paramViewGroup); 
    return this;
  }
  
  public TransitionSet setStartDelay(long paramLong) {
    return (TransitionSet)super.setStartDelay(paramLong);
  }
  
  String toString(String paramString) {
    String str = super.toString(paramString);
    for (byte b = 0; b < this.mTransitions.size(); b++)
      str = str + "\n" + ((Transition)this.mTransitions.get(b)).toString(paramString + "  "); 
    return str;
  }
  
  static class TransitionSetListener extends TransitionListenerAdapter {
    TransitionSet mTransitionSet;
    
    TransitionSetListener(TransitionSet param1TransitionSet) {
      this.mTransitionSet = param1TransitionSet;
    }
    
    public void onTransitionEnd(Transition param1Transition) {
      TransitionSet transitionSet = this.mTransitionSet;
      transitionSet.mCurrentListeners--;
      if (this.mTransitionSet.mCurrentListeners == 0) {
        this.mTransitionSet.mStarted = false;
        this.mTransitionSet.end();
      } 
      param1Transition.removeListener(this);
    }
    
    public void onTransitionStart(Transition param1Transition) {
      if (!this.mTransitionSet.mStarted) {
        this.mTransitionSet.start();
        this.mTransitionSet.mStarted = true;
      } 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\TransitionSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */