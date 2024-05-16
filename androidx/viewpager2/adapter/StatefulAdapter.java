package androidx.viewpager2.adapter;

import android.os.Parcelable;

public interface StatefulAdapter {
  void restoreState(Parcelable paramParcelable);
  
  Parcelable saveState();
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\viewpager2\adapter\StatefulAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */