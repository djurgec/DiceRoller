package androidx.transition;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewOverlay;

class ViewOverlayApi18 implements ViewOverlayImpl {
  private final ViewOverlay mViewOverlay;
  
  ViewOverlayApi18(View paramView) {
    this.mViewOverlay = paramView.getOverlay();
  }
  
  public void add(Drawable paramDrawable) {
    this.mViewOverlay.add(paramDrawable);
  }
  
  public void remove(Drawable paramDrawable) {
    this.mViewOverlay.remove(paramDrawable);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\ViewOverlayApi18.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */