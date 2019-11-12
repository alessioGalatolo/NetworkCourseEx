//Alessio Galatolo 564857

public class MainClass {


    //main class. launches an instance of the server and Consts.N_CLIENTS instances of the clients as new threads.
    public static void main(String[] args) {
        //may get the port as an argument, in its absence uses the default one


        //checks for the existence of the argument
        int currentPort = Consts.SOCKET_PORT;
        try {
            currentPort = Integer.parseInt(args[0]);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No arguments were passed for the port, using " + Consts.SOCKET_PORT);
        }

        Server server = new Server(new String[]{(Integer.valueOf(currentPort)).toString()}); //passing the same port to client and server

        server.start();

        for(int i = 0; i < Consts.N_CLIENTS; i++) {
            Client client = new Client(new String[]{(Integer.valueOf(currentPort)).toString()});
            client.start();
        }

    }

    //simple class to call main method of MainServer in a separate thread
    static class Server extends Thread{

        private String[] args;

        public Server(String[] args){
            this.args = args;
        }

        @Override
        public void run() {
            MainServer.main(args);
        }
    }

    //simple class to call main method of MainClient in a separate thread
    static class Client extends Thread{

        private String[] args;

        public Client(String[] args){
            this.args = args;
        }

        @Override
        public void run() {
            MainClient.main(args);
        }
    }
}
