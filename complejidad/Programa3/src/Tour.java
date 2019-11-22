import java.util.ArrayList;
import java.util.Random;

public class Tour{

	private ArrayList<Integer> tour;
	private double fitness;
	private int distancia;

	//Calcula el fitness de este candidato a solución y se lo asigna
	public void calculaFitness(int[][] matriz){
		int cuenta = this.getDistancia(matriz);
		fitness = 0.0;

		fitness = 1.0/cuenta; //una longitud mas corta equivale a un fitness mas grande
		fitness = fitness * 10000; //intento normalizar el fitnez con una escala mas grande.
	}

	public Tour reproduce(Tour pareja){
		Random random = new Random();
		//Se le aplica mutacion al hijo con una probabilidad de 0.2
		if(random.nextDouble() <= 0.2)
			return cruce(pareja).muta();

		return cruce(pareja);
	}

	//Se hace al azar uno de los 3 crossovers implementados
	//los tres tienen aprox. la misma probabilidad de ocurrir
	private Tour cruce(Tour pareja){
		Random random = new Random();
		double resultado = random.nextDouble();
		//NOTA: ambos Tours deben tener la misma longitud

		if(resultado>0.66)
			return partially_mapped_crossover(pareja);
		else if (resultado>0.33)
			return cycle_crossover(pareja);
		else
			return order_crossover(pareja);
	}

	//Se hace al azar una de las dos mutaciones implementadas
	//ambas tienen una probabilidad del 50% de ocurrir
	private Tour muta(){
		Random random = new Random();
		double resultado = random.nextDouble();

		if(resultado>0.5)
			return this.displacement_mutation();
		else
			return this.exchange_mutation();
	}

	//Una porcion de uno de los padres es mapeada en una porcion del otro padre
	//y la informacion restante es intercambiada.
	//NOTA: este operador puede regresar dos hijos, pero por simpleza solo regreso uno
	private Tour partially_mapped_crossover(Tour pareja){
		ArrayList<Integer> hijo = new ArrayList<Integer>();
		Random random = new Random();

		int n=this.tour.size();
		int a = random.nextInt(n-n/4);
		int b = a + n/4;
		ArrayList<Integer> porcion_1 = new ArrayList<Integer>(this.tour.subList(a,b));
		ArrayList<Integer> porcion_2 = new ArrayList<Integer>(pareja.getTour().subList(a,b));

		int indice=0;
		int aux=0;

		for(int i=0;i<a;i++){
			if(porcion_2.contains(this.tour.get(i))){
				aux = i;
				
				while(porcion_2.contains(this.tour.get(aux))){
					indice = porcion_2.indexOf(this.tour.get(aux));
					aux = this.tour.indexOf(porcion_1.get(indice));
				}

				hijo.add(porcion_1.get(indice));
			}else
				hijo.add(this.tour.get(i));
		}

		for(Integer i: porcion_2)
			hijo.add(i);

		for(int i=b;i<n;i++){
			if(porcion_2.contains(this.tour.get(i))){
				aux = i;
				
				while(porcion_2.contains(this.tour.get(aux))){
					indice = porcion_2.indexOf(this.tour.get(aux));
					aux = this.tour.indexOf(porcion_1.get(indice));
				}

				hijo.add(porcion_1.get(indice));
			}else
				hijo.add(this.tour.get(i));
		}

		Tour h = new Tour();
		h.setTour(hijo);
		h.setFitness(0);

		return h;
	}

	//Trata de crear un hijo de los padres donde cada posicion es ocupada por un elemento
	//correspondiente de uno u otro de los padres
	public Tour cycle_crossover(Tour pareja){
		int inicio =0;
		int fin = 0;
		int n = this.tour.size();

		int[] hijo = new int[n]; //Tour del hijo en forma de arreglo
		for (int i=0;i<n;i++)
			hijo[i] = -1;

		int aux = 0;
		while(aux!=-1){
			aux = cicloTour(inicio,fin,hijo,pareja);
			inicio = aux;
		}

		ArrayList<Integer> h = new ArrayList<Integer>();
		for (int i=0;i<n;i++)
			h.add(hijo[i]);

		Tour ht = new Tour();
		ht.setTour(h);
		ht.setFitness(0);

		return ht;
	}

	//Lleva acabo el proceso de ciclo en ambos Tour y regresa el proximo indicie de inicio
	private int cicloTour(int inicio,int fin, int[] hijo, Tour pareja){
		int n = this.tour.size();
		hijo[inicio] = this.tour.get(inicio);
		fin = pareja.getTour().indexOf(hijo[inicio]);
		
		while(inicio!=fin){
			hijo[fin] = this.tour.get(fin);
			fin = pareja.getTour().indexOf(hijo[fin]);
		}

		int aux=-1;
		for(int i=0;i<n;i++){
			if(hijo[i]==-1){
				aux=i;
				i=n;
			}
		}

		return aux;
	}

	//Construye un hijo escogiendo un subtour de un padre y preservando el orden relativo de las ciudades en el otro padre
	//NOTA: este operador puede regresar dos hijos, pero por simpleza solo regreso uno
	private Tour order_crossover(Tour pareja){
		int n = this.tour.size();
		Random random = new Random();

		int[] hijo = new int[n]; //Tour del hijo en forma de arreglo
		for (int i=0;i<n;i++)
			hijo[i] = -1;

		int a = random.nextInt(n-n/4);
		int b = a + n/4;
		ArrayList<Integer> porcion = new ArrayList<Integer>(this.tour.subList(a,b));
		ArrayList<Integer> resto = new ArrayList<Integer>();
		ArrayList<Integer> copia = new ArrayList<Integer>(this.tour);

		for (Integer i:porcion)
			copia.remove(i);

		resto = mergeSort(copia,pareja.getTour()); //Resto tiene las ciudades en this.tour que no estan en la porcion,
		//y en orden relativo a pareja

		int aux=0; //recorre resto
		for (int i=b;i<n;i++){
			hijo[i] = resto.get(aux);
			aux++;
		}

		for (int i=0;i<a;i++){
			hijo[i] = resto.get(aux);
			aux++;
		}

		for (int i=a;i<b;i++){
			hijo[i] = this.tour.get(i);
		}

		ArrayList<Integer> h = new ArrayList<Integer>();
		for (int i=0;i<n;i++)
			h.add(hijo[i]);

		Tour ht = new Tour();
		ht.setTour(h);
		ht.setFitness(0);

		return ht;
	}

	//regresa una lista ordenada (relativa a pareja) de Tours O(nlog(n))
	private ArrayList<Integer> mergeSort(ArrayList<Integer> l,ArrayList<Integer> pareja){
        if(l.size() == 0 || l.size() == 1)
            return new ArrayList<Integer>(l);

       ArrayList<Integer> li = new ArrayList<Integer>();
       ArrayList<Integer> ld = new ArrayList<Integer>();

        for(int i=0;i<l.size()/2;i++){
            li.add(l.get(i));
        }

        for(int i=l.size()/2;i<l.size();i++){
            ld.add(l.get(i));
        }

        li = mergeSort(li,pareja);
        ld = mergeSort(ld,pareja);
        return mezcla(li,ld,pareja);
    }

    //Mezcla dos listas en orden por orden relativo (decendente)
    private ArrayList<Integer> mezcla(ArrayList<Integer> li, ArrayList<Integer> ld,ArrayList<Integer> pareja){
        ArrayList<Integer> l = new ArrayList<Integer>();

        int i = 0;
        int d = 0;

        while(i < li.size() && d < ld.size()){
        	if(this.orden_relativo(li.get(i),ld.get(d),pareja) > 0){
        		l.add(li.get(i));
        		i++;
        	}else{
        		l.add(ld.get(d));
        		d++;
        	}
        }

        ArrayList<Integer> resto;

        if(i >= li.size())
        	//La lista de la izquierda se acabo
        	resto = new ArrayList<Integer>(ld.subList(d,ld.size()));
        else
        	//La lista de la derecha se acabo
        	resto = new ArrayList<Integer>(li.subList(i,li.size()));

        //Agregar al final de la nueva lista el resto
        for (Integer r:resto)
        	l.add(r);

        return l;
    }

    //regresa un numero positivo si i esta antes que j en pareja, de lo contrario regresa un numero negativo
	private int orden_relativo(int i, int j, ArrayList<Integer> pareja){
		return pareja.indexOf(j) - pareja.indexOf(i);
	}

	//AUXILIAR
	//Me dice si un arreglo a de longitud length contiene un elemnto x
	private boolean contiene_arreglo(int[] a,int length,int x){
		for(int i=0;i<length;i++){
			if(a[i] == x){
				return true;
			}
		}

		return false;
	}

	//Selecciona un subtour aleatorio lo elimina y lo inserta en un lugar aleatorio
	//NOTA: por simpleza mi implementacion no lo inserta en un lugar aleatorio, si no al final
	//considero que es lo suficientemente aproximado.
	private Tour displacement_mutation(){
		int n=this.tour.size();
		Random random = new Random();

		int a = random.nextInt(n-n/4);
		int b = a + n/4;
		ArrayList<Integer> porcion = new ArrayList<Integer>(this.tour.subList(a,b));
		ArrayList<Integer> hijo = new ArrayList<Integer>();

		for(int i=0;i<n;i++)
			if(!(i>=a && i<b))
				hijo.add(this.tour.get(i));
	
		for (Integer i:porcion)
			hijo.add(i);

		Tour h = new Tour();
		h.setTour(hijo);
		h.setFitness(0);

		return h;
	}

	//Selecciona dos ciudades del tour al azar, y las intercambia
	private Tour exchange_mutation(){
		ArrayList<Integer> hijo = new ArrayList<Integer>(this.tour);

		int i = 0;
		int j = 0;

		Random random = new Random();

		while(i==j){
			i = random.nextInt(hijo.size());
			j = random.nextInt(hijo.size());
		}

		int temp = hijo.get(i);
		hijo.set(i,hijo.get(j));
		hijo.set(j,temp);

		Tour h = new Tour();
		h.setTour(hijo);
		h.setFitness(0);

		return h;
	}

	//Genera un objeto Tour distinto a este, que tiene un orden de camino aleatorio
	//(Solo para uso de inicializacion, no destruye el Tour que llama este método, si no que regresa otro)
	public Tour generaTour(int size){
		Random random = new Random();
		tour = new ArrayList<Integer>();
		int next;

		for (int i=0;i<size;i++){
			next = random.nextInt(size);

			//si se repite un numero, se hace otro.
			if(this.tour.contains(next))
				i--;
			else
				this.tour.add(next);
		}

		//regresa una copia de este Tour;
		Tour ret_val = new Tour();
		ret_val.setTour(this.tour);
		ret_val.setFitness(0);
		return ret_val;
	}

	public int size(){
		return this.tour.size();
	}

	//Calcula longitud del tour
	private int getDistancia(int[][] matriz){
		int cuenta = 0;

		for (int i=0;i<this.tour.size()-1;i++)
			if(matriz[this.tour.get(i)][this.tour.get(i+1)] == -1)
				cuenta += matriz[this.tour.get(i+1)][this.tour.get(i)];
			else
				cuenta += matriz[this.tour.get(i)][this.tour.get(i+1)];

		if(matriz[this.tour.get(this.tour.size()-1)][this.tour.get(0)] == -1)
			cuenta += matriz[this.tour.get(this.tour.size()-1)][this.tour.get(0)];
		else
			cuenta += matriz[this.tour.get(0)][this.tour.get(this.tour.size()-1)];
		
		this.distancia = cuenta;
		return cuenta;
	}

	public int getDistancia(){
		return this.distancia;
	}

	public double getFitness(){
		return this.fitness;
	}

	public void setFitness(double f){
		this.fitness = f;
	}

	public ArrayList<Integer> getTour(){
		return this.tour;
	}

	public void setTour(ArrayList<Integer> t){
		this.tour = t;
	}

	public String toString(int[][] matriz){
		String s="[";
		for (int i=0;i<this.tour.size();i++){
			if(i==this.tour.size()-1)
    			s+=this.tour.get(i);
    		else
    			s+=this.tour.get(i)+",";
		}

		s += "] Distancia "+ this.getDistancia(matriz);

		return s;
	}

}