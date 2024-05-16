package androidx.emoji2.viewsintegration;

import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.widget.TextView;
import androidx.emoji2.text.EmojiCompat;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

final class EmojiInputFilter implements InputFilter {
  private EmojiCompat.InitCallback mInitCallback;
  
  private final TextView mTextView;
  
  EmojiInputFilter(TextView paramTextView) {
    this.mTextView = paramTextView;
  }
  
  private EmojiCompat.InitCallback getInitCallback() {
    if (this.mInitCallback == null)
      this.mInitCallback = new InitCallbackImpl(this.mTextView, this); 
    return this.mInitCallback;
  }
  
  static void updateSelection(Spannable paramSpannable, int paramInt1, int paramInt2) {
    if (paramInt1 >= 0 && paramInt2 >= 0) {
      Selection.setSelection(paramSpannable, paramInt1, paramInt2);
    } else if (paramInt1 >= 0) {
      Selection.setSelection(paramSpannable, paramInt1);
    } else if (paramInt2 >= 0) {
      Selection.setSelection(paramSpannable, paramInt2);
    } 
  }
  
  public CharSequence filter(CharSequence paramCharSequence, int paramInt1, int paramInt2, Spanned paramSpanned, int paramInt3, int paramInt4) {
    boolean bool1;
    boolean bool2;
    if (this.mTextView.isInEditMode())
      return paramCharSequence; 
    switch (EmojiCompat.get().getLoadState()) {
      default:
        return paramCharSequence;
      case 1:
        bool2 = true;
        bool1 = bool2;
        if (paramInt4 == 0) {
          bool1 = bool2;
          if (paramInt3 == 0) {
            bool1 = bool2;
            if (paramSpanned.length() == 0) {
              bool1 = bool2;
              if (paramCharSequence == this.mTextView.getText())
                bool1 = false; 
            } 
          } 
        } 
        if (bool1 && paramCharSequence != null) {
          if (paramInt1 != 0 || paramInt2 != paramCharSequence.length())
            paramCharSequence = paramCharSequence.subSequence(paramInt1, paramInt2); 
          return EmojiCompat.get().process(paramCharSequence, 0, paramCharSequence.length());
        } 
        return paramCharSequence;
      case 0:
      case 3:
        break;
    } 
    EmojiCompat.get().registerInitCallback(getInitCallback());
    return paramCharSequence;
  }
  
  private static class InitCallbackImpl extends EmojiCompat.InitCallback {
    private final Reference<EmojiInputFilter> mEmojiInputFilterReference;
    
    private final Reference<TextView> mViewRef;
    
    InitCallbackImpl(TextView param1TextView, EmojiInputFilter param1EmojiInputFilter) {
      this.mViewRef = new WeakReference<>(param1TextView);
      this.mEmojiInputFilterReference = new WeakReference<>(param1EmojiInputFilter);
    }
    
    private boolean isInputFilterCurrentlyRegisteredOnTextView(TextView param1TextView, InputFilter param1InputFilter) {
      if (param1InputFilter == null || param1TextView == null)
        return false; 
      InputFilter[] arrayOfInputFilter = param1TextView.getFilters();
      if (arrayOfInputFilter == null)
        return false; 
      for (byte b = 0; b < arrayOfInputFilter.length; b++) {
        if (arrayOfInputFilter[b] == param1InputFilter)
          return true; 
      } 
      return false;
    }
    
    public void onInitialized() {
      super.onInitialized();
      TextView textView = this.mViewRef.get();
      if (!isInputFilterCurrentlyRegisteredOnTextView(textView, this.mEmojiInputFilterReference.get()))
        return; 
      if (textView.isAttachedToWindow()) {
        CharSequence charSequence = EmojiCompat.get().process(textView.getText());
        int i = Selection.getSelectionStart(charSequence);
        int j = Selection.getSelectionEnd(charSequence);
        textView.setText(charSequence);
        if (charSequence instanceof Spannable)
          EmojiInputFilter.updateSelection((Spannable)charSequence, i, j); 
      } 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\viewsintegration\EmojiInputFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */