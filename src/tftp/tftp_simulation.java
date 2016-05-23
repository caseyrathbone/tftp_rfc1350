
// Create enum for opcodes
// Create enum for tftp node types
// Create enum for modes
// Refactor logging and error handling
// -- Complete UnknownOpcodeException
// -- Finish incorrect port error handling

// What if we wanted to make it more general where you could make any request and it would ask a
// service locator to find a DAO for that and start sending it back
// We could make it very generic where any request could be parsed into a database request
// Right now we have RRQ and WRQ but what if instead of a hard disk they were making requests to a DB

// Things I would do differently
// 1. Develop an extensive set of regression tests and expose command line arguments to allow for additional testing beyond functional block tests.
// 2. Identify a machine or device to test across different operating architectures (20-bit DEC vs. 32-bit x86)
// 3. Include more thorough logging and standardize my exception captures (if I don't detect a bug or log it then it is very difficult to debug (ie determine the state of everything when it broke))
// 4. Create an interface for logging and have it write to a different system or persistent storage.
// 5. Include performance metrics data to provide a rough guideline for health of the disk IO/network connectivity

package tftp;

import java.io.IOException;

public class tftp_simulation {
    public static void main( String[] args ) {        
        int port = 1234;
        String filename = args[0];
        
        try {
            new Thread(new tftp_masterserver(port)).start();
            new Thread(new tftp_client("127.0.0.1", port, filename)).start();
        }
        catch(IOException e) {
            System.out.println("Uh oh.. : " + e.getMessage());
        }
    }
}