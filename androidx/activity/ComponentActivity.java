package androidx.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.activity.contextaware.ContextAware;
import androidx.activity.contextaware.ContextAwareHelper;
import androidx.activity.contextaware.OnContextAvailableListener;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.ActivityResultRegistryOwner;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.ComponentActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ReportFragment;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.lifecycle.ViewTreeLifecycleOwner;
import androidx.lifecycle.ViewTreeViewModelStoreOwner;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryController;
import androidx.savedstate.SavedStateRegistryOwner;
import androidx.savedstate.ViewTreeSavedStateRegistryOwner;
import androidx.tracing.Trace;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class ComponentActivity extends ComponentActivity implements ContextAware, LifecycleOwner, ViewModelStoreOwner, HasDefaultViewModelProviderFactory, SavedStateRegistryOwner, OnBackPressedDispatcherOwner, ActivityResultRegistryOwner, ActivityResultCaller {
  private static final String ACTIVITY_RESULT_TAG = "android:support:activity-result";
  
  private final ActivityResultRegistry mActivityResultRegistry = new ActivityResultRegistry() {
      final ComponentActivity this$0;
      
      public <I, O> void onLaunch(final int requestCode, ActivityResultContract<I, O> param1ActivityResultContract, I param1I, ActivityOptionsCompat param1ActivityOptionsCompat) {
        String[] arrayOfString1;
        String[] arrayOfString2;
        ComponentActivity componentActivity = ComponentActivity.this;
        final ActivityResultContract.SynchronousResult synchronousResult = param1ActivityResultContract.getSynchronousResult((Context)componentActivity, param1I);
        if (synchronousResult != null) {
          (new Handler(Looper.getMainLooper())).post(new Runnable() {
                final ComponentActivity.null this$1;
                
                final int val$requestCode;
                
                final ActivityResultContract.SynchronousResult val$synchronousResult;
                
                public void run() {
                  ComponentActivity.null.this.dispatchResult(requestCode, synchronousResult.getValue());
                }
              });
          return;
        } 
        Intent intent = param1ActivityResultContract.createIntent((Context)componentActivity, param1I);
        if (intent.getExtras() != null && intent.getExtras().getClassLoader() == null)
          intent.setExtrasClassLoader(componentActivity.getClassLoader()); 
        if (intent.hasExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE")) {
          Bundle bundle = intent.getBundleExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE");
          intent.removeExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE");
        } else if (param1ActivityOptionsCompat != null) {
          Bundle bundle = param1ActivityOptionsCompat.toBundle();
        } else {
          param1ActivityResultContract = null;
        } 
        if ("androidx.activity.result.contract.action.REQUEST_PERMISSIONS".equals(intent.getAction())) {
          arrayOfString2 = intent.getStringArrayExtra("androidx.activity.result.contract.extra.PERMISSIONS");
          arrayOfString1 = arrayOfString2;
          if (arrayOfString2 == null)
            arrayOfString1 = new String[0]; 
          ActivityCompat.requestPermissions((Activity)componentActivity, arrayOfString1, requestCode);
        } else {
          Intent intent1;
          if ("androidx.activity.result.contract.action.INTENT_SENDER_REQUEST".equals(arrayOfString2.getAction())) {
            IntentSenderRequest intentSenderRequest = (IntentSenderRequest)arrayOfString2.getParcelableExtra("androidx.activity.result.contract.extra.INTENT_SENDER_REQUEST");
            try {
              IntentSender intentSender = intentSenderRequest.getIntentSender();
              intent1 = intentSenderRequest.getFillInIntent();
              int i = intentSenderRequest.getFlagsMask();
              int j = intentSenderRequest.getFlagsValues();
              try {
                ActivityCompat.startIntentSenderForResult((Activity)componentActivity, intentSender, requestCode, intent1, i, j, 0, (Bundle)arrayOfString1);
              } catch (android.content.IntentSender.SendIntentException null) {}
            } catch (android.content.IntentSender.SendIntentException sendIntentException) {}
            (new Handler(Looper.getMainLooper())).post(new Runnable() {
                  final ComponentActivity.null this$1;
                  
                  final IntentSender.SendIntentException val$e;
                  
                  final int val$requestCode;
                  
                  public void run() {
                    ComponentActivity.null.this.dispatchResult(requestCode, 0, (new Intent()).setAction("androidx.activity.result.contract.action.INTENT_SENDER_REQUEST").putExtra("androidx.activity.result.contract.extra.SEND_INTENT_EXCEPTION", (Serializable)e));
                  }
                });
          } 
          ActivityCompat.startActivityForResult((Activity)componentActivity, intent1, requestCode, (Bundle)sendIntentException);
        } 
      }
    };
  
  private int mContentLayoutId;
  
  final ContextAwareHelper mContextAwareHelper = new ContextAwareHelper();
  
  private ViewModelProvider.Factory mDefaultFactory;
  
  private final LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
  
  private final AtomicInteger mNextLocalRequestCode = new AtomicInteger();
  
  private final OnBackPressedDispatcher mOnBackPressedDispatcher = new OnBackPressedDispatcher(new Runnable() {
        final ComponentActivity this$0;
        
        public void run() {
          try {
            ComponentActivity.this.onBackPressed();
          } catch (IllegalStateException illegalStateException) {
            if (!TextUtils.equals(illegalStateException.getMessage(), "Can not perform this action after onSaveInstanceState"))
              throw illegalStateException; 
          } 
        }
      });
  
  final SavedStateRegistryController mSavedStateRegistryController = SavedStateRegistryController.create(this);
  
  private ViewModelStore mViewModelStore;
  
  public ComponentActivity() {
    if (getLifecycle() != null) {
      if (Build.VERSION.SDK_INT >= 19)
        getLifecycle().addObserver((LifecycleObserver)new LifecycleEventObserver() {
              final ComponentActivity this$0;
              
              public void onStateChanged(LifecycleOwner param1LifecycleOwner, Lifecycle.Event param1Event) {
                if (param1Event == Lifecycle.Event.ON_STOP) {
                  Window window = ComponentActivity.this.getWindow();
                  if (window != null) {
                    View view = window.peekDecorView();
                  } else {
                    window = null;
                  } 
                  if (window != null)
                    window.cancelPendingInputEvents(); 
                } 
              }
            }); 
      getLifecycle().addObserver((LifecycleObserver)new LifecycleEventObserver() {
            final ComponentActivity this$0;
            
            public void onStateChanged(LifecycleOwner param1LifecycleOwner, Lifecycle.Event param1Event) {
              if (param1Event == Lifecycle.Event.ON_DESTROY) {
                ComponentActivity.this.mContextAwareHelper.clearAvailableContext();
                if (!ComponentActivity.this.isChangingConfigurations())
                  ComponentActivity.this.getViewModelStore().clear(); 
              } 
            }
          });
      getLifecycle().addObserver((LifecycleObserver)new LifecycleEventObserver() {
            final ComponentActivity this$0;
            
            public void onStateChanged(LifecycleOwner param1LifecycleOwner, Lifecycle.Event param1Event) {
              ComponentActivity.this.ensureViewModelStore();
              ComponentActivity.this.getLifecycle().removeObserver((LifecycleObserver)this);
            }
          });
      if (19 <= Build.VERSION.SDK_INT && Build.VERSION.SDK_INT <= 23)
        getLifecycle().addObserver((LifecycleObserver)new ImmLeaksCleaner((Activity)this)); 
      getSavedStateRegistry().registerSavedStateProvider("android:support:activity-result", new SavedStateRegistry.SavedStateProvider() {
            final ComponentActivity this$0;
            
            public Bundle saveState() {
              Bundle bundle = new Bundle();
              ComponentActivity.this.mActivityResultRegistry.onSaveInstanceState(bundle);
              return bundle;
            }
          });
      addOnContextAvailableListener(new OnContextAvailableListener() {
            final ComponentActivity this$0;
            
            public void onContextAvailable(Context param1Context) {
              Bundle bundle = ComponentActivity.this.getSavedStateRegistry().consumeRestoredStateForKey("android:support:activity-result");
              if (bundle != null)
                ComponentActivity.this.mActivityResultRegistry.onRestoreInstanceState(bundle); 
            }
          });
      return;
    } 
    throw new IllegalStateException("getLifecycle() returned null in ComponentActivity's constructor. Please make sure you are lazily constructing your Lifecycle in the first call to getLifecycle() rather than relying on field initialization.");
  }
  
  public ComponentActivity(int paramInt) {
    this();
    this.mContentLayoutId = paramInt;
  }
  
  private void initViewTreeOwners() {
    ViewTreeLifecycleOwner.set(getWindow().getDecorView(), this);
    ViewTreeViewModelStoreOwner.set(getWindow().getDecorView(), this);
    ViewTreeSavedStateRegistryOwner.set(getWindow().getDecorView(), this);
  }
  
  public void addContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    initViewTreeOwners();
    super.addContentView(paramView, paramLayoutParams);
  }
  
  public final void addOnContextAvailableListener(OnContextAvailableListener paramOnContextAvailableListener) {
    this.mContextAwareHelper.addOnContextAvailableListener(paramOnContextAvailableListener);
  }
  
  void ensureViewModelStore() {
    if (this.mViewModelStore == null) {
      NonConfigurationInstances nonConfigurationInstances = (NonConfigurationInstances)getLastNonConfigurationInstance();
      if (nonConfigurationInstances != null)
        this.mViewModelStore = nonConfigurationInstances.viewModelStore; 
      if (this.mViewModelStore == null)
        this.mViewModelStore = new ViewModelStore(); 
    } 
  }
  
  public final ActivityResultRegistry getActivityResultRegistry() {
    return this.mActivityResultRegistry;
  }
  
  public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
    if (getApplication() != null) {
      if (this.mDefaultFactory == null) {
        Bundle bundle;
        Application application = getApplication();
        if (getIntent() != null) {
          bundle = getIntent().getExtras();
        } else {
          bundle = null;
        } 
        this.mDefaultFactory = (ViewModelProvider.Factory)new SavedStateViewModelFactory(application, this, bundle);
      } 
      return this.mDefaultFactory;
    } 
    throw new IllegalStateException("Your activity is not yet attached to the Application instance. You can't request ViewModel before onCreate call.");
  }
  
  @Deprecated
  public Object getLastCustomNonConfigurationInstance() {
    NonConfigurationInstances nonConfigurationInstances = (NonConfigurationInstances)getLastNonConfigurationInstance();
    if (nonConfigurationInstances != null) {
      Object object = nonConfigurationInstances.custom;
    } else {
      nonConfigurationInstances = null;
    } 
    return nonConfigurationInstances;
  }
  
  public Lifecycle getLifecycle() {
    return (Lifecycle)this.mLifecycleRegistry;
  }
  
  public final OnBackPressedDispatcher getOnBackPressedDispatcher() {
    return this.mOnBackPressedDispatcher;
  }
  
  public final SavedStateRegistry getSavedStateRegistry() {
    return this.mSavedStateRegistryController.getSavedStateRegistry();
  }
  
  public ViewModelStore getViewModelStore() {
    if (getApplication() != null) {
      ensureViewModelStore();
      return this.mViewModelStore;
    } 
    throw new IllegalStateException("Your activity is not yet attached to the Application instance. You can't request ViewModel before onCreate call.");
  }
  
  @Deprecated
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    if (!this.mActivityResultRegistry.dispatchResult(paramInt1, paramInt2, paramIntent))
      super.onActivityResult(paramInt1, paramInt2, paramIntent); 
  }
  
  public void onBackPressed() {
    this.mOnBackPressedDispatcher.onBackPressed();
  }
  
  protected void onCreate(Bundle paramBundle) {
    this.mSavedStateRegistryController.performRestore(paramBundle);
    this.mContextAwareHelper.dispatchOnContextAvailable((Context)this);
    super.onCreate(paramBundle);
    ReportFragment.injectIfNeededIn((Activity)this);
    int i = this.mContentLayoutId;
    if (i != 0)
      setContentView(i); 
  }
  
  @Deprecated
  public void onRequestPermissionsResult(int paramInt, String[] paramArrayOfString, int[] paramArrayOfint) {
    if (!this.mActivityResultRegistry.dispatchResult(paramInt, -1, (new Intent()).putExtra("androidx.activity.result.contract.extra.PERMISSIONS", paramArrayOfString).putExtra("androidx.activity.result.contract.extra.PERMISSION_GRANT_RESULTS", paramArrayOfint)) && Build.VERSION.SDK_INT >= 23)
      super.onRequestPermissionsResult(paramInt, paramArrayOfString, paramArrayOfint); 
  }
  
  @Deprecated
  public Object onRetainCustomNonConfigurationInstance() {
    return null;
  }
  
  public final Object onRetainNonConfigurationInstance() {
    Object object = onRetainCustomNonConfigurationInstance();
    ViewModelStore viewModelStore2 = this.mViewModelStore;
    ViewModelStore viewModelStore1 = viewModelStore2;
    if (viewModelStore2 == null) {
      NonConfigurationInstances nonConfigurationInstances1 = (NonConfigurationInstances)getLastNonConfigurationInstance();
      viewModelStore1 = viewModelStore2;
      if (nonConfigurationInstances1 != null)
        viewModelStore1 = nonConfigurationInstances1.viewModelStore; 
    } 
    if (viewModelStore1 == null && object == null)
      return null; 
    NonConfigurationInstances nonConfigurationInstances = new NonConfigurationInstances();
    nonConfigurationInstances.custom = object;
    nonConfigurationInstances.viewModelStore = viewModelStore1;
    return nonConfigurationInstances;
  }
  
  protected void onSaveInstanceState(Bundle paramBundle) {
    Lifecycle lifecycle = getLifecycle();
    if (lifecycle instanceof LifecycleRegistry)
      ((LifecycleRegistry)lifecycle).setCurrentState(Lifecycle.State.CREATED); 
    super.onSaveInstanceState(paramBundle);
    this.mSavedStateRegistryController.performSave(paramBundle);
  }
  
  public Context peekAvailableContext() {
    return this.mContextAwareHelper.peekAvailableContext();
  }
  
  public final <I, O> ActivityResultLauncher<I> registerForActivityResult(ActivityResultContract<I, O> paramActivityResultContract, ActivityResultCallback<O> paramActivityResultCallback) {
    return registerForActivityResult(paramActivityResultContract, this.mActivityResultRegistry, paramActivityResultCallback);
  }
  
  public final <I, O> ActivityResultLauncher<I> registerForActivityResult(ActivityResultContract<I, O> paramActivityResultContract, ActivityResultRegistry paramActivityResultRegistry, ActivityResultCallback<O> paramActivityResultCallback) {
    return paramActivityResultRegistry.register("activity_rq#" + this.mNextLocalRequestCode.getAndIncrement(), this, paramActivityResultContract, paramActivityResultCallback);
  }
  
  public final void removeOnContextAvailableListener(OnContextAvailableListener paramOnContextAvailableListener) {
    this.mContextAwareHelper.removeOnContextAvailableListener(paramOnContextAvailableListener);
  }
  
  public void reportFullyDrawn() {
    try {
      if (Trace.isEnabled()) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        Trace.beginSection(stringBuilder.append("reportFullyDrawn() for ").append(getComponentName()).toString());
      } 
      if (Build.VERSION.SDK_INT > 19) {
        super.reportFullyDrawn();
      } else if (Build.VERSION.SDK_INT == 19 && ContextCompat.checkSelfPermission((Context)this, "android.permission.UPDATE_DEVICE_STATS") == 0) {
        super.reportFullyDrawn();
      } 
      return;
    } finally {
      Trace.endSection();
    } 
  }
  
  public void setContentView(int paramInt) {
    initViewTreeOwners();
    super.setContentView(paramInt);
  }
  
  public void setContentView(View paramView) {
    initViewTreeOwners();
    super.setContentView(paramView);
  }
  
  public void setContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    initViewTreeOwners();
    super.setContentView(paramView, paramLayoutParams);
  }
  
  @Deprecated
  public void startActivityForResult(Intent paramIntent, int paramInt) {
    super.startActivityForResult(paramIntent, paramInt);
  }
  
  @Deprecated
  public void startActivityForResult(Intent paramIntent, int paramInt, Bundle paramBundle) {
    super.startActivityForResult(paramIntent, paramInt, paramBundle);
  }
  
  @Deprecated
  public void startIntentSenderForResult(IntentSender paramIntentSender, int paramInt1, Intent paramIntent, int paramInt2, int paramInt3, int paramInt4) throws IntentSender.SendIntentException {
    super.startIntentSenderForResult(paramIntentSender, paramInt1, paramIntent, paramInt2, paramInt3, paramInt4);
  }
  
  @Deprecated
  public void startIntentSenderForResult(IntentSender paramIntentSender, int paramInt1, Intent paramIntent, int paramInt2, int paramInt3, int paramInt4, Bundle paramBundle) throws IntentSender.SendIntentException {
    super.startIntentSenderForResult(paramIntentSender, paramInt1, paramIntent, paramInt2, paramInt3, paramInt4, paramBundle);
  }
  
  static final class NonConfigurationInstances {
    Object custom;
    
    ViewModelStore viewModelStore;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\activity\ComponentActivity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */