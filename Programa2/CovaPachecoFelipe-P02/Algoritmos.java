import java.util.*;
import java.text.DecimalFormat;

public class Algoritmos implements Runnable {

  public void run() {

    // Ejecutar el menú hasta que se seleccione la opción Salir
    while(true){
      /* Menú para seleccionar el algoritmo a ejecutar */
      System.out.println("************************************");
      System.out.println("* 1. Bin Packing 1.5-aproximado    *");
      System.out.println("* 2. Subset Sum epsilon-aproximado *");
      System.out.println("* 3. Salir                         *");
      System.out.println("************************************");
      System.out.print("Ingresa la opcion deseada: ");

      Scanner sc = new Scanner(System.in);
      String opcion = sc.nextLine();

      while(!opcion.matches("[123]")){
        System.out.println("\n*** Opcion incorrecta! ***\n");
        System.out.println("************************************");
        System.out.println("* 1. Bin Packing 1.5-aproximado    *");
        System.out.println("* 2. Subset Sum epsilon-aproximado *");
        System.out.println("* 3. Salir                         *");
        System.out.println("************************************");
        System.out.print("Ingresa la opcion deseada: ");
        opcion = sc.nextLine();
      }

      if(opcion.equals("1")) {
        // Tamaño de la lista de objetos
        System.out.print("\nIngresa el numero n de objetos: ");
        String n = sc.nextLine();

        while(!n.matches("[0-9]+")) {
          System.out.println("\nNo es un numero!");
          System.out.print("Ingresa el numero n de objetos: ");
          n = sc.nextLine();
        }
        approxBinPacking(Integer.parseInt(n));
      }

      if(opcion.equals("2")) {
        // Tamaño del conjunto
        System.out.print("\nIngresa el tamaño n del conjunto: ");
        String n = sc.nextLine();

        while(!n.matches("[0-9]+")) {
          System.out.println("\nNo es un numero!");
          System.out.print("Ingresa el numero n de objetos: ");
          n = sc.nextLine();
        }

        // Parametro epsilon
        System.out.print("\nIngresa el valor de epsilon: ");
        String epsilon = sc.nextLine();
        while(!epsilon.matches("0.[0-9]+")) {
          System.out.println("\nNo es un numero decimal valido!");
          System.out.print("Ingresa el numero n de objetos: ");
          epsilon = sc.nextLine();
        }

        approxSubsetSum(Integer.parseInt(n), Float.parseFloat(epsilon));
      }

      if(opcion.equals("3")){
        System.exit(1);
      }
    }
  }

  /**
  * Imprime el numero optimo de "bins" o contenedores de capacidad 1 en los que
  * n objetos con pesos s <= 1 caben y los objetos dentro de cada contenedor.
  * Utiliza la tecnica de first fit, es decir, acomoda los objetos en el primer
  * contenedor que encuentren.
  * @param n el numero de objetos
  * @return el numero optimo de contenedores para acomodar objetos
  */
  public void approxBinPacking(int n) {

    // La entrada se genera aleatoriamente
    Random rand = new Random();
    int contador = 0;
    // Arreglo de los pesos s de los objetos
    ArrayList<Float> objetos = new ArrayList<>();

    // Agregar los pesos de los elementos
    while(contador < n) {

      int d1 = rand.nextInt(9);
      int d2 = rand.nextInt(9);
      // Generar flotante aleatorio
      float f = d1 == 0.0f && d2 == 0.0f ? 1.0f : (d1/10f) + (d2/100f);
      DecimalFormat formatoDD = new DecimalFormat("#.##");
      f = Float.valueOf(formatoDD.format(f));
      objetos.add(f);
      contador++;
    }
    System.out.printf("\nEntrada\nPesos: %s\n",objetos);

    // Lista de contenedores
    ArrayList<Contenedor> contenedores = new ArrayList<>();

    /* Algoritmo */
    // Ordenar los pesos en orden descendente
    Collections.sort(objetos, Collections.reverseOrder());
    // Mientras haya objetos sin empacar
    for(float w : objetos) {
      boolean empacado = false;

      // Mientras haya más contenedores
      for(Contenedor cont : contenedores) {
        if(w + cont.obtenerPesoTotal() <= 1) {
          cont.agregaObjeto(w);
          empacado = true;
          break;
        }
      }

      // Si el objeto no ha sido empacado
      if(!empacado) {
        Contenedor nuevoCont = new Contenedor();
        nuevoCont.agregaObjeto(w);
        contenedores.add(nuevoCont);
      }
    }

    System.out.printf("\nSalida\nNumero de contenedores: %d\n",contenedores.size());
    int i = 1;
    for(Contenedor cont : contenedores) {
      System.out.printf("Contenedor %d: %s\n", i, cont);
      i++;
    }
    System.out.println();
  }

  /**
  * Dado un conjunto de numeros S de tamaño |S| = n y un entero t, imprime el
  * subconjunto S' tal que la suma de los elementos de S' sea igual a t. Si no
  * existe esa suma, devuelve el subconjunto con la suma más cercana a t.
  * @param n el tamaño del conjunto S
  * @param epsilon el parametro de aproximacion
  */
  private void approxSubsetSum(int n, float epsilon){

    // La entrada se genera aleatoriamente
    Random rand = new Random();
    int contador = 0;
    // Arreglo que representa el conjunto S
    ArrayList<Integer> conjunto = new ArrayList<>();

    // Agregar elementos al conjunto
    while(contador < n) {
      conjunto.add(rand.nextInt(999) + 1);
      contador++;
    }

    String conjuntoStr = conjunto.toString().replace("[","{").replace("]","}");
    System.out.printf("\nS = %s",conjuntoStr);

    // Valor de la suma que queremos encontrar
    // Se le pide al usuario después de ver el conjunto de numeros
    System.out.print("\nIngresa el valor de la suma (t): ");
    Scanner sc = new Scanner(System.in);
    String tStr = sc.nextLine();
    while(!tStr.matches("[0-9]+")) {
      System.out.println("\nNo es un numero!");
      System.out.print("Ingresa el numero n de objetos: ");
      tStr = sc.nextLine();
    }
    int t = Integer.parseInt(tStr);

    System.out.printf("\nEntrada\nS = %s\nt = %d\nepsilon = %f\n",
      conjuntoStr, t, epsilon);

    /* Algoritmo */

    ArrayList<Integer> actual = new ArrayList<>();
    actual.add(0);

    for(int i = 0; i < n; i++) {
      ArrayList<Integer> anterior = new ArrayList<>(actual);
      actual = uneListas(anterior, sumaATodos(anterior, conjunto.get(i)));
      actual = poda(actual, epsilon/(float)(2*n));

      for(int j = actual.size() - 1; j >= 0; j--) {
        if(actual.get(j) <= t)
          break;
        actual.remove(j);
      }
    }

    int suma = actual.get(actual.size() - 1);
    System.out.printf("\nSalida\n");
    if(t == suma)
      System.out.printf("Existe un subconjunto que sume %d? Si\n", t);
    else
      System.out.printf("Existe un subconjunto que sume %d? No\nSuma mas cercana: %d",
        t, suma);
    System.out.println();
  }

  /* Suma un numero m con todos los elementos de un arraylist */
  private ArrayList<Integer> sumaATodos(ArrayList<Integer> al, int m) {

    ArrayList<Integer> tmp = new ArrayList<>();
    for(Integer n : al)
      tmp.add(n + m);

    return tmp;
  }

  /* Une dos arraylists removiendo elementos duplicados y ordenando la lista */
  private ArrayList<Integer> uneListas(ArrayList<Integer> l1, ArrayList<Integer> l2) {

    ArrayList<Integer> tmp = new ArrayList<>(l1);
    tmp.addAll(l2);

    ArrayList<Integer> nueva = new ArrayList<Integer>(new HashSet<Integer>(tmp));
    Collections.sort(nueva);

    return nueva;
  }

  /* Poda un arraylist usando un parametro delta */
  private ArrayList<Integer> poda(ArrayList<Integer> l, float delta) {
    int m = l.size();
    ArrayList<Integer> lPrima = new ArrayList<>();

    int y = l.get(0);
    lPrima.add(y);
    int ultimo = y;

    for(int i = 1; i < m; i++) {
      y = l.get(i);
      if((float)y > ultimo * (1.0f + delta)) {
        lPrima.add(y);
        ultimo = y;
      }
    }

    return lPrima;
  }

}
