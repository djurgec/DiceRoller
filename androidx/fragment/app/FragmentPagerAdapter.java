package androidx.fragment.app;

import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.PagerAdapter;

@Deprecated
public abstract class FragmentPagerAdapter extends PagerAdapter {
  public static final int BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT = 1;
  
  @Deprecated
  public static final int BEHAVIOR_SET_USER_VISIBLE_HINT = 0;
  
  private static final boolean DEBUG = false;
  
  private static final String TAG = "FragmentPagerAdapter";
  
  private final int mBehavior;
  
  private FragmentTransaction mCurTransaction = null;
  
  private Fragment mCurrentPrimaryItem = null;
  
  private boolean mExecutingFinishUpdate;
  
  private final FragmentManager mFragmentManager;
  
  @Deprecated
  public FragmentPagerAdapter(FragmentManager paramFragmentManager) {
    this(paramFragmentManager, 0);
  }
  
  public FragmentPagerAdapter(FragmentManager paramFragmentManager, int paramInt) {
    this.mFragmentManager = paramFragmentManager;
    this.mBehavior = paramInt;
  }
  
  private static String makeFragmentName(int paramInt, long paramLong) {
    return "android:switcher:" + paramInt + ":" + paramLong;
  }
  
  public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject) {
    Fragment fragment = (Fragment)paramObject;
    if (this.mCurTransaction == null)
      this.mCurTransaction = this.mFragmentManager.beginTransaction(); 
    this.mCurTransaction.detach(fragment);
    if (fragment.equals(this.mCurrentPrimaryItem))
      this.mCurrentPrimaryItem = null; 
  }
  
  public void finishUpdate(ViewGroup paramViewGroup) {
    FragmentTransaction fragmentTransaction = this.mCurTransaction;
    if (fragmentTransaction != null) {
      if (!this.mExecutingFinishUpdate)
        try {
          this.mExecutingFinishUpdate = true;
          fragmentTransaction.commitNowAllowingStateLoss();
        } finally {
          this.mExecutingFinishUpdate = false;
        }  
      this.mCurTransaction = null;
    } 
  }
  
  public abstract Fragment getItem(int paramInt);
  
  public long getItemId(int paramInt) {
    return paramInt;
  }
  
  public Object instantiateItem(ViewGroup paramViewGroup, int paramInt) {
    Fragment fragment1;
    if (this.mCurTransaction == null)
      this.mCurTransaction = this.mFragmentManager.beginTransaction(); 
    long l = getItemId(paramInt);
    String str = makeFragmentName(paramViewGroup.getId(), l);
    Fragment fragment2 = this.mFragmentManager.findFragmentByTag(str);
    if (fragment2 != null) {
      this.mCurTransaction.attach(fragment2);
      fragment1 = fragment2;
    } else {
      fragment2 = getItem(paramInt);
      this.mCurTransaction.add(fragment1.getId(), fragment2, makeFragmentName(fragment1.getId(), l));
      fragment1 = fragment2;
    } 
    if (fragment1 != this.mCurrentPrimaryItem) {
      fragment1.setMenuVisibility(false);
      if (this.mBehavior == 1) {
        this.mCurTransaction.setMaxLifecycle(fragment1, Lifecycle.State.STARTED);
      } else {
        fragment1.setUserVisibleHint(false);
      } 
    } 
    return fragment1;
  }
  
  public boolean isViewFromObject(View paramView, Object paramObject) {
    boolean bool;
    if (((Fragment)paramObject).getView() == paramView) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void restoreState(Parcelable paramParcelable, ClassLoader paramClassLoader) {}
  
  public Parcelable saveState() {
    return null;
  }
  
  public void setPrimaryItem(ViewGroup paramViewGroup, int paramInt, Object paramObject) {
    Fragment fragment = (Fragment)paramObject;
    paramObject = this.mCurrentPrimaryItem;
    if (fragment != paramObject) {
      if (paramObject != null) {
        paramObject.setMenuVisibility(false);
        if (this.mBehavior == 1) {
          if (this.mCurTransaction == null)
            this.mCurTransaction = this.mFragmentManager.beginTransaction(); 
          this.mCurTransaction.setMaxLifecycle(this.mCurrentPrimaryItem, Lifecycle.State.STARTED);
        } else {
          this.mCurrentPrimaryItem.setUserVisibleHint(false);
        } 
      } 
      fragment.setMenuVisibility(true);
      if (this.mBehavior == 1) {
        if (this.mCurTransaction == null)
          this.mCurTransaction = this.mFragmentManager.beginTransaction(); 
        this.mCurTransaction.setMaxLifecycle(fragment, Lifecycle.State.RESUMED);
      } else {
        fragment.setUserVisibleHint(true);
      } 
      this.mCurrentPrimaryItem = fragment;
    } 
  }
  
  public void startUpdate(ViewGroup paramViewGroup) {
    if (paramViewGroup.getId() != -1)
      return; 
    throw new IllegalStateException("ViewPager with adapter " + this + " requires a view id");
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\fragment\app\FragmentPagerAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */