package androidx.core.view;

import android.view.View;

public interface NestedScrollingParent {
  int getNestedScrollAxes();
  
  boolean onNestedFling(View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean);
  
  boolean onNestedPreFling(View paramView, float paramFloat1, float paramFloat2);
  
  void onNestedPreScroll(View paramView, int paramInt1, int paramInt2, int[] paramArrayOfint);
  
  void onNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  void onNestedScrollAccepted(View paramView1, View paramView2, int paramInt);
  
  boolean onStartNestedScroll(View paramView1, View paramView2, int paramInt);
  
  void onStopNestedScroll(View paramView);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\view\NestedScrollingParent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */