package androidx.core.view;

public interface NestedScrollingChild2 extends NestedScrollingChild {
  boolean dispatchNestedPreScroll(int paramInt1, int paramInt2, int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt3);
  
  boolean dispatchNestedScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint, int paramInt5);
  
  boolean hasNestedScrollingParent(int paramInt);
  
  boolean startNestedScroll(int paramInt1, int paramInt2);
  
  void stopNestedScroll(int paramInt);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\view\NestedScrollingChild2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */