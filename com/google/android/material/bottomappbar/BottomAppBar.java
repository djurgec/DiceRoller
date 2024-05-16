package com.google.android.material.bottomappbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R;
import com.google.android.material.animation.TransformationCallback;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BottomAppBar extends Toolbar implements CoordinatorLayout.AttachedBehavior {
  private static final long ANIMATION_DURATION = 300L;
  
  private static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_BottomAppBar;
  
  public static final int FAB_ALIGNMENT_MODE_CENTER = 0;
  
  public static final int FAB_ALIGNMENT_MODE_END = 1;
  
  public static final int FAB_ANIMATION_MODE_SCALE = 0;
  
  public static final int FAB_ANIMATION_MODE_SLIDE = 1;
  
  private static final int NO_MENU_RES_ID = 0;
  
  private int animatingModeChangeCounter;
  
  private ArrayList<AnimationListener> animationListeners;
  
  private Behavior behavior;
  
  private int bottomInset;
  
  private int fabAlignmentMode;
  
  AnimatorListenerAdapter fabAnimationListener;
  
  private int fabAnimationMode;
  
  private boolean fabAttached;
  
  private final int fabOffsetEndMode;
  
  TransformationCallback<FloatingActionButton> fabTransformationCallback;
  
  private boolean hideOnScroll;
  
  private int leftInset;
  
  private final MaterialShapeDrawable materialShapeDrawable;
  
  private boolean menuAnimatingWithFabAlignmentMode;
  
  private Animator menuAnimator;
  
  private Animator modeAnimator;
  
  private final boolean paddingBottomSystemWindowInsets;
  
  private final boolean paddingLeftSystemWindowInsets;
  
  private final boolean paddingRightSystemWindowInsets;
  
  private int pendingMenuResId;
  
  private int rightInset;
  
  public BottomAppBar(Context paramContext) {
    this(paramContext, (AttributeSet)null, 0);
  }
  
  public BottomAppBar(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.bottomAppBarStyle);
  }
  
  public BottomAppBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(MaterialThemeOverlay.wrap(paramContext, paramAttributeSet, paramInt, i), paramAttributeSet, paramInt);
    MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
    this.materialShapeDrawable = materialShapeDrawable;
    this.animatingModeChangeCounter = 0;
    this.pendingMenuResId = 0;
    this.menuAnimatingWithFabAlignmentMode = false;
    this.fabAttached = true;
    this.fabAnimationListener = new AnimatorListenerAdapter() {
        final BottomAppBar this$0;
        
        public void onAnimationStart(Animator param1Animator) {
          if (!BottomAppBar.this.menuAnimatingWithFabAlignmentMode) {
            BottomAppBar bottomAppBar = BottomAppBar.this;
            bottomAppBar.maybeAnimateMenuView(bottomAppBar.fabAlignmentMode, BottomAppBar.this.fabAttached);
          } 
        }
      };
    this.fabTransformationCallback = new TransformationCallback<FloatingActionButton>() {
        final BottomAppBar this$0;
        
        public void onScaleChanged(FloatingActionButton param1FloatingActionButton) {
          float f;
          MaterialShapeDrawable materialShapeDrawable = BottomAppBar.this.materialShapeDrawable;
          if (param1FloatingActionButton.getVisibility() == 0) {
            f = param1FloatingActionButton.getScaleY();
          } else {
            f = 0.0F;
          } 
          materialShapeDrawable.setInterpolation(f);
        }
        
        public void onTranslationChanged(FloatingActionButton param1FloatingActionButton) {
          float f1 = param1FloatingActionButton.getTranslationX();
          if (BottomAppBar.this.getTopEdgeTreatment().getHorizontalOffset() != f1) {
            BottomAppBar.this.getTopEdgeTreatment().setHorizontalOffset(f1);
            BottomAppBar.this.materialShapeDrawable.invalidateSelf();
          } 
          float f2 = -param1FloatingActionButton.getTranslationY();
          f1 = 0.0F;
          f2 = Math.max(0.0F, f2);
          if (BottomAppBar.this.getTopEdgeTreatment().getCradleVerticalOffset() != f2) {
            BottomAppBar.this.getTopEdgeTreatment().setCradleVerticalOffset(f2);
            BottomAppBar.this.materialShapeDrawable.invalidateSelf();
          } 
          MaterialShapeDrawable materialShapeDrawable = BottomAppBar.this.materialShapeDrawable;
          if (param1FloatingActionButton.getVisibility() == 0)
            f1 = param1FloatingActionButton.getScaleY(); 
          materialShapeDrawable.setInterpolation(f1);
        }
      };
    Context context = getContext();
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(context, paramAttributeSet, R.styleable.BottomAppBar, paramInt, i, new int[0]);
    ColorStateList colorStateList = MaterialResources.getColorStateList(context, typedArray, R.styleable.BottomAppBar_backgroundTint);
    int j = typedArray.getDimensionPixelSize(R.styleable.BottomAppBar_elevation, 0);
    float f3 = typedArray.getDimensionPixelOffset(R.styleable.BottomAppBar_fabCradleMargin, 0);
    float f2 = typedArray.getDimensionPixelOffset(R.styleable.BottomAppBar_fabCradleRoundedCornerRadius, 0);
    float f1 = typedArray.getDimensionPixelOffset(R.styleable.BottomAppBar_fabCradleVerticalOffset, 0);
    this.fabAlignmentMode = typedArray.getInt(R.styleable.BottomAppBar_fabAlignmentMode, 0);
    this.fabAnimationMode = typedArray.getInt(R.styleable.BottomAppBar_fabAnimationMode, 0);
    this.hideOnScroll = typedArray.getBoolean(R.styleable.BottomAppBar_hideOnScroll, false);
    this.paddingBottomSystemWindowInsets = typedArray.getBoolean(R.styleable.BottomAppBar_paddingBottomSystemWindowInsets, false);
    this.paddingLeftSystemWindowInsets = typedArray.getBoolean(R.styleable.BottomAppBar_paddingLeftSystemWindowInsets, false);
    this.paddingRightSystemWindowInsets = typedArray.getBoolean(R.styleable.BottomAppBar_paddingRightSystemWindowInsets, false);
    typedArray.recycle();
    this.fabOffsetEndMode = getResources().getDimensionPixelOffset(R.dimen.mtrl_bottomappbar_fabOffsetEndMode);
    BottomAppBarTopEdgeTreatment bottomAppBarTopEdgeTreatment = new BottomAppBarTopEdgeTreatment(f3, f2, f1);
    materialShapeDrawable.setShapeAppearanceModel(ShapeAppearanceModel.builder().setTopEdge(bottomAppBarTopEdgeTreatment).build());
    materialShapeDrawable.setShadowCompatibilityMode(2);
    materialShapeDrawable.setPaintStyle(Paint.Style.FILL);
    materialShapeDrawable.initializeElevationOverlay(context);
    setElevation(j);
    DrawableCompat.setTintList((Drawable)materialShapeDrawable, colorStateList);
    ViewCompat.setBackground((View)this, (Drawable)materialShapeDrawable);
    ViewUtils.doOnApplyWindowInsets((View)this, paramAttributeSet, paramInt, i, new ViewUtils.OnApplyWindowInsetsListener() {
          final BottomAppBar this$0;
          
          public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat, ViewUtils.RelativePadding param1RelativePadding) {
            boolean bool2 = false;
            boolean bool4 = false;
            if (BottomAppBar.this.paddingBottomSystemWindowInsets)
              BottomAppBar.access$702(BottomAppBar.this, param1WindowInsetsCompat.getSystemWindowInsetBottom()); 
            boolean bool = BottomAppBar.this.paddingLeftSystemWindowInsets;
            boolean bool3 = true;
            if (bool) {
              boolean bool5;
              if (BottomAppBar.this.leftInset != param1WindowInsetsCompat.getSystemWindowInsetLeft()) {
                bool5 = true;
              } else {
                bool5 = false;
              } 
              BottomAppBar.access$902(BottomAppBar.this, param1WindowInsetsCompat.getSystemWindowInsetLeft());
              bool2 = bool5;
            } 
            boolean bool1 = bool4;
            if (BottomAppBar.this.paddingRightSystemWindowInsets) {
              if (BottomAppBar.this.rightInset != param1WindowInsetsCompat.getSystemWindowInsetRight()) {
                bool1 = bool3;
              } else {
                bool1 = false;
              } 
              BottomAppBar.access$1102(BottomAppBar.this, param1WindowInsetsCompat.getSystemWindowInsetRight());
            } 
            if (bool2 || bool1) {
              BottomAppBar.this.cancelAnimations();
              BottomAppBar.this.setCutoutState();
              BottomAppBar.this.setActionMenuViewPosition();
            } 
            return param1WindowInsetsCompat;
          }
        });
  }
  
  private void addFabAnimationListeners(FloatingActionButton paramFloatingActionButton) {
    paramFloatingActionButton.addOnHideAnimationListener((Animator.AnimatorListener)this.fabAnimationListener);
    paramFloatingActionButton.addOnShowAnimationListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          final BottomAppBar this$0;
          
          public void onAnimationStart(Animator param1Animator) {
            BottomAppBar.this.fabAnimationListener.onAnimationStart(param1Animator);
            FloatingActionButton floatingActionButton = BottomAppBar.this.findDependentFab();
            if (floatingActionButton != null)
              floatingActionButton.setTranslationX(BottomAppBar.this.getFabTranslationX()); 
          }
        });
    paramFloatingActionButton.addTransformationCallback(this.fabTransformationCallback);
  }
  
  private void cancelAnimations() {
    Animator animator = this.menuAnimator;
    if (animator != null)
      animator.cancel(); 
    animator = this.modeAnimator;
    if (animator != null)
      animator.cancel(); 
  }
  
  private void createFabTranslationXAnimation(int paramInt, List<Animator> paramList) {
    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(findDependentFab(), "translationX", new float[] { getFabTranslationX(paramInt) });
    objectAnimator.setDuration(300L);
    paramList.add(objectAnimator);
  }
  
  private void createMenuViewTranslationAnimation(final int targetMode, final boolean targetAttached, List<Animator> paramList) {
    AnimatorSet animatorSet;
    final ActionMenuView actionMenuView = getActionMenuView();
    if (actionMenuView == null)
      return; 
    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(actionMenuView, "alpha", new float[] { 1.0F });
    if (Math.abs(actionMenuView.getTranslationX() - getActionMenuViewTranslationX(actionMenuView, targetMode, targetAttached)) > 1.0F) {
      ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(actionMenuView, "alpha", new float[] { 0.0F });
      objectAnimator1.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            public boolean cancelled;
            
            final BottomAppBar this$0;
            
            final ActionMenuView val$actionMenuView;
            
            final boolean val$targetAttached;
            
            final int val$targetMode;
            
            public void onAnimationCancel(Animator param1Animator) {
              this.cancelled = true;
            }
            
            public void onAnimationEnd(Animator param1Animator) {
              if (!this.cancelled) {
                boolean bool;
                if (BottomAppBar.this.pendingMenuResId != 0) {
                  bool = true;
                } else {
                  bool = false;
                } 
                BottomAppBar bottomAppBar = BottomAppBar.this;
                bottomAppBar.replaceMenu(bottomAppBar.pendingMenuResId);
                BottomAppBar.this.translateActionMenuView(actionMenuView, targetMode, targetAttached, bool);
              } 
            }
          });
      animatorSet = new AnimatorSet();
      animatorSet.setDuration(150L);
      animatorSet.playSequentially(new Animator[] { (Animator)objectAnimator1, (Animator)objectAnimator });
      paramList.add(animatorSet);
    } else if (animatorSet.getAlpha() < 1.0F) {
      paramList.add(objectAnimator);
    } 
  }
  
  private void dispatchAnimationEnd() {
    int i = this.animatingModeChangeCounter - 1;
    this.animatingModeChangeCounter = i;
    if (i == 0) {
      ArrayList<AnimationListener> arrayList = this.animationListeners;
      if (arrayList != null) {
        Iterator<AnimationListener> iterator = arrayList.iterator();
        while (iterator.hasNext())
          ((AnimationListener)iterator.next()).onAnimationEnd(this); 
      } 
    } 
  }
  
  private void dispatchAnimationStart() {
    int i = this.animatingModeChangeCounter;
    this.animatingModeChangeCounter = i + 1;
    if (i == 0) {
      ArrayList<AnimationListener> arrayList = this.animationListeners;
      if (arrayList != null) {
        Iterator<AnimationListener> iterator = arrayList.iterator();
        while (iterator.hasNext())
          ((AnimationListener)iterator.next()).onAnimationStart(this); 
      } 
    } 
  }
  
  private FloatingActionButton findDependentFab() {
    View view = findDependentView();
    if (view instanceof FloatingActionButton) {
      FloatingActionButton floatingActionButton = (FloatingActionButton)view;
    } else {
      view = null;
    } 
    return (FloatingActionButton)view;
  }
  
  private View findDependentView() {
    if (!(getParent() instanceof CoordinatorLayout))
      return null; 
    for (View view : ((CoordinatorLayout)getParent()).getDependents((View)this)) {
      if (view instanceof FloatingActionButton || view instanceof com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton)
        return view; 
    } 
    return null;
  }
  
  private ActionMenuView getActionMenuView() {
    for (byte b = 0; b < getChildCount(); b++) {
      View view = getChildAt(b);
      if (view instanceof ActionMenuView)
        return (ActionMenuView)view; 
    } 
    return null;
  }
  
  private int getBottomInset() {
    return this.bottomInset;
  }
  
  private float getFabTranslationX() {
    return getFabTranslationX(this.fabAlignmentMode);
  }
  
  private float getFabTranslationX(int paramInt) {
    boolean bool = ViewUtils.isLayoutRtl((View)this);
    byte b = 1;
    if (paramInt == 1) {
      if (bool) {
        paramInt = this.leftInset;
      } else {
        paramInt = this.rightInset;
      } 
      int j = this.fabOffsetEndMode;
      int i = getMeasuredWidth() / 2;
      if (bool)
        b = -1; 
      return ((i - j + paramInt) * b);
    } 
    return 0.0F;
  }
  
  private float getFabTranslationY() {
    return -getTopEdgeTreatment().getCradleVerticalOffset();
  }
  
  private int getLeftInset() {
    return this.leftInset;
  }
  
  private int getRightInset() {
    return this.rightInset;
  }
  
  private BottomAppBarTopEdgeTreatment getTopEdgeTreatment() {
    return (BottomAppBarTopEdgeTreatment)this.materialShapeDrawable.getShapeAppearanceModel().getTopEdge();
  }
  
  private boolean isFabVisibleOrWillBeShown() {
    boolean bool;
    FloatingActionButton floatingActionButton = findDependentFab();
    if (floatingActionButton != null && floatingActionButton.isOrWillBeShown()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void maybeAnimateMenuView(int paramInt, boolean paramBoolean) {
    if (!ViewCompat.isLaidOut((View)this)) {
      this.menuAnimatingWithFabAlignmentMode = false;
      replaceMenu(this.pendingMenuResId);
      return;
    } 
    Animator animator = this.menuAnimator;
    if (animator != null)
      animator.cancel(); 
    ArrayList<Animator> arrayList = new ArrayList();
    if (!isFabVisibleOrWillBeShown()) {
      paramInt = 0;
      paramBoolean = false;
    } 
    createMenuViewTranslationAnimation(paramInt, paramBoolean, arrayList);
    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(arrayList);
    this.menuAnimator = (Animator)animatorSet;
    animatorSet.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          final BottomAppBar this$0;
          
          public void onAnimationEnd(Animator param1Animator) {
            BottomAppBar.this.dispatchAnimationEnd();
            BottomAppBar.access$002(BottomAppBar.this, false);
            BottomAppBar.access$1902(BottomAppBar.this, (Animator)null);
          }
          
          public void onAnimationStart(Animator param1Animator) {
            BottomAppBar.this.dispatchAnimationStart();
          }
        });
    this.menuAnimator.start();
  }
  
  private void maybeAnimateModeChange(int paramInt) {
    if (this.fabAlignmentMode == paramInt || !ViewCompat.isLaidOut((View)this))
      return; 
    Animator animator = this.modeAnimator;
    if (animator != null)
      animator.cancel(); 
    ArrayList<Animator> arrayList = new ArrayList();
    if (this.fabAnimationMode == 1) {
      createFabTranslationXAnimation(paramInt, arrayList);
    } else {
      createFabDefaultXAnimation(paramInt, arrayList);
    } 
    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(arrayList);
    this.modeAnimator = (Animator)animatorSet;
    animatorSet.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          final BottomAppBar this$0;
          
          public void onAnimationEnd(Animator param1Animator) {
            BottomAppBar.this.dispatchAnimationEnd();
            BottomAppBar.access$1702(BottomAppBar.this, (Animator)null);
          }
          
          public void onAnimationStart(Animator param1Animator) {
            BottomAppBar.this.dispatchAnimationStart();
          }
        });
    this.modeAnimator.start();
  }
  
  private void setActionMenuViewPosition() {
    ActionMenuView actionMenuView = getActionMenuView();
    if (actionMenuView != null && this.menuAnimator == null) {
      actionMenuView.setAlpha(1.0F);
      if (!isFabVisibleOrWillBeShown()) {
        translateActionMenuView(actionMenuView, 0, false);
      } else {
        translateActionMenuView(actionMenuView, this.fabAlignmentMode, this.fabAttached);
      } 
    } 
  }
  
  private void setCutoutState() {
    float f;
    getTopEdgeTreatment().setHorizontalOffset(getFabTranslationX());
    View view = findDependentView();
    MaterialShapeDrawable materialShapeDrawable = this.materialShapeDrawable;
    if (this.fabAttached && isFabVisibleOrWillBeShown()) {
      f = 1.0F;
    } else {
      f = 0.0F;
    } 
    materialShapeDrawable.setInterpolation(f);
    if (view != null) {
      view.setTranslationY(getFabTranslationY());
      view.setTranslationX(getFabTranslationX());
    } 
  }
  
  private void translateActionMenuView(ActionMenuView paramActionMenuView, int paramInt, boolean paramBoolean) {
    translateActionMenuView(paramActionMenuView, paramInt, paramBoolean, false);
  }
  
  private void translateActionMenuView(final ActionMenuView actionMenuView, final int fabAlignmentMode, final boolean fabAttached, boolean paramBoolean2) {
    Runnable runnable = new Runnable() {
        final BottomAppBar this$0;
        
        final ActionMenuView val$actionMenuView;
        
        final int val$fabAlignmentMode;
        
        final boolean val$fabAttached;
        
        public void run() {
          ActionMenuView actionMenuView = actionMenuView;
          actionMenuView.setTranslationX(BottomAppBar.this.getActionMenuViewTranslationX(actionMenuView, fabAlignmentMode, fabAttached));
        }
      };
    if (paramBoolean2) {
      actionMenuView.post(runnable);
    } else {
      runnable.run();
    } 
  }
  
  void addAnimationListener(AnimationListener paramAnimationListener) {
    if (this.animationListeners == null)
      this.animationListeners = new ArrayList<>(); 
    this.animationListeners.add(paramAnimationListener);
  }
  
  protected void createFabDefaultXAnimation(final int targetMode, List<Animator> paramList) {
    FloatingActionButton floatingActionButton = findDependentFab();
    if (floatingActionButton == null || floatingActionButton.isOrWillBeHidden())
      return; 
    dispatchAnimationStart();
    floatingActionButton.hide(new FloatingActionButton.OnVisibilityChangedListener() {
          final BottomAppBar this$0;
          
          final int val$targetMode;
          
          public void onHidden(FloatingActionButton param1FloatingActionButton) {
            param1FloatingActionButton.setTranslationX(BottomAppBar.this.getFabTranslationX(targetMode));
            param1FloatingActionButton.show(new FloatingActionButton.OnVisibilityChangedListener() {
                  final BottomAppBar.null this$1;
                  
                  public void onShown(FloatingActionButton param2FloatingActionButton) {
                    BottomAppBar.this.dispatchAnimationEnd();
                  }
                });
          }
        });
  }
  
  protected int getActionMenuViewTranslationX(ActionMenuView paramActionMenuView, int paramInt, boolean paramBoolean) {
    int j;
    if (paramInt != 1 || !paramBoolean)
      return 0; 
    paramBoolean = ViewUtils.isLayoutRtl((View)this);
    if (paramBoolean) {
      paramInt = getMeasuredWidth();
    } else {
      paramInt = 0;
    } 
    int i = 0;
    while (i < getChildCount()) {
      boolean bool;
      View view = getChildAt(i);
      if (view.getLayoutParams() instanceof Toolbar.LayoutParams && (((Toolbar.LayoutParams)view.getLayoutParams()).gravity & 0x800007) == 8388611) {
        bool = true;
      } else {
        bool = false;
      } 
      j = paramInt;
      if (bool) {
        if (paramBoolean) {
          paramInt = Math.min(paramInt, view.getLeft());
        } else {
          paramInt = Math.max(paramInt, view.getRight());
        } 
        j = paramInt;
      } 
      i++;
      paramInt = j;
    } 
    if (paramBoolean) {
      i = paramActionMenuView.getRight();
    } else {
      i = paramActionMenuView.getLeft();
    } 
    if (paramBoolean) {
      j = this.rightInset;
    } else {
      j = -this.leftInset;
    } 
    return paramInt - i + j;
  }
  
  public ColorStateList getBackgroundTint() {
    return this.materialShapeDrawable.getTintList();
  }
  
  public Behavior getBehavior() {
    if (this.behavior == null)
      this.behavior = new Behavior(); 
    return this.behavior;
  }
  
  public float getCradleVerticalOffset() {
    return getTopEdgeTreatment().getCradleVerticalOffset();
  }
  
  public int getFabAlignmentMode() {
    return this.fabAlignmentMode;
  }
  
  public int getFabAnimationMode() {
    return this.fabAnimationMode;
  }
  
  public float getFabCradleMargin() {
    return getTopEdgeTreatment().getFabCradleMargin();
  }
  
  public float getFabCradleRoundedCornerRadius() {
    return getTopEdgeTreatment().getFabCradleRoundedCornerRadius();
  }
  
  public boolean getHideOnScroll() {
    return this.hideOnScroll;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    MaterialShapeUtils.setParentAbsoluteElevation((View)this, this.materialShapeDrawable);
    if (getParent() instanceof ViewGroup)
      ((ViewGroup)getParent()).setClipChildren(false); 
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramBoolean) {
      cancelAnimations();
      setCutoutState();
    } 
    setActionMenuViewPosition();
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    this.fabAlignmentMode = savedState.fabAlignmentMode;
    this.fabAttached = savedState.fabAttached;
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.fabAlignmentMode = this.fabAlignmentMode;
    savedState.fabAttached = this.fabAttached;
    return (Parcelable)savedState;
  }
  
  public void performHide() {
    getBehavior().slideDown((View)this);
  }
  
  public void performShow() {
    getBehavior().slideUp((View)this);
  }
  
  void removeAnimationListener(AnimationListener paramAnimationListener) {
    ArrayList<AnimationListener> arrayList = this.animationListeners;
    if (arrayList == null)
      return; 
    arrayList.remove(paramAnimationListener);
  }
  
  public void replaceMenu(int paramInt) {
    if (paramInt != 0) {
      this.pendingMenuResId = 0;
      getMenu().clear();
      inflateMenu(paramInt);
    } 
  }
  
  public void setBackgroundTint(ColorStateList paramColorStateList) {
    DrawableCompat.setTintList((Drawable)this.materialShapeDrawable, paramColorStateList);
  }
  
  public void setCradleVerticalOffset(float paramFloat) {
    if (paramFloat != getCradleVerticalOffset()) {
      getTopEdgeTreatment().setCradleVerticalOffset(paramFloat);
      this.materialShapeDrawable.invalidateSelf();
      setCutoutState();
    } 
  }
  
  public void setElevation(float paramFloat) {
    this.materialShapeDrawable.setElevation(paramFloat);
    int j = this.materialShapeDrawable.getShadowRadius();
    int i = this.materialShapeDrawable.getShadowOffsetY();
    getBehavior().setAdditionalHiddenOffsetY((View)this, j - i);
  }
  
  public void setFabAlignmentMode(int paramInt) {
    setFabAlignmentModeAndReplaceMenu(paramInt, 0);
  }
  
  public void setFabAlignmentModeAndReplaceMenu(int paramInt1, int paramInt2) {
    this.pendingMenuResId = paramInt2;
    this.menuAnimatingWithFabAlignmentMode = true;
    maybeAnimateMenuView(paramInt1, this.fabAttached);
    maybeAnimateModeChange(paramInt1);
    this.fabAlignmentMode = paramInt1;
  }
  
  public void setFabAnimationMode(int paramInt) {
    this.fabAnimationMode = paramInt;
  }
  
  void setFabCornerSize(float paramFloat) {
    if (paramFloat != getTopEdgeTreatment().getFabCornerRadius()) {
      getTopEdgeTreatment().setFabCornerSize(paramFloat);
      this.materialShapeDrawable.invalidateSelf();
    } 
  }
  
  public void setFabCradleMargin(float paramFloat) {
    if (paramFloat != getFabCradleMargin()) {
      getTopEdgeTreatment().setFabCradleMargin(paramFloat);
      this.materialShapeDrawable.invalidateSelf();
    } 
  }
  
  public void setFabCradleRoundedCornerRadius(float paramFloat) {
    if (paramFloat != getFabCradleRoundedCornerRadius()) {
      getTopEdgeTreatment().setFabCradleRoundedCornerRadius(paramFloat);
      this.materialShapeDrawable.invalidateSelf();
    } 
  }
  
  boolean setFabDiameter(int paramInt) {
    if (paramInt != getTopEdgeTreatment().getFabDiameter()) {
      getTopEdgeTreatment().setFabDiameter(paramInt);
      this.materialShapeDrawable.invalidateSelf();
      return true;
    } 
    return false;
  }
  
  public void setHideOnScroll(boolean paramBoolean) {
    this.hideOnScroll = paramBoolean;
  }
  
  public void setSubtitle(CharSequence paramCharSequence) {}
  
  public void setTitle(CharSequence paramCharSequence) {}
  
  static interface AnimationListener {
    void onAnimationEnd(BottomAppBar param1BottomAppBar);
    
    void onAnimationStart(BottomAppBar param1BottomAppBar);
  }
  
  public static class Behavior extends HideBottomViewOnScrollBehavior<BottomAppBar> {
    private final Rect fabContentRect = new Rect();
    
    private final View.OnLayoutChangeListener fabLayoutListener = new View.OnLayoutChangeListener() {
        final BottomAppBar.Behavior this$0;
        
        public void onLayoutChange(View param2View, int param2Int1, int param2Int2, int param2Int3, int param2Int4, int param2Int5, int param2Int6, int param2Int7, int param2Int8) {
          BottomAppBar bottomAppBar = BottomAppBar.Behavior.this.viewRef.get();
          if (bottomAppBar == null || !(param2View instanceof FloatingActionButton)) {
            param2View.removeOnLayoutChangeListener(this);
            return;
          } 
          FloatingActionButton floatingActionButton = (FloatingActionButton)param2View;
          floatingActionButton.getMeasuredContentRect(BottomAppBar.Behavior.this.fabContentRect);
          param2Int1 = BottomAppBar.Behavior.this.fabContentRect.height();
          bottomAppBar.setFabDiameter(param2Int1);
          bottomAppBar.setFabCornerSize(floatingActionButton.getShapeAppearanceModel().getTopLeftCornerSize().getCornerSize(new RectF(BottomAppBar.Behavior.this.fabContentRect)));
          CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)param2View.getLayoutParams();
          if (BottomAppBar.Behavior.this.originalBottomMargin == 0) {
            param2Int1 = (floatingActionButton.getMeasuredHeight() - param2Int1) / 2;
            param2Int2 = bottomAppBar.getResources().getDimensionPixelOffset(R.dimen.mtrl_bottomappbar_fab_bottom_margin);
            layoutParams.bottomMargin = bottomAppBar.getBottomInset() + param2Int2 - param2Int1;
            layoutParams.leftMargin = bottomAppBar.getLeftInset();
            layoutParams.rightMargin = bottomAppBar.getRightInset();
            if (ViewUtils.isLayoutRtl((View)floatingActionButton)) {
              layoutParams.leftMargin += bottomAppBar.fabOffsetEndMode;
            } else {
              layoutParams.rightMargin += bottomAppBar.fabOffsetEndMode;
            } 
          } 
        }
      };
    
    private int originalBottomMargin;
    
    private WeakReference<BottomAppBar> viewRef;
    
    public Behavior() {}
    
    public Behavior(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    public boolean onLayoutChild(CoordinatorLayout param1CoordinatorLayout, BottomAppBar param1BottomAppBar, int param1Int) {
      this.viewRef = new WeakReference<>(param1BottomAppBar);
      View view = param1BottomAppBar.findDependentView();
      if (view != null && !ViewCompat.isLaidOut(view)) {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)view.getLayoutParams();
        layoutParams.anchorGravity = 49;
        this.originalBottomMargin = layoutParams.bottomMargin;
        if (view instanceof FloatingActionButton) {
          FloatingActionButton floatingActionButton = (FloatingActionButton)view;
          floatingActionButton.addOnLayoutChangeListener(this.fabLayoutListener);
          param1BottomAppBar.addFabAnimationListeners(floatingActionButton);
        } 
        param1BottomAppBar.setCutoutState();
      } 
      param1CoordinatorLayout.onLayoutChild((View)param1BottomAppBar, param1Int);
      return super.onLayoutChild(param1CoordinatorLayout, (View)param1BottomAppBar, param1Int);
    }
    
    public boolean onStartNestedScroll(CoordinatorLayout param1CoordinatorLayout, BottomAppBar param1BottomAppBar, View param1View1, View param1View2, int param1Int1, int param1Int2) {
      boolean bool;
      if (param1BottomAppBar.getHideOnScroll() && super.onStartNestedScroll(param1CoordinatorLayout, (View)param1BottomAppBar, param1View1, param1View2, param1Int1, param1Int2)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
  }
  
  class null implements View.OnLayoutChangeListener {
    final BottomAppBar.Behavior this$0;
    
    public void onLayoutChange(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6, int param1Int7, int param1Int8) {
      BottomAppBar bottomAppBar = this.this$0.viewRef.get();
      if (bottomAppBar == null || !(param1View instanceof FloatingActionButton)) {
        param1View.removeOnLayoutChangeListener(this);
        return;
      } 
      FloatingActionButton floatingActionButton = (FloatingActionButton)param1View;
      floatingActionButton.getMeasuredContentRect(this.this$0.fabContentRect);
      param1Int1 = this.this$0.fabContentRect.height();
      bottomAppBar.setFabDiameter(param1Int1);
      bottomAppBar.setFabCornerSize(floatingActionButton.getShapeAppearanceModel().getTopLeftCornerSize().getCornerSize(new RectF(this.this$0.fabContentRect)));
      CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)param1View.getLayoutParams();
      if (this.this$0.originalBottomMargin == 0) {
        param1Int1 = (floatingActionButton.getMeasuredHeight() - param1Int1) / 2;
        param1Int2 = bottomAppBar.getResources().getDimensionPixelOffset(R.dimen.mtrl_bottomappbar_fab_bottom_margin);
        layoutParams.bottomMargin = bottomAppBar.getBottomInset() + param1Int2 - param1Int1;
        layoutParams.leftMargin = bottomAppBar.getLeftInset();
        layoutParams.rightMargin = bottomAppBar.getRightInset();
        if (ViewUtils.isLayoutRtl((View)floatingActionButton)) {
          layoutParams.leftMargin += bottomAppBar.fabOffsetEndMode;
        } else {
          layoutParams.rightMargin += bottomAppBar.fabOffsetEndMode;
        } 
      } 
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface FabAlignmentMode {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface FabAnimationMode {}
  
  static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public BottomAppBar.SavedState createFromParcel(Parcel param2Parcel) {
          return new BottomAppBar.SavedState(param2Parcel, null);
        }
        
        public BottomAppBar.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new BottomAppBar.SavedState(param2Parcel, param2ClassLoader);
        }
        
        public BottomAppBar.SavedState[] newArray(int param2Int) {
          return new BottomAppBar.SavedState[param2Int];
        }
      };
    
    int fabAlignmentMode;
    
    boolean fabAttached;
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      boolean bool;
      this.fabAlignmentMode = param1Parcel.readInt();
      if (param1Parcel.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      this.fabAttached = bool;
    }
    
    public SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.fabAlignmentMode);
      param1Parcel.writeInt(this.fabAttached);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public BottomAppBar.SavedState createFromParcel(Parcel param1Parcel) {
      return new BottomAppBar.SavedState(param1Parcel, null);
    }
    
    public BottomAppBar.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new BottomAppBar.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public BottomAppBar.SavedState[] newArray(int param1Int) {
      return new BottomAppBar.SavedState[param1Int];
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\bottomappbar\BottomAppBar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */