package androidx.constraintlayout.helper.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.R;
import androidx.constraintlayout.widget.VirtualLayout;
import java.util.Arrays;

public class CircularFlow extends VirtualLayout {
  private static float DEFAULT_ANGLE = 0.0F;
  
  private static int DEFAULT_RADIUS = 0;
  
  private static final String TAG = "CircularFlow";
  
  private float[] mAngles;
  
  ConstraintLayout mContainer;
  
  private int mCountAngle;
  
  private int mCountRadius;
  
  private int[] mRadius;
  
  private String mReferenceAngles;
  
  private Float mReferenceDefaultAngle;
  
  private Integer mReferenceDefaultRadius;
  
  private String mReferenceRadius;
  
  int mViewCenter;
  
  static {
    DEFAULT_ANGLE = 0.0F;
  }
  
  public CircularFlow(Context paramContext) {
    super(paramContext);
  }
  
  public CircularFlow(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  public CircularFlow(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  private void addAngle(String paramString) {
    if (paramString == null || paramString.length() == 0)
      return; 
    if (this.myContext == null)
      return; 
    float[] arrayOfFloat = this.mAngles;
    if (arrayOfFloat == null)
      return; 
    if (this.mCountAngle + 1 > arrayOfFloat.length)
      this.mAngles = Arrays.copyOf(arrayOfFloat, arrayOfFloat.length + 1); 
    this.mAngles[this.mCountAngle] = Integer.parseInt(paramString);
    this.mCountAngle++;
  }
  
  private void addRadius(String paramString) {
    if (paramString == null || paramString.length() == 0)
      return; 
    if (this.myContext == null)
      return; 
    int[] arrayOfInt = this.mRadius;
    if (arrayOfInt == null)
      return; 
    if (this.mCountRadius + 1 > arrayOfInt.length)
      this.mRadius = Arrays.copyOf(arrayOfInt, arrayOfInt.length + 1); 
    this.mRadius[this.mCountRadius] = (int)(Integer.parseInt(paramString) * (this.myContext.getResources().getDisplayMetrics()).density);
    this.mCountRadius++;
  }
  
  private void anchorReferences() {
    this.mContainer = (ConstraintLayout)getParent();
    for (byte b = 0; b < this.mCount; b++) {
      View view = this.mContainer.getViewById(this.mIds[b]);
      if (view != null) {
        int i = DEFAULT_RADIUS;
        float f = DEFAULT_ANGLE;
        int[] arrayOfInt = this.mRadius;
        if (arrayOfInt != null && b < arrayOfInt.length) {
          i = arrayOfInt[b];
        } else {
          Integer integer = this.mReferenceDefaultRadius;
          if (integer != null && integer.intValue() != -1) {
            this.mCountRadius++;
            if (this.mRadius == null)
              this.mRadius = new int[1]; 
            int[] arrayOfInt1 = getRadius();
            this.mRadius = arrayOfInt1;
            arrayOfInt1[this.mCountRadius - 1] = i;
          } else {
            String str = String.valueOf(this.mMap.get(Integer.valueOf(view.getId())));
            if (str.length() != 0) {
              str = "Added radius to view with id: ".concat(str);
            } else {
              str = new String("Added radius to view with id: ");
            } 
            Log.e("CircularFlow", str);
          } 
        } 
        float[] arrayOfFloat = this.mAngles;
        if (arrayOfFloat != null && b < arrayOfFloat.length) {
          f = arrayOfFloat[b];
        } else {
          Float float_ = this.mReferenceDefaultAngle;
          if (float_ != null && float_.floatValue() != -1.0F) {
            this.mCountAngle++;
            if (this.mAngles == null)
              this.mAngles = new float[1]; 
            float[] arrayOfFloat1 = getAngles();
            this.mAngles = arrayOfFloat1;
            arrayOfFloat1[this.mCountAngle - 1] = f;
          } else {
            String str = String.valueOf(this.mMap.get(Integer.valueOf(view.getId())));
            if (str.length() != 0) {
              str = "Added angle to view with id: ".concat(str);
            } else {
              str = new String("Added angle to view with id: ");
            } 
            Log.e("CircularFlow", str);
          } 
        } 
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)view.getLayoutParams();
        layoutParams.circleAngle = f;
        layoutParams.circleConstraint = this.mViewCenter;
        layoutParams.circleRadius = i;
        view.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
      } 
    } 
    applyLayoutFeatures();
  }
  
  private float[] removeAngle(float[] paramArrayOffloat, int paramInt) {
    return (paramArrayOffloat == null || paramInt < 0 || paramInt >= this.mCountAngle) ? paramArrayOffloat : removeElementFromArray(paramArrayOffloat, paramInt);
  }
  
  public static float[] removeElementFromArray(float[] paramArrayOffloat, int paramInt) {
    float[] arrayOfFloat = new float[paramArrayOffloat.length - 1];
    byte b1 = 0;
    byte b2 = 0;
    while (b1 < paramArrayOffloat.length) {
      if (b1 != paramInt) {
        arrayOfFloat[b2] = paramArrayOffloat[b1];
        b2++;
      } 
      b1++;
    } 
    return arrayOfFloat;
  }
  
  public static int[] removeElementFromArray(int[] paramArrayOfint, int paramInt) {
    int[] arrayOfInt = new int[paramArrayOfint.length - 1];
    byte b1 = 0;
    byte b2 = 0;
    while (b1 < paramArrayOfint.length) {
      if (b1 != paramInt) {
        arrayOfInt[b2] = paramArrayOfint[b1];
        b2++;
      } 
      b1++;
    } 
    return arrayOfInt;
  }
  
  private int[] removeRadius(int[] paramArrayOfint, int paramInt) {
    return (paramArrayOfint == null || paramInt < 0 || paramInt >= this.mCountRadius) ? paramArrayOfint : removeElementFromArray(paramArrayOfint, paramInt);
  }
  
  private void setAngles(String paramString) {
    if (paramString == null)
      return; 
    int i = 0;
    this.mCountAngle = 0;
    while (true) {
      int j = paramString.indexOf(',', i);
      if (j == -1) {
        addAngle(paramString.substring(i).trim());
        return;
      } 
      addAngle(paramString.substring(i, j).trim());
      i = j + 1;
    } 
  }
  
  private void setRadius(String paramString) {
    if (paramString == null)
      return; 
    int i = 0;
    this.mCountRadius = 0;
    while (true) {
      int j = paramString.indexOf(',', i);
      if (j == -1) {
        addRadius(paramString.substring(i).trim());
        return;
      } 
      addRadius(paramString.substring(i, j).trim());
      i = j + 1;
    } 
  }
  
  public void addViewToCircularFlow(View paramView, int paramInt, float paramFloat) {
    if (containsId(paramView.getId()))
      return; 
    addView(paramView);
    this.mCountAngle++;
    float[] arrayOfFloat = getAngles();
    this.mAngles = arrayOfFloat;
    arrayOfFloat[this.mCountAngle - 1] = paramFloat;
    this.mCountRadius++;
    int[] arrayOfInt = getRadius();
    this.mRadius = arrayOfInt;
    arrayOfInt[this.mCountRadius - 1] = (int)(paramInt * (this.myContext.getResources().getDisplayMetrics()).density);
    anchorReferences();
  }
  
  public float[] getAngles() {
    return Arrays.copyOf(this.mAngles, this.mCountAngle);
  }
  
  public int[] getRadius() {
    return Arrays.copyOf(this.mRadius, this.mCountRadius);
  }
  
  protected void init(AttributeSet paramAttributeSet) {
    super.init(paramAttributeSet);
    if (paramAttributeSet != null) {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.ConstraintLayout_Layout);
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        if (j == R.styleable.ConstraintLayout_Layout_circularflow_viewCenter) {
          this.mViewCenter = typedArray.getResourceId(j, 0);
        } else if (j == R.styleable.ConstraintLayout_Layout_circularflow_angles) {
          String str = typedArray.getString(j);
          this.mReferenceAngles = str;
          setAngles(str);
        } else if (j == R.styleable.ConstraintLayout_Layout_circularflow_radiusInDP) {
          String str = typedArray.getString(j);
          this.mReferenceRadius = str;
          setRadius(str);
        } else if (j == R.styleable.ConstraintLayout_Layout_circularflow_defaultAngle) {
          Float float_ = Float.valueOf(typedArray.getFloat(j, DEFAULT_ANGLE));
          this.mReferenceDefaultAngle = float_;
          setDefaultAngle(float_.floatValue());
        } else if (j == R.styleable.ConstraintLayout_Layout_circularflow_defaultRadius) {
          Integer integer = Integer.valueOf(typedArray.getDimensionPixelSize(j, DEFAULT_RADIUS));
          this.mReferenceDefaultRadius = integer;
          setDefaultRadius(integer.intValue());
        } 
      } 
      typedArray.recycle();
    } 
  }
  
  public boolean isUpdatable(View paramView) {
    boolean bool1 = containsId(paramView.getId());
    boolean bool = false;
    if (!bool1)
      return false; 
    if (indexFromId(paramView.getId()) != -1)
      bool = true; 
    return bool;
  }
  
  public void onAttachedToWindow() {
    super.onAttachedToWindow();
    String str = this.mReferenceAngles;
    if (str != null) {
      this.mAngles = new float[1];
      setAngles(str);
    } 
    str = this.mReferenceRadius;
    if (str != null) {
      this.mRadius = new int[1];
      setRadius(str);
    } 
    Float float_ = this.mReferenceDefaultAngle;
    if (float_ != null)
      setDefaultAngle(float_.floatValue()); 
    Integer integer = this.mReferenceDefaultRadius;
    if (integer != null)
      setDefaultRadius(integer.intValue()); 
    anchorReferences();
  }
  
  public int removeView(View paramView) {
    int i = super.removeView(paramView);
    if (i == -1)
      return i; 
    ConstraintSet constraintSet = new ConstraintSet();
    constraintSet.clone(this.mContainer);
    constraintSet.clear(paramView.getId(), 8);
    constraintSet.applyTo(this.mContainer);
    float[] arrayOfFloat = this.mAngles;
    if (i < arrayOfFloat.length) {
      this.mAngles = removeAngle(arrayOfFloat, i);
      this.mCountAngle--;
    } 
    int[] arrayOfInt = this.mRadius;
    if (i < arrayOfInt.length) {
      this.mRadius = removeRadius(arrayOfInt, i);
      this.mCountRadius--;
    } 
    anchorReferences();
    return i;
  }
  
  public void setDefaultAngle(float paramFloat) {
    DEFAULT_ANGLE = paramFloat;
  }
  
  public void setDefaultRadius(int paramInt) {
    DEFAULT_RADIUS = paramInt;
  }
  
  public void updateAngle(View paramView, float paramFloat) {
    if (!isUpdatable(paramView)) {
      int j = paramView.getId();
      Log.e("CircularFlow", (new StringBuilder(64)).append("It was not possible to update angle to view with id: ").append(j).toString());
      return;
    } 
    int i = indexFromId(paramView.getId());
    if (i > this.mAngles.length)
      return; 
    float[] arrayOfFloat = getAngles();
    this.mAngles = arrayOfFloat;
    arrayOfFloat[i] = paramFloat;
    anchorReferences();
  }
  
  public void updateRadius(View paramView, int paramInt) {
    if (!isUpdatable(paramView)) {
      paramInt = paramView.getId();
      Log.e("CircularFlow", (new StringBuilder(65)).append("It was not possible to update radius to view with id: ").append(paramInt).toString());
      return;
    } 
    int i = indexFromId(paramView.getId());
    if (i > this.mRadius.length)
      return; 
    int[] arrayOfInt = getRadius();
    this.mRadius = arrayOfInt;
    arrayOfInt[i] = (int)(paramInt * (this.myContext.getResources().getDisplayMetrics()).density);
    anchorReferences();
  }
  
  public void updateReference(View paramView, int paramInt, float paramFloat) {
    if (!isUpdatable(paramView)) {
      paramInt = paramView.getId();
      Log.e("CircularFlow", (new StringBuilder(75)).append("It was not possible to update radius and angle to view with id: ").append(paramInt).toString());
      return;
    } 
    int i = indexFromId(paramView.getId());
    if ((getAngles()).length > i) {
      float[] arrayOfFloat = getAngles();
      this.mAngles = arrayOfFloat;
      arrayOfFloat[i] = paramFloat;
    } 
    if ((getRadius()).length > i) {
      int[] arrayOfInt = getRadius();
      this.mRadius = arrayOfInt;
      arrayOfInt[i] = (int)(paramInt * (this.myContext.getResources().getDisplayMetrics()).density);
    } 
    anchorReferences();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\helper\widget\CircularFlow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */