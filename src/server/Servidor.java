package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Scanner;

import ia.Intelecto;

public class Servidor implements Runnable {

	private ServerSocket server;
	private boolean inicializado;
	private boolean executando;
	private Thread thread;
	public static ArrayList<Atendente> atendentes;
	private Intelecto ia;

	public Servidor(int porta) throws Exception {

		atendentes = new ArrayList<Atendente>();

		inicializado = false;
		executando = false;

		open(porta);

	}

	private void open(int porta) throws Exception {

		server = new ServerSocket(porta);
		inicializado = true;

	}

	private void close() {

		for (Atendente atendente : atendentes) {

			try {
				atendente.stop();

			} catch (Exception e) {
				System.out.println(e);
			}
		}

		try {
			server.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		server = null;
		inicializado = false;
		executando = false;
		thread = null;

	}

	public void start() {

		if (!inicializado || executando)
			return;

		ia = new Intelecto();
		executando = true;
		thread = new Thread(this);
		thread.start();

	}

	public void stop() throws Exception {

		executando = false;

		if (thread != null)
			thread.join(); // Bloqueia a thread atual até que a thread auxiliar seja finalizada. Até que o método run() retorne

	}

	@Override
	public void run() {

		System.out.println("Aguardando conexão");

		while (executando) {
			try {
				server.setSoTimeout(2500);
				Socket socket = server.accept();

				System.out.println("Conexão estabelecida");

				/**
				 * Obriga a conectar primeiramente o Celular e
				 * em seguida conectar o Robo
				 */
				//Atendente dispositivoGeneric = Atendente.getInstance(socket);
				Atendente dispositivoGeneric = Atendente.getInstance(socket, ia);
				dispositivoGeneric.start();
				atendentes.add(dispositivoGeneric);

			} catch (SocketTimeoutException e) {
				//ingnorar???
			} catch (Exception e) {
				System.out.println(e);
				break;
			}
		}
		close();

	}

	public static void main(String[] args) throws Exception {

		Servidor servidor = new Servidor(8500);

		servidor.start();

		System.out.println("ENTER para encerrar o servidor");
		new Scanner(System.in).nextLine();

		System.out.println("Encerrando servidor");
		servidor.stop();

	}

}
