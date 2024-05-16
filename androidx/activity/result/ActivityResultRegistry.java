package androidx.activity.result;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class ActivityResultRegistry {
  private static final int INITIAL_REQUEST_CODE_VALUE = 65536;
  
  private static final String KEY_COMPONENT_ACTIVITY_LAUNCHED_KEYS = "KEY_COMPONENT_ACTIVITY_LAUNCHED_KEYS";
  
  private static final String KEY_COMPONENT_ACTIVITY_PENDING_RESULTS = "KEY_COMPONENT_ACTIVITY_PENDING_RESULT";
  
  private static final String KEY_COMPONENT_ACTIVITY_RANDOM_OBJECT = "KEY_COMPONENT_ACTIVITY_RANDOM_OBJECT";
  
  private static final String KEY_COMPONENT_ACTIVITY_REGISTERED_KEYS = "KEY_COMPONENT_ACTIVITY_REGISTERED_KEYS";
  
  private static final String KEY_COMPONENT_ACTIVITY_REGISTERED_RCS = "KEY_COMPONENT_ACTIVITY_REGISTERED_RCS";
  
  private static final String LOG_TAG = "ActivityResultRegistry";
  
  final transient Map<String, CallbackAndContract<?>> mKeyToCallback = new HashMap<>();
  
  private final Map<String, LifecycleContainer> mKeyToLifecycleContainers = new HashMap<>();
  
  final Map<String, Integer> mKeyToRc = new HashMap<>();
  
  ArrayList<String> mLaunchedKeys = new ArrayList<>();
  
  final Map<String, Object> mParsedPendingResults = new HashMap<>();
  
  final Bundle mPendingResults = new Bundle();
  
  private Random mRandom = new Random();
  
  private final Map<Integer, String> mRcToKey = new HashMap<>();
  
  private void bindRcKey(int paramInt, String paramString) {
    this.mRcToKey.put(Integer.valueOf(paramInt), paramString);
    this.mKeyToRc.put(paramString, Integer.valueOf(paramInt));
  }
  
  private <O> void doDispatch(String paramString, int paramInt, Intent paramIntent, CallbackAndContract<O> paramCallbackAndContract) {
    if (paramCallbackAndContract != null && paramCallbackAndContract.mCallback != null) {
      paramCallbackAndContract.mCallback.onActivityResult((O)paramCallbackAndContract.mContract.parseResult(paramInt, paramIntent));
    } else {
      this.mParsedPendingResults.remove(paramString);
      this.mPendingResults.putParcelable(paramString, new ActivityResult(paramInt, paramIntent));
    } 
  }
  
  private int generateRandomNumber() {
    int i;
    for (i = this.mRandom.nextInt(2147418112) + 65536; this.mRcToKey.containsKey(Integer.valueOf(i)); i = this.mRandom.nextInt(2147418112) + 65536);
    return i;
  }
  
  private int registerKey(String paramString) {
    Integer integer = this.mKeyToRc.get(paramString);
    if (integer != null)
      return integer.intValue(); 
    int i = generateRandomNumber();
    bindRcKey(i, paramString);
    return i;
  }
  
  public final boolean dispatchResult(int paramInt1, int paramInt2, Intent paramIntent) {
    String str = this.mRcToKey.get(Integer.valueOf(paramInt1));
    if (str == null)
      return false; 
    this.mLaunchedKeys.remove(str);
    doDispatch(str, paramInt2, paramIntent, this.mKeyToCallback.get(str));
    return true;
  }
  
  public final <O> boolean dispatchResult(int paramInt, O paramO) {
    String str = this.mRcToKey.get(Integer.valueOf(paramInt));
    if (str == null)
      return false; 
    this.mLaunchedKeys.remove(str);
    CallbackAndContract callbackAndContract = this.mKeyToCallback.get(str);
    if (callbackAndContract == null || callbackAndContract.mCallback == null) {
      this.mPendingResults.remove(str);
      this.mParsedPendingResults.put(str, paramO);
      return true;
    } 
    callbackAndContract.mCallback.onActivityResult(paramO);
    return true;
  }
  
  public abstract <I, O> void onLaunch(int paramInt, ActivityResultContract<I, O> paramActivityResultContract, I paramI, ActivityOptionsCompat paramActivityOptionsCompat);
  
  public final void onRestoreInstanceState(Bundle paramBundle) {
    if (paramBundle == null)
      return; 
    ArrayList<Integer> arrayList = paramBundle.getIntegerArrayList("KEY_COMPONENT_ACTIVITY_REGISTERED_RCS");
    ArrayList<String> arrayList1 = paramBundle.getStringArrayList("KEY_COMPONENT_ACTIVITY_REGISTERED_KEYS");
    if (arrayList1 == null || arrayList == null)
      return; 
    this.mLaunchedKeys = paramBundle.getStringArrayList("KEY_COMPONENT_ACTIVITY_LAUNCHED_KEYS");
    this.mRandom = (Random)paramBundle.getSerializable("KEY_COMPONENT_ACTIVITY_RANDOM_OBJECT");
    this.mPendingResults.putAll(paramBundle.getBundle("KEY_COMPONENT_ACTIVITY_PENDING_RESULT"));
    for (byte b = 0; b < arrayList1.size(); b++) {
      String str = arrayList1.get(b);
      if (this.mKeyToRc.containsKey(str)) {
        Integer integer = this.mKeyToRc.remove(str);
        if (!this.mPendingResults.containsKey(str))
          this.mRcToKey.remove(integer); 
      } 
      bindRcKey(((Integer)arrayList.get(b)).intValue(), arrayList1.get(b));
    } 
  }
  
  public final void onSaveInstanceState(Bundle paramBundle) {
    paramBundle.putIntegerArrayList("KEY_COMPONENT_ACTIVITY_REGISTERED_RCS", new ArrayList(this.mKeyToRc.values()));
    paramBundle.putStringArrayList("KEY_COMPONENT_ACTIVITY_REGISTERED_KEYS", new ArrayList(this.mKeyToRc.keySet()));
    paramBundle.putStringArrayList("KEY_COMPONENT_ACTIVITY_LAUNCHED_KEYS", new ArrayList<>(this.mLaunchedKeys));
    paramBundle.putBundle("KEY_COMPONENT_ACTIVITY_PENDING_RESULT", (Bundle)this.mPendingResults.clone());
    paramBundle.putSerializable("KEY_COMPONENT_ACTIVITY_RANDOM_OBJECT", this.mRandom);
  }
  
  public final <I, O> ActivityResultLauncher<I> register(final String key, final ActivityResultContract<I, O> contract, ActivityResultCallback<O> paramActivityResultCallback) {
    final int requestCode = registerKey(key);
    this.mKeyToCallback.put(key, new CallbackAndContract(paramActivityResultCallback, contract));
    if (this.mParsedPendingResults.containsKey(key)) {
      Object object = this.mParsedPendingResults.get(key);
      this.mParsedPendingResults.remove(key);
      paramActivityResultCallback.onActivityResult((O)object);
    } 
    ActivityResult activityResult = (ActivityResult)this.mPendingResults.getParcelable(key);
    if (activityResult != null) {
      this.mPendingResults.remove(key);
      paramActivityResultCallback.onActivityResult((O)contract.parseResult(activityResult.getResultCode(), activityResult.getData()));
    } 
    return new ActivityResultLauncher<I>() {
        final ActivityResultRegistry this$0;
        
        final ActivityResultContract val$contract;
        
        final String val$key;
        
        final int val$requestCode;
        
        public ActivityResultContract<I, ?> getContract() {
          return contract;
        }
        
        public void launch(I param1I, ActivityOptionsCompat param1ActivityOptionsCompat) {
          int i;
          ActivityResultRegistry.this.mLaunchedKeys.add(key);
          Integer integer = ActivityResultRegistry.this.mKeyToRc.get(key);
          ActivityResultRegistry activityResultRegistry = ActivityResultRegistry.this;
          if (integer != null) {
            i = integer.intValue();
          } else {
            i = requestCode;
          } 
          activityResultRegistry.onLaunch(i, contract, param1I, param1ActivityOptionsCompat);
        }
        
        public void unregister() {
          ActivityResultRegistry.this.unregister(key);
        }
      };
  }
  
  public final <I, O> ActivityResultLauncher<I> register(final String key, LifecycleOwner paramLifecycleOwner, final ActivityResultContract<I, O> contract, final ActivityResultCallback<O> callback) {
    LifecycleContainer lifecycleContainer;
    Lifecycle lifecycle = paramLifecycleOwner.getLifecycle();
    if (!lifecycle.getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
      final int requestCode = registerKey(key);
      LifecycleContainer lifecycleContainer1 = this.mKeyToLifecycleContainers.get(key);
      lifecycleContainer = lifecycleContainer1;
      if (lifecycleContainer1 == null)
        lifecycleContainer = new LifecycleContainer(lifecycle); 
      lifecycleContainer.addObserver(new LifecycleEventObserver() {
            final ActivityResultRegistry this$0;
            
            final ActivityResultCallback val$callback;
            
            final ActivityResultContract val$contract;
            
            final String val$key;
            
            public void onStateChanged(LifecycleOwner param1LifecycleOwner, Lifecycle.Event param1Event) {
              if (Lifecycle.Event.ON_START.equals(param1Event)) {
                ActivityResultRegistry.this.mKeyToCallback.put(key, new ActivityResultRegistry.CallbackAndContract(callback, contract));
                if (ActivityResultRegistry.this.mParsedPendingResults.containsKey(key)) {
                  param1LifecycleOwner = (LifecycleOwner)ActivityResultRegistry.this.mParsedPendingResults.get(key);
                  ActivityResultRegistry.this.mParsedPendingResults.remove(key);
                  callback.onActivityResult(param1LifecycleOwner);
                } 
                ActivityResult activityResult = (ActivityResult)ActivityResultRegistry.this.mPendingResults.getParcelable(key);
                if (activityResult != null) {
                  ActivityResultRegistry.this.mPendingResults.remove(key);
                  callback.onActivityResult(contract.parseResult(activityResult.getResultCode(), activityResult.getData()));
                } 
              } else if (Lifecycle.Event.ON_STOP.equals(param1Event)) {
                ActivityResultRegistry.this.mKeyToCallback.remove(key);
              } else if (Lifecycle.Event.ON_DESTROY.equals(param1Event)) {
                ActivityResultRegistry.this.unregister(key);
              } 
            }
          });
      this.mKeyToLifecycleContainers.put(key, lifecycleContainer);
      return new ActivityResultLauncher<I>() {
          final ActivityResultRegistry this$0;
          
          final ActivityResultContract val$contract;
          
          final String val$key;
          
          final int val$requestCode;
          
          public ActivityResultContract<I, ?> getContract() {
            return contract;
          }
          
          public void launch(I param1I, ActivityOptionsCompat param1ActivityOptionsCompat) {
            int i;
            ActivityResultRegistry.this.mLaunchedKeys.add(key);
            Integer integer = ActivityResultRegistry.this.mKeyToRc.get(key);
            ActivityResultRegistry activityResultRegistry = ActivityResultRegistry.this;
            if (integer != null) {
              i = integer.intValue();
            } else {
              i = requestCode;
            } 
            activityResultRegistry.onLaunch(i, contract, param1I, param1ActivityOptionsCompat);
          }
          
          public void unregister() {
            ActivityResultRegistry.this.unregister(key);
          }
        };
    } 
    throw new IllegalStateException("LifecycleOwner " + lifecycleContainer + " is attempting to register while current state is " + lifecycle.getCurrentState() + ". LifecycleOwners must call register before they are STARTED.");
  }
  
  final void unregister(String paramString) {
    if (!this.mLaunchedKeys.contains(paramString)) {
      Integer integer = this.mKeyToRc.remove(paramString);
      if (integer != null)
        this.mRcToKey.remove(integer); 
    } 
    this.mKeyToCallback.remove(paramString);
    if (this.mParsedPendingResults.containsKey(paramString)) {
      Log.w("ActivityResultRegistry", "Dropping pending result for request " + paramString + ": " + this.mParsedPendingResults.get(paramString));
      this.mParsedPendingResults.remove(paramString);
    } 
    if (this.mPendingResults.containsKey(paramString)) {
      Log.w("ActivityResultRegistry", "Dropping pending result for request " + paramString + ": " + this.mPendingResults.getParcelable(paramString));
      this.mPendingResults.remove(paramString);
    } 
    LifecycleContainer lifecycleContainer = this.mKeyToLifecycleContainers.get(paramString);
    if (lifecycleContainer != null) {
      lifecycleContainer.clearObservers();
      this.mKeyToLifecycleContainers.remove(paramString);
    } 
  }
  
  private static class CallbackAndContract<O> {
    final ActivityResultCallback<O> mCallback;
    
    final ActivityResultContract<?, O> mContract;
    
    CallbackAndContract(ActivityResultCallback<O> param1ActivityResultCallback, ActivityResultContract<?, O> param1ActivityResultContract) {
      this.mCallback = param1ActivityResultCallback;
      this.mContract = param1ActivityResultContract;
    }
  }
  
  private static class LifecycleContainer {
    final Lifecycle mLifecycle;
    
    private final ArrayList<LifecycleEventObserver> mObservers;
    
    LifecycleContainer(Lifecycle param1Lifecycle) {
      this.mLifecycle = param1Lifecycle;
      this.mObservers = new ArrayList<>();
    }
    
    void addObserver(LifecycleEventObserver param1LifecycleEventObserver) {
      this.mLifecycle.addObserver((LifecycleObserver)param1LifecycleEventObserver);
      this.mObservers.add(param1LifecycleEventObserver);
    }
    
    void clearObservers() {
      for (LifecycleEventObserver lifecycleEventObserver : this.mObservers)
        this.mLifecycle.removeObserver((LifecycleObserver)lifecycleEventObserver); 
      this.mObservers.clear();
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\activity\result\ActivityResultRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */