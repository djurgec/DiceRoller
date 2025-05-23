package androidx.recyclerview.widget;

public class BatchingListUpdateCallback implements ListUpdateCallback {
  private static final int TYPE_ADD = 1;
  
  private static final int TYPE_CHANGE = 3;
  
  private static final int TYPE_NONE = 0;
  
  private static final int TYPE_REMOVE = 2;
  
  int mLastEventCount = -1;
  
  Object mLastEventPayload = null;
  
  int mLastEventPosition = -1;
  
  int mLastEventType = 0;
  
  final ListUpdateCallback mWrapped;
  
  public BatchingListUpdateCallback(ListUpdateCallback paramListUpdateCallback) {
    this.mWrapped = paramListUpdateCallback;
  }
  
  public void dispatchLastEvent() {
    int i = this.mLastEventType;
    if (i == 0)
      return; 
    switch (i) {
      case 3:
        this.mWrapped.onChanged(this.mLastEventPosition, this.mLastEventCount, this.mLastEventPayload);
        break;
      case 2:
        this.mWrapped.onRemoved(this.mLastEventPosition, this.mLastEventCount);
        break;
      case 1:
        this.mWrapped.onInserted(this.mLastEventPosition, this.mLastEventCount);
        break;
    } 
    this.mLastEventPayload = null;
    this.mLastEventType = 0;
  }
  
  public void onChanged(int paramInt1, int paramInt2, Object paramObject) {
    if (this.mLastEventType == 3) {
      int i = this.mLastEventPosition;
      int j = this.mLastEventCount;
      if (paramInt1 <= i + j && paramInt1 + paramInt2 >= i && this.mLastEventPayload == paramObject) {
        this.mLastEventPosition = Math.min(paramInt1, i);
        this.mLastEventCount = Math.max(j + i, paramInt1 + paramInt2) - this.mLastEventPosition;
        return;
      } 
    } 
    dispatchLastEvent();
    this.mLastEventPosition = paramInt1;
    this.mLastEventCount = paramInt2;
    this.mLastEventPayload = paramObject;
    this.mLastEventType = 3;
  }
  
  public void onInserted(int paramInt1, int paramInt2) {
    if (this.mLastEventType == 1) {
      int i = this.mLastEventPosition;
      if (paramInt1 >= i) {
        int j = this.mLastEventCount;
        if (paramInt1 <= i + j) {
          this.mLastEventCount = j + paramInt2;
          this.mLastEventPosition = Math.min(paramInt1, i);
          return;
        } 
      } 
    } 
    dispatchLastEvent();
    this.mLastEventPosition = paramInt1;
    this.mLastEventCount = paramInt2;
    this.mLastEventType = 1;
  }
  
  public void onMoved(int paramInt1, int paramInt2) {
    dispatchLastEvent();
    this.mWrapped.onMoved(paramInt1, paramInt2);
  }
  
  public void onRemoved(int paramInt1, int paramInt2) {
    if (this.mLastEventType == 2) {
      int i = this.mLastEventPosition;
      if (i >= paramInt1 && i <= paramInt1 + paramInt2) {
        this.mLastEventCount += paramInt2;
        this.mLastEventPosition = paramInt1;
        return;
      } 
    } 
    dispatchLastEvent();
    this.mLastEventPosition = paramInt1;
    this.mLastEventCount = paramInt2;
    this.mLastEventType = 2;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\BatchingListUpdateCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */