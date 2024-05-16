package androidx.emoji2.text;

import android.content.Context;
import android.os.Build;
import androidx.core.os.TraceCompat;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleInitializer;
import androidx.startup.AppInitializer;
import androidx.startup.Initializer;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public class EmojiCompatInitializer implements Initializer<Boolean> {
  private static final long STARTUP_THREAD_CREATION_DELAY_MS = 500L;
  
  private static final String S_INITIALIZER_THREAD_NAME = "EmojiCompatInitializer";
  
  public Boolean create(Context paramContext) {
    if (Build.VERSION.SDK_INT >= 19) {
      EmojiCompat.init(new BackgroundDefaultConfig(paramContext));
      delayUntilFirstResume(paramContext);
      return Boolean.valueOf(true);
    } 
    return Boolean.valueOf(false);
  }
  
  void delayUntilFirstResume(Context paramContext) {
    final Lifecycle lifecycle = ((LifecycleOwner)AppInitializer.getInstance(paramContext).initializeComponent(ProcessLifecycleInitializer.class)).getLifecycle();
    lifecycle.addObserver((LifecycleObserver)new DefaultLifecycleObserver() {
          final EmojiCompatInitializer this$0;
          
          final Lifecycle val$lifecycle;
          
          public void onResume(LifecycleOwner param1LifecycleOwner) {
            EmojiCompatInitializer.this.loadEmojiCompatAfterDelay();
            lifecycle.removeObserver((LifecycleObserver)this);
          }
        });
  }
  
  public List<Class<? extends Initializer<?>>> dependencies() {
    return (List)Collections.singletonList(ProcessLifecycleInitializer.class);
  }
  
  void loadEmojiCompatAfterDelay() {
    ConcurrencyHelpers.mainHandlerAsync().postDelayed(new LoadEmojiCompatRunnable(), 500L);
  }
  
  static class BackgroundDefaultConfig extends EmojiCompat.Config {
    protected BackgroundDefaultConfig(Context param1Context) {
      super(new EmojiCompatInitializer.BackgroundDefaultLoader(param1Context));
      setMetadataLoadStrategy(1);
    }
  }
  
  static class BackgroundDefaultLoader implements EmojiCompat.MetadataRepoLoader {
    private final Context mContext;
    
    BackgroundDefaultLoader(Context param1Context) {
      this.mContext = param1Context.getApplicationContext();
    }
    
    void doLoad(EmojiCompat.MetadataRepoLoaderCallback param1MetadataRepoLoaderCallback, ThreadPoolExecutor param1ThreadPoolExecutor) {
      try {
      
      } finally {
        Exception exception = null;
        param1MetadataRepoLoaderCallback.onFailed(exception);
      } 
    }
    
    public void load(EmojiCompat.MetadataRepoLoaderCallback param1MetadataRepoLoaderCallback) {
      ThreadPoolExecutor threadPoolExecutor = ConcurrencyHelpers.createBackgroundPriorityExecutor("EmojiCompatInitializer");
      threadPoolExecutor.execute(new EmojiCompatInitializer$BackgroundDefaultLoader$$ExternalSyntheticLambda0(this, param1MetadataRepoLoaderCallback, threadPoolExecutor));
    }
  }
  
  class null extends EmojiCompat.MetadataRepoLoaderCallback {
    final EmojiCompatInitializer.BackgroundDefaultLoader this$0;
    
    final ThreadPoolExecutor val$executor;
    
    final EmojiCompat.MetadataRepoLoaderCallback val$loaderCallback;
    
    public void onFailed(Throwable param1Throwable) {
      try {
        loaderCallback.onFailed(param1Throwable);
        return;
      } finally {
        executor.shutdown();
      } 
    }
    
    public void onLoaded(MetadataRepo param1MetadataRepo) {
      try {
        loaderCallback.onLoaded(param1MetadataRepo);
        return;
      } finally {
        executor.shutdown();
      } 
    }
  }
  
  static class LoadEmojiCompatRunnable implements Runnable {
    public void run() {
      try {
        TraceCompat.beginSection("EmojiCompat.EmojiCompatInitializer.run");
        if (EmojiCompat.isConfigured())
          EmojiCompat.get().load(); 
        return;
      } finally {
        TraceCompat.endSection();
      } 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\EmojiCompatInitializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */