import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    public static void main(String[] args) {

        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(); //queue of people entering postal office at opening

        //init person task
        for(int i = 0; i < 100; i++){
            try {
                queue.put(new PostalTask(i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int k = 2; //max people in the second, smaller, area
        Office busyOffice = new Office(k, queue); //office with all threads always open
        Office lazyOffice = new Office(k, queue, 500); //office where the threads shutdown after 500ms

        busyOffice.open();
        lazyOffice.open();


        System.out.print("Active threads before first add cycle: ");
        System.out.println(Thread.activeCount());
        for(int i = 0; i < 100; i++) {
            busyOffice.addPerson(new PostalTask(i));
            lazyOffice.addPerson(new PostalTask(i));
        }
        System.out.print("Active threads after first add cycle: ");
        System.out.println(Thread.activeCount());


        //waits for some threads to be terminated
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print("Active threads after sleep: ");
        System.out.println(Thread.activeCount());


        for(int i = 0; i < 100; i++) {
            busyOffice.addPerson(new PostalTask(i));
            lazyOffice.addPerson(new PostalTask(i));
        }
        System.out.print("Active threads after second add cycle: ");
        System.out.println(Thread.activeCount());




        busyOffice.close();
        lazyOffice.close();
        System.out.print("Active at the end: ");
        System.out.println(Thread.activeCount()); //most threads should be still active

    }
}
