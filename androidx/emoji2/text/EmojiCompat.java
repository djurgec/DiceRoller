package androidx.emoji2.text;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import androidx.collection.ArraySet;
import androidx.core.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EmojiCompat {
  private static final Object CONFIG_LOCK;
  
  public static final String EDITOR_INFO_METAVERSION_KEY = "android.support.text.emoji.emojiCompat_metadataVersion";
  
  public static final String EDITOR_INFO_REPLACE_ALL_KEY = "android.support.text.emoji.emojiCompat_replaceAll";
  
  static final int EMOJI_COUNT_UNLIMITED = 2147483647;
  
  private static final Object INSTANCE_LOCK = new Object();
  
  public static final int LOAD_STATE_DEFAULT = 3;
  
  public static final int LOAD_STATE_FAILED = 2;
  
  public static final int LOAD_STATE_LOADING = 0;
  
  public static final int LOAD_STATE_SUCCEEDED = 1;
  
  public static final int LOAD_STRATEGY_DEFAULT = 0;
  
  public static final int LOAD_STRATEGY_MANUAL = 1;
  
  private static final String NOT_INITIALIZED_ERROR_TEXT = "EmojiCompat is not initialized.\n\nYou must initialize EmojiCompat prior to referencing the EmojiCompat instance.\n\nThe most likely cause of this error is disabling the EmojiCompatInitializer\neither explicitly in AndroidManifest.xml, or by including\nandroidx.emoji2:emoji2-bundled.\n\nAutomatic initialization is typically performed by EmojiCompatInitializer. If\nyou are not expecting to initialize EmojiCompat manually in your application,\nplease check to ensure it has not been removed from your APK's manifest. You can\ndo this in Android Studio using Build > Analyze APK.\n\nIn the APK Analyzer, ensure that the startup entry for\nEmojiCompatInitializer and InitializationProvider is present in\n AndroidManifest.xml. If it is missing or contains tools:node=\"remove\", and you\nintend to use automatic configuration, verify:\n\n  1. Your application does not include emoji2-bundled\n  2. All modules do not contain an exclusion manifest rule for\n     EmojiCompatInitializer or InitializationProvider. For more information\n     about manifest exclusions see the documentation for the androidx startup\n     library.\n\nIf you intend to use emoji2-bundled, please call EmojiCompat.init. You can\nlearn more in the documentation for BundledEmojiCompatConfig.\n\nIf you intended to perform manual configuration, it is recommended that you call\nEmojiCompat.init immediately on application startup.\n\nIf you still cannot resolve this issue, please open a bug with your specific\nconfiguration to help improve error message.";
  
  public static final int REPLACE_STRATEGY_ALL = 1;
  
  public static final int REPLACE_STRATEGY_DEFAULT = 0;
  
  public static final int REPLACE_STRATEGY_NON_EXISTENT = 2;
  
  private static volatile boolean sHasDoneDefaultConfigLookup;
  
  private static volatile EmojiCompat sInstance;
  
  final int[] mEmojiAsDefaultStyleExceptions;
  
  private final int mEmojiSpanIndicatorColor;
  
  private final boolean mEmojiSpanIndicatorEnabled;
  
  private final GlyphChecker mGlyphChecker;
  
  private final CompatInternal mHelper;
  
  private final Set<InitCallback> mInitCallbacks;
  
  private final ReadWriteLock mInitLock;
  
  private volatile int mLoadState;
  
  private final Handler mMainHandler;
  
  private final int mMetadataLoadStrategy;
  
  final MetadataRepoLoader mMetadataLoader;
  
  final boolean mReplaceAll;
  
  final boolean mUseEmojiAsDefaultStyle;
  
  static {
    CONFIG_LOCK = new Object();
  }
  
  private EmojiCompat(Config paramConfig) {
    CompatInternal compatInternal;
    this.mInitLock = new ReentrantReadWriteLock();
    this.mLoadState = 3;
    this.mReplaceAll = paramConfig.mReplaceAll;
    this.mUseEmojiAsDefaultStyle = paramConfig.mUseEmojiAsDefaultStyle;
    this.mEmojiAsDefaultStyleExceptions = paramConfig.mEmojiAsDefaultStyleExceptions;
    this.mEmojiSpanIndicatorEnabled = paramConfig.mEmojiSpanIndicatorEnabled;
    this.mEmojiSpanIndicatorColor = paramConfig.mEmojiSpanIndicatorColor;
    this.mMetadataLoader = paramConfig.mMetadataLoader;
    this.mMetadataLoadStrategy = paramConfig.mMetadataLoadStrategy;
    this.mGlyphChecker = paramConfig.mGlyphChecker;
    this.mMainHandler = new Handler(Looper.getMainLooper());
    ArraySet<InitCallback> arraySet = new ArraySet();
    this.mInitCallbacks = (Set<InitCallback>)arraySet;
    if (paramConfig.mInitCallbacks != null && !paramConfig.mInitCallbacks.isEmpty())
      arraySet.addAll(paramConfig.mInitCallbacks); 
    if (Build.VERSION.SDK_INT < 19) {
      compatInternal = new CompatInternal(this);
    } else {
      compatInternal = new CompatInternal19(this);
    } 
    this.mHelper = compatInternal;
    loadMetadata();
  }
  
  public static EmojiCompat get() {
    synchronized (INSTANCE_LOCK) {
      boolean bool;
      EmojiCompat emojiCompat = sInstance;
      if (emojiCompat != null) {
        bool = true;
      } else {
        bool = false;
      } 
      Preconditions.checkState(bool, "EmojiCompat is not initialized.\n\nYou must initialize EmojiCompat prior to referencing the EmojiCompat instance.\n\nThe most likely cause of this error is disabling the EmojiCompatInitializer\neither explicitly in AndroidManifest.xml, or by including\nandroidx.emoji2:emoji2-bundled.\n\nAutomatic initialization is typically performed by EmojiCompatInitializer. If\nyou are not expecting to initialize EmojiCompat manually in your application,\nplease check to ensure it has not been removed from your APK's manifest. You can\ndo this in Android Studio using Build > Analyze APK.\n\nIn the APK Analyzer, ensure that the startup entry for\nEmojiCompatInitializer and InitializationProvider is present in\n AndroidManifest.xml. If it is missing or contains tools:node=\"remove\", and you\nintend to use automatic configuration, verify:\n\n  1. Your application does not include emoji2-bundled\n  2. All modules do not contain an exclusion manifest rule for\n     EmojiCompatInitializer or InitializationProvider. For more information\n     about manifest exclusions see the documentation for the androidx startup\n     library.\n\nIf you intend to use emoji2-bundled, please call EmojiCompat.init. You can\nlearn more in the documentation for BundledEmojiCompatConfig.\n\nIf you intended to perform manual configuration, it is recommended that you call\nEmojiCompat.init immediately on application startup.\n\nIf you still cannot resolve this issue, please open a bug with your specific\nconfiguration to help improve error message.");
      return emojiCompat;
    } 
  }
  
  public static boolean handleDeleteSurroundingText(InputConnection paramInputConnection, Editable paramEditable, int paramInt1, int paramInt2, boolean paramBoolean) {
    return (Build.VERSION.SDK_INT >= 19) ? EmojiProcessor.handleDeleteSurroundingText(paramInputConnection, paramEditable, paramInt1, paramInt2, paramBoolean) : false;
  }
  
  public static boolean handleOnKeyDown(Editable paramEditable, int paramInt, KeyEvent paramKeyEvent) {
    return (Build.VERSION.SDK_INT >= 19) ? EmojiProcessor.handleOnKeyDown(paramEditable, paramInt, paramKeyEvent) : false;
  }
  
  public static EmojiCompat init(Context paramContext) {
    return init(paramContext, null);
  }
  
  public static EmojiCompat init(Context paramContext, DefaultEmojiCompatConfig.DefaultEmojiCompatConfigFactory paramDefaultEmojiCompatConfigFactory) {
    if (sHasDoneDefaultConfigLookup)
      return sInstance; 
    if (paramDefaultEmojiCompatConfigFactory == null)
      paramDefaultEmojiCompatConfigFactory = new DefaultEmojiCompatConfig.DefaultEmojiCompatConfigFactory(null); 
    null = paramDefaultEmojiCompatConfigFactory.create(paramContext);
    synchronized (CONFIG_LOCK) {
      if (!sHasDoneDefaultConfigLookup) {
        if (null != null)
          init(null); 
        sHasDoneDefaultConfigLookup = true;
      } 
      return sInstance;
    } 
  }
  
  public static EmojiCompat init(Config paramConfig) {
    EmojiCompat emojiCompat2 = sInstance;
    EmojiCompat emojiCompat1 = emojiCompat2;
    if (emojiCompat2 == null)
      synchronized (INSTANCE_LOCK) {
        emojiCompat2 = sInstance;
        emojiCompat1 = emojiCompat2;
        if (emojiCompat2 == null) {
          emojiCompat1 = new EmojiCompat();
          this(paramConfig);
          sInstance = emojiCompat1;
        } 
      }  
    return emojiCompat1;
  }
  
  public static boolean isConfigured() {
    boolean bool;
    if (sInstance != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean isInitialized() {
    int i = getLoadState();
    boolean bool = true;
    if (i != 1)
      bool = false; 
    return bool;
  }
  
  private void loadMetadata() {
    this.mInitLock.writeLock().lock();
    try {
      if (this.mMetadataLoadStrategy == 0)
        this.mLoadState = 0; 
      this.mInitLock.writeLock().unlock();
      return;
    } finally {
      this.mInitLock.writeLock().unlock();
    } 
  }
  
  public static EmojiCompat reset(Config paramConfig) {
    synchronized (INSTANCE_LOCK) {
      EmojiCompat emojiCompat = new EmojiCompat();
      this(paramConfig);
      sInstance = emojiCompat;
      return emojiCompat;
    } 
  }
  
  public static EmojiCompat reset(EmojiCompat paramEmojiCompat) {
    synchronized (INSTANCE_LOCK) {
      sInstance = paramEmojiCompat;
      paramEmojiCompat = sInstance;
      return paramEmojiCompat;
    } 
  }
  
  public static void skipDefaultConfigurationLookup(boolean paramBoolean) {
    synchronized (CONFIG_LOCK) {
      sHasDoneDefaultConfigLookup = paramBoolean;
      return;
    } 
  }
  
  public String getAssetSignature() {
    Preconditions.checkState(isInitialized(), "Not initialized yet");
    return this.mHelper.getAssetSignature();
  }
  
  public int getEmojiSpanIndicatorColor() {
    return this.mEmojiSpanIndicatorColor;
  }
  
  public int getLoadState() {
    this.mInitLock.readLock().lock();
    try {
      return this.mLoadState;
    } finally {
      this.mInitLock.readLock().unlock();
    } 
  }
  
  public boolean hasEmojiGlyph(CharSequence paramCharSequence) {
    Preconditions.checkState(isInitialized(), "Not initialized yet");
    Preconditions.checkNotNull(paramCharSequence, "sequence cannot be null");
    return this.mHelper.hasEmojiGlyph(paramCharSequence);
  }
  
  public boolean hasEmojiGlyph(CharSequence paramCharSequence, int paramInt) {
    Preconditions.checkState(isInitialized(), "Not initialized yet");
    Preconditions.checkNotNull(paramCharSequence, "sequence cannot be null");
    return this.mHelper.hasEmojiGlyph(paramCharSequence, paramInt);
  }
  
  public boolean isEmojiSpanIndicatorEnabled() {
    return this.mEmojiSpanIndicatorEnabled;
  }
  
  public void load() {
    int i = this.mMetadataLoadStrategy;
    boolean bool = true;
    if (i != 1)
      bool = false; 
    Preconditions.checkState(bool, "Set metadataLoadStrategy to LOAD_STRATEGY_MANUAL to execute manual loading");
    if (isInitialized())
      return; 
    this.mInitLock.writeLock().lock();
    try {
      i = this.mLoadState;
      if (i == 0)
        return; 
      this.mLoadState = 0;
      this.mInitLock.writeLock().unlock();
      return;
    } finally {
      this.mInitLock.writeLock().unlock();
    } 
  }
  
  void onMetadataLoadFailed(Throwable paramThrowable) {
    ArrayList<InitCallback> arrayList = new ArrayList();
    this.mInitLock.writeLock().lock();
    try {
      this.mLoadState = 2;
      arrayList.addAll(this.mInitCallbacks);
      this.mInitCallbacks.clear();
      this.mInitLock.writeLock().unlock();
      return;
    } finally {
      this.mInitLock.writeLock().unlock();
    } 
  }
  
  void onMetadataLoadSuccess() {
    null = new ArrayList();
    this.mInitLock.writeLock().lock();
    try {
      this.mLoadState = 1;
      null.addAll(this.mInitCallbacks);
      this.mInitCallbacks.clear();
      this.mInitLock.writeLock().unlock();
      return;
    } finally {
      this.mInitLock.writeLock().unlock();
    } 
  }
  
  public CharSequence process(CharSequence paramCharSequence) {
    int i;
    if (paramCharSequence == null) {
      i = 0;
    } else {
      i = paramCharSequence.length();
    } 
    return process(paramCharSequence, 0, i);
  }
  
  public CharSequence process(CharSequence paramCharSequence, int paramInt1, int paramInt2) {
    return process(paramCharSequence, paramInt1, paramInt2, 2147483647);
  }
  
  public CharSequence process(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
    return process(paramCharSequence, paramInt1, paramInt2, paramInt3, 0);
  }
  
  public CharSequence process(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    Preconditions.checkState(isInitialized(), "Not initialized yet");
    Preconditions.checkArgumentNonnegative(paramInt1, "start cannot be negative");
    Preconditions.checkArgumentNonnegative(paramInt2, "end cannot be negative");
    Preconditions.checkArgumentNonnegative(paramInt3, "maxEmojiCount cannot be negative");
    boolean bool2 = true;
    if (paramInt1 <= paramInt2) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    Preconditions.checkArgument(bool1, "start should be <= than end");
    if (paramCharSequence == null)
      return null; 
    if (paramInt1 <= paramCharSequence.length()) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    Preconditions.checkArgument(bool1, "start should be < than charSequence length");
    if (paramInt2 <= paramCharSequence.length()) {
      bool1 = bool2;
    } else {
      bool1 = false;
    } 
    Preconditions.checkArgument(bool1, "end should be < than charSequence length");
    if (paramCharSequence.length() == 0 || paramInt1 == paramInt2)
      return paramCharSequence; 
    switch (paramInt4) {
      default:
        bool1 = this.mReplaceAll;
        return this.mHelper.process(paramCharSequence, paramInt1, paramInt2, paramInt3, bool1);
      case 2:
        bool1 = false;
        return this.mHelper.process(paramCharSequence, paramInt1, paramInt2, paramInt3, bool1);
      case 1:
        break;
    } 
    boolean bool1 = true;
    return this.mHelper.process(paramCharSequence, paramInt1, paramInt2, paramInt3, bool1);
  }
  
  public void registerInitCallback(InitCallback paramInitCallback) {
    Preconditions.checkNotNull(paramInitCallback, "initCallback cannot be null");
    this.mInitLock.writeLock().lock();
    try {
      if (this.mLoadState == 1 || this.mLoadState == 2) {
        Handler handler = this.mMainHandler;
        ListenerDispatcher listenerDispatcher = new ListenerDispatcher();
        this(paramInitCallback, this.mLoadState);
        handler.post(listenerDispatcher);
      } else {
        this.mInitCallbacks.add(paramInitCallback);
      } 
      return;
    } finally {
      this.mInitLock.writeLock().unlock();
    } 
  }
  
  public void unregisterInitCallback(InitCallback paramInitCallback) {
    Preconditions.checkNotNull(paramInitCallback, "initCallback cannot be null");
    this.mInitLock.writeLock().lock();
    try {
      this.mInitCallbacks.remove(paramInitCallback);
      return;
    } finally {
      this.mInitLock.writeLock().unlock();
    } 
  }
  
  public void updateEditorInfo(EditorInfo paramEditorInfo) {
    if (!isInitialized() || paramEditorInfo == null)
      return; 
    if (paramEditorInfo.extras == null)
      paramEditorInfo.extras = new Bundle(); 
    this.mHelper.updateEditorInfoAttrs(paramEditorInfo);
  }
  
  private static class CompatInternal {
    final EmojiCompat mEmojiCompat;
    
    CompatInternal(EmojiCompat param1EmojiCompat) {
      this.mEmojiCompat = param1EmojiCompat;
    }
    
    String getAssetSignature() {
      return "";
    }
    
    boolean hasEmojiGlyph(CharSequence param1CharSequence) {
      return false;
    }
    
    boolean hasEmojiGlyph(CharSequence param1CharSequence, int param1Int) {
      return false;
    }
    
    void loadMetadata() {
      this.mEmojiCompat.onMetadataLoadSuccess();
    }
    
    CharSequence process(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3, boolean param1Boolean) {
      return param1CharSequence;
    }
    
    void updateEditorInfoAttrs(EditorInfo param1EditorInfo) {}
  }
  
  private static final class CompatInternal19 extends CompatInternal {
    private volatile MetadataRepo mMetadataRepo;
    
    private volatile EmojiProcessor mProcessor;
    
    CompatInternal19(EmojiCompat param1EmojiCompat) {
      super(param1EmojiCompat);
    }
    
    String getAssetSignature() {
      String str = this.mMetadataRepo.getMetadataList().sourceSha();
      if (str == null)
        str = ""; 
      return str;
    }
    
    boolean hasEmojiGlyph(CharSequence param1CharSequence) {
      boolean bool;
      if (this.mProcessor.getEmojiMetadata(param1CharSequence) != null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean hasEmojiGlyph(CharSequence param1CharSequence, int param1Int) {
      boolean bool;
      EmojiMetadata emojiMetadata = this.mProcessor.getEmojiMetadata(param1CharSequence);
      if (emojiMetadata != null && emojiMetadata.getCompatAdded() <= param1Int) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    void loadMetadata() {
      try {
        EmojiCompat.MetadataRepoLoaderCallback metadataRepoLoaderCallback = new EmojiCompat.MetadataRepoLoaderCallback() {
            final EmojiCompat.CompatInternal19 this$0;
            
            public void onFailed(Throwable param2Throwable) {
              EmojiCompat.CompatInternal19.this.mEmojiCompat.onMetadataLoadFailed(param2Throwable);
            }
            
            public void onLoaded(MetadataRepo param2MetadataRepo) {
              EmojiCompat.CompatInternal19.this.onMetadataLoadSuccess(param2MetadataRepo);
            }
          };
        super(this);
        this.mEmojiCompat.mMetadataLoader.load(metadataRepoLoaderCallback);
      } finally {
        Exception exception = null;
      } 
    }
    
    void onMetadataLoadSuccess(MetadataRepo param1MetadataRepo) {
      if (param1MetadataRepo == null) {
        this.mEmojiCompat.onMetadataLoadFailed(new IllegalArgumentException("metadataRepo cannot be null"));
        return;
      } 
      this.mMetadataRepo = param1MetadataRepo;
      this.mProcessor = new EmojiProcessor(this.mMetadataRepo, new EmojiCompat.SpanFactory(), this.mEmojiCompat.mGlyphChecker, this.mEmojiCompat.mUseEmojiAsDefaultStyle, this.mEmojiCompat.mEmojiAsDefaultStyleExceptions);
      this.mEmojiCompat.onMetadataLoadSuccess();
    }
    
    CharSequence process(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3, boolean param1Boolean) {
      return this.mProcessor.process(param1CharSequence, param1Int1, param1Int2, param1Int3, param1Boolean);
    }
    
    void updateEditorInfoAttrs(EditorInfo param1EditorInfo) {
      param1EditorInfo.extras.putInt("android.support.text.emoji.emojiCompat_metadataVersion", this.mMetadataRepo.getMetadataVersion());
      param1EditorInfo.extras.putBoolean("android.support.text.emoji.emojiCompat_replaceAll", this.mEmojiCompat.mReplaceAll);
    }
  }
  
  class null extends MetadataRepoLoaderCallback {
    final EmojiCompat.CompatInternal19 this$0;
    
    public void onFailed(Throwable param1Throwable) {
      this.this$0.mEmojiCompat.onMetadataLoadFailed(param1Throwable);
    }
    
    public void onLoaded(MetadataRepo param1MetadataRepo) {
      this.this$0.onMetadataLoadSuccess(param1MetadataRepo);
    }
  }
  
  public static abstract class Config {
    int[] mEmojiAsDefaultStyleExceptions;
    
    int mEmojiSpanIndicatorColor = -16711936;
    
    boolean mEmojiSpanIndicatorEnabled;
    
    EmojiCompat.GlyphChecker mGlyphChecker = new EmojiProcessor.DefaultGlyphChecker();
    
    Set<EmojiCompat.InitCallback> mInitCallbacks;
    
    int mMetadataLoadStrategy = 0;
    
    final EmojiCompat.MetadataRepoLoader mMetadataLoader;
    
    boolean mReplaceAll;
    
    boolean mUseEmojiAsDefaultStyle;
    
    protected Config(EmojiCompat.MetadataRepoLoader param1MetadataRepoLoader) {
      Preconditions.checkNotNull(param1MetadataRepoLoader, "metadataLoader cannot be null.");
      this.mMetadataLoader = param1MetadataRepoLoader;
    }
    
    protected final EmojiCompat.MetadataRepoLoader getMetadataRepoLoader() {
      return this.mMetadataLoader;
    }
    
    public Config registerInitCallback(EmojiCompat.InitCallback param1InitCallback) {
      Preconditions.checkNotNull(param1InitCallback, "initCallback cannot be null");
      if (this.mInitCallbacks == null)
        this.mInitCallbacks = (Set<EmojiCompat.InitCallback>)new ArraySet(); 
      this.mInitCallbacks.add(param1InitCallback);
      return this;
    }
    
    public Config setEmojiSpanIndicatorColor(int param1Int) {
      this.mEmojiSpanIndicatorColor = param1Int;
      return this;
    }
    
    public Config setEmojiSpanIndicatorEnabled(boolean param1Boolean) {
      this.mEmojiSpanIndicatorEnabled = param1Boolean;
      return this;
    }
    
    public Config setGlyphChecker(EmojiCompat.GlyphChecker param1GlyphChecker) {
      Preconditions.checkNotNull(param1GlyphChecker, "GlyphChecker cannot be null");
      this.mGlyphChecker = param1GlyphChecker;
      return this;
    }
    
    public Config setMetadataLoadStrategy(int param1Int) {
      this.mMetadataLoadStrategy = param1Int;
      return this;
    }
    
    public Config setReplaceAll(boolean param1Boolean) {
      this.mReplaceAll = param1Boolean;
      return this;
    }
    
    public Config setUseEmojiAsDefaultStyle(boolean param1Boolean) {
      return setUseEmojiAsDefaultStyle(param1Boolean, null);
    }
    
    public Config setUseEmojiAsDefaultStyle(boolean param1Boolean, List<Integer> param1List) {
      this.mUseEmojiAsDefaultStyle = param1Boolean;
      if (param1Boolean && param1List != null) {
        this.mEmojiAsDefaultStyleExceptions = new int[param1List.size()];
        byte b = 0;
        for (Integer integer : param1List) {
          this.mEmojiAsDefaultStyleExceptions[b] = integer.intValue();
          b++;
        } 
        Arrays.sort(this.mEmojiAsDefaultStyleExceptions);
      } else {
        this.mEmojiAsDefaultStyleExceptions = null;
      } 
      return this;
    }
    
    public Config unregisterInitCallback(EmojiCompat.InitCallback param1InitCallback) {
      Preconditions.checkNotNull(param1InitCallback, "initCallback cannot be null");
      Set<EmojiCompat.InitCallback> set = this.mInitCallbacks;
      if (set != null)
        set.remove(param1InitCallback); 
      return this;
    }
  }
  
  public static interface GlyphChecker {
    boolean hasGlyph(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3);
  }
  
  public static abstract class InitCallback {
    public void onFailed(Throwable param1Throwable) {}
    
    public void onInitialized() {}
  }
  
  private static class ListenerDispatcher implements Runnable {
    private final List<EmojiCompat.InitCallback> mInitCallbacks;
    
    private final int mLoadState;
    
    private final Throwable mThrowable;
    
    ListenerDispatcher(EmojiCompat.InitCallback param1InitCallback, int param1Int) {
      this(Arrays.asList(new EmojiCompat.InitCallback[] { (EmojiCompat.InitCallback)Preconditions.checkNotNull(param1InitCallback, "initCallback cannot be null") }), param1Int, null);
    }
    
    ListenerDispatcher(Collection<EmojiCompat.InitCallback> param1Collection, int param1Int) {
      this(param1Collection, param1Int, null);
    }
    
    ListenerDispatcher(Collection<EmojiCompat.InitCallback> param1Collection, int param1Int, Throwable param1Throwable) {
      Preconditions.checkNotNull(param1Collection, "initCallbacks cannot be null");
      this.mInitCallbacks = new ArrayList<>(param1Collection);
      this.mLoadState = param1Int;
      this.mThrowable = param1Throwable;
    }
    
    public void run() {
      byte b;
      int i = this.mInitCallbacks.size();
      switch (this.mLoadState) {
        default:
          b = 0;
          break;
        case 1:
          for (b = 0; b < i; b++)
            ((EmojiCompat.InitCallback)this.mInitCallbacks.get(b)).onInitialized(); 
          return;
      } 
      while (b < i) {
        ((EmojiCompat.InitCallback)this.mInitCallbacks.get(b)).onFailed(this.mThrowable);
        b++;
      } 
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface LoadStrategy {}
  
  public static interface MetadataRepoLoader {
    void load(EmojiCompat.MetadataRepoLoaderCallback param1MetadataRepoLoaderCallback);
  }
  
  public static abstract class MetadataRepoLoaderCallback {
    public abstract void onFailed(Throwable param1Throwable);
    
    public abstract void onLoaded(MetadataRepo param1MetadataRepo);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ReplaceStrategy {}
  
  static class SpanFactory {
    EmojiSpan createSpan(EmojiMetadata param1EmojiMetadata) {
      return new TypefaceEmojiSpan(param1EmojiMetadata);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\EmojiCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */