//Alessio Galatolo 564857
//For the convertion JSON-java the lib GSON has been used


import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class MainClass {

    public static void main(String[] args) {

        createBankAccountsFile();

        FileChannel inChannel = null;

        try {

            inChannel = FileChannel.open(Paths.get(Consts.BANK_ACCOUNT_FILENAME), StandardOpenOption.READ);
            System.out.println(BankAccount.readFromFile(inChannel));

        } catch (IOException e) {
            e.printStackTrace();
        }


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
