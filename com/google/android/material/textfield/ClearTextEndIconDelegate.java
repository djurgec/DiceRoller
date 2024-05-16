package com.google.android.material.textfield;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.content.res.AppCompatResources;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;

class ClearTextEndIconDelegate extends EndIconDelegate {
  private static final int ANIMATION_FADE_DURATION = 100;
  
  private static final int ANIMATION_SCALE_DURATION = 150;
  
  private static final float ANIMATION_SCALE_FROM_VALUE = 0.8F;
  
  private final TextWatcher clearTextEndIconTextWatcher = new TextWatcher() {
      final ClearTextEndIconDelegate this$0;
      
      public void afterTextChanged(Editable param1Editable) {
        boolean bool;
        if (ClearTextEndIconDelegate.this.textInputLayout.getSuffixText() != null)
          return; 
        ClearTextEndIconDelegate clearTextEndIconDelegate = ClearTextEndIconDelegate.this;
        if (clearTextEndIconDelegate.textInputLayout.hasFocus() && ClearTextEndIconDelegate.hasText(param1Editable)) {
          bool = true;
        } else {
          bool = false;
        } 
        clearTextEndIconDelegate.animateIcon(bool);
      }
      
      public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
      
      public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
    };
  
  private final TextInputLayout.OnEditTextAttachedListener clearTextOnEditTextAttachedListener = new TextInputLayout.OnEditTextAttachedListener() {
      final ClearTextEndIconDelegate this$0;
      
      public void onEditTextAttached(TextInputLayout param1TextInputLayout) {
        boolean bool;
        EditText editText = param1TextInputLayout.getEditText();
        if (editText.hasFocus() && ClearTextEndIconDelegate.hasText(editText.getText())) {
          bool = true;
        } else {
          bool = false;
        } 
        param1TextInputLayout.setEndIconVisible(bool);
        param1TextInputLayout.setEndIconCheckable(false);
        editText.setOnFocusChangeListener(ClearTextEndIconDelegate.this.onFocusChangeListener);
        editText.removeTextChangedListener(ClearTextEndIconDelegate.this.clearTextEndIconTextWatcher);
        editText.addTextChangedListener(ClearTextEndIconDelegate.this.clearTextEndIconTextWatcher);
      }
    };
  
  private final TextInputLayout.OnEndIconChangedListener endIconChangedListener = new TextInputLayout.OnEndIconChangedListener() {
      final ClearTextEndIconDelegate this$0;
      
      public void onEndIconChanged(TextInputLayout param1TextInputLayout, int param1Int) {
        final EditText editText = param1TextInputLayout.getEditText();
        if (editText != null && param1Int == 2) {
          editText.post(new Runnable() {
                final ClearTextEndIconDelegate.null this$1;
                
                final EditText val$editText;
                
                public void run() {
                  editText.removeTextChangedListener(ClearTextEndIconDelegate.this.clearTextEndIconTextWatcher);
                }
              });
          if (editText.getOnFocusChangeListener() == ClearTextEndIconDelegate.this.onFocusChangeListener)
            editText.setOnFocusChangeListener(null); 
        } 
      }
    };
  
  private AnimatorSet iconInAnim;
  
  private ValueAnimator iconOutAnim;
  
  private final View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
      final ClearTextEndIconDelegate this$0;
      
      public void onFocusChange(View param1View, boolean param1Boolean) {
        boolean bool1 = TextUtils.isEmpty((CharSequence)((EditText)param1View).getText());
        boolean bool = true;
        ClearTextEndIconDelegate clearTextEndIconDelegate = ClearTextEndIconDelegate.this;
        if ((bool1 ^ true) != 0 && param1Boolean) {
          param1Boolean = bool;
        } else {
          param1Boolean = false;
        } 
        clearTextEndIconDelegate.animateIcon(param1Boolean);
      }
    };
  
  ClearTextEndIconDelegate(TextInputLayout paramTextInputLayout) {
    super(paramTextInputLayout);
  }
  
  private void animateIcon(boolean paramBoolean) {
    boolean bool;
    if (this.textInputLayout.isEndIconVisible() == paramBoolean) {
      bool = true;
    } else {
      bool = false;
    } 
    if (paramBoolean && !this.iconInAnim.isRunning()) {
      this.iconOutAnim.cancel();
      this.iconInAnim.start();
      if (bool)
        this.iconInAnim.end(); 
    } else if (!paramBoolean) {
      this.iconInAnim.cancel();
      this.iconOutAnim.start();
      if (bool)
        this.iconOutAnim.end(); 
    } 
  }
  
  private ValueAnimator getAlphaAnimator(float... paramVarArgs) {
    ValueAnimator valueAnimator = ValueAnimator.ofFloat(paramVarArgs);
    valueAnimator.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
    valueAnimator.setDuration(100L);
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          final ClearTextEndIconDelegate this$0;
          
          public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
            float f = ((Float)param1ValueAnimator.getAnimatedValue()).floatValue();
            ClearTextEndIconDelegate.this.endIconView.setAlpha(f);
          }
        });
    return valueAnimator;
  }
  
  private ValueAnimator getScaleAnimator() {
    ValueAnimator valueAnimator = ValueAnimator.ofFloat(new float[] { 0.8F, 1.0F });
    valueAnimator.setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
    valueAnimator.setDuration(150L);
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          final ClearTextEndIconDelegate this$0;
          
          public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
            float f = ((Float)param1ValueAnimator.getAnimatedValue()).floatValue();
            ClearTextEndIconDelegate.this.endIconView.setScaleX(f);
            ClearTextEndIconDelegate.this.endIconView.setScaleY(f);
          }
        });
    return valueAnimator;
  }
  
  private static boolean hasText(Editable paramEditable) {
    boolean bool;
    if (paramEditable.length() > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void initAnimators() {
    ValueAnimator valueAnimator1 = getScaleAnimator();
    ValueAnimator valueAnimator2 = getAlphaAnimator(new float[] { 0.0F, 1.0F });
    AnimatorSet animatorSet = new AnimatorSet();
    this.iconInAnim = animatorSet;
    animatorSet.playTogether(new Animator[] { (Animator)valueAnimator1, (Animator)valueAnimator2 });
    this.iconInAnim.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          final ClearTextEndIconDelegate this$0;
          
          public void onAnimationStart(Animator param1Animator) {
            ClearTextEndIconDelegate.this.textInputLayout.setEndIconVisible(true);
          }
        });
    valueAnimator1 = getAlphaAnimator(new float[] { 1.0F, 0.0F });
    this.iconOutAnim = valueAnimator1;
    valueAnimator1.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          final ClearTextEndIconDelegate this$0;
          
          public void onAnimationEnd(Animator param1Animator) {
            ClearTextEndIconDelegate.this.textInputLayout.setEndIconVisible(false);
          }
        });
  }
  
  void initialize() {
    this.textInputLayout.setEndIconDrawable(AppCompatResources.getDrawable(this.context, R.drawable.mtrl_ic_cancel));
    this.textInputLayout.setEndIconContentDescription(this.textInputLayout.getResources().getText(R.string.clear_text_end_icon_content_description));
    this.textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
          final ClearTextEndIconDelegate this$0;
          
          public void onClick(View param1View) {
            Editable editable = ClearTextEndIconDelegate.this.textInputLayout.getEditText().getText();
            if (editable != null)
              editable.clear(); 
            ClearTextEndIconDelegate.this.textInputLayout.refreshEndIconDrawableState();
          }
        });
    this.textInputLayout.addOnEditTextAttachedListener(this.clearTextOnEditTextAttachedListener);
    this.textInputLayout.addOnEndIconChangedListener(this.endIconChangedListener);
    initAnimators();
  }
  
  void onSuffixVisibilityChanged(boolean paramBoolean) {
    if (this.textInputLayout.getSuffixText() == null)
      return; 
    animateIcon(paramBoolean);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\textfield\ClearTextEndIconDelegate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */