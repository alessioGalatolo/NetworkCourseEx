import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Arrays;

//interface to book sessions of the congress (used by the client for RMI)
public interface CongressBooking extends Remote {

    //books the session requested
    //@requires: the requested session has at least a speaker slot free
    //@returns: the outcome of the request
    boolean bookSession(Session session) throws RemoteException;

    //@returns: a list of all the session for the congress
    Session[][] getCongressProgram() throws RemoteException;


    //data class for a session, it is used to book the session
    //it implements serializable to let the client send the Session to the server
    class Session implements Serializable {
        private String[] speakers; //list of speakers of the session, initialized as empty array; size always < MAX_SPEAKERS
        private int time; //hour of the session, counting from 0
        private int day; //day of the session, counting from 0


        public Session(String[] speakers, int time, int day) {
            if(speakers.length > Consts.SPEAKERS_PER_SESSION) //checks array size
                this.speakers = Arrays.copyOfRange(speakers, 0, Consts.SPEAKERS_PER_SESSION); //truncate the array if bigger than allowed
            else
                this.speakers = speakers;

            this.time = time;
            this.day = day;
        }

        public Session(int time, int day){
            this(new String[0], time, day);
        }

        public int getDay() {
            return day;
        }

        public int getTime() {
            return time;
        }

        //returns true if there is space for int speakers
        public boolean hasFreeSpeakerSlot(int speakersNeeded){
            return Consts.SPEAKERS_PER_SESSION - speakers.length >= speakersNeeded;
        }

        public String[] getSpeakers() {
            return speakers;
        }

        //adds the speakers to the list if the sum of the arrays doesn't exceed the maximum allowed
        //returns the outcome of the operation
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
