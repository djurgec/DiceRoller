package androidx.core.view;

import android.view.View;

public interface NestedScrollingParent2 extends NestedScrollingParent {
  void onNestedPreScroll(View paramView, int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3);
  
  void onNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  void onNestedScrollAccepted(View paramView1, View paramView2, int paramInt1, int paramInt2);
  
  boolean onStartNestedScroll(View paramView1, View paramView2, int paramInt1, int paramInt2);
  
  void onStopNestedScroll(View paramView, int paramInt);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\view\NestedScrollingParent2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */