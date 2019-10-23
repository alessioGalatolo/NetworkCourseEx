import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ThreadPoolExecutor;

public class BankAccountRetriever extends Thread{

    private ThreadPoolExecutor threadPoolExecutor;

    public BankAccountRetriever(ThreadPoolExecutor threadPoolExecutor){
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Override
    public void run() {
        try {

            FileChannel inChannel = FileChannel.open(Paths.get(Consts.BANK_ACCOUNT_FILENAME), StandardOpenOption.READ);

            threadPoolExecutor.execute(new BankAccountOperator(BankAccount.readFromFile(inChannel)));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
