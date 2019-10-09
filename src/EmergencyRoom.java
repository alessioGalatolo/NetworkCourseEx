public class EmergencyRoom {
    boolean[] busyDoctors = new boolean[10]; //represents the doctor from 1 to 10, the value is true if the doctor is visiting
    Patient currentRed = null;

    int redWaiting = 0; //number of red codes waiting
    int[] yellowWaiting = new int[10]; //number of yellow codes waiting per doctor


    public synchronized void startVisit(Patient patient) {
        switch(patient.getUrgency()){
            case WHITE:
                int freeDoctor;
                while(redWaiting > 0 || (freeDoctor = getFreeDoctor()) == -1){
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                patient.setDoctor(freeDoctor);

                break;
            case YELLOW:
                yellowWaiting[patient.getDoctor()]++;
                while(redWaiting > 0){
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                while(busyDoctors[patient.getDoctor()]){
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                busyDoctors[patient.getDoctor()] = true;
                yellowWaiting[patient.getDoctor()]--;

                break;
            case RED:
                //declares he's waiting for a visit
                redWaiting++;

                while(currentRed != null){
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                currentRed = patient;
                redWaiting--;

                //now waits for all doctors to be free
                for(int i = 0; i < 10; i++){
                    while(busyDoctors[i]){
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    busyDoctors[i] = true;
                }

                break;
        }
    }

    private int getFreeDoctor() {
        for (int i = 0; i < 10; i++)
            if (!busyDoctors[i] && yellowWaiting[i] == 0) {
                busyDoctors[i] = true;
                return i;
            }
        return -1;
    }


    public synchronized void endVisit(Patient patient) {
        switch(patient.getUrgency()){
            case WHITE:
            case YELLOW:
                busyDoctors[patient.getDoctor()] = false;
                break;
            case RED:
                currentRed = null;
                for(int i = 0; i < 10; i++){
                    busyDoctors[i] = false;
                }
                notify();
                break;
        }
    }
}
