package com.company.Thread;

import java.io.*;

/**
 * Class to manage redirection of output of certain programs to a specific file
 * When running tshark to filter packet
 */
public class FileStreamExternalThread extends Thread {
    private InputStream is;
    private OutputStream os;

    public FileStreamExternalThread(InputStream is) {
        this.is = is;
    }
    public FileStreamExternalThread(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
    }

    public void run() {
        try {
            PrintWriter pw = null;
            if (os != null) pw = new PrintWriter(os);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null) {
                if (pw != null)
                    pw.println(line);
            }
            if (pw != null)
                pw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
