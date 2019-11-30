import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

public class CongressEasyBooking extends UnicastRemoteObject implements CongressBooking {
    private Session[][] congressProgram = new Session[Consts.CONGRESS_DAYS][Consts.SESSIONS_PER_DAY];

    public CongressEasyBooking() throws RemoteException {
        super();
        for(int i = 0; i < Consts.CONGRESS_DAYS; i++)
            Arrays.fill(congressProgram[i], null);

    }

    @Override
    public boolean bookSession(Session session) {
        Session requestedSession = congressProgram[session.getDay()][session.getTime()];
        if(requestedSession == null){
            //no session -> free
            congressProgram[session.getDay()][session.getTime()] = session;
            return  true;
        }else if(requestedSession.hasFreeSpeakerSlot(session.getSpeakers().length)){
            //enough slots
            requestedSession.addSpeakers(session.getSpeakers());
            return true;
        }

        return false;
    }

    @Override
    public Session[][] getDaySessions(){
        return congressProgram;
    }
}
