package androidx.fragment.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.R;

class FragmentLayoutInflaterFactory implements LayoutInflater.Factory2 {
  private static final String TAG = "FragmentManager";
  
  final FragmentManager mFragmentManager;
  
  FragmentLayoutInflaterFactory(FragmentManager paramFragmentManager) {
    this.mFragmentManager = paramFragmentManager;
  }
  
  public View onCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet) {
    Fragment fragment;
    boolean bool;
    if (FragmentContainerView.class.getName().equals(paramString))
      return (View)new FragmentContainerView(paramContext, paramAttributeSet, this.mFragmentManager); 
    boolean bool1 = "fragment".equals(paramString);
    paramString = null;
    if (!bool1)
      return null; 
    String str1 = paramAttributeSet.getAttributeValue(null, "class");
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.Fragment);
    String str2 = str1;
    if (str1 == null)
      str2 = typedArray.getString(R.styleable.Fragment_android_name); 
    int i = typedArray.getResourceId(R.styleable.Fragment_android_id, -1);
    String str3 = typedArray.getString(R.styleable.Fragment_android_tag);
    typedArray.recycle();
    if (str2 == null || !FragmentFactory.isFragmentClass(paramContext.getClassLoader(), str2))
      return null; 
    if (paramView != null) {
      bool = paramView.getId();
    } else {
      bool = false;
    } 
    if (bool != -1 || i != -1 || str3 != null) {
      final FragmentStateManager fragmentStateManager;
      if (i != -1)
        fragment1 = this.mFragmentManager.findFragmentById(i); 
      Fragment fragment2 = fragment1;
      if (fragment1 == null) {
        fragment2 = fragment1;
        if (str3 != null)
          fragment2 = this.mFragmentManager.findFragmentByTag(str3); 
      } 
      Fragment fragment1 = fragment2;
      if (fragment2 == null) {
        fragment1 = fragment2;
        if (bool != -1)
          fragment1 = this.mFragmentManager.findFragmentById(bool); 
      } 
      if (fragment1 == null) {
        boolean bool2;
        fragment1 = this.mFragmentManager.getFragmentFactory().instantiate(paramContext.getClassLoader(), str2);
        fragment1.mFromLayout = true;
        if (i != 0) {
          bool2 = i;
        } else {
          bool2 = bool;
        } 
        fragment1.mFragmentId = bool2;
        fragment1.mContainerId = bool;
        fragment1.mTag = str3;
        fragment1.mInLayout = true;
        fragment1.mFragmentManager = this.mFragmentManager;
        fragment1.mHost = this.mFragmentManager.getHost();
        fragment1.onInflate(this.mFragmentManager.getHost().getContext(), paramAttributeSet, fragment1.mSavedFragmentState);
        FragmentStateManager fragmentStateManager1 = this.mFragmentManager.addFragment(fragment1);
        fragment = fragment1;
        fragmentStateManager = fragmentStateManager1;
        if (FragmentManager.isLoggingEnabled(2)) {
          Log.v("FragmentManager", "Fragment " + fragment1 + " has been inflated via the <fragment> tag: id=0x" + Integer.toHexString(i));
          fragment = fragment1;
          fragmentStateManager = fragmentStateManager1;
        } 
      } else if (!fragment1.mInLayout) {
        fragment1.mInLayout = true;
        fragment1.mFragmentManager = this.mFragmentManager;
        fragment1.mHost = this.mFragmentManager.getHost();
        fragment1.onInflate(this.mFragmentManager.getHost().getContext(), (AttributeSet)fragment, fragment1.mSavedFragmentState);
        FragmentStateManager fragmentStateManager1 = this.mFragmentManager.createOrGetFragmentStateManager(fragment1);
        fragment = fragment1;
        fragmentStateManager = fragmentStateManager1;
        if (FragmentManager.isLoggingEnabled(2)) {
          Log.v("FragmentManager", "Retained Fragment " + fragment1 + " has been re-attached via the <fragment> tag: id=0x" + Integer.toHexString(i));
          fragmentStateManager = fragmentStateManager1;
          fragment = fragment1;
        } 
      } else {
        throw new IllegalArgumentException(fragment.getPositionDescription() + ": Duplicate id 0x" + Integer.toHexString(i) + ", tag " + str3 + ", or parent id 0x" + Integer.toHexString(bool) + " with another fragment for " + str2);
      } 
      fragment.mContainer = (ViewGroup)paramView;
      fragmentStateManager.moveToExpectedState();
      fragmentStateManager.ensureInflatedView();
      if (fragment.mView != null) {
        if (i != 0)
          fragment.mView.setId(i); 
        if (fragment.mView.getTag() == null)
          fragment.mView.setTag(str3); 
        fragment.mView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
              final FragmentLayoutInflaterFactory this$0;
              
              final FragmentStateManager val$fragmentStateManager;
              
              public void onViewAttachedToWindow(View param1View) {
                Fragment fragment = fragmentStateManager.getFragment();
                fragmentStateManager.moveToExpectedState();
                SpecialEffectsController.getOrCreateController((ViewGroup)fragment.mView.getParent(), FragmentLayoutInflaterFactory.this.mFragmentManager).forceCompleteAllOperations();
              }
              
              public void onViewDetachedFromWindow(View param1View) {}
            });
        return fragment.mView;
      } 
      throw new IllegalStateException("Fragment " + str2 + " did not create a view.");
    } 
    throw new IllegalArgumentException(fragment.getPositionDescription() + ": Must specify unique android:id, android:tag, or have a parent with an id for " + str2);
  }
  
  public View onCreateView(String paramString, Context paramContext, AttributeSet paramAttributeSet) {
    return onCreateView(null, paramString, paramContext, paramAttributeSet);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\fragment\app\FragmentLayoutInflaterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */