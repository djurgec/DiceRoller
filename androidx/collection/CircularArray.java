package androidx.collection;

public final class CircularArray<E> {
  private int mCapacityBitmask;
  
  private E[] mElements;
  
  private int mHead;
  
  private int mTail;
  
  public CircularArray() {
    this(8);
  }
  
  public CircularArray(int paramInt) {
    if (paramInt >= 1) {
      if (paramInt <= 1073741824) {
        if (Integer.bitCount(paramInt) != 1)
          paramInt = Integer.highestOneBit(paramInt - 1) << 1; 
        this.mCapacityBitmask = paramInt - 1;
        this.mElements = (E[])new Object[paramInt];
        return;
      } 
      throw new IllegalArgumentException("capacity must be <= 2^30");
    } 
    throw new IllegalArgumentException("capacity must be >= 1");
  }
  
  private void doubleCapacity() {
    E[] arrayOfE = this.mElements;
    int k = arrayOfE.length;
    int i = this.mHead;
    int m = k - i;
    int j = k << 1;
    if (j >= 0) {
      Object[] arrayOfObject = new Object[j];
      System.arraycopy(arrayOfE, i, arrayOfObject, 0, m);
      System.arraycopy(this.mElements, 0, arrayOfObject, m, this.mHead);
      this.mElements = (E[])arrayOfObject;
      this.mHead = 0;
      this.mTail = k;
      this.mCapacityBitmask = j - 1;
      return;
    } 
    throw new RuntimeException("Max array capacity exceeded");
  }
  
  public void addFirst(E paramE) {
    int i = this.mHead - 1 & this.mCapacityBitmask;
    this.mHead = i;
    this.mElements[i] = paramE;
    if (i == this.mTail)
      doubleCapacity(); 
  }
  
  public void addLast(E paramE) {
    E[] arrayOfE = this.mElements;
    int i = this.mTail;
    arrayOfE[i] = paramE;
    i = this.mCapacityBitmask & i + 1;
    this.mTail = i;
    if (i == this.mHead)
      doubleCapacity(); 
  }
  
  public void clear() {
    removeFromStart(size());
  }
  
  public E get(int paramInt) {
    if (paramInt >= 0 && paramInt < size())
      return this.mElements[this.mHead + paramInt & this.mCapacityBitmask]; 
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public E getFirst() {
    int i = this.mHead;
    if (i != this.mTail)
      return this.mElements[i]; 
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public E getLast() {
    int j = this.mHead;
    int i = this.mTail;
    if (j != i)
      return this.mElements[i - 1 & this.mCapacityBitmask]; 
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public boolean isEmpty() {
    boolean bool;
    if (this.mHead == this.mTail) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public E popFirst() {
    int i = this.mHead;
    if (i != this.mTail) {
      E[] arrayOfE = this.mElements;
      E e = arrayOfE[i];
      arrayOfE[i] = null;
      this.mHead = i + 1 & this.mCapacityBitmask;
      return e;
    } 
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public E popLast() {
    int i = this.mHead;
    int j = this.mTail;
    if (i != j) {
      i = this.mCapacityBitmask & j - 1;
      E[] arrayOfE = this.mElements;
      E e = arrayOfE[i];
      arrayOfE[i] = null;
      this.mTail = i;
      return e;
    } 
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public void removeFromEnd(int paramInt) {
    if (paramInt <= 0)
      return; 
    if (paramInt <= size()) {
      int i = 0;
      int j = this.mTail;
      if (paramInt < j)
        i = j - paramInt; 
      j = i;
      while (true) {
        int k = this.mTail;
        if (j < k) {
          this.mElements[j] = null;
          j++;
          continue;
        } 
        i = k - i;
        paramInt -= i;
        this.mTail = k - i;
        if (paramInt > 0) {
          i = this.mElements.length;
          this.mTail = i;
          i -= paramInt;
          for (paramInt = i; paramInt < this.mTail; paramInt++)
            this.mElements[paramInt] = null; 
          this.mTail = i;
        } 
        return;
      } 
    } 
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public void removeFromStart(int paramInt) {
    if (paramInt <= 0)
      return; 
    if (paramInt <= size()) {
      int j = this.mElements.length;
      int k = this.mHead;
      int i = j;
      if (paramInt < j - k)
        i = k + paramInt; 
      for (j = this.mHead; j < i; j++)
        this.mElements[j] = null; 
      j = this.mHead;
      k = i - j;
      i = paramInt - k;
      this.mHead = j + k & this.mCapacityBitmask;
      if (i > 0) {
        for (paramInt = 0; paramInt < i; paramInt++)
          this.mElements[paramInt] = null; 
        this.mHead = i;
      } 
      return;
    } 
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public int size() {
    return this.mTail - this.mHead & this.mCapacityBitmask;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\collection\CircularArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */