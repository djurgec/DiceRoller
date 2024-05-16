package androidx.constraintlayout.core.motion.key;

import androidx.constraintlayout.core.motion.CustomVariable;
import androidx.constraintlayout.core.motion.MotionWidget;
import androidx.constraintlayout.core.motion.utils.FloatRect;
import androidx.constraintlayout.core.motion.utils.SplineSet;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

public class MotionKeyTrigger extends MotionKey {
  public static final String CROSS = "CROSS";
  
  public static final int KEY_TYPE = 5;
  
  public static final String NEGATIVE_CROSS = "negativeCross";
  
  public static final String POSITIVE_CROSS = "positiveCross";
  
  public static final String POST_LAYOUT = "postLayout";
  
  private static final String TAG = "KeyTrigger";
  
  public static final String TRIGGER_COLLISION_ID = "triggerCollisionId";
  
  public static final String TRIGGER_COLLISION_VIEW = "triggerCollisionView";
  
  public static final String TRIGGER_ID = "triggerID";
  
  public static final String TRIGGER_RECEIVER = "triggerReceiver";
  
  public static final String TRIGGER_SLACK = "triggerSlack";
  
  public static final int TYPE_CROSS = 312;
  
  public static final int TYPE_NEGATIVE_CROSS = 310;
  
  public static final int TYPE_POSITIVE_CROSS = 309;
  
  public static final int TYPE_POST_LAYOUT = 304;
  
  public static final int TYPE_TRIGGER_COLLISION_ID = 307;
  
  public static final int TYPE_TRIGGER_COLLISION_VIEW = 306;
  
  public static final int TYPE_TRIGGER_ID = 308;
  
  public static final int TYPE_TRIGGER_RECEIVER = 311;
  
  public static final int TYPE_TRIGGER_SLACK = 305;
  
  public static final int TYPE_VIEW_TRANSITION_ON_CROSS = 301;
  
  public static final int TYPE_VIEW_TRANSITION_ON_NEGATIVE_CROSS = 303;
  
  public static final int TYPE_VIEW_TRANSITION_ON_POSITIVE_CROSS = 302;
  
  public static final String VIEW_TRANSITION_ON_CROSS = "viewTransitionOnCross";
  
  public static final String VIEW_TRANSITION_ON_NEGATIVE_CROSS = "viewTransitionOnNegativeCross";
  
  public static final String VIEW_TRANSITION_ON_POSITIVE_CROSS = "viewTransitionOnPositiveCross";
  
  FloatRect mCollisionRect = new FloatRect();
  
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
  
  FloatRect mTargetRect = new FloatRect();
  
  private int mTriggerCollisionId = UNSET;
  
  private int mTriggerID = UNSET;
  
  private int mTriggerReceiver = UNSET;
  
  float mTriggerSlack = 0.1F;
  
  int mViewTransitionOnCross = UNSET;
  
  int mViewTransitionOnNegativeCross = UNSET;
  
  int mViewTransitionOnPositiveCross = UNSET;
  
  private void fireCustom(String paramString, MotionWidget paramMotionWidget) {
    boolean bool;
    if (paramString.length() == 1) {
      bool = true;
    } else {
      bool = false;
    } 
    String str = paramString;
    if (!bool)
      str = paramString.substring(1).toLowerCase(Locale.ROOT); 
    for (String str2 : this.mCustom.keySet()) {
      String str1 = str2.toLowerCase(Locale.ROOT);
      if (bool || str1.matches(str)) {
        CustomVariable customVariable = this.mCustom.get(str2);
        if (customVariable != null)
          customVariable.applyToWidget(paramMotionWidget); 
      } 
    } 
  }
  
  public void addValues(HashMap<String, SplineSet> paramHashMap) {}
  
  public MotionKey clone() {
    return (new MotionKeyTrigger()).copy(this);
  }
  
  public void conditionallyFire(float paramFloat, MotionWidget paramMotionWidget) {}
  
  public MotionKeyTrigger copy(MotionKey paramMotionKey) {
    super.copy(paramMotionKey);
    paramMotionKey = paramMotionKey;
    this.mCurveFit = ((MotionKeyTrigger)paramMotionKey).mCurveFit;
    this.mCross = ((MotionKeyTrigger)paramMotionKey).mCross;
    this.mTriggerReceiver = ((MotionKeyTrigger)paramMotionKey).mTriggerReceiver;
    this.mNegativeCross = ((MotionKeyTrigger)paramMotionKey).mNegativeCross;
    this.mPositiveCross = ((MotionKeyTrigger)paramMotionKey).mPositiveCross;
    this.mTriggerID = ((MotionKeyTrigger)paramMotionKey).mTriggerID;
    this.mTriggerCollisionId = ((MotionKeyTrigger)paramMotionKey).mTriggerCollisionId;
    this.mTriggerSlack = ((MotionKeyTrigger)paramMotionKey).mTriggerSlack;
    this.mFireCrossReset = ((MotionKeyTrigger)paramMotionKey).mFireCrossReset;
    this.mFireNegativeReset = ((MotionKeyTrigger)paramMotionKey).mFireNegativeReset;
    this.mFirePositiveReset = ((MotionKeyTrigger)paramMotionKey).mFirePositiveReset;
    this.mFireThreshold = ((MotionKeyTrigger)paramMotionKey).mFireThreshold;
    this.mFireLastPos = ((MotionKeyTrigger)paramMotionKey).mFireLastPos;
    this.mPostLayout = ((MotionKeyTrigger)paramMotionKey).mPostLayout;
    this.mCollisionRect = ((MotionKeyTrigger)paramMotionKey).mCollisionRect;
    this.mTargetRect = ((MotionKeyTrigger)paramMotionKey).mTargetRect;
    this.mMethodHashMap = ((MotionKeyTrigger)paramMotionKey).mMethodHashMap;
    return this;
  }
  
  public void getAttributeNames(HashSet<String> paramHashSet) {}
  
  public int getId(String paramString) {
    byte b;
    switch (paramString.hashCode()) {
      default:
        b = -1;
        break;
      case 1535404999:
        if (paramString.equals("triggerReceiver")) {
          b = 10;
          break;
        } 
      case 1401391082:
        if (paramString.equals("postLayout")) {
          b = 3;
          break;
        } 
      case 1301930599:
        if (paramString.equals("viewTransitionOnCross")) {
          b = 0;
          break;
        } 
      case 364489912:
        if (paramString.equals("triggerSlack")) {
          b = 4;
          break;
        } 
      case -9754574:
        if (paramString.equals("viewTransitionOnNegativeCross")) {
          b = 2;
          break;
        } 
      case -76025313:
        if (paramString.equals("triggerCollisionView")) {
          b = 5;
          break;
        } 
      case -638126837:
        if (paramString.equals("negativeCross")) {
          b = 9;
          break;
        } 
      case -648752941:
        if (paramString.equals("triggerID")) {
          b = 7;
          break;
        } 
      case -786670827:
        if (paramString.equals("triggerCollisionId")) {
          b = 6;
          break;
        } 
      case -966421266:
        if (paramString.equals("viewTransitionOnPositiveCross")) {
          b = 1;
          break;
        } 
      case -1594793529:
        if (paramString.equals("positiveCross")) {
          b = 8;
          break;
        } 
    } 
    switch (b) {
      default:
        return -1;
      case 10:
        return 311;
      case 9:
        return 310;
      case 8:
        return 309;
      case 7:
        return 308;
      case 6:
        return 307;
      case 5:
        return 306;
      case 4:
        return 305;
      case 3:
        return 304;
      case 2:
        return 303;
      case 1:
        return 302;
      case 0:
        break;
    } 
    return 301;
  }
  
  public boolean setValue(int paramInt, float paramFloat) {
    switch (paramInt) {
      default:
        return super.setValue(paramInt, paramFloat);
      case 305:
        break;
    } 
    this.mTriggerSlack = paramFloat;
    return true;
  }
  
  public boolean setValue(int paramInt1, int paramInt2) {
    switch (paramInt1) {
      default:
        return super.setValue(paramInt1, paramInt2);
      case 311:
        this.mTriggerReceiver = paramInt2;
        return true;
      case 308:
        this.mTriggerID = toInt(Integer.valueOf(paramInt2));
        return true;
      case 307:
        this.mTriggerCollisionId = paramInt2;
        return true;
      case 303:
        this.mViewTransitionOnNegativeCross = paramInt2;
        return true;
      case 302:
        this.mViewTransitionOnPositiveCross = paramInt2;
        return true;
      case 301:
        break;
    } 
    this.mViewTransitionOnCross = paramInt2;
    return true;
  }
  
  public boolean setValue(int paramInt, String paramString) {
    switch (paramInt) {
      default:
        return super.setValue(paramInt, paramString);
      case 312:
        this.mCross = paramString;
        return true;
      case 310:
        this.mNegativeCross = paramString;
        return true;
      case 309:
        break;
    } 
    this.mPositiveCross = paramString;
    return true;
  }
  
  public boolean setValue(int paramInt, boolean paramBoolean) {
    switch (paramInt) {
      default:
        return super.setValue(paramInt, paramBoolean);
      case 304:
        break;
    } 
    this.mPostLayout = paramBoolean;
    return true;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motion\key\MotionKeyTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */