package androidx.lifecycle;

import android.view.View;
import android.view.ViewParent;
import androidx.lifecycle.viewmodel.R;

public class ViewTreeViewModelStoreOwner {
  public static ViewModelStoreOwner get(View paramView) {
    ViewModelStoreOwner viewModelStoreOwner2 = (ViewModelStoreOwner)paramView.getTag(R.id.view_tree_view_model_store_owner);
    if (viewModelStoreOwner2 != null)
      return viewModelStoreOwner2; 
    ViewParent viewParent = paramView.getParent();
    ViewModelStoreOwner viewModelStoreOwner1 = viewModelStoreOwner2;
    while (viewModelStoreOwner1 == null && viewParent instanceof View) {
      View view = (View)viewParent;
      viewModelStoreOwner1 = (ViewModelStoreOwner)view.getTag(R.id.view_tree_view_model_store_owner);
      ViewParent viewParent1 = view.getParent();
    } 
    return viewModelStoreOwner1;
  }
  
  public static void set(View paramView, ViewModelStoreOwner paramViewModelStoreOwner) {
    paramView.setTag(R.id.view_tree_view_model_store_owner, paramViewModelStoreOwner);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\lifecycle\ViewTreeViewModelStoreOwner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */