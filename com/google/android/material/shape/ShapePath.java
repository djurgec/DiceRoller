package com.google.android.material.shape;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import com.google.android.material.shadow.ShadowRenderer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShapePath {
  protected static final float ANGLE_LEFT = 180.0F;
  
  private static final float ANGLE_UP = 270.0F;
  
  private boolean containsIncompatibleShadowOp;
  
  @Deprecated
  public float currentShadowAngle;
  
  @Deprecated
  public float endShadowAngle;
  
  @Deprecated
  public float endX;
  
  @Deprecated
  public float endY;
  
  private final List<PathOperation> operations = new ArrayList<>();
  
  private final List<ShadowCompatOperation> shadowCompatOperations = new ArrayList<>();
  
  @Deprecated
  public float startX;
  
  @Deprecated
  public float startY;
  
  public ShapePath() {
    reset(0.0F, 0.0F);
  }
  
  public ShapePath(float paramFloat1, float paramFloat2) {
    reset(paramFloat1, paramFloat2);
  }
  
  private void addConnectingShadowIfNecessary(float paramFloat) {
    if (getCurrentShadowAngle() == paramFloat)
      return; 
    float f = (paramFloat - getCurrentShadowAngle() + 360.0F) % 360.0F;
    if (f > 180.0F)
      return; 
    PathArcOperation pathArcOperation = new PathArcOperation(getEndX(), getEndY(), getEndX(), getEndY());
    pathArcOperation.setStartAngle(getCurrentShadowAngle());
    pathArcOperation.setSweepAngle(f);
    this.shadowCompatOperations.add(new ArcShadowOperation(pathArcOperation));
    setCurrentShadowAngle(paramFloat);
  }
  
  private void addShadowCompatOperation(ShadowCompatOperation paramShadowCompatOperation, float paramFloat1, float paramFloat2) {
    addConnectingShadowIfNecessary(paramFloat1);
    this.shadowCompatOperations.add(paramShadowCompatOperation);
    setCurrentShadowAngle(paramFloat2);
  }
  
  private float getCurrentShadowAngle() {
    return this.currentShadowAngle;
  }
  
  private float getEndShadowAngle() {
    return this.endShadowAngle;
  }
  
  private void setCurrentShadowAngle(float paramFloat) {
    this.currentShadowAngle = paramFloat;
  }
  
  private void setEndShadowAngle(float paramFloat) {
    this.endShadowAngle = paramFloat;
  }
  
  private void setEndX(float paramFloat) {
    this.endX = paramFloat;
  }
  
  private void setEndY(float paramFloat) {
    this.endY = paramFloat;
  }
  
  private void setStartX(float paramFloat) {
    this.startX = paramFloat;
  }
  
  private void setStartY(float paramFloat) {
    this.startY = paramFloat;
  }
  
  public void addArc(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6) {
    float f1;
    boolean bool;
    PathArcOperation pathArcOperation = new PathArcOperation(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    pathArcOperation.setStartAngle(paramFloat5);
    pathArcOperation.setSweepAngle(paramFloat6);
    this.operations.add(pathArcOperation);
    ArcShadowOperation arcShadowOperation = new ArcShadowOperation(pathArcOperation);
    float f2 = paramFloat5 + paramFloat6;
    if (paramFloat6 < 0.0F) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool) {
      f1 = (paramFloat5 + 180.0F) % 360.0F;
    } else {
      f1 = paramFloat5;
    } 
    if (bool)
      f2 = (180.0F + f2) % 360.0F; 
    addShadowCompatOperation(arcShadowOperation, f1, f2);
    setEndX((paramFloat1 + paramFloat3) * 0.5F + (paramFloat3 - paramFloat1) / 2.0F * (float)Math.cos(Math.toRadians((paramFloat5 + paramFloat6))));
    setEndY((paramFloat2 + paramFloat4) * 0.5F + (paramFloat4 - paramFloat2) / 2.0F * (float)Math.sin(Math.toRadians((paramFloat5 + paramFloat6))));
  }
  
  public void applyToPath(Matrix paramMatrix, Path paramPath) {
    byte b = 0;
    int i = this.operations.size();
    while (b < i) {
      ((PathOperation)this.operations.get(b)).applyToPath(paramMatrix, paramPath);
      b++;
    } 
  }
  
  boolean containsIncompatibleShadowOp() {
    return this.containsIncompatibleShadowOp;
  }
  
  ShadowCompatOperation createShadowCompatOperation(final Matrix transformCopy) {
    addConnectingShadowIfNecessary(getEndShadowAngle());
    transformCopy = new Matrix(transformCopy);
    return new ShadowCompatOperation() {
        final ShapePath this$0;
        
        final List val$operations;
        
        final Matrix val$transformCopy;
        
        public void draw(Matrix param1Matrix, ShadowRenderer param1ShadowRenderer, int param1Int, Canvas param1Canvas) {
          Iterator<ShapePath.ShadowCompatOperation> iterator = operations.iterator();
          while (iterator.hasNext())
            ((ShapePath.ShadowCompatOperation)iterator.next()).draw(transformCopy, param1ShadowRenderer, param1Int, param1Canvas); 
        }
      };
  }
  
  public void cubicToPoint(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6) {
    PathCubicOperation pathCubicOperation = new PathCubicOperation(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
    this.operations.add(pathCubicOperation);
    this.containsIncompatibleShadowOp = true;
    setEndX(paramFloat5);
    setEndY(paramFloat6);
  }
  
  float getEndX() {
    return this.endX;
  }
  
  float getEndY() {
    return this.endY;
  }
  
  float getStartX() {
    return this.startX;
  }
  
  float getStartY() {
    return this.startY;
  }
  
  public void lineTo(float paramFloat1, float paramFloat2) {
    PathLineOperation pathLineOperation = new PathLineOperation();
    PathLineOperation.access$002(pathLineOperation, paramFloat1);
    PathLineOperation.access$102(pathLineOperation, paramFloat2);
    this.operations.add(pathLineOperation);
    LineShadowOperation lineShadowOperation = new LineShadowOperation(pathLineOperation, getEndX(), getEndY());
    addShadowCompatOperation(lineShadowOperation, lineShadowOperation.getAngle() + 270.0F, lineShadowOperation.getAngle() + 270.0F);
    setEndX(paramFloat1);
    setEndY(paramFloat2);
  }
  
  public void quadToPoint(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    PathQuadOperation pathQuadOperation = new PathQuadOperation();
    pathQuadOperation.setControlX(paramFloat1);
    pathQuadOperation.setControlY(paramFloat2);
    pathQuadOperation.setEndX(paramFloat3);
    pathQuadOperation.setEndY(paramFloat4);
    this.operations.add(pathQuadOperation);
    this.containsIncompatibleShadowOp = true;
    setEndX(paramFloat3);
    setEndY(paramFloat4);
  }
  
  public void reset(float paramFloat1, float paramFloat2) {
    reset(paramFloat1, paramFloat2, 270.0F, 0.0F);
  }
  
  public void reset(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    setStartX(paramFloat1);
    setStartY(paramFloat2);
    setEndX(paramFloat1);
    setEndY(paramFloat2);
    setCurrentShadowAngle(paramFloat3);
    setEndShadowAngle((paramFloat3 + paramFloat4) % 360.0F);
    this.operations.clear();
    this.shadowCompatOperations.clear();
    this.containsIncompatibleShadowOp = false;
  }
  
  static class ArcShadowOperation extends ShadowCompatOperation {
    private final ShapePath.PathArcOperation operation;
    
    public ArcShadowOperation(ShapePath.PathArcOperation param1PathArcOperation) {
      this.operation = param1PathArcOperation;
    }
    
    public void draw(Matrix param1Matrix, ShadowRenderer param1ShadowRenderer, int param1Int, Canvas param1Canvas) {
      float f1 = this.operation.getStartAngle();
      float f2 = this.operation.getSweepAngle();
      param1ShadowRenderer.drawCornerShadow(param1Canvas, param1Matrix, new RectF(this.operation.getLeft(), this.operation.getTop(), this.operation.getRight(), this.operation.getBottom()), param1Int, f1, f2);
    }
  }
  
  static class LineShadowOperation extends ShadowCompatOperation {
    private final ShapePath.PathLineOperation operation;
    
    private final float startX;
    
    private final float startY;
    
    public LineShadowOperation(ShapePath.PathLineOperation param1PathLineOperation, float param1Float1, float param1Float2) {
      this.operation = param1PathLineOperation;
      this.startX = param1Float1;
      this.startY = param1Float2;
    }
    
    public void draw(Matrix param1Matrix, ShadowRenderer param1ShadowRenderer, int param1Int, Canvas param1Canvas) {
      float f3 = this.operation.y;
      float f2 = this.startY;
      float f4 = this.operation.x;
      float f1 = this.startX;
      RectF rectF = new RectF(0.0F, 0.0F, (float)Math.hypot((f3 - f2), (f4 - f1)), 0.0F);
      param1Matrix = new Matrix(param1Matrix);
      param1Matrix.preTranslate(this.startX, this.startY);
      param1Matrix.preRotate(getAngle());
      param1ShadowRenderer.drawEdgeShadow(param1Canvas, param1Matrix, rectF, param1Int);
    }
    
    float getAngle() {
      return (float)Math.toDegrees(Math.atan(((this.operation.y - this.startY) / (this.operation.x - this.startX))));
    }
  }
  
  public static class PathArcOperation extends PathOperation {
    private static final RectF rectF = new RectF();
    
    @Deprecated
    public float bottom;
    
    @Deprecated
    public float left;
    
    @Deprecated
    public float right;
    
    @Deprecated
    public float startAngle;
    
    @Deprecated
    public float sweepAngle;
    
    @Deprecated
    public float top;
    
    public PathArcOperation(float param1Float1, float param1Float2, float param1Float3, float param1Float4) {
      setLeft(param1Float1);
      setTop(param1Float2);
      setRight(param1Float3);
      setBottom(param1Float4);
    }
    
    private float getBottom() {
      return this.bottom;
    }
    
    private float getLeft() {
      return this.left;
    }
    
    private float getRight() {
      return this.right;
    }
    
    private float getStartAngle() {
      return this.startAngle;
    }
    
    private float getSweepAngle() {
      return this.sweepAngle;
    }
    
    private float getTop() {
      return this.top;
    }
    
    private void setBottom(float param1Float) {
      this.bottom = param1Float;
    }
    
    private void setLeft(float param1Float) {
      this.left = param1Float;
    }
    
    private void setRight(float param1Float) {
      this.right = param1Float;
    }
    
    private void setStartAngle(float param1Float) {
      this.startAngle = param1Float;
    }
    
    private void setSweepAngle(float param1Float) {
      this.sweepAngle = param1Float;
    }
    
    private void setTop(float param1Float) {
      this.top = param1Float;
    }
    
    public void applyToPath(Matrix param1Matrix, Path param1Path) {
      Matrix matrix = this.matrix;
      param1Matrix.invert(matrix);
      param1Path.transform(matrix);
      RectF rectF = rectF;
      rectF.set(getLeft(), getTop(), getRight(), getBottom());
      param1Path.arcTo(rectF, getStartAngle(), getSweepAngle(), false);
      param1Path.transform(param1Matrix);
    }
  }
  
  public static class PathCubicOperation extends PathOperation {
    private float controlX1;
    
    private float controlX2;
    
    private float controlY1;
    
    private float controlY2;
    
    private float endX;
    
    private float endY;
    
    public PathCubicOperation(float param1Float1, float param1Float2, float param1Float3, float param1Float4, float param1Float5, float param1Float6) {
      setControlX1(param1Float1);
      setControlY1(param1Float2);
      setControlX2(param1Float3);
      setControlY2(param1Float4);
      setEndX(param1Float5);
      setEndY(param1Float6);
    }
    
    private float getControlX1() {
      return this.controlX1;
    }
    
    private float getControlX2() {
      return this.controlX2;
    }
    
    private float getControlY1() {
      return this.controlY1;
    }
    
    private float getControlY2() {
      return this.controlY1;
    }
    
    private float getEndX() {
      return this.endX;
    }
    
    private float getEndY() {
      return this.endY;
    }
    
    private void setControlX1(float param1Float) {
      this.controlX1 = param1Float;
    }
    
    private void setControlX2(float param1Float) {
      this.controlX2 = param1Float;
    }
    
    private void setControlY1(float param1Float) {
      this.controlY1 = param1Float;
    }
    
    private void setControlY2(float param1Float) {
      this.controlY2 = param1Float;
    }
    
    private void setEndX(float param1Float) {
      this.endX = param1Float;
    }
    
    private void setEndY(float param1Float) {
      this.endY = param1Float;
    }
    
    public void applyToPath(Matrix param1Matrix, Path param1Path) {
      Matrix matrix = this.matrix;
      param1Matrix.invert(matrix);
      param1Path.transform(matrix);
      param1Path.cubicTo(this.controlX1, this.controlY1, this.controlX2, this.controlY2, this.endX, this.endY);
      param1Path.transform(param1Matrix);
    }
  }
  
  public static class PathLineOperation extends PathOperation {
    private float x;
    
    private float y;
    
    public void applyToPath(Matrix param1Matrix, Path param1Path) {
      Matrix matrix = this.matrix;
      param1Matrix.invert(matrix);
      param1Path.transform(matrix);
      param1Path.lineTo(this.x, this.y);
      param1Path.transform(param1Matrix);
    }
  }
  
  public static abstract class PathOperation {
    protected final Matrix matrix = new Matrix();
    
    public abstract void applyToPath(Matrix param1Matrix, Path param1Path);
  }
  
  public static class PathQuadOperation extends PathOperation {
    @Deprecated
    public float controlX;
    
    @Deprecated
    public float controlY;
    
    @Deprecated
    public float endX;
    
    @Deprecated
    public float endY;
    
    private float getControlX() {
      return this.controlX;
    }
    
    private float getControlY() {
      return this.controlY;
    }
    
    private float getEndX() {
      return this.endX;
    }
    
    private float getEndY() {
      return this.endY;
    }
    
    private void setControlX(float param1Float) {
      this.controlX = param1Float;
    }
    
    private void setControlY(float param1Float) {
      this.controlY = param1Float;
    }
    
    private void setEndX(float param1Float) {
      this.endX = param1Float;
    }
    
    private void setEndY(float param1Float) {
      this.endY = param1Float;
    }
    
    public void applyToPath(Matrix param1Matrix, Path param1Path) {
      Matrix matrix = this.matrix;
      param1Matrix.invert(matrix);
      param1Path.transform(matrix);
      param1Path.quadTo(getControlX(), getControlY(), getEndX(), getEndY());
      param1Path.transform(param1Matrix);
    }
  }
  
  static abstract class ShadowCompatOperation {
    static final Matrix IDENTITY_MATRIX = new Matrix();
    
    public abstract void draw(Matrix param1Matrix, ShadowRenderer param1ShadowRenderer, int param1Int, Canvas param1Canvas);
    
    public final void draw(ShadowRenderer param1ShadowRenderer, int param1Int, Canvas param1Canvas) {
      draw(IDENTITY_MATRIX, param1ShadowRenderer, param1Int, param1Canvas);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\shape\ShapePath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */