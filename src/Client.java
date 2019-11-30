import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Random;


public class Client {

    public static void main(String[] args) {
        CongressBooking.Session[][] congressProgram = null; //for a final print

        try(BufferedReader in= new BufferedReader(new InputStreamReader(System.in))) {
            Registry r = LocateRegistry.getRegistry(/*args[0]*/);
            CongressBooking serverObject = (CongressBooking) r.lookup(Consts.CONGRESS_STUB_NAME);

            System.out.println("Type exit or quit at any moment to terminate");
            while (true) {

                System.out.println("Please insert the list of the speakers separated by a space (or 'auto' for autocomplete): ");
                String message = in.readLine();
                if (message.contains("auto")) {
                    //generate random numbers
                    System.out.println("Please write the number of sessions you want to randomly generate: ");
                    int n = Integer.parseInt(in.readLine());
                    Random random = new Random(GregorianCalendar.getInstance().getTimeInMillis());
                    String[] speakers;
                    int time;
                    int day;

                    for (int i = 0; i < n; i++) {
                        speakers = Arrays.copyOfRange(Consts.SPEAKERS_NAMES, 0, random.nextInt(5));
                        time = random.nextInt(Consts.SESSIONS_PER_DAY);
                        day = random.nextInt(Consts.CONGRESS_DAYS);
                        serverObject.bookSession(new CongressBooking.Session(speakers, time, day));
                    }

                    System.out.println(Arrays.deepToString(serverObject.getDaySessions()));
                    congressProgram = serverObject.getDaySessions();

                } else {
                    if(message.contains("quit") || message.contains("exit")){
                        break; //TODO: remove break
                    }
                    String[] speakers = message.split(" ");
                    System.out.println("Please insert the time you want to register for: ");
                    int time = Integer.parseInt(in.readLine());
                    System.out.println("Please insert the day you want to register for: ");
                    int day = Integer.parseInt(in.readLine());
                    if (!serverObject.bookSession(new CongressBooking.Session(speakers, time, day)))
                        System.out.println("Could not add speakers to the desired session");

                    System.out.println(Arrays.deepToString(serverObject.getDaySessions()));
                    congressProgram = serverObject.getDaySessions();
                }
            }


        }catch (NumberFormatException e){
            e.printStackTrace();
        } catch (NotBoundException | IOException e){
            e.printStackTrace();
        }

        if(congressProgram != null){
            printFormattedProgram(congressProgram);
        }
    }

    private static void printFormattedProgram(CongressBooking.Session[][] congressProgram) {
        for(CongressBooking.Session[] sessions: congressProgram){
            for(CongressBooking.Session session: sessions){
                String[] speakers = session.getSpeakers();
                System.out.print("Session " + (session.getDay() + 1) + "." + (session.getTime() + 1) + "\t");
                for(int i = 0; i < Consts.SPEAKERS_PER_SESSION; i++){
                    if(i < speakers.length){
                        System.out.print(speakers[i] + "\t");
                    }else{
                        System.out.print("empty\t");
                    }
                }
                System.out.println();
            }
        }
    }
}
