import java.util.ArrayList;
import java.util.Arrays;

public class EmergencyRoom {

    Doctor[] doctors = new Doctor[10];
    DrMonitor[] drMonitors = new DrMonitor[10];
    ArrayList<Patient> patientsWaiting = new ArrayList<>();

    public void addPatient(String name, Priority priority, int doctor){
        switch(priority){
            case WHITE:
                break;
            case YELLOW:
                break;
            case RED:
                break;
        }
        patientsWaiting.add(new Patient(name, priority,))

    }

    private class Doctor{

    }

    private class DrMonitor {

    }
}
