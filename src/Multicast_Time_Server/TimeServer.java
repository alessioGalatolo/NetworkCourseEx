package Multicast_Time_Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.GregorianCalendar;

public class TimeServer {

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


        try(DatagramSocket datagramSocket = new DatagramSocket()){

            byte[] message;

            //terminates when interrupted
            while (!Thread.interrupted()) {
                message = GregorianCalendar.getInstance().getTime().toString().getBytes(StandardCharsets.UTF_8); //gets date

                DatagramPacket datePacket = new DatagramPacket(message, 0, message.length, multicastGroup, Consts.SOCKET_PORT); //create the packet
                datagramSocket.send(datePacket);
                Thread.sleep(Consts.SLEEP_TIME); //sleeps for some time
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException ignored) {
        }

    }
}
