package com.bumptech.glide.request;

public interface RequestCoordinator {
  boolean canNotifyCleared(Request paramRequest);
  
  boolean canNotifyStatusChanged(Request paramRequest);
  
  boolean canSetImage(Request paramRequest);
  
  RequestCoordinator getRoot();
  
  boolean isAnyResourceSet();
  
  void onRequestFailed(Request paramRequest);
  
  void onRequestSuccess(Request paramRequest);
  
  public enum RequestState {
    CLEARED, FAILED, PAUSED, RUNNING, SUCCESS;
    
    private static final RequestState[] $VALUES;
    
    private final boolean isComplete;
    
    static {
      RequestState requestState2 = new RequestState("RUNNING", 0, false);
      RUNNING = requestState2;
      RequestState requestState1 = new RequestState("PAUSED", 1, false);
      PAUSED = requestState1;
      RequestState requestState4 = new RequestState("CLEARED", 2, false);
      CLEARED = requestState4;
      RequestState requestState5 = new RequestState("SUCCESS", 3, true);
      SUCCESS = requestState5;
      RequestState requestState3 = new RequestState("FAILED", 4, true);
      FAILED = requestState3;
      $VALUES = new RequestState[] { requestState2, requestState1, requestState4, requestState5, requestState3 };
    }
    
    RequestState(boolean param1Boolean) {
      this.isComplete = param1Boolean;
    }
    
    boolean isComplete() {
      return this.isComplete;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\request\RequestCoordinator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */