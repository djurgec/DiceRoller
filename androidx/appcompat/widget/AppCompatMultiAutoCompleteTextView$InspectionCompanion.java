package androidx.appcompat.widget;

import android.view.inspector.InspectionCompanion;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import androidx.appcompat.R;

public final class AppCompatMultiAutoCompleteTextView$InspectionCompanion implements InspectionCompanion<AppCompatMultiAutoCompleteTextView> {
  private int mBackgroundTintId;
  
  private int mBackgroundTintModeId;
  
  private boolean mPropertiesMapped = false;
  
  public void mapProperties(PropertyMapper paramPropertyMapper) {
    this.mBackgroundTintId = paramPropertyMapper.mapObject("backgroundTint", R.attr.backgroundTint);
    this.mBackgroundTintModeId = paramPropertyMapper.mapObject("backgroundTintMode", R.attr.backgroundTintMode);
    this.mPropertiesMapped = true;
  }
  
  public void readProperties(AppCompatMultiAutoCompleteTextView paramAppCompatMultiAutoCompleteTextView, PropertyReader paramPropertyReader) {
    if (this.mPropertiesMapped) {
      paramPropertyReader.readObject(this.mBackgroundTintId, paramAppCompatMultiAutoCompleteTextView.getBackgroundTintList());
      paramPropertyReader.readObject(this.mBackgroundTintModeId, paramAppCompatMultiAutoCompleteTextView.getBackgroundTintMode());
      return;
    } 
    throw new InspectionCompanion.UninitializedPropertyMapException();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\AppCompatMultiAutoCompleteTextView$InspectionCompanion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */