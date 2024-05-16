package androidx.appcompat.widget;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckedTextView;
import androidx.appcompat.R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.CheckedTextViewCompat;

class AppCompatCheckedTextViewHelper {
  private ColorStateList mCheckMarkTintList = null;
  
  private PorterDuff.Mode mCheckMarkTintMode = null;
  
  private boolean mHasCheckMarkTint = false;
  
  private boolean mHasCheckMarkTintMode = false;
  
  private boolean mSkipNextApply;
  
  private final CheckedTextView mView;
  
  AppCompatCheckedTextViewHelper(CheckedTextView paramCheckedTextView) {
    this.mView = paramCheckedTextView;
  }
  
  void applyCheckMarkTint() {
    Drawable drawable = CheckedTextViewCompat.getCheckMarkDrawable(this.mView);
    if (drawable != null && (this.mHasCheckMarkTint || this.mHasCheckMarkTintMode)) {
      drawable = DrawableCompat.wrap(drawable).mutate();
      if (this.mHasCheckMarkTint)
        DrawableCompat.setTintList(drawable, this.mCheckMarkTintList); 
      if (this.mHasCheckMarkTintMode)
        DrawableCompat.setTintMode(drawable, this.mCheckMarkTintMode); 
      if (drawable.isStateful())
        drawable.setState(this.mView.getDrawableState()); 
      this.mView.setCheckMarkDrawable(drawable);
    } 
  }
  
  ColorStateList getSupportCheckMarkTintList() {
    return this.mCheckMarkTintList;
  }
  
  PorterDuff.Mode getSupportCheckMarkTintMode() {
    return this.mCheckMarkTintMode;
  }
  
  void loadFromAttributes(AttributeSet paramAttributeSet, int paramInt) {
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), paramAttributeSet, R.styleable.CheckedTextView, paramInt, 0);
    CheckedTextView checkedTextView = this.mView;
    ViewCompat.saveAttributeDataForStyleable((View)checkedTextView, checkedTextView.getContext(), R.styleable.CheckedTextView, paramAttributeSet, tintTypedArray.getWrappedTypeArray(), paramInt, 0);
    boolean bool = false;
    paramInt = bool;
    try {
      if (tintTypedArray.hasValue(R.styleable.CheckedTextView_checkMarkCompat)) {
        int i = tintTypedArray.getResourceId(R.styleable.CheckedTextView_checkMarkCompat, 0);
        paramInt = bool;
        if (i != 0)
          try {
            CheckedTextView checkedTextView1 = this.mView;
            checkedTextView1.setCheckMarkDrawable(AppCompatResources.getDrawable(checkedTextView1.getContext(), i));
            paramInt = 1;
          } catch (android.content.res.Resources.NotFoundException notFoundException) {
            paramInt = bool;
          }  
      } 
      if (paramInt == 0 && tintTypedArray.hasValue(R.styleable.CheckedTextView_android_checkMark)) {
        paramInt = tintTypedArray.getResourceId(R.styleable.CheckedTextView_android_checkMark, 0);
        if (paramInt != 0) {
          CheckedTextView checkedTextView1 = this.mView;
          checkedTextView1.setCheckMarkDrawable(AppCompatResources.getDrawable(checkedTextView1.getContext(), paramInt));
        } 
      } 
      if (tintTypedArray.hasValue(R.styleable.CheckedTextView_checkMarkTint))
        CheckedTextViewCompat.setCheckMarkTintList(this.mView, tintTypedArray.getColorStateList(R.styleable.CheckedTextView_checkMarkTint)); 
      if (tintTypedArray.hasValue(R.styleable.CheckedTextView_checkMarkTintMode))
        CheckedTextViewCompat.setCheckMarkTintMode(this.mView, DrawableUtils.parseTintMode(tintTypedArray.getInt(R.styleable.CheckedTextView_checkMarkTintMode, -1), null)); 
      return;
    } finally {
      tintTypedArray.recycle();
    } 
  }
  
  void onSetCheckMarkDrawable() {
    if (this.mSkipNextApply) {
      this.mSkipNextApply = false;
      return;
    } 
    this.mSkipNextApply = true;
    applyCheckMarkTint();
  }
  
  void setSupportCheckMarkTintList(ColorStateList paramColorStateList) {
    this.mCheckMarkTintList = paramColorStateList;
    this.mHasCheckMarkTint = true;
    applyCheckMarkTint();
  }
  
  void setSupportCheckMarkTintMode(PorterDuff.Mode paramMode) {
    this.mCheckMarkTintMode = paramMode;
    this.mHasCheckMarkTintMode = true;
    applyCheckMarkTint();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\AppCompatCheckedTextViewHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */