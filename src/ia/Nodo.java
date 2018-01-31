package ia;

public class Nodo implements Comparable<Nodo>{
	
	private Estado estadoPai;
	private Estado estado;
	private Integer custo;
	
	public Estado getEstadoPai() {
		return estadoPai;
	}
	public void setEstadoPai(Estado estadoPai) {
		this.estadoPai = estadoPai;
	}
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	public Integer getCusto() {
		return custo;
	}
	public void setCusto(Integer custo) {
		this.custo = custo;
	}

	@Override
	public int compareTo(Nodo nodoOrder) {
      if (this.custo < nodoOrder.custo) {
          return -1;
      }
      if (this.custo > nodoOrder.custo) {
          return 1;
      }
      return 0;
  }
}
