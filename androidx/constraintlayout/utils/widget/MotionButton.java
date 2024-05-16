package androidx.constraintlayout.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.R;

public class MotionButton extends AppCompatButton {
  private Path mPath;
  
  RectF mRect;
  
  private float mRound = Float.NaN;
  
  private float mRoundPercent = 0.0F;
  
  ViewOutlineProvider mViewOutlineProvider;
  
  public MotionButton(Context paramContext) {
    super(paramContext);
    init(paramContext, (AttributeSet)null);
  }
  
  public MotionButton(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramContext, paramAttributeSet);
  }
  
  public MotionButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext, paramAttributeSet);
  }
  
  private void init(Context paramContext, AttributeSet paramAttributeSet) {
    setPadding(0, 0, 0, 0);
    if (paramAttributeSet != null) {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.ImageFilterView);
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        if (j == R.styleable.ImageFilterView_round) {
          if (Build.VERSION.SDK_INT >= 21)
            setRound(typedArray.getDimension(j, 0.0F)); 
        } else if (j == R.styleable.ImageFilterView_roundPercent && Build.VERSION.SDK_INT >= 21) {
          setRoundPercent(typedArray.getFloat(j, 0.0F));
        } 
      } 
      typedArray.recycle();
    } 
  }
  
  public void draw(Canvas paramCanvas) {
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (Build.VERSION.SDK_INT < 21) {
      bool1 = bool2;
      if (this.mRound != 0.0F) {
        bool1 = bool2;
        if (this.mPath != null) {
          bool1 = true;
          paramCanvas.save();
          paramCanvas.clipPath(this.mPath);
        } 
      } 
    } 
    super.draw(paramCanvas);
    if (bool1)
      paramCanvas.restore(); 
  }
  
  public float getRound() {
    return this.mRound;
  }
  
  public float getRoundPercent() {
    return this.mRoundPercent;
  }
  
  public void setRound(float paramFloat) {
    boolean bool;
    if (Float.isNaN(paramFloat)) {
      this.mRound = paramFloat;
      paramFloat = this.mRoundPercent;
      this.mRoundPercent = -1.0F;
      setRoundPercent(paramFloat);
      return;
    } 
    if (this.mRound != paramFloat) {
      bool = true;
    } else {
      bool = false;
    } 
    this.mRound = paramFloat;
    if (paramFloat != 0.0F) {
      if (this.mPath == null)
        this.mPath = new Path(); 
      if (this.mRect == null)
        this.mRect = new RectF(); 
      if (Build.VERSION.SDK_INT >= 21) {
        if (this.mViewOutlineProvider == null) {
          ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
              final MotionButton this$0;
              
              public void getOutline(View param1View, Outline param1Outline) {
                param1Outline.setRoundRect(0, 0, MotionButton.this.getWidth(), MotionButton.this.getHeight(), MotionButton.this.mRound);
              }
            };
          this.mViewOutlineProvider = viewOutlineProvider;
          setOutlineProvider(viewOutlineProvider);
        } 
        setClipToOutline(true);
      } 
      int i = getWidth();
      int j = getHeight();
      this.mRect.set(0.0F, 0.0F, i, j);
      this.mPath.reset();
      Path path = this.mPath;
      RectF rectF = this.mRect;
      paramFloat = this.mRound;
      path.addRoundRect(rectF, paramFloat, paramFloat, Path.Direction.CW);
    } else if (Build.VERSION.SDK_INT >= 21) {
      setClipToOutline(false);
    } 
    if (bool && Build.VERSION.SDK_INT >= 21)
      invalidateOutline(); 
  }
  
  public void setRoundPercent(float paramFloat) {
    boolean bool;
    if (this.mRoundPercent != paramFloat) {
      bool = true;
    } else {
      bool = false;
    } 
    this.mRoundPercent = paramFloat;
    if (paramFloat != 0.0F) {
      if (this.mPath == null)
        this.mPath = new Path(); 
      if (this.mRect == null)
        this.mRect = new RectF(); 
      if (Build.VERSION.SDK_INT >= 21) {
        if (this.mViewOutlineProvider == null) {
          ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
              final MotionButton this$0;
              
              public void getOutline(View param1View, Outline param1Outline) {
                int i = MotionButton.this.getWidth();
                int j = MotionButton.this.getHeight();
                param1Outline.setRoundRect(0, 0, i, j, Math.min(i, j) * MotionButton.this.mRoundPercent / 2.0F);
              }
            };
          this.mViewOutlineProvider = viewOutlineProvider;
          setOutlineProvider(viewOutlineProvider);
        } 
        setClipToOutline(true);
      } 
      int j = getWidth();
      int i = getHeight();
      paramFloat = Math.min(j, i) * this.mRoundPercent / 2.0F;
      this.mRect.set(0.0F, 0.0F, j, i);
      this.mPath.reset();
      this.mPath.addRoundRect(this.mRect, paramFloat, paramFloat, Path.Direction.CW);
    } else if (Build.VERSION.SDK_INT >= 21) {
      setClipToOutline(false);
    } 
    if (bool && Build.VERSION.SDK_INT >= 21)
      invalidateOutline(); 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayou\\utils\widget\MotionButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */