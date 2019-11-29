import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {

        try {
            Registry r;
//        /* Creazione di un registry sulla porta args[0]
            LocateRegistry.createRegistry(1099);
            r = LocateRegistry.getRegistry(1099);


            /* Pubblicazione dello stub nel registry */
            CongressEasyBooking congressEasyBooking = new CongressEasyBooking();
            r.rebind(Consts.CONGRESS_STUB_NAME, congressEasyBooking);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
