package ia;

public class Mapa {

	private Estado estadoIni;
	private Estado estadoObjetivo;
	private Estado[][] mapa;
	
	protected int qtdLinhasMapa,qtdColunasMapa;
	

	public Estado getEstadoIni() {
		return estadoIni;
	}

	public void setEstadoIni(Estado estadoIni) {
		this.estadoIni = estadoIni;
	}

	public Estado getEstadoObjetivo() {
		return estadoObjetivo;
	}

	public void setEstadoObjetivo(Estado estadoObjetivo) {
		this.estadoObjetivo = estadoObjetivo;
	}

	public Estado[][] getMapa() {
		return this.mapa;
	}

	public void setMapa(Estado[][] mapa) {
		this.mapa = mapa;
	}

	public int getQtdLinhasMapa() {
		return this.qtdLinhasMapa;
	}

	public int getQtdColunasMapa() {
		return this.qtdColunasMapa;
	}

	public void setQtdLinhasMapa(int qtdLinhasMapa) {
		this.qtdLinhasMapa = qtdLinhasMapa;
	}

	public void setQtdColunasMapa(int qtdColunasMapa) {
		this.qtdColunasMapa = qtdColunasMapa;
	}
	
	public Estado getPosMapa(int linha, int coluna){
		return this.mapa[linha][coluna];
	}
	
	
	public int[] getPosMapa(Estado estado){
		
		int[] posMapa = new int [2];
		for(int linha = 0; linha < qtdLinhasMapa; linha++)
			for(int coluna = 0; coluna < qtdColunasMapa; coluna ++)
				if(mapa[linha][coluna].getNome().equals(estado.getNome())){
					posMapa[0] = linha;
					posMapa[1] = coluna;
				}
				
		return posMapa;
	}
	
	public void setPosMapa(Estado estado){
		
		for(int linha = 0; linha < qtdLinhasMapa; linha++)
			for(int coluna = 0; coluna < qtdColunasMapa; coluna ++)
				if(mapa[linha][coluna].getNome().equals(estado.getNome()))
					mapa[linha][coluna] = estado;
				
	}
	
	public Estado getEstadoMapa(String nome){
		Estado ret = new Estado();
		for(int x = 0; x < qtdLinhasMapa; x++)
			for(int y = 0; y < qtdColunasMapa; y++)
				if(mapa[x][y].getNome().equals(nome))
					ret = mapa[x][y];
		
		return ret;
	}
	
	public void imprimePosMapa(int x, int y){
		
		System.out.println("Estado " + mapa[x][y].getNome());
		
	}
	
	public void imprimePosMapa(Estado estado){
		
		for(int x = 0; x < qtdLinhasMapa; x++)
			for(int y = 0; y < qtdColunasMapa; y++)
				if(mapa[x][y].getNome().equals(estado.getNome()))
					System.out.println("Estado " + mapa[x][y].getNome());
		
	}

}
