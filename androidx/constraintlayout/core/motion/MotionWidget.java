package androidx.constraintlayout.core.motion;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.core.state.WidgetFrame;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import java.util.Set;

public class MotionWidget implements TypedValues {
  public static final int FILL_PARENT = -1;
  
  public static final int GONE_UNSET = -2147483648;
  
  private static final int INTERNAL_MATCH_CONSTRAINT = -3;
  
  private static final int INTERNAL_MATCH_PARENT = -1;
  
  private static final int INTERNAL_WRAP_CONTENT = -2;
  
  private static final int INTERNAL_WRAP_CONTENT_CONSTRAINED = -4;
  
  public static final int INVISIBLE = 0;
  
  public static final int MATCH_CONSTRAINT = 0;
  
  public static final int MATCH_CONSTRAINT_WRAP = 1;
  
  public static final int MATCH_PARENT = -1;
  
  public static final int PARENT_ID = 0;
  
  public static final int ROTATE_LEFT_OF_PORTRATE = 4;
  
  public static final int ROTATE_NONE = 0;
  
  public static final int ROTATE_PORTRATE_OF_LEFT = 2;
  
  public static final int ROTATE_PORTRATE_OF_RIGHT = 1;
  
  public static final int ROTATE_RIGHT_OF_PORTRATE = 3;
  
  public static final int UNSET = -1;
  
  public static final int VISIBILITY_MODE_IGNORE = 1;
  
  public static final int VISIBILITY_MODE_NORMAL = 0;
  
  public static final int VISIBLE = 4;
  
  public static final int WRAP_CONTENT = -2;
  
  private float mProgress;
  
  float mTransitionPathRotate;
  
  Motion motion = new Motion();
  
  PropertySet propertySet = new PropertySet();
  
  WidgetFrame widgetFrame = new WidgetFrame();
  
  public MotionWidget() {}
  
  public MotionWidget(WidgetFrame paramWidgetFrame) {
    this.widgetFrame = paramWidgetFrame;
  }
  
  public MotionWidget findViewById(int paramInt) {
    return null;
  }
  
  public float getAlpha() {
    return this.propertySet.alpha;
  }
  
  public int getBottom() {
    return this.widgetFrame.bottom;
  }
  
  public CustomVariable getCustomAttribute(String paramString) {
    return this.widgetFrame.getCustomAttribute(paramString);
  }
  
  public Set<String> getCustomAttributeNames() {
    return this.widgetFrame.getCustomAttributeNames();
  }
  
  public int getHeight() {
    return this.widgetFrame.bottom - this.widgetFrame.top;
  }
  
  public int getId(String paramString) {
    int i = TypedValues.Attributes.getId(paramString);
    return (i != -1) ? i : TypedValues.Motion.getId(paramString);
  }
  
  public int getLeft() {
    return this.widgetFrame.left;
  }
  
  public String getName() {
    return getClass().getSimpleName();
  }
  
  public MotionWidget getParent() {
    return null;
  }
  
  public float getPivotX() {
    return this.widgetFrame.pivotX;
  }
  
  public float getPivotY() {
    return this.widgetFrame.pivotY;
  }
  
  public int getRight() {
    return this.widgetFrame.right;
  }
  
  public float getRotationX() {
    return this.widgetFrame.rotationX;
  }
  
  public float getRotationY() {
    return this.widgetFrame.rotationY;
  }
  
  public float getRotationZ() {
    return this.widgetFrame.rotationZ;
  }
  
  public float getScaleX() {
    return this.widgetFrame.scaleX;
  }
  
  public float getScaleY() {
    return this.widgetFrame.scaleY;
  }
  
  public int getTop() {
    return this.widgetFrame.top;
  }
  
  public float getTranslationX() {
    return this.widgetFrame.translationX;
  }
  
  public float getTranslationY() {
    return this.widgetFrame.translationY;
  }
  
  public float getTranslationZ() {
    return this.widgetFrame.translationZ;
  }
  
  public float getValueAttributes(int paramInt) {
    switch (paramInt) {
      default:
        return Float.NaN;
      case 316:
        return this.mTransitionPathRotate;
      case 315:
        return this.mProgress;
      case 314:
        return this.widgetFrame.pivotY;
      case 313:
        return this.widgetFrame.pivotX;
      case 312:
        return this.widgetFrame.scaleY;
      case 311:
        return this.widgetFrame.scaleX;
      case 310:
        return this.widgetFrame.rotationZ;
      case 309:
        return this.widgetFrame.rotationY;
      case 308:
        return this.widgetFrame.rotationX;
      case 306:
        return this.widgetFrame.translationZ;
      case 305:
        return this.widgetFrame.translationY;
      case 304:
        return this.widgetFrame.translationX;
      case 303:
        break;
    } 
    return this.widgetFrame.alpha;
  }
  
  public int getVisibility() {
    return this.propertySet.visibility;
  }
  
  public WidgetFrame getWidgetFrame() {
    return this.widgetFrame;
  }
  
  public int getWidth() {
    return this.widgetFrame.right - this.widgetFrame.left;
  }
  
  public int getX() {
    return this.widgetFrame.left;
  }
  
  public int getY() {
    return this.widgetFrame.top;
  }
  
  public void layout(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (this.widgetFrame == null)
      this.widgetFrame = new WidgetFrame((ConstraintWidget)null); 
    this.widgetFrame.top = paramInt2;
    this.widgetFrame.left = paramInt1;
    this.widgetFrame.right = paramInt3;
    this.widgetFrame.bottom = paramInt4;
  }
  
  public void setCustomAttribute(String paramString, int paramInt, float paramFloat) {
    this.widgetFrame.setCustomAttribute(paramString, paramInt, paramFloat);
  }
  
  public void setCustomAttribute(String paramString, int paramInt1, int paramInt2) {
    this.widgetFrame.setCustomAttribute(paramString, paramInt1, paramInt2);
  }
  
  public void setCustomAttribute(String paramString1, int paramInt, String paramString2) {
    this.widgetFrame.setCustomAttribute(paramString1, paramInt, paramString2);
  }
  
  public void setCustomAttribute(String paramString, int paramInt, boolean paramBoolean) {
    this.widgetFrame.setCustomAttribute(paramString, paramInt, paramBoolean);
  }
  
  public void setPivotX(float paramFloat) {
    this.widgetFrame.pivotX = paramFloat;
  }
  
  public void setPivotY(float paramFloat) {
    this.widgetFrame.pivotY = paramFloat;
  }
  
  public void setRotationX(float paramFloat) {
    this.widgetFrame.rotationX = paramFloat;
  }
  
  public void setRotationY(float paramFloat) {
    this.widgetFrame.rotationY = paramFloat;
  }
  
  public void setRotationZ(float paramFloat) {
    this.widgetFrame.rotationZ = paramFloat;
  }
  
  public void setScaleX(float paramFloat) {
    this.widgetFrame.scaleX = paramFloat;
  }
  
  public void setScaleY(float paramFloat) {
    this.widgetFrame.scaleY = paramFloat;
  }
  
  public void setTranslationX(float paramFloat) {
    this.widgetFrame.translationX = paramFloat;
  }
  
  public void setTranslationY(float paramFloat) {
    this.widgetFrame.translationY = paramFloat;
  }
  
  public void setTranslationZ(float paramFloat) {
    this.widgetFrame.translationZ = paramFloat;
  }
  
  public boolean setValue(int paramInt, float paramFloat) {
    return setValueAttributes(paramInt, paramFloat) ? true : setValueMotion(paramInt, paramFloat);
  }
  
  public boolean setValue(int paramInt1, int paramInt2) {
    return setValueAttributes(paramInt1, paramInt2);
  }
  
  public boolean setValue(int paramInt, String paramString) {
    return setValueMotion(paramInt, paramString);
  }
  
  public boolean setValue(int paramInt, boolean paramBoolean) {
    return false;
  }
  
  public boolean setValueAttributes(int paramInt, float paramFloat) {
    switch (paramInt) {
      default:
        return false;
      case 316:
        this.mTransitionPathRotate = paramFloat;
        return true;
      case 315:
        this.mProgress = paramFloat;
        return true;
      case 314:
        this.widgetFrame.pivotY = paramFloat;
        return true;
      case 313:
        this.widgetFrame.pivotX = paramFloat;
        return true;
      case 312:
        this.widgetFrame.scaleY = paramFloat;
        return true;
      case 311:
        this.widgetFrame.scaleX = paramFloat;
        return true;
      case 310:
        this.widgetFrame.rotationZ = paramFloat;
        return true;
      case 309:
        this.widgetFrame.rotationY = paramFloat;
        return true;
      case 308:
        this.widgetFrame.rotationX = paramFloat;
        return true;
      case 306:
        this.widgetFrame.translationZ = paramFloat;
        return true;
      case 305:
        this.widgetFrame.translationY = paramFloat;
        return true;
      case 304:
        this.widgetFrame.translationX = paramFloat;
        return true;
      case 303:
        break;
    } 
    this.widgetFrame.alpha = paramFloat;
    return true;
  }
  
  public boolean setValueMotion(int paramInt, float paramFloat) {
    switch (paramInt) {
      default:
        return false;
      case 602:
        this.motion.mQuantizeMotionPhase = paramFloat;
        return true;
      case 601:
        this.motion.mPathRotate = paramFloat;
        return true;
      case 600:
        break;
    } 
    this.motion.mMotionStagger = paramFloat;
    return true;
  }
  
  public boolean setValueMotion(int paramInt1, int paramInt2) {
    switch (paramInt1) {
      default:
        return false;
      case 612:
        this.motion.mQuantizeInterpolatorID = paramInt2;
        return true;
      case 611:
        this.motion.mQuantizeInterpolatorType = paramInt2;
        return true;
      case 610:
        this.motion.mQuantizeMotionSteps = paramInt2;
        return true;
      case 609:
        this.motion.mPolarRelativeTo = paramInt2;
        return true;
      case 608:
        this.motion.mDrawPath = paramInt2;
        return true;
      case 607:
        this.motion.mPathMotionArc = paramInt2;
        return true;
      case 606:
        this.motion.mAnimateCircleAngleTo = paramInt2;
        return true;
      case 605:
        break;
    } 
    this.motion.mAnimateRelativeTo = paramInt2;
    return true;
  }
  
  public boolean setValueMotion(int paramInt, String paramString) {
    switch (paramInt) {
      default:
        return false;
      case 604:
        this.motion.mQuantizeInterpolatorString = paramString;
        return true;
      case 603:
        break;
    } 
    this.motion.mTransitionEasing = paramString;
    return true;
  }
  
  public void setVisibility(int paramInt) {
    this.propertySet.visibility = paramInt;
  }
  
  public String toString() {
    return this.widgetFrame.left + ", " + this.widgetFrame.top + ", " + this.widgetFrame.right + ", " + this.widgetFrame.bottom;
  }
  
  public static class Motion {
    private static final int INTERPOLATOR_REFERENCE_ID = -2;
    
    private static final int INTERPOLATOR_UNDEFINED = -3;
    
    private static final int SPLINE_STRING = -1;
    
    public int mAnimateCircleAngleTo = 0;
    
    public int mAnimateRelativeTo = -1;
    
    public int mDrawPath = 0;
    
    public float mMotionStagger = Float.NaN;
    
    public int mPathMotionArc = -1;
    
    public float mPathRotate = Float.NaN;
    
    public int mPolarRelativeTo = -1;
    
    public int mQuantizeInterpolatorID = -1;
    
    public String mQuantizeInterpolatorString = null;
    
    public int mQuantizeInterpolatorType = -3;
    
    public float mQuantizeMotionPhase = Float.NaN;
    
    public int mQuantizeMotionSteps = -1;
    
    public String mTransitionEasing = null;
  }
  
  public static class PropertySet {
    public float alpha = 1.0F;
    
    public float mProgress = Float.NaN;
    
    public int mVisibilityMode = 0;
    
    public int visibility = 4;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motion\MotionWidget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */