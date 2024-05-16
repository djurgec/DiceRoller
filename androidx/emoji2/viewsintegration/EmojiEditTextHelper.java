package androidx.emoji2.viewsintegration;

import android.os.Build;
import android.text.method.KeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import android.widget.TextView;
import androidx.core.util.Preconditions;

public final class EmojiEditTextHelper {
  private int mEmojiReplaceStrategy = 0;
  
  private final HelperInternal mHelper;
  
  private int mMaxEmojiCount = Integer.MAX_VALUE;
  
  public EmojiEditTextHelper(EditText paramEditText) {
    this(paramEditText, true);
  }
  
  public EmojiEditTextHelper(EditText paramEditText, boolean paramBoolean) {
    Preconditions.checkNotNull(paramEditText, "editText cannot be null");
    if (Build.VERSION.SDK_INT < 19) {
      this.mHelper = new HelperInternal();
    } else {
      this.mHelper = new HelperInternal19(paramEditText, paramBoolean);
    } 
  }
  
  public int getEmojiReplaceStrategy() {
    return this.mEmojiReplaceStrategy;
  }
  
  public KeyListener getKeyListener(KeyListener paramKeyListener) {
    return this.mHelper.getKeyListener(paramKeyListener);
  }
  
  public int getMaxEmojiCount() {
    return this.mMaxEmojiCount;
  }
  
  public boolean isEnabled() {
    return this.mHelper.isEnabled();
  }
  
  public InputConnection onCreateInputConnection(InputConnection paramInputConnection, EditorInfo paramEditorInfo) {
    return (paramInputConnection == null) ? null : this.mHelper.onCreateInputConnection(paramInputConnection, paramEditorInfo);
  }
  
  public void setEmojiReplaceStrategy(int paramInt) {
    this.mEmojiReplaceStrategy = paramInt;
    this.mHelper.setEmojiReplaceStrategy(paramInt);
  }
  
  public void setEnabled(boolean paramBoolean) {
    this.mHelper.setEnabled(paramBoolean);
  }
  
  public void setMaxEmojiCount(int paramInt) {
    Preconditions.checkArgumentNonnegative(paramInt, "maxEmojiCount should be greater than 0");
    this.mMaxEmojiCount = paramInt;
    this.mHelper.setMaxEmojiCount(paramInt);
  }
  
  static class HelperInternal {
    KeyListener getKeyListener(KeyListener param1KeyListener) {
      return param1KeyListener;
    }
    
    boolean isEnabled() {
      return false;
    }
    
    InputConnection onCreateInputConnection(InputConnection param1InputConnection, EditorInfo param1EditorInfo) {
      return param1InputConnection;
    }
    
    void setEmojiReplaceStrategy(int param1Int) {}
    
    void setEnabled(boolean param1Boolean) {}
    
    void setMaxEmojiCount(int param1Int) {}
  }
  
  private static class HelperInternal19 extends HelperInternal {
    private final EditText mEditText;
    
    private final EmojiTextWatcher mTextWatcher;
    
    HelperInternal19(EditText param1EditText, boolean param1Boolean) {
      this.mEditText = param1EditText;
      EmojiTextWatcher emojiTextWatcher = new EmojiTextWatcher(param1EditText, param1Boolean);
      this.mTextWatcher = emojiTextWatcher;
      param1EditText.addTextChangedListener(emojiTextWatcher);
      param1EditText.setEditableFactory(EmojiEditableFactory.getInstance());
    }
    
    KeyListener getKeyListener(KeyListener param1KeyListener) {
      return (param1KeyListener instanceof EmojiKeyListener) ? param1KeyListener : ((param1KeyListener == null) ? null : new EmojiKeyListener(param1KeyListener));
    }
    
    boolean isEnabled() {
      return this.mTextWatcher.isEnabled();
    }
    
    InputConnection onCreateInputConnection(InputConnection param1InputConnection, EditorInfo param1EditorInfo) {
      return (InputConnection)((param1InputConnection instanceof EmojiInputConnection) ? param1InputConnection : new EmojiInputConnection((TextView)this.mEditText, param1InputConnection, param1EditorInfo));
    }
    
    void setEmojiReplaceStrategy(int param1Int) {
      this.mTextWatcher.setEmojiReplaceStrategy(param1Int);
    }
    
    void setEnabled(boolean param1Boolean) {
      this.mTextWatcher.setEnabled(param1Boolean);
    }
    
    void setMaxEmojiCount(int param1Int) {
      this.mTextWatcher.setMaxEmojiCount(param1Int);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\viewsintegration\EmojiEditTextHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */