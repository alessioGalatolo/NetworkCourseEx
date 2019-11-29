package Multicast_Time_Server;

public class MainClass {

    //main class. launches an instance of the server and an instance of the client as new threads.
    public static void main(String[] args) {
        //may get the multicast address, in its absence it uses a default one


        String multicastAddress = Consts.MULTICAST_ADDRESS;

        try{
            multicastAddress = args[0];
        }catch (IndexOutOfBoundsException ignored){
            System.out.println("No multicast address found, using default one");
        }


        Server server = new Server(new String[]{multicastAddress}); //passing the same address to client and server
        server.start();

        Client client = new Client(new String[]{multicastAddress});
        client.start();


        try {
            Thread.sleep(Consts.TOTAL_RUNTIME); //Total runtime is an approximation of the time needed for the client to read all the dates
            server.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    //simple class to call main method of TimeServer in a separate thread
    static class Server extends Thread{

        private String[] args;

        public Server(String[] args){
            this.args = args;
        }

        @Override
        public void run() {
            TimeServer.main(args);
        }
    }

    //simple class to call main method of TimeClient in a separate thread
    static class Client extends Thread{

        private String[] args;

        public Client(String[] args){
            this.args = args;
        }

        @Override
        public void run() {
            TimeClient.main(args);
        }
    }
}
