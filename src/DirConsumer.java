import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class DirConsumer extends Thread {

    private final LinkedList<File> dirQueue;
    private final Lock lock;
    private final Condition isEmpty;
    private boolean terminating = false;

    public DirConsumer(LinkedList<File> queue, Lock lock, Condition isEmpty){
        dirQueue = queue;
        this.lock = lock;
        this.isEmpty = isEmpty;
    }

    @Override
    public void run() {

        while(true) { //thread terminates only when interrupted during lock waiting

            File dirToCheck;
            lock.lock();
            if(terminating && dirQueue.isEmpty()){
                //shutting down
                lock.unlock();
                return;
            }
            while (dirQueue.isEmpty()) {
                try {
                    isEmpty.await();
                } catch (InterruptedException e) {
                    lock.unlock();
                    terminating = true;
                    return;
                }
            }
            dirToCheck = dirQueue.getFirst();
            dirQueue.removeFirst();
            lock.unlock();


            if (dirToCheck.isFile()) {
                System.out.println("Error -> Consumer: trying to open a non dir");
            } else {
                String[] fileNames = dirToCheck.list();
                if(fileNames == null){
                    System.out.println(dirToCheck.getName() + " is not a directory or an I/O error occurred");
                }else
                    for (String fileName : fileNames) {
                        File tmpFile = new File(dirToCheck.getAbsolutePath() + "/" + fileName);
                        if (tmpFile.isFile()) {
                            System.out.println(fileName);
                        }
                    }
            }
            if(interrupted())
                terminating = true;

        }
    }
}
