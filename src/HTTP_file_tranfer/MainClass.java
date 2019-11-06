package HTTP_file_tranfer;//Alessio Galatolo 564857

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MainClass {

    //File available for request: 'myfile' and 'myfile.png'
    public static void main(String[] args) {

        //threadpool for the worker threads
        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(Consts.N_THREADS);

        try(ServerSocket welcomeSocket = new ServerSocket(Consts.PORT)) {//tries to open the socket


            while (true) {
                Socket socket = welcomeSocket.accept();

                //sends request to worker thread
                threadPool.execute(new ServiceTask(socket));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
