package androidx.appcompat.widget;

import android.view.inspector.InspectionCompanion;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import androidx.appcompat.R;
import java.util.function.IntFunction;

public final class AppCompatButton$InspectionCompanion implements InspectionCompanion<AppCompatButton> {
  private int mAutoSizeMaxTextSizeId;
  
  private int mAutoSizeMinTextSizeId;
  
  private int mAutoSizeStepGranularityId;
  
  private int mAutoSizeTextTypeId;
  
  private int mBackgroundTintId;
  
  private int mBackgroundTintModeId;
  
  private int mDrawableTintId;
  
  private int mDrawableTintModeId;
  
  private boolean mPropertiesMapped = false;
  
  public void mapProperties(PropertyMapper paramPropertyMapper) {
    this.mAutoSizeMaxTextSizeId = paramPropertyMapper.mapInt("autoSizeMaxTextSize", R.attr.autoSizeMaxTextSize);
    this.mAutoSizeMinTextSizeId = paramPropertyMapper.mapInt("autoSizeMinTextSize", R.attr.autoSizeMinTextSize);
    this.mAutoSizeStepGranularityId = paramPropertyMapper.mapInt("autoSizeStepGranularity", R.attr.autoSizeStepGranularity);
    this.mAutoSizeTextTypeId = paramPropertyMapper.mapIntEnum("autoSizeTextType", R.attr.autoSizeTextType, new IntFunction<String>() {
          final AppCompatButton$InspectionCompanion this$0;
          
          public String apply(int param1Int) {
            switch (param1Int) {
              default:
                return String.valueOf(param1Int);
              case 1:
                return "uniform";
              case 0:
                break;
            } 
            return "none";
          }
        });
    this.mBackgroundTintId = paramPropertyMapper.mapObject("backgroundTint", R.attr.backgroundTint);
    this.mBackgroundTintModeId = paramPropertyMapper.mapObject("backgroundTintMode", R.attr.backgroundTintMode);
    this.mDrawableTintId = paramPropertyMapper.mapObject("drawableTint", R.attr.drawableTint);
    this.mDrawableTintModeId = paramPropertyMapper.mapObject("drawableTintMode", R.attr.drawableTintMode);
    this.mPropertiesMapped = true;
  }
  
  public void readProperties(AppCompatButton paramAppCompatButton, PropertyReader paramPropertyReader) {
    if (this.mPropertiesMapped) {
      paramPropertyReader.readInt(this.mAutoSizeMaxTextSizeId, paramAppCompatButton.getAutoSizeMaxTextSize());
      paramPropertyReader.readInt(this.mAutoSizeMinTextSizeId, paramAppCompatButton.getAutoSizeMinTextSize());
      paramPropertyReader.readInt(this.mAutoSizeStepGranularityId, paramAppCompatButton.getAutoSizeStepGranularity());
      paramPropertyReader.readIntEnum(this.mAutoSizeTextTypeId, paramAppCompatButton.getAutoSizeTextType());
      paramPropertyReader.readObject(this.mBackgroundTintId, paramAppCompatButton.getBackgroundTintList());
      paramPropertyReader.readObject(this.mBackgroundTintModeId, paramAppCompatButton.getBackgroundTintMode());
      paramPropertyReader.readObject(this.mDrawableTintId, paramAppCompatButton.getCompoundDrawableTintList());
      paramPropertyReader.readObject(this.mDrawableTintModeId, paramAppCompatButton.getCompoundDrawableTintMode());
      return;
    } 
    throw new InspectionCompanion.UninitializedPropertyMapException();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\AppCompatButton$InspectionCompanion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */