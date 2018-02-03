package ia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Roterizador {

	private Grafo grafo;
	private MelhorEscolha busca;
	private ArrayList<Rota> listaDeRotas;
	
	public Roterizador(Grafo grafo){
		
		this.grafo = grafo;
		listaDeRotas = new ArrayList<>();
	}

	public void buscaRotas(Estado estadoIni) { 	// Buscar rotas para todos os estados poss�veis gerados pelo Localizador
		
		grafo.getMapa().setEstadoIni(estadoIni); 	// seta estado poss�vel em estado Objetivo para buscar sua rota at� o estadoObjetivo
		grafo.estruturaDados(); 						//Refazer matrizAdjac Para considerar novo estadoIni como breakpoint

		busca = new MelhorEscolha(grafo);

		Rota rota = new Rota();
		rota.setInicio(estadoIni);
		rota.setCaminhoSolucao(busca.buscaHeuristica());

		listaDeRotas.add(rota);
		
	}

	public void rankingRotas() {
		List<Integer> pos1 = new ArrayList<>();
		List<Integer> pos2 = new ArrayList<>();
		int distancia, cont;

		for (Rota rota : listaDeRotas) { 							// Passa por todas as rotas adicionadas a lista atrav�s de cada estado poss�vel de > probabilidade 
			distancia = 0;
			cont = rota.getCaminhoSolucao().size() - 1;
			Estado estado = rota.getCaminhoSolucao().get(cont); // pega um estado da lista de "caminho solu��o" at� passar por todos da lista
//			System.out.print("\nEstado " + estado.getNome());

			while (!estado.getNome().equals(grafo.getMapa().getEstadoObjetivo().getNome())) { // Enquando n�o chegar ao estado ra�z da rota continua fazeno o procedimento	
//				System.out.println("");
//				System.out.print("Custo entre " + estado.getNome() + "-");
				for (int i = 0; i < grafo.getMapa().getQtdLinhasMapa(); i++) { // procura a posi��o do estado que est� na lista "caminho solu��o"
					for (int j = 0; j < grafo.getMapa().getQtdColunasMapa(); j++) {
						if (grafo.getMapa().getPosMapa(i, j).getNome().equals(estado.getNome())) {
							pos1.add(i);
							pos1.add(j);
							cont--;
						}
					}
				}

				estado = rota.getCaminhoSolucao().get(cont); // procura o pr�ximo estado para calcular a dist�ncia entre eles
//				System.out.print(estado.getNome() + " = ");
				for (int i = 0; i < grafo.getMapa().getQtdLinhasMapa(); i++) {
					for (int j = 0; j < grafo.getMapa().getQtdColunasMapa(); j++) {
						if (grafo.getMapa().getPosMapa(i, j).getNome().equals(estado.getNome())) {
							pos2.add(i);
							pos2.add(j);
						}
					}
				}
				// calcula dist�ncia entre os estados
				if (pos1.get(0) == pos2.get(0)) { //se est�o na mesma linha a diferen�a ser� entre j
//					System.out.print(grafo.getCusto(pos1.get(1), pos2.get(1)));
					distancia += grafo.getCusto(pos1.get(1), pos2.get(1)); // j-j										
				} else { //se est�o na mesma coluna a diferen�a ser� entre i
//					System.out.print(grafo.getCusto(pos1.get(0), pos2.get(0)));
					distancia += grafo.getCusto(pos1.get(0), pos2.get(0)); // i-i																		
				}

				// DEFINIR TRILHA DE MIGALHAS
				int indiceDaMigalha = 0;
				for (Estado estadoInd : rota.getCaminhoSolucao()) {
					if (estadoInd.getNome().equals(grafo.getMapa().getPosMapa(pos1.get(0), pos1.get(1)).getNome())) {
						indiceDaMigalha = rota.getCaminhoSolucao().indexOf(estadoInd);
//						System.out.println(" Estado: " + estadoInd.getNome() + " Indice: " + indiceDaMigalha);
					}
				}
				addMigalhas(pos1.get(0), pos2.get(0), pos1.get(1), pos2.get(1), rota, indiceDaMigalha);
				pos1.clear();
				pos2.clear();
			}
			rota.setCusto(distancia);
//			System.out.print("\nRota do estado " + rota.getCaminhoSolucao().get(rota.getCaminhoSolucao().size() - 1).getNome() + " custo total = "	+ rota.getCusto());
//			imprimeTrilhaDeMigalhas(rota);
//			System.out.println("");
		}

		Collections.sort(listaDeRotas);

	}

	// Método recebe as posi��es de onde deve come�ar a busca pelos estados
	private void addMigalhas(int linhaIni, int linhaFin, int colunaIni, int colunaFin, Rota rota, int indice) {

		boolean corrigeIndice = false; // Se precisar inverter usar incremento no contIndex
		int aux;
		if (linhaIni > linhaFin) {
			aux = linhaIni;
			linhaIni = linhaFin;
			linhaFin = aux;
			corrigeIndice = true;
		}
		if (colunaIni > colunaFin) {
			aux = colunaIni;
			colunaIni = colunaFin;
			colunaFin = aux;
			corrigeIndice = true;
		}

		int contIndice = indice; // Recebe o indice do ultimo estado da lista, ent�o subtrai 1 para fica na posi��o certa para add
		for (int i = linhaIni; i <= linhaFin; i++) {
			for (int j = colunaIni; j <= colunaFin; j++) {
				if (!grafo.getMapa().getPosMapa(i, j).getNome().equals(grafo.getMapa().getPosMapa(linhaIni, colunaIni).getNome())
						&& !grafo.getMapa().getPosMapa(i, j).getNome().equals(grafo.getMapa().getPosMapa(linhaFin, colunaFin).getNome())) { // Verifica se o estado n�o � um breakpoint que ja est� na lista												
//					System.out.print(" Add estado: " + grafo.getMapa().getPosMapa(i, j).getNome());
					rota.getCaminhoSolucao().add(contIndice, grafo.getMapa().getPosMapa(i, j));
					if (corrigeIndice)
						contIndice++;
				}
			}
		}

	}

	public void imprimeRotas() {
		System.out.println("Rotas Ordenadas");
		for (Rota rota : listaDeRotas){
			System.out.print("Rota do estado " + rota.getInicio().getNome() + " custo total = " + rota.getCusto());
			imprimeTrilhaDeMigalhas(rota);
		}

	}

	public void imprimeTrilhaDeMigalhas(Rota rota) {

		System.out.print(" Trilha de Migalhas: " + rota.getInicio().getNome() + ">> ");
		for (Estado estado : rota.getCaminhoSolucao()) {
			System.out.print(estado.getNome() + ";");
		}
		System.out.println();

	}

	public ArrayList<Rota> getListaDeRotas() {
		return listaDeRotas;
	}

	public void setListaDeRotas(ArrayList<Rota> listaDeRotas) {
		this.listaDeRotas = listaDeRotas;
	}


	public void setGrafo(Grafo grafo) {
		this.grafo = grafo;
	}
	
	
}
