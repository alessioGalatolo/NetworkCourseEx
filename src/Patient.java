import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Patient extends Thread {


    private final ArrayList<Doctor> doctors;
    private String name;
    private Urgency urgency;

    public Patient(String name, ArrayList<Doctor> d) {
        doctors = d;
        this.name = name;
    }

    public Patient(String name, Doctor d) {
        doctors = new ArrayList<>();
        doctors.add(d);
    }


    @Override
    public void run() {
        for(Doctor d: doctors){ //waits for all the doctors
            d.startVisit(urgency);
        }

        try {
            Thread.sleep(GregorianCalendar.getInstance().getTimeInMillis() % 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //TODO: patient returns again

        for(Doctor d: doctors){
            d.endVisit(urgency);
        }
    }

    public Urgency getUrgency(){
        return urgency;
    }
}
