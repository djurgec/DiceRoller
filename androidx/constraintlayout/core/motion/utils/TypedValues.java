package androidx.constraintlayout.core.motion.utils;

public interface TypedValues {
  public static final int BOOLEAN_MASK = 1;
  
  public static final int FLOAT_MASK = 4;
  
  public static final int INT_MASK = 2;
  
  public static final int STRING_MASK = 8;
  
  public static final String S_CUSTOM = "CUSTOM";
  
  public static final int TYPE_FRAME_POSITION = 100;
  
  public static final int TYPE_TARGET = 101;
  
  int getId(String paramString);
  
  boolean setValue(int paramInt, float paramFloat);
  
  boolean setValue(int paramInt1, int paramInt2);
  
  boolean setValue(int paramInt, String paramString);
  
  boolean setValue(int paramInt, boolean paramBoolean);
  
  public static interface Attributes {
    public static final String[] KEY_WORDS = new String[] { 
        "curveFit", "visibility", "alpha", "translationX", "translationY", "translationZ", "elevation", "rotationX", "rotationY", "rotationZ", 
        "scaleX", "scaleY", "pivotX", "pivotY", "progress", "pathRotate", "easing", "CUSTOM", "frame", "target", 
        "pivotTarget" };
    
    public static final String NAME = "KeyAttributes";
    
    public static final String S_ALPHA = "alpha";
    
    public static final String S_CURVE_FIT = "curveFit";
    
    public static final String S_CUSTOM = "CUSTOM";
    
    public static final String S_EASING = "easing";
    
    public static final String S_ELEVATION = "elevation";
    
    public static final String S_FRAME = "frame";
    
    public static final String S_PATH_ROTATE = "pathRotate";
    
    public static final String S_PIVOT_TARGET = "pivotTarget";
    
    public static final String S_PIVOT_X = "pivotX";
    
    public static final String S_PIVOT_Y = "pivotY";
    
    public static final String S_PROGRESS = "progress";
    
    public static final String S_ROTATION_X = "rotationX";
    
    public static final String S_ROTATION_Y = "rotationY";
    
    public static final String S_ROTATION_Z = "rotationZ";
    
    public static final String S_SCALE_X = "scaleX";
    
    public static final String S_SCALE_Y = "scaleY";
    
    public static final String S_TARGET = "target";
    
    public static final String S_TRANSLATION_X = "translationX";
    
    public static final String S_TRANSLATION_Y = "translationY";
    
    public static final String S_TRANSLATION_Z = "translationZ";
    
    public static final String S_VISIBILITY = "visibility";
    
    public static final int TYPE_ALPHA = 303;
    
    public static final int TYPE_CURVE_FIT = 301;
    
    public static final int TYPE_EASING = 317;
    
    public static final int TYPE_ELEVATION = 307;
    
    public static final int TYPE_PATH_ROTATE = 316;
    
    public static final int TYPE_PIVOT_TARGET = 318;
    
    public static final int TYPE_PIVOT_X = 313;
    
    public static final int TYPE_PIVOT_Y = 314;
    
    public static final int TYPE_PROGRESS = 315;
    
    public static final int TYPE_ROTATION_X = 308;
    
    public static final int TYPE_ROTATION_Y = 309;
    
    public static final int TYPE_ROTATION_Z = 310;
    
    public static final int TYPE_SCALE_X = 311;
    
    public static final int TYPE_SCALE_Y = 312;
    
    public static final int TYPE_TRANSLATION_X = 304;
    
    public static final int TYPE_TRANSLATION_Y = 305;
    
    public static final int TYPE_TRANSLATION_Z = 306;
    
    public static final int TYPE_VISIBILITY = 302;
    
    static int getId(String param1String) {
      byte b;
      switch (param1String.hashCode()) {
        default:
          b = -1;
          break;
        case 1941332754:
          if (param1String.equals("visibility")) {
            b = 1;
            break;
          } 
        case 1167159411:
          if (param1String.equals("pivotTarget")) {
            b = 19;
            break;
          } 
        case 803192288:
          if (param1String.equals("pathRotate")) {
            b = 15;
            break;
          } 
        case 579057826:
          if (param1String.equals("curveFit")) {
            b = 0;
            break;
          } 
        case 97692013:
          if (param1String.equals("frame")) {
            b = 17;
            break;
          } 
        case 92909918:
          if (param1String.equals("alpha")) {
            b = 2;
            break;
          } 
        case -4379043:
          if (param1String.equals("elevation")) {
            b = 6;
            break;
          } 
        case -880905839:
          if (param1String.equals("target")) {
            b = 18;
            break;
          } 
        case -908189617:
          if (param1String.equals("scaleY")) {
            b = 11;
            break;
          } 
        case -908189618:
          if (param1String.equals("scaleX")) {
            b = 10;
            break;
          } 
        case -987906985:
          if (param1String.equals("pivotY")) {
            b = 13;
            break;
          } 
        case -987906986:
          if (param1String.equals("pivotX")) {
            b = 12;
            break;
          } 
        case -1001078227:
          if (param1String.equals("progress")) {
            b = 14;
            break;
          } 
        case -1225497655:
          if (param1String.equals("translationZ")) {
            b = 5;
            break;
          } 
        case -1225497656:
          if (param1String.equals("translationY")) {
            b = 4;
            break;
          } 
        case -1225497657:
          if (param1String.equals("translationX")) {
            b = 3;
            break;
          } 
        case -1249320804:
          if (param1String.equals("rotationZ")) {
            b = 9;
            break;
          } 
        case -1249320805:
          if (param1String.equals("rotationY")) {
            b = 8;
            break;
          } 
        case -1249320806:
          if (param1String.equals("rotationX")) {
            b = 7;
            break;
          } 
        case -1310311125:
          if (param1String.equals("easing")) {
            b = 16;
            break;
          } 
      } 
      switch (b) {
        default:
          return -1;
        case 19:
          return 318;
        case 18:
          return 101;
        case 17:
          return 100;
        case 16:
          return 317;
        case 15:
          return 316;
        case 14:
          return 315;
        case 13:
          return 314;
        case 12:
          return 313;
        case 11:
          return 312;
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
    
    static int getType(int param1Int) {
      switch (param1Int) {
        default:
          return -1;
        case 303:
        case 304:
        case 305:
        case 306:
        case 307:
        case 308:
        case 309:
        case 310:
        case 311:
        case 312:
        case 313:
        case 314:
        case 315:
        case 316:
          return 4;
        case 101:
        case 317:
        case 318:
          return 8;
        case 100:
        case 301:
        case 302:
          break;
      } 
      return 2;
    }
  }
  
  public static interface Custom {
    public static final String[] KEY_WORDS = new String[] { "float", "color", "string", "boolean", "dimension", "refrence" };
    
    public static final String NAME = "Custom";
    
    public static final String S_BOOLEAN = "boolean";
    
    public static final String S_COLOR = "color";
    
    public static final String S_DIMENSION = "dimension";
    
    public static final String S_FLOAT = "float";
    
    public static final String S_INT = "integer";
    
    public static final String S_REFERENCE = "refrence";
    
    public static final String S_STRING = "string";
    
    public static final int TYPE_BOOLEAN = 904;
    
    public static final int TYPE_COLOR = 902;
    
    public static final int TYPE_DIMENSION = 905;
    
    public static final int TYPE_FLOAT = 901;
    
    public static final int TYPE_INT = 900;
    
    public static final int TYPE_REFERENCE = 906;
    
    public static final int TYPE_STRING = 903;
    
    static int getId(String param1String) {
      byte b;
      switch (param1String.hashCode()) {
        default:
          b = -1;
          break;
        case 1958052158:
          if (param1String.equals("integer")) {
            b = 0;
            break;
          } 
        case 97526364:
          if (param1String.equals("float")) {
            b = 1;
            break;
          } 
        case 94842723:
          if (param1String.equals("color")) {
            b = 2;
            break;
          } 
        case 64711720:
          if (param1String.equals("boolean")) {
            b = 4;
            break;
          } 
        case -710953590:
          if (param1String.equals("refrence")) {
            b = 6;
            break;
          } 
        case -891985903:
          if (param1String.equals("string")) {
            b = 3;
            break;
          } 
        case -1095013018:
          if (param1String.equals("dimension")) {
            b = 5;
            break;
          } 
      } 
      switch (b) {
        default:
          return -1;
        case 6:
          return 906;
        case 5:
          return 905;
        case 4:
          return 904;
        case 3:
          return 903;
        case 2:
          return 902;
        case 1:
          return 901;
        case 0:
          break;
      } 
      return 900;
    }
  }
  
  public static interface Cycle {
    public static final String[] KEY_WORDS = new String[] { 
        "curveFit", "visibility", "alpha", "translationX", "translationY", "translationZ", "elevation", "rotationX", "rotationY", "rotationZ", 
        "scaleX", "scaleY", "pivotX", "pivotY", "progress", "pathRotate", "easing", "waveShape", "customWave", "period", 
        "offset", "phase" };
    
    public static final String NAME = "KeyCycle";
    
    public static final String S_ALPHA = "alpha";
    
    public static final String S_CURVE_FIT = "curveFit";
    
    public static final String S_CUSTOM_WAVE_SHAPE = "customWave";
    
    public static final String S_EASING = "easing";
    
    public static final String S_ELEVATION = "elevation";
    
    public static final String S_PATH_ROTATE = "pathRotate";
    
    public static final String S_PIVOT_X = "pivotX";
    
    public static final String S_PIVOT_Y = "pivotY";
    
    public static final String S_PROGRESS = "progress";
    
    public static final String S_ROTATION_X = "rotationX";
    
    public static final String S_ROTATION_Y = "rotationY";
    
    public static final String S_ROTATION_Z = "rotationZ";
    
    public static final String S_SCALE_X = "scaleX";
    
    public static final String S_SCALE_Y = "scaleY";
    
    public static final String S_TRANSLATION_X = "translationX";
    
    public static final String S_TRANSLATION_Y = "translationY";
    
    public static final String S_TRANSLATION_Z = "translationZ";
    
    public static final String S_VISIBILITY = "visibility";
    
    public static final String S_WAVE_OFFSET = "offset";
    
    public static final String S_WAVE_PERIOD = "period";
    
    public static final String S_WAVE_PHASE = "phase";
    
    public static final String S_WAVE_SHAPE = "waveShape";
    
    public static final int TYPE_ALPHA = 403;
    
    public static final int TYPE_CURVE_FIT = 401;
    
    public static final int TYPE_CUSTOM_WAVE_SHAPE = 422;
    
    public static final int TYPE_EASING = 420;
    
    public static final int TYPE_ELEVATION = 307;
    
    public static final int TYPE_PATH_ROTATE = 416;
    
    public static final int TYPE_PIVOT_X = 313;
    
    public static final int TYPE_PIVOT_Y = 314;
    
    public static final int TYPE_PROGRESS = 315;
    
    public static final int TYPE_ROTATION_X = 308;
    
    public static final int TYPE_ROTATION_Y = 309;
    
    public static final int TYPE_ROTATION_Z = 310;
    
    public static final int TYPE_SCALE_X = 311;
    
    public static final int TYPE_SCALE_Y = 312;
    
    public static final int TYPE_TRANSLATION_X = 304;
    
    public static final int TYPE_TRANSLATION_Y = 305;
    
    public static final int TYPE_TRANSLATION_Z = 306;
    
    public static final int TYPE_VISIBILITY = 402;
    
    public static final int TYPE_WAVE_OFFSET = 424;
    
    public static final int TYPE_WAVE_PERIOD = 423;
    
    public static final int TYPE_WAVE_PHASE = 425;
    
    public static final int TYPE_WAVE_SHAPE = 421;
    
    static int getId(String param1String) {
      byte b;
      switch (param1String.hashCode()) {
        default:
          b = -1;
          break;
        case 1941332754:
          if (param1String.equals("visibility")) {
            b = 1;
            break;
          } 
        case 803192288:
          if (param1String.equals("pathRotate")) {
            b = 14;
            break;
          } 
        case 579057826:
          if (param1String.equals("curveFit")) {
            b = 0;
            break;
          } 
        case 92909918:
          if (param1String.equals("alpha")) {
            b = 2;
            break;
          } 
        case -908189617:
          if (param1String.equals("scaleY")) {
            b = 10;
            break;
          } 
        case -908189618:
          if (param1String.equals("scaleX")) {
            b = 9;
            break;
          } 
        case -987906985:
          if (param1String.equals("pivotY")) {
            b = 12;
            break;
          } 
        case -987906986:
          if (param1String.equals("pivotX")) {
            b = 11;
            break;
          } 
        case -1001078227:
          if (param1String.equals("progress")) {
            b = 13;
            break;
          } 
        case -1225497655:
          if (param1String.equals("translationZ")) {
            b = 5;
            break;
          } 
        case -1225497656:
          if (param1String.equals("translationY")) {
            b = 4;
            break;
          } 
        case -1225497657:
          if (param1String.equals("translationX")) {
            b = 3;
            break;
          } 
        case -1249320804:
          if (param1String.equals("rotationZ")) {
            b = 8;
            break;
          } 
        case -1249320805:
          if (param1String.equals("rotationY")) {
            b = 7;
            break;
          } 
        case -1249320806:
          if (param1String.equals("rotationX")) {
            b = 6;
            break;
          } 
        case -1310311125:
          if (param1String.equals("easing")) {
            b = 15;
            break;
          } 
      } 
      switch (b) {
        default:
          return -1;
        case 15:
          return 420;
        case 14:
          return 416;
        case 13:
          return 315;
        case 12:
          return 314;
        case 11:
          return 313;
        case 10:
          return 312;
        case 9:
          return 311;
        case 8:
          return 310;
        case 7:
          return 309;
        case 6:
          return 308;
        case 5:
          return 306;
        case 4:
          return 305;
        case 3:
          return 304;
        case 2:
          return 403;
        case 1:
          return 402;
        case 0:
          break;
      } 
      return 401;
    }
    
    static int getType(int param1Int) {
      switch (param1Int) {
        default:
          return -1;
        case 304:
        case 305:
        case 306:
        case 307:
        case 308:
        case 309:
        case 310:
        case 311:
        case 312:
        case 313:
        case 314:
        case 315:
        case 403:
        case 416:
        case 423:
        case 424:
        case 425:
          return 4;
        case 101:
        case 420:
        case 421:
          return 8;
        case 100:
        case 401:
        case 402:
          break;
      } 
      return 2;
    }
  }
  
  public static interface Motion {
    public static final String[] KEY_WORDS = new String[] { 
        "Stagger", "PathRotate", "QuantizeMotionPhase", "TransitionEasing", "QuantizeInterpolator", "AnimateRelativeTo", "AnimateCircleAngleTo", "PathMotionArc", "DrawPath", "PolarRelativeTo", 
        "QuantizeMotionSteps", "QuantizeInterpolatorType", "QuantizeInterpolatorID" };
    
    public static final String NAME = "Motion";
    
    public static final String S_ANIMATE_CIRCLEANGLE_TO = "AnimateCircleAngleTo";
    
    public static final String S_ANIMATE_RELATIVE_TO = "AnimateRelativeTo";
    
    public static final String S_DRAW_PATH = "DrawPath";
    
    public static final String S_EASING = "TransitionEasing";
    
    public static final String S_PATHMOTION_ARC = "PathMotionArc";
    
    public static final String S_PATH_ROTATE = "PathRotate";
    
    public static final String S_POLAR_RELATIVETO = "PolarRelativeTo";
    
    public static final String S_QUANTIZE_INTERPOLATOR = "QuantizeInterpolator";
    
    public static final String S_QUANTIZE_INTERPOLATOR_ID = "QuantizeInterpolatorID";
    
    public static final String S_QUANTIZE_INTERPOLATOR_TYPE = "QuantizeInterpolatorType";
    
    public static final String S_QUANTIZE_MOTIONSTEPS = "QuantizeMotionSteps";
    
    public static final String S_QUANTIZE_MOTION_PHASE = "QuantizeMotionPhase";
    
    public static final String S_STAGGER = "Stagger";
    
    public static final int TYPE_ANIMATE_CIRCLEANGLE_TO = 606;
    
    public static final int TYPE_ANIMATE_RELATIVE_TO = 605;
    
    public static final int TYPE_DRAW_PATH = 608;
    
    public static final int TYPE_EASING = 603;
    
    public static final int TYPE_PATHMOTION_ARC = 607;
    
    public static final int TYPE_PATH_ROTATE = 601;
    
    public static final int TYPE_POLAR_RELATIVETO = 609;
    
    public static final int TYPE_QUANTIZE_INTERPOLATOR = 604;
    
    public static final int TYPE_QUANTIZE_INTERPOLATOR_ID = 612;
    
    public static final int TYPE_QUANTIZE_INTERPOLATOR_TYPE = 611;
    
    public static final int TYPE_QUANTIZE_MOTIONSTEPS = 610;
    
    public static final int TYPE_QUANTIZE_MOTION_PHASE = 602;
    
    public static final int TYPE_STAGGER = 600;
    
    static int getId(String param1String) {
      byte b;
      switch (param1String.hashCode()) {
        default:
          b = -1;
          break;
        case 2109694967:
          if (param1String.equals("PathMotionArc")) {
            b = 7;
            break;
          } 
        case 1900899336:
          if (param1String.equals("AnimateRelativeTo")) {
            b = 5;
            break;
          } 
        case 1639368448:
          if (param1String.equals("TransitionEasing")) {
            b = 3;
            break;
          } 
        case 1583722451:
          if (param1String.equals("QuantizeInterpolatorID")) {
            b = 12;
            break;
          } 
        case 1539234834:
          if (param1String.equals("QuantizeInterpolatorType")) {
            b = 11;
            break;
          } 
        case 1138491429:
          if (param1String.equals("PolarRelativeTo")) {
            b = 9;
            break;
          } 
        case -232872051:
          if (param1String.equals("Stagger")) {
            b = 0;
            break;
          } 
        case -762370135:
          if (param1String.equals("DrawPath")) {
            b = 8;
            break;
          } 
        case -1030753096:
          if (param1String.equals("QuantizeInterpolator")) {
            b = 4;
            break;
          } 
        case -1498310144:
          if (param1String.equals("PathRotate")) {
            b = 1;
            break;
          } 
        case -1529145600:
          if (param1String.equals("QuantizeMotionSteps")) {
            b = 10;
            break;
          } 
        case -1532277420:
          if (param1String.equals("QuantizeMotionPhase")) {
            b = 2;
            break;
          } 
        case -2033446275:
          if (param1String.equals("AnimateCircleAngleTo")) {
            b = 6;
            break;
          } 
      } 
      switch (b) {
        default:
          return -1;
        case 12:
          return 612;
        case 11:
          return 611;
        case 10:
          return 610;
        case 9:
          return 609;
        case 8:
          return 608;
        case 7:
          return 607;
        case 6:
          return 606;
        case 5:
          return 605;
        case 4:
          return 604;
        case 3:
          return 603;
        case 2:
          return 602;
        case 1:
          return 601;
        case 0:
          break;
      } 
      return 600;
    }
  }
  
  public static interface MotionScene {
    public static final String[] KEY_WORDS = new String[] { "defaultDuration", "layoutDuringTransition" };
    
    public static final String NAME = "MotionScene";
    
    public static final String S_DEFAULT_DURATION = "defaultDuration";
    
    public static final String S_LAYOUT_DURING_TRANSITION = "layoutDuringTransition";
    
    public static final int TYPE_DEFAULT_DURATION = 600;
    
    public static final int TYPE_LAYOUT_DURING_TRANSITION = 601;
    
    static int getId(String param1String) {
      byte b;
      switch (param1String.hashCode()) {
        default:
          b = -1;
          break;
        case 1028758976:
          if (param1String.equals("layoutDuringTransition")) {
            b = 1;
            break;
          } 
        case 6076149:
          if (param1String.equals("defaultDuration")) {
            b = 0;
            break;
          } 
      } 
      switch (b) {
        default:
          return -1;
        case 1:
          return 601;
        case 0:
          break;
      } 
      return 600;
    }
    
    static int getType(int param1Int) {
      switch (param1Int) {
        default:
          return -1;
        case 601:
          return 1;
        case 600:
          break;
      } 
      return 2;
    }
  }
  
  public static interface OnSwipe {
    public static final String AUTOCOMPLETE_MODE = "autocompletemode";
    
    public static final String[] AUTOCOMPLETE_MODE_ENUM = new String[] { "continuousVelocity", "spring" };
    
    public static final String DRAG_DIRECTION = "dragdirection";
    
    public static final String DRAG_SCALE = "dragscale";
    
    public static final String DRAG_THRESHOLD = "dragthreshold";
    
    public static final String LIMIT_BOUNDS_TO = "limitboundsto";
    
    public static final String MAX_ACCELERATION = "maxacceleration";
    
    public static final String MAX_VELOCITY = "maxvelocity";
    
    public static final String MOVE_WHEN_SCROLLAT_TOP = "movewhenscrollattop";
    
    public static final String NESTED_SCROLL_FLAGS = "nestedscrollflags";
    
    public static final String[] NESTED_SCROLL_FLAGS_ENUM = new String[] { "none", "disablePostScroll", "disableScroll", "supportScrollUp" };
    
    public static final String ON_TOUCH_UP = "ontouchup";
    
    public static final String[] ON_TOUCH_UP_ENUM = new String[] { "autoComplete", "autoCompleteToStart", "autoCompleteToEnd", "stop", "decelerate", "decelerateAndComplete", "neverCompleteToStart", "neverCompleteToEnd" };
    
    public static final String ROTATION_CENTER_ID = "rotationcenterid";
    
    public static final String SPRINGS_TOP_THRESHOLD = "springstopthreshold";
    
    public static final String SPRING_BOUNDARY = "springboundary";
    
    public static final String[] SPRING_BOUNDARY_ENUM = new String[] { "overshoot", "bounceStart", "bounceEnd", "bounceBoth" };
    
    public static final String SPRING_DAMPING = "springdamping";
    
    public static final String SPRING_MASS = "springmass";
    
    public static final String SPRING_STIFFNESS = "springstiffness";
    
    public static final String TOUCH_ANCHOR_ID = "touchanchorid";
    
    public static final String TOUCH_ANCHOR_SIDE = "touchanchorside";
    
    public static final String TOUCH_REGION_ID = "touchregionid";
    
    static {
    
    }
  }
  
  public static interface Position {
    public static final String[] KEY_WORDS = new String[] { "transitionEasing", "drawPath", "percentWidth", "percentHeight", "sizePercent", "percentX", "percentY" };
    
    public static final String NAME = "KeyPosition";
    
    public static final String S_DRAWPATH = "drawPath";
    
    public static final String S_PERCENT_HEIGHT = "percentHeight";
    
    public static final String S_PERCENT_WIDTH = "percentWidth";
    
    public static final String S_PERCENT_X = "percentX";
    
    public static final String S_PERCENT_Y = "percentY";
    
    public static final String S_SIZE_PERCENT = "sizePercent";
    
    public static final String S_TRANSITION_EASING = "transitionEasing";
    
    public static final int TYPE_CURVE_FIT = 508;
    
    public static final int TYPE_DRAWPATH = 502;
    
    public static final int TYPE_PATH_MOTION_ARC = 509;
    
    public static final int TYPE_PERCENT_HEIGHT = 504;
    
    public static final int TYPE_PERCENT_WIDTH = 503;
    
    public static final int TYPE_PERCENT_X = 506;
    
    public static final int TYPE_PERCENT_Y = 507;
    
    public static final int TYPE_POSITION_TYPE = 510;
    
    public static final int TYPE_SIZE_PERCENT = 505;
    
    public static final int TYPE_TRANSITION_EASING = 501;
    
    static int getId(String param1String) {
      byte b;
      switch (param1String.hashCode()) {
        default:
          b = -1;
          break;
        case 428090548:
          if (param1String.equals("percentY")) {
            b = 6;
            break;
          } 
        case 428090547:
          if (param1String.equals("percentX")) {
            b = 5;
            break;
          } 
        case -200259324:
          if (param1String.equals("sizePercent")) {
            b = 4;
            break;
          } 
        case -827014263:
          if (param1String.equals("drawPath")) {
            b = 1;
            break;
          } 
        case -1017587252:
          if (param1String.equals("percentHeight")) {
            b = 3;
            break;
          } 
        case -1127236479:
          if (param1String.equals("percentWidth")) {
            b = 2;
            break;
          } 
        case -1812823328:
          if (param1String.equals("transitionEasing")) {
            b = 0;
            break;
          } 
      } 
      switch (b) {
        default:
          return -1;
        case 6:
          return 507;
        case 5:
          return 506;
        case 4:
          return 505;
        case 3:
          return 504;
        case 2:
          return 503;
        case 1:
          return 502;
        case 0:
          break;
      } 
      return 501;
    }
    
    static int getType(int param1Int) {
      switch (param1Int) {
        default:
          return -1;
        case 503:
        case 504:
        case 505:
        case 506:
        case 507:
          return 4;
        case 101:
        case 501:
        case 502:
          return 8;
        case 100:
        case 508:
          break;
      } 
      return 2;
    }
  }
  
  public static interface Transition {
    public static final String[] KEY_WORDS = new String[] { "duration", "from", "to", "pathMotionArc", "autoTransition", "motionInterpolator", "staggered", "from", "transitionFlags" };
    
    public static final String NAME = "Transitions";
    
    public static final String S_AUTO_TRANSITION = "autoTransition";
    
    public static final String S_DURATION = "duration";
    
    public static final String S_FROM = "from";
    
    public static final String S_INTERPOLATOR = "motionInterpolator";
    
    public static final String S_PATH_MOTION_ARC = "pathMotionArc";
    
    public static final String S_STAGGERED = "staggered";
    
    public static final String S_TO = "to";
    
    public static final String S_TRANSITION_FLAGS = "transitionFlags";
    
    public static final int TYPE_AUTO_TRANSITION = 704;
    
    public static final int TYPE_DURATION = 700;
    
    public static final int TYPE_FROM = 701;
    
    public static final int TYPE_INTERPOLATOR = 705;
    
    public static final int TYPE_PATH_MOTION_ARC = 509;
    
    public static final int TYPE_STAGGERED = 706;
    
    public static final int TYPE_TO = 702;
    
    public static final int TYPE_TRANSITION_FLAGS = 707;
    
    static int getId(String param1String) {
      byte b;
      switch (param1String.hashCode()) {
        default:
          b = -1;
          break;
        case 1839260940:
          if (param1String.equals("staggered")) {
            b = 6;
            break;
          } 
        case 1310733335:
          if (param1String.equals("pathMotionArc")) {
            b = 3;
            break;
          } 
        case 3151786:
          if (param1String.equals("from")) {
            b = 1;
            break;
          } 
        case 3707:
          if (param1String.equals("to")) {
            b = 2;
            break;
          } 
        case -1298065308:
          if (param1String.equals("autoTransition")) {
            b = 4;
            break;
          } 
        case -1357874275:
          if (param1String.equals("motionInterpolator")) {
            b = 5;
            break;
          } 
        case -1992012396:
          if (param1String.equals("duration")) {
            b = 0;
            break;
          } 
        case -1996906958:
          if (param1String.equals("transitionFlags")) {
            b = 7;
            break;
          } 
      } 
      switch (b) {
        default:
          return -1;
        case 7:
          return 707;
        case 6:
          return 706;
        case 5:
          return 705;
        case 4:
          return 704;
        case 3:
          return 509;
        case 2:
          return 702;
        case 1:
          return 701;
        case 0:
          break;
      } 
      return 700;
    }
    
    static int getType(int param1Int) {
      switch (param1Int) {
        default:
          return -1;
        case 706:
          return 4;
        case 701:
        case 702:
        case 705:
        case 707:
          return 8;
        case 509:
        case 700:
          break;
      } 
      return 2;
    }
  }
  
  public static interface Trigger {
    public static final String CROSS = "CROSS";
    
    public static final String[] KEY_WORDS = new String[] { 
        "viewTransitionOnCross", "viewTransitionOnPositiveCross", "viewTransitionOnNegativeCross", "postLayout", "triggerSlack", "triggerCollisionView", "triggerCollisionId", "triggerID", "positiveCross", "negativeCross", 
        "triggerReceiver", "CROSS" };
    
    public static final String NAME = "KeyTrigger";
    
    public static final String NEGATIVE_CROSS = "negativeCross";
    
    public static final String POSITIVE_CROSS = "positiveCross";
    
    public static final String POST_LAYOUT = "postLayout";
    
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
    
    static int getId(String param1String) {
      byte b;
      switch (param1String.hashCode()) {
        default:
          b = -1;
          break;
        case 1535404999:
          if (param1String.equals("triggerReceiver")) {
            b = 10;
            break;
          } 
        case 1401391082:
          if (param1String.equals("postLayout")) {
            b = 3;
            break;
          } 
        case 1301930599:
          if (param1String.equals("viewTransitionOnCross")) {
            b = 0;
            break;
          } 
        case 364489912:
          if (param1String.equals("triggerSlack")) {
            b = 4;
            break;
          } 
        case 64397344:
          if (param1String.equals("CROSS")) {
            b = 11;
            break;
          } 
        case -9754574:
          if (param1String.equals("viewTransitionOnNegativeCross")) {
            b = 2;
            break;
          } 
        case -76025313:
          if (param1String.equals("triggerCollisionView")) {
            b = 5;
            break;
          } 
        case -638126837:
          if (param1String.equals("negativeCross")) {
            b = 9;
            break;
          } 
        case -648752941:
          if (param1String.equals("triggerID")) {
            b = 7;
            break;
          } 
        case -786670827:
          if (param1String.equals("triggerCollisionId")) {
            b = 6;
            break;
          } 
        case -966421266:
          if (param1String.equals("viewTransitionOnPositiveCross")) {
            b = 1;
            break;
          } 
        case -1594793529:
          if (param1String.equals("positiveCross")) {
            b = 8;
            break;
          } 
      } 
      switch (b) {
        default:
          return -1;
        case 11:
          return 312;
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
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motio\\utils\TypedValues.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */