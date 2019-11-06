package HTTP_file_tranfer;

import java.io.*;
import java.util.Date;
import java.util.StringTokenizer;

//class to interpret basic HTTP strings
public class HTTPInterpreter {

    //reads the string and does basic functions such as get, head, ecc..
    //returns a custom class, HTTPMessage, it represents the status of the request (eg: request type, request outcome, ecc..)
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

                    //checks file existence
                    try {
                        fr = new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        outputStream.write("HTTP/1.1 404".getBytes());
                        return new HTTPMessage(RequestType.GET, Outcome.UNSUCCESSFUL);
                    }


                    //Header
                    outputStream.write("HTTP/1.1 200 OK\r\n".getBytes());
                    outputStream.write(("Last-modified: " + new Date(file.lastModified()) + "\r\n").getBytes());
                    outputStream.write(("Content-Type: " + getFileType(file.getName()) + "\r\n").getBytes());
                    outputStream.write(("Content-Length: " + file.length() + "\r\n").getBytes());
                    outputStream.write("\r\n".getBytes()); //end of header

                    //arrays used to read and write
                    byte[] byteArray = new byte[Consts.ARRAY_INIT_SIZE];
                    int byteRead = 0;
                    while ((byteRead = fr.read(byteArray, 0, Consts.ARRAY_INIT_SIZE)) != -1){ //reads file
                        outputStream.write(byteArray, 0, byteRead); //sends bytes to client
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
                        return new HTTPMessage(RequestType.HEAD, Outcome.UNSUCCESSFUL);
                    }
                    //Header
                    outputStream.write("HTTP/1.1 200 OK\r\n".getBytes());
                    outputStream.write(("Last-modified: " + new Date(file.lastModified()) + "\r\n").getBytes());
                    outputStream.write(("Content-Type: " + getFileType(file.getName()) + "\r\n").getBytes());
                    outputStream.write(("Content-Length: " + file.length() + "\r\n").getBytes());
                    outputStream.write("\r\n".getBytes()); //end of header


                }catch (IOException e){
                    e.printStackTrace();
                    return new HTTPMessage(RequestType.HEAD, Outcome.UNSUCCESSFUL);
                }
                return new HTTPMessage(RequestType.HEAD);

            case "DELETE":
                return new HTTPMessage(RequestType.DELETE, Outcome.SUSPENDED); //further actions required
            case "OPTIONS":
                return new HTTPMessage(RequestType.OPTIONS, Outcome.SUSPENDED);//further actions required
            case "PUT":
                return new HTTPMessage(RequestType.PUT, Outcome.SUSPENDED);//further actions required
            case "TRACE":
                return new HTTPMessage(RequestType.TRACE, Outcome.SUSPENDED);//further actions required
        }
        return null;
    }

    //given the file name returns the 'content-type' as a String
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
            default:
                return "text/plain";
        }
    }

    //class representing an http message
    public static class HTTPMessage {

        private RequestType requestType;
        private Outcome outcome = Outcome.SUCCESSFUL;

        public HTTPMessage(RequestType rt){
            requestType = rt;
        }

        public HTTPMessage(RequestType rt, Outcome outcome){
            requestType = rt;
            this.outcome = outcome;
        }

        public RequestType getRequestType(){
            return requestType;
        }

        public Outcome getOutcome(){
            return outcome;
        }
    }
}
