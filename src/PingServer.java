import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.GregorianCalendar;
import java.util.Random;

import static java.lang.Math.abs;

public class PingServer {

    public static void main(String[] args) {
        //may get the port as an argument, in its absence uses the default one


        //checks for the existence of the argument
        int currentPort = Consts.SOCKET_PORT;
        try {
            currentPort = Integer.parseInt(args[0]);
        } catch (IndexOutOfBoundsException e) {
            //no arguments
            System.out.println("PingServer: no arguments were passed for the port, using " + Consts.SOCKET_PORT);
        } catch (NumberFormatException e){
            //something was passed as an argument but could not be parsed to int
            System.out.println("PingServer: Err -arg 0");
        }


        try {
            DatagramSocket datagramSocket = new DatagramSocket(currentPort);

            byte[] buffer = new byte[Consts.ARRAY_INIT_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                datagramSocket.receive(packet);

                Random randomGenerator = new Random(GregorianCalendar.getInstance().getTimeInMillis());



                if(abs(randomGenerator.nextLong() % 100) >= Consts.LOSS_CHANCE){
                    //random induced delay
                    long delay = abs(randomGenerator.nextLong() % Consts.MAX_SLEEP_TIME);
                    Thread.sleep(delay);
                    System.out.println("PingServer: I decided to send back the following message with " + delay + "ms delay: " + new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8));

                    //setaddress????????????????????????????????????????????????
                    datagramSocket.send(packet);
                }else{
                    //not sent
                    System.out.println("PingServer: I decided NOT to send back the following message: " + new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8));
                }



//                String[] message = new String(packet.getData(), StandardCharsets.UTF_8).split(" ");
//                if(!message[0].equals("PING")){
//                    //error
//                }
//                int sequenceNumber = Integer.parseInt(message[1]);
//                int timestamp = Integer.parseInt(message[2]);


            }


        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
