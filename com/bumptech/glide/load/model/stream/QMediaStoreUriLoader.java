package com.bumptech.glide.load.model.stream;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.mediastore.MediaStoreUtil;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public final class QMediaStoreUriLoader<DataT> implements ModelLoader<Uri, DataT> {
  private final Context context;
  
  private final Class<DataT> dataClass;
  
  private final ModelLoader<File, DataT> fileDelegate;
  
  private final ModelLoader<Uri, DataT> uriDelegate;
  
  QMediaStoreUriLoader(Context paramContext, ModelLoader<File, DataT> paramModelLoader, ModelLoader<Uri, DataT> paramModelLoader1, Class<DataT> paramClass) {
    this.context = paramContext.getApplicationContext();
    this.fileDelegate = paramModelLoader;
    this.uriDelegate = paramModelLoader1;
    this.dataClass = paramClass;
  }
  
  public ModelLoader.LoadData<DataT> buildLoadData(Uri paramUri, int paramInt1, int paramInt2, Options paramOptions) {
    return new ModelLoader.LoadData((Key)new ObjectKey(paramUri), new QMediaStoreUriFetcher<>(this.context, this.fileDelegate, this.uriDelegate, paramUri, paramInt1, paramInt2, paramOptions, this.dataClass));
  }
  
  public boolean handles(Uri paramUri) {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 29 && MediaStoreUtil.isMediaStoreUri(paramUri)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static abstract class Factory<DataT> implements ModelLoaderFactory<Uri, DataT> {
    private final Context context;
    
    private final Class<DataT> dataClass;
    
    Factory(Context param1Context, Class<DataT> param1Class) {
      this.context = param1Context;
      this.dataClass = param1Class;
    }
    
    public final ModelLoader<Uri, DataT> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new QMediaStoreUriLoader<>(this.context, param1MultiModelLoaderFactory.build(File.class, this.dataClass), param1MultiModelLoaderFactory.build(Uri.class, this.dataClass), this.dataClass);
    }
    
    public final void teardown() {}
  }
  
  public static final class FileDescriptorFactory extends Factory<ParcelFileDescriptor> {
    public FileDescriptorFactory(Context param1Context) {
      super(param1Context, ParcelFileDescriptor.class);
    }
  }
  
  public static final class InputStreamFactory extends Factory<InputStream> {
    public InputStreamFactory(Context param1Context) {
      super(param1Context, InputStream.class);
    }
  }
  
  private static final class QMediaStoreUriFetcher<DataT> implements DataFetcher<DataT> {
    private static final String[] PROJECTION = new String[] { "_data" };
    
    private final Context context;
    
    private final Class<DataT> dataClass;
    
    private volatile DataFetcher<DataT> delegate;
    
    private final ModelLoader<File, DataT> fileDelegate;
    
    private final int height;
    
    private volatile boolean isCancelled;
    
    private final Options options;
    
    private final Uri uri;
    
    private final ModelLoader<Uri, DataT> uriDelegate;
    
    private final int width;
    
    QMediaStoreUriFetcher(Context param1Context, ModelLoader<File, DataT> param1ModelLoader, ModelLoader<Uri, DataT> param1ModelLoader1, Uri param1Uri, int param1Int1, int param1Int2, Options param1Options, Class<DataT> param1Class) {
      this.context = param1Context.getApplicationContext();
      this.fileDelegate = param1ModelLoader;
      this.uriDelegate = param1ModelLoader1;
      this.uri = param1Uri;
      this.width = param1Int1;
      this.height = param1Int2;
      this.options = param1Options;
      this.dataClass = param1Class;
    }
    
    private ModelLoader.LoadData<DataT> buildDelegateData() throws FileNotFoundException {
      Uri uri;
      if (Environment.isExternalStorageLegacy())
        return this.fileDelegate.buildLoadData(queryForFilePath(this.uri), this.width, this.height, this.options); 
      if (isAccessMediaLocationGranted()) {
        uri = MediaStore.setRequireOriginal(this.uri);
      } else {
        uri = this.uri;
      } 
      return this.uriDelegate.buildLoadData(uri, this.width, this.height, this.options);
    }
    
    private DataFetcher<DataT> buildDelegateFetcher() throws FileNotFoundException {
      ModelLoader.LoadData<DataT> loadData = buildDelegateData();
      if (loadData != null) {
        DataFetcher dataFetcher = loadData.fetcher;
      } else {
        loadData = null;
      } 
      return (DataFetcher<DataT>)loadData;
    }
    
    private boolean isAccessMediaLocationGranted() {
      boolean bool;
      if (this.context.checkSelfPermission("android.permission.ACCESS_MEDIA_LOCATION") == 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    private File queryForFilePath(Uri param1Uri) throws FileNotFoundException {
      Cursor cursor = null;
      try {
        File file;
        Cursor cursor1 = this.context.getContentResolver().query(param1Uri, PROJECTION, null, null, null);
        if (cursor1 != null) {
          cursor = cursor1;
          if (cursor1.moveToFirst()) {
            cursor = cursor1;
            String str = cursor1.getString(cursor1.getColumnIndexOrThrow("_data"));
            cursor = cursor1;
            if (!TextUtils.isEmpty(str)) {
              cursor = cursor1;
              file = new File(str);
              return file;
            } 
            cursor = cursor1;
            FileNotFoundException fileNotFoundException1 = new FileNotFoundException();
            cursor = cursor1;
            StringBuilder stringBuilder1 = new StringBuilder();
            cursor = cursor1;
            this();
            cursor = cursor1;
            this(stringBuilder1.append("File path was empty in media store for: ").append(file).toString());
            cursor = cursor1;
            throw fileNotFoundException1;
          } 
        } 
        cursor = cursor1;
        FileNotFoundException fileNotFoundException = new FileNotFoundException();
        cursor = cursor1;
        StringBuilder stringBuilder = new StringBuilder();
        cursor = cursor1;
        this();
        cursor = cursor1;
        this(stringBuilder.append("Failed to media store entry for: ").append(file).toString());
        cursor = cursor1;
        throw fileNotFoundException;
      } finally {
        if (cursor != null)
          cursor.close(); 
      } 
    }
    
    public void cancel() {
      this.isCancelled = true;
      DataFetcher<DataT> dataFetcher = this.delegate;
      if (dataFetcher != null)
        dataFetcher.cancel(); 
    }
    
    public void cleanup() {
      DataFetcher<DataT> dataFetcher = this.delegate;
      if (dataFetcher != null)
        dataFetcher.cleanup(); 
    }
    
    public Class<DataT> getDataClass() {
      return this.dataClass;
    }
    
    public DataSource getDataSource() {
      return DataSource.LOCAL;
    }
    
    public void loadData(Priority param1Priority, DataFetcher.DataCallback<? super DataT> param1DataCallback) {
      try {
        StringBuilder stringBuilder;
        IllegalArgumentException illegalArgumentException;
        DataFetcher<DataT> dataFetcher = buildDelegateFetcher();
        if (dataFetcher == null) {
          illegalArgumentException = new IllegalArgumentException();
          stringBuilder = new StringBuilder();
          this();
          this(stringBuilder.append("Failed to build fetcher for: ").append(this.uri).toString());
          param1DataCallback.onLoadFailed(illegalArgumentException);
          return;
        } 
        this.delegate = (DataFetcher<DataT>)illegalArgumentException;
        if (this.isCancelled) {
          cancel();
        } else {
          illegalArgumentException.loadData((Priority)stringBuilder, param1DataCallback);
        } 
      } catch (FileNotFoundException fileNotFoundException) {
        param1DataCallback.onLoadFailed(fileNotFoundException);
      } 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\model\stream\QMediaStoreUriLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */