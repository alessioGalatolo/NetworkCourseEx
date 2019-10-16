package Emergency_Room;//Alessio Galatolo 564857

public class Main {

    public static void main(String[] args) {
        //input from args, 3 ints representing the number of patients with each code. [WhiteCodes] [YellowCodes] [RedCodes]

        int whiteCodes = Integer.parseInt(args[0]);
        int yellowCodes = Integer.parseInt(args[1]);
        int redCodes = Integer.parseInt(args[2]);

        EmergencyRoom er = new EmergencyRoom();

        //creates all the patients (extends thread) and starts them
        for(int i = 0; i < redCodes; i++){
            Patient p = new Patient("patientRed" + i, Urgency.RED, er);
            p.start();
        }

        for(int i = 0; i < yellowCodes; i++){
            Patient p = new Patient("patientYellow" + i, Urgency.YELLOW, er);
            p.start();
        }

        for(int i = 0; i < whiteCodes; i++){
            Patient p = new Patient("patientWhite" + i, Urgency.WHITE, er);
            p.start();
        }
    }

}
