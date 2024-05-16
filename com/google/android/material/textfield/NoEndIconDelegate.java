package com.google.android.material.textfield;

import android.graphics.drawable.Drawable;

class NoEndIconDelegate extends EndIconDelegate {
  NoEndIconDelegate(TextInputLayout paramTextInputLayout) {
    super(paramTextInputLayout);
  }
  
  void initialize() {
    this.textInputLayout.setEndIconOnClickListener(null);
    this.textInputLayout.setEndIconDrawable((Drawable)null);
    this.textInputLayout.setEndIconContentDescription((CharSequence)null);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\textfield\NoEndIconDelegate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */