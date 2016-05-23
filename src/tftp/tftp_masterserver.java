package tftp;

import java.io.IOException;
import java.lang.Exception;

public class tftp_masterserver implements Runnable {
    private final tftp_protocol_layer tpm;    

    public tftp_masterserver(int listeningPort) throws IOException {
        this.tpm = new tftp_protocol_layer(listeningPort);
    }

    public void run() {
        try {
            tftp_packet receivedPkt = tpm.listen();
            
            switch(receivedPkt.getOpcode()) { 
                case (short) 1: //case "RRQ":
                    new Thread(new tftp_brokeredserver(tpm.getHostName(), tpm.getPort(), receivedPkt.getFilename())).start();
                    break;
                
                //case (short) 2: // "WRQ":
                
                default:
                    // Reply with error packet.
                    throw new Exception("NOT IMPLEMENTED!");
            }
            
            tpm.closeConnection();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}