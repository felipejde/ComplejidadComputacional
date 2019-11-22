import java.util.*;

public class Main {
  // Para evitar mensajes del compilador al crear arreglos con genéricos
  @SuppressWarnings("unchecked")
  public static void main(String[] args) {

    Random r = new Random();
    // Gráficas 5-partitas a 10-partitas
    int k = 5 + r.nextInt(6);
    // Número aleatorio de vértices (mínimo = 10, máximo = 100)
    int n = 10 + r.nextInt(101);
    // promedio de vértices por partición
    int p = n / k;

    System.out.printf("\nk = %d\n", k);
    System.out.printf("n = %d\n", n);

    int[][] matriz_adyacencias = new int[n][n];

    // Lista de los vértices de la gráfica
    ArrayList<Integer> listaVertices = new ArrayList<>();
    for (int i = 0; i < n; i++) { listaVertices.add(i); }

    // Desordenar los elementos de la lista
    Collections.shuffle(listaVertices);

    int indiceInicio = 0;
    int indiceFin = p;

    // Arreglo donde se guardan los elementos de cada partición
    ArrayList<Integer>[] particiones = (ArrayList<Integer>[])new ArrayList[k];

    /******** Genera una gráfica k-partita completa aleatoria. *********/

    // Acomoda los vértices hasta la posición p * k
    for(int i = 0; i < k; i++) {

      ArrayList<Integer> subLista =
        new ArrayList<>(listaVertices.subList(indiceInicio,indiceFin));

      // Todos los vértices de la lista están en la misma partición
      for(int vertice : subLista) {
        for(int j = 0; j < n; j++) {
          if(subLista.contains(j)) { continue; }
          matriz_adyacencias[vertice][j] = 1;
        }
      }

      particiones[i] = subLista;

      indiceInicio = indiceFin;
      indiceFin += p;
    }

    // Acomodar los vértices restantes en particiones existentes
    if(indiceInicio != n) {
      ArrayList<Integer> listaRestante =
        new ArrayList<>(listaVertices.subList(indiceInicio, n));

      for(int vertice : listaRestante) {
        int rand = r.nextInt(k);

        for(int v : particiones[rand]){ matriz_adyacencias[v][vertice] = 0; }
        matriz_adyacencias[vertice] = matriz_adyacencias[particiones[rand].get(0)];
        particiones[rand].add(vertice);
      }
    }

    /* ********************************************************************* */

    System.out.printf("\nV = {0, 1, 2, ..., %d}\n", n - 1);

    int i = 1;
    // Imprimir los vértices en cada partición
    for(ArrayList<Integer> ps : particiones) {
      String particion = ps.toString().replace("[","{").replace("]","}");
      System.out.printf("Particion %d: %s\n", i, particion);
      i++;
    }

    System.out.println("\nLa grafica k-partita es completa, por lo que cada vertice es vecino de todos los demas que no esten en su particion");

    Bui bui = new Bui(matriz_adyacencias);
    ArrayList<Integer> coloracion = bui.obtenerColoracion();
    int numColores = Collections.max(coloracion);
    System.out.println();

    // Imprimir la coloración de los vértices
    for (int c = 1; c <= numColores; c++) {

      ArrayList<Integer> tmp = new ArrayList<>();
      int contador = 0;
      for(int v : coloracion) {
        if(v == c)
          tmp.add(contador);
        contador++;
      }
      String lVertices = tmp.toString().replace("[","{").replace("]","}");
      System.out.printf("Color %d = %s\n",c, lVertices);
    }
  }
}
