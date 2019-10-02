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
        this.maxPeopleSecondArea = maxPeopleSecondArea; //set the number
        ArrayBlockingQueue<Runnable> secondAreaQueue = new ArrayBlockingQueue<>(maxPeopleSecondArea);

        if(keepAliveTime == 0) {//no branch shutdown
            branches = new ThreadPoolExecutor(4, 4, keepAliveTime, TimeUnit.MILLISECONDS, secondAreaQueue);
        }else { //branch shutdown
            branches = new ThreadPoolExecutor(1, 4, keepAliveTime, TimeUnit.MILLISECONDS, secondAreaQueue);
        }
        //branches.prestartAllCoreThreads(); //start all worker threads
        mainAreaQueue = Objects.requireNonNullElseGet(initQueue, LinkedBlockingQueue::new); //(REQUIRES JAVA 9) if null queue is passed it creates a new one

        System.out.print("Active threads on construct: ");
        System.out.println(Thread.activeCount());
    }

    //opens the postal office, now the people will be loaded into the internal queue and it is available a continuous flow
    public void open(){
        System.out.print("Active threads before thread activation: ");
        System.out.println(Thread.activeCount());
        officeThread = new Thread(new OfficeTask());
        System.out.println(branches.getActiveCount());
        officeThread.start();
        System.out.print("Active threads on thread activation: ");
        System.out.println(Thread.activeCount());

    }

    public void getOpenBranches(){
        System.out.println("Active Threads: " + branches.getActiveCount() + "\t poolsize: " + branches.getPoolSize());
    }


    //closes the postal office
    public void close(){
        officeThread.interrupt();
        System.out.println("Shutting down office thread");

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
                if((branches.getQueue().size() < maxPeopleSecondArea || branches.getPoolSize() < branches.getMaximumPoolSize()) && !mainAreaQueue.isEmpty()) {
                    try {
                        branches.execute(mainAreaQueue.take());
                    } catch (InterruptedException e) {
                        branches.shutdown();
                        System.out.println("Shutting down threadpool");
                        return;
                    }
                }
            }

            branches.shutdown();
            System.out.println("Shutting down threadpool");
        }
    }

}
