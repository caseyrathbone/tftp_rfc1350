package tftp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class udp_connection_layer implements IConnectionLayer {
    private final DatagramSocket socket;
    private InetAddress address;
    private int port;

    // Server initialization.
    public udp_connection_layer(int listeningPort) throws IOException {
        this.address = null;
        this.port = 0;
        this.socket = new DatagramSocket(listeningPort);
    }
    
    // Client initialization.
    public udp_connection_layer(String dest, int port) throws IOException {
        this.address = InetAddress.getByName(dest);
        this.port = port;
        this.socket = new DatagramSocket();
    }
    
    public void sendData(byte[] bufToSend) throws IOException {
        DatagramPacket dgram = new DatagramPacket(bufToSend, bufToSend.length, address, port);
        socket.send(dgram);
    }
    
    public byte[] receiveData(int bufSize) throws IOException {
        byte[] buf = new byte[bufSize];
        DatagramPacket pkt = new DatagramPacket(buf, buf.length);
        socket.receive(pkt);
        
        address = pkt.getAddress();
        port = pkt.getPort();
        
        int numBytesReceived = pkt.getLength();
        
        if (bufSize != numBytesReceived) {
            byte[] newBuf = new byte[numBytesReceived];
            newBuf = Arrays.copyOfRange(buf, 0, numBytesReceived);
            buf = newBuf;
        }
        
        return buf;
    }
    
    public String getHostName() {
        return address.getHostName();
    }
    
    public int getPort() {
        return port;
    }
    
    public void closeConnection() {
        socket.close();
    }
}
