package com.bumptech.glide.request;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.drawable.DrawableDecoderCompat;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.request.transition.TransitionFactory;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public final class SingleRequest<R> implements Request, SizeReadyCallback, ResourceCallback {
  private static final String GLIDE_TAG = "Glide";
  
  private static final boolean IS_VERBOSE_LOGGABLE = Log.isLoggable("Request", 2);
  
  private static final String TAG = "Request";
  
  private final TransitionFactory<? super R> animationFactory;
  
  private final Executor callbackExecutor;
  
  private final Context context;
  
  private volatile Engine engine;
  
  private Drawable errorDrawable;
  
  private Drawable fallbackDrawable;
  
  private final GlideContext glideContext;
  
  private int height;
  
  private boolean isCallingCallbacks;
  
  private Engine.LoadStatus loadStatus;
  
  private final Object model;
  
  private final int overrideHeight;
  
  private final int overrideWidth;
  
  private Drawable placeholderDrawable;
  
  private final Priority priority;
  
  private final RequestCoordinator requestCoordinator;
  
  private final List<RequestListener<R>> requestListeners;
  
  private final Object requestLock;
  
  private final BaseRequestOptions<?> requestOptions;
  
  private RuntimeException requestOrigin;
  
  private Resource<R> resource;
  
  private long startTime;
  
  private final StateVerifier stateVerifier;
  
  private Status status;
  
  private final String tag;
  
  private final Target<R> target;
  
  private final RequestListener<R> targetListener;
  
  private final Class<R> transcodeClass;
  
  private int width;
  
  private SingleRequest(Context paramContext, GlideContext paramGlideContext, Object paramObject1, Object paramObject2, Class<R> paramClass, BaseRequestOptions<?> paramBaseRequestOptions, int paramInt1, int paramInt2, Priority paramPriority, Target<R> paramTarget, RequestListener<R> paramRequestListener, List<RequestListener<R>> paramList, RequestCoordinator paramRequestCoordinator, Engine paramEngine, TransitionFactory<? super R> paramTransitionFactory, Executor paramExecutor) {
    String str;
    if (IS_VERBOSE_LOGGABLE) {
      str = String.valueOf(hashCode());
    } else {
      str = null;
    } 
    this.tag = str;
    this.stateVerifier = StateVerifier.newInstance();
    this.requestLock = paramObject1;
    this.context = paramContext;
    this.glideContext = paramGlideContext;
    this.model = paramObject2;
    this.transcodeClass = paramClass;
    this.requestOptions = paramBaseRequestOptions;
    this.overrideWidth = paramInt1;
    this.overrideHeight = paramInt2;
    this.priority = paramPriority;
    this.target = paramTarget;
    this.targetListener = paramRequestListener;
    this.requestListeners = paramList;
    this.requestCoordinator = paramRequestCoordinator;
    this.engine = paramEngine;
    this.animationFactory = paramTransitionFactory;
    this.callbackExecutor = paramExecutor;
    this.status = Status.PENDING;
    if (this.requestOrigin == null && paramGlideContext.getExperiments().isEnabled(GlideBuilder.LogRequestOrigins.class))
      this.requestOrigin = new RuntimeException("Glide request origin trace"); 
  }
  
  private void assertNotCallingCallbacks() {
    if (!this.isCallingCallbacks)
      return; 
    throw new IllegalStateException("You can't start or clear loads in RequestListener or Target callbacks. If you're trying to start a fallback request when a load fails, use RequestBuilder#error(RequestBuilder). Otherwise consider posting your into() or clear() calls to the main thread using a Handler instead.");
  }
  
  private boolean canNotifyCleared() {
    RequestCoordinator requestCoordinator = this.requestCoordinator;
    return (requestCoordinator == null || requestCoordinator.canNotifyCleared(this));
  }
  
  private boolean canNotifyStatusChanged() {
    RequestCoordinator requestCoordinator = this.requestCoordinator;
    return (requestCoordinator == null || requestCoordinator.canNotifyStatusChanged(this));
  }
  
  private boolean canSetResource() {
    RequestCoordinator requestCoordinator = this.requestCoordinator;
    return (requestCoordinator == null || requestCoordinator.canSetImage(this));
  }
  
  private void cancel() {
    assertNotCallingCallbacks();
    this.stateVerifier.throwIfRecycled();
    this.target.removeCallback(this);
    Engine.LoadStatus loadStatus = this.loadStatus;
    if (loadStatus != null) {
      loadStatus.cancel();
      this.loadStatus = null;
    } 
  }
  
  private Drawable getErrorDrawable() {
    if (this.errorDrawable == null) {
      Drawable drawable = this.requestOptions.getErrorPlaceholder();
      this.errorDrawable = drawable;
      if (drawable == null && this.requestOptions.getErrorId() > 0)
        this.errorDrawable = loadDrawable(this.requestOptions.getErrorId()); 
    } 
    return this.errorDrawable;
  }
  
  private Drawable getFallbackDrawable() {
    if (this.fallbackDrawable == null) {
      Drawable drawable = this.requestOptions.getFallbackDrawable();
      this.fallbackDrawable = drawable;
      if (drawable == null && this.requestOptions.getFallbackId() > 0)
        this.fallbackDrawable = loadDrawable(this.requestOptions.getFallbackId()); 
    } 
    return this.fallbackDrawable;
  }
  
  private Drawable getPlaceholderDrawable() {
    if (this.placeholderDrawable == null) {
      Drawable drawable = this.requestOptions.getPlaceholderDrawable();
      this.placeholderDrawable = drawable;
      if (drawable == null && this.requestOptions.getPlaceholderId() > 0)
        this.placeholderDrawable = loadDrawable(this.requestOptions.getPlaceholderId()); 
    } 
    return this.placeholderDrawable;
  }
  
  private boolean isFirstReadyResource() {
    RequestCoordinator requestCoordinator = this.requestCoordinator;
    return (requestCoordinator == null || !requestCoordinator.getRoot().isAnyResourceSet());
  }
  
  private Drawable loadDrawable(int paramInt) {
    Resources.Theme theme;
    if (this.requestOptions.getTheme() != null) {
      theme = this.requestOptions.getTheme();
    } else {
      theme = this.context.getTheme();
    } 
    return DrawableDecoderCompat.getDrawable((Context)this.glideContext, paramInt, theme);
  }
  
  private void logV(String paramString) {
    Log.v("Request", paramString + " this: " + this.tag);
  }
  
  private static int maybeApplySizeMultiplier(int paramInt, float paramFloat) {
    if (paramInt != Integer.MIN_VALUE)
      paramInt = Math.round(paramInt * paramFloat); 
    return paramInt;
  }
  
  private void notifyLoadFailed() {
    RequestCoordinator requestCoordinator = this.requestCoordinator;
    if (requestCoordinator != null)
      requestCoordinator.onRequestFailed(this); 
  }
  
  private void notifyLoadSuccess() {
    RequestCoordinator requestCoordinator = this.requestCoordinator;
    if (requestCoordinator != null)
      requestCoordinator.onRequestSuccess(this); 
  }
  
  public static <R> SingleRequest<R> obtain(Context paramContext, GlideContext paramGlideContext, Object paramObject1, Object paramObject2, Class<R> paramClass, BaseRequestOptions<?> paramBaseRequestOptions, int paramInt1, int paramInt2, Priority paramPriority, Target<R> paramTarget, RequestListener<R> paramRequestListener, List<RequestListener<R>> paramList, RequestCoordinator paramRequestCoordinator, Engine paramEngine, TransitionFactory<? super R> paramTransitionFactory, Executor paramExecutor) {
    return new SingleRequest<>(paramContext, paramGlideContext, paramObject1, paramObject2, paramClass, paramBaseRequestOptions, paramInt1, paramInt2, paramPriority, paramTarget, paramRequestListener, paramList, paramRequestCoordinator, paramEngine, paramTransitionFactory, paramExecutor);
  }
  
  private void onLoadFailed(GlideException paramGlideException, int paramInt) {
    this.stateVerifier.throwIfRecycled();
    synchronized (this.requestLock) {
      paramGlideException.setOrigin(this.requestOrigin);
      int i = this.glideContext.getLogLevel();
      if (i <= paramInt) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        Log.w("Glide", stringBuilder.append("Load failed for ").append(this.model).append(" with size [").append(this.width).append("x").append(this.height).append("]").toString(), (Throwable)paramGlideException);
        if (i <= 4)
          paramGlideException.logRootCauses("Glide"); 
      } 
      this.loadStatus = null;
      this.status = Status.FAILED;
      boolean bool = true;
      this.isCallingCallbacks = true;
      i = 0;
      paramInt = 0;
      try {
        List<RequestListener<R>> list = this.requestListeners;
        if (list != null) {
          Iterator<RequestListener<R>> iterator = list.iterator();
          while (true) {
            i = paramInt;
            if (iterator.hasNext()) {
              boolean bool1 = paramInt | ((RequestListener<R>)iterator.next()).onLoadFailed(paramGlideException, this.model, this.target, isFirstReadyResource());
              continue;
            } 
            break;
          } 
        } 
        RequestListener<R> requestListener = this.targetListener;
        if (requestListener != null && requestListener.onLoadFailed(paramGlideException, this.model, this.target, isFirstReadyResource())) {
          paramInt = bool;
        } else {
          paramInt = 0;
        } 
        if ((paramInt | i) == 0)
          setErrorPlaceholder(); 
        this.isCallingCallbacks = false;
        return;
      } finally {
        this.isCallingCallbacks = false;
      } 
    } 
  }
  
  private void onResourceReady(Resource<R> paramResource, R paramR, DataSource paramDataSource, boolean paramBoolean) {
    paramBoolean = isFirstReadyResource();
    this.status = Status.COMPLETE;
    this.resource = paramResource;
    if (this.glideContext.getLogLevel() <= 3)
      Log.d("Glide", "Finished loading " + paramR.getClass().getSimpleName() + " from " + paramDataSource + " for " + this.model + " with size [" + this.width + "x" + this.height + "] in " + LogTime.getElapsedMillis(this.startTime) + " ms"); 
    boolean bool = true;
    this.isCallingCallbacks = true;
    try {
      boolean bool1;
      boolean bool2;
      List<RequestListener<R>> list = this.requestListeners;
      if (list != null) {
        Iterator<RequestListener<R>> iterator = list.iterator();
        bool1 = false;
        while (true) {
          bool2 = bool1;
          if (iterator.hasNext()) {
            bool1 |= ((RequestListener<R>)iterator.next()).onResourceReady(paramR, this.model, this.target, paramDataSource, paramBoolean);
            continue;
          } 
          break;
        } 
      } else {
        bool2 = false;
      } 
      RequestListener<R> requestListener = this.targetListener;
      if (requestListener != null && requestListener.onResourceReady(paramR, this.model, this.target, paramDataSource, paramBoolean)) {
        bool1 = bool;
      } else {
        bool1 = false;
      } 
      if ((bool2 | bool1) == 0) {
        Transition transition = this.animationFactory.build(paramDataSource, paramBoolean);
        this.target.onResourceReady(paramR, transition);
      } 
      this.isCallingCallbacks = false;
      return;
    } finally {
      this.isCallingCallbacks = false;
    } 
  }
  
  private void setErrorPlaceholder() {
    if (!canNotifyStatusChanged())
      return; 
    Drawable drawable2 = null;
    if (this.model == null)
      drawable2 = getFallbackDrawable(); 
    Drawable drawable1 = drawable2;
    if (drawable2 == null)
      drawable1 = getErrorDrawable(); 
    drawable2 = drawable1;
    if (drawable1 == null)
      drawable2 = getPlaceholderDrawable(); 
    this.target.onLoadFailed(drawable2);
  }
  
  public void begin() {
    synchronized (this.requestLock) {
      assertNotCallingCallbacks();
      this.stateVerifier.throwIfRecycled();
      this.startTime = LogTime.getLogTime();
      if (this.model == null) {
        byte b;
        if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
          this.width = this.overrideWidth;
          this.height = this.overrideHeight;
        } 
        if (getFallbackDrawable() == null) {
          b = 5;
        } else {
          b = 3;
        } 
        GlideException glideException = new GlideException();
        this("Received null model");
        onLoadFailed(glideException, b);
        return;
      } 
      if (this.status != Status.RUNNING) {
        if (this.status == Status.COMPLETE) {
          onResourceReady(this.resource, DataSource.MEMORY_CACHE, false);
          return;
        } 
        this.status = Status.WAITING_FOR_SIZE;
        if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
          onSizeReady(this.overrideWidth, this.overrideHeight);
        } else {
          this.target.getSize(this);
        } 
        if ((this.status == Status.RUNNING || this.status == Status.WAITING_FOR_SIZE) && canNotifyStatusChanged())
          this.target.onLoadStarted(getPlaceholderDrawable()); 
        if (IS_VERBOSE_LOGGABLE) {
          StringBuilder stringBuilder = new StringBuilder();
          this();
          logV(stringBuilder.append("finished run method in ").append(LogTime.getElapsedMillis(this.startTime)).toString());
        } 
        return;
      } 
      IllegalArgumentException illegalArgumentException = new IllegalArgumentException();
      this("Cannot restart a running request");
      throw illegalArgumentException;
    } 
  }
  
  public void clear() {
    null = null;
    synchronized (this.requestLock) {
      assertNotCallingCallbacks();
      this.stateVerifier.throwIfRecycled();
      if (this.status == Status.CLEARED)
        return; 
      cancel();
      Resource<R> resource = this.resource;
      if (resource != null) {
        null = resource;
        this.resource = null;
      } 
      if (canNotifyCleared())
        this.target.onLoadCleared(getPlaceholderDrawable()); 
      this.status = Status.CLEARED;
      if (null != null)
        this.engine.release(null); 
      return;
    } 
  }
  
  public Object getLock() {
    this.stateVerifier.throwIfRecycled();
    return this.requestLock;
  }
  
  public boolean isAnyResourceSet() {
    synchronized (this.requestLock) {
      boolean bool;
      if (this.status == Status.COMPLETE) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
  }
  
  public boolean isCleared() {
    synchronized (this.requestLock) {
      boolean bool;
      if (this.status == Status.CLEARED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
  }
  
  public boolean isComplete() {
    synchronized (this.requestLock) {
      boolean bool;
      if (this.status == Status.COMPLETE) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
  }
  
  public boolean isEquivalentTo(Request paramRequest) {
    if (!(paramRequest instanceof SingleRequest))
      return false; 
    synchronized (this.requestLock) {
      boolean bool;
      int i = this.overrideWidth;
      int j = this.overrideHeight;
      Object object = this.model;
      Class<R> clazz = this.transcodeClass;
      null = this.requestOptions;
      Priority priority = this.priority;
      List<RequestListener<R>> list = this.requestListeners;
      if (list != null) {
        bool = list.size();
      } else {
        bool = false;
      } 
      SingleRequest singleRequest = (SingleRequest)paramRequest;
      synchronized (singleRequest.requestLock) {
        boolean bool1;
        boolean bool2;
        int k = singleRequest.overrideWidth;
        int m = singleRequest.overrideHeight;
        Object object1 = singleRequest.model;
        null = (Object<R>)singleRequest.transcodeClass;
        BaseRequestOptions<?> baseRequestOptions = singleRequest.requestOptions;
        Priority priority1 = singleRequest.priority;
        List<RequestListener<R>> list1 = singleRequest.requestListeners;
        if (list1 != null) {
          bool1 = list1.size();
        } else {
          bool1 = false;
        } 
        if (i == k && j == m && Util.bothModelsNullEquivalentOrEquals(object, object1) && clazz.equals(null) && null.equals(baseRequestOptions) && priority == priority1 && bool == bool1) {
          bool2 = true;
        } else {
          bool2 = false;
        } 
        return bool2;
      } 
    } 
  }
  
  public boolean isRunning() {
    synchronized (this.requestLock) {
      if (this.status == Status.RUNNING || this.status == Status.WAITING_FOR_SIZE)
        return true; 
      return false;
    } 
  }
  
  public void onLoadFailed(GlideException paramGlideException) {
    onLoadFailed(paramGlideException, 5);
  }
  
  public void onResourceReady(Resource<?> paramResource, DataSource paramDataSource, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield stateVerifier : Lcom/bumptech/glide/util/pool/StateVerifier;
    //   4: invokevirtual throwIfRecycled : ()V
    //   7: aconst_null
    //   8: astore #6
    //   10: aconst_null
    //   11: astore #5
    //   13: aload #6
    //   15: astore #4
    //   17: aload_0
    //   18: getfield requestLock : Ljava/lang/Object;
    //   21: astore #7
    //   23: aload #6
    //   25: astore #4
    //   27: aload #7
    //   29: monitorenter
    //   30: aload #5
    //   32: astore #4
    //   34: aload_0
    //   35: aconst_null
    //   36: putfield loadStatus : Lcom/bumptech/glide/load/engine/Engine$LoadStatus;
    //   39: aload_1
    //   40: ifnonnull -> 127
    //   43: aload #5
    //   45: astore #4
    //   47: new com/bumptech/glide/load/engine/GlideException
    //   50: astore_1
    //   51: aload #5
    //   53: astore #4
    //   55: new java/lang/StringBuilder
    //   58: astore_2
    //   59: aload #5
    //   61: astore #4
    //   63: aload_2
    //   64: invokespecial <init> : ()V
    //   67: aload #5
    //   69: astore #4
    //   71: aload_1
    //   72: aload_2
    //   73: ldc_w 'Expected to receive a Resource<R> with an object of '
    //   76: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   79: aload_0
    //   80: getfield transcodeClass : Ljava/lang/Class;
    //   83: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   86: ldc_w ' inside, but instead got null.'
    //   89: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   92: invokevirtual toString : ()Ljava/lang/String;
    //   95: invokespecial <init> : (Ljava/lang/String;)V
    //   98: aload #5
    //   100: astore #4
    //   102: aload_0
    //   103: aload_1
    //   104: invokevirtual onLoadFailed : (Lcom/bumptech/glide/load/engine/GlideException;)V
    //   107: aload #5
    //   109: astore #4
    //   111: aload #7
    //   113: monitorexit
    //   114: iconst_0
    //   115: ifeq -> 126
    //   118: aload_0
    //   119: getfield engine : Lcom/bumptech/glide/load/engine/Engine;
    //   122: aconst_null
    //   123: invokevirtual release : (Lcom/bumptech/glide/load/engine/Resource;)V
    //   126: return
    //   127: aload #5
    //   129: astore #4
    //   131: aload_1
    //   132: invokeinterface get : ()Ljava/lang/Object;
    //   137: astore #8
    //   139: aload #8
    //   141: ifnull -> 247
    //   144: aload #5
    //   146: astore #4
    //   148: aload_0
    //   149: getfield transcodeClass : Ljava/lang/Class;
    //   152: aload #8
    //   154: invokevirtual getClass : ()Ljava/lang/Class;
    //   157: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   160: ifne -> 166
    //   163: goto -> 247
    //   166: aload #5
    //   168: astore #4
    //   170: aload_0
    //   171: invokespecial canSetResource : ()Z
    //   174: ifne -> 214
    //   177: aload_1
    //   178: astore #4
    //   180: aload_0
    //   181: aconst_null
    //   182: putfield resource : Lcom/bumptech/glide/load/engine/Resource;
    //   185: aload_1
    //   186: astore #4
    //   188: aload_0
    //   189: getstatic com/bumptech/glide/request/SingleRequest$Status.COMPLETE : Lcom/bumptech/glide/request/SingleRequest$Status;
    //   192: putfield status : Lcom/bumptech/glide/request/SingleRequest$Status;
    //   195: aload_1
    //   196: astore #4
    //   198: aload #7
    //   200: monitorexit
    //   201: aload_1
    //   202: ifnull -> 213
    //   205: aload_0
    //   206: getfield engine : Lcom/bumptech/glide/load/engine/Engine;
    //   209: aload_1
    //   210: invokevirtual release : (Lcom/bumptech/glide/load/engine/Resource;)V
    //   213: return
    //   214: aload #5
    //   216: astore #4
    //   218: aload_0
    //   219: aload_1
    //   220: aload #8
    //   222: aload_2
    //   223: iload_3
    //   224: invokespecial onResourceReady : (Lcom/bumptech/glide/load/engine/Resource;Ljava/lang/Object;Lcom/bumptech/glide/load/DataSource;Z)V
    //   227: aload #5
    //   229: astore #4
    //   231: aload #7
    //   233: monitorexit
    //   234: iconst_0
    //   235: ifeq -> 246
    //   238: aload_0
    //   239: getfield engine : Lcom/bumptech/glide/load/engine/Engine;
    //   242: aconst_null
    //   243: invokevirtual release : (Lcom/bumptech/glide/load/engine/Resource;)V
    //   246: return
    //   247: aload_1
    //   248: astore_2
    //   249: aload_2
    //   250: astore #4
    //   252: aload_0
    //   253: aconst_null
    //   254: putfield resource : Lcom/bumptech/glide/load/engine/Resource;
    //   257: aload_2
    //   258: astore #4
    //   260: new com/bumptech/glide/load/engine/GlideException
    //   263: astore #6
    //   265: aload_2
    //   266: astore #4
    //   268: new java/lang/StringBuilder
    //   271: astore #5
    //   273: aload_2
    //   274: astore #4
    //   276: aload #5
    //   278: invokespecial <init> : ()V
    //   281: aload_2
    //   282: astore #4
    //   284: aload #5
    //   286: ldc_w 'Expected to receive an object of '
    //   289: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   292: aload_0
    //   293: getfield transcodeClass : Ljava/lang/Class;
    //   296: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   299: ldc_w ' but instead got '
    //   302: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   305: astore #9
    //   307: aload #8
    //   309: ifnull -> 325
    //   312: aload_2
    //   313: astore #4
    //   315: aload #8
    //   317: invokevirtual getClass : ()Ljava/lang/Class;
    //   320: astore #5
    //   322: goto -> 330
    //   325: ldc_w ''
    //   328: astore #5
    //   330: aload_2
    //   331: astore #4
    //   333: aload #9
    //   335: aload #5
    //   337: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   340: ldc_w '{'
    //   343: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   346: aload #8
    //   348: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   351: ldc_w '} inside Resource{'
    //   354: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   357: aload_1
    //   358: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   361: ldc_w '}.'
    //   364: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   367: astore #5
    //   369: aload #8
    //   371: ifnull -> 381
    //   374: ldc_w ''
    //   377: astore_1
    //   378: goto -> 385
    //   381: ldc_w ' To indicate failure return a null Resource object, rather than a Resource object containing null data.'
    //   384: astore_1
    //   385: aload_2
    //   386: astore #4
    //   388: aload #6
    //   390: aload #5
    //   392: aload_1
    //   393: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   396: invokevirtual toString : ()Ljava/lang/String;
    //   399: invokespecial <init> : (Ljava/lang/String;)V
    //   402: aload_2
    //   403: astore #4
    //   405: aload_0
    //   406: aload #6
    //   408: invokevirtual onLoadFailed : (Lcom/bumptech/glide/load/engine/GlideException;)V
    //   411: aload_2
    //   412: astore #4
    //   414: aload #7
    //   416: monitorexit
    //   417: aload_2
    //   418: ifnull -> 429
    //   421: aload_0
    //   422: getfield engine : Lcom/bumptech/glide/load/engine/Engine;
    //   425: aload_2
    //   426: invokevirtual release : (Lcom/bumptech/glide/load/engine/Resource;)V
    //   429: return
    //   430: astore_1
    //   431: aload #7
    //   433: monitorexit
    //   434: aload_1
    //   435: athrow
    //   436: astore_1
    //   437: aload #4
    //   439: ifnull -> 451
    //   442: aload_0
    //   443: getfield engine : Lcom/bumptech/glide/load/engine/Engine;
    //   446: aload #4
    //   448: invokevirtual release : (Lcom/bumptech/glide/load/engine/Resource;)V
    //   451: aload_1
    //   452: athrow
    // Exception table:
    //   from	to	target	type
    //   17	23	436	finally
    //   27	30	436	finally
    //   34	39	430	finally
    //   47	51	430	finally
    //   55	59	430	finally
    //   63	67	430	finally
    //   71	98	430	finally
    //   102	107	430	finally
    //   111	114	430	finally
    //   131	139	430	finally
    //   148	163	430	finally
    //   170	177	430	finally
    //   180	185	430	finally
    //   188	195	430	finally
    //   198	201	430	finally
    //   218	227	430	finally
    //   231	234	430	finally
    //   252	257	430	finally
    //   260	265	430	finally
    //   268	273	430	finally
    //   276	281	430	finally
    //   284	307	430	finally
    //   315	322	430	finally
    //   333	369	430	finally
    //   388	402	430	finally
    //   405	411	430	finally
    //   414	417	430	finally
    //   431	434	430	finally
    //   434	436	436	finally
  }
  
  public void onSizeReady(int paramInt1, int paramInt2) {
    Exception exception;
    this.stateVerifier.throwIfRecycled();
    Object object1 = this.requestLock;
    /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
    try {
      boolean bool4 = IS_VERBOSE_LOGGABLE;
      if (bool4) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        logV(stringBuilder.append("Got onSizeReady in ").append(LogTime.getElapsedMillis(this.startTime)).toString());
      } 
      if (this.status != Status.WAITING_FOR_SIZE) {
        /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
        return;
      } 
      this.status = Status.RUNNING;
      float f = this.requestOptions.getSizeMultiplier();
      this.width = maybeApplySizeMultiplier(paramInt1, f);
      this.height = maybeApplySizeMultiplier(paramInt2, f);
      if (bool4) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        logV(stringBuilder.append("finished setup for calling load in ").append(LogTime.getElapsedMillis(this.startTime)).toString());
      } 
      Engine engine = this.engine;
      GlideContext glideContext = this.glideContext;
      Object object = this.model;
      Key key = this.requestOptions.getSignature();
      paramInt1 = this.width;
      paramInt2 = this.height;
      Class<?> clazz1 = this.requestOptions.getResourceClass();
      Class<R> clazz = this.transcodeClass;
      Priority priority = this.priority;
      DiskCacheStrategy diskCacheStrategy = this.requestOptions.getDiskCacheStrategy();
      Map<Class<?>, Transformation<?>> map = this.requestOptions.getTransformations();
      boolean bool2 = this.requestOptions.isTransformationRequired();
      boolean bool1 = this.requestOptions.isScaleOnlyOrNoTransform();
      Options options = this.requestOptions.getOptions();
      boolean bool5 = this.requestOptions.isMemoryCacheable();
      boolean bool3 = this.requestOptions.getUseUnlimitedSourceGeneratorsPool();
      boolean bool7 = this.requestOptions.getUseAnimationPool();
      boolean bool6 = this.requestOptions.getOnlyRetrieveFromCache();
      Executor executor = this.callbackExecutor;
      try {
        Engine.LoadStatus loadStatus = engine.load(glideContext, object, key, paramInt1, paramInt2, clazz1, clazz, priority, diskCacheStrategy, map, bool2, bool1, options, bool5, bool3, bool7, bool6, this, executor);
        Object object3 = object1;
        try {
          this.loadStatus = loadStatus;
          object3 = object1;
          if (this.status != Status.RUNNING) {
            object3 = object1;
            this.loadStatus = null;
          } 
          if (bool4) {
            object3 = object1;
            StringBuilder stringBuilder = new StringBuilder();
            object3 = object1;
            this();
            object3 = object1;
            logV(stringBuilder.append("finished onSizeReady in ").append(LogTime.getElapsedMillis(this.startTime)).toString());
          } 
          object3 = object1;
          return;
        } finally {
          loadStatus = null;
        } 
      } finally {}
    } finally {}
    Object object2 = object1;
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
    throw exception;
  }
  
  public void pause() {
    synchronized (this.requestLock) {
      if (isRunning())
        clear(); 
      return;
    } 
  }
  
  private enum Status {
    CLEARED, COMPLETE, FAILED, PENDING, RUNNING, WAITING_FOR_SIZE;
    
    private static final Status[] $VALUES;
    
    static {
      Status status3 = new Status("PENDING", 0);
      PENDING = status3;
      Status status1 = new Status("RUNNING", 1);
      RUNNING = status1;
      Status status5 = new Status("WAITING_FOR_SIZE", 2);
      WAITING_FOR_SIZE = status5;
      Status status2 = new Status("COMPLETE", 3);
      COMPLETE = status2;
      Status status6 = new Status("FAILED", 4);
      FAILED = status6;
      Status status4 = new Status("CLEARED", 5);
      CLEARED = status4;
      $VALUES = new Status[] { status3, status1, status5, status2, status6, status4 };
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\request\SingleRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */