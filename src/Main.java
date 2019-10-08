//Alessio Galatolo 564857

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        //generating a tuple of names, urgency and doctor need (In case of yellow code)
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Urgency> urgencies = new ArrayList<>();
        ArrayList<Integer> doctors = new ArrayList<>();

        Random randomGen = new Random(GregorianCalendar.getInstance().getTimeInMillis()); //random generator for the doctor

        for (int i = 0; i < 100; i++) {
            Urgency u = Urgency.WHITE; //needs init because of a compilation error
            switch (i % 3) {
                case 0:
                    u = Urgency.WHITE;
                    break;
                case 1:
                    u = Urgency.YELLOW;
                    break;
                case 2:
                    u = Urgency.RED;
                    break;
            }
            names.add("Name" + i);
            urgencies.add(u);
            doctors.add(randomGen.nextInt(10));
        }

        try {
            EmergencyRoom ER = new EmergencyRoom(names, urgencies, doctors);
        } catch (EmergencyRoom.IllegalPatientException e) {
            e.printStackTrace();
        }



    }
}
