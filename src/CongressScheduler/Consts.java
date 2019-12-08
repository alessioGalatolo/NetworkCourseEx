package CongressScheduler;//class with most used constants

import java.util.Arrays;

public final class Consts {
    public static final int CONGRESS_DAYS = 3;
    public static final int SESSIONS_PER_DAY = 12;
    public static final int SPEAKERS_PER_SESSION = 5;
    public static final int INT_SIZE = 4; //in bytes
    public static final int SOCKET_PORT = 6789;
    public static final int ARRAY_INIT_SIZE = 1024;
    public static final String CONGRESS_STUB_NAME = "CONGRESS_EASY_BOOKING_OBJECT_STUB_NAME";

    private static final String[] SPEAKERS_NAMES = new String[Consts.SPEAKERS_PER_SESSION];// = {"name1", "name2", "name3", "name4", "name5"};

    //returns and array with some names
    public static final String[] GET_SPEAKERS_NAMES(){
        if(SPEAKERS_NAMES[0] == null)
            Arrays.setAll(SPEAKERS_NAMES, (index) -> "name" + (index + 1));
        return SPEAKERS_NAMES;
    }
}
