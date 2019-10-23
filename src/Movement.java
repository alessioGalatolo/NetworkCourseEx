import java.util.Date;

public class Movement {
    private Date date;
    private Cause cause;

    public Movement(Date date, Cause cause){
        this.date = date;
        this.cause = cause;
    }

    public Cause getCause() {
        return cause;
    }
}
