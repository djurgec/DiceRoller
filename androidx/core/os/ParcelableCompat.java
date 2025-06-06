package androidx.core.os;

import android.os.Parcel;
import android.os.Parcelable;

@Deprecated
public final class ParcelableCompat {
  @Deprecated
  public static <T> Parcelable.Creator<T> newCreator(ParcelableCompatCreatorCallbacks<T> paramParcelableCompatCreatorCallbacks) {
    return (Parcelable.Creator<T>)new ParcelableCompatCreatorHoneycombMR2<>(paramParcelableCompatCreatorCallbacks);
  }
  
  static class ParcelableCompatCreatorHoneycombMR2<T> implements Parcelable.ClassLoaderCreator<T> {
    private final ParcelableCompatCreatorCallbacks<T> mCallbacks;
    
    ParcelableCompatCreatorHoneycombMR2(ParcelableCompatCreatorCallbacks<T> param1ParcelableCompatCreatorCallbacks) {
      this.mCallbacks = param1ParcelableCompatCreatorCallbacks;
    }
    
    public T createFromParcel(Parcel param1Parcel) {
      return this.mCallbacks.createFromParcel(param1Parcel, null);
    }
    
    public T createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return this.mCallbacks.createFromParcel(param1Parcel, param1ClassLoader);
    }
    
    public T[] newArray(int param1Int) {
      return this.mCallbacks.newArray(param1Int);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\os\ParcelableCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */