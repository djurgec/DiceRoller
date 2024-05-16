package com.bumptech.glide.request;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.target.Target;

public interface RequestListener<R> {
  boolean onLoadFailed(GlideException paramGlideException, Object paramObject, Target<R> paramTarget, boolean paramBoolean);
  
  boolean onResourceReady(R paramR, Object paramObject, Target<R> paramTarget, DataSource paramDataSource, boolean paramBoolean);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\request\RequestListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */