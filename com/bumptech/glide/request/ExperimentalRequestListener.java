package com.bumptech.glide.request;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.request.target.Target;

@Deprecated
public abstract class ExperimentalRequestListener<ResourceT> implements RequestListener<ResourceT> {
  public abstract boolean onResourceReady(ResourceT paramResourceT, Object paramObject, Target<ResourceT> paramTarget, DataSource paramDataSource, boolean paramBoolean1, boolean paramBoolean2);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\request\ExperimentalRequestListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */