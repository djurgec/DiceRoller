package com.bumptech.glide.manager;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import androidx.collection.ArrayMap;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.GlideExperiments;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.HardwareConfigState;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RequestManagerRetriever implements Handler.Callback {
  private static final RequestManagerFactory DEFAULT_FACTORY = new RequestManagerFactory() {
      public RequestManager build(Glide param1Glide, Lifecycle param1Lifecycle, RequestManagerTreeNode param1RequestManagerTreeNode, Context param1Context) {
        return new RequestManager(param1Glide, param1Lifecycle, param1RequestManagerTreeNode, param1Context);
      }
    };
  
  private static final String FRAGMENT_INDEX_KEY = "key";
  
  static final String FRAGMENT_TAG = "com.bumptech.glide.manager";
  
  private static final int ID_REMOVE_FRAGMENT_MANAGER = 1;
  
  private static final int ID_REMOVE_SUPPORT_FRAGMENT_MANAGER = 2;
  
  private static final String TAG = "RMRetriever";
  
  private volatile RequestManager applicationManager;
  
  private final RequestManagerFactory factory;
  
  private final FrameWaiter frameWaiter;
  
  private final Handler handler;
  
  final Map<FragmentManager, RequestManagerFragment> pendingRequestManagerFragments = new HashMap<>();
  
  final Map<FragmentManager, SupportRequestManagerFragment> pendingSupportRequestManagerFragments = new HashMap<>();
  
  private final Bundle tempBundle = new Bundle();
  
  private final ArrayMap<View, Fragment> tempViewToFragment = new ArrayMap();
  
  private final ArrayMap<View, Fragment> tempViewToSupportFragment = new ArrayMap();
  
  public RequestManagerRetriever(RequestManagerFactory paramRequestManagerFactory, GlideExperiments paramGlideExperiments) {
    if (paramRequestManagerFactory == null)
      paramRequestManagerFactory = DEFAULT_FACTORY; 
    this.factory = paramRequestManagerFactory;
    this.handler = new Handler(Looper.getMainLooper(), this);
    this.frameWaiter = buildFrameWaiter(paramGlideExperiments);
  }
  
  private static void assertNotDestroyed(Activity paramActivity) {
    if (Build.VERSION.SDK_INT < 17 || !paramActivity.isDestroyed())
      return; 
    throw new IllegalArgumentException("You cannot start a load for a destroyed activity");
  }
  
  private static FrameWaiter buildFrameWaiter(GlideExperiments paramGlideExperiments) {
    FirstFrameWaiter firstFrameWaiter;
    if (!HardwareConfigState.HARDWARE_BITMAPS_SUPPORTED || !HardwareConfigState.BLOCK_HARDWARE_BITMAPS_WHEN_GL_CONTEXT_MIGHT_NOT_BE_INITIALIZED)
      return new DoNothingFirstFrameWaiter(); 
    if (paramGlideExperiments.isEnabled(GlideBuilder.WaitForFramesAfterTrimMemory.class)) {
      FirstFrameAndAfterTrimMemoryWaiter firstFrameAndAfterTrimMemoryWaiter = new FirstFrameAndAfterTrimMemoryWaiter();
    } else {
      firstFrameWaiter = new FirstFrameWaiter();
    } 
    return firstFrameWaiter;
  }
  
  private static Activity findActivity(Context paramContext) {
    return (paramContext instanceof Activity) ? (Activity)paramContext : ((paramContext instanceof ContextWrapper) ? findActivity(((ContextWrapper)paramContext).getBaseContext()) : null);
  }
  
  @Deprecated
  private void findAllFragmentsWithViews(FragmentManager paramFragmentManager, ArrayMap<View, Fragment> paramArrayMap) {
    if (Build.VERSION.SDK_INT >= 26) {
      for (Fragment fragment : paramFragmentManager.getFragments()) {
        if (fragment.getView() != null) {
          paramArrayMap.put(fragment.getView(), fragment);
          findAllFragmentsWithViews(fragment.getChildFragmentManager(), paramArrayMap);
        } 
      } 
    } else {
      findAllFragmentsWithViewsPreO(paramFragmentManager, paramArrayMap);
    } 
  }
  
  @Deprecated
  private void findAllFragmentsWithViewsPreO(FragmentManager paramFragmentManager, ArrayMap<View, Fragment> paramArrayMap) {
    for (byte b = 0;; b++) {
      this.tempBundle.putInt("key", b);
      Fragment fragment = null;
      try {
        Fragment fragment1 = paramFragmentManager.getFragment(this.tempBundle, "key");
        fragment = fragment1;
      } catch (Exception exception) {}
      if (fragment == null)
        return; 
      if (fragment.getView() != null) {
        paramArrayMap.put(fragment.getView(), fragment);
        if (Build.VERSION.SDK_INT >= 17)
          findAllFragmentsWithViews(fragment.getChildFragmentManager(), paramArrayMap); 
      } 
    } 
  }
  
  private static void findAllSupportFragmentsWithViews(Collection<Fragment> paramCollection, Map<View, Fragment> paramMap) {
    if (paramCollection == null)
      return; 
    for (Fragment fragment : paramCollection) {
      if (fragment == null || fragment.getView() == null)
        continue; 
      paramMap.put(fragment.getView(), fragment);
      findAllSupportFragmentsWithViews(fragment.getChildFragmentManager().getFragments(), paramMap);
    } 
  }
  
  @Deprecated
  private Fragment findFragment(View paramView, Activity paramActivity) {
    Fragment fragment;
    this.tempViewToFragment.clear();
    findAllFragmentsWithViews(paramActivity.getFragmentManager(), this.tempViewToFragment);
    View view2 = null;
    View view3 = paramActivity.findViewById(16908290);
    View view1 = paramView;
    paramView = view2;
    while (true) {
      view2 = paramView;
      if (!view1.equals(view3)) {
        Fragment fragment1 = (Fragment)this.tempViewToFragment.get(view1);
        if (fragment1 != null) {
          Fragment fragment2 = fragment1;
          break;
        } 
        fragment = fragment1;
        if (view1.getParent() instanceof View) {
          view1 = (View)view1.getParent();
          continue;
        } 
      } 
      break;
    } 
    this.tempViewToFragment.clear();
    return fragment;
  }
  
  private Fragment findSupportFragment(View paramView, FragmentActivity paramFragmentActivity) {
    Fragment fragment;
    this.tempViewToSupportFragment.clear();
    findAllSupportFragmentsWithViews(paramFragmentActivity.getSupportFragmentManager().getFragments(), (Map<View, Fragment>)this.tempViewToSupportFragment);
    View view2 = null;
    View view3 = paramFragmentActivity.findViewById(16908290);
    View view1 = paramView;
    paramView = view2;
    while (true) {
      view2 = paramView;
      if (!view1.equals(view3)) {
        Fragment fragment1 = (Fragment)this.tempViewToSupportFragment.get(view1);
        if (fragment1 != null) {
          Fragment fragment2 = fragment1;
          break;
        } 
        fragment = fragment1;
        if (view1.getParent() instanceof View) {
          view1 = (View)view1.getParent();
          continue;
        } 
      } 
      break;
    } 
    this.tempViewToSupportFragment.clear();
    return fragment;
  }
  
  @Deprecated
  private RequestManager fragmentGet(Context paramContext, FragmentManager paramFragmentManager, Fragment paramFragment, boolean paramBoolean) {
    RequestManagerFragment requestManagerFragment = getRequestManagerFragment(paramFragmentManager, paramFragment);
    RequestManager requestManager2 = requestManagerFragment.getRequestManager();
    RequestManager requestManager1 = requestManager2;
    if (requestManager2 == null) {
      Glide glide = Glide.get(paramContext);
      requestManager1 = this.factory.build(glide, requestManagerFragment.getGlideLifecycle(), requestManagerFragment.getRequestManagerTreeNode(), paramContext);
      if (paramBoolean)
        requestManager1.onStart(); 
      requestManagerFragment.setRequestManager(requestManager1);
    } 
    return requestManager1;
  }
  
  private RequestManager getApplicationManager(Context paramContext) {
    // Byte code:
    //   0: aload_0
    //   1: getfield applicationManager : Lcom/bumptech/glide/RequestManager;
    //   4: ifnonnull -> 77
    //   7: aload_0
    //   8: monitorenter
    //   9: aload_0
    //   10: getfield applicationManager : Lcom/bumptech/glide/RequestManager;
    //   13: ifnonnull -> 67
    //   16: aload_1
    //   17: invokevirtual getApplicationContext : ()Landroid/content/Context;
    //   20: invokestatic get : (Landroid/content/Context;)Lcom/bumptech/glide/Glide;
    //   23: astore_2
    //   24: aload_0
    //   25: getfield factory : Lcom/bumptech/glide/manager/RequestManagerRetriever$RequestManagerFactory;
    //   28: astore #4
    //   30: new com/bumptech/glide/manager/ApplicationLifecycle
    //   33: astore_3
    //   34: aload_3
    //   35: invokespecial <init> : ()V
    //   38: new com/bumptech/glide/manager/EmptyRequestManagerTreeNode
    //   41: astore #5
    //   43: aload #5
    //   45: invokespecial <init> : ()V
    //   48: aload_0
    //   49: aload #4
    //   51: aload_2
    //   52: aload_3
    //   53: aload #5
    //   55: aload_1
    //   56: invokevirtual getApplicationContext : ()Landroid/content/Context;
    //   59: invokeinterface build : (Lcom/bumptech/glide/Glide;Lcom/bumptech/glide/manager/Lifecycle;Lcom/bumptech/glide/manager/RequestManagerTreeNode;Landroid/content/Context;)Lcom/bumptech/glide/RequestManager;
    //   64: putfield applicationManager : Lcom/bumptech/glide/RequestManager;
    //   67: aload_0
    //   68: monitorexit
    //   69: goto -> 77
    //   72: astore_1
    //   73: aload_0
    //   74: monitorexit
    //   75: aload_1
    //   76: athrow
    //   77: aload_0
    //   78: getfield applicationManager : Lcom/bumptech/glide/RequestManager;
    //   81: areturn
    // Exception table:
    //   from	to	target	type
    //   9	67	72	finally
    //   67	69	72	finally
    //   73	75	72	finally
  }
  
  private RequestManagerFragment getRequestManagerFragment(FragmentManager paramFragmentManager, Fragment paramFragment) {
    RequestManagerFragment requestManagerFragment2 = (RequestManagerFragment)paramFragmentManager.findFragmentByTag("com.bumptech.glide.manager");
    RequestManagerFragment requestManagerFragment1 = requestManagerFragment2;
    if (requestManagerFragment2 == null) {
      requestManagerFragment2 = this.pendingRequestManagerFragments.get(paramFragmentManager);
      requestManagerFragment1 = requestManagerFragment2;
      if (requestManagerFragment2 == null) {
        requestManagerFragment1 = new RequestManagerFragment();
        requestManagerFragment1.setParentFragmentHint(paramFragment);
        this.pendingRequestManagerFragments.put(paramFragmentManager, requestManagerFragment1);
        paramFragmentManager.beginTransaction().add(requestManagerFragment1, "com.bumptech.glide.manager").commitAllowingStateLoss();
        this.handler.obtainMessage(1, paramFragmentManager).sendToTarget();
      } 
    } 
    return requestManagerFragment1;
  }
  
  private SupportRequestManagerFragment getSupportRequestManagerFragment(FragmentManager paramFragmentManager, Fragment paramFragment) {
    SupportRequestManagerFragment supportRequestManagerFragment2 = (SupportRequestManagerFragment)paramFragmentManager.findFragmentByTag("com.bumptech.glide.manager");
    SupportRequestManagerFragment supportRequestManagerFragment1 = supportRequestManagerFragment2;
    if (supportRequestManagerFragment2 == null) {
      supportRequestManagerFragment2 = this.pendingSupportRequestManagerFragments.get(paramFragmentManager);
      supportRequestManagerFragment1 = supportRequestManagerFragment2;
      if (supportRequestManagerFragment2 == null) {
        supportRequestManagerFragment1 = new SupportRequestManagerFragment();
        supportRequestManagerFragment1.setParentFragmentHint(paramFragment);
        this.pendingSupportRequestManagerFragments.put(paramFragmentManager, supportRequestManagerFragment1);
        paramFragmentManager.beginTransaction().add(supportRequestManagerFragment1, "com.bumptech.glide.manager").commitAllowingStateLoss();
        this.handler.obtainMessage(2, paramFragmentManager).sendToTarget();
      } 
    } 
    return supportRequestManagerFragment1;
  }
  
  private static boolean isActivityVisible(Context paramContext) {
    Activity activity = findActivity(paramContext);
    return (activity == null || !activity.isFinishing());
  }
  
  private RequestManager supportFragmentGet(Context paramContext, FragmentManager paramFragmentManager, Fragment paramFragment, boolean paramBoolean) {
    SupportRequestManagerFragment supportRequestManagerFragment = getSupportRequestManagerFragment(paramFragmentManager, paramFragment);
    RequestManager requestManager2 = supportRequestManagerFragment.getRequestManager();
    RequestManager requestManager1 = requestManager2;
    if (requestManager2 == null) {
      Glide glide = Glide.get(paramContext);
      requestManager1 = this.factory.build(glide, supportRequestManagerFragment.getGlideLifecycle(), supportRequestManagerFragment.getRequestManagerTreeNode(), paramContext);
      if (paramBoolean)
        requestManager1.onStart(); 
      supportRequestManagerFragment.setRequestManager(requestManager1);
    } 
    return requestManager1;
  }
  
  public RequestManager get(Activity paramActivity) {
    if (Util.isOnBackgroundThread())
      return get(paramActivity.getApplicationContext()); 
    if (paramActivity instanceof FragmentActivity)
      return get((FragmentActivity)paramActivity); 
    assertNotDestroyed(paramActivity);
    this.frameWaiter.registerSelf(paramActivity);
    return fragmentGet((Context)paramActivity, paramActivity.getFragmentManager(), null, isActivityVisible((Context)paramActivity));
  }
  
  @Deprecated
  public RequestManager get(Fragment paramFragment) {
    if (paramFragment.getActivity() != null) {
      if (Util.isOnBackgroundThread() || Build.VERSION.SDK_INT < 17)
        return get(paramFragment.getActivity().getApplicationContext()); 
      if (paramFragment.getActivity() != null)
        this.frameWaiter.registerSelf(paramFragment.getActivity()); 
      FragmentManager fragmentManager = paramFragment.getChildFragmentManager();
      return fragmentGet((Context)paramFragment.getActivity(), fragmentManager, paramFragment, paramFragment.isVisible());
    } 
    throw new IllegalArgumentException("You cannot start a load on a fragment before it is attached");
  }
  
  public RequestManager get(Context paramContext) {
    if (paramContext != null) {
      if (Util.isOnMainThread() && !(paramContext instanceof android.app.Application)) {
        if (paramContext instanceof FragmentActivity)
          return get((FragmentActivity)paramContext); 
        if (paramContext instanceof Activity)
          return get((Activity)paramContext); 
        if (paramContext instanceof ContextWrapper && ((ContextWrapper)paramContext).getBaseContext().getApplicationContext() != null)
          return get(((ContextWrapper)paramContext).getBaseContext()); 
      } 
      return getApplicationManager(paramContext);
    } 
    throw new IllegalArgumentException("You cannot start a load on a null Context");
  }
  
  public RequestManager get(View paramView) {
    RequestManager requestManager;
    if (Util.isOnBackgroundThread())
      return get(paramView.getContext().getApplicationContext()); 
    Preconditions.checkNotNull(paramView);
    Preconditions.checkNotNull(paramView.getContext(), "Unable to obtain a request manager for a view without a Context");
    Activity activity = findActivity(paramView.getContext());
    if (activity == null)
      return get(paramView.getContext().getApplicationContext()); 
    if (activity instanceof FragmentActivity) {
      Fragment fragment1 = findSupportFragment(paramView, (FragmentActivity)activity);
      if (fragment1 != null) {
        requestManager = get(fragment1);
      } else {
        requestManager = get((FragmentActivity)activity);
      } 
      return requestManager;
    } 
    Fragment fragment = findFragment((View)requestManager, activity);
    return (fragment == null) ? get(activity) : get(fragment);
  }
  
  public RequestManager get(Fragment paramFragment) {
    Preconditions.checkNotNull(paramFragment.getContext(), "You cannot start a load on a fragment before it is attached or after it is destroyed");
    if (Util.isOnBackgroundThread())
      return get(paramFragment.getContext().getApplicationContext()); 
    if (paramFragment.getActivity() != null)
      this.frameWaiter.registerSelf((Activity)paramFragment.getActivity()); 
    FragmentManager fragmentManager = paramFragment.getChildFragmentManager();
    return supportFragmentGet(paramFragment.getContext(), fragmentManager, paramFragment, paramFragment.isVisible());
  }
  
  public RequestManager get(FragmentActivity paramFragmentActivity) {
    if (Util.isOnBackgroundThread())
      return get(paramFragmentActivity.getApplicationContext()); 
    assertNotDestroyed((Activity)paramFragmentActivity);
    this.frameWaiter.registerSelf((Activity)paramFragmentActivity);
    return supportFragmentGet((Context)paramFragmentActivity, paramFragmentActivity.getSupportFragmentManager(), null, isActivityVisible((Context)paramFragmentActivity));
  }
  
  @Deprecated
  RequestManagerFragment getRequestManagerFragment(Activity paramActivity) {
    return getRequestManagerFragment(paramActivity.getFragmentManager(), null);
  }
  
  SupportRequestManagerFragment getSupportRequestManagerFragment(FragmentManager paramFragmentManager) {
    return getSupportRequestManagerFragment(paramFragmentManager, null);
  }
  
  public boolean handleMessage(Message paramMessage) {
    FragmentManager fragmentManager2;
    FragmentManager fragmentManager1;
    FragmentManager fragmentManager3;
    boolean bool = true;
    Message message = null;
    FragmentManager fragmentManager4 = null;
    switch (paramMessage.what) {
      default:
        bool = false;
        paramMessage = message;
        break;
      case 2:
        fragmentManager2 = (FragmentManager)paramMessage.obj;
        fragmentManager4 = fragmentManager2;
        fragmentManager2 = (FragmentManager)this.pendingSupportRequestManagerFragments.remove(fragmentManager2);
        break;
      case 1:
        fragmentManager1 = (FragmentManager)((Message)fragmentManager2).obj;
        fragmentManager3 = fragmentManager1;
        fragmentManager1 = (FragmentManager)this.pendingRequestManagerFragments.remove(fragmentManager1);
        break;
    } 
    if (bool && fragmentManager1 == null && Log.isLoggable("RMRetriever", 5))
      Log.w("RMRetriever", "Failed to remove expected request manager fragment, manager: " + fragmentManager3); 
    return bool;
  }
  
  public static interface RequestManagerFactory {
    RequestManager build(Glide param1Glide, Lifecycle param1Lifecycle, RequestManagerTreeNode param1RequestManagerTreeNode, Context param1Context);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\manager\RequestManagerRetriever.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */