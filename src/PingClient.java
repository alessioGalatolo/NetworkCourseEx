import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.GregorianCalendar;

public class PingClient {

    public static void main(String[] args) {
        //checks for the existence of the arguments
        int serverPort;
        String serverName;

        //checking first argument
        try {
            serverName = args[0];
        } catch (IndexOutOfBoundsException e) {
            //no arguments for server name
            System.out.println("Usage: java PingClient hostname port");
            return;
        }

        //checking second argument
        try{
            serverPort = Integer.parseInt(args[1]);
        }catch (IndexOutOfBoundsException | NumberFormatException e){
            //no arguments for server port
            System.out.println("PingClient: Err -arg 1");
            return;
        }


        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.setSoTimeout(Consts.UDP_TIMEOUT); //timeout for receive

            int survivedPackets = 0; //number of packets that made it back
            long delaySum = 0; //sums of all the delays
            long minDelay = Long.MAX_VALUE; //min delay
            long maxDelay = 0; //max delay

            DatagramPacket packet = new DatagramPacket(new byte[0], 0, InetAddress.getByName(serverName), serverPort); //data set as "empty"

            //send ping requests
            for(int i = 0; i < Consts.SEQ_N_RANGE; i++) {
                byte[] pingStringBytes = Consts.PING_STRING(i + 1).getBytes(StandardCharsets.UTF_8); //gets a generated constant string
                packet.setData(pingStringBytes, 0, pingStringBytes.length); //sets data
                datagramSocket.send(packet); //send packet

                try {
                    datagramSocket.receive(packet); //receives the packet or catches the timeoutException
                    String message = new String(packet.getData(), StandardCharsets.UTF_8); //converts message
                    long oldTimestamp = Long.parseLong(message.split(" ")[2]); //gets old timestamp
                    long delay = GregorianCalendar.getInstance().getTimeInMillis() - oldTimestamp; //computes delay
                    System.out.println("PingClient: " + message + ": " + delay + " ms"); //prints delay

                    //update vars for stats
                    survivedPackets++;
                    delaySum += delay;

                    if(delay < minDelay)
                        minDelay = delay;
                    else if(delay > maxDelay)
                        maxDelay = delay;

                }catch (SocketTimeoutException e){
                    //no data received
                    String message = new String(packet.getData(), StandardCharsets.UTF_8);
                    System.out.println("PingClient: " + message + ": *");
                }
            }

            //print summary statistics
            System.out.println();
            System.out.println();
            System.out.println(Consts.PINGCLIENT_SUMMARY_STRING(Consts.SEQ_N_RANGE, survivedPackets, minDelay, delaySum, maxDelay));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
