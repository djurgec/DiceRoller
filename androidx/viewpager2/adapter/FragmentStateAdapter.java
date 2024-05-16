package androidx.viewpager2.adapter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import androidx.collection.ArraySet;
import androidx.collection.LongSparseArray;
import androidx.core.util.Preconditions;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import java.util.Iterator;

public abstract class FragmentStateAdapter extends RecyclerView.Adapter<FragmentViewHolder> implements StatefulAdapter {
  private static final long GRACE_WINDOW_TIME_MS = 10000L;
  
  private static final String KEY_PREFIX_FRAGMENT = "f#";
  
  private static final String KEY_PREFIX_STATE = "s#";
  
  final FragmentManager mFragmentManager;
  
  private FragmentMaxLifecycleEnforcer mFragmentMaxLifecycleEnforcer;
  
  final LongSparseArray<Fragment> mFragments = new LongSparseArray();
  
  private boolean mHasStaleFragments = false;
  
  boolean mIsInGracePeriod = false;
  
  private final LongSparseArray<Integer> mItemIdToViewHolder = new LongSparseArray();
  
  final Lifecycle mLifecycle;
  
  private final LongSparseArray<Fragment.SavedState> mSavedStates = new LongSparseArray();
  
  public FragmentStateAdapter(Fragment paramFragment) {
    this(paramFragment.getChildFragmentManager(), paramFragment.getLifecycle());
  }
  
  public FragmentStateAdapter(FragmentActivity paramFragmentActivity) {
    this(paramFragmentActivity.getSupportFragmentManager(), paramFragmentActivity.getLifecycle());
  }
  
  public FragmentStateAdapter(FragmentManager paramFragmentManager, Lifecycle paramLifecycle) {
    this.mFragmentManager = paramFragmentManager;
    this.mLifecycle = paramLifecycle;
    super.setHasStableIds(true);
  }
  
  private static String createKey(String paramString, long paramLong) {
    return paramString + paramLong;
  }
  
  private void ensureFragment(int paramInt) {
    long l = getItemId(paramInt);
    if (!this.mFragments.containsKey(l)) {
      Fragment fragment = createFragment(paramInt);
      fragment.setInitialSavedState((Fragment.SavedState)this.mSavedStates.get(l));
      this.mFragments.put(l, fragment);
    } 
  }
  
  private boolean isFragmentViewBound(long paramLong) {
    boolean bool1 = this.mItemIdToViewHolder.containsKey(paramLong);
    boolean bool = true;
    if (bool1)
      return true; 
    Fragment fragment = (Fragment)this.mFragments.get(paramLong);
    if (fragment == null)
      return false; 
    View view = fragment.getView();
    if (view == null)
      return false; 
    if (view.getParent() == null)
      bool = false; 
    return bool;
  }
  
  private static boolean isValidKey(String paramString1, String paramString2) {
    boolean bool;
    if (paramString1.startsWith(paramString2) && paramString1.length() > paramString2.length()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private Long itemForViewHolder(int paramInt) {
    Long long_ = null;
    byte b = 0;
    while (b < this.mItemIdToViewHolder.size()) {
      Long long_1 = long_;
      if (((Integer)this.mItemIdToViewHolder.valueAt(b)).intValue() == paramInt)
        if (long_ == null) {
          long_1 = Long.valueOf(this.mItemIdToViewHolder.keyAt(b));
        } else {
          throw new IllegalStateException("Design assumption violated: a ViewHolder can only be bound to one item at a time.");
        }  
      b++;
      long_ = long_1;
    } 
    return long_;
  }
  
  private static long parseIdFromKey(String paramString1, String paramString2) {
    return Long.parseLong(paramString1.substring(paramString2.length()));
  }
  
  private void removeFragment(long paramLong) {
    Fragment fragment = (Fragment)this.mFragments.get(paramLong);
    if (fragment == null)
      return; 
    if (fragment.getView() != null) {
      ViewParent viewParent = fragment.getView().getParent();
      if (viewParent != null)
        ((FrameLayout)viewParent).removeAllViews(); 
    } 
    if (!containsItem(paramLong))
      this.mSavedStates.remove(paramLong); 
    if (!fragment.isAdded()) {
      this.mFragments.remove(paramLong);
      return;
    } 
    if (shouldDelayFragmentTransactions()) {
      this.mHasStaleFragments = true;
      return;
    } 
    if (fragment.isAdded() && containsItem(paramLong))
      this.mSavedStates.put(paramLong, this.mFragmentManager.saveFragmentInstanceState(fragment)); 
    this.mFragmentManager.beginTransaction().remove(fragment).commitNow();
    this.mFragments.remove(paramLong);
  }
  
  private void scheduleGracePeriodEnd() {
    final Handler handler = new Handler(Looper.getMainLooper());
    final Runnable runnable = new Runnable() {
        final FragmentStateAdapter this$0;
        
        public void run() {
          FragmentStateAdapter.this.mIsInGracePeriod = false;
          FragmentStateAdapter.this.gcFragments();
        }
      };
    this.mLifecycle.addObserver((LifecycleObserver)new LifecycleEventObserver() {
          final FragmentStateAdapter this$0;
          
          final Handler val$handler;
          
          final Runnable val$runnable;
          
          public void onStateChanged(LifecycleOwner param1LifecycleOwner, Lifecycle.Event param1Event) {
            if (param1Event == Lifecycle.Event.ON_DESTROY) {
              handler.removeCallbacks(runnable);
              param1LifecycleOwner.getLifecycle().removeObserver((LifecycleObserver)this);
            } 
          }
        });
    handler.postDelayed(runnable, 10000L);
  }
  
  private void scheduleViewAttach(final Fragment fragment, final FrameLayout container) {
    this.mFragmentManager.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
          final FragmentStateAdapter this$0;
          
          final FrameLayout val$container;
          
          final Fragment val$fragment;
          
          public void onFragmentViewCreated(FragmentManager param1FragmentManager, Fragment param1Fragment, View param1View, Bundle param1Bundle) {
            if (param1Fragment == fragment) {
              param1FragmentManager.unregisterFragmentLifecycleCallbacks(this);
              FragmentStateAdapter.this.addViewToContainer(param1View, container);
            } 
          }
        }false);
  }
  
  void addViewToContainer(View paramView, FrameLayout paramFrameLayout) {
    if (paramFrameLayout.getChildCount() <= 1) {
      if (paramView.getParent() == paramFrameLayout)
        return; 
      if (paramFrameLayout.getChildCount() > 0)
        paramFrameLayout.removeAllViews(); 
      if (paramView.getParent() != null)
        ((ViewGroup)paramView.getParent()).removeView(paramView); 
      paramFrameLayout.addView(paramView);
      return;
    } 
    throw new IllegalStateException("Design assumption violated.");
  }
  
  public boolean containsItem(long paramLong) {
    boolean bool;
    if (paramLong >= 0L && paramLong < getItemCount()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public abstract Fragment createFragment(int paramInt);
  
  void gcFragments() {
    if (!this.mHasStaleFragments || shouldDelayFragmentTransactions())
      return; 
    ArraySet<Long> arraySet = new ArraySet();
    byte b;
    for (b = 0; b < this.mFragments.size(); b++) {
      long l = this.mFragments.keyAt(b);
      if (!containsItem(l)) {
        arraySet.add(Long.valueOf(l));
        this.mItemIdToViewHolder.remove(l);
      } 
    } 
    if (!this.mIsInGracePeriod) {
      this.mHasStaleFragments = false;
      for (b = 0; b < this.mFragments.size(); b++) {
        long l = this.mFragments.keyAt(b);
        if (!isFragmentViewBound(l))
          arraySet.add(Long.valueOf(l)); 
      } 
    } 
    Iterator<Long> iterator = arraySet.iterator();
    while (iterator.hasNext())
      removeFragment(((Long)iterator.next()).longValue()); 
  }
  
  public long getItemId(int paramInt) {
    return paramInt;
  }
  
  public void onAttachedToRecyclerView(RecyclerView paramRecyclerView) {
    boolean bool;
    if (this.mFragmentMaxLifecycleEnforcer == null) {
      bool = true;
    } else {
      bool = false;
    } 
    Preconditions.checkArgument(bool);
    FragmentMaxLifecycleEnforcer fragmentMaxLifecycleEnforcer = new FragmentMaxLifecycleEnforcer();
    this.mFragmentMaxLifecycleEnforcer = fragmentMaxLifecycleEnforcer;
    fragmentMaxLifecycleEnforcer.register(paramRecyclerView);
  }
  
  public final void onBindViewHolder(final FragmentViewHolder holder, int paramInt) {
    long l = holder.getItemId();
    int i = holder.getContainer().getId();
    Long long_ = itemForViewHolder(i);
    if (long_ != null && long_.longValue() != l) {
      removeFragment(long_.longValue());
      this.mItemIdToViewHolder.remove(long_.longValue());
    } 
    this.mItemIdToViewHolder.put(l, Integer.valueOf(i));
    ensureFragment(paramInt);
    final FrameLayout container = holder.getContainer();
    if (ViewCompat.isAttachedToWindow((View)frameLayout))
      if (frameLayout.getParent() == null) {
        frameLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
              final FragmentStateAdapter this$0;
              
              final FrameLayout val$container;
              
              final FragmentViewHolder val$holder;
              
              public void onLayoutChange(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6, int param1Int7, int param1Int8) {
                if (container.getParent() != null) {
                  container.removeOnLayoutChangeListener(this);
                  FragmentStateAdapter.this.placeFragmentInViewHolder(holder);
                } 
              }
            });
      } else {
        throw new IllegalStateException("Design assumption violated.");
      }  
    gcFragments();
  }
  
  public final FragmentViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
    return FragmentViewHolder.create(paramViewGroup);
  }
  
  public void onDetachedFromRecyclerView(RecyclerView paramRecyclerView) {
    this.mFragmentMaxLifecycleEnforcer.unregister(paramRecyclerView);
    this.mFragmentMaxLifecycleEnforcer = null;
  }
  
  public final boolean onFailedToRecycleView(FragmentViewHolder paramFragmentViewHolder) {
    return true;
  }
  
  public final void onViewAttachedToWindow(FragmentViewHolder paramFragmentViewHolder) {
    placeFragmentInViewHolder(paramFragmentViewHolder);
    gcFragments();
  }
  
  public final void onViewRecycled(FragmentViewHolder paramFragmentViewHolder) {
    Long long_ = itemForViewHolder(paramFragmentViewHolder.getContainer().getId());
    if (long_ != null) {
      removeFragment(long_.longValue());
      this.mItemIdToViewHolder.remove(long_.longValue());
    } 
  }
  
  void placeFragmentInViewHolder(final FragmentViewHolder holder) {
    Fragment fragment = (Fragment)this.mFragments.get(holder.getItemId());
    if (fragment != null) {
      FrameLayout frameLayout = holder.getContainer();
      View view = fragment.getView();
      if (fragment.isAdded() || view == null) {
        if (fragment.isAdded() && view == null) {
          scheduleViewAttach(fragment, frameLayout);
          return;
        } 
        if (fragment.isAdded() && view.getParent() != null) {
          if (view.getParent() != frameLayout)
            addViewToContainer(view, frameLayout); 
          return;
        } 
        if (fragment.isAdded()) {
          addViewToContainer(view, frameLayout);
          return;
        } 
        if (!shouldDelayFragmentTransactions()) {
          scheduleViewAttach(fragment, frameLayout);
          this.mFragmentManager.beginTransaction().add(fragment, "f" + holder.getItemId()).setMaxLifecycle(fragment, Lifecycle.State.STARTED).commitNow();
          this.mFragmentMaxLifecycleEnforcer.updateFragmentMaxLifecycle(false);
        } else {
          if (this.mFragmentManager.isDestroyed())
            return; 
          this.mLifecycle.addObserver((LifecycleObserver)new LifecycleEventObserver() {
                final FragmentStateAdapter this$0;
                
                final FragmentViewHolder val$holder;
                
                public void onStateChanged(LifecycleOwner param1LifecycleOwner, Lifecycle.Event param1Event) {
                  if (FragmentStateAdapter.this.shouldDelayFragmentTransactions())
                    return; 
                  param1LifecycleOwner.getLifecycle().removeObserver((LifecycleObserver)this);
                  if (ViewCompat.isAttachedToWindow((View)holder.getContainer()))
                    FragmentStateAdapter.this.placeFragmentInViewHolder(holder); 
                }
              });
        } 
        return;
      } 
      throw new IllegalStateException("Design assumption violated.");
    } 
    throw new IllegalStateException("Design assumption violated.");
  }
  
  public final void restoreState(Parcelable paramParcelable) {
    if (this.mSavedStates.isEmpty() && this.mFragments.isEmpty()) {
      Bundle bundle = (Bundle)paramParcelable;
      if (bundle.getClassLoader() == null)
        bundle.setClassLoader(getClass().getClassLoader()); 
      for (String str : bundle.keySet()) {
        Fragment fragment;
        Fragment.SavedState savedState;
        if (isValidKey(str, "f#")) {
          long l = parseIdFromKey(str, "f#");
          fragment = this.mFragmentManager.getFragment(bundle, str);
          this.mFragments.put(l, fragment);
          continue;
        } 
        if (isValidKey((String)fragment, "s#")) {
          long l = parseIdFromKey((String)fragment, "s#");
          savedState = (Fragment.SavedState)bundle.getParcelable((String)fragment);
          if (containsItem(l))
            this.mSavedStates.put(l, savedState); 
          continue;
        } 
        throw new IllegalArgumentException("Unexpected key in savedState: " + savedState);
      } 
      if (!this.mFragments.isEmpty()) {
        this.mHasStaleFragments = true;
        this.mIsInGracePeriod = true;
        gcFragments();
        scheduleGracePeriodEnd();
      } 
      return;
    } 
    throw new IllegalStateException("Expected the adapter to be 'fresh' while restoring state.");
  }
  
  public final Parcelable saveState() {
    Bundle bundle = new Bundle(this.mFragments.size() + this.mSavedStates.size());
    byte b;
    for (b = 0; b < this.mFragments.size(); b++) {
      long l = this.mFragments.keyAt(b);
      Fragment fragment = (Fragment)this.mFragments.get(l);
      if (fragment != null && fragment.isAdded()) {
        String str = createKey("f#", l);
        this.mFragmentManager.putFragment(bundle, str, fragment);
      } 
    } 
    for (b = 0; b < this.mSavedStates.size(); b++) {
      long l = this.mSavedStates.keyAt(b);
      if (containsItem(l))
        bundle.putParcelable(createKey("s#", l), (Parcelable)this.mSavedStates.get(l)); 
    } 
    return (Parcelable)bundle;
  }
  
  public final void setHasStableIds(boolean paramBoolean) {
    throw new UnsupportedOperationException("Stable Ids are required for the adapter to function properly, and the adapter takes care of setting the flag.");
  }
  
  boolean shouldDelayFragmentTransactions() {
    return this.mFragmentManager.isStateSaved();
  }
  
  private static abstract class DataSetChangeObserver extends RecyclerView.AdapterDataObserver {
    private DataSetChangeObserver() {}
    
    public abstract void onChanged();
    
    public final void onItemRangeChanged(int param1Int1, int param1Int2) {
      onChanged();
    }
    
    public final void onItemRangeChanged(int param1Int1, int param1Int2, Object param1Object) {
      onChanged();
    }
    
    public final void onItemRangeInserted(int param1Int1, int param1Int2) {
      onChanged();
    }
    
    public final void onItemRangeMoved(int param1Int1, int param1Int2, int param1Int3) {
      onChanged();
    }
    
    public final void onItemRangeRemoved(int param1Int1, int param1Int2) {
      onChanged();
    }
  }
  
  class FragmentMaxLifecycleEnforcer {
    private RecyclerView.AdapterDataObserver mDataObserver;
    
    private LifecycleEventObserver mLifecycleObserver;
    
    private ViewPager2.OnPageChangeCallback mPageChangeCallback;
    
    private long mPrimaryItemId = -1L;
    
    private ViewPager2 mViewPager;
    
    final FragmentStateAdapter this$0;
    
    private ViewPager2 inferViewPager(RecyclerView param1RecyclerView) {
      ViewParent viewParent = param1RecyclerView.getParent();
      if (viewParent instanceof ViewPager2)
        return (ViewPager2)viewParent; 
      throw new IllegalStateException("Expected ViewPager2 instance. Got: " + viewParent);
    }
    
    void register(RecyclerView param1RecyclerView) {
      this.mViewPager = inferViewPager(param1RecyclerView);
      ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
          final FragmentStateAdapter.FragmentMaxLifecycleEnforcer this$1;
          
          public void onPageScrollStateChanged(int param2Int) {
            FragmentStateAdapter.FragmentMaxLifecycleEnforcer.this.updateFragmentMaxLifecycle(false);
          }
          
          public void onPageSelected(int param2Int) {
            FragmentStateAdapter.FragmentMaxLifecycleEnforcer.this.updateFragmentMaxLifecycle(false);
          }
        };
      this.mPageChangeCallback = onPageChangeCallback;
      this.mViewPager.registerOnPageChangeCallback(onPageChangeCallback);
      FragmentStateAdapter.DataSetChangeObserver dataSetChangeObserver = new FragmentStateAdapter.DataSetChangeObserver() {
          final FragmentStateAdapter.FragmentMaxLifecycleEnforcer this$1;
          
          public void onChanged() {
            FragmentStateAdapter.FragmentMaxLifecycleEnforcer.this.updateFragmentMaxLifecycle(true);
          }
        };
      this.mDataObserver = dataSetChangeObserver;
      FragmentStateAdapter.this.registerAdapterDataObserver(dataSetChangeObserver);
      this.mLifecycleObserver = new LifecycleEventObserver() {
          final FragmentStateAdapter.FragmentMaxLifecycleEnforcer this$1;
          
          public void onStateChanged(LifecycleOwner param2LifecycleOwner, Lifecycle.Event param2Event) {
            FragmentStateAdapter.FragmentMaxLifecycleEnforcer.this.updateFragmentMaxLifecycle(false);
          }
        };
      FragmentStateAdapter.this.mLifecycle.addObserver((LifecycleObserver)this.mLifecycleObserver);
    }
    
    void unregister(RecyclerView param1RecyclerView) {
      inferViewPager(param1RecyclerView).unregisterOnPageChangeCallback(this.mPageChangeCallback);
      FragmentStateAdapter.this.unregisterAdapterDataObserver(this.mDataObserver);
      FragmentStateAdapter.this.mLifecycle.removeObserver((LifecycleObserver)this.mLifecycleObserver);
      this.mViewPager = null;
    }
    
    void updateFragmentMaxLifecycle(boolean param1Boolean) {
      if (FragmentStateAdapter.this.shouldDelayFragmentTransactions())
        return; 
      if (this.mViewPager.getScrollState() != 0)
        return; 
      if (FragmentStateAdapter.this.mFragments.isEmpty() || FragmentStateAdapter.this.getItemCount() == 0)
        return; 
      int i = this.mViewPager.getCurrentItem();
      if (i >= FragmentStateAdapter.this.getItemCount())
        return; 
      long l = FragmentStateAdapter.this.getItemId(i);
      if (l == this.mPrimaryItemId && !param1Boolean)
        return; 
      Fragment fragment = (Fragment)FragmentStateAdapter.this.mFragments.get(l);
      if (fragment == null || !fragment.isAdded())
        return; 
      this.mPrimaryItemId = l;
      FragmentTransaction fragmentTransaction = FragmentStateAdapter.this.mFragmentManager.beginTransaction();
      fragment = null;
      for (i = 0; i < FragmentStateAdapter.this.mFragments.size(); i++) {
        l = FragmentStateAdapter.this.mFragments.keyAt(i);
        Fragment fragment1 = (Fragment)FragmentStateAdapter.this.mFragments.valueAt(i);
        if (fragment1.isAdded()) {
          if (l != this.mPrimaryItemId) {
            fragmentTransaction.setMaxLifecycle(fragment1, Lifecycle.State.STARTED);
          } else {
            fragment = fragment1;
          } 
          if (l == this.mPrimaryItemId) {
            param1Boolean = true;
          } else {
            param1Boolean = false;
          } 
          fragment1.setMenuVisibility(param1Boolean);
        } 
      } 
      if (fragment != null)
        fragmentTransaction.setMaxLifecycle(fragment, Lifecycle.State.RESUMED); 
      if (!fragmentTransaction.isEmpty())
        fragmentTransaction.commitNow(); 
    }
  }
  
  class null extends ViewPager2.OnPageChangeCallback {
    final FragmentStateAdapter.FragmentMaxLifecycleEnforcer this$1;
    
    public void onPageScrollStateChanged(int param1Int) {
      this.this$1.updateFragmentMaxLifecycle(false);
    }
    
    public void onPageSelected(int param1Int) {
      this.this$1.updateFragmentMaxLifecycle(false);
    }
  }
  
  class null extends DataSetChangeObserver {
    final FragmentStateAdapter.FragmentMaxLifecycleEnforcer this$1;
    
    public void onChanged() {
      this.this$1.updateFragmentMaxLifecycle(true);
    }
  }
  
  class null implements LifecycleEventObserver {
    final FragmentStateAdapter.FragmentMaxLifecycleEnforcer this$1;
    
    public void onStateChanged(LifecycleOwner param1LifecycleOwner, Lifecycle.Event param1Event) {
      this.this$1.updateFragmentMaxLifecycle(false);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\viewpager2\adapter\FragmentStateAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */