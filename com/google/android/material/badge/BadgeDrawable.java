package com.google.android.material.badge;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.drawable.DrawableUtils;
import com.google.android.material.internal.TextDrawableHelper;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.shape.MaterialShapeDrawable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.text.NumberFormat;

public class BadgeDrawable extends Drawable implements TextDrawableHelper.TextDrawableDelegate {
  private static final int BADGE_NUMBER_NONE = -1;
  
  public static final int BOTTOM_END = 8388693;
  
  public static final int BOTTOM_START = 8388691;
  
  static final String DEFAULT_EXCEED_MAX_BADGE_NUMBER_SUFFIX = "+";
  
  private static final int DEFAULT_MAX_BADGE_CHARACTER_COUNT = 4;
  
  private static final int DEFAULT_STYLE = R.style.Widget_MaterialComponents_Badge;
  
  private static final int DEFAULT_THEME_ATTR = R.attr.badgeStyle;
  
  private static final int MAX_CIRCULAR_BADGE_NUMBER_COUNT = 9;
  
  public static final int TOP_END = 8388661;
  
  public static final int TOP_START = 8388659;
  
  private WeakReference<View> anchorViewRef;
  
  private final Rect badgeBounds;
  
  private float badgeCenterX;
  
  private float badgeCenterY;
  
  private final float badgeRadius;
  
  private final float badgeWidePadding;
  
  private final float badgeWithTextRadius;
  
  private final WeakReference<Context> contextRef;
  
  private float cornerRadius;
  
  private WeakReference<FrameLayout> customBadgeParentRef;
  
  private float halfBadgeHeight;
  
  private float halfBadgeWidth;
  
  private int maxBadgeNumber;
  
  private final SavedState savedState;
  
  private final MaterialShapeDrawable shapeDrawable;
  
  private final TextDrawableHelper textDrawableHelper;
  
  private BadgeDrawable(Context paramContext) {
    this.contextRef = new WeakReference<>(paramContext);
    ThemeEnforcement.checkMaterialTheme(paramContext);
    Resources resources = paramContext.getResources();
    this.badgeBounds = new Rect();
    this.shapeDrawable = new MaterialShapeDrawable();
    this.badgeRadius = resources.getDimensionPixelSize(R.dimen.mtrl_badge_radius);
    this.badgeWidePadding = resources.getDimensionPixelSize(R.dimen.mtrl_badge_long_text_horizontal_padding);
    this.badgeWithTextRadius = resources.getDimensionPixelSize(R.dimen.mtrl_badge_with_text_radius);
    TextDrawableHelper textDrawableHelper = new TextDrawableHelper(this);
    this.textDrawableHelper = textDrawableHelper;
    textDrawableHelper.getTextPaint().setTextAlign(Paint.Align.CENTER);
    this.savedState = new SavedState(paramContext);
    setTextAppearanceResource(R.style.TextAppearance_MaterialComponents_Badge);
  }
  
  private void calculateCenterAndBounds(Context paramContext, Rect paramRect, View paramView) {
    float f;
    int i = this.savedState.verticalOffset + this.savedState.additionalVerticalOffset;
    switch (this.savedState.badgeGravity) {
      default:
        this.badgeCenterY = (paramRect.top + i);
        break;
      case 8388691:
      case 8388693:
        this.badgeCenterY = (paramRect.bottom - i);
        break;
    } 
    if (getNumber() <= 9) {
      float f1;
      if (!hasNumber()) {
        f1 = this.badgeRadius;
      } else {
        f1 = this.badgeWithTextRadius;
      } 
      this.cornerRadius = f1;
      this.halfBadgeHeight = f1;
      this.halfBadgeWidth = f1;
    } else {
      float f1 = this.badgeWithTextRadius;
      this.cornerRadius = f1;
      this.halfBadgeHeight = f1;
      String str = getBadgeText();
      this.halfBadgeWidth = this.textDrawableHelper.getTextWidth(str) / 2.0F + this.badgeWidePadding;
    } 
    Resources resources = paramContext.getResources();
    if (hasNumber()) {
      i = R.dimen.mtrl_badge_text_horizontal_edge_offset;
    } else {
      i = R.dimen.mtrl_badge_horizontal_edge_offset;
    } 
    i = resources.getDimensionPixelSize(i);
    int j = this.savedState.horizontalOffset + this.savedState.additionalHorizontalOffset;
    switch (this.savedState.badgeGravity) {
      default:
        if (ViewCompat.getLayoutDirection(paramView) == 0) {
          float f1 = paramRect.right + this.halfBadgeWidth - i - j;
          break;
        } 
        f = paramRect.left - this.halfBadgeWidth + i + j;
        this.badgeCenterX = f;
        return;
      case 8388659:
      case 8388691:
        if (ViewCompat.getLayoutDirection(paramView) == 0) {
          f = paramRect.left - this.halfBadgeWidth + i + j;
        } else {
          f = paramRect.right + this.halfBadgeWidth - i - j;
        } 
        this.badgeCenterX = f;
        return;
    } 
    this.badgeCenterX = f;
  }
  
  public static BadgeDrawable create(Context paramContext) {
    return createFromAttributes(paramContext, (AttributeSet)null, DEFAULT_THEME_ATTR, DEFAULT_STYLE);
  }
  
  private static BadgeDrawable createFromAttributes(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    BadgeDrawable badgeDrawable = new BadgeDrawable(paramContext);
    badgeDrawable.loadDefaultStateFromAttributes(paramContext, paramAttributeSet, paramInt1, paramInt2);
    return badgeDrawable;
  }
  
  public static BadgeDrawable createFromResource(Context paramContext, int paramInt) {
    AttributeSet attributeSet = DrawableUtils.parseDrawableXml(paramContext, paramInt, "badge");
    int i = attributeSet.getStyleAttribute();
    paramInt = i;
    if (i == 0)
      paramInt = DEFAULT_STYLE; 
    return createFromAttributes(paramContext, attributeSet, DEFAULT_THEME_ATTR, paramInt);
  }
  
  static BadgeDrawable createFromSavedState(Context paramContext, SavedState paramSavedState) {
    BadgeDrawable badgeDrawable = new BadgeDrawable(paramContext);
    badgeDrawable.restoreFromSavedState(paramSavedState);
    return badgeDrawable;
  }
  
  private void drawText(Canvas paramCanvas) {
    Rect rect = new Rect();
    String str = getBadgeText();
    this.textDrawableHelper.getTextPaint().getTextBounds(str, 0, str.length(), rect);
    paramCanvas.drawText(str, this.badgeCenterX, this.badgeCenterY + (rect.height() / 2), (Paint)this.textDrawableHelper.getTextPaint());
  }
  
  private String getBadgeText() {
    if (getNumber() <= this.maxBadgeNumber)
      return NumberFormat.getInstance().format(getNumber()); 
    Context context = this.contextRef.get();
    return (context == null) ? "" : context.getString(R.string.mtrl_exceed_max_badge_number_suffix, new Object[] { Integer.valueOf(this.maxBadgeNumber), "+" });
  }
  
  private void loadDefaultStateFromAttributes(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.Badge, paramInt1, paramInt2, new int[0]);
    setMaxCharacterCount(typedArray.getInt(R.styleable.Badge_maxCharacterCount, 4));
    if (typedArray.hasValue(R.styleable.Badge_number))
      setNumber(typedArray.getInt(R.styleable.Badge_number, 0)); 
    setBackgroundColor(readColorFromAttributes(paramContext, typedArray, R.styleable.Badge_backgroundColor));
    if (typedArray.hasValue(R.styleable.Badge_badgeTextColor))
      setBadgeTextColor(readColorFromAttributes(paramContext, typedArray, R.styleable.Badge_badgeTextColor)); 
    setBadgeGravity(typedArray.getInt(R.styleable.Badge_badgeGravity, 8388661));
    setHorizontalOffset(typedArray.getDimensionPixelOffset(R.styleable.Badge_horizontalOffset, 0));
    setVerticalOffset(typedArray.getDimensionPixelOffset(R.styleable.Badge_verticalOffset, 0));
    typedArray.recycle();
  }
  
  private static int readColorFromAttributes(Context paramContext, TypedArray paramTypedArray, int paramInt) {
    return MaterialResources.getColorStateList(paramContext, paramTypedArray, paramInt).getDefaultColor();
  }
  
  private void restoreFromSavedState(SavedState paramSavedState) {
    setMaxCharacterCount(paramSavedState.maxCharacterCount);
    if (paramSavedState.number != -1)
      setNumber(paramSavedState.number); 
    setBackgroundColor(paramSavedState.backgroundColor);
    setBadgeTextColor(paramSavedState.badgeTextColor);
    setBadgeGravity(paramSavedState.badgeGravity);
    setHorizontalOffset(paramSavedState.horizontalOffset);
    setVerticalOffset(paramSavedState.verticalOffset);
    setAdditionalHorizontalOffset(paramSavedState.additionalHorizontalOffset);
    setAdditionalVerticalOffset(paramSavedState.additionalVerticalOffset);
    setVisible(paramSavedState.isVisible);
  }
  
  private void setTextAppearance(TextAppearance paramTextAppearance) {
    if (this.textDrawableHelper.getTextAppearance() == paramTextAppearance)
      return; 
    Context context = this.contextRef.get();
    if (context == null)
      return; 
    this.textDrawableHelper.setTextAppearance(paramTextAppearance, context);
    updateCenterAndBounds();
  }
  
  private void setTextAppearanceResource(int paramInt) {
    Context context = this.contextRef.get();
    if (context == null)
      return; 
    setTextAppearance(new TextAppearance(context, paramInt));
  }
  
  private void tryWrapAnchorInCompatParent(final View anchorView) {
    ViewGroup viewGroup = (ViewGroup)anchorView.getParent();
    if (viewGroup == null || viewGroup.getId() != R.id.mtrl_anchor_parent) {
      WeakReference<FrameLayout> weakReference = this.customBadgeParentRef;
      if (weakReference == null || weakReference.get() != viewGroup) {
        updateAnchorParentToNotClip(anchorView);
        final FrameLayout frameLayout = new FrameLayout(anchorView.getContext());
        frameLayout.setId(R.id.mtrl_anchor_parent);
        frameLayout.setClipChildren(false);
        frameLayout.setClipToPadding(false);
        frameLayout.setLayoutParams(anchorView.getLayoutParams());
        frameLayout.setMinimumWidth(anchorView.getWidth());
        frameLayout.setMinimumHeight(anchorView.getHeight());
        int i = viewGroup.indexOfChild(anchorView);
        viewGroup.removeViewAt(i);
        anchorView.setLayoutParams((ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-1, -1));
        frameLayout.addView(anchorView);
        viewGroup.addView((View)frameLayout, i);
        this.customBadgeParentRef = new WeakReference<>(frameLayout);
        frameLayout.post(new Runnable() {
              final BadgeDrawable this$0;
              
              final View val$anchorView;
              
              final FrameLayout val$frameLayout;
              
              public void run() {
                BadgeDrawable.this.updateBadgeCoordinates(anchorView, frameLayout);
              }
            });
        return;
      } 
    } 
  }
  
  private static void updateAnchorParentToNotClip(View paramView) {
    ViewGroup viewGroup = (ViewGroup)paramView.getParent();
    viewGroup.setClipChildren(false);
    viewGroup.setClipToPadding(false);
  }
  
  private void updateCenterAndBounds() {
    Context context = this.contextRef.get();
    WeakReference<View> weakReference = this.anchorViewRef;
    FrameLayout frameLayout = null;
    if (weakReference != null) {
      View view = weakReference.get();
    } else {
      weakReference = null;
    } 
    if (context == null || weakReference == null)
      return; 
    Rect rect2 = new Rect();
    rect2.set(this.badgeBounds);
    Rect rect1 = new Rect();
    weakReference.getDrawingRect(rect1);
    WeakReference<FrameLayout> weakReference1 = this.customBadgeParentRef;
    if (weakReference1 != null)
      frameLayout = weakReference1.get(); 
    if (frameLayout != null || BadgeUtils.USE_COMPAT_PARENT) {
      ViewGroup viewGroup;
      if (frameLayout == null)
        viewGroup = (ViewGroup)weakReference.getParent(); 
      viewGroup.offsetDescendantRectToMyCoords((View)weakReference, rect1);
    } 
    calculateCenterAndBounds(context, rect1, (View)weakReference);
    BadgeUtils.updateBadgeBounds(this.badgeBounds, this.badgeCenterX, this.badgeCenterY, this.halfBadgeWidth, this.halfBadgeHeight);
    this.shapeDrawable.setCornerSize(this.cornerRadius);
    if (!rect2.equals(this.badgeBounds))
      this.shapeDrawable.setBounds(this.badgeBounds); 
  }
  
  private void updateMaxBadgeNumber() {
    this.maxBadgeNumber = (int)Math.pow(10.0D, getMaxCharacterCount() - 1.0D) - 1;
  }
  
  public void clearNumber() {
    SavedState.access$202(this.savedState, -1);
    invalidateSelf();
  }
  
  public void draw(Canvas paramCanvas) {
    if (getBounds().isEmpty() || getAlpha() == 0 || !isVisible())
      return; 
    this.shapeDrawable.draw(paramCanvas);
    if (hasNumber())
      drawText(paramCanvas); 
  }
  
  int getAdditionalHorizontalOffset() {
    return this.savedState.additionalHorizontalOffset;
  }
  
  int getAdditionalVerticalOffset() {
    return this.savedState.additionalVerticalOffset;
  }
  
  public int getAlpha() {
    return this.savedState.alpha;
  }
  
  public int getBackgroundColor() {
    return this.shapeDrawable.getFillColor().getDefaultColor();
  }
  
  public int getBadgeGravity() {
    return this.savedState.badgeGravity;
  }
  
  public int getBadgeTextColor() {
    return this.textDrawableHelper.getTextPaint().getColor();
  }
  
  public CharSequence getContentDescription() {
    if (!isVisible())
      return null; 
    if (hasNumber()) {
      if (this.savedState.contentDescriptionQuantityStrings > 0) {
        Context context = this.contextRef.get();
        return (context == null) ? null : ((getNumber() <= this.maxBadgeNumber) ? context.getResources().getQuantityString(this.savedState.contentDescriptionQuantityStrings, getNumber(), new Object[] { Integer.valueOf(getNumber()) }) : context.getString(this.savedState.contentDescriptionExceedsMaxBadgeNumberRes, new Object[] { Integer.valueOf(this.maxBadgeNumber) }));
      } 
      return null;
    } 
    return this.savedState.contentDescriptionNumberless;
  }
  
  public FrameLayout getCustomBadgeParent() {
    WeakReference<FrameLayout> weakReference = this.customBadgeParentRef;
    if (weakReference != null) {
      FrameLayout frameLayout = weakReference.get();
    } else {
      weakReference = null;
    } 
    return (FrameLayout)weakReference;
  }
  
  public int getHorizontalOffset() {
    return this.savedState.horizontalOffset;
  }
  
  public int getIntrinsicHeight() {
    return this.badgeBounds.height();
  }
  
  public int getIntrinsicWidth() {
    return this.badgeBounds.width();
  }
  
  public int getMaxCharacterCount() {
    return this.savedState.maxCharacterCount;
  }
  
  public int getNumber() {
    return !hasNumber() ? 0 : this.savedState.number;
  }
  
  public int getOpacity() {
    return -3;
  }
  
  public SavedState getSavedState() {
    return this.savedState;
  }
  
  public int getVerticalOffset() {
    return this.savedState.verticalOffset;
  }
  
  public boolean hasNumber() {
    boolean bool;
    if (this.savedState.number != -1) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isStateful() {
    return false;
  }
  
  public boolean onStateChange(int[] paramArrayOfint) {
    return super.onStateChange(paramArrayOfint);
  }
  
  public void onTextSizeChange() {
    invalidateSelf();
  }
  
  void setAdditionalHorizontalOffset(int paramInt) {
    SavedState.access$802(this.savedState, paramInt);
    updateCenterAndBounds();
  }
  
  void setAdditionalVerticalOffset(int paramInt) {
    SavedState.access$902(this.savedState, paramInt);
    updateCenterAndBounds();
  }
  
  public void setAlpha(int paramInt) {
    SavedState.access$1002(this.savedState, paramInt);
    this.textDrawableHelper.getTextPaint().setAlpha(paramInt);
    invalidateSelf();
  }
  
  public void setBackgroundColor(int paramInt) {
    SavedState.access$302(this.savedState, paramInt);
    ColorStateList colorStateList = ColorStateList.valueOf(paramInt);
    if (this.shapeDrawable.getFillColor() != colorStateList) {
      this.shapeDrawable.setFillColor(colorStateList);
      invalidateSelf();
    } 
  }
  
  public void setBadgeGravity(int paramInt) {
    if (this.savedState.badgeGravity != paramInt) {
      SavedState.access$502(this.savedState, paramInt);
      WeakReference<View> weakReference = this.anchorViewRef;
      if (weakReference != null && weakReference.get() != null) {
        View view = this.anchorViewRef.get();
        WeakReference<FrameLayout> weakReference1 = this.customBadgeParentRef;
        if (weakReference1 != null) {
          FrameLayout frameLayout = weakReference1.get();
        } else {
          weakReference1 = null;
        } 
        updateBadgeCoordinates(view, (FrameLayout)weakReference1);
      } 
    } 
  }
  
  public void setBadgeTextColor(int paramInt) {
    SavedState.access$402(this.savedState, paramInt);
    if (this.textDrawableHelper.getTextPaint().getColor() != paramInt) {
      this.textDrawableHelper.getTextPaint().setColor(paramInt);
      invalidateSelf();
    } 
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {}
  
  public void setContentDescriptionExceedsMaxBadgeNumberStringResource(int paramInt) {
    SavedState.access$1302(this.savedState, paramInt);
  }
  
  public void setContentDescriptionNumberless(CharSequence paramCharSequence) {
    SavedState.access$1102(this.savedState, paramCharSequence);
  }
  
  public void setContentDescriptionQuantityStringsResource(int paramInt) {
    SavedState.access$1202(this.savedState, paramInt);
  }
  
  public void setHorizontalOffset(int paramInt) {
    SavedState.access$602(this.savedState, paramInt);
    updateCenterAndBounds();
  }
  
  public void setMaxCharacterCount(int paramInt) {
    if (this.savedState.maxCharacterCount != paramInt) {
      SavedState.access$102(this.savedState, paramInt);
      updateMaxBadgeNumber();
      this.textDrawableHelper.setTextWidthDirty(true);
      updateCenterAndBounds();
      invalidateSelf();
    } 
  }
  
  public void setNumber(int paramInt) {
    paramInt = Math.max(0, paramInt);
    if (this.savedState.number != paramInt) {
      SavedState.access$202(this.savedState, paramInt);
      this.textDrawableHelper.setTextWidthDirty(true);
      updateCenterAndBounds();
      invalidateSelf();
    } 
  }
  
  public void setVerticalOffset(int paramInt) {
    SavedState.access$702(this.savedState, paramInt);
    updateCenterAndBounds();
  }
  
  public void setVisible(boolean paramBoolean) {
    setVisible(paramBoolean, false);
    SavedState.access$002(this.savedState, paramBoolean);
    if (BadgeUtils.USE_COMPAT_PARENT && getCustomBadgeParent() != null && !paramBoolean)
      ((ViewGroup)getCustomBadgeParent().getParent()).invalidate(); 
  }
  
  public void updateBadgeCoordinates(View paramView) {
    updateBadgeCoordinates(paramView, (FrameLayout)null);
  }
  
  @Deprecated
  public void updateBadgeCoordinates(View paramView, ViewGroup paramViewGroup) {
    if (paramViewGroup instanceof FrameLayout) {
      updateBadgeCoordinates(paramView, (FrameLayout)paramViewGroup);
      return;
    } 
    throw new IllegalArgumentException("customBadgeParent must be a FrameLayout");
  }
  
  public void updateBadgeCoordinates(View paramView, FrameLayout paramFrameLayout) {
    this.anchorViewRef = new WeakReference<>(paramView);
    if (BadgeUtils.USE_COMPAT_PARENT && paramFrameLayout == null) {
      tryWrapAnchorInCompatParent(paramView);
    } else {
      this.customBadgeParentRef = new WeakReference<>(paramFrameLayout);
    } 
    if (!BadgeUtils.USE_COMPAT_PARENT)
      updateAnchorParentToNotClip(paramView); 
    updateCenterAndBounds();
    invalidateSelf();
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface BadgeGravity {}
  
  public static final class SavedState implements Parcelable {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public BadgeDrawable.SavedState createFromParcel(Parcel param2Parcel) {
          return new BadgeDrawable.SavedState(param2Parcel);
        }
        
        public BadgeDrawable.SavedState[] newArray(int param2Int) {
          return new BadgeDrawable.SavedState[param2Int];
        }
      };
    
    private int additionalHorizontalOffset;
    
    private int additionalVerticalOffset;
    
    private int alpha;
    
    private int backgroundColor;
    
    private int badgeGravity;
    
    private int badgeTextColor;
    
    private int contentDescriptionExceedsMaxBadgeNumberRes;
    
    private CharSequence contentDescriptionNumberless;
    
    private int contentDescriptionQuantityStrings;
    
    private int horizontalOffset;
    
    private boolean isVisible;
    
    private int maxCharacterCount;
    
    private int number;
    
    private int verticalOffset;
    
    public SavedState(Context param1Context) {
      this.alpha = 255;
      this.number = -1;
      this.badgeTextColor = (new TextAppearance(param1Context, R.style.TextAppearance_MaterialComponents_Badge)).textColor.getDefaultColor();
      this.contentDescriptionNumberless = param1Context.getString(R.string.mtrl_badge_numberless_content_description);
      this.contentDescriptionQuantityStrings = R.plurals.mtrl_badge_content_description;
      this.contentDescriptionExceedsMaxBadgeNumberRes = R.string.mtrl_exceed_max_badge_number_content_description;
      this.isVisible = true;
    }
    
    protected SavedState(Parcel param1Parcel) {
      boolean bool;
      this.alpha = 255;
      this.number = -1;
      this.backgroundColor = param1Parcel.readInt();
      this.badgeTextColor = param1Parcel.readInt();
      this.alpha = param1Parcel.readInt();
      this.number = param1Parcel.readInt();
      this.maxCharacterCount = param1Parcel.readInt();
      this.contentDescriptionNumberless = param1Parcel.readString();
      this.contentDescriptionQuantityStrings = param1Parcel.readInt();
      this.badgeGravity = param1Parcel.readInt();
      this.horizontalOffset = param1Parcel.readInt();
      this.verticalOffset = param1Parcel.readInt();
      this.additionalHorizontalOffset = param1Parcel.readInt();
      this.additionalVerticalOffset = param1Parcel.readInt();
      if (param1Parcel.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      this.isVisible = bool;
    }
    
    public int describeContents() {
      return 0;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      param1Parcel.writeInt(this.backgroundColor);
      param1Parcel.writeInt(this.badgeTextColor);
      param1Parcel.writeInt(this.alpha);
      param1Parcel.writeInt(this.number);
      param1Parcel.writeInt(this.maxCharacterCount);
      param1Parcel.writeString(this.contentDescriptionNumberless.toString());
      param1Parcel.writeInt(this.contentDescriptionQuantityStrings);
      param1Parcel.writeInt(this.badgeGravity);
      param1Parcel.writeInt(this.horizontalOffset);
      param1Parcel.writeInt(this.verticalOffset);
      param1Parcel.writeInt(this.additionalHorizontalOffset);
      param1Parcel.writeInt(this.additionalVerticalOffset);
      param1Parcel.writeInt(this.isVisible);
    }
  }
  
  static final class null implements Parcelable.Creator<SavedState> {
    public BadgeDrawable.SavedState createFromParcel(Parcel param1Parcel) {
      return new BadgeDrawable.SavedState(param1Parcel);
    }
    
    public BadgeDrawable.SavedState[] newArray(int param1Int) {
      return new BadgeDrawable.SavedState[param1Int];
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\badge\BadgeDrawable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */