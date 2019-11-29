import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteObject;
import java.util.Arrays;

import static java.lang.Math.abs;

public class Client {

    public static void main(String[] args) {
        try(BufferedReader in= new BufferedReader(new InputStreamReader(System.in))){
            Registry r = LocateRegistry.getRegistry(/*args[0]*/);
            Remote remoteObject= r.lookup(Consts.CONGRESS_STUB_NAME);
            CongressBooking serverObject = (CongressBooking) remoteObject;

            while (true){
                String string = in.readLine();
                //DO SOMETHING
                serverObject.bookSession(new CongressBooking.Session(new String[]{"tizio"}, abs(Integer.parseInt(string) % 12), abs(Integer.parseInt(string) % 3) ));
//                serverObject.bookSession(new CongressBooking.Session(new String[]{"tizio", ""}, 3, 0 ));
                System.out.println(Arrays.deepToString(((CongressBooking) remoteObject).getDaySessions()));
            }



        } catch (NotBoundException | IOException e){
            e.printStackTrace();
        }
    }
}
