package tftp;

import java.nio.ByteBuffer;

public class buffer_base {
    private ByteBuffer buf;
    
    public buffer_base() {}
    
    public buffer_base(int buffersize) {
        byte[] bytes = new byte[buffersize];
    }
    
    protected void initialize(byte[] bytes) {
        buf = ByteBuffer.wrap(bytes);
    }
    
    protected void allocate_buffer(int length) {
        buf = ByteBuffer.allocate(length);
    }
    
    protected short getShort(int index) {
        return buf.getShort(index);
    }
    
    protected void putShort(int index, short s) {
        buf.putShort(index, s);
    }
    
    protected void putString(int index, String s) {
        byte[] byteString = s.getBytes();
        putBytes(index, byteString);
        buf.put(index + byteString.length, (byte)0x00); // Terminate string with 0x00
    }
    
    protected void putBytes(int index, byte[] bytes) {
        for (byte b: bytes) {
            buf.put(index, b);
            index++;
        }
    }
    
    // Method for debugging raw bytes.
    protected void printData() {
        for (byte b: buf.array()) {
            System.out.printf("0x%02X ", b);
        }
        System.out.printf("\n");
    }
    
    protected byte[] getRawPacket() {
        return buf.array();
    }
}

/*
TFTP Formats
   Type   Op #     Format without header
   
          2 bytes    string   1 byte     string   1 byte
          -----------------------------------------------
   RRQ/  | 01/02 |  Filename  |   0  |    Mode    |   0  |
   WRQ    -----------------------------------------------
   
        These must be built in sequential order b/c there isn't any easy way to determine filename length
        Also, depending on MTU of network if filename is too long it might break the UDP protocol (?)
   
          2 bytes    2 bytes       n bytes
          ---------------------------------
   DATA  | 03    |   Block #  |    Data    |
          ---------------------------------
          
          2 bytes    2 bytes
          -------------------
   ACK   | 04    |   Block #  |
          --------------------
          
          2 bytes  2 bytes        string    1 byte
          ----------------------------------------
   ERROR | 05    |  ErrorCode |   ErrMsg   |   0  |
          ----------------------------------------        
*/

class tftp_read_pkt extends buffer_base {
    public tftp_read_pkt(String filename, String mode) {
        //super((short) 1, filename, mode);
        short opcode = 1;
        allocate_buffer(2 + 2 + (1 * (filename.length() - 1 + mode.length() - 1) + 2));
        putShort(0, opcode);
        putString(2, filename);
        putString(2 + (1 * filename.length() - 1) + 2, mode);
    }
}

class tftp_write_pkt extends buffer_base {
    public tftp_write_pkt(String filename, String mode) {
        //super((short) 2, filename, mode);
        short opcode = 2;
        allocate_buffer(2 + 2 + (filename.length() - 1 + mode.length() - 1) + 2);
        putShort(0, opcode);
        putString(2, filename);
        putString(2 + (1 * filename.length() - 1) + 2, mode);
    }
}

class tftp_data_pkt extends buffer_base {
    public tftp_data_pkt(short blocknum, byte[] data) {
        //super((short) 3, blocknum, data);
        short opcode = 3;
        allocate_buffer(2 + 2 + data.length);
        putShort(0, opcode);
        putShort(2, blocknum);
        putBytes(4, data);
    }
}

class tftp_ack_pkt extends buffer_base {
    public tftp_ack_pkt(short blocknum) {
        //super((short) 4, blocknum);
        short opcode = 4;
        allocate_buffer(4);
        putShort(0, opcode);
        putShort(2, blocknum);
    }
}

class tftp_error_pkt extends buffer_base {
    public tftp_error_pkt(short errorcode, String errormessage) {
        //super((short) 5, errorcode, errormessage);
        short opcode = 5;
        allocate_buffer(2 + 2 + (errormessage.length() - 1) + 1);
        putShort(0, opcode);
        putShort(2, errorcode);
        putString(4, errormessage);
    }
}