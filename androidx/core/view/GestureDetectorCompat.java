package androidx.core.view;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public final class GestureDetectorCompat {
  private final GestureDetectorCompatImpl mImpl;
  
  public GestureDetectorCompat(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener) {
    this(paramContext, paramOnGestureListener, null);
  }
  
  public GestureDetectorCompat(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener, Handler paramHandler) {
    if (Build.VERSION.SDK_INT > 17) {
      this.mImpl = new GestureDetectorCompatImplJellybeanMr2(paramContext, paramOnGestureListener, paramHandler);
    } else {
      this.mImpl = new GestureDetectorCompatImplBase(paramContext, paramOnGestureListener, paramHandler);
    } 
  }
  
  public boolean isLongpressEnabled() {
    return this.mImpl.isLongpressEnabled();
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    return this.mImpl.onTouchEvent(paramMotionEvent);
  }
  
  public void setIsLongpressEnabled(boolean paramBoolean) {
    this.mImpl.setIsLongpressEnabled(paramBoolean);
  }
  
  public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener paramOnDoubleTapListener) {
    this.mImpl.setOnDoubleTapListener(paramOnDoubleTapListener);
  }
  
  static interface GestureDetectorCompatImpl {
    boolean isLongpressEnabled();
    
    boolean onTouchEvent(MotionEvent param1MotionEvent);
    
    void setIsLongpressEnabled(boolean param1Boolean);
    
    void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener param1OnDoubleTapListener);
  }
  
  static class GestureDetectorCompatImplBase implements GestureDetectorCompatImpl {
    private static final int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
    
    private static final int LONG_PRESS = 2;
    
    private static final int SHOW_PRESS = 1;
    
    private static final int TAP = 3;
    
    private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
    
    private boolean mAlwaysInBiggerTapRegion;
    
    private boolean mAlwaysInTapRegion;
    
    MotionEvent mCurrentDownEvent;
    
    boolean mDeferConfirmSingleTap;
    
    GestureDetector.OnDoubleTapListener mDoubleTapListener;
    
    private int mDoubleTapSlopSquare;
    
    private float mDownFocusX;
    
    private float mDownFocusY;
    
    private final Handler mHandler;
    
    private boolean mInLongPress;
    
    private boolean mIsDoubleTapping;
    
    private boolean mIsLongpressEnabled;
    
    private float mLastFocusX;
    
    private float mLastFocusY;
    
    final GestureDetector.OnGestureListener mListener;
    
    private int mMaximumFlingVelocity;
    
    private int mMinimumFlingVelocity;
    
    private MotionEvent mPreviousUpEvent;
    
    boolean mStillDown;
    
    private int mTouchSlopSquare;
    
    private VelocityTracker mVelocityTracker;
    
    static {
    
    }
    
    GestureDetectorCompatImplBase(Context param1Context, GestureDetector.OnGestureListener param1OnGestureListener, Handler param1Handler) {
      if (param1Handler != null) {
        this.mHandler = new GestureHandler(param1Handler);
      } else {
        this.mHandler = new GestureHandler();
      } 
      this.mListener = param1OnGestureListener;
      if (param1OnGestureListener instanceof GestureDetector.OnDoubleTapListener)
        setOnDoubleTapListener((GestureDetector.OnDoubleTapListener)param1OnGestureListener); 
      init(param1Context);
    }
    
    private void cancel() {
      this.mHandler.removeMessages(1);
      this.mHandler.removeMessages(2);
      this.mHandler.removeMessages(3);
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
      this.mIsDoubleTapping = false;
      this.mStillDown = false;
      this.mAlwaysInTapRegion = false;
      this.mAlwaysInBiggerTapRegion = false;
      this.mDeferConfirmSingleTap = false;
      if (this.mInLongPress)
        this.mInLongPress = false; 
    }
    
    private void cancelTaps() {
      this.mHandler.removeMessages(1);
      this.mHandler.removeMessages(2);
      this.mHandler.removeMessages(3);
      this.mIsDoubleTapping = false;
      this.mAlwaysInTapRegion = false;
      this.mAlwaysInBiggerTapRegion = false;
      this.mDeferConfirmSingleTap = false;
      if (this.mInLongPress)
        this.mInLongPress = false; 
    }
    
    private void init(Context param1Context) {
      if (param1Context != null) {
        if (this.mListener != null) {
          this.mIsLongpressEnabled = true;
          ViewConfiguration viewConfiguration = ViewConfiguration.get(param1Context);
          int i = viewConfiguration.getScaledTouchSlop();
          int j = viewConfiguration.getScaledDoubleTapSlop();
          this.mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
          this.mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
          this.mTouchSlopSquare = i * i;
          this.mDoubleTapSlopSquare = j * j;
          return;
        } 
        throw new IllegalArgumentException("OnGestureListener must not be null");
      } 
      throw new IllegalArgumentException("Context must not be null");
    }
    
    private boolean isConsideredDoubleTap(MotionEvent param1MotionEvent1, MotionEvent param1MotionEvent2, MotionEvent param1MotionEvent3) {
      boolean bool1 = this.mAlwaysInBiggerTapRegion;
      boolean bool = false;
      if (!bool1)
        return false; 
      if (param1MotionEvent3.getEventTime() - param1MotionEvent2.getEventTime() > DOUBLE_TAP_TIMEOUT)
        return false; 
      int j = (int)param1MotionEvent1.getX() - (int)param1MotionEvent3.getX();
      int i = (int)param1MotionEvent1.getY() - (int)param1MotionEvent3.getY();
      if (j * j + i * i < this.mDoubleTapSlopSquare)
        bool = true; 
      return bool;
    }
    
    void dispatchLongPress() {
      this.mHandler.removeMessages(3);
      this.mDeferConfirmSingleTap = false;
      this.mInLongPress = true;
      this.mListener.onLongPress(this.mCurrentDownEvent);
    }
    
    public boolean isLongpressEnabled() {
      return this.mIsLongpressEnabled;
    }
    
    public boolean onTouchEvent(MotionEvent param1MotionEvent) {
      // Byte code:
      //   0: aload_1
      //   1: invokevirtual getAction : ()I
      //   4: istore #9
      //   6: aload_0
      //   7: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   10: ifnonnull -> 20
      //   13: aload_0
      //   14: invokestatic obtain : ()Landroid/view/VelocityTracker;
      //   17: putfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   20: aload_0
      //   21: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   24: aload_1
      //   25: invokevirtual addMovement : (Landroid/view/MotionEvent;)V
      //   28: iload #9
      //   30: sipush #255
      //   33: iand
      //   34: bipush #6
      //   36: if_icmpne -> 45
      //   39: iconst_1
      //   40: istore #6
      //   42: goto -> 48
      //   45: iconst_0
      //   46: istore #6
      //   48: iload #6
      //   50: ifeq -> 62
      //   53: aload_1
      //   54: invokevirtual getActionIndex : ()I
      //   57: istore #7
      //   59: goto -> 65
      //   62: iconst_m1
      //   63: istore #7
      //   65: fconst_0
      //   66: fstore_3
      //   67: fconst_0
      //   68: fstore_2
      //   69: aload_1
      //   70: invokevirtual getPointerCount : ()I
      //   73: istore #10
      //   75: iconst_0
      //   76: istore #8
      //   78: iload #8
      //   80: iload #10
      //   82: if_icmpge -> 119
      //   85: iload #7
      //   87: iload #8
      //   89: if_icmpne -> 95
      //   92: goto -> 113
      //   95: fload_3
      //   96: aload_1
      //   97: iload #8
      //   99: invokevirtual getX : (I)F
      //   102: fadd
      //   103: fstore_3
      //   104: fload_2
      //   105: aload_1
      //   106: iload #8
      //   108: invokevirtual getY : (I)F
      //   111: fadd
      //   112: fstore_2
      //   113: iinc #8, 1
      //   116: goto -> 78
      //   119: iload #6
      //   121: ifeq -> 133
      //   124: iload #10
      //   126: iconst_1
      //   127: isub
      //   128: istore #8
      //   130: goto -> 137
      //   133: iload #10
      //   135: istore #8
      //   137: fload_3
      //   138: iload #8
      //   140: i2f
      //   141: fdiv
      //   142: fstore_3
      //   143: fload_2
      //   144: iload #8
      //   146: i2f
      //   147: fdiv
      //   148: fstore #4
      //   150: iconst_0
      //   151: istore #14
      //   153: iconst_0
      //   154: istore #15
      //   156: iconst_0
      //   157: istore #8
      //   159: iconst_0
      //   160: istore #12
      //   162: iconst_0
      //   163: istore #13
      //   165: iload #9
      //   167: sipush #255
      //   170: iand
      //   171: tableswitch default -> 212, 0 -> 914, 1 -> 635, 2 -> 397, 3 -> 390, 4 -> 212, 5 -> 361, 6 -> 215
      //   212: goto -> 1188
      //   215: aload_0
      //   216: fload_3
      //   217: putfield mLastFocusX : F
      //   220: aload_0
      //   221: fload_3
      //   222: putfield mDownFocusX : F
      //   225: aload_0
      //   226: fload #4
      //   228: putfield mLastFocusY : F
      //   231: aload_0
      //   232: fload #4
      //   234: putfield mDownFocusY : F
      //   237: aload_0
      //   238: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   241: sipush #1000
      //   244: aload_0
      //   245: getfield mMaximumFlingVelocity : I
      //   248: i2f
      //   249: invokevirtual computeCurrentVelocity : (IF)V
      //   252: aload_1
      //   253: invokevirtual getActionIndex : ()I
      //   256: istore #8
      //   258: aload_1
      //   259: iload #8
      //   261: invokevirtual getPointerId : (I)I
      //   264: istore #9
      //   266: aload_0
      //   267: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   270: iload #9
      //   272: invokevirtual getXVelocity : (I)F
      //   275: fstore_2
      //   276: aload_0
      //   277: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   280: iload #9
      //   282: invokevirtual getYVelocity : (I)F
      //   285: fstore_3
      //   286: iconst_0
      //   287: istore #9
      //   289: iload #9
      //   291: iload #10
      //   293: if_icmpge -> 358
      //   296: iload #9
      //   298: iload #8
      //   300: if_icmpne -> 306
      //   303: goto -> 352
      //   306: aload_1
      //   307: iload #9
      //   309: invokevirtual getPointerId : (I)I
      //   312: istore #11
      //   314: aload_0
      //   315: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   318: iload #11
      //   320: invokevirtual getXVelocity : (I)F
      //   323: fload_2
      //   324: fmul
      //   325: aload_0
      //   326: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   329: iload #11
      //   331: invokevirtual getYVelocity : (I)F
      //   334: fload_3
      //   335: fmul
      //   336: fadd
      //   337: fconst_0
      //   338: fcmpg
      //   339: ifge -> 352
      //   342: aload_0
      //   343: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   346: invokevirtual clear : ()V
      //   349: goto -> 358
      //   352: iinc #9, 1
      //   355: goto -> 289
      //   358: goto -> 1188
      //   361: aload_0
      //   362: fload_3
      //   363: putfield mLastFocusX : F
      //   366: aload_0
      //   367: fload_3
      //   368: putfield mDownFocusX : F
      //   371: aload_0
      //   372: fload #4
      //   374: putfield mLastFocusY : F
      //   377: aload_0
      //   378: fload #4
      //   380: putfield mDownFocusY : F
      //   383: aload_0
      //   384: invokespecial cancelTaps : ()V
      //   387: goto -> 1188
      //   390: aload_0
      //   391: invokespecial cancel : ()V
      //   394: goto -> 1188
      //   397: aload_0
      //   398: getfield mInLongPress : Z
      //   401: ifeq -> 407
      //   404: goto -> 1188
      //   407: aload_0
      //   408: getfield mLastFocusX : F
      //   411: fload_3
      //   412: fsub
      //   413: fstore #5
      //   415: aload_0
      //   416: getfield mLastFocusY : F
      //   419: fload #4
      //   421: fsub
      //   422: fstore_2
      //   423: aload_0
      //   424: getfield mIsDoubleTapping : Z
      //   427: ifeq -> 447
      //   430: iconst_0
      //   431: aload_0
      //   432: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   435: aload_1
      //   436: invokeinterface onDoubleTapEvent : (Landroid/view/MotionEvent;)Z
      //   441: ior
      //   442: istore #12
      //   444: goto -> 1188
      //   447: aload_0
      //   448: getfield mAlwaysInTapRegion : Z
      //   451: ifeq -> 579
      //   454: fload_3
      //   455: aload_0
      //   456: getfield mDownFocusX : F
      //   459: fsub
      //   460: f2i
      //   461: istore #6
      //   463: fload #4
      //   465: aload_0
      //   466: getfield mDownFocusY : F
      //   469: fsub
      //   470: f2i
      //   471: istore #7
      //   473: iload #6
      //   475: iload #6
      //   477: imul
      //   478: iload #7
      //   480: iload #7
      //   482: imul
      //   483: iadd
      //   484: istore #6
      //   486: iload #6
      //   488: aload_0
      //   489: getfield mTouchSlopSquare : I
      //   492: if_icmple -> 554
      //   495: aload_0
      //   496: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   499: aload_0
      //   500: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   503: aload_1
      //   504: fload #5
      //   506: fload_2
      //   507: invokeinterface onScroll : (Landroid/view/MotionEvent;Landroid/view/MotionEvent;FF)Z
      //   512: istore #13
      //   514: aload_0
      //   515: fload_3
      //   516: putfield mLastFocusX : F
      //   519: aload_0
      //   520: fload #4
      //   522: putfield mLastFocusY : F
      //   525: aload_0
      //   526: iconst_0
      //   527: putfield mAlwaysInTapRegion : Z
      //   530: aload_0
      //   531: getfield mHandler : Landroid/os/Handler;
      //   534: iconst_3
      //   535: invokevirtual removeMessages : (I)V
      //   538: aload_0
      //   539: getfield mHandler : Landroid/os/Handler;
      //   542: iconst_1
      //   543: invokevirtual removeMessages : (I)V
      //   546: aload_0
      //   547: getfield mHandler : Landroid/os/Handler;
      //   550: iconst_2
      //   551: invokevirtual removeMessages : (I)V
      //   554: iload #13
      //   556: istore #12
      //   558: iload #6
      //   560: aload_0
      //   561: getfield mTouchSlopSquare : I
      //   564: if_icmple -> 576
      //   567: aload_0
      //   568: iconst_0
      //   569: putfield mAlwaysInBiggerTapRegion : Z
      //   572: iload #13
      //   574: istore #12
      //   576: goto -> 1188
      //   579: fload #5
      //   581: invokestatic abs : (F)F
      //   584: fconst_1
      //   585: fcmpl
      //   586: ifge -> 602
      //   589: iload #14
      //   591: istore #12
      //   593: fload_2
      //   594: invokestatic abs : (F)F
      //   597: fconst_1
      //   598: fcmpl
      //   599: iflt -> 576
      //   602: aload_0
      //   603: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   606: aload_0
      //   607: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   610: aload_1
      //   611: fload #5
      //   613: fload_2
      //   614: invokeinterface onScroll : (Landroid/view/MotionEvent;Landroid/view/MotionEvent;FF)Z
      //   619: istore #12
      //   621: aload_0
      //   622: fload_3
      //   623: putfield mLastFocusX : F
      //   626: aload_0
      //   627: fload #4
      //   629: putfield mLastFocusY : F
      //   632: goto -> 1188
      //   635: aload_0
      //   636: iconst_0
      //   637: putfield mStillDown : Z
      //   640: aload_1
      //   641: invokestatic obtain : (Landroid/view/MotionEvent;)Landroid/view/MotionEvent;
      //   644: astore #16
      //   646: aload_0
      //   647: getfield mIsDoubleTapping : Z
      //   650: ifeq -> 670
      //   653: iconst_0
      //   654: aload_0
      //   655: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   658: aload_1
      //   659: invokeinterface onDoubleTapEvent : (Landroid/view/MotionEvent;)Z
      //   664: ior
      //   665: istore #12
      //   667: goto -> 848
      //   670: aload_0
      //   671: getfield mInLongPress : Z
      //   674: ifeq -> 697
      //   677: aload_0
      //   678: getfield mHandler : Landroid/os/Handler;
      //   681: iconst_3
      //   682: invokevirtual removeMessages : (I)V
      //   685: aload_0
      //   686: iconst_0
      //   687: putfield mInLongPress : Z
      //   690: iload #15
      //   692: istore #12
      //   694: goto -> 848
      //   697: aload_0
      //   698: getfield mAlwaysInTapRegion : Z
      //   701: ifeq -> 758
      //   704: aload_0
      //   705: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   708: aload_1
      //   709: invokeinterface onSingleTapUp : (Landroid/view/MotionEvent;)Z
      //   714: istore #13
      //   716: iload #13
      //   718: istore #12
      //   720: aload_0
      //   721: getfield mDeferConfirmSingleTap : Z
      //   724: ifeq -> 848
      //   727: aload_0
      //   728: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   731: astore #17
      //   733: iload #13
      //   735: istore #12
      //   737: aload #17
      //   739: ifnull -> 848
      //   742: aload #17
      //   744: aload_1
      //   745: invokeinterface onSingleTapConfirmed : (Landroid/view/MotionEvent;)Z
      //   750: pop
      //   751: iload #13
      //   753: istore #12
      //   755: goto -> 848
      //   758: aload_0
      //   759: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   762: astore #17
      //   764: aload_1
      //   765: iconst_0
      //   766: invokevirtual getPointerId : (I)I
      //   769: istore #6
      //   771: aload #17
      //   773: sipush #1000
      //   776: aload_0
      //   777: getfield mMaximumFlingVelocity : I
      //   780: i2f
      //   781: invokevirtual computeCurrentVelocity : (IF)V
      //   784: aload #17
      //   786: iload #6
      //   788: invokevirtual getYVelocity : (I)F
      //   791: fstore_2
      //   792: aload #17
      //   794: iload #6
      //   796: invokevirtual getXVelocity : (I)F
      //   799: fstore_3
      //   800: fload_2
      //   801: invokestatic abs : (F)F
      //   804: aload_0
      //   805: getfield mMinimumFlingVelocity : I
      //   808: i2f
      //   809: fcmpl
      //   810: ifgt -> 830
      //   813: iload #15
      //   815: istore #12
      //   817: fload_3
      //   818: invokestatic abs : (F)F
      //   821: aload_0
      //   822: getfield mMinimumFlingVelocity : I
      //   825: i2f
      //   826: fcmpl
      //   827: ifle -> 848
      //   830: aload_0
      //   831: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   834: aload_0
      //   835: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   838: aload_1
      //   839: fload_3
      //   840: fload_2
      //   841: invokeinterface onFling : (Landroid/view/MotionEvent;Landroid/view/MotionEvent;FF)Z
      //   846: istore #12
      //   848: aload_0
      //   849: getfield mPreviousUpEvent : Landroid/view/MotionEvent;
      //   852: astore_1
      //   853: aload_1
      //   854: ifnull -> 861
      //   857: aload_1
      //   858: invokevirtual recycle : ()V
      //   861: aload_0
      //   862: aload #16
      //   864: putfield mPreviousUpEvent : Landroid/view/MotionEvent;
      //   867: aload_0
      //   868: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   871: astore_1
      //   872: aload_1
      //   873: ifnull -> 885
      //   876: aload_1
      //   877: invokevirtual recycle : ()V
      //   880: aload_0
      //   881: aconst_null
      //   882: putfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   885: aload_0
      //   886: iconst_0
      //   887: putfield mIsDoubleTapping : Z
      //   890: aload_0
      //   891: iconst_0
      //   892: putfield mDeferConfirmSingleTap : Z
      //   895: aload_0
      //   896: getfield mHandler : Landroid/os/Handler;
      //   899: iconst_1
      //   900: invokevirtual removeMessages : (I)V
      //   903: aload_0
      //   904: getfield mHandler : Landroid/os/Handler;
      //   907: iconst_2
      //   908: invokevirtual removeMessages : (I)V
      //   911: goto -> 1188
      //   914: iload #8
      //   916: istore #6
      //   918: aload_0
      //   919: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   922: ifnull -> 1040
      //   925: aload_0
      //   926: getfield mHandler : Landroid/os/Handler;
      //   929: iconst_3
      //   930: invokevirtual hasMessages : (I)Z
      //   933: istore #12
      //   935: iload #12
      //   937: ifeq -> 948
      //   940: aload_0
      //   941: getfield mHandler : Landroid/os/Handler;
      //   944: iconst_3
      //   945: invokevirtual removeMessages : (I)V
      //   948: aload_0
      //   949: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   952: astore #17
      //   954: aload #17
      //   956: ifnull -> 1023
      //   959: aload_0
      //   960: getfield mPreviousUpEvent : Landroid/view/MotionEvent;
      //   963: astore #16
      //   965: aload #16
      //   967: ifnull -> 1023
      //   970: iload #12
      //   972: ifeq -> 1023
      //   975: aload_0
      //   976: aload #17
      //   978: aload #16
      //   980: aload_1
      //   981: invokespecial isConsideredDoubleTap : (Landroid/view/MotionEvent;Landroid/view/MotionEvent;Landroid/view/MotionEvent;)Z
      //   984: ifeq -> 1023
      //   987: aload_0
      //   988: iconst_1
      //   989: putfield mIsDoubleTapping : Z
      //   992: aload_0
      //   993: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   996: aload_0
      //   997: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1000: invokeinterface onDoubleTap : (Landroid/view/MotionEvent;)Z
      //   1005: iconst_0
      //   1006: ior
      //   1007: aload_0
      //   1008: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   1011: aload_1
      //   1012: invokeinterface onDoubleTapEvent : (Landroid/view/MotionEvent;)Z
      //   1017: ior
      //   1018: istore #6
      //   1020: goto -> 1040
      //   1023: aload_0
      //   1024: getfield mHandler : Landroid/os/Handler;
      //   1027: iconst_3
      //   1028: getstatic androidx/core/view/GestureDetectorCompat$GestureDetectorCompatImplBase.DOUBLE_TAP_TIMEOUT : I
      //   1031: i2l
      //   1032: invokevirtual sendEmptyMessageDelayed : (IJ)Z
      //   1035: pop
      //   1036: iload #8
      //   1038: istore #6
      //   1040: aload_0
      //   1041: fload_3
      //   1042: putfield mLastFocusX : F
      //   1045: aload_0
      //   1046: fload_3
      //   1047: putfield mDownFocusX : F
      //   1050: aload_0
      //   1051: fload #4
      //   1053: putfield mLastFocusY : F
      //   1056: aload_0
      //   1057: fload #4
      //   1059: putfield mDownFocusY : F
      //   1062: aload_0
      //   1063: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1066: astore #16
      //   1068: aload #16
      //   1070: ifnull -> 1078
      //   1073: aload #16
      //   1075: invokevirtual recycle : ()V
      //   1078: aload_0
      //   1079: aload_1
      //   1080: invokestatic obtain : (Landroid/view/MotionEvent;)Landroid/view/MotionEvent;
      //   1083: putfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1086: aload_0
      //   1087: iconst_1
      //   1088: putfield mAlwaysInTapRegion : Z
      //   1091: aload_0
      //   1092: iconst_1
      //   1093: putfield mAlwaysInBiggerTapRegion : Z
      //   1096: aload_0
      //   1097: iconst_1
      //   1098: putfield mStillDown : Z
      //   1101: aload_0
      //   1102: iconst_0
      //   1103: putfield mInLongPress : Z
      //   1106: aload_0
      //   1107: iconst_0
      //   1108: putfield mDeferConfirmSingleTap : Z
      //   1111: aload_0
      //   1112: getfield mIsLongpressEnabled : Z
      //   1115: ifeq -> 1152
      //   1118: aload_0
      //   1119: getfield mHandler : Landroid/os/Handler;
      //   1122: iconst_2
      //   1123: invokevirtual removeMessages : (I)V
      //   1126: aload_0
      //   1127: getfield mHandler : Landroid/os/Handler;
      //   1130: iconst_2
      //   1131: aload_0
      //   1132: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1135: invokevirtual getDownTime : ()J
      //   1138: getstatic androidx/core/view/GestureDetectorCompat$GestureDetectorCompatImplBase.TAP_TIMEOUT : I
      //   1141: i2l
      //   1142: ladd
      //   1143: invokestatic getLongPressTimeout : ()I
      //   1146: i2l
      //   1147: ladd
      //   1148: invokevirtual sendEmptyMessageAtTime : (IJ)Z
      //   1151: pop
      //   1152: aload_0
      //   1153: getfield mHandler : Landroid/os/Handler;
      //   1156: iconst_1
      //   1157: aload_0
      //   1158: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1161: invokevirtual getDownTime : ()J
      //   1164: getstatic androidx/core/view/GestureDetectorCompat$GestureDetectorCompatImplBase.TAP_TIMEOUT : I
      //   1167: i2l
      //   1168: ladd
      //   1169: invokevirtual sendEmptyMessageAtTime : (IJ)Z
      //   1172: pop
      //   1173: iload #6
      //   1175: aload_0
      //   1176: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   1179: aload_1
      //   1180: invokeinterface onDown : (Landroid/view/MotionEvent;)Z
      //   1185: ior
      //   1186: istore #12
      //   1188: iload #12
      //   1190: ireturn
    }
    
    public void setIsLongpressEnabled(boolean param1Boolean) {
      this.mIsLongpressEnabled = param1Boolean;
    }
    
    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener param1OnDoubleTapListener) {
      this.mDoubleTapListener = param1OnDoubleTapListener;
    }
    
    private class GestureHandler extends Handler {
      final GestureDetectorCompat.GestureDetectorCompatImplBase this$0;
      
      GestureHandler() {}
      
      GestureHandler(Handler param2Handler) {
        super(param2Handler.getLooper());
      }
      
      public void handleMessage(Message param2Message) {
        switch (param2Message.what) {
          default:
            throw new RuntimeException("Unknown message " + param2Message);
          case 3:
            if (GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDoubleTapListener != null)
              if (!GestureDetectorCompat.GestureDetectorCompatImplBase.this.mStillDown) {
                GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDoubleTapListener.onSingleTapConfirmed(GestureDetectorCompat.GestureDetectorCompatImplBase.this.mCurrentDownEvent);
              } else {
                GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDeferConfirmSingleTap = true;
              }  
            return;
          case 2:
            GestureDetectorCompat.GestureDetectorCompatImplBase.this.dispatchLongPress();
            return;
          case 1:
            break;
        } 
        GestureDetectorCompat.GestureDetectorCompatImplBase.this.mListener.onShowPress(GestureDetectorCompat.GestureDetectorCompatImplBase.this.mCurrentDownEvent);
      }
    }
  }
  
  private class GestureHandler extends Handler {
    final GestureDetectorCompat.GestureDetectorCompatImplBase this$0;
    
    GestureHandler() {}
    
    GestureHandler(Handler param1Handler) {
      super(param1Handler.getLooper());
    }
    
    public void handleMessage(Message param1Message) {
      switch (param1Message.what) {
        default:
          throw new RuntimeException("Unknown message " + param1Message);
        case 3:
          if (this.this$0.mDoubleTapListener != null)
            if (!this.this$0.mStillDown) {
              this.this$0.mDoubleTapListener.onSingleTapConfirmed(this.this$0.mCurrentDownEvent);
            } else {
              this.this$0.mDeferConfirmSingleTap = true;
            }  
          return;
        case 2:
          this.this$0.dispatchLongPress();
          return;
        case 1:
          break;
      } 
      this.this$0.mListener.onShowPress(this.this$0.mCurrentDownEvent);
    }
  }
  
  static class GestureDetectorCompatImplJellybeanMr2 implements GestureDetectorCompatImpl {
    private final GestureDetector mDetector;
    
    GestureDetectorCompatImplJellybeanMr2(Context param1Context, GestureDetector.OnGestureListener param1OnGestureListener, Handler param1Handler) {
      this.mDetector = new GestureDetector(param1Context, param1OnGestureListener, param1Handler);
    }
    
    public boolean isLongpressEnabled() {
      return this.mDetector.isLongpressEnabled();
    }
    
    public boolean onTouchEvent(MotionEvent param1MotionEvent) {
      return this.mDetector.onTouchEvent(param1MotionEvent);
    }
    
    public void setIsLongpressEnabled(boolean param1Boolean) {
      this.mDetector.setIsLongpressEnabled(param1Boolean);
    }
    
    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener param1OnDoubleTapListener) {
      this.mDetector.setOnDoubleTapListener(param1OnDoubleTapListener);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\view\GestureDetectorCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */