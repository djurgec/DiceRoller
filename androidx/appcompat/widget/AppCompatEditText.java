package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.DragEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.textclassifier.TextClassifier;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.core.view.ContentInfoCompat;
import androidx.core.view.OnReceiveContentViewBehavior;
import androidx.core.view.TintableBackgroundView;
import androidx.core.view.ViewCompat;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.view.inputmethod.InputConnectionCompat;
import androidx.core.widget.TextViewCompat;
import androidx.core.widget.TextViewOnReceiveContentListener;

public class AppCompatEditText extends EditText implements TintableBackgroundView, OnReceiveContentViewBehavior, EmojiCompatConfigurationView {
  private final AppCompatEmojiEditTextHelper mAppCompatEmojiEditTextHelper;
  
  private final AppCompatBackgroundHelper mBackgroundTintHelper;
  
  private final TextViewOnReceiveContentListener mDefaultOnReceiveContentListener;
  
  private final AppCompatTextClassifierHelper mTextClassifierHelper;
  
  private final AppCompatTextHelper mTextHelper;
  
  public AppCompatEditText(Context paramContext) {
    this(paramContext, null);
  }
  
  public AppCompatEditText(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.editTextStyle);
  }
  
  public AppCompatEditText(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(TintContextWrapper.wrap(paramContext), paramAttributeSet, paramInt);
    ThemeUtils.checkAppCompatTheme((View)this, getContext());
    AppCompatBackgroundHelper appCompatBackgroundHelper = new AppCompatBackgroundHelper((View)this);
    this.mBackgroundTintHelper = appCompatBackgroundHelper;
    appCompatBackgroundHelper.loadFromAttributes(paramAttributeSet, paramInt);
    AppCompatTextHelper appCompatTextHelper = new AppCompatTextHelper((TextView)this);
    this.mTextHelper = appCompatTextHelper;
    appCompatTextHelper.loadFromAttributes(paramAttributeSet, paramInt);
    appCompatTextHelper.applyCompoundDrawablesTints();
    this.mTextClassifierHelper = new AppCompatTextClassifierHelper((TextView)this);
    this.mDefaultOnReceiveContentListener = new TextViewOnReceiveContentListener();
    AppCompatEmojiEditTextHelper appCompatEmojiEditTextHelper = new AppCompatEmojiEditTextHelper(this);
    this.mAppCompatEmojiEditTextHelper = appCompatEmojiEditTextHelper;
    appCompatEmojiEditTextHelper.loadFromAttributes(paramAttributeSet, paramInt);
    appCompatEmojiEditTextHelper.initKeyListener();
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null)
      appCompatBackgroundHelper.applySupportBackgroundTint(); 
    AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
    if (appCompatTextHelper != null)
      appCompatTextHelper.applyCompoundDrawablesTints(); 
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
  
  public Editable getText() {
    return (Build.VERSION.SDK_INT >= 28) ? super.getText() : getEditableText();
  }
  
  public TextClassifier getTextClassifier() {
    if (Build.VERSION.SDK_INT < 28) {
      AppCompatTextClassifierHelper appCompatTextClassifierHelper = this.mTextClassifierHelper;
      if (appCompatTextClassifierHelper != null)
        return appCompatTextClassifierHelper.getTextClassifier(); 
    } 
    return super.getTextClassifier();
  }
  
  public boolean isEmojiCompatEnabled() {
    return this.mAppCompatEmojiEditTextHelper.isEnabled();
  }
  
  public InputConnection onCreateInputConnection(EditorInfo paramEditorInfo) {
    InputConnection inputConnection1 = super.onCreateInputConnection(paramEditorInfo);
    this.mTextHelper.populateSurroundingTextIfNeeded((TextView)this, inputConnection1, paramEditorInfo);
    InputConnection inputConnection2 = AppCompatHintHelper.onCreateInputConnection(inputConnection1, paramEditorInfo, (View)this);
    inputConnection1 = inputConnection2;
    if (inputConnection2 != null) {
      inputConnection1 = inputConnection2;
      if (Build.VERSION.SDK_INT <= 30) {
        String[] arrayOfString = ViewCompat.getOnReceiveContentMimeTypes((View)this);
        inputConnection1 = inputConnection2;
        if (arrayOfString != null) {
          EditorInfoCompat.setContentMimeTypes(paramEditorInfo, arrayOfString);
          inputConnection1 = InputConnectionCompat.createWrapper((View)this, inputConnection2, paramEditorInfo);
        } 
      } 
    } 
    return this.mAppCompatEmojiEditTextHelper.onCreateInputConnection(inputConnection1, paramEditorInfo);
  }
  
  public boolean onDragEvent(DragEvent paramDragEvent) {
    return AppCompatReceiveContentHelper.maybeHandleDragEventViaPerformReceiveContent((View)this, paramDragEvent) ? true : super.onDragEvent(paramDragEvent);
  }
  
  public ContentInfoCompat onReceiveContent(ContentInfoCompat paramContentInfoCompat) {
    return this.mDefaultOnReceiveContentListener.onReceiveContent((View)this, paramContentInfoCompat);
  }
  
  public boolean onTextContextMenuItem(int paramInt) {
    return AppCompatReceiveContentHelper.maybeHandleMenuActionViaPerformReceiveContent((TextView)this, paramInt) ? true : super.onTextContextMenuItem(paramInt);
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
  
  public void setCustomSelectionActionModeCallback(ActionMode.Callback paramCallback) {
    super.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback((TextView)this, paramCallback));
  }
  
  public void setEmojiCompatEnabled(boolean paramBoolean) {
    this.mAppCompatEmojiEditTextHelper.setEnabled(paramBoolean);
  }
  
  public void setKeyListener(KeyListener paramKeyListener) {
    super.setKeyListener(this.mAppCompatEmojiEditTextHelper.getKeyListener(paramKeyListener));
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
  
  public void setTextAppearance(Context paramContext, int paramInt) {
    super.setTextAppearance(paramContext, paramInt);
    AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
    if (appCompatTextHelper != null)
      appCompatTextHelper.onSetTextAppearance(paramContext, paramInt); 
  }
  
  public void setTextClassifier(TextClassifier paramTextClassifier) {
    if (Build.VERSION.SDK_INT < 28) {
      AppCompatTextClassifierHelper appCompatTextClassifierHelper = this.mTextClassifierHelper;
      if (appCompatTextClassifierHelper != null) {
        appCompatTextClassifierHelper.setTextClassifier(paramTextClassifier);
        return;
      } 
    } 
    super.setTextClassifier(paramTextClassifier);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\AppCompatEditText.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */