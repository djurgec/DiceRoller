package com.bumptech.glide.request.transition;

import com.bumptech.glide.load.DataSource;

public interface TransitionFactory<R> {
  Transition<R> build(DataSource paramDataSource, boolean paramBoolean);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\request\transition\TransitionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */