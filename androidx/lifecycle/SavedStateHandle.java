package androidx.lifecycle;

import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;
import androidx.savedstate.SavedStateRegistry;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class SavedStateHandle {
  private static final Class[] ACCEPTABLE_CLASSES;
  
  private static final String KEYS = "keys";
  
  private static final String VALUES = "values";
  
  private final Map<String, SavingStateLiveData<?>> mLiveDatas = new HashMap<>();
  
  final Map<String, Object> mRegular = new HashMap<>();
  
  private final SavedStateRegistry.SavedStateProvider mSavedStateProvider = new SavedStateRegistry.SavedStateProvider() {
      final SavedStateHandle this$0;
      
      public Bundle saveState() {
        for (Map.Entry<?, ?> entry : (new HashMap<>(SavedStateHandle.this.mSavedStateProviders)).entrySet()) {
          Bundle bundle1 = ((SavedStateRegistry.SavedStateProvider)entry.getValue()).saveState();
          SavedStateHandle.this.set((String)entry.getKey(), bundle1);
        } 
        Set<String> set = SavedStateHandle.this.mRegular.keySet();
        ArrayList<String> arrayList1 = new ArrayList(set.size());
        ArrayList arrayList = new ArrayList(arrayList1.size());
        for (String str : set) {
          arrayList1.add(str);
          arrayList.add(SavedStateHandle.this.mRegular.get(str));
        } 
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("keys", arrayList1);
        bundle.putParcelableArrayList("values", arrayList);
        return bundle;
      }
    };
  
  final Map<String, SavedStateRegistry.SavedStateProvider> mSavedStateProviders = new HashMap<>();
  
  static {
    Class<int> clazz1;
    Class<int> clazz2;
    Class<boolean> clazz9 = boolean.class;
    Class<double> clazz3 = double.class;
    Class<int> clazz6 = int.class;
    Class<long> clazz8 = long.class;
    Class<byte> clazz5 = byte.class;
    Class<char> clazz7 = char.class;
    Class<float> clazz = float.class;
    Class<short> clazz4 = short.class;
    if (Build.VERSION.SDK_INT >= 21) {
      Class<Size> clazz10 = Size.class;
    } else {
      clazz1 = int.class;
    } 
    if (Build.VERSION.SDK_INT >= 21) {
      Class<SizeF> clazz10 = SizeF.class;
    } else {
      clazz2 = int.class;
    } 
    ACCEPTABLE_CLASSES = new Class[] { 
        clazz9, boolean[].class, clazz3, double[].class, clazz6, int[].class, clazz8, long[].class, String.class, String[].class, 
        Binder.class, Bundle.class, clazz5, byte[].class, clazz7, char[].class, CharSequence.class, CharSequence[].class, ArrayList.class, clazz, 
        float[].class, Parcelable.class, Parcelable[].class, Serializable.class, clazz4, short[].class, SparseArray.class, clazz1, clazz2 };
  }
  
  public SavedStateHandle() {}
  
  public SavedStateHandle(Map<String, Object> paramMap) {}
  
  static SavedStateHandle createHandle(Bundle paramBundle1, Bundle paramBundle2) {
    if (paramBundle1 == null && paramBundle2 == null)
      return new SavedStateHandle(); 
    HashMap<Object, Object> hashMap = new HashMap<>();
    if (paramBundle2 != null)
      for (String str : paramBundle2.keySet())
        hashMap.put(str, paramBundle2.get(str));  
    if (paramBundle1 == null)
      return new SavedStateHandle((Map)hashMap); 
    ArrayList<String> arrayList1 = paramBundle1.getParcelableArrayList("keys");
    ArrayList arrayList = paramBundle1.getParcelableArrayList("values");
    if (arrayList1 != null && arrayList != null && arrayList1.size() == arrayList.size()) {
      for (byte b = 0; b < arrayList1.size(); b++)
        hashMap.put(arrayList1.get(b), arrayList.get(b)); 
      return new SavedStateHandle((Map)hashMap);
    } 
    throw new IllegalStateException("Invalid bundle passed as restored state");
  }
  
  private <T> MutableLiveData<T> getLiveDataInternal(String paramString, boolean paramBoolean, T paramT) {
    SavingStateLiveData<SavingStateLiveData> savingStateLiveData;
    MutableLiveData<T> mutableLiveData = this.mLiveDatas.get(paramString);
    if (mutableLiveData != null)
      return mutableLiveData; 
    if (this.mRegular.containsKey(paramString)) {
      savingStateLiveData = new SavingStateLiveData(this, paramString, this.mRegular.get(paramString));
    } else if (paramBoolean) {
      savingStateLiveData = new SavingStateLiveData<>(this, paramString, savingStateLiveData);
    } else {
      savingStateLiveData = new SavingStateLiveData<>(this, paramString);
    } 
    this.mLiveDatas.put(paramString, savingStateLiveData);
    return (MutableLiveData)savingStateLiveData;
  }
  
  private static void validateValue(Object paramObject) {
    if (paramObject == null)
      return; 
    Class[] arrayOfClass = ACCEPTABLE_CLASSES;
    int i = arrayOfClass.length;
    for (byte b = 0; b < i; b++) {
      if (arrayOfClass[b].isInstance(paramObject))
        return; 
    } 
    throw new IllegalArgumentException("Can't put value with type " + paramObject.getClass() + " into saved state");
  }
  
  public void clearSavedStateProvider(String paramString) {
    this.mSavedStateProviders.remove(paramString);
  }
  
  public boolean contains(String paramString) {
    return this.mRegular.containsKey(paramString);
  }
  
  public <T> T get(String paramString) {
    return (T)this.mRegular.get(paramString);
  }
  
  public <T> MutableLiveData<T> getLiveData(String paramString) {
    return getLiveDataInternal(paramString, false, null);
  }
  
  public <T> MutableLiveData<T> getLiveData(String paramString, T paramT) {
    return getLiveDataInternal(paramString, true, paramT);
  }
  
  public Set<String> keys() {
    HashSet<String> hashSet = new HashSet(this.mRegular.keySet());
    hashSet.addAll(this.mSavedStateProviders.keySet());
    hashSet.addAll(this.mLiveDatas.keySet());
    return hashSet;
  }
  
  public <T> T remove(String paramString) {
    Object object = this.mRegular.remove(paramString);
    SavingStateLiveData savingStateLiveData = this.mLiveDatas.remove(paramString);
    if (savingStateLiveData != null)
      savingStateLiveData.detach(); 
    return (T)object;
  }
  
  SavedStateRegistry.SavedStateProvider savedStateProvider() {
    return this.mSavedStateProvider;
  }
  
  public <T> void set(String paramString, T paramT) {
    validateValue(paramT);
    MutableLiveData<T> mutableLiveData = this.mLiveDatas.get(paramString);
    if (mutableLiveData != null) {
      mutableLiveData.setValue(paramT);
    } else {
      this.mRegular.put(paramString, paramT);
    } 
  }
  
  public void setSavedStateProvider(String paramString, SavedStateRegistry.SavedStateProvider paramSavedStateProvider) {
    this.mSavedStateProviders.put(paramString, paramSavedStateProvider);
  }
  
  static class SavingStateLiveData<T> extends MutableLiveData<T> {
    private SavedStateHandle mHandle;
    
    private String mKey;
    
    SavingStateLiveData(SavedStateHandle param1SavedStateHandle, String param1String) {
      this.mKey = param1String;
      this.mHandle = param1SavedStateHandle;
    }
    
    SavingStateLiveData(SavedStateHandle param1SavedStateHandle, String param1String, T param1T) {
      super(param1T);
      this.mKey = param1String;
      this.mHandle = param1SavedStateHandle;
    }
    
    void detach() {
      this.mHandle = null;
    }
    
    public void setValue(T param1T) {
      SavedStateHandle savedStateHandle = this.mHandle;
      if (savedStateHandle != null)
        savedStateHandle.mRegular.put(this.mKey, param1T); 
      super.setValue(param1T);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\lifecycle\SavedStateHandle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */