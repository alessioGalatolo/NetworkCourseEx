public class EmergencyRoom {
    boolean[] busyDoctors = new boolean[10]; //represents the doctor from 1 to 10, the value is true if the doctor is visiting


    public void startVisit(Patient patient) {
        patient.getDoctor();
    }


    public void endVisit(Patient patient) {

    }
}
