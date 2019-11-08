//Alessio Galatolo 564857

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Set;

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
            //open server socket

            ServerSocket serverSocket = serverChannel.socket();
            InetSocketAddress address = new InetSocketAddress(currentPort);
            serverSocket.bind(address); //binds address
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {

                selector.select(); //blocking request

                Set<SelectionKey> readyKeys = selector.selectedKeys();

                for(SelectionKey key: readyKeys){

                    readyKeys.remove(key); //removing key from the set

                    try {
                        if (key.isAcceptable()) {
                            //accept new connection
                            ServerSocketChannel server = (ServerSocketChannel) key.channel();
                            SocketChannel clientSocketChannel = server.accept();
                            System.out.println("Accepted connection from " + clientSocketChannel);
                            clientSocketChannel.configureBlocking(false);
                            SelectionKey registerKey = clientSocketChannel.register(selector, SelectionKey.OP_READ);
                            ByteBuffer byteBuffer = ByteBuffer.allocate(Consts.ARRAY_INIT_SIZE);
                            System.out.println(byteBuffer);
//                            byteBuffer.flip();
                            registerKey.attach(byteBuffer);

                        }else if(key.isReadable()){
//                            System.out.println("Server: preparing to read");
                            SocketChannel currentSocketChannel = (SocketChannel) key.channel();
                            ByteBuffer inputBuffer = (ByteBuffer) key.attachment();

//                            if(inputBuffer.hasRemaining()) //the buffer was ready for writing, flipping it to read
//                                inputBuffer.flip();
                            currentSocketChannel.read(inputBuffer);
                            inputBuffer.flip();
                            SelectionKey selectionKey = currentSocketChannel.register(selector, SelectionKey.OP_WRITE);
                            selectionKey.attach(inputBuffer);

                            currentSocketChannel.write(inputBuffer);

//                            inputBuffer.flip(); //flips it to make it ready to write
//                            System.out.println(inputBuffer.toString());

//                            key.attach(inputBuffer);

                        }else if (key.isWritable()){
//                            System.out.println("Server: preparing to write");
                            SocketChannel currentSocketChannel = (SocketChannel) key.channel();
                            ByteBuffer outputBuffer = (ByteBuffer) key.attachment();

//                            System.out.println(outputBuffer.hasRemaining());
                            if(outputBuffer.hasRemaining()) {
                                System.out.println(currentSocketChannel.write(outputBuffer));
                            }

                            outputBuffer.clear();
                            SelectionKey selectionKey = currentSocketChannel.register(selector, SelectionKey.OP_READ);
                            selectionKey.attach(outputBuffer);

                        }
                    }catch (IOException e) {
                        key.cancel();
                        try{
                            key.channel().close();
                        }catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

    }

}
