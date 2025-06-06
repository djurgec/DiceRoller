package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.util.SparseArray;
import android.util.Xml;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ConstraintLayoutStates {
  private static final boolean DEBUG = false;
  
  public static final String TAG = "ConstraintLayoutStates";
  
  private final ConstraintLayout mConstraintLayout;
  
  private SparseArray<ConstraintSet> mConstraintSetMap = new SparseArray();
  
  private ConstraintsChangedListener mConstraintsChangedListener = null;
  
  int mCurrentConstraintNumber = -1;
  
  int mCurrentStateId = -1;
  
  ConstraintSet mDefaultConstraintSet;
  
  private SparseArray<State> mStateList = new SparseArray();
  
  ConstraintLayoutStates(Context paramContext, ConstraintLayout paramConstraintLayout, int paramInt) {
    this.mConstraintLayout = paramConstraintLayout;
    load(paramContext, paramInt);
  }
  
  private void load(Context paramContext, int paramInt) {
    XmlResourceParser xmlResourceParser = paramContext.getResources().getXml(paramInt);
    String str = null;
    try {
      paramInt = xmlResourceParser.getEventType();
      while (true) {
        boolean bool = true;
        if (paramInt != 1) {
          String str2;
          State state;
          String str1;
          Variant variant;
          switch (paramInt) {
            case 2:
              str2 = xmlResourceParser.getName();
              switch (str2.hashCode()) {
                default:
                  paramInt = -1;
                  break;
                case 1901439077:
                  if (str2.equals("Variant")) {
                    paramInt = 3;
                    break;
                  } 
                case 1657696882:
                  if (str2.equals("layoutDescription")) {
                    paramInt = 0;
                    break;
                  } 
                case 1382829617:
                  if (str2.equals("StateSet")) {
                    paramInt = bool;
                    break;
                  } 
                case 80204913:
                  if (str2.equals("State")) {
                    paramInt = 2;
                    break;
                  } 
                case -1349929691:
                  if (str2.equals("ConstraintSet")) {
                    paramInt = 4;
                    break;
                  } 
              } 
              switch (paramInt) {
                default:
                  str2 = str;
                  break;
                case 4:
                  parseConstraintSet(paramContext, (XmlPullParser)xmlResourceParser);
                  str2 = str;
                  break;
                case 3:
                  variant = new Variant();
                  this(paramContext, (XmlPullParser)xmlResourceParser);
                  str2 = str;
                  if (str != null) {
                    str.add(variant);
                    str2 = str;
                  } 
                  break;
                case 2:
                  state = new State();
                  this(paramContext, (XmlPullParser)xmlResourceParser);
                  this.mStateList.put(state.mId, state);
                  break;
                case 1:
                  str1 = str;
                  break;
                case 0:
                  str1 = str;
                  break;
              } 
              str = str1;
              break;
            case 0:
              xmlResourceParser.getName();
              break;
          } 
          paramInt = xmlResourceParser.next();
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
  
  private void parseConstraintSet(Context paramContext, XmlPullParser paramXmlPullParser) {
    ConstraintSet constraintSet = new ConstraintSet();
    int j = paramXmlPullParser.getAttributeCount();
    for (int i = 0; i < j; i++) {
      String str2 = paramXmlPullParser.getAttributeName(i);
      String str1 = paramXmlPullParser.getAttributeValue(i);
      if (str2 != null && str1 != null && "id".equals(str2)) {
        i = -1;
        if (str1.contains("/")) {
          str2 = str1.substring(str1.indexOf('/') + 1);
          i = paramContext.getResources().getIdentifier(str2, "id", paramContext.getPackageName());
        } 
        j = i;
        if (i == -1)
          if (str1.length() > 1) {
            j = Integer.parseInt(str1.substring(1));
          } else {
            Log.e("ConstraintLayoutStates", "error in parsing id");
            j = i;
          }  
        constraintSet.load(paramContext, paramXmlPullParser);
        this.mConstraintSetMap.put(j, constraintSet);
        break;
      } 
    } 
  }
  
  public boolean needsToChange(int paramInt, float paramFloat1, float paramFloat2) {
    int i = this.mCurrentStateId;
    if (i != paramInt)
      return true; 
    if (paramInt == -1) {
      object = this.mStateList.valueAt(0);
    } else {
      object = this.mStateList.get(i);
    } 
    Object object = object;
    return (this.mCurrentConstraintNumber != -1 && ((Variant)((State)object).mVariants.get(this.mCurrentConstraintNumber)).match(paramFloat1, paramFloat2)) ? false : (!(this.mCurrentConstraintNumber == object.findMatch(paramFloat1, paramFloat2)));
  }
  
  public void setOnConstraintsChanged(ConstraintsChangedListener paramConstraintsChangedListener) {
    this.mConstraintsChangedListener = paramConstraintsChangedListener;
  }
  
  public void updateConstraints(int paramInt, float paramFloat1, float paramFloat2) {
    int i = this.mCurrentStateId;
    if (i == paramInt) {
      State state;
      ConstraintSet constraintSet;
      if (paramInt == -1) {
        state = (State)this.mStateList.valueAt(0);
      } else {
        state = (State)this.mStateList.get(i);
      } 
      if (this.mCurrentConstraintNumber != -1 && ((Variant)state.mVariants.get(this.mCurrentConstraintNumber)).match(paramFloat1, paramFloat2))
        return; 
      i = state.findMatch(paramFloat1, paramFloat2);
      if (this.mCurrentConstraintNumber == i)
        return; 
      if (i == -1) {
        constraintSet = this.mDefaultConstraintSet;
      } else {
        constraintSet = ((Variant)state.mVariants.get(i)).mConstraintSet;
      } 
      if (i == -1) {
        paramInt = state.mConstraintID;
      } else {
        paramInt = ((Variant)state.mVariants.get(i)).mConstraintID;
      } 
      if (constraintSet == null)
        return; 
      this.mCurrentConstraintNumber = i;
      ConstraintsChangedListener constraintsChangedListener = this.mConstraintsChangedListener;
      if (constraintsChangedListener != null)
        constraintsChangedListener.preLayoutChange(-1, paramInt); 
      constraintSet.applyTo(this.mConstraintLayout);
      constraintsChangedListener = this.mConstraintsChangedListener;
      if (constraintsChangedListener != null)
        constraintsChangedListener.postLayoutChange(-1, paramInt); 
    } else {
      ConstraintSet constraintSet;
      this.mCurrentStateId = paramInt;
      State state = (State)this.mStateList.get(paramInt);
      int j = state.findMatch(paramFloat1, paramFloat2);
      if (j == -1) {
        constraintSet = state.mConstraintSet;
      } else {
        constraintSet = ((Variant)state.mVariants.get(j)).mConstraintSet;
      } 
      if (j == -1) {
        i = state.mConstraintID;
      } else {
        i = ((Variant)state.mVariants.get(j)).mConstraintID;
      } 
      if (constraintSet == null) {
        Log.v("ConstraintLayoutStates", (new StringBuilder(79)).append("NO Constraint set found ! id=").append(paramInt).append(", dim =").append(paramFloat1).append(", ").append(paramFloat2).toString());
        return;
      } 
      this.mCurrentConstraintNumber = j;
      ConstraintsChangedListener constraintsChangedListener2 = this.mConstraintsChangedListener;
      if (constraintsChangedListener2 != null)
        constraintsChangedListener2.preLayoutChange(paramInt, i); 
      constraintSet.applyTo(this.mConstraintLayout);
      ConstraintsChangedListener constraintsChangedListener1 = this.mConstraintsChangedListener;
      if (constraintsChangedListener1 != null)
        constraintsChangedListener1.postLayoutChange(paramInt, i); 
    } 
  }
  
  static class State {
    int mConstraintID = -1;
    
    ConstraintSet mConstraintSet;
    
    int mId;
    
    ArrayList<ConstraintLayoutStates.Variant> mVariants = new ArrayList<>();
    
    public State(Context param1Context, XmlPullParser param1XmlPullParser) {
      TypedArray typedArray = param1Context.obtainStyledAttributes(Xml.asAttributeSet(param1XmlPullParser), R.styleable.State);
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        if (j == R.styleable.State_android_id) {
          this.mId = typedArray.getResourceId(j, this.mId);
        } else if (j == R.styleable.State_constraints) {
          this.mConstraintID = typedArray.getResourceId(j, this.mConstraintID);
          String str = param1Context.getResources().getResourceTypeName(this.mConstraintID);
          param1Context.getResources().getResourceName(this.mConstraintID);
          if ("layout".equals(str)) {
            ConstraintSet constraintSet = new ConstraintSet();
            this.mConstraintSet = constraintSet;
            constraintSet.clone(param1Context, this.mConstraintID);
          } 
        } 
      } 
      typedArray.recycle();
    }
    
    void add(ConstraintLayoutStates.Variant param1Variant) {
      this.mVariants.add(param1Variant);
    }
    
    public int findMatch(float param1Float1, float param1Float2) {
      for (byte b = 0; b < this.mVariants.size(); b++) {
        if (((ConstraintLayoutStates.Variant)this.mVariants.get(b)).match(param1Float1, param1Float2))
          return b; 
      } 
      return -1;
    }
  }
  
  static class Variant {
    int mConstraintID = -1;
    
    ConstraintSet mConstraintSet;
    
    int mId;
    
    float mMaxHeight = Float.NaN;
    
    float mMaxWidth = Float.NaN;
    
    float mMinHeight = Float.NaN;
    
    float mMinWidth = Float.NaN;
    
    public Variant(Context param1Context, XmlPullParser param1XmlPullParser) {
      TypedArray typedArray = param1Context.obtainStyledAttributes(Xml.asAttributeSet(param1XmlPullParser), R.styleable.Variant);
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        if (j == R.styleable.Variant_constraints) {
          this.mConstraintID = typedArray.getResourceId(j, this.mConstraintID);
          String str = param1Context.getResources().getResourceTypeName(this.mConstraintID);
          param1Context.getResources().getResourceName(this.mConstraintID);
          if ("layout".equals(str)) {
            ConstraintSet constraintSet = new ConstraintSet();
            this.mConstraintSet = constraintSet;
            constraintSet.clone(param1Context, this.mConstraintID);
          } 
        } else if (j == R.styleable.Variant_region_heightLessThan) {
          this.mMaxHeight = typedArray.getDimension(j, this.mMaxHeight);
        } else if (j == R.styleable.Variant_region_heightMoreThan) {
          this.mMinHeight = typedArray.getDimension(j, this.mMinHeight);
        } else if (j == R.styleable.Variant_region_widthLessThan) {
          this.mMaxWidth = typedArray.getDimension(j, this.mMaxWidth);
        } else if (j == R.styleable.Variant_region_widthMoreThan) {
          this.mMinWidth = typedArray.getDimension(j, this.mMinWidth);
        } else {
          Log.v("ConstraintLayoutStates", "Unknown tag");
        } 
      } 
      typedArray.recycle();
    }
    
    boolean match(float param1Float1, float param1Float2) {
      return (!Float.isNaN(this.mMinWidth) && param1Float1 < this.mMinWidth) ? false : ((!Float.isNaN(this.mMinHeight) && param1Float2 < this.mMinHeight) ? false : ((!Float.isNaN(this.mMaxWidth) && param1Float1 > this.mMaxWidth) ? false : (!(!Float.isNaN(this.mMaxHeight) && param1Float2 > this.mMaxHeight))));
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\widget\ConstraintLayoutStates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */