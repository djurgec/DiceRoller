package com.google.android.material.textfield;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

class DropdownMenuEndIconDelegate extends EndIconDelegate {
  private static final int ANIMATION_FADE_IN_DURATION = 67;
  
  private static final int ANIMATION_FADE_OUT_DURATION = 50;
  
  private static final boolean IS_LOLLIPOP;
  
  private final TextInputLayout.AccessibilityDelegate accessibilityDelegate = new TextInputLayout.AccessibilityDelegate(this.textInputLayout) {
      final DropdownMenuEndIconDelegate this$0;
      
      public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
        super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
        if (!DropdownMenuEndIconDelegate.isEditable(DropdownMenuEndIconDelegate.this.textInputLayout.getEditText()))
          param1AccessibilityNodeInfoCompat.setClassName(Spinner.class.getName()); 
        if (param1AccessibilityNodeInfoCompat.isShowingHintText())
          param1AccessibilityNodeInfoCompat.setHintText(null); 
      }
      
      public void onPopulateAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
        super.onPopulateAccessibilityEvent(param1View, param1AccessibilityEvent);
        AutoCompleteTextView autoCompleteTextView = DropdownMenuEndIconDelegate.castAutoCompleteTextViewOrThrow(DropdownMenuEndIconDelegate.this.textInputLayout.getEditText());
        if (param1AccessibilityEvent.getEventType() == 1 && DropdownMenuEndIconDelegate.this.accessibilityManager.isTouchExplorationEnabled() && !DropdownMenuEndIconDelegate.isEditable(DropdownMenuEndIconDelegate.this.textInputLayout.getEditText()))
          DropdownMenuEndIconDelegate.this.showHideDropdown(autoCompleteTextView); 
      }
    };
  
  private AccessibilityManager accessibilityManager;
  
  private final TextInputLayout.OnEditTextAttachedListener dropdownMenuOnEditTextAttachedListener = new TextInputLayout.OnEditTextAttachedListener() {
      final DropdownMenuEndIconDelegate this$0;
      
      public void onEditTextAttached(TextInputLayout param1TextInputLayout) {
        AutoCompleteTextView autoCompleteTextView = DropdownMenuEndIconDelegate.castAutoCompleteTextViewOrThrow(param1TextInputLayout.getEditText());
        DropdownMenuEndIconDelegate.this.setPopupBackground(autoCompleteTextView);
        DropdownMenuEndIconDelegate.this.addRippleEffect(autoCompleteTextView);
        DropdownMenuEndIconDelegate.this.setUpDropdownShowHideBehavior(autoCompleteTextView);
        autoCompleteTextView.setThreshold(0);
        autoCompleteTextView.removeTextChangedListener(DropdownMenuEndIconDelegate.this.exposedDropdownEndIconTextWatcher);
        autoCompleteTextView.addTextChangedListener(DropdownMenuEndIconDelegate.this.exposedDropdownEndIconTextWatcher);
        param1TextInputLayout.setEndIconCheckable(true);
        param1TextInputLayout.setErrorIconDrawable((Drawable)null);
        if (!DropdownMenuEndIconDelegate.isEditable((EditText)autoCompleteTextView))
          ViewCompat.setImportantForAccessibility((View)DropdownMenuEndIconDelegate.this.endIconView, 2); 
        param1TextInputLayout.setTextInputAccessibilityDelegate(DropdownMenuEndIconDelegate.this.accessibilityDelegate);
        param1TextInputLayout.setEndIconVisible(true);
      }
    };
  
  private long dropdownPopupActivatedAt = Long.MAX_VALUE;
  
  private boolean dropdownPopupDirty = false;
  
  private final TextInputLayout.OnEndIconChangedListener endIconChangedListener = new TextInputLayout.OnEndIconChangedListener() {
      final DropdownMenuEndIconDelegate this$0;
      
      public void onEndIconChanged(TextInputLayout param1TextInputLayout, int param1Int) {
        final AutoCompleteTextView editText = (AutoCompleteTextView)param1TextInputLayout.getEditText();
        if (autoCompleteTextView != null && param1Int == 3) {
          autoCompleteTextView.post(new Runnable() {
                final DropdownMenuEndIconDelegate.null this$1;
                
                final AutoCompleteTextView val$editText;
                
                public void run() {
                  editText.removeTextChangedListener(DropdownMenuEndIconDelegate.this.exposedDropdownEndIconTextWatcher);
                }
              });
          if (autoCompleteTextView.getOnFocusChangeListener() == DropdownMenuEndIconDelegate.this.onFocusChangeListener)
            autoCompleteTextView.setOnFocusChangeListener(null); 
          autoCompleteTextView.setOnTouchListener(null);
          if (DropdownMenuEndIconDelegate.IS_LOLLIPOP)
            autoCompleteTextView.setOnDismissListener(null); 
        } 
      }
    };
  
  private final TextWatcher exposedDropdownEndIconTextWatcher = (TextWatcher)new TextWatcherAdapter() {
      final DropdownMenuEndIconDelegate this$0;
      
      public void afterTextChanged(Editable param1Editable) {
        final AutoCompleteTextView editText = DropdownMenuEndIconDelegate.castAutoCompleteTextViewOrThrow(DropdownMenuEndIconDelegate.this.textInputLayout.getEditText());
        if (DropdownMenuEndIconDelegate.this.accessibilityManager.isTouchExplorationEnabled() && DropdownMenuEndIconDelegate.isEditable((EditText)autoCompleteTextView) && !DropdownMenuEndIconDelegate.this.endIconView.hasFocus())
          autoCompleteTextView.dismissDropDown(); 
        autoCompleteTextView.post(new Runnable() {
              final DropdownMenuEndIconDelegate.null this$1;
              
              final AutoCompleteTextView val$editText;
              
              public void run() {
                boolean bool = editText.isPopupShowing();
                DropdownMenuEndIconDelegate.this.setEndIconChecked(bool);
                DropdownMenuEndIconDelegate.access$402(DropdownMenuEndIconDelegate.this, bool);
              }
            });
      }
    };
  
  private ValueAnimator fadeInAnim;
  
  private ValueAnimator fadeOutAnim;
  
  private StateListDrawable filledPopupBackground;
  
  private boolean isEndIconChecked = false;
  
  private final View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
      final DropdownMenuEndIconDelegate this$0;
      
      public void onFocusChange(View param1View, boolean param1Boolean) {
        DropdownMenuEndIconDelegate.this.textInputLayout.setEndIconActivated(param1Boolean);
        if (!param1Boolean) {
          DropdownMenuEndIconDelegate.this.setEndIconChecked(false);
          DropdownMenuEndIconDelegate.access$402(DropdownMenuEndIconDelegate.this, false);
        } 
      }
    };
  
  private MaterialShapeDrawable outlinedPopupBackground;
  
  static {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 21) {
      bool = true;
    } else {
      bool = false;
    } 
    IS_LOLLIPOP = bool;
  }
  
  DropdownMenuEndIconDelegate(TextInputLayout paramTextInputLayout) {
    super(paramTextInputLayout);
  }
  
  private void addRippleEffect(AutoCompleteTextView paramAutoCompleteTextView) {
    if (isEditable((EditText)paramAutoCompleteTextView))
      return; 
    int i = this.textInputLayout.getBoxBackgroundMode();
    MaterialShapeDrawable materialShapeDrawable = this.textInputLayout.getBoxBackground();
    int j = MaterialColors.getColor((View)paramAutoCompleteTextView, R.attr.colorControlHighlight);
    int[][] arrayOfInt = new int[2][];
    (new int[1])[0] = 16842919;
    arrayOfInt[0] = new int[1];
    arrayOfInt[1] = new int[0];
    if (i == 2) {
      addRippleEffectOnOutlinedLayout(paramAutoCompleteTextView, j, arrayOfInt, materialShapeDrawable);
    } else if (i == 1) {
      addRippleEffectOnFilledLayout(paramAutoCompleteTextView, j, arrayOfInt, materialShapeDrawable);
    } 
  }
  
  private void addRippleEffectOnFilledLayout(AutoCompleteTextView paramAutoCompleteTextView, int paramInt, int[][] paramArrayOfint, MaterialShapeDrawable paramMaterialShapeDrawable) {
    int i = this.textInputLayout.getBoxBackgroundColor();
    paramInt = MaterialColors.layer(paramInt, i, 0.1F);
    int[] arrayOfInt = new int[2];
    arrayOfInt[0] = paramInt;
    arrayOfInt[1] = i;
    if (IS_LOLLIPOP) {
      ViewCompat.setBackground((View)paramAutoCompleteTextView, (Drawable)new RippleDrawable(new ColorStateList(paramArrayOfint, arrayOfInt), (Drawable)paramMaterialShapeDrawable, (Drawable)paramMaterialShapeDrawable));
    } else {
      MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(paramMaterialShapeDrawable.getShapeAppearanceModel());
      materialShapeDrawable.setFillColor(new ColorStateList(paramArrayOfint, arrayOfInt));
      LayerDrawable layerDrawable = new LayerDrawable(new Drawable[] { (Drawable)paramMaterialShapeDrawable, (Drawable)materialShapeDrawable });
      i = ViewCompat.getPaddingStart((View)paramAutoCompleteTextView);
      int k = paramAutoCompleteTextView.getPaddingTop();
      int j = ViewCompat.getPaddingEnd((View)paramAutoCompleteTextView);
      paramInt = paramAutoCompleteTextView.getPaddingBottom();
      ViewCompat.setBackground((View)paramAutoCompleteTextView, (Drawable)layerDrawable);
      ViewCompat.setPaddingRelative((View)paramAutoCompleteTextView, i, k, j, paramInt);
    } 
  }
  
  private void addRippleEffectOnOutlinedLayout(AutoCompleteTextView paramAutoCompleteTextView, int paramInt, int[][] paramArrayOfint, MaterialShapeDrawable paramMaterialShapeDrawable) {
    LayerDrawable layerDrawable;
    int i = MaterialColors.getColor((View)paramAutoCompleteTextView, R.attr.colorSurface);
    MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(paramMaterialShapeDrawable.getShapeAppearanceModel());
    paramInt = MaterialColors.layer(paramInt, i, 0.1F);
    materialShapeDrawable.setFillColor(new ColorStateList(paramArrayOfint, new int[] { paramInt, 0 }));
    if (IS_LOLLIPOP) {
      materialShapeDrawable.setTint(i);
      ColorStateList colorStateList = new ColorStateList(paramArrayOfint, new int[] { paramInt, i });
      MaterialShapeDrawable materialShapeDrawable1 = new MaterialShapeDrawable(paramMaterialShapeDrawable.getShapeAppearanceModel());
      materialShapeDrawable1.setTint(-1);
      layerDrawable = new LayerDrawable(new Drawable[] { (Drawable)new RippleDrawable(colorStateList, (Drawable)materialShapeDrawable, (Drawable)materialShapeDrawable1), (Drawable)paramMaterialShapeDrawable });
    } else {
      layerDrawable = new LayerDrawable(new Drawable[] { (Drawable)materialShapeDrawable, (Drawable)paramMaterialShapeDrawable });
    } 
    ViewCompat.setBackground((View)paramAutoCompleteTextView, (Drawable)layerDrawable);
  }
  
  private static AutoCompleteTextView castAutoCompleteTextViewOrThrow(EditText paramEditText) {
    if (paramEditText instanceof AutoCompleteTextView)
      return (AutoCompleteTextView)paramEditText; 
    throw new RuntimeException("EditText needs to be an AutoCompleteTextView if an Exposed Dropdown Menu is being used.");
  }
  
  private ValueAnimator getAlphaAnimator(int paramInt, float... paramVarArgs) {
    ValueAnimator valueAnimator = ValueAnimator.ofFloat(paramVarArgs);
    valueAnimator.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
    valueAnimator.setDuration(paramInt);
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          final DropdownMenuEndIconDelegate this$0;
          
          public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
            float f = ((Float)param1ValueAnimator.getAnimatedValue()).floatValue();
            DropdownMenuEndIconDelegate.this.endIconView.setAlpha(f);
          }
        });
    return valueAnimator;
  }
  
  private MaterialShapeDrawable getPopUpMaterialShapeDrawable(float paramFloat1, float paramFloat2, float paramFloat3, int paramInt) {
    ShapeAppearanceModel shapeAppearanceModel = ShapeAppearanceModel.builder().setTopLeftCornerSize(paramFloat1).setTopRightCornerSize(paramFloat1).setBottomLeftCornerSize(paramFloat2).setBottomRightCornerSize(paramFloat2).build();
    MaterialShapeDrawable materialShapeDrawable = MaterialShapeDrawable.createWithElevationOverlay(this.context, paramFloat3);
    materialShapeDrawable.setShapeAppearanceModel(shapeAppearanceModel);
    materialShapeDrawable.setPadding(0, paramInt, 0, paramInt);
    return materialShapeDrawable;
  }
  
  private void initAnimators() {
    this.fadeInAnim = getAlphaAnimator(67, new float[] { 0.0F, 1.0F });
    ValueAnimator valueAnimator = getAlphaAnimator(50, new float[] { 1.0F, 0.0F });
    this.fadeOutAnim = valueAnimator;
    valueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          final DropdownMenuEndIconDelegate this$0;
          
          public void onAnimationEnd(Animator param1Animator) {
            DropdownMenuEndIconDelegate.this.endIconView.setChecked(DropdownMenuEndIconDelegate.this.isEndIconChecked);
            DropdownMenuEndIconDelegate.this.fadeInAnim.start();
          }
        });
  }
  
  private boolean isDropdownPopupActive() {
    long l = System.currentTimeMillis() - this.dropdownPopupActivatedAt;
    return (l < 0L || l > 300L);
  }
  
  private static boolean isEditable(EditText paramEditText) {
    boolean bool;
    if (paramEditText.getKeyListener() != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void setEndIconChecked(boolean paramBoolean) {
    if (this.isEndIconChecked != paramBoolean) {
      this.isEndIconChecked = paramBoolean;
      this.fadeInAnim.cancel();
      this.fadeOutAnim.start();
    } 
  }
  
  private void setPopupBackground(AutoCompleteTextView paramAutoCompleteTextView) {
    if (IS_LOLLIPOP) {
      int i = this.textInputLayout.getBoxBackgroundMode();
      if (i == 2) {
        paramAutoCompleteTextView.setDropDownBackgroundDrawable((Drawable)this.outlinedPopupBackground);
      } else if (i == 1) {
        paramAutoCompleteTextView.setDropDownBackgroundDrawable((Drawable)this.filledPopupBackground);
      } 
    } 
  }
  
  private void setUpDropdownShowHideBehavior(final AutoCompleteTextView editText) {
    editText.setOnTouchListener(new View.OnTouchListener() {
          final DropdownMenuEndIconDelegate this$0;
          
          final AutoCompleteTextView val$editText;
          
          public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
            if (param1MotionEvent.getAction() == 1) {
              if (DropdownMenuEndIconDelegate.this.isDropdownPopupActive())
                DropdownMenuEndIconDelegate.access$402(DropdownMenuEndIconDelegate.this, false); 
              DropdownMenuEndIconDelegate.this.showHideDropdown(editText);
            } 
            return false;
          }
        });
    editText.setOnFocusChangeListener(this.onFocusChangeListener);
    if (IS_LOLLIPOP)
      editText.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            final DropdownMenuEndIconDelegate this$0;
            
            public void onDismiss() {
              DropdownMenuEndIconDelegate.access$402(DropdownMenuEndIconDelegate.this, true);
              DropdownMenuEndIconDelegate.access$1402(DropdownMenuEndIconDelegate.this, System.currentTimeMillis());
              DropdownMenuEndIconDelegate.this.setEndIconChecked(false);
            }
          }); 
  }
  
  private void showHideDropdown(AutoCompleteTextView paramAutoCompleteTextView) {
    if (paramAutoCompleteTextView == null)
      return; 
    if (isDropdownPopupActive())
      this.dropdownPopupDirty = false; 
    if (!this.dropdownPopupDirty) {
      if (IS_LOLLIPOP) {
        setEndIconChecked(this.isEndIconChecked ^ true);
      } else {
        this.isEndIconChecked ^= 0x1;
        this.endIconView.toggle();
      } 
      if (this.isEndIconChecked) {
        paramAutoCompleteTextView.requestFocus();
        paramAutoCompleteTextView.showDropDown();
      } else {
        paramAutoCompleteTextView.dismissDropDown();
      } 
    } else {
      this.dropdownPopupDirty = false;
    } 
  }
  
  void initialize() {
    float f2 = this.context.getResources().getDimensionPixelOffset(R.dimen.mtrl_shape_corner_size_small_component);
    float f1 = this.context.getResources().getDimensionPixelOffset(R.dimen.mtrl_exposed_dropdown_menu_popup_elevation);
    int i = this.context.getResources().getDimensionPixelOffset(R.dimen.mtrl_exposed_dropdown_menu_popup_vertical_padding);
    MaterialShapeDrawable materialShapeDrawable1 = getPopUpMaterialShapeDrawable(f2, f2, f1, i);
    MaterialShapeDrawable materialShapeDrawable2 = getPopUpMaterialShapeDrawable(0.0F, f2, f1, i);
    this.outlinedPopupBackground = materialShapeDrawable1;
    StateListDrawable stateListDrawable = new StateListDrawable();
    this.filledPopupBackground = stateListDrawable;
    stateListDrawable.addState(new int[] { 16842922 }, (Drawable)materialShapeDrawable1);
    this.filledPopupBackground.addState(new int[0], (Drawable)materialShapeDrawable2);
    if (IS_LOLLIPOP) {
      i = R.drawable.mtrl_dropdown_arrow;
    } else {
      i = R.drawable.mtrl_ic_arrow_drop_down;
    } 
    this.textInputLayout.setEndIconDrawable(AppCompatResources.getDrawable(this.context, i));
    this.textInputLayout.setEndIconContentDescription(this.textInputLayout.getResources().getText(R.string.exposed_dropdown_menu_content_description));
    this.textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
          final DropdownMenuEndIconDelegate this$0;
          
          public void onClick(View param1View) {
            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)DropdownMenuEndIconDelegate.this.textInputLayout.getEditText();
            DropdownMenuEndIconDelegate.this.showHideDropdown(autoCompleteTextView);
          }
        });
    this.textInputLayout.addOnEditTextAttachedListener(this.dropdownMenuOnEditTextAttachedListener);
    this.textInputLayout.addOnEndIconChangedListener(this.endIconChangedListener);
    initAnimators();
    this.accessibilityManager = (AccessibilityManager)this.context.getSystemService("accessibility");
  }
  
  boolean isBoxBackgroundModeSupported(int paramInt) {
    boolean bool;
    if (paramInt != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  boolean shouldTintIconOnError() {
    return true;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\textfield\DropdownMenuEndIconDelegate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */