package CongressScheduler;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MainClass {

    //main class. Creates the remote object and the launches the client who will be using it
    public static void main(String[] args) {
        //may get the port of the registry, in its absence it uses a default one

        int socketPort = Consts.SOCKET_PORT; //default port
        try{
            socketPort = Integer.parseInt(args[0]); //look for port as argument
        }catch (IndexOutOfBoundsException ignored){
            System.out.println("No port found, using default one");
        }


        try {
            //create registry
            LocateRegistry.createRegistry(socketPort);
            Registry r = LocateRegistry.getRegistry(socketPort);

            CongressEasyBooking congressEasyBooking = new CongressEasyBooking(); //create remote object
            r.rebind(Consts.CONGRESS_STUB_NAME, congressEasyBooking); //bind remote object

            RMIClient.main(new String[]{String.valueOf(socketPort)}); //launch client

            //unexport object to terminate the RMI thread
            UnicastRemoteObject.unexportObject(congressEasyBooking, true);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
