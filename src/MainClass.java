import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainClass {

    public static void main(String[] args) {
        //input: filepath


        File mainDir = new File(args[0]);
        if(mainDir.isFile()){ //checks if input is a dir
            System.out.println("The path is not a directory");
            return;
        }

        LinkedList<File> fileQueue = new LinkedList<>(); //the queue to put the directory to print
        Lock lock = new ReentrantLock(); //the lock for producer/consumer
        Condition isEmpty = lock.newCondition(); //condition to represent the emptiness of the queue
        AtomicBoolean terminate = new AtomicBoolean();
        terminate.set(false);

        DirConsumer[] dirConsumers = new DirConsumer[Consts.N_CONSUMER]; //Class consts has the value for the number of consumer
        for(int i = 0; i < Consts.N_CONSUMER; i++){
            dirConsumers[i] = new DirConsumer(fileQueue, lock, isEmpty, terminate); //init
            dirConsumers[i].start();
        }

        DirProducer dirProducer = new DirProducer(args[0], fileQueue, lock, isEmpty, terminate);
        dirProducer.run(); //runs in main thread. When the control comes back to the main it means its time to interrupt the consumer threads


//        for(DirConsumer dirConsumer: dirConsumers){
//            dirConsumer.interrupt();
//        }

        System.out.println(fileQueue);

    }
}
