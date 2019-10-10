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
            doctor = random.nextInt(Consts.N_DOCTORS);
        }

        //assignees visit
        visits = random.nextInt(5) + 1;
    }




    @Override
    public void run() {
        for(int i = 0; i < visits; i++) {

            //waits for all the doctors
            //System.out.println("Patient " + name + " has requested its " + i + " visit");
            er.startVisit(this);


            //emulating the time for a visit
            try {
                Thread.sleep(abs(GregorianCalendar.getInstance().getTimeInMillis() % 500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            er.endVisit(this);

            //patient waits for next visit
            try {
                Thread.sleep(abs(GregorianCalendar.getInstance().getTimeInMillis() % 100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Patient " + name + " has done its visit");
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
