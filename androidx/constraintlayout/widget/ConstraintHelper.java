package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.core.widgets.Helper;
import androidx.constraintlayout.core.widgets.HelperWidget;
import java.util.Arrays;
import java.util.HashMap;

public abstract class ConstraintHelper extends View {
  protected int mCount;
  
  protected Helper mHelperWidget;
  
  protected int[] mIds = new int[32];
  
  protected HashMap<Integer, String> mMap = new HashMap<>();
  
  protected String mReferenceIds;
  
  protected String mReferenceTags;
  
  protected boolean mUseViewMeasure = false;
  
  private View[] mViews = null;
  
  protected Context myContext;
  
  public ConstraintHelper(Context paramContext) {
    super(paramContext);
    this.myContext = paramContext;
    init((AttributeSet)null);
  }
  
  public ConstraintHelper(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    this.myContext = paramContext;
    init(paramAttributeSet);
  }
  
  public ConstraintHelper(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    this.myContext = paramContext;
    init(paramAttributeSet);
  }
  
  private void addID(String paramString) {
    if (paramString == null || paramString.length() == 0)
      return; 
    if (this.myContext == null)
      return; 
    String str = paramString.trim();
    if (getParent() instanceof ConstraintLayout)
      ConstraintLayout constraintLayout = (ConstraintLayout)getParent(); 
    int i = findId(str);
    if (i != 0) {
      this.mMap.put(Integer.valueOf(i), str);
      addRscID(i);
    } else {
      Log.w("ConstraintHelper", (new StringBuilder(String.valueOf(str).length() + 23)).append("Could not find id of \"").append(str).append("\"").toString());
    } 
  }
  
  private void addRscID(int paramInt) {
    if (paramInt == getId())
      return; 
    int i = this.mCount;
    int[] arrayOfInt = this.mIds;
    if (i + 1 > arrayOfInt.length)
      this.mIds = Arrays.copyOf(arrayOfInt, arrayOfInt.length * 2); 
    arrayOfInt = this.mIds;
    i = this.mCount;
    arrayOfInt[i] = paramInt;
    this.mCount = i + 1;
  }
  
  private void addTag(String paramString) {
    ConstraintLayout constraintLayout;
    if (paramString == null || paramString.length() == 0)
      return; 
    if (this.myContext == null)
      return; 
    String str = paramString.trim();
    paramString = null;
    if (getParent() instanceof ConstraintLayout)
      constraintLayout = (ConstraintLayout)getParent(); 
    if (constraintLayout == null) {
      Log.w("ConstraintHelper", "Parent not a ConstraintLayout");
      return;
    } 
    int i = constraintLayout.getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = constraintLayout.getChildAt(b);
      ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
      if (layoutParams instanceof ConstraintLayout.LayoutParams && str.equals(((ConstraintLayout.LayoutParams)layoutParams).constraintTag))
        if (view.getId() == -1) {
          String str1 = view.getClass().getSimpleName();
          Log.w("ConstraintHelper", (new StringBuilder(String.valueOf(str1).length() + 42)).append("to use ConstraintTag view ").append(str1).append(" must have an ID").toString());
        } else {
          addRscID(view.getId());
        }  
    } 
  }
  
  private int[] convertReferenceString(View paramView, String paramString) {
    String[] arrayOfString = paramString.split(",");
    paramView.getContext();
    int[] arrayOfInt2 = new int[arrayOfString.length];
    int i = 0;
    byte b = 0;
    while (b < arrayOfString.length) {
      int k = findId(arrayOfString[b].trim());
      int j = i;
      if (k != 0) {
        arrayOfInt2[i] = k;
        j = i + 1;
      } 
      b++;
      i = j;
    } 
    int[] arrayOfInt1 = arrayOfInt2;
    if (i != arrayOfString.length)
      arrayOfInt1 = Arrays.copyOf(arrayOfInt2, i); 
    return arrayOfInt1;
  }
  
  private int findId(ConstraintLayout paramConstraintLayout, String paramString) {
    if (paramString == null || paramConstraintLayout == null)
      return 0; 
    Resources resources = this.myContext.getResources();
    if (resources == null)
      return 0; 
    int i = paramConstraintLayout.getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = paramConstraintLayout.getChildAt(b);
      if (view.getId() != -1) {
        String str = null;
        try {
          String str1 = resources.getResourceEntryName(view.getId());
          str = str1;
        } catch (android.content.res.Resources.NotFoundException notFoundException) {}
        if (paramString.equals(str))
          return view.getId(); 
      } 
    } 
    return 0;
  }
  
  private int findId(String paramString) {
    ConstraintLayout constraintLayout = null;
    if (getParent() instanceof ConstraintLayout)
      constraintLayout = (ConstraintLayout)getParent(); 
    int i = 0;
    int j = i;
    if (isInEditMode()) {
      j = i;
      if (constraintLayout != null) {
        Object object = constraintLayout.getDesignInformation(0, paramString);
        j = i;
        if (object instanceof Integer)
          j = ((Integer)object).intValue(); 
      } 
    } 
    i = j;
    if (j == 0) {
      i = j;
      if (constraintLayout != null)
        i = findId(constraintLayout, paramString); 
    } 
    j = i;
    if (i == 0)
      try {
        j = R.id.class.getField(paramString).getInt(null);
      } catch (Exception exception) {
        j = i;
      }  
    i = j;
    if (j == 0)
      i = this.myContext.getResources().getIdentifier(paramString, "id", this.myContext.getPackageName()); 
    return i;
  }
  
  public void addView(View paramView) {
    if (paramView == this)
      return; 
    if (paramView.getId() == -1) {
      Log.e("ConstraintHelper", "Views added to a ConstraintHelper need to have an id");
      return;
    } 
    if (paramView.getParent() == null) {
      Log.e("ConstraintHelper", "Views added to a ConstraintHelper need to have a parent");
      return;
    } 
    this.mReferenceIds = null;
    addRscID(paramView.getId());
    requestLayout();
  }
  
  protected void applyLayoutFeatures() {
    ViewParent viewParent = getParent();
    if (viewParent != null && viewParent instanceof ConstraintLayout)
      applyLayoutFeatures((ConstraintLayout)viewParent); 
  }
  
  protected void applyLayoutFeatures(ConstraintLayout paramConstraintLayout) {
    int i = getVisibility();
    float f = 0.0F;
    if (Build.VERSION.SDK_INT >= 21)
      f = getElevation(); 
    for (byte b = 0; b < this.mCount; b++) {
      View view = paramConstraintLayout.getViewById(this.mIds[b]);
      if (view != null) {
        view.setVisibility(i);
        if (f > 0.0F && Build.VERSION.SDK_INT >= 21)
          view.setTranslationZ(view.getTranslationZ() + f); 
      } 
    } 
  }
  
  protected void applyLayoutFeaturesInConstraintSet(ConstraintLayout paramConstraintLayout) {}
  
  public boolean containsId(int paramInt) {
    boolean bool1;
    boolean bool2 = false;
    int[] arrayOfInt = this.mIds;
    int i = arrayOfInt.length;
    byte b = 0;
    while (true) {
      bool1 = bool2;
      if (b < i) {
        if (arrayOfInt[b] == paramInt) {
          bool1 = true;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    return bool1;
  }
  
  public int[] getReferencedIds() {
    return Arrays.copyOf(this.mIds, this.mCount);
  }
  
  protected View[] getViews(ConstraintLayout paramConstraintLayout) {
    View[] arrayOfView = this.mViews;
    if (arrayOfView == null || arrayOfView.length != this.mCount)
      this.mViews = new View[this.mCount]; 
    for (byte b = 0; b < this.mCount; b++) {
      int i = this.mIds[b];
      this.mViews[b] = paramConstraintLayout.getViewById(i);
    } 
    return this.mViews;
  }
  
  public int indexFromId(int paramInt) {
    byte b = -1;
    for (int i : this.mIds) {
      b++;
      if (i == paramInt)
        return b; 
    } 
    return b;
  }
  
  protected void init(AttributeSet paramAttributeSet) {
    if (paramAttributeSet != null) {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.ConstraintLayout_Layout);
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        if (j == R.styleable.ConstraintLayout_Layout_constraint_referenced_ids) {
          String str = typedArray.getString(j);
          this.mReferenceIds = str;
          setIds(str);
        } else if (j == R.styleable.ConstraintLayout_Layout_constraint_referenced_tags) {
          String str = typedArray.getString(j);
          this.mReferenceTags = str;
          setReferenceTags(str);
        } 
      } 
      typedArray.recycle();
    } 
  }
  
  public void loadParameters(ConstraintSet.Constraint paramConstraint, HelperWidget paramHelperWidget, ConstraintLayout.LayoutParams paramLayoutParams, SparseArray<ConstraintWidget> paramSparseArray) {
    if (paramConstraint.layout.mReferenceIds != null) {
      setReferencedIds(paramConstraint.layout.mReferenceIds);
    } else if (paramConstraint.layout.mReferenceIdString != null) {
      if (paramConstraint.layout.mReferenceIdString.length() > 0) {
        paramConstraint.layout.mReferenceIds = convertReferenceString(this, paramConstraint.layout.mReferenceIdString);
      } else {
        paramConstraint.layout.mReferenceIds = null;
      } 
    } 
    if (paramHelperWidget != null) {
      paramHelperWidget.removeAllIds();
      if (paramConstraint.layout.mReferenceIds != null)
        for (byte b = 0; b < paramConstraint.layout.mReferenceIds.length; b++) {
          ConstraintWidget constraintWidget = (ConstraintWidget)paramSparseArray.get(paramConstraint.layout.mReferenceIds[b]);
          if (constraintWidget != null)
            paramHelperWidget.add(constraintWidget); 
        }  
    } 
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    String str = this.mReferenceIds;
    if (str != null)
      setIds(str); 
    str = this.mReferenceTags;
    if (str != null)
      setReferenceTags(str); 
  }
  
  public void onDraw(Canvas paramCanvas) {}
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    if (this.mUseViewMeasure) {
      super.onMeasure(paramInt1, paramInt2);
    } else {
      setMeasuredDimension(0, 0);
    } 
  }
  
  public int removeView(View paramView) {
    int i;
    int j = -1;
    int k = paramView.getId();
    if (k == -1)
      return -1; 
    this.mReferenceIds = null;
    byte b = 0;
    while (true) {
      i = j;
      if (b < this.mCount) {
        if (this.mIds[b] == k) {
          i = b;
          while (true) {
            j = this.mCount;
            if (b < j - 1) {
              int[] arrayOfInt = this.mIds;
              arrayOfInt[b] = arrayOfInt[b + 1];
              b++;
              continue;
            } 
            this.mIds[j - 1] = 0;
            this.mCount = j - 1;
            break;
          } 
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    requestLayout();
    return i;
  }
  
  public void resolveRtl(ConstraintWidget paramConstraintWidget, boolean paramBoolean) {}
  
  protected void setIds(String paramString) {
    this.mReferenceIds = paramString;
    if (paramString == null)
      return; 
    int i = 0;
    this.mCount = 0;
    while (true) {
      int j = paramString.indexOf(',', i);
      if (j == -1) {
        addID(paramString.substring(i));
        return;
      } 
      addID(paramString.substring(i, j));
      i = j + 1;
    } 
  }
  
  protected void setReferenceTags(String paramString) {
    this.mReferenceTags = paramString;
    if (paramString == null)
      return; 
    int i = 0;
    this.mCount = 0;
    while (true) {
      int j = paramString.indexOf(',', i);
      if (j == -1) {
        addTag(paramString.substring(i));
        return;
      } 
      addTag(paramString.substring(i, j));
      i = j + 1;
    } 
  }
  
  public void setReferencedIds(int[] paramArrayOfint) {
    this.mReferenceIds = null;
    this.mCount = 0;
    for (byte b = 0; b < paramArrayOfint.length; b++)
      addRscID(paramArrayOfint[b]); 
  }
  
  public void setTag(int paramInt, Object paramObject) {
    super.setTag(paramInt, paramObject);
    if (paramObject == null && this.mReferenceIds == null)
      addRscID(paramInt); 
  }
  
  public void updatePostConstraints(ConstraintLayout paramConstraintLayout) {}
  
  public void updatePostLayout(ConstraintLayout paramConstraintLayout) {}
  
  public void updatePostMeasure(ConstraintLayout paramConstraintLayout) {}
  
  public void updatePreDraw(ConstraintLayout paramConstraintLayout) {}
  
  public void updatePreLayout(ConstraintWidgetContainer paramConstraintWidgetContainer, Helper paramHelper, SparseArray<ConstraintWidget> paramSparseArray) {
    paramHelper.removeAllIds();
    for (byte b = 0; b < this.mCount; b++)
      paramHelper.add((ConstraintWidget)paramSparseArray.get(this.mIds[b])); 
  }
  
  public void updatePreLayout(ConstraintLayout paramConstraintLayout) {
    if (isInEditMode())
      setIds(this.mReferenceIds); 
    Helper helper = this.mHelperWidget;
    if (helper == null)
      return; 
    helper.removeAllIds();
    for (byte b = 0; b < this.mCount; b++) {
      int i = this.mIds[b];
      View view2 = paramConstraintLayout.getViewById(i);
      View view1 = view2;
      if (view2 == null) {
        String str = this.mMap.get(Integer.valueOf(i));
        i = findId(paramConstraintLayout, str);
        view1 = view2;
        if (i != 0) {
          this.mIds[b] = i;
          this.mMap.put(Integer.valueOf(i), str);
          view1 = paramConstraintLayout.getViewById(i);
        } 
      } 
      if (view1 != null)
        this.mHelperWidget.add(paramConstraintLayout.getViewWidget(view1)); 
    } 
    this.mHelperWidget.updateConstraints(paramConstraintLayout.mLayoutWidget);
  }
  
  public void validateParams() {
    if (this.mHelperWidget == null)
      return; 
    ViewGroup.LayoutParams layoutParams = getLayoutParams();
    if (layoutParams instanceof ConstraintLayout.LayoutParams)
      ((ConstraintLayout.LayoutParams)layoutParams).widget = (ConstraintWidget)this.mHelperWidget; 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\widget\ConstraintHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */