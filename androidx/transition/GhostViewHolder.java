package androidx.transition;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import java.util.ArrayList;

class GhostViewHolder extends FrameLayout {
  private boolean mAttached;
  
  private ViewGroup mParent;
  
  GhostViewHolder(ViewGroup paramViewGroup) {
    super(paramViewGroup.getContext());
    setClipChildren(false);
    this.mParent = paramViewGroup;
    paramViewGroup.setTag(R.id.ghost_view_holder, this);
    ViewGroupUtils.getOverlay(this.mParent).add((View)this);
    this.mAttached = true;
  }
  
  static GhostViewHolder getHolder(ViewGroup paramViewGroup) {
    return (GhostViewHolder)paramViewGroup.getTag(R.id.ghost_view_holder);
  }
  
  private int getInsertIndex(ArrayList<View> paramArrayList) {
    ArrayList<View> arrayList = new ArrayList();
    int j = 0;
    int i = getChildCount() - 1;
    while (j <= i) {
      int k = (j + i) / 2;
      getParents(((GhostViewPort)getChildAt(k)).mView, arrayList);
      if (isOnTop(paramArrayList, arrayList)) {
        j = k + 1;
      } else {
        i = k - 1;
      } 
      arrayList.clear();
    } 
    return j;
  }
  
  private static void getParents(View paramView, ArrayList<View> paramArrayList) {
    ViewParent viewParent = paramView.getParent();
    if (viewParent instanceof ViewGroup)
      getParents((View)viewParent, paramArrayList); 
    paramArrayList.add(paramView);
  }
  
  private static boolean isOnTop(View paramView1, View paramView2) {
    boolean bool1;
    ViewGroup viewGroup = (ViewGroup)paramView1.getParent();
    int i = viewGroup.getChildCount();
    if (Build.VERSION.SDK_INT >= 21 && paramView1.getZ() != paramView2.getZ()) {
      if (paramView1.getZ() > paramView2.getZ()) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      return bool1;
    } 
    boolean bool2 = true;
    byte b = 0;
    while (true) {
      bool1 = bool2;
      if (b < i) {
        View view = viewGroup.getChildAt(ViewGroupUtils.getChildDrawingOrder(viewGroup, b));
        if (view == paramView1) {
          bool1 = false;
          break;
        } 
        if (view == paramView2) {
          bool1 = true;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    return bool1;
  }
  
  private static boolean isOnTop(ArrayList<View> paramArrayList1, ArrayList<View> paramArrayList2) {
    boolean bool1 = paramArrayList1.isEmpty();
    boolean bool = true;
    if (bool1 || paramArrayList2.isEmpty() || paramArrayList1.get(0) != paramArrayList2.get(0))
      return true; 
    int i = Math.min(paramArrayList1.size(), paramArrayList2.size());
    for (byte b = 1; b < i; b++) {
      View view1 = paramArrayList1.get(b);
      View view2 = paramArrayList2.get(b);
      if (view1 != view2)
        return isOnTop(view1, view2); 
    } 
    if (paramArrayList2.size() != i)
      bool = false; 
    return bool;
  }
  
  void addGhostView(GhostViewPort paramGhostViewPort) {
    ArrayList<View> arrayList = new ArrayList();
    getParents(paramGhostViewPort.mView, arrayList);
    int i = getInsertIndex(arrayList);
    if (i < 0 || i >= getChildCount()) {
      addView((View)paramGhostViewPort);
      return;
    } 
    addView((View)paramGhostViewPort, i);
  }
  
  public void onViewAdded(View paramView) {
    if (this.mAttached) {
      super.onViewAdded(paramView);
      return;
    } 
    throw new IllegalStateException("This GhostViewHolder is detached!");
  }
  
  public void onViewRemoved(View paramView) {
    super.onViewRemoved(paramView);
    if ((getChildCount() == 1 && getChildAt(0) == paramView) || getChildCount() == 0) {
      this.mParent.setTag(R.id.ghost_view_holder, null);
      ViewGroupUtils.getOverlay(this.mParent).remove((View)this);
      this.mAttached = false;
    } 
  }
  
  void popToOverlayTop() {
    if (this.mAttached) {
      ViewGroupUtils.getOverlay(this.mParent).remove((View)this);
      ViewGroupUtils.getOverlay(this.mParent).add((View)this);
      return;
    } 
    throw new IllegalStateException("This GhostViewHolder is detached!");
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\GhostViewHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */