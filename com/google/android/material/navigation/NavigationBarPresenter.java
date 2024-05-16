package com.google.android.material.navigation;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.ViewGroup;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.view.menu.SubMenuBuilder;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.internal.ParcelableSparseArray;

public class NavigationBarPresenter implements MenuPresenter {
  private int id;
  
  private MenuBuilder menu;
  
  private NavigationBarMenuView menuView;
  
  private boolean updateSuspended = false;
  
  public boolean collapseItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl) {
    return false;
  }
  
  public boolean expandItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl) {
    return false;
  }
  
  public boolean flagActionItems() {
    return false;
  }
  
  public int getId() {
    return this.id;
  }
  
  public MenuView getMenuView(ViewGroup paramViewGroup) {
    return this.menuView;
  }
  
  public void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder) {
    this.menu = paramMenuBuilder;
    this.menuView.initialize(paramMenuBuilder);
  }
  
  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {}
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    if (paramParcelable instanceof SavedState) {
      this.menuView.tryRestoreSelectedItemId(((SavedState)paramParcelable).selectedItemId);
      SparseArray<BadgeDrawable> sparseArray = BadgeUtils.createBadgeDrawablesFromSavedStates(this.menuView.getContext(), ((SavedState)paramParcelable).badgeSavedStates);
      this.menuView.setBadgeDrawables(sparseArray);
    } 
  }
  
  public Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState();
    savedState.selectedItemId = this.menuView.getSelectedItemId();
    savedState.badgeSavedStates = BadgeUtils.createParcelableBadgeStates(this.menuView.getBadgeDrawables());
    return savedState;
  }
  
  public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder) {
    return false;
  }
  
  public void setCallback(MenuPresenter.Callback paramCallback) {}
  
  public void setId(int paramInt) {
    this.id = paramInt;
  }
  
  public void setMenuView(NavigationBarMenuView paramNavigationBarMenuView) {
    this.menuView = paramNavigationBarMenuView;
  }
  
  public void setUpdateSuspended(boolean paramBoolean) {
    this.updateSuspended = paramBoolean;
  }
  
  public void updateMenuView(boolean paramBoolean) {
    if (this.updateSuspended)
      return; 
    if (paramBoolean) {
      this.menuView.buildMenuView();
    } else {
      this.menuView.updateMenuView();
    } 
  }
  
  static class SavedState implements Parcelable {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public NavigationBarPresenter.SavedState createFromParcel(Parcel param2Parcel) {
          return new NavigationBarPresenter.SavedState(param2Parcel);
        }
        
        public NavigationBarPresenter.SavedState[] newArray(int param2Int) {
          return new NavigationBarPresenter.SavedState[param2Int];
        }
      };
    
    ParcelableSparseArray badgeSavedStates;
    
    int selectedItemId;
    
    SavedState() {}
    
    SavedState(Parcel param1Parcel) {
      this.selectedItemId = param1Parcel.readInt();
      this.badgeSavedStates = (ParcelableSparseArray)param1Parcel.readParcelable(getClass().getClassLoader());
    }
    
    public int describeContents() {
      return 0;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      param1Parcel.writeInt(this.selectedItemId);
      param1Parcel.writeParcelable((Parcelable)this.badgeSavedStates, 0);
    }
  }
  
  static final class null implements Parcelable.Creator<SavedState> {
    public NavigationBarPresenter.SavedState createFromParcel(Parcel param1Parcel) {
      return new NavigationBarPresenter.SavedState(param1Parcel);
    }
    
    public NavigationBarPresenter.SavedState[] newArray(int param1Int) {
      return new NavigationBarPresenter.SavedState[param1Int];
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\navigation\NavigationBarPresenter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */