package androidx.dynamicanimation.animation;

public final class FlingAnimation extends DynamicAnimation<FlingAnimation> {
  private final DragForce mFlingForce;
  
  public FlingAnimation(FloatValueHolder paramFloatValueHolder) {
    super(paramFloatValueHolder);
    DragForce dragForce = new DragForce();
    this.mFlingForce = dragForce;
    dragForce.setValueThreshold(getValueThreshold());
  }
  
  public <K> FlingAnimation(K paramK, FloatPropertyCompat<K> paramFloatPropertyCompat) {
    super(paramK, paramFloatPropertyCompat);
    DragForce dragForce = new DragForce();
    this.mFlingForce = dragForce;
    dragForce.setValueThreshold(getValueThreshold());
  }
  
  float getAcceleration(float paramFloat1, float paramFloat2) {
    return this.mFlingForce.getAcceleration(paramFloat1, paramFloat2);
  }
  
  public float getFriction() {
    return this.mFlingForce.getFrictionScalar();
  }
  
  boolean isAtEquilibrium(float paramFloat1, float paramFloat2) {
    return (paramFloat1 >= this.mMaxValue || paramFloat1 <= this.mMinValue || this.mFlingForce.isAtEquilibrium(paramFloat1, paramFloat2));
  }
  
  public FlingAnimation setFriction(float paramFloat) {
    if (paramFloat > 0.0F) {
      this.mFlingForce.setFrictionScalar(paramFloat);
      return this;
    } 
    throw new IllegalArgumentException("Friction must be positive");
  }
  
  public FlingAnimation setMaxValue(float paramFloat) {
    super.setMaxValue(paramFloat);
    return this;
  }
  
  public FlingAnimation setMinValue(float paramFloat) {
    super.setMinValue(paramFloat);
    return this;
  }
  
  public FlingAnimation setStartVelocity(float paramFloat) {
    super.setStartVelocity(paramFloat);
    return this;
  }
  
  void setValueThreshold(float paramFloat) {
    this.mFlingForce.setValueThreshold(paramFloat);
  }
  
  boolean updateValueAndVelocity(long paramLong) {
    DynamicAnimation.MassState massState = this.mFlingForce.updateValueAndVelocity(this.mValue, this.mVelocity, paramLong);
    this.mValue = massState.mValue;
    this.mVelocity = massState.mVelocity;
    if (this.mValue < this.mMinValue) {
      this.mValue = this.mMinValue;
      return true;
    } 
    if (this.mValue > this.mMaxValue) {
      this.mValue = this.mMaxValue;
      return true;
    } 
    return isAtEquilibrium(this.mValue, this.mVelocity);
  }
  
  static final class DragForce implements Force {
    private static final float DEFAULT_FRICTION = -4.2F;
    
    private static final float VELOCITY_THRESHOLD_MULTIPLIER = 62.5F;
    
    private float mFriction = -4.2F;
    
    private final DynamicAnimation.MassState mMassState = new DynamicAnimation.MassState();
    
    private float mVelocityThreshold;
    
    public float getAcceleration(float param1Float1, float param1Float2) {
      return this.mFriction * param1Float2;
    }
    
    float getFrictionScalar() {
      return this.mFriction / -4.2F;
    }
    
    public boolean isAtEquilibrium(float param1Float1, float param1Float2) {
      boolean bool;
      if (Math.abs(param1Float2) < this.mVelocityThreshold) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    void setFrictionScalar(float param1Float) {
      this.mFriction = -4.2F * param1Float;
    }
    
    void setValueThreshold(float param1Float) {
      this.mVelocityThreshold = 62.5F * param1Float;
    }
    
    DynamicAnimation.MassState updateValueAndVelocity(float param1Float1, float param1Float2, long param1Long) {
      this.mMassState.mVelocity = (float)(param1Float2 * Math.exp(((float)param1Long / 1000.0F * this.mFriction)));
      DynamicAnimation.MassState massState = this.mMassState;
      float f = this.mFriction;
      massState.mValue = (float)((param1Float1 - param1Float2 / f) + (param1Float2 / f) * Math.exp((f * (float)param1Long / 1000.0F)));
      if (isAtEquilibrium(this.mMassState.mValue, this.mMassState.mVelocity))
        this.mMassState.mVelocity = 0.0F; 
      return this.mMassState;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\dynamicanimation\animation\FlingAnimation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */