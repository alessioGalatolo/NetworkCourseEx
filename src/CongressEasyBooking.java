
public class CongressEasyBooking implements CongressBooking {
    Session[][] congressProgram = new Session[Consts.CONGRESS_DAYS][Consts.SESSIONS_PER_DAY];


    @Override
    public boolean bookSession(Session session) {
        Session requestedSession = congressProgram[session.getDay()][session.getTime()];
        if(requestedSession.hasFreeSpeakerSlot(session.getSpeakers().length)){
            //enough slots
            requestedSession.addSpeakers(session.getSpeakers());
            return true;
        }

        return false;
    }

    @Override
    public Session[][] getDaySessions() {
        return congressProgram;
    }
}
