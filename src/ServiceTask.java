import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

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
                    FileInputStream fr = null;
                    try {
                        fr = new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return;
                    }
//                    BufferedReader bfr = new BufferedReader(fr);
                    int line;
                    try {
                        //Header
                        outputStream.write("HTTP/1.0 200 OK\r\n".getBytes());
//                        outputStream.write("Last-modified: Fri, 09 Aug 1996 14:21:40 GMT\r\n");
                        outputStream.write("Content-Type: image/jpg\r\n".getBytes());
                        outputStream.write(("Content-Length: " + file.length() + "\r\n").getBytes());
//                        System.out.println(file.length());
                        outputStream.write("\r\n".getBytes()); //end of header
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Reading" + file.getName());
                    while (true) {
                        try {
                            if ((line = fr.read()) == -1) break;
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
