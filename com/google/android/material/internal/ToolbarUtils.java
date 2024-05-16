package com.google.android.material.internal;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;

public class ToolbarUtils {
  public static ActionMenuItemView getActionMenuItemView(Toolbar paramToolbar, int paramInt) {
    ActionMenuView actionMenuView = getActionMenuView(paramToolbar);
    if (actionMenuView != null)
      for (byte b = 0; b < actionMenuView.getChildCount(); b++) {
        View view = actionMenuView.getChildAt(b);
        if (view instanceof ActionMenuItemView) {
          ActionMenuItemView actionMenuItemView = (ActionMenuItemView)view;
          if (actionMenuItemView.getItemData().getItemId() == paramInt)
            return actionMenuItemView; 
        } 
      }  
    return null;
  }
  
  public static ActionMenuView getActionMenuView(Toolbar paramToolbar) {
    for (byte b = 0; b < paramToolbar.getChildCount(); b++) {
      View view = paramToolbar.getChildAt(b);
      if (view instanceof ActionMenuView)
        return (ActionMenuView)view; 
    } 
    return null;
  }
  
  public static ImageButton getNavigationIconButton(Toolbar paramToolbar) {
    Drawable drawable = paramToolbar.getNavigationIcon();
    if (drawable == null)
      return null; 
    for (byte b = 0; b < paramToolbar.getChildCount(); b++) {
      View view = paramToolbar.getChildAt(b);
      if (view instanceof ImageButton) {
        ImageButton imageButton = (ImageButton)view;
        if (imageButton.getDrawable() == drawable)
          return imageButton; 
      } 
    } 
    return null;
  }
  
  public static View getSecondaryActionMenuItemView(Toolbar paramToolbar) {
    ActionMenuView actionMenuView = getActionMenuView(paramToolbar);
    return (actionMenuView != null && actionMenuView.getChildCount() > 1) ? actionMenuView.getChildAt(0) : null;
  }
  
  public static TextView getSubtitleTextView(Toolbar paramToolbar) {
    return getTextView(paramToolbar, paramToolbar.getSubtitle());
  }
  
  private static TextView getTextView(Toolbar paramToolbar, CharSequence paramCharSequence) {
    for (byte b = 0; b < paramToolbar.getChildCount(); b++) {
      View view = paramToolbar.getChildAt(b);
      if (view instanceof TextView) {
        TextView textView = (TextView)view;
        if (TextUtils.equals(textView.getText(), paramCharSequence))
          return textView; 
      } 
    } 
    return null;
  }
  
  public static TextView getTitleTextView(Toolbar paramToolbar) {
    return getTextView(paramToolbar, paramToolbar.getTitle());
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\internal\ToolbarUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */