package com.bumptech.glide.request;

public class ThumbnailRequestCoordinator implements RequestCoordinator, Request {
  private volatile Request full;
  
  private RequestCoordinator.RequestState fullState = RequestCoordinator.RequestState.CLEARED;
  
  private boolean isRunningDuringBegin;
  
  private final RequestCoordinator parent;
  
  private final Object requestLock;
  
  private volatile Request thumb;
  
  private RequestCoordinator.RequestState thumbState = RequestCoordinator.RequestState.CLEARED;
  
  public ThumbnailRequestCoordinator(Object paramObject, RequestCoordinator paramRequestCoordinator) {
    this.requestLock = paramObject;
    this.parent = paramRequestCoordinator;
  }
  
  private boolean parentCanNotifyCleared() {
    RequestCoordinator requestCoordinator = this.parent;
    return (requestCoordinator == null || requestCoordinator.canNotifyCleared(this));
  }
  
  private boolean parentCanNotifyStatusChanged() {
    RequestCoordinator requestCoordinator = this.parent;
    return (requestCoordinator == null || requestCoordinator.canNotifyStatusChanged(this));
  }
  
  private boolean parentCanSetImage() {
    RequestCoordinator requestCoordinator = this.parent;
    return (requestCoordinator == null || requestCoordinator.canSetImage(this));
  }
  
  public void begin() {
    synchronized (this.requestLock) {
      this.isRunningDuringBegin = true;
      try {
        if (this.fullState != RequestCoordinator.RequestState.SUCCESS && this.thumbState != RequestCoordinator.RequestState.RUNNING) {
          this.thumbState = RequestCoordinator.RequestState.RUNNING;
          this.thumb.begin();
        } 
        if (this.isRunningDuringBegin && this.fullState != RequestCoordinator.RequestState.RUNNING) {
          this.fullState = RequestCoordinator.RequestState.RUNNING;
          this.full.begin();
        } 
        return;
      } finally {
        this.isRunningDuringBegin = false;
      } 
    } 
  }
  
  public boolean canNotifyCleared(Request paramRequest) {
    synchronized (this.requestLock) {
      boolean bool;
      if (parentCanNotifyCleared() && paramRequest.equals(this.full) && this.fullState != RequestCoordinator.RequestState.PAUSED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
  }
  
  public boolean canNotifyStatusChanged(Request paramRequest) {
    synchronized (this.requestLock) {
      boolean bool;
      if (parentCanNotifyStatusChanged() && paramRequest.equals(this.full) && !isAnyResourceSet()) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
  }
  
  public boolean canSetImage(Request paramRequest) {
    synchronized (this.requestLock) {
      boolean bool;
      if (parentCanSetImage() && (paramRequest.equals(this.full) || this.fullState != RequestCoordinator.RequestState.SUCCESS)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
  }
  
  public void clear() {
    synchronized (this.requestLock) {
      this.isRunningDuringBegin = false;
      this.fullState = RequestCoordinator.RequestState.CLEARED;
      this.thumbState = RequestCoordinator.RequestState.CLEARED;
      this.thumb.clear();
      this.full.clear();
      return;
    } 
  }
  
  public RequestCoordinator getRoot() {
    synchronized (this.requestLock) {
      RequestCoordinator requestCoordinator = this.parent;
      if (requestCoordinator != null) {
        requestCoordinator = requestCoordinator.getRoot();
      } else {
        requestCoordinator = this;
      } 
      return requestCoordinator;
    } 
  }
  
  public boolean isAnyResourceSet() {
    synchronized (this.requestLock) {
      if (this.thumb.isAnyResourceSet() || this.full.isAnyResourceSet())
        return true; 
      return false;
    } 
  }
  
  public boolean isCleared() {
    synchronized (this.requestLock) {
      boolean bool;
      if (this.fullState == RequestCoordinator.RequestState.CLEARED) {
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
      if (this.fullState == RequestCoordinator.RequestState.SUCCESS) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
  }
  
  public boolean isEquivalentTo(Request paramRequest) {
    boolean bool1 = paramRequest instanceof ThumbnailRequestCoordinator;
    boolean bool = false;
    if (bool1) {
      paramRequest = paramRequest;
      if (((this.full == null) ? (((ThumbnailRequestCoordinator)paramRequest).full == null) : this.full.isEquivalentTo(((ThumbnailRequestCoordinator)paramRequest).full)) && ((this.thumb == null) ? (((ThumbnailRequestCoordinator)paramRequest).thumb == null) : this.thumb.isEquivalentTo(((ThumbnailRequestCoordinator)paramRequest).thumb)))
        bool = true; 
      return bool;
    } 
    return false;
  }
  
  public boolean isRunning() {
    synchronized (this.requestLock) {
      boolean bool;
      if (this.fullState == RequestCoordinator.RequestState.RUNNING) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
  }
  
  public void onRequestFailed(Request paramRequest) {
    synchronized (this.requestLock) {
      if (!paramRequest.equals(this.full)) {
        this.thumbState = RequestCoordinator.RequestState.FAILED;
        return;
      } 
      this.fullState = RequestCoordinator.RequestState.FAILED;
      RequestCoordinator requestCoordinator = this.parent;
      if (requestCoordinator != null)
        requestCoordinator.onRequestFailed(this); 
      return;
    } 
  }
  
  public void onRequestSuccess(Request paramRequest) {
    synchronized (this.requestLock) {
      if (paramRequest.equals(this.thumb)) {
        this.thumbState = RequestCoordinator.RequestState.SUCCESS;
        return;
      } 
      this.fullState = RequestCoordinator.RequestState.SUCCESS;
      RequestCoordinator requestCoordinator = this.parent;
      if (requestCoordinator != null)
        requestCoordinator.onRequestSuccess(this); 
      if (!this.thumbState.isComplete())
        this.thumb.clear(); 
      return;
    } 
  }
  
  public void pause() {
    synchronized (this.requestLock) {
      if (!this.thumbState.isComplete()) {
        this.thumbState = RequestCoordinator.RequestState.PAUSED;
        this.thumb.pause();
      } 
      if (!this.fullState.isComplete()) {
        this.fullState = RequestCoordinator.RequestState.PAUSED;
        this.full.pause();
      } 
      return;
    } 
  }
  
  public void setRequests(Request paramRequest1, Request paramRequest2) {
    this.full = paramRequest1;
    this.thumb = paramRequest2;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\request\ThumbnailRequestCoordinator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */