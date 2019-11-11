public class MainClass {

    public static void main(String[] args) {

        //checks for the existence of the argument
        int currentPort = Consts.SOCKET_PORT;
        try {
            currentPort = Integer.parseInt(args[0]);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No arguments were passed for the port, using " + Consts.SOCKET_PORT);
        }

        Server server = new Server(new String[]{(Integer.valueOf(currentPort)).toString()});

        server.start();

        for(int i = 0; i < Consts.N_CLIENTS; i++) {
            Client client = new Client(new String[]{(Integer.valueOf(currentPort)).toString()});
            client.start();
        }

    }

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
