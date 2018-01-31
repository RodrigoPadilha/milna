package client;

public class TesteMetricas {
	
	boolean norte;
	boolean sul;
	boolean leste;
	boolean oeste;

	int movimento[];

	public boolean isNorte() {
		return norte;
	}

	public boolean isSul() {
		return sul;
	}

	public boolean isLeste() {
		return leste;
	}

	public boolean isOeste() {
		return oeste;
	}

	public int[] getMovimento() {
		return movimento;
	}

	public void setNorte(boolean norte) {
		this.norte = norte;
	}

	public void setSul(boolean sul) {
		this.sul = sul;
	}

	public void setLeste(boolean leste) {
		this.leste = leste;
	}

	public void setOeste(boolean oeste) {
		this.oeste = oeste;
	}

	public void setMovimento(int[] movimento) {
		this.movimento = movimento;
	}
	
	public void addMovimento(int [] mov){
		
		this.movimento = mov;
	}
	
}
