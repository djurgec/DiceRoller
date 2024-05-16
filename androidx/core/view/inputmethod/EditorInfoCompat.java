package androidx.core.view.inputmethod;

import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import androidx.core.util.Preconditions;

public final class EditorInfoCompat {
  private static final String CONTENT_MIME_TYPES_INTEROP_KEY = "android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES";
  
  private static final String CONTENT_MIME_TYPES_KEY = "androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES";
  
  private static final String CONTENT_SELECTION_END_KEY = "androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_SELECTION_END";
  
  private static final String CONTENT_SELECTION_HEAD_KEY = "androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_SELECTION_HEAD";
  
  private static final String CONTENT_SURROUNDING_TEXT_KEY = "androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_SURROUNDING_TEXT";
  
  private static final String[] EMPTY_STRING_ARRAY = new String[0];
  
  public static final int IME_FLAG_FORCE_ASCII = -2147483648;
  
  public static final int IME_FLAG_NO_PERSONALIZED_LEARNING = 16777216;
  
  static final int MAX_INITIAL_SELECTION_LENGTH = 1024;
  
  static final int MEMORY_EFFICIENT_TEXT_LENGTH = 2048;
  
  public static String[] getContentMimeTypes(EditorInfo paramEditorInfo) {
    String[] arrayOfString1;
    if (Build.VERSION.SDK_INT >= 25) {
      arrayOfString1 = paramEditorInfo.contentMimeTypes;
      if (arrayOfString1 == null)
        arrayOfString1 = EMPTY_STRING_ARRAY; 
      return arrayOfString1;
    } 
    if (((EditorInfo)arrayOfString1).extras == null)
      return EMPTY_STRING_ARRAY; 
    String[] arrayOfString3 = ((EditorInfo)arrayOfString1).extras.getStringArray("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES");
    String[] arrayOfString2 = arrayOfString3;
    if (arrayOfString3 == null)
      arrayOfString2 = ((EditorInfo)arrayOfString1).extras.getStringArray("android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES"); 
    if (arrayOfString2 == null)
      arrayOfString2 = EMPTY_STRING_ARRAY; 
    return arrayOfString2;
  }
  
  public static CharSequence getInitialSelectedText(EditorInfo paramEditorInfo, int paramInt) {
    if (Build.VERSION.SDK_INT >= 30)
      return Api30Impl.getInitialSelectedText(paramEditorInfo, paramInt); 
    if (paramEditorInfo.extras == null)
      return null; 
    int k = Math.min(paramEditorInfo.initialSelStart, paramEditorInfo.initialSelEnd);
    int i = Math.max(paramEditorInfo.initialSelStart, paramEditorInfo.initialSelEnd);
    int j = paramEditorInfo.extras.getInt("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_SELECTION_HEAD");
    int m = paramEditorInfo.extras.getInt("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_SELECTION_END");
    if (paramEditorInfo.initialSelStart < 0 || paramEditorInfo.initialSelEnd < 0 || m - j != i - k)
      return null; 
    CharSequence charSequence = paramEditorInfo.extras.getCharSequence("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_SURROUNDING_TEXT");
    if (charSequence == null)
      return null; 
    if ((paramInt & 0x1) != 0) {
      charSequence = charSequence.subSequence(j, m);
    } else {
      charSequence = TextUtils.substring(charSequence, j, m);
    } 
    return charSequence;
  }
  
  public static CharSequence getInitialTextAfterCursor(EditorInfo paramEditorInfo, int paramInt1, int paramInt2) {
    CharSequence charSequence1;
    if (Build.VERSION.SDK_INT >= 30)
      return Api30Impl.getInitialTextAfterCursor(paramEditorInfo, paramInt1, paramInt2); 
    if (paramEditorInfo.extras == null)
      return null; 
    CharSequence charSequence2 = paramEditorInfo.extras.getCharSequence("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_SURROUNDING_TEXT");
    if (charSequence2 == null)
      return null; 
    int i = paramEditorInfo.extras.getInt("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_SELECTION_END");
    paramInt1 = Math.min(paramInt1, charSequence2.length() - i);
    if ((paramInt2 & 0x1) != 0) {
      charSequence1 = charSequence2.subSequence(i, i + paramInt1);
    } else {
      charSequence1 = TextUtils.substring(charSequence2, i, i + paramInt1);
    } 
    return charSequence1;
  }
  
  public static CharSequence getInitialTextBeforeCursor(EditorInfo paramEditorInfo, int paramInt1, int paramInt2) {
    CharSequence charSequence1;
    if (Build.VERSION.SDK_INT >= 30)
      return Api30Impl.getInitialTextBeforeCursor(paramEditorInfo, paramInt1, paramInt2); 
    if (paramEditorInfo.extras == null)
      return null; 
    CharSequence charSequence2 = paramEditorInfo.extras.getCharSequence("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_SURROUNDING_TEXT");
    if (charSequence2 == null)
      return null; 
    int i = paramEditorInfo.extras.getInt("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_SELECTION_HEAD");
    paramInt1 = Math.min(paramInt1, i);
    if ((paramInt2 & 0x1) != 0) {
      charSequence1 = charSequence2.subSequence(i - paramInt1, i);
    } else {
      charSequence1 = TextUtils.substring(charSequence2, i - paramInt1, i);
    } 
    return charSequence1;
  }
  
  static int getProtocol(EditorInfo paramEditorInfo) {
    if (Build.VERSION.SDK_INT >= 25)
      return 1; 
    if (paramEditorInfo.extras == null)
      return 0; 
    boolean bool1 = paramEditorInfo.extras.containsKey("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES");
    boolean bool2 = paramEditorInfo.extras.containsKey("android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES");
    return (bool1 && bool2) ? 4 : (bool1 ? 3 : (bool2 ? 2 : 0));
  }
  
  private static boolean isCutOnSurrogate(CharSequence paramCharSequence, int paramInt1, int paramInt2) {
    switch (paramInt2) {
      default:
        return false;
      case 1:
        return Character.isHighSurrogate(paramCharSequence.charAt(paramInt1));
      case 0:
        break;
    } 
    return Character.isLowSurrogate(paramCharSequence.charAt(paramInt1));
  }
  
  private static boolean isPasswordInputType(int paramInt) {
    paramInt &= 0xFFF;
    return (paramInt == 129 || paramInt == 225 || paramInt == 18);
  }
  
  public static void setContentMimeTypes(EditorInfo paramEditorInfo, String[] paramArrayOfString) {
    if (Build.VERSION.SDK_INT >= 25) {
      paramEditorInfo.contentMimeTypes = paramArrayOfString;
    } else {
      if (paramEditorInfo.extras == null)
        paramEditorInfo.extras = new Bundle(); 
      paramEditorInfo.extras.putStringArray("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES", paramArrayOfString);
      paramEditorInfo.extras.putStringArray("android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES", paramArrayOfString);
    } 
  }
  
  public static void setInitialSurroundingSubText(EditorInfo paramEditorInfo, CharSequence paramCharSequence, int paramInt) {
    int i;
    int j;
    Preconditions.checkNotNull(paramCharSequence);
    if (Build.VERSION.SDK_INT >= 30) {
      Api30Impl.setInitialSurroundingSubText(paramEditorInfo, paramCharSequence, paramInt);
      return;
    } 
    if (paramEditorInfo.initialSelStart > paramEditorInfo.initialSelEnd) {
      i = paramEditorInfo.initialSelEnd - paramInt;
    } else {
      i = paramEditorInfo.initialSelStart - paramInt;
    } 
    if (paramEditorInfo.initialSelStart > paramEditorInfo.initialSelEnd) {
      j = paramEditorInfo.initialSelStart - paramInt;
    } else {
      j = paramEditorInfo.initialSelEnd - paramInt;
    } 
    int k = paramCharSequence.length();
    if (paramInt < 0 || i < 0 || j > k) {
      setSurroundingText(paramEditorInfo, null, 0, 0);
      return;
    } 
    if (isPasswordInputType(paramEditorInfo.inputType)) {
      setSurroundingText(paramEditorInfo, null, 0, 0);
      return;
    } 
    if (k <= 2048) {
      setSurroundingText(paramEditorInfo, paramCharSequence, i, j);
      return;
    } 
    trimLongSurroundingText(paramEditorInfo, paramCharSequence, i, j);
  }
  
  public static void setInitialSurroundingText(EditorInfo paramEditorInfo, CharSequence paramCharSequence) {
    if (Build.VERSION.SDK_INT >= 30) {
      Api30Impl.setInitialSurroundingSubText(paramEditorInfo, paramCharSequence, 0);
    } else {
      setInitialSurroundingSubText(paramEditorInfo, paramCharSequence, 0);
    } 
  }
  
  private static void setSurroundingText(EditorInfo paramEditorInfo, CharSequence paramCharSequence, int paramInt1, int paramInt2) {
    if (paramEditorInfo.extras == null)
      paramEditorInfo.extras = new Bundle(); 
    if (paramCharSequence != null) {
      SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(paramCharSequence);
    } else {
      paramCharSequence = null;
    } 
    paramEditorInfo.extras.putCharSequence("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_SURROUNDING_TEXT", paramCharSequence);
    paramEditorInfo.extras.putInt("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_SELECTION_HEAD", paramInt1);
    paramEditorInfo.extras.putInt("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_SELECTION_END", paramInt2);
  }
  
  private static void trimLongSurroundingText(EditorInfo paramEditorInfo, CharSequence paramCharSequence, int paramInt1, int paramInt2) {
    int i;
    int m = paramInt2 - paramInt1;
    if (m > 1024) {
      i = 0;
    } else {
      i = m;
    } 
    int k = paramCharSequence.length();
    int j = 2048 - i;
    int n = Math.min(k - paramInt2, j - Math.min(paramInt1, (int)(j * 0.8D)));
    int i1 = Math.min(paramInt1, j - n);
    int i2 = paramInt1 - i1;
    k = i1;
    j = i2;
    if (isCutOnSurrogate(paramCharSequence, paramInt1 - i1, 0)) {
      j = i2 + 1;
      k = i1 - 1;
    } 
    paramInt1 = n;
    if (isCutOnSurrogate(paramCharSequence, paramInt2 + n - 1, 1))
      paramInt1 = n - 1; 
    if (i != m) {
      paramCharSequence = TextUtils.concat(new CharSequence[] { paramCharSequence.subSequence(j, j + k), paramCharSequence.subSequence(paramInt2, paramInt2 + paramInt1) });
    } else {
      paramCharSequence = paramCharSequence.subSequence(j, j + k + i + paramInt1);
    } 
    paramInt1 = 0 + k;
    setSurroundingText(paramEditorInfo, paramCharSequence, paramInt1, paramInt1 + i);
  }
  
  private static class Api30Impl {
    static CharSequence getInitialSelectedText(EditorInfo param1EditorInfo, int param1Int) {
      return param1EditorInfo.getInitialSelectedText(param1Int);
    }
    
    static CharSequence getInitialTextAfterCursor(EditorInfo param1EditorInfo, int param1Int1, int param1Int2) {
      return param1EditorInfo.getInitialTextAfterCursor(param1Int1, param1Int2);
    }
    
    static CharSequence getInitialTextBeforeCursor(EditorInfo param1EditorInfo, int param1Int1, int param1Int2) {
      return param1EditorInfo.getInitialTextBeforeCursor(param1Int1, param1Int2);
    }
    
    static void setInitialSurroundingSubText(EditorInfo param1EditorInfo, CharSequence param1CharSequence, int param1Int) {
      param1EditorInfo.setInitialSurroundingSubText(param1CharSequence, param1Int);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\view\inputmethod\EditorInfoCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */