import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;


//The class to be executed is MainClass only (which automatically runs an instance of the client and the server)

//Class representing the client
public class MainClient {

    public static void main(String[] args) {
        //a port may be passed as an argument


        //checks for the existence of the argument
        int currentPort = Consts.SOCKET_PORT;
        try {
            currentPort = Integer.parseInt(args[0]);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        SocketAddress address = new InetSocketAddress(currentPort);
        try(SocketChannel client = SocketChannel.open(address)) {

            System.out.println("Client socket connected");

            for(int i = 0; i < Consts.ARRAY_INIT_SIZE / 100; i++){
                String outputString = Consts.CLIENT_MESSAGE(i);
                System.out.println("Client has sent: " + outputString);
                ByteBuffer outputBuffer = ByteBuffer.wrap(outputString.getBytes());
                client.write(outputBuffer);

                System.out.println("Client has received: " + readLine(client));
            }

        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static String readLine(SocketChannel client) {
        try {
            //read buffer size
            ByteBuffer intBuffer = ByteBuffer.allocate(Consts.INT_SIZE);
            client.read(intBuffer);
            intBuffer.flip();

            //allocate right buffer
            ByteBuffer inputBuffer = ByteBuffer.allocate(intBuffer.getInt());

            client.read(inputBuffer);
            inputBuffer.flip();
            return new String(StandardCharsets.UTF_8.decode(inputBuffer).array()); //using UTF_8 decoding for right conversion

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
