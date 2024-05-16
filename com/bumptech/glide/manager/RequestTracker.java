package com.bumptech.glide.manager;

import android.util.Log;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

public class RequestTracker {
  private static final String TAG = "RequestTracker";
  
  private boolean isPaused;
  
  private final List<Request> pendingRequests = new ArrayList<>();
  
  private final Set<Request> requests = Collections.newSetFromMap(new WeakHashMap<>());
  
  void addRequest(Request paramRequest) {
    this.requests.add(paramRequest);
  }
  
  public boolean clearAndRemove(Request paramRequest) {
    boolean bool2 = true;
    if (paramRequest == null)
      return true; 
    boolean bool = this.requests.remove(paramRequest);
    boolean bool1 = bool2;
    if (!this.pendingRequests.remove(paramRequest))
      if (bool) {
        bool1 = bool2;
      } else {
        bool1 = false;
      }  
    if (bool1)
      paramRequest.clear(); 
    return bool1;
  }
  
  public void clearRequests() {
    Iterator<Request> iterator = Util.getSnapshot(this.requests).iterator();
    while (iterator.hasNext())
      clearAndRemove(iterator.next()); 
    this.pendingRequests.clear();
  }
  
  public boolean isPaused() {
    return this.isPaused;
  }
  
  public void pauseAllRequests() {
    this.isPaused = true;
    for (Request request : Util.getSnapshot(this.requests)) {
      if (request.isRunning() || request.isComplete()) {
        request.clear();
        this.pendingRequests.add(request);
      } 
    } 
  }
  
  public void pauseRequests() {
    this.isPaused = true;
    for (Request request : Util.getSnapshot(this.requests)) {
      if (request.isRunning()) {
        request.pause();
        this.pendingRequests.add(request);
      } 
    } 
  }
  
  public void restartRequests() {
    for (Request request : Util.getSnapshot(this.requests)) {
      if (!request.isComplete() && !request.isCleared()) {
        request.clear();
        if (!this.isPaused) {
          request.begin();
          continue;
        } 
        this.pendingRequests.add(request);
      } 
    } 
  }
  
  public void resumeRequests() {
    this.isPaused = false;
    for (Request request : Util.getSnapshot(this.requests)) {
      if (!request.isComplete() && !request.isRunning())
        request.begin(); 
    } 
    this.pendingRequests.clear();
  }
  
  public void runRequest(Request paramRequest) {
    this.requests.add(paramRequest);
    if (!this.isPaused) {
      paramRequest.begin();
    } else {
      paramRequest.clear();
      if (Log.isLoggable("RequestTracker", 2))
        Log.v("RequestTracker", "Paused, delaying request"); 
      this.pendingRequests.add(paramRequest);
    } 
  }
  
  public String toString() {
    return super.toString() + "{numRequests=" + this.requests.size() + ", isPaused=" + this.isPaused + "}";
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\manager\RequestTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */