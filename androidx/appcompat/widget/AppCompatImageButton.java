package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.R;
import androidx.core.view.TintableBackgroundView;
import androidx.core.widget.TintableImageSourceView;

public class AppCompatImageButton extends ImageButton implements TintableBackgroundView, TintableImageSourceView {
  private final AppCompatBackgroundHelper mBackgroundTintHelper;
  
  private boolean mHasLevel = false;
  
  private final AppCompatImageHelper mImageHelper;
  
  public AppCompatImageButton(Context paramContext) {
    this(paramContext, null);
  }
  
  public AppCompatImageButton(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.imageButtonStyle);
  }
  
  public AppCompatImageButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(TintContextWrapper.wrap(paramContext), paramAttributeSet, paramInt);
    ThemeUtils.checkAppCompatTheme((View)this, getContext());
    AppCompatBackgroundHelper appCompatBackgroundHelper = new AppCompatBackgroundHelper((View)this);
    this.mBackgroundTintHelper = appCompatBackgroundHelper;
    appCompatBackgroundHelper.loadFromAttributes(paramAttributeSet, paramInt);
    AppCompatImageHelper appCompatImageHelper = new AppCompatImageHelper((ImageView)this);
    this.mImageHelper = appCompatImageHelper;
    appCompatImageHelper.loadFromAttributes(paramAttributeSet, paramInt);
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null)
      appCompatBackgroundHelper.applySupportBackgroundTint(); 
    AppCompatImageHelper appCompatImageHelper = this.mImageHelper;
    if (appCompatImageHelper != null)
      appCompatImageHelper.applySupportImageTint(); 
  }
  
  public ColorStateList getSupportBackgroundTintList() {
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null) {
      ColorStateList colorStateList = appCompatBackgroundHelper.getSupportBackgroundTintList();
    } else {
      appCompatBackgroundHelper = null;
    } 
    return (ColorStateList)appCompatBackgroundHelper;
  }
  
  public PorterDuff.Mode getSupportBackgroundTintMode() {
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null) {
      PorterDuff.Mode mode = appCompatBackgroundHelper.getSupportBackgroundTintMode();
    } else {
      appCompatBackgroundHelper = null;
    } 
    return (PorterDuff.Mode)appCompatBackgroundHelper;
  }
  
  public ColorStateList getSupportImageTintList() {
    AppCompatImageHelper appCompatImageHelper = this.mImageHelper;
    if (appCompatImageHelper != null) {
      ColorStateList colorStateList = appCompatImageHelper.getSupportImageTintList();
    } else {
      appCompatImageHelper = null;
    } 
    return (ColorStateList)appCompatImageHelper;
  }
  
  public PorterDuff.Mode getSupportImageTintMode() {
    AppCompatImageHelper appCompatImageHelper = this.mImageHelper;
    if (appCompatImageHelper != null) {
      PorterDuff.Mode mode = appCompatImageHelper.getSupportImageTintMode();
    } else {
      appCompatImageHelper = null;
    } 
    return (PorterDuff.Mode)appCompatImageHelper;
  }
  
  public boolean hasOverlappingRendering() {
    boolean bool;
    if (this.mImageHelper.hasOverlappingRendering() && super.hasOverlappingRendering()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void setBackgroundDrawable(Drawable paramDrawable) {
    super.setBackgroundDrawable(paramDrawable);
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null)
      appCompatBackgroundHelper.onSetBackgroundDrawable(paramDrawable); 
  }
  
  public void setBackgroundResource(int paramInt) {
    super.setBackgroundResource(paramInt);
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null)
      appCompatBackgroundHelper.onSetBackgroundResource(paramInt); 
  }
  
  public void setImageBitmap(Bitmap paramBitmap) {
    super.setImageBitmap(paramBitmap);
    AppCompatImageHelper appCompatImageHelper = this.mImageHelper;
    if (appCompatImageHelper != null)
      appCompatImageHelper.applySupportImageTint(); 
  }
  
  public void setImageDrawable(Drawable paramDrawable) {
    AppCompatImageHelper appCompatImageHelper2 = this.mImageHelper;
    if (appCompatImageHelper2 != null && paramDrawable != null && !this.mHasLevel)
      appCompatImageHelper2.obtainLevelFromDrawable(paramDrawable); 
    super.setImageDrawable(paramDrawable);
    AppCompatImageHelper appCompatImageHelper1 = this.mImageHelper;
    if (appCompatImageHelper1 != null) {
      appCompatImageHelper1.applySupportImageTint();
      if (!this.mHasLevel)
        this.mImageHelper.applyImageLevel(); 
    } 
  }
  
  public void setImageLevel(int paramInt) {
    super.setImageLevel(paramInt);
    this.mHasLevel = true;
  }
  
  public void setImageResource(int paramInt) {
    this.mImageHelper.setImageResource(paramInt);
  }
  
  public void setImageURI(Uri paramUri) {
    super.setImageURI(paramUri);
    AppCompatImageHelper appCompatImageHelper = this.mImageHelper;
    if (appCompatImageHelper != null)
      appCompatImageHelper.applySupportImageTint(); 
  }
  
  public void setSupportBackgroundTintList(ColorStateList paramColorStateList) {
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null)
      appCompatBackgroundHelper.setSupportBackgroundTintList(paramColorStateList); 
  }
  
  public void setSupportBackgroundTintMode(PorterDuff.Mode paramMode) {
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null)
      appCompatBackgroundHelper.setSupportBackgroundTintMode(paramMode); 
  }
  
  public void setSupportImageTintList(ColorStateList paramColorStateList) {
    AppCompatImageHelper appCompatImageHelper = this.mImageHelper;
    if (appCompatImageHelper != null)
      appCompatImageHelper.setSupportImageTintList(paramColorStateList); 
  }
  
  public void setSupportImageTintMode(PorterDuff.Mode paramMode) {
    AppCompatImageHelper appCompatImageHelper = this.mImageHelper;
    if (appCompatImageHelper != null)
      appCompatImageHelper.setSupportImageTintMode(paramMode); 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\AppCompatImageButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */