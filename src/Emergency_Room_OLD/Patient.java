package Emergency_Room_OLD;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;
import static java.lang.Math.abs;

public class Patient extends Thread {


    private final ArrayList<Doctor> doctors;
    private String name;
    private Urgency urgency;
    private int visits; //number of visits per patient

    public Patient(String name, ArrayList<Doctor> d, Urgency u) {
        doctors = d;
        urgency = u;
        this.name = name;
        Random random = new Random();
        visits = random.nextInt() % 15;
    }

    public Patient(String name, Doctor d, Urgency u) {
        doctors = new ArrayList<>();
        doctors.add(d);
        this.name = name;
        urgency = u;

        Random random = new Random();
        visits = random.nextInt() % 15;
    }


    @Override
    public void run() {
        for(int i = 0; i < visits; i++) {

            //waits for all the doctors
            for (Doctor d : doctors) {
                //System.out.println("Emergency_Room_OLD.Patient: " + name + ", waiting for doctor " + d.getName());
                d.startVisit(urgency);
                //System.out.println("Emergency_Room_OLD.Patient: " + name + ", got doctor " + d.getName());
            }

            //emulating the time for a visit
            try {
                Thread.sleep(abs(GregorianCalendar.getInstance().getTimeInMillis() % 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            for (Doctor d : doctors) {
                d.endVisit(urgency);
            }

            //patient waits for next visit
            try {
                Thread.sleep(GregorianCalendar.getInstance().getTimeInMillis() % 500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Emergency_Room_OLD.Patient " + name + " has done");
    }
}
