package androidx.appcompat.widget;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import androidx.appcompat.R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.CompoundButtonCompat;

class AppCompatCompoundButtonHelper {
  private ColorStateList mButtonTintList = null;
  
  private PorterDuff.Mode mButtonTintMode = null;
  
  private boolean mHasButtonTint = false;
  
  private boolean mHasButtonTintMode = false;
  
  private boolean mSkipNextApply;
  
  private final CompoundButton mView;
  
  AppCompatCompoundButtonHelper(CompoundButton paramCompoundButton) {
    this.mView = paramCompoundButton;
  }
  
  void applyButtonTint() {
    Drawable drawable = CompoundButtonCompat.getButtonDrawable(this.mView);
    if (drawable != null && (this.mHasButtonTint || this.mHasButtonTintMode)) {
      drawable = DrawableCompat.wrap(drawable).mutate();
      if (this.mHasButtonTint)
        DrawableCompat.setTintList(drawable, this.mButtonTintList); 
      if (this.mHasButtonTintMode)
        DrawableCompat.setTintMode(drawable, this.mButtonTintMode); 
      if (drawable.isStateful())
        drawable.setState(this.mView.getDrawableState()); 
      this.mView.setButtonDrawable(drawable);
    } 
  }
  
  int getCompoundPaddingLeft(int paramInt) {
    int i = paramInt;
    if (Build.VERSION.SDK_INT < 17) {
      Drawable drawable = CompoundButtonCompat.getButtonDrawable(this.mView);
      i = paramInt;
      if (drawable != null)
        i = paramInt + drawable.getIntrinsicWidth(); 
    } 
    return i;
  }
  
  ColorStateList getSupportButtonTintList() {
    return this.mButtonTintList;
  }
  
  PorterDuff.Mode getSupportButtonTintMode() {
    return this.mButtonTintMode;
  }
  
  void loadFromAttributes(AttributeSet paramAttributeSet, int paramInt) {
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), paramAttributeSet, R.styleable.CompoundButton, paramInt, 0);
    CompoundButton compoundButton = this.mView;
    ViewCompat.saveAttributeDataForStyleable((View)compoundButton, compoundButton.getContext(), R.styleable.CompoundButton, paramAttributeSet, tintTypedArray.getWrappedTypeArray(), paramInt, 0);
    boolean bool = false;
    paramInt = bool;
    try {
      if (tintTypedArray.hasValue(R.styleable.CompoundButton_buttonCompat)) {
        int i = tintTypedArray.getResourceId(R.styleable.CompoundButton_buttonCompat, 0);
        paramInt = bool;
        if (i != 0)
          try {
            CompoundButton compoundButton1 = this.mView;
            compoundButton1.setButtonDrawable(AppCompatResources.getDrawable(compoundButton1.getContext(), i));
            paramInt = 1;
          } catch (android.content.res.Resources.NotFoundException notFoundException) {
            paramInt = bool;
          }  
      } 
      if (paramInt == 0 && tintTypedArray.hasValue(R.styleable.CompoundButton_android_button)) {
        paramInt = tintTypedArray.getResourceId(R.styleable.CompoundButton_android_button, 0);
        if (paramInt != 0) {
          CompoundButton compoundButton1 = this.mView;
          compoundButton1.setButtonDrawable(AppCompatResources.getDrawable(compoundButton1.getContext(), paramInt));
        } 
      } 
      if (tintTypedArray.hasValue(R.styleable.CompoundButton_buttonTint))
        CompoundButtonCompat.setButtonTintList(this.mView, tintTypedArray.getColorStateList(R.styleable.CompoundButton_buttonTint)); 
      if (tintTypedArray.hasValue(R.styleable.CompoundButton_buttonTintMode))
        CompoundButtonCompat.setButtonTintMode(this.mView, DrawableUtils.parseTintMode(tintTypedArray.getInt(R.styleable.CompoundButton_buttonTintMode, -1), null)); 
      return;
    } finally {
      tintTypedArray.recycle();
    } 
  }
  
  void onSetButtonDrawable() {
    if (this.mSkipNextApply) {
      this.mSkipNextApply = false;
      return;
    } 
    this.mSkipNextApply = true;
    applyButtonTint();
  }
  
  void setSupportButtonTintList(ColorStateList paramColorStateList) {
    this.mButtonTintList = paramColorStateList;
    this.mHasButtonTint = true;
    applyButtonTint();
  }
  
  void setSupportButtonTintMode(PorterDuff.Mode paramMode) {
    this.mButtonTintMode = paramMode;
    this.mHasButtonTintMode = true;
    applyButtonTint();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\AppCompatCompoundButtonHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */