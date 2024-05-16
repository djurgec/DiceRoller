package androidx.viewpager2.widget;

import android.view.View;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class CompositePageTransformer implements ViewPager2.PageTransformer {
  private final List<ViewPager2.PageTransformer> mTransformers = new ArrayList<>();
  
  public void addTransformer(ViewPager2.PageTransformer paramPageTransformer) {
    this.mTransformers.add(paramPageTransformer);
  }
  
  public void removeTransformer(ViewPager2.PageTransformer paramPageTransformer) {
    this.mTransformers.remove(paramPageTransformer);
  }
  
  public void transformPage(View paramView, float paramFloat) {
    Iterator<ViewPager2.PageTransformer> iterator = this.mTransformers.iterator();
    while (iterator.hasNext())
      ((ViewPager2.PageTransformer)iterator.next()).transformPage(paramView, paramFloat); 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\viewpager2\widget\CompositePageTransformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */