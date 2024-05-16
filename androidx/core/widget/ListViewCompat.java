package androidx.core.widget;

import android.os.Build;
import android.view.View;
import android.widget.ListView;

public final class ListViewCompat {
  public static boolean canScrollList(ListView paramListView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 19)
      return paramListView.canScrollList(paramInt); 
    int j = paramListView.getChildCount();
    null = false;
    boolean bool = false;
    if (j == 0)
      return false; 
    int i = paramListView.getFirstVisiblePosition();
    if (paramInt > 0) {
      paramInt = paramListView.getChildAt(j - 1).getBottom();
      if (i + j >= paramListView.getCount()) {
        null = bool;
        return (paramInt > paramListView.getHeight() - paramListView.getListPaddingBottom()) ? true : null;
      } 
    } else {
      paramInt = paramListView.getChildAt(0).getTop();
      if (i > 0 || paramInt < paramListView.getListPaddingTop())
        null = true; 
      return null;
    } 
    return true;
  }
  
  public static void scrollListBy(ListView paramListView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 19) {
      paramListView.scrollListBy(paramInt);
    } else {
      int i = paramListView.getFirstVisiblePosition();
      if (i == -1)
        return; 
      View view = paramListView.getChildAt(0);
      if (view == null)
        return; 
      paramListView.setSelectionFromTop(i, view.getTop() - paramInt);
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\widget\ListViewCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */