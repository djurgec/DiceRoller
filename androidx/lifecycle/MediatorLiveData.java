package androidx.lifecycle;

import androidx.arch.core.internal.SafeIterableMap;
import java.util.Iterator;
import java.util.Map;

public class MediatorLiveData<T> extends MutableLiveData<T> {
  private SafeIterableMap<LiveData<?>, Source<?>> mSources = new SafeIterableMap();
  
  public <S> void addSource(LiveData<S> paramLiveData, Observer<? super S> paramObserver) {
    Source<S> source1 = new Source<>(paramLiveData, paramObserver);
    Source source = (Source)this.mSources.putIfAbsent(paramLiveData, source1);
    if (source == null || source.mObserver == paramObserver) {
      if (source != null)
        return; 
      if (hasActiveObservers())
        source1.plug(); 
      return;
    } 
    throw new IllegalArgumentException("This source was already added with the different observer");
  }
  
  protected void onActive() {
    Iterator<Map.Entry> iterator = this.mSources.iterator();
    while (iterator.hasNext())
      ((Source)((Map.Entry)iterator.next()).getValue()).plug(); 
  }
  
  protected void onInactive() {
    Iterator<Map.Entry> iterator = this.mSources.iterator();
    while (iterator.hasNext())
      ((Source)((Map.Entry)iterator.next()).getValue()).unplug(); 
  }
  
  public <S> void removeSource(LiveData<S> paramLiveData) {
    Source source = (Source)this.mSources.remove(paramLiveData);
    if (source != null)
      source.unplug(); 
  }
  
  private static class Source<V> implements Observer<V> {
    final LiveData<V> mLiveData;
    
    final Observer<? super V> mObserver;
    
    int mVersion = -1;
    
    Source(LiveData<V> param1LiveData, Observer<? super V> param1Observer) {
      this.mLiveData = param1LiveData;
      this.mObserver = param1Observer;
    }
    
    public void onChanged(V param1V) {
      if (this.mVersion != this.mLiveData.getVersion()) {
        this.mVersion = this.mLiveData.getVersion();
        this.mObserver.onChanged(param1V);
      } 
    }
    
    void plug() {
      this.mLiveData.observeForever(this);
    }
    
    void unplug() {
      this.mLiveData.removeObserver(this);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\lifecycle\MediatorLiveData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */