package ia;

import java.util.ArrayList;

public class Intelecto {

	private Grafo grafo;
	private Localizador localizador;
	private Roterizador roterizador;
	private Locomotor locomotor;
	private String proxMovimento;

	private boolean perdido = true;
	
	/**
	 * Método chamado quando for recebido uma mensagem do App (ou seja, apenas uma vez) */
	public void inicio(Mapa mapa){
		
		perdido = true;
		grafo = new Grafo(mapa);
		grafo.estruturaDados();							// Cria Matriz Adjascencia		
		localizador = new Localizador(mapa); 
		roterizador = new Roterizador(grafo);
		locomotor = new Locomotor();
		
		if(grafo.getMapa().getEstadoIni().getNome() == null){		// Se NÃO foi selecionado um ponto inicial tenta se localizar			
			localizador.inicializaVariaveis();
			localizador.inicializaProbabilidades();
		}else{																	// Novo mapa e sei minha localização
			perdido = false;													// Sei onde estou
			
			localizador.inicializaVariaveis();
			localizador.inicializaProbabilidades();
			Estado estadoIni = grafo.getMapa().getEstadoMapa(grafo.getMapa().getEstadoIni().getNome()); // Busca o estado com as direções preenchidas Norte = true, Sul = false, etc
			roterizador.buscaRotas(estadoIni);
			roterizador.rankingRotas();						
			comportamentoInteligente(estadoIni, new int []{0,0}); // Passa os valores para a primeira leitura do robô
			/*
			roterizador.buscaRotas(grafo.getMapa().getEstadoIni());
			
			roterizador.rankingRotas();						
			locomotor.definieMovimento(roterizador.getListaDeRotas());
			proxMovimento = locomotor.buscaDirecao(grafo.getMapa(),grafo.getMapa().getQtdLinhasMapa(), grafo.getMapa().getQtdColunasMapa());
		
			imprimeInfo();
			*/
		}
		//enviarParaApp( Todas as possibilidades); pode ser a probabilidade de estar em todos ou em apenas um estado	
		//enviarMovimentoParaRobo();	
	}
	
	/**
	 * Método chamado quando for recebido uma mensagem do robo */
	public void comportamentoInteligente(Estado leitura, int movimento []){	
										// Estou Perdido?
		localizador.addLeitura(leitura);
		localizador.addMovimento(movimento);		

//		ArrayList<Estado> possibilidades = localizador.buscaLocalizacao();
		ArrayList<Estado> possibilidades;		
		if(perdido){ 				// Sim 
			//			localizador.addLeitura(leitura);
			//			localizador.addMovimento(movimento);		

			//			ArrayList<Estado> possibilidades = localizador.buscaLocalizacao();
			
			possibilidades = localizador.buscaLocalizacao();
			roterizador.getListaDeRotas().clear();		// Apaga a lista de rotas para receber novas
			if(possibilidades.size() > 1){ 					 				 				
				for (Estado estado : possibilidades)	
					roterizador.buscaRotas(estado);
			}else{
				System.out.println("Localização concluída");
				roterizador.buscaRotas(possibilidades.get(0));
				perdido = false;
			}	
			roterizador.rankingRotas();			
			locomotor.definieMovimento(roterizador.getListaDeRotas());
			proxMovimento = locomotor.buscaDirecao(grafo.getMapa(),grafo.getMapa().getQtdLinhasMapa(), grafo.getMapa().getQtdColunasMapa());			
			imprimeInfo();

		}else{						// Não			
			locomotor.definieMovimento(roterizador.getListaDeRotas());				// Remove o ultimo estado da trilha de migalhas			 						
			if(!locomotor.jaChegou){				
				grafo.atualizaMapa(locomotor.getEstadoPresente(),locomotor.getEstadoFuturo(),leitura);							
				if(!locomotor.getEstadoFuturo().isCaminho()){						// O estado recebido confere com a migalha?
					roterizador.getListaDeRotas().clear();								// Limpa a lista que possui velhas rotas
					roterizador.buscaRotas(locomotor.getEstadoPresente());		// Inclui as rotas atualizadas
					roterizador.rankingRotas();											
					locomotor.definieMovimento(roterizador.getListaDeRotas());					
				}	
				localizador.geraMapaSimbolos();
				possibilidades = localizador.buscaLocalizacao();
				proxMovimento = locomotor.buscaDirecao(grafo.getMapa(), grafo.getMapa().getQtdLinhasMapa(), grafo.getMapa().getQtdColunasMapa());
			} else {
				possibilidades = localizador.buscaLocalizacao();
				proxMovimento = "Mov> Parada";
			}
			imprimeInfo();
		}		
		//enviarParaApp( Todas as possibilidades ou estaduPresente);	
		//enviarMovimentoParaRobo();	
	}
	
		
	private void imprimeInfo(){
		
		grafo.imprimeMatrizAdjasc();		// Matriz Adjascente
		localizador.imprimeMovimentos();	// Measurements
		localizador.imprimeLeituras();   // Movimentos
//		localizador.geraMapaSimbolos();	// mapaSimbolos
		localizador.imprimeMapaSimbolos();
		//if(perdido)
		localizador.imprimeP();				// P		
		System.out.println();
		System.out.println("Estado Presente: " + locomotor.getEstadoPresente().getNome());	// Estado presente
		System.out.println("Estado Futuro: " + locomotor.getEstadoFuturo().getNome());      // Estado Futuro  
		roterizador.imprimeRotas();		// caminhoSolucao/Trilha de Migalhas e custo				
		System.out.println(proxMovimento);
		
	}
	
	
	private void metodoTeste(){
		roterizador.rankingRotas();			
		locomotor.definieMovimento(roterizador.getListaDeRotas());
		proxMovimento = locomotor.buscaDirecao(grafo.getMapa(),grafo.getMapa().getQtdLinhasMapa(), grafo.getMapa().getQtdColunasMapa());			
		imprimeInfo();
	}

	public String getProxMovimento() {
		return proxMovimento;
	}
	
	
}
