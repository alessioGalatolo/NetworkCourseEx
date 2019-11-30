import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

public class CongressEasyBooking extends UnicastRemoteObject implements CongressBooking {
    private Session[][] congressProgram = new Session[Consts.CONGRESS_DAYS][Consts.SESSIONS_PER_DAY];

    public CongressEasyBooking() throws RemoteException {
        super();

        //fill congressProgram with null
        for(int i = 0; i < Consts.CONGRESS_DAYS; i++)
            Arrays.fill(congressProgram[i], null);
    }

    @Override
    public boolean bookSession(Session session) {
        if(session.getDay() < 0 || session.getDay() >= Consts.CONGRESS_DAYS || session.getTime() < 0 || session.getTime() >= Consts.SESSIONS_PER_DAY)
            //invalid session
            return false;

        Session requestedSession = congressProgram[session.getDay()][session.getTime()];
        if(requestedSession == null){
            //session is null => free
            congressProgram[session.getDay()][session.getTime()] = session;
            return true;

        }else if(requestedSession.hasFreeSpeakerSlot(session.getSpeakers().length)){
            //enough slots
            if(requestedSession.addSpeakers(session.getSpeakers()))
                return true;

            //the speakers were not added
            return false;
        }

        return false;
    }

    @Override
    public Session[][] getCongressProgram(){
        return congressProgram;
    }
}
