package com.google.android.material.textfield;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.animation.AnimatorSetCompat;
import com.google.android.material.resources.MaterialResources;
import java.util.ArrayList;
import java.util.List;

final class IndicatorViewController {
  private static final int CAPTION_OPACITY_FADE_ANIMATION_DURATION = 167;
  
  private static final int CAPTION_STATE_ERROR = 1;
  
  private static final int CAPTION_STATE_HELPER_TEXT = 2;
  
  private static final int CAPTION_STATE_NONE = 0;
  
  private static final int CAPTION_TRANSLATE_Y_ANIMATION_DURATION = 217;
  
  static final int COUNTER_INDEX = 2;
  
  static final int ERROR_INDEX = 0;
  
  static final int HELPER_INDEX = 1;
  
  private Animator captionAnimator;
  
  private FrameLayout captionArea;
  
  private int captionDisplayed;
  
  private int captionToShow;
  
  private final float captionTranslationYPx;
  
  private final Context context;
  
  private boolean errorEnabled;
  
  private CharSequence errorText;
  
  private int errorTextAppearance;
  
  private TextView errorView;
  
  private CharSequence errorViewContentDescription;
  
  private ColorStateList errorViewTextColor;
  
  private CharSequence helperText;
  
  private boolean helperTextEnabled;
  
  private int helperTextTextAppearance;
  
  private TextView helperTextView;
  
  private ColorStateList helperTextViewTextColor;
  
  private LinearLayout indicatorArea;
  
  private int indicatorsAdded;
  
  private final TextInputLayout textInputView;
  
  private Typeface typeface;
  
  public IndicatorViewController(TextInputLayout paramTextInputLayout) {
    Context context = paramTextInputLayout.getContext();
    this.context = context;
    this.textInputView = paramTextInputLayout;
    this.captionTranslationYPx = context.getResources().getDimensionPixelSize(R.dimen.design_textinput_caption_translate_y);
  }
  
  private boolean canAdjustIndicatorPadding() {
    boolean bool;
    if (this.indicatorArea != null && this.textInputView.getEditText() != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void createCaptionAnimators(List<Animator> paramList, boolean paramBoolean, TextView paramTextView, int paramInt1, int paramInt2, int paramInt3) {
    if (paramTextView == null || !paramBoolean)
      return; 
    if (paramInt1 == paramInt3 || paramInt1 == paramInt2) {
      if (paramInt3 == paramInt1) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      } 
      paramList.add(createCaptionOpacityAnimator(paramTextView, paramBoolean));
      if (paramInt3 == paramInt1)
        paramList.add(createCaptionTranslationYAnimator(paramTextView)); 
    } 
  }
  
  private ObjectAnimator createCaptionOpacityAnimator(TextView paramTextView, boolean paramBoolean) {
    float f;
    if (paramBoolean) {
      f = 1.0F;
    } else {
      f = 0.0F;
    } 
    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(paramTextView, View.ALPHA, new float[] { f });
    objectAnimator.setDuration(167L);
    objectAnimator.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
    return objectAnimator;
  }
  
  private ObjectAnimator createCaptionTranslationYAnimator(TextView paramTextView) {
    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(paramTextView, View.TRANSLATION_Y, new float[] { -this.captionTranslationYPx, 0.0F });
    objectAnimator.setDuration(217L);
    objectAnimator.setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
    return objectAnimator;
  }
  
  private TextView getCaptionViewFromDisplayState(int paramInt) {
    switch (paramInt) {
      default:
        return null;
      case 2:
        return this.helperTextView;
      case 1:
        break;
    } 
    return this.errorView;
  }
  
  private int getIndicatorPadding(boolean paramBoolean, int paramInt1, int paramInt2) {
    if (paramBoolean)
      paramInt2 = this.context.getResources().getDimensionPixelSize(paramInt1); 
    return paramInt2;
  }
  
  private boolean isCaptionStateError(int paramInt) {
    boolean bool = true;
    if (paramInt != 1 || this.errorView == null || TextUtils.isEmpty(this.errorText))
      bool = false; 
    return bool;
  }
  
  private boolean isCaptionStateHelperText(int paramInt) {
    boolean bool;
    if (paramInt == 2 && this.helperTextView != null && !TextUtils.isEmpty(this.helperText)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void setCaptionViewVisibilities(int paramInt1, int paramInt2) {
    if (paramInt1 == paramInt2)
      return; 
    if (paramInt2 != 0) {
      TextView textView = getCaptionViewFromDisplayState(paramInt2);
      if (textView != null) {
        textView.setVisibility(0);
        textView.setAlpha(1.0F);
      } 
    } 
    if (paramInt1 != 0) {
      TextView textView = getCaptionViewFromDisplayState(paramInt1);
      if (textView != null) {
        textView.setVisibility(4);
        if (paramInt1 == 1)
          textView.setText(null); 
      } 
    } 
    this.captionDisplayed = paramInt2;
  }
  
  private void setTextViewTypeface(TextView paramTextView, Typeface paramTypeface) {
    if (paramTextView != null)
      paramTextView.setTypeface(paramTypeface); 
  }
  
  private void setViewGroupGoneIfEmpty(ViewGroup paramViewGroup, int paramInt) {
    if (paramInt == 0)
      paramViewGroup.setVisibility(8); 
  }
  
  private boolean shouldAnimateCaptionView(TextView paramTextView, CharSequence paramCharSequence) {
    boolean bool;
    if (ViewCompat.isLaidOut((View)this.textInputView) && this.textInputView.isEnabled() && (this.captionToShow != this.captionDisplayed || paramTextView == null || !TextUtils.equals(paramTextView.getText(), paramCharSequence))) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void updateCaptionViewsVisibility(final int captionToHide, final int captionToShow, boolean paramBoolean) {
    if (captionToHide == captionToShow)
      return; 
    if (paramBoolean) {
      AnimatorSet animatorSet = new AnimatorSet();
      this.captionAnimator = (Animator)animatorSet;
      ArrayList<Animator> arrayList = new ArrayList();
      createCaptionAnimators(arrayList, this.helperTextEnabled, this.helperTextView, 2, captionToHide, captionToShow);
      createCaptionAnimators(arrayList, this.errorEnabled, this.errorView, 1, captionToHide, captionToShow);
      AnimatorSetCompat.playTogether(animatorSet, arrayList);
      animatorSet.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            final IndicatorViewController this$0;
            
            final int val$captionToHide;
            
            final int val$captionToShow;
            
            final TextView val$captionViewToHide;
            
            final TextView val$captionViewToShow;
            
            public void onAnimationEnd(Animator param1Animator) {
              IndicatorViewController.access$002(IndicatorViewController.this, captionToShow);
              IndicatorViewController.access$102(IndicatorViewController.this, null);
              TextView textView = captionViewToHide;
              if (textView != null) {
                textView.setVisibility(4);
                if (captionToHide == 1 && IndicatorViewController.this.errorView != null)
                  IndicatorViewController.this.errorView.setText(null); 
              } 
              textView = captionViewToShow;
              if (textView != null) {
                textView.setTranslationY(0.0F);
                captionViewToShow.setAlpha(1.0F);
              } 
            }
            
            public void onAnimationStart(Animator param1Animator) {
              TextView textView = captionViewToShow;
              if (textView != null)
                textView.setVisibility(0); 
            }
          });
      animatorSet.start();
    } else {
      setCaptionViewVisibilities(captionToHide, captionToShow);
    } 
    this.textInputView.updateEditTextBackground();
    this.textInputView.updateLabelState(paramBoolean);
    this.textInputView.updateTextInputBoxState();
  }
  
  void addIndicator(TextView paramTextView, int paramInt) {
    if (this.indicatorArea == null && this.captionArea == null) {
      LinearLayout linearLayout = new LinearLayout(this.context);
      this.indicatorArea = linearLayout;
      linearLayout.setOrientation(0);
      this.textInputView.addView((View)this.indicatorArea, -1, -2);
      this.captionArea = new FrameLayout(this.context);
      LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0F);
      this.indicatorArea.addView((View)this.captionArea, (ViewGroup.LayoutParams)layoutParams);
      if (this.textInputView.getEditText() != null)
        adjustIndicatorPadding(); 
    } 
    if (isCaptionView(paramInt)) {
      this.captionArea.setVisibility(0);
      this.captionArea.addView((View)paramTextView);
    } else {
      LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
      this.indicatorArea.addView((View)paramTextView, (ViewGroup.LayoutParams)layoutParams);
    } 
    this.indicatorArea.setVisibility(0);
    this.indicatorsAdded++;
  }
  
  void adjustIndicatorPadding() {
    if (canAdjustIndicatorPadding()) {
      EditText editText = this.textInputView.getEditText();
      boolean bool = MaterialResources.isFontScaleAtLeast1_3(this.context);
      ViewCompat.setPaddingRelative((View)this.indicatorArea, getIndicatorPadding(bool, R.dimen.material_helper_text_font_1_3_padding_horizontal, ViewCompat.getPaddingStart((View)editText)), getIndicatorPadding(bool, R.dimen.material_helper_text_font_1_3_padding_top, this.context.getResources().getDimensionPixelSize(R.dimen.material_helper_text_default_padding_top)), getIndicatorPadding(bool, R.dimen.material_helper_text_font_1_3_padding_horizontal, ViewCompat.getPaddingEnd((View)editText)), 0);
    } 
  }
  
  void cancelCaptionAnimator() {
    Animator animator = this.captionAnimator;
    if (animator != null)
      animator.cancel(); 
  }
  
  boolean errorIsDisplayed() {
    return isCaptionStateError(this.captionDisplayed);
  }
  
  boolean errorShouldBeShown() {
    return isCaptionStateError(this.captionToShow);
  }
  
  CharSequence getErrorContentDescription() {
    return this.errorViewContentDescription;
  }
  
  CharSequence getErrorText() {
    return this.errorText;
  }
  
  int getErrorViewCurrentTextColor() {
    byte b;
    TextView textView = this.errorView;
    if (textView != null) {
      b = textView.getCurrentTextColor();
    } else {
      b = -1;
    } 
    return b;
  }
  
  ColorStateList getErrorViewTextColors() {
    TextView textView = this.errorView;
    if (textView != null) {
      ColorStateList colorStateList = textView.getTextColors();
    } else {
      textView = null;
    } 
    return (ColorStateList)textView;
  }
  
  CharSequence getHelperText() {
    return this.helperText;
  }
  
  ColorStateList getHelperTextViewColors() {
    TextView textView = this.helperTextView;
    if (textView != null) {
      ColorStateList colorStateList = textView.getTextColors();
    } else {
      textView = null;
    } 
    return (ColorStateList)textView;
  }
  
  int getHelperTextViewCurrentTextColor() {
    byte b;
    TextView textView = this.helperTextView;
    if (textView != null) {
      b = textView.getCurrentTextColor();
    } else {
      b = -1;
    } 
    return b;
  }
  
  boolean helperTextIsDisplayed() {
    return isCaptionStateHelperText(this.captionDisplayed);
  }
  
  boolean helperTextShouldBeShown() {
    return isCaptionStateHelperText(this.captionToShow);
  }
  
  void hideError() {
    this.errorText = null;
    cancelCaptionAnimator();
    if (this.captionDisplayed == 1)
      if (this.helperTextEnabled && !TextUtils.isEmpty(this.helperText)) {
        this.captionToShow = 2;
      } else {
        this.captionToShow = 0;
      }  
    updateCaptionViewsVisibility(this.captionDisplayed, this.captionToShow, shouldAnimateCaptionView(this.errorView, null));
  }
  
  void hideHelperText() {
    cancelCaptionAnimator();
    int i = this.captionDisplayed;
    if (i == 2)
      this.captionToShow = 0; 
    updateCaptionViewsVisibility(i, this.captionToShow, shouldAnimateCaptionView(this.helperTextView, null));
  }
  
  boolean isCaptionView(int paramInt) {
    boolean bool2 = true;
    boolean bool1 = bool2;
    if (paramInt != 0)
      if (paramInt == 1) {
        bool1 = bool2;
      } else {
        bool1 = false;
      }  
    return bool1;
  }
  
  boolean isErrorEnabled() {
    return this.errorEnabled;
  }
  
  boolean isHelperTextEnabled() {
    return this.helperTextEnabled;
  }
  
  void removeIndicator(TextView paramTextView, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: getfield indicatorArea : Landroid/widget/LinearLayout;
    //   4: ifnonnull -> 8
    //   7: return
    //   8: aload_0
    //   9: iload_2
    //   10: invokevirtual isCaptionView : (I)Z
    //   13: ifeq -> 33
    //   16: aload_0
    //   17: getfield captionArea : Landroid/widget/FrameLayout;
    //   20: astore_3
    //   21: aload_3
    //   22: ifnull -> 33
    //   25: aload_3
    //   26: aload_1
    //   27: invokevirtual removeView : (Landroid/view/View;)V
    //   30: goto -> 41
    //   33: aload_0
    //   34: getfield indicatorArea : Landroid/widget/LinearLayout;
    //   37: aload_1
    //   38: invokevirtual removeView : (Landroid/view/View;)V
    //   41: aload_0
    //   42: getfield indicatorsAdded : I
    //   45: iconst_1
    //   46: isub
    //   47: istore_2
    //   48: aload_0
    //   49: iload_2
    //   50: putfield indicatorsAdded : I
    //   53: aload_0
    //   54: aload_0
    //   55: getfield indicatorArea : Landroid/widget/LinearLayout;
    //   58: iload_2
    //   59: invokespecial setViewGroupGoneIfEmpty : (Landroid/view/ViewGroup;I)V
    //   62: return
  }
  
  void setErrorContentDescription(CharSequence paramCharSequence) {
    this.errorViewContentDescription = paramCharSequence;
    TextView textView = this.errorView;
    if (textView != null)
      textView.setContentDescription(paramCharSequence); 
  }
  
  void setErrorEnabled(boolean paramBoolean) {
    if (this.errorEnabled == paramBoolean)
      return; 
    cancelCaptionAnimator();
    if (paramBoolean) {
      AppCompatTextView appCompatTextView = new AppCompatTextView(this.context);
      this.errorView = (TextView)appCompatTextView;
      appCompatTextView.setId(R.id.textinput_error);
      if (Build.VERSION.SDK_INT >= 17)
        this.errorView.setTextAlignment(5); 
      Typeface typeface = this.typeface;
      if (typeface != null)
        this.errorView.setTypeface(typeface); 
      setErrorTextAppearance(this.errorTextAppearance);
      setErrorViewTextColor(this.errorViewTextColor);
      setErrorContentDescription(this.errorViewContentDescription);
      this.errorView.setVisibility(4);
      ViewCompat.setAccessibilityLiveRegion((View)this.errorView, 1);
      addIndicator(this.errorView, 0);
    } else {
      hideError();
      removeIndicator(this.errorView, 0);
      this.errorView = null;
      this.textInputView.updateEditTextBackground();
      this.textInputView.updateTextInputBoxState();
    } 
    this.errorEnabled = paramBoolean;
  }
  
  void setErrorTextAppearance(int paramInt) {
    this.errorTextAppearance = paramInt;
    TextView textView = this.errorView;
    if (textView != null)
      this.textInputView.setTextAppearanceCompatWithErrorFallback(textView, paramInt); 
  }
  
  void setErrorViewTextColor(ColorStateList paramColorStateList) {
    this.errorViewTextColor = paramColorStateList;
    TextView textView = this.errorView;
    if (textView != null && paramColorStateList != null)
      textView.setTextColor(paramColorStateList); 
  }
  
  void setHelperTextAppearance(int paramInt) {
    this.helperTextTextAppearance = paramInt;
    TextView textView = this.helperTextView;
    if (textView != null)
      TextViewCompat.setTextAppearance(textView, paramInt); 
  }
  
  void setHelperTextEnabled(boolean paramBoolean) {
    if (this.helperTextEnabled == paramBoolean)
      return; 
    cancelCaptionAnimator();
    if (paramBoolean) {
      AppCompatTextView appCompatTextView = new AppCompatTextView(this.context);
      this.helperTextView = (TextView)appCompatTextView;
      appCompatTextView.setId(R.id.textinput_helper_text);
      if (Build.VERSION.SDK_INT >= 17)
        this.helperTextView.setTextAlignment(5); 
      Typeface typeface = this.typeface;
      if (typeface != null)
        this.helperTextView.setTypeface(typeface); 
      this.helperTextView.setVisibility(4);
      ViewCompat.setAccessibilityLiveRegion((View)this.helperTextView, 1);
      setHelperTextAppearance(this.helperTextTextAppearance);
      setHelperTextViewTextColor(this.helperTextViewTextColor);
      addIndicator(this.helperTextView, 1);
    } else {
      hideHelperText();
      removeIndicator(this.helperTextView, 1);
      this.helperTextView = null;
      this.textInputView.updateEditTextBackground();
      this.textInputView.updateTextInputBoxState();
    } 
    this.helperTextEnabled = paramBoolean;
  }
  
  void setHelperTextViewTextColor(ColorStateList paramColorStateList) {
    this.helperTextViewTextColor = paramColorStateList;
    TextView textView = this.helperTextView;
    if (textView != null && paramColorStateList != null)
      textView.setTextColor(paramColorStateList); 
  }
  
  void setTypefaces(Typeface paramTypeface) {
    if (paramTypeface != this.typeface) {
      this.typeface = paramTypeface;
      setTextViewTypeface(this.errorView, paramTypeface);
      setTextViewTypeface(this.helperTextView, paramTypeface);
    } 
  }
  
  void showError(CharSequence paramCharSequence) {
    cancelCaptionAnimator();
    this.errorText = paramCharSequence;
    this.errorView.setText(paramCharSequence);
    int i = this.captionDisplayed;
    if (i != 1)
      this.captionToShow = 1; 
    updateCaptionViewsVisibility(i, this.captionToShow, shouldAnimateCaptionView(this.errorView, paramCharSequence));
  }
  
  void showHelper(CharSequence paramCharSequence) {
    cancelCaptionAnimator();
    this.helperText = paramCharSequence;
    this.helperTextView.setText(paramCharSequence);
    int i = this.captionDisplayed;
    if (i != 2)
      this.captionToShow = 2; 
    updateCaptionViewsVisibility(i, this.captionToShow, shouldAnimateCaptionView(this.helperTextView, paramCharSequence));
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\textfield\IndicatorViewController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */