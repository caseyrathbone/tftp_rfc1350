package tftp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class tftp_client implements Runnable {
    private final tftp_protocol_layer tpm;
    private String filename;

    public tftp_client(String dest, int destPort, String filename) throws IOException {
        this.tpm = new tftp_protocol_layer("Client", dest, destPort);
        this.filename = filename;
    }
    
    public void run() {
        try {
            tpm.sendRRQ(filename);
            FileOutputStream fos = new FileOutputStream(filename + "-clientfile");
            byte[] data = null;
            
            do {
                data = tpm.receiveData();
                fos.write(data);
            } while (data.length >= 512);
            
            fos.close();
            tpm.closeConnection();    
        }    
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}