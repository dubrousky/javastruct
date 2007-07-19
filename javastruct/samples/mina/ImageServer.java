package mina;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.SocketAcceptor;

public class ImageServer
{
  public static final int PORT = 33789;

  public static void main(String[] args) throws IOException
  {
    ImageServerIoHandler handler = new ImageServerIoHandler();
    SocketAcceptor acceptor = new SocketAcceptor();
    acceptor.getFilterChain().addLast("protocol", new ProtocolCodecFilter(new ImageCodecFactory(false)));
    acceptor.bind(new InetSocketAddress(PORT), handler);
    System.out.println("server is listenig at port " + PORT);
  }

}