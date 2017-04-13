
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.zip.CRC32;

/**
 *
 * @author Jorge
 */
public class Ex2Client {

    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket("codebank.xyz", 38102)) {
            byte a;
            byte b;
            byte[] container = new byte[100];
            byte[] errorCode = new byte[4];
            long result;

            System.out.println("Connected...");

            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            CRC32 crc = new CRC32();

            System.out.println("Recieved bytes: \n");
            
            for (int i = 0; i < 100; i++) {
                a = (byte) is.read();
                a = (byte) (a << 4);
                b = (byte) is.read();
                a = (byte) (a + b);
                container[i] = a;

                if (((i + 1) % 10) != 0) {
                    
                    System.out.print(container[i] + "\t");
                    
                } else {
                    
                    System.out.print(container[i] + "\t");
                    System.out.println();
                    
                }
                
            }

            crc.update(container);
            result = crc.getValue();

            System.out.println("\nGenerated CRC32: " + result);

            os.write((byte) (result >>> 24));
            os.write((byte) (result >>> 16));
            os.write((byte) (result >>> 8));
            os.write((byte) (result));

            if (is.read() == 1) {
                
                System.out.println("Response good.");
                
            } else {
                
                System.out.println("Alignment failed!");
                
            }

            socket.close();
            System.out.println("Disconnected from server.");
        }
    }
}
