package com.google.android.material.timepicker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.shape.MaterialShapeDrawable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public final class MaterialTimePicker extends DialogFragment {
  public static final int INPUT_MODE_CLOCK = 0;
  
  static final String INPUT_MODE_EXTRA = "TIME_PICKER_INPUT_MODE";
  
  public static final int INPUT_MODE_KEYBOARD = 1;
  
  static final String OVERRIDE_THEME_RES_ID = "TIME_PICKER_OVERRIDE_THEME_RES_ID";
  
  static final String TIME_MODEL_EXTRA = "TIME_PICKER_TIME_MODEL";
  
  static final String TITLE_RES_EXTRA = "TIME_PICKER_TITLE_RES";
  
  static final String TITLE_TEXT_EXTRA = "TIME_PICKER_TITLE_TEXT";
  
  private TimePickerPresenter activePresenter;
  
  private final Set<DialogInterface.OnCancelListener> cancelListeners = new LinkedHashSet<>();
  
  private int clockIcon;
  
  private final Set<DialogInterface.OnDismissListener> dismissListeners = new LinkedHashSet<>();
  
  private int inputMode = 0;
  
  private int keyboardIcon;
  
  private MaterialButton modeButton;
  
  private final Set<View.OnClickListener> negativeButtonListeners = new LinkedHashSet<>();
  
  private int overrideThemeResId = 0;
  
  private final Set<View.OnClickListener> positiveButtonListeners = new LinkedHashSet<>();
  
  private ViewStub textInputStub;
  
  private TimeModel time;
  
  private TimePickerClockPresenter timePickerClockPresenter;
  
  private TimePickerTextInputPresenter timePickerTextInputPresenter;
  
  private TimePickerView timePickerView;
  
  private int titleResId = 0;
  
  private String titleText;
  
  private Pair<Integer, Integer> dataForMode(int paramInt) {
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("no icon for mode: " + paramInt);
      case 1:
        return new Pair(Integer.valueOf(this.clockIcon), Integer.valueOf(R.string.material_timepicker_clock_mode_description));
      case 0:
        break;
    } 
    return new Pair(Integer.valueOf(this.keyboardIcon), Integer.valueOf(R.string.material_timepicker_text_input_mode_description));
  }
  
  private int getThemeResId() {
    int i = this.overrideThemeResId;
    if (i != 0)
      return i; 
    TypedValue typedValue = MaterialAttributes.resolve(requireContext(), R.attr.materialTimePickerTheme);
    if (typedValue == null) {
      i = 0;
    } else {
      i = typedValue.data;
    } 
    return i;
  }
  
  private TimePickerPresenter initializeOrRetrieveActivePresenterForMode(int paramInt) {
    if (paramInt == 0) {
      TimePickerClockPresenter timePickerClockPresenter2 = this.timePickerClockPresenter;
      TimePickerClockPresenter timePickerClockPresenter1 = timePickerClockPresenter2;
      if (timePickerClockPresenter2 == null)
        timePickerClockPresenter1 = new TimePickerClockPresenter(this.timePickerView, this.time); 
      this.timePickerClockPresenter = timePickerClockPresenter1;
      return timePickerClockPresenter1;
    } 
    if (this.timePickerTextInputPresenter == null)
      this.timePickerTextInputPresenter = new TimePickerTextInputPresenter((LinearLayout)this.textInputStub.inflate(), this.time); 
    this.timePickerTextInputPresenter.clearCheck();
    return this.timePickerTextInputPresenter;
  }
  
  private static MaterialTimePicker newInstance(Builder paramBuilder) {
    MaterialTimePicker materialTimePicker = new MaterialTimePicker();
    Bundle bundle = new Bundle();
    bundle.putParcelable("TIME_PICKER_TIME_MODEL", paramBuilder.time);
    bundle.putInt("TIME_PICKER_INPUT_MODE", paramBuilder.inputMode);
    bundle.putInt("TIME_PICKER_TITLE_RES", paramBuilder.titleTextResId);
    bundle.putInt("TIME_PICKER_OVERRIDE_THEME_RES_ID", paramBuilder.overrideThemeResId);
    if (paramBuilder.titleText != null)
      bundle.putString("TIME_PICKER_TITLE_TEXT", paramBuilder.titleText.toString()); 
    materialTimePicker.setArguments(bundle);
    return materialTimePicker;
  }
  
  private void restoreState(Bundle paramBundle) {
    if (paramBundle == null)
      return; 
    TimeModel timeModel = (TimeModel)paramBundle.getParcelable("TIME_PICKER_TIME_MODEL");
    this.time = timeModel;
    if (timeModel == null)
      this.time = new TimeModel(); 
    this.inputMode = paramBundle.getInt("TIME_PICKER_INPUT_MODE", 0);
    this.titleResId = paramBundle.getInt("TIME_PICKER_TITLE_RES", 0);
    this.titleText = paramBundle.getString("TIME_PICKER_TITLE_TEXT");
    this.overrideThemeResId = paramBundle.getInt("TIME_PICKER_OVERRIDE_THEME_RES_ID", 0);
  }
  
  private void updateInputMode(MaterialButton paramMaterialButton) {
    TimePickerPresenter timePickerPresenter = this.activePresenter;
    if (timePickerPresenter != null)
      timePickerPresenter.hide(); 
    timePickerPresenter = initializeOrRetrieveActivePresenterForMode(this.inputMode);
    this.activePresenter = timePickerPresenter;
    timePickerPresenter.show();
    this.activePresenter.invalidate();
    Pair<Integer, Integer> pair = dataForMode(this.inputMode);
    paramMaterialButton.setIconResource(((Integer)pair.first).intValue());
    paramMaterialButton.setContentDescription(getResources().getString(((Integer)pair.second).intValue()));
  }
  
  public boolean addOnCancelListener(DialogInterface.OnCancelListener paramOnCancelListener) {
    return this.cancelListeners.add(paramOnCancelListener);
  }
  
  public boolean addOnDismissListener(DialogInterface.OnDismissListener paramOnDismissListener) {
    return this.dismissListeners.add(paramOnDismissListener);
  }
  
  public boolean addOnNegativeButtonClickListener(View.OnClickListener paramOnClickListener) {
    return this.negativeButtonListeners.add(paramOnClickListener);
  }
  
  public boolean addOnPositiveButtonClickListener(View.OnClickListener paramOnClickListener) {
    return this.positiveButtonListeners.add(paramOnClickListener);
  }
  
  public void clearOnCancelListeners() {
    this.cancelListeners.clear();
  }
  
  public void clearOnDismissListeners() {
    this.dismissListeners.clear();
  }
  
  public void clearOnNegativeButtonClickListeners() {
    this.negativeButtonListeners.clear();
  }
  
  public void clearOnPositiveButtonClickListeners() {
    this.positiveButtonListeners.clear();
  }
  
  public int getHour() {
    return this.time.hour % 24;
  }
  
  public int getInputMode() {
    return this.inputMode;
  }
  
  public int getMinute() {
    return this.time.minute;
  }
  
  TimePickerClockPresenter getTimePickerClockPresenter() {
    return this.timePickerClockPresenter;
  }
  
  public final void onCancel(DialogInterface paramDialogInterface) {
    Iterator<DialogInterface.OnCancelListener> iterator = this.cancelListeners.iterator();
    while (iterator.hasNext())
      ((DialogInterface.OnCancelListener)iterator.next()).onCancel(paramDialogInterface); 
    super.onCancel(paramDialogInterface);
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    if (paramBundle == null)
      paramBundle = getArguments(); 
    restoreState(paramBundle);
  }
  
  public final Dialog onCreateDialog(Bundle paramBundle) {
    Dialog dialog = new Dialog(requireContext(), getThemeResId());
    Context context = dialog.getContext();
    int i = MaterialAttributes.resolveOrThrow(context, R.attr.colorSurface, MaterialTimePicker.class.getCanonicalName());
    MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(context, null, R.attr.materialTimePickerStyle, R.style.Widget_MaterialComponents_TimePicker);
    TypedArray typedArray = context.obtainStyledAttributes(null, R.styleable.MaterialTimePicker, R.attr.materialTimePickerStyle, R.style.Widget_MaterialComponents_TimePicker);
    this.clockIcon = typedArray.getResourceId(R.styleable.MaterialTimePicker_clockIcon, 0);
    this.keyboardIcon = typedArray.getResourceId(R.styleable.MaterialTimePicker_keyboardIcon, 0);
    typedArray.recycle();
    materialShapeDrawable.initializeElevationOverlay(context);
    materialShapeDrawable.setFillColor(ColorStateList.valueOf(i));
    Window window = dialog.getWindow();
    window.setBackgroundDrawable((Drawable)materialShapeDrawable);
    window.requestFeature(1);
    window.setLayout(-2, -2);
    return dialog;
  }
  
  public final View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
    ViewGroup viewGroup = (ViewGroup)paramLayoutInflater.inflate(R.layout.material_timepicker_dialog, paramViewGroup);
    TimePickerView timePickerView = (TimePickerView)viewGroup.findViewById(R.id.material_timepicker_view);
    this.timePickerView = timePickerView;
    timePickerView.setOnDoubleTapListener(new TimePickerView.OnDoubleTapListener() {
          final MaterialTimePicker this$0;
          
          public void onDoubleTap() {
            MaterialTimePicker.access$502(MaterialTimePicker.this, 1);
            MaterialTimePicker materialTimePicker = MaterialTimePicker.this;
            materialTimePicker.updateInputMode(materialTimePicker.modeButton);
            MaterialTimePicker.this.timePickerTextInputPresenter.resetChecked();
          }
        });
    this.textInputStub = (ViewStub)viewGroup.findViewById(R.id.material_textinput_timepicker);
    this.modeButton = (MaterialButton)viewGroup.findViewById(R.id.material_timepicker_mode_button);
    TextView textView = (TextView)viewGroup.findViewById(R.id.header_title);
    if (!TextUtils.isEmpty(this.titleText))
      textView.setText(this.titleText); 
    int i = this.titleResId;
    if (i != 0)
      textView.setText(i); 
    updateInputMode(this.modeButton);
    ((Button)viewGroup.findViewById(R.id.material_timepicker_ok_button)).setOnClickListener(new View.OnClickListener() {
          final MaterialTimePicker this$0;
          
          public void onClick(View param1View) {
            Iterator<View.OnClickListener> iterator = MaterialTimePicker.this.positiveButtonListeners.iterator();
            while (iterator.hasNext())
              ((View.OnClickListener)iterator.next()).onClick(param1View); 
            MaterialTimePicker.this.dismiss();
          }
        });
    ((Button)viewGroup.findViewById(R.id.material_timepicker_cancel_button)).setOnClickListener(new View.OnClickListener() {
          final MaterialTimePicker this$0;
          
          public void onClick(View param1View) {
            Iterator<View.OnClickListener> iterator = MaterialTimePicker.this.negativeButtonListeners.iterator();
            while (iterator.hasNext())
              ((View.OnClickListener)iterator.next()).onClick(param1View); 
            MaterialTimePicker.this.dismiss();
          }
        });
    this.modeButton.setOnClickListener(new View.OnClickListener() {
          final MaterialTimePicker this$0;
          
          public void onClick(View param1View) {
            boolean bool;
            MaterialTimePicker materialTimePicker = MaterialTimePicker.this;
            if (materialTimePicker.inputMode == 0) {
              bool = true;
            } else {
              bool = false;
            } 
            MaterialTimePicker.access$502(materialTimePicker, bool);
            materialTimePicker = MaterialTimePicker.this;
            materialTimePicker.updateInputMode(materialTimePicker.modeButton);
          }
        });
    return (View)viewGroup;
  }
  
  public final void onDismiss(DialogInterface paramDialogInterface) {
    Iterator<DialogInterface.OnDismissListener> iterator = this.dismissListeners.iterator();
    while (iterator.hasNext())
      ((DialogInterface.OnDismissListener)iterator.next()).onDismiss(paramDialogInterface); 
    super.onDismiss(paramDialogInterface);
  }
  
  public void onSaveInstanceState(Bundle paramBundle) {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("TIME_PICKER_TIME_MODEL", this.time);
    paramBundle.putInt("TIME_PICKER_INPUT_MODE", this.inputMode);
    paramBundle.putInt("TIME_PICKER_TITLE_RES", this.titleResId);
    paramBundle.putString("TIME_PICKER_TITLE_TEXT", this.titleText);
    paramBundle.putInt("TIME_PICKER_OVERRIDE_THEME_RES_ID", this.overrideThemeResId);
  }
  
  public void onStop() {
    super.onStop();
    this.activePresenter = null;
    this.timePickerClockPresenter = null;
    this.timePickerTextInputPresenter = null;
    this.timePickerView = null;
  }
  
  public boolean removeOnCancelListener(DialogInterface.OnCancelListener paramOnCancelListener) {
    return this.cancelListeners.remove(paramOnCancelListener);
  }
  
  public boolean removeOnDismissListener(DialogInterface.OnDismissListener paramOnDismissListener) {
    return this.dismissListeners.remove(paramOnDismissListener);
  }
  
  public boolean removeOnNegativeButtonClickListener(View.OnClickListener paramOnClickListener) {
    return this.negativeButtonListeners.remove(paramOnClickListener);
  }
  
  public boolean removeOnPositiveButtonClickListener(View.OnClickListener paramOnClickListener) {
    return this.positiveButtonListeners.remove(paramOnClickListener);
  }
  
  public static final class Builder {
    private int inputMode;
    
    private int overrideThemeResId = 0;
    
    private TimeModel time = new TimeModel();
    
    private CharSequence titleText;
    
    private int titleTextResId = 0;
    
    public MaterialTimePicker build() {
      return MaterialTimePicker.newInstance(this);
    }
    
    public Builder setHour(int param1Int) {
      this.time.setHourOfDay(param1Int);
      return this;
    }
    
    public Builder setInputMode(int param1Int) {
      this.inputMode = param1Int;
      return this;
    }
    
    public Builder setMinute(int param1Int) {
      this.time.setMinute(param1Int);
      return this;
    }
    
    public Builder setTheme(int param1Int) {
      this.overrideThemeResId = param1Int;
      return this;
    }
    
    public Builder setTimeFormat(int param1Int) {
      int j = this.time.hour;
      int i = this.time.minute;
      TimeModel timeModel = new TimeModel(param1Int);
      this.time = timeModel;
      timeModel.setMinute(i);
      this.time.setHourOfDay(j);
      return this;
    }
    
    public Builder setTitleText(int param1Int) {
      this.titleTextResId = param1Int;
      return this;
    }
    
    public Builder setTitleText(CharSequence param1CharSequence) {
      this.titleText = param1CharSequence;
      return this;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\timepicker\MaterialTimePicker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */