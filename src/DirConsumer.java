import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class DirConsumer extends Thread {

    private final LinkedList<File> dirQueue;
    private final Lock lock;
    private final Condition isEmpty;

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
            if(isInterrupted() && dirQueue.isEmpty()){
                //shutting down
                lock.unlock();
                return;
            }
            while (dirQueue.isEmpty()) {
                try {
                    isEmpty.await();
                } catch (InterruptedException e) {
                    lock.unlock();
                    return;
                }
            }
            dirToCheck = dirQueue.getFirst();
            dirQueue.removeFirst();
            lock.unlock();

//            System.out.println(dirToCheck.getName());

            if (dirToCheck.isFile()) {
                System.out.println("Error -> Consumer: trying to open a non dir");
            } else {
                String[] fileNames = dirToCheck.list();
                for (String fileName : fileNames) {
                    File tmpFile = new File(dirToCheck.getAbsolutePath() + "/" + fileName);
                    if (tmpFile.isFile())
                        System.out.println(this.getName() + " prints " + fileName);
                    else if(!tmpFile.exists())
                        System.out.println("error");
                }
            }

        }
    }
}
