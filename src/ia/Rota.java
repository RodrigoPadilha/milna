package ia;

import java.util.ArrayList;

public class Rota implements Comparable<Rota>{
	
	private int custo;
	private Estado inicio;
	private ArrayList<Estado> caminhoSolucao;
	
	public int getCusto() {
		return custo;
	}
	public void setCusto(int custo) {
		this.custo = custo;
	}
	public Estado getInicio() {
		return inicio;
	}
	public void setInicio(Estado inicio) {
		this.inicio = inicio;
	}
	public ArrayList<Estado> getCaminhoSolucao() {
		return caminhoSolucao;
	}
	public void setCaminhoSolucao(ArrayList<Estado> caminhoSolucao) {
		this.caminhoSolucao = caminhoSolucao;
	}
	
	@Override
	public int compareTo(Rota rotaOrder) {
		
		if (this.custo < rotaOrder.custo) 
		    return -1;                     
		
		if (this.custo > rotaOrder.custo) 
		    return 1;                                                        
		
		return 0;
	}
	
}
