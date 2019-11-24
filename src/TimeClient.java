import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class TimeClient {

    public static void main(String[] args) {
        //Gets the multicast address as an argument

        //checks for the existence of the argument
        InetAddress multicastGroup;
        try {
            multicastGroup = InetAddress.getByName(args[0]);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Usage: java TimeClient MulticastAddress");
            return;
        } catch (UnknownHostException e) {
            System.out.println("ERR arg-0");
            return;
        }

        //opens multicast socket
        try(MulticastSocket multicastSocket = new MulticastSocket(Consts.SOCKET_PORT)){

            DatagramPacket packet = new DatagramPacket(new byte[Consts.ARRAY_INIT_SIZE], Consts.ARRAY_INIT_SIZE); //packet to be received
            multicastSocket.setSoTimeout(Consts.UDP_TIMEOUT);
            multicastSocket.joinGroup(multicastGroup);

            for(int i = 0; i < Consts.READS_N; i++){
                multicastSocket.receive(packet);
                System.out.println(new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8)); //prints date for 10 times
            }

            multicastSocket.leaveGroup(multicastGroup);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
