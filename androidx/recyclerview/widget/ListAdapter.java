package androidx.recyclerview.widget;

import java.util.List;

public abstract class ListAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
  final AsyncListDiffer<T> mDiffer;
  
  private final AsyncListDiffer.ListListener<T> mListener;
  
  protected ListAdapter(AsyncDifferConfig<T> paramAsyncDifferConfig) {
    AsyncListDiffer.ListListener<T> listListener = new AsyncListDiffer.ListListener<T>() {
        final ListAdapter this$0;
        
        public void onCurrentListChanged(List<T> param1List1, List<T> param1List2) {
          ListAdapter.this.onCurrentListChanged(param1List1, param1List2);
        }
      };
    this.mListener = listListener;
    AsyncListDiffer<T> asyncListDiffer = new AsyncListDiffer<>(new AdapterListUpdateCallback(this), paramAsyncDifferConfig);
    this.mDiffer = asyncListDiffer;
    asyncListDiffer.addListListener(listListener);
  }
  
  protected ListAdapter(DiffUtil.ItemCallback<T> paramItemCallback) {
    AsyncListDiffer.ListListener<T> listListener = new AsyncListDiffer.ListListener<T>() {
        final ListAdapter this$0;
        
        public void onCurrentListChanged(List<T> param1List1, List<T> param1List2) {
          ListAdapter.this.onCurrentListChanged(param1List1, param1List2);
        }
      };
    this.mListener = listListener;
    AsyncListDiffer<T> asyncListDiffer = new AsyncListDiffer(new AdapterListUpdateCallback(this), (new AsyncDifferConfig.Builder(paramItemCallback)).build());
    this.mDiffer = asyncListDiffer;
    asyncListDiffer.addListListener(listListener);
  }
  
  public List<T> getCurrentList() {
    return this.mDiffer.getCurrentList();
  }
  
  protected T getItem(int paramInt) {
    return this.mDiffer.getCurrentList().get(paramInt);
  }
  
  public int getItemCount() {
    return this.mDiffer.getCurrentList().size();
  }
  
  public void onCurrentListChanged(List<T> paramList1, List<T> paramList2) {}
  
  public void submitList(List<T> paramList) {
    this.mDiffer.submitList(paramList);
  }
  
  public void submitList(List<T> paramList, Runnable paramRunnable) {
    this.mDiffer.submitList(paramList, paramRunnable);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\ListAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */