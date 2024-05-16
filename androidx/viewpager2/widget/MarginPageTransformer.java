package androidx.viewpager2.widget;

import android.view.View;
import android.view.ViewParent;
import androidx.core.util.Preconditions;

public final class MarginPageTransformer implements ViewPager2.PageTransformer {
  private final int mMarginPx;
  
  public MarginPageTransformer(int paramInt) {
    Preconditions.checkArgumentNonnegative(paramInt, "Margin must be non-negative");
    this.mMarginPx = paramInt;
  }
  
  private ViewPager2 requireViewPager(View paramView) {
    ViewParent viewParent1 = paramView.getParent();
    ViewParent viewParent2 = viewParent1.getParent();
    if (viewParent1 instanceof androidx.recyclerview.widget.RecyclerView && viewParent2 instanceof ViewPager2)
      return (ViewPager2)viewParent2; 
    throw new IllegalStateException("Expected the page view to be managed by a ViewPager2 instance.");
  }
  
  public void transformPage(View paramView, float paramFloat) {
    ViewPager2 viewPager2 = requireViewPager(paramView);
    paramFloat = this.mMarginPx * paramFloat;
    if (viewPager2.getOrientation() == 0) {
      if (viewPager2.isRtl())
        paramFloat = -paramFloat; 
      paramView.setTranslationX(paramFloat);
    } else {
      paramView.setTranslationY(paramFloat);
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\viewpager2\widget\MarginPageTransformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */