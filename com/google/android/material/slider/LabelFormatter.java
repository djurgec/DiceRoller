package com.google.android.material.slider;

public interface LabelFormatter {
  public static final int LABEL_FLOATING = 0;
  
  public static final int LABEL_GONE = 2;
  
  public static final int LABEL_WITHIN_BOUNDS = 1;
  
  String getFormattedValue(float paramFloat);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\slider\LabelFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */