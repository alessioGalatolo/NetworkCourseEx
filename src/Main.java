import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.out.print("Active threads on first add cycle: ");
        System.out.println(Thread.activeCount());

        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(); //queue of people entering postal office at opening

        //init person task
        for(int i = 0; i < 100; i++){
            try {
                queue.put(new PostalTask(i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int k = 10; //max people in the second, smaller, area
        Office busyOffice = new Office(k, queue); //office with all threads always open
        Office lazyOffice = new Office(k, queue, 1000); //office where the threads shutdown after 1000ms

        busyOffice.open();
        lazyOffice.open();

        for(int i = 0; i < 100; i++) {
            System.out.print("Active threads on first add cycle: ");
            System.out.println(Thread.activeCount());
            busyOffice.addPerson(new PostalTask(i));
            lazyOffice.addPerson(new PostalTask(i));
        }

        Thread.sleep(1500);//puts some threads to sleep

        for(int i = 0; i < 100; i++) {
            System.out.print("Active threads on second add cycle: ");
            System.out.println(Thread.activeCount());
            busyOffice.addPerson(new PostalTask(i));
            lazyOffice.addPerson(new PostalTask(i));
        }

        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print("Active threads after sleep: ");
        System.out.println(Thread.activeCount());


        busyOffice.close();
        lazyOffice.close();
        Thread.sleep(1000);
        System.out.print("Active at the end: ");
        System.out.println(Thread.activeCount());

    }
}
