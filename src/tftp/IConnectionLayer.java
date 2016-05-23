package tftp;

import java.io.IOException;

public interface IConnectionLayer {
    void sendData(byte[] buf) throws IOException;
    byte[] receiveData(int bufSize) throws IOException;
    void closeConnection();
    String getHostName();
    int getPort();
}