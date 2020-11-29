package de.asdf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.math.BigInteger;

public class Worker implements Runnable {
    private Socket clientSock;

    public Worker(Socket clientSock) {
        this.clientSock = clientSock;
    }

    public void run() {
        System.out.println("Worker start");
        try (OutputStream os = clientSock.getOutputStream();
             InputStream is = clientSock.getInputStream()) {
            this.clientSock.setSoTimeout(15000);

            // ============================================================
            byte[] len = new byte[2];
            if (is.read(len, 0, 2) != 2) {
                System.out.println("Could not read len (val)");
                return;
            }
            int lenVal = (byte)(len[0] << 8) | (byte)len[1];

            System.out.println(lenVal);

            byte[] valueBytes = new byte[lenVal];
            if (is.read(valueBytes, 0, lenVal) != lenVal) {
                System.out.println("Could not read value");
                return;
            }

            // ============================================================
            if (is.read(len, 0, 2) != 2) {
                System.out.println("Could not read len (exp)");
                return;
            }
            lenVal = (byte)(len[0] << 8) | (byte)len[1];

            byte[] expBytes = new byte[lenVal];
            if (is.read(expBytes, 0, lenVal) != lenVal) {
                System.out.println("Could not read exponent");
                return;
            }

            // ============================================================
            if (is.read(len, 0, 2) != 2) {
                System.out.println("Could not read len (mod)");
                return;
            }
            lenVal = (byte)(len[0] << 8) | (byte)len[1];

            byte[] modBytes = new byte[lenVal];
            if (is.read(modBytes, 0, lenVal) != lenVal) {
                System.out.println("Could not read exponent");
                return;
            }
            
            BigInteger value = new BigInteger(valueBytes);
            BigInteger result = value.modPow(new BigInteger(expBytes), new BigInteger(modBytes));

            byte[] resBytes = result.toByteArray();

            os.write(new byte[]{
                    (byte)(resBytes.length & (0xFF00)),
                    (byte)(resBytes.length & (0xFF)),
                });
            os.write(resBytes);
            System.out.println("Done");
        } catch (IOException ioe) {
            System.out.println("Exn during sleep");
        }
        catch (ArithmeticException ae) {
            System.out.println("Exn during calculation");
        }
    }
}
