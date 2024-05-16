package com.google.android.material.slider;

import java.util.Locale;

public final class BasicLabelFormatter implements LabelFormatter {
  private static final int BILLION = 1000000000;
  
  private static final int MILLION = 1000000;
  
  private static final int THOUSAND = 1000;
  
  private static final long TRILLION = 1000000000000L;
  
  public String getFormattedValue(float paramFloat) {
    return (paramFloat >= 1.0E12F) ? String.format(Locale.US, "%.1fT", new Object[] { Float.valueOf(paramFloat / 1.0E12F) }) : ((paramFloat >= 1.0E9F) ? String.format(Locale.US, "%.1fB", new Object[] { Float.valueOf(paramFloat / 1.0E9F) }) : ((paramFloat >= 1000000.0F) ? String.format(Locale.US, "%.1fM", new Object[] { Float.valueOf(paramFloat / 1000000.0F) }) : ((paramFloat >= 1000.0F) ? String.format(Locale.US, "%.1fK", new Object[] { Float.valueOf(paramFloat / 1000.0F) }) : String.format(Locale.US, "%.0f", new Object[] { Float.valueOf(paramFloat) }))));
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\slider\BasicLabelFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */