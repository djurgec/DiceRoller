package com.google.android.material.datepicker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.R;
import com.google.android.material.dialog.InsetDialogOnTouchListener;
import com.google.android.material.internal.CheckableImageButton;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.shape.MaterialShapeDrawable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import java.util.LinkedHashSet;

public final class MaterialDatePicker<S> extends DialogFragment {
  private static final String CALENDAR_CONSTRAINTS_KEY = "CALENDAR_CONSTRAINTS_KEY";
  
  static final Object CANCEL_BUTTON_TAG;
  
  static final Object CONFIRM_BUTTON_TAG = "CONFIRM_BUTTON_TAG";
  
  private static final String DATE_SELECTOR_KEY = "DATE_SELECTOR_KEY";
  
  public static final int INPUT_MODE_CALENDAR = 0;
  
  private static final String INPUT_MODE_KEY = "INPUT_MODE_KEY";
  
  public static final int INPUT_MODE_TEXT = 1;
  
  private static final String OVERRIDE_THEME_RES_ID = "OVERRIDE_THEME_RES_ID";
  
  private static final String TITLE_TEXT_KEY = "TITLE_TEXT_KEY";
  
  private static final String TITLE_TEXT_RES_ID_KEY = "TITLE_TEXT_RES_ID_KEY";
  
  static final Object TOGGLE_BUTTON_TAG;
  
  private MaterialShapeDrawable background;
  
  private MaterialCalendar<S> calendar;
  
  private CalendarConstraints calendarConstraints;
  
  private Button confirmButton;
  
  private DateSelector<S> dateSelector;
  
  private boolean fullscreen;
  
  private TextView headerSelectionText;
  
  private CheckableImageButton headerToggleButton;
  
  private int inputMode;
  
  private final LinkedHashSet<DialogInterface.OnCancelListener> onCancelListeners = new LinkedHashSet<>();
  
  private final LinkedHashSet<DialogInterface.OnDismissListener> onDismissListeners = new LinkedHashSet<>();
  
  private final LinkedHashSet<View.OnClickListener> onNegativeButtonClickListeners = new LinkedHashSet<>();
  
  private final LinkedHashSet<MaterialPickerOnPositiveButtonClickListener<? super S>> onPositiveButtonClickListeners = new LinkedHashSet<>();
  
  private int overrideThemeResId;
  
  private PickerFragment<S> pickerFragment;
  
  private CharSequence titleText;
  
  private int titleTextResId;
  
  static {
    CANCEL_BUTTON_TAG = "CANCEL_BUTTON_TAG";
    TOGGLE_BUTTON_TAG = "TOGGLE_BUTTON_TAG";
  }
  
  private static Drawable createHeaderToggleDrawable(Context paramContext) {
    StateListDrawable stateListDrawable = new StateListDrawable();
    Drawable drawable2 = AppCompatResources.getDrawable(paramContext, R.drawable.material_ic_calendar_black_24dp);
    stateListDrawable.addState(new int[] { 16842912 }, drawable2);
    Drawable drawable1 = AppCompatResources.getDrawable(paramContext, R.drawable.material_ic_edit_black_24dp);
    stateListDrawable.addState(new int[0], drawable1);
    return (Drawable)stateListDrawable;
  }
  
  private static int getDialogPickerHeight(Context paramContext) {
    Resources resources = paramContext.getResources();
    return resources.getDimensionPixelSize(R.dimen.mtrl_calendar_navigation_height) + resources.getDimensionPixelOffset(R.dimen.mtrl_calendar_navigation_top_padding) + resources.getDimensionPixelOffset(R.dimen.mtrl_calendar_navigation_bottom_padding) + resources.getDimensionPixelSize(R.dimen.mtrl_calendar_days_of_week_height) + MonthAdapter.MAXIMUM_WEEKS * resources.getDimensionPixelSize(R.dimen.mtrl_calendar_day_height) + (MonthAdapter.MAXIMUM_WEEKS - 1) * resources.getDimensionPixelOffset(R.dimen.mtrl_calendar_month_vertical_padding) + resources.getDimensionPixelOffset(R.dimen.mtrl_calendar_bottom_padding);
  }
  
  private static int getPaddedPickerWidth(Context paramContext) {
    Resources resources = paramContext.getResources();
    int i = resources.getDimensionPixelOffset(R.dimen.mtrl_calendar_content_padding);
    int j = (Month.current()).daysInWeek;
    return i * 2 + j * resources.getDimensionPixelSize(R.dimen.mtrl_calendar_day_width) + (j - 1) * resources.getDimensionPixelOffset(R.dimen.mtrl_calendar_month_horizontal_padding);
  }
  
  private int getThemeResId(Context paramContext) {
    int i = this.overrideThemeResId;
    return (i != 0) ? i : this.dateSelector.getDefaultThemeResId(paramContext);
  }
  
  private void initHeaderToggle(Context paramContext) {
    boolean bool;
    this.headerToggleButton.setTag(TOGGLE_BUTTON_TAG);
    this.headerToggleButton.setImageDrawable(createHeaderToggleDrawable(paramContext));
    CheckableImageButton checkableImageButton = this.headerToggleButton;
    if (this.inputMode != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    checkableImageButton.setChecked(bool);
    ViewCompat.setAccessibilityDelegate((View)this.headerToggleButton, null);
    updateToggleContentDescription(this.headerToggleButton);
    this.headerToggleButton.setOnClickListener(new View.OnClickListener() {
          final MaterialDatePicker this$0;
          
          public void onClick(View param1View) {
            MaterialDatePicker.this.confirmButton.setEnabled(MaterialDatePicker.this.dateSelector.isSelectionComplete());
            MaterialDatePicker.this.headerToggleButton.toggle();
            MaterialDatePicker materialDatePicker = MaterialDatePicker.this;
            materialDatePicker.updateToggleContentDescription(materialDatePicker.headerToggleButton);
            MaterialDatePicker.this.startPickerFragment();
          }
        });
  }
  
  static boolean isFullscreen(Context paramContext) {
    return readMaterialCalendarStyleBoolean(paramContext, 16843277);
  }
  
  static boolean isNestedScrollable(Context paramContext) {
    return readMaterialCalendarStyleBoolean(paramContext, R.attr.nestedScrollable);
  }
  
  static <S> MaterialDatePicker<S> newInstance(Builder<S> paramBuilder) {
    MaterialDatePicker<S> materialDatePicker = new MaterialDatePicker();
    Bundle bundle = new Bundle();
    bundle.putInt("OVERRIDE_THEME_RES_ID", paramBuilder.overrideThemeResId);
    bundle.putParcelable("DATE_SELECTOR_KEY", paramBuilder.dateSelector);
    bundle.putParcelable("CALENDAR_CONSTRAINTS_KEY", paramBuilder.calendarConstraints);
    bundle.putInt("TITLE_TEXT_RES_ID_KEY", paramBuilder.titleTextResId);
    bundle.putCharSequence("TITLE_TEXT_KEY", paramBuilder.titleText);
    bundle.putInt("INPUT_MODE_KEY", paramBuilder.inputMode);
    materialDatePicker.setArguments(bundle);
    return materialDatePicker;
  }
  
  static boolean readMaterialCalendarStyleBoolean(Context paramContext, int paramInt) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(MaterialAttributes.resolveOrThrow(paramContext, R.attr.materialCalendarStyle, MaterialCalendar.class.getCanonicalName()), new int[] { paramInt });
    boolean bool = typedArray.getBoolean(0, false);
    typedArray.recycle();
    return bool;
  }
  
  private void startPickerFragment() {
    MaterialCalendar<S> materialCalendar;
    int i = getThemeResId(requireContext());
    this.calendar = MaterialCalendar.newInstance(this.dateSelector, i, this.calendarConstraints);
    if (this.headerToggleButton.isChecked()) {
      MaterialTextInputPicker<S> materialTextInputPicker = MaterialTextInputPicker.newInstance(this.dateSelector, i, this.calendarConstraints);
    } else {
      materialCalendar = this.calendar;
    } 
    this.pickerFragment = materialCalendar;
    updateHeader();
    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
    fragmentTransaction.replace(R.id.mtrl_calendar_frame, this.pickerFragment);
    fragmentTransaction.commitNow();
    this.pickerFragment.addOnSelectionChangedListener(new OnSelectionChangedListener<S>() {
          final MaterialDatePicker this$0;
          
          public void onIncompleteSelectionChanged() {
            MaterialDatePicker.this.confirmButton.setEnabled(false);
          }
          
          public void onSelectionChanged(S param1S) {
            MaterialDatePicker.this.updateHeader();
            MaterialDatePicker.this.confirmButton.setEnabled(MaterialDatePicker.this.dateSelector.isSelectionComplete());
          }
        });
  }
  
  public static long thisMonthInUtcMilliseconds() {
    return (Month.current()).timeInMillis;
  }
  
  public static long todayInUtcMilliseconds() {
    return UtcDates.getTodayCalendar().getTimeInMillis();
  }
  
  private void updateHeader() {
    String str = getHeaderText();
    this.headerSelectionText.setContentDescription(String.format(getString(R.string.mtrl_picker_announce_current_selection), new Object[] { str }));
    this.headerSelectionText.setText(str);
  }
  
  private void updateToggleContentDescription(CheckableImageButton paramCheckableImageButton) {
    String str;
    if (this.headerToggleButton.isChecked()) {
      str = paramCheckableImageButton.getContext().getString(R.string.mtrl_picker_toggle_to_calendar_input_mode);
    } else {
      str = str.getContext().getString(R.string.mtrl_picker_toggle_to_text_input_mode);
    } 
    this.headerToggleButton.setContentDescription(str);
  }
  
  public boolean addOnCancelListener(DialogInterface.OnCancelListener paramOnCancelListener) {
    return this.onCancelListeners.add(paramOnCancelListener);
  }
  
  public boolean addOnDismissListener(DialogInterface.OnDismissListener paramOnDismissListener) {
    return this.onDismissListeners.add(paramOnDismissListener);
  }
  
  public boolean addOnNegativeButtonClickListener(View.OnClickListener paramOnClickListener) {
    return this.onNegativeButtonClickListeners.add(paramOnClickListener);
  }
  
  public boolean addOnPositiveButtonClickListener(MaterialPickerOnPositiveButtonClickListener<? super S> paramMaterialPickerOnPositiveButtonClickListener) {
    return this.onPositiveButtonClickListeners.add(paramMaterialPickerOnPositiveButtonClickListener);
  }
  
  public void clearOnCancelListeners() {
    this.onCancelListeners.clear();
  }
  
  public void clearOnDismissListeners() {
    this.onDismissListeners.clear();
  }
  
  public void clearOnNegativeButtonClickListeners() {
    this.onNegativeButtonClickListeners.clear();
  }
  
  public void clearOnPositiveButtonClickListeners() {
    this.onPositiveButtonClickListeners.clear();
  }
  
  public String getHeaderText() {
    return this.dateSelector.getSelectionDisplayString(getContext());
  }
  
  public final S getSelection() {
    return this.dateSelector.getSelection();
  }
  
  public final void onCancel(DialogInterface paramDialogInterface) {
    Iterator<DialogInterface.OnCancelListener> iterator = this.onCancelListeners.iterator();
    while (iterator.hasNext())
      ((DialogInterface.OnCancelListener)iterator.next()).onCancel(paramDialogInterface); 
    super.onCancel(paramDialogInterface);
  }
  
  public final void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    if (paramBundle == null)
      paramBundle = getArguments(); 
    this.overrideThemeResId = paramBundle.getInt("OVERRIDE_THEME_RES_ID");
    this.dateSelector = (DateSelector<S>)paramBundle.getParcelable("DATE_SELECTOR_KEY");
    this.calendarConstraints = (CalendarConstraints)paramBundle.getParcelable("CALENDAR_CONSTRAINTS_KEY");
    this.titleTextResId = paramBundle.getInt("TITLE_TEXT_RES_ID_KEY");
    this.titleText = paramBundle.getCharSequence("TITLE_TEXT_KEY");
    this.inputMode = paramBundle.getInt("INPUT_MODE_KEY");
  }
  
  public final Dialog onCreateDialog(Bundle paramBundle) {
    Dialog dialog = new Dialog(requireContext(), getThemeResId(requireContext()));
    Context context = dialog.getContext();
    this.fullscreen = isFullscreen(context);
    int i = MaterialAttributes.resolveOrThrow(context, R.attr.colorSurface, MaterialDatePicker.class.getCanonicalName());
    MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(context, null, R.attr.materialCalendarStyle, R.style.Widget_MaterialComponents_MaterialCalendar);
    this.background = materialShapeDrawable;
    materialShapeDrawable.initializeElevationOverlay(context);
    this.background.setFillColor(ColorStateList.valueOf(i));
    this.background.setElevation(ViewCompat.getElevation(dialog.getWindow().getDecorView()));
    return dialog;
  }
  
  public final View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
    int i;
    if (this.fullscreen) {
      i = R.layout.mtrl_picker_fullscreen;
    } else {
      i = R.layout.mtrl_picker_dialog;
    } 
    View view = paramLayoutInflater.inflate(i, paramViewGroup);
    Context context = view.getContext();
    if (this.fullscreen) {
      view.findViewById(R.id.mtrl_calendar_frame).setLayoutParams((ViewGroup.LayoutParams)new LinearLayout.LayoutParams(getPaddedPickerWidth(context), -2));
    } else {
      View view2 = view.findViewById(R.id.mtrl_calendar_main_pane);
      View view1 = view.findViewById(R.id.mtrl_calendar_frame);
      view2.setLayoutParams((ViewGroup.LayoutParams)new LinearLayout.LayoutParams(getPaddedPickerWidth(context), -1));
      view1.setMinimumHeight(getDialogPickerHeight(requireContext()));
    } 
    TextView textView = (TextView)view.findViewById(R.id.mtrl_picker_header_selection_text);
    this.headerSelectionText = textView;
    ViewCompat.setAccessibilityLiveRegion((View)textView, 1);
    this.headerToggleButton = (CheckableImageButton)view.findViewById(R.id.mtrl_picker_header_toggle);
    textView = (TextView)view.findViewById(R.id.mtrl_picker_title_text);
    CharSequence charSequence = this.titleText;
    if (charSequence != null) {
      textView.setText(charSequence);
    } else {
      textView.setText(this.titleTextResId);
    } 
    initHeaderToggle(context);
    this.confirmButton = (Button)view.findViewById(R.id.confirm_button);
    if (this.dateSelector.isSelectionComplete()) {
      this.confirmButton.setEnabled(true);
    } else {
      this.confirmButton.setEnabled(false);
    } 
    this.confirmButton.setTag(CONFIRM_BUTTON_TAG);
    this.confirmButton.setOnClickListener(new View.OnClickListener() {
          final MaterialDatePicker this$0;
          
          public void onClick(View param1View) {
            Iterator<MaterialPickerOnPositiveButtonClickListener> iterator = MaterialDatePicker.this.onPositiveButtonClickListeners.iterator();
            while (iterator.hasNext())
              ((MaterialPickerOnPositiveButtonClickListener)iterator.next()).onPositiveButtonClick(MaterialDatePicker.this.getSelection()); 
            MaterialDatePicker.this.dismiss();
          }
        });
    Button button = (Button)view.findViewById(R.id.cancel_button);
    button.setTag(CANCEL_BUTTON_TAG);
    button.setOnClickListener(new View.OnClickListener() {
          final MaterialDatePicker this$0;
          
          public void onClick(View param1View) {
            Iterator<View.OnClickListener> iterator = MaterialDatePicker.this.onNegativeButtonClickListeners.iterator();
            while (iterator.hasNext())
              ((View.OnClickListener)iterator.next()).onClick(param1View); 
            MaterialDatePicker.this.dismiss();
          }
        });
    return view;
  }
  
  public final void onDismiss(DialogInterface paramDialogInterface) {
    Iterator<DialogInterface.OnDismissListener> iterator = this.onDismissListeners.iterator();
    while (iterator.hasNext())
      ((DialogInterface.OnDismissListener)iterator.next()).onDismiss(paramDialogInterface); 
    ViewGroup viewGroup = (ViewGroup)getView();
    if (viewGroup != null)
      viewGroup.removeAllViews(); 
    super.onDismiss(paramDialogInterface);
  }
  
  public final void onSaveInstanceState(Bundle paramBundle) {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("OVERRIDE_THEME_RES_ID", this.overrideThemeResId);
    paramBundle.putParcelable("DATE_SELECTOR_KEY", this.dateSelector);
    CalendarConstraints.Builder builder = new CalendarConstraints.Builder(this.calendarConstraints);
    if (this.calendar.getCurrentMonth() != null)
      builder.setOpenAt((this.calendar.getCurrentMonth()).timeInMillis); 
    paramBundle.putParcelable("CALENDAR_CONSTRAINTS_KEY", builder.build());
    paramBundle.putInt("TITLE_TEXT_RES_ID_KEY", this.titleTextResId);
    paramBundle.putCharSequence("TITLE_TEXT_KEY", this.titleText);
  }
  
  public void onStart() {
    super.onStart();
    Window window = requireDialog().getWindow();
    if (this.fullscreen) {
      window.setLayout(-1, -1);
      window.setBackgroundDrawable((Drawable)this.background);
    } else {
      window.setLayout(-2, -2);
      int i = getResources().getDimensionPixelOffset(R.dimen.mtrl_calendar_dialog_background_inset);
      Rect rect = new Rect(i, i, i, i);
      window.setBackgroundDrawable((Drawable)new InsetDrawable((Drawable)this.background, i, i, i, i));
      window.getDecorView().setOnTouchListener((View.OnTouchListener)new InsetDialogOnTouchListener(requireDialog(), rect));
    } 
    startPickerFragment();
  }
  
  public void onStop() {
    this.pickerFragment.clearOnSelectionChangedListeners();
    super.onStop();
  }
  
  public boolean removeOnCancelListener(DialogInterface.OnCancelListener paramOnCancelListener) {
    return this.onCancelListeners.remove(paramOnCancelListener);
  }
  
  public boolean removeOnDismissListener(DialogInterface.OnDismissListener paramOnDismissListener) {
    return this.onDismissListeners.remove(paramOnDismissListener);
  }
  
  public boolean removeOnNegativeButtonClickListener(View.OnClickListener paramOnClickListener) {
    return this.onNegativeButtonClickListeners.remove(paramOnClickListener);
  }
  
  public boolean removeOnPositiveButtonClickListener(MaterialPickerOnPositiveButtonClickListener<? super S> paramMaterialPickerOnPositiveButtonClickListener) {
    return this.onPositiveButtonClickListeners.remove(paramMaterialPickerOnPositiveButtonClickListener);
  }
  
  public static final class Builder<S> {
    CalendarConstraints calendarConstraints;
    
    final DateSelector<S> dateSelector;
    
    int inputMode = 0;
    
    int overrideThemeResId = 0;
    
    S selection = null;
    
    CharSequence titleText = null;
    
    int titleTextResId = 0;
    
    private Builder(DateSelector<S> param1DateSelector) {
      this.dateSelector = param1DateSelector;
    }
    
    private Month createDefaultOpenAt() {
      long l1 = (this.calendarConstraints.getStart()).timeInMillis;
      long l3 = (this.calendarConstraints.getEnd()).timeInMillis;
      if (!this.dateSelector.getSelectedDays().isEmpty()) {
        long l = ((Long)this.dateSelector.getSelectedDays().iterator().next()).longValue();
        if (l >= l1 && l <= l3)
          return Month.create(l); 
      } 
      long l2 = MaterialDatePicker.thisMonthInUtcMilliseconds();
      if (l1 <= l2 && l2 <= l3)
        l1 = l2; 
      return Month.create(l1);
    }
    
    public static <S> Builder<S> customDatePicker(DateSelector<S> param1DateSelector) {
      return new Builder<>(param1DateSelector);
    }
    
    public static Builder<Long> datePicker() {
      return new Builder<>(new SingleDateSelector());
    }
    
    public static Builder<Pair<Long, Long>> dateRangePicker() {
      return new Builder<>(new RangeDateSelector());
    }
    
    public MaterialDatePicker<S> build() {
      if (this.calendarConstraints == null)
        this.calendarConstraints = (new CalendarConstraints.Builder()).build(); 
      if (this.titleTextResId == 0)
        this.titleTextResId = this.dateSelector.getDefaultTitleResId(); 
      S s = this.selection;
      if (s != null)
        this.dateSelector.setSelection(s); 
      if (this.calendarConstraints.getOpenAt() == null)
        this.calendarConstraints.setOpenAt(createDefaultOpenAt()); 
      return MaterialDatePicker.newInstance(this);
    }
    
    public Builder<S> setCalendarConstraints(CalendarConstraints param1CalendarConstraints) {
      this.calendarConstraints = param1CalendarConstraints;
      return this;
    }
    
    public Builder<S> setInputMode(int param1Int) {
      this.inputMode = param1Int;
      return this;
    }
    
    public Builder<S> setSelection(S param1S) {
      this.selection = param1S;
      return this;
    }
    
    public Builder<S> setTheme(int param1Int) {
      this.overrideThemeResId = param1Int;
      return this;
    }
    
    public Builder<S> setTitleText(int param1Int) {
      this.titleTextResId = param1Int;
      this.titleText = null;
      return this;
    }
    
    public Builder<S> setTitleText(CharSequence param1CharSequence) {
      this.titleText = param1CharSequence;
      this.titleTextResId = 0;
      return this;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface InputMode {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\datepicker\MaterialDatePicker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */