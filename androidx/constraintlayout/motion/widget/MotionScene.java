package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import androidx.constraintlayout.core.motion.utils.Easing;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.R;
import androidx.constraintlayout.widget.StateSet;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class MotionScene {
  static final int ANTICIPATE = 6;
  
  static final int BOUNCE = 4;
  
  private static final String CONSTRAINTSET_TAG = "ConstraintSet";
  
  private static final boolean DEBUG = false;
  
  static final int EASE_IN = 1;
  
  static final int EASE_IN_OUT = 0;
  
  static final int EASE_OUT = 2;
  
  private static final String INCLUDE_TAG = "include";
  
  private static final String INCLUDE_TAG_UC = "Include";
  
  private static final int INTERPOLATOR_REFERENCE_ID = -2;
  
  private static final String KEYFRAMESET_TAG = "KeyFrameSet";
  
  public static final int LAYOUT_CALL_MEASURE = 2;
  
  public static final int LAYOUT_HONOR_REQUEST = 1;
  
  public static final int LAYOUT_IGNORE_REQUEST = 0;
  
  static final int LINEAR = 3;
  
  private static final int MIN_DURATION = 8;
  
  private static final String MOTIONSCENE_TAG = "MotionScene";
  
  private static final String ONCLICK_TAG = "OnClick";
  
  private static final String ONSWIPE_TAG = "OnSwipe";
  
  static final int OVERSHOOT = 5;
  
  private static final int SPLINE_STRING = -1;
  
  private static final String STATESET_TAG = "StateSet";
  
  private static final String TAG = "MotionScene";
  
  static final int TRANSITION_BACKWARD = 0;
  
  static final int TRANSITION_FORWARD = 1;
  
  private static final String TRANSITION_TAG = "Transition";
  
  public static final int UNSET = -1;
  
  private static final String VIEW_TRANSITION = "ViewTransition";
  
  private boolean DEBUG_DESKTOP = false;
  
  private ArrayList<Transition> mAbstractTransitionList = new ArrayList<>();
  
  private HashMap<String, Integer> mConstraintSetIdMap = new HashMap<>();
  
  private SparseArray<ConstraintSet> mConstraintSetMap = new SparseArray();
  
  Transition mCurrentTransition = null;
  
  private int mDefaultDuration = 400;
  
  private Transition mDefaultTransition = null;
  
  private SparseIntArray mDeriveMap = new SparseIntArray();
  
  private boolean mDisableAutoTransition = false;
  
  private boolean mIgnoreTouch = false;
  
  private MotionEvent mLastTouchDown;
  
  float mLastTouchX;
  
  float mLastTouchY;
  
  private int mLayoutDuringTransition = 0;
  
  private final MotionLayout mMotionLayout;
  
  private boolean mMotionOutsideRegion = false;
  
  private boolean mRtl;
  
  StateSet mStateSet = null;
  
  private ArrayList<Transition> mTransitionList = new ArrayList<>();
  
  private MotionLayout.MotionTracker mVelocityTracker;
  
  final ViewTransitionController mViewTransitionController;
  
  MotionScene(Context paramContext, MotionLayout paramMotionLayout, int paramInt) {
    this.mMotionLayout = paramMotionLayout;
    this.mViewTransitionController = new ViewTransitionController(paramMotionLayout);
    load(paramContext, paramInt);
    this.mConstraintSetMap.put(R.id.motion_base, new ConstraintSet());
    this.mConstraintSetIdMap.put("motion_base", Integer.valueOf(R.id.motion_base));
  }
  
  public MotionScene(MotionLayout paramMotionLayout) {
    this.mMotionLayout = paramMotionLayout;
    this.mViewTransitionController = new ViewTransitionController(paramMotionLayout);
  }
  
  private int getId(Context paramContext, String paramString) {
    int i = -1;
    if (paramString.contains("/")) {
      String str = paramString.substring(paramString.indexOf('/') + 1);
      int k = paramContext.getResources().getIdentifier(str, "id", paramContext.getPackageName());
      i = k;
      if (this.DEBUG_DESKTOP) {
        System.out.println((new StringBuilder(27)).append("id getMap res = ").append(k).toString());
        i = k;
      } 
    } 
    int j = i;
    if (i == -1)
      if (paramString != null && paramString.length() > 1) {
        j = Integer.parseInt(paramString.substring(1));
      } else {
        Log.e("MotionScene", "error in parsing id");
        j = i;
      }  
    return j;
  }
  
  private int getIndex(Transition paramTransition) {
    int i = paramTransition.mId;
    if (i != -1) {
      for (byte b = 0; b < this.mTransitionList.size(); b++) {
        if ((this.mTransitionList.get(b)).mId == i)
          return b; 
      } 
      return -1;
    } 
    throw new IllegalArgumentException("The transition must have an id");
  }
  
  static String getLine(Context paramContext, int paramInt, XmlPullParser paramXmlPullParser) {
    String str1 = Debug.getName(paramContext, paramInt);
    paramInt = paramXmlPullParser.getLineNumber();
    String str2 = paramXmlPullParser.getName();
    return (new StringBuilder(String.valueOf(str1).length() + 22 + String.valueOf(str2).length())).append(".(").append(str1).append(".xml:").append(paramInt).append(") \"").append(str2).append("\"").toString();
  }
  
  private int getRealID(int paramInt) {
    StateSet stateSet = this.mStateSet;
    if (stateSet != null) {
      int i = stateSet.stateGetConstraintID(paramInt, -1, -1);
      if (i != -1)
        return i; 
    } 
    return paramInt;
  }
  
  private boolean hasCycleDependency(int paramInt) {
    int j = this.mDeriveMap.get(paramInt);
    for (int i = this.mDeriveMap.size(); j > 0; i--) {
      if (j == paramInt)
        return true; 
      if (i < 0)
        return true; 
      j = this.mDeriveMap.get(j);
    } 
    return false;
  }
  
  private boolean isProcessingTouch() {
    boolean bool;
    if (this.mVelocityTracker != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void load(Context paramContext, int paramInt) {
    XmlResourceParser xmlResourceParser = paramContext.getResources().getXml(paramInt);
    ViewTransition viewTransition = null;
    try {
      int i = xmlResourceParser.getEventType();
      while (true) {
        int j = 1;
        if (i != 1) {
          Transition transition1;
          ViewTransition viewTransition2;
          StateSet stateSet;
          ViewTransition viewTransition1;
          Transition transition2;
          String str;
          KeyFrames keyFrames;
          ArrayList<Transition> arrayList;
          switch (i) {
            case 2:
              str = xmlResourceParser.getName();
              if (this.DEBUG_DESKTOP) {
                PrintStream printStream = System.out;
                String str1 = String.valueOf(str);
                if (str1.length() != 0) {
                  str1 = "parsing = ".concat(str1);
                } else {
                  str1 = new String("parsing = ");
                } 
                printStream.println(str1);
              } 
              i = str.hashCode();
              switch (i) {
                default:
                  i = -1;
                  break;
                case 1942574248:
                  if (str.equals("include")) {
                    i = 6;
                    break;
                  } 
                case 1382829617:
                  if (str.equals("StateSet")) {
                    i = 4;
                    break;
                  } 
                case 793277014:
                  if (str.equals("MotionScene")) {
                    i = 0;
                    break;
                  } 
                case 327855227:
                  if (str.equals("OnSwipe")) {
                    i = 2;
                    break;
                  } 
                case 312750793:
                  if (str.equals("OnClick")) {
                    i = 3;
                    break;
                  } 
                case 269306229:
                  if (str.equals("Transition")) {
                    i = j;
                    break;
                  } 
                case 61998586:
                  if (str.equals("ViewTransition")) {
                    i = 9;
                    break;
                  } 
                case -687739768:
                  if (str.equals("Include")) {
                    i = 7;
                    break;
                  } 
                case -1239391468:
                  if (str.equals("KeyFrameSet")) {
                    i = 8;
                    break;
                  } 
                case -1349929691:
                  if (str.equals("ConstraintSet")) {
                    i = 5;
                    break;
                  } 
              } 
              switch (i) {
                default:
                  viewTransition2 = viewTransition;
                  break;
                case 9:
                  viewTransition2 = new ViewTransition();
                  this(paramContext, (XmlPullParser)xmlResourceParser);
                  this.mViewTransitionController.add(viewTransition2);
                  viewTransition2 = viewTransition;
                  break;
                case 8:
                  keyFrames = new KeyFrames();
                  this(paramContext, (XmlPullParser)xmlResourceParser);
                  viewTransition2 = viewTransition;
                  if (viewTransition != null) {
                    ((Transition)viewTransition).mKeyFramesList.add(keyFrames);
                    viewTransition2 = viewTransition;
                  } 
                  break;
                case 6:
                case 7:
                  parseInclude(paramContext, (XmlPullParser)xmlResourceParser);
                  viewTransition2 = viewTransition;
                  break;
                case 5:
                  parseConstraintSet(paramContext, (XmlPullParser)xmlResourceParser);
                  viewTransition2 = viewTransition;
                  break;
                case 4:
                  stateSet = new StateSet();
                  this(paramContext, (XmlPullParser)xmlResourceParser);
                  this.mStateSet = stateSet;
                  viewTransition1 = viewTransition;
                  break;
                case 3:
                  viewTransition1 = viewTransition;
                  if (viewTransition != null) {
                    viewTransition.addOnClick(paramContext, (XmlPullParser)xmlResourceParser);
                    viewTransition1 = viewTransition;
                  } 
                  break;
                case 2:
                  if (viewTransition == null) {
                    String str1 = paramContext.getResources().getResourceEntryName(paramInt);
                    i = xmlResourceParser.getLineNumber();
                    j = String.valueOf(str1).length();
                    StringBuilder stringBuilder = new StringBuilder();
                    this(j + 27);
                    Log.v("MotionScene", stringBuilder.append(" OnSwipe (").append(str1).append(".xml:").append(i).append(")").toString());
                  } 
                  viewTransition1 = viewTransition;
                  if (viewTransition != null) {
                    TouchResponse touchResponse = new TouchResponse();
                    this(paramContext, this.mMotionLayout, (XmlPullParser)xmlResourceParser);
                    Transition.access$202((Transition)viewTransition, touchResponse);
                    ViewTransition viewTransition3 = viewTransition;
                  } 
                  break;
                case 1:
                  arrayList = this.mTransitionList;
                  transition2 = new Transition();
                  this(this, paramContext, (XmlPullParser)xmlResourceParser);
                  transition1 = transition2;
                  arrayList.add(transition2);
                  if (this.mCurrentTransition == null && !transition1.mIsAbstract) {
                    this.mCurrentTransition = transition1;
                    if (transition1.mTouchResponse != null)
                      this.mCurrentTransition.mTouchResponse.setRTL(this.mRtl); 
                  } 
                  transition2 = transition1;
                  if (transition1.mIsAbstract) {
                    if (transition1.mConstraintSetEnd == -1) {
                      this.mDefaultTransition = transition1;
                    } else {
                      this.mAbstractTransitionList.add(transition1);
                    } 
                    this.mTransitionList.remove(transition1);
                    transition2 = transition1;
                  } 
                  break;
                case 0:
                  parseMotionSceneTags(paramContext, (XmlPullParser)xmlResourceParser);
                  transition2 = transition1;
                  break;
              } 
              transition1 = transition2;
              break;
            case 0:
              xmlResourceParser.getName();
              break;
          } 
          i = xmlResourceParser.next();
          continue;
        } 
        break;
      } 
    } catch (XmlPullParserException xmlPullParserException) {
      xmlPullParserException.printStackTrace();
    } catch (IOException iOException) {
      iOException.printStackTrace();
    } 
  }
  
  private int parseConstraintSet(Context paramContext, XmlPullParser paramXmlPullParser) {
    ConstraintSet constraintSet = new ConstraintSet();
    byte b = 0;
    constraintSet.setForceId(false);
    int k = paramXmlPullParser.getAttributeCount();
    byte b1 = 0;
    int i = -1;
    int j = -1;
    while (b1 < k) {
      byte b2;
      String str2 = paramXmlPullParser.getAttributeName(b1);
      String str1 = paramXmlPullParser.getAttributeValue(b1);
      if (this.DEBUG_DESKTOP) {
        PrintStream printStream = System.out;
        String str = String.valueOf(str1);
        if (str.length() != 0) {
          str = "id string = ".concat(str);
        } else {
          str = new String("id string = ");
        } 
        printStream.println(str);
      } 
      switch (str2.hashCode()) {
        default:
          b2 = -1;
          break;
        case 3355:
          if (str2.equals("id")) {
            b2 = b;
            break;
          } 
        case -1153153640:
          if (str2.equals("constraintRotate")) {
            b2 = 2;
            break;
          } 
        case -1496482599:
          if (str2.equals("deriveConstraintsFrom")) {
            b2 = 1;
            break;
          } 
      } 
      switch (b2) {
        default:
          b2 = b;
          break;
        case 2:
          try {
            constraintSet.mRotate = Integer.parseInt(str1);
            b2 = b;
          } catch (NumberFormatException numberFormatException) {
            switch (str1.hashCode()) {
              default:
                b2 = -1;
                break;
              case 1954540437:
                if (str1.equals("x_right")) {
                  b2 = 3;
                  break;
                } 
              case 108511772:
                if (str1.equals("right")) {
                  b2 = 1;
                  break;
                } 
              case 3387192:
                if (str1.equals("none")) {
                  b2 = 0;
                  break;
                } 
              case 3317767:
                if (str1.equals("left")) {
                  b2 = 2;
                  break;
                } 
              case -768416914:
                if (str1.equals("x_left")) {
                  b2 = 4;
                  break;
                } 
            } 
            switch (b2) {
              default:
                b2 = 0;
                break;
              case 4:
                constraintSet.mRotate = 4;
                b2 = 0;
                break;
              case 3:
                constraintSet.mRotate = 3;
                b2 = 0;
                break;
              case 2:
                constraintSet.mRotate = 2;
                b2 = 0;
                break;
              case 1:
                constraintSet.mRotate = 1;
                b2 = 0;
                break;
              case 0:
                break;
            } 
            b2 = 0;
            constraintSet.mRotate = 0;
          } 
          break;
        case 1:
          i = getId(paramContext, str1);
          b2 = b;
          break;
        case 0:
          j = getId(paramContext, str1);
          this.mConstraintSetIdMap.put(stripID(str1), Integer.valueOf(j));
          constraintSet.mIdString = Debug.getName(paramContext, j);
          b2 = b;
          break;
      } 
      b1++;
      b = b2;
    } 
    if (j != -1) {
      if (this.mMotionLayout.mDebugPath != 0)
        constraintSet.setValidateOnParse(true); 
      constraintSet.load(paramContext, paramXmlPullParser);
      if (i != -1)
        this.mDeriveMap.put(j, i); 
      this.mConstraintSetMap.put(j, constraintSet);
    } 
    return j;
  }
  
  private int parseInclude(Context paramContext, int paramInt) {
    XmlResourceParser xmlResourceParser = paramContext.getResources().getXml(paramInt);
    try {
      for (paramInt = xmlResourceParser.getEventType(); paramInt != 1; paramInt = xmlResourceParser.next()) {
        String str = xmlResourceParser.getName();
        if (2 == paramInt && "ConstraintSet".equals(str))
          return parseConstraintSet(paramContext, (XmlPullParser)xmlResourceParser); 
      } 
    } catch (XmlPullParserException xmlPullParserException) {
      xmlPullParserException.printStackTrace();
    } catch (IOException iOException) {
      iOException.printStackTrace();
    } 
    return -1;
  }
  
  private void parseInclude(Context paramContext, XmlPullParser paramXmlPullParser) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(Xml.asAttributeSet(paramXmlPullParser), R.styleable.include);
    int i = typedArray.getIndexCount();
    for (byte b = 0; b < i; b++) {
      int j = typedArray.getIndex(b);
      if (j == R.styleable.include_constraintSet)
        parseInclude(paramContext, typedArray.getResourceId(j, -1)); 
    } 
    typedArray.recycle();
  }
  
  private void parseMotionSceneTags(Context paramContext, XmlPullParser paramXmlPullParser) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(Xml.asAttributeSet(paramXmlPullParser), R.styleable.MotionScene);
    int i = typedArray.getIndexCount();
    for (byte b = 0; b < i; b++) {
      int j = typedArray.getIndex(b);
      if (j == R.styleable.MotionScene_defaultDuration) {
        j = typedArray.getInt(j, this.mDefaultDuration);
        this.mDefaultDuration = j;
        if (j < 8)
          this.mDefaultDuration = 8; 
      } else if (j == R.styleable.MotionScene_layoutDuringTransition) {
        this.mLayoutDuringTransition = typedArray.getInteger(j, 0);
      } 
    } 
    typedArray.recycle();
  }
  
  private void readConstraintChain(int paramInt, MotionLayout paramMotionLayout) {
    String str;
    ConstraintSet constraintSet = (ConstraintSet)this.mConstraintSetMap.get(paramInt);
    constraintSet.derivedState = constraintSet.mIdString;
    paramInt = this.mDeriveMap.get(paramInt);
    if (paramInt > 0) {
      readConstraintChain(paramInt, paramMotionLayout);
      ConstraintSet constraintSet1 = (ConstraintSet)this.mConstraintSetMap.get(paramInt);
      if (constraintSet1 == null) {
        String str2 = String.valueOf(Debug.getName(this.mMotionLayout.getContext(), paramInt));
        if (str2.length() != 0) {
          str2 = "ERROR! invalid deriveConstraintsFrom: @id/".concat(str2);
        } else {
          str2 = new String("ERROR! invalid deriveConstraintsFrom: @id/");
        } 
        Log.e("MotionScene", str2);
        return;
      } 
      str = String.valueOf(constraintSet.derivedState);
      String str1 = constraintSet1.derivedState;
      constraintSet.derivedState = (new StringBuilder(String.valueOf(str).length() + 1 + String.valueOf(str1).length())).append(str).append("/").append(str1).toString();
      constraintSet.readFallback(constraintSet1);
    } else {
      constraintSet.derivedState = String.valueOf(constraintSet.derivedState).concat("  layout");
      constraintSet.readFallback((ConstraintLayout)str);
    } 
    constraintSet.applyDeltaFrom(constraintSet);
  }
  
  public static String stripID(String paramString) {
    if (paramString == null)
      return ""; 
    int i = paramString.indexOf('/');
    return (i < 0) ? paramString : paramString.substring(i + 1);
  }
  
  public void addOnClickListeners(MotionLayout paramMotionLayout, int paramInt) {
    for (Transition transition : this.mTransitionList) {
      if (transition.mOnClicks.size() > 0) {
        Iterator<Transition.TransitionOnClick> iterator = transition.mOnClicks.iterator();
        while (iterator.hasNext())
          ((Transition.TransitionOnClick)iterator.next()).removeOnClickListeners(paramMotionLayout); 
      } 
    } 
    for (Transition transition : this.mAbstractTransitionList) {
      if (transition.mOnClicks.size() > 0) {
        Iterator<Transition.TransitionOnClick> iterator = transition.mOnClicks.iterator();
        while (iterator.hasNext())
          ((Transition.TransitionOnClick)iterator.next()).removeOnClickListeners(paramMotionLayout); 
      } 
    } 
    for (Transition transition : this.mTransitionList) {
      if (transition.mOnClicks.size() > 0) {
        Iterator<Transition.TransitionOnClick> iterator = transition.mOnClicks.iterator();
        while (iterator.hasNext())
          ((Transition.TransitionOnClick)iterator.next()).addOnClickListeners(paramMotionLayout, paramInt, transition); 
      } 
    } 
    for (Transition transition : this.mAbstractTransitionList) {
      if (transition.mOnClicks.size() > 0) {
        Iterator<Transition.TransitionOnClick> iterator = transition.mOnClicks.iterator();
        while (iterator.hasNext())
          ((Transition.TransitionOnClick)iterator.next()).addOnClickListeners(paramMotionLayout, paramInt, transition); 
      } 
    } 
  }
  
  public void addTransition(Transition paramTransition) {
    int i = getIndex(paramTransition);
    if (i == -1) {
      this.mTransitionList.add(paramTransition);
    } else {
      this.mTransitionList.set(i, paramTransition);
    } 
  }
  
  public boolean applyViewTransition(int paramInt, MotionController paramMotionController) {
    return this.mViewTransitionController.applyViewTransition(paramInt, paramMotionController);
  }
  
  boolean autoTransition(MotionLayout paramMotionLayout, int paramInt) {
    if (isProcessingTouch())
      return false; 
    if (this.mDisableAutoTransition)
      return false; 
    for (Transition transition1 : this.mTransitionList) {
      if (transition1.mAutoTransition == 0)
        continue; 
      Transition transition2 = this.mCurrentTransition;
      if (transition2 == transition1 && transition2.isTransitionFlag(2))
        continue; 
      if (paramInt == transition1.mConstraintSetStart && (transition1.mAutoTransition == 4 || transition1.mAutoTransition == 2)) {
        paramMotionLayout.setState(MotionLayout.TransitionState.FINISHED);
        paramMotionLayout.setTransition(transition1);
        if (transition1.mAutoTransition == 4) {
          paramMotionLayout.transitionToEnd();
          paramMotionLayout.setState(MotionLayout.TransitionState.SETUP);
          paramMotionLayout.setState(MotionLayout.TransitionState.MOVING);
        } else {
          paramMotionLayout.setProgress(1.0F);
          paramMotionLayout.evaluate(true);
          paramMotionLayout.setState(MotionLayout.TransitionState.SETUP);
          paramMotionLayout.setState(MotionLayout.TransitionState.MOVING);
          paramMotionLayout.setState(MotionLayout.TransitionState.FINISHED);
          paramMotionLayout.onNewStateAttachHandlers();
        } 
        return true;
      } 
      if (paramInt == transition1.mConstraintSetEnd && (transition1.mAutoTransition == 3 || transition1.mAutoTransition == 1)) {
        paramMotionLayout.setState(MotionLayout.TransitionState.FINISHED);
        paramMotionLayout.setTransition(transition1);
        if (transition1.mAutoTransition == 3) {
          paramMotionLayout.transitionToStart();
          paramMotionLayout.setState(MotionLayout.TransitionState.SETUP);
          paramMotionLayout.setState(MotionLayout.TransitionState.MOVING);
        } else {
          paramMotionLayout.setProgress(0.0F);
          paramMotionLayout.evaluate(true);
          paramMotionLayout.setState(MotionLayout.TransitionState.SETUP);
          paramMotionLayout.setState(MotionLayout.TransitionState.MOVING);
          paramMotionLayout.setState(MotionLayout.TransitionState.FINISHED);
          paramMotionLayout.onNewStateAttachHandlers();
        } 
        return true;
      } 
    } 
    return false;
  }
  
  public Transition bestTransitionFor(int paramInt, float paramFloat1, float paramFloat2, MotionEvent paramMotionEvent) {
    if (paramInt != -1) {
      List<Transition> list = getTransitionsWithState(paramInt);
      float f = 0.0F;
      Transition transition = null;
      RectF rectF = new RectF();
      for (Transition transition1 : list) {
        float f1;
        if (transition1.mDisable)
          continue; 
        if (transition1.mTouchResponse != null) {
          transition1.mTouchResponse.setRTL(this.mRtl);
          RectF rectF1 = transition1.mTouchResponse.getTouchRegion((ViewGroup)this.mMotionLayout, rectF);
          if (rectF1 != null && paramMotionEvent != null && !rectF1.contains(paramMotionEvent.getX(), paramMotionEvent.getY()))
            continue; 
          rectF1 = transition1.mTouchResponse.getLimitBoundsTo((ViewGroup)this.mMotionLayout, rectF);
          if (rectF1 != null && paramMotionEvent != null && !rectF1.contains(paramMotionEvent.getX(), paramMotionEvent.getY()))
            continue; 
          float f2 = transition1.mTouchResponse.dot(paramFloat1, paramFloat2);
          if (transition1.mTouchResponse.mIsRotateMode && paramMotionEvent != null) {
            float f3 = paramMotionEvent.getX() - transition1.mTouchResponse.mRotateCenterX;
            f2 = paramMotionEvent.getY() - transition1.mTouchResponse.mRotateCenterY;
            f2 = 10.0F * (float)(Math.atan2((paramFloat2 + f2), (paramFloat1 + f3)) - Math.atan2(f3, f2));
          } 
          if (transition1.mConstraintSetEnd == paramInt) {
            f2 *= -1.0F;
          } else {
            f2 *= 1.1F;
          } 
          f1 = f;
          if (f2 > f) {
            transition = transition1;
            f1 = f2;
          } 
        } else {
          f1 = f;
        } 
        f = f1;
      } 
      return transition;
    } 
    return this.mCurrentTransition;
  }
  
  public void disableAutoTransition(boolean paramBoolean) {
    this.mDisableAutoTransition = paramBoolean;
  }
  
  public void enableViewTransition(int paramInt, boolean paramBoolean) {
    this.mViewTransitionController.enableViewTransition(paramInt, paramBoolean);
  }
  
  public int gatPathMotionArc() {
    byte b;
    Transition transition = this.mCurrentTransition;
    if (transition != null) {
      b = transition.mPathMotionArc;
    } else {
      b = -1;
    } 
    return b;
  }
  
  int getAutoCompleteMode() {
    Transition transition = this.mCurrentTransition;
    return (transition != null && transition.mTouchResponse != null) ? this.mCurrentTransition.mTouchResponse.getAutoCompleteMode() : 0;
  }
  
  ConstraintSet getConstraintSet(int paramInt) {
    return getConstraintSet(paramInt, -1, -1);
  }
  
  ConstraintSet getConstraintSet(int paramInt1, int paramInt2, int paramInt3) {
    if (this.DEBUG_DESKTOP) {
      System.out.println((new StringBuilder(14)).append("id ").append(paramInt1).toString());
      PrintStream printStream = System.out;
      int j = this.mConstraintSetMap.size();
      printStream.println((new StringBuilder(16)).append("size ").append(j).toString());
    } 
    StateSet stateSet = this.mStateSet;
    int i = paramInt1;
    if (stateSet != null) {
      paramInt2 = stateSet.stateGetConstraintID(paramInt1, paramInt2, paramInt3);
      i = paramInt1;
      if (paramInt2 != -1)
        i = paramInt2; 
    } 
    if (this.mConstraintSetMap.get(i) == null) {
      String str = Debug.getName(this.mMotionLayout.getContext(), i);
      Log.e("MotionScene", (new StringBuilder(String.valueOf(str).length() + 55)).append("Warning could not find ConstraintSet id/").append(str).append(" In MotionScene").toString());
      SparseArray<ConstraintSet> sparseArray = this.mConstraintSetMap;
      return (ConstraintSet)sparseArray.get(sparseArray.keyAt(0));
    } 
    return (ConstraintSet)this.mConstraintSetMap.get(i);
  }
  
  public ConstraintSet getConstraintSet(Context paramContext, String paramString) {
    if (this.DEBUG_DESKTOP) {
      PrintStream printStream2 = System.out;
      String str = String.valueOf(paramString);
      if (str.length() != 0) {
        str = "id ".concat(str);
      } else {
        str = new String("id ");
      } 
      printStream2.println(str);
      PrintStream printStream1 = System.out;
      int i = this.mConstraintSetMap.size();
      printStream1.println((new StringBuilder(16)).append("size ").append(i).toString());
    } 
    for (byte b = 0; b < this.mConstraintSetMap.size(); b++) {
      int i = this.mConstraintSetMap.keyAt(b);
      String str = paramContext.getResources().getResourceName(i);
      if (this.DEBUG_DESKTOP)
        System.out.println((new StringBuilder(String.valueOf(str).length() + 41 + String.valueOf(paramString).length())).append("Id for <").append(b).append("> is <").append(str).append("> looking for <").append(paramString).append(">").toString()); 
      if (paramString.equals(str))
        return (ConstraintSet)this.mConstraintSetMap.get(i); 
    } 
    return null;
  }
  
  public int[] getConstraintSetIds() {
    int[] arrayOfInt = new int[this.mConstraintSetMap.size()];
    for (byte b = 0; b < arrayOfInt.length; b++)
      arrayOfInt[b] = this.mConstraintSetMap.keyAt(b); 
    return arrayOfInt;
  }
  
  public ArrayList<Transition> getDefinedTransitions() {
    return this.mTransitionList;
  }
  
  public int getDuration() {
    Transition transition = this.mCurrentTransition;
    return (transition != null) ? transition.mDuration : this.mDefaultDuration;
  }
  
  int getEndId() {
    Transition transition = this.mCurrentTransition;
    return (transition == null) ? -1 : transition.mConstraintSetEnd;
  }
  
  public Interpolator getInterpolator() {
    switch (this.mCurrentTransition.mDefaultInterpolator) {
      default:
        return null;
      case 6:
        return (Interpolator)new AnticipateInterpolator();
      case 5:
        return (Interpolator)new OvershootInterpolator();
      case 4:
        return (Interpolator)new BounceInterpolator();
      case 3:
        return null;
      case 2:
        return (Interpolator)new DecelerateInterpolator();
      case 1:
        return (Interpolator)new AccelerateInterpolator();
      case 0:
        return (Interpolator)new AccelerateDecelerateInterpolator();
      case -1:
        return new Interpolator(this) {
            final Easing val$easing;
            
            public float getInterpolation(float param1Float) {
              return (float)easing.get(param1Float);
            }
          };
      case -2:
        break;
    } 
    return AnimationUtils.loadInterpolator(this.mMotionLayout.getContext(), this.mCurrentTransition.mDefaultInterpolatorID);
  }
  
  Key getKeyFrame(Context paramContext, int paramInt1, int paramInt2, int paramInt3) {
    Transition transition = this.mCurrentTransition;
    if (transition == null)
      return null; 
    for (KeyFrames keyFrames : transition.mKeyFramesList) {
      for (Integer integer : keyFrames.getKeys()) {
        if (paramInt2 == integer.intValue())
          for (Key key : keyFrames.getKeyFramesForView(integer.intValue())) {
            if (key.mFramePosition == paramInt3 && key.mType == paramInt1)
              return key; 
          }  
      } 
    } 
    return null;
  }
  
  public void getKeyFrames(MotionController paramMotionController) {
    Transition transition = this.mCurrentTransition;
    if (transition == null) {
      transition = this.mDefaultTransition;
      if (transition != null) {
        iterator = transition.mKeyFramesList.iterator();
        while (iterator.hasNext())
          ((KeyFrames)iterator.next()).addFrames(paramMotionController); 
      } 
      return;
    } 
    Iterator<KeyFrames> iterator = ((Transition)iterator).mKeyFramesList.iterator();
    while (iterator.hasNext())
      ((KeyFrames)iterator.next()).addFrames(paramMotionController); 
  }
  
  float getMaxAcceleration() {
    Transition transition = this.mCurrentTransition;
    return (transition != null && transition.mTouchResponse != null) ? this.mCurrentTransition.mTouchResponse.getMaxAcceleration() : 0.0F;
  }
  
  float getMaxVelocity() {
    Transition transition = this.mCurrentTransition;
    return (transition != null && transition.mTouchResponse != null) ? this.mCurrentTransition.mTouchResponse.getMaxVelocity() : 0.0F;
  }
  
  boolean getMoveWhenScrollAtTop() {
    Transition transition = this.mCurrentTransition;
    return (transition != null && transition.mTouchResponse != null) ? this.mCurrentTransition.mTouchResponse.getMoveWhenScrollAtTop() : false;
  }
  
  public float getPathPercent(View paramView, int paramInt) {
    return 0.0F;
  }
  
  float getProgressDirection(float paramFloat1, float paramFloat2) {
    Transition transition = this.mCurrentTransition;
    return (transition != null && transition.mTouchResponse != null) ? this.mCurrentTransition.mTouchResponse.getProgressDirection(paramFloat1, paramFloat2) : 0.0F;
  }
  
  int getSpringBoundary() {
    Transition transition = this.mCurrentTransition;
    return (transition != null && transition.mTouchResponse != null) ? this.mCurrentTransition.mTouchResponse.getSpringBoundary() : 0;
  }
  
  float getSpringDamping() {
    Transition transition = this.mCurrentTransition;
    return (transition != null && transition.mTouchResponse != null) ? this.mCurrentTransition.mTouchResponse.getSpringDamping() : 0.0F;
  }
  
  float getSpringMass() {
    Transition transition = this.mCurrentTransition;
    return (transition != null && transition.mTouchResponse != null) ? this.mCurrentTransition.mTouchResponse.getSpringMass() : 0.0F;
  }
  
  float getSpringStiffiness() {
    Transition transition = this.mCurrentTransition;
    return (transition != null && transition.mTouchResponse != null) ? this.mCurrentTransition.mTouchResponse.getSpringStiffness() : 0.0F;
  }
  
  float getSpringStopThreshold() {
    Transition transition = this.mCurrentTransition;
    return (transition != null && transition.mTouchResponse != null) ? this.mCurrentTransition.mTouchResponse.getSpringStopThreshold() : 0.0F;
  }
  
  public float getStaggered() {
    Transition transition = this.mCurrentTransition;
    return (transition != null) ? transition.mStagger : 0.0F;
  }
  
  int getStartId() {
    Transition transition = this.mCurrentTransition;
    return (transition == null) ? -1 : transition.mConstraintSetStart;
  }
  
  public Transition getTransitionById(int paramInt) {
    for (Transition transition : this.mTransitionList) {
      if (transition.mId == paramInt)
        return transition; 
    } 
    return null;
  }
  
  int getTransitionDirection(int paramInt) {
    Iterator<Transition> iterator = this.mTransitionList.iterator();
    while (iterator.hasNext()) {
      if ((iterator.next()).mConstraintSetStart == paramInt)
        return 0; 
    } 
    return 1;
  }
  
  public List<Transition> getTransitionsWithState(int paramInt) {
    paramInt = getRealID(paramInt);
    ArrayList<Transition> arrayList = new ArrayList();
    for (Transition transition : this.mTransitionList) {
      if (transition.mConstraintSetStart == paramInt || transition.mConstraintSetEnd == paramInt)
        arrayList.add(transition); 
    } 
    return arrayList;
  }
  
  boolean hasKeyFramePosition(View paramView, int paramInt) {
    Transition transition = this.mCurrentTransition;
    if (transition == null)
      return false; 
    Iterator<KeyFrames> iterator = transition.mKeyFramesList.iterator();
    while (iterator.hasNext()) {
      Iterator<Key> iterator1 = ((KeyFrames)iterator.next()).getKeyFramesForView(paramView.getId()).iterator();
      while (iterator1.hasNext()) {
        if (((Key)iterator1.next()).mFramePosition == paramInt)
          return true; 
      } 
    } 
    return false;
  }
  
  public boolean isViewTransitionEnabled(int paramInt) {
    return this.mViewTransitionController.isViewTransitionEnabled(paramInt);
  }
  
  public int lookUpConstraintId(String paramString) {
    Integer integer = this.mConstraintSetIdMap.get(paramString);
    return (integer == null) ? 0 : integer.intValue();
  }
  
  public String lookUpConstraintName(int paramInt) {
    for (Map.Entry<String, Integer> entry : this.mConstraintSetIdMap.entrySet()) {
      Integer integer = (Integer)entry.getValue();
      if (integer != null && integer.intValue() == paramInt)
        return (String)entry.getKey(); 
    } 
    return null;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {}
  
  void processScrollMove(float paramFloat1, float paramFloat2) {
    Transition transition = this.mCurrentTransition;
    if (transition != null && transition.mTouchResponse != null)
      this.mCurrentTransition.mTouchResponse.scrollMove(paramFloat1, paramFloat2); 
  }
  
  void processScrollUp(float paramFloat1, float paramFloat2) {
    Transition transition = this.mCurrentTransition;
    if (transition != null && transition.mTouchResponse != null)
      this.mCurrentTransition.mTouchResponse.scrollUp(paramFloat1, paramFloat2); 
  }
  
  void processTouchEvent(MotionEvent paramMotionEvent, int paramInt, MotionLayout paramMotionLayout) {
    RectF rectF1;
    RectF rectF2 = new RectF();
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = this.mMotionLayout.obtainVelocityTracker(); 
    this.mVelocityTracker.addMovement(paramMotionEvent);
    if (paramInt != -1) {
      float f1;
      float f2;
      int i = paramMotionEvent.getAction();
      boolean bool = false;
      switch (i) {
        case 2:
          if (this.mIgnoreTouch)
            break; 
          f1 = paramMotionEvent.getRawY() - this.mLastTouchY;
          f2 = paramMotionEvent.getRawX() - this.mLastTouchX;
          if (f2 != 0.0D || f1 != 0.0D) {
            MotionEvent motionEvent = this.mLastTouchDown;
            if (motionEvent != null) {
              Transition transition1 = bestTransitionFor(paramInt, f2, f1, motionEvent);
              if (transition1 != null) {
                paramMotionLayout.setTransition(transition1);
                rectF2 = this.mCurrentTransition.mTouchResponse.getTouchRegion((ViewGroup)this.mMotionLayout, rectF2);
                if (rectF2 != null && !rectF2.contains(this.mLastTouchDown.getX(), this.mLastTouchDown.getY()))
                  bool = true; 
                this.mMotionOutsideRegion = bool;
                this.mCurrentTransition.mTouchResponse.setUpTouchEvent(this.mLastTouchX, this.mLastTouchY);
              } 
              break;
            } 
          } 
          return;
        case 0:
          this.mLastTouchX = paramMotionEvent.getRawX();
          this.mLastTouchY = paramMotionEvent.getRawY();
          this.mLastTouchDown = paramMotionEvent;
          this.mIgnoreTouch = false;
          if (this.mCurrentTransition.mTouchResponse != null) {
            rectF1 = this.mCurrentTransition.mTouchResponse.getLimitBoundsTo((ViewGroup)this.mMotionLayout, rectF2);
            if (rectF1 != null && !rectF1.contains(this.mLastTouchDown.getX(), this.mLastTouchDown.getY())) {
              this.mLastTouchDown = null;
              this.mIgnoreTouch = true;
              return;
            } 
            rectF1 = this.mCurrentTransition.mTouchResponse.getTouchRegion((ViewGroup)this.mMotionLayout, rectF2);
            if (rectF1 != null && !rectF1.contains(this.mLastTouchDown.getX(), this.mLastTouchDown.getY())) {
              this.mMotionOutsideRegion = true;
            } else {
              this.mMotionOutsideRegion = false;
            } 
            this.mCurrentTransition.mTouchResponse.setDown(this.mLastTouchX, this.mLastTouchY);
          } 
          return;
      } 
    } 
    if (this.mIgnoreTouch)
      return; 
    Transition transition = this.mCurrentTransition;
    if (transition != null && transition.mTouchResponse != null && !this.mMotionOutsideRegion)
      this.mCurrentTransition.mTouchResponse.processTouchEvent((MotionEvent)rectF1, this.mVelocityTracker, paramInt, this); 
    this.mLastTouchX = rectF1.getRawX();
    this.mLastTouchY = rectF1.getRawY();
    if (rectF1.getAction() == 1) {
      MotionLayout.MotionTracker motionTracker = this.mVelocityTracker;
      if (motionTracker != null) {
        motionTracker.recycle();
        this.mVelocityTracker = null;
        if (paramMotionLayout.mCurrentState != -1)
          autoTransition(paramMotionLayout, paramMotionLayout.mCurrentState); 
      } 
    } 
  }
  
  void readFallback(MotionLayout paramMotionLayout) {
    for (byte b = 0; b < this.mConstraintSetMap.size(); b++) {
      int i = this.mConstraintSetMap.keyAt(b);
      if (hasCycleDependency(i)) {
        Log.e("MotionScene", "Cannot be derived from yourself");
        return;
      } 
      readConstraintChain(i, paramMotionLayout);
    } 
  }
  
  public void removeTransition(Transition paramTransition) {
    int i = getIndex(paramTransition);
    if (i != -1)
      this.mTransitionList.remove(i); 
  }
  
  public void setConstraintSet(int paramInt, ConstraintSet paramConstraintSet) {
    this.mConstraintSetMap.put(paramInt, paramConstraintSet);
  }
  
  public void setDuration(int paramInt) {
    Transition transition = this.mCurrentTransition;
    if (transition != null) {
      transition.setDuration(paramInt);
    } else {
      this.mDefaultDuration = paramInt;
    } 
  }
  
  public void setKeyframe(View paramView, int paramInt, String paramString, Object paramObject) {
    Transition transition = this.mCurrentTransition;
    if (transition == null)
      return; 
    Iterator<KeyFrames> iterator = transition.mKeyFramesList.iterator();
    while (iterator.hasNext()) {
      Iterator<Key> iterator1 = ((KeyFrames)iterator.next()).getKeyFramesForView(paramView.getId()).iterator();
      while (iterator1.hasNext()) {
        if (((Key)iterator1.next()).mFramePosition == paramInt) {
          float f = 0.0F;
          if (paramObject != null)
            f = ((Float)paramObject).floatValue(); 
          if (f == 0.0F);
          paramString.equalsIgnoreCase("app:PerpendicularPath_percent");
        } 
      } 
    } 
  }
  
  public void setRtl(boolean paramBoolean) {
    this.mRtl = paramBoolean;
    Transition transition = this.mCurrentTransition;
    if (transition != null && transition.mTouchResponse != null)
      this.mCurrentTransition.mTouchResponse.setRTL(this.mRtl); 
  }
  
  void setTransition(int paramInt1, int paramInt2) {
    int i = paramInt1;
    int j = paramInt2;
    StateSet stateSet = this.mStateSet;
    int m = i;
    int k = j;
    if (stateSet != null) {
      k = stateSet.stateGetConstraintID(paramInt1, -1, -1);
      if (k != -1)
        i = k; 
      int n = this.mStateSet.stateGetConstraintID(paramInt2, -1, -1);
      m = i;
      k = j;
      if (n != -1) {
        k = n;
        m = i;
      } 
    } 
    Transition transition = this.mCurrentTransition;
    if (transition != null && transition.mConstraintSetEnd == paramInt2 && this.mCurrentTransition.mConstraintSetStart == paramInt1)
      return; 
    for (Transition transition1 : this.mTransitionList) {
      if ((transition1.mConstraintSetEnd == k && transition1.mConstraintSetStart == m) || (transition1.mConstraintSetEnd == paramInt2 && transition1.mConstraintSetStart == paramInt1)) {
        this.mCurrentTransition = transition1;
        if (transition1 != null && transition1.mTouchResponse != null)
          this.mCurrentTransition.mTouchResponse.setRTL(this.mRtl); 
        return;
      } 
    } 
    transition = this.mDefaultTransition;
    for (Transition transition1 : this.mAbstractTransitionList) {
      if (transition1.mConstraintSetEnd == paramInt2)
        transition = transition1; 
    } 
    transition = new Transition(this, transition);
    Transition.access$102(transition, m);
    Transition.access$002(transition, k);
    if (m != -1)
      this.mTransitionList.add(transition); 
    this.mCurrentTransition = transition;
  }
  
  public void setTransition(Transition paramTransition) {
    this.mCurrentTransition = paramTransition;
    if (paramTransition != null && paramTransition.mTouchResponse != null)
      this.mCurrentTransition.mTouchResponse.setRTL(this.mRtl); 
  }
  
  void setupTouch() {
    Transition transition = this.mCurrentTransition;
    if (transition != null && transition.mTouchResponse != null)
      this.mCurrentTransition.mTouchResponse.setupTouch(); 
  }
  
  boolean supportTouch() {
    Iterator<Transition> iterator = this.mTransitionList.iterator();
    while (true) {
      boolean bool1 = iterator.hasNext();
      boolean bool = true;
      if (bool1) {
        if ((iterator.next()).mTouchResponse != null)
          return true; 
        continue;
      } 
      Transition transition = this.mCurrentTransition;
      if (transition == null || transition.mTouchResponse == null)
        bool = false; 
      return bool;
    } 
  }
  
  public boolean validateLayout(MotionLayout paramMotionLayout) {
    boolean bool;
    if (paramMotionLayout == this.mMotionLayout && paramMotionLayout.mScene == this) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void viewTransition(int paramInt, View... paramVarArgs) {
    this.mViewTransitionController.viewTransition(paramInt, paramVarArgs);
  }
  
  public static class Transition {
    public static final int AUTO_ANIMATE_TO_END = 4;
    
    public static final int AUTO_ANIMATE_TO_START = 3;
    
    public static final int AUTO_JUMP_TO_END = 2;
    
    public static final int AUTO_JUMP_TO_START = 1;
    
    public static final int AUTO_NONE = 0;
    
    static final int TRANSITION_FLAG_FIRST_DRAW = 1;
    
    static final int TRANSITION_FLAG_INTERCEPT_TOUCH = 4;
    
    static final int TRANSITION_FLAG_INTRA_AUTO = 2;
    
    private int mAutoTransition = 0;
    
    private int mConstraintSetEnd = -1;
    
    private int mConstraintSetStart = -1;
    
    private int mDefaultInterpolator = 0;
    
    private int mDefaultInterpolatorID = -1;
    
    private String mDefaultInterpolatorString = null;
    
    private boolean mDisable = false;
    
    private int mDuration = 400;
    
    private int mId = -1;
    
    private boolean mIsAbstract = false;
    
    private ArrayList<KeyFrames> mKeyFramesList = new ArrayList<>();
    
    private int mLayoutDuringTransition = 0;
    
    private final MotionScene mMotionScene;
    
    private ArrayList<TransitionOnClick> mOnClicks = new ArrayList<>();
    
    private int mPathMotionArc = -1;
    
    private float mStagger = 0.0F;
    
    private TouchResponse mTouchResponse = null;
    
    private int mTransitionFlags = 0;
    
    public Transition(int param1Int1, MotionScene param1MotionScene, int param1Int2, int param1Int3) {
      this.mId = param1Int1;
      this.mMotionScene = param1MotionScene;
      this.mConstraintSetStart = param1Int2;
      this.mConstraintSetEnd = param1Int3;
      this.mDuration = param1MotionScene.mDefaultDuration;
      this.mLayoutDuringTransition = param1MotionScene.mLayoutDuringTransition;
    }
    
    Transition(MotionScene param1MotionScene, Context param1Context, XmlPullParser param1XmlPullParser) {
      this.mDuration = param1MotionScene.mDefaultDuration;
      this.mLayoutDuringTransition = param1MotionScene.mLayoutDuringTransition;
      this.mMotionScene = param1MotionScene;
      fillFromAttributeList(param1MotionScene, param1Context, Xml.asAttributeSet(param1XmlPullParser));
    }
    
    Transition(MotionScene param1MotionScene, Transition param1Transition) {
      this.mMotionScene = param1MotionScene;
      this.mDuration = param1MotionScene.mDefaultDuration;
      if (param1Transition != null) {
        this.mPathMotionArc = param1Transition.mPathMotionArc;
        this.mDefaultInterpolator = param1Transition.mDefaultInterpolator;
        this.mDefaultInterpolatorString = param1Transition.mDefaultInterpolatorString;
        this.mDefaultInterpolatorID = param1Transition.mDefaultInterpolatorID;
        this.mDuration = param1Transition.mDuration;
        this.mKeyFramesList = param1Transition.mKeyFramesList;
        this.mStagger = param1Transition.mStagger;
        this.mLayoutDuringTransition = param1Transition.mLayoutDuringTransition;
      } 
    }
    
    private void fill(MotionScene param1MotionScene, Context param1Context, TypedArray param1TypedArray) {
      int i = param1TypedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = param1TypedArray.getIndex(b);
        if (j == R.styleable.Transition_constraintSetEnd) {
          ConstraintSet constraintSet;
          this.mConstraintSetEnd = param1TypedArray.getResourceId(j, -1);
          String str = param1Context.getResources().getResourceTypeName(this.mConstraintSetEnd);
          if ("layout".equals(str)) {
            constraintSet = new ConstraintSet();
            constraintSet.load(param1Context, this.mConstraintSetEnd);
            param1MotionScene.mConstraintSetMap.append(this.mConstraintSetEnd, constraintSet);
          } else if ("xml".equals(constraintSet)) {
            this.mConstraintSetEnd = param1MotionScene.parseInclude(param1Context, this.mConstraintSetEnd);
          } 
        } else if (j == R.styleable.Transition_constraintSetStart) {
          ConstraintSet constraintSet;
          this.mConstraintSetStart = param1TypedArray.getResourceId(j, this.mConstraintSetStart);
          String str = param1Context.getResources().getResourceTypeName(this.mConstraintSetStart);
          if ("layout".equals(str)) {
            constraintSet = new ConstraintSet();
            constraintSet.load(param1Context, this.mConstraintSetStart);
            param1MotionScene.mConstraintSetMap.append(this.mConstraintSetStart, constraintSet);
          } else if ("xml".equals(constraintSet)) {
            this.mConstraintSetStart = param1MotionScene.parseInclude(param1Context, this.mConstraintSetStart);
          } 
        } else if (j == R.styleable.Transition_motionInterpolator) {
          TypedValue typedValue = param1TypedArray.peekValue(j);
          if (typedValue.type == 1) {
            j = param1TypedArray.getResourceId(j, -1);
            this.mDefaultInterpolatorID = j;
            if (j != -1)
              this.mDefaultInterpolator = -2; 
          } else if (typedValue.type == 3) {
            String str = param1TypedArray.getString(j);
            this.mDefaultInterpolatorString = str;
            if (str != null)
              if (str.indexOf("/") > 0) {
                this.mDefaultInterpolatorID = param1TypedArray.getResourceId(j, -1);
                this.mDefaultInterpolator = -2;
              } else {
                this.mDefaultInterpolator = -1;
              }  
          } else {
            this.mDefaultInterpolator = param1TypedArray.getInteger(j, this.mDefaultInterpolator);
          } 
        } else if (j == R.styleable.Transition_duration) {
          j = param1TypedArray.getInt(j, this.mDuration);
          this.mDuration = j;
          if (j < 8)
            this.mDuration = 8; 
        } else if (j == R.styleable.Transition_staggered) {
          this.mStagger = param1TypedArray.getFloat(j, this.mStagger);
        } else if (j == R.styleable.Transition_autoTransition) {
          this.mAutoTransition = param1TypedArray.getInteger(j, this.mAutoTransition);
        } else if (j == R.styleable.Transition_android_id) {
          this.mId = param1TypedArray.getResourceId(j, this.mId);
        } else if (j == R.styleable.Transition_transitionDisable) {
          this.mDisable = param1TypedArray.getBoolean(j, this.mDisable);
        } else if (j == R.styleable.Transition_pathMotionArc) {
          this.mPathMotionArc = param1TypedArray.getInteger(j, -1);
        } else if (j == R.styleable.Transition_layoutDuringTransition) {
          this.mLayoutDuringTransition = param1TypedArray.getInteger(j, 0);
        } else if (j == R.styleable.Transition_transitionFlags) {
          this.mTransitionFlags = param1TypedArray.getInteger(j, 0);
        } 
      } 
      if (this.mConstraintSetStart == -1)
        this.mIsAbstract = true; 
    }
    
    private void fillFromAttributeList(MotionScene param1MotionScene, Context param1Context, AttributeSet param1AttributeSet) {
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.Transition);
      fill(param1MotionScene, param1Context, typedArray);
      typedArray.recycle();
    }
    
    public void addKeyFrame(KeyFrames param1KeyFrames) {
      this.mKeyFramesList.add(param1KeyFrames);
    }
    
    public void addOnClick(int param1Int1, int param1Int2) {
      for (TransitionOnClick transitionOnClick1 : this.mOnClicks) {
        if (transitionOnClick1.mTargetId == param1Int1) {
          transitionOnClick1.mMode = param1Int2;
          return;
        } 
      } 
      TransitionOnClick transitionOnClick = new TransitionOnClick(this, param1Int1, param1Int2);
      this.mOnClicks.add(transitionOnClick);
    }
    
    public void addOnClick(Context param1Context, XmlPullParser param1XmlPullParser) {
      this.mOnClicks.add(new TransitionOnClick(param1Context, this, param1XmlPullParser));
    }
    
    public String debugString(Context param1Context) {
      String str1;
      String str2;
      if (this.mConstraintSetStart == -1) {
        str2 = "null";
      } else {
        str2 = param1Context.getResources().getResourceEntryName(this.mConstraintSetStart);
      } 
      if (this.mConstraintSetEnd == -1) {
        str1 = String.valueOf(str2).concat(" -> null");
      } else {
        str2 = String.valueOf(str2);
        str1 = str1.getResources().getResourceEntryName(this.mConstraintSetEnd);
        str1 = (new StringBuilder(String.valueOf(str2).length() + 4 + String.valueOf(str1).length())).append(str2).append(" -> ").append(str1).toString();
      } 
      return str1;
    }
    
    public int getAutoTransition() {
      return this.mAutoTransition;
    }
    
    public int getDuration() {
      return this.mDuration;
    }
    
    public int getEndConstraintSetId() {
      return this.mConstraintSetEnd;
    }
    
    public int getId() {
      return this.mId;
    }
    
    public List<KeyFrames> getKeyFrameList() {
      return this.mKeyFramesList;
    }
    
    public int getLayoutDuringTransition() {
      return this.mLayoutDuringTransition;
    }
    
    public List<TransitionOnClick> getOnClickList() {
      return this.mOnClicks;
    }
    
    public int getPathMotionArc() {
      return this.mPathMotionArc;
    }
    
    public float getStagger() {
      return this.mStagger;
    }
    
    public int getStartConstraintSetId() {
      return this.mConstraintSetStart;
    }
    
    public TouchResponse getTouchResponse() {
      return this.mTouchResponse;
    }
    
    public boolean isEnabled() {
      return this.mDisable ^ true;
    }
    
    public boolean isTransitionFlag(int param1Int) {
      boolean bool;
      if ((this.mTransitionFlags & param1Int) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void removeOnClick(int param1Int) {
      TransitionOnClick transitionOnClick1;
      TransitionOnClick transitionOnClick2 = null;
      Iterator<TransitionOnClick> iterator = this.mOnClicks.iterator();
      while (true) {
        transitionOnClick1 = transitionOnClick2;
        if (iterator.hasNext()) {
          transitionOnClick1 = iterator.next();
          if (transitionOnClick1.mTargetId == param1Int)
            break; 
          continue;
        } 
        break;
      } 
      if (transitionOnClick1 != null)
        this.mOnClicks.remove(transitionOnClick1); 
    }
    
    public void setAutoTransition(int param1Int) {
      this.mAutoTransition = param1Int;
    }
    
    public void setDuration(int param1Int) {
      this.mDuration = Math.max(param1Int, 8);
    }
    
    public void setEnable(boolean param1Boolean) {
      setEnabled(param1Boolean);
    }
    
    public void setEnabled(boolean param1Boolean) {
      this.mDisable = param1Boolean ^ true;
    }
    
    public void setInterpolatorInfo(int param1Int1, String param1String, int param1Int2) {
      this.mDefaultInterpolator = param1Int1;
      this.mDefaultInterpolatorString = param1String;
      this.mDefaultInterpolatorID = param1Int2;
    }
    
    public void setLayoutDuringTransition(int param1Int) {
      this.mLayoutDuringTransition = param1Int;
    }
    
    public void setOnSwipe(OnSwipe param1OnSwipe) {
      TouchResponse touchResponse;
      if (param1OnSwipe == null) {
        param1OnSwipe = null;
      } else {
        touchResponse = new TouchResponse(this.mMotionScene.mMotionLayout, param1OnSwipe);
      } 
      this.mTouchResponse = touchResponse;
    }
    
    public void setOnTouchUp(int param1Int) {
      TouchResponse touchResponse = getTouchResponse();
      if (touchResponse != null)
        touchResponse.setTouchUpMode(param1Int); 
    }
    
    public void setPathMotionArc(int param1Int) {
      this.mPathMotionArc = param1Int;
    }
    
    public void setStagger(float param1Float) {
      this.mStagger = param1Float;
    }
    
    public void setTransitionFlag(int param1Int) {
      this.mTransitionFlags = param1Int;
    }
    
    public static class TransitionOnClick implements View.OnClickListener {
      public static final int ANIM_TOGGLE = 17;
      
      public static final int ANIM_TO_END = 1;
      
      public static final int ANIM_TO_START = 16;
      
      public static final int JUMP_TO_END = 256;
      
      public static final int JUMP_TO_START = 4096;
      
      int mMode = 17;
      
      int mTargetId = -1;
      
      private final MotionScene.Transition mTransition;
      
      public TransitionOnClick(Context param2Context, MotionScene.Transition param2Transition, XmlPullParser param2XmlPullParser) {
        this.mTransition = param2Transition;
        TypedArray typedArray = param2Context.obtainStyledAttributes(Xml.asAttributeSet(param2XmlPullParser), R.styleable.OnClick);
        int i = typedArray.getIndexCount();
        for (byte b = 0; b < i; b++) {
          int j = typedArray.getIndex(b);
          if (j == R.styleable.OnClick_targetId) {
            this.mTargetId = typedArray.getResourceId(j, this.mTargetId);
          } else if (j == R.styleable.OnClick_clickAction) {
            this.mMode = typedArray.getInt(j, this.mMode);
          } 
        } 
        typedArray.recycle();
      }
      
      public TransitionOnClick(MotionScene.Transition param2Transition, int param2Int1, int param2Int2) {
        this.mTransition = param2Transition;
        this.mTargetId = param2Int1;
        this.mMode = param2Int2;
      }
      
      public void addOnClickListeners(MotionLayout param2MotionLayout, int param2Int, MotionScene.Transition param2Transition) {
        View view;
        byte b;
        boolean bool1;
        int i = this.mTargetId;
        if (i != -1)
          view = param2MotionLayout.findViewById(i); 
        if (view == null) {
          param2Int = this.mTargetId;
          Log.e("MotionScene", (new StringBuilder(37)).append("OnClick could not find id ").append(param2Int).toString());
          return;
        } 
        int j = param2Transition.mConstraintSetStart;
        int m = param2Transition.mConstraintSetEnd;
        if (j == -1) {
          view.setOnClickListener(this);
          return;
        } 
        int k = this.mMode;
        boolean bool3 = false;
        if ((k & 0x1) != 0 && param2Int == j) {
          i = 1;
        } else {
          i = 0;
        } 
        if ((k & 0x100) != 0 && param2Int == j) {
          b = 1;
        } else {
          b = 0;
        } 
        if ((k & 0x1) != 0 && param2Int == j) {
          j = 1;
        } else {
          j = 0;
        } 
        if ((k & 0x10) != 0 && param2Int == m) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        boolean bool2 = bool3;
        if ((k & 0x1000) != 0) {
          bool2 = bool3;
          if (param2Int == m)
            bool2 = true; 
        } 
        if ((i | b | j | bool1 | bool2) != 0)
          view.setOnClickListener(this); 
      }
      
      boolean isTransitionViable(MotionScene.Transition param2Transition, MotionLayout param2MotionLayout) {
        MotionScene.Transition transition = this.mTransition;
        boolean bool2 = true;
        boolean bool1 = true;
        if (transition == param2Transition)
          return true; 
        int i = transition.mConstraintSetEnd;
        int j = this.mTransition.mConstraintSetStart;
        if (j == -1) {
          if (param2MotionLayout.mCurrentState == i)
            bool1 = false; 
          return bool1;
        } 
        bool1 = bool2;
        if (param2MotionLayout.mCurrentState != j)
          if (param2MotionLayout.mCurrentState == i) {
            bool1 = bool2;
          } else {
            bool1 = false;
          }  
        return bool1;
      }
      
      public void onClick(View param2View) {
        boolean bool1;
        MotionLayout motionLayout = this.mTransition.mMotionScene.mMotionLayout;
        if (!motionLayout.isInteractionEnabled())
          return; 
        if (this.mTransition.mConstraintSetStart == -1) {
          bool1 = motionLayout.getCurrentState();
          if (bool1 == -1) {
            motionLayout.transitionToState(this.mTransition.mConstraintSetEnd);
            return;
          } 
          MotionScene.Transition transition1 = new MotionScene.Transition(this.mTransition.mMotionScene, this.mTransition);
          MotionScene.Transition.access$102(transition1, bool1);
          MotionScene.Transition.access$002(transition1, this.mTransition.mConstraintSetEnd);
          motionLayout.setTransition(transition1);
          motionLayout.transitionToEnd();
          return;
        } 
        MotionScene.Transition transition = this.mTransition.mMotionScene.mCurrentTransition;
        int i = this.mMode;
        boolean bool3 = false;
        if ((i & 0x1) != 0 || (i & 0x100) != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        if ((i & 0x10) != 0 || (i & 0x1000) != 0) {
          i = 1;
        } else {
          i = 0;
        } 
        boolean bool2 = bool3;
        if (bool1) {
          bool2 = bool3;
          if (i != 0)
            bool2 = true; 
        } 
        int j = i;
        bool3 = bool1;
        if (bool2) {
          MotionScene.Transition transition2 = this.mTransition.mMotionScene.mCurrentTransition;
          MotionScene.Transition transition1 = this.mTransition;
          if (transition2 != transition1)
            motionLayout.setTransition(transition1); 
          if (motionLayout.getCurrentState() == motionLayout.getEndState() || motionLayout.getProgress() > 0.5F) {
            bool3 = false;
            j = i;
          } else {
            j = 0;
            bool3 = bool1;
          } 
        } 
        if (isTransitionViable(transition, motionLayout))
          if (bool3 && (0x1 & this.mMode) != 0) {
            motionLayout.setTransition(this.mTransition);
            motionLayout.transitionToEnd();
          } else if (j != 0 && (this.mMode & 0x10) != 0) {
            motionLayout.setTransition(this.mTransition);
            motionLayout.transitionToStart();
          } else if (bool3 && (this.mMode & 0x100) != 0) {
            motionLayout.setTransition(this.mTransition);
            motionLayout.setProgress(1.0F);
          } else if (j != 0 && (this.mMode & 0x1000) != 0) {
            motionLayout.setTransition(this.mTransition);
            motionLayout.setProgress(0.0F);
          }  
      }
      
      public void removeOnClickListeners(MotionLayout param2MotionLayout) {
        int i = this.mTargetId;
        if (i == -1)
          return; 
        View view = param2MotionLayout.findViewById(i);
        if (view == null) {
          i = this.mTargetId;
          Log.e("MotionScene", (new StringBuilder(35)).append(" (*)  could not find id ").append(i).toString());
          return;
        } 
        view.setOnClickListener(null);
      }
    }
  }
  
  public static class TransitionOnClick implements View.OnClickListener {
    public static final int ANIM_TOGGLE = 17;
    
    public static final int ANIM_TO_END = 1;
    
    public static final int ANIM_TO_START = 16;
    
    public static final int JUMP_TO_END = 256;
    
    public static final int JUMP_TO_START = 4096;
    
    int mMode = 17;
    
    int mTargetId = -1;
    
    private final MotionScene.Transition mTransition;
    
    public TransitionOnClick(Context param1Context, MotionScene.Transition param1Transition, XmlPullParser param1XmlPullParser) {
      this.mTransition = param1Transition;
      TypedArray typedArray = param1Context.obtainStyledAttributes(Xml.asAttributeSet(param1XmlPullParser), R.styleable.OnClick);
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        if (j == R.styleable.OnClick_targetId) {
          this.mTargetId = typedArray.getResourceId(j, this.mTargetId);
        } else if (j == R.styleable.OnClick_clickAction) {
          this.mMode = typedArray.getInt(j, this.mMode);
        } 
      } 
      typedArray.recycle();
    }
    
    public TransitionOnClick(MotionScene.Transition param1Transition, int param1Int1, int param1Int2) {
      this.mTransition = param1Transition;
      this.mTargetId = param1Int1;
      this.mMode = param1Int2;
    }
    
    public void addOnClickListeners(MotionLayout param1MotionLayout, int param1Int, MotionScene.Transition param1Transition) {
      View view;
      byte b;
      boolean bool1;
      int i = this.mTargetId;
      if (i != -1)
        view = param1MotionLayout.findViewById(i); 
      if (view == null) {
        param1Int = this.mTargetId;
        Log.e("MotionScene", (new StringBuilder(37)).append("OnClick could not find id ").append(param1Int).toString());
        return;
      } 
      int j = param1Transition.mConstraintSetStart;
      int m = param1Transition.mConstraintSetEnd;
      if (j == -1) {
        view.setOnClickListener(this);
        return;
      } 
      int k = this.mMode;
      boolean bool3 = false;
      if ((k & 0x1) != 0 && param1Int == j) {
        i = 1;
      } else {
        i = 0;
      } 
      if ((k & 0x100) != 0 && param1Int == j) {
        b = 1;
      } else {
        b = 0;
      } 
      if ((k & 0x1) != 0 && param1Int == j) {
        j = 1;
      } else {
        j = 0;
      } 
      if ((k & 0x10) != 0 && param1Int == m) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      boolean bool2 = bool3;
      if ((k & 0x1000) != 0) {
        bool2 = bool3;
        if (param1Int == m)
          bool2 = true; 
      } 
      if ((i | b | j | bool1 | bool2) != 0)
        view.setOnClickListener(this); 
    }
    
    boolean isTransitionViable(MotionScene.Transition param1Transition, MotionLayout param1MotionLayout) {
      MotionScene.Transition transition = this.mTransition;
      boolean bool2 = true;
      boolean bool1 = true;
      if (transition == param1Transition)
        return true; 
      int i = transition.mConstraintSetEnd;
      int j = this.mTransition.mConstraintSetStart;
      if (j == -1) {
        if (param1MotionLayout.mCurrentState == i)
          bool1 = false; 
        return bool1;
      } 
      bool1 = bool2;
      if (param1MotionLayout.mCurrentState != j)
        if (param1MotionLayout.mCurrentState == i) {
          bool1 = bool2;
        } else {
          bool1 = false;
        }  
      return bool1;
    }
    
    public void onClick(View param1View) {
      boolean bool1;
      MotionLayout motionLayout = this.mTransition.mMotionScene.mMotionLayout;
      if (!motionLayout.isInteractionEnabled())
        return; 
      if (this.mTransition.mConstraintSetStart == -1) {
        bool1 = motionLayout.getCurrentState();
        if (bool1 == -1) {
          motionLayout.transitionToState(this.mTransition.mConstraintSetEnd);
          return;
        } 
        MotionScene.Transition transition1 = new MotionScene.Transition(this.mTransition.mMotionScene, this.mTransition);
        MotionScene.Transition.access$102(transition1, bool1);
        MotionScene.Transition.access$002(transition1, this.mTransition.mConstraintSetEnd);
        motionLayout.setTransition(transition1);
        motionLayout.transitionToEnd();
        return;
      } 
      MotionScene.Transition transition = this.mTransition.mMotionScene.mCurrentTransition;
      int i = this.mMode;
      boolean bool3 = false;
      if ((i & 0x1) != 0 || (i & 0x100) != 0) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if ((i & 0x10) != 0 || (i & 0x1000) != 0) {
        i = 1;
      } else {
        i = 0;
      } 
      boolean bool2 = bool3;
      if (bool1) {
        bool2 = bool3;
        if (i != 0)
          bool2 = true; 
      } 
      int j = i;
      bool3 = bool1;
      if (bool2) {
        MotionScene.Transition transition2 = this.mTransition.mMotionScene.mCurrentTransition;
        MotionScene.Transition transition1 = this.mTransition;
        if (transition2 != transition1)
          motionLayout.setTransition(transition1); 
        if (motionLayout.getCurrentState() == motionLayout.getEndState() || motionLayout.getProgress() > 0.5F) {
          bool3 = false;
          j = i;
        } else {
          j = 0;
          bool3 = bool1;
        } 
      } 
      if (isTransitionViable(transition, motionLayout))
        if (bool3 && (0x1 & this.mMode) != 0) {
          motionLayout.setTransition(this.mTransition);
          motionLayout.transitionToEnd();
        } else if (j != 0 && (this.mMode & 0x10) != 0) {
          motionLayout.setTransition(this.mTransition);
          motionLayout.transitionToStart();
        } else if (bool3 && (this.mMode & 0x100) != 0) {
          motionLayout.setTransition(this.mTransition);
          motionLayout.setProgress(1.0F);
        } else if (j != 0 && (this.mMode & 0x1000) != 0) {
          motionLayout.setTransition(this.mTransition);
          motionLayout.setProgress(0.0F);
        }  
    }
    
    public void removeOnClickListeners(MotionLayout param1MotionLayout) {
      int i = this.mTargetId;
      if (i == -1)
        return; 
      View view = param1MotionLayout.findViewById(i);
      if (view == null) {
        i = this.mTargetId;
        Log.e("MotionScene", (new StringBuilder(35)).append(" (*)  could not find id ").append(i).toString());
        return;
      } 
      view.setOnClickListener(null);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\motion\widget\MotionScene.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */