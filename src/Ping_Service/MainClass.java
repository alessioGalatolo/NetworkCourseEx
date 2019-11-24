package Ping_Service;//Alessio Galatolo 564857


public class MainClass {

    //main class. launches an instance of the server and Echo_NIO_Server.Ping_Service.Consts.N_CLIENTS instances of the clients as new threads.
    public static void main(String[] args) {
        //may get the port as an argument, in its absence uses the default one


        //checks for the existence of the argument
        int currentPort = Consts.SOCKET_PORT;
        try {
            currentPort = Integer.parseInt(args[0]);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No arguments were passed for the port, using " + Consts.SOCKET_PORT);
        }

        Client client = new Client(new String[]{Consts.SERVER_ADDRESS_NAME, (Integer.valueOf(currentPort)).toString()});
        client.start();

        Server server = new Server(new String[]{(Integer.valueOf(currentPort)).toString()}); //passing the same port to client and server
        server.start(); //server will keep running even after clients below have completed their requests

    }

    //simple class to call main method of Ping_Service.PingServer in a separate thread
    static class Server extends Thread{

        private String[] args;

        public Server(String[] args){
            this.args = args;
        }

        @Override
        public void run() {
            PingServer.main(args);
        }
    }

    //simple class to call main method of Ping_Service.PingClient in a separate thread
    static class Client extends Thread{

        private String[] args;

        public Client(String[] args){
            this.args = args;
        }

        @Override
        public void run() {
            PingClient.main(args);
        }
    }
}
