import java.util.ArrayList;
import java.util.Random;

public class Poblacion{

	private ArrayList<Tour> poblacion;
	private int size;
	private int[][] matriz;

	public Poblacion (){
		poblacion = new ArrayList<Tour>();
		size = 0;
		matriz = null;
	}

	//Metodo que genera una poblacion inicial aleatoria de tamaño size con Tours de tamaño size_individuo
	private void genera(int size,int size_individuo){
		this.poblacion = new ArrayList<Tour>();
		this.size = size;
		Tour generedor = new Tour();

		for (int i =0;i<size;i++)
			this.poblacion.add(generedor.generaTour(size_individuo));
	}

	//Este metodo asigna el Fitness de todos los individuos de la poblacion
	//Debe ser llamado JUSTO DESPUES de genera()
	public void setFitness(){
		for (Tour t : this.poblacion)
			t.calculaFitness(this.matriz);
	}

	//Ordena la poblacion de Tours por Fitness (decendente)
	private void ordena(){
		this.poblacion = this.mergeSort(this.poblacion);
	}

	//Metodo que inicializa la poblacion con size individuos de tamaño size_individuo
	//resive tambien la matriz de adyacencias
	public void inicializa(int size, int size_individuo, int[][] matriz){
		this.setMatriz(matriz);
		this.genera(size,size_individuo);
		this.setFitness();
		this.ordena();
	}

	public Poblacion evoluciona(){
		Poblacion nueva = this.reemplaza(crea_nueva_generacion(selecciona(3*this.size/4)));
		return nueva;
	}

	//Selecciona un subconjunto de "size" Tours
	//Los Tours con un mayor fitness tienen mas PROBABILIDADES de ser seleccionados
	private ArrayList<Tour> selecciona(int size){
		ArrayList<Tour> seleccionados = new ArrayList<Tour>();
		Random random = new Random();
		Tour current;
		double probabilidad = random.nextDouble(); //genera un numero entre 0.0 y 1.0
		//si probabilidad es muy pequeño, solo los Tours con fitness muy alto podran ser seleccionados
		//mientras que si es muy grande, un Tour con bajo fitness puede ser seleccionado

		for (int i=0;i<size;i++) {
			probabilidad = random.nextDouble();
			//Selecciona un individuo de la poblacion al azar....
			current = this.poblacion.get(random.nextInt(this.size));

			//... y verifica se su fitness es suficiente para ser seleccionado
			if(current.getFitness()>probabilidad)
				seleccionados.add(current);
			else
				i--;
		}

		return seleccionados;
	}

	//Crea una nueva generacion de individuos reproduciendo a los individuos seleccionados
	private ArrayList<Tour> crea_nueva_generacion(ArrayList<Tour> seleccionados){
		ArrayList<Tour> nueva_generacion = new ArrayList<Tour>();

		for (int i=0;i<seleccionados.size()-1;i++){
			Tour hijo = seleccionados.get(i).reproduce(seleccionados.get(i+1));
			nueva_generacion.add(hijo);
		}
	
		return nueva_generacion;
	}

	//Reemplaza los individuos mas debiles de esta Poblacion por la nueva_generacion
	private Poblacion reemplaza(ArrayList<Tour> nueva_generacion){
		Poblacion nueva = new Poblacion();
		ArrayList<Tour> nuevo_array = new ArrayList<Tour>();

		//Los mejores Tours (con mayor fitness) permanecen en la nueva poblacion
		for (int i=0;i<(this.size-nueva_generacion.size());i++)
			nuevo_array.add(this.poblacion.get(i));

		//Los Tours de la nueva generacion reemplazan a los peores Tours de esta poblacion
		for(Tour t: nueva_generacion)
			nuevo_array.add(t);

		nueva.setMatriz(this.matriz);
		nueva.setSize(this.size);
		nueva.setPoblacion(nuevo_array);
		nueva.setFitness();
		nueva.ordena();
		return nueva;
	}

	//regresa una lista ordenada de Tours O(nlog(n))
	private ArrayList<Tour> mergeSort(ArrayList<Tour> l){
        if(l.size() == 0 || l.size() == 1)
            return new ArrayList<Tour>(l);

       ArrayList<Tour> li = new ArrayList<Tour>();
       ArrayList<Tour> ld = new ArrayList<Tour>();

        for(int i=0;i<l.size()/2;i++){
            li.add(l.get(i));
        }

        for(int i=l.size()/2;i<l.size();i++){
            ld.add(l.get(i));
        }

        li = mergeSort(li);
        ld = mergeSort(ld);
        return mezcla(li,ld);
    }

    //Mezcla dos listas en orden por Fitness (decendente)
    private ArrayList<Tour> mezcla(ArrayList<Tour> li, ArrayList<Tour> ld){
        ArrayList<Tour> l = new ArrayList<Tour>();

        int index_l = 0;
        int i = 0;
        int d = 0;

        while(i < li.size() && d < ld.size()){
        	if(li.get(i).getFitness() > ld.get(d).getFitness()){
        		l.add(li.get(i));
        		i++;
        	}else{
        		l.add(ld.get(d));
        		d++;
        	}
        	index_l++;
        }

        ArrayList<Tour> resto;

        if(i >= li.size())
        	//La lista de la izquierda se acabo
        	resto = new ArrayList<Tour>(ld.subList(d,ld.size()));
        else
        	//La lista de la derecha se acabo
        	resto = new ArrayList<Tour>(li.subList(i,li.size()));

        //Agregar al final de la nueva lista el resto
        for (Tour t:resto)
        	l.add(t);

        return l;
    }

    //Muestra la poblacion en forma de lista del fitness de sus individuos (PARA DEBUG)
    public String muestra(){
    	String s = "[";

    	for (int i=0;i<this.size;i++) {
    		if(i==this.size-1)
    			s+=this.poblacion.get(i).getFitness();
    		else
    			s+=this.poblacion.get(i).getFitness()+",";
    	}

    	s+= "]";

    	return s;
    }

    public String toString(){
    	String s = "[";

    	for (int i=0;i<this.size;i++) {
    		if(i==this.size-1)
    			s+=this.poblacion.get(i).toString(this.matriz);
    		else
    			s+=this.poblacion.get(i).toString(this.matriz)+",";
    	}

    	s+= "]";

    	return s;
    }

    //Regresa el mejor Tour de esta poblacion
    public Tour getMejor(){
    	return this.poblacion.get(0);
    }

	public void setPoblacion(ArrayList<Tour> poblacion){
		this.poblacion = poblacion;
	}

	public ArrayList<Tour> getPoblacion(){
		return this.poblacion;
	}

	public void setSize(int s){
		this.size=s;
	}

	public int getSize(){
		return this.size;
	}

	public void setMatriz(int[][] matriz){
		this.matriz=matriz;
	}

	public int[][] getMatriz(){
		return this.matriz;
	}

}