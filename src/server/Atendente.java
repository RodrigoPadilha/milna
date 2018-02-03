package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import ia.Intelecto;

public abstract class Atendente implements Runnable, Dispositivo {

    private static Atendente UNIQUE_INSTANCE;
    protected boolean inicializado;
    protected boolean executando;
    protected Socket socket;
    protected Intelecto ia;
    protected Thread thread;
    protected BufferedReader in;
    protected PrintStream out;

    public static synchronized Atendente getInstance(Socket socket) {

        try {

            if (UNIQUE_INSTANCE == null) {
                UNIQUE_INSTANCE = new Mobile(socket);
            } else {
                UNIQUE_INSTANCE = new Robo(socket);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return UNIQUE_INSTANCE;
    }

    protected void open() throws Exception {

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());
            inicializado = true;
        } catch (Exception e) {
            close();
            throw e;
        }

    }

    protected void close() {

        if (in != null) {
            try {
                in.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        if (out != null) {
            try {
                out.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        try {
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        in = null;
        out = null;
        socket = null;

        inicializado = false;
        executando = false;

        thread = null;
    }

    public void start() {

        if (!inicializado || executando)
            return;

        executando = true;
        thread = new Thread(this);
        thread.start();

    }

    public void stop() throws Exception {

        executando = false;

        if (thread != null)
            thread.join();

    }

}
