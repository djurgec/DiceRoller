package androidx.core.graphics.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.graphics.Rect;
import android.view.Gravity;

class RoundedBitmapDrawable21 extends RoundedBitmapDrawable {
  protected RoundedBitmapDrawable21(Resources paramResources, Bitmap paramBitmap) {
    super(paramResources, paramBitmap);
  }
  
  public void getOutline(Outline paramOutline) {
    updateDstRect();
    paramOutline.setRoundRect(this.mDstRect, getCornerRadius());
  }
  
  void gravityCompatApply(int paramInt1, int paramInt2, int paramInt3, Rect paramRect1, Rect paramRect2) {
    Gravity.apply(paramInt1, paramInt2, paramInt3, paramRect1, paramRect2, 0);
  }
  
  public boolean hasMipMap() {
    boolean bool;
    if (this.mBitmap != null && this.mBitmap.hasMipMap()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void setMipMap(boolean paramBoolean) {
    if (this.mBitmap != null) {
      this.mBitmap.setHasMipMap(paramBoolean);
      invalidateSelf();
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\graphics\drawable\RoundedBitmapDrawable21.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */