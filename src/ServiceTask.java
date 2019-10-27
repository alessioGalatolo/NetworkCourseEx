import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

public class ServiceTask implements Runnable {

    private final Socket socket;
    private BufferedReader inputStream;
    private BufferedWriter outputStream;

    public ServiceTask(Socket socket) throws IOException {
        this.socket = socket;
        inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

    }

    @Override
    public void run() {
        try {
            executeHTTPcmd(inputStream.readLine());



            socket.close();
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void executeHTTPcmd(String readLine) throws IOException {
        if(readLine != null){
            StringTokenizer stringTok = new StringTokenizer(readLine);
            switch (stringTok.nextToken()){
                case "GET":
                    String filename = stringTok.nextToken();
//                    FileChannel fileChannel = FileChannel.open(Paths.get(filename), StandardOpenOption.READ);
//                    fileChannel.
                    File file = new File(filename.substring(1));
                    FileReader fileReader = new FileReader(file);
                    char[] buf = new char[1000];
                    fileReader.read(buf, 0, 1000);
                    outputStream.write(buf, 0, 1000);
                    break;
                case "PUT":
                    break;
                case "DEL":
                    break;
            }
        }
    }
}
