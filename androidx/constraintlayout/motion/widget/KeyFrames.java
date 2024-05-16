package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.util.Log;
import android.util.Xml;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class KeyFrames {
  private static final String CUSTOM_ATTRIBUTE = "CustomAttribute";
  
  private static final String CUSTOM_METHOD = "CustomMethod";
  
  private static final String TAG = "KeyFrames";
  
  public static final int UNSET = -1;
  
  static HashMap<String, Constructor<? extends Key>> sKeyMakers;
  
  private HashMap<Integer, ArrayList<Key>> mFramesMap = new HashMap<>();
  
  static {
    HashMap<Object, Object> hashMap = new HashMap<>();
    sKeyMakers = (HashMap)hashMap;
    try {
      hashMap.put("KeyAttribute", KeyAttributes.class.getConstructor(new Class[0]));
      sKeyMakers.put("KeyPosition", KeyPosition.class.getConstructor(new Class[0]));
      sKeyMakers.put("KeyCycle", KeyCycle.class.getConstructor(new Class[0]));
      sKeyMakers.put("KeyTimeCycle", KeyTimeCycle.class.getConstructor(new Class[0]));
      sKeyMakers.put("KeyTrigger", KeyTrigger.class.getConstructor(new Class[0]));
    } catch (NoSuchMethodException noSuchMethodException) {
      Log.e("KeyFrames", "unable to load", noSuchMethodException);
    } 
  }
  
  public KeyFrames() {}
  
  public KeyFrames(Context paramContext, XmlPullParser paramXmlPullParser) {
    Key key = null;
    try {
      int i = paramXmlPullParser.getEventType();
      while (i != 1) {
        boolean bool;
        Key key1;
        Exception exception1;
        String str;
        switch (i) {
          default:
            key1 = key;
            break;
          case 3:
            key1 = key;
            if ("KeyFrameSet".equals(paramXmlPullParser.getName()))
              return; 
            break;
          case 2:
            str = paramXmlPullParser.getName();
            bool = sKeyMakers.containsKey(str);
            if (bool) {
              key1 = key;
              try {
                Constructor<Key> constructor = (Constructor)sKeyMakers.get(str);
                if (constructor != null) {
                  key1 = key;
                  key = constructor.newInstance(new Object[0]);
                  key1 = key;
                  key.load(paramContext, Xml.asAttributeSet(paramXmlPullParser));
                  key1 = key;
                  addKey(key);
                  key1 = key;
                  break;
                } 
                key1 = key;
                NullPointerException nullPointerException = new NullPointerException();
                key1 = key;
                i = String.valueOf(str).length();
                key1 = key;
                StringBuilder stringBuilder = new StringBuilder();
                key1 = key;
                this(i + 23);
                key1 = key;
                this(stringBuilder.append("Keymaker for ").append(str).append(" not found").toString());
                key1 = key;
                throw nullPointerException;
              } catch (Exception exception2) {
                Log.e("KeyFrames", "unable to create ", exception2);
                break;
              } 
            } 
            if (str.equalsIgnoreCase("CustomAttribute")) {
              Exception exception = exception2;
              if (exception2 != null) {
                exception = exception2;
                if (((Key)exception2).mCustomConstraints != null) {
                  ConstraintAttribute.parse(paramContext, paramXmlPullParser, ((Key)exception2).mCustomConstraints);
                  exception = exception2;
                } 
              } 
              break;
            } 
            exception1 = exception2;
            if (str.equalsIgnoreCase("CustomMethod")) {
              exception1 = exception2;
              if (exception2 != null) {
                exception1 = exception2;
                if (((Key)exception2).mCustomConstraints != null) {
                  ConstraintAttribute.parse(paramContext, paramXmlPullParser, ((Key)exception2).mCustomConstraints);
                  exception1 = exception2;
                } 
              } 
            } 
            break;
          case 0:
            exception1 = exception2;
            break;
        } 
        i = paramXmlPullParser.next();
        exception2 = exception1;
      } 
    } catch (XmlPullParserException xmlPullParserException) {
      xmlPullParserException.printStackTrace();
    } catch (IOException iOException) {
      iOException.printStackTrace();
    } 
  }
  
  static String name(int paramInt, Context paramContext) {
    return paramContext.getResources().getResourceEntryName(paramInt);
  }
  
  public void addAllFrames(MotionController paramMotionController) {
    ArrayList<Key> arrayList = this.mFramesMap.get(Integer.valueOf(-1));
    if (arrayList != null)
      paramMotionController.addKeys(arrayList); 
  }
  
  public void addFrames(MotionController paramMotionController) {
    ArrayList<Key> arrayList = this.mFramesMap.get(Integer.valueOf(paramMotionController.mId));
    if (arrayList != null)
      paramMotionController.addKeys(arrayList); 
    arrayList = this.mFramesMap.get(Integer.valueOf(-1));
    if (arrayList != null)
      for (Key key : arrayList) {
        if (key.matches(((ConstraintLayout.LayoutParams)paramMotionController.mView.getLayoutParams()).constraintTag))
          paramMotionController.addKey(key); 
      }  
  }
  
  public void addKey(Key paramKey) {
    if (!this.mFramesMap.containsKey(Integer.valueOf(paramKey.mTargetId)))
      this.mFramesMap.put(Integer.valueOf(paramKey.mTargetId), new ArrayList<>()); 
    ArrayList<Key> arrayList = this.mFramesMap.get(Integer.valueOf(paramKey.mTargetId));
    if (arrayList != null)
      arrayList.add(paramKey); 
  }
  
  public ArrayList<Key> getKeyFramesForView(int paramInt) {
    return this.mFramesMap.get(Integer.valueOf(paramInt));
  }
  
  public Set<Integer> getKeys() {
    return this.mFramesMap.keySet();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\motion\widget\KeyFrames.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */