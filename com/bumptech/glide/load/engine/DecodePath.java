package com.bumptech.glide.load.engine;

import android.util.Log;
import androidx.core.util.Pools;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.util.Preconditions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DecodePath<DataType, ResourceType, Transcode> {
  private static final String TAG = "DecodePath";
  
  private final Class<DataType> dataClass;
  
  private final List<? extends ResourceDecoder<DataType, ResourceType>> decoders;
  
  private final String failureMessage;
  
  private final Pools.Pool<List<Throwable>> listPool;
  
  private final ResourceTranscoder<ResourceType, Transcode> transcoder;
  
  public DecodePath(Class<DataType> paramClass, Class<ResourceType> paramClass1, Class<Transcode> paramClass2, List<? extends ResourceDecoder<DataType, ResourceType>> paramList, ResourceTranscoder<ResourceType, Transcode> paramResourceTranscoder, Pools.Pool<List<Throwable>> paramPool) {
    this.dataClass = paramClass;
    this.decoders = paramList;
    this.transcoder = paramResourceTranscoder;
    this.listPool = paramPool;
    this.failureMessage = "Failed DecodePath{" + paramClass.getSimpleName() + "->" + paramClass1.getSimpleName() + "->" + paramClass2.getSimpleName() + "}";
  }
  
  private Resource<ResourceType> decodeResource(DataRewinder<DataType> paramDataRewinder, int paramInt1, int paramInt2, Options paramOptions) throws GlideException {
    List<Throwable> list = (List)Preconditions.checkNotNull(this.listPool.acquire());
    try {
      return decodeResourceWithList(paramDataRewinder, paramInt1, paramInt2, paramOptions, list);
    } finally {
      this.listPool.release(list);
    } 
  }
  
  private Resource<ResourceType> decodeResourceWithList(DataRewinder<DataType> paramDataRewinder, int paramInt1, int paramInt2, Options paramOptions, List<Throwable> paramList) throws GlideException {
    Resource<ResourceType> resource2;
    Resource<ResourceType> resource1 = null;
    byte b = 0;
    int i = this.decoders.size();
    while (true) {
      resource2 = resource1;
      if (b < i) {
        ResourceDecoder resourceDecoder = this.decoders.get(b);
        resource2 = resource1;
        try {
          if (resourceDecoder.handles(paramDataRewinder.rewindAndGet(), paramOptions))
            resource2 = resourceDecoder.decode(paramDataRewinder.rewindAndGet(), paramInt1, paramInt2, paramOptions); 
          resource1 = resource2;
        } catch (IOException|RuntimeException|OutOfMemoryError iOException) {
          if (Log.isLoggable("DecodePath", 2))
            Log.v("DecodePath", "Failed to decode data for " + resourceDecoder, iOException); 
          paramList.add(iOException);
        } 
        if (resource1 != null) {
          resource2 = resource1;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    if (resource2 != null)
      return resource2; 
    throw new GlideException(this.failureMessage, new ArrayList(paramList));
  }
  
  public Resource<Transcode> decode(DataRewinder<DataType> paramDataRewinder, int paramInt1, int paramInt2, Options paramOptions, DecodeCallback<ResourceType> paramDecodeCallback) throws GlideException {
    Resource<ResourceType> resource = paramDecodeCallback.onResourceDecoded(decodeResource(paramDataRewinder, paramInt1, paramInt2, paramOptions));
    return this.transcoder.transcode(resource, paramOptions);
  }
  
  public String toString() {
    return "DecodePath{ dataClass=" + this.dataClass + ", decoders=" + this.decoders + ", transcoder=" + this.transcoder + '}';
  }
  
  static interface DecodeCallback<ResourceType> {
    Resource<ResourceType> onResourceDecoded(Resource<ResourceType> param1Resource);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\DecodePath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */