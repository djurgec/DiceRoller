package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.internal.DescendantOffsetUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.util.Iterator;
import java.util.List;

public class ExtendedFloatingActionButton extends MaterialButton implements CoordinatorLayout.AttachedBehavior {
  private static final int ANIM_STATE_HIDING = 1;
  
  private static final int ANIM_STATE_NONE = 0;
  
  private static final int ANIM_STATE_SHOWING = 2;
  
  private static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_ExtendedFloatingActionButton_Icon;
  
  static final Property<View, Float> HEIGHT;
  
  static final Property<View, Float> PADDING_END;
  
  static final Property<View, Float> PADDING_START;
  
  static final Property<View, Float> WIDTH = new Property<View, Float>(Float.class, "width") {
      public Float get(View param1View) {
        return Float.valueOf((param1View.getLayoutParams()).width);
      }
      
      public void set(View param1View, Float param1Float) {
        (param1View.getLayoutParams()).width = param1Float.intValue();
        param1View.requestLayout();
      }
    };
  
  private int animState = 0;
  
  private boolean animateShowBeforeLayout;
  
  private final CoordinatorLayout.Behavior<ExtendedFloatingActionButton> behavior;
  
  private final AnimatorTracker changeVisibilityTracker;
  
  private final int collapsedSize;
  
  private final MotionStrategy extendStrategy;
  
  private int extendedPaddingEnd;
  
  private int extendedPaddingStart;
  
  private final MotionStrategy hideStrategy;
  
  private boolean isExtended;
  
  private boolean isTransforming;
  
  protected ColorStateList originalTextCsl;
  
  private final MotionStrategy showStrategy;
  
  private final MotionStrategy shrinkStrategy;
  
  static {
    HEIGHT = new Property<View, Float>(Float.class, "height") {
        public Float get(View param1View) {
          return Float.valueOf((param1View.getLayoutParams()).height);
        }
        
        public void set(View param1View, Float param1Float) {
          (param1View.getLayoutParams()).height = param1Float.intValue();
          param1View.requestLayout();
        }
      };
    PADDING_START = new Property<View, Float>(Float.class, "paddingStart") {
        public Float get(View param1View) {
          return Float.valueOf(ViewCompat.getPaddingStart(param1View));
        }
        
        public void set(View param1View, Float param1Float) {
          ViewCompat.setPaddingRelative(param1View, param1Float.intValue(), param1View.getPaddingTop(), ViewCompat.getPaddingEnd(param1View), param1View.getPaddingBottom());
        }
      };
    PADDING_END = new Property<View, Float>(Float.class, "paddingEnd") {
        public Float get(View param1View) {
          return Float.valueOf(ViewCompat.getPaddingEnd(param1View));
        }
        
        public void set(View param1View, Float param1Float) {
          ViewCompat.setPaddingRelative(param1View, ViewCompat.getPaddingStart(param1View), param1View.getPaddingTop(), param1Float.intValue(), param1View.getPaddingBottom());
        }
      };
  }
  
  public ExtendedFloatingActionButton(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public ExtendedFloatingActionButton(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.extendedFloatingActionButtonStyle);
  }
  
  public ExtendedFloatingActionButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(MaterialThemeOverlay.wrap(paramContext, paramAttributeSet, paramInt, i), paramAttributeSet, paramInt);
    AnimatorTracker animatorTracker1 = new AnimatorTracker();
    this.changeVisibilityTracker = animatorTracker1;
    ShowStrategy showStrategy = new ShowStrategy(animatorTracker1);
    this.showStrategy = showStrategy;
    HideStrategy hideStrategy = new HideStrategy(animatorTracker1);
    this.hideStrategy = hideStrategy;
    this.isExtended = true;
    this.isTransforming = false;
    this.animateShowBeforeLayout = false;
    Context context = getContext();
    this.behavior = new ExtendedFloatingActionButtonBehavior<>(context, paramAttributeSet);
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(context, paramAttributeSet, R.styleable.ExtendedFloatingActionButton, paramInt, i, new int[0]);
    MotionSpec motionSpec1 = MotionSpec.createFromAttribute(context, typedArray, R.styleable.ExtendedFloatingActionButton_showMotionSpec);
    MotionSpec motionSpec3 = MotionSpec.createFromAttribute(context, typedArray, R.styleable.ExtendedFloatingActionButton_hideMotionSpec);
    MotionSpec motionSpec2 = MotionSpec.createFromAttribute(context, typedArray, R.styleable.ExtendedFloatingActionButton_extendMotionSpec);
    MotionSpec motionSpec4 = MotionSpec.createFromAttribute(context, typedArray, R.styleable.ExtendedFloatingActionButton_shrinkMotionSpec);
    this.collapsedSize = typedArray.getDimensionPixelSize(R.styleable.ExtendedFloatingActionButton_collapsedSize, -1);
    this.extendedPaddingStart = ViewCompat.getPaddingStart((View)this);
    this.extendedPaddingEnd = ViewCompat.getPaddingEnd((View)this);
    AnimatorTracker animatorTracker2 = new AnimatorTracker();
    ChangeSizeStrategy changeSizeStrategy1 = new ChangeSizeStrategy(animatorTracker2, new Size() {
          final ExtendedFloatingActionButton this$0;
          
          public int getHeight() {
            return ExtendedFloatingActionButton.this.getMeasuredHeight();
          }
          
          public ViewGroup.LayoutParams getLayoutParams() {
            return new ViewGroup.LayoutParams(-2, -2);
          }
          
          public int getPaddingEnd() {
            return ExtendedFloatingActionButton.this.extendedPaddingEnd;
          }
          
          public int getPaddingStart() {
            return ExtendedFloatingActionButton.this.extendedPaddingStart;
          }
          
          public int getWidth() {
            return ExtendedFloatingActionButton.this.getMeasuredWidth() - ExtendedFloatingActionButton.this.getCollapsedPadding() * 2 + ExtendedFloatingActionButton.this.extendedPaddingStart + ExtendedFloatingActionButton.this.extendedPaddingEnd;
          }
        }true);
    this.extendStrategy = changeSizeStrategy1;
    ChangeSizeStrategy changeSizeStrategy2 = new ChangeSizeStrategy(animatorTracker2, new Size() {
          final ExtendedFloatingActionButton this$0;
          
          public int getHeight() {
            return ExtendedFloatingActionButton.this.getCollapsedSize();
          }
          
          public ViewGroup.LayoutParams getLayoutParams() {
            return new ViewGroup.LayoutParams(getWidth(), getHeight());
          }
          
          public int getPaddingEnd() {
            return ExtendedFloatingActionButton.this.getCollapsedPadding();
          }
          
          public int getPaddingStart() {
            return ExtendedFloatingActionButton.this.getCollapsedPadding();
          }
          
          public int getWidth() {
            return ExtendedFloatingActionButton.this.getCollapsedSize();
          }
        }false);
    this.shrinkStrategy = changeSizeStrategy2;
    showStrategy.setMotionSpec(motionSpec1);
    hideStrategy.setMotionSpec(motionSpec3);
    changeSizeStrategy1.setMotionSpec(motionSpec2);
    changeSizeStrategy2.setMotionSpec(motionSpec4);
    typedArray.recycle();
    setShapeAppearanceModel(ShapeAppearanceModel.builder(context, paramAttributeSet, paramInt, i, ShapeAppearanceModel.PILL).build());
    saveOriginalTextCsl();
  }
  
  private boolean isOrWillBeHidden() {
    int i = getVisibility();
    boolean bool2 = false;
    boolean bool1 = false;
    if (i == 0) {
      if (this.animState == 1)
        bool1 = true; 
      return bool1;
    } 
    bool1 = bool2;
    if (this.animState != 2)
      bool1 = true; 
    return bool1;
  }
  
  private boolean isOrWillBeShown() {
    int i = getVisibility();
    boolean bool1 = false;
    boolean bool2 = false;
    if (i != 0) {
      bool1 = bool2;
      if (this.animState == 2)
        bool1 = true; 
      return bool1;
    } 
    if (this.animState != 1)
      bool1 = true; 
    return bool1;
  }
  
  private void performMotion(final MotionStrategy strategy, final OnChangedCallback callback) {
    if (strategy.shouldCancel())
      return; 
    if (!shouldAnimateVisibilityChange()) {
      strategy.performNow();
      strategy.onChange(callback);
      return;
    } 
    measure(0, 0);
    AnimatorSet animatorSet = strategy.createAnimator();
    animatorSet.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          private boolean cancelled;
          
          final ExtendedFloatingActionButton this$0;
          
          final ExtendedFloatingActionButton.OnChangedCallback val$callback;
          
          final MotionStrategy val$strategy;
          
          public void onAnimationCancel(Animator param1Animator) {
            this.cancelled = true;
            strategy.onAnimationCancel();
          }
          
          public void onAnimationEnd(Animator param1Animator) {
            strategy.onAnimationEnd();
            if (!this.cancelled)
              strategy.onChange(callback); 
          }
          
          public void onAnimationStart(Animator param1Animator) {
            strategy.onAnimationStart(param1Animator);
            this.cancelled = false;
          }
        });
    Iterator<Animator.AnimatorListener> iterator = strategy.getListeners().iterator();
    while (iterator.hasNext())
      animatorSet.addListener(iterator.next()); 
    animatorSet.start();
  }
  
  private void saveOriginalTextCsl() {
    this.originalTextCsl = getTextColors();
  }
  
  private boolean shouldAnimateVisibilityChange() {
    boolean bool;
    if ((ViewCompat.isLaidOut((View)this) || (!isOrWillBeShown() && this.animateShowBeforeLayout)) && !isInEditMode()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void addOnExtendAnimationListener(Animator.AnimatorListener paramAnimatorListener) {
    this.extendStrategy.addAnimationListener(paramAnimatorListener);
  }
  
  public void addOnHideAnimationListener(Animator.AnimatorListener paramAnimatorListener) {
    this.hideStrategy.addAnimationListener(paramAnimatorListener);
  }
  
  public void addOnShowAnimationListener(Animator.AnimatorListener paramAnimatorListener) {
    this.showStrategy.addAnimationListener(paramAnimatorListener);
  }
  
  public void addOnShrinkAnimationListener(Animator.AnimatorListener paramAnimatorListener) {
    this.shrinkStrategy.addAnimationListener(paramAnimatorListener);
  }
  
  public void extend() {
    performMotion(this.extendStrategy, (OnChangedCallback)null);
  }
  
  public void extend(OnChangedCallback paramOnChangedCallback) {
    performMotion(this.extendStrategy, paramOnChangedCallback);
  }
  
  public CoordinatorLayout.Behavior<ExtendedFloatingActionButton> getBehavior() {
    return this.behavior;
  }
  
  int getCollapsedPadding() {
    return (getCollapsedSize() - getIconSize()) / 2;
  }
  
  int getCollapsedSize() {
    int i = this.collapsedSize;
    if (i < 0)
      i = Math.min(ViewCompat.getPaddingStart((View)this), ViewCompat.getPaddingEnd((View)this)) * 2 + getIconSize(); 
    return i;
  }
  
  public MotionSpec getExtendMotionSpec() {
    return this.extendStrategy.getMotionSpec();
  }
  
  public MotionSpec getHideMotionSpec() {
    return this.hideStrategy.getMotionSpec();
  }
  
  public MotionSpec getShowMotionSpec() {
    return this.showStrategy.getMotionSpec();
  }
  
  public MotionSpec getShrinkMotionSpec() {
    return this.shrinkStrategy.getMotionSpec();
  }
  
  public void hide() {
    performMotion(this.hideStrategy, (OnChangedCallback)null);
  }
  
  public void hide(OnChangedCallback paramOnChangedCallback) {
    performMotion(this.hideStrategy, paramOnChangedCallback);
  }
  
  public final boolean isExtended() {
    return this.isExtended;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (this.isExtended && TextUtils.isEmpty(getText()) && getIcon() != null) {
      this.isExtended = false;
      this.shrinkStrategy.performNow();
    } 
  }
  
  public void removeOnExtendAnimationListener(Animator.AnimatorListener paramAnimatorListener) {
    this.extendStrategy.removeAnimationListener(paramAnimatorListener);
  }
  
  public void removeOnHideAnimationListener(Animator.AnimatorListener paramAnimatorListener) {
    this.hideStrategy.removeAnimationListener(paramAnimatorListener);
  }
  
  public void removeOnShowAnimationListener(Animator.AnimatorListener paramAnimatorListener) {
    this.showStrategy.removeAnimationListener(paramAnimatorListener);
  }
  
  public void removeOnShrinkAnimationListener(Animator.AnimatorListener paramAnimatorListener) {
    this.shrinkStrategy.removeAnimationListener(paramAnimatorListener);
  }
  
  public void setAnimateShowBeforeLayout(boolean paramBoolean) {
    this.animateShowBeforeLayout = paramBoolean;
  }
  
  public void setExtendMotionSpec(MotionSpec paramMotionSpec) {
    this.extendStrategy.setMotionSpec(paramMotionSpec);
  }
  
  public void setExtendMotionSpecResource(int paramInt) {
    setExtendMotionSpec(MotionSpec.createFromResource(getContext(), paramInt));
  }
  
  public void setExtended(boolean paramBoolean) {
    MotionStrategy motionStrategy;
    if (this.isExtended == paramBoolean)
      return; 
    if (paramBoolean) {
      motionStrategy = this.extendStrategy;
    } else {
      motionStrategy = this.shrinkStrategy;
    } 
    if (motionStrategy.shouldCancel())
      return; 
    motionStrategy.performNow();
  }
  
  public void setHideMotionSpec(MotionSpec paramMotionSpec) {
    this.hideStrategy.setMotionSpec(paramMotionSpec);
  }
  
  public void setHideMotionSpecResource(int paramInt) {
    setHideMotionSpec(MotionSpec.createFromResource(getContext(), paramInt));
  }
  
  public void setPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.setPadding(paramInt1, paramInt2, paramInt3, paramInt4);
    if (this.isExtended && !this.isTransforming) {
      this.extendedPaddingStart = ViewCompat.getPaddingStart((View)this);
      this.extendedPaddingEnd = ViewCompat.getPaddingEnd((View)this);
    } 
  }
  
  public void setPaddingRelative(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.setPaddingRelative(paramInt1, paramInt2, paramInt3, paramInt4);
    if (this.isExtended && !this.isTransforming) {
      this.extendedPaddingStart = paramInt1;
      this.extendedPaddingEnd = paramInt3;
    } 
  }
  
  public void setShowMotionSpec(MotionSpec paramMotionSpec) {
    this.showStrategy.setMotionSpec(paramMotionSpec);
  }
  
  public void setShowMotionSpecResource(int paramInt) {
    setShowMotionSpec(MotionSpec.createFromResource(getContext(), paramInt));
  }
  
  public void setShrinkMotionSpec(MotionSpec paramMotionSpec) {
    this.shrinkStrategy.setMotionSpec(paramMotionSpec);
  }
  
  public void setShrinkMotionSpecResource(int paramInt) {
    setShrinkMotionSpec(MotionSpec.createFromResource(getContext(), paramInt));
  }
  
  public void setTextColor(int paramInt) {
    super.setTextColor(paramInt);
    saveOriginalTextCsl();
  }
  
  public void setTextColor(ColorStateList paramColorStateList) {
    super.setTextColor(paramColorStateList);
    saveOriginalTextCsl();
  }
  
  public void show() {
    performMotion(this.showStrategy, (OnChangedCallback)null);
  }
  
  public void show(OnChangedCallback paramOnChangedCallback) {
    performMotion(this.showStrategy, paramOnChangedCallback);
  }
  
  public void shrink() {
    performMotion(this.shrinkStrategy, (OnChangedCallback)null);
  }
  
  public void shrink(OnChangedCallback paramOnChangedCallback) {
    performMotion(this.shrinkStrategy, paramOnChangedCallback);
  }
  
  protected void silentlyUpdateTextColor(ColorStateList paramColorStateList) {
    super.setTextColor(paramColorStateList);
  }
  
  class ChangeSizeStrategy extends BaseMotionStrategy {
    private final boolean extending;
    
    private final ExtendedFloatingActionButton.Size size;
    
    final ExtendedFloatingActionButton this$0;
    
    ChangeSizeStrategy(AnimatorTracker param1AnimatorTracker, ExtendedFloatingActionButton.Size param1Size, boolean param1Boolean) {
      super(ExtendedFloatingActionButton.this, param1AnimatorTracker);
      this.size = param1Size;
      this.extending = param1Boolean;
    }
    
    public AnimatorSet createAnimator() {
      MotionSpec motionSpec = getCurrentMotionSpec();
      if (motionSpec.hasPropertyValues("width")) {
        PropertyValuesHolder[] arrayOfPropertyValuesHolder = motionSpec.getPropertyValues("width");
        arrayOfPropertyValuesHolder[0].setFloatValues(new float[] { this.this$0.getWidth(), this.size.getWidth() });
        motionSpec.setPropertyValues("width", arrayOfPropertyValuesHolder);
      } 
      if (motionSpec.hasPropertyValues("height")) {
        PropertyValuesHolder[] arrayOfPropertyValuesHolder = motionSpec.getPropertyValues("height");
        arrayOfPropertyValuesHolder[0].setFloatValues(new float[] { this.this$0.getHeight(), this.size.getHeight() });
        motionSpec.setPropertyValues("height", arrayOfPropertyValuesHolder);
      } 
      if (motionSpec.hasPropertyValues("paddingStart")) {
        PropertyValuesHolder[] arrayOfPropertyValuesHolder = motionSpec.getPropertyValues("paddingStart");
        arrayOfPropertyValuesHolder[0].setFloatValues(new float[] { ViewCompat.getPaddingStart((View)this.this$0), this.size.getPaddingStart() });
        motionSpec.setPropertyValues("paddingStart", arrayOfPropertyValuesHolder);
      } 
      if (motionSpec.hasPropertyValues("paddingEnd")) {
        PropertyValuesHolder[] arrayOfPropertyValuesHolder = motionSpec.getPropertyValues("paddingEnd");
        arrayOfPropertyValuesHolder[0].setFloatValues(new float[] { ViewCompat.getPaddingEnd((View)this.this$0), this.size.getPaddingEnd() });
        motionSpec.setPropertyValues("paddingEnd", arrayOfPropertyValuesHolder);
      } 
      if (motionSpec.hasPropertyValues("labelOpacity")) {
        float f1;
        PropertyValuesHolder[] arrayOfPropertyValuesHolder = motionSpec.getPropertyValues("labelOpacity");
        boolean bool = this.extending;
        float f2 = 0.0F;
        if (bool) {
          f1 = 0.0F;
        } else {
          f1 = 1.0F;
        } 
        if (bool)
          f2 = 1.0F; 
        arrayOfPropertyValuesHolder[0].setFloatValues(new float[] { f1, f2 });
        motionSpec.setPropertyValues("labelOpacity", arrayOfPropertyValuesHolder);
      } 
      return createAnimator(motionSpec);
    }
    
    public int getDefaultMotionSpecResource() {
      int i;
      if (this.extending) {
        i = R.animator.mtrl_extended_fab_change_size_expand_motion_spec;
      } else {
        i = R.animator.mtrl_extended_fab_change_size_collapse_motion_spec;
      } 
      return i;
    }
    
    public void onAnimationEnd() {
      super.onAnimationEnd();
      ExtendedFloatingActionButton.access$802(ExtendedFloatingActionButton.this, false);
      ExtendedFloatingActionButton.this.setHorizontallyScrolling(false);
      ViewGroup.LayoutParams layoutParams = ExtendedFloatingActionButton.this.getLayoutParams();
      if (layoutParams == null)
        return; 
      layoutParams.width = (this.size.getLayoutParams()).width;
      layoutParams.height = (this.size.getLayoutParams()).height;
    }
    
    public void onAnimationStart(Animator param1Animator) {
      super.onAnimationStart(param1Animator);
      ExtendedFloatingActionButton.access$702(ExtendedFloatingActionButton.this, this.extending);
      ExtendedFloatingActionButton.access$802(ExtendedFloatingActionButton.this, true);
      ExtendedFloatingActionButton.this.setHorizontallyScrolling(true);
    }
    
    public void onChange(ExtendedFloatingActionButton.OnChangedCallback param1OnChangedCallback) {
      if (param1OnChangedCallback == null)
        return; 
      if (this.extending) {
        param1OnChangedCallback.onExtended(ExtendedFloatingActionButton.this);
      } else {
        param1OnChangedCallback.onShrunken(ExtendedFloatingActionButton.this);
      } 
    }
    
    public void performNow() {
      ExtendedFloatingActionButton.access$702(ExtendedFloatingActionButton.this, this.extending);
      ViewGroup.LayoutParams layoutParams = ExtendedFloatingActionButton.this.getLayoutParams();
      if (layoutParams == null)
        return; 
      layoutParams.width = (this.size.getLayoutParams()).width;
      layoutParams.height = (this.size.getLayoutParams()).height;
      ViewCompat.setPaddingRelative((View)ExtendedFloatingActionButton.this, this.size.getPaddingStart(), ExtendedFloatingActionButton.this.getPaddingTop(), this.size.getPaddingEnd(), ExtendedFloatingActionButton.this.getPaddingBottom());
      ExtendedFloatingActionButton.this.requestLayout();
    }
    
    public boolean shouldCancel() {
      return (this.extending == ExtendedFloatingActionButton.this.isExtended || ExtendedFloatingActionButton.this.getIcon() == null || TextUtils.isEmpty(ExtendedFloatingActionButton.this.getText()));
    }
  }
  
  protected static class ExtendedFloatingActionButtonBehavior<T extends ExtendedFloatingActionButton> extends CoordinatorLayout.Behavior<T> {
    private static final boolean AUTO_HIDE_DEFAULT = false;
    
    private static final boolean AUTO_SHRINK_DEFAULT = true;
    
    private boolean autoHideEnabled;
    
    private boolean autoShrinkEnabled;
    
    private ExtendedFloatingActionButton.OnChangedCallback internalAutoHideCallback;
    
    private ExtendedFloatingActionButton.OnChangedCallback internalAutoShrinkCallback;
    
    private Rect tmpRect;
    
    public ExtendedFloatingActionButtonBehavior() {
      this.autoHideEnabled = false;
      this.autoShrinkEnabled = true;
    }
    
    public ExtendedFloatingActionButtonBehavior(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.ExtendedFloatingActionButton_Behavior_Layout);
      this.autoHideEnabled = typedArray.getBoolean(R.styleable.ExtendedFloatingActionButton_Behavior_Layout_behavior_autoHide, false);
      this.autoShrinkEnabled = typedArray.getBoolean(R.styleable.ExtendedFloatingActionButton_Behavior_Layout_behavior_autoShrink, true);
      typedArray.recycle();
    }
    
    private static boolean isBottomSheet(View param1View) {
      ViewGroup.LayoutParams layoutParams = param1View.getLayoutParams();
      return (layoutParams instanceof CoordinatorLayout.LayoutParams) ? (((CoordinatorLayout.LayoutParams)layoutParams).getBehavior() instanceof com.google.android.material.bottomsheet.BottomSheetBehavior) : false;
    }
    
    private boolean shouldUpdateVisibility(View param1View, ExtendedFloatingActionButton param1ExtendedFloatingActionButton) {
      CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)param1ExtendedFloatingActionButton.getLayoutParams();
      return (!this.autoHideEnabled && !this.autoShrinkEnabled) ? false : (!(layoutParams.getAnchorId() != param1View.getId()));
    }
    
    private boolean updateFabVisibilityForAppBarLayout(CoordinatorLayout param1CoordinatorLayout, AppBarLayout param1AppBarLayout, ExtendedFloatingActionButton param1ExtendedFloatingActionButton) {
      if (!shouldUpdateVisibility((View)param1AppBarLayout, param1ExtendedFloatingActionButton))
        return false; 
      if (this.tmpRect == null)
        this.tmpRect = new Rect(); 
      Rect rect = this.tmpRect;
      DescendantOffsetUtils.getDescendantRect((ViewGroup)param1CoordinatorLayout, (View)param1AppBarLayout, rect);
      if (rect.bottom <= param1AppBarLayout.getMinimumHeightForVisibleOverlappingContent()) {
        shrinkOrHide(param1ExtendedFloatingActionButton);
      } else {
        extendOrShow(param1ExtendedFloatingActionButton);
      } 
      return true;
    }
    
    private boolean updateFabVisibilityForBottomSheet(View param1View, ExtendedFloatingActionButton param1ExtendedFloatingActionButton) {
      if (!shouldUpdateVisibility(param1View, param1ExtendedFloatingActionButton))
        return false; 
      CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)param1ExtendedFloatingActionButton.getLayoutParams();
      if (param1View.getTop() < param1ExtendedFloatingActionButton.getHeight() / 2 + layoutParams.topMargin) {
        shrinkOrHide(param1ExtendedFloatingActionButton);
      } else {
        extendOrShow(param1ExtendedFloatingActionButton);
      } 
      return true;
    }
    
    protected void extendOrShow(ExtendedFloatingActionButton param1ExtendedFloatingActionButton) {
      ExtendedFloatingActionButton.OnChangedCallback onChangedCallback;
      MotionStrategy motionStrategy;
      boolean bool = this.autoShrinkEnabled;
      if (bool) {
        onChangedCallback = this.internalAutoShrinkCallback;
      } else {
        onChangedCallback = this.internalAutoHideCallback;
      } 
      if (bool) {
        motionStrategy = param1ExtendedFloatingActionButton.extendStrategy;
      } else {
        motionStrategy = param1ExtendedFloatingActionButton.showStrategy;
      } 
      param1ExtendedFloatingActionButton.performMotion(motionStrategy, onChangedCallback);
    }
    
    public boolean getInsetDodgeRect(CoordinatorLayout param1CoordinatorLayout, ExtendedFloatingActionButton param1ExtendedFloatingActionButton, Rect param1Rect) {
      return super.getInsetDodgeRect(param1CoordinatorLayout, (View)param1ExtendedFloatingActionButton, param1Rect);
    }
    
    public boolean isAutoHideEnabled() {
      return this.autoHideEnabled;
    }
    
    public boolean isAutoShrinkEnabled() {
      return this.autoShrinkEnabled;
    }
    
    public void onAttachedToLayoutParams(CoordinatorLayout.LayoutParams param1LayoutParams) {
      if (param1LayoutParams.dodgeInsetEdges == 0)
        param1LayoutParams.dodgeInsetEdges = 80; 
    }
    
    public boolean onDependentViewChanged(CoordinatorLayout param1CoordinatorLayout, ExtendedFloatingActionButton param1ExtendedFloatingActionButton, View param1View) {
      if (param1View instanceof AppBarLayout) {
        updateFabVisibilityForAppBarLayout(param1CoordinatorLayout, (AppBarLayout)param1View, param1ExtendedFloatingActionButton);
      } else if (isBottomSheet(param1View)) {
        updateFabVisibilityForBottomSheet(param1View, param1ExtendedFloatingActionButton);
      } 
      return false;
    }
    
    public boolean onLayoutChild(CoordinatorLayout param1CoordinatorLayout, ExtendedFloatingActionButton param1ExtendedFloatingActionButton, int param1Int) {
      List<View> list = param1CoordinatorLayout.getDependencies((View)param1ExtendedFloatingActionButton);
      byte b = 0;
      int i = list.size();
      while (b < i) {
        View view = list.get(b);
        if ((view instanceof AppBarLayout) ? updateFabVisibilityForAppBarLayout(param1CoordinatorLayout, (AppBarLayout)view, param1ExtendedFloatingActionButton) : (isBottomSheet(view) && updateFabVisibilityForBottomSheet(view, param1ExtendedFloatingActionButton)))
          break; 
        b++;
      } 
      param1CoordinatorLayout.onLayoutChild((View)param1ExtendedFloatingActionButton, param1Int);
      return true;
    }
    
    public void setAutoHideEnabled(boolean param1Boolean) {
      this.autoHideEnabled = param1Boolean;
    }
    
    public void setAutoShrinkEnabled(boolean param1Boolean) {
      this.autoShrinkEnabled = param1Boolean;
    }
    
    void setInternalAutoHideCallback(ExtendedFloatingActionButton.OnChangedCallback param1OnChangedCallback) {
      this.internalAutoHideCallback = param1OnChangedCallback;
    }
    
    void setInternalAutoShrinkCallback(ExtendedFloatingActionButton.OnChangedCallback param1OnChangedCallback) {
      this.internalAutoShrinkCallback = param1OnChangedCallback;
    }
    
    protected void shrinkOrHide(ExtendedFloatingActionButton param1ExtendedFloatingActionButton) {
      ExtendedFloatingActionButton.OnChangedCallback onChangedCallback;
      MotionStrategy motionStrategy;
      boolean bool = this.autoShrinkEnabled;
      if (bool) {
        onChangedCallback = this.internalAutoShrinkCallback;
      } else {
        onChangedCallback = this.internalAutoHideCallback;
      } 
      if (bool) {
        motionStrategy = param1ExtendedFloatingActionButton.shrinkStrategy;
      } else {
        motionStrategy = param1ExtendedFloatingActionButton.hideStrategy;
      } 
      param1ExtendedFloatingActionButton.performMotion(motionStrategy, onChangedCallback);
    }
  }
  
  class HideStrategy extends BaseMotionStrategy {
    private boolean isCancelled;
    
    final ExtendedFloatingActionButton this$0;
    
    public HideStrategy(AnimatorTracker param1AnimatorTracker) {
      super(ExtendedFloatingActionButton.this, param1AnimatorTracker);
    }
    
    public int getDefaultMotionSpecResource() {
      return R.animator.mtrl_extended_fab_hide_motion_spec;
    }
    
    public void onAnimationCancel() {
      super.onAnimationCancel();
      this.isCancelled = true;
    }
    
    public void onAnimationEnd() {
      super.onAnimationEnd();
      ExtendedFloatingActionButton.access$902(ExtendedFloatingActionButton.this, 0);
      if (!this.isCancelled)
        ExtendedFloatingActionButton.this.setVisibility(8); 
    }
    
    public void onAnimationStart(Animator param1Animator) {
      super.onAnimationStart(param1Animator);
      this.isCancelled = false;
      ExtendedFloatingActionButton.this.setVisibility(0);
      ExtendedFloatingActionButton.access$902(ExtendedFloatingActionButton.this, 1);
    }
    
    public void onChange(ExtendedFloatingActionButton.OnChangedCallback param1OnChangedCallback) {
      if (param1OnChangedCallback != null)
        param1OnChangedCallback.onHidden(ExtendedFloatingActionButton.this); 
    }
    
    public void performNow() {
      ExtendedFloatingActionButton.this.setVisibility(8);
    }
    
    public boolean shouldCancel() {
      return ExtendedFloatingActionButton.this.isOrWillBeHidden();
    }
  }
  
  public static abstract class OnChangedCallback {
    public void onExtended(ExtendedFloatingActionButton param1ExtendedFloatingActionButton) {}
    
    public void onHidden(ExtendedFloatingActionButton param1ExtendedFloatingActionButton) {}
    
    public void onShown(ExtendedFloatingActionButton param1ExtendedFloatingActionButton) {}
    
    public void onShrunken(ExtendedFloatingActionButton param1ExtendedFloatingActionButton) {}
  }
  
  class ShowStrategy extends BaseMotionStrategy {
    final ExtendedFloatingActionButton this$0;
    
    public ShowStrategy(AnimatorTracker param1AnimatorTracker) {
      super(ExtendedFloatingActionButton.this, param1AnimatorTracker);
    }
    
    public int getDefaultMotionSpecResource() {
      return R.animator.mtrl_extended_fab_show_motion_spec;
    }
    
    public void onAnimationEnd() {
      super.onAnimationEnd();
      ExtendedFloatingActionButton.access$902(ExtendedFloatingActionButton.this, 0);
    }
    
    public void onAnimationStart(Animator param1Animator) {
      super.onAnimationStart(param1Animator);
      ExtendedFloatingActionButton.this.setVisibility(0);
      ExtendedFloatingActionButton.access$902(ExtendedFloatingActionButton.this, 2);
    }
    
    public void onChange(ExtendedFloatingActionButton.OnChangedCallback param1OnChangedCallback) {
      if (param1OnChangedCallback != null)
        param1OnChangedCallback.onShown(ExtendedFloatingActionButton.this); 
    }
    
    public void performNow() {
      ExtendedFloatingActionButton.this.setVisibility(0);
      ExtendedFloatingActionButton.this.setAlpha(1.0F);
      ExtendedFloatingActionButton.this.setScaleY(1.0F);
      ExtendedFloatingActionButton.this.setScaleX(1.0F);
    }
    
    public boolean shouldCancel() {
      return ExtendedFloatingActionButton.this.isOrWillBeShown();
    }
  }
  
  static interface Size {
    int getHeight();
    
    ViewGroup.LayoutParams getLayoutParams();
    
    int getPaddingEnd();
    
    int getPaddingStart();
    
    int getWidth();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\floatingactionbutton\ExtendedFloatingActionButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */