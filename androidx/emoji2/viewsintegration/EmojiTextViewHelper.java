package androidx.emoji2.viewsintegration;

import android.os.Build;
import android.text.InputFilter;
import android.text.method.TransformationMethod;
import android.util.SparseArray;
import android.widget.TextView;
import androidx.core.util.Preconditions;
import androidx.emoji2.text.EmojiCompat;

public final class EmojiTextViewHelper {
  private final HelperInternal mHelper;
  
  public EmojiTextViewHelper(TextView paramTextView) {
    this(paramTextView, true);
  }
  
  public EmojiTextViewHelper(TextView paramTextView, boolean paramBoolean) {
    Preconditions.checkNotNull(paramTextView, "textView cannot be null");
    if (Build.VERSION.SDK_INT < 19) {
      this.mHelper = new HelperInternal();
    } else if (!paramBoolean) {
      this.mHelper = new SkippingHelper19(paramTextView);
    } else {
      this.mHelper = new HelperInternal19(paramTextView);
    } 
  }
  
  public InputFilter[] getFilters(InputFilter[] paramArrayOfInputFilter) {
    return this.mHelper.getFilters(paramArrayOfInputFilter);
  }
  
  public boolean isEnabled() {
    return this.mHelper.isEnabled();
  }
  
  public void setAllCaps(boolean paramBoolean) {
    this.mHelper.setAllCaps(paramBoolean);
  }
  
  public void setEnabled(boolean paramBoolean) {
    this.mHelper.setEnabled(paramBoolean);
  }
  
  public void updateTransformationMethod() {
    this.mHelper.updateTransformationMethod();
  }
  
  public TransformationMethod wrapTransformationMethod(TransformationMethod paramTransformationMethod) {
    return this.mHelper.wrapTransformationMethod(paramTransformationMethod);
  }
  
  static class HelperInternal {
    InputFilter[] getFilters(InputFilter[] param1ArrayOfInputFilter) {
      return param1ArrayOfInputFilter;
    }
    
    public boolean isEnabled() {
      return false;
    }
    
    void setAllCaps(boolean param1Boolean) {}
    
    void setEnabled(boolean param1Boolean) {}
    
    void updateTransformationMethod() {}
    
    TransformationMethod wrapTransformationMethod(TransformationMethod param1TransformationMethod) {
      return param1TransformationMethod;
    }
  }
  
  private static class HelperInternal19 extends HelperInternal {
    private final EmojiInputFilter mEmojiInputFilter;
    
    private boolean mEnabled;
    
    private final TextView mTextView;
    
    HelperInternal19(TextView param1TextView) {
      this.mTextView = param1TextView;
      this.mEnabled = true;
      this.mEmojiInputFilter = new EmojiInputFilter(param1TextView);
    }
    
    private InputFilter[] addEmojiInputFilterIfMissing(InputFilter[] param1ArrayOfInputFilter) {
      int i = param1ArrayOfInputFilter.length;
      for (byte b = 0; b < i; b++) {
        if (param1ArrayOfInputFilter[b] == this.mEmojiInputFilter)
          return param1ArrayOfInputFilter; 
      } 
      InputFilter[] arrayOfInputFilter = new InputFilter[param1ArrayOfInputFilter.length + 1];
      System.arraycopy(param1ArrayOfInputFilter, 0, arrayOfInputFilter, 0, i);
      arrayOfInputFilter[i] = this.mEmojiInputFilter;
      return arrayOfInputFilter;
    }
    
    private SparseArray<InputFilter> getEmojiInputFilterPositionArray(InputFilter[] param1ArrayOfInputFilter) {
      SparseArray<InputFilter> sparseArray = new SparseArray(1);
      for (byte b = 0; b < param1ArrayOfInputFilter.length; b++) {
        if (param1ArrayOfInputFilter[b] instanceof EmojiInputFilter)
          sparseArray.put(b, param1ArrayOfInputFilter[b]); 
      } 
      return sparseArray;
    }
    
    private InputFilter[] removeEmojiInputFilterIfPresent(InputFilter[] param1ArrayOfInputFilter) {
      SparseArray<InputFilter> sparseArray = getEmojiInputFilterPositionArray(param1ArrayOfInputFilter);
      if (sparseArray.size() == 0)
        return param1ArrayOfInputFilter; 
      int j = param1ArrayOfInputFilter.length;
      InputFilter[] arrayOfInputFilter = new InputFilter[param1ArrayOfInputFilter.length - sparseArray.size()];
      int i = 0;
      byte b = 0;
      while (b < j) {
        int k = i;
        if (sparseArray.indexOfKey(b) < 0) {
          arrayOfInputFilter[i] = param1ArrayOfInputFilter[b];
          k = i + 1;
        } 
        b++;
        i = k;
      } 
      return arrayOfInputFilter;
    }
    
    private TransformationMethod unwrapForDisabled(TransformationMethod param1TransformationMethod) {
      return (param1TransformationMethod instanceof EmojiTransformationMethod) ? ((EmojiTransformationMethod)param1TransformationMethod).getOriginalTransformationMethod() : param1TransformationMethod;
    }
    
    private void updateFilters() {
      InputFilter[] arrayOfInputFilter = this.mTextView.getFilters();
      this.mTextView.setFilters(getFilters(arrayOfInputFilter));
    }
    
    private TransformationMethod wrapForEnabled(TransformationMethod param1TransformationMethod) {
      return (param1TransformationMethod instanceof EmojiTransformationMethod) ? param1TransformationMethod : ((param1TransformationMethod instanceof android.text.method.PasswordTransformationMethod) ? param1TransformationMethod : new EmojiTransformationMethod(param1TransformationMethod));
    }
    
    InputFilter[] getFilters(InputFilter[] param1ArrayOfInputFilter) {
      return !this.mEnabled ? removeEmojiInputFilterIfPresent(param1ArrayOfInputFilter) : addEmojiInputFilterIfMissing(param1ArrayOfInputFilter);
    }
    
    public boolean isEnabled() {
      return this.mEnabled;
    }
    
    void setAllCaps(boolean param1Boolean) {
      if (param1Boolean)
        updateTransformationMethod(); 
    }
    
    void setEnabled(boolean param1Boolean) {
      this.mEnabled = param1Boolean;
      updateTransformationMethod();
      updateFilters();
    }
    
    void setEnabledUnsafe(boolean param1Boolean) {
      this.mEnabled = param1Boolean;
    }
    
    void updateTransformationMethod() {
      TransformationMethod transformationMethod = wrapTransformationMethod(this.mTextView.getTransformationMethod());
      this.mTextView.setTransformationMethod(transformationMethod);
    }
    
    TransformationMethod wrapTransformationMethod(TransformationMethod param1TransformationMethod) {
      return this.mEnabled ? wrapForEnabled(param1TransformationMethod) : unwrapForDisabled(param1TransformationMethod);
    }
  }
  
  private static class SkippingHelper19 extends HelperInternal {
    private final EmojiTextViewHelper.HelperInternal19 mHelperDelegate;
    
    SkippingHelper19(TextView param1TextView) {
      this.mHelperDelegate = new EmojiTextViewHelper.HelperInternal19(param1TextView);
    }
    
    private boolean skipBecauseEmojiCompatNotInitialized() {
      return EmojiCompat.isConfigured() ^ true;
    }
    
    InputFilter[] getFilters(InputFilter[] param1ArrayOfInputFilter) {
      return skipBecauseEmojiCompatNotInitialized() ? param1ArrayOfInputFilter : this.mHelperDelegate.getFilters(param1ArrayOfInputFilter);
    }
    
    public boolean isEnabled() {
      return this.mHelperDelegate.isEnabled();
    }
    
    void setAllCaps(boolean param1Boolean) {
      if (skipBecauseEmojiCompatNotInitialized())
        return; 
      this.mHelperDelegate.setAllCaps(param1Boolean);
    }
    
    void setEnabled(boolean param1Boolean) {
      if (skipBecauseEmojiCompatNotInitialized()) {
        this.mHelperDelegate.setEnabledUnsafe(param1Boolean);
      } else {
        this.mHelperDelegate.setEnabled(param1Boolean);
      } 
    }
    
    void updateTransformationMethod() {
      if (skipBecauseEmojiCompatNotInitialized())
        return; 
      this.mHelperDelegate.updateTransformationMethod();
    }
    
    TransformationMethod wrapTransformationMethod(TransformationMethod param1TransformationMethod) {
      return skipBecauseEmojiCompatNotInitialized() ? param1TransformationMethod : this.mHelperDelegate.wrapTransformationMethod(param1TransformationMethod);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\viewsintegration\EmojiTextViewHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */