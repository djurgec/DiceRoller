package com.google.android.material.shape;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;

public class ShapeAppearancePathProvider {
  private final Path boundsPath = new Path();
  
  private final Path cornerPath = new Path();
  
  private final ShapePath[] cornerPaths = new ShapePath[4];
  
  private final Matrix[] cornerTransforms = new Matrix[4];
  
  private boolean edgeIntersectionCheckEnabled = true;
  
  private final Path edgePath = new Path();
  
  private final Matrix[] edgeTransforms = new Matrix[4];
  
  private final Path overlappedEdgePath = new Path();
  
  private final PointF pointF = new PointF();
  
  private final float[] scratch = new float[2];
  
  private final float[] scratch2 = new float[2];
  
  private final ShapePath shapePath = new ShapePath();
  
  public ShapeAppearancePathProvider() {
    for (byte b = 0; b < 4; b++) {
      this.cornerPaths[b] = new ShapePath();
      this.cornerTransforms[b] = new Matrix();
      this.edgeTransforms[b] = new Matrix();
    } 
  }
  
  private float angleOfEdge(int paramInt) {
    return ((paramInt + 1) * 90);
  }
  
  private void appendCornerPath(ShapeAppearancePathSpec paramShapeAppearancePathSpec, int paramInt) {
    this.scratch[0] = this.cornerPaths[paramInt].getStartX();
    this.scratch[1] = this.cornerPaths[paramInt].getStartY();
    this.cornerTransforms[paramInt].mapPoints(this.scratch);
    if (paramInt == 0) {
      Path path = paramShapeAppearancePathSpec.path;
      float[] arrayOfFloat = this.scratch;
      path.moveTo(arrayOfFloat[0], arrayOfFloat[1]);
    } else {
      Path path = paramShapeAppearancePathSpec.path;
      float[] arrayOfFloat = this.scratch;
      path.lineTo(arrayOfFloat[0], arrayOfFloat[1]);
    } 
    this.cornerPaths[paramInt].applyToPath(this.cornerTransforms[paramInt], paramShapeAppearancePathSpec.path);
    if (paramShapeAppearancePathSpec.pathListener != null)
      paramShapeAppearancePathSpec.pathListener.onCornerPathCreated(this.cornerPaths[paramInt], this.cornerTransforms[paramInt], paramInt); 
  }
  
  private void appendEdgePath(ShapeAppearancePathSpec paramShapeAppearancePathSpec, int paramInt) {
    int i = (paramInt + 1) % 4;
    this.scratch[0] = this.cornerPaths[paramInt].getEndX();
    this.scratch[1] = this.cornerPaths[paramInt].getEndY();
    this.cornerTransforms[paramInt].mapPoints(this.scratch);
    this.scratch2[0] = this.cornerPaths[i].getStartX();
    this.scratch2[1] = this.cornerPaths[i].getStartY();
    this.cornerTransforms[i].mapPoints(this.scratch2);
    float[] arrayOfFloat1 = this.scratch;
    float f1 = arrayOfFloat1[0];
    float[] arrayOfFloat2 = this.scratch2;
    f1 = Math.max((float)Math.hypot((f1 - arrayOfFloat2[0]), (arrayOfFloat1[1] - arrayOfFloat2[1])) - 0.001F, 0.0F);
    float f2 = getEdgeCenterForIndex(paramShapeAppearancePathSpec.bounds, paramInt);
    this.shapePath.reset(0.0F, 0.0F);
    EdgeTreatment edgeTreatment = getEdgeTreatmentForIndex(paramInt, paramShapeAppearancePathSpec.shapeAppearanceModel);
    edgeTreatment.getEdgePath(f1, f2, paramShapeAppearancePathSpec.interpolation, this.shapePath);
    this.edgePath.reset();
    this.shapePath.applyToPath(this.edgeTransforms[paramInt], this.edgePath);
    if (this.edgeIntersectionCheckEnabled && Build.VERSION.SDK_INT >= 19 && (edgeTreatment.forceIntersection() || pathOverlapsCorner(this.edgePath, paramInt) || pathOverlapsCorner(this.edgePath, i))) {
      Path path = this.edgePath;
      path.op(path, this.boundsPath, Path.Op.DIFFERENCE);
      this.scratch[0] = this.shapePath.getStartX();
      this.scratch[1] = this.shapePath.getStartY();
      this.edgeTransforms[paramInt].mapPoints(this.scratch);
      path = this.overlappedEdgePath;
      arrayOfFloat2 = this.scratch;
      path.moveTo(arrayOfFloat2[0], arrayOfFloat2[1]);
      this.shapePath.applyToPath(this.edgeTransforms[paramInt], this.overlappedEdgePath);
    } else {
      this.shapePath.applyToPath(this.edgeTransforms[paramInt], paramShapeAppearancePathSpec.path);
    } 
    if (paramShapeAppearancePathSpec.pathListener != null)
      paramShapeAppearancePathSpec.pathListener.onEdgePathCreated(this.shapePath, this.edgeTransforms[paramInt], paramInt); 
  }
  
  private void getCoordinatesOfCorner(int paramInt, RectF paramRectF, PointF paramPointF) {
    switch (paramInt) {
      default:
        paramPointF.set(paramRectF.right, paramRectF.top);
        return;
      case 3:
        paramPointF.set(paramRectF.left, paramRectF.top);
        return;
      case 2:
        paramPointF.set(paramRectF.left, paramRectF.bottom);
        return;
      case 1:
        break;
    } 
    paramPointF.set(paramRectF.right, paramRectF.bottom);
  }
  
  private CornerSize getCornerSizeForIndex(int paramInt, ShapeAppearanceModel paramShapeAppearanceModel) {
    switch (paramInt) {
      default:
        return paramShapeAppearanceModel.getTopRightCornerSize();
      case 3:
        return paramShapeAppearanceModel.getTopLeftCornerSize();
      case 2:
        return paramShapeAppearanceModel.getBottomLeftCornerSize();
      case 1:
        break;
    } 
    return paramShapeAppearanceModel.getBottomRightCornerSize();
  }
  
  private CornerTreatment getCornerTreatmentForIndex(int paramInt, ShapeAppearanceModel paramShapeAppearanceModel) {
    switch (paramInt) {
      default:
        return paramShapeAppearanceModel.getTopRightCorner();
      case 3:
        return paramShapeAppearanceModel.getTopLeftCorner();
      case 2:
        return paramShapeAppearanceModel.getBottomLeftCorner();
      case 1:
        break;
    } 
    return paramShapeAppearanceModel.getBottomRightCorner();
  }
  
  private float getEdgeCenterForIndex(RectF paramRectF, int paramInt) {
    this.scratch[0] = (this.cornerPaths[paramInt]).endX;
    this.scratch[1] = (this.cornerPaths[paramInt]).endY;
    this.cornerTransforms[paramInt].mapPoints(this.scratch);
    switch (paramInt) {
      default:
        return Math.abs(paramRectF.centerY() - this.scratch[1]);
      case 1:
      case 3:
        break;
    } 
    return Math.abs(paramRectF.centerX() - this.scratch[0]);
  }
  
  private EdgeTreatment getEdgeTreatmentForIndex(int paramInt, ShapeAppearanceModel paramShapeAppearanceModel) {
    switch (paramInt) {
      default:
        return paramShapeAppearanceModel.getRightEdge();
      case 3:
        return paramShapeAppearanceModel.getTopEdge();
      case 2:
        return paramShapeAppearanceModel.getLeftEdge();
      case 1:
        break;
    } 
    return paramShapeAppearanceModel.getBottomEdge();
  }
  
  public static ShapeAppearancePathProvider getInstance() {
    return Lazy.INSTANCE;
  }
  
  private boolean pathOverlapsCorner(Path paramPath, int paramInt) {
    this.cornerPath.reset();
    this.cornerPaths[paramInt].applyToPath(this.cornerTransforms[paramInt], this.cornerPath);
    RectF rectF = new RectF();
    boolean bool2 = true;
    paramPath.computeBounds(rectF, true);
    this.cornerPath.computeBounds(rectF, true);
    paramPath.op(this.cornerPath, Path.Op.INTERSECT);
    paramPath.computeBounds(rectF, true);
    boolean bool1 = bool2;
    if (rectF.isEmpty())
      if (rectF.width() > 1.0F && rectF.height() > 1.0F) {
        bool1 = bool2;
      } else {
        bool1 = false;
      }  
    return bool1;
  }
  
  private void setCornerPathAndTransform(ShapeAppearancePathSpec paramShapeAppearancePathSpec, int paramInt) {
    CornerSize cornerSize = getCornerSizeForIndex(paramInt, paramShapeAppearancePathSpec.shapeAppearanceModel);
    getCornerTreatmentForIndex(paramInt, paramShapeAppearancePathSpec.shapeAppearanceModel).getCornerPath(this.cornerPaths[paramInt], 90.0F, paramShapeAppearancePathSpec.interpolation, paramShapeAppearancePathSpec.bounds, cornerSize);
    float f = angleOfEdge(paramInt);
    this.cornerTransforms[paramInt].reset();
    getCoordinatesOfCorner(paramInt, paramShapeAppearancePathSpec.bounds, this.pointF);
    this.cornerTransforms[paramInt].setTranslate(this.pointF.x, this.pointF.y);
    this.cornerTransforms[paramInt].preRotate(f);
  }
  
  private void setEdgePathAndTransform(int paramInt) {
    this.scratch[0] = this.cornerPaths[paramInt].getEndX();
    this.scratch[1] = this.cornerPaths[paramInt].getEndY();
    this.cornerTransforms[paramInt].mapPoints(this.scratch);
    float f = angleOfEdge(paramInt);
    this.edgeTransforms[paramInt].reset();
    Matrix matrix = this.edgeTransforms[paramInt];
    float[] arrayOfFloat = this.scratch;
    matrix.setTranslate(arrayOfFloat[0], arrayOfFloat[1]);
    this.edgeTransforms[paramInt].preRotate(f);
  }
  
  public void calculatePath(ShapeAppearanceModel paramShapeAppearanceModel, float paramFloat, RectF paramRectF, Path paramPath) {
    calculatePath(paramShapeAppearanceModel, paramFloat, paramRectF, null, paramPath);
  }
  
  public void calculatePath(ShapeAppearanceModel paramShapeAppearanceModel, float paramFloat, RectF paramRectF, PathListener paramPathListener, Path paramPath) {
    paramPath.rewind();
    this.overlappedEdgePath.rewind();
    this.boundsPath.rewind();
    this.boundsPath.addRect(paramRectF, Path.Direction.CW);
    ShapeAppearancePathSpec shapeAppearancePathSpec = new ShapeAppearancePathSpec(paramShapeAppearanceModel, paramFloat, paramRectF, paramPathListener, paramPath);
    byte b;
    for (b = 0; b < 4; b++) {
      setCornerPathAndTransform(shapeAppearancePathSpec, b);
      setEdgePathAndTransform(b);
    } 
    for (b = 0; b < 4; b++) {
      appendCornerPath(shapeAppearancePathSpec, b);
      appendEdgePath(shapeAppearancePathSpec, b);
    } 
    paramPath.close();
    this.overlappedEdgePath.close();
    if (Build.VERSION.SDK_INT >= 19 && !this.overlappedEdgePath.isEmpty())
      paramPath.op(this.overlappedEdgePath, Path.Op.UNION); 
  }
  
  void setEdgeIntersectionCheckEnable(boolean paramBoolean) {
    this.edgeIntersectionCheckEnabled = paramBoolean;
  }
  
  private static class Lazy {
    static final ShapeAppearancePathProvider INSTANCE = new ShapeAppearancePathProvider();
  }
  
  public static interface PathListener {
    void onCornerPathCreated(ShapePath param1ShapePath, Matrix param1Matrix, int param1Int);
    
    void onEdgePathCreated(ShapePath param1ShapePath, Matrix param1Matrix, int param1Int);
  }
  
  static final class ShapeAppearancePathSpec {
    public final RectF bounds;
    
    public final float interpolation;
    
    public final Path path;
    
    public final ShapeAppearancePathProvider.PathListener pathListener;
    
    public final ShapeAppearanceModel shapeAppearanceModel;
    
    ShapeAppearancePathSpec(ShapeAppearanceModel param1ShapeAppearanceModel, float param1Float, RectF param1RectF, ShapeAppearancePathProvider.PathListener param1PathListener, Path param1Path) {
      this.pathListener = param1PathListener;
      this.shapeAppearanceModel = param1ShapeAppearanceModel;
      this.interpolation = param1Float;
      this.bounds = param1RectF;
      this.path = param1Path;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\shape\ShapeAppearancePathProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */