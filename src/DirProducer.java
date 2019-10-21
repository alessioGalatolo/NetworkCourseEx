import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class DirProducer extends Thread{

    private final LinkedList<File> dirQueue; //queue for the dir files
    private final Lock queueLock; //queueLock to access the queue
    private String dir; //the name of the initial directory
    private final Condition queueIsEmpty; //condition to signal when the queue is not empty anymore
    private AtomicBoolean terminate; //Bool to make the consumer threads terminate

    public DirProducer(String dir, LinkedList<File> queue, Lock queueLock, Condition queueIsEmpty, AtomicBoolean terminate){
        dirQueue = queue;
        this.queueLock = queueLock;
        this.dir = dir;
        this.queueIsEmpty = queueIsEmpty;
        this.terminate = terminate;
    }

    @Override
    public void run() {
        checkDir(dir);
    }


    //rec fun to check all the subDirs
    private void checkDir(String dir) {
        File curFile = new File(dir);

        //if is dir -> put in the queue
        if(curFile.isDirectory()){
            queueLock.lock();
            dirQueue.add(curFile);

            String[] fileNames = curFile.list(); //list of files inside the directory
            queueIsEmpty.signal();//queue is not empty anymore
            queueLock.unlock();

            if(fileNames == null){
                System.out.println(curFile.getName() + " is not a directory or an I/O error occurred, producer");
            }else
            for (String fileName : fileNames) {
                //rec call
                checkDir(dir + "/" + fileName);
            }
        }
        queueLock.lock();
        terminate.set(true); //nothing to add anymore, consumers can start to shutdown
        queueIsEmpty.signalAll(); //wakes all the consumer to make them shutdown
        queueLock.unlock();
    }
}
