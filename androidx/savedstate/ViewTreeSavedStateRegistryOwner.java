package androidx.savedstate;

import android.view.View;
import android.view.ViewParent;

public final class ViewTreeSavedStateRegistryOwner {
  public static SavedStateRegistryOwner get(View paramView) {
    SavedStateRegistryOwner savedStateRegistryOwner2 = (SavedStateRegistryOwner)paramView.getTag(R.id.view_tree_saved_state_registry_owner);
    if (savedStateRegistryOwner2 != null)
      return savedStateRegistryOwner2; 
    ViewParent viewParent = paramView.getParent();
    SavedStateRegistryOwner savedStateRegistryOwner1 = savedStateRegistryOwner2;
    while (savedStateRegistryOwner1 == null && viewParent instanceof View) {
      View view = (View)viewParent;
      savedStateRegistryOwner1 = (SavedStateRegistryOwner)view.getTag(R.id.view_tree_saved_state_registry_owner);
      ViewParent viewParent1 = view.getParent();
    } 
    return savedStateRegistryOwner1;
  }
  
  public static void set(View paramView, SavedStateRegistryOwner paramSavedStateRegistryOwner) {
    paramView.setTag(R.id.view_tree_saved_state_registry_owner, paramSavedStateRegistryOwner);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\savedstate\ViewTreeSavedStateRegistryOwner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */