package tftp;

import java.util.Arrays;

public class tftp_packet extends buffer_base {
    private short opcode;
    private short blocknum;
    private String filename;
    private String mode;
    private byte[] data;
    private short errorcode;
    private String errormessage; 

    public tftp_packet() {}    
    
    public tftp_packet(byte[] pkt) {
        create_packet(pkt);
    }
    
    public void create_packet(byte[] pkt) {
        initialize(pkt);
        
        opcode = getShort(0);//buf.getShort(0);
        switch(opcode) {
            case (short)1:
                int splitzero = 0;
                int i = 0;
                
                // Functional but not the most elegant way to determine the 0x00 split between filename and mode.
                while (splitzero == 0 && i < pkt.length) {
                    if (pkt[i] == (byte)0x00) {
                        splitzero = i;
                    }
                    i++;
                }
                
                // if (i == pkt.length) { throw new Exception("Error parsing RRQ packet"); }
                
                filename = new String(Arrays.copyOfRange(pkt, 2, splitzero));
                mode = new String(Arrays.copyOfRange(pkt, splitzero + 1, pkt.length));
                break;
                
            case (short)2:
                // throw new Exception("NOT IMPLEMENTED");
                break;
                
            case (short)3:
                blocknum = getShort(2);
                data = Arrays.copyOfRange(pkt, 4, pkt.length);
                break;
                
            case (short)4:
                blocknum = getShort(2);
                break;
                
            case (short)5:
                blocknum = getShort(2);
                errormessage = new String(Arrays.copyOfRange(pkt, 4, pkt.length)); 
                break;
                
            default:
                // throw new UnknownOpcodeException();
        }
    }

    public short getOpcode() { return opcode; }
    public String getFilename() { return filename; }
    public String getMode() { return mode; }    
    public short getBlocknum() { return blocknum; }
    public byte[] getData() { return data; }
    public short getErrorCode() { return errorcode; }
    public String getErrorMessage() { return errormessage; }
    public byte[] getPacket() { return getRawPacket(); }
}