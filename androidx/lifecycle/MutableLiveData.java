package androidx.lifecycle;

public class MutableLiveData<T> extends LiveData<T> {
  public MutableLiveData() {}
  
  public MutableLiveData(T paramT) {
    super(paramT);
  }
  
  public void postValue(T paramT) {
    super.postValue(paramT);
  }
  
  public void setValue(T paramT) {
    super.setValue(paramT);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\lifecycle\MutableLiveData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */