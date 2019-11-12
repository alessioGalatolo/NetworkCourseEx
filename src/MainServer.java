//Alessio Galatolo 564857

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

//The class to be executed is only MainClass (which automatically runs an instance of the client and the server)

//Class representing the server
public class MainServer {

    public static void main(String[] args) {
        //a port may be passed as an argument


        //checks for the existence of the argument
        int currentPort = Consts.SOCKET_PORT;
        try {
            currentPort = Integer.parseInt(args[0]);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        //try with resources
        try(ServerSocketChannel serverChannel = ServerSocketChannel.open();
                Selector selector = Selector.open()) {

            //init server
            ServerSocket serverSocket = serverChannel.socket();
            InetSocketAddress address = new InetSocketAddress(currentPort);
            serverSocket.bind(address); //binds address
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                selector.select(); //blocking request

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                //get ready keys
                while (iterator.hasNext()){

                    SelectionKey currentKey = iterator.next();
                    iterator.remove();

                    try {
                        if (currentKey.isAcceptable()) {
                            //accept new connection

                            ServerSocketChannel server = (ServerSocketChannel) currentKey.channel();
                            SocketChannel clientSocketChannel = server.accept();
                            System.out.println("Server has accepted connection from " + clientSocketChannel);
                            clientSocketChannel.configureBlocking(false);
                            clientSocketChannel.register(selector, SelectionKey.OP_READ);

                        }else if(currentKey.isReadable()){
                            SocketChannel currentSocketChannel = (SocketChannel) currentKey.channel();

                            ByteBuffer byteBuffer = ByteBuffer.allocate(Consts.ARRAY_INIT_SIZE); //if bytes to be read > Consts.ARRAY_INIT_SIZE the message will be split

                            if(currentSocketChannel.read(byteBuffer) == -1) {
                                //end of stream
                                currentKey.cancel();
                            }else {
                                SelectionKey selectionKey = currentSocketChannel.register(selector, SelectionKey.OP_WRITE); //register for writing
                                selectionKey.attach(byteBuffer); //attach buffer
                            }
                        }else if (currentKey.isWritable()){
                            //writing buffer attached to the key
                            SocketChannel currentSocketChannel = (SocketChannel) currentKey.channel();
                            ByteBuffer byteBuffer = (ByteBuffer) currentKey.attachment();
                            byteBuffer.flip();

                            //sending first the size of the buffer to be allocated
                            ByteBuffer intBuffer = ByteBuffer.allocate(Consts.INT_SIZE);
                            intBuffer.putInt(byteBuffer.remaining());
                            intBuffer.flip();
                            currentSocketChannel.write(intBuffer);

                            //writing buffer
                            currentSocketChannel.write(byteBuffer);
                            byteBuffer.clear();
                            currentSocketChannel.register(selector, SelectionKey.OP_READ); //expecting a write from the client as the new operation
                        }else{
                            System.out.println("Key has not been recognised");
                        }
                    }catch (IOException e) {
                        currentKey.cancel();
                        try{
                            currentKey.channel().close();
                        }catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
