package File_Crawler;

import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;


//class of the dir producer. It looks for the files inside the directories in
//dirQueue until terminating is set to true (generally by the producer) or it get interrupted while waiting.
//before terminating it checks the emptiness of the queue
public class DirConsumer extends Thread {

    private final LinkedList<File> dirQueue; //list of dirs to check
    private final Lock queueLock; //queueLock to access the queue
    private final Condition queueIsEmpty; //condition to handle the emptiness of the queue
    private AtomicBoolean terminating; //var to make the thread return

    public DirConsumer(LinkedList<File> queue, Lock queueLock, Condition queueIsEmpty, AtomicBoolean terminate){
        dirQueue = queue;
        this.queueLock = queueLock;
        this.queueIsEmpty = queueIsEmpty;
        terminating = terminate;
    }

    @Override
    public void run() {

        while(true) {
            //exits only when terminating is true and queue is empty, or when interrupted

            File dirToCheck;
            queueLock.lock();


            //waits for a directory to check
            while (dirQueue.isEmpty()) {

                //checks shutdown conditions
                if(terminating.get()){
                    queueLock.unlock();
                    return;
                }

                //waits for an element in the queue
                try {
                    queueIsEmpty.await();
                } catch (InterruptedException e) {
                    //shutting down
                    queueLock.unlock();
                    terminating.set(true);
                    return;
                }
            }

            //gets dir file
            dirToCheck = dirQueue.getFirst();
            dirQueue.removeFirst();
            queueLock.unlock();


            if (dirToCheck.isFile()) {
                System.out.println("Error -> Consumer: trying to open a non dir");
            } else {
                //extracts the file names
                String[] fileNames = dirToCheck.list();

                if(fileNames == null){
                    //error
                    System.out.println(dirToCheck.getName() + " is not a directory or an I/O error occurred");
                }else
                    for (String fileName : fileNames) {
                        //for each file in the directory prints the filename
                        File tmpFile = new File(dirToCheck.getAbsolutePath() + "/" + fileName);
                        if (tmpFile.isFile()) {
                            System.out.println(fileName);
                        }
                    }
            }

            //if one thread is interrupted it sets the terminating var to make all the consumers terminate
            if(interrupted())
                terminating.set(true);

        }
    }
}
