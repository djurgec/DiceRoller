package androidx.viewpager.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.core.content.ContextCompat;

public class PagerTabStrip extends PagerTitleStrip {
  private static final int FULL_UNDERLINE_HEIGHT = 1;
  
  private static final int INDICATOR_HEIGHT = 3;
  
  private static final int MIN_PADDING_BOTTOM = 6;
  
  private static final int MIN_STRIP_HEIGHT = 32;
  
  private static final int MIN_TEXT_SPACING = 64;
  
  private static final int TAB_PADDING = 16;
  
  private static final int TAB_SPACING = 32;
  
  private static final String TAG = "PagerTabStrip";
  
  private boolean mDrawFullUnderline;
  
  private boolean mDrawFullUnderlineSet;
  
  private int mFullUnderlineHeight;
  
  private boolean mIgnoreTap;
  
  private int mIndicatorColor;
  
  private int mIndicatorHeight;
  
  private float mInitialMotionX;
  
  private float mInitialMotionY;
  
  private int mMinPaddingBottom;
  
  private int mMinStripHeight;
  
  private int mMinTextSpacing;
  
  private int mTabAlpha;
  
  private int mTabPadding;
  
  private final Paint mTabPaint;
  
  private final Rect mTempRect;
  
  private int mTouchSlop;
  
  public PagerTabStrip(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public PagerTabStrip(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    Paint paint = new Paint();
    this.mTabPaint = paint;
    this.mTempRect = new Rect();
    this.mTabAlpha = 255;
    this.mDrawFullUnderline = false;
    this.mDrawFullUnderlineSet = false;
    int i = this.mTextColor;
    this.mIndicatorColor = i;
    paint.setColor(i);
    float f = (paramContext.getResources().getDisplayMetrics()).density;
    this.mIndicatorHeight = (int)(3.0F * f + 0.5F);
    this.mMinPaddingBottom = (int)(6.0F * f + 0.5F);
    this.mMinTextSpacing = (int)(64.0F * f);
    this.mTabPadding = (int)(16.0F * f + 0.5F);
    this.mFullUnderlineHeight = (int)(1.0F * f + 0.5F);
    this.mMinStripHeight = (int)(32.0F * f + 0.5F);
    this.mTouchSlop = ViewConfiguration.get(paramContext).getScaledTouchSlop();
    setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
    setTextSpacing(getTextSpacing());
    setWillNotDraw(false);
    this.mPrevText.setFocusable(true);
    this.mPrevText.setOnClickListener(new View.OnClickListener() {
          final PagerTabStrip this$0;
          
          public void onClick(View param1View) {
            PagerTabStrip.this.mPager.setCurrentItem(PagerTabStrip.this.mPager.getCurrentItem() - 1);
          }
        });
    this.mNextText.setFocusable(true);
    this.mNextText.setOnClickListener(new View.OnClickListener() {
          final PagerTabStrip this$0;
          
          public void onClick(View param1View) {
            PagerTabStrip.this.mPager.setCurrentItem(PagerTabStrip.this.mPager.getCurrentItem() + 1);
          }
        });
    if (getBackground() == null)
      this.mDrawFullUnderline = true; 
  }
  
  public boolean getDrawFullUnderline() {
    return this.mDrawFullUnderline;
  }
  
  int getMinHeight() {
    return Math.max(super.getMinHeight(), this.mMinStripHeight);
  }
  
  public int getTabIndicatorColor() {
    return this.mIndicatorColor;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    int j = getHeight();
    int m = this.mCurrText.getLeft();
    int k = this.mTabPadding;
    int i = this.mCurrText.getRight();
    int n = this.mTabPadding;
    int i1 = this.mIndicatorHeight;
    this.mTabPaint.setColor(this.mTabAlpha << 24 | this.mIndicatorColor & 0xFFFFFF);
    paramCanvas.drawRect((m - k), (j - i1), (i + n), j, this.mTabPaint);
    if (this.mDrawFullUnderline) {
      this.mTabPaint.setColor(0xFF000000 | this.mIndicatorColor & 0xFFFFFF);
      paramCanvas.drawRect(getPaddingLeft(), (j - this.mFullUnderlineHeight), (getWidth() - getPaddingRight()), j, this.mTabPaint);
    } 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getAction();
    if (i != 0 && this.mIgnoreTap)
      return false; 
    float f1 = paramMotionEvent.getX();
    float f2 = paramMotionEvent.getY();
    switch (i) {
      default:
        return true;
      case 2:
        if (Math.abs(f1 - this.mInitialMotionX) > this.mTouchSlop || Math.abs(f2 - this.mInitialMotionY) > this.mTouchSlop)
          this.mIgnoreTap = true; 
      case 1:
        if (f1 < (this.mCurrText.getLeft() - this.mTabPadding)) {
          this.mPager.setCurrentItem(this.mPager.getCurrentItem() - 1);
        } else if (f1 > (this.mCurrText.getRight() + this.mTabPadding)) {
          this.mPager.setCurrentItem(this.mPager.getCurrentItem() + 1);
        } 
      case 0:
        break;
    } 
    this.mInitialMotionX = f1;
    this.mInitialMotionY = f2;
    this.mIgnoreTap = false;
  }
  
  public void setBackgroundColor(int paramInt) {
    super.setBackgroundColor(paramInt);
    if (!this.mDrawFullUnderlineSet) {
      boolean bool;
      if ((0xFF000000 & paramInt) == 0) {
        bool = true;
      } else {
        bool = false;
      } 
      this.mDrawFullUnderline = bool;
    } 
  }
  
  public void setBackgroundDrawable(Drawable paramDrawable) {
    super.setBackgroundDrawable(paramDrawable);
    if (!this.mDrawFullUnderlineSet) {
      boolean bool;
      if (paramDrawable == null) {
        bool = true;
      } else {
        bool = false;
      } 
      this.mDrawFullUnderline = bool;
    } 
  }
  
  public void setBackgroundResource(int paramInt) {
    super.setBackgroundResource(paramInt);
    if (!this.mDrawFullUnderlineSet) {
      boolean bool;
      if (paramInt == 0) {
        bool = true;
      } else {
        bool = false;
      } 
      this.mDrawFullUnderline = bool;
    } 
  }
  
  public void setDrawFullUnderline(boolean paramBoolean) {
    this.mDrawFullUnderline = paramBoolean;
    this.mDrawFullUnderlineSet = true;
    invalidate();
  }
  
  public void setPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = paramInt4;
    if (paramInt4 < this.mMinPaddingBottom)
      i = this.mMinPaddingBottom; 
    super.setPadding(paramInt1, paramInt2, paramInt3, i);
  }
  
  public void setTabIndicatorColor(int paramInt) {
    this.mIndicatorColor = paramInt;
    this.mTabPaint.setColor(paramInt);
    invalidate();
  }
  
  public void setTabIndicatorColorResource(int paramInt) {
    setTabIndicatorColor(ContextCompat.getColor(getContext(), paramInt));
  }
  
  public void setTextSpacing(int paramInt) {
    int i = paramInt;
    if (paramInt < this.mMinTextSpacing)
      i = this.mMinTextSpacing; 
    super.setTextSpacing(i);
  }
  
  void updateTextPositions(int paramInt, float paramFloat, boolean paramBoolean) {
    Rect rect = this.mTempRect;
    int k = getHeight();
    int i = this.mCurrText.getLeft();
    int i1 = this.mTabPadding;
    int m = this.mCurrText.getRight();
    int j = this.mTabPadding;
    int n = k - this.mIndicatorHeight;
    rect.set(i - i1, n, m + j, k);
    super.updateTextPositions(paramInt, paramFloat, paramBoolean);
    this.mTabAlpha = (int)(Math.abs(paramFloat - 0.5F) * 2.0F * 255.0F);
    rect.union(this.mCurrText.getLeft() - this.mTabPadding, n, this.mCurrText.getRight() + this.mTabPadding, k);
    invalidate(rect);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\viewpager\widget\PagerTabStrip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */