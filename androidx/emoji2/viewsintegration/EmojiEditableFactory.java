package androidx.emoji2.viewsintegration;

import android.text.Editable;
import androidx.emoji2.text.SpannableBuilder;

final class EmojiEditableFactory extends Editable.Factory {
  private static final Object INSTANCE_LOCK = new Object();
  
  private static volatile Editable.Factory sInstance;
  
  private static Class<?> sWatcherClass;
  
  private EmojiEditableFactory() {
    try {
      sWatcherClass = Class.forName("android.text.DynamicLayout$ChangeWatcher", false, getClass().getClassLoader());
    } finally {
      Exception exception;
    } 
  }
  
  public static Editable.Factory getInstance() {
    if (sInstance == null)
      synchronized (INSTANCE_LOCK) {
        if (sInstance == null) {
          EmojiEditableFactory emojiEditableFactory = new EmojiEditableFactory();
          this();
          sInstance = emojiEditableFactory;
        } 
      }  
    return sInstance;
  }
  
  public Editable newEditable(CharSequence paramCharSequence) {
    Class<?> clazz = sWatcherClass;
    return (Editable)((clazz != null) ? SpannableBuilder.create(clazz, paramCharSequence) : super.newEditable(paramCharSequence));
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\viewsintegration\EmojiEditableFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */