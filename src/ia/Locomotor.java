package ia;

import java.util.ArrayList;
import java.util.List;

public class Locomotor {

	private Estado aux;
	private Estado estadoPassado;
	private Estado estadoPresente;
	private Estado estadoFuturo;
	public boolean jaChegou = false;
	
	public void definieMovimento(ArrayList<Rota> listaDeRotas) {

		if (listaDeRotas.size() == 1 && listaDeRotas.get(0).getCaminhoSolucao().size() == 1) {
			System.out.println("Vc chegou ao seu destino!!!!");
			jaChegou = true;
		} else {
	
			int indLista = 0;
			if (listaDeRotas.get(indLista).getCaminhoSolucao().size() == 1) // Incrementa somente quando existe a possibilidade de estar em cima do estado Objetivo
				indLista++;

			estadoPresente = listaDeRotas.get(indLista).getCaminhoSolucao().get(listaDeRotas.get(indLista).getCaminhoSolucao().size() - 1); // pega o primeiro passo
			estadoFuturo = listaDeRotas.get(indLista).getCaminhoSolucao().get(listaDeRotas.get(indLista).getCaminhoSolucao().indexOf(estadoPresente) - 1);

			
			/*			boolean voltando; 
			do{				
				voltando = false;
				estadoPassado = estadoPresente;
				estadoPresente = listaDeRotas.get(indLista).getCaminhoSolucao().get(listaDeRotas.get(indLista).getCaminhoSolucao().size() - 1); // pega o primeiro passo
				estadoFuturo = listaDeRotas.get(indLista).getCaminhoSolucao().get(listaDeRotas.get(indLista).getCaminhoSolucao().indexOf(estadoPresente) - 1);
											
				if(aux != null && aux.getNome().equals(estadoPresente.getNome())){
					System.out.println("Passado: " + estadoPassado.getNome() + " Presente: " + estadoPresente.getNome() + " Futuro: " + estadoFuturo.getNome() + " aux: " + aux.getNome());
					System.out.println("Está voltando!!!");
					voltando = true;
				}
				aux = estadoPassado;
			}while(voltando);
	*/		
			listaDeRotas.get(indLista).getCaminhoSolucao().remove(estadoPresente);
		}

		
		//Collections.rotate(listaDeRotas,4)????? listaDeRotas.size()-1			
		//considerar um estado passado???
	}

	public String buscaDirecao(Mapa mapa, int qtdLinhas, int qtdColunas) {

		List<Integer> posPresente = new ArrayList<>();
		List<Integer> posFuturo = new ArrayList<>();
		String direcao = "";
		
		for (int linha = 0; linha < qtdLinhas; linha++) {
			for (int coluna = 0; coluna < qtdColunas; coluna++) {

				if (mapa.getPosMapa(linha, coluna).getNome().equals(estadoPresente.getNome())) {
					posPresente.add(linha);
					posPresente.add(coluna);
				}
				if (mapa.getPosMapa(linha, coluna).getNome().equals(estadoFuturo.getNome())) {
					posFuturo.add(linha);
					posFuturo.add(coluna);
				}
			}

		}

		if (posPresente.get(0) == posFuturo.get(0)) { //se estão na mesma linha a diferença será entre j
			if (posPresente.get(1) > posFuturo.get(1)) {
				direcao = "Mov> Oeste";
			} else {
				direcao = "Mov> Leste";
			}
		} else { //se estão na mesma coluna a diferença será entre i
			if (posPresente.get(0) > posFuturo.get(0)) {
				direcao = "Mov> Norte";
			} else {
				direcao = "Mov> Sul";
			}

		}
		return direcao;

	}
	
	public Estado getEstadoPresente() {
		return estadoPresente;
	}

	public Estado getEstadoFuturo() {
		return estadoFuturo;
	}

	public void setEstadoPresente(Estado estadoPresente) {
		this.estadoPresente = estadoPresente;
	}

	public void setEstadoFuturo(Estado estadoFuturo) {
		this.estadoFuturo = estadoFuturo;
	}

	
	
	
}
