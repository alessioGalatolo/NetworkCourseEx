import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.GregorianCalendar;

public class PingClient {

    public static void main(String[] args) {
        //may get the port as an argument, in its absence uses the default one


        //checks for the existence of the argument
        int serverPort;
        String serverName;
        try {
            serverName = args[0];
        } catch (IndexOutOfBoundsException e) {
            //no arguments for server name
            System.out.println("PingClient: Err -arg 0");
            return;
        }

        try{
            serverPort = Integer.parseInt(args[1]);
        }catch (IndexOutOfBoundsException e){
            //no arguments for server port
            System.out.println("PingClient: Err -arg 1");
            return;
        }

        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.setSoTimeout(Consts.UDP_TIMEOUT);

            int survivedPackets = 0;
            long delaySum = 0;
            long minDelay = Long.MAX_VALUE;
            long maxDelay = 0;


            DatagramPacket packet = new DatagramPacket(new byte[0], 0, InetAddress.getByName(serverName), serverPort);
            for(int i = 0; i < Consts.SEQ_N_RANGE; i++) {
                byte[] pingStringBytes = Consts.PING_STRING(i + 1).getBytes();
                packet.setData(pingStringBytes, 0, pingStringBytes.length);
                datagramSocket.send(packet);

                try {
                    datagramSocket.receive(packet);
                    String message = new String(packet.getData(), StandardCharsets.UTF_8);
                    System.out.println("PingClient: sent " + message);
                    long oldTimestamp = Long.parseLong(message.split(" ")[2]);
                    long delay = GregorianCalendar.getInstance().getTimeInMillis() - oldTimestamp;
                    System.out.println("PingClient: RTT = " + delay);

                    survivedPackets++;
                    delaySum += delay;
                    if(delay < minDelay)
                        minDelay = delay;
                    else if(delay > maxDelay)
                        maxDelay = delay;

                }catch (SocketTimeoutException e){
                    String message = new String(packet.getData(), StandardCharsets.UTF_8);
                    System.out.println("PingClient: sent " + message);

                    System.out.println("PingClient: RTT = *");
                }
            }

            System.out.println();
            System.out.println();
            System.out.println(Consts.PINGCLIENT_SUMMARY_STRING(Consts.SEQ_N_RANGE, survivedPackets, minDelay, delaySum, maxDelay));
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
