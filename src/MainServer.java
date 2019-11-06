//Alessio Galatolo 564857

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

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
                            SocketChannel client = server.accept();
                            System.out.println("Accepted connection from " + client);
                            client.configureBlocking(false);
                            SelectionKey registerKey = client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                            registerKey.attach(ByteBuffer.allocate(Consts.ARRAY_INIT_SIZE));

                        } else if (key.isWritable()){
                            SocketChannel currentSocketChannel = (SocketChannel) key.channel();
                            ByteBuffer outputBuffer = (ByteBuffer) key.attachment();

                            if(outputBuffer.hasRemaining()) {
                                currentSocketChannel.write(outputBuffer);
                            }
                        }else if(key.isReadable()){
                            SocketChannel currentSocketChannel = (SocketChannel) key.channel();
                            ByteBuffer inputBuffer = (ByteBuffer) key.attachment();
                            if(inputBuffer.hasRemaining()) //the buffer was ready for writing, flipping it to read
                                inputBuffer.flip();
                            currentSocketChannel.read(inputBuffer);

                            inputBuffer.flip(); //flips it to make it ready to write

//                            key.attach(inputBuffer);

                        }
                    }catch (IOException ex) {
                        key.cancel();
                        try{
                            key.channel().close();
                        }catch (IOException cex) {

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
