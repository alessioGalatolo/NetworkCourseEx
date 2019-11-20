import java.util.GregorianCalendar;

import static java.lang.Math.round;

public final class Consts {
    public static final int INT_SIZE = 4; //in bytes
    public static final int N_CLIENTS = 1;
    public static final int N_STRINGS = 10;
    public static final int N_THREADS = 10;
    public static final int N_DOCTORS = 10;
    public static final int N_CONSUMER = 10;
    public static final int SOCKET_PORT = 6789;
    public static final int ARRAY_INIT_SIZE = 1024;
    public static final long MAX_SLEEP_TIME = 1000;
    public static final long LOSS_CHANCE = 25;
    public static final int SEQ_N_RANGE = 10;
    public static final int UDP_TIMEOUT = 2000;
    public static final String SERVER_ADDRESS_NAME = "localhost";


    public static final String PING_STRING(int i) {
        return "PING " + i + " " + GregorianCalendar.getInstance().getTimeInMillis();
    }

    public static String PINGCLIENT_SUMMARY_STRING(int seqNRange, int survivedPackets, long minDelay, long delaySum, long maxDelay) {
        String returnString = "PingClient: " + seqNRange + " packets sent, " + survivedPackets + " packets received, ";
        int lossPercentage = round(((1 - ((float) survivedPackets / seqNRange)) * 100));
        returnString += lossPercentage + "% packet loss\n";
        returnString += "PingClient: Round-trip (ms) min/avg/max = " + minDelay + "/" + round((float) delaySum * 100 / survivedPackets ) / 100 + "/" + maxDelay;
        return returnString;
    }
}
