import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Arrays;

public interface CongressBooking extends Remote {

    //books the session requested
    //@requires: the requested session has at least a speaker slot free
    //@returns: the outcome of the request
    boolean bookSession(Session session) throws RemoteException;

    //@returns: a list of all the session for the congress
    Session[][] getDaySessions() throws RemoteException;


    //data class for a session
    class Session implements Serializable {
        //TODO: change String[] size from constant speakers_per_session to the actual size
        private String[] speakers = new String[0];
//        int freeSpeakerSlot = Consts.SPEAKERS_PER_SESSION;
        private int time;
        private int day;

        //TODO: check obione error

        public Session(String[] speakers, int time, int day) {
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

//        public int getFreeSpeakerSlot() {
//            return freeSpeakerSlot;
//        }

        public String[] getSpeakers() {
            return speakers;
        }


        public void addSpeakers(String[] newSpeakers) {
            //TODO: add Index out of bound exception check

            String[] newArray = new String[speakers.length + newSpeakers.length];
            System.arraycopy(speakers, 0, newArray, 0, speakers.length);
            System.arraycopy(newSpeakers, 0, newArray, speakers.length, newSpeakers.length);
            speakers = newArray;

        }

        @Override
        public String toString() {
            return "Session on day " + (day + 1) + ", starting at " + (time + 1) + ". Speaker list: " + Arrays.toString(speakers);
        }
    }

}
