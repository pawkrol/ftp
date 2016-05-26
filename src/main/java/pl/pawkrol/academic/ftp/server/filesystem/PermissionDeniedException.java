package pl.pawkrol.academic.ftp.server.filesystem;

/**
 * Created by pawkrol on 4/24/16.
 */
public class PermissionDeniedException extends Exception{

    public PermissionDeniedException(String msg){
        super(msg);
    }

}
