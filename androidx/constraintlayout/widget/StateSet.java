package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.util.SparseArray;
import android.util.Xml;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class StateSet {
  private static final boolean DEBUG = false;
  
  public static final String TAG = "ConstraintLayoutStates";
  
  private SparseArray<ConstraintSet> mConstraintSetMap = new SparseArray();
  
  private ConstraintsChangedListener mConstraintsChangedListener = null;
  
  int mCurrentConstraintNumber = -1;
  
  int mCurrentStateId = -1;
  
  ConstraintSet mDefaultConstraintSet;
  
  int mDefaultState = -1;
  
  private SparseArray<State> mStateList = new SparseArray();
  
  public StateSet(Context paramContext, XmlPullParser paramXmlPullParser) {
    load(paramContext, paramXmlPullParser);
  }
  
  private void load(Context paramContext, XmlPullParser paramXmlPullParser) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(Xml.asAttributeSet(paramXmlPullParser), R.styleable.StateSet);
    int j = typedArray.getIndexCount();
    int i;
    for (i = 0; i < j; i++) {
      int k = typedArray.getIndex(i);
      if (k == R.styleable.StateSet_defaultState)
        this.mDefaultState = typedArray.getResourceId(k, this.mDefaultState); 
    } 
    typedArray.recycle();
    typedArray = null;
    try {
      i = paramXmlPullParser.getEventType();
      while (true) {
        j = 1;
        if (i != 1) {
          String str;
          TypedArray typedArray2;
          State state;
          TypedArray typedArray1;
          Variant variant;
          switch (i) {
            case 3:
              if ("StateSet".equals(paramXmlPullParser.getName()))
                return; 
              break;
            case 2:
              str = paramXmlPullParser.getName();
              switch (str.hashCode()) {
                default:
                  i = -1;
                  break;
                case 1901439077:
                  if (str.equals("Variant")) {
                    i = 3;
                    break;
                  } 
                case 1382829617:
                  if (str.equals("StateSet")) {
                    i = j;
                    break;
                  } 
                case 1301459538:
                  if (str.equals("LayoutDescription")) {
                    i = 0;
                    break;
                  } 
                case 80204913:
                  if (str.equals("State")) {
                    i = 2;
                    break;
                  } 
              } 
              switch (i) {
                default:
                  typedArray2 = typedArray;
                  break;
                case 3:
                  variant = new Variant();
                  this(paramContext, paramXmlPullParser);
                  typedArray2 = typedArray;
                  if (typedArray != null) {
                    typedArray.add(variant);
                    typedArray2 = typedArray;
                  } 
                  break;
                case 2:
                  state = new State();
                  this(paramContext, paramXmlPullParser);
                  this.mStateList.put(state.mId, state);
                  break;
                case 1:
                  typedArray1 = typedArray;
                  break;
                case 0:
                  typedArray1 = typedArray;
                  break;
              } 
              typedArray = typedArray1;
              break;
            case 0:
              paramXmlPullParser.getName();
              break;
          } 
          i = paramXmlPullParser.next();
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
  
  public int convertToConstraintSet(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2) {
    State state = (State)this.mStateList.get(paramInt2);
    if (state == null)
      return paramInt2; 
    if (paramFloat1 == -1.0F || paramFloat2 == -1.0F) {
      if (state.mConstraintID == paramInt1)
        return paramInt1; 
      Iterator<Variant> iterator = state.mVariants.iterator();
      while (iterator.hasNext()) {
        if (paramInt1 == ((Variant)iterator.next()).mConstraintID)
          return paramInt1; 
      } 
      return state.mConstraintID;
    } 
    Variant variant = null;
    for (Variant variant1 : state.mVariants) {
      if (variant1.match(paramFloat1, paramFloat2)) {
        if (paramInt1 == variant1.mConstraintID)
          return paramInt1; 
        variant = variant1;
      } 
    } 
    return (variant != null) ? variant.mConstraintID : state.mConstraintID;
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
  
  public int stateGetConstraintID(int paramInt1, int paramInt2, int paramInt3) {
    return updateConstraints(-1, paramInt1, paramInt2, paramInt3);
  }
  
  public int updateConstraints(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2) {
    if (paramInt1 == paramInt2) {
      State state1;
      if (paramInt2 == -1) {
        state1 = (State)this.mStateList.valueAt(0);
      } else {
        state1 = (State)this.mStateList.get(this.mCurrentStateId);
      } 
      if (state1 == null)
        return -1; 
      if (this.mCurrentConstraintNumber != -1 && ((Variant)state1.mVariants.get(paramInt1)).match(paramFloat1, paramFloat2))
        return paramInt1; 
      paramInt2 = state1.findMatch(paramFloat1, paramFloat2);
      if (paramInt1 == paramInt2)
        return paramInt1; 
      if (paramInt2 == -1) {
        paramInt1 = state1.mConstraintID;
      } else {
        paramInt1 = ((Variant)state1.mVariants.get(paramInt2)).mConstraintID;
      } 
      return paramInt1;
    } 
    State state = (State)this.mStateList.get(paramInt2);
    if (state == null)
      return -1; 
    paramInt1 = state.findMatch(paramFloat1, paramFloat2);
    if (paramInt1 == -1) {
      paramInt1 = state.mConstraintID;
    } else {
      paramInt1 = ((Variant)state.mVariants.get(paramInt1)).mConstraintID;
    } 
    return paramInt1;
  }
  
  static class State {
    int mConstraintID = -1;
    
    int mId;
    
    boolean mIsLayout = false;
    
    ArrayList<StateSet.Variant> mVariants = new ArrayList<>();
    
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
          if ("layout".equals(str))
            this.mIsLayout = true; 
        } 
      } 
      typedArray.recycle();
    }
    
    void add(StateSet.Variant param1Variant) {
      this.mVariants.add(param1Variant);
    }
    
    public int findMatch(float param1Float1, float param1Float2) {
      for (byte b = 0; b < this.mVariants.size(); b++) {
        if (((StateSet.Variant)this.mVariants.get(b)).match(param1Float1, param1Float2))
          return b; 
      } 
      return -1;
    }
  }
  
  static class Variant {
    int mConstraintID = -1;
    
    int mId;
    
    boolean mIsLayout = false;
    
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
          if ("layout".equals(str))
            this.mIsLayout = true; 
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


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\widget\StateSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */