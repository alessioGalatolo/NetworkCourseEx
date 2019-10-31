//Alessio Galatolo 564857

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MainClass {

    public static void main(String[] args) {
        //checks for the existence of the argument
        try {

        }catch (ArrayIndexOutOfBoundsException e){
            //no arguments
            System.out.println("No init parameters found");
        }

        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(Consts.N_THREADS);

        try(ServerSocket welcomeSocket = new ServerSocket(Consts.PORT)) {


            while (true) {
                Socket socket = welcomeSocket.accept();
                threadPool.execute(new ServiceTask(socket));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
