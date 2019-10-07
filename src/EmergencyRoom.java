import java.util.ArrayList;
import java.util.List;



//emulates the emergency room, also behaves as a department manager
public class EmergencyRoom {

    private ArrayList<Doctor> doctors = new ArrayList<>(); //stores the doctors (used also as monitor)
    private final int DOCTORS_NUMBER = 10; //constant for the number of doctors

    public EmergencyRoom(List<String> names, List<Urgency> urgencies, List<Integer> doctors) throws IllegalPatientException {
        //init doctors
        for(int i = 0; i < DOCTORS_NUMBER; i++) {
            this.doctors.add(new Doctor(i));
        }

        //
        for(int i = 0; i < names.size(); i++){
            if(urgencies.get(i) == Urgency.YELLOW)
                addPatient(names.get(i), urgencies.get(i), doctors.get(i));
            else{
                addPatient(names.get(i), urgencies.get(i));
            }
        }
    }

    //addPatients for red and white codes (that don't need to specify the doctor)
    public void addPatient(String name, Urgency urgency) throws IllegalPatientException {
        switch(urgency){
            case WHITE:
                addPatient(name, urgency, getLessBusyDoctor());
                break;
            case RED:
                addPatient(name, urgency, 0);
                break;
            case YELLOW:
                //Error (yellow should say its doctor)
                throw new IllegalPatientException();
        }

    }

    //Main addPatient
    public void addPatient(String name, Urgency urgency, int doctor){
        Patient p; //the patient

        switch(urgency){
            case WHITE:
                //goes on, at this point WHITE and YELLOW code behave the same
            case YELLOW:
                p = new Patient(name, doctors.get(doctor), urgency);
                p.start();
                break;
            case RED:
                p = new Patient(name, doctors, urgency);
                p.start();
                break;
        }


    }

    //finds the doctor with the least number of patients waiting
    private int getLessBusyDoctor() {
        int doctorIndex = 0;
        int minWait = doctors.get(0).getWaitingNumber();
        for(int i = 1; i < doctors.size(); i++){
            if(doctors.get(i).getWaitingNumber() < minWait){
                minWait = doctors.get(i).getWaitingNumber();
                doctorIndex = i;
            }
        }
        return doctorIndex;
    }

    //exception for invalid use of addPatient
    private class IllegalPatientException extends Exception{}
}
