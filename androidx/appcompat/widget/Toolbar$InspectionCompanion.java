package androidx.appcompat.widget;

import android.view.inspector.InspectionCompanion;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import androidx.appcompat.R;

public final class Toolbar$InspectionCompanion implements InspectionCompanion<Toolbar> {
  private int mCollapseContentDescriptionId;
  
  private int mCollapseIconId;
  
  private int mContentInsetEndId;
  
  private int mContentInsetEndWithActionsId;
  
  private int mContentInsetLeftId;
  
  private int mContentInsetRightId;
  
  private int mContentInsetStartId;
  
  private int mContentInsetStartWithNavigationId;
  
  private int mLogoDescriptionId;
  
  private int mLogoId;
  
  private int mMenuId;
  
  private int mNavigationContentDescriptionId;
  
  private int mNavigationIconId;
  
  private int mPopupThemeId;
  
  private boolean mPropertiesMapped = false;
  
  private int mSubtitleId;
  
  private int mTitleId;
  
  private int mTitleMarginBottomId;
  
  private int mTitleMarginEndId;
  
  private int mTitleMarginStartId;
  
  private int mTitleMarginTopId;
  
  public void mapProperties(PropertyMapper paramPropertyMapper) {
    this.mCollapseContentDescriptionId = paramPropertyMapper.mapObject("collapseContentDescription", R.attr.collapseContentDescription);
    this.mCollapseIconId = paramPropertyMapper.mapObject("collapseIcon", R.attr.collapseIcon);
    this.mContentInsetEndId = paramPropertyMapper.mapInt("contentInsetEnd", R.attr.contentInsetEnd);
    this.mContentInsetEndWithActionsId = paramPropertyMapper.mapInt("contentInsetEndWithActions", R.attr.contentInsetEndWithActions);
    this.mContentInsetLeftId = paramPropertyMapper.mapInt("contentInsetLeft", R.attr.contentInsetLeft);
    this.mContentInsetRightId = paramPropertyMapper.mapInt("contentInsetRight", R.attr.contentInsetRight);
    this.mContentInsetStartId = paramPropertyMapper.mapInt("contentInsetStart", R.attr.contentInsetStart);
    this.mContentInsetStartWithNavigationId = paramPropertyMapper.mapInt("contentInsetStartWithNavigation", R.attr.contentInsetStartWithNavigation);
    this.mLogoId = paramPropertyMapper.mapObject("logo", R.attr.logo);
    this.mLogoDescriptionId = paramPropertyMapper.mapObject("logoDescription", R.attr.logoDescription);
    this.mMenuId = paramPropertyMapper.mapObject("menu", R.attr.menu);
    this.mNavigationContentDescriptionId = paramPropertyMapper.mapObject("navigationContentDescription", R.attr.navigationContentDescription);
    this.mNavigationIconId = paramPropertyMapper.mapObject("navigationIcon", R.attr.navigationIcon);
    this.mPopupThemeId = paramPropertyMapper.mapResourceId("popupTheme", R.attr.popupTheme);
    this.mSubtitleId = paramPropertyMapper.mapObject("subtitle", R.attr.subtitle);
    this.mTitleId = paramPropertyMapper.mapObject("title", R.attr.title);
    this.mTitleMarginBottomId = paramPropertyMapper.mapInt("titleMarginBottom", R.attr.titleMarginBottom);
    this.mTitleMarginEndId = paramPropertyMapper.mapInt("titleMarginEnd", R.attr.titleMarginEnd);
    this.mTitleMarginStartId = paramPropertyMapper.mapInt("titleMarginStart", R.attr.titleMarginStart);
    this.mTitleMarginTopId = paramPropertyMapper.mapInt("titleMarginTop", R.attr.titleMarginTop);
    this.mPropertiesMapped = true;
  }
  
  public void readProperties(Toolbar paramToolbar, PropertyReader paramPropertyReader) {
    if (this.mPropertiesMapped) {
      paramPropertyReader.readObject(this.mCollapseContentDescriptionId, paramToolbar.getCollapseContentDescription());
      paramPropertyReader.readObject(this.mCollapseIconId, paramToolbar.getCollapseIcon());
      paramPropertyReader.readInt(this.mContentInsetEndId, paramToolbar.getContentInsetEnd());
      paramPropertyReader.readInt(this.mContentInsetEndWithActionsId, paramToolbar.getContentInsetEndWithActions());
      paramPropertyReader.readInt(this.mContentInsetLeftId, paramToolbar.getContentInsetLeft());
      paramPropertyReader.readInt(this.mContentInsetRightId, paramToolbar.getContentInsetRight());
      paramPropertyReader.readInt(this.mContentInsetStartId, paramToolbar.getContentInsetStart());
      paramPropertyReader.readInt(this.mContentInsetStartWithNavigationId, paramToolbar.getContentInsetStartWithNavigation());
      paramPropertyReader.readObject(this.mLogoId, paramToolbar.getLogo());
      paramPropertyReader.readObject(this.mLogoDescriptionId, paramToolbar.getLogoDescription());
      paramPropertyReader.readObject(this.mMenuId, paramToolbar.getMenu());
      paramPropertyReader.readObject(this.mNavigationContentDescriptionId, paramToolbar.getNavigationContentDescription());
      paramPropertyReader.readObject(this.mNavigationIconId, paramToolbar.getNavigationIcon());
      paramPropertyReader.readResourceId(this.mPopupThemeId, paramToolbar.getPopupTheme());
      paramPropertyReader.readObject(this.mSubtitleId, paramToolbar.getSubtitle());
      paramPropertyReader.readObject(this.mTitleId, paramToolbar.getTitle());
      paramPropertyReader.readInt(this.mTitleMarginBottomId, paramToolbar.getTitleMarginBottom());
      paramPropertyReader.readInt(this.mTitleMarginEndId, paramToolbar.getTitleMarginEnd());
      paramPropertyReader.readInt(this.mTitleMarginStartId, paramToolbar.getTitleMarginStart());
      paramPropertyReader.readInt(this.mTitleMarginTopId, paramToolbar.getTitleMarginTop());
      return;
    } 
    throw new InspectionCompanion.UninitializedPropertyMapException();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\Toolbar$InspectionCompanion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */