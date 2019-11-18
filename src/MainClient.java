import java.io.IOException;
import java.net.*;

public class MainClient {

    public static void main(String[] args) {
        //may get the port as an argument, in its absence uses the default one


        //checks for the existence of the argument
        int serverPort;
        String serverName;
        try {
            serverName = args[0];
        } catch (IndexOutOfBoundsException e) {
            //no arguments for server name
            System.out.println("Err -arg 0");
            return;
        }

        try{
            serverPort = Integer.parseInt(args[1]);
        }catch (IndexOutOfBoundsException e){
            //no arguments for server port
            System.out.println("Err -arg 1");
            return;
        }

        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.setSoTimeout(Consts.UDP_TIMEOUT);

            for(int i = 0; i < Consts.SEQ_N_RANGE; i++) {
                byte[] pingStringBytes = Consts.PING_STRING(i).getBytes();
                DatagramPacket packet = new DatagramPacket(pingStringBytes, pingStringBytes.length, InetAddress.getByName(serverName), serverPort);
                datagramSocket.send(packet);

                try {
                    datagramSocket.receive(packet);
                }catch (SocketTimeoutException e){
                    //packet lost. do something

                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
