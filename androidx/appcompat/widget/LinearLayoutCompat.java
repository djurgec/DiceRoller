package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import androidx.appcompat.R;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LinearLayoutCompat extends ViewGroup {
  private static final String ACCESSIBILITY_CLASS_NAME = "androidx.appcompat.widget.LinearLayoutCompat";
  
  public static final int HORIZONTAL = 0;
  
  private static final int INDEX_BOTTOM = 2;
  
  private static final int INDEX_CENTER_VERTICAL = 0;
  
  private static final int INDEX_FILL = 3;
  
  private static final int INDEX_TOP = 1;
  
  public static final int SHOW_DIVIDER_BEGINNING = 1;
  
  public static final int SHOW_DIVIDER_END = 4;
  
  public static final int SHOW_DIVIDER_MIDDLE = 2;
  
  public static final int SHOW_DIVIDER_NONE = 0;
  
  public static final int VERTICAL = 1;
  
  private static final int VERTICAL_GRAVITY_COUNT = 4;
  
  private boolean mBaselineAligned = true;
  
  private int mBaselineAlignedChildIndex = -1;
  
  private int mBaselineChildTop = 0;
  
  private Drawable mDivider;
  
  private int mDividerHeight;
  
  private int mDividerPadding;
  
  private int mDividerWidth;
  
  private int mGravity = 8388659;
  
  private int[] mMaxAscent;
  
  private int[] mMaxDescent;
  
  private int mOrientation;
  
  private int mShowDividers;
  
  private int mTotalLength;
  
  private boolean mUseLargestChild;
  
  private float mWeightSum;
  
  public LinearLayoutCompat(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public LinearLayoutCompat(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public LinearLayoutCompat(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.LinearLayoutCompat, paramInt, 0);
    ViewCompat.saveAttributeDataForStyleable((View)this, paramContext, R.styleable.LinearLayoutCompat, paramAttributeSet, tintTypedArray.getWrappedTypeArray(), paramInt, 0);
    paramInt = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_orientation, -1);
    if (paramInt >= 0)
      setOrientation(paramInt); 
    paramInt = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_gravity, -1);
    if (paramInt >= 0)
      setGravity(paramInt); 
    boolean bool = tintTypedArray.getBoolean(R.styleable.LinearLayoutCompat_android_baselineAligned, true);
    if (!bool)
      setBaselineAligned(bool); 
    this.mWeightSum = tintTypedArray.getFloat(R.styleable.LinearLayoutCompat_android_weightSum, -1.0F);
    this.mBaselineAlignedChildIndex = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
    this.mUseLargestChild = tintTypedArray.getBoolean(R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
    setDividerDrawable(tintTypedArray.getDrawable(R.styleable.LinearLayoutCompat_divider));
    this.mShowDividers = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_showDividers, 0);
    this.mDividerPadding = tintTypedArray.getDimensionPixelSize(R.styleable.LinearLayoutCompat_dividerPadding, 0);
    tintTypedArray.recycle();
  }
  
  private void forceUniformHeight(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
    for (byte b = 0; b < paramInt1; b++) {
      View view = getVirtualChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.height == -1) {
          int j = layoutParams.width;
          layoutParams.width = view.getMeasuredWidth();
          measureChildWithMargins(view, paramInt2, 0, i, 0);
          layoutParams.width = j;
        } 
      } 
    } 
  }
  
  private void forceUniformWidth(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
    for (byte b = 0; b < paramInt1; b++) {
      View view = getVirtualChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.width == -1) {
          int j = layoutParams.height;
          layoutParams.height = view.getMeasuredHeight();
          measureChildWithMargins(view, i, 0, paramInt2, 0);
          layoutParams.height = j;
        } 
      } 
    } 
  }
  
  private void setChildFrame(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramView.layout(paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4);
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  void drawDividersHorizontal(Canvas paramCanvas) {
    int j = getVirtualChildCount();
    boolean bool = ViewUtils.isLayoutRtl((View)this);
    int i;
    for (i = 0; i < j; i++) {
      View view = getVirtualChildAt(i);
      if (view != null && view.getVisibility() != 8 && hasDividerBeforeChildAt(i)) {
        int k;
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (bool) {
          k = view.getRight() + layoutParams.rightMargin;
        } else {
          k = view.getLeft() - layoutParams.leftMargin - this.mDividerWidth;
        } 
        drawVerticalDivider(paramCanvas, k);
      } 
    } 
    if (hasDividerBeforeChildAt(j)) {
      View view = getVirtualChildAt(j - 1);
      if (view == null) {
        if (bool) {
          i = getPaddingLeft();
        } else {
          i = getWidth() - getPaddingRight() - this.mDividerWidth;
        } 
      } else {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (bool) {
          i = view.getLeft() - layoutParams.leftMargin - this.mDividerWidth;
        } else {
          i = view.getRight() + layoutParams.rightMargin;
        } 
      } 
      drawVerticalDivider(paramCanvas, i);
    } 
  }
  
  void drawDividersVertical(Canvas paramCanvas) {
    int j = getVirtualChildCount();
    int i;
    for (i = 0; i < j; i++) {
      View view = getVirtualChildAt(i);
      if (view != null && view.getVisibility() != 8 && hasDividerBeforeChildAt(i)) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        drawHorizontalDivider(paramCanvas, view.getTop() - layoutParams.topMargin - this.mDividerHeight);
      } 
    } 
    if (hasDividerBeforeChildAt(j)) {
      View view = getVirtualChildAt(j - 1);
      if (view == null) {
        i = getHeight() - getPaddingBottom() - this.mDividerHeight;
      } else {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        i = view.getBottom() + layoutParams.bottomMargin;
      } 
      drawHorizontalDivider(paramCanvas, i);
    } 
  }
  
  void drawHorizontalDivider(Canvas paramCanvas, int paramInt) {
    this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, paramInt, getWidth() - getPaddingRight() - this.mDividerPadding, this.mDividerHeight + paramInt);
    this.mDivider.draw(paramCanvas);
  }
  
  void drawVerticalDivider(Canvas paramCanvas, int paramInt) {
    this.mDivider.setBounds(paramInt, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + paramInt, getHeight() - getPaddingBottom() - this.mDividerPadding);
    this.mDivider.draw(paramCanvas);
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    int i = this.mOrientation;
    return (i == 0) ? new LayoutParams(-2, -2) : ((i == 1) ? new LayoutParams(-1, -2) : null);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return new LayoutParams(paramLayoutParams);
  }
  
  public int getBaseline() {
    if (this.mBaselineAlignedChildIndex < 0)
      return super.getBaseline(); 
    int i = getChildCount();
    int j = this.mBaselineAlignedChildIndex;
    if (i > j) {
      View view = getChildAt(j);
      int k = view.getBaseline();
      if (k == -1) {
        if (this.mBaselineAlignedChildIndex == 0)
          return -1; 
        throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
      } 
      j = this.mBaselineChildTop;
      i = j;
      if (this.mOrientation == 1) {
        int m = this.mGravity & 0x70;
        i = j;
        if (m != 48) {
          switch (m) {
            default:
              i = j;
              return ((LayoutParams)view.getLayoutParams()).topMargin + i + k;
            case 80:
              i = getBottom() - getTop() - getPaddingBottom() - this.mTotalLength;
              return ((LayoutParams)view.getLayoutParams()).topMargin + i + k;
            case 16:
              break;
          } 
          i = j + (getBottom() - getTop() - getPaddingTop() - getPaddingBottom() - this.mTotalLength) / 2;
        } 
      } 
      return ((LayoutParams)view.getLayoutParams()).topMargin + i + k;
    } 
    throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
  }
  
  public int getBaselineAlignedChildIndex() {
    return this.mBaselineAlignedChildIndex;
  }
  
  int getChildrenSkipCount(View paramView, int paramInt) {
    return 0;
  }
  
  public Drawable getDividerDrawable() {
    return this.mDivider;
  }
  
  public int getDividerPadding() {
    return this.mDividerPadding;
  }
  
  public int getDividerWidth() {
    return this.mDividerWidth;
  }
  
  public int getGravity() {
    return this.mGravity;
  }
  
  int getLocationOffset(View paramView) {
    return 0;
  }
  
  int getNextLocationOffset(View paramView) {
    return 0;
  }
  
  public int getOrientation() {
    return this.mOrientation;
  }
  
  public int getShowDividers() {
    return this.mShowDividers;
  }
  
  View getVirtualChildAt(int paramInt) {
    return getChildAt(paramInt);
  }
  
  int getVirtualChildCount() {
    return getChildCount();
  }
  
  public float getWeightSum() {
    return this.mWeightSum;
  }
  
  protected boolean hasDividerBeforeChildAt(int paramInt) {
    boolean bool2 = false;
    boolean bool1 = false;
    if (paramInt == 0) {
      if ((this.mShowDividers & 0x1) != 0)
        bool1 = true; 
      return bool1;
    } 
    if (paramInt == getChildCount()) {
      bool1 = bool2;
      if ((this.mShowDividers & 0x4) != 0)
        bool1 = true; 
      return bool1;
    } 
    if ((this.mShowDividers & 0x2) != 0) {
      bool2 = false;
      paramInt--;
      while (true) {
        bool1 = bool2;
        if (paramInt >= 0) {
          if (getChildAt(paramInt).getVisibility() != 8) {
            bool1 = true;
            break;
          } 
          paramInt--;
          continue;
        } 
        break;
      } 
      return bool1;
    } 
    return false;
  }
  
  public boolean isBaselineAligned() {
    return this.mBaselineAligned;
  }
  
  public boolean isMeasureWithLargestChildEnabled() {
    return this.mUseLargestChild;
  }
  
  void layoutHorizontal(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    byte b1;
    byte b2;
    boolean bool1 = ViewUtils.isLayoutRtl((View)this);
    int m = getPaddingTop();
    int n = paramInt4 - paramInt2;
    int j = n - getPaddingBottom();
    int i3 = getPaddingBottom();
    int i2 = getVirtualChildCount();
    int i1 = this.mGravity;
    boolean bool2 = this.mBaselineAligned;
    int[] arrayOfInt1 = this.mMaxAscent;
    int[] arrayOfInt2 = this.mMaxDescent;
    int k = ViewCompat.getLayoutDirection((View)this);
    switch (GravityCompat.getAbsoluteGravity(i1 & 0x800007, k)) {
      default:
        paramInt1 = getPaddingLeft();
        break;
      case 5:
        paramInt1 = getPaddingLeft() + paramInt3 - paramInt1 - this.mTotalLength;
        break;
      case 1:
        paramInt1 = getPaddingLeft() + (paramInt3 - paramInt1 - this.mTotalLength) / 2;
        break;
    } 
    if (bool1) {
      b2 = i2 - 1;
      b1 = -1;
    } else {
      b2 = 0;
      b1 = 1;
    } 
    paramInt2 = 0;
    int i = n;
    paramInt3 = m;
    paramInt4 = paramInt1;
    while (paramInt2 < i2) {
      int i4 = b2 + b1 * paramInt2;
      View view = getVirtualChildAt(i4);
      if (view == null) {
        paramInt4 += measureNullChild(i4);
      } else if (view.getVisibility() != 8) {
        int i6 = view.getMeasuredWidth();
        int i7 = view.getMeasuredHeight();
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (bool2 && layoutParams.height != -1) {
          paramInt1 = view.getBaseline();
        } else {
          paramInt1 = -1;
        } 
        int i5 = layoutParams.gravity;
        if (i5 < 0)
          i5 = i1 & 0x70; 
        switch (i5 & 0x70) {
          default:
            paramInt1 = paramInt3;
            break;
          case 80:
            i5 = j - i7 - layoutParams.bottomMargin;
            if (paramInt1 != -1) {
              int i8 = view.getMeasuredHeight();
              paramInt1 = i5 - arrayOfInt2[2] - i8 - paramInt1;
              break;
            } 
            paramInt1 = i5;
            break;
          case 48:
            i5 = layoutParams.topMargin + paramInt3;
            if (paramInt1 != -1) {
              paramInt1 = i5 + arrayOfInt1[1] - paramInt1;
              break;
            } 
            paramInt1 = i5;
            break;
          case 16:
            paramInt1 = (n - m - i3 - i7) / 2 + paramInt3 + layoutParams.topMargin - layoutParams.bottomMargin;
            break;
        } 
        i5 = paramInt4;
        if (hasDividerBeforeChildAt(i4))
          i5 = paramInt4 + this.mDividerWidth; 
        paramInt4 = i5 + layoutParams.leftMargin;
        setChildFrame(view, paramInt4 + getLocationOffset(view), paramInt1, i6, i7);
        paramInt1 = layoutParams.rightMargin;
        i5 = getNextLocationOffset(view);
        paramInt2 += getChildrenSkipCount(view, i4);
        paramInt4 += i6 + paramInt1 + i5;
      } 
      paramInt2++;
    } 
  }
  
  void layoutVertical(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = getPaddingLeft();
    int k = paramInt3 - paramInt1;
    int j = getPaddingRight();
    int m = getPaddingRight();
    int i1 = getVirtualChildCount();
    int n = this.mGravity;
    switch (n & 0x70) {
      default:
        paramInt1 = getPaddingTop();
        break;
      case 80:
        paramInt1 = getPaddingTop() + paramInt4 - paramInt2 - this.mTotalLength;
        break;
      case 16:
        paramInt1 = getPaddingTop() + (paramInt4 - paramInt2 - this.mTotalLength) / 2;
        break;
    } 
    paramInt2 = 0;
    paramInt3 = i;
    while (true) {
      paramInt4 = paramInt3;
      if (paramInt2 < i1) {
        View view = getVirtualChildAt(paramInt2);
        if (view == null) {
          paramInt1 += measureNullChild(paramInt2);
        } else if (view.getVisibility() != 8) {
          int i4 = view.getMeasuredWidth();
          int i3 = view.getMeasuredHeight();
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          paramInt3 = layoutParams.gravity;
          if (paramInt3 < 0)
            paramInt3 = n & 0x800007; 
          switch (GravityCompat.getAbsoluteGravity(paramInt3, ViewCompat.getLayoutDirection((View)this)) & 0x7) {
            default:
              paramInt3 = layoutParams.leftMargin + paramInt4;
              break;
            case 5:
              paramInt3 = k - j - i4 - layoutParams.rightMargin;
              break;
            case 1:
              paramInt3 = (k - i - m - i4) / 2 + paramInt4 + layoutParams.leftMargin - layoutParams.rightMargin;
              break;
          } 
          int i2 = paramInt1;
          if (hasDividerBeforeChildAt(paramInt2))
            i2 = paramInt1 + this.mDividerHeight; 
          paramInt1 = i2 + layoutParams.topMargin;
          setChildFrame(view, paramInt3, paramInt1 + getLocationOffset(view), i4, i3);
          paramInt3 = layoutParams.bottomMargin;
          i2 = getNextLocationOffset(view);
          paramInt2 += getChildrenSkipCount(view, paramInt2);
          paramInt1 += i3 + paramInt3 + i2;
        } 
        paramInt2++;
        paramInt3 = paramInt4;
        continue;
      } 
      break;
    } 
  }
  
  void measureChildBeforeLayout(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    measureChildWithMargins(paramView, paramInt2, paramInt3, paramInt4, paramInt5);
  }
  
  void measureHorizontal(int paramInt1, int paramInt2) {
    boolean bool2;
    this.mTotalLength = 0;
    int i2 = getVirtualChildCount();
    int i1 = View.MeasureSpec.getMode(paramInt1);
    int i8 = View.MeasureSpec.getMode(paramInt2);
    if (this.mMaxAscent == null || this.mMaxDescent == null) {
      this.mMaxAscent = new int[4];
      this.mMaxDescent = new int[4];
    } 
    int[] arrayOfInt2 = this.mMaxAscent;
    int[] arrayOfInt1 = this.mMaxDescent;
    arrayOfInt2[3] = -1;
    arrayOfInt2[2] = -1;
    arrayOfInt2[1] = -1;
    arrayOfInt2[0] = -1;
    arrayOfInt1[3] = -1;
    arrayOfInt1[2] = -1;
    arrayOfInt1[1] = -1;
    arrayOfInt1[0] = -1;
    boolean bool3 = this.mBaselineAligned;
    boolean bool4 = this.mUseLargestChild;
    if (i1 == 1073741824) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    int i3 = 0;
    int i5 = 0;
    float f = 0.0F;
    int m = 0;
    int i = 0;
    int i4 = 0;
    int n = 0;
    boolean bool1 = true;
    int j = 0;
    int k = 0;
    while (i3 < i2) {
      View view = getVirtualChildAt(i3);
      if (view == null) {
        this.mTotalLength += measureNullChild(i3);
      } else if (view.getVisibility() == 8) {
        i3 += getChildrenSkipCount(view, i3);
      } else {
        if (hasDividerBeforeChildAt(i3))
          this.mTotalLength += this.mDividerWidth; 
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        f += layoutParams.weight;
        if (i1 == 1073741824 && layoutParams.width == 0 && layoutParams.weight > 0.0F) {
          if (bool2) {
            this.mTotalLength += layoutParams.leftMargin + layoutParams.rightMargin;
          } else {
            int i13 = this.mTotalLength;
            this.mTotalLength = Math.max(i13, layoutParams.leftMargin + i13 + layoutParams.rightMargin);
          } 
          if (bool3) {
            int i13 = View.MeasureSpec.makeMeasureSpec(0, 0);
            view.measure(i13, i13);
            i13 = j;
          } else {
            i4 = 1;
            int i13 = j;
          } 
        } else {
          int i13;
          if (layoutParams.width == 0 && layoutParams.weight > 0.0F) {
            layoutParams.width = -2;
            i13 = 0;
          } else {
            i13 = Integer.MIN_VALUE;
          } 
          if (f == 0.0F) {
            i14 = this.mTotalLength;
          } else {
            i14 = 0;
          } 
          int i15 = j;
          measureChildBeforeLayout(view, i3, paramInt1, i14, paramInt2, 0);
          if (i13 != Integer.MIN_VALUE)
            layoutParams.width = i13; 
          int i14 = view.getMeasuredWidth();
          if (bool2) {
            this.mTotalLength += layoutParams.leftMargin + i14 + layoutParams.rightMargin + getNextLocationOffset(view);
          } else {
            i13 = this.mTotalLength;
            this.mTotalLength = Math.max(i13, i13 + i14 + layoutParams.leftMargin + layoutParams.rightMargin + getNextLocationOffset(view));
          } 
          if (bool4)
            i = Math.max(i14, i); 
        } 
        int i9 = j;
        int i11 = 0;
        int i10 = i11;
        j = n;
        if (i8 != 1073741824) {
          i10 = i11;
          j = n;
          if (layoutParams.height == -1) {
            j = 1;
            i10 = 1;
          } 
        } 
        i11 = layoutParams.topMargin + layoutParams.bottomMargin;
        n = view.getMeasuredHeight() + i11;
        int i12 = View.combineMeasuredStates(m, view.getMeasuredState());
        if (bool3) {
          int i13 = view.getBaseline();
          if (i13 != -1) {
            if (layoutParams.gravity < 0) {
              m = this.mGravity;
            } else {
              m = layoutParams.gravity;
            } 
            m = ((m & 0x70) >> 4 & 0xFFFFFFFE) >> 1;
            arrayOfInt2[m] = Math.max(arrayOfInt2[m], i13);
            arrayOfInt1[m] = Math.max(arrayOfInt1[m], n - i13);
          } 
        } 
        i5 = Math.max(i5, n);
        if (bool1 && layoutParams.height == -1) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        if (layoutParams.weight > 0.0F) {
          if (i10 != 0) {
            m = i11;
          } else {
            m = n;
          } 
          k = Math.max(k, m);
          m = i9;
        } else {
          if (i10 != 0) {
            m = i11;
          } else {
            m = n;
          } 
          m = Math.max(i9, m);
        } 
        i3 += getChildrenSkipCount(view, i3);
        i9 = i12;
        i10 = m;
        n = j;
        m = i9;
        j = i10;
      } 
      i3++;
    } 
    i3 = k;
    int i6 = i;
    if (this.mTotalLength > 0 && hasDividerBeforeChildAt(i2))
      this.mTotalLength += this.mDividerWidth; 
    if (arrayOfInt2[1] != -1 || arrayOfInt2[0] != -1 || arrayOfInt2[2] != -1 || arrayOfInt2[3] != -1) {
      i = Math.max(i5, Math.max(arrayOfInt2[3], Math.max(arrayOfInt2[0], Math.max(arrayOfInt2[1], arrayOfInt2[2]))) + Math.max(arrayOfInt1[3], Math.max(arrayOfInt1[0], Math.max(arrayOfInt1[1], arrayOfInt1[2]))));
    } else {
      i = i5;
    } 
    if (bool4 && (i1 == Integer.MIN_VALUE || i1 == 0)) {
      this.mTotalLength = 0;
      for (k = 0; k < i2; k++) {
        View view = getVirtualChildAt(k);
        if (view == null) {
          this.mTotalLength += measureNullChild(k);
        } else if (view.getVisibility() == 8) {
          k += getChildrenSkipCount(view, k);
        } else {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          if (bool2) {
            this.mTotalLength += layoutParams.leftMargin + i6 + layoutParams.rightMargin + getNextLocationOffset(view);
          } else {
            i5 = this.mTotalLength;
            this.mTotalLength = Math.max(i5, i5 + i6 + layoutParams.leftMargin + layoutParams.rightMargin + getNextLocationOffset(view));
          } 
        } 
      } 
    } 
    this.mTotalLength += getPaddingLeft() + getPaddingRight();
    k = View.resolveSizeAndState(Math.max(this.mTotalLength, getSuggestedMinimumWidth()), paramInt1, 0);
    i5 = k & 0xFFFFFF;
    int i7 = i5 - this.mTotalLength;
    if (i4 || (i7 != 0 && f > 0.0F)) {
      i6 = j;
      float f1 = this.mWeightSum;
      if (f1 > 0.0F)
        f = f1; 
      arrayOfInt2[3] = -1;
      arrayOfInt2[2] = -1;
      arrayOfInt2[1] = -1;
      arrayOfInt2[0] = -1;
      arrayOfInt1[3] = -1;
      arrayOfInt1[2] = -1;
      arrayOfInt1[1] = -1;
      arrayOfInt1[0] = -1;
      this.mTotalLength = 0;
      int i9 = 0;
      i = i7;
      i5 = -1;
      j = m;
      i4 = i3;
      i3 = i5;
      i5 = i1;
      i1 = i6;
      i6 = i9;
      m = i2;
      while (i6 < m) {
        View view = getVirtualChildAt(i6);
        if (view != null && view.getVisibility() != 8) {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          f1 = layoutParams.weight;
          if (f1 > 0.0F) {
            i9 = (int)(i * f1 / f);
            int i10 = getChildMeasureSpec(paramInt2, getPaddingTop() + getPaddingBottom() + layoutParams.topMargin + layoutParams.bottomMargin, layoutParams.height);
            if (layoutParams.width != 0 || i5 != 1073741824) {
              i7 = view.getMeasuredWidth() + i9;
              i2 = i7;
              if (i7 < 0)
                i2 = 0; 
              view.measure(View.MeasureSpec.makeMeasureSpec(i2, 1073741824), i10);
            } else {
              if (i9 > 0) {
                i2 = i9;
              } else {
                i2 = 0;
              } 
              view.measure(View.MeasureSpec.makeMeasureSpec(i2, 1073741824), i10);
            } 
            j = View.combineMeasuredStates(j, view.getMeasuredState() & 0xFF000000);
            f -= f1;
            i -= i9;
          } 
          if (bool2) {
            this.mTotalLength += view.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin + getNextLocationOffset(view);
          } else {
            i2 = this.mTotalLength;
            this.mTotalLength = Math.max(i2, view.getMeasuredWidth() + i2 + layoutParams.leftMargin + layoutParams.rightMargin + getNextLocationOffset(view));
          } 
          if (i8 != 1073741824 && layoutParams.height == -1) {
            i2 = 1;
          } else {
            i2 = 0;
          } 
          i7 = layoutParams.topMargin + layoutParams.bottomMargin;
          i9 = view.getMeasuredHeight() + i7;
          i3 = Math.max(i3, i9);
          if (i2 != 0) {
            i2 = i7;
          } else {
            i2 = i9;
          } 
          i2 = Math.max(i1, i2);
          if (bool1 && layoutParams.height == -1) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
          if (bool3) {
            i7 = view.getBaseline();
            if (i7 != -1) {
              if (layoutParams.gravity < 0) {
                i1 = this.mGravity;
              } else {
                i1 = layoutParams.gravity;
              } 
              i1 = ((i1 & 0x70) >> 4 & 0xFFFFFFFE) >> 1;
              arrayOfInt2[i1] = Math.max(arrayOfInt2[i1], i7);
              arrayOfInt1[i1] = Math.max(arrayOfInt1[i1], i9 - i7);
            } 
          } 
          i1 = i2;
        } 
        i6++;
      } 
      this.mTotalLength += getPaddingLeft() + getPaddingRight();
      if (arrayOfInt2[1] != -1 || arrayOfInt2[0] != -1 || arrayOfInt2[2] != -1 || arrayOfInt2[3] != -1) {
        i = Math.max(i3, Math.max(arrayOfInt2[3], Math.max(arrayOfInt2[0], Math.max(arrayOfInt2[1], arrayOfInt2[2]))) + Math.max(arrayOfInt1[3], Math.max(arrayOfInt1[0], Math.max(arrayOfInt1[1], arrayOfInt1[2]))));
      } else {
        i = i3;
      } 
      i2 = m;
      m = j;
      j = i1;
    } else {
      j = Math.max(j, i3);
      if (bool4 && i1 != 1073741824) {
        i3 = 0;
        i1 = i5;
        while (i3 < i2) {
          View view = getVirtualChildAt(i3);
          if (view != null && view.getVisibility() != 8 && ((LayoutParams)view.getLayoutParams()).weight > 0.0F)
            view.measure(View.MeasureSpec.makeMeasureSpec(i6, 1073741824), View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), 1073741824)); 
          i3++;
        } 
      } 
    } 
    i1 = i;
    if (!bool1) {
      i1 = i;
      if (i8 != 1073741824)
        i1 = j; 
    } 
    setMeasuredDimension(k | 0xFF000000 & m, View.resolveSizeAndState(Math.max(i1 + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), paramInt2, m << 16));
    if (n != 0)
      forceUniformHeight(i2, paramInt1); 
  }
  
  int measureNullChild(int paramInt) {
    return 0;
  }
  
  void measureVertical(int paramInt1, int paramInt2) {
    this.mTotalLength = 0;
    int i6 = getVirtualChildCount();
    int i11 = View.MeasureSpec.getMode(paramInt1);
    int i3 = View.MeasureSpec.getMode(paramInt2);
    int i9 = this.mBaselineAlignedChildIndex;
    boolean bool = this.mUseLargestChild;
    int i5 = 0;
    int n = 0;
    float f = 0.0F;
    int i1 = 0;
    int i4 = 0;
    int i2 = 0;
    int k = 0;
    int i = 0;
    int j = 0;
    int m = 1;
    while (i4 < i6) {
      View view = getVirtualChildAt(i4);
      if (view == null) {
        this.mTotalLength += measureNullChild(i4);
      } else if (view.getVisibility() == 8) {
        i4 += getChildrenSkipCount(view, i4);
      } else {
        if (hasDividerBeforeChildAt(i4))
          this.mTotalLength += this.mDividerHeight; 
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        f += layoutParams.weight;
        if (i3 == 1073741824 && layoutParams.height == 0 && layoutParams.weight > 0.0F) {
          i5 = this.mTotalLength;
          this.mTotalLength = Math.max(i5, layoutParams.topMargin + i5 + layoutParams.bottomMargin);
          i5 = 1;
        } else {
          if (layoutParams.height == 0 && layoutParams.weight > 0.0F) {
            layoutParams.height = -2;
            i13 = 0;
          } else {
            i13 = Integer.MIN_VALUE;
          } 
          if (f == 0.0F) {
            i14 = this.mTotalLength;
          } else {
            i14 = 0;
          } 
          measureChildBeforeLayout(view, i4, paramInt1, 0, paramInt2, i14);
          if (i13 != Integer.MIN_VALUE)
            layoutParams.height = i13; 
          int i14 = view.getMeasuredHeight();
          int i13 = this.mTotalLength;
          this.mTotalLength = Math.max(i13, i13 + i14 + layoutParams.topMargin + layoutParams.bottomMargin + getNextLocationOffset(view));
          if (bool)
            j = Math.max(i14, j); 
        } 
        int i12 = i1;
        if (i9 >= 0 && i9 == i4 + 1)
          this.mBaselineChildTop = this.mTotalLength; 
        if (i4 >= i9 || layoutParams.weight <= 0.0F) {
          int i14 = 0;
          int i13 = i14;
          i1 = i2;
          if (i11 != 1073741824) {
            i13 = i14;
            i1 = i2;
            if (layoutParams.width == -1) {
              i1 = 1;
              i13 = 1;
            } 
          } 
          i2 = layoutParams.leftMargin + layoutParams.rightMargin;
          i14 = view.getMeasuredWidth() + i2;
          n = Math.max(n, i14);
          int i15 = View.combineMeasuredStates(k, view.getMeasuredState());
          if (m && layoutParams.width == -1) {
            k = 1;
          } else {
            k = 0;
          } 
          if (layoutParams.weight > 0.0F) {
            if (i13 == 0)
              i2 = i14; 
            m = Math.max(i, i2);
            i = i12;
          } else {
            m = i;
            if (i13 == 0)
              i2 = i14; 
            i = Math.max(i12, i2);
          } 
          i2 = getChildrenSkipCount(view, i4);
          i12 = m;
          i13 = i2 + i4;
          i4 = i15;
          i2 = i1;
          m = k;
          k = i4;
          i1 = i;
          i = i12;
          i4 = i13;
        } else {
          throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
        } 
      } 
      i4++;
    } 
    i4 = i;
    int i7 = i1;
    if (this.mTotalLength > 0 && hasDividerBeforeChildAt(i6))
      this.mTotalLength += this.mDividerHeight; 
    if (bool) {
      if (i3 == Integer.MIN_VALUE || i3 == 0) {
        this.mTotalLength = 0;
        for (i = 0; i < i6; i++) {
          View view = getVirtualChildAt(i);
          if (view == null) {
            this.mTotalLength += measureNullChild(i);
          } else if (view.getVisibility() == 8) {
            i += getChildrenSkipCount(view, i);
          } else {
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            i1 = this.mTotalLength;
            this.mTotalLength = Math.max(i1, i1 + j + layoutParams.topMargin + layoutParams.bottomMargin + getNextLocationOffset(view));
          } 
        } 
        i = k;
      } else {
        i = k;
      } 
    } else {
      i = k;
    } 
    k = i3;
    this.mTotalLength += getPaddingTop() + getPaddingBottom();
    i1 = Math.max(this.mTotalLength, getSuggestedMinimumHeight());
    i3 = j;
    int i10 = View.resolveSizeAndState(i1, paramInt2, 0);
    int i8 = i10 & 0xFFFFFF;
    j = i8 - this.mTotalLength;
    if (i5 != 0 || (j != 0 && f > 0.0F)) {
      float f1 = this.mWeightSum;
      if (f1 > 0.0F)
        f = f1; 
      this.mTotalLength = 0;
      i5 = 0;
      i1 = i9;
      i4 = k;
      k = i7;
      while (i5 < i6) {
        View view = getVirtualChildAt(i5);
        if (view.getVisibility() != 8) {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          f1 = layoutParams.weight;
          if (f1 > 0.0F) {
            i8 = (int)(j * f1 / f);
            i9 = getPaddingLeft();
            int i15 = getPaddingRight();
            int i16 = layoutParams.leftMargin;
            int i14 = layoutParams.rightMargin;
            int i13 = layoutParams.width;
            i7 = j - i8;
            i13 = getChildMeasureSpec(paramInt1, i9 + i15 + i16 + i14, i13);
            if (layoutParams.height != 0 || i4 != 1073741824) {
              i8 = view.getMeasuredHeight() + i8;
              j = i8;
              if (i8 < 0)
                j = 0; 
              view.measure(i13, View.MeasureSpec.makeMeasureSpec(j, 1073741824));
            } else {
              if (i8 > 0) {
                j = i8;
              } else {
                j = 0;
              } 
              view.measure(i13, View.MeasureSpec.makeMeasureSpec(j, 1073741824));
            } 
            i = View.combineMeasuredStates(i, view.getMeasuredState() & 0xFFFFFF00);
            f -= f1;
            j = i7;
          } 
          int i12 = layoutParams.leftMargin + layoutParams.rightMargin;
          i8 = view.getMeasuredWidth() + i12;
          i7 = Math.max(n, i8);
          if (i11 != 1073741824 && layoutParams.width == -1) {
            n = 1;
          } else {
            n = 0;
          } 
          if (n != 0) {
            n = i12;
          } else {
            n = i8;
          } 
          n = Math.max(k, n);
          if (m != 0 && layoutParams.width == -1) {
            k = 1;
          } else {
            k = 0;
          } 
          m = this.mTotalLength;
          this.mTotalLength = Math.max(m, m + view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin + getNextLocationOffset(view));
          m = k;
          k = n;
          n = i7;
        } 
        i5++;
      } 
      this.mTotalLength += getPaddingTop() + getPaddingBottom();
    } else {
      j = Math.max(i7, i4);
      if (bool && k != 1073741824) {
        i1 = 0;
        k = i8;
        while (i1 < i6) {
          View view = getVirtualChildAt(i1);
          if (view != null && view.getVisibility() != 8 && ((LayoutParams)view.getLayoutParams()).weight > 0.0F)
            view.measure(View.MeasureSpec.makeMeasureSpec(view.getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(i3, 1073741824)); 
          i1++;
        } 
      } 
      k = j;
    } 
    j = n;
    if (m == 0) {
      j = n;
      if (i11 != 1073741824)
        j = k; 
    } 
    setMeasuredDimension(View.resolveSizeAndState(Math.max(j + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), paramInt1, i), i10);
    if (i2 != 0)
      forceUniformWidth(i6, paramInt2); 
  }
  
  protected void onDraw(Canvas paramCanvas) {
    if (this.mDivider == null)
      return; 
    if (this.mOrientation == 1) {
      drawDividersVertical(paramCanvas);
    } else {
      drawDividersHorizontal(paramCanvas);
    } 
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    paramAccessibilityEvent.setClassName("androidx.appcompat.widget.LinearLayoutCompat");
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName("androidx.appcompat.widget.LinearLayoutCompat");
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (this.mOrientation == 1) {
      layoutVertical(paramInt1, paramInt2, paramInt3, paramInt4);
    } else {
      layoutHorizontal(paramInt1, paramInt2, paramInt3, paramInt4);
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    if (this.mOrientation == 1) {
      measureVertical(paramInt1, paramInt2);
    } else {
      measureHorizontal(paramInt1, paramInt2);
    } 
  }
  
  public void setBaselineAligned(boolean paramBoolean) {
    this.mBaselineAligned = paramBoolean;
  }
  
  public void setBaselineAlignedChildIndex(int paramInt) {
    if (paramInt >= 0 && paramInt < getChildCount()) {
      this.mBaselineAlignedChildIndex = paramInt;
      return;
    } 
    throw new IllegalArgumentException("base aligned child index out of range (0, " + getChildCount() + ")");
  }
  
  public void setDividerDrawable(Drawable paramDrawable) {
    if (paramDrawable == this.mDivider)
      return; 
    this.mDivider = paramDrawable;
    boolean bool = false;
    if (paramDrawable != null) {
      this.mDividerWidth = paramDrawable.getIntrinsicWidth();
      this.mDividerHeight = paramDrawable.getIntrinsicHeight();
    } else {
      this.mDividerWidth = 0;
      this.mDividerHeight = 0;
    } 
    if (paramDrawable == null)
      bool = true; 
    setWillNotDraw(bool);
    requestLayout();
  }
  
  public void setDividerPadding(int paramInt) {
    this.mDividerPadding = paramInt;
  }
  
  public void setGravity(int paramInt) {
    if (this.mGravity != paramInt) {
      int i = paramInt;
      if ((0x800007 & paramInt) == 0)
        i = paramInt | 0x800003; 
      paramInt = i;
      if ((i & 0x70) == 0)
        paramInt = i | 0x30; 
      this.mGravity = paramInt;
      requestLayout();
    } 
  }
  
  public void setHorizontalGravity(int paramInt) {
    int i = paramInt & 0x800007;
    paramInt = this.mGravity;
    if ((0x800007 & paramInt) != i) {
      this.mGravity = 0xFF7FFFF8 & paramInt | i;
      requestLayout();
    } 
  }
  
  public void setMeasureWithLargestChildEnabled(boolean paramBoolean) {
    this.mUseLargestChild = paramBoolean;
  }
  
  public void setOrientation(int paramInt) {
    if (this.mOrientation != paramInt) {
      this.mOrientation = paramInt;
      requestLayout();
    } 
  }
  
  public void setShowDividers(int paramInt) {
    if (paramInt != this.mShowDividers)
      requestLayout(); 
    this.mShowDividers = paramInt;
  }
  
  public void setVerticalGravity(int paramInt) {
    int i = paramInt & 0x70;
    paramInt = this.mGravity;
    if ((paramInt & 0x70) != i) {
      this.mGravity = paramInt & 0xFFFFFF8F | i;
      requestLayout();
    } 
  }
  
  public void setWeightSum(float paramFloat) {
    this.mWeightSum = Math.max(0.0F, paramFloat);
  }
  
  public boolean shouldDelayChildPressedState() {
    return false;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DividerMode {}
  
  public static class LayoutParams extends LinearLayout.LayoutParams {
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(int param1Int1, int param1Int2, float param1Float) {
      super(param1Int1, param1Int2, param1Float);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface OrientationMode {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\LinearLayoutCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */