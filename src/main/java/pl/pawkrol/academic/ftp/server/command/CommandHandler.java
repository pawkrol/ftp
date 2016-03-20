package pl.pawkrol.academic.ftp.server.command;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Scanner;

/**
 * Created by Pawel on 2016-03-19.
 */
public class CommandHandler implements Runnable{

    private static final Logger log = LogManager.getLogger("logger");
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

            log.log(Level.INFO, "Connection from: "
                    + socket.getInetAddress().getCanonicalHostName());

            Scanner scanner = new Scanner(socket.getInputStream());
            while (running && scanner.hasNext() && (!Thread.currentThread().isInterrupted())){
                System.out.print(scanner.nextLine());
                OutputStream os = socket.getOutputStream();
                os.write("ok\n".getBytes());
                os.flush();
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
        log.log(Level.INFO, "End of onnection from: "
                + socket.getInetAddress().getCanonicalHostName());
    }

    private void onTimeout(){
        log.log(Level.WARN, "Timeout terminating...");
        handleClose();
    }
}
