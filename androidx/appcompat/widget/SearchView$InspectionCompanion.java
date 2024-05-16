package androidx.appcompat.widget;

import android.view.inspector.InspectionCompanion;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import androidx.appcompat.R;

public final class SearchView$InspectionCompanion implements InspectionCompanion<SearchView> {
  private int mIconifiedByDefaultId;
  
  private int mImeOptionsId;
  
  private int mMaxWidthId;
  
  private boolean mPropertiesMapped = false;
  
  private int mQueryHintId;
  
  public void mapProperties(PropertyMapper paramPropertyMapper) {
    this.mImeOptionsId = paramPropertyMapper.mapInt("imeOptions", 16843364);
    this.mMaxWidthId = paramPropertyMapper.mapInt("maxWidth", 16843039);
    this.mIconifiedByDefaultId = paramPropertyMapper.mapBoolean("iconifiedByDefault", R.attr.iconifiedByDefault);
    this.mQueryHintId = paramPropertyMapper.mapObject("queryHint", R.attr.queryHint);
    this.mPropertiesMapped = true;
  }
  
  public void readProperties(SearchView paramSearchView, PropertyReader paramPropertyReader) {
    if (this.mPropertiesMapped) {
      paramPropertyReader.readInt(this.mImeOptionsId, paramSearchView.getImeOptions());
      paramPropertyReader.readInt(this.mMaxWidthId, paramSearchView.getMaxWidth());
      paramPropertyReader.readBoolean(this.mIconifiedByDefaultId, paramSearchView.isIconfiedByDefault());
      paramPropertyReader.readObject(this.mQueryHintId, paramSearchView.getQueryHint());
      return;
    } 
    throw new InspectionCompanion.UninitializedPropertyMapException();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\SearchView$InspectionCompanion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */