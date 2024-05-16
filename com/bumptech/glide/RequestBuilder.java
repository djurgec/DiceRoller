package com.bumptech.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.ErrorRequestCoordinator;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestCoordinator;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.SingleRequest;
import com.bumptech.glide.request.ThumbnailRequestCoordinator;
import com.bumptech.glide.request.target.PreloadTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.signature.AndroidResourceSignature;
import com.bumptech.glide.util.Executors;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

public class RequestBuilder<TranscodeType> extends BaseRequestOptions<RequestBuilder<TranscodeType>> implements Cloneable, ModelTypes<RequestBuilder<TranscodeType>> {
  protected static final RequestOptions DOWNLOAD_ONLY_OPTIONS = (RequestOptions)((RequestOptions)((RequestOptions)(new RequestOptions()).diskCacheStrategy(DiskCacheStrategy.DATA)).priority(Priority.LOW)).skipMemoryCache(true);
  
  private final Context context;
  
  private RequestBuilder<TranscodeType> errorBuilder;
  
  private final Glide glide;
  
  private final GlideContext glideContext;
  
  private boolean isDefaultTransitionOptionsSet = true;
  
  private boolean isModelSet;
  
  private boolean isThumbnailBuilt;
  
  private Object model;
  
  private List<RequestListener<TranscodeType>> requestListeners;
  
  private final RequestManager requestManager;
  
  private Float thumbSizeMultiplier;
  
  private RequestBuilder<TranscodeType> thumbnailBuilder;
  
  private final Class<TranscodeType> transcodeClass;
  
  private TransitionOptions<?, ? super TranscodeType> transitionOptions;
  
  protected RequestBuilder(Glide paramGlide, RequestManager paramRequestManager, Class<TranscodeType> paramClass, Context paramContext) {
    this.glide = paramGlide;
    this.requestManager = paramRequestManager;
    this.transcodeClass = paramClass;
    this.context = paramContext;
    this.transitionOptions = paramRequestManager.getDefaultTransitionOptions(paramClass);
    this.glideContext = paramGlide.getGlideContext();
    initRequestListeners(paramRequestManager.getDefaultRequestListeners());
    apply((BaseRequestOptions<?>)paramRequestManager.getDefaultRequestOptions());
  }
  
  protected RequestBuilder(Class<TranscodeType> paramClass, RequestBuilder<?> paramRequestBuilder) {
    this(paramRequestBuilder.glide, paramRequestBuilder.requestManager, paramClass, paramRequestBuilder.context);
    this.model = paramRequestBuilder.model;
    this.isModelSet = paramRequestBuilder.isModelSet;
    apply(paramRequestBuilder);
  }
  
  private Request buildRequest(Target<TranscodeType> paramTarget, RequestListener<TranscodeType> paramRequestListener, BaseRequestOptions<?> paramBaseRequestOptions, Executor paramExecutor) {
    return buildRequestRecursive(new Object(), paramTarget, paramRequestListener, (RequestCoordinator)null, this.transitionOptions, paramBaseRequestOptions.getPriority(), paramBaseRequestOptions.getOverrideWidth(), paramBaseRequestOptions.getOverrideHeight(), paramBaseRequestOptions, paramExecutor);
  }
  
  private Request buildRequestRecursive(Object paramObject, Target<TranscodeType> paramTarget, RequestListener<TranscodeType> paramRequestListener, RequestCoordinator paramRequestCoordinator, TransitionOptions<?, ? super TranscodeType> paramTransitionOptions, Priority paramPriority, int paramInt1, int paramInt2, BaseRequestOptions<?> paramBaseRequestOptions, Executor paramExecutor) {
    ErrorRequestCoordinator errorRequestCoordinator1;
    ErrorRequestCoordinator errorRequestCoordinator2;
    if (this.errorBuilder != null) {
      errorRequestCoordinator2 = new ErrorRequestCoordinator(paramObject, paramRequestCoordinator);
      errorRequestCoordinator1 = errorRequestCoordinator2;
    } else {
      ErrorRequestCoordinator errorRequestCoordinator = null;
      errorRequestCoordinator2 = errorRequestCoordinator1;
      errorRequestCoordinator1 = errorRequestCoordinator;
    } 
    Request request = buildThumbnailRequestRecursive(paramObject, paramTarget, paramRequestListener, (RequestCoordinator)errorRequestCoordinator2, paramTransitionOptions, paramPriority, paramInt1, paramInt2, paramBaseRequestOptions, paramExecutor);
    if (errorRequestCoordinator1 == null)
      return request; 
    int k = this.errorBuilder.getOverrideWidth();
    int m = this.errorBuilder.getOverrideHeight();
    int j = k;
    int i = m;
    if (Util.isValidDimensions(paramInt1, paramInt2)) {
      j = k;
      i = m;
      if (!this.errorBuilder.isValidOverride()) {
        j = paramBaseRequestOptions.getOverrideWidth();
        i = paramBaseRequestOptions.getOverrideHeight();
      } 
    } 
    RequestBuilder<TranscodeType> requestBuilder = this.errorBuilder;
    errorRequestCoordinator1.setRequests(request, requestBuilder.buildRequestRecursive(paramObject, paramTarget, paramRequestListener, (RequestCoordinator)errorRequestCoordinator1, requestBuilder.transitionOptions, requestBuilder.getPriority(), j, i, this.errorBuilder, paramExecutor));
    return (Request)errorRequestCoordinator1;
  }
  
  private Request buildThumbnailRequestRecursive(Object paramObject, Target<TranscodeType> paramTarget, RequestListener<TranscodeType> paramRequestListener, RequestCoordinator paramRequestCoordinator, TransitionOptions<?, ? super TranscodeType> paramTransitionOptions, Priority paramPriority, int paramInt1, int paramInt2, BaseRequestOptions<?> paramBaseRequestOptions, Executor paramExecutor) {
    ThumbnailRequestCoordinator thumbnailRequestCoordinator;
    Request request1;
    Request request2;
    RequestBuilder<TranscodeType> requestBuilder1;
    RequestBuilder<TranscodeType> requestBuilder2 = this.thumbnailBuilder;
    if (requestBuilder2 != null) {
      if (!this.isThumbnailBuilt) {
        Priority priority;
        TransitionOptions<?, ? super TranscodeType> transitionOptions = requestBuilder2.transitionOptions;
        if (requestBuilder2.isDefaultTransitionOptionsSet)
          transitionOptions = paramTransitionOptions; 
        if (requestBuilder2.isPrioritySet()) {
          priority = this.thumbnailBuilder.getPriority();
        } else {
          priority = getThumbnailPriority(paramPriority);
        } 
        int i = this.thumbnailBuilder.getOverrideWidth();
        int j = this.thumbnailBuilder.getOverrideHeight();
        if (Util.isValidDimensions(paramInt1, paramInt2) && !this.thumbnailBuilder.isValidOverride()) {
          i = paramBaseRequestOptions.getOverrideWidth();
          j = paramBaseRequestOptions.getOverrideHeight();
        } 
        thumbnailRequestCoordinator = new ThumbnailRequestCoordinator(paramObject, paramRequestCoordinator);
        request2 = obtainRequest(paramObject, paramTarget, paramRequestListener, paramBaseRequestOptions, (RequestCoordinator)thumbnailRequestCoordinator, paramTransitionOptions, paramPriority, paramInt1, paramInt2, paramExecutor);
        this.isThumbnailBuilt = true;
        requestBuilder1 = this.thumbnailBuilder;
        paramObject = requestBuilder1.buildRequestRecursive(paramObject, paramTarget, paramRequestListener, (RequestCoordinator)thumbnailRequestCoordinator, transitionOptions, priority, i, j, requestBuilder1, paramExecutor);
        this.isThumbnailBuilt = false;
        thumbnailRequestCoordinator.setRequests(request2, (Request)paramObject);
        return (Request)thumbnailRequestCoordinator;
      } 
      throw new IllegalStateException("You cannot use a request as both the main request and a thumbnail, consider using clone() on the request(s) passed to thumbnail()");
    } 
    if (this.thumbSizeMultiplier != null) {
      ThumbnailRequestCoordinator thumbnailRequestCoordinator1 = new ThumbnailRequestCoordinator(paramObject, (RequestCoordinator)thumbnailRequestCoordinator);
      request1 = obtainRequest(paramObject, paramTarget, paramRequestListener, paramBaseRequestOptions, (RequestCoordinator)thumbnailRequestCoordinator1, (TransitionOptions<?, ? super TranscodeType>)request2, (Priority)requestBuilder1, paramInt1, paramInt2, paramExecutor);
      paramBaseRequestOptions = paramBaseRequestOptions.clone().sizeMultiplier(this.thumbSizeMultiplier.floatValue());
      thumbnailRequestCoordinator1.setRequests(request1, obtainRequest(paramObject, paramTarget, paramRequestListener, paramBaseRequestOptions, (RequestCoordinator)thumbnailRequestCoordinator1, (TransitionOptions<?, ? super TranscodeType>)request2, getThumbnailPriority((Priority)requestBuilder1), paramInt1, paramInt2, paramExecutor));
      return (Request)thumbnailRequestCoordinator1;
    } 
    return obtainRequest(paramObject, paramTarget, paramRequestListener, paramBaseRequestOptions, (RequestCoordinator)request1, (TransitionOptions<?, ? super TranscodeType>)request2, (Priority)requestBuilder1, paramInt1, paramInt2, paramExecutor);
  }
  
  private RequestBuilder<TranscodeType> cloneWithNullErrorAndThumbnail() {
    RequestBuilder<TranscodeType> requestBuilder1 = clone();
    RequestBuilder<TranscodeType> requestBuilder2 = (RequestBuilder)null;
    return requestBuilder1.error(requestBuilder2).thumbnail(requestBuilder2);
  }
  
  private Priority getThumbnailPriority(Priority paramPriority) {
    switch (paramPriority) {
      default:
        throw new IllegalArgumentException("unknown priority: " + getPriority());
      case null:
      case null:
        return Priority.IMMEDIATE;
      case null:
        return Priority.HIGH;
      case null:
        break;
    } 
    return Priority.NORMAL;
  }
  
  private void initRequestListeners(List<RequestListener<Object>> paramList) {
    Iterator<RequestListener<Object>> iterator = paramList.iterator();
    while (iterator.hasNext())
      addListener((RequestListener<TranscodeType>)iterator.next()); 
  }
  
  private <Y extends Target<TranscodeType>> Y into(Y paramY, RequestListener<TranscodeType> paramRequestListener, BaseRequestOptions<?> paramBaseRequestOptions, Executor paramExecutor) {
    Preconditions.checkNotNull(paramY);
    if (this.isModelSet) {
      Request request1 = buildRequest((Target<TranscodeType>)paramY, paramRequestListener, paramBaseRequestOptions, paramExecutor);
      Request request2 = paramY.getRequest();
      if (request1.isEquivalentTo(request2) && !isSkipMemoryCacheWithCompletePreviousRequest(paramBaseRequestOptions, request2)) {
        if (!((Request)Preconditions.checkNotNull(request2)).isRunning())
          request2.begin(); 
        return paramY;
      } 
      this.requestManager.clear((Target<?>)paramY);
      paramY.setRequest(request1);
      this.requestManager.track((Target<?>)paramY, request1);
      return paramY;
    } 
    throw new IllegalArgumentException("You must call #load() before calling #into()");
  }
  
  private boolean isSkipMemoryCacheWithCompletePreviousRequest(BaseRequestOptions<?> paramBaseRequestOptions, Request paramRequest) {
    boolean bool;
    if (!paramBaseRequestOptions.isMemoryCacheable() && paramRequest.isComplete()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private RequestBuilder<TranscodeType> loadGeneric(Object paramObject) {
    if (isAutoCloneEnabled())
      return clone().loadGeneric(paramObject); 
    this.model = paramObject;
    this.isModelSet = true;
    return (RequestBuilder<TranscodeType>)selfOrThrowIfLocked();
  }
  
  private Request obtainRequest(Object paramObject, Target<TranscodeType> paramTarget, RequestListener<TranscodeType> paramRequestListener, BaseRequestOptions<?> paramBaseRequestOptions, RequestCoordinator paramRequestCoordinator, TransitionOptions<?, ? super TranscodeType> paramTransitionOptions, Priority paramPriority, int paramInt1, int paramInt2, Executor paramExecutor) {
    Context context = this.context;
    GlideContext glideContext = this.glideContext;
    return (Request)SingleRequest.obtain(context, glideContext, paramObject, this.model, this.transcodeClass, paramBaseRequestOptions, paramInt1, paramInt2, paramPriority, paramTarget, paramRequestListener, this.requestListeners, paramRequestCoordinator, glideContext.getEngine(), paramTransitionOptions.getTransitionFactory(), paramExecutor);
  }
  
  public RequestBuilder<TranscodeType> addListener(RequestListener<TranscodeType> paramRequestListener) {
    if (isAutoCloneEnabled())
      return clone().addListener(paramRequestListener); 
    if (paramRequestListener != null) {
      if (this.requestListeners == null)
        this.requestListeners = new ArrayList<>(); 
      this.requestListeners.add(paramRequestListener);
    } 
    return (RequestBuilder<TranscodeType>)selfOrThrowIfLocked();
  }
  
  public RequestBuilder<TranscodeType> apply(BaseRequestOptions<?> paramBaseRequestOptions) {
    Preconditions.checkNotNull(paramBaseRequestOptions);
    return (RequestBuilder<TranscodeType>)super.apply(paramBaseRequestOptions);
  }
  
  public RequestBuilder<TranscodeType> clone() {
    RequestBuilder<TranscodeType> requestBuilder1 = (RequestBuilder)super.clone();
    requestBuilder1.transitionOptions = (TransitionOptions<?, ? super TranscodeType>)requestBuilder1.transitionOptions.clone();
    if (requestBuilder1.requestListeners != null)
      requestBuilder1.requestListeners = new ArrayList<>(requestBuilder1.requestListeners); 
    RequestBuilder<TranscodeType> requestBuilder2 = requestBuilder1.thumbnailBuilder;
    if (requestBuilder2 != null)
      requestBuilder1.thumbnailBuilder = requestBuilder2.clone(); 
    requestBuilder2 = requestBuilder1.errorBuilder;
    if (requestBuilder2 != null)
      requestBuilder1.errorBuilder = requestBuilder2.clone(); 
    return requestBuilder1;
  }
  
  @Deprecated
  public FutureTarget<File> downloadOnly(int paramInt1, int paramInt2) {
    return getDownloadOnlyRequest().submit(paramInt1, paramInt2);
  }
  
  @Deprecated
  public <Y extends Target<File>> Y downloadOnly(Y paramY) {
    return (Y)getDownloadOnlyRequest().into((Target<File>)paramY);
  }
  
  public RequestBuilder<TranscodeType> error(RequestBuilder<TranscodeType> paramRequestBuilder) {
    if (isAutoCloneEnabled())
      return clone().error(paramRequestBuilder); 
    this.errorBuilder = paramRequestBuilder;
    return (RequestBuilder<TranscodeType>)selfOrThrowIfLocked();
  }
  
  public RequestBuilder<TranscodeType> error(Object paramObject) {
    return (paramObject == null) ? error((RequestBuilder<TranscodeType>)null) : error(cloneWithNullErrorAndThumbnail().load(paramObject));
  }
  
  protected RequestBuilder<File> getDownloadOnlyRequest() {
    return (new RequestBuilder((Class)File.class, this)).apply((BaseRequestOptions<?>)DOWNLOAD_ONLY_OPTIONS);
  }
  
  @Deprecated
  public FutureTarget<TranscodeType> into(int paramInt1, int paramInt2) {
    return submit(paramInt1, paramInt2);
  }
  
  public <Y extends Target<TranscodeType>> Y into(Y paramY) {
    return into(paramY, (RequestListener<?>)null, Executors.mainThreadExecutor());
  }
  
  <Y extends Target<TranscodeType>> Y into(Y paramY, RequestListener<TranscodeType> paramRequestListener, Executor paramExecutor) {
    return into(paramY, paramRequestListener, this, paramExecutor);
  }
  
  public ViewTarget<ImageView, TranscodeType> into(ImageView paramImageView) {
    Util.assertMainThread();
    Preconditions.checkNotNull(paramImageView);
    RequestBuilder<?> requestBuilder = this;
    BaseRequestOptions<?> baseRequestOptions = requestBuilder;
    if (!requestBuilder.isTransformationSet()) {
      baseRequestOptions = requestBuilder;
      if (requestBuilder.isTransformationAllowed()) {
        baseRequestOptions = requestBuilder;
        if (paramImageView.getScaleType() != null) {
          switch (paramImageView.getScaleType()) {
            default:
              baseRequestOptions = requestBuilder;
              return into(this.glideContext.buildImageViewTarget(paramImageView, this.transcodeClass), (RequestListener<?>)null, baseRequestOptions, Executors.mainThreadExecutor());
            case null:
              baseRequestOptions = requestBuilder.clone().optionalCenterInside();
              return into(this.glideContext.buildImageViewTarget(paramImageView, this.transcodeClass), (RequestListener<?>)null, baseRequestOptions, Executors.mainThreadExecutor());
            case null:
            case null:
            case null:
              baseRequestOptions = requestBuilder.clone().optionalFitCenter();
              return into(this.glideContext.buildImageViewTarget(paramImageView, this.transcodeClass), (RequestListener<?>)null, baseRequestOptions, Executors.mainThreadExecutor());
            case null:
              baseRequestOptions = requestBuilder.clone().optionalCenterInside();
              return into(this.glideContext.buildImageViewTarget(paramImageView, this.transcodeClass), (RequestListener<?>)null, baseRequestOptions, Executors.mainThreadExecutor());
            case null:
              break;
          } 
          baseRequestOptions = requestBuilder.clone().optionalCenterCrop();
        } 
      } 
    } 
    return into(this.glideContext.buildImageViewTarget(paramImageView, this.transcodeClass), (RequestListener<?>)null, baseRequestOptions, Executors.mainThreadExecutor());
  }
  
  public RequestBuilder<TranscodeType> listener(RequestListener<TranscodeType> paramRequestListener) {
    if (isAutoCloneEnabled())
      return clone().listener(paramRequestListener); 
    this.requestListeners = null;
    return addListener(paramRequestListener);
  }
  
  public RequestBuilder<TranscodeType> load(Bitmap paramBitmap) {
    return loadGeneric(paramBitmap).apply((BaseRequestOptions<?>)RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE));
  }
  
  public RequestBuilder<TranscodeType> load(Drawable paramDrawable) {
    return loadGeneric(paramDrawable).apply((BaseRequestOptions<?>)RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE));
  }
  
  public RequestBuilder<TranscodeType> load(Uri paramUri) {
    return loadGeneric(paramUri);
  }
  
  public RequestBuilder<TranscodeType> load(File paramFile) {
    return loadGeneric(paramFile);
  }
  
  public RequestBuilder<TranscodeType> load(Integer paramInteger) {
    return loadGeneric(paramInteger).apply((BaseRequestOptions<?>)RequestOptions.signatureOf(AndroidResourceSignature.obtain(this.context)));
  }
  
  public RequestBuilder<TranscodeType> load(Object paramObject) {
    return loadGeneric(paramObject);
  }
  
  public RequestBuilder<TranscodeType> load(String paramString) {
    return loadGeneric(paramString);
  }
  
  @Deprecated
  public RequestBuilder<TranscodeType> load(URL paramURL) {
    return loadGeneric(paramURL);
  }
  
  public RequestBuilder<TranscodeType> load(byte[] paramArrayOfbyte) {
    RequestBuilder<TranscodeType> requestBuilder2 = loadGeneric(paramArrayOfbyte);
    RequestBuilder<TranscodeType> requestBuilder1 = requestBuilder2;
    if (!requestBuilder2.isDiskCacheStrategySet())
      requestBuilder1 = requestBuilder2.apply((BaseRequestOptions<?>)RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)); 
    requestBuilder2 = requestBuilder1;
    if (!requestBuilder1.isSkipMemoryCacheSet())
      requestBuilder2 = requestBuilder1.apply((BaseRequestOptions<?>)RequestOptions.skipMemoryCacheOf(true)); 
    return requestBuilder2;
  }
  
  public Target<TranscodeType> preload() {
    return preload(-2147483648, -2147483648);
  }
  
  public Target<TranscodeType> preload(int paramInt1, int paramInt2) {
    return (Target<TranscodeType>)into(PreloadTarget.obtain(this.requestManager, paramInt1, paramInt2));
  }
  
  public FutureTarget<TranscodeType> submit() {
    return submit(-2147483648, -2147483648);
  }
  
  public FutureTarget<TranscodeType> submit(int paramInt1, int paramInt2) {
    RequestFutureTarget requestFutureTarget = new RequestFutureTarget(paramInt1, paramInt2);
    return (FutureTarget<TranscodeType>)into(requestFutureTarget, (RequestListener<?>)requestFutureTarget, Executors.directExecutor());
  }
  
  public RequestBuilder<TranscodeType> thumbnail(float paramFloat) {
    if (isAutoCloneEnabled())
      return clone().thumbnail(paramFloat); 
    if (paramFloat >= 0.0F && paramFloat <= 1.0F) {
      this.thumbSizeMultiplier = Float.valueOf(paramFloat);
      return (RequestBuilder<TranscodeType>)selfOrThrowIfLocked();
    } 
    throw new IllegalArgumentException("sizeMultiplier must be between 0 and 1");
  }
  
  public RequestBuilder<TranscodeType> thumbnail(RequestBuilder<TranscodeType> paramRequestBuilder) {
    if (isAutoCloneEnabled())
      return clone().thumbnail(paramRequestBuilder); 
    this.thumbnailBuilder = paramRequestBuilder;
    return (RequestBuilder<TranscodeType>)selfOrThrowIfLocked();
  }
  
  public RequestBuilder<TranscodeType> thumbnail(List<RequestBuilder<TranscodeType>> paramList) {
    if (paramList == null || paramList.isEmpty())
      return thumbnail((RequestBuilder<TranscodeType>)null); 
    RequestBuilder<TranscodeType> requestBuilder = null;
    for (int i = paramList.size() - 1; i >= 0; i--) {
      RequestBuilder<TranscodeType> requestBuilder1 = paramList.get(i);
      if (requestBuilder1 != null)
        if (requestBuilder == null) {
          requestBuilder = requestBuilder1;
        } else {
          requestBuilder = requestBuilder1.thumbnail(requestBuilder);
        }  
    } 
    return thumbnail(requestBuilder);
  }
  
  public RequestBuilder<TranscodeType> thumbnail(RequestBuilder<TranscodeType>... paramVarArgs) {
    return (paramVarArgs == null || paramVarArgs.length == 0) ? thumbnail((RequestBuilder<TranscodeType>)null) : thumbnail(Arrays.asList(paramVarArgs));
  }
  
  public RequestBuilder<TranscodeType> transition(TransitionOptions<?, ? super TranscodeType> paramTransitionOptions) {
    if (isAutoCloneEnabled())
      return clone().transition(paramTransitionOptions); 
    this.transitionOptions = (TransitionOptions<?, ? super TranscodeType>)Preconditions.checkNotNull(paramTransitionOptions);
    this.isDefaultTransitionOptionsSet = false;
    return (RequestBuilder<TranscodeType>)selfOrThrowIfLocked();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\RequestBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */