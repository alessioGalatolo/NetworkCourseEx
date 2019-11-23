import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.GregorianCalendar;
import java.util.Random;

import static java.lang.Math.abs;

public class PingServer {

    public static void main(String[] args) {
        //checks for the existence of the arguments
        int currentPort;

        try {
            currentPort = Integer.parseInt(args[0]);
        } catch (IndexOutOfBoundsException e) {
            //no arguments
            System.out.println("Usage: java PingServer port");
            return;
        } catch (NumberFormatException e){
            //something was passed as an argument but could not be parsed to int
            System.out.println("PingServer: Err -arg 0");
            return;
        }


        try {
            DatagramSocket datagramSocket = new DatagramSocket(currentPort);

            byte[] buffer = new byte[Consts.ARRAY_INIT_SIZE];
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);

            while (true) {
                datagramSocket.receive(request);

                Random randomGenerator = new Random(GregorianCalendar.getInstance().getTimeInMillis());

                if(abs(randomGenerator.nextLong() % 100) >= Consts.LOSS_CHANCE){
                    //random induced delay
                    long delay = abs(randomGenerator.nextLong() % Consts.MAX_SLEEP_TIME);
                    Thread.sleep(delay);
                    System.out.println("PingServer: " + request.getAddress() + ":" + request.getPort() + "> " + new String(request.getData(), 0, request.getLength(), StandardCharsets.UTF_8) + " ACTION: delayed " + delay + " ms");

                    DatagramPacket response = new DatagramPacket(request.getData(), request.getData().length, request.getAddress(), request.getPort());
                    datagramSocket.send(response);
                }else{
                    //not sent
                    System.out.println("PingServer: " + request.getAddress() + ":" + request.getPort() + "> " + new String(request.getData(), 0, request.getLength(), StandardCharsets.UTF_8) + " ACTION: not sent");
                }

            }


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
