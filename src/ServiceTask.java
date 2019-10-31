import java.io.*;
import java.net.Socket;

public class ServiceTask implements Runnable {

    private final Socket socket;
    private BufferedReader inputStream;
    private OutputStream outputStream;

    public ServiceTask(Socket socket) throws IOException {
        this.socket = socket;
        inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputStream = socket.getOutputStream();

    }

    @Override
    public void run() {
        try {
            HTTPInterpreter.HTTPMessage outcome = HTTPInterpreter.stringToHTTP(inputStream.readLine(), outputStream);
            System.out.println("Received " + outcome.getRequestType().toString() + " request, the outcome was " + (outcome.getSuccessfulOutcome()? "successful" : "unsuccessful"));
            socket.close();
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
