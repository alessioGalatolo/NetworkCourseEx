import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class DirProducer extends Thread{

    private final LinkedList<File> dirQueue;
    private final Lock lock;
    private String dir;
    private final Condition isEmpty;
    private AtomicBoolean terminate;

    public DirProducer(String dir, LinkedList<File> queue, Lock lock, Condition isEmpty, AtomicBoolean terminate){
        dirQueue = queue;
        this.lock = lock;
        this.dir = dir;
        this.isEmpty = isEmpty;
        this.terminate = terminate;
    }

    @Override
    public void run() {
        checkDir(dir);
    }

    private void checkDir(String dir) {
        File curFile = new File(dir);
        if(curFile.isDirectory()){
            lock.lock();
            dirQueue.add(curFile);
            String[] fileNames = curFile.list();
            isEmpty.signal();
            lock.unlock();
            if(fileNames == null){
                System.out.println(curFile.getName() + " is not a directory or an I/O error occurred, producer");
            }else
            for (String fileName : fileNames) {
                checkDir(dir + "/" + fileName);
            }
        }
        terminate.set(true);
    }
}
