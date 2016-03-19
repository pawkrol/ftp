package pl.pawkrol.academic.ftp.server.command;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Scanner;

/**
 * Created by Pawel on 2016-03-19.
 */
public class CommandHandler implements Runnable{

    private final Socket socket;

    private int timeout;
    private boolean running;

    public CommandHandler(Socket socket) {
        this.socket = socket;
        this.running = true;
        this.timeout = 60; //seconds
    }

    public void close(){
        running = false;
    }

    @Override
    public void run(){
        try {
            socket.setSoTimeout(timeout * 1000);

            Scanner scanner = new Scanner(socket.getInputStream());
            while (running && scanner.hasNext()){

            }

            handleClose();

        } catch (SocketTimeoutException e){
            onTimeout();
        } catch (SocketException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClose(){

    }

    private void onTimeout(){
        handleClose();
    }
}
