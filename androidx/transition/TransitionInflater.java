package androidx.transition;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.InflateException;
import android.view.ViewGroup;
import androidx.collection.ArrayMap;
import androidx.core.content.res.TypedArrayUtils;
import java.io.IOException;
import java.lang.reflect.Constructor;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class TransitionInflater {
  private static final ArrayMap<String, Constructor<?>> CONSTRUCTORS;
  
  private static final Class<?>[] CONSTRUCTOR_SIGNATURE = new Class[] { Context.class, AttributeSet.class };
  
  private final Context mContext;
  
  static {
    CONSTRUCTORS = new ArrayMap();
  }
  
  private TransitionInflater(Context paramContext) {
    this.mContext = paramContext;
  }
  
  private Object createCustom(AttributeSet paramAttributeSet, Class<?> paramClass, String paramString) {
    Constructor<?> constructor;
    String str = paramAttributeSet.getAttributeValue(null, "class");
    if (str != null)
      try {
        synchronized (CONSTRUCTORS) {
          Constructor<?> constructor1 = (Constructor)null.get(str);
          constructor = constructor1;
          if (constructor1 == null) {
            Class<?> clazz = Class.forName(str, false, this.mContext.getClassLoader()).asSubclass(paramClass);
            constructor = constructor1;
            if (clazz != null) {
              constructor = clazz.getConstructor(CONSTRUCTOR_SIGNATURE);
              constructor.setAccessible(true);
              null.put(str, constructor);
            } 
          } 
          paramAttributeSet = (AttributeSet)constructor.newInstance(new Object[] { this.mContext, paramAttributeSet });
          return paramAttributeSet;
        } 
      } catch (Exception exception) {
        throw new InflateException("Could not instantiate " + paramClass + " class " + str, exception);
      }  
    throw new InflateException(constructor + " tag must have a 'class' attribute");
  }
  
  private Transition createTransitionFromXml(XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Transition paramTransition) throws XmlPullParserException, IOException {
    TransitionSet transitionSet;
    Transition transition = null;
    int i = paramXmlPullParser.getDepth();
    if (paramTransition instanceof TransitionSet) {
      transitionSet = (TransitionSet)paramTransition;
    } else {
      transitionSet = null;
    } 
    while (true) {
      int j = paramXmlPullParser.next();
      if ((j != 3 || paramXmlPullParser.getDepth() > i) && j != 1) {
        if (j != 2)
          continue; 
        String str = paramXmlPullParser.getName();
        if ("fade".equals(str)) {
          transition = new Fade(this.mContext, paramAttributeSet);
        } else if ("changeBounds".equals(str)) {
          transition = new ChangeBounds(this.mContext, paramAttributeSet);
        } else if ("slide".equals(str)) {
          transition = new Slide(this.mContext, paramAttributeSet);
        } else if ("explode".equals(str)) {
          transition = new Explode(this.mContext, paramAttributeSet);
        } else if ("changeImageTransform".equals(str)) {
          transition = new ChangeImageTransform(this.mContext, paramAttributeSet);
        } else if ("changeTransform".equals(str)) {
          transition = new ChangeTransform(this.mContext, paramAttributeSet);
        } else if ("changeClipBounds".equals(str)) {
          transition = new ChangeClipBounds(this.mContext, paramAttributeSet);
        } else if ("autoTransition".equals(str)) {
          transition = new AutoTransition(this.mContext, paramAttributeSet);
        } else if ("changeScroll".equals(str)) {
          transition = new ChangeScroll(this.mContext, paramAttributeSet);
        } else if ("transitionSet".equals(str)) {
          transition = new TransitionSet(this.mContext, paramAttributeSet);
        } else if ("transition".equals(str)) {
          transition = (Transition)createCustom(paramAttributeSet, Transition.class, "transition");
        } else if ("targets".equals(str)) {
          getTargetIds(paramXmlPullParser, paramAttributeSet, paramTransition);
        } else if ("arcMotion".equals(str)) {
          if (paramTransition != null) {
            paramTransition.setPathMotion(new ArcMotion(this.mContext, paramAttributeSet));
          } else {
            throw new RuntimeException("Invalid use of arcMotion element");
          } 
        } else if ("pathMotion".equals(str)) {
          if (paramTransition != null) {
            paramTransition.setPathMotion((PathMotion)createCustom(paramAttributeSet, PathMotion.class, "pathMotion"));
          } else {
            throw new RuntimeException("Invalid use of pathMotion element");
          } 
        } else if ("patternPathMotion".equals(str)) {
          if (paramTransition != null) {
            paramTransition.setPathMotion(new PatternPathMotion(this.mContext, paramAttributeSet));
          } else {
            throw new RuntimeException("Invalid use of patternPathMotion element");
          } 
        } else {
          throw new RuntimeException("Unknown scene name: " + paramXmlPullParser.getName());
        } 
        Transition transition1 = transition;
        if (transition != null) {
          if (!paramXmlPullParser.isEmptyElementTag())
            createTransitionFromXml(paramXmlPullParser, paramAttributeSet, transition); 
          if (transitionSet != null) {
            transitionSet.addTransition(transition);
            transition1 = null;
          } else if (paramTransition == null) {
            transition1 = transition;
          } else {
            throw new InflateException("Could not add transition to another transition.");
          } 
        } 
        transition = transition1;
        continue;
      } 
      break;
    } 
    return transition;
  }
  
  private TransitionManager createTransitionManagerFromXml(XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, ViewGroup paramViewGroup) throws XmlPullParserException, IOException {
    int i = paramXmlPullParser.getDepth();
    TransitionManager transitionManager = null;
    while (true) {
      int j = paramXmlPullParser.next();
      if ((j != 3 || paramXmlPullParser.getDepth() > i) && j != 1) {
        if (j != 2)
          continue; 
        String str = paramXmlPullParser.getName();
        if (str.equals("transitionManager")) {
          transitionManager = new TransitionManager();
          continue;
        } 
        if (str.equals("transition") && transitionManager != null) {
          loadTransition(paramAttributeSet, paramXmlPullParser, paramViewGroup, transitionManager);
          continue;
        } 
        throw new RuntimeException("Unknown scene name: " + paramXmlPullParser.getName());
      } 
      break;
    } 
    return transitionManager;
  }
  
  public static TransitionInflater from(Context paramContext) {
    return new TransitionInflater(paramContext);
  }
  
  private void getTargetIds(XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Transition paramTransition) throws XmlPullParserException, IOException {
    int i = paramXmlPullParser.getDepth();
    while (true) {
      TypedArray typedArray;
      int j = paramXmlPullParser.next();
      if ((j != 3 || paramXmlPullParser.getDepth() > i) && j != 1) {
        if (j != 2)
          continue; 
        if (paramXmlPullParser.getName().equals("target")) {
          typedArray = this.mContext.obtainStyledAttributes(paramAttributeSet, Styleable.TRANSITION_TARGET);
          j = TypedArrayUtils.getNamedResourceId(typedArray, paramXmlPullParser, "targetId", 1, 0);
          if (j != 0) {
            paramTransition.addTarget(j);
          } else {
            j = TypedArrayUtils.getNamedResourceId(typedArray, paramXmlPullParser, "excludeId", 2, 0);
            if (j != 0) {
              paramTransition.excludeTarget(j, true);
            } else {
              String str = TypedArrayUtils.getNamedString(typedArray, paramXmlPullParser, "targetName", 4);
              if (str != null) {
                paramTransition.addTarget(str);
              } else {
                str = TypedArrayUtils.getNamedString(typedArray, paramXmlPullParser, "excludeName", 5);
                if (str != null) {
                  paramTransition.excludeTarget(str, true);
                } else {
                  String str1 = TypedArrayUtils.getNamedString(typedArray, paramXmlPullParser, "excludeClass", 3);
                  if (str1 != null) {
                    str = str1;
                    try {
                      paramTransition.excludeTarget(Class.forName(str1), true);
                      typedArray.recycle();
                    } catch (ClassNotFoundException classNotFoundException) {
                      typedArray.recycle();
                      throw new RuntimeException("Could not create " + str, classNotFoundException);
                    } 
                    continue;
                  } 
                  str = str1;
                  String str2 = TypedArrayUtils.getNamedString(typedArray, (XmlPullParser)classNotFoundException, "targetClass", 0);
                  str1 = str2;
                  if (str2 != null) {
                    str = str1;
                    paramTransition.addTarget(Class.forName(str1));
                  } 
                } 
              } 
            } 
          } 
        } else {
          throw new RuntimeException("Unknown scene name: " + classNotFoundException.getName());
        } 
      } else {
        break;
      } 
      typedArray.recycle();
    } 
  }
  
  private void loadTransition(AttributeSet paramAttributeSet, XmlPullParser paramXmlPullParser, ViewGroup paramViewGroup, TransitionManager paramTransitionManager) throws Resources.NotFoundException {
    Scene scene1;
    Scene scene2;
    TypedArray typedArray = this.mContext.obtainStyledAttributes(paramAttributeSet, Styleable.TRANSITION_MANAGER);
    int i = TypedArrayUtils.getNamedResourceId(typedArray, paramXmlPullParser, "transition", 2, -1);
    int j = TypedArrayUtils.getNamedResourceId(typedArray, paramXmlPullParser, "fromScene", 0, -1);
    XmlPullParser xmlPullParser = null;
    if (j < 0) {
      paramAttributeSet = null;
    } else {
      scene1 = Scene.getSceneForLayout(paramViewGroup, j, this.mContext);
    } 
    j = TypedArrayUtils.getNamedResourceId(typedArray, paramXmlPullParser, "toScene", 1, -1);
    if (j < 0) {
      paramXmlPullParser = xmlPullParser;
    } else {
      scene2 = Scene.getSceneForLayout(paramViewGroup, j, this.mContext);
    } 
    if (i >= 0) {
      Transition transition = inflateTransition(i);
      if (transition != null)
        if (scene2 != null) {
          if (scene1 == null) {
            paramTransitionManager.setTransition(scene2, transition);
          } else {
            paramTransitionManager.setTransition(scene1, scene2, transition);
          } 
        } else {
          throw new RuntimeException("No toScene for transition ID " + i);
        }  
    } 
    typedArray.recycle();
  }
  
  public Transition inflateTransition(int paramInt) {
    Exception exception;
    XmlResourceParser xmlResourceParser = this.mContext.getResources().getXml(paramInt);
    try {
      Transition transition = createTransitionFromXml((XmlPullParser)xmlResourceParser, Xml.asAttributeSet((XmlPullParser)xmlResourceParser), null);
      xmlResourceParser.close();
      return transition;
    } catch (XmlPullParserException xmlPullParserException) {
      InflateException inflateException = new InflateException();
      this(xmlPullParserException.getMessage(), (Throwable)xmlPullParserException);
      throw inflateException;
    } catch (IOException iOException) {
      InflateException inflateException = new InflateException();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      this(stringBuilder.append(xmlResourceParser.getPositionDescription()).append(": ").append(iOException.getMessage()).toString(), iOException);
      throw inflateException;
    } finally {}
    xmlResourceParser.close();
    throw exception;
  }
  
  public TransitionManager inflateTransitionManager(int paramInt, ViewGroup paramViewGroup) {
    XmlResourceParser xmlResourceParser = this.mContext.getResources().getXml(paramInt);
    try {
      TransitionManager transitionManager = createTransitionManagerFromXml((XmlPullParser)xmlResourceParser, Xml.asAttributeSet((XmlPullParser)xmlResourceParser), paramViewGroup);
      xmlResourceParser.close();
      return transitionManager;
    } catch (XmlPullParserException xmlPullParserException) {
      InflateException inflateException = new InflateException();
      this(xmlPullParserException.getMessage());
      inflateException.initCause((Throwable)xmlPullParserException);
      throw inflateException;
    } catch (IOException iOException) {
      InflateException inflateException = new InflateException();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      this(stringBuilder.append(xmlResourceParser.getPositionDescription()).append(": ").append(iOException.getMessage()).toString());
      inflateException.initCause(iOException);
      throw inflateException;
    } finally {}
    xmlResourceParser.close();
    throw paramViewGroup;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\TransitionInflater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */