package androidx.appcompat.widget;

import android.view.inspector.InspectionCompanion;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import androidx.appcompat.R;

public final class AppCompatImageButton$InspectionCompanion implements InspectionCompanion<AppCompatImageButton> {
  private int mBackgroundTintId;
  
  private int mBackgroundTintModeId;
  
  private boolean mPropertiesMapped = false;
  
  private int mTintId;
  
  private int mTintModeId;
  
  public void mapProperties(PropertyMapper paramPropertyMapper) {
    this.mBackgroundTintId = paramPropertyMapper.mapObject("backgroundTint", R.attr.backgroundTint);
    this.mBackgroundTintModeId = paramPropertyMapper.mapObject("backgroundTintMode", R.attr.backgroundTintMode);
    this.mTintId = paramPropertyMapper.mapObject("tint", R.attr.tint);
    this.mTintModeId = paramPropertyMapper.mapObject("tintMode", R.attr.tintMode);
    this.mPropertiesMapped = true;
  }
  
  public void readProperties(AppCompatImageButton paramAppCompatImageButton, PropertyReader paramPropertyReader) {
    if (this.mPropertiesMapped) {
      paramPropertyReader.readObject(this.mBackgroundTintId, paramAppCompatImageButton.getBackgroundTintList());
      paramPropertyReader.readObject(this.mBackgroundTintModeId, paramAppCompatImageButton.getBackgroundTintMode());
      paramPropertyReader.readObject(this.mTintId, paramAppCompatImageButton.getImageTintList());
      paramPropertyReader.readObject(this.mTintModeId, paramAppCompatImageButton.getImageTintMode());
      return;
    } 
    throw new InspectionCompanion.UninitializedPropertyMapException();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\AppCompatImageButton$InspectionCompanion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */