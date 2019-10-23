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

public class MainClass {

    public static void main(String[] args) {

        createBankAccountsFile();

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Consts.N_THREADS);
        BankAccountRetriever bankAccountRetriever = new BankAccountRetriever(threadPoolExecutor);
        bankAccountRetriever.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("F24 = " + GlobalVars.F24_counter);
        System.out.println("Acc = " + GlobalVars.ACC_counter);
        System.out.println("Transaction = " + GlobalVars.TRANS_counter);
        System.out.println("Bancomat = " + GlobalVars.BANC_counter);
        System.out.println("Postal = " + GlobalVars.POST_counter);

        threadPoolExecutor.shutdown();
    }

    //creates file with bank accounts if not already existent
    private static void createBankAccountsFile() {
        FileChannel outChannel = null;
        try {
            outChannel = FileChannel.open(Paths.get(Consts.BANK_ACCOUNT_FILENAME), StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
        } catch(FileAlreadyExistsException e){
            //file exists, skip creation
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }

        BankAccount[] bankAccounts = new BankAccount[Consts.N_BANK_ACCOUNTS];
        Random randomGen = new Random(GregorianCalendar.getInstance().getTimeInMillis());

        for(int i = 0; i < Consts.N_BANK_ACCOUNTS; i++){
            bankAccounts[i] = new BankAccount(Consts.BASE_NAME + i, movementsFactory(randomGen, Consts.N_MOVEMENTS_BASE + i));
            bankAccounts[i].writeToFile(outChannel);
        }


        try {
            outChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Movement> movementsFactory(Random random, int n){
        ArrayList<Movement> movements = new ArrayList<>();

        for(int i = 0; i < n; i++){
            Date randomDate = new Date(Math.abs(random.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));
            movements.add(new Movement(randomDate, Cause.getRandomCause()));
        }

        return movements;
    }
}
