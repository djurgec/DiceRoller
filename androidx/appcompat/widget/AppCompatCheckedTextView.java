package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.CheckedTextView;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.TintableBackgroundView;
import androidx.core.widget.TextViewCompat;
import androidx.core.widget.TintableCheckedTextView;

public class AppCompatCheckedTextView extends CheckedTextView implements TintableCheckedTextView, TintableBackgroundView, EmojiCompatConfigurationView {
  private AppCompatEmojiTextHelper mAppCompatEmojiTextHelper;
  
  private final AppCompatBackgroundHelper mBackgroundTintHelper;
  
  private final AppCompatCheckedTextViewHelper mCheckedHelper;
  
  private final AppCompatTextHelper mTextHelper;
  
  public AppCompatCheckedTextView(Context paramContext) {
    this(paramContext, null);
  }
  
  public AppCompatCheckedTextView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.checkedTextViewStyle);
  }
  
  public AppCompatCheckedTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(TintContextWrapper.wrap(paramContext), paramAttributeSet, paramInt);
    ThemeUtils.checkAppCompatTheme((View)this, getContext());
    AppCompatTextHelper appCompatTextHelper = new AppCompatTextHelper((TextView)this);
    this.mTextHelper = appCompatTextHelper;
    appCompatTextHelper.loadFromAttributes(paramAttributeSet, paramInt);
    appCompatTextHelper.applyCompoundDrawablesTints();
    AppCompatBackgroundHelper appCompatBackgroundHelper = new AppCompatBackgroundHelper((View)this);
    this.mBackgroundTintHelper = appCompatBackgroundHelper;
    appCompatBackgroundHelper.loadFromAttributes(paramAttributeSet, paramInt);
    AppCompatCheckedTextViewHelper appCompatCheckedTextViewHelper = new AppCompatCheckedTextViewHelper(this);
    this.mCheckedHelper = appCompatCheckedTextViewHelper;
    appCompatCheckedTextViewHelper.loadFromAttributes(paramAttributeSet, paramInt);
    getEmojiTextViewHelper().loadFromAttributes(paramAttributeSet, paramInt);
  }
  
  private AppCompatEmojiTextHelper getEmojiTextViewHelper() {
    if (this.mAppCompatEmojiTextHelper == null)
      this.mAppCompatEmojiTextHelper = new AppCompatEmojiTextHelper((TextView)this); 
    return this.mAppCompatEmojiTextHelper;
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
    if (appCompatTextHelper != null)
      appCompatTextHelper.applyCompoundDrawablesTints(); 
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null)
      appCompatBackgroundHelper.applySupportBackgroundTint(); 
    AppCompatCheckedTextViewHelper appCompatCheckedTextViewHelper = this.mCheckedHelper;
    if (appCompatCheckedTextViewHelper != null)
      appCompatCheckedTextViewHelper.applyCheckMarkTint(); 
  }
  
  public ActionMode.Callback getCustomSelectionActionModeCallback() {
    return TextViewCompat.unwrapCustomSelectionActionModeCallback(super.getCustomSelectionActionModeCallback());
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
  
  public ColorStateList getSupportCheckMarkTintList() {
    AppCompatCheckedTextViewHelper appCompatCheckedTextViewHelper = this.mCheckedHelper;
    if (appCompatCheckedTextViewHelper != null) {
      ColorStateList colorStateList = appCompatCheckedTextViewHelper.getSupportCheckMarkTintList();
    } else {
      appCompatCheckedTextViewHelper = null;
    } 
    return (ColorStateList)appCompatCheckedTextViewHelper;
  }
  
  public PorterDuff.Mode getSupportCheckMarkTintMode() {
    AppCompatCheckedTextViewHelper appCompatCheckedTextViewHelper = this.mCheckedHelper;
    if (appCompatCheckedTextViewHelper != null) {
      PorterDuff.Mode mode = appCompatCheckedTextViewHelper.getSupportCheckMarkTintMode();
    } else {
      appCompatCheckedTextViewHelper = null;
    } 
    return (PorterDuff.Mode)appCompatCheckedTextViewHelper;
  }
  
  public boolean isEmojiCompatEnabled() {
    return getEmojiTextViewHelper().isEnabled();
  }
  
  public InputConnection onCreateInputConnection(EditorInfo paramEditorInfo) {
    return AppCompatHintHelper.onCreateInputConnection(super.onCreateInputConnection(paramEditorInfo), paramEditorInfo, (View)this);
  }
  
  public void setAllCaps(boolean paramBoolean) {
    super.setAllCaps(paramBoolean);
    getEmojiTextViewHelper().setAllCaps(paramBoolean);
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
  
  public void setCheckMarkDrawable(int paramInt) {
    setCheckMarkDrawable(AppCompatResources.getDrawable(getContext(), paramInt));
  }
  
  public void setCheckMarkDrawable(Drawable paramDrawable) {
    super.setCheckMarkDrawable(paramDrawable);
    AppCompatCheckedTextViewHelper appCompatCheckedTextViewHelper = this.mCheckedHelper;
    if (appCompatCheckedTextViewHelper != null)
      appCompatCheckedTextViewHelper.onSetCheckMarkDrawable(); 
  }
  
  public void setCustomSelectionActionModeCallback(ActionMode.Callback paramCallback) {
    super.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback((TextView)this, paramCallback));
  }
  
  public void setEmojiCompatEnabled(boolean paramBoolean) {
    getEmojiTextViewHelper().setEnabled(paramBoolean);
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
  
  public void setSupportCheckMarkTintList(ColorStateList paramColorStateList) {
    AppCompatCheckedTextViewHelper appCompatCheckedTextViewHelper = this.mCheckedHelper;
    if (appCompatCheckedTextViewHelper != null)
      appCompatCheckedTextViewHelper.setSupportCheckMarkTintList(paramColorStateList); 
  }
  
  public void setSupportCheckMarkTintMode(PorterDuff.Mode paramMode) {
    AppCompatCheckedTextViewHelper appCompatCheckedTextViewHelper = this.mCheckedHelper;
    if (appCompatCheckedTextViewHelper != null)
      appCompatCheckedTextViewHelper.setSupportCheckMarkTintMode(paramMode); 
  }
  
  public void setTextAppearance(Context paramContext, int paramInt) {
    super.setTextAppearance(paramContext, paramInt);
    AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
    if (appCompatTextHelper != null)
      appCompatTextHelper.onSetTextAppearance(paramContext, paramInt); 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\AppCompatCheckedTextView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */