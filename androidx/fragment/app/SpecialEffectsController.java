package androidx.fragment.app;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.os.CancellationSignal;
import androidx.core.view.ViewCompat;
import androidx.fragment.R;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

abstract class SpecialEffectsController {
  private final ViewGroup mContainer;
  
  boolean mIsContainerPostponed = false;
  
  boolean mOperationDirectionIsPop = false;
  
  final ArrayList<Operation> mPendingOperations = new ArrayList<>();
  
  final ArrayList<Operation> mRunningOperations = new ArrayList<>();
  
  SpecialEffectsController(ViewGroup paramViewGroup) {
    this.mContainer = paramViewGroup;
  }
  
  private void enqueue(Operation.State paramState, Operation.LifecycleImpact paramLifecycleImpact, FragmentStateManager paramFragmentStateManager) {
    synchronized (this.mPendingOperations) {
      CancellationSignal cancellationSignal = new CancellationSignal();
      this();
      Operation operation = findPendingOperation(paramFragmentStateManager.getFragment());
      if (operation != null) {
        operation.mergeWith(paramState, paramLifecycleImpact);
        return;
      } 
      operation = new FragmentStateManagerOperation();
      super(paramState, paramLifecycleImpact, paramFragmentStateManager, cancellationSignal);
      this.mPendingOperations.add(operation);
      Runnable runnable = new Runnable() {
          final SpecialEffectsController this$0;
          
          final SpecialEffectsController.FragmentStateManagerOperation val$operation;
          
          public void run() {
            if (SpecialEffectsController.this.mPendingOperations.contains(operation))
              operation.getFinalState().applyState((operation.getFragment()).mView); 
          }
        };
      super(this, (FragmentStateManagerOperation)operation);
      operation.addCompletionListener(runnable);
      runnable = new Runnable() {
          final SpecialEffectsController this$0;
          
          final SpecialEffectsController.FragmentStateManagerOperation val$operation;
          
          public void run() {
            SpecialEffectsController.this.mPendingOperations.remove(operation);
            SpecialEffectsController.this.mRunningOperations.remove(operation);
          }
        };
      super(this, (FragmentStateManagerOperation)operation);
      operation.addCompletionListener(runnable);
      return;
    } 
  }
  
  private Operation findPendingOperation(Fragment paramFragment) {
    for (Operation operation : this.mPendingOperations) {
      if (operation.getFragment().equals(paramFragment) && !operation.isCanceled())
        return operation; 
    } 
    return null;
  }
  
  private Operation findRunningOperation(Fragment paramFragment) {
    for (Operation operation : this.mRunningOperations) {
      if (operation.getFragment().equals(paramFragment) && !operation.isCanceled())
        return operation; 
    } 
    return null;
  }
  
  static SpecialEffectsController getOrCreateController(ViewGroup paramViewGroup, FragmentManager paramFragmentManager) {
    return getOrCreateController(paramViewGroup, paramFragmentManager.getSpecialEffectsControllerFactory());
  }
  
  static SpecialEffectsController getOrCreateController(ViewGroup paramViewGroup, SpecialEffectsControllerFactory paramSpecialEffectsControllerFactory) {
    Object object = paramViewGroup.getTag(R.id.special_effects_controller_view_tag);
    if (object instanceof SpecialEffectsController)
      return (SpecialEffectsController)object; 
    SpecialEffectsController specialEffectsController = paramSpecialEffectsControllerFactory.createController(paramViewGroup);
    paramViewGroup.setTag(R.id.special_effects_controller_view_tag, specialEffectsController);
    return specialEffectsController;
  }
  
  private void updateFinalState() {
    for (Operation operation : this.mPendingOperations) {
      if (operation.getLifecycleImpact() == Operation.LifecycleImpact.ADDING)
        operation.mergeWith(Operation.State.from(operation.getFragment().requireView().getVisibility()), Operation.LifecycleImpact.NONE); 
    } 
  }
  
  void enqueueAdd(Operation.State paramState, FragmentStateManager paramFragmentStateManager) {
    if (FragmentManager.isLoggingEnabled(2))
      Log.v("FragmentManager", "SpecialEffectsController: Enqueuing add operation for fragment " + paramFragmentStateManager.getFragment()); 
    enqueue(paramState, Operation.LifecycleImpact.ADDING, paramFragmentStateManager);
  }
  
  void enqueueHide(FragmentStateManager paramFragmentStateManager) {
    if (FragmentManager.isLoggingEnabled(2))
      Log.v("FragmentManager", "SpecialEffectsController: Enqueuing hide operation for fragment " + paramFragmentStateManager.getFragment()); 
    enqueue(Operation.State.GONE, Operation.LifecycleImpact.NONE, paramFragmentStateManager);
  }
  
  void enqueueRemove(FragmentStateManager paramFragmentStateManager) {
    if (FragmentManager.isLoggingEnabled(2))
      Log.v("FragmentManager", "SpecialEffectsController: Enqueuing remove operation for fragment " + paramFragmentStateManager.getFragment()); 
    enqueue(Operation.State.REMOVED, Operation.LifecycleImpact.REMOVING, paramFragmentStateManager);
  }
  
  void enqueueShow(FragmentStateManager paramFragmentStateManager) {
    if (FragmentManager.isLoggingEnabled(2))
      Log.v("FragmentManager", "SpecialEffectsController: Enqueuing show operation for fragment " + paramFragmentStateManager.getFragment()); 
    enqueue(Operation.State.VISIBLE, Operation.LifecycleImpact.NONE, paramFragmentStateManager);
  }
  
  abstract void executeOperations(List<Operation> paramList, boolean paramBoolean);
  
  void executePendingOperations() {
    if (this.mIsContainerPostponed)
      return; 
    if (!ViewCompat.isAttachedToWindow((View)this.mContainer)) {
      forceCompleteAllOperations();
      this.mOperationDirectionIsPop = false;
      return;
    } 
    synchronized (this.mPendingOperations) {
      if (!this.mPendingOperations.isEmpty()) {
        ArrayList<? extends Operation> arrayList = new ArrayList();
        this((Collection)this.mRunningOperations);
        this.mRunningOperations.clear();
        for (Operation operation : arrayList) {
          if (FragmentManager.isLoggingEnabled(2)) {
            StringBuilder stringBuilder = new StringBuilder();
            this();
            Log.v("FragmentManager", stringBuilder.append("SpecialEffectsController: Cancelling operation ").append(operation).toString());
          } 
          operation.cancel();
          if (!operation.isComplete())
            this.mRunningOperations.add(operation); 
        } 
        updateFinalState();
        arrayList = new ArrayList();
        this((Collection)this.mPendingOperations);
        this.mPendingOperations.clear();
        this.mRunningOperations.addAll(arrayList);
        Iterator<? extends Operation> iterator = arrayList.iterator();
        while (iterator.hasNext())
          ((Operation)iterator.next()).onStart(); 
        executeOperations((List)arrayList, this.mOperationDirectionIsPop);
        this.mOperationDirectionIsPop = false;
      } 
      return;
    } 
  }
  
  void forceCompleteAllOperations() {
    boolean bool = ViewCompat.isAttachedToWindow((View)this.mContainer);
    synchronized (this.mPendingOperations) {
      updateFinalState();
      Iterator<Operation> iterator = this.mPendingOperations.iterator();
      while (iterator.hasNext())
        ((Operation)iterator.next()).onStart(); 
      ArrayList arrayList = new ArrayList();
      this((Collection)this.mRunningOperations);
      for (Operation operation : arrayList) {
        if (FragmentManager.isLoggingEnabled(2)) {
          String str;
          StringBuilder stringBuilder1 = new StringBuilder();
          this();
          StringBuilder stringBuilder2 = stringBuilder1.append("SpecialEffectsController: ");
          if (bool) {
            str = "";
          } else {
            stringBuilder1 = new StringBuilder();
            this();
            str = stringBuilder1.append("Container ").append(this.mContainer).append(" is not attached to window. ").toString();
          } 
          Log.v("FragmentManager", stringBuilder2.append(str).append("Cancelling running operation ").append(operation).toString());
        } 
        operation.cancel();
      } 
      arrayList = new ArrayList();
      this((Collection)this.mPendingOperations);
      for (Operation operation : arrayList) {
        if (FragmentManager.isLoggingEnabled(2)) {
          String str;
          StringBuilder stringBuilder1 = new StringBuilder();
          this();
          StringBuilder stringBuilder2 = stringBuilder1.append("SpecialEffectsController: ");
          if (bool) {
            str = "";
          } else {
            stringBuilder1 = new StringBuilder();
            this();
            str = stringBuilder1.append("Container ").append(this.mContainer).append(" is not attached to window. ").toString();
          } 
          Log.v("FragmentManager", stringBuilder2.append(str).append("Cancelling pending operation ").append(operation).toString());
        } 
        operation.cancel();
      } 
      return;
    } 
  }
  
  void forcePostponedExecutePendingOperations() {
    if (this.mIsContainerPostponed) {
      this.mIsContainerPostponed = false;
      executePendingOperations();
    } 
  }
  
  Operation.LifecycleImpact getAwaitingCompletionLifecycleImpact(FragmentStateManager paramFragmentStateManager) {
    Operation.LifecycleImpact lifecycleImpact = null;
    Operation operation2 = findPendingOperation(paramFragmentStateManager.getFragment());
    if (operation2 != null)
      lifecycleImpact = operation2.getLifecycleImpact(); 
    Operation operation1 = findRunningOperation(paramFragmentStateManager.getFragment());
    return (operation1 != null && (lifecycleImpact == null || lifecycleImpact == Operation.LifecycleImpact.NONE)) ? operation1.getLifecycleImpact() : lifecycleImpact;
  }
  
  public ViewGroup getContainer() {
    return this.mContainer;
  }
  
  void markPostponedState() {
    synchronized (this.mPendingOperations) {
      updateFinalState();
      this.mIsContainerPostponed = false;
      for (int i = this.mPendingOperations.size() - 1; i >= 0; i--) {
        Operation operation = this.mPendingOperations.get(i);
        Operation.State state = Operation.State.from((operation.getFragment()).mView);
        if (operation.getFinalState() == Operation.State.VISIBLE && state != Operation.State.VISIBLE) {
          this.mIsContainerPostponed = operation.getFragment().isPostponed();
          break;
        } 
      } 
      return;
    } 
  }
  
  void updateOperationDirection(boolean paramBoolean) {
    this.mOperationDirectionIsPop = paramBoolean;
  }
  
  private static class FragmentStateManagerOperation extends Operation {
    private final FragmentStateManager mFragmentStateManager;
    
    FragmentStateManagerOperation(SpecialEffectsController.Operation.State param1State, SpecialEffectsController.Operation.LifecycleImpact param1LifecycleImpact, FragmentStateManager param1FragmentStateManager, CancellationSignal param1CancellationSignal) {
      super(param1State, param1LifecycleImpact, param1FragmentStateManager.getFragment(), param1CancellationSignal);
      this.mFragmentStateManager = param1FragmentStateManager;
    }
    
    public void complete() {
      super.complete();
      this.mFragmentStateManager.moveToExpectedState();
    }
    
    void onStart() {
      if (getLifecycleImpact() == SpecialEffectsController.Operation.LifecycleImpact.ADDING) {
        Fragment fragment = this.mFragmentStateManager.getFragment();
        View view = fragment.mView.findFocus();
        if (view != null) {
          fragment.setFocusedView(view);
          if (FragmentManager.isLoggingEnabled(2))
            Log.v("FragmentManager", "requestFocus: Saved focused view " + view + " for Fragment " + fragment); 
        } 
        view = getFragment().requireView();
        if (view.getParent() == null) {
          this.mFragmentStateManager.addViewToContainer();
          view.setAlpha(0.0F);
        } 
        if (view.getAlpha() == 0.0F && view.getVisibility() == 0)
          view.setVisibility(4); 
        view.setAlpha(fragment.getPostOnViewCreatedAlpha());
      } 
    }
  }
  
  static class Operation {
    private final List<Runnable> mCompletionListeners = new ArrayList<>();
    
    private State mFinalState;
    
    private final Fragment mFragment;
    
    private boolean mIsCanceled = false;
    
    private boolean mIsComplete = false;
    
    private LifecycleImpact mLifecycleImpact;
    
    private final HashSet<CancellationSignal> mSpecialEffectsSignals = new HashSet<>();
    
    Operation(State param1State, LifecycleImpact param1LifecycleImpact, Fragment param1Fragment, CancellationSignal param1CancellationSignal) {
      this.mFinalState = param1State;
      this.mLifecycleImpact = param1LifecycleImpact;
      this.mFragment = param1Fragment;
      param1CancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            final SpecialEffectsController.Operation this$0;
            
            public void onCancel() {
              SpecialEffectsController.Operation.this.cancel();
            }
          });
    }
    
    final void addCompletionListener(Runnable param1Runnable) {
      this.mCompletionListeners.add(param1Runnable);
    }
    
    final void cancel() {
      if (isCanceled())
        return; 
      this.mIsCanceled = true;
      if (this.mSpecialEffectsSignals.isEmpty()) {
        complete();
      } else {
        Iterator<?> iterator = (new ArrayList(this.mSpecialEffectsSignals)).iterator();
        while (iterator.hasNext())
          ((CancellationSignal)iterator.next()).cancel(); 
      } 
    }
    
    public void complete() {
      if (this.mIsComplete)
        return; 
      if (FragmentManager.isLoggingEnabled(2))
        Log.v("FragmentManager", "SpecialEffectsController: " + this + " has called complete."); 
      this.mIsComplete = true;
      Iterator<Runnable> iterator = this.mCompletionListeners.iterator();
      while (iterator.hasNext())
        ((Runnable)iterator.next()).run(); 
    }
    
    public final void completeSpecialEffect(CancellationSignal param1CancellationSignal) {
      if (this.mSpecialEffectsSignals.remove(param1CancellationSignal) && this.mSpecialEffectsSignals.isEmpty())
        complete(); 
    }
    
    public State getFinalState() {
      return this.mFinalState;
    }
    
    public final Fragment getFragment() {
      return this.mFragment;
    }
    
    LifecycleImpact getLifecycleImpact() {
      return this.mLifecycleImpact;
    }
    
    final boolean isCanceled() {
      return this.mIsCanceled;
    }
    
    final boolean isComplete() {
      return this.mIsComplete;
    }
    
    public final void markStartedSpecialEffect(CancellationSignal param1CancellationSignal) {
      onStart();
      this.mSpecialEffectsSignals.add(param1CancellationSignal);
    }
    
    final void mergeWith(State param1State, LifecycleImpact param1LifecycleImpact) {
      switch (param1LifecycleImpact) {
        default:
          return;
        case null:
          if (this.mFinalState != State.REMOVED) {
            if (FragmentManager.isLoggingEnabled(2))
              Log.v("FragmentManager", "SpecialEffectsController: For fragment " + this.mFragment + " mFinalState = " + this.mFinalState + " -> " + param1State + ". "); 
            this.mFinalState = param1State;
          } 
        case null:
          if (FragmentManager.isLoggingEnabled(2))
            Log.v("FragmentManager", "SpecialEffectsController: For fragment " + this.mFragment + " mFinalState = " + this.mFinalState + " -> REMOVED. mLifecycleImpact  = " + this.mLifecycleImpact + " to REMOVING."); 
          this.mFinalState = State.REMOVED;
          this.mLifecycleImpact = LifecycleImpact.REMOVING;
        case null:
          break;
      } 
      if (this.mFinalState == State.REMOVED) {
        if (FragmentManager.isLoggingEnabled(2))
          Log.v("FragmentManager", "SpecialEffectsController: For fragment " + this.mFragment + " mFinalState = REMOVED -> VISIBLE. mLifecycleImpact = " + this.mLifecycleImpact + " to ADDING."); 
        this.mFinalState = State.VISIBLE;
        this.mLifecycleImpact = LifecycleImpact.ADDING;
      } 
    }
    
    void onStart() {}
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Operation ");
      stringBuilder.append("{");
      stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      stringBuilder.append("} ");
      stringBuilder.append("{");
      stringBuilder.append("mFinalState = ");
      stringBuilder.append(this.mFinalState);
      stringBuilder.append("} ");
      stringBuilder.append("{");
      stringBuilder.append("mLifecycleImpact = ");
      stringBuilder.append(this.mLifecycleImpact);
      stringBuilder.append("} ");
      stringBuilder.append("{");
      stringBuilder.append("mFragment = ");
      stringBuilder.append(this.mFragment);
      stringBuilder.append("}");
      return stringBuilder.toString();
    }
    
    enum LifecycleImpact {
      ADDING, NONE, REMOVING;
      
      private static final LifecycleImpact[] $VALUES;
      
      static {
        LifecycleImpact lifecycleImpact2 = new LifecycleImpact("NONE", 0);
        NONE = lifecycleImpact2;
        LifecycleImpact lifecycleImpact3 = new LifecycleImpact("ADDING", 1);
        ADDING = lifecycleImpact3;
        LifecycleImpact lifecycleImpact1 = new LifecycleImpact("REMOVING", 2);
        REMOVING = lifecycleImpact1;
        $VALUES = new LifecycleImpact[] { lifecycleImpact2, lifecycleImpact3, lifecycleImpact1 };
      }
    }
    
    enum State {
      GONE, INVISIBLE, REMOVED, VISIBLE;
      
      private static final State[] $VALUES;
      
      static {
        State state2 = new State("REMOVED", 0);
        REMOVED = state2;
        State state1 = new State("VISIBLE", 1);
        VISIBLE = state1;
        State state3 = new State("GONE", 2);
        GONE = state3;
        State state4 = new State("INVISIBLE", 3);
        INVISIBLE = state4;
        $VALUES = new State[] { state2, state1, state3, state4 };
      }
      
      static State from(int param2Int) {
        switch (param2Int) {
          default:
            throw new IllegalArgumentException("Unknown visibility " + param2Int);
          case 8:
            return GONE;
          case 4:
            return INVISIBLE;
          case 0:
            break;
        } 
        return VISIBLE;
      }
      
      static State from(View param2View) {
        return (param2View.getAlpha() == 0.0F && param2View.getVisibility() == 0) ? INVISIBLE : from(param2View.getVisibility());
      }
      
      void applyState(View param2View) {
        switch (this) {
          default:
            return;
          case null:
            if (FragmentManager.isLoggingEnabled(2))
              Log.v("FragmentManager", "SpecialEffectsController: Setting view " + param2View + " to INVISIBLE"); 
            param2View.setVisibility(4);
          case null:
            if (FragmentManager.isLoggingEnabled(2))
              Log.v("FragmentManager", "SpecialEffectsController: Setting view " + param2View + " to GONE"); 
            param2View.setVisibility(8);
          case null:
            if (FragmentManager.isLoggingEnabled(2))
              Log.v("FragmentManager", "SpecialEffectsController: Setting view " + param2View + " to VISIBLE"); 
            param2View.setVisibility(0);
          case null:
            break;
        } 
        ViewGroup viewGroup = (ViewGroup)param2View.getParent();
        if (viewGroup != null) {
          if (FragmentManager.isLoggingEnabled(2))
            Log.v("FragmentManager", "SpecialEffectsController: Removing view " + param2View + " from container " + viewGroup); 
          viewGroup.removeView(param2View);
        } 
      }
    }
  }
  
  class null implements CancellationSignal.OnCancelListener {
    final SpecialEffectsController.Operation this$0;
    
    public void onCancel() {
      this.this$0.cancel();
    }
  }
  
  enum LifecycleImpact {
    ADDING, NONE, REMOVING;
    
    private static final LifecycleImpact[] $VALUES;
    
    static {
      LifecycleImpact lifecycleImpact2 = new LifecycleImpact("NONE", 0);
      NONE = lifecycleImpact2;
      LifecycleImpact lifecycleImpact3 = new LifecycleImpact("ADDING", 1);
      ADDING = lifecycleImpact3;
      LifecycleImpact lifecycleImpact1 = new LifecycleImpact("REMOVING", 2);
      REMOVING = lifecycleImpact1;
      $VALUES = new LifecycleImpact[] { lifecycleImpact2, lifecycleImpact3, lifecycleImpact1 };
    }
  }
  
  enum State {
    GONE, INVISIBLE, REMOVED, VISIBLE;
    
    private static final State[] $VALUES;
    
    static {
      State state2 = new State("REMOVED", 0);
      REMOVED = state2;
      State state1 = new State("VISIBLE", 1);
      VISIBLE = state1;
      State state3 = new State("GONE", 2);
      GONE = state3;
      State state4 = new State("INVISIBLE", 3);
      INVISIBLE = state4;
      $VALUES = new State[] { state2, state1, state3, state4 };
    }
    
    static State from(int param1Int) {
      switch (param1Int) {
        default:
          throw new IllegalArgumentException("Unknown visibility " + param1Int);
        case 8:
          return GONE;
        case 4:
          return INVISIBLE;
        case 0:
          break;
      } 
      return VISIBLE;
    }
    
    static State from(View param1View) {
      return (param1View.getAlpha() == 0.0F && param1View.getVisibility() == 0) ? INVISIBLE : from(param1View.getVisibility());
    }
    
    void applyState(View param1View) {
      switch (this) {
        default:
          return;
        case null:
          if (FragmentManager.isLoggingEnabled(2))
            Log.v("FragmentManager", "SpecialEffectsController: Setting view " + param1View + " to INVISIBLE"); 
          param1View.setVisibility(4);
        case null:
          if (FragmentManager.isLoggingEnabled(2))
            Log.v("FragmentManager", "SpecialEffectsController: Setting view " + param1View + " to GONE"); 
          param1View.setVisibility(8);
        case null:
          if (FragmentManager.isLoggingEnabled(2))
            Log.v("FragmentManager", "SpecialEffectsController: Setting view " + param1View + " to VISIBLE"); 
          param1View.setVisibility(0);
        case null:
          break;
      } 
      ViewGroup viewGroup = (ViewGroup)param1View.getParent();
      if (viewGroup != null) {
        if (FragmentManager.isLoggingEnabled(2))
          Log.v("FragmentManager", "SpecialEffectsController: Removing view " + param1View + " from container " + viewGroup); 
        viewGroup.removeView(param1View);
      } 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\fragment\app\SpecialEffectsController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */