package androidx.emoji2.viewsintegration;

import android.text.Editable;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.TextView;
import androidx.emoji2.text.EmojiCompat;

final class EmojiInputConnection extends InputConnectionWrapper {
  private final EmojiCompatDeleteHelper mEmojiCompatDeleteHelper;
  
  private final TextView mTextView;
  
  EmojiInputConnection(TextView paramTextView, InputConnection paramInputConnection, EditorInfo paramEditorInfo) {
    this(paramTextView, paramInputConnection, paramEditorInfo, new EmojiCompatDeleteHelper());
  }
  
  EmojiInputConnection(TextView paramTextView, InputConnection paramInputConnection, EditorInfo paramEditorInfo, EmojiCompatDeleteHelper paramEmojiCompatDeleteHelper) {
    super(paramInputConnection, false);
    this.mTextView = paramTextView;
    this.mEmojiCompatDeleteHelper = paramEmojiCompatDeleteHelper;
    paramEmojiCompatDeleteHelper.updateEditorInfoAttrs(paramEditorInfo);
  }
  
  private Editable getEditable() {
    return this.mTextView.getEditableText();
  }
  
  public boolean deleteSurroundingText(int paramInt1, int paramInt2) {
    return (this.mEmojiCompatDeleteHelper.handleDeleteSurroundingText((InputConnection)this, getEditable(), paramInt1, paramInt2, false) || super.deleteSurroundingText(paramInt1, paramInt2));
  }
  
  public boolean deleteSurroundingTextInCodePoints(int paramInt1, int paramInt2) {
    return (this.mEmojiCompatDeleteHelper.handleDeleteSurroundingText((InputConnection)this, getEditable(), paramInt1, paramInt2, true) || super.deleteSurroundingTextInCodePoints(paramInt1, paramInt2));
  }
  
  public static class EmojiCompatDeleteHelper {
    public boolean handleDeleteSurroundingText(InputConnection param1InputConnection, Editable param1Editable, int param1Int1, int param1Int2, boolean param1Boolean) {
      return EmojiCompat.handleDeleteSurroundingText(param1InputConnection, param1Editable, param1Int1, param1Int2, param1Boolean);
    }
    
    public void updateEditorInfoAttrs(EditorInfo param1EditorInfo) {
      if (EmojiCompat.isConfigured())
        EmojiCompat.get().updateEditorInfo(param1EditorInfo); 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\viewsintegration\EmojiInputConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */