package pl.pawkrol.academic.ftp.client.connection;

/**
 * Created by pawkrol on 5/26/16.
 */
public abstract class DataHandler {

    protected DataConnector dataConnector;
    protected AddressPortPair addressPortPair;

    public void open(AddressPortPair addressPortPair){
        this.addressPortPair = addressPortPair;
    }

    public void setConnector(DataConnector dataConnector){
        this.dataConnector = dataConnector;
    }

    public void close(){
        if (dataConnector != null){
            dataConnector.close();
        }
    }

}
