package com.google.android.material.timepicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;
import java.util.Locale;

class TimePickerView extends ConstraintLayout implements TimePickerControls {
  private final ClockFaceView clockFace;
  
  private final ClockHandView clockHandView;
  
  private final Chip hourView;
  
  private final Chip minuteView;
  
  private OnDoubleTapListener onDoubleTapListener;
  
  private OnPeriodChangeListener onPeriodChangeListener;
  
  private OnSelectionChange onSelectionChangeListener;
  
  private final View.OnClickListener selectionListener = new View.OnClickListener() {
      final TimePickerView this$0;
      
      public void onClick(View param1View) {
        if (TimePickerView.this.onSelectionChangeListener != null)
          TimePickerView.this.onSelectionChangeListener.onSelectionChanged(((Integer)param1View.getTag(R.id.selection_type)).intValue()); 
      }
    };
  
  private final MaterialButtonToggleGroup toggle;
  
  public TimePickerView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public TimePickerView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public TimePickerView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    LayoutInflater.from(paramContext).inflate(R.layout.material_timepicker, (ViewGroup)this);
    this.clockFace = (ClockFaceView)findViewById(R.id.material_clock_face);
    MaterialButtonToggleGroup materialButtonToggleGroup = (MaterialButtonToggleGroup)findViewById(R.id.material_clock_period_toggle);
    this.toggle = materialButtonToggleGroup;
    materialButtonToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
          final TimePickerView this$0;
          
          public void onButtonChecked(MaterialButtonToggleGroup param1MaterialButtonToggleGroup, int param1Int, boolean param1Boolean) {
            if (param1Int == R.id.material_clock_period_pm_button) {
              param1Int = 1;
            } else {
              param1Int = 0;
            } 
            if (TimePickerView.this.onPeriodChangeListener != null && param1Boolean)
              TimePickerView.this.onPeriodChangeListener.onPeriodChange(param1Int); 
          }
        });
    this.minuteView = (Chip)findViewById(R.id.material_minute_tv);
    this.hourView = (Chip)findViewById(R.id.material_hour_tv);
    this.clockHandView = (ClockHandView)findViewById(R.id.material_clock_hand);
    setupDoubleTap();
    setUpDisplay();
  }
  
  private void setUpDisplay() {
    this.minuteView.setTag(R.id.selection_type, Integer.valueOf(12));
    this.hourView.setTag(R.id.selection_type, Integer.valueOf(10));
    this.minuteView.setOnClickListener(this.selectionListener);
    this.hourView.setOnClickListener(this.selectionListener);
  }
  
  private void setupDoubleTap() {
    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        final TimePickerView this$0;
        
        final GestureDetector val$gestureDetector;
        
        public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
          return ((Checkable)param1View).isChecked() ? gestureDetector.onTouchEvent(param1MotionEvent) : false;
        }
      };
    this.minuteView.setOnTouchListener(onTouchListener);
    this.hourView.setOnTouchListener(onTouchListener);
  }
  
  private void updateToggleConstraints() {
    if (this.toggle.getVisibility() == 0) {
      ConstraintSet constraintSet = new ConstraintSet();
      constraintSet.clone(this);
      int i = ViewCompat.getLayoutDirection((View)this);
      byte b = 1;
      if (i == 0) {
        i = 1;
      } else {
        i = 0;
      } 
      if (i != 0)
        b = 2; 
      constraintSet.clear(R.id.material_clock_display, b);
      constraintSet.applyTo(this);
    } 
  }
  
  public void addOnRotateListener(ClockHandView.OnRotateListener paramOnRotateListener) {
    this.clockHandView.addOnRotateListener(paramOnRotateListener);
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    updateToggleConstraints();
  }
  
  protected void onVisibilityChanged(View paramView, int paramInt) {
    super.onVisibilityChanged(paramView, paramInt);
    if (paramView == this && paramInt == 0)
      updateToggleConstraints(); 
  }
  
  public void setActiveSelection(int paramInt) {
    boolean bool1;
    Chip chip = this.minuteView;
    boolean bool2 = true;
    if (paramInt == 12) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    chip.setChecked(bool1);
    chip = this.hourView;
    if (paramInt == 10) {
      bool1 = bool2;
    } else {
      bool1 = false;
    } 
    chip.setChecked(bool1);
  }
  
  public void setAnimateOnTouchUp(boolean paramBoolean) {
    this.clockHandView.setAnimateOnTouchUp(paramBoolean);
  }
  
  public void setHandRotation(float paramFloat) {
    this.clockHandView.setHandRotation(paramFloat);
  }
  
  public void setHandRotation(float paramFloat, boolean paramBoolean) {
    this.clockHandView.setHandRotation(paramFloat, paramBoolean);
  }
  
  public void setHourClickDelegate(AccessibilityDelegateCompat paramAccessibilityDelegateCompat) {
    ViewCompat.setAccessibilityDelegate((View)this.minuteView, paramAccessibilityDelegateCompat);
  }
  
  public void setMinuteHourDelegate(AccessibilityDelegateCompat paramAccessibilityDelegateCompat) {
    ViewCompat.setAccessibilityDelegate((View)this.hourView, paramAccessibilityDelegateCompat);
  }
  
  public void setOnActionUpListener(ClockHandView.OnActionUpListener paramOnActionUpListener) {
    this.clockHandView.setOnActionUpListener(paramOnActionUpListener);
  }
  
  void setOnDoubleTapListener(OnDoubleTapListener paramOnDoubleTapListener) {
    this.onDoubleTapListener = paramOnDoubleTapListener;
  }
  
  void setOnPeriodChangeListener(OnPeriodChangeListener paramOnPeriodChangeListener) {
    this.onPeriodChangeListener = paramOnPeriodChangeListener;
  }
  
  void setOnSelectionChangeListener(OnSelectionChange paramOnSelectionChange) {
    this.onSelectionChangeListener = paramOnSelectionChange;
  }
  
  public void setValues(String[] paramArrayOfString, int paramInt) {
    this.clockFace.setValues(paramArrayOfString, paramInt);
  }
  
  public void showToggle() {
    this.toggle.setVisibility(0);
  }
  
  public void updateTime(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt1 == 1) {
      paramInt1 = R.id.material_clock_period_pm_button;
    } else {
      paramInt1 = R.id.material_clock_period_am_button;
    } 
    this.toggle.check(paramInt1);
    Locale locale = (getResources().getConfiguration()).locale;
    String str1 = String.format(locale, "%02d", new Object[] { Integer.valueOf(paramInt3) });
    String str2 = String.format(locale, "%02d", new Object[] { Integer.valueOf(paramInt2) });
    this.minuteView.setText(str1);
    this.hourView.setText(str2);
  }
  
  static interface OnDoubleTapListener {
    void onDoubleTap();
  }
  
  static interface OnPeriodChangeListener {
    void onPeriodChange(int param1Int);
  }
  
  static interface OnSelectionChange {
    void onSelectionChanged(int param1Int);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\timepicker\TimePickerView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */