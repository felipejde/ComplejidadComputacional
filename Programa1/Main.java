import java.util.*;

public class Main {

  public static void main(String[] args) {

    int k = Integer.parseInt(args[0]);

    List<Integer> list = new LinkedList<>();
    Random rng = new Random();

    // Se generan número de vértices aleatorios
    int verticesNum = 5 + rng.nextInt(6);
    for(int i = 1; i <= verticesNum; i++) {
      list.add(i);
    }

    Grafica grafica = new Grafica(list);

    int randomV1 = 0, randomV2 = 0;
    int randomEdgeNum = 1 + rng.nextInt(((verticesNum*(verticesNum -1))/2) * 2/3);
    int c = 0;

    // Aqui se generan aristas aleatorias
    while(c < randomEdgeNum) {
      do {
        randomV1 = 1 + rng.nextInt(10);
        randomV2 = 1 + rng.nextInt(10);
        if(!grafica.existsEdge(randomV1, randomV2) && randomV1 != randomV2)
        grafica.addEdge(randomV1, randomV2);
      } while(randomV1 == randomV2);
      c++;
    }
    System.out.println("\n Gráfica aleatoria \n\n" + grafica.toString() + "\n");
    Clan solution = new Clan(grafica, verticesNum, k);
    System.out.println("\n\n");
    System.out.println("¿Es solución?");
    if(solution.isSolution()) {
      System.out.println("Sí\n");
    } else {
      System.out.println("No\n");
    }
  }
}
