import java.util.*;

public class Bui {

  int[][] grafica;

  public Bui(int[][] matriz_adyacencias){
    this.grafica = matriz_adyacencias;
  }

  /**
  * Obtiene una coloración para una gráfica k-partita
  * @return la coloración de los vértices de la gráfica
  */
  public ArrayList<Integer> obtenerColoracion() {

    List<Integer> listaVertices = new LinkedList<>();
    for (int i = 0; i < grafica.length; i++) { listaVertices.add(i); }

    // Conjunto de vértices de la gráfica
    Set<Integer> vertices = new HashSet<>(listaVertices);
    // Conjunto X
    Set<Integer> x = new HashSet<>(listaVertices);
    // Conjunto Y
    Set<Integer> y = new HashSet<>();
    // Vector de colores actuales (CCV)
    ArrayList<Integer> vectorColoresActuales = new ArrayList<>(Arrays.asList(new Integer[grafica.length]));
    // Coloración
    int k = 0;

    Random rand = new Random();

    while(!x.isEmpty()) {

      k++;
      Set<Integer> conjuntoColores = new HashSet<>();

      int i = x.size() > 1 ? rand.nextInt(x.size() - 1) : 0;

      List<Integer> tmp = new LinkedList<>(x);
      i = tmp.get(i);

      vectorColoresActuales.set(i, k);

      x.remove(i);
      y.add(i);

      conjuntoColores.add(i);
      Set<Integer> vecinos = obtenerVecinos(i);
      vecinos.add(i);

      x.removeAll(vecinos);

      while(!x.isEmpty()) {

        i = x.size() > 1 ? rand.nextInt(x.size() - 1) : 0;
        tmp.clear();
        tmp.addAll(x);
        i = tmp.get(i);

        vectorColoresActuales.set(i, k);

        x.remove(i);

        y.add(i);

        conjuntoColores.add(i);

        vecinos = obtenerVecinos(i);
        vecinos.add(i);

        x.removeAll(vecinos);
      }

      vertices.removeAll(y);
      x.clear();
      x.addAll(vertices);
    }
    return vectorColoresActuales;
  }
  /*
  * Obtiene la vecindad de un vértice de la gráfica.
  */
  private Set<Integer> obtenerVecinos(int idVertice){
    Set<Integer> vecinos = new HashSet<>();
    int i = 0;
    for(int v : grafica[idVertice]) {
      if(v != 0)
        vecinos.add(i);
      i++;
    }
    return vecinos;
  }
}
