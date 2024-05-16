package com.google.android.material.textfield;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.DrawableUtils;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.text.BidiFormatter;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.TextViewCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.CheckableImageButton;
import com.google.android.material.internal.CollapsingTextHelper;
import com.google.android.material.internal.DescendantOffsetUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class TextInputLayout extends LinearLayout {
  public static final int BOX_BACKGROUND_FILLED = 1;
  
  public static final int BOX_BACKGROUND_NONE = 0;
  
  public static final int BOX_BACKGROUND_OUTLINE = 2;
  
  private static final int DEF_STYLE_RES = R.style.Widget_Design_TextInputLayout;
  
  public static final int END_ICON_CLEAR_TEXT = 2;
  
  public static final int END_ICON_CUSTOM = -1;
  
  public static final int END_ICON_DROPDOWN_MENU = 3;
  
  public static final int END_ICON_NONE = 0;
  
  public static final int END_ICON_PASSWORD_TOGGLE = 1;
  
  private static final int INVALID_MAX_LENGTH = -1;
  
  private static final int LABEL_SCALE_ANIMATION_DURATION = 167;
  
  private static final String LOG_TAG = "TextInputLayout";
  
  private static final int NO_WIDTH = -1;
  
  private ValueAnimator animator;
  
  private MaterialShapeDrawable boxBackground;
  
  private int boxBackgroundColor;
  
  private int boxBackgroundMode;
  
  private int boxCollapsedPaddingTopPx;
  
  private int boxLabelCutoutHeight;
  
  private final int boxLabelCutoutPaddingPx;
  
  private int boxStrokeColor;
  
  private int boxStrokeWidthDefaultPx;
  
  private int boxStrokeWidthFocusedPx;
  
  private int boxStrokeWidthPx;
  
  private MaterialShapeDrawable boxUnderline;
  
  final CollapsingTextHelper collapsingTextHelper;
  
  boolean counterEnabled;
  
  private int counterMaxLength;
  
  private int counterOverflowTextAppearance;
  
  private ColorStateList counterOverflowTextColor;
  
  private boolean counterOverflowed;
  
  private int counterTextAppearance;
  
  private ColorStateList counterTextColor;
  
  private TextView counterView;
  
  private int defaultFilledBackgroundColor;
  
  private ColorStateList defaultHintTextColor;
  
  private int defaultStrokeColor;
  
  private int disabledColor;
  
  private int disabledFilledBackgroundColor;
  
  EditText editText;
  
  private final LinkedHashSet<OnEditTextAttachedListener> editTextAttachedListeners = new LinkedHashSet<>();
  
  private Drawable endDummyDrawable;
  
  private int endDummyDrawableWidth;
  
  private final LinkedHashSet<OnEndIconChangedListener> endIconChangedListeners;
  
  private final SparseArray<EndIconDelegate> endIconDelegates;
  
  private final FrameLayout endIconFrame;
  
  private int endIconMode = 0;
  
  private View.OnLongClickListener endIconOnLongClickListener;
  
  private ColorStateList endIconTintList;
  
  private PorterDuff.Mode endIconTintMode;
  
  private final CheckableImageButton endIconView;
  
  private final LinearLayout endLayout;
  
  private View.OnLongClickListener errorIconOnLongClickListener;
  
  private ColorStateList errorIconTintList;
  
  private final CheckableImageButton errorIconView;
  
  private boolean expandedHintEnabled;
  
  private int focusedFilledBackgroundColor;
  
  private int focusedStrokeColor;
  
  private ColorStateList focusedTextColor;
  
  private boolean hasEndIconTintList;
  
  private boolean hasEndIconTintMode;
  
  private boolean hasStartIconTintList;
  
  private boolean hasStartIconTintMode;
  
  private CharSequence hint;
  
  private boolean hintAnimationEnabled;
  
  private boolean hintEnabled;
  
  private boolean hintExpanded;
  
  private int hoveredFilledBackgroundColor;
  
  private int hoveredStrokeColor;
  
  private boolean inDrawableStateChanged;
  
  private final IndicatorViewController indicatorViewController = new IndicatorViewController(this);
  
  private final FrameLayout inputFrame;
  
  private boolean isProvidingHint;
  
  private int maxWidth = -1;
  
  private int minWidth = -1;
  
  private Drawable originalEditTextEndDrawable;
  
  private CharSequence originalHint;
  
  private boolean placeholderEnabled;
  
  private CharSequence placeholderText;
  
  private int placeholderTextAppearance;
  
  private ColorStateList placeholderTextColor;
  
  private TextView placeholderTextView;
  
  private CharSequence prefixText;
  
  private final TextView prefixTextView;
  
  private boolean restoringSavedState;
  
  private ShapeAppearanceModel shapeAppearanceModel;
  
  private Drawable startDummyDrawable;
  
  private int startDummyDrawableWidth;
  
  private View.OnLongClickListener startIconOnLongClickListener;
  
  private ColorStateList startIconTintList;
  
  private PorterDuff.Mode startIconTintMode;
  
  private final CheckableImageButton startIconView;
  
  private final LinearLayout startLayout;
  
  private ColorStateList strokeErrorColor;
  
  private CharSequence suffixText;
  
  private final TextView suffixTextView;
  
  private final Rect tmpBoundsRect = new Rect();
  
  private final Rect tmpRect = new Rect();
  
  private final RectF tmpRectF = new RectF();
  
  private Typeface typeface;
  
  public TextInputLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public TextInputLayout(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.textInputStyle);
  }
  
  public TextInputLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(MaterialThemeOverlay.wrap(paramContext, paramAttributeSet, paramInt, i), paramAttributeSet, paramInt);
    SparseArray<EndIconDelegate> sparseArray = new SparseArray();
    this.endIconDelegates = sparseArray;
    this.endIconChangedListeners = new LinkedHashSet<>();
    CollapsingTextHelper collapsingTextHelper = new CollapsingTextHelper((View)this);
    this.collapsingTextHelper = collapsingTextHelper;
    Context context = getContext();
    setOrientation(1);
    setWillNotDraw(false);
    setAddStatesFromChildren(true);
    FrameLayout frameLayout = new FrameLayout(context);
    this.inputFrame = frameLayout;
    frameLayout.setAddStatesFromChildren(true);
    addView((View)frameLayout);
    LinearLayout linearLayout2 = new LinearLayout(context);
    this.startLayout = linearLayout2;
    linearLayout2.setOrientation(0);
    linearLayout2.setLayoutParams((ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-2, -1, 8388611));
    frameLayout.addView((View)linearLayout2);
    LinearLayout linearLayout1 = new LinearLayout(context);
    this.endLayout = linearLayout1;
    linearLayout1.setOrientation(0);
    linearLayout1.setLayoutParams((ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-2, -1, 8388613));
    frameLayout.addView((View)linearLayout1);
    frameLayout = new FrameLayout(context);
    this.endIconFrame = frameLayout;
    frameLayout.setLayoutParams((ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-2, -1));
    collapsingTextHelper.setTextSizeInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
    collapsingTextHelper.setPositionInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
    collapsingTextHelper.setCollapsedTextGravity(8388659);
    TintTypedArray tintTypedArray = ThemeEnforcement.obtainTintedStyledAttributes(context, paramAttributeSet, R.styleable.TextInputLayout, paramInt, i, new int[] { R.styleable.TextInputLayout_counterTextAppearance, R.styleable.TextInputLayout_counterOverflowTextAppearance, R.styleable.TextInputLayout_errorTextAppearance, R.styleable.TextInputLayout_helperTextTextAppearance, R.styleable.TextInputLayout_hintTextAppearance });
    this.hintEnabled = tintTypedArray.getBoolean(R.styleable.TextInputLayout_hintEnabled, true);
    setHint(tintTypedArray.getText(R.styleable.TextInputLayout_android_hint));
    this.hintAnimationEnabled = tintTypedArray.getBoolean(R.styleable.TextInputLayout_hintAnimationEnabled, true);
    this.expandedHintEnabled = tintTypedArray.getBoolean(R.styleable.TextInputLayout_expandedHintEnabled, true);
    if (tintTypedArray.hasValue(R.styleable.TextInputLayout_android_minWidth))
      setMinWidth(tintTypedArray.getDimensionPixelSize(R.styleable.TextInputLayout_android_minWidth, -1)); 
    if (tintTypedArray.hasValue(R.styleable.TextInputLayout_android_maxWidth))
      setMaxWidth(tintTypedArray.getDimensionPixelSize(R.styleable.TextInputLayout_android_maxWidth, -1)); 
    this.shapeAppearanceModel = ShapeAppearanceModel.builder(context, paramAttributeSet, paramInt, i).build();
    this.boxLabelCutoutPaddingPx = context.getResources().getDimensionPixelOffset(R.dimen.mtrl_textinput_box_label_cutout_padding);
    this.boxCollapsedPaddingTopPx = tintTypedArray.getDimensionPixelOffset(R.styleable.TextInputLayout_boxCollapsedPaddingTop, 0);
    this.boxStrokeWidthDefaultPx = tintTypedArray.getDimensionPixelSize(R.styleable.TextInputLayout_boxStrokeWidth, context.getResources().getDimensionPixelSize(R.dimen.mtrl_textinput_box_stroke_width_default));
    this.boxStrokeWidthFocusedPx = tintTypedArray.getDimensionPixelSize(R.styleable.TextInputLayout_boxStrokeWidthFocused, context.getResources().getDimensionPixelSize(R.dimen.mtrl_textinput_box_stroke_width_focused));
    this.boxStrokeWidthPx = this.boxStrokeWidthDefaultPx;
    float f1 = tintTypedArray.getDimension(R.styleable.TextInputLayout_boxCornerRadiusTopStart, -1.0F);
    float f4 = tintTypedArray.getDimension(R.styleable.TextInputLayout_boxCornerRadiusTopEnd, -1.0F);
    float f2 = tintTypedArray.getDimension(R.styleable.TextInputLayout_boxCornerRadiusBottomEnd, -1.0F);
    float f3 = tintTypedArray.getDimension(R.styleable.TextInputLayout_boxCornerRadiusBottomStart, -1.0F);
    ShapeAppearanceModel.Builder builder = this.shapeAppearanceModel.toBuilder();
    if (f1 >= 0.0F)
      builder.setTopLeftCornerSize(f1); 
    if (f4 >= 0.0F)
      builder.setTopRightCornerSize(f4); 
    if (f2 >= 0.0F)
      builder.setBottomRightCornerSize(f2); 
    if (f3 >= 0.0F)
      builder.setBottomLeftCornerSize(f3); 
    this.shapeAppearanceModel = builder.build();
    ColorStateList colorStateList = MaterialResources.getColorStateList(context, tintTypedArray, R.styleable.TextInputLayout_boxBackgroundColor);
    if (colorStateList != null) {
      paramInt = colorStateList.getDefaultColor();
      this.defaultFilledBackgroundColor = paramInt;
      this.boxBackgroundColor = paramInt;
      if (colorStateList.isStateful()) {
        this.disabledFilledBackgroundColor = colorStateList.getColorForState(new int[] { -16842910 }, -1);
        this.focusedFilledBackgroundColor = colorStateList.getColorForState(new int[] { 16842908, 16842910 }, -1);
        this.hoveredFilledBackgroundColor = colorStateList.getColorForState(new int[] { 16843623, 16842910 }, -1);
      } else {
        this.focusedFilledBackgroundColor = this.defaultFilledBackgroundColor;
        colorStateList = AppCompatResources.getColorStateList(context, R.color.mtrl_filled_background_color);
        this.disabledFilledBackgroundColor = colorStateList.getColorForState(new int[] { -16842910 }, -1);
        this.hoveredFilledBackgroundColor = colorStateList.getColorForState(new int[] { 16843623 }, -1);
      } 
    } else {
      this.boxBackgroundColor = 0;
      this.defaultFilledBackgroundColor = 0;
      this.disabledFilledBackgroundColor = 0;
      this.focusedFilledBackgroundColor = 0;
      this.hoveredFilledBackgroundColor = 0;
    } 
    if (tintTypedArray.hasValue(R.styleable.TextInputLayout_android_textColorHint)) {
      colorStateList = tintTypedArray.getColorStateList(R.styleable.TextInputLayout_android_textColorHint);
      this.focusedTextColor = colorStateList;
      this.defaultHintTextColor = colorStateList;
    } 
    colorStateList = MaterialResources.getColorStateList(context, tintTypedArray, R.styleable.TextInputLayout_boxStrokeColor);
    this.focusedStrokeColor = tintTypedArray.getColor(R.styleable.TextInputLayout_boxStrokeColor, 0);
    this.defaultStrokeColor = ContextCompat.getColor(context, R.color.mtrl_textinput_default_box_stroke_color);
    this.disabledColor = ContextCompat.getColor(context, R.color.mtrl_textinput_disabled_color);
    this.hoveredStrokeColor = ContextCompat.getColor(context, R.color.mtrl_textinput_hovered_box_stroke_color);
    if (colorStateList != null)
      setBoxStrokeColorStateList(colorStateList); 
    if (tintTypedArray.hasValue(R.styleable.TextInputLayout_boxStrokeErrorColor))
      setBoxStrokeErrorColor(MaterialResources.getColorStateList(context, tintTypedArray, R.styleable.TextInputLayout_boxStrokeErrorColor)); 
    if (tintTypedArray.getResourceId(R.styleable.TextInputLayout_hintTextAppearance, -1) != -1)
      setHintTextAppearance(tintTypedArray.getResourceId(R.styleable.TextInputLayout_hintTextAppearance, 0)); 
    int k = tintTypedArray.getResourceId(R.styleable.TextInputLayout_errorTextAppearance, 0);
    CharSequence charSequence3 = tintTypedArray.getText(R.styleable.TextInputLayout_errorContentDescription);
    boolean bool1 = tintTypedArray.getBoolean(R.styleable.TextInputLayout_errorEnabled, false);
    CheckableImageButton checkableImageButton1 = (CheckableImageButton)LayoutInflater.from(getContext()).inflate(R.layout.design_text_input_end_icon, (ViewGroup)linearLayout1, false);
    this.errorIconView = checkableImageButton1;
    checkableImageButton1.setId(R.id.text_input_error_icon);
    checkableImageButton1.setVisibility(8);
    if (MaterialResources.isFontScaleAtLeast1_3(context))
      MarginLayoutParamsCompat.setMarginStart((ViewGroup.MarginLayoutParams)checkableImageButton1.getLayoutParams(), 0); 
    if (tintTypedArray.hasValue(R.styleable.TextInputLayout_errorIconDrawable))
      setErrorIconDrawable(tintTypedArray.getDrawable(R.styleable.TextInputLayout_errorIconDrawable)); 
    if (tintTypedArray.hasValue(R.styleable.TextInputLayout_errorIconTint))
      setErrorIconTintList(MaterialResources.getColorStateList(context, tintTypedArray, R.styleable.TextInputLayout_errorIconTint)); 
    if (tintTypedArray.hasValue(R.styleable.TextInputLayout_errorIconTintMode))
      setErrorIconTintMode(ViewUtils.parseTintMode(tintTypedArray.getInt(R.styleable.TextInputLayout_errorIconTintMode, -1), null)); 
    checkableImageButton1.setContentDescription(getResources().getText(R.string.error_icon_content_description));
    ViewCompat.setImportantForAccessibility((View)checkableImageButton1, 2);
    checkableImageButton1.setClickable(false);
    checkableImageButton1.setPressable(false);
    checkableImageButton1.setFocusable(false);
    int j = tintTypedArray.getResourceId(R.styleable.TextInputLayout_helperTextTextAppearance, 0);
    boolean bool3 = tintTypedArray.getBoolean(R.styleable.TextInputLayout_helperTextEnabled, false);
    CharSequence charSequence1 = tintTypedArray.getText(R.styleable.TextInputLayout_helperText);
    i = tintTypedArray.getResourceId(R.styleable.TextInputLayout_placeholderTextAppearance, 0);
    CharSequence charSequence5 = tintTypedArray.getText(R.styleable.TextInputLayout_placeholderText);
    int m = tintTypedArray.getResourceId(R.styleable.TextInputLayout_prefixTextAppearance, 0);
    CharSequence charSequence2 = tintTypedArray.getText(R.styleable.TextInputLayout_prefixText);
    paramInt = tintTypedArray.getResourceId(R.styleable.TextInputLayout_suffixTextAppearance, 0);
    CharSequence charSequence4 = tintTypedArray.getText(R.styleable.TextInputLayout_suffixText);
    boolean bool2 = tintTypedArray.getBoolean(R.styleable.TextInputLayout_counterEnabled, false);
    setCounterMaxLength(tintTypedArray.getInt(R.styleable.TextInputLayout_counterMaxLength, -1));
    this.counterTextAppearance = tintTypedArray.getResourceId(R.styleable.TextInputLayout_counterTextAppearance, 0);
    this.counterOverflowTextAppearance = tintTypedArray.getResourceId(R.styleable.TextInputLayout_counterOverflowTextAppearance, 0);
    CheckableImageButton checkableImageButton2 = (CheckableImageButton)LayoutInflater.from(getContext()).inflate(R.layout.design_text_input_start_icon, (ViewGroup)linearLayout2, false);
    this.startIconView = checkableImageButton2;
    checkableImageButton2.setVisibility(8);
    if (MaterialResources.isFontScaleAtLeast1_3(context))
      MarginLayoutParamsCompat.setMarginEnd((ViewGroup.MarginLayoutParams)checkableImageButton2.getLayoutParams(), 0); 
    setStartIconOnClickListener((View.OnClickListener)null);
    setStartIconOnLongClickListener((View.OnLongClickListener)null);
    if (tintTypedArray.hasValue(R.styleable.TextInputLayout_startIconDrawable)) {
      setStartIconDrawable(tintTypedArray.getDrawable(R.styleable.TextInputLayout_startIconDrawable));
      if (tintTypedArray.hasValue(R.styleable.TextInputLayout_startIconContentDescription))
        setStartIconContentDescription(tintTypedArray.getText(R.styleable.TextInputLayout_startIconContentDescription)); 
      setStartIconCheckable(tintTypedArray.getBoolean(R.styleable.TextInputLayout_startIconCheckable, true));
    } 
    if (tintTypedArray.hasValue(R.styleable.TextInputLayout_startIconTint))
      setStartIconTintList(MaterialResources.getColorStateList(context, tintTypedArray, R.styleable.TextInputLayout_startIconTint)); 
    if (tintTypedArray.hasValue(R.styleable.TextInputLayout_startIconTintMode))
      setStartIconTintMode(ViewUtils.parseTintMode(tintTypedArray.getInt(R.styleable.TextInputLayout_startIconTintMode, -1), null)); 
    setBoxBackgroundMode(tintTypedArray.getInt(R.styleable.TextInputLayout_boxBackgroundMode, 0));
    CheckableImageButton checkableImageButton3 = (CheckableImageButton)LayoutInflater.from(getContext()).inflate(R.layout.design_text_input_end_icon, (ViewGroup)frameLayout, false);
    this.endIconView = checkableImageButton3;
    frameLayout.addView((View)checkableImageButton3);
    checkableImageButton3.setVisibility(8);
    if (MaterialResources.isFontScaleAtLeast1_3(context))
      MarginLayoutParamsCompat.setMarginStart((ViewGroup.MarginLayoutParams)checkableImageButton3.getLayoutParams(), 0); 
    sparseArray.append(-1, new CustomEndIconDelegate(this));
    sparseArray.append(0, new NoEndIconDelegate(this));
    sparseArray.append(1, new PasswordToggleEndIconDelegate(this));
    sparseArray.append(2, new ClearTextEndIconDelegate(this));
    sparseArray.append(3, new DropdownMenuEndIconDelegate(this));
    if (tintTypedArray.hasValue(R.styleable.TextInputLayout_endIconMode)) {
      setEndIconMode(tintTypedArray.getInt(R.styleable.TextInputLayout_endIconMode, 0));
      if (tintTypedArray.hasValue(R.styleable.TextInputLayout_endIconDrawable))
        setEndIconDrawable(tintTypedArray.getDrawable(R.styleable.TextInputLayout_endIconDrawable)); 
      if (tintTypedArray.hasValue(R.styleable.TextInputLayout_endIconContentDescription))
        setEndIconContentDescription(tintTypedArray.getText(R.styleable.TextInputLayout_endIconContentDescription)); 
      setEndIconCheckable(tintTypedArray.getBoolean(R.styleable.TextInputLayout_endIconCheckable, true));
    } else if (tintTypedArray.hasValue(R.styleable.TextInputLayout_passwordToggleEnabled)) {
      setEndIconMode(tintTypedArray.getBoolean(R.styleable.TextInputLayout_passwordToggleEnabled, false));
      setEndIconDrawable(tintTypedArray.getDrawable(R.styleable.TextInputLayout_passwordToggleDrawable));
      setEndIconContentDescription(tintTypedArray.getText(R.styleable.TextInputLayout_passwordToggleContentDescription));
      if (tintTypedArray.hasValue(R.styleable.TextInputLayout_passwordToggleTint))
        setEndIconTintList(MaterialResources.getColorStateList(context, tintTypedArray, R.styleable.TextInputLayout_passwordToggleTint)); 
      if (tintTypedArray.hasValue(R.styleable.TextInputLayout_passwordToggleTintMode))
        setEndIconTintMode(ViewUtils.parseTintMode(tintTypedArray.getInt(R.styleable.TextInputLayout_passwordToggleTintMode, -1), null)); 
    } 
    if (!tintTypedArray.hasValue(R.styleable.TextInputLayout_passwordToggleEnabled)) {
      if (tintTypedArray.hasValue(R.styleable.TextInputLayout_endIconTint))
        setEndIconTintList(MaterialResources.getColorStateList(context, tintTypedArray, R.styleable.TextInputLayout_endIconTint)); 
      if (tintTypedArray.hasValue(R.styleable.TextInputLayout_endIconTintMode))
        setEndIconTintMode(ViewUtils.parseTintMode(tintTypedArray.getInt(R.styleable.TextInputLayout_endIconTintMode, -1), null)); 
    } 
    AppCompatTextView appCompatTextView2 = new AppCompatTextView(context);
    this.prefixTextView = (TextView)appCompatTextView2;
    appCompatTextView2.setId(R.id.textinput_prefix_text);
    appCompatTextView2.setLayoutParams((ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-2, -2));
    ViewCompat.setAccessibilityLiveRegion((View)appCompatTextView2, 1);
    linearLayout2.addView((View)checkableImageButton2);
    linearLayout2.addView((View)appCompatTextView2);
    AppCompatTextView appCompatTextView1 = new AppCompatTextView(context);
    this.suffixTextView = (TextView)appCompatTextView1;
    appCompatTextView1.setId(R.id.textinput_suffix_text);
    appCompatTextView1.setLayoutParams((ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-2, -2, 80));
    ViewCompat.setAccessibilityLiveRegion((View)appCompatTextView1, 1);
    linearLayout1.addView((View)appCompatTextView1);
    linearLayout1.addView((View)checkableImageButton1);
    linearLayout1.addView((View)frameLayout);
    setHelperTextEnabled(bool3);
    setHelperText(charSequence1);
    setHelperTextTextAppearance(j);
    setErrorEnabled(bool1);
    setErrorTextAppearance(k);
    setErrorContentDescription(charSequence3);
    setCounterTextAppearance(this.counterTextAppearance);
    setCounterOverflowTextAppearance(this.counterOverflowTextAppearance);
    setPlaceholderText(charSequence5);
    setPlaceholderTextAppearance(i);
    setPrefixText(charSequence2);
    setPrefixTextAppearance(m);
    setSuffixText(charSequence4);
    setSuffixTextAppearance(paramInt);
    if (tintTypedArray.hasValue(R.styleable.TextInputLayout_errorTextColor))
      setErrorTextColor(tintTypedArray.getColorStateList(R.styleable.TextInputLayout_errorTextColor)); 
    if (tintTypedArray.hasValue(R.styleable.TextInputLayout_helperTextTextColor))
      setHelperTextColor(tintTypedArray.getColorStateList(R.styleable.TextInputLayout_helperTextTextColor)); 
    if (tintTypedArray.hasValue(R.styleable.TextInputLayout_hintTextColor))
      setHintTextColor(tintTypedArray.getColorStateList(R.styleable.TextInputLayout_hintTextColor)); 
    if (tintTypedArray.hasValue(R.styleable.TextInputLayout_counterTextColor))
      setCounterTextColor(tintTypedArray.getColorStateList(R.styleable.TextInputLayout_counterTextColor)); 
    if (tintTypedArray.hasValue(R.styleable.TextInputLayout_counterOverflowTextColor))
      setCounterOverflowTextColor(tintTypedArray.getColorStateList(R.styleable.TextInputLayout_counterOverflowTextColor)); 
    if (tintTypedArray.hasValue(R.styleable.TextInputLayout_placeholderTextColor))
      setPlaceholderTextColor(tintTypedArray.getColorStateList(R.styleable.TextInputLayout_placeholderTextColor)); 
    if (tintTypedArray.hasValue(R.styleable.TextInputLayout_prefixTextColor))
      setPrefixTextColor(tintTypedArray.getColorStateList(R.styleable.TextInputLayout_prefixTextColor)); 
    if (tintTypedArray.hasValue(R.styleable.TextInputLayout_suffixTextColor))
      setSuffixTextColor(tintTypedArray.getColorStateList(R.styleable.TextInputLayout_suffixTextColor)); 
    setCounterEnabled(bool2);
    setEnabled(tintTypedArray.getBoolean(R.styleable.TextInputLayout_android_enabled, true));
    tintTypedArray.recycle();
    ViewCompat.setImportantForAccessibility((View)this, 2);
    if (Build.VERSION.SDK_INT >= 26)
      ViewCompat.setImportantForAutofill((View)this, 1); 
  }
  
  private void addPlaceholderTextView() {
    TextView textView = this.placeholderTextView;
    if (textView != null) {
      this.inputFrame.addView((View)textView);
      this.placeholderTextView.setVisibility(0);
    } 
  }
  
  private void adjustFilledEditTextPaddingForLargeFont() {
    if (this.editText == null || this.boxBackgroundMode != 1)
      return; 
    if (MaterialResources.isFontScaleAtLeast2_0(getContext())) {
      EditText editText = this.editText;
      ViewCompat.setPaddingRelative((View)editText, ViewCompat.getPaddingStart((View)editText), getResources().getDimensionPixelSize(R.dimen.material_filled_edittext_font_2_0_padding_top), ViewCompat.getPaddingEnd((View)this.editText), getResources().getDimensionPixelSize(R.dimen.material_filled_edittext_font_2_0_padding_bottom));
    } else if (MaterialResources.isFontScaleAtLeast1_3(getContext())) {
      EditText editText = this.editText;
      ViewCompat.setPaddingRelative((View)editText, ViewCompat.getPaddingStart((View)editText), getResources().getDimensionPixelSize(R.dimen.material_filled_edittext_font_1_3_padding_top), ViewCompat.getPaddingEnd((View)this.editText), getResources().getDimensionPixelSize(R.dimen.material_filled_edittext_font_1_3_padding_bottom));
    } 
  }
  
  private void applyBoxAttributes() {
    MaterialShapeDrawable materialShapeDrawable = this.boxBackground;
    if (materialShapeDrawable == null)
      return; 
    materialShapeDrawable.setShapeAppearanceModel(this.shapeAppearanceModel);
    if (canDrawOutlineStroke())
      this.boxBackground.setStroke(this.boxStrokeWidthPx, this.boxStrokeColor); 
    int i = calculateBoxBackgroundColor();
    this.boxBackgroundColor = i;
    this.boxBackground.setFillColor(ColorStateList.valueOf(i));
    if (this.endIconMode == 3)
      this.editText.getBackground().invalidateSelf(); 
    applyBoxUnderlineAttributes();
    invalidate();
  }
  
  private void applyBoxUnderlineAttributes() {
    if (this.boxUnderline == null)
      return; 
    if (canDrawStroke())
      this.boxUnderline.setFillColor(ColorStateList.valueOf(this.boxStrokeColor)); 
    invalidate();
  }
  
  private void applyCutoutPadding(RectF paramRectF) {
    paramRectF.left -= this.boxLabelCutoutPaddingPx;
    paramRectF.right += this.boxLabelCutoutPaddingPx;
  }
  
  private void applyEndIconTint() {
    applyIconTint(this.endIconView, this.hasEndIconTintList, this.endIconTintList, this.hasEndIconTintMode, this.endIconTintMode);
  }
  
  private void applyIconTint(CheckableImageButton paramCheckableImageButton, boolean paramBoolean1, ColorStateList paramColorStateList, boolean paramBoolean2, PorterDuff.Mode paramMode) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getDrawable : ()Landroid/graphics/drawable/Drawable;
    //   4: astore #7
    //   6: aload #7
    //   8: astore #6
    //   10: aload #7
    //   12: ifnull -> 68
    //   15: iload_2
    //   16: ifne -> 28
    //   19: aload #7
    //   21: astore #6
    //   23: iload #4
    //   25: ifeq -> 68
    //   28: aload #7
    //   30: invokestatic wrap : (Landroid/graphics/drawable/Drawable;)Landroid/graphics/drawable/Drawable;
    //   33: invokevirtual mutate : ()Landroid/graphics/drawable/Drawable;
    //   36: astore #7
    //   38: iload_2
    //   39: ifeq -> 48
    //   42: aload #7
    //   44: aload_3
    //   45: invokestatic setTintList : (Landroid/graphics/drawable/Drawable;Landroid/content/res/ColorStateList;)V
    //   48: aload #7
    //   50: astore #6
    //   52: iload #4
    //   54: ifeq -> 68
    //   57: aload #7
    //   59: aload #5
    //   61: invokestatic setTintMode : (Landroid/graphics/drawable/Drawable;Landroid/graphics/PorterDuff$Mode;)V
    //   64: aload #7
    //   66: astore #6
    //   68: aload_1
    //   69: invokevirtual getDrawable : ()Landroid/graphics/drawable/Drawable;
    //   72: aload #6
    //   74: if_acmpeq -> 83
    //   77: aload_1
    //   78: aload #6
    //   80: invokevirtual setImageDrawable : (Landroid/graphics/drawable/Drawable;)V
    //   83: return
  }
  
  private void applyStartIconTint() {
    applyIconTint(this.startIconView, this.hasStartIconTintList, this.startIconTintList, this.hasStartIconTintMode, this.startIconTintMode);
  }
  
  private void assignBoxBackgroundByMode() {
    switch (this.boxBackgroundMode) {
      default:
        throw new IllegalArgumentException(this.boxBackgroundMode + " is illegal; only @BoxBackgroundMode constants are supported.");
      case 2:
        if (this.hintEnabled && !(this.boxBackground instanceof CutoutDrawable)) {
          this.boxBackground = new CutoutDrawable(this.shapeAppearanceModel);
        } else {
          this.boxBackground = new MaterialShapeDrawable(this.shapeAppearanceModel);
        } 
        this.boxUnderline = null;
        return;
      case 1:
        this.boxBackground = new MaterialShapeDrawable(this.shapeAppearanceModel);
        this.boxUnderline = new MaterialShapeDrawable();
        return;
      case 0:
        break;
    } 
    this.boxBackground = null;
    this.boxUnderline = null;
  }
  
  private int calculateBoxBackgroundColor() {
    int i = this.boxBackgroundColor;
    if (this.boxBackgroundMode == 1)
      i = MaterialColors.layer(MaterialColors.getColor((View)this, R.attr.colorSurface, 0), this.boxBackgroundColor); 
    return i;
  }
  
  private Rect calculateCollapsedTextBounds(Rect paramRect) {
    if (this.editText != null) {
      Rect rect = this.tmpBoundsRect;
      int i = ViewCompat.getLayoutDirection((View)this);
      boolean bool = true;
      if (i != 1)
        bool = false; 
      rect.bottom = paramRect.bottom;
      switch (this.boxBackgroundMode) {
        default:
          rect.left = getLabelLeftBoundAlightWithPrefix(paramRect.left, bool);
          rect.top = getPaddingTop();
          rect.right = getLabelRightBoundAlignedWithSuffix(paramRect.right, bool);
          return rect;
        case 2:
          paramRect.left += this.editText.getPaddingLeft();
          paramRect.top -= calculateLabelMarginTop();
          paramRect.right -= this.editText.getPaddingRight();
          return rect;
        case 1:
          break;
      } 
      rect.left = getLabelLeftBoundAlightWithPrefix(paramRect.left, bool);
      paramRect.top += this.boxCollapsedPaddingTopPx;
      rect.right = getLabelRightBoundAlignedWithSuffix(paramRect.right, bool);
      return rect;
    } 
    throw new IllegalStateException();
  }
  
  private int calculateExpandedLabelBottom(Rect paramRect1, Rect paramRect2, float paramFloat) {
    return isSingleLineFilledTextField() ? (int)(paramRect2.top + paramFloat) : (paramRect1.bottom - this.editText.getCompoundPaddingBottom());
  }
  
  private int calculateExpandedLabelTop(Rect paramRect, float paramFloat) {
    return isSingleLineFilledTextField() ? (int)(paramRect.centerY() - paramFloat / 2.0F) : (paramRect.top + this.editText.getCompoundPaddingTop());
  }
  
  private Rect calculateExpandedTextBounds(Rect paramRect) {
    if (this.editText != null) {
      Rect rect = this.tmpBoundsRect;
      float f = this.collapsingTextHelper.getExpandedTextHeight();
      paramRect.left += this.editText.getCompoundPaddingLeft();
      rect.top = calculateExpandedLabelTop(paramRect, f);
      paramRect.right -= this.editText.getCompoundPaddingRight();
      rect.bottom = calculateExpandedLabelBottom(paramRect, rect, f);
      return rect;
    } 
    throw new IllegalStateException();
  }
  
  private int calculateLabelMarginTop() {
    if (!this.hintEnabled)
      return 0; 
    switch (this.boxBackgroundMode) {
      default:
        return 0;
      case 2:
        return (int)(this.collapsingTextHelper.getCollapsedTextHeight() / 2.0F);
      case 0:
      case 1:
        break;
    } 
    return (int)this.collapsingTextHelper.getCollapsedTextHeight();
  }
  
  private boolean canDrawOutlineStroke() {
    boolean bool;
    if (this.boxBackgroundMode == 2 && canDrawStroke()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean canDrawStroke() {
    boolean bool;
    if (this.boxStrokeWidthPx > -1 && this.boxStrokeColor != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void closeCutout() {
    if (cutoutEnabled())
      ((CutoutDrawable)this.boxBackground).removeCutout(); 
  }
  
  private void collapseHint(boolean paramBoolean) {
    ValueAnimator valueAnimator = this.animator;
    if (valueAnimator != null && valueAnimator.isRunning())
      this.animator.cancel(); 
    if (paramBoolean && this.hintAnimationEnabled) {
      animateToExpansionFraction(1.0F);
    } else {
      this.collapsingTextHelper.setExpansionFraction(1.0F);
    } 
    this.hintExpanded = false;
    if (cutoutEnabled())
      openCutout(); 
    updatePlaceholderText();
    updatePrefixTextVisibility();
    updateSuffixTextVisibility();
  }
  
  private boolean cutoutEnabled() {
    boolean bool;
    if (this.hintEnabled && !TextUtils.isEmpty(this.hint) && this.boxBackground instanceof CutoutDrawable) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void dispatchOnEditTextAttached() {
    Iterator<OnEditTextAttachedListener> iterator = this.editTextAttachedListeners.iterator();
    while (iterator.hasNext())
      ((OnEditTextAttachedListener)iterator.next()).onEditTextAttached(this); 
  }
  
  private void dispatchOnEndIconChanged(int paramInt) {
    Iterator<OnEndIconChangedListener> iterator = this.endIconChangedListeners.iterator();
    while (iterator.hasNext())
      ((OnEndIconChangedListener)iterator.next()).onEndIconChanged(this, paramInt); 
  }
  
  private void drawBoxUnderline(Canvas paramCanvas) {
    MaterialShapeDrawable materialShapeDrawable = this.boxUnderline;
    if (materialShapeDrawable != null) {
      Rect rect = materialShapeDrawable.getBounds();
      rect.top = rect.bottom - this.boxStrokeWidthPx;
      this.boxUnderline.draw(paramCanvas);
    } 
  }
  
  private void drawHint(Canvas paramCanvas) {
    if (this.hintEnabled)
      this.collapsingTextHelper.draw(paramCanvas); 
  }
  
  private void expandHint(boolean paramBoolean) {
    ValueAnimator valueAnimator = this.animator;
    if (valueAnimator != null && valueAnimator.isRunning())
      this.animator.cancel(); 
    if (paramBoolean && this.hintAnimationEnabled) {
      animateToExpansionFraction(0.0F);
    } else {
      this.collapsingTextHelper.setExpansionFraction(0.0F);
    } 
    if (cutoutEnabled() && ((CutoutDrawable)this.boxBackground).hasCutout())
      closeCutout(); 
    this.hintExpanded = true;
    hidePlaceholderText();
    updatePrefixTextVisibility();
    updateSuffixTextVisibility();
  }
  
  private EndIconDelegate getEndIconDelegate() {
    EndIconDelegate endIconDelegate = (EndIconDelegate)this.endIconDelegates.get(this.endIconMode);
    if (endIconDelegate == null)
      endIconDelegate = (EndIconDelegate)this.endIconDelegates.get(0); 
    return endIconDelegate;
  }
  
  private CheckableImageButton getEndIconToUpdateDummyDrawable() {
    return (this.errorIconView.getVisibility() == 0) ? this.errorIconView : ((hasEndIcon() && isEndIconVisible()) ? this.endIconView : null);
  }
  
  private int getLabelLeftBoundAlightWithPrefix(int paramInt, boolean paramBoolean) {
    int i = this.editText.getCompoundPaddingLeft() + paramInt;
    paramInt = i;
    if (this.prefixText != null) {
      paramInt = i;
      if (!paramBoolean)
        paramInt = i - this.prefixTextView.getMeasuredWidth() + this.prefixTextView.getPaddingLeft(); 
    } 
    return paramInt;
  }
  
  private int getLabelRightBoundAlignedWithSuffix(int paramInt, boolean paramBoolean) {
    int i = paramInt - this.editText.getCompoundPaddingRight();
    paramInt = i;
    if (this.prefixText != null) {
      paramInt = i;
      if (paramBoolean)
        paramInt = i + this.prefixTextView.getMeasuredWidth() - this.prefixTextView.getPaddingRight(); 
    } 
    return paramInt;
  }
  
  private boolean hasEndIcon() {
    boolean bool;
    if (this.endIconMode != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void hidePlaceholderText() {
    TextView textView = this.placeholderTextView;
    if (textView != null && this.placeholderEnabled) {
      textView.setText(null);
      this.placeholderTextView.setVisibility(4);
    } 
  }
  
  private boolean isErrorIconVisible() {
    boolean bool;
    if (this.errorIconView.getVisibility() == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean isSingleLineFilledTextField() {
    int i = this.boxBackgroundMode;
    boolean bool = true;
    if (i != 1 || (Build.VERSION.SDK_INT >= 16 && this.editText.getMinLines() > 1))
      bool = false; 
    return bool;
  }
  
  private int[] mergeIconState(CheckableImageButton paramCheckableImageButton) {
    int[] arrayOfInt2 = getDrawableState();
    int[] arrayOfInt1 = paramCheckableImageButton.getDrawableState();
    int i = arrayOfInt2.length;
    arrayOfInt2 = Arrays.copyOf(arrayOfInt2, arrayOfInt2.length + arrayOfInt1.length);
    System.arraycopy(arrayOfInt1, 0, arrayOfInt2, i, arrayOfInt1.length);
    return arrayOfInt2;
  }
  
  private void onApplyBoxBackgroundMode() {
    assignBoxBackgroundByMode();
    setEditTextBoxBackground();
    updateTextInputBoxState();
    updateBoxCollapsedPaddingTop();
    adjustFilledEditTextPaddingForLargeFont();
    if (this.boxBackgroundMode != 0)
      updateInputLayoutMargins(); 
  }
  
  private void openCutout() {
    if (!cutoutEnabled())
      return; 
    RectF rectF = this.tmpRectF;
    this.collapsingTextHelper.getCollapsedTextActualBounds(rectF, this.editText.getWidth(), this.editText.getGravity());
    applyCutoutPadding(rectF);
    this.boxLabelCutoutHeight = this.boxStrokeWidthPx;
    rectF.top = 0.0F;
    rectF.bottom = this.boxLabelCutoutHeight;
    rectF.offset(-getPaddingLeft(), 0.0F);
    ((CutoutDrawable)this.boxBackground).setCutout(rectF);
  }
  
  private static void recursiveSetEnabled(ViewGroup paramViewGroup, boolean paramBoolean) {
    byte b = 0;
    int i = paramViewGroup.getChildCount();
    while (b < i) {
      View view = paramViewGroup.getChildAt(b);
      view.setEnabled(paramBoolean);
      if (view instanceof ViewGroup)
        recursiveSetEnabled((ViewGroup)view, paramBoolean); 
      b++;
    } 
  }
  
  private void refreshIconDrawableState(CheckableImageButton paramCheckableImageButton, ColorStateList paramColorStateList) {
    Drawable drawable2 = paramCheckableImageButton.getDrawable();
    if (paramCheckableImageButton.getDrawable() == null || paramColorStateList == null || !paramColorStateList.isStateful())
      return; 
    int i = paramColorStateList.getColorForState(mergeIconState(paramCheckableImageButton), paramColorStateList.getDefaultColor());
    Drawable drawable1 = DrawableCompat.wrap(drawable2).mutate();
    DrawableCompat.setTintList(drawable1, ColorStateList.valueOf(i));
    paramCheckableImageButton.setImageDrawable(drawable1);
  }
  
  private void removePlaceholderTextView() {
    TextView textView = this.placeholderTextView;
    if (textView != null)
      textView.setVisibility(8); 
  }
  
  private void setEditText(EditText paramEditText) {
    if (this.editText == null) {
      if (this.endIconMode != 3 && !(paramEditText instanceof TextInputEditText))
        Log.i("TextInputLayout", "EditText added is not a TextInputEditText. Please switch to using that class instead."); 
      this.editText = paramEditText;
      setMinWidth(this.minWidth);
      setMaxWidth(this.maxWidth);
      onApplyBoxBackgroundMode();
      setTextInputAccessibilityDelegate(new AccessibilityDelegate(this));
      this.collapsingTextHelper.setTypefaces(this.editText.getTypeface());
      this.collapsingTextHelper.setExpandedTextSize(this.editText.getTextSize());
      int i = this.editText.getGravity();
      this.collapsingTextHelper.setCollapsedTextGravity(i & 0xFFFFFF8F | 0x30);
      this.collapsingTextHelper.setExpandedTextGravity(i);
      this.editText.addTextChangedListener(new TextWatcher() {
            final TextInputLayout this$0;
            
            public void afterTextChanged(Editable param1Editable) {
              TextInputLayout textInputLayout = TextInputLayout.this;
              textInputLayout.updateLabelState(textInputLayout.restoringSavedState ^ true);
              if (TextInputLayout.this.counterEnabled)
                TextInputLayout.this.updateCounter(param1Editable.length()); 
              if (TextInputLayout.this.placeholderEnabled)
                TextInputLayout.this.updatePlaceholderText(param1Editable.length()); 
            }
            
            public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
            
            public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
          });
      if (this.defaultHintTextColor == null)
        this.defaultHintTextColor = this.editText.getHintTextColors(); 
      if (this.hintEnabled) {
        if (TextUtils.isEmpty(this.hint)) {
          CharSequence charSequence = this.editText.getHint();
          this.originalHint = charSequence;
          setHint(charSequence);
          this.editText.setHint(null);
        } 
        this.isProvidingHint = true;
      } 
      if (this.counterView != null)
        updateCounter(this.editText.getText().length()); 
      updateEditTextBackground();
      this.indicatorViewController.adjustIndicatorPadding();
      this.startLayout.bringToFront();
      this.endLayout.bringToFront();
      this.endIconFrame.bringToFront();
      this.errorIconView.bringToFront();
      dispatchOnEditTextAttached();
      updatePrefixTextViewPadding();
      updateSuffixTextViewPadding();
      if (!isEnabled())
        paramEditText.setEnabled(false); 
      updateLabelState(false, true);
      return;
    } 
    throw new IllegalArgumentException("We already have an EditText, can only have one");
  }
  
  private void setEditTextBoxBackground() {
    if (shouldUseEditTextBackgroundForBoxBackground())
      ViewCompat.setBackground((View)this.editText, (Drawable)this.boxBackground); 
  }
  
  private void setErrorIconVisible(boolean paramBoolean) {
    CheckableImageButton checkableImageButton = this.errorIconView;
    boolean bool = false;
    if (paramBoolean) {
      b = 0;
    } else {
      b = 8;
    } 
    checkableImageButton.setVisibility(b);
    FrameLayout frameLayout = this.endIconFrame;
    byte b = bool;
    if (paramBoolean)
      b = 8; 
    frameLayout.setVisibility(b);
    updateSuffixTextViewPadding();
    if (!hasEndIcon())
      updateDummyDrawables(); 
  }
  
  private void setHintInternal(CharSequence paramCharSequence) {
    if (!TextUtils.equals(paramCharSequence, this.hint)) {
      this.hint = paramCharSequence;
      this.collapsingTextHelper.setText(paramCharSequence);
      if (!this.hintExpanded)
        openCutout(); 
    } 
  }
  
  private static void setIconClickable(CheckableImageButton paramCheckableImageButton, View.OnLongClickListener paramOnLongClickListener) {
    boolean bool1;
    boolean bool = ViewCompat.hasOnClickListeners((View)paramCheckableImageButton);
    boolean bool2 = false;
    byte b = 1;
    if (paramOnLongClickListener != null) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (bool || bool1)
      bool2 = true; 
    paramCheckableImageButton.setFocusable(bool2);
    paramCheckableImageButton.setClickable(bool);
    paramCheckableImageButton.setPressable(bool);
    paramCheckableImageButton.setLongClickable(bool1);
    if (!bool2)
      b = 2; 
    ViewCompat.setImportantForAccessibility((View)paramCheckableImageButton, b);
  }
  
  private static void setIconOnClickListener(CheckableImageButton paramCheckableImageButton, View.OnClickListener paramOnClickListener, View.OnLongClickListener paramOnLongClickListener) {
    paramCheckableImageButton.setOnClickListener(paramOnClickListener);
    setIconClickable(paramCheckableImageButton, paramOnLongClickListener);
  }
  
  private static void setIconOnLongClickListener(CheckableImageButton paramCheckableImageButton, View.OnLongClickListener paramOnLongClickListener) {
    paramCheckableImageButton.setOnLongClickListener(paramOnLongClickListener);
    setIconClickable(paramCheckableImageButton, paramOnLongClickListener);
  }
  
  private void setPlaceholderTextEnabled(boolean paramBoolean) {
    if (this.placeholderEnabled == paramBoolean)
      return; 
    if (paramBoolean) {
      AppCompatTextView appCompatTextView = new AppCompatTextView(getContext());
      this.placeholderTextView = (TextView)appCompatTextView;
      appCompatTextView.setId(R.id.textinput_placeholder);
      ViewCompat.setAccessibilityLiveRegion((View)this.placeholderTextView, 1);
      setPlaceholderTextAppearance(this.placeholderTextAppearance);
      setPlaceholderTextColor(this.placeholderTextColor);
      addPlaceholderTextView();
    } else {
      removePlaceholderTextView();
      this.placeholderTextView = null;
    } 
    this.placeholderEnabled = paramBoolean;
  }
  
  private boolean shouldUpdateEndDummyDrawable() {
    boolean bool;
    if ((this.errorIconView.getVisibility() == 0 || (hasEndIcon() && isEndIconVisible()) || this.suffixText != null) && this.endLayout.getMeasuredWidth() > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean shouldUpdateStartDummyDrawable() {
    boolean bool;
    if ((getStartIconDrawable() != null || this.prefixText != null) && this.startLayout.getMeasuredWidth() > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean shouldUseEditTextBackgroundForBoxBackground() {
    boolean bool;
    EditText editText = this.editText;
    if (editText != null && this.boxBackground != null && editText.getBackground() == null && this.boxBackgroundMode != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void showPlaceholderText() {
    TextView textView = this.placeholderTextView;
    if (textView != null && this.placeholderEnabled) {
      textView.setText(this.placeholderText);
      this.placeholderTextView.setVisibility(0);
      this.placeholderTextView.bringToFront();
    } 
  }
  
  private void tintEndIconOnError(boolean paramBoolean) {
    if (paramBoolean && getEndIconDrawable() != null) {
      Drawable drawable = DrawableCompat.wrap(getEndIconDrawable()).mutate();
      DrawableCompat.setTint(drawable, this.indicatorViewController.getErrorViewCurrentTextColor());
      this.endIconView.setImageDrawable(drawable);
    } else {
      applyEndIconTint();
    } 
  }
  
  private void updateBoxCollapsedPaddingTop() {
    if (this.boxBackgroundMode == 1)
      if (MaterialResources.isFontScaleAtLeast2_0(getContext())) {
        this.boxCollapsedPaddingTopPx = getResources().getDimensionPixelSize(R.dimen.material_font_2_0_box_collapsed_padding_top);
      } else if (MaterialResources.isFontScaleAtLeast1_3(getContext())) {
        this.boxCollapsedPaddingTopPx = getResources().getDimensionPixelSize(R.dimen.material_font_1_3_box_collapsed_padding_top);
      }  
  }
  
  private void updateBoxUnderlineBounds(Rect paramRect) {
    if (this.boxUnderline != null) {
      int j = paramRect.bottom;
      int i = this.boxStrokeWidthFocusedPx;
      this.boxUnderline.setBounds(paramRect.left, j - i, paramRect.right, paramRect.bottom);
    } 
  }
  
  private void updateCounter() {
    if (this.counterView != null) {
      int i;
      EditText editText = this.editText;
      if (editText == null) {
        i = 0;
      } else {
        i = editText.getText().length();
      } 
      updateCounter(i);
    } 
  }
  
  private static void updateCounterContentDescription(Context paramContext, TextView paramTextView, int paramInt1, int paramInt2, boolean paramBoolean) {
    int i;
    if (paramBoolean) {
      i = R.string.character_counter_overflowed_content_description;
    } else {
      i = R.string.character_counter_content_description;
    } 
    paramTextView.setContentDescription(paramContext.getString(i, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) }));
  }
  
  private void updateCounterTextAppearanceAndColor() {
    TextView textView = this.counterView;
    if (textView != null) {
      int i;
      if (this.counterOverflowed) {
        i = this.counterOverflowTextAppearance;
      } else {
        i = this.counterTextAppearance;
      } 
      setTextAppearanceCompatWithErrorFallback(textView, i);
      if (!this.counterOverflowed) {
        ColorStateList colorStateList = this.counterTextColor;
        if (colorStateList != null)
          this.counterView.setTextColor(colorStateList); 
      } 
      if (this.counterOverflowed) {
        ColorStateList colorStateList = this.counterOverflowTextColor;
        if (colorStateList != null)
          this.counterView.setTextColor(colorStateList); 
      } 
    } 
  }
  
  private void updateCutout() {
    if (cutoutEnabled() && !this.hintExpanded && this.boxLabelCutoutHeight != this.boxStrokeWidthPx) {
      closeCutout();
      openCutout();
    } 
  }
  
  private boolean updateDummyDrawables() {
    boolean bool1;
    if (this.editText == null)
      return false; 
    boolean bool2 = false;
    if (shouldUpdateStartDummyDrawable()) {
      int i = this.startLayout.getMeasuredWidth() - this.editText.getPaddingLeft();
      if (this.startDummyDrawable == null || this.startDummyDrawableWidth != i) {
        ColorDrawable colorDrawable = new ColorDrawable();
        this.startDummyDrawable = (Drawable)colorDrawable;
        this.startDummyDrawableWidth = i;
        colorDrawable.setBounds(0, 0, i, 1);
      } 
      Drawable[] arrayOfDrawable = TextViewCompat.getCompoundDrawablesRelative((TextView)this.editText);
      Drawable drawable2 = arrayOfDrawable[0];
      Drawable drawable1 = this.startDummyDrawable;
      if (drawable2 != drawable1) {
        TextViewCompat.setCompoundDrawablesRelative((TextView)this.editText, drawable1, arrayOfDrawable[1], arrayOfDrawable[2], arrayOfDrawable[3]);
        bool2 = true;
      } 
    } else if (this.startDummyDrawable != null) {
      Drawable[] arrayOfDrawable = TextViewCompat.getCompoundDrawablesRelative((TextView)this.editText);
      TextViewCompat.setCompoundDrawablesRelative((TextView)this.editText, null, arrayOfDrawable[1], arrayOfDrawable[2], arrayOfDrawable[3]);
      this.startDummyDrawable = null;
      bool2 = true;
    } 
    if (shouldUpdateEndDummyDrawable()) {
      int j = this.suffixTextView.getMeasuredWidth() - this.editText.getPaddingRight();
      CheckableImageButton checkableImageButton = getEndIconToUpdateDummyDrawable();
      int i = j;
      if (checkableImageButton != null)
        i = checkableImageButton.getMeasuredWidth() + j + MarginLayoutParamsCompat.getMarginStart((ViewGroup.MarginLayoutParams)checkableImageButton.getLayoutParams()); 
      Drawable[] arrayOfDrawable = TextViewCompat.getCompoundDrawablesRelative((TextView)this.editText);
      Drawable drawable = this.endDummyDrawable;
      if (drawable != null && this.endDummyDrawableWidth != i) {
        this.endDummyDrawableWidth = i;
        drawable.setBounds(0, 0, i, 1);
        TextViewCompat.setCompoundDrawablesRelative((TextView)this.editText, arrayOfDrawable[0], arrayOfDrawable[1], this.endDummyDrawable, arrayOfDrawable[3]);
        bool1 = true;
      } else {
        if (drawable == null) {
          ColorDrawable colorDrawable = new ColorDrawable();
          this.endDummyDrawable = (Drawable)colorDrawable;
          this.endDummyDrawableWidth = i;
          colorDrawable.setBounds(0, 0, i, 1);
        } 
        drawable = arrayOfDrawable[2];
        Drawable drawable1 = this.endDummyDrawable;
        bool1 = bool2;
        if (drawable != drawable1) {
          this.originalEditTextEndDrawable = arrayOfDrawable[2];
          TextViewCompat.setCompoundDrawablesRelative((TextView)this.editText, arrayOfDrawable[0], arrayOfDrawable[1], drawable1, arrayOfDrawable[3]);
          bool1 = true;
        } 
      } 
    } else {
      bool1 = bool2;
      if (this.endDummyDrawable != null) {
        Drawable[] arrayOfDrawable = TextViewCompat.getCompoundDrawablesRelative((TextView)this.editText);
        if (arrayOfDrawable[2] == this.endDummyDrawable) {
          TextViewCompat.setCompoundDrawablesRelative((TextView)this.editText, arrayOfDrawable[0], arrayOfDrawable[1], this.originalEditTextEndDrawable, arrayOfDrawable[3]);
          bool2 = true;
        } 
        this.endDummyDrawable = null;
        bool1 = bool2;
      } 
    } 
    return bool1;
  }
  
  private boolean updateEditTextHeightBasedOnIcon() {
    if (this.editText == null)
      return false; 
    int i = Math.max(this.endLayout.getMeasuredHeight(), this.startLayout.getMeasuredHeight());
    if (this.editText.getMeasuredHeight() < i) {
      this.editText.setMinimumHeight(i);
      return true;
    } 
    return false;
  }
  
  private void updateInputLayoutMargins() {
    if (this.boxBackgroundMode != 1) {
      LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)this.inputFrame.getLayoutParams();
      int i = calculateLabelMarginTop();
      if (i != layoutParams.topMargin) {
        layoutParams.topMargin = i;
        this.inputFrame.requestLayout();
      } 
    } 
  }
  
  private void updateLabelState(boolean paramBoolean1, boolean paramBoolean2) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual isEnabled : ()Z
    //   4: istore #6
    //   6: aload_0
    //   7: getfield editText : Landroid/widget/EditText;
    //   10: astore #8
    //   12: aload #8
    //   14: ifnull -> 33
    //   17: aload #8
    //   19: invokevirtual getText : ()Landroid/text/Editable;
    //   22: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   25: ifne -> 33
    //   28: iconst_1
    //   29: istore_3
    //   30: goto -> 35
    //   33: iconst_0
    //   34: istore_3
    //   35: aload_0
    //   36: getfield editText : Landroid/widget/EditText;
    //   39: astore #8
    //   41: aload #8
    //   43: ifnull -> 60
    //   46: aload #8
    //   48: invokevirtual hasFocus : ()Z
    //   51: ifeq -> 60
    //   54: iconst_1
    //   55: istore #4
    //   57: goto -> 63
    //   60: iconst_0
    //   61: istore #4
    //   63: aload_0
    //   64: getfield indicatorViewController : Lcom/google/android/material/textfield/IndicatorViewController;
    //   67: invokevirtual errorShouldBeShown : ()Z
    //   70: istore #7
    //   72: aload_0
    //   73: getfield defaultHintTextColor : Landroid/content/res/ColorStateList;
    //   76: astore #8
    //   78: aload #8
    //   80: ifnull -> 103
    //   83: aload_0
    //   84: getfield collapsingTextHelper : Lcom/google/android/material/internal/CollapsingTextHelper;
    //   87: aload #8
    //   89: invokevirtual setCollapsedTextColor : (Landroid/content/res/ColorStateList;)V
    //   92: aload_0
    //   93: getfield collapsingTextHelper : Lcom/google/android/material/internal/CollapsingTextHelper;
    //   96: aload_0
    //   97: getfield defaultHintTextColor : Landroid/content/res/ColorStateList;
    //   100: invokevirtual setExpandedTextColor : (Landroid/content/res/ColorStateList;)V
    //   103: iload #6
    //   105: ifne -> 179
    //   108: aload_0
    //   109: getfield defaultHintTextColor : Landroid/content/res/ColorStateList;
    //   112: astore #8
    //   114: aload #8
    //   116: ifnull -> 146
    //   119: aload_0
    //   120: getfield disabledColor : I
    //   123: istore #5
    //   125: aload #8
    //   127: iconst_1
    //   128: newarray int
    //   130: dup
    //   131: iconst_0
    //   132: ldc_w -16842910
    //   135: iastore
    //   136: iload #5
    //   138: invokevirtual getColorForState : ([II)I
    //   141: istore #5
    //   143: goto -> 152
    //   146: aload_0
    //   147: getfield disabledColor : I
    //   150: istore #5
    //   152: aload_0
    //   153: getfield collapsingTextHelper : Lcom/google/android/material/internal/CollapsingTextHelper;
    //   156: iload #5
    //   158: invokestatic valueOf : (I)Landroid/content/res/ColorStateList;
    //   161: invokevirtual setCollapsedTextColor : (Landroid/content/res/ColorStateList;)V
    //   164: aload_0
    //   165: getfield collapsingTextHelper : Lcom/google/android/material/internal/CollapsingTextHelper;
    //   168: iload #5
    //   170: invokestatic valueOf : (I)Landroid/content/res/ColorStateList;
    //   173: invokevirtual setExpandedTextColor : (Landroid/content/res/ColorStateList;)V
    //   176: goto -> 259
    //   179: iload #7
    //   181: ifeq -> 201
    //   184: aload_0
    //   185: getfield collapsingTextHelper : Lcom/google/android/material/internal/CollapsingTextHelper;
    //   188: aload_0
    //   189: getfield indicatorViewController : Lcom/google/android/material/textfield/IndicatorViewController;
    //   192: invokevirtual getErrorViewTextColors : ()Landroid/content/res/ColorStateList;
    //   195: invokevirtual setCollapsedTextColor : (Landroid/content/res/ColorStateList;)V
    //   198: goto -> 259
    //   201: aload_0
    //   202: getfield counterOverflowed : Z
    //   205: ifeq -> 234
    //   208: aload_0
    //   209: getfield counterView : Landroid/widget/TextView;
    //   212: astore #8
    //   214: aload #8
    //   216: ifnull -> 234
    //   219: aload_0
    //   220: getfield collapsingTextHelper : Lcom/google/android/material/internal/CollapsingTextHelper;
    //   223: aload #8
    //   225: invokevirtual getTextColors : ()Landroid/content/res/ColorStateList;
    //   228: invokevirtual setCollapsedTextColor : (Landroid/content/res/ColorStateList;)V
    //   231: goto -> 259
    //   234: iload #4
    //   236: ifeq -> 259
    //   239: aload_0
    //   240: getfield focusedTextColor : Landroid/content/res/ColorStateList;
    //   243: astore #8
    //   245: aload #8
    //   247: ifnull -> 259
    //   250: aload_0
    //   251: getfield collapsingTextHelper : Lcom/google/android/material/internal/CollapsingTextHelper;
    //   254: aload #8
    //   256: invokevirtual setCollapsedTextColor : (Landroid/content/res/ColorStateList;)V
    //   259: iload_3
    //   260: ifne -> 304
    //   263: aload_0
    //   264: getfield expandedHintEnabled : Z
    //   267: ifeq -> 304
    //   270: aload_0
    //   271: invokevirtual isEnabled : ()Z
    //   274: ifeq -> 285
    //   277: iload #4
    //   279: ifeq -> 285
    //   282: goto -> 304
    //   285: iload_2
    //   286: ifne -> 296
    //   289: aload_0
    //   290: getfield hintExpanded : Z
    //   293: ifne -> 320
    //   296: aload_0
    //   297: iload_1
    //   298: invokespecial expandHint : (Z)V
    //   301: goto -> 320
    //   304: iload_2
    //   305: ifne -> 315
    //   308: aload_0
    //   309: getfield hintExpanded : Z
    //   312: ifeq -> 320
    //   315: aload_0
    //   316: iload_1
    //   317: invokespecial collapseHint : (Z)V
    //   320: return
  }
  
  private void updatePlaceholderMeasurementsBasedOnEditText() {
    if (this.placeholderTextView != null) {
      EditText editText = this.editText;
      if (editText != null) {
        int i = editText.getGravity();
        this.placeholderTextView.setGravity(i);
        this.placeholderTextView.setPadding(this.editText.getCompoundPaddingLeft(), this.editText.getCompoundPaddingTop(), this.editText.getCompoundPaddingRight(), this.editText.getCompoundPaddingBottom());
      } 
    } 
  }
  
  private void updatePlaceholderText() {
    int i;
    EditText editText = this.editText;
    if (editText == null) {
      i = 0;
    } else {
      i = editText.getText().length();
    } 
    updatePlaceholderText(i);
  }
  
  private void updatePlaceholderText(int paramInt) {
    if (paramInt == 0 && !this.hintExpanded) {
      showPlaceholderText();
    } else {
      hidePlaceholderText();
    } 
  }
  
  private void updatePrefixTextViewPadding() {
    int i;
    if (this.editText == null)
      return; 
    if (isStartIconVisible()) {
      i = 0;
    } else {
      i = ViewCompat.getPaddingStart((View)this.editText);
    } 
    ViewCompat.setPaddingRelative((View)this.prefixTextView, i, this.editText.getCompoundPaddingTop(), getContext().getResources().getDimensionPixelSize(R.dimen.material_input_text_to_prefix_suffix_padding), this.editText.getCompoundPaddingBottom());
  }
  
  private void updatePrefixTextVisibility() {
    byte b;
    TextView textView = this.prefixTextView;
    if (this.prefixText != null && !isHintExpanded()) {
      b = 0;
    } else {
      b = 8;
    } 
    textView.setVisibility(b);
    updateDummyDrawables();
  }
  
  private void updateStrokeErrorColor(boolean paramBoolean1, boolean paramBoolean2) {
    int i = this.strokeErrorColor.getDefaultColor();
    int j = this.strokeErrorColor.getColorForState(new int[] { 16843623, 16842910 }, i);
    int k = this.strokeErrorColor.getColorForState(new int[] { 16843518, 16842910 }, i);
    if (paramBoolean1) {
      this.boxStrokeColor = k;
    } else if (paramBoolean2) {
      this.boxStrokeColor = j;
    } else {
      this.boxStrokeColor = i;
    } 
  }
  
  private void updateSuffixTextViewPadding() {
    int i;
    if (this.editText == null)
      return; 
    if (isEndIconVisible() || isErrorIconVisible()) {
      i = 0;
    } else {
      i = ViewCompat.getPaddingEnd((View)this.editText);
    } 
    ViewCompat.setPaddingRelative((View)this.suffixTextView, getContext().getResources().getDimensionPixelSize(R.dimen.material_input_text_to_prefix_suffix_padding), this.editText.getPaddingTop(), i, this.editText.getPaddingBottom());
  }
  
  private void updateSuffixTextVisibility() {
    boolean bool;
    int i = this.suffixTextView.getVisibility();
    CharSequence charSequence = this.suffixText;
    byte b = 0;
    if (charSequence != null && !isHintExpanded()) {
      bool = true;
    } else {
      bool = false;
    } 
    TextView textView = this.suffixTextView;
    if (!bool)
      b = 8; 
    textView.setVisibility(b);
    if (i != this.suffixTextView.getVisibility())
      getEndIconDelegate().onSuffixVisibilityChanged(bool); 
    updateDummyDrawables();
  }
  
  public void addOnEditTextAttachedListener(OnEditTextAttachedListener paramOnEditTextAttachedListener) {
    this.editTextAttachedListeners.add(paramOnEditTextAttachedListener);
    if (this.editText != null)
      paramOnEditTextAttachedListener.onEditTextAttached(this); 
  }
  
  public void addOnEndIconChangedListener(OnEndIconChangedListener paramOnEndIconChangedListener) {
    this.endIconChangedListeners.add(paramOnEndIconChangedListener);
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    if (paramView instanceof EditText) {
      FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(paramLayoutParams);
      layoutParams.gravity = layoutParams.gravity & 0xFFFFFF8F | 0x10;
      this.inputFrame.addView(paramView, (ViewGroup.LayoutParams)layoutParams);
      this.inputFrame.setLayoutParams(paramLayoutParams);
      updateInputLayoutMargins();
      setEditText((EditText)paramView);
    } else {
      super.addView(paramView, paramInt, paramLayoutParams);
    } 
  }
  
  void animateToExpansionFraction(float paramFloat) {
    if (this.collapsingTextHelper.getExpansionFraction() == paramFloat)
      return; 
    if (this.animator == null) {
      ValueAnimator valueAnimator = new ValueAnimator();
      this.animator = valueAnimator;
      valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
      this.animator.setDuration(167L);
      this.animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            final TextInputLayout this$0;
            
            public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
              TextInputLayout.this.collapsingTextHelper.setExpansionFraction(((Float)param1ValueAnimator.getAnimatedValue()).floatValue());
            }
          });
    } 
    this.animator.setFloatValues(new float[] { this.collapsingTextHelper.getExpansionFraction(), paramFloat });
    this.animator.start();
  }
  
  public void clearOnEditTextAttachedListeners() {
    this.editTextAttachedListeners.clear();
  }
  
  public void clearOnEndIconChangedListeners() {
    this.endIconChangedListeners.clear();
  }
  
  boolean cutoutIsOpen() {
    boolean bool;
    if (cutoutEnabled() && ((CutoutDrawable)this.boxBackground).hasCutout()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void dispatchProvideAutofillStructure(ViewStructure paramViewStructure, int paramInt) {
    EditText editText = this.editText;
    if (editText == null) {
      super.dispatchProvideAutofillStructure(paramViewStructure, paramInt);
      return;
    } 
    if (this.originalHint != null) {
      boolean bool = this.isProvidingHint;
      this.isProvidingHint = false;
      CharSequence charSequence = editText.getHint();
      this.editText.setHint(this.originalHint);
      try {
        super.dispatchProvideAutofillStructure(paramViewStructure, paramInt);
      } finally {
        this.editText.setHint(charSequence);
        this.isProvidingHint = bool;
      } 
    } else {
      paramViewStructure.setAutofillId(getAutofillId());
      onProvideAutofillStructure(paramViewStructure, paramInt);
      onProvideAutofillVirtualStructure(paramViewStructure, paramInt);
      paramViewStructure.setChildCount(this.inputFrame.getChildCount());
      for (byte b = 0; b < this.inputFrame.getChildCount(); b++) {
        View view = this.inputFrame.getChildAt(b);
        ViewStructure viewStructure = paramViewStructure.newChild(b);
        view.dispatchProvideAutofillStructure(viewStructure, paramInt);
        if (view == this.editText)
          viewStructure.setHint(getHint()); 
      } 
    } 
  }
  
  protected void dispatchRestoreInstanceState(SparseArray<Parcelable> paramSparseArray) {
    this.restoringSavedState = true;
    super.dispatchRestoreInstanceState(paramSparseArray);
    this.restoringSavedState = false;
  }
  
  public void draw(Canvas paramCanvas) {
    super.draw(paramCanvas);
    drawHint(paramCanvas);
    drawBoxUnderline(paramCanvas);
  }
  
  protected void drawableStateChanged() {
    if (this.inDrawableStateChanged)
      return; 
    boolean bool = true;
    this.inDrawableStateChanged = true;
    super.drawableStateChanged();
    int[] arrayOfInt = getDrawableState();
    int i = 0;
    CollapsingTextHelper collapsingTextHelper = this.collapsingTextHelper;
    if (collapsingTextHelper != null)
      i = false | collapsingTextHelper.setState(arrayOfInt); 
    if (this.editText != null) {
      if (!ViewCompat.isLaidOut((View)this) || !isEnabled())
        bool = false; 
      updateLabelState(bool);
    } 
    updateEditTextBackground();
    updateTextInputBoxState();
    if (i != 0)
      invalidate(); 
    this.inDrawableStateChanged = false;
  }
  
  public int getBaseline() {
    EditText editText = this.editText;
    return (editText != null) ? (editText.getBaseline() + getPaddingTop() + calculateLabelMarginTop()) : super.getBaseline();
  }
  
  MaterialShapeDrawable getBoxBackground() {
    int i = this.boxBackgroundMode;
    if (i == 1 || i == 2)
      return this.boxBackground; 
    throw new IllegalStateException();
  }
  
  public int getBoxBackgroundColor() {
    return this.boxBackgroundColor;
  }
  
  public int getBoxBackgroundMode() {
    return this.boxBackgroundMode;
  }
  
  public float getBoxCornerRadiusBottomEnd() {
    return this.boxBackground.getBottomLeftCornerResolvedSize();
  }
  
  public float getBoxCornerRadiusBottomStart() {
    return this.boxBackground.getBottomRightCornerResolvedSize();
  }
  
  public float getBoxCornerRadiusTopEnd() {
    return this.boxBackground.getTopRightCornerResolvedSize();
  }
  
  public float getBoxCornerRadiusTopStart() {
    return this.boxBackground.getTopLeftCornerResolvedSize();
  }
  
  public int getBoxStrokeColor() {
    return this.focusedStrokeColor;
  }
  
  public ColorStateList getBoxStrokeErrorColor() {
    return this.strokeErrorColor;
  }
  
  public int getBoxStrokeWidth() {
    return this.boxStrokeWidthDefaultPx;
  }
  
  public int getBoxStrokeWidthFocused() {
    return this.boxStrokeWidthFocusedPx;
  }
  
  public int getCounterMaxLength() {
    return this.counterMaxLength;
  }
  
  CharSequence getCounterOverflowDescription() {
    if (this.counterEnabled && this.counterOverflowed) {
      TextView textView = this.counterView;
      if (textView != null)
        return textView.getContentDescription(); 
    } 
    return null;
  }
  
  public ColorStateList getCounterOverflowTextColor() {
    return this.counterTextColor;
  }
  
  public ColorStateList getCounterTextColor() {
    return this.counterTextColor;
  }
  
  public ColorStateList getDefaultHintTextColor() {
    return this.defaultHintTextColor;
  }
  
  public EditText getEditText() {
    return this.editText;
  }
  
  public CharSequence getEndIconContentDescription() {
    return this.endIconView.getContentDescription();
  }
  
  public Drawable getEndIconDrawable() {
    return this.endIconView.getDrawable();
  }
  
  public int getEndIconMode() {
    return this.endIconMode;
  }
  
  CheckableImageButton getEndIconView() {
    return this.endIconView;
  }
  
  public CharSequence getError() {
    CharSequence charSequence;
    if (this.indicatorViewController.isErrorEnabled()) {
      charSequence = this.indicatorViewController.getErrorText();
    } else {
      charSequence = null;
    } 
    return charSequence;
  }
  
  public CharSequence getErrorContentDescription() {
    return this.indicatorViewController.getErrorContentDescription();
  }
  
  public int getErrorCurrentTextColors() {
    return this.indicatorViewController.getErrorViewCurrentTextColor();
  }
  
  public Drawable getErrorIconDrawable() {
    return this.errorIconView.getDrawable();
  }
  
  final int getErrorTextCurrentColor() {
    return this.indicatorViewController.getErrorViewCurrentTextColor();
  }
  
  public CharSequence getHelperText() {
    CharSequence charSequence;
    if (this.indicatorViewController.isHelperTextEnabled()) {
      charSequence = this.indicatorViewController.getHelperText();
    } else {
      charSequence = null;
    } 
    return charSequence;
  }
  
  public int getHelperTextCurrentTextColor() {
    return this.indicatorViewController.getHelperTextViewCurrentTextColor();
  }
  
  public CharSequence getHint() {
    CharSequence charSequence;
    if (this.hintEnabled) {
      charSequence = this.hint;
    } else {
      charSequence = null;
    } 
    return charSequence;
  }
  
  final float getHintCollapsedTextHeight() {
    return this.collapsingTextHelper.getCollapsedTextHeight();
  }
  
  final int getHintCurrentCollapsedTextColor() {
    return this.collapsingTextHelper.getCurrentCollapsedTextColor();
  }
  
  public ColorStateList getHintTextColor() {
    return this.focusedTextColor;
  }
  
  public int getMaxWidth() {
    return this.maxWidth;
  }
  
  public int getMinWidth() {
    return this.minWidth;
  }
  
  @Deprecated
  public CharSequence getPasswordVisibilityToggleContentDescription() {
    return this.endIconView.getContentDescription();
  }
  
  @Deprecated
  public Drawable getPasswordVisibilityToggleDrawable() {
    return this.endIconView.getDrawable();
  }
  
  public CharSequence getPlaceholderText() {
    CharSequence charSequence;
    if (this.placeholderEnabled) {
      charSequence = this.placeholderText;
    } else {
      charSequence = null;
    } 
    return charSequence;
  }
  
  public int getPlaceholderTextAppearance() {
    return this.placeholderTextAppearance;
  }
  
  public ColorStateList getPlaceholderTextColor() {
    return this.placeholderTextColor;
  }
  
  public CharSequence getPrefixText() {
    return this.prefixText;
  }
  
  public ColorStateList getPrefixTextColor() {
    return this.prefixTextView.getTextColors();
  }
  
  public TextView getPrefixTextView() {
    return this.prefixTextView;
  }
  
  public CharSequence getStartIconContentDescription() {
    return this.startIconView.getContentDescription();
  }
  
  public Drawable getStartIconDrawable() {
    return this.startIconView.getDrawable();
  }
  
  public CharSequence getSuffixText() {
    return this.suffixText;
  }
  
  public ColorStateList getSuffixTextColor() {
    return this.suffixTextView.getTextColors();
  }
  
  public TextView getSuffixTextView() {
    return this.suffixTextView;
  }
  
  public Typeface getTypeface() {
    return this.typeface;
  }
  
  public boolean isCounterEnabled() {
    return this.counterEnabled;
  }
  
  public boolean isEndIconCheckable() {
    return this.endIconView.isCheckable();
  }
  
  public boolean isEndIconVisible() {
    boolean bool;
    if (this.endIconFrame.getVisibility() == 0 && this.endIconView.getVisibility() == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isErrorEnabled() {
    return this.indicatorViewController.isErrorEnabled();
  }
  
  public boolean isExpandedHintEnabled() {
    return this.expandedHintEnabled;
  }
  
  final boolean isHelperTextDisplayed() {
    return this.indicatorViewController.helperTextIsDisplayed();
  }
  
  public boolean isHelperTextEnabled() {
    return this.indicatorViewController.isHelperTextEnabled();
  }
  
  public boolean isHintAnimationEnabled() {
    return this.hintAnimationEnabled;
  }
  
  public boolean isHintEnabled() {
    return this.hintEnabled;
  }
  
  final boolean isHintExpanded() {
    return this.hintExpanded;
  }
  
  @Deprecated
  public boolean isPasswordVisibilityToggleEnabled() {
    int i = this.endIconMode;
    boolean bool = true;
    if (i != 1)
      bool = false; 
    return bool;
  }
  
  public boolean isProvidingHint() {
    return this.isProvidingHint;
  }
  
  public boolean isStartIconCheckable() {
    return this.startIconView.isCheckable();
  }
  
  public boolean isStartIconVisible() {
    boolean bool;
    if (this.startIconView.getVisibility() == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    EditText editText = this.editText;
    if (editText != null) {
      Rect rect = this.tmpRect;
      DescendantOffsetUtils.getDescendantRect((ViewGroup)this, (View)editText, rect);
      updateBoxUnderlineBounds(rect);
      if (this.hintEnabled) {
        this.collapsingTextHelper.setExpandedTextSize(this.editText.getTextSize());
        paramInt1 = this.editText.getGravity();
        this.collapsingTextHelper.setCollapsedTextGravity(paramInt1 & 0xFFFFFF8F | 0x30);
        this.collapsingTextHelper.setExpandedTextGravity(paramInt1);
        this.collapsingTextHelper.setCollapsedBounds(calculateCollapsedTextBounds(rect));
        this.collapsingTextHelper.setExpandedBounds(calculateExpandedTextBounds(rect));
        this.collapsingTextHelper.recalculate();
        if (cutoutEnabled() && !this.hintExpanded)
          openCutout(); 
      } 
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    boolean bool2 = updateEditTextHeightBasedOnIcon();
    boolean bool1 = updateDummyDrawables();
    if (bool2 || bool1)
      this.editText.post(new Runnable() {
            final TextInputLayout this$0;
            
            public void run() {
              TextInputLayout.this.editText.requestLayout();
            }
          }); 
    updatePlaceholderMeasurementsBasedOnEditText();
    updatePrefixTextViewPadding();
    updateSuffixTextViewPadding();
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    setError(savedState.error);
    if (savedState.isEndIconChecked)
      this.endIconView.post(new Runnable() {
            final TextInputLayout this$0;
            
            public void run() {
              TextInputLayout.this.endIconView.performClick();
              TextInputLayout.this.endIconView.jumpDrawablesToCurrentState();
            }
          }); 
    setHint(savedState.hintText);
    setHelperText(savedState.helperText);
    setPlaceholderText(savedState.placeholderText);
    requestLayout();
  }
  
  public Parcelable onSaveInstanceState() {
    boolean bool;
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    if (this.indicatorViewController.errorShouldBeShown())
      savedState.error = getError(); 
    if (hasEndIcon() && this.endIconView.isChecked()) {
      bool = true;
    } else {
      bool = false;
    } 
    savedState.isEndIconChecked = bool;
    savedState.hintText = getHint();
    savedState.helperText = getHelperText();
    savedState.placeholderText = getPlaceholderText();
    return (Parcelable)savedState;
  }
  
  @Deprecated
  public void passwordVisibilityToggleRequested(boolean paramBoolean) {
    if (this.endIconMode == 1) {
      this.endIconView.performClick();
      if (paramBoolean)
        this.endIconView.jumpDrawablesToCurrentState(); 
    } 
  }
  
  public void refreshEndIconDrawableState() {
    refreshIconDrawableState(this.endIconView, this.endIconTintList);
  }
  
  public void refreshErrorIconDrawableState() {
    refreshIconDrawableState(this.errorIconView, this.errorIconTintList);
  }
  
  public void refreshStartIconDrawableState() {
    refreshIconDrawableState(this.startIconView, this.startIconTintList);
  }
  
  public void removeOnEditTextAttachedListener(OnEditTextAttachedListener paramOnEditTextAttachedListener) {
    this.editTextAttachedListeners.remove(paramOnEditTextAttachedListener);
  }
  
  public void removeOnEndIconChangedListener(OnEndIconChangedListener paramOnEndIconChangedListener) {
    this.endIconChangedListeners.remove(paramOnEndIconChangedListener);
  }
  
  public void setBoxBackgroundColor(int paramInt) {
    if (this.boxBackgroundColor != paramInt) {
      this.boxBackgroundColor = paramInt;
      this.defaultFilledBackgroundColor = paramInt;
      this.focusedFilledBackgroundColor = paramInt;
      this.hoveredFilledBackgroundColor = paramInt;
      applyBoxAttributes();
    } 
  }
  
  public void setBoxBackgroundColorResource(int paramInt) {
    setBoxBackgroundColor(ContextCompat.getColor(getContext(), paramInt));
  }
  
  public void setBoxBackgroundColorStateList(ColorStateList paramColorStateList) {
    int i = paramColorStateList.getDefaultColor();
    this.defaultFilledBackgroundColor = i;
    this.boxBackgroundColor = i;
    this.disabledFilledBackgroundColor = paramColorStateList.getColorForState(new int[] { -16842910 }, -1);
    this.focusedFilledBackgroundColor = paramColorStateList.getColorForState(new int[] { 16842908, 16842910 }, -1);
    this.hoveredFilledBackgroundColor = paramColorStateList.getColorForState(new int[] { 16843623, 16842910 }, -1);
    applyBoxAttributes();
  }
  
  public void setBoxBackgroundMode(int paramInt) {
    if (paramInt == this.boxBackgroundMode)
      return; 
    this.boxBackgroundMode = paramInt;
    if (this.editText != null)
      onApplyBoxBackgroundMode(); 
  }
  
  public void setBoxCornerRadii(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    MaterialShapeDrawable materialShapeDrawable = this.boxBackground;
    if (materialShapeDrawable == null || materialShapeDrawable.getTopLeftCornerResolvedSize() != paramFloat1 || this.boxBackground.getTopRightCornerResolvedSize() != paramFloat2 || this.boxBackground.getBottomRightCornerResolvedSize() != paramFloat4 || this.boxBackground.getBottomLeftCornerResolvedSize() != paramFloat3) {
      this.shapeAppearanceModel = this.shapeAppearanceModel.toBuilder().setTopLeftCornerSize(paramFloat1).setTopRightCornerSize(paramFloat2).setBottomRightCornerSize(paramFloat4).setBottomLeftCornerSize(paramFloat3).build();
      applyBoxAttributes();
    } 
  }
  
  public void setBoxCornerRadiiResources(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    setBoxCornerRadii(getContext().getResources().getDimension(paramInt1), getContext().getResources().getDimension(paramInt2), getContext().getResources().getDimension(paramInt4), getContext().getResources().getDimension(paramInt3));
  }
  
  public void setBoxStrokeColor(int paramInt) {
    if (this.focusedStrokeColor != paramInt) {
      this.focusedStrokeColor = paramInt;
      updateTextInputBoxState();
    } 
  }
  
  public void setBoxStrokeColorStateList(ColorStateList paramColorStateList) {
    if (paramColorStateList.isStateful()) {
      this.defaultStrokeColor = paramColorStateList.getDefaultColor();
      this.disabledColor = paramColorStateList.getColorForState(new int[] { -16842910 }, -1);
      this.hoveredStrokeColor = paramColorStateList.getColorForState(new int[] { 16843623, 16842910 }, -1);
      this.focusedStrokeColor = paramColorStateList.getColorForState(new int[] { 16842908, 16842910 }, -1);
    } else if (this.focusedStrokeColor != paramColorStateList.getDefaultColor()) {
      this.focusedStrokeColor = paramColorStateList.getDefaultColor();
    } 
    updateTextInputBoxState();
  }
  
  public void setBoxStrokeErrorColor(ColorStateList paramColorStateList) {
    if (this.strokeErrorColor != paramColorStateList) {
      this.strokeErrorColor = paramColorStateList;
      updateTextInputBoxState();
    } 
  }
  
  public void setBoxStrokeWidth(int paramInt) {
    this.boxStrokeWidthDefaultPx = paramInt;
    updateTextInputBoxState();
  }
  
  public void setBoxStrokeWidthFocused(int paramInt) {
    this.boxStrokeWidthFocusedPx = paramInt;
    updateTextInputBoxState();
  }
  
  public void setBoxStrokeWidthFocusedResource(int paramInt) {
    setBoxStrokeWidthFocused(getResources().getDimensionPixelSize(paramInt));
  }
  
  public void setBoxStrokeWidthResource(int paramInt) {
    setBoxStrokeWidth(getResources().getDimensionPixelSize(paramInt));
  }
  
  public void setCounterEnabled(boolean paramBoolean) {
    if (this.counterEnabled != paramBoolean) {
      if (paramBoolean) {
        AppCompatTextView appCompatTextView = new AppCompatTextView(getContext());
        this.counterView = (TextView)appCompatTextView;
        appCompatTextView.setId(R.id.textinput_counter);
        Typeface typeface = this.typeface;
        if (typeface != null)
          this.counterView.setTypeface(typeface); 
        this.counterView.setMaxLines(1);
        this.indicatorViewController.addIndicator(this.counterView, 2);
        MarginLayoutParamsCompat.setMarginStart((ViewGroup.MarginLayoutParams)this.counterView.getLayoutParams(), getResources().getDimensionPixelOffset(R.dimen.mtrl_textinput_counter_margin_start));
        updateCounterTextAppearanceAndColor();
        updateCounter();
      } else {
        this.indicatorViewController.removeIndicator(this.counterView, 2);
        this.counterView = null;
      } 
      this.counterEnabled = paramBoolean;
    } 
  }
  
  public void setCounterMaxLength(int paramInt) {
    if (this.counterMaxLength != paramInt) {
      if (paramInt > 0) {
        this.counterMaxLength = paramInt;
      } else {
        this.counterMaxLength = -1;
      } 
      if (this.counterEnabled)
        updateCounter(); 
    } 
  }
  
  public void setCounterOverflowTextAppearance(int paramInt) {
    if (this.counterOverflowTextAppearance != paramInt) {
      this.counterOverflowTextAppearance = paramInt;
      updateCounterTextAppearanceAndColor();
    } 
  }
  
  public void setCounterOverflowTextColor(ColorStateList paramColorStateList) {
    if (this.counterOverflowTextColor != paramColorStateList) {
      this.counterOverflowTextColor = paramColorStateList;
      updateCounterTextAppearanceAndColor();
    } 
  }
  
  public void setCounterTextAppearance(int paramInt) {
    if (this.counterTextAppearance != paramInt) {
      this.counterTextAppearance = paramInt;
      updateCounterTextAppearanceAndColor();
    } 
  }
  
  public void setCounterTextColor(ColorStateList paramColorStateList) {
    if (this.counterTextColor != paramColorStateList) {
      this.counterTextColor = paramColorStateList;
      updateCounterTextAppearanceAndColor();
    } 
  }
  
  public void setDefaultHintTextColor(ColorStateList paramColorStateList) {
    this.defaultHintTextColor = paramColorStateList;
    this.focusedTextColor = paramColorStateList;
    if (this.editText != null)
      updateLabelState(false); 
  }
  
  public void setEnabled(boolean paramBoolean) {
    recursiveSetEnabled((ViewGroup)this, paramBoolean);
    super.setEnabled(paramBoolean);
  }
  
  public void setEndIconActivated(boolean paramBoolean) {
    this.endIconView.setActivated(paramBoolean);
  }
  
  public void setEndIconCheckable(boolean paramBoolean) {
    this.endIconView.setCheckable(paramBoolean);
  }
  
  public void setEndIconContentDescription(int paramInt) {
    CharSequence charSequence;
    if (paramInt != 0) {
      charSequence = getResources().getText(paramInt);
    } else {
      charSequence = null;
    } 
    setEndIconContentDescription(charSequence);
  }
  
  public void setEndIconContentDescription(CharSequence paramCharSequence) {
    if (getEndIconContentDescription() != paramCharSequence)
      this.endIconView.setContentDescription(paramCharSequence); 
  }
  
  public void setEndIconDrawable(int paramInt) {
    Drawable drawable;
    if (paramInt != 0) {
      drawable = AppCompatResources.getDrawable(getContext(), paramInt);
    } else {
      drawable = null;
    } 
    setEndIconDrawable(drawable);
  }
  
  public void setEndIconDrawable(Drawable paramDrawable) {
    this.endIconView.setImageDrawable(paramDrawable);
    refreshEndIconDrawableState();
  }
  
  public void setEndIconMode(int paramInt) {
    boolean bool;
    int i = this.endIconMode;
    this.endIconMode = paramInt;
    dispatchOnEndIconChanged(i);
    if (paramInt != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    setEndIconVisible(bool);
    if (getEndIconDelegate().isBoxBackgroundModeSupported(this.boxBackgroundMode)) {
      getEndIconDelegate().initialize();
      applyEndIconTint();
      return;
    } 
    throw new IllegalStateException("The current box background mode " + this.boxBackgroundMode + " is not supported by the end icon mode " + paramInt);
  }
  
  public void setEndIconOnClickListener(View.OnClickListener paramOnClickListener) {
    setIconOnClickListener(this.endIconView, paramOnClickListener, this.endIconOnLongClickListener);
  }
  
  public void setEndIconOnLongClickListener(View.OnLongClickListener paramOnLongClickListener) {
    this.endIconOnLongClickListener = paramOnLongClickListener;
    setIconOnLongClickListener(this.endIconView, paramOnLongClickListener);
  }
  
  public void setEndIconTintList(ColorStateList paramColorStateList) {
    if (this.endIconTintList != paramColorStateList) {
      this.endIconTintList = paramColorStateList;
      this.hasEndIconTintList = true;
      applyEndIconTint();
    } 
  }
  
  public void setEndIconTintMode(PorterDuff.Mode paramMode) {
    if (this.endIconTintMode != paramMode) {
      this.endIconTintMode = paramMode;
      this.hasEndIconTintMode = true;
      applyEndIconTint();
    } 
  }
  
  public void setEndIconVisible(boolean paramBoolean) {
    if (isEndIconVisible() != paramBoolean) {
      byte b;
      CheckableImageButton checkableImageButton = this.endIconView;
      if (paramBoolean) {
        b = 0;
      } else {
        b = 8;
      } 
      checkableImageButton.setVisibility(b);
      updateSuffixTextViewPadding();
      updateDummyDrawables();
    } 
  }
  
  public void setError(CharSequence paramCharSequence) {
    if (!this.indicatorViewController.isErrorEnabled()) {
      if (TextUtils.isEmpty(paramCharSequence))
        return; 
      setErrorEnabled(true);
    } 
    if (!TextUtils.isEmpty(paramCharSequence)) {
      this.indicatorViewController.showError(paramCharSequence);
    } else {
      this.indicatorViewController.hideError();
    } 
  }
  
  public void setErrorContentDescription(CharSequence paramCharSequence) {
    this.indicatorViewController.setErrorContentDescription(paramCharSequence);
  }
  
  public void setErrorEnabled(boolean paramBoolean) {
    this.indicatorViewController.setErrorEnabled(paramBoolean);
  }
  
  public void setErrorIconDrawable(int paramInt) {
    Drawable drawable;
    if (paramInt != 0) {
      drawable = AppCompatResources.getDrawable(getContext(), paramInt);
    } else {
      drawable = null;
    } 
    setErrorIconDrawable(drawable);
    refreshErrorIconDrawableState();
  }
  
  public void setErrorIconDrawable(Drawable paramDrawable) {
    boolean bool;
    this.errorIconView.setImageDrawable(paramDrawable);
    if (paramDrawable != null && this.indicatorViewController.isErrorEnabled()) {
      bool = true;
    } else {
      bool = false;
    } 
    setErrorIconVisible(bool);
  }
  
  public void setErrorIconOnClickListener(View.OnClickListener paramOnClickListener) {
    setIconOnClickListener(this.errorIconView, paramOnClickListener, this.errorIconOnLongClickListener);
  }
  
  public void setErrorIconOnLongClickListener(View.OnLongClickListener paramOnLongClickListener) {
    this.errorIconOnLongClickListener = paramOnLongClickListener;
    setIconOnLongClickListener(this.errorIconView, paramOnLongClickListener);
  }
  
  public void setErrorIconTintList(ColorStateList paramColorStateList) {
    this.errorIconTintList = paramColorStateList;
    Drawable drawable2 = this.errorIconView.getDrawable();
    Drawable drawable1 = drawable2;
    if (drawable2 != null) {
      drawable1 = DrawableCompat.wrap(drawable2).mutate();
      DrawableCompat.setTintList(drawable1, paramColorStateList);
    } 
    if (this.errorIconView.getDrawable() != drawable1)
      this.errorIconView.setImageDrawable(drawable1); 
  }
  
  public void setErrorIconTintMode(PorterDuff.Mode paramMode) {
    Drawable drawable2 = this.errorIconView.getDrawable();
    Drawable drawable1 = drawable2;
    if (drawable2 != null) {
      drawable1 = DrawableCompat.wrap(drawable2).mutate();
      DrawableCompat.setTintMode(drawable1, paramMode);
    } 
    if (this.errorIconView.getDrawable() != drawable1)
      this.errorIconView.setImageDrawable(drawable1); 
  }
  
  public void setErrorTextAppearance(int paramInt) {
    this.indicatorViewController.setErrorTextAppearance(paramInt);
  }
  
  public void setErrorTextColor(ColorStateList paramColorStateList) {
    this.indicatorViewController.setErrorViewTextColor(paramColorStateList);
  }
  
  public void setExpandedHintEnabled(boolean paramBoolean) {
    if (this.expandedHintEnabled != paramBoolean) {
      this.expandedHintEnabled = paramBoolean;
      updateLabelState(false);
    } 
  }
  
  public void setHelperText(CharSequence paramCharSequence) {
    if (TextUtils.isEmpty(paramCharSequence)) {
      if (isHelperTextEnabled())
        setHelperTextEnabled(false); 
    } else {
      if (!isHelperTextEnabled())
        setHelperTextEnabled(true); 
      this.indicatorViewController.showHelper(paramCharSequence);
    } 
  }
  
  public void setHelperTextColor(ColorStateList paramColorStateList) {
    this.indicatorViewController.setHelperTextViewTextColor(paramColorStateList);
  }
  
  public void setHelperTextEnabled(boolean paramBoolean) {
    this.indicatorViewController.setHelperTextEnabled(paramBoolean);
  }
  
  public void setHelperTextTextAppearance(int paramInt) {
    this.indicatorViewController.setHelperTextAppearance(paramInt);
  }
  
  public void setHint(int paramInt) {
    CharSequence charSequence;
    if (paramInt != 0) {
      charSequence = getResources().getText(paramInt);
    } else {
      charSequence = null;
    } 
    setHint(charSequence);
  }
  
  public void setHint(CharSequence paramCharSequence) {
    if (this.hintEnabled) {
      setHintInternal(paramCharSequence);
      sendAccessibilityEvent(2048);
    } 
  }
  
  public void setHintAnimationEnabled(boolean paramBoolean) {
    this.hintAnimationEnabled = paramBoolean;
  }
  
  public void setHintEnabled(boolean paramBoolean) {
    if (paramBoolean != this.hintEnabled) {
      this.hintEnabled = paramBoolean;
      if (!paramBoolean) {
        this.isProvidingHint = false;
        if (!TextUtils.isEmpty(this.hint) && TextUtils.isEmpty(this.editText.getHint()))
          this.editText.setHint(this.hint); 
        setHintInternal((CharSequence)null);
      } else {
        CharSequence charSequence = this.editText.getHint();
        if (!TextUtils.isEmpty(charSequence)) {
          if (TextUtils.isEmpty(this.hint))
            setHint(charSequence); 
          this.editText.setHint(null);
        } 
        this.isProvidingHint = true;
      } 
      if (this.editText != null)
        updateInputLayoutMargins(); 
    } 
  }
  
  public void setHintTextAppearance(int paramInt) {
    this.collapsingTextHelper.setCollapsedTextAppearance(paramInt);
    this.focusedTextColor = this.collapsingTextHelper.getCollapsedTextColor();
    if (this.editText != null) {
      updateLabelState(false);
      updateInputLayoutMargins();
    } 
  }
  
  public void setHintTextColor(ColorStateList paramColorStateList) {
    if (this.focusedTextColor != paramColorStateList) {
      if (this.defaultHintTextColor == null)
        this.collapsingTextHelper.setCollapsedTextColor(paramColorStateList); 
      this.focusedTextColor = paramColorStateList;
      if (this.editText != null)
        updateLabelState(false); 
    } 
  }
  
  public void setMaxWidth(int paramInt) {
    this.maxWidth = paramInt;
    EditText editText = this.editText;
    if (editText != null && paramInt != -1)
      editText.setMaxWidth(paramInt); 
  }
  
  public void setMaxWidthResource(int paramInt) {
    setMaxWidth(getContext().getResources().getDimensionPixelSize(paramInt));
  }
  
  public void setMinWidth(int paramInt) {
    this.minWidth = paramInt;
    EditText editText = this.editText;
    if (editText != null && paramInt != -1)
      editText.setMinWidth(paramInt); 
  }
  
  public void setMinWidthResource(int paramInt) {
    setMinWidth(getContext().getResources().getDimensionPixelSize(paramInt));
  }
  
  @Deprecated
  public void setPasswordVisibilityToggleContentDescription(int paramInt) {
    CharSequence charSequence;
    if (paramInt != 0) {
      charSequence = getResources().getText(paramInt);
    } else {
      charSequence = null;
    } 
    setPasswordVisibilityToggleContentDescription(charSequence);
  }
  
  @Deprecated
  public void setPasswordVisibilityToggleContentDescription(CharSequence paramCharSequence) {
    this.endIconView.setContentDescription(paramCharSequence);
  }
  
  @Deprecated
  public void setPasswordVisibilityToggleDrawable(int paramInt) {
    Drawable drawable;
    if (paramInt != 0) {
      drawable = AppCompatResources.getDrawable(getContext(), paramInt);
    } else {
      drawable = null;
    } 
    setPasswordVisibilityToggleDrawable(drawable);
  }
  
  @Deprecated
  public void setPasswordVisibilityToggleDrawable(Drawable paramDrawable) {
    this.endIconView.setImageDrawable(paramDrawable);
  }
  
  @Deprecated
  public void setPasswordVisibilityToggleEnabled(boolean paramBoolean) {
    if (paramBoolean && this.endIconMode != 1) {
      setEndIconMode(1);
    } else if (!paramBoolean) {
      setEndIconMode(0);
    } 
  }
  
  @Deprecated
  public void setPasswordVisibilityToggleTintList(ColorStateList paramColorStateList) {
    this.endIconTintList = paramColorStateList;
    this.hasEndIconTintList = true;
    applyEndIconTint();
  }
  
  @Deprecated
  public void setPasswordVisibilityToggleTintMode(PorterDuff.Mode paramMode) {
    this.endIconTintMode = paramMode;
    this.hasEndIconTintMode = true;
    applyEndIconTint();
  }
  
  public void setPlaceholderText(CharSequence paramCharSequence) {
    if (this.placeholderEnabled && TextUtils.isEmpty(paramCharSequence)) {
      setPlaceholderTextEnabled(false);
    } else {
      if (!this.placeholderEnabled)
        setPlaceholderTextEnabled(true); 
      this.placeholderText = paramCharSequence;
    } 
    updatePlaceholderText();
  }
  
  public void setPlaceholderTextAppearance(int paramInt) {
    this.placeholderTextAppearance = paramInt;
    TextView textView = this.placeholderTextView;
    if (textView != null)
      TextViewCompat.setTextAppearance(textView, paramInt); 
  }
  
  public void setPlaceholderTextColor(ColorStateList paramColorStateList) {
    if (this.placeholderTextColor != paramColorStateList) {
      this.placeholderTextColor = paramColorStateList;
      TextView textView = this.placeholderTextView;
      if (textView != null && paramColorStateList != null)
        textView.setTextColor(paramColorStateList); 
    } 
  }
  
  public void setPrefixText(CharSequence paramCharSequence) {
    CharSequence charSequence;
    if (TextUtils.isEmpty(paramCharSequence)) {
      charSequence = null;
    } else {
      charSequence = paramCharSequence;
    } 
    this.prefixText = charSequence;
    this.prefixTextView.setText(paramCharSequence);
    updatePrefixTextVisibility();
  }
  
  public void setPrefixTextAppearance(int paramInt) {
    TextViewCompat.setTextAppearance(this.prefixTextView, paramInt);
  }
  
  public void setPrefixTextColor(ColorStateList paramColorStateList) {
    this.prefixTextView.setTextColor(paramColorStateList);
  }
  
  public void setStartIconCheckable(boolean paramBoolean) {
    this.startIconView.setCheckable(paramBoolean);
  }
  
  public void setStartIconContentDescription(int paramInt) {
    CharSequence charSequence;
    if (paramInt != 0) {
      charSequence = getResources().getText(paramInt);
    } else {
      charSequence = null;
    } 
    setStartIconContentDescription(charSequence);
  }
  
  public void setStartIconContentDescription(CharSequence paramCharSequence) {
    if (getStartIconContentDescription() != paramCharSequence)
      this.startIconView.setContentDescription(paramCharSequence); 
  }
  
  public void setStartIconDrawable(int paramInt) {
    Drawable drawable;
    if (paramInt != 0) {
      drawable = AppCompatResources.getDrawable(getContext(), paramInt);
    } else {
      drawable = null;
    } 
    setStartIconDrawable(drawable);
  }
  
  public void setStartIconDrawable(Drawable paramDrawable) {
    this.startIconView.setImageDrawable(paramDrawable);
    if (paramDrawable != null) {
      setStartIconVisible(true);
      refreshStartIconDrawableState();
    } else {
      setStartIconVisible(false);
      setStartIconOnClickListener((View.OnClickListener)null);
      setStartIconOnLongClickListener((View.OnLongClickListener)null);
      setStartIconContentDescription((CharSequence)null);
    } 
  }
  
  public void setStartIconOnClickListener(View.OnClickListener paramOnClickListener) {
    setIconOnClickListener(this.startIconView, paramOnClickListener, this.startIconOnLongClickListener);
  }
  
  public void setStartIconOnLongClickListener(View.OnLongClickListener paramOnLongClickListener) {
    this.startIconOnLongClickListener = paramOnLongClickListener;
    setIconOnLongClickListener(this.startIconView, paramOnLongClickListener);
  }
  
  public void setStartIconTintList(ColorStateList paramColorStateList) {
    if (this.startIconTintList != paramColorStateList) {
      this.startIconTintList = paramColorStateList;
      this.hasStartIconTintList = true;
      applyStartIconTint();
    } 
  }
  
  public void setStartIconTintMode(PorterDuff.Mode paramMode) {
    if (this.startIconTintMode != paramMode) {
      this.startIconTintMode = paramMode;
      this.hasStartIconTintMode = true;
      applyStartIconTint();
    } 
  }
  
  public void setStartIconVisible(boolean paramBoolean) {
    if (isStartIconVisible() != paramBoolean) {
      byte b;
      CheckableImageButton checkableImageButton = this.startIconView;
      if (paramBoolean) {
        b = 0;
      } else {
        b = 8;
      } 
      checkableImageButton.setVisibility(b);
      updatePrefixTextViewPadding();
      updateDummyDrawables();
    } 
  }
  
  public void setSuffixText(CharSequence paramCharSequence) {
    CharSequence charSequence;
    if (TextUtils.isEmpty(paramCharSequence)) {
      charSequence = null;
    } else {
      charSequence = paramCharSequence;
    } 
    this.suffixText = charSequence;
    this.suffixTextView.setText(paramCharSequence);
    updateSuffixTextVisibility();
  }
  
  public void setSuffixTextAppearance(int paramInt) {
    TextViewCompat.setTextAppearance(this.suffixTextView, paramInt);
  }
  
  public void setSuffixTextColor(ColorStateList paramColorStateList) {
    this.suffixTextView.setTextColor(paramColorStateList);
  }
  
  void setTextAppearanceCompatWithErrorFallback(TextView paramTextView, int paramInt) {
    boolean bool = false;
    try {
      TextViewCompat.setTextAppearance(paramTextView, paramInt);
      paramInt = bool;
      if (Build.VERSION.SDK_INT >= 23) {
        int i = paramTextView.getTextColors().getDefaultColor();
        paramInt = bool;
        if (i == -65281)
          paramInt = 1; 
      } 
    } catch (Exception exception) {
      paramInt = 1;
    } 
    if (paramInt != 0) {
      TextViewCompat.setTextAppearance(paramTextView, R.style.TextAppearance_AppCompat_Caption);
      paramTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.design_error));
    } 
  }
  
  public void setTextInputAccessibilityDelegate(AccessibilityDelegate paramAccessibilityDelegate) {
    EditText editText = this.editText;
    if (editText != null)
      ViewCompat.setAccessibilityDelegate((View)editText, paramAccessibilityDelegate); 
  }
  
  public void setTypeface(Typeface paramTypeface) {
    if (paramTypeface != this.typeface) {
      this.typeface = paramTypeface;
      this.collapsingTextHelper.setTypefaces(paramTypeface);
      this.indicatorViewController.setTypefaces(paramTypeface);
      TextView textView = this.counterView;
      if (textView != null)
        textView.setTypeface(paramTypeface); 
    } 
  }
  
  void updateCounter(int paramInt) {
    boolean bool = this.counterOverflowed;
    int i = this.counterMaxLength;
    if (i == -1) {
      this.counterView.setText(String.valueOf(paramInt));
      this.counterView.setContentDescription(null);
      this.counterOverflowed = false;
    } else {
      boolean bool1;
      if (paramInt > i) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      this.counterOverflowed = bool1;
      updateCounterContentDescription(getContext(), this.counterView, paramInt, this.counterMaxLength, this.counterOverflowed);
      if (bool != this.counterOverflowed)
        updateCounterTextAppearanceAndColor(); 
      BidiFormatter bidiFormatter = BidiFormatter.getInstance();
      this.counterView.setText(bidiFormatter.unicodeWrap(getContext().getString(R.string.character_counter_pattern, new Object[] { Integer.valueOf(paramInt), Integer.valueOf(this.counterMaxLength) })));
    } 
    if (this.editText != null && bool != this.counterOverflowed) {
      updateLabelState(false);
      updateTextInputBoxState();
      updateEditTextBackground();
    } 
  }
  
  void updateEditTextBackground() {
    EditText editText = this.editText;
    if (editText == null || this.boxBackgroundMode != 0)
      return; 
    Drawable drawable2 = editText.getBackground();
    if (drawable2 == null)
      return; 
    Drawable drawable1 = drawable2;
    if (DrawableUtils.canSafelyMutateDrawable(drawable2))
      drawable1 = drawable2.mutate(); 
    if (this.indicatorViewController.errorShouldBeShown()) {
      drawable1.setColorFilter((ColorFilter)AppCompatDrawableManager.getPorterDuffColorFilter(this.indicatorViewController.getErrorViewCurrentTextColor(), PorterDuff.Mode.SRC_IN));
    } else {
      if (this.counterOverflowed) {
        TextView textView = this.counterView;
        if (textView != null) {
          drawable1.setColorFilter((ColorFilter)AppCompatDrawableManager.getPorterDuffColorFilter(textView.getCurrentTextColor(), PorterDuff.Mode.SRC_IN));
          return;
        } 
      } 
      DrawableCompat.clearColorFilter(drawable1);
      this.editText.refreshDrawableState();
    } 
  }
  
  void updateLabelState(boolean paramBoolean) {
    updateLabelState(paramBoolean, false);
  }
  
  void updateTextInputBoxState() {
    // Byte code:
    //   0: aload_0
    //   1: getfield boxBackground : Lcom/google/android/material/shape/MaterialShapeDrawable;
    //   4: ifnull -> 420
    //   7: aload_0
    //   8: getfield boxBackgroundMode : I
    //   11: ifne -> 17
    //   14: goto -> 420
    //   17: aload_0
    //   18: invokevirtual isFocused : ()Z
    //   21: istore_1
    //   22: iconst_0
    //   23: istore_3
    //   24: iload_1
    //   25: ifne -> 55
    //   28: aload_0
    //   29: getfield editText : Landroid/widget/EditText;
    //   32: astore #4
    //   34: aload #4
    //   36: ifnull -> 50
    //   39: aload #4
    //   41: invokevirtual hasFocus : ()Z
    //   44: ifeq -> 50
    //   47: goto -> 55
    //   50: iconst_0
    //   51: istore_1
    //   52: goto -> 57
    //   55: iconst_1
    //   56: istore_1
    //   57: aload_0
    //   58: invokevirtual isHovered : ()Z
    //   61: ifne -> 91
    //   64: aload_0
    //   65: getfield editText : Landroid/widget/EditText;
    //   68: astore #4
    //   70: aload #4
    //   72: ifnull -> 86
    //   75: aload #4
    //   77: invokevirtual isHovered : ()Z
    //   80: ifeq -> 86
    //   83: goto -> 91
    //   86: iconst_0
    //   87: istore_2
    //   88: goto -> 93
    //   91: iconst_1
    //   92: istore_2
    //   93: aload_0
    //   94: invokevirtual isEnabled : ()Z
    //   97: ifne -> 111
    //   100: aload_0
    //   101: aload_0
    //   102: getfield disabledColor : I
    //   105: putfield boxStrokeColor : I
    //   108: goto -> 235
    //   111: aload_0
    //   112: getfield indicatorViewController : Lcom/google/android/material/textfield/IndicatorViewController;
    //   115: invokevirtual errorShouldBeShown : ()Z
    //   118: ifeq -> 151
    //   121: aload_0
    //   122: getfield strokeErrorColor : Landroid/content/res/ColorStateList;
    //   125: ifnull -> 137
    //   128: aload_0
    //   129: iload_1
    //   130: iload_2
    //   131: invokespecial updateStrokeErrorColor : (ZZ)V
    //   134: goto -> 235
    //   137: aload_0
    //   138: aload_0
    //   139: getfield indicatorViewController : Lcom/google/android/material/textfield/IndicatorViewController;
    //   142: invokevirtual getErrorViewCurrentTextColor : ()I
    //   145: putfield boxStrokeColor : I
    //   148: goto -> 235
    //   151: aload_0
    //   152: getfield counterOverflowed : Z
    //   155: ifeq -> 197
    //   158: aload_0
    //   159: getfield counterView : Landroid/widget/TextView;
    //   162: astore #4
    //   164: aload #4
    //   166: ifnull -> 197
    //   169: aload_0
    //   170: getfield strokeErrorColor : Landroid/content/res/ColorStateList;
    //   173: ifnull -> 185
    //   176: aload_0
    //   177: iload_1
    //   178: iload_2
    //   179: invokespecial updateStrokeErrorColor : (ZZ)V
    //   182: goto -> 235
    //   185: aload_0
    //   186: aload #4
    //   188: invokevirtual getCurrentTextColor : ()I
    //   191: putfield boxStrokeColor : I
    //   194: goto -> 235
    //   197: iload_1
    //   198: ifeq -> 212
    //   201: aload_0
    //   202: aload_0
    //   203: getfield focusedStrokeColor : I
    //   206: putfield boxStrokeColor : I
    //   209: goto -> 235
    //   212: iload_2
    //   213: ifeq -> 227
    //   216: aload_0
    //   217: aload_0
    //   218: getfield hoveredStrokeColor : I
    //   221: putfield boxStrokeColor : I
    //   224: goto -> 235
    //   227: aload_0
    //   228: aload_0
    //   229: getfield defaultStrokeColor : I
    //   232: putfield boxStrokeColor : I
    //   235: aload_0
    //   236: invokevirtual getErrorIconDrawable : ()Landroid/graphics/drawable/Drawable;
    //   239: ifnull -> 267
    //   242: aload_0
    //   243: getfield indicatorViewController : Lcom/google/android/material/textfield/IndicatorViewController;
    //   246: invokevirtual isErrorEnabled : ()Z
    //   249: ifeq -> 267
    //   252: aload_0
    //   253: getfield indicatorViewController : Lcom/google/android/material/textfield/IndicatorViewController;
    //   256: invokevirtual errorShouldBeShown : ()Z
    //   259: ifeq -> 267
    //   262: iconst_1
    //   263: istore_3
    //   264: goto -> 267
    //   267: aload_0
    //   268: iload_3
    //   269: invokespecial setErrorIconVisible : (Z)V
    //   272: aload_0
    //   273: invokevirtual refreshErrorIconDrawableState : ()V
    //   276: aload_0
    //   277: invokevirtual refreshStartIconDrawableState : ()V
    //   280: aload_0
    //   281: invokevirtual refreshEndIconDrawableState : ()V
    //   284: aload_0
    //   285: invokespecial getEndIconDelegate : ()Lcom/google/android/material/textfield/EndIconDelegate;
    //   288: invokevirtual shouldTintIconOnError : ()Z
    //   291: ifeq -> 305
    //   294: aload_0
    //   295: aload_0
    //   296: getfield indicatorViewController : Lcom/google/android/material/textfield/IndicatorViewController;
    //   299: invokevirtual errorShouldBeShown : ()Z
    //   302: invokespecial tintEndIconOnError : (Z)V
    //   305: iload_1
    //   306: ifeq -> 327
    //   309: aload_0
    //   310: invokevirtual isEnabled : ()Z
    //   313: ifeq -> 327
    //   316: aload_0
    //   317: aload_0
    //   318: getfield boxStrokeWidthFocusedPx : I
    //   321: putfield boxStrokeWidthPx : I
    //   324: goto -> 335
    //   327: aload_0
    //   328: aload_0
    //   329: getfield boxStrokeWidthDefaultPx : I
    //   332: putfield boxStrokeWidthPx : I
    //   335: aload_0
    //   336: getfield boxBackgroundMode : I
    //   339: iconst_2
    //   340: if_icmpne -> 347
    //   343: aload_0
    //   344: invokespecial updateCutout : ()V
    //   347: aload_0
    //   348: getfield boxBackgroundMode : I
    //   351: iconst_1
    //   352: if_icmpne -> 415
    //   355: aload_0
    //   356: invokevirtual isEnabled : ()Z
    //   359: ifne -> 373
    //   362: aload_0
    //   363: aload_0
    //   364: getfield disabledFilledBackgroundColor : I
    //   367: putfield boxBackgroundColor : I
    //   370: goto -> 415
    //   373: iload_2
    //   374: ifeq -> 392
    //   377: iload_1
    //   378: ifne -> 392
    //   381: aload_0
    //   382: aload_0
    //   383: getfield hoveredFilledBackgroundColor : I
    //   386: putfield boxBackgroundColor : I
    //   389: goto -> 415
    //   392: iload_1
    //   393: ifeq -> 407
    //   396: aload_0
    //   397: aload_0
    //   398: getfield focusedFilledBackgroundColor : I
    //   401: putfield boxBackgroundColor : I
    //   404: goto -> 415
    //   407: aload_0
    //   408: aload_0
    //   409: getfield defaultFilledBackgroundColor : I
    //   412: putfield boxBackgroundColor : I
    //   415: aload_0
    //   416: invokespecial applyBoxAttributes : ()V
    //   419: return
    //   420: return
  }
  
  public static class AccessibilityDelegate extends AccessibilityDelegateCompat {
    private final TextInputLayout layout;
    
    public AccessibilityDelegate(TextInputLayout param1TextInputLayout) {
      this.layout = param1TextInputLayout;
    }
    
    public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      boolean bool;
      CharSequence charSequence2;
      super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
      EditText editText = this.layout.getEditText();
      if (editText != null) {
        charSequence2 = (CharSequence)editText.getText();
      } else {
        charSequence2 = null;
      } 
      CharSequence charSequence1 = this.layout.getHint();
      CharSequence charSequence3 = this.layout.getError();
      CharSequence charSequence5 = this.layout.getPlaceholderText();
      int i = this.layout.getCounterMaxLength();
      CharSequence charSequence4 = this.layout.getCounterOverflowDescription();
      int k = TextUtils.isEmpty(charSequence2) ^ true;
      boolean bool1 = TextUtils.isEmpty(charSequence1);
      boolean bool2 = this.layout.isHintExpanded();
      int j = TextUtils.isEmpty(charSequence3) ^ true;
      if (j != 0 || !TextUtils.isEmpty(charSequence4)) {
        bool = true;
      } else {
        bool = false;
      } 
      if ((bool1 ^ true) != 0) {
        charSequence1 = charSequence1.toString();
      } else {
        charSequence1 = "";
      } 
      if (k != 0) {
        param1AccessibilityNodeInfoCompat.setText(charSequence2);
      } else if (!TextUtils.isEmpty(charSequence1)) {
        param1AccessibilityNodeInfoCompat.setText(charSequence1);
        if ((bool2 ^ true) != 0 && charSequence5 != null)
          param1AccessibilityNodeInfoCompat.setText(charSequence1 + ", " + charSequence5); 
      } else if (charSequence5 != null) {
        param1AccessibilityNodeInfoCompat.setText(charSequence5);
      } 
      if (!TextUtils.isEmpty(charSequence1)) {
        if (Build.VERSION.SDK_INT >= 26) {
          param1AccessibilityNodeInfoCompat.setHintText(charSequence1);
        } else {
          if (k != 0)
            charSequence1 = charSequence2 + ", " + charSequence1; 
          param1AccessibilityNodeInfoCompat.setText(charSequence1);
        } 
        if (k == 0) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        param1AccessibilityNodeInfoCompat.setShowingHintText(bool1);
      } 
      if (charSequence2 == null || charSequence2.length() != i)
        i = -1; 
      param1AccessibilityNodeInfoCompat.setMaxTextLength(i);
      if (bool) {
        if (j != 0) {
          charSequence1 = charSequence3;
        } else {
          charSequence1 = charSequence4;
        } 
        param1AccessibilityNodeInfoCompat.setError(charSequence1);
      } 
      if (Build.VERSION.SDK_INT >= 17 && editText != null)
        editText.setLabelFor(R.id.textinput_helper_text); 
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface BoxBackgroundMode {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface EndIconMode {}
  
  public static interface OnEditTextAttachedListener {
    void onEditTextAttached(TextInputLayout param1TextInputLayout);
  }
  
  public static interface OnEndIconChangedListener {
    void onEndIconChanged(TextInputLayout param1TextInputLayout, int param1Int);
  }
  
  static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public TextInputLayout.SavedState createFromParcel(Parcel param2Parcel) {
          return new TextInputLayout.SavedState(param2Parcel, null);
        }
        
        public TextInputLayout.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new TextInputLayout.SavedState(param2Parcel, param2ClassLoader);
        }
        
        public TextInputLayout.SavedState[] newArray(int param2Int) {
          return new TextInputLayout.SavedState[param2Int];
        }
      };
    
    CharSequence error;
    
    CharSequence helperText;
    
    CharSequence hintText;
    
    boolean isEndIconChecked;
    
    CharSequence placeholderText;
    
    SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      this.error = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(param1Parcel);
      int i = param1Parcel.readInt();
      boolean bool = true;
      if (i != 1)
        bool = false; 
      this.isEndIconChecked = bool;
      this.hintText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(param1Parcel);
      this.helperText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(param1Parcel);
      this.placeholderText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(param1Parcel);
    }
    
    SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public String toString() {
      return "TextInputLayout.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " error=" + this.error + " hint=" + this.hintText + " helperText=" + this.helperText + " placeholderText=" + this.placeholderText + "}";
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      TextUtils.writeToParcel(this.error, param1Parcel, param1Int);
      param1Parcel.writeInt(this.isEndIconChecked);
      TextUtils.writeToParcel(this.hintText, param1Parcel, param1Int);
      TextUtils.writeToParcel(this.helperText, param1Parcel, param1Int);
      TextUtils.writeToParcel(this.placeholderText, param1Parcel, param1Int);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public TextInputLayout.SavedState createFromParcel(Parcel param1Parcel) {
      return new TextInputLayout.SavedState(param1Parcel, null);
    }
    
    public TextInputLayout.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new TextInputLayout.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public TextInputLayout.SavedState[] newArray(int param1Int) {
      return new TextInputLayout.SavedState[param1Int];
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\textfield\TextInputLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */