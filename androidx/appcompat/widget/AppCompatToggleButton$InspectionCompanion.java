package androidx.appcompat.widget;

import android.view.inspector.InspectionCompanion;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import androidx.appcompat.R;

public final class AppCompatToggleButton$InspectionCompanion implements InspectionCompanion<AppCompatToggleButton> {
  private int mBackgroundTintId;
  
  private int mBackgroundTintModeId;
  
  private boolean mPropertiesMapped = false;
  
  public void mapProperties(PropertyMapper paramPropertyMapper) {
    this.mBackgroundTintId = paramPropertyMapper.mapObject("backgroundTint", R.attr.backgroundTint);
    this.mBackgroundTintModeId = paramPropertyMapper.mapObject("backgroundTintMode", R.attr.backgroundTintMode);
    this.mPropertiesMapped = true;
  }
  
  public void readProperties(AppCompatToggleButton paramAppCompatToggleButton, PropertyReader paramPropertyReader) {
    if (this.mPropertiesMapped) {
      paramPropertyReader.readObject(this.mBackgroundTintId, paramAppCompatToggleButton.getBackgroundTintList());
      paramPropertyReader.readObject(this.mBackgroundTintModeId, paramAppCompatToggleButton.getBackgroundTintMode());
      return;
    } 
    throw new InspectionCompanion.UninitializedPropertyMapException();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\AppCompatToggleButton$InspectionCompanion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */