package androidx.emoji2.text;

import android.text.Editable;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import androidx.core.util.Preconditions;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class SpannableBuilder extends SpannableStringBuilder {
  private final Class<?> mWatcherClass;
  
  private final List<WatcherWrapper> mWatchers = new ArrayList<>();
  
  SpannableBuilder(Class<?> paramClass) {
    Preconditions.checkNotNull(paramClass, "watcherClass cannot be null");
    this.mWatcherClass = paramClass;
  }
  
  SpannableBuilder(Class<?> paramClass, CharSequence paramCharSequence) {
    super(paramCharSequence);
    Preconditions.checkNotNull(paramClass, "watcherClass cannot be null");
    this.mWatcherClass = paramClass;
  }
  
  SpannableBuilder(Class<?> paramClass, CharSequence paramCharSequence, int paramInt1, int paramInt2) {
    super(paramCharSequence, paramInt1, paramInt2);
    Preconditions.checkNotNull(paramClass, "watcherClass cannot be null");
    this.mWatcherClass = paramClass;
  }
  
  private void blockWatchers() {
    for (byte b = 0; b < this.mWatchers.size(); b++)
      ((WatcherWrapper)this.mWatchers.get(b)).blockCalls(); 
  }
  
  public static SpannableBuilder create(Class<?> paramClass, CharSequence paramCharSequence) {
    return new SpannableBuilder(paramClass, paramCharSequence);
  }
  
  private void fireWatchers() {
    for (byte b = 0; b < this.mWatchers.size(); b++)
      ((WatcherWrapper)this.mWatchers.get(b)).onTextChanged((CharSequence)this, 0, length(), length()); 
  }
  
  private WatcherWrapper getWatcherFor(Object paramObject) {
    for (byte b = 0; b < this.mWatchers.size(); b++) {
      WatcherWrapper watcherWrapper = this.mWatchers.get(b);
      if (watcherWrapper.mObject == paramObject)
        return watcherWrapper; 
    } 
    return null;
  }
  
  private boolean isWatcher(Class<?> paramClass) {
    boolean bool;
    if (this.mWatcherClass == paramClass) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean isWatcher(Object paramObject) {
    boolean bool;
    if (paramObject != null && isWatcher(paramObject.getClass())) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void unblockwatchers() {
    for (byte b = 0; b < this.mWatchers.size(); b++)
      ((WatcherWrapper)this.mWatchers.get(b)).unblockCalls(); 
  }
  
  public SpannableStringBuilder append(char paramChar) {
    super.append(paramChar);
    return this;
  }
  
  public SpannableStringBuilder append(CharSequence paramCharSequence) {
    super.append(paramCharSequence);
    return this;
  }
  
  public SpannableStringBuilder append(CharSequence paramCharSequence, int paramInt1, int paramInt2) {
    super.append(paramCharSequence, paramInt1, paramInt2);
    return this;
  }
  
  public SpannableStringBuilder append(CharSequence paramCharSequence, Object paramObject, int paramInt) {
    super.append(paramCharSequence, paramObject, paramInt);
    return this;
  }
  
  public void beginBatchEdit() {
    blockWatchers();
  }
  
  public SpannableStringBuilder delete(int paramInt1, int paramInt2) {
    super.delete(paramInt1, paramInt2);
    return this;
  }
  
  public void endBatchEdit() {
    unblockwatchers();
    fireWatchers();
  }
  
  public int getSpanEnd(Object paramObject) {
    Object object = paramObject;
    if (isWatcher(paramObject)) {
      WatcherWrapper watcherWrapper = getWatcherFor(paramObject);
      object = paramObject;
      if (watcherWrapper != null)
        object = watcherWrapper; 
    } 
    return super.getSpanEnd(object);
  }
  
  public int getSpanFlags(Object paramObject) {
    Object object = paramObject;
    if (isWatcher(paramObject)) {
      WatcherWrapper watcherWrapper = getWatcherFor(paramObject);
      object = paramObject;
      if (watcherWrapper != null)
        object = watcherWrapper; 
    } 
    return super.getSpanFlags(object);
  }
  
  public int getSpanStart(Object paramObject) {
    Object object = paramObject;
    if (isWatcher(paramObject)) {
      WatcherWrapper watcherWrapper = getWatcherFor(paramObject);
      object = paramObject;
      if (watcherWrapper != null)
        object = watcherWrapper; 
    } 
    return super.getSpanStart(object);
  }
  
  public <T> T[] getSpans(int paramInt1, int paramInt2, Class<T> paramClass) {
    Object[] arrayOfObject;
    if (isWatcher(paramClass)) {
      WatcherWrapper[] arrayOfWatcherWrapper = (WatcherWrapper[])super.getSpans(paramInt1, paramInt2, WatcherWrapper.class);
      arrayOfObject = (Object[])Array.newInstance(paramClass, arrayOfWatcherWrapper.length);
      for (paramInt1 = 0; paramInt1 < arrayOfWatcherWrapper.length; paramInt1++)
        arrayOfObject[paramInt1] = (arrayOfWatcherWrapper[paramInt1]).mObject; 
      return (T[])arrayOfObject;
    } 
    return (T[])super.getSpans(paramInt1, paramInt2, (Class)arrayOfObject);
  }
  
  public SpannableStringBuilder insert(int paramInt, CharSequence paramCharSequence) {
    super.insert(paramInt, paramCharSequence);
    return this;
  }
  
  public SpannableStringBuilder insert(int paramInt1, CharSequence paramCharSequence, int paramInt2, int paramInt3) {
    super.insert(paramInt1, paramCharSequence, paramInt2, paramInt3);
    return this;
  }
  
  public int nextSpanTransition(int paramInt1, int paramInt2, Class<?> paramClass) {
    if (paramClass != null) {
      Class<?> clazz1 = paramClass;
      if (isWatcher(paramClass)) {
        clazz1 = WatcherWrapper.class;
        return super.nextSpanTransition(paramInt1, paramInt2, clazz1);
      } 
      return super.nextSpanTransition(paramInt1, paramInt2, clazz1);
    } 
    Class<WatcherWrapper> clazz = WatcherWrapper.class;
    return super.nextSpanTransition(paramInt1, paramInt2, clazz);
  }
  
  public void removeSpan(Object paramObject) {
    Object object;
    if (isWatcher(paramObject)) {
      WatcherWrapper watcherWrapper = getWatcherFor(paramObject);
      object = watcherWrapper;
      if (watcherWrapper != null) {
        paramObject = watcherWrapper;
        object = watcherWrapper;
      } 
    } else {
      object = null;
    } 
    super.removeSpan(paramObject);
    if (object != null)
      this.mWatchers.remove(object); 
  }
  
  public SpannableStringBuilder replace(int paramInt1, int paramInt2, CharSequence paramCharSequence) {
    blockWatchers();
    super.replace(paramInt1, paramInt2, paramCharSequence);
    unblockwatchers();
    return this;
  }
  
  public SpannableStringBuilder replace(int paramInt1, int paramInt2, CharSequence paramCharSequence, int paramInt3, int paramInt4) {
    blockWatchers();
    super.replace(paramInt1, paramInt2, paramCharSequence, paramInt3, paramInt4);
    unblockwatchers();
    return this;
  }
  
  public void setSpan(Object paramObject, int paramInt1, int paramInt2, int paramInt3) {
    Object object = paramObject;
    if (isWatcher(paramObject)) {
      object = new WatcherWrapper(paramObject);
      this.mWatchers.add(object);
    } 
    super.setSpan(object, paramInt1, paramInt2, paramInt3);
  }
  
  public CharSequence subSequence(int paramInt1, int paramInt2) {
    return (CharSequence)new SpannableBuilder(this.mWatcherClass, (CharSequence)this, paramInt1, paramInt2);
  }
  
  private static class WatcherWrapper implements TextWatcher, SpanWatcher {
    private final AtomicInteger mBlockCalls = new AtomicInteger(0);
    
    final Object mObject;
    
    WatcherWrapper(Object param1Object) {
      this.mObject = param1Object;
    }
    
    private boolean isEmojiSpan(Object param1Object) {
      return param1Object instanceof EmojiSpan;
    }
    
    public void afterTextChanged(Editable param1Editable) {
      ((TextWatcher)this.mObject).afterTextChanged(param1Editable);
    }
    
    public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {
      ((TextWatcher)this.mObject).beforeTextChanged(param1CharSequence, param1Int1, param1Int2, param1Int3);
    }
    
    final void blockCalls() {
      this.mBlockCalls.incrementAndGet();
    }
    
    public void onSpanAdded(Spannable param1Spannable, Object param1Object, int param1Int1, int param1Int2) {
      if (this.mBlockCalls.get() > 0 && isEmojiSpan(param1Object))
        return; 
      ((SpanWatcher)this.mObject).onSpanAdded(param1Spannable, param1Object, param1Int1, param1Int2);
    }
    
    public void onSpanChanged(Spannable param1Spannable, Object param1Object, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      if (this.mBlockCalls.get() > 0 && isEmojiSpan(param1Object))
        return; 
      ((SpanWatcher)this.mObject).onSpanChanged(param1Spannable, param1Object, param1Int1, param1Int2, param1Int3, param1Int4);
    }
    
    public void onSpanRemoved(Spannable param1Spannable, Object param1Object, int param1Int1, int param1Int2) {
      if (this.mBlockCalls.get() > 0 && isEmojiSpan(param1Object))
        return; 
      ((SpanWatcher)this.mObject).onSpanRemoved(param1Spannable, param1Object, param1Int1, param1Int2);
    }
    
    public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {
      ((TextWatcher)this.mObject).onTextChanged(param1CharSequence, param1Int1, param1Int2, param1Int3);
    }
    
    final void unblockCalls() {
      this.mBlockCalls.decrementAndGet();
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\SpannableBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */