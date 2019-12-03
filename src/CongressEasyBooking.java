import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

//simple implementation of the congress booking
public class CongressEasyBooking extends UnicastRemoteObject implements CongressBooking {

    private Session[][] congressProgram = new Session[Consts.CONGRESS_DAYS][Consts.SESSIONS_PER_DAY]; //stores the schedule for the congress

    public CongressEasyBooking() throws RemoteException {
        super(); //creates the remote object

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
            //session is null => it has to be created
            congressProgram[session.getDay()][session.getTime()] = session;
            return true;

            

        }else if(requestedSession.hasFreeSpeakerSlot(session.getSpeakers().length)){
            //enough slots

            //return the outcome of the add
            return requestedSession.addSpeakers(session.getSpeakers());
        }

        //not enough slots
        return false;
    }

    @Override
    public Session[][] getCongressProgram(){
        return congressProgram;
    }
}
