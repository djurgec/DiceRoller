package androidx.viewpager2.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public final class FragmentViewHolder extends RecyclerView.ViewHolder {
  private FragmentViewHolder(FrameLayout paramFrameLayout) {
    super((View)paramFrameLayout);
  }
  
  static FragmentViewHolder create(ViewGroup paramViewGroup) {
    FrameLayout frameLayout = new FrameLayout(paramViewGroup.getContext());
    frameLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    frameLayout.setId(ViewCompat.generateViewId());
    frameLayout.setSaveEnabled(false);
    return new FragmentViewHolder(frameLayout);
  }
  
  FrameLayout getContainer() {
    return (FrameLayout)this.itemView;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\viewpager2\adapter\FragmentViewHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */