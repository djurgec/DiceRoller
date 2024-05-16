package androidx.fragment.app;

import android.os.Bundle;
import androidx.lifecycle.LifecycleOwner;

public interface FragmentResultOwner {
  void clearFragmentResult(String paramString);
  
  void clearFragmentResultListener(String paramString);
  
  void setFragmentResult(String paramString, Bundle paramBundle);
  
  void setFragmentResultListener(String paramString, LifecycleOwner paramLifecycleOwner, FragmentResultListener paramFragmentResultListener);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\fragment\app\FragmentResultOwner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */