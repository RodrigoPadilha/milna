package ia;

import java.util.ArrayList;
import java.util.Collections;

public class MelhorEscolha {
	
	private Grafo grafo;
	
	private ArrayList<Nodo> abertos = new ArrayList<>();
	private ArrayList<Nodo> fechados = new ArrayList<>();
	
	public MelhorEscolha(Grafo grafo){

		this.grafo = grafo;
		
	}
	
	public ArrayList<Estado> buscaHeuristica(){
		
		Nodo nodo = new Nodo();																					// Adiciona o nodo RAIZ a Lista de Prioridades
		nodo.setEstado(grafo.getMapa().getEstadoIni());
		nodo.setEstadoPai(null);
		nodo.setCusto(0);
		abertos.add(nodo);
		 
		while(!abertos.isEmpty()){
						
			Nodo nodoAtual = abertos.get(0); 																// Prepara o primeiro NODO da lista de Prioridades para busca do Objetivo 
			abertos.remove(0);
						
			if(nodoAtual.getEstado().getNome().equals(grafo.getMapa().getEstadoObjetivo().getNome())) 						// Testar se inicio e objetivo são nulos *****
				return buscaCaminhoSolucao(nodoAtual);
			
			for (Estado filho : grafo.getFilhos(nodoAtual.getEstado())) {									// Busca todos os filhos do NODO que está em análise 

				Nodo nodoFilho = new Nodo();																	// Prepara Filhos para inserir em lista de abertos ou fechados
				nodoFilho.setEstado(filho);
				nodoFilho.setEstadoPai(nodoAtual.getEstado());
				nodoFilho.setCusto(filho.getCusto());
				// o filho...
				if(!verificaLista(abertos,nodoFilho) && !verificaLista(fechados, nodoFilho)){	// SE o filho não está em abertos ou fechados, 
					abertos.add(nodoFilho);																		// atribui um valor heuístico para ele e acrescenta ele a Fila de Prioridades (abertos)				
				// o filho...
				}else if(!abertos.isEmpty() && verificaLista(abertos, nodoFilho)){				// SE o filho já está em abertos 
					int indice = getIndiceLista(abertos, nodoFilho);									//		SE o filho foi alcançado por um caminho mais curto é atribuído ao NODO que está na lista o caminho mais curto   
					if(nodoFilho.getCusto() < abertos.get(indice).getCusto())  						
						abertos.get(indice).setCusto(nodoFilho.getCusto());							// apenas ADD o custo e não o objeto inteiro!!!!
				// o filho...
				}else if(!fechados.isEmpty() && verificaLista(fechados, nodoFilho)){				// SE o filho está em fechados 
					int indice = getIndiceLista(fechados, nodoFilho);
					if(nodoFilho.getCusto() < fechados.get(indice).getCusto()){						//		SE o filho foi alcançado por um caminho mais curto 
						nodoFilho.setEstadoPai(fechados.get(indice).getEstadoPai());
						fechados.remove(fechados.get(indice));												//		o NODO é removido dos estados visitados (fechados)
						abertos.add(nodoFilho);																	//		o novo FILHO é adicionado a Fila de Prioridades
					}
				}
			}
			
			fechados.add(nodoAtual);			// Coloca o nodo analisado como um estado visitado						
			Collections.sort(abertos);			// reordenar abertos pela heurística			
		}
		
		return null;
	}

//	private ArrayList<Estado> getFilhos(Estado estado){
//		
//		ArrayList<Estado> filhos = new ArrayList<>();
//		
//		for (Entry<Estado, HashMap<Estado, Integer>> pai : grafo.getMatrizAdjac().entrySet()) {
//			if(pai.getKey().getNome().equals(estado.getNome())){
//				for (Estado filho : pai.getValue().keySet()) {					
//					filho.setCusto(pai.getValue().get(filho));		// Pega o custo e seta bo estado filho					
//					filhos.add(filho);
//				}
//			}
//		}
//				
//		return filhos;		
//		
//	}
	
	public ArrayList<Estado> buscaCaminhoSolucao(Nodo nodoObjetivo){
		
		ArrayList<Estado> caminhoSolucao = new ArrayList<>();
		Nodo nodo = nodoObjetivo;
		caminhoSolucao.add(nodoObjetivo.getEstado());
		
		boolean caminhoEncontrado = false;
		while(!caminhoEncontrado){
			boolean encontrouPai = false;
			
			for (Nodo nodoLista : fechados) {
				if(nodoLista.getEstado().getNome().equals(nodo.getEstadoPai().getNome())){
					caminhoSolucao.add(nodoLista.getEstado());
					nodo.setEstadoPai(nodoLista.getEstadoPai());
					encontrouPai = true;
					if(nodo.getEstadoPai() == null){
						caminhoEncontrado = true;
						break;
					}
				}
			}
			
			if(!encontrouPai){
				for (Nodo nodoLista : abertos) {
					if(nodoLista.getEstado().getNome().equals(nodo.getEstadoPai().getNome())){
						caminhoSolucao.add(nodoLista.getEstado());
						nodo.setEstadoPai(nodoLista.getEstadoPai());
						encontrouPai = true;
						if(nodo.getEstadoPai() == null){
							caminhoEncontrado = true;
							break;
						}
					}			
				}
			}
			
			if(nodo.getEstadoPai() == null)
				caminhoEncontrado = true;
		}
		
		imprimeCaminhObjetivo(caminhoSolucao);
		return caminhoSolucao;
		
	}
	
	private boolean verificaLista(ArrayList<Nodo> lista, Nodo nodoFilho){
		
		for (Nodo nodo : lista) {
			if(nodo.getEstado().getNome().equals(nodoFilho.getEstado().getNome()))
				return true;
		}
		return false;
	}
	
	public int getIndiceLista(ArrayList<Nodo> lista, Nodo nodoFilho){
		
		for(int i = 0; i <= lista.size(); i++){
			if(lista.get(i).getEstado().getNome().equals(nodoFilho.getEstado().getNome()))
				return i;
		}
		
		return 0;
	
	}	
	
	public void imprimeCaminhObjetivo(ArrayList<Estado> lista){
		System.out.print("Caminho Solução do estado " + lista.get(lista.size()-1).getNome() + ">> ");
		for (Estado estado : lista) 
			System.out.print(estado.getNome()  + "; ");
		
		System.out.println("");
		
	}
	
	public void imprimeLista(ArrayList<Nodo> lista){
		
		for (Nodo nodo : lista) 
			System.out.print(nodo.getEstado().getNome() + ":" + nodo.getCusto() + "; ");		
		
	}
	
	
}
