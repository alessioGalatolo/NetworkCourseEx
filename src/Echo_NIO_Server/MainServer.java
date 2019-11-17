package Echo_NIO_Server;//Alessio Galatolo 564857

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;

//The class to be executed is Echo_NIO_Server.MainClass only (which automatically runs an instance of the client and the server)

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

            //no termination is provided
            while (true) {
                selector.select(); //blocking request

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                //check every ready key.
                while (iterator.hasNext()){

                    SelectionKey currentKey = iterator.next();
                    iterator.remove();

                    try {
                        if (currentKey.isAcceptable()) {
                            //accept new connection

                            ServerSocketChannel server = (ServerSocketChannel) currentKey.channel();
                            SocketChannel clientSocketChannel = server.accept();
                            System.out.println("Server has accepted connection from " + clientSocketChannel.getRemoteAddress());
                            clientSocketChannel.configureBlocking(false);
                            clientSocketChannel.register(selector, SelectionKey.OP_READ); //expecting a write from the client as the new operation

                        }else if(currentKey.isReadable()){
                            /*
                                socket reads at most Echo_NIO_Server.Consts.ARRAY_INIT_SIZE bytes and puts the bytes read as an
                                attachment to the the selectionKey. If an array of byte was already attached, they
                                are joined and put back in the selectionKey.
                            */

                            SocketChannel currentSocketChannel = (SocketChannel) currentKey.channel();

                            ByteBuffer byteBuffer = ByteBuffer.allocate(Consts.ARRAY_INIT_SIZE);

                            if(currentSocketChannel.read(byteBuffer) == -1) {
                                //end of stream
                                currentKey.cancel();
                            }else {
                                //something has been read

                                byteBuffer.flip();

                                byte[] lastBytes = (byte[]) currentKey.attachment(); //getting previous pieces of message (if existent)
                                if(lastBytes != null){
                                    //an old attachment has been found

                                    byte[] largerByteArray = Arrays.copyOf(lastBytes, lastBytes.length + byteBuffer.limit()); //returns bigger array
                                    byte[] lastByteRead = Arrays.copyOfRange(byteBuffer.array(), 0, byteBuffer.limit());
                                    System.arraycopy(lastByteRead, 0, largerByteArray, lastBytes.length, lastByteRead.length); //joins arrays
                                    currentKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE); //updates key with write op
                                    currentKey.attach(largerByteArray); //attach buffer
                                }else{
                                    //no old attachment
                                    currentKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                                    currentKey.attach(Arrays.copyOfRange(byteBuffer.array(), 0, byteBuffer.limit())); //attach buffer
                                }
                            }
                        }else if (currentKey.isWritable()){
                            /*
                                Checks the attachment of the selectionKey.
                                if a byte[] is found -> allocates a byteBuffer and tries to write it all. if an incomplete write happens, the remaining buffer is stored as an attachment
                                if a ByteBuffer is found -> an incomplete write happened, tries to complete it.
                             */

                            SocketChannel currentSocketChannel = (SocketChannel) currentKey.channel();

                            ByteBuffer byteBuffer;

                            if(currentKey.attachment() instanceof ByteBuffer){
                                byteBuffer = (ByteBuffer) currentKey.attachment();
                            }else{
                                byteBuffer = ByteBuffer.wrap((byte[]) currentKey.attachment());
                            }


                            //sending first the size of the buffer to be allocated
                            ByteBuffer intBuffer = ByteBuffer.allocate(Consts.INT_SIZE);
                            intBuffer.putInt(byteBuffer.remaining());
                            intBuffer.flip();
                            currentSocketChannel.write(intBuffer);

                            //writing buffer
                            currentSocketChannel.write(byteBuffer);
                            if(byteBuffer.hasRemaining()) {
                                currentKey.interestOps(SelectionKey.OP_WRITE); //expecting a write from the server as the new operation
                                currentKey.attach(byteBuffer);
                            }else{
                                currentKey.interestOps(SelectionKey.OP_READ); //expecting a write from the client as the new operation
                                currentKey.attach(null); //nothing to be kept
                            }
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
