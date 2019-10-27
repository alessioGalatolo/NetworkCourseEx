package Bank_Accounts;

import java.util.Random;


//enum for the cause of the movement, includes a method for a random generation of causes
public enum Cause {
    TRANSFER,
    ACCREDITATION,
    POSTAL,
    F24,
    BANCOMAT;

    private static Random random = new Random();

    public static Cause getRandomCause() {
        return values()[random.nextInt(values().length)];
    }

}
