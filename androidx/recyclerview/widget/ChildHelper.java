package androidx.recyclerview.widget;

import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

class ChildHelper {
  private static final boolean DEBUG = false;
  
  private static final String TAG = "ChildrenHelper";
  
  final Bucket mBucket;
  
  final Callback mCallback;
  
  final List<View> mHiddenViews;
  
  ChildHelper(Callback paramCallback) {
    this.mCallback = paramCallback;
    this.mBucket = new Bucket();
    this.mHiddenViews = new ArrayList<>();
  }
  
  private int getOffset(int paramInt) {
    if (paramInt < 0)
      return -1; 
    int j = this.mCallback.getChildCount();
    for (int i = paramInt; i < j; i += k) {
      int k = paramInt - i - this.mBucket.countOnesBefore(i);
      if (k == 0) {
        while (this.mBucket.get(i))
          i++; 
        return i;
      } 
    } 
    return -1;
  }
  
  private void hideViewInternal(View paramView) {
    this.mHiddenViews.add(paramView);
    this.mCallback.onEnteredHiddenState(paramView);
  }
  
  private boolean unhideViewInternal(View paramView) {
    if (this.mHiddenViews.remove(paramView)) {
      this.mCallback.onLeftHiddenState(paramView);
      return true;
    } 
    return false;
  }
  
  void addView(View paramView, int paramInt, boolean paramBoolean) {
    if (paramInt < 0) {
      paramInt = this.mCallback.getChildCount();
    } else {
      paramInt = getOffset(paramInt);
    } 
    this.mBucket.insert(paramInt, paramBoolean);
    if (paramBoolean)
      hideViewInternal(paramView); 
    this.mCallback.addView(paramView, paramInt);
  }
  
  void addView(View paramView, boolean paramBoolean) {
    addView(paramView, -1, paramBoolean);
  }
  
  void attachViewToParent(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams, boolean paramBoolean) {
    if (paramInt < 0) {
      paramInt = this.mCallback.getChildCount();
    } else {
      paramInt = getOffset(paramInt);
    } 
    this.mBucket.insert(paramInt, paramBoolean);
    if (paramBoolean)
      hideViewInternal(paramView); 
    this.mCallback.attachViewToParent(paramView, paramInt, paramLayoutParams);
  }
  
  void detachViewFromParent(int paramInt) {
    paramInt = getOffset(paramInt);
    this.mBucket.remove(paramInt);
    this.mCallback.detachViewFromParent(paramInt);
  }
  
  View findHiddenNonRemovedView(int paramInt) {
    int i = this.mHiddenViews.size();
    for (byte b = 0; b < i; b++) {
      View view = this.mHiddenViews.get(b);
      RecyclerView.ViewHolder viewHolder = this.mCallback.getChildViewHolder(view);
      if (viewHolder.getLayoutPosition() == paramInt && !viewHolder.isInvalid() && !viewHolder.isRemoved())
        return view; 
    } 
    return null;
  }
  
  View getChildAt(int paramInt) {
    paramInt = getOffset(paramInt);
    return this.mCallback.getChildAt(paramInt);
  }
  
  int getChildCount() {
    return this.mCallback.getChildCount() - this.mHiddenViews.size();
  }
  
  View getUnfilteredChildAt(int paramInt) {
    return this.mCallback.getChildAt(paramInt);
  }
  
  int getUnfilteredChildCount() {
    return this.mCallback.getChildCount();
  }
  
  void hide(View paramView) {
    int i = this.mCallback.indexOfChild(paramView);
    if (i >= 0) {
      this.mBucket.set(i);
      hideViewInternal(paramView);
      return;
    } 
    throw new IllegalArgumentException("view is not a child, cannot hide " + paramView);
  }
  
  int indexOfChild(View paramView) {
    int i = this.mCallback.indexOfChild(paramView);
    return (i == -1) ? -1 : (this.mBucket.get(i) ? -1 : (i - this.mBucket.countOnesBefore(i)));
  }
  
  boolean isHidden(View paramView) {
    return this.mHiddenViews.contains(paramView);
  }
  
  void removeAllViewsUnfiltered() {
    this.mBucket.reset();
    for (int i = this.mHiddenViews.size() - 1; i >= 0; i--) {
      this.mCallback.onLeftHiddenState(this.mHiddenViews.get(i));
      this.mHiddenViews.remove(i);
    } 
    this.mCallback.removeAllViews();
  }
  
  void removeView(View paramView) {
    int i = this.mCallback.indexOfChild(paramView);
    if (i < 0)
      return; 
    if (this.mBucket.remove(i))
      unhideViewInternal(paramView); 
    this.mCallback.removeViewAt(i);
  }
  
  void removeViewAt(int paramInt) {
    paramInt = getOffset(paramInt);
    View view = this.mCallback.getChildAt(paramInt);
    if (view == null)
      return; 
    if (this.mBucket.remove(paramInt))
      unhideViewInternal(view); 
    this.mCallback.removeViewAt(paramInt);
  }
  
  boolean removeViewIfHidden(View paramView) {
    int i = this.mCallback.indexOfChild(paramView);
    if (i == -1) {
      unhideViewInternal(paramView);
      return true;
    } 
    if (this.mBucket.get(i)) {
      this.mBucket.remove(i);
      unhideViewInternal(paramView);
      this.mCallback.removeViewAt(i);
      return true;
    } 
    return false;
  }
  
  public String toString() {
    return this.mBucket.toString() + ", hidden list:" + this.mHiddenViews.size();
  }
  
  void unhide(View paramView) {
    int i = this.mCallback.indexOfChild(paramView);
    if (i >= 0) {
      if (this.mBucket.get(i)) {
        this.mBucket.clear(i);
        unhideViewInternal(paramView);
        return;
      } 
      throw new RuntimeException("trying to unhide a view that was not hidden" + paramView);
    } 
    throw new IllegalArgumentException("view is not a child, cannot hide " + paramView);
  }
  
  static class Bucket {
    static final int BITS_PER_WORD = 64;
    
    static final long LAST_BIT = -9223372036854775808L;
    
    long mData = 0L;
    
    Bucket mNext;
    
    private void ensureNext() {
      if (this.mNext == null)
        this.mNext = new Bucket(); 
    }
    
    void clear(int param1Int) {
      if (param1Int >= 64) {
        Bucket bucket = this.mNext;
        if (bucket != null)
          bucket.clear(param1Int - 64); 
      } else {
        this.mData &= 1L << param1Int ^ 0xFFFFFFFFFFFFFFFFL;
      } 
    }
    
    int countOnesBefore(int param1Int) {
      Bucket bucket = this.mNext;
      return (bucket == null) ? ((param1Int >= 64) ? Long.bitCount(this.mData) : Long.bitCount(this.mData & (1L << param1Int) - 1L)) : ((param1Int < 64) ? Long.bitCount(this.mData & (1L << param1Int) - 1L) : (bucket.countOnesBefore(param1Int - 64) + Long.bitCount(this.mData)));
    }
    
    boolean get(int param1Int) {
      boolean bool;
      if (param1Int >= 64) {
        ensureNext();
        return this.mNext.get(param1Int - 64);
      } 
      if ((this.mData & 1L << param1Int) != 0L) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    void insert(int param1Int, boolean param1Boolean) {
      if (param1Int >= 64) {
        ensureNext();
        this.mNext.insert(param1Int - 64, param1Boolean);
      } else {
        boolean bool;
        long l2 = this.mData;
        if ((Long.MIN_VALUE & l2) != 0L) {
          bool = true;
        } else {
          bool = false;
        } 
        long l1 = (1L << param1Int) - 1L;
        this.mData = l2 & l1 | (l2 & (l1 ^ 0xFFFFFFFFFFFFFFFFL)) << 1L;
        if (param1Boolean) {
          set(param1Int);
        } else {
          clear(param1Int);
        } 
        if (bool || this.mNext != null) {
          ensureNext();
          this.mNext.insert(0, bool);
        } 
      } 
    }
    
    boolean remove(int param1Int) {
      boolean bool;
      if (param1Int >= 64) {
        ensureNext();
        return this.mNext.remove(param1Int - 64);
      } 
      long l1 = 1L << param1Int;
      long l2 = this.mData;
      if ((l2 & l1) != 0L) {
        bool = true;
      } else {
        bool = false;
      } 
      l2 &= l1 ^ 0xFFFFFFFFFFFFFFFFL;
      this.mData = l2;
      l1--;
      this.mData = l2 & l1 | Long.rotateRight(l2 & (l1 ^ 0xFFFFFFFFFFFFFFFFL), 1);
      Bucket bucket = this.mNext;
      if (bucket != null) {
        if (bucket.get(0))
          set(63); 
        this.mNext.remove(0);
      } 
      return bool;
    }
    
    void reset() {
      this.mData = 0L;
      Bucket bucket = this.mNext;
      if (bucket != null)
        bucket.reset(); 
    }
    
    void set(int param1Int) {
      if (param1Int >= 64) {
        ensureNext();
        this.mNext.set(param1Int - 64);
      } else {
        this.mData |= 1L << param1Int;
      } 
    }
    
    public String toString() {
      String str;
      if (this.mNext == null) {
        str = Long.toBinaryString(this.mData);
      } else {
        str = this.mNext.toString() + "xx" + Long.toBinaryString(this.mData);
      } 
      return str;
    }
  }
  
  static interface Callback {
    void addView(View param1View, int param1Int);
    
    void attachViewToParent(View param1View, int param1Int, ViewGroup.LayoutParams param1LayoutParams);
    
    void detachViewFromParent(int param1Int);
    
    View getChildAt(int param1Int);
    
    int getChildCount();
    
    RecyclerView.ViewHolder getChildViewHolder(View param1View);
    
    int indexOfChild(View param1View);
    
    void onEnteredHiddenState(View param1View);
    
    void onLeftHiddenState(View param1View);
    
    void removeAllViews();
    
    void removeViewAt(int param1Int);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\ChildHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */