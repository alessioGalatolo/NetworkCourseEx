public final class Consts {
    public static final int INT_SIZE = 4;
    public static final int N_THREADS = 10;
    public static final int N_DOCTORS = 10;
    public static final int N_CONSUMER = 10;
    public static final int SOCKET_PORT = 6789;
    public static final int ARRAY_INIT_SIZE = 1024;
    public static final int N_CLIENTS = 100;

    public static final String CLIENT_MESSAGE(int i){
        return "The string n " + i + " will be sent to the server to be echoed";
    }
}
