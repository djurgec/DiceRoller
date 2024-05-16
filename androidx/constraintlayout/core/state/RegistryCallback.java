package androidx.constraintlayout.core.state;

public interface RegistryCallback {
  String currentLayoutInformation();
  
  String currentMotionScene();
  
  long getLastModified();
  
  void onDimensions(int paramInt1, int paramInt2);
  
  void onNewMotionScene(String paramString);
  
  void onProgress(float paramFloat);
  
  void setDrawDebug(int paramInt);
  
  void setLayoutInformationMode(int paramInt);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\state\RegistryCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */