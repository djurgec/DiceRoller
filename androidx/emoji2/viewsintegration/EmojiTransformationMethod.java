package androidx.emoji2.viewsintegration;

import android.graphics.Rect;
import android.text.method.TransformationMethod;
import android.view.View;
import androidx.emoji2.text.EmojiCompat;

class EmojiTransformationMethod implements TransformationMethod {
  private final TransformationMethod mTransformationMethod;
  
  EmojiTransformationMethod(TransformationMethod paramTransformationMethod) {
    this.mTransformationMethod = paramTransformationMethod;
  }
  
  public TransformationMethod getOriginalTransformationMethod() {
    return this.mTransformationMethod;
  }
  
  public CharSequence getTransformation(CharSequence paramCharSequence, View paramView) {
    if (paramView.isInEditMode())
      return paramCharSequence; 
    TransformationMethod transformationMethod = this.mTransformationMethod;
    CharSequence charSequence = paramCharSequence;
    if (transformationMethod != null)
      charSequence = transformationMethod.getTransformation(paramCharSequence, paramView); 
    if (charSequence != null) {
      switch (EmojiCompat.get().getLoadState()) {
        default:
          return charSequence;
        case 1:
          break;
      } 
      return EmojiCompat.get().process(charSequence);
    } 
  }
  
  public void onFocusChanged(View paramView, CharSequence paramCharSequence, boolean paramBoolean, int paramInt, Rect paramRect) {
    TransformationMethod transformationMethod = this.mTransformationMethod;
    if (transformationMethod != null)
      transformationMethod.onFocusChanged(paramView, paramCharSequence, paramBoolean, paramInt, paramRect); 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\viewsintegration\EmojiTransformationMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */