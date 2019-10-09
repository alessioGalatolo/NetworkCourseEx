import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;
import static java.lang.Math.abs;

public class Patient extends Thread {


    private EmergencyRoom er;
    private int doctor = 0;
    private String name;
    private Urgency urgency;
    private int visits; //number of visits per patient

    public Patient(String name, Urgency u, EmergencyRoom er) {
        urgency = u;
        this.er = er;
        this.name = name;


        Random random = new Random(GregorianCalendar.getInstance().getTimeInMillis()); //random with time seed

        if(u == Urgency.YELLOW){
            doctor = random.nextInt(10);
        }

        //assignees visit
        visits = random.nextInt(15);
    }




    @Override
    public void run() {
        for(int i = 0; i < visits; i++) {

            //waits for all the doctors
            er.startVisit(this);

            //emulating the time for a visit
            try {
                Thread.sleep(abs(GregorianCalendar.getInstance().getTimeInMillis() % 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            er.endVisit(this);

            //patient waits for next visit
            try {
                Thread.sleep(GregorianCalendar.getInstance().getTimeInMillis() % 500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getDoctor() {
        return doctor;
    }

    public Urgency getUrgency() {
        return urgency;
    }

    public void setDoctor(int freeDoctor) {
        if(urgency == Urgency.WHITE)
            doctor = freeDoctor;
    }
}
