//Alessio Galatolo 564857

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<Patient> list = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Urgency u;
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
            //TODO
        }

        EmergencyRoom ER = new EmergencyRoom();

    }
}
