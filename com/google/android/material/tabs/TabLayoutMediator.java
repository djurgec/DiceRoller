package com.google.android.material.tabs;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import java.lang.ref.WeakReference;

public final class TabLayoutMediator {
  private RecyclerView.Adapter<?> adapter;
  
  private boolean attached;
  
  private final boolean autoRefresh;
  
  private TabLayoutOnPageChangeCallback onPageChangeCallback;
  
  private TabLayout.OnTabSelectedListener onTabSelectedListener;
  
  private RecyclerView.AdapterDataObserver pagerAdapterObserver;
  
  private final boolean smoothScroll;
  
  private final TabConfigurationStrategy tabConfigurationStrategy;
  
  private final TabLayout tabLayout;
  
  private final ViewPager2 viewPager;
  
  public TabLayoutMediator(TabLayout paramTabLayout, ViewPager2 paramViewPager2, TabConfigurationStrategy paramTabConfigurationStrategy) {
    this(paramTabLayout, paramViewPager2, true, paramTabConfigurationStrategy);
  }
  
  public TabLayoutMediator(TabLayout paramTabLayout, ViewPager2 paramViewPager2, boolean paramBoolean, TabConfigurationStrategy paramTabConfigurationStrategy) {
    this(paramTabLayout, paramViewPager2, paramBoolean, true, paramTabConfigurationStrategy);
  }
  
  public TabLayoutMediator(TabLayout paramTabLayout, ViewPager2 paramViewPager2, boolean paramBoolean1, boolean paramBoolean2, TabConfigurationStrategy paramTabConfigurationStrategy) {
    this.tabLayout = paramTabLayout;
    this.viewPager = paramViewPager2;
    this.autoRefresh = paramBoolean1;
    this.smoothScroll = paramBoolean2;
    this.tabConfigurationStrategy = paramTabConfigurationStrategy;
  }
  
  public void attach() {
    if (!this.attached) {
      RecyclerView.Adapter<?> adapter = this.viewPager.getAdapter();
      this.adapter = adapter;
      if (adapter != null) {
        this.attached = true;
        TabLayoutOnPageChangeCallback tabLayoutOnPageChangeCallback = new TabLayoutOnPageChangeCallback(this.tabLayout);
        this.onPageChangeCallback = tabLayoutOnPageChangeCallback;
        this.viewPager.registerOnPageChangeCallback(tabLayoutOnPageChangeCallback);
        ViewPagerOnTabSelectedListener viewPagerOnTabSelectedListener = new ViewPagerOnTabSelectedListener(this.viewPager, this.smoothScroll);
        this.onTabSelectedListener = viewPagerOnTabSelectedListener;
        this.tabLayout.addOnTabSelectedListener(viewPagerOnTabSelectedListener);
        if (this.autoRefresh) {
          PagerAdapterObserver pagerAdapterObserver = new PagerAdapterObserver();
          this.pagerAdapterObserver = pagerAdapterObserver;
          this.adapter.registerAdapterDataObserver(pagerAdapterObserver);
        } 
        populateTabsFromPagerAdapter();
        this.tabLayout.setScrollPosition(this.viewPager.getCurrentItem(), 0.0F, true);
        return;
      } 
      throw new IllegalStateException("TabLayoutMediator attached before ViewPager2 has an adapter");
    } 
    throw new IllegalStateException("TabLayoutMediator is already attached");
  }
  
  public void detach() {
    if (this.autoRefresh) {
      RecyclerView.Adapter<?> adapter = this.adapter;
      if (adapter != null) {
        adapter.unregisterAdapterDataObserver(this.pagerAdapterObserver);
        this.pagerAdapterObserver = null;
      } 
    } 
    this.tabLayout.removeOnTabSelectedListener(this.onTabSelectedListener);
    this.viewPager.unregisterOnPageChangeCallback(this.onPageChangeCallback);
    this.onTabSelectedListener = null;
    this.onPageChangeCallback = null;
    this.adapter = null;
    this.attached = false;
  }
  
  public boolean isAttached() {
    return this.attached;
  }
  
  void populateTabsFromPagerAdapter() {
    this.tabLayout.removeAllTabs();
    RecyclerView.Adapter<?> adapter = this.adapter;
    if (adapter != null) {
      int j = adapter.getItemCount();
      int i;
      for (i = 0; i < j; i++) {
        TabLayout.Tab tab = this.tabLayout.newTab();
        this.tabConfigurationStrategy.onConfigureTab(tab, i);
        this.tabLayout.addTab(tab, false);
      } 
      if (j > 0) {
        i = this.tabLayout.getTabCount();
        i = Math.min(this.viewPager.getCurrentItem(), i - 1);
        if (i != this.tabLayout.getSelectedTabPosition()) {
          TabLayout tabLayout = this.tabLayout;
          tabLayout.selectTab(tabLayout.getTabAt(i));
        } 
      } 
    } 
  }
  
  private class PagerAdapterObserver extends RecyclerView.AdapterDataObserver {
    final TabLayoutMediator this$0;
    
    public void onChanged() {
      TabLayoutMediator.this.populateTabsFromPagerAdapter();
    }
    
    public void onItemRangeChanged(int param1Int1, int param1Int2) {
      TabLayoutMediator.this.populateTabsFromPagerAdapter();
    }
    
    public void onItemRangeChanged(int param1Int1, int param1Int2, Object param1Object) {
      TabLayoutMediator.this.populateTabsFromPagerAdapter();
    }
    
    public void onItemRangeInserted(int param1Int1, int param1Int2) {
      TabLayoutMediator.this.populateTabsFromPagerAdapter();
    }
    
    public void onItemRangeMoved(int param1Int1, int param1Int2, int param1Int3) {
      TabLayoutMediator.this.populateTabsFromPagerAdapter();
    }
    
    public void onItemRangeRemoved(int param1Int1, int param1Int2) {
      TabLayoutMediator.this.populateTabsFromPagerAdapter();
    }
  }
  
  public static interface TabConfigurationStrategy {
    void onConfigureTab(TabLayout.Tab param1Tab, int param1Int);
  }
  
  private static class TabLayoutOnPageChangeCallback extends ViewPager2.OnPageChangeCallback {
    private int previousScrollState;
    
    private int scrollState;
    
    private final WeakReference<TabLayout> tabLayoutRef;
    
    TabLayoutOnPageChangeCallback(TabLayout param1TabLayout) {
      this.tabLayoutRef = new WeakReference<>(param1TabLayout);
      reset();
    }
    
    public void onPageScrollStateChanged(int param1Int) {
      this.previousScrollState = this.scrollState;
      this.scrollState = param1Int;
    }
    
    public void onPageScrolled(int param1Int1, float param1Float, int param1Int2) {
      TabLayout tabLayout = this.tabLayoutRef.get();
      if (tabLayout != null) {
        boolean bool1;
        param1Int2 = this.scrollState;
        boolean bool2 = false;
        if (param1Int2 != 2 || this.previousScrollState == 1) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        if (param1Int2 != 2 || this.previousScrollState != 0)
          bool2 = true; 
        tabLayout.setScrollPosition(param1Int1, param1Float, bool1, bool2);
      } 
    }
    
    public void onPageSelected(int param1Int) {
      TabLayout tabLayout = this.tabLayoutRef.get();
      if (tabLayout != null && tabLayout.getSelectedTabPosition() != param1Int && param1Int < tabLayout.getTabCount()) {
        boolean bool;
        int i = this.scrollState;
        if (i == 0 || (i == 2 && this.previousScrollState == 0)) {
          bool = true;
        } else {
          bool = false;
        } 
        tabLayout.selectTab(tabLayout.getTabAt(param1Int), bool);
      } 
    }
    
    void reset() {
      this.scrollState = 0;
      this.previousScrollState = 0;
    }
  }
  
  private static class ViewPagerOnTabSelectedListener implements TabLayout.OnTabSelectedListener {
    private final boolean smoothScroll;
    
    private final ViewPager2 viewPager;
    
    ViewPagerOnTabSelectedListener(ViewPager2 param1ViewPager2, boolean param1Boolean) {
      this.viewPager = param1ViewPager2;
      this.smoothScroll = param1Boolean;
    }
    
    public void onTabReselected(TabLayout.Tab param1Tab) {}
    
    public void onTabSelected(TabLayout.Tab param1Tab) {
      this.viewPager.setCurrentItem(param1Tab.getPosition(), this.smoothScroll);
    }
    
    public void onTabUnselected(TabLayout.Tab param1Tab) {}
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\tabs\TabLayoutMediator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */