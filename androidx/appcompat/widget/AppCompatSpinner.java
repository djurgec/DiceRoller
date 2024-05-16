package androidx.appcompat.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.ThemedSpinnerAdapter;
import androidx.appcompat.R;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.core.view.TintableBackgroundView;
import androidx.core.view.ViewCompat;

public class AppCompatSpinner extends Spinner implements TintableBackgroundView {
  private static final int[] ATTRS_ANDROID_SPINNERMODE = new int[] { 16843505 };
  
  private static final int MAX_ITEMS_MEASURED = 15;
  
  private static final int MODE_DIALOG = 0;
  
  private static final int MODE_DROPDOWN = 1;
  
  private static final int MODE_THEME = -1;
  
  private static final String TAG = "AppCompatSpinner";
  
  private final AppCompatBackgroundHelper mBackgroundTintHelper;
  
  int mDropDownWidth;
  
  private ForwardingListener mForwardingListener;
  
  private SpinnerPopup mPopup;
  
  private final Context mPopupContext;
  
  private final boolean mPopupSet;
  
  private SpinnerAdapter mTempAdapter;
  
  final Rect mTempRect;
  
  public AppCompatSpinner(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public AppCompatSpinner(Context paramContext, int paramInt) {
    this(paramContext, (AttributeSet)null, R.attr.spinnerStyle, paramInt);
  }
  
  public AppCompatSpinner(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.spinnerStyle);
  }
  
  public AppCompatSpinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    this(paramContext, paramAttributeSet, paramInt, -1);
  }
  
  public AppCompatSpinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    this(paramContext, paramAttributeSet, paramInt1, paramInt2, (Resources.Theme)null);
  }
  
  public AppCompatSpinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2, Resources.Theme paramTheme) {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: aload_2
    //   3: iload_3
    //   4: invokespecial <init> : (Landroid/content/Context;Landroid/util/AttributeSet;I)V
    //   7: aload_0
    //   8: new android/graphics/Rect
    //   11: dup
    //   12: invokespecial <init> : ()V
    //   15: putfield mTempRect : Landroid/graphics/Rect;
    //   18: aload_0
    //   19: aload_0
    //   20: invokevirtual getContext : ()Landroid/content/Context;
    //   23: invokestatic checkAppCompatTheme : (Landroid/view/View;Landroid/content/Context;)V
    //   26: aload_1
    //   27: aload_2
    //   28: getstatic androidx/appcompat/R$styleable.Spinner : [I
    //   31: iload_3
    //   32: iconst_0
    //   33: invokestatic obtainStyledAttributes : (Landroid/content/Context;Landroid/util/AttributeSet;[III)Landroidx/appcompat/widget/TintTypedArray;
    //   36: astore #10
    //   38: aload_0
    //   39: new androidx/appcompat/widget/AppCompatBackgroundHelper
    //   42: dup
    //   43: aload_0
    //   44: invokespecial <init> : (Landroid/view/View;)V
    //   47: putfield mBackgroundTintHelper : Landroidx/appcompat/widget/AppCompatBackgroundHelper;
    //   50: aload #5
    //   52: ifnull -> 72
    //   55: aload_0
    //   56: new androidx/appcompat/view/ContextThemeWrapper
    //   59: dup
    //   60: aload_1
    //   61: aload #5
    //   63: invokespecial <init> : (Landroid/content/Context;Landroid/content/res/Resources$Theme;)V
    //   66: putfield mPopupContext : Landroid/content/Context;
    //   69: goto -> 110
    //   72: aload #10
    //   74: getstatic androidx/appcompat/R$styleable.Spinner_popupTheme : I
    //   77: iconst_0
    //   78: invokevirtual getResourceId : (II)I
    //   81: istore #6
    //   83: iload #6
    //   85: ifeq -> 105
    //   88: aload_0
    //   89: new androidx/appcompat/view/ContextThemeWrapper
    //   92: dup
    //   93: aload_1
    //   94: iload #6
    //   96: invokespecial <init> : (Landroid/content/Context;I)V
    //   99: putfield mPopupContext : Landroid/content/Context;
    //   102: goto -> 110
    //   105: aload_0
    //   106: aload_1
    //   107: putfield mPopupContext : Landroid/content/Context;
    //   110: iload #4
    //   112: istore #7
    //   114: iload #4
    //   116: iconst_m1
    //   117: if_icmpne -> 249
    //   120: aconst_null
    //   121: astore #5
    //   123: aconst_null
    //   124: astore #8
    //   126: aload_1
    //   127: aload_2
    //   128: getstatic androidx/appcompat/widget/AppCompatSpinner.ATTRS_ANDROID_SPINNERMODE : [I
    //   131: iload_3
    //   132: iconst_0
    //   133: invokevirtual obtainStyledAttributes : (Landroid/util/AttributeSet;[III)Landroid/content/res/TypedArray;
    //   136: astore #9
    //   138: iload #4
    //   140: istore #6
    //   142: aload #9
    //   144: astore #8
    //   146: aload #9
    //   148: astore #5
    //   150: aload #9
    //   152: iconst_0
    //   153: invokevirtual hasValue : (I)Z
    //   156: ifeq -> 176
    //   159: aload #9
    //   161: astore #8
    //   163: aload #9
    //   165: astore #5
    //   167: aload #9
    //   169: iconst_0
    //   170: iconst_0
    //   171: invokevirtual getInt : (II)I
    //   174: istore #6
    //   176: iload #6
    //   178: istore #7
    //   180: aload #9
    //   182: ifnull -> 249
    //   185: iload #6
    //   187: istore #4
    //   189: aload #9
    //   191: astore #5
    //   193: aload #5
    //   195: invokevirtual recycle : ()V
    //   198: iload #4
    //   200: istore #7
    //   202: goto -> 249
    //   205: astore_1
    //   206: goto -> 237
    //   209: astore #9
    //   211: aload #5
    //   213: astore #8
    //   215: ldc 'AppCompatSpinner'
    //   217: ldc 'Could not read android:spinnerMode'
    //   219: aload #9
    //   221: invokestatic i : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   224: pop
    //   225: iload #4
    //   227: istore #7
    //   229: aload #5
    //   231: ifnull -> 249
    //   234: goto -> 193
    //   237: aload #8
    //   239: ifnull -> 247
    //   242: aload #8
    //   244: invokevirtual recycle : ()V
    //   247: aload_1
    //   248: athrow
    //   249: iload #7
    //   251: tableswitch default -> 272, 0 -> 375, 1 -> 275
    //   272: goto -> 406
    //   275: new androidx/appcompat/widget/AppCompatSpinner$DropdownPopup
    //   278: dup
    //   279: aload_0
    //   280: aload_0
    //   281: getfield mPopupContext : Landroid/content/Context;
    //   284: aload_2
    //   285: iload_3
    //   286: invokespecial <init> : (Landroidx/appcompat/widget/AppCompatSpinner;Landroid/content/Context;Landroid/util/AttributeSet;I)V
    //   289: astore #8
    //   291: aload_0
    //   292: getfield mPopupContext : Landroid/content/Context;
    //   295: aload_2
    //   296: getstatic androidx/appcompat/R$styleable.Spinner : [I
    //   299: iload_3
    //   300: iconst_0
    //   301: invokestatic obtainStyledAttributes : (Landroid/content/Context;Landroid/util/AttributeSet;[III)Landroidx/appcompat/widget/TintTypedArray;
    //   304: astore #5
    //   306: aload_0
    //   307: aload #5
    //   309: getstatic androidx/appcompat/R$styleable.Spinner_android_dropDownWidth : I
    //   312: bipush #-2
    //   314: invokevirtual getLayoutDimension : (II)I
    //   317: putfield mDropDownWidth : I
    //   320: aload #8
    //   322: aload #5
    //   324: getstatic androidx/appcompat/R$styleable.Spinner_android_popupBackground : I
    //   327: invokevirtual getDrawable : (I)Landroid/graphics/drawable/Drawable;
    //   330: invokevirtual setBackgroundDrawable : (Landroid/graphics/drawable/Drawable;)V
    //   333: aload #8
    //   335: aload #10
    //   337: getstatic androidx/appcompat/R$styleable.Spinner_android_prompt : I
    //   340: invokevirtual getString : (I)Ljava/lang/String;
    //   343: invokevirtual setPromptText : (Ljava/lang/CharSequence;)V
    //   346: aload #5
    //   348: invokevirtual recycle : ()V
    //   351: aload_0
    //   352: aload #8
    //   354: putfield mPopup : Landroidx/appcompat/widget/AppCompatSpinner$SpinnerPopup;
    //   357: aload_0
    //   358: new androidx/appcompat/widget/AppCompatSpinner$1
    //   361: dup
    //   362: aload_0
    //   363: aload_0
    //   364: aload #8
    //   366: invokespecial <init> : (Landroidx/appcompat/widget/AppCompatSpinner;Landroid/view/View;Landroidx/appcompat/widget/AppCompatSpinner$DropdownPopup;)V
    //   369: putfield mForwardingListener : Landroidx/appcompat/widget/ForwardingListener;
    //   372: goto -> 406
    //   375: new androidx/appcompat/widget/AppCompatSpinner$DialogPopup
    //   378: dup
    //   379: aload_0
    //   380: invokespecial <init> : (Landroidx/appcompat/widget/AppCompatSpinner;)V
    //   383: astore #5
    //   385: aload_0
    //   386: aload #5
    //   388: putfield mPopup : Landroidx/appcompat/widget/AppCompatSpinner$SpinnerPopup;
    //   391: aload #5
    //   393: aload #10
    //   395: getstatic androidx/appcompat/R$styleable.Spinner_android_prompt : I
    //   398: invokevirtual getString : (I)Ljava/lang/String;
    //   401: invokeinterface setPromptText : (Ljava/lang/CharSequence;)V
    //   406: aload #10
    //   408: getstatic androidx/appcompat/R$styleable.Spinner_android_entries : I
    //   411: invokevirtual getTextArray : (I)[Ljava/lang/CharSequence;
    //   414: astore #5
    //   416: aload #5
    //   418: ifnull -> 446
    //   421: new android/widget/ArrayAdapter
    //   424: dup
    //   425: aload_1
    //   426: ldc 17367048
    //   428: aload #5
    //   430: invokespecial <init> : (Landroid/content/Context;I[Ljava/lang/Object;)V
    //   433: astore_1
    //   434: aload_1
    //   435: getstatic androidx/appcompat/R$layout.support_simple_spinner_dropdown_item : I
    //   438: invokevirtual setDropDownViewResource : (I)V
    //   441: aload_0
    //   442: aload_1
    //   443: invokevirtual setAdapter : (Landroid/widget/SpinnerAdapter;)V
    //   446: aload #10
    //   448: invokevirtual recycle : ()V
    //   451: aload_0
    //   452: iconst_1
    //   453: putfield mPopupSet : Z
    //   456: aload_0
    //   457: getfield mTempAdapter : Landroid/widget/SpinnerAdapter;
    //   460: astore_1
    //   461: aload_1
    //   462: ifnull -> 475
    //   465: aload_0
    //   466: aload_1
    //   467: invokevirtual setAdapter : (Landroid/widget/SpinnerAdapter;)V
    //   470: aload_0
    //   471: aconst_null
    //   472: putfield mTempAdapter : Landroid/widget/SpinnerAdapter;
    //   475: aload_0
    //   476: getfield mBackgroundTintHelper : Landroidx/appcompat/widget/AppCompatBackgroundHelper;
    //   479: aload_2
    //   480: iload_3
    //   481: invokevirtual loadFromAttributes : (Landroid/util/AttributeSet;I)V
    //   484: return
    // Exception table:
    //   from	to	target	type
    //   126	138	209	java/lang/Exception
    //   126	138	205	finally
    //   150	159	209	java/lang/Exception
    //   150	159	205	finally
    //   167	176	209	java/lang/Exception
    //   167	176	205	finally
    //   215	225	205	finally
  }
  
  int compatMeasureContentWidth(SpinnerAdapter paramSpinnerAdapter, Drawable paramDrawable) {
    if (paramSpinnerAdapter == null)
      return 0; 
    int i = 0;
    View view = null;
    int k = 0;
    int i1 = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 0);
    int n = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 0);
    int j = Math.max(0, getSelectedItemPosition());
    int m = Math.min(paramSpinnerAdapter.getCount(), j + 15);
    j = Math.max(0, j - 15 - m - j);
    while (j < m) {
      int i3 = paramSpinnerAdapter.getItemViewType(j);
      int i2 = k;
      if (i3 != k) {
        i2 = i3;
        view = null;
      } 
      view = paramSpinnerAdapter.getView(j, view, (ViewGroup)this);
      if (view.getLayoutParams() == null)
        view.setLayoutParams(new ViewGroup.LayoutParams(-2, -2)); 
      view.measure(i1, n);
      i = Math.max(i, view.getMeasuredWidth());
      j++;
      k = i2;
    } 
    j = i;
    if (paramDrawable != null) {
      paramDrawable.getPadding(this.mTempRect);
      j = i + this.mTempRect.left + this.mTempRect.right;
    } 
    return j;
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null)
      appCompatBackgroundHelper.applySupportBackgroundTint(); 
  }
  
  public int getDropDownHorizontalOffset() {
    SpinnerPopup spinnerPopup = this.mPopup;
    return (spinnerPopup != null) ? spinnerPopup.getHorizontalOffset() : ((Build.VERSION.SDK_INT >= 16) ? super.getDropDownHorizontalOffset() : 0);
  }
  
  public int getDropDownVerticalOffset() {
    SpinnerPopup spinnerPopup = this.mPopup;
    return (spinnerPopup != null) ? spinnerPopup.getVerticalOffset() : ((Build.VERSION.SDK_INT >= 16) ? super.getDropDownVerticalOffset() : 0);
  }
  
  public int getDropDownWidth() {
    return (this.mPopup != null) ? this.mDropDownWidth : ((Build.VERSION.SDK_INT >= 16) ? super.getDropDownWidth() : 0);
  }
  
  final SpinnerPopup getInternalPopup() {
    return this.mPopup;
  }
  
  public Drawable getPopupBackground() {
    SpinnerPopup spinnerPopup = this.mPopup;
    return (spinnerPopup != null) ? spinnerPopup.getBackground() : ((Build.VERSION.SDK_INT >= 16) ? super.getPopupBackground() : null);
  }
  
  public Context getPopupContext() {
    return this.mPopupContext;
  }
  
  public CharSequence getPrompt() {
    CharSequence charSequence;
    SpinnerPopup spinnerPopup = this.mPopup;
    if (spinnerPopup != null) {
      charSequence = spinnerPopup.getHintText();
    } else {
      charSequence = super.getPrompt();
    } 
    return charSequence;
  }
  
  public ColorStateList getSupportBackgroundTintList() {
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null) {
      ColorStateList colorStateList = appCompatBackgroundHelper.getSupportBackgroundTintList();
    } else {
      appCompatBackgroundHelper = null;
    } 
    return (ColorStateList)appCompatBackgroundHelper;
  }
  
  public PorterDuff.Mode getSupportBackgroundTintMode() {
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null) {
      PorterDuff.Mode mode = appCompatBackgroundHelper.getSupportBackgroundTintMode();
    } else {
      appCompatBackgroundHelper = null;
    } 
    return (PorterDuff.Mode)appCompatBackgroundHelper;
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    SpinnerPopup spinnerPopup = this.mPopup;
    if (spinnerPopup != null && spinnerPopup.isShowing())
      this.mPopup.dismiss(); 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    if (this.mPopup != null && View.MeasureSpec.getMode(paramInt1) == Integer.MIN_VALUE) {
      paramInt2 = getMeasuredWidth();
      setMeasuredDimension(Math.min(Math.max(paramInt2, compatMeasureContentWidth(getAdapter(), getBackground())), View.MeasureSpec.getSize(paramInt1)), getMeasuredHeight());
    } 
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    if (savedState.mShowDropdown) {
      ViewTreeObserver viewTreeObserver = getViewTreeObserver();
      if (viewTreeObserver != null)
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
              final AppCompatSpinner this$0;
              
              public void onGlobalLayout() {
                if (!AppCompatSpinner.this.getInternalPopup().isShowing())
                  AppCompatSpinner.this.showPopup(); 
                ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
                if (viewTreeObserver != null)
                  if (Build.VERSION.SDK_INT >= 16) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this);
                  } else {
                    viewTreeObserver.removeGlobalOnLayoutListener(this);
                  }  
              }
            }); 
    } 
  }
  
  public Parcelable onSaveInstanceState() {
    boolean bool;
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    SpinnerPopup spinnerPopup = this.mPopup;
    if (spinnerPopup != null && spinnerPopup.isShowing()) {
      bool = true;
    } else {
      bool = false;
    } 
    savedState.mShowDropdown = bool;
    return (Parcelable)savedState;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    ForwardingListener forwardingListener = this.mForwardingListener;
    return (forwardingListener != null && forwardingListener.onTouch((View)this, paramMotionEvent)) ? true : super.onTouchEvent(paramMotionEvent);
  }
  
  public boolean performClick() {
    SpinnerPopup spinnerPopup = this.mPopup;
    if (spinnerPopup != null) {
      if (!spinnerPopup.isShowing())
        showPopup(); 
      return true;
    } 
    return super.performClick();
  }
  
  public void setAdapter(SpinnerAdapter paramSpinnerAdapter) {
    if (!this.mPopupSet) {
      this.mTempAdapter = paramSpinnerAdapter;
      return;
    } 
    super.setAdapter(paramSpinnerAdapter);
    if (this.mPopup != null) {
      Context context2 = this.mPopupContext;
      Context context1 = context2;
      if (context2 == null)
        context1 = getContext(); 
      this.mPopup.setAdapter(new DropDownAdapter(paramSpinnerAdapter, context1.getTheme()));
    } 
  }
  
  public void setBackgroundDrawable(Drawable paramDrawable) {
    super.setBackgroundDrawable(paramDrawable);
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null)
      appCompatBackgroundHelper.onSetBackgroundDrawable(paramDrawable); 
  }
  
  public void setBackgroundResource(int paramInt) {
    super.setBackgroundResource(paramInt);
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null)
      appCompatBackgroundHelper.onSetBackgroundResource(paramInt); 
  }
  
  public void setDropDownHorizontalOffset(int paramInt) {
    SpinnerPopup spinnerPopup = this.mPopup;
    if (spinnerPopup != null) {
      spinnerPopup.setHorizontalOriginalOffset(paramInt);
      this.mPopup.setHorizontalOffset(paramInt);
    } else if (Build.VERSION.SDK_INT >= 16) {
      super.setDropDownHorizontalOffset(paramInt);
    } 
  }
  
  public void setDropDownVerticalOffset(int paramInt) {
    SpinnerPopup spinnerPopup = this.mPopup;
    if (spinnerPopup != null) {
      spinnerPopup.setVerticalOffset(paramInt);
    } else if (Build.VERSION.SDK_INT >= 16) {
      super.setDropDownVerticalOffset(paramInt);
    } 
  }
  
  public void setDropDownWidth(int paramInt) {
    if (this.mPopup != null) {
      this.mDropDownWidth = paramInt;
    } else if (Build.VERSION.SDK_INT >= 16) {
      super.setDropDownWidth(paramInt);
    } 
  }
  
  public void setPopupBackgroundDrawable(Drawable paramDrawable) {
    SpinnerPopup spinnerPopup = this.mPopup;
    if (spinnerPopup != null) {
      spinnerPopup.setBackgroundDrawable(paramDrawable);
    } else if (Build.VERSION.SDK_INT >= 16) {
      super.setPopupBackgroundDrawable(paramDrawable);
    } 
  }
  
  public void setPopupBackgroundResource(int paramInt) {
    setPopupBackgroundDrawable(AppCompatResources.getDrawable(getPopupContext(), paramInt));
  }
  
  public void setPrompt(CharSequence paramCharSequence) {
    SpinnerPopup spinnerPopup = this.mPopup;
    if (spinnerPopup != null) {
      spinnerPopup.setPromptText(paramCharSequence);
    } else {
      super.setPrompt(paramCharSequence);
    } 
  }
  
  public void setSupportBackgroundTintList(ColorStateList paramColorStateList) {
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null)
      appCompatBackgroundHelper.setSupportBackgroundTintList(paramColorStateList); 
  }
  
  public void setSupportBackgroundTintMode(PorterDuff.Mode paramMode) {
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null)
      appCompatBackgroundHelper.setSupportBackgroundTintMode(paramMode); 
  }
  
  void showPopup() {
    if (Build.VERSION.SDK_INT >= 17) {
      this.mPopup.show(getTextDirection(), getTextAlignment());
    } else {
      this.mPopup.show(-1, -1);
    } 
  }
  
  class DialogPopup implements SpinnerPopup, DialogInterface.OnClickListener {
    private ListAdapter mListAdapter;
    
    AlertDialog mPopup;
    
    private CharSequence mPrompt;
    
    final AppCompatSpinner this$0;
    
    public void dismiss() {
      AlertDialog alertDialog = this.mPopup;
      if (alertDialog != null) {
        alertDialog.dismiss();
        this.mPopup = null;
      } 
    }
    
    public Drawable getBackground() {
      return null;
    }
    
    public CharSequence getHintText() {
      return this.mPrompt;
    }
    
    public int getHorizontalOffset() {
      return 0;
    }
    
    public int getHorizontalOriginalOffset() {
      return 0;
    }
    
    public int getVerticalOffset() {
      return 0;
    }
    
    public boolean isShowing() {
      boolean bool;
      AlertDialog alertDialog = this.mPopup;
      if (alertDialog != null) {
        bool = alertDialog.isShowing();
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onClick(DialogInterface param1DialogInterface, int param1Int) {
      AppCompatSpinner.this.setSelection(param1Int);
      if (AppCompatSpinner.this.getOnItemClickListener() != null)
        AppCompatSpinner.this.performItemClick(null, param1Int, this.mListAdapter.getItemId(param1Int)); 
      dismiss();
    }
    
    public void setAdapter(ListAdapter param1ListAdapter) {
      this.mListAdapter = param1ListAdapter;
    }
    
    public void setBackgroundDrawable(Drawable param1Drawable) {
      Log.e("AppCompatSpinner", "Cannot set popup background for MODE_DIALOG, ignoring");
    }
    
    public void setHorizontalOffset(int param1Int) {
      Log.e("AppCompatSpinner", "Cannot set horizontal offset for MODE_DIALOG, ignoring");
    }
    
    public void setHorizontalOriginalOffset(int param1Int) {
      Log.e("AppCompatSpinner", "Cannot set horizontal (original) offset for MODE_DIALOG, ignoring");
    }
    
    public void setPromptText(CharSequence param1CharSequence) {
      this.mPrompt = param1CharSequence;
    }
    
    public void setVerticalOffset(int param1Int) {
      Log.e("AppCompatSpinner", "Cannot set vertical offset for MODE_DIALOG, ignoring");
    }
    
    public void show(int param1Int1, int param1Int2) {
      if (this.mListAdapter == null)
        return; 
      AlertDialog.Builder builder = new AlertDialog.Builder(AppCompatSpinner.this.getPopupContext());
      CharSequence charSequence = this.mPrompt;
      if (charSequence != null)
        builder.setTitle(charSequence); 
      AlertDialog alertDialog = builder.setSingleChoiceItems(this.mListAdapter, AppCompatSpinner.this.getSelectedItemPosition(), this).create();
      this.mPopup = alertDialog;
      ListView listView = alertDialog.getListView();
      if (Build.VERSION.SDK_INT >= 17) {
        listView.setTextDirection(param1Int1);
        listView.setTextAlignment(param1Int2);
      } 
      this.mPopup.show();
    }
  }
  
  private static class DropDownAdapter implements ListAdapter, SpinnerAdapter {
    private SpinnerAdapter mAdapter;
    
    private ListAdapter mListAdapter;
    
    public DropDownAdapter(SpinnerAdapter param1SpinnerAdapter, Resources.Theme param1Theme) {
      this.mAdapter = param1SpinnerAdapter;
      if (param1SpinnerAdapter instanceof ListAdapter)
        this.mListAdapter = (ListAdapter)param1SpinnerAdapter; 
      if (param1Theme != null) {
        ThemedSpinnerAdapter themedSpinnerAdapter;
        if (Build.VERSION.SDK_INT >= 23 && param1SpinnerAdapter instanceof ThemedSpinnerAdapter) {
          themedSpinnerAdapter = (ThemedSpinnerAdapter)param1SpinnerAdapter;
          if (themedSpinnerAdapter.getDropDownViewTheme() != param1Theme)
            themedSpinnerAdapter.setDropDownViewTheme(param1Theme); 
        } else if (themedSpinnerAdapter instanceof ThemedSpinnerAdapter) {
          ThemedSpinnerAdapter themedSpinnerAdapter1 = (ThemedSpinnerAdapter)themedSpinnerAdapter;
          if (themedSpinnerAdapter1.getDropDownViewTheme() == null)
            themedSpinnerAdapter1.setDropDownViewTheme(param1Theme); 
        } 
      } 
    }
    
    public boolean areAllItemsEnabled() {
      ListAdapter listAdapter = this.mListAdapter;
      return (listAdapter != null) ? listAdapter.areAllItemsEnabled() : true;
    }
    
    public int getCount() {
      int i;
      SpinnerAdapter spinnerAdapter = this.mAdapter;
      if (spinnerAdapter == null) {
        i = 0;
      } else {
        i = spinnerAdapter.getCount();
      } 
      return i;
    }
    
    public View getDropDownView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      SpinnerAdapter spinnerAdapter = this.mAdapter;
      if (spinnerAdapter == null) {
        param1View = null;
      } else {
        param1View = spinnerAdapter.getDropDownView(param1Int, param1View, param1ViewGroup);
      } 
      return param1View;
    }
    
    public Object getItem(int param1Int) {
      Object object = this.mAdapter;
      if (object == null) {
        object = null;
      } else {
        object = object.getItem(param1Int);
      } 
      return object;
    }
    
    public long getItemId(int param1Int) {
      long l;
      SpinnerAdapter spinnerAdapter = this.mAdapter;
      if (spinnerAdapter == null) {
        l = -1L;
      } else {
        l = spinnerAdapter.getItemId(param1Int);
      } 
      return l;
    }
    
    public int getItemViewType(int param1Int) {
      return 0;
    }
    
    public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      return getDropDownView(param1Int, param1View, param1ViewGroup);
    }
    
    public int getViewTypeCount() {
      return 1;
    }
    
    public boolean hasStableIds() {
      boolean bool;
      SpinnerAdapter spinnerAdapter = this.mAdapter;
      if (spinnerAdapter != null && spinnerAdapter.hasStableIds()) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean isEmpty() {
      boolean bool;
      if (getCount() == 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean isEnabled(int param1Int) {
      ListAdapter listAdapter = this.mListAdapter;
      return (listAdapter != null) ? listAdapter.isEnabled(param1Int) : true;
    }
    
    public void registerDataSetObserver(DataSetObserver param1DataSetObserver) {
      SpinnerAdapter spinnerAdapter = this.mAdapter;
      if (spinnerAdapter != null)
        spinnerAdapter.registerDataSetObserver(param1DataSetObserver); 
    }
    
    public void unregisterDataSetObserver(DataSetObserver param1DataSetObserver) {
      SpinnerAdapter spinnerAdapter = this.mAdapter;
      if (spinnerAdapter != null)
        spinnerAdapter.unregisterDataSetObserver(param1DataSetObserver); 
    }
  }
  
  class DropdownPopup extends ListPopupWindow implements SpinnerPopup {
    ListAdapter mAdapter;
    
    private CharSequence mHintText;
    
    private int mOriginalHorizontalOffset;
    
    private final Rect mVisibleRect = new Rect();
    
    final AppCompatSpinner this$0;
    
    public DropdownPopup(Context param1Context, AttributeSet param1AttributeSet, int param1Int) {
      super(param1Context, param1AttributeSet, param1Int);
      setAnchorView((View)AppCompatSpinner.this);
      setModal(true);
      setPromptPosition(0);
      setOnItemClickListener(new AdapterView.OnItemClickListener() {
            final AppCompatSpinner.DropdownPopup this$1;
            
            final AppCompatSpinner val$this$0;
            
            public void onItemClick(AdapterView<?> param2AdapterView, View param2View, int param2Int, long param2Long) {
              AppCompatSpinner.this.setSelection(param2Int);
              if (AppCompatSpinner.this.getOnItemClickListener() != null)
                AppCompatSpinner.this.performItemClick(param2View, param2Int, AppCompatSpinner.DropdownPopup.this.mAdapter.getItemId(param2Int)); 
              AppCompatSpinner.DropdownPopup.this.dismiss();
            }
          });
    }
    
    void computeContentWidth() {
      Drawable drawable = getBackground();
      int i = 0;
      if (drawable != null) {
        drawable.getPadding(AppCompatSpinner.this.mTempRect);
        if (ViewUtils.isLayoutRtl((View)AppCompatSpinner.this)) {
          i = AppCompatSpinner.this.mTempRect.right;
        } else {
          i = -AppCompatSpinner.this.mTempRect.left;
        } 
      } else {
        Rect rect = AppCompatSpinner.this.mTempRect;
        AppCompatSpinner.this.mTempRect.right = 0;
        rect.left = 0;
      } 
      int m = AppCompatSpinner.this.getPaddingLeft();
      int j = AppCompatSpinner.this.getPaddingRight();
      int k = AppCompatSpinner.this.getWidth();
      if (AppCompatSpinner.this.mDropDownWidth == -2) {
        int i2 = AppCompatSpinner.this.compatMeasureContentWidth((SpinnerAdapter)this.mAdapter, getBackground());
        int i1 = (AppCompatSpinner.this.getContext().getResources().getDisplayMetrics()).widthPixels - AppCompatSpinner.this.mTempRect.left - AppCompatSpinner.this.mTempRect.right;
        int n = i2;
        if (i2 > i1)
          n = i1; 
        setContentWidth(Math.max(n, k - m - j));
      } else if (AppCompatSpinner.this.mDropDownWidth == -1) {
        setContentWidth(k - m - j);
      } else {
        setContentWidth(AppCompatSpinner.this.mDropDownWidth);
      } 
      if (ViewUtils.isLayoutRtl((View)AppCompatSpinner.this)) {
        i += k - j - getWidth() - getHorizontalOriginalOffset();
      } else {
        i += getHorizontalOriginalOffset() + m;
      } 
      setHorizontalOffset(i);
    }
    
    public CharSequence getHintText() {
      return this.mHintText;
    }
    
    public int getHorizontalOriginalOffset() {
      return this.mOriginalHorizontalOffset;
    }
    
    boolean isVisibleToUser(View param1View) {
      boolean bool;
      if (ViewCompat.isAttachedToWindow(param1View) && param1View.getGlobalVisibleRect(this.mVisibleRect)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void setAdapter(ListAdapter param1ListAdapter) {
      super.setAdapter(param1ListAdapter);
      this.mAdapter = param1ListAdapter;
    }
    
    public void setHorizontalOriginalOffset(int param1Int) {
      this.mOriginalHorizontalOffset = param1Int;
    }
    
    public void setPromptText(CharSequence param1CharSequence) {
      this.mHintText = param1CharSequence;
    }
    
    public void show(int param1Int1, int param1Int2) {
      boolean bool = isShowing();
      computeContentWidth();
      setInputMethodMode(2);
      show();
      ListView listView = getListView();
      listView.setChoiceMode(1);
      if (Build.VERSION.SDK_INT >= 17) {
        listView.setTextDirection(param1Int1);
        listView.setTextAlignment(param1Int2);
      } 
      setSelection(AppCompatSpinner.this.getSelectedItemPosition());
      if (bool)
        return; 
      ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
      if (viewTreeObserver != null) {
        final ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            final AppCompatSpinner.DropdownPopup this$1;
            
            public void onGlobalLayout() {
              AppCompatSpinner.DropdownPopup dropdownPopup = AppCompatSpinner.DropdownPopup.this;
              if (!dropdownPopup.isVisibleToUser((View)AppCompatSpinner.this)) {
                AppCompatSpinner.DropdownPopup.this.dismiss();
              } else {
                AppCompatSpinner.DropdownPopup.this.computeContentWidth();
                AppCompatSpinner.DropdownPopup.this.show();
              } 
            }
          };
        viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener);
        setOnDismissListener(new PopupWindow.OnDismissListener() {
              final AppCompatSpinner.DropdownPopup this$1;
              
              final ViewTreeObserver.OnGlobalLayoutListener val$layoutListener;
              
              public void onDismiss() {
                ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
                if (viewTreeObserver != null)
                  viewTreeObserver.removeGlobalOnLayoutListener(layoutListener); 
              }
            });
      } 
    }
  }
  
  class null implements AdapterView.OnItemClickListener {
    final AppCompatSpinner.DropdownPopup this$1;
    
    final AppCompatSpinner val$this$0;
    
    public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
      AppCompatSpinner.this.setSelection(param1Int);
      if (AppCompatSpinner.this.getOnItemClickListener() != null)
        AppCompatSpinner.this.performItemClick(param1View, param1Int, this.this$1.mAdapter.getItemId(param1Int)); 
      this.this$1.dismiss();
    }
  }
  
  class null implements ViewTreeObserver.OnGlobalLayoutListener {
    final AppCompatSpinner.DropdownPopup this$1;
    
    public void onGlobalLayout() {
      AppCompatSpinner.DropdownPopup dropdownPopup = this.this$1;
      if (!dropdownPopup.isVisibleToUser((View)AppCompatSpinner.this)) {
        this.this$1.dismiss();
      } else {
        this.this$1.computeContentWidth();
        this.this$1.show();
      } 
    }
  }
  
  class null implements PopupWindow.OnDismissListener {
    final AppCompatSpinner.DropdownPopup this$1;
    
    final ViewTreeObserver.OnGlobalLayoutListener val$layoutListener;
    
    public void onDismiss() {
      ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
      if (viewTreeObserver != null)
        viewTreeObserver.removeGlobalOnLayoutListener(layoutListener); 
    }
  }
  
  static class SavedState extends View.BaseSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public AppCompatSpinner.SavedState createFromParcel(Parcel param2Parcel) {
          return new AppCompatSpinner.SavedState(param2Parcel);
        }
        
        public AppCompatSpinner.SavedState[] newArray(int param2Int) {
          return new AppCompatSpinner.SavedState[param2Int];
        }
      };
    
    boolean mShowDropdown;
    
    SavedState(Parcel param1Parcel) {
      super(param1Parcel);
      boolean bool;
      if (param1Parcel.readByte() != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      this.mShowDropdown = bool;
    }
    
    SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeByte((byte)this.mShowDropdown);
    }
  }
  
  class null implements Parcelable.Creator<SavedState> {
    public AppCompatSpinner.SavedState createFromParcel(Parcel param1Parcel) {
      return new AppCompatSpinner.SavedState(param1Parcel);
    }
    
    public AppCompatSpinner.SavedState[] newArray(int param1Int) {
      return new AppCompatSpinner.SavedState[param1Int];
    }
  }
  
  static interface SpinnerPopup {
    void dismiss();
    
    Drawable getBackground();
    
    CharSequence getHintText();
    
    int getHorizontalOffset();
    
    int getHorizontalOriginalOffset();
    
    int getVerticalOffset();
    
    boolean isShowing();
    
    void setAdapter(ListAdapter param1ListAdapter);
    
    void setBackgroundDrawable(Drawable param1Drawable);
    
    void setHorizontalOffset(int param1Int);
    
    void setHorizontalOriginalOffset(int param1Int);
    
    void setPromptText(CharSequence param1CharSequence);
    
    void setVerticalOffset(int param1Int);
    
    void show(int param1Int1, int param1Int2);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\AppCompatSpinner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */