package androidx.emoji2.viewsintegration;

import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.emoji2.text.EmojiCompat;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

final class EmojiTextWatcher implements TextWatcher {
  private final EditText mEditText;
  
  private int mEmojiReplaceStrategy = 0;
  
  private boolean mEnabled;
  
  private final boolean mExpectInitializedEmojiCompat;
  
  private EmojiCompat.InitCallback mInitCallback;
  
  private int mMaxEmojiCount = Integer.MAX_VALUE;
  
  EmojiTextWatcher(EditText paramEditText, boolean paramBoolean) {
    this.mEditText = paramEditText;
    this.mExpectInitializedEmojiCompat = paramBoolean;
    this.mEnabled = true;
  }
  
  private EmojiCompat.InitCallback getInitCallback() {
    if (this.mInitCallback == null)
      this.mInitCallback = new InitCallbackImpl(this.mEditText); 
    return this.mInitCallback;
  }
  
  static void processTextOnEnablingEvent(EditText paramEditText, int paramInt) {
    if (paramInt == 1 && paramEditText != null && paramEditText.isAttachedToWindow()) {
      Editable editable = paramEditText.getEditableText();
      int i = Selection.getSelectionStart((CharSequence)editable);
      paramInt = Selection.getSelectionEnd((CharSequence)editable);
      EmojiCompat.get().process((CharSequence)editable);
      EmojiInputFilter.updateSelection((Spannable)editable, i, paramInt);
    } 
  }
  
  private boolean shouldSkipForDisabledOrNotConfigured() {
    return (!this.mEnabled || (!this.mExpectInitializedEmojiCompat && !EmojiCompat.isConfigured()));
  }
  
  public void afterTextChanged(Editable paramEditable) {}
  
  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
  
  int getEmojiReplaceStrategy() {
    return this.mEmojiReplaceStrategy;
  }
  
  int getMaxEmojiCount() {
    return this.mMaxEmojiCount;
  }
  
  public boolean isEnabled() {
    return this.mEnabled;
  }
  
  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
    if (this.mEditText.isInEditMode() || shouldSkipForDisabledOrNotConfigured())
      return; 
    if (paramInt2 <= paramInt3 && paramCharSequence instanceof Spannable) {
      Spannable spannable;
      switch (EmojiCompat.get().getLoadState()) {
        default:
          return;
        case 1:
          spannable = (Spannable)paramCharSequence;
          EmojiCompat.get().process((CharSequence)spannable, paramInt1, paramInt1 + paramInt3, this.mMaxEmojiCount, this.mEmojiReplaceStrategy);
        case 0:
        case 3:
          break;
      } 
      EmojiCompat.get().registerInitCallback(getInitCallback());
    } 
  }
  
  void setEmojiReplaceStrategy(int paramInt) {
    this.mEmojiReplaceStrategy = paramInt;
  }
  
  public void setEnabled(boolean paramBoolean) {
    if (this.mEnabled != paramBoolean) {
      if (this.mInitCallback != null)
        EmojiCompat.get().unregisterInitCallback(this.mInitCallback); 
      this.mEnabled = paramBoolean;
      if (paramBoolean)
        processTextOnEnablingEvent(this.mEditText, EmojiCompat.get().getLoadState()); 
    } 
  }
  
  void setMaxEmojiCount(int paramInt) {
    this.mMaxEmojiCount = paramInt;
  }
  
  private static class InitCallbackImpl extends EmojiCompat.InitCallback {
    private final Reference<EditText> mViewRef;
    
    InitCallbackImpl(EditText param1EditText) {
      this.mViewRef = new WeakReference<>(param1EditText);
    }
    
    public void onInitialized() {
      super.onInitialized();
      EmojiTextWatcher.processTextOnEnablingEvent(this.mViewRef.get(), 1);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\viewsintegration\EmojiTextWatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */