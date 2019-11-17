import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;


//The class to be executed is MainClass only (which automatically runs an instance of client and server)

//Class representing the client
public class MainClient {

    public static void main(String[] args) {
        //a port may be passed as an argument


        //checks for the existence of the argument
        int currentPort = Consts.SOCKET_PORT;
        try {
            currentPort = Integer.parseInt(args[0]);
        } catch (IndexOutOfBoundsException ignored) {

        }

        //socket init
        SocketAddress address = new InetSocketAddress(currentPort);
        try(SocketChannel client = SocketChannel.open(address)) {

            //for console input
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            //writing n strings to the server (taking 10 lines from console)
            for(int i = 0; i < Consts.N_STRINGS; i++){
//                String outputString = Consts.CLIENT_MESSAGE(i); //constant string relying on the index passed
//                String outputString = Consts.LONG_CLIENT_MESSAGE(i); //string that exceeds the 1024 bytes
                String outputString = consoleInput.readLine();
                ByteBuffer outputBuffer = ByteBuffer.wrap(outputString.getBytes());

                client.write(outputBuffer);

                System.out.println("Client has received: " + readBytes(client));
            }

        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    //a simple method which reads and integer from the buffer representing the number of bytes to be read afterwards
    //then returns the String corresponding to the bytes received
    private static String readBytes(SocketChannel client) {
        try {
            //read buffer size
            ByteBuffer intBuffer = ByteBuffer.allocate(Consts.INT_SIZE);
            client.read(intBuffer);
            intBuffer.flip();

            //allocate right sized buffer
            ByteBuffer inputBuffer = ByteBuffer.allocate(intBuffer.getInt());

            client.read(inputBuffer);
            inputBuffer.flip();
            return new String(inputBuffer.array(), StandardCharsets.UTF_8); //using UTF_8 decoding for right conversion

        } catch (IOException e) {
            e.printStackTrace();
            return null; //error, nothing has to be returned
        }
    }

}
