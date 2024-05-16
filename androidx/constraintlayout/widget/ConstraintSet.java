package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.core.motion.utils.Easing;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.HelperWidget;
import androidx.constraintlayout.motion.widget.Debug;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.MotionScene;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ConstraintSet {
  private static final int ALPHA = 43;
  
  private static final int ANIMATE_CIRCLE_ANGLE_TO = 82;
  
  private static final int ANIMATE_RELATIVE_TO = 64;
  
  private static final int BARRIER_ALLOWS_GONE_WIDGETS = 75;
  
  private static final int BARRIER_DIRECTION = 72;
  
  private static final int BARRIER_MARGIN = 73;
  
  private static final int BARRIER_TYPE = 1;
  
  public static final int BASELINE = 5;
  
  private static final int BASELINE_MARGIN = 93;
  
  private static final int BASELINE_TO_BASELINE = 1;
  
  private static final int BASELINE_TO_BOTTOM = 92;
  
  private static final int BASELINE_TO_TOP = 91;
  
  public static final int BOTTOM = 4;
  
  private static final int BOTTOM_MARGIN = 2;
  
  private static final int BOTTOM_TO_BOTTOM = 3;
  
  private static final int BOTTOM_TO_TOP = 4;
  
  public static final int CHAIN_PACKED = 2;
  
  public static final int CHAIN_SPREAD = 0;
  
  public static final int CHAIN_SPREAD_INSIDE = 1;
  
  private static final int CHAIN_USE_RTL = 71;
  
  private static final int CIRCLE = 61;
  
  private static final int CIRCLE_ANGLE = 63;
  
  private static final int CIRCLE_RADIUS = 62;
  
  public static final int CIRCLE_REFERENCE = 8;
  
  private static final int CONSTRAINED_HEIGHT = 81;
  
  private static final int CONSTRAINED_WIDTH = 80;
  
  private static final int CONSTRAINT_REFERENCED_IDS = 74;
  
  private static final int CONSTRAINT_TAG = 77;
  
  private static final boolean DEBUG = false;
  
  private static final int DIMENSION_RATIO = 5;
  
  private static final int DRAW_PATH = 66;
  
  private static final int EDITOR_ABSOLUTE_X = 6;
  
  private static final int EDITOR_ABSOLUTE_Y = 7;
  
  private static final int ELEVATION = 44;
  
  public static final int END = 7;
  
  private static final int END_MARGIN = 8;
  
  private static final int END_TO_END = 9;
  
  private static final int END_TO_START = 10;
  
  private static final String ERROR_MESSAGE = "XML parser error must be within a Constraint ";
  
  public static final int GONE = 8;
  
  private static final int GONE_BASELINE_MARGIN = 94;
  
  private static final int GONE_BOTTOM_MARGIN = 11;
  
  private static final int GONE_END_MARGIN = 12;
  
  private static final int GONE_LEFT_MARGIN = 13;
  
  private static final int GONE_RIGHT_MARGIN = 14;
  
  private static final int GONE_START_MARGIN = 15;
  
  private static final int GONE_TOP_MARGIN = 16;
  
  private static final int GUIDE_BEGIN = 17;
  
  private static final int GUIDE_END = 18;
  
  private static final int GUIDE_PERCENT = 19;
  
  private static final int HEIGHT_DEFAULT = 55;
  
  private static final int HEIGHT_MAX = 57;
  
  private static final int HEIGHT_MIN = 59;
  
  private static final int HEIGHT_PERCENT = 70;
  
  public static final int HORIZONTAL = 0;
  
  private static final int HORIZONTAL_BIAS = 20;
  
  public static final int HORIZONTAL_GUIDELINE = 0;
  
  private static final int HORIZONTAL_STYLE = 41;
  
  private static final int HORIZONTAL_WEIGHT = 39;
  
  private static final int INTERNAL_MATCH_CONSTRAINT = -3;
  
  private static final int INTERNAL_MATCH_PARENT = -1;
  
  private static final int INTERNAL_WRAP_CONTENT = -2;
  
  private static final int INTERNAL_WRAP_CONTENT_CONSTRAINED = -4;
  
  public static final int INVISIBLE = 4;
  
  private static final String KEY_PERCENT_PARENT = "parent";
  
  private static final String KEY_RATIO = "ratio";
  
  private static final String KEY_WEIGHT = "weight";
  
  private static final int LAYOUT_CONSTRAINT_HEIGHT = 96;
  
  private static final int LAYOUT_CONSTRAINT_WIDTH = 95;
  
  private static final int LAYOUT_HEIGHT = 21;
  
  private static final int LAYOUT_VISIBILITY = 22;
  
  private static final int LAYOUT_WIDTH = 23;
  
  private static final int LAYOUT_WRAP_BEHAVIOR = 97;
  
  public static final int LEFT = 1;
  
  private static final int LEFT_MARGIN = 24;
  
  private static final int LEFT_TO_LEFT = 25;
  
  private static final int LEFT_TO_RIGHT = 26;
  
  public static final int MATCH_CONSTRAINT = 0;
  
  public static final int MATCH_CONSTRAINT_PERCENT = 2;
  
  public static final int MATCH_CONSTRAINT_SPREAD = 0;
  
  public static final int MATCH_CONSTRAINT_WRAP = 1;
  
  private static final int MOTION_STAGGER = 79;
  
  private static final int MOTION_TARGET = 98;
  
  private static final int ORIENTATION = 27;
  
  public static final int PARENT_ID = 0;
  
  private static final int PATH_MOTION_ARC = 76;
  
  private static final int PROGRESS = 68;
  
  private static final int QUANTIZE_MOTION_INTERPOLATOR = 86;
  
  private static final int QUANTIZE_MOTION_INTERPOLATOR_ID = 89;
  
  private static final int QUANTIZE_MOTION_INTERPOLATOR_STR = 90;
  
  private static final int QUANTIZE_MOTION_INTERPOLATOR_TYPE = 88;
  
  private static final int QUANTIZE_MOTION_PHASE = 85;
  
  private static final int QUANTIZE_MOTION_STEPS = 84;
  
  public static final int RIGHT = 2;
  
  private static final int RIGHT_MARGIN = 28;
  
  private static final int RIGHT_TO_LEFT = 29;
  
  private static final int RIGHT_TO_RIGHT = 30;
  
  public static final int ROTATE_LEFT_OF_PORTRATE = 4;
  
  public static final int ROTATE_NONE = 0;
  
  public static final int ROTATE_PORTRATE_OF_LEFT = 2;
  
  public static final int ROTATE_PORTRATE_OF_RIGHT = 1;
  
  public static final int ROTATE_RIGHT_OF_PORTRATE = 3;
  
  private static final int ROTATION = 60;
  
  private static final int ROTATION_X = 45;
  
  private static final int ROTATION_Y = 46;
  
  private static final int SCALE_X = 47;
  
  private static final int SCALE_Y = 48;
  
  public static final int START = 6;
  
  private static final int START_MARGIN = 31;
  
  private static final int START_TO_END = 32;
  
  private static final int START_TO_START = 33;
  
  private static final String TAG = "ConstraintSet";
  
  public static final int TOP = 3;
  
  private static final int TOP_MARGIN = 34;
  
  private static final int TOP_TO_BOTTOM = 35;
  
  private static final int TOP_TO_TOP = 36;
  
  private static final int TRANSFORM_PIVOT_TARGET = 83;
  
  private static final int TRANSFORM_PIVOT_X = 49;
  
  private static final int TRANSFORM_PIVOT_Y = 50;
  
  private static final int TRANSITION_EASING = 65;
  
  private static final int TRANSITION_PATH_ROTATE = 67;
  
  private static final int TRANSLATION_X = 51;
  
  private static final int TRANSLATION_Y = 52;
  
  private static final int TRANSLATION_Z = 53;
  
  public static final int UNSET = -1;
  
  private static final int UNUSED = 87;
  
  public static final int VERTICAL = 1;
  
  private static final int VERTICAL_BIAS = 37;
  
  public static final int VERTICAL_GUIDELINE = 1;
  
  private static final int VERTICAL_STYLE = 42;
  
  private static final int VERTICAL_WEIGHT = 40;
  
  private static final int VIEW_ID = 38;
  
  private static final int[] VISIBILITY_FLAGS = new int[] { 0, 4, 8 };
  
  private static final int VISIBILITY_MODE = 78;
  
  public static final int VISIBILITY_MODE_IGNORE = 1;
  
  public static final int VISIBILITY_MODE_NORMAL = 0;
  
  public static final int VISIBLE = 0;
  
  private static final int WIDTH_DEFAULT = 54;
  
  private static final int WIDTH_MAX = 56;
  
  private static final int WIDTH_MIN = 58;
  
  private static final int WIDTH_PERCENT = 69;
  
  public static final int WRAP_CONTENT = -2;
  
  private static SparseIntArray mapToConstant = new SparseIntArray();
  
  private static SparseIntArray overrideMapToConstant = new SparseIntArray();
  
  public String derivedState = "";
  
  private HashMap<Integer, Constraint> mConstraints = new HashMap<>();
  
  private boolean mForceId = true;
  
  public String mIdString;
  
  public int mRotate = 0;
  
  private HashMap<String, ConstraintAttribute> mSavedAttributes = new HashMap<>();
  
  private boolean mValidate;
  
  static {
    mapToConstant.append(R.styleable.Constraint_layout_constraintLeft_toLeftOf, 25);
    mapToConstant.append(R.styleable.Constraint_layout_constraintLeft_toRightOf, 26);
    mapToConstant.append(R.styleable.Constraint_layout_constraintRight_toLeftOf, 29);
    mapToConstant.append(R.styleable.Constraint_layout_constraintRight_toRightOf, 30);
    mapToConstant.append(R.styleable.Constraint_layout_constraintTop_toTopOf, 36);
    mapToConstant.append(R.styleable.Constraint_layout_constraintTop_toBottomOf, 35);
    mapToConstant.append(R.styleable.Constraint_layout_constraintBottom_toTopOf, 4);
    mapToConstant.append(R.styleable.Constraint_layout_constraintBottom_toBottomOf, 3);
    mapToConstant.append(R.styleable.Constraint_layout_constraintBaseline_toBaselineOf, 1);
    mapToConstant.append(R.styleable.Constraint_layout_constraintBaseline_toTopOf, 91);
    mapToConstant.append(R.styleable.Constraint_layout_constraintBaseline_toBottomOf, 92);
    mapToConstant.append(R.styleable.Constraint_layout_editor_absoluteX, 6);
    mapToConstant.append(R.styleable.Constraint_layout_editor_absoluteY, 7);
    mapToConstant.append(R.styleable.Constraint_layout_constraintGuide_begin, 17);
    mapToConstant.append(R.styleable.Constraint_layout_constraintGuide_end, 18);
    mapToConstant.append(R.styleable.Constraint_layout_constraintGuide_percent, 19);
    mapToConstant.append(R.styleable.Constraint_android_orientation, 27);
    mapToConstant.append(R.styleable.Constraint_layout_constraintStart_toEndOf, 32);
    mapToConstant.append(R.styleable.Constraint_layout_constraintStart_toStartOf, 33);
    mapToConstant.append(R.styleable.Constraint_layout_constraintEnd_toStartOf, 10);
    mapToConstant.append(R.styleable.Constraint_layout_constraintEnd_toEndOf, 9);
    mapToConstant.append(R.styleable.Constraint_layout_goneMarginLeft, 13);
    mapToConstant.append(R.styleable.Constraint_layout_goneMarginTop, 16);
    mapToConstant.append(R.styleable.Constraint_layout_goneMarginRight, 14);
    mapToConstant.append(R.styleable.Constraint_layout_goneMarginBottom, 11);
    mapToConstant.append(R.styleable.Constraint_layout_goneMarginStart, 15);
    mapToConstant.append(R.styleable.Constraint_layout_goneMarginEnd, 12);
    mapToConstant.append(R.styleable.Constraint_layout_constraintVertical_weight, 40);
    mapToConstant.append(R.styleable.Constraint_layout_constraintHorizontal_weight, 39);
    mapToConstant.append(R.styleable.Constraint_layout_constraintHorizontal_chainStyle, 41);
    mapToConstant.append(R.styleable.Constraint_layout_constraintVertical_chainStyle, 42);
    mapToConstant.append(R.styleable.Constraint_layout_constraintHorizontal_bias, 20);
    mapToConstant.append(R.styleable.Constraint_layout_constraintVertical_bias, 37);
    mapToConstant.append(R.styleable.Constraint_layout_constraintDimensionRatio, 5);
    mapToConstant.append(R.styleable.Constraint_layout_constraintLeft_creator, 87);
    mapToConstant.append(R.styleable.Constraint_layout_constraintTop_creator, 87);
    mapToConstant.append(R.styleable.Constraint_layout_constraintRight_creator, 87);
    mapToConstant.append(R.styleable.Constraint_layout_constraintBottom_creator, 87);
    mapToConstant.append(R.styleable.Constraint_layout_constraintBaseline_creator, 87);
    mapToConstant.append(R.styleable.Constraint_android_layout_marginLeft, 24);
    mapToConstant.append(R.styleable.Constraint_android_layout_marginRight, 28);
    mapToConstant.append(R.styleable.Constraint_android_layout_marginStart, 31);
    mapToConstant.append(R.styleable.Constraint_android_layout_marginEnd, 8);
    mapToConstant.append(R.styleable.Constraint_android_layout_marginTop, 34);
    mapToConstant.append(R.styleable.Constraint_android_layout_marginBottom, 2);
    mapToConstant.append(R.styleable.Constraint_android_layout_width, 23);
    mapToConstant.append(R.styleable.Constraint_android_layout_height, 21);
    mapToConstant.append(R.styleable.Constraint_layout_constraintWidth, 95);
    mapToConstant.append(R.styleable.Constraint_layout_constraintHeight, 96);
    mapToConstant.append(R.styleable.Constraint_android_visibility, 22);
    mapToConstant.append(R.styleable.Constraint_android_alpha, 43);
    mapToConstant.append(R.styleable.Constraint_android_elevation, 44);
    mapToConstant.append(R.styleable.Constraint_android_rotationX, 45);
    mapToConstant.append(R.styleable.Constraint_android_rotationY, 46);
    mapToConstant.append(R.styleable.Constraint_android_rotation, 60);
    mapToConstant.append(R.styleable.Constraint_android_scaleX, 47);
    mapToConstant.append(R.styleable.Constraint_android_scaleY, 48);
    mapToConstant.append(R.styleable.Constraint_android_transformPivotX, 49);
    mapToConstant.append(R.styleable.Constraint_android_transformPivotY, 50);
    mapToConstant.append(R.styleable.Constraint_android_translationX, 51);
    mapToConstant.append(R.styleable.Constraint_android_translationY, 52);
    mapToConstant.append(R.styleable.Constraint_android_translationZ, 53);
    mapToConstant.append(R.styleable.Constraint_layout_constraintWidth_default, 54);
    mapToConstant.append(R.styleable.Constraint_layout_constraintHeight_default, 55);
    mapToConstant.append(R.styleable.Constraint_layout_constraintWidth_max, 56);
    mapToConstant.append(R.styleable.Constraint_layout_constraintHeight_max, 57);
    mapToConstant.append(R.styleable.Constraint_layout_constraintWidth_min, 58);
    mapToConstant.append(R.styleable.Constraint_layout_constraintHeight_min, 59);
    mapToConstant.append(R.styleable.Constraint_layout_constraintCircle, 61);
    mapToConstant.append(R.styleable.Constraint_layout_constraintCircleRadius, 62);
    mapToConstant.append(R.styleable.Constraint_layout_constraintCircleAngle, 63);
    mapToConstant.append(R.styleable.Constraint_animateRelativeTo, 64);
    mapToConstant.append(R.styleable.Constraint_transitionEasing, 65);
    mapToConstant.append(R.styleable.Constraint_drawPath, 66);
    mapToConstant.append(R.styleable.Constraint_transitionPathRotate, 67);
    mapToConstant.append(R.styleable.Constraint_motionStagger, 79);
    mapToConstant.append(R.styleable.Constraint_android_id, 38);
    mapToConstant.append(R.styleable.Constraint_motionProgress, 68);
    mapToConstant.append(R.styleable.Constraint_layout_constraintWidth_percent, 69);
    mapToConstant.append(R.styleable.Constraint_layout_constraintHeight_percent, 70);
    mapToConstant.append(R.styleable.Constraint_layout_wrapBehaviorInParent, 97);
    mapToConstant.append(R.styleable.Constraint_chainUseRtl, 71);
    mapToConstant.append(R.styleable.Constraint_barrierDirection, 72);
    mapToConstant.append(R.styleable.Constraint_barrierMargin, 73);
    mapToConstant.append(R.styleable.Constraint_constraint_referenced_ids, 74);
    mapToConstant.append(R.styleable.Constraint_barrierAllowsGoneWidgets, 75);
    mapToConstant.append(R.styleable.Constraint_pathMotionArc, 76);
    mapToConstant.append(R.styleable.Constraint_layout_constraintTag, 77);
    mapToConstant.append(R.styleable.Constraint_visibilityMode, 78);
    mapToConstant.append(R.styleable.Constraint_layout_constrainedWidth, 80);
    mapToConstant.append(R.styleable.Constraint_layout_constrainedHeight, 81);
    mapToConstant.append(R.styleable.Constraint_polarRelativeTo, 82);
    mapToConstant.append(R.styleable.Constraint_transformPivotTarget, 83);
    mapToConstant.append(R.styleable.Constraint_quantizeMotionSteps, 84);
    mapToConstant.append(R.styleable.Constraint_quantizeMotionPhase, 85);
    mapToConstant.append(R.styleable.Constraint_quantizeMotionInterpolator, 86);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_editor_absoluteY, 6);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_editor_absoluteY, 7);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_android_orientation, 27);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_goneMarginLeft, 13);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_goneMarginTop, 16);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_goneMarginRight, 14);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_goneMarginBottom, 11);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_goneMarginStart, 15);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_goneMarginEnd, 12);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintVertical_weight, 40);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintHorizontal_weight, 39);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintHorizontal_chainStyle, 41);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintVertical_chainStyle, 42);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintHorizontal_bias, 20);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintVertical_bias, 37);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintDimensionRatio, 5);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintLeft_creator, 87);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintTop_creator, 87);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintRight_creator, 87);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintBottom_creator, 87);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintBaseline_creator, 87);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_android_layout_marginLeft, 24);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_android_layout_marginRight, 28);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_android_layout_marginStart, 31);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_android_layout_marginEnd, 8);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_android_layout_marginTop, 34);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_android_layout_marginBottom, 2);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_android_layout_width, 23);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_android_layout_height, 21);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintWidth, 95);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintHeight, 96);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_android_visibility, 22);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_android_alpha, 43);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_android_elevation, 44);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_android_rotationX, 45);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_android_rotationY, 46);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_android_rotation, 60);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_android_scaleX, 47);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_android_scaleY, 48);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_android_transformPivotX, 49);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_android_transformPivotY, 50);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_android_translationX, 51);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_android_translationY, 52);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_android_translationZ, 53);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintWidth_default, 54);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintHeight_default, 55);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintWidth_max, 56);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintHeight_max, 57);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintWidth_min, 58);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintHeight_min, 59);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintCircleRadius, 62);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintCircleAngle, 63);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_animateRelativeTo, 64);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_transitionEasing, 65);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_drawPath, 66);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_transitionPathRotate, 67);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_motionStagger, 79);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_android_id, 38);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_motionTarget, 98);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_motionProgress, 68);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintWidth_percent, 69);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintHeight_percent, 70);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_chainUseRtl, 71);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_barrierDirection, 72);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_barrierMargin, 73);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_constraint_referenced_ids, 74);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_barrierAllowsGoneWidgets, 75);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_pathMotionArc, 76);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constraintTag, 77);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_visibilityMode, 78);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constrainedWidth, 80);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_constrainedHeight, 81);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_polarRelativeTo, 82);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_transformPivotTarget, 83);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_quantizeMotionSteps, 84);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_quantizeMotionPhase, 85);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_quantizeMotionInterpolator, 86);
    overrideMapToConstant.append(R.styleable.ConstraintOverride_layout_wrapBehaviorInParent, 97);
  }
  
  private void addAttributes(ConstraintAttribute.AttributeType paramAttributeType, String... paramVarArgs) {
    for (byte b = 0; b < paramVarArgs.length; b++) {
      String str;
      if (this.mSavedAttributes.containsKey(paramVarArgs[b])) {
        ConstraintAttribute constraintAttribute = this.mSavedAttributes.get(paramVarArgs[b]);
        if (constraintAttribute != null && constraintAttribute.getType() != paramAttributeType) {
          str = String.valueOf(constraintAttribute.getType().name());
          if (str.length() != 0) {
            str = "ConstraintAttribute is already a ".concat(str);
          } else {
            str = new String("ConstraintAttribute is already a ");
          } 
          throw new IllegalArgumentException(str);
        } 
      } else {
        ConstraintAttribute constraintAttribute = new ConstraintAttribute(paramVarArgs[b], (ConstraintAttribute.AttributeType)str);
        this.mSavedAttributes.put(paramVarArgs[b], constraintAttribute);
      } 
    } 
  }
  
  public static Constraint buildDelta(Context paramContext, XmlPullParser paramXmlPullParser) {
    AttributeSet attributeSet = Xml.asAttributeSet(paramXmlPullParser);
    Constraint constraint = new Constraint();
    TypedArray typedArray = paramContext.obtainStyledAttributes(attributeSet, R.styleable.ConstraintOverride);
    populateOverride(paramContext, constraint, typedArray);
    typedArray.recycle();
    return constraint;
  }
  
  private int[] convertReferenceString(View paramView, String paramString) {
    String[] arrayOfString = paramString.split(",");
    Context context = paramView.getContext();
    int[] arrayOfInt2 = new int[arrayOfString.length];
    byte b1 = 0;
    byte b2 = 0;
    while (b2 < arrayOfString.length) {
      String str = arrayOfString[b2].trim();
      int j = 0;
      try {
        int k = R.id.class.getField(str).getInt(null);
        j = k;
      } catch (Exception exception) {}
      int i = j;
      if (j == 0)
        i = context.getResources().getIdentifier(str, "id", context.getPackageName()); 
      j = i;
      if (i == 0) {
        j = i;
        if (paramView.isInEditMode()) {
          j = i;
          if (paramView.getParent() instanceof ConstraintLayout) {
            Object object = ((ConstraintLayout)paramView.getParent()).getDesignInformation(0, str);
            j = i;
            if (object != null) {
              j = i;
              if (object instanceof Integer)
                j = ((Integer)object).intValue(); 
            } 
          } 
        } 
      } 
      arrayOfInt2[b1] = j;
      b2++;
      b1++;
    } 
    int[] arrayOfInt1 = arrayOfInt2;
    if (b1 != arrayOfString.length)
      arrayOfInt1 = Arrays.copyOf(arrayOfInt2, b1); 
    return arrayOfInt1;
  }
  
  private void createHorizontalChain(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint, float[] paramArrayOffloat, int paramInt5, int paramInt6, int paramInt7) {
    if (paramArrayOfint.length >= 2) {
      if (paramArrayOffloat == null || paramArrayOffloat.length == paramArrayOfint.length) {
        if (paramArrayOffloat != null)
          (get(paramArrayOfint[0])).layout.horizontalWeight = paramArrayOffloat[0]; 
        (get(paramArrayOfint[0])).layout.horizontalChainStyle = paramInt5;
        connect(paramArrayOfint[0], paramInt6, paramInt1, paramInt2, -1);
        for (paramInt1 = 1; paramInt1 < paramArrayOfint.length; paramInt1++) {
          paramInt2 = paramArrayOfint[paramInt1];
          connect(paramArrayOfint[paramInt1], paramInt6, paramArrayOfint[paramInt1 - 1], paramInt7, -1);
          connect(paramArrayOfint[paramInt1 - 1], paramInt7, paramArrayOfint[paramInt1], paramInt6, -1);
          if (paramArrayOffloat != null)
            (get(paramArrayOfint[paramInt1])).layout.horizontalWeight = paramArrayOffloat[paramInt1]; 
        } 
        connect(paramArrayOfint[paramArrayOfint.length - 1], paramInt7, paramInt3, paramInt4, -1);
        return;
      } 
      throw new IllegalArgumentException("must have 2 or more widgets in a chain");
    } 
    throw new IllegalArgumentException("must have 2 or more widgets in a chain");
  }
  
  private Constraint fillFromAttributeList(Context paramContext, AttributeSet paramAttributeSet, boolean paramBoolean) {
    int[] arrayOfInt;
    Constraint constraint = new Constraint();
    if (paramBoolean) {
      arrayOfInt = R.styleable.ConstraintOverride;
    } else {
      arrayOfInt = R.styleable.Constraint;
    } 
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt);
    populateConstraint(paramContext, constraint, typedArray, paramBoolean);
    typedArray.recycle();
    return constraint;
  }
  
  private Constraint get(int paramInt) {
    if (!this.mConstraints.containsKey(Integer.valueOf(paramInt)))
      this.mConstraints.put(Integer.valueOf(paramInt), new Constraint()); 
    return this.mConstraints.get(Integer.valueOf(paramInt));
  }
  
  static String getDebugName(int paramInt) {
    for (Field field : ConstraintSet.class.getDeclaredFields()) {
      if (field.getName().contains("_") && field.getType() == int.class && Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()))
        try {
          if (field.getInt(null) == paramInt)
            return field.getName(); 
        } catch (IllegalAccessException illegalAccessException) {
          illegalAccessException.printStackTrace();
        }  
    } 
    return "UNKNOWN";
  }
  
  static String getLine(Context paramContext, int paramInt, XmlPullParser paramXmlPullParser) {
    String str1 = Debug.getName(paramContext, paramInt);
    paramInt = paramXmlPullParser.getLineNumber();
    String str2 = paramXmlPullParser.getName();
    return (new StringBuilder(String.valueOf(str1).length() + 22 + String.valueOf(str2).length())).append(".(").append(str1).append(".xml:").append(paramInt).append(") \"").append(str2).append("\"").toString();
  }
  
  private static int lookupID(TypedArray paramTypedArray, int paramInt1, int paramInt2) {
    int i = paramTypedArray.getResourceId(paramInt1, paramInt2);
    paramInt2 = i;
    if (i == -1)
      paramInt2 = paramTypedArray.getInt(paramInt1, -1); 
    return paramInt2;
  }
  
  static void parseDimensionConstraints(Object paramObject, TypedArray paramTypedArray, int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: ifnonnull -> 5
    //   4: return
    //   5: aload_1
    //   6: iload_2
    //   7: invokevirtual peekValue : (I)Landroid/util/TypedValue;
    //   10: getfield type : I
    //   13: istore #5
    //   15: iconst_0
    //   16: istore #4
    //   18: iconst_0
    //   19: istore #6
    //   21: iload #5
    //   23: tableswitch default -> 48, 3 -> 104, 4 -> 48, 5 -> 94
    //   48: aload_1
    //   49: iload_2
    //   50: iconst_0
    //   51: invokevirtual getInt : (II)I
    //   54: istore_2
    //   55: iload_2
    //   56: tableswitch default -> 88, -4 -> 123, -3 -> 118, -2 -> 115, -1 -> 115
    //   88: iload #4
    //   90: istore_2
    //   91: goto -> 129
    //   94: aload_1
    //   95: iload_2
    //   96: iconst_0
    //   97: invokevirtual getDimensionPixelSize : (II)I
    //   100: istore_2
    //   101: goto -> 129
    //   104: aload_0
    //   105: aload_1
    //   106: iload_2
    //   107: invokevirtual getString : (I)Ljava/lang/String;
    //   110: iload_3
    //   111: invokestatic parseDimensionConstraintsString : (Ljava/lang/Object;Ljava/lang/String;I)V
    //   114: return
    //   115: goto -> 129
    //   118: iconst_0
    //   119: istore_2
    //   120: goto -> 129
    //   123: bipush #-2
    //   125: istore_2
    //   126: iconst_1
    //   127: istore #6
    //   129: aload_0
    //   130: instanceof androidx/constraintlayout/widget/ConstraintLayout$LayoutParams
    //   133: ifeq -> 173
    //   136: aload_0
    //   137: checkcast androidx/constraintlayout/widget/ConstraintLayout$LayoutParams
    //   140: astore_0
    //   141: iload_3
    //   142: ifne -> 159
    //   145: aload_0
    //   146: iload_2
    //   147: putfield width : I
    //   150: aload_0
    //   151: iload #6
    //   153: putfield constrainedWidth : Z
    //   156: goto -> 170
    //   159: aload_0
    //   160: iload_2
    //   161: putfield height : I
    //   164: aload_0
    //   165: iload #6
    //   167: putfield constrainedHeight : Z
    //   170: goto -> 269
    //   173: aload_0
    //   174: instanceof androidx/constraintlayout/widget/ConstraintSet$Layout
    //   177: ifeq -> 217
    //   180: aload_0
    //   181: checkcast androidx/constraintlayout/widget/ConstraintSet$Layout
    //   184: astore_0
    //   185: iload_3
    //   186: ifne -> 203
    //   189: aload_0
    //   190: iload_2
    //   191: putfield mWidth : I
    //   194: aload_0
    //   195: iload #6
    //   197: putfield constrainedWidth : Z
    //   200: goto -> 269
    //   203: aload_0
    //   204: iload_2
    //   205: putfield mHeight : I
    //   208: aload_0
    //   209: iload #6
    //   211: putfield constrainedHeight : Z
    //   214: goto -> 269
    //   217: aload_0
    //   218: instanceof androidx/constraintlayout/widget/ConstraintSet$Constraint$Delta
    //   221: ifeq -> 269
    //   224: aload_0
    //   225: checkcast androidx/constraintlayout/widget/ConstraintSet$Constraint$Delta
    //   228: astore_0
    //   229: iload_3
    //   230: ifne -> 251
    //   233: aload_0
    //   234: bipush #23
    //   236: iload_2
    //   237: invokevirtual add : (II)V
    //   240: aload_0
    //   241: bipush #80
    //   243: iload #6
    //   245: invokevirtual add : (IZ)V
    //   248: goto -> 269
    //   251: aload_0
    //   252: bipush #21
    //   254: iload_2
    //   255: invokevirtual add : (II)V
    //   258: aload_0
    //   259: bipush #81
    //   261: iload #6
    //   263: invokevirtual add : (IZ)V
    //   266: goto -> 269
    //   269: return
  }
  
  static void parseDimensionConstraintsString(Object paramObject, String paramString, int paramInt) {
    if (paramString == null)
      return; 
    int j = paramString.indexOf('=');
    int i = paramString.length();
    if (j > 0 && j < i - 1) {
      String str = paramString.substring(0, j);
      paramString = paramString.substring(j + 1);
      if (paramString.length() > 0) {
        str = str.trim();
        paramString = paramString.trim();
        if ("ratio".equalsIgnoreCase(str)) {
          if (paramObject instanceof ConstraintLayout.LayoutParams) {
            paramObject = paramObject;
            if (paramInt == 0) {
              ((ConstraintLayout.LayoutParams)paramObject).width = 0;
            } else {
              ((ConstraintLayout.LayoutParams)paramObject).height = 0;
            } 
            parseDimensionRatioString((ConstraintLayout.LayoutParams)paramObject, paramString);
          } else if (paramObject instanceof Layout) {
            ((Layout)paramObject).dimensionRatio = paramString;
          } else if (paramObject instanceof Constraint.Delta) {
            ((Constraint.Delta)paramObject).add(5, paramString);
          } 
        } else if ("weight".equalsIgnoreCase(str)) {
          try {
            float f = Float.parseFloat(paramString);
            if (paramObject instanceof ConstraintLayout.LayoutParams) {
              paramObject = paramObject;
              if (paramInt == 0) {
                ((ConstraintLayout.LayoutParams)paramObject).width = 0;
                ((ConstraintLayout.LayoutParams)paramObject).horizontalWeight = f;
              } else {
                ((ConstraintLayout.LayoutParams)paramObject).height = 0;
                ((ConstraintLayout.LayoutParams)paramObject).verticalWeight = f;
              } 
            } else if (paramObject instanceof Layout) {
              paramObject = paramObject;
              if (paramInt == 0) {
                ((Layout)paramObject).mWidth = 0;
                ((Layout)paramObject).horizontalWeight = f;
              } else {
                ((Layout)paramObject).mHeight = 0;
                ((Layout)paramObject).verticalWeight = f;
              } 
            } else if (paramObject instanceof Constraint.Delta) {
              paramObject = paramObject;
              if (paramInt == 0) {
                paramObject.add(23, 0);
                paramObject.add(39, f);
              } else {
                paramObject.add(21, 0);
                paramObject.add(40, f);
              } 
            } 
          } catch (NumberFormatException numberFormatException) {}
        } else if ("parent".equalsIgnoreCase(str)) {
          try {
            ConstraintLayout.LayoutParams layoutParams;
            float f = Math.max(0.0F, Math.min(1.0F, Float.parseFloat(paramString)));
            if (numberFormatException instanceof ConstraintLayout.LayoutParams) {
              layoutParams = (ConstraintLayout.LayoutParams)numberFormatException;
              if (paramInt == 0) {
                layoutParams.width = 0;
                layoutParams.matchConstraintPercentWidth = f;
                layoutParams.matchConstraintDefaultWidth = 2;
              } else {
                layoutParams.height = 0;
                layoutParams.matchConstraintPercentHeight = f;
                layoutParams.matchConstraintDefaultHeight = 2;
              } 
            } else {
              Layout layout;
              if (layoutParams instanceof Layout) {
                layout = (Layout)layoutParams;
                if (paramInt == 0) {
                  layout.mWidth = 0;
                  layout.widthPercent = f;
                  layout.widthDefault = 2;
                } else {
                  layout.mHeight = 0;
                  layout.heightPercent = f;
                  layout.heightDefault = 2;
                } 
              } else if (layout instanceof Constraint.Delta) {
                Constraint.Delta delta = (Constraint.Delta)layout;
                if (paramInt == 0) {
                  delta.add(23, 0);
                  delta.add(54, 2);
                } else {
                  delta.add(21, 0);
                  delta.add(55, 2);
                } 
              } 
            } 
          } catch (NumberFormatException numberFormatException1) {}
        } 
      } 
    } 
  }
  
  static void parseDimensionRatioString(ConstraintLayout.LayoutParams paramLayoutParams, String paramString) {
    float f2 = Float.NaN;
    byte b = -1;
    float f1 = f2;
    int i = b;
    if (paramString != null) {
      int j = paramString.length();
      i = paramString.indexOf(',');
      if (i > 0 && i < j - 1) {
        String str = paramString.substring(0, i);
        if (str.equalsIgnoreCase("W")) {
          b = 0;
        } else if (str.equalsIgnoreCase("H")) {
          b = 1;
        } 
        i++;
      } else {
        i = 0;
      } 
      int k = paramString.indexOf(':');
      if (k >= 0 && k < j - 1) {
        String str1 = paramString.substring(i, k);
        String str2 = paramString.substring(k + 1);
        f1 = f2;
        if (str1.length() > 0) {
          f1 = f2;
          if (str2.length() > 0)
            try {
              float f4 = Float.parseFloat(str1);
              float f3 = Float.parseFloat(str2);
              f1 = f2;
              if (f4 > 0.0F) {
                f1 = f2;
                if (f3 > 0.0F)
                  if (b == 1) {
                    f1 = Math.abs(f3 / f4);
                  } else {
                    f1 = Math.abs(f4 / f3);
                  }  
              } 
            } catch (NumberFormatException numberFormatException) {
              f1 = f2;
            }  
        } 
        i = b;
      } else {
        String str = paramString.substring(i);
        f1 = f2;
        i = b;
        if (str.length() > 0)
          try {
            f1 = Float.parseFloat(str);
            i = b;
          } catch (NumberFormatException numberFormatException) {
            i = b;
            f1 = f2;
          }  
      } 
    } 
    paramLayoutParams.dimensionRatio = paramString;
    paramLayoutParams.dimensionRatioValue = f1;
    paramLayoutParams.dimensionRatioSide = i;
  }
  
  private void populateConstraint(Context paramContext, Constraint paramConstraint, TypedArray paramTypedArray, boolean paramBoolean) {
    if (paramBoolean) {
      populateOverride(paramContext, paramConstraint, paramTypedArray);
      return;
    } 
    int i = paramTypedArray.getIndexCount();
    for (byte b = 0; b < i; b++) {
      String str;
      TypedValue typedValue;
      int j = paramTypedArray.getIndex(b);
      if (j != R.styleable.Constraint_android_id && R.styleable.Constraint_android_layout_marginStart != j && R.styleable.Constraint_android_layout_marginEnd != j) {
        paramConstraint.motion.mApply = true;
        paramConstraint.layout.mApply = true;
        paramConstraint.propertySet.mApply = true;
        paramConstraint.transform.mApply = true;
      } 
      switch (mapToConstant.get(j)) {
        default:
          str = Integer.toHexString(j);
          j = mapToConstant.get(j);
          Log.w("ConstraintSet", (new StringBuilder(String.valueOf(str).length() + 34)).append("Unknown attribute 0x").append(str).append("   ").append(j).toString());
          break;
        case 97:
          paramConstraint.layout.mWrapBehavior = paramTypedArray.getInt(j, paramConstraint.layout.mWrapBehavior);
          break;
        case 96:
          parseDimensionConstraints(paramConstraint.layout, paramTypedArray, j, 1);
          break;
        case 95:
          parseDimensionConstraints(paramConstraint.layout, paramTypedArray, j, 0);
          break;
        case 94:
          paramConstraint.layout.goneBaselineMargin = paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.goneBaselineMargin);
          break;
        case 93:
          paramConstraint.layout.baselineMargin = paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.baselineMargin);
          break;
        case 92:
          paramConstraint.layout.baselineToBottom = lookupID(paramTypedArray, j, paramConstraint.layout.baselineToBottom);
          break;
        case 91:
          paramConstraint.layout.baselineToTop = lookupID(paramTypedArray, j, paramConstraint.layout.baselineToTop);
          break;
        case 87:
          str = Integer.toHexString(j);
          j = mapToConstant.get(j);
          Log.w("ConstraintSet", (new StringBuilder(String.valueOf(str).length() + 33)).append("unused attribute 0x").append(str).append("   ").append(j).toString());
          break;
        case 86:
          typedValue = paramTypedArray.peekValue(j);
          if (typedValue.type == 1) {
            paramConstraint.motion.mQuantizeInterpolatorID = paramTypedArray.getResourceId(j, -1);
            if (paramConstraint.motion.mQuantizeInterpolatorID != -1)
              paramConstraint.motion.mQuantizeInterpolatorType = -2; 
            break;
          } 
          if (typedValue.type == 3) {
            paramConstraint.motion.mQuantizeInterpolatorString = paramTypedArray.getString(j);
            if (paramConstraint.motion.mQuantizeInterpolatorString.indexOf("/") > 0) {
              paramConstraint.motion.mQuantizeInterpolatorID = paramTypedArray.getResourceId(j, -1);
              paramConstraint.motion.mQuantizeInterpolatorType = -2;
              break;
            } 
            paramConstraint.motion.mQuantizeInterpolatorType = -1;
            break;
          } 
          paramConstraint.motion.mQuantizeInterpolatorType = paramTypedArray.getInteger(j, paramConstraint.motion.mQuantizeInterpolatorID);
          break;
        case 85:
          paramConstraint.motion.mQuantizeMotionPhase = paramTypedArray.getFloat(j, paramConstraint.motion.mQuantizeMotionPhase);
          break;
        case 84:
          paramConstraint.motion.mQuantizeMotionSteps = paramTypedArray.getInteger(j, paramConstraint.motion.mQuantizeMotionSteps);
          break;
        case 83:
          paramConstraint.transform.transformPivotTarget = lookupID(paramTypedArray, j, paramConstraint.transform.transformPivotTarget);
          break;
        case 82:
          paramConstraint.motion.mAnimateCircleAngleTo = paramTypedArray.getInteger(j, paramConstraint.motion.mAnimateCircleAngleTo);
          break;
        case 81:
          paramConstraint.layout.constrainedHeight = paramTypedArray.getBoolean(j, paramConstraint.layout.constrainedHeight);
          break;
        case 80:
          paramConstraint.layout.constrainedWidth = paramTypedArray.getBoolean(j, paramConstraint.layout.constrainedWidth);
          break;
        case 79:
          paramConstraint.motion.mMotionStagger = paramTypedArray.getFloat(j, paramConstraint.motion.mMotionStagger);
          break;
        case 78:
          paramConstraint.propertySet.mVisibilityMode = paramTypedArray.getInt(j, paramConstraint.propertySet.mVisibilityMode);
          break;
        case 77:
          paramConstraint.layout.mConstraintTag = paramTypedArray.getString(j);
          break;
        case 76:
          paramConstraint.motion.mPathMotionArc = paramTypedArray.getInt(j, paramConstraint.motion.mPathMotionArc);
          break;
        case 75:
          paramConstraint.layout.mBarrierAllowsGoneWidgets = paramTypedArray.getBoolean(j, paramConstraint.layout.mBarrierAllowsGoneWidgets);
          break;
        case 74:
          paramConstraint.layout.mReferenceIdString = paramTypedArray.getString(j);
          break;
        case 73:
          paramConstraint.layout.mBarrierMargin = paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.mBarrierMargin);
          break;
        case 72:
          paramConstraint.layout.mBarrierDirection = paramTypedArray.getInt(j, paramConstraint.layout.mBarrierDirection);
          break;
        case 71:
          Log.e("ConstraintSet", "CURRENTLY UNSUPPORTED");
          break;
        case 70:
          paramConstraint.layout.heightPercent = paramTypedArray.getFloat(j, 1.0F);
          break;
        case 69:
          paramConstraint.layout.widthPercent = paramTypedArray.getFloat(j, 1.0F);
          break;
        case 68:
          paramConstraint.propertySet.mProgress = paramTypedArray.getFloat(j, paramConstraint.propertySet.mProgress);
          break;
        case 67:
          paramConstraint.motion.mPathRotate = paramTypedArray.getFloat(j, paramConstraint.motion.mPathRotate);
          break;
        case 66:
          paramConstraint.motion.mDrawPath = paramTypedArray.getInt(j, 0);
          break;
        case 65:
          if ((paramTypedArray.peekValue(j)).type == 3) {
            paramConstraint.motion.mTransitionEasing = paramTypedArray.getString(j);
            break;
          } 
          paramConstraint.motion.mTransitionEasing = Easing.NAMED_EASING[paramTypedArray.getInteger(j, 0)];
          break;
        case 64:
          paramConstraint.motion.mAnimateRelativeTo = lookupID(paramTypedArray, j, paramConstraint.motion.mAnimateRelativeTo);
          break;
        case 63:
          paramConstraint.layout.circleAngle = paramTypedArray.getFloat(j, paramConstraint.layout.circleAngle);
          break;
        case 62:
          paramConstraint.layout.circleRadius = paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.circleRadius);
          break;
        case 61:
          paramConstraint.layout.circleConstraint = lookupID(paramTypedArray, j, paramConstraint.layout.circleConstraint);
          break;
        case 60:
          paramConstraint.transform.rotation = paramTypedArray.getFloat(j, paramConstraint.transform.rotation);
          break;
        case 59:
          paramConstraint.layout.heightMin = paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.heightMin);
          break;
        case 58:
          paramConstraint.layout.widthMin = paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.widthMin);
          break;
        case 57:
          paramConstraint.layout.heightMax = paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.heightMax);
          break;
        case 56:
          paramConstraint.layout.widthMax = paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.widthMax);
          break;
        case 55:
          paramConstraint.layout.heightDefault = paramTypedArray.getInt(j, paramConstraint.layout.heightDefault);
          break;
        case 54:
          paramConstraint.layout.widthDefault = paramTypedArray.getInt(j, paramConstraint.layout.widthDefault);
          break;
        case 53:
          if (Build.VERSION.SDK_INT >= 21)
            paramConstraint.transform.translationZ = paramTypedArray.getDimension(j, paramConstraint.transform.translationZ); 
          break;
        case 52:
          paramConstraint.transform.translationY = paramTypedArray.getDimension(j, paramConstraint.transform.translationY);
          break;
        case 51:
          paramConstraint.transform.translationX = paramTypedArray.getDimension(j, paramConstraint.transform.translationX);
          break;
        case 50:
          paramConstraint.transform.transformPivotY = paramTypedArray.getDimension(j, paramConstraint.transform.transformPivotY);
          break;
        case 49:
          paramConstraint.transform.transformPivotX = paramTypedArray.getDimension(j, paramConstraint.transform.transformPivotX);
          break;
        case 48:
          paramConstraint.transform.scaleY = paramTypedArray.getFloat(j, paramConstraint.transform.scaleY);
          break;
        case 47:
          paramConstraint.transform.scaleX = paramTypedArray.getFloat(j, paramConstraint.transform.scaleX);
          break;
        case 46:
          paramConstraint.transform.rotationY = paramTypedArray.getFloat(j, paramConstraint.transform.rotationY);
          break;
        case 45:
          paramConstraint.transform.rotationX = paramTypedArray.getFloat(j, paramConstraint.transform.rotationX);
          break;
        case 44:
          if (Build.VERSION.SDK_INT >= 21) {
            paramConstraint.transform.applyElevation = true;
            paramConstraint.transform.elevation = paramTypedArray.getDimension(j, paramConstraint.transform.elevation);
          } 
          break;
        case 43:
          paramConstraint.propertySet.alpha = paramTypedArray.getFloat(j, paramConstraint.propertySet.alpha);
          break;
        case 42:
          paramConstraint.layout.verticalChainStyle = paramTypedArray.getInt(j, paramConstraint.layout.verticalChainStyle);
          break;
        case 41:
          paramConstraint.layout.horizontalChainStyle = paramTypedArray.getInt(j, paramConstraint.layout.horizontalChainStyle);
          break;
        case 40:
          paramConstraint.layout.verticalWeight = paramTypedArray.getFloat(j, paramConstraint.layout.verticalWeight);
          break;
        case 39:
          paramConstraint.layout.horizontalWeight = paramTypedArray.getFloat(j, paramConstraint.layout.horizontalWeight);
          break;
        case 38:
          paramConstraint.mViewId = paramTypedArray.getResourceId(j, paramConstraint.mViewId);
          break;
        case 37:
          paramConstraint.layout.verticalBias = paramTypedArray.getFloat(j, paramConstraint.layout.verticalBias);
          break;
        case 36:
          paramConstraint.layout.topToTop = lookupID(paramTypedArray, j, paramConstraint.layout.topToTop);
          break;
        case 35:
          paramConstraint.layout.topToBottom = lookupID(paramTypedArray, j, paramConstraint.layout.topToBottom);
          break;
        case 34:
          paramConstraint.layout.topMargin = paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.topMargin);
          break;
        case 33:
          paramConstraint.layout.startToStart = lookupID(paramTypedArray, j, paramConstraint.layout.startToStart);
          break;
        case 32:
          paramConstraint.layout.startToEnd = lookupID(paramTypedArray, j, paramConstraint.layout.startToEnd);
          break;
        case 31:
          if (Build.VERSION.SDK_INT >= 17)
            paramConstraint.layout.startMargin = paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.startMargin); 
          break;
        case 30:
          paramConstraint.layout.rightToRight = lookupID(paramTypedArray, j, paramConstraint.layout.rightToRight);
          break;
        case 29:
          paramConstraint.layout.rightToLeft = lookupID(paramTypedArray, j, paramConstraint.layout.rightToLeft);
          break;
        case 28:
          paramConstraint.layout.rightMargin = paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.rightMargin);
          break;
        case 27:
          paramConstraint.layout.orientation = paramTypedArray.getInt(j, paramConstraint.layout.orientation);
          break;
        case 26:
          paramConstraint.layout.leftToRight = lookupID(paramTypedArray, j, paramConstraint.layout.leftToRight);
          break;
        case 25:
          paramConstraint.layout.leftToLeft = lookupID(paramTypedArray, j, paramConstraint.layout.leftToLeft);
          break;
        case 24:
          paramConstraint.layout.leftMargin = paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.leftMargin);
          break;
        case 23:
          paramConstraint.layout.mWidth = paramTypedArray.getLayoutDimension(j, paramConstraint.layout.mWidth);
          break;
        case 22:
          paramConstraint.propertySet.visibility = paramTypedArray.getInt(j, paramConstraint.propertySet.visibility);
          paramConstraint.propertySet.visibility = VISIBILITY_FLAGS[paramConstraint.propertySet.visibility];
          break;
        case 21:
          paramConstraint.layout.mHeight = paramTypedArray.getLayoutDimension(j, paramConstraint.layout.mHeight);
          break;
        case 20:
          paramConstraint.layout.horizontalBias = paramTypedArray.getFloat(j, paramConstraint.layout.horizontalBias);
          break;
        case 19:
          paramConstraint.layout.guidePercent = paramTypedArray.getFloat(j, paramConstraint.layout.guidePercent);
          break;
        case 18:
          paramConstraint.layout.guideEnd = paramTypedArray.getDimensionPixelOffset(j, paramConstraint.layout.guideEnd);
          break;
        case 17:
          paramConstraint.layout.guideBegin = paramTypedArray.getDimensionPixelOffset(j, paramConstraint.layout.guideBegin);
          break;
        case 16:
          paramConstraint.layout.goneTopMargin = paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.goneTopMargin);
          break;
        case 15:
          paramConstraint.layout.goneStartMargin = paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.goneStartMargin);
          break;
        case 14:
          paramConstraint.layout.goneRightMargin = paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.goneRightMargin);
          break;
        case 13:
          paramConstraint.layout.goneLeftMargin = paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.goneLeftMargin);
          break;
        case 12:
          paramConstraint.layout.goneEndMargin = paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.goneEndMargin);
          break;
        case 11:
          paramConstraint.layout.goneBottomMargin = paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.goneBottomMargin);
          break;
        case 10:
          paramConstraint.layout.endToStart = lookupID(paramTypedArray, j, paramConstraint.layout.endToStart);
          break;
        case 9:
          paramConstraint.layout.endToEnd = lookupID(paramTypedArray, j, paramConstraint.layout.endToEnd);
          break;
        case 8:
          if (Build.VERSION.SDK_INT >= 17)
            paramConstraint.layout.endMargin = paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.endMargin); 
          break;
        case 7:
          paramConstraint.layout.editorAbsoluteY = paramTypedArray.getDimensionPixelOffset(j, paramConstraint.layout.editorAbsoluteY);
          break;
        case 6:
          paramConstraint.layout.editorAbsoluteX = paramTypedArray.getDimensionPixelOffset(j, paramConstraint.layout.editorAbsoluteX);
          break;
        case 5:
          paramConstraint.layout.dimensionRatio = paramTypedArray.getString(j);
          break;
        case 4:
          paramConstraint.layout.bottomToTop = lookupID(paramTypedArray, j, paramConstraint.layout.bottomToTop);
          break;
        case 3:
          paramConstraint.layout.bottomToBottom = lookupID(paramTypedArray, j, paramConstraint.layout.bottomToBottom);
          break;
        case 2:
          paramConstraint.layout.bottomMargin = paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.bottomMargin);
          break;
        case 1:
          paramConstraint.layout.baselineToBaseline = lookupID(paramTypedArray, j, paramConstraint.layout.baselineToBaseline);
          break;
      } 
    } 
    if (paramConstraint.layout.mReferenceIdString != null)
      paramConstraint.layout.mReferenceIds = null; 
  }
  
  private static void populateOverride(Context paramContext, Constraint paramConstraint, TypedArray paramTypedArray) {
    int i = paramTypedArray.getIndexCount();
    Constraint.Delta delta = new Constraint.Delta();
    paramConstraint.mDelta = delta;
    paramConstraint.motion.mApply = false;
    paramConstraint.layout.mApply = false;
    paramConstraint.propertySet.mApply = false;
    paramConstraint.transform.mApply = false;
    for (byte b = 0; b < i; b++) {
      String str;
      TypedValue typedValue;
      int j = paramTypedArray.getIndex(b);
      switch (overrideMapToConstant.get(j)) {
        default:
          str = Integer.toHexString(j);
          j = mapToConstant.get(j);
          Log.w("ConstraintSet", (new StringBuilder(String.valueOf(str).length() + 34)).append("Unknown attribute 0x").append(str).append("   ").append(j).toString());
          break;
        case 98:
          if (MotionLayout.IS_IN_EDIT_MODE) {
            paramConstraint.mViewId = paramTypedArray.getResourceId(j, paramConstraint.mViewId);
            if (paramConstraint.mViewId == -1)
              paramConstraint.mTargetString = paramTypedArray.getString(j); 
            break;
          } 
          if ((paramTypedArray.peekValue(j)).type == 3) {
            paramConstraint.mTargetString = paramTypedArray.getString(j);
            break;
          } 
          paramConstraint.mViewId = paramTypedArray.getResourceId(j, paramConstraint.mViewId);
          break;
        case 97:
          delta.add(97, paramTypedArray.getInt(j, paramConstraint.layout.mWrapBehavior));
          break;
        case 96:
          parseDimensionConstraints(delta, paramTypedArray, j, 1);
          break;
        case 95:
          parseDimensionConstraints(delta, paramTypedArray, j, 0);
          break;
        case 94:
          delta.add(94, paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.goneBaselineMargin));
          break;
        case 93:
          delta.add(93, paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.baselineMargin));
          break;
        case 87:
          str = Integer.toHexString(j);
          j = mapToConstant.get(j);
          Log.w("ConstraintSet", (new StringBuilder(String.valueOf(str).length() + 33)).append("unused attribute 0x").append(str).append("   ").append(j).toString());
          break;
        case 86:
          typedValue = paramTypedArray.peekValue(j);
          if (typedValue.type == 1) {
            paramConstraint.motion.mQuantizeInterpolatorID = paramTypedArray.getResourceId(j, -1);
            delta.add(89, paramConstraint.motion.mQuantizeInterpolatorID);
            if (paramConstraint.motion.mQuantizeInterpolatorID != -1) {
              paramConstraint.motion.mQuantizeInterpolatorType = -2;
              delta.add(88, paramConstraint.motion.mQuantizeInterpolatorType);
            } 
            break;
          } 
          if (typedValue.type == 3) {
            paramConstraint.motion.mQuantizeInterpolatorString = paramTypedArray.getString(j);
            delta.add(90, paramConstraint.motion.mQuantizeInterpolatorString);
            if (paramConstraint.motion.mQuantizeInterpolatorString.indexOf("/") > 0) {
              paramConstraint.motion.mQuantizeInterpolatorID = paramTypedArray.getResourceId(j, -1);
              delta.add(89, paramConstraint.motion.mQuantizeInterpolatorID);
              paramConstraint.motion.mQuantizeInterpolatorType = -2;
              delta.add(88, paramConstraint.motion.mQuantizeInterpolatorType);
              break;
            } 
            paramConstraint.motion.mQuantizeInterpolatorType = -1;
            delta.add(88, paramConstraint.motion.mQuantizeInterpolatorType);
            break;
          } 
          paramConstraint.motion.mQuantizeInterpolatorType = paramTypedArray.getInteger(j, paramConstraint.motion.mQuantizeInterpolatorID);
          delta.add(88, paramConstraint.motion.mQuantizeInterpolatorType);
          break;
        case 85:
          delta.add(85, paramTypedArray.getFloat(j, paramConstraint.motion.mQuantizeMotionPhase));
          break;
        case 84:
          delta.add(84, paramTypedArray.getInteger(j, paramConstraint.motion.mQuantizeMotionSteps));
          break;
        case 83:
          delta.add(83, lookupID(paramTypedArray, j, paramConstraint.transform.transformPivotTarget));
          break;
        case 82:
          delta.add(82, paramTypedArray.getInteger(j, paramConstraint.motion.mAnimateCircleAngleTo));
          break;
        case 81:
          delta.add(81, paramTypedArray.getBoolean(j, paramConstraint.layout.constrainedHeight));
          break;
        case 80:
          delta.add(80, paramTypedArray.getBoolean(j, paramConstraint.layout.constrainedWidth));
          break;
        case 79:
          delta.add(79, paramTypedArray.getFloat(j, paramConstraint.motion.mMotionStagger));
          break;
        case 78:
          delta.add(78, paramTypedArray.getInt(j, paramConstraint.propertySet.mVisibilityMode));
          break;
        case 77:
          delta.add(77, paramTypedArray.getString(j));
          break;
        case 76:
          delta.add(76, paramTypedArray.getInt(j, paramConstraint.motion.mPathMotionArc));
          break;
        case 75:
          delta.add(75, paramTypedArray.getBoolean(j, paramConstraint.layout.mBarrierAllowsGoneWidgets));
          break;
        case 74:
          delta.add(74, paramTypedArray.getString(j));
          break;
        case 73:
          delta.add(73, paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.mBarrierMargin));
          break;
        case 72:
          delta.add(72, paramTypedArray.getInt(j, paramConstraint.layout.mBarrierDirection));
          break;
        case 71:
          Log.e("ConstraintSet", "CURRENTLY UNSUPPORTED");
          break;
        case 70:
          delta.add(70, paramTypedArray.getFloat(j, 1.0F));
          break;
        case 69:
          delta.add(69, paramTypedArray.getFloat(j, 1.0F));
          break;
        case 68:
          delta.add(68, paramTypedArray.getFloat(j, paramConstraint.propertySet.mProgress));
          break;
        case 67:
          delta.add(67, paramTypedArray.getFloat(j, paramConstraint.motion.mPathRotate));
          break;
        case 66:
          delta.add(66, paramTypedArray.getInt(j, 0));
          break;
        case 65:
          if ((paramTypedArray.peekValue(j)).type == 3) {
            delta.add(65, paramTypedArray.getString(j));
            break;
          } 
          delta.add(65, Easing.NAMED_EASING[paramTypedArray.getInteger(j, 0)]);
          break;
        case 64:
          delta.add(64, lookupID(paramTypedArray, j, paramConstraint.motion.mAnimateRelativeTo));
          break;
        case 63:
          delta.add(63, paramTypedArray.getFloat(j, paramConstraint.layout.circleAngle));
          break;
        case 62:
          delta.add(62, paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.circleRadius));
          break;
        case 60:
          delta.add(60, paramTypedArray.getFloat(j, paramConstraint.transform.rotation));
          break;
        case 59:
          delta.add(59, paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.heightMin));
          break;
        case 58:
          delta.add(58, paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.widthMin));
          break;
        case 57:
          delta.add(57, paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.heightMax));
          break;
        case 56:
          delta.add(56, paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.widthMax));
          break;
        case 55:
          delta.add(55, paramTypedArray.getInt(j, paramConstraint.layout.heightDefault));
          break;
        case 54:
          delta.add(54, paramTypedArray.getInt(j, paramConstraint.layout.widthDefault));
          break;
        case 53:
          if (Build.VERSION.SDK_INT >= 21)
            delta.add(53, paramTypedArray.getDimension(j, paramConstraint.transform.translationZ)); 
          break;
        case 52:
          delta.add(52, paramTypedArray.getDimension(j, paramConstraint.transform.translationY));
          break;
        case 51:
          delta.add(51, paramTypedArray.getDimension(j, paramConstraint.transform.translationX));
          break;
        case 50:
          delta.add(50, paramTypedArray.getDimension(j, paramConstraint.transform.transformPivotY));
          break;
        case 49:
          delta.add(49, paramTypedArray.getDimension(j, paramConstraint.transform.transformPivotX));
          break;
        case 48:
          delta.add(48, paramTypedArray.getFloat(j, paramConstraint.transform.scaleY));
          break;
        case 47:
          delta.add(47, paramTypedArray.getFloat(j, paramConstraint.transform.scaleX));
          break;
        case 46:
          delta.add(46, paramTypedArray.getFloat(j, paramConstraint.transform.rotationY));
          break;
        case 45:
          delta.add(45, paramTypedArray.getFloat(j, paramConstraint.transform.rotationX));
          break;
        case 44:
          if (Build.VERSION.SDK_INT >= 21) {
            delta.add(44, true);
            delta.add(44, paramTypedArray.getDimension(j, paramConstraint.transform.elevation));
          } 
          break;
        case 43:
          delta.add(43, paramTypedArray.getFloat(j, paramConstraint.propertySet.alpha));
          break;
        case 42:
          delta.add(42, paramTypedArray.getInt(j, paramConstraint.layout.verticalChainStyle));
          break;
        case 41:
          delta.add(41, paramTypedArray.getInt(j, paramConstraint.layout.horizontalChainStyle));
          break;
        case 40:
          delta.add(40, paramTypedArray.getFloat(j, paramConstraint.layout.verticalWeight));
          break;
        case 39:
          delta.add(39, paramTypedArray.getFloat(j, paramConstraint.layout.horizontalWeight));
          break;
        case 38:
          paramConstraint.mViewId = paramTypedArray.getResourceId(j, paramConstraint.mViewId);
          delta.add(38, paramConstraint.mViewId);
          break;
        case 37:
          delta.add(37, paramTypedArray.getFloat(j, paramConstraint.layout.verticalBias));
          break;
        case 34:
          delta.add(34, paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.topMargin));
          break;
        case 31:
          if (Build.VERSION.SDK_INT >= 17)
            delta.add(31, paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.startMargin)); 
          break;
        case 28:
          delta.add(28, paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.rightMargin));
          break;
        case 27:
          delta.add(27, paramTypedArray.getInt(j, paramConstraint.layout.orientation));
          break;
        case 24:
          delta.add(24, paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.leftMargin));
          break;
        case 23:
          delta.add(23, paramTypedArray.getLayoutDimension(j, paramConstraint.layout.mWidth));
          break;
        case 22:
          delta.add(22, VISIBILITY_FLAGS[paramTypedArray.getInt(j, paramConstraint.propertySet.visibility)]);
          break;
        case 21:
          delta.add(21, paramTypedArray.getLayoutDimension(j, paramConstraint.layout.mHeight));
          break;
        case 20:
          delta.add(20, paramTypedArray.getFloat(j, paramConstraint.layout.horizontalBias));
          break;
        case 19:
          delta.add(19, paramTypedArray.getFloat(j, paramConstraint.layout.guidePercent));
          break;
        case 18:
          delta.add(18, paramTypedArray.getDimensionPixelOffset(j, paramConstraint.layout.guideEnd));
          break;
        case 17:
          delta.add(17, paramTypedArray.getDimensionPixelOffset(j, paramConstraint.layout.guideBegin));
          break;
        case 16:
          delta.add(16, paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.goneTopMargin));
          break;
        case 15:
          delta.add(15, paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.goneStartMargin));
          break;
        case 14:
          delta.add(14, paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.goneRightMargin));
          break;
        case 13:
          delta.add(13, paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.goneLeftMargin));
          break;
        case 12:
          delta.add(12, paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.goneEndMargin));
          break;
        case 11:
          delta.add(11, paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.goneBottomMargin));
          break;
        case 8:
          if (Build.VERSION.SDK_INT >= 17)
            delta.add(8, paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.endMargin)); 
          break;
        case 7:
          delta.add(7, paramTypedArray.getDimensionPixelOffset(j, paramConstraint.layout.editorAbsoluteY));
          break;
        case 6:
          delta.add(6, paramTypedArray.getDimensionPixelOffset(j, paramConstraint.layout.editorAbsoluteX));
          break;
        case 5:
          delta.add(5, paramTypedArray.getString(j));
          break;
        case 2:
          delta.add(2, paramTypedArray.getDimensionPixelSize(j, paramConstraint.layout.bottomMargin));
          break;
      } 
    } 
  }
  
  private static void setDeltaValue(Constraint paramConstraint, int paramInt, float paramFloat) {
    switch (paramInt) {
      default:
        Log.w("ConstraintSet", "Unknown attribute 0x");
      case 87:
        return;
      case 85:
        paramConstraint.motion.mQuantizeMotionPhase = paramFloat;
      case 79:
        paramConstraint.motion.mMotionStagger = paramFloat;
      case 70:
        paramConstraint.layout.heightPercent = paramFloat;
      case 69:
        paramConstraint.layout.widthPercent = paramFloat;
      case 68:
        paramConstraint.propertySet.mProgress = paramFloat;
      case 67:
        paramConstraint.motion.mPathRotate = paramFloat;
      case 63:
        paramConstraint.layout.circleAngle = paramFloat;
      case 60:
        paramConstraint.transform.rotation = paramFloat;
      case 53:
        paramConstraint.transform.translationZ = paramFloat;
      case 52:
        paramConstraint.transform.translationY = paramFloat;
      case 51:
        paramConstraint.transform.translationX = paramFloat;
      case 50:
        paramConstraint.transform.transformPivotY = paramFloat;
      case 49:
        paramConstraint.transform.transformPivotX = paramFloat;
      case 48:
        paramConstraint.transform.scaleY = paramFloat;
      case 47:
        paramConstraint.transform.scaleX = paramFloat;
      case 46:
        paramConstraint.transform.rotationY = paramFloat;
      case 45:
        paramConstraint.transform.rotationX = paramFloat;
      case 44:
        paramConstraint.transform.elevation = paramFloat;
        paramConstraint.transform.applyElevation = true;
      case 43:
        paramConstraint.propertySet.alpha = paramFloat;
      case 40:
        paramConstraint.layout.verticalWeight = paramFloat;
      case 39:
        paramConstraint.layout.horizontalWeight = paramFloat;
      case 37:
        paramConstraint.layout.verticalBias = paramFloat;
      case 20:
        paramConstraint.layout.horizontalBias = paramFloat;
      case 19:
        break;
    } 
    paramConstraint.layout.guidePercent = paramFloat;
  }
  
  private static void setDeltaValue(Constraint paramConstraint, int paramInt1, int paramInt2) {
    switch (paramInt1) {
      default:
        Log.w("ConstraintSet", "Unknown attribute 0x");
      case 97:
        paramConstraint.layout.mWrapBehavior = paramInt2;
      case 94:
        paramConstraint.layout.goneBaselineMargin = paramInt2;
      case 93:
        paramConstraint.layout.baselineMargin = paramInt2;
      case 89:
        paramConstraint.motion.mQuantizeInterpolatorID = paramInt2;
      case 88:
        paramConstraint.motion.mQuantizeInterpolatorType = paramInt2;
      case 87:
        return;
      case 84:
        paramConstraint.motion.mQuantizeMotionSteps = paramInt2;
      case 83:
        paramConstraint.transform.transformPivotTarget = paramInt2;
      case 82:
        paramConstraint.motion.mAnimateCircleAngleTo = paramInt2;
      case 78:
        paramConstraint.propertySet.mVisibilityMode = paramInt2;
      case 76:
        paramConstraint.motion.mPathMotionArc = paramInt2;
      case 73:
        paramConstraint.layout.mBarrierMargin = paramInt2;
      case 72:
        paramConstraint.layout.mBarrierDirection = paramInt2;
      case 66:
        paramConstraint.motion.mDrawPath = paramInt2;
      case 64:
        paramConstraint.motion.mAnimateRelativeTo = paramInt2;
      case 62:
        paramConstraint.layout.circleRadius = paramInt2;
      case 61:
        paramConstraint.layout.circleConstraint = paramInt2;
      case 59:
        paramConstraint.layout.heightMin = paramInt2;
      case 58:
        paramConstraint.layout.widthMin = paramInt2;
      case 57:
        paramConstraint.layout.heightMax = paramInt2;
      case 56:
        paramConstraint.layout.widthMax = paramInt2;
      case 55:
        paramConstraint.layout.heightDefault = paramInt2;
      case 54:
        paramConstraint.layout.widthDefault = paramInt2;
      case 42:
        paramConstraint.layout.verticalChainStyle = paramInt2;
      case 41:
        paramConstraint.layout.horizontalChainStyle = paramInt2;
      case 38:
        paramConstraint.mViewId = paramInt2;
      case 34:
        paramConstraint.layout.topMargin = paramInt2;
      case 31:
        paramConstraint.layout.startMargin = paramInt2;
      case 28:
        paramConstraint.layout.rightMargin = paramInt2;
      case 27:
        paramConstraint.layout.orientation = paramInt2;
      case 24:
        paramConstraint.layout.leftMargin = paramInt2;
      case 23:
        paramConstraint.layout.mWidth = paramInt2;
      case 22:
        paramConstraint.propertySet.visibility = paramInt2;
      case 21:
        paramConstraint.layout.mHeight = paramInt2;
      case 18:
        paramConstraint.layout.guideEnd = paramInt2;
      case 17:
        paramConstraint.layout.guideBegin = paramInt2;
      case 16:
        paramConstraint.layout.goneTopMargin = paramInt2;
      case 15:
        paramConstraint.layout.goneStartMargin = paramInt2;
      case 14:
        paramConstraint.layout.goneRightMargin = paramInt2;
      case 13:
        paramConstraint.layout.goneLeftMargin = paramInt2;
      case 12:
        paramConstraint.layout.goneEndMargin = paramInt2;
      case 11:
        paramConstraint.layout.goneBottomMargin = paramInt2;
      case 8:
        paramConstraint.layout.endMargin = paramInt2;
      case 7:
        paramConstraint.layout.editorAbsoluteY = paramInt2;
      case 6:
        paramConstraint.layout.editorAbsoluteX = paramInt2;
      case 2:
        break;
    } 
    paramConstraint.layout.bottomMargin = paramInt2;
  }
  
  private static void setDeltaValue(Constraint paramConstraint, int paramInt, String paramString) {
    switch (paramInt) {
      default:
        Log.w("ConstraintSet", "Unknown attribute 0x");
      case 90:
        paramConstraint.motion.mQuantizeInterpolatorString = paramString;
      case 87:
        return;
      case 77:
        paramConstraint.layout.mConstraintTag = paramString;
      case 74:
        paramConstraint.layout.mReferenceIdString = paramString;
        paramConstraint.layout.mReferenceIds = null;
      case 65:
        paramConstraint.motion.mTransitionEasing = paramString;
      case 5:
        break;
    } 
    paramConstraint.layout.dimensionRatio = paramString;
  }
  
  private static void setDeltaValue(Constraint paramConstraint, int paramInt, boolean paramBoolean) {
    switch (paramInt) {
      default:
        Log.w("ConstraintSet", "Unknown attribute 0x");
      case 87:
        return;
      case 81:
        paramConstraint.layout.constrainedHeight = paramBoolean;
      case 80:
        paramConstraint.layout.constrainedWidth = paramBoolean;
      case 75:
        paramConstraint.layout.mBarrierAllowsGoneWidgets = paramBoolean;
      case 44:
        break;
    } 
    paramConstraint.transform.applyElevation = paramBoolean;
  }
  
  private String sideToString(int paramInt) {
    switch (paramInt) {
      default:
        return "undefined";
      case 7:
        return "end";
      case 6:
        return "start";
      case 5:
        return "baseline";
      case 4:
        return "bottom";
      case 3:
        return "top";
      case 2:
        return "right";
      case 1:
        break;
    } 
    return "left";
  }
  
  private static String[] splitString(String paramString) {
    char[] arrayOfChar = paramString.toCharArray();
    ArrayList<String> arrayList = new ArrayList();
    boolean bool = false;
    byte b2 = 0;
    byte b1 = 0;
    while (b1 < arrayOfChar.length) {
      boolean bool1;
      byte b;
      if (arrayOfChar[b1] == ',' && !bool) {
        arrayList.add(new String(arrayOfChar, b2, b1 - b2));
        b = b1 + 1;
        bool1 = bool;
      } else {
        bool1 = bool;
        b = b2;
        if (arrayOfChar[b1] == '"') {
          if (!bool) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
          b = b2;
        } 
      } 
      b1++;
      bool = bool1;
      b2 = b;
    } 
    arrayList.add(new String(arrayOfChar, b2, arrayOfChar.length - b2));
    return arrayList.<String>toArray(new String[arrayList.size()]);
  }
  
  public void addColorAttributes(String... paramVarArgs) {
    addAttributes(ConstraintAttribute.AttributeType.COLOR_TYPE, paramVarArgs);
  }
  
  public void addFloatAttributes(String... paramVarArgs) {
    addAttributes(ConstraintAttribute.AttributeType.FLOAT_TYPE, paramVarArgs);
  }
  
  public void addIntAttributes(String... paramVarArgs) {
    addAttributes(ConstraintAttribute.AttributeType.INT_TYPE, paramVarArgs);
  }
  
  public void addStringAttributes(String... paramVarArgs) {
    addAttributes(ConstraintAttribute.AttributeType.STRING_TYPE, paramVarArgs);
  }
  
  public void addToHorizontalChain(int paramInt1, int paramInt2, int paramInt3) {
    byte b;
    if (paramInt2 == 0) {
      b = 1;
    } else {
      b = 2;
    } 
    connect(paramInt1, 1, paramInt2, b, 0);
    if (paramInt3 == 0) {
      b = 2;
    } else {
      b = 1;
    } 
    connect(paramInt1, 2, paramInt3, b, 0);
    if (paramInt2 != 0)
      connect(paramInt2, 2, paramInt1, 1, 0); 
    if (paramInt3 != 0)
      connect(paramInt3, 1, paramInt1, 2, 0); 
  }
  
  public void addToHorizontalChainRTL(int paramInt1, int paramInt2, int paramInt3) {
    byte b;
    if (paramInt2 == 0) {
      b = 6;
    } else {
      b = 7;
    } 
    connect(paramInt1, 6, paramInt2, b, 0);
    if (paramInt3 == 0) {
      b = 7;
    } else {
      b = 6;
    } 
    connect(paramInt1, 7, paramInt3, b, 0);
    if (paramInt2 != 0)
      connect(paramInt2, 7, paramInt1, 6, 0); 
    if (paramInt3 != 0)
      connect(paramInt3, 6, paramInt1, 7, 0); 
  }
  
  public void addToVerticalChain(int paramInt1, int paramInt2, int paramInt3) {
    byte b;
    if (paramInt2 == 0) {
      b = 3;
    } else {
      b = 4;
    } 
    connect(paramInt1, 3, paramInt2, b, 0);
    if (paramInt3 == 0) {
      b = 4;
    } else {
      b = 3;
    } 
    connect(paramInt1, 4, paramInt3, b, 0);
    if (paramInt2 != 0)
      connect(paramInt2, 4, paramInt1, 3, 0); 
    if (paramInt3 != 0)
      connect(paramInt3, 3, paramInt1, 4, 0); 
  }
  
  public void applyCustomAttributes(ConstraintLayout paramConstraintLayout) {
    int i = paramConstraintLayout.getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = paramConstraintLayout.getChildAt(b);
      int j = view.getId();
      if (!this.mConstraints.containsKey(Integer.valueOf(j))) {
        String str = String.valueOf(Debug.getName(view));
        if (str.length() != 0) {
          str = "id unknown ".concat(str);
        } else {
          str = new String("id unknown ");
        } 
        Log.w("ConstraintSet", str);
      } else if (!this.mForceId || j != -1) {
        if (this.mConstraints.containsKey(Integer.valueOf(j))) {
          Constraint constraint = this.mConstraints.get(Integer.valueOf(j));
          if (constraint != null)
            ConstraintAttribute.setAttributes(view, constraint.mCustomConstraints); 
        } 
      } else {
        throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
      } 
    } 
  }
  
  public void applyDeltaFrom(ConstraintSet paramConstraintSet) {
    for (Constraint constraint : paramConstraintSet.mConstraints.values()) {
      if (constraint.mDelta != null) {
        if (constraint.mTargetString != null) {
          Iterator<Integer> iterator = this.mConstraints.keySet().iterator();
          while (iterator.hasNext()) {
            Constraint constraint2 = getConstraint(((Integer)iterator.next()).intValue());
            if (constraint2.layout.mConstraintTag != null && constraint.mTargetString.matches(constraint2.layout.mConstraintTag)) {
              constraint.mDelta.applyDelta(constraint2);
              constraint2.mCustomConstraints.putAll((HashMap)constraint.mCustomConstraints.clone());
            } 
          } 
          continue;
        } 
        Constraint constraint1 = getConstraint(constraint.mViewId);
        constraint.mDelta.applyDelta(constraint1);
      } 
    } 
  }
  
  public void applyTo(ConstraintLayout paramConstraintLayout) {
    applyToInternal(paramConstraintLayout, true);
    paramConstraintLayout.setConstraintSet((ConstraintSet)null);
    paramConstraintLayout.requestLayout();
  }
  
  public void applyToHelper(ConstraintHelper paramConstraintHelper, ConstraintWidget paramConstraintWidget, ConstraintLayout.LayoutParams paramLayoutParams, SparseArray<ConstraintWidget> paramSparseArray) {
    int i = paramConstraintHelper.getId();
    if (this.mConstraints.containsKey(Integer.valueOf(i))) {
      Constraint constraint = this.mConstraints.get(Integer.valueOf(i));
      if (constraint != null && paramConstraintWidget instanceof HelperWidget)
        paramConstraintHelper.loadParameters(constraint, (HelperWidget)paramConstraintWidget, paramLayoutParams, paramSparseArray); 
    } 
  }
  
  void applyToInternal(ConstraintLayout paramConstraintLayout, boolean paramBoolean) {
    int i = paramConstraintLayout.getChildCount();
    HashSet hashSet = new HashSet(this.mConstraints.keySet());
    byte b;
    for (b = 0; b < i; b++) {
      String str;
      View view = paramConstraintLayout.getChildAt(b);
      int j = view.getId();
      if (!this.mConstraints.containsKey(Integer.valueOf(j))) {
        str = String.valueOf(Debug.getName(view));
        if (str.length() != 0) {
          str = "id unknown ".concat(str);
        } else {
          str = new String("id unknown ");
        } 
        Log.w("ConstraintSet", str);
      } else if (!this.mForceId || j != -1) {
        if (j != -1)
          if (this.mConstraints.containsKey(Integer.valueOf(j))) {
            hashSet.remove(Integer.valueOf(j));
            Constraint constraint = this.mConstraints.get(Integer.valueOf(j));
            if (constraint != null) {
              if (str instanceof Barrier) {
                constraint.layout.mHelperType = 1;
                Barrier barrier = (Barrier)str;
                barrier.setId(j);
                barrier.setType(constraint.layout.mBarrierDirection);
                barrier.setMargin(constraint.layout.mBarrierMargin);
                barrier.setAllowsGoneWidget(constraint.layout.mBarrierAllowsGoneWidgets);
                if (constraint.layout.mReferenceIds != null) {
                  barrier.setReferencedIds(constraint.layout.mReferenceIds);
                } else if (constraint.layout.mReferenceIdString != null) {
                  constraint.layout.mReferenceIds = convertReferenceString(barrier, constraint.layout.mReferenceIdString);
                  barrier.setReferencedIds(constraint.layout.mReferenceIds);
                } 
              } 
              ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)str.getLayoutParams();
              layoutParams.validate();
              constraint.applyTo(layoutParams);
              if (paramBoolean)
                ConstraintAttribute.setAttributes((View)str, constraint.mCustomConstraints); 
              str.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
              if (constraint.propertySet.mVisibilityMode == 0)
                str.setVisibility(constraint.propertySet.visibility); 
              if (Build.VERSION.SDK_INT >= 17) {
                str.setAlpha(constraint.propertySet.alpha);
                str.setRotation(constraint.transform.rotation);
                str.setRotationX(constraint.transform.rotationX);
                str.setRotationY(constraint.transform.rotationY);
                str.setScaleX(constraint.transform.scaleX);
                str.setScaleY(constraint.transform.scaleY);
                if (constraint.transform.transformPivotTarget != -1) {
                  View view1 = ((View)str.getParent()).findViewById(constraint.transform.transformPivotTarget);
                  if (view1 != null) {
                    float f1 = (view1.getTop() + view1.getBottom()) / 2.0F;
                    float f2 = (view1.getLeft() + view1.getRight()) / 2.0F;
                    if (str.getRight() - str.getLeft() > 0 && str.getBottom() - str.getTop() > 0) {
                      float f3 = str.getLeft();
                      float f4 = str.getTop();
                      str.setPivotX(f2 - f3);
                      str.setPivotY(f1 - f4);
                    } 
                  } 
                } else {
                  if (!Float.isNaN(constraint.transform.transformPivotX))
                    str.setPivotX(constraint.transform.transformPivotX); 
                  if (!Float.isNaN(constraint.transform.transformPivotY))
                    str.setPivotY(constraint.transform.transformPivotY); 
                } 
                str.setTranslationX(constraint.transform.translationX);
                str.setTranslationY(constraint.transform.translationY);
                if (Build.VERSION.SDK_INT >= 21) {
                  str.setTranslationZ(constraint.transform.translationZ);
                  if (constraint.transform.applyElevation)
                    str.setElevation(constraint.transform.elevation); 
                } 
              } 
            } 
          } else {
            Log.v("ConstraintSet", (new StringBuilder(43)).append("WARNING NO CONSTRAINTS for view ").append(j).toString());
          }  
      } else {
        throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
      } 
    } 
    for (Integer integer : hashSet) {
      Constraint constraint = this.mConstraints.get(integer);
      if (constraint == null)
        continue; 
      if (constraint.layout.mHelperType == 1) {
        Barrier barrier = new Barrier(paramConstraintLayout.getContext());
        barrier.setId(integer.intValue());
        if (constraint.layout.mReferenceIds != null) {
          barrier.setReferencedIds(constraint.layout.mReferenceIds);
        } else if (constraint.layout.mReferenceIdString != null) {
          constraint.layout.mReferenceIds = convertReferenceString(barrier, constraint.layout.mReferenceIdString);
          barrier.setReferencedIds(constraint.layout.mReferenceIds);
        } 
        barrier.setType(constraint.layout.mBarrierDirection);
        barrier.setMargin(constraint.layout.mBarrierMargin);
        ConstraintLayout.LayoutParams layoutParams = paramConstraintLayout.generateDefaultLayoutParams();
        barrier.validateParams();
        constraint.applyTo(layoutParams);
        paramConstraintLayout.addView(barrier, (ViewGroup.LayoutParams)layoutParams);
      } 
      if (constraint.layout.mIsGuideline) {
        Guideline guideline = new Guideline(paramConstraintLayout.getContext());
        guideline.setId(integer.intValue());
        ConstraintLayout.LayoutParams layoutParams = paramConstraintLayout.generateDefaultLayoutParams();
        constraint.applyTo(layoutParams);
        paramConstraintLayout.addView(guideline, (ViewGroup.LayoutParams)layoutParams);
      } 
    } 
    for (b = 0; b < i; b++) {
      View view = paramConstraintLayout.getChildAt(b);
      if (view instanceof ConstraintHelper)
        ((ConstraintHelper)view).applyLayoutFeaturesInConstraintSet(paramConstraintLayout); 
    } 
  }
  
  public void applyToLayoutParams(int paramInt, ConstraintLayout.LayoutParams paramLayoutParams) {
    if (this.mConstraints.containsKey(Integer.valueOf(paramInt))) {
      Constraint constraint = this.mConstraints.get(Integer.valueOf(paramInt));
      if (constraint != null)
        constraint.applyTo(paramLayoutParams); 
    } 
  }
  
  public void applyToWithoutCustom(ConstraintLayout paramConstraintLayout) {
    applyToInternal(paramConstraintLayout, false);
    paramConstraintLayout.setConstraintSet((ConstraintSet)null);
  }
  
  public void center(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, float paramFloat) {
    if (paramInt4 >= 0) {
      if (paramInt7 >= 0) {
        if (paramFloat > 0.0F && paramFloat <= 1.0F) {
          if (paramInt3 == 1 || paramInt3 == 2) {
            connect(paramInt1, 1, paramInt2, paramInt3, paramInt4);
            connect(paramInt1, 2, paramInt5, paramInt6, paramInt7);
            Constraint constraint1 = this.mConstraints.get(Integer.valueOf(paramInt1));
            if (constraint1 != null)
              constraint1.layout.horizontalBias = paramFloat; 
            return;
          } 
          if (paramInt3 == 6 || paramInt3 == 7) {
            connect(paramInt1, 6, paramInt2, paramInt3, paramInt4);
            connect(paramInt1, 7, paramInt5, paramInt6, paramInt7);
            Constraint constraint1 = this.mConstraints.get(Integer.valueOf(paramInt1));
            if (constraint1 != null)
              constraint1.layout.horizontalBias = paramFloat; 
            return;
          } 
          connect(paramInt1, 3, paramInt2, paramInt3, paramInt4);
          connect(paramInt1, 4, paramInt5, paramInt6, paramInt7);
          Constraint constraint = this.mConstraints.get(Integer.valueOf(paramInt1));
          if (constraint != null)
            constraint.layout.verticalBias = paramFloat; 
          return;
        } 
        throw new IllegalArgumentException("bias must be between 0 and 1 inclusive");
      } 
      throw new IllegalArgumentException("margin must be > 0");
    } 
    throw new IllegalArgumentException("margin must be > 0");
  }
  
  public void centerHorizontally(int paramInt1, int paramInt2) {
    if (paramInt2 == 0) {
      center(paramInt1, 0, 1, 0, 0, 2, 0, 0.5F);
    } else {
      center(paramInt1, paramInt2, 2, 0, paramInt2, 1, 0, 0.5F);
    } 
  }
  
  public void centerHorizontally(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, float paramFloat) {
    connect(paramInt1, 1, paramInt2, paramInt3, paramInt4);
    connect(paramInt1, 2, paramInt5, paramInt6, paramInt7);
    Constraint constraint = this.mConstraints.get(Integer.valueOf(paramInt1));
    if (constraint != null)
      constraint.layout.horizontalBias = paramFloat; 
  }
  
  public void centerHorizontallyRtl(int paramInt1, int paramInt2) {
    if (paramInt2 == 0) {
      center(paramInt1, 0, 6, 0, 0, 7, 0, 0.5F);
    } else {
      center(paramInt1, paramInt2, 7, 0, paramInt2, 6, 0, 0.5F);
    } 
  }
  
  public void centerHorizontallyRtl(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, float paramFloat) {
    connect(paramInt1, 6, paramInt2, paramInt3, paramInt4);
    connect(paramInt1, 7, paramInt5, paramInt6, paramInt7);
    Constraint constraint = this.mConstraints.get(Integer.valueOf(paramInt1));
    if (constraint != null)
      constraint.layout.horizontalBias = paramFloat; 
  }
  
  public void centerVertically(int paramInt1, int paramInt2) {
    if (paramInt2 == 0) {
      center(paramInt1, 0, 3, 0, 0, 4, 0, 0.5F);
    } else {
      center(paramInt1, paramInt2, 4, 0, paramInt2, 3, 0, 0.5F);
    } 
  }
  
  public void centerVertically(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, float paramFloat) {
    connect(paramInt1, 3, paramInt2, paramInt3, paramInt4);
    connect(paramInt1, 4, paramInt5, paramInt6, paramInt7);
    Constraint constraint = this.mConstraints.get(Integer.valueOf(paramInt1));
    if (constraint != null)
      constraint.layout.verticalBias = paramFloat; 
  }
  
  public void clear(int paramInt) {
    this.mConstraints.remove(Integer.valueOf(paramInt));
  }
  
  public void clear(int paramInt1, int paramInt2) {
    if (this.mConstraints.containsKey(Integer.valueOf(paramInt1))) {
      Constraint constraint = this.mConstraints.get(Integer.valueOf(paramInt1));
      if (constraint == null)
        return; 
      switch (paramInt2) {
        default:
          throw new IllegalArgumentException("unknown constraint");
        case 8:
          constraint.layout.circleAngle = -1.0F;
          constraint.layout.circleRadius = -1;
          constraint.layout.circleConstraint = -1;
          return;
        case 7:
          constraint.layout.endToStart = -1;
          constraint.layout.endToEnd = -1;
          constraint.layout.endMargin = 0;
          constraint.layout.goneEndMargin = Integer.MIN_VALUE;
          return;
        case 6:
          constraint.layout.startToEnd = -1;
          constraint.layout.startToStart = -1;
          constraint.layout.startMargin = 0;
          constraint.layout.goneStartMargin = Integer.MIN_VALUE;
          return;
        case 5:
          constraint.layout.baselineToBaseline = -1;
          constraint.layout.baselineToTop = -1;
          constraint.layout.baselineToBottom = -1;
          constraint.layout.baselineMargin = 0;
          constraint.layout.goneBaselineMargin = Integer.MIN_VALUE;
          return;
        case 4:
          constraint.layout.bottomToTop = -1;
          constraint.layout.bottomToBottom = -1;
          constraint.layout.bottomMargin = 0;
          constraint.layout.goneBottomMargin = Integer.MIN_VALUE;
          return;
        case 3:
          constraint.layout.topToBottom = -1;
          constraint.layout.topToTop = -1;
          constraint.layout.topMargin = 0;
          constraint.layout.goneTopMargin = Integer.MIN_VALUE;
          return;
        case 2:
          constraint.layout.rightToRight = -1;
          constraint.layout.rightToLeft = -1;
          constraint.layout.rightMargin = -1;
          constraint.layout.goneRightMargin = Integer.MIN_VALUE;
          return;
        case 1:
          break;
      } 
      constraint.layout.leftToRight = -1;
      constraint.layout.leftToLeft = -1;
      constraint.layout.leftMargin = -1;
      constraint.layout.goneLeftMargin = Integer.MIN_VALUE;
    } 
  }
  
  public void clone(Context paramContext, int paramInt) {
    clone((ConstraintLayout)LayoutInflater.from(paramContext).inflate(paramInt, null));
  }
  
  public void clone(ConstraintLayout paramConstraintLayout) {
    int i = paramConstraintLayout.getChildCount();
    this.mConstraints.clear();
    byte b = 0;
    while (b < i) {
      View view = paramConstraintLayout.getChildAt(b);
      ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)view.getLayoutParams();
      int j = view.getId();
      if (!this.mForceId || j != -1) {
        if (!this.mConstraints.containsKey(Integer.valueOf(j)))
          this.mConstraints.put(Integer.valueOf(j), new Constraint()); 
        Constraint constraint = this.mConstraints.get(Integer.valueOf(j));
        if (constraint != null) {
          constraint.mCustomConstraints = ConstraintAttribute.extractAttributes(this.mSavedAttributes, view);
          constraint.fillFrom(j, layoutParams);
          constraint.propertySet.visibility = view.getVisibility();
          if (Build.VERSION.SDK_INT >= 17) {
            constraint.propertySet.alpha = view.getAlpha();
            constraint.transform.rotation = view.getRotation();
            constraint.transform.rotationX = view.getRotationX();
            constraint.transform.rotationY = view.getRotationY();
            constraint.transform.scaleX = view.getScaleX();
            constraint.transform.scaleY = view.getScaleY();
            float f1 = view.getPivotX();
            float f2 = view.getPivotY();
            if (f1 != 0.0D || f2 != 0.0D) {
              constraint.transform.transformPivotX = f1;
              constraint.transform.transformPivotY = f2;
            } 
            constraint.transform.translationX = view.getTranslationX();
            constraint.transform.translationY = view.getTranslationY();
            if (Build.VERSION.SDK_INT >= 21) {
              constraint.transform.translationZ = view.getTranslationZ();
              if (constraint.transform.applyElevation)
                constraint.transform.elevation = view.getElevation(); 
            } 
          } 
          if (view instanceof Barrier) {
            Barrier barrier = (Barrier)view;
            constraint.layout.mBarrierAllowsGoneWidgets = barrier.getAllowsGoneWidget();
            constraint.layout.mReferenceIds = barrier.getReferencedIds();
            constraint.layout.mBarrierDirection = barrier.getType();
            constraint.layout.mBarrierMargin = barrier.getMargin();
          } 
        } 
        b++;
        continue;
      } 
      throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
    } 
  }
  
  public void clone(ConstraintSet paramConstraintSet) {
    this.mConstraints.clear();
    for (Integer integer : paramConstraintSet.mConstraints.keySet()) {
      Constraint constraint = paramConstraintSet.mConstraints.get(integer);
      if (constraint == null)
        continue; 
      this.mConstraints.put(integer, constraint.clone());
    } 
  }
  
  public void clone(Constraints paramConstraints) {
    int i = paramConstraints.getChildCount();
    this.mConstraints.clear();
    byte b = 0;
    while (b < i) {
      View view = paramConstraints.getChildAt(b);
      Constraints.LayoutParams layoutParams = (Constraints.LayoutParams)view.getLayoutParams();
      int j = view.getId();
      if (!this.mForceId || j != -1) {
        if (!this.mConstraints.containsKey(Integer.valueOf(j)))
          this.mConstraints.put(Integer.valueOf(j), new Constraint()); 
        Constraint constraint = this.mConstraints.get(Integer.valueOf(j));
        if (constraint != null) {
          if (view instanceof ConstraintHelper)
            constraint.fillFromConstraints((ConstraintHelper)view, j, layoutParams); 
          constraint.fillFromConstraints(j, layoutParams);
        } 
        b++;
        continue;
      } 
      throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
    } 
  }
  
  public void connect(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    String str1;
    String str2;
    if (!this.mConstraints.containsKey(Integer.valueOf(paramInt1)))
      this.mConstraints.put(Integer.valueOf(paramInt1), new Constraint()); 
    Constraint constraint = this.mConstraints.get(Integer.valueOf(paramInt1));
    if (constraint == null)
      return; 
    switch (paramInt2) {
      default:
        str1 = sideToString(paramInt2);
        str2 = sideToString(paramInt4);
        throw new IllegalArgumentException((new StringBuilder(String.valueOf(str1).length() + 12 + String.valueOf(str2).length())).append(str1).append(" to ").append(str2).append(" unknown").toString());
      case 7:
        if (paramInt4 == 7) {
          ((Constraint)str1).layout.endToEnd = paramInt3;
          ((Constraint)str1).layout.endToStart = -1;
        } else if (paramInt4 == 6) {
          ((Constraint)str1).layout.endToStart = paramInt3;
          ((Constraint)str1).layout.endToEnd = -1;
        } else {
          str1 = sideToString(paramInt4);
          throw new IllegalArgumentException((new StringBuilder(String.valueOf(str1).length() + 19)).append("right to ").append(str1).append(" undefined").toString());
        } 
        return;
      case 6:
        if (paramInt4 == 6) {
          ((Constraint)str1).layout.startToStart = paramInt3;
          ((Constraint)str1).layout.startToEnd = -1;
        } else if (paramInt4 == 7) {
          ((Constraint)str1).layout.startToEnd = paramInt3;
          ((Constraint)str1).layout.startToStart = -1;
        } else {
          str1 = sideToString(paramInt4);
          throw new IllegalArgumentException((new StringBuilder(String.valueOf(str1).length() + 19)).append("right to ").append(str1).append(" undefined").toString());
        } 
        return;
      case 5:
        if (paramInt4 == 5) {
          ((Constraint)str1).layout.baselineToBaseline = paramInt3;
          ((Constraint)str1).layout.bottomToBottom = -1;
          ((Constraint)str1).layout.bottomToTop = -1;
          ((Constraint)str1).layout.topToTop = -1;
          ((Constraint)str1).layout.topToBottom = -1;
        } else if (paramInt4 == 3) {
          ((Constraint)str1).layout.baselineToTop = paramInt3;
          ((Constraint)str1).layout.bottomToBottom = -1;
          ((Constraint)str1).layout.bottomToTop = -1;
          ((Constraint)str1).layout.topToTop = -1;
          ((Constraint)str1).layout.topToBottom = -1;
        } else if (paramInt4 == 4) {
          ((Constraint)str1).layout.baselineToBottom = paramInt3;
          ((Constraint)str1).layout.bottomToBottom = -1;
          ((Constraint)str1).layout.bottomToTop = -1;
          ((Constraint)str1).layout.topToTop = -1;
          ((Constraint)str1).layout.topToBottom = -1;
        } else {
          str1 = sideToString(paramInt4);
          throw new IllegalArgumentException((new StringBuilder(String.valueOf(str1).length() + 19)).append("right to ").append(str1).append(" undefined").toString());
        } 
        return;
      case 4:
        if (paramInt4 == 4) {
          ((Constraint)str1).layout.bottomToBottom = paramInt3;
          ((Constraint)str1).layout.bottomToTop = -1;
          ((Constraint)str1).layout.baselineToBaseline = -1;
          ((Constraint)str1).layout.baselineToTop = -1;
          ((Constraint)str1).layout.baselineToBottom = -1;
        } else if (paramInt4 == 3) {
          ((Constraint)str1).layout.bottomToTop = paramInt3;
          ((Constraint)str1).layout.bottomToBottom = -1;
          ((Constraint)str1).layout.baselineToBaseline = -1;
          ((Constraint)str1).layout.baselineToTop = -1;
          ((Constraint)str1).layout.baselineToBottom = -1;
        } else {
          str1 = sideToString(paramInt4);
          throw new IllegalArgumentException((new StringBuilder(String.valueOf(str1).length() + 19)).append("right to ").append(str1).append(" undefined").toString());
        } 
        return;
      case 3:
        if (paramInt4 == 3) {
          ((Constraint)str1).layout.topToTop = paramInt3;
          ((Constraint)str1).layout.topToBottom = -1;
          ((Constraint)str1).layout.baselineToBaseline = -1;
          ((Constraint)str1).layout.baselineToTop = -1;
          ((Constraint)str1).layout.baselineToBottom = -1;
        } else if (paramInt4 == 4) {
          ((Constraint)str1).layout.topToBottom = paramInt3;
          ((Constraint)str1).layout.topToTop = -1;
          ((Constraint)str1).layout.baselineToBaseline = -1;
          ((Constraint)str1).layout.baselineToTop = -1;
          ((Constraint)str1).layout.baselineToBottom = -1;
        } else {
          str1 = sideToString(paramInt4);
          throw new IllegalArgumentException((new StringBuilder(String.valueOf(str1).length() + 19)).append("right to ").append(str1).append(" undefined").toString());
        } 
        return;
      case 2:
        if (paramInt4 == 1) {
          ((Constraint)str1).layout.rightToLeft = paramInt3;
          ((Constraint)str1).layout.rightToRight = -1;
        } else if (paramInt4 == 2) {
          ((Constraint)str1).layout.rightToRight = paramInt3;
          ((Constraint)str1).layout.rightToLeft = -1;
        } else {
          str1 = sideToString(paramInt4);
          throw new IllegalArgumentException((new StringBuilder(String.valueOf(str1).length() + 19)).append("right to ").append(str1).append(" undefined").toString());
        } 
        return;
      case 1:
        break;
    } 
    if (paramInt4 == 1) {
      ((Constraint)str1).layout.leftToLeft = paramInt3;
      ((Constraint)str1).layout.leftToRight = -1;
    } else {
      if (paramInt4 == 2) {
        ((Constraint)str1).layout.leftToRight = paramInt3;
        ((Constraint)str1).layout.leftToLeft = -1;
        return;
      } 
      str1 = sideToString(paramInt4);
      throw new IllegalArgumentException((new StringBuilder(String.valueOf(str1).length() + 18)).append("left to ").append(str1).append(" undefined").toString());
    } 
  }
  
  public void connect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    String str1;
    String str2;
    if (!this.mConstraints.containsKey(Integer.valueOf(paramInt1)))
      this.mConstraints.put(Integer.valueOf(paramInt1), new Constraint()); 
    Constraint constraint = this.mConstraints.get(Integer.valueOf(paramInt1));
    if (constraint == null)
      return; 
    switch (paramInt2) {
      default:
        str1 = sideToString(paramInt2);
        str2 = sideToString(paramInt4);
        throw new IllegalArgumentException((new StringBuilder(String.valueOf(str1).length() + 12 + String.valueOf(str2).length())).append(str1).append(" to ").append(str2).append(" unknown").toString());
      case 7:
        if (paramInt4 == 7) {
          ((Constraint)str1).layout.endToEnd = paramInt3;
          ((Constraint)str1).layout.endToStart = -1;
        } else if (paramInt4 == 6) {
          ((Constraint)str1).layout.endToStart = paramInt3;
          ((Constraint)str1).layout.endToEnd = -1;
        } else {
          str1 = sideToString(paramInt4);
          throw new IllegalArgumentException((new StringBuilder(String.valueOf(str1).length() + 19)).append("right to ").append(str1).append(" undefined").toString());
        } 
        ((Constraint)str1).layout.endMargin = paramInt5;
        return;
      case 6:
        if (paramInt4 == 6) {
          ((Constraint)str1).layout.startToStart = paramInt3;
          ((Constraint)str1).layout.startToEnd = -1;
        } else if (paramInt4 == 7) {
          ((Constraint)str1).layout.startToEnd = paramInt3;
          ((Constraint)str1).layout.startToStart = -1;
        } else {
          str1 = sideToString(paramInt4);
          throw new IllegalArgumentException((new StringBuilder(String.valueOf(str1).length() + 19)).append("right to ").append(str1).append(" undefined").toString());
        } 
        ((Constraint)str1).layout.startMargin = paramInt5;
        return;
      case 5:
        if (paramInt4 == 5) {
          ((Constraint)str1).layout.baselineToBaseline = paramInt3;
          ((Constraint)str1).layout.bottomToBottom = -1;
          ((Constraint)str1).layout.bottomToTop = -1;
          ((Constraint)str1).layout.topToTop = -1;
          ((Constraint)str1).layout.topToBottom = -1;
        } else if (paramInt4 == 3) {
          ((Constraint)str1).layout.baselineToTop = paramInt3;
          ((Constraint)str1).layout.bottomToBottom = -1;
          ((Constraint)str1).layout.bottomToTop = -1;
          ((Constraint)str1).layout.topToTop = -1;
          ((Constraint)str1).layout.topToBottom = -1;
        } else if (paramInt4 == 4) {
          ((Constraint)str1).layout.baselineToBottom = paramInt3;
          ((Constraint)str1).layout.bottomToBottom = -1;
          ((Constraint)str1).layout.bottomToTop = -1;
          ((Constraint)str1).layout.topToTop = -1;
          ((Constraint)str1).layout.topToBottom = -1;
        } else {
          str1 = sideToString(paramInt4);
          throw new IllegalArgumentException((new StringBuilder(String.valueOf(str1).length() + 19)).append("right to ").append(str1).append(" undefined").toString());
        } 
        return;
      case 4:
        if (paramInt4 == 4) {
          ((Constraint)str1).layout.bottomToBottom = paramInt3;
          ((Constraint)str1).layout.bottomToTop = -1;
          ((Constraint)str1).layout.baselineToBaseline = -1;
          ((Constraint)str1).layout.baselineToTop = -1;
          ((Constraint)str1).layout.baselineToBottom = -1;
        } else if (paramInt4 == 3) {
          ((Constraint)str1).layout.bottomToTop = paramInt3;
          ((Constraint)str1).layout.bottomToBottom = -1;
          ((Constraint)str1).layout.baselineToBaseline = -1;
          ((Constraint)str1).layout.baselineToTop = -1;
          ((Constraint)str1).layout.baselineToBottom = -1;
        } else {
          str1 = sideToString(paramInt4);
          throw new IllegalArgumentException((new StringBuilder(String.valueOf(str1).length() + 19)).append("right to ").append(str1).append(" undefined").toString());
        } 
        ((Constraint)str1).layout.bottomMargin = paramInt5;
        return;
      case 3:
        if (paramInt4 == 3) {
          ((Constraint)str1).layout.topToTop = paramInt3;
          ((Constraint)str1).layout.topToBottom = -1;
          ((Constraint)str1).layout.baselineToBaseline = -1;
          ((Constraint)str1).layout.baselineToTop = -1;
          ((Constraint)str1).layout.baselineToBottom = -1;
        } else if (paramInt4 == 4) {
          ((Constraint)str1).layout.topToBottom = paramInt3;
          ((Constraint)str1).layout.topToTop = -1;
          ((Constraint)str1).layout.baselineToBaseline = -1;
          ((Constraint)str1).layout.baselineToTop = -1;
          ((Constraint)str1).layout.baselineToBottom = -1;
        } else {
          str1 = sideToString(paramInt4);
          throw new IllegalArgumentException((new StringBuilder(String.valueOf(str1).length() + 19)).append("right to ").append(str1).append(" undefined").toString());
        } 
        ((Constraint)str1).layout.topMargin = paramInt5;
        return;
      case 2:
        if (paramInt4 == 1) {
          ((Constraint)str1).layout.rightToLeft = paramInt3;
          ((Constraint)str1).layout.rightToRight = -1;
        } else if (paramInt4 == 2) {
          ((Constraint)str1).layout.rightToRight = paramInt3;
          ((Constraint)str1).layout.rightToLeft = -1;
        } else {
          str1 = sideToString(paramInt4);
          throw new IllegalArgumentException((new StringBuilder(String.valueOf(str1).length() + 19)).append("right to ").append(str1).append(" undefined").toString());
        } 
        ((Constraint)str1).layout.rightMargin = paramInt5;
        return;
      case 1:
        break;
    } 
    if (paramInt4 == 1) {
      ((Constraint)str1).layout.leftToLeft = paramInt3;
      ((Constraint)str1).layout.leftToRight = -1;
    } else if (paramInt4 == 2) {
      ((Constraint)str1).layout.leftToRight = paramInt3;
      ((Constraint)str1).layout.leftToLeft = -1;
    } else {
      str1 = sideToString(paramInt4);
      throw new IllegalArgumentException((new StringBuilder(String.valueOf(str1).length() + 18)).append("Left to ").append(str1).append(" undefined").toString());
    } 
    ((Constraint)str1).layout.leftMargin = paramInt5;
  }
  
  public void constrainCircle(int paramInt1, int paramInt2, int paramInt3, float paramFloat) {
    Constraint constraint = get(paramInt1);
    constraint.layout.circleConstraint = paramInt2;
    constraint.layout.circleRadius = paramInt3;
    constraint.layout.circleAngle = paramFloat;
  }
  
  public void constrainDefaultHeight(int paramInt1, int paramInt2) {
    (get(paramInt1)).layout.heightDefault = paramInt2;
  }
  
  public void constrainDefaultWidth(int paramInt1, int paramInt2) {
    (get(paramInt1)).layout.widthDefault = paramInt2;
  }
  
  public void constrainHeight(int paramInt1, int paramInt2) {
    (get(paramInt1)).layout.mHeight = paramInt2;
  }
  
  public void constrainMaxHeight(int paramInt1, int paramInt2) {
    (get(paramInt1)).layout.heightMax = paramInt2;
  }
  
  public void constrainMaxWidth(int paramInt1, int paramInt2) {
    (get(paramInt1)).layout.widthMax = paramInt2;
  }
  
  public void constrainMinHeight(int paramInt1, int paramInt2) {
    (get(paramInt1)).layout.heightMin = paramInt2;
  }
  
  public void constrainMinWidth(int paramInt1, int paramInt2) {
    (get(paramInt1)).layout.widthMin = paramInt2;
  }
  
  public void constrainPercentHeight(int paramInt, float paramFloat) {
    (get(paramInt)).layout.heightPercent = paramFloat;
  }
  
  public void constrainPercentWidth(int paramInt, float paramFloat) {
    (get(paramInt)).layout.widthPercent = paramFloat;
  }
  
  public void constrainWidth(int paramInt1, int paramInt2) {
    (get(paramInt1)).layout.mWidth = paramInt2;
  }
  
  public void constrainedHeight(int paramInt, boolean paramBoolean) {
    (get(paramInt)).layout.constrainedHeight = paramBoolean;
  }
  
  public void constrainedWidth(int paramInt, boolean paramBoolean) {
    (get(paramInt)).layout.constrainedWidth = paramBoolean;
  }
  
  public void create(int paramInt1, int paramInt2) {
    Constraint constraint = get(paramInt1);
    constraint.layout.mIsGuideline = true;
    constraint.layout.orientation = paramInt2;
  }
  
  public void createBarrier(int paramInt1, int paramInt2, int paramInt3, int... paramVarArgs) {
    Constraint constraint = get(paramInt1);
    constraint.layout.mHelperType = 1;
    constraint.layout.mBarrierDirection = paramInt2;
    constraint.layout.mBarrierMargin = paramInt3;
    constraint.layout.mIsGuideline = false;
    constraint.layout.mReferenceIds = paramVarArgs;
  }
  
  public void createHorizontalChain(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint, float[] paramArrayOffloat, int paramInt5) {
    createHorizontalChain(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfint, paramArrayOffloat, paramInt5, 1, 2);
  }
  
  public void createHorizontalChainRtl(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint, float[] paramArrayOffloat, int paramInt5) {
    createHorizontalChain(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfint, paramArrayOffloat, paramInt5, 6, 7);
  }
  
  public void createVerticalChain(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint, float[] paramArrayOffloat, int paramInt5) {
    if (paramArrayOfint.length >= 2) {
      if (paramArrayOffloat == null || paramArrayOffloat.length == paramArrayOfint.length) {
        if (paramArrayOffloat != null)
          (get(paramArrayOfint[0])).layout.verticalWeight = paramArrayOffloat[0]; 
        (get(paramArrayOfint[0])).layout.verticalChainStyle = paramInt5;
        connect(paramArrayOfint[0], 3, paramInt1, paramInt2, 0);
        for (paramInt1 = 1; paramInt1 < paramArrayOfint.length; paramInt1++) {
          paramInt2 = paramArrayOfint[paramInt1];
          connect(paramArrayOfint[paramInt1], 3, paramArrayOfint[paramInt1 - 1], 4, 0);
          connect(paramArrayOfint[paramInt1 - 1], 4, paramArrayOfint[paramInt1], 3, 0);
          if (paramArrayOffloat != null)
            (get(paramArrayOfint[paramInt1])).layout.verticalWeight = paramArrayOffloat[paramInt1]; 
        } 
        connect(paramArrayOfint[paramArrayOfint.length - 1], 4, paramInt3, paramInt4, 0);
        return;
      } 
      throw new IllegalArgumentException("must have 2 or more widgets in a chain");
    } 
    throw new IllegalArgumentException("must have 2 or more widgets in a chain");
  }
  
  public void dump(MotionScene paramMotionScene, int... paramVarArgs) {
    Set<Integer> set = this.mConstraints.keySet();
    int i = paramVarArgs.length;
    boolean bool = false;
    if (i != 0) {
      HashSet<Integer> hashSet = new HashSet();
      int k = paramVarArgs.length;
      i = 0;
      while (true) {
        set = hashSet;
        if (i < k) {
          hashSet.add(Integer.valueOf(paramVarArgs[i]));
          i++;
          continue;
        } 
        break;
      } 
    } else {
      set = new HashSet<>(set);
    } 
    PrintStream printStream = System.out;
    i = set.size();
    printStream.println((new StringBuilder(23)).append(i).append(" constraints").toString());
    StringBuilder stringBuilder = new StringBuilder();
    Integer[] arrayOfInteger = (Integer[])set.toArray((Object[])new Integer[0]);
    int j = arrayOfInteger.length;
    for (i = bool; i < j; i++) {
      Integer integer = arrayOfInteger[i];
      Constraint constraint = this.mConstraints.get(integer);
      if (constraint != null) {
        stringBuilder.append("<Constraint id=");
        stringBuilder.append(integer);
        stringBuilder.append(" \n");
        constraint.layout.dump(paramMotionScene, stringBuilder);
        stringBuilder.append("/>\n");
      } 
    } 
    System.out.println(stringBuilder.toString());
  }
  
  public boolean getApplyElevation(int paramInt) {
    return (get(paramInt)).transform.applyElevation;
  }
  
  public Constraint getConstraint(int paramInt) {
    return this.mConstraints.containsKey(Integer.valueOf(paramInt)) ? this.mConstraints.get(Integer.valueOf(paramInt)) : null;
  }
  
  public HashMap<String, ConstraintAttribute> getCustomAttributeSet() {
    return this.mSavedAttributes;
  }
  
  public int getHeight(int paramInt) {
    return (get(paramInt)).layout.mHeight;
  }
  
  public int[] getKnownIds() {
    Integer[] arrayOfInteger = (Integer[])this.mConstraints.keySet().toArray((Object[])new Integer[0]);
    int[] arrayOfInt = new int[arrayOfInteger.length];
    for (byte b = 0; b < arrayOfInt.length; b++)
      arrayOfInt[b] = arrayOfInteger[b].intValue(); 
    return arrayOfInt;
  }
  
  public Constraint getParameters(int paramInt) {
    return get(paramInt);
  }
  
  public int[] getReferencedIds(int paramInt) {
    Constraint constraint = get(paramInt);
    return (constraint.layout.mReferenceIds == null) ? new int[0] : Arrays.copyOf(constraint.layout.mReferenceIds, constraint.layout.mReferenceIds.length);
  }
  
  public int getVisibility(int paramInt) {
    return (get(paramInt)).propertySet.visibility;
  }
  
  public int getVisibilityMode(int paramInt) {
    return (get(paramInt)).propertySet.mVisibilityMode;
  }
  
  public int getWidth(int paramInt) {
    return (get(paramInt)).layout.mWidth;
  }
  
  public boolean isForceId() {
    return this.mForceId;
  }
  
  public void load(Context paramContext, int paramInt) {
    XmlResourceParser xmlResourceParser = paramContext.getResources().getXml(paramInt);
    try {
      for (paramInt = xmlResourceParser.getEventType(); paramInt != 1; paramInt = xmlResourceParser.next()) {
        String str;
        Constraint constraint;
        switch (paramInt) {
          case 2:
            str = xmlResourceParser.getName();
            constraint = fillFromAttributeList(paramContext, Xml.asAttributeSet((XmlPullParser)xmlResourceParser), false);
            if (str.equalsIgnoreCase("Guideline"))
              constraint.layout.mIsGuideline = true; 
            this.mConstraints.put(Integer.valueOf(constraint.mViewId), constraint);
            break;
          case 0:
            xmlResourceParser.getName();
            break;
        } 
      } 
    } catch (XmlPullParserException xmlPullParserException) {
      xmlPullParserException.printStackTrace();
    } catch (IOException iOException) {
      iOException.printStackTrace();
    } 
  }
  
  public void load(Context paramContext, XmlPullParser paramXmlPullParser) {
    Constraint constraint = null;
    try {
      int i = paramXmlPullParser.getEventType();
      while (true) {
        boolean bool = true;
        if (i != 1) {
          RuntimeException runtimeException;
          StringBuilder stringBuilder;
          boolean bool1;
          String str;
          byte b = 3;
          switch (i) {
            case 3:
              str = paramXmlPullParser.getName().toLowerCase(Locale.ROOT);
              switch (str.hashCode()) {
                default:
                  i = -1;
                  break;
                case 2146106725:
                  if (str.equals("constraintset")) {
                    i = 0;
                    break;
                  } 
                case 426575017:
                  if (str.equals("constraintoverride")) {
                    i = 2;
                    break;
                  } 
                case -190376483:
                  if (str.equals("constraint")) {
                    i = bool;
                    break;
                  } 
                case -2075718416:
                  if (str.equals("guideline")) {
                    i = 3;
                    break;
                  } 
              } 
              switch (i) {
                default:
                  break;
                case 1:
                case 2:
                case 3:
                  this.mConstraints.put(Integer.valueOf(constraint.mViewId), constraint);
                  constraint = null;
                  break;
                case 0:
                  break;
              } 
              return;
            case 2:
              str = paramXmlPullParser.getName();
              switch (str.hashCode()) {
                default:
                  i = -1;
                  break;
                case 1803088381:
                  if (str.equals("Constraint")) {
                    i = 0;
                    break;
                  } 
                case 1791837707:
                  if (str.equals("CustomAttribute")) {
                    i = 8;
                    break;
                  } 
                case 1331510167:
                  if (str.equals("Barrier")) {
                    i = b;
                    break;
                  } 
                case 366511058:
                  if (str.equals("CustomMethod")) {
                    i = 9;
                    break;
                  } 
                case -71750448:
                  if (str.equals("Guideline")) {
                    i = 2;
                    break;
                  } 
                case -1238332596:
                  if (str.equals("Transform")) {
                    i = 5;
                    break;
                  } 
                case -1269513683:
                  if (str.equals("PropertySet")) {
                    i = 4;
                    break;
                  } 
                case -1962203927:
                  if (str.equals("ConstraintOverride")) {
                    i = 1;
                    break;
                  } 
                case -1984451626:
                  if (str.equals("Motion")) {
                    i = 7;
                    break;
                  } 
                case -2025855158:
                  bool1 = str.equals("Layout");
                  if (bool1) {
                    i = 6;
                    break;
                  } 
              } 
              switch (i) {
                default:
                  break;
                case 8:
                case 9:
                  if (constraint != null) {
                    ConstraintAttribute.parse(paramContext, paramXmlPullParser, constraint.mCustomConstraints);
                    break;
                  } 
                  runtimeException = new RuntimeException();
                  i = paramXmlPullParser.getLineNumber();
                  stringBuilder = new StringBuilder();
                  this(56);
                  this(stringBuilder.append("XML parser error must be within a Constraint ").append(i).toString());
                  throw runtimeException;
                case 7:
                  if (constraint != null) {
                    constraint.motion.fillFromAttributeList((Context)runtimeException, Xml.asAttributeSet((XmlPullParser)stringBuilder));
                    break;
                  } 
                  runtimeException = new RuntimeException();
                  i = stringBuilder.getLineNumber();
                  stringBuilder = new StringBuilder();
                  this(56);
                  this(stringBuilder.append("XML parser error must be within a Constraint ").append(i).toString());
                  throw runtimeException;
                case 6:
                  if (constraint != null) {
                    constraint.layout.fillFromAttributeList((Context)runtimeException, Xml.asAttributeSet((XmlPullParser)stringBuilder));
                    break;
                  } 
                  runtimeException = new RuntimeException();
                  i = stringBuilder.getLineNumber();
                  stringBuilder = new StringBuilder();
                  this(56);
                  this(stringBuilder.append("XML parser error must be within a Constraint ").append(i).toString());
                  throw runtimeException;
                case 5:
                  if (constraint != null) {
                    constraint.transform.fillFromAttributeList((Context)runtimeException, Xml.asAttributeSet((XmlPullParser)stringBuilder));
                    break;
                  } 
                  runtimeException = new RuntimeException();
                  i = stringBuilder.getLineNumber();
                  stringBuilder = new StringBuilder();
                  this(56);
                  this(stringBuilder.append("XML parser error must be within a Constraint ").append(i).toString());
                  throw runtimeException;
                case 4:
                  if (constraint != null) {
                    constraint.propertySet.fillFromAttributeList((Context)runtimeException, Xml.asAttributeSet((XmlPullParser)stringBuilder));
                    break;
                  } 
                  runtimeException = new RuntimeException();
                  i = stringBuilder.getLineNumber();
                  stringBuilder = new StringBuilder();
                  this(56);
                  this(stringBuilder.append("XML parser error must be within a Constraint ").append(i).toString());
                  throw runtimeException;
                case 3:
                  constraint = fillFromAttributeList((Context)runtimeException, Xml.asAttributeSet((XmlPullParser)stringBuilder), false);
                  constraint.layout.mHelperType = 1;
                  break;
                case 2:
                  constraint = fillFromAttributeList((Context)runtimeException, Xml.asAttributeSet((XmlPullParser)stringBuilder), false);
                  constraint.layout.mIsGuideline = true;
                  constraint.layout.mApply = true;
                  break;
                case 1:
                  constraint = fillFromAttributeList((Context)runtimeException, Xml.asAttributeSet((XmlPullParser)stringBuilder), true);
                  break;
                case 0:
                  break;
              } 
              constraint = fillFromAttributeList((Context)runtimeException, Xml.asAttributeSet((XmlPullParser)stringBuilder), false);
              break;
            case 0:
              stringBuilder.getName();
              break;
          } 
          i = stringBuilder.next();
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
  
  public void parseColorAttributes(Constraint paramConstraint, String paramString) {
    String[] arrayOfString = paramString.split(",");
    for (byte b = 0; b < arrayOfString.length; b++) {
      String str;
      String[] arrayOfString1 = arrayOfString[b].split("=");
      if (arrayOfString1.length != 2) {
        str = String.valueOf(arrayOfString[b]);
        if (str.length() != 0) {
          str = " Unable to parse ".concat(str);
        } else {
          str = new String(" Unable to parse ");
        } 
        Log.w("ConstraintSet", str);
      } else {
        paramConstraint.setColorValue(str[0], Color.parseColor(str[1]));
      } 
    } 
  }
  
  public void parseFloatAttributes(Constraint paramConstraint, String paramString) {
    String[] arrayOfString = paramString.split(",");
    for (byte b = 0; b < arrayOfString.length; b++) {
      String str;
      String[] arrayOfString1 = arrayOfString[b].split("=");
      if (arrayOfString1.length != 2) {
        str = String.valueOf(arrayOfString[b]);
        if (str.length() != 0) {
          str = " Unable to parse ".concat(str);
        } else {
          str = new String(" Unable to parse ");
        } 
        Log.w("ConstraintSet", str);
      } else {
        paramConstraint.setFloatValue(str[0], Float.parseFloat(str[1]));
      } 
    } 
  }
  
  public void parseIntAttributes(Constraint paramConstraint, String paramString) {
    String[] arrayOfString = paramString.split(",");
    for (byte b = 0; b < arrayOfString.length; b++) {
      String str;
      String[] arrayOfString1 = arrayOfString[b].split("=");
      if (arrayOfString1.length != 2) {
        str = String.valueOf(arrayOfString[b]);
        if (str.length() != 0) {
          str = " Unable to parse ".concat(str);
        } else {
          str = new String(" Unable to parse ");
        } 
        Log.w("ConstraintSet", str);
      } else {
        paramConstraint.setFloatValue(str[0], Integer.decode(str[1]).intValue());
      } 
    } 
  }
  
  public void parseStringAttributes(Constraint paramConstraint, String paramString) {
    String[] arrayOfString = splitString(paramString);
    for (byte b = 0; b < arrayOfString.length; b++) {
      String[] arrayOfString1 = arrayOfString[b].split("=");
      paramString = String.valueOf(arrayOfString[b]);
      if (paramString.length() != 0) {
        paramString = " Unable to parse ".concat(paramString);
      } else {
        paramString = new String(" Unable to parse ");
      } 
      Log.w("ConstraintSet", paramString);
      paramConstraint.setStringValue(arrayOfString1[0], arrayOfString1[1]);
    } 
  }
  
  public void readFallback(ConstraintLayout paramConstraintLayout) {
    int i = paramConstraintLayout.getChildCount();
    byte b = 0;
    while (b < i) {
      View view = paramConstraintLayout.getChildAt(b);
      ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)view.getLayoutParams();
      int j = view.getId();
      if (!this.mForceId || j != -1) {
        if (!this.mConstraints.containsKey(Integer.valueOf(j)))
          this.mConstraints.put(Integer.valueOf(j), new Constraint()); 
        Constraint constraint = this.mConstraints.get(Integer.valueOf(j));
        if (constraint != null) {
          if (!constraint.layout.mApply) {
            constraint.fillFrom(j, layoutParams);
            if (view instanceof ConstraintHelper) {
              constraint.layout.mReferenceIds = ((ConstraintHelper)view).getReferencedIds();
              if (view instanceof Barrier) {
                Barrier barrier = (Barrier)view;
                constraint.layout.mBarrierAllowsGoneWidgets = barrier.getAllowsGoneWidget();
                constraint.layout.mBarrierDirection = barrier.getType();
                constraint.layout.mBarrierMargin = barrier.getMargin();
              } 
            } 
            constraint.layout.mApply = true;
          } 
          if (!constraint.propertySet.mApply) {
            constraint.propertySet.visibility = view.getVisibility();
            constraint.propertySet.alpha = view.getAlpha();
            constraint.propertySet.mApply = true;
          } 
          if (Build.VERSION.SDK_INT >= 17 && !constraint.transform.mApply) {
            constraint.transform.mApply = true;
            constraint.transform.rotation = view.getRotation();
            constraint.transform.rotationX = view.getRotationX();
            constraint.transform.rotationY = view.getRotationY();
            constraint.transform.scaleX = view.getScaleX();
            constraint.transform.scaleY = view.getScaleY();
            float f2 = view.getPivotX();
            float f1 = view.getPivotY();
            if (f2 != 0.0D || f1 != 0.0D) {
              constraint.transform.transformPivotX = f2;
              constraint.transform.transformPivotY = f1;
            } 
            constraint.transform.translationX = view.getTranslationX();
            constraint.transform.translationY = view.getTranslationY();
            if (Build.VERSION.SDK_INT >= 21) {
              constraint.transform.translationZ = view.getTranslationZ();
              if (constraint.transform.applyElevation)
                constraint.transform.elevation = view.getElevation(); 
            } 
          } 
        } 
        b++;
        continue;
      } 
      throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
    } 
  }
  
  public void readFallback(ConstraintSet paramConstraintSet) {
    for (Integer integer : paramConstraintSet.mConstraints.keySet()) {
      int i = integer.intValue();
      Constraint constraint1 = paramConstraintSet.mConstraints.get(integer);
      if (!this.mConstraints.containsKey(Integer.valueOf(i)))
        this.mConstraints.put(Integer.valueOf(i), new Constraint()); 
      Constraint constraint2 = this.mConstraints.get(Integer.valueOf(i));
      if (constraint2 == null)
        continue; 
      if (!constraint2.layout.mApply)
        constraint2.layout.copyFrom(constraint1.layout); 
      if (!constraint2.propertySet.mApply)
        constraint2.propertySet.copyFrom(constraint1.propertySet); 
      if (!constraint2.transform.mApply)
        constraint2.transform.copyFrom(constraint1.transform); 
      if (!constraint2.motion.mApply)
        constraint2.motion.copyFrom(constraint1.motion); 
      for (String str : constraint1.mCustomConstraints.keySet()) {
        if (!constraint2.mCustomConstraints.containsKey(str))
          constraint2.mCustomConstraints.put(str, constraint1.mCustomConstraints.get(str)); 
      } 
    } 
  }
  
  public void removeAttribute(String paramString) {
    this.mSavedAttributes.remove(paramString);
  }
  
  public void removeFromHorizontalChain(int paramInt) {
    if (this.mConstraints.containsKey(Integer.valueOf(paramInt))) {
      Constraint constraint = this.mConstraints.get(Integer.valueOf(paramInt));
      if (constraint == null)
        return; 
      int i = constraint.layout.leftToRight;
      int j = constraint.layout.rightToLeft;
      if (i != -1 || j != -1) {
        if (i != -1 && j != -1) {
          connect(i, 2, j, 1, 0);
          connect(j, 1, i, 2, 0);
        } else if (constraint.layout.rightToRight != -1) {
          connect(i, 2, constraint.layout.rightToRight, 2, 0);
        } else if (constraint.layout.leftToLeft != -1) {
          connect(j, 1, constraint.layout.leftToLeft, 1, 0);
        } 
        clear(paramInt, 1);
        clear(paramInt, 2);
        return;
      } 
      j = constraint.layout.startToEnd;
      int k = constraint.layout.endToStart;
      if (j != -1 || k != -1)
        if (j != -1 && k != -1) {
          connect(j, 7, k, 6, 0);
          connect(k, 6, i, 7, 0);
        } else if (k != -1) {
          if (constraint.layout.rightToRight != -1) {
            connect(i, 7, constraint.layout.rightToRight, 7, 0);
          } else if (constraint.layout.leftToLeft != -1) {
            connect(k, 6, constraint.layout.leftToLeft, 6, 0);
          } 
        }  
      clear(paramInt, 6);
      clear(paramInt, 7);
    } 
  }
  
  public void removeFromVerticalChain(int paramInt) {
    if (this.mConstraints.containsKey(Integer.valueOf(paramInt))) {
      Constraint constraint = this.mConstraints.get(Integer.valueOf(paramInt));
      if (constraint == null)
        return; 
      int i = constraint.layout.topToBottom;
      int j = constraint.layout.bottomToTop;
      if (i != -1 || j != -1)
        if (i != -1 && j != -1) {
          connect(i, 4, j, 3, 0);
          connect(j, 3, i, 4, 0);
        } else if (constraint.layout.bottomToBottom != -1) {
          connect(i, 4, constraint.layout.bottomToBottom, 4, 0);
        } else if (constraint.layout.topToTop != -1) {
          connect(j, 3, constraint.layout.topToTop, 3, 0);
        }  
    } 
    clear(paramInt, 3);
    clear(paramInt, 4);
  }
  
  public void setAlpha(int paramInt, float paramFloat) {
    (get(paramInt)).propertySet.alpha = paramFloat;
  }
  
  public void setApplyElevation(int paramInt, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 21)
      (get(paramInt)).transform.applyElevation = paramBoolean; 
  }
  
  public void setBarrierType(int paramInt1, int paramInt2) {
    (get(paramInt1)).layout.mHelperType = paramInt2;
  }
  
  public void setColorValue(int paramInt1, String paramString, int paramInt2) {
    get(paramInt1).setColorValue(paramString, paramInt2);
  }
  
  public void setDimensionRatio(int paramInt, String paramString) {
    (get(paramInt)).layout.dimensionRatio = paramString;
  }
  
  public void setEditorAbsoluteX(int paramInt1, int paramInt2) {
    (get(paramInt1)).layout.editorAbsoluteX = paramInt2;
  }
  
  public void setEditorAbsoluteY(int paramInt1, int paramInt2) {
    (get(paramInt1)).layout.editorAbsoluteY = paramInt2;
  }
  
  public void setElevation(int paramInt, float paramFloat) {
    if (Build.VERSION.SDK_INT >= 21) {
      (get(paramInt)).transform.elevation = paramFloat;
      (get(paramInt)).transform.applyElevation = true;
    } 
  }
  
  public void setFloatValue(int paramInt, String paramString, float paramFloat) {
    get(paramInt).setFloatValue(paramString, paramFloat);
  }
  
  public void setForceId(boolean paramBoolean) {
    this.mForceId = paramBoolean;
  }
  
  public void setGoneMargin(int paramInt1, int paramInt2, int paramInt3) {
    Constraint constraint = get(paramInt1);
    switch (paramInt2) {
      default:
        throw new IllegalArgumentException("unknown constraint");
      case 7:
        constraint.layout.goneEndMargin = paramInt3;
        return;
      case 6:
        constraint.layout.goneStartMargin = paramInt3;
        return;
      case 5:
        constraint.layout.goneBaselineMargin = paramInt3;
        return;
      case 4:
        constraint.layout.goneBottomMargin = paramInt3;
        return;
      case 3:
        constraint.layout.goneTopMargin = paramInt3;
        return;
      case 2:
        constraint.layout.goneRightMargin = paramInt3;
        return;
      case 1:
        break;
    } 
    constraint.layout.goneLeftMargin = paramInt3;
  }
  
  public void setGuidelineBegin(int paramInt1, int paramInt2) {
    (get(paramInt1)).layout.guideBegin = paramInt2;
    (get(paramInt1)).layout.guideEnd = -1;
    (get(paramInt1)).layout.guidePercent = -1.0F;
  }
  
  public void setGuidelineEnd(int paramInt1, int paramInt2) {
    (get(paramInt1)).layout.guideEnd = paramInt2;
    (get(paramInt1)).layout.guideBegin = -1;
    (get(paramInt1)).layout.guidePercent = -1.0F;
  }
  
  public void setGuidelinePercent(int paramInt, float paramFloat) {
    (get(paramInt)).layout.guidePercent = paramFloat;
    (get(paramInt)).layout.guideEnd = -1;
    (get(paramInt)).layout.guideBegin = -1;
  }
  
  public void setHorizontalBias(int paramInt, float paramFloat) {
    (get(paramInt)).layout.horizontalBias = paramFloat;
  }
  
  public void setHorizontalChainStyle(int paramInt1, int paramInt2) {
    (get(paramInt1)).layout.horizontalChainStyle = paramInt2;
  }
  
  public void setHorizontalWeight(int paramInt, float paramFloat) {
    (get(paramInt)).layout.horizontalWeight = paramFloat;
  }
  
  public void setIntValue(int paramInt1, String paramString, int paramInt2) {
    get(paramInt1).setIntValue(paramString, paramInt2);
  }
  
  public void setLayoutWrapBehavior(int paramInt1, int paramInt2) {
    if (paramInt2 >= 0 && paramInt2 <= 3)
      (get(paramInt1)).layout.mWrapBehavior = paramInt2; 
  }
  
  public void setMargin(int paramInt1, int paramInt2, int paramInt3) {
    Constraint constraint = get(paramInt1);
    switch (paramInt2) {
      default:
        throw new IllegalArgumentException("unknown constraint");
      case 7:
        constraint.layout.endMargin = paramInt3;
        return;
      case 6:
        constraint.layout.startMargin = paramInt3;
        return;
      case 5:
        constraint.layout.baselineMargin = paramInt3;
        return;
      case 4:
        constraint.layout.bottomMargin = paramInt3;
        return;
      case 3:
        constraint.layout.topMargin = paramInt3;
        return;
      case 2:
        constraint.layout.rightMargin = paramInt3;
        return;
      case 1:
        break;
    } 
    constraint.layout.leftMargin = paramInt3;
  }
  
  public void setReferencedIds(int paramInt, int... paramVarArgs) {
    (get(paramInt)).layout.mReferenceIds = paramVarArgs;
  }
  
  public void setRotation(int paramInt, float paramFloat) {
    (get(paramInt)).transform.rotation = paramFloat;
  }
  
  public void setRotationX(int paramInt, float paramFloat) {
    (get(paramInt)).transform.rotationX = paramFloat;
  }
  
  public void setRotationY(int paramInt, float paramFloat) {
    (get(paramInt)).transform.rotationY = paramFloat;
  }
  
  public void setScaleX(int paramInt, float paramFloat) {
    (get(paramInt)).transform.scaleX = paramFloat;
  }
  
  public void setScaleY(int paramInt, float paramFloat) {
    (get(paramInt)).transform.scaleY = paramFloat;
  }
  
  public void setStringValue(int paramInt, String paramString1, String paramString2) {
    get(paramInt).setStringValue(paramString1, paramString2);
  }
  
  public void setTransformPivot(int paramInt, float paramFloat1, float paramFloat2) {
    Constraint constraint = get(paramInt);
    constraint.transform.transformPivotY = paramFloat2;
    constraint.transform.transformPivotX = paramFloat1;
  }
  
  public void setTransformPivotX(int paramInt, float paramFloat) {
    (get(paramInt)).transform.transformPivotX = paramFloat;
  }
  
  public void setTransformPivotY(int paramInt, float paramFloat) {
    (get(paramInt)).transform.transformPivotY = paramFloat;
  }
  
  public void setTranslation(int paramInt, float paramFloat1, float paramFloat2) {
    Constraint constraint = get(paramInt);
    constraint.transform.translationX = paramFloat1;
    constraint.transform.translationY = paramFloat2;
  }
  
  public void setTranslationX(int paramInt, float paramFloat) {
    (get(paramInt)).transform.translationX = paramFloat;
  }
  
  public void setTranslationY(int paramInt, float paramFloat) {
    (get(paramInt)).transform.translationY = paramFloat;
  }
  
  public void setTranslationZ(int paramInt, float paramFloat) {
    if (Build.VERSION.SDK_INT >= 21)
      (get(paramInt)).transform.translationZ = paramFloat; 
  }
  
  public void setValidateOnParse(boolean paramBoolean) {
    this.mValidate = paramBoolean;
  }
  
  public void setVerticalBias(int paramInt, float paramFloat) {
    (get(paramInt)).layout.verticalBias = paramFloat;
  }
  
  public void setVerticalChainStyle(int paramInt1, int paramInt2) {
    (get(paramInt1)).layout.verticalChainStyle = paramInt2;
  }
  
  public void setVerticalWeight(int paramInt, float paramFloat) {
    (get(paramInt)).layout.verticalWeight = paramFloat;
  }
  
  public void setVisibility(int paramInt1, int paramInt2) {
    (get(paramInt1)).propertySet.visibility = paramInt2;
  }
  
  public void setVisibilityMode(int paramInt1, int paramInt2) {
    (get(paramInt1)).propertySet.mVisibilityMode = paramInt2;
  }
  
  public void writeState(Writer paramWriter, ConstraintLayout paramConstraintLayout, int paramInt) throws IOException {
    paramWriter.write("\n---------------------------------------------\n");
    if ((paramInt & 0x1) == 1) {
      (new WriteXmlEngine(paramWriter, paramConstraintLayout, paramInt)).writeLayout();
    } else {
      (new WriteJsonEngine(paramWriter, paramConstraintLayout, paramInt)).writeLayout();
    } 
    paramWriter.write("\n---------------------------------------------\n");
  }
  
  public static class Constraint {
    public final ConstraintSet.Layout layout = new ConstraintSet.Layout();
    
    public HashMap<String, ConstraintAttribute> mCustomConstraints = new HashMap<>();
    
    Delta mDelta;
    
    String mTargetString;
    
    int mViewId;
    
    public final ConstraintSet.Motion motion = new ConstraintSet.Motion();
    
    public final ConstraintSet.PropertySet propertySet = new ConstraintSet.PropertySet();
    
    public final ConstraintSet.Transform transform = new ConstraintSet.Transform();
    
    private void fillFrom(int param1Int, ConstraintLayout.LayoutParams param1LayoutParams) {
      this.mViewId = param1Int;
      this.layout.leftToLeft = param1LayoutParams.leftToLeft;
      this.layout.leftToRight = param1LayoutParams.leftToRight;
      this.layout.rightToLeft = param1LayoutParams.rightToLeft;
      this.layout.rightToRight = param1LayoutParams.rightToRight;
      this.layout.topToTop = param1LayoutParams.topToTop;
      this.layout.topToBottom = param1LayoutParams.topToBottom;
      this.layout.bottomToTop = param1LayoutParams.bottomToTop;
      this.layout.bottomToBottom = param1LayoutParams.bottomToBottom;
      this.layout.baselineToBaseline = param1LayoutParams.baselineToBaseline;
      this.layout.baselineToTop = param1LayoutParams.baselineToTop;
      this.layout.baselineToBottom = param1LayoutParams.baselineToBottom;
      this.layout.startToEnd = param1LayoutParams.startToEnd;
      this.layout.startToStart = param1LayoutParams.startToStart;
      this.layout.endToStart = param1LayoutParams.endToStart;
      this.layout.endToEnd = param1LayoutParams.endToEnd;
      this.layout.horizontalBias = param1LayoutParams.horizontalBias;
      this.layout.verticalBias = param1LayoutParams.verticalBias;
      this.layout.dimensionRatio = param1LayoutParams.dimensionRatio;
      this.layout.circleConstraint = param1LayoutParams.circleConstraint;
      this.layout.circleRadius = param1LayoutParams.circleRadius;
      this.layout.circleAngle = param1LayoutParams.circleAngle;
      this.layout.editorAbsoluteX = param1LayoutParams.editorAbsoluteX;
      this.layout.editorAbsoluteY = param1LayoutParams.editorAbsoluteY;
      this.layout.orientation = param1LayoutParams.orientation;
      this.layout.guidePercent = param1LayoutParams.guidePercent;
      this.layout.guideBegin = param1LayoutParams.guideBegin;
      this.layout.guideEnd = param1LayoutParams.guideEnd;
      this.layout.mWidth = param1LayoutParams.width;
      this.layout.mHeight = param1LayoutParams.height;
      this.layout.leftMargin = param1LayoutParams.leftMargin;
      this.layout.rightMargin = param1LayoutParams.rightMargin;
      this.layout.topMargin = param1LayoutParams.topMargin;
      this.layout.bottomMargin = param1LayoutParams.bottomMargin;
      this.layout.baselineMargin = param1LayoutParams.baselineMargin;
      this.layout.verticalWeight = param1LayoutParams.verticalWeight;
      this.layout.horizontalWeight = param1LayoutParams.horizontalWeight;
      this.layout.verticalChainStyle = param1LayoutParams.verticalChainStyle;
      this.layout.horizontalChainStyle = param1LayoutParams.horizontalChainStyle;
      this.layout.constrainedWidth = param1LayoutParams.constrainedWidth;
      this.layout.constrainedHeight = param1LayoutParams.constrainedHeight;
      this.layout.widthDefault = param1LayoutParams.matchConstraintDefaultWidth;
      this.layout.heightDefault = param1LayoutParams.matchConstraintDefaultHeight;
      this.layout.widthMax = param1LayoutParams.matchConstraintMaxWidth;
      this.layout.heightMax = param1LayoutParams.matchConstraintMaxHeight;
      this.layout.widthMin = param1LayoutParams.matchConstraintMinWidth;
      this.layout.heightMin = param1LayoutParams.matchConstraintMinHeight;
      this.layout.widthPercent = param1LayoutParams.matchConstraintPercentWidth;
      this.layout.heightPercent = param1LayoutParams.matchConstraintPercentHeight;
      this.layout.mConstraintTag = param1LayoutParams.constraintTag;
      this.layout.goneTopMargin = param1LayoutParams.goneTopMargin;
      this.layout.goneBottomMargin = param1LayoutParams.goneBottomMargin;
      this.layout.goneLeftMargin = param1LayoutParams.goneLeftMargin;
      this.layout.goneRightMargin = param1LayoutParams.goneRightMargin;
      this.layout.goneStartMargin = param1LayoutParams.goneStartMargin;
      this.layout.goneEndMargin = param1LayoutParams.goneEndMargin;
      this.layout.goneBaselineMargin = param1LayoutParams.goneBaselineMargin;
      this.layout.mWrapBehavior = param1LayoutParams.wrapBehaviorInParent;
      if (Build.VERSION.SDK_INT >= 17) {
        this.layout.endMargin = param1LayoutParams.getMarginEnd();
        this.layout.startMargin = param1LayoutParams.getMarginStart();
      } 
    }
    
    private void fillFromConstraints(int param1Int, Constraints.LayoutParams param1LayoutParams) {
      fillFrom(param1Int, param1LayoutParams);
      this.propertySet.alpha = param1LayoutParams.alpha;
      this.transform.rotation = param1LayoutParams.rotation;
      this.transform.rotationX = param1LayoutParams.rotationX;
      this.transform.rotationY = param1LayoutParams.rotationY;
      this.transform.scaleX = param1LayoutParams.scaleX;
      this.transform.scaleY = param1LayoutParams.scaleY;
      this.transform.transformPivotX = param1LayoutParams.transformPivotX;
      this.transform.transformPivotY = param1LayoutParams.transformPivotY;
      this.transform.translationX = param1LayoutParams.translationX;
      this.transform.translationY = param1LayoutParams.translationY;
      this.transform.translationZ = param1LayoutParams.translationZ;
      this.transform.elevation = param1LayoutParams.elevation;
      this.transform.applyElevation = param1LayoutParams.applyElevation;
    }
    
    private void fillFromConstraints(ConstraintHelper param1ConstraintHelper, int param1Int, Constraints.LayoutParams param1LayoutParams) {
      fillFromConstraints(param1Int, param1LayoutParams);
      if (param1ConstraintHelper instanceof Barrier) {
        this.layout.mHelperType = 1;
        param1ConstraintHelper = param1ConstraintHelper;
        this.layout.mBarrierDirection = param1ConstraintHelper.getType();
        this.layout.mReferenceIds = param1ConstraintHelper.getReferencedIds();
        this.layout.mBarrierMargin = param1ConstraintHelper.getMargin();
      } 
    }
    
    private ConstraintAttribute get(String param1String, ConstraintAttribute.AttributeType param1AttributeType) {
      String str;
      ConstraintAttribute constraintAttribute;
      if (this.mCustomConstraints.containsKey(param1String)) {
        ConstraintAttribute constraintAttribute1 = this.mCustomConstraints.get(param1String);
        constraintAttribute = constraintAttribute1;
        if (constraintAttribute1.getType() != param1AttributeType) {
          str = String.valueOf(constraintAttribute1.getType().name());
          if (str.length() != 0) {
            str = "ConstraintAttribute is already a ".concat(str);
          } else {
            str = new String("ConstraintAttribute is already a ");
          } 
          throw new IllegalArgumentException(str);
        } 
      } else {
        ConstraintAttribute constraintAttribute1 = new ConstraintAttribute(str, param1AttributeType);
        this.mCustomConstraints.put(str, constraintAttribute1);
        constraintAttribute = constraintAttribute1;
      } 
      return constraintAttribute;
    }
    
    private void setColorValue(String param1String, int param1Int) {
      get(param1String, ConstraintAttribute.AttributeType.COLOR_TYPE).setColorValue(param1Int);
    }
    
    private void setFloatValue(String param1String, float param1Float) {
      get(param1String, ConstraintAttribute.AttributeType.FLOAT_TYPE).setFloatValue(param1Float);
    }
    
    private void setIntValue(String param1String, int param1Int) {
      get(param1String, ConstraintAttribute.AttributeType.INT_TYPE).setIntValue(param1Int);
    }
    
    private void setStringValue(String param1String1, String param1String2) {
      get(param1String1, ConstraintAttribute.AttributeType.STRING_TYPE).setStringValue(param1String2);
    }
    
    public void applyDelta(Constraint param1Constraint) {
      Delta delta = this.mDelta;
      if (delta != null)
        delta.applyDelta(param1Constraint); 
    }
    
    public void applyTo(ConstraintLayout.LayoutParams param1LayoutParams) {
      param1LayoutParams.leftToLeft = this.layout.leftToLeft;
      param1LayoutParams.leftToRight = this.layout.leftToRight;
      param1LayoutParams.rightToLeft = this.layout.rightToLeft;
      param1LayoutParams.rightToRight = this.layout.rightToRight;
      param1LayoutParams.topToTop = this.layout.topToTop;
      param1LayoutParams.topToBottom = this.layout.topToBottom;
      param1LayoutParams.bottomToTop = this.layout.bottomToTop;
      param1LayoutParams.bottomToBottom = this.layout.bottomToBottom;
      param1LayoutParams.baselineToBaseline = this.layout.baselineToBaseline;
      param1LayoutParams.baselineToTop = this.layout.baselineToTop;
      param1LayoutParams.baselineToBottom = this.layout.baselineToBottom;
      param1LayoutParams.startToEnd = this.layout.startToEnd;
      param1LayoutParams.startToStart = this.layout.startToStart;
      param1LayoutParams.endToStart = this.layout.endToStart;
      param1LayoutParams.endToEnd = this.layout.endToEnd;
      param1LayoutParams.leftMargin = this.layout.leftMargin;
      param1LayoutParams.rightMargin = this.layout.rightMargin;
      param1LayoutParams.topMargin = this.layout.topMargin;
      param1LayoutParams.bottomMargin = this.layout.bottomMargin;
      param1LayoutParams.goneStartMargin = this.layout.goneStartMargin;
      param1LayoutParams.goneEndMargin = this.layout.goneEndMargin;
      param1LayoutParams.goneTopMargin = this.layout.goneTopMargin;
      param1LayoutParams.goneBottomMargin = this.layout.goneBottomMargin;
      param1LayoutParams.horizontalBias = this.layout.horizontalBias;
      param1LayoutParams.verticalBias = this.layout.verticalBias;
      param1LayoutParams.circleConstraint = this.layout.circleConstraint;
      param1LayoutParams.circleRadius = this.layout.circleRadius;
      param1LayoutParams.circleAngle = this.layout.circleAngle;
      param1LayoutParams.dimensionRatio = this.layout.dimensionRatio;
      param1LayoutParams.editorAbsoluteX = this.layout.editorAbsoluteX;
      param1LayoutParams.editorAbsoluteY = this.layout.editorAbsoluteY;
      param1LayoutParams.verticalWeight = this.layout.verticalWeight;
      param1LayoutParams.horizontalWeight = this.layout.horizontalWeight;
      param1LayoutParams.verticalChainStyle = this.layout.verticalChainStyle;
      param1LayoutParams.horizontalChainStyle = this.layout.horizontalChainStyle;
      param1LayoutParams.constrainedWidth = this.layout.constrainedWidth;
      param1LayoutParams.constrainedHeight = this.layout.constrainedHeight;
      param1LayoutParams.matchConstraintDefaultWidth = this.layout.widthDefault;
      param1LayoutParams.matchConstraintDefaultHeight = this.layout.heightDefault;
      param1LayoutParams.matchConstraintMaxWidth = this.layout.widthMax;
      param1LayoutParams.matchConstraintMaxHeight = this.layout.heightMax;
      param1LayoutParams.matchConstraintMinWidth = this.layout.widthMin;
      param1LayoutParams.matchConstraintMinHeight = this.layout.heightMin;
      param1LayoutParams.matchConstraintPercentWidth = this.layout.widthPercent;
      param1LayoutParams.matchConstraintPercentHeight = this.layout.heightPercent;
      param1LayoutParams.orientation = this.layout.orientation;
      param1LayoutParams.guidePercent = this.layout.guidePercent;
      param1LayoutParams.guideBegin = this.layout.guideBegin;
      param1LayoutParams.guideEnd = this.layout.guideEnd;
      param1LayoutParams.width = this.layout.mWidth;
      param1LayoutParams.height = this.layout.mHeight;
      if (this.layout.mConstraintTag != null)
        param1LayoutParams.constraintTag = this.layout.mConstraintTag; 
      param1LayoutParams.wrapBehaviorInParent = this.layout.mWrapBehavior;
      if (Build.VERSION.SDK_INT >= 17) {
        param1LayoutParams.setMarginStart(this.layout.startMargin);
        param1LayoutParams.setMarginEnd(this.layout.endMargin);
      } 
      param1LayoutParams.validate();
    }
    
    public Constraint clone() {
      Constraint constraint = new Constraint();
      constraint.layout.copyFrom(this.layout);
      constraint.motion.copyFrom(this.motion);
      constraint.propertySet.copyFrom(this.propertySet);
      constraint.transform.copyFrom(this.transform);
      constraint.mViewId = this.mViewId;
      constraint.mDelta = this.mDelta;
      return constraint;
    }
    
    public void printDelta(String param1String) {
      Delta delta = this.mDelta;
      if (delta != null) {
        delta.printDelta(param1String);
      } else {
        Log.v(param1String, "DELTA IS NULL");
      } 
    }
    
    static class Delta {
      private static final int INITIAL_BOOLEAN = 4;
      
      private static final int INITIAL_FLOAT = 10;
      
      private static final int INITIAL_INT = 10;
      
      private static final int INITIAL_STRING = 5;
      
      int mCountBoolean = 0;
      
      int mCountFloat = 0;
      
      int mCountInt = 0;
      
      int mCountString = 0;
      
      int[] mTypeBoolean = new int[4];
      
      int[] mTypeFloat = new int[10];
      
      int[] mTypeInt = new int[10];
      
      int[] mTypeString = new int[5];
      
      boolean[] mValueBoolean = new boolean[4];
      
      float[] mValueFloat = new float[10];
      
      int[] mValueInt = new int[10];
      
      String[] mValueString = new String[5];
      
      void add(int param2Int, float param2Float) {
        int i = this.mCountFloat;
        int[] arrayOfInt = this.mTypeFloat;
        if (i >= arrayOfInt.length) {
          this.mTypeFloat = Arrays.copyOf(arrayOfInt, arrayOfInt.length * 2);
          float[] arrayOfFloat1 = this.mValueFloat;
          this.mValueFloat = Arrays.copyOf(arrayOfFloat1, arrayOfFloat1.length * 2);
        } 
        arrayOfInt = this.mTypeFloat;
        i = this.mCountFloat;
        arrayOfInt[i] = param2Int;
        float[] arrayOfFloat = this.mValueFloat;
        this.mCountFloat = i + 1;
        arrayOfFloat[i] = param2Float;
      }
      
      void add(int param2Int1, int param2Int2) {
        int i = this.mCountInt;
        int[] arrayOfInt = this.mTypeInt;
        if (i >= arrayOfInt.length) {
          this.mTypeInt = Arrays.copyOf(arrayOfInt, arrayOfInt.length * 2);
          arrayOfInt = this.mValueInt;
          this.mValueInt = Arrays.copyOf(arrayOfInt, arrayOfInt.length * 2);
        } 
        arrayOfInt = this.mTypeInt;
        i = this.mCountInt;
        arrayOfInt[i] = param2Int1;
        arrayOfInt = this.mValueInt;
        this.mCountInt = i + 1;
        arrayOfInt[i] = param2Int2;
      }
      
      void add(int param2Int, String param2String) {
        int i = this.mCountString;
        int[] arrayOfInt = this.mTypeString;
        if (i >= arrayOfInt.length) {
          this.mTypeString = Arrays.copyOf(arrayOfInt, arrayOfInt.length * 2);
          String[] arrayOfString1 = this.mValueString;
          this.mValueString = Arrays.<String>copyOf(arrayOfString1, arrayOfString1.length * 2);
        } 
        arrayOfInt = this.mTypeString;
        i = this.mCountString;
        arrayOfInt[i] = param2Int;
        String[] arrayOfString = this.mValueString;
        this.mCountString = i + 1;
        arrayOfString[i] = param2String;
      }
      
      void add(int param2Int, boolean param2Boolean) {
        int i = this.mCountBoolean;
        int[] arrayOfInt = this.mTypeBoolean;
        if (i >= arrayOfInt.length) {
          this.mTypeBoolean = Arrays.copyOf(arrayOfInt, arrayOfInt.length * 2);
          boolean[] arrayOfBoolean1 = this.mValueBoolean;
          this.mValueBoolean = Arrays.copyOf(arrayOfBoolean1, arrayOfBoolean1.length * 2);
        } 
        arrayOfInt = this.mTypeBoolean;
        i = this.mCountBoolean;
        arrayOfInt[i] = param2Int;
        boolean[] arrayOfBoolean = this.mValueBoolean;
        this.mCountBoolean = i + 1;
        arrayOfBoolean[i] = param2Boolean;
      }
      
      void applyDelta(ConstraintSet.Constraint param2Constraint) {
        byte b;
        for (b = 0; b < this.mCountInt; b++)
          ConstraintSet.setDeltaValue(param2Constraint, this.mTypeInt[b], this.mValueInt[b]); 
        for (b = 0; b < this.mCountFloat; b++)
          ConstraintSet.setDeltaValue(param2Constraint, this.mTypeFloat[b], this.mValueFloat[b]); 
        for (b = 0; b < this.mCountString; b++)
          ConstraintSet.setDeltaValue(param2Constraint, this.mTypeString[b], this.mValueString[b]); 
        for (b = 0; b < this.mCountBoolean; b++)
          ConstraintSet.setDeltaValue(param2Constraint, this.mTypeBoolean[b], this.mValueBoolean[b]); 
      }
      
      void printDelta(String param2String) {
        Log.v(param2String, "int");
        byte b;
        for (b = 0; b < this.mCountInt; b++) {
          int j = this.mTypeInt[b];
          int i = this.mValueInt[b];
          Log.v(param2String, (new StringBuilder(25)).append(j).append(" = ").append(i).toString());
        } 
        Log.v(param2String, "float");
        for (b = 0; b < this.mCountFloat; b++) {
          int i = this.mTypeFloat[b];
          float f = this.mValueFloat[b];
          Log.v(param2String, (new StringBuilder(29)).append(i).append(" = ").append(f).toString());
        } 
        Log.v(param2String, "strings");
        for (b = 0; b < this.mCountString; b++) {
          int i = this.mTypeString[b];
          String str = this.mValueString[b];
          Log.v(param2String, (new StringBuilder(String.valueOf(str).length() + 14)).append(i).append(" = ").append(str).toString());
        } 
        Log.v(param2String, "boolean");
        for (b = 0; b < this.mCountBoolean; b++) {
          int i = this.mTypeBoolean[b];
          boolean bool = this.mValueBoolean[b];
          Log.v(param2String, (new StringBuilder(19)).append(i).append(" = ").append(bool).toString());
        } 
      }
    }
  }
  
  static class Delta {
    private static final int INITIAL_BOOLEAN = 4;
    
    private static final int INITIAL_FLOAT = 10;
    
    private static final int INITIAL_INT = 10;
    
    private static final int INITIAL_STRING = 5;
    
    int mCountBoolean = 0;
    
    int mCountFloat = 0;
    
    int mCountInt = 0;
    
    int mCountString = 0;
    
    int[] mTypeBoolean = new int[4];
    
    int[] mTypeFloat = new int[10];
    
    int[] mTypeInt = new int[10];
    
    int[] mTypeString = new int[5];
    
    boolean[] mValueBoolean = new boolean[4];
    
    float[] mValueFloat = new float[10];
    
    int[] mValueInt = new int[10];
    
    String[] mValueString = new String[5];
    
    void add(int param1Int, float param1Float) {
      int i = this.mCountFloat;
      int[] arrayOfInt = this.mTypeFloat;
      if (i >= arrayOfInt.length) {
        this.mTypeFloat = Arrays.copyOf(arrayOfInt, arrayOfInt.length * 2);
        float[] arrayOfFloat1 = this.mValueFloat;
        this.mValueFloat = Arrays.copyOf(arrayOfFloat1, arrayOfFloat1.length * 2);
      } 
      arrayOfInt = this.mTypeFloat;
      i = this.mCountFloat;
      arrayOfInt[i] = param1Int;
      float[] arrayOfFloat = this.mValueFloat;
      this.mCountFloat = i + 1;
      arrayOfFloat[i] = param1Float;
    }
    
    void add(int param1Int1, int param1Int2) {
      int i = this.mCountInt;
      int[] arrayOfInt = this.mTypeInt;
      if (i >= arrayOfInt.length) {
        this.mTypeInt = Arrays.copyOf(arrayOfInt, arrayOfInt.length * 2);
        arrayOfInt = this.mValueInt;
        this.mValueInt = Arrays.copyOf(arrayOfInt, arrayOfInt.length * 2);
      } 
      arrayOfInt = this.mTypeInt;
      i = this.mCountInt;
      arrayOfInt[i] = param1Int1;
      arrayOfInt = this.mValueInt;
      this.mCountInt = i + 1;
      arrayOfInt[i] = param1Int2;
    }
    
    void add(int param1Int, String param1String) {
      int i = this.mCountString;
      int[] arrayOfInt = this.mTypeString;
      if (i >= arrayOfInt.length) {
        this.mTypeString = Arrays.copyOf(arrayOfInt, arrayOfInt.length * 2);
        String[] arrayOfString1 = this.mValueString;
        this.mValueString = Arrays.<String>copyOf(arrayOfString1, arrayOfString1.length * 2);
      } 
      arrayOfInt = this.mTypeString;
      i = this.mCountString;
      arrayOfInt[i] = param1Int;
      String[] arrayOfString = this.mValueString;
      this.mCountString = i + 1;
      arrayOfString[i] = param1String;
    }
    
    void add(int param1Int, boolean param1Boolean) {
      int i = this.mCountBoolean;
      int[] arrayOfInt = this.mTypeBoolean;
      if (i >= arrayOfInt.length) {
        this.mTypeBoolean = Arrays.copyOf(arrayOfInt, arrayOfInt.length * 2);
        boolean[] arrayOfBoolean1 = this.mValueBoolean;
        this.mValueBoolean = Arrays.copyOf(arrayOfBoolean1, arrayOfBoolean1.length * 2);
      } 
      arrayOfInt = this.mTypeBoolean;
      i = this.mCountBoolean;
      arrayOfInt[i] = param1Int;
      boolean[] arrayOfBoolean = this.mValueBoolean;
      this.mCountBoolean = i + 1;
      arrayOfBoolean[i] = param1Boolean;
    }
    
    void applyDelta(ConstraintSet.Constraint param1Constraint) {
      byte b;
      for (b = 0; b < this.mCountInt; b++)
        ConstraintSet.setDeltaValue(param1Constraint, this.mTypeInt[b], this.mValueInt[b]); 
      for (b = 0; b < this.mCountFloat; b++)
        ConstraintSet.setDeltaValue(param1Constraint, this.mTypeFloat[b], this.mValueFloat[b]); 
      for (b = 0; b < this.mCountString; b++)
        ConstraintSet.setDeltaValue(param1Constraint, this.mTypeString[b], this.mValueString[b]); 
      for (b = 0; b < this.mCountBoolean; b++)
        ConstraintSet.setDeltaValue(param1Constraint, this.mTypeBoolean[b], this.mValueBoolean[b]); 
    }
    
    void printDelta(String param1String) {
      Log.v(param1String, "int");
      byte b;
      for (b = 0; b < this.mCountInt; b++) {
        int j = this.mTypeInt[b];
        int i = this.mValueInt[b];
        Log.v(param1String, (new StringBuilder(25)).append(j).append(" = ").append(i).toString());
      } 
      Log.v(param1String, "float");
      for (b = 0; b < this.mCountFloat; b++) {
        int i = this.mTypeFloat[b];
        float f = this.mValueFloat[b];
        Log.v(param1String, (new StringBuilder(29)).append(i).append(" = ").append(f).toString());
      } 
      Log.v(param1String, "strings");
      for (b = 0; b < this.mCountString; b++) {
        int i = this.mTypeString[b];
        String str = this.mValueString[b];
        Log.v(param1String, (new StringBuilder(String.valueOf(str).length() + 14)).append(i).append(" = ").append(str).toString());
      } 
      Log.v(param1String, "boolean");
      for (b = 0; b < this.mCountBoolean; b++) {
        int i = this.mTypeBoolean[b];
        boolean bool = this.mValueBoolean[b];
        Log.v(param1String, (new StringBuilder(19)).append(i).append(" = ").append(bool).toString());
      } 
    }
  }
  
  public static class Layout {
    private static final int BARRIER_ALLOWS_GONE_WIDGETS = 75;
    
    private static final int BARRIER_DIRECTION = 72;
    
    private static final int BARRIER_MARGIN = 73;
    
    private static final int BASELINE_TO_BASELINE = 1;
    
    private static final int BOTTOM_MARGIN = 2;
    
    private static final int BOTTOM_TO_BOTTOM = 3;
    
    private static final int BOTTOM_TO_TOP = 4;
    
    private static final int CHAIN_USE_RTL = 71;
    
    private static final int CIRCLE = 61;
    
    private static final int CIRCLE_ANGLE = 63;
    
    private static final int CIRCLE_RADIUS = 62;
    
    private static final int CONSTRAINT_REFERENCED_IDS = 74;
    
    private static final int DIMENSION_RATIO = 5;
    
    private static final int EDITOR_ABSOLUTE_X = 6;
    
    private static final int EDITOR_ABSOLUTE_Y = 7;
    
    private static final int END_MARGIN = 8;
    
    private static final int END_TO_END = 9;
    
    private static final int END_TO_START = 10;
    
    private static final int GONE_BOTTOM_MARGIN = 11;
    
    private static final int GONE_END_MARGIN = 12;
    
    private static final int GONE_LEFT_MARGIN = 13;
    
    private static final int GONE_RIGHT_MARGIN = 14;
    
    private static final int GONE_START_MARGIN = 15;
    
    private static final int GONE_TOP_MARGIN = 16;
    
    private static final int GUIDE_BEGIN = 17;
    
    private static final int GUIDE_END = 18;
    
    private static final int GUIDE_PERCENT = 19;
    
    private static final int HEIGHT_PERCENT = 70;
    
    private static final int HORIZONTAL_BIAS = 20;
    
    private static final int HORIZONTAL_STYLE = 39;
    
    private static final int HORIZONTAL_WEIGHT = 37;
    
    private static final int LAYOUT_CONSTRAINT_HEIGHT = 42;
    
    private static final int LAYOUT_CONSTRAINT_WIDTH = 41;
    
    private static final int LAYOUT_HEIGHT = 21;
    
    private static final int LAYOUT_WIDTH = 22;
    
    private static final int LEFT_MARGIN = 23;
    
    private static final int LEFT_TO_LEFT = 24;
    
    private static final int LEFT_TO_RIGHT = 25;
    
    private static final int ORIENTATION = 26;
    
    private static final int RIGHT_MARGIN = 27;
    
    private static final int RIGHT_TO_LEFT = 28;
    
    private static final int RIGHT_TO_RIGHT = 29;
    
    private static final int START_MARGIN = 30;
    
    private static final int START_TO_END = 31;
    
    private static final int START_TO_START = 32;
    
    private static final int TOP_MARGIN = 33;
    
    private static final int TOP_TO_BOTTOM = 34;
    
    private static final int TOP_TO_TOP = 35;
    
    public static final int UNSET = -1;
    
    public static final int UNSET_GONE_MARGIN = -2147483648;
    
    private static final int UNUSED = 76;
    
    private static final int VERTICAL_BIAS = 36;
    
    private static final int VERTICAL_STYLE = 40;
    
    private static final int VERTICAL_WEIGHT = 38;
    
    private static final int WIDTH_PERCENT = 69;
    
    private static SparseIntArray mapToConstant;
    
    public int baselineMargin = 0;
    
    public int baselineToBaseline = -1;
    
    public int baselineToBottom = -1;
    
    public int baselineToTop = -1;
    
    public int bottomMargin = 0;
    
    public int bottomToBottom = -1;
    
    public int bottomToTop = -1;
    
    public float circleAngle = 0.0F;
    
    public int circleConstraint = -1;
    
    public int circleRadius = 0;
    
    public boolean constrainedHeight = false;
    
    public boolean constrainedWidth = false;
    
    public String dimensionRatio = null;
    
    public int editorAbsoluteX = -1;
    
    public int editorAbsoluteY = -1;
    
    public int endMargin = 0;
    
    public int endToEnd = -1;
    
    public int endToStart = -1;
    
    public int goneBaselineMargin = Integer.MIN_VALUE;
    
    public int goneBottomMargin = Integer.MIN_VALUE;
    
    public int goneEndMargin = Integer.MIN_VALUE;
    
    public int goneLeftMargin = Integer.MIN_VALUE;
    
    public int goneRightMargin = Integer.MIN_VALUE;
    
    public int goneStartMargin = Integer.MIN_VALUE;
    
    public int goneTopMargin = Integer.MIN_VALUE;
    
    public int guideBegin = -1;
    
    public int guideEnd = -1;
    
    public float guidePercent = -1.0F;
    
    public int heightDefault = 0;
    
    public int heightMax = 0;
    
    public int heightMin = 0;
    
    public float heightPercent = 1.0F;
    
    public float horizontalBias = 0.5F;
    
    public int horizontalChainStyle = 0;
    
    public float horizontalWeight = -1.0F;
    
    public int leftMargin = 0;
    
    public int leftToLeft = -1;
    
    public int leftToRight = -1;
    
    public boolean mApply = false;
    
    public boolean mBarrierAllowsGoneWidgets = true;
    
    public int mBarrierDirection = -1;
    
    public int mBarrierMargin = 0;
    
    public String mConstraintTag;
    
    public int mHeight;
    
    public int mHelperType = -1;
    
    public boolean mIsGuideline = false;
    
    public boolean mOverride = false;
    
    public String mReferenceIdString;
    
    public int[] mReferenceIds;
    
    public int mWidth;
    
    public int mWrapBehavior = 0;
    
    public int orientation = -1;
    
    public int rightMargin = 0;
    
    public int rightToLeft = -1;
    
    public int rightToRight = -1;
    
    public int startMargin = 0;
    
    public int startToEnd = -1;
    
    public int startToStart = -1;
    
    public int topMargin = 0;
    
    public int topToBottom = -1;
    
    public int topToTop = -1;
    
    public float verticalBias = 0.5F;
    
    public int verticalChainStyle = 0;
    
    public float verticalWeight = -1.0F;
    
    public int widthDefault = 0;
    
    public int widthMax = 0;
    
    public int widthMin = 0;
    
    public float widthPercent = 1.0F;
    
    static {
      SparseIntArray sparseIntArray = new SparseIntArray();
      mapToConstant = sparseIntArray;
      sparseIntArray.append(R.styleable.Layout_layout_constraintLeft_toLeftOf, 24);
      mapToConstant.append(R.styleable.Layout_layout_constraintLeft_toRightOf, 25);
      mapToConstant.append(R.styleable.Layout_layout_constraintRight_toLeftOf, 28);
      mapToConstant.append(R.styleable.Layout_layout_constraintRight_toRightOf, 29);
      mapToConstant.append(R.styleable.Layout_layout_constraintTop_toTopOf, 35);
      mapToConstant.append(R.styleable.Layout_layout_constraintTop_toBottomOf, 34);
      mapToConstant.append(R.styleable.Layout_layout_constraintBottom_toTopOf, 4);
      mapToConstant.append(R.styleable.Layout_layout_constraintBottom_toBottomOf, 3);
      mapToConstant.append(R.styleable.Layout_layout_constraintBaseline_toBaselineOf, 1);
      mapToConstant.append(R.styleable.Layout_layout_editor_absoluteX, 6);
      mapToConstant.append(R.styleable.Layout_layout_editor_absoluteY, 7);
      mapToConstant.append(R.styleable.Layout_layout_constraintGuide_begin, 17);
      mapToConstant.append(R.styleable.Layout_layout_constraintGuide_end, 18);
      mapToConstant.append(R.styleable.Layout_layout_constraintGuide_percent, 19);
      mapToConstant.append(R.styleable.Layout_android_orientation, 26);
      mapToConstant.append(R.styleable.Layout_layout_constraintStart_toEndOf, 31);
      mapToConstant.append(R.styleable.Layout_layout_constraintStart_toStartOf, 32);
      mapToConstant.append(R.styleable.Layout_layout_constraintEnd_toStartOf, 10);
      mapToConstant.append(R.styleable.Layout_layout_constraintEnd_toEndOf, 9);
      mapToConstant.append(R.styleable.Layout_layout_goneMarginLeft, 13);
      mapToConstant.append(R.styleable.Layout_layout_goneMarginTop, 16);
      mapToConstant.append(R.styleable.Layout_layout_goneMarginRight, 14);
      mapToConstant.append(R.styleable.Layout_layout_goneMarginBottom, 11);
      mapToConstant.append(R.styleable.Layout_layout_goneMarginStart, 15);
      mapToConstant.append(R.styleable.Layout_layout_goneMarginEnd, 12);
      mapToConstant.append(R.styleable.Layout_layout_constraintVertical_weight, 38);
      mapToConstant.append(R.styleable.Layout_layout_constraintHorizontal_weight, 37);
      mapToConstant.append(R.styleable.Layout_layout_constraintHorizontal_chainStyle, 39);
      mapToConstant.append(R.styleable.Layout_layout_constraintVertical_chainStyle, 40);
      mapToConstant.append(R.styleable.Layout_layout_constraintHorizontal_bias, 20);
      mapToConstant.append(R.styleable.Layout_layout_constraintVertical_bias, 36);
      mapToConstant.append(R.styleable.Layout_layout_constraintDimensionRatio, 5);
      mapToConstant.append(R.styleable.Layout_layout_constraintLeft_creator, 76);
      mapToConstant.append(R.styleable.Layout_layout_constraintTop_creator, 76);
      mapToConstant.append(R.styleable.Layout_layout_constraintRight_creator, 76);
      mapToConstant.append(R.styleable.Layout_layout_constraintBottom_creator, 76);
      mapToConstant.append(R.styleable.Layout_layout_constraintBaseline_creator, 76);
      mapToConstant.append(R.styleable.Layout_android_layout_marginLeft, 23);
      mapToConstant.append(R.styleable.Layout_android_layout_marginRight, 27);
      mapToConstant.append(R.styleable.Layout_android_layout_marginStart, 30);
      mapToConstant.append(R.styleable.Layout_android_layout_marginEnd, 8);
      mapToConstant.append(R.styleable.Layout_android_layout_marginTop, 33);
      mapToConstant.append(R.styleable.Layout_android_layout_marginBottom, 2);
      mapToConstant.append(R.styleable.Layout_android_layout_width, 22);
      mapToConstant.append(R.styleable.Layout_android_layout_height, 21);
      mapToConstant.append(R.styleable.Layout_layout_constraintWidth, 41);
      mapToConstant.append(R.styleable.Layout_layout_constraintHeight, 42);
      mapToConstant.append(R.styleable.Layout_layout_constrainedWidth, 41);
      mapToConstant.append(R.styleable.Layout_layout_constrainedHeight, 42);
      mapToConstant.append(R.styleable.Layout_layout_wrapBehaviorInParent, 97);
      mapToConstant.append(R.styleable.Layout_layout_constraintCircle, 61);
      mapToConstant.append(R.styleable.Layout_layout_constraintCircleRadius, 62);
      mapToConstant.append(R.styleable.Layout_layout_constraintCircleAngle, 63);
      mapToConstant.append(R.styleable.Layout_layout_constraintWidth_percent, 69);
      mapToConstant.append(R.styleable.Layout_layout_constraintHeight_percent, 70);
      mapToConstant.append(R.styleable.Layout_chainUseRtl, 71);
      mapToConstant.append(R.styleable.Layout_barrierDirection, 72);
      mapToConstant.append(R.styleable.Layout_barrierMargin, 73);
      mapToConstant.append(R.styleable.Layout_constraint_referenced_ids, 74);
      mapToConstant.append(R.styleable.Layout_barrierAllowsGoneWidgets, 75);
    }
    
    public void copyFrom(Layout param1Layout) {
      this.mIsGuideline = param1Layout.mIsGuideline;
      this.mWidth = param1Layout.mWidth;
      this.mApply = param1Layout.mApply;
      this.mHeight = param1Layout.mHeight;
      this.guideBegin = param1Layout.guideBegin;
      this.guideEnd = param1Layout.guideEnd;
      this.guidePercent = param1Layout.guidePercent;
      this.leftToLeft = param1Layout.leftToLeft;
      this.leftToRight = param1Layout.leftToRight;
      this.rightToLeft = param1Layout.rightToLeft;
      this.rightToRight = param1Layout.rightToRight;
      this.topToTop = param1Layout.topToTop;
      this.topToBottom = param1Layout.topToBottom;
      this.bottomToTop = param1Layout.bottomToTop;
      this.bottomToBottom = param1Layout.bottomToBottom;
      this.baselineToBaseline = param1Layout.baselineToBaseline;
      this.baselineToTop = param1Layout.baselineToTop;
      this.baselineToBottom = param1Layout.baselineToBottom;
      this.startToEnd = param1Layout.startToEnd;
      this.startToStart = param1Layout.startToStart;
      this.endToStart = param1Layout.endToStart;
      this.endToEnd = param1Layout.endToEnd;
      this.horizontalBias = param1Layout.horizontalBias;
      this.verticalBias = param1Layout.verticalBias;
      this.dimensionRatio = param1Layout.dimensionRatio;
      this.circleConstraint = param1Layout.circleConstraint;
      this.circleRadius = param1Layout.circleRadius;
      this.circleAngle = param1Layout.circleAngle;
      this.editorAbsoluteX = param1Layout.editorAbsoluteX;
      this.editorAbsoluteY = param1Layout.editorAbsoluteY;
      this.orientation = param1Layout.orientation;
      this.leftMargin = param1Layout.leftMargin;
      this.rightMargin = param1Layout.rightMargin;
      this.topMargin = param1Layout.topMargin;
      this.bottomMargin = param1Layout.bottomMargin;
      this.endMargin = param1Layout.endMargin;
      this.startMargin = param1Layout.startMargin;
      this.baselineMargin = param1Layout.baselineMargin;
      this.goneLeftMargin = param1Layout.goneLeftMargin;
      this.goneTopMargin = param1Layout.goneTopMargin;
      this.goneRightMargin = param1Layout.goneRightMargin;
      this.goneBottomMargin = param1Layout.goneBottomMargin;
      this.goneEndMargin = param1Layout.goneEndMargin;
      this.goneStartMargin = param1Layout.goneStartMargin;
      this.goneBaselineMargin = param1Layout.goneBaselineMargin;
      this.verticalWeight = param1Layout.verticalWeight;
      this.horizontalWeight = param1Layout.horizontalWeight;
      this.horizontalChainStyle = param1Layout.horizontalChainStyle;
      this.verticalChainStyle = param1Layout.verticalChainStyle;
      this.widthDefault = param1Layout.widthDefault;
      this.heightDefault = param1Layout.heightDefault;
      this.widthMax = param1Layout.widthMax;
      this.heightMax = param1Layout.heightMax;
      this.widthMin = param1Layout.widthMin;
      this.heightMin = param1Layout.heightMin;
      this.widthPercent = param1Layout.widthPercent;
      this.heightPercent = param1Layout.heightPercent;
      this.mBarrierDirection = param1Layout.mBarrierDirection;
      this.mBarrierMargin = param1Layout.mBarrierMargin;
      this.mHelperType = param1Layout.mHelperType;
      this.mConstraintTag = param1Layout.mConstraintTag;
      int[] arrayOfInt = param1Layout.mReferenceIds;
      if (arrayOfInt != null && param1Layout.mReferenceIdString == null) {
        this.mReferenceIds = Arrays.copyOf(arrayOfInt, arrayOfInt.length);
      } else {
        this.mReferenceIds = null;
      } 
      this.mReferenceIdString = param1Layout.mReferenceIdString;
      this.constrainedWidth = param1Layout.constrainedWidth;
      this.constrainedHeight = param1Layout.constrainedHeight;
      this.mBarrierAllowsGoneWidgets = param1Layout.mBarrierAllowsGoneWidgets;
      this.mWrapBehavior = param1Layout.mWrapBehavior;
    }
    
    public void dump(MotionScene param1MotionScene, StringBuilder param1StringBuilder) {
      Field[] arrayOfField = getClass().getDeclaredFields();
      param1StringBuilder.append("\n");
      for (byte b = 0; b < arrayOfField.length; b++) {
        Field field = arrayOfField[b];
        String str = field.getName();
        if (!Modifier.isStatic(field.getModifiers()))
          try {
            Object object = field.get(this);
            Class<?> clazz1 = field.getType();
            Class<int> clazz = int.class;
            if (clazz1 == clazz) {
              object = object;
              if (object.intValue() != -1) {
                String str1 = param1MotionScene.lookUpConstraintName(object.intValue());
                param1StringBuilder.append("    ");
                param1StringBuilder.append(str);
                param1StringBuilder.append(" = \"");
                if (str1 != null)
                  object = str1; 
                param1StringBuilder.append(object);
                param1StringBuilder.append("\"\n");
              } 
            } else if (clazz1 == float.class) {
              object = object;
              if (object.floatValue() != -1.0F) {
                param1StringBuilder.append("    ");
                param1StringBuilder.append(str);
                param1StringBuilder.append(" = \"");
                param1StringBuilder.append(object);
                param1StringBuilder.append("\"\n");
              } 
            } 
          } catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
          }  
      } 
    }
    
    void fillFromAttributeList(Context param1Context, AttributeSet param1AttributeSet) {
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.Layout);
      this.mApply = true;
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        String str;
        int j = typedArray.getIndex(b);
        switch (mapToConstant.get(j)) {
          default:
            str = Integer.toHexString(j);
            j = mapToConstant.get(j);
            Log.w("ConstraintSet", (new StringBuilder(String.valueOf(str).length() + 34)).append("Unknown attribute 0x").append(str).append("   ").append(j).toString());
            break;
          case 97:
            this.mWrapBehavior = typedArray.getInt(j, this.mWrapBehavior);
            break;
          case 94:
            this.goneBaselineMargin = typedArray.getDimensionPixelSize(j, this.goneBaselineMargin);
            break;
          case 93:
            this.baselineMargin = typedArray.getDimensionPixelSize(j, this.baselineMargin);
            break;
          case 92:
            this.baselineToBottom = ConstraintSet.lookupID(typedArray, j, this.baselineToBottom);
            break;
          case 91:
            this.baselineToTop = ConstraintSet.lookupID(typedArray, j, this.baselineToTop);
            break;
          case 81:
            this.constrainedHeight = typedArray.getBoolean(j, this.constrainedHeight);
            break;
          case 80:
            this.constrainedWidth = typedArray.getBoolean(j, this.constrainedWidth);
            break;
          case 77:
            this.mConstraintTag = typedArray.getString(j);
            break;
          case 76:
            str = Integer.toHexString(j);
            j = mapToConstant.get(j);
            Log.w("ConstraintSet", (new StringBuilder(String.valueOf(str).length() + 33)).append("unused attribute 0x").append(str).append("   ").append(j).toString());
            break;
          case 75:
            this.mBarrierAllowsGoneWidgets = typedArray.getBoolean(j, this.mBarrierAllowsGoneWidgets);
            break;
          case 74:
            this.mReferenceIdString = typedArray.getString(j);
            break;
          case 73:
            this.mBarrierMargin = typedArray.getDimensionPixelSize(j, this.mBarrierMargin);
            break;
          case 72:
            this.mBarrierDirection = typedArray.getInt(j, this.mBarrierDirection);
            break;
          case 71:
            Log.e("ConstraintSet", "CURRENTLY UNSUPPORTED");
            break;
          case 70:
            this.heightPercent = typedArray.getFloat(j, 1.0F);
            break;
          case 69:
            this.widthPercent = typedArray.getFloat(j, 1.0F);
            break;
          case 63:
            this.circleAngle = typedArray.getFloat(j, this.circleAngle);
            break;
          case 62:
            this.circleRadius = typedArray.getDimensionPixelSize(j, this.circleRadius);
            break;
          case 61:
            this.circleConstraint = ConstraintSet.lookupID(typedArray, j, this.circleConstraint);
            break;
          case 59:
            this.heightMin = typedArray.getDimensionPixelSize(j, this.heightMin);
            break;
          case 58:
            this.widthMin = typedArray.getDimensionPixelSize(j, this.widthMin);
            break;
          case 57:
            this.heightMax = typedArray.getDimensionPixelSize(j, this.heightMax);
            break;
          case 56:
            this.widthMax = typedArray.getDimensionPixelSize(j, this.widthMax);
            break;
          case 55:
            this.heightDefault = typedArray.getInt(j, this.heightDefault);
            break;
          case 54:
            this.widthDefault = typedArray.getInt(j, this.widthDefault);
            break;
          case 42:
            ConstraintSet.parseDimensionConstraints(this, typedArray, j, 1);
            break;
          case 41:
            ConstraintSet.parseDimensionConstraints(this, typedArray, j, 0);
            break;
          case 40:
            this.verticalChainStyle = typedArray.getInt(j, this.verticalChainStyle);
            break;
          case 39:
            this.horizontalChainStyle = typedArray.getInt(j, this.horizontalChainStyle);
            break;
          case 38:
            this.verticalWeight = typedArray.getFloat(j, this.verticalWeight);
            break;
          case 37:
            this.horizontalWeight = typedArray.getFloat(j, this.horizontalWeight);
            break;
          case 36:
            this.verticalBias = typedArray.getFloat(j, this.verticalBias);
            break;
          case 35:
            this.topToTop = ConstraintSet.lookupID(typedArray, j, this.topToTop);
            break;
          case 34:
            this.topToBottom = ConstraintSet.lookupID(typedArray, j, this.topToBottom);
            break;
          case 33:
            this.topMargin = typedArray.getDimensionPixelSize(j, this.topMargin);
            break;
          case 32:
            this.startToStart = ConstraintSet.lookupID(typedArray, j, this.startToStart);
            break;
          case 31:
            this.startToEnd = ConstraintSet.lookupID(typedArray, j, this.startToEnd);
            break;
          case 30:
            if (Build.VERSION.SDK_INT >= 17)
              this.startMargin = typedArray.getDimensionPixelSize(j, this.startMargin); 
            break;
          case 29:
            this.rightToRight = ConstraintSet.lookupID(typedArray, j, this.rightToRight);
            break;
          case 28:
            this.rightToLeft = ConstraintSet.lookupID(typedArray, j, this.rightToLeft);
            break;
          case 27:
            this.rightMargin = typedArray.getDimensionPixelSize(j, this.rightMargin);
            break;
          case 26:
            this.orientation = typedArray.getInt(j, this.orientation);
            break;
          case 25:
            this.leftToRight = ConstraintSet.lookupID(typedArray, j, this.leftToRight);
            break;
          case 24:
            this.leftToLeft = ConstraintSet.lookupID(typedArray, j, this.leftToLeft);
            break;
          case 23:
            this.leftMargin = typedArray.getDimensionPixelSize(j, this.leftMargin);
            break;
          case 22:
            this.mWidth = typedArray.getLayoutDimension(j, this.mWidth);
            break;
          case 21:
            this.mHeight = typedArray.getLayoutDimension(j, this.mHeight);
            break;
          case 20:
            this.horizontalBias = typedArray.getFloat(j, this.horizontalBias);
            break;
          case 19:
            this.guidePercent = typedArray.getFloat(j, this.guidePercent);
            break;
          case 18:
            this.guideEnd = typedArray.getDimensionPixelOffset(j, this.guideEnd);
            break;
          case 17:
            this.guideBegin = typedArray.getDimensionPixelOffset(j, this.guideBegin);
            break;
          case 16:
            this.goneTopMargin = typedArray.getDimensionPixelSize(j, this.goneTopMargin);
            break;
          case 15:
            this.goneStartMargin = typedArray.getDimensionPixelSize(j, this.goneStartMargin);
            break;
          case 14:
            this.goneRightMargin = typedArray.getDimensionPixelSize(j, this.goneRightMargin);
            break;
          case 13:
            this.goneLeftMargin = typedArray.getDimensionPixelSize(j, this.goneLeftMargin);
            break;
          case 12:
            this.goneEndMargin = typedArray.getDimensionPixelSize(j, this.goneEndMargin);
            break;
          case 11:
            this.goneBottomMargin = typedArray.getDimensionPixelSize(j, this.goneBottomMargin);
            break;
          case 10:
            this.endToStart = ConstraintSet.lookupID(typedArray, j, this.endToStart);
            break;
          case 9:
            this.endToEnd = ConstraintSet.lookupID(typedArray, j, this.endToEnd);
            break;
          case 8:
            if (Build.VERSION.SDK_INT >= 17)
              this.endMargin = typedArray.getDimensionPixelSize(j, this.endMargin); 
            break;
          case 7:
            this.editorAbsoluteY = typedArray.getDimensionPixelOffset(j, this.editorAbsoluteY);
            break;
          case 6:
            this.editorAbsoluteX = typedArray.getDimensionPixelOffset(j, this.editorAbsoluteX);
            break;
          case 5:
            this.dimensionRatio = typedArray.getString(j);
            break;
          case 4:
            this.bottomToTop = ConstraintSet.lookupID(typedArray, j, this.bottomToTop);
            break;
          case 3:
            this.bottomToBottom = ConstraintSet.lookupID(typedArray, j, this.bottomToBottom);
            break;
          case 2:
            this.bottomMargin = typedArray.getDimensionPixelSize(j, this.bottomMargin);
            break;
          case 1:
            this.baselineToBaseline = ConstraintSet.lookupID(typedArray, j, this.baselineToBaseline);
            break;
        } 
      } 
      typedArray.recycle();
    }
  }
  
  public static class Motion {
    private static final int ANIMATE_CIRCLE_ANGLE_TO = 6;
    
    private static final int ANIMATE_RELATIVE_TO = 5;
    
    private static final int INTERPOLATOR_REFERENCE_ID = -2;
    
    private static final int INTERPOLATOR_UNDEFINED = -3;
    
    private static final int MOTION_DRAW_PATH = 4;
    
    private static final int MOTION_STAGGER = 7;
    
    private static final int PATH_MOTION_ARC = 2;
    
    private static final int QUANTIZE_MOTION_INTERPOLATOR = 10;
    
    private static final int QUANTIZE_MOTION_PHASE = 9;
    
    private static final int QUANTIZE_MOTION_STEPS = 8;
    
    private static final int SPLINE_STRING = -1;
    
    private static final int TRANSITION_EASING = 3;
    
    private static final int TRANSITION_PATH_ROTATE = 1;
    
    private static SparseIntArray mapToConstant;
    
    public int mAnimateCircleAngleTo = 0;
    
    public int mAnimateRelativeTo = -1;
    
    public boolean mApply = false;
    
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
    
    static {
      SparseIntArray sparseIntArray = new SparseIntArray();
      mapToConstant = sparseIntArray;
      sparseIntArray.append(R.styleable.Motion_motionPathRotate, 1);
      mapToConstant.append(R.styleable.Motion_pathMotionArc, 2);
      mapToConstant.append(R.styleable.Motion_transitionEasing, 3);
      mapToConstant.append(R.styleable.Motion_drawPath, 4);
      mapToConstant.append(R.styleable.Motion_animateRelativeTo, 5);
      mapToConstant.append(R.styleable.Motion_animateCircleAngleTo, 6);
      mapToConstant.append(R.styleable.Motion_motionStagger, 7);
      mapToConstant.append(R.styleable.Motion_quantizeMotionSteps, 8);
      mapToConstant.append(R.styleable.Motion_quantizeMotionPhase, 9);
      mapToConstant.append(R.styleable.Motion_quantizeMotionInterpolator, 10);
    }
    
    public void copyFrom(Motion param1Motion) {
      this.mApply = param1Motion.mApply;
      this.mAnimateRelativeTo = param1Motion.mAnimateRelativeTo;
      this.mTransitionEasing = param1Motion.mTransitionEasing;
      this.mPathMotionArc = param1Motion.mPathMotionArc;
      this.mDrawPath = param1Motion.mDrawPath;
      this.mPathRotate = param1Motion.mPathRotate;
      this.mMotionStagger = param1Motion.mMotionStagger;
      this.mPolarRelativeTo = param1Motion.mPolarRelativeTo;
    }
    
    void fillFromAttributeList(Context param1Context, AttributeSet param1AttributeSet) {
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.Motion);
      this.mApply = true;
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        TypedValue typedValue;
        int j = typedArray.getIndex(b);
        switch (mapToConstant.get(j)) {
          case 10:
            typedValue = typedArray.peekValue(j);
            if (typedValue.type == 1) {
              j = typedArray.getResourceId(j, -1);
              this.mQuantizeInterpolatorID = j;
              if (j != -1)
                this.mQuantizeInterpolatorType = -2; 
              break;
            } 
            if (typedValue.type == 3) {
              String str = typedArray.getString(j);
              this.mQuantizeInterpolatorString = str;
              if (str.indexOf("/") > 0) {
                this.mQuantizeInterpolatorID = typedArray.getResourceId(j, -1);
                this.mQuantizeInterpolatorType = -2;
                break;
              } 
              this.mQuantizeInterpolatorType = -1;
              break;
            } 
            this.mQuantizeInterpolatorType = typedArray.getInteger(j, this.mQuantizeInterpolatorID);
            break;
          case 9:
            this.mQuantizeMotionPhase = typedArray.getFloat(j, this.mQuantizeMotionPhase);
            break;
          case 8:
            this.mQuantizeMotionSteps = typedArray.getInteger(j, this.mQuantizeMotionSteps);
            break;
          case 7:
            this.mMotionStagger = typedArray.getFloat(j, this.mMotionStagger);
            break;
          case 6:
            this.mAnimateCircleAngleTo = typedArray.getInteger(j, this.mAnimateCircleAngleTo);
            break;
          case 5:
            this.mAnimateRelativeTo = ConstraintSet.lookupID(typedArray, j, this.mAnimateRelativeTo);
            break;
          case 4:
            this.mDrawPath = typedArray.getInt(j, 0);
            break;
          case 3:
            if ((typedArray.peekValue(j)).type == 3) {
              this.mTransitionEasing = typedArray.getString(j);
              break;
            } 
            this.mTransitionEasing = Easing.NAMED_EASING[typedArray.getInteger(j, 0)];
            break;
          case 2:
            this.mPathMotionArc = typedArray.getInt(j, this.mPathMotionArc);
            break;
          case 1:
            this.mPathRotate = typedArray.getFloat(j, this.mPathRotate);
            break;
        } 
      } 
      typedArray.recycle();
    }
  }
  
  public static class PropertySet {
    public float alpha = 1.0F;
    
    public boolean mApply = false;
    
    public float mProgress = Float.NaN;
    
    public int mVisibilityMode = 0;
    
    public int visibility = 0;
    
    public void copyFrom(PropertySet param1PropertySet) {
      this.mApply = param1PropertySet.mApply;
      this.visibility = param1PropertySet.visibility;
      this.alpha = param1PropertySet.alpha;
      this.mProgress = param1PropertySet.mProgress;
      this.mVisibilityMode = param1PropertySet.mVisibilityMode;
    }
    
    void fillFromAttributeList(Context param1Context, AttributeSet param1AttributeSet) {
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.PropertySet);
      this.mApply = true;
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        if (j == R.styleable.PropertySet_android_alpha) {
          this.alpha = typedArray.getFloat(j, this.alpha);
        } else if (j == R.styleable.PropertySet_android_visibility) {
          this.visibility = typedArray.getInt(j, this.visibility);
          this.visibility = ConstraintSet.VISIBILITY_FLAGS[this.visibility];
        } else if (j == R.styleable.PropertySet_visibilityMode) {
          this.mVisibilityMode = typedArray.getInt(j, this.mVisibilityMode);
        } else if (j == R.styleable.PropertySet_motionProgress) {
          this.mProgress = typedArray.getFloat(j, this.mProgress);
        } 
      } 
      typedArray.recycle();
    }
  }
  
  public static class Transform {
    private static final int ELEVATION = 11;
    
    private static final int ROTATION = 1;
    
    private static final int ROTATION_X = 2;
    
    private static final int ROTATION_Y = 3;
    
    private static final int SCALE_X = 4;
    
    private static final int SCALE_Y = 5;
    
    private static final int TRANSFORM_PIVOT_TARGET = 12;
    
    private static final int TRANSFORM_PIVOT_X = 6;
    
    private static final int TRANSFORM_PIVOT_Y = 7;
    
    private static final int TRANSLATION_X = 8;
    
    private static final int TRANSLATION_Y = 9;
    
    private static final int TRANSLATION_Z = 10;
    
    private static SparseIntArray mapToConstant;
    
    public boolean applyElevation = false;
    
    public float elevation = 0.0F;
    
    public boolean mApply = false;
    
    public float rotation = 0.0F;
    
    public float rotationX = 0.0F;
    
    public float rotationY = 0.0F;
    
    public float scaleX = 1.0F;
    
    public float scaleY = 1.0F;
    
    public int transformPivotTarget = -1;
    
    public float transformPivotX = Float.NaN;
    
    public float transformPivotY = Float.NaN;
    
    public float translationX = 0.0F;
    
    public float translationY = 0.0F;
    
    public float translationZ = 0.0F;
    
    static {
      SparseIntArray sparseIntArray = new SparseIntArray();
      mapToConstant = sparseIntArray;
      sparseIntArray.append(R.styleable.Transform_android_rotation, 1);
      mapToConstant.append(R.styleable.Transform_android_rotationX, 2);
      mapToConstant.append(R.styleable.Transform_android_rotationY, 3);
      mapToConstant.append(R.styleable.Transform_android_scaleX, 4);
      mapToConstant.append(R.styleable.Transform_android_scaleY, 5);
      mapToConstant.append(R.styleable.Transform_android_transformPivotX, 6);
      mapToConstant.append(R.styleable.Transform_android_transformPivotY, 7);
      mapToConstant.append(R.styleable.Transform_android_translationX, 8);
      mapToConstant.append(R.styleable.Transform_android_translationY, 9);
      mapToConstant.append(R.styleable.Transform_android_translationZ, 10);
      mapToConstant.append(R.styleable.Transform_android_elevation, 11);
      mapToConstant.append(R.styleable.Transform_transformPivotTarget, 12);
    }
    
    public void copyFrom(Transform param1Transform) {
      this.mApply = param1Transform.mApply;
      this.rotation = param1Transform.rotation;
      this.rotationX = param1Transform.rotationX;
      this.rotationY = param1Transform.rotationY;
      this.scaleX = param1Transform.scaleX;
      this.scaleY = param1Transform.scaleY;
      this.transformPivotX = param1Transform.transformPivotX;
      this.transformPivotY = param1Transform.transformPivotY;
      this.transformPivotTarget = param1Transform.transformPivotTarget;
      this.translationX = param1Transform.translationX;
      this.translationY = param1Transform.translationY;
      this.translationZ = param1Transform.translationZ;
      this.applyElevation = param1Transform.applyElevation;
      this.elevation = param1Transform.elevation;
    }
    
    void fillFromAttributeList(Context param1Context, AttributeSet param1AttributeSet) {
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.Transform);
      this.mApply = true;
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        switch (mapToConstant.get(j)) {
          case 12:
            this.transformPivotTarget = ConstraintSet.lookupID(typedArray, j, this.transformPivotTarget);
            break;
          case 11:
            if (Build.VERSION.SDK_INT >= 21) {
              this.applyElevation = true;
              this.elevation = typedArray.getDimension(j, this.elevation);
            } 
            break;
          case 10:
            if (Build.VERSION.SDK_INT >= 21)
              this.translationZ = typedArray.getDimension(j, this.translationZ); 
            break;
          case 9:
            this.translationY = typedArray.getDimension(j, this.translationY);
            break;
          case 8:
            this.translationX = typedArray.getDimension(j, this.translationX);
            break;
          case 7:
            this.transformPivotY = typedArray.getDimension(j, this.transformPivotY);
            break;
          case 6:
            this.transformPivotX = typedArray.getDimension(j, this.transformPivotX);
            break;
          case 5:
            this.scaleY = typedArray.getFloat(j, this.scaleY);
            break;
          case 4:
            this.scaleX = typedArray.getFloat(j, this.scaleX);
            break;
          case 3:
            this.rotationY = typedArray.getFloat(j, this.rotationY);
            break;
          case 2:
            this.rotationX = typedArray.getFloat(j, this.rotationX);
            break;
          case 1:
            this.rotation = typedArray.getFloat(j, this.rotation);
            break;
        } 
      } 
      typedArray.recycle();
    }
  }
  
  class WriteJsonEngine {
    private static final String SPACE = "       ";
    
    final String BASELINE = "'baseline'";
    
    final String BOTTOM = "'bottom'";
    
    final String END = "'end'";
    
    final String LEFT = "'left'";
    
    final String RIGHT = "'right'";
    
    final String START = "'start'";
    
    final String TOP = "'top'";
    
    Context context;
    
    int flags;
    
    HashMap<Integer, String> idMap = new HashMap<>();
    
    ConstraintLayout layout;
    
    final ConstraintSet this$0;
    
    int unknownCount = 0;
    
    Writer writer;
    
    WriteJsonEngine(Writer param1Writer, ConstraintLayout param1ConstraintLayout, int param1Int) throws IOException {
      this.writer = param1Writer;
      this.layout = param1ConstraintLayout;
      this.context = param1ConstraintLayout.getContext();
      this.flags = param1Int;
    }
    
    private void writeDimension(String param1String, int param1Int1, int param1Int2, float param1Float, int param1Int3, int param1Int4, boolean param1Boolean) throws IOException {
      if (param1Int1 == 0) {
        if (param1Int4 != -1 || param1Int3 != -1) {
          switch (param1Int2) {
            default:
              return;
            case 2:
              this.writer.write((new StringBuilder(String.valueOf(param1String).length() + 56)).append("       ").append(param1String).append(": {'").append(param1Float).append("'% ,").append(param1Int3).append(", ").append(param1Int4).append("}\n").toString());
              return;
            case 1:
              this.writer.write((new StringBuilder(String.valueOf(param1String).length() + 44)).append("       ").append(param1String).append(": {'wrap' ,").append(param1Int3).append(", ").append(param1Int4).append("}\n").toString());
              return;
            case 0:
              break;
          } 
          this.writer.write((new StringBuilder(String.valueOf(param1String).length() + 46)).append("       ").append(param1String).append(": {'spread' ,").append(param1Int3).append(", ").append(param1Int4).append("}\n").toString());
        } 
        switch (param1Int2) {
          case 2:
            this.writer.write((new StringBuilder(String.valueOf(param1String).length() + 29)).append("       ").append(param1String).append(": '").append(param1Float).append("%',\n").toString());
            return;
          case 1:
            this.writer.write((new StringBuilder(String.valueOf(param1String).length() + 24)).append("       ").append(param1String).append(": '???????????',\n").toString());
            return;
        } 
      } else if (param1Int1 == -2) {
        this.writer.write((new StringBuilder(String.valueOf(param1String).length() + 16)).append("       ").append(param1String).append(": 'wrap'\n").toString());
      } else if (param1Int1 == -1) {
        this.writer.write((new StringBuilder(String.valueOf(param1String).length() + 18)).append("       ").append(param1String).append(": 'parent'\n").toString());
      } else {
        this.writer.write((new StringBuilder(String.valueOf(param1String).length() + 22)).append("       ").append(param1String).append(": ").append(param1Int1).append(",\n").toString());
      } 
    }
    
    private void writeGuideline(int param1Int1, int param1Int2, int param1Int3, float param1Float) {}
    
    String getName(int param1Int) {
      if (this.idMap.containsKey(Integer.valueOf(param1Int))) {
        String str1 = this.idMap.get(Integer.valueOf(param1Int));
        return (new StringBuilder(String.valueOf(str1).length() + 2)).append("'").append(str1).append("'").toString();
      } 
      if (param1Int == 0)
        return "'parent'"; 
      String str = lookup(param1Int);
      this.idMap.put(Integer.valueOf(param1Int), str);
      return (new StringBuilder(String.valueOf(str).length() + 2)).append("'").append(str).append("'").toString();
    }
    
    String lookup(int param1Int) {
      if (param1Int != -1)
        try {
          return this.context.getResources().getResourceEntryName(param1Int);
        } catch (Exception exception) {
          param1Int = this.unknownCount + 1;
          this.unknownCount = param1Int;
          return (new StringBuilder(18)).append("unknown").append(param1Int).toString();
        }  
      param1Int = this.unknownCount + 1;
      this.unknownCount = param1Int;
      StringBuilder stringBuilder = new StringBuilder();
      this(18);
      return stringBuilder.append("unknown").append(param1Int).toString();
    }
    
    void writeCircle(int param1Int1, float param1Float, int param1Int2) throws IOException {
      if (param1Int1 == -1)
        return; 
      this.writer.write("       circle");
      this.writer.write(":[");
      this.writer.write(getName(param1Int1));
      this.writer.write((new StringBuilder(17)).append(", ").append(param1Float).toString());
      this.writer.write((new StringBuilder(12)).append(param1Int2).append("]").toString());
    }
    
    void writeConstraint(String param1String1, int param1Int1, String param1String2, int param1Int2, int param1Int3) throws IOException {
      if (param1Int1 == -1)
        return; 
      Writer writer = this.writer;
      String str = String.valueOf("       ");
      param1String1 = String.valueOf(param1String1);
      if (param1String1.length() != 0) {
        param1String1 = str.concat(param1String1);
      } else {
        param1String1 = new String(str);
      } 
      writer.write(param1String1);
      this.writer.write(":[");
      this.writer.write(getName(param1Int1));
      this.writer.write(" , ");
      this.writer.write(param1String2);
      if (param1Int2 != 0)
        this.writer.write((new StringBuilder(14)).append(" , ").append(param1Int2).toString()); 
      this.writer.write("],\n");
    }
    
    void writeLayout() throws IOException {
      this.writer.write("\n'ConstraintSet':{\n");
      for (Integer integer : ConstraintSet.this.mConstraints.keySet()) {
        ConstraintSet.Constraint constraint = (ConstraintSet.Constraint)ConstraintSet.this.mConstraints.get(integer);
        String str = getName(integer.intValue());
        this.writer.write(String.valueOf(str).concat(":{\n"));
        ConstraintSet.Layout layout = constraint.layout;
        writeDimension("height", layout.mHeight, layout.heightDefault, layout.heightPercent, layout.heightMin, layout.heightMax, layout.constrainedHeight);
        writeDimension("width", layout.mWidth, layout.widthDefault, layout.widthPercent, layout.widthMin, layout.widthMax, layout.constrainedWidth);
        writeConstraint("'left'", layout.leftToLeft, "'left'", layout.leftMargin, layout.goneLeftMargin);
        writeConstraint("'left'", layout.leftToRight, "'right'", layout.leftMargin, layout.goneLeftMargin);
        writeConstraint("'right'", layout.rightToLeft, "'left'", layout.rightMargin, layout.goneRightMargin);
        writeConstraint("'right'", layout.rightToRight, "'right'", layout.rightMargin, layout.goneRightMargin);
        writeConstraint("'baseline'", layout.baselineToBaseline, "'baseline'", -1, layout.goneBaselineMargin);
        writeConstraint("'baseline'", layout.baselineToTop, "'top'", -1, layout.goneBaselineMargin);
        writeConstraint("'baseline'", layout.baselineToBottom, "'bottom'", -1, layout.goneBaselineMargin);
        writeConstraint("'top'", layout.topToBottom, "'bottom'", layout.topMargin, layout.goneTopMargin);
        writeConstraint("'top'", layout.topToTop, "'top'", layout.topMargin, layout.goneTopMargin);
        writeConstraint("'bottom'", layout.bottomToBottom, "'bottom'", layout.bottomMargin, layout.goneBottomMargin);
        writeConstraint("'bottom'", layout.bottomToTop, "'top'", layout.bottomMargin, layout.goneBottomMargin);
        writeConstraint("'start'", layout.startToStart, "'start'", layout.startMargin, layout.goneStartMargin);
        writeConstraint("'start'", layout.startToEnd, "'end'", layout.startMargin, layout.goneStartMargin);
        writeConstraint("'end'", layout.endToStart, "'start'", layout.endMargin, layout.goneEndMargin);
        writeConstraint("'end'", layout.endToEnd, "'end'", layout.endMargin, layout.goneEndMargin);
        writeVariable("'horizontalBias'", layout.horizontalBias, 0.5F);
        writeVariable("'verticalBias'", layout.verticalBias, 0.5F);
        writeCircle(layout.circleConstraint, layout.circleAngle, layout.circleRadius);
        writeGuideline(layout.orientation, layout.guideBegin, layout.guideEnd, layout.guidePercent);
        writeVariable("'dimensionRatio'", layout.dimensionRatio);
        writeVariable("'barrierMargin'", layout.mBarrierMargin);
        writeVariable("'type'", layout.mHelperType);
        writeVariable("'ReferenceId'", layout.mReferenceIdString);
        writeVariable("'mBarrierAllowsGoneWidgets'", layout.mBarrierAllowsGoneWidgets, true);
        writeVariable("'WrapBehavior'", layout.mWrapBehavior);
        writeVariable("'verticalWeight'", layout.verticalWeight);
        writeVariable("'horizontalWeight'", layout.horizontalWeight);
        writeVariable("'horizontalChainStyle'", layout.horizontalChainStyle);
        writeVariable("'verticalChainStyle'", layout.verticalChainStyle);
        writeVariable("'barrierDirection'", layout.mBarrierDirection);
        if (layout.mReferenceIds != null)
          writeVariable("'ReferenceIds'", layout.mReferenceIds); 
        this.writer.write("}\n");
      } 
      this.writer.write("}\n");
    }
    
    void writeVariable(String param1String, float param1Float) throws IOException {
      if (param1Float == -1.0F)
        return; 
      Writer writer = this.writer;
      String str = String.valueOf("       ");
      param1String = String.valueOf(param1String);
      if (param1String.length() != 0) {
        param1String = str.concat(param1String);
      } else {
        param1String = new String(str);
      } 
      writer.write(param1String);
      this.writer.write((new StringBuilder(17)).append(": ").append(param1Float).toString());
      this.writer.write(",\n");
    }
    
    void writeVariable(String param1String, float param1Float1, float param1Float2) throws IOException {
      if (param1Float1 == param1Float2)
        return; 
      Writer writer = this.writer;
      String str = String.valueOf("       ");
      param1String = String.valueOf(param1String);
      if (param1String.length() != 0) {
        param1String = str.concat(param1String);
      } else {
        param1String = new String(str);
      } 
      writer.write(param1String);
      this.writer.write((new StringBuilder(17)).append(": ").append(param1Float1).toString());
      this.writer.write(",\n");
    }
    
    void writeVariable(String param1String, int param1Int) throws IOException {
      if (param1Int == 0 || param1Int == -1)
        return; 
      Writer writer = this.writer;
      String str = String.valueOf("       ");
      param1String = String.valueOf(param1String);
      if (param1String.length() != 0) {
        param1String = str.concat(param1String);
      } else {
        param1String = new String(str);
      } 
      writer.write(param1String);
      this.writer.write(":");
      this.writer.write((new StringBuilder(13)).append(", ").append(param1Int).toString());
      this.writer.write("\n");
    }
    
    void writeVariable(String param1String1, String param1String2) throws IOException {
      if (param1String2 == null)
        return; 
      Writer writer = this.writer;
      String str = String.valueOf("       ");
      param1String1 = String.valueOf(param1String1);
      if (param1String1.length() != 0) {
        param1String1 = str.concat(param1String1);
      } else {
        param1String1 = new String(str);
      } 
      writer.write(param1String1);
      this.writer.write(":");
      writer = this.writer;
      param1String1 = String.valueOf(param1String2);
      if (param1String1.length() != 0) {
        param1String1 = ", ".concat(param1String1);
      } else {
        param1String1 = new String(", ");
      } 
      writer.write(param1String1);
      this.writer.write("\n");
    }
    
    void writeVariable(String param1String, boolean param1Boolean) throws IOException {
      if (!param1Boolean)
        return; 
      Writer writer = this.writer;
      String str = String.valueOf("       ");
      param1String = String.valueOf(param1String);
      if (param1String.length() != 0) {
        param1String = str.concat(param1String);
      } else {
        param1String = new String(str);
      } 
      writer.write(param1String);
      this.writer.write((new StringBuilder(7)).append(": ").append(param1Boolean).toString());
      this.writer.write(",\n");
    }
    
    void writeVariable(String param1String, boolean param1Boolean1, boolean param1Boolean2) throws IOException {
      if (param1Boolean1 == param1Boolean2)
        return; 
      Writer writer = this.writer;
      String str = String.valueOf("       ");
      param1String = String.valueOf(param1String);
      if (param1String.length() != 0) {
        param1String = str.concat(param1String);
      } else {
        param1String = new String(str);
      } 
      writer.write(param1String);
      this.writer.write((new StringBuilder(7)).append(": ").append(param1Boolean1).toString());
      this.writer.write(",\n");
    }
    
    void writeVariable(String param1String, int[] param1ArrayOfint) throws IOException {
      if (param1ArrayOfint == null)
        return; 
      Writer writer = this.writer;
      String str = String.valueOf("       ");
      param1String = String.valueOf(param1String);
      if (param1String.length() != 0) {
        param1String = str.concat(param1String);
      } else {
        param1String = new String(str);
      } 
      writer.write(param1String);
      this.writer.write(": ");
      for (byte b = 0; b < param1ArrayOfint.length; b++) {
        writer = this.writer;
        if (b == 0) {
          param1String = "[";
        } else {
          param1String = ", ";
        } 
        param1String = String.valueOf(param1String);
        str = String.valueOf(getName(param1ArrayOfint[b]));
        if (str.length() != 0) {
          param1String = param1String.concat(str);
        } else {
          param1String = new String(param1String);
        } 
        writer.write(param1String);
      } 
      this.writer.write("],\n");
    }
  }
  
  class WriteXmlEngine {
    private static final String SPACE = "\n       ";
    
    final String BASELINE = "'baseline'";
    
    final String BOTTOM = "'bottom'";
    
    final String END = "'end'";
    
    final String LEFT = "'left'";
    
    final String RIGHT = "'right'";
    
    final String START = "'start'";
    
    final String TOP = "'top'";
    
    Context context;
    
    int flags;
    
    HashMap<Integer, String> idMap = new HashMap<>();
    
    ConstraintLayout layout;
    
    final ConstraintSet this$0;
    
    int unknownCount = 0;
    
    Writer writer;
    
    WriteXmlEngine(Writer param1Writer, ConstraintLayout param1ConstraintLayout, int param1Int) throws IOException {
      this.writer = param1Writer;
      this.layout = param1ConstraintLayout;
      this.context = param1ConstraintLayout.getContext();
      this.flags = param1Int;
    }
    
    private void writeBaseDimension(String param1String, int param1Int1, int param1Int2) throws IOException {
      if (param1Int1 != param1Int2)
        if (param1Int1 == -2) {
          this.writer.write((new StringBuilder(String.valueOf(param1String).length() + 23)).append("\n       ").append(param1String).append("=\"wrap_content\"").toString());
        } else if (param1Int1 == -1) {
          this.writer.write((new StringBuilder(String.valueOf(param1String).length() + 23)).append("\n       ").append(param1String).append("=\"match_parent\"").toString());
        } else {
          this.writer.write((new StringBuilder(String.valueOf(param1String).length() + 24)).append("\n       ").append(param1String).append("=\"").append(param1Int1).append("dp\"").toString());
        }  
    }
    
    private void writeBoolen(String param1String, boolean param1Boolean1, boolean param1Boolean2) throws IOException {
      if (param1Boolean1 != param1Boolean2)
        this.writer.write((new StringBuilder(String.valueOf(param1String).length() + 18)).append("\n       ").append(param1String).append("=\"").append(param1Boolean1).append("dp\"").toString()); 
    }
    
    private void writeDimension(String param1String, int param1Int1, int param1Int2) throws IOException {
      if (param1Int1 != param1Int2)
        this.writer.write((new StringBuilder(String.valueOf(param1String).length() + 24)).append("\n       ").append(param1String).append("=\"").append(param1Int1).append("dp\"").toString()); 
    }
    
    private void writeEnum(String param1String, int param1Int1, String[] param1ArrayOfString, int param1Int2) throws IOException {
      if (param1Int1 != param1Int2) {
        Writer writer = this.writer;
        String str = param1ArrayOfString[param1Int1];
        writer.write((new StringBuilder(String.valueOf(param1String).length() + 11 + String.valueOf(str).length())).append("\n       ").append(param1String).append("=\"").append(str).append("\"").toString());
      } 
    }
    
    String getName(int param1Int) {
      if (this.idMap.containsKey(Integer.valueOf(param1Int))) {
        String str1 = this.idMap.get(Integer.valueOf(param1Int));
        return (new StringBuilder(String.valueOf(str1).length() + 5)).append("@+id/").append(str1).toString();
      } 
      if (param1Int == 0)
        return "parent"; 
      String str = lookup(param1Int);
      this.idMap.put(Integer.valueOf(param1Int), str);
      return (new StringBuilder(String.valueOf(str).length() + 5)).append("@+id/").append(str).toString();
    }
    
    String lookup(int param1Int) {
      if (param1Int != -1)
        try {
          return this.context.getResources().getResourceEntryName(param1Int);
        } catch (Exception exception) {
          param1Int = this.unknownCount + 1;
          this.unknownCount = param1Int;
          return (new StringBuilder(18)).append("unknown").append(param1Int).toString();
        }  
      param1Int = this.unknownCount + 1;
      this.unknownCount = param1Int;
      StringBuilder stringBuilder = new StringBuilder();
      this(18);
      return stringBuilder.append("unknown").append(param1Int).toString();
    }
    
    void writeCircle(int param1Int1, float param1Float, int param1Int2) throws IOException {
      if (param1Int1 == -1)
        return; 
      this.writer.write("circle");
      this.writer.write(":[");
      this.writer.write(getName(param1Int1));
      this.writer.write((new StringBuilder(17)).append(", ").append(param1Float).toString());
      this.writer.write((new StringBuilder(12)).append(param1Int2).append("]").toString());
    }
    
    void writeConstraint(String param1String1, int param1Int1, String param1String2, int param1Int2, int param1Int3) throws IOException {
      if (param1Int1 == -1)
        return; 
      Writer writer = this.writer;
      String str = String.valueOf("\n       ");
      param1String1 = String.valueOf(param1String1);
      if (param1String1.length() != 0) {
        param1String1 = str.concat(param1String1);
      } else {
        param1String1 = new String(str);
      } 
      writer.write(param1String1);
      this.writer.write(":[");
      this.writer.write(getName(param1Int1));
      this.writer.write(" , ");
      this.writer.write(param1String2);
      if (param1Int2 != 0)
        this.writer.write((new StringBuilder(14)).append(" , ").append(param1Int2).toString()); 
      this.writer.write("],\n");
    }
    
    void writeLayout() throws IOException {
      this.writer.write("\n<ConstraintSet>\n");
      for (Integer integer : ConstraintSet.this.mConstraints.keySet()) {
        ConstraintSet.Constraint constraint = (ConstraintSet.Constraint)ConstraintSet.this.mConstraints.get(integer);
        String str = getName(integer.intValue());
        this.writer.write("  <Constraint");
        this.writer.write((new StringBuilder(String.valueOf(str).length() + 21)).append("\n       android:id=\"").append(str).append("\"").toString());
        ConstraintSet.Layout layout = constraint.layout;
        writeBaseDimension("android:layout_width", layout.mWidth, -5);
        writeBaseDimension("android:layout_height", layout.mHeight, -5);
        writeVariable("app:layout_constraintGuide_begin", layout.guideBegin, -1.0F);
        writeVariable("app:layout_constraintGuide_end", layout.guideEnd, -1.0F);
        writeVariable("app:layout_constraintGuide_percent", layout.guidePercent, -1.0F);
        writeVariable("app:layout_constraintHorizontal_bias", layout.horizontalBias, 0.5F);
        writeVariable("app:layout_constraintVertical_bias", layout.verticalBias, 0.5F);
        writeVariable("app:layout_constraintDimensionRatio", layout.dimensionRatio, (String)null);
        writeXmlConstraint("app:layout_constraintCircle", layout.circleConstraint);
        writeVariable("app:layout_constraintCircleRadius", layout.circleRadius, 0.0F);
        writeVariable("app:layout_constraintCircleAngle", layout.circleAngle, 0.0F);
        writeVariable("android:orientation", layout.orientation, -1.0F);
        writeVariable("app:layout_constraintVertical_weight", layout.verticalWeight, -1.0F);
        writeVariable("app:layout_constraintHorizontal_weight", layout.horizontalWeight, -1.0F);
        writeVariable("app:layout_constraintHorizontal_chainStyle", layout.horizontalChainStyle, 0.0F);
        writeVariable("app:layout_constraintVertical_chainStyle", layout.verticalChainStyle, 0.0F);
        writeVariable("app:barrierDirection", layout.mBarrierDirection, -1.0F);
        writeVariable("app:barrierMargin", layout.mBarrierMargin, 0.0F);
        writeDimension("app:layout_marginLeft", layout.leftMargin, 0);
        writeDimension("app:layout_goneMarginLeft", layout.goneLeftMargin, -2147483648);
        writeDimension("app:layout_marginRight", layout.rightMargin, 0);
        writeDimension("app:layout_goneMarginRight", layout.goneRightMargin, -2147483648);
        writeDimension("app:layout_marginStart", layout.startMargin, 0);
        writeDimension("app:layout_goneMarginStart", layout.goneStartMargin, -2147483648);
        writeDimension("app:layout_marginEnd", layout.endMargin, 0);
        writeDimension("app:layout_goneMarginEnd", layout.goneEndMargin, -2147483648);
        writeDimension("app:layout_marginTop", layout.topMargin, 0);
        writeDimension("app:layout_goneMarginTop", layout.goneTopMargin, -2147483648);
        writeDimension("app:layout_marginBottom", layout.bottomMargin, 0);
        writeDimension("app:layout_goneMarginBottom", layout.goneBottomMargin, -2147483648);
        writeDimension("app:goneBaselineMargin", layout.goneBaselineMargin, -2147483648);
        writeDimension("app:baselineMargin", layout.baselineMargin, 0);
        writeBoolen("app:layout_constrainedWidth", layout.constrainedWidth, false);
        writeBoolen("app:layout_constrainedHeight", layout.constrainedHeight, false);
        writeBoolen("app:barrierAllowsGoneWidgets", layout.mBarrierAllowsGoneWidgets, true);
        writeVariable("app:layout_wrapBehaviorInParent", layout.mWrapBehavior, 0.0F);
        writeXmlConstraint("app:baselineToBaseline", layout.baselineToBaseline);
        writeXmlConstraint("app:baselineToBottom", layout.baselineToBottom);
        writeXmlConstraint("app:baselineToTop", layout.baselineToTop);
        writeXmlConstraint("app:layout_constraintBottom_toBottomOf", layout.bottomToBottom);
        writeXmlConstraint("app:layout_constraintBottom_toTopOf", layout.bottomToTop);
        writeXmlConstraint("app:layout_constraintEnd_toEndOf", layout.endToEnd);
        writeXmlConstraint("app:layout_constraintEnd_toStartOf", layout.endToStart);
        writeXmlConstraint("app:layout_constraintLeft_toLeftOf", layout.leftToLeft);
        writeXmlConstraint("app:layout_constraintLeft_toRightOf", layout.leftToRight);
        writeXmlConstraint("app:layout_constraintRight_toLeftOf", layout.rightToLeft);
        writeXmlConstraint("app:layout_constraintRight_toRightOf", layout.rightToRight);
        writeXmlConstraint("app:layout_constraintStart_toEndOf", layout.startToEnd);
        writeXmlConstraint("app:layout_constraintStart_toStartOf", layout.startToStart);
        writeXmlConstraint("app:layout_constraintTop_toBottomOf", layout.topToBottom);
        writeXmlConstraint("app:layout_constraintTop_toTopOf", layout.topToTop);
        String[] arrayOfString = new String[3];
        arrayOfString[0] = "spread";
        arrayOfString[1] = "wrap";
        arrayOfString[2] = "percent";
        writeEnum("app:layout_constraintHeight_default", layout.heightDefault, arrayOfString, 0);
        writeVariable("app:layout_constraintHeight_percent", layout.heightPercent, 1.0F);
        writeDimension("app:layout_constraintHeight_min", layout.heightMin, 0);
        writeDimension("app:layout_constraintHeight_max", layout.heightMax, 0);
        writeBoolen("android:layout_constrainedHeight", layout.constrainedHeight, false);
        writeEnum("app:layout_constraintWidth_default", layout.widthDefault, arrayOfString, 0);
        writeVariable("app:layout_constraintWidth_percent", layout.widthPercent, 1.0F);
        writeDimension("app:layout_constraintWidth_min", layout.widthMin, 0);
        writeDimension("app:layout_constraintWidth_max", layout.widthMax, 0);
        writeBoolen("android:layout_constrainedWidth", layout.constrainedWidth, false);
        writeVariable("app:layout_constraintVertical_weight", layout.verticalWeight, -1.0F);
        writeVariable("app:layout_constraintHorizontal_weight", layout.horizontalWeight, -1.0F);
        writeVariable("app:layout_constraintHorizontal_chainStyle", layout.horizontalChainStyle);
        writeVariable("app:layout_constraintVertical_chainStyle", layout.verticalChainStyle);
        writeEnum("app:barrierDirection", layout.mBarrierDirection, new String[] { "left", "right", "top", "bottom", "start", "end" }, -1);
        writeVariable("app:layout_constraintTag", layout.mConstraintTag, (String)null);
        if (layout.mReferenceIds != null)
          writeVariable("'ReferenceIds'", layout.mReferenceIds); 
        this.writer.write(" />\n");
      } 
      this.writer.write("</ConstraintSet>\n");
    }
    
    void writeVariable(String param1String, float param1Float1, float param1Float2) throws IOException {
      if (param1Float1 == param1Float2)
        return; 
      Writer writer = this.writer;
      String str = String.valueOf("\n       ");
      param1String = String.valueOf(param1String);
      if (param1String.length() != 0) {
        param1String = str.concat(param1String);
      } else {
        param1String = new String(str);
      } 
      writer.write(param1String);
      this.writer.write((new StringBuilder(18)).append("=\"").append(param1Float1).append("\"").toString());
    }
    
    void writeVariable(String param1String, int param1Int) throws IOException {
      if (param1Int == 0 || param1Int == -1)
        return; 
      this.writer.write((new StringBuilder(String.valueOf(param1String).length() + 23)).append("\n       ").append(param1String).append("=\"").append(param1Int).append("\"\n").toString());
    }
    
    void writeVariable(String param1String1, String param1String2) throws IOException {
      if (param1String2 == null)
        return; 
      this.writer.write(param1String1);
      this.writer.write(":");
      Writer writer = this.writer;
      param1String1 = String.valueOf(param1String2);
      if (param1String1.length() != 0) {
        param1String1 = ", ".concat(param1String1);
      } else {
        param1String1 = new String(", ");
      } 
      writer.write(param1String1);
      this.writer.write("\n");
    }
    
    void writeVariable(String param1String1, String param1String2, String param1String3) throws IOException {
      if (param1String2 == null || param1String2.equals(param1String3))
        return; 
      Writer writer = this.writer;
      String str = String.valueOf("\n       ");
      param1String1 = String.valueOf(param1String1);
      if (param1String1.length() != 0) {
        param1String1 = str.concat(param1String1);
      } else {
        param1String1 = new String(str);
      } 
      writer.write(param1String1);
      this.writer.write((new StringBuilder(String.valueOf(param1String2).length() + 3)).append("=\"").append(param1String2).append("\"").toString());
    }
    
    void writeVariable(String param1String, int[] param1ArrayOfint) throws IOException {
      if (param1ArrayOfint == null)
        return; 
      Writer writer = this.writer;
      String str = String.valueOf("\n       ");
      param1String = String.valueOf(param1String);
      if (param1String.length() != 0) {
        param1String = str.concat(param1String);
      } else {
        param1String = new String(str);
      } 
      writer.write(param1String);
      this.writer.write(":");
      for (byte b = 0; b < param1ArrayOfint.length; b++) {
        writer = this.writer;
        if (b == 0) {
          param1String = "[";
        } else {
          param1String = ", ";
        } 
        str = String.valueOf(param1String);
        param1String = String.valueOf(getName(param1ArrayOfint[b]));
        if (param1String.length() != 0) {
          param1String = str.concat(param1String);
        } else {
          param1String = new String(str);
        } 
        writer.write(param1String);
      } 
      this.writer.write("],\n");
    }
    
    void writeXmlConstraint(String param1String, int param1Int) throws IOException {
      if (param1Int == -1)
        return; 
      Writer writer = this.writer;
      String str = String.valueOf("\n       ");
      param1String = String.valueOf(param1String);
      if (param1String.length() != 0) {
        param1String = str.concat(param1String);
      } else {
        param1String = new String(str);
      } 
      writer.write(param1String);
      writer = this.writer;
      param1String = getName(param1Int);
      writer.write((new StringBuilder(String.valueOf(param1String).length() + 3)).append("=\"").append(param1String).append("\"").toString());
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\widget\ConstraintSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */