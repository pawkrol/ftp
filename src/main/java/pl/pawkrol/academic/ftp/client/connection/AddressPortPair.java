package pl.pawkrol.academic.ftp.client.connection;

/**
 * Created by pawkrol on 5/27/16.
 */
public class AddressPortPair{

    private String address;
    private int port;

    public AddressPortPair(String msg){
        String[] tokens = msg.split(",", 6);

        address = tokens[0] + "." + tokens[1] + "." + tokens[2] + "." + tokens[3];
        port = Integer.parseInt(tokens[4]) * 256 + Integer.parseInt(tokens[5]);
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
