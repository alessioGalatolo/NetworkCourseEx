import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class EchoServerWorker extends Thread{
    private SocketChannel socketChannel;

    public EchoServerWorker(SocketChannel socketChannel){
        this.socketChannel = socketChannel;
    }


    @Override
    public void run() {
        String finalString = "";
        try {
            while (true) {
                ByteBuffer inputBuffer = ByteBuffer.allocate(Consts.ARRAY_INIT_SIZE);
                socketChannel.read(inputBuffer);
                inputBuffer.flip();
                String s = new String(StandardCharsets.UTF_8.decode(inputBuffer).array());
                inputBuffer.clear();

                
                inputBuffer = ByteBuffer.wrap(s.getBytes());

                while (inputBuffer.hasRemaining()){
                    socketChannel.write(inputBuffer);
                }

            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
