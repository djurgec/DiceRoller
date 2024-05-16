package androidx.constraintlayout.motion.utils;

import androidx.constraintlayout.core.motion.utils.SpringStopEngine;
import androidx.constraintlayout.core.motion.utils.StopEngine;
import androidx.constraintlayout.core.motion.utils.StopLogicEngine;
import androidx.constraintlayout.motion.widget.MotionInterpolator;

public class StopLogic extends MotionInterpolator {
  private StopEngine mEngine;
  
  private SpringStopEngine mSpringStopEngine;
  
  private StopLogicEngine mStopLogicEngine;
  
  public StopLogic() {
    StopLogicEngine stopLogicEngine = new StopLogicEngine();
    this.mStopLogicEngine = stopLogicEngine;
    this.mEngine = (StopEngine)stopLogicEngine;
  }
  
  public void config(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6) {
    StopLogicEngine stopLogicEngine = this.mStopLogicEngine;
    this.mEngine = (StopEngine)stopLogicEngine;
    stopLogicEngine.config(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
  }
  
  public String debug(String paramString, float paramFloat) {
    return this.mEngine.debug(paramString, paramFloat);
  }
  
  public float getInterpolation(float paramFloat) {
    return this.mEngine.getInterpolation(paramFloat);
  }
  
  public float getVelocity() {
    return this.mEngine.getVelocity();
  }
  
  public float getVelocity(float paramFloat) {
    return this.mEngine.getVelocity(paramFloat);
  }
  
  public boolean isStopped() {
    return this.mEngine.isStopped();
  }
  
  public void springConfig(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, int paramInt) {
    if (this.mSpringStopEngine == null)
      this.mSpringStopEngine = new SpringStopEngine(); 
    SpringStopEngine springStopEngine = this.mSpringStopEngine;
    this.mEngine = (StopEngine)springStopEngine;
    springStopEngine.springConfig(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramInt);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\motio\\utils\StopLogic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */