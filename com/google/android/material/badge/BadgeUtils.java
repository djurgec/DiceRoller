package com.google.android.material.badge;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.R;
import com.google.android.material.internal.ParcelableSparseArray;
import com.google.android.material.internal.ToolbarUtils;

public class BadgeUtils {
  private static final String LOG_TAG = "BadgeUtils";
  
  public static final boolean USE_COMPAT_PARENT;
  
  static {
    boolean bool;
    if (Build.VERSION.SDK_INT < 18) {
      bool = true;
    } else {
      bool = false;
    } 
    USE_COMPAT_PARENT = bool;
  }
  
  public static void attachBadgeDrawable(BadgeDrawable paramBadgeDrawable, View paramView) {
    attachBadgeDrawable(paramBadgeDrawable, paramView, (FrameLayout)null);
  }
  
  public static void attachBadgeDrawable(BadgeDrawable paramBadgeDrawable, View paramView, FrameLayout paramFrameLayout) {
    setBadgeDrawableBounds(paramBadgeDrawable, paramView, paramFrameLayout);
    if (paramBadgeDrawable.getCustomBadgeParent() != null) {
      paramBadgeDrawable.getCustomBadgeParent().setForeground(paramBadgeDrawable);
    } else {
      if (!USE_COMPAT_PARENT) {
        paramView.getOverlay().add(paramBadgeDrawable);
        return;
      } 
      throw new IllegalArgumentException("Trying to reference null customBadgeParent");
    } 
  }
  
  public static void attachBadgeDrawable(BadgeDrawable paramBadgeDrawable, Toolbar paramToolbar, int paramInt) {
    attachBadgeDrawable(paramBadgeDrawable, paramToolbar, paramInt, null);
  }
  
  public static void attachBadgeDrawable(final BadgeDrawable badgeDrawable, final Toolbar toolbar, final int menuItemId, final FrameLayout customBadgeParent) {
    toolbar.post(new Runnable() {
          final BadgeDrawable val$badgeDrawable;
          
          final FrameLayout val$customBadgeParent;
          
          final int val$menuItemId;
          
          final Toolbar val$toolbar;
          
          public void run() {
            ActionMenuItemView actionMenuItemView = ToolbarUtils.getActionMenuItemView(toolbar, menuItemId);
            if (actionMenuItemView != null) {
              BadgeUtils.setToolbarOffset(badgeDrawable, toolbar.getResources());
              BadgeUtils.attachBadgeDrawable(badgeDrawable, (View)actionMenuItemView, customBadgeParent);
            } 
          }
        });
  }
  
  public static SparseArray<BadgeDrawable> createBadgeDrawablesFromSavedStates(Context paramContext, ParcelableSparseArray paramParcelableSparseArray) {
    SparseArray<BadgeDrawable> sparseArray = new SparseArray(paramParcelableSparseArray.size());
    byte b = 0;
    while (b < paramParcelableSparseArray.size()) {
      int i = paramParcelableSparseArray.keyAt(b);
      BadgeDrawable.SavedState savedState = (BadgeDrawable.SavedState)paramParcelableSparseArray.valueAt(b);
      if (savedState != null) {
        sparseArray.put(i, BadgeDrawable.createFromSavedState(paramContext, savedState));
        b++;
        continue;
      } 
      throw new IllegalArgumentException("BadgeDrawable's savedState cannot be null");
    } 
    return sparseArray;
  }
  
  public static ParcelableSparseArray createParcelableBadgeStates(SparseArray<BadgeDrawable> paramSparseArray) {
    ParcelableSparseArray parcelableSparseArray = new ParcelableSparseArray();
    byte b = 0;
    while (b < paramSparseArray.size()) {
      int i = paramSparseArray.keyAt(b);
      BadgeDrawable badgeDrawable = (BadgeDrawable)paramSparseArray.valueAt(b);
      if (badgeDrawable != null) {
        parcelableSparseArray.put(i, badgeDrawable.getSavedState());
        b++;
        continue;
      } 
      throw new IllegalArgumentException("badgeDrawable cannot be null");
    } 
    return parcelableSparseArray;
  }
  
  public static void detachBadgeDrawable(BadgeDrawable paramBadgeDrawable, View paramView) {
    if (paramBadgeDrawable == null)
      return; 
    if (USE_COMPAT_PARENT || paramBadgeDrawable.getCustomBadgeParent() != null) {
      paramBadgeDrawable.getCustomBadgeParent().setForeground(null);
      return;
    } 
    paramView.getOverlay().remove(paramBadgeDrawable);
  }
  
  public static void detachBadgeDrawable(BadgeDrawable paramBadgeDrawable, Toolbar paramToolbar, int paramInt) {
    if (paramBadgeDrawable == null)
      return; 
    ActionMenuItemView actionMenuItemView = ToolbarUtils.getActionMenuItemView(paramToolbar, paramInt);
    if (actionMenuItemView != null) {
      removeToolbarOffset(paramBadgeDrawable);
      detachBadgeDrawable(paramBadgeDrawable, (View)actionMenuItemView);
    } else {
      Log.w("BadgeUtils", "Trying to remove badge from a null menuItemView: " + paramInt);
    } 
  }
  
  static void removeToolbarOffset(BadgeDrawable paramBadgeDrawable) {
    paramBadgeDrawable.setAdditionalHorizontalOffset(0);
    paramBadgeDrawable.setAdditionalVerticalOffset(0);
  }
  
  public static void setBadgeDrawableBounds(BadgeDrawable paramBadgeDrawable, View paramView, FrameLayout paramFrameLayout) {
    Rect rect = new Rect();
    paramView.getDrawingRect(rect);
    paramBadgeDrawable.setBounds(rect);
    paramBadgeDrawable.updateBadgeCoordinates(paramView, paramFrameLayout);
  }
  
  static void setToolbarOffset(BadgeDrawable paramBadgeDrawable, Resources paramResources) {
    paramBadgeDrawable.setAdditionalHorizontalOffset(paramResources.getDimensionPixelOffset(R.dimen.mtrl_badge_toolbar_action_menu_item_horizontal_offset));
    paramBadgeDrawable.setAdditionalVerticalOffset(paramResources.getDimensionPixelOffset(R.dimen.mtrl_badge_toolbar_action_menu_item_vertical_offset));
  }
  
  public static void updateBadgeBounds(Rect paramRect, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    paramRect.set((int)(paramFloat1 - paramFloat3), (int)(paramFloat2 - paramFloat4), (int)(paramFloat1 + paramFloat3), (int)(paramFloat2 + paramFloat4));
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\badge\BadgeUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */