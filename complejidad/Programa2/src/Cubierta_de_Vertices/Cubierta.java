/*
* Algoritmo no deterministico polinomial
* para el problema de la cubierta de vertices
* Juan Carlos Montero Santiago
* 415122599
*/

import java.util.Random;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Cubierta{
	Grafica g; //Grafica g, parte de la entrada del problema
	int tam; //tamaño de la grafica

	/*
	* Constructor de esta clase,tam es el tamaño de la grafica a ser construida
	*/
	public Cubierta(int tam){
		g = generaGraficaAleatoria(tam);
		this.tam = tam;
	}

	/*
	* Este metodo genera una grafica aleatoria.
	*/
	private Grafica generaGraficaAleatoria(int tam){
		Grafica graf = new Grafica();

		for(int i=0 ; i<tam ; i++)
			graf.agrega(i);

		Random rnd = new Random();
		//generar como maximo 2*tam
		for(int i=0; i<2*tam; i++)
			try{
				graf.conecta(rnd.nextInt(tam),rnd.nextInt(tam));
			}catch(IllegalArgumentException e){
				//Si el programa llega aqui se intentaron conectar dos vertices ya conectados
			}

		return graf;
	}

	/*
	*	Este metodo ejecuta el algoritmo 1-aproximado que esta dado
	* 	en  Papadimitriou, Christos H. y Steiglitz, K. Combinatorial
	* 	Optimization. Algorithms and Complexity.
	*/
	public Lista<Integer> cubiertaAproximada(){
		Lista<Integer> cubierta = new Lista<Integer>();

		Lista<String> aristas = this.g.listaDeAdyacencias();

		while(!(aristas.getLongitud()==0)){
			String a = aristas.get(0);
			int coma = a.indexOf(',');
			String us = "" + a.substring(1,coma);
			String vs = "" + a.substring(coma+1,a.length()-1);

			int u = Integer.parseInt(us);
			int v = Integer.parseInt(vs);

			cubierta.agrega(u);
			cubierta.agrega(v);

			aristas = this.nuevaListaAristas(aristas,u,v);
		}

		return cubierta;
	}

	/*
	*	Simula la eliminacion de la grafica de los elementos u y v
	*/
	private Lista<String> nuevaListaAristas(Lista<String> aristas,int u,int v){

		String us = ""+u;
		String vs = ""+v;

		Iterator<String> it = aristas.iterator();

		while(it.hasNext()){
			String current = it.next();
			if(current.contains(us)||current.contains(vs))
				aristas.elimina(current);
		}

		return aristas;
	}

	/*
	* Este metodo muestra la grafica, representada por su conjunto de vertices
	* seguido de su lista de adyacencias. Ya que mis graficas no son dirigidas
	* las adyacencias (u,v) y (v,u) apareceran ambas en la lista.
	*/
	public void mostrar(){
		Lista<Integer> candidato = cubiertaAproximada();

		System.out.println("G = "+g.toStringElementos()+"\n"+g.listaDeAdyacencias().toString()+"\n"+"Candidato = "+candidato.toString()+"\n");
	}
}