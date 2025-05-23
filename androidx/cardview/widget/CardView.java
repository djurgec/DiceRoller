package androidx.cardview.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.cardview.R;

public class CardView extends FrameLayout {
  private static final int[] COLOR_BACKGROUND_ATTR = new int[] { 16842801 };
  
  private static final CardViewImpl IMPL;
  
  private final CardViewDelegate mCardViewDelegate;
  
  private boolean mCompatPadding;
  
  final Rect mContentPadding;
  
  private boolean mPreventCornerOverlap;
  
  final Rect mShadowBounds;
  
  int mUserSetMinHeight;
  
  int mUserSetMinWidth;
  
  static {
    if (Build.VERSION.SDK_INT >= 21) {
      IMPL = new CardViewApi21Impl();
    } else if (Build.VERSION.SDK_INT >= 17) {
      IMPL = new CardViewApi17Impl();
    } else {
      IMPL = new CardViewBaseImpl();
    } 
    IMPL.initStatic();
  }
  
  public CardView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public CardView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.cardViewStyle);
  }
  
  public CardView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    ColorStateList colorStateList;
    Rect rect = new Rect();
    this.mContentPadding = rect;
    this.mShadowBounds = new Rect();
    CardViewDelegate cardViewDelegate = new CardViewDelegate() {
        private Drawable mCardBackground;
        
        final CardView this$0;
        
        public Drawable getCardBackground() {
          return this.mCardBackground;
        }
        
        public View getCardView() {
          return (View)CardView.this;
        }
        
        public boolean getPreventCornerOverlap() {
          return CardView.this.getPreventCornerOverlap();
        }
        
        public boolean getUseCompatPadding() {
          return CardView.this.getUseCompatPadding();
        }
        
        public void setCardBackground(Drawable param1Drawable) {
          this.mCardBackground = param1Drawable;
          CardView.this.setBackgroundDrawable(param1Drawable);
        }
        
        public void setMinWidthHeightInternal(int param1Int1, int param1Int2) {
          if (param1Int1 > CardView.this.mUserSetMinWidth)
            CardView.this.setMinimumWidth(param1Int1); 
          if (param1Int2 > CardView.this.mUserSetMinHeight)
            CardView.this.setMinimumHeight(param1Int2); 
        }
        
        public void setShadowPadding(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
          CardView.this.mShadowBounds.set(param1Int1, param1Int2, param1Int3, param1Int4);
          CardView cardView = CardView.this;
          cardView.setPadding(cardView.mContentPadding.left + param1Int1, CardView.this.mContentPadding.top + param1Int2, CardView.this.mContentPadding.right + param1Int3, CardView.this.mContentPadding.bottom + param1Int4);
        }
      };
    this.mCardViewDelegate = cardViewDelegate;
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CardView, paramInt, R.style.CardView);
    if (typedArray.hasValue(R.styleable.CardView_cardBackgroundColor)) {
      colorStateList = typedArray.getColorStateList(R.styleable.CardView_cardBackgroundColor);
    } else {
      TypedArray typedArray1 = getContext().obtainStyledAttributes(COLOR_BACKGROUND_ATTR);
      paramInt = typedArray1.getColor(0, 0);
      typedArray1.recycle();
      float[] arrayOfFloat = new float[3];
      Color.colorToHSV(paramInt, arrayOfFloat);
      if (arrayOfFloat[2] > 0.5F) {
        paramInt = getResources().getColor(R.color.cardview_light_background);
      } else {
        paramInt = getResources().getColor(R.color.cardview_dark_background);
      } 
      colorStateList = ColorStateList.valueOf(paramInt);
    } 
    float f3 = typedArray.getDimension(R.styleable.CardView_cardCornerRadius, 0.0F);
    float f2 = typedArray.getDimension(R.styleable.CardView_cardElevation, 0.0F);
    float f1 = typedArray.getDimension(R.styleable.CardView_cardMaxElevation, 0.0F);
    this.mCompatPadding = typedArray.getBoolean(R.styleable.CardView_cardUseCompatPadding, false);
    this.mPreventCornerOverlap = typedArray.getBoolean(R.styleable.CardView_cardPreventCornerOverlap, true);
    paramInt = typedArray.getDimensionPixelSize(R.styleable.CardView_contentPadding, 0);
    rect.left = typedArray.getDimensionPixelSize(R.styleable.CardView_contentPaddingLeft, paramInt);
    rect.top = typedArray.getDimensionPixelSize(R.styleable.CardView_contentPaddingTop, paramInt);
    rect.right = typedArray.getDimensionPixelSize(R.styleable.CardView_contentPaddingRight, paramInt);
    rect.bottom = typedArray.getDimensionPixelSize(R.styleable.CardView_contentPaddingBottom, paramInt);
    if (f2 > f1)
      f1 = f2; 
    this.mUserSetMinWidth = typedArray.getDimensionPixelSize(R.styleable.CardView_android_minWidth, 0);
    this.mUserSetMinHeight = typedArray.getDimensionPixelSize(R.styleable.CardView_android_minHeight, 0);
    typedArray.recycle();
    IMPL.initialize(cardViewDelegate, paramContext, colorStateList, f3, f2, f1);
  }
  
  public ColorStateList getCardBackgroundColor() {
    return IMPL.getBackgroundColor(this.mCardViewDelegate);
  }
  
  public float getCardElevation() {
    return IMPL.getElevation(this.mCardViewDelegate);
  }
  
  public int getContentPaddingBottom() {
    return this.mContentPadding.bottom;
  }
  
  public int getContentPaddingLeft() {
    return this.mContentPadding.left;
  }
  
  public int getContentPaddingRight() {
    return this.mContentPadding.right;
  }
  
  public int getContentPaddingTop() {
    return this.mContentPadding.top;
  }
  
  public float getMaxCardElevation() {
    return IMPL.getMaxElevation(this.mCardViewDelegate);
  }
  
  public boolean getPreventCornerOverlap() {
    return this.mPreventCornerOverlap;
  }
  
  public float getRadius() {
    return IMPL.getRadius(this.mCardViewDelegate);
  }
  
  public boolean getUseCompatPadding() {
    return this.mCompatPadding;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    CardViewImpl cardViewImpl = IMPL;
    if (!(cardViewImpl instanceof CardViewApi21Impl)) {
      int j;
      int i = View.MeasureSpec.getMode(paramInt1);
      switch (i) {
        case 1073741824:
        case -2147483648:
          j = (int)Math.ceil(cardViewImpl.getMinWidth(this.mCardViewDelegate));
          paramInt1 = View.MeasureSpec.makeMeasureSpec(Math.max(j, View.MeasureSpec.getSize(paramInt1)), i);
          break;
      } 
      i = View.MeasureSpec.getMode(paramInt2);
      switch (i) {
        case 1073741824:
        case -2147483648:
          j = (int)Math.ceil(cardViewImpl.getMinHeight(this.mCardViewDelegate));
          paramInt2 = View.MeasureSpec.makeMeasureSpec(Math.max(j, View.MeasureSpec.getSize(paramInt2)), i);
          break;
      } 
      super.onMeasure(paramInt1, paramInt2);
    } else {
      super.onMeasure(paramInt1, paramInt2);
    } 
  }
  
  public void setCardBackgroundColor(int paramInt) {
    IMPL.setBackgroundColor(this.mCardViewDelegate, ColorStateList.valueOf(paramInt));
  }
  
  public void setCardBackgroundColor(ColorStateList paramColorStateList) {
    IMPL.setBackgroundColor(this.mCardViewDelegate, paramColorStateList);
  }
  
  public void setCardElevation(float paramFloat) {
    IMPL.setElevation(this.mCardViewDelegate, paramFloat);
  }
  
  public void setContentPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mContentPadding.set(paramInt1, paramInt2, paramInt3, paramInt4);
    IMPL.updatePadding(this.mCardViewDelegate);
  }
  
  public void setMaxCardElevation(float paramFloat) {
    IMPL.setMaxElevation(this.mCardViewDelegate, paramFloat);
  }
  
  public void setMinimumHeight(int paramInt) {
    this.mUserSetMinHeight = paramInt;
    super.setMinimumHeight(paramInt);
  }
  
  public void setMinimumWidth(int paramInt) {
    this.mUserSetMinWidth = paramInt;
    super.setMinimumWidth(paramInt);
  }
  
  public void setPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {}
  
  public void setPaddingRelative(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {}
  
  public void setPreventCornerOverlap(boolean paramBoolean) {
    if (paramBoolean != this.mPreventCornerOverlap) {
      this.mPreventCornerOverlap = paramBoolean;
      IMPL.onPreventCornerOverlapChanged(this.mCardViewDelegate);
    } 
  }
  
  public void setRadius(float paramFloat) {
    IMPL.setRadius(this.mCardViewDelegate, paramFloat);
  }
  
  public void setUseCompatPadding(boolean paramBoolean) {
    if (this.mCompatPadding != paramBoolean) {
      this.mCompatPadding = paramBoolean;
      IMPL.onCompatPaddingChanged(this.mCardViewDelegate);
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\cardview\widget\CardView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */