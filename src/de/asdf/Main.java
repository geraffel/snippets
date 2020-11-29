package de.asdf;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

import de.asdf.Worker;

public class Main {

    public static void main(String[] args) {
        long count = 0;
        try {
            if (args.length != 1) {
                System.out.println("Wrong usage");
            } else {
                int port = Integer.parseInt(args[0]);
                ServerSocket serverSock = new ServerSocket(port);
                while (true) {
                    Socket client = serverSock.accept();
                    Worker w = new Worker(client);
                    Thread workerThread = new Thread(w);
                    workerThread.setDaemon(true);
                    workerThread.run();
                }
            }
        }
        catch (IOException ioe) {
            System.out.println("Error during startup");
            ioe.printStackTrace();
        }
    }
}
