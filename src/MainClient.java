import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

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

        SocketAddress address = new InetSocketAddress(args[0], currentPort);
        try(SocketChannel client = SocketChannel.open(address)) {

            for(int i = 0; i < Consts.ARRAY_INIT_SIZE; i++){
                String outputString = "This string,n " + i + " ,is to be sent to the server to be echoed";
                ByteBuffer outputBuffer = ByteBuffer.wrap(outputString.getBytes());
                client.write(outputBuffer);

                System.out.println(readline(client));
            }

//            ByteBuffer buffer = ByteBuffer.allocate(4);
//            IntBuffer view = buffer.asIntBuffer();
//
//            for (int expected = 0; ; expected++) {
//                client.read(buffer);
//                int actual = view.get();
//                buffer.clear();
//                view.rewind();
//                if (actual != expected) {
//                    System.err.println("Expected " + expected + "; was " +
//                            actual);
//                    break;
//                }
//                System.out.println(actual);
//            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static String readline(SocketChannel client) {
        ByteBuffer inputBuffer = ByteBuffer.allocate(Consts.ARRAY_INIT_SIZE);

        String finalString = "";

        try {

            while(client.read(inputBuffer) != -1){
                String s = String.valueOf(inputBuffer);
                finalString = finalString.concat(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return finalString;
    }

}
