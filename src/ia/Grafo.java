package ia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Grafo {

	private Mapa mapa;
	
	private ArrayList<Estado> listaDeNodos;
	private HashMap<Estado, HashMap<Estado, Integer>> matrizAdjac = new HashMap<>();
	
	public Grafo(Mapa mapa){
		
		this.mapa = mapa;
		
	}
	
	public void estruturaDados(){

		getNodos();
		geraMatrizAdjacente();
		
	}
	
	private void getNodos() {

		listaDeNodos = new ArrayList<>();

		for (int linha = 0; linha < mapa.getQtdLinhasMapa(); linha++) 
			for (int coluna = 0; coluna < mapa.getQtdColunasMapa(); coluna++) 
				if (mapa.getMapa()[linha][coluna].isCaminho() && isBreakpoint(linha, coluna) == true) {
					listaDeNodos.add(mapa.getMapa()[linha][coluna]);
				}
		
	}
	
	private boolean isBreakpoint(int linha, int coluna) {

		try{
			if(!mapa.getMapa()[linha][coluna].isCaminho())
				return false;
			
			int cont = 0;
			if (linha > 0 && mapa.getMapa()[linha - 1][coluna].isCaminho()) {	// Valida Norte
				mapa.getMapa()[linha][coluna].setNorte(true);
				cont += 1;
			}
			if (linha < (mapa.getQtdLinhasMapa() - 1) && mapa.getMapa()[linha + 1][coluna].isCaminho()) { //Valida Sul
				mapa.getMapa()[linha][coluna].setSul(true);
				cont += 2;
			}
			if (coluna > 0 && mapa.getMapa()[linha][coluna - 1].isCaminho()) {	//Valida Oeste
				mapa.getMapa()[linha][coluna].setOeste(true);
				cont += 4;
			}
			if (coluna < (mapa.getQtdColunasMapa() - 1) && mapa.getMapa()[linha][coluna + 1].isCaminho()) {	// Valida Leste
				mapa.getMapa()[linha][coluna].setLeste(true);
				cont += 8;
			}
			if ((cont == 3 || cont == 12) && (!mapa.getMapa()[linha][coluna].getNome().equals(mapa.getEstadoIni().getNome()) && !mapa.getMapa()[linha][coluna].getNome().equals(mapa.getEstadoObjetivo().getNome()))) // && .equals(estadoIni) || .equals(estadoObjetivo) is.caminho
				return false;
						
		}catch(ArrayIndexOutOfBoundsException exception){
			System.out.println("O índice " + exception.getMessage() + " não existe para verificar se o estado " + mapa.getPosMapa(linha, coluna).getNome() + " é um Breakpoint");
		}
		
		return true;

	}

	private void geraMatrizAdjacente() {

		matrizAdjac = new HashMap<>();

		for (Estado estado : listaDeNodos) {			
			matrizAdjac.put(estado, new HashMap<>()); 		// Para cada item cria-se uma nova lista. 
																			// Esta lista cont?m um relacionamento com os filhos/custo
			if (estado.isNorte()) 
				getDescendentes(estado, "Norte");			
			if (estado.isSul()) 
				getDescendentes(estado,"Sul");			
			if (estado.isLeste()) 
				getDescendentes(estado,"Leste");			
			if (estado.isOeste()) 
				getDescendentes(estado,"Oeste");			
		}
		
	}
	
	private void getDescendentes(Estado estado,String direcao) {
		
		int limite = 0;
		boolean filhoEncontrado = false;
		
		if(direcao =="Sul")
			limite = mapa.getQtdLinhasMapa();
		if(direcao == "Leste")
			limite = mapa.getQtdColunasMapa();
		
		for (int linha = 0; linha < mapa.getQtdLinhasMapa(); linha++) {
			if(filhoEncontrado)
				break;
			for (int coluna = 0; coluna < mapa.getQtdColunasMapa(); coluna++) {
				if (estado.getNome().equals(mapa.getMapa()[linha][coluna].getNome())) {
					int custoIni = 0;
					int custoFin = 0;
					int custoLinha = linha;
					int custoColuna = coluna;
					
					while ((custoLinha != limite || custoColuna != limite) && !filhoEncontrado) {
						
						switch (direcao) {
							case "Norte":
								custoLinha--;
								custoIni = linha;
								custoFin = custoLinha;
							break;
							case "Sul":
								custoLinha++;	
								custoIni = linha;
								custoFin = custoLinha;
							break;
							case "Oeste":
								custoColuna--;
								custoIni = coluna;
								custoFin = custoColuna;
							break;
							case "Leste":
								custoColuna++;	
								custoIni = coluna;
								custoFin = custoColuna;
							break;
														
							default:
								break;
						}
	
						if (isBreakpoint(custoLinha, custoColuna)) {		//if (isBreakpoint(custoX,custoY )) {
							boolean isPai = false; 	
							if(!isPai){			// Significa que n?o foi adicionado a lista de pais
								matrizAdjac.get(estado).put(mapa.getMapa()[custoLinha][custoColuna], getCusto(custoIni, custoFin));	//matrizAdjac.get(estado).put(mapa.getMapa()[custoY][custoX], getCusto(custoIni, custoFin));																			
								filhoEncontrado = true;
							}				
						}
					} //fim while
					
				} //fim if
			} //fim for qtdColunasMapa
		} //fim for qtdLinhasMapa

	}
	
	public int getCusto(int valor1,int valor2){
		
		if(valor1 > valor2)
			return valor1 - valor2;
		else
			return valor2 - valor1;
		
	}
	
	/** Retorna uma lista com os vértices do grafo ou seja, os breakpoints */
	public ArrayList<Estado> getFilhos(Estado estado){
		
		ArrayList<Estado> filhos = new ArrayList<>();
		
		for (Entry<Estado, HashMap<Estado, Integer>> pai : matrizAdjac.entrySet()) {
			if(pai.getKey().getNome().equals(estado.getNome())){
				for (Estado filho : pai.getValue().keySet()) {					
					filho.setCusto(pai.getValue().get(filho));		// Pega o custo e seta bo estado filho					
					filhos.add(filho);
				}
			}
		}
				
		return filhos;		
		
	}


	/** 
	 *Diverg?ncia entre estado e leitura significa uma Barreira,
	 *seta o estado do pr?ximo movimento como isCaminho = false
	 *e seta a dire??o onde foi constatado a barreira como false  */ 
	public void atualizaMapa(Estado estadoPresente,Estado estadoFuturo,Estado leitura){
		
		int linha = mapa.getPosMapa(estadoPresente)[0];	// pega pos do mapa no eixo X do estadoValido
		int coluna = mapa.getPosMapa(estadoPresente)[1];	// pega pos do mapa no eixo Y do estadoValido

		if(estadoPresente.isNorte() != leitura.isNorte()){	// Norte
			System.out.println("EstadoOld: " +  mapa.getPosMapa(linha - 1, coluna).getNome() + " Caminho: " + mapa.getPosMapa(linha - 1, coluna).isCaminho());				
			alteraDirecoesDoEstado(linha - 1, coluna);		// atualiza vizinho do Norte (cima)
			alteraDirecoesVizinhos(linha - 1, coluna);				
			System.out.println("EstadoNew: " + mapa.getPosMapa(linha - 1, coluna).getNome() + " Caminho: " + mapa.getPosMapa(linha - 1, coluna).isCaminho());
		}
		if(estadoPresente.isSul() != leitura.isSul()){	// Sul
			System.out.println("EstadoOld: " + mapa.getPosMapa(linha + 1, coluna).getNome() + " Caminho: " + mapa.getPosMapa(linha + 1, coluna).isCaminho());
			alteraDirecoesDoEstado(linha + 1, coluna);		// atualiza vizinho do Sul (baixo)
			alteraDirecoesVizinhos(linha + 1, coluna);
			System.out.println("EstadoNew: " + mapa.getPosMapa(linha + 1, coluna).getNome() + " Caminho: " + mapa.getPosMapa(linha + 1, coluna).isCaminho());
		}			
		if(estadoPresente.isLeste() != leitura.isLeste()){	// Leste		
			System.out.println("EstadoOld: " + mapa.getPosMapa(linha, coluna + 1).getNome() + " Caminho: " + mapa.getPosMapa(linha, coluna + 1).isCaminho());
			alteraDirecoesDoEstado(linha, coluna + 1);		// atualiza vizinho do Leste (direita)
			alteraDirecoesVizinhos(linha, coluna + 1);
			System.out.println("EstadoNew: " + mapa.getPosMapa(linha, coluna + 1).getNome() + " Caminho: " + mapa.getPosMapa(linha, coluna + 1).isCaminho());
		}
		if(estadoPresente.isOeste() != leitura.isOeste()){	// Oeste
			System.out.println("EstadoOld: " + mapa.getPosMapa(linha, coluna - 1).getNome() + " Caminho: " + mapa.getPosMapa(linha, coluna - 1).isCaminho());
			alteraDirecoesDoEstado(linha, coluna - 1);		// atualiza vizinho do Oeste (esquerda)
			alteraDirecoesVizinhos(linha, coluna - 1);
			System.out.println("EstadoNew: " + mapa.getPosMapa(linha, coluna - 1).getNome() + " Caminho: " + mapa.getPosMapa(linha, coluna - 1).isCaminho());
		}

	}
	
	private void alteraDirecoesDoEstado(int x, int y){
		
		try{
			mapa.getPosMapa(x, y).setCaminho(false);
			mapa.getPosMapa(x, y).setNorte(false);  
			mapa.getPosMapa(x, y).setSul(false);    
			mapa.getPosMapa(x, y).setOeste(false);  
			mapa.getPosMapa(x, y).setLeste(false);  
		}catch(ArrayIndexOutOfBoundsException exception){
			System.out.println("O ?ndice " + exception.getMessage() + " n?o existe para ao alterar direcoes do Mapa " + mapa.getPosMapa(x, y).getNome());			
		}		
	}
	
	private void alteraDirecoesVizinhos(int x, int y){
		
		try{
			mapa.getPosMapa(x + 1, y).setNorte(false);
			mapa.getPosMapa(x - 1, y).setSul(false);
			mapa.getPosMapa(x, y - 1).setLeste(false);
			mapa.getPosMapa(x, y + 1).setOeste(false);
		}catch(ArrayIndexOutOfBoundsException exception){
			System.out.println("O ?ndice " + exception.getMessage() + " n?o existe para ao alterar direcoes dos Vizinhos " + mapa.getPosMapa(x, y).getNome());			
		}	
	}
	
	public void imprimeListaDeNodos(){
		System.out.print("\nLista de Nodos:");
		for (Estado estado : listaDeNodos) {
			System.out.print("\n" + estado.getNome() + " Norte:" + estado.isNorte() + " Sul:" + estado.isSul() + " Leste:" + estado.isLeste() + " Oeste:" + estado.isOeste());
			if(estado.isObjetivo())
				System.out.print(" - Objetivo");
			if(estado.isInicio())
				System.out.print(" - Inicio");
		}
		System.out.println("");
		
	}
	
	public void imprimeMatrizAdjasc(){
		
		System.out.print("\nMatriz Adjascente:");
		for (Entry<Estado, HashMap<Estado, Integer>> pai : matrizAdjac.entrySet()) {
			System.out.print("\nPai: " + pai.getKey().getNome() + "> ");
			for (Estado filho : pai.getValue().keySet()) { 
				System.out.print(filho.getNome() + "; ");
			}
		}
		System.out.println("");
		
	}
	
	public HashMap<Estado, HashMap<Estado, Integer>> getMatrizAdjac() {
		return matrizAdjac;
	}
	public void setMatrizAdjac(HashMap<Estado, HashMap<Estado, Integer>> matrizAdjac) {
		this.matrizAdjac = matrizAdjac;
	}

	public ArrayList<Estado> getListaDeNodos() {
		return listaDeNodos;
	}
	public void setListaDeNodos(ArrayList<Estado> listaDeNodos) {
		this.listaDeNodos = listaDeNodos;
	}

	public Mapa getMapa() {
		return mapa;
	}

	public void setMapa(Mapa mapa) {
		this.mapa = mapa;
	}
	
	
}
