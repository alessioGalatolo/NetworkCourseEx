import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Patient extends Thread {


    private final ArrayList<Doctor> doctors;

    public Patient(ArrayList<Doctor> d) {
        this.doctors = d;
    }

    @Override
    public void run() {
        for(Doctor d: doctors){
            d.startVisit();
        }

        try {
            Thread.sleep(GregorianCalendar.getInstance().getTimeInMillis() % 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //TODO: patient returns again

        for(Doctor d: doctors){
            d.endVisit();
        }
    }
}
