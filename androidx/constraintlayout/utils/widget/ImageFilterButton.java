package androidx.constraintlayout.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.R;

public class ImageFilterButton extends AppCompatImageButton {
  private Drawable mAltDrawable = null;
  
  private float mCrossfade = 0.0F;
  
  private Drawable mDrawable = null;
  
  private ImageFilterView.ImageMatrix mImageMatrix = new ImageFilterView.ImageMatrix();
  
  LayerDrawable mLayer;
  
  Drawable[] mLayers = new Drawable[2];
  
  private boolean mOverlay = true;
  
  private float mPanX = Float.NaN;
  
  private float mPanY = Float.NaN;
  
  private Path mPath;
  
  RectF mRect;
  
  private float mRotate = Float.NaN;
  
  private float mRound = Float.NaN;
  
  private float mRoundPercent = 0.0F;
  
  ViewOutlineProvider mViewOutlineProvider;
  
  private float mZoom = Float.NaN;
  
  public ImageFilterButton(Context paramContext) {
    super(paramContext);
    init(paramContext, (AttributeSet)null);
  }
  
  public ImageFilterButton(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramContext, paramAttributeSet);
  }
  
  public ImageFilterButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext, paramAttributeSet);
  }
  
  private void init(Context paramContext, AttributeSet paramAttributeSet) {
    setPadding(0, 0, 0, 0);
    if (paramAttributeSet != null) {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.ImageFilterView);
      int i = typedArray.getIndexCount();
      this.mAltDrawable = typedArray.getDrawable(R.styleable.ImageFilterView_altSrc);
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        if (j == R.styleable.ImageFilterView_crossfade) {
          this.mCrossfade = typedArray.getFloat(j, 0.0F);
        } else if (j == R.styleable.ImageFilterView_warmth) {
          setWarmth(typedArray.getFloat(j, 0.0F));
        } else if (j == R.styleable.ImageFilterView_saturation) {
          setSaturation(typedArray.getFloat(j, 0.0F));
        } else if (j == R.styleable.ImageFilterView_contrast) {
          setContrast(typedArray.getFloat(j, 0.0F));
        } else if (j == R.styleable.ImageFilterView_round) {
          if (Build.VERSION.SDK_INT >= 21)
            setRound(typedArray.getDimension(j, 0.0F)); 
        } else if (j == R.styleable.ImageFilterView_roundPercent) {
          if (Build.VERSION.SDK_INT >= 21)
            setRoundPercent(typedArray.getFloat(j, 0.0F)); 
        } else if (j == R.styleable.ImageFilterView_overlay) {
          setOverlay(typedArray.getBoolean(j, this.mOverlay));
        } else if (j == R.styleable.ImageFilterView_imagePanX) {
          setImagePanX(typedArray.getFloat(j, this.mPanX));
        } else if (j == R.styleable.ImageFilterView_imagePanY) {
          setImagePanY(typedArray.getFloat(j, this.mPanY));
        } else if (j == R.styleable.ImageFilterView_imageRotate) {
          setImageRotate(typedArray.getFloat(j, this.mRotate));
        } else if (j == R.styleable.ImageFilterView_imageZoom) {
          setImageZoom(typedArray.getFloat(j, this.mZoom));
        } 
      } 
      typedArray.recycle();
      Drawable drawable = getDrawable();
      this.mDrawable = drawable;
      if (this.mAltDrawable != null && drawable != null) {
        Drawable[] arrayOfDrawable = this.mLayers;
        Drawable drawable1 = getDrawable().mutate();
        this.mDrawable = drawable1;
        arrayOfDrawable[0] = drawable1;
        this.mLayers[1] = this.mAltDrawable.mutate();
        LayerDrawable layerDrawable = new LayerDrawable(this.mLayers);
        this.mLayer = layerDrawable;
        layerDrawable.getDrawable(1).setAlpha((int)(this.mCrossfade * 255.0F));
        if (!this.mOverlay)
          this.mLayer.getDrawable(0).setAlpha((int)((1.0F - this.mCrossfade) * 255.0F)); 
        super.setImageDrawable((Drawable)this.mLayer);
      } else {
        Drawable drawable1 = getDrawable();
        this.mDrawable = drawable1;
        if (drawable1 != null) {
          Drawable[] arrayOfDrawable = this.mLayers;
          drawable1 = drawable1.mutate();
          this.mDrawable = drawable1;
          arrayOfDrawable[0] = drawable1;
        } 
      } 
    } 
  }
  
  private void setMatrix() {
    float f1;
    float f2;
    float f5;
    if (Float.isNaN(this.mPanX) && Float.isNaN(this.mPanY) && Float.isNaN(this.mZoom) && Float.isNaN(this.mRotate))
      return; 
    boolean bool = Float.isNaN(this.mPanX);
    float f4 = 0.0F;
    if (bool) {
      f1 = 0.0F;
    } else {
      f1 = this.mPanX;
    } 
    if (Float.isNaN(this.mPanY)) {
      f2 = 0.0F;
    } else {
      f2 = this.mPanY;
    } 
    if (Float.isNaN(this.mZoom)) {
      f3 = 1.0F;
    } else {
      f3 = this.mZoom;
    } 
    if (!Float.isNaN(this.mRotate))
      f4 = this.mRotate; 
    Matrix matrix = new Matrix();
    matrix.reset();
    float f9 = getDrawable().getIntrinsicWidth();
    float f7 = getDrawable().getIntrinsicHeight();
    float f6 = getWidth();
    float f8 = getHeight();
    if (f9 * f8 < f7 * f6) {
      f5 = f6 / f9;
    } else {
      f5 = f8 / f7;
    } 
    float f3 = f5 * f3;
    matrix.postScale(f3, f3);
    matrix.postTranslate(((f6 - f3 * f9) * f1 + f6 - f3 * f9) * 0.5F, ((f8 - f3 * f7) * f2 + f8 - f3 * f7) * 0.5F);
    matrix.postRotate(f4, f6 / 2.0F, f8 / 2.0F);
    setImageMatrix(matrix);
    setScaleType(ImageView.ScaleType.MATRIX);
  }
  
  private void setOverlay(boolean paramBoolean) {
    this.mOverlay = paramBoolean;
  }
  
  private void updateViewMatrix() {
    if (Float.isNaN(this.mPanX) && Float.isNaN(this.mPanY) && Float.isNaN(this.mZoom) && Float.isNaN(this.mRotate)) {
      setScaleType(ImageView.ScaleType.FIT_CENTER);
      return;
    } 
    setMatrix();
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
  
  public float getContrast() {
    return this.mImageMatrix.mContrast;
  }
  
  public float getCrossfade() {
    return this.mCrossfade;
  }
  
  public float getImagePanX() {
    return this.mPanX;
  }
  
  public float getImagePanY() {
    return this.mPanY;
  }
  
  public float getImageRotate() {
    return this.mRotate;
  }
  
  public float getImageZoom() {
    return this.mZoom;
  }
  
  public float getRound() {
    return this.mRound;
  }
  
  public float getRoundPercent() {
    return this.mRoundPercent;
  }
  
  public float getSaturation() {
    return this.mImageMatrix.mSaturation;
  }
  
  public float getWarmth() {
    return this.mImageMatrix.mWarmth;
  }
  
  public void layout(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.layout(paramInt1, paramInt2, paramInt3, paramInt4);
    setMatrix();
  }
  
  public void setAltImageResource(int paramInt) {
    Drawable drawable = AppCompatResources.getDrawable(getContext(), paramInt).mutate();
    this.mAltDrawable = drawable;
    Drawable[] arrayOfDrawable = this.mLayers;
    arrayOfDrawable[0] = this.mDrawable;
    arrayOfDrawable[1] = drawable;
    LayerDrawable layerDrawable = new LayerDrawable(this.mLayers);
    this.mLayer = layerDrawable;
    super.setImageDrawable((Drawable)layerDrawable);
    setCrossfade(this.mCrossfade);
  }
  
  public void setBrightness(float paramFloat) {
    this.mImageMatrix.mBrightness = paramFloat;
    this.mImageMatrix.updateMatrix((ImageView)this);
  }
  
  public void setContrast(float paramFloat) {
    this.mImageMatrix.mContrast = paramFloat;
    this.mImageMatrix.updateMatrix((ImageView)this);
  }
  
  public void setCrossfade(float paramFloat) {
    this.mCrossfade = paramFloat;
    if (this.mLayers != null) {
      if (!this.mOverlay)
        this.mLayer.getDrawable(0).setAlpha((int)((1.0F - this.mCrossfade) * 255.0F)); 
      this.mLayer.getDrawable(1).setAlpha((int)(this.mCrossfade * 255.0F));
      super.setImageDrawable((Drawable)this.mLayer);
    } 
  }
  
  public void setImageDrawable(Drawable paramDrawable) {
    LayerDrawable layerDrawable;
    if (this.mAltDrawable != null && paramDrawable != null) {
      paramDrawable = paramDrawable.mutate();
      this.mDrawable = paramDrawable;
      Drawable[] arrayOfDrawable = this.mLayers;
      arrayOfDrawable[0] = paramDrawable;
      arrayOfDrawable[1] = this.mAltDrawable;
      layerDrawable = new LayerDrawable(this.mLayers);
      this.mLayer = layerDrawable;
      super.setImageDrawable((Drawable)layerDrawable);
      setCrossfade(this.mCrossfade);
    } else {
      super.setImageDrawable((Drawable)layerDrawable);
    } 
  }
  
  public void setImagePanX(float paramFloat) {
    this.mPanX = paramFloat;
    updateViewMatrix();
  }
  
  public void setImagePanY(float paramFloat) {
    this.mPanY = paramFloat;
    updateViewMatrix();
  }
  
  public void setImageResource(int paramInt) {
    if (this.mAltDrawable != null) {
      Drawable drawable = AppCompatResources.getDrawable(getContext(), paramInt).mutate();
      this.mDrawable = drawable;
      Drawable[] arrayOfDrawable = this.mLayers;
      arrayOfDrawable[0] = drawable;
      arrayOfDrawable[1] = this.mAltDrawable;
      LayerDrawable layerDrawable = new LayerDrawable(this.mLayers);
      this.mLayer = layerDrawable;
      super.setImageDrawable((Drawable)layerDrawable);
      setCrossfade(this.mCrossfade);
    } else {
      super.setImageResource(paramInt);
    } 
  }
  
  public void setImageRotate(float paramFloat) {
    this.mRotate = paramFloat;
    updateViewMatrix();
  }
  
  public void setImageZoom(float paramFloat) {
    this.mZoom = paramFloat;
    updateViewMatrix();
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
              final ImageFilterButton this$0;
              
              public void getOutline(View param1View, Outline param1Outline) {
                param1Outline.setRoundRect(0, 0, ImageFilterButton.this.getWidth(), ImageFilterButton.this.getHeight(), ImageFilterButton.this.mRound);
              }
            };
          this.mViewOutlineProvider = viewOutlineProvider;
          setOutlineProvider(viewOutlineProvider);
        } 
        setClipToOutline(true);
      } 
      int j = getWidth();
      int i = getHeight();
      this.mRect.set(0.0F, 0.0F, j, i);
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
              final ImageFilterButton this$0;
              
              public void getOutline(View param1View, Outline param1Outline) {
                int i = ImageFilterButton.this.getWidth();
                int j = ImageFilterButton.this.getHeight();
                param1Outline.setRoundRect(0, 0, i, j, Math.min(i, j) * ImageFilterButton.this.mRoundPercent / 2.0F);
              }
            };
          this.mViewOutlineProvider = viewOutlineProvider;
          setOutlineProvider(viewOutlineProvider);
        } 
        setClipToOutline(true);
      } 
      int i = getWidth();
      int j = getHeight();
      paramFloat = Math.min(i, j) * this.mRoundPercent / 2.0F;
      this.mRect.set(0.0F, 0.0F, i, j);
      this.mPath.reset();
      this.mPath.addRoundRect(this.mRect, paramFloat, paramFloat, Path.Direction.CW);
    } else if (Build.VERSION.SDK_INT >= 21) {
      setClipToOutline(false);
    } 
    if (bool && Build.VERSION.SDK_INT >= 21)
      invalidateOutline(); 
  }
  
  public void setSaturation(float paramFloat) {
    this.mImageMatrix.mSaturation = paramFloat;
    this.mImageMatrix.updateMatrix((ImageView)this);
  }
  
  public void setWarmth(float paramFloat) {
    this.mImageMatrix.mWarmth = paramFloat;
    this.mImageMatrix.updateMatrix((ImageView)this);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayou\\utils\widget\ImageFilterButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */