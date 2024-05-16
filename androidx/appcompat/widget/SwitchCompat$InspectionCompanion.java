package androidx.appcompat.widget;

import android.view.inspector.InspectionCompanion;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import androidx.appcompat.R;

public final class SwitchCompat$InspectionCompanion implements InspectionCompanion<SwitchCompat> {
  private boolean mPropertiesMapped = false;
  
  private int mShowTextId;
  
  private int mSplitTrackId;
  
  private int mSwitchMinWidthId;
  
  private int mSwitchPaddingId;
  
  private int mTextOffId;
  
  private int mTextOnId;
  
  private int mThumbId;
  
  private int mThumbTextPaddingId;
  
  private int mThumbTintId;
  
  private int mThumbTintModeId;
  
  private int mTrackId;
  
  private int mTrackTintId;
  
  private int mTrackTintModeId;
  
  public void mapProperties(PropertyMapper paramPropertyMapper) {
    this.mTextOffId = paramPropertyMapper.mapObject("textOff", 16843045);
    this.mTextOnId = paramPropertyMapper.mapObject("textOn", 16843044);
    this.mThumbId = paramPropertyMapper.mapObject("thumb", 16843074);
    this.mShowTextId = paramPropertyMapper.mapBoolean("showText", R.attr.showText);
    this.mSplitTrackId = paramPropertyMapper.mapBoolean("splitTrack", R.attr.splitTrack);
    this.mSwitchMinWidthId = paramPropertyMapper.mapInt("switchMinWidth", R.attr.switchMinWidth);
    this.mSwitchPaddingId = paramPropertyMapper.mapInt("switchPadding", R.attr.switchPadding);
    this.mThumbTextPaddingId = paramPropertyMapper.mapInt("thumbTextPadding", R.attr.thumbTextPadding);
    this.mThumbTintId = paramPropertyMapper.mapObject("thumbTint", R.attr.thumbTint);
    this.mThumbTintModeId = paramPropertyMapper.mapObject("thumbTintMode", R.attr.thumbTintMode);
    this.mTrackId = paramPropertyMapper.mapObject("track", R.attr.track);
    this.mTrackTintId = paramPropertyMapper.mapObject("trackTint", R.attr.trackTint);
    this.mTrackTintModeId = paramPropertyMapper.mapObject("trackTintMode", R.attr.trackTintMode);
    this.mPropertiesMapped = true;
  }
  
  public void readProperties(SwitchCompat paramSwitchCompat, PropertyReader paramPropertyReader) {
    if (this.mPropertiesMapped) {
      paramPropertyReader.readObject(this.mTextOffId, paramSwitchCompat.getTextOff());
      paramPropertyReader.readObject(this.mTextOnId, paramSwitchCompat.getTextOn());
      paramPropertyReader.readObject(this.mThumbId, paramSwitchCompat.getThumbDrawable());
      paramPropertyReader.readBoolean(this.mShowTextId, paramSwitchCompat.getShowText());
      paramPropertyReader.readBoolean(this.mSplitTrackId, paramSwitchCompat.getSplitTrack());
      paramPropertyReader.readInt(this.mSwitchMinWidthId, paramSwitchCompat.getSwitchMinWidth());
      paramPropertyReader.readInt(this.mSwitchPaddingId, paramSwitchCompat.getSwitchPadding());
      paramPropertyReader.readInt(this.mThumbTextPaddingId, paramSwitchCompat.getThumbTextPadding());
      paramPropertyReader.readObject(this.mThumbTintId, paramSwitchCompat.getThumbTintList());
      paramPropertyReader.readObject(this.mThumbTintModeId, paramSwitchCompat.getThumbTintMode());
      paramPropertyReader.readObject(this.mTrackId, paramSwitchCompat.getTrackDrawable());
      paramPropertyReader.readObject(this.mTrackTintId, paramSwitchCompat.getTrackTintList());
      paramPropertyReader.readObject(this.mTrackTintModeId, paramSwitchCompat.getTrackTintMode());
      return;
    } 
    throw new InspectionCompanion.UninitializedPropertyMapException();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\SwitchCompat$InspectionCompanion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */