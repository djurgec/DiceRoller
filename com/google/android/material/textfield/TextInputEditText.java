package com.google.android.material.textfield;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import androidx.appcompat.widget.AppCompatEditText;
import com.google.android.material.R;
import com.google.android.material.internal.ManufacturerUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;

public class TextInputEditText extends AppCompatEditText {
  private final Rect parentRect = new Rect();
  
  private boolean textInputLayoutFocusedRectEnabled;
  
  public TextInputEditText(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public TextInputEditText(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.editTextStyle);
  }
  
  public TextInputEditText(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(MaterialThemeOverlay.wrap(paramContext, paramAttributeSet, paramInt, 0), paramAttributeSet, paramInt);
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.TextInputEditText, paramInt, R.style.Widget_Design_TextInputEditText, new int[0]);
    setTextInputLayoutFocusedRectEnabled(typedArray.getBoolean(R.styleable.TextInputEditText_textInputLayoutFocusedRectEnabled, false));
    typedArray.recycle();
  }
  
  private String getAccessibilityNodeInfoText(TextInputLayout paramTextInputLayout) {
    Editable editable = getText();
    CharSequence charSequence = paramTextInputLayout.getHint();
    boolean bool1 = TextUtils.isEmpty((CharSequence)editable);
    boolean bool2 = TextUtils.isEmpty(charSequence);
    if (Build.VERSION.SDK_INT >= 17)
      setLabelFor(R.id.textinput_helper_text); 
    String str = "";
    if ((bool2 ^ true) != 0) {
      charSequence = charSequence.toString();
    } else {
      charSequence = "";
    } 
    if ((bool1 ^ true) != 0) {
      StringBuilder stringBuilder = (new StringBuilder()).append(editable);
      if (!TextUtils.isEmpty(charSequence))
        str = ", " + charSequence; 
      return stringBuilder.append(str).toString();
    } 
    return (String)(!TextUtils.isEmpty(charSequence) ? charSequence : "");
  }
  
  private CharSequence getHintFromLayout() {
    TextInputLayout textInputLayout = getTextInputLayout();
    if (textInputLayout != null) {
      CharSequence charSequence = textInputLayout.getHint();
    } else {
      textInputLayout = null;
    } 
    return (CharSequence)textInputLayout;
  }
  
  private TextInputLayout getTextInputLayout() {
    for (ViewParent viewParent = getParent(); viewParent instanceof android.view.View; viewParent = viewParent.getParent()) {
      if (viewParent instanceof TextInputLayout)
        return (TextInputLayout)viewParent; 
    } 
    return null;
  }
  
  public void getFocusedRect(Rect paramRect) {
    super.getFocusedRect(paramRect);
    TextInputLayout textInputLayout = getTextInputLayout();
    if (textInputLayout != null && this.textInputLayoutFocusedRectEnabled && paramRect != null) {
      textInputLayout.getFocusedRect(this.parentRect);
      paramRect.bottom = this.parentRect.bottom;
    } 
  }
  
  public boolean getGlobalVisibleRect(Rect paramRect, Point paramPoint) {
    boolean bool = super.getGlobalVisibleRect(paramRect, paramPoint);
    TextInputLayout textInputLayout = getTextInputLayout();
    if (textInputLayout != null && this.textInputLayoutFocusedRectEnabled && paramRect != null) {
      textInputLayout.getGlobalVisibleRect(this.parentRect, paramPoint);
      paramRect.bottom = this.parentRect.bottom;
    } 
    return bool;
  }
  
  public CharSequence getHint() {
    TextInputLayout textInputLayout = getTextInputLayout();
    return (textInputLayout != null && textInputLayout.isProvidingHint()) ? textInputLayout.getHint() : super.getHint();
  }
  
  public boolean isTextInputLayoutFocusedRectEnabled() {
    return this.textInputLayoutFocusedRectEnabled;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    TextInputLayout textInputLayout = getTextInputLayout();
    if (textInputLayout != null && textInputLayout.isProvidingHint() && super.getHint() == null && ManufacturerUtils.isMeizuDevice())
      setHint(""); 
  }
  
  public InputConnection onCreateInputConnection(EditorInfo paramEditorInfo) {
    InputConnection inputConnection = super.onCreateInputConnection(paramEditorInfo);
    if (inputConnection != null && paramEditorInfo.hintText == null)
      paramEditorInfo.hintText = getHintFromLayout(); 
    return inputConnection;
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    TextInputLayout textInputLayout = getTextInputLayout();
    if (Build.VERSION.SDK_INT < 23 && textInputLayout != null)
      paramAccessibilityNodeInfo.setText(getAccessibilityNodeInfoText(textInputLayout)); 
  }
  
  public boolean requestRectangleOnScreen(Rect paramRect) {
    boolean bool = super.requestRectangleOnScreen(paramRect);
    TextInputLayout textInputLayout = getTextInputLayout();
    if (textInputLayout != null && this.textInputLayoutFocusedRectEnabled) {
      this.parentRect.set(0, textInputLayout.getHeight() - getResources().getDimensionPixelOffset(R.dimen.mtrl_edittext_rectangle_top_offset), textInputLayout.getWidth(), textInputLayout.getHeight());
      textInputLayout.requestRectangleOnScreen(this.parentRect, true);
    } 
    return bool;
  }
  
  public void setTextInputLayoutFocusedRectEnabled(boolean paramBoolean) {
    this.textInputLayoutFocusedRectEnabled = paramBoolean;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\textfield\TextInputEditText.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */