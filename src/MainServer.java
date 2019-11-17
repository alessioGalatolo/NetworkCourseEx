import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class MainServer {

    public static void main(String[] args) {
        try {
            DatagramSocket datagramSocket = new DatagramSocket(Consts.SOCKET_PORT);

            byte[] buffer = new byte[Consts.ARRAY_INIT_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                datagramSocket.receive(packet);

                String[] message = new String(packet.getData(), StandardCharsets.UTF_8).split(" ");
                if(!message[0].equals("PING")){
                    //error
                }
                if (Integer.parseInt(message[1]) == 0 ){
                    //....
                }
            }


        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
