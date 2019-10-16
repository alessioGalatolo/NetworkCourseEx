import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class DirProducer extends Thread{

    private final LinkedList<File> dirQueue;
    private final Lock lock;
    private String dir;
    private final Condition isEmpty;

    public DirProducer(String dir, LinkedList<File> queue, Lock lock, Condition isEmpty){
        dirQueue = queue;
        this.lock = lock;
        this.dir = dir;
        this.isEmpty = isEmpty;
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
            isEmpty.signal();
            lock.unlock();
            String[] fileNames = curFile.list();
            for (String fileName : fileNames) {
                checkDir(dir + "/" + fileName);
            }
        }
    }
}
