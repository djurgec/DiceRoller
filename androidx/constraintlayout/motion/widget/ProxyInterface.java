package androidx.constraintlayout.motion.widget;

interface ProxyInterface {
  int designAccess(int paramInt1, String paramString, Object paramObject, float[] paramArrayOffloat1, int paramInt2, float[] paramArrayOffloat2, int paramInt3);
  
  float getKeyFramePosition(Object paramObject, int paramInt, float paramFloat1, float paramFloat2);
  
  Object getKeyframeAtLocation(Object paramObject, float paramFloat1, float paramFloat2);
  
  Boolean getPositionKeyframe(Object paramObject1, Object paramObject2, float paramFloat1, float paramFloat2, String[] paramArrayOfString, float[] paramArrayOffloat);
  
  long getTransitionTimeMs();
  
  void setAttributes(int paramInt, String paramString, Object paramObject1, Object paramObject2);
  
  void setKeyFrame(Object paramObject1, int paramInt, String paramString, Object paramObject2);
  
  boolean setKeyFramePosition(Object paramObject, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2);
  
  void setToolPosition(float paramFloat);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\motion\widget\ProxyInterface.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */