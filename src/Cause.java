import java.util.Random;

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
