package androidx.appcompat.widget;

import android.view.textclassifier.TextClassificationManager;
import android.view.textclassifier.TextClassifier;
import android.widget.TextView;
import androidx.core.util.Preconditions;

final class AppCompatTextClassifierHelper {
  private TextClassifier mTextClassifier;
  
  private TextView mTextView;
  
  AppCompatTextClassifierHelper(TextView paramTextView) {
    this.mTextView = (TextView)Preconditions.checkNotNull(paramTextView);
  }
  
  public TextClassifier getTextClassifier() {
    TextClassificationManager textClassificationManager;
    TextClassifier textClassifier = this.mTextClassifier;
    if (textClassifier == null) {
      textClassificationManager = (TextClassificationManager)this.mTextView.getContext().getSystemService(TextClassificationManager.class);
      return (textClassificationManager != null) ? textClassificationManager.getTextClassifier() : TextClassifier.NO_OP;
    } 
    return (TextClassifier)textClassificationManager;
  }
  
  public void setTextClassifier(TextClassifier paramTextClassifier) {
    this.mTextClassifier = paramTextClassifier;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\AppCompatTextClassifierHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */