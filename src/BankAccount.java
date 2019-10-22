import com.google.gson.Gson;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;

public class BankAccount {
    private String accountHolderName;
    private List<Movement> movementList;
    private static Gson gson = new Gson();

    public BankAccount(String accountHolderName, List<Movement> movementList){
        this.accountHolderName = accountHolderName;
        this.movementList = movementList;
    }

    public void addMovement(Movement m){
        //TODO: add movement check
        movementList.add(m);
    }

    public void writeToFile(FileChannel outChannel){
        byte[] jsonObj = gson.toJson(this).getBytes();
        ByteBuffer lengthByteBuffer = ByteBuffer.allocate(Consts.INT_SIZE);
        System.out.println("obj size " + jsonObj.length);
        lengthByteBuffer.putInt(jsonObj.length);
        ByteBuffer byteBuffer = ByteBuffer.wrap(jsonObj);

        lengthByteBuffer.flip();

        while(lengthByteBuffer.hasRemaining()) {
            try {
                outChannel.write(lengthByteBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        while (byteBuffer.hasRemaining()) {
            try {
                outChannel.write(byteBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static BankAccount readFromFile(FileChannel inChannel) throws IOException {
        ByteBuffer lengthByteBuffer = ByteBuffer.allocate(Consts.INT_SIZE);

        inChannel.read(lengthByteBuffer);

        lengthByteBuffer.flip();
        ByteBuffer byteBuffer = ByteBuffer.allocate(lengthByteBuffer.getInt());

        inChannel.read(byteBuffer);
//        MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
//        buffer.load();
        gson.fromJson(Arrays.toString(byteBuffer.array()), BankAccount[].class);
        byteBuffer.clear(); // do something with the data and clear/compact it.

        return null;
    }

    @Override
    public String toString() {
        return accountHolderName + movementList;
    }
}
