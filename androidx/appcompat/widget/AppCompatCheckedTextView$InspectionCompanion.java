package androidx.appcompat.widget;

import android.view.inspector.InspectionCompanion;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import androidx.appcompat.R;

public final class AppCompatCheckedTextView$InspectionCompanion implements InspectionCompanion<AppCompatCheckedTextView> {
  private int mBackgroundTintId;
  
  private int mBackgroundTintModeId;
  
  private int mCheckMarkTintId;
  
  private int mCheckMarkTintModeId;
  
  private boolean mPropertiesMapped = false;
  
  public void mapProperties(PropertyMapper paramPropertyMapper) {
    this.mBackgroundTintId = paramPropertyMapper.mapObject("backgroundTint", R.attr.backgroundTint);
    this.mBackgroundTintModeId = paramPropertyMapper.mapObject("backgroundTintMode", R.attr.backgroundTintMode);
    this.mCheckMarkTintId = paramPropertyMapper.mapObject("checkMarkTint", R.attr.checkMarkTint);
    this.mCheckMarkTintModeId = paramPropertyMapper.mapObject("checkMarkTintMode", R.attr.checkMarkTintMode);
    this.mPropertiesMapped = true;
  }
  
  public void readProperties(AppCompatCheckedTextView paramAppCompatCheckedTextView, PropertyReader paramPropertyReader) {
    if (this.mPropertiesMapped) {
      paramPropertyReader.readObject(this.mBackgroundTintId, paramAppCompatCheckedTextView.getBackgroundTintList());
      paramPropertyReader.readObject(this.mBackgroundTintModeId, paramAppCompatCheckedTextView.getBackgroundTintMode());
      paramPropertyReader.readObject(this.mCheckMarkTintId, paramAppCompatCheckedTextView.getCheckMarkTintList());
      paramPropertyReader.readObject(this.mCheckMarkTintModeId, paramAppCompatCheckedTextView.getCheckMarkTintMode());
      return;
    } 
    throw new InspectionCompanion.UninitializedPropertyMapException();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\AppCompatCheckedTextView$InspectionCompanion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */