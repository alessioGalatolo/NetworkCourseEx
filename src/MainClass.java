//Alessio Galatolo 564857

import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainClass {

    public static void main(String[] args) {
        //input by terminal: filepath

        File mainDir; //original dir
        LinkedList<File> fileQueue = new LinkedList<>(); //the queue to put the directory to print
        Lock queueLock = new ReentrantLock(); //the lock for producer/consumer
        Condition queueIsEmpty = queueLock.newCondition(); //condition to represent the emptiness of the queue
        AtomicBoolean terminate = new AtomicBoolean();
        terminate.set(false);

        //checks for the argument
        try {
            mainDir = new File(args[0]);
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("No arguments were passed as parameters, please provide a dir path");
            return;
        }

        //checks if input is a dir
        if(mainDir.isFile()){
            System.out.println("The path is not a directory");
            return;
        }

        //array of consumer
        DirConsumer[] dirConsumers = new DirConsumer[Consts.N_CONSUMER]; //Class consts has the value for the number of consumer
        for(int i = 0; i < Consts.N_CONSUMER; i++){
            //consumer init
            dirConsumers[i] = new DirConsumer(fileQueue, queueLock, queueIsEmpty, terminate); //init
            dirConsumers[i].start();
        }

        DirProducer dirProducer = new DirProducer(args[0], fileQueue, queueLock, queueIsEmpty, terminate);
        dirProducer.start();

        //consumer thread may be terminated also with interrupt method
//        for(DirConsumer dirConsumer: dirConsumers){
//            dirConsumer.interrupt();
//        }


    }
}
