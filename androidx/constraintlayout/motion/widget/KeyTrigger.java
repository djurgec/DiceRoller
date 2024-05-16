package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.motion.utils.ViewSpline;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.R;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

public class KeyTrigger extends Key {
  public static final String CROSS = "CROSS";
  
  public static final int KEY_TYPE = 5;
  
  static final String NAME = "KeyTrigger";
  
  public static final String NEGATIVE_CROSS = "negativeCross";
  
  public static final String POSITIVE_CROSS = "positiveCross";
  
  public static final String POST_LAYOUT = "postLayout";
  
  private static final String TAG = "KeyTrigger";
  
  public static final String TRIGGER_COLLISION_ID = "triggerCollisionId";
  
  public static final String TRIGGER_COLLISION_VIEW = "triggerCollisionView";
  
  public static final String TRIGGER_ID = "triggerID";
  
  public static final String TRIGGER_RECEIVER = "triggerReceiver";
  
  public static final String TRIGGER_SLACK = "triggerSlack";
  
  public static final String VIEW_TRANSITION_ON_CROSS = "viewTransitionOnCross";
  
  public static final String VIEW_TRANSITION_ON_NEGATIVE_CROSS = "viewTransitionOnNegativeCross";
  
  public static final String VIEW_TRANSITION_ON_POSITIVE_CROSS = "viewTransitionOnPositiveCross";
  
  RectF mCollisionRect = new RectF();
  
  private String mCross = null;
  
  private int mCurveFit = -1;
  
  private boolean mFireCrossReset = true;
  
  private float mFireLastPos;
  
  private boolean mFireNegativeReset = true;
  
  private boolean mFirePositiveReset = true;
  
  private float mFireThreshold = Float.NaN;
  
  HashMap<String, Method> mMethodHashMap = new HashMap<>();
  
  private String mNegativeCross = null;
  
  private String mPositiveCross = null;
  
  private boolean mPostLayout = false;
  
  RectF mTargetRect = new RectF();
  
  private int mTriggerCollisionId = UNSET;
  
  private View mTriggerCollisionView = null;
  
  private int mTriggerID = UNSET;
  
  private int mTriggerReceiver = UNSET;
  
  float mTriggerSlack = 0.1F;
  
  int mViewTransitionOnCross = UNSET;
  
  int mViewTransitionOnNegativeCross = UNSET;
  
  int mViewTransitionOnPositiveCross = UNSET;
  
  private void fire(String paramString, View paramView) {
    String str;
    if (paramString == null)
      return; 
    if (paramString.startsWith(".")) {
      fireCustom(paramString, paramView);
      return;
    } 
    Method method1 = null;
    if (this.mMethodHashMap.containsKey(paramString)) {
      Method method = this.mMethodHashMap.get(paramString);
      method1 = method;
      if (method == null)
        return; 
    } 
    Method method2 = method1;
    if (method1 == null)
      try {
        method2 = paramView.getClass().getMethod(paramString, new Class[0]);
        this.mMethodHashMap.put(paramString, method2);
      } catch (NoSuchMethodException noSuchMethodException) {
        this.mMethodHashMap.put(paramString, null);
        String str1 = paramView.getClass().getSimpleName();
        str = Debug.getName(paramView);
        Log.e("KeyTrigger", (new StringBuilder(String.valueOf(paramString).length() + 34 + String.valueOf(str1).length() + String.valueOf(str).length())).append("Could not find method \"").append(paramString).append("\"on class ").append(str1).append(" ").append(str).toString());
        return;
      }  
    try {
      method2.invoke(str, new Object[0]);
    } catch (Exception exception) {
      String str2 = this.mCross;
      String str1 = str.getClass().getSimpleName();
      str = Debug.getName((View)str);
      Log.e("KeyTrigger", (new StringBuilder(String.valueOf(str2).length() + 30 + String.valueOf(str1).length() + String.valueOf(str).length())).append("Exception in call \"").append(str2).append("\"on class ").append(str1).append(" ").append(str).toString());
    } 
  }
  
  private void fireCustom(String paramString, View paramView) {
    boolean bool;
    if (paramString.length() == 1) {
      bool = true;
    } else {
      bool = false;
    } 
    String str = paramString;
    if (!bool)
      str = paramString.substring(1).toLowerCase(Locale.ROOT); 
    for (String str2 : this.mCustomConstraints.keySet()) {
      String str1 = str2.toLowerCase(Locale.ROOT);
      if (bool || str1.matches(str)) {
        ConstraintAttribute constraintAttribute = this.mCustomConstraints.get(str2);
        if (constraintAttribute != null)
          constraintAttribute.applyCustom(paramView); 
      } 
    } 
  }
  
  private void setUpRect(RectF paramRectF, View paramView, boolean paramBoolean) {
    paramRectF.top = paramView.getTop();
    paramRectF.bottom = paramView.getBottom();
    paramRectF.left = paramView.getLeft();
    paramRectF.right = paramView.getRight();
    if (paramBoolean)
      paramView.getMatrix().mapRect(paramRectF); 
  }
  
  public void addValues(HashMap<String, ViewSpline> paramHashMap) {}
  
  public Key clone() {
    return (new KeyTrigger()).copy(this);
  }
  
  public void conditionallyFire(float paramFloat, View paramView) {
    View view;
    boolean bool7 = false;
    boolean bool8 = false;
    boolean bool6 = false;
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    boolean bool5 = false;
    boolean bool9 = false;
    boolean bool11 = false;
    boolean bool10 = false;
    boolean bool12 = false;
    if (this.mTriggerCollisionId != UNSET) {
      if (this.mTriggerCollisionView == null)
        this.mTriggerCollisionView = ((ViewGroup)paramView.getParent()).findViewById(this.mTriggerCollisionId); 
      setUpRect(this.mCollisionRect, this.mTriggerCollisionView, this.mPostLayout);
      setUpRect(this.mTargetRect, paramView, this.mPostLayout);
      if (this.mCollisionRect.intersect(this.mTargetRect)) {
        if (this.mFireCrossReset) {
          bool1 = true;
          this.mFireCrossReset = false;
        } 
        bool9 = bool12;
        if (this.mFirePositiveReset) {
          bool9 = true;
          this.mFirePositiveReset = false;
        } 
        this.mFireNegativeReset = true;
      } else {
        bool1 = bool7;
        if (!this.mFireCrossReset) {
          bool1 = true;
          this.mFireCrossReset = true;
        } 
        bool2 = bool5;
        if (this.mFireNegativeReset) {
          bool2 = true;
          this.mFireNegativeReset = false;
        } 
        this.mFirePositiveReset = true;
      } 
      bool3 = bool1;
      bool4 = bool2;
    } else {
      if (this.mFireCrossReset) {
        float f = this.mFireThreshold;
        bool1 = bool8;
        if ((paramFloat - f) * (this.mFireLastPos - f) < 0.0F) {
          bool1 = true;
          this.mFireCrossReset = false;
        } 
      } else {
        bool1 = bool6;
        if (Math.abs(paramFloat - this.mFireThreshold) > this.mTriggerSlack) {
          this.mFireCrossReset = true;
          bool1 = bool6;
        } 
      } 
      if (this.mFireNegativeReset) {
        float f2 = this.mFireThreshold;
        float f1 = paramFloat - f2;
        bool2 = bool3;
        if (f1 * (this.mFireLastPos - f2) < 0.0F) {
          bool2 = bool3;
          if (f1 < 0.0F) {
            bool2 = true;
            this.mFireNegativeReset = false;
          } 
        } 
      } else {
        bool2 = bool4;
        if (Math.abs(paramFloat - this.mFireThreshold) > this.mTriggerSlack) {
          this.mFireNegativeReset = true;
          bool2 = bool4;
        } 
      } 
      if (this.mFirePositiveReset) {
        float f1 = this.mFireThreshold;
        float f2 = paramFloat - f1;
        bool9 = bool11;
        if (f2 * (this.mFireLastPos - f1) < 0.0F) {
          bool9 = bool11;
          if (f2 > 0.0F) {
            bool9 = true;
            this.mFirePositiveReset = false;
          } 
        } 
        bool3 = bool1;
        bool4 = bool2;
      } else {
        bool3 = bool1;
        bool4 = bool2;
        bool9 = bool10;
        if (Math.abs(paramFloat - this.mFireThreshold) > this.mTriggerSlack) {
          this.mFirePositiveReset = true;
          bool9 = bool10;
          bool4 = bool2;
          bool3 = bool1;
        } 
      } 
    } 
    this.mFireLastPos = paramFloat;
    if (bool4 || bool3 || bool9)
      ((MotionLayout)paramView.getParent()).fireTrigger(this.mTriggerID, bool9, paramFloat); 
    if (this.mTriggerReceiver == UNSET) {
      view = paramView;
    } else {
      view = ((MotionLayout)paramView.getParent()).findViewById(this.mTriggerReceiver);
    } 
    if (bool4) {
      String str = this.mNegativeCross;
      if (str != null)
        fire(str, view); 
      if (this.mViewTransitionOnNegativeCross != UNSET)
        ((MotionLayout)paramView.getParent()).viewTransition(this.mViewTransitionOnNegativeCross, new View[] { view }); 
    } 
    if (bool9) {
      String str = this.mPositiveCross;
      if (str != null)
        fire(str, view); 
      if (this.mViewTransitionOnPositiveCross != UNSET)
        ((MotionLayout)paramView.getParent()).viewTransition(this.mViewTransitionOnPositiveCross, new View[] { view }); 
    } 
    if (bool3) {
      String str = this.mCross;
      if (str != null)
        fire(str, view); 
      if (this.mViewTransitionOnCross != UNSET)
        ((MotionLayout)paramView.getParent()).viewTransition(this.mViewTransitionOnCross, new View[] { view }); 
    } 
  }
  
  public Key copy(Key paramKey) {
    super.copy(paramKey);
    paramKey = paramKey;
    this.mCurveFit = ((KeyTrigger)paramKey).mCurveFit;
    this.mCross = ((KeyTrigger)paramKey).mCross;
    this.mTriggerReceiver = ((KeyTrigger)paramKey).mTriggerReceiver;
    this.mNegativeCross = ((KeyTrigger)paramKey).mNegativeCross;
    this.mPositiveCross = ((KeyTrigger)paramKey).mPositiveCross;
    this.mTriggerID = ((KeyTrigger)paramKey).mTriggerID;
    this.mTriggerCollisionId = ((KeyTrigger)paramKey).mTriggerCollisionId;
    this.mTriggerCollisionView = ((KeyTrigger)paramKey).mTriggerCollisionView;
    this.mTriggerSlack = ((KeyTrigger)paramKey).mTriggerSlack;
    this.mFireCrossReset = ((KeyTrigger)paramKey).mFireCrossReset;
    this.mFireNegativeReset = ((KeyTrigger)paramKey).mFireNegativeReset;
    this.mFirePositiveReset = ((KeyTrigger)paramKey).mFirePositiveReset;
    this.mFireThreshold = ((KeyTrigger)paramKey).mFireThreshold;
    this.mFireLastPos = ((KeyTrigger)paramKey).mFireLastPos;
    this.mPostLayout = ((KeyTrigger)paramKey).mPostLayout;
    this.mCollisionRect = ((KeyTrigger)paramKey).mCollisionRect;
    this.mTargetRect = ((KeyTrigger)paramKey).mTargetRect;
    this.mMethodHashMap = ((KeyTrigger)paramKey).mMethodHashMap;
    return this;
  }
  
  public void getAttributeNames(HashSet<String> paramHashSet) {}
  
  int getCurveFit() {
    return this.mCurveFit;
  }
  
  public void load(Context paramContext, AttributeSet paramAttributeSet) {
    Loader.read(this, paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.KeyTrigger), paramContext);
  }
  
  public void setValue(String paramString, Object paramObject) {
    byte b;
    switch (paramString.hashCode()) {
      default:
        b = -1;
        break;
      case 1535404999:
        if (paramString.equals("triggerReceiver")) {
          b = 1;
          break;
        } 
      case 1401391082:
        if (paramString.equals("postLayout")) {
          b = 8;
          break;
        } 
      case 1301930599:
        if (paramString.equals("viewTransitionOnCross")) {
          b = 11;
          break;
        } 
      case 364489912:
        if (paramString.equals("triggerSlack")) {
          b = 7;
          break;
        } 
      case 64397344:
        if (paramString.equals("CROSS")) {
          b = 0;
          break;
        } 
      case -9754574:
        if (paramString.equals("viewTransitionOnNegativeCross")) {
          b = 9;
          break;
        } 
      case -76025313:
        if (paramString.equals("triggerCollisionView")) {
          b = 6;
          break;
        } 
      case -638126837:
        if (paramString.equals("negativeCross")) {
          b = 2;
          break;
        } 
      case -648752941:
        if (paramString.equals("triggerID")) {
          b = 4;
          break;
        } 
      case -786670827:
        if (paramString.equals("triggerCollisionId")) {
          b = 5;
          break;
        } 
      case -966421266:
        if (paramString.equals("viewTransitionOnPositiveCross")) {
          b = 10;
          break;
        } 
      case -1594793529:
        if (paramString.equals("positiveCross")) {
          b = 3;
          break;
        } 
    } 
    switch (b) {
      default:
        return;
      case 11:
        this.mViewTransitionOnCross = toInt(paramObject);
      case 10:
        this.mViewTransitionOnPositiveCross = toInt(paramObject);
      case 9:
        this.mViewTransitionOnNegativeCross = toInt(paramObject);
      case 8:
        this.mPostLayout = toBoolean(paramObject);
      case 7:
        this.mTriggerSlack = toFloat(paramObject);
      case 6:
        this.mTriggerCollisionView = (View)paramObject;
      case 5:
        this.mTriggerCollisionId = toInt(paramObject);
      case 4:
        this.mTriggerID = toInt(paramObject);
      case 3:
        this.mPositiveCross = paramObject.toString();
      case 2:
        this.mNegativeCross = paramObject.toString();
      case 1:
        this.mTriggerReceiver = toInt(paramObject);
      case 0:
        break;
    } 
    this.mCross = paramObject.toString();
  }
  
  private static class Loader {
    private static final int COLLISION = 9;
    
    private static final int CROSS = 4;
    
    private static final int FRAME_POS = 8;
    
    private static final int NEGATIVE_CROSS = 1;
    
    private static final int POSITIVE_CROSS = 2;
    
    private static final int POST_LAYOUT = 10;
    
    private static final int TARGET_ID = 7;
    
    private static final int TRIGGER_ID = 6;
    
    private static final int TRIGGER_RECEIVER = 11;
    
    private static final int TRIGGER_SLACK = 5;
    
    private static final int VT_CROSS = 12;
    
    private static final int VT_NEGATIVE_CROSS = 13;
    
    private static final int VT_POSITIVE_CROSS = 14;
    
    private static SparseIntArray mAttrMap;
    
    static {
      SparseIntArray sparseIntArray = new SparseIntArray();
      mAttrMap = sparseIntArray;
      sparseIntArray.append(R.styleable.KeyTrigger_framePosition, 8);
      mAttrMap.append(R.styleable.KeyTrigger_onCross, 4);
      mAttrMap.append(R.styleable.KeyTrigger_onNegativeCross, 1);
      mAttrMap.append(R.styleable.KeyTrigger_onPositiveCross, 2);
      mAttrMap.append(R.styleable.KeyTrigger_motionTarget, 7);
      mAttrMap.append(R.styleable.KeyTrigger_triggerId, 6);
      mAttrMap.append(R.styleable.KeyTrigger_triggerSlack, 5);
      mAttrMap.append(R.styleable.KeyTrigger_motion_triggerOnCollision, 9);
      mAttrMap.append(R.styleable.KeyTrigger_motion_postLayoutCollision, 10);
      mAttrMap.append(R.styleable.KeyTrigger_triggerReceiver, 11);
      mAttrMap.append(R.styleable.KeyTrigger_viewTransitionOnCross, 12);
      mAttrMap.append(R.styleable.KeyTrigger_viewTransitionOnNegativeCross, 13);
      mAttrMap.append(R.styleable.KeyTrigger_viewTransitionOnPositiveCross, 14);
    }
    
    public static void read(KeyTrigger param1KeyTrigger, TypedArray param1TypedArray, Context param1Context) {
      int i = param1TypedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        String str;
        int j = param1TypedArray.getIndex(b);
        switch (mAttrMap.get(j)) {
          default:
            str = Integer.toHexString(j);
            j = mAttrMap.get(j);
            Log.e("KeyTrigger", (new StringBuilder(String.valueOf(str).length() + 33)).append("unused attribute 0x").append(str).append("   ").append(j).toString());
            break;
          case 14:
            param1KeyTrigger.mViewTransitionOnPositiveCross = param1TypedArray.getResourceId(j, param1KeyTrigger.mViewTransitionOnPositiveCross);
            break;
          case 13:
            param1KeyTrigger.mViewTransitionOnNegativeCross = param1TypedArray.getResourceId(j, param1KeyTrigger.mViewTransitionOnNegativeCross);
            break;
          case 12:
            param1KeyTrigger.mViewTransitionOnCross = param1TypedArray.getResourceId(j, param1KeyTrigger.mViewTransitionOnCross);
            break;
          case 11:
            KeyTrigger.access$702(param1KeyTrigger, param1TypedArray.getResourceId(j, param1KeyTrigger.mTriggerReceiver));
            break;
          case 10:
            KeyTrigger.access$602(param1KeyTrigger, param1TypedArray.getBoolean(j, param1KeyTrigger.mPostLayout));
            break;
          case 9:
            KeyTrigger.access$502(param1KeyTrigger, param1TypedArray.getResourceId(j, param1KeyTrigger.mTriggerCollisionId));
            break;
          case 8:
            param1KeyTrigger.mFramePosition = param1TypedArray.getInteger(j, param1KeyTrigger.mFramePosition);
            KeyTrigger.access$002(param1KeyTrigger, (param1KeyTrigger.mFramePosition + 0.5F) / 100.0F);
            break;
          case 7:
            if (MotionLayout.IS_IN_EDIT_MODE) {
              param1KeyTrigger.mTargetId = param1TypedArray.getResourceId(j, param1KeyTrigger.mTargetId);
              if (param1KeyTrigger.mTargetId == -1)
                param1KeyTrigger.mTargetString = param1TypedArray.getString(j); 
              break;
            } 
            if ((param1TypedArray.peekValue(j)).type == 3) {
              param1KeyTrigger.mTargetString = param1TypedArray.getString(j);
              break;
            } 
            param1KeyTrigger.mTargetId = param1TypedArray.getResourceId(j, param1KeyTrigger.mTargetId);
            break;
          case 6:
            KeyTrigger.access$402(param1KeyTrigger, param1TypedArray.getResourceId(j, param1KeyTrigger.mTriggerID));
            break;
          case 5:
            param1KeyTrigger.mTriggerSlack = param1TypedArray.getFloat(j, param1KeyTrigger.mTriggerSlack);
            break;
          case 4:
            KeyTrigger.access$302(param1KeyTrigger, param1TypedArray.getString(j));
            break;
          case 2:
            KeyTrigger.access$202(param1KeyTrigger, param1TypedArray.getString(j));
            break;
          case 1:
            KeyTrigger.access$102(param1KeyTrigger, param1TypedArray.getString(j));
            break;
        } 
      } 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\motion\widget\KeyTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */