package Bank_Accounts;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

//class for bank accounts
public class BankAccount {
    private String accountHolderName; //name
    private List<Movement> movementList;
    private static Gson gson = new Gson(); //class for JSON-JAVA conversion

    public BankAccount(String accountHolderName, List<Movement> movementList){
        this.accountHolderName = accountHolderName;
        this.movementList = movementList;
    }

    public void addMovement(Movement m){
        movementList.add(m);
    }

    public List<Movement> getMovementList() {
        return movementList;
    }


    //method for writing to file(java to json)
    public void writeToFile(FileChannel outChannel){
        byte[] jsonObj = gson.toJson(this).getBytes();
        ByteBuffer lengthByteBuffer = ByteBuffer.allocate(Consts.INT_SIZE); //buffer to write object size to file
        lengthByteBuffer.putInt(jsonObj.length);
        ByteBuffer byteBuffer = ByteBuffer.wrap(jsonObj); //buffer to write object

        lengthByteBuffer.flip();

        //writes obj size
        while(lengthByteBuffer.hasRemaining()) {
            try {
                outChannel.write(lengthByteBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //writes obj
        while (byteBuffer.hasRemaining()) {
            try {
                outChannel.write(byteBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //reads from file(json to java)
    public static BankAccount readFromFile(FileChannel inChannel) throws IOException {
        ByteBuffer lengthByteBuffer = ByteBuffer.allocate(Consts.INT_SIZE); //allocates a buffer to read an int

        inChannel.read(lengthByteBuffer);//reads and int representing the size of the String for the obj

        lengthByteBuffer.flip();
        ByteBuffer byteBuffer = ByteBuffer.allocate(lengthByteBuffer.getInt());

        inChannel.read(byteBuffer); //reads object

        return gson.fromJson(new String(byteBuffer.array()), BankAccount.class); //returns the converted object
    }

    @Override
    public String toString() {
        return accountHolderName;
    }
}
