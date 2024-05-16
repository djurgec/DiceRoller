package com.bumptech.glide.load;

import android.content.Context;
import com.bumptech.glide.load.engine.Resource;

public interface Transformation<T> extends Key {
  Resource<T> transform(Context paramContext, Resource<T> paramResource, int paramInt1, int paramInt2);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\Transformation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */