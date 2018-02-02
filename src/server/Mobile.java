package server;

import java.net.Socket;
import java.net.SocketTimeoutException;

import com.google.gson.Gson;

import ia.Intelecto;
import ia.Mapa;

public class Mobile extends Atendente {

	public Mobile(Socket socket, Intelecto ia) throws Exception {

		// TODO Auto-generated constructor stub

		super.socket = socket;
		super.inicializado = false;
		super.executando = false;
		super.ia = ia;

		open();
	}

	public Mobile(Socket socket) throws Exception {

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

						System.out.println("Android!!!");
						thread.setName("Android");
						Mapa mapa = new Mapa();
						Gson gson = new Gson();
						mapa = gson.fromJson(mensagem, Mapa.class);
						mapa.setQtdLinhasMapa(mapa.getMapa().length);
						mapa.setQtdColunasMapa(mapa.getMapa()[0].length);
						ia.inicio(mapa);

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
