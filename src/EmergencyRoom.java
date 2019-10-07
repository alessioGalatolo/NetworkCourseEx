import java.util.ArrayList;


//emulates the emergency room, also behaves as a department manager
public class EmergencyRoom {

    private ArrayList<Doctor> doctors = new ArrayList<>();
    private final int DOCTORS_NUMBER = 10;

    //for red and white codes (that don't need to specify the doctor)
    public void addPatient(String name, Urgency urgency) throws IllegalPatientException {
        switch(urgency){
            case WHITE:
                //TODO: figure out the doctor number
                addPatient(name, urgency, /*TODO*/ getLessBusyDoctor());
                break;
            case RED:
                addPatient(name, urgency, 0);
                break;
            case YELLOW:
                //Error
                throw new IllegalPatientException();
        }

    }

    private int getLessBusyDoctor() {
        //TODO
        return 0;
    }

    //for
    public void addPatient(String name, Urgency urgency, int doctor){
        for(int i = 0; i < DOCTORS_NUMBER; i++) {
            doctors.add(new Doctor(i));
        }

        Patient p; //the patient

        switch(urgency){
            case WHITE:
                //goes on, at this point WHITE and YELLOW code behave the same
            case YELLOW:
                p = new Patient(name, doctors.get(doctor));
                p.start();
                break;
            case RED:
                p = new Patient(name, doctors);
                p.start();
                break;
        }


    }



    private class IllegalPatientException extends Exception{}
}
