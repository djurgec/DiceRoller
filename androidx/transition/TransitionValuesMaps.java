package androidx.transition;

import android.util.SparseArray;
import android.view.View;
import androidx.collection.ArrayMap;
import androidx.collection.LongSparseArray;

class TransitionValuesMaps {
  final SparseArray<View> mIdValues = new SparseArray();
  
  final LongSparseArray<View> mItemIdValues = new LongSparseArray();
  
  final ArrayMap<String, View> mNameValues = new ArrayMap();
  
  final ArrayMap<View, TransitionValues> mViewValues = new ArrayMap();
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\TransitionValuesMaps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */