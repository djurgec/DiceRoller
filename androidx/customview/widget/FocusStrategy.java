package androidx.customview.widget;

import android.graphics.Rect;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class FocusStrategy {
  private static boolean beamBeats(int paramInt, Rect paramRect1, Rect paramRect2, Rect paramRect3) {
    boolean bool2 = beamsOverlap(paramInt, paramRect1, paramRect2);
    boolean bool1 = beamsOverlap(paramInt, paramRect1, paramRect3);
    boolean bool = false;
    if (bool1 || !bool2)
      return false; 
    if (!isToDirectionOf(paramInt, paramRect1, paramRect3))
      return true; 
    if (paramInt == 17 || paramInt == 66)
      return true; 
    if (majorAxisDistance(paramInt, paramRect1, paramRect2) < majorAxisDistanceToFarEdge(paramInt, paramRect1, paramRect3))
      bool = true; 
    return bool;
  }
  
  private static boolean beamsOverlap(int paramInt, Rect paramRect1, Rect paramRect2) {
    boolean bool2 = true;
    boolean bool1 = true;
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      case 33:
      case 130:
        if (paramRect2.right < paramRect1.left || paramRect2.left > paramRect1.right)
          bool1 = false; 
        return bool1;
      case 17:
      case 66:
        break;
    } 
    if (paramRect2.bottom >= paramRect1.top && paramRect2.top <= paramRect1.bottom) {
      bool1 = bool2;
    } else {
      bool1 = false;
    } 
    return bool1;
  }
  
  public static <L, T> T findNextFocusInAbsoluteDirection(L paramL, CollectionAdapter<L, T> paramCollectionAdapter, BoundsAdapter<T> paramBoundsAdapter, T paramT, Rect paramRect, int paramInt) {
    T t;
    Rect rect2 = new Rect(paramRect);
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      case 130:
        rect2.offset(0, -(paramRect.height() + 1));
        break;
      case 66:
        rect2.offset(-(paramRect.width() + 1), 0);
        break;
      case 33:
        rect2.offset(0, paramRect.height() + 1);
        break;
      case 17:
        rect2.offset(paramRect.width() + 1, 0);
        break;
    } 
    Object object = null;
    int i = paramCollectionAdapter.size(paramL);
    Rect rect1 = new Rect();
    for (byte b = 0; b < i; b++) {
      T t1 = paramCollectionAdapter.get(paramL, b);
      if (t1 != paramT) {
        paramBoundsAdapter.obtainBounds(t1, rect1);
        if (isBetterCandidate(paramInt, paramRect, rect1, rect2)) {
          rect2.set(rect1);
          t = t1;
        } 
      } 
    } 
    return t;
  }
  
  public static <L, T> T findNextFocusInRelativeDirection(L paramL, CollectionAdapter<L, T> paramCollectionAdapter, BoundsAdapter<T> paramBoundsAdapter, T paramT, int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
    int i = paramCollectionAdapter.size(paramL);
    ArrayList<?> arrayList = new ArrayList(i);
    for (byte b = 0; b < i; b++)
      arrayList.add(paramCollectionAdapter.get(paramL, b)); 
    Collections.sort(arrayList, new SequentialComparator(paramBoolean1, paramBoundsAdapter));
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD}.");
      case 2:
        return getNextFocusable(paramT, (ArrayList)arrayList, paramBoolean2);
      case 1:
        break;
    } 
    return getPreviousFocusable(paramT, (ArrayList)arrayList, paramBoolean2);
  }
  
  private static <T> T getNextFocusable(T paramT, ArrayList<T> paramArrayList, boolean paramBoolean) {
    int i;
    int j = paramArrayList.size();
    if (paramT == null) {
      i = -1;
    } else {
      i = paramArrayList.lastIndexOf(paramT);
    } 
    return (++i < j) ? paramArrayList.get(i) : ((paramBoolean && j > 0) ? paramArrayList.get(0) : null);
  }
  
  private static <T> T getPreviousFocusable(T paramT, ArrayList<T> paramArrayList, boolean paramBoolean) {
    int i;
    int j = paramArrayList.size();
    if (paramT == null) {
      i = j;
    } else {
      i = paramArrayList.indexOf(paramT);
    } 
    return (--i >= 0) ? paramArrayList.get(i) : ((paramBoolean && j > 0) ? paramArrayList.get(j - 1) : null);
  }
  
  private static int getWeightedDistanceFor(int paramInt1, int paramInt2) {
    return paramInt1 * 13 * paramInt1 + paramInt2 * paramInt2;
  }
  
  private static boolean isBetterCandidate(int paramInt, Rect paramRect1, Rect paramRect2, Rect paramRect3) {
    boolean bool1 = isCandidate(paramRect1, paramRect2, paramInt);
    boolean bool = false;
    if (!bool1)
      return false; 
    if (!isCandidate(paramRect1, paramRect3, paramInt))
      return true; 
    if (beamBeats(paramInt, paramRect1, paramRect2, paramRect3))
      return true; 
    if (beamBeats(paramInt, paramRect1, paramRect3, paramRect2))
      return false; 
    int i = getWeightedDistanceFor(majorAxisDistance(paramInt, paramRect1, paramRect2), minorAxisDistance(paramInt, paramRect1, paramRect2));
    if (i < getWeightedDistanceFor(majorAxisDistance(paramInt, paramRect1, paramRect3), minorAxisDistance(paramInt, paramRect1, paramRect3)))
      bool = true; 
    return bool;
  }
  
  private static boolean isCandidate(Rect paramRect1, Rect paramRect2, int paramInt) {
    boolean bool3 = true;
    boolean bool4 = true;
    boolean bool2 = true;
    boolean bool1 = true;
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      case 130:
        if ((paramRect1.top >= paramRect2.top && paramRect1.bottom > paramRect2.top) || paramRect1.bottom >= paramRect2.bottom)
          bool1 = false; 
        return bool1;
      case 66:
        if ((paramRect1.left < paramRect2.left || paramRect1.right <= paramRect2.left) && paramRect1.right < paramRect2.right) {
          bool1 = bool3;
        } else {
          bool1 = false;
        } 
        return bool1;
      case 33:
        if ((paramRect1.bottom > paramRect2.bottom || paramRect1.top >= paramRect2.bottom) && paramRect1.top > paramRect2.top) {
          bool1 = bool4;
        } else {
          bool1 = false;
        } 
        return bool1;
      case 17:
        break;
    } 
    if ((paramRect1.right > paramRect2.right || paramRect1.left >= paramRect2.right) && paramRect1.left > paramRect2.left) {
      bool1 = bool2;
    } else {
      bool1 = false;
    } 
    return bool1;
  }
  
  private static boolean isToDirectionOf(int paramInt, Rect paramRect1, Rect paramRect2) {
    boolean bool4 = true;
    boolean bool1 = true;
    boolean bool3 = true;
    boolean bool2 = true;
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      case 130:
        if (paramRect1.bottom <= paramRect2.top) {
          bool1 = bool2;
        } else {
          bool1 = false;
        } 
        return bool1;
      case 66:
        if (paramRect1.right <= paramRect2.left) {
          bool1 = bool4;
        } else {
          bool1 = false;
        } 
        return bool1;
      case 33:
        if (paramRect1.top < paramRect2.bottom)
          bool1 = false; 
        return bool1;
      case 17:
        break;
    } 
    if (paramRect1.left >= paramRect2.right) {
      bool1 = bool3;
    } else {
      bool1 = false;
    } 
    return bool1;
  }
  
  private static int majorAxisDistance(int paramInt, Rect paramRect1, Rect paramRect2) {
    return Math.max(0, majorAxisDistanceRaw(paramInt, paramRect1, paramRect2));
  }
  
  private static int majorAxisDistanceRaw(int paramInt, Rect paramRect1, Rect paramRect2) {
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      case 130:
        return paramRect2.top - paramRect1.bottom;
      case 66:
        return paramRect2.left - paramRect1.right;
      case 33:
        return paramRect1.top - paramRect2.bottom;
      case 17:
        break;
    } 
    return paramRect1.left - paramRect2.right;
  }
  
  private static int majorAxisDistanceToFarEdge(int paramInt, Rect paramRect1, Rect paramRect2) {
    return Math.max(1, majorAxisDistanceToFarEdgeRaw(paramInt, paramRect1, paramRect2));
  }
  
  private static int majorAxisDistanceToFarEdgeRaw(int paramInt, Rect paramRect1, Rect paramRect2) {
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      case 130:
        return paramRect2.bottom - paramRect1.bottom;
      case 66:
        return paramRect2.right - paramRect1.right;
      case 33:
        return paramRect1.top - paramRect2.top;
      case 17:
        break;
    } 
    return paramRect1.left - paramRect2.left;
  }
  
  private static int minorAxisDistance(int paramInt, Rect paramRect1, Rect paramRect2) {
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      case 33:
      case 130:
        return Math.abs(paramRect1.left + paramRect1.width() / 2 - paramRect2.left + paramRect2.width() / 2);
      case 17:
      case 66:
        break;
    } 
    return Math.abs(paramRect1.top + paramRect1.height() / 2 - paramRect2.top + paramRect2.height() / 2);
  }
  
  public static interface BoundsAdapter<T> {
    void obtainBounds(T param1T, Rect param1Rect);
  }
  
  public static interface CollectionAdapter<T, V> {
    V get(T param1T, int param1Int);
    
    int size(T param1T);
  }
  
  private static class SequentialComparator<T> implements Comparator<T> {
    private final FocusStrategy.BoundsAdapter<T> mAdapter;
    
    private final boolean mIsLayoutRtl;
    
    private final Rect mTemp1 = new Rect();
    
    private final Rect mTemp2 = new Rect();
    
    SequentialComparator(boolean param1Boolean, FocusStrategy.BoundsAdapter<T> param1BoundsAdapter) {
      this.mIsLayoutRtl = param1Boolean;
      this.mAdapter = param1BoundsAdapter;
    }
    
    public int compare(T param1T1, T param1T2) {
      Rect rect1 = this.mTemp1;
      Rect rect2 = this.mTemp2;
      this.mAdapter.obtainBounds(param1T1, rect1);
      this.mAdapter.obtainBounds(param1T2, rect2);
      int i = rect1.top;
      int j = rect2.top;
      byte b = -1;
      if (i < j)
        return -1; 
      if (rect1.top > rect2.top)
        return 1; 
      if (rect1.left < rect2.left) {
        if (this.mIsLayoutRtl)
          b = 1; 
        return b;
      } 
      if (rect1.left > rect2.left) {
        if (!this.mIsLayoutRtl)
          b = 1; 
        return b;
      } 
      if (rect1.bottom < rect2.bottom)
        return -1; 
      if (rect1.bottom > rect2.bottom)
        return 1; 
      if (rect1.right < rect2.right) {
        if (this.mIsLayoutRtl)
          b = 1; 
        return b;
      } 
      if (rect1.right > rect2.right) {
        if (!this.mIsLayoutRtl)
          b = 1; 
        return b;
      } 
      return 0;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\customview\widget\FocusStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */