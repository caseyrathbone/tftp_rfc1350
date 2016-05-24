# tftp_rfc1350
TFTP Protocol

This project is an initial draft of TFTP Protocol (https://www.ietf.org/rfc/rfc1350.txt)

To build:
  ./make.sh
  
To run:
  ./run.sh <file_to_tranfer>
  
To run the simulation that creates a simple text file and runs the tftp_simulation:
  ./simulation.sh
  
Opens:
  1. Create test framework to run test cases
    - Determine a test max MTU of protocol 
    - (Note, the udp_connection_layer implementation only reads packets upto 2048 bytes what if a filename and mode exceed this limit and parsing fails...)
  2. Move termination of transfer detection into tftp_protocol_layer
    - Implement a data object abstraction for the tftp_protocol_layer
  3. Determine if moving termination of detection allows the tftp_brokeredserver and tftp_client to be merged into a single object
  4. Update all exception handling to more specific exceptions objects and include better details
  5. Implement a standardized logging methodology
  6. Refactor tftp_packet.create_packet()
    - Does it really need buffer_base and access to the byte[]... seems somewhat redundant functionality
  7. Create enums for tftp packet opcodes and transmission modes
  8. Refactor several code blocks noted within code
  
