package androidx.activity.result;

import androidx.activity.result.contract.ActivityResultContract;

public interface ActivityResultCaller {
  <I, O> ActivityResultLauncher<I> registerForActivityResult(ActivityResultContract<I, O> paramActivityResultContract, ActivityResultCallback<O> paramActivityResultCallback);
  
  <I, O> ActivityResultLauncher<I> registerForActivityResult(ActivityResultContract<I, O> paramActivityResultContract, ActivityResultRegistry paramActivityResultRegistry, ActivityResultCallback<O> paramActivityResultCallback);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\activity\result\ActivityResultCaller.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */