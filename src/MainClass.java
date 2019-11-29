import Multicast_Time_Server.Consts;
import Multicast_Time_Server.TimeClient;
import Multicast_Time_Server.TimeServer;

public class MainClass {

    //main class. launches an instance of the server and an instance of the client as new threads.
    public static void main(String[] args) {
        //may get the _________ address, in its absence it uses a default one


        String multicastAddress = Consts.MULTICAST_ADDRESS;

        try{
            multicastAddress = args[0];
        }catch (IndexOutOfBoundsException ignored){
            System.out.println("No multicast address found, using default one");
        }


        TServer server = new TServer(new String[]{String.valueOf(Consts.SOCKET_PORT)}); //passing the same address to client and server
        server.run();

        TClient client = new TClient(new String[]{String.valueOf(Consts.SOCKET_PORT)});
        client.start();


    }

    //simple class to call main method of _____________ in a separate thread
    static class TServer extends Thread{

        private String[] args;

        public TServer(String[] args){
            this.args = args;
        }

        @Override
        public void run() {
            Server.main(args);
        }
    }

    //simple class to call main method of _________________ in a separate thread
    static class TClient extends Thread{

        private String[] args;

        public TClient(String[] args){
            this.args = args;
        }

        @Override
        public void run() {
            Client.main(args);
        }
    }
}
