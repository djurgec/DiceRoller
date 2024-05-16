package androidx.core.provider;

import android.graphics.Typeface;
import android.os.Handler;

class CallbackWithHandler {
  private final FontsContractCompat.FontRequestCallback mCallback;
  
  private final Handler mCallbackHandler;
  
  CallbackWithHandler(FontsContractCompat.FontRequestCallback paramFontRequestCallback) {
    this.mCallback = paramFontRequestCallback;
    this.mCallbackHandler = CalleeHandler.create();
  }
  
  CallbackWithHandler(FontsContractCompat.FontRequestCallback paramFontRequestCallback, Handler paramHandler) {
    this.mCallback = paramFontRequestCallback;
    this.mCallbackHandler = paramHandler;
  }
  
  private void onTypefaceRequestFailed(final int reason) {
    final FontsContractCompat.FontRequestCallback callback = this.mCallback;
    this.mCallbackHandler.post(new Runnable() {
          final CallbackWithHandler this$0;
          
          final FontsContractCompat.FontRequestCallback val$callback;
          
          final int val$reason;
          
          public void run() {
            callback.onTypefaceRequestFailed(reason);
          }
        });
  }
  
  private void onTypefaceRetrieved(final Typeface typeface) {
    final FontsContractCompat.FontRequestCallback callback = this.mCallback;
    this.mCallbackHandler.post(new Runnable() {
          final CallbackWithHandler this$0;
          
          final FontsContractCompat.FontRequestCallback val$callback;
          
          final Typeface val$typeface;
          
          public void run() {
            callback.onTypefaceRetrieved(typeface);
          }
        });
  }
  
  void onTypefaceResult(FontRequestWorker.TypefaceResult paramTypefaceResult) {
    if (paramTypefaceResult.isSuccess()) {
      onTypefaceRetrieved(paramTypefaceResult.mTypeface);
    } else {
      onTypefaceRequestFailed(paramTypefaceResult.mResult);
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\provider\CallbackWithHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */