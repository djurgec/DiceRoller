package com.google.android.material.appbar;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.Toolbar;
import androidx.appcompat.R;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.math.MathUtils;
import androidx.core.util.ObjectsCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.elevation.ElevationOverlayProvider;
import com.google.android.material.internal.CollapsingTextHelper;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CollapsingToolbarLayout extends FrameLayout {
  private static final int DEFAULT_SCRIM_ANIMATION_DURATION = 600;
  
  private static final int DEF_STYLE_RES = R.style.Widget_Design_CollapsingToolbar;
  
  public static final int TITLE_COLLAPSE_MODE_FADE = 1;
  
  public static final int TITLE_COLLAPSE_MODE_SCALE = 0;
  
  final CollapsingTextHelper collapsingTextHelper;
  
  private boolean collapsingTitleEnabled;
  
  private Drawable contentScrim;
  
  int currentOffset;
  
  private boolean drawCollapsingTitle;
  
  private View dummyView;
  
  final ElevationOverlayProvider elevationOverlayProvider;
  
  private int expandedMarginBottom;
  
  private int expandedMarginEnd;
  
  private int expandedMarginStart;
  
  private int expandedMarginTop;
  
  private int extraMultilineHeight = 0;
  
  private boolean extraMultilineHeightEnabled;
  
  private boolean forceApplySystemWindowInsetTop;
  
  WindowInsetsCompat lastInsets;
  
  private AppBarLayout.OnOffsetChangedListener onOffsetChangedListener;
  
  private boolean refreshToolbar = true;
  
  private int scrimAlpha;
  
  private long scrimAnimationDuration;
  
  private ValueAnimator scrimAnimator;
  
  private int scrimVisibleHeightTrigger = -1;
  
  private boolean scrimsAreShown;
  
  Drawable statusBarScrim;
  
  private int titleCollapseMode;
  
  private final Rect tmpRect = new Rect();
  
  private ViewGroup toolbar;
  
  private View toolbarDirectChild;
  
  private int toolbarId;
  
  private int topInsetApplied = 0;
  
  public CollapsingToolbarLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public CollapsingToolbarLayout(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.collapsingToolbarLayoutStyle);
  }
  
  public CollapsingToolbarLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(MaterialThemeOverlay.wrap(paramContext, paramAttributeSet, paramInt, i), paramAttributeSet, paramInt);
    Context context = getContext();
    CollapsingTextHelper collapsingTextHelper = new CollapsingTextHelper((View)this);
    this.collapsingTextHelper = collapsingTextHelper;
    collapsingTextHelper.setTextSizeInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);
    collapsingTextHelper.setRtlTextDirectionHeuristicsEnabled(false);
    this.elevationOverlayProvider = new ElevationOverlayProvider(context);
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(context, paramAttributeSet, R.styleable.CollapsingToolbarLayout, paramInt, i, new int[0]);
    collapsingTextHelper.setExpandedTextGravity(typedArray.getInt(R.styleable.CollapsingToolbarLayout_expandedTitleGravity, 8388691));
    collapsingTextHelper.setCollapsedTextGravity(typedArray.getInt(R.styleable.CollapsingToolbarLayout_collapsedTitleGravity, 8388627));
    paramInt = typedArray.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_expandedTitleMargin, 0);
    this.expandedMarginBottom = paramInt;
    this.expandedMarginEnd = paramInt;
    this.expandedMarginTop = paramInt;
    this.expandedMarginStart = paramInt;
    if (typedArray.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginStart))
      this.expandedMarginStart = typedArray.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_expandedTitleMarginStart, 0); 
    if (typedArray.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginEnd))
      this.expandedMarginEnd = typedArray.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_expandedTitleMarginEnd, 0); 
    if (typedArray.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginTop))
      this.expandedMarginTop = typedArray.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_expandedTitleMarginTop, 0); 
    if (typedArray.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginBottom))
      this.expandedMarginBottom = typedArray.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_expandedTitleMarginBottom, 0); 
    this.collapsingTitleEnabled = typedArray.getBoolean(R.styleable.CollapsingToolbarLayout_titleEnabled, true);
    setTitle(typedArray.getText(R.styleable.CollapsingToolbarLayout_title));
    collapsingTextHelper.setExpandedTextAppearance(R.style.TextAppearance_Design_CollapsingToolbar_Expanded);
    collapsingTextHelper.setCollapsedTextAppearance(R.style.TextAppearance_AppCompat_Widget_ActionBar_Title);
    if (typedArray.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleTextAppearance))
      collapsingTextHelper.setExpandedTextAppearance(typedArray.getResourceId(R.styleable.CollapsingToolbarLayout_expandedTitleTextAppearance, 0)); 
    if (typedArray.hasValue(R.styleable.CollapsingToolbarLayout_collapsedTitleTextAppearance))
      collapsingTextHelper.setCollapsedTextAppearance(typedArray.getResourceId(R.styleable.CollapsingToolbarLayout_collapsedTitleTextAppearance, 0)); 
    this.scrimVisibleHeightTrigger = typedArray.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_scrimVisibleHeightTrigger, -1);
    if (typedArray.hasValue(R.styleable.CollapsingToolbarLayout_maxLines))
      collapsingTextHelper.setMaxLines(typedArray.getInt(R.styleable.CollapsingToolbarLayout_maxLines, 1)); 
    this.scrimAnimationDuration = typedArray.getInt(R.styleable.CollapsingToolbarLayout_scrimAnimationDuration, 600);
    setContentScrim(typedArray.getDrawable(R.styleable.CollapsingToolbarLayout_contentScrim));
    setStatusBarScrim(typedArray.getDrawable(R.styleable.CollapsingToolbarLayout_statusBarScrim));
    setTitleCollapseMode(typedArray.getInt(R.styleable.CollapsingToolbarLayout_titleCollapseMode, 0));
    this.toolbarId = typedArray.getResourceId(R.styleable.CollapsingToolbarLayout_toolbarId, -1);
    this.forceApplySystemWindowInsetTop = typedArray.getBoolean(R.styleable.CollapsingToolbarLayout_forceApplySystemWindowInsetTop, false);
    this.extraMultilineHeightEnabled = typedArray.getBoolean(R.styleable.CollapsingToolbarLayout_extraMultilineHeightEnabled, false);
    typedArray.recycle();
    setWillNotDraw(false);
    ViewCompat.setOnApplyWindowInsetsListener((View)this, new OnApplyWindowInsetsListener() {
          final CollapsingToolbarLayout this$0;
          
          public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat) {
            return CollapsingToolbarLayout.this.onWindowInsetChanged(param1WindowInsetsCompat);
          }
        });
  }
  
  private void animateScrim(int paramInt) {
    TimeInterpolator timeInterpolator;
    ensureToolbar();
    ValueAnimator valueAnimator = this.scrimAnimator;
    if (valueAnimator == null) {
      valueAnimator = new ValueAnimator();
      this.scrimAnimator = valueAnimator;
      valueAnimator.setDuration(this.scrimAnimationDuration);
      ValueAnimator valueAnimator1 = this.scrimAnimator;
      if (paramInt > this.scrimAlpha) {
        timeInterpolator = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
      } else {
        timeInterpolator = AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR;
      } 
      valueAnimator1.setInterpolator(timeInterpolator);
      this.scrimAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            final CollapsingToolbarLayout this$0;
            
            public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
              CollapsingToolbarLayout.this.setScrimAlpha(((Integer)param1ValueAnimator.getAnimatedValue()).intValue());
            }
          });
    } else if (timeInterpolator.isRunning()) {
      this.scrimAnimator.cancel();
    } 
    this.scrimAnimator.setIntValues(new int[] { this.scrimAlpha, paramInt });
    this.scrimAnimator.start();
  }
  
  private void disableLiftOnScrollIfNeeded(AppBarLayout paramAppBarLayout) {
    if (isTitleCollapseFadeMode())
      paramAppBarLayout.setLiftOnScroll(false); 
  }
  
  private void ensureToolbar() {
    if (!this.refreshToolbar)
      return; 
    this.toolbar = null;
    this.toolbarDirectChild = null;
    int i = this.toolbarId;
    if (i != -1) {
      ViewGroup viewGroup = (ViewGroup)findViewById(i);
      this.toolbar = viewGroup;
      if (viewGroup != null)
        this.toolbarDirectChild = findDirectChild((View)viewGroup); 
    } 
    if (this.toolbar == null) {
      ViewGroup viewGroup;
      View view = null;
      i = 0;
      int j = getChildCount();
      while (true) {
        View view1 = view;
        if (i < j) {
          view1 = getChildAt(i);
          if (isToolbar(view1)) {
            viewGroup = (ViewGroup)view1;
            break;
          } 
          i++;
          continue;
        } 
        break;
      } 
      this.toolbar = viewGroup;
    } 
    updateDummyView();
    this.refreshToolbar = false;
  }
  
  private View findDirectChild(View paramView) {
    View view = paramView;
    for (ViewParent viewParent = paramView.getParent(); viewParent != this && viewParent != null; viewParent = viewParent.getParent()) {
      if (viewParent instanceof View)
        view = (View)viewParent; 
    } 
    return view;
  }
  
  private static int getHeightWithMargins(View paramView) {
    ViewGroup.LayoutParams layoutParams = paramView.getLayoutParams();
    if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
      ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)layoutParams;
      return paramView.getMeasuredHeight() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
    } 
    return paramView.getMeasuredHeight();
  }
  
  private static CharSequence getToolbarTitle(View paramView) {
    return (paramView instanceof Toolbar) ? ((Toolbar)paramView).getTitle() : ((Build.VERSION.SDK_INT >= 21 && paramView instanceof Toolbar) ? ((Toolbar)paramView).getTitle() : null);
  }
  
  static ViewOffsetHelper getViewOffsetHelper(View paramView) {
    ViewOffsetHelper viewOffsetHelper2 = (ViewOffsetHelper)paramView.getTag(R.id.view_offset_helper);
    ViewOffsetHelper viewOffsetHelper1 = viewOffsetHelper2;
    if (viewOffsetHelper2 == null) {
      viewOffsetHelper1 = new ViewOffsetHelper(paramView);
      paramView.setTag(R.id.view_offset_helper, viewOffsetHelper1);
    } 
    return viewOffsetHelper1;
  }
  
  private boolean isTitleCollapseFadeMode() {
    int i = this.titleCollapseMode;
    boolean bool = true;
    if (i != 1)
      bool = false; 
    return bool;
  }
  
  private static boolean isToolbar(View paramView) {
    return (paramView instanceof Toolbar || (Build.VERSION.SDK_INT >= 21 && paramView instanceof Toolbar));
  }
  
  private boolean isToolbarChild(View paramView) {
    View view = this.toolbarDirectChild;
    boolean bool = true;
    if ((view == this) ? (paramView == this.toolbar) : (paramView == view))
      bool = false; 
    return bool;
  }
  
  private void updateCollapsedBounds(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield toolbarDirectChild : Landroid/view/View;
    //   4: astore #11
    //   6: aload #11
    //   8: ifnull -> 14
    //   11: goto -> 20
    //   14: aload_0
    //   15: getfield toolbar : Landroid/view/ViewGroup;
    //   18: astore #11
    //   20: aload_0
    //   21: aload #11
    //   23: invokevirtual getMaxOffsetForPinChild : (Landroid/view/View;)I
    //   26: istore #7
    //   28: aload_0
    //   29: aload_0
    //   30: getfield dummyView : Landroid/view/View;
    //   33: aload_0
    //   34: getfield tmpRect : Landroid/graphics/Rect;
    //   37: invokestatic getDescendantRect : (Landroid/view/ViewGroup;Landroid/view/View;Landroid/graphics/Rect;)V
    //   40: aload_0
    //   41: getfield toolbar : Landroid/view/ViewGroup;
    //   44: astore #11
    //   46: aload #11
    //   48: instanceof androidx/appcompat/widget/Toolbar
    //   51: ifeq -> 90
    //   54: aload #11
    //   56: checkcast androidx/appcompat/widget/Toolbar
    //   59: astore #11
    //   61: aload #11
    //   63: invokevirtual getTitleMarginStart : ()I
    //   66: istore_2
    //   67: aload #11
    //   69: invokevirtual getTitleMarginEnd : ()I
    //   72: istore_3
    //   73: aload #11
    //   75: invokevirtual getTitleMarginTop : ()I
    //   78: istore #5
    //   80: aload #11
    //   82: invokevirtual getTitleMarginBottom : ()I
    //   85: istore #4
    //   87: goto -> 158
    //   90: getstatic android/os/Build$VERSION.SDK_INT : I
    //   93: bipush #24
    //   95: if_icmplt -> 148
    //   98: aload_0
    //   99: getfield toolbar : Landroid/view/ViewGroup;
    //   102: astore #11
    //   104: aload #11
    //   106: instanceof android/widget/Toolbar
    //   109: ifeq -> 148
    //   112: aload #11
    //   114: checkcast android/widget/Toolbar
    //   117: astore #11
    //   119: aload #11
    //   121: invokevirtual getTitleMarginStart : ()I
    //   124: istore_2
    //   125: aload #11
    //   127: invokevirtual getTitleMarginEnd : ()I
    //   130: istore_3
    //   131: aload #11
    //   133: invokevirtual getTitleMarginTop : ()I
    //   136: istore #5
    //   138: aload #11
    //   140: invokevirtual getTitleMarginBottom : ()I
    //   143: istore #4
    //   145: goto -> 158
    //   148: iconst_0
    //   149: istore_2
    //   150: iconst_0
    //   151: istore_3
    //   152: iconst_0
    //   153: istore #5
    //   155: iconst_0
    //   156: istore #4
    //   158: aload_0
    //   159: getfield collapsingTextHelper : Lcom/google/android/material/internal/CollapsingTextHelper;
    //   162: astore #11
    //   164: aload_0
    //   165: getfield tmpRect : Landroid/graphics/Rect;
    //   168: getfield left : I
    //   171: istore #8
    //   173: iload_1
    //   174: ifeq -> 183
    //   177: iload_3
    //   178: istore #6
    //   180: goto -> 186
    //   183: iload_2
    //   184: istore #6
    //   186: aload_0
    //   187: getfield tmpRect : Landroid/graphics/Rect;
    //   190: getfield top : I
    //   193: istore #10
    //   195: aload_0
    //   196: getfield tmpRect : Landroid/graphics/Rect;
    //   199: getfield right : I
    //   202: istore #9
    //   204: iload_1
    //   205: ifeq -> 211
    //   208: goto -> 213
    //   211: iload_3
    //   212: istore_2
    //   213: aload #11
    //   215: iload #8
    //   217: iload #6
    //   219: iadd
    //   220: iload #10
    //   222: iload #7
    //   224: iadd
    //   225: iload #5
    //   227: iadd
    //   228: iload #9
    //   230: iload_2
    //   231: isub
    //   232: aload_0
    //   233: getfield tmpRect : Landroid/graphics/Rect;
    //   236: getfield bottom : I
    //   239: iload #7
    //   241: iadd
    //   242: iload #4
    //   244: isub
    //   245: invokevirtual setCollapsedBounds : (IIII)V
    //   248: return
  }
  
  private void updateContentDescriptionFromTitle() {
    setContentDescription(getTitle());
  }
  
  private void updateContentScrimBounds(Drawable paramDrawable, int paramInt1, int paramInt2) {
    updateContentScrimBounds(paramDrawable, (View)this.toolbar, paramInt1, paramInt2);
  }
  
  private void updateContentScrimBounds(Drawable paramDrawable, View paramView, int paramInt1, int paramInt2) {
    if (isTitleCollapseFadeMode() && paramView != null && this.collapsingTitleEnabled)
      paramInt2 = paramView.getBottom(); 
    paramDrawable.setBounds(0, 0, paramInt1, paramInt2);
  }
  
  private void updateDummyView() {
    if (!this.collapsingTitleEnabled) {
      View view = this.dummyView;
      if (view != null) {
        ViewParent viewParent = view.getParent();
        if (viewParent instanceof ViewGroup)
          ((ViewGroup)viewParent).removeView(this.dummyView); 
      } 
    } 
    if (this.collapsingTitleEnabled && this.toolbar != null) {
      if (this.dummyView == null)
        this.dummyView = new View(getContext()); 
      if (this.dummyView.getParent() == null)
        this.toolbar.addView(this.dummyView, -1, -1); 
    } 
  }
  
  private void updateTextBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
    if (this.collapsingTitleEnabled) {
      View view = this.dummyView;
      if (view != null) {
        boolean bool = ViewCompat.isAttachedToWindow(view);
        boolean bool1 = false;
        if (bool && this.dummyView.getVisibility() == 0) {
          bool = true;
        } else {
          bool = false;
        } 
        this.drawCollapsingTitle = bool;
        if (bool || paramBoolean) {
          int i;
          int j;
          bool = bool1;
          if (ViewCompat.getLayoutDirection((View)this) == 1)
            bool = true; 
          updateCollapsedBounds(bool);
          CollapsingTextHelper collapsingTextHelper = this.collapsingTextHelper;
          if (bool) {
            i = this.expandedMarginEnd;
          } else {
            i = this.expandedMarginStart;
          } 
          int k = this.tmpRect.top;
          int m = this.expandedMarginTop;
          if (bool) {
            j = this.expandedMarginStart;
          } else {
            j = this.expandedMarginEnd;
          } 
          collapsingTextHelper.setExpandedBounds(i, k + m, paramInt3 - paramInt1 - j, paramInt4 - paramInt2 - this.expandedMarginBottom);
          this.collapsingTextHelper.recalculate(paramBoolean);
        } 
      } 
    } 
  }
  
  private void updateTitleFromToolbarIfNeeded() {
    if (this.toolbar != null && this.collapsingTitleEnabled && TextUtils.isEmpty(this.collapsingTextHelper.getText()))
      setTitle(getToolbarTitle((View)this.toolbar)); 
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  public void draw(Canvas paramCanvas) {
    super.draw(paramCanvas);
    ensureToolbar();
    if (this.toolbar == null) {
      Drawable drawable = this.contentScrim;
      if (drawable != null && this.scrimAlpha > 0) {
        drawable.mutate().setAlpha(this.scrimAlpha);
        this.contentScrim.draw(paramCanvas);
      } 
    } 
    if (this.collapsingTitleEnabled && this.drawCollapsingTitle)
      if (this.toolbar != null && this.contentScrim != null && this.scrimAlpha > 0 && isTitleCollapseFadeMode() && this.collapsingTextHelper.getExpansionFraction() < this.collapsingTextHelper.getFadeModeThresholdFraction()) {
        int i = paramCanvas.save();
        paramCanvas.clipRect(this.contentScrim.getBounds(), Region.Op.DIFFERENCE);
        this.collapsingTextHelper.draw(paramCanvas);
        paramCanvas.restoreToCount(i);
      } else {
        this.collapsingTextHelper.draw(paramCanvas);
      }  
    if (this.statusBarScrim != null && this.scrimAlpha > 0) {
      byte b;
      WindowInsetsCompat windowInsetsCompat = this.lastInsets;
      if (windowInsetsCompat != null) {
        b = windowInsetsCompat.getSystemWindowInsetTop();
      } else {
        b = 0;
      } 
      if (b) {
        this.statusBarScrim.setBounds(0, -this.currentOffset, getWidth(), b - this.currentOffset);
        this.statusBarScrim.mutate().setAlpha(this.scrimAlpha);
        this.statusBarScrim.draw(paramCanvas);
      } 
    } 
  }
  
  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong) {
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (this.contentScrim != null) {
      bool1 = bool2;
      if (this.scrimAlpha > 0) {
        bool1 = bool2;
        if (isToolbarChild(paramView)) {
          updateContentScrimBounds(this.contentScrim, paramView, getWidth(), getHeight());
          this.contentScrim.mutate().setAlpha(this.scrimAlpha);
          this.contentScrim.draw(paramCanvas);
          bool1 = true;
        } 
      } 
    } 
    return (super.drawChild(paramCanvas, paramView, paramLong) || bool1);
  }
  
  protected void drawableStateChanged() {
    boolean bool2;
    super.drawableStateChanged();
    int[] arrayOfInt = getDrawableState();
    int j = 0;
    Drawable drawable = this.statusBarScrim;
    int i = j;
    if (drawable != null) {
      i = j;
      if (drawable.isStateful())
        i = false | drawable.setState(arrayOfInt); 
    } 
    drawable = this.contentScrim;
    j = i;
    if (drawable != null) {
      j = i;
      if (drawable.isStateful())
        bool2 = i | drawable.setState(arrayOfInt); 
    } 
    CollapsingTextHelper collapsingTextHelper = this.collapsingTextHelper;
    boolean bool1 = bool2;
    if (collapsingTextHelper != null)
      bool1 = bool2 | collapsingTextHelper.setState(arrayOfInt); 
    if (bool1)
      invalidate(); 
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams(-1, -1);
  }
  
  public FrameLayout.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected FrameLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return new LayoutParams(paramLayoutParams);
  }
  
  public int getCollapsedTitleGravity() {
    return this.collapsingTextHelper.getCollapsedTextGravity();
  }
  
  public Typeface getCollapsedTitleTypeface() {
    return this.collapsingTextHelper.getCollapsedTypeface();
  }
  
  public Drawable getContentScrim() {
    return this.contentScrim;
  }
  
  public int getExpandedTitleGravity() {
    return this.collapsingTextHelper.getExpandedTextGravity();
  }
  
  public int getExpandedTitleMarginBottom() {
    return this.expandedMarginBottom;
  }
  
  public int getExpandedTitleMarginEnd() {
    return this.expandedMarginEnd;
  }
  
  public int getExpandedTitleMarginStart() {
    return this.expandedMarginStart;
  }
  
  public int getExpandedTitleMarginTop() {
    return this.expandedMarginTop;
  }
  
  public Typeface getExpandedTitleTypeface() {
    return this.collapsingTextHelper.getExpandedTypeface();
  }
  
  public int getHyphenationFrequency() {
    return this.collapsingTextHelper.getHyphenationFrequency();
  }
  
  public int getLineCount() {
    return this.collapsingTextHelper.getLineCount();
  }
  
  public float getLineSpacingAdd() {
    return this.collapsingTextHelper.getLineSpacingAdd();
  }
  
  public float getLineSpacingMultiplier() {
    return this.collapsingTextHelper.getLineSpacingMultiplier();
  }
  
  public int getMaxLines() {
    return this.collapsingTextHelper.getMaxLines();
  }
  
  final int getMaxOffsetForPinChild(View paramView) {
    ViewOffsetHelper viewOffsetHelper = getViewOffsetHelper(paramView);
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    return getHeight() - viewOffsetHelper.getLayoutTop() - paramView.getHeight() - layoutParams.bottomMargin;
  }
  
  int getScrimAlpha() {
    return this.scrimAlpha;
  }
  
  public long getScrimAnimationDuration() {
    return this.scrimAnimationDuration;
  }
  
  public int getScrimVisibleHeightTrigger() {
    int i = this.scrimVisibleHeightTrigger;
    if (i >= 0)
      return i + this.topInsetApplied + this.extraMultilineHeight; 
    WindowInsetsCompat windowInsetsCompat = this.lastInsets;
    if (windowInsetsCompat != null) {
      i = windowInsetsCompat.getSystemWindowInsetTop();
    } else {
      i = 0;
    } 
    int j = ViewCompat.getMinimumHeight((View)this);
    return (j > 0) ? Math.min(j * 2 + i, getHeight()) : (getHeight() / 3);
  }
  
  public Drawable getStatusBarScrim() {
    return this.statusBarScrim;
  }
  
  public CharSequence getTitle() {
    CharSequence charSequence;
    if (this.collapsingTitleEnabled) {
      charSequence = this.collapsingTextHelper.getText();
    } else {
      charSequence = null;
    } 
    return charSequence;
  }
  
  public int getTitleCollapseMode() {
    return this.titleCollapseMode;
  }
  
  public boolean isExtraMultilineHeightEnabled() {
    return this.extraMultilineHeightEnabled;
  }
  
  public boolean isForceApplySystemWindowInsetTop() {
    return this.forceApplySystemWindowInsetTop;
  }
  
  public boolean isRtlTextDirectionHeuristicsEnabled() {
    return this.collapsingTextHelper.isRtlTextDirectionHeuristicsEnabled();
  }
  
  public boolean isTitleEnabled() {
    return this.collapsingTitleEnabled;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    ViewParent viewParent = getParent();
    if (viewParent instanceof AppBarLayout) {
      AppBarLayout appBarLayout = (AppBarLayout)viewParent;
      disableLiftOnScrollIfNeeded(appBarLayout);
      ViewCompat.setFitsSystemWindows((View)this, ViewCompat.getFitsSystemWindows((View)appBarLayout));
      if (this.onOffsetChangedListener == null)
        this.onOffsetChangedListener = new OffsetUpdateListener(); 
      appBarLayout.addOnOffsetChangedListener(this.onOffsetChangedListener);
      ViewCompat.requestApplyInsets((View)this);
    } 
  }
  
  protected void onDetachedFromWindow() {
    ViewParent viewParent = getParent();
    AppBarLayout.OnOffsetChangedListener onOffsetChangedListener = this.onOffsetChangedListener;
    if (onOffsetChangedListener != null && viewParent instanceof AppBarLayout)
      ((AppBarLayout)viewParent).removeOnOffsetChangedListener(onOffsetChangedListener); 
    super.onDetachedFromWindow();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    WindowInsetsCompat windowInsetsCompat = this.lastInsets;
    if (windowInsetsCompat != null) {
      int k = windowInsetsCompat.getSystemWindowInsetTop();
      byte b1 = 0;
      int j = getChildCount();
      while (b1 < j) {
        View view = getChildAt(b1);
        if (!ViewCompat.getFitsSystemWindows(view) && view.getTop() < k)
          ViewCompat.offsetTopAndBottom(view, k); 
        b1++;
      } 
    } 
    byte b = 0;
    int i = getChildCount();
    while (b < i) {
      getViewOffsetHelper(getChildAt(b)).onViewLayout();
      b++;
    } 
    updateTextBounds(paramInt1, paramInt2, paramInt3, paramInt4, false);
    updateTitleFromToolbarIfNeeded();
    updateScrimVisibility();
    paramInt1 = 0;
    paramInt2 = getChildCount();
    while (paramInt1 < paramInt2) {
      getViewOffsetHelper(getChildAt(paramInt1)).applyOffsets();
      paramInt1++;
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    ensureToolbar();
    super.onMeasure(paramInt1, paramInt2);
    int i = View.MeasureSpec.getMode(paramInt2);
    WindowInsetsCompat windowInsetsCompat = this.lastInsets;
    if (windowInsetsCompat != null) {
      paramInt2 = windowInsetsCompat.getSystemWindowInsetTop();
    } else {
      paramInt2 = 0;
    } 
    if ((i == 0 || this.forceApplySystemWindowInsetTop) && paramInt2 > 0) {
      this.topInsetApplied = paramInt2;
      super.onMeasure(paramInt1, View.MeasureSpec.makeMeasureSpec(getMeasuredHeight() + paramInt2, 1073741824));
    } 
    if (this.extraMultilineHeightEnabled && this.collapsingTextHelper.getMaxLines() > 1) {
      updateTitleFromToolbarIfNeeded();
      updateTextBounds(0, 0, getMeasuredWidth(), getMeasuredHeight(), true);
      paramInt2 = this.collapsingTextHelper.getLineCount();
      if (paramInt2 > 1) {
        this.extraMultilineHeight = (paramInt2 - 1) * Math.round(this.collapsingTextHelper.getExpandedTextFullHeight());
        super.onMeasure(paramInt1, View.MeasureSpec.makeMeasureSpec(getMeasuredHeight() + this.extraMultilineHeight, 1073741824));
      } 
    } 
    ViewGroup viewGroup = this.toolbar;
    if (viewGroup != null) {
      View view = this.toolbarDirectChild;
      if (view == null || view == this) {
        setMinimumHeight(getHeightWithMargins((View)viewGroup));
        return;
      } 
      setMinimumHeight(getHeightWithMargins(view));
    } 
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    Drawable drawable = this.contentScrim;
    if (drawable != null)
      updateContentScrimBounds(drawable, paramInt1, paramInt2); 
  }
  
  WindowInsetsCompat onWindowInsetChanged(WindowInsetsCompat paramWindowInsetsCompat) {
    WindowInsetsCompat windowInsetsCompat = null;
    if (ViewCompat.getFitsSystemWindows((View)this))
      windowInsetsCompat = paramWindowInsetsCompat; 
    if (!ObjectsCompat.equals(this.lastInsets, windowInsetsCompat)) {
      this.lastInsets = windowInsetsCompat;
      requestLayout();
    } 
    return paramWindowInsetsCompat.consumeSystemWindowInsets();
  }
  
  public void setCollapsedTitleGravity(int paramInt) {
    this.collapsingTextHelper.setCollapsedTextGravity(paramInt);
  }
  
  public void setCollapsedTitleTextAppearance(int paramInt) {
    this.collapsingTextHelper.setCollapsedTextAppearance(paramInt);
  }
  
  public void setCollapsedTitleTextColor(int paramInt) {
    setCollapsedTitleTextColor(ColorStateList.valueOf(paramInt));
  }
  
  public void setCollapsedTitleTextColor(ColorStateList paramColorStateList) {
    this.collapsingTextHelper.setCollapsedTextColor(paramColorStateList);
  }
  
  public void setCollapsedTitleTypeface(Typeface paramTypeface) {
    this.collapsingTextHelper.setCollapsedTypeface(paramTypeface);
  }
  
  public void setContentScrim(Drawable paramDrawable) {
    Drawable drawable = this.contentScrim;
    if (drawable != paramDrawable) {
      Drawable drawable1 = null;
      if (drawable != null)
        drawable.setCallback(null); 
      if (paramDrawable != null)
        drawable1 = paramDrawable.mutate(); 
      this.contentScrim = drawable1;
      if (drawable1 != null) {
        updateContentScrimBounds(drawable1, getWidth(), getHeight());
        this.contentScrim.setCallback((Drawable.Callback)this);
        this.contentScrim.setAlpha(this.scrimAlpha);
      } 
      ViewCompat.postInvalidateOnAnimation((View)this);
    } 
  }
  
  public void setContentScrimColor(int paramInt) {
    setContentScrim((Drawable)new ColorDrawable(paramInt));
  }
  
  public void setContentScrimResource(int paramInt) {
    setContentScrim(ContextCompat.getDrawable(getContext(), paramInt));
  }
  
  public void setExpandedTitleColor(int paramInt) {
    setExpandedTitleTextColor(ColorStateList.valueOf(paramInt));
  }
  
  public void setExpandedTitleGravity(int paramInt) {
    this.collapsingTextHelper.setExpandedTextGravity(paramInt);
  }
  
  public void setExpandedTitleMargin(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.expandedMarginStart = paramInt1;
    this.expandedMarginTop = paramInt2;
    this.expandedMarginEnd = paramInt3;
    this.expandedMarginBottom = paramInt4;
    requestLayout();
  }
  
  public void setExpandedTitleMarginBottom(int paramInt) {
    this.expandedMarginBottom = paramInt;
    requestLayout();
  }
  
  public void setExpandedTitleMarginEnd(int paramInt) {
    this.expandedMarginEnd = paramInt;
    requestLayout();
  }
  
  public void setExpandedTitleMarginStart(int paramInt) {
    this.expandedMarginStart = paramInt;
    requestLayout();
  }
  
  public void setExpandedTitleMarginTop(int paramInt) {
    this.expandedMarginTop = paramInt;
    requestLayout();
  }
  
  public void setExpandedTitleTextAppearance(int paramInt) {
    this.collapsingTextHelper.setExpandedTextAppearance(paramInt);
  }
  
  public void setExpandedTitleTextColor(ColorStateList paramColorStateList) {
    this.collapsingTextHelper.setExpandedTextColor(paramColorStateList);
  }
  
  public void setExpandedTitleTypeface(Typeface paramTypeface) {
    this.collapsingTextHelper.setExpandedTypeface(paramTypeface);
  }
  
  public void setExtraMultilineHeightEnabled(boolean paramBoolean) {
    this.extraMultilineHeightEnabled = paramBoolean;
  }
  
  public void setForceApplySystemWindowInsetTop(boolean paramBoolean) {
    this.forceApplySystemWindowInsetTop = paramBoolean;
  }
  
  public void setHyphenationFrequency(int paramInt) {
    this.collapsingTextHelper.setHyphenationFrequency(paramInt);
  }
  
  public void setLineSpacingAdd(float paramFloat) {
    this.collapsingTextHelper.setLineSpacingAdd(paramFloat);
  }
  
  public void setLineSpacingMultiplier(float paramFloat) {
    this.collapsingTextHelper.setLineSpacingMultiplier(paramFloat);
  }
  
  public void setMaxLines(int paramInt) {
    this.collapsingTextHelper.setMaxLines(paramInt);
  }
  
  public void setRtlTextDirectionHeuristicsEnabled(boolean paramBoolean) {
    this.collapsingTextHelper.setRtlTextDirectionHeuristicsEnabled(paramBoolean);
  }
  
  void setScrimAlpha(int paramInt) {
    if (paramInt != this.scrimAlpha) {
      if (this.contentScrim != null) {
        ViewGroup viewGroup = this.toolbar;
        if (viewGroup != null)
          ViewCompat.postInvalidateOnAnimation((View)viewGroup); 
      } 
      this.scrimAlpha = paramInt;
      ViewCompat.postInvalidateOnAnimation((View)this);
    } 
  }
  
  public void setScrimAnimationDuration(long paramLong) {
    this.scrimAnimationDuration = paramLong;
  }
  
  public void setScrimVisibleHeightTrigger(int paramInt) {
    if (this.scrimVisibleHeightTrigger != paramInt) {
      this.scrimVisibleHeightTrigger = paramInt;
      updateScrimVisibility();
    } 
  }
  
  public void setScrimsShown(boolean paramBoolean) {
    boolean bool;
    if (ViewCompat.isLaidOut((View)this) && !isInEditMode()) {
      bool = true;
    } else {
      bool = false;
    } 
    setScrimsShown(paramBoolean, bool);
  }
  
  public void setScrimsShown(boolean paramBoolean1, boolean paramBoolean2) {
    if (this.scrimsAreShown != paramBoolean1) {
      char c = 'ÿ';
      if (paramBoolean2) {
        if (!paramBoolean1)
          c = Character.MIN_VALUE; 
        animateScrim(c);
      } else {
        if (!paramBoolean1)
          c = Character.MIN_VALUE; 
        setScrimAlpha(c);
      } 
      this.scrimsAreShown = paramBoolean1;
    } 
  }
  
  public void setStatusBarScrim(Drawable paramDrawable) {
    Drawable drawable = this.statusBarScrim;
    if (drawable != paramDrawable) {
      Drawable drawable1 = null;
      if (drawable != null)
        drawable.setCallback(null); 
      if (paramDrawable != null)
        drawable1 = paramDrawable.mutate(); 
      this.statusBarScrim = drawable1;
      if (drawable1 != null) {
        boolean bool;
        if (drawable1.isStateful())
          this.statusBarScrim.setState(getDrawableState()); 
        DrawableCompat.setLayoutDirection(this.statusBarScrim, ViewCompat.getLayoutDirection((View)this));
        paramDrawable = this.statusBarScrim;
        if (getVisibility() == 0) {
          bool = true;
        } else {
          bool = false;
        } 
        paramDrawable.setVisible(bool, false);
        this.statusBarScrim.setCallback((Drawable.Callback)this);
        this.statusBarScrim.setAlpha(this.scrimAlpha);
      } 
      ViewCompat.postInvalidateOnAnimation((View)this);
    } 
  }
  
  public void setStatusBarScrimColor(int paramInt) {
    setStatusBarScrim((Drawable)new ColorDrawable(paramInt));
  }
  
  public void setStatusBarScrimResource(int paramInt) {
    setStatusBarScrim(ContextCompat.getDrawable(getContext(), paramInt));
  }
  
  public void setTitle(CharSequence paramCharSequence) {
    this.collapsingTextHelper.setText(paramCharSequence);
    updateContentDescriptionFromTitle();
  }
  
  public void setTitleCollapseMode(int paramInt) {
    this.titleCollapseMode = paramInt;
    boolean bool = isTitleCollapseFadeMode();
    this.collapsingTextHelper.setFadeModeEnabled(bool);
    ViewParent viewParent = getParent();
    if (viewParent instanceof AppBarLayout)
      disableLiftOnScrollIfNeeded((AppBarLayout)viewParent); 
    if (bool && this.contentScrim == null) {
      float f = getResources().getDimension(R.dimen.design_appbar_elevation);
      setContentScrimColor(this.elevationOverlayProvider.compositeOverlayWithThemeSurfaceColorIfNeeded(f));
    } 
  }
  
  public void setTitleEnabled(boolean paramBoolean) {
    if (paramBoolean != this.collapsingTitleEnabled) {
      this.collapsingTitleEnabled = paramBoolean;
      updateContentDescriptionFromTitle();
      updateDummyView();
      requestLayout();
    } 
  }
  
  public void setVisibility(int paramInt) {
    boolean bool;
    super.setVisibility(paramInt);
    if (paramInt == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    Drawable drawable = this.statusBarScrim;
    if (drawable != null && drawable.isVisible() != bool)
      this.statusBarScrim.setVisible(bool, false); 
    drawable = this.contentScrim;
    if (drawable != null && drawable.isVisible() != bool)
      this.contentScrim.setVisible(bool, false); 
  }
  
  final void updateScrimVisibility() {
    if (this.contentScrim != null || this.statusBarScrim != null) {
      boolean bool;
      if (getHeight() + this.currentOffset < getScrimVisibleHeightTrigger()) {
        bool = true;
      } else {
        bool = false;
      } 
      setScrimsShown(bool);
    } 
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable) {
    return (super.verifyDrawable(paramDrawable) || paramDrawable == this.contentScrim || paramDrawable == this.statusBarScrim);
  }
  
  public static class LayoutParams extends FrameLayout.LayoutParams {
    public static final int COLLAPSE_MODE_OFF = 0;
    
    public static final int COLLAPSE_MODE_PARALLAX = 2;
    
    public static final int COLLAPSE_MODE_PIN = 1;
    
    private static final float DEFAULT_PARALLAX_MULTIPLIER = 0.5F;
    
    int collapseMode = 0;
    
    float parallaxMult = 0.5F;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(int param1Int1, int param1Int2, int param1Int3) {
      super(param1Int1, param1Int2, param1Int3);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.CollapsingToolbarLayout_Layout);
      this.collapseMode = typedArray.getInt(R.styleable.CollapsingToolbarLayout_Layout_layout_collapseMode, 0);
      setParallaxMultiplier(typedArray.getFloat(R.styleable.CollapsingToolbarLayout_Layout_layout_collapseParallaxMultiplier, 0.5F));
      typedArray.recycle();
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
    
    public LayoutParams(FrameLayout.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public int getCollapseMode() {
      return this.collapseMode;
    }
    
    public float getParallaxMultiplier() {
      return this.parallaxMult;
    }
    
    public void setCollapseMode(int param1Int) {
      this.collapseMode = param1Int;
    }
    
    public void setParallaxMultiplier(float param1Float) {
      this.parallaxMult = param1Float;
    }
  }
  
  private class OffsetUpdateListener implements AppBarLayout.OnOffsetChangedListener {
    final CollapsingToolbarLayout this$0;
    
    public void onOffsetChanged(AppBarLayout param1AppBarLayout, int param1Int) {
      CollapsingToolbarLayout.this.currentOffset = param1Int;
      if (CollapsingToolbarLayout.this.lastInsets != null) {
        i = CollapsingToolbarLayout.this.lastInsets.getSystemWindowInsetTop();
      } else {
        i = 0;
      } 
      int j = 0;
      int k = CollapsingToolbarLayout.this.getChildCount();
      while (j < k) {
        View view = CollapsingToolbarLayout.this.getChildAt(j);
        CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams)view.getLayoutParams();
        ViewOffsetHelper viewOffsetHelper = CollapsingToolbarLayout.getViewOffsetHelper(view);
        switch (layoutParams.collapseMode) {
          case 2:
            viewOffsetHelper.setTopAndBottomOffset(Math.round(-param1Int * layoutParams.parallaxMult));
            break;
          case 1:
            viewOffsetHelper.setTopAndBottomOffset(MathUtils.clamp(-param1Int, 0, CollapsingToolbarLayout.this.getMaxOffsetForPinChild(view)));
            break;
        } 
        j++;
      } 
      CollapsingToolbarLayout.this.updateScrimVisibility();
      if (CollapsingToolbarLayout.this.statusBarScrim != null && i)
        ViewCompat.postInvalidateOnAnimation((View)CollapsingToolbarLayout.this); 
      j = CollapsingToolbarLayout.this.getHeight();
      int i = j - ViewCompat.getMinimumHeight((View)CollapsingToolbarLayout.this) - i;
      k = CollapsingToolbarLayout.this.getScrimVisibleHeightTrigger();
      CollapsingToolbarLayout.this.collapsingTextHelper.setFadeModeStartFraction(Math.min(1.0F, (j - k) / i));
      CollapsingToolbarLayout.this.collapsingTextHelper.setCurrentOffsetY(CollapsingToolbarLayout.this.currentOffset + i);
      CollapsingToolbarLayout.this.collapsingTextHelper.setExpansionFraction(Math.abs(param1Int) / i);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TitleCollapseMode {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\appbar\CollapsingToolbarLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */