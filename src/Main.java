import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    public static void main(String[] args) {
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

        for(int i = 0; i < 100; i++){
            try {
                queue.put(new PostalTask(i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Office o1 = new Office(queue, 10);
        o1.open();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        o1.close();
    }
}
