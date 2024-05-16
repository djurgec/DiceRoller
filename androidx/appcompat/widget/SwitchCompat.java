package androidx.appcompat.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.InputFilter;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.Property;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.text.AllCapsTransformationMethod;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;
import androidx.emoji2.text.EmojiCompat;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class SwitchCompat extends CompoundButton implements EmojiCompatConfigurationView {
  private static final String ACCESSIBILITY_EVENT_CLASS_NAME = "android.widget.Switch";
  
  private static final int[] CHECKED_STATE_SET;
  
  private static final int MONOSPACE = 3;
  
  private static final int SANS = 1;
  
  private static final int SERIF = 2;
  
  private static final int THUMB_ANIMATION_DURATION = 250;
  
  private static final Property<SwitchCompat, Float> THUMB_POS = new Property<SwitchCompat, Float>(Float.class, "thumbPos") {
      public Float get(SwitchCompat param1SwitchCompat) {
        return Float.valueOf(param1SwitchCompat.mThumbPosition);
      }
      
      public void set(SwitchCompat param1SwitchCompat, Float param1Float) {
        param1SwitchCompat.setThumbPosition(param1Float.floatValue());
      }
    };
  
  private static final int TOUCH_MODE_DOWN = 1;
  
  private static final int TOUCH_MODE_DRAGGING = 2;
  
  private static final int TOUCH_MODE_IDLE = 0;
  
  private AppCompatEmojiTextHelper mAppCompatEmojiTextHelper;
  
  private EmojiCompatInitCallback mEmojiCompatInitCallback;
  
  private boolean mHasThumbTint = false;
  
  private boolean mHasThumbTintMode = false;
  
  private boolean mHasTrackTint = false;
  
  private boolean mHasTrackTintMode = false;
  
  private int mMinFlingVelocity;
  
  private Layout mOffLayout;
  
  private Layout mOnLayout;
  
  ObjectAnimator mPositionAnimator;
  
  private boolean mShowText;
  
  private boolean mSplitTrack;
  
  private int mSwitchBottom;
  
  private int mSwitchHeight;
  
  private int mSwitchLeft;
  
  private int mSwitchMinWidth;
  
  private int mSwitchPadding;
  
  private int mSwitchRight;
  
  private int mSwitchTop;
  
  private TransformationMethod mSwitchTransformationMethod;
  
  private int mSwitchWidth;
  
  private final Rect mTempRect = new Rect();
  
  private ColorStateList mTextColors;
  
  private final AppCompatTextHelper mTextHelper;
  
  private CharSequence mTextOff;
  
  private CharSequence mTextOffTransformed;
  
  private CharSequence mTextOn;
  
  private CharSequence mTextOnTransformed;
  
  private final TextPaint mTextPaint;
  
  private Drawable mThumbDrawable;
  
  float mThumbPosition;
  
  private int mThumbTextPadding;
  
  private ColorStateList mThumbTintList = null;
  
  private PorterDuff.Mode mThumbTintMode = null;
  
  private int mThumbWidth;
  
  private int mTouchMode;
  
  private int mTouchSlop;
  
  private float mTouchX;
  
  private float mTouchY;
  
  private Drawable mTrackDrawable;
  
  private ColorStateList mTrackTintList = null;
  
  private PorterDuff.Mode mTrackTintMode = null;
  
  private VelocityTracker mVelocityTracker = VelocityTracker.obtain();
  
  static {
    CHECKED_STATE_SET = new int[] { 16842912 };
  }
  
  public SwitchCompat(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public SwitchCompat(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.switchStyle);
  }
  
  public SwitchCompat(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    ThemeUtils.checkAppCompatTheme((View)this, getContext());
    TextPaint textPaint = new TextPaint(1);
    this.mTextPaint = textPaint;
    textPaint.density = (getResources().getDisplayMetrics()).density;
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.SwitchCompat, paramInt, 0);
    ViewCompat.saveAttributeDataForStyleable((View)this, paramContext, R.styleable.SwitchCompat, paramAttributeSet, tintTypedArray.getWrappedTypeArray(), paramInt, 0);
    Drawable drawable = tintTypedArray.getDrawable(R.styleable.SwitchCompat_android_thumb);
    this.mThumbDrawable = drawable;
    if (drawable != null)
      drawable.setCallback((Drawable.Callback)this); 
    drawable = tintTypedArray.getDrawable(R.styleable.SwitchCompat_track);
    this.mTrackDrawable = drawable;
    if (drawable != null)
      drawable.setCallback((Drawable.Callback)this); 
    setTextOnInternal(tintTypedArray.getText(R.styleable.SwitchCompat_android_textOn));
    setTextOffInternal(tintTypedArray.getText(R.styleable.SwitchCompat_android_textOff));
    this.mShowText = tintTypedArray.getBoolean(R.styleable.SwitchCompat_showText, true);
    this.mThumbTextPadding = tintTypedArray.getDimensionPixelSize(R.styleable.SwitchCompat_thumbTextPadding, 0);
    this.mSwitchMinWidth = tintTypedArray.getDimensionPixelSize(R.styleable.SwitchCompat_switchMinWidth, 0);
    this.mSwitchPadding = tintTypedArray.getDimensionPixelSize(R.styleable.SwitchCompat_switchPadding, 0);
    this.mSplitTrack = tintTypedArray.getBoolean(R.styleable.SwitchCompat_splitTrack, false);
    ColorStateList colorStateList2 = tintTypedArray.getColorStateList(R.styleable.SwitchCompat_thumbTint);
    if (colorStateList2 != null) {
      this.mThumbTintList = colorStateList2;
      this.mHasThumbTint = true;
    } 
    PorterDuff.Mode mode2 = DrawableUtils.parseTintMode(tintTypedArray.getInt(R.styleable.SwitchCompat_thumbTintMode, -1), null);
    if (this.mThumbTintMode != mode2) {
      this.mThumbTintMode = mode2;
      this.mHasThumbTintMode = true;
    } 
    if (this.mHasThumbTint || this.mHasThumbTintMode)
      applyThumbTint(); 
    ColorStateList colorStateList1 = tintTypedArray.getColorStateList(R.styleable.SwitchCompat_trackTint);
    if (colorStateList1 != null) {
      this.mTrackTintList = colorStateList1;
      this.mHasTrackTint = true;
    } 
    PorterDuff.Mode mode1 = DrawableUtils.parseTintMode(tintTypedArray.getInt(R.styleable.SwitchCompat_trackTintMode, -1), null);
    if (this.mTrackTintMode != mode1) {
      this.mTrackTintMode = mode1;
      this.mHasTrackTintMode = true;
    } 
    if (this.mHasTrackTint || this.mHasTrackTintMode)
      applyTrackTint(); 
    int i = tintTypedArray.getResourceId(R.styleable.SwitchCompat_switchTextAppearance, 0);
    if (i != 0)
      setSwitchTextAppearance(paramContext, i); 
    AppCompatTextHelper appCompatTextHelper = new AppCompatTextHelper((TextView)this);
    this.mTextHelper = appCompatTextHelper;
    appCompatTextHelper.loadFromAttributes(paramAttributeSet, paramInt);
    tintTypedArray.recycle();
    ViewConfiguration viewConfiguration = ViewConfiguration.get(paramContext);
    this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
    this.mMinFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    getEmojiTextViewHelper().loadFromAttributes(paramAttributeSet, paramInt);
    refreshDrawableState();
    setChecked(isChecked());
  }
  
  private void animateThumbToCheckedState(boolean paramBoolean) {
    float f;
    if (paramBoolean) {
      f = 1.0F;
    } else {
      f = 0.0F;
    } 
    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, THUMB_POS, new float[] { f });
    this.mPositionAnimator = objectAnimator;
    objectAnimator.setDuration(250L);
    if (Build.VERSION.SDK_INT >= 18)
      this.mPositionAnimator.setAutoCancel(true); 
    this.mPositionAnimator.start();
  }
  
  private void applyThumbTint() {
    Drawable drawable = this.mThumbDrawable;
    if (drawable != null && (this.mHasThumbTint || this.mHasThumbTintMode)) {
      drawable = DrawableCompat.wrap(drawable).mutate();
      this.mThumbDrawable = drawable;
      if (this.mHasThumbTint)
        DrawableCompat.setTintList(drawable, this.mThumbTintList); 
      if (this.mHasThumbTintMode)
        DrawableCompat.setTintMode(this.mThumbDrawable, this.mThumbTintMode); 
      if (this.mThumbDrawable.isStateful())
        this.mThumbDrawable.setState(getDrawableState()); 
    } 
  }
  
  private void applyTrackTint() {
    Drawable drawable = this.mTrackDrawable;
    if (drawable != null && (this.mHasTrackTint || this.mHasTrackTintMode)) {
      drawable = DrawableCompat.wrap(drawable).mutate();
      this.mTrackDrawable = drawable;
      if (this.mHasTrackTint)
        DrawableCompat.setTintList(drawable, this.mTrackTintList); 
      if (this.mHasTrackTintMode)
        DrawableCompat.setTintMode(this.mTrackDrawable, this.mTrackTintMode); 
      if (this.mTrackDrawable.isStateful())
        this.mTrackDrawable.setState(getDrawableState()); 
    } 
  }
  
  private void cancelPositionAnimator() {
    ObjectAnimator objectAnimator = this.mPositionAnimator;
    if (objectAnimator != null)
      objectAnimator.cancel(); 
  }
  
  private void cancelSuperTouch(MotionEvent paramMotionEvent) {
    paramMotionEvent = MotionEvent.obtain(paramMotionEvent);
    paramMotionEvent.setAction(3);
    super.onTouchEvent(paramMotionEvent);
    paramMotionEvent.recycle();
  }
  
  private static float constrain(float paramFloat1, float paramFloat2, float paramFloat3) {
    if (paramFloat1 < paramFloat2) {
      paramFloat1 = paramFloat2;
    } else if (paramFloat1 > paramFloat3) {
      paramFloat1 = paramFloat3;
    } 
    return paramFloat1;
  }
  
  private CharSequence doTransformForOnOffText(CharSequence paramCharSequence) {
    TransformationMethod transformationMethod = getEmojiTextViewHelper().wrapTransformationMethod(this.mSwitchTransformationMethod);
    if (transformationMethod != null)
      paramCharSequence = transformationMethod.getTransformation(paramCharSequence, (View)this); 
    return paramCharSequence;
  }
  
  private AppCompatEmojiTextHelper getEmojiTextViewHelper() {
    if (this.mAppCompatEmojiTextHelper == null)
      this.mAppCompatEmojiTextHelper = new AppCompatEmojiTextHelper((TextView)this); 
    return this.mAppCompatEmojiTextHelper;
  }
  
  private boolean getTargetCheckedState() {
    boolean bool;
    if (this.mThumbPosition > 0.5F) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private int getThumbOffset() {
    float f;
    if (ViewUtils.isLayoutRtl((View)this)) {
      f = 1.0F - this.mThumbPosition;
    } else {
      f = this.mThumbPosition;
    } 
    return (int)(getThumbScrollRange() * f + 0.5F);
  }
  
  private int getThumbScrollRange() {
    Drawable drawable = this.mTrackDrawable;
    if (drawable != null) {
      Rect rect1;
      Rect rect2 = this.mTempRect;
      drawable.getPadding(rect2);
      drawable = this.mThumbDrawable;
      if (drawable != null) {
        rect1 = DrawableUtils.getOpticalBounds(drawable);
      } else {
        rect1 = DrawableUtils.INSETS_NONE;
      } 
      return this.mSwitchWidth - this.mThumbWidth - rect2.left - rect2.right - rect1.left - rect1.right;
    } 
    return 0;
  }
  
  private boolean hitThumb(float paramFloat1, float paramFloat2) {
    Drawable drawable = this.mThumbDrawable;
    boolean bool2 = false;
    if (drawable == null)
      return false; 
    int k = getThumbOffset();
    this.mThumbDrawable.getPadding(this.mTempRect);
    int i = this.mSwitchTop;
    int j = this.mTouchSlop;
    int n = this.mSwitchLeft + k - j;
    int m = this.mThumbWidth;
    k = this.mTempRect.left;
    int i3 = this.mTempRect.right;
    int i1 = this.mTouchSlop;
    int i2 = this.mSwitchBottom;
    boolean bool1 = bool2;
    if (paramFloat1 > n) {
      bool1 = bool2;
      if (paramFloat1 < (m + n + k + i3 + i1)) {
        bool1 = bool2;
        if (paramFloat2 > (i - j)) {
          bool1 = bool2;
          if (paramFloat2 < (i2 + i1))
            bool1 = true; 
        } 
      } 
    } 
    return bool1;
  }
  
  private Layout makeLayout(CharSequence paramCharSequence) {
    boolean bool;
    TextPaint textPaint = this.mTextPaint;
    if (paramCharSequence != null) {
      bool = (int)Math.ceil(Layout.getDesiredWidth(paramCharSequence, textPaint));
    } else {
      bool = false;
    } 
    return (Layout)new StaticLayout(paramCharSequence, textPaint, bool, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
  }
  
  private void setOffStateDescriptionOnRAndAbove() {
    if (Build.VERSION.SDK_INT >= 30) {
      CharSequence charSequence2 = this.mTextOff;
      CharSequence charSequence1 = charSequence2;
      if (charSequence2 == null)
        charSequence1 = getResources().getString(R.string.abc_capital_off); 
      ViewCompat.setStateDescription((View)this, charSequence1);
    } 
  }
  
  private void setOnStateDescriptionOnRAndAbove() {
    if (Build.VERSION.SDK_INT >= 30) {
      CharSequence charSequence2 = this.mTextOn;
      CharSequence charSequence1 = charSequence2;
      if (charSequence2 == null)
        charSequence1 = getResources().getString(R.string.abc_capital_on); 
      ViewCompat.setStateDescription((View)this, charSequence1);
    } 
  }
  
  private void setSwitchTypefaceByIndex(int paramInt1, int paramInt2) {
    Typeface typeface = null;
    switch (paramInt1) {
      case 3:
        typeface = Typeface.MONOSPACE;
        break;
      case 2:
        typeface = Typeface.SERIF;
        break;
      case 1:
        typeface = Typeface.SANS_SERIF;
        break;
    } 
    setSwitchTypeface(typeface, paramInt2);
  }
  
  private void setTextOffInternal(CharSequence paramCharSequence) {
    this.mTextOff = paramCharSequence;
    this.mTextOffTransformed = doTransformForOnOffText(paramCharSequence);
    this.mOffLayout = null;
    if (this.mShowText)
      setupEmojiCompatLoadCallback(); 
  }
  
  private void setTextOnInternal(CharSequence paramCharSequence) {
    this.mTextOn = paramCharSequence;
    this.mTextOnTransformed = doTransformForOnOffText(paramCharSequence);
    this.mOnLayout = null;
    if (this.mShowText)
      setupEmojiCompatLoadCallback(); 
  }
  
  private void setupEmojiCompatLoadCallback() {
    if (this.mEmojiCompatInitCallback != null || !this.mAppCompatEmojiTextHelper.isEnabled())
      return; 
    if (EmojiCompat.isConfigured()) {
      EmojiCompat emojiCompat = EmojiCompat.get();
      int i = emojiCompat.getLoadState();
      if (i == 3 || i == 0) {
        EmojiCompatInitCallback emojiCompatInitCallback = new EmojiCompatInitCallback(this);
        this.mEmojiCompatInitCallback = emojiCompatInitCallback;
        emojiCompat.registerInitCallback(emojiCompatInitCallback);
      } 
    } 
  }
  
  private void stopDrag(MotionEvent paramMotionEvent) {
    this.mTouchMode = 0;
    int i = paramMotionEvent.getAction();
    boolean bool1 = true;
    if (i == 1 && isEnabled()) {
      i = 1;
    } else {
      i = 0;
    } 
    boolean bool2 = isChecked();
    if (i != 0) {
      this.mVelocityTracker.computeCurrentVelocity(1000);
      float f = this.mVelocityTracker.getXVelocity();
      if (Math.abs(f) > this.mMinFlingVelocity) {
        if (ViewUtils.isLayoutRtl((View)this) ? (f < 0.0F) : (f > 0.0F))
          bool1 = false; 
      } else {
        bool1 = getTargetCheckedState();
      } 
    } else {
      bool1 = bool2;
    } 
    if (bool1 != bool2)
      playSoundEffect(0); 
    setChecked(bool1);
    cancelSuperTouch(paramMotionEvent);
  }
  
  public void draw(Canvas paramCanvas) {
    Rect rect1;
    Rect rect2 = this.mTempRect;
    int m = this.mSwitchLeft;
    int n = this.mSwitchTop;
    int j = this.mSwitchRight;
    int i1 = this.mSwitchBottom;
    int k = getThumbOffset() + m;
    Drawable drawable2 = this.mThumbDrawable;
    if (drawable2 != null) {
      rect1 = DrawableUtils.getOpticalBounds(drawable2);
    } else {
      rect1 = DrawableUtils.INSETS_NONE;
    } 
    Drawable drawable3 = this.mTrackDrawable;
    int i = k;
    if (drawable3 != null) {
      drawable3.getPadding(rect2);
      int i7 = k + rect2.left;
      k = n;
      int i2 = i1;
      int i3 = m;
      int i5 = k;
      int i4 = j;
      int i6 = i2;
      if (rect1 != null) {
        i = m;
        if (rect1.left > rect2.left)
          i = m + rect1.left - rect2.left; 
        m = k;
        if (rect1.top > rect2.top)
          m = k + rect1.top - rect2.top; 
        k = j;
        if (rect1.right > rect2.right)
          k = j - rect1.right - rect2.right; 
        i3 = i;
        i5 = m;
        i4 = k;
        i6 = i2;
        if (rect1.bottom > rect2.bottom) {
          i6 = i2 - rect1.bottom - rect2.bottom;
          i4 = k;
          i5 = m;
          i3 = i;
        } 
      } 
      this.mTrackDrawable.setBounds(i3, i5, i4, i6);
      i = i7;
    } 
    Drawable drawable1 = this.mThumbDrawable;
    if (drawable1 != null) {
      drawable1.getPadding(rect2);
      j = i - rect2.left;
      i = this.mThumbWidth + i + rect2.right;
      this.mThumbDrawable.setBounds(j, n, i, i1);
      drawable1 = getBackground();
      if (drawable1 != null)
        DrawableCompat.setHotspotBounds(drawable1, j, n, i, i1); 
    } 
    super.draw(paramCanvas);
  }
  
  public void drawableHotspotChanged(float paramFloat1, float paramFloat2) {
    if (Build.VERSION.SDK_INT >= 21)
      super.drawableHotspotChanged(paramFloat1, paramFloat2); 
    Drawable drawable = this.mThumbDrawable;
    if (drawable != null)
      DrawableCompat.setHotspot(drawable, paramFloat1, paramFloat2); 
    drawable = this.mTrackDrawable;
    if (drawable != null)
      DrawableCompat.setHotspot(drawable, paramFloat1, paramFloat2); 
  }
  
  protected void drawableStateChanged() {
    boolean bool;
    super.drawableStateChanged();
    int[] arrayOfInt = getDrawableState();
    int j = 0;
    Drawable drawable = this.mThumbDrawable;
    int i = j;
    if (drawable != null) {
      i = j;
      if (drawable.isStateful())
        i = false | drawable.setState(arrayOfInt); 
    } 
    drawable = this.mTrackDrawable;
    j = i;
    if (drawable != null) {
      j = i;
      if (drawable.isStateful())
        bool = i | drawable.setState(arrayOfInt); 
    } 
    if (bool)
      invalidate(); 
  }
  
  public int getCompoundPaddingLeft() {
    if (!ViewUtils.isLayoutRtl((View)this))
      return super.getCompoundPaddingLeft(); 
    int j = super.getCompoundPaddingLeft() + this.mSwitchWidth;
    int i = j;
    if (!TextUtils.isEmpty(getText()))
      i = j + this.mSwitchPadding; 
    return i;
  }
  
  public int getCompoundPaddingRight() {
    if (ViewUtils.isLayoutRtl((View)this))
      return super.getCompoundPaddingRight(); 
    int j = super.getCompoundPaddingRight() + this.mSwitchWidth;
    int i = j;
    if (!TextUtils.isEmpty(getText()))
      i = j + this.mSwitchPadding; 
    return i;
  }
  
  public ActionMode.Callback getCustomSelectionActionModeCallback() {
    return TextViewCompat.unwrapCustomSelectionActionModeCallback(super.getCustomSelectionActionModeCallback());
  }
  
  public boolean getShowText() {
    return this.mShowText;
  }
  
  public boolean getSplitTrack() {
    return this.mSplitTrack;
  }
  
  public int getSwitchMinWidth() {
    return this.mSwitchMinWidth;
  }
  
  public int getSwitchPadding() {
    return this.mSwitchPadding;
  }
  
  public CharSequence getTextOff() {
    return this.mTextOff;
  }
  
  public CharSequence getTextOn() {
    return this.mTextOn;
  }
  
  public Drawable getThumbDrawable() {
    return this.mThumbDrawable;
  }
  
  public int getThumbTextPadding() {
    return this.mThumbTextPadding;
  }
  
  public ColorStateList getThumbTintList() {
    return this.mThumbTintList;
  }
  
  public PorterDuff.Mode getThumbTintMode() {
    return this.mThumbTintMode;
  }
  
  public Drawable getTrackDrawable() {
    return this.mTrackDrawable;
  }
  
  public ColorStateList getTrackTintList() {
    return this.mTrackTintList;
  }
  
  public PorterDuff.Mode getTrackTintMode() {
    return this.mTrackTintMode;
  }
  
  public boolean isEmojiCompatEnabled() {
    return getEmojiTextViewHelper().isEnabled();
  }
  
  public void jumpDrawablesToCurrentState() {
    super.jumpDrawablesToCurrentState();
    Drawable drawable = this.mThumbDrawable;
    if (drawable != null)
      drawable.jumpToCurrentState(); 
    drawable = this.mTrackDrawable;
    if (drawable != null)
      drawable.jumpToCurrentState(); 
    ObjectAnimator objectAnimator = this.mPositionAnimator;
    if (objectAnimator != null && objectAnimator.isStarted()) {
      this.mPositionAnimator.end();
      this.mPositionAnimator = null;
    } 
  }
  
  protected int[] onCreateDrawableState(int paramInt) {
    int[] arrayOfInt = super.onCreateDrawableState(paramInt + 1);
    if (isChecked())
      mergeDrawableStates(arrayOfInt, CHECKED_STATE_SET); 
    return arrayOfInt;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    Layout layout;
    super.onDraw(paramCanvas);
    Rect rect = this.mTempRect;
    Drawable drawable2 = this.mTrackDrawable;
    if (drawable2 != null) {
      drawable2.getPadding(rect);
    } else {
      rect.setEmpty();
    } 
    int j = this.mSwitchTop;
    int m = this.mSwitchBottom;
    int n = rect.top;
    int k = rect.bottom;
    Drawable drawable1 = this.mThumbDrawable;
    if (drawable2 != null)
      if (this.mSplitTrack && drawable1 != null) {
        Rect rect1 = DrawableUtils.getOpticalBounds(drawable1);
        drawable1.copyBounds(rect);
        rect.left += rect1.left;
        rect.right -= rect1.right;
        int i1 = paramCanvas.save();
        paramCanvas.clipRect(rect, Region.Op.DIFFERENCE);
        drawable2.draw(paramCanvas);
        paramCanvas.restoreToCount(i1);
      } else {
        drawable2.draw(paramCanvas);
      }  
    int i = paramCanvas.save();
    if (drawable1 != null)
      drawable1.draw(paramCanvas); 
    if (getTargetCheckedState()) {
      layout = this.mOnLayout;
    } else {
      layout = this.mOffLayout;
    } 
    if (layout != null) {
      int[] arrayOfInt = getDrawableState();
      ColorStateList colorStateList = this.mTextColors;
      if (colorStateList != null)
        this.mTextPaint.setColor(colorStateList.getColorForState(arrayOfInt, 0)); 
      this.mTextPaint.drawableState = arrayOfInt;
      if (drawable1 != null) {
        Rect rect1 = drawable1.getBounds();
        i1 = rect1.left + rect1.right;
      } else {
        i1 = getWidth();
      } 
      int i2 = i1 / 2;
      int i1 = layout.getWidth() / 2;
      j = (n + j + m - k) / 2;
      k = layout.getHeight() / 2;
      paramCanvas.translate((i2 - i1), (j - k));
      layout.draw(paramCanvas);
    } 
    paramCanvas.restoreToCount(i);
  }
  
  void onEmojiCompatInitializedForSwitchText() {
    setTextOnInternal(this.mTextOn);
    setTextOffInternal(this.mTextOff);
    requestLayout();
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    paramAccessibilityEvent.setClassName("android.widget.Switch");
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName("android.widget.Switch");
    if (Build.VERSION.SDK_INT < 30) {
      CharSequence charSequence;
      if (isChecked()) {
        charSequence = this.mTextOn;
      } else {
        charSequence = this.mTextOff;
      } 
      if (!TextUtils.isEmpty(charSequence)) {
        CharSequence charSequence1 = paramAccessibilityNodeInfo.getText();
        if (TextUtils.isEmpty(charSequence1)) {
          paramAccessibilityNodeInfo.setText(charSequence);
        } else {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(charSequence1).append(' ').append(charSequence);
          paramAccessibilityNodeInfo.setText(stringBuilder);
        } 
      } 
    } 
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i;
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    paramInt1 = 0;
    paramInt2 = 0;
    if (this.mThumbDrawable != null) {
      Rect rect1 = this.mTempRect;
      Drawable drawable = this.mTrackDrawable;
      if (drawable != null) {
        drawable.getPadding(rect1);
      } else {
        rect1.setEmpty();
      } 
      Rect rect2 = DrawableUtils.getOpticalBounds(this.mThumbDrawable);
      paramInt1 = Math.max(0, rect2.left - rect1.left);
      paramInt2 = Math.max(0, rect2.right - rect1.right);
    } 
    if (ViewUtils.isLayoutRtl((View)this)) {
      paramInt4 = getPaddingLeft() + paramInt1;
      paramInt3 = this.mSwitchWidth + paramInt4 - paramInt1 - paramInt2;
    } else {
      paramInt3 = getWidth() - getPaddingRight() - paramInt2;
      paramInt4 = paramInt3 - this.mSwitchWidth + paramInt1 + paramInt2;
    } 
    switch (getGravity() & 0x70) {
      default:
        paramInt2 = getPaddingTop();
        paramInt1 = this.mSwitchHeight + paramInt2;
        break;
      case 80:
        paramInt1 = getHeight() - getPaddingBottom();
        paramInt2 = paramInt1 - this.mSwitchHeight;
        break;
      case 16:
        paramInt1 = (getPaddingTop() + getHeight() - getPaddingBottom()) / 2;
        i = this.mSwitchHeight;
        paramInt1 -= i / 2;
        paramInt2 = paramInt1;
        paramInt1 = i + paramInt1;
        break;
    } 
    this.mSwitchLeft = paramInt4;
    this.mSwitchTop = paramInt2;
    this.mSwitchBottom = paramInt1;
    this.mSwitchRight = paramInt3;
  }
  
  public void onMeasure(int paramInt1, int paramInt2) {
    int j;
    if (this.mShowText) {
      if (this.mOnLayout == null)
        this.mOnLayout = makeLayout(this.mTextOnTransformed); 
      if (this.mOffLayout == null)
        this.mOffLayout = makeLayout(this.mTextOffTransformed); 
    } 
    Rect rect = this.mTempRect;
    Drawable drawable2 = this.mThumbDrawable;
    if (drawable2 != null) {
      drawable2.getPadding(rect);
      j = this.mThumbDrawable.getIntrinsicWidth() - rect.left - rect.right;
      i = this.mThumbDrawable.getIntrinsicHeight();
    } else {
      j = 0;
      i = 0;
    } 
    if (this.mShowText) {
      k = Math.max(this.mOnLayout.getWidth(), this.mOffLayout.getWidth()) + this.mThumbTextPadding * 2;
    } else {
      k = 0;
    } 
    this.mThumbWidth = Math.max(k, j);
    drawable2 = this.mTrackDrawable;
    if (drawable2 != null) {
      drawable2.getPadding(rect);
      j = this.mTrackDrawable.getIntrinsicHeight();
    } else {
      rect.setEmpty();
      j = 0;
    } 
    int i1 = rect.left;
    int n = rect.right;
    Drawable drawable1 = this.mThumbDrawable;
    int m = i1;
    int k = n;
    if (drawable1 != null) {
      Rect rect1 = DrawableUtils.getOpticalBounds(drawable1);
      m = Math.max(i1, rect1.left);
      k = Math.max(n, rect1.right);
    } 
    k = Math.max(this.mSwitchMinWidth, this.mThumbWidth * 2 + m + k);
    int i = Math.max(j, i);
    this.mSwitchWidth = k;
    this.mSwitchHeight = i;
    super.onMeasure(paramInt1, paramInt2);
    if (getMeasuredHeight() < i)
      setMeasuredDimension(getMeasuredWidthAndState(), i); 
  }
  
  public void onPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    CharSequence charSequence;
    super.onPopulateAccessibilityEvent(paramAccessibilityEvent);
    if (isChecked()) {
      charSequence = this.mTextOn;
    } else {
      charSequence = this.mTextOff;
    } 
    if (charSequence != null)
      paramAccessibilityEvent.getText().add(charSequence); 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    float f3;
    int i;
    this.mVelocityTracker.addMovement(paramMotionEvent);
    switch (paramMotionEvent.getActionMasked()) {
      default:
        return super.onTouchEvent(paramMotionEvent);
      case 2:
        switch (this.mTouchMode) {
          case 2:
            f3 = paramMotionEvent.getX();
            i = getThumbScrollRange();
            f1 = f3 - this.mTouchX;
            if (i != 0) {
              f1 /= i;
            } else if (f1 > 0.0F) {
              f1 = 1.0F;
            } else {
              f1 = -1.0F;
            } 
            f2 = f1;
            if (ViewUtils.isLayoutRtl((View)this))
              f2 = -f1; 
            f1 = constrain(this.mThumbPosition + f2, 0.0F, 1.0F);
            if (f1 != this.mThumbPosition) {
              this.mTouchX = f3;
              setThumbPosition(f1);
            } 
            return true;
          case 1:
            f2 = paramMotionEvent.getX();
            f1 = paramMotionEvent.getY();
            if (Math.abs(f2 - this.mTouchX) > this.mTouchSlop || Math.abs(f1 - this.mTouchY) > this.mTouchSlop) {
              this.mTouchMode = 2;
              getParent().requestDisallowInterceptTouchEvent(true);
              this.mTouchX = f2;
              this.mTouchY = f1;
              return true;
            } 
            break;
        } 
      case 1:
      case 3:
        if (this.mTouchMode == 2) {
          stopDrag(paramMotionEvent);
          super.onTouchEvent(paramMotionEvent);
          return true;
        } 
        this.mTouchMode = 0;
        this.mVelocityTracker.clear();
      case 0:
        break;
    } 
    float f1 = paramMotionEvent.getX();
    float f2 = paramMotionEvent.getY();
    if (isEnabled() && hitThumb(f1, f2)) {
      this.mTouchMode = 1;
      this.mTouchX = f1;
      this.mTouchY = f2;
    } 
  }
  
  public void setAllCaps(boolean paramBoolean) {
    super.setAllCaps(paramBoolean);
    getEmojiTextViewHelper().setAllCaps(paramBoolean);
  }
  
  public void setChecked(boolean paramBoolean) {
    super.setChecked(paramBoolean);
    paramBoolean = isChecked();
    if (paramBoolean) {
      setOnStateDescriptionOnRAndAbove();
    } else {
      setOffStateDescriptionOnRAndAbove();
    } 
    if (getWindowToken() != null && ViewCompat.isLaidOut((View)this)) {
      animateThumbToCheckedState(paramBoolean);
    } else {
      float f;
      cancelPositionAnimator();
      if (paramBoolean) {
        f = 1.0F;
      } else {
        f = 0.0F;
      } 
      setThumbPosition(f);
    } 
  }
  
  public void setCustomSelectionActionModeCallback(ActionMode.Callback paramCallback) {
    super.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback((TextView)this, paramCallback));
  }
  
  public void setEmojiCompatEnabled(boolean paramBoolean) {
    getEmojiTextViewHelper().setEnabled(paramBoolean);
    setTextOnInternal(this.mTextOn);
    setTextOffInternal(this.mTextOff);
    requestLayout();
  }
  
  public void setFilters(InputFilter[] paramArrayOfInputFilter) {
    super.setFilters(getEmojiTextViewHelper().getFilters(paramArrayOfInputFilter));
  }
  
  public void setShowText(boolean paramBoolean) {
    if (this.mShowText != paramBoolean) {
      this.mShowText = paramBoolean;
      requestLayout();
      if (paramBoolean)
        setupEmojiCompatLoadCallback(); 
    } 
  }
  
  public void setSplitTrack(boolean paramBoolean) {
    this.mSplitTrack = paramBoolean;
    invalidate();
  }
  
  public void setSwitchMinWidth(int paramInt) {
    this.mSwitchMinWidth = paramInt;
    requestLayout();
  }
  
  public void setSwitchPadding(int paramInt) {
    this.mSwitchPadding = paramInt;
    requestLayout();
  }
  
  public void setSwitchTextAppearance(Context paramContext, int paramInt) {
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramInt, R.styleable.TextAppearance);
    ColorStateList colorStateList = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColor);
    if (colorStateList != null) {
      this.mTextColors = colorStateList;
    } else {
      this.mTextColors = getTextColors();
    } 
    paramInt = tintTypedArray.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, 0);
    if (paramInt != 0 && paramInt != this.mTextPaint.getTextSize()) {
      this.mTextPaint.setTextSize(paramInt);
      requestLayout();
    } 
    setSwitchTypefaceByIndex(tintTypedArray.getInt(R.styleable.TextAppearance_android_typeface, -1), tintTypedArray.getInt(R.styleable.TextAppearance_android_textStyle, -1));
    if (tintTypedArray.getBoolean(R.styleable.TextAppearance_textAllCaps, false)) {
      this.mSwitchTransformationMethod = (TransformationMethod)new AllCapsTransformationMethod(getContext());
    } else {
      this.mSwitchTransformationMethod = null;
    } 
    setTextOnInternal(this.mTextOn);
    setTextOffInternal(this.mTextOff);
    tintTypedArray.recycle();
  }
  
  public void setSwitchTypeface(Typeface paramTypeface) {
    if ((this.mTextPaint.getTypeface() != null && !this.mTextPaint.getTypeface().equals(paramTypeface)) || (this.mTextPaint.getTypeface() == null && paramTypeface != null)) {
      this.mTextPaint.setTypeface(paramTypeface);
      requestLayout();
      invalidate();
    } 
  }
  
  public void setSwitchTypeface(Typeface paramTypeface, int paramInt) {
    TextPaint textPaint;
    float f = 0.0F;
    boolean bool = false;
    if (paramInt > 0) {
      boolean bool1;
      if (paramTypeface == null) {
        paramTypeface = Typeface.defaultFromStyle(paramInt);
      } else {
        paramTypeface = Typeface.create(paramTypeface, paramInt);
      } 
      setSwitchTypeface(paramTypeface);
      if (paramTypeface != null) {
        bool1 = paramTypeface.getStyle();
      } else {
        bool1 = false;
      } 
      paramInt = (bool1 ^ 0xFFFFFFFF) & paramInt;
      textPaint = this.mTextPaint;
      if ((paramInt & 0x1) != 0)
        bool = true; 
      textPaint.setFakeBoldText(bool);
      textPaint = this.mTextPaint;
      if ((paramInt & 0x2) != 0)
        f = -0.25F; 
      textPaint.setTextSkewX(f);
    } else {
      this.mTextPaint.setFakeBoldText(false);
      this.mTextPaint.setTextSkewX(0.0F);
      setSwitchTypeface((Typeface)textPaint);
    } 
  }
  
  public void setTextOff(CharSequence paramCharSequence) {
    setTextOffInternal(paramCharSequence);
    requestLayout();
    if (!isChecked())
      setOffStateDescriptionOnRAndAbove(); 
  }
  
  public void setTextOn(CharSequence paramCharSequence) {
    setTextOnInternal(paramCharSequence);
    requestLayout();
    if (isChecked())
      setOnStateDescriptionOnRAndAbove(); 
  }
  
  public void setThumbDrawable(Drawable paramDrawable) {
    Drawable drawable = this.mThumbDrawable;
    if (drawable != null)
      drawable.setCallback(null); 
    this.mThumbDrawable = paramDrawable;
    if (paramDrawable != null)
      paramDrawable.setCallback((Drawable.Callback)this); 
    requestLayout();
  }
  
  void setThumbPosition(float paramFloat) {
    this.mThumbPosition = paramFloat;
    invalidate();
  }
  
  public void setThumbResource(int paramInt) {
    setThumbDrawable(AppCompatResources.getDrawable(getContext(), paramInt));
  }
  
  public void setThumbTextPadding(int paramInt) {
    this.mThumbTextPadding = paramInt;
    requestLayout();
  }
  
  public void setThumbTintList(ColorStateList paramColorStateList) {
    this.mThumbTintList = paramColorStateList;
    this.mHasThumbTint = true;
    applyThumbTint();
  }
  
  public void setThumbTintMode(PorterDuff.Mode paramMode) {
    this.mThumbTintMode = paramMode;
    this.mHasThumbTintMode = true;
    applyThumbTint();
  }
  
  public void setTrackDrawable(Drawable paramDrawable) {
    Drawable drawable = this.mTrackDrawable;
    if (drawable != null)
      drawable.setCallback(null); 
    this.mTrackDrawable = paramDrawable;
    if (paramDrawable != null)
      paramDrawable.setCallback((Drawable.Callback)this); 
    requestLayout();
  }
  
  public void setTrackResource(int paramInt) {
    setTrackDrawable(AppCompatResources.getDrawable(getContext(), paramInt));
  }
  
  public void setTrackTintList(ColorStateList paramColorStateList) {
    this.mTrackTintList = paramColorStateList;
    this.mHasTrackTint = true;
    applyTrackTint();
  }
  
  public void setTrackTintMode(PorterDuff.Mode paramMode) {
    this.mTrackTintMode = paramMode;
    this.mHasTrackTintMode = true;
    applyTrackTint();
  }
  
  public void toggle() {
    setChecked(isChecked() ^ true);
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable) {
    return (super.verifyDrawable(paramDrawable) || paramDrawable == this.mThumbDrawable || paramDrawable == this.mTrackDrawable);
  }
  
  static class EmojiCompatInitCallback extends EmojiCompat.InitCallback {
    private final Reference<SwitchCompat> mOuterWeakRef;
    
    EmojiCompatInitCallback(SwitchCompat param1SwitchCompat) {
      this.mOuterWeakRef = new WeakReference<>(param1SwitchCompat);
    }
    
    public void onFailed(Throwable param1Throwable) {
      SwitchCompat switchCompat = this.mOuterWeakRef.get();
      if (switchCompat != null)
        switchCompat.onEmojiCompatInitializedForSwitchText(); 
    }
    
    public void onInitialized() {
      SwitchCompat switchCompat = this.mOuterWeakRef.get();
      if (switchCompat != null)
        switchCompat.onEmojiCompatInitializedForSwitchText(); 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\SwitchCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */