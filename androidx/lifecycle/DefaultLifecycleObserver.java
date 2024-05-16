package androidx.lifecycle;

public interface DefaultLifecycleObserver extends FullLifecycleObserver {
  default void onCreate(LifecycleOwner paramLifecycleOwner) {}
  
  default void onDestroy(LifecycleOwner paramLifecycleOwner) {}
  
  default void onPause(LifecycleOwner paramLifecycleOwner) {}
  
  default void onResume(LifecycleOwner paramLifecycleOwner) {}
  
  default void onStart(LifecycleOwner paramLifecycleOwner) {}
  
  default void onStop(LifecycleOwner paramLifecycleOwner) {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\lifecycle\DefaultLifecycleObserver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */