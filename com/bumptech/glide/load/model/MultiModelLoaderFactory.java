package com.bumptech.glide.load.model;

import androidx.core.util.Pools;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultiModelLoaderFactory {
  private static final Factory DEFAULT_FACTORY = new Factory();
  
  private static final ModelLoader<Object, Object> EMPTY_MODEL_LOADER = new EmptyModelLoader();
  
  private final Set<Entry<?, ?>> alreadyUsedEntries = new HashSet<>();
  
  private final List<Entry<?, ?>> entries = new ArrayList<>();
  
  private final Factory factory;
  
  private final Pools.Pool<List<Throwable>> throwableListPool;
  
  public MultiModelLoaderFactory(Pools.Pool<List<Throwable>> paramPool) {
    this(paramPool, DEFAULT_FACTORY);
  }
  
  MultiModelLoaderFactory(Pools.Pool<List<Throwable>> paramPool, Factory paramFactory) {
    this.throwableListPool = paramPool;
    this.factory = paramFactory;
  }
  
  private <Model, Data> void add(Class<Model> paramClass, Class<Data> paramClass1, ModelLoaderFactory<? extends Model, ? extends Data> paramModelLoaderFactory, boolean paramBoolean) {
    boolean bool;
    Entry<Model, Data> entry = new Entry<>(paramClass, paramClass1, paramModelLoaderFactory);
    List<Entry<?, ?>> list = this.entries;
    if (paramBoolean) {
      bool = list.size();
    } else {
      bool = false;
    } 
    list.add(bool, entry);
  }
  
  private <Model, Data> ModelLoader<Model, Data> build(Entry<?, ?> paramEntry) {
    return (ModelLoader<Model, Data>)Preconditions.checkNotNull(paramEntry.factory.build(this));
  }
  
  private static <Model, Data> ModelLoader<Model, Data> emptyModelLoader() {
    return (ModelLoader)EMPTY_MODEL_LOADER;
  }
  
  private <Model, Data> ModelLoaderFactory<Model, Data> getFactory(Entry<?, ?> paramEntry) {
    return (ModelLoaderFactory)paramEntry.factory;
  }
  
  <Model, Data> void append(Class<Model> paramClass, Class<Data> paramClass1, ModelLoaderFactory<? extends Model, ? extends Data> paramModelLoaderFactory) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: aload_2
    //   5: aload_3
    //   6: iconst_1
    //   7: invokespecial add : (Ljava/lang/Class;Ljava/lang/Class;Lcom/bumptech/glide/load/model/ModelLoaderFactory;Z)V
    //   10: aload_0
    //   11: monitorexit
    //   12: return
    //   13: astore_1
    //   14: aload_0
    //   15: monitorexit
    //   16: aload_1
    //   17: athrow
    // Exception table:
    //   from	to	target	type
    //   2	10	13	finally
  }
  
  public <Model, Data> ModelLoader<Model, Data> build(Class<Model> paramClass, Class<Data> paramClass1) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: astore #4
    //   7: aload #4
    //   9: invokespecial <init> : ()V
    //   12: iconst_0
    //   13: istore_3
    //   14: aload_0
    //   15: getfield entries : Ljava/util/List;
    //   18: invokeinterface iterator : ()Ljava/util/Iterator;
    //   23: astore #5
    //   25: aload #5
    //   27: invokeinterface hasNext : ()Z
    //   32: ifeq -> 117
    //   35: aload #5
    //   37: invokeinterface next : ()Ljava/lang/Object;
    //   42: checkcast com/bumptech/glide/load/model/MultiModelLoaderFactory$Entry
    //   45: astore #6
    //   47: aload_0
    //   48: getfield alreadyUsedEntries : Ljava/util/Set;
    //   51: aload #6
    //   53: invokeinterface contains : (Ljava/lang/Object;)Z
    //   58: ifeq -> 66
    //   61: iconst_1
    //   62: istore_3
    //   63: goto -> 25
    //   66: aload #6
    //   68: aload_1
    //   69: aload_2
    //   70: invokevirtual handles : (Ljava/lang/Class;Ljava/lang/Class;)Z
    //   73: ifeq -> 114
    //   76: aload_0
    //   77: getfield alreadyUsedEntries : Ljava/util/Set;
    //   80: aload #6
    //   82: invokeinterface add : (Ljava/lang/Object;)Z
    //   87: pop
    //   88: aload #4
    //   90: aload_0
    //   91: aload #6
    //   93: invokespecial build : (Lcom/bumptech/glide/load/model/MultiModelLoaderFactory$Entry;)Lcom/bumptech/glide/load/model/ModelLoader;
    //   96: invokeinterface add : (Ljava/lang/Object;)Z
    //   101: pop
    //   102: aload_0
    //   103: getfield alreadyUsedEntries : Ljava/util/Set;
    //   106: aload #6
    //   108: invokeinterface remove : (Ljava/lang/Object;)Z
    //   113: pop
    //   114: goto -> 25
    //   117: aload #4
    //   119: invokeinterface size : ()I
    //   124: iconst_1
    //   125: if_icmple -> 146
    //   128: aload_0
    //   129: getfield factory : Lcom/bumptech/glide/load/model/MultiModelLoaderFactory$Factory;
    //   132: aload #4
    //   134: aload_0
    //   135: getfield throwableListPool : Landroidx/core/util/Pools$Pool;
    //   138: invokevirtual build : (Ljava/util/List;Landroidx/core/util/Pools$Pool;)Lcom/bumptech/glide/load/model/MultiModelLoader;
    //   141: astore_1
    //   142: aload_0
    //   143: monitorexit
    //   144: aload_1
    //   145: areturn
    //   146: aload #4
    //   148: invokeinterface size : ()I
    //   153: iconst_1
    //   154: if_icmpne -> 173
    //   157: aload #4
    //   159: iconst_0
    //   160: invokeinterface get : (I)Ljava/lang/Object;
    //   165: checkcast com/bumptech/glide/load/model/ModelLoader
    //   168: astore_1
    //   169: aload_0
    //   170: monitorexit
    //   171: aload_1
    //   172: areturn
    //   173: iload_3
    //   174: ifeq -> 185
    //   177: invokestatic emptyModelLoader : ()Lcom/bumptech/glide/load/model/ModelLoader;
    //   180: astore_1
    //   181: aload_0
    //   182: monitorexit
    //   183: aload_1
    //   184: areturn
    //   185: new com/bumptech/glide/Registry$NoModelLoaderAvailableException
    //   188: astore #4
    //   190: aload #4
    //   192: aload_1
    //   193: aload_2
    //   194: invokespecial <init> : (Ljava/lang/Class;Ljava/lang/Class;)V
    //   197: aload #4
    //   199: athrow
    //   200: astore_1
    //   201: aload_0
    //   202: getfield alreadyUsedEntries : Ljava/util/Set;
    //   205: invokeinterface clear : ()V
    //   210: aload_1
    //   211: athrow
    //   212: astore_1
    //   213: aload_0
    //   214: monitorexit
    //   215: aload_1
    //   216: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	200	finally
    //   14	25	200	finally
    //   25	61	200	finally
    //   66	114	200	finally
    //   117	142	200	finally
    //   146	169	200	finally
    //   177	181	200	finally
    //   185	200	200	finally
    //   201	212	212	finally
  }
  
  <Model> List<ModelLoader<Model, ?>> build(Class<Model> paramClass) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: astore #4
    //   7: aload #4
    //   9: invokespecial <init> : ()V
    //   12: aload_0
    //   13: getfield entries : Ljava/util/List;
    //   16: invokeinterface iterator : ()Ljava/util/Iterator;
    //   21: astore_2
    //   22: aload_2
    //   23: invokeinterface hasNext : ()Z
    //   28: ifeq -> 103
    //   31: aload_2
    //   32: invokeinterface next : ()Ljava/lang/Object;
    //   37: checkcast com/bumptech/glide/load/model/MultiModelLoaderFactory$Entry
    //   40: astore_3
    //   41: aload_0
    //   42: getfield alreadyUsedEntries : Ljava/util/Set;
    //   45: aload_3
    //   46: invokeinterface contains : (Ljava/lang/Object;)Z
    //   51: ifeq -> 57
    //   54: goto -> 22
    //   57: aload_3
    //   58: aload_1
    //   59: invokevirtual handles : (Ljava/lang/Class;)Z
    //   62: ifeq -> 100
    //   65: aload_0
    //   66: getfield alreadyUsedEntries : Ljava/util/Set;
    //   69: aload_3
    //   70: invokeinterface add : (Ljava/lang/Object;)Z
    //   75: pop
    //   76: aload #4
    //   78: aload_0
    //   79: aload_3
    //   80: invokespecial build : (Lcom/bumptech/glide/load/model/MultiModelLoaderFactory$Entry;)Lcom/bumptech/glide/load/model/ModelLoader;
    //   83: invokeinterface add : (Ljava/lang/Object;)Z
    //   88: pop
    //   89: aload_0
    //   90: getfield alreadyUsedEntries : Ljava/util/Set;
    //   93: aload_3
    //   94: invokeinterface remove : (Ljava/lang/Object;)Z
    //   99: pop
    //   100: goto -> 22
    //   103: aload_0
    //   104: monitorexit
    //   105: aload #4
    //   107: areturn
    //   108: astore_1
    //   109: aload_0
    //   110: getfield alreadyUsedEntries : Ljava/util/Set;
    //   113: invokeinterface clear : ()V
    //   118: aload_1
    //   119: athrow
    //   120: astore_1
    //   121: aload_0
    //   122: monitorexit
    //   123: aload_1
    //   124: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	108	finally
    //   22	54	108	finally
    //   57	100	108	finally
    //   109	120	120	finally
  }
  
  List<Class<?>> getDataClasses(Class<?> paramClass) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: astore #4
    //   7: aload #4
    //   9: invokespecial <init> : ()V
    //   12: aload_0
    //   13: getfield entries : Ljava/util/List;
    //   16: invokeinterface iterator : ()Ljava/util/Iterator;
    //   21: astore_3
    //   22: aload_3
    //   23: invokeinterface hasNext : ()Z
    //   28: ifeq -> 78
    //   31: aload_3
    //   32: invokeinterface next : ()Ljava/lang/Object;
    //   37: checkcast com/bumptech/glide/load/model/MultiModelLoaderFactory$Entry
    //   40: astore_2
    //   41: aload #4
    //   43: aload_2
    //   44: getfield dataClass : Ljava/lang/Class;
    //   47: invokeinterface contains : (Ljava/lang/Object;)Z
    //   52: ifne -> 75
    //   55: aload_2
    //   56: aload_1
    //   57: invokevirtual handles : (Ljava/lang/Class;)Z
    //   60: ifeq -> 75
    //   63: aload #4
    //   65: aload_2
    //   66: getfield dataClass : Ljava/lang/Class;
    //   69: invokeinterface add : (Ljava/lang/Object;)Z
    //   74: pop
    //   75: goto -> 22
    //   78: aload_0
    //   79: monitorexit
    //   80: aload #4
    //   82: areturn
    //   83: astore_1
    //   84: aload_0
    //   85: monitorexit
    //   86: aload_1
    //   87: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	83	finally
    //   22	75	83	finally
  }
  
  <Model, Data> void prepend(Class<Model> paramClass, Class<Data> paramClass1, ModelLoaderFactory<? extends Model, ? extends Data> paramModelLoaderFactory) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: aload_2
    //   5: aload_3
    //   6: iconst_0
    //   7: invokespecial add : (Ljava/lang/Class;Ljava/lang/Class;Lcom/bumptech/glide/load/model/ModelLoaderFactory;Z)V
    //   10: aload_0
    //   11: monitorexit
    //   12: return
    //   13: astore_1
    //   14: aload_0
    //   15: monitorexit
    //   16: aload_1
    //   17: athrow
    // Exception table:
    //   from	to	target	type
    //   2	10	13	finally
  }
  
  <Model, Data> List<ModelLoaderFactory<? extends Model, ? extends Data>> remove(Class<Model> paramClass, Class<Data> paramClass1) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: astore #5
    //   7: aload #5
    //   9: invokespecial <init> : ()V
    //   12: aload_0
    //   13: getfield entries : Ljava/util/List;
    //   16: invokeinterface iterator : ()Ljava/util/Iterator;
    //   21: astore_3
    //   22: aload_3
    //   23: invokeinterface hasNext : ()Z
    //   28: ifeq -> 75
    //   31: aload_3
    //   32: invokeinterface next : ()Ljava/lang/Object;
    //   37: checkcast com/bumptech/glide/load/model/MultiModelLoaderFactory$Entry
    //   40: astore #4
    //   42: aload #4
    //   44: aload_1
    //   45: aload_2
    //   46: invokevirtual handles : (Ljava/lang/Class;Ljava/lang/Class;)Z
    //   49: ifeq -> 72
    //   52: aload_3
    //   53: invokeinterface remove : ()V
    //   58: aload #5
    //   60: aload_0
    //   61: aload #4
    //   63: invokespecial getFactory : (Lcom/bumptech/glide/load/model/MultiModelLoaderFactory$Entry;)Lcom/bumptech/glide/load/model/ModelLoaderFactory;
    //   66: invokeinterface add : (Ljava/lang/Object;)Z
    //   71: pop
    //   72: goto -> 22
    //   75: aload_0
    //   76: monitorexit
    //   77: aload #5
    //   79: areturn
    //   80: astore_1
    //   81: aload_0
    //   82: monitorexit
    //   83: aload_1
    //   84: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	80	finally
    //   22	72	80	finally
  }
  
  <Model, Data> List<ModelLoaderFactory<? extends Model, ? extends Data>> replace(Class<Model> paramClass, Class<Data> paramClass1, ModelLoaderFactory<? extends Model, ? extends Data> paramModelLoaderFactory) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: aload_2
    //   5: invokevirtual remove : (Ljava/lang/Class;Ljava/lang/Class;)Ljava/util/List;
    //   8: astore #4
    //   10: aload_0
    //   11: aload_1
    //   12: aload_2
    //   13: aload_3
    //   14: invokevirtual append : (Ljava/lang/Class;Ljava/lang/Class;Lcom/bumptech/glide/load/model/ModelLoaderFactory;)V
    //   17: aload_0
    //   18: monitorexit
    //   19: aload #4
    //   21: areturn
    //   22: astore_1
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_1
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	22	finally
  }
  
  private static class EmptyModelLoader implements ModelLoader<Object, Object> {
    public ModelLoader.LoadData<Object> buildLoadData(Object param1Object, int param1Int1, int param1Int2, Options param1Options) {
      return null;
    }
    
    public boolean handles(Object param1Object) {
      return false;
    }
  }
  
  private static class Entry<Model, Data> {
    final Class<Data> dataClass;
    
    final ModelLoaderFactory<? extends Model, ? extends Data> factory;
    
    private final Class<Model> modelClass;
    
    public Entry(Class<Model> param1Class, Class<Data> param1Class1, ModelLoaderFactory<? extends Model, ? extends Data> param1ModelLoaderFactory) {
      this.modelClass = param1Class;
      this.dataClass = param1Class1;
      this.factory = param1ModelLoaderFactory;
    }
    
    public boolean handles(Class<?> param1Class) {
      return this.modelClass.isAssignableFrom(param1Class);
    }
    
    public boolean handles(Class<?> param1Class1, Class<?> param1Class2) {
      boolean bool;
      if (handles(param1Class1) && this.dataClass.isAssignableFrom(param1Class2)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
  }
  
  static class Factory {
    public <Model, Data> MultiModelLoader<Model, Data> build(List<ModelLoader<Model, Data>> param1List, Pools.Pool<List<Throwable>> param1Pool) {
      return new MultiModelLoader<>(param1List, param1Pool);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\model\MultiModelLoaderFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */