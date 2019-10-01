import java.util.Objects;
import java.util.concurrent.*;

public class Office {

    private ThreadPoolExecutor branches; //(ThreadPoolExecutor) Executors.newFixedThreadPool(4); //the worker threads
    private LinkedBlockingQueue<Runnable> mainAreaQueue; //queue for the bigger area
    private int maxPeopleSecondArea; //max number of people for the smaller area
    private Thread officeThread; //an internal thread which simulates the office

    //constructor without the ability to shutdown a branch
    public Office(int maxPeopleSecondArea, LinkedBlockingQueue<Runnable> initQueue){
        this(maxPeopleSecondArea, initQueue, 0L);
    }

    //constructor with a parameter to specify how much time to keep a thread alive
    public Office(int maxPeopleSecondArea, LinkedBlockingQueue<Runnable> initQueue, long keepAliveTime){

        ArrayBlockingQueue<Runnable> secondAreaQueue = new ArrayBlockingQueue<>(maxPeopleSecondArea);
        branches = new ThreadPoolExecutor(4, 4, keepAliveTime, TimeUnit.MILLISECONDS, secondAreaQueue); //TODO: probably need to lower corepoolsize to 0 in order to make keepAliveTime effective
        mainAreaQueue = Objects.requireNonNullElseGet(initQueue, LinkedBlockingQueue::new); //(REQUIRES JAVA 9) if null queue is passed, it is ignored
        branches.prestartAllCoreThreads(); //start all worker threads
        this.maxPeopleSecondArea = maxPeopleSecondArea; //set the number

    }

    //opens the postal office, now the people will be loaded into the internal queue and it is available a continuous flow
    public void open(){
        officeThread = new Thread(new OfficeTask());
        officeThread.start();

    }

    //closes the postal office
    public void close(){
        officeThread.interrupt();
        System.out.println("Shutting down");

    }

    //one person comes in the postal office
    public void addPerson(PostalTask pt){
        try {
            mainAreaQueue.put(pt);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private class OfficeTask implements Runnable {
        @Override
        public void run() {
            while(!Thread.interrupted()){
                if(branches.getQueue().size() < maxPeopleSecondArea && !mainAreaQueue.isEmpty()) {
                    try {
                        branches.execute(mainAreaQueue.take());
                    } catch (InterruptedException e) {
                        branches.shutdown();
                        return;
                    }
                }
            }

            branches.shutdown();
        }
    }

}
