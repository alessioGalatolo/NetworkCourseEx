import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    public static void main(String[] args) {

        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(); //queue of people entering postal office at opening

        //init person task
        for(int i = 0; i < 10; i++){
            try {
                queue.put(new PostalTask(i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int k = 2; //max people in the second, smaller, area
        //Office busyOffice = new Office(k, queue); //office with all threads always open
        Office lazyOffice = new Office(k, queue, 1000); //office where the threads shutdown after 100ms

        lazyOffice.getOpenBranches();
        //busyOffice.open();
        lazyOffice.open();
//
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.print("Active threads after sleep: ");
//        System.out.println(Thread.activeCount());


        for(int i = 0; i < 10; i++) {
            System.out.print("Active threads on first add cycle: ");
            System.out.println(Thread.activeCount());
            //busyOffice.addPerson(new PostalTask(i));
            lazyOffice.addPerson(new PostalTask(i));
        }
        lazyOffice.getOpenBranches();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print("Active threads after sleep: ");
        System.out.println(Thread.activeCount());
        lazyOffice.getOpenBranches();

        for(int i = 0; i < 10; i++) {
            System.out.print("Active threads on second add cycle: ");
            System.out.println(Thread.activeCount());
            //busyOffice.addPerson(new PostalTask(i));
            lazyOffice.addPerson(new PostalTask(i));
        }

        lazyOffice.getOpenBranches();



        //busyOffice.close();
        lazyOffice.close();
        System.out.print("Active at the end: ");
        System.out.println(Thread.activeCount());

    }
}
