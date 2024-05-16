package androidx.constraintlayout.core.state;

import androidx.constraintlayout.core.motion.Motion;
import androidx.constraintlayout.core.motion.MotionWidget;
import androidx.constraintlayout.core.motion.key.MotionKey;
import androidx.constraintlayout.core.motion.key.MotionKeyAttributes;
import androidx.constraintlayout.core.motion.key.MotionKeyCycle;
import androidx.constraintlayout.core.motion.key.MotionKeyPosition;
import androidx.constraintlayout.core.motion.utils.Easing;
import androidx.constraintlayout.core.motion.utils.KeyCache;
import androidx.constraintlayout.core.motion.utils.TypedBundle;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import java.util.ArrayList;
import java.util.HashMap;

public class Transition {
  static final int ANTICIPATE = 6;
  
  static final int BOUNCE = 4;
  
  static final int EASE_IN = 1;
  
  static final int EASE_IN_OUT = 0;
  
  static final int EASE_OUT = 2;
  
  public static final int END = 1;
  
  public static final int INTERPOLATED = 2;
  
  private static final int INTERPOLATOR_REFERENCE_ID = -2;
  
  static final int LINEAR = 3;
  
  static final int OVERSHOOT = 5;
  
  private static final int SPLINE_STRING = -1;
  
  public static final int START = 0;
  
  HashMap<Integer, HashMap<String, KeyPosition>> keyPositions = new HashMap<>();
  
  private int mAutoTransition = 0;
  
  private int mDefaultInterpolator = 0;
  
  private String mDefaultInterpolatorString = null;
  
  private int mDuration = 400;
  
  private float mStagger = 0.0F;
  
  private int pathMotionArc = -1;
  
  private HashMap<String, WidgetState> state = new HashMap<>();
  
  public static Interpolator getInterpolator(int paramInt, String paramString) {
    switch (paramInt) {
      default:
        return null;
      case 6:
        return Transition$$ExternalSyntheticLambda5.INSTANCE;
      case 5:
        return Transition$$ExternalSyntheticLambda6.INSTANCE;
      case 4:
        return Transition$$ExternalSyntheticLambda7.INSTANCE;
      case 3:
        return Transition$$ExternalSyntheticLambda4.INSTANCE;
      case 2:
        return Transition$$ExternalSyntheticLambda3.INSTANCE;
      case 1:
        return Transition$$ExternalSyntheticLambda2.INSTANCE;
      case 0:
        return Transition$$ExternalSyntheticLambda1.INSTANCE;
      case -1:
        break;
    } 
    return new Transition$$ExternalSyntheticLambda0(paramString);
  }
  
  private WidgetState getWidgetState(String paramString) {
    return this.state.get(paramString);
  }
  
  private WidgetState getWidgetState(String paramString, ConstraintWidget paramConstraintWidget, int paramInt) {
    WidgetState widgetState2 = this.state.get(paramString);
    WidgetState widgetState1 = widgetState2;
    if (widgetState2 == null) {
      widgetState2 = new WidgetState();
      if (this.pathMotionArc != -1)
        widgetState2.motionControl.setPathMotionArc(this.pathMotionArc); 
      this.state.put(paramString, widgetState2);
      widgetState1 = widgetState2;
      if (paramConstraintWidget != null) {
        widgetState2.update(paramConstraintWidget, paramInt);
        widgetState1 = widgetState2;
      } 
    } 
    return widgetState1;
  }
  
  public void addCustomColor(int paramInt1, String paramString1, String paramString2, int paramInt2) {
    getWidgetState(paramString1, null, paramInt1).getFrame(paramInt1).addCustomColor(paramString2, paramInt2);
  }
  
  public void addCustomFloat(int paramInt, String paramString1, String paramString2, float paramFloat) {
    getWidgetState(paramString1, null, paramInt).getFrame(paramInt).addCustomFloat(paramString2, paramFloat);
  }
  
  public void addKeyAttribute(String paramString, TypedBundle paramTypedBundle) {
    getWidgetState(paramString, null, 0).setKeyAttribute(paramTypedBundle);
  }
  
  public void addKeyCycle(String paramString, TypedBundle paramTypedBundle) {
    getWidgetState(paramString, null, 0).setKeyCycle(paramTypedBundle);
  }
  
  public void addKeyPosition(String paramString, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2) {
    TypedBundle typedBundle = new TypedBundle();
    typedBundle.add(510, 2);
    typedBundle.add(100, paramInt1);
    typedBundle.add(506, paramFloat1);
    typedBundle.add(507, paramFloat2);
    getWidgetState(paramString, null, 0).setKeyPosition(typedBundle);
    KeyPosition keyPosition = new KeyPosition(paramString, paramInt1, paramInt2, paramFloat1, paramFloat2);
    HashMap<Object, Object> hashMap2 = (HashMap)this.keyPositions.get(Integer.valueOf(paramInt1));
    HashMap<Object, Object> hashMap1 = hashMap2;
    if (hashMap2 == null) {
      hashMap1 = new HashMap<>();
      this.keyPositions.put(Integer.valueOf(paramInt1), hashMap1);
    } 
    hashMap1.put(paramString, keyPosition);
  }
  
  public void addKeyPosition(String paramString, TypedBundle paramTypedBundle) {
    getWidgetState(paramString, null, 0).setKeyPosition(paramTypedBundle);
  }
  
  public void clear() {
    this.state.clear();
  }
  
  public boolean contains(String paramString) {
    return this.state.containsKey(paramString);
  }
  
  public void fillKeyPositions(WidgetFrame paramWidgetFrame, float[] paramArrayOffloat1, float[] paramArrayOffloat2, float[] paramArrayOffloat3) {
    int i = 0;
    byte b = 0;
    while (b <= 100) {
      HashMap hashMap = this.keyPositions.get(Integer.valueOf(b));
      int j = i;
      if (hashMap != null) {
        KeyPosition keyPosition = (KeyPosition)hashMap.get(paramWidgetFrame.widget.stringId);
        j = i;
        if (keyPosition != null) {
          paramArrayOffloat1[i] = keyPosition.x;
          paramArrayOffloat2[i] = keyPosition.y;
          paramArrayOffloat3[i] = keyPosition.frame;
          j = i + 1;
        } 
      } 
      b++;
      i = j;
    } 
  }
  
  public KeyPosition findNextPosition(String paramString, int paramInt) {
    while (paramInt <= 100) {
      HashMap hashMap = this.keyPositions.get(Integer.valueOf(paramInt));
      if (hashMap != null) {
        KeyPosition keyPosition = (KeyPosition)hashMap.get(paramString);
        if (keyPosition != null)
          return keyPosition; 
      } 
      paramInt++;
    } 
    return null;
  }
  
  public KeyPosition findPreviousPosition(String paramString, int paramInt) {
    while (paramInt >= 0) {
      HashMap hashMap = this.keyPositions.get(Integer.valueOf(paramInt));
      if (hashMap != null) {
        KeyPosition keyPosition = (KeyPosition)hashMap.get(paramString);
        if (keyPosition != null)
          return keyPosition; 
      } 
      paramInt--;
    } 
    return null;
  }
  
  public int getAutoTransition() {
    return this.mAutoTransition;
  }
  
  public WidgetFrame getEnd(ConstraintWidget paramConstraintWidget) {
    return (getWidgetState(paramConstraintWidget.stringId, null, 1)).end;
  }
  
  public WidgetFrame getEnd(String paramString) {
    WidgetState widgetState = this.state.get(paramString);
    return (widgetState == null) ? null : widgetState.end;
  }
  
  public WidgetFrame getInterpolated(ConstraintWidget paramConstraintWidget) {
    return (getWidgetState(paramConstraintWidget.stringId, null, 2)).interpolated;
  }
  
  public WidgetFrame getInterpolated(String paramString) {
    WidgetState widgetState = this.state.get(paramString);
    return (widgetState == null) ? null : widgetState.interpolated;
  }
  
  public Interpolator getInterpolator() {
    return getInterpolator(this.mDefaultInterpolator, this.mDefaultInterpolatorString);
  }
  
  public int getKeyFrames(String paramString, float[] paramArrayOffloat, int[] paramArrayOfint1, int[] paramArrayOfint2) {
    return ((WidgetState)this.state.get(paramString)).motionControl.buildKeyFrames(paramArrayOffloat, paramArrayOfint1, paramArrayOfint2);
  }
  
  public Motion getMotion(String paramString) {
    return (getWidgetState(paramString, null, 0)).motionControl;
  }
  
  public int getNumberKeyPositions(WidgetFrame paramWidgetFrame) {
    int i = 0;
    byte b = 0;
    while (b <= 100) {
      HashMap hashMap = this.keyPositions.get(Integer.valueOf(b));
      int j = i;
      if (hashMap != null) {
        j = i;
        if ((KeyPosition)hashMap.get(paramWidgetFrame.widget.stringId) != null)
          j = i + 1; 
      } 
      b++;
      i = j;
    } 
    return i;
  }
  
  public float[] getPath(String paramString) {
    WidgetState widgetState = this.state.get(paramString);
    int i = 1000 / 16;
    float[] arrayOfFloat = new float[i * 2];
    widgetState.motionControl.buildPath(arrayOfFloat, i);
    return arrayOfFloat;
  }
  
  public WidgetFrame getStart(ConstraintWidget paramConstraintWidget) {
    return (getWidgetState(paramConstraintWidget.stringId, null, 0)).start;
  }
  
  public WidgetFrame getStart(String paramString) {
    WidgetState widgetState = this.state.get(paramString);
    return (widgetState == null) ? null : widgetState.start;
  }
  
  public boolean hasPositionKeyframes() {
    boolean bool;
    if (this.keyPositions.size() > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void interpolate(int paramInt1, int paramInt2, float paramFloat) {
    for (String str : this.state.keySet())
      ((WidgetState)this.state.get(str)).interpolate(paramInt1, paramInt2, paramFloat, this); 
  }
  
  public boolean isEmpty() {
    return this.state.isEmpty();
  }
  
  public void setTransitionProperties(TypedBundle paramTypedBundle) {
    this.pathMotionArc = paramTypedBundle.getInteger(509);
    this.mAutoTransition = paramTypedBundle.getInteger(704);
  }
  
  public void updateFrom(ConstraintWidgetContainer paramConstraintWidgetContainer, int paramInt) {
    ArrayList<ConstraintWidget> arrayList = paramConstraintWidgetContainer.getChildren();
    int i = arrayList.size();
    for (byte b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = arrayList.get(b);
      getWidgetState(constraintWidget.stringId, null, paramInt).update(constraintWidget, paramInt);
    } 
  }
  
  static class KeyPosition {
    int frame;
    
    String target;
    
    int type;
    
    float x;
    
    float y;
    
    public KeyPosition(String param1String, int param1Int1, int param1Int2, float param1Float1, float param1Float2) {
      this.target = param1String;
      this.frame = param1Int1;
      this.type = param1Int2;
      this.x = param1Float1;
      this.y = param1Float2;
    }
  }
  
  static class WidgetState {
    WidgetFrame end = new WidgetFrame();
    
    WidgetFrame interpolated = new WidgetFrame();
    
    Motion motionControl;
    
    MotionWidget motionWidgetEnd = new MotionWidget(this.end);
    
    MotionWidget motionWidgetInterpolated = new MotionWidget(this.interpolated);
    
    MotionWidget motionWidgetStart = new MotionWidget(this.start);
    
    KeyCache myKeyCache = new KeyCache();
    
    int myParentHeight = -1;
    
    int myParentWidth = -1;
    
    WidgetFrame start = new WidgetFrame();
    
    public WidgetState() {
      Motion motion = new Motion(this.motionWidgetStart);
      this.motionControl = motion;
      motion.setStart(this.motionWidgetStart);
      this.motionControl.setEnd(this.motionWidgetEnd);
    }
    
    public WidgetFrame getFrame(int param1Int) {
      return (param1Int == 0) ? this.start : ((param1Int == 1) ? this.end : this.interpolated);
    }
    
    public void interpolate(int param1Int1, int param1Int2, float param1Float, Transition param1Transition) {
      this.myParentHeight = param1Int2;
      this.myParentWidth = param1Int1;
      this.motionControl.setup(param1Int1, param1Int2, 1.0F, System.nanoTime());
      WidgetFrame.interpolate(param1Int1, param1Int2, this.interpolated, this.start, this.end, param1Transition, param1Float);
      this.interpolated.interpolatedPos = param1Float;
      this.motionControl.interpolate(this.motionWidgetInterpolated, param1Float, System.nanoTime(), this.myKeyCache);
    }
    
    public void setKeyAttribute(TypedBundle param1TypedBundle) {
      MotionKeyAttributes motionKeyAttributes = new MotionKeyAttributes();
      param1TypedBundle.applyDelta((TypedValues)motionKeyAttributes);
      this.motionControl.addKey((MotionKey)motionKeyAttributes);
    }
    
    public void setKeyCycle(TypedBundle param1TypedBundle) {
      MotionKeyCycle motionKeyCycle = new MotionKeyCycle();
      param1TypedBundle.applyDelta((TypedValues)motionKeyCycle);
      this.motionControl.addKey((MotionKey)motionKeyCycle);
    }
    
    public void setKeyPosition(TypedBundle param1TypedBundle) {
      MotionKeyPosition motionKeyPosition = new MotionKeyPosition();
      param1TypedBundle.applyDelta((TypedValues)motionKeyPosition);
      this.motionControl.addKey((MotionKey)motionKeyPosition);
    }
    
    public void update(ConstraintWidget param1ConstraintWidget, int param1Int) {
      if (param1Int == 0) {
        this.start.update(param1ConstraintWidget);
        this.motionControl.setStart(this.motionWidgetStart);
      } else if (param1Int == 1) {
        this.end.update(param1ConstraintWidget);
        this.motionControl.setEnd(this.motionWidgetEnd);
      } 
      this.myParentWidth = -1;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\state\Transition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */