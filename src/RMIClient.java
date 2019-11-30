import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Random;


public class RMIClient {

    public static void main(String[] args) {
        CongressBooking.Session[][] congressProgram = null; //used for a print of the congress program when the application ends

        try(BufferedReader in= new BufferedReader(new InputStreamReader(System.in))) { //opening stream for console input

            Registry r = LocateRegistry.getRegistry(Integer.parseInt(args[0]));
            CongressBooking serverObject = (CongressBooking) r.lookup(Consts.CONGRESS_STUB_NAME); //get remote object

            //starting to take session to be added to remote object

            //may get the details of the session manually from the user or it may generate them
            //randomly using the 'auto' command
            System.out.println("Type exit or quit at any moment to terminate");
            while (true) {
                System.out.println("Please insert the list of the speakers separated by a space (or 'auto' for autocomplete): ");
                String message = in.readLine();

                if (message.contains("auto")) {
                    //create random data
                    System.out.println("Please write the number of sessions you want to randomly generate: ");
                    int n = Integer.parseInt(in.readLine()); //get number of data
                    Random random = new Random(GregorianCalendar.getInstance().getTimeInMillis());
                    String[] speakers;
                    int time;
                    int day;

                    //generate data
                    for (int i = 0; i < n; i++) {
                        speakers = Arrays.copyOfRange(Consts.SPEAKERS_NAMES, 0, random.nextInt(4) + 1); //array of names
                        time = random.nextInt(Consts.SESSIONS_PER_DAY);
                        day = random.nextInt(Consts.CONGRESS_DAYS);
                        serverObject.bookSession(new CongressBooking.Session(speakers, time, day));
                    }


                } else {
                    if(message.contains("quit") || message.contains("exit")){
                        //user exit
                        break;
                    }
                    //get data
                    String[] speakers = message.split(" ");
                    System.out.println("Please insert the time you want to register for: ");
                    int time = Integer.parseInt(in.readLine());
                    System.out.println("Please insert the day you want to register for: ");
                    int day = Integer.parseInt(in.readLine());

                    if (!serverObject.bookSession(new CongressBooking.Session(speakers, time, day)))
                        //session not added
                        System.out.println("Could not add speakers to the desired session (invalid session or full schedule)");

                }

                //print current schedule
                System.out.println(Arrays.deepToString(serverObject.getCongressProgram()));
                congressProgram = serverObject.getCongressProgram();
            }


        }catch (NumberFormatException ignored){
            //probably 'quit' was passed when a number was expected, ending program
        } catch (NotBoundException | IOException e){
            e.printStackTrace();
        }

        //print the congress program at the end
        if(congressProgram != null){
            printFormattedProgram(congressProgram);
        }

    }


    //method to print the formatted program from session[][]
    private static void printFormattedProgram(CongressBooking.Session[][] congressProgram) {
        for(int dayIndex = 0; dayIndex < congressProgram.length; dayIndex++){
            for(int sessionIndex = 0; sessionIndex < congressProgram[dayIndex].length; sessionIndex++){
                CongressBooking.Session currentSession = congressProgram[dayIndex][sessionIndex]; //may be null if nothing is scheduled for that session

                String[] speakers = currentSession == null ? new String[0]: currentSession.getSpeakers();

                System.out.format("%-16s", "Session " + (dayIndex + 1) + "." + (sessionIndex + 1));
                for(int speakerIndex = 0; speakerIndex < Consts.SPEAKERS_PER_SESSION; speakerIndex++){
                    if(speakerIndex < speakers.length){
                        System.out.print(speakers[speakerIndex] + "\t");
                    }else{
                        System.out.print("empty\t");
                    }
                }
                System.out.println();
            }
        }
    }
}
