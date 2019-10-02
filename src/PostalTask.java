public class PostalTask implements Runnable{
    //a simple task which mimics the operations of a client


    private String name = "Task";
    private int length;

    public PostalTask(int n){
        name += n;
        length = n;
    }


    @Override
    public void run() {
        //System.out.println("Starting " + name); //prints its name
        try {
            Thread.sleep(length); //sleeps
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println("Ended " + name); //ends task
    }
}
