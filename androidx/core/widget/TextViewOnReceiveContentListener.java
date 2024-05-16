package androidx.core.widget;

import android.content.ClipData;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.core.view.ContentInfoCompat;
import androidx.core.view.OnReceiveContentListener;

public final class TextViewOnReceiveContentListener implements OnReceiveContentListener {
  private static final String LOG_TAG = "ReceiveContent";
  
  private static CharSequence coerceToText(Context paramContext, ClipData.Item paramItem, int paramInt) {
    return (Build.VERSION.SDK_INT >= 16) ? Api16Impl.coerce(paramContext, paramItem, paramInt) : ApiImpl.coerce(paramContext, paramItem, paramInt);
  }
  
  private static void replaceSelection(Editable paramEditable, CharSequence paramCharSequence) {
    int k = Selection.getSelectionStart((CharSequence)paramEditable);
    int j = Selection.getSelectionEnd((CharSequence)paramEditable);
    int i = Math.max(0, Math.min(k, j));
    j = Math.max(0, Math.max(k, j));
    Selection.setSelection((Spannable)paramEditable, j);
    paramEditable.replace(i, j, paramCharSequence);
  }
  
  public ContentInfoCompat onReceiveContent(View paramView, ContentInfoCompat paramContentInfoCompat) {
    if (Log.isLoggable("ReceiveContent", 3))
      Log.d("ReceiveContent", "onReceive: " + paramContentInfoCompat); 
    if (paramContentInfoCompat.getSource() == 2)
      return paramContentInfoCompat; 
    ClipData clipData = paramContentInfoCompat.getClip();
    int i = paramContentInfoCompat.getFlags();
    TextView textView = (TextView)paramView;
    Editable editable = (Editable)textView.getText();
    Context context = textView.getContext();
    boolean bool = false;
    byte b = 0;
    while (b < clipData.getItemCount()) {
      CharSequence charSequence = coerceToText(context, clipData.getItemAt(b), i);
      boolean bool1 = bool;
      if (charSequence != null)
        if (!bool) {
          replaceSelection(editable, charSequence);
          bool1 = true;
        } else {
          editable.insert(Selection.getSelectionEnd((CharSequence)editable), "\n");
          editable.insert(Selection.getSelectionEnd((CharSequence)editable), charSequence);
          bool1 = bool;
        }  
      b++;
      bool = bool1;
    } 
    return null;
  }
  
  private static final class Api16Impl {
    static CharSequence coerce(Context param1Context, ClipData.Item param1Item, int param1Int) {
      CharSequence charSequence;
      if ((param1Int & 0x1) != 0) {
        charSequence = param1Item.coerceToText(param1Context);
        if (charSequence instanceof android.text.Spanned)
          charSequence = charSequence.toString(); 
        return charSequence;
      } 
      return param1Item.coerceToStyledText((Context)charSequence);
    }
  }
  
  private static final class ApiImpl {
    static CharSequence coerce(Context param1Context, ClipData.Item param1Item, int param1Int) {
      CharSequence charSequence2 = param1Item.coerceToText(param1Context);
      CharSequence charSequence1 = charSequence2;
      if ((param1Int & 0x1) != 0) {
        charSequence1 = charSequence2;
        if (charSequence2 instanceof android.text.Spanned)
          charSequence1 = charSequence2.toString(); 
      } 
      return charSequence1;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\widget\TextViewOnReceiveContentListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */