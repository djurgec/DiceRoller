package androidx.appcompat.widget;

import android.content.res.TypedArray;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import androidx.appcompat.R;
import androidx.emoji2.viewsintegration.EmojiEditTextHelper;

class AppCompatEmojiEditTextHelper {
  private final EmojiEditTextHelper mEmojiEditTextHelper;
  
  private final EditText mView;
  
  AppCompatEmojiEditTextHelper(EditText paramEditText) {
    this.mView = paramEditText;
    this.mEmojiEditTextHelper = new EmojiEditTextHelper(paramEditText, false);
  }
  
  KeyListener getKeyListener(KeyListener paramKeyListener) {
    return this.mEmojiEditTextHelper.getKeyListener(paramKeyListener);
  }
  
  void initKeyListener() {
    boolean bool = this.mView.isFocusable();
    int i = this.mView.getInputType();
    EditText editText = this.mView;
    editText.setKeyListener(editText.getKeyListener());
    this.mView.setRawInputType(i);
    this.mView.setFocusable(bool);
  }
  
  boolean isEnabled() {
    return this.mEmojiEditTextHelper.isEnabled();
  }
  
  void loadFromAttributes(AttributeSet paramAttributeSet, int paramInt) {
    TypedArray typedArray = this.mView.getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.AppCompatTextView, paramInt, 0);
    boolean bool = true;
    try {
      if (typedArray.hasValue(R.styleable.AppCompatTextView_emojiCompatEnabled))
        bool = typedArray.getBoolean(R.styleable.AppCompatTextView_emojiCompatEnabled, true); 
      typedArray.recycle();
      return;
    } finally {
      typedArray.recycle();
    } 
  }
  
  InputConnection onCreateInputConnection(InputConnection paramInputConnection, EditorInfo paramEditorInfo) {
    return this.mEmojiEditTextHelper.onCreateInputConnection(paramInputConnection, paramEditorInfo);
  }
  
  void setEnabled(boolean paramBoolean) {
    this.mEmojiEditTextHelper.setEnabled(paramBoolean);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\AppCompatEmojiEditTextHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */