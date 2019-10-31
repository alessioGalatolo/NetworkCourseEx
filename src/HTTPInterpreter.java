import java.io.*;
import java.util.Date;
import java.util.StringTokenizer;

public class HTTPInterpreter {

    public static HTTPMessage stringToHTTP(String s, OutputStream outputStream){
        if(s == null)
            return null;

        StringTokenizer stringTok = new StringTokenizer(s);
        switch (stringTok.nextToken()){
            case "GET":
                try {
                    String filename = stringTok.nextToken().substring(1); //removing first '/'
                    File file = new File(filename);
                    FileInputStream fr;
                    try {
                        fr = new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        outputStream.write("HTTP/1.1 404".getBytes());
                        return new HTTPMessage(RequestType.GET, false);
                    }
                    //Header
                    outputStream.write("HTTP/1.1 200 OK\r\n".getBytes());
                    outputStream.write(("Last-modified: " + new Date(file.lastModified()) + "\r\n").getBytes());
                    outputStream.write(("Content-Type: " + getFileType(file.getName()) + "\r\n").getBytes());
                    outputStream.write(("Content-Length: " + file.length() + "\r\n").getBytes());
                    outputStream.write("\r\n".getBytes()); //end of header

                    byte[] byteArray = new byte[Consts.ARRAY_INIT_SIZE];
                    int byteRead = 0;
                    while ((byteRead = fr.read(byteArray, 0, Consts.ARRAY_INIT_SIZE)) != -1){
                        outputStream.write(byteArray, 0, byteRead);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new HTTPMessage(RequestType.GET);
            case "HEAD":
                try {
                    String filename = stringTok.nextToken().substring(1); //removing first '/'
                    File file = new File(filename);
                    FileInputStream fr;
                    try {
                        fr = new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        outputStream.write("HTTP/1.1 404".getBytes());
                        return new HTTPMessage(RequestType.HEAD, false);
                    }
                    //Header
                    outputStream.write("HTTP/1.1 200 OK\r\n".getBytes());
                    outputStream.write(("Last-modified: " + new Date(file.lastModified()) + "\r\n").getBytes());
                    outputStream.write(("Content-Type: " + getFileType(file.getName()) + "\r\n").getBytes());
                    outputStream.write(("Content-Length: " + file.length() + "\r\n").getBytes());
                    outputStream.write("\r\n".getBytes()); //end of header
                }catch (IOException e){
                    e.printStackTrace();
                    return new HTTPMessage(RequestType.HEAD, false);
                }
                //TODO: to add other request types
        }
        return null;
    }

    private static String getFileType(String name) {
        switch (name.substring(name.length() - 3)){
            case "jpg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "txt":
                return "text/plain";
                //TODO: add other file formats
        }
        return "text/plain";
    }


    public static class HTTPMessage {

        private RequestType requestType;
        private boolean successfulOutcome = true;

        public HTTPMessage(RequestType rt){
            requestType = rt;
        }

        public HTTPMessage(RequestType rt, boolean successfulOutcome){
            requestType = rt;
            this.successfulOutcome = successfulOutcome;
        }

        public RequestType getRequestType(){
            return requestType;
        }

        public boolean getSuccessfulOutcome(){
            return successfulOutcome;
        }
    }
}
