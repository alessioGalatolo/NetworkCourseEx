package Emergency_Room;//class which emulates the emergency room, it also manages the doctors and acts as a monitor

public class EmergencyRoom {

    private boolean[] busyDoctors = new boolean[Consts.N_DOCTORS]; //represents the doctor from 1 to 10, the value is true if the doctor is visiting
    private Patient currentRed = null; //keeps the pointer the the current red code
    private int redWaiting = 0; //number of red codes waiting
    private int[] yellowWaiting = new int[Consts.N_DOCTORS]; //number of yellow codes waiting per doctor

    public EmergencyRoom(){
        //inits the arrays
        for(int i = 0; i < Consts.N_DOCTORS; i++){
            yellowWaiting[i] = 0;
            busyDoctors[i] = false;
        }
    }

    public synchronized void startVisit(Patient patient) {
        //switches between different types of codes
        switch(patient.getUrgency()){
            case WHITE:
                //should wait for all red codes
                //tries to find a free doctor(no yellows are waiting and none is being visited)
                int freeDoctor;
                while(redWaiting > 0 || currentRed != null || (freeDoctor = getFreeDoctor()) == -1){
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //sets the free doctor just found ad the doctor of that patient
                patient.setDoctor(freeDoctor);

                break;
            case YELLOW:
                //declares he is waiting for that doctor
                yellowWaiting[patient.getDoctor()]++;
                //waiting for all red codes to finish and the designated doctor to be free
                while(redWaiting > 0 || currentRed != null || busyDoctors[patient.getDoctor()]){
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //gets the doctor
                busyDoctors[patient.getDoctor()] = true;
                //leaves the queue for that doctor
                yellowWaiting[patient.getDoctor()]--;

                break;
            case RED:
                //declares he's waiting for a visit
                redWaiting++;

                //waiting to be the focus
                while(currentRed != null){
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                currentRed = patient;

                //leaves the queue (he is now on focus)
                redWaiting--;

                //now waits for all doctors to be free
                for(int i = 0; i < Consts.N_DOCTORS; i++){
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

    //looks for a free doctor, if found, he is set as busy and returns it number
    //if no free doctor is found, -1 is returned
    private int getFreeDoctor() {
        for (int i = 0; i < Consts.N_DOCTORS; i++)
            if (!busyDoctors[i] && yellowWaiting[i] == 0) {
                busyDoctors[i] = true;
                return i;
            }
        return -1;
    }


    public synchronized void endVisit(Patient patient) {
        switch(patient.getUrgency()){
            case WHITE:
                //white and yellow only have to release the doctor
            case YELLOW:
                busyDoctors[patient.getDoctor()] = false;
                notifyAll();
                break;
            case RED:
                //releases all the doctors
                currentRed = null;
                for(int i = 0; i < Consts.N_DOCTORS; i++){
                    busyDoctors[i] = false;
                }
                notifyAll();
                break;
        }
    }
}
