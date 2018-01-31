package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import ia.Estado;
import ia.Intelecto;
import ia.Mapa;

public class Atendente implements Runnable {

	private Socket socket;
	private BufferedReader in;
	private PrintStream out;
	private boolean inicializado;
	private boolean executando;
	private Thread thread;
	private Intelecto ia;

	public Atendente(Socket socket, Intelecto ia) throws Exception {

		this.socket = socket;
		this.inicializado = false;
		this.executando = false;
		this.ia = ia;

		open();
	}

	private void open() throws Exception {

		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintStream(socket.getOutputStream());
			inicializado = true;
		} catch (Exception e) {
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

	@Override
	public void run() {

		while (executando) {
			try {

				socket.setSoTimeout(2500);

				String mensagem = in.readLine();

				//System.out.println("Mensagem recebida do cliente " + socket.getInetAddress().getHostName() + ":" + socket.getPort() + " - " + mensagem);

				if ("Fim".equals(mensagem)) {
					System.out.println(socket.getInetAddress().getHostName() + ":" + socket.getPort() + " - " + mensagem);
					break;
				}

				//Json aqui
				if (mensagem != null) {
					System.out.println(
							"Mensagem recebida do cliente " + socket.getInetAddress().getHostName() + ":" + socket.getPort() + " - " + mensagem);

					System.out.println(thread.getName());
					try {
												
						if(mensagem.contains("estadoIni")){
							System.out.println("Android!!!");
							thread.setName("Android");
							Mapa mapa = new Mapa();
							Gson gson = new Gson();
							mapa = gson.fromJson(mensagem, Mapa.class);
							mapa.setQtdLinhasMapa (mapa.getMapa().length);   
							mapa.setQtdColunasMapa(mapa.getMapa()[0].length);
							ia.inicio(mapa);
						}else{
							System.out.println("Robô!!!");
							thread.setName("Robo");
							MensagemRobo mensagemRobo = new MensagemRobo();
							Gson gson = new Gson();
							try {																
								mensagemRobo = gson.fromJson(mensagem, MensagemRobo.class);							
							}catch(JsonSyntaxException e){								
								MetricasRobo metricasRobo = new MetricasRobo();
								metricasRobo = gson.fromJson(mensagem, MetricasRobo.class);
								
								if(metricasRobo.getNorte() == 1)
									mensagemRobo.setNorte(true);
								else
									mensagemRobo.setNorte(false);
								
								if(metricasRobo.getSul() == 1)
									mensagemRobo.setSul(true);
								else
									mensagemRobo.setSul(false);
								
								if(metricasRobo.getLeste() == 1)
									mensagemRobo.setLeste(true);
								else	
									mensagemRobo.setLeste(false);
								
								if(metricasRobo.getOeste() == 1)
									mensagemRobo.setOeste(true);
								else	
									mensagemRobo.setOeste(false);
									
								mensagemRobo.setMovimento(metricasRobo.getMovimento());
							} catch (IllegalStateException e) {
								e.printStackTrace();	
							}
							
							Estado estadoLeitura = new Estado();
							estadoLeitura.setNorte(mensagemRobo.isNorte());
							estadoLeitura.setSul(mensagemRobo.isSul());
							estadoLeitura.setLeste(mensagemRobo.isLeste());
							estadoLeitura.setOeste(mensagemRobo.isOeste());
							ia.comportamentoInteligente(estadoLeitura, mensagemRobo.getMovimento());							
							
							if(thread.getName().equals("Robo")){	// RESPONDE PARA O ROBÔ
								out.println(ia.getProxMovimento());		
								
								/*if (isRobo) {
									ArrayList<Atendente> atendentes = Servidor.atendentes;
								   for (Atendente att : atendentes) {
								   	if (att == Atendente.this) { // se o obj do LOOP eh o mesmo onde este metodo sendo executad
								   	if ("robo".equals(att.thread.getName())) {
								   		att.out.print(arg0);
								   		att.out.
								   		break;
								   	}
								   }
								   
								}
								
								*/
							}
							
						}
						
						/*
						List<Atendente> atendentes = Servidor.atendentes;
					   for (Atendente att : atendentes) {
					   	if (att == Atendente.this) { // se o obj do LOOP eh o mesmo onde este metodo sendo executad
					   		if ("Android".equals(att.thread.getName())) {
					   			att.out.print(ia.get);
					   			att.out.
					   			break;
					   		}
					   	}
					   }
					*/

					} catch (Exception e) {

						System.out.println("Não abriu Gson");
					}
					mensagem = null;
					//out.println("OK");
				}

			} catch (SocketTimeoutException e) {
				//ingorar
			} catch (Exception e) {
				System.out.println(e);
				break;
			}
		}

		System.out.println("Encerrando conexão");

		close();
	}

}
