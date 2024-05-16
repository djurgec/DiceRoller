package com.google.android.material.textfield;

class CustomEndIconDelegate extends EndIconDelegate {
  CustomEndIconDelegate(TextInputLayout paramTextInputLayout) {
    super(paramTextInputLayout);
  }
  
  void initialize() {
    this.textInputLayout.setEndIconOnClickListener(null);
    this.textInputLayout.setEndIconOnLongClickListener(null);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\textfield\CustomEndIconDelegate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */