package ia;

public class Estado {

	private String nome;
	private boolean caminho;
	private boolean inicio;
	private boolean objetivo;
	private Integer custo;

	private boolean norte, sul, leste, oeste;
	
	public Estado(){
		
	}
	public Estado(Estado estado){
		this.nome 		= estado.getNome();
		this.caminho 	= estado.isCaminho(); 
		this.inicio 	= estado.isInicio();  
		this.objetivo	= estado.isObjetivo();
		this.custo		= estado.getCusto();   
		this.norte 		= estado.isNorte();
		this.sul 		= estado.isSul();
		this.leste	 	= estado.isLeste();
		this.oeste 		= estado.isOeste();

	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isCaminho() {
		return caminho;
	}

	public void setCaminho(boolean caminho) {
		this.caminho = caminho;
	}

	public boolean isInicio() {
		return inicio;
	}

	public void setInicio(boolean inicio) {
		this.inicio = inicio;
	}

	public boolean isObjetivo() {
		return objetivo;
	}

	public void setObjetivo(boolean objetivo) {
		this.objetivo = objetivo;
	}

	public Integer getCusto() {
		return custo;
	}

	public void setCusto(Integer custo) {
		this.custo = custo;
	}

	public boolean isNorte() {
		return norte;
	}

	public void setNorte(boolean norte) {
		this.norte = norte;
	}

	public boolean isSul() {
		return sul;
	}

	public void setSul(boolean sul) {
		this.sul = sul;
	}

	public boolean isLeste() {
		return leste;
	}

	public void setLeste(boolean leste) {
		this.leste = leste;
	}

	public boolean isOeste() {
		return oeste;
	}

	public void setOeste(boolean oeste) {
		this.oeste = oeste;
	}
	

}
