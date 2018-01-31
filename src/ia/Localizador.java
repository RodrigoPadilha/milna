package ia;

import java.util.ArrayList;

public class Localizador {

	private Mapa mapaEstados;
	private String[][] mapaSimbolos;

	private int qtdEstados = 0;
	private double sensorRigth,sensorWrong,pMove,pStay,pInit;                       			  				
	private double p[][];
																														// {0,0},	 Parado  
//	String measurements[] = { "T", "T", "|", "T", "-", "<" };										// {0,1},	 Direita 
	ArrayList<Estado> leituras = new ArrayList<>();                                        // {0,-1},	 Esquerda
//	int motions[][] = { { 0, 0 }, { 0, 1 }, { -1, 0 }, { -1, 0 }, { 0, 1 }, { 0, 1 } };		// {-1,0},   Cima    
	ArrayList<int[]> movimentos = new ArrayList<>();    												// {1,0}	};  Baixo                                      
                                                                                       
	public Localizador(Mapa mapa) {                                               
                                                                                       
		this.mapaEstados = mapa;		
		mapaSimbolos = new String[mapaEstados.getQtdLinhasMapa()][mapaEstados.getQtdColunasMapa()];
		
	}
	
	public void inicializaVariaveis(){
						
		sensorRigth = 1.0; //0.7                       			        
		pMove = 1.0; //0.8                       			                                                                              
		sensorWrong = 1.0 - sensorRigth;                                
		pStay = 1.0 - pMove;                                            	                                                                   
		
		geraMapaSimbolos();			
		
		pInit = 1.0 / qtdEstados;
		p = new double[mapaSimbolos.length][mapaSimbolos[0].length];
		
	}

	public void geraMapaSimbolos() {
		
		for (int i = 0; i < mapaEstados.getQtdLinhasMapa(); i++) {
			for (int j = 0; j < mapaEstados.getQtdColunasMapa(); j++) {
				mapaSimbolos[i][j] = String.valueOf(getSimbolo(mapaEstados.getPosMapa(i, j)));
				if(!mapaSimbolos[i][j].equals("#"))
					qtdEstados++;
			}
		}
		imprimeMapaSimbolos();

	}

	public void inicializaProbabilidades() {

		for (int i = 0; i < p.length; i++)
			for (int j = 0; j < p[0].length; j++)
				if(!mapaSimbolos[i][j].equals("#"))
					p[i][j] = pInit;			
		
		imprimeP();

	}

	public ArrayList<Estado> buscaLocalizacao() {

		int k = leituras.size()-1;			
			p = move(p, movimentos.get(k), pMove, pStay);	
			p = sense(p, mapaSimbolos, String.valueOf(getSimbolo(leituras.get(k))), sensorRigth, sensorWrong);
//			imprimeP();
					
		return buscaEstadosProvaveis(); // Envia apenas os estados com MAIORES probabilidades 

	}

	private double[][] move(double p[][], int motion[], double pMove, double pStay) {
		double aux[][] = new double[p.length][p[0].length];

		System.out.println("Move");
		for (int i = 0; i < p.length; i++) 
			for (int j = 0; j < p[i].length; j++) 
				aux[i][j] = (pMove * p[Math.floorMod((i - motion[0]), p.length)][Math.floorMod((j - motion[1]), p[i].length)]) + (pStay * p[i][j]);
			
		return aux;
	}

	private double[][] sense(double[][] p, String[][] mapaSimbolos, String measurement, double sensorRigth, double sensorWrong) {
		double aux[][] = new double[p.length][p[0].length];

		double s = 0.0;
		for (int i = 0; i < p.length; i++) {
			for (int j = 0; j < p[i].length; j++) {
				int hit = 0;
				if (measurement.equals(mapaSimbolos[i][j]))
					hit = 1;
				
				aux[i][j] = p[i][j] * (hit * sensorRigth + (1 - hit) * sensorWrong);
				s += aux[i][j];
			}
		}

		for (int i = 0; i < aux.length; i++)
			for (int j = 0; j < p[i].length; j++)
				aux[i][j] = aux[i][j] / s;

		return aux;
		
	}

	private char getSimbolo(Estado estado) {

		int contBytes = 0;
		if (estado.isOeste()) {
			contBytes += 1;
		}
		if (estado.isLeste()) {
			contBytes += 2;
		}
		if (estado.isSul()) {
			contBytes += 4;
		}
		if (estado.isNorte()) {
			contBytes += 8;
		}

		char simbolo = 0;
		switch (contBytes) {
			
			case 0:
				simbolo = (char)35; // #
				break;

			case 1:
				simbolo = (char)60; // <
				break;

			case 2:
				simbolo = (char)62; // >
				break;

			case 3:
				simbolo = (char)45; // -
				break;

			case 4:
				simbolo = (char)118; // v
				//simbolo = (char)66; // _ SUL
				break;

			case 5:
				simbolo = (char)69; //SUL,OESTE
				break;

			case 6:
				simbolo = (char)68; //SUL,LESTE
				break;

			case 7:
				simbolo = (char)84; // T
				break;
				
			case 8:
				simbolo = (char)94; // ^
				break;

			case 9:
				simbolo = (char)74; // OESTE,NORTE
				break;

			case 10:
				simbolo = (char)76; // L
				break;

			case 11:
				simbolo = (char)70; // NORTE,OESTE,LESTE
				break;

			case 12:
				simbolo = (char)124; // |
				break;

			case 13:
				simbolo = (char)75; // NORTE,SUL,OESTE
				break;

			case 14:
				simbolo = (char)72; // NORTE,SUL,LESTE
				break;

			case 15:
				simbolo = (char)43; // +
				break;

			default:
				break;
		}

		return simbolo;
	}
	
	private ArrayList<Estado> buscaEstadosProvaveis(){
		
		double maiorValor = p[0][0];
		for(int i = 0; i < mapaEstados.getQtdLinhasMapa(); i++){
			for(int j = 0; j < mapaEstados.getQtdColunasMapa(); j++){
				if(p[i][j] > maiorValor)
					maiorValor = p[i][j];
			}
		}
		
		ArrayList<Estado> estadosProvaveis = new ArrayList<>();
		for(int i = 0; i < mapaEstados.getQtdLinhasMapa(); i++){
			for(int j = 0; j < mapaEstados.getQtdColunasMapa(); j++){
				if(p[i][j] == maiorValor){
					estadosProvaveis.add(mapaEstados.getPosMapa(i, j));
				}
			}
		}
			
		return estadosProvaveis;
	}
	
	public void addLeitura(Estado leitura) {
		leituras.add(leitura);
	}

	public void addMovimento(int[] movimento) {
		movimentos.add(movimento);
	}

	
	public void imprimeMapaSimbolos() {
		System.out.println("mapaSimbolos:");
		for (int i = 0; i < mapaSimbolos.length; i++) {
			for (int j = 0; j < mapaSimbolos[0].length; j++) {
				//System.out.print(mapaSimbolos[i][j] + " | ");
				System.out.print("  " + mapaSimbolos[i][j] + "  | ");
			}
			System.out.println("");
		}
	}

	public void imprimeLeituras() {
		System.out.println("");
		System.out.println("Leituras:");
		for (int i = 0; i < leituras.size(); i++)
			System.out.println(getSimbolo(leituras.get(i)));
		System.out.println();
	}

	public void imprimeMovimentos() {
		System.out.println("");
		System.out.println("Movimentos:");

		for (int[] movimento : movimentos) {
			System.out.print("{");
			for(int j = 0; j < movimento.length; j++){
				System.out.print(movimento[j] + " ");
			}
			System.out.println("}");
		}
	}

	public void imprimeP() {
		System.out.println("P:");
		for (int i = 0; i < p.length; i++) {
			for (int j = 0; j < p[i].length; j++){
				//DecimalFormat df = new DecimalFormat("#.##");
				//String saida = df.format(p[i][j]);
				String saida = String.format("%.2f", p[i][j]);
				System.out.print(saida + " | ");
				//System.out.print(p[i][j] + " | ");
			}
			System.out.println("");
		}
	}
}
