package com.google.android.material.timepicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R;
import com.google.android.material.resources.MaterialResources;
import java.util.Arrays;

class ClockFaceView extends RadialViewGroup implements ClockHandView.OnRotateListener {
  private static final float EPSILON = 0.001F;
  
  private static final int INITIAL_CAPACITY = 12;
  
  private static final String VALUE_PLACEHOLDER = "";
  
  private final int clockHandPadding;
  
  private final ClockHandView clockHandView;
  
  private final int clockSize;
  
  private float currentHandRotation;
  
  private final int[] gradientColors;
  
  private final float[] gradientPositions = new float[] { 0.0F, 0.9F, 1.0F };
  
  private final int minimumHeight;
  
  private final int minimumWidth;
  
  private final RectF scratch = new RectF();
  
  private final ColorStateList textColor;
  
  private final SparseArray<TextView> textViewPool = new SparseArray();
  
  private final Rect textViewRect = new Rect();
  
  private final AccessibilityDelegateCompat valueAccessibilityDelegate;
  
  private String[] values;
  
  public ClockFaceView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public ClockFaceView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.materialClockStyle);
  }
  
  public ClockFaceView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ClockFaceView, paramInt, R.style.Widget_MaterialComponents_TimePicker_Clock);
    Resources resources = getResources();
    ColorStateList colorStateList2 = MaterialResources.getColorStateList(paramContext, typedArray, R.styleable.ClockFaceView_clockNumberTextColor);
    this.textColor = colorStateList2;
    LayoutInflater.from(paramContext).inflate(R.layout.material_clockface_view, (ViewGroup)this, true);
    ClockHandView clockHandView = (ClockHandView)findViewById(R.id.material_clock_hand);
    this.clockHandView = clockHandView;
    this.clockHandPadding = resources.getDimensionPixelSize(R.dimen.material_clock_hand_padding);
    paramInt = colorStateList2.getDefaultColor();
    paramInt = colorStateList2.getColorForState(new int[] { 16842913 }, paramInt);
    this.gradientColors = new int[] { paramInt, paramInt, colorStateList2.getDefaultColor() };
    clockHandView.addOnRotateListener(this);
    paramInt = AppCompatResources.getColorStateList(paramContext, R.color.material_timepicker_clockface).getDefaultColor();
    ColorStateList colorStateList1 = MaterialResources.getColorStateList(paramContext, typedArray, R.styleable.ClockFaceView_clockFaceBackgroundColor);
    if (colorStateList1 != null)
      paramInt = colorStateList1.getDefaultColor(); 
    setBackgroundColor(paramInt);
    getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
          final ClockFaceView this$0;
          
          public boolean onPreDraw() {
            if (!ClockFaceView.this.isShown())
              return true; 
            ClockFaceView.this.getViewTreeObserver().removeOnPreDrawListener(this);
            int k = ClockFaceView.this.getHeight() / 2;
            int i = ClockFaceView.this.clockHandView.getSelectorRadius();
            int j = ClockFaceView.this.clockHandPadding;
            ClockFaceView.this.setRadius(k - i - j);
            return true;
          }
        });
    setFocusable(true);
    typedArray.recycle();
    this.valueAccessibilityDelegate = new AccessibilityDelegateCompat() {
        final ClockFaceView this$0;
        
        public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
          super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
          int i = ((Integer)param1View.getTag(R.id.material_value_index)).intValue();
          if (i > 0)
            param1AccessibilityNodeInfoCompat.setTraversalAfter((View)ClockFaceView.this.textViewPool.get(i - 1)); 
          param1AccessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(0, 1, i, 1, false, param1View.isSelected()));
        }
      };
    String[] arrayOfString = new String[12];
    Arrays.fill((Object[])arrayOfString, "");
    setValues(arrayOfString, 0);
    this.minimumHeight = resources.getDimensionPixelSize(R.dimen.material_time_picker_minimum_screen_height);
    this.minimumWidth = resources.getDimensionPixelSize(R.dimen.material_time_picker_minimum_screen_width);
    this.clockSize = resources.getDimensionPixelSize(R.dimen.material_clock_size);
  }
  
  private void findIntersectingTextView() {
    RectF rectF = this.clockHandView.getCurrentSelectorBox();
    for (byte b = 0; b < this.textViewPool.size(); b++) {
      TextView textView = (TextView)this.textViewPool.get(b);
      if (textView != null) {
        textView.getDrawingRect(this.textViewRect);
        this.textViewRect.offset(textView.getPaddingLeft(), textView.getPaddingTop());
        offsetDescendantRectToMyCoords((View)textView, this.textViewRect);
        this.scratch.set(this.textViewRect);
        RadialGradient radialGradient = getGradientForTextView(rectF, this.scratch);
        textView.getPaint().setShader((Shader)radialGradient);
        textView.invalidate();
      } 
    } 
  }
  
  private RadialGradient getGradientForTextView(RectF paramRectF1, RectF paramRectF2) {
    return !RectF.intersects(paramRectF1, paramRectF2) ? null : new RadialGradient(paramRectF1.centerX() - this.scratch.left, paramRectF1.centerY() - this.scratch.top, 0.5F * paramRectF1.width(), this.gradientColors, this.gradientPositions, Shader.TileMode.CLAMP);
  }
  
  private static float max3(float paramFloat1, float paramFloat2, float paramFloat3) {
    return Math.max(Math.max(paramFloat1, paramFloat2), paramFloat3);
  }
  
  private void updateTextViews(int paramInt) {
    LayoutInflater layoutInflater = LayoutInflater.from(getContext());
    int i = this.textViewPool.size();
    for (byte b = 0; b < Math.max(this.values.length, i); b++) {
      TextView textView = (TextView)this.textViewPool.get(b);
      if (b >= this.values.length) {
        removeView((View)textView);
        this.textViewPool.remove(b);
      } else {
        TextView textView1 = textView;
        if (textView == null) {
          textView1 = (TextView)layoutInflater.inflate(R.layout.material_clockface_textview, (ViewGroup)this, false);
          this.textViewPool.put(b, textView1);
          addView((View)textView1);
        } 
        textView1.setVisibility(0);
        textView1.setText(this.values[b]);
        textView1.setTag(R.id.material_value_index, Integer.valueOf(b));
        ViewCompat.setAccessibilityDelegate((View)textView1, this.valueAccessibilityDelegate);
        textView1.setTextColor(this.textColor);
        if (paramInt != 0)
          textView1.setContentDescription(getResources().getString(paramInt, new Object[] { this.values[b] })); 
      } 
    } 
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    AccessibilityNodeInfoCompat.wrap(paramAccessibilityNodeInfo).setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(1, this.values.length, false, 1));
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    findIntersectingTextView();
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
    float f2 = displayMetrics.heightPixels;
    float f1 = displayMetrics.widthPixels;
    paramInt2 = (int)(this.clockSize / max3(this.minimumHeight / f2, this.minimumWidth / f1, 1.0F));
    paramInt1 = View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824);
    setMeasuredDimension(paramInt2, paramInt2);
    super.onMeasure(paramInt1, paramInt1);
  }
  
  public void onRotate(float paramFloat, boolean paramBoolean) {
    if (Math.abs(this.currentHandRotation - paramFloat) > 0.001F) {
      this.currentHandRotation = paramFloat;
      findIntersectingTextView();
    } 
  }
  
  public void setHandRotation(float paramFloat) {
    this.clockHandView.setHandRotation(paramFloat);
    findIntersectingTextView();
  }
  
  public void setRadius(int paramInt) {
    if (paramInt != getRadius()) {
      super.setRadius(paramInt);
      this.clockHandView.setCircleRadius(getRadius());
    } 
  }
  
  public void setValues(String[] paramArrayOfString, int paramInt) {
    this.values = paramArrayOfString;
    updateTextViews(paramInt);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\timepicker\ClockFaceView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */