package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.LocaleList;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.widget.AutoSizeableTextView;
import androidx.core.widget.TextViewCompat;
import java.lang.ref.WeakReference;
import java.util.Locale;

class AppCompatTextHelper {
  private static final int MONOSPACE = 3;
  
  private static final int SANS = 1;
  
  private static final int SERIF = 2;
  
  private static final int TEXT_FONT_WEIGHT_UNSPECIFIED = -1;
  
  private boolean mAsyncFontPending;
  
  private final AppCompatTextViewAutoSizeHelper mAutoSizeTextHelper;
  
  private TintInfo mDrawableBottomTint;
  
  private TintInfo mDrawableEndTint;
  
  private TintInfo mDrawableLeftTint;
  
  private TintInfo mDrawableRightTint;
  
  private TintInfo mDrawableStartTint;
  
  private TintInfo mDrawableTint;
  
  private TintInfo mDrawableTopTint;
  
  private Typeface mFontTypeface;
  
  private int mFontWeight = -1;
  
  private int mStyle = 0;
  
  private final TextView mView;
  
  AppCompatTextHelper(TextView paramTextView) {
    this.mView = paramTextView;
    this.mAutoSizeTextHelper = new AppCompatTextViewAutoSizeHelper(paramTextView);
  }
  
  private void applyCompoundDrawableTint(Drawable paramDrawable, TintInfo paramTintInfo) {
    if (paramDrawable != null && paramTintInfo != null)
      AppCompatDrawableManager.tintDrawable(paramDrawable, paramTintInfo, this.mView.getDrawableState()); 
  }
  
  private static TintInfo createTintInfo(Context paramContext, AppCompatDrawableManager paramAppCompatDrawableManager, int paramInt) {
    ColorStateList colorStateList = paramAppCompatDrawableManager.getTintList(paramContext, paramInt);
    if (colorStateList != null) {
      TintInfo tintInfo = new TintInfo();
      tintInfo.mHasTintList = true;
      tintInfo.mTintList = colorStateList;
      return tintInfo;
    } 
    return null;
  }
  
  private void setCompoundDrawables(Drawable paramDrawable1, Drawable paramDrawable2, Drawable paramDrawable3, Drawable paramDrawable4, Drawable paramDrawable5, Drawable paramDrawable6) {
    if (Build.VERSION.SDK_INT >= 17 && (paramDrawable5 != null || paramDrawable6 != null)) {
      Drawable[] arrayOfDrawable = this.mView.getCompoundDrawablesRelative();
      TextView textView = this.mView;
      if (paramDrawable5 != null) {
        paramDrawable1 = paramDrawable5;
      } else {
        paramDrawable1 = arrayOfDrawable[0];
      } 
      if (paramDrawable2 == null)
        paramDrawable2 = arrayOfDrawable[1]; 
      if (paramDrawable6 != null) {
        paramDrawable3 = paramDrawable6;
      } else {
        paramDrawable3 = arrayOfDrawable[2];
      } 
      if (paramDrawable4 == null)
        paramDrawable4 = arrayOfDrawable[3]; 
      textView.setCompoundDrawablesRelativeWithIntrinsicBounds(paramDrawable1, paramDrawable2, paramDrawable3, paramDrawable4);
    } else if (paramDrawable1 != null || paramDrawable2 != null || paramDrawable3 != null || paramDrawable4 != null) {
      TextView textView1;
      Drawable drawable;
      if (Build.VERSION.SDK_INT >= 17) {
        Drawable[] arrayOfDrawable1 = this.mView.getCompoundDrawablesRelative();
        if (arrayOfDrawable1[0] != null || arrayOfDrawable1[2] != null) {
          textView1 = this.mView;
          paramDrawable5 = arrayOfDrawable1[0];
          if (paramDrawable2 != null) {
            paramDrawable1 = paramDrawable2;
          } else {
            paramDrawable1 = arrayOfDrawable1[1];
          } 
          Drawable drawable1 = arrayOfDrawable1[2];
          if (paramDrawable4 != null) {
            paramDrawable2 = paramDrawable4;
          } else {
            paramDrawable2 = arrayOfDrawable1[3];
          } 
          textView1.setCompoundDrawablesRelativeWithIntrinsicBounds(paramDrawable5, paramDrawable1, drawable1, paramDrawable2);
          return;
        } 
      } 
      Drawable[] arrayOfDrawable = this.mView.getCompoundDrawables();
      TextView textView2 = this.mView;
      if (paramDrawable1 == null)
        paramDrawable1 = arrayOfDrawable[0]; 
      if (paramDrawable2 == null)
        paramDrawable2 = arrayOfDrawable[1]; 
      if (textView1 == null)
        drawable = arrayOfDrawable[2]; 
      if (paramDrawable4 == null)
        paramDrawable4 = arrayOfDrawable[3]; 
      textView2.setCompoundDrawablesWithIntrinsicBounds(paramDrawable1, paramDrawable2, drawable, paramDrawable4);
    } 
  }
  
  private void setCompoundTints() {
    TintInfo tintInfo = this.mDrawableTint;
    this.mDrawableLeftTint = tintInfo;
    this.mDrawableTopTint = tintInfo;
    this.mDrawableRightTint = tintInfo;
    this.mDrawableBottomTint = tintInfo;
    this.mDrawableStartTint = tintInfo;
    this.mDrawableEndTint = tintInfo;
  }
  
  private void setTextSizeInternal(int paramInt, float paramFloat) {
    this.mAutoSizeTextHelper.setTextSizeInternal(paramInt, paramFloat);
  }
  
  private void updateTypefaceAndStyle(Context paramContext, TintTypedArray paramTintTypedArray) {
    this.mStyle = paramTintTypedArray.getInt(R.styleable.TextAppearance_android_textStyle, this.mStyle);
    int i = Build.VERSION.SDK_INT;
    boolean bool = false;
    if (i >= 28) {
      i = paramTintTypedArray.getInt(R.styleable.TextAppearance_android_textFontWeight, -1);
      this.mFontWeight = i;
      if (i != -1)
        this.mStyle = this.mStyle & 0x2 | 0x0; 
    } 
    if (paramTintTypedArray.hasValue(R.styleable.TextAppearance_android_fontFamily) || paramTintTypedArray.hasValue(R.styleable.TextAppearance_fontFamily)) {
      this.mFontTypeface = null;
      if (paramTintTypedArray.hasValue(R.styleable.TextAppearance_fontFamily)) {
        i = R.styleable.TextAppearance_fontFamily;
      } else {
        i = R.styleable.TextAppearance_android_fontFamily;
      } 
      final int fontWeight = this.mFontWeight;
      final int style = this.mStyle;
      if (!paramContext.isRestricted()) {
        ResourcesCompat.FontCallback fontCallback = new ResourcesCompat.FontCallback() {
            final AppCompatTextHelper this$0;
            
            final int val$fontWeight;
            
            final int val$style;
            
            final WeakReference val$textViewWeak;
            
            public void onFontRetrievalFailed(int param1Int) {}
            
            public void onFontRetrieved(Typeface param1Typeface) {
              Typeface typeface = param1Typeface;
              if (Build.VERSION.SDK_INT >= 28) {
                int i = fontWeight;
                typeface = param1Typeface;
                if (i != -1) {
                  boolean bool;
                  if ((style & 0x2) != 0) {
                    bool = true;
                  } else {
                    bool = false;
                  } 
                  typeface = Typeface.create(param1Typeface, i, bool);
                } 
              } 
              AppCompatTextHelper.this.onAsyncTypefaceReceived(textViewWeak, typeface);
            }
          };
        try {
          boolean bool1;
          Typeface typeface = paramTintTypedArray.getFont(i, this.mStyle, fontCallback);
          if (typeface != null)
            if (Build.VERSION.SDK_INT >= 28 && this.mFontWeight != -1) {
              typeface = Typeface.create(typeface, 0);
              j = this.mFontWeight;
              if ((this.mStyle & 0x2) != 0) {
                bool1 = true;
              } else {
                bool1 = false;
              } 
              this.mFontTypeface = Typeface.create(typeface, j, bool1);
            } else {
              this.mFontTypeface = typeface;
            }  
          if (this.mFontTypeface == null) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
          this.mAsyncFontPending = bool1;
        } catch (UnsupportedOperationException|android.content.res.Resources.NotFoundException unsupportedOperationException) {}
      } 
      if (this.mFontTypeface == null) {
        String str = paramTintTypedArray.getString(i);
        if (str != null) {
          Typeface typeface;
          if (Build.VERSION.SDK_INT >= 28 && this.mFontWeight != -1) {
            typeface = Typeface.create(str, 0);
            i = this.mFontWeight;
            boolean bool1 = bool;
            if ((this.mStyle & 0x2) != 0)
              bool1 = true; 
            this.mFontTypeface = Typeface.create(typeface, i, bool1);
          } else {
            this.mFontTypeface = Typeface.create((String)typeface, this.mStyle);
          } 
        } 
      } 
      return;
    } 
    if (paramTintTypedArray.hasValue(R.styleable.TextAppearance_android_typeface)) {
      this.mAsyncFontPending = false;
      switch (paramTintTypedArray.getInt(R.styleable.TextAppearance_android_typeface, 1)) {
        default:
          return;
        case 3:
          this.mFontTypeface = Typeface.MONOSPACE;
        case 2:
          this.mFontTypeface = Typeface.SERIF;
        case 1:
          break;
      } 
      this.mFontTypeface = Typeface.SANS_SERIF;
    } 
  }
  
  void applyCompoundDrawablesTints() {
    if (this.mDrawableLeftTint != null || this.mDrawableTopTint != null || this.mDrawableRightTint != null || this.mDrawableBottomTint != null) {
      Drawable[] arrayOfDrawable = this.mView.getCompoundDrawables();
      applyCompoundDrawableTint(arrayOfDrawable[0], this.mDrawableLeftTint);
      applyCompoundDrawableTint(arrayOfDrawable[1], this.mDrawableTopTint);
      applyCompoundDrawableTint(arrayOfDrawable[2], this.mDrawableRightTint);
      applyCompoundDrawableTint(arrayOfDrawable[3], this.mDrawableBottomTint);
    } 
    if (Build.VERSION.SDK_INT >= 17 && (this.mDrawableStartTint != null || this.mDrawableEndTint != null)) {
      Drawable[] arrayOfDrawable = this.mView.getCompoundDrawablesRelative();
      applyCompoundDrawableTint(arrayOfDrawable[0], this.mDrawableStartTint);
      applyCompoundDrawableTint(arrayOfDrawable[2], this.mDrawableEndTint);
    } 
  }
  
  void autoSizeText() {
    this.mAutoSizeTextHelper.autoSizeText();
  }
  
  int getAutoSizeMaxTextSize() {
    return this.mAutoSizeTextHelper.getAutoSizeMaxTextSize();
  }
  
  int getAutoSizeMinTextSize() {
    return this.mAutoSizeTextHelper.getAutoSizeMinTextSize();
  }
  
  int getAutoSizeStepGranularity() {
    return this.mAutoSizeTextHelper.getAutoSizeStepGranularity();
  }
  
  int[] getAutoSizeTextAvailableSizes() {
    return this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
  }
  
  int getAutoSizeTextType() {
    return this.mAutoSizeTextHelper.getAutoSizeTextType();
  }
  
  ColorStateList getCompoundDrawableTintList() {
    TintInfo tintInfo = this.mDrawableTint;
    if (tintInfo != null) {
      ColorStateList colorStateList = tintInfo.mTintList;
    } else {
      tintInfo = null;
    } 
    return (ColorStateList)tintInfo;
  }
  
  PorterDuff.Mode getCompoundDrawableTintMode() {
    TintInfo tintInfo = this.mDrawableTint;
    if (tintInfo != null) {
      PorterDuff.Mode mode = tintInfo.mTintMode;
    } else {
      tintInfo = null;
    } 
    return (PorterDuff.Mode)tintInfo;
  }
  
  boolean isAutoSizeEnabled() {
    return this.mAutoSizeTextHelper.isAutoSizeEnabled();
  }
  
  void loadFromAttributes(AttributeSet paramAttributeSet, int paramInt) {
    Drawable drawable1;
    ColorStateList colorStateList1;
    Drawable drawable2;
    Drawable drawable3;
    Drawable drawable4;
    Drawable drawable5;
    Context context = this.mView.getContext();
    AppCompatDrawableManager appCompatDrawableManager = AppCompatDrawableManager.get();
    TintTypedArray tintTypedArray1 = TintTypedArray.obtainStyledAttributes(context, paramAttributeSet, R.styleable.AppCompatTextHelper, paramInt, 0);
    TextView textView1 = this.mView;
    ViewCompat.saveAttributeDataForStyleable((View)textView1, textView1.getContext(), R.styleable.AppCompatTextHelper, paramAttributeSet, tintTypedArray1.getWrappedTypeArray(), paramInt, 0);
    int k = tintTypedArray1.getResourceId(R.styleable.AppCompatTextHelper_android_textAppearance, -1);
    if (tintTypedArray1.hasValue(R.styleable.AppCompatTextHelper_android_drawableLeft))
      this.mDrawableLeftTint = createTintInfo(context, appCompatDrawableManager, tintTypedArray1.getResourceId(R.styleable.AppCompatTextHelper_android_drawableLeft, 0)); 
    if (tintTypedArray1.hasValue(R.styleable.AppCompatTextHelper_android_drawableTop))
      this.mDrawableTopTint = createTintInfo(context, appCompatDrawableManager, tintTypedArray1.getResourceId(R.styleable.AppCompatTextHelper_android_drawableTop, 0)); 
    if (tintTypedArray1.hasValue(R.styleable.AppCompatTextHelper_android_drawableRight))
      this.mDrawableRightTint = createTintInfo(context, appCompatDrawableManager, tintTypedArray1.getResourceId(R.styleable.AppCompatTextHelper_android_drawableRight, 0)); 
    if (tintTypedArray1.hasValue(R.styleable.AppCompatTextHelper_android_drawableBottom))
      this.mDrawableBottomTint = createTintInfo(context, appCompatDrawableManager, tintTypedArray1.getResourceId(R.styleable.AppCompatTextHelper_android_drawableBottom, 0)); 
    if (Build.VERSION.SDK_INT >= 17) {
      if (tintTypedArray1.hasValue(R.styleable.AppCompatTextHelper_android_drawableStart))
        this.mDrawableStartTint = createTintInfo(context, appCompatDrawableManager, tintTypedArray1.getResourceId(R.styleable.AppCompatTextHelper_android_drawableStart, 0)); 
      if (tintTypedArray1.hasValue(R.styleable.AppCompatTextHelper_android_drawableEnd))
        this.mDrawableEndTint = createTintInfo(context, appCompatDrawableManager, tintTypedArray1.getResourceId(R.styleable.AppCompatTextHelper_android_drawableEnd, 0)); 
    } 
    tintTypedArray1.recycle();
    boolean bool3 = this.mView.getTransformationMethod() instanceof android.text.method.PasswordTransformationMethod;
    boolean bool1 = false;
    boolean bool2 = false;
    int i = 0;
    int j = 0;
    ColorStateList colorStateList10 = null;
    ColorStateList colorStateList3 = null;
    ColorStateList colorStateList9 = null;
    TextView textView2 = null;
    textView1 = null;
    ColorStateList colorStateList8 = null;
    tintTypedArray1 = null;
    TintTypedArray tintTypedArray3 = null;
    ColorStateList colorStateList4 = null;
    ColorStateList colorStateList6 = null;
    ColorStateList colorStateList5 = null;
    ColorStateList colorStateList7 = null;
    if (k != -1) {
      TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(context, k, R.styleable.TextAppearance);
      bool1 = bool2;
      i = j;
      if (!bool3) {
        bool1 = bool2;
        i = j;
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_textAllCaps)) {
          i = 1;
          bool1 = tintTypedArray.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
        } 
      } 
      updateTypefaceAndStyle(context, tintTypedArray);
      colorStateList3 = colorStateList10;
      textView1 = textView2;
      tintTypedArray1 = tintTypedArray3;
      if (Build.VERSION.SDK_INT < 23) {
        colorStateList4 = colorStateList9;
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColor))
          colorStateList4 = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColor); 
        colorStateList5 = colorStateList8;
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColorHint))
          colorStateList5 = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColorHint); 
        colorStateList3 = colorStateList4;
        colorStateList2 = colorStateList5;
        tintTypedArray1 = tintTypedArray3;
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColorLink)) {
          colorStateList1 = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColorLink);
          colorStateList2 = colorStateList5;
          colorStateList3 = colorStateList4;
        } 
      } 
      colorStateList5 = colorStateList7;
      if (tintTypedArray.hasValue(R.styleable.TextAppearance_textLocale))
        str2 = tintTypedArray.getString(R.styleable.TextAppearance_textLocale); 
      colorStateList4 = colorStateList6;
      if (Build.VERSION.SDK_INT >= 26) {
        colorStateList4 = colorStateList6;
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_fontVariationSettings))
          str1 = tintTypedArray.getString(R.styleable.TextAppearance_fontVariationSettings); 
      } 
      tintTypedArray.recycle();
    } 
    TintTypedArray tintTypedArray2 = TintTypedArray.obtainStyledAttributes(context, paramAttributeSet, R.styleable.TextAppearance, paramInt, 0);
    if (!bool3 && tintTypedArray2.hasValue(R.styleable.TextAppearance_textAllCaps)) {
      bool1 = tintTypedArray2.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
      i = 1;
    } 
    if (Build.VERSION.SDK_INT < 23) {
      if (tintTypedArray2.hasValue(R.styleable.TextAppearance_android_textColor))
        colorStateList3 = tintTypedArray2.getColorStateList(R.styleable.TextAppearance_android_textColor); 
      if (tintTypedArray2.hasValue(R.styleable.TextAppearance_android_textColorHint))
        colorStateList2 = tintTypedArray2.getColorStateList(R.styleable.TextAppearance_android_textColorHint); 
      if (tintTypedArray2.hasValue(R.styleable.TextAppearance_android_textColorLink))
        colorStateList1 = tintTypedArray2.getColorStateList(R.styleable.TextAppearance_android_textColorLink); 
    } 
    if (tintTypedArray2.hasValue(R.styleable.TextAppearance_textLocale))
      str2 = tintTypedArray2.getString(R.styleable.TextAppearance_textLocale); 
    if (Build.VERSION.SDK_INT >= 26 && tintTypedArray2.hasValue(R.styleable.TextAppearance_fontVariationSettings))
      str1 = tintTypedArray2.getString(R.styleable.TextAppearance_fontVariationSettings); 
    if (Build.VERSION.SDK_INT >= 28 && tintTypedArray2.hasValue(R.styleable.TextAppearance_android_textSize) && tintTypedArray2.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, -1) == 0)
      this.mView.setTextSize(0, 0.0F); 
    updateTypefaceAndStyle(context, tintTypedArray2);
    tintTypedArray2.recycle();
    if (colorStateList3 != null)
      this.mView.setTextColor(colorStateList3); 
    if (colorStateList2 != null)
      this.mView.setHintTextColor(colorStateList2); 
    if (colorStateList1 != null)
      this.mView.setLinkTextColor(colorStateList1); 
    if (!bool3 && i)
      setAllCaps(bool1); 
    Typeface typeface = this.mFontTypeface;
    if (typeface != null)
      if (this.mFontWeight == -1) {
        this.mView.setTypeface(typeface, this.mStyle);
      } else {
        this.mView.setTypeface(typeface);
      }  
    if (str1 != null)
      this.mView.setFontVariationSettings(str1); 
    if (str2 != null)
      if (Build.VERSION.SDK_INT >= 24) {
        this.mView.setTextLocales(LocaleList.forLanguageTags(str2));
      } else if (Build.VERSION.SDK_INT >= 21) {
        String str = str2.substring(0, str2.indexOf(','));
        this.mView.setTextLocale(Locale.forLanguageTag(str));
      }  
    this.mAutoSizeTextHelper.loadFromAttributes(paramAttributeSet, paramInt);
    if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE && this.mAutoSizeTextHelper.getAutoSizeTextType() != 0) {
      int[] arrayOfInt = this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
      if (arrayOfInt.length > 0)
        if (this.mView.getAutoSizeStepGranularity() != -1.0F) {
          this.mView.setAutoSizeTextTypeUniformWithConfiguration(this.mAutoSizeTextHelper.getAutoSizeMinTextSize(), this.mAutoSizeTextHelper.getAutoSizeMaxTextSize(), this.mAutoSizeTextHelper.getAutoSizeStepGranularity(), 0);
        } else {
          this.mView.setAutoSizeTextTypeUniformWithPresetSizes(arrayOfInt, 0);
        }  
    } 
    tintTypedArray2 = TintTypedArray.obtainStyledAttributes(context, paramAttributeSet, R.styleable.AppCompatTextView);
    String str1 = null;
    String str2 = null;
    paramAttributeSet = null;
    paramInt = R.styleable.AppCompatTextView_drawableLeftCompat;
    typeface = null;
    paramInt = tintTypedArray2.getResourceId(paramInt, -1);
    if (paramInt != -1)
      drawable1 = appCompatDrawableManager.getDrawable(context, paramInt); 
    paramInt = R.styleable.AppCompatTextView_drawableTopCompat;
    ColorStateList colorStateList2 = null;
    paramInt = tintTypedArray2.getResourceId(paramInt, -1);
    if (paramInt != -1)
      drawable2 = appCompatDrawableManager.getDrawable(context, paramInt); 
    paramInt = tintTypedArray2.getResourceId(R.styleable.AppCompatTextView_drawableRightCompat, -1);
    if (paramInt != -1)
      drawable3 = appCompatDrawableManager.getDrawable(context, paramInt); 
    paramInt = tintTypedArray2.getResourceId(R.styleable.AppCompatTextView_drawableBottomCompat, -1);
    if (paramInt != -1) {
      Drawable drawable = appCompatDrawableManager.getDrawable(context, paramInt);
    } else {
      colorStateList3 = null;
    } 
    paramInt = tintTypedArray2.getResourceId(R.styleable.AppCompatTextView_drawableStartCompat, -1);
    if (paramInt != -1)
      drawable4 = appCompatDrawableManager.getDrawable(context, paramInt); 
    paramInt = tintTypedArray2.getResourceId(R.styleable.AppCompatTextView_drawableEndCompat, -1);
    if (paramInt != -1)
      drawable5 = appCompatDrawableManager.getDrawable(context, paramInt); 
    setCompoundDrawables(drawable1, drawable2, drawable3, (Drawable)colorStateList3, drawable4, drawable5);
    if (tintTypedArray2.hasValue(R.styleable.AppCompatTextView_drawableTint)) {
      ColorStateList colorStateList = tintTypedArray2.getColorStateList(R.styleable.AppCompatTextView_drawableTint);
      TextViewCompat.setCompoundDrawableTintList(this.mView, colorStateList);
    } 
    if (tintTypedArray2.hasValue(R.styleable.AppCompatTextView_drawableTintMode)) {
      PorterDuff.Mode mode = DrawableUtils.parseTintMode(tintTypedArray2.getInt(R.styleable.AppCompatTextView_drawableTintMode, -1), null);
      TextViewCompat.setCompoundDrawableTintMode(this.mView, mode);
    } 
    i = tintTypedArray2.getDimensionPixelSize(R.styleable.AppCompatTextView_firstBaselineToTopHeight, -1);
    j = tintTypedArray2.getDimensionPixelSize(R.styleable.AppCompatTextView_lastBaselineToBottomHeight, -1);
    paramInt = tintTypedArray2.getDimensionPixelSize(R.styleable.AppCompatTextView_lineHeight, -1);
    tintTypedArray2.recycle();
    if (i != -1)
      TextViewCompat.setFirstBaselineToTopHeight(this.mView, i); 
    if (j != -1)
      TextViewCompat.setLastBaselineToBottomHeight(this.mView, j); 
    if (paramInt != -1)
      TextViewCompat.setLineHeight(this.mView, paramInt); 
  }
  
  void onAsyncTypefaceReceived(WeakReference<TextView> paramWeakReference, final Typeface typeface) {
    if (this.mAsyncFontPending) {
      this.mFontTypeface = typeface;
      final TextView textView = paramWeakReference.get();
      if (textView != null)
        if (ViewCompat.isAttachedToWindow((View)textView)) {
          textView.post(new Runnable() {
                final AppCompatTextHelper this$0;
                
                final int val$style;
                
                final TextView val$textView;
                
                final Typeface val$typeface;
                
                public void run() {
                  textView.setTypeface(typeface, style);
                }
              });
        } else {
          textView.setTypeface(typeface, this.mStyle);
        }  
    } 
  }
  
  void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (!AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE)
      autoSizeText(); 
  }
  
  void onSetCompoundDrawables() {
    applyCompoundDrawablesTints();
  }
  
  void onSetTextAppearance(Context paramContext, int paramInt) {
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramInt, R.styleable.TextAppearance);
    if (tintTypedArray.hasValue(R.styleable.TextAppearance_textAllCaps))
      setAllCaps(tintTypedArray.getBoolean(R.styleable.TextAppearance_textAllCaps, false)); 
    if (Build.VERSION.SDK_INT < 23) {
      if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColor)) {
        ColorStateList colorStateList = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColor);
        if (colorStateList != null)
          this.mView.setTextColor(colorStateList); 
      } 
      if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColorLink)) {
        ColorStateList colorStateList = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColorLink);
        if (colorStateList != null)
          this.mView.setLinkTextColor(colorStateList); 
      } 
      if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
        ColorStateList colorStateList = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
        if (colorStateList != null)
          this.mView.setHintTextColor(colorStateList); 
      } 
    } 
    if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textSize) && tintTypedArray.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, -1) == 0)
      this.mView.setTextSize(0, 0.0F); 
    updateTypefaceAndStyle(paramContext, tintTypedArray);
    if (Build.VERSION.SDK_INT >= 26 && tintTypedArray.hasValue(R.styleable.TextAppearance_fontVariationSettings)) {
      String str = tintTypedArray.getString(R.styleable.TextAppearance_fontVariationSettings);
      if (str != null)
        this.mView.setFontVariationSettings(str); 
    } 
    tintTypedArray.recycle();
    Typeface typeface = this.mFontTypeface;
    if (typeface != null)
      this.mView.setTypeface(typeface, this.mStyle); 
  }
  
  void populateSurroundingTextIfNeeded(TextView paramTextView, InputConnection paramInputConnection, EditorInfo paramEditorInfo) {
    if (Build.VERSION.SDK_INT < 30 && paramInputConnection != null)
      EditorInfoCompat.setInitialSurroundingText(paramEditorInfo, paramTextView.getText()); 
  }
  
  void setAllCaps(boolean paramBoolean) {
    this.mView.setAllCaps(paramBoolean);
  }
  
  void setAutoSizeTextTypeUniformWithConfiguration(int paramInt1, int paramInt2, int paramInt3, int paramInt4) throws IllegalArgumentException {
    this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithConfiguration(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  void setAutoSizeTextTypeUniformWithPresetSizes(int[] paramArrayOfint, int paramInt) throws IllegalArgumentException {
    this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(paramArrayOfint, paramInt);
  }
  
  void setAutoSizeTextTypeWithDefaults(int paramInt) {
    this.mAutoSizeTextHelper.setAutoSizeTextTypeWithDefaults(paramInt);
  }
  
  void setCompoundDrawableTintList(ColorStateList paramColorStateList) {
    boolean bool;
    if (this.mDrawableTint == null)
      this.mDrawableTint = new TintInfo(); 
    this.mDrawableTint.mTintList = paramColorStateList;
    TintInfo tintInfo = this.mDrawableTint;
    if (paramColorStateList != null) {
      bool = true;
    } else {
      bool = false;
    } 
    tintInfo.mHasTintList = bool;
    setCompoundTints();
  }
  
  void setCompoundDrawableTintMode(PorterDuff.Mode paramMode) {
    boolean bool;
    if (this.mDrawableTint == null)
      this.mDrawableTint = new TintInfo(); 
    this.mDrawableTint.mTintMode = paramMode;
    TintInfo tintInfo = this.mDrawableTint;
    if (paramMode != null) {
      bool = true;
    } else {
      bool = false;
    } 
    tintInfo.mHasTintMode = bool;
    setCompoundTints();
  }
  
  void setTextSize(int paramInt, float paramFloat) {
    if (!AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE && !isAutoSizeEnabled())
      setTextSizeInternal(paramInt, paramFloat); 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\AppCompatTextHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */