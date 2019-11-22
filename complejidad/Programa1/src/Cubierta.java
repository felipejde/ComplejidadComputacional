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
	int k; //entero positivo k, parte de la entrada del problema
	int tam; //tamaño de la grafica

	/*
	* Constructor de esta clase, k es el entero positivo parte de la entrada,
	* tam es el tamaño de la grafica a ser construida, el booleano prueba
	* indica al contructor no contruir una grafica aleatoria, si no una grafica
	* cuya cubierta ya conozco. (para probar la parte verificadora)
	*/
	public Cubierta(int k, int tam, boolean prueba){
		if(prueba){
			g = generaGraficaPrueba();
			this.k = 3;
			this.tam = 6;
		}
		else{	
			g = generaGraficaAleatoria(tam);
			this.k = k;
			this.tam = tam;
		}
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
	* Este metodo genera una grafica cuya cobierta de vertices ya conozco
	* con fines de probar la parte verificadora
	*/
	private Grafica generaGraficaPrueba(){
		Grafica graf = new Grafica();

		for(int i=0 ; i<6 ; i++)
			graf.agrega(i);

		graf.conecta(0,1);
		graf.conecta(0,4);
		graf.conecta(1,4);
		graf.conecta(1,2);
		graf.conecta(4,3);
		graf.conecta(2,3);
		graf.conecta(3,5);

		return graf;
	}

	/*
	* Este metodo genera un candidato a solucion aleatorio, a menos
	* que el parametro prueba sea verdadero, en cuyo caso genera un 
	* candidato a solucion que es de hecho solucion para la grafica construida
	* en generaGraficaPrueba().
	* ESTA ES LA PARTE ADIVINADORA
	*/
	private Lista<Integer> generaCandidato(boolean prueba){
		Lista<Integer> candidato = new Lista<Integer>();

		if(prueba){
			candidato.agrega(0);
			candidato.agrega(1);
			candidato.agrega(3);
		}else{
			Random rnd = new Random();
			int nxt;
			int tam_cand = rnd.nextInt(tam-1) + 1; //numero aleatorio entre 1 y tam 
			for(int i=0; i<tam_cand; i++){
				nxt = rnd.nextInt(tam);
				if(!candidato.contiene(nxt))
					candidato.agrega(nxt);
			}
		}

		return candidato;
	}

	/*
	* Esta parte verifica si efectivamente el candidato a solucion
	* es o no solucion.
	*/
	private boolean verificadora(Lista<Integer> candidato){
		if(candidato.getLongitud() <= k){
			Lista<String> ad = g.listaDeAdyacencias();
			boolean flag= true;
			//Se por la implementacion que cada elemento de la lista ad es de la forma "(u,v)" 
			//por eso puedo estar seguro que charAt(1) y charAt(3) regresan los dos
			//elementos que componene la arista
			for(String t: ad)
				flag = flag && (candidato.contiene(Character.getNumericValue(t.charAt(1))) || 
					candidato.contiene(Character.getNumericValue(t.charAt(3))));

			return flag;
		}else{
			return false;
		}
	}

	/*
	* Este metodo muestra la grafica, representada por su conjunto de vertices
	* seguido de su lista de adyacencias. Ya que mis graficas no son dirigidas
	* las adyacencias (u,v) y (v,u) apareceran ambas en la lista.
	* NOTA: ESTE METODO EJECUTA LA PARTE ADIVINADORA Y VERIFICADORA DEL ALGORITMO.
	*/
	public void mostrar(boolean prueba){
		Lista<Integer> candidato = generaCandidato(prueba);
		boolean respuesta = verificadora(candidato);

		System.out.println("K = "+k+"\n"+"G = "+g.toStringElementos()+"\n"+g.listaDeAdyacencias().toString()+"\n"+"Candidato = "+candidato.toString()+"\n"+"Respuesta = "+respuesta);
	}
}