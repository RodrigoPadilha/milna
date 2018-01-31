package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

import com.google.gson.Gson;

public class Cliente implements Runnable {

	private Socket socket;
	private BufferedReader in;
	private PrintStream out;
	private boolean inicializado;
	private boolean executando;
	private Thread thread;

	public Cliente(String endereco, int porta) throws Exception {
		inicializado = false;
		executando = false;

		open(endereco, porta);
	}

	private void open(String endereco, int porta) throws Exception {
		try {
			socket = new Socket(endereco, porta);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintStream(socket.getOutputStream());
			inicializado = true;
		} catch (Exception e) {
			System.out.println(e);
			close();
			throw e;
		}
	}

	private void close() {
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

		if (socket != null) {
			try {
				socket.close();
			} catch (Exception e) {
				System.out.println(e);
			}
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

	public void send(String mensagem) {
		out.println(mensagem);
	}

	public boolean isExecutando() {
		return executando;
	}

	@Override
	public void run() {
		while (executando) {

			try {
				socket.setSoTimeout(2500);
				String mensagem = in.readLine();

				if (mensagem == null)
					break;

				System.out.println("Mensagem enviada pelo servidor: " + mensagem);
			} catch (SocketTimeoutException e) {
				// ignorar
			} catch (Exception e) {
				System.out.println(e);
				break;
			}
		}
		close();
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Iniciando cliente...");

		System.out.println("Iniciando conexão com servidor...");

		Cliente cliente = new Cliente("192.168.25.36", 8500);// 52.67.10.115
		//Cliente cliente = new Cliente("172.16.29.29", 8500);// 52.67.10.115
		//Cliente cliente = new Cliente("ec2-52-67-10-115.sa-east-1.compute.amazonaws.com", 8500);

		System.out.println("Conexão estabelecida com sucesso");

		cliente.start();

		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("Digite sua mensagem: ");
			//String mensagem = scanner.nextLine();
			String mensagem = "";
			
			TesteMetricas metricas = new TesteMetricas();
			
			System.out.println("Insira o movimento: norte, sul, leste,oeste");
			String movimento = scanner.nextLine();
			switch (movimento) {
				case "norte":
					metricas.addMovimento(new int []{-1,0});
					break;

				case "sul":
					metricas.addMovimento(new int []{1,0});
					break;
				case "leste":
					metricas.addMovimento(new int []{0,1});
					break;

				case "oeste":
					metricas.addMovimento(new int []{0,-1});
					break;
					
				default : //parado
					metricas.addMovimento(new int []{0,0});
					break;
			}
			
			System.out.println("Insira as características: 1-True 0-False");
			
			System.out.println("Norte");
			String celula = scanner.nextLine();
//			String celula = "1";
			if (celula.equals("1")) {
				metricas.setNorte(true);				
			}else if(celula.equals("0") || celula.equals("")){
				metricas.setNorte(false);
			}
			
			System.out.println("Sul");
			celula = scanner.nextLine();
//			celula = "0";
			if (celula.equals("1")) {
				metricas.setSul(true);				
			}else{
				metricas.setSul(false);
			}
			
			System.out.println("Leste");
			celula = scanner.nextLine();
//			celula = "1";
			if (celula.equals("1")) {
				metricas.setLeste(true);				
			}else{
				metricas.setLeste(false);
			}
			
			System.out.println("Oeste");
			celula = scanner.nextLine();
//			celula = "1";
			if (celula.equals("1")) {
				metricas.setOeste(true);				
			}else{
				metricas.setOeste(false);
			}
			
			Gson gson = new Gson();
			mensagem = gson.toJson(metricas);
			
			if (!cliente.isExecutando())
				break;

			cliente.send(mensagem);

			if ("Fim".equals(mensagem))
				break;
		}
		System.out.println("Encerrando cliente...");
		cliente.stop();
	}
}
