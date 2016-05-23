package tftp;

import java.io.IOException;

public class tftp_protocol_layer {
    private final IConnectionLayer cl;
    private String endpointType;
    private short blocknum = 1;

    public tftp_protocol_layer(int listeningPort) throws IOException {
        this.endpointType = "Server";
        this.cl = new udp_connection_layer(listeningPort);
    }

    public tftp_protocol_layer(String type, String dest, int destTID) throws IOException {
        this.endpointType = type;
        this.cl = new udp_connection_layer(dest, destTID);
    }
    
    void sendRRQ(String filename) throws IOException {
        cl.sendData(new tftp_read_pkt(filename, "netascii").getRawPacket());
        logme("Sent RRQ");
    }
    
    boolean sendDataPkt(byte[] data) throws IOException {
        cl.sendData(new tftp_data_pkt(blocknum, data).getRawPacket());
        logme("Sent DATA");
        
        tftp_packet response = listen(); // Listen for ACK reply
        logme("Ack received");
        if (blocknum != response.getBlocknum()) {
            return false; // Signal resending of data.  
        }
        blocknum++;
        
        return true;
    }
    
    void sendAck(short blocknum) throws IOException {
        logme("Send Ack");
        cl.sendData(new tftp_ack_pkt(blocknum).getRawPacket());
    }

    tftp_packet listen() throws IOException {
        logme("Listening...");
        return new tftp_packet(cl.receiveData(1024));
    }
    
    byte[] receiveData() throws IOException {
        tftp_packet received = listen();
        sendAck(received.getBlocknum());       
        return received.getData();
    }
    
    String getHostName() {
        return cl.getHostName();
    }
    
    int getPort() {
        return cl.getPort();
    }

    void closeConnection() {
        logme("Close connection");
        cl.closeConnection();
    }

    private void logme(String msg) {
        System.out.println(endpointType + ": " + msg);
    }
}