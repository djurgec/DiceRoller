package androidx.viewpager2.widget;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

final class CompositeOnPageChangeCallback extends ViewPager2.OnPageChangeCallback {
  private final List<ViewPager2.OnPageChangeCallback> mCallbacks;
  
  CompositeOnPageChangeCallback(int paramInt) {
    this.mCallbacks = new ArrayList<>(paramInt);
  }
  
  private void throwCallbackListModifiedWhileInUse(ConcurrentModificationException paramConcurrentModificationException) {
    throw new IllegalStateException("Adding and removing callbacks during dispatch to callbacks is not supported", paramConcurrentModificationException);
  }
  
  void addOnPageChangeCallback(ViewPager2.OnPageChangeCallback paramOnPageChangeCallback) {
    this.mCallbacks.add(paramOnPageChangeCallback);
  }
  
  public void onPageScrollStateChanged(int paramInt) {
    try {
      Iterator<ViewPager2.OnPageChangeCallback> iterator = this.mCallbacks.iterator();
      while (iterator.hasNext())
        ((ViewPager2.OnPageChangeCallback)iterator.next()).onPageScrollStateChanged(paramInt); 
    } catch (ConcurrentModificationException concurrentModificationException) {
      throwCallbackListModifiedWhileInUse(concurrentModificationException);
    } 
  }
  
  public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
    try {
      Iterator<ViewPager2.OnPageChangeCallback> iterator = this.mCallbacks.iterator();
      while (iterator.hasNext())
        ((ViewPager2.OnPageChangeCallback)iterator.next()).onPageScrolled(paramInt1, paramFloat, paramInt2); 
    } catch (ConcurrentModificationException concurrentModificationException) {
      throwCallbackListModifiedWhileInUse(concurrentModificationException);
    } 
  }
  
  public void onPageSelected(int paramInt) {
    try {
      Iterator<ViewPager2.OnPageChangeCallback> iterator = this.mCallbacks.iterator();
      while (iterator.hasNext())
        ((ViewPager2.OnPageChangeCallback)iterator.next()).onPageSelected(paramInt); 
    } catch (ConcurrentModificationException concurrentModificationException) {
      throwCallbackListModifiedWhileInUse(concurrentModificationException);
    } 
  }
  
  void removeOnPageChangeCallback(ViewPager2.OnPageChangeCallback paramOnPageChangeCallback) {
    this.mCallbacks.remove(paramOnPageChangeCallback);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\viewpager2\widget\CompositeOnPageChangeCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */