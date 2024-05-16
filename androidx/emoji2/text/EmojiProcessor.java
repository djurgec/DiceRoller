package androidx.emoji2.text;

import android.graphics.Paint;
import android.os.Build;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.MetaKeyKeyListener;
import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;
import androidx.core.graphics.PaintCompat;
import java.util.Arrays;

final class EmojiProcessor {
  private static final int ACTION_ADVANCE_BOTH = 1;
  
  private static final int ACTION_ADVANCE_END = 2;
  
  private static final int ACTION_FLUSH = 3;
  
  private final int[] mEmojiAsDefaultStyleExceptions;
  
  private EmojiCompat.GlyphChecker mGlyphChecker;
  
  private final MetadataRepo mMetadataRepo;
  
  private final EmojiCompat.SpanFactory mSpanFactory;
  
  private final boolean mUseEmojiAsDefaultStyle;
  
  EmojiProcessor(MetadataRepo paramMetadataRepo, EmojiCompat.SpanFactory paramSpanFactory, EmojiCompat.GlyphChecker paramGlyphChecker, boolean paramBoolean, int[] paramArrayOfint) {
    this.mSpanFactory = paramSpanFactory;
    this.mMetadataRepo = paramMetadataRepo;
    this.mGlyphChecker = paramGlyphChecker;
    this.mUseEmojiAsDefaultStyle = paramBoolean;
    this.mEmojiAsDefaultStyleExceptions = paramArrayOfint;
  }
  
  private void addEmoji(Spannable paramSpannable, EmojiMetadata paramEmojiMetadata, int paramInt1, int paramInt2) {
    paramSpannable.setSpan(this.mSpanFactory.createSpan(paramEmojiMetadata), paramInt1, paramInt2, 33);
  }
  
  private static boolean delete(Editable paramEditable, KeyEvent paramKeyEvent, boolean paramBoolean) {
    if (hasModifiers(paramKeyEvent))
      return false; 
    int j = Selection.getSelectionStart((CharSequence)paramEditable);
    int i = Selection.getSelectionEnd((CharSequence)paramEditable);
    if (hasInvalidSelection(j, i))
      return false; 
    EmojiSpan[] arrayOfEmojiSpan = (EmojiSpan[])paramEditable.getSpans(j, i, EmojiSpan.class);
    if (arrayOfEmojiSpan != null && arrayOfEmojiSpan.length > 0) {
      int k = arrayOfEmojiSpan.length;
      for (i = 0; i < k; i++) {
        EmojiSpan emojiSpan = arrayOfEmojiSpan[i];
        int n = paramEditable.getSpanStart(emojiSpan);
        int m = paramEditable.getSpanEnd(emojiSpan);
        if ((paramBoolean && n == j) || (!paramBoolean && m == j) || (j > n && j < m)) {
          paramEditable.delete(n, m);
          return true;
        } 
      } 
    } 
    return false;
  }
  
  static boolean handleDeleteSurroundingText(InputConnection paramInputConnection, Editable paramEditable, int paramInt1, int paramInt2, boolean paramBoolean) {
    if (paramEditable == null || paramInputConnection == null)
      return false; 
    if (paramInt1 < 0 || paramInt2 < 0)
      return false; 
    int i = Selection.getSelectionStart((CharSequence)paramEditable);
    int j = Selection.getSelectionEnd((CharSequence)paramEditable);
    if (hasInvalidSelection(i, j))
      return false; 
    if (paramBoolean) {
      paramInt1 = CodepointIndexFinder.findIndexBackward((CharSequence)paramEditable, i, Math.max(paramInt1, 0));
      i = CodepointIndexFinder.findIndexForward((CharSequence)paramEditable, j, Math.max(paramInt2, 0));
      if (paramInt1 != -1) {
        paramInt2 = paramInt1;
        paramInt1 = i;
        if (i == -1)
          return false; 
      } else {
        return false;
      } 
    } else {
      i = Math.max(i - paramInt1, 0);
      paramInt1 = Math.min(j + paramInt2, paramEditable.length());
      paramInt2 = i;
    } 
    EmojiSpan[] arrayOfEmojiSpan = (EmojiSpan[])paramEditable.getSpans(paramInt2, paramInt1, EmojiSpan.class);
    if (arrayOfEmojiSpan != null && arrayOfEmojiSpan.length > 0) {
      j = arrayOfEmojiSpan.length;
      for (i = 0; i < j; i++) {
        EmojiSpan emojiSpan = arrayOfEmojiSpan[i];
        int m = paramEditable.getSpanStart(emojiSpan);
        int k = paramEditable.getSpanEnd(emojiSpan);
        paramInt2 = Math.min(m, paramInt2);
        paramInt1 = Math.max(k, paramInt1);
      } 
      paramInt2 = Math.max(paramInt2, 0);
      paramInt1 = Math.min(paramInt1, paramEditable.length());
      paramInputConnection.beginBatchEdit();
      paramEditable.delete(paramInt2, paramInt1);
      paramInputConnection.endBatchEdit();
      return true;
    } 
    return false;
  }
  
  static boolean handleOnKeyDown(Editable paramEditable, int paramInt, KeyEvent paramKeyEvent) {
    boolean bool;
    switch (paramInt) {
      default:
        bool = false;
        break;
      case 112:
        bool = delete(paramEditable, paramKeyEvent, true);
        break;
      case 67:
        bool = delete(paramEditable, paramKeyEvent, false);
        break;
    } 
    if (bool) {
      MetaKeyKeyListener.adjustMetaAfterKeypress((Spannable)paramEditable);
      return true;
    } 
    return false;
  }
  
  private boolean hasGlyph(CharSequence paramCharSequence, int paramInt1, int paramInt2, EmojiMetadata paramEmojiMetadata) {
    boolean bool;
    if (paramEmojiMetadata.getHasGlyph() == 0)
      paramEmojiMetadata.setHasGlyph(this.mGlyphChecker.hasGlyph(paramCharSequence, paramInt1, paramInt2, paramEmojiMetadata.getSdkAdded())); 
    if (paramEmojiMetadata.getHasGlyph() == 2) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static boolean hasInvalidSelection(int paramInt1, int paramInt2) {
    return (paramInt1 == -1 || paramInt2 == -1 || paramInt1 != paramInt2);
  }
  
  private static boolean hasModifiers(KeyEvent paramKeyEvent) {
    return KeyEvent.metaStateHasNoModifiers(paramKeyEvent.getMetaState()) ^ true;
  }
  
  EmojiMetadata getEmojiMetadata(CharSequence paramCharSequence) {
    ProcessorSm processorSm = new ProcessorSm(this.mMetadataRepo.getRootNode(), this.mUseEmojiAsDefaultStyle, this.mEmojiAsDefaultStyleExceptions);
    int j = paramCharSequence.length();
    for (int i = 0; i < j; i += Character.charCount(k)) {
      int k = Character.codePointAt(paramCharSequence, i);
      if (processorSm.check(k) != 2)
        return null; 
    } 
    return processorSm.isInFlushableState() ? processorSm.getCurrentMetadata() : null;
  }
  
  CharSequence process(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
    // Byte code:
    //   0: aload_1
    //   1: instanceof androidx/emoji2/text/SpannableBuilder
    //   4: istore #12
    //   6: iload #12
    //   8: ifeq -> 18
    //   11: aload_1
    //   12: checkcast androidx/emoji2/text/SpannableBuilder
    //   15: invokevirtual beginBatchEdit : ()V
    //   18: aconst_null
    //   19: astore #14
    //   21: iload #12
    //   23: ifne -> 85
    //   26: aload_1
    //   27: instanceof android/text/Spannable
    //   30: ifeq -> 36
    //   33: goto -> 85
    //   36: aload #14
    //   38: astore #13
    //   40: aload_1
    //   41: instanceof android/text/Spanned
    //   44: ifeq -> 91
    //   47: aload #14
    //   49: astore #13
    //   51: aload_1
    //   52: checkcast android/text/Spanned
    //   55: iload_2
    //   56: iconst_1
    //   57: isub
    //   58: iload_3
    //   59: iconst_1
    //   60: iadd
    //   61: ldc androidx/emoji2/text/EmojiSpan
    //   63: invokeinterface nextSpanTransition : (IILjava/lang/Class;)I
    //   68: iload_3
    //   69: if_icmpgt -> 91
    //   72: new android/text/SpannableString
    //   75: dup
    //   76: aload_1
    //   77: invokespecial <init> : (Ljava/lang/CharSequence;)V
    //   80: astore #13
    //   82: goto -> 91
    //   85: aload_1
    //   86: checkcast android/text/Spannable
    //   89: astore #13
    //   91: iload_2
    //   92: istore #6
    //   94: iload_3
    //   95: istore #7
    //   97: aload #13
    //   99: ifnull -> 226
    //   102: aload #13
    //   104: iload_2
    //   105: iload_3
    //   106: ldc androidx/emoji2/text/EmojiSpan
    //   108: invokeinterface getSpans : (IILjava/lang/Class;)[Ljava/lang/Object;
    //   113: checkcast [Landroidx/emoji2/text/EmojiSpan;
    //   116: astore #14
    //   118: iload_2
    //   119: istore #6
    //   121: iload_3
    //   122: istore #7
    //   124: aload #14
    //   126: ifnull -> 226
    //   129: iload_2
    //   130: istore #6
    //   132: iload_3
    //   133: istore #7
    //   135: aload #14
    //   137: arraylength
    //   138: ifle -> 226
    //   141: aload #14
    //   143: arraylength
    //   144: istore #9
    //   146: iconst_0
    //   147: istore #8
    //   149: iload_2
    //   150: istore #6
    //   152: iload_3
    //   153: istore #7
    //   155: iload #8
    //   157: iload #9
    //   159: if_icmpge -> 226
    //   162: aload #14
    //   164: iload #8
    //   166: aaload
    //   167: astore #15
    //   169: aload #13
    //   171: aload #15
    //   173: invokeinterface getSpanStart : (Ljava/lang/Object;)I
    //   178: istore #7
    //   180: aload #13
    //   182: aload #15
    //   184: invokeinterface getSpanEnd : (Ljava/lang/Object;)I
    //   189: istore #6
    //   191: iload #7
    //   193: iload_3
    //   194: if_icmpeq -> 206
    //   197: aload #13
    //   199: aload #15
    //   201: invokeinterface removeSpan : (Ljava/lang/Object;)V
    //   206: iload #7
    //   208: iload_2
    //   209: invokestatic min : (II)I
    //   212: istore_2
    //   213: iload #6
    //   215: iload_3
    //   216: invokestatic max : (II)I
    //   219: istore_3
    //   220: iinc #8, 1
    //   223: goto -> 149
    //   226: iload #6
    //   228: iload #7
    //   230: if_icmpeq -> 718
    //   233: iload #6
    //   235: aload_1
    //   236: invokeinterface length : ()I
    //   241: if_icmplt -> 247
    //   244: goto -> 718
    //   247: iload #4
    //   249: istore #8
    //   251: iload #4
    //   253: ldc 2147483647
    //   255: if_icmpeq -> 293
    //   258: iload #4
    //   260: istore #8
    //   262: aload #13
    //   264: ifnull -> 293
    //   267: iload #4
    //   269: aload #13
    //   271: iconst_0
    //   272: aload #13
    //   274: invokeinterface length : ()I
    //   279: ldc androidx/emoji2/text/EmojiSpan
    //   281: invokeinterface getSpans : (IILjava/lang/Class;)[Ljava/lang/Object;
    //   286: checkcast [Landroidx/emoji2/text/EmojiSpan;
    //   289: arraylength
    //   290: isub
    //   291: istore #8
    //   293: iconst_0
    //   294: istore_2
    //   295: new androidx/emoji2/text/EmojiProcessor$ProcessorSm
    //   298: astore #15
    //   300: aload #15
    //   302: aload_0
    //   303: getfield mMetadataRepo : Landroidx/emoji2/text/MetadataRepo;
    //   306: invokevirtual getRootNode : ()Landroidx/emoji2/text/MetadataRepo$Node;
    //   309: aload_0
    //   310: getfield mUseEmojiAsDefaultStyle : Z
    //   313: aload_0
    //   314: getfield mEmojiAsDefaultStyleExceptions : [I
    //   317: invokespecial <init> : (Landroidx/emoji2/text/MetadataRepo$Node;Z[I)V
    //   320: iload #6
    //   322: istore_3
    //   323: aload_1
    //   324: iload_3
    //   325: invokestatic codePointAt : (Ljava/lang/CharSequence;I)I
    //   328: istore #4
    //   330: iload #6
    //   332: istore #9
    //   334: iload #4
    //   336: istore #6
    //   338: iload_3
    //   339: iload #7
    //   341: if_icmpge -> 607
    //   344: iload_2
    //   345: iload #8
    //   347: if_icmpge -> 607
    //   350: aload #15
    //   352: iload #6
    //   354: invokevirtual check : (I)I
    //   357: tableswitch default -> 384, 1 -> 532, 2 -> 477, 3 -> 398
    //   384: aload #13
    //   386: astore #14
    //   388: iload_2
    //   389: istore #10
    //   391: iload #9
    //   393: istore #4
    //   395: goto -> 593
    //   398: iload #5
    //   400: ifne -> 426
    //   403: aload #13
    //   405: astore #14
    //   407: iload_2
    //   408: istore #4
    //   410: aload_0
    //   411: aload_1
    //   412: iload #9
    //   414: iload_3
    //   415: aload #15
    //   417: invokevirtual getFlushMetadata : ()Landroidx/emoji2/text/EmojiMetadata;
    //   420: invokespecial hasGlyph : (Ljava/lang/CharSequence;IILandroidx/emoji2/text/EmojiMetadata;)Z
    //   423: ifne -> 465
    //   426: aload #13
    //   428: astore #14
    //   430: aload #13
    //   432: ifnonnull -> 446
    //   435: new android/text/SpannableString
    //   438: astore #14
    //   440: aload #14
    //   442: aload_1
    //   443: invokespecial <init> : (Ljava/lang/CharSequence;)V
    //   446: aload_0
    //   447: aload #14
    //   449: aload #15
    //   451: invokevirtual getFlushMetadata : ()Landroidx/emoji2/text/EmojiMetadata;
    //   454: iload #9
    //   456: iload_3
    //   457: invokespecial addEmoji : (Landroid/text/Spannable;Landroidx/emoji2/text/EmojiMetadata;II)V
    //   460: iload_2
    //   461: iconst_1
    //   462: iadd
    //   463: istore #4
    //   465: iload_3
    //   466: istore_2
    //   467: iload #4
    //   469: istore #10
    //   471: iload_2
    //   472: istore #4
    //   474: goto -> 593
    //   477: iload_3
    //   478: iload #6
    //   480: invokestatic charCount : (I)I
    //   483: iadd
    //   484: istore #11
    //   486: aload #13
    //   488: astore #14
    //   490: iload_2
    //   491: istore #10
    //   493: iload #11
    //   495: istore_3
    //   496: iload #9
    //   498: istore #4
    //   500: iload #11
    //   502: iload #7
    //   504: if_icmpge -> 593
    //   507: aload_1
    //   508: iload #11
    //   510: invokestatic codePointAt : (Ljava/lang/CharSequence;I)I
    //   513: istore #6
    //   515: aload #13
    //   517: astore #14
    //   519: iload_2
    //   520: istore #10
    //   522: iload #11
    //   524: istore_3
    //   525: iload #9
    //   527: istore #4
    //   529: goto -> 593
    //   532: iload #9
    //   534: aload_1
    //   535: iload #9
    //   537: invokestatic codePointAt : (Ljava/lang/CharSequence;I)I
    //   540: invokestatic charCount : (I)I
    //   543: iadd
    //   544: istore #9
    //   546: iload #9
    //   548: istore #11
    //   550: aload #13
    //   552: astore #14
    //   554: iload_2
    //   555: istore #10
    //   557: iload #11
    //   559: istore_3
    //   560: iload #9
    //   562: istore #4
    //   564: iload #11
    //   566: iload #7
    //   568: if_icmpge -> 593
    //   571: aload_1
    //   572: iload #11
    //   574: invokestatic codePointAt : (Ljava/lang/CharSequence;I)I
    //   577: istore #6
    //   579: iload #9
    //   581: istore #4
    //   583: iload #11
    //   585: istore_3
    //   586: iload_2
    //   587: istore #10
    //   589: aload #13
    //   591: astore #14
    //   593: aload #14
    //   595: astore #13
    //   597: iload #10
    //   599: istore_2
    //   600: iload #4
    //   602: istore #9
    //   604: goto -> 338
    //   607: aload #13
    //   609: astore #14
    //   611: aload #15
    //   613: invokevirtual isInFlushableState : ()Z
    //   616: ifeq -> 688
    //   619: aload #13
    //   621: astore #14
    //   623: iload_2
    //   624: iload #8
    //   626: if_icmpge -> 688
    //   629: iload #5
    //   631: ifne -> 654
    //   634: aload #13
    //   636: astore #14
    //   638: aload_0
    //   639: aload_1
    //   640: iload #9
    //   642: iload_3
    //   643: aload #15
    //   645: invokevirtual getCurrentMetadata : ()Landroidx/emoji2/text/EmojiMetadata;
    //   648: invokespecial hasGlyph : (Ljava/lang/CharSequence;IILandroidx/emoji2/text/EmojiMetadata;)Z
    //   651: ifne -> 688
    //   654: aload #13
    //   656: astore #14
    //   658: aload #13
    //   660: ifnonnull -> 674
    //   663: new android/text/SpannableString
    //   666: astore #14
    //   668: aload #14
    //   670: aload_1
    //   671: invokespecial <init> : (Ljava/lang/CharSequence;)V
    //   674: aload_0
    //   675: aload #14
    //   677: aload #15
    //   679: invokevirtual getCurrentMetadata : ()Landroidx/emoji2/text/EmojiMetadata;
    //   682: iload #9
    //   684: iload_3
    //   685: invokespecial addEmoji : (Landroid/text/Spannable;Landroidx/emoji2/text/EmojiMetadata;II)V
    //   688: aload #14
    //   690: ifnonnull -> 699
    //   693: aload_1
    //   694: astore #13
    //   696: goto -> 703
    //   699: aload #14
    //   701: astore #13
    //   703: iload #12
    //   705: ifeq -> 715
    //   708: aload_1
    //   709: checkcast androidx/emoji2/text/SpannableBuilder
    //   712: invokevirtual endBatchEdit : ()V
    //   715: aload #13
    //   717: areturn
    //   718: iload #12
    //   720: ifeq -> 730
    //   723: aload_1
    //   724: checkcast androidx/emoji2/text/SpannableBuilder
    //   727: invokevirtual endBatchEdit : ()V
    //   730: aload_1
    //   731: areturn
    //   732: astore #13
    //   734: iload #12
    //   736: ifeq -> 746
    //   739: aload_1
    //   740: checkcast androidx/emoji2/text/SpannableBuilder
    //   743: invokevirtual endBatchEdit : ()V
    //   746: aload #13
    //   748: athrow
    // Exception table:
    //   from	to	target	type
    //   26	33	732	finally
    //   40	47	732	finally
    //   51	82	732	finally
    //   85	91	732	finally
    //   102	118	732	finally
    //   135	146	732	finally
    //   169	191	732	finally
    //   197	206	732	finally
    //   206	220	732	finally
    //   233	244	732	finally
    //   267	293	732	finally
    //   295	320	732	finally
    //   323	330	732	finally
    //   350	384	732	finally
    //   410	426	732	finally
    //   435	446	732	finally
    //   446	460	732	finally
    //   477	486	732	finally
    //   507	515	732	finally
    //   532	546	732	finally
    //   571	579	732	finally
    //   611	619	732	finally
    //   638	654	732	finally
    //   663	674	732	finally
    //   674	688	732	finally
  }
  
  private static final class CodepointIndexFinder {
    private static final int INVALID_INDEX = -1;
    
    static int findIndexBackward(CharSequence param1CharSequence, int param1Int1, int param1Int2) {
      int i = param1Int1;
      boolean bool = false;
      param1Int1 = param1CharSequence.length();
      if (i < 0 || param1Int1 < i)
        return -1; 
      if (param1Int2 < 0)
        return -1; 
      param1Int1 = param1Int2;
      for (param1Int2 = bool;; param1Int2 = 1) {
        if (param1Int1 == 0)
          return i; 
        if (--i < 0)
          return (param1Int2 != 0) ? -1 : 0; 
        char c = param1CharSequence.charAt(i);
        if (param1Int2 != 0) {
          if (!Character.isHighSurrogate(c))
            return -1; 
          param1Int2 = 0;
          param1Int1--;
          continue;
        } 
        if (!Character.isSurrogate(c)) {
          param1Int1--;
          continue;
        } 
        if (Character.isHighSurrogate(c))
          return -1; 
      } 
    }
    
    static int findIndexForward(CharSequence param1CharSequence, int param1Int1, int param1Int2) {
      boolean bool = false;
      int i = param1CharSequence.length();
      if (param1Int1 < 0 || i < param1Int1)
        return -1; 
      if (param1Int2 < 0)
        return -1; 
      while (true) {
        if (param1Int2 == 0)
          return param1Int1; 
        if (param1Int1 >= i)
          return bool ? -1 : i; 
        char c = param1CharSequence.charAt(param1Int1);
        if (bool) {
          if (!Character.isLowSurrogate(c))
            return -1; 
          param1Int2--;
          bool = false;
          param1Int1++;
          continue;
        } 
        if (!Character.isSurrogate(c)) {
          param1Int2--;
          param1Int1++;
          continue;
        } 
        if (Character.isLowSurrogate(c))
          return -1; 
        bool = true;
        param1Int1++;
      } 
    }
  }
  
  public static class DefaultGlyphChecker implements EmojiCompat.GlyphChecker {
    private static final int PAINT_TEXT_SIZE = 10;
    
    private static final ThreadLocal<StringBuilder> sStringBuilder = new ThreadLocal<>();
    
    private final TextPaint mTextPaint;
    
    DefaultGlyphChecker() {
      TextPaint textPaint = new TextPaint();
      this.mTextPaint = textPaint;
      textPaint.setTextSize(10.0F);
    }
    
    private static StringBuilder getStringBuilder() {
      ThreadLocal<StringBuilder> threadLocal = sStringBuilder;
      if (threadLocal.get() == null)
        threadLocal.set(new StringBuilder()); 
      return threadLocal.get();
    }
    
    public boolean hasGlyph(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {
      if (Build.VERSION.SDK_INT < 23 && param1Int3 > Build.VERSION.SDK_INT)
        return false; 
      StringBuilder stringBuilder = getStringBuilder();
      stringBuilder.setLength(0);
      while (param1Int1 < param1Int2) {
        stringBuilder.append(param1CharSequence.charAt(param1Int1));
        param1Int1++;
      } 
      return PaintCompat.hasGlyph((Paint)this.mTextPaint, stringBuilder.toString());
    }
  }
  
  static final class ProcessorSm {
    private static final int STATE_DEFAULT = 1;
    
    private static final int STATE_WALKING = 2;
    
    private int mCurrentDepth;
    
    private MetadataRepo.Node mCurrentNode;
    
    private final int[] mEmojiAsDefaultStyleExceptions;
    
    private MetadataRepo.Node mFlushNode;
    
    private int mLastCodepoint;
    
    private final MetadataRepo.Node mRootNode;
    
    private int mState = 1;
    
    private final boolean mUseEmojiAsDefaultStyle;
    
    ProcessorSm(MetadataRepo.Node param1Node, boolean param1Boolean, int[] param1ArrayOfint) {
      this.mRootNode = param1Node;
      this.mCurrentNode = param1Node;
      this.mUseEmojiAsDefaultStyle = param1Boolean;
      this.mEmojiAsDefaultStyleExceptions = param1ArrayOfint;
    }
    
    private static boolean isEmojiStyle(int param1Int) {
      boolean bool;
      if (param1Int == 65039) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    private static boolean isTextStyle(int param1Int) {
      boolean bool;
      if (param1Int == 65038) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    private int reset() {
      this.mState = 1;
      this.mCurrentNode = this.mRootNode;
      this.mCurrentDepth = 0;
      return 1;
    }
    
    private boolean shouldUseEmojiPresentationStyleForSingleCodepoint() {
      if (this.mCurrentNode.getData().isDefaultEmoji())
        return true; 
      if (isEmojiStyle(this.mLastCodepoint))
        return true; 
      if (this.mUseEmojiAsDefaultStyle) {
        if (this.mEmojiAsDefaultStyleExceptions == null)
          return true; 
        int i = this.mCurrentNode.getData().getCodepointAt(0);
        if (Arrays.binarySearch(this.mEmojiAsDefaultStyleExceptions, i) < 0)
          return true; 
      } 
      return false;
    }
    
    int check(int param1Int) {
      MetadataRepo.Node node = this.mCurrentNode.get(param1Int);
      switch (this.mState) {
        default:
          if (node == null) {
            i = reset();
            this.mLastCodepoint = param1Int;
            return i;
          } 
          break;
        case 2:
          if (node != null) {
            this.mCurrentNode = node;
            this.mCurrentDepth++;
            i = 2;
          } else if (isTextStyle(param1Int)) {
            i = reset();
          } else if (isEmojiStyle(param1Int)) {
            i = 2;
          } else if (this.mCurrentNode.getData() != null) {
            if (this.mCurrentDepth == 1) {
              if (shouldUseEmojiPresentationStyleForSingleCodepoint()) {
                this.mFlushNode = this.mCurrentNode;
                i = 3;
                reset();
              } else {
                i = reset();
              } 
            } else {
              this.mFlushNode = this.mCurrentNode;
              i = 3;
              reset();
            } 
          } else {
            i = reset();
          } 
          this.mLastCodepoint = param1Int;
          return i;
      } 
      this.mState = 2;
      this.mCurrentNode = node;
      this.mCurrentDepth = 1;
      int i = 2;
      this.mLastCodepoint = param1Int;
      return i;
    }
    
    EmojiMetadata getCurrentMetadata() {
      return this.mCurrentNode.getData();
    }
    
    EmojiMetadata getFlushMetadata() {
      return this.mFlushNode.getData();
    }
    
    boolean isInFlushableState() {
      int i = this.mState;
      boolean bool = true;
      if (i != 2 || this.mCurrentNode.getData() == null || (this.mCurrentDepth <= 1 && !shouldUseEmojiPresentationStyleForSingleCodepoint()))
        bool = false; 
      return bool;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\EmojiProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */