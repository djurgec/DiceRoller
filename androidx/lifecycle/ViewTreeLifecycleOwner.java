package androidx.lifecycle;

import android.view.View;
import android.view.ViewParent;
import androidx.lifecycle.runtime.R;

public class ViewTreeLifecycleOwner {
  public static LifecycleOwner get(View paramView) {
    LifecycleOwner lifecycleOwner = (LifecycleOwner)paramView.getTag(R.id.view_tree_lifecycle_owner);
    if (lifecycleOwner != null)
      return lifecycleOwner; 
    ViewParent viewParent = paramView.getParent();
    while (lifecycleOwner == null && viewParent instanceof View) {
      View view = (View)viewParent;
      lifecycleOwner = (LifecycleOwner)view.getTag(R.id.view_tree_lifecycle_owner);
      ViewParent viewParent1 = view.getParent();
    } 
    return lifecycleOwner;
  }
  
  public static void set(View paramView, LifecycleOwner paramLifecycleOwner) {
    paramView.setTag(R.id.view_tree_lifecycle_owner, paramLifecycleOwner);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\lifecycle\ViewTreeLifecycleOwner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */