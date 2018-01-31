package server;

public class MensagemRobo {

	boolean norte, sul,leste,oeste;
//	int norte, sul,leste,oeste; 		CIRAR essas variáveis para receber os dados do PIC que não suporte boolean! 
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
	
	
}
