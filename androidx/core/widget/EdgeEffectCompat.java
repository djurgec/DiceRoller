package androidx.core.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EdgeEffect;
import androidx.core.os.BuildCompat;

public final class EdgeEffectCompat {
  private EdgeEffect mEdgeEffect;
  
  @Deprecated
  public EdgeEffectCompat(Context paramContext) {
    this.mEdgeEffect = new EdgeEffect(paramContext);
  }
  
  public static EdgeEffect create(Context paramContext, AttributeSet paramAttributeSet) {
    return BuildCompat.isAtLeastS() ? Api31Impl.create(paramContext, paramAttributeSet) : new EdgeEffect(paramContext);
  }
  
  public static float getDistance(EdgeEffect paramEdgeEffect) {
    return BuildCompat.isAtLeastS() ? Api31Impl.getDistance(paramEdgeEffect) : 0.0F;
  }
  
  public static void onPull(EdgeEffect paramEdgeEffect, float paramFloat1, float paramFloat2) {
    if (Build.VERSION.SDK_INT >= 21) {
      paramEdgeEffect.onPull(paramFloat1, paramFloat2);
    } else {
      paramEdgeEffect.onPull(paramFloat1);
    } 
  }
  
  public static float onPullDistance(EdgeEffect paramEdgeEffect, float paramFloat1, float paramFloat2) {
    if (BuildCompat.isAtLeastS())
      return Api31Impl.onPullDistance(paramEdgeEffect, paramFloat1, paramFloat2); 
    onPull(paramEdgeEffect, paramFloat1, paramFloat2);
    return paramFloat1;
  }
  
  @Deprecated
  public boolean draw(Canvas paramCanvas) {
    return this.mEdgeEffect.draw(paramCanvas);
  }
  
  @Deprecated
  public void finish() {
    this.mEdgeEffect.finish();
  }
  
  @Deprecated
  public boolean isFinished() {
    return this.mEdgeEffect.isFinished();
  }
  
  @Deprecated
  public boolean onAbsorb(int paramInt) {
    this.mEdgeEffect.onAbsorb(paramInt);
    return true;
  }
  
  @Deprecated
  public boolean onPull(float paramFloat) {
    this.mEdgeEffect.onPull(paramFloat);
    return true;
  }
  
  @Deprecated
  public boolean onPull(float paramFloat1, float paramFloat2) {
    onPull(this.mEdgeEffect, paramFloat1, paramFloat2);
    return true;
  }
  
  @Deprecated
  public boolean onRelease() {
    this.mEdgeEffect.onRelease();
    return this.mEdgeEffect.isFinished();
  }
  
  @Deprecated
  public void setSize(int paramInt1, int paramInt2) {
    this.mEdgeEffect.setSize(paramInt1, paramInt2);
  }
  
  private static class Api31Impl {
    public static EdgeEffect create(Context param1Context, AttributeSet param1AttributeSet) {
      try {
        return new EdgeEffect(param1Context, param1AttributeSet);
      } finally {
        param1AttributeSet = null;
      } 
    }
    
    public static float getDistance(EdgeEffect param1EdgeEffect) {
      try {
        return param1EdgeEffect.getDistance();
      } finally {
        param1EdgeEffect = null;
      } 
    }
    
    public static float onPullDistance(EdgeEffect param1EdgeEffect, float param1Float1, float param1Float2) {
      try {
        return param1EdgeEffect.onPullDistance(param1Float1, param1Float2);
      } finally {
        Exception exception = null;
        param1EdgeEffect.onPull(param1Float1, param1Float2);
      } 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\widget\EdgeEffectCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */