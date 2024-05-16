package androidx.versionedparcelable;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcelable;
import androidx.collection.ArrayMap;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Set;

class VersionedParcelStream extends VersionedParcel {
  private static final int TYPE_BOOLEAN = 5;
  
  private static final int TYPE_BOOLEAN_ARRAY = 6;
  
  private static final int TYPE_DOUBLE = 7;
  
  private static final int TYPE_DOUBLE_ARRAY = 8;
  
  private static final int TYPE_FLOAT = 13;
  
  private static final int TYPE_FLOAT_ARRAY = 14;
  
  private static final int TYPE_INT = 9;
  
  private static final int TYPE_INT_ARRAY = 10;
  
  private static final int TYPE_LONG = 11;
  
  private static final int TYPE_LONG_ARRAY = 12;
  
  private static final int TYPE_NULL = 0;
  
  private static final int TYPE_STRING = 3;
  
  private static final int TYPE_STRING_ARRAY = 4;
  
  private static final int TYPE_SUB_BUNDLE = 1;
  
  private static final int TYPE_SUB_PERSISTABLE_BUNDLE = 2;
  
  private static final Charset UTF_16 = Charset.forName("UTF-16");
  
  int mCount;
  
  private DataInputStream mCurrentInput;
  
  private DataOutputStream mCurrentOutput;
  
  private FieldBuffer mFieldBuffer;
  
  private int mFieldId;
  
  int mFieldSize;
  
  private boolean mIgnoreParcelables;
  
  private final DataInputStream mMasterInput;
  
  private final DataOutputStream mMasterOutput;
  
  public VersionedParcelStream(InputStream paramInputStream, OutputStream paramOutputStream) {
    this(paramInputStream, paramOutputStream, new ArrayMap(), new ArrayMap(), new ArrayMap());
  }
  
  private VersionedParcelStream(InputStream paramInputStream, OutputStream paramOutputStream, ArrayMap<String, Method> paramArrayMap1, ArrayMap<String, Method> paramArrayMap2, ArrayMap<String, Class> paramArrayMap) {
    super(paramArrayMap1, paramArrayMap2, paramArrayMap);
    DataOutputStream dataOutputStream;
    this.mCount = 0;
    this.mFieldId = -1;
    this.mFieldSize = -1;
    paramArrayMap1 = null;
    if (paramInputStream != null) {
      paramInputStream = new DataInputStream(new FilterInputStream(paramInputStream) {
            final VersionedParcelStream this$0;
            
            public int read() throws IOException {
              if (VersionedParcelStream.this.mFieldSize == -1 || VersionedParcelStream.this.mCount < VersionedParcelStream.this.mFieldSize) {
                int i = super.read();
                VersionedParcelStream versionedParcelStream = VersionedParcelStream.this;
                versionedParcelStream.mCount++;
                return i;
              } 
              throw new IOException();
            }
            
            public int read(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
              if (VersionedParcelStream.this.mFieldSize == -1 || VersionedParcelStream.this.mCount < VersionedParcelStream.this.mFieldSize) {
                param1Int1 = super.read(param1ArrayOfbyte, param1Int1, param1Int2);
                if (param1Int1 > 0) {
                  VersionedParcelStream versionedParcelStream = VersionedParcelStream.this;
                  versionedParcelStream.mCount += param1Int1;
                } 
                return param1Int1;
              } 
              throw new IOException();
            }
            
            public long skip(long param1Long) throws IOException {
              if (VersionedParcelStream.this.mFieldSize == -1 || VersionedParcelStream.this.mCount < VersionedParcelStream.this.mFieldSize) {
                param1Long = super.skip(param1Long);
                if (param1Long > 0L) {
                  VersionedParcelStream versionedParcelStream = VersionedParcelStream.this;
                  versionedParcelStream.mCount += (int)param1Long;
                } 
                return param1Long;
              } 
              throw new IOException();
            }
          });
    } else {
      paramInputStream = null;
    } 
    this.mMasterInput = (DataInputStream)paramInputStream;
    if (paramOutputStream != null)
      dataOutputStream = new DataOutputStream(paramOutputStream); 
    this.mMasterOutput = dataOutputStream;
    this.mCurrentInput = (DataInputStream)paramInputStream;
    this.mCurrentOutput = dataOutputStream;
  }
  
  private void readObject(int paramInt, String paramString, Bundle paramBundle) {
    switch (paramInt) {
      default:
        throw new RuntimeException("Unknown type " + paramInt);
      case 14:
        paramBundle.putFloatArray(paramString, readFloatArray());
        return;
      case 13:
        paramBundle.putFloat(paramString, readFloat());
        return;
      case 12:
        paramBundle.putLongArray(paramString, readLongArray());
        return;
      case 11:
        paramBundle.putLong(paramString, readLong());
        return;
      case 10:
        paramBundle.putIntArray(paramString, readIntArray());
        return;
      case 9:
        paramBundle.putInt(paramString, readInt());
        return;
      case 8:
        paramBundle.putDoubleArray(paramString, readDoubleArray());
        return;
      case 7:
        paramBundle.putDouble(paramString, readDouble());
        return;
      case 6:
        paramBundle.putBooleanArray(paramString, readBooleanArray());
        return;
      case 5:
        paramBundle.putBoolean(paramString, readBoolean());
        return;
      case 4:
        paramBundle.putStringArray(paramString, readArray(new String[0]));
        return;
      case 3:
        paramBundle.putString(paramString, readString());
        return;
      case 2:
        paramBundle.putBundle(paramString, readBundle());
        return;
      case 1:
        paramBundle.putBundle(paramString, readBundle());
        return;
      case 0:
        break;
    } 
    paramBundle.putParcelable(paramString, null);
  }
  
  private void writeObject(Object paramObject) {
    if (paramObject == null) {
      writeInt(0);
    } else if (paramObject instanceof Bundle) {
      writeInt(1);
      writeBundle((Bundle)paramObject);
    } else if (paramObject instanceof String) {
      writeInt(3);
      writeString((String)paramObject);
    } else if (paramObject instanceof String[]) {
      writeInt(4);
      writeArray((String[])paramObject);
    } else if (paramObject instanceof Boolean) {
      writeInt(5);
      writeBoolean(((Boolean)paramObject).booleanValue());
    } else if (paramObject instanceof boolean[]) {
      writeInt(6);
      writeBooleanArray((boolean[])paramObject);
    } else if (paramObject instanceof Double) {
      writeInt(7);
      writeDouble(((Double)paramObject).doubleValue());
    } else if (paramObject instanceof double[]) {
      writeInt(8);
      writeDoubleArray((double[])paramObject);
    } else if (paramObject instanceof Integer) {
      writeInt(9);
      writeInt(((Integer)paramObject).intValue());
    } else if (paramObject instanceof int[]) {
      writeInt(10);
      writeIntArray((int[])paramObject);
    } else if (paramObject instanceof Long) {
      writeInt(11);
      writeLong(((Long)paramObject).longValue());
    } else if (paramObject instanceof long[]) {
      writeInt(12);
      writeLongArray((long[])paramObject);
    } else if (paramObject instanceof Float) {
      writeInt(13);
      writeFloat(((Float)paramObject).floatValue());
    } else {
      if (paramObject instanceof float[]) {
        writeInt(14);
        writeFloatArray((float[])paramObject);
        return;
      } 
      throw new IllegalArgumentException("Unsupported type " + paramObject.getClass());
    } 
  }
  
  public void closeField() {
    FieldBuffer fieldBuffer = this.mFieldBuffer;
    if (fieldBuffer != null)
      try {
        if (fieldBuffer.mOutput.size() != 0)
          this.mFieldBuffer.flushField(); 
        this.mFieldBuffer = null;
      } catch (IOException iOException) {
        throw new VersionedParcel.ParcelException(iOException);
      }  
  }
  
  protected VersionedParcel createSubParcel() {
    return new VersionedParcelStream(this.mCurrentInput, this.mCurrentOutput, this.mReadCache, this.mWriteCache, this.mParcelizerCache);
  }
  
  public boolean isStream() {
    return true;
  }
  
  public boolean readBoolean() {
    try {
      return this.mCurrentInput.readBoolean();
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public Bundle readBundle() {
    int i = readInt();
    if (i < 0)
      return null; 
    Bundle bundle = new Bundle();
    for (byte b = 0; b < i; b++) {
      String str = readString();
      readObject(readInt(), str, bundle);
    } 
    return bundle;
  }
  
  public byte[] readByteArray() {
    try {
      int i = this.mCurrentInput.readInt();
      if (i > 0) {
        byte[] arrayOfByte = new byte[i];
        this.mCurrentInput.readFully(arrayOfByte);
        return arrayOfByte;
      } 
      return null;
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  protected CharSequence readCharSequence() {
    return null;
  }
  
  public double readDouble() {
    try {
      return this.mCurrentInput.readDouble();
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public boolean readField(int paramInt) {
    try {
      while (true) {
        int i = this.mFieldId;
        if (i == paramInt)
          return true; 
        if (String.valueOf(i).compareTo(String.valueOf(paramInt)) > 0)
          return false; 
        i = this.mCount;
        int j = this.mFieldSize;
        if (i < j)
          this.mMasterInput.skip((j - i)); 
        this.mFieldSize = -1;
        int k = this.mMasterInput.readInt();
        this.mCount = 0;
        j = k & 0xFFFF;
        i = j;
        if (j == 65535)
          i = this.mMasterInput.readInt(); 
        this.mFieldId = 0xFFFF & k >> 16;
        this.mFieldSize = i;
      } 
    } catch (IOException iOException) {
      return false;
    } 
  }
  
  public float readFloat() {
    try {
      return this.mCurrentInput.readFloat();
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public int readInt() {
    try {
      return this.mCurrentInput.readInt();
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public long readLong() {
    try {
      return this.mCurrentInput.readLong();
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public <T extends Parcelable> T readParcelable() {
    return null;
  }
  
  public String readString() {
    try {
      int i = this.mCurrentInput.readInt();
      if (i > 0) {
        byte[] arrayOfByte = new byte[i];
        this.mCurrentInput.readFully(arrayOfByte);
        return new String(arrayOfByte, UTF_16);
      } 
      return null;
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public IBinder readStrongBinder() {
    return null;
  }
  
  public void setOutputField(int paramInt) {
    closeField();
    FieldBuffer fieldBuffer = new FieldBuffer(paramInt, this.mMasterOutput);
    this.mFieldBuffer = fieldBuffer;
    this.mCurrentOutput = fieldBuffer.mDataStream;
  }
  
  public void setSerializationFlags(boolean paramBoolean1, boolean paramBoolean2) {
    if (paramBoolean1) {
      this.mIgnoreParcelables = paramBoolean2;
      return;
    } 
    throw new RuntimeException("Serialization of this object is not allowed");
  }
  
  public void writeBoolean(boolean paramBoolean) {
    try {
      this.mCurrentOutput.writeBoolean(paramBoolean);
      return;
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public void writeBundle(Bundle paramBundle) {
    if (paramBundle != null)
      try {
        Set set = paramBundle.keySet();
        this.mCurrentOutput.writeInt(set.size());
        for (String str : set) {
          writeString(str);
          writeObject(paramBundle.get(str));
        } 
        return;
      } catch (IOException iOException) {
        throw new VersionedParcel.ParcelException(iOException);
      }  
    this.mCurrentOutput.writeInt(-1);
  }
  
  public void writeByteArray(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte != null)
      try {
        this.mCurrentOutput.writeInt(paramArrayOfbyte.length);
        this.mCurrentOutput.write(paramArrayOfbyte);
        return;
      } catch (IOException iOException) {
        throw new VersionedParcel.ParcelException(iOException);
      }  
    this.mCurrentOutput.writeInt(-1);
  }
  
  public void writeByteArray(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    if (paramArrayOfbyte != null)
      try {
        this.mCurrentOutput.writeInt(paramInt2);
        this.mCurrentOutput.write(paramArrayOfbyte, paramInt1, paramInt2);
        return;
      } catch (IOException iOException) {
        throw new VersionedParcel.ParcelException(iOException);
      }  
    this.mCurrentOutput.writeInt(-1);
  }
  
  protected void writeCharSequence(CharSequence paramCharSequence) {
    if (this.mIgnoreParcelables)
      return; 
    throw new RuntimeException("CharSequence cannot be written to an OutputStream");
  }
  
  public void writeDouble(double paramDouble) {
    try {
      this.mCurrentOutput.writeDouble(paramDouble);
      return;
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public void writeFloat(float paramFloat) {
    try {
      this.mCurrentOutput.writeFloat(paramFloat);
      return;
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public void writeInt(int paramInt) {
    try {
      this.mCurrentOutput.writeInt(paramInt);
      return;
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public void writeLong(long paramLong) {
    try {
      this.mCurrentOutput.writeLong(paramLong);
      return;
    } catch (IOException iOException) {
      throw new VersionedParcel.ParcelException(iOException);
    } 
  }
  
  public void writeParcelable(Parcelable paramParcelable) {
    if (this.mIgnoreParcelables)
      return; 
    throw new RuntimeException("Parcelables cannot be written to an OutputStream");
  }
  
  public void writeString(String paramString) {
    if (paramString != null)
      try {
        byte[] arrayOfByte = paramString.getBytes(UTF_16);
        this.mCurrentOutput.writeInt(arrayOfByte.length);
        this.mCurrentOutput.write(arrayOfByte);
        return;
      } catch (IOException iOException) {
        throw new VersionedParcel.ParcelException(iOException);
      }  
    this.mCurrentOutput.writeInt(-1);
  }
  
  public void writeStrongBinder(IBinder paramIBinder) {
    if (this.mIgnoreParcelables)
      return; 
    throw new RuntimeException("Binders cannot be written to an OutputStream");
  }
  
  public void writeStrongInterface(IInterface paramIInterface) {
    if (this.mIgnoreParcelables)
      return; 
    throw new RuntimeException("Binders cannot be written to an OutputStream");
  }
  
  private static class FieldBuffer {
    final DataOutputStream mDataStream;
    
    private final int mFieldId;
    
    final ByteArrayOutputStream mOutput;
    
    private final DataOutputStream mTarget;
    
    FieldBuffer(int param1Int, DataOutputStream param1DataOutputStream) {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      this.mOutput = byteArrayOutputStream;
      this.mDataStream = new DataOutputStream(byteArrayOutputStream);
      this.mFieldId = param1Int;
      this.mTarget = param1DataOutputStream;
    }
    
    void flushField() throws IOException {
      int i;
      this.mDataStream.flush();
      int j = this.mOutput.size();
      int k = this.mFieldId;
      if (j >= 65535) {
        i = 65535;
      } else {
        i = j;
      } 
      this.mTarget.writeInt(k << 16 | i);
      if (j >= 65535)
        this.mTarget.writeInt(j); 
      this.mOutput.writeTo(this.mTarget);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\versionedparcelable\VersionedParcelStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */