import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Arrays;

//interface of the
public interface CongressBooking extends Remote {

    //books the session requested
    //@requires: the requested session has at least a speaker slot free
    //@returns: the outcome of the request
    boolean bookSession(Session session) throws RemoteException;

    //@returns: a list of all the session for the congress
    Session[][] getCongressProgram() throws RemoteException;


    //data class for a session
    class Session implements Serializable {
        private String[] speakers;
        private int time;
        private int day;


        public Session(String[] speakers, int time, int day) {
            if(speakers.length > Consts.SPEAKERS_PER_SESSION)
                this.speakers = Arrays.copyOfRange(speakers, 0, Consts.SPEAKERS_PER_SESSION); //truncate the array
            else
                this.speakers = speakers;

            this.time = time;
            this.day = day;
        }

        public int getDay() {
            return day;
        }

        public int getTime() {
            return time;
        }

        public boolean hasFreeSpeakerSlot(int speakersNeeded){
            return Consts.SPEAKERS_PER_SESSION - speakers.length >= speakersNeeded;
        }

        public String[] getSpeakers() {
            return speakers;
        }

        public boolean addSpeakers(String[] newSpeakers) {
            if(newSpeakers.length + speakers.length > Consts.SPEAKERS_PER_SESSION) //too many speakers
                return false;

            String[] newArray = new String[speakers.length + newSpeakers.length];
            System.arraycopy(speakers, 0, newArray, 0, speakers.length);
            System.arraycopy(newSpeakers, 0, newArray, speakers.length, newSpeakers.length);
            speakers = newArray;

            return true;

        }

        @Override
        public String toString() {
            return "Session on day " + (day + 1) + ", starting at " + (time + 1) + ". Speaker list: " + Arrays.toString(speakers);
        }
    }

}
