package com.bumptech.glide.disklrucache;

import android.os.Build;
import android.os.StrictMode;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class DiskLruCache implements Closeable {
  static final long ANY_SEQUENCE_NUMBER = -1L;
  
  private static final String CLEAN = "CLEAN";
  
  private static final String DIRTY = "DIRTY";
  
  static final String JOURNAL_FILE = "journal";
  
  static final String JOURNAL_FILE_BACKUP = "journal.bkp";
  
  static final String JOURNAL_FILE_TEMP = "journal.tmp";
  
  static final String MAGIC = "libcore.io.DiskLruCache";
  
  private static final String READ = "READ";
  
  private static final String REMOVE = "REMOVE";
  
  static final String VERSION_1 = "1";
  
  private final int appVersion;
  
  private final Callable<Void> cleanupCallable = new Callable<Void>() {
      final DiskLruCache this$0;
      
      public Void call() throws Exception {
        synchronized (DiskLruCache.this) {
          if (DiskLruCache.this.journalWriter == null)
            return null; 
          DiskLruCache.this.trimToSize();
          if (DiskLruCache.this.journalRebuildRequired()) {
            DiskLruCache.this.rebuildJournal();
            DiskLruCache.access$502(DiskLruCache.this, 0);
          } 
          return null;
        } 
      }
    };
  
  private final File directory;
  
  final ThreadPoolExecutor executorService = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new DiskLruCacheThreadFactory());
  
  private final File journalFile;
  
  private final File journalFileBackup;
  
  private final File journalFileTmp;
  
  private Writer journalWriter;
  
  private final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap<>(0, 0.75F, true);
  
  private long maxSize;
  
  private long nextSequenceNumber = 0L;
  
  private int redundantOpCount;
  
  private long size = 0L;
  
  private final int valueCount;
  
  private DiskLruCache(File paramFile, int paramInt1, int paramInt2, long paramLong) {
    this.directory = paramFile;
    this.appVersion = paramInt1;
    this.journalFile = new File(paramFile, "journal");
    this.journalFileTmp = new File(paramFile, "journal.tmp");
    this.journalFileBackup = new File(paramFile, "journal.bkp");
    this.valueCount = paramInt2;
    this.maxSize = paramLong;
  }
  
  private void checkNotClosed() {
    if (this.journalWriter != null)
      return; 
    throw new IllegalStateException("cache is closed");
  }
  
  private static void closeWriter(Writer paramWriter) throws IOException {
    if (Build.VERSION.SDK_INT < 26) {
      paramWriter.close();
      return;
    } 
    StrictMode.ThreadPolicy threadPolicy = StrictMode.getThreadPolicy();
    StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder(threadPolicy)).permitUnbufferedIo().build());
    try {
      paramWriter.close();
      return;
    } finally {
      StrictMode.setThreadPolicy(threadPolicy);
    } 
  }
  
  private void completeEdit(Editor paramEditor, boolean paramBoolean) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokestatic access$1500 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Editor;)Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;
    //   6: astore #8
    //   8: aload #8
    //   10: invokestatic access$800 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Lcom/bumptech/glide/disklrucache/DiskLruCache$Editor;
    //   13: aload_1
    //   14: if_acmpne -> 422
    //   17: iload_2
    //   18: ifeq -> 111
    //   21: aload #8
    //   23: invokestatic access$700 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Z
    //   26: ifne -> 111
    //   29: iconst_0
    //   30: istore_3
    //   31: iload_3
    //   32: aload_0
    //   33: getfield valueCount : I
    //   36: if_icmpge -> 111
    //   39: aload_1
    //   40: invokestatic access$1600 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Editor;)[Z
    //   43: iload_3
    //   44: baload
    //   45: ifeq -> 73
    //   48: aload #8
    //   50: iload_3
    //   51: invokevirtual getDirtyFile : (I)Ljava/io/File;
    //   54: invokevirtual exists : ()Z
    //   57: ifne -> 67
    //   60: aload_1
    //   61: invokevirtual abort : ()V
    //   64: aload_0
    //   65: monitorexit
    //   66: return
    //   67: iinc #3, 1
    //   70: goto -> 31
    //   73: aload_1
    //   74: invokevirtual abort : ()V
    //   77: new java/lang/IllegalStateException
    //   80: astore #8
    //   82: new java/lang/StringBuilder
    //   85: astore_1
    //   86: aload_1
    //   87: invokespecial <init> : ()V
    //   90: aload #8
    //   92: aload_1
    //   93: ldc 'Newly created entry didn't create value for index '
    //   95: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   98: iload_3
    //   99: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   102: invokevirtual toString : ()Ljava/lang/String;
    //   105: invokespecial <init> : (Ljava/lang/String;)V
    //   108: aload #8
    //   110: athrow
    //   111: iconst_0
    //   112: istore_3
    //   113: iload_3
    //   114: aload_0
    //   115: getfield valueCount : I
    //   118: if_icmpge -> 206
    //   121: aload #8
    //   123: iload_3
    //   124: invokevirtual getDirtyFile : (I)Ljava/io/File;
    //   127: astore_1
    //   128: iload_2
    //   129: ifeq -> 196
    //   132: aload_1
    //   133: invokevirtual exists : ()Z
    //   136: ifeq -> 200
    //   139: aload #8
    //   141: iload_3
    //   142: invokevirtual getCleanFile : (I)Ljava/io/File;
    //   145: astore #9
    //   147: aload_1
    //   148: aload #9
    //   150: invokevirtual renameTo : (Ljava/io/File;)Z
    //   153: pop
    //   154: aload #8
    //   156: invokestatic access$1100 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)[J
    //   159: iload_3
    //   160: laload
    //   161: lstore #4
    //   163: aload #9
    //   165: invokevirtual length : ()J
    //   168: lstore #6
    //   170: aload #8
    //   172: invokestatic access$1100 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)[J
    //   175: iload_3
    //   176: lload #6
    //   178: lastore
    //   179: aload_0
    //   180: aload_0
    //   181: getfield size : J
    //   184: lload #4
    //   186: lsub
    //   187: lload #6
    //   189: ladd
    //   190: putfield size : J
    //   193: goto -> 200
    //   196: aload_1
    //   197: invokestatic deleteIfExists : (Ljava/io/File;)V
    //   200: iinc #3, 1
    //   203: goto -> 113
    //   206: aload_0
    //   207: aload_0
    //   208: getfield redundantOpCount : I
    //   211: iconst_1
    //   212: iadd
    //   213: putfield redundantOpCount : I
    //   216: aload #8
    //   218: aconst_null
    //   219: invokestatic access$802 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;Lcom/bumptech/glide/disklrucache/DiskLruCache$Editor;)Lcom/bumptech/glide/disklrucache/DiskLruCache$Editor;
    //   222: pop
    //   223: aload #8
    //   225: invokestatic access$700 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Z
    //   228: iload_2
    //   229: ior
    //   230: ifeq -> 325
    //   233: aload #8
    //   235: iconst_1
    //   236: invokestatic access$702 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;Z)Z
    //   239: pop
    //   240: aload_0
    //   241: getfield journalWriter : Ljava/io/Writer;
    //   244: ldc 'CLEAN'
    //   246: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/io/Writer;
    //   249: pop
    //   250: aload_0
    //   251: getfield journalWriter : Ljava/io/Writer;
    //   254: bipush #32
    //   256: invokevirtual append : (C)Ljava/io/Writer;
    //   259: pop
    //   260: aload_0
    //   261: getfield journalWriter : Ljava/io/Writer;
    //   264: aload #8
    //   266: invokestatic access$1200 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Ljava/lang/String;
    //   269: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/io/Writer;
    //   272: pop
    //   273: aload_0
    //   274: getfield journalWriter : Ljava/io/Writer;
    //   277: aload #8
    //   279: invokevirtual getLengths : ()Ljava/lang/String;
    //   282: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/io/Writer;
    //   285: pop
    //   286: aload_0
    //   287: getfield journalWriter : Ljava/io/Writer;
    //   290: bipush #10
    //   292: invokevirtual append : (C)Ljava/io/Writer;
    //   295: pop
    //   296: iload_2
    //   297: ifeq -> 381
    //   300: aload_0
    //   301: getfield nextSequenceNumber : J
    //   304: lstore #4
    //   306: aload_0
    //   307: lconst_1
    //   308: lload #4
    //   310: ladd
    //   311: putfield nextSequenceNumber : J
    //   314: aload #8
    //   316: lload #4
    //   318: invokestatic access$1302 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;J)J
    //   321: pop2
    //   322: goto -> 381
    //   325: aload_0
    //   326: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   329: aload #8
    //   331: invokestatic access$1200 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Ljava/lang/String;
    //   334: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   337: pop
    //   338: aload_0
    //   339: getfield journalWriter : Ljava/io/Writer;
    //   342: ldc 'REMOVE'
    //   344: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/io/Writer;
    //   347: pop
    //   348: aload_0
    //   349: getfield journalWriter : Ljava/io/Writer;
    //   352: bipush #32
    //   354: invokevirtual append : (C)Ljava/io/Writer;
    //   357: pop
    //   358: aload_0
    //   359: getfield journalWriter : Ljava/io/Writer;
    //   362: aload #8
    //   364: invokestatic access$1200 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Ljava/lang/String;
    //   367: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/io/Writer;
    //   370: pop
    //   371: aload_0
    //   372: getfield journalWriter : Ljava/io/Writer;
    //   375: bipush #10
    //   377: invokevirtual append : (C)Ljava/io/Writer;
    //   380: pop
    //   381: aload_0
    //   382: getfield journalWriter : Ljava/io/Writer;
    //   385: invokestatic flushWriter : (Ljava/io/Writer;)V
    //   388: aload_0
    //   389: getfield size : J
    //   392: aload_0
    //   393: getfield maxSize : J
    //   396: lcmp
    //   397: ifgt -> 407
    //   400: aload_0
    //   401: invokespecial journalRebuildRequired : ()Z
    //   404: ifeq -> 419
    //   407: aload_0
    //   408: getfield executorService : Ljava/util/concurrent/ThreadPoolExecutor;
    //   411: aload_0
    //   412: getfield cleanupCallable : Ljava/util/concurrent/Callable;
    //   415: invokevirtual submit : (Ljava/util/concurrent/Callable;)Ljava/util/concurrent/Future;
    //   418: pop
    //   419: aload_0
    //   420: monitorexit
    //   421: return
    //   422: new java/lang/IllegalStateException
    //   425: astore_1
    //   426: aload_1
    //   427: invokespecial <init> : ()V
    //   430: aload_1
    //   431: athrow
    //   432: astore_1
    //   433: aload_0
    //   434: monitorexit
    //   435: aload_1
    //   436: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	432	finally
    //   21	29	432	finally
    //   31	64	432	finally
    //   73	111	432	finally
    //   113	128	432	finally
    //   132	193	432	finally
    //   196	200	432	finally
    //   206	296	432	finally
    //   300	322	432	finally
    //   325	381	432	finally
    //   381	407	432	finally
    //   407	419	432	finally
    //   422	432	432	finally
  }
  
  private static void deleteIfExists(File paramFile) throws IOException {
    if (!paramFile.exists() || paramFile.delete())
      return; 
    throw new IOException();
  }
  
  private Editor edit(String paramString, long paramLong) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkNotClosed : ()V
    //   6: aload_0
    //   7: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   10: aload_1
    //   11: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   14: checkcast com/bumptech/glide/disklrucache/DiskLruCache$Entry
    //   17: astore #6
    //   19: lload_2
    //   20: ldc2_w -1
    //   23: lcmp
    //   24: ifeq -> 50
    //   27: aload #6
    //   29: ifnull -> 46
    //   32: aload #6
    //   34: invokestatic access$1300 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)J
    //   37: lstore #4
    //   39: lload #4
    //   41: lload_2
    //   42: lcmp
    //   43: ifeq -> 50
    //   46: aload_0
    //   47: monitorexit
    //   48: aconst_null
    //   49: areturn
    //   50: aload #6
    //   52: ifnonnull -> 82
    //   55: new com/bumptech/glide/disklrucache/DiskLruCache$Entry
    //   58: astore #6
    //   60: aload #6
    //   62: aload_0
    //   63: aload_1
    //   64: aconst_null
    //   65: invokespecial <init> : (Lcom/bumptech/glide/disklrucache/DiskLruCache;Ljava/lang/String;Lcom/bumptech/glide/disklrucache/DiskLruCache$1;)V
    //   68: aload_0
    //   69: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   72: aload_1
    //   73: aload #6
    //   75: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   78: pop
    //   79: goto -> 98
    //   82: aload #6
    //   84: invokestatic access$800 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Lcom/bumptech/glide/disklrucache/DiskLruCache$Editor;
    //   87: astore #7
    //   89: aload #7
    //   91: ifnull -> 98
    //   94: aload_0
    //   95: monitorexit
    //   96: aconst_null
    //   97: areturn
    //   98: new com/bumptech/glide/disklrucache/DiskLruCache$Editor
    //   101: astore #7
    //   103: aload #7
    //   105: aload_0
    //   106: aload #6
    //   108: aconst_null
    //   109: invokespecial <init> : (Lcom/bumptech/glide/disklrucache/DiskLruCache;Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;Lcom/bumptech/glide/disklrucache/DiskLruCache$1;)V
    //   112: aload #6
    //   114: aload #7
    //   116: invokestatic access$802 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;Lcom/bumptech/glide/disklrucache/DiskLruCache$Editor;)Lcom/bumptech/glide/disklrucache/DiskLruCache$Editor;
    //   119: pop
    //   120: aload_0
    //   121: getfield journalWriter : Ljava/io/Writer;
    //   124: ldc 'DIRTY'
    //   126: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/io/Writer;
    //   129: pop
    //   130: aload_0
    //   131: getfield journalWriter : Ljava/io/Writer;
    //   134: bipush #32
    //   136: invokevirtual append : (C)Ljava/io/Writer;
    //   139: pop
    //   140: aload_0
    //   141: getfield journalWriter : Ljava/io/Writer;
    //   144: aload_1
    //   145: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/io/Writer;
    //   148: pop
    //   149: aload_0
    //   150: getfield journalWriter : Ljava/io/Writer;
    //   153: bipush #10
    //   155: invokevirtual append : (C)Ljava/io/Writer;
    //   158: pop
    //   159: aload_0
    //   160: getfield journalWriter : Ljava/io/Writer;
    //   163: invokestatic flushWriter : (Ljava/io/Writer;)V
    //   166: aload_0
    //   167: monitorexit
    //   168: aload #7
    //   170: areturn
    //   171: astore_1
    //   172: aload_0
    //   173: monitorexit
    //   174: aload_1
    //   175: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	171	finally
    //   32	39	171	finally
    //   55	68	171	finally
    //   68	79	171	finally
    //   82	89	171	finally
    //   98	166	171	finally
  }
  
  private static void flushWriter(Writer paramWriter) throws IOException {
    if (Build.VERSION.SDK_INT < 26) {
      paramWriter.flush();
      return;
    } 
    StrictMode.ThreadPolicy threadPolicy = StrictMode.getThreadPolicy();
    StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder(threadPolicy)).permitUnbufferedIo().build());
    try {
      paramWriter.flush();
      return;
    } finally {
      StrictMode.setThreadPolicy(threadPolicy);
    } 
  }
  
  private static String inputStreamToString(InputStream paramInputStream) throws IOException {
    return Util.readFully(new InputStreamReader(paramInputStream, Util.UTF_8));
  }
  
  private boolean journalRebuildRequired() {
    boolean bool;
    int i = this.redundantOpCount;
    if (i >= 2000 && i >= this.lruEntries.size()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static DiskLruCache open(File paramFile, int paramInt1, int paramInt2, long paramLong) throws IOException {
    if (paramLong > 0L) {
      if (paramInt2 > 0) {
        File file = new File(paramFile, "journal.bkp");
        if (file.exists()) {
          File file1 = new File(paramFile, "journal");
          if (file1.exists()) {
            file.delete();
          } else {
            renameTo(file, file1, false);
          } 
        } 
        DiskLruCache diskLruCache2 = new DiskLruCache(paramFile, paramInt1, paramInt2, paramLong);
        if (diskLruCache2.journalFile.exists())
          try {
            diskLruCache2.readJournal();
            diskLruCache2.processJournal();
            return diskLruCache2;
          } catch (IOException iOException) {
            System.out.println("DiskLruCache " + paramFile + " is corrupt: " + iOException.getMessage() + ", removing");
            diskLruCache2.delete();
          }  
        paramFile.mkdirs();
        DiskLruCache diskLruCache1 = new DiskLruCache(paramFile, paramInt1, paramInt2, paramLong);
        diskLruCache1.rebuildJournal();
        return diskLruCache1;
      } 
      throw new IllegalArgumentException("valueCount <= 0");
    } 
    throw new IllegalArgumentException("maxSize <= 0");
  }
  
  private void processJournal() throws IOException {
    deleteIfExists(this.journalFileTmp);
    Iterator<Entry> iterator = this.lruEntries.values().iterator();
    while (iterator.hasNext()) {
      Entry entry = iterator.next();
      if (entry.currentEditor == null) {
        for (byte b1 = 0; b1 < this.valueCount; b1++)
          this.size += entry.lengths[b1]; 
        continue;
      } 
      Entry.access$802(entry, null);
      for (byte b = 0; b < this.valueCount; b++) {
        deleteIfExists(entry.getCleanFile(b));
        deleteIfExists(entry.getDirtyFile(b));
      } 
      iterator.remove();
    } 
  }
  
  private void readJournal() throws IOException {
    StrictLineReader strictLineReader = new StrictLineReader(new FileInputStream(this.journalFile), Util.US_ASCII);
    try {
      FileOutputStream fileOutputStream;
      BufferedWriter bufferedWriter;
      OutputStreamWriter outputStreamWriter;
      String str3 = strictLineReader.readLine();
      String str4 = strictLineReader.readLine();
      String str5 = strictLineReader.readLine();
      String str1 = strictLineReader.readLine();
      String str2 = strictLineReader.readLine();
      if ("libcore.io.DiskLruCache".equals(str3) && "1".equals(str4) && Integer.toString(this.appVersion).equals(str5) && Integer.toString(this.valueCount).equals(str1)) {
        boolean bool = "".equals(str2);
        if (bool) {
          byte b = 0;
          try {
            while (true) {
              readJournalLine(strictLineReader.readLine());
              b++;
            } 
          } catch (EOFException eOFException) {
            this.redundantOpCount = b - this.lruEntries.size();
            if (strictLineReader.hasUnterminatedLine()) {
              rebuildJournal();
            } else {
              bufferedWriter = new BufferedWriter();
              outputStreamWriter = new OutputStreamWriter();
              fileOutputStream = new FileOutputStream();
              this(this.journalFile, true);
              this(fileOutputStream, Util.US_ASCII);
              this(outputStreamWriter);
              this.journalWriter = bufferedWriter;
            } 
            return;
          } 
        } 
      } 
      IOException iOException = new IOException();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      this(stringBuilder.append("unexpected journal header: [").append((String)outputStreamWriter).append(", ").append(str4).append(", ").append((String)fileOutputStream).append(", ").append((String)bufferedWriter).append("]").toString());
      throw iOException;
    } finally {
      Util.closeQuietly(strictLineReader);
    } 
  }
  
  private void readJournalLine(String paramString) throws IOException {
    String[] arrayOfString;
    int i = paramString.indexOf(' ');
    if (i != -1) {
      String str;
      int j = i + 1;
      int k = paramString.indexOf(' ', j);
      if (k == -1) {
        String str1 = paramString.substring(j);
        str = str1;
        if (i == "REMOVE".length()) {
          str = str1;
          if (paramString.startsWith("REMOVE")) {
            this.lruEntries.remove(str1);
            return;
          } 
        } 
      } else {
        str = paramString.substring(j, k);
      } 
      Entry entry2 = this.lruEntries.get(str);
      Entry entry1 = entry2;
      if (entry2 == null) {
        entry1 = new Entry(str);
        this.lruEntries.put(str, entry1);
      } 
      if (k != -1 && i == "CLEAN".length() && paramString.startsWith("CLEAN")) {
        arrayOfString = paramString.substring(k + 1).split(" ");
        Entry.access$702(entry1, true);
        Entry.access$802(entry1, null);
        entry1.setLengths(arrayOfString);
      } else if (k == -1 && i == "DIRTY".length() && arrayOfString.startsWith("DIRTY")) {
        Entry.access$802(entry1, new Editor(entry1));
      } else if (k != -1 || i != "READ".length() || !arrayOfString.startsWith("READ")) {
        throw new IOException("unexpected journal line: " + arrayOfString);
      } 
      return;
    } 
    throw new IOException("unexpected journal line: " + arrayOfString);
  }
  
  private void rebuildJournal() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield journalWriter : Ljava/io/Writer;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnull -> 15
    //   11: aload_1
    //   12: invokestatic closeWriter : (Ljava/io/Writer;)V
    //   15: new java/io/BufferedWriter
    //   18: astore_1
    //   19: new java/io/OutputStreamWriter
    //   22: astore_2
    //   23: new java/io/FileOutputStream
    //   26: astore_3
    //   27: aload_3
    //   28: aload_0
    //   29: getfield journalFileTmp : Ljava/io/File;
    //   32: invokespecial <init> : (Ljava/io/File;)V
    //   35: aload_2
    //   36: aload_3
    //   37: getstatic com/bumptech/glide/disklrucache/Util.US_ASCII : Ljava/nio/charset/Charset;
    //   40: invokespecial <init> : (Ljava/io/OutputStream;Ljava/nio/charset/Charset;)V
    //   43: aload_1
    //   44: aload_2
    //   45: invokespecial <init> : (Ljava/io/Writer;)V
    //   48: aload_1
    //   49: ldc 'libcore.io.DiskLruCache'
    //   51: invokevirtual write : (Ljava/lang/String;)V
    //   54: aload_1
    //   55: ldc_w '\\n'
    //   58: invokevirtual write : (Ljava/lang/String;)V
    //   61: aload_1
    //   62: ldc '1'
    //   64: invokevirtual write : (Ljava/lang/String;)V
    //   67: aload_1
    //   68: ldc_w '\\n'
    //   71: invokevirtual write : (Ljava/lang/String;)V
    //   74: aload_1
    //   75: aload_0
    //   76: getfield appVersion : I
    //   79: invokestatic toString : (I)Ljava/lang/String;
    //   82: invokevirtual write : (Ljava/lang/String;)V
    //   85: aload_1
    //   86: ldc_w '\\n'
    //   89: invokevirtual write : (Ljava/lang/String;)V
    //   92: aload_1
    //   93: aload_0
    //   94: getfield valueCount : I
    //   97: invokestatic toString : (I)Ljava/lang/String;
    //   100: invokevirtual write : (Ljava/lang/String;)V
    //   103: aload_1
    //   104: ldc_w '\\n'
    //   107: invokevirtual write : (Ljava/lang/String;)V
    //   110: aload_1
    //   111: ldc_w '\\n'
    //   114: invokevirtual write : (Ljava/lang/String;)V
    //   117: aload_0
    //   118: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   121: invokevirtual values : ()Ljava/util/Collection;
    //   124: invokeinterface iterator : ()Ljava/util/Iterator;
    //   129: astore_3
    //   130: aload_3
    //   131: invokeinterface hasNext : ()Z
    //   136: ifeq -> 243
    //   139: aload_3
    //   140: invokeinterface next : ()Ljava/lang/Object;
    //   145: checkcast com/bumptech/glide/disklrucache/DiskLruCache$Entry
    //   148: astore_2
    //   149: aload_2
    //   150: invokestatic access$800 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Lcom/bumptech/glide/disklrucache/DiskLruCache$Editor;
    //   153: ifnull -> 196
    //   156: new java/lang/StringBuilder
    //   159: astore #4
    //   161: aload #4
    //   163: invokespecial <init> : ()V
    //   166: aload_1
    //   167: aload #4
    //   169: ldc_w 'DIRTY '
    //   172: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   175: aload_2
    //   176: invokestatic access$1200 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Ljava/lang/String;
    //   179: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   182: bipush #10
    //   184: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   187: invokevirtual toString : ()Ljava/lang/String;
    //   190: invokevirtual write : (Ljava/lang/String;)V
    //   193: goto -> 240
    //   196: new java/lang/StringBuilder
    //   199: astore #4
    //   201: aload #4
    //   203: invokespecial <init> : ()V
    //   206: aload_1
    //   207: aload #4
    //   209: ldc_w 'CLEAN '
    //   212: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   215: aload_2
    //   216: invokestatic access$1200 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Ljava/lang/String;
    //   219: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   222: aload_2
    //   223: invokevirtual getLengths : ()Ljava/lang/String;
    //   226: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   229: bipush #10
    //   231: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   234: invokevirtual toString : ()Ljava/lang/String;
    //   237: invokevirtual write : (Ljava/lang/String;)V
    //   240: goto -> 130
    //   243: aload_1
    //   244: invokestatic closeWriter : (Ljava/io/Writer;)V
    //   247: aload_0
    //   248: getfield journalFile : Ljava/io/File;
    //   251: invokevirtual exists : ()Z
    //   254: ifeq -> 269
    //   257: aload_0
    //   258: getfield journalFile : Ljava/io/File;
    //   261: aload_0
    //   262: getfield journalFileBackup : Ljava/io/File;
    //   265: iconst_1
    //   266: invokestatic renameTo : (Ljava/io/File;Ljava/io/File;Z)V
    //   269: aload_0
    //   270: getfield journalFileTmp : Ljava/io/File;
    //   273: aload_0
    //   274: getfield journalFile : Ljava/io/File;
    //   277: iconst_0
    //   278: invokestatic renameTo : (Ljava/io/File;Ljava/io/File;Z)V
    //   281: aload_0
    //   282: getfield journalFileBackup : Ljava/io/File;
    //   285: invokevirtual delete : ()Z
    //   288: pop
    //   289: new java/io/BufferedWriter
    //   292: astore_1
    //   293: new java/io/OutputStreamWriter
    //   296: astore_2
    //   297: new java/io/FileOutputStream
    //   300: astore_3
    //   301: aload_3
    //   302: aload_0
    //   303: getfield journalFile : Ljava/io/File;
    //   306: iconst_1
    //   307: invokespecial <init> : (Ljava/io/File;Z)V
    //   310: aload_2
    //   311: aload_3
    //   312: getstatic com/bumptech/glide/disklrucache/Util.US_ASCII : Ljava/nio/charset/Charset;
    //   315: invokespecial <init> : (Ljava/io/OutputStream;Ljava/nio/charset/Charset;)V
    //   318: aload_1
    //   319: aload_2
    //   320: invokespecial <init> : (Ljava/io/Writer;)V
    //   323: aload_0
    //   324: aload_1
    //   325: putfield journalWriter : Ljava/io/Writer;
    //   328: aload_0
    //   329: monitorexit
    //   330: return
    //   331: astore_2
    //   332: aload_1
    //   333: invokestatic closeWriter : (Ljava/io/Writer;)V
    //   336: aload_2
    //   337: athrow
    //   338: astore_1
    //   339: aload_0
    //   340: monitorexit
    //   341: aload_1
    //   342: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	338	finally
    //   11	15	338	finally
    //   15	48	338	finally
    //   48	130	331	finally
    //   130	193	331	finally
    //   196	240	331	finally
    //   243	247	338	finally
    //   247	269	338	finally
    //   269	328	338	finally
    //   332	338	338	finally
  }
  
  private static void renameTo(File paramFile1, File paramFile2, boolean paramBoolean) throws IOException {
    if (paramBoolean)
      deleteIfExists(paramFile2); 
    if (paramFile1.renameTo(paramFile2))
      return; 
    throw new IOException();
  }
  
  private void trimToSize() throws IOException {
    while (this.size > this.maxSize)
      remove((String)((Map.Entry)this.lruEntries.entrySet().iterator().next()).getKey()); 
  }
  
  public void close() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield journalWriter : Ljava/io/Writer;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnonnull -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: new java/util/ArrayList
    //   17: astore_1
    //   18: aload_1
    //   19: aload_0
    //   20: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   23: invokevirtual values : ()Ljava/util/Collection;
    //   26: invokespecial <init> : (Ljava/util/Collection;)V
    //   29: aload_1
    //   30: invokevirtual iterator : ()Ljava/util/Iterator;
    //   33: astore_2
    //   34: aload_2
    //   35: invokeinterface hasNext : ()Z
    //   40: ifeq -> 70
    //   43: aload_2
    //   44: invokeinterface next : ()Ljava/lang/Object;
    //   49: checkcast com/bumptech/glide/disklrucache/DiskLruCache$Entry
    //   52: astore_1
    //   53: aload_1
    //   54: invokestatic access$800 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Lcom/bumptech/glide/disklrucache/DiskLruCache$Editor;
    //   57: ifnull -> 67
    //   60: aload_1
    //   61: invokestatic access$800 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Lcom/bumptech/glide/disklrucache/DiskLruCache$Editor;
    //   64: invokevirtual abort : ()V
    //   67: goto -> 34
    //   70: aload_0
    //   71: invokespecial trimToSize : ()V
    //   74: aload_0
    //   75: getfield journalWriter : Ljava/io/Writer;
    //   78: invokestatic closeWriter : (Ljava/io/Writer;)V
    //   81: aload_0
    //   82: aconst_null
    //   83: putfield journalWriter : Ljava/io/Writer;
    //   86: aload_0
    //   87: monitorexit
    //   88: return
    //   89: astore_1
    //   90: aload_0
    //   91: monitorexit
    //   92: aload_1
    //   93: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	89	finally
    //   14	34	89	finally
    //   34	67	89	finally
    //   70	86	89	finally
  }
  
  public void delete() throws IOException {
    close();
    Util.deleteContents(this.directory);
  }
  
  public Editor edit(String paramString) throws IOException {
    return edit(paramString, -1L);
  }
  
  public void flush() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkNotClosed : ()V
    //   6: aload_0
    //   7: invokespecial trimToSize : ()V
    //   10: aload_0
    //   11: getfield journalWriter : Ljava/io/Writer;
    //   14: invokestatic flushWriter : (Ljava/io/Writer;)V
    //   17: aload_0
    //   18: monitorexit
    //   19: return
    //   20: astore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_1
    //   24: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	20	finally
  }
  
  public Value get(String paramString) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkNotClosed : ()V
    //   6: aload_0
    //   7: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   10: aload_1
    //   11: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   14: checkcast com/bumptech/glide/disklrucache/DiskLruCache$Entry
    //   17: astore #5
    //   19: aload #5
    //   21: ifnonnull -> 28
    //   24: aload_0
    //   25: monitorexit
    //   26: aconst_null
    //   27: areturn
    //   28: aload #5
    //   30: invokestatic access$700 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Z
    //   33: istore #4
    //   35: iload #4
    //   37: ifne -> 44
    //   40: aload_0
    //   41: monitorexit
    //   42: aconst_null
    //   43: areturn
    //   44: aload #5
    //   46: getfield cleanFiles : [Ljava/io/File;
    //   49: astore #6
    //   51: aload #6
    //   53: arraylength
    //   54: istore_3
    //   55: iconst_0
    //   56: istore_2
    //   57: iload_2
    //   58: iload_3
    //   59: if_icmpge -> 86
    //   62: aload #6
    //   64: iload_2
    //   65: aaload
    //   66: invokevirtual exists : ()Z
    //   69: istore #4
    //   71: iload #4
    //   73: ifne -> 80
    //   76: aload_0
    //   77: monitorexit
    //   78: aconst_null
    //   79: areturn
    //   80: iinc #2, 1
    //   83: goto -> 57
    //   86: aload_0
    //   87: aload_0
    //   88: getfield redundantOpCount : I
    //   91: iconst_1
    //   92: iadd
    //   93: putfield redundantOpCount : I
    //   96: aload_0
    //   97: getfield journalWriter : Ljava/io/Writer;
    //   100: ldc 'READ'
    //   102: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/io/Writer;
    //   105: pop
    //   106: aload_0
    //   107: getfield journalWriter : Ljava/io/Writer;
    //   110: bipush #32
    //   112: invokevirtual append : (C)Ljava/io/Writer;
    //   115: pop
    //   116: aload_0
    //   117: getfield journalWriter : Ljava/io/Writer;
    //   120: aload_1
    //   121: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/io/Writer;
    //   124: pop
    //   125: aload_0
    //   126: getfield journalWriter : Ljava/io/Writer;
    //   129: bipush #10
    //   131: invokevirtual append : (C)Ljava/io/Writer;
    //   134: pop
    //   135: aload_0
    //   136: invokespecial journalRebuildRequired : ()Z
    //   139: ifeq -> 154
    //   142: aload_0
    //   143: getfield executorService : Ljava/util/concurrent/ThreadPoolExecutor;
    //   146: aload_0
    //   147: getfield cleanupCallable : Ljava/util/concurrent/Callable;
    //   150: invokevirtual submit : (Ljava/util/concurrent/Callable;)Ljava/util/concurrent/Future;
    //   153: pop
    //   154: new com/bumptech/glide/disklrucache/DiskLruCache$Value
    //   157: dup
    //   158: aload_0
    //   159: aload_1
    //   160: aload #5
    //   162: invokestatic access$1300 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)J
    //   165: aload #5
    //   167: getfield cleanFiles : [Ljava/io/File;
    //   170: aload #5
    //   172: invokestatic access$1100 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)[J
    //   175: aconst_null
    //   176: invokespecial <init> : (Lcom/bumptech/glide/disklrucache/DiskLruCache;Ljava/lang/String;J[Ljava/io/File;[JLcom/bumptech/glide/disklrucache/DiskLruCache$1;)V
    //   179: astore_1
    //   180: aload_0
    //   181: monitorexit
    //   182: aload_1
    //   183: areturn
    //   184: astore_1
    //   185: aload_0
    //   186: monitorexit
    //   187: aload_1
    //   188: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	184	finally
    //   28	35	184	finally
    //   44	55	184	finally
    //   62	71	184	finally
    //   86	154	184	finally
    //   154	180	184	finally
  }
  
  public File getDirectory() {
    return this.directory;
  }
  
  public long getMaxSize() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield maxSize : J
    //   6: lstore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: lload_1
    //   10: lreturn
    //   11: astore_3
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_3
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public boolean isClosed() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield journalWriter : Ljava/io/Writer;
    //   6: astore_2
    //   7: aload_2
    //   8: ifnonnull -> 16
    //   11: iconst_1
    //   12: istore_1
    //   13: goto -> 18
    //   16: iconst_0
    //   17: istore_1
    //   18: aload_0
    //   19: monitorexit
    //   20: iload_1
    //   21: ireturn
    //   22: astore_2
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_2
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	22	finally
  }
  
  public boolean remove(String paramString) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkNotClosed : ()V
    //   6: aload_0
    //   7: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   10: aload_1
    //   11: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   14: checkcast com/bumptech/glide/disklrucache/DiskLruCache$Entry
    //   17: astore #4
    //   19: aload #4
    //   21: ifnull -> 215
    //   24: aload #4
    //   26: invokestatic access$800 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Lcom/bumptech/glide/disklrucache/DiskLruCache$Editor;
    //   29: ifnull -> 35
    //   32: goto -> 215
    //   35: iconst_0
    //   36: istore_2
    //   37: iload_2
    //   38: aload_0
    //   39: getfield valueCount : I
    //   42: if_icmpge -> 134
    //   45: aload #4
    //   47: iload_2
    //   48: invokevirtual getCleanFile : (I)Ljava/io/File;
    //   51: astore_3
    //   52: aload_3
    //   53: invokevirtual exists : ()Z
    //   56: ifeq -> 104
    //   59: aload_3
    //   60: invokevirtual delete : ()Z
    //   63: ifeq -> 69
    //   66: goto -> 104
    //   69: new java/io/IOException
    //   72: astore #4
    //   74: new java/lang/StringBuilder
    //   77: astore_1
    //   78: aload_1
    //   79: invokespecial <init> : ()V
    //   82: aload #4
    //   84: aload_1
    //   85: ldc_w 'failed to delete '
    //   88: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   91: aload_3
    //   92: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   95: invokevirtual toString : ()Ljava/lang/String;
    //   98: invokespecial <init> : (Ljava/lang/String;)V
    //   101: aload #4
    //   103: athrow
    //   104: aload_0
    //   105: aload_0
    //   106: getfield size : J
    //   109: aload #4
    //   111: invokestatic access$1100 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)[J
    //   114: iload_2
    //   115: laload
    //   116: lsub
    //   117: putfield size : J
    //   120: aload #4
    //   122: invokestatic access$1100 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)[J
    //   125: iload_2
    //   126: lconst_0
    //   127: lastore
    //   128: iinc #2, 1
    //   131: goto -> 37
    //   134: aload_0
    //   135: aload_0
    //   136: getfield redundantOpCount : I
    //   139: iconst_1
    //   140: iadd
    //   141: putfield redundantOpCount : I
    //   144: aload_0
    //   145: getfield journalWriter : Ljava/io/Writer;
    //   148: ldc 'REMOVE'
    //   150: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/io/Writer;
    //   153: pop
    //   154: aload_0
    //   155: getfield journalWriter : Ljava/io/Writer;
    //   158: bipush #32
    //   160: invokevirtual append : (C)Ljava/io/Writer;
    //   163: pop
    //   164: aload_0
    //   165: getfield journalWriter : Ljava/io/Writer;
    //   168: aload_1
    //   169: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/io/Writer;
    //   172: pop
    //   173: aload_0
    //   174: getfield journalWriter : Ljava/io/Writer;
    //   177: bipush #10
    //   179: invokevirtual append : (C)Ljava/io/Writer;
    //   182: pop
    //   183: aload_0
    //   184: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   187: aload_1
    //   188: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   191: pop
    //   192: aload_0
    //   193: invokespecial journalRebuildRequired : ()Z
    //   196: ifeq -> 211
    //   199: aload_0
    //   200: getfield executorService : Ljava/util/concurrent/ThreadPoolExecutor;
    //   203: aload_0
    //   204: getfield cleanupCallable : Ljava/util/concurrent/Callable;
    //   207: invokevirtual submit : (Ljava/util/concurrent/Callable;)Ljava/util/concurrent/Future;
    //   210: pop
    //   211: aload_0
    //   212: monitorexit
    //   213: iconst_1
    //   214: ireturn
    //   215: aload_0
    //   216: monitorexit
    //   217: iconst_0
    //   218: ireturn
    //   219: astore_1
    //   220: aload_0
    //   221: monitorexit
    //   222: aload_1
    //   223: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	219	finally
    //   24	32	219	finally
    //   37	66	219	finally
    //   69	104	219	finally
    //   104	128	219	finally
    //   134	211	219	finally
  }
  
  public void setMaxSize(long paramLong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: lload_1
    //   4: putfield maxSize : J
    //   7: aload_0
    //   8: getfield executorService : Ljava/util/concurrent/ThreadPoolExecutor;
    //   11: aload_0
    //   12: getfield cleanupCallable : Ljava/util/concurrent/Callable;
    //   15: invokevirtual submit : (Ljava/util/concurrent/Callable;)Ljava/util/concurrent/Future;
    //   18: pop
    //   19: aload_0
    //   20: monitorexit
    //   21: return
    //   22: astore_3
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_3
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	22	finally
  }
  
  public long size() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield size : J
    //   6: lstore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: lload_1
    //   10: lreturn
    //   11: astore_3
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_3
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  private static final class DiskLruCacheThreadFactory implements ThreadFactory {
    private DiskLruCacheThreadFactory() {}
    
    public Thread newThread(Runnable param1Runnable) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: new java/lang/Thread
      //   5: astore_2
      //   6: aload_2
      //   7: aload_1
      //   8: ldc 'glide-disk-lru-cache-thread'
      //   10: invokespecial <init> : (Ljava/lang/Runnable;Ljava/lang/String;)V
      //   13: aload_2
      //   14: iconst_1
      //   15: invokevirtual setPriority : (I)V
      //   18: aload_0
      //   19: monitorexit
      //   20: aload_2
      //   21: areturn
      //   22: astore_1
      //   23: aload_0
      //   24: monitorexit
      //   25: aload_1
      //   26: athrow
      // Exception table:
      //   from	to	target	type
      //   2	18	22	finally
    }
  }
  
  public final class Editor {
    private boolean committed;
    
    private final DiskLruCache.Entry entry;
    
    final DiskLruCache this$0;
    
    private final boolean[] written;
    
    private Editor(DiskLruCache.Entry param1Entry) {
      boolean[] arrayOfBoolean;
      this.entry = param1Entry;
      if (param1Entry.readable) {
        DiskLruCache.this = null;
      } else {
        arrayOfBoolean = new boolean[DiskLruCache.this.valueCount];
      } 
      this.written = arrayOfBoolean;
    }
    
    private InputStream newInputStream(int param1Int) throws IOException {
      synchronized (DiskLruCache.this) {
        if (this.entry.currentEditor == this) {
          if (!this.entry.readable)
            return null; 
          try {
            FileInputStream fileInputStream = new FileInputStream();
            this(this.entry.getCleanFile(param1Int));
            return fileInputStream;
          } catch (FileNotFoundException fileNotFoundException) {
            return null;
          } 
        } 
        IllegalStateException illegalStateException = new IllegalStateException();
        this();
        throw illegalStateException;
      } 
    }
    
    public void abort() throws IOException {
      DiskLruCache.this.completeEdit(this, false);
    }
    
    public void abortUnlessCommitted() {
      if (!this.committed)
        try {
          abort();
        } catch (IOException iOException) {} 
    }
    
    public void commit() throws IOException {
      DiskLruCache.this.completeEdit(this, true);
      this.committed = true;
    }
    
    public File getFile(int param1Int) throws IOException {
      synchronized (DiskLruCache.this) {
        if (this.entry.currentEditor == this) {
          if (!this.entry.readable)
            this.written[param1Int] = true; 
          File file = this.entry.getDirtyFile(param1Int);
          DiskLruCache.this.directory.mkdirs();
          return file;
        } 
        IllegalStateException illegalStateException = new IllegalStateException();
        this();
        throw illegalStateException;
      } 
    }
    
    public String getString(int param1Int) throws IOException {
      InputStream inputStream = newInputStream(param1Int);
      if (inputStream != null) {
        String str = DiskLruCache.inputStreamToString(inputStream);
      } else {
        inputStream = null;
      } 
      return (String)inputStream;
    }
    
    public void set(int param1Int, String param1String) throws IOException {
      OutputStreamWriter outputStreamWriter2 = null;
      OutputStreamWriter outputStreamWriter1 = outputStreamWriter2;
      try {
        FileOutputStream fileOutputStream = new FileOutputStream();
        outputStreamWriter1 = outputStreamWriter2;
        this(getFile(param1Int));
        outputStreamWriter1 = outputStreamWriter2;
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter();
        outputStreamWriter1 = outputStreamWriter2;
        this(fileOutputStream, Util.UTF_8);
        outputStreamWriter1 = outputStreamWriter;
        outputStreamWriter.write(param1String);
        return;
      } finally {
        Util.closeQuietly(outputStreamWriter1);
      } 
    }
  }
  
  private final class Entry {
    File[] cleanFiles;
    
    private DiskLruCache.Editor currentEditor;
    
    File[] dirtyFiles;
    
    private final String key;
    
    private final long[] lengths;
    
    private boolean readable;
    
    private long sequenceNumber;
    
    final DiskLruCache this$0;
    
    private Entry(String param1String) {
      this.key = param1String;
      this.lengths = new long[DiskLruCache.this.valueCount];
      this.cleanFiles = new File[DiskLruCache.this.valueCount];
      this.dirtyFiles = new File[DiskLruCache.this.valueCount];
      StringBuilder stringBuilder = (new StringBuilder(param1String)).append('.');
      int i = stringBuilder.length();
      for (byte b = 0; b < DiskLruCache.this.valueCount; b++) {
        stringBuilder.append(b);
        this.cleanFiles[b] = new File(DiskLruCache.this.directory, stringBuilder.toString());
        stringBuilder.append(".tmp");
        this.dirtyFiles[b] = new File(DiskLruCache.this.directory, stringBuilder.toString());
        stringBuilder.setLength(i);
      } 
    }
    
    private IOException invalidLengths(String[] param1ArrayOfString) throws IOException {
      throw new IOException("unexpected journal line: " + Arrays.toString(param1ArrayOfString));
    }
    
    private void setLengths(String[] param1ArrayOfString) throws IOException {
      if (param1ArrayOfString.length == DiskLruCache.this.valueCount) {
        byte b = 0;
        try {
          while (b < param1ArrayOfString.length) {
            this.lengths[b] = Long.parseLong(param1ArrayOfString[b]);
            b++;
          } 
          return;
        } catch (NumberFormatException numberFormatException) {
          throw invalidLengths(param1ArrayOfString);
        } 
      } 
      throw invalidLengths(param1ArrayOfString);
    }
    
    public File getCleanFile(int param1Int) {
      return this.cleanFiles[param1Int];
    }
    
    public File getDirtyFile(int param1Int) {
      return this.dirtyFiles[param1Int];
    }
    
    public String getLengths() throws IOException {
      StringBuilder stringBuilder = new StringBuilder();
      for (long l : this.lengths)
        stringBuilder.append(' ').append(l); 
      return stringBuilder.toString();
    }
  }
  
  public final class Value {
    private final File[] files;
    
    private final String key;
    
    private final long[] lengths;
    
    private final long sequenceNumber;
    
    final DiskLruCache this$0;
    
    private Value(String param1String, long param1Long, File[] param1ArrayOfFile, long[] param1ArrayOflong) {
      this.key = param1String;
      this.sequenceNumber = param1Long;
      this.files = param1ArrayOfFile;
      this.lengths = param1ArrayOflong;
    }
    
    public DiskLruCache.Editor edit() throws IOException {
      return DiskLruCache.this.edit(this.key, this.sequenceNumber);
    }
    
    public File getFile(int param1Int) {
      return this.files[param1Int];
    }
    
    public long getLength(int param1Int) {
      return this.lengths[param1Int];
    }
    
    public String getString(int param1Int) throws IOException {
      return DiskLruCache.inputStreamToString(new FileInputStream(this.files[param1Int]));
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\disklrucache\DiskLruCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */