package androidx.appcompat.widget;

import android.view.inspector.InspectionCompanion;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import androidx.appcompat.R;
import java.util.HashSet;
import java.util.Set;
import java.util.function.IntFunction;

public final class LinearLayoutCompat$InspectionCompanion implements InspectionCompanion<LinearLayoutCompat> {
  private int mBaselineAlignedChildIndexId;
  
  private int mBaselineAlignedId;
  
  private int mDividerId;
  
  private int mDividerPaddingId;
  
  private int mGravityId;
  
  private int mMeasureWithLargestChildId;
  
  private int mOrientationId;
  
  private boolean mPropertiesMapped = false;
  
  private int mShowDividersId;
  
  private int mWeightSumId;
  
  public void mapProperties(PropertyMapper paramPropertyMapper) {
    this.mBaselineAlignedId = paramPropertyMapper.mapBoolean("baselineAligned", 16843046);
    this.mBaselineAlignedChildIndexId = paramPropertyMapper.mapInt("baselineAlignedChildIndex", 16843047);
    this.mGravityId = paramPropertyMapper.mapGravity("gravity", 16842927);
    this.mOrientationId = paramPropertyMapper.mapIntEnum("orientation", 16842948, new IntFunction<String>() {
          final LinearLayoutCompat$InspectionCompanion this$0;
          
          public String apply(int param1Int) {
            switch (param1Int) {
              default:
                return String.valueOf(param1Int);
              case 1:
                return "vertical";
              case 0:
                break;
            } 
            return "horizontal";
          }
        });
    this.mWeightSumId = paramPropertyMapper.mapFloat("weightSum", 16843048);
    this.mDividerId = paramPropertyMapper.mapObject("divider", R.attr.divider);
    this.mDividerPaddingId = paramPropertyMapper.mapInt("dividerPadding", R.attr.dividerPadding);
    this.mMeasureWithLargestChildId = paramPropertyMapper.mapBoolean("measureWithLargestChild", R.attr.measureWithLargestChild);
    this.mShowDividersId = paramPropertyMapper.mapIntFlag("showDividers", R.attr.showDividers, new IntFunction<Set<String>>() {
          final LinearLayoutCompat$InspectionCompanion this$0;
          
          public Set<String> apply(int param1Int) {
            HashSet<String> hashSet = new HashSet();
            if (param1Int == 0)
              hashSet.add("none"); 
            if (param1Int == 1)
              hashSet.add("beginning"); 
            if (param1Int == 2)
              hashSet.add("middle"); 
            if (param1Int == 4)
              hashSet.add("end"); 
            return hashSet;
          }
        });
    this.mPropertiesMapped = true;
  }
  
  public void readProperties(LinearLayoutCompat paramLinearLayoutCompat, PropertyReader paramPropertyReader) {
    if (this.mPropertiesMapped) {
      paramPropertyReader.readBoolean(this.mBaselineAlignedId, paramLinearLayoutCompat.isBaselineAligned());
      paramPropertyReader.readInt(this.mBaselineAlignedChildIndexId, paramLinearLayoutCompat.getBaselineAlignedChildIndex());
      paramPropertyReader.readGravity(this.mGravityId, paramLinearLayoutCompat.getGravity());
      paramPropertyReader.readIntEnum(this.mOrientationId, paramLinearLayoutCompat.getOrientation());
      paramPropertyReader.readFloat(this.mWeightSumId, paramLinearLayoutCompat.getWeightSum());
      paramPropertyReader.readObject(this.mDividerId, paramLinearLayoutCompat.getDividerDrawable());
      paramPropertyReader.readInt(this.mDividerPaddingId, paramLinearLayoutCompat.getDividerPadding());
      paramPropertyReader.readBoolean(this.mMeasureWithLargestChildId, paramLinearLayoutCompat.isMeasureWithLargestChildEnabled());
      paramPropertyReader.readIntFlag(this.mShowDividersId, paramLinearLayoutCompat.getShowDividers());
      return;
    } 
    throw new InspectionCompanion.UninitializedPropertyMapException();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\LinearLayoutCompat$InspectionCompanion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */