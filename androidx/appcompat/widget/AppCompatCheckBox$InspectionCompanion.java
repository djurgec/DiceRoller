package androidx.appcompat.widget;

import android.view.inspector.InspectionCompanion;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import androidx.appcompat.R;

public final class AppCompatCheckBox$InspectionCompanion implements InspectionCompanion<AppCompatCheckBox> {
  private int mBackgroundTintId;
  
  private int mBackgroundTintModeId;
  
  private int mButtonTintId;
  
  private int mButtonTintModeId;
  
  private boolean mPropertiesMapped = false;
  
  public void mapProperties(PropertyMapper paramPropertyMapper) {
    this.mBackgroundTintId = paramPropertyMapper.mapObject("backgroundTint", R.attr.backgroundTint);
    this.mBackgroundTintModeId = paramPropertyMapper.mapObject("backgroundTintMode", R.attr.backgroundTintMode);
    this.mButtonTintId = paramPropertyMapper.mapObject("buttonTint", R.attr.buttonTint);
    this.mButtonTintModeId = paramPropertyMapper.mapObject("buttonTintMode", R.attr.buttonTintMode);
    this.mPropertiesMapped = true;
  }
  
  public void readProperties(AppCompatCheckBox paramAppCompatCheckBox, PropertyReader paramPropertyReader) {
    if (this.mPropertiesMapped) {
      paramPropertyReader.readObject(this.mBackgroundTintId, paramAppCompatCheckBox.getBackgroundTintList());
      paramPropertyReader.readObject(this.mBackgroundTintModeId, paramAppCompatCheckBox.getBackgroundTintMode());
      paramPropertyReader.readObject(this.mButtonTintId, paramAppCompatCheckBox.getButtonTintList());
      paramPropertyReader.readObject(this.mButtonTintModeId, paramAppCompatCheckBox.getButtonTintMode());
      return;
    } 
    throw new InspectionCompanion.UninitializedPropertyMapException();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\AppCompatCheckBox$InspectionCompanion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */