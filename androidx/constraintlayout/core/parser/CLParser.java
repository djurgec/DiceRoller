package androidx.constraintlayout.core.parser;

public class CLParser {
  static boolean DEBUG = false;
  
  private boolean hasComment = false;
  
  private int lineNumber;
  
  private String mContent;
  
  public CLParser(String paramString) {
    this.mContent = paramString;
  }
  
  private CLElement createElement(CLElement paramCLElement, int paramInt, TYPE paramTYPE, boolean paramBoolean, char[] paramArrayOfchar) {
    CLElement cLElement;
    TYPE tYPE = null;
    if (DEBUG)
      System.out.println("CREATE " + paramTYPE + " at " + paramArrayOfchar[paramInt]); 
    switch (paramTYPE) {
      default:
        paramTYPE = tYPE;
        break;
      case null:
        cLElement = CLToken.allocate(paramArrayOfchar);
        break;
      case null:
        cLElement = CLKey.allocate(paramArrayOfchar);
        break;
      case null:
        cLElement = CLNumber.allocate(paramArrayOfchar);
        break;
      case null:
        cLElement = CLString.allocate(paramArrayOfchar);
        break;
      case null:
        cLElement = CLArray.allocate(paramArrayOfchar);
        paramInt++;
        break;
      case null:
        cLElement = CLObject.allocate(paramArrayOfchar);
        paramInt++;
        break;
    } 
    if (cLElement == null)
      return null; 
    cLElement.setLine(this.lineNumber);
    if (paramBoolean)
      cLElement.setStart(paramInt); 
    if (paramCLElement instanceof CLContainer)
      cLElement.setContainer((CLContainer)paramCLElement); 
    return cLElement;
  }
  
  private CLElement getNextJsonElement(int paramInt, char paramChar, CLElement paramCLElement, char[] paramArrayOfchar) throws CLParsingException {
    switch (paramChar) {
      default:
        if (paramCLElement instanceof CLContainer && !(paramCLElement instanceof CLObject)) {
          null = createElement(paramCLElement, paramInt, TYPE.TOKEN, true, paramArrayOfchar);
          paramCLElement = null;
          if (!paramCLElement.validate(paramChar, paramInt))
            throw new CLParsingException("incorrect token <" + paramChar + "> at line " + this.lineNumber, paramCLElement); 
          return null;
        } 
        break;
      case '{':
        return createElement(paramCLElement, paramInt, TYPE.OBJECT, true, paramArrayOfchar);
      case ']':
      case '}':
        paramCLElement.setEnd((paramInt - 1));
        null = paramCLElement.getContainer();
        null.setEnd(paramInt);
        return null;
      case '[':
        return createElement(paramCLElement, paramInt, TYPE.ARRAY, true, paramArrayOfchar);
      case '/':
        null = paramCLElement;
        if (paramInt + 1 < paramArrayOfchar.length) {
          null = paramCLElement;
          if (paramArrayOfchar[paramInt + 1] == '/') {
            this.hasComment = true;
            null = paramCLElement;
          } 
        } 
        return null;
      case '+':
      case '-':
      case '.':
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
        return createElement(paramCLElement, paramInt, TYPE.NUMBER, true, paramArrayOfchar);
      case '"':
      case '\'':
        if (paramCLElement instanceof CLObject) {
          null = createElement(paramCLElement, paramInt, TYPE.KEY, true, paramArrayOfchar);
        } else {
          null = createElement(paramCLElement, paramInt, TYPE.STRING, true, paramArrayOfchar);
        } 
        return null;
      case '\t':
      case '\n':
      case '\r':
      case ' ':
      case ',':
      case ':':
        return paramCLElement;
    } 
    return createElement(paramCLElement, paramInt, TYPE.KEY, true, paramArrayOfchar);
  }
  
  public static CLObject parse(String paramString) throws CLParsingException {
    return (new CLParser(paramString)).parse();
  }
  
  public CLObject parse() throws CLParsingException {
    // Byte code:
    //   0: aload_0
    //   1: getfield mContent : Ljava/lang/String;
    //   4: invokevirtual toCharArray : ()[C
    //   7: astore #10
    //   9: aload #10
    //   11: arraylength
    //   12: istore #6
    //   14: aload_0
    //   15: iconst_1
    //   16: putfield lineNumber : I
    //   19: iconst_m1
    //   20: istore #4
    //   22: iconst_0
    //   23: istore_2
    //   24: bipush #10
    //   26: istore #5
    //   28: iload #4
    //   30: istore_3
    //   31: iload_2
    //   32: iload #6
    //   34: if_icmpge -> 75
    //   37: aload #10
    //   39: iload_2
    //   40: caload
    //   41: istore_3
    //   42: iload_3
    //   43: bipush #123
    //   45: if_icmpne -> 53
    //   48: iload_2
    //   49: istore_3
    //   50: goto -> 75
    //   53: iload_3
    //   54: bipush #10
    //   56: if_icmpne -> 69
    //   59: aload_0
    //   60: aload_0
    //   61: getfield lineNumber : I
    //   64: iconst_1
    //   65: iadd
    //   66: putfield lineNumber : I
    //   69: iinc #2, 1
    //   72: goto -> 24
    //   75: iload_3
    //   76: iconst_m1
    //   77: if_icmpeq -> 818
    //   80: aload #10
    //   82: invokestatic allocate : ([C)Landroidx/constraintlayout/core/parser/CLObject;
    //   85: astore #9
    //   87: aload #9
    //   89: aload_0
    //   90: getfield lineNumber : I
    //   93: invokevirtual setLine : (I)V
    //   96: aload #9
    //   98: iload_3
    //   99: i2l
    //   100: invokevirtual setStart : (J)V
    //   103: aload #9
    //   105: astore #8
    //   107: iload_3
    //   108: iconst_1
    //   109: iadd
    //   110: istore #4
    //   112: iload #5
    //   114: istore_2
    //   115: iload #4
    //   117: istore #5
    //   119: iload_3
    //   120: istore #4
    //   122: iload #5
    //   124: iload #6
    //   126: if_icmpge -> 722
    //   129: aload #10
    //   131: iload #5
    //   133: caload
    //   134: istore_1
    //   135: iload_1
    //   136: iload_2
    //   137: if_icmpne -> 150
    //   140: aload_0
    //   141: aload_0
    //   142: getfield lineNumber : I
    //   145: iconst_1
    //   146: iadd
    //   147: putfield lineNumber : I
    //   150: aload_0
    //   151: getfield hasComment : Z
    //   154: ifeq -> 175
    //   157: iload_1
    //   158: iload_2
    //   159: if_icmpne -> 170
    //   162: aload_0
    //   163: iconst_0
    //   164: putfield hasComment : Z
    //   167: goto -> 175
    //   170: iload_2
    //   171: istore_3
    //   172: goto -> 714
    //   175: aload #8
    //   177: ifnonnull -> 183
    //   180: goto -> 722
    //   183: aload #8
    //   185: invokevirtual isDone : ()Z
    //   188: ifeq -> 207
    //   191: aload_0
    //   192: iload #5
    //   194: iload_1
    //   195: aload #8
    //   197: aload #10
    //   199: invokespecial getNextJsonElement : (ICLandroidx/constraintlayout/core/parser/CLElement;[C)Landroidx/constraintlayout/core/parser/CLElement;
    //   202: astore #7
    //   204: goto -> 663
    //   207: aload #8
    //   209: instanceof androidx/constraintlayout/core/parser/CLObject
    //   212: ifeq -> 254
    //   215: iload_1
    //   216: bipush #125
    //   218: if_icmpne -> 238
    //   221: aload #8
    //   223: iload #5
    //   225: iconst_1
    //   226: isub
    //   227: i2l
    //   228: invokevirtual setEnd : (J)V
    //   231: aload #8
    //   233: astore #7
    //   235: goto -> 663
    //   238: aload_0
    //   239: iload #5
    //   241: iload_1
    //   242: aload #8
    //   244: aload #10
    //   246: invokespecial getNextJsonElement : (ICLandroidx/constraintlayout/core/parser/CLElement;[C)Landroidx/constraintlayout/core/parser/CLElement;
    //   249: astore #7
    //   251: goto -> 663
    //   254: aload #8
    //   256: instanceof androidx/constraintlayout/core/parser/CLArray
    //   259: ifeq -> 301
    //   262: iload_1
    //   263: bipush #93
    //   265: if_icmpne -> 285
    //   268: aload #8
    //   270: iload #5
    //   272: iconst_1
    //   273: isub
    //   274: i2l
    //   275: invokevirtual setEnd : (J)V
    //   278: aload #8
    //   280: astore #7
    //   282: goto -> 663
    //   285: aload_0
    //   286: iload #5
    //   288: iload_1
    //   289: aload #8
    //   291: aload #10
    //   293: invokespecial getNextJsonElement : (ICLandroidx/constraintlayout/core/parser/CLElement;[C)Landroidx/constraintlayout/core/parser/CLElement;
    //   296: astore #7
    //   298: goto -> 663
    //   301: aload #8
    //   303: instanceof androidx/constraintlayout/core/parser/CLString
    //   306: ifeq -> 351
    //   309: aload #10
    //   311: aload #8
    //   313: getfield start : J
    //   316: l2i
    //   317: caload
    //   318: iload_1
    //   319: if_icmpne -> 344
    //   322: aload #8
    //   324: aload #8
    //   326: getfield start : J
    //   329: lconst_1
    //   330: ladd
    //   331: invokevirtual setStart : (J)V
    //   334: aload #8
    //   336: iload #5
    //   338: iconst_1
    //   339: isub
    //   340: i2l
    //   341: invokevirtual setEnd : (J)V
    //   344: aload #8
    //   346: astore #7
    //   348: goto -> 663
    //   351: aload #8
    //   353: instanceof androidx/constraintlayout/core/parser/CLToken
    //   356: ifeq -> 426
    //   359: aload #8
    //   361: checkcast androidx/constraintlayout/core/parser/CLToken
    //   364: astore #7
    //   366: aload #7
    //   368: iload_1
    //   369: iload #5
    //   371: i2l
    //   372: invokevirtual validate : (CJ)Z
    //   375: ifeq -> 381
    //   378: goto -> 426
    //   381: new androidx/constraintlayout/core/parser/CLParsingException
    //   384: dup
    //   385: new java/lang/StringBuilder
    //   388: dup
    //   389: invokespecial <init> : ()V
    //   392: ldc 'parsing incorrect token '
    //   394: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   397: aload #7
    //   399: invokevirtual content : ()Ljava/lang/String;
    //   402: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   405: ldc ' at line '
    //   407: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   410: aload_0
    //   411: getfield lineNumber : I
    //   414: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   417: invokevirtual toString : ()Ljava/lang/String;
    //   420: aload #7
    //   422: invokespecial <init> : (Ljava/lang/String;Landroidx/constraintlayout/core/parser/CLElement;)V
    //   425: athrow
    //   426: aload #8
    //   428: instanceof androidx/constraintlayout/core/parser/CLKey
    //   431: ifne -> 448
    //   434: aload #8
    //   436: instanceof androidx/constraintlayout/core/parser/CLString
    //   439: ifeq -> 445
    //   442: goto -> 448
    //   445: goto -> 506
    //   448: aload #10
    //   450: aload #8
    //   452: getfield start : J
    //   455: l2i
    //   456: caload
    //   457: istore_2
    //   458: iload_2
    //   459: bipush #39
    //   461: if_icmpeq -> 476
    //   464: iload_2
    //   465: bipush #34
    //   467: if_icmpne -> 473
    //   470: goto -> 476
    //   473: goto -> 506
    //   476: iload_2
    //   477: iload_1
    //   478: if_icmpne -> 506
    //   481: aload #8
    //   483: aload #8
    //   485: getfield start : J
    //   488: lconst_1
    //   489: ladd
    //   490: invokevirtual setStart : (J)V
    //   493: aload #8
    //   495: iload #5
    //   497: iconst_1
    //   498: isub
    //   499: i2l
    //   500: invokevirtual setEnd : (J)V
    //   503: goto -> 506
    //   506: aload #8
    //   508: invokevirtual isDone : ()Z
    //   511: ifne -> 656
    //   514: iload_1
    //   515: bipush #125
    //   517: if_icmpeq -> 572
    //   520: iload_1
    //   521: bipush #93
    //   523: if_icmpeq -> 572
    //   526: iload_1
    //   527: bipush #44
    //   529: if_icmpeq -> 572
    //   532: iload_1
    //   533: bipush #32
    //   535: if_icmpeq -> 572
    //   538: iload_1
    //   539: bipush #9
    //   541: if_icmpeq -> 572
    //   544: iload_1
    //   545: bipush #13
    //   547: if_icmpeq -> 572
    //   550: bipush #10
    //   552: istore_2
    //   553: iload_1
    //   554: bipush #10
    //   556: if_icmpeq -> 572
    //   559: aload #8
    //   561: astore #7
    //   563: iload_1
    //   564: bipush #58
    //   566: if_icmpne -> 663
    //   569: goto -> 572
    //   572: bipush #10
    //   574: istore_3
    //   575: aload #8
    //   577: iload #5
    //   579: iconst_1
    //   580: isub
    //   581: i2l
    //   582: invokevirtual setEnd : (J)V
    //   585: iload_1
    //   586: bipush #125
    //   588: if_icmpeq -> 603
    //   591: aload #8
    //   593: astore #7
    //   595: iload_3
    //   596: istore_2
    //   597: iload_1
    //   598: bipush #93
    //   600: if_icmpne -> 663
    //   603: aload #8
    //   605: invokevirtual getContainer : ()Landroidx/constraintlayout/core/parser/CLElement;
    //   608: astore #8
    //   610: aload #8
    //   612: iload #5
    //   614: iconst_1
    //   615: isub
    //   616: i2l
    //   617: invokevirtual setEnd : (J)V
    //   620: aload #8
    //   622: astore #7
    //   624: iload_3
    //   625: istore_2
    //   626: aload #8
    //   628: instanceof androidx/constraintlayout/core/parser/CLKey
    //   631: ifeq -> 663
    //   634: aload #8
    //   636: invokevirtual getContainer : ()Landroidx/constraintlayout/core/parser/CLElement;
    //   639: astore #7
    //   641: aload #7
    //   643: iload #5
    //   645: iconst_1
    //   646: isub
    //   647: i2l
    //   648: invokevirtual setEnd : (J)V
    //   651: iload_3
    //   652: istore_2
    //   653: goto -> 663
    //   656: bipush #10
    //   658: istore_2
    //   659: aload #8
    //   661: astore #7
    //   663: aload #7
    //   665: astore #8
    //   667: iload_2
    //   668: istore_3
    //   669: aload #7
    //   671: invokevirtual isDone : ()Z
    //   674: ifeq -> 714
    //   677: aload #7
    //   679: instanceof androidx/constraintlayout/core/parser/CLKey
    //   682: ifeq -> 705
    //   685: aload #7
    //   687: astore #8
    //   689: iload_2
    //   690: istore_3
    //   691: aload #7
    //   693: checkcast androidx/constraintlayout/core/parser/CLKey
    //   696: getfield mElements : Ljava/util/ArrayList;
    //   699: invokevirtual size : ()I
    //   702: ifle -> 714
    //   705: aload #7
    //   707: invokevirtual getContainer : ()Landroidx/constraintlayout/core/parser/CLElement;
    //   710: astore #8
    //   712: iload_2
    //   713: istore_3
    //   714: iinc #5, 1
    //   717: iload_3
    //   718: istore_2
    //   719: goto -> 122
    //   722: aload #8
    //   724: ifnull -> 780
    //   727: aload #8
    //   729: invokevirtual isDone : ()Z
    //   732: ifne -> 780
    //   735: aload #8
    //   737: instanceof androidx/constraintlayout/core/parser/CLString
    //   740: ifeq -> 760
    //   743: aload #8
    //   745: aload #8
    //   747: getfield start : J
    //   750: l2i
    //   751: iconst_1
    //   752: iadd
    //   753: i2l
    //   754: invokevirtual setStart : (J)V
    //   757: goto -> 760
    //   760: aload #8
    //   762: iload #6
    //   764: iconst_1
    //   765: isub
    //   766: i2l
    //   767: invokevirtual setEnd : (J)V
    //   770: aload #8
    //   772: invokevirtual getContainer : ()Landroidx/constraintlayout/core/parser/CLElement;
    //   775: astore #8
    //   777: goto -> 722
    //   780: getstatic androidx/constraintlayout/core/parser/CLParser.DEBUG : Z
    //   783: ifeq -> 815
    //   786: getstatic java/lang/System.out : Ljava/io/PrintStream;
    //   789: new java/lang/StringBuilder
    //   792: dup
    //   793: invokespecial <init> : ()V
    //   796: ldc 'Root: '
    //   798: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   801: aload #9
    //   803: invokevirtual toJSON : ()Ljava/lang/String;
    //   806: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   809: invokevirtual toString : ()Ljava/lang/String;
    //   812: invokevirtual println : (Ljava/lang/String;)V
    //   815: aload #9
    //   817: areturn
    //   818: new androidx/constraintlayout/core/parser/CLParsingException
    //   821: dup
    //   822: ldc 'invalid json content'
    //   824: aconst_null
    //   825: invokespecial <init> : (Ljava/lang/String;Landroidx/constraintlayout/core/parser/CLElement;)V
    //   828: athrow
  }
  
  enum TYPE {
    ARRAY, KEY, NUMBER, OBJECT, STRING, TOKEN, UNKNOWN;
    
    private static final TYPE[] $VALUES;
    
    static {
      TYPE tYPE3 = new TYPE("UNKNOWN", 0);
      UNKNOWN = tYPE3;
      TYPE tYPE1 = new TYPE("OBJECT", 1);
      OBJECT = tYPE1;
      TYPE tYPE2 = new TYPE("ARRAY", 2);
      ARRAY = tYPE2;
      TYPE tYPE7 = new TYPE("NUMBER", 3);
      NUMBER = tYPE7;
      TYPE tYPE4 = new TYPE("STRING", 4);
      STRING = tYPE4;
      TYPE tYPE5 = new TYPE("KEY", 5);
      KEY = tYPE5;
      TYPE tYPE6 = new TYPE("TOKEN", 6);
      TOKEN = tYPE6;
      $VALUES = new TYPE[] { tYPE3, tYPE1, tYPE2, tYPE7, tYPE4, tYPE5, tYPE6 };
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\parser\CLParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */