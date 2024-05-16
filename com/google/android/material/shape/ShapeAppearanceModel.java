package com.google.android.material.shape;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import com.google.android.material.R;

public class ShapeAppearanceModel {
  public static final CornerSize PILL = new RelativeCornerSize(0.5F);
  
  EdgeTreatment bottomEdge = MaterialShapeUtils.createDefaultEdgeTreatment();
  
  CornerTreatment bottomLeftCorner = MaterialShapeUtils.createDefaultCornerTreatment();
  
  CornerSize bottomLeftCornerSize = new AbsoluteCornerSize(0.0F);
  
  CornerTreatment bottomRightCorner = MaterialShapeUtils.createDefaultCornerTreatment();
  
  CornerSize bottomRightCornerSize = new AbsoluteCornerSize(0.0F);
  
  EdgeTreatment leftEdge = MaterialShapeUtils.createDefaultEdgeTreatment();
  
  EdgeTreatment rightEdge = MaterialShapeUtils.createDefaultEdgeTreatment();
  
  EdgeTreatment topEdge = MaterialShapeUtils.createDefaultEdgeTreatment();
  
  CornerTreatment topLeftCorner = MaterialShapeUtils.createDefaultCornerTreatment();
  
  CornerSize topLeftCornerSize = new AbsoluteCornerSize(0.0F);
  
  CornerTreatment topRightCorner = MaterialShapeUtils.createDefaultCornerTreatment();
  
  CornerSize topRightCornerSize = new AbsoluteCornerSize(0.0F);
  
  public ShapeAppearanceModel() {}
  
  private ShapeAppearanceModel(Builder paramBuilder) {}
  
  public static Builder builder() {
    return new Builder();
  }
  
  public static Builder builder(Context paramContext, int paramInt1, int paramInt2) {
    return builder(paramContext, paramInt1, paramInt2, 0);
  }
  
  private static Builder builder(Context paramContext, int paramInt1, int paramInt2, int paramInt3) {
    return builder(paramContext, paramInt1, paramInt2, new AbsoluteCornerSize(paramInt3));
  }
  
  private static Builder builder(Context paramContext, int paramInt1, int paramInt2, CornerSize paramCornerSize) {
    ContextThemeWrapper contextThemeWrapper;
    Context context = paramContext;
    int i = paramInt1;
    if (paramInt2 != 0) {
      contextThemeWrapper = new ContextThemeWrapper(paramContext, paramInt1);
      i = paramInt2;
    } 
    TypedArray typedArray = contextThemeWrapper.obtainStyledAttributes(i, R.styleable.ShapeAppearance);
    try {
      int j = typedArray.getInt(R.styleable.ShapeAppearance_cornerFamily, 0);
      paramInt1 = typedArray.getInt(R.styleable.ShapeAppearance_cornerFamilyTopLeft, j);
      i = typedArray.getInt(R.styleable.ShapeAppearance_cornerFamilyTopRight, j);
      paramInt2 = typedArray.getInt(R.styleable.ShapeAppearance_cornerFamilyBottomRight, j);
      j = typedArray.getInt(R.styleable.ShapeAppearance_cornerFamilyBottomLeft, j);
      CornerSize cornerSize3 = getCornerSize(typedArray, R.styleable.ShapeAppearance_cornerSize, paramCornerSize);
      paramCornerSize = getCornerSize(typedArray, R.styleable.ShapeAppearance_cornerSizeTopLeft, cornerSize3);
      CornerSize cornerSize2 = getCornerSize(typedArray, R.styleable.ShapeAppearance_cornerSizeTopRight, cornerSize3);
      CornerSize cornerSize1 = getCornerSize(typedArray, R.styleable.ShapeAppearance_cornerSizeBottomRight, cornerSize3);
      cornerSize3 = getCornerSize(typedArray, R.styleable.ShapeAppearance_cornerSizeBottomLeft, cornerSize3);
      Builder builder = new Builder();
      this();
      return builder.setTopLeftCorner(paramInt1, paramCornerSize).setTopRightCorner(i, cornerSize2).setBottomRightCorner(paramInt2, cornerSize1).setBottomLeftCorner(j, cornerSize3);
    } finally {
      typedArray.recycle();
    } 
  }
  
  public static Builder builder(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    return builder(paramContext, paramAttributeSet, paramInt1, paramInt2, 0);
  }
  
  public static Builder builder(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2, int paramInt3) {
    return builder(paramContext, paramAttributeSet, paramInt1, paramInt2, new AbsoluteCornerSize(paramInt3));
  }
  
  public static Builder builder(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2, CornerSize paramCornerSize) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.MaterialShape, paramInt1, paramInt2);
    paramInt1 = typedArray.getResourceId(R.styleable.MaterialShape_shapeAppearance, 0);
    paramInt2 = typedArray.getResourceId(R.styleable.MaterialShape_shapeAppearanceOverlay, 0);
    typedArray.recycle();
    return builder(paramContext, paramInt1, paramInt2, paramCornerSize);
  }
  
  private static CornerSize getCornerSize(TypedArray paramTypedArray, int paramInt, CornerSize paramCornerSize) {
    TypedValue typedValue = paramTypedArray.peekValue(paramInt);
    return (typedValue == null) ? paramCornerSize : ((typedValue.type == 5) ? new AbsoluteCornerSize(TypedValue.complexToDimensionPixelSize(typedValue.data, paramTypedArray.getResources().getDisplayMetrics())) : ((typedValue.type == 6) ? new RelativeCornerSize(typedValue.getFraction(1.0F, 1.0F)) : paramCornerSize));
  }
  
  public EdgeTreatment getBottomEdge() {
    return this.bottomEdge;
  }
  
  public CornerTreatment getBottomLeftCorner() {
    return this.bottomLeftCorner;
  }
  
  public CornerSize getBottomLeftCornerSize() {
    return this.bottomLeftCornerSize;
  }
  
  public CornerTreatment getBottomRightCorner() {
    return this.bottomRightCorner;
  }
  
  public CornerSize getBottomRightCornerSize() {
    return this.bottomRightCornerSize;
  }
  
  public EdgeTreatment getLeftEdge() {
    return this.leftEdge;
  }
  
  public EdgeTreatment getRightEdge() {
    return this.rightEdge;
  }
  
  public EdgeTreatment getTopEdge() {
    return this.topEdge;
  }
  
  public CornerTreatment getTopLeftCorner() {
    return this.topLeftCorner;
  }
  
  public CornerSize getTopLeftCornerSize() {
    return this.topLeftCornerSize;
  }
  
  public CornerTreatment getTopRightCorner() {
    return this.topRightCorner;
  }
  
  public CornerSize getTopRightCornerSize() {
    return this.topRightCornerSize;
  }
  
  public boolean isRoundRect(RectF paramRectF) {
    boolean bool1;
    boolean bool2;
    boolean bool3;
    boolean bool = this.leftEdge.getClass().equals(EdgeTreatment.class);
    boolean bool4 = true;
    if (bool && this.rightEdge.getClass().equals(EdgeTreatment.class) && this.topEdge.getClass().equals(EdgeTreatment.class) && this.bottomEdge.getClass().equals(EdgeTreatment.class)) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    float f = this.topLeftCornerSize.getCornerSize(paramRectF);
    if (this.topRightCornerSize.getCornerSize(paramRectF) == f && this.bottomLeftCornerSize.getCornerSize(paramRectF) == f && this.bottomRightCornerSize.getCornerSize(paramRectF) == f) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (this.topRightCorner instanceof RoundedCornerTreatment && this.topLeftCorner instanceof RoundedCornerTreatment && this.bottomRightCorner instanceof RoundedCornerTreatment && this.bottomLeftCorner instanceof RoundedCornerTreatment) {
      bool3 = true;
    } else {
      bool3 = false;
    } 
    if (!bool1 || !bool2 || !bool3)
      bool4 = false; 
    return bool4;
  }
  
  public Builder toBuilder() {
    return new Builder(this);
  }
  
  public ShapeAppearanceModel withCornerSize(float paramFloat) {
    return toBuilder().setAllCornerSizes(paramFloat).build();
  }
  
  public ShapeAppearanceModel withCornerSize(CornerSize paramCornerSize) {
    return toBuilder().setAllCornerSizes(paramCornerSize).build();
  }
  
  public ShapeAppearanceModel withTransformedCornerSizes(CornerSizeUnaryOperator paramCornerSizeUnaryOperator) {
    return toBuilder().setTopLeftCornerSize(paramCornerSizeUnaryOperator.apply(getTopLeftCornerSize())).setTopRightCornerSize(paramCornerSizeUnaryOperator.apply(getTopRightCornerSize())).setBottomLeftCornerSize(paramCornerSizeUnaryOperator.apply(getBottomLeftCornerSize())).setBottomRightCornerSize(paramCornerSizeUnaryOperator.apply(getBottomRightCornerSize())).build();
  }
  
  public static final class Builder {
    private EdgeTreatment bottomEdge = MaterialShapeUtils.createDefaultEdgeTreatment();
    
    private CornerTreatment bottomLeftCorner = MaterialShapeUtils.createDefaultCornerTreatment();
    
    private CornerSize bottomLeftCornerSize = new AbsoluteCornerSize(0.0F);
    
    private CornerTreatment bottomRightCorner = MaterialShapeUtils.createDefaultCornerTreatment();
    
    private CornerSize bottomRightCornerSize = new AbsoluteCornerSize(0.0F);
    
    private EdgeTreatment leftEdge = MaterialShapeUtils.createDefaultEdgeTreatment();
    
    private EdgeTreatment rightEdge = MaterialShapeUtils.createDefaultEdgeTreatment();
    
    private EdgeTreatment topEdge = MaterialShapeUtils.createDefaultEdgeTreatment();
    
    private CornerTreatment topLeftCorner = MaterialShapeUtils.createDefaultCornerTreatment();
    
    private CornerSize topLeftCornerSize = new AbsoluteCornerSize(0.0F);
    
    private CornerTreatment topRightCorner = MaterialShapeUtils.createDefaultCornerTreatment();
    
    private CornerSize topRightCornerSize = new AbsoluteCornerSize(0.0F);
    
    public Builder() {}
    
    public Builder(ShapeAppearanceModel param1ShapeAppearanceModel) {
      this.topLeftCorner = param1ShapeAppearanceModel.topLeftCorner;
      this.topRightCorner = param1ShapeAppearanceModel.topRightCorner;
      this.bottomRightCorner = param1ShapeAppearanceModel.bottomRightCorner;
      this.bottomLeftCorner = param1ShapeAppearanceModel.bottomLeftCorner;
      this.topLeftCornerSize = param1ShapeAppearanceModel.topLeftCornerSize;
      this.topRightCornerSize = param1ShapeAppearanceModel.topRightCornerSize;
      this.bottomRightCornerSize = param1ShapeAppearanceModel.bottomRightCornerSize;
      this.bottomLeftCornerSize = param1ShapeAppearanceModel.bottomLeftCornerSize;
      this.topEdge = param1ShapeAppearanceModel.topEdge;
      this.rightEdge = param1ShapeAppearanceModel.rightEdge;
      this.bottomEdge = param1ShapeAppearanceModel.bottomEdge;
      this.leftEdge = param1ShapeAppearanceModel.leftEdge;
    }
    
    private static float compatCornerTreatmentSize(CornerTreatment param1CornerTreatment) {
      return (param1CornerTreatment instanceof RoundedCornerTreatment) ? ((RoundedCornerTreatment)param1CornerTreatment).radius : ((param1CornerTreatment instanceof CutCornerTreatment) ? ((CutCornerTreatment)param1CornerTreatment).size : -1.0F);
    }
    
    public ShapeAppearanceModel build() {
      return new ShapeAppearanceModel(this);
    }
    
    public Builder setAllCornerSizes(float param1Float) {
      return setTopLeftCornerSize(param1Float).setTopRightCornerSize(param1Float).setBottomRightCornerSize(param1Float).setBottomLeftCornerSize(param1Float);
    }
    
    public Builder setAllCornerSizes(CornerSize param1CornerSize) {
      return setTopLeftCornerSize(param1CornerSize).setTopRightCornerSize(param1CornerSize).setBottomRightCornerSize(param1CornerSize).setBottomLeftCornerSize(param1CornerSize);
    }
    
    public Builder setAllCorners(int param1Int, float param1Float) {
      return setAllCorners(MaterialShapeUtils.createCornerTreatment(param1Int)).setAllCornerSizes(param1Float);
    }
    
    public Builder setAllCorners(CornerTreatment param1CornerTreatment) {
      return setTopLeftCorner(param1CornerTreatment).setTopRightCorner(param1CornerTreatment).setBottomRightCorner(param1CornerTreatment).setBottomLeftCorner(param1CornerTreatment);
    }
    
    public Builder setAllEdges(EdgeTreatment param1EdgeTreatment) {
      return setLeftEdge(param1EdgeTreatment).setTopEdge(param1EdgeTreatment).setRightEdge(param1EdgeTreatment).setBottomEdge(param1EdgeTreatment);
    }
    
    public Builder setBottomEdge(EdgeTreatment param1EdgeTreatment) {
      this.bottomEdge = param1EdgeTreatment;
      return this;
    }
    
    public Builder setBottomLeftCorner(int param1Int, float param1Float) {
      return setBottomLeftCorner(MaterialShapeUtils.createCornerTreatment(param1Int)).setBottomLeftCornerSize(param1Float);
    }
    
    public Builder setBottomLeftCorner(int param1Int, CornerSize param1CornerSize) {
      return setBottomLeftCorner(MaterialShapeUtils.createCornerTreatment(param1Int)).setBottomLeftCornerSize(param1CornerSize);
    }
    
    public Builder setBottomLeftCorner(CornerTreatment param1CornerTreatment) {
      this.bottomLeftCorner = param1CornerTreatment;
      float f = compatCornerTreatmentSize(param1CornerTreatment);
      if (f != -1.0F)
        setBottomLeftCornerSize(f); 
      return this;
    }
    
    public Builder setBottomLeftCornerSize(float param1Float) {
      this.bottomLeftCornerSize = new AbsoluteCornerSize(param1Float);
      return this;
    }
    
    public Builder setBottomLeftCornerSize(CornerSize param1CornerSize) {
      this.bottomLeftCornerSize = param1CornerSize;
      return this;
    }
    
    public Builder setBottomRightCorner(int param1Int, float param1Float) {
      return setBottomRightCorner(MaterialShapeUtils.createCornerTreatment(param1Int)).setBottomRightCornerSize(param1Float);
    }
    
    public Builder setBottomRightCorner(int param1Int, CornerSize param1CornerSize) {
      return setBottomRightCorner(MaterialShapeUtils.createCornerTreatment(param1Int)).setBottomRightCornerSize(param1CornerSize);
    }
    
    public Builder setBottomRightCorner(CornerTreatment param1CornerTreatment) {
      this.bottomRightCorner = param1CornerTreatment;
      float f = compatCornerTreatmentSize(param1CornerTreatment);
      if (f != -1.0F)
        setBottomRightCornerSize(f); 
      return this;
    }
    
    public Builder setBottomRightCornerSize(float param1Float) {
      this.bottomRightCornerSize = new AbsoluteCornerSize(param1Float);
      return this;
    }
    
    public Builder setBottomRightCornerSize(CornerSize param1CornerSize) {
      this.bottomRightCornerSize = param1CornerSize;
      return this;
    }
    
    public Builder setLeftEdge(EdgeTreatment param1EdgeTreatment) {
      this.leftEdge = param1EdgeTreatment;
      return this;
    }
    
    public Builder setRightEdge(EdgeTreatment param1EdgeTreatment) {
      this.rightEdge = param1EdgeTreatment;
      return this;
    }
    
    public Builder setTopEdge(EdgeTreatment param1EdgeTreatment) {
      this.topEdge = param1EdgeTreatment;
      return this;
    }
    
    public Builder setTopLeftCorner(int param1Int, float param1Float) {
      return setTopLeftCorner(MaterialShapeUtils.createCornerTreatment(param1Int)).setTopLeftCornerSize(param1Float);
    }
    
    public Builder setTopLeftCorner(int param1Int, CornerSize param1CornerSize) {
      return setTopLeftCorner(MaterialShapeUtils.createCornerTreatment(param1Int)).setTopLeftCornerSize(param1CornerSize);
    }
    
    public Builder setTopLeftCorner(CornerTreatment param1CornerTreatment) {
      this.topLeftCorner = param1CornerTreatment;
      float f = compatCornerTreatmentSize(param1CornerTreatment);
      if (f != -1.0F)
        setTopLeftCornerSize(f); 
      return this;
    }
    
    public Builder setTopLeftCornerSize(float param1Float) {
      this.topLeftCornerSize = new AbsoluteCornerSize(param1Float);
      return this;
    }
    
    public Builder setTopLeftCornerSize(CornerSize param1CornerSize) {
      this.topLeftCornerSize = param1CornerSize;
      return this;
    }
    
    public Builder setTopRightCorner(int param1Int, float param1Float) {
      return setTopRightCorner(MaterialShapeUtils.createCornerTreatment(param1Int)).setTopRightCornerSize(param1Float);
    }
    
    public Builder setTopRightCorner(int param1Int, CornerSize param1CornerSize) {
      return setTopRightCorner(MaterialShapeUtils.createCornerTreatment(param1Int)).setTopRightCornerSize(param1CornerSize);
    }
    
    public Builder setTopRightCorner(CornerTreatment param1CornerTreatment) {
      this.topRightCorner = param1CornerTreatment;
      float f = compatCornerTreatmentSize(param1CornerTreatment);
      if (f != -1.0F)
        setTopRightCornerSize(f); 
      return this;
    }
    
    public Builder setTopRightCornerSize(float param1Float) {
      this.topRightCornerSize = new AbsoluteCornerSize(param1Float);
      return this;
    }
    
    public Builder setTopRightCornerSize(CornerSize param1CornerSize) {
      this.topRightCornerSize = param1CornerSize;
      return this;
    }
  }
  
  public static interface CornerSizeUnaryOperator {
    CornerSize apply(CornerSize param1CornerSize);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\shape\ShapeAppearanceModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */