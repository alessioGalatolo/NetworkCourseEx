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

        while(!Thread.interrupted()) {

            File dirToCheck;
            lock.lock();
            while (dirQueue.isEmpty()) {
                try {
                    isEmpty.await();
                } catch (InterruptedException e) {
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
                for (String fileName : fileNames) {
                    File tmpFile = new File(fileName);
                    if (tmpFile.isFile())
                        System.out.println(fileName);
                }
            }

        }
    }
}
