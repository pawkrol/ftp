package pl.pawkrol.academic.ftp.server.connection;

import pl.pawkrol.academic.ftp.server.session.Session;

/**
 * Created by Pawel on 2016-04-10.
 */
public abstract class DataHandler implements Runnable{

    private final Session session;

    public DataHandler(Session session){
        this.session = session;
    }

    @Override
    public void run() {}

    public void close() {}
}
