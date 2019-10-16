import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class DirProducer extends Thread{

    private final LinkedList<File> dirQueue;
    private final Lock lock;
    private File curFile;
    private final Condition isEmpty;

    public DirProducer(String dir, LinkedList<File> queue, Lock lock, Condition isEmpty){
        dirQueue = queue;
        this.lock = lock;
        curFile = new File(dir);
        this.isEmpty = isEmpty;
    }

    @Override
    public void run() {
        checkDir(curFile);
    }

    private void checkDir(File curFile) {
        if(curFile.isDirectory()){
            lock.lock();
            dirQueue.add(curFile);
            isEmpty.signal();
            lock.unlock();
            String[] files = curFile.list();
            for (String file : files) {
                File tmpFile = new File(file);
                checkDir(tmpFile);
            }
        }
    }
}
