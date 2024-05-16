package androidx.recyclerview.widget;

import android.view.View;

class LayoutState {
  static final int INVALID_LAYOUT = -2147483648;
  
  static final int ITEM_DIRECTION_HEAD = -1;
  
  static final int ITEM_DIRECTION_TAIL = 1;
  
  static final int LAYOUT_END = 1;
  
  static final int LAYOUT_START = -1;
  
  int mAvailable;
  
  int mCurrentPosition;
  
  int mEndLine = 0;
  
  boolean mInfinite;
  
  int mItemDirection;
  
  int mLayoutDirection;
  
  boolean mRecycle = true;
  
  int mStartLine = 0;
  
  boolean mStopInFocusable;
  
  boolean hasMore(RecyclerView.State paramState) {
    boolean bool;
    int i = this.mCurrentPosition;
    if (i >= 0 && i < paramState.getItemCount()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  View next(RecyclerView.Recycler paramRecycler) {
    View view = paramRecycler.getViewForPosition(this.mCurrentPosition);
    this.mCurrentPosition += this.mItemDirection;
    return view;
  }
  
  public String toString() {
    return "LayoutState{mAvailable=" + this.mAvailable + ", mCurrentPosition=" + this.mCurrentPosition + ", mItemDirection=" + this.mItemDirection + ", mLayoutDirection=" + this.mLayoutDirection + ", mStartLine=" + this.mStartLine + ", mEndLine=" + this.mEndLine + '}';
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\LayoutState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */