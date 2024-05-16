package com.google.android.material.timepicker;

import android.text.InputFilter;
import android.text.Spanned;

class MaxInputValidator implements InputFilter {
  private int max;
  
  public MaxInputValidator(int paramInt) {
    this.max = paramInt;
  }
  
  public CharSequence filter(CharSequence paramCharSequence, int paramInt1, int paramInt2, Spanned paramSpanned, int paramInt3, int paramInt4) {
    try {
      StringBuilder stringBuilder = new StringBuilder();
      this((CharSequence)paramSpanned);
      stringBuilder.replace(paramInt3, paramInt4, paramCharSequence.subSequence(paramInt1, paramInt2).toString());
      paramInt1 = Integer.parseInt(stringBuilder.toString());
      paramInt2 = this.max;
      if (paramInt1 <= paramInt2)
        return null; 
    } catch (NumberFormatException numberFormatException) {}
    return "";
  }
  
  public int getMax() {
    return this.max;
  }
  
  public void setMax(int paramInt) {
    this.max = paramInt;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\timepicker\MaxInputValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */