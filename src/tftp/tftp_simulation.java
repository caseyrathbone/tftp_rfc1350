
package tftp;

import java.io.IOException;

public class tftp_simulation {
    public static void main( String[] args ) {
        if (args.length == 1) {        
            int port = 1234;
            String filename = args[0];
            
            try {
                new Thread(new tftp_masterserver(port)).start();
                new Thread(new tftp_client("127.0.0.1", port, filename)).start();
            }
            catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }
        else {
            System.out.println("\nUsage:\n\tjava -cp ./bin tftp.tftp_simulation <file>\n");
        }
    }
}