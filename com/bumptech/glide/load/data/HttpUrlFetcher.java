package com.bumptech.glide.load.data;

import android.text.TextUtils;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.HttpException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;
import com.bumptech.glide.util.LogTime;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

public class HttpUrlFetcher implements DataFetcher<InputStream> {
  static final HttpUrlConnectionFactory DEFAULT_CONNECTION_FACTORY = new DefaultHttpUrlConnectionFactory();
  
  static final int INVALID_STATUS_CODE = -1;
  
  private static final int MAXIMUM_REDIRECTS = 5;
  
  static final String REDIRECT_HEADER_FIELD = "Location";
  
  private static final String TAG = "HttpUrlFetcher";
  
  private final HttpUrlConnectionFactory connectionFactory;
  
  private final GlideUrl glideUrl;
  
  private volatile boolean isCancelled;
  
  private InputStream stream;
  
  private final int timeout;
  
  private HttpURLConnection urlConnection;
  
  public HttpUrlFetcher(GlideUrl paramGlideUrl, int paramInt) {
    this(paramGlideUrl, paramInt, DEFAULT_CONNECTION_FACTORY);
  }
  
  HttpUrlFetcher(GlideUrl paramGlideUrl, int paramInt, HttpUrlConnectionFactory paramHttpUrlConnectionFactory) {
    this.glideUrl = paramGlideUrl;
    this.timeout = paramInt;
    this.connectionFactory = paramHttpUrlConnectionFactory;
  }
  
  private HttpURLConnection buildAndConfigureConnection(URL paramURL, Map<String, String> paramMap) throws HttpException {
    try {
      HttpURLConnection httpURLConnection = this.connectionFactory.build(paramURL);
      for (Map.Entry<String, String> entry : paramMap.entrySet())
        httpURLConnection.addRequestProperty((String)entry.getKey(), (String)entry.getValue()); 
      httpURLConnection.setConnectTimeout(this.timeout);
      httpURLConnection.setReadTimeout(this.timeout);
      httpURLConnection.setUseCaches(false);
      httpURLConnection.setDoInput(true);
      httpURLConnection.setInstanceFollowRedirects(false);
      return httpURLConnection;
    } catch (IOException iOException) {
      throw new HttpException("URL.openConnection threw", 0, iOException);
    } 
  }
  
  private static int getHttpStatusCodeOrInvalid(HttpURLConnection paramHttpURLConnection) {
    try {
      return paramHttpURLConnection.getResponseCode();
    } catch (IOException iOException) {
      if (Log.isLoggable("HttpUrlFetcher", 3))
        Log.d("HttpUrlFetcher", "Failed to get a response code", iOException); 
      return -1;
    } 
  }
  
  private InputStream getStreamForSuccessfulRequest(HttpURLConnection paramHttpURLConnection) throws HttpException {
    try {
      if (TextUtils.isEmpty(paramHttpURLConnection.getContentEncoding())) {
        int i = paramHttpURLConnection.getContentLength();
        this.stream = ContentLengthInputStream.obtain(paramHttpURLConnection.getInputStream(), i);
      } else {
        if (Log.isLoggable("HttpUrlFetcher", 3)) {
          StringBuilder stringBuilder = new StringBuilder();
          this();
          Log.d("HttpUrlFetcher", stringBuilder.append("Got non empty content encoding: ").append(paramHttpURLConnection.getContentEncoding()).toString());
        } 
        this.stream = paramHttpURLConnection.getInputStream();
      } 
      return this.stream;
    } catch (IOException iOException) {
      throw new HttpException("Failed to obtain InputStream", getHttpStatusCodeOrInvalid(paramHttpURLConnection), iOException);
    } 
  }
  
  private static boolean isHttpOk(int paramInt) {
    boolean bool;
    if (paramInt / 100 == 2) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static boolean isHttpRedirect(int paramInt) {
    boolean bool;
    if (paramInt / 100 == 3) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private InputStream loadDataWithRedirects(URL paramURL1, int paramInt, URL paramURL2, Map<String, String> paramMap) throws HttpException {
    if (paramInt < 5) {
      if (paramURL2 != null)
        try {
          if (paramURL1.toURI().equals(paramURL2.toURI())) {
            HttpException httpException = new HttpException();
            this("In re-direct loop", -1);
            throw httpException;
          } 
        } catch (URISyntaxException uRISyntaxException) {} 
      HttpURLConnection httpURLConnection = buildAndConfigureConnection(paramURL1, paramMap);
      this.urlConnection = httpURLConnection;
      try {
        httpURLConnection.connect();
        this.stream = this.urlConnection.getInputStream();
        if (this.isCancelled)
          return null; 
        int i = getHttpStatusCodeOrInvalid(this.urlConnection);
        if (isHttpOk(i))
          return getStreamForSuccessfulRequest(this.urlConnection); 
        if (isHttpRedirect(i)) {
          String str = this.urlConnection.getHeaderField("Location");
          if (!TextUtils.isEmpty(str))
            try {
              URL uRL = new URL(paramURL1, str);
              cleanup();
              return loadDataWithRedirects(uRL, paramInt + 1, paramURL1, paramMap);
            } catch (MalformedURLException malformedURLException) {
              throw new HttpException("Bad redirect url: " + str, i, malformedURLException);
            }  
          throw new HttpException("Received empty or null redirect url", i);
        } 
        if (i == -1)
          throw new HttpException(i); 
        try {
          HttpException httpException = new HttpException();
          this(this.urlConnection.getResponseMessage(), i);
          throw httpException;
        } catch (IOException iOException) {
          throw new HttpException("Failed to get a response message", i, iOException);
        } 
      } catch (IOException iOException) {
        throw new HttpException("Failed to connect or obtain data", getHttpStatusCodeOrInvalid(this.urlConnection), iOException);
      } 
    } 
    throw new HttpException("Too many (> 5) redirects!", -1);
  }
  
  public void cancel() {
    this.isCancelled = true;
  }
  
  public void cleanup() {
    InputStream inputStream = this.stream;
    if (inputStream != null)
      try {
        inputStream.close();
      } catch (IOException iOException) {} 
    HttpURLConnection httpURLConnection = this.urlConnection;
    if (httpURLConnection != null)
      httpURLConnection.disconnect(); 
    this.urlConnection = null;
  }
  
  public Class<InputStream> getDataClass() {
    return InputStream.class;
  }
  
  public DataSource getDataSource() {
    return DataSource.REMOTE;
  }
  
  public void loadData(Priority paramPriority, DataFetcher.DataCallback<? super InputStream> paramDataCallback) {
    long l = LogTime.getLogTime();
    try {
      paramDataCallback.onDataReady(loadDataWithRedirects(this.glideUrl.toURL(), 0, null, this.glideUrl.getHeaders()));
      if (Log.isLoggable("HttpUrlFetcher", 2)) {
        StringBuilder stringBuilder = new StringBuilder();
      } else {
        return;
      } 
    } catch (IOException iOException) {
      if (Log.isLoggable("HttpUrlFetcher", 3))
        Log.d("HttpUrlFetcher", "Failed to load data for url", iOException); 
      paramDataCallback.onLoadFailed(iOException);
      if (Log.isLoggable("HttpUrlFetcher", 2)) {
        StringBuilder stringBuilder = new StringBuilder();
      } else {
        return;
      } 
    } finally {}
    Log.v("HttpUrlFetcher", paramPriority.append("Finished http url fetcher fetch in ").append(LogTime.getElapsedMillis(l)).toString());
  }
  
  private static class DefaultHttpUrlConnectionFactory implements HttpUrlConnectionFactory {
    public HttpURLConnection build(URL param1URL) throws IOException {
      return (HttpURLConnection)param1URL.openConnection();
    }
  }
  
  static interface HttpUrlConnectionFactory {
    HttpURLConnection build(URL param1URL) throws IOException;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\data\HttpUrlFetcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */