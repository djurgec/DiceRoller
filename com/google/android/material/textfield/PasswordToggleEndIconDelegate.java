package com.google.android.material.textfield;

import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.content.res.AppCompatResources;
import com.google.android.material.R;
import com.google.android.material.internal.TextWatcherAdapter;

class PasswordToggleEndIconDelegate extends EndIconDelegate {
  private final TextInputLayout.OnEditTextAttachedListener onEditTextAttachedListener = new TextInputLayout.OnEditTextAttachedListener() {
      final PasswordToggleEndIconDelegate this$0;
      
      public void onEditTextAttached(TextInputLayout param1TextInputLayout) {
        EditText editText = param1TextInputLayout.getEditText();
        param1TextInputLayout.setEndIconVisible(true);
        param1TextInputLayout.setEndIconCheckable(true);
        PasswordToggleEndIconDelegate.this.endIconView.setChecked(true ^ PasswordToggleEndIconDelegate.this.hasPasswordTransformation());
        editText.removeTextChangedListener(PasswordToggleEndIconDelegate.this.textWatcher);
        editText.addTextChangedListener(PasswordToggleEndIconDelegate.this.textWatcher);
      }
    };
  
  private final TextInputLayout.OnEndIconChangedListener onEndIconChangedListener = new TextInputLayout.OnEndIconChangedListener() {
      final PasswordToggleEndIconDelegate this$0;
      
      public void onEndIconChanged(TextInputLayout param1TextInputLayout, int param1Int) {
        final EditText editText = param1TextInputLayout.getEditText();
        if (editText != null && param1Int == 1) {
          editText.setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance());
          editText.post(new Runnable() {
                final PasswordToggleEndIconDelegate.null this$1;
                
                final EditText val$editText;
                
                public void run() {
                  editText.removeTextChangedListener(PasswordToggleEndIconDelegate.this.textWatcher);
                }
              });
        } 
      }
    };
  
  private final TextWatcher textWatcher = (TextWatcher)new TextWatcherAdapter() {
      final PasswordToggleEndIconDelegate this$0;
      
      public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {
        PasswordToggleEndIconDelegate.this.endIconView.setChecked(PasswordToggleEndIconDelegate.this.hasPasswordTransformation() ^ true);
      }
    };
  
  PasswordToggleEndIconDelegate(TextInputLayout paramTextInputLayout) {
    super(paramTextInputLayout);
  }
  
  private boolean hasPasswordTransformation() {
    boolean bool;
    EditText editText = this.textInputLayout.getEditText();
    if (editText != null && editText.getTransformationMethod() instanceof PasswordTransformationMethod) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static boolean isInputTypePassword(EditText paramEditText) {
    boolean bool;
    if (paramEditText != null && (paramEditText.getInputType() == 16 || paramEditText.getInputType() == 128 || paramEditText.getInputType() == 144 || paramEditText.getInputType() == 224)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  void initialize() {
    this.textInputLayout.setEndIconDrawable(AppCompatResources.getDrawable(this.context, R.drawable.design_password_eye));
    this.textInputLayout.setEndIconContentDescription(this.textInputLayout.getResources().getText(R.string.password_toggle_content_description));
    this.textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
          final PasswordToggleEndIconDelegate this$0;
          
          public void onClick(View param1View) {
            EditText editText = PasswordToggleEndIconDelegate.this.textInputLayout.getEditText();
            if (editText == null)
              return; 
            int i = editText.getSelectionEnd();
            if (PasswordToggleEndIconDelegate.this.hasPasswordTransformation()) {
              editText.setTransformationMethod(null);
            } else {
              editText.setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance());
            } 
            if (i >= 0)
              editText.setSelection(i); 
            PasswordToggleEndIconDelegate.this.textInputLayout.refreshEndIconDrawableState();
          }
        });
    this.textInputLayout.addOnEditTextAttachedListener(this.onEditTextAttachedListener);
    this.textInputLayout.addOnEndIconChangedListener(this.onEndIconChangedListener);
    EditText editText = this.textInputLayout.getEditText();
    if (isInputTypePassword(editText))
      editText.setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance()); 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\textfield\PasswordToggleEndIconDelegate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */