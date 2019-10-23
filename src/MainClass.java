//Alessio Galatolo 564857
//For the convertion JSON-java the lib GSON has been used

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainClass {

    public static void main(String[] args) {
        //accepts input for number of bank accounts and number of movements


        //checks for the argument
        try {
            GlobalVars.N_BANK_ACCOUNTS = Integer.parseInt(args[0]);
            GlobalVars.N_MOVEMENTS_BASE = Integer.parseInt(args[1]);
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("No init parameters found, using default ones");
        }

        createBankAccountsFile();

        System.out.println("Started movements causes count");
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Consts.N_THREADS);
        BankAccountRetriever bankAccountRetriever = new BankAccountRetriever(threadPoolExecutor); //creates the producer
        bankAccountRetriever.run(); //runs in main thread so that main methods gets back control only when all task has been queued

        //shuts down thread pool, all task already queued will be competed
        threadPoolExecutor.shutdown();

        //waits for all tasks to complete
        try {
            threadPoolExecutor.awaitTermination(10, TimeUnit.SECONDS); //passive wait
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //prints results
        System.out.println("F24 = " + GlobalVars.F24_counter);
        System.out.println("Accreditation = " + GlobalVars.ACC_counter);
        System.out.println("Transaction = " + GlobalVars.TRANS_counter);
        System.out.println("Bancomat = " + GlobalVars.BANC_counter);
        System.out.println("Postal = " + GlobalVars.POST_counter);
    }

    //creates file with bank accounts if not already existent
    private static void createBankAccountsFile() {
        FileChannel outChannel = null;

        //first checks file existence
        try {
            outChannel = FileChannel.open(Paths.get(Consts.BANK_ACCOUNT_FILENAME), StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
        } catch(FileAlreadyExistsException e){
            //file exists, skip creation
            System.out.println("Found file with list of Bank Accounts, skipping creation");
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }

        //file not found, creating one
        System.out.println("No file found with the list of bank accounts, starting creation");
        BankAccount[] bankAccounts = new BankAccount[GlobalVars.N_BANK_ACCOUNTS]; //arrays of bankAccounts
        Random randomGen = new Random(GregorianCalendar.getInstance().getTimeInMillis());

        for(int i = 0; i < GlobalVars.N_BANK_ACCOUNTS; i++){
            bankAccounts[i] = new BankAccount(Consts.BASE_NAME + i, movementsFactory(randomGen, GlobalVars.N_MOVEMENTS_BASE + i)); //creates obj
            bankAccounts[i].writeToFile(outChannel); //writes obj to file
        }


        try {
            outChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("File Created");
    }


    //a random generator for the movement list
    private static List<Movement> movementsFactory(Random random, int n){
        ArrayList<Movement> movements = new ArrayList<>();

        for(int i = 0; i < n; i++){
            Date randomDate = new Date(Math.abs(random.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));
            movements.add(new Movement(randomDate, Cause.getRandomCause()));
        }

        return movements;
    }
}
