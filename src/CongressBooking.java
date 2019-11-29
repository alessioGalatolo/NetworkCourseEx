import java.rmi.Remote;

public interface CongressBooking extends Remote {

    //books the session requested
    //@requires: the requested session has at least a speaker slot free
    //@returns: the outcome of the request
    boolean bookSession(Session session);

    //@returns: a list of all the session for the congress
    Session[][] getDaySessions();


    //data class for a session
    class Session {
        private String[] speakers;
        int freeSpeakerSlot = Consts.SPEAKERS_PER_SESSION;
        private int time;
        private int day;

        //TODO: check obione error

        public Session(String[] speakers, int time, int day){
            this.speakers = new String[Consts.SPEAKERS_PER_SESSION];
            System.arraycopy(speakers, 0, this.speakers, 0, Consts.SPEAKERS_PER_SESSION);
            freeSpeakerSlot -= speakers.length;
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
            return freeSpeakerSlot >= speakersNeeded;
        }

        public String[] getSpeakers() {
            return speakers;
        }


        public void addSpeakers(String[] newSpeakers) {
            //TODO: add Index out of bound exception check
            System.arraycopy(newSpeakers, 0, speakers, freeSpeakerSlot, Consts.SPEAKERS_PER_SESSION);
            freeSpeakerSlot -= newSpeakers.length;
        }
    }

}
