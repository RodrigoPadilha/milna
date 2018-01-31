package server;

public class MetricasRobo {
	
	int norte,sul,leste,oeste;
	
	int movimento[];

	public int getNorte() {
		return norte;
	}

	public int getSul() {
		return sul;
	}

	public int getLeste() {
		return leste;
	}

	public int getOeste() {
		return oeste;
	}

	public int[] getMovimento() {
		return movimento;
	}

	public void setNorte(int norte) {
		this.norte = norte;
	}

	public void setSul(int sul) {
		this.sul = sul;
	}

	public void setLeste(int leste) {
		this.leste = leste;
	}

	public void setOeste(int oeste) {
		this.oeste = oeste;
	}

	public void setMovimento(int[] movimento) {
		this.movimento = movimento;
	}
	
	

}
