package com.google.android.material.progressindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.widget.ProgressBar;
import androidx.core.view.ViewCompat;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import com.google.android.material.R;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

public abstract class BaseProgressIndicator<S extends BaseProgressIndicatorSpec> extends ProgressBar {
  static final float DEFAULT_OPACITY = 0.2F;
  
  static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_ProgressIndicator;
  
  public static final int HIDE_INWARD = 2;
  
  public static final int HIDE_NONE = 0;
  
  public static final int HIDE_OUTWARD = 1;
  
  static final int MAX_ALPHA = 255;
  
  static final int MAX_HIDE_DELAY = 1000;
  
  public static final int SHOW_INWARD = 2;
  
  public static final int SHOW_NONE = 0;
  
  public static final int SHOW_OUTWARD = 1;
  
  AnimatorDurationScaleProvider animatorDurationScaleProvider;
  
  private final Runnable delayedHide = new Runnable() {
      final BaseProgressIndicator this$0;
      
      public void run() {
        BaseProgressIndicator.this.internalHide();
        BaseProgressIndicator.access$202(BaseProgressIndicator.this, -1L);
      }
    };
  
  private final Runnable delayedShow = new Runnable() {
      final BaseProgressIndicator this$0;
      
      public void run() {
        BaseProgressIndicator.this.internalShow();
      }
    };
  
  private final Animatable2Compat.AnimationCallback hideAnimationCallback = new Animatable2Compat.AnimationCallback() {
      final BaseProgressIndicator this$0;
      
      public void onAnimationEnd(Drawable param1Drawable) {
        super.onAnimationEnd(param1Drawable);
        if (!BaseProgressIndicator.this.isIndeterminateModeChangeRequested) {
          BaseProgressIndicator baseProgressIndicator = BaseProgressIndicator.this;
          baseProgressIndicator.setVisibility(baseProgressIndicator.visibilityAfterHide);
        } 
      }
    };
  
  private boolean isIndeterminateModeChangeRequested = false;
  
  private boolean isParentDoneInitializing;
  
  private long lastShowStartTime = -1L;
  
  private final int minHideDelay;
  
  private final int showDelay;
  
  S spec;
  
  private int storedProgress;
  
  private boolean storedProgressAnimated;
  
  private final Animatable2Compat.AnimationCallback switchIndeterminateModeCallback = new Animatable2Compat.AnimationCallback() {
      final BaseProgressIndicator this$0;
      
      public void onAnimationEnd(Drawable param1Drawable) {
        BaseProgressIndicator.this.setIndeterminate(false);
        BaseProgressIndicator.this.setProgressCompat(0, false);
        BaseProgressIndicator baseProgressIndicator = BaseProgressIndicator.this;
        baseProgressIndicator.setProgressCompat(baseProgressIndicator.storedProgress, BaseProgressIndicator.this.storedProgressAnimated);
      }
    };
  
  private int visibilityAfterHide = 4;
  
  protected BaseProgressIndicator(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(MaterialThemeOverlay.wrap(paramContext, paramAttributeSet, paramInt1, DEF_STYLE_RES), paramAttributeSet, paramInt1);
    paramContext = getContext();
    this.spec = createSpec(paramContext, paramAttributeSet);
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.BaseProgressIndicator, paramInt1, paramInt2, new int[0]);
    this.showDelay = typedArray.getInt(R.styleable.BaseProgressIndicator_showDelay, -1);
    this.minHideDelay = Math.min(typedArray.getInt(R.styleable.BaseProgressIndicator_minHideDelay, -1), 1000);
    typedArray.recycle();
    this.animatorDurationScaleProvider = new AnimatorDurationScaleProvider();
    this.isParentDoneInitializing = true;
  }
  
  private DrawingDelegate<S> getCurrentDrawingDelegate() {
    boolean bool = isIndeterminate();
    DrawingDelegate drawingDelegate1 = null;
    DrawingDelegate<S> drawingDelegate = null;
    if (bool) {
      if (getIndeterminateDrawable() != null)
        drawingDelegate = getIndeterminateDrawable().getDrawingDelegate(); 
      return drawingDelegate;
    } 
    if (getProgressDrawable() == null) {
      drawingDelegate = drawingDelegate1;
    } else {
      drawingDelegate = getProgressDrawable().getDrawingDelegate();
    } 
    return drawingDelegate;
  }
  
  private void internalHide() {
    ((DrawableWithAnimatedVisibilityChange)getCurrentDrawable()).setVisible(false, false, true);
    if (isNoLongerNeedToBeVisible())
      setVisibility(4); 
  }
  
  private void internalShow() {
    if (this.minHideDelay > 0)
      this.lastShowStartTime = SystemClock.uptimeMillis(); 
    setVisibility(0);
  }
  
  private boolean isNoLongerNeedToBeVisible() {
    return ((getProgressDrawable() == null || !getProgressDrawable().isVisible()) && (getIndeterminateDrawable() == null || !getIndeterminateDrawable().isVisible()));
  }
  
  private void registerAnimationCallbacks() {
    if (getProgressDrawable() != null && getIndeterminateDrawable() != null)
      getIndeterminateDrawable().getAnimatorDelegate().registerAnimatorsCompleteCallback(this.switchIndeterminateModeCallback); 
    if (getProgressDrawable() != null)
      getProgressDrawable().registerAnimationCallback(this.hideAnimationCallback); 
    if (getIndeterminateDrawable() != null)
      getIndeterminateDrawable().registerAnimationCallback(this.hideAnimationCallback); 
  }
  
  private void unregisterAnimationCallbacks() {
    if (getIndeterminateDrawable() != null) {
      getIndeterminateDrawable().unregisterAnimationCallback(this.hideAnimationCallback);
      getIndeterminateDrawable().getAnimatorDelegate().unregisterAnimatorsCompleteCallback();
    } 
    if (getProgressDrawable() != null)
      getProgressDrawable().unregisterAnimationCallback(this.hideAnimationCallback); 
  }
  
  protected void applyNewVisibility(boolean paramBoolean) {
    if (!this.isParentDoneInitializing)
      return; 
    ((DrawableWithAnimatedVisibilityChange)getCurrentDrawable()).setVisible(visibleToUser(), false, paramBoolean);
  }
  
  abstract S createSpec(Context paramContext, AttributeSet paramAttributeSet);
  
  public Drawable getCurrentDrawable() {
    DeterminateDrawable<S> determinateDrawable;
    if (isIndeterminate()) {
      IndeterminateDrawable<S> indeterminateDrawable = getIndeterminateDrawable();
    } else {
      determinateDrawable = getProgressDrawable();
    } 
    return determinateDrawable;
  }
  
  public int getHideAnimationBehavior() {
    return ((BaseProgressIndicatorSpec)this.spec).hideAnimationBehavior;
  }
  
  public IndeterminateDrawable<S> getIndeterminateDrawable() {
    return (IndeterminateDrawable<S>)super.getIndeterminateDrawable();
  }
  
  public int[] getIndicatorColor() {
    return ((BaseProgressIndicatorSpec)this.spec).indicatorColors;
  }
  
  public DeterminateDrawable<S> getProgressDrawable() {
    return (DeterminateDrawable<S>)super.getProgressDrawable();
  }
  
  public int getShowAnimationBehavior() {
    return ((BaseProgressIndicatorSpec)this.spec).showAnimationBehavior;
  }
  
  public int getTrackColor() {
    return ((BaseProgressIndicatorSpec)this.spec).trackColor;
  }
  
  public int getTrackCornerRadius() {
    return ((BaseProgressIndicatorSpec)this.spec).trackCornerRadius;
  }
  
  public int getTrackThickness() {
    return ((BaseProgressIndicatorSpec)this.spec).trackThickness;
  }
  
  public void hide() {
    boolean bool;
    if (getVisibility() != 0) {
      removeCallbacks(this.delayedShow);
      return;
    } 
    removeCallbacks(this.delayedHide);
    long l = SystemClock.uptimeMillis() - this.lastShowStartTime;
    int i = this.minHideDelay;
    if (l >= i) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool) {
      this.delayedHide.run();
      return;
    } 
    postDelayed(this.delayedHide, i - l);
  }
  
  public void invalidate() {
    super.invalidate();
    if (getCurrentDrawable() != null)
      getCurrentDrawable().invalidateSelf(); 
  }
  
  boolean isEffectivelyVisible() {
    BaseProgressIndicator baseProgressIndicator = this;
    while (true) {
      int i = baseProgressIndicator.getVisibility();
      boolean bool = false;
      if (i != 0)
        return false; 
      ViewParent viewParent = baseProgressIndicator.getParent();
      if (viewParent == null) {
        if (getWindowVisibility() == 0)
          bool = true; 
        return bool;
      } 
      if (!(viewParent instanceof View))
        return true; 
      View view = (View)viewParent;
    } 
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    registerAnimationCallbacks();
    if (visibleToUser())
      internalShow(); 
  }
  
  protected void onDetachedFromWindow() {
    removeCallbacks(this.delayedHide);
    removeCallbacks(this.delayedShow);
    ((DrawableWithAnimatedVisibilityChange)getCurrentDrawable()).hideNow();
    unregisterAnimationCallbacks();
    super.onDetachedFromWindow();
  }
  
  protected void onDraw(Canvas paramCanvas) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokevirtual save : ()I
    //   6: istore_2
    //   7: aload_0
    //   8: invokevirtual getPaddingLeft : ()I
    //   11: ifne -> 21
    //   14: aload_0
    //   15: invokevirtual getPaddingTop : ()I
    //   18: ifeq -> 35
    //   21: aload_1
    //   22: aload_0
    //   23: invokevirtual getPaddingLeft : ()I
    //   26: i2f
    //   27: aload_0
    //   28: invokevirtual getPaddingTop : ()I
    //   31: i2f
    //   32: invokevirtual translate : (FF)V
    //   35: aload_0
    //   36: invokevirtual getPaddingRight : ()I
    //   39: ifne -> 49
    //   42: aload_0
    //   43: invokevirtual getPaddingBottom : ()I
    //   46: ifeq -> 84
    //   49: aload_1
    //   50: iconst_0
    //   51: iconst_0
    //   52: aload_0
    //   53: invokevirtual getWidth : ()I
    //   56: aload_0
    //   57: invokevirtual getPaddingLeft : ()I
    //   60: aload_0
    //   61: invokevirtual getPaddingRight : ()I
    //   64: iadd
    //   65: isub
    //   66: aload_0
    //   67: invokevirtual getHeight : ()I
    //   70: aload_0
    //   71: invokevirtual getPaddingTop : ()I
    //   74: aload_0
    //   75: invokevirtual getPaddingBottom : ()I
    //   78: iadd
    //   79: isub
    //   80: invokevirtual clipRect : (IIII)Z
    //   83: pop
    //   84: aload_0
    //   85: invokevirtual getCurrentDrawable : ()Landroid/graphics/drawable/Drawable;
    //   88: aload_1
    //   89: invokevirtual draw : (Landroid/graphics/Canvas;)V
    //   92: aload_1
    //   93: iload_2
    //   94: invokevirtual restoreToCount : (I)V
    //   97: aload_0
    //   98: monitorexit
    //   99: return
    //   100: astore_1
    //   101: aload_0
    //   102: monitorexit
    //   103: aload_1
    //   104: athrow
    // Exception table:
    //   from	to	target	type
    //   2	21	100	finally
    //   21	35	100	finally
    //   35	49	100	finally
    //   49	84	100	finally
    //   84	97	100	finally
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: iload_2
    //   5: invokespecial onMeasure : (II)V
    //   8: aload_0
    //   9: invokespecial getCurrentDrawingDelegate : ()Lcom/google/android/material/progressindicator/DrawingDelegate;
    //   12: astore_3
    //   13: aload_3
    //   14: ifnonnull -> 20
    //   17: aload_0
    //   18: monitorexit
    //   19: return
    //   20: aload_3
    //   21: invokevirtual getPreferredWidth : ()I
    //   24: istore_1
    //   25: aload_3
    //   26: invokevirtual getPreferredHeight : ()I
    //   29: istore_2
    //   30: iload_1
    //   31: ifge -> 42
    //   34: aload_0
    //   35: invokevirtual getMeasuredWidth : ()I
    //   38: istore_1
    //   39: goto -> 54
    //   42: aload_0
    //   43: invokevirtual getPaddingLeft : ()I
    //   46: iload_1
    //   47: iadd
    //   48: aload_0
    //   49: invokevirtual getPaddingRight : ()I
    //   52: iadd
    //   53: istore_1
    //   54: iload_2
    //   55: ifge -> 66
    //   58: aload_0
    //   59: invokevirtual getMeasuredHeight : ()I
    //   62: istore_2
    //   63: goto -> 78
    //   66: aload_0
    //   67: invokevirtual getPaddingTop : ()I
    //   70: iload_2
    //   71: iadd
    //   72: aload_0
    //   73: invokevirtual getPaddingBottom : ()I
    //   76: iadd
    //   77: istore_2
    //   78: aload_0
    //   79: iload_1
    //   80: iload_2
    //   81: invokevirtual setMeasuredDimension : (II)V
    //   84: aload_0
    //   85: monitorexit
    //   86: return
    //   87: astore_3
    //   88: aload_0
    //   89: monitorexit
    //   90: aload_3
    //   91: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	87	finally
    //   20	30	87	finally
    //   34	39	87	finally
    //   42	54	87	finally
    //   58	63	87	finally
    //   66	78	87	finally
    //   78	84	87	finally
  }
  
  protected void onVisibilityChanged(View paramView, int paramInt) {
    boolean bool;
    super.onVisibilityChanged(paramView, paramInt);
    if (paramInt == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    applyNewVisibility(bool);
  }
  
  protected void onWindowVisibilityChanged(int paramInt) {
    super.onWindowVisibilityChanged(paramInt);
    applyNewVisibility(false);
  }
  
  public void setAnimatorDurationScaleProvider(AnimatorDurationScaleProvider paramAnimatorDurationScaleProvider) {
    this.animatorDurationScaleProvider = paramAnimatorDurationScaleProvider;
    if (getProgressDrawable() != null)
      (getProgressDrawable()).animatorDurationScaleProvider = paramAnimatorDurationScaleProvider; 
    if (getIndeterminateDrawable() != null)
      (getIndeterminateDrawable()).animatorDurationScaleProvider = paramAnimatorDurationScaleProvider; 
  }
  
  public void setHideAnimationBehavior(int paramInt) {
    ((BaseProgressIndicatorSpec)this.spec).hideAnimationBehavior = paramInt;
    invalidate();
  }
  
  public void setIndeterminate(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual isIndeterminate : ()Z
    //   6: istore_2
    //   7: iload_1
    //   8: iload_2
    //   9: if_icmpne -> 15
    //   12: aload_0
    //   13: monitorexit
    //   14: return
    //   15: aload_0
    //   16: invokevirtual visibleToUser : ()Z
    //   19: ifeq -> 42
    //   22: iload_1
    //   23: ifne -> 29
    //   26: goto -> 42
    //   29: new java/lang/IllegalStateException
    //   32: astore_3
    //   33: aload_3
    //   34: ldc_w 'Cannot switch to indeterminate mode while the progress indicator is visible.'
    //   37: invokespecial <init> : (Ljava/lang/String;)V
    //   40: aload_3
    //   41: athrow
    //   42: aload_0
    //   43: invokevirtual getCurrentDrawable : ()Landroid/graphics/drawable/Drawable;
    //   46: checkcast com/google/android/material/progressindicator/DrawableWithAnimatedVisibilityChange
    //   49: astore_3
    //   50: aload_3
    //   51: ifnull -> 59
    //   54: aload_3
    //   55: invokevirtual hideNow : ()Z
    //   58: pop
    //   59: aload_0
    //   60: iload_1
    //   61: invokespecial setIndeterminate : (Z)V
    //   64: aload_0
    //   65: invokevirtual getCurrentDrawable : ()Landroid/graphics/drawable/Drawable;
    //   68: checkcast com/google/android/material/progressindicator/DrawableWithAnimatedVisibilityChange
    //   71: astore_3
    //   72: aload_3
    //   73: ifnull -> 87
    //   76: aload_3
    //   77: aload_0
    //   78: invokevirtual visibleToUser : ()Z
    //   81: iconst_0
    //   82: iconst_0
    //   83: invokevirtual setVisible : (ZZZ)Z
    //   86: pop
    //   87: aload_0
    //   88: iconst_0
    //   89: putfield isIndeterminateModeChangeRequested : Z
    //   92: aload_0
    //   93: monitorexit
    //   94: return
    //   95: astore_3
    //   96: aload_0
    //   97: monitorexit
    //   98: aload_3
    //   99: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	95	finally
    //   15	22	95	finally
    //   29	42	95	finally
    //   42	50	95	finally
    //   54	59	95	finally
    //   59	64	95	finally
    //   64	72	95	finally
    //   76	87	95	finally
    //   87	92	95	finally
  }
  
  public void setIndeterminateDrawable(Drawable paramDrawable) {
    if (paramDrawable == null) {
      super.setIndeterminateDrawable(null);
      return;
    } 
    if (paramDrawable instanceof IndeterminateDrawable) {
      ((DrawableWithAnimatedVisibilityChange)paramDrawable).hideNow();
      super.setIndeterminateDrawable(paramDrawable);
      return;
    } 
    throw new IllegalArgumentException("Cannot set framework drawable as indeterminate drawable.");
  }
  
  public void setIndicatorColor(int... paramVarArgs) {
    int[] arrayOfInt = paramVarArgs;
    if (paramVarArgs.length == 0)
      arrayOfInt = new int[] { MaterialColors.getColor(getContext(), R.attr.colorPrimary, -1) }; 
    if (!Arrays.equals(getIndicatorColor(), arrayOfInt)) {
      ((BaseProgressIndicatorSpec)this.spec).indicatorColors = arrayOfInt;
      getIndeterminateDrawable().getAnimatorDelegate().invalidateSpecValues();
      invalidate();
    } 
  }
  
  public void setProgress(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual isIndeterminate : ()Z
    //   6: istore_2
    //   7: iload_2
    //   8: ifeq -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: aload_0
    //   15: iload_1
    //   16: iconst_0
    //   17: invokevirtual setProgressCompat : (IZ)V
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: astore_3
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_3
    //   27: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	23	finally
    //   14	20	23	finally
  }
  
  public void setProgressCompat(int paramInt, boolean paramBoolean) {
    if (isIndeterminate()) {
      if (getProgressDrawable() != null) {
        this.storedProgress = paramInt;
        this.storedProgressAnimated = paramBoolean;
        this.isIndeterminateModeChangeRequested = true;
        if (!getIndeterminateDrawable().isVisible() || this.animatorDurationScaleProvider.getSystemAnimatorDurationScale(getContext().getContentResolver()) == 0.0F) {
          this.switchIndeterminateModeCallback.onAnimationEnd(getIndeterminateDrawable());
          return;
        } 
        getIndeterminateDrawable().getAnimatorDelegate().requestCancelAnimatorAfterCurrentCycle();
      } 
    } else {
      super.setProgress(paramInt);
      if (getProgressDrawable() != null && !paramBoolean)
        getProgressDrawable().jumpToCurrentState(); 
    } 
  }
  
  public void setProgressDrawable(Drawable paramDrawable) {
    if (paramDrawable == null) {
      super.setProgressDrawable(null);
      return;
    } 
    if (paramDrawable instanceof DeterminateDrawable) {
      paramDrawable = paramDrawable;
      paramDrawable.hideNow();
      super.setProgressDrawable(paramDrawable);
      paramDrawable.setLevelByFraction(getProgress() / getMax());
      return;
    } 
    throw new IllegalArgumentException("Cannot set framework drawable as progress drawable.");
  }
  
  public void setShowAnimationBehavior(int paramInt) {
    ((BaseProgressIndicatorSpec)this.spec).showAnimationBehavior = paramInt;
    invalidate();
  }
  
  public void setTrackColor(int paramInt) {
    if (((BaseProgressIndicatorSpec)this.spec).trackColor != paramInt) {
      ((BaseProgressIndicatorSpec)this.spec).trackColor = paramInt;
      invalidate();
    } 
  }
  
  public void setTrackCornerRadius(int paramInt) {
    if (((BaseProgressIndicatorSpec)this.spec).trackCornerRadius != paramInt) {
      S s = this.spec;
      ((BaseProgressIndicatorSpec)s).trackCornerRadius = Math.min(paramInt, ((BaseProgressIndicatorSpec)s).trackThickness / 2);
    } 
  }
  
  public void setTrackThickness(int paramInt) {
    if (((BaseProgressIndicatorSpec)this.spec).trackThickness != paramInt) {
      ((BaseProgressIndicatorSpec)this.spec).trackThickness = paramInt;
      requestLayout();
    } 
  }
  
  public void setVisibilityAfterHide(int paramInt) {
    if (paramInt == 0 || paramInt == 4 || paramInt == 8) {
      this.visibilityAfterHide = paramInt;
      return;
    } 
    throw new IllegalArgumentException("The component's visibility must be one of VISIBLE, INVISIBLE, and GONE defined in View.");
  }
  
  public void show() {
    if (this.showDelay > 0) {
      removeCallbacks(this.delayedShow);
      postDelayed(this.delayedShow, this.showDelay);
    } else {
      this.delayedShow.run();
    } 
  }
  
  boolean visibleToUser() {
    boolean bool;
    if (ViewCompat.isAttachedToWindow((View)this) && getWindowVisibility() == 0 && isEffectivelyVisible()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface HideAnimationBehavior {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ShowAnimationBehavior {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\progressindicator\BaseProgressIndicator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */