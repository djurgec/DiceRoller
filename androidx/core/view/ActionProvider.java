package androidx.core.view;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

public abstract class ActionProvider {
  private static final String TAG = "ActionProvider(support)";
  
  private final Context mContext;
  
  private SubUiVisibilityListener mSubUiVisibilityListener;
  
  private VisibilityListener mVisibilityListener;
  
  public ActionProvider(Context paramContext) {
    this.mContext = paramContext;
  }
  
  public Context getContext() {
    return this.mContext;
  }
  
  public boolean hasSubMenu() {
    return false;
  }
  
  public boolean isVisible() {
    return true;
  }
  
  public abstract View onCreateActionView();
  
  public View onCreateActionView(MenuItem paramMenuItem) {
    return onCreateActionView();
  }
  
  public boolean onPerformDefaultAction() {
    return false;
  }
  
  public void onPrepareSubMenu(SubMenu paramSubMenu) {}
  
  public boolean overridesItemVisibility() {
    return false;
  }
  
  public void refreshVisibility() {
    if (this.mVisibilityListener != null && overridesItemVisibility())
      this.mVisibilityListener.onActionProviderVisibilityChanged(isVisible()); 
  }
  
  public void reset() {
    this.mVisibilityListener = null;
    this.mSubUiVisibilityListener = null;
  }
  
  public void setSubUiVisibilityListener(SubUiVisibilityListener paramSubUiVisibilityListener) {
    this.mSubUiVisibilityListener = paramSubUiVisibilityListener;
  }
  
  public void setVisibilityListener(VisibilityListener paramVisibilityListener) {
    if (this.mVisibilityListener != null && paramVisibilityListener != null)
      Log.w("ActionProvider(support)", "setVisibilityListener: Setting a new ActionProvider.VisibilityListener when one is already set. Are you reusing this " + getClass().getSimpleName() + " instance while it is still in use somewhere else?"); 
    this.mVisibilityListener = paramVisibilityListener;
  }
  
  public void subUiVisibilityChanged(boolean paramBoolean) {
    SubUiVisibilityListener subUiVisibilityListener = this.mSubUiVisibilityListener;
    if (subUiVisibilityListener != null)
      subUiVisibilityListener.onSubUiVisibilityChanged(paramBoolean); 
  }
  
  public static interface SubUiVisibilityListener {
    void onSubUiVisibilityChanged(boolean param1Boolean);
  }
  
  public static interface VisibilityListener {
    void onActionProviderVisibilityChanged(boolean param1Boolean);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\view\ActionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */