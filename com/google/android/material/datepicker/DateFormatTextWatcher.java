package com.google.android.material.datepicker;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.google.android.material.R;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.textfield.TextInputLayout;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

abstract class DateFormatTextWatcher extends TextWatcherAdapter {
  private static final int VALIDATION_DELAY = 1000;
  
  private final CalendarConstraints constraints;
  
  private final DateFormat dateFormat;
  
  private final String outOfRange;
  
  private final Runnable setErrorCallback;
  
  private Runnable setRangeErrorCallback;
  
  private final TextInputLayout textInputLayout;
  
  DateFormatTextWatcher(final String formatHint, DateFormat paramDateFormat, TextInputLayout paramTextInputLayout, CalendarConstraints paramCalendarConstraints) {
    this.dateFormat = paramDateFormat;
    this.textInputLayout = paramTextInputLayout;
    this.constraints = paramCalendarConstraints;
    this.outOfRange = paramTextInputLayout.getContext().getString(R.string.mtrl_picker_out_of_range);
    this.setErrorCallback = new Runnable() {
        final DateFormatTextWatcher this$0;
        
        final String val$formatHint;
        
        public void run() {
          TextInputLayout textInputLayout = DateFormatTextWatcher.this.textInputLayout;
          DateFormat dateFormat = DateFormatTextWatcher.this.dateFormat;
          Context context = textInputLayout.getContext();
          String str1 = context.getString(R.string.mtrl_picker_invalid_format);
          String str2 = String.format(context.getString(R.string.mtrl_picker_invalid_format_use), new Object[] { this.val$formatHint });
          String str3 = String.format(context.getString(R.string.mtrl_picker_invalid_format_example), new Object[] { dateFormat.format(new Date(UtcDates.getTodayCalendar().getTimeInMillis())) });
          textInputLayout.setError(str1 + "\n" + str2 + "\n" + str3);
          DateFormatTextWatcher.this.onInvalidDate();
        }
      };
  }
  
  private Runnable createRangeErrorCallback(final long milliseconds) {
    return new Runnable() {
        final DateFormatTextWatcher this$0;
        
        final long val$milliseconds;
        
        public void run() {
          DateFormatTextWatcher.this.textInputLayout.setError(String.format(DateFormatTextWatcher.this.outOfRange, new Object[] { DateStrings.getDateString(this.val$milliseconds) }));
          DateFormatTextWatcher.this.onInvalidDate();
        }
      };
  }
  
  void onInvalidDate() {}
  
  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
    this.textInputLayout.removeCallbacks(this.setErrorCallback);
    this.textInputLayout.removeCallbacks(this.setRangeErrorCallback);
    this.textInputLayout.setError(null);
    onValidDate(null);
    if (TextUtils.isEmpty(paramCharSequence))
      return; 
    try {
      Date date = this.dateFormat.parse(paramCharSequence.toString());
      this.textInputLayout.setError(null);
      long l = date.getTime();
      if (this.constraints.getDateValidator().isValid(l) && this.constraints.isWithinBounds(l)) {
        onValidDate(Long.valueOf(date.getTime()));
        return;
      } 
      Runnable runnable = createRangeErrorCallback(l);
      this.setRangeErrorCallback = runnable;
      runValidation((View)this.textInputLayout, runnable);
    } catch (ParseException parseException) {
      runValidation((View)this.textInputLayout, this.setErrorCallback);
    } 
  }
  
  abstract void onValidDate(Long paramLong);
  
  public void runValidation(View paramView, Runnable paramRunnable) {
    paramView.postDelayed(paramRunnable, 1000L);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\datepicker\DateFormatTextWatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */