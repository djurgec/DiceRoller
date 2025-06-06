package androidx.transition;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Scene {
  private Context mContext;
  
  private Runnable mEnterAction;
  
  private Runnable mExitAction;
  
  private View mLayout;
  
  private int mLayoutId = -1;
  
  private ViewGroup mSceneRoot;
  
  public Scene(ViewGroup paramViewGroup) {
    this.mSceneRoot = paramViewGroup;
  }
  
  private Scene(ViewGroup paramViewGroup, int paramInt, Context paramContext) {
    this.mContext = paramContext;
    this.mSceneRoot = paramViewGroup;
    this.mLayoutId = paramInt;
  }
  
  public Scene(ViewGroup paramViewGroup, View paramView) {
    this.mSceneRoot = paramViewGroup;
    this.mLayout = paramView;
  }
  
  public static Scene getCurrentScene(ViewGroup paramViewGroup) {
    return (Scene)paramViewGroup.getTag(R.id.transition_current_scene);
  }
  
  public static Scene getSceneForLayout(ViewGroup paramViewGroup, int paramInt, Context paramContext) {
    SparseArray sparseArray2 = (SparseArray)paramViewGroup.getTag(R.id.transition_scene_layoutid_cache);
    SparseArray sparseArray1 = sparseArray2;
    if (sparseArray2 == null) {
      sparseArray1 = new SparseArray();
      paramViewGroup.setTag(R.id.transition_scene_layoutid_cache, sparseArray1);
    } 
    Scene scene2 = (Scene)sparseArray1.get(paramInt);
    if (scene2 != null)
      return scene2; 
    Scene scene1 = new Scene(paramViewGroup, paramInt, paramContext);
    sparseArray1.put(paramInt, scene1);
    return scene1;
  }
  
  static void setCurrentScene(ViewGroup paramViewGroup, Scene paramScene) {
    paramViewGroup.setTag(R.id.transition_current_scene, paramScene);
  }
  
  public void enter() {
    if (this.mLayoutId > 0 || this.mLayout != null) {
      getSceneRoot().removeAllViews();
      if (this.mLayoutId > 0) {
        LayoutInflater.from(this.mContext).inflate(this.mLayoutId, this.mSceneRoot);
      } else {
        this.mSceneRoot.addView(this.mLayout);
      } 
    } 
    Runnable runnable = this.mEnterAction;
    if (runnable != null)
      runnable.run(); 
    setCurrentScene(this.mSceneRoot, this);
  }
  
  public void exit() {
    if (getCurrentScene(this.mSceneRoot) == this) {
      Runnable runnable = this.mExitAction;
      if (runnable != null)
        runnable.run(); 
    } 
  }
  
  public ViewGroup getSceneRoot() {
    return this.mSceneRoot;
  }
  
  boolean isCreatedFromLayoutResource() {
    boolean bool;
    if (this.mLayoutId > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void setEnterAction(Runnable paramRunnable) {
    this.mEnterAction = paramRunnable;
  }
  
  public void setExitAction(Runnable paramRunnable) {
    this.mExitAction = paramRunnable;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\Scene.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */