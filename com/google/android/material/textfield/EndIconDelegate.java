package com.google.android.material.textfield;

import android.content.Context;
import com.google.android.material.internal.CheckableImageButton;

abstract class EndIconDelegate {
  Context context;
  
  CheckableImageButton endIconView;
  
  TextInputLayout textInputLayout;
  
  EndIconDelegate(TextInputLayout paramTextInputLayout) {
    this.textInputLayout = paramTextInputLayout;
    this.context = paramTextInputLayout.getContext();
    this.endIconView = paramTextInputLayout.getEndIconView();
  }
  
  abstract void initialize();
  
  boolean isBoxBackgroundModeSupported(int paramInt) {
    return true;
  }
  
  void onSuffixVisibilityChanged(boolean paramBoolean) {}
  
  boolean shouldTintIconOnError() {
    return false;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\textfield\EndIconDelegate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */