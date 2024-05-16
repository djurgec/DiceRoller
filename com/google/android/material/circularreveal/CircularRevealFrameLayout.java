package com.google.android.material.circularreveal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class CircularRevealFrameLayout extends FrameLayout implements CircularRevealWidget {
  private final CircularRevealHelper helper = new CircularRevealHelper(this);
  
  public CircularRevealFrameLayout(Context paramContext) {
    this(paramContext, null);
  }
  
  public CircularRevealFrameLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  public void actualDraw(Canvas paramCanvas) {
    super.draw(paramCanvas);
  }
  
  public boolean actualIsOpaque() {
    return super.isOpaque();
  }
  
  public void buildCircularRevealCache() {
    this.helper.buildCircularRevealCache();
  }
  
  public void destroyCircularRevealCache() {
    this.helper.destroyCircularRevealCache();
  }
  
  public void draw(Canvas paramCanvas) {
    CircularRevealHelper circularRevealHelper = this.helper;
    if (circularRevealHelper != null) {
      circularRevealHelper.draw(paramCanvas);
    } else {
      super.draw(paramCanvas);
    } 
  }
  
  public Drawable getCircularRevealOverlayDrawable() {
    return this.helper.getCircularRevealOverlayDrawable();
  }
  
  public int getCircularRevealScrimColor() {
    return this.helper.getCircularRevealScrimColor();
  }
  
  public CircularRevealWidget.RevealInfo getRevealInfo() {
    return this.helper.getRevealInfo();
  }
  
  public boolean isOpaque() {
    CircularRevealHelper circularRevealHelper = this.helper;
    return (circularRevealHelper != null) ? circularRevealHelper.isOpaque() : super.isOpaque();
  }
  
  public void setCircularRevealOverlayDrawable(Drawable paramDrawable) {
    this.helper.setCircularRevealOverlayDrawable(paramDrawable);
  }
  
  public void setCircularRevealScrimColor(int paramInt) {
    this.helper.setCircularRevealScrimColor(paramInt);
  }
  
  public void setRevealInfo(CircularRevealWidget.RevealInfo paramRevealInfo) {
    this.helper.setRevealInfo(paramRevealInfo);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\circularreveal\CircularRevealFrameLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */