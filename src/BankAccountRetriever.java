import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ThreadPoolExecutor;


//class that reads BankAccount objs from files and adds it to "bankAccountOperator" as a task in the thread pool to be worked on.
public class BankAccountRetriever extends Thread{

    private ThreadPoolExecutor threadPoolExecutor;

    public BankAccountRetriever(ThreadPoolExecutor threadPoolExecutor){
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Override
    public void run() {
        try {

            FileChannel inChannel = FileChannel.open(Paths.get(GlobalVars.BANK_ACCOUNT_FILENAME), StandardOpenOption.READ);

            for(int i = 0; i < GlobalVars.N_BANK_ACCOUNTS; i++) {
                threadPoolExecutor.execute(new BankAccountOperator(BankAccount.readFromFile(inChannel)));
            }

            inChannel.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
