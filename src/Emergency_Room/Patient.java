package Emergency_Room;

import java.util.GregorianCalendar;
import java.util.Random;
import static java.lang.Math.abs;

//class representing a patient
public class Patient extends Thread {

    private EmergencyRoom er; //used as a monitor
    private int doctor = 0; //the doctor assigned or needed (0 in case of red code)
    private String name;
    private Urgency urgency;
    private int visits; //number of visits per patient

    public Patient(String name, Urgency u, EmergencyRoom er) {
        urgency = u;
        this.er = er;
        this.name = name;


        Random random = new Random(GregorianCalendar.getInstance().getTimeInMillis()); //random with time seed

        if(u == Urgency.YELLOW){
            doctor = random.nextInt(Consts.N_DOCTORS); //generates a random doctor in case of a yellow code
        }

        //assignees visit
        visits = random.nextInt(5) + 1;
    }




    @Override
    public void run() {
        //emulates a visit

        for(int i = 0; i < visits; i++) {

            //waits to enter the visit room
            er.startVisit(this);


            //emulating the time for a visit
            try {
                Thread.sleep(abs(GregorianCalendar.getInstance().getTimeInMillis() % 500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //visit end
            er.endVisit(this);

            //patient waits before next visit
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
