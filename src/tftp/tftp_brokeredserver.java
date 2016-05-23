package tftp;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class tftp_brokeredserver implements Runnable {
    private String filename;
    private final tftp_protocol_layer tpm;
    
    public tftp_brokeredserver(String dest, int destPort, String filename) throws IOException {
        this.filename = filename;
        tpm = new tftp_protocol_layer("Brokered", dest, destPort); 
    }
    
    public void run() {
        try {
            FileInputStream fis = new FileInputStream(filename);
            int defaulBlockSize = 512;
            byte[] block = new byte[defaulBlockSize];
            int numOfBytesRead = 0;
            
            do {
                numOfBytesRead = fis.read(block);
                if (numOfBytesRead != defaulBlockSize && numOfBytesRead != -1) {
                    byte[] newBuf = new byte[numOfBytesRead];
                    newBuf = Arrays.copyOfRange(block, 0, numOfBytesRead);
                    block = newBuf;
                }
                
                // Need a better method for handling timeouts and blocknum errors.
                while(!tpm.sendDataPkt(block));
            } while (numOfBytesRead == 512);
            
            // Corner case that occurs when filesize is multiple of 512.
            if (numOfBytesRead == -1) {
                block = new byte[0];
                tpm.sendDataPkt(block);
            }
            
            fis.close();
            tpm.closeConnection();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}