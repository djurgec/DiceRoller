package androidx.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.xmlpull.v1.XmlPullParser;

public class Slide extends Visibility {
  private static final String PROPNAME_SCREEN_POSITION = "android:slide:screenPosition";
  
  private static final TimeInterpolator sAccelerate;
  
  private static final CalculateSlide sCalculateBottom;
  
  private static final CalculateSlide sCalculateEnd;
  
  private static final CalculateSlide sCalculateLeft;
  
  private static final CalculateSlide sCalculateRight;
  
  private static final CalculateSlide sCalculateStart;
  
  private static final CalculateSlide sCalculateTop;
  
  private static final TimeInterpolator sDecelerate = (TimeInterpolator)new DecelerateInterpolator();
  
  private CalculateSlide mSlideCalculator = sCalculateBottom;
  
  private int mSlideEdge = 80;
  
  static {
    sAccelerate = (TimeInterpolator)new AccelerateInterpolator();
    sCalculateLeft = new CalculateSlideHorizontal() {
        public float getGoneX(ViewGroup param1ViewGroup, View param1View) {
          return param1View.getTranslationX() - param1ViewGroup.getWidth();
        }
      };
    sCalculateStart = new CalculateSlideHorizontal() {
        public float getGoneX(ViewGroup param1ViewGroup, View param1View) {
          float f;
          int i = ViewCompat.getLayoutDirection((View)param1ViewGroup);
          boolean bool = true;
          if (i != 1)
            bool = false; 
          if (bool) {
            f = param1View.getTranslationX() + param1ViewGroup.getWidth();
          } else {
            f = param1View.getTranslationX() - param1ViewGroup.getWidth();
          } 
          return f;
        }
      };
    sCalculateTop = new CalculateSlideVertical() {
        public float getGoneY(ViewGroup param1ViewGroup, View param1View) {
          return param1View.getTranslationY() - param1ViewGroup.getHeight();
        }
      };
    sCalculateRight = new CalculateSlideHorizontal() {
        public float getGoneX(ViewGroup param1ViewGroup, View param1View) {
          return param1View.getTranslationX() + param1ViewGroup.getWidth();
        }
      };
    sCalculateEnd = new CalculateSlideHorizontal() {
        public float getGoneX(ViewGroup param1ViewGroup, View param1View) {
          float f;
          int i = ViewCompat.getLayoutDirection((View)param1ViewGroup);
          boolean bool = true;
          if (i != 1)
            bool = false; 
          if (bool) {
            f = param1View.getTranslationX() - param1ViewGroup.getWidth();
          } else {
            f = param1View.getTranslationX() + param1ViewGroup.getWidth();
          } 
          return f;
        }
      };
    sCalculateBottom = new CalculateSlideVertical() {
        public float getGoneY(ViewGroup param1ViewGroup, View param1View) {
          return param1View.getTranslationY() + param1ViewGroup.getHeight();
        }
      };
  }
  
  public Slide() {
    setSlideEdge(80);
  }
  
  public Slide(int paramInt) {
    setSlideEdge(paramInt);
  }
  
  public Slide(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, Styleable.SLIDE);
    int i = TypedArrayUtils.getNamedInt(typedArray, (XmlPullParser)paramAttributeSet, "slideEdge", 0, 80);
    typedArray.recycle();
    setSlideEdge(i);
  }
  
  private void captureValues(TransitionValues paramTransitionValues) {
    View view = paramTransitionValues.view;
    int[] arrayOfInt = new int[2];
    view.getLocationOnScreen(arrayOfInt);
    paramTransitionValues.values.put("android:slide:screenPosition", arrayOfInt);
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues) {
    super.captureEndValues(paramTransitionValues);
    captureValues(paramTransitionValues);
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues) {
    super.captureStartValues(paramTransitionValues);
    captureValues(paramTransitionValues);
  }
  
  public int getSlideEdge() {
    return this.mSlideEdge;
  }
  
  public Animator onAppear(ViewGroup paramViewGroup, View paramView, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    if (paramTransitionValues2 == null)
      return null; 
    int[] arrayOfInt = (int[])paramTransitionValues2.values.get("android:slide:screenPosition");
    float f3 = paramView.getTranslationX();
    float f1 = paramView.getTranslationY();
    float f4 = this.mSlideCalculator.getGoneX(paramViewGroup, paramView);
    float f2 = this.mSlideCalculator.getGoneY(paramViewGroup, paramView);
    return TranslationAnimationCreator.createAnimation(paramView, paramTransitionValues2, arrayOfInt[0], arrayOfInt[1], f4, f2, f3, f1, sDecelerate, this);
  }
  
  public Animator onDisappear(ViewGroup paramViewGroup, View paramView, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    if (paramTransitionValues1 == null)
      return null; 
    int[] arrayOfInt = (int[])paramTransitionValues1.values.get("android:slide:screenPosition");
    float f3 = paramView.getTranslationX();
    float f1 = paramView.getTranslationY();
    float f4 = this.mSlideCalculator.getGoneX(paramViewGroup, paramView);
    float f2 = this.mSlideCalculator.getGoneY(paramViewGroup, paramView);
    return TranslationAnimationCreator.createAnimation(paramView, paramTransitionValues1, arrayOfInt[0], arrayOfInt[1], f3, f1, f4, f2, sAccelerate, this);
  }
  
  public void setSlideEdge(int paramInt) {
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("Invalid slide direction");
      case 8388613:
        this.mSlideCalculator = sCalculateEnd;
        break;
      case 8388611:
        this.mSlideCalculator = sCalculateStart;
        break;
      case 80:
        this.mSlideCalculator = sCalculateBottom;
        break;
      case 48:
        this.mSlideCalculator = sCalculateTop;
        break;
      case 5:
        this.mSlideCalculator = sCalculateRight;
        break;
      case 3:
        this.mSlideCalculator = sCalculateLeft;
        break;
    } 
    this.mSlideEdge = paramInt;
    SidePropagation sidePropagation = new SidePropagation();
    sidePropagation.setSide(paramInt);
    setPropagation(sidePropagation);
  }
  
  private static interface CalculateSlide {
    float getGoneX(ViewGroup param1ViewGroup, View param1View);
    
    float getGoneY(ViewGroup param1ViewGroup, View param1View);
  }
  
  private static abstract class CalculateSlideHorizontal implements CalculateSlide {
    private CalculateSlideHorizontal() {}
    
    public float getGoneY(ViewGroup param1ViewGroup, View param1View) {
      return param1View.getTranslationY();
    }
  }
  
  private static abstract class CalculateSlideVertical implements CalculateSlide {
    private CalculateSlideVertical() {}
    
    public float getGoneX(ViewGroup param1ViewGroup, View param1View) {
      return param1View.getTranslationX();
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface GravityFlag {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\Slide.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */