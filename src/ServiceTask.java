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


//
            socket.close();
            inputStream.close();
//            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void executeHTTPcmd(String readLine) {
        if(readLine != null){
            StringTokenizer stringTok = new StringTokenizer(readLine);
            switch (stringTok.nextToken()){
                case "GET":
                    String filename = stringTok.nextToken();
//                    FileChannel fileChannel = FileChannel.open(Paths.get(filename), StandardOpenOption.READ);
//                    fileChannel.
                    File file = new File(filename.substring(1));
                    FileReader fr = null;
                    try {
                        fr = new FileReader(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    BufferedReader bfr = new BufferedReader(fr);
                    String line = null;
                    try {
                        outputStream.write("HTTP/1.1 200 OK\r\n");
// Header...
                        outputStream.write("Last-modified: Fri, 09 Aug 1996 14:21:40 GMT\r\n");
                        outputStream.write("\r\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    while (true) {
                        try {
                            if ((line = bfr.readLine()) == null) break;
                            System.out.println(line);
                            outputStream.write(line);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "PUT":
                    break;
                case "DEL":
                    break;
            }
        }
    }
}
