package server;

import java.net.Socket;
import java.net.SocketTimeoutException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import ia.Estado;
import ia.Intelecto;

public class Robo extends Atendente {

	public Robo(Socket socket, Intelecto ia) throws Exception {

		super.socket = socket;
		super.inicializado = false;
		super.executando = false;
		super.ia = ia;

		open();
	}

	public Robo(Socket socket) throws Exception {

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

						System.out.println("Robô!!!");
						thread.setName("Robo");
						MensagemRobo mensagemRobo = new MensagemRobo();
						Gson gson = new Gson();
						try {
							mensagemRobo = gson.fromJson(mensagem, MensagemRobo.class);
						} catch (JsonSyntaxException e) {
							MetricasRobo metricasRobo = new MetricasRobo();
							metricasRobo = gson.fromJson(mensagem, MetricasRobo.class);

							if (metricasRobo.getNorte() == 1)
								mensagemRobo.setNorte(true);
							else
								mensagemRobo.setNorte(false);

							if (metricasRobo.getSul() == 1)
								mensagemRobo.setSul(true);
							else
								mensagemRobo.setSul(false);

							if (metricasRobo.getLeste() == 1)
								mensagemRobo.setLeste(true);
							else
								mensagemRobo.setLeste(false);

							if (metricasRobo.getOeste() == 1)
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

						if (thread.getName().equals("Robo")) { // RESPONDE PARA O ROBÔ
							out.println(ia.getProxMovimento());

						}

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
