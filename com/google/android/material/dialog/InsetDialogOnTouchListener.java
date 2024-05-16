package com.google.android.material.dialog;

import android.app.Dialog;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class InsetDialogOnTouchListener implements View.OnTouchListener {
  private final Dialog dialog;
  
  private final int leftInset;
  
  private final int prePieSlop;
  
  private final int topInset;
  
  public InsetDialogOnTouchListener(Dialog paramDialog, Rect paramRect) {
    this.dialog = paramDialog;
    this.leftInset = paramRect.left;
    this.topInset = paramRect.top;
    this.prePieSlop = ViewConfiguration.get(paramDialog.getContext()).getScaledWindowTouchSlop();
  }
  
  public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
    View view = paramView.findViewById(16908290);
    int k = this.leftInset + view.getLeft();
    int m = view.getWidth();
    int i = this.topInset + view.getTop();
    int j = view.getHeight();
    if ((new RectF(k, i, (m + k), (j + i))).contains(paramMotionEvent.getX(), paramMotionEvent.getY()))
      return false; 
    MotionEvent motionEvent = MotionEvent.obtain(paramMotionEvent);
    if (paramMotionEvent.getAction() == 1)
      motionEvent.setAction(4); 
    if (Build.VERSION.SDK_INT < 28) {
      motionEvent.setAction(0);
      i = this.prePieSlop;
      motionEvent.setLocation((-i - 1), (-i - 1));
    } 
    paramView.performClick();
    return this.dialog.onTouchEvent(motionEvent);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\dialog\InsetDialogOnTouchListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */