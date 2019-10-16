import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainClass {

    public static void main(String[] args) {
        //input: filepath

        final int k = 10;

        File mainDir = new File(args[0]);
        if(mainDir.isFile()){
            System.out.println("The path is not a directory");
            return;
        }

        LinkedList<File> fileQueue = new LinkedList<>();
        Lock lock = new ReentrantLock();
        Condition isEmpty = lock.newCondition();

        DirConsumer[] dirConsumers = new DirConsumer[10];
//        for(DirConsumer dirConsumer: dirConsumers){
//            dirConsumer = new DirConsumer(fileQueue, lock, isEmpty);
//            dirConsumer.start();
//        }
        DirConsumer dc = new DirConsumer(fileQueue, lock, isEmpty);
        dc.start();

        DirProducer dirProducer = new DirProducer(args[0], fileQueue, lock, isEmpty);
        dirProducer.run(); //runs in main thread

        dirProducer.interrupt();
//        for(DirConsumer dirConsumer: dirConsumers){
//            dirConsumer.interrupt();
//        }
        //System.out.println(fileQueue);

    }
}
