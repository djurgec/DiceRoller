package androidx.core.net;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;

class DatagramSocketWrapper extends Socket {
  DatagramSocketWrapper(DatagramSocket paramDatagramSocket, FileDescriptor paramFileDescriptor) throws SocketException {
    super(new DatagramSocketImplWrapper(paramDatagramSocket, paramFileDescriptor));
  }
  
  private static class DatagramSocketImplWrapper extends SocketImpl {
    DatagramSocketImplWrapper(DatagramSocket param1DatagramSocket, FileDescriptor param1FileDescriptor) {
      this.localport = param1DatagramSocket.getLocalPort();
      this.fd = param1FileDescriptor;
    }
    
    protected void accept(SocketImpl param1SocketImpl) throws IOException {
      throw new UnsupportedOperationException();
    }
    
    protected int available() throws IOException {
      throw new UnsupportedOperationException();
    }
    
    protected void bind(InetAddress param1InetAddress, int param1Int) throws IOException {
      throw new UnsupportedOperationException();
    }
    
    protected void close() throws IOException {
      throw new UnsupportedOperationException();
    }
    
    protected void connect(String param1String, int param1Int) throws IOException {
      throw new UnsupportedOperationException();
    }
    
    protected void connect(InetAddress param1InetAddress, int param1Int) throws IOException {
      throw new UnsupportedOperationException();
    }
    
    protected void connect(SocketAddress param1SocketAddress, int param1Int) throws IOException {
      throw new UnsupportedOperationException();
    }
    
    protected void create(boolean param1Boolean) throws IOException {
      throw new UnsupportedOperationException();
    }
    
    protected InputStream getInputStream() throws IOException {
      throw new UnsupportedOperationException();
    }
    
    public Object getOption(int param1Int) throws SocketException {
      throw new UnsupportedOperationException();
    }
    
    protected OutputStream getOutputStream() throws IOException {
      throw new UnsupportedOperationException();
    }
    
    protected void listen(int param1Int) throws IOException {
      throw new UnsupportedOperationException();
    }
    
    protected void sendUrgentData(int param1Int) throws IOException {
      throw new UnsupportedOperationException();
    }
    
    public void setOption(int param1Int, Object param1Object) throws SocketException {
      throw new UnsupportedOperationException();
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\net\DatagramSocketWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */