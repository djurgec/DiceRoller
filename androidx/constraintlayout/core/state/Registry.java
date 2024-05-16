package androidx.constraintlayout.core.state;

import java.util.HashMap;
import java.util.Set;

public class Registry {
  private static final Registry sRegistry = new Registry();
  
  private HashMap<String, RegistryCallback> mCallbacks = new HashMap<>();
  
  public static Registry getInstance() {
    return sRegistry;
  }
  
  public String currentContent(String paramString) {
    RegistryCallback registryCallback = this.mCallbacks.get(paramString);
    return (registryCallback != null) ? registryCallback.currentMotionScene() : null;
  }
  
  public String currentLayoutInformation(String paramString) {
    RegistryCallback registryCallback = this.mCallbacks.get(paramString);
    return (registryCallback != null) ? registryCallback.currentLayoutInformation() : null;
  }
  
  public long getLastModified(String paramString) {
    RegistryCallback registryCallback = this.mCallbacks.get(paramString);
    return (registryCallback != null) ? registryCallback.getLastModified() : Long.MAX_VALUE;
  }
  
  public Set<String> getLayoutList() {
    return this.mCallbacks.keySet();
  }
  
  public void register(String paramString, RegistryCallback paramRegistryCallback) {
    this.mCallbacks.put(paramString, paramRegistryCallback);
  }
  
  public void setDrawDebug(String paramString, int paramInt) {
    RegistryCallback registryCallback = this.mCallbacks.get(paramString);
    if (registryCallback != null)
      registryCallback.setDrawDebug(paramInt); 
  }
  
  public void setLayoutInformationMode(String paramString, int paramInt) {
    RegistryCallback registryCallback = this.mCallbacks.get(paramString);
    if (registryCallback != null)
      registryCallback.setLayoutInformationMode(paramInt); 
  }
  
  public void unregister(String paramString, RegistryCallback paramRegistryCallback) {
    this.mCallbacks.remove(paramString);
  }
  
  public void updateContent(String paramString1, String paramString2) {
    RegistryCallback registryCallback = this.mCallbacks.get(paramString1);
    if (registryCallback != null)
      registryCallback.onNewMotionScene(paramString2); 
  }
  
  public void updateDimensions(String paramString, int paramInt1, int paramInt2) {
    RegistryCallback registryCallback = this.mCallbacks.get(paramString);
    if (registryCallback != null)
      registryCallback.onDimensions(paramInt1, paramInt2); 
  }
  
  public void updateProgress(String paramString, float paramFloat) {
    RegistryCallback registryCallback = this.mCallbacks.get(paramString);
    if (registryCallback != null)
      registryCallback.onProgress(paramFloat); 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\state\Registry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */